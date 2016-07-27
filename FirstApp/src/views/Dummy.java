
package views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;

public class Dummy {
	@Inject
	public Dummy() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(null);
		
		Label lblDateLable = new Label(parent, SWT.NONE);
		lblDateLable.setBounds(0, 0, 448, 298);
		lblDateLable.setText("Date Lable");

	}

}