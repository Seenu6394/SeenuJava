package com.scs.service;

import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;

public interface SelectionListDbServices {

	public Object getSelectionListDetails(BaseRequestModel baseModel) throws ApiException;

	public Object createSelectionList(BaseRequestModel baseModel) throws ApiException;

	public Object updateSelectionList(BaseRequestModel baseModel) throws ApiException;

	public Object deleteSelectionList(BaseRequestModel baseModel, String id) throws ApiException;

	public Object getSelectionListById(BaseRequestModel baseModel, String id) throws ApiException;

}
