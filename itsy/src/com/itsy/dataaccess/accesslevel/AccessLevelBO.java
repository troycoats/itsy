package com.itsy.dataaccess.accesslevel;

import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.base.Optionable;

public class AccessLevelBO extends AccessLevelVO implements Optionable {
	
	private static Logger log = Logger.getLogger(AccessLevelBO.class);
	
	public AccessLevelBO(){
		super();
	}	
	
	/**
	 * Method to return AccessLevel_DefnBO by accesslevelid.
	 * @param accesslevelId
	 * @return AccessLevel_DefnBO
	 * @throws Exception
	 */
	public static AccessLevelBO findByAccessLevelId(int accesslevelId) throws Exception {
		return AccessLevelFacade.findByAccessLevelId(accesslevelId);		
	}

	/**
	 * Method to return all AccessLevel_DefnBO records.
	 * @return List<AccessLevel_DefnBO>
	 * @throws Exception
	 */
	public static List<AccessLevelBO> findAll() throws Exception {
		return AccessLevelFacade.findAll();		
	}
	
	/**
	 * returns the search results for the specified search criteria.
	 * @param AccessLevelBO bo
	 * @return List<AccessLevelBO>
	 * @throws Exception
	 */
	public static List<AccessLevelBO> search(AccessLevelBO po) throws Exception {
		return AccessLevelFacade.search(po);
	}
	
	/**
	 * inserts new record
	 * @param bo AccessLevel_DefnBO
	 * @return AccessLevel_DefnVO 
	 * <p>
	 * @throws Exception
	 */
	public static AccessLevelVO insert(AccessLevelBO bo) throws Exception {
		return AccessLevelFacade.insert(bo);
	}

	/**
	 * update record
	 * @param bo AccessLevel_DefnBO
	 * <p>
	 * @throws Exception
	 */
	public static void update(AccessLevelBO bo) throws Exception {
		AccessLevelFacade.update(bo);
	}

	/**
	 * delete record
	 * @param bo AccessLevel_DefnBO
	 * <p>
	 * @throws Exception
	 */
	public static void delete(AccessLevelBO bo) throws Exception {
		AccessLevelFacade.delete(bo);
	}
}
