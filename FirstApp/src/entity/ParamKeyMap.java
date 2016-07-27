package entity;

public class ParamKeyMap {

	String paramName;
	String paramValue;

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public ParamKeyMap(String paramName, String paramValue) {
		super();
		this.paramName = paramName;
		this.paramValue = paramValue;
	}

	@Override
	public String toString() {
		return "ParamKeyMap [paramName=" + paramName + ", paramValue=" + paramValue + "]";
	}

}
