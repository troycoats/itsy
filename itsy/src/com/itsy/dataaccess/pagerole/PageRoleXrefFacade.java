package com.itsy.dataaccess.pagerole;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.base.DAOFactory;
import com.itsy.dataaccess.base.DatabaseConnection;
import com.itsy.dataaccess.page.PageBO;
import com.itsy.enumeration.AccessLevel;
import com.itsy.session.PageAccess;

public class PageRoleXrefFacade {
	private static Logger logger = Logger.getLogger(PageRoleXrefFacade.class);
	
	/**
	 * This method returns all the page access roles, defn records available
	 * and store in a Map all the access roles by pageurl.
	 * @return HashMap<String, List<PageAccess>>
	 * @throws Exception
	 */
	public static HashMap<String, List<PageAccess>> retrieveAllPageAccessWithRoles() throws Exception {		
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getPageRoleDAO(conn).retrieveAllPageAccessWithRoles();
		} catch (Exception e) {
			logger.info(".retrieveAllPageAccessWithRoles [Exception]", e);
			throw e;
		} finally {
			try {
				conn.close();
			} catch (Exception e) { 
				// do nothing
			}
		}
	}
	
	/**
	 * This method returns page access for specific roles, defn records available
	 * and store in a list all the access roles by pageurl.
	 * @return List<PageAccess>
	 * @throws Exception
	 */
	public static List<PageAccess> retrievePageAccessForSpecificRoles(List<Integer> roleIds, AccessLevel accessLevel) throws Exception {		
		logger.info(".retrievePageAccessForSpecificRoles() start");
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getPageRoleDAO(conn).retrievePageAccessForSpecificRoles(roleIds, accessLevel);
		} catch (Exception e) {
			logger.info(".retrievePageAccessForSpecificRoles [Exception]", e);
			throw e;
		} finally {
			try {
				conn.close();
			} catch (Exception e) { /* do nothing */ }
			logger.info(".retrievePageAccessForSpecificRoles() end");
		}
	}
	
	/**
	 * This method returns page object for a specific role
	 * @param pagePO PagePO
	 * @return PagePO
	 * @throws Exception
	 */
	public static PageBO retrieveXref(PageBO pageBO) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			pageBO = getPageRoleDAO(conn).retrieveXref(pageBO);
		} catch (Exception e) {
			logger.info(".retrieveXref(pageBO) [Exception]", e);
			throw e;
		} finally {
			try {
				conn.close();
			}
			catch (Exception e) { 
				// do nothing 
			}
		}
		return pageBO;
	}
	
	/**
	 * This method returns page objects for a all roles
	 * @param pagePO PagePO
	 * @return List<PagePO>
	 * @throws Exception
	 */
	public static List<PageBO> retrieveAllXref(PageBO pageBO, boolean activeOnly) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getPageRoleDAO(conn).retrieveAllXref(pageBO, activeOnly);
		} catch (Exception e) {
			logger.info(".retrieveAllXref(pageBO) [Exception]", e);
			throw e;
		} finally {
			try {
				conn.close();
			} catch (Exception e) { 
				// do nothing
			}
		}
	}
	
	/**
	 * Updates existing record.
	 *
	 * Handles exception from DAO if any
	 * <p>
	 * @param bo PageRoleXrefBO 
	 * 
	 * @throws Exception 
	 */
	public static void update(PageRoleXrefBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			getPageRoleDAO(conn).update(bo);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in update(PageRoleXrefBO bo)", e);
			throw e;	
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				// nothing
			}
		}
	}
	
	/**
	 * Deletes existing record.
	 *
	 * Handles exception from DAO if any
	 * <p>
	 * @param bo PageRoleXrefBO 
	 * 
	 * @throws Exception 
	 */
	public static void delete(PageRoleXrefBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			getPageRoleDAO(conn).delete(bo);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in delete(PageRoleXrefBO bo)", e);
			throw e;	
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				// nothing
			}
		}
	}
	
	/**
	 * Inserts new record
	 *
	 * Handles exception from DAO if any
	 * <p>
	 * @param bo PageRoleXrefBO
	 * @return PageRoleXrefBO  
	 * 
	 * @throws Exception 
	 */
	public static PageRoleXrefBO insert(PageRoleXrefBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			bo = getPageRoleDAO(conn).insert(bo);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in insert(PageRoleXrefBO bo)", e);
			throw e;	
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				// nothing
			}
		}
		return bo;
	}
	
	private static PageRoleXrefDAO getPageRoleDAO(Connection conn) {
		PageRoleXrefDAO pageRoleXrefDAO = DAOFactory.getFactory().getPageRoleXrefDAO();
		pageRoleXrefDAO.setCon(conn);
		return pageRoleXrefDAO;
	}
}
