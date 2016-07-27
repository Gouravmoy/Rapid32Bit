package check;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectToDB {

	public static Connection ConnectToDBC() {
		Connection conn = null;
		String dbURL = "jdbc:derby:simpleDB1;create=true";
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			// Get a connection
			conn = DriverManager.getConnection(dbURL);
		} catch (Exception except) {
			except.printStackTrace();
		}
		return conn;

	}

}
