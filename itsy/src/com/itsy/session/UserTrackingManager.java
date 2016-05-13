package com.itsy.session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserTrackingManager {
	
	//  This map stores the relationship between a user and all sessions
	//  We use list because a user may log in from multiple machines
	private HashMap<String, List<String>> userSessionMap;
	
	//  This map keeps track the session and host machine name
	private HashMap<String, String> sessionHostMap;
	private static UserTrackingManager instance;


	public static UserTrackingManager getInstance() {
		if (instance == null)
			instance = new UserTrackingManager();

		return instance;
	}

	public boolean exist(String logName) {
		if (userSessionMap.containsKey(logName)) {
			return true;
		}
		return false;
	}

	public boolean addSession(String logName, String session, String clientMachine) {
		String dateTimeLogged = new SimpleDateFormat("hh:mm:ss aa").format(new Date());
		
		if (exist(logName)){
			List<String> list = getSession(logName);
			for (String ses : list){
				if (ses.equals(session))
					return false;
			}
			list.add(session);
			list = null;
		} else {
			List<String> list = new ArrayList<String>();
			list.add(session);
			userSessionMap.put(logName, list);
			list = null;
		}
	  
		sessionHostMap.put(session, clientMachine + " / " + dateTimeLogged);
		return true;
	}

	public List<String> getSession(String logName) {
		return userSessionMap.get(logName);
	}
	  
	public String getHostName(String session) {
		return sessionHostMap.get(session);
	}
	  
	public HashMap<String, List<String>> getAllSessions() {
		return userSessionMap;
	}

	public boolean removeSession(String logName, String session) {
		if (!userSessionMap.containsKey(logName)) {
			return false;
		} else {
			List<String> list = getSession(logName);
			if (list.size() > 1){
				for (String ses : list){
					if (ses.equals(session)){
						list.remove(ses);
						sessionHostMap.remove(session);
						break;
					}	  
				}
			} else {
				userSessionMap.remove(logName);
				sessionHostMap.remove(session);
			}
			list = null;
			return true;
		}
	}

	public void removeAllSessions() {
		userSessionMap.clear();
		sessionHostMap.clear();
	}

	protected UserTrackingManager() {
		userSessionMap = new HashMap<String, List<String>>();
	    sessionHostMap = new HashMap<String, String>();
	}

}