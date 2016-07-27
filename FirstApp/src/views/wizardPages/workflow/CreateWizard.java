package views.wizardPages.workflow;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.rolling.RollingFileAppender;
import dao.ColumnMetaDao;
import dao.DatabaseDao;
import dao.FileDAO;
import dao.LookUpColumnDao;
import dao.QueryDao;
import dao.TestScenarioDao;
import dao.TestSuiteDao;
import daoImpl.ColumnMetaDAOImpl;
import daoImpl.DatabaseDAOImpl;
import entity.ColumnMeta;
import entity.Database;
import entity.Files;
import entity.LookUpCols;
import entity.QueryEntity;
import entity.TestScenario;
import entity.TestSuite;
import exceptions.DAOException;
import exceptions.ServiceException;
import service.LookUpQueryService;
import views.WorkBenchTreesView;

public class CreateWizard extends Wizard {

	DatabaseDao databaseDao;
	ColumnMetaDao columnMetaDao;
	FileDAO fileDAO;
	QueryDao queryDao;
	TestScenarioDao testScenarioDao;
	TestSuiteDao suiteDao;
	LookUpColumnDao lookUpColumnDao;
	LookUpQueryService lookUpQueryService;

	Database database;
	ColumnMeta columnMeta;
	Files files;
	QueryEntity queryEntity;
	TestScenario testScenario;
	TestSuite testSuite;
	List<LookUpCols> lookUpCols;

	DatabaseWizardPage databaseWizard;
	ColumnMetaWizardPage columnMetaWizardPage;
	FilesWizard filesWizard;
	QueriesWizardPage queriesWizardPage;
	TestScenarioWizard scenarioWizard;
	TestSuiteWizard suiteWizard;
	LookUpQueriesWizardPage lookUpQueriesWizardPage;

	static LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
	@SuppressWarnings("rawtypes")
	static RollingFileAppender rfAppender = new RollingFileAppender();
	static Logger logbackLogger;

	Class<?> clazz;

	@SuppressWarnings("unchecked")
	public CreateWizard(Class<?> clazz) {
		super();
		rfAppender.setContext(loggerContext);
		rfAppender.setFile(System.getProperty("log_file_loc") + "/log/" + "rapid" + ".log");
		logbackLogger = loggerContext.getLogger("CreateWizard");
		logbackLogger.addAppender(rfAppender);
		this.clazz = clazz;
		if (clazz.getSimpleName().equals(Database.class.getSimpleName())) {
			database = new Database();
		} else if (clazz.getSimpleName().equals(ColumnMeta.class.getSimpleName())) {
			columnMeta = new ColumnMeta();
		} else if (clazz.getSimpleName().equals(Files.class.getSimpleName())) {

		} else if (clazz.getSimpleName().equals(QueryEntity.class.getSimpleName())) {

		} else if (clazz.getSimpleName().equals(TestScenario.class.getSimpleName())) {

		} else if (clazz.getSimpleName().equals(TestSuite.class.getSimpleName())) {

		}
	}

	@Override
	public boolean performFinish() {
		try {
			if (database != null) {
				databaseDao = new DatabaseDAOImpl();
				databaseDao.saveDatabse(database);
			} else if (columnMeta != null) {
				columnMetaDao = new ColumnMetaDAOImpl();
				columnMetaDao.saveColumnMeta(columnMeta);
			}
			WorkBenchTreesView.queryAndRefresh();
		} catch (DAOException | ServiceException err) {
			logbackLogger.error(err.getMessage(), err);
		}
		return true;
	}

	@Override
	public void addPages() {
		if (database != null) {
			databaseWizard = new DatabaseWizardPage("Create Database Connection", database);
			addPage(databaseWizard);
		} else if (columnMeta != null) {
			columnMetaWizardPage = new ColumnMetaWizardPage("Create Column Meta Data", columnMeta, new ArrayList<>());
			addPage(columnMetaWizardPage);
		}

	}

}
