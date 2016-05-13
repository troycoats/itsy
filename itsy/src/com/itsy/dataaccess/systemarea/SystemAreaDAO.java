package com.itsy.dataaccess.systemarea;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.base.BaseDAO;
import com.itsy.util.SQLPropertiesUtil;

public class SystemAreaDAO extends BaseDAO {

	private static Logger logger = Logger.getLogger(SystemAreaDAO.class);
	
	/**
	 * Method to return {@link SystemAreaVO} by areaId.
	 * @param areaId
	 * @return {@link SystemAreaVO}
	 * @throws Exception
	 */
	public SystemAreaBO findByAreaId(int areaId) throws Exception {
		SystemAreaBO bo = null;
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.by.areaId"));
			setPStmtValue(1, Integer.valueOf(areaId));
			executeQuery();
			
			if (hasMoreResults()) {
				bo = createSystemAreaBO();
			}
		} catch (Exception e) {
			logger.error(". findByAreaId(int areaId) [Exception]", e);
			throw e;
		} finally {
			close();
		}
		return bo;
	}
	
	/**
	 * Method to return all SystemArea_DefnBO records
	 * @return List<SystemArea_DefnBO>
	 * @throws Exception
	 */
	public List<SystemAreaBO>  findAll() throws Exception {
		List<SystemAreaBO> retVal = new ArrayList<SystemAreaBO>();
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.all"));
			executeQuery();
			
			while (hasMoreResults()) {
				retVal.add(createSystemAreaBO());
			}
		} catch (Exception e) {
			logger.error(".findAll() [Exception]", e);
			throw e;
		} finally {
			close();
		}
		return retVal;
	}
	
	/**
	 * 
	 * @return SystemAreaBO
	 * @throws Exception
	 */
	private SystemAreaBO createSystemAreaBO() throws Exception{
		SystemAreaBO systemAreaBO = new SystemAreaBO();
		systemAreaBO.setAreaId(getRSValueInt("areaid"));
		systemAreaBO.setDescription(getRSValueString("description"));
		return systemAreaBO;
	}
	
	/**
	 * returns the search results for the specified search criteria.
	 * @param SystemAreaBO bo
	 * @return List<SystemAreaBO>
	 * @throws Exception
	 */
	public List<SystemAreaBO> search(SystemAreaBO bo) throws Exception {
		List<SystemAreaBO> retVal = new ArrayList<SystemAreaBO>();
		
		StringBuilder fields = new StringBuilder();
		fields.append("select areaid, description");
		
		ArrayList<String>tables = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>psValues = new ArrayList<String>();
		
		// add in the base tables and linking fields
		tables.add("systemarea_defn");
		
		// build the dynamic sql
		if (bo.getDescription() != null && bo.getDescription().length() > 0) {
			where.add("lower(description) like ?");
			psValues.add((bo.getDescription().toLowerCase()) + "%");
		} 
		
		String sql = buildSQL(fields.toString(), tables, where) + " order by upper(description)";
		logger.debug("dynamic sql [" + sql + "]");
		
		try {
			doSearchNoException(sql, psValues);

			while (hasMoreResults()) {
				retVal.add(createSystemAreaObject());
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
	 * @param bo SystemAreaBO 
	 * @return SystemAreaBO
	 * <p>
	 * @throws Exception
	 */
	public SystemAreaBO insert(SystemAreaBO bo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.insert"));
			setPStmtValue(1, bo.getDescription());
			execute();
			
			bo.setAreaId(getIdentityValueInt("areaid", "systemarea_defn"));
			
			return bo;
		} catch (Exception e) {
			logger.error("Exception in SystemAreaBO insert(SystemAreaBO bo)", e);
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
	 * @param bo SystemAreaBO 
	 * <p>
	 * @throws Exception
	 */
	public void update(SystemAreaBO bo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.update.byid"));
			setPStmtValue(1, bo.getDescription());
			setPStmtValue(2, bo.getAreaId());
			execute();
		} catch (Exception e) {
			logger.error("Exception in SystemAreaBO update(SystemAreaBO bo)", e);
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
	 * @param bo SystemAreaBO 
	 * <p>
	 * @throws Exception
	 */
	public void delete(SystemAreaBO bo) throws Exception {
		try {
			// delete page system xref
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.deletexref.byid"));
			setPStmtValue(1, bo.getAreaId());
			execute();
			
			// delete system area
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.delete.byid"));
			setPStmtValue(1, bo.getAreaId());
			execute();
		} catch (Exception e) {
			logger.error("Exception in SystemAreaBO delete(SystemAreaBO bo)", e);
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
	 * @return List<SystemAreaBO>
	 * @throws Exception
	 */
	private SystemAreaBO createSystemAreaObject() throws Exception {
		SystemAreaBO bo = new SystemAreaBO();
		bo.setAreaId(getRSValueInt("areaid"));
		bo.setDescription(getRSValueString("description"));
		return bo;
	}
}
