package com.scs.exception;

import org.springframework.context.MessageSource;

import com.scs.util.Utility;

public class ApiException extends Exception {

	private static final long serialVersionUID = 1L;
	private final String errorCode;
	private final String errorMessage;

	public ApiException(String errCode, MessageSource messageSource) {
		this.errorCode = errCode;
		this.errorMessage = Utility.getMessageByLocale(errCode, messageSource);
	}

	public ApiException(String errCode, String errMessage) {
		this.errorCode = errCode;
		this.errorMessage = errMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
