package com.itsy.servlets;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.itsy.config.XMLConfig;
import com.itsy.constants.Constants;
import com.itsy.dataaccess.page.PageBO;
import com.itsy.dataaccess.pagerole.PageRoleXrefBO;
import com.itsy.dataaccess.pagesystem.PageSystemXrefBO;
import com.itsy.exception.TooManyResultsException;
import com.itsy.session.PageMode;
import com.itsy.session.PagePermissions;
import com.itsy.session.URLEncryption;
import com.itsy.util.AccessLevelUtil;
import com.itsy.util.PageUtil;
import com.itsy.util.RoleUtil;
import com.itsy.util.SystemAreaUtil;
import com.itsy.util.TextUtil;

/**
 * Servlet implementation class for Servlet: DefineServlet
 *
 */
@WebServlet("/PageServlet")
public class PageServlet extends BaseServlet {
	private static final long serialVersionUID = 568946222;
	
	private static Logger logger = Logger.getLogger(PageServlet.class);
	
	private static final String PERMISSIONS = "/itsy/PageServlet";
	
	private static final String MODIFY_PAGE = "/jsp/modifyPage.jsp";
	private static final String FIND_PAGE = "/jsp/findPage.jsp";
	private static final String MODIFY_ROLE = "/jsp/modifyPageRoleXref.jsp";
	private static final String MODIFY_AREA = "/jsp/modifyPageSystemXref.jsp";
	private static final String CLOSE_AND_REFRESH_PAGE = "/jsp/closeAndRefresh.jsp";
	private static final String ERROR_PAGE = "/jsp/errorPage.jsp";
	
	public static final String SEARCH_TYPE = "search_type";
	public static final String SEARCH_BY_PAGE = "Page";
	public static final String SEARCH_BY_ROLE = "Role";
	public static final String SEARCH_BY_AREA = "Area";
	
	public PageServlet() {
		super();
	}
	
	/**
	 * handle servlet requests
	 * @param request HttpServletRequest 
	 * @param response HttpServletResponse
	 * @return void
	 */
	protected void performTask(HttpServletRequest request, HttpServletResponse response) {
		PageMode mode = PageMode.resolveEnumFromString(URLEncryption.getParamAsString(request, "mode"));
		switch (mode) {
			case FIND:    find(request, response);   	break;
			case EDIT:    edit(request, response);   	break;
			case ADD:     add(request, response);    	break;
			case DELETE:  delete(request, response); 	break;
		}
	}
	
	/**
	 * Forward request to the FindPage.jsp
	 * @param request HttpServletRequest 
	 * @param response HttpServletResponse
	 * @return void
	 */
	private void find(HttpServletRequest request, HttpServletResponse response) {
		PageBO pageBO = getFromRequest(request);

		String searchType = URLEncryption.getParameter(request, SEARCH_TYPE);
		boolean searchByPage = SEARCH_BY_PAGE.equalsIgnoreCase(searchType);
		boolean searchByRole = SEARCH_BY_ROLE.equalsIgnoreCase(searchType);
		boolean searchByArea = SEARCH_BY_AREA.equalsIgnoreCase(searchType);
		
		pageBO.setAttribute(SEARCH_TYPE, (TextUtil.isEmpty(searchType) ? SEARCH_BY_PAGE : searchType));
		
		List<PageBO> results = new ArrayList<PageBO>();
		try {
			// only perform search when initiated by the user
	    	if ("true".equalsIgnoreCase(URLEncryption.getParameter(request, "requested"))) {
			    try {
			    	if (searchByPage) {
			    		results = PageBO.searchByPage(pageBO);
			    	} else if (searchByRole) {
			    		results = PageBO.searchByRole(pageBO);
			    	} else if (searchByArea) {
			    		results = PageBO.searchByArea(pageBO);
			    	}
			    	if (results.size() == 0) {
			    		request.setAttribute("tooMany", XMLConfig.getProperty("PROPERTY.no_results_message"));
			    	}					
			    } catch (TooManyResultsException tme) {
			    	request.setAttribute("tooMany", XMLConfig.getProperty("PROPERTY.max_results_message"));
			    }
	    	}
		} catch (Exception e) {
			logger.error("Exception retrieving the results for the search criteria ", e);
			pageBO.setAttribute("Error", e.getMessage());
		}
		
		pageBO = addPageObjects(pageBO);
		
		request.setAttribute(Constants.RESULTS, results);
		request.setAttribute("searchCriteria", pageBO);
		
		try {
			redirect(FIND_PAGE, PERMISSIONS, request, response);
		} catch (Throwable theException) {
			logger.error("Exception retrieving page information");		
		}
		pageBO = null;
		results = null;
	}
	
	/**
	 * Forward edit request to the ModifyPage.jsp
	 * @param request HttpServletRequest 
	 * @param response HttpServletResponse
	 * @return void
	 */
	private void edit(HttpServletRequest request, HttpServletResponse response) {
		PageBO pageBO = getFromRequest(request);
		
		String requestMode = URLEncryption.getParameter(request, "mode");
		String requestAction = URLEncryption.getParameter(request, "action");
		boolean modifyRole = requestMode.contains("Role");
		boolean modifyArea = requestMode.contains("Area");
		boolean saveNew = "true".equals(URLEncryption.getParameter(request, "saveNew"));
		boolean closePopup = "true".equals(URLEncryption.getParameter(request, "closePopup"));
		
		PageMode mode = null;
		if (modifyRole) {
			mode = "addRole".equalsIgnoreCase(requestAction) ? PageMode.ADD : PageMode.EDIT;
		} else if (modifyArea) {
			mode = "addArea".equalsIgnoreCase(requestAction) ? PageMode.ADD : PageMode.EDIT;
		} else {
			mode = PageMode.determineMode(pageBO.getPageId());
		}
		
		try {
			String errorMsg = "";
			try {
				if (!"Y".equals(URLEncryption.getParamAsString(request, Constants.FIRST_TIME))) {
					switch (mode) {
						case ADD:
							if (modifyRole) {
								String assignedRoles = URLEncryption.getParameter(request, "assigned_roles");
								if (!"UNCHANGED".equals(assignedRoles) && assignedRoles != null) {
									PageRoleXrefBO.insert(PageBO.convertToPageRoleXrefBO(pageBO), RoleUtil.resolveRolesFromCommaSeparated(assignedRoles));
								}
							} else if (modifyArea) {
								PageSystemXrefBO.insert(PageBO.convertToPageSystemXrefBO(pageBO));
							} else {
								pageBO = PageBO.insert(pageBO);
							}
							break;
								
						case EDIT:
							if (modifyRole) {
								PageRoleXrefBO.update(PageBO.convertToPageRoleXrefBO(pageBO));
							} else if (modifyArea) {
								PageSystemXrefBO.update(PageBO.convertToPageSystemXrefBO(pageBO));
							} else {
								PageBO.update(pageBO);
							}
							break;
					}
					
					PagePermissions.buildCache();   // update the cached permissions
				}
			} catch (Exception e) {
				logger.error("Exception occurred while saving page information");
				errorMsg = checkErrorMessage(modifyRole, modifyArea, "Exception while saving the information page: " + e.getMessage());
			}
			
			if ("".equals(errorMsg)) {
				if (saveNew) {
					pageBO = getFromDb(pageBO, request);
					pageBO.setAttribute("roleid", "");
					pageBO.setAttribute("areaid", "");
					pageBO.setAttribute("accesslevelid", "");
					pageBO = addPageObjects(pageBO);
					request.setAttribute("PageInfo", pageBO);
					redirect(getRedirectPage(modifyRole, modifyArea) + "?mode=add", PERMISSIONS, request, response);
				} else if (closePopup) {
					redirect(CLOSE_AND_REFRESH_PAGE, PERMISSIONS, request, response);
				} else {
					pageBO = getFromDb(pageBO, request);
					pageBO = addPageObjects(pageBO);
					pageBO = addReferencedCollections(pageBO, modifyRole, modifyArea, request);
					request.setAttribute("PageInfo", pageBO);
					redirect(getRedirectPage(modifyRole, modifyArea), PERMISSIONS, request, response);
				}
			} else {
				// error handling
				try {
					pageBO = getFromDb(pageBO, request);
					pageBO = addPageObjects(pageBO);
					request.setAttribute("PageInfo", pageBO.setAttribute("Error", errorMsg));
					String redirectMode = PageMode.ADD.equals(mode) ? "?mode=add" : "?mode=edit";
					redirect(getRedirectPage(modifyRole, modifyArea) + redirectMode, PERMISSIONS, request, response);
				} catch (Throwable theException) {
					logger.error("Exception occurred while saving page information");
				}	
			}

		} catch (Exception e) {
			
			request.setAttribute("Error", e.getMessage());
			try {
				redirect(ERROR_PAGE, PERMISSIONS, request, response);
			} catch (Throwable theException) {
				logger.error("Exception occurred while saving page information");
			}	
			
		}
		pageBO = null;
	}
	
	/**
	 * obtain the page to redirect the request
	 * @param boolean modifyRole 
	 * @param boolean modifyArea
	 * @return String
	 */
	private String getRedirectPage(boolean modifyRole, boolean modifyArea) {
		if (modifyRole) 
			return MODIFY_ROLE; 
		else if (modifyArea) 
			return MODIFY_AREA; 
		else  
			return MODIFY_PAGE;
	}
	
	/**
	 * add information to the request object to show xref dependencies
	 * @param boolean modifyRole 
	 * @param boolean modifyArea
	 * @return String
	 */
	private PageBO addReferencedCollections(PageBO pageBO, boolean modifyRole, boolean modifyArea, HttpServletRequest request) {
		if (!modifyRole && !modifyArea) {
			try {
				request.setAttribute("ReferencedRoles", PageRoleXrefBO.retrieveAllXref(pageBO, false));
				request.setAttribute("ReferencedAreas", PageSystemXrefBO.retrieveAllXref(pageBO));
			} catch (Exception ex) {
				pageBO.setAttribute("Error", ex.getMessage());
			}
		}
		return pageBO;
	}
	
	/**
	 * filter the error message to make error message more relevant
	 * @param boolean modifyRole 
	 * @param boolean modifyArea
	 * @param String errorMessage
	 * @return String
	 */
	private String checkErrorMessage(boolean modifyRole, boolean modifyArea, String errorMessage) {
		final String DUPLICATE_ROLE_ERR_MSG = "Page already exists for this Role, pk (pageId and roleId)";
		final String DUPLICATE_AREA_ERR_MSG = "Page already exists for this Area, pk (pageId and roleId)";
		
		if (errorMessage.contains("Unique")){
			errorMessage = modifyRole ? DUPLICATE_ROLE_ERR_MSG : DUPLICATE_AREA_ERR_MSG;
		} else if (errorMessage.contains("still being referenced")) {
			errorMessage = "Record cannot be deleted, it is associated with other records.";
		}
		return errorMessage;
	}
	
	/**
	 * Forward request to the ModifyPage.jsp
	 * @param request HttpServletRequest 
	 * @param response HttpServletResponse
	 * @return void
	 */
	private void add(HttpServletRequest request, HttpServletResponse response) {
		try {
			String mode = URLEncryption.getParameter(request, "mode");
			boolean addRole = "addRole".equalsIgnoreCase(mode);
			boolean addArea = "addArea".equalsIgnoreCase(mode);
			
			request.setAttribute("PageInfo", addPageObjects(getFromRequest(request)));
			redirect(getRedirectPage(addRole, addArea), PERMISSIONS, request, response);
		} catch (Throwable theException) {
			logger.error("Exception occurred while adding page");
		}
	}
	
	/**
	 * handle request to delete
	 * @param request HttpServletRequest 
	 * @param response HttpServletResponse
	 * @return void
	 */
	private void delete(HttpServletRequest request, HttpServletResponse response) {
		PageBO pageBO = getFromRequest(request);
		String redirectPage = CLOSE_AND_REFRESH_PAGE;
		
		String mode = URLEncryption.getParameter(request, "mode");
		boolean deleteRole = "deleteRole".equalsIgnoreCase(mode);
		boolean deleteArea = "deleteArea".equalsIgnoreCase(mode);

		try {
			
			if (deleteRole) {
				PageRoleXrefBO.delete(PageBO.convertToPageRoleXrefBO(pageBO));
			} else if (deleteArea) {
				PageSystemXrefBO.delete(PageBO.convertToPageSystemXrefBO(pageBO));
			} else {
				PageBO.delete(pageBO);
			}
			
		} catch (Exception e) {
			logger.error("Exception deleting record ", e);
			String errorMessage = checkErrorMessage(deleteRole, deleteArea, e.getMessage());
			pageBO = getFromDb(pageBO, request);
			pageBO = addPageObjects(pageBO);
			request.setAttribute("PageInfo", pageBO.setAttribute("Error", errorMessage));
			redirectPage = getRedirectPage(deleteRole, deleteArea);
		}
		
		try {
			redirect(redirectPage, PERMISSIONS, request, response);
		} catch (Throwable theException) {
			logger.error("Exception occurred while deleting page information");
		}
		pageBO = null;
	}
	
	/**
	 * obtain the information from the request
	 * @param request HttpServletRequest 
	 * @return PageBO
	 */
	private PageBO getFromRequest(HttpServletRequest request) {
		PageBO pageBO = new PageBO();
		try {
			try {
				int pageId = URLEncryption.getParamAsInt(request, "id");
				if (pageId == 0) {
					pageId = URLEncryption.getParamAsInt(request, "pageId");
				}
				pageBO.setPageId(pageId);
			} catch (Exception e) {
				pageBO.setPageId(0);
			}

			if ("Y".equals(URLEncryption.getParamAsString(request, Constants.FIRST_TIME))) {
				// load from db
				pageBO = getFromDb(pageBO, request);
			} else {
				pageBO.setDescription(URLEncryption.getParamAsString(request, "entry_description"));
				pageBO.setIsAvailable(URLEncryption.getParamAsString(request, "isAvailable"));
				pageBO.setPageUrl(URLEncryption.getParamAsString(request, "pageUrl"));

				// role
				pageBO.setAttribute("roleid", URLEncryption.getParamAsString(request, "roleId"));
				pageBO.setAttribute("accesslevelid", URLEncryption.getParamAsString(request, "accessLevelId"));
				pageBO.setAttribute("pageisavailableforrole", URLEncryption.getParamAsString(request, "isAvailable"));
				
				// area
				pageBO.setAttribute("areaid", URLEncryption.getParamAsString(request, "areaId"));
				pageBO.setAttribute("displayorder", URLEncryption.getParamAsString(request, "displayOrder"));
				pageBO.setAttribute("parentmenu", URLEncryption.getParamAsString(request, "parentMenu"));
			}
		} catch (Exception e) {
			logger.error(".getFromRequest() [Exception]", e);
		}
		return pageBO;
	}
	
	/**
	 * obtain the information from the database
	 * @param pageBO PageBO 
	 * @return PageBO
	 */
	private PageBO getFromDb(PageBO pageBO, HttpServletRequest request) {
		try {
			pageBO = PageBO.findByPageId(pageBO.getPageId());
			
			// role
			pageBO.setAttribute("roleid", URLEncryption.getParamAsString(request, "roleId"));
			if (!TextUtil.isEmpty(pageBO.getAttribute("roleid"))) {
				// retrieve the pagerole_xref from the db 
				pageBO = PageRoleXrefBO.retrieveXref(pageBO);
			}
			
			// area
			pageBO.setAttribute("areaid", URLEncryption.getParamAsString(request, "areaId"));
			if (!TextUtil.isEmpty(pageBO.getAttribute("areaid"))) {
				// retrieve the pagesystem_xref from the db 
				pageBO = PageSystemXrefBO.retrieveXref(pageBO);
			}
		} catch (Exception e) {
			logger.error(".getFromDb() [Exception]", e);
		}	
		return pageBO;
	}
	
	/**
	 * add page objects needed for the UI
	 * @param pagePo PagePo 
	 * @return PagePo
	 */
	private PageBO addPageObjects(PageBO pageBO) {
		// role name
		String roleId = pageBO.getAttribute("roleid");
		if (!TextUtil.isEmpty(roleId)) {
			try {
				pageBO.setAttribute("RoleName", RoleUtil.getDescription(Integer.parseInt(roleId)));
			} catch (Exception e) {
				logger.error(".addPageObjects() [Exception]", e);
			}
		}
		
		List<PageBO> activeRoles = null;
		try {
			activeRoles = PageRoleXrefBO.retrieveAllXref(pageBO, true);
		} catch (Exception e) {
			// do nothing
		}
		
		pageBO.setAttribute("AvailableRoles", RoleUtil.getAvailableRoles(activeRoles));
		pageBO.setAttribute("RoleOptions", RoleUtil.getRoleOptions(pageBO.getAttribute("roleid"), false));
		pageBO.setAttribute("AssignedRoles", "");
		pageBO.setAttribute("AvailableOptionsForRole", PageUtil.getAvailableOptionsForRole(pageBO));
		pageBO.setAttribute("AccessLevelOptionsForRole", AccessLevelUtil.getAccessLevelOptions(pageBO));
		pageBO.setAttribute("AreaOptions", SystemAreaUtil.getAreaOptions(pageBO, false));
		pageBO.setAttribute("AvailableOptions", PageUtil.getAvailableOptions(pageBO));
		pageBO.setAttribute("PageOptions", PageUtil.getPageOptions(pageBO));
		return pageBO;
	}
}
