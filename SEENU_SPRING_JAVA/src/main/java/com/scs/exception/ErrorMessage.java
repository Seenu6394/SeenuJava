package com.scs.exception;

public class ErrorMessage {

	private String errorDescription;
	private String errorCode;

	public ErrorMessage(String code, String message) {
		this.errorCode = code;
		this.errorDescription = message;

	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
