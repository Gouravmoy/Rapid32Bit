package views.wizardPages.executeModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import dao.TestScenarioDao;
import daoImpl.TestScenarioDaoImpl;
import dataProvider.CellModifier;
import dataProvider.ModelContentProvider;
import dataProvider.ModelLabelProvider;
import entity.ColumnMeta;
import entity.ParamKeyMap;
import entity.QueryEntity;
import entity.TestScenario;
import enums.JobType;
import enums.ProjectType;
import enums.QueryType;
import exceptions.ReadEntityException;
import exceptions.ServiceException;
import service.QueryService;
import serviceImpl.QueryServiceImpl;

public class MyPageTwo extends WizardPage {
	private Composite container;

	private static String COLUMN_NAMES[] = { "QUERY PARAMETERS", "INPUT" };

	private static TableViewer sourceParamTableViewer;
	private static TableViewer targetParamTableViewer;

	private Table sourceParamTable;
	private Table targetParamTable;

	private Text fileInputText;
	private Button fileBrowser;
	private Button targetFileBrowseBtn;
	private Combo testScenarioCombobox;

	private TestScenarioDao testScenarioDao;
	private QueryService queryService;

	private List<TestScenario> allTestScenarios;
	List<String> sourceParamNames;
	List<String> targetParamNames;

	static List<ParamKeyMap> sourceParamKeyMaps;
	static List<ParamKeyMap> targetParamKeyMaps;
	private Label lblTargetFileLocation;
	private Text targetFileText;
	private Label lblSource;

	protected MyPageTwo() {
		super("Data Runner");
		setTitle("Execute Test Scenarios");
		setDescription("Execute Test Scenarios");
		sourceParamKeyMaps = new ArrayList<>();
		targetParamKeyMaps = new ArrayList<>();
	}

	@Override
	public void createControl(Composite parent) {
		Point size = getShell().computeSize(611, 739);
		getShell().setSize(size);
		allTestScenarios = new ArrayList<>();
		testScenarioDao = new TestScenarioDaoImpl();
		queryService = new QueryServiceImpl();
		try {
			allTestScenarios = testScenarioDao.getAllTestScenarios();
		} catch (ReadEntityException e) {
			e.printStackTrace();
		}

		container = new Composite(parent, SWT.NONE);
		// required to avoid an error in the system
		setControl(container);
		container.setLayout(null);
		Label label1 = new Label(container, SWT.NONE);
		label1.setBounds(5, 29, 84, 15);
		label1.setText("Select Scenarios");

		testScenarioCombobox = new Combo(container, SWT.NONE);
		testScenarioCombobox.setBounds(131, 25, 432, 23);
		testScenarioCombobox.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!testScenarioCombobox.getText().isEmpty()) {
					setPageComplete(true);
				}
			}

		});
		testScenarioCombobox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selectedScenario = testScenarioCombobox.getText();
				ColumnMeta columnMeta = null;
				for (TestScenario scenario : allTestScenarios) {
					if (selectedScenario.equals(scenario.getProjectName())) {
						if (scenario.getProjectType() == ProjectType.DB_To_DB) {
							sourceParamTable.setEnabled(true);
							targetParamTable.setEnabled(true);
							fileInputText.setEnabled(false);
							fileBrowser.setEnabled(false);
							targetFileText.setEnabled(false);
							targetFileBrowseBtn.setEnabled(false);
							columnMeta = scenario.getColumnMeta();
							updateParamTables(columnMeta);
						} else if (scenario.getProjectType() == ProjectType.File_To_DB) {
							sourceParamTable.setEnabled(false);
							targetParamTable.setEnabled(true);
							fileInputText.setEnabled(true);
							fileBrowser.setEnabled(true);
							targetFileText.setEnabled(false);
							targetFileBrowseBtn.setEnabled(false);
							columnMeta = scenario.getColumnMeta();
							updateParamTables(columnMeta);
						} else if (scenario.getProjectType() == ProjectType.DB_To_File) {
							sourceParamTable.setEnabled(true);
							targetParamTable.setEnabled(false);
							fileInputText.setEnabled(false);
							fileBrowser.setEnabled(false);
							targetFileText.setEnabled(true);
							targetFileBrowseBtn.setEnabled(true);
							columnMeta = scenario.getColumnMeta();
							updateParamTables(columnMeta);
						} else if (scenario.getProjectType() == ProjectType.File_To_File) {
							sourceParamTable.setEnabled(false);
							targetParamTable.setEnabled(false);
							fileInputText.setEnabled(true);
							fileBrowser.setEnabled(true);
							targetFileText.setEnabled(true);
							targetFileBrowseBtn.setEnabled(true);
							// columnMeta = scenario.getColumnMeta();
							// updateParamTables(columnMeta);
						}
						break;
					}
				}
			}

			private void updateParamTables(ColumnMeta columnMeta) {
				List<QueryEntity> queryEntities;
				try {
					queryEntities = queryService.getQueryByColumnId(columnMeta.getIdColumnMeta());
					for (QueryEntity queryEntity : queryEntities) {
						if (queryEntity.getQueryType() == QueryType.SOURCE) {
							sourceParamKeyMaps = new ArrayList<>();
							if (queryEntity.getParamNames() != null) {
								sourceParamNames = Arrays.asList(queryEntity.getParamNames().split("\\,"));
								for (String sourceParamName : sourceParamNames) {
									ParamKeyMap keyMap = new ParamKeyMap(sourceParamName, "");
									sourceParamKeyMaps.add(keyMap);
								}
							}
							sourceParamTableViewer.setInput(sourceParamKeyMaps);
							sourceParamTableViewer.refresh();
						}

						if (queryEntity.getQueryType() == QueryType.TARGET) {
							targetParamKeyMaps = new ArrayList<>();
							if (queryEntity.getParamNames() != null) {
								targetParamNames = Arrays.asList(queryEntity.getParamNames().split("\\,"));
								for (String sourceParamName : targetParamNames) {
									ParamKeyMap keyMap = new ParamKeyMap(sourceParamName, "");
									targetParamKeyMaps.add(keyMap);
								}

							}
							targetParamTableViewer.setInput(targetParamKeyMaps);
							targetParamTableViewer.refresh();
						}
						setPageComplete(true);
					}
				} catch (ServiceException e1) {
					e1.printStackTrace();
				}
			}
		});
		for (TestScenario testScenario : allTestScenarios) {
			testScenarioCombobox.add(testScenario.getProjectName());
		}

		Label lblParameterValues = new Label(container, SWT.NONE);
		lblParameterValues.setBounds(5, 109, 120, 15);
		lblParameterValues.setText("Source File Location");

		fileInputText = new Text(container, SWT.BORDER);
		fileInputText.setBounds(131, 106, 342, 21);

		fileBrowser = new Button(container, SWT.NONE);
		fileBrowser.setBounds(479, 104, 84, 25);
		fileBrowser.setText("Browse");
		fileBrowser.addSelectionListener(new Open(JobType.SOURCE));

		Label lblSourceParameter = new Label(container, SWT.NONE);
		lblSourceParameter.setBounds(5, 195, 120, 15);
		lblSourceParameter.setText("Source Parameter");

		sourceParamTable = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		sourceParamTable.setBounds(131, 137, 432, 127);
		sourceParamTable.setHeaderVisible(true);
		sourceParamTable.setLinesVisible(true);

		createTable(sourceParamTable);
		sourceParamTableViewer = new TableViewer(sourceParamTable);
		sourceParamTableViewer.setContentProvider(new ModelContentProvider());
		sourceParamTableViewer.setLabelProvider(new ModelLabelProvider());
		createCellEditors(parent, sourceParamTableViewer, sourceParamTable);
		sourceParamTableViewer.setUseHashlookup(true);
		sourceParamTableViewer.setColumnProperties(COLUMN_NAMES);

		lblTargetFileLocation = new Label(container, SWT.NONE);
		lblTargetFileLocation.setBounds(5, 325, 120, 15);
		lblTargetFileLocation.setText("Target File Location");

		targetFileText = new Text(container, SWT.BORDER);
		targetFileText.setBounds(131, 322, 342, 21);
		targetFileText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				setPageComplete(true);
			}
		});

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setBounds(5, 409, 120, 15);
		lblNewLabel.setText("Target Parameters");

		targetParamTable = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		targetParamTable.setBounds(131, 356, 432, 130);
		targetParamTable.setHeaderVisible(true);
		targetParamTable.setLinesVisible(true);

		createTable(targetParamTable);
		targetParamTableViewer = new TableViewer(targetParamTable);
		targetParamTableViewer.setContentProvider(new ModelContentProvider());
		targetParamTableViewer.setLabelProvider(new ModelLabelProvider());
		createCellEditors(parent, targetParamTableViewer, targetParamTable);
		targetParamTableViewer.setUseHashlookup(true);
		targetParamTableViewer.setColumnProperties(COLUMN_NAMES);

		targetFileBrowseBtn = new Button(container, SWT.NONE);
		targetFileBrowseBtn.setText("Browse");
		targetFileBrowseBtn.setBounds(479, 320, 84, 25);
		targetFileBrowseBtn.addSelectionListener(new Open(JobType.TARGET));

		lblSource = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblSource.setText("SOURCE");
		lblSource.setBounds(5, 79, 558, 15);

		Label lblSource_1 = new Label(container, SWT.NONE);
		lblSource_1.setBounds(5, 66, 55, 15);
		lblSource_1.setText("SOURCE");

		Label lblTarget = new Label(container, SWT.NONE);
		lblTarget.setText("TARGET");
		lblTarget.setBounds(5, 282, 55, 15);

		Label label_2 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_2.setText("SOURCE");
		label_2.setBounds(5, 295, 558, 15);

		setPageComplete(false);
	}

	public static void refreshTables() {
		sourceParamTableViewer.setInput(sourceParamKeyMaps);
		targetParamTableViewer.setInput(targetParamKeyMaps);
	}

	private void createCellEditors(Composite parent, TableViewer tableViewer, Table table) {
		CellEditor[] editors = new CellEditor[COLUMN_NAMES.length];
		TextCellEditor textCellEditor = new TextCellEditor(table, SWT.NULL);
		TextCellEditor textEditor = new TextCellEditor(table, SWT.READ_ONLY);
		editors[0] = textEditor;
		editors[1] = textCellEditor;
		tableViewer.setCellEditors(editors);
		tableViewer.setCellModifier(new CellModifier(tableViewer));
	}

	private void createTable(Table table) {
		for (int i = 0; i < COLUMN_NAMES.length; i++) {
			TableColumn column = new TableColumn(table, SWT.LEFT, i);
			column.setText(COLUMN_NAMES[i]);
			column.setWidth(215);
		}
	}

	class Open implements SelectionListener {
		JobType jobType;

		public Open(JobType target) {
			this.jobType = target;
		}

		public void widgetSelected(SelectionEvent event) {
			FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
			fd.setText("Open");
			fd.setFilterPath("C:/");
			String[] filterExt = { "*.csv", "*.txt", "*.*" };
			fd.setFilterExtensions(filterExt);
			String selected = fd.open();
			if (jobType == JobType.SOURCE)
				fileInputText.setText(selected);
			else
				targetFileText.setText(selected);
			System.out.println(selected);
		}

		public void widgetDefaultSelected(SelectionEvent event) {
		}
	}

	public Text getFileInputText() {
		return fileInputText;
	}

	public static List<ParamKeyMap> getSourceParamKeyMaps() {
		return sourceParamKeyMaps;
	}

	public static List<ParamKeyMap> getTargetParamKeyMaps() {
		return targetParamKeyMaps;
	}

	public Combo getTestScenarioCombobox() {
		return testScenarioCombobox;
	}

	public Text getTargetFileText() {
		return targetFileText;
	}

	public void setTargetFileText(Text targetFileText) {
		this.targetFileText = targetFileText;
	}

}
