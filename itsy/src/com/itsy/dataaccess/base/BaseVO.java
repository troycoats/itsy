package com.itsy.dataaccess.base;

import java.util.HashMap;

import com.itsy.util.TextUtil;

public abstract class BaseVO implements Attributable {

	/*
	 * used for storing miscellaneous page objects
	 * 
	 */
	protected HashMap<String, String> attributes = new HashMap<String, String>();
	
	public String getAttribute(String key) {
		String returnValue = attributes.get(key.toLowerCase());
		return TextUtil.isEmpty(returnValue) ? "" : returnValue;
	}
	
	public BaseVO setAttribute(String key, String value) {
		attributes.put(key.toLowerCase(), ((value == null) ? "" : value));
		return this;
	}
	
	public HashMap<String, String> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(HashMap<String, String> attributes) {
		this.attributes = attributes;
	}
}
