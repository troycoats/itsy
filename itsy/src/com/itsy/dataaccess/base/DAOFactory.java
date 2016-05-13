package com.itsy.dataaccess.base;

import com.itsy.dataaccess.accesslevel.AccessLevelDAO;
import com.itsy.dataaccess.page.PageDAO;
import com.itsy.dataaccess.pagerole.PageRoleXrefDAO;
import com.itsy.dataaccess.pagesystem.PageSystemXrefDAO;
import com.itsy.dataaccess.role.RoleDAO;
import com.itsy.dataaccess.role.UserRoleXrefDAO;
import com.itsy.dataaccess.systemarea.SystemAreaDAO;
import com.itsy.dataaccess.user.UserDAO;


/**
 * This is the class used to get any DAO.  This follows the factory pattern
 * as outlined by Sun.  This object also abstracts the method of obtaining 
 * a DAO to facilitate the event that DAO's might be cached, etc.  Each get*DAO
 * method returns the type specified.
 */
public class DAOFactory {
	
	/**
	 * Following the singleton pattern, this static member is
	 * used to hold on to a single instance of the DAOFactory.
	 */
	private static DAOFactory theFactory;
	
	/**
	 * Private Constructor, should not be instantiated this way, use the getFactory() method.
	 */
	private DAOFactory() {
		super();
	}
	
	/**
	 * Singleton method for returning a DAOFactory object.
	 * 
	 * @return the single instance of the DAOFactory.
	 */
	public static DAOFactory getFactory() {
		if (theFactory == null) {
			theFactory = new DAOFactory();
		}
		return theFactory;
	}
	
	public UserDAO getUserDAO() {
		return new UserDAO();
	}
	
	public RoleDAO getRoleDAO() {
		return new RoleDAO();
	}

	public PageRoleXrefDAO getPageRoleXrefDAO() {
		return new PageRoleXrefDAO();
	}
	
	public PageSystemXrefDAO getPageSystemXrefDAO() {
		return new PageSystemXrefDAO();
	}
	
	public SystemAreaDAO getSystemAreaDAO() {
		return new SystemAreaDAO();
	}
	
	public AccessLevelDAO getAccessLevelDAO() {
		return new AccessLevelDAO();
	}
	
	public PageDAO getPageDAO() {
		return new PageDAO();
	}
	
	public UserRoleXrefDAO getUserRoleXrefDAO() {
		return new UserRoleXrefDAO();
	}
}