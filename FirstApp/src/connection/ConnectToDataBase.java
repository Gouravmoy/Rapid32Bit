package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.rolling.RollingFileAppender;
import entity.Database;

public class ConnectToDataBase {
	static Database database;

	static LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
	@SuppressWarnings("rawtypes")
	static RollingFileAppender rfAppender = new RollingFileAppender();
	static Logger logbackLogger;

	@SuppressWarnings({ "unchecked" })
	public static Connection getConnection(Database database) throws Exception {
		ConnectToDataBase.database = database;
		rfAppender.setContext(loggerContext);
		rfAppender.setFile(System.getProperty("log_file_loc") + "/log/" + "rapid" + ".log");
		logbackLogger = loggerContext.getLogger("MainController");
		logbackLogger.addAppender(rfAppender);

		switch (database.getDbType().toString()) {
		case "MYSQL":
			return getMySQlConnection();
		case "DB2":
			return getDB2Connection();
		case "ORACLE":
			return getOracleConnection();
		case "HADOOP_HIVE":
			return getHiveConnection();
		default:
			return null;
		}
	}

	private static Connection getHiveConnection() throws ClassNotFoundException, SQLException {
		Connection con = null;
		logbackLogger.info("Trying to conect to Hive Connection");
		try {
			Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver");
			con = null;
			con = DriverManager.getConnection("jdbc:hive://" + database.getServerName() + ":" + database.getPortNo(),
					database.getUserName(), database.getPassword());
		} catch (Exception err) {
			logbackLogger.error(err.getMessage(), err);
		}
		return con;
	}

	private static Connection getOracleConnection() throws ClassNotFoundException, SQLException {
		Connection con;
		logbackLogger.info("Trying to conect to Oracle Connection");
		String url = "";
		url = "jdbc:oracle:thin:@" + database.getServerName() + ":" + database.getPortNo() + "/"
				+ database.getDatabaseName();
		Class.forName("oracle.jdbc.driver.OracleDriver");
		try {
			con = DriverManager.getConnection(url, database.getUserName(), database.getPassword());
		} catch (Exception err) {
			err.printStackTrace();
			logbackLogger.error(err.getMessage() + url + "User Name " + database.getUserName() + "Password "
					+ database.getPassword(), err);
			return null;
		}
		return con;
	}

	private static Connection getDB2Connection() {
		return null;
	}

	private static Connection getMySQlConnection() throws Exception {
		logbackLogger.info("Trying to conect to MySQL Connection");
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = null;
		String connectionString;
		connectionString = "jdbc:mysql://";
		connectionString += database.getServerName();
		connectionString += ":" + database.getPortNo();
		connectionString += "/" + database.getDatabaseName();
		try {
			con = DriverManager.getConnection(connectionString, database.getUserName(), database.getPassword());
		} catch (Exception err) {
			err.printStackTrace();
			logbackLogger.error(err.getMessage() + connectionString + "User Name " + database.getUserName()
					+ "Password " + database.getPassword(), err);
			return null;
		}
		return con;
	}
}
