package com.itsy.dataaccess.role;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.base.DAOFactory;
import com.itsy.dataaccess.base.DatabaseConnection;

public class UserRoleXrefFacade {
	private static Logger log = Logger.getLogger(UserRoleXrefFacade.class.getName());

	public static HashMap<Integer, String> getRoles(int userid) throws Exception {
		HashMap<Integer, String> retVal = new HashMap<Integer, String>();
		Connection conn = null;
		try	{
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(true);
			
			UserRoleXrefDAO dao = DAOFactory.getFactory().getUserRoleXrefDAO();
			dao.setCon(conn);
			
			retVal = dao.getRoles(userid);
			
			dao = null;
		} catch (Exception e) {
			log.error("Exception in getRoles", e);
			throw e;
		} finally {
			try {
				conn.close();
			} catch (Exception execption) {
			}
		}
		return retVal;
	}
	
	public static List<Integer> getAllRoles(int userid) throws Exception {
		List<Integer> retVal = new ArrayList<Integer>();
		Connection conn = null;
		try	{
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(true);
			
			UserRoleXrefDAO dao = DAOFactory.getFactory().getUserRoleXrefDAO();
			dao.setCon(conn);
			
			retVal = dao.getAllRoles(userid);
			
			dao = null;
		} catch (Exception e) {
			log.error("Exception in getAllRoles", e);
			throw e;
		} finally {
			try	{
				conn.close();
			} catch (Exception execption) {
			}
		}
		return retVal;
	}
	
	public static void delete(int userid, int roleId) throws Exception {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			UserRoleXrefDAO dao = DAOFactory.getFactory().getUserRoleXrefDAO();
			dao.setCon(conn);
			dao.delete(userid, roleId);
			conn.commit();
			
			dao = null;
		} catch (Exception e) {
			conn.rollback();
			log.error("Exception in delete", e);
			throw e;
		} finally {
			try {
				conn.close();
			} catch (Exception execption) {
			}
		}
	}
	
	public static int insert(int userid, int roleId) throws Exception {
		Connection conn = null;
		int retVal = -1;
		try {
			conn = DatabaseConnection.getItsyConnection();
			conn.setAutoCommit(false);
			UserRoleXrefDAO dao = DAOFactory.getFactory().getUserRoleXrefDAO();
			dao.setCon(conn);
			retVal = dao.insert(userid, roleId);
			conn.commit();
			
			dao = null;
		} catch (Exception e) {
			conn.rollback();
			log.error("Exception in insert", e);
			throw e;
		} finally {
			try {
				conn.close();
			} catch (Exception execption) {
			}
		}
		return retVal;
	}

}
