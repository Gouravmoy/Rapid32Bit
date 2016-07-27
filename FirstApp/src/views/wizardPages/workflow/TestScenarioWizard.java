package views.wizardPages.workflow;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import dao.ColumnMetaDao;
import dao.DatabaseDao;
import dao.TestScenarioDao;
import daoImpl.ColumnMetaDAOImpl;
import daoImpl.DatabaseDAOImpl;
import daoImpl.TestScenarioDaoImpl;
import entity.ColumnMeta;
import entity.Database;
import entity.TestScenario;
import enums.ProjectType;
import exceptions.DAOException;
import exceptions.ReadEntityException;

public class TestScenarioWizard extends WizardPage {

	private Composite container;
	private Text testScenarioName;
	private Text scenarioDesc;

	TestScenarioDao testScenarioDao;
	DatabaseDao databaseDao;
	ColumnMetaDao columnMetaDao;
	List<Database> databaseList = null;
	List<ColumnMeta> colMetaList = null;
	Combo type;

	public TestScenarioWizard(String pageName, TestScenario testScenario) {
		super("Test Scenario");
		setTitle(pageName);
		setDescription("Create a File Metadata ");
	}

	public Text getTestScenarioName() {
		return testScenarioName;
	}

	public void setTestScenarioName(Text testScenarioName) {
		this.testScenarioName = testScenarioName;
	}

	public Text getScenarioDesc() {
		return scenarioDesc;
	}

	public void setScenarioDesc(Text scenarioDesc) {
		this.scenarioDesc = scenarioDesc;
	}

	public void setType(Combo type) {
		this.type = type;
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(null);

		testScenarioDao = new TestScenarioDaoImpl();
		databaseDao = new DatabaseDAOImpl();
		columnMetaDao = new ColumnMetaDAOImpl();

		try {
			databaseList = databaseDao.getAllDatabaseinDB();
			colMetaList = columnMetaDao.getAllColumnMetas();
		} catch (ReadEntityException e) {
			e.printStackTrace();
		} catch (DAOException e1) {
			e1.printStackTrace();
		}

		Label lblTestScenarioName = new Label(container, SWT.NONE);
		lblTestScenarioName.setBounds(5, 8, 105, 15);
		lblTestScenarioName.setText("Test Scenario Name");

		testScenarioName = new Text(container, SWT.BORDER);
		testScenarioName.setBounds(167, 5, 402, 21);
		testScenarioName.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!testScenarioName.getText().isEmpty()) {
					setPageComplete(true);
				}
			}

		});

		Label lblTestScenarioDescription = new Label(container, SWT.NONE);
		lblTestScenarioDescription.setBounds(5, 31, 133, 15);
		lblTestScenarioDescription.setText("Test Scenario Description");

		scenarioDesc = new Text(container, SWT.BORDER);
		scenarioDesc.setBounds(167, 31, 402, 53);

		Label lblProjectType = new Label(container, SWT.NONE);
		lblProjectType.setBounds(5, 93, 66, 15);
		lblProjectType.setText("Project Type");

		type = new Combo(container, SWT.NONE);
		type.setBounds(167, 89, 402, 23);

		for (ProjectType projectType : ProjectType.values()) {
			type.add(projectType.toString());
		}

		Button btnClearAll = new Button(container, SWT.NONE);
		btnClearAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				testScenarioName.setText("");
				scenarioDesc.setText("");
				type.deselectAll();
			}
		});
		btnClearAll.setBounds(167, 118, 75, 25);
		btnClearAll.setText("Reset");
		setPageComplete(false);
		if (isPageComplete()) {
			// to be implemented
		}

	}

	public Combo getType() {
		return type;
	}
}
