package jobs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.Task;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.rolling.RollingFileAppender;
import dbpackage.CreateDb;
import entity.ScenarioExecData;
import enums.DBTypes;
import enums.ExecutionPhase;
import enums.ExportType;
import enums.JobType;
import extra.ModelProvider;
import jobs.tasks.CompareTask2;
import jobs.tasks.GenerateCSVTask;
import jobs.tasks.GenerateReportTask;
import jobs.tasks.ReadBigDataTask;
import jobs.tasks.ReadDatabaseTask;
import jobs.tasks.ReadTextFileTask;
import jobs.tasks.SortFilesTask;
import util.TimeUtil;
import views.ExecutePart;

public class MyJob extends Job {
	ScenarioExecData scenarioExecData;
	List<Task> tasks = new ArrayList<Task>();
	Map<String, String> sourceRow = CreateDb.sourceMapDB.getHashMap("Source");
	Map<String, String> targetRow = CreateDb.targetMapDB.getHashMap("Target");
	Map<String, String> resultRow = CreateDb.compareMapDB.getHashMap("Result");
	Map<String, String> errorRow = CreateDb.compareMapDB.getHashMap("Error");

	static LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
	@SuppressWarnings("rawtypes")
	static RollingFileAppender rfAppender = new RollingFileAppender();
	static Logger logbackLogger;
	static String tempSystemPath;

	Date now = new Date();
	SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyhhmmss");
	String time = dateFormat.format(now);

	@SuppressWarnings("unchecked")
	public MyJob(String name, ScenarioExecData s) {
		super(name);
		rfAppender.setContext(loggerContext);
		rfAppender.setFile(System.getProperty("log_file_loc") + "/log/" + "rapid" + ".log");
		logbackLogger = loggerContext.getLogger("MainController");
		logbackLogger.addAppender(rfAppender);

		tempSystemPath = System.getProperty("log_file_loc");

		this.scenarioExecData = s;
		ReadTextFileTask readTextFileTask = new ReadTextFileTask();
		Task readDatabaseTask;
		SortFilesTask sortFilesTask = new SortFilesTask();
		CompareTask2 compareTask2 = new CompareTask2();
		GenerateCSVTask generateCSVTask = new GenerateCSVTask();
		GenerateReportTask generateReportTask = new GenerateReportTask();

		readTextFileTask.setTaskName("Read File Task");
		readTextFileTask.setFilePath(scenarioExecData.getSourceFilePath());
		readTextFileTask.setColumnValue(sourceRow);
		readTextFileTask.setColumns(scenarioExecData.getSourceColumnList());
		readTextFileTask.setSeperator(scenarioExecData.getSeparator());
		readTextFileTask.setUniqueColumn(scenarioExecData.getUniqueCols());
		readTextFileTask.setJobType(JobType.SOURCE);
		if (scenarioExecData.getTargetDatabase().getDbType().equals(DBTypes.HADOOP_HIVE)) {
			readDatabaseTask = new ReadBigDataTask();
			readDatabaseTask.setTaskName("Read Target Database Task");
			((ReadBigDataTask) readDatabaseTask).setColumns(scenarioExecData.getSourceColumnList());
			((ReadBigDataTask) readDatabaseTask).setColumnUnique(scenarioExecData.getUniqueCols());
			((ReadBigDataTask) readDatabaseTask).setColumnValue(targetRow);
			((ReadBigDataTask) readDatabaseTask).setQuery(scenarioExecData.getTargetQuery());
			((ReadBigDataTask) readDatabaseTask).setParamReplaceKeys(scenarioExecData.getTargetParameterKeyMap());
			((ReadBigDataTask) readDatabaseTask).setDatabase(scenarioExecData.getTargetDatabase());
			((ReadBigDataTask) readDatabaseTask).setLookUpCols(scenarioExecData.getLookUpColsTarget());
			((ReadBigDataTask) readDatabaseTask).setJobType(JobType.TARGET);

		} else {
			readDatabaseTask = new ReadDatabaseTask();
			readDatabaseTask.setTaskName("Read Target Database Task");
			((ReadDatabaseTask) readDatabaseTask).setSearchByColname(scenarioExecData.isDataByColName());
			((ReadDatabaseTask) readDatabaseTask).setColumns(scenarioExecData.getSourceColumnList());
			((ReadDatabaseTask) readDatabaseTask).setColumnUnique(scenarioExecData.getUniqueCols());
			((ReadDatabaseTask) readDatabaseTask).setColumnValue(targetRow);
			((ReadDatabaseTask) readDatabaseTask).setQuery(scenarioExecData.getTargetQuery());
			((ReadDatabaseTask) readDatabaseTask).setParamReplaceKeys(scenarioExecData.getTargetParameterKeyMap());
			((ReadDatabaseTask) readDatabaseTask).setDatabase(scenarioExecData.getTargetDatabase());
			((ReadDatabaseTask) readDatabaseTask).setLookUpCols(scenarioExecData.getLookUpColsTarget());
			((ReadDatabaseTask) readDatabaseTask).setJobType(JobType.TARGET);
		}

		List<String> fileList = new ArrayList<>();
		fileList.add("sourceKeysValues.txt");
		fileList.add("targetKeysValues.txt");

		sortFilesTask.setTaskName("Sorting of the Map Files");
		sortFilesTask.setFilesToBeSorted(fileList);

		compareTask2.setTaskName("Compare Task");
		compareTask2.setSourceSortedFile(tempSystemPath + "\\sourceKeysValues_Sorted.txt");
		compareTask2.setTargetSortedFile(tempSystemPath + "\\targetKeysValues_Sorted.txt");
		compareTask2.setColumnList(scenarioExecData.getSourceColumnList());

		/*
		 * compareTask.setTaskName("File to DB Compare Task");
		 * compareTask.setColumnName(scenarioExecData.getColumnList());
		 * compareTask.setSourceMap(sourceRow);
		 * compareTask.setTargetMap(targetRow);
		 * compareTask.setResultMap(resultRow);
		 * compareTask.setErrorMap(errorRow);
		 */

		if (scenarioExecData.getExportType() == ExportType.CSV) {
			generateCSVTask.setTaskName("Generate CSV Task");
			generateCSVTask.setColumns(scenarioExecData.getSourceColumnList());
			generateCSVTask.setMatchedFile(tempSystemPath + "\\matchedOutputFile.txt");
			generateCSVTask.setErrorFile(tempSystemPath + "\\errorOutputFile.txt");
			generateCSVTask.setFileOutputPath(
					scenarioExecData.getOutputFolderPath() + "\\" + scenarioExecData.getScenarioName() + "_" + time);
			generateCSVTask.setScenarioName(scenarioExecData.getScenarioName());
		} else if (scenarioExecData.getExportType() == ExportType.HTML) {
			generateReportTask.setTaskName("Generate HTML Task");
			generateReportTask.setMatchedFile(tempSystemPath + "\\matchedOutputFile.txt");
			generateReportTask.setErrorFile(tempSystemPath + "\\errorOutputFile.txt");
			generateReportTask.setFileOutputPath(
					scenarioExecData.getOutputFolderPath() + "\\" + scenarioExecData.getScenarioName() + "_" + time);
		}

		tasks.add(readTextFileTask);
		tasks.add(readDatabaseTask);
		tasks.add(sortFilesTask);
		// tasks.add(compareTask);
		tasks.add(compareTask2);
		if (scenarioExecData.getExportType() == ExportType.CSV) {
			tasks.add(generateCSVTask);
		} else if (scenarioExecData.getExportType() == ExportType.HTML) {
			tasks.add(generateReportTask);
		}
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, tasks.size());
		long starttime = System.currentTimeMillis();
		ExecutionPhase status = ExecutionPhase.NOT_STARTED;
		for (Task task : tasks) {
			if (task.getTaskName().equals("Read File Task"))
				status = ExecutionPhase.READING_SOURCE;
			if (task.getTaskName().equals("Read Target Database Task"))
				status = ExecutionPhase.READING_TARGET;
			if (task.getTaskName().equals("Sorting of the Map Files"))
				status = ExecutionPhase.COMPARING;
			if (task.getTaskName().equals("Compare Task"))
				status = ExecutionPhase.COMPARING;
			if (task.getTaskName().equals("Generate CSV Task"))
				status = ExecutionPhase.GENERATING_REPORTS;
			if (task.getTaskName().equals("Generate HTML Task"))
				status = ExecutionPhase.GENERATING_REPORTS;

			ModelProvider.INSTANCE.getExecutionStatus().get(0).setStatus(status);
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					ExecutePart.viewer.refresh();
				}
			});
			logbackLogger.info(
					"Executing - " + task.getTaskName() + "************************************************" + "\n");
			try{
			dothisTask(task, subMonitor.newChild(1));
			}catch(Exception exception){
				logbackLogger.debug(exception.getMessage(),exception);
			}
			logbackLogger.debug("Execution Completed - " + task.getTaskName());
		}
		long endtime = (System.currentTimeMillis());
		logbackLogger.info("All Tasks completed in " + (endtime - starttime) + " miliseconds");
		ModelProvider.INSTANCE.getExecutionStatus().get(0).setStatus(ExecutionPhase.COMPLETED);
		ModelProvider.INSTANCE.getExecutionStatus().get(0)
				.setExecutionTime(TimeUtil.getDurationBreakdown(endtime - starttime));
		ModelProvider.INSTANCE.getExecutionStatus().get(0).setRecordsProcessed("Completed");
		;
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				ExecutePart.viewer.refresh();
			}
		});

		CreateDb.closeDataBase();
		return Status.OK_STATUS;
	}

	private void dothisTask(Task task, SubMonitor monitor) {
		try{
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		subMonitor.worked(25);
		task.execute();
		subMonitor.worked(100);
		}catch(Exception err){
			logbackLogger.error(err.getMessage(),err);
		}
	}

}
