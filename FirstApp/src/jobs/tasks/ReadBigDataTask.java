package jobs.tasks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.tools.ant.Task;
import org.eclipse.swt.widgets.Display;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.rolling.RollingFileAppender;
import connection.ConnectToDataBase;
import entity.Column;
import entity.Database;
import entity.LookUpCols;
import entity.ParamKeyMap;
import enums.JobType;
import extra.ModelProvider;
import views.ExecutePart;

public class ReadBigDataTask extends Task {

	Database database;
	Database lookUpDB;
	String query;
	List<LookUpCols> lookUpCols;
	List<ParamKeyMap> paramReplaceKeys;
	List<Column> columns;
	List<String> columnUnique;
	Map<String, String> columnValue;
	Map<String, String> tempKeyVal;
	JobType jobType;
	String tempSystemPath = System.getProperty("log_file_loc");
	File targetKeysValues;
	List<Integer> resultSetIndexes = new ArrayList<>();
	List<Integer> resultSetLookUpIndexes = new ArrayList<>();
	ResultSetMetaData resultSetMetaData;
	boolean isLookUpIndexSearch;

	static LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
	@SuppressWarnings("rawtypes")
	static RollingFileAppender rfAppender = new RollingFileAppender();
	static Logger logbackLogger;

	@SuppressWarnings("unchecked")
	public void execute() {
		rfAppender.setContext(loggerContext);
		rfAppender.setFile(System.getProperty("log_file_loc") + "/log/" + "rapid" + ".log");
		logbackLogger = loggerContext.getLogger("MainController");
		logbackLogger.addAppender(rfAppender);

		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyhhmmss");
		String time = dateFormat.format(now);

		if (jobType == JobType.SOURCE) {
			targetKeysValues = new File(tempSystemPath + "\\source" + time + ".txt");
			ModelProvider.INSTANCE.setSourcePath(targetKeysValues.getAbsolutePath());
		} else {
			targetKeysValues = new File(tempSystemPath + "\\target" + time + ".txt");
			ModelProvider.INSTANCE.setTargetPath(targetKeysValues.getAbsolutePath());
		}

		long starttime = System.currentTimeMillis();
		long endtime = 0;
		String key = "";
		String value = "";
		String lookUpId = "";
		String lookUpValue = "";
		int rowCount = 0;
		Connection connection = null;
		Statement stmt = null;
		//HiveQueryResultSet resultSet = null;
		ResultSet resultSet = null;
		query = replaceQueryWithParamValues(query, paramReplaceKeys);
		tempKeyVal = new TreeMap<>();

		try {
			connection = ConnectToDataBase.getConnection(database);
			stmt = connection.createStatement();
			stmt.execute("use " + database.getDatabaseName());
			// modify to support for handling huge no of records solution
			//resultSet = (HiveQueryResultSet) stmt.executeQuery(query);
			resultSet = stmt.executeQuery(query);
			resultSetMetaData = resultSet.getMetaData();

			resultSetIndexes = getResultSetIndexex(resultSetMetaData, false);
			if (!lookUpCols.isEmpty())
				resultSetLookUpIndexes = getResultSetIndexex(resultSetMetaData, true);
			while (resultSet.next()) {
				key = "";
				value = "";
				lookUpId = "";
				lookUpValue = "";
				int count = 0;
				int lookUpCount = 0;
				for (Integer colIndex : resultSetIndexes) {
					String colName = columns.get(count).getColumnName();
					if (!resultSetLookUpIndexes.contains(colIndex)) {
						String resultValue = resultSet.getString(colIndex);
						if (resultValue == null) {
							value += "NULL" + "#~";
						} else {
							value += resultValue + "#~";
						}
						if (columnUnique.contains(colName)) {
							key += resultValue + "#~";
						}
					} else {
						lookUpId = resultSet.getString(colIndex);
						lookUpValue = getLookUpValue(lookUpId, lookUpCols.get(lookUpCount).getLookUpQuery(), colName);
						if (columnUnique.contains(colName)) {
							key += lookUpValue + "#~";
						}
						value += lookUpValue + "#~";
						lookUpCount++;
					}
					count++;
				}
				tempKeyVal.put(key.substring(0, key.length() - 2), value.substring(0, value.length() - 2));
				if (rowCount % 100 == 0) {
					ModelProvider.INSTANCE.getExecutionStatus().get(0).setRecordsProcessed(Long.toString(rowCount));
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							ExecutePart.viewer.refresh();
						}
					});
				}
				if (rowCount % 10000 == 0) {

					logbackLogger.info("Map DB commit at Commit at " + rowCount);
					write(tempKeyVal);
					tempKeyVal = null;
					tempKeyVal = new TreeMap<>();
					System.out.println("Committed - " + rowCount);
				}
				rowCount++;
			}
			write(tempKeyVal);
			stmt.close();
			resultSet.close();
			connection.close();
		} catch (Exception err) {
			logbackLogger.error(err.getMessage(), err);
		} finally {
			try {
				stmt.close();
				resultSet.close();
				connection.close();
			} catch (SQLException err) {
				logbackLogger.error(err.getMessage(), err);
			}
		}
		endtime = System.currentTimeMillis();
		logbackLogger.info("MAP DONE IN " + (endtime - starttime) + "miliseconds");
		System.out.println("Done");
	}

	private List<Integer> getResultSetIndexex(ResultSetMetaData resultSetMetaData2, boolean isLookUpIndexSearch2)
			throws SQLException {
		List<Integer> returnValues = new ArrayList<>();

		if (!isLookUpIndexSearch2) {
			for (Column column : columns) {
				for (int i = 1; i <= resultSetMetaData2.getColumnCount(); i++) {
					// Check if u get array out of bound exception here
					// resultSetMetaData2 is appending column name with table
					// name
					if (column.getColumnName().equalsIgnoreCase(resultSetMetaData2.getColumnName(i).split("\\.")[1])) {
						returnValues.add(i);
						break;
					}
				}
			}
		} else {
			for (LookUpCols lookUpCol : lookUpCols) {
				for (int i = 1; i <= resultSetMetaData2.getColumnCount(); i++) {
					if (lookUpCol.getLookUpColName().equals(resultSetMetaData2.getColumnName(i))) {
						returnValues.add(i);
						break;
					}
				}
			}
		}
		return returnValues;
	}

	private String getLookUpValue(String lookUpId, String lookUpQuery, String lookUpColName) {
		Connection lookUpConnection = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		String returnValue = "";
		lookUpQuery = lookUpQuery.toUpperCase();
		lookUpColName = lookUpColName.toUpperCase();

		try {
			lookUpConnection = ConnectToDataBase.getConnection(lookUpDB);
			stmt = lookUpConnection.createStatement();
			lookUpQuery.replace("'" + lookUpQuery + "'", lookUpId);
			resultSet = stmt.executeQuery(lookUpQuery);
			while (resultSet.next()) {
				if (resultSet.getString(lookUpColName) == null) {
					returnValue = "";
				} else {
					returnValue = resultSet.getString(lookUpColName);
				}
				break;
			}
			stmt.close();
			resultSet.close();
			lookUpConnection.close();
		} catch (Exception err) {
			err.printStackTrace();
			logbackLogger.error(err.getMessage(), err);
		} finally {
			try {
				stmt.close();
				resultSet.close();
				lookUpConnection.close();
			} catch (SQLException err) {
				err.printStackTrace();
				logbackLogger.error(err.getMessage(), err);
			}
		}
		return returnValue;
	}

	private String replaceQueryWithParamValues(String query, List<ParamKeyMap> paramReplaceKeys2) {
		query = query.toUpperCase();
		for (ParamKeyMap paramKeyMap : paramReplaceKeys2) {
			String paramName = paramKeyMap.getParamName().toUpperCase();
			String paramValue = paramKeyMap.getParamValue();
			query = query.replace("'" + paramName + "'", "'" + paramValue + "'");
			logbackLogger.info("Replacing query parameter " + paramName + " with value - " + paramValue);
		}
		return query;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public List<String> getColumnUnique() {
		return columnUnique;
	}

	public void setColumnUnique(List<String> columnUnique) {
		this.columnUnique = columnUnique;
	}

	public Map<String, String> getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(Map<String, String> columnValue) {
		this.columnValue = columnValue;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	public Database getLookUpDB() {
		return lookUpDB;
	}

	public void setLookUpDB(Database lookUpDB) {
		this.lookUpDB = lookUpDB;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<LookUpCols> getLookUpCols() {
		return lookUpCols;
	}

	public void setLookUpCols(List<LookUpCols> lookUpCols) {
		this.lookUpCols = lookUpCols;
	}

	public List<ParamKeyMap> getParamReplaceKeys() {
		return paramReplaceKeys;
	}

	public void setParamReplaceKeys(List<ParamKeyMap> paramReplaceKeys) {
		this.paramReplaceKeys = paramReplaceKeys;
	}

	public JobType getJobType() {
		return jobType;
	}

	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}

	private void write(Map<String, String> tempkeyVal2) {
		try {
			FileWriter fileWriter = new FileWriter(targetKeysValues, true);
			for (Entry<String, String> resultValue : tempkeyVal2.entrySet()) {
				fileWriter.write(resultValue.getKey() + "#=" + resultValue.getValue() + "\n");
			}
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
