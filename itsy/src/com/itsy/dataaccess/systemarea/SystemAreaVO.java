package com.itsy.dataaccess.systemarea;

import com.itsy.dataaccess.base.BaseVO;


public class SystemAreaVO extends BaseVO {
	private int areaId;
	private String description;

	public int getAreaId() {
		return areaId;
	}
	public String getAreaIdAsString() {
		return Integer.toString(areaId);
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	public String getDescription() {
		return description;
	}
	public SystemAreaBO setDescription(String description) {
		this.description = description;
		return (SystemAreaBO) this;
	}
	
	// optionable
	public int getId() {
		return getAreaId();
	}
}
