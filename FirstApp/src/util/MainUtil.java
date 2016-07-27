package util;

import java.util.ArrayList;
import java.util.List;

import entity.Column;
import entity.ColumnMeta;
import entity.Database;
import entity.Files;
import entity.LookUpCols;
import entity.QueryEntity;
import entity.TestScenario;
import entity.TestSuite;
import exceptions.ServiceException;
import service.ColumnService;
import service.TestScenarioService;
import serviceImpl.ColumnServiceImpl;
import serviceImpl.TestScenarioServiceImpl;

public class MainUtil {

	List<String> returnList;
	TestScenarioService testScenarioService = new TestScenarioServiceImpl();
	ColumnService columnService = new ColumnServiceImpl();

	public List<String> getDBProps(Database databse) {
		returnList = new ArrayList<>();
		returnList.add("Database Name:" + databse.getDatabaseName());
		returnList.add("Database Type:" + databse.getDbType());
		returnList.add("Server Name:" + databse.getServerName());
		returnList.add("User Name:" + databse.getUserName());
		returnList.add("Password:" + databse.getPassword());
		returnList.add("Port Number:" + databse.getPortNo());
		returnList.add("Last Updated:" + databse.getLastUpdtTS());
		return returnList;
	}

	public List<String> getFilesProp(Files file) {
		returnList = new ArrayList<>();

		returnList.add("File Name:" + file.getFileName());
		returnList.add("File Type:" + file.getFileTypes());
		returnList.add("Separator:" + file.getSeparator());
		returnList.add("Associated Col Meta:" + file.getFileColumnMeta().getColumnMetaName());
		returnList.add("Last Updated:" + file.getLastUpdtTS());

		return returnList;
	}

	public List<String> getLookUpProps(LookUpCols lookUpCol) {
		returnList = new ArrayList<>();

		returnList.add("LookUp Col Name:" + lookUpCol.getLookUpColName());
		returnList.add("Associated Query:" + lookUpCol.getQuery().getQueryName());
		returnList.add("LookUp Query:" + lookUpCol.getLookUpQuery());
		returnList.add("Last Updated:" + lookUpCol.getLastUpdtTS());

		return returnList;
	}

	public List<String> getProjectProps(TestScenario project) {
		returnList = new ArrayList<>();

		returnList.addAll(testScenarioService.getTestScenariosProps(project));

		return returnList;
	}

	public List<String> getQueryEntityProps(QueryEntity queryEntity) {
		returnList = new ArrayList<>();

		returnList.add("Query Name:" + queryEntity.getQueryName());
		returnList.add("Query Type:" + queryEntity.getQueryType());
		returnList.add("Main Query:" + queryEntity.getMainQuery());
		returnList.add("Parameters:" + queryEntity.getParamNames());
		returnList.add("Last Updated:" + queryEntity.getLastUpdtTS());

		return returnList;
	}

	public List<String> getColumnMetaProps(ColumnMeta columnMeta) {
		returnList = new ArrayList<>();
		List<Column> columns = null;
		List<String> uniqueCols;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			columns = columnService.getColummsByColumnMetaId(columnMeta.getIdColumnMeta());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		uniqueCols = columnService.getUniqueColumnsOfColumnMeta(columnMeta.getIdColumnMeta());

		returnList.add("Column Meta Name:" + columnMeta.getColumnMetaName());
		for (Column column : columns) {
			stringBuilder.append(column.getColumnName() + ",");
		}
		returnList.add("Column Names:" + stringBuilder.substring(0, stringBuilder.length() - 1));
		stringBuilder = new StringBuilder();
		for (String column : uniqueCols) {
			stringBuilder.append(column + ",");
		}
		returnList.add("Unique Columns:" + stringBuilder);
		returnList.add("Last Updated:" + columnMeta.getLastUpdtTS());

		return returnList;
	}

	public List<String> getLookUpColsProps(LookUpCols lookUpCol) {
		returnList = new ArrayList<>();

		returnList.add("LookUp Col Name:" + lookUpCol.getLookUpColName());
		returnList.add("LookUp Query:" + lookUpCol.getLookUpQuery());
		returnList.add("Associated Query:" + lookUpCol.getQuery().getQueryName());
		returnList.add("Last Updated:" + lookUpCol.getLastUpdtTS());

		return returnList;
	}

	public List<String> getSuiteProps(TestSuite testSuite) {
		returnList = new ArrayList<>();
		int count = 0;
		returnList.add("Test Suite Name:" + testSuite.getTestSuiteName());
		returnList.add("Description:" + testSuite.getSuiteDescription());
		returnList.add("Associated Test Scenarios:Scenarios");
		List<TestScenario> testScenarios = testScenarioService.getScenariosForSuiteId(testSuite.getTestSuiteId());
		for (TestScenario testScenario : testScenarios) {
			returnList.add((count++) + ":" + testScenario.getProjectName());
		}
		return returnList;
	}

}
