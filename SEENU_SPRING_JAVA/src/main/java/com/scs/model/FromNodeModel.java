package com.scs.model;

import java.util.List;

public class FromNodeModel {

	private Long id;
	
	private String name;
	
	private Long kuId;
	
	private List<KeywordModel> keywords;
	
	private List<ResponseModel> responses;
	
	private Long positiveKeywords;
	
	private Long negativeKeywords;
	
	private String date;

	private Long workflowSequenceId;
	
	private String workflowSequenceKey;
	
	private String entryExpression;
	
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

	public List<KeywordModel> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<KeywordModel> keywords) {
		this.keywords = keywords;
	}

	public List<ResponseModel> getResponses() {
		return responses;
	}

	public void setResponses(List<ResponseModel> responses) {
		this.responses = responses;
	}


	public Long getPositiveKeywords() {
		return positiveKeywords;
	}

	public void setPositiveKeywords(Long positiveKeywords) {
		this.positiveKeywords = positiveKeywords;
	}

	public Long getNegativeKeywords() {
		return negativeKeywords;
	}

	public void setNegativeKeywords(Long negativeKeywords) {
		this.negativeKeywords = negativeKeywords;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public String getWorkflowSequenceKey() {
		return workflowSequenceKey;
	}

	public void setWorkflowSequenceKey(String workflowSequenceKey) {
		this.workflowSequenceKey = workflowSequenceKey;
	}
	
}
