package com.itsy.dataaccess.pagesystem;

import java.sql.Connection;
import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.base.DAOFactory;
import com.itsy.dataaccess.base.DatabaseConnection;
import com.itsy.dataaccess.page.PageBO;
import com.itsy.dataaccess.systemarea.SystemAreaBO;

public class PageSystemXrefFacade {

	private static Logger logger = Logger.getLogger(PageSystemXrefFacade.class);
	
	/**
	 * Method to return {@link PageSystemXrefBO} by pageId
	 * @param pageId
	 * @return PageSystemXrefBO
	 * @throws Exception
	 */
	public static PageSystemXrefBO findByPageId(int pageId) throws Exception{
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getPageSystemXrefDAO(conn).findByPageId(pageId);
		} catch (Exception e) {
			logger.info(".find(int pageId,int areaId) [Exception]", e);
			throw(e);
		} finally {
			try {
				conn.close();
			}
			catch (Exception e) { 
				// do nothing 
			}
		}
	}
	
	/**
	 * Method to return List<DefinitionPO> findSystemAreasByPageIds(int[] pageIds)
	 * @param int[] pageIds
	 * @return List<SystemAreaBO>
	 * @throws Exception
	 */
	public static List<SystemAreaBO> findSystemAreasByPageIds(int[] pageIds) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getPageSystemXrefDAO(conn).findSystemAreasByPageIds(pageIds);
		} catch (Exception e) {
			logger.info(".findSystemAreasByPageIds(int[] pageIds) [Exception]", e);
			throw(e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) { 
				// do nothing 
			}
		}
	}
	
	/**
	 * Method to return List<PagePO> findPagesBySystemAreaId(int areaId, int[] pageIds)
	 * @param int areaId
	 * @param int[] pageIds
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public static List<PageBO> findPagesBySystemAreaId(int areaId, int[] pageIds) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getPageSystemXrefDAO(conn).findPagesBySystemAreaId(areaId, pageIds);
		} catch (Exception e) {
			logger.info(".findPagesBySystemAreaId(int areaId, int[] pageIds) [Exception]", e);
			throw(e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) { 
				// do nothing
			}
		}
	}
	
	/**
	 * Method to return List<PageBO> findPages(int[] pageIds)
	 * @param int[] pageIds
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public static List<PageBO> findPages(int[] pageIds) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getPageSystemXrefDAO(conn).findPages(pageIds);
		} catch (Exception e) {
			logger.info(".findPages(int[] pageIds) [Exception]", e);
			throw(e);
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
	 * @return {@link PageSystemXrefDAO}
	 */
	private static PageSystemXrefDAO getPageSystemXrefDAO(Connection conn) {
		PageSystemXrefDAO pageSystemXrefDAO = DAOFactory.getFactory().getPageSystemXrefDAO();
		pageSystemXrefDAO.setCon(conn);
		return pageSystemXrefDAO;
	}

	/**
	 * This method returns page object for a specific system area
	 * @param pageBO PageBO
	 * @return PageBO
	 * @throws Exception
	 */
	public static PageBO retrieveXref(PageBO pageBO) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			pageBO = getPageSystemXrefDAO(conn).retrieveXref(pageBO);
		} catch (Exception e) {
			logger.info(".retrieveXref(pageBO) [Exception]", e);
			throw(e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) { 
				// do nothing 
			}
		}
		return pageBO;
	}
	
	/**
	 * This method returns page objects for a all system areas
	 * @param pageBO PageBO
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public static List<PageBO> retrieveAllXref(PageBO pageBO) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getPageSystemXrefDAO(conn).retrieveAllXref(pageBO);
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
	 * @param bo PageSystemXrefBO 
	 * 
	 * @throws Exception 
	 */
	public static void update(PageSystemXrefBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			getPageSystemXrefDAO(conn).update(bo);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in update(PageSystemXrefBO bo)", e);
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
	 * @param bo PageSystemXrefBO 
	 * 
	 * @throws Exception 
	 */
	public static void delete(PageSystemXrefBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			getPageSystemXrefDAO(conn).delete(bo);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in delete(PageSystemXrefBO bo)", e);
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
	 * @param bo PageSystemXrefBO
	 * @return PageSystemXrefBO  
	 * 
	 * @throws Exception 
	 */
	public static PageSystemXrefBO insert(PageSystemXrefBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			bo = getPageSystemXrefDAO(conn).insert(bo);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in insert(PageSystemXrefBO bo)", e);
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
	 * Updates page display orders
	 *
	 * Handles exception from DAO if any
	 * <p>
	 * @param String[] pageids
	 * @param String[] displayorders
	 * 
	 * @throws Exception 
	 */
	public static void updateDisplayOrders(String[] pageids, String[] displayorders) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			getPageSystemXrefDAO(conn).updateDisplayOrders(pageids, displayorders);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in updateDisplayOrders(String[] pageids, String[] displayorders)", e);
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
