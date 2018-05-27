package com.scs.exception;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	private final String errMessage;

	
	public ServiceException(String errMessage) {
		this.errMessage = errMessage;
	}

	public String getErrMessage() {
		return errMessage;
	}



	
}
