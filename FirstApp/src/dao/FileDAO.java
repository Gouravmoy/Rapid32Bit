package dao;

import java.util.List;

import entity.Files;
import exceptions.DAOException;
import exceptions.EntityNotPresent;

public interface FileDAO {

	public void saveFile(Files file) throws DAOException;

	public Files getFilesById(Long id) throws DAOException;

	public List<Files> getAllFiles() throws DAOException;

	public void removeFiles(long id) throws DAOException;

	public List<String> getAllFilesnames() throws DAOException;

	public void update(Files file) throws EntityNotPresent;

	public List<Files> getFilesForColumnMeta(Long columnMetaId) throws DAOException;

}
