package com.itsy.dataaccess.page;

import java.net.URLEncoder;

import com.itsy.dataaccess.base.BaseVO;
import com.itsy.session.URLEncryption;

public class PageVO extends BaseVO {
	private int pageId;
	private String areaId;
	private String isAvailable;
	private String description;
	private String pageUrl;
	private String parentMenu;
	
	public String getDescription() {
		return (description != null) ? description : "";
	}
	public void setDescription(String description) {
		this.description = (description != null) ? description : "";
	}
	public String getIsAvailable() {
		return isAvailable;
	}
	public void setIsAvailable(String isAvailable) {
		this.isAvailable = isAvailable;
	}
	public String getPageIdAsString() {
		return Integer.toString(pageId);
	}
	public int getPageId() {
		return pageId;
	}
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getPageUrl() {
		return pageUrl;
	}
	public String getPageUrlEncrypted() {
		if (pageUrl.contains("?")) {
			try {
				String[] strArray = pageUrl.split("\\?");
				if (strArray != null && strArray.length == 2)
					return strArray[0] + URLEncoder.encode(URLEncryption.urlEncryptStatic(strArray[1]), "UTF-8");
			} catch (Exception e) {
				// do nothing
			}
		} 
		
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	public String getParentMenu() {
		return parentMenu;
	}
	public void setParentMenu(String parentMenu) {
		this.parentMenu = parentMenu;
	}
	
	// optionable
	public int getId() {
		return getPageId();
	}
}
