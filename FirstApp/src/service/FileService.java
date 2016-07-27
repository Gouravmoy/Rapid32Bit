package service;

import entity.Files;
import exceptions.ServiceException;

public interface FileService {

	public Files getFileForColMeta(Long columnMetaId) throws ServiceException;

}
