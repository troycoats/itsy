package com.itsy.constants;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import com.itsy.config.XMLConfig;

public interface Constants {
	/** Id used to specify the id is invalid. */
	public static final long INVALID_ID = -1;
	/** Id used to specify the id is invalid. */
	public static final int INVALID_ID_INT = -1;
	/** used to format dates for display.*/
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	/** Used to format dates for viewing the time only.*/
	public static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
	/** Used to format dates for time and date.*/
	public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
	/** Used to parse and format the hour.*/
	public static SimpleDateFormat hourFormat = new SimpleDateFormat("hh");
	/** Used to parse and format the minute.*/
	public static SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
	/** Used to parse and format the meridiem.*/
	public static SimpleDateFormat meridiemFormat = new SimpleDateFormat("a");
	/** Used to parse and format the meridiem.*/
	public static SimpleDateFormat longTextFormat = new SimpleDateFormat("MMMMM dd, yyyy");
	/** Used to format decimals. */
	public static final DecimalFormat accountingDecimalFormatter = new DecimalFormat("#,##0.00");
	/** String format for dates in care.*/
	public static final String dateFormatString = "yyyy-MM-dd";
	public static final SimpleDateFormat dateSlashFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	public static final String UNKNOWN = "UNKNOWN";
	
	public static final SimpleDateFormat hourMinute = new SimpleDateFormat("hh:mm");
	public static final SimpleDateFormat dateTimeLong = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	
	/** this will get us a 0-23 hr format */
	public static final SimpleDateFormat dateTimeKLong = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static final SimpleDateFormat militaryTimeFormat = new SimpleDateFormat("HH:mm");
	public static final SimpleDateFormat hourTimeFormat = new SimpleDateFormat("hh:mm");
	
	public static final String dateTimeLongStr = "yyyy-MM-dd hh:mm";
	public static final String dateTimeKLongStr = "yyyy-MM-dd HH:mm";
	public static final String hourMinuteString = "hh:mm";
	public static final String MilitaryMinuteString = "HH:mm";
	
	public final static SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
	public final static SimpleDateFormat yearFmt = new SimpleDateFormat("yyyy");
	
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
	
	public static final String ASTERISK = "<font color=\"red\" face=\"arial\">*</font>";
	
	public static final String UNIQUE_WINDOW_ID = "uniqueIdWindow";
	public static final String PAGE_VARIABLES = "pageVariables";
	
	public static final String REQUEST_PERMISSION = "permission";
	public static final char[] REMOVE_CHARACTERS_FROM_FILE_DESCRIPTIONS_CHARS = {'/', '\\', ':', '*', '|', '<', '>', '%', '"', '&'};
	
	public static final String SYSTEM_VERSION = XMLConfig.getProperty("PROPERTY.system_version");
	
	public static final String RESULTS = "results";
	public static final String FIRST_TIME = "firstTime";
	public static final String SEARCH_PERFORMED = "searched";
	
	public static final String CLOSE_PAGE = "jsp/closeAllPopups.jsp";
	public static final String ACCESS_DENIED_PAGE = "jsp/accessDenied.jsp";
	
	public static final String MAIN_PAGE = "jsp/main.jsp";
	public static final String MAIN_PAGE_PERMISSIONS = "/itsy/MainServlet";

	public static final String LOGOUT_PAGE = "jsp/logout.jsp";
	
	public static final String TASK_LIST_PAGE = "jsp/taskList.jsp";
	public static final String TASK_LIST_PAGE_PERMISSIONS = "/itsy/TaskListServlet";
	
}
