package com.itsy.dataaccess.base;

import java.util.HashMap;

public interface Attributable {
	String getAttribute(String key);
	BaseVO setAttribute(String key, String value);
	HashMap<String, String> getAttributes();
	void setAttributes(HashMap<String, String> attributes);
}
