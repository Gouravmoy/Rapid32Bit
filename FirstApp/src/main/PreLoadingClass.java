package main;

import dao.ColumnMetaDao;
import dao.DatabaseDao;
import dao.FileDAO;
import dao.LookUpColumnDao;
import dao.TestScenarioDao;
import dao.TestSuiteDao;
import dao.QueryDao;
import daoImpl.AllTestSuiteDaoImpl;
import daoImpl.ColumnMetaDAOImpl;
import daoImpl.DatabaseDAOImpl;
import daoImpl.FileDAOImpl;
import daoImpl.LookUpQueryDaoImpl;
import daoImpl.TestScenarioDaoImpl;
import daoImpl.QueryDaoImpl;
import entity.ColumnMeta;
import entity.Database;
import entity.Files;
import entity.LookUpCols;
import entity.TestScenario;
import entity.TestSuite;
import enums.DBTypes;
import enums.FileTypes;
import enums.LookUpType;
import enums.ProjectType;
import enums.QueryType;
import entity.QueryEntity;
import exceptions.DAOException;
import exceptions.PersistException;

public class PreLoadingClass {

	public static DatabaseDao databaseDao;
	public static ColumnMetaDao columnMetaDao;
	public static FileDAO fileDAO;
	public static LookUpColumnDao lookUpColumnDao;
	public static TestScenarioDao testScenarioDao;
	public static QueryDao queryDao;
	public static TestSuiteDao testSuiteDao;

	static Database databse1;
	static Database databse2;
	static Database databse3;

	static ColumnMeta columnMeta1;
	static ColumnMeta columnMeta2;
	static ColumnMeta columnMeta3;
	static ColumnMeta columnMeta4;

	static QueryEntity queryEntity1;
	static QueryEntity queryEntity2;
	static QueryEntity queryEntity3;
	static QueryEntity queryEntity4;
	static QueryEntity queryEntity5;
	static QueryEntity queryEntity6;
	static QueryEntity queryEntity7;

	static LookUpCols lookUpCol1;
	static LookUpCols lookUpCol2;
	static LookUpCols lookUpCol3;
	static LookUpCols lookUpCol4;
	static LookUpCols lookUpCol5;
	static LookUpCols lookUpCol6;
	static LookUpCols lookUpCol7;
	static LookUpCols lookUpCol8;

	static TestScenario testScenario1;
	static TestScenario testScenario2;

	static TestSuite testSuite;

	public static void firstLoad() {

		databaseDao = new DatabaseDAOImpl();
		columnMetaDao = new ColumnMetaDAOImpl();
		fileDAO = new FileDAOImpl();
		lookUpColumnDao = new LookUpQueryDaoImpl();
		testScenarioDao = new TestScenarioDaoImpl();
		queryDao = new QueryDaoImpl();
		testSuiteDao = new AllTestSuiteDaoImpl();

		addDatabase();
		addColumnMeta();
		addFiles();
		addQueryEntity();
		addLookUpCols();
		addTestScenarios();
		addTestSuite();

	}

	private static void addTestSuite() {
		testSuite = new TestSuite("Sherlock", "Awesome Detective");
		try {
			testSuiteDao.saveTestSuite(testSuite);
			testScenario1.setTestSuite(testSuite);
			testScenario2.setTestSuite(testSuite);
			// testScenarioDao.addTestScenarios(testScenario1);
			// testScenarioDao.addTestScenarios(testScenario2);
			testScenarioDao.updateTestScenario(testScenario1);
			testScenarioDao.updateTestScenario(testScenario2);
		} catch (PersistException e) {
			e.printStackTrace();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	private static void addTestScenarios() {

		testScenario1 = new TestScenario("Project1", columnMeta1,null, databse1.getDbId(), databse1.getDbId(),
				databse1.getDbId(), databse1.getDbId(), ProjectType.DB_To_DB, "");
		testScenario2 = new TestScenario("Project2", columnMeta2,null, new Long(0), databse2.getDbId(), new Long(0),
				databse2.getDbId(), ProjectType.File_To_DB, "");
		try {
			testScenarioDao.addTestScenarios(testScenario1);
			testScenarioDao.addTestScenarios(testScenario2);
		} catch (PersistException e) {
			e.printStackTrace();
		}

	}

	private static void addLookUpCols() {

		lookUpCol1 = new LookUpCols(queryEntity1, "LookUpCol1", "Select * from Ref", LookUpType.SOURCE_LOOK_UP);
		lookUpCol2 = new LookUpCols(queryEntity1, "LookUpCol2", "Select * from Ref", LookUpType.TARGET_LOOK_UP);
		lookUpCol3 = new LookUpCols(queryEntity2, "LookUpCol3", "Select * from Ref", LookUpType.SOURCE_LOOK_UP);
		lookUpCol4 = new LookUpCols(queryEntity2, "LookUpCol4", "Select * from Ref", LookUpType.TARGET_LOOK_UP);
		lookUpCol5 = new LookUpCols(queryEntity3, "LookUpCol5", "Select * from Ref", LookUpType.SOURCE_LOOK_UP);
		lookUpCol6 = new LookUpCols(queryEntity3, "LookUpCol6", "Select * from Ref", LookUpType.TARGET_LOOK_UP);
		lookUpCol7 = new LookUpCols(queryEntity4, "LookUpCol7", "Select * from Ref", LookUpType.SOURCE_LOOK_UP);
		lookUpCol8 = new LookUpCols(queryEntity4, "LookUpCol8", "Select * from Ref", LookUpType.TARGET_LOOK_UP);

		try {
			lookUpColumnDao.addLookUpQuery(lookUpCol1);
			lookUpColumnDao.addLookUpQuery(lookUpCol2);
			lookUpColumnDao.addLookUpQuery(lookUpCol3);
			lookUpColumnDao.addLookUpQuery(lookUpCol4);
			lookUpColumnDao.addLookUpQuery(lookUpCol5);
			lookUpColumnDao.addLookUpQuery(lookUpCol6);
		} catch (PersistException e) {
			e.printStackTrace();
		}
	}

	private static void addQueryEntity() {
		queryEntity1 = new QueryEntity(columnMeta1, QueryType.SOURCE, "P,Q,R", "Select * from A", "SourceQuery1");
		queryEntity2 = new QueryEntity(columnMeta1, QueryType.TARGET, "P,Q,R", "Select * from B", "TargetQuery1");
		queryEntity3 = new QueryEntity(columnMeta2, QueryType.SOURCE, "R,S,T", "Select * from C", "SourceQuery2");
		queryEntity4 = new QueryEntity(columnMeta2, QueryType.TARGET, "R,S,T", "Select * from D", "TargetQuery2");
		queryEntity5 = new QueryEntity(columnMeta3, QueryType.SOURCE, "U,V,W", "Select * from E", "SourceQuery3");
		queryEntity6 = new QueryEntity(columnMeta3, QueryType.TARGET, "U,V,W", "Select * from F", "TargetQuery3");
		queryEntity7 = new QueryEntity(columnMeta4, QueryType.SOURCE, "U,V,W", "Select * from F", "SourceQuery3");

		try {
			queryDao.addQuery(queryEntity1);
			queryDao.addQuery(queryEntity2);
			queryDao.addQuery(queryEntity3);
			queryDao.addQuery(queryEntity4);
			queryDao.addQuery(queryEntity5);
			queryDao.addQuery(queryEntity6);
			queryDao.addQuery(queryEntity7);
		} catch (PersistException e) {
			e.printStackTrace();
		}
	}

	private static void addFiles() {

		Files file1 = new Files(FileTypes.CSV, "File1", columnMeta1, ",");
		Files file2 = new Files(FileTypes.CSV, "File2", columnMeta1, ",");
		Files file3 = new Files(FileTypes.TXT, "File3", columnMeta2, ",");
		Files file4 = new Files(FileTypes.CSV, "File4", columnMeta2, ",");
		Files file5 = new Files(FileTypes.CSV, "File5", columnMeta3, ",");
		Files file6 = new Files(FileTypes.CSV, "File6", columnMeta3, ",");

		try {
			fileDAO.saveFile(file1);
			fileDAO.saveFile(file2);
			fileDAO.saveFile(file3);
			fileDAO.saveFile(file4);
			fileDAO.saveFile(file5);
			fileDAO.saveFile(file6);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	private static void addColumnMeta() {
		columnMeta1 = new ColumnMeta("ColMetaName1", "A,B,C", "A");
		columnMeta2 = new ColumnMeta("ColMetaName2", "D,E,F", "A");
		columnMeta3 = new ColumnMeta("ColMetaName3", "G,H,I", "A");
		columnMeta4 = new ColumnMeta("ColMetaName4", "G,H,I", "A");
		try {
			columnMetaDao.saveColumnMeta(columnMeta1);
			columnMetaDao.saveColumnMeta(columnMeta2);
			columnMetaDao.saveColumnMeta(columnMeta3);
			columnMetaDao.saveColumnMeta(columnMeta4);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	private static void addDatabase() {
		databse1 = new Database("First Connection", "", "DB1", "ServerName1", "UserName1", "Passwoed1", "12221",
				DBTypes.DB2);
		databse2 = new Database("Second Connection", "", "DB2", "ServerName2", "UserName2", "Passwoed2", "12222",
				DBTypes.MYSQL);
		databse3 = new Database("Trhird Connection", "", "DB3", "ServerName3", "UserName3", "Passwoed3", "12223",
				DBTypes.ORACLE);

		try {
			databaseDao.saveDatabse(databse1);
			databaseDao.saveDatabse(databse2);
			databaseDao.saveDatabse(databse3);
		} catch (PersistException e) {
			e.printStackTrace();
		}
	}
}
