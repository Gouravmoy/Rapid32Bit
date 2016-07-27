package views.wizardPages.executeModule;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.jface.wizard.Wizard;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.rolling.RollingFileAppender;
import entity.Column;
import entity.ColumnMeta;
import entity.Database;
import entity.ExecutionDTO;
import entity.Files;
import entity.LookUpCols;
import entity.ParamKeyMap;
import entity.QueryEntity;
import entity.ScenarioExecData;
import entity.TestScenario;
import enums.ExportType;
import enums.ProjectType;
import enums.QueryType;
import enums.VolumeOfData;
import exceptions.ServiceException;
import extra.ModelProvider;
import service.ColumnService;
import service.DataBaseService;
import service.FileService;
import service.LookUpQueryService;
import service.QueryService;
import service.TestScenarioService;
import serviceImpl.ColumnServiceImpl;
import serviceImpl.DataBaseServiceImpl;
import serviceImpl.FileServiceImpl;
import serviceImpl.LookUpQueryServiceImpl;
import serviceImpl.QueryServiceImpl;
import serviceImpl.TestScenarioServiceImpl;
import views.WorkBenchTreesView;

public class MyWizard extends Wizard {

	protected MyPageOne mainPage;
	protected MyPageTwo scenarioPage;
	protected MyPageThree exportPage;

	static LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
	@SuppressWarnings("rawtypes")
	static RollingFileAppender rfAppender = new RollingFileAppender();
	static Logger logbackLogger;

	@SuppressWarnings("unchecked")
	public MyWizard() {
		super();
		rfAppender.setContext(loggerContext);
		rfAppender.setFile(System.getProperty("log_file_loc") + "/log/" + "rapid" + ".log");
		logbackLogger = loggerContext.getLogger("MainController");
		logbackLogger.addAppender(rfAppender);
		setNeedsProgressMonitor(true);
	}

	@Override
	public String getWindowTitle() {
		return "Data Runner";
	}

	@Override
	public void addPages() {
		mainPage = new MyPageOne();
		scenarioPage = new MyPageTwo();
		exportPage = new MyPageThree();

		addPage(mainPage);
		addPage(scenarioPage);
		addPage(exportPage);

	}

	@Override
	public boolean performFinish() {
		System.out.println("Finished Reached");
		ModelProvider.INSTANCE.setExecutionDTO(setExecutionDTO());
		createExecutePart();
		return true;
	}

	private void createExecutePart() {
		MPart partDy = MBasicFactory.INSTANCE.createPart();
		partDy.setContributionURI("bundleclass://FirstApp/views.ExecutePart");
		MPart oldPart = WorkBenchTreesView.partService.findPart("firstapp.part.playground");
		MPartStack parent = (MPartStack) WorkBenchTreesView.modelService.getContainer(oldPart);
		partDy.setElementId("firstapp.part.playground");
		partDy.setContainerData("60");
		partDy.setIconURI("platform:/plugin/FirstApp/src/icons/lightning.png");
		partDy.setLabel("DATA RUNNER");
		partDy.setCloseable(true);
		parent.getChildren().add(partDy);
		parent.setSelectedElement(partDy);

	}

	private ExecutionDTO setExecutionDTO() {

		TestScenarioService testScenarioService;
		DataBaseService dataBaseService;
		QueryService queryService;
		LookUpQueryService lookUpQueryService;
		FileService fileService;
		ColumnService columnService;

		TestScenario testScenario = null;
		ColumnMeta columnMeta = null;
		ColumnMeta columnMetaTarget = null;
		List<QueryEntity> queryEntities = new ArrayList<>();
		Files file = null;
		Files fileTarget = null;

		ExecutionDTO executionDto = null;
		List<ScenarioExecData> scenarioExecDatas = new ArrayList<>();
		ScenarioExecData scenarioExecData = null;

		String execInstName = null;
		String execInstDesc = null;
		VolumeOfData volumeOfDataIND = null;

		String scenarioName = null;
		String scenarioType = null;
		Database sourceDatabase = null;
		Database targetDatabase = null;
		Database sourceLookUpDatabase = null;
		Database targetLookUpDatabase = null;
		List<LookUpCols> lookUpColsSource = null;
		List<LookUpCols> lookUpColsTarget = null;
		List<Column> columnList = null;
		List<Column> columnListTarget = null;
		String sourceQuery = null;
		String targetQuery = null;
		String filepath = null;
		String filePathTarget = null;
		String separator = null;
		String separatorTarget = null;
		List<String> uniqueCols = null;
		List<ParamKeyMap> sourceParameterKeyMap = null;
		List<ParamKeyMap> targetParameterKeyMap = null;
		String outputFolderPath = null;
		ExportType exportType = null;
		boolean isDataByColName = false;

		testScenarioService = new TestScenarioServiceImpl();
		dataBaseService = new DataBaseServiceImpl();
		queryService = new QueryServiceImpl();
		lookUpQueryService = new LookUpQueryServiceImpl();
		fileService = new FileServiceImpl();
		columnService = new ColumnServiceImpl();
		try {
			testScenario = testScenarioService
					.getScenarioByScenarioName(scenarioPage.getTestScenarioCombobox().getText());
			columnMeta = testScenario.getColumnMeta();
			file = fileService.getFileForColMeta(columnMeta.getIdColumnMeta());
			columnList = columnService.getColummsByColumnMetaId(columnMeta.getIdColumnMeta());
			uniqueCols = columnService.getUniqueColumnsOfColumnMeta(columnMeta.getIdColumnMeta());
			if (testScenario.getProjectType() == ProjectType.File_To_File) {
				columnMetaTarget = testScenario.getColumnMetaTarget();
				fileTarget = fileService.getFileForColMeta(columnMetaTarget.getIdColumnMeta());
				columnListTarget = columnService.getColummsByColumnMetaId(columnMetaTarget.getIdColumnMeta());
			} else {
				queryEntities = queryService.getQueryByColumnId(columnMeta.getIdColumnMeta());
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		execInstName = mainPage.getExecInstNameText().getText();
		execInstDesc = mainPage.getExecInstDescText().getText();
		volumeOfDataIND = VolumeOfData.valueOf(mainPage.getVolumData().getText());
		scenarioName = testScenario.getProjectName();
		scenarioType = testScenario.getProjectType().toString();
		if (testScenario.getSource() != null && testScenario.getSource() != 0)
			sourceDatabase = dataBaseService.getDatabaseById(testScenario.getSource());
		if (testScenario.getTarget() != null)
			targetDatabase = dataBaseService.getDatabaseById(testScenario.getTarget());
		if (testScenario.getSourceLookup() != null)
			sourceLookUpDatabase = dataBaseService.getDatabaseById(testScenario.getSourceLookup());
		if (testScenario.getTargetLookup() != null)
			targetLookUpDatabase = dataBaseService.getDatabaseById(testScenario.getTargetLookup());

		for (QueryEntity queryEntity : queryEntities) {
			if (queryEntity.getQueryType() == QueryType.SOURCE) {
				try {
					isDataByColName = queryEntity.isDataByColNameFlag();
					sourceQuery = queryEntity.getMainQuery();
					lookUpColsSource = lookUpQueryService.getLookUpColsByQueryId(queryEntity.getQueryId());
				} catch (ServiceException err) {
					logbackLogger.error(err.getMessage(), err);
				}
			} else if (queryEntity.getQueryType() == QueryType.TARGET) {
				try {
					isDataByColName = queryEntity.isDataByColNameFlag();
					targetQuery = queryEntity.getMainQuery();
					lookUpColsTarget = lookUpQueryService.getLookUpColsByQueryId(queryEntity.getQueryId());
				} catch (ServiceException err) {
					logbackLogger.error(err.getMessage(), err);
				}
			}
		}
		if (file != null) {
			separator = file.getSeparator();
			filepath = scenarioPage.getFileInputText().getText();
			if (fileTarget != null) {
				separatorTarget = fileTarget.getSeparator();
			}
			filePathTarget = scenarioPage.getTargetFileText().getText();
		}

		sourceParameterKeyMap = MyPageTwo.getSourceParamKeyMaps();
		targetParameterKeyMap = MyPageTwo.getTargetParamKeyMaps();
		outputFolderPath = exportPage.getExportFileLocationPathText().getText();
		exportType = ExportType.valueOf(exportPage.getExportTypeComboBox().getText());

		scenarioExecData = new ScenarioExecData(scenarioName, scenarioType, sourceDatabase, targetDatabase,
				sourceLookUpDatabase, targetLookUpDatabase, columnList, columnListTarget, lookUpColsSource,
				lookUpColsTarget, sourceQuery, targetQuery, filepath, filePathTarget, separator, separatorTarget,
				uniqueCols, sourceParameterKeyMap, targetParameterKeyMap, outputFolderPath, exportType,
				isDataByColName);
		scenarioExecDatas.add(scenarioExecData);
		executionDto = new ExecutionDTO(execInstName, execInstDesc, scenarioExecDatas, volumeOfDataIND, new Date(0));
		logbackLogger.info(executionDto.toString());
		return executionDto;

	}

}
