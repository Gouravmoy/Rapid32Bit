package handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import views.wizardPages.executeModule.MyWizard;

public class ExecuteMainHandler {

	@Execute
	public void execute(Shell shell) {
		WizardDialog wizardDialog = new WizardDialog(shell, new MyWizard());
		if (wizardDialog.open() == Window.OK) {
			System.out.println("Ok pressed");
		} else {
			System.out.println("Cancel pressed");
		}
	}

}
