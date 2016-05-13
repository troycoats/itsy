package com.itsy.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itsy.constants.Constants;

/**
 * Servlet implementation class for Servlet: LogoutServlet
 *
 */
@WebServlet(name="/LogoutServlet", urlPatterns="logout")
public class LogoutServlet extends BaseServlet {
  
	private static final long serialVersionUID = 784545454;
	 
	public LogoutServlet() {
		super();
	}   	
	
	protected void performTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null)
			session.invalidate();
 		
		response.sendRedirect(Constants.LOGOUT_PAGE);
	}   	  	    
}