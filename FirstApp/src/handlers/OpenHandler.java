package handlers;

import org.eclipse.swt.widgets.FileDialog;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

public class OpenHandler {

	@Execute
	public void execute(Shell shell) {

		FileDialog fileDialog = new FileDialog(shell);
		fileDialog.open();
	}
}
