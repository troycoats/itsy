package com.itsy.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.page.PageBO;
import com.itsy.dataaccess.systemarea.SystemAreaBO;

public class SystemAreaUtil {

	private static Logger logger = Logger.getLogger(SystemAreaUtil.class);
	
	/**
	 * create a list of options with the selected system area
	 * @param String selectedOption
	 * @return String
	 */
	public static String getAreaOptions(PageBO pageBO, boolean includeBlank) {
		try {
			List<SystemAreaBO> options = SystemAreaBO.findAll();
			pageBO.setAttribute("AreaName", OptionUtil.getSelectedOptionDescription(pageBO.getAttribute("areaid"), (List) options));
			return OptionUtil.createSelectOptionList(pageBO.getAttribute("areaid"), options, null, includeBlank);
		} catch (Exception e) {
			pageBO.setAttribute("Error", e.getMessage());
			logger.error("Error occurred while retrieving system area options. " + e.getMessage(), e);
		}
		return "";
	}
}
