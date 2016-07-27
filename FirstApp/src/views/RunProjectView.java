package views;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import dao.TestScenarioDao;
import entity.ColumnMeta;
import entity.QueryEntity;
import entity.TestScenario;
import enums.ProjectType;
import exceptions.DAOException;
import exceptions.ServiceException;
import extra.ModelProvider;
import service.ColumnMetaService;
import service.FileService;
import service.QueryService;
import service.TestScenarioService;
import serviceImpl.ColumnMetaServiceImpl;
import serviceImpl.FileServiceImpl;
import serviceImpl.QueryServiceImpl;
import serviceImpl.TestScenarioServiceImpl;

public class RunProjectView {
	private FormToolkit toolkit;
	private Form form;

	public static final String ID = "views.RunProjectView"; //$NON-NLS-1$
	private Text inputFileLoc;
	Shell shell;

	TestScenarioDao projectDao;
	QueryService queryService;
	TestScenarioService projectService;
	ColumnMetaService colMetaService;
	FileService fileService;

	QueryEntity queryEntity;

	Table table;
	TableViewer tableViewer;

	private static String COLUMN_NAMES[] = { "Parameter Name", "Value" };

	@Inject
	public RunProjectView() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		projectService = new TestScenarioServiceImpl();
		colMetaService = new ColumnMetaServiceImpl();
		fileService = new FileServiceImpl();
		queryService = new QueryServiceImpl();
		TestScenario currentProject = ModelProvider.INSTANCE.getSelectedProject();
		List<String> propProject = projectService.getTestScenariosProps(currentProject);

		ColumnMeta currentColMeta = null;

		String projectName = propProject.get(0).split("\\:")[1];
		String projectType = propProject.get(1).split("\\:")[1];
		String colMetaName = propProject.get(2).split("\\:")[1];
		String source = propProject.get(3).split("\\:")[1];
		String target = propProject.get(4).split("\\:")[1];
		String sourceLookup = propProject.get(5).split("\\:")[1];
		String targetLookup = propProject.get(6).split("\\:")[1];
		String fileName = "";

		try {
			currentColMeta = colMetaService.getColMetaByName(colMetaName);
		} catch (DAOException e) {
			e.printStackTrace();
		}

		if (ProjectType.valueOf(projectType) == ProjectType.File_To_DB) {
			try {
				fileName = fileService.getFileForColMeta(currentColMeta.getIdColumnMeta()).getFileName();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			queryEntity = queryService.getQueryByColumnId(currentColMeta.getIdColumnMeta()).get(0);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		List<String> paramNames = Arrays.asList(queryEntity.getParamNames().split("\\,"));

		toolkit = new FormToolkit(parent.getDisplay());
		shell = new Shell(parent.getDisplay());
		form = toolkit.createForm(parent);
		form.getBody().setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
		form.getBody().setLayout(null);
		table = new Table(form.getBody(), SWT.NULL);
		tableViewer = new TableViewer(table);
		table.setLocation(143, 275);
		table.setSize(549, 115);

		for (int i = 0; i < COLUMN_NAMES.length; i++) {
			TableColumn column = new TableColumn(table, SWT.LEFT, i);
			column.setText(COLUMN_NAMES[i]);
			column.setWidth(250);
		}
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		Label lblProjectName = new Label(form.getBody(), SWT.NONE);
		lblProjectName.setBounds(10, 10, 100, 15);
		toolkit.adapt(lblProjectName, true, true);
		lblProjectName.setText("PROJECT NAME");
		// lblProjectName.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

		Label lblNewLabel = new Label(form.getBody(), SWT.NONE);
		lblNewLabel.setText(projectName);
		lblNewLabel.setBounds(167, 10, 172, 15);
		toolkit.adapt(lblNewLabel, true, true);
		lblNewLabel.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

		Label lblProjectType = new Label(form.getBody(), SWT.NONE);
		lblProjectType.setText("PROJECT TYPE");
		lblProjectType.setBounds(10, 47, 100, 15);
		toolkit.adapt(lblProjectType, true, true);
		// lblProjectType.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

		Label label_1 = new Label(form.getBody(), SWT.NONE);
		label_1.setText(projectType);
		label_1.setBounds(167, 47, 172, 15);
		toolkit.adapt(label_1, true, true);
		label_1.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

		Label lblInputFile = new Label(form.getBody(), SWT.NONE);
		lblInputFile.setText("INPUT FILE");
		lblInputFile.setBounds(10, 81, 100, 15);
		toolkit.adapt(lblInputFile, true, true);
		// lblInputFile.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

		Label label_3 = new Label(form.getBody(), SWT.NONE);
		label_3.setText(fileName);
		label_3.setBounds(167, 81, 172, 15);
		toolkit.adapt(label_3, true, true);
		label_3.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

		Label lblColumnMeta = new Label(form.getBody(), SWT.NONE);
		lblColumnMeta.setText("COLUMN META");
		lblColumnMeta.setBounds(10, 116, 100, 15);
		// lblColumnMeta.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));
		toolkit.adapt(lblColumnMeta, true, true);

		Label label_5 = new Label(form.getBody(), SWT.NONE);
		label_5.setText(colMetaName);
		label_5.setBounds(167, 116, 172, 15);
		toolkit.adapt(label_5, true, true);
		label_5.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));
		// lblProjectName.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

		Label lblSource = new Label(form.getBody(), SWT.NONE);
		lblSource.setText("SOURCE");
		lblSource.setBounds(404, 10, 100, 15);
		toolkit.adapt(lblSource, true, true);
		// lblSource.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

		Label label_7 = new Label(form.getBody(), SWT.NONE);
		label_7.setText(source);
		label_7.setBounds(561, 10, 172, 15);
		toolkit.adapt(label_7, true, true);
		label_7.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

		Label lblTarget = new Label(form.getBody(), SWT.NONE);
		lblTarget.setText("TARGET");
		lblTarget.setBounds(404, 47, 100, 15);
		toolkit.adapt(lblTarget, true, true);
		// lblTarget.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

		Label label_9 = new Label(form.getBody(), SWT.NONE);
		label_9.setText(target);
		label_9.setBounds(561, 47, 172, 15);
		toolkit.adapt(label_9, true, true);
		label_9.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

		Label lblSourceLookupDb = new Label(form.getBody(), SWT.NONE);
		lblSourceLookupDb.setText("SOURCE LOOKUP DB");
		lblSourceLookupDb.setBounds(404, 81, 127, 15);
		toolkit.adapt(lblSourceLookupDb, true, true);
		// lblSourceLookupDb.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

		Label label_11 = new Label(form.getBody(), SWT.NONE);
		label_11.setText(sourceLookup);
		label_11.setBounds(561, 81, 172, 15);
		toolkit.adapt(label_11, true, true);
		label_11.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

		Label lblTargetLookupDb = new Label(form.getBody(), SWT.NONE);
		lblTargetLookupDb.setText("TARGET LOOKUP DB");
		lblTargetLookupDb.setBounds(404, 116, 127, 15);
		toolkit.adapt(lblTargetLookupDb, true, true);
		// lblTargetLookupDb.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

		Label label_13 = new Label(form.getBody(), SWT.NONE);
		label_13.setText(targetLookup);
		label_13.setBounds(561, 116, 172, 15);
		toolkit.adapt(label_13, true, true);
		label_13.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

		Label lblInputFileLocation = new Label(form.getBody(), SWT.NONE);
		lblInputFileLocation.setBounds(10, 206, 127, 15);
		toolkit.adapt(lblInputFileLocation, true, true);
		lblInputFileLocation.setText("INPUT FILE LOCATION");
		// lblInputFileLocation.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

		inputFileLoc = new Text(form.getBody(), SWT.BORDER);
		inputFileLoc.setBounds(143, 200, 234, 21);
		toolkit.adapt(inputFileLoc, true, true);

		Button btnBrowse = new Button(form.getBody(), SWT.NONE);
		btnBrowse.setText("Browse");
		btnBrowse.setBounds(383, 198, 75, 25);
		btnBrowse.addSelectionListener(new Open());
		toolkit.adapt(btnBrowse, true, true);

		Label lblQueryParameters = new Label(form.getBody(), SWT.NONE);
		lblQueryParameters.setBounds(10, 254, 127, 15);
		toolkit.adapt(lblQueryParameters, true, true);
		lblQueryParameters.setText("QUERY PARAMETER");

		Button btnRunProject = new Button(form.getBody(), SWT.NONE);
		btnRunProject.setBounds(264, 396, 113, 37);
		toolkit.adapt(btnRunProject, true, true);
		btnRunProject.setText("Run Project");
		form.setBounds(0, 0, 703, 500);
		form.setText("RUN PROJECT");
		// lblQueryParameters.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

	}

	class Open implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			FileDialog fd = new FileDialog(shell, SWT.OPEN);
			fd.setText("Open");
			fd.setFilterPath("C:/");
			String[] filterExt = { "*.txt", "*.doc", ".rtf", "*.*" };
			fd.setFilterExtensions(filterExt);
			String selected = fd.open();
			inputFileLoc.setText(selected);
			System.out.println(selected);
		}

		public void widgetDefaultSelected(SelectionEvent event) {
		}
	}
}
