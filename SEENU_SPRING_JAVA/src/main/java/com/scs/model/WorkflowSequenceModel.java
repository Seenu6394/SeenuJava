package com.scs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

public class WorkflowSequenceModel {

	private Long id;
	
	private Long workflowId;
	
	private String workflowSequenceKey;

	private Long intentId;

	private String entryType;

	private String entryExpression;

	private Long primaryDestWorkflowId;
	
	private String primaryDestSequenceKey;
	
	private Long secondaryDestWorkflowId;
	
	private String secondaryDestSequenceKey;
	
	private String terminalType;
	
	private String required;

	private Long kuId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getWorkflowSequenceKey() {
		return workflowSequenceKey;
	}

	public void setWorkflowSequenceKey(String workflowSequenceKey) {
		this.workflowSequenceKey = workflowSequenceKey;
	}

	public Long getIntentId() {
		return intentId;
	}

	public void setIntentId(Long intentId) {
		this.intentId = intentId;
	}

	public String getEntryType() {
		return entryType;
	}

	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}

	public String getEntryExpression() {
		return entryExpression;
	}

	public void setEntryExpression(String entryExpression) {
		this.entryExpression = entryExpression;
	}

	public Long getPrimaryDestWorkflowId() {
		return primaryDestWorkflowId;
	}

	public void setPrimaryDestWorkflowId(Long primaryDestWorkflowId) {
		this.primaryDestWorkflowId = primaryDestWorkflowId;
	}


	public Long getSecondaryDestWorkflowId() {
		return secondaryDestWorkflowId;
	}

	public void setSecondaryDestWorkflowId(Long secondaryDestWorkflowId) {
		this.secondaryDestWorkflowId = secondaryDestWorkflowId;
	}



	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getPrimaryDestSequenceKey() {
		return primaryDestSequenceKey;
	}

	public void setPrimaryDestSequenceKey(String primaryDestSequenceKey) {
		this.primaryDestSequenceKey = primaryDestSequenceKey;
	}

	public String getSecondaryDestSequenceKey() {
		return secondaryDestSequenceKey;
	}

	public void setSecondaryDestSequenceKey(String secondaryDestSequenceKey) {
		this.secondaryDestSequenceKey = secondaryDestSequenceKey;
	}

	public Long getKuId() {
		return kuId;
	}

	public void setKuId(Long kuId) {
		this.kuId = kuId;
	}

	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}
	
	

}
