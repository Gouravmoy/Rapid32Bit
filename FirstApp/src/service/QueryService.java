package service;

import java.util.ArrayList;
import java.util.List;

import entity.QueryEntity;
import exceptions.ServiceException;

public interface QueryService {

	void addQuery(Long cId, QueryEntity queryEntity) throws ServiceException;
	
	List<QueryEntity> getAllQuries() throws ServiceException;

	QueryEntity getQueryByID(Long qId) throws ServiceException;

	QueryEntity updateQueryEntity(Long qId, QueryEntity queryEntity) throws ServiceException;

	ArrayList<String> getQueryByName() throws ServiceException;

	List<QueryEntity> getQueryByColumnId(Long cID) throws ServiceException;

	void addQuery(String projectName,String queryName,String queryMain, String parameterList, String queryType) throws ServiceException;

}
