package model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.Person;

public class PersonTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	List<Person> personList;
	private final String[] columnNames = new String[] { "Id", "Name", "LastName", "Gender" };
	@SuppressWarnings("rawtypes")
	private final Class[] columnClass = new Class[] { Integer.class, String.class, String.class, String.class };

	public PersonTableModel(List<Person> personList) {
		super();
		this.personList = personList;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClass[columnIndex];
	}

	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}

	@Override
	public int getRowCount() {
		return this.personList.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Person row = personList.get(rowIndex);
		if (0 == columnIndex) {
			return row.getId();
		} else if (1 == columnIndex) {
			return row.getFirstName();
		} else if (2 == columnIndex) {
			return row.getLastName();
		} else if (3 == columnIndex) {
			return row.getGender();
		}
		return null;
	}

	public void refresh(List<Person> listPerson) {
		this.personList = listPerson;
	}

}