package jobs.tasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.tools.ant.Task;
import org.eclipse.swt.widgets.Display;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.rolling.RollingFileAppender;
import entity.Column;
import extra.ModelProvider;
import views.ExecutePart;

public class GenerateCSVTask extends Task {
	String fileOutputPath;
	// Map<String, String> resultValues;
	// Map<String, String> errorValues;
	final String newLine = "\n";
	String matchedFile;
	String errorFile;
	List<Column> columns;
	String scenarioName;

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

		BufferedReader matchedReader;
		BufferedReader errorReader;
		BufferedWriter passBuffWriter = null;
		BufferedWriter partialMismatchBuffWriter = null;
		BufferedWriter failBuffWriter = null;

		int matchedCounter = 0;
		int mismatchCounter = 0;
		int rowCount = 0;
		long starttime = System.currentTimeMillis();
		long endtime = 0;

		File outputFolder = new File(fileOutputPath);
		if (!outputFolder.exists()) {
			outputFolder.mkdirs();
		}
		String line = "";
		try {
			matchedReader = new BufferedReader(new FileReader(ModelProvider.INSTANCE.getMatchedPath()));
			//matchedReader = new BufferedReader(new FileReader("C:\\Users\\m1026335\\Desktop\\Test\\21st July\\error\\matched.txt"));
			errorReader = new BufferedReader(new FileReader(ModelProvider.INSTANCE.getErrorPath()));
			passBuffWriter = new BufferedWriter(
					new FileWriter(new File(outputFolder + "\\" + scenarioName + "_Pass" + ".csv")));
			partialMismatchBuffWriter = new BufferedWriter(
					new FileWriter(new File(outputFolder + "\\" + scenarioName + "_Mismatch" + ".csv")));
			failBuffWriter = new BufferedWriter(
					new FileWriter(new File(outputFolder + "\\" + scenarioName + "_Fail" + ".csv")));
			for (Column column : columns) {
				passBuffWriter.write(column.getColumnName() + "," + "SOURCE ," + "TARGET ,");
				partialMismatchBuffWriter.write(column.getColumnName() + "," + "SOURCE ," + "TARGET ,");
				failBuffWriter.write(column.getColumnName() + "," + "SOURCE ," + "TARGET ,");
			}
			passBuffWriter.append("\n");
			partialMismatchBuffWriter.append("\n");
			failBuffWriter.append("\n");
			while ((line = matchedReader.readLine()) != null) {
				if (line.length() > 1) {
					rowCount++;
					if (rowCount % 100 == 0) {
						ModelProvider.INSTANCE.getExecutionStatus().get(0).setRecordsProcessed(Long.toString(rowCount));
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								ExecutePart.viewer.refresh();
							}
						});
					}
					line = line.split("#=")[1];
					String[] colValues = line.split("#~");
					if (line.contains("false")) {
						mismatchCounter++;
						String status, source, target;

						for (String colValue : colValues) {
							if (colValue.split("\\|").length > 0) {
								if (colValue.split("\\|").length > 3)
									status = colValue.split("\\|")[3];
								else
									status = "false";
								if (colValue.split("\\|").length > 1)
									source = colValue.split("\\|")[1];
								else
									source = "";
								if (colValue.split("\\|").length > 2)
									target = colValue.split("\\|")[2];
								else
									target = "";
								if (source.contains("\\,")) {
									source += "\"" + source + "\"";
								}
								if (target.contains("\\,")) {
									target += "\"" + source + "\"";
								}
								partialMismatchBuffWriter
										.write(status.toUpperCase() + "," + source + "," + target + ",");
							}
						}
						partialMismatchBuffWriter.write("\n");
					} else {
						matchedCounter++;
						String status, source, target;
						for (String colValue : colValues) {
							if (colValue.split("\\|").length > 0) {
								if (colValue.split("\\|").length > 3)
									status = colValue.split("\\|")[3];
								else
									status = "false";
								if (colValue.split("\\|").length > 1)
									source = colValue.split("\\|")[1];
								else
									source = "";
								if (colValue.split("\\|").length > 2)
									target = colValue.split("\\|")[2];
								else
									target = "";
								if (source.contains(",")) {
									source = "\"" + source + "\"";
								}
								if (target.contains(",")) {
									target = "\"" + target + "\"";
								}
								passBuffWriter.write(status.toUpperCase() + "," + source + "," + target + ",");
							}
						}
						passBuffWriter.write("\n");
					}
					if (matchedCounter % 1000 == 0 || mismatchCounter % 1000 == 0) {
						passBuffWriter.flush();
						partialMismatchBuffWriter.flush();
					}
				}
			}
			passBuffWriter.flush();
			passBuffWriter.close();
			partialMismatchBuffWriter.flush();
			partialMismatchBuffWriter.close();

			while ((line = errorReader.readLine()) != null) {
				if (line.length() > 0) {
					rowCount++;
					if (rowCount % 100 == 0) {
						ModelProvider.INSTANCE.getExecutionStatus().get(0).setRecordsProcessed(Long.toString(rowCount));
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								ExecutePart.viewer.refresh();
							}
						});
					}
					line = line.split("#=")[1];
					matchedCounter++;
					String status, source, target;
					String[] colValues = line.split("#~");
					for (String colValue : colValues) {
						status = colValue.split("\\|")[3];
						source = colValue.split("\\|")[1];
						target = colValue.split("\\|")[2];
						if (source.contains("\\,")) {
							source += "\"" + source + "\"";
						}
						if (target.contains("\\,")) {
							target += "\"" + source + "\"";
						}
						failBuffWriter.write(status.toUpperCase() + "," + source + "," + target + ",");
					}
					failBuffWriter.write("\n");
					if (rowCount % 10000 == 0) {
						failBuffWriter.flush();
					}
				}
			}
			failBuffWriter.flush();
			failBuffWriter.close();
			endtime = System.currentTimeMillis();
			logbackLogger.info("CSV Report Generated in " + (endtime - starttime) + "miliseconds");
		} catch (IOException e) {
			logbackLogger.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			logbackLogger.error("Error in line ->" + line);
			logbackLogger.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (Exception e) {
			logbackLogger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public String getFileOutputPath() {
		return fileOutputPath;
	}

	public void setFileOutputPath(String fileOutputPath) {
		this.fileOutputPath = fileOutputPath;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public String getMatchedFile() {
		return matchedFile;
	}

	public void setMatchedFile(String matchedFile) {
		this.matchedFile = matchedFile;
	}

	public String getErrorFile() {
		return errorFile;
	}

	public void setErrorFile(String errorFile) {
		this.errorFile = errorFile;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

}
