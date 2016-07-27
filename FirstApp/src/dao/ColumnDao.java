package dao;

import java.util.List;

import entity.Column;
import exceptions.EntityNotPresent;
import exceptions.PersistException;

public interface ColumnDao {

	public void saveColumn(Column column) throws PersistException;

	public List<Column> getAllColumns();

	public List<Column> getColumnByCMId(Long id) throws EntityNotPresent;

	public void batchSave(List<Column> columnList);

	public void updateBatch(List<Column> columnList);

}
