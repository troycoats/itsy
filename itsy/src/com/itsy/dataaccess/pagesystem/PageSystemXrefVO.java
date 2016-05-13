package com.itsy.dataaccess.pagesystem;

import com.itsy.dataaccess.base.BaseVO;

public class PageSystemXrefVO extends BaseVO {
	private int pageSystemXrefId;
	private int pageId;
	private int areaId;
	private float displayOrder;
	private String parentMenu;
	
	public int getPageSystemXrefId() {
		return pageSystemXrefId;
	}
	public void setPageSystemXrefId(int pageSystemXrefId) {
		this.pageSystemXrefId = pageSystemXrefId;
	}
	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	public float getDisplayOrder() {
		return displayOrder;
	}
	public String getDisplayOrderAsString() {
		return Float.toString(displayOrder);
	}
	public void setDisplayOrder(float displayOrder) {
		this.displayOrder = displayOrder;
	}
	public int getPageId() {
		return pageId;
	}
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
	public String getParentMenu() {
		return parentMenu;
	}
	public void setParentMenu(String parentMenu) {
		this.parentMenu = parentMenu;
	}
}
