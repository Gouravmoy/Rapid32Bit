package entity;

import java.util.Date;

import enums.ProjectType;

public class TestScenario {

	Long projectId;
	String projectName;
	String testScenarioDescription;
	ColumnMeta columnMeta;
	ColumnMeta columnMetaTarget;
	Long source;
	Long target;
	Long sourceLookup;
	Long targetLookup;
	Date lastUpdtTS;
	ProjectType projectType;
	TestSuite testSuite;

	public ColumnMeta getColumnMetaTarget() {
		return columnMetaTarget;
	}

	public void setColumnMetaTarget(ColumnMeta columnMetaTarget) {
		this.columnMetaTarget = columnMetaTarget;
	}

	public String getTestScenarioDescription() {
		return testScenarioDescription;
	}

	public void setTestScenarioDescription(String testScenarioDescription) {
		this.testScenarioDescription = testScenarioDescription;
	}

	public Date getLastUpdtTS() {
		return lastUpdtTS;
	}

	public void setLastUpdtTS(Date lastUpdtTS) {
		this.lastUpdtTS = lastUpdtTS;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public ColumnMeta getColumnMeta() {
		return columnMeta;
	}

	public void setColumnMeta(ColumnMeta columnMeta) {
		this.columnMeta = columnMeta;
	}

	public Long getSource() {
		return source;
	}

	public void setSource(Long source) {
		this.source = source;
	}

	public Long getTarget() {
		return target;
	}

	public void setTarget(Long target) {
		this.target = target;
	}

	public Long getSourceLookup() {
		return sourceLookup;
	}

	public void setSourceLookup(Long sourceLookup) {
		this.sourceLookup = sourceLookup;
	}

	public Long getTargetLookup() {
		return targetLookup;
	}

	public void setTargetLookup(Long targetLookup) {
		this.targetLookup = targetLookup;
	}

	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	public TestScenario() {
		super();
	}

	public TestSuite getTestSuite() {
		return testSuite;
	}

	public void setTestSuite(TestSuite testSuite) {
		this.testSuite = testSuite;
	}

	public TestScenario(String projectName, ColumnMeta columnMeta, ColumnMeta columnMetaTarget, Long source,
			Long target, Long sourceLookup, Long targetLookup, ProjectType projectType, String testScenarioDesc) {
		super();
		this.projectName = projectName;
		this.columnMeta = columnMeta;
		this.columnMetaTarget = columnMetaTarget;
		this.source = source;
		this.target = target;
		this.sourceLookup = sourceLookup;
		this.targetLookup = targetLookup;
		this.projectType = projectType;
		this.lastUpdtTS = new Date();
		this.testScenarioDescription = testScenarioDesc;
	}

	@Override
	public String toString() {
		return projectName;
	}

}
