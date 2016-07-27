package serviceImpl;

import java.util.ArrayList;
import java.util.List;

import dao.LookUpColumnDao;
import daoImpl.LookUpQueryDaoImpl;
import entity.LookUpCols;
import exceptions.EntityNotPresent;
import exceptions.PersistException;
import exceptions.ServiceException;
import service.LookUpQueryService;

public class LookUpQueryServiceImpl implements LookUpQueryService {

	LookUpColumnDao lookUpDao;

	public LookUpQueryServiceImpl() {
		super();
		this.lookUpDao = new LookUpQueryDaoImpl();
	}

	@Override
	public void addQuery(LookUpCols lookUpCols) throws ServiceException {
		try {
			lookUpDao.addLookUpQuery(lookUpCols);
		} catch (PersistException e) {
			throw new ServiceException();
		}
	}

	@Override
	public List<LookUpCols> getLookUpColsByQueryId(Long Id) throws ServiceException {
		try {
			return lookUpDao.lookUpQueriesByQueryId(Id);
		} catch (EntityNotPresent e) {
			throw new ServiceException();
		}
	}

	@Override
	public ArrayList<String> getQueryNameByColumnId(Long Id) throws ServiceException {
		ArrayList<String> returnList = new ArrayList<String>();
		try {
			for (LookUpCols columnName : lookUpDao.lookUpQueriesByQueryId(Id)) {
				returnList.add(columnName.getLookUpColName());
			}
		} catch (EntityNotPresent e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnList;
	}

	@Override
	public LookUpCols updateQueryEntity(Long qId, LookUpCols lookUpCols) throws ServiceException {
		return null;
	}
}
