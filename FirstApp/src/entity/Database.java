package entity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.Map;

import enums.DBTypes;
import interfaces.PropViewerInterface;

public class Database implements PropViewerInterface, PropertyChangeListener {

	Long dbId;
	String connectionName;
	String connectionDescription;
	String databaseName;
	String serverName;
	String userName;
	String password;
	String portNo;
	DBTypes dbType;
	Date lastUpdtTS;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public String getConnectionName() {
		return connectionName;
	}

	public String getConnectionDescription() {
		return connectionDescription;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	public void setConnectionDescription(String connectionDescription) {
		this.connectionDescription = connectionDescription;
	}

	public Long getDbId() {
		return dbId;
	}

	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPortNo() {
		return portNo;
	}

	public void setPortNo(String portNo) {
		this.portNo = portNo;
	}

	public DBTypes getDbType() {
		return dbType;
	}

	public void setDbType(DBTypes dbType) {
		this.dbType = dbType;
	}

	public Date getLastUpdtTS() {
		return lastUpdtTS;
	}

	public void setLastUpdtTS(Date lastUpdtTS) {
		this.lastUpdtTS = lastUpdtTS;
	}

	public Database() {
		super();
	}

	public Database(String connectionName, String connectionDescription, String databaseName, String serverName,
			String userName, String password, String portNo, DBTypes dbType) {
		super();
		this.connectionName = connectionName;
		this.connectionDescription = connectionDescription;
		this.databaseName = databaseName;
		this.serverName = serverName;
		this.userName = userName;
		this.password = password;
		this.portNo = portNo;
		this.dbType = dbType;
		this.lastUpdtTS = new Date();
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	@Override
	public String toString() {
		return connectionName;
	}

	@Override
	public String[] geaders() {
		String COLUMN_NAMES[] = { "connectionName", "connectionDescription", "databaseName", "serverName", "userName",
				"password", "portNo", "dbType", "lastUpdtTS" };
		return COLUMN_NAMES;
	}

	@Override
	public Map<String, String> getKeyValueMap() {
		return null;
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		propertyChangeSupport.firePropertyChange("Change", true, true);
	}

}
