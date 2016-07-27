
package views;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;

import extra.ModelProvider;

public class PropViewer {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	public static TableViewer viewer;
	public static List<String> keyValues;

	public TableViewer getViewer() {
		return viewer;
	}

	@Inject
	public PropViewer() {

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
		viewer.setInput(ModelProvider.INSTANCE.getKeyValues());
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
		String[] titles = { "Property", "Values" };
		TableViewerColumn col = createTableViewerColumn(titles[0], 200, 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String value = (String) element;
				return value.split("\\:")[0];
			}
		});
		col = createTableViewerColumn(titles[1], 200, 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String value = (String) element;
				try {
					value = value.split("\\:")[1];
				} catch (Exception err) {
					err.printStackTrace();
				}
				return value;
			}
		});
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(140);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}
}