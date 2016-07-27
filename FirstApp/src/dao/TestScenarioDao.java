package dao;

import java.util.List;

import entity.TestScenario;
import exceptions.DAOException;
import exceptions.PersistException;
import exceptions.ReadEntityException;

public interface TestScenarioDao {

	TestScenario addTestScenarios(TestScenario project) throws PersistException;

	TestScenario getTestScenarioById(Long ID) throws ReadEntityException;

	List<TestScenario> getAllTestScenarios() throws ReadEntityException;

	List<String> getAllTestScenarioName() throws ReadEntityException;
	
	void updateTestScenario(TestScenario updatedTestScenario)throws DAOException;

}
