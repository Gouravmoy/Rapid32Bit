package entity;

import java.util.Date;

import enums.LookUpType;

public class LookUpCols {
	
	Long lookUpId;
	QueryEntity query;
	String lookUpColName;
	String lookUpQuery;
	Date lastUpdtTS;

	public LookUpCols() {
		super();
	}

	public LookUpCols(QueryEntity query, String lookUpColName, String lookUpQuery, LookUpType lookUpType) {
		super();
		this.query = query;
		this.lookUpColName = lookUpColName;
		this.lookUpQuery = lookUpQuery;
		this.lastUpdtTS = new Date();
	}
	
	

	public Long getLookUpId() {
		return lookUpId;
	}

	public void setLookUpId(Long lookUpId) {
		this.lookUpId = lookUpId;
	}

	public QueryEntity getQuery() {
		return query;
	}

	public void setQuery(QueryEntity query) {
		this.query = query;
	}

	public String getLookUpColName() {
		return lookUpColName;
	}

	public void setLookUpColName(String lookUpColName) {
		this.lookUpColName = lookUpColName;
	}

	public String getLookUpQuery() {
		return lookUpQuery;
	}

	public void setLookUpQuery(String lookUpQuery) {
		this.lookUpQuery = lookUpQuery;
	}

	public Date getLastUpdtTS() {
		return lastUpdtTS;
	}

	public void setLastUpdtTS(Date lastUpdtTS) {
		this.lastUpdtTS = lastUpdtTS;
	}

	@Override
	public String toString() {
		return lookUpColName;
	}

}
