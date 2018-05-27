package com.scs.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

public class EntityDetailsModel {

	private Long id;

	private Long kuId;

	private String name;

	private String entityType;

	private String example;

	private List<EntityQuestionModel> questions = new ArrayList<>();
	
	private ActionModel action;

	private String required;
	
	private String dataType;

	private Long intentId;

	private String engExample;

	private String arExample;
	
	private String title;
	
	private String subTitle;
	
	private String imageUrl;
	
	private String buttonText;

	private String globalIdentifier;
	
	private List<RegExModel> regex;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getKuId() {
		return kuId;
	}

	public void setKuId(Long kuId) {
		this.kuId = kuId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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


	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public Long getIntentId() {
		return intentId;
	}

	public void setIntentId(Long intentId) {
		this.intentId = intentId;
	}

	public String getGlobalIdentifier() {
		return globalIdentifier;
	}

	public String getEngExample() {
		return engExample;
	}

	public void setEngExample(String engExample) {
		this.engExample = engExample;
	}

	public ActionModel getAction() {
		return action;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setAction(ActionModel action) {
		this.action = action;
	}

	public String getArExample() {
		return arExample;
	}

	public void setArExample(String arExample) {
		this.arExample = arExample;
	}

	public String getTitle() {
		return title;
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

	public void setGlobalIdentifier(String globalIdentifier) {
		this.globalIdentifier = globalIdentifier;
	}

	public List<RegExModel> getRegex() {
		return regex;
	}

	public void setRegex(List<RegExModel> regex) {
		this.regex = regex;
	}



}
