package daoImpl;

import java.util.ArrayList;
import java.util.List;

import dao.TestSuiteDao;
import entity.TestSuite;
import exceptions.EntityAlreadyExists;
import exceptions.EntityNotPresent;
import exceptions.PersistException;
import exceptions.ReadEntityException;

public class AllTestSuiteDaoImpl extends GenericDAOImpl<TestSuite, Long> implements TestSuiteDao {
	// Logger logger = Logger.getLogger(getClass());

	@Override
	public void saveTestSuite(TestSuite testSuite) throws PersistException {
		try {
			List<String> testSuitesName = getAllAllTestSuiteNames();
			if (testSuitesName.contains(testSuite.getTestSuiteName())) {
				throw new EntityAlreadyExists(
						"Test Suite - " + testSuite.getTestSuiteName() + " already exists in the database");
			}
			save(testSuite);
		} catch (Exception err) {
			err.printStackTrace();
			// logger.error(err);
			throw new PersistException("Could not persist Database Data - " + err.getMessage() + testSuite);
		}
	}

	@Override
	public TestSuite getTestSuiteid(Long id) throws ReadEntityException {
		try {
			return readById(TestSuite.class, id);
		} catch (Exception err) {
			// logger.error(err);
			throw new ReadEntityException("Could not get TestSuite Data for ID - " + id);
		}
	}

	@Override
	public List<TestSuite> getAllTestSuiteInDB() throws ReadEntityException {
		List<TestSuite> testSuites;
		try {
			testSuites = readAll("TestSuite.finadAll", TestSuite.class);
		} catch (Exception err) {
			// logger.error(err);
			err.printStackTrace();
			throw new ReadEntityException("Could not get All TestSuite Information");
		}
		return testSuites;
	}

	@Override
	public List<String> getAllAllTestSuiteNames() throws ReadEntityException {
		List<TestSuite> testSuites;
		List<String> dbNames;
		dbNames = new ArrayList<>();
		try {
			testSuites = getAllTestSuiteInDB();
			for (TestSuite testSuite : testSuites) {
				dbNames.add(testSuite.getTestSuiteName());
			}
		} catch (Exception err) {
			// logger.error(err);
			err.printStackTrace();
			throw new ReadEntityException("Could not get All TestSuite Name Information");
		}
		return dbNames;
	}

	@Override
	public void update(TestSuite suite) throws EntityNotPresent {
		try {
			update(TestSuite.class, suite.getTestSuiteId(), suite);
		} catch (EntityNotPresent err) {
			throw new EntityNotPresent("Could not get Update TestSuite Information");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
