package com.itsy.exception;

public class RowLockedException extends Exception {
	static final long serialVersionUID = 797466444;
	
	/**
	 * Default constructor.
	 */
	public RowLockedException(){
		super();
		fillInStackTrace();
	}
}
