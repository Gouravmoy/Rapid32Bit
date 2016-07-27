package views.wizardPages.workflow;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import dao.ColumnDao;
import dao.ColumnMetaDao;
import dao.DatabaseDao;
import dao.FileDAO;
import dao.LookUpColumnDao;
import dao.QueryDao;
import dao.TestScenarioDao;
import dao.TestSuiteDao;
import daoImpl.AllTestSuiteDaoImpl;
import daoImpl.ColumnDaoImpl;
import daoImpl.ColumnMetaDAOImpl;
import daoImpl.DatabaseDAOImpl;
import daoImpl.FileDAOImpl;
import daoImpl.LookUpQueryDaoImpl;
import daoImpl.QueryDaoImpl;
import daoImpl.TestScenarioDaoImpl;
import entity.Column;
import entity.ColumnMeta;
import entity.Database;
import entity.Files;
import entity.LookUpCols;
import entity.QueryEntity;
import entity.TestScenario;
import entity.TestSuite;
import exceptions.DAOException;
import exceptions.EntityNotPresent;
import exceptions.ServiceException;
import service.LookUpQueryService;
import serviceImpl.LookUpQueryServiceImpl;
import views.WorkBenchTreesView;

public class EditWizard extends Wizard {

	DatabaseDao databaseDao;
	ColumnMetaDao columnMetaDao;
	FileDAO fileDAO;
	QueryDao queryDao;
	TestScenarioDao testScenarioDao;
	TestSuiteDao suiteDao;
	LookUpColumnDao lookUpColumnDao;
	LookUpQueryService lookUpQueryService;
	ColumnDao columnDao;

	Database database;
	ColumnMeta columnMeta;
	Files files;
	QueryEntity queryEntity;
	TestScenario testScenario;
	TestSuite testSuite;
	List<LookUpCols> lookUpCols;
	List<Column> columnList;

	DatabaseWizardPage databaseWizard;
	ColumnMetaWizardPage columnMetaWizardPage;
	FilesWizard filesWizard;
	QueriesWizardPage queriesWizardPage;
	TestScenarioWizard scenarioWizard;
	TestSuiteWizard suiteWizard;
	LookUpQueriesWizardPage lookUpQueriesWizardPage;

	String[] listColumn;

	public EditWizard(Object editObject) {
		super();
		if (editObject instanceof Database) {
			this.database = (Database) editObject;
		} else if (editObject instanceof ColumnMeta) {
			columnMeta = (ColumnMeta) editObject;
			columnDao = new ColumnDaoImpl();
			try {
				this.columnList = new ArrayList<>();
				for (Column column : columnDao.getColumnByCMId(columnMeta.getIdColumnMeta())) {
					Column columnOne = column;
					columnList.add(columnOne);
				}
			} catch (EntityNotPresent e) {
				e.printStackTrace();
			}
		} else if (editObject instanceof Files) {
			this.files = (Files) editObject;
		} else if (editObject instanceof QueryEntity) {
			this.queryEntity = (QueryEntity) editObject;
			lookUpQueryService = new LookUpQueryServiceImpl();
			try {
				lookUpCols = lookUpQueryService.getLookUpColsByQueryId(queryEntity.getQueryId());
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		} else if (editObject instanceof TestScenario) {
			this.testScenario = (TestScenario) editObject;
		} else if (editObject instanceof TestSuite) {
			this.testSuite = (TestSuite) editObject;
		}
	}

	@Override
	public boolean performFinish() {
		System.out.println("Edit Here");
		try {
			if (database != null) {
				databaseDao = new DatabaseDAOImpl();
				databaseDao.update(database);
			} else if (columnMeta != null) {
				columnMetaDao = new ColumnMetaDAOImpl();
				columnMetaDao.update(columnMeta);
				columnDao.updateBatch(columnList);
			} else if (files != null) {
				fileDAO = new FileDAOImpl();
				fileDAO.update(files);
			} else if (queryEntity != null) {
				queryDao = new QueryDaoImpl();
				queryDao.update(queryEntity);
				listColumn = new String[0];
				for (LookUpCols lookUpCols : lookUpCols) {
					lookUpColumnDao = new LookUpQueryDaoImpl();
					lookUpCols.setQuery(queryEntity);
					lookUpColumnDao.saveOrUpdateLookUpQuery(lookUpCols);
				}
			} else if (testScenario != null) {
				testScenarioDao = new TestScenarioDaoImpl();
				testScenarioDao.updateTestScenario(testScenario);
			} else if (testSuite != null) {
				suiteDao = new AllTestSuiteDaoImpl();
				suiteDao.update(testSuite);
			}
			WorkBenchTreesView.queryAndRefresh();
		} catch (EntityNotPresent e) {
			e.printStackTrace();
		} catch (DAOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void addPage(IWizardPage page) {
		super.addPage(page);
	}

	@Override
	public void addPages() {
		if (database != null) {
			databaseWizard = new DatabaseWizardPage("Edit Database", database);
			addPage(databaseWizard);
		} else if (columnMeta != null) {
			columnMetaWizardPage = new ColumnMetaWizardPage("Edit Column Metadata", columnMeta, columnList);
			addPage(columnMetaWizardPage);
		} else if (files != null) {
			filesWizard = new FilesWizard("Edit Files Metadata", files);
			addPage(filesWizard);
		} else if (queryEntity != null) {
			queriesWizardPage = new QueriesWizardPage("Edit Queries Metadata", queryEntity);
			lookUpQueriesWizardPage = new LookUpQueriesWizardPage("Edit LookUp Column Metadata", lookUpCols,
					queryEntity);
			addPage(queriesWizardPage);
			addPage(lookUpQueriesWizardPage);
			if (listColumn != null)
				addPage(lookUpQueriesWizardPage);
		} else if (testScenario != null) {
			scenarioWizard = new TestScenarioWizard("Edit Test Scenario Metadata", testScenario);
			addPage(scenarioWizard);
		} else if (testSuite != null) {
			suiteWizard = new TestSuiteWizard("Edit Test Suite Metadata", testSuite);
			addPage(suiteWizard);
		}
	}

}
