package entity;

import java.util.Date;

import enums.ColumnType;

public class Column {
	Long columnId;
	String columnName;
	ColumnType columnType;
	String columnLength;
	String decimalLength;
	ColumnMeta columnMeta;
	Boolean uniqueColumn;
	Date date;
	String defaultValue;

	public Column() {
		super();
		this.date = new Date();
		columnName = "";
		columnLength = "";
		decimalLength = "";
		columnType = ColumnType.Varchar;
		uniqueColumn = false;
	}

	public Column(String colname) {
		super();
		this.date = new Date();
		columnName = colname;
		columnLength = "";
		decimalLength = "";
		columnType = ColumnType.Varchar;
		uniqueColumn = false;
	}

	public Column(String columnName, ColumnType columnType, String columnLength, String decimalLength,
			ColumnMeta columnMeta) {
		super();
		this.columnName = columnName;
		this.columnType = columnType;
		this.columnLength = columnLength;
		this.decimalLength = decimalLength;
		this.columnMeta = columnMeta;
		this.date = new Date();
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public ColumnType getColumnType() {
		return columnType;
	}

	public void setColumnType(ColumnType columnType) {
		this.columnType = columnType;
	}

	public String getColumnLength() {
		return columnLength;
	}

	public void setColumnLength(String columnLength) {
		this.columnLength = columnLength;
	}

	public String getDecimalLength() {
		return decimalLength;
	}

	public void setDecimalLength(String decimalLength) {
		this.decimalLength = decimalLength;
	}

	public ColumnMeta getColumnMeta() {
		return columnMeta;
	}

	public void setColumnMeta(ColumnMeta columnMeta) {
		this.columnMeta = columnMeta;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return columnName;
	}

	public Boolean getUniqueColumn() {
		return uniqueColumn;
	}

	public void setUniqueColumn(Boolean uniqueColumn) {
		this.uniqueColumn = uniqueColumn;
	}

}
