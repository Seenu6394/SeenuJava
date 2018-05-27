package com.scs.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.scs.entity.model.Intent;

@JsonInclude(Include.NON_NULL)
public class MessageModel {

	private Long id;

	private String messageCode;
	
	private IntentModel intent;

	private List<ResponseModel> responses = new ArrayList<>();


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public List<ResponseModel> getResponses() {
		return responses;
	}

	public void setResponses(List<ResponseModel> responses) {
		this.responses = responses;
	}
	
	public IntentModel getIntent() {
		return intent;
	}

	public void setIntent(IntentModel intent) {
		this.intent = intent;
	}

}
