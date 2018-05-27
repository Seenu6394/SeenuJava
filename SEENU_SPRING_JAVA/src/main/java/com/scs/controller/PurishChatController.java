package com.scs.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.scs.entity.model.EntityDetails;
import com.scs.entity.model.UserInfo;
import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;
import com.scs.model.PuristUserLogin;
import com.scs.model.PuristUserLoginRes;
import com.scs.model.SampleJson;
import com.scs.service.impl.PuristChatService;
import com.scs.service.impl.RandomPasswordGenerator;
import com.scs.util.ApiConstants;
import com.scs.util.ErrorConstants;
import com.scs.util.Utility;

@RestController
@RequestMapping(ApiConstants.API)

public class PurishChatController {

	private static final Logger logger = Logger.getLogger(PurishChatController.class);
	
	@Autowired
	private MessageSource messageSource;
	
	
	@PostMapping(value = ApiConstants.S4M_BOT_LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object s4mUserLogin(@Valid @RequestBody PuristUserLogin puristLoginObj, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		try {
			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}
			return new PuristChatService().getUserLoginObject(puristLoginObj);

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
	}

	public void sendToPurist(UserInfo user) {
		final String uri = "http://api.puristchat.com/admin/v1/users";
		try {

			UserInfo userInfo = new UserInfo();
			userInfo.setUserName(user.getUserName());
			userInfo.setPassword(user.getPassword());
			userInfo.setFirstName(user.getUserName());

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.postForObject(uri, userInfo, UserInfo.class);

		} catch (HttpClientErrorException ex) {
			logger.error(Utility.getExceptionMessage(ex));
		}
	}

	@PostMapping(value = ApiConstants.PURIST_LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object loginPurist(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		Object user = null;

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}
			user = generateUsernamePassword();

		} catch (ApiException ex) {
			logger.error(ex.getErrorCode() + " : " + ex.getErrorCode(), ex);

			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));

		}
		return user;
	}

	public Object generateUsernamePassword() {

		UserInfo user = new UserInfo();
		int noOfCAPSAlpha = 1;
		int noOfDigits = 1;
		int noOfSplChars = 1;
		int minLen = 8;
		int maxLen = 16;

		Random r = new Random();

		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		String generatedUserName = "";

		for (int i = 0; i < 6; i++) {
			Character ch = alphabet.charAt(r.nextInt(alphabet.length()));
			generatedUserName = generatedUserName.concat(ch.toString());

		}

		String pwd = RandomPasswordGenerator.generatePswd(minLen, maxLen, noOfCAPSAlpha, noOfDigits, noOfSplChars);
		generatedUserName = generatedUserName+ "@botchestra.com";
		user.setUserName(generatedUserName);
		user.setPassword(passwordEncoder().encode(pwd));

		return user;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
