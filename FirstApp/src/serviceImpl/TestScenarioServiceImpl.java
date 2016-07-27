package serviceImpl;

import java.util.ArrayList;
import java.util.List;

import dao.DatabaseDao;
import dao.TestScenarioDao;
import daoImpl.DatabaseDAOImpl;
import daoImpl.TestScenarioDaoImpl;
import entity.Database;
import entity.TestScenario;
import exceptions.ReadEntityException;
import exceptions.ServiceException;
import service.FileService;
import service.TestScenarioService;

public class TestScenarioServiceImpl implements TestScenarioService {

	TestScenarioDao testScenarioDao;
	DatabaseDao databaseDao;
	FileService fileService = new FileServiceImpl();

	public TestScenarioServiceImpl() {
		super();
		testScenarioDao = new TestScenarioDaoImpl();
		databaseDao = new DatabaseDAOImpl();
	}

	@Override
	public List<String> getTestScenariosProps(TestScenario project) {
		List<String> props = new ArrayList<>();
		List<Database> databases;
		props.add("Test Scenario Name:" + project.getProjectName());
		props.add("Test Scenario Type:" + project.getProjectType());
		props.add("Column Meta Name:" + project.getColumnMeta().getColumnMetaName());
		try {
			databases = databaseDao.getAllDatabaseinDB();
			if (project.getSource() == 0) {
				props.add("Test Scenario Source File:"
						+ fileService.getFileForColMeta(project.getColumnMeta().getIdColumnMeta()));
			} else {
				props.add("Test Scenario Source DB:" + getDBPropforId(databases, project.getSource()));
			}
			if (project.getTarget() == 0) {
				props.add("Test Scenario Target File:"
						+ fileService.getFileForColMeta(project.getColumnMeta().getIdColumnMeta()));
			} else {
				props.add("Target Target DB:" + getDBPropforId(databases, project.getTarget()));
			}
			if (project.getSourceLookup() != 0)
				props.add("Test Scenario Source Lookup DB:" + getDBPropforId(databases, project.getSourceLookup()));
			if (project.getTargetLookup() != 0)
				props.add("Test Scenario Target Lookup DB:" + getDBPropforId(databases, project.getTargetLookup()));
		} catch (ReadEntityException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		props.add("Last Updated:" + project.getLastUpdtTS());
		return props;
	}

	private String getDBPropforId(List<Database> databases, Long long1) {
		for (Database database : databases) {
			if (database.getDbId() == long1) {
				return database.getDatabaseName();
			}
		}
		return null;

	}

	@Override
	public List<TestScenario> getScenariosForSuiteId(Long suiteId) {
		List<TestScenario> returnScenarios = new ArrayList<>();
		List<TestScenario> testScenarios;
		try {
			testScenarios = testScenarioDao.getAllTestScenarios();
			for (TestScenario scenario : testScenarios) {
				if (scenario.getTestSuite() != null) {
					if (scenario.getTestSuite().getTestSuiteId() == suiteId) {
						returnScenarios.add(scenario);
					}
				}
			}
		} catch (ReadEntityException e) {
			e.printStackTrace();
		}
		return returnScenarios;
	}

	@Override
	public TestScenario getScenarioByScenarioName(String name) {
		try {
			List<TestScenario> testScenarios = testScenarioDao.getAllTestScenarios();
			for (TestScenario testScenario : testScenarios) {
				if (name.equals(testScenario.getProjectName())) {
					return testScenario;
				}
			}
		} catch (ReadEntityException e) {
			e.printStackTrace();
		}
		return null;
	}

}
