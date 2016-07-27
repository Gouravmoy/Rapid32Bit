package entity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

import enums.QueryType;

public class QueryEntity implements PropertyChangeListener {
	Long queryId;
	ColumnMeta queryColumnMeta;
	QueryType queryType;
	String paramNames;
	String mainQuery;
	Date lastUpdtTS;
	String queryName;
	boolean dataByColNameFlag;

	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public Long getQueryId() {
		return queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}

	public ColumnMeta getQueryColumnMeta() {
		return queryColumnMeta;
	}

	public void setQueryColumnMeta(ColumnMeta queryColumnMeta) {
		this.queryColumnMeta = queryColumnMeta;
	}

	public QueryType getQueryType() {
		return queryType;
	}

	public void setQueryType(QueryType queryType) {
		this.queryType = queryType;
	}

	public String getParamNames() {
		return paramNames;
	}

	public void setParamNames(String paramNames) {
		this.paramNames = paramNames;
	}

	public String getMainQuery() {
		return mainQuery;
	}

	public void setMainQuery(String mainQuery) {
		this.mainQuery = mainQuery;
	}

	public Date getLastUpdtTS() {
		return lastUpdtTS;
	}

	public void setLastUpdtTS(Date lastUpdtTS) {
		this.lastUpdtTS = lastUpdtTS;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	
	public boolean isDataByColNameFlag() {
		return dataByColNameFlag;
	}

	public void setDataByColNameFlag(boolean dataByColNameFlag) {
		this.dataByColNameFlag = dataByColNameFlag;
	}

	public QueryEntity(ColumnMeta queryColumnMeta, QueryType queryType, String paramNames, String mainQuery,
			String queryName) {
		super();
		this.queryColumnMeta = queryColumnMeta;
		this.queryType = queryType;
		this.paramNames = paramNames;
		this.mainQuery = mainQuery;
		this.queryName = queryName;
		this.lastUpdtTS = new Date();
	}

	public QueryEntity() {
		super();
	}

	@Override
	public String toString() {
		return queryName;
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getSource().toString().equals("")) {

		}

	}

}
