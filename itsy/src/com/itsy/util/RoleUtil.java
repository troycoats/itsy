package com.itsy.util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.page.PageBO;
import com.itsy.dataaccess.role.RoleBO;
import com.itsy.session.SessionVariables;

public class RoleUtil {
	private static Logger logger = Logger.getLogger(RoleUtil.class);
	
	static String UNKNOWN_ROLE = "UNKNOWN ROLE";
	static HashMap<Integer, String> ROLE_DESCRIPTIONS = null;
	
	public static Set<Role> ALL_ROLES = null;
	
	static {
		initializeRoles();
	}
	
	public static void initializeRoles() {
		try {
			ALL_ROLES = EnumSet.of(
				Role.ADMIN, 
				Role.END_USER
			);
		} catch (Exception e) {
			logger.error("Error occurred initializing roles: " + e.toString());
		}
	}
	
	public static void initializeDescriptions() {
		try {
			ROLE_DESCRIPTIONS = new HashMap<Integer, String>();
			
			for (Role role : ALL_ROLES) {
				ROLE_DESCRIPTIONS.put(role.roleId, role.description);
			}
		} catch (Exception e) {
			logger.error("Error occurred initializing role descriptions: " + e.toString());
		}
	}
	
	public static String getDescription(int roleId) {
		if (ROLE_DESCRIPTIONS == null) 
			initializeDescriptions();
		
		try {
			String description = ROLE_DESCRIPTIONS.get(roleId);
			return TextUtil.isEmpty(description) ? UNKNOWN_ROLE : description;
		} catch (Exception e) {
			return UNKNOWN_ROLE;
		}
	}
	
	public static enum Role {
		ADMIN 								("Admin", 		true,  1),
		END_USER 							("Itsy User",	false, 2);
				
		Role (String description, boolean displayOnly, int roleId) {
			this.roleId = roleId;
			this.description = description;
			this.displayOnly = displayOnly;
		}
		
		public int roleId;
		public String description;
		public boolean displayOnly;
		
		public static Role findById(int roleId){
		    for (Role r : values()){
		        if (r.roleId == roleId){
		            return r;
		        }
		    }
		    return null;
		}
	}
	
	public static boolean userHasRole(HttpSession session, Role... roles) {
		boolean hasRole = false;
		if (roles != null && roles.length > 0) {
			try {
				hasRole = userHasRole((List<Integer>) session.getAttribute(SessionVariables.USER_ROLE), roles);
			} catch (Exception e) {
				return false;
			}
		}
		return hasRole;
	}
	
	public static boolean userHasRole(HttpSession session, Set<Role>... roleSets) {
		boolean hasRole = false;
		if (roleSets != null && roleSets.length > 0) {
			try {
				hasRole = userHasRole((List<Integer>) session.getAttribute(SessionVariables.USER_ROLE), roleSets);
			} catch (Exception e) {
				return false;
			}
		}
		return hasRole;
	}
	
	public static boolean userHasRole(HttpSession session, String commaDeliminatedRoles) {
		String[] roles =  commaDeliminatedRoles.split(",");
		if (roles != null && roles.length > 0) {
			try {
				for (Integer userRole : (List<Integer>) session.getAttribute(SessionVariables.USER_ROLE)){
					for (String role:roles){
						if (userRole == Integer.valueOf(role.trim()))
							return true;
					}
				}
				
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	
	public static boolean userHasRole(List<Integer> userRoles, Role... roles) {
		boolean hasRole = false;
		if (roles != null && roles.length > 0) {
			try {
				for (Role role : roles) {
					if (userRoles.contains(role.roleId)) {
						hasRole = true;
						break;
					}
				}
			} catch (Exception e) {
				return false;
			}
		}
		return hasRole;
	}
	
	public static boolean userHasRole(List<Integer> userRoles, Set<Role>... roleSets) {
		boolean hasRole = false;
		if (roleSets != null && roleSets.length > 0) {
			try {
				for (Set<Role> roleSet : roleSets) {
					for (Role role : roleSet) {
						if (userRoles.contains(role.roleId)) {
							hasRole = true;
							break;
						}
					}
				}
			} catch (Exception e) {
				return false;
			}
		}
		return hasRole;
	}
	
	public static String getAllRoles() {
		StringBuilder sb = new StringBuilder();
		for (Role role : RoleUtil.ALL_ROLES) {
			if (!TextUtil.isEmpty(sb.toString()))
				sb.append(",");
			sb.append(role.roleId);
		}
		return sb.toString();
	}
	
	public static String getStringValueFromInt(int i) {
	     for (Role role : Role.values()) {
	         if (role.roleId == i) {
	             return role.toString().toLowerCase().replace("_", " ");
	         }
	     }
	     return "UNKNOWN";
	}
	
	public static Role getRole(int roleId) {
		Role validRole = null;
		for (Role role : Role.values()) {
			if (role.roleId == roleId){
				validRole = role;
				break;
			}
		}
		return validRole;
	}
	
	/**
	 * create a list of options with the selected role
	 * @param String selectedOption
	 * @return String
	 */
	public static String getRoleOptions(String selectedId, boolean includeBlank) {
		try {
			List<RoleBO> roles = RoleBO.findAll();
			return OptionUtil.createSelectOptionList(selectedId, roles, null, includeBlank);
		} catch (Exception e) {
			logger.error("Error occurred while retrieving getRoleOptions. " + e.getMessage(), e);
		}
		return "";
	}
	
	public static String getAvailableRoles(List<PageBO> activeRoles) {
		StringBuilder availableRoles = new StringBuilder();
		try {
			List<RoleBO> roles = RoleBO.findAll();
			if (roles != null && roles.size() > 0) {
				for (RoleBO role : roles) {
					boolean roleAlreadyAssigned = false;
					if (activeRoles != null && activeRoles.size() > 0) {
						for (PageBO pageBO : activeRoles) {
							if (pageBO.getAttribute("roleid").equalsIgnoreCase(role.getRoleIdAsString())) {
								roleAlreadyAssigned = true;
								break;
							}
						}
					}
					if (!roleAlreadyAssigned) {
						if (!TextUtil.isEmpty(availableRoles.toString())) 
							availableRoles.append(",");
						availableRoles.append("'" + role.getDescription() + " (" + role.getRoleId() + ")'");
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error occurred while retrieving getAvailableRoles. " + e.getMessage(), e);
		}
		return availableRoles.toString();
	}
	
	/**
	 * retrieve a list of rolesBO's from a comma delimited string of role ids
	 * @param String names
	 * @return List<RoleBO>
	 */
	public static List<RoleBO> resolveRolesFromCommaSeparated(String ids) {
		List<RoleBO> roles = new ArrayList<RoleBO>();
		try {
			String roleId;
			if (ids != null && ids.length() > 0) {
				if (ids.contains(",")) {
					StringTokenizer st = new StringTokenizer(ids, ",");
					while (st.hasMoreTokens()) {
						roleId = st.nextToken();
						roleId = roleId.substring(roleId.indexOf('(') + 1, roleId.indexOf(')'));
						RoleBO roleBO = new RoleBO();
						roleBO.setRoleId(Integer.parseInt(roleId));
						roleBO.setDescription(RoleUtil.getDescription(Integer.parseInt(roleId)));
						roles.add(roleBO);
						roleBO = null;
					}
					st = null;
				} else {
					roleId = ids.substring(ids.indexOf('(') + 1, ids.indexOf(')'));
					RoleBO roleBO = new RoleBO();
					roleBO.setRoleId(Integer.parseInt(roleId));
					roleBO.setDescription(RoleUtil.getDescription(Integer.parseInt(roleId)));
					roles.add(roleBO);
					roleBO = null;
				}
			}
		} catch (Exception e) {
		}
		return roles;
	}
	
	/**
	 * retrieve a list of role enums from a comma delimited string of role ids
	 * @param String names
	 * @return List<Role>
	 */
	public static List<Role> resolveEnumFromCommaSeparated(String ids) {
		List<Role> roles = new ArrayList<Role>();
		try {
			String roleId;
			if (ids != null && ids.length() > 0) {
				if (ids.contains(",")) {
					StringTokenizer st = new StringTokenizer(ids, ",");
					while (st.hasMoreTokens()) {
						roleId = st.nextToken();
						roleId = roleId.substring(roleId.indexOf('(') + 1, roleId.indexOf(')'));
						roles.add(Role.findById(Integer.parseInt(roleId)));
					}
					st = null;
				} else {
					roleId = ids.substring(ids.indexOf('(') + 1, ids.indexOf(')'));
					roles.add(Role.findById(Integer.parseInt(roleId)));
				}
			}
		} catch (Exception e) {
		}
		return roles;
	}
	
	public static boolean isDisplayOnly(int roleId) {
		try {
			return getRole(roleId).displayOnly;	
		} catch (Exception e) {
			return true;
		}
	}
	
	public static boolean isItsyRole(int roleId, boolean includeDisplayOnly) {
		for (Role role : ALL_ROLES) 
			if (role.roleId == roleId) 
				if (includeDisplayOnly)
					return true;
				else 
					return !role.displayOnly;

		return false; 
	}
}
