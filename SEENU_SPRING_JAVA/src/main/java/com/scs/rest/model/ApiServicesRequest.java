package com.scs.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.scs.util.ApiServiceType;

@JsonInclude(Include.NON_NULL)
public class ApiServicesRequest extends BaseApiServicesRequest implements Cloneable {

	private String serviceUrl;

	public ApiServicesRequest(ApiServiceType serviceType) {
		super(serviceType.name(), serviceType.getServiceName());
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
