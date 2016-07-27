package dao;

import java.util.List;

import entity.TestSuite;
import exceptions.EntityNotPresent;
import exceptions.PersistException;
import exceptions.ReadEntityException;

public interface TestSuiteDao {
	public void saveTestSuite(TestSuite databse) throws PersistException;

	public TestSuite getTestSuiteid(Long id) throws ReadEntityException;

	public List<TestSuite> getAllTestSuiteInDB() throws ReadEntityException;

	public List<String> getAllAllTestSuiteNames() throws ReadEntityException;
	
	public void update(TestSuite suite) throws EntityNotPresent;
}
