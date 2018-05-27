package com.scs.config;

import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scs.util.Utility;

@Component
@ControllerAdvice
public class ApplicationResponseAdviceAdapter implements ResponseBodyAdvice<Object> {

	private static final Logger logger = Logger.getLogger(ApplicationResponseAdviceAdapter.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
			try {
				logger.info(Utility.getLogDump(mapper.writeValueAsString(body), "RESPONSE SENT"));
			} catch (JsonProcessingException e) {
				logger.error("ERROR WRITING RESPONSE : " + e);
			}
		return body;
	}

}