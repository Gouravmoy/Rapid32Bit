package dao;

import java.util.List;

import entity.Database;
import exceptions.EntityNotPresent;
import exceptions.PersistException;
import exceptions.ReadEntityException;

public interface DatabaseDao {

	public void saveDatabse(Database databse) throws PersistException;

	public Database getDatabaseByid(Long id) throws ReadEntityException;

	public List<Database> getAllDatabaseinDB() throws ReadEntityException;

	public List<String> getAllConnectionNames() throws ReadEntityException;
	
	public void update(Database database) throws EntityNotPresent;

}
