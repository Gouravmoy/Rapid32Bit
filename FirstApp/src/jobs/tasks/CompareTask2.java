package jobs.tasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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

public class CompareTask2 extends Task {

	final String newLine = "\n";
	String sourceSortedFile;
	String targetSortedFile;
	List<Column> columnList;
	String tempSystemPath = System.getProperty("log_file_loc");

	static LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
	@SuppressWarnings("rawtypes")
	static RollingFileAppender rfAppender = new RollingFileAppender();
	static Logger logbackLogger;

	@SuppressWarnings("unchecked")
	public void execute() {

		rfAppender.setContext(loggerContext);
		rfAppender.setFile(System.getProperty("log_file_loc") + "/log/" + "rapid" + ".log");
		logbackLogger = loggerContext.getLogger("CompareTask2");
		logbackLogger.addAppender(rfAppender);

		rfAppender.setContext(loggerContext);
		rfAppender.setFile(System.getProperty("log_file_loc") + "/log/" + "rapid" + ".log");
		logbackLogger = loggerContext.getLogger("MainController");
		logbackLogger.addAppender(rfAppender);

		try {
			logbackLogger.info("Started01");
			String matchedOutputFile = tempSystemPath + "\\matched.txt";
			ModelProvider.INSTANCE.setMatchedPath(matchedOutputFile);
			String errorOutputFile = tempSystemPath + "\\error.txt";
			ModelProvider.INSTANCE.setErrorPath(errorOutputFile);
			String keyValueSeparator = "#=";
			logbackLogger.info("Started1");
			BufferedReader sourceReader;
			BufferedReader targetReader;
			BufferedWriter matchedWriter;
			BufferedWriter errorOutputWriter;
			logbackLogger.info("Started2");
			logbackLogger.info("Started3");
			long starttime = System.currentTimeMillis();
			long endtime = 0;

			sourceReader = new BufferedReader(new FileReader(new File(ModelProvider.INSTANCE.getSourceSortedPath())));
			targetReader = new BufferedReader(new FileReader(new File(ModelProvider.INSTANCE.getTargetSortedpath())));
			// sourceReader = new BufferedReader(new FileReader(new File(
			// "C:\\Users\\m1026335\\Desktop\\Test\\21st
			// July\\target21072016015013_Sorted\\source21072016015013_Sorted.txt")));
			// targetReader = new BufferedReader(new FileReader(new File(
			// "C:\\Users\\m1026335\\Desktop\\Test\\21st
			// July\\target21072016015013_Sorted\\target21072016015013_Sorted.txt")));

			logbackLogger.info("Source Sorted Path - " + ModelProvider.INSTANCE.getSourceSortedPath());
			logbackLogger.info("Target Sorted Path - " + ModelProvider.INSTANCE.getTargetSortedpath());

			matchedWriter = new BufferedWriter(new FileWriter(new File(matchedOutputFile)));
			errorOutputWriter = new BufferedWriter(new FileWriter(new File(errorOutputFile)));
			String sourceLine = "";
			String targetLine = "";
			String sourceKey = "";
			String targetKey = "";
			String sourceValue = "";
			String targetValue = "";

			int matchedCounter = 0;
			int sourceUnMatchedCounter = 0;
			int targetUnmatchedCounter = 0;
			int rowCount = 0;

			logbackLogger.info("Started4");
			boolean isReadSource = true;
			boolean isReadTarget = true;
			while (true) {
				rowCount++;
				if (rowCount % 100 == 0) {
					logbackLogger.info("Commit at" + rowCount);
					ModelProvider.INSTANCE.getExecutionStatus().get(0).setRecordsProcessed(Long.toString(rowCount));
					matchedWriter.flush();
					errorOutputWriter.flush();
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							ExecutePart.viewer.refresh();
						}
					});
				}
				if (isReadSource == true) {
					sourceLine = sourceReader.readLine();
				}
				if (isReadTarget == true) {
					targetLine = targetReader.readLine();
				}
				if (sourceLine == null) {
					boolean status = false;
					while (targetLine != null) {
						int colCount = 0;
						targetKey = targetLine.split(keyValueSeparator, -1)[0];
						StringBuilder resultValue = new StringBuilder();
						targetValue = targetLine.split(keyValueSeparator, -1)[1];
						String targetValues[] = targetValue.split("#~", -1);
						for (Column column : columnList) {
							resultValue.append(column.getColumnName() + "|" + "NO VALUE" + "|" + targetValues[colCount]
									+ "|" + status + "#~");
							colCount++;
						}

						errorOutputWriter
								.write(targetKey + "#=" + resultValue.substring(0, resultValue.length() - 2) + newLine);
						errorOutputWriter.flush();
						targetLine = targetReader.readLine();
					}
					break;
				}
				if (targetLine == null) {
					boolean status = false;
					while (sourceLine != null) {
						int colCount = 0;
						sourceKey = sourceLine.split(keyValueSeparator, -1)[0];
						StringBuilder resultValue = new StringBuilder();
						sourceValue = sourceLine.split(keyValueSeparator, -1)[1];
						String sourceValues[] = sourceValue.split("\\#~", -1);
						for (Column column : columnList) {
							try {
								resultValue.append(column.getColumnName() + "|" + sourceValues[colCount] + "|"
										+ "NO VALUE" + "|" + status + "#~");
							} catch (ArrayIndexOutOfBoundsException e) {
								e.printStackTrace();
							}
							colCount++;
						}

						errorOutputWriter
								.write(sourceKey + "#=" + resultValue.substring(0, resultValue.length() - 2) + newLine);
						errorOutputWriter.flush();
						sourceLine = sourceReader.readLine();
					}
					break;
				}

				sourceKey = sourceLine.split(keyValueSeparator, -1)[0];
				targetKey = targetLine.split(keyValueSeparator, -1)[0];
				int result = sourceKey.compareTo(targetKey);

				// remove
				/*
				 * columnList = new ArrayList<>(); for (int i = 0; i < 198; i++)
				 * { columnList.add(new Column("C" + i)); }
				 */

				if (result < 0) {
					// source before Target
					sourceUnMatchedCounter++;
					isReadSource = true;
					isReadTarget = false;
					int colCount = 0;
					boolean status = false;

					StringBuilder resultValue = new StringBuilder();
					sourceValue = sourceLine.split(keyValueSeparator, -1)[1];
					String sourceValues[] = sourceValue.split("#~", -1);

					for (Column column : columnList) {
						resultValue.append(column.getColumnName() + "|" + sourceValues[colCount] + "|" + "NO VALUE"
								+ "|" + status + "#~");
						colCount++;
					}
					errorOutputWriter
							.write(sourceKey + "#=" + resultValue.substring(0, resultValue.length() - 2) + newLine);
					errorOutputWriter.flush();
					if (sourceUnMatchedCounter % 1000 == 0) {
						errorOutputWriter.flush();
					}
				} else if (result > 0) {
					// target before Source
					targetUnmatchedCounter++;
					isReadTarget = true;
					isReadSource = false;

					int colCount = 0;
					boolean status = false;
					StringBuilder resultValue = new StringBuilder();
					targetValue = targetLine.split(keyValueSeparator, -1)[1];
					String targetValues[] = targetValue.split("#~", -1);

					for (Column column : columnList) {

						resultValue.append(column.getColumnName() + "|" + "NO VALUE" + "|" + targetValues[colCount]
								+ "|" + status + "#~");
						colCount++;
					}

					errorOutputWriter
							.write(targetKey + "#=" + resultValue.substring(0, resultValue.length() - 2) + newLine);
					errorOutputWriter.flush();
					if (targetUnmatchedCounter % 1000 == 0) {
						errorOutputWriter.flush();
					}
				} else {
					// matched key
					matchedCounter++;
					sourceValue = sourceLine.split(keyValueSeparator, -1)[1];
					targetValue = targetLine.split(keyValueSeparator, -1)[1];
					String sourceValues[] = sourceValue.split("#~", -1);
					String targetValues[] = targetValue.split("#~", -1);
					int colCount = 0;
					StringBuilder resultValue = new StringBuilder();
					boolean status = false;
					for (Column column : columnList) {
						// colCount = 0;
						if (colCount > sourceValues.length - 1 && colCount > targetValues.length - 1) {
							status = true;
							resultValue.append(column.getColumnName() + "|" + "" + "|" + "" + "|" + status + "#~");
						} else if (colCount > sourceValues.length - 1) {
							status = false;
							try {
								resultValue.append(column.getColumnName() + "|" + "" + "|" + targetValues[colCount]
										+ "|" + status + "#~");
							} catch (ArrayIndexOutOfBoundsException err) {
								err.printStackTrace();
							}
						} else if (colCount > targetValues.length - 1) {
							status = false;
							resultValue.append(column.getColumnName() + "|" + sourceValues[colCount] + "|" + "" + "|"
									+ status + "#~");
						} else {
							try {
								status = sourceValues[colCount].equals(targetValues[colCount]);
							} catch (Exception err) {
								err.printStackTrace();
							}
							if (column.getColumnName().equals("last_update")) {
								status = targetValues[colCount].contains(sourceValues[colCount]);
							}
							resultValue.append(column.getColumnName() + "|" + sourceValues[colCount] + "|"
									+ targetValues[colCount] + "|" + status + "#~");
						}
						colCount++;
					}
					matchedWriter.write(sourceKey + "#=" + resultValue.substring(0, resultValue.length() - 2) + "\n");
					matchedWriter.flush();
					if (matchedCounter % 1000 == 0) {
						matchedWriter.flush();
					}
					isReadSource = true;
					isReadTarget = true;
				}

			}
			flushAndCloseWriters(sourceReader, targetReader, matchedWriter, errorOutputWriter);
			endtime = System.currentTimeMillis();
			logbackLogger.info("Comparision done in " + (endtime - starttime) + "miliseconds");
		} catch (FileNotFoundException e) {
			logbackLogger.error(e.getMessage(), e);
		} catch (IOException e) {
			logbackLogger.error(e.getMessage(), e);
		} catch (Exception e) {
			logbackLogger.error(e.getMessage(), e);
		}

	}

	private static void flushAndCloseWriters(BufferedReader sourceReader, BufferedReader targetReader,
			BufferedWriter matchedWriter, BufferedWriter errorOutputWriter) throws IOException {
		try {
			errorOutputWriter.flush();
			matchedWriter.flush();
			errorOutputWriter.close();
			matchedWriter.close();
			sourceReader.close();
			targetReader.close();
		} catch (Exception err) {
			logbackLogger.error(err.getMessage(), err);
		}
	}

	public String getSourceSortedFile() {
		return sourceSortedFile;
	}

	public void setSourceSortedFile(String sourceSortedFile) {
		this.sourceSortedFile = sourceSortedFile;
	}

	public String getTargetSortedFile() {
		return targetSortedFile;
	}

	public void setTargetSortedFile(String targetSortedFile) {
		this.targetSortedFile = targetSortedFile;
	}

	public List<Column> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<Column> columnList) {
		this.columnList = columnList;
	}

}
