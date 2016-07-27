package dbpackage;

import java.io.File;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class CreateDb {
	public static DB sourceMapDB;
	public static DB targetMapDB;
	public static DB compareMapDB;

	public static void createDataBase() {
		// myDb = DBMaker.newMemoryDB().make();
		sourceMapDB = DBMaker.newFileDB(new File("Source")).deleteFilesAfterClose().make();
		targetMapDB = DBMaker.newFileDB(new File("Target")).deleteFilesAfterClose().make();
		compareMapDB = DBMaker.newFileDB(new File("Compare")).deleteFilesAfterClose().make();
	}

	public static void closeDataBase() {
		sourceMapDB.close();
		targetMapDB.close();
		compareMapDB.close();
	}
}
