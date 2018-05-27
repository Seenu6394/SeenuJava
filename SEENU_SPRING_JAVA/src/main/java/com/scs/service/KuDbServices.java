package com.scs.service;

import com.scs.entity.model.Ku;
import com.scs.exception.ApiException;
import com.scs.model.BCSettingsModel;
import com.scs.model.BaseRequestModel;
import com.scs.model.SettingsModel;

public interface KuDbServices {

	public Object getKuDetails(BaseRequestModel baseModel) throws ApiException;

	public Object createKU(BaseRequestModel baseModel) throws ApiException;

	public Object updateKU(BaseRequestModel baseModel) throws ApiException;

	public Object deleteKU(BaseRequestModel baseModel, String id) throws ApiException;

	public Object getKuById(BaseRequestModel baseModel, String id) throws ApiException;

	public Object getUserByName(BaseRequestModel baseModel, String id) throws ApiException;

	public Object checkNames(BaseRequestModel baseModel, Ku ku) throws ApiException;

	public Object getKuByName(BaseRequestModel baseModel, String name) throws ApiException;

	public Object getSettings(BaseRequestModel baseModel) throws ApiException;

	public Object importSettings(BCSettingsModel bcSettingsModel) throws ApiException;

	public Object getProjectKeywordDetails() throws ApiException;

	public Object getLanguage() throws ApiException;


}
