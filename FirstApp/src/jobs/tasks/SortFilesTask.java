package jobs.tasks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.Task;
import org.slf4j.LoggerFactory;

import com.google.code.externalsorting.ExternalSort;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.rolling.RollingFileAppender;
import extra.ModelProvider;

public class SortFilesTask extends Task {

	List<String> filesToBeSorted;
	List<File> listOfBatchFiles;

	static LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
	@SuppressWarnings("rawtypes")
	static RollingFileAppender rfAppender = new RollingFileAppender();
	static Logger logbackLogger;
	String tempSystemPath = System.getProperty("log_file_loc") + "//";

	@SuppressWarnings("unchecked")
	public void execute() {
		rfAppender.setContext(loggerContext);
		rfAppender.setFile(System.getProperty("log_file_loc") + "/log/" + "rapid" + ".log");
		logbackLogger = loggerContext.getLogger("MainController");
		logbackLogger.addAppender(rfAppender);
		try {
			File outputFile;
			logbackLogger.info("Starting Sorting of file");
			filesToBeSorted = new ArrayList<>();
			filesToBeSorted.add(ModelProvider.INSTANCE.getSourcePath());
			filesToBeSorted.add(ModelProvider.INSTANCE.getTargetPath());
			for (String file : filesToBeSorted) {
				logbackLogger.info("Starting Srting of file" + file);
				long starttime = System.currentTimeMillis();
				long endtime = 0;

				logbackLogger.info("Sorting File " + file);
				File fileTobeSorted = new File(file);
				listOfBatchFiles = ExternalSort.sortInBatch(fileTobeSorted);
				if (file.contains("source")) {
					outputFile = new File(fileTobeSorted.getAbsolutePath().replace(file.split("\\.")[0],
							file.split("\\.")[0] + "_Sorted"));
					ModelProvider.INSTANCE.setSourceSortedPath(outputFile.getAbsolutePath());
				} else {
					outputFile = new File(fileTobeSorted.getAbsolutePath().replace(file.split("\\.")[0],
							file.split("\\.")[0] + "_Sorted"));
					ModelProvider.INSTANCE.setTargetSortedpath(outputFile.getAbsolutePath());
				}
				logbackLogger.info("Sorting of file" + file + " in path " + outputFile.getAbsolutePath());
				ExternalSort.mergeSortedFiles(listOfBatchFiles, outputFile);
				endtime = System.currentTimeMillis();
				logbackLogger.info("Sorting done for file " + file + " in " + (endtime - starttime) + "miliseconds");
				if (outputFile.exists()) {
					logbackLogger.info("Output file exists for " + file);
				} else {
					logbackLogger.info("Output file does not exists for " + file);
				}
			}
			/*
			 * for (String file : filesToBeSorted) { File fileTobeSorted = new
			 * File(file); if (fileTobeSorted.delete()) {
			 * System.out.println(fileTobeSorted.getName() + " is deleted!");
			 * logbackLogger.debug(fileTobeSorted.getName() + " is deleted!"); }
			 * else { System.out.println("Delete operation is failed.");
			 * logbackLogger.error(fileTobeSorted.getName() +
			 * "Delete operation is failed."); } }
			 */
		} catch (IOException e) {
			logbackLogger.error(e.getMessage(), e);
		} catch (Exception e) {
			logbackLogger.error(e.getMessage(), e);
		}
	}

	public List<String> getFilesToBeSorted() {
		return filesToBeSorted;
	}

	public void setFilesToBeSorted(List<String> filesToBeSorted) {
		this.filesToBeSorted = filesToBeSorted;
	}
}
