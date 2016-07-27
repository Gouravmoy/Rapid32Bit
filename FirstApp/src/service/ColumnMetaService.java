package service;

import entity.ColumnMeta;
import exceptions.DAOException;

public interface ColumnMetaService {
	public ColumnMeta getColMetaByName(String colMetaName) throws DAOException;
}
