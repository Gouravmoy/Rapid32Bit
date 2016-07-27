package views;

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
import dao.DatabaseDao;
import dao.TestScenarioDao;
import daoImpl.ColumnMetaDAOImpl;
import daoImpl.DatabaseDAOImpl;
import daoImpl.TestScenarioDaoImpl;
import entity.ColumnMeta;
import entity.Database;
import enums.ProjectType;
import exceptions.DAOException;
import exceptions.PersistException;
import exceptions.ReadEntityException;
import exceptions.ServiceException;

public class ProjectView {

	private FormToolkit toolkit;
	private Form form;

	TestScenarioDao testScenarioDao;
	DatabaseDao databaseDao;
	ColumnMetaDao columnMetaDao;
	List<Database> databaseList = null;
	List<ColumnMeta> colMetaList = null;
	MessageBox dialog;

	@Inject
	public ProjectView() {

	}

	/**
	 * @param parent
	 */
	@PostConstruct
	public void postConstruct(Composite parent) {
		testScenarioDao = new TestScenarioDaoImpl();
		databaseDao = new DatabaseDAOImpl();
		columnMetaDao = new ColumnMetaDAOImpl();

		dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK | SWT.CANCEL);

		try {
			databaseList = databaseDao.getAllDatabaseinDB();
			colMetaList = columnMetaDao.getAllColumnMetas();
		} catch (ReadEntityException e) {
			e.printStackTrace();
		} catch (DAOException e1) {
			e1.printStackTrace();
		}

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		form.setBounds(0, 0, 461, 298);
		form.setText("ADD PROJECT");
		form.getBody().setLayout(new GridLayout(3, false));

		Label lblProjectName = new Label(form.getBody(), SWT.NONE);
		toolkit.adapt(lblProjectName, true, true);
		lblProjectName.setText("PROJECT NAME");
		new Label(form.getBody(), SWT.NONE);

		Text projectName = new Text(form.getBody(), SWT.BORDER);
		GridData gd_text = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		gd_text.widthHint = 145;
		projectName.setLayoutData(gd_text);
		toolkit.adapt(projectName, true, true);

		Label lblProjectType = new Label(form.getBody(), SWT.NONE);
		toolkit.adapt(lblProjectType, true, true);
		lblProjectType.setText("PROJECT TYPE");
		new Label(form.getBody(), SWT.NONE);

		Combo type = new Combo(form.getBody(), SWT.NONE);
		GridData gd_type = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		gd_type.widthHint = 131;
		type.setLayoutData(gd_type);
		for (ProjectType projectType : ProjectType.values()) {
			type.add(projectType.toString());
		}
		toolkit.adapt(type);
		toolkit.paintBordersFor(type);

		Label lblColumnMeta = new Label(form.getBody(), SWT.NONE);
		toolkit.adapt(lblColumnMeta, true, true);
		lblColumnMeta.setText("COLUMN META");
		new Label(form.getBody(), SWT.NONE);

		Combo colMeta = new Combo(form.getBody(), SWT.NONE);
		GridData gd_colMeta = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_colMeta.widthHint = 132;
		colMeta.setLayoutData(gd_colMeta);
		toolkit.adapt(colMeta);
		toolkit.paintBordersFor(colMeta);

		Label lblSourceDatabase = new Label(form.getBody(), SWT.NONE);
		toolkit.adapt(lblSourceDatabase, true, true);
		lblSourceDatabase.setText("SOURCE DATABASE");
		new Label(form.getBody(), SWT.NONE);

		Combo sourceDB = new Combo(form.getBody(), SWT.NONE);
		GridData gd_sourceDB = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_sourceDB.widthHint = 133;
		sourceDB.setLayoutData(gd_sourceDB);
		for (Database database : databaseList) {
			sourceDB.add(database.getDatabaseName());
		}
		toolkit.adapt(sourceDB);
		toolkit.paintBordersFor(sourceDB);

		Label lblTargetDatabase = new Label(form.getBody(), SWT.NONE);
		toolkit.adapt(lblTargetDatabase, true, true);
		lblTargetDatabase.setText("TARGET DATABASE");
		new Label(form.getBody(), SWT.NONE);

		Combo targetDB = new Combo(form.getBody(), SWT.NONE);
		GridData gd_targetDB = new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1);
		gd_targetDB.widthHint = 131;
		targetDB.setLayoutData(gd_targetDB);
		for (Database database : databaseList) {
			targetDB.add(database.getDatabaseName());
		}
		toolkit.adapt(targetDB);
		toolkit.paintBordersFor(targetDB);

		Label lblSouceLookUp = new Label(form.getBody(), SWT.NONE);
		toolkit.adapt(lblSouceLookUp, true, true);
		lblSouceLookUp.setText("SOURCE LOOK UP DATABASE");
		new Label(form.getBody(), SWT.NONE);

		Combo sourceLookup = new Combo(form.getBody(), SWT.NONE);
		GridData gd_sourceLookup = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_sourceLookup.widthHint = 131;
		sourceLookup.setLayoutData(gd_sourceLookup);
		for (Database database : databaseList) {
			sourceLookup.add(database.getDatabaseName());
		}
		toolkit.adapt(sourceLookup);
		toolkit.paintBordersFor(sourceLookup);

		Label lblTargetLookUp = new Label(form.getBody(), SWT.NONE);
		toolkit.adapt(lblTargetLookUp, true, true);
		lblTargetLookUp.setText("TARGET LOOK UP DATABASE");
		new Label(form.getBody(), SWT.NONE);

		Combo targetLookUp = new Combo(form.getBody(), SWT.NONE);
		GridData gd_targetLookUp = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_targetLookUp.widthHint = 129;
		targetLookUp.setLayoutData(gd_targetLookUp);
		for (Database database : databaseList) {
			targetLookUp.add(database.getDatabaseName());
		}
		toolkit.adapt(targetLookUp);
		toolkit.paintBordersFor(targetLookUp);
		new Label(form.getBody(), SWT.NONE);
		new Label(form.getBody(), SWT.NONE);
		new Label(form.getBody(), SWT.NONE);

		Button btnCreateProject = new Button(form.getBody(), SWT.NONE);
		btnCreateProject.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		btnCreateProject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (type.getText() != null && sourceDB.getText() != null && targetDB.getText() != null
						&& sourceLookup.getText() != null && targetLookUp.getText() != null && colMeta.getText() != null
						&& projectName.getText() != null) {
					try {
						ColumnMeta columnMeta = getColMetaForName(colMeta.getText());
						columnMetaDao.update(columnMeta);
						//TreeViewPart.createTestScenariosNodes();
						//TreeViewPart.createColumnMetaNodes();
						WorkBenchTreesView.queryAndRefresh();
						dialog = new MessageBox(parent.getShell(), SWT.ARROW_RIGHT | SWT.OK);
						dialog.setText("Success!!");
						dialog.setMessage("Database - " + projectName.getText() + " added successfully!");
						reinitilizeAllFeilds();
					} catch (PersistException e1) {
						e1.printStackTrace();
					} catch (ReadEntityException e1) {
						e1.printStackTrace();
					} catch (DAOException e1) {
						e1.printStackTrace();
					} catch (ServiceException e1) {
						e1.printStackTrace();
					}
				} else {
					dialog.setText("Error");
					dialog.setMessage("Please fill all the details");
				}
			}

			private void reinitilizeAllFeilds() throws DAOException {
				projectName.setText("");
				type.deselectAll();
				colMeta.removeAll();
				colMetaList = columnMetaDao.getAllColumnMetas();
				sourceDB.deselectAll();
				targetDB.deselectAll();
				sourceLookup.deselectAll();
				targetLookUp.deselectAll();
			}
		});
		toolkit.adapt(btnCreateProject, true, true);
		btnCreateProject.setText("CREATE PROJECT");
		new Label(form.getBody(), SWT.NONE);
		new Label(form.getBody(), SWT.NONE);
	}

	public Long idForDatabase(String databaseName) {
		for (Database database : databaseList) {
			if (database.getDatabaseName().equals(databaseName))
				return database.getDbId();
		}
		return null;
	}

	public ColumnMeta getColMetaForName(String colMetaName) {
		for (ColumnMeta singleColMetaName : colMetaList) {
			if (singleColMetaName.getColumnMetaName().equals(colMetaName))
				return singleColMetaName;
		}
		return null;
	}
}
