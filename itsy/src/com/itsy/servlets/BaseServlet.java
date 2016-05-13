package com.itsy.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.itsy.constants.Constants;
import com.itsy.session.PagePermissions;
import com.itsy.session.Permission;

/**
 * Servlet implementation class BaseServlet
 */
@WebServlet("/BaseServlet")
public abstract class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final String WEB_ROOT = "itsy";
	
	public static final String UNEXPECTED_ERROR = "An unexpected error has occurred.";

	static Logger log = Logger.getLogger(BaseServlet.class);

	public BaseServlet() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		performTask(request, response);
	}  	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		performTask(request, response);
	}

	protected abstract void performTask(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException;
	
	/**
	 * Sets request parameters with information for security for the requested URI.  This method should
	 * be used to set the request parameters based on a virtual uri for sub tabs as called from within
	 * a controller servlet.
	 * @param	request the request used in the attempt to reach the uri.
	 * @param	response the response to be used in redirecting the user to the Access Denied page in the event
	 * of access denied.
	 * @param	requestedURI the virtual URI to use when retrieving sub tab permissions.
	 * @return boolean true if user should have access to the uri, false if not.
	 * @throws java.io.IOException
	 * @see	itsy.servlets.BaseCareControllerServlet.setRequestParameters
	 */
	public static boolean hasAccess(HttpServletRequest request, HttpServletResponse response, String requestedURI) throws IOException {
		request.getSession(false); // existing session for the framework.
		Permission permissions = PagePermissions.getPermissions(request, requestedURI);
		if (permissions.hasAccess()) { 
			// the user has access so set the permission attribute
			request.setAttribute(Constants.REQUEST_PERMISSION, permissions);
			
			permissions = null;
			return true;
		} else { 
			// the user does not have access
			if (response != null) {
				response.sendRedirect(Constants.ACCESS_DENIED_PAGE);
			}
			
			permissions = null;
			return false;
		}
	}
	
	public static boolean verifyAccess(HttpServletRequest request, HttpServletResponse response, String requestedURI) throws IOException {
		request.getSession(false); 
		return PagePermissions.getPermissions(request, requestedURI).hasAccess();
	}
		
	public void redirect(String URL, String URI, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if (hasAccess(request, response, URI))
				getServletContext().getRequestDispatcher(URL).forward(request, response);
		} catch (Exception e) {
			getServletContext().getRequestDispatcher(Constants.CLOSE_PAGE).forward(request, response);
		}
	}
}