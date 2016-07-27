package dataProvider;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import entity.Column;
import entity.LookUpCols;
import entity.ParamKeyMap;
import enums.ColumnType;
import views.wizardPages.executeModule.MyPageTwo;

public class CellModifier implements ICellModifier {
	TableViewer tableViewer;

	public CellModifier(TableViewer tableViewer2) {
		this.tableViewer = tableViewer2;
	}

	@Override
	public boolean canModify(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Object getValue(Object arg0, String arg1) {
		if (arg0 instanceof LookUpCols) {
			LookUpCols lookUpCols = (LookUpCols) arg0;
			if (arg1.equals("LookUpColumn")) {
				int i = ListColumns.INSTANCES.length - 1;
				while (!arg1.equals(ListColumns.INSTANCES[i]) && i > 0)
					--i;
				System.out.println(i);
				return new Integer(i);
			} else {
				if (lookUpCols.getLookUpQuery() != null) {
					return lookUpCols.getLookUpQuery();
				} else {
					return "";
				}

			}
		} else if (arg0 instanceof ParamKeyMap) {
			ParamKeyMap keyMap;
			keyMap = (ParamKeyMap) arg0;
			if (arg1.equals("QUERY PARAMETERS")) {
				return keyMap.getParamName();
			} else {
				return keyMap.getParamValue();
			}
		} else {
			Column column;
			column = (Column) arg0;
			if (arg1.equals("ColumnName")) {
				return column.getColumnName();
			} else if (arg1.equals("ColumnType")) {
				return column.getColumnType().ordinal();
			} else if (arg1.equals("columnDefaultValue")) {
				return column.getDefaultValue();
			} else if (arg1.equals("ColumnLength")) {
				return column.getColumnLength();
			} else if (arg1.equals("Unique Column")) {
				return Boolean.valueOf(column.getUniqueColumn());
			} else {
				return column.getDecimalLength();
			}
		}

	}

	@Override
	public void modify(Object itemTable, String colName, Object tableValue) {
		TableItem tableItem = (TableItem) itemTable;
		if (tableItem.getData() instanceof LookUpCols) {
			LookUpCols lookUpCols = (LookUpCols) tableItem.getData();
			if (colName.equals("LookUpColumn")) {
				String columnName = ListColumns.INSTANCES[(int) tableValue];
				lookUpCols.setLookUpColName(columnName);
			} else {
				String query = (String) tableValue;
				lookUpCols.setLookUpQuery(query);
			}
		} else if (tableItem.getData() instanceof ParamKeyMap) {
			ParamKeyMap paramKeyMap = (ParamKeyMap) tableItem.getData();
			if (colName.equals("INPUT")) {
				paramKeyMap.setParamValue((String) tableValue);
				MyPageTwo.refreshTables();
			}
		} else {
			Column column = (Column) tableItem.getData();
			if (colName.equals("ColumnName")) {
				column.setColumnName((String) tableValue);
			} else if (colName.equals("ColumnType")) {
				ColumnType columnType = ColumnType.values()[(int) tableValue];
				column.setColumnType(columnType);
			} else if (colName.equals("columnDefaultValue")) {
				column.setDefaultValue((String) tableValue);
			} else if (colName.equals("ColumnLength")) {
				column.setColumnLength((String) tableValue);
			} else if (colName.equals("Unique Column")) {
				column.setUniqueColumn(((Boolean) tableValue).booleanValue());
			} else {
				column.setDecimalLength((String) tableValue);
			}
		}
		tableViewer.refresh();
	}

}
