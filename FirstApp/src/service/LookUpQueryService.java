package service;

import java.util.ArrayList;
import java.util.List;

import entity.LookUpCols;
import exceptions.ServiceException;

public interface LookUpQueryService {

	void addQuery(LookUpCols lookUpCols) throws ServiceException;

	List<LookUpCols> getLookUpColsByQueryId(Long Id) throws ServiceException;

	ArrayList<String> getQueryNameByColumnId(Long Id) throws ServiceException;

	LookUpCols updateQueryEntity(Long qId, LookUpCols lookUpCols)
			throws ServiceException;

}
