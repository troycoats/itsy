package com.itsy.session;

public class PageAccess {
	public  String access;
	public int accessLevelId;
	public int pageId;
	public String pageName;
	public String pageUrl;
	public int roleId;
	public String roleName;
	public String systemName;
	private String isPageAvailable;
	private String isPageAvailableForRole;
	
	public String isPageAvailable() {
		return isPageAvailable;
	}
	public void setPageAvailable(String isPageAvailable) {
		this.isPageAvailable = isPageAvailable;
	}
	public String isPageAvailableForRole() {
		return isPageAvailableForRole;
	}
	public void setPageAvailableForRole(String isPageAvailableForRole) {
		this.isPageAvailableForRole = isPageAvailableForRole;
	}
	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}
	public int getAccessLevelId() {
		return accessLevelId;
	}
	public void setAccessLevelId(int accessLevelId) {
		this.accessLevelId = accessLevelId;
	}
	public int getPageId() {
		return pageId;
	}
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
}
