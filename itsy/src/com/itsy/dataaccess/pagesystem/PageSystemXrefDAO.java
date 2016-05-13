package com.itsy.dataaccess.pagesystem;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.base.BaseDAO;
import com.itsy.dataaccess.page.PageBO;
import com.itsy.dataaccess.systemarea.SystemAreaBO;
import com.itsy.util.SQLPropertiesUtil;
import com.itsy.util.TextUtil;

public class PageSystemXrefDAO extends BaseDAO {

	private static Logger logger = Logger.getLogger(PageSystemXrefDAO.class);
	
	/**
	 * Method to return {@link PageSystemXrefBO} by pageid
	 * @param pageId
	 * @return {@link PageSystemXrefBO}
	 * @throws Exception
	 */
	public PageSystemXrefBO findByPageId(int pageId) throws Exception {
		PageSystemXrefBO bo = null;
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.by.pageid"));
			setPStmtValue(1, Integer.valueOf(pageId));
			executeQuery();
			
			if (hasMoreResults()) {
				bo = createPageSystemBO();
			}
		} catch (Exception e) {
			logger.error(".findByPageId(int pageId) [Exception]", e);
			throw e;
		} finally {
			close();
		}
		return bo;
	}
	
	/**
	 * Method to return List<SystemAreaBO> findSystemAreasByPageIds(int[] pageIds)
	 * @param int[] pageIds
	 * @return List<DefinitionPO>
	 * @throws Exception
	 */
	public List<SystemAreaBO> findSystemAreasByPageIds(int[] pageIds) throws Exception {
		List<SystemAreaBO> systemAreas = new ArrayList<SystemAreaBO>();
		try {
			String sql = SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.system.areas.by.pageids");
			if (pageIds != null) {
				setPStmt(sql.replace("_IN_CLAUSE_", TextUtil.getPreparedStatementInClause(pageIds.length)));
				for (int i=0; i < pageIds.length; i++) {
					setPStmtValue((i+1), pageIds[i]);
				}
			} else {
				setPStmt(sql.replace("_IN_CLAUSE_", "0"));
			}
			executeQuery();
			
			while (hasMoreResults()) {
				systemAreas.add(createSystemAreaBO());
			}
		} catch (Exception e) {
			logger.error(".findSystemAreasByPageIds(int[] pageIds) [Exception]", e);
			throw e;
		} finally {
			close();
		}
		return systemAreas;
	}
	
	/**
	 * Method to return List<PageBO> findPagesBySystemAreaId(int areaId, int[] pageIds)
	 * @param int areaId
	 * @param int[] pageIds
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public List<PageBO> findPagesBySystemAreaId(int areaId, int[] pageIds) throws Exception {
		List<PageBO> pages = new ArrayList<PageBO>();
		try {
			String sql = SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.pages.by.areaid");
			setPStmt(sql.replace("_IN_CLAUSE_", TextUtil.getPreparedStatementInClause(pageIds.length)));
			setPStmtValue(1, areaId);
			for (int i=0; i < pageIds.length; i++) {
				setPStmtValue((i+2), pageIds[i]);
			}
			executeQuery();
			while (hasMoreResults()) {
				pages.add(createPageBO());
			}
		} catch (Exception e) {
			logger.error(".findPagesBySystemAreaId(int areaId, int[] pageIds) [Exception]", e);
			throw e;
		} finally {
			close();
		}
		return pages;
	}
	
	/**
	 * Method to return List<PageBO> findPages(int[] pageIds)
	 * @param int[] pageIds
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public List<PageBO> findPages(int[] pageIds) throws Exception {
		List<PageBO> pages = new ArrayList<PageBO>();
		try {
			String sql = SQLPropertiesUtil.getProperty(getClass().getName(), "sql.select.pages.by.pageid");
			setPStmt(sql.replace("_IN_CLAUSE_", TextUtil.getPreparedStatementInClause(pageIds.length)));
			for (int i=0; i < pageIds.length; i++) {
				setPStmtValue(i+1, pageIds[i]);
			}
			executeQuery();
			
			while (hasMoreResults()) {
				pages.add(createPageBO());
			}
		} catch (Exception e) {
			logger.error(".findPages(int[] pageIds) [Exception]", e);
			throw e;
		} finally {
			close();
		}
		return pages;
	}
	
	/**
	 * @return PageSystemXrefBO
	 * @throws Exception
	 */
	private PageSystemXrefBO createPageSystemBO() throws Exception {
		PageSystemXrefBO pageSystemXrefBO = new PageSystemXrefBO();
		pageSystemXrefBO.setPageId(getRSValueInt("pageid"));
		pageSystemXrefBO.setAreaId(getRSValueInt("areaid"));
		pageSystemXrefBO.setDisplayOrder(getRSValueFloat("displayorder"));
		return pageSystemXrefBO;
	}
	
	/**
	 * @return SystemAreaBO
	 * @throws Exception
	 */
	private SystemAreaBO createSystemAreaBO() throws Exception {
		SystemAreaBO systemAreaBO = new SystemAreaBO();
		systemAreaBO.setAreaId(getRSValueInt("areaid"));
		systemAreaBO.setDescription(getRSValueString("description"));
		return systemAreaBO;
	}
	
	/**
	 * @return PagePO
	 * @throws Exception
	 */
	private PageBO createPageBO() throws Exception {
		PageBO pageBO = new PageBO();
		pageBO.setPageId(getRSValueInt("pageid"));
		pageBO.setDescription(getRSValueString("description"));
		pageBO.setIsAvailable(getRSValueString("isavailable"));
		pageBO.setPageUrl(getRSValueString("pageurl"));
		pageBO.setAreaId(getRSValueString("areaid"));
		pageBO.setParentMenu(getRSValueString("parentmenu"));
		
		pageBO.setAttribute("parentmenu", getRSValueString("parentmenu"));
		pageBO.setAttribute("areaid", getRSValueString("areaid"));
		return pageBO;
	}
	
	/**
	 * This method returns page object for a specific system area
	 * @param pagePO PagePO
	 * @return PagePO
	 * @throws Exception
	 */
	public PageBO retrieveXref(PageBO pageBO) throws Exception {
		StringBuilder fields = new StringBuilder();
		fields.append("select pagesystem_xref.displayorder, pagesystem_xref.parentmenu");
		
		ArrayList<String>tables = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>psValues = new ArrayList<String>();
		
		// add in the base tables and linking fields
		tables.add("pagesystem_xref");
		
		// build the dynamic sql
		where.add("pagesystem_xref.pageid = " + pageBO.getPageId());
		where.add("pagesystem_xref.areaid = " + pageBO.getAttribute("areaid"));
		
		String sql = buildSQL(fields.toString(), tables, where);
		logger.debug("dynamic sql [" + sql + "]");
		
		try {
			doSearchNoException(sql, psValues);

			while (hasMoreResults()) {
				pageBO.setAttribute("displayorder", getRSValueString("displayorder"));
				pageBO.setAttribute("parentmenu", getRSValueString("parentmenu"));
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
	 * This method returns page objects for a all system areas
	 * @param pageBO PageBO
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public List<PageBO> retrieveAllXref(PageBO pageBO) throws Exception {
		StringBuilder fields = new StringBuilder();
		fields.append("select pagesystem_xref.areaid, pagesystem_xref.displayorder, pagesystem_xref.parentmenu, ");
		fields.append("page_defn.pageid, page_defn.isavailable, page_defn.description, page_defn.pageurl, ");
		fields.append("systemarea_defn.description as areadescription, systemarea_defn.areaid");
		
		ArrayList<String>tables = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>psValues = new ArrayList<String>();
		
		// add in the base tables and linking fields
		tables.add("pagesystem_xref");
		tables.add("page_defn");
		tables.add("systemarea_defn");
		
		// build the dynamic sql
		where.add("pagesystem_xref.pageid = page_defn.pageid");
		where.add("systemarea_defn.areaid = pagesystem_xref.areaid");
		where.add("pagesystem_xref.pageid = " + pageBO.getPageId());
		
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
				bo.setAttribute("areaid", getRSValueString("areaid"));
				bo.setAttribute("areadescription", getRSValueString("areadescription"));
				bo.setAttribute("displayorder", getRSValueString("displayorder"));
				bo.setAttribute("parentmenu", getRSValueString("parentmenu"));
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
	 * @param bo PageSystemXrefBO 
	 * <p>
	 * @throws Exception
	 */
	public void update(PageSystemXrefBO bo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.update.byId"));
			setPStmtValue(1, bo.getDisplayOrder());
			setPStmtValue(2, bo.getParentMenu());
			setPStmtValue(3, bo.getAreaId());
			setPStmtValue(4, bo.getPageId());
			execute();
		} catch (Exception e) {
			logger.error("Exception in void update(PageSystemXrefBO bo)", e);
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
	 * @param bo PageSystemXrefBO 
	 * <p>
	 * @throws Exception
	 */
	public void delete(PageSystemXrefBO bo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.delete.byId"));
			setPStmtValue(1, bo.getPageId());
			setPStmtValue(2, bo.getAreaId());
			execute();
		} catch (Exception e) {
			logger.error("Exception in void delete(PageSystemXrefBO bo)", e);
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
	 * @param bo PageSystemXrefBO 
	 * @return PageSystemXrefBO
	 * <p>
	 * @throws Exception
	 */
	public PageSystemXrefBO insert(PageSystemXrefBO bo) throws Exception {
		try {
			setPStmt(SQLPropertiesUtil.getProperty(getClass().getName(), "sql.insert"));
			setPStmtValue(1, bo.getPageId());
			setPStmtValue(2, bo.getAreaId());
			setPStmtValue(3, bo.getDisplayOrder());
			setPStmtValue(4, bo.getParentMenu());
			execute();
		} catch (Exception e) {
			logger.error("Exception in PageSystemXrefBO insert(PageSystemXrefBO bo)", e);
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
	 * update page display orders
	 * @param String[] pageids
	 * @param String[] displayorders
	 * <p>
	 * @throws Exception
	 */
	public void updateDisplayOrders(String[] pageids, String[] displayorders) throws Exception {
		try {
			String sql = SQLPropertiesUtil.getProperty(getClass().getName(), "sql.update.displayOrder");
			for (int i=0; i < (pageids != null ? pageids.length : 0); i++) {
				setPStmt(sql);
				setPStmtValue(1, displayorders[i]);
				setPStmtValue(2, pageids[i]);
				execute();
			}
		} catch (Exception e) {
			logger.error("Exception in void updateDisplayOrders(String[] pageids, String[] displayorders)", e);
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
