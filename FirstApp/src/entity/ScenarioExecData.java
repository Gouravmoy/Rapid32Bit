package entity;

import java.util.List;

import enums.ExportType;

public class ScenarioExecData {

	String scenarioName;
	String scenarioType;
	Database sourceDatabase;
	Database targetDatabase;
	Database sourceLookUpDatabase;
	Database targetLookUpDatabase;
	List<Column> sourceColumnList;
	List<Column> targetColumnList;
	List<LookUpCols> lookUpColsSource;
	List<LookUpCols> lookUpColsTarget;
	String sourceQuery;
	String targetQuery;
	String sourceFilePath;
	String targetFilePath;
	String separator;
	String separatorTarget;
	List<String> uniqueCols;
	List<ParamKeyMap> sourceParameterKeyMap;
	List<ParamKeyMap> targetParameterKeyMap;
	String outputFolderPath;
	ExportType exportType;
	boolean isDataByColName;

	public ScenarioExecData(String scenarioName, String scenarioType, Database sourceDatabase, Database targetDatabase,
			Database sourceLookUpDatabase, Database targetLookUpDatabase, List<Column> sourceColumnList,
			List<Column> targetColumnList, List<LookUpCols> lookUpColsSource, List<LookUpCols> lookUpColsTarget,
			String sourceQuery, String targetQuery, String sourceFilePath, String targetFilePath, String separator,
			String separatorTarget, List<String> uniqueCols, List<ParamKeyMap> sourceParameterKeyMap,
			List<ParamKeyMap> targetParameterKeyMap, String outputFolderPath, ExportType exportType,
			boolean isDataByColName) {
		super();
		this.scenarioName = scenarioName;
		this.scenarioType = scenarioType;
		this.sourceDatabase = sourceDatabase;
		this.targetDatabase = targetDatabase;
		this.sourceLookUpDatabase = sourceLookUpDatabase;
		this.targetLookUpDatabase = targetLookUpDatabase;
		this.sourceColumnList = sourceColumnList;
		this.targetColumnList = targetColumnList;
		this.lookUpColsSource = lookUpColsSource;
		this.lookUpColsTarget = lookUpColsTarget;
		this.sourceQuery = sourceQuery;
		this.targetQuery = targetQuery;
		this.sourceFilePath = sourceFilePath;
		this.targetFilePath = targetFilePath;
		this.separator = separator;
		this.separatorTarget = separatorTarget;
		this.uniqueCols = uniqueCols;
		this.sourceParameterKeyMap = sourceParameterKeyMap;
		this.targetParameterKeyMap = targetParameterKeyMap;
		this.outputFolderPath = outputFolderPath;
		this.exportType = exportType;
		this.isDataByColName = isDataByColName;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public String getScenarioType() {
		return scenarioType;
	}

	public void setScenarioType(String scenarioType) {
		this.scenarioType = scenarioType;
	}

	public Database getSourceDatabase() {
		return sourceDatabase;
	}

	public void setSourceDatabase(Database sourceDatabase) {
		this.sourceDatabase = sourceDatabase;
	}

	public Database getTargetDatabase() {
		return targetDatabase;
	}

	public void setTargetDatabase(Database targetDatabase) {
		this.targetDatabase = targetDatabase;
	}

	public Database getSourceLookUpDatabase() {
		return sourceLookUpDatabase;
	}

	public void setSourceLookUpDatabase(Database sourceLookUpDatabase) {
		this.sourceLookUpDatabase = sourceLookUpDatabase;
	}

	public Database getTargetLookUpDatabase() {
		return targetLookUpDatabase;
	}

	public void setTargetLookUpDatabase(Database targetLookUpDatabase) {
		this.targetLookUpDatabase = targetLookUpDatabase;
	}

	public List<Column> getSourceColumnList() {
		return sourceColumnList;
	}

	public void setSourceColumnList(List<Column> sourceColumnList) {
		this.sourceColumnList = sourceColumnList;
	}

	public List<Column> getTargetColumnList() {
		return targetColumnList;
	}

	public void setTargetColumnList(List<Column> targetColumnList) {
		this.targetColumnList = targetColumnList;
	}

	public List<LookUpCols> getLookUpColsSource() {
		return lookUpColsSource;
	}

	public void setLookUpColsSource(List<LookUpCols> lookUpColsSource) {
		this.lookUpColsSource = lookUpColsSource;
	}

	public List<LookUpCols> getLookUpColsTarget() {
		return lookUpColsTarget;
	}

	public void setLookUpColsTarget(List<LookUpCols> lookUpColsTarget) {
		this.lookUpColsTarget = lookUpColsTarget;
	}

	public String getSourceQuery() {
		return sourceQuery;
	}

	public void setSourceQuery(String sourceQuery) {
		this.sourceQuery = sourceQuery;
	}

	public String getTargetQuery() {
		return targetQuery;
	}

	public void setTargetQuery(String targetQuery) {
		this.targetQuery = targetQuery;
	}

	public String getSourceFilePath() {
		return sourceFilePath;
	}

	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}

	public String getTargetFilePath() {
		return targetFilePath;
	}

	public void setTargetFilePath(String targetFilePath) {
		this.targetFilePath = targetFilePath;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public List<String> getUniqueCols() {
		return uniqueCols;
	}

	public void setUniqueCols(List<String> uniqueCols) {
		this.uniqueCols = uniqueCols;
	}

	public List<ParamKeyMap> getSourceParameterKeyMap() {
		return sourceParameterKeyMap;
	}

	public void setSourceParameterKeyMap(List<ParamKeyMap> sourceParameterKeyMap) {
		this.sourceParameterKeyMap = sourceParameterKeyMap;
	}

	public List<ParamKeyMap> getTargetParameterKeyMap() {
		return targetParameterKeyMap;
	}

	public void setTargetParameterKeyMap(List<ParamKeyMap> targetParameterKeyMap) {
		this.targetParameterKeyMap = targetParameterKeyMap;
	}

	public String getOutputFolderPath() {
		return outputFolderPath;
	}

	public void setOutputFolderPath(String outputFolderPath) {
		this.outputFolderPath = outputFolderPath;
	}

	public ExportType getExportType() {
		return exportType;
	}

	public void setExportType(ExportType exportType) {
		this.exportType = exportType;
	}

	public String getSeparatorTarget() {
		return separatorTarget;
	}

	public void setSeparatorTarget(String separatorTarget) {
		this.separatorTarget = separatorTarget;
	}

	public boolean isDataByColName() {
		return isDataByColName;
	}

	public void setDataByColName(boolean isDataByColName) {
		this.isDataByColName = isDataByColName;
	}

	@Override
	public String toString() {
		return "ScenarioExecData [scenarioName=" + scenarioName + ", scenarioType=" + scenarioType + ", sourceDatabase="
				+ sourceDatabase + ", targetDatabase=" + targetDatabase + ", sourceLookUpDatabase="
				+ sourceLookUpDatabase + ", targetLookUpDatabase=" + targetLookUpDatabase + ", sourceColumnList="
				+ sourceColumnList + ", targetColumnList=" + targetColumnList + ", lookUpColsSource=" + lookUpColsSource
				+ ", lookUpColsTarget=" + lookUpColsTarget + ", sourceQuery=" + sourceQuery + ", targetQuery="
				+ targetQuery + ", sourceFilePath=" + sourceFilePath + ", targetFilePath=" + targetFilePath
				+ ", separator=" + separator + ", separatorTarget=" + separatorTarget + ", uniqueCols=" + uniqueCols
				+ ", sourceParameterKeyMap=" + sourceParameterKeyMap + ", targetParameterKeyMap="
				+ targetParameterKeyMap + ", outputFolderPath=" + outputFolderPath + ", exportType=" + exportType
				+ ", isDataByColName=" + isDataByColName + "]";
	}

}
