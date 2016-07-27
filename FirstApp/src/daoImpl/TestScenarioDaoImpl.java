package daoImpl;

import java.util.ArrayList;
import java.util.List;

import dao.TestScenarioDao;
import entity.TestScenario;
import exceptions.DAOException;
import exceptions.PersistException;
import exceptions.ReadEntityException;

public class TestScenarioDaoImpl extends GenericDAOImpl<TestScenario, Long> implements TestScenarioDao {

	@Override
	public TestScenario addTestScenarios(TestScenario project) throws PersistException {
		try {
			return save(project);
		} catch (Exception e) {
			e.printStackTrace();
			throw new PersistException();
		}

	}

	@Override
	public TestScenario getTestScenarioById(Long ID) throws ReadEntityException {
		try {
			return readById(TestScenario.class, ID);
		} catch (Exception e) {
			throw new ReadEntityException();
		}
	}

	@Override
	public List<TestScenario> getAllTestScenarios() throws ReadEntityException {
		try {
			return readAll("Project.finadAll", TestScenario.class);
		} catch (Exception e) {
			throw new ReadEntityException();
		}
	}

	@Override
	public List<String> getAllTestScenarioName() throws ReadEntityException {
		try {
			List<String> listOfProjects = new ArrayList<String>();
			for (TestScenario p : readAll("Project.finadAll", TestScenario.class)) {
				listOfProjects.add(p.getProjectName());
			}
			return listOfProjects;
		} catch (Exception e) {
			throw new ReadEntityException();
		}
	}

	@Override
	public void updateTestScenario(TestScenario updatedTestScenario) throws DAOException {
		try{
		update(TestScenario.class, updatedTestScenario.getProjectId(), updatedTestScenario);
		}catch(Exception err){
			err.printStackTrace();
			throw new DAOException();
		}
	}

}
