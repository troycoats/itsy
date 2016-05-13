package com.itsy.dataaccess.role;

import java.util.HashMap;
import java.util.List;

public class UserRoleXrefBO {
	
	public static HashMap<Integer, String> getRoles(int pid) throws Exception {
		return UserRoleXrefFacade.getRoles(pid);
	}
	
	public static List<Integer> getAllRoles(int pid) throws Exception {
		return UserRoleXrefFacade.getAllRoles(pid);
	}
	
	public static void delete(int pid, int roleId) throws Exception {
		UserRoleXrefFacade.delete(pid, roleId);
	}
	
	public static int insert(int pid, int roleId) throws Exception {
		return UserRoleXrefFacade.insert(pid, roleId);
	}
}
