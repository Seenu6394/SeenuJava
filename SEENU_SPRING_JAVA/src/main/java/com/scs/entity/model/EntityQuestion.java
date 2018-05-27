package com.scs.entity.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.scs.model.EntityModel;

@JsonInclude(Include.NON_NULL)

@Entity
@Table(name = "t_entity_question")
public class EntityQuestion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2392162741993391686L;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "question_id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "question")
	private String question;

	@Column(name = "entity_example")
	private String example;

	@Column(name = "locale_code")
	private String localeCode;

	@Column(name = "title")
	private String title;

	@Column(name = "button_text")
	private String buttonText;

	@Column(name = "sub_title")
	private String subTitle;

	@Column(name = "image_url")
	private String imageUrl;

	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "entity_id", nullable = false)
	@JsonBackReference
	private EntityDetails entity;

	public EntityQuestion() {
		//
	}

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
