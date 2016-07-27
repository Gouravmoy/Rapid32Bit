package serviceImpl;

import dao.FileDAO;
import daoImpl.FileDAOImpl;
import entity.Files;
import exceptions.DAOException;
import exceptions.ServiceException;
import service.FileService;

public class FileServiceImpl implements FileService {

	FileDAO fleDao = new FileDAOImpl();

	public Files getFileForColMeta(Long columnMetaId) throws ServiceException {
		try {
			if (fleDao.getFilesForColumnMeta(columnMetaId).size() > 0) {
				return fleDao.getFilesForColumnMeta(columnMetaId).get(0);
			} else {
				return null;
			}

		} catch (DAOException e) {
			throw new ServiceException();
		}

	}

}
