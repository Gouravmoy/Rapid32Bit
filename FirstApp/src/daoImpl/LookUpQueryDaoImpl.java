package daoImpl;

import java.util.List;

import dao.LookUpColumnDao;
import entity.LookUpCols;
import exceptions.EntityNotPresent;
import exceptions.PersistException;
import exceptions.ReadEntityException;

public class LookUpQueryDaoImpl extends GenericDAOImpl<LookUpCols, Long> implements LookUpColumnDao {

	@Override
	public void addLookUpQuery(LookUpCols lookUpCols) throws PersistException {
		try {
			save(lookUpCols);
		} catch (Exception e) {
			throw new PersistException(e.getMessage(), e);
		}
	}

	@Override
	public LookUpCols getLookUpQuriebyID(Long qID) throws EntityNotPresent {
		try {
			return readById(LookUpCols.class, qID);
		} catch (Exception c) {
			throw new EntityNotPresent();
		}

	}

	@Override
	public List<LookUpCols> getAllLookUpQueries() throws ReadEntityException {
		try {
			return readAll("LookUpCols.finadAll", LookUpCols.class);
		} catch (Exception e) {
			throw new ReadEntityException();
		}
	}

	@Override
	public LookUpCols updateLookUpquery(Long id, LookUpCols lookUpCols) throws EntityNotPresent {
		try {
			return update(LookUpCols.class, id, lookUpCols);
		} catch (EntityNotPresent e) {
			throw new EntityNotPresent();
		}
	}

	@Override
	public List<LookUpCols> lookUpQueriesByQueryId(Long qiD) throws EntityNotPresent {
		try {
			String queryExecute = "SELECT q FROM LookUpCols q where q.query.queryId=:arg0";
			Object[] pars = { qiD };
			return getByQuery(queryExecute, pars, LookUpCols.class);
		} catch (Exception e) {
			throw new EntityNotPresent();
		}
	}

	@Override
	public LookUpCols saveOrUpdateLookUpQuery(LookUpCols lookUpCols) throws EntityNotPresent {
		return saveOrUpdate(LookUpCols.class, lookUpCols);
	}


}
