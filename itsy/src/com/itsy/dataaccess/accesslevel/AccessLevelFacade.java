package com.itsy.dataaccess.accesslevel;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.base.DAOFactory;
import com.itsy.dataaccess.base.DatabaseConnection;
import com.itsy.exception.TooManyResultsException;

public class AccessLevelFacade {

	private static Logger logger = Logger.getLogger(AccessLevelFacade.class);
	
	/**
	 * Method to return AccessLevelBO by accesslevelid.
	 * @param accessLevelId
	 * @return AccessLevelBO
	 * @throws Exception
	 */
	public static AccessLevelBO findByAccessLevelId(int accessLevelId) throws Exception {
		Connection conn = null;
		AccessLevelBO bo = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			bo = getAccessLevelDAO(conn).findByAccessLevelId(accessLevelId);
		} catch (Exception e) {
			logger.info(".findByAccessLevelId(" + accessLevelId + ") [Exception]", e);
			throw e;
		} finally {
			try {
				conn.close();
			} catch (Exception e) { 
				// do nothing 
			}
		}
		return bo;	
	}
	
	/**
	 * Method to return all AccessLevelBO records.
	 * @return List<AccessLevelBO>
	 * @throws Exception
	 */
	public static List<AccessLevelBO> findAll() throws Exception {
		Connection conn = null;
		List<AccessLevelBO> result = new ArrayList<AccessLevelBO>();
		try {
			conn = DatabaseConnection.getItsyConnection();
			result = getAccessLevelDAO(conn).findAll();
		} catch (Exception e) {
			logger.info(".findAll() [Exception]", e);
			throw e;
		} finally {
			try {
				conn.close();
			} catch (Exception e) { 
				// do nothing
			}
		}
		return (result);	
	}
	
	/**
	 * @param conn
	 * @return {@link AccessLevelDAO}
	 */
	private static AccessLevelDAO getAccessLevelDAO(Connection conn) {
		AccessLevelDAO accessLevelDAO = DAOFactory.getFactory().getAccessLevelDAO();
		accessLevelDAO.setCon(conn);
		return accessLevelDAO;
	}

	/**
	 * returns the search results for the specified search criteria.
	 * @param AccessLevelBO bo
	 * @return List<AccessLevelBO>
	 * @throws Exception
	 */
	public static List<AccessLevelBO> search(AccessLevelBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getAccessLevelDAO(conn).search(bo);
		} catch (TooManyResultsException tme) {
			throw tme;
		} catch (Exception e) {
			logger.error("Exception in List<AccessLevelBO> search(AccessLevelBO bo)", e);
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
	 * @param bo AccessLevelBO
	 * @return AccessLevel_DefnVO  
	 * 
	 * @throws Exception 
	 */
	public static AccessLevelBO insert(AccessLevelBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			bo = getAccessLevelDAO(conn).insert(bo);
			conn.commit();
			
			return bo;
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in insert(AccessLevelBO bo)", e);
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
	 * Updates existing record.
	 *
	 * Handles exception from DAO if any
	 * <p>
	 * @param bo AccessLevelBO 
	 * 
	 * @throws Exception 
	 */
	public static void update(AccessLevelBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			getAccessLevelDAO(conn).update(bo);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in update(AccessLevelBO bo)", e);
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
	 * @param bo AccessLevelBO 
	 * 
	 * @throws Exception 
	 */
	public static void delete(AccessLevelBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			getAccessLevelDAO(conn).delete(bo);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in delete(AccessLevelBO bo)", e);
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
