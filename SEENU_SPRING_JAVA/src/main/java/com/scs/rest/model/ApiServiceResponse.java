package com.scs.rest.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.scs.util.ApiConstants;

public class ApiServiceResponse {
	
	public ApiServiceResponse(JsonNode responseNode) {
		super();
		this.responseNode = responseNode;
	}

	private JsonNode responseNode;

	public JsonNode getResponseNode() {
		return responseNode;
	}

	public void setResponseNode(JsonNode responseNode) {
		this.responseNode = responseNode;
	}

	public String getStringValue(String key){
		if (responseNode.has(key) && !responseNode.get(key).isNull()) {
			return responseNode.get(key).textValue();
		} else {
			return "";
		}
	}
	
	public Double getDoubleValue(String key){
		if (responseNode.has(key) && !responseNode.get(key).isNull()) {
			return responseNode.get(key).asDouble();
		} else {
			return 0.0;
		}
	}
	
	public int getIntValue(String key){
		return responseNode.get(key).asInt();
	}
	
	public boolean getBooleanValue(String key){
		return responseNode.get(key).booleanValue();
	}
	
	public JsonNode getObject(String key){
		return responseNode.get(key);	
	}
	
	public boolean isValidResponse() {
			return "000".equals(getErrorCode());
	}
	
	public String getErrorMessage() {
		if (responseNode != null && responseNode.has(ApiConstants.API_RESP_ERR_MSG)) {
				return getStringValue(ApiConstants.API_RESP_ERR_MSG);
		}
		return "";
	}
	
	public String getErrorCode() {
		if (responseNode != null && responseNode.has(ApiConstants.API_RESP_ERR_CODE)) {
				return getStringValue(ApiConstants.API_RESP_ERR_CODE);
		}
		return "";
	}
	
}
