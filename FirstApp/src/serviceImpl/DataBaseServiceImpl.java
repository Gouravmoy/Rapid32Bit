package serviceImpl;

import java.util.List;

import dao.DatabaseDao;
import daoImpl.DatabaseDAOImpl;
import entity.Database;
import enums.DBTypes;
import exceptions.DAOException;
import exceptions.ReadEntityException;
import exceptions.ServiceException;
import service.DataBaseService;

public class DataBaseServiceImpl implements DataBaseService {
	DatabaseDao dataBaseDao;

	public DataBaseServiceImpl() {
		super();
		dataBaseDao = new DatabaseDAOImpl();
	}

	@Override
	public void addDataBase(String connectionName, String connectionDescription, String dataBaseName, String serverName,
			String userName, String password, String port, String dbType) throws ServiceException {
		try {
			Database database = new Database(connectionName, connectionDescription, dataBaseName, serverName, userName,
					password, port, DBTypes.valueOf(dbType));
			dataBaseDao.saveDatabse(database);
		} catch (DAOException d) {
			throw new ServiceException(d.getMessage());
		}

	}

	@Override
	public List<Database> getAllDataBase() throws ServiceException {
		try {
			List<Database> dataBases;
			dataBases = dataBaseDao.getAllDatabaseinDB();
			return dataBases;
		} catch (DAOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Database getDatabaseById(Long Id) {
		try {
			return dataBaseDao.getDatabaseByid(Id);
		} catch (ReadEntityException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Database getDatabaseByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
