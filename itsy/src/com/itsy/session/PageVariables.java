package com.itsy.session;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.itsy.util.TextUtil;

/**
 * pass variables from page to page that are required throughout the application
 *
 */
public class PageVariables {
	private static Logger logger = Logger.getLogger(PageVariables.class);

	public static final String ALL_KEYS = "allkeys";

	public static final String EMBEDDED = "embedded";

	private HashMap<String, String> variables = new HashMap<String, String>();

	/**
	 * obtain the parameters for the http request
	 *
	 * @param request
	 * @return PageVariables object
	 */
	public PageVariables getFromRequest(HttpServletRequest request) {
		String embedded = URLEncryption.getParameter(request, EMBEDDED);
		if (!TextUtil.isEmpty(embedded)) {
			try {
				embedded = URLDecoder.decode(embedded, "UTF-8");
			} catch (Exception e) {
				logger.error("Error occurred decrypting page variables: "
						+ e.toString());
			}
			variables = URLEncryption.urlDecrypt(embedded);
		} else {
			String[] allKeys = getVariableNames(request);

			// RETRIEVE all of the parameters from the request
			for (String key : allKeys) {
				variables.put(key, URLEncryption.getParameter(request, key));
			}
		}
		return this;
	}
	
	public PageVariables getFromPlain(String embedded) {
		
		if (!TextUtil.isEmpty(embedded)) {
			try {
				embedded = URLDecoder.decode(embedded, "UTF-8");
			} catch (Exception e) {
				logger.error("Error occurred decrypting page variables: "
						+ e.toString());
			}
			variables = URLEncryption.urlDecrypt(embedded);
		} 
		
		return this;
	}

	/**
	 * obtain a list of names for all the variables 
	 * 
	 * @param HttpServletRequest request
	 * @return String[]
	 */
	public String[] getVariableNames(HttpServletRequest request) {
		HashMap<String, String> keys = new HashMap<String, String>();
		String[] allKeys = new String[] {};
		try {
			String key = null;
			Iterator<String> iterator = this.variables.keySet().iterator();
			while (iterator.hasNext()) {
				key = iterator.next();
				keys.put(key, key);
			}

			String listOfKeys = URLEncryption.getParameter(request, ALL_KEYS);
			if (!TextUtil.isEmpty(listOfKeys)) {
				allKeys = listOfKeys.contains("|") ? listOfKeys.split("\\|")
						: new String[] { listOfKeys };
			}
			for (String k : allKeys) {
				keys.put(k, k);
			}
		} catch (Exception e) {
			logger.error("Error occurred retrieving page variables keys: "
					+ e.toString());
		}
		return keys.keySet().toArray(allKeys);
	}

	/**
	 * store the variables on the jsp page as an encrypted hidden parameter
	 *
	 * @return String
	 */
	public String getEnryptedHtmlField() {
		return "<input type=\"hidden\" name=\"" + EMBEDDED + "\" id=\""
				+ EMBEDDED + "\" value=\"" + getUrlParameters(true) + "\">";
	}

	/**
	 * store the variables on the jsp page as non-encrypted hidden input fields 
	 *
	 * @return String
	 */
	public String getHiddenHtmlFields() {
		StringBuilder sb = new StringBuilder();
		StringBuilder allKeys = new StringBuilder();
		String key = null;
		String value = null;

		Iterator<String> iterator = this.variables.keySet().iterator();
		while (iterator.hasNext()) {
			key = iterator.next();

			try {
				value = URLDecoder.decode(this.variables.get(key), "UTF-8");
			} catch (Exception e) {
				// do nothing
			}

			sb.append("<input type=\"hidden\" name=\"" + key + "\" id=\"" + key
					+ "\" value=\"" + value + "\">");

			// keep track of all of the keys
			if (allKeys.length() > 0) {
				allKeys.append("|");
			}
			allKeys.append(key);
		}
		iterator = null;

		sb.append("<input type=\"hidden\" name=\"" + ALL_KEYS + "\" id=\""
				+ ALL_KEYS + "\" value=\"" + allKeys.toString() + "\">");

		return sb.toString();
	}

	/**
	 * store the variables on the url request
	 *
	 * @param boolean whether or not to append the variables  
	 * @return String
	 */
	public String getUrlParameters(boolean append) {
		String encryptedUrlParameters = URLEncryption
				.urlEncryptStatic(getRawValues());
		if (append) {
			String urlDescriptor = "?" + URLEncryption.REQUEST_PARAMETER + "=";
			encryptedUrlParameters = encryptedUrlParameters.substring(
					urlDescriptor.length(), encryptedUrlParameters.length());
		}
		return encryptedUrlParameters;
	}

	/**
	 * obtain the parameters before encryption
	 *
	 * @return String
	 */
	public String getRawValues() {
		StringBuilder sb = new StringBuilder();
		StringBuilder allKeys = new StringBuilder();
		String key = null;

		Iterator<String> iterator = this.variables.keySet().iterator();
		while (iterator.hasNext()) {
			key = iterator.next();
			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(key + "=" + this.variables.get(key));

			// keep track of all of the keys
			if (allKeys.length() > 0) {
				allKeys.append("|");
			}
			allKeys.append(key);
		}
		iterator = null;

		sb.append("&" + ALL_KEYS + "=" + allKeys.toString());
		return sb.toString();
	}

	/**
	 * helper method
	 */
	public String getUrlParameters() {
		return getUrlParameters(false);
	}

	/**
	 * add a list of variables
	 *
	 * @param String[] keys
	 * @return PageVariables
	 */
	public PageVariables addMultipleVariables(String... keys) {
		for (String key : keys) {
			this.variables.put(key, "");
		}
		return this;
	}

	/**
	 * add a variable
	 *
	 * @param key
	 * @param value
	 */
	public void addVariable(String key, String value) {
		try {
			value = URLEncoder.encode(value, "UTF-8");
		} catch (Exception e) {
			// do nothing
		}
		this.variables.put(key, value);
	}

	/**
	 * Accept Object as value.
	 * @param key
	 * @param value
	 */
	public void addVariable(String key, Object obj) {
		String value = String.valueOf(obj);
		try {
			value = URLEncoder.encode(value, "UTF-8");
		} catch (Exception e) {
			// do nothing
		}
		this.variables.put(key, value);
	}

	/**
	 * remove a variable
	 *
	 * @param key
	 */
	public void removeVariable(String key) {
		this.variables.remove(key);
	}

	/**
	 * RETRIEVE a variable
	 *
	 * @param key
	 * @return String
	 */
	public String getVariable(String key) {
		String value = this.variables.get(key);
		if (TextUtil.isEmpty(value)) {
			return "";
		} else {
			try {
				value = URLDecoder.decode(value, "UTF-8");
			} catch (Exception e) {
				// do nothing
			}
			return value;
		}
	}

	/**
	 * RETRIEVE a variable as an int
	 *
	 * @param key
	 * @return int
	 */
	public int getVariableAsInt(String key) {
		String value = getVariable(key);
		return !TextUtil.isEmpty(value) ? Integer.parseInt(value) : 0;
	}

	/**
	 * set a variable
	 *
	 * @param key
	 */
	public void setVariable(String key, String value) {
		this.variables.put(key, value);
	}
	
	
}
