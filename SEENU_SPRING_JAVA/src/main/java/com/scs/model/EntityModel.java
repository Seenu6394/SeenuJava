package com.scs.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.scs.entity.model.Action;
import com.scs.entity.model.EntityDetails;
import com.scs.entity.model.EntityQuestion;
import com.scs.entity.model.EntityRegex;
import com.scs.entity.model.RegEx;
import com.scs.entity.model.Response;
import com.scs.entity.model.WorkFlow;
import com.scs.entity.model.WorkflowSequence;

public class EntityModel {

	private Long id;
	
	private String name;

	private Long intentId;
	
	private String intentName;

	private EntityDetails entity;
	
	private String dataType;
	
	private String date;

	private String arExample;
	
	private String engExample;
	
	private Long kuId;

	private List<RegExModel> regex;
	
	private String regexname;
	
	private String example;
	
	private String entityType;
	
	private ActionModel action = new ActionModel();
	
	private List<EntityQuestionModel> questions = new ArrayList<>();
	
	private List<ResponseModel> responses = new ArrayList<>();
	
	private WorkFlowModel workFlow = new WorkFlowModel();
	
	private WorkflowSequenceModel workflowSequence = new WorkflowSequenceModel();
	
	private String required;
	
	private String title;
	
	private String subTitle;
	
	private String imageUrl;
	
	private String buttonText;

	private List<EntityRegex> entityRegex = new ArrayList<>();
	
	private String globalIdentifier;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIntentId() {
		return intentId;
	}

	

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getKuId() {
		return kuId;
	}

	public void setKuId(Long kuId) {
		this.kuId = kuId;
	}

	public EntityDetails getEntity() {
		return entity;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setEntity(EntityDetails entity) {
		this.entity = entity;
	}

	public void setIntentId(Long intentId) {
		this.intentId = intentId;
	}

	public List<RegExModel> getRegex() {
		return regex;
	}

	public void setRegex(List<RegExModel> regex) {
		this.regex = regex;
	}

	public String getName() {
		return name;
	}

	public String getArExample() {
		return arExample;
	}

	public void setArExample(String arExample) {
		this.arExample = arExample;
	}

	public String getEngExample() {
		return engExample;
	}

	public void setEngExample(String engExample) {
		this.engExample = engExample;
	}

	

	public void setName(String name) {
		this.name = name;
	}

	public String getRegexname() {
		return regexname;
	}

	public void setRegexname(String regexname) {
		this.regexname = regexname;
	}

	public String getExample() {
		return example;
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

	public List<EntityQuestionModel> getQuestions() {
		return questions;
	}

	public void setQuestions(List<EntityQuestionModel> questions) {
		this.questions = questions;
	}

	public String getTitle() {
		return title;
	}

	public List<EntityRegex> getEntityRegex() {
		return entityRegex;
	}

	public void setEntityRegex(List<EntityRegex> entityRegex) {
		this.entityRegex = entityRegex;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	public List<ResponseModel> getResponses() {
		return responses;
	}

	public void setResponses(List<ResponseModel> responses) {
		this.responses = responses;
	}

	public WorkFlowModel getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(WorkFlowModel workFlow) {
		this.workFlow = workFlow;
	}

	public ActionModel getAction() {
		return action;
	}

	public String getGlobalIdentifier() {
		return globalIdentifier;
	}

	public void setGlobalIdentifier(String globalIdentifier) {
		this.globalIdentifier = globalIdentifier;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public void setAction(ActionModel action) {
		this.action = action;
	}

	public WorkflowSequenceModel getWorkflowSequence() {
		return workflowSequence;
	}

	public void setWorkflowSequence(WorkflowSequenceModel workflowSequence) {
		this.workflowSequence = workflowSequence;
	}

	public String getIntentName() {
		return intentName;
	}

	public void setIntentName(String intentName) {
		this.intentName = intentName;
	}

	

}
