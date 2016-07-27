
package views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import dao.ColumnMetaDao;
import daoImpl.ColumnMetaDAOImpl;
import enums.QueryType;
import exceptions.DAOException;
import exceptions.ReadEntityException;
import exceptions.ServiceException;
import service.QueryService;
import serviceImpl.QueryServiceImpl;

public class QueryForm {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text queryNameText;
	private Text parameterText;
	private Text mainQueryText;
	ColumnMetaDao columnMetaDao;
	QueryService queryService;

	@Inject
	public QueryForm() {
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		columnMetaDao = new ColumnMetaDAOImpl();
		queryService = new QueryServiceImpl();
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		Form frmAddQuery = formToolkit.createForm(parent);
		formToolkit.paintBordersFor(frmAddQuery);
		frmAddQuery.setText("ADD QUERY");
		frmAddQuery.getBody().setLayout(new GridLayout(2, false));

		Label queryNameLabel = formToolkit.createLabel(frmAddQuery.getBody(), "Query Name", SWT.NONE);

		queryNameText = formToolkit.createText(frmAddQuery.getBody(), "", SWT.NULL);
		GridData gd_queryNameText = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_queryNameText.widthHint = 320;
		queryNameText.setLayoutData(gd_queryNameText);
		queryNameText.setText("");

		Label lblNewLabel = formToolkit.createLabel(frmAddQuery.getBody(), "ColumnMeta", SWT.NONE);

		ComboViewer comboViewer_1 = new ComboViewer(frmAddQuery.getBody(), SWT.NONE);
		Combo projectCombo = comboViewer_1.getCombo();
		GridData gd_projectCombo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_projectCombo.widthHint = 319;
		projectCombo.setLayoutData(gd_projectCombo);
		formToolkit.paintBordersFor(projectCombo);

		Label lblQueryType = formToolkit.createLabel(frmAddQuery.getBody(), "Query Type", SWT.NONE);

		ComboViewer comboViewer = new ComboViewer(frmAddQuery.getBody(), SWT.NONE);
		Combo queryTypeCombo = comboViewer.getCombo();
		GridData gd_queryTypeCombo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_queryTypeCombo.widthHint = 344;
		queryTypeCombo.setLayoutData(gd_queryTypeCombo);
		queryTypeCombo.setItems(new String[] { QueryType.SOURCE.toString(), QueryType.TARGET.toString() });
		formToolkit.paintBordersFor(queryTypeCombo);

		Label lblQueryParameters = formToolkit.createLabel(frmAddQuery.getBody(), "Query Parameters", SWT.NONE);

		parameterText = formToolkit.createText(frmAddQuery.getBody(), "New Text", SWT.NONE);
		GridData gd_parameterText = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_parameterText.heightHint = 41;
		gd_parameterText.widthHint = 542;
		parameterText.setLayoutData(gd_parameterText);
		parameterText.setText("");

		Label lblMainQuery = formToolkit.createLabel(frmAddQuery.getBody(), "Main Query", SWT.NONE);
		lblMainQuery.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));

		mainQueryText = formToolkit.createText(frmAddQuery.getBody(), "New Text",
				SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		GridData gd_mainQueryText = new GridData(SWT.LEFT, SWT.FILL, true, true, 1, 1);
		gd_mainQueryText.widthHint = 550;
		mainQueryText.setLayoutData(gd_mainQueryText);
		mainQueryText.setText("");
		Label label = new Label(frmAddQuery.getBody(), SWT.NONE);
		formToolkit.adapt(label, true, true);

		Button btnEnter = formToolkit.createButton(frmAddQuery.getBody(), "Enter", SWT.NONE);
		btnEnter.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

		btnEnter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					queryService.addQuery(projectCombo.getText(), queryNameText.getText(), mainQueryText.getText(),
							parameterText.getText(), queryTypeCombo.getText());
					btnEnter.setEnabled(false);
					//TreeViewPart.createQueryEntityNodes();
					WorkBenchTreesView.createMetaDataTree();
				} catch (ServiceException | DAOException e1) {
					e1.printStackTrace();
				}
			}
		});

		try {
			for (String project : columnMetaDao.getColumnMetaNames()) {
				projectCombo.add(project);
			}
		} catch (ReadEntityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}