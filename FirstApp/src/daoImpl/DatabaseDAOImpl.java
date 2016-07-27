package daoImpl;

import java.util.ArrayList;
import java.util.List;

import dao.DatabaseDao;
import entity.Database;
import exceptions.EntityAlreadyExists;
import exceptions.EntityNotPresent;
import exceptions.PersistException;
import exceptions.ReadEntityException;

public class DatabaseDAOImpl extends GenericDAOImpl<Database, Long> implements DatabaseDao {

	// Logger logger = Logger.getLogger(getClass());

	@Override
	public void saveDatabse(Database databse) throws PersistException {
		try {
			List<String> dbNames = getAllConnectionNames();
			if (dbNames.contains(databse.getDatabaseName())) {
				throw new EntityAlreadyExists(
						"Database - " + databse.getConnectionName() + " already exists in the databse");
			}
			save(databse);
		} catch (Exception err) {
			err.printStackTrace();
			// logger.error(err);
			throw new PersistException("Could not persist Database Data - " + err.getMessage() + databse);
		}
	}

	@Override
	public Database getDatabaseByid(Long id) throws ReadEntityException {
		try {
			return readById(Database.class, id);
		} catch (Exception err) {
			// logger.error(err);
			throw new ReadEntityException("Could not get Database Data for ID - " + id);
		}
	}

	@Override
	public List<Database> getAllDatabaseinDB() throws ReadEntityException {
		List<Database> databases;
		try {
			databases = readAll("Database.finadAll", Database.class);
		} catch (Exception err) {
			// logger.error(err);
			throw new ReadEntityException("Could not get All Database Information");
		}
		return databases;
	}

	@Override
	public List<String> getAllConnectionNames() throws ReadEntityException {
		List<Database> databases;
		List<String> dbConnectionNames;
		dbConnectionNames = new ArrayList<>();
		try {
			databases = getAllDatabaseinDB();
			for (Database database : databases) {
				dbConnectionNames.add(database.getConnectionName());
			}
		} catch (Exception err) {
			// logger.error(err);
			throw new ReadEntityException("Could not get All Database Information");
		}
		return dbConnectionNames;
	}

	@Override
	public void update(Database database) throws EntityNotPresent {
		try {
			update(Database.class, database.getDbId(), database);
		} catch (EntityNotPresent err) {
			throw new EntityNotPresent("Could not get Update Database Information");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
