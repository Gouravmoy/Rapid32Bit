package model;

import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import entity.Database;

public class DatabaseTreeModel implements TreeModel {

	List<Database> databases;

	public DatabaseTreeModel(List<Database> allDatabaseinDB) {
		this.databases = allDatabaseinDB;
	}

	@Override
	public void addTreeModelListener(TreeModelListener arg0) {

	}

	@Override
	public Object getChild(Object arg0, int arg1) {
		return databases.get(arg1).getDatabaseName();
	}

	@Override
	public int getChildCount(Object arg0) {
		return databases.size();
	}

	@Override
	public int getIndexOfChild(Object arg0, Object arg1) {
		return 0;
	}

	@Override
	public Object getRoot() {
		return "Databases";
	}

	@Override
	public boolean isLeaf(Object arg0) {
		return !arg0.equals("Databases");
	}

	@Override
	public void removeTreeModelListener(TreeModelListener arg0) {

	}

	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) {

	}

}
