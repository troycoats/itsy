package com.itsy.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.itsy.dataaccess.base.Optionable;

public class OptionUtil {

	private static Logger logger = Logger.getLogger(OptionUtil.class);
	
	/**
	 * generic method to create a list of options for a select dropdown
	 * @param selectedOption String
	 * @param options Object
	 * @param ids Object
	 * @param boolean showBlankSelection
	 * @return String
	 */
	public static String createSelectOptionList(String selectedOption, Object options, Object ids, boolean showBlankSelection) {
		StringBuilder returnValue = new StringBuilder();
		String description = null;
		String id = "";
		if (ids != null && ids instanceof ArrayList) {
			id = (String) (((List) ids).get(0));
		}
		String blankValue = TextUtil.isNumeric(id) && !"0".equals(id) ? "0" : "";
		try {
			String selected = null;
			int i = 0;
			if (options != null && ((List) options).size() > 0) {
				int size = ((List) options).size();
				
				if (showBlankSelection) {
					returnValue.append("<option value='" + blankValue + "'></option>");
				}
				
				Iterator it = ((List) options).iterator();
				while (it.hasNext()) {
					Object option = it.next();
					
					if (option instanceof Optionable) {	
						id = Integer.toString(((Optionable) option).getId());
						description = ((Optionable) option).getDescription();
					} else if (option instanceof Integer) {
						id = ((Integer) option).toString();
						description = id;
					} else if (option instanceof String) {
						if (ids != null && ids instanceof ArrayList) {
							id = (String) (((List) ids).get(i));
							description = (String) option;
							
							i++;
						} else {	
							id = (String) option;
							description = id;
						}
					}
					selected = id.equals(selectedOption) || size == 1 ? "SELECTED" : "";
					returnValue.append("<option " + selected + " value='" + id + "'>" + TextUtil.maxLength(description, 50) + "</option>");
				}
			} else {
				returnValue.append("<option value='" + blankValue + "'>NONE AVAILABLE</option>");
			}
		} catch (Exception e) {
			logger.error("Error occurred while retrieving options. " + e.getMessage(), e);
		}
		options = null;
		
		return returnValue.toString();
	}
	
	/**
	 * generic method to create a list of options for a select dropdown
	 * @param selectedOption String
	 * @param options List<Optionable> 
	 * @return String
	 */
	public static String getSelectedOptionDescription(String selectedOption, List<Optionable> options) {
		Iterator it = ((List) options).iterator();
		while (it.hasNext()) {
			Object option = it.next();
			if (!TextUtil.isEmpty(selectedOption) && ((Optionable) option).getId() == Integer.parseInt(selectedOption)) {
				return ((Optionable) option).getDescription();
			}
		}
		return "";
	}
	
	public static String isSelected(String value, String compare) {
		return compare.equals(value) ? "SELECTED" : "";
	}
}
