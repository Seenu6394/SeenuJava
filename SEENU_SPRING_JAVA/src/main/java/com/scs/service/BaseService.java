package com.scs.service;

import javax.servlet.http.HttpSession;

import com.scs.entity.model.Ku;
import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;

public interface BaseService {

	public Object authenticateMobileUser(BaseRequestModel baseModel, HttpSession session,Ku customer) throws ApiException;

	public Object getAccounts(BaseRequestModel baseModel, HttpSession session) throws ApiException;
	
	public Object validateTransfer(BaseRequestModel baseModel, HttpSession session) throws ApiException;
	
	public Object transfer(HttpSession session) throws ApiException;
}
