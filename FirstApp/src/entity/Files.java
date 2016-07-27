package entity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

import enums.FileTypes;

public class Files implements PropertyChangeListener {
	Long fileId = new Long(1);
	ColumnMeta fileColumnMeta;
	FileTypes fileTypes;
	String fileName;
	String separator;
	Date lastUpdtTS;

	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public Files() {
		super();
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public ColumnMeta getFileColumnMeta() {
		return fileColumnMeta;
	}

	public void setFileColumnMeta(ColumnMeta fileColumnMeta) {
		this.fileColumnMeta = fileColumnMeta;
	}

	public FileTypes getFileTypes() {
		return fileTypes;
	}

	public void setFileTypes(FileTypes fileTypes) {
		this.fileTypes = fileTypes;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getLastUpdtTS() {
		return lastUpdtTS;
	}

	public void setLastUpdtTS(Date lastUpdtTS) {
		this.lastUpdtTS = lastUpdtTS;
	}

	public Files(FileTypes fileTypes, String fileName) {
		super();
		this.fileTypes = fileTypes;
		this.fileName = fileName;
		this.lastUpdtTS = new Date();
	}

	public Files(FileTypes fileTypes, String fileName, ColumnMeta columnMeta, String separator) {
		super();
		this.fileTypes = fileTypes;
		this.fileName = fileName;
		this.fileColumnMeta = columnMeta;
		this.separator = separator;
		this.lastUpdtTS = new Date();
	}

	@Override
	public String toString() {
		return fileName;
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		propertyChangeSupport.firePropertyChange("Change", true, true);

	}

}
