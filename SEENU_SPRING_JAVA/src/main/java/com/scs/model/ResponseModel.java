package com.scs.model;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

public class ResponseModel {

	private Long id;

	private String responseText;

	private IntentModel intent;

	private Long entityId;

	private Long kuId;
	
	private WorkflowSequenceModel workflowSequence = new WorkflowSequenceModel();
	
	private String localeCode;

	private String globalIdentifier;
	
	private MessageModel message;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResponseText() {
		return responseText;
	}

	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

	public IntentModel getIntent() {
		return intent;
	}

	public void setIntent(IntentModel intent) {
		this.intent = intent;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getKuId() {
		return kuId;
	}

	public void setKuId(Long kuId) {
		this.kuId = kuId;
	}
	
	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public String getGlobalIdentifier() {
		return globalIdentifier;
	}

	public void setGlobalIdentifier(String globalIdentifier) {
		this.globalIdentifier = globalIdentifier;
	}

	public WorkflowSequenceModel getWorkflowSequence() {
		return workflowSequence;
	}

	public void setWorkflowSequence(WorkflowSequenceModel workflowSequence) {
		this.workflowSequence = workflowSequence;
	}

	public MessageModel getMessage() {
		return message;
	}

	public void setMessage(MessageModel message) {
		this.message = message;
	}



}
