package handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import entity.ColumnMeta;
import views.wizardPages.workflow.CreateWizard;

public class AddColMetaHandler {
	@Execute
	public void execute(Shell shell) {
		WizardDialog wizardDialog = new WizardDialog(shell, new CreateWizard(ColumnMeta.class));
		if (wizardDialog.open() == Window.OK) {
			System.out.println("Ok pressed");
		} else {
			System.out.println("Cancel pressed");
		}
	}
}
