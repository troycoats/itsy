package com.itsy.dataaccess.role;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.base.BaseDAO;
import com.itsy.util.SQLPropertiesUtil;

public class RoleDAO extends BaseDAO {
	
	private static  Logger logger = Logger.getLogger(RoleDAO.class);
	
	/**
	 * Method to return {@link RoleVO} by roleId.
	 * @param roleId
	 * @return {@link RoleBO}
	 * @throws Exception
	 */
	public RoleBO findByRoleId(int roleId) throws Exception {
		RoleBO bo = null;
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.by.roleid"));
			setPStmtValue(1, roleId);
			executeQuery();

			if (hasMoreResults()) {
				bo = createRoleBO();
			}
		} catch (Exception e) {
			logger.error(".findByRoleId(int roleId) [Exception]", e);
			throw e;
		} finally {
			close();
		}
		return bo;
	}
	
	/**
	 * Method to return {@link RoleVO} by description.
	 * @param roleName
	 * @return {@link RoleBO}
	 * @throws Exception
	 */
	public RoleBO findByRoleName(String roleName) throws Exception {
		RoleBO bo = null;
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.by.description"));
			setPStmtValue(1, roleName);
			executeQuery();

			if (hasMoreResults()) {
				bo = createRoleBO();
			}
		} catch (Exception e) {
			logger.error(".findByRoleName(String roleName) [Exception]", e);
			throw e;
		} finally {
			close();
		}
		return bo;
	}
	
	/**
	 * Method to return all available RoleBO
	 * @return List<RoleBO>
	 * @throws Exception
	 */
	public List<RoleBO> findAll() throws Exception {
		List<RoleBO> retVal = new ArrayList<RoleBO>();
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.all"));
			executeQuery();
			
			while (hasMoreResults()) {
				retVal.add(createRoleBO());
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
	 * Method to return all available RoleBO
	 * @return List<RoleBO>
	 * @throws Exception
	 */
	public List<RoleBO> findByRoleIds(String roleIds) throws Exception {
		List<RoleBO> retVal = new ArrayList<RoleBO>();
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.by.roleids") + " (" + roleIds + ") order by description");
			executeQuery();
			
			while (hasMoreResults()) {
				retVal.add(createRoleBO());
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
	 * @return RoleBO
	 * @throws Exception
	 */
	private RoleBO createRoleBO() throws Exception{
		RoleBO roleBO = new RoleBO();
		roleBO.setRoleId(getRSValueInt("roleid"));
		roleBO.setDescription(getRSValueString("description"));
		return roleBO;
	}
	
	/**
	 * returns the search results for the specified search criteria.
	 * @param RoleBO bo
	 * @return List<RoleBO>
	 * @throws Exception
	 */
	public List<RoleBO> search(RoleBO bo) throws Exception {
		List<RoleBO>retVal = new ArrayList<RoleBO>();
		
		StringBuilder fields = new StringBuilder();
		fields.append("select role_id, role_desc");
		
		ArrayList<String>tables = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>psValues = new ArrayList<String>();
		
		// add in the base tables and linking fields
		tables.add("role_defn");
		
		// build the dynamic sql
		if (bo.getDescription() != null && bo.getDescription().length() > 0) {
			where.add("lower(role_desc) like ?");
			psValues.add((bo.getDescription().toLowerCase()) + "%");
		} 
		
		String sql = buildSQL(fields.toString(), tables, where) + " order by upper(role_desc)";
		logger.debug("dynamic sql [" + sql + "]");
		
		try {
			doSearchNoException(sql, psValues);

			while (hasMoreResults()) {
				retVal.add(createRoleBO());
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
	 * @param vo RoleVO 
	 * @return RoleVO
	 * <p>
	 * @throws Exception
	 */
	public RoleBO insert(RoleBO bo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.insert"));
			setPStmtValue(1, bo.getDescription());
			execute();
			
			bo.setRoleId(getIdentityValueInt("roleid", "role_defn"));
			
			return bo;
		} catch (Exception e) {
			logger.error("Exception in Role_DefnVO insert(Role_DefnVO vo)", e);
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
	 * @param vo Role_DefnVO 
	 * <p>
	 * @throws Exception
	 */
	public void update(RoleBO bo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.update.byid"));
			setPStmtValue(1, bo.getDescription());
			setPStmtValue(2, bo.getRoleId());
			execute();
		} catch (Exception e) {
			logger.error("Exception in void update(RoleBO bo)", e);
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
	 * @param vo RoleVO 
	 * <p>
	 * @throws Exception
	 */
	public void delete(RoleVO vo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.delete.byid"));
			setPStmtValue(1, vo.getRoleId());
			execute();
		} catch (Exception e) {
			logger.error("Exception in void delete(RoleVO vo)", e);
			throw e;
		} finally {
			try {
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
