package views;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import dbpackage.CreateDb;
import entity.ExecutionDTO;
import entity.ExecutionStatus;
import entity.ScenarioExecData;
import enums.ExecutionPhase;
import enums.ProjectType;
import extra.ModelProvider;
import jobs.DBCompareJob;
import jobs.FileToFileJob;
import jobs.MyJob;

public class ExecutePart {
	public static final String ID = "views.ExecutePart"; //$NON-NLS-1$
	public static TableViewer viewer;
	List<ExecutionStatus> execList = new ArrayList<>();

	@Inject
	public ExecutePart() {
		CreateDb.createDataBase();
		ExecutionDTO executionDTO = ModelProvider.INSTANCE.getExecutionDTO();
		ExecutionStatus executionStatus = new ExecutionStatus();
		for (ScenarioExecData scenarioExecData : executionDTO.getScenarioExecDatas()) {
			if (scenarioExecData.getScenarioType().equals(ProjectType.File_To_DB.toString())) {
				executionStatus.setScenarioName(scenarioExecData.getScenarioName());
				executionStatus.setExecutionTime("0s");
				executionStatus.setStatus(ExecutionPhase.NOT_STARTED);
				executionStatus.setRecordsProcessed("-");
				execList.add(executionStatus);
				ModelProvider.INSTANCE.setExecutionStatus(execList);
				MyJob job = new MyJob("My Job", ModelProvider.INSTANCE.getExecutionDTO().getScenarioExecDatas().get(0));
				job.schedule();
			} else if (scenarioExecData.getScenarioType().equals(ProjectType.DB_To_DB.toString())) {
				executionStatus.setScenarioName(scenarioExecData.getScenarioName());
				executionStatus.setExecutionTime("0s");
				executionStatus.setStatus(ExecutionPhase.NOT_STARTED);
				executionStatus.setRecordsProcessed("-");
				execList.add(executionStatus);
				ModelProvider.INSTANCE.setExecutionStatus(execList);
				DBCompareJob job = new DBCompareJob("My Job",
						ModelProvider.INSTANCE.getExecutionDTO().getScenarioExecDatas().get(0));
				job.schedule();
			} else if (scenarioExecData.getScenarioType().equals(ProjectType.DB_To_File.toString())) {

			} else if (scenarioExecData.getScenarioType().equals(ProjectType.File_To_File.toString())) {
				executionStatus.setScenarioName(scenarioExecData.getScenarioName());
				executionStatus.setExecutionTime("0s");
				executionStatus.setStatus(ExecutionPhase.NOT_STARTED);
				executionStatus.setRecordsProcessed("-");
				execList.add(executionStatus);
				ModelProvider.INSTANCE.setExecutionStatus(execList);
				FileToFileJob job = new FileToFileJob("File to File Job",
						ModelProvider.INSTANCE.getExecutionDTO().getScenarioExecDatas().get(0));
				job.schedule();
			}
		}
	}

	public TableViewer getViewer() {
		return viewer;
	}

	public void updateTableViewer() {
		if (viewer != null)
			viewer.refresh();
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		createViewer(parent);
	}

	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, viewer);
		final Table table = viewer.getTable();
		table.setBounds(0, 0, 277, 298);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(new ArrayContentProvider());
		// get the content for the viewer, setInput will call getElements in the
		// contentProvider
		// keyValues = new ArrayList<String>();
		viewer.setInput(ModelProvider.INSTANCE.getExecutionStatus());
		// define layout for the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);

	}

	private void createColumns(Composite parent, TableViewer viewer2) {
		String[] titles = { "Scenario Name", "Execution Time", "Execution Status", "Processing Records" };
		TableViewerColumn col = createTableViewerColumn(titles[0], 200, 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ExecutionStatus value = (ExecutionStatus) element;
				return value.getScenarioName();
			}
		});
		col = createTableViewerColumn(titles[1], 200, 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ExecutionStatus value = (ExecutionStatus) element;
				return value.getExecutionTime();
			}
		});
		col = createTableViewerColumn(titles[2], 200, 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ExecutionStatus value = (ExecutionStatus) element;
				return value.getStatus().toString();
			}
		});
		col = createTableViewerColumn(titles[3], 200, 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ExecutionStatus value = (ExecutionStatus) element;
				return value.getRecordsProcessed();
			}
		});
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(175);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
