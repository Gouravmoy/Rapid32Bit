package views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import dao.ColumnMetaDao;
import daoImpl.ColumnMetaDAOImpl;
import entity.ColumnMeta;
import exceptions.DAOException;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;

public class ColumnMetaView {
	private FormToolkit toolkit;
	private Form form;

	public static final String ID = "views.ColumnMetaView"; //$NON-NLS-1$

	ColumnMetaDao columnMetaDao;
	MessageBox dialog;
	private Text colMetaName;
	private Text colNames;
	private Label lblListOfColumns;
	private Button btnAddColumnMeta;
	private Label lblListOfUnique;
	private Text uniqueColtext;

	@Inject
	public ColumnMetaView() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		columnMetaDao = new ColumnMetaDAOImpl();
		dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK | SWT.CANCEL);

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		form.getBody().setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
		form.setBounds(0, 0, 348, 298);
		form.setText("ADD COLUMN META DATA");
		form.getBody().setLayout(new GridLayout(3, false));

		Label lblNewLabel = new Label(form.getBody(), SWT.NONE);
		toolkit.adapt(lblNewLabel, true, true);
		lblNewLabel.setText("COLUMN META NAME");
		new Label(form.getBody(), SWT.NONE);

		colMetaName = new Text(form.getBody(), SWT.BORDER);
		colMetaName.setToolTipText("");
		GridData gd_text = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_text.widthHint = 187;
		colMetaName.setLayoutData(gd_text);
		toolkit.adapt(colMetaName, true, true);

		lblListOfColumns = new Label(form.getBody(), SWT.NONE);
		toolkit.adapt(lblListOfColumns, true, true);
		lblListOfColumns.setText("LIST OF COLUMNS");
		new Label(form.getBody(), SWT.NONE);

		colNames = new Text(form.getBody(), SWT.BORDER);
		colNames.setToolTipText("Use comma (\",\") as separator between columns");
		GridData gd_text_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 5);
		gd_text_1.heightHint = 72;
		gd_text_1.widthHint = 172;
		colNames.setLayoutData(gd_text_1);
		toolkit.adapt(colNames, true, true);
		new Label(form.getBody(), SWT.NONE);
		new Label(form.getBody(), SWT.NONE);
		new Label(form.getBody(), SWT.NONE);
		new Label(form.getBody(), SWT.NONE);
		new Label(form.getBody(), SWT.NONE);
		new Label(form.getBody(), SWT.NONE);
		new Label(form.getBody(), SWT.NONE);
		new Label(form.getBody(), SWT.NONE);

		lblListOfUnique = new Label(form.getBody(), SWT.NONE);
		toolkit.adapt(lblListOfUnique, true, true);
		lblListOfUnique.setText("LIST OF UNIQUE COLS");
		new Label(form.getBody(), SWT.NONE);

		uniqueColtext = new Text(form.getBody(), SWT.BORDER);
		uniqueColtext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolkit.adapt(uniqueColtext, true, true);

		btnAddColumnMeta = new Button(form.getBody(), SWT.NONE);

		btnAddColumnMeta.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (colMetaName.getText() != null && colNames.getText() != null && uniqueColtext.getText() != null) {
					try {
						columnMetaDao.saveColumnMeta(
								new ColumnMeta(colMetaName.getText(), colNames.getText(), uniqueColtext.getText()));

						//TreeViewPart.createColumnMetaNodes();
						WorkBenchTreesView.createMetaDataTree();
						dialog = new MessageBox(parent.getShell(), SWT.ARROW_RIGHT | SWT.OK);
						dialog.setText("Success!!");
						dialog.setMessage("Column Meta - " + colMetaName.getText() + " added successfully!");
						reinitilizeAllFeilds();

					} catch (DAOException e1) {
						e1.printStackTrace();
					}
				}
			}

			private void reinitilizeAllFeilds() {
				colMetaName.setText("");
				colNames.setText("");
			}
		});

		toolkit.adapt(btnAddColumnMeta, true, true);
		btnAddColumnMeta.setText("ADD COLUMN META");
		new Label(form.getBody(), SWT.NONE);
		new Label(form.getBody(), SWT.NONE);

	}
}
