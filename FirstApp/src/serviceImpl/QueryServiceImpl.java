package serviceImpl;

import java.util.ArrayList;
import java.util.List;

import dao.ColumnMetaDao;
import dao.QueryDao;
import daoImpl.ColumnMetaDAOImpl;
import daoImpl.QueryDaoImpl;
import entity.ColumnMeta;
import entity.QueryEntity;
import enums.QueryType;
import exceptions.DAOException;
import exceptions.EntityNotPresent;
import exceptions.ReadEntityException;
import exceptions.ServiceException;
import service.QueryService;

public class QueryServiceImpl implements QueryService {
	QueryDao queryDao;
	ColumnMetaDao columnMetaDao;

	@Override
	public void addQuery(Long cId, QueryEntity queryEntity) throws ServiceException {
		try {
			queryDao = new QueryDaoImpl();
			columnMetaDao = new ColumnMetaDAOImpl();
			queryEntity.setQueryColumnMeta(columnMetaDao.getColumnMetaById(cId));
			queryDao.addQuery(queryEntity);
		} catch (DAOException p) {
			throw new ServiceException(p.getMessage());
		}

	}

	@Override
	public List<QueryEntity> getAllQuries() throws ServiceException {
		try {
			queryDao = new QueryDaoImpl();
			return queryDao.getAllQueries();
		} catch (ReadEntityException r) {
			throw new ServiceException();
		}
	}

	@Override
	public QueryEntity getQueryByID(Long qId) throws ServiceException {
		try {
			queryDao = new QueryDaoImpl();
			return queryDao.getQueryByID(qId);
		} catch (EntityNotPresent e) {
			throw new ServiceException();
		}
	}

	@Override
	public QueryEntity updateQueryEntity(Long qId, QueryEntity queryEntity) throws ServiceException {
		try {
			queryDao = new QueryDaoImpl();
			return queryDao.updateQuery(qId, queryEntity);
		} catch (EntityNotPresent e) {
			throw new ServiceException();
		}
	}

	@Override
	public ArrayList<String> getQueryByName() throws ServiceException {
		try {
			ArrayList<String> queryStringList = new ArrayList<String>();
			queryDao = new QueryDaoImpl();
			List<QueryEntity> queriesList = queryDao.getAllQueries();
			for (QueryEntity q : queriesList) {
				queryStringList.add(q.getQueryName());
			}
			return queryStringList;
		} catch (ReadEntityException r) {
			throw new ServiceException();
		}
	}

	@Override
	public List<QueryEntity> getQueryByColumnId(Long cID) throws ServiceException {
		try {
			queryDao = new QueryDaoImpl();
			return queryDao.getQueryByCMId(cID);
		} catch (EntityNotPresent e) {
			throw new ServiceException();
		}

	}

	@Override
	public void addQuery(String projectName, String queryName, String queryMain, String parameterList, String queryType)
			throws ServiceException {
		queryDao = new QueryDaoImpl();
		columnMetaDao = new ColumnMetaDAOImpl();
		ColumnMeta queryColumnMeta;
		try {
			queryColumnMeta = columnMetaDao.getColumnMetaByName(projectName);
			QueryEntity queryEntity = new QueryEntity(queryColumnMeta, QueryType.valueOf(queryType), parameterList,
					queryMain, queryName);
			queryDao.addQuery(queryEntity);
		} catch (DAOException e) {
			throw new ServiceException(e.getMessage());
		}

	}

}
