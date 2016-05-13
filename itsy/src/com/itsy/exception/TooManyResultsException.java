package com.itsy.exception;

public class TooManyResultsException extends Exception {
	static final long serialVersionUID = 554433256;
	
	/**
	 * Default constructor.
	 */
	public TooManyResultsException() {
		super();
		fillInStackTrace();
	}
}

