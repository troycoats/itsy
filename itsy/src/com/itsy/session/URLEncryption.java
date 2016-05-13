package com.itsy.session;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.itsy.util.TextUtil;

public class URLEncryption {
	public static final String DEFAULT_DATE_FORMAT = "MM-dd-yyyy";
	public static final String DEFAULT_DATETIME_FORMAT = "MM-dd-yyyy hh:mm:ss";
	
	private static Logger logger = Logger.getLogger(URLEncryption.class);
	
	public static String REQUEST_PARAMETER = "_";
	
	private String servletContext = "";
	
	/**
	 * constructor
	 */
	public URLEncryption(String servletContext) {
		this.servletContext = servletContext;
	}
	
	/**
	 * method to encrypt and url encode request parameters
	 * 
	 * NOTE: this method WILL prepend the servlet context to the returned url address
	 */
	public String urlEncrypt(String parameters) {
		return this.servletContext + urlEncryptStatic(parameters);
	}
	
	/**
	 * static method to encrypt and url encode request parameters
	 * 
	 * NOTE: this method WILL NOT prepend the servlet context to the returned url address
	 */
	public static String urlEncryptStatic(String parameters) {
		String encoded = "";
		try {
			SAPEncryptDecrypt cryptor = new SAPEncryptDecrypt("DESede", SAPEncryptDecrypt.DEFAULT_ENCRYPTION_KEY);
			if (!TextUtil.isEmpty(parameters)) {
				String encrypted = cryptor.encrypt(stripBadCharacters(parameters));
				encoded = URLEncoder.encode(encrypted, "UTF-8");
			}
		} catch (Exception e) {
			System.err.println(e);	  
		}	
		return "?" + REQUEST_PARAMETER + "=" + encoded;
	}
	
	/**
	 * method to decrypt request parameters
	 */
	public static HashMap<String, String> urlDecrypt(String parameters) {
		HashMap<String, String> decryptedParameters = new HashMap<String, String>(); 
		try {
			SAPEncryptDecrypt cryptor = new SAPEncryptDecrypt("DESede", SAPEncryptDecrypt.DEFAULT_ENCRYPTION_KEY);
			String decrypted = cryptor.decrypt(parameters);
			if (decrypted != null && decrypted.length() > 0) {
				StringTokenizer st = new StringTokenizer(decrypted, "&");
				if (st.hasMoreTokens()) {
					while (st.hasMoreTokens()) {
						decryptedParameters = addPair(st.nextToken(), decryptedParameters);
					}
				} else {
					decryptedParameters = addPair(decrypted, decryptedParameters);
				}
			}
		} catch (Exception e) {
			System.err.println(e);	  
		}		
		return decryptedParameters;
	}
	
	/**
	 * method to remove characters from the url before adding class specific formating
	 */
	private static String stripBadCharacters(String parameters) {
		parameters = parameters.replaceAll("\\?", "");
		parameters = parameters.replaceAll(REQUEST_PARAMETER + "\\=", "");
		return parameters;
	}
	
	/**
	 * method to create a collection of mapped pairs of url parameters
	 */
	private static HashMap<String, String> addPair(String pair, HashMap<String, String> parameters) {
		StringTokenizer st = new StringTokenizer(pair, "=");
		if (st.hasMoreTokens()) {
			String t1 = "";
			String t2 = "";
			try {
				while (st.hasMoreTokens()) {
					t1 = st.nextToken();
					t2 = st.nextToken();
				}
			} catch (Exception e) {
				// do nothing
			}
			if (!TextUtil.isEmpty(t1)) {
				parameters.put(t1, t2);
			}
		}
		return parameters;
	}
	
	/**
	 * method to be used in place of a direct call to request.getParameter
	 */
	public static String getParameter(HttpServletRequest request, String name) {
		return getParamAsString(request, name);
	}
	
	/*
	 * METHODS BELOW REPLACE METHODS FROM TEXTUTIL
	 */
	
	/**
	 * Extracts a String Object from the named HttpServletRequest parameter.
	 * 
	 * If the named parameter does not exist or it's value is empty  
	 * (length == 0), this method shall return a null.
	 * 
	 * @param request The HttpServletRequest object.
	 * @param name The name of the paramter in the request object.
	 * @return A valid String object or null if missing or length is 0.
	 */
	public static String getParamAsString(HttpServletRequest request, String key, String defaultValue) {
		String encryptedParameters = TextUtil_getParamAsString(request, REQUEST_PARAMETER);
		if (TextUtil.isEmpty(encryptedParameters)) {
			String value = TextUtil_getParamAsString(request, key);
			if (TextUtil.isEmpty(value)) {
				String embedded = request.getParameter(PageVariables.EMBEDDED);
				if (!TextUtil.isEmpty(embedded)) {
					PageVariables pageVariables = new PageVariables();
					pageVariables.getFromRequest(request);
					value = pageVariables.getVariable(key);
				}
			}
			if (TextUtil.isEmpty(value)) {
				value = (String) request.getAttribute(key);
			}
			return TextUtil.isEmpty(value) ? defaultValue : value;
		} else {
			HashMap<String, String> requestParameters = urlDecrypt(encryptedParameters);
			String value = requestParameters.get(key);
			if (TextUtil.isEmpty(value)) {
				value = TextUtil_getParamAsString(request, key);
			}
			if (TextUtil.isEmpty(value)) {
				value = (String) request.getAttribute(key);
			}
			return TextUtil.isEmpty(value) ? defaultValue : value;
		}
	}
	private static String TextUtil_getParamAsString(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null && value.length() > 0)
			return value;
		else
			return null;
	}
	public static String getParamAsString(HttpServletRequest request, String name) {
		return getParamAsString(request, name, "");
	}
	
	public static String getParamAsString(HttpServletRequest request, String name,boolean isCORIS) {
		return getParamAsString(request, name, "",isCORIS);
	}
	
	/**
	 * Extracts a Boolean object from the named HttpServletRequest parameter.
	 * 
	 * This method traps all exceptions that might occur during extraction, 
	 * so if the named parameter does not exist or an exception occurs
	 * this method shall log the exception and return a null.
	 * 
	 * This method follows the rules of the 'new Boolean(String s)' constructor.
	 * 
	 * @param request The HttpServletRequest object.
	 * @param name The name of the HttpServletRequest parameter to extract an int from.
	 * @return A Boolean object containing the value of the named HttpServletRequest 
	 * parameter or null.
	 */
	public static Boolean getParamAsBoolean(HttpServletRequest request, String name) {
		String param = getParamAsString(request, name);
		
		Boolean value = false;
		try {
			if (!TextUtil.isEmpty(param)) {
				value = "true".equalsIgnoreCase(param) ||
						   "t".equalsIgnoreCase(param) ||
						 "yes".equalsIgnoreCase(param) ||
						   "y".equalsIgnoreCase(param) ||
						   "1".equalsIgnoreCase(param);
			}
		} catch (NumberFormatException e) { /* do nothing */
		} catch (Exception e) {
			logger.debug(".getParamAsBoolean(request, " + name + ") [Exception]", e);
		}
		return value;
	}
	
	/**
	 * Extracts a Date Object from the named HttpServletRequest parameter.
	 * 
	 * NOTE: The date format must be either "MM-dd-yy" or "MM-dd-yyyy".
	 * 
	 * This method traps all exceptions that might occur during extraction, 
	 * so if the named parameter does not exist or it's value is not a date, 
	 * this method shall log the exception and return a null.
	 * 
	 * Modification Note (01-15-2004, Dave Hayward)
	 * Since a null or empty string value is very common, just return null
	 * without logging an exception.
	 * 
	 * @param request The HttpServletRequest object.
	 * @param name The name of the paramter to get an Date from.
	 * @return A valid Date object or null if unable to parse a date.
	 */
	public static Date getParamAsDate(HttpServletRequest request, String name) {
		String param = getParamAsString(request, name);
		
		Date value = null;
		try {
			param = param.replaceAll("/", "-");
			value = TextUtil.parseDate(param, DEFAULT_DATE_FORMAT);
		} catch (Exception e) {
			logger.debug(".getParamAsDate(request, " + name + ") [Exception]", e);
		}
		return value;
	}
	
	public static Date getParamAsDate(HttpServletRequest request, String name, String dateFormat) {
		String param = getParamAsString(request, name);
		
		Date value = null;
		try {
			param = param.replaceAll("/", "-");
			value = TextUtil.parseDate(param, dateFormat);
		} catch (Exception e) {
			logger.debug(".getParamAsDate(request, " + name + ") [Exception]", e);
		}
		return value;
	}
	
	/**
	 * Extracts a Date Object from the named HttpServletRequest parameter.
	 * 
	 * NOTE: The date format must be either "MM-dd-yy" or "MM-dd-yyyy".
	 * 
	 * This method traps all exceptions that might occur during extraction, 
	 * so if the named parameter does not exist or it's value is not a date, 
	 * this method shall log the exception and return a null.
	 * 
	 * Modification Note (01-15-2004, Dave Hayward)
	 * Since a null or empty string value is very common, just return null
	 * without logging an exception.
	 * 
	 * @param request The HttpServletRequest object.
	 * @param name The name of the paramter to get an Date from.
	 * @return A valid Date object or null if unable to parse a date.
	 */
	public static Date getParamAsDateTime(HttpServletRequest request, String name) {
		String param = getParamAsString(request, name);
		
		Date value = null;
		try {
			param = param.replaceAll("/", "-");			
			if (param.substring((param.lastIndexOf("-") + 1)).length() == 2)
				value = TextUtil.parseDate(param, DEFAULT_DATETIME_FORMAT);
			else
				value = TextUtil.parseDate(param, DEFAULT_DATETIME_FORMAT);
		} catch (Exception e) {
			logger.debug(".getParamAsDateTime(request, " + name + ") [Exception]", e);
		}
		return(value);
	}
	
	public static Date getParamAsDateTime(HttpServletRequest request, String name,String dateFormat) {
		String param = getParamAsString(request, name);
		
		Date value = null;
		try {
			param = param.replaceAll("/", "-");	
			value = TextUtil.parseDate(param, dateFormat);
		} catch (Exception e) {
			logger.debug(".getParamAsDateTime(request, " + name + ") [Exception]", e);
			
		}
		return(value);
	}
	
	/**
	 * Extracts an int value from the named HttpServletRequest parameter.
	 * 
	 * This method traps all exceptions that might occur during extraction, 
	 * so if the named parameter does not exist or it's value is not numeric, 
	 * this method shall log the exception and return a zero (0).
	 * 
	 * @param request The HttpServletRequest object.
	 * @param name The name of the HttpServletRequest parameter to extract an int from.
	 * @return The int value of the named HttpServletRequest parameter or zero (0).
	 */
	public static int getParamAsInt(HttpServletRequest request, String name, int defaultValue) {
		String param = getParamAsString(request, name);
		
		int value = defaultValue;
		try {
			value = Integer.parseInt(param);
		} catch (NumberFormatException e) { /* do nothing */
		} catch (Exception e) {
			logger.debug(".getParamAsInt(request, " + name + ") [Exception]", e);
		}
		return value;
	}
	public static int getParamAsInt(HttpServletRequest request, String name) {
		return getParamAsInt(request, name, 0); 
	}
	
	/**
	 * Extracts an int value from the named HttpServletRequest parameter.
	 * 
	 * This method traps all exceptions that might occur during extraction, 
	 * so if the named parameter does not exist or it's value is not numeric, 
	 * this method shall log the exception and return a zero (0).
	 * 
	 * @param request The HttpServletRequest object.
	 * @param name The name of the HttpServletRequest parameter to extract an double from.
	 * @return The int value of the named HttpServletRequest parameter or zero (0).
	 */
	public static double getParamAsDouble(HttpServletRequest request, String name) {
		String param = getParamAsString(request, name);
		
		double value = 0;
		try {
			value = Double.parseDouble(param);
		} catch (NumberFormatException e) { /* do nothing */
		} catch (Exception e) {
			logger.debug(".getParamAsDouble(request, " + name + ") [Exception]", e);
		}
		return(value);
	}
	
	/**
	 * Extracts an Integer object from the named HttpServletRequest parameter.
	 * 
	 * This method traps all exceptions that might occur during extraction, 
	 * so if the named parameter does not exist or it's value is not numeric, 
	 * this method shall log the exception and return a null.
	 * 
	 * @param request The HttpServletRequest object.
	 * @param name The name of the HttpServletRequest parameter to extract an int from.
	 * @return An Integer object containing the value of the named HttpServletRequest 
	 * parameter or null.
	 */
	public static Integer getParamAsInteger(HttpServletRequest request, String name) {
		String param = getParamAsString(request, name);
		
		Integer value = null;
		try {
			value = new Integer(param);
		} catch (NumberFormatException e) { /* do nothing */
		} catch (Exception e) {
			logger.debug(".getParamAsInteger(request, " + name + ") [Exception]", e);
		}
		return(value);
	}
	
	/**
	 * Extracts a Long object from the named HttpServletRequest parameter.
	 * 
	 * This method traps all exceptions that might occur during extraction, 
	 * so if the named parameter does not exist or it's value is not numeric, 
	 * this method shall log the exception and return a null.
	 * 
	 * @param request The HttpServletRequest object.
	 * @param name The name of the HttpServletRequest parameter to extract an int from.
	 * @return A Long object containing the value of the named HttpServletRequest 
	 * parameter or null.
	 */
	public static Long getParamAsLong(HttpServletRequest request, String name) {
		String param = getParamAsString(request, name);
		
		Long value = null;
		try {
			value = new Long(param);
		} catch (NumberFormatException e) { /* do nothing */
		} catch (Exception e) {
			logger.debug(".getParamAsLong(request, " + name + ") [Exception]", e);
		}
		return(value);
	}
	
	public static String getParamAsString(HttpServletRequest request, String key, String defaultValue,boolean isCORIS) {
		String encryptedParameters = TextUtil_getParamAsString(request, REQUEST_PARAMETER);
		String value = TextUtil_getParamAsString(request, key);
		
		if (value ==  null) {
			HashMap<String, String> requestParameters = null;
				requestParameters = urlDecrypt(encryptedParameters);
				value = requestParameters.get(key);
				if (TextUtil.isEmpty(value)) {
					value = TextUtil_getParamAsString(request, key);
				}
		}
		return TextUtil.isEmpty(value) ? defaultValue : value;
	}

	
	public static String encryptStatic(String parameters) {
		String encoded = "";
		try {
			SAPEncryptDecrypt cryptor = new SAPEncryptDecrypt("DESede", SAPEncryptDecrypt.DEFAULT_ENCRYPTION_KEY);
			if (!TextUtil.isEmpty(parameters)) {
				String encrypted = cryptor.encrypt(stripBadCharacters(parameters));
				encoded = URLEncoder.encode(encrypted, "UTF-8");
			}
		} catch (Exception e) {
			System.err.println(e);	  
		}	
		return  encoded;
	}
	
	/**
	 * method to decrypt request parameters
	 */
	public static String decryptStatic(String parameters) {
		String decoded = "";
		try {
			SAPEncryptDecrypt cryptor = new SAPEncryptDecrypt("DESede", SAPEncryptDecrypt.DEFAULT_ENCRYPTION_KEY);
			decoded = cryptor.decrypt(parameters);
		
		} catch (Exception e) {
			System.err.println(e);	  
		}		
		return decoded;
	}
	
	
}
