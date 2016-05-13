package com.itsy.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itsy.constants.Constants;
import com.itsy.dataaccess.user.UserBO;
import com.itsy.session.LoginFilter;
import com.itsy.session.PagePermissions;
import com.itsy.util.LoginUtil;
import com.itsy.util.TextUtil;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(name="/LoginServlet", urlPatterns="login")
public class LoginServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	@Override
	protected void performTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (TextUtil.isEmpty(username)) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.sendRedirect(LoginFilter.LOGIN_PAGE);
		} else {
			UserBO userBO = null;
			try {
				userBO = LoginUtil.validate(username, password);
				if (LoginUtil.isValidUser(userBO)) {
					LoginUtil.writeUserToSession(userBO, request);
					LoginUtil.updateLastLogin(username);
					try {
						PagePermissions.buildCache();
						redirect(Constants.MAIN_PAGE, Constants.MAIN_PAGE_PERMISSIONS, request, response);	
					} catch(Exception e) {
						HttpServletResponse httpResponse = (HttpServletResponse) response;
						httpResponse.sendRedirect(Constants.LOGOUT_PAGE);
					}
				} else {
					writer.write("{ success: false, errors: { reason: 'Invalid username or password.'}}");
				}
			} catch (Exception e) {
				writer.write("{ success: false, errors: { reason: 'Error occurred while connectiong to the server. " + e.getMessage() + "'}}");
			}
			
			writer.write("{ success: true }");
			
			userBO = null;
		}
	}
	
}
