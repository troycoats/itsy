package com.itsy.enumeration;

public enum AccessLevel {
	READ	(1),
	UPDATE	(2),
	INSERT	(3),
	DELETE  (4);
	
	AccessLevel(int accessLevelId) {
		this.accessLevelId = accessLevelId;
	}
	
	public int accessLevelId;
}
