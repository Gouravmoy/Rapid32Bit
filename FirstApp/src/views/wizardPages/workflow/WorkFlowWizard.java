package views.wizardPages.workflow;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import dao.ColumnDao;
import dao.ColumnMetaDao;
import dao.DatabaseDao;
import dao.FileDAO;
import dao.LookUpColumnDao;
import dao.QueryDao;
import dao.TestScenarioDao;
import daoImpl.ColumnDaoImpl;
import daoImpl.ColumnMetaDAOImpl;
import daoImpl.DatabaseDAOImpl;
import daoImpl.FileDAOImpl;
import daoImpl.LookUpQueryDaoImpl;
import daoImpl.QueryDaoImpl;
import daoImpl.TestScenarioDaoImpl;
import dataProvider.ListColumns;
import entity.Column;
import entity.ColumnMeta;
import entity.Database;
import entity.Files;
import entity.LookUpCols;
import entity.QueryEntity;
import entity.TestScenario;
import entity.TestSuite;
import enums.ProjectType;
import enums.QueryType;
import exceptions.DAOException;
import exceptions.ServiceException;
import views.WorkBenchTreesView;

public class WorkFlowWizard extends Wizard implements IPageChangedListener {

	protected ColumnMetaWizardPage columnMetaWizardPage;
	protected ColumnMetaWizardPage columnMetaTargetWizardPage;
	protected DatabaseWizardPage dbWizardSource;
	protected DatabaseWizardPage dbWizardSourceLookUp;
	protected DatabaseWizardPage dbWizardTarget;
	protected DatabaseWizardPage dbWizardTargetLookUp;

	protected FilesWizard sourceFileWizard;
	protected FilesWizard targetFileWizard;

	protected QueriesWizardPage queriesWizardPageSource;
	protected QueriesWizardPage queriesWizardPageTarget;
	protected LookUpQueriesWizardPage lookUpQueriesWizardPageSource;
	protected LookUpQueriesWizardPage lookUpQueriesWizardPageTarget;

	protected TestScenarioWizard scenarioWizard;
	protected TestSuiteWizard testSuiteWizard;

	FinishWizard finishWizard;

	Database database;
	ColumnMeta columnMeta;
	ColumnMeta columnMetaTarget;
	Files sourceFile;
	Files targetFile;
	QueryEntity sourceQueryEntity;
	QueryEntity targetQueryEntity;
	List<LookUpCols> lookUpColsSource;
	List<LookUpCols> lookUpColsTarget;
	List<Column> columns;
	List<Column> columnsTarget;
	TestScenario testScenario;

	FileDAO fileDAO;
	DatabaseDao databaseDao;
	QueryDao queryDao;
	LookUpColumnDao lookUpColumnDao;
	ColumnMetaDao columnMetaDao;
	TestScenarioDao testScenarioDao;
	ColumnDao columnDao;

	public WorkFlowWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public String getWindowTitle() {
		return "Test Scenario Work Flow";
	}

	@Override
	public void addPages() {
		WizardDialog dialog = (WizardDialog) getContainer();
		dialog.addPageChangedListener(this);

		database = new Database();
		columnMeta = new ColumnMeta();
		columnMetaTarget = new ColumnMeta();
		sourceFile = new Files();
		sourceFile.setFileColumnMeta(columnMeta);
		targetFile = new Files();
		targetFile.setFileColumnMeta(columnMetaTarget);

		sourceQueryEntity = new QueryEntity();
		sourceQueryEntity.setQueryColumnMeta(columnMeta);
		sourceQueryEntity.setQueryType(QueryType.SOURCE);
		targetQueryEntity = new QueryEntity();
		targetQueryEntity.setQueryColumnMeta(columnMeta);
		targetQueryEntity.setQueryType(QueryType.TARGET);
		lookUpColsSource = new ArrayList<LookUpCols>();
		lookUpColsTarget = new ArrayList<LookUpCols>();
		columns = new ArrayList<Column>();
		columnsTarget = new ArrayList<Column>();

		columnMetaWizardPage = new ColumnMetaWizardPage("Add Column metadata", columnMeta, columns);
		columnMetaTargetWizardPage = new ColumnMetaWizardPage("Add Column metadata target", columnMetaTarget,
				columnsTarget);
		dbWizardSource = new DatabaseWizardPage("Add  Source Database", new Database());
		dbWizardSourceLookUp = new DatabaseWizardPage("Add Source lookup Database", new Database());
		dbWizardTarget = new DatabaseWizardPage("Add  target Database", new Database());
		dbWizardTargetLookUp = new DatabaseWizardPage("Add target lookup Database", new Database());
		sourceFileWizard = new FilesWizard("Add Source File", sourceFile);
		targetFileWizard = new FilesWizard("Add Target File", targetFile);
		queriesWizardPageSource = new QueriesWizardPage("Add Source Queries", sourceQueryEntity);
		queriesWizardPageTarget = new QueriesWizardPage("Add Target Queries", targetQueryEntity);
		lookUpQueriesWizardPageSource = new LookUpQueriesWizardPage("Add Source Look Up Queries", lookUpColsSource,
				sourceQueryEntity);
		lookUpQueriesWizardPageTarget = new LookUpQueriesWizardPage("Add Target Look Up Queries", lookUpColsTarget,
				targetQueryEntity);
		scenarioWizard = new TestScenarioWizard("Add test Scenario", new TestScenario());
		testSuiteWizard = new TestSuiteWizard("Add Test Suite", new TestSuite());
		finishWizard = new FinishWizard();

		addPage(scenarioWizard);
		addPage(columnMetaWizardPage);
		addPage(columnMetaTargetWizardPage);
		addPage(dbWizardSource);
		addPage(dbWizardSourceLookUp);
		addPage(dbWizardTarget);
		addPage(dbWizardTargetLookUp);
		addPage(sourceFileWizard);
		addPage(targetFileWizard);
		addPage(queriesWizardPageSource);
		addPage(lookUpQueriesWizardPageSource);
		addPage(queriesWizardPageTarget);
		addPage(lookUpQueriesWizardPageTarget);
		addPage(finishWizard);

	}

	@Override
	public boolean canFinish() {
		if (getContainer().getCurrentPage().equals(finishWizard))
			return true;
		else
			return false;
	}

	@Override
	public boolean performFinish() {
		fileDAO = new FileDAOImpl();
		databaseDao = new DatabaseDAOImpl();
		queryDao = new QueryDaoImpl();
		lookUpColumnDao = new LookUpQueryDaoImpl();
		columnMetaDao = new ColumnMetaDAOImpl();
		columnDao = new ColumnDaoImpl();
		testScenarioDao = new TestScenarioDaoImpl();
		if (scenarioWizard.getType().getText().equals("File_To_DB")) {
			System.out.println(columnMetaWizardPage.getColumnMeta());
			System.out.println(sourceFileWizard.getFile());
			System.out.println(sourceFileWizard.getFile().getFileColumnMeta());
			System.out.println(dbWizardTarget.getDatabase());
			System.out.println(dbWizardTargetLookUp.getDatabase());
			System.out.println(queriesWizardPageTarget.getQueryEntity());
			try {
				columnMetaDao.saveColumnMeta(columnMetaWizardPage.getColumnMeta());
				columnDao.batchSave(columnMetaWizardPage.getColumnList());
				if (dbWizardTarget.getDatabase().getDbId() == null) {
					databaseDao.saveDatabse(dbWizardTarget.getDatabase());
					databaseDao.saveDatabse(dbWizardTargetLookUp.getDatabase());
				}
				fileDAO.saveFile(sourceFileWizard.getFile());
				queryDao.addQuery(queriesWizardPageTarget.getQueryEntity());
				for (LookUpCols lookUpCols : lookUpQueriesWizardPageTarget.getLookupCols()) {
					System.out.println(lookUpCols.getQuery());
					lookUpColumnDao.addLookUpQuery(lookUpCols);
				}
				testScenario = new TestScenario(scenarioWizard.getTestScenarioName().getText(),
						columnMetaWizardPage.getColumnMeta(), null, new Long(0), dbWizardTarget.getDatabase().getDbId(),
						new Long(0), dbWizardTargetLookUp.getDatabase().getDbId(),
						ProjectType.valueOf(scenarioWizard.getType().getText()),
						scenarioWizard.getScenarioDesc().getText());
				testScenarioDao.addTestScenarios(testScenario);
				WorkBenchTreesView.queryAndRefresh();
			} catch (DAOException | ServiceException e) {
				e.printStackTrace();
			}
		} else if (scenarioWizard.getType().getText().equals(ProjectType.File_To_File.toString())) {
			try {
				columnMetaDao.saveColumnMeta(columnMetaWizardPage.getColumnMeta());
				columnDao.batchSave(columnMetaWizardPage.getColumnList());
				fileDAO.saveFile(sourceFileWizard.getFile());
				columnMetaDao.saveColumnMeta(columnMetaTargetWizardPage.getColumnMeta());
				columnDao.batchSave(columnMetaTargetWizardPage.getColumnList());
				fileDAO.saveFile(targetFileWizard.getFile());
				testScenarioDao.addTestScenarios(new TestScenario(scenarioWizard.getTestScenarioName().getText(),
						columnMetaWizardPage.getColumnMeta(), columnMetaTargetWizardPage.getColumnMeta(), new Long(0),
						new Long(0), new Long(0), new Long(0), ProjectType.valueOf(scenarioWizard.getType().getText()),
						scenarioWizard.getScenarioDesc().getText()));
				WorkBenchTreesView.queryAndRefresh();
			} catch (DAOException e) {
				e.printStackTrace();
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		} else if (scenarioWizard.getType().getText().equals("DB_To_DB")) {
			try {
				columnMetaDao.saveColumnMeta(columnMetaWizardPage.getColumnMeta());
				columnDao.batchSave(columnMetaWizardPage.getColumnList());
				if (dbWizardSource.getDatabase().getDbId() == null) {
					databaseDao.saveDatabse(dbWizardSource.getDatabase());
					databaseDao.saveDatabse(dbWizardSourceLookUp.getDatabase());
				}
				if (dbWizardTarget.getDatabase().getDbId() == null) {
					databaseDao.saveDatabse(dbWizardTarget.getDatabase());
					databaseDao.saveDatabse(dbWizardTargetLookUp.getDatabase());
				}
				queryDao.addQuery(queriesWizardPageSource.getQueryEntity());
				for (LookUpCols lookUpCols : lookUpQueriesWizardPageSource.getLookupCols()) {
					System.out.println(lookUpCols.getQuery());
					lookUpColumnDao.addLookUpQuery(lookUpCols);
				}
				queryDao.addQuery(queriesWizardPageTarget.getQueryEntity());
				for (LookUpCols lookUpCols : lookUpQueriesWizardPageTarget.getLookupCols()) {
					System.out.println(lookUpCols.getQuery());
					lookUpColumnDao.addLookUpQuery(lookUpCols);
				}
				testScenarioDao.addTestScenarios(new TestScenario(scenarioWizard.getTestScenarioName().getText(),
						columnMetaWizardPage.getColumnMeta(), null, dbWizardSource.getDatabase().getDbId(),
						dbWizardTarget.getDatabase().getDbId(), dbWizardSourceLookUp.getDatabase().getDbId(),
						dbWizardTargetLookUp.getDatabase().getDbId(),
						ProjectType.valueOf(scenarioWizard.getType().getText()),
						scenarioWizard.getScenarioDesc().getText()));
				WorkBenchTreesView.queryAndRefresh();
			} catch (DAOException | ServiceException e) {
				e.printStackTrace();
			}
		}
		Display.getCurrent().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					// TreeViewPart.queryAndRefresh();
					WorkBenchTreesView.queryAndRefresh();
				} catch (DAOException | ServiceException e) {
					e.printStackTrace();
				}
			}
		});

		return true;
	}

	@Override
	public void pageChanged(PageChangedEvent arg0) {
		switch (arg0.getSelectedPage().toString()) {
		case "Add Source File":
			FilesWizard filesWizard = (FilesWizard) arg0.getSelectedPage();
			sourceFile.setFileColumnMeta(columnMetaWizardPage.getColumnMeta());
			filesWizard.setFile(sourceFile);
			break;
		case "Add Target File":
			FilesWizard filesTargetWizard = (FilesWizard) arg0.getSelectedPage();
			targetFile.setFileColumnMeta(columnMetaTargetWizardPage.getColumnMeta());
			filesTargetWizard.setFile(targetFile);
			break;
		case "Add Source Queries":
			QueriesWizardPage queriesWizardPage = (QueriesWizardPage) arg0.getSelectedPage();
			sourceQueryEntity.setQueryColumnMeta(columnMetaWizardPage.getColumnMeta());
			queriesWizardPage.setQueryEntity(sourceQueryEntity);
			break;
		case "Add Target Queries":
			QueriesWizardPage queriesWizardPage1 = (QueriesWizardPage) arg0.getSelectedPage();
			targetQueryEntity.setQueryColumnMeta(columnMetaWizardPage.getColumnMeta());
			queriesWizardPage1.setQueryEntity(targetQueryEntity);
			break;
		case "Add Source Look Up Queries":
			LookUpQueriesWizardPage lookUpQueriesWizardPage = (LookUpQueriesWizardPage) arg0.getSelectedPage();
			for (LookUpCols cols : lookUpColsSource) {
				cols.setQuery(queriesWizardPageSource.getQueryEntity());
			}
			lookUpQueriesWizardPage.setLookupCols(lookUpColsSource);
			ArrayList<String> columns = new ArrayList<String>();
			for (Column column : columnMetaWizardPage.getColumnList()) {
				columns.add(column.getColumnName());
			}
			ListColumns.INSTANCES = columns.toArray(ListColumns.INSTANCES);
			break;
		case "Add Target Look Up Queries":
			LookUpQueriesWizardPage lookUpQueriesWizardPage1 = (LookUpQueriesWizardPage) arg0.getSelectedPage();
			for (LookUpCols cols : lookUpColsTarget) {
				cols.setQuery(queriesWizardPageTarget.getQueryEntity());
			}
			lookUpQueriesWizardPage1.setLookupCols(lookUpColsTarget);
			ArrayList<String> columns1 = new ArrayList<String>();
			for (Column column : columnMetaWizardPage.getColumnList()) {
				columns1.add(column.getColumnName());
			}
			ListColumns.INSTANCES = columns1.toArray(ListColumns.INSTANCES);
			break;
		}
	}

	@Override
	public IWizardPage getNextPage(IWizardPage currentPage) {
		if (scenarioWizard.getType().getText().length() > 0) {
			ProjectType projectType = ProjectType.valueOf(scenarioWizard.getType().getText());
			if (projectType.equals(ProjectType.File_To_DB)) {
				if (currentPage == scenarioWizard)
					return columnMetaWizardPage;
				else if (currentPage == columnMetaWizardPage)
					return sourceFileWizard;
				else if (currentPage == sourceFileWizard)
					return dbWizardTarget;
				else if (currentPage == dbWizardTarget)
					return dbWizardTargetLookUp;
				else if (currentPage == dbWizardTargetLookUp)
					return queriesWizardPageTarget;
				else if (currentPage == queriesWizardPageTarget)
					return lookUpQueriesWizardPageTarget;
				else if (currentPage == lookUpQueriesWizardPageTarget)
					return finishWizard;
			} else if (projectType.equals(ProjectType.DB_To_DB)) {
				if (currentPage == scenarioWizard)
					return columnMetaWizardPage;
				else if (currentPage == columnMetaWizardPage)
					return dbWizardSource;
				else if (currentPage == dbWizardSource)
					return dbWizardSourceLookUp;
				else if (currentPage == dbWizardSourceLookUp)
					return dbWizardTarget;
				else if (currentPage == dbWizardTarget)
					return dbWizardTargetLookUp;
				else if (currentPage == dbWizardTargetLookUp)
					return queriesWizardPageSource;
				else if (currentPage == queriesWizardPageSource)
					return lookUpQueriesWizardPageSource;
				else if (currentPage == lookUpQueriesWizardPageSource)
					return queriesWizardPageTarget;
				else if (currentPage == queriesWizardPageTarget)
					return lookUpQueriesWizardPageTarget;
				else if (currentPage == lookUpQueriesWizardPageTarget)
					return finishWizard;
			} else if (projectType.equals(ProjectType.File_To_File)) {
				if (currentPage == scenarioWizard)
					return columnMetaWizardPage;
				else if (currentPage == columnMetaWizardPage)
					return sourceFileWizard;
				else if (currentPage == sourceFileWizard)
					return columnMetaTargetWizardPage;
				else if (currentPage == columnMetaTargetWizardPage)
					return targetFileWizard;
				else if (currentPage == targetFileWizard)
					return finishWizard;
			} else if (projectType.equals(ProjectType.DB_To_File)) {

			}
		}
		return currentPage;
	}

}
