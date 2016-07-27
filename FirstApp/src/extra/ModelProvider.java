package extra;

import java.util.ArrayList;
import java.util.List;

import entity.ExecutionDTO;
import entity.ExecutionStatus;
import entity.TestScenario;

public enum ModelProvider {
	INSTANCE;
	private List<String> keyValues;
	private TestScenario selectedProject;
	private ExecutionDTO executionDTO;
	private List<ExecutionStatus> executionStatus;
	
	private String sourcePath;
	private String targetPath;
	private String sourceSortedPath;
	private String targetSortedpath;
	private String matchedPath;
	private String errorPath;

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	public String getSourceSortedPath() {
		return sourceSortedPath;
	}

	public void setSourceSortedPath(String sourceSortedPath) {
		this.sourceSortedPath = sourceSortedPath;
	}

	public String getTargetSortedpath() {
		return targetSortedpath;
	}

	public void setTargetSortedpath(String targetSortedpath) {
		this.targetSortedpath = targetSortedpath;
	}

	public String getMatchedPath() {
		return matchedPath;
	}

	public void setMatchedPath(String matchedPath) {
		this.matchedPath = matchedPath;
	}

	public String getErrorPath() {
		return errorPath;
	}

	public void setErrorPath(String errorPath) {
		this.errorPath = errorPath;
	}

	public ExecutionDTO getExecutionDTO() {
		return executionDTO;
	}

	public void setExecutionDTO(ExecutionDTO executionDTO) {
		this.executionDTO = executionDTO;
	}

	private ModelProvider() {
		keyValues = new ArrayList<String>();
	}

	public List<String> getKeyValues() {
		return keyValues;
	}

	public void setKeyValues(List<String> keyValues) {
		this.keyValues = keyValues;
	}

	public TestScenario getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(TestScenario selectedProject) {
		this.selectedProject = selectedProject;
	}

	public List<ExecutionStatus> getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(List<ExecutionStatus> executionStatus) {
		this.executionStatus = executionStatus;
	}
}
