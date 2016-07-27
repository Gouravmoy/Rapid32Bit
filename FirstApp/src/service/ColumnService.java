package service;

import java.util.List;

import entity.Column;
import exceptions.ServiceException;

public interface ColumnService {

	public List<Column> getColummsByColumnMetaId(Long id) throws ServiceException;

	public List<String> getUniqueColumnsOfColumnMeta(Long id);

}
