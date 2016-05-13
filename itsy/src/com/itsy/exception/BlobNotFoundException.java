package com.itsy.exception;

public class BlobNotFoundException extends Exception {
	static final long serialVersionUID = 1312919831;
	
	/**
	 * Default constructor.
	 */
	public BlobNotFoundException(){
		super();
		fillInStackTrace();
	}
}
