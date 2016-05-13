package com.itsy.dataaccess.pagerole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.accesslevel.AccessLevelBO;
import com.itsy.dataaccess.base.BaseDAO;
import com.itsy.dataaccess.page.PageBO;
import com.itsy.enumeration.AccessLevel;
import com.itsy.session.PageAccess;
import com.itsy.util.RoleUtil;
import com.itsy.util.SQLPropertiesUtil;
import com.itsy.util.TextUtil;

public class PageRoleXrefDAO extends BaseDAO {
	
	private static Logger logger = Logger.getLogger(PageRoleXrefDAO.class);

	private HashMap<String,List<PageAccess>> pageAccessMap = null;
	private List<PageAccess> pageAccessList = null;
	
	/**
	 * This method returns all the page access roles, defn records available
	 * and store in a Map all the access roles by pageurl.
	 * @return HashMap<String,List<SimplePageAccess>>
	 * @throws Exception
	 */
	public HashMap<String, List<PageAccess>> retrieveAllPageAccessWithRoles() throws Exception {
		retrievePageAccess(SQLPropertiesUtil.getProperty(getClass().getName(), "select.all.page.roles.accesses"), null, null);
		return this.pageAccessMap;
	}

	/**
	 * This method returns page access for specific roles, defn records available
	 * and store in a Map all the access roles by pageurl.
	 * @return HashMap<String,List<SimplePageAccess>>
	 * @throws Exception
	 */
	public List<PageAccess> retrievePageAccessForSpecificRoles(List<Integer> roleIds, AccessLevel accessLevel) throws Exception {
		retrievePageAccess(SQLPropertiesUtil.getProperty(getClass().getName(), "select.page.roles.byRoleIds"), roleIds, accessLevel);
		return this.pageAccessList;
	}
	
	private void retrievePageAccess(String sql, List<Integer> roleIds, AccessLevel accessLevel) throws Exception {
		logger.debug(".retrieveAllPageAccessWithRoles() start");
		this.pageAccessMap = new HashMap<String, List<PageAccess>>();
		this.pageAccessList = new ArrayList<PageAccess>();
		try {
			if (roleIds != null) {
				setPStmt(sql.replace("_IN_CLAUSE_", TextUtil.getPreparedStatementInClause(roleIds.size())));
				for (int i=0; i < roleIds.size(); i++) {
					setPStmtValue((i+2), roleIds.get(i));
				}
			} else {
				setPStmt(sql);
			}
			
			if (accessLevel != null) {
				setPStmtValue(1, accessLevel.accessLevelId);
			} else {
				setPStmtValue(1, 0);
			}
			
			executeQuery();
			
			PageAccess access = null;
			List<PageAccess> pal = null;
			while (hasMoreResults()) {
				access = new PageAccess();
				access.setPageUrl(getRSValueString("pageurl"));
				access.setRoleId(getRSValueInt("roleid"));
				access.setAccessLevelId(getRSValueInt("accesslevelid"));
				access.setSystemName(getRSValueString("systemarea"));
				access.setPageId(getRSValueInt("pageid"));
				access.setPageName(getRSValueString("description"));
				access.setAccess(getRSValueString("accesslevel"));
				access.setPageAvailable(getRSValueString("isavailable"));
				access.setPageAvailableForRole(getRSValueString("roleavailable"));
				access.setRoleName(getRSValueString("rolename"));
				
				pal = (List<PageAccess>)this.pageAccessMap.get(access.getPageUrl());
				if (pal == null) {
					pal = new ArrayList<PageAccess>();
					pal.add(access);					
				} else {
					pal.add(access);					
				}
				if ("Y".equals(access.isPageAvailable()))
					this.pageAccessMap.put(access.getPageUrl(), pal);
				
				pageAccessList.add(access);
			}
			
			access = null;
			pal = null;
		} catch (Exception e) {
			logger.error("void retrievePageAccess() [Exception]", e);
			throw e;
		} finally {
			close();
			logger.debug("void retrievePageAccess()  end");
		}
	}
	
	/**
	 * This method returns page object for a specific role
	 * @param pageBO PageBO
	 * @return PageBO
	 * @throws Exception
	 */
	public PageBO retrieveXref(PageBO pageBO) throws Exception {
		StringBuilder fields = new StringBuilder();
		fields.append("select pagerole_xref.accesslevelid, pagerole_xref.isavailable");
		
		ArrayList<String>tables = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>psValues = new ArrayList<String>();
		
		// add in the base tables and linking fields
		tables.add("pagerole_xref");
		
		// build the dynamic sql
		where.add("pagerole_xref.pageid = " + pageBO.getPageId());
		where.add("pagerole_xref.roleid = " + pageBO.getAttribute("roleid"));
		
		String sql = buildSQL(fields.toString(), tables, where);
		logger.debug("dynamic sql [" + sql + "]");
		
		try {
			doSearchNoException(sql, psValues);
			
			while (hasMoreResults()) {
				pageBO.setAttribute("accesslevelid", getRSValueString("accesslevelid"));
				pageBO.setAttribute("pageisavailableforrole", getRSValueString("isavailable"));
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
		
		return pageBO;
	}
	
	/**
	 * This method returns page objects for all roles
	 * @param pageBO PageBO
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public List<PageBO> retrieveAllXref(PageBO pageBO, boolean activeOnly) throws Exception {
		StringBuilder fields = new StringBuilder();
		fields.append("select pagerole_xref.accesslevelid, pagerole_xref.isavailable as pageisavailableforrole, pagerole_xref.roleid, ");
		fields.append("page_defn.pageid, page_defn.isavailable, page_defn.description, page_defn.pageurl, ");
		fields.append("accesslevel_defn.description as accessleveldescription");
		
		ArrayList<String>tables = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>psValues = new ArrayList<String>();
		
		// add in the base tables and linking fields
		tables.add("pagerole_xref");
		tables.add("page_defn");
		tables.add("accesslevel_defn");
		
		// build the dynamic sql
		where.add("pagerole_xref.pageid = page_defn.pageid");
		where.add("accesslevel_defn.accesslevelid = pagerole_xref.accesslevelid");
		where.add("pagerole_xref.pageid = " + pageBO.getPageId());
		if (activeOnly) {
			where.add("page_defn.isavailable='Y'");
		}
		
		String sql = buildSQL(fields.toString(), tables, where);
		logger.debug("dynamic sql [" + sql + "]");
		
		List<PageBO> pages = new ArrayList<PageBO>();
		
		try {
			doSearchNoException(sql, psValues);

			while (hasMoreResults()) {
				PageBO bo = new PageBO();
				bo.setPageId(getRSValueInt("pageid"));
				bo.setIsAvailable(getRSValueString("isavailable"));
				bo.setDescription(getRSValueString("description"));
				bo.setPageUrl(getRSValueString("pageurl"));
				bo.setAttribute("accesslevelid", getRSValueString("accesslevelid"));
				bo.setAttribute("pageisavailableforrole", getRSValueString("pageisavailableforrole"));
				bo.setAttribute("accessleveldescription", getRSValueString("accessleveldescription"));
				bo.setAttribute("roleid", getRSValueString("roleid"));
				bo.setAttribute("roledescription", RoleUtil.getDescription(getRSValueInt("roleid")));
				pages.add(bo);
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
		
		return pages;
	}
	
	/**
	 * update existing record
	 * @param bo PageRoleXrefBO 
	 * <p>
	 * @throws Exception
	 */
	public void update(PageRoleXrefBO bo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.update.byId"));
			setPStmtValue(1, bo.getIsAvailable());
			setPStmtValue(2, bo.getAccessLevelId());
			setPStmtValue(3, bo.getPageId());
			setPStmtValue(4, bo.getRoleId());
			execute();
		} catch (Exception e) {
			logger.error("Exception in void update(PageRoleXrefBO bo)", e);
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
	 * @param bo PageRoleXrefBO 
	 * <p>
	 * @throws Exception
	 */
	public void delete(PageRoleXrefBO bo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.delete.byId"));
			setPStmtValue(1, bo.getPageId());
			setPStmtValue(2, bo.getRoleId());
			execute();
		} catch (Exception e) {
			logger.error("Exception in void delete(PageRoleXrefBO bo)", e);
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
	 * inserts new record 
	 * @param vo PageRoleXrefBO 
	 * @return PageRoleXrefBO
	 * <p>
	 * @throws Exception
	 */
	public PageRoleXrefBO insert(PageRoleXrefBO bo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.insert"));
			setPStmtValue(1, bo.getPageId());
			setPStmtValue(2, bo.getRoleId());
			setPStmtValue(3, bo.getAccessLevelId());
			setPStmtValue(4, bo.getIsAvailable());
			execute();
			
			return bo;
		} catch (Exception e) {
			logger.error("Exception in PageRoleXrefBO insert(PageRoleXrefBO bo)", e);
			throw e;
		} finally {
			try {
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
