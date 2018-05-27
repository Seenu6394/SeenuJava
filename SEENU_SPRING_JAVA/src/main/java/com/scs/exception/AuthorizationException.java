package com.scs.exception;

public class AuthorizationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String description;

	public AuthorizationException(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}



}
