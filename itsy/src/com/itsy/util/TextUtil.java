package com.itsy.util;

import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.itsy.constants.Constants;

public class TextUtil {
	  
	/**
	 * Logger object.
	 */
	private static Logger logger = Logger.getLogger(TextUtil.class);
	
	/**
	 * The default date format is "MM-dd-yyyy".
	 * This is used when a date format is unspecified.
	 */
	public static final String DEFAULT_DATE_FORMAT = "MM-dd-yyyy";

	/**
	 * The  date format is "MM/dd/yyyy".
	 * 
	 */
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	
	public static final String DATE_FORMAT_YEAR_FIRST = "yyyy/MM/dd";
	
	/**
	 * Regular Expression for MM/dd/yyyy.  
	 * Using this because parse date does not catch errors like 00/01/2012, 15/01/2012 (These dates should be errors?)
	 * 
	 * (			#start of group #1
	 *  0?[1-9]		#  01-09 or 1-9
	 *  |           #  ..or
	 *  [12][0-9]	#  10-19 or 20-29
	 *  |			#  ..or
	 *  3[01]		#  30, 31
	 * ) 			#end of group #1
	 *   /			#  follow by a "/"
	 *    (			#    start of group #2
	 *     0?[1-9]	#	01-09 or 1-9
	 *     |		#	..or
	 *     1[012]	#	10,11,12
	 *     )		#    end of group #2
	 *      /		#	follow by a "/"
	 *       (		#	  start of group #3
	 *        (19|20)\\d\\d	#	    19[0-9][0-9] or 20[0-9][0-9]
	 *        )		#	  end of group #3
   	 */
	public static final String DATE_FORMAT_RE = "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)";

	/**
	 * The default datetime format is "MM-dd-yyyy hh:mm:ss".
	 * This is used when a datetime format is unspecified.
	 */
	public static final String DEFAULT_DATETIME_FORMAT = "MM-dd-yyyy HH:mm:ss";
	
	/**
	 * The default time format is "hh:mm a".
	 * This is used when a time format is unspecified.
	 */
	public static final String DEFAULT_TIME_FORMAT = "hh:mm a";
	
	/**
	 * The short date format is "MM-dd-yy".
	 */
	public static final String SHORT_DATE_FORMAT = "MM-dd-yy";
	
	/**
	 * The default decimal format is "###0.00" (currency amount without the '$' and the ',' symbols).
	 * This is used when a number format is unspecified.
	 */
	public static final String DEFAULT_DECIMAL_FORMAT = "###0.00";
	
	/**
	 * The accounting number format is "#,##0.00" (currency amount without the '$' but has the ',' symbols).
	 */
	public static final String ACCOUNTING_NUMBER_FORMAT = "#,##0.00";
	/**
	 * The default identifier format is "000000" .
	 * This is used when a identifier is displayed.
	 */
	public static final String DEFAULT_IDENTIFIER_FORMAT = "000000";
	
	
	/**
	 * Used to format numbers (including decimals) to the format of "123.1" and 123.0 (float) is "123" -- 
	 * note that numbers are rounded to the 4th decimal place.
	 */
	public static final DecimalFormat shortDecimalFormatter = new DecimalFormat("#.####");

	/**
	 * Used to format numbers (including decimals) to the format of "123.1" and 123.0 (float) is "123" -- 
	 * note that numbers are rounded to the 2nd decimal place.
	 */
	public static final DecimalFormat shortRightDecimalFormatter = new DecimalFormat("#.##");
	
	
	/**
	 * Used to format dates to the format of MMMM d, yyyy (i.e. January 3. 2003).
	 */
	public static final SimpleDateFormat longDateFormatter = new SimpleDateFormat("MMMM d, yyyy");
	
	/**
	 * Used to format a number into currency.
	 */
	public static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
	
	/**
	 * Email Regular Expression
	 * ^				# start of the line
	 * [_A-Za-z0-9-]+	# must start with string in the bracket [ ], must contains one or more (+)
	 * (				# start of group #1
	 *    \\.[_A-Za-z0-9-]+	# follow by a dot "." and string in the bracket [ ], must contains one or more (+)
	 * )*				# end of group #1, this group is optional (*)
	 * @				# must contains a "@" symbol
	 * [A-Za-z0-9]+     # follow by string in the bracket [ ], must contains one or more (+)
	 * (				# start of group #2 - first level TLD checking
	 * \\.[A-Za-z0-9]+  # follow by a dot "." and string in the bracket [ ], must contains one or more (+)
	 * )*				# end of group #2, this group is optional (*)
	 * (				# start of group #3 - second level TLD checking
	 *  \\.[A-Za-z]{2,} # follow by a dot "." and string in the bracket [ ], with minimum length of 2
	 * )				# end of group #3
	 * $				#end of the line
	 */
	public static final String EmailFormatRE = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"; 
	
	/**
	 * User Name Regular Expression
	 * ^                    # Start of the line
	 * [a-z0-9_-]	     # Match characters and symbols in the list, a-z, 0-9 , underscore , hyphen
	 * {3,15}  # Length at least 3 characters and maximum length of 15 
	 * $                    # End of the line
	 */
	public static final String UserNameRE = "^[a-z0-9_-]{3,15}$";
	
	/**
	 * Password Regular Expression
	 * (			# Start of group
	 * (?=.*\d)		#   must contains one digit from 0-9
	 * (?=.*[a-z])	#   must contains one lowercase characters
	 * (?=.*[A-Z])	#   must contains one uppercase characters
	 * .			#     match anything with previous condition checking
	 * {6,20}		#        length at least 6 characters and maximum of 20	
	 * )			# End of group	 
	 */
	public static final String PasswordRE = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})"; 
	
	static {
		shortDecimalFormatter.setDecimalSeparatorAlwaysShown(false);
	}
	
	
	/**
	 * (private) TextUtil Constructor.
	 */
	private TextUtil() {
		super();
	}
	
	/**
	 * Checks to see if <code>s</code> contains SQL wildcard characters (e.g.
	 * <code>%</code>).
	 * 
	 * @param s the String to check
	 * @return <code>true</code> if the String contains wildcards, 
	 * 			<code>false</code> otherwise.
	 */
	public static boolean containsWildcards( String s )
	{
		boolean containsPercent = s.indexOf('%') >= 0;

		return containsPercent;	
	}
	
	/**
	 * Convenience method for adding query-string parameters to a URL.  This 
	 * method only adds the key/value pair if the key is not null.  If the 
	 * parameter value is null, the value is left off.
	 * 
	 * @param baseUrl A (base/starting) URL (with or without an existing
	 * query-string).
	 * @param key The parameter key to add (required).
	 * @param value The parameter value to add (optional).
	 * @return A new URL with the key/value pair added as a query-string parameter.
	 */
	public static final String addParam(String baseUrl, String key, String value) {
		StringBuffer newUrl = new StringBuffer(baseUrl);
		if (key != null && key.length() > 0) {
			if (baseUrl.indexOf("?") < 0)
				newUrl.append("?");
			else
				newUrl.append("&");
			
			newUrl.append(key).append("=");
			
			if (value != null && value.length() > 0)
				newUrl.append(value);
		}
		return(newUrl.toString());
	}
	
	

	/**
	 * Format the given date using into a date string in 'MM/dd/yyyy' form. 
	 **/
	public static final String formatDateForDomino(final java.util.GregorianCalendar date) {
		String value = "";
		value += date.get(GregorianCalendar.YEAR);
		int month = date.get(GregorianCalendar.MONTH) + 1;
		if (month < 10) {
			value += "-0" + month;
		} else {
			value += "-" + month;
		}
		int day = date.get(GregorianCalendar.DATE);
		if (day < 10) {
			value += "-0" + day;
		} else {
			value += "-" + day;
		}
		return value;
	}
	
	/**
	 * This method merely returns the String of the int value for a month
	 **/
	public static final String getMonthString(int month) {
		String result = "";
		switch (month) {
			case java.util.Calendar.JANUARY :
				result = "January";
				break;
			case java.util.Calendar.FEBRUARY :
				result = "February";
				break;
			case java.util.Calendar.MARCH :
				result = "March";
				break;
			case java.util.Calendar.APRIL :
				result = "April";
				break;
			case java.util.Calendar.MAY :
				result = "May";
				break;
			case java.util.Calendar.JUNE :
				result = "June";
				break;
			case java.util.Calendar.JULY :
				result = "July";
				break;
			case java.util.Calendar.AUGUST :
				result = "August";
				break;
			case java.util.Calendar.SEPTEMBER :
				result = "September";
				break;
			case java.util.Calendar.OCTOBER :
				result = "October";
				break;
			case java.util.Calendar.NOVEMBER :
				result = "November";
				break;
			case java.util.Calendar.DECEMBER :
				result = "December";
				break;
		}
		return result;
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
		Boolean value = null;
		try {
			if (request.getParameter(name) != null)
				value =Boolean.valueOf(request.getParameter(name));
		} catch (NumberFormatException e) { /* do nothing */
		} catch (Exception e) {
			logger.debug(".getParamAsBoolean(request, " + name + ") [Exception]", e);
		}
		return(value);
	}
	
	public static boolean getParamAsBooleanValue(HttpServletRequest request, String name) {
		Boolean value = new Boolean(false);
		try {
			if (request.getParameter(name) != null)
				value =Boolean.valueOf(request.getParameter(name));
		} catch (NumberFormatException e) { /* do nothing */
		} catch (Exception e) {
			logger.debug(".getParamAsBoolean(request, " + name + ") [Exception]", e);
		}
		return(value);
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
		Date value = null;
		try {
			String sParam = request.getParameter(name);
			sParam = sParam.replaceAll("/", "-");
			value = parseDate(sParam, Constants.dateFormatString);
		} catch (Exception e) {
			logger.debug(".getParamAsDate(request, " + name + ") [Exception]", e);
		}
		return(value);
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
		Date value = null;
		try {
			String sParam = request.getParameter(name);
			if (sParam.substring((sParam.lastIndexOf("-") + 1)).length() == 2)
				value = parseDate(sParam, DEFAULT_DATETIME_FORMAT);
			else
				value = parseDate(sParam, DEFAULT_DATETIME_FORMAT);
		} catch (Exception e) {
			logger.debug(".getParamAsDateTime(request, " + name + ") [Exception]", e);
		}
		return(value);
	}
	
	public static Date getParamAsDateTime(HttpServletRequest request, String name,String dateFormat) {
		Date value = null;
		try {
			String sParam = request.getParameter(name);
			value = parseDate(sParam, dateFormat);
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
	public static int getParamAsInt(HttpServletRequest request, String name) {
		int value = 0;
		try {
			value = Integer.parseInt(request.getParameter(name));
		} catch (NumberFormatException e) { /* do nothing */
		} catch (Exception e) {
			logger.debug(".getParamAsInt(request, " + name + ") [Exception]", e);
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
	public static double getParamAsDouble(HttpServletRequest request, String name) {
		double value = 0;
		try {
			value = Double.parseDouble(request.getParameter(name));
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
		Integer value = null;
		try {
			value = new Integer(request.getParameter(name));
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
		Long value = null;
		try {
			value = new Long(request.getParameter(name));
		} catch (NumberFormatException e) { /* do nothing */
		} catch (Exception e) {
			logger.debug(".getParamAsLong(request, " + name + ") [Exception]", e);
		}
		return(value);
	}
	
	/**
	 * Extracts a String Object from the named HttpServletRequest parameter.
	 * 
	 * If the named parameter does not exist or it's value is empty  
	 * (length == 0), this method shall return a null.
	 * 
	 * @param request The HttpServletRequest object.
	 * @param name The name of the paramter to get an Date from.
	 * @return A valid String object or null if missing or length is 0.
	 */
	public static String getParamAsString(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null && value.length() > 0)
			return(value);
		else
			return(null);
	}
	
	public static final String isChecked(String value, String dataValue) {
		String checked = "";
		if (value.equalsIgnoreCase(dataValue)) {
			checked = "checked";
		}
		return checked;
	}
	
	public static final String isChecked(Boolean b) {
		if (b != null && b.booleanValue())
			return "checked";
		else
			return "";
	}
	
	public static final String isChecked(boolean b) {
		if (b)
			return "checked";
		else
			return "";
	}
	public static final String isChecked(String value, Enumeration dataValues) {
		String checked = "";
		while (dataValues.hasMoreElements()) {
			if (value.equals((String) dataValues.nextElement())) {
				checked = "checked";
			}
		}
		return checked;
	}
	
	public static final String isSelected(Boolean b) {
		if (b != null && b.booleanValue())
			return "selected";
		else
			return "";
	}
	
	public static final String isSelected(boolean b) {
		if (b)
			return "selected";
		else
			return "";
	}
	
	public static final String isSelected(String value, String dataValue) {
		String selected = "";
		if (value != null && value.equalsIgnoreCase(dataValue)) {
			selected = "selected";
		}
		return selected;
	}
	
	public static final String isSelected(Object value, Object dataValue) {
		String selected = "";
		if (value != null && value.equals(dataValue)) {
			selected = "selected";
		}
		return selected;
	}
	
	public static final String isSelected(long value, long dataValue) {
		String selected = "";
		if (value == dataValue) {
			selected = "selected";
		}
		return selected;
	}
	public static final String isSelected(String value, Enumeration dataValues) {
		String selected = "";
		while (dataValues.hasMoreElements()) {
			if (value.equals((String) dataValues.nextElement())) {
				selected = "selected";
			}
		}
		return selected;
	}
	
	public static final String isSelected(String s1, String[] s2) {

		String sval = "";

		for (int i = 0; i < s2.length; i++) {

			if (s1.equals(s2[i])) {

				sval = "selected";

			}

		}

		return sval;

	}
	
	/**
	 * Checks every character in a string to test for a valid number digit 
	 **/
	public static boolean isNumber(String param) {
		if (param == null || param.length() < 1) {
			return false;
		}
		char[] charArray = param.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (Character.isLetter(charArray[i])) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks every character in a string removing all non-numeric charaters.
	 * @param param - string containing alphanumeric characters
	 * @return String - of only numeric characters
	 **/
	public static String makeNumber(String param) {
		StringBuffer retVal = new StringBuffer();
		if (param == null || param.length() < 1) {
			return "";
		}
		char[] charArray = param.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (Character.isDigit(charArray[i])) {
				retVal.append(charArray[i]);
			}
		}
		return retVal.toString();
	}
	
	/**	
	 * Parses a Date value from the String data sent to it using the default
	 * date format mask (DEFAULT_DATE_FORMAT).
	 * 
	 * @param data String data to be parsed into a Date object.
	 * @return The Date object parsed from the data or null if unable to parse.
	 */
	public static final Date parseDate(String data) {
		return (parseDate(data, DEFAULT_DATE_FORMAT));
	}
	
	/**	
	 * Parses a date value from the String data sent to it using the
	 * dateFormat provided.
	 * 	
	 * @param data String data to be parsed into a Date object.
	 * @param dateFormat The format mask to use on the data.
	 * @return The Date object parsed from the data or null if unable to parse.
	 */
	public static final Date parseDate(String data, String dateFormat) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
		try {
			return (dateFormatter.parse(data));
		} catch (ParseException e) {
			return (null);
		}
	}
	
	public static final Date parseAndValidateDate(String data) {
		try{
			if(data.matches(DATE_FORMAT_RE)){
				int year = Integer.parseInt(data.substring(6,10));
				int month = Integer.parseInt(data.substring(0,2));
				int day = Integer.parseInt(data.substring(3,5));
				if(year < 1900 || month < 1 || month > 12 || day < 1 || day > 31){
					return null;
				}
				return new Date(year-1900,month-1,day);
			}
		}
		catch (Exception e) {
			//ignoring the exception
		}
		return null;
	}
	
	public static final boolean validateEmail(String email){
		return email.matches(EmailFormatRE);
	}
	
	public static final boolean validatePasswordStrength(String password){
		return password.matches(PasswordRE);
	}
	
	public static final boolean validateUserName(String userName){
		return userName.matches(UserNameRE);
	}
	

	
	/**
	 * Converts the three (hour, minute, ampm) String fields into a Date object.
	 * 
	 * @param hour The hour in am/pm (1-12). 
	 * @param minute The minute of an hour (00-59).
	 * @param ampm The am/pm marker (either "AM" or "PM").
	 * @return A Date object representing the hour and minute entered by the user.
	 */
	public static Date getDateFromStrings(String hour, String minute, String ampm) {
		logger.debug(".getDateFromStrings(" + hour + "," + minute + "," + ampm + ") start");
		return(TextUtil.parseDate(hour + ":" + minute + " " + ampm, TextUtil.DEFAULT_TIME_FORMAT));
	}
	
	/**
	 * used with JSP's so that you don't have nullpointers while attempting to print a field.
	 * instead of null, an empty string is returned.
	 */
	public static String print(long param) {
		if (param == 0) {
			return "";
		}
		return param + "";
	}
	
	/**
	 * used with JSP's so that you don't have nullpointers while attempting to print a field.
	 * instead of null, an empty string is returned.
	 */
	public static String print(int param) {
		if (param == 0) {
			return "";
		}
		return param + "";
	}
	
	/**
	 * used with JSP's so that you don't have nullpointers while attempting to print a field.
	 * instead of null, an empty string is returned.
	 * If the boolean value is true returns  "true" else, it returns an empty string.
	 */
	public static String printStringValueForboolean(boolean param) {
		String retVal = "";
		if (param) {
			retVal = "true";
		}  else {
			retVal = "";			
		}
		return retVal;
	}

	/**
	 * used with JSP's so that you don't have nullpointers while attempting to print a field.
	 * instead of null, an empty string is returned.
	 * If the boolean value is true returns  "true" else, it returns an empty string.
	 */
	public static String printStringValueForbooleanTrueFalse(boolean param) {
		String retVal = "";
		if (param) {
			retVal = "true";
		}  else {
			retVal = "false";			
		}
		return retVal;
	}
	
	
	/**
	 * used with JSP's so that you don't have nullpointers while attempting to print a field.
	 * instead of null, an empty string is returned.
	 */
	public static String printIfIntEqualZeroReturnZero(int param) {
		if (param == 0) {
			return "0";
		}
		return param + "";
	}
	/**
	 * used with JSP's so that you don't have nullpointers while attempting to print a field.
	 * instead of null, an empty string is returned.
	 */
	public static String print(Object param) {
		if (param instanceof String) {
			if ("null".equalsIgnoreCase((String) param))
				return "";
		}
		if (param == null) {
			return "";
		}
		return replaceHTMLCharacters(param.toString());
	}
	
	public static String printWeb(Object param) {
		if (param == null || "".equals(param.toString().trim())) {
			return "&nbsp;";
		}
		return replaceHTMLCharacters(param.toString());
	}

	/**
	 * Used to display only the first <code>length</code> characters of long strings.
	 */
	public static String printStart(Object param, int length) {
		String retVal = "";
		if (param != null) {
			int origLen = param.toString().length();
			if (length > origLen) {
				length = origLen;
				retVal = param.toString();
			} else {
				retVal = param.toString().substring(0, param.toString().lastIndexOf(" ", length))
						.concat(" ...");
			}
		}
		return retVal;
	}

	/**
	 * used with JSP's so that you don't have nullpointers while attempting to print a field.
	 * instead of null, an empty string is returned.
	 */
	public static String printNoHTMLCharConversion(Object param) {
		if (param == null) {
			return "";
		}
		return param.toString();
	}
	
	/**
	 * Used with JSPs to print Date objects with the default ("MM-dd-yyyy")
	 * date format.  If the date parameter passed-in is a null, an empty
	 * String is returned.
	 * 
	 * @param date The Date object to format.
	 * @return A formatted Date object with the default date format.
	 */
	public static final String printDate(Date date) {
		return(printDate(date, TextUtil.DEFAULT_DATE_FORMAT));
	}
	
	/**
	 * Used with JSPs to print Date objects with the provided date
	 * format.  If the date parameter passed-in is a null, an empty
	 * String is returned.
	 * 
	 * @param date The Date object to format.
	 * @param format The desired format to use.
	 * @return A formatted Date object with the default date format.
	 */
	public static final String printDate(Date date, String format) {
		if (date == null)
			return("");
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return(sdf.format(date));
	}
	
	/**
	 * Used with JSPs to print Date objects with the default ("MM-dd-yyyy hh:mm:ss")
	 * datetime format.  If the date parameter passed-in is a null, an empty
	 * String is returned.
	 * 
	 * @param date The Date object to format.
	 * @return A formatted Date object with the default datetime format.
	 */
	public static final String printDatetime(Date date) {
		return(printDate(date, TextUtil.DEFAULT_DATETIME_FORMAT));
	}
	
	public static final String printDatetime(Date date,String format) {
				return(printDate(date, format));
	}
	
	/**
	 * used with JSP's to print time formatted correctly.
	 * instead of null, 12 is returned.
	 * @param Expecting a partial time, example: HH (hour)
	 */
	public static String printHour(int param) {
		String retVal = "";
		if (param == 0) {
			retVal = "12";
		} else if (param < 10) {
			retVal = "0" + param;
		}else{
			retVal = param+"";	
		}
		return retVal;
	}
	
	/**
	 * used with JSP's to print time formatted correctly.
	 * instead of null,00 is returned.
	 * @param Expecting a partial time, example: HH (hour)
	 */
	public static String printMinute(int param) {
		String retVal = "";
		if (param == 0) {
			retVal = "00";
		} else if (param < 10) {
			retVal = "0" + param;
		} else if (param >= 10 ){
			retVal = param+"";
		}
		return retVal;
	}
	
	/**
	 * Used with JSPs to print Date objects with the default ("hh:mm a")
	 * time format.  If the date parameter passed-in is a null, an empty
	 * String is returned.
	 * 
	 * @param date The Date object to format.
	 * @return A formatted Date object with the default datetime format.
	 */
	public static final String printTime(Date date) {
		return(printDate(date, TextUtil.DEFAULT_TIME_FORMAT));
	}
	
	/**
	 * Used in JSP's to print Yes or No when flag is Y, N, or null
	 * @param	String that is the single character to expand
	 * @return	String
	 */
	public static String printYesNo(String param) {
		String retVal = "";
		if (param == null) {
			retVal = "No";
		} else if (param.equalsIgnoreCase("Y")) {
			retVal = "Yes";
		} else {
			retVal = "No";
		}
		return retVal;
	}
	
	/**
	 * Used in JSP's to print Yes or No when parameter is boolean.
	 * @param	boolean value
	 * @return	String "Yes" or "No"
	 */
	public static String printYesNo(boolean b)
	{
		return b?"Yes":"No";
	}

	/**
	 * Used in JSP's to print Y or N when parameter is boolean.
	 * @param	boolean value
	 * @return	String "Y" or "N"
	 */
	public static String printYesNoBoolToChar(boolean b)
	{
		return b?"Y":"N";
	}

	public static String formatPhone(String number) {
		if (number == null) {
			number = "";
		}
		String display = "";
		if (number.length() == 7) { //no area code
			display = number.substring(0, 3) + "-" + number.substring(3, 7);
		} else if (number.length() == 10) { //area code
			display = "(" + number.substring(0, 3) + ")" + number.substring(3, 6) + "-" + number.substring(6, 10);
		} else { //not known, return original
			display = number;
		}
		return display;
	}
	
	public static String formatPhoneWithSpaceAfterAreaCode(String number) {
		if (number == null) {
			number = "";
		}
		String display = "";
		if (number.length() == 7) { //no area code
			display = number.substring(0, 3) + "-" + number.substring(3, 7);
		} else if (number.length() == 10) { //area code
			display = "(" + number.substring(0, 3) + ") " + number.substring(3, 6) + "-" + number.substring(6, 10);
		} else { //not known, return original
			display = number;
		}
		return display;
	}
	
	/** 
	 * Formats a string into HTML format
	 * Creation date: (4/12/2002 10:18:26 AM) 
	 * @return java.lang.String 
	 * @param toCheck java.lang.String 
	 **/
	public static String replaceHTMLCharacters(String toCheck) {
		String newString = toCheck;
		while (newString.indexOf("'") >= 0) {
			int index = newString.indexOf("'");
			newString = newString.substring(0, index) + "&#39;" + newString.substring(index + 1);
		}
		while (newString.indexOf("\"") >= 0) {
			int index = newString.indexOf("\"");
			newString = newString.substring(0, index) + "&quot;" + newString.substring(index + 1);
		}
		return newString;
	}
	
	/** 
	 * Used for javascript. 
	 * Creation date: (4/12/2002 10:18:26 AM) 
	 * @return java.lang.String 
	 * @param toCheck java.lang.String */
	public static String replaceJavascriptCharacters(String toCheck) {
		String newString = toCheck;
		while (newString.indexOf("'") >= 0) {
			int index = newString.indexOf("'");
			newString = newString.substring(0, index) + "\\&#39;" + newString.substring(index + 1);
		}
		while (newString.indexOf("\"") >= 0) {
			int index = newString.indexOf("\"");
			newString = newString.substring(0, index) + "\\&quot;" + newString.substring(index + 1);
		}
		return newString;
	}
	
	/***
	 * Returns a string representation of date objects as mm-dd-yyyy
	 * @return java.lang.String representation of a date object
	 * @param myDate java.lang.Object can be java.util.Date or java.sql.Date
	 **/
	public static String dateFormat(Object myDate) {
		String output = "";
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		Date currentTime_1 = new Date();
		//String dateString = formatter.format(currentTime_1);
		if (myDate != null) {
			if (myDate instanceof java.sql.Date) {
				java.sql.Date nd = (java.sql.Date) myDate;
				currentTime_1 = nd;
				output = formatter.format(currentTime_1);
			} else if (myDate instanceof java.util.Date) {
				java.util.Date nd = (java.util.Date) myDate;
				currentTime_1 = nd;
				output = formatter.format(currentTime_1);
			}
			return output;
		} else
			return null;
	}	
	
	/** 
	 * Returns a string representation of date objects as mm-dd-yyyy hh:mm.
	 *
	 * @param date java.lang.Object containing the Date object.
	 * @return java.lang.String
	 */
	public static String dateTimeFormat(Object date) {
		String dateTimeString = null;
		if (date != null) {
			SimpleDateFormat formatter = Constants.dateTimeFormat;
			if (date instanceof java.sql.Date) {
				java.sql.Date thisDate = (java.sql.Date) date;
				dateTimeString = formatter.format( thisDate );
			} else if (date instanceof java.util.Date) {
				java.util.Date thisDate = (java.util.Date) date;
				dateTimeString = formatter.format( thisDate );
			}
		}
		return dateTimeString;
	}
	
	public static String dateTimeFormat(Object date,SimpleDateFormat formatter) {
		String dateTimeString = null;
		if (date != null) {
			//SimpleDateFormat formatter = NotesConstants.dateTimeFormat;
			if (date instanceof java.sql.Date) {
				java.sql.Date thisDate = (java.sql.Date) date;
				dateTimeString = formatter.format( thisDate );
			} else if (date instanceof java.util.Date) {
				java.util.Date thisDate = (java.util.Date) date;
				dateTimeString = formatter.format( thisDate );
			}
		}
		return dateTimeString;
	}
	
	/*** Returns a string representation of date objects as mm-dd-yyyy based
	 * on the divider parameter (ie. '-','/')
	 * Creation date: (4/16/2002 7:28:45 PM)
	 * @return java.lang.String
	 * @param myDate java.lang.Object
	 * @param divider String represents what is used to divide 
	 * 		the date (ie.'-'= MM-dd-yyyy, or '/'=MM/dd/yyy)
	 */
	public static String dateFormat(Object myDate, String divider) {
		String output = "";
		SimpleDateFormat formatter = new SimpleDateFormat("MM" + divider + "dd" + divider + "yyyy");
		Date currentTime_1 = new Date();
		//String dateString = formatter.format(currentTime_1);
		if (myDate != null) {
			if (myDate instanceof java.sql.Date) {
				java.sql.Date nd = (java.sql.Date) myDate;
				currentTime_1 = nd;
				output = formatter.format(currentTime_1);
			} else if (myDate instanceof java.util.Date) {
				java.util.Date nd = (java.util.Date) myDate;
				currentTime_1 = nd;
				output = formatter.format(currentTime_1);
			}
			return output;
		} else
			return null;
	}
	
	
	public static String dateFormat(Object myDate, SimpleDateFormat fmt) {
		String output = "";
		Date currentTime_1 = new Date();
		//String dateString = formatter.format(currentTime_1);
		if (myDate != null) {
			if (myDate instanceof java.sql.Date) {
				java.sql.Date nd = (java.sql.Date) myDate;
				currentTime_1 = nd;
				output = fmt.format(currentTime_1);
			} else if (myDate instanceof java.util.Date) {
				java.util.Date nd = (java.util.Date) myDate;
				currentTime_1 = nd;
				output = fmt.format(currentTime_1);
			}
			return output;
		} else
			return null;
	}
	
	/** 
	 * Returns a Date representation of date objects as mm/dd/yyyy 
	 * Creation date: (4/16/2002 7:28:45 PM) 
	 * @@return java.lang.String 
	 * @@param myDate java.lang.Object */
	public static java.util.Date dateFormat(String myDate) {
		if (myDate != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			// Parse the string back into a Date.
			ParsePosition pos = new ParsePosition(0);
			Date theDate = formatter.parse(myDate, pos);
			return theDate;
		} else
			return null;
	}
	
	
	/** 
	 * Returns a Date representation of date objects as mm/dd/yyyy based
	 * on the divider parameter (ie. '-','/')
	 * Creation date: (4/16/2002 7:28:45 PM) 
	 * @return java.lang.String 
	 * @param myDate java.lang.Object 
	 * @param divider String represents what is used to divide 
	 * 		the date (ie.'-'= MM-dd-yyyy, or '/'=MM/dd/yyy)
	 **/
	public static java.util.Date dateFormat(String myDate, String divider) {
		if (myDate != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("MM" + divider + "dd" + divider + "yyyy");
			// Parse the string back into a Date.
			ParsePosition pos = new ParsePosition(0);
			Date theDate = formatter.parse(myDate, pos);
			return theDate;
		} else
			return null;
	}
	
	/**
	 * Formats the Date
	 * @param myDate
	 * @return Date
	 */
	public static java.util.Date dateFormatMinutes(String myDate) {
		if (myDate != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			// Parse the string back into a Date.
			ParsePosition pos = new ParsePosition(0);
			Date theDate = formatter.parse(myDate, pos);
			return theDate;
		} else
			return null;
	}


	/**
     * Returns the name of the month for the given month number (1 = 'January').
     * @param month int value of month a beginning index of 1.
     * @return month name.
     */
    public static String monthNumToString(int month) {
        switch (month) {
            case 1: return "January";
            case 2: return "February";
            case 3: return "March";
            case 4: return "April";
            case 5: return "May";
            case 6: return "June";
            case 7: return "July";
            case 8: return "August";
            case 9: return "September";
            case 10: return "October";
            case 11: return "November";
            case 12: return "December";
        }
        return null;
    }
	
	/**
	 * Returns the day of the month as a string in the form of 'nth' (1st, 2nd, 3rd, etc.) for the
	 * given integer. Only works for integers up to 31.
	 *
	 * @param dayOfMonth integer for day of month
	 * @return day of the month in form of 'nth'
	 */
	public static String nthDayOfMonth(int dayOfMonth) {
		if (dayOfMonth == 1 || dayOfMonth == 21 || dayOfMonth == 31) {
			return "" + dayOfMonth + "st";
		}
		if (dayOfMonth == 2 || dayOfMonth == 22) {
			return "" + dayOfMonth + "nd";
		}
		if (dayOfMonth == 3 || dayOfMonth == 23) {
			return "" + dayOfMonth + "rd";
		}
		if ((dayOfMonth > 3 && dayOfMonth < 21) || (dayOfMonth > 23 && dayOfMonth < 31)) {
			return "" + dayOfMonth + "th";
		}
		return null;
	}
	
	/**
	 * Makes the first letter of each word in <code>theString</code> capitalized, the rest are lower case.
	 * @param theString the string to be made proper case.
	 * @return the proper-case string.
	 */
	public static String makeProper(String theString) { 
		try {
			StringReader in = new StringReader(theString.toLowerCase()); 
			boolean precededBySpace = true; 
			StringBuffer properCase = new StringBuffer();
			while(true) {
				int i = in.read();
				if (i == -1)
					break;
				char c = (char)i;
				if (c == ' ' || c == '-') {
					properCase.append(c);
					precededBySpace = true;
				} else {
					if (precededBySpace) {
						properCase.append(Character.toUpperCase(c));
					} else {
						properCase.append(c);
					}
					precededBySpace = false;
				}
			}
			return properCase.toString();
		}catch(IOException e) {
			logger.error(e);
			return theString;
		}
	}
	
	/**
	 * Makes the first letter of each word in <code>inputString</code> minus <code>suffix</code>
	 * capitalized, the rest are made lower case. The <code>suffix</code> is not affected by
	 * the processing
	 * @param theString the string to be made proper case.
	 * @param suffix the final part of the string that should not be affected.
	 * @return the proper-case string.
	 */
	public static String makeProper(String inputString, String suffix) {
		String theString = null;
		try {
			if (suffix != null && suffix.length() > 0) {
				int index = inputString.lastIndexOf(suffix);
				if (index > -1)
					theString = inputString.substring(0, index);
					
			} else {
				theString = inputString;
				suffix = "";
			}
			StringReader in = new StringReader(theString.toLowerCase());
			boolean precededBySpace = true;
			StringBuffer properCase = new StringBuffer();
			while (true) {
				int i = in.read();
				if (i == -1)
					break;
				char c = (char) i;
				if (c == ' ' || c == '-') {
					properCase.append(c);
					precededBySpace = true;
				} else {
					if (precededBySpace) {
						properCase.append(Character.toUpperCase(c));
					} else {
						properCase.append(c);
					}
					precededBySpace = false;
				}
			}
			return properCase.toString().concat(suffix);
		} catch (IOException e) {
			logger.error(e);
			return inputString;
		}
	}
	
	/**
	 * Returns true if the two strings are different.  Accounts for the case in which one of the strings is null.
	 * @param string1 the first string to be compared.
	 * @param string2 the second string to be compared.
	 * @return true if the two strings are different (case sensitive).
	 */
	public static boolean stringsAreDifferent(String string1, String string2) {
		if (string1 == string2) {
			return false;
		}
		if (string1 == null && string2 != null) {
			return true;
		}
		if (string1 != null && string2 == null) {
			return true;
		}
		return !string1.equals(string2);
	}
	/**
	 * @param date
	 * @return
	 */
	public static String dateFormatApproval(Date date) {
		String output = "";
		SimpleDateFormat formatter = new SimpleDateFormat("dd' day of 'MMMM','yyyy'.'");
		if (date != null) {
			output = formatter.format(date);
			return output;
		} else
			return null;
	}
	
	public static String longDate(Date date) {
		String output = "";
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
		if (date != null) {
			output = formatter.format(date);
			return output;
		} else
			return null;
	}
		
	/**
	 * Returns the boolean value true if the string argument is not null
	 * and is equal to, ignoring case, the string "true".  Otherwise, returns
	 * false.  This method guarantees to not throw an Exception.
	 * 
	 * NOTE:  Follows the rules defined in the Boolean object constructor.
	 * @see java.lang.Boolean.html#Boolean(java.lang.String)
	 * 
	 * @param s A String object to parse a boolean value from.
	 * @return A boolean representing the value true if the string argument is not null
	 * and is equal, ignoring case, to the string "true".  Otherwise, returns false.
	 */
	public static boolean booleanValue(String s) {
		boolean b = false;
		try {
			b = (Boolean.valueOf(s)).booleanValue();
		}
		catch (Exception e) { /* do nothing. */ }
		return(b);
	}
	
	/**
	 * Returns the Date value parsed from the String provided.  This method
	 * guarantees to not throw an Exception.
	 * 
	 * @param s A String object to parse a double value from.
	 * @return The double value parsed from a String or 0.00.
	 */
	public static Date dateValue(String s) {
		Date d = null;
		try {
			d = parseDate(s);
		}
		catch(Exception e) { /* do nothing. */ }
		return(d);
	}

	
	/**
	 * Returns the double value parsed from the String provided.  This method
	 * guarantees to not throw an Exception.
	 * 
	 * @param s A String object to parse a double value from.
	 * @return The double value parsed from a String or 0.00.
	 */
	public static double doubleValue(String s) {
		double d = 0.0;
		try {
			d = Double.parseDouble(s);
		}
		catch(Exception e) { /* do nothing */ }
		return(d);
	}
	
	/**
	 * Returns the int value parsed from the String provided.  This method
	 * guarantees to not throw an Exception.
	 * 
	 * @param s A String object to parse an int value from.
	 * @return The int value parsed from a String or 0.
	 */
	public static int intValue(String s) {
		int i = 0;
		try {
			i = Integer.parseInt(s);
		}
		catch(Exception e) { /* do nothing */ }
		return(i);
	}
	
	/**
	 * Returns the long value parsed from the String provided.  This method
	 * guarantees to not throw an Exception.
	 * 
	 * @param s A String object to parse an int value from.
	 * @return The longvalue parsed from a String or 0.
	 */
	public static long longValue(String s) {
		long l = 0;
		try {
			l = Long.parseLong(s);
		}
		catch(Exception e) { /* do nothing */ }
		return(l);
	}
	
	/**
	 * Returns the largest (closest to positive infinity) double value that is 
	 * not greater than the argument (the effect is to truncate the decimal value
	 * past the second decimal position).  This method uses Java's Math.floor() 
	 * method to eliminate inaccuracies in floating-point (currency) math.
	 * 
	 * @param d A float value.
	 * @return The largest (closest to positive infinity) double value that is 
	 * not greater than the argument (the effect is to truncate the decimal value
	 * past the second decimal position).
	 */
	public static double floor(double d) {
		d = Math.floor(d * 100);
		d = (d / 100);
		return(d);
	}
	
	/**
	 * Rounds, to the nearest hundredth, a float value to currency value.  This
	 * method uses Java's Math.round() method to eliminate inaccuracies in
	 * floating-point (currency) math.
	 * 
	 * @param d A float value.
	 * @return The rounded (to the nearest hundredth) value of the float provided.
	 */
	public static double round(double d) {
		d = Math.round(d * 100);
		d = (d / 100);
		return(d);
	}
	
	public static int calculateAge(Date date, String deceased)  {
		int age = 0;
		Calendar youthCal = Calendar.getInstance();
		youthCal.setTime(date);
		boolean deceasedFlag = false;
		if(deceased.equals("Y"))  {
			deceasedFlag = true;
		}
		

		Calendar cal = Calendar.getInstance();
		if (date != null)
		{
			cal.setTime(new Date());
		}
		else if (deceasedFlag)
		{
			return -1;
		}

		int yearCount = cal.get(Calendar.YEAR) - youthCal.get(Calendar.YEAR);
		if (yearCount > 0)
		{
			int month = cal.get(Calendar.MONTH);
			int youthMonth = youthCal.get(Calendar.MONTH);
			if (youthMonth > month)
			{
				age = yearCount - 1;
			}
			else if (youthMonth < month)
			{
				age = yearCount;
			}
			else
			{
				// Months are the same, next compare days-of-month
				int day = cal.get(Calendar.DAY_OF_MONTH);
				int youthDay = youthCal.get(Calendar.DAY_OF_MONTH);
				if (youthDay > day)
				{
					age = yearCount - 1;
				}
				else
				{
					age = yearCount;
				}
			}
		}
		age = (deceasedFlag) ? age * -1 : age;
		return age;			
	}

	/**
	 * Helper method to parse given Calendar object to a String display value for the date.
	 * The SAFE 2 CARE definition of a valid date is greater than 1/1/1901, else it is null.
	 * @param cal
	 * @return String date display value
	 * @throws Exception
	 */
	public static String displayDate(Calendar cal) {
		return (cal != null && cal.get(Calendar.YEAR) > 1900) ? TextUtil
				.printDate(getGMTAdjustedDate(cal.getTime())) : "&nbsp;";
	}

	/**
	 * Helper method to parse given Calendar object to a String display value for the datetime.
	 * The SAFE 2 CARE definition of a valid date is greater than 1/1/1901, else it is null.
	 * @param cal
	 * @return String datetime display value
	 * @throws Exception
	 */
	public static String displayDatetime(Calendar cal) {
		return (cal != null && cal.get(Calendar.YEAR) > 1900) ? TextUtil
				.printDatetime(getGMTAdjustedDate(cal.getTime())) : "&nbsp;";
	}

    /**
     * The SAFE2CARE web service sends calendar objects that have no locale information. Java interprets
     * these as being GMT locale. This makes them 7 hours behind Mountain time, and show incorrect
     * values. This routine is used to convert from GMT to Mountain time (taking care to deal with
     * Daylight Saving Time appropriately). If other web services display the date values incorrectly,
     * this method would need to either be modified, or a new method added to adjust the calendar
     * values appropriately.
     * 
     * @param date object based on GMT Timezone
     * @return date object adjusted for Mountain Timezone
     */
	public static Date getGMTAdjustedDate(Date date) {

    	Calendar localCal = Calendar.getInstance();
		Calendar targetCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

		targetCal.setTime(date);
		localCal.setTime(date);

		long targetOffsetInMillis = targetCal.get(Calendar.ZONE_OFFSET)
				+ targetCal.get(Calendar.DST_OFFSET);
		long localOffsetInMillis = localCal.get(Calendar.ZONE_OFFSET)
				+ localCal.get(Calendar.DST_OFFSET);

		return new Date(date.getTime() - localOffsetInMillis + targetOffsetInMillis);
	}

	/**
	 * Helper method to build a Calendar object from the given date. If date is
	 * null, then return null without throwing an exception.
	 * @param dt Date object that holds the date to convert to a Calendar object.
	 * @return Calendar
	 */
	public static Calendar getCalendar(Date dt) {
		Calendar cal = null;
		if (dt != null) {
			cal = Calendar.getInstance();
			cal.setTime(dt);
		}
		return cal;
	}
	/**
	 * Helper method to translate the single character gender indicator to the full name.
	 * @param genderChar single character representing the gender.
	 * @return Full word describing gender.
	 */
	public static String displayGender(String genderChar) {
		String genderName = "Unknown";
		if (genderChar != null) {
			if (genderChar.equals("M"))
				genderName = "Male";
			else if (genderChar.equals("F"))
				genderName = "Female";
		}
		return genderName;
	}

	public static String bytesToMegabytes(long b) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		return nf.format((double)b / 1024 / 1024);
	}

	public static Date getTodayPlus30Days() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_MONTH, 30);
		return new Date(c.getTimeInMillis());
	}
	
	/**
	 * Replaces "illegal" characters from <code>s</code> with spaces 
	 * and returns the "clean" String.
	 * 
	 * @param s the String to clean
	 * @return the "clean" String
	 */	
	public static String filterIllegalChars( String s )
	{
		String retVal = "";

		if( s != null )
		{
			char badchars[]={'{','}','\"','|','!','@','#','$','^','[',']','<','>'};
			for( int i=0; i<badchars.length; i++)
			{
				s=s.replace(badchars[i],' ');
			}

			//let's also replace the '*' with a '%' so the 'LIKE' clauses work properly in the queries
			s=s.replace('*','%');
			retVal = s.trim();
		}
		return retVal;
	}
	
	/**
	 * Creates a new <code>java.util.Date</code> from the date string in 
	 * <code>myDate</code>.
	 * 
	 * @param myDate
	 * @return <code>java.util.Date</code> representing <code>myDate</code>.
	 */
	public static java.util.Date dateFromString(String myDate)
	{
		java.util.Date retVal = null;
		if (myDate != null)
		{
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			// Parse the string back into a Date.
			ParsePosition pos = new ParsePosition(0);
			Date theDate = formatter.parse(myDate, pos);
			retVal = theDate;
		} 
		return retVal;
	}
	
	public static java.util.Date dateFromString1(String myDate)
	{
		java.util.Date retVal = null;
		if (myDate != null)
		{
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			// Parse the string back into a Date.
			ParsePosition pos = new ParsePosition(0);
			Date theDate = formatter.parse(myDate, pos);
			retVal = theDate;
		} 
		return retVal;
	}
	
	public static boolean isEmpty(String value) {
		return value == null || value.trim().length() <= 0 || "null".equalsIgnoreCase(value);
	}
	
	public static boolean isEmpty(String[] values) {
		return values == null || values.length <= 0 || "null".equalsIgnoreCase(values[0]);
	}
	
	public static String maxLength(String value, int size) {
		return (value != null && value.length() > size) ? value.substring(0, size) + "..." : value;
	}

	public static boolean isNumeric(String input) {  
		try {  
			Integer.parseInt( input );  
			return true;  
		} catch (Exception e) {  
			return false;  
		}  
	}  
	
	public static String checkNull(String value, String defaultValue) {
		return !isEmpty(value) ? value : defaultValue;
	}
	
	public static String checkNull(String value) {
		return checkNull(value, "");
	}
	
	public static Object checkNull(Object a, Object b) {
		if (a == null) {
			return b;
		}
		return a;
	}
	
	public static String[] split(String value, String delimiter) {
		return TextUtil.isEmpty(value) ? null : (value.contains(delimiter) ? value.split(delimiter) : new String[] {value});
	}
	
	/**
	 * create a prepared statement in clause from an int[]
	 * @param int[] ids
	 * @return String
	 */
	public static String getPreparedStatementInClause(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < length; i++) {
			sb.append((sb.length() > 0 ? "," : "") + "?");
		}
		return sb.toString();
	}
	
	/**
	 * Replaces "illegal" characters with underscore 
	 * 
	 * @return the "clean" String
	 */	
	public static String removeIllegalChars(String s){
		if( s != null ) {
			char badchars[] = Constants.REMOVE_CHARACTERS_FROM_FILE_DESCRIPTIONS_CHARS;
			for( int i=0; i<badchars.length; i++) {
				s = s.replace(badchars[i], '_');
			}
		}
		return s.trim();
	}
	
	/**
	 * Returns a new String composed of the original baseString and the joinString separated by the specified delimiter
	 * @param baseString The original String
	 * @param joinString The new String to be joined with baseString
	 * @param delimiter
	 * @return result String
	 */
	public static String appendWithDelimiter(String baseString, String joinString, String delimiter) {
		if (isEmpty(baseString)) {
			return joinString;
	    } else if (isEmpty(joinString)) {
	    	return baseString;
	    } else if (!isEmpty(baseString) && !isEmpty(joinString)) {
	    	StringBuilder sb = new StringBuilder();
	    	sb.append(baseString);
	    	sb.append(print(delimiter));
	    	sb.append(joinString);
	    	return sb.toString();
	    }
		return "";
	}
	
	/**
	 * Returns a new String composed of copies of the CharSequence elements joined together with a copy of the specified delimiter
	 * @param iterable CharSequence elements
	 * @param delimiter
	 * @return result String
	 */
	public static String appendWithDelimiter(Iterable<? extends CharSequence> strings, String delimiter) {
	    Iterator<? extends CharSequence> iter = strings.iterator();
	    if (!iter.hasNext()) {
	    	return "";
	    }
	    StringBuilder sb = new StringBuilder(iter.next());
	    while (iter.hasNext()) {
	    	sb.append(delimiter).append(iter.next());
	    }
	    return sb.toString();
	}
}