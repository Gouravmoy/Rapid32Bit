package views.wizardPages.workflow;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import connection.ConnectToDataBase;
import dao.DatabaseDao;
import daoImpl.DatabaseDAOImpl;
import entity.Database;
import enums.DBTypes;
import exceptions.ReadEntityException;
import service.DataBaseService;

public class DatabaseWizardPage extends WizardPage {

	private Composite container;
	DatabaseDao databaseDao;
	DataBaseService databaseService;
	Database database;
	private Text connectionName;
	private Text connectionDescription;
	private Text dbName;
	private Text dbUrl;
	private Text dbPortNo;
	private Text dbUserName;
	private Text dbPassword;

	private Combo dbTypecombo;

	private Label label;
	private Label lblDatabaseName;
	private Label lblDatabaseType;
	private Label lblServer;
	private Label lblPortNumber;
	private Label lblUserName;
	private Label lblPassword;
	private Button btnTestConnection;

	public DatabaseWizardPage(String pName, Database database) {
		super(pName);
		setTitle(pName);
		setDescription("Create a Database Connection");
		this.database = database;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	@Override
	public void createControl(Composite parent) {

		container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(null);

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setBounds(1, 44, 148, 15);
		lblNewLabel.setText("Database Connection Name");

		connectionName = new Text(container, SWT.BORDER);
		connectionName.setBounds(158, 41, 411, 21);
		connectionName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				setPageComplete(true);

			}
		});

		Label lblDescription = new Label(container, SWT.NONE);
		lblDescription.setBounds(1, 71, 60, 15);
		lblDescription.setText("Description");

		connectionDescription = new Text(container, SWT.BORDER);
		connectionDescription.setBounds(158, 68, 411, 23);

		label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 110, 559, 2);

		lblDatabaseType = new Label(container, SWT.NONE);
		lblDatabaseType.setBounds(5, 141, 77, 15);
		lblDatabaseType.setText("Database Type");

		dbTypecombo = new Combo(container, SWT.NONE);
		dbTypecombo.setBounds(158, 137, 411, 23);
		for (DBTypes dbType : DBTypes.values()) {
			dbTypecombo.add(dbType.toString());
		}
		dbTypecombo.setText(DBTypes.MYSQL.toString());
		dbTypecombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				database.setDbType(DBTypes.valueOf(dbTypecombo.getText()));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				if (database.getDbType() != null) {
					dbTypecombo.select(database.getDbType().ordinal());
				}

			}
		});
		lblDatabaseName = new Label(container, SWT.NONE);
		lblDatabaseName.setBounds(5, 168, 81, 15);
		lblDatabaseName.setText("Database name");

		dbName = new Text(container, SWT.BORDER);
		dbName.setBounds(158, 165, 411, 21);

		lblServer = new Label(container, SWT.NONE);
		lblServer.setBounds(5, 194, 56, 15);
		lblServer.setText("Server URL");

		dbUrl = new Text(container, SWT.BORDER);
		dbUrl.setBounds(158, 191, 411, 21);

		lblPortNumber = new Label(container, SWT.NONE);
		lblPortNumber.setBounds(5, 220, 69, 15);
		lblPortNumber.setText("Port Number");

		dbPortNo = new Text(container, SWT.BORDER);
		dbPortNo.setBounds(158, 217, 411, 21);

		lblUserName = new Label(container, SWT.NONE);
		lblUserName.setBounds(5, 246, 58, 15);
		lblUserName.setText("User Name");

		dbUserName = new Text(container, SWT.BORDER);
		dbUserName.setBounds(158, 243, 411, 21);

		lblPassword = new Label(container, SWT.NONE);
		lblPassword.setBounds(5, 272, 50, 15);
		lblPassword.setText("Password");

		dbPassword = new Text(container, SWT.BORDER);
		dbPassword.setBounds(158, 269, 411, 21);

		btnTestConnection = new Button(container, SWT.NONE);
		btnTestConnection.setBounds(158, 296, 99, 25);
		btnTestConnection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if (ConnectToDataBase.getConnection(database) != null) {
						MessageDialog.openConfirm(parent.getShell(), "Valid", "Valid Connection");
					}
				} catch (Exception e1) {
					MessageDialog.openError(parent.getShell(), "Unable To Connect", "Check Parameters");
					e1.printStackTrace();
				}
			}
		});

		btnTestConnection.setText("Test Connection");
		Button btnClearAll = new Button(container, SWT.NONE);
		btnClearAll.setBounds(276, 296, 75, 25);
		btnClearAll.setText("Clear All");

		Combo databaseCombo = new Combo(container, SWT.NONE);
		databaseCombo.setBounds(158, 12, 411, 23);
		databaseDao = new DatabaseDAOImpl();
		try {
			for (Database database : databaseDao.getAllDatabaseinDB()) {
				databaseCombo.add(database.getConnectionName());
				databaseCombo.setData(database.getConnectionName(), database);
			}
		} catch (ReadEntityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		databaseCombo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				database = (Database) databaseCombo.getData(databaseCombo.getText());
				bindValues();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}
		});

		Label databaseLabel = new Label(container, SWT.NONE);
		databaseLabel.setText("Select Existing database");
		databaseLabel.setBounds(1, 12, 148, 15);
		bindValues();
		if (database.getDbId() == null) {
			setPageComplete(false);
		}

	}

	private void bindValues() {
		DataBindingContext ctx = new DataBindingContext();
		IObservableValue widgetValue = WidgetProperties.text(SWT.Modify).observe(connectionName);
		IObservableValue modelValue = BeanProperties.value(Database.class, "connectionName").observe(database);
		UpdateValueStrategy update = new UpdateValueStrategy();
		ctx.bindValue(widgetValue, modelValue, update, null);

		widgetValue = WidgetProperties.text(SWT.Modify).observe(connectionDescription);
		modelValue = BeanProperties.value(Database.class, "connectionDescription").observe(database);
		update = new UpdateValueStrategy();
		ctx.bindValue(widgetValue, modelValue, update, null);

		widgetValue = WidgetProperties.text(SWT.Modify).observe(dbName);
		modelValue = BeanProperties.value(Database.class, "databaseName").observe(database);
		update = new UpdateValueStrategy();
		ctx.bindValue(widgetValue, modelValue, update, null);

		widgetValue = WidgetProperties.text(SWT.Modify).observe(dbUrl);
		modelValue = BeanProperties.value(Database.class, "serverName").observe(database);
		update = new UpdateValueStrategy();
		ctx.bindValue(widgetValue, modelValue, update, null);

		widgetValue = WidgetProperties.text(SWT.Modify).observe(dbPortNo);
		modelValue = BeanProperties.value(Database.class, "portNo").observe(database);
		update = new UpdateValueStrategy();
		ctx.bindValue(widgetValue, modelValue, update, null);

		widgetValue = WidgetProperties.text(SWT.Modify).observe(dbUserName);
		modelValue = BeanProperties.value(Database.class, "userName").observe(database);
		update = new UpdateValueStrategy();
		ctx.bindValue(widgetValue, modelValue, update, null);

		widgetValue = WidgetProperties.text(SWT.Modify).observe(dbPassword);
		modelValue = BeanProperties.value(Database.class, "password").observe(database);
		update = new UpdateValueStrategy();
		ctx.bindValue(widgetValue, modelValue, update, null);

	}
}
