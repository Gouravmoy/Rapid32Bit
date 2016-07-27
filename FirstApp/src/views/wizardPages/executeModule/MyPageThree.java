package views.wizardPages.executeModule;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import enums.ExportType;

public class MyPageThree extends WizardPage {
	private Composite container;
	private Combo exportTypeComboBox;
	private Text exportFileLocationPathText;

	protected MyPageThree() {
		super("Data Runner");
		setTitle("Export Result");
		setDescription("Execute Data Comparision Result");
	}

	@Override
	public void createControl(Composite parent) {
		Point size = getShell().computeSize(611, 739);
		getShell().setSize(size);
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		container.setLayout(layout);
		// required to avoid an error in the system
		setControl(container);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		Label label1 = new Label(container, SWT.NONE);
		label1.setText("Export Format");
		new Label(container, SWT.NONE);

		exportTypeComboBox = new Combo(container, SWT.NONE);
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo.widthHint = 196;
		exportTypeComboBox.setLayoutData(gd_combo);
		for (ExportType exportType : ExportType.values())
			exportTypeComboBox.add(exportType.toString());

		Label lblExportFileLocation = new Label(container, SWT.NONE);
		lblExportFileLocation.setText("Export File Location");
		new Label(container, SWT.NONE);

		exportFileLocationPathText = new Text(container, SWT.BORDER);
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 212;
		exportFileLocationPathText.setLayoutData(gd_text);
		exportFileLocationPathText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				setPageComplete(true);
			}
		});
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		Button btnBrowse = new Button(container, SWT.NONE);
		GridData gd_btnBrowse = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBrowse.widthHint = 64;
		btnBrowse.setLayoutData(gd_btnBrowse);
		btnBrowse.setText("Browse");
		btnBrowse.addSelectionListener(new Open());

		setPageComplete(false);
	}

	class Open implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			DirectoryDialog dialog = new DirectoryDialog(getShell());
			dialog.setText("Open Folder");
			dialog.setFilterPath("C:/");
			String selected = dialog.open();
			exportFileLocationPathText.setText(selected);
		}

		public void widgetDefaultSelected(SelectionEvent event) {
		}
	}

	public Combo getExportTypeComboBox() {
		return exportTypeComboBox;
	}

	public Text getExportFileLocationPathText() {
		return exportFileLocationPathText;
	}

}
