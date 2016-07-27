
package views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import connection.ConnectToDataBase;
import entity.Database;
import enums.DBTypes;
import exceptions.DAOException;
import exceptions.ServiceException;
import service.DataBaseService;
import serviceImpl.DataBaseServiceImpl;

public class DataBaseForm {
	DataBaseService dataBaseService;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text nameText;
	private Text userNameText;
	private Text passWordText;
	private Text portText;
	private Text serverText;
	private Text connectionNameText;
	private Text connectionDescText;

	@Inject
	public DataBaseForm() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		dataBaseService = new DataBaseServiceImpl();
		Form frmNewForm = formToolkit.createForm(parent);
		formToolkit.paintBordersFor(frmNewForm);
		frmNewForm.setText("ADD DATABASE");
		ComboViewer comboViewer = new ComboViewer(frmNewForm.getBody(), SWT.NONE);
		Combo combo = comboViewer.getCombo();
		for (DBTypes dbTypes : DBTypes.values()) {
			combo.add(dbTypes.toString());
		}
		combo.setBounds(144, 257, 252, 23);
		Button btnAdd = formToolkit.createButton(frmNewForm.getBody(), "ADD", SWT.NONE);
		btnAdd.setEnabled(false);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (connectionNameText.getText() != null) {

					try {
						Database database = new Database(connectionNameText.getText(), connectionDescText.getText(),
								nameText.getText(), serverText.getText(), userNameText.getText(),
								passWordText.getText(), portText.getText(), DBTypes.valueOf(combo.getText()));
						if (ConnectToDataBase.getConnection(database) != null) {
							dataBaseService.addDataBase(connectionNameText.getText(), connectionDescText.getText(),
									nameText.getText(), serverText.getText(), userNameText.getText(),
									passWordText.getText(), portText.getText(), combo.getText());
							WorkBenchTreesView.createMetaDataTree();
							btnAdd.setEnabled(false);
							MessageDialog.openConfirm(parent.getShell(), "Success", "Connection Added Successfully");
						} else {
							MessageDialog.openError(parent.getShell(), "Unable To Connect", "Check Parameters");
						}
					} catch (ServiceException | DAOException e1) {
						e1.printStackTrace();
						MessageDialog.openError(parent.getShell(), "Unable To Connect",
								"Please use unique Connection Name");
					} catch (Exception e1) {
						MessageDialog.openError(parent.getShell(), "Unable To Connect", "Check Parameters");
						e1.printStackTrace();
					}
				} else {
					MessageDialog.openError(parent.getShell(), "Error", "Please Fill the Connection Name");
				}
			}
		});
		btnAdd.setBounds(144, 298, 75, 25);

		Label databaseName = formToolkit.createLabel(frmNewForm.getBody(), "DB Name", SWT.NONE);
		databaseName.setBounds(10, 88, 75, 15);

		Label dbUserName = formToolkit.createLabel(frmNewForm.getBody(), "UserName", SWT.NONE);
		dbUserName.setBounds(10, 122, 75, 15);

		Label dbPswd = formToolkit.createLabel(frmNewForm.getBody(), "Password", SWT.NONE);
		dbPswd.setBounds(10, 156, 75, 15);

		Label dbPort = formToolkit.createLabel(frmNewForm.getBody(), "Port", SWT.NONE);
		dbPort.setBounds(10, 190, 75, 15);

		Label server = formToolkit.createLabel(frmNewForm.getBody(), "Server URL", SWT.NONE);
		server.setBounds(10, 228, 75, 15);

		nameText = formToolkit.createText(frmNewForm.getBody(), "", SWT.NONE);
		nameText.setBounds(144, 82, 252, 21);

		userNameText = formToolkit.createText(frmNewForm.getBody(), "", SWT.NONE);
		userNameText.setBounds(144, 116, 252, 21);

		passWordText = formToolkit.createText(frmNewForm.getBody(), "", SWT.NONE);
		passWordText.setBounds(144, 150, 252, 21);

		portText = formToolkit.createText(frmNewForm.getBody(), "", SWT.NONE);
		portText.setBounds(144, 184, 252, 21);

		serverText = formToolkit.createText(frmNewForm.getBody(), "", SWT.NONE);
		serverText.setBounds(144, 222, 252, 21);

		Label dataBaseTypeLabel = new Label(frmNewForm.getBody(), SWT.NONE);
		dataBaseTypeLabel.setBounds(10, 265, 97, 15);
		formToolkit.adapt(dataBaseTypeLabel, true, true);
		dataBaseTypeLabel.setText("Database Type");

		formToolkit.paintBordersFor(combo);

		Button btnTest = new Button(frmNewForm.getBody(), SWT.NONE);
		btnTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (connectionNameText.getText() != null) {
					try {
						Database database = new Database(connectionNameText.getText(), connectionDescText.getText(),
								nameText.getText(), serverText.getText(), userNameText.getText(),
								passWordText.getText(), portText.getText(), DBTypes.valueOf(combo.getText()));
						if (ConnectToDataBase.getConnection(database) != null) {
							MessageDialog.openConfirm(parent.getShell(), "Valid", "Valid Connection");
							btnAdd.setEnabled(true);
						} else {
							MessageDialog.openError(parent.getShell(), "Unable To Connect", "Check Parameters");
						}
					} catch (Exception e1) {
						MessageDialog.openError(parent.getShell(), "Unable To Connect", "Check Parameters");
						e1.printStackTrace();
					}
				} else {
					MessageDialog.openError(parent.getShell(), "Error", "Please Fill the Connection Name");
				}

			}
		});
		btnTest.setBounds(321, 298, 75, 25);
		formToolkit.adapt(btnTest, true, true);
		btnTest.setText("Test");

		Label lblConnectionDesc = new Label(frmNewForm.getBody(), SWT.NONE);
		lblConnectionDesc.setBounds(10, 55, 133, 15);
		formToolkit.adapt(lblConnectionDesc, true, true);
		lblConnectionDesc.setText("Connection Desc");

		Label lblConnectionName = new Label(frmNewForm.getBody(), SWT.NONE);
		lblConnectionName.setBounds(10, 24, 133, 15);
		formToolkit.adapt(lblConnectionName, true, true);
		lblConnectionName.setText("Connection  Name");

		connectionNameText = new Text(frmNewForm.getBody(), SWT.BORDER);
		connectionNameText.setBounds(144, 24, 252, 21);
		formToolkit.adapt(connectionNameText, true, true);

		connectionDescText = new Text(frmNewForm.getBody(), SWT.BORDER);
		connectionDescText.setBounds(144, 52, 253, 21);
		formToolkit.adapt(connectionDescText, true, true);
		frmNewForm.getBody().setTabList(
				new Control[] { btnAdd, nameText, userNameText, passWordText, portText, serverText, combo });

	}
}