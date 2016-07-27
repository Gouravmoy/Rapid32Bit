package jobs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import entity.ScenarioExecData;
import enums.ExecutionPhase;
import enums.ExportType;
import enums.JobType;
import enums.ProjectType;
import extra.ModelProvider;
import jobs.tasks.CompareTask2;
import jobs.tasks.GenerateCSVTask;
import jobs.tasks.GenerateReportTask;
import jobs.tasks.ReadTextFileTask;
import jobs.tasks.SortFilesTask;
import util.TimeUtil;
import views.ExecutePart;

public class FileToFileJob extends Job {
	ScenarioExecData scenarioExecData;
	List<Task> tasks = new ArrayList<Task>();
	static LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
	@SuppressWarnings("rawtypes")
	static RollingFileAppender rfAppender = new RollingFileAppender();
	static Logger logbackLogger;
	static String tempSystemPath;

	Date now = new Date();
	SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyhhmmss");
	String time = dateFormat.format(now);

	@SuppressWarnings("unchecked")
	public FileToFileJob(String name, ScenarioExecData s) {
		super(name);
		this.scenarioExecData = s;
		rfAppender.setContext(loggerContext);
		rfAppender.setFile(System.getProperty("log_file_loc") + "/log/" + "rapid" + ".log");
		logbackLogger = loggerContext.getLogger("MainController");
		logbackLogger.addAppender(rfAppender);
		ReadTextFileTask readSourceTextFileTask = new ReadTextFileTask();
		ReadTextFileTask readTargetTextFileTask = new ReadTextFileTask();
		SortFilesTask sortFilesTask = new SortFilesTask();
		CompareTask2 compareTask2 = new CompareTask2();
		GenerateCSVTask generateCSVTask = new GenerateCSVTask();
		GenerateReportTask generateReportTask = new GenerateReportTask();

		readSourceTextFileTask.setTaskName("Read Source File Task");
		readSourceTextFileTask.setFilePath(scenarioExecData.getSourceFilePath());
		readSourceTextFileTask.setColumns(scenarioExecData.getSourceColumnList());
		readSourceTextFileTask.setSeperator(scenarioExecData.getSeparator());
		readSourceTextFileTask.setUniqueColumn(scenarioExecData.getUniqueCols());
		readSourceTextFileTask.setJobType(JobType.SOURCE);

		readTargetTextFileTask.setTaskName("Read Target File Task");
		readTargetTextFileTask.setFilePath(scenarioExecData.getTargetFilePath());
		readTargetTextFileTask.setColumns(scenarioExecData.getSourceColumnList());
		readTargetTextFileTask.setColumnsTarget(scenarioExecData.getTargetColumnList());
		readTargetTextFileTask.setSeperator(scenarioExecData.getSeparatorTarget());
		readTargetTextFileTask.setUniqueColumn(scenarioExecData.getUniqueCols());
		readTargetTextFileTask.setJobType(JobType.TARGET);
		readTargetTextFileTask.setProjectType(ProjectType.File_To_File);

		sortFilesTask.setTaskName("Sorting of the Map Files");

		compareTask2.setTaskName("Compare Task");
		compareTask2.setColumnList(scenarioExecData.getSourceColumnList());

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
		tasks.add(readSourceTextFileTask);
		tasks.add(readTargetTextFileTask);
		tasks.add(sortFilesTask);
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
			status = getTaskStatus(status, task);

			ModelProvider.INSTANCE.getExecutionStatus().get(0).setStatus(status);
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					ExecutePart.viewer.refresh();
				}
			});

			logbackLogger.debug("Executing - " + task.getTaskName());
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

	private ExecutionPhase getTaskStatus(ExecutionPhase status, Task task) {
		if (task.getTaskName().equals("Read Source File Task"))
			status = ExecutionPhase.READING_SOURCE;
		if (task.getTaskName().equals("Read Target File Task"))
			status = ExecutionPhase.READING_TARGET;
		if (task.getTaskName().equals("Sorting of the Map Files"))
			status = ExecutionPhase.COMPARING;
		if (task.getTaskName().equals("Compare Task"))
			status = ExecutionPhase.COMPARING;
		if (task.getTaskName().equals("Generate CSV Task"))
			status = ExecutionPhase.GENERATING_REPORTS;
		if (task.getTaskName().equals("Generate HTML Task"))
			status = ExecutionPhase.GENERATING_REPORTS;
		return status;
	}

	private void dothisTask(Task task, SubMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		subMonitor.worked(25);
		task.execute();
		subMonitor.worked(100);
	}

}
