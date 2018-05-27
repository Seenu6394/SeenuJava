package com.scs.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.scs.entity.model.EntityRegex;
import com.scs.entity.model.RegEx;
import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;
import com.scs.model.IntentRegex;
import com.scs.service.IntentDbServices;
import com.scs.service.impl.ReDbServicesImpl;
import com.scs.util.ApiConstants;
import com.scs.util.ErrorConstants;
import com.scs.util.Utility;

@RestController
@RequestMapping(ApiConstants.API)

public class ReController {

	private static final Logger logger = Logger.getLogger(ReController.class);
	private static final String CONTROLLER_END_EXCEPTION = "RE CONTROLLER  ENDED WITH EXCEPTION";

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private IntentDbServices intentDbService;

	@Autowired
	private ReDbServicesImpl reDbService;

	@SuppressWarnings("unchecked")
	@GetMapping(value = ApiConstants.GET_REGULAR_EXPRESSIONS, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getRegularExpressions(BaseRequestModel baseModel, HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			List<RegEx> regularExpressionLst;

			regularExpressionLst = (List<RegEx>) reDbService.getReDetails(baseModel);
			responseObject.put(ApiConstants.API_RESPONSE, regularExpressionLst);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = ApiConstants.GET_INTENT_REGULAR_EXPRESSIONS, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getIntentRegularExpressions(BaseRequestModel baseModel, HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			List<IntentRegex> regularExpressionLst;

			regularExpressionLst = (List<IntentRegex>) reDbService.getRegexwithIntents(baseModel);
			responseObject.put(ApiConstants.API_RESPONSE, regularExpressionLst);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@GetMapping(value = ApiConstants.REGULAR_EXPRESSION, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getReById(@RequestParam("id") String id, BaseRequestModel baseModel, HttpSession session)
			throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			RegEx regularExpression;
			regularExpression = (RegEx) reDbService.getReById(baseModel, id);

			responseObject.put(ApiConstants.API_RESPONSE, regularExpression);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@PostMapping(value = ApiConstants.REGULAR_EXPRESSION, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object createRe(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			reDbService.createRe(baseModel);

			responseObject.put(ApiConstants.API_RESPONSE, ApiConstants.SUCCESS);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@PostMapping(value = ApiConstants.REGEX_MAPPING, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object createRegexMapping(@RequestParam("entityId") Long entityId, @RequestParam("regexId") Long regexId)
			throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();
		EntityRegex entityRegex = new EntityRegex();
		try {

			entityRegex = (EntityRegex) reDbService.createRegexMapping(entityId, regexId);

			responseObject.put(ApiConstants.API_RESPONSE, entityRegex);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@DeleteMapping(value = ApiConstants.REGEX_MAPPING, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object deleteRegexMapping(@RequestParam("entityId") Long entityId, @RequestParam("regexId") Long regexId)
			throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			reDbService.deleteRegexMapping(entityId, regexId);
			responseObject.put(ApiConstants.API_RESPONSE, ApiConstants.SUCCESS);
		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@DeleteMapping(value = ApiConstants.REGULAR_EXPRESSION, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object deleteRegex(@RequestParam("id") String id) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			reDbService.deleteRegEx(id);
			responseObject.put(ApiConstants.API_RESPONSE, ApiConstants.SUCCESS);
		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@PutMapping(value = ApiConstants.REGULAR_EXPRESSION, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object update(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			String response = (String) reDbService.updateRe(baseModel);
			if (!response.equalsIgnoreCase(ApiConstants.SUCCESS)) {
				return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(null);

			}

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

}
