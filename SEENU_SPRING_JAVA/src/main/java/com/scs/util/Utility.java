
package com.scs.util;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scs.exception.ApiException;
import com.scs.model.PuristUserLoginRes;

public class Utility {

	private static final Logger logger = Logger.getLogger(Utility.class);
	
	@Autowired
	private static MessageSource messageSource;

	
	
	private Utility() {

	}
	
	public static String getBase64Credits(String unamePwd) {
		String base64Str = "";
		byte[] plainCredsBytes = unamePwd.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		base64Str = new String(base64CredsBytes);
		return base64Str;
		
	}
	
	
	public static ResponseEntity<String> sendHttpRequest(HttpEntity<String> httpEntity, HttpMethod httpMethod,
			String url) throws ApiException {

		try {
			RestTemplate restTemplate = new RestTemplate();

			logger.info("url: " + url);
			logger.info("httpMethod: " + httpMethod);
			ResponseEntity<String> response = restTemplate.exchange(url, httpMethod, httpEntity, String.class);
			logger.debug(response);
			return response;

		} catch (HttpClientErrorException hcee) {
			throw hcee;
		} catch (HttpServerErrorException hsee) {
			throw hsee;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ApiException(ErrorConstants.SERVICEFAILURE, "");
		}

	}

	public static String getJsonForRequest(Object obj) throws ApiException {
		String jsonInString = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			jsonInString = mapper.writeValueAsString(obj);
			return jsonInString;
		} catch (JsonProcessingException jpe) {
			throw new ApiException(ErrorConstants.SERVICEFAILURE, "");
		}

	}

	
	private static Pattern longTextPattern = Pattern.compile("^[a-zA-Z0-9\\p{Punct} ]{0,512}$");

	// Method to return Exception message from stack trace
	public static String getExceptionMessage(Exception ex) {
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		return sw.toString();

	}

	// Get The field information for parameters validation from json request
	public static String getErrorInformation(BindingResult bindingResult) {

		logger.debug("++----Utility -------getErrorInformation()---++");
		StringBuilder sb = new StringBuilder();

		for (Object object : bindingResult.getAllErrors()) {
			if (object instanceof FieldError) {
				FieldError fieldError = (FieldError) object;
				sb.append(fieldError.getField() + "-" + fieldError.getDefaultMessage() + "--");
				logger.debug("FIELD ERROR_" + fieldError.getField());
			}

			if (object instanceof ObjectError) {
				ObjectError objectError = (ObjectError) object;
				sb.append(objectError.getObjectName());
				logger.debug("OBJECT_ERROR_" + objectError.getObjectName());
			}

		}

		return sb.toString();
	}

	public static String getFirstErrorInformation(BindingResult bindingResult) {
		String fieldName = null;
		logger.debug("++----Utility Methods--getErrorInformation()---++");
		StringBuilder sb = new StringBuilder();
		for (Object object : bindingResult.getAllErrors()) {
			if (object instanceof FieldError) {
				FieldError fieldError = (FieldError) object;
				sb.append(fieldError.getDefaultMessage().trim());
				fieldName = fieldError.getField();
				logger.debug("FIELD ERROR_" + fieldError.getField());
				break;
			}

			if (object instanceof ObjectError) {
				ObjectError objectError = (ObjectError) object;
				sb.append(objectError.getObjectName());
				logger.debug("OBJECT_ERROR_" + objectError.getObjectName());
				break;
			}

		}

		return fieldName + " " + sb.toString();
	}

	// Logging for complete object details

	public static String getLogDump(Object o, String logMessage) {
		StringBuilder logDump = new StringBuilder();
		logDump.append("<-----------------------------------------" + logMessage
				+ "------------------------------------------>");
		logDump.append(System.getProperty(ApiConstants.LINE_SEPARATOR));
		if (o != null) {
			logDump.append(o.toString());
		}
		logDump.append(System.getProperty(ApiConstants.LINE_SEPARATOR));
		return logDump.toString();
	}

	public static String getLogDump(String logMessage) {
		StringBuilder logDump = new StringBuilder();
		logDump.append(
				"<------------------------------------------------------------------------------------------------------------------>");
		logDump.append(System.getProperty(ApiConstants.LINE_SEPARATOR));
		logDump.append(logMessage);
		logDump.append(System.getProperty(ApiConstants.LINE_SEPARATOR));
		return logDump.toString();
	}

	/**
	 * Method to be used if a constant product hash is required throughout a single
	 * session.
	 * 
	 * @param productID
	 * @return
	 */
	public static String generateHashKey(String productID, String sessionId) {

		String uniqueSessionString = sessionId;
		MessageDigest messageDigest;
		try {
			String stringToHash = productID + uniqueSessionString;
			messageDigest = MessageDigest.getInstance("SHA-256");
			byte[] bytes = messageDigest.digest(stringToHash.getBytes());
			return Base64.encodeBase64String(bytes);
		} catch (Exception e) {
			logger.debug(getExceptionMessage(e));

		}
		return "";
	}

	public static boolean checkNullEmpty(String value) {

		if (value != null && !value.isEmpty()) {
			return true;
		}
		return false;

	}

	public static String getMaskedNumber(String number) {

		int ccLength = 16;
		int acLength = 13;
		int ciflength = 8;
		String result = "";
		if (checkNullEmpty(number)) {
			logger.debug("Length of Card " + number.length());
			if (ccLength == number.length()) {
				logger.debug(" CARD MASKING " + number.length());
				result = getX(11) + number.substring(12);
			} else if (acLength == number.length()) {
				logger.debug("ACCOUNT NUMBER MASKING " + number.length());
				result = number.substring(0, 3) + getX(3) + number.substring(5, 7) + getX(3) + number.substring(11, 13);
			} else if (ciflength == number.length()) {
				logger.debug("CIF MASKING " + number.length());
				result = getX(3) + number.substring(3, 5) + getX(3);
			} else {
				return result;
			}

		} else {
			return result;
		}

		logger.debug("Masked Number " + result);

		return result;
	}

	public static String getMaskedString(String text) {
		StringBuilder masked = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			masked.append('X');
		}
		return masked.toString();
	}

	public static Long generateRandom() {
		DateFormat df = new SimpleDateFormat("yyMMddHHmmssSSS");
		Date today = Calendar.getInstance().getTime();
		String tranDate = df.format(today);
		return Long.parseLong(tranDate);

	}

	public static String getX(int count) {

		String result;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append("X");
		}

		result = sb.toString();
		return result;
	}

	public static String getMessageByLocale(String id, MessageSource messageSource) {
		String message;
		try {
			message = messageSource.getMessage(id, null, LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException ex) {

			message = messageSource.getMessage("SERVICEEXCEPTION", null, LocaleContextHolder.getLocale());
			logger.debug(ex);
		}
		return message;
	}

	private static String canonicalize(String input) {
		return sun.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD, 0);
	}

	public static String getRequestHeaderParam(HttpServletRequest request, String fieldName) {
		String input = request.getHeader(fieldName);
		if (checkNullEmpty(input)) {
			// important - always canonicalize before validating
			String canonical = canonicalize(input);
			if (!longTextPattern.matcher(canonical).matches()) {
				logger.debug("Improper format in " + fieldName + " field : " + input);
				return "";
			}
			return canonical;
		}
		return input;
	}

	public static String checkResponseStringNull(String responseString) throws ApiException {
		if (responseString == null) {
			logger.debug(responseString + " is coming null");
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		} else {
			return responseString;
		}
	}

	public static String checkUniqueConstraint(Exception ex) {
		ConstraintViolationException con = (ConstraintViolationException) ex.getCause();
		PSQLException psql = (PSQLException) con.getCause();
		logger.debug(psql.getSQLState());
		if (psql.getSQLState().equals(ErrorConstants.UNIQUECONSTRAINTSTATE)) {
			return "dublicate";
		}
		return null;
	}

	public static Boolean checkUniqueConstraintBool(Exception ex) {
		ConstraintViolationException con = (ConstraintViolationException) ex.getCause();
		PSQLException psql = (PSQLException) con.getCause();
		logger.debug(psql.getSQLState());
		if (psql.getSQLState().equals(ErrorConstants.UNIQUECONSTRAINTSTATE)) {
			return true;
		}
		return false;
	}

	public static String checkUniqueConstraintHibernate(HibernateException ex) {

		PSQLException psql = (PSQLException) ex.getCause();
		logger.debug(psql.getSQLState());
		if (psql.getSQLState().equals(ErrorConstants.UNIQUECONSTRAINTSTATE)) {
			return "dublicate";
		}
		return null;
	}

	public static Boolean checkUniqueConstraintHibernateBool(HibernateException ex) {
		PSQLException psql = (PSQLException) ex.getCause();
		logger.debug(psql.getSQLState());
		if (psql.getSQLState().equals(ErrorConstants.UNIQUECONSTRAINTSTATE)) {
			return true;
		}
		return false;
	}

	public static String getCaseChangedName(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	}

	public static String capitalize(final String line) {
		return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}

	public static String toTitleCase(String givenString) {
		String[] arr = givenString.split(" ");
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < arr.length; i++) {
			sb.append(Character.toUpperCase(arr[i].charAt(0))).append(arr[i].substring(1)).append(" ");
		}
		return sb.toString().trim();
	}

	public static String CapsFirst(String string) {
		String[] words = string.split(" ");
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < words.length; i++) {
			ret.append(Character.toUpperCase(words[i].charAt(0)));
			ret.append(words[i].substring(1));
			if (i < words.length - 1) {
				ret.append(' ');
			}
		}
		return ret.toString();
	}
	
	public static String patternMatch(String string) {

		Pattern pattern = Pattern.compile("(\\[\\d+\\])");
		Matcher matcher = pattern.matcher(string);
		String val1 = "";
		while (matcher.find()) {
			val1 = matcher.group().replace("[", "").replace("]", "");

		}

		return val1;
	}
	
	public static List<String> fetchEntites(String rawText) {

		List<String> entityIds = new ArrayList<>();
		if (rawText != null) {
			Pattern pattern = Pattern.compile("\\%(.*?)\\%");
			Matcher matcher = pattern.matcher(rawText);

			while (matcher.find()) {
				entityIds.add(matcher.group().replace("%", ""));
			}
		}
		return entityIds;
	}
	
	
	public static List<String> fetchActions(String rawText) {

		List<String> actionIds = new ArrayList<>();
		if (rawText != null) {
			Pattern pattern = Pattern.compile("\\@(.*?)\\@");
			Matcher matcher = pattern.matcher(rawText);

			while (matcher.find()) {
				actionIds.add(matcher.group().replace("@", ""));
			}
		}
		return actionIds;
	}
	

	public static String dateFromInstant(Instant date) {
		Date myDate = Date.from(date);
		SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,yyyy hh:mma");
		return formatter.format(myDate);

	}

	public static void commonExceptionMethod(Exception ex) throws ApiException {
		logger.error(Utility.getExceptionMessage(ex));
		if (Utility.checkUniqueConstraintBool(ex)) {
			throw new ApiException("DUPLICATE_ENTRY", "RECORD ALREAY EXISTS");
		}
		throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);

	}
	

	public static void commonHibernateExceptionMethod(HibernateException ex) throws ApiException {
		logger.error(Utility.getExceptionMessage(ex));
		if (Utility.checkUniqueConstraintHibernateBool(ex)) {
			throw new ApiException("DUPLICATE_ENTRY", "RECORD ALREAY EXISTS");
		}
		throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);

	}

	public static void finallyBlock(Session session) {
		if (session != null) {
			session.close();
		}
	}

	public static String logAPIResponse(ResponseEntity<JsonNode> serviceResponse, ObjectMapper mapper)
			throws JsonProcessingException {

		return mapper.writer().writeValueAsString(serviceResponse.getBody());

	}

}
