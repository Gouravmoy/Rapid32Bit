package daoImpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import dao.QueryDao;
import entity.QueryEntity;
import exceptions.DAOException;
import exceptions.EntityNotPresent;
import exceptions.PersistException;
import exceptions.ReadEntityException;

public class QueryDaoImpl extends GenericDAOImpl<QueryEntity, Long> implements QueryDao {

	@Override
	public void addQuery(QueryEntity queryEntity) throws PersistException {
		try {
			save(queryEntity);
		} catch (PersistenceException p) {
			throw new PersistException();
		}
	}

	@Override
	public List<QueryEntity> getAllQueries() throws ReadEntityException {
		try {
			return readAll("QueryEntity.finadAll", QueryEntity.class);
		} catch (Exception e) {
			throw new ReadEntityException();
		}
	}

	@Override
	public QueryEntity getQueryByID(Long qId) throws EntityNotPresent {
		try {
			return readById(QueryEntity.class, qId);
		} catch (Exception e) {
			throw new EntityNotPresent();
		}
	}

	@Override
	public QueryEntity updateQuery(Long qId, QueryEntity queryEntity) throws EntityNotPresent {
		try {
			return update(QueryEntity.class, qId, queryEntity);
		} catch (Exception e) {
			throw new EntityNotPresent();
		}
	}

	@Override
	public List<QueryEntity> getQueryByCMId(Long cId) throws EntityNotPresent {
		try {
			String queryExecute = "FROM QueryEntity q join fetch q.queryColumnMeta where q.queryColumnMeta.idColumnMeta=:arg0";
			Object[] pars = { cId };

			return getByQuery(queryExecute, pars, QueryEntity.class);

		} catch (Exception e) {
			throw new EntityNotPresent();
		}
	}

	@Override
	public List<String> getQueryByName() throws DAOException {
		List<QueryEntity> queryEntities;
		List<String> queryStringList = new ArrayList<>();
		queryEntities = getAllQueries();
		for (QueryEntity queryEntity : queryEntities) {
			queryStringList.add(queryEntity.getQueryName());
		}
		return queryStringList;
	}

	@Override
	public QueryEntity getQueryByName(String queryName) throws DAOException {
		String queryExecute = "FROM QueryEntity q where q.queryName=:arg0";
		Object[] pars = { queryName };
		List<QueryEntity> qList = getByQuery(queryExecute, pars, QueryEntity.class);
		return qList.get(0);
	}

	@Override
	public void update(QueryEntity queryEntity) throws EntityNotPresent {
		try {
			update(QueryEntity.class, queryEntity.getQueryId(), queryEntity);
		} catch (EntityNotPresent err) {
			throw new EntityNotPresent("Could not get Update QueryEntity Information");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
