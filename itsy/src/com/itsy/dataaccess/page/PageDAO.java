package com.itsy.dataaccess.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.accesslevel.AccessLevelBO;
import com.itsy.dataaccess.base.BaseDAO;
import com.itsy.util.RoleUtil;
import com.itsy.util.RoleUtil.Role;
import com.itsy.util.SQLPropertiesUtil;
import com.itsy.util.TextUtil;

public class PageDAO extends BaseDAO {

	private static Logger logger = Logger.getLogger(PageDAO.class);
	
	/**
	 * Method to return PageBO by pageid.
	 * @param pageId
	 * @return PageBO
	 * @throws Exception
	 */
	public PageBO findByPageId(int pageId) throws Exception {
		PageBO bo = new PageBO();
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.by.pageid"));
			setPStmtValue(1, pageId);
			executeQuery();
			
			if (hasMoreResults()) {
				bo = createPageBO();
			}
		} catch (Exception e) {
			logger.error(". findByPageId(int pageId) [Exception]", e);
			throw e;
		} finally {
			close();
		}
		return bo;
	}
	
	/**
	 * Method to return all Page_DefnBO records.
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public List<PageBO> findAll() throws Exception {
		List<PageBO> retVal = new ArrayList<PageBO>();
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.all"));
			executeQuery();
			
			while (hasMoreResults()) {
				retVal.add(createPageBO());
			}
		} catch (Exception e) {
			logger.error(".findAll() [Exception]", e);
			throw e;
		} finally {
			close();
		}
		return retVal;
	}
	
	public List<PageBO> findPages() throws Exception {
		List<PageBO> pages = new ArrayList<PageBO>();
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.pages"));
			executeQuery();
			
			while (hasMoreResults()) {
				pages.add(createPageBO(getRSValueString("description"), getRSValueString("pageurl"), getRSValueString("title")));
			}
		} catch (Exception e) {
			logger.error(".findPages() [Exception]", e);
			throw e;
		} finally {
			close();
		}
		return pages;
	}
	
	/**
	 * 
	 * @return PageBO
	 * @throws Exception
	 */
	private PageBO createPageBO() throws Exception {
		PageBO pageBO = new PageBO();
		pageBO.setPageId(getRSValueInt("pageid"));
		pageBO.setIsAvailable(getRSValueString("isavailable"));
		pageBO.setDescription(getRSValueString("description"));
		pageBO.setPageUrl(getRSValueString("pageurl"));
		return pageBO;
	}
	
	private PageBO createPageBO(String description, String url, String title) throws Exception {
		PageBO pageBO = new PageBO();
		pageBO.setDescription(description);
		pageBO.setPageUrl(url);
		return pageBO;
	}
		
	/**
	 * returns the search results for the specified search criteria.
	 * @param PageBO bo
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public List<PageBO> searchByPage(PageBO bo) throws Exception {
		List<PageBO> retVal = new ArrayList<PageBO>();
		
		StringBuilder fields = new StringBuilder();
		fields.append("select p.*, a.description as areadescription, a.areaid, x.displayorder, x.parentmenu ");
		
		ArrayList<String>tables = new ArrayList<String>();
		ArrayList<String>join = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>psValues = new ArrayList<String>();
		
		// add in the base tables and linking fields
		tables.add("page_defn p");
		
		join.add("left outer join pagesystem_xref x on p.pageid = x.pageid"); 
		join.add("left outer join systemarea_defn a on x.areaid = a.areaid");
		
		if (!TextUtil.isEmpty(bo.getDescription())) {
			where.add("lower(p.description) like ?");
			psValues.add("%" +(bo.getDescription().toLowerCase()) + "%");
		} 
		
		if (!TextUtil.isEmpty(bo.getPageUrl())) {
			where.add("lower(p.pageurl) like ?");
			psValues.add("%" +(bo.getPageUrl().toLowerCase()) + "%");
		} 		
		
		String sql = buildSQL(fields.toString(), tables, join, where) + " order by x.displayorder";
		logger.debug("dynamic sql [" + sql + "]");
		
		try {
			doSearchNoException(sql, psValues);

			while (hasMoreResults()) {
				retVal.add(createPageObject());
			}
		} catch (Exception e) {
			logger.error("Exception in search", e);
			bo.setAttribute("Error", e.getMessage());
			throw e;
		} finally {
			close();
		}	
		
		fields = null;
		tables = null;
		join = null;
		where = null;
		psValues = null;
		
		return retVal;
	}
	
	/**
	 * returns the search results for the specified search criteria.
	 * @param PageBO bo
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public List<PageBO> searchByRole(PageBO bo) throws Exception {
		List<PageBO> results = new ArrayList<PageBO>();
		
		StringBuilder fields = new StringBuilder();
		fields.append("select page_defn.pageid, page_defn.isavailable as pageisavailable, page_defn.description as pagedescription, page_defn.pageurl, ");
		fields.append("pagerole_xref.isavailable as pageisavailableforrole, pagerole_xref.roleid, ");
		fields.append("accesslevel_defn.description as accessleveldescription");
		
		ArrayList<String>tables = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>psValues = new ArrayList<String>();
		
		// add in the base tables and linking fields
		tables.add("page_defn");
		tables.add("pagerole_xref");
		tables.add("accesslevel_defn");
		
		// build the dynamic sql
		where.add("pagerole_xref.pageid = page_defn.pageid");
		where.add("accesslevel_defn.accesslevelid = pagerole_xref.accesslevelid");
		where.add("pagerole_xref.roleid = " + bo.getAttribute("roleid"));
		
		String sql = buildSQL(fields.toString(), tables, where) + " order by pagerole_xref.roleid";
		logger.debug("dynamic sql [" + sql + "]");
		
		try {
			doSearchNoException(sql, psValues);

			while (hasMoreResults()) {
				results.add(createSearchByRoleObject());
			}
		} catch (Exception e) {
			logger.error("Exception in search", e);
			throw e;
		} finally {
			close();
		}	
		
		fields = null;
		tables = null;
		where = null;
		psValues = null;
		
		return results;
	}
	
	/**
	 * returns the search results for the specified search criteria.
	 * @param String pageUrl
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public List<Role> searchForRole(String pageUrl, AccessLevelBO level) throws Exception {
		List<Role> results = new ArrayList<Role>();
		
		StringBuilder fields = new StringBuilder();
		fields.append("select page_defn.pageid, page_defn.isavailable as pageisavailable, page_defn.description as pagedescription, page_defn.pageurl, ");
		fields.append("pagerole_xref.isavailable as pageisavailableforrole, ");
		fields.append("accesslevel_defn.description as accessleveldescription, ");
		fields.append("role_defn.description as roledescription, role_defn.roleid ");
		
		ArrayList<String>tables = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>psValues = new ArrayList<String>();
		
		// add in the base tables and linking fields
		tables.add("page_defn");
		tables.add("pagerole_xref");
		tables.add("accesslevel_defn");
		tables.add("roles_defn");
		
		// build the dynamic sql
		where.add("pagerole_xref.pageid = page_defn.pageid");
		where.add("accesslevel_defn.accesslevelid = pagerole_xref.accesslevelid");
		where.add("roles_defn.roleid = pagerole_xref.roleid");
		where.add("page_defn.pageurl = '" + pageUrl + "'");
		
		if (level != null) {
			where.add("accesslevel_defn.accesslevelid = " + level.getAccessLevelId());
		}
		
		String sql = buildSQL(fields.toString(), tables, where) + " order by role_defn.description";
		logger.debug("dynamic sql [" + sql + "]");
		
		try {
			doSearchNoException(sql, psValues);
			
			while (hasMoreResults()) {
				//FIX results.add(RoleUtil.resolveRoleFromId(getRSValueInt("roleid")));
			}
		} catch (Exception e) {
			logger.error("Exception in search", e);
			throw e;
		} finally {
			close();
		}	
		
		fields = null;
		tables = null;
		where = null;
		psValues = null;
		
		return results;
	}
	
	/**
	 * returns the search results for the specified search criteria.
	 * @param PageBO bo
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public List<PageBO> searchByArea(PageBO bo) throws Exception {
		List<PageBO>retVal = new ArrayList<PageBO>();
		
		StringBuilder fields = new StringBuilder();
		fields.append("select page_defn.pageid, page_defn.isavailable as pageisavailable, page_defn.description as pagedescription, page_defn.pageurl, ");
		fields.append("systemarea_defn.description as areadescription, systemarea_defn.areaid, ");
		fields.append("pagesystem_xref.displayorder, pagesystem_xref.parentmenu ");
		
		ArrayList<String>tables = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>psValues = new ArrayList<String>();
		
		// add in the base tables and linking fields
		tables.add("page_defn");
		tables.add("pagesystem_xref");
		tables.add("systemarea_defn");
		
		// build the dynamic sql
		where.add("pagesystem_xref.pageid = page_defn.pageid");
		where.add("systemarea_defn.areaid = pagesystem_xref.areaid");
		where.add("pagesystem_xref.areaid = " + bo.getAttribute("areaid"));
		
		String sql = buildSQL(fields.toString(), tables, where) + " order by pagesystem_xref.displayorder";
		logger.debug("dynamic sql [" + sql + "]");
		
		try {
			doSearchNoException(sql, psValues);

			while (hasMoreResults()) {
				retVal.add(createSearchByAreaObject());
			}
		} catch (Exception e) {
			logger.error("Exception in search", e);
			throw e;
		} finally {
			close();
		}	
		
		fields = null;
		tables = null;
		where = null;
		psValues = null;
		
		return retVal;
	}
	
	/**
	 * inserts new record 
	 * @param bo PageBO 
	 * @return PageBO
	 * <p>
	 * @throws Exception
	 */
	public PageBO insert(PageBO bo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.insert"));
			setPStmtValue(1, bo.getIsAvailable());
			setPStmtValue(2, bo.getDescription());
			setPStmtValue(3, bo.getPageUrl());
			execute();
			
			bo.setPageId(getIdentityValueInt("pageid", "page_defn"));
		} catch (Exception e) {
			logger.error("Exception in PageBO insert(PageBO bo)", e);
			throw e;
		} finally {
			try {
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bo;
	}
	
	/**
	 * update existing record
	 * @param bo PageBO 
	 * <p>
	 * @throws Exception
	 */
	public void update(PageBO bo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.update.byid"));
			setPStmtValue(1, bo.getIsAvailable());
			setPStmtValue(2, bo.getDescription());
			setPStmtValue(3, bo.getPageUrl());
			setPStmtValue(4, bo.getPageId());
			execute();
		} catch (Exception e) {
			logger.error("Exception in PageBO update(PageBO bo)", e);
			throw e;
		} finally {
			try {
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * delete existing record
	 * @param vo Page_DefnVO 
	 * <p>
	 * @throws Exception
	 */
	public void delete(PageBO bo, String part) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), part));
			setPStmtValue(1, bo.getPageId());
			execute();
		} catch (Exception e) {
			logger.error("Exception in PageBO delete(PageBO bo)", e);
			throw e;
		} finally {
			try {
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * returns a page object
	 * @return List<PageBO>
	 * @throws Exception
	 */
	private PageBO createPageObject() throws Exception {
		PageBO bo = new PageBO();
		bo.setPageId(getRSValueInt("pageid"));
		bo.setDescription(getRSValueString("description"));
		bo.setPageUrl(getRSValueString("pageurl"));
		bo.setIsAvailable(getRSValueString("isavailable"));
		
		bo.setAttribute("areadescription", getRSValueString("areadescription"));
		bo.setAttribute("parentmenu", getRSValueString("parentmenu"));
		
		return bo;
	}
	
	/**
	 * returns a page object
	 * @return List<PageBO>
	 * @throws Exception
	 */
	private PageBO createSearchByRoleObject() throws Exception {
		PageBO bo = new PageBO();
		bo.setPageId(getRSValueInt("pageid"));
		bo.setDescription(getRSValueString("pagedescription"));
		bo.setPageUrl(getRSValueString("pageurl"));
		bo.setIsAvailable(getRSValueString("pageisavailable"));
		
		bo.setAttribute("pageisavailableforrole", getRSValueString("pageisavailableforrole"));
		bo.setAttribute("accessleveldescription", getRSValueString("accessleveldescription"));
		bo.setAttribute("roleid", Integer.toString(getRSValueInt("roleid")));
		bo.setAttribute("roledescription", RoleUtil.getDescription(getRSValueInt("roleid")));
		
		return bo;
	}
	
	/**
	 * returns a page object
	 * @return List<PageBO>
	 * @throws Exception
	 */
	private PageBO createSearchByAreaObject() throws Exception {
		PageBO bo = new PageBO();
		bo.setPageId(getRSValueInt("pageid"));
		bo.setDescription(getRSValueString("pagedescription"));
		bo.setPageUrl(getRSValueString("pageurl"));
		bo.setIsAvailable(getRSValueString("pageisavailable"));
		
		bo.setAttribute("displayorder", getRSValueString("displayorder"));
		bo.setAttribute("parentmenu", getRSValueString("parentmenu"));
		bo.setAttribute("areadescription", getRSValueString("areadescription"));
		bo.setAttribute("areaid", Integer.toString(getRSValueInt("areaid")));
		return bo;
	}
}
