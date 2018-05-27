package com.scs.model;

import java.util.List;

public class ToNodeModel {
	
	private Long id;

	private String name;

	private Long kuId;
	
	private Long intentId;
	
	private Long workflowSequenceId;
	
	private String workflowSequenceKey;
	
	private String entryExpression;

	private String example;
	
	private String entityType;
	
	private String required;
	
	private List<EntityQuestionModel> questions;
	
	private ActionModel action;
	
	private List<ResponseModel> responses;
	
	private WorkFlowModel workFlow;
	
	private String date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getKuId() {
		return kuId;
	}

	public void setKuId(Long kuId) {
		this.kuId = kuId;
	}

	public String getExample() {
		return example;
	}
	public Long getIntentId() {
		return intentId;
	}

	public void setIntentId(Long intentId) {
		this.intentId = intentId;
	}
	public void setExample(String example) {
		this.example = example;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public List<EntityQuestionModel> getQuestions() {
		return questions;
	}

	public void setQuestions(List<EntityQuestionModel> questions) {
		this.questions = questions;
	}

	public ActionModel getAction() {
		return action;
	}

	public void setAction(ActionModel action) {
		this.action = action;
	}

	public List<ResponseModel> getResponses() {
		return responses;
	}

	public void setResponses(List<ResponseModel> responses) {
		this.responses = responses;
	}

	public String getWorkflowSequenceKey() {
		return workflowSequenceKey;
	}

	public void setWorkflowSequenceKey(String workflowSequenceKey) {
		this.workflowSequenceKey = workflowSequenceKey;
	}

	public Long getWorkflowSequenceId() {
		return workflowSequenceId;
	}

	public void setWorkflowSequenceId(Long workflowSequenceId) {
		this.workflowSequenceId = workflowSequenceId;
	}

	public String getEntryExpression() {
		return entryExpression;
	}

	public void setEntryExpression(String entryExpression) {
		this.entryExpression = entryExpression;
	}

	public WorkFlowModel getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(WorkFlowModel workFlow) {
		this.workFlow = workFlow;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
