package views.wizardPages.workflow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import dataProvider.CellModifier;
import dataProvider.ModelContentProvider;
import dataProvider.ModelLabelProvider;
import entity.Column;
import entity.ColumnMeta;
import enums.ColumnType;

public class ColumnMetaWizardPage extends WizardPage {
	ColumnMeta columnMeta;

	private Composite container;
	private Text colMetaName;
	private Table table;
	TableViewer tableViewer;
	TableViewer tableViewerUnique;
	private static String COLUMN_NAMES[] = { "Unique Column", "ColumnName", "ColumnType", "columnDefaultValue",
			"ColumnLength", "DecimalLenth", };
	List<Column> columnList;
	boolean createTableView = true;

	public ColumnMetaWizardPage(String pageName, ColumnMeta columnMeta, List<Column> coList) {
		super(pageName);
		setTitle(pageName);
		setDescription("Create a Column Metadata ");
		this.columnMeta = columnMeta;
		this.columnList = coList;
	}

	public ColumnMeta getColumnMeta() {
		return columnMeta;
	}

	public List<Column> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<Column> columnList) {
		this.columnList = columnList;
	}

	@Override
	public void createControl(Composite parent) {

		container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(null);
		Label lblColumnMetadataName = new Label(container, SWT.NONE);
		lblColumnMetadataName.setBounds(5, 8, 131, 15);
		lblColumnMetadataName.setText("Column Metadata Name");

		colMetaName = new Text(container, SWT.BORDER);
		colMetaName.setBounds(164, 5, 509, 21);
		colMetaName.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!colMetaName.getText().isEmpty()) {
					setPageComplete(true);
				}
			}

		});

		Button btnImportColumns = new Button(container, SWT.NONE);
		btnImportColumns.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
				fd.setText("Open");
				fd.setFilterPath("C:/");
				String[] filterExt = { "*.csv", "*.txt", "*.*" };
				fd.setFilterExtensions(filterExt);
				String selected = fd.open();
				if (selected != null)
					importFile(selected);
				createTableViewer();
				tableViewer.setInput(columnList);
				tableViewer.refresh();
			}

			private void importFile(String selected) {
				File file = new File(selected);
				FileReader fileReader = null;
				String sCurrentLine = "";
				String importValues[] = null;
				Column column;
				try {
					fileReader = new FileReader(file);
					BufferedReader bufferedReader = new BufferedReader(fileReader);
					while ((sCurrentLine = bufferedReader.readLine()) != null) {
						importValues = sCurrentLine.split("\\,");
						column = new Column();
						column.setColumnMeta(columnMeta);
						column.setColumnName(importValues[0]);
						column.setColumnType(ColumnType.valueOf(importValues[1]));
						if (importValues[2].equals("")) {
							column.setDefaultValue("");
						} else {
							column.setDefaultValue(importValues[2]);
						}
						column.setColumnLength(importValues[3]);
						if (importValues.length == 5) {
							column.setDecimalLength(importValues[4]);
						}
						columnList.add(column);
					}
					bufferedReader.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {

				}
			}
		});
		btnImportColumns.setBounds(164, 206, 121, 25);
		btnImportColumns.setText("Import Columns");

		Button btnClearAll = new Button(container, SWT.NONE);
		btnClearAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				colMetaName.setText("");
				columnList.clear();
				tableViewer.setInput(columnList);
				tableViewer.refresh();
			}
		});
		btnClearAll.setBounds(164, 265, 75, 25);
		btnClearAll.setText("Reset");

		Label label = new Label(container, SWT.NONE);
		label.setText("List of Columns");
		label.setBounds(5, 89, 83, 15);

		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(164, 32, 509, 168);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		createTable(parent);

		Button btnAddColumn = new Button(container, SWT.NONE);
		btnAddColumn.setBounds(313, 206, 75, 25);
		btnAddColumn.setText("Add Column");

		btnAddColumn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (createTableView) {
					createTableViewer();
				}
				Column column = new Column();
				column.setColumnMeta(columnMeta);
				columnList.add(column);
				tableViewer.setInput(columnList);
				tableViewer.refresh();
			}
		});
		if (columnMeta.getIdColumnMeta() == null) {
			if (columnMeta != null) {
				if (columnMeta.getColNames() != null)
					setPageComplete(true);
			} else {
				setPageComplete(false);
			}
		}
		bindValue();
	}

	private void bindValue() {
		DataBindingContext ctx = new DataBindingContext();
		IObservableValue widgetValue = WidgetProperties.text(SWT.Modify).observe(colMetaName);
		IObservableValue modelValue = BeanProperties.value(ColumnMeta.class, "columnMetaName").observe(columnMeta);
		UpdateValueStrategy update = new UpdateValueStrategy();
		ctx.bindValue(widgetValue, modelValue, update, null);

	}

	private void createTableViewer() {
		if (createTableView) {
			tableViewer = new TableViewer(table);
			tableViewer.setUseHashlookup(true);
			tableViewer.setColumnProperties(COLUMN_NAMES);
			tableViewer.setContentProvider(new ModelContentProvider());
			tableViewer.setLabelProvider(new ModelLabelProvider());
			tableViewer.setInput(columnList);
			tableViewer.refresh();
			CellEditor[] editors = new CellEditor[6];
			editors[0] = new CheckboxCellEditor(table, SWT.CHECK);
			editors[1] = new TextCellEditor(table, SWT.NULL);
			editors[2] = new ComboBoxCellEditor(table, ColumnType.names(), SWT.CHECK);
			editors[3] = new TextCellEditor(table, SWT.NULL);
			editors[4] = new TextCellEditor(table, SWT.NULL);
			editors[5] = new TextCellEditor(table, SWT.NULL);
			tableViewer.setCellEditors(editors);
			tableViewer.setCellModifier(new CellModifier(tableViewer));
			createTableView = false;
		}
	}

	private void createTable(Composite parent) {
		for (int i = 0; i < COLUMN_NAMES.length; i++) {
			TableColumn column = new TableColumn(table, SWT.CENTER, i);
			column.setText(COLUMN_NAMES[i]);
			column.setWidth(80);
		}
		if (columnMeta.getIdColumnMeta() != null) {
			createTableViewer();
		}

	}
}
