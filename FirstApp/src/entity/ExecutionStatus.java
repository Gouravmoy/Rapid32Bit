package entity;

import enums.ExecutionPhase;

public class ExecutionStatus {

	String scenarioName;
	String executionTime;
	ExecutionPhase status;
	String recordsProcessed;

	public String getRecordsProcessed() {
		return recordsProcessed;
	}

	public void setRecordsProcessed(String recordsProcessed) {
		this.recordsProcessed = recordsProcessed;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public String getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(String executionTime) {
		this.executionTime = executionTime;
	}

	public ExecutionPhase getStatus() {
		return status;
	}

	public void setStatus(ExecutionPhase status) {
		this.status = status;
	}

}
