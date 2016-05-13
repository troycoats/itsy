package com.itsy.dataaccess.pagerole;

import com.itsy.dataaccess.base.BaseVO;

public class PageRoleXrefVO extends BaseVO {
	private int pageRoleXrefId;
	private int pageId;
	private int roleId;
	private int accessLevelId;
	private String isAvailable;
	
	public int getPageRoleXrefId() {
		return pageRoleXrefId;
	}
	public void setPageRoleXrefId(int pageRoleXrefId) {
		this.pageRoleXrefId = pageRoleXrefId;
	}
	public int getAccessLevelId() {
		return accessLevelId;
	}
	public void setAccessLevelId(int accessLevelId) {
		this.accessLevelId = accessLevelId;
	}
	public String getIsAvailable() {
		return isAvailable;
	}
	public void setIsAvailable(String isAvailable) {
		this.isAvailable = isAvailable;
	}
	public int getPageId() {
		return pageId;
	}
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
}
