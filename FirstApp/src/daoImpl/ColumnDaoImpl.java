package daoImpl;

import java.util.List;

import dao.ColumnDao;
import entity.Column;
import exceptions.EntityNotPresent;
import exceptions.PersistException;

public class ColumnDaoImpl extends GenericDAOImpl<Column, Long> implements ColumnDao {

	@Override
	public void saveColumn(Column column) throws PersistException {
		try {
			save(column);
		} catch (Exception e) {
			throw new PersistException("Unable add Column");
		}

	}

	@Override
	public void batchSave(List<Column> columnList) {
		batchSaveDAO(columnList);
	}

	@Override
	public List<Column> getAllColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Column> getColumnByCMId(Long id) throws EntityNotPresent {
		try {
			String queryExecute = "SELECT q FROM Column q where q.columnMeta.idColumnMeta=:arg0";
			Object[] pars = { id };
			return getByQuery(queryExecute, pars, Column.class);
		} catch (Exception e) {
			throw new EntityNotPresent("Column meta not available");
		}
	}

	@Override
	public void updateBatch(List<Column> columnList) {
		saveOrUpdateBatch(columnList);
	}

}
