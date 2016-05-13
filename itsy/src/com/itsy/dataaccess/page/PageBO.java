package com.itsy.dataaccess.page;

import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.accesslevel.AccessLevelBO;
import com.itsy.dataaccess.base.Optionable;
import com.itsy.dataaccess.pagerole.PageRoleXrefBO;
import com.itsy.dataaccess.pagesystem.PageSystemXrefBO;
import com.itsy.util.RoleUtil.Role;
import com.itsy.util.TextUtil;

public class PageBO extends PageVO implements Optionable {
	private static Logger logger = Logger.getLogger(PageBO.class);
	
	public PageBO(){
		super();
	}	
	
	/**
	 * Method to return PageBO by pageid.
	 * @param pageid
	 * @return PageBO
	 * @throws Exception
	 */
	public static PageBO findByPageId(int pageId) throws Exception{
		return PageFacade.findByPageId(pageId);		
	}

	/**
	 * Method to return all PageBO records.
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public static List<PageBO> findAll() throws Exception {
		return PageFacade.findAll();		
	}
	
	public static List<PageBO> findPages() throws Exception {
		return PageFacade.findPages();		
	}
	
	/**
	 * returns the search results for the specified search criteria.
	 * @param PagePO po
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public static List<PageBO> searchByPage(PageBO bo) throws Exception {
		return PageFacade.searchByPage(bo);
	}
		
	/**
	 * returns the search results for the specified search criteria.
	 * @param PageBO bo
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public static List<PageBO> searchByRole(PageBO bo) throws Exception {
		return PageFacade.searchByRole(bo);
	}
	
	/**
	 * returns the search results for the specified search criteria.
	 * @param String pageUrl
	 * @return List<Role>
	 * @throws Exception
	 */
	public static List<Role> searchForRole(String pageUrl, AccessLevelBO level) throws Exception {
		return PageFacade.searchForRole(pageUrl, level);
	}
	
	/**
	 * returns the search results for the specified search criteria.
	 * @param PagePO po
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public static List<PageBO> searchByArea(PageBO bo) throws Exception {
		return PageFacade.searchByArea(bo);
	}

	/**
	 * inserts new record
	 * @param bo PageBO
	 * @return PageBO 
	 * <p>
	 * @throws Exception
	 */
	public static PageBO insert(PageBO bo) throws Exception {
		return PageFacade.insert(bo);
	}

	/**
	 * update record
	 * @param bo PageBO
	 * <p>
	 * @throws Exception
	 */
	public static void update(PageBO bo) throws Exception {
		PageFacade.update(bo);
	}

	/**
	 * delete record
	 * @param bo PageBO
	 * <p>
	 * @throws Exception
	 */
	public static void delete(PageBO bo) throws Exception {
		PageFacade.delete(bo);
	}
	
	/**
	 * Convert PageBO to a PageRoleXrefBO
	 * @param bo PageBO
	 * @return Object
	 */
	public static PageRoleXrefBO convertToPageRoleXrefBO(PageBO bo) {
		PageRoleXrefBO pageRole_XrefBO = new PageRoleXrefBO();
		pageRole_XrefBO.setPageId(bo.getPageId());
		pageRole_XrefBO.setRoleId(!TextUtil.isEmpty(bo.getAttribute("roleid")) ? Integer.parseInt(bo.getAttribute("roleid")) : 0);
		pageRole_XrefBO.setAccessLevelId(Integer.parseInt((!TextUtil.isEmpty(bo.getAttribute("accesslevelid")) ? bo.getAttribute("accesslevelid") : "0")));   // empty check needed when deleting record
		pageRole_XrefBO.setIsAvailable(bo.getAttribute("pageisavailableforrole"));
		return pageRole_XrefBO;
	}
	
	/**
	 * Convert PageBO to a PageSystemXrefBO
	 * @param bo PageBO
	 * @return Object
	 */
	public static PageSystemXrefBO convertToPageSystemXrefBO(PageBO bo) {
		PageSystemXrefBO pageSystem_XrefBO = new PageSystemXrefBO();
		pageSystem_XrefBO.setPageId(bo.getPageId());
		pageSystem_XrefBO.setAreaId(Integer.parseInt(bo.getAttribute("areaid")));
		pageSystem_XrefBO.setDisplayOrder(Float.parseFloat((!TextUtil.isEmpty(bo.getAttribute("displayorder")) ? bo.getAttribute("displayorder") : "0")));    // empty check needed when deleting record
		pageSystem_XrefBO.setParentMenu(bo.getAttribute("parentmenu"));
		return pageSystem_XrefBO;
	}
}
