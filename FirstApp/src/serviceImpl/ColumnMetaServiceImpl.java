package serviceImpl;

import java.util.List;

import dao.ColumnMetaDao;
import daoImpl.ColumnMetaDAOImpl;
import entity.ColumnMeta;
import exceptions.DAOException;
import service.ColumnMetaService;

public class ColumnMetaServiceImpl implements ColumnMetaService {

	ColumnMetaDao colMetaDao = new ColumnMetaDAOImpl();

	@Override
	public ColumnMeta getColMetaByName(String colMetaName) throws DAOException {
		try {
			List<ColumnMeta> colMetas = colMetaDao.getAllColumnMetas();
			for (ColumnMeta columnMeta : colMetas) {
				if (columnMeta.getColumnMetaName().equals(colMetaName))
					return columnMeta;
			}
		} catch (DAOException e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		}
		return null;
	}

}
