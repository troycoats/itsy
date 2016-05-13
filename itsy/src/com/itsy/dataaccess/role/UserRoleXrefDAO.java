package com.itsy.dataaccess.role;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.base.BaseDAO;
import com.itsy.util.SQLPropertiesUtil;

public class UserRoleXrefDAO extends BaseDAO {
	private static Logger log = Logger.getLogger(UserRoleXrefDAO.class.getName());

	public HashMap<Integer, String> getRoles(int userid) throws Exception {
		HashMap<Integer, String> retVal = new HashMap<Integer, String>();
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.by.id"));
			setPStmtValue(1, userid);
			executeQuery();
			
			while (hasMoreResults()) {
				retVal.put(getRSValueInt("roleid"), getRSValueString("description"));
			}
		} catch (SQLException sqe) {
			StringBuffer logMsg = new StringBuffer("SQLException in getRole. \nError code: ");
			logMsg.append(sqe.getErrorCode());
			logMsg.append("\nMessage: ");
			logMsg.append(sqe.getMessage());
			logMsg.append("\nSQL state: ");
			logMsg.append(sqe.getSQLState());
			log.error(logMsg);
			log.error(sqe);
			logMsg = null;
			throw sqe;
		} catch (Exception e) {
			log.error("Exception in getRole", e);
			throw e;
		} finally {
			close();
		}	
		return retVal;
	}
	
	public List<Integer> getAllRoles(int userid) throws Exception {
		List<Integer> retVal = new ArrayList<Integer>();
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.by.id"));
			setPStmtValue(1, userid);
			executeQuery();
			
			while (hasMoreResults()) {
				retVal.add(getRSValueInt("roleid"));
			}
		} catch (SQLException sqe) {
			StringBuffer logMsg = new StringBuffer("SQLException in getAllRoles. \nError code: ");
			logMsg.append(sqe.getErrorCode());
			logMsg.append("\nMessage: ");
			logMsg.append(sqe.getMessage());
			logMsg.append("\nSQL state: ");
			logMsg.append(sqe.getSQLState());
			log.error(logMsg);
			log.error(sqe);
			logMsg = null;
			throw sqe;
		} catch (Exception e) {
			log.error("Exception in getAllRoles", e);
			throw e;
		} finally {
			close();
		}	
		return retVal;
	}
	
	public void delete(int userid, int roleId) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.delete"));

			setPStmtValue(1, userid);
			setPStmtValue(2, roleId);

			if (execute() != 1)
				throw new SQLException("Unable to delete the user role row.");
		} catch (Exception e) {
			log.error(".delete(" + userid + ") [Exception]", e);
			throw e;
		} finally {
			close();
		}
	}

	public int insert(int userid, int roleId) throws Exception {
		int retVal = INVALID_ID_INT;
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.insert"));

			setPStmtValue(1, userid);
			setPStmtValue(2, roleId);
				
			if (execute() != 1)
				throw new SQLException("Unable to create new user role.");
			
			retVal = getIdentityValueInt("userrolexrefid", "userrole_xref");
			
			return(retVal);
		} catch (Exception e) {
			log.error(".insert() {Exception]", e);
			throw e;
		} finally {
			close();
		}
	}
}
