package serviceImpl;

import java.util.ArrayList;
import java.util.List;

import dao.ColumnDao;
import daoImpl.ColumnDaoImpl;
import entity.Column;
import exceptions.EntityNotPresent;
import exceptions.ServiceException;
import service.ColumnService;

public class ColumnServiceImpl implements ColumnService {

	ColumnDao columnDao = new ColumnDaoImpl();

	@Override
	public List<Column> getColummsByColumnMetaId(Long id) throws ServiceException {
		try {
			return columnDao.getColumnByCMId(id);
		} catch (EntityNotPresent r) {
			throw new ServiceException();
		}
	}

	@Override
	public List<String> getUniqueColumnsOfColumnMeta(Long id) {
		List<String> uniqueColumns = new ArrayList<>();
		try {
			for (Column column : columnDao.getColumnByCMId(id)) {
				if (column.getUniqueColumn()) {
					uniqueColumns.add(column.getColumnName());
				}
			}
		} catch (EntityNotPresent e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uniqueColumns;
	}

}
