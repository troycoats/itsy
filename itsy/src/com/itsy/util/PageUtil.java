package com.itsy.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.page.PageBO;
import com.itsy.session.PageAccess;

public class PageUtil {

	private static Logger logger = Logger.getLogger(PageUtil.class);
	
	/**
	 * create a list of options for all pages
	 * @param String selectedOption
	 * @return String
	 */
	public static String getPageOptions(PageBO pageBO) {
		try {
			List<PageBO> options = PageBO.findAll();
			return OptionUtil.createSelectOptionList(pageBO.getPageIdAsString(), options, null, true);
		} catch (Exception e) {
			pageBO.setAttribute("Error", e.getMessage());
			logger.error("Error occurred while retrieving page options. " + e.getMessage(), e);
		}
		return "";
	}
	
	/**
	 * create options with the selected availability for the given page
	 * @param PageBO
	 * @return String
	 */
	public static String getAvailableOptions(PageBO pageBO) {
		try {
			String availability = "N".equalsIgnoreCase(pageBO.getIsAvailable()) ? "N" : "Y";
			
			StringBuilder returnValue = new StringBuilder();
			returnValue.append("<option " + TextUtil.isSelected(availability, "Y") + " value=\"Y\">Y</option>");
			returnValue.append("<option " + TextUtil.isSelected(availability, "N") + " value=\"N\">N</option>");
			return returnValue.toString();		
		} catch (Exception e) {
			pageBO.setAttribute("Error", e.getMessage());
			logger.error("Error occurred while retrieving availability. " + e.getMessage(), e);
		}
		return "";
	}
	
	/**
	 * create options with the selected availability for the given role
	 * @param PageBO
	 * @return String
	 */
	public static String getAvailableOptionsForRole(PageBO pageBO) {
		try {
			String availability = "N".equalsIgnoreCase(pageBO.getAttribute("pageisavailableforrole")) ? "N" : "Y";
			
			StringBuilder returnValue = new StringBuilder();
			returnValue.append("<option " + TextUtil.isSelected(availability, "Y") + " value=\"Y\">Y</option>");
			returnValue.append("<option " + TextUtil.isSelected(availability, "N") + " value=\"N\">N</option>");
			return returnValue.toString();		
		} catch (Exception e) {
			pageBO.setAttribute("Error", e.getMessage());
			logger.error("Error occurred while retrieving availability for role. " + e.getMessage(), e);
		}
		return "";
	}
	
	public static int[] getPageIds(List<PageAccess> pages) {
		int[] pageIds = new int[pages.size()];
		int j = 0;
		for (PageAccess access : pages) {
			pageIds[j++] = access.getPageId();
		}
		return pageIds;
	}
	
	/**
	 * return the pages for the given system area
	 * param List<PageBO> allPages
	 * param String areaId
	 * @return List<PageBO>
	 */
	public static List<PageBO> getPagesForArea(List<PageBO> allPages, String areaId) {
		List<PageBO> pagesForArea = new ArrayList<PageBO>();
		for (PageBO page : allPages) {
			if (page.getAreaId().equals(areaId)) {
				pagesForArea.add(page);
			}
		}
		return pagesForArea;
	}
	
	public static class PageComparator implements Comparator<PageBO> {
		public int compare(PageBO p1, PageBO p2) {
	        return p1.getDescription().toUpperCase().compareTo(p2.getDescription().toUpperCase());
	    }
	}
}
