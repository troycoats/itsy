package com.itsy.dataaccess.accesslevel;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.base.BaseDAO;
import com.itsy.util.SQLPropertiesUtil;

public class AccessLevelDAO extends BaseDAO {

	private static Logger logger = Logger.getLogger(AccessLevelDAO.class);
	
	/**
	 * Method to return {@link AccessLevelBO} by accessLevelId.
	 * @param accessLevelId
	 * @return {@link AccessLevelBO}
	 * @throws Exception
	 */
	public AccessLevelBO findByAccessLevelId(int accessLevelId) throws Exception {
		logger.debug(".findByAccessLevelId(" + accessLevelId + ") start");
		AccessLevelBO bo = null;
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.by.accesslevelid"));
			setPStmtValue(1, accessLevelId);
			
			executeQuery();
			
			if (hasMoreResults()) {
				bo = new AccessLevelBO();
				bo.setAccessLevelId(getRSValueInt("accesslevelid"));
				bo.setDescription(getRSValueString("description"));
			}
		} catch (Exception e) {
			logger.error(".findByAccessLevelId(" + accessLevelId + ") [Exception]", e);
			throw e;
		} finally {
			close();
		}
		return bo;
	}
	
	/**
	 * Method to return all AccessLevelBO records.
	 * @return List<AccessLevelBO>
	 * @throws Exception
	 */
	public List<AccessLevelBO>  findAll() throws Exception {
		AccessLevelBO bo = null;
		List<AccessLevelBO> results = new ArrayList<AccessLevelBO>();
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.all"));
			executeQuery();
			
			while (hasMoreResults()) {
				bo = new AccessLevelBO();
				bo.setAccessLevelId(getRSValueInt("accesslevelid"));
				bo.setDescription(getRSValueString("description"));
				results.add(bo);
			}
		} catch (Exception e) {
			logger.error(".findAll() [Exception]", e);
			throw e;
		} finally {
			close();
		}
		return results;
	}
	
	/**
	 * returns the search results for the specified search criteria.
	 * @param AccessLevelBO bo
	 * @return List<AccessLevelBO>
	 * @throws Exception
	 */
	public List<AccessLevelBO> search(AccessLevelBO bo) throws Exception {
		List<AccessLevelBO> retVal = new ArrayList<AccessLevelBO>();
		
		StringBuilder fields = new StringBuilder();
		fields.append("select accesslevelid, description");
		
		ArrayList<String>tables = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>psValues = new ArrayList<String>();
		
		// add in the base tables and linking fields
		tables.add("accesslevel_defn");
		
		// build the dynamic sql
		if (bo.getDescription() != null && bo.getDescription().length() > 0) {
			where.add("lower(description) like ?");
			psValues.add((bo.getDescription().toLowerCase()) + "%");
		} 
		
		String countSql = buildSQL(fields.toString(), tables, where);
		String sql = buildSQL(fields.toString(), tables, where) + " order by upper(description)";
		logger.debug("dynamic sql [" + sql + "]");
		
		try {
			doSearch(countSql, sql, psValues);
			
			while (hasMoreResults()) {
				retVal.add(createAccessLevelObject());
			}
		} catch (Exception e) {
			logger.error("Exception in search", e);
			throw e;
		} finally {
			close();
		}	
		
		fields = null;
		tables = null;
		where = null;
		psValues = null;
		
		return retVal;
	}
	
	/**
	 * inserts new record 
	 * @param bo AccessLevelBO 
	 * @return AccessLevelBO
	 * <p>
	 * @throws Exception
	 */
	public AccessLevelBO insert(AccessLevelBO bo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.insert"));
			setPStmtValue(1, bo.getDescription());
			
			execute();
			
			bo.setAccessLevelId(getIdentityValueInt("accesslevelid", "accesslevel_defn"));
			
			return bo;
		} catch (Exception e) {
			logger.error("Exception in AccessLevelBO insert(AccessLevelBO bo)", e);
			throw e;
		} finally {
			try {
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * update existing record
	 * @param bo AccessLevelBO 
	 * <p>
	 * @throws Exception
	 */
	public void update(AccessLevelBO bo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.update.byid"));
			setPStmtValue(1, bo.getDescription());
			setPStmtValue(2, bo.getAccessLevelId());
			execute();
		} catch (Exception e) {
			logger.error("Exception in AccessLevelBO update(AccessLevelBO bo)", e);
			throw e;
		} finally {
			try {
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * delete existing record
	 * @param bo AccessLevelBO 
	 * <p>
	 * @throws Exception
	 */
	public void delete(AccessLevelBO bo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.delete.byid"));
			setPStmtValue(1, bo.getAccessLevelId());

			execute();
		} catch (Exception e) {
			logger.error("Exception in AccessLevelBO delete(AccessLevelBO bo)", e);
			throw e;
		} finally {
			try {
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * returns a definition object
	 * @return List<AccessLevelBO>
	 * @throws Exception
	 */
	private AccessLevelBO createAccessLevelObject() throws Exception {
		AccessLevelBO bo = new AccessLevelBO();
		bo.setAccessLevelId(getRSValueInt("accesslevelid"));
		bo.setDescription(getRSValueString("description"));
		return bo;
	}
}
