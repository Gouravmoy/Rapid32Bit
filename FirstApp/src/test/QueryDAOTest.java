package test;

import java.util.List;

import org.junit.Test;

import dao.ColumnMetaDao;
import dao.QueryDao;
import daoImpl.ColumnMetaDAOImpl;
import daoImpl.QueryDaoImpl;
import entity.ColumnMeta;
import entity.QueryEntity;
import exceptions.DAOException;
import exceptions.EntityNotPresent;
import exceptions.ReadEntityException;
import junit.framework.TestCase;

public class QueryDAOTest extends TestCase {
	QueryEntity queryEntity;
	ColumnMeta columnMeta;
	QueryDao queryDao = new QueryDaoImpl();
	ColumnMetaDao columnMetaDao = new ColumnMetaDAOImpl();

	public void setUp() {
		try {
			super.setUp();
			columnMeta = new ColumnMeta("Name", "ABC","A");
			queryEntity = new QueryEntity();
			columnMeta = new ColumnMeta("", null, null, null);
			String l = "-21474836480";
			Long l1 = Long.parseLong(l);
			columnMeta.setIdColumnMeta(l1);

			queryEntity = new QueryEntity(columnMeta, null, "", "", "");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testAddQuery() {
		try {
			columnMetaDao.saveColumnMeta(columnMeta);
			queryEntity.setQueryColumnMeta(columnMeta);
			queryDao.addQuery(queryEntity);
			assertTrue(true);
		} catch (DAOException e) {
			assertTrue(false);
		}
	}

	@Test
	public void testgetAllDatabaseInDB() {
		List<QueryEntity> queryEntities;
		try {
			queryEntities = queryDao.getAllQueries();
			assertTrue(queryEntities.size() > 0);
		} catch (ReadEntityException e) {
			assertTrue(false);
		}

	}

	@Test
	public void testgetDatabaseByid() {
		QueryEntity queryEntity;
		Long Id = new Long(2);
		try {
			queryEntity = queryDao.getQueryByID(Id);
			assertNotNull(queryEntity);
		} catch (EntityNotPresent e) {
			assertTrue(false);
		}

	}

	@Test
	public void testGetByColumnId() {
		List<QueryEntity> queryEntities;
		try {
			String l = "-2147483";
			Long cId = Long.parseLong(l);
			queryEntities = queryDao.getQueryByCMId(cId);
			assertTrue(queryEntities.size() > 0);
		} catch (EntityNotPresent e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	
}
