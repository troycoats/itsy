package com.itsy.dataaccess.systemarea;

import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.base.Optionable;

public class SystemAreaBO extends SystemAreaVO implements Optionable {
	
	private static Logger log = Logger.getLogger(SystemAreaBO.class);
	
	public SystemAreaBO() {
		super();
	}	
	
	/**
	 * Method to return SystemArea_DefnBO by roleId.
	 * @param roleId
	 * @return SystemArea_DefnBO
	 * @throws Exception
	 */
	public static SystemAreaBO findByAreaId(int areaId) throws Exception{
		return SystemAreaFacade.findByAreaId(areaId);		
	}

	/**
	 * Method to return all SystemArea_DefnBO records
	 * @return List<SystemArea_DefnBO>
	 * @throws Exception
	 */
	public static List<SystemAreaBO> findAll() throws Exception{
		return SystemAreaFacade.findAll();		
	}

	/**
	 * returns the search results for the specified search criteria.
	 * @param SystemAreaBO bo
	 * @return List<SystemAreaBO>
	 * @throws Exception
	 */
	public static List<SystemAreaBO> search(SystemAreaBO bo) throws Exception {
		return SystemAreaFacade.search(bo);
	}
	
	/**
	 * inserts new record
	 * @param bo SystemArea_DefnBO
	 * @return SystemArea_DefnVO 
	 * <p>
	 * @throws Exception
	 */
	public static SystemAreaBO insert(SystemAreaBO bo) throws Exception {
		return SystemAreaFacade.insert(bo);
	}

	/**
	 * update record
	 * @param bo SystemArea_DefnBO
	 * <p>
	 * @throws Exception
	 */
	public static void update(SystemAreaBO bo) throws Exception {
		SystemAreaFacade.update(bo);
	}

	/**
	 * delete record
	 * @param bo SystemArea_DefnBO
	 * <p>
	 * @throws Exception
	 */
	public static void delete(SystemAreaBO bo) throws Exception {
		SystemAreaFacade.delete(bo);
	}
}
