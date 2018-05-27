package com.scs.service;

import javax.servlet.http.HttpSession;

import com.scs.entity.model.UserInfo;
import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;

public interface UserDbServices {

	public UserInfo getUserByName(String name) throws ApiException;
	
	public boolean updateSuccess(String name) throws ApiException;

	public Object getUsersDetails(BaseRequestModel baseModel) throws ApiException;

	public Object createUser(BaseRequestModel baseModel) throws ApiException;

	public Object updateUser(BaseRequestModel baseModel) throws ApiException;

	public Object deleteUser(BaseRequestModel baseModel, String id) throws ApiException;

	public Object getUserById(BaseRequestModel baseModel, String id) throws ApiException;

	public Object findUserByEmail(String email, HttpSession httpSession) throws ApiException;

	public Object findUserByResetToken(String resetToken, HttpSession httpSession) throws ApiException;

	public Object resetPassword(String password, HttpSession httpSession) throws ApiException;

	public Object createUserToken(UserInfo user, HttpSession httpSession) throws ApiException;

	public Object changePassword(BaseRequestModel baseModel, HttpSession httpSession) throws ApiException;

	public Object deleteUserToken(UserInfo user) throws ApiException;

	public Object loginFailure(String name) throws ApiException;

	public Object deleteUserLoginDetails(Long userid) throws ApiException;

	public Object getLanguages(BaseRequestModel baseModel) throws ApiException;

	public Object getLocale(BaseRequestModel baseModel) throws ApiException;

	public Object updateLanguage(BaseRequestModel baseModel) throws ApiException;


	// public Object getUsersRoles(BaseRequestModel baseModel) throws ApiException;
}
