package views.wizardPages.executeModule;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import enums.VolumeOfData;

public class MyPageOne extends WizardPage {
	private Composite container;
	private Button btnTestScenario;
	private Button btnTestSuite;
	private Label lblSelectTestScenariosuite;
	private Label lblExecutionInstanceName;
	private Text execInstNameText;
	private Label lblDescription;
	private Text execInstDescText;
	private Combo volumData;

	public MyPageOne() {
		super("Red Runner");
		setTitle("Execution Page");
		setDescription("Execute Test Scenarios and Test Suites");
	}

	@Override
	public void createControl(Composite parent) {
		Point size = getShell().computeSize(611, 739);
		getShell().setSize(size);
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		container.setLayout(layout);
		// required to avoid an error in the system
		setControl(container);

		lblExecutionInstanceName = new Label(container, SWT.NONE);
		lblExecutionInstanceName.setText("Execution Instance Name");
		new Label(container, SWT.NONE);

		execInstNameText = new Text(container, SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_text.widthHint = 171;
		execInstNameText.setLayoutData(gd_text);
		execInstNameText.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!execInstNameText.getText().isEmpty()) {
					setPageComplete(true);
				}
			}

		});

		lblDescription = new Label(container, SWT.NONE);
		lblDescription.setText("Description");
		new Label(container, SWT.NONE);

		execInstDescText = new Text(container, SWT.BORDER);
		GridData gd_text_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text_1.heightHint = 85;
		execInstDescText.setLayoutData(gd_text_1);
		Label label1 = new Label(container, SWT.NONE);
		label1.setText("Run Test Scenario/Suite");
		new Label(container, SWT.NONE);

		btnTestScenario = new Button(container, SWT.RADIO);
		btnTestScenario.setText("Test Scenario");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		btnTestSuite = new Button(container, SWT.RADIO);
		btnTestSuite.setText("Test Suite");

		lblSelectTestScenariosuite = new Label(container, SWT.NONE);
		lblSelectTestScenariosuite.setText("Volume of Data");
		new Label(container, SWT.NONE);

		volumData = new Combo(container, SWT.NONE);
		volumData.setToolTipText("HIGH - >1,000,000\r\nMED - < 500,000\r\nHIGH -< 100,000");
		volumData.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		for (VolumeOfData volume : VolumeOfData.values()) {
			volumData.add(volume.toString());
		}
		setPageComplete(false);
	}

	public Button getBtnTestScenario() {
		return btnTestScenario;
	}

	public Button getBtnTestSuite() {
		return btnTestSuite;
	}

	public Label getLblSelectTestScenariosuite() {
		return lblSelectTestScenariosuite;
	}

	public Label getLblExecutionInstanceName() {
		return lblExecutionInstanceName;
	}

	public Text getExecInstNameText() {
		return execInstNameText;
	}

	public Label getLblDescription() {
		return lblDescription;
	}

	public Text getExecInstDescText() {
		return execInstDescText;
	}

	public Combo getVolumData() {
		return volumData;
	}

}
