package jobs.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.Task;
import org.eclipse.swt.widgets.Display;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.rolling.RollingFileAppender;
import entity.Column;
import extra.ModelProvider;
import views.ExecutePart;

public class GenerateReportTask extends Task {

	static final String pass = "Pass";
	static final String mismatch = "Mismatch";
	static final String fail = "Fail";

	String fileOutputPath;
	/*
	 * Map<String, String> resultValues = new TreeMap<String, String>();;
	 * Map<String, String> errorValues = new TreeMap<String, String>();;
	 */

	final String newLine = "\n";
	String matchedFile;
	String errorFile;
	List<Column> columns;

	Map<String, String> filePassPathMap = new TreeMap<String, String>();
	Map<String, String> fileMismatchPathMap = new TreeMap<String, String>();
	Map<String, String> fileFailPathMap = new TreeMap<String, String>();
	Map<String, String> clusterPass100filePathMap = new TreeMap<String, String>();
	Map<String, String> clusterMismatch100filePathMap = new TreeMap<String, String>();
	Map<String, String> clusterFail100filePathMap = new TreeMap<String, String>();
	Map<String, String> finalPassPath = new TreeMap<>();
	Map<String, String> finalMismatchPath = new TreeMap<>();
	Map<String, String> finalFailPath = new TreeMap<>();
	Map<String, String> finalMainPath = new TreeMap<>();
	String folderName = "";
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
		// int matchedCounter = 0;
		long starttime = System.currentTimeMillis();
		long endtime = 0;
		try {
			/*
			 * Date now = new Date(); SimpleDateFormat dateFormat = new
			 * SimpleDateFormat("ddMMyyyhhmmss"); String time =
			 * dateFormat.format(now);
			 */
			folderName = "";
			logbackLogger.info("Generatug reports at Folder - " + fileOutputPath);
			logbackLogger.info("Generating Single Layer Reports");
			generateSingleHTMLReports();
			logbackLogger.info("Generated Single Layer Reports");
			logbackLogger.info("Generating 100 Cluster Reports");
			generateCluster100HTMLReports();
			logbackLogger.info("Generated 100 Cluster Reports");
			logbackLogger.info("Generating Link Cluster Reports");
			generateLinkHTMLReports();
			logbackLogger.info("Generated Link Cluster Reports");
			endtime = System.currentTimeMillis();
			logbackLogger.info("CSV Report Generated in " + (endtime - starttime) + "miliseconds");
		} catch (Exception err) {
			logbackLogger.error(err.getMessage(), err);
		}
	}

	private void generateLinkHTMLReports() {
		finalPassPath = createMultiLayerHTML(clusterPass100filePathMap, pass);
		finalMismatchPath = createMultiLayerHTML(clusterMismatch100filePathMap, mismatch);
		finalFailPath = createMultiLayerHTML(clusterFail100filePathMap, fail);
		generateMainSubPage(finalPassPath, pass);
		generateMainSubPage(finalMismatchPath, mismatch);
		generateMainSubPage(finalFailPath, fail);
		generateMain();
	}

	private void generateMain() {
		File resultTemplateFile = new File("tempFinalSubTemplate.html");
		URL resultTemplateURL;
		try {
			resultTemplateURL = new URL("platform:/plugin/FirstApp/icons/templates/Link Template.html");
			FileUtils.copyURLToFile(resultTemplateURL, resultTemplateFile);
			StringBuilder resultString = new StringBuilder();
			File fileDir = new File(fileOutputPath + folderName + "");
			String color = "";
			String status = "";
			for (Entry<String, String> resultValue : finalMainPath.entrySet()) {
				String fileName = "";
				String key = resultValue.getKey();
				String fileLocation = resultValue.getValue();
				String fileTruthStatus = key.split("\\_")[1];
				if (fileTruthStatus.equals(pass)) {
					fileName = "Pass Records";
					status = "True";
					color = "green";
				} else if (fileTruthStatus.equals(mismatch)) {
					fileName = "Partial Mismatch Records";
					status = "False";
					color = "red";
				} else {
					fileName = "Failed Records";
					status = "False";
					color = "red";
				}

				resultString
						.append("<TD WIDTH='4%'  STYLE='WORD-WRAP: BREAK-WORD' BGCOLOR='D8EEEE' align='center' valign='center'><a href="
								+ "'./Cluster100/" + fileLocation + "'"
								+ "><FONT FACE='VERDANA' COLOR = #000066 SIZE=2>" + fileName + "</a>"
								+ "</FONT></TD><TD WIDTH='8%'  STYLE='WORD-WRAP: BREAK-WORD' BGCOLOR='D8EEEE' align='center' valign='center'><FONT FACE='VERDANA' COLOR ="
								+ color + " SIZE=2>" + status + "</FONT></TD></TR>");

			}
			String resultHtmlString = FileUtils.readFileToString(resultTemplateFile);
			resultHtmlString = resultHtmlString.replace("$build", "Version 1.0.1");
			resultHtmlString = resultHtmlString.replace("$typeOfTesting", "Smoke Testing");
			resultHtmlString = resultHtmlString.replace("$TestCaseHeader", "Cluster Report");
			resultHtmlString = resultHtmlString.replace("$results", resultString);
			resultHtmlString = resultHtmlString.replace("$Header1", "Cluster Groups");
			resultHtmlString = resultHtmlString.replace("$Header2", "Status");

			String newFileName = "Main";
			File newindividualHtmlFile = new File(fileDir.getPath() + "/" + newFileName + ".html");
			FileUtils.writeStringToFile(newindividualHtmlFile, resultHtmlString, "UTF-8");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resultTemplateFile.delete()) {
			System.out.println(resultTemplateFile.getName() + " is deleted!");
		} else {
			System.out.println("Delete operation is failed.");
		}
	}

	private void generateMainSubPage(Map<String, String> finalPath, String truthStatus) {
		File resultTemplateFile = new File("tempFinalSubTemplate.html");
		URL resultTemplateURL;
		try {
			resultTemplateURL = new URL("platform:/plugin/FirstApp/icons/templates/Link Template.html");
			FileUtils.copyURLToFile(resultTemplateURL, resultTemplateFile);
			StringBuilder resultString = new StringBuilder();
			File fileDir = new File(fileOutputPath + folderName + "/Cluster100/");
			for (Entry<String, String> resultValue : finalPath.entrySet()) {

				String key = resultValue.getKey();
				String fileName = key.split("\\|")[0];
				String fileLocation = resultValue.getValue();
				String status = key.split("\\|")[1];
				String color = "";
				if (status.equalsIgnoreCase("False")) {
					color = "red";
				} else if (status.equalsIgnoreCase("True")) {
					color = "green";
				} else {
					color = "blue";
				}
				resultString
						.append("<TD WIDTH='4%'  STYLE='WORD-WRAP: BREAK-WORD' BGCOLOR='D8EEEE' align='center' valign='center'><a href="
								+ "'" + fileLocation + "'" + "><FONT FACE='VERDANA' COLOR = #000066 SIZE=2>" + fileName
								+ "</a>"
								+ "</FONT></TD><TD WIDTH='8%'  STYLE='WORD-WRAP: BREAK-WORD' BGCOLOR='D8EEEE' align='center' valign='center'><FONT FACE='VERDANA' COLOR ="
								+ color + " SIZE=2>" + status + "</FONT></TD></TR>");
			}
			String resultHtmlString = FileUtils.readFileToString(resultTemplateFile);
			resultHtmlString = resultHtmlString.replace("$build", "Version 1.0.1");
			resultHtmlString = resultHtmlString.replace("$typeOfTesting", "Smoke Testing");
			resultHtmlString = resultHtmlString.replace("$TestCaseHeader", "Cluster Report");
			resultHtmlString = resultHtmlString.replace("$results", resultString);
			resultHtmlString = resultHtmlString.replace("$Header1", "Cluster Groups");
			resultHtmlString = resultHtmlString.replace("$Header2", "Status");
			String newFileName = "Main_" + truthStatus;
			File newindividualHtmlFile = new File(fileDir.getPath() + "/" + newFileName + ".html");
			FileUtils.writeStringToFile(newindividualHtmlFile, resultHtmlString, "UTF-8");
			finalMainPath.put(newFileName, "" + newFileName + ".html");
		} catch (MalformedURLException e) {
			logbackLogger.error(e.getMessage(), e);
		} catch (IOException e) {
			logbackLogger.error(e.getMessage(), e);
		}
		if (resultTemplateFile.delete()) {
			System.out.println(resultTemplateFile.getName() + " is deleted!");
		} else {
			System.out.println("Delete operation is failed.");
		}
	}

	private Map<String, String> createMultiLayerHTML(Map<String, String> pathMap, String truthStatus) {
		File resultTemplateFile = new File("tempMultipleTemplate.html");
		URL resultTemplateURL;
		Map<String, String> newMap = new TreeMap<>();
		File fileDir;
		StringBuilder resultString;
		String resultHtmlString;
		int start = 1;
		int max = 0;
		int count = 0;
		int blocks = 0;
		int mainCounter = 0;
		try {
			resultTemplateURL = new URL("platform:/plugin/FirstApp/icons/templates/Link Template.html");
			FileUtils.copyURLToFile(resultTemplateURL, resultTemplateFile);
			fileDir = new File(fileOutputPath + folderName + "/Cluster100/");
			String key = "", status = "", fileName = "", fileLocation = "", color = "";

			int increment = 10;
			while ((newMap.size() >= 2 || newMap.size() == 0) && pathMap.size() != 0) {
				resultString = new StringBuilder();
				increment *= 10;
				mainCounter = 0;
				blocks = 0;
				start = 0;
				if (newMap.size() != 0) {
					pathMap.clear();
					pathMap.putAll(newMap);
					newMap.clear();
				}
				for (Entry<String, String> resultValue : pathMap.entrySet()) {
					mainCounter++;
					count++;
					key = resultValue.getKey();
					fileName = key.split("\\|")[0];
					fileLocation = resultValue.getValue();
					status = key.split("\\|")[1];
					if (status.equalsIgnoreCase("False")) {
						color = "red";
					} else if (status.equalsIgnoreCase("True")) {
						color = "green";
					} else {
						color = "blue";
					}
					resultString
							.append("<TD WIDTH='4%'  STYLE='WORD-WRAP: BREAK-WORD' BGCOLOR='D8EEEE' align='center' valign='center'><a href="
									+ "'" + fileLocation + "'" + "><FONT FACE='VERDANA' COLOR = #000066 SIZE=2>"
									+ fileName + "</a>"
									+ "</FONT></TD><TD WIDTH='8%'  STYLE='WORD-WRAP: BREAK-WORD' BGCOLOR='D8EEEE' align='center' valign='center'><FONT FACE='VERDANA' COLOR ="
									+ color + " SIZE=2>" + status + "</FONT></TD></TR>");
					if (count == 10 || mainCounter == pathMap.size()) {
						blocks++;
						resultHtmlString = FileUtils.readFileToString(resultTemplateFile);
						resultHtmlString = resultHtmlString.replace("$build", "Version 1.0.1");
						resultHtmlString = resultHtmlString.replace("$typeOfTesting", "Smoke Testing");
						resultHtmlString = resultHtmlString.replace("$TestCaseHeader", "Cluster Report");
						resultHtmlString = resultHtmlString.replace("$results", resultString);
						resultHtmlString = resultHtmlString.replace("$Header1", "Cluster Groups");
						resultHtmlString = resultHtmlString.replace("$Header2", "Status");
						max = count * increment * blocks;
						String newFileName = start + "-" + max + "_" + truthStatus;
						File newindividualHtmlFile = new File(fileDir.getPath() + "/" + newFileName + ".html");
						FileUtils.writeStringToFile(newindividualHtmlFile, resultHtmlString, "UTF-8");
						newMap.put(newFileName + "|False", "" + newFileName + ".html");
						count = 0;
						start = max;
						resultString = new StringBuilder();
					}
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (resultTemplateFile.delete()) {
			System.out.println(resultTemplateFile.getName() + " is deleted!");
		} else {
			System.out.println("Delete operation is failed.");
		}
		return newMap;
	}

	private void generateCluster100HTMLReports() {
		File resultTemplateFile = new File("tempSingleTemplate.html");
		try {
			URL resultTemplateURL = new URL("platform:/plugin/FirstApp/icons/templates/Link Template.html");
			FileUtils.copyURLToFile(resultTemplateURL, resultTemplateFile);
			generate100Clusters(resultTemplateFile, pass, filePassPathMap);
			generate100Clusters(resultTemplateFile, mismatch, fileMismatchPathMap);
			generate100Clusters(resultTemplateFile, fail, fileFailPathMap);
			if (resultTemplateFile.delete()) {
				System.out.println(resultTemplateFile.getName() + " is deleted!");
			} else {
				System.out.println("Delete operation is failed.");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void generate100Clusters(File resultTemplateFile, String mapStatus, Map<String, String> pathMap)
			throws IOException {
		File fileDir;
		String resultHtmlString = null;
		fileDir = new File(fileOutputPath + folderName + "/Cluster100/");
		StringBuilder resultString;
		String key = "", status = "", fileName = "", fileLocation = "", color = "";
		int count = 0;
		int startCnt = 1;
		resultString = new StringBuilder();

		for (Entry<String, String> resultValue : pathMap.entrySet()) {
			count++;
			key = resultValue.getKey();
			fileName = key.split("\\|")[0];
			fileLocation = resultValue.getValue();
			status = key.split("\\|")[1];
			if (status.equalsIgnoreCase("False")) {
				color = "red";
			} else if (status.equalsIgnoreCase("True")) {
				color = "green";
			} else {
				color = "blue";
			}

			resultString
					.append("<TD WIDTH='4%'  STYLE='WORD-WRAP: BREAK-WORD' BGCOLOR='D8EEEE' align='center' valign='center'><a href="
							+ "'../../../" + fileLocation + "'" + "><FONT FACE='VERDANA' COLOR = #000066 SIZE=2>"
							+ fileName + "</a>"
							+ "</FONT></TD><TD WIDTH='8%'  STYLE='WORD-WRAP: BREAK-WORD' BGCOLOR='D8EEEE' align='center' valign='center'><FONT FACE='VERDANA' COLOR ="
							+ color + " SIZE=2>" + status + "</FONT></TD></TR>");
			if (count % 100 == 0 || count == pathMap.size()) {
				resultHtmlString = FileUtils.readFileToString(resultTemplateFile);
				resultHtmlString = resultHtmlString.replace("$build", "Version 1.0.1");
				resultHtmlString = resultHtmlString.replace("$typeOfTesting", "Smoke Testing");
				resultHtmlString = resultHtmlString.replace("$TestCaseHeader", "Cluster Report");
				resultHtmlString = resultHtmlString.replace("$results", resultString);
				resultHtmlString = resultHtmlString.replace("$Header1", "Cluster Groups");
				resultHtmlString = resultHtmlString.replace("$Header2", "Status");
				String newFileName = startCnt + "-" + count;
				File newindividualHtmlFile = new File(
						fileDir.getPath() + "/" + mapStatus + "/" + newFileName + ".html");
				FileUtils.writeStringToFile(newindividualHtmlFile, resultHtmlString, "UTF-8");
				if (mapStatus.equals(pass))
					clusterPass100filePathMap.put(newFileName + "|True",
							"../Cluster100/" + "/" + mapStatus + "/" + newFileName + ".html");
				if (mapStatus.equals(mismatch))
					clusterMismatch100filePathMap.put(newFileName + "|False",
							"../Cluster100/" + "/" + mapStatus + "/" + newFileName + ".html");
				if (mapStatus.equals(fail))
					clusterFail100filePathMap.put(newFileName + "|False",
							"../Cluster100/" + "/" + mapStatus + "/" + newFileName + ".html");
				resultString = new StringBuilder();
				startCnt = count + 1;
			}
		}
	}

	private void generateSingleHTMLReports() {

		File resultTemplateFile = new File("tempSingleTemplate.html");
		try {
			URL resultTemplateURL = new URL("platform:/plugin/FirstApp/icons/templates/SingleTemplate.html");
			FileUtils.copyURLToFile(resultTemplateURL, resultTemplateFile);
			matchedFile = ModelProvider.INSTANCE.getMatchedPath();
			errorFile = ModelProvider.INSTANCE.getErrorPath();
			generateIndivisualHTMLFromMap(resultTemplateFile, matchedFile);
			generateIndivisualHTMLFromMap(resultTemplateFile, errorFile);
			if (resultTemplateFile.delete()) {
				System.out.println(resultTemplateFile.getName() + " is deleted!");
			} else {
				System.out.println("Delete operation is failed.");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generateIndivisualHTMLFromMap(File resultTemplateFile, String File) throws IOException {
		String fileName;
		String resultHtmlString;
		String colName;
		String sourceVal;
		String targetVal;
		String status = null;
		String color;
		File fileDir;
		StringBuilder resultString;
		boolean failStatus = true;
		boolean mismatchStatus = false;

		BufferedReader bufferedReader;

		bufferedReader = new BufferedReader(new FileReader(new File(File)));

		fileDir = new File(fileOutputPath + folderName);
		if (fileDir.exists()) {
			fileDir.mkdirs();
		}
		int rowCount = 0;
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			resultString = new StringBuilder();
			resultHtmlString = FileUtils.readFileToString(resultTemplateFile);
			String key = line.split("#=")[0];
			String value = line.split("#=")[1];
			fileName = key.replace("#~", "_");
			failStatus = true;
			mismatchStatus = false;
			rowCount++;
			if (rowCount % 100 == 0) {
				ModelProvider.INSTANCE.getExecutionStatus().get(0).setRecordsProcessed(Long.toString(rowCount));
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						ExecutePart.viewer.refresh();
					}
				});
			}
			for (String resultRow : value.split("#~")) {
				try {
					colName = resultRow.split("\\|")[0] != null ? resultRow.split("\\|")[0] : "null";
					sourceVal = resultRow.split("\\|")[1] != null ? resultRow.split("\\|")[1] : "null";
					targetVal = resultRow.split("\\|")[2] != null ? resultRow.split("\\|")[2] : "null";
					status = resultRow.split("\\|")[3] != null ? resultRow.split("\\|")[3] : "False";
				} catch (Exception e) {
					logbackLogger.error(e.getMessage(), e);
					continue;
				}
				if (status.equalsIgnoreCase("False")) {
					mismatchStatus = true;
					color = "red";
				} else if (status.equalsIgnoreCase("True")) {
					failStatus = false;
					color = "green";
				} else {
					color = "blue";
				}

				resultString.append("<TD>" + colName + "</TD><TD>" + sourceVal + "</TD><TD>" + targetVal + "</TD>"
						+ "<TD><FONT COLOR = " + color + ">" + status + "</FONT></TD></TR>");
			}
			if (mismatchStatus == false) {
				fileDir = new File(fileOutputPath + folderName + "/Pass");
				filePassPathMap.put(fileName + "|True", "Pass" + "/" + fileName + ".html");
			}
			if (mismatchStatus == true && failStatus == false) {
				fileDir = new File(fileOutputPath + folderName + "/Mismatch");
				fileMismatchPathMap.put(fileName + "|False", "Mismatch" + "/" + fileName + ".html");
			}
			if (failStatus == true) {
				fileDir = new File(fileOutputPath + folderName + "/Fail");
				fileFailPathMap.put(fileName + "|False", "Fail" + "/" + fileName + ".html");
			}
			resultHtmlString = resultHtmlString.replace("$build", "Version 1.0.1");
			resultHtmlString = resultHtmlString.replace("$typeOfTesting", "Smoke Testing");
			resultHtmlString = resultHtmlString.replace("$results", resultString);
			resultHtmlString = resultHtmlString.replace("$UNIQUE_KEY", fileName);
			resultHtmlString = resultHtmlString.replace("$ResultHeader", "Comparision Report");
			File newindividualHtmlFile = new File(fileDir.getPath() + "\\" + fileName + ".html");
			FileUtils.writeStringToFile(newindividualHtmlFile, resultHtmlString, "UTF-8");
		}
		bufferedReader.close();
	}

	public String getFileOutputPath() {
		return fileOutputPath;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setFileOutputPath(String fileOutputPath) {
		this.fileOutputPath = fileOutputPath;
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

}
