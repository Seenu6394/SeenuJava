package com.scs.exception;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.scs.util.ErrorConstants;
import com.scs.util.Utility;

@Component
@ControllerAdvice(annotations = RestController.class)
class ApiExceptionHandler {

	private static final Logger logger = Logger.getLogger(ApiExceptionHandler.class);
	
	@Autowired
	protected MessageSource messageSource;

	@ExceptionHandler(ApiException.class)
	public @ResponseBody @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY) ErrorMessage handleCustomException(
			ApiException ex) {
		logger.debug("CONTROLLER CUSTOM EXCEPTION START");
		ErrorMessage errMessage = new ErrorMessage(ex.getErrorCode(), ex.getErrorMessage());
		logger.error(ex.getErrorCode() + " : " + ex.getErrorMessage());
		logger.debug("CONTROLLER CUSTOM EXCEPTION END");
		return errMessage;
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public @ResponseBody @ResponseStatus(value = HttpStatus.BAD_REQUEST) ErrorMessage handleHttpMessageException(
			HttpMessageNotReadableException hmnre) {
		logger.debug("CONTROLLER HTTP MSG READ EXCEPTION START");
		ErrorMessage errMessage = new ErrorMessage(ErrorConstants.INVALIDREQUEST, Utility.getMessageByLocale(ErrorConstants.INVALIDREQUEST, messageSource));
		logger.error(hmnre.getMessage());
		logger.debug("CONTROLLER HTTP MSG READ EXCEPTION END");
		return errMessage;
	}

	@ExceptionHandler(Exception.class)
	public @ResponseBody ErrorMessage handleException(Exception ex) {
		logger.debug("CONTROLLER EXCEPTION START");
		logger.error(Utility.getExceptionMessage(ex));
		ErrorMessage errMessage = new ErrorMessage("EXCEPTION", ex.getMessage());
		logger.debug("CONTROLLER EXCEPTION END");
		return errMessage;

	}
	
	@ExceptionHandler(AuthorizationException.class)
	public @ResponseBody @ResponseStatus(value = HttpStatus.UNAUTHORIZED) ErrorMessage handleAuthorizationException(
			AuthorizationException ex) {
		logger.debug(" CONTROLLER CUSTOM EXCEPTION START");
		ErrorMessage errMessage = new ErrorMessage("UNAUTHORIZED",ex.getDescription());
		logger.error(ex.getDescription());
		logger.debug(" CONTROLLER CUSTOM EXCEPTION END");
		return errMessage;
	}
	
	
}