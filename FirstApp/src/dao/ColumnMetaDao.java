package dao;

import java.util.List;

import entity.ColumnMeta;
import exceptions.DAOException;
import exceptions.EntityNotPresent;
import exceptions.ReadEntityException;

public interface ColumnMetaDao {

	public void saveColumnMeta(ColumnMeta columnMeta) throws DAOException;

	public ColumnMeta getColumnMetaById(Long id) throws DAOException;

	public List<ColumnMeta> getAllColumnMetas() throws DAOException;

	public void removeColumnMeta(Long id) throws DAOException;

	public List<String> getColumnMetaNames() throws ReadEntityException;
	
	public ColumnMeta getColumnMetaByName(String name) throws DAOException;
	
	public void update(ColumnMeta columnMeta) throws EntityNotPresent;
}
