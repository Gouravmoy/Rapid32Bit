package views.wizardPages.workflow;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import entity.TestSuite;

public class TestSuiteWizard extends WizardPage {
	
	private Composite container;
	private Text testSuiteName;
	private Text testSuiteDescription;
	private Button btnDeselectAll;

	public TestSuiteWizard(String pageName,TestSuite testSuite) {
		super("Test Scenario");
		setTitle(pageName);
		setDescription("Create a Test Suite ");
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new GridLayout(3, false));
		
		Label lblTestSuiteName = new Label(container, SWT.NONE);
		lblTestSuiteName.setText("Test Suite Name");
		new Label(container, SWT.NONE);
		
		testSuiteName = new Text(container, SWT.BORDER);
		testSuiteName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTestSuiteDescription = new Label(container, SWT.NONE);
		lblTestSuiteDescription.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblTestSuiteDescription.setText("Test Suite Description");
		new Label(container, SWT.NONE);
		
		testSuiteDescription = new Text(container, SWT.BORDER);
		GridData gd_text_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text_1.heightHint = 65;
		testSuiteDescription.setLayoutData(gd_text_1);
		
		Label lblSelectTestScenarios = new Label(container, SWT.NONE);
		lblSelectTestScenarios.setText("Select test Scenarios");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		btnDeselectAll = new Button(container, SWT.NONE);
		btnDeselectAll.setText("Deselect All");
		setPageComplete(false);
	}

}
