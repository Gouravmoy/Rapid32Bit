package test;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import service.QueryService;
import serviceImpl.QueryServiceImpl;
import entity.QueryEntity;
import exceptions.ServiceException;

public class QueryServiceTest extends TestCase {
	QueryEntity queryEntity;
	QueryService queryService = new QueryServiceImpl();

	public void setUp() {
		queryEntity = new QueryEntity();

	}

	@Test(expected = ServiceException.class)
	public void testAddQueryService() throws ServiceException {
		String l = "-222";
		Long cId = Long.parseLong(l);
		queryService.addQuery(cId, queryEntity);

	}

	@Test
	public void getAllQueries() {
		try {
			List<QueryEntity> queryList;
			queryList = queryService.getAllQuries();
			assertTrue(queryList.size() > 0);
		} catch (ServiceException s) {
			fail();
		}
	}

}
