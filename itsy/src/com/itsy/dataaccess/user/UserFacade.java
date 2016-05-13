package com.itsy.dataaccess.user;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.base.DAOFactory;
import com.itsy.dataaccess.base.DataFetch;
import com.itsy.dataaccess.base.DatabaseConnection;

public class UserFacade {

	private static Logger logger = Logger.getLogger(UserFacade.class);
	
	/**
	 * Method to return UserBO by username 
	 * @param userName
	 * @return UserBO
	 * @throws Exception
	 */
	public static UserBO findByUsername(String userName, DataFetch fetchMode) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getUserDAO(conn).findByUsername(userName, fetchMode);
		} catch (Exception e) {
			logger.info(".findByUsername(" + userName + ") [Exception]", e);
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
	 * Method to return UserBO by userId 
	 * @param userId
	 * @return UserBO
	 * @throws Exception
	 */
	public static UserBO findByUserId(int userId, DataFetch fetchMode) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getUserDAO(conn).findByUserId(userId, fetchMode);
		} catch (Exception e) {
			logger.info(".findByUserId(" + userId + ") [Exception]", e);
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
	 * Method to return UserBO by internalPid 
	 * @param internalPid
	 * @return UserBO
	 * @throws Exception
	 */
	public static UserBO findByInternalPid(int internalPid, DataFetch fetchMode) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			return getUserDAO(conn).findByInternalPid(internalPid, fetchMode);
		} catch (Exception e) {
			logger.info(".findByInternalPid(" + internalPid + ") [Exception]", e);
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
	 * updates existing user record.
	 *
	 * Handles exception from DAO if any
	 * <p>
	 * @param bo UserBO 
	 * 
	 * @throws Exception 
	 */
	public static void updateUser(UserBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			getUserDAO(conn).updateUser(bo);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in updateUser(UserBO bo)", e);
			throw e;	
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				// do nothing
			}
		}
	}
	
	public static void insertItsyUser(int userId, String firstName, String lastName) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			getUserDAO(conn).insertItsyUser(userId, firstName, lastName);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in insertItsyUser(UserBO bo)", e);
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
	 * Deletes existing user record
	 *
	 * Handles exception from DAO if any
	 * <p>
	 * @param bo UserBO 
	 * 
	 * @throws Exception 
	 */
	public static void deleteUser(UserBO bo) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			getUserDAO(conn).deleteUser(bo);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			logger.error("Exception in deleteUser(UserBO bo)", e);
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
	 * @return UserDAO
	 */
	public static UserDAO getUserDAO(Connection conn) {
		UserDAO userDAO = DAOFactory.getFactory().getUserDAO();
		userDAO.setCon(conn);
		return userDAO;
	}
}
