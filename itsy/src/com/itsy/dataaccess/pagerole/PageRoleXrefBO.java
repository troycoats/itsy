package com.itsy.dataaccess.pagerole;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.page.PageBO;
import com.itsy.dataaccess.role.RoleBO;
import com.itsy.enumeration.AccessLevel;
import com.itsy.session.PageAccess;

public class PageRoleXrefBO extends PageRoleXrefVO {
	
	private static Logger log = Logger.getLogger(PageRoleXrefBO.class);

	public PageRoleXrefBO(){
		super();
	}	
	
	/**
	 * update record
	 * @param bo PageRole_XrefBO
	 * <p>
	 * @throws Exception
	 */
	public static void update(PageRoleXrefBO bo) throws Exception {
		PageRoleXrefFacade.update(bo);
	}
	
	/**
	 * delete record
	 * @param bo PageRole_XrefBO
	 * <p>
	 * @throws Exception
	 */
	public static void delete(PageRoleXrefBO bo) throws Exception {
		PageRoleXrefFacade.delete(bo);
	}
	
	/**
	 * insert record
	 * @param bo PageRole_XrefBO
	 * <p>
	 * @throws Exception
	 */
	public static void insert(PageRoleXrefBO bo, List<RoleBO> roles) throws Exception {
		if (roles != null && roles.size() > 0) {
			for (RoleBO role : roles) {
				bo.setRoleId(role.getRoleId());
				PageRoleXrefFacade.delete(bo);		// avoid duplicates
				PageRoleXrefFacade.insert(bo);
			}
		}
	}
	
	public static void insert(PageRoleXrefBO bo) throws Exception {
		PageRoleXrefFacade.delete(bo);		// avoid duplicates
		PageRoleXrefFacade.insert(bo);
	}
	
	/**
	 * This method returns all the page access roles, defn records available
	 * and store in a Map all the access roles by pageurl.
	 * @return HashMap<String,List<SimplePageAccess>>
	 * @throws Exception
	 */
	public static HashMap<String, List<PageAccess>> retrieveAllPageAccessWithRoles() throws Exception {
		return PageRoleXrefFacade.retrieveAllPageAccessWithRoles();		
	}
	
	/**
	 * This method returns page access for specific roles, defn records available
	 * and store in a Map all the access roles by pageurl.
	 * @return List<PageAccess>
	 * @throws Exception
	 */
	public static List<PageAccess> retrievePageAccessForSpecificRoles(List<Integer> roleIds, AccessLevel accessLevel) throws Exception {
		return PageRoleXrefFacade.retrievePageAccessForSpecificRoles(roleIds, accessLevel);		
	}
	
	/**
	 * This method returns page object for a specific role
	 * @param pageBO PageBO
	 * @return PageBO
	 * @throws Exception
	 */
	public static PageBO retrieveXref(PageBO pageBO) throws Exception {
		return PageRoleXrefFacade.retrieveXref(pageBO);		
	}
	
	/**
	 * This method returns page objects for a all roles
	 * @param pageBO PageBO
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public static List<PageBO> retrieveAllXref(PageBO pageBO, boolean activeOnly) throws Exception {
		return PageRoleXrefFacade.retrieveAllXref(pageBO, activeOnly);		
	}
}
