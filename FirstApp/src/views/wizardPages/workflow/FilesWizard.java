package views.wizardPages.workflow;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import entity.Files;
import enums.FileTypes;

public class FilesWizard extends WizardPage {
	Files file;

	private Composite container;

	private Text fileName;
	private Text separator;
	private Text columnMeta;

	public FilesWizard(String pageName, Files files) {
		super(pageName);
		setTitle(pageName);
		setDescription("Create a File Metadata ");
		this.file = files;
	}

	public Files getFile() {
		return file;
	}

	public void setFile(Files file) {
		this.file = file;
		columnMeta.setText(file.getFileColumnMeta().getColumnMetaName());
	}

	@Override
	public void createControl(Composite parent) {

		container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(null);

		Label lblFileMetadatName = new Label(container, SWT.NONE);
		lblFileMetadatName.setBounds(5, 8, 115, 15);
		lblFileMetadatName.setText("File Metadata Name");

		fileName = new Text(container, SWT.BORDER);
		fileName.setBounds(167, 5, 402, 21);
		fileName.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!fileName.getText().isEmpty()) {
					setPageComplete(true);
				}
			}
		});
		Label lblFiletype = new Label(container, SWT.NONE);
		lblFiletype.setBounds(5, 35, 44, 15);
		lblFiletype.setText("FileType");

		Combo fileTypeCombo = new Combo(container, SWT.NONE);
		fileTypeCombo.setBounds(167, 31, 402, 23);
		for (FileTypes fileType : FileTypes.values()) {
			fileTypeCombo.add(fileType.toString());
		}
		fileTypeCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				file.setFileTypes(FileTypes.valueOf(fileTypeCombo.getText()));

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}
		});
		Label lblLinkedColumnMetadata = new Label(container, SWT.NONE);
		lblLinkedColumnMetadata.setBounds(5, 63, 134, 15);
		lblLinkedColumnMetadata.setText("Linked Column Metadata");

		Label lblSeparator = new Label(container, SWT.NONE);
		lblSeparator.setBounds(5, 90, 50, 15);
		lblSeparator.setText("Separator");

		separator = new Text(container, SWT.BORDER);
		separator.setBounds(167, 87, 402, 21);

		Button btnClearAll = new Button(container, SWT.NONE);
		btnClearAll.setBounds(167, 114, 56, 25);
		btnClearAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileName.setText("");
				fileTypeCombo.deselectAll();
				separator.setText("");
			}
		});
		btnClearAll.setText("Clear All");

		columnMeta = new Text(container, SWT.BORDER);
		columnMeta.setBounds(167, 60, 402, 21);
		columnMeta.setText(file.getFileColumnMeta().getColumnMetaName());

		if (file.getFileId() == null) {
			if (file != null) {
				if (file.getFileName() != null)
					setPageComplete(true);
			} else {
				setPageComplete(false);
			}
		}
		bindValues();

	}

	private void bindValues() {

		DataBindingContext ctx = new DataBindingContext();
		IObservableValue widgetValue = WidgetProperties.text(SWT.Modify).observe(fileName);
		IObservableValue modelValue = BeanProperties.value(Files.class, "fileName").observe(file);
		UpdateValueStrategy update = new UpdateValueStrategy();
		ctx.bindValue(widgetValue, modelValue, update, null);

		widgetValue = WidgetProperties.text(SWT.Modify).observe(separator);
		modelValue = BeanProperties.value(Files.class, "separator").observe(file);
		update = new UpdateValueStrategy();
		ctx.bindValue(widgetValue, modelValue, update, null);

	}
}
