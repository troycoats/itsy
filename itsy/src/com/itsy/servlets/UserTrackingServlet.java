package com.itsy.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;
import com.itsy.dataaccess.base.DataFetch;
import com.itsy.dataaccess.role.UserRoleXrefFacade;
import com.itsy.dataaccess.user.UserBO;
import com.itsy.session.UserTrackingManager;
import com.itsy.util.RoleUtil;
import com.itsy.util.TextUtil;

/**
 * Servlet implementation class UserTrackingServlet
 */
@WebServlet("/UserTrackingServlet")
public class UserTrackingServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserTrackingServlet() {
        super();
    }

	@Override
	protected void performTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mode = request.getParameter("mode");
		boolean errorOccurred = false;
		JSONObject obj = new JSONObject();
		JSONArray json = new JSONArray(); 
		
		if (mode != null && "clear".equals(mode)){
			UserTrackingManager.getInstance().removeAllSessions();
			obj.put("success", true);
			obj.put("message", "User tracking has been reset .");
		} else {
			HashMap<String, List<String>> map = UserTrackingManager.getInstance().getAllSessions();
			Entry entry = null;
			String logName = null;
			List<String> value = null;
			JSONObject jEvent = null;
			UserBO userBO = null;
			List<Integer> roles = null;
			String hostName = null;
			for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
				entry = (Entry) iter.next();
				logName = (String) entry.getKey();
				value = (List<String>) entry.getValue();
				if (logName != null){
					try {
						jEvent = new JSONObject();
						
						userBO = UserBO.findByUsername(logName, DataFetch.QUICK);
						jEvent.put("name", userBO.getFullName());
						
						String sessId = "";
						for (String sess : value){
							hostName = UserTrackingManager.getInstance().getHostName(sess);
							if (hostName == null)
								hostName = "unknown";
							sessId = sessId + hostName + "<br>";
							jEvent.put("session", sessId);
						}
						jEvent.put("email", userBO.getEmailAddress());
						if (!TextUtil.isEmpty(userBO.getPhone()))
							jEvent.put("phone", userBO.getPhone());
						else
							jEvent.put("phone", "n/a");
						
						if (userBO.getRoles() == null && userBO.getUserId() > 0) {
							userBO = UserBO.findByUsername(logName, DataFetch.QUICK);
							if (userBO != null) {
								roles = UserRoleXrefFacade.getAllRoles(userBO.getUserId());
								//FIX -> jEvent.put("role", RoleUtil.getDominantRole(roles));
								roles = null;
							}
						} else {
							// FIX -> jEvent.put("role", RoleUtil.getDominantRole(userBO.getRoles()));
						}
												
						json.add(jEvent);
						
						userBO = null;
						jEvent = null;
					} catch (Exception e) {
						e.printStackTrace();
						errorOccurred = true;
					}
				}
			}
			
			if (errorOccurred){
				obj.put("success", false);
				obj.put("message", "Error occurred while retrieving data.");
			} else {
				obj.put("currentUsers", json);
				obj.put("success", true);
				obj.put("message", "Data retrieved successfully.");
			}
			
			map = null;
			entry = null;
			logName = null;
			value = null;
			hostName = null;
		}
		
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(obj.toString());
		
		obj = null;
		json = null;
	}
}
