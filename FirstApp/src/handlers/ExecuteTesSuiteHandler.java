package handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import jobs.FileToFileJob;

public class ExecuteTesSuiteHandler {
	@Execute
	public void execute(Shell shell) {
		
		FileToFileJob job = new FileToFileJob("My Job",null);
		job.schedule();
	}
}
