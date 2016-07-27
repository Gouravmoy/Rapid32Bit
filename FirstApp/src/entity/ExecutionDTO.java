package entity;

import java.sql.Date;
import java.util.List;

import enums.VolumeOfData;

public class ExecutionDTO {

	String execInstName;
	String execInstDesc;
	List<ScenarioExecData> scenarioExecDatas;
	VolumeOfData volumeOfDataIND;
	Date esecDatetimeStamp;

	public String getExecInstName() {
		return execInstName;
	}

	public void setExecInstName(String execInstName) {
		this.execInstName = execInstName;
	}

	public String getExecInstDesc() {
		return execInstDesc;
	}

	public void setExecInstDesc(String execInstDesc) {
		this.execInstDesc = execInstDesc;
	}

	public List<ScenarioExecData> getScenarioExecDatas() {
		return scenarioExecDatas;
	}

	public void setScenarioExecDatas(List<ScenarioExecData> scenarioExecDatas) {
		this.scenarioExecDatas = scenarioExecDatas;
	}

	public VolumeOfData getVolumeOfDataIND() {
		return volumeOfDataIND;
	}

	public void setVolumeOfDataIND(VolumeOfData volumeOfDataIND) {
		this.volumeOfDataIND = volumeOfDataIND;
	}

	public Date getEsecDatetimeStamp() {
		return esecDatetimeStamp;
	}

	public void setEsecDatetimeStamp(Date esecDatetimeStamp) {
		this.esecDatetimeStamp = esecDatetimeStamp;
	}

	public ExecutionDTO(String execInstName, String execInstDesc, List<ScenarioExecData> scenarioExecDatas,
			VolumeOfData volumeOfDataIND, Date esecDatetimeStamp) {
		super();
		this.execInstName = execInstName;
		this.execInstDesc = execInstDesc;
		this.scenarioExecDatas = scenarioExecDatas;
		this.volumeOfDataIND = volumeOfDataIND;
		this.esecDatetimeStamp = esecDatetimeStamp;
	}

	@Override
	public String toString() {
		return "ExecutionDTO [execInstName=" + execInstName + ", execInstDesc=" + execInstDesc + ", scenarioExecDatas="
				+ scenarioExecDatas + ", volumeOfDataIND=" + volumeOfDataIND + ", esecDatetimeStamp="
				+ esecDatetimeStamp + "]";
	}

}
