package daoImpl;

import java.util.ArrayList;
import java.util.List;

import dao.ColumnMetaDao;
import entity.ColumnMeta;
import exceptions.DAOException;
import exceptions.EntityNotPresent;
import exceptions.ReadEntityException;

public class ColumnMetaDAOImpl extends GenericDAOImpl<ColumnMeta, Long> implements ColumnMetaDao {
	//static final Logger logger = Logger.getLogger(ColumnMetaDAOImpl.class);

	@Override
	public void saveColumnMeta(ColumnMeta columnMeta) throws DAOException {
		/*
		 * colMetanames = getColumnMetaNames(); if
		 * (colMetanames.contains(columnMeta.getColumnMetaName())) { throw new
		 * EntityAlreadyExists("Column Meta wit Name - " +
		 * columnMeta.getColumnMetaName() + " Already Exists"); }
		 */
		save(columnMeta);

	}

	@Override
	public ColumnMeta getColumnMetaById(Long id) throws DAOException {
		try {
			return readById(ColumnMeta.class, id);
		} catch (Exception err) {
			//logger.error(err);
			throw new DAOException("Could not get ColumnMeta Data for ID - " + id);
		}
	}

	@Override
	public List<ColumnMeta> getAllColumnMetas() throws DAOException {
		List<ColumnMeta> columnMetas;
		try {
			columnMetas = readAll("ColumnMeta.finadAll", ColumnMeta.class);
		} catch (Exception err) {
			err.printStackTrace();
			//logger.error(err);
			throw new DAOException("Could not get All ColumnMeta Information");
		}
		return columnMetas;
	}

	@Override
	public void removeColumnMeta(Long id) throws DAOException {
		try {
			delete(ColumnMeta.class, id);
		} catch (Exception err) {
			//logger.error(err);
			throw new DAOException("Column Meta Could not be Removed", err);
		}
	}

	@Override
	public List<String> getColumnMetaNames() throws ReadEntityException {
		List<ColumnMeta> columnMetas;
		List<String> colMetaNames;
		colMetaNames = new ArrayList<>();
		try {
			columnMetas = getAllColumnMetas();
			for (ColumnMeta columnMeta : columnMetas) {
				colMetaNames.add(columnMeta.getColumnMetaName());
			}
		} catch (Exception err) {
			//logger.error(err);
			throw new ReadEntityException("Could not get All ColMeta Information");
		}
		return colMetaNames;
	}

	@Override
	public ColumnMeta getColumnMetaByName(String name) throws DAOException {
		List<ColumnMeta> columnMetas;
		columnMetas = getAllColumnMetas();
		for (ColumnMeta columnMeta : columnMetas) {
			if (columnMeta.getColumnMetaName() != null) {
				if (columnMeta.getColumnMetaName().equals(name)) {
					return columnMeta;
				}
			}
		}
		return new ColumnMeta();
	}

	@Override
	public void update(ColumnMeta columnMeta) throws EntityNotPresent {
		try {
			update(ColumnMeta.class, columnMeta.getIdColumnMeta(), columnMeta);
		} catch (EntityNotPresent err) {
			throw new EntityNotPresent("Could not get Update ColumnMeta Information");
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
