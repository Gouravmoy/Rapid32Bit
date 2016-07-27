package handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;

public class NewWindow {
	
	@ Execute
	public void execute(MApplication application) {
	MWindow mWindow = MBasicFactory.INSTANCE.createTrimmedWindow();
	mWindow.setHeight(200);
	mWindow.setWidth(400);
	mWindow.getChildren().add(MBasicFactory.INSTANCE.createPart());
	application.getChildren().add(mWindow);
	}

}
