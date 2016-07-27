package listners;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.eclipse.swt.widgets.Display;

import entity.ColumnMeta;
import entity.Database;
import entity.Files;
import entity.LookUpCols;
import entity.TestScenario;
import entity.TestSuite;
import entity.QueryEntity;
import extra.ModelProvider;
import util.MainUtil;
import views.PropViewer;

public class TreeSelectionListner implements TreeSelectionListener {

	JTree tree;
	MainUtil mainUtil;

	public TreeSelectionListner(JTree tree) {
		super();
		this.tree = tree;
		mainUtil = new MainUtil();
	}

	@Override
	public void valueChanged(TreeSelectionEvent event) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (node == null)
			return;
		if (node.getUserObject() instanceof Database) {
			Database nodeInfo = (Database) node.getUserObject();
			ModelProvider.INSTANCE.setKeyValues(mainUtil.getDBProps(nodeInfo));
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					PropViewer.viewer.setInput(ModelProvider.INSTANCE.getKeyValues());

				}
			});
		} else if (node.getUserObject() instanceof Files) {
			Files nodeInfo = (Files) node.getUserObject();
			ModelProvider.INSTANCE.setKeyValues(mainUtil.getFilesProp(nodeInfo));
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					PropViewer.viewer.setInput(ModelProvider.INSTANCE.getKeyValues());

				}
			});
		} else if (node.getUserObject() instanceof TestScenario) {
			TestScenario nodeInfo = (TestScenario) node.getUserObject();
			ModelProvider.INSTANCE.setKeyValues(mainUtil.getProjectProps(nodeInfo));
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					PropViewer.viewer.setInput(ModelProvider.INSTANCE.getKeyValues());

				}
			});
		} else if (node.getUserObject() instanceof QueryEntity) {
			QueryEntity nodeInfo = (QueryEntity) node.getUserObject();
			ModelProvider.INSTANCE.setKeyValues(mainUtil.getQueryEntityProps(nodeInfo));
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					PropViewer.viewer.setInput(ModelProvider.INSTANCE.getKeyValues());

				}
			});
		} else if (node.getUserObject() instanceof ColumnMeta) {
			ColumnMeta nodeInfo = (ColumnMeta) node.getUserObject();
			ModelProvider.INSTANCE.setKeyValues(mainUtil.getColumnMetaProps(nodeInfo));
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					PropViewer.viewer.setInput(ModelProvider.INSTANCE.getKeyValues());

				}
			});
		} else if (node.getUserObject() instanceof LookUpCols) {
			LookUpCols nodeInfo = (LookUpCols) node.getUserObject();
			ModelProvider.INSTANCE.setKeyValues(mainUtil.getLookUpColsProps(nodeInfo));
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					PropViewer.viewer.setInput(ModelProvider.INSTANCE.getKeyValues());

				}
			});
		} else if (node.getUserObject() instanceof TestSuite) {
			TestSuite nodeInfo = (TestSuite) node.getUserObject();
			ModelProvider.INSTANCE.setKeyValues(mainUtil.getSuiteProps(nodeInfo));
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					PropViewer.viewer.setInput(ModelProvider.INSTANCE.getKeyValues());
				}
			});
		}
	}
}
