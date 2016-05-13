package com.itsy.dataaccess.role;

import com.itsy.dataaccess.base.BaseVO;

public class RoleVO extends BaseVO {
	private int roleId;
	private String description;
	
	public int getRoleId() {
		return roleId;
	}
	public String getRoleIdAsString() {
		return Integer.toString(roleId);
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
