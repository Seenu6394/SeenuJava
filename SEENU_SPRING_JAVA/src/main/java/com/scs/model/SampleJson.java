package com.scs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SampleJson {

	private String event;

	private Long conversation_id;

	private Long support_catagory_id;

	private String support_catagory_name;

	private Long operator_id;

	private String operator_name;

	private Long end_customer_id;

	private String end_customer_name;

	private String body;

	private String file_url;
	
	private String sessionId;

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Long getConversation_id() {
		return conversation_id;
	}

	public void setConversation_id(Long conversation_id) {
		this.conversation_id = conversation_id;
	}

	public Long getSupport_catagory_id() {
		return support_catagory_id;
	}

	public void setSupport_catagory_id(Long support_catagory_id) {
		this.support_catagory_id = support_catagory_id;
	}

	public String getSupport_catagory_name() {
		return support_catagory_name;
	}

	public void setSupport_catagory_name(String support_catagory_name) {
		this.support_catagory_name = support_catagory_name;
	}

	public Long getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(Long operator_id) {
		this.operator_id = operator_id;
	}

	public String getOperator_name() {
		return operator_name;
	}

	public void setOperator_name(String operator_name) {
		this.operator_name = operator_name;
	}

	public Long getEnd_customer_id() {
		return end_customer_id;
	}

	public void setEnd_customer_id(Long end_customer_id) {
		this.end_customer_id = end_customer_id;
	}

	public String getEnd_customer_name() {
		return end_customer_name;
	}

	public void setEnd_customer_name(String end_customer_name) {
		this.end_customer_name = end_customer_name;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getFile_url() {
		return file_url;
	}

	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	
}
