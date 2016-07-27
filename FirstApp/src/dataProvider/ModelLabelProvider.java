package dataProvider;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import entity.Column;
import entity.LookUpCols;
import entity.ParamKeyMap;

public class ModelLabelProvider extends LabelProvider implements ITableLabelProvider {

	URL url;
	Image image;
	boolean createImage = true;

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		if (arg0 instanceof Column) {
			if (arg1 == 0) {
				if (createImage) {
					createImage();
				}
				if (((Column) arg0).getUniqueColumn()) {
					return image;
				}
			}
		}
		return null;
	}

	private void createImage() {
		try {
			url = new URL("platform:/plugin/FirstApp/icons/checkbox.png");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File file = new File("check.png");
		try {
			FileUtils.copyURLToFile(url, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		image = new Image(Display.getCurrent(), file.getPath());
		createImage = false;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof LookUpCols) {
			LookUpCols model = (LookUpCols) element;
			switch (columnIndex) {
			case 0:
				return model.getLookUpColName();
			case 1:
				return model.getLookUpQuery();
			default:
				break;
			}
			return "";
		} else if (element instanceof ParamKeyMap) {
			ParamKeyMap model = (ParamKeyMap) element;
			switch (columnIndex) {
			case 0:
				return model.getParamName();
			case 1:
				return model.getParamValue();
			default:
				break;
			}
			return "";
		} else {
			Column model = (Column) element;
			switch (columnIndex) {
			case 0:
				break;
			case 1:
				return model.getColumnName();
			case 2:
				return model.getColumnType().toString();
			case 3:
				return model.getDefaultValue();
			case 4:
				return model.getColumnLength();
			case 5:
				return model.getDecimalLength();
			case 6:
				break;
			}
			return "";
		}

	}
}
