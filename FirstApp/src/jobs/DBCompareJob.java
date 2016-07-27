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
import enums.ExecutionPhase;
import enums.ExportType;
import enums.JobType;
import extra.ModelProvider;
import jobs.tasks.CompareTask2;
import jobs.tasks.GenerateCSVTask;
import jobs.tasks.GenerateReportTask;
import jobs.tasks.ReadDatabaseTask;
import jobs.tasks.SortFilesTask;
import util.TimeUtil;
import views.ExecutePart;

public class DBCompareJob extends Job {
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
	public DBCompareJob(String name, ScenarioExecData scenarioExecData) {
		super(name);
		rfAppender.setContext(loggerContext);
		rfAppender.setFile(System.getProperty("log_file_loc") + "/log/" + "rapid" + ".log");
		logbackLogger = loggerContext.getLogger("MainController");
		logbackLogger.addAppender(rfAppender);

		tempSystemPath = System.getProperty("log_file_loc");

		ReadDatabaseTask readSourceDatabaseTask = new ReadDatabaseTask();
		ReadDatabaseTask readTargetDatabaseTask = new ReadDatabaseTask();
		SortFilesTask sortFilesTask = new SortFilesTask();
		CompareTask2 compareTask2 = new CompareTask2();
		// CompareTask compareTask = new CompareTask();
		GenerateCSVTask generateCSVTask = new GenerateCSVTask();
		GenerateReportTask generateReportTask = new GenerateReportTask();

		readSourceDatabaseTask.setColumns(scenarioExecData.getSourceColumnList());
		readSourceDatabaseTask.setColumnUnique(scenarioExecData.getUniqueCols());
		readSourceDatabaseTask.setColumnValue(sourceRow);
		readSourceDatabaseTask.setQuery(scenarioExecData.getSourceQuery());
		readSourceDatabaseTask.setParamReplaceKeys(scenarioExecData.getSourceParameterKeyMap());
		readSourceDatabaseTask.setDatabase(scenarioExecData.getSourceDatabase());
		readSourceDatabaseTask.setLookUpCols(scenarioExecData.getLookUpColsSource());
		readSourceDatabaseTask.setJobType(JobType.SOURCE);
		readSourceDatabaseTask.setSearchByColname(scenarioExecData.isDataByColName());
		readSourceDatabaseTask.setTaskName("DB 2 DB Read Source Database Task");

		readTargetDatabaseTask.setColumns(scenarioExecData.getSourceColumnList());
		readTargetDatabaseTask.setColumnUnique(scenarioExecData.getUniqueCols());
		readTargetDatabaseTask.setColumnValue(targetRow);
		readTargetDatabaseTask.setQuery(scenarioExecData.getTargetQuery());
		readTargetDatabaseTask.setParamReplaceKeys(scenarioExecData.getTargetParameterKeyMap());
		readTargetDatabaseTask.setDatabase(scenarioExecData.getTargetDatabase());
		readTargetDatabaseTask.setLookUpCols(scenarioExecData.getLookUpColsTarget());
		readTargetDatabaseTask.setJobType(JobType.TARGET);
		readTargetDatabaseTask.setSearchByColname(scenarioExecData.isDataByColName());
		readTargetDatabaseTask.setTaskName("DB 2 DB Read Target Database Task");

		List<String> fileList = new ArrayList<>();
		fileList.add(tempSystemPath + "\\sourceKeysValues.txt");
		fileList.add(tempSystemPath + "\\targetKeysValues.txt");

		sortFilesTask.setTaskName("Sorting of the Map Files");
		sortFilesTask.setFilesToBeSorted(fileList);

		compareTask2.setTaskName("DB to DB Compare Task");
		compareTask2.setSourceSortedFile(tempSystemPath + "\\sourceKeysValues_Sorted.txt");
		compareTask2.setTargetSortedFile(tempSystemPath + "\\targetKeysValues_Sorted.txt");
		compareTask2.setColumnList(scenarioExecData.getSourceColumnList());

		/*
		 * compareTask.setColumnName(scenarioExecData.getColumnList());
		 * compareTask.setSourceMap(sourceRow);
		 * compareTask.setTargetMap(targetRow);
		 * compareTask.setResultMap(resultRow);
		 * compareTask.setErrorMap(errorRow); compareTask.setTaskName(
		 * "DB 2 DB Compare Task");
		 */

		tasks.add(readSourceDatabaseTask);
		tasks.add(readTargetDatabaseTask);
		tasks.add(sortFilesTask);
		// tasks.add(compareTask);
		tasks.add(compareTask2);

		if (scenarioExecData.getExportType() == ExportType.CSV) {
			generateCSVTask.setColumns(scenarioExecData.getSourceColumnList());
			generateCSVTask.setMatchedFile(tempSystemPath + "\\matchedOutputFile.txt");
			generateCSVTask.setErrorFile(tempSystemPath + "\\errorOutputFile.txt");
			generateCSVTask.setFileOutputPath(
					scenarioExecData.getOutputFolderPath() + "\\" + scenarioExecData.getScenarioName() + "_" + time);
			generateCSVTask.setTaskName("DB 2 DB Generate CSV Task");
			generateCSVTask.setScenarioName(scenarioExecData.getScenarioName());
			tasks.add(generateCSVTask);
		} else if (scenarioExecData.getExportType() == ExportType.HTML) {
			generateReportTask.setMatchedFile(tempSystemPath + "\\matchedOutputFile.txt");
			generateReportTask.setErrorFile(tempSystemPath + "\\errorOutputFile.txt");
			generateReportTask.setFileOutputPath(
					scenarioExecData.getOutputFolderPath() + "\\" + scenarioExecData.getScenarioName() + "_" + time);
			generateReportTask.setTaskName("DB 2 DB Generate HTML Task");
			tasks.add(generateReportTask);
		}
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, tasks.size());
		long starttime = System.currentTimeMillis();
		ExecutionPhase status = ExecutionPhase.NOT_STARTED;
		for (Task task : tasks) {
			if (task.getTaskName().equals("DB 2 DB Read Source Database Task"))
				status = ExecutionPhase.READING_SOURCE;
			if (task.getTaskName().equals("DB 2 DB Read Target Database Task"))
				status = ExecutionPhase.READING_TARGET;
			if (task.getTaskName().equals("Sorting of the Map Files"))
				status = ExecutionPhase.COMPARING;
			if (task.getTaskName().equals("DB to DB Compare Task"))
				status = ExecutionPhase.COMPARING;
			if (task.getTaskName().equals("DB 2 DB Generate CSV Task"))
				status = ExecutionPhase.GENERATING_REPORTS;
			if (task.getTaskName().equals("DB 2 DB Generate HTML Task"))
				status = ExecutionPhase.GENERATING_REPORTS;

			ModelProvider.INSTANCE.getExecutionStatus().get(0).setStatus(status);
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					ExecutePart.viewer.refresh();
				}
			});
			logbackLogger.info(
					"Executing - " + task.getTaskName() + "************************************************" + "\n");
			dothisTask(task, subMonitor.newChild(1));
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
		return Status.OK_STATUS;
	}

	private void dothisTask(Task task, SubMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		subMonitor.worked(25);
		task.execute();
		subMonitor.worked(100);

	}

}
