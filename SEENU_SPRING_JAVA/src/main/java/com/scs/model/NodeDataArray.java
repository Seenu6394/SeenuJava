package com.scs.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scs.entity.model.Action;
import com.scs.entity.model.Intent;
import com.scs.entity.model.Response;

public class NodeDataArray {

	private String category;
	
	@JsonProperty("class")
	private String classvar;
	
	private String text;

	private ActionModel action;

	private String isReadOnly;

	private String loc;

	private Long modelid;

	private String key;
	
	private Long orderId;
	
	private Long id;

	private List<RegExModel> regExs = new ArrayList<>();

	private EntityModel entity;
	
	private DiamondModel decision;
	
	private Boolean order;
	
	private String required;

	private IntentModel intent;
	
	private WorkflowSequenceModel workflowSequence;
		
	private List<ResponseModel> response = new ArrayList<>();
	
	private MessageModel message;
	
	private String intentName;
	
	private Boolean from;
	
	private Boolean to;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}



	public ActionModel getAction() {
		return action;
	}

	public void setAction(ActionModel action) {
		this.action = action;
	}

	public String getIsReadOnly() {
		return isReadOnly;
	}

	public void setIsReadOnly(String isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public Long getModelid() {
		return modelid;
	}

	public void setModelid(Long modelid) {
		this.modelid = modelid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}


	public EntityModel getEntity() {
		return entity;
	}

	public void setEntity(EntityModel entity) {
		this.entity = entity;
	}



	public List<RegExModel> getRegExs() {
		return regExs;
	}

	public void setRegExs(List<RegExModel> regExs) {
		this.regExs = regExs;
	}

	public String getClassvar() {
		return classvar;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public DiamondModel getDecision() {
		return decision;
	}

	public void setDecision(DiamondModel decision) {
		this.decision = decision;
	}

	public void setClassvar(String classvar) {
		this.classvar = classvar;
	}

	

	public IntentModel getIntent() {
		return intent;
	}


	public WorkflowSequenceModel getWorkflowSequence() {
		return workflowSequence;
	}

	public MessageModel getMessage() {
		return message;
	}

	public void setMessage(MessageModel message) {
		this.message = message;
	}

	public void setWorkflowSequence(WorkflowSequenceModel workflowSequence) {
		this.workflowSequence = workflowSequence;
	}

	public void setIntent(IntentModel intent) {
		this.intent = intent;
	}

	public Boolean getFrom() {
		return from;
	}


	public Boolean getOrder() {
		return order;
	}

	public void setOrder(Boolean order) {
		this.order = order;
	}

	public void setFrom(Boolean from) {
		this.from = from;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Boolean getTo() {
		return to;
	}

	public void setTo(Boolean to) {
		this.to = to;
	}

	public String getIntentName() {
		return intentName;
	}

	public void setIntentName(String intentName) {
		this.intentName = intentName;
	}

	public List<ResponseModel> getResponse() {
		return response;
	}

	public void setResponse(List<ResponseModel> response) {
		this.response = response;
	}

	

}
