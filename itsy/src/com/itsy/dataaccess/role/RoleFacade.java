package com.itsy.dataaccess.role;

import java.sql.Connection;
import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.base.DAOFactory;
import com.itsy.dataaccess.base.DatabaseConnection;
import com.itsy.exception.TooManyResultsException;

public class RoleFacade {

	private static Logger logger = Logger.getLogger(RoleFacade.class);
	
	/**
	 * Method to return RoleBO by roleId.
	 * @param roleId
	 * @return RoleBO
	 * @throws Exception
	 */
	public static RoleBO findByRoleId(int roleId) throws Exception{
		logger.info(".findByRoleId(int roleId) start");
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getRoleDAO(conn).findByRoleId(roleId);
		} catch (Exception e) {
			logger.info(".findByRoleId(int roleId) [Exception]", e);
			throw(e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) { /* do nothing */ }
			logger.info(".findByRoleId(int roleId) end");
		}
	}
	
	/**
	 * Method to return RoleBO by description.
	 * @param roleName
	 * @return RoleBO
	 * @throws Exception
	 */
	public static RoleBO findByRoleName(String roleName, Connection conn) throws Exception{
		logger.info(".findByRoleName(String roleName) start");
		try {
			return getRoleDAO(conn).findByRoleName(roleName);
		} catch (Exception e) {
			logger.info(".findByRoleName(String roleName)) [Exception]", e);
			throw e;
		} finally {
			try {
				conn.close();
			} catch (Exception e) { 
				// do nothing 
			}
			logger.info(".findByRoleName(String roleName) end");
		}
	}
	
	/**
	 * Method to return all available RoleBOs
	 * @return List<RoleBO>
	 * @throws Exception
	 */
	public static List<RoleBO> findAll() throws Exception{
		logger.info(".findAll() start");
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getRoleDAO(conn).findAll();
		} catch (Exception e) {
			logger.info(".findAll() [Exception]", e);
			throw e;
		} finally {
			try {
				conn.close();
			} catch (Exception e) { 
				// do nothing
			}
			logger.info(".findAll() end");
		}
	}
	
	/**
	 * Method to return all available RoleBOs
	 * @return List<RoleBO>
	 * @throws Exception
	 */
	public static List<RoleBO> findByRoleIds(String roleIds) throws Exception{
		logger.info(".findByRoleIds(String roleIds) start");
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getRoleDAO(conn).findByRoleIds(roleIds);
		} catch (Exception e) {
			logger.info(".findByRoleIds(String roleIds) [Exception]", e);
			throw e;
		} finally {
			try {
				conn.close();
			} catch (Exception e) { 
				// do nothing
			}
			logger.info(".findByRoleIds(String roleIds) end");
		}
	}
		
	/**
	 * @param conn
	 * @return {@link RoleDAO}
	 */
	private static RoleDAO getRoleDAO(Connection conn) {
		DAOFactory factory = DAOFactory.getFactory();
		RoleDAO rolesDAO = factory.getRoleDAO();
		rolesDAO.setCon(conn);
		return rolesDAO;
	}

	/**
	 * returns the search results for the specified search criteria.
	 * @param RoleBO bo
	 * @return List<RoleBO>
	 * @throws Exception
	 */
	public static List<RoleBO> search(RoleBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getRoleDAO(conn).search(bo);
		} catch (TooManyResultsException tme) {
			throw tme;
		} catch (Exception e) {
			logger.error("Exception in List<DefinitionPO> search(RoleBO bo)", e);
			throw e;
		} finally {
			try {
				conn.close();
			} catch (Exception ex) {
			}
		}
	}
	
	/**
	 * Inserts new record
	 *
	 * Handles exception from DAO if any
	 * <p>
	 * @param bo RoleBO
	 * @return RoleBO  
	 * 
	 * @throws Exception 
	 */
	public static RoleBO insert(RoleBO bo) throws Exception {
		Connection conn = null;
		logger.debug(">>> insert(RoleBO bo)");
		
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			bo = getRoleDAO(conn).insert(bo);
			conn.commit();
			
			return bo;
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in insert(RoleBO bo)", e);
			throw e;	
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				// nothing
			}
			logger.debug(">>Exiting insert(RoleBO bo)<<");
		}
	}
	
	/**
	 * Updates existing record.
	 *
	 * Handles exception from DAO if any
	 * <p>
	 * @param bo RoleBO 
	 * 
	 * @throws Exception 
	 */
	public static void update(RoleBO bo) throws Exception {
		Connection conn = null;
		logger.debug(">>> update(RoleBO bo)");
		
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			getRoleDAO(conn).update(bo);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in update(RoleBO bo)", e);
			throw e;	
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				// nothing
			}
			logger.debug(">>Exiting update(RoleBO bo)<<");
		}
	}
	
	/**
	 * Delete existing record.
	 *
	 * Handles exception from DAO if any
	 * <p>
	 * @param bo RoleBO 
	 * 
	 * @throws Exception 
	 */
	public static void delete(RoleBO bo) throws Exception {
		Connection conn = null;
		logger.debug(">>> delete(RoleBO bo)");
		
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			getRoleDAO(conn).delete(bo);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in delete(RoleBO bo)", e);
			throw e;	
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				// nothing
			}
			logger.debug(">>Exiting delete(RoleBO bo)<<");
		}
	}
}
