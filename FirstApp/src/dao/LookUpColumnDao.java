package dao;

import java.util.List;

import entity.LookUpCols;
import exceptions.EntityNotPresent;
import exceptions.PersistException;
import exceptions.ReadEntityException;

public interface LookUpColumnDao {

	public void addLookUpQuery(LookUpCols lookUpCols) throws PersistException;

	public LookUpCols getLookUpQuriebyID(Long qID) throws EntityNotPresent;

	public List<LookUpCols> getAllLookUpQueries() throws ReadEntityException;

	public LookUpCols updateLookUpquery(Long iD, LookUpCols lookUpCols) throws EntityNotPresent;

	public List<LookUpCols> lookUpQueriesByQueryId(Long qiD) throws EntityNotPresent;

	public LookUpCols saveOrUpdateLookUpQuery(LookUpCols lookUpCols) throws EntityNotPresent;

}
