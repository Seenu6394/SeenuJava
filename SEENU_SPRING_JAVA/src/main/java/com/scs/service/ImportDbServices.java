package com.scs.service;

import com.scs.entity.model.Ku;
import com.scs.exception.ApiException;

public interface ImportDbServices {

	public Object importNewKu(Ku ku) throws ApiException;

}
