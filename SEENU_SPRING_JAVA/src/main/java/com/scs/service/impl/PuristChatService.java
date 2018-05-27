package com.scs.service.impl;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scs.controller.PurishChatController;
import com.scs.exception.ApiException;
import com.scs.model.PuristAdminTokenReq;
import com.scs.model.PuristUserLogin;
import com.scs.model.PuristUserLoginRes;
import com.scs.model.RegisterPuristUser;
import com.scs.util.ApiConstants;
import com.scs.util.ErrorConstants;
import com.scs.util.Utility;

public class PuristChatService {

	private static final Logger logger = Logger.getLogger(PuristChatService.class);

	String loginUrl = ApiConstants.PURIST_USER_LOGINURL;
	String adminKey = ApiConstants.PURIST_ADMIN_KEY;
	String password = ApiConstants.PURIST_PASSWORD;
	
	ObjectMapper mapper = new ObjectMapper();
	
	public Object getUserLoginObject(PuristUserLogin puristLoginObj) throws ApiException {
		
		PuristUserLoginRes puristUserLoginResp = null;
		

		try {

			String plainCreds = adminKey + ":" + password;
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Basic " + Utility.getBase64Credits(plainCreds));
			headers.setContentType(MediaType.APPLICATION_JSON);
			String loginReqBody = Utility.getJsonForRequest(puristLoginObj);
			HttpEntity<String> loginHttpEntity = new HttpEntity<String>(loginReqBody, headers);
			ResponseEntity<String> loginResponse = Utility.sendHttpRequest(loginHttpEntity, HttpMethod.POST, loginUrl);

			logger.info("----------------------got user login response------------------------------------------");
			puristUserLoginResp = mapper.readValue(loginResponse.getBody(), PuristUserLoginRes.class);

			return puristUserLoginResp;
		} catch (HttpServerErrorException hsee) {
			
			puristUserLoginResp = (PuristUserLoginRes)puristAdminLogin(puristLoginObj, hsee);
			
			return puristUserLoginResp;
		} catch (ApiException ae) {
			ae.printStackTrace();
			throw ae;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ApiException(ErrorConstants.USER_LOGIN_EXCEPTION, "");
		}
	}
	
	
	public Object puristAdminLogin(PuristUserLogin puristLoginObj, HttpServerErrorException hsee) throws ApiException{

		PuristUserLoginRes puristUserLoginResp = null;

		try {
			
			
			if (hsee.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR) ) {
				// do admin login -> user registration ->userlogin

				logger.info("----------------------user not register------------------------------------------");
				String admintokenUrl = ApiConstants.PURIST_ADMIN_TOKEN_URL;

				PuristAdminTokenReq admintokenReq = new PuristAdminTokenReq();
				admintokenReq.setClient_id("na");
				admintokenReq.setGrant_type("password");
				admintokenReq.setUsername(ApiConstants.PURIST_ADMIN_USERNAME);
				admintokenReq.setPassword(ApiConstants.PURIST_ADMIN_PASSWORD);
				
				HttpHeaders adminLoginheaders = new HttpHeaders();
				adminLoginheaders.setContentType(MediaType.APPLICATION_JSON);
				
				String adminReqBody = Utility.getJsonForRequest(admintokenReq);
				logger.info("adminReqBody: "+adminReqBody);
				HttpEntity<String> adminHttpEntity = new HttpEntity<String>(adminReqBody, adminLoginheaders);
				ResponseEntity<String> adminLoginResponse = Utility.sendHttpRequest(adminHttpEntity,
						HttpMethod.POST, admintokenUrl);

				if (adminLoginResponse.getStatusCode().equals(HttpStatus.CREATED)) {
					logger.info(
							"----------------------admin logged in successfully------------------------------------");

					PuristUserLoginRes adminloginResponse = mapper.readValue(adminLoginResponse.getBody(),
							PuristUserLoginRes.class);// (PuristUserLoginRes) adminLoginResponse.getBody();
					String accessToken = adminloginResponse.getAccess_token();
					logger.info("accessToken: " + accessToken);

					RegisterPuristUser registerPuristUser = new RegisterPuristUser();
					registerPuristUser.setEmail(puristLoginObj.getP_username());
					registerPuristUser.setName(puristLoginObj.getP_username());
					registerPuristUser.setPassword(password);
					registerPuristUser.setUsername(puristLoginObj.getP_username().substring(0, puristLoginObj.getP_username().indexOf("@")));

					HttpHeaders createUserHeaders = new HttpHeaders();
					createUserHeaders.add("Authorization", "Bearer " + accessToken);
					createUserHeaders.setContentType(MediaType.APPLICATION_JSON);
					
					String createUserReqBody = Utility.getJsonForRequest(registerPuristUser);
					logger.info("createUserReqBody: "+createUserReqBody);
					HttpEntity<String> createUserHttpEntity = new HttpEntity<String>(createUserReqBody,
							createUserHeaders);
					ResponseEntity<String> createuserResponse = Utility.sendHttpRequest(createUserHttpEntity,
							HttpMethod.POST, ApiConstants.PURIST_CREATE_USER_URL);

					if (createuserResponse.getStatusCode().equals(HttpStatus.CREATED)) {
						logger.info(
								"----------------------User has been Registered------------------------------------------");
						
						String plainCreds = adminKey + ":" + password;
						HttpHeaders headers = new HttpHeaders();
						headers.add("Authorization", "Basic " + Utility.getBase64Credits(plainCreds));
						headers.setContentType(MediaType.APPLICATION_JSON);
						String loginReqBody = Utility.getJsonForRequest(puristLoginObj);
						HttpEntity<String> loginHttpEntity = new HttpEntity<String>(loginReqBody, headers);
						
						ResponseEntity<String> newUserLoginResponse = Utility.sendHttpRequest(loginHttpEntity,
								HttpMethod.POST, loginUrl);

						if (newUserLoginResponse.getStatusCode().equals(HttpStatus.OK)) {
							logger.info(
									"----------------------user logged in successfully------------------------------------------");
							puristUserLoginResp = mapper.readValue(newUserLoginResponse.getBody(),
									PuristUserLoginRes.class);// (PuristUserLoginRes) loginResponse.getBody();
						} else {
							throw new ApiException(ErrorConstants.USER_LOGIN_EXCEPTION, "");
						}

					} else {
						logger.info(
								"----------------------User Registration failed------------------------------------------");
						throw new ApiException(ErrorConstants.PLEASE_PROVIDE_EMAIL, "");
					}

				} else {
					logger.info(
							"----------------------admin login failed for user creation------------------------------------------");
					throw new ApiException(ErrorConstants.USER_LOGIN_EXCEPTION, "");
				}

			} else {
				// 404
				logger.info(
						"----------------------unable to create or login user------------------------------------------");
				throw new ApiException(ErrorConstants.USER_LOGIN_EXCEPTION, "");

			}
			return puristUserLoginResp;
		} catch (StringIndexOutOfBoundsException sioobe) {
			
			throw new ApiException(ErrorConstants.PLEASE_PROVIDE_EMAIL, "");
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			throw new ApiException(ErrorConstants.USER_LOGIN_EXCEPTION, "");
		}

	}

}
