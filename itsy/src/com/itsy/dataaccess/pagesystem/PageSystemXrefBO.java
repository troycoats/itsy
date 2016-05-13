package com.itsy.dataaccess.pagesystem;

import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.page.PageBO;
import com.itsy.dataaccess.systemarea.SystemAreaBO;

public class PageSystemXrefBO extends PageSystemXrefVO {
	
	private static Logger log = Logger.getLogger(PageSystemXrefBO.class);

	public PageSystemXrefBO() {
		super();
	}	
	
	/**
	 * Method to return PageSystemXrefBO by pageId
	 * @param pageId
	 * @return PageSystemXrefBO
	 * @throws Exception
	 */
	public static PageSystemXrefBO findByPageId(int pageId) throws Exception {
		return PageSystemXrefFacade.findByPageId(pageId);		
	}

	/**
	 * Method to return List<SystemAreaBO> findSystemAreasByPageIds(int[] pageIds)
	 * @param int[] pageIds
	 * @return List<SystemAreaBO>
	 * @throws Exception
	 */
	public static List<SystemAreaBO> findSystemAreasByPageIds(int[] pageIds) throws Exception {
		return PageSystemXrefFacade.findSystemAreasByPageIds(pageIds);		
	}
	
	/**
	 * Method to return List<PagePO> findPagesBySystemAreaId(int areaId, int[] pageIds)
	 * @param int areaId
	 * @param int[] pageIds
	 * @return List<PagePO>
	 * @throws Exception
	 */
	public static List<PageBO> findPagesBySystemAreaId(int areaId, int[] pageIds) throws Exception {
		return PageSystemXrefFacade.findPagesBySystemAreaId(areaId, pageIds);		
	}

	/**
	 * Method to return List<PagePO> findPages(int[] pageIds)
	 * @param int[] pageIds
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public static List<PageBO> findPages(int[] pageIds) throws Exception {
		return PageSystemXrefFacade.findPages(pageIds);		
	}
	
	/**
	 * update record
	 * @param bo PageSystemXrefBO
	 * <p>
	 * @throws Exception
	 */
	public static void update(PageSystemXrefBO bo) throws Exception {
		PageSystemXrefFacade.update(bo);
	}
	
	/**
	 * delete record
	 * @param bo PageSystemXrefBO
	 * <p>
	 * @throws Exception
	 */
	public static void delete(PageSystemXrefBO bo) throws Exception {
		PageSystemXrefFacade.delete(bo);
	}
	
	/**
	 * insert record
	 * @param bo PageSystemXrefBO
	 * <p>
	 * @throws Exception
	 */
	public static void insert(PageSystemXrefBO bo) throws Exception {
		PageSystemXrefFacade.insert(bo);
	}
	
	/**
	 * This method returns page object for a specific system area
	 * @param pageBO PageBO
	 * @return PageBO
	 * @throws Exception
	 */
	public static PageBO retrieveXref(PageBO pageBO) throws Exception {
		return PageSystemXrefFacade.retrieveXref(pageBO);		
	}
	
	/**
	 * This method returns page objects for a all system areas
	 * @param pagePO PageBO
	 * @return List<PageBO>
	 * @throws Exception
	 */
	public static List<PageBO> retrieveAllXref(PageBO pageBO) throws Exception {
		return PageSystemXrefFacade.retrieveAllXref(pageBO);		
	}
	
	/**
	 * update page display orders
	 * @param bo PageSystem_XrefBO
	 * <p>
	 * @throws Exception
	 */
	public static void updateDisplayOrders(String[] pageids, String[] displayOrders) throws Exception {
		if (pageids != null && displayOrders != null && pageids.length == displayOrders.length) {
			PageSystemXrefFacade.updateDisplayOrders(pageids, displayOrders);
		}
	}
}
