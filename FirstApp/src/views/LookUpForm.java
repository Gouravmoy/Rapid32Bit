
package views;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import dao.ColumnMetaDao;
import dao.LookUpColumnDao;
import dao.QueryDao;
import daoImpl.ColumnMetaDAOImpl;
import daoImpl.LookUpQueryDaoImpl;
import daoImpl.QueryDaoImpl;
import dataProvider.CellModifier;
import dataProvider.ListColumns;
import dataProvider.ModelContentProvider;
import dataProvider.ModelLabelProvider;
import entity.ColumnMeta;
import entity.LookUpCols;
import entity.QueryEntity;
import exceptions.DAOException;
import exceptions.PersistException;
import exceptions.ReadEntityException;

public class LookUpForm {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text textName;
	static QueryDao queryDao;
	static ColumnMetaDao columnMetaDao;
	LookUpColumnDao lookUpColumnDao;
	private static String COLUMN_NAMES[] = { "LookUpColumn", "Query" };
	Table table;
	TableViewer tableViewer;
	String[] listColums = {};
	List<LookUpCols> lookupCols = new ArrayList<LookUpCols>();
	static QueryEntity queryEntity;

	@Inject
	public LookUpForm() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		queryDao = new QueryDaoImpl();
		columnMetaDao = new ColumnMetaDAOImpl();
		lookUpColumnDao = new LookUpQueryDaoImpl();
		parent.setLayout(null);
		Form frmAddLookup = formToolkit.createForm(parent);
		frmAddLookup.setBounds(5, 5, 824, 479);
		formToolkit.paintBordersFor(frmAddLookup);
		frmAddLookup.setText("ADD LOOKUP");
		frmAddLookup.getBody().setLayout(null);

		Label llokUpNameLabel = formToolkit.createLabel(frmAddLookup.getBody(), "Name", SWT.NONE);
		llokUpNameLabel.setBounds(47, 23, 136, 15);

		Label lblQueryName = formToolkit.createLabel(frmAddLookup.getBody(), "Query Name", SWT.NONE);
		lblQueryName.setBounds(47, 62, 136, 15);

		textName = new Text(frmAddLookup.getBody(), SWT.BORDER);
		textName.setBounds(425, 20, 221, 21);
		formToolkit.adapt(textName, true, true);

		Combo comboQueryName = new Combo(frmAddLookup.getBody(), SWT.NONE);
		comboQueryName.setBounds(425, 59, 221, 23);
		formToolkit.adapt(comboQueryName);
		formToolkit.paintBordersFor(comboQueryName);

		Button btnAdd = new Button(frmAddLookup.getBody(), SWT.NONE);
		btnAdd.setBounds(194, 357, 75, 25);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comboQueryName.setEnabled(false);
				LookUpCols lookUpCols = new LookUpCols();
				lookUpCols.setLookUpColName("Name");
				lookupCols.add(lookUpCols);
				tableViewer.setInput(lookupCols);
				tableViewer.refresh();
			}
		});
		formToolkit.adapt(btnAdd, true, true);
		btnAdd.setText("ADD");
		table = new Table(frmAddLookup.getBody(), SWT.NULL);
		table.setBounds(47, 116, 599, 215);
		createTable(parent);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		tableViewer.setColumnProperties(COLUMN_NAMES);

		Button btnDone = new Button(frmAddLookup.getBody(), SWT.NONE);
		btnDone.setBounds(425, 357, 75, 25);
		btnDone.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (LookUpCols lookUpCols : lookupCols) {
					lookUpCols.setQuery(queryEntity);
					try {
						lookUpColumnDao.addLookUpQuery(lookUpCols);
						WorkBenchTreesView.createMetaDataTree();
					} catch (DAOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		formToolkit.adapt(btnDone, true, true);
		btnDone.setText("Done");
		try {
			for (QueryEntity queryEntity : queryDao.getAllQueries()) {
				comboQueryName.add(queryEntity.getQueryName());
			}
		} catch (ReadEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		comboQueryName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				ListColumns.INSTANCES = getListOfColumns(comboQueryName.getText());
				createTableViewer();
				tableViewer.setContentProvider(new ModelContentProvider());
				tableViewer.setLabelProvider(new ModelLabelProvider());
				tableViewer.setInput(lookupCols);
				tableViewer.refresh();

			}
		});

	}

	private void createTableViewer() {
		CellEditor[] editors = new CellEditor[2];
		TextCellEditor textCellEditor = new TextCellEditor(table, SWT.NULL);
		editors[1] = textCellEditor;
		editors[0] = new ComboBoxCellEditor(table, ListColumns.INSTANCES, SWT.NULL);
		tableViewer.setCellEditors(editors);
		tableViewer.setCellModifier(new CellModifier(tableViewer));

	}

	private void createTable(Composite parent) {
		for (int i = 0; i < COLUMN_NAMES.length; i++) {
			TableColumn column = new TableColumn(table, SWT.LEFT, i);
			column.setText(COLUMN_NAMES[i]);
			column.setWidth(400);
		}

	}

	public static String[] getListOfColumns(String text) {
		ArrayList<String> listColumnsAList = new ArrayList<String>();
		String[] listColums = null;
		try {
			queryEntity = queryDao.getQueryByName(text);
			ColumnMeta columnMeta = columnMetaDao.getColumnMetaById(queryEntity.getQueryColumnMeta().getIdColumnMeta());
			for (String column : columnMeta.getColNames().split("\\,")) {
				listColumnsAList.add(column);
			}
			listColums = listColumnsAList.toArray(new String[listColumnsAList.size()]);
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return listColums;
	}
}