package views.wizardPages.workflow;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import dao.ColumnDao;
import daoImpl.ColumnDaoImpl;
import dataProvider.CellModifier;
import dataProvider.ListColumns;
import dataProvider.ModelContentProvider;
import dataProvider.ModelLabelProvider;
import entity.Column;
import entity.LookUpCols;
import entity.QueryEntity;
import exceptions.EntityNotPresent;

public class LookUpQueriesWizardPage extends WizardPage {

	private Composite container;

	Text queryName;
	private static String COLUMN_NAMES[] = { "LookUpColumn", "Query" };
	Table table;
	TableViewer tableViewer;
	List<LookUpCols> lookupCols;
	boolean createTableView = true;;
	QueryEntity queryEntity;

	public LookUpQueriesWizardPage(String pageName, List<LookUpCols> lookupCols, QueryEntity queryEntity) {
		super(pageName);
		setDescription("Create a Source/Target LookUp Queries ");
		this.lookupCols = lookupCols;
		this.queryEntity = queryEntity;
	}

	public List<LookUpCols> getLookupCols() {
		return lookupCols;
	}

	public void setLookupCols(List<LookUpCols> lookupCols) {
		this.lookupCols = lookupCols;
		queryName.setText(queryEntity.getQueryName());
	}

	@Override
	public void createControl(Composite parent) {

		container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label lblLookupForQuery = new Label(container, SWT.NONE);
		lblLookupForQuery.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblLookupForQuery.setText("LookUp for Query");

		queryName = new Text(container, SWT.NONE);
		GridData gd_comboQueryName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_comboQueryName.widthHint = 507;
		queryName.setLayoutData(gd_comboQueryName);
		queryName.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!queryName.getText().isEmpty()) {
					setPageComplete(true);
				}
			}

		});

		new Label(container, SWT.NONE);

		table = new Table(container, SWT.NULL);
		GridData gd_table = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_table.heightHint = 135;
		gd_table.widthHint = 502;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		new Label(container, SWT.NONE);
		// createTableViewer();
		Button btnAdd = new Button(container, SWT.NONE);
		btnAdd.setText("Add LookUp Column");
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (createTableView) {
					createTableViewer();
				}
				LookUpCols lookUpCols = new LookUpCols();
				lookUpCols.setQuery(queryEntity);
				lookUpCols.setLookUpColName("");
				lookupCols.add(lookUpCols);
				tableViewer.setInput(lookupCols);
				tableViewer.refresh();

			}
		});
		new Label(container, SWT.NONE);
		createTable(parent);

		setPageComplete(true);

	}

	private void createTableViewer() {
		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		tableViewer.setColumnProperties(COLUMN_NAMES);
		tableViewer.setContentProvider(new ModelContentProvider());
		tableViewer.setLabelProvider(new ModelLabelProvider());
		tableViewer.setInput(lookupCols);
		tableViewer.refresh();
		CellEditor[] editors = new CellEditor[2];
		TextCellEditor textCellEditor = new TextCellEditor(table, SWT.NULL);
		editors[1] = textCellEditor;
		editors[0] = new ComboBoxCellEditor(table, ListColumns.getInstances(), SWT.NULL);
		tableViewer.setCellEditors(editors);
		tableViewer.setCellModifier(new CellModifier(tableViewer));
		createTableView = false;

	}

	private void getColumns() {
		ColumnDao columnDao = new ColumnDaoImpl();
		try {
			ArrayList<String> columnList = new ArrayList<String>();
			for (Column column : columnDao.getColumnByCMId(queryEntity.getQueryColumnMeta().getIdColumnMeta())) {
				columnList.add(column.getColumnName());
			}

			ListColumns.INSTANCES = columnList.toArray(ListColumns.INSTANCES);
			createTableViewer();

		} catch (EntityNotPresent e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createTable(Composite parent) {
		for (int i = 0; i < COLUMN_NAMES.length; i++) {
			TableColumn column = new TableColumn(table, SWT.LEFT, i);
			column.setText(COLUMN_NAMES[i]);
			column.setWidth(400);
		}
		if (queryEntity.getQueryId() != null) {
			getColumns();
		}

	}

}
