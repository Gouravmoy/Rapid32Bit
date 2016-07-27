package service;

import java.util.List;

import entity.TestScenario;

public interface TestScenarioService {
	public List<String> getTestScenariosProps(TestScenario project);
	public List<TestScenario> getScenariosForSuiteId(Long suiteId);
	public TestScenario getScenarioByScenarioName(String name);
}
