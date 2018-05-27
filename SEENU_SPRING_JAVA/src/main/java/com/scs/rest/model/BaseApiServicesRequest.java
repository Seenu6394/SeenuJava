package com.scs.rest.model;

import com.scs.util.DateUtil;
import com.scs.util.Utility;

public class BaseApiServicesRequest implements Cloneable{

	private String serviceType;

	private String serviceName;

	private String messageID;


	private String timestamp;

	public BaseApiServicesRequest(String serviceType, String serviceName) {
		this.serviceType = serviceType;
		this.serviceName = serviceName;
		this.messageID = Utility.generateRandom().toString();
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String getTimestamp() {
		timestamp = DateUtil.getCurrentDateTime();
		return timestamp;
	}
	
	@Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
