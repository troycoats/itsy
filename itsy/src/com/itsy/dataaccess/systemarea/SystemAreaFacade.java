package com.itsy.dataaccess.systemarea;

import java.sql.Connection;
import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.base.DAOFactory;
import com.itsy.dataaccess.base.DatabaseConnection;
import com.itsy.exception.TooManyResultsException;

public class SystemAreaFacade {

	private static Logger logger = Logger.getLogger(SystemAreaFacade.class);
	
	/**
	 * Method to return SystemAreaBO by roleId.
	 * @param roleId
	 * @return SystemAreaBO
	 * @throws Exception
	 */
	public static SystemAreaBO  findByAreaId(int areaId) throws Exception{
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getSystemAreaDAO(conn).findByAreaId(areaId);
		} catch (Exception e) {
			logger.info(".findByAreaId(int areaId) [Exception]", e);
			throw(e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) { /* do nothing */ }
		}
	}
	
	/**
	 * Method to return all SystemAreaBO records
	 * @return List<SystemAreaBO>
	 * @throws Exception
	 */
	public static List<SystemAreaBO>  findAll() throws Exception{
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getSystemAreaDAO(conn).findAll();
		} catch (Exception e) {
			logger.info(".findAll()  [Exception]", e);
			throw(e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) { /* do nothing */ }
		}
	}
	
	
	/**
	 * @param conn
	 * @return {@link SystemAreaDAO}
	 */
	private static SystemAreaDAO getSystemAreaDAO(Connection conn) {
		SystemAreaDAO systemAreaDAO = DAOFactory.getFactory().getSystemAreaDAO();
		systemAreaDAO.setCon(conn);
		return systemAreaDAO;
	}
	
	/**
	 * returns the search results for the specified search criteria.
	 * @param SystemAreaBO bo
	 * @return List<SystemAreaBO>
	 * @throws Exception
	 */
	public static List<SystemAreaBO> search(SystemAreaBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getSystemAreaDAO(conn).search(bo);
		} catch (TooManyResultsException tme) {
			throw tme;
		} catch (Exception e) {
			logger.error("Exception in List<SystemAreaBO> search(SystemAreaBO bo)", e);
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
	 * @param bo SystemAreaBO
	 * @return SystemAreaVO  
	 * 
	 * @throws Exception 
	 */
	public static SystemAreaBO insert(SystemAreaBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			bo = getSystemAreaDAO(conn).insert(bo);
			conn.commit();
			
			return bo;
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in insert(SystemAreaBO bo)", e);
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
	 * @param bo SystemAreaBO 
	 * 
	 * @throws Exception 
	 */
	public static void update(SystemAreaBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			getSystemAreaDAO(conn).update(bo);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in update(SystemAreaBO bo)", e);
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
	 * @param bo SystemAreaBO 
	 * 
	 * @throws Exception 
	 */
	public static void delete(SystemAreaBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			getSystemAreaDAO(conn).delete(bo);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in delete(SystemAreaBO bo)", e);
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
