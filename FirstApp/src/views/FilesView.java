package views;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import dao.ColumnMetaDao;
import dao.FileDAO;
import daoImpl.ColumnMetaDAOImpl;
import daoImpl.FileDAOImpl;
import entity.ColumnMeta;
import entity.Files;
import enums.FileTypes;
import exceptions.DAOException;

public class FilesView {

	private FormToolkit toolkit;
	private Form form;

	public static final String ID = "views.FilesView"; //$NON-NLS-1$
	private Text text;
	ColumnMetaDao columnMetaDao;
	FileDAO fileDAO;
	List<ColumnMeta> colMetaList = null;
	MessageBox dialog;
	private Text separators;

	@Inject
	public FilesView() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		columnMetaDao = new ColumnMetaDAOImpl();
		fileDAO = new FileDAOImpl();
		dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK | SWT.CANCEL);
		try {
			colMetaList = columnMetaDao.getAllColumnMetas();
		} catch (DAOException e) {
			e.printStackTrace();
		}

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		form.setBounds(0, 0, 348, 298);
		form.setText("ADD FILES");
		form.getBody().setLayout(new GridLayout(3, false));

		Label lblFileName = new Label(form.getBody(), SWT.NONE);
		toolkit.adapt(lblFileName, true, true);
		lblFileName.setText("FILE NAME");
		new Label(form.getBody(), SWT.NONE);

		text = new Text(form.getBody(), SWT.BORDER);
		GridData gd_text_1 = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		gd_text_1.widthHint = 141;
		text.setLayoutData(gd_text_1);
		toolkit.adapt(text, true, true);

		Label lblFileType = new Label(form.getBody(), SWT.NONE);
		toolkit.adapt(lblFileType, true, true);
		lblFileType.setText("FILE TYPE");
		new Label(form.getBody(), SWT.NONE);

		Combo fileType = new Combo(form.getBody(), SWT.NONE);
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo.widthHint = 126;

		for (FileTypes fileTypes : FileTypes.values()) {
			fileType.add(fileTypes.toString());
		}
		fileType.setLayoutData(gd_combo);
		toolkit.adapt(fileType);
		toolkit.paintBordersFor(fileType);

		Label lblColumnMeta = new Label(form.getBody(), SWT.NONE);
		toolkit.adapt(lblColumnMeta, true, true);
		lblColumnMeta.setText("COLUMN META");
		new Label(form.getBody(), SWT.NONE);

		Combo colMetaDrop = new Combo(form.getBody(), SWT.NONE);
		GridData gd_combo_1 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo_1.widthHint = 127;


		colMetaDrop.setLayoutData(gd_combo_1);
		toolkit.adapt(colMetaDrop);
		toolkit.paintBordersFor(colMetaDrop);

		Label lblSeparator = new Label(form.getBody(), SWT.NONE);
		toolkit.adapt(lblSeparator, true, true);
		lblSeparator.setText("SEPARATOR");
		new Label(form.getBody(), SWT.NONE);

		separators = new Text(form.getBody(), SWT.BORDER);
		GridData gd_text_11 = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		gd_text_11.widthHint = 143;
		if (fileType.getText().equals("CSV")) {
			separators.setText(",");
			separators.setEnabled(false);
		}
		separators.setLayoutData(gd_text_11);
		toolkit.adapt(separators, true, true);
		new Label(form.getBody(), SWT.NONE);
		new Label(form.getBody(), SWT.NONE);
		new Label(form.getBody(), SWT.NONE);

		Button btnAddFiles = new Button(form.getBody(), SWT.NONE);
		btnAddFiles.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		btnAddFiles.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (text.getText() != null && fileType.getText() != null && colMetaDrop.getText() != null
						&& separators.getText() != null) {
					ColumnMeta columnMeta = getColMetaForName(colMetaDrop.getText());
					try {
						fileDAO.saveFile(new Files(FileTypes.valueOf(fileType.getText()), text.getText(), columnMeta,
								separators.getText()));
						//TreeViewPart.createFileNodes();
						WorkBenchTreesView.createMetaDataTree();
						dialog = new MessageBox(parent.getShell(), SWT.ARROW_RIGHT | SWT.OK);
						dialog.setText("Success!!");
						dialog.setMessage("File - " + text.getText() + " added successfully!");
						reinitilizeAllFeilds();
					} catch (DAOException e1) {
						e1.printStackTrace();
					}
				}
			}

			private void reinitilizeAllFeilds() throws DAOException {
				text.setText("");
				fileType.deselectAll();
				colMetaList = new ArrayList<>();
				colMetaList = columnMetaDao.getAllColumnMetas();
				colMetaDrop.removeAll();
				colMetaDrop.deselectAll();
			}
		});
		toolkit.adapt(btnAddFiles, true, true);
		btnAddFiles.setText("ADD FILES");
		new Label(form.getBody(), SWT.NONE);
		new Label(form.getBody(), SWT.NONE);

	}

	public ColumnMeta getColMetaForName(String colMetaName) {
		for (ColumnMeta singleColMetaName : colMetaList) {
			if (singleColMetaName.getColumnMetaName().equals(colMetaName))
				return singleColMetaName;
		}
		return null;
	}
}
