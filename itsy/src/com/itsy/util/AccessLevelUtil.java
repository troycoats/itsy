package com.itsy.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.accesslevel.AccessLevelBO;
import com.itsy.dataaccess.page.PageBO;

public class AccessLevelUtil {

	private static Logger logger = Logger.getLogger(AccessLevelUtil.class);
	
	/**
	 * create a list of options with the selected access level
	 * @param String selectedOption
	 * @return String
	 */
	public static String getAccessLevelOptions(PageBO pageBO) {
		try {
			List<AccessLevelBO> options = AccessLevelBO.findAll();
			return OptionUtil.createSelectOptionList(pageBO.getAttribute("accesslevelid"), options, null, true);
		} catch (Exception e) {
			pageBO.setAttribute("Error", e.getMessage());
			logger.error("Error occurred while retrieving access level options. " + e.getMessage(), e);
		}
		return "";
	}
}
