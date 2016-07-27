package entity;

import java.util.Date;

public class TestSuite {

	Long testSuiteId;
	String testSuiteName;
	String suiteDescription;
	Date lastUpdtTS;

	public TestSuite() {
		super();
	}

	@Override
	public String toString() {
		return testSuiteName;
	}

	public Long getTestSuiteId() {
		return testSuiteId;
	}

	public void setTestSuiteId(Long testSuiteId) {
		this.testSuiteId = testSuiteId;
	}

	public String getTestSuiteName() {
		return testSuiteName;
	}

	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}

	public String getSuiteDescription() {
		return suiteDescription;
	}

	public void setSuiteDescription(String suiteDescription) {
		this.suiteDescription = suiteDescription;
	}

	public Date getLastUpdtTS() {
		return lastUpdtTS;
	}

	public void setLastUpdtTS(Date lastUpdtTS) {
		this.lastUpdtTS = lastUpdtTS;
	}

	public TestSuite(String testSuiteName, String suiteDescription) {
		super();
		this.testSuiteName = testSuiteName;
		this.suiteDescription = suiteDescription;
		this.lastUpdtTS = new Date();
	}

}
