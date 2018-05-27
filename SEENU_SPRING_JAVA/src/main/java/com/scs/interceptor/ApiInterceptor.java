package com.scs.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.scs.util.SessionUtil;


public class ApiInterceptor extends HandlerInterceptorAdapter {
	

	private static final Logger logger = Logger.getLogger(ApiInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {
		
		logger.debug(request.getAttribute("authCode"));
		MDC.put("sessionId", SessionUtil.getSessionId(request.getSession()));
		logger.debug(SessionUtil.getSessionId(request.getSession()));
		logger.info("REQUEST URL::" + request.getRequestURL().toString() + " token :" + request.getHeader("Authorization"));
		logger.info("User-Agent :" + request.getHeader("User-Agent"));
		Enumeration<String> params  = request.getParameterNames();
		if (params != null) {
			logger.info("===================================================================================");

			while (params.hasMoreElements()) {
				String param = params.nextElement();
				logger.info("Parameter : " + param + " : Value : " + request.getParameter(param));
			}
			logger.info("===================================================================================");
		}
		return true;

    }
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
		logger.info("RESPONSE STATUS TO CLIENT : " + response.getStatus());
		MDC.remove("sessionId");
		response.setHeader("X-Powered-By", "");
		response.setHeader("cache-control", "no-store,no-cache");
		response.setHeader("pragma", "no-cache");
	}


}
