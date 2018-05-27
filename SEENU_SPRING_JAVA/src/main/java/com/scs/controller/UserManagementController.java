package com.scs.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.scs.entity.model.Languages;
import com.scs.entity.model.Locale;
import com.scs.entity.model.UserInfo;
import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;
import com.scs.service.UserDbServices;
import com.scs.util.ApiConstants;
import com.scs.util.ErrorConstants;
import com.scs.util.SessionUtil;
import com.scs.util.StorageConstants;
import com.scs.util.Utility;
import com.scs.validation.ValidationGroups.UserRequest;

@RestController
@RequestMapping(ApiConstants.API)

public class UserManagementController {

	private static final Logger logger = Logger.getLogger(UserManagementController.class);
	private static final String  RESPONSE = "response";
	
	@Autowired
	private UserDbServices userDbServices;

	@Autowired
	private MessageSource messageSource;

	
	
	@PostMapping(value = ApiConstants.USER, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object create(@RequestBody @Validated(UserRequest.class) BaseRequestModel baseModel,
			BindingResult bindingResult, HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		userDbServices.createUser(baseModel);
		responseObject.put(ApiConstants.API_RESPONSE, ApiConstants.SUCCESS);

		return responseObject;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = ApiConstants.GET_USERS, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getUsers(BaseRequestModel baseModel) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		List<UserInfo> users;
		users = (List<UserInfo>) userDbServices.getUsersDetails(baseModel);

		responseObject.put(ApiConstants.API_RESPONSE, users);

		return responseObject;

	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = ApiConstants.GET_LOCALE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getLocaleLanguages(BaseRequestModel baseModel) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		List<Locale> languages;
		languages = (List<Locale>) userDbServices.getLanguages(baseModel);

		responseObject.put(ApiConstants.API_RESPONSE, languages);

		return responseObject;

	}
	
	
	@GetMapping(value = ApiConstants.GET_LANGUAGES, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getLanguages(BaseRequestModel baseModel) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		Languages languages;
		languages = (Languages) userDbServices.getLanguages(baseModel);

		responseObject.put(ApiConstants.API_RESPONSE, languages);

		return responseObject;

	}

	@DeleteMapping(value = ApiConstants.USER, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object delete(@RequestParam("id") String id, BaseRequestModel baseModel) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		userDbServices.deleteUser(baseModel, id);
		responseObject.put(ApiConstants.API_RESPONSE, ApiConstants.SUCCESS);

		return responseObject;
	}

	@GetMapping(value = ApiConstants.USER, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getUserById(@RequestParam("id") String id, BaseRequestModel baseModel) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		UserInfo user;
		user = (UserInfo) userDbServices.getUserById(baseModel, id);

		responseObject.put(ApiConstants.API_RESPONSE, user);

		return responseObject;
	}

	@PutMapping(value = ApiConstants.USER, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object update(@RequestBody @Validated(UserRequest.class) BaseRequestModel baseModel,
			BindingResult bindingResult, HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		if (bindingResult.hasErrors()) {
			logger.debug(ApiConstants.BINDING_ERRORS);
			throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
		}

		userDbServices.updateUser(baseModel);
		responseObject.put(RESPONSE, ApiConstants.SUCCESS);

		return responseObject;
	}
	
	
	
	@PutMapping(value = ApiConstants.LANGUAGE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object updateLanguage(@RequestBody @Validated(UserRequest.class) BaseRequestModel baseModel,
			BindingResult bindingResult, HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		userDbServices.updateLanguage(baseModel);
		responseObject.put(RESPONSE, ApiConstants.SUCCESS);

		return responseObject;
	}
	
	
	
	
	@PostMapping(value = ApiConstants.FORGOT_PASS, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object forgotPassword(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			UserInfo user = (UserInfo) userDbServices.findUserByEmail(baseModel.getUser().getEmail(), session);

			if (user.getUserToken() != null) {
				userDbServices.deleteUserToken(user);
			}
			userDbServices.createUserToken(user, session);
			responseObject.put(ApiConstants.API_RESPONSE, ApiConstants.SUCCESS);

		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));

			throw new ApiException("INVALID_EMAIL", "This email doesn't exist");
		}
		return responseObject;
	}

	@GetMapping(value = ApiConstants.RESET_PASS, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object resetPasswordGet(@RequestParam("token") String token, BaseRequestModel baseModel, HttpSession session)
			throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();
		userDbServices.findUserByResetToken(token, session);
		responseObject.put(ApiConstants.API_RESPONSE, ApiConstants.SUCCESS);

		return responseObject;
	}

	@PostMapping(value = ApiConstants.RESET_PASS, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object resetPasswordPost(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			final String propPath = System.getProperty("scsprops.pDir");
			logger.debug(propPath);

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}
			if (!SessionUtil.objectExists(session, StorageConstants.USER)) {
				throw new ApiException(ErrorConstants.INVALIDDATA, "INVALID_REQUEST");

			}

			userDbServices.resetPassword(baseModel.getUser().getPassword(), session);
			responseObject.put(ApiConstants.API_RESPONSE, ApiConstants.SUCCESS);

		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}

		SessionUtil.setValue(session, StorageConstants.USER, null);
		return responseObject;
	}

	@PostMapping(value = ApiConstants.CHANGE_PASS, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object changePasswordPost(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			if (!SessionUtil.objectExists(session, StorageConstants.USER)) {
				throw new ApiException(ErrorConstants.INVALIDDATA, "INVALID_REQUEST");

			}

			userDbServices.changePassword(baseModel, session);
			responseObject.put(ApiConstants.API_RESPONSE, ApiConstants.SUCCESS);
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}

		return responseObject;
	}

	@GetMapping(value = ApiConstants.LOGOUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object logout(HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();
		try {

			session.invalidate();
			responseObject.put(RESPONSE, ApiConstants.SUCCESS);
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@GetMapping(value = ApiConstants.LOGIN_SUCCESS, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object login(HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			responseObject.put("userName", authentication.getName());
			for (GrantedAuthority authority : authentication.getAuthorities()) {
				responseObject.put("userRole", authority.getAuthority());

			}

			UserInfo user = userDbServices.getUserByName(authentication.getName());
			Boolean flag = userDbServices.updateSuccess(authentication.getName());
			if (flag) {
				responseObject.put("firstLogin", true);
			} else {
				responseObject.put("firstLogin", false);
			}
			SessionUtil.setValue(session, StorageConstants.USER, user);

		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));

			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@PostMapping(value = ApiConstants.LOGIN_FAILURE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object loginFailure(HttpServletRequest request, HttpSession session) throws ApiException {

		Object status = null;

		String username = request.getParameter("username");

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			status = userDbServices.loginFailure(username);

			if (status == ApiConstants.LOGIN_FAILURE_STATUS) {

				responseObject.put("INVALID_PASSWORD", "Username or password is invalid.");

			} else if (status == ApiConstants.ATTEMPT_FAILURE) {

				responseObject.put("ACCOUNT_LOCKED", "Your account is locked, Try Forgot Password");

			} else {

				responseObject.put("INVALID_PASSWORD", "Username or password is invalid.");
			}

		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@PostMapping(value = ApiConstants.TEST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object loginfailtest(@RequestParam("name") String name, BaseRequestModel baseModel,
			BindingResult bindingResult, HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		if (bindingResult.hasErrors()) {
			logger.debug(ApiConstants.BINDING_ERRORS);
			throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
		}

		responseObject.put(RESPONSE, ApiConstants.SUCCESS);

		return responseObject;
	}

}
