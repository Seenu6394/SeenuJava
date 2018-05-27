package com.scs.util;

/**
 * Author : Sanal Samuel 
 * 
 */

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class SessionUtil {

	private static final Logger logger = Logger.getLogger(SessionUtil.class);

	/**
	 * Clear all the keys in the session. The MOBILE_SERVICES keys are the ones
	 * that start with the predefined MOBILE_SERVICES_.
	 * 
	 * @param request
	 */

	private SessionUtil() {

	}

	public static void clearAll(HttpServletRequest request) {

		Enumeration<String> attributeNames = request.getSession().getAttributeNames();

		while (attributeNames.hasMoreElements()) {
			String currentName = attributeNames.nextElement();
			if (currentName.startsWith(ApiConstants.MOBILE_SERVICES)) {
				request.getSession().removeAttribute(ApiConstants.MOBILE_SERVICES + currentName);
			}

		}
	}

	/**
	 * Get value from the session attribute
	 * 
	 * @param request
	 * @param key
	 * @return
	 */
	public static <T> T getValue(HttpSession session, StorageConstants key) {
		Object valueToReturn = session.getAttribute(generateStorageKey(key));
		if (valueToReturn != null) {
			return (T) valueToReturn;
		}

		return null;
	}

	/**
	 * Print all the session attributes
	 * 
	 * @param request
	 */
	public static void printAll(HttpServletRequest request) {
		Enumeration<String> attributeNames = request.getSession().getAttributeNames();

		while (attributeNames.hasMoreElements()) {
			String currentName = attributeNames.nextElement();
			if (currentName.startsWith(ApiConstants.MOBILE_SERVICES)) {
				logger.debug(currentName + ": " + request.getSession().getAttribute(currentName).toString());
			}
		}
	}

	/**
	 * Return a list of all the session attributes
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, Object> fetchAll(HttpSession session) {

		Enumeration<String> attributeNames = session.getAttributeNames();

		HashMap<String, Object> sessionDataMap = new HashMap<>();
		while (attributeNames.hasMoreElements()) {
			String currentName = attributeNames.nextElement();
			if (currentName.startsWith(ApiConstants.MOBILE_SERVICES)) {
				logger.debug(currentName + ": " + session.getAttribute(currentName).toString());
				sessionDataMap.put(currentName, session.getAttribute(currentName));
			}
		}
		return sessionDataMap;

	}

	/**
	 * Internally stores data in the following format prefix + key
	 * 
	 * This is to distinguish between the normal session variables and the
	 * application session attributes
	 * 
	 * 
	 * @param request
	 * @param key
	 * @return
	 */
	public static String generateStorageKey(StorageConstants key) {
		return ApiConstants.MOBILE_SERVICES + key;
	}

	/**
	 * Function to store information to the session
	 * 
	 * @param request
	 * @param key
	 * @param val
	 */
	public static void setValue(HttpSession session, StorageConstants key, Object val) {
		session.setAttribute(generateStorageKey(key), val);
	}

	public static void getNewSession(HttpServletRequest request) {
		request.getSession().getId();
		logger.debug("Session =" + request.getSession().getId().substring(0, 15));
		request.getSession().invalidate();
		HttpSession session = request.getSession();
		logger.debug("New Generated Session = " + session.getId().substring(0, 15));

	}

	public static boolean objectExists(HttpSession session, StorageConstants key) {

		Object valueToReturn = getValue(session, key);
		if (valueToReturn != null) {
			return true;
		}

		return false;

	}

	public static String getSessionId(HttpSession session) {
		return session.getId().substring(0, 15);
	}
}
