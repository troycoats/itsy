package com.itsy.session;

import com.itsy.util.TextUtil;

public enum PageMode {
	ADD     	("add"),
	EDIT    	("edit"),
	FIND    	("find"),
	DELETE  	("delete"),
	RESET   	("reset"),
	CLOSE   	("close"),
	OPEN    	("open"),
	INVITE    	("invite"),
	TRANSFER	("transfer"),
	PICK    	("pick"),
	PREVIEW    	("preview"),
	FINALIZE   	("finalize"),
	NOTIFY      ("notify");
	
	PageMode (String operation) {
		this.operation = operation;
	}

	public String operation;
	
	/**
	 * Resolve the enumeration from a string value
	 * @param ofDefinition String 
	 * @return DState
	 */
	public static PageMode resolveEnumFromString(String requestAttributeString) {
		if (requestAttributeString != null) {
			if (requestAttributeString.toLowerCase().contains(ADD.operation)) 
				return ADD;
			else if (requestAttributeString.toLowerCase().contains(EDIT.operation))
				return EDIT;
			else if (requestAttributeString.toLowerCase().contains(FIND.operation))
				return FIND;
			else if (requestAttributeString.toLowerCase().contains(DELETE.operation))
				return DELETE;
			else if (requestAttributeString.toLowerCase().contains(RESET.operation))
				return RESET;
			else if (requestAttributeString.toLowerCase().contains(CLOSE.operation))
				return CLOSE;
			else if (requestAttributeString.toLowerCase().contains(INVITE.operation))
				return INVITE;
			else if (requestAttributeString.toLowerCase().contains(OPEN.operation))
				return OPEN;
			else if (requestAttributeString.toLowerCase().contains(TRANSFER.operation))
				return TRANSFER;
			else if (requestAttributeString.toLowerCase().contains(PREVIEW.operation))
				return PREVIEW;
			else if (requestAttributeString.toLowerCase().contains(FINALIZE.operation))
				return FINALIZE;
			else if (requestAttributeString.toLowerCase().contains(NOTIFY.operation))
				return NOTIFY;
			else if (requestAttributeString.toLowerCase().contains(PICK.operation))
				return PICK;
		}
	    return FIND;
	}
	
	/**
	 * Resolve the enumeration by checking to see if the id is present
	 * @param int id 
	 * @return PageMode
	 */
	public static PageMode determineMode(int id) {
		if (id == 0)
			return ADD;
		else if (id > 0)
			return EDIT;
		else 
			return ADD;
	}
	
	public static PageMode determineMode(String id) {
		return determineMode(TextUtil.isEmpty(id) ? 0 : Integer.parseInt(id));
	}
}
