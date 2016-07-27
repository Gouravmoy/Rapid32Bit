package views.wizardPages.workflow;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import entity.QueryEntity;
import enums.QueryType;

public class QueriesWizardPage extends WizardPage {

	private Composite container;
	private Text queryName;
	private Text queryParams;
	private Text mainQuery;
	Combo queryTypeCombo;
	Combo dataCheckTypeCombo;

	Button btnColName;
	Button btnColIndex;

	QueryEntity queryEntity;
	private Text text;

	public QueriesWizardPage(String pageName, QueryEntity entity) {
		super(pageName);
		setTitle(pageName);
		setDescription("Create a Source/Target Queries ");
		queryEntity = entity;
	}

	public QueryEntity getQueryEntity() {
		return queryEntity;
	}

	public void setQueryEntity(QueryEntity queryEntity) {
		this.queryEntity = queryEntity;
		text.setText(queryEntity.getQueryColumnMeta().getColumnMetaName());
	}

	@Override
	public void createControl(Composite parent) {

		container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(null);

		Label lblQueryName = new Label(container, SWT.READ_ONLY);
		lblQueryName.setBounds(5, 8, 67, 15);
		lblQueryName.setText("Query Name");

		queryName = new Text(container, SWT.BORDER);
		queryName.setBounds(171, 5, 398, 21);
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
		Label lblLinkedcolumnMetadata = new Label(container, SWT.NONE);
		lblLinkedcolumnMetadata.setBounds(5, 37, 131, 15);
		lblLinkedcolumnMetadata.setText("LinkedColumn Metadata");

		text = new Text(container, SWT.BORDER);
		text.setBounds(171, 31, 398, 27);
		String columnMetaName=queryEntity.getQueryColumnMeta().getColumnMetaName();
		text.setText(columnMetaName);

		Label lblQueryType = new Label(container, SWT.NONE);
		lblQueryType.setBounds(5, 67, 61, 15);
		lblQueryType.setText("Query Type");

		queryTypeCombo = new Combo(container, SWT.NONE);
		queryTypeCombo.setBounds(171, 63, 398, 23);

		for (QueryType queryType : QueryType.values()) {
			queryTypeCombo.add(queryType.toString());
		}
		queryTypeCombo.setText(queryEntity.getQueryType().toString());
		queryTypeCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				queryEntity.setQueryType(QueryType.valueOf(queryTypeCombo.getText()));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		Label lblQueryParameters = new Label(container, SWT.NONE);
		lblQueryParameters.setBounds(5, 91, 94, 15);
		lblQueryParameters.setText("Query Parameters");

		queryParams = new Text(container, SWT.BORDER);
		queryParams.setBounds(171, 91, 398, 43);

		Label lblMaiunQuery = new Label(container, SWT.NONE);
		lblMaiunQuery.setBounds(5, 139, 62, 15);
		lblMaiunQuery.setText("Main Query");

		mainQuery = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		mainQuery.setBounds(171, 139, 398, 142);
		mainQuery.setTextLimit(20000);

		Button btnClearAll = new Button(container, SWT.NONE);
		btnClearAll.setBounds(171, 391, 78, 25);
		btnClearAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				queryName.setText("");
				queryParams.setText("");
				mainQuery.setText("");
				dataCheckTypeCombo.deselectAll();
			}
		});
		btnClearAll.setText("Reset");

		Label lblCompareDataBy = new Label(container, SWT.NONE);
		lblCompareDataBy.setBounds(5, 303, 131, 15);
		lblCompareDataBy.setText("Compare Data By");

		dataCheckTypeCombo = new Combo(container, SWT.NONE);
		dataCheckTypeCombo.setBounds(171, 300, 398, 23);
		dataCheckTypeCombo.add("Column Name");
		dataCheckTypeCombo.add("Column Index");
		if (queryEntity != null) {
			if (queryEntity.isDataByColNameFlag())
				dataCheckTypeCombo.select(0);
			else
				dataCheckTypeCombo.select(1);
		}
		dataCheckTypeCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selectedScenario = dataCheckTypeCombo.getText();
				if (selectedScenario.equals("Column Name"))
					queryEntity.setDataByColNameFlag(true);
				else
					queryEntity.setDataByColNameFlag(false);
				setPageComplete(true);
			}
		});
		if (queryEntity != null) {
			if (queryEntity.getQueryName() != null)
				setPageComplete(true);
		} else {
			setPageComplete(false);
		}
		bindValues();
	}

	public Text getQueryName() {
		return queryName;
	}

	public Text getQueryParams() {
		return queryParams;
	}

	public Text getMainQuery() {
		return mainQuery;
	}

	public void setQueryName(Text queryName) {
		this.queryName = queryName;
	}

	public void setQueryParams(Text queryParams) {
		this.queryParams = queryParams;
	}

	public void setMainQuery(Text mainQuery) {
		this.mainQuery = mainQuery;
	}

	private void bindValues() {
		DataBindingContext ctx = new DataBindingContext();
		IObservableValue widgetValue = WidgetProperties.text(SWT.Modify).observe(queryName);
		IObservableValue modelValue = BeanProperties.value(QueryEntity.class, "queryName").observe(queryEntity);
		UpdateValueStrategy update = new UpdateValueStrategy();
		ctx.bindValue(widgetValue, modelValue, update, null);

		widgetValue = WidgetProperties.text(SWT.Modify).observe(queryParams);
		modelValue = BeanProperties.value(QueryEntity.class, "paramNames").observe(queryEntity);
		update = new UpdateValueStrategy();
		ctx.bindValue(widgetValue, modelValue, update, null);

		widgetValue = WidgetProperties.text(SWT.Modify).observe(mainQuery);
		modelValue = BeanProperties.value(QueryEntity.class, "mainQuery").observe(queryEntity);
		update = new UpdateValueStrategy();
		ctx.bindValue(widgetValue, modelValue, update, null);

	}
}
