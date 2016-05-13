package com.itsy.dataaccess.user;

import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.base.BaseDAO;
import com.itsy.dataaccess.base.DataFetch;
import com.itsy.util.SQLPropertiesUtil;

public class UserDAO extends BaseDAO {
	
	public static Logger logger = Logger.getLogger(UserDAO.class);
	
	/**
	 * Method to return UserBO by username 
	 * @param userName
	 * @return UserBO
	 * @throws Exception
	 */
	public UserBO findByUsername(String username, DataFetch fetchMode) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.by.username"));
			setPStmtValue(1, username);
			executeQuery();
			
			if (hasMoreResults()) {
				return createUserBO(fetchMode);
			}
		} catch (Exception e) {
			logger.error(".findByUsername(" + username + ") [Exception]", e);
			throw e;
		} finally {
			close();
		}
		return null;
	}
	
	/**
	 * Method to return UserBO by userId 
	 * @param userId
	 * @return UserBO
	 * @throws Exception
	 */
	public UserBO findByUserId(int userId, DataFetch fetchMode) throws Exception {
		UserBO bo = new UserBO();
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.userprofile.by.userId"));
			setPStmtValue(1, userId);
			executeQuery();
			
			if (hasMoreResults()) {
				bo = createUserBO(fetchMode);
			}
		} catch (Exception e) {
			logger.error(".findByUserId(" + userId + ") [Exception]", e);
			throw e;
		} finally {
			close();
		}
		return bo;
	}
	
	/**
	 * Method to return UserBO by userId 
	 * @param internalPid
	 * @return UserBO
	 * @throws Exception
	 */
	public UserBO findByInternalPid(int internalPid, DataFetch fetchMode) throws Exception {
		UserBO bo = new UserBO();
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.userprofile.by.internalPid"));
			setPStmtValue(1, internalPid);
			executeQuery();
			
			if (hasMoreResults()) {
				bo = createUserBO(fetchMode);
			}
		} catch (Exception e) {
			logger.error(".findByInternalPid(" + internalPid + ") [Exception]", e);
			throw e;
		} finally {
			close();
		}
		return bo;
	}
	
	/**
	 * Method to return UserBO by userId 
	 * @param userId
	 * @return UserBO
	 * @throws Exception
	 */
	public UserBO findByBarNumber(String barNumber, DataFetch fetchMode) throws Exception {
		UserBO bo = new UserBO();
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.userprofile.by.barNumber"));
			setPStmtValue(1, barNumber);
			executeQuery();
			
			if (hasMoreResults()) {
				bo = createUserBO(fetchMode);
			}
		} catch (Exception e) {
			logger.error(".findByBarNumber(" + barNumber + ") [Exception]", e);
			throw e;
		} finally {
			close();
		}
		return bo;
	}
	
	public void insertItsyUser(int userId, String firstName, String lastName) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.insert.itsyUser"));
			setPStmtValue(1, userId);
			setPStmtValue(2, firstName);
			setPStmtValue(3, lastName);
			setPStmtValue(4, new Date());
				
			if (execute() != 1)
				throw new SQLException("Unable to create new itsy user.");
		} catch (Exception e) {
			logger.error("Exception in void insertAisUser()", e);
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
	 * update existing user record
	 * @param vo UserProfileVO 
	 * <p>
	 * @throws Exception
	 */
	public void updateUser(UserBO bo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.update"));
			setPStmtValue(1,  bo.getFirstName());
			setPStmtValue(2,  bo.getMiddleName());
			setPStmtValue(3,  bo.getLastName());
			setPStmtValue(4,  bo.getAddr1());
			setPStmtValue(5,  bo.getAddr2());
			setPStmtValue(6,  bo.getAddrCity());
			setPStmtValue(7,  bo.getAddrState());
			setPStmtValue(8,  bo.getAddrZip());
			setPStmtValue(9,  bo.getPhone());
			setPStmtValue(10, bo.getEmailAddress());
			setPStmtValue(11, new Date());
			setPStmtValue(12, bo.getUserId());
			execute();
		} catch (Exception e) {
			logger.error("Exception in UserBO updateUser(UserBO bo)", e);
			throw e;
		} finally {
			try {
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void updateWorkspaceInformation(UserBO bo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.update.workspace.information"));
			setPStmtValue(1, bo.getFirstName());
			setPStmtValue(2, bo.getLastName());
			setPStmtValue(3, bo.getEmailAddress());
			setPStmtValue(4, new Date());
			setPStmtValue(5, bo.getUserId());
			execute();
		} catch (Exception e) {
			logger.error("Exception in UserBO updateUser(UserBO bo)", e);
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
	 * delete existing user record
	 * @param vo UserProfileVO 
	 * <p>
	 * @throws Exception
	 */
	public void deleteUser(UserBO bo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.delete"));
			setPStmtValue(1, bo.getUserId());
			execute();
		} catch (Exception e) {
			logger.error("Exception in UserBO deleteUser(UserBO bo)", e);
			throw e;
		} finally {
			try {
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void delete(int userId) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.delete"));
			setPStmtValue(1, userId);
			execute();
		} catch (Exception e) {
			logger.error("Exception in UserBO delete(int userId)", e);
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
	 * 
	 * @return UserBO
	 * @throws Exception
	 */
	private UserBO createUserBO(DataFetch fetchMode) throws Exception {
		UserBO UserBO = new UserBO();
		UserBO.setUserId(getRSValueInt("userid"));
		
		UserBO.setUsername(getRSValueString("username"));
		UserBO.setPassword(getRSValueString("password"));
		UserBO.setIsActive("Y".equalsIgnoreCase(getRSValueString("isactive")) ? true : false);
		UserBO.setLoginAttempts(getRSValueInt("loginattempts"));
		
		// ADD ->
		//logindate
		//lastlogindate
		
		UserBO.setFirstName(getRSValueString("firstname"));
		UserBO.setLastName(getRSValueString("lastname"));
		
		if (DataFetch.FULL.equals(fetchMode)) {
			UserBO.setEmailAddress(getRSValueString("emailaddress"));
			UserBO.setMiddleName(getRSValueString("middlename"));
			UserBO.setAddr1(getRSValueString("addrline1"));
			UserBO.setAddr2(getRSValueString("addrline2"));
			UserBO.setAddrCity(getRSValueString("addrcity"));
			UserBO.setAddrState(getRSValueString("addrstate"));
			UserBO.setAddrZip(getRSValueString("addrzip"));
			// FIX -> UserBO.setUpdateTimestamp(getRSValueDate("updatetimestamp"));
		}
		return UserBO;
	}
}
