package test;

import junit.framework.TestCase;
import dao.TestScenarioDao;
import daoImpl.TestScenarioDaoImpl;
import entity.ColumnMeta;
import entity.TestScenario;
import exceptions.PersistException;
import exceptions.ReadEntityException;

public class TestScenarioDaoTest extends TestCase {

	TestScenarioDao projectDao = new TestScenarioDaoImpl();
	ColumnMeta columnMeta;
	TestScenario project;
	Long id;

	@Override
	protected void setUp() throws Exception {
		String Id = "301";
		id = Long.parseLong(Id);
		super.setUp();
		columnMeta = new ColumnMeta();
		project = new TestScenario();
	}

	public void testAddProject() {
		try {
			projectDao.addTestScenarios(project);
			assertTrue(true);
		} catch (PersistException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	public void testGetProjectById() {
		try {
			TestScenario p = projectDao.getTestScenarioById(id);
			assertTrue(!p.equals(null));
		} catch (ReadEntityException e) {
			fail();
			e.printStackTrace();
		}
	}

	public void testGetAllProject() {
		try {
			assertTrue(projectDao.getAllTestScenarios().size() > 0);
		} catch (ReadEntityException e) {
			fail();
			e.printStackTrace();
		}
	}

}
