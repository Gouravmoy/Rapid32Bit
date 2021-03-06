package handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class AddTestScenarioHandler {
	@Execute
	public void execute(EPartService partService, MApplication application, EModelService modelService) {
		MPart partDy = MBasicFactory.INSTANCE.createPart();
		partDy.setContributionURI("bundleclass://FirstApp/views.ProjectView");
		MPart oldPart = partService.findPart("firstapp.part.playground");
		MPartStack parent = (MPartStack) modelService.getContainer(oldPart);
		partDy.setElementId("firstapp.part.playground");
		partDy.setContainerData("60");
		partDy.setIconURI("platform:/plugin/FirstApp/src/icons/test.png");
		partDy.setLabel("ADD TEST SCENARIO");
		partDy.setCloseable(true);
		parent.getChildren().add(partDy);
		parent.setSelectedElement(partDy);
	}
}
