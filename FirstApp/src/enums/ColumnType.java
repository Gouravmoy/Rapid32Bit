package enums;

public enum ColumnType {

	Varchar, Integer, Boolean, Date;

	public static String[] names() {
		ColumnType[] columnType = values();
		String[] names = new String[columnType.length];

		for (int i = 0; i < columnType.length; i++) {
			names[i] = columnType[i].name();
		}

		return names;
	}

}
