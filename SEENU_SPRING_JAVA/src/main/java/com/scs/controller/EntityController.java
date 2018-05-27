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

import com.scs.entity.model.EntityDetails;
import com.scs.entity.model.EntityTypeDetails;
import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;
import com.scs.service.EntityDbServices;
import com.scs.util.ApiConstants;
import com.scs.util.ErrorConstants;
import com.scs.util.Utility;

@RestController
@RequestMapping(ApiConstants.API)

public class EntityController {

	private static final Logger logger = Logger.getLogger(EntityController.class);
	private static final String CONTROLLER_END_EXCEPTION = "ENTITY CONTROLLER  ENDED WITH EXCEPTION";

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private EntityDbServices entityDbService;

	@SuppressWarnings("unchecked")
	@GetMapping(value = ApiConstants.GET_ENTITY_DETAILS, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getEntity(BaseRequestModel baseModel, HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		List<EntityDetails> entityLst;
		entityLst = (List<EntityDetails>) entityDbService.getEntityDetails(baseModel);
		responseObject.put(ApiConstants.API_RESPONSE, entityLst);

		return responseObject;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = ApiConstants.GET_ENTITY_TYPES, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getEntityTypes(BaseRequestModel baseModel, HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		List<EntityTypeDetails> entityTypeLst;
		entityTypeLst = (List<EntityTypeDetails>) entityDbService.getEntityTypeDetails(baseModel);
		responseObject.put(ApiConstants.API_RESPONSE, entityTypeLst);

		return responseObject;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = ApiConstants.GET_ENTITY_BY_KU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getEntityByKu(@RequestParam("id") String id, BaseRequestModel baseModel, HttpSession session)
			throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			List<EntityDetails> entities;
			if (("All KU").equals(id)) {
				entities = (List<EntityDetails>) entityDbService.getEntityDetails(baseModel);
			} else {
				entities = (List<EntityDetails>) entityDbService.getEntityByKu(baseModel, id);
			}
			responseObject.put(ApiConstants.API_RESPONSE, entities);
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@GetMapping(value = ApiConstants.ENTITY, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getEntityById(@RequestParam("id") String id, BaseRequestModel baseModel, HttpSession session)
			throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			EntityDetails entity;
			entity = (EntityDetails) entityDbService.getEntityById(baseModel, id);

			responseObject.put(ApiConstants.API_RESPONSE, entity);
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@PostMapping(value = ApiConstants.ENTITY, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object createEntity(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		EntityDetails entity = null;

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			entity = (EntityDetails) entityDbService.createEntity(baseModel);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return entity;
	}

	@PostMapping(value = ApiConstants.ENTITY_QUESTION, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object createEntityQuestion(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			 entityDbService.createEntityQuestion(baseModel);
		

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

	@DeleteMapping(value = ApiConstants.ENTITY, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object delete(@RequestParam("id") String id, BaseRequestModel baseModel, HttpSession session)
			throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			entityDbService.deleteEntity(baseModel, id);
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

	@DeleteMapping(value = ApiConstants.ENTITY_QUESTION, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object deleteEntityQuestion(@RequestParam("id") String id, BaseRequestModel baseModel, HttpSession session)
			throws ApiException {

		try {

			entityDbService.deleteEntityQuestion(baseModel, id);
		
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}

	@PutMapping(value = ApiConstants.ENTITY, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object update(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();
		
	

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			entityDbService.updateEntity(baseModel);

			responseObject.put(ApiConstants.API_RESPONSE, ApiConstants.SUCCESS);
			
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

}
