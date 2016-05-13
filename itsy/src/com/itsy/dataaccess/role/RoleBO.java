package com.itsy.dataaccess.role;

import java.sql.Connection;
import java.util.List;

import com.itsy.dataaccess.base.Optionable;
import com.itsy.util.RoleUtil.Role;
import com.itsy.util.TextUtil;

public class RoleBO extends RoleVO implements Optionable {
	
	public RoleBO() {
		super();
	}	
	
	/**
	 * Method to return RoleBO by roleId.
	 * @param roleId
	 * @return RoleBO
	 * @throws Exception
	 */
	public static RoleBO findByRoleId(int roleId) throws Exception{
		return RoleFacade.findByRoleId(roleId);		
	}
	
	/**
	 * Method to return RoleBO by description.
	 * @param roleId
	 * @return RoleBO
	 * @throws Exception
	 */
	public static RoleBO findByRoleName(String roleName, Connection conn) throws Exception{
		return RoleFacade.findByRoleName(roleName, conn);		
	}

	/**
	 * Method to return all available RoleBOs
	 * @return List<RoleBO>
	 * @throws Exception
	 */
	public static List<RoleBO> findAll() throws Exception {
		return RoleFacade.findAll();		
	}
	
	/**
	 * Method to return all available RoleBOs
	 * @return List<RoleBO>
	 * @throws Exception
	 */
	public static List<RoleBO> findByRoleIds(String roleIds) throws Exception {
		return RoleFacade.findByRoleIds(roleIds);		
	}

	/**
	 * Method to return all available RoleBOs
	 * @return List<RoleBO>
	 * @throws Exception
	 */
	public static List<RoleBO> findByRole(Role... Role) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (Role role : Role) {
			if (!TextUtil.isEmpty(sb.toString())) {
				sb.append(",");
			}
			sb.append(role.roleId);
		}
		return findByRoleIds(sb.toString());
	}
	
	/**
	 * returns the search results for the specified search criteria.
	 * @param DefinitionPO po
	 * @return List<UserPO>
	 * @throws Exception
	 */
	public static List<RoleBO> search(RoleBO bo) throws Exception {
		return RoleFacade.search(bo);
	}
	
	/**
	 * inserts new record
	 * @param bo RoleBO
	 * @return Role_DefnVO 
	 * <p>
	 * @throws Exception
	 */
	public static RoleBO insert(RoleBO bo) throws Exception {
		return RoleFacade.insert(bo);
	}

	/**
	 * update record
	 * @param bo RoleBO
	 * <p>
	 * @throws Exception
	 */
	public static void update(RoleBO bo) throws Exception {
		RoleFacade.update(bo);
	}

	/**
	 * delete record
	 * @param bo RoleBO
	 * <p>
	 * @throws Exception
	 */
	public static void delete(RoleBO bo) throws Exception {
		RoleFacade.delete(bo);
	}
	
	public int getId() {
		return this.getRoleId();
	}
}
