package com.itsy.util;

import javax.servlet.http.HttpServletRequest;

import com.itsy.session.URLEncryption;

public class NullUtil {
	/*
	 * helper method to guard against null pointer exceptions
	 */
	public static Object checkNull(Object a, Object b) {
		if (a == null) {
			return b;
		}
		return a;
	}
	
	public static int getCssVersion(HttpServletRequest request) {
		return URLEncryption.getParamAsInt(request, "cssVersion", 1);
	}
}
