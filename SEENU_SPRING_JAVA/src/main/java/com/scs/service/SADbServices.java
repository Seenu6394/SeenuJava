package com.scs.service;

import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;

public interface SADbServices {

	public Object getServiceActionDetails(BaseRequestModel baseModel) throws ApiException;

	public Object createServiceAction(BaseRequestModel baseModel) throws ApiException;

	public Object updateServiceAction(BaseRequestModel baseModel) throws ApiException;

	public Object deleteServiceAction(BaseRequestModel baseModel, String id) throws ApiException;

	public Object getServiceActionById(BaseRequestModel baseModel, String id) throws ApiException;

	public Object getServiceActionByKu(BaseRequestModel baseModel, String id) throws ApiException;

	// public Object createServiceResponse(BaseRequestModel baseModel) throws ApiException;

	public Object createErrorResponse(BaseRequestModel baseModel) throws ApiException;

	public Object deleteErrorResponse(BaseRequestModel baseModel, String id) throws ApiException;

	public Object updateSAIntent(BaseRequestModel baseModel) throws ApiException;
}
