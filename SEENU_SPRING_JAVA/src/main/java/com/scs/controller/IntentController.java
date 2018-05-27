package com.scs.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scs.entity.model.Intent;
import com.scs.entity.model.Ku;
import com.scs.entity.model.Message;
import com.scs.entity.model.ProjectKeyword;
import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;
import com.scs.service.IntentDbServices;
import com.scs.service.impl.ImportDbServicesImpl;
import com.scs.util.ApiConstants;
import com.scs.util.ErrorConstants;
import com.scs.util.Utility;

@RestController
@RequestMapping(ApiConstants.API)

public class IntentController {

	private static final Logger logger = Logger.getLogger(IntentController.class);
	private static final String CONTROLLER_END_EXCEPTION = "INTENT CONTROLLER  ENDED WITH EXCEPTION";

	private static final String  RESPONSE = "response";
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private IntentDbServices intentDbService;
	
	@Autowired
	private ImportDbServicesImpl importDbService;
	
	

	

	@PostMapping(value = ApiConstants.INTENT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object createIntent(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		Object intent = null;

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			intent = intentDbService.createintent(baseModel);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return intent;
	}

	@PostMapping(value = "/importNewKu", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object importKu(@RequestParam("file") MultipartFile multiparFile, HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			ObjectMapper mapper = new ObjectMapper();

			Ku ku = mapper.readValue(multiparFile.getInputStream(), Ku.class);
			importDbService.importNewKu(ku);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject.put(RESPONSE, ApiConstants.SUCCESS);
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = ApiConstants.GET_INTENT_DETAILS, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getIntent(BaseRequestModel baseModel) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			List<Intent> intentLst;
			intentLst = (List<Intent>) intentDbService.getintentDetails(baseModel);

			responseObject.put(ApiConstants.API_RESPONSE, intentLst);

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
	@GetMapping(value = ApiConstants.GET_PROJECT_KEYWORD, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getProjectKeywords(BaseRequestModel baseModel) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			List<ProjectKeyword> projectKeywordLst;
			projectKeywordLst = (List<ProjectKeyword>) intentDbService.getProjectKeywords(baseModel);

			responseObject.put(ApiConstants.API_RESPONSE, projectKeywordLst);

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

	
	@ResponseBody
	@PostMapping(value = ApiConstants.KEYWORD, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object createKeyword(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			intentDbService.createKeyword(baseModel);
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

	@ResponseBody
	@PostMapping(value = ApiConstants.PROJECT_KEYWORD, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object createFillerKeyword(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			intentDbService.createProjectKeyword(baseModel);
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


	@DeleteMapping(value = ApiConstants.INTENT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object delete(@RequestParam("id") String id, BaseRequestModel baseModel, HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			intentDbService.deleteintent(baseModel, id);
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

	@DeleteMapping(value = ApiConstants.KEYWORD, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object deleteKeyword(@RequestParam("id") String id, BaseRequestModel baseModel) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			intentDbService.deleteKeyword(baseModel, id);
		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject.put(RESPONSE, ApiConstants.SUCCESS);
	}

	@PostMapping(value = ApiConstants.DELETE_PROJECT_KEYWORD, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object deleteProjectKeyword(@RequestBody @Valid BaseRequestModel baseModel) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {
			intentDbService.deleteProjectKeyword(baseModel);
		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject.put(RESPONSE, ApiConstants.SUCCESS);
	}

	
	@GetMapping(value = ApiConstants.INTENT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getIntentById(@RequestParam("id") String id, BaseRequestModel baseModel) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			Intent intent;
			intent = (Intent) intentDbService.getIntentById(baseModel, id);

			responseObject.put(ApiConstants.API_RESPONSE, intent);

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
	
	
	@PutMapping(value = ApiConstants.INTENT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object update(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		Object intent = null;

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			intentDbService.updateIntent(baseModel);
			intent = intentDbService.getIntentById(baseModel, baseModel.getIntents().getId().toString());

			responseObject.put(RESPONSE , intent);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);

		}
		return intent;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = ApiConstants.GET_INTENT_BY_KU, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getIntentByKu(@RequestParam("id") String id, BaseRequestModel baseModel) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			List<Intent> intents;
			if (("All KU").equals(id)) {
				intents = (List<Intent>) intentDbService.getintentDetails(baseModel);

			} else {
				intents = (List<Intent>) intentDbService.getIntentByKu(baseModel, id);
			}

			responseObject.put(ApiConstants.API_RESPONSE, intents);

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

	@PostMapping(value = ApiConstants.INTENT_RESPONSE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object createIntentResponse(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();
		Message message = null;

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			message = (Message) intentDbService.createintentResponses(baseModel);
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
		return message;
	}

	@PutMapping(value = ApiConstants.UPDATE_RESPONSE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object updateIntentResponse(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();
		Message message = new Message();

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			message = (Message) intentDbService.updateIntentResponse(baseModel);

			responseObject.put(RESPONSE , message);

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

	@DeleteMapping(value = ApiConstants.INTENT_RESPONSE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object deleteIntentResponsebyIntentID(@RequestParam("id") String id, BaseRequestModel baseModel)
			throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			intentDbService.deleteIntentResponse(baseModel, id);
		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject.put(RESPONSE , "success");
	}

	@DeleteMapping(value = ApiConstants.DELETE_RESPONSE_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object deleteIntentResponsesbyIntentID(@RequestParam("id") String id, BaseRequestModel baseModel)
			throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			intentDbService.deleteIntentResponseByResponseID(baseModel, id);
		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error(CONTROLLER_END_EXCEPTION);
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject.put(RESPONSE , "success");
	}

}
