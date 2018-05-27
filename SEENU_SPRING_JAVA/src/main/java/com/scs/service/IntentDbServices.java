package com.scs.service;

import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;

public interface IntentDbServices {

	public Object getintentDetails(BaseRequestModel baseModel) throws ApiException;
	
	public Object createintent(BaseRequestModel baseModel) throws ApiException;

	public Object createKeyword(BaseRequestModel baseModel) throws ApiException;

	public Object deleteintent(BaseRequestModel baseModel, String id) throws ApiException;
	
	public Object deleteintentbyIntentId(BaseRequestModel baseModel, String id) throws ApiException;

	public Object getIntentById(BaseRequestModel baseModel, String id) throws ApiException;

	public Object updateIntent(BaseRequestModel baseModel) throws ApiException;

	public Object getIntentByKu(BaseRequestModel baseModel, String id) throws ApiException;

	public Object deleteKeyword(BaseRequestModel baseModel, String id) throws ApiException;
	
	public Object createintentResponses(BaseRequestModel baseModel) throws ApiException;

	public Object deleteIntentResponse(BaseRequestModel baseModel, String id) throws ApiException;

	public Object deleteIntentResponseByResponseID(BaseRequestModel baseModel, String id) throws ApiException;

	public Object getintentLst(BaseRequestModel baseModel) throws ApiException;

	public Object deleteIntentWorkflow(BaseRequestModel baseModel, String id) throws ApiException;

	public Object getWorkflowByIntentId(Long id) throws ApiException;

	public Object updateIntentResponse(BaseRequestModel baseModel) throws ApiException;

	public Object getKeywordByIntentId(BaseRequestModel baseModel, Long id) throws ApiException;

	public Object createProjectKeyword(BaseRequestModel baseModel) throws ApiException;

	public Object getProjectKeywords(BaseRequestModel baseModel) throws ApiException;

	public Object getKeywords(BaseRequestModel baseModel) throws ApiException;

	public Object deleteProjectKeyword(BaseRequestModel baseModel) throws ApiException;

	public Object getintentExtnDetails(BaseRequestModel baseModel) throws ApiException;

	

//	public Object updateResponse(BaseRequestModel baseModel) throws ApiException;

//	public Object getKeywordByNameandIntentId(BaseRequestModel baseModel, Long id) throws ApiException;

	

}
