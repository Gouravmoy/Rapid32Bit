package dataProvider;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ModelContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object arg0) {
		if (arg0 instanceof List) {
			@SuppressWarnings("rawtypes")
			List models = (List) arg0;
			return models.toArray();
		}
		return new Object[0];
	}
}
