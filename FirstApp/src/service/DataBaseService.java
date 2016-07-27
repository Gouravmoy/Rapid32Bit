package service;

import java.util.List;

import entity.Database;
import exceptions.ServiceException;

public interface DataBaseService {

	void addDataBase(String connectionName, String connectionDescription, String dataBaseName, String serverName,
			String userName, String password, String port, String dbType) throws ServiceException;

	public List<Database> getAllDataBase() throws ServiceException;

	public Database getDatabaseById(Long Id);

	public Database getDatabaseByName(String name);

}
