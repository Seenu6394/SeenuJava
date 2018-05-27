package com.scs.service;

import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;


public interface ReDbServices {

	public Object getReDetails(BaseRequestModel baseModel) throws ApiException;

	public Object createRe(BaseRequestModel baseModel) throws ApiException;

	public Object updateRe(BaseRequestModel baseModel) throws ApiException;

	public Object getReById(BaseRequestModel baseModel, String id) throws ApiException;

	public Object createRegexMapping(Long entityId, Long regexId) throws ApiException;

	public Object deleteRegexMapping(Long entityId, Long regexId) throws ApiException;

	public Object getRegexwithIntents(BaseRequestModel baseModel) throws ApiException;

	public Object deleteRegEx(String id) throws ApiException;

	
	
	
}
