package com.itsy.dataaccess.page;

import java.sql.Connection;
import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.accesslevel.AccessLevelBO;
import com.itsy.dataaccess.base.DAOFactory;
import com.itsy.dataaccess.base.DatabaseConnection;
import com.itsy.exception.TooManyResultsException;
import com.itsy.util.RoleUtil.Role;

public class PageFacade {
	private static Logger logger = Logger.getLogger(PageFacade.class);
	
	/**
	 * Method to return PageBO by pageid.
	 * @param pageid
	 * @return PageBO
	 * @throws Exception
	 */
	public static PageBO findByPageId(int pageId) throws Exception{
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getDefnDAO(conn).findByPageId(pageId);
		} catch (Exception e) {
			logger.info(".findByPageId(int pageId) [Exception]", e);
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
	 * Method to return all PageBO records.
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public static List<PageBO> findAll() throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getDefnDAO(conn).findAll();
		} catch (Exception e) {
			logger.info(".findAll()  [Exception]", e);
			throw e;
		} finally {
			try {
				conn.close();
			} catch (Exception e) { 
				// do nothing 
			}
		}
	}
		
	public static List<PageBO> findPages() throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getDefnDAO(conn).findPages();
		} catch (Exception e) {
			logger.info(".findPages()  [Exception]", e);
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
	 * @param conn
	 * @return {@link PageDAO}
	 */
	private static PageDAO getDefnDAO(Connection conn) {
		PageDAO defnDAO = DAOFactory.getFactory().getPageDAO();
		defnDAO.setCon(conn);
		return defnDAO;
	}

	/**
	 * returns the search results for the specified search criteria.
	 * @param PageBO bo
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public static List<PageBO> searchByPage(PageBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getDefnDAO(conn).searchByPage(bo);
		} catch (TooManyResultsException tme) {
			throw tme;
		} catch (Exception e) {
			logger.error("Exception in List<PageBO> searchByPage(PagePO po)", e);
			throw e;
		} finally {
			try {
				conn.close();
			} catch (Exception ex) {
			}
		}
	}
	
	/**
	 * returns the search results for the specified search criteria.
	 * @param PageBO bo
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public static List<PageBO> searchByRole(PageBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getDefnDAO(conn).searchByRole(bo);
		} catch (TooManyResultsException tme) {
			throw tme;
		} catch (Exception e) {
			logger.error("Exception in List<PageBO> searchByRole(PageBO bo)", e);
			throw e;
		} finally {
			try {
				conn.close();
			} catch (Exception ex) {
			}
		}
	}
	
	/**
	 * returns the search results for the specified search criteria.
	 * @param String pageUrl
	 * @return List<Role>
	 * @throws Exception
	 */
	public static List<Role> searchForRole(String pageUrl, AccessLevelBO level) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getDefnDAO(conn).searchForRole(pageUrl, level);
		} catch (TooManyResultsException tme) {
			throw tme;
		} catch (Exception e) {
			logger.error("Exception in List<Role> searchForRole(String pageUrl)", e);
			throw e;
		} finally {
			try {
				conn.close();
			} catch (Exception ex) {
			}
		}
	}
	
	/**
	 * returns the search results for the specified search criteria.
	 * @param PageBO bo
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public static List<PageBO> searchByArea(PageBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getDefnDAO(conn).searchByArea(bo);
		} catch (TooManyResultsException tme) {
			throw tme;
		} catch (Exception e) {
			logger.error("Exception in List<PageBO> searchByArea(PageBO bo)", e);
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
	 * @param bo PageBO
	 * @return PageBO  
	 * 
	 * @throws Exception 
	 */
	public static PageBO insert(PageBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			bo = getDefnDAO(conn).insert(bo);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in insert(PageBO bo)", e);
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
	
	/**
	 * Updates existing record.
	 *
	 * Handles exception from DAO if any
	 * <p>
	 * @param bo PageBO 
	 * 
	 * @throws Exception 
	 */
	public static void update(PageBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			getDefnDAO(conn).update(bo);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in update(PageBO bo)", e);
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
	 * Delete existing record.
	 *
	 * Handles exception from DAO if any
	 * <p>
	 * @param bo PageBO 
	 * 
	 * @throws Exception 
	 */
	public static void delete(PageBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			getDefnDAO(conn).delete(bo, "sql.delete.referenced_roles");
			getDefnDAO(conn).delete(bo, "sql.delete.referenced_areas");
			getDefnDAO(conn).delete(bo, "sql.delete.page");
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in delete(PageBO bo)", e);
			throw e;	
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				// nothing
			}
		}
	}
}
