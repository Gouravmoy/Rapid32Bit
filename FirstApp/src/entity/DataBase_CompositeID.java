package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Embeddable
public class DataBase_CompositeID implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer dbId;

	public Integer getDbId() {
		return dbId;
	}

	public void setDbId(Integer dbId) {
		this.dbId = dbId;
	}

	@Column
	private String dbName;

	public DataBase_CompositeID(String dbName, Integer dbId) {
		super();
		this.dbName = dbName;
		this.dbId = dbId;
	}

	public DataBase_CompositeID() {
		super();
	}

}