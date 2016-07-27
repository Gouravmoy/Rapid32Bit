package jobs.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.tools.ant.Task;
import org.eclipse.swt.widgets.Display;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.rolling.RollingFileAppender;
import entity.Column;
import enums.ColumnType;
import enums.JobType;
import enums.ProjectType;
import extra.ModelProvider;
import util.TimeUtil;
import views.ExecutePart;

public class ReadTextFileTask extends Task {
	String filePath;
	String seperator;
	Map<String, String> columnValue;
	List<String> keyValueList = new ArrayList<>();
	List<Column> columns;
	List<Column> columnsTarget;
	List<String> uniqueColumn;
	JobType jobType;
	ProjectType projectType;

	String tempSystemPath = System.getProperty("log_file_loc");
	File keysValuesFile;
	FileWriter fileWriter;

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
			keysValuesFile = new File(tempSystemPath + "\\source" + time + ".txt");
			ModelProvider.INSTANCE.setSourcePath(keysValuesFile.getAbsolutePath());
		} else {
			keysValuesFile = new File(tempSystemPath + "\\target" + time + ".txt");
			ModelProvider.INSTANCE.setTargetPath(keysValuesFile.getAbsolutePath());
		}
		try {
			fileWriter = new FileWriter(keysValuesFile, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long starttime = 0;
		long endtime = 0;
		File file = new File(filePath);
		FileReader fileReader;
		keyValueList = new ArrayList<>();
		List<Integer> uniqueColPos = new ArrayList<>();
		List<Integer> colindexex = new ArrayList<>();
		if (filePath.endsWith(".txt") || filePath.endsWith(".TXT")) {
			try {
				fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				String sCurrentLine = "";
				long rowCount = 0;
				String uniqueValue;
				String columnRowValue;
				starttime = System.currentTimeMillis();
				if (projectType == ProjectType.File_To_File && jobType == JobType.TARGET) {
					uniqueColPos = getColindexes(columnsTarget);
					colindexex = getColindexes(columns, columnsTarget);
				}
				while ((sCurrentLine = bufferedReader.readLine()) != null) {
					System.out.println(rowCount);
					uniqueValue = "";
					columnRowValue = "";
					String[] columnValues = sCurrentLine.split(seperator);
					int columCount = 0;
					if (!(projectType == ProjectType.File_To_File && jobType == JobType.TARGET)) {
						String value = "";
						for (Column column : columns) {
							value = columnValues[columCount];
							if (value != null) {
								if (value.length() != 0) {
									if (column.getColumnType() == ColumnType.Date) {
										value = populateDate(value);
									} else {
										if (uniqueColumn.contains(column.getColumnName())) {
											uniqueValue += value + "#~";
										}
										columnRowValue += value + "#~";
										columCount++;
									}
								} else {
									if (!(column.getColumnType() == ColumnType.Date)) {
										value = column.getDefaultValue();
									} else {
										value = populateDate(column.getDefaultValue());
									}
									if (uniqueColumn.contains(column.getColumnName())) {
										uniqueValue += value + "#~";
									}
									columnRowValue += value + "#~";
									columCount++;
								}
							} else {
								if (!(column.getColumnType() == ColumnType.Date)) {
									value = column.getDefaultValue();
								} else {
									value = populateDate(column.getDefaultValue());
								}
								if (uniqueColumn.contains(column.getColumnName())) {
									uniqueValue += value + "#~";
								}
								columnRowValue += value + "#~";
								columCount++;
							}
							if (uniqueColumn.contains(column.getColumnName())) {
								uniqueValue += value + "#~";
							}
							columnRowValue += value + "#~";
							columCount++;
						}
					} else {
						for (int colindex : uniqueColPos) {
							if (colindex == 0) {
								uniqueValue += columnValues[colindex] + "#~";
							}
						}
						for (int colindex : colindexex) {
							columnRowValue += columnValues[colindex] + "#~";
						}
					}
					keyValueList.add(uniqueValue.substring(0, uniqueValue.length() - 2) + "#="
							+ columnRowValue.substring(0, columnRowValue.length() - 2));
					/*
					 * columnValue.put(uniqueValue.substring(0,
					 * uniqueValue.length() - 2), columnRowValue.substring(0,
					 * columnRowValue.length() - 2));
					 */
					rowCount++;
					if (rowCount % 100 == 0) {
						ModelProvider.INSTANCE.getExecutionStatus().get(0).setRecordsProcessed(Long.toString(rowCount));
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								ExecutePart.viewer.refresh();
							}
						});
					}
					if (rowCount % 1000 == 0) {
						write(keyValueList);
						keyValueList = null;
						keyValueList = new ArrayList<>();
						/*
						 * if (jobType == JobType.SOURCE){
						 * CreateDb.sourceMapDB.commit(); } else if (jobType ==
						 * JobType.TARGET){ CreateDb.targetMapDB.commit(); }
						 */
						logbackLogger.debug("File Read Map Text DB Committed" + rowCount);
						System.out.println(rowCount);
					}
				}
				write(keyValueList);
				/*
				 * if (jobType == JobType.SOURCE) CreateDb.sourceMapDB.commit();
				 * else if (jobType == JobType.TARGET)
				 * CreateDb.targetMapDB.commit();
				 */
				fileWriter.close();
				endtime = System.currentTimeMillis();
				logbackLogger.debug("MAP DONE IN " + (endtime - starttime) + " miliseconds");
				System.out.println("Done");
				bufferedReader.close();
			} catch (FileNotFoundException err) {
				logbackLogger.error(err.getMessage(), err);
			} catch (IOException err) {
				logbackLogger.error(err.getMessage(), err);
			}
		} else if (filePath.endsWith(".csv") || filePath.endsWith(".CSV")) {
			Reader in;
			keyValueList = new ArrayList<>();
			starttime = System.currentTimeMillis();
			if (projectType == ProjectType.File_To_File && jobType == JobType.TARGET) {
				uniqueColPos = getColindexes(columnsTarget);
				colindexex = getColindexes(columns, columnsTarget);
			}
			try {
				in = new FileReader(filePath);
				Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
				String uniqueValue;
				String columnRowValue;
				int columCount;
				int rowCount;
				rowCount = 0;
				for (CSVRecord record : records) {
					uniqueValue = "";
					columnRowValue = "";
					columCount = 0;

					if (!(projectType == ProjectType.File_To_File && jobType == JobType.TARGET)) {
						String value = "";
						for (Column column : columns) {
							value = record.get(columCount);
							System.out.println(columCount + column.getColumnName() + column.getColumnType());
							/*
							 * if(value.equals("ER")){
							 * System.out.println("Here"); }
							 */
							if (value != null) {
								if (value.length() != 0) {
									if (column.getColumnType() == ColumnType.Date) {
										value = populateDate(value);
									}
									if (uniqueColumn.contains(column.getColumnName())) {
										uniqueValue += value + "#~";
									}
									columnRowValue += value + "#~";
									columCount++;
								} else {
									if (!(column.getColumnType() == ColumnType.Date)) {
										value = column.getDefaultValue();
									} else {
										value = populateDate(column.getDefaultValue());
									}
									if (uniqueColumn.contains(column.getColumnName())) {
										uniqueValue += value + "#~";
									}
									columnRowValue += value + "#~";
									columCount++;
								}
							} else {
								if (!(column.getColumnType() == ColumnType.Date)) {
									value = column.getDefaultValue();
								} else {
									value = populateDate(column.getDefaultValue());
								}
								if (uniqueColumn.contains(column.getColumnName())) {
									uniqueValue += value + "#~";
								}
								columnRowValue += value + "#~";
								columCount++;
							}
							/*
							 * if
							 * (uniqueColumn.contains(column.getColumnName())) {
							 * uniqueValue += value + "#~"; } columnRowValue +=
							 * value + "#~"; columCount++;
							 */
						}
					} else {
						for (int colindex : uniqueColPos) {
							uniqueValue += record.get(colindex) + "#~";
						}
						for (int colindex : colindexex) {
							columnRowValue += record.get(colindex) + "#~";
						}
					}
					/*
					 * columnValue.put(uniqueValue.substring(0,
					 * uniqueValue.length() - 2), columnRowValue.substring(0,
					 * columnRowValue.length() - 2));
					 */

					keyValueList.add(uniqueValue.substring(0, uniqueValue.length() - 2) + "#="
							+ columnRowValue.substring(0, columnRowValue.length() - 2));

					rowCount++;
					if (rowCount % 100 == 0) {
						ModelProvider.INSTANCE.getExecutionStatus().get(0).setRecordsProcessed(Long.toString(rowCount));
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								ExecutePart.viewer.refresh();
							}
						});
					}
					if (rowCount % 1000 == 0) {
						write(keyValueList);
						keyValueList = null;
						keyValueList = new ArrayList<>();
						/*
						 * if (jobType == JobType.SOURCE){
						 * CreateDb.sourceMapDB.commit(); } else if (jobType ==
						 * JobType.TARGET){ CreateDb.targetMapDB.commit(); }
						 */
						logbackLogger.debug("File Read Map Text DB Committed" + rowCount);
						System.out.println(rowCount);
					}
					/*
					 * if (rowCount % 10000 == 0) { if (jobType ==
					 * JobType.SOURCE) CreateDb.sourceMapDB.commit(); else if
					 * (jobType == JobType.TARGET)
					 * CreateDb.targetMapDB.commit(); logbackLogger.debug(
					 * "File Read Map CSV DB Committed" + rowCount);
					 * System.out.println(rowCount); }
					 */
				}
				write(keyValueList);
				/*
				 * if (jobType == JobType.SOURCE) CreateDb.sourceMapDB.commit();
				 * else if (jobType == JobType.TARGET)
				 * CreateDb.targetMapDB.commit();
				 */
				fileWriter.close();
				endtime = System.currentTimeMillis();
				logbackLogger.debug("MAP DONE IN " + (endtime - starttime) + " miliseconds");
			} catch (IOException err) {
				logbackLogger.error(err.getMessage(), err);
				err.printStackTrace();
			}

		}

	}

	private String populateDate(String value) {
		SimpleDateFormat finalDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		SimpleDateFormat dateFormat;
		String format = TimeUtil.determineDateFormat(value);
		if (format != null) {
			dateFormat = new SimpleDateFormat(TimeUtil.determineDateFormat(value));
			try {
				Date date = dateFormat.parse(value);
				value = finalDateFormat.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
				value = "Date Format Exception";
			}
		} else {
			value = "";
		}

		return value;
	}

	private List<Integer> getColindexes(List<Column> columns) {
		List<Integer> colindexex = new ArrayList<>();
		int count = 0;
		for (Column column : columns) {
			if (uniqueColumn.contains(column.getColumnName())) {
				colindexex.add(count);
			}
			count++;
		}
		return colindexex;
	}

	private List<Integer> getColindexes(List<Column> columns, List<Column> columnsTarget) {
		List<Integer> colindexex = new ArrayList<>();
		int count = 0;
		for (Column column : columns) {
			count = 0;
			for (Column unOrderedColumn : columnsTarget) {
				if (unOrderedColumn.getColumnName().equals(column.getColumnName())) {
					colindexex.add(count);
				} else {
					count++;
				}
			}
		}
		return colindexex;
	}

	private void write(List<String> keyValueList2) {
		try {
			for (String line : keyValueList2) {
				fileWriter.write(line + "\n");
			}
			fileWriter.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSeperator() {
		return seperator;
	}

	public void setSeperator(String seperator) {
		this.seperator = seperator;
	}

	public Map<String, String> getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(Map<String, String> columnValue) {
		this.columnValue = columnValue;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public List<String> getUniqueColumn() {
		return uniqueColumn;
	}

	public void setUniqueColumn(List<String> uniqueColumn) {
		this.uniqueColumn = uniqueColumn;
	}

	public JobType getJobType() {
		return jobType;
	}

	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}

	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	public List<Column> getColumnsTarget() {
		return columnsTarget;
	}

	public void setColumnsTarget(List<Column> columnsTarget) {
		this.columnsTarget = columnsTarget;
	}
}
