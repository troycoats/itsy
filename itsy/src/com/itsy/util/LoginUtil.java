package com.itsy.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.base.DataFetch;
import com.itsy.dataaccess.user.UserBO;
import com.itsy.session.SessionVariables;
import com.itsy.session.UserTrackingManager;

public class LoginUtil implements SessionVariables {

	private static Logger log = Logger.getLogger(LoginUtil.class);
	
	public static UserBO validate(String username, String password) throws Exception {
		boolean valid = false;
		UserBO userBO = null;
		try {
			userBO = UserBO.findByUsername(username, DataFetch.FULL);
			if (userBO != null) 
				valid = userBO.getPassword().equals(password);
		} catch (Exception e) {
			log.error(e.getMessage() + " " + e.getStackTrace());
			throw e;
		}
		return (valid) ? userBO : null;
	}
	
	public static void writeUserToSession(UserBO userBO, HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute(USER_NAME, userBO.getUsername());
		session.setAttribute(USER_ID, userBO.getUserId());
		session.setAttribute(USER_ROLE, userBO.getRoles());
		session.setAttribute(FULL_NAME, userBO.getFullName());
		
		UserTrackingManager.getInstance().addSession(userBO.getUsername(), session.getId(), request.getRemoteAddr());
		
		session = null;
	}
		
	public static boolean isValidUser(UserBO userBO) {
		return userBO != null && userBO.getRoles() != null && userBO.getRoles().size() > 0;
	}
	
	public static void updateLastLogin(String logname) throws Exception {
		try {
			// TODO
		} catch (Exception e) {
			throw e;
		}
	}
}
