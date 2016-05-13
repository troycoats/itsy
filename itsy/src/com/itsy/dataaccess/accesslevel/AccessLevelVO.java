package com.itsy.dataaccess.accesslevel;

import com.itsy.dataaccess.base.BaseVO;

public class AccessLevelVO extends BaseVO {
	private int accessLevelId;
	private String description;
	
	public int getAccessLevelId() {
		return accessLevelId;
	}
	public void setAccessLevelId(int accessLevelId) {
		this.accessLevelId = accessLevelId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	// optionable
	public int getId() {
		return getAccessLevelId();
	}
}
