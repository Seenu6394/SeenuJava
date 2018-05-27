package com.scs.config;

import java.lang.reflect.Type;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scs.util.Utility;

@Component
@ControllerAdvice
public class ApplicationRequestAdviceAdapter extends RequestBodyAdviceAdapter{

	private static final Logger logger = Logger.getLogger(ApplicationRequestAdviceAdapter.class);
	
	@Autowired
	protected ObjectMapper mapper;

	
	@Override
	public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
		return true;
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter,
			Type type, Class<? extends HttpMessageConverter<?>> aClass) {
		try {
			logger.info(Utility.getLogDump(mapper.writeValueAsString(body), "REQUEST RECEIVED"));
		} catch (JsonProcessingException e) {
			logger.error("ERROR READING REQUEST : " + e);
		}
		return body;
	}
}
	
