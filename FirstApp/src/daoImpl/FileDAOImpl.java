package daoImpl;

import java.util.ArrayList;
import java.util.List;

import dao.FileDAO;
import entity.Files;
import entity.QueryEntity;
import exceptions.DAOException;
import exceptions.EntityAlreadyExists;
import exceptions.EntityNotPresent;
import exceptions.ReadEntityException;

public class FileDAOImpl extends GenericDAOImpl<Files, Long> implements FileDAO {
	// static final Logger logger = Logger.getLogger(FileDAOImpl.class);

	@Override
	public void saveFile(Files file) throws DAOException {
		List<String> fileNames;
		try {
			fileNames = getAllFilesnames();
			if (fileNames.contains(file.getFileName())) {
				throw new EntityAlreadyExists("File with Name - " + file.getFileName() + " Already Exists");
			}
			save(file);
		} catch (ReadEntityException | EntityAlreadyExists e) {
			// logger.error(e);
			throw new DAOException("Could not Persist File Data - " + e.getMessage(), e);
		}
	}

	@Override
	public Files getFilesById(Long id) throws DAOException {
		try {
			return readById(Files.class, id);
		} catch (Exception err) {
			// logger.error(err);
			throw new DAOException("Could not get Files Data for ID - " + id);
		}
	}

	@Override
	public List<Files> getAllFiles() throws DAOException {
		List<Files> files;
		try {
			files = readAll("Files.finadAll", Files.class);
		} catch (Exception err) {
			// logger.error(err);
			throw new DAOException("Could not get All Files Information");
		}
		return files;
	}

	@Override
	public void removeFiles(long id) throws DAOException {
		try {
			delete(Files.class, id);
		} catch (Exception err) {
			// logger.error(err);
			throw new DAOException("File Could not be Removed", err);
		}
	}

	@Override
	public List<String> getAllFilesnames() throws DAOException {
		List<Files> files;
		List<String> fileNames;
		fileNames = new ArrayList<>();
		try {
			files = getAllFiles();
			for (Files file : files) {
				fileNames.add(file.getFileName());
			}
		} catch (Exception err) {
			// logger.error(err);
			throw new ReadEntityException("Could not get All Files Information");
		}
		return fileNames;
	}

	@Override
	public void update(Files file) throws EntityNotPresent {
		try {
			update(Files.class, file.getFileId(), file);
		} catch (EntityNotPresent err) {
			throw new EntityNotPresent("Could not get Update Files Information");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<Files> getFilesForColumnMeta(Long columnMetaId) throws DAOException {
		try {
			String queryExecute = "FROM Files f join fetch f.fileColumnMeta where f.fileColumnMeta.idColumnMeta=:arg0";
			Object[] pars = { columnMetaId };
			return getByQuery(queryExecute, pars, QueryEntity.class);
		} catch (Exception e) {
			throw new EntityNotPresent();
		}
	}
}
