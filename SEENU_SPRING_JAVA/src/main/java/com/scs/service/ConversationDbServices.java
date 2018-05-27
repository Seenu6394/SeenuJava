package com.scs.service;

import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;

public interface ConversationDbServices {

	public Object getConversationDetails(BaseRequestModel baseModel) throws ApiException;

	public Object getConversationById(BaseRequestModel baseModel, String id) throws ApiException;

}
