package junit;

import org.junit.Test;

import dao.TestSuiteDao;
import daoImpl.AllTestSuiteDaoImpl;
import entity.TestSuite;
import exceptions.PersistException;
import exceptions.ReadEntityException;
import junit.framework.TestCase;

public class TestSuitetest extends TestCase {

	//static Logger logger = //logger.getLogger(DatabaseDAOTest.class);
	TestSuite testSuite;
	TestSuiteDao testSuiteDao;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		testSuiteDao = new AllTestSuiteDaoImpl();
		testSuite = new TestSuite("FirstSuite2", "Sherlock");
	}

	@Test
	public void testSaveSuite() {
		try {
			//logger.info("Test Case Inserting Entity " + testSuite);
			testSuiteDao.saveTestSuite(testSuite);
			assertTrue(true);
		} catch (PersistException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}

	@Test
	public void testgetTestSuiteid() {
		//logger.info("Test Case Fetch Entity " + testSuite);
		try {
			testSuiteDao.getTestSuiteid(new Long(1));
		} catch (ReadEntityException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}

	@Test
	public void testgetAllTestSuiteInDB() {
		//logger.info("Test Case Fetch All Entity " + testSuite);
		try {
			assertTrue(testSuiteDao.getAllTestSuiteInDB().size() > 0);
		} catch (ReadEntityException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}

	@Test
	public void testgetAllAllTestSuiteNames() {
		//logger.info("Test Case Fetch All Entity by Name " + testSuite);
		try {
			assertTrue(testSuiteDao.getAllAllTestSuiteNames().size() > 0);
		} catch (ReadEntityException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}

}
