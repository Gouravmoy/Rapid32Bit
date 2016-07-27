package junit;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import dao.DatabaseDao;
import daoImpl.DatabaseDAOImpl;
import entity.Database;
import enums.DBTypes;
import exceptions.PersistException;
import exceptions.ReadEntityException;

public class DatabaseDAOTest extends TestCase {
	//static Logger logger = //logger.getLogger(DatabaseDAOTest.class);
	Database database;
	DatabaseDao databaseDao = new DatabaseDAOImpl();

	@Override
	public void setUp() throws Exception {
		super.setUp();
		database = new Database("First Connection","","DB44", "sName", "U", "P", "P", DBTypes.MYSQL);
	}

	@Test
	public void testAddDB() {
		try {
			//logger.info("Test Case Inserting Entity " + database);
			databaseDao.saveDatabse(database);
			assertTrue(true);
		} catch (PersistException e) {
			assertTrue(false);
			//logger.error("Failed to Enter Database into DB ", e);
		}
	}

	@Test
	public void testgetAllDatabaseInDB() {
		//logger.info("Test Get All DB ");
		List<Database> databases;
		try {
			databases = databaseDao.getAllDatabaseinDB();
			assertTrue(databases.size() > 0);
			//logger.info("Test Get All DB " + databases);
		} catch (ReadEntityException e) {
			//logger.error("Failed to Retrive Database from DB ", e);
			assertTrue(false);
		}

	}

	@Test
	public void testgetAllDBNames() {
		//logger.info("Test Get All DB Names ");
		List<String> dbnames;
		try {
			dbnames = databaseDao.getAllConnectionNames();
			assertTrue(dbnames.size() > 0);
			//logger.info("Test Get All DB " + dbnames);
		} catch (ReadEntityException e) {
			//logger.error("Failed to Retrive Database Names from DB ", e);
			assertTrue(false);
		}

	}

	@Test
	public void testgetDatabaseByid() {
		//logger.info("Test Get All DB by Id ");
		Database database;
		Long Id = new Long(101);
		try {
			database = databaseDao.getDatabaseByid(Id);
			assertNotNull(database);
			//logger.info("Test Get DB with Id = " + Id + " = " + database);
		} catch (ReadEntityException e) {
			//logger.error("Failed to Retrive Database by Id from DB ", e);
			assertTrue(false);
		}

	}
}
