package com.itsy.util;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.page.PageBO;
import com.itsy.dataaccess.pagerole.PageRoleXrefBO;
import com.itsy.dataaccess.pagesystem.PageSystemXrefBO;
import com.itsy.dataaccess.systemarea.SystemAreaBO;
import com.itsy.enumeration.AccessLevel;
import com.itsy.session.PageAccess;
import com.itsy.session.SessionVariables;

public class MenuUtil {

	private static Logger logger = Logger.getLogger(MenuUtil.class);
	
	private StringBuilder systemAreasHtml = null;
	private StringBuilder javascriptVariables = null;
	
	public MenuUtil(HttpSession session) {
		createMenu(session);
	}
	
	/**
	 * create the menu content
	 */
	private void createMenu(HttpSession session) {
		if (session != null) {
			try {
				boolean hasSplitMenu = false;
				
				systemAreasHtml = new StringBuilder();
				javascriptVariables = new StringBuilder();
				int k = 0;
				
				systemAreasHtml.append("<td>&nbsp;&nbsp;");
				
				// get a list of role ids to get the pages
				List<Integer> roleIds = (List<Integer>) session.getAttribute(SessionVariables.USER_ROLE);
				
				List<PageAccess> pages = null;
				try {
					pages = PageRoleXrefBO.retrievePageAccessForSpecificRoles(roleIds, AccessLevel.UPDATE);
				} catch(Exception e) {
					logger.error("Error creating menu - " + e.getMessage() + " " + e.getStackTrace());
				}
				if (pages != null && pages.size() > 0) {
					int[] pageIds = PageUtil.getPageIds(pages);
											
					// get a list of system area ids to get the system areas
					List<SystemAreaBO> systemAreas = PageSystemXrefBO.findSystemAreasByPageIds(pageIds);
					List<PageBO> allPages = PageSystemXrefBO.findPages(pageIds);
					
					HashMap<String, String> unique = new HashMap<String, String>();
					if (systemAreas != null && systemAreas.size() > 0) {
						for (SystemAreaBO systemArea : systemAreas) {
							if (!unique.containsKey(systemArea.getAreaIdAsString())) {
								
								if (!hasSplitMenu) {
									if (systemArea.getId() == 3) {  // admin
										systemAreasHtml.append("</td><td align='right'>");
										hasSplitMenu = true;	
									}
								}
								
								systemAreasHtml.append("<a class=\"navButton\" name=\"A" + k + "\" id=\"A" + k + "\"><input name=\"btn" + k + "\" id=\"btn" + k + "\" type=\"button\" " + getButtonClass(systemArea.getId()) + " value=\"" + systemArea.getDescription() + "\" onclick=\"setMain(this);\" /></a>&nbsp;");
								unique.put(systemArea.getAreaIdAsString(), systemArea.getAreaIdAsString());
								k++;
								
								// get the pages for this system area
								List<PageBO> pagesByArea = PageUtil.getPagesForArea(allPages, systemArea.getAreaIdAsString());
								if (pagesByArea != null && pagesByArea.size() > 0) { 
									javascriptVariables.append("var subArray = new Array(); ");
									for (PageBO page : pagesByArea) {
										javascriptVariables.append("subArray.push('" + getParentMenu(page) + "'); ");
									}
									javascriptVariables.append("menuArray.push(subArray); ");
								}
								
								systemAreasHtml.append("&nbsp;<span class='menuDivider' style='float: none;'></span>&nbsp;&nbsp;&nbsp;");
							}
						}

					}
				}
				
				if (!hasSplitMenu) {
					systemAreasHtml.append("</td><td align='right'>");
				}
			
				// logout servlet
				javascriptVariables.append("var subArray = new Array(); ");
				javascriptVariables.append("subArray.push('/itsy/logout'); ");
				javascriptVariables.append("menuArray.push(subArray); ");
				systemAreasHtml.append("<a class=\"navButton\" name=\"A" + k + "\" id=\"A" + k + "\"><input name=\"btn" + k + "\" id=\"btnLogout\" type=\"button\" class=\"navButton\" value=\"Logout\" onclick=\"top.location.href='/itsy/logout';\" /></a>&nbsp;");
				
				systemAreasHtml.append("&nbsp;</td>");
			} catch (Exception e) {
				logger.error(".createJavascript MenuUtil " + e.getMessage());
			}
		}
	}
	
	/**
	 * return the css class for the given button
	 * @return String
	 */
	public String getButtonClass(int buttonId) {
		String css = "";
		int buttonWidth = 50;
		switch (buttonId) {
			case 3:  buttonWidth = 60;
					 css = "class='navButton navDropdown' style='width: " + buttonWidth + "px; background-position: " + (buttonWidth - 14) + "px 10px;'";	// Admin
					 break;
					 
			case 8:  buttonWidth = 90;
					 css = "class='navButton navDropdown' style='width: " + buttonWidth + "px; background-position: " + (buttonWidth - 15) + "px 10px;'";	// Documents
					 break;
					 
			case 9:  buttonWidth = 60;
					 css = "class='navButton navDropdown' style='width: " + buttonWidth + "px; background-position: " + (buttonWidth - 15) + "px 10px;'";	// Cases
				     break;
				     
			case 10: buttonWidth = 68;
					 css = "class='navButton navDropdown' style='width: " + buttonWidth + "px; background-position: " + (buttonWidth - 15) + "px 10px;'";	// Reports
					 break;
					 
			case 13: buttonWidth = 70;
					 css = "class='navButton navDropdown' style='width: " + buttonWidth + "px; background-position: " + (buttonWidth - 14) + "px 10px;'";	// Training
			 		 break;
			 		 
			default: css = "class='navButton' style='width: " + buttonWidth + "px;'";
					 break;	
		}
		return css;
	}
	
	/**
	 * return the html needed for the system area tabs
	 * @return String
	 */
	public String getSystemAreasHtml() {
		return (systemAreasHtml != null) ? systemAreasHtml.toString() : "";
	}
	
	/**
	 * return the javascript variables needed for the menu items
	 * @return String
	 */
	public String getJavascriptVariables() {
		return (javascriptVariables != null) ? javascriptVariables.toString() : "";
	}
	
	public String getParentMenu(PageBO page) {
		String parentMenu = page.getParentMenu();
		return (!TextUtil.isEmpty(parentMenu) ? (parentMenu + "|") : "") + page.getPageUrlEncrypted();
	}
}
