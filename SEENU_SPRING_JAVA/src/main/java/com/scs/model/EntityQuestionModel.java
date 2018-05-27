package com.scs.model;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.scs.entity.model.EntityDetails;

@JsonInclude(Include.NON_NULL)

public class EntityQuestionModel {

	/**
	 * 
	 */

	private Long id;

	private String question;

	private EntityDetails entity;
	
	private String example; 

	private String localeCode;
	
	private String engExample;
	
	private String arExample;
	
	private String title;

	private String buttonText;

	private String subTitle;
	
	private String imageUrl;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public EntityDetails getEntity() {
		return entity;
	}

	public void setEntity(EntityDetails entity) {
		this.entity = entity;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public String getEngExample() {
		return engExample;
	}

	public void setEngExample(String engExample) {
		this.engExample = engExample;
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

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
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


}
