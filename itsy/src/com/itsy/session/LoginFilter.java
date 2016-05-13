package com.itsy.session;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itsy.servlets.BaseServlet;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(filterName = "/LoginFilter", urlPatterns = {
	"/PageServlet",
	"/TaskListServlet" 
})

public class LoginFilter implements Filter {

	public static final String LOGIN_PAGE = "/" + BaseServlet.WEB_ROOT + "/index.jsp";
	
    /**
     * Default constructor. 
     */
    public LoginFilter() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	if (request instanceof HttpServletRequest) {
    		HttpServletRequest httpRequest = (HttpServletRequest) request;     
    		HttpServletResponse httpResponse = (HttpServletResponse) response;  
    		
    		HttpSession session = ((HttpServletRequest) request).getSession(false);
    		if (session != null && session.getAttribute(SessionVariables.USER_NAME) != null)
    			chain.doFilter(request, response);   
    		else {
    			if (isAjax(httpRequest)) { 
    				httpResponse.setContentType("text/json; charset=UTF-8");
    				PrintWriter out = httpResponse.getWriter();      
    				String json = "{'redirect':'" + LOGIN_PAGE + "', 'success':false, 'sessionExpCode':401}";      
    				out.write(json);          
    				out.flush();     
    				out.close(); 
    			} else
    				httpResponse.sendRedirect(LOGIN_PAGE);	
    		}
    		session = null;
    		httpRequest = null;
    		httpResponse = null;
        }		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}
	
	private boolean isAjax(HttpServletRequest request) {     
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With")); 
	} 
}
