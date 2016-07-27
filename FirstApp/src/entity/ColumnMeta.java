package entity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ColumnMeta implements PropertyChangeListener {

	Long idColumnMeta;
	String columnMetaName;
	String colNames;
	String uniqueCols;
	Date lastUpdtTS;


	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public String getUniqueCols() {
		return uniqueCols;
	}

	public void setUniqueCols(String uniqueCols) {
		this.uniqueCols = uniqueCols;
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public Long getIdColumnMeta() {
		return idColumnMeta;
	}

	public void setIdColumnMeta(Long idColumnMeta) {
		this.idColumnMeta = idColumnMeta;
	}

	public String getColumnMetaName() {
		return columnMetaName;
	}

	public void setColumnMetaName(String columnMetaName) {
		this.columnMetaName = columnMetaName;
	}

	public String getColNames() {
		return colNames;
	}

	public void setColNames(String colNames) {
		this.colNames = colNames;
	}

	public Date getLastUpdtTS() {
		return lastUpdtTS;
	}

	public void setLastUpdtTS(Date lastUpdtTS) {
		this.lastUpdtTS = lastUpdtTS;
	}

	public ColumnMeta(String columnMetaName, String colNames, String uniqueCols) {
		super();
		this.columnMetaName = columnMetaName;
		this.colNames = colNames;
		this.lastUpdtTS = new Date();
	}

	public ColumnMeta(String colNames, List<QueryEntity> queryList, Set<Files> files, TestScenario project) {
		super();
		this.colNames = colNames;
		this.lastUpdtTS = new Date();
	}

	public ColumnMeta() {
		super();
		this.columnMetaName = "";

	}

	@Override
	public String toString() {
		return columnMetaName;

	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		propertyChangeSupport.firePropertyChange("Change", true, true);
	}

}
