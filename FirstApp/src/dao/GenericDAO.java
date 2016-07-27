package dao;

import java.io.Serializable;
import java.util.List;

import exceptions.EntityNotPresent;

public interface GenericDAO<T, ID extends Serializable> {

	public T save(T t);

	public void batchSaveDAO(List<T> t);

	public List<T> readAll(String namedQueryName, @SuppressWarnings("rawtypes") Class clazz);

	public T readById(@SuppressWarnings("rawtypes") Class clazz, ID id);

	public T update(@SuppressWarnings("rawtypes") Class clazz, ID id, T t) throws EntityNotPresent;

	public void delete(@SuppressWarnings("rawtypes") Class clazz, ID removeId);

	public boolean isEntityExists(@SuppressWarnings("rawtypes") Class clazz, ID id);

	T getFirstRecord(Class<?> clazz);

	List<T> getByQuery(String queryExecute, Object[] pars, @SuppressWarnings("rawtypes") Class clazz);

	public T saveOrUpdate(@SuppressWarnings("rawtypes") Class clazz, T t);

	public void saveOrUpdateBatch(List<T> t);

}
