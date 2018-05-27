package com.scs.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class CallbackModel {
	
	private String id;
	
	private String date;
	
	private List<ChatMessagingModel> messaging = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<ChatMessagingModel> getMessaging() {
		return messaging;
	}

	public void setMessaging(List<ChatMessagingModel> messaging) {
		this.messaging = messaging;
	}

}
