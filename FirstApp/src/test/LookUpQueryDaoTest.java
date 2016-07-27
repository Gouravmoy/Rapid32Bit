package test;

import java.util.List;

import org.junit.Test;

import dao.ColumnMetaDao;
import dao.LookUpColumnDao;
import dao.QueryDao;
import daoImpl.ColumnMetaDAOImpl;
import daoImpl.LookUpQueryDaoImpl;
import daoImpl.QueryDaoImpl;
import entity.ColumnMeta;
import entity.LookUpCols;
import entity.QueryEntity;
import exceptions.EntityNotPresent;
import exceptions.PersistException;
import exceptions.ReadEntityException;
import junit.framework.TestCase;

public class LookUpQueryDaoTest extends TestCase {
	ColumnMeta columnMeta;
	QueryEntity queryEntity;
	LookUpCols lookUpCols;
	LookUpColumnDao lookUpColumnDao = new LookUpQueryDaoImpl();
	QueryDao queryDao = new QueryDaoImpl();
	ColumnMetaDao columnDao = new ColumnMetaDAOImpl();

	public void setUp() {
		try {
			super.setUp();
			columnMeta = new ColumnMeta("", null, null, null);
			columnDao.saveColumnMeta(columnMeta);
			queryEntity = new QueryEntity(columnMeta, null, "", "",  "");
			queryDao.addQuery(queryEntity);
			lookUpCols = new LookUpCols(queryEntity, "", "",null);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testAddQuery() {
		try {
			lookUpColumnDao.addLookUpQuery(lookUpCols);
			assertTrue(true);
		} catch (PersistException e) {
			assertTrue(false);
			e.printStackTrace();
		}

	}

	@Test
	public void testgetAllQueryInDB() {
		List<LookUpCols> lookUpCols;
		try {
			lookUpCols = lookUpColumnDao.getAllLookUpQueries();
			assertTrue(lookUpCols.size() > 0);
		} catch (ReadEntityException e) {
			e.printStackTrace();
			assertTrue(false);
		}

	}

	@Test
	public void testgetQueryByID() {
		LookUpCols lookUpCols;
		Long Id = new Long(601);
		try {
			lookUpCols = lookUpColumnDao.getLookUpQuriebyID(Id);
			assertNotNull(lookUpCols);
		} catch (EntityNotPresent e) {
			e.printStackTrace();
			assertTrue(false);
		}

	}

}
