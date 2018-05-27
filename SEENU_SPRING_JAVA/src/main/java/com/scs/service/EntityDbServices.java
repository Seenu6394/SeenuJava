package com.scs.service;

import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;

public interface EntityDbServices {

	public Object getEntityDetails(BaseRequestModel baseModel) throws ApiException;

	public Object getEntityTypeDetails(BaseRequestModel baseModel) throws ApiException;

	public Object createEntity(BaseRequestModel baseModel) throws ApiException;

	public Object createEntityQuestion(BaseRequestModel baseModel) throws ApiException;

	public Object updateEntity(BaseRequestModel baseModel) throws ApiException;

	public Object deleteEntity(BaseRequestModel baseModel, String id) throws ApiException;

	public Object getEntityById(BaseRequestModel baseModel, String id) throws ApiException;

	public Object getEntityByKu(BaseRequestModel baseModel, String id) throws ApiException;

	public Object deleteEntityQuestion(BaseRequestModel baseModel, String id) throws ApiException;

	public Object getEntityQuestionsById(Long entityId) throws ApiException;

}
