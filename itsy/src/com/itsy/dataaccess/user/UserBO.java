package com.itsy.dataaccess.user;

import com.itsy.dataaccess.base.DataFetch;
import com.itsy.dataaccess.role.UserRoleXrefFacade;

public class UserBO extends UserVO {
	
	public UserBO() {
		super();
	}
	
	public static UserBO findByUsername(String username, DataFetch fetchMode) throws Exception {
		UserBO userBO = UserFacade.findByUsername(username, fetchMode);
		
		if (userBO != null && userBO.getUserId() > 0 && fetchMode == DataFetch.FULL) 
			userBO.setRoles(UserRoleXrefFacade.getAllRoles(userBO.getUserId()));
		
		return userBO;
	}
}
