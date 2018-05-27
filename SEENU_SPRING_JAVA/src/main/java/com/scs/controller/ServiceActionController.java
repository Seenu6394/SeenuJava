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

import com.scs.entity.model.Action;

import com.scs.exception.ApiException;
import com.scs.model.ActionModel;
import com.scs.model.BaseRequestModel;

import com.scs.service.SADbServices;
import com.scs.util.ApiConstants;
import com.scs.util.ErrorConstants;
import com.scs.util.Utility;

@RestController
@RequestMapping(ApiConstants.API)

public class ServiceActionController {

	private static final Logger logger = Logger.getLogger(ServiceActionController.class);
	private static final String CONTROLLER_END_EXCEPTION = "ServiceAction CONTROLLER  ENDED WITH EXCEPTION";

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private SADbServices saDbService;


	@PostMapping(value = ApiConstants.SERVICE_ACTION, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object createServiceAction(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();
		Action action = new Action();
		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}
			if (baseModel.getAction().getName() != null) {
			action = (Action) saDbService.createServiceAction(baseModel);
			}
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
		return action;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = ApiConstants.GET_SERVICE_ACTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getServiceActions(BaseRequestModel baseModel, HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			List<Action> actionLst;

			actionLst = (List<Action>) saDbService.getServiceActionDetails(baseModel);

			responseObject.put(ApiConstants.API_RESPONSE, actionLst);

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

	@DeleteMapping(value = ApiConstants.SERVICE_ACTION, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object delete(@RequestParam("id") String id, BaseRequestModel baseModel, HttpSession session)
			throws ApiException {

		try {

			saDbService.deleteServiceAction(baseModel, id);
		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}

	@DeleteMapping(value = ApiConstants.ACTION_ERROR_RESPONSE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object deleteErrorResponse(@RequestParam("id") String id, BaseRequestModel baseModel, HttpSession session)
			throws ApiException {

		try {

			saDbService.deleteErrorResponse(baseModel, id);
		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}

	@GetMapping(value = ApiConstants.SERVICE_ACTION, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getSAById(@RequestParam("id") String id, BaseRequestModel baseModel, HttpSession session)
			throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			Action action;
			action = (Action) saDbService.getServiceActionById(baseModel, id);

			responseObject.put(ApiConstants.API_RESPONSE, action);

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

	@PutMapping(value = ApiConstants.SERVICE_ACTION, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object update(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		ActionModel action = new ActionModel();

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			action = (ActionModel) saDbService.updateServiceAction(baseModel);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return action;
	}

	@PutMapping(value = ApiConstants.SERVICE_ACTION_INTENT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object updateIntentBySA(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		Action action = new Action();
		
		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			action = (Action) saDbService.updateSAIntent(baseModel);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return action;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = ApiConstants.GET_SA_BY_KU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getSAByKu(@RequestParam("id") String id, BaseRequestModel baseModel, HttpSession session)
			throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			List<Action> actions;
			if (("All KU").equals(id)) {
				actions = (List<Action>) saDbService.getServiceActionDetails(baseModel);

			} else {
				actions = (List<Action>) saDbService.getServiceActionByKu(baseModel, id);
			}

			responseObject.put(ApiConstants.API_RESPONSE, actions);

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
