package com.scs.entity.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.scs.util.Utility;

@JsonInclude(Include.NON_NULL)

@Entity
@Table(name = "t_entity")
public class EntityDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4946511579549918446L;


	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "entity_id", unique = true, nullable = false)
	private Long id;


	@Column(name = "kuid", nullable = false)
	private Long kuId;

	@Column(name = "entity_name")
	private String name;

	@Column(name = "example")
	private String example;

	@Column(name = "entity_type_code")
	private String entityType;

	@Column(name = "required")
	private String required;

	@Column(name = "intent_id")
	private Long intentId;

	@Column(name = "global_identifier")
	private String globalIdentifier;
	
	@Column(name = "data_type")
	private String dataType;

	@OneToMany(mappedBy = "entity", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference
	private List<EntityQuestion> questions = new ArrayList<>();

	@OneToOne(mappedBy = "entity", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JsonManagedReference(value = "entityAction-reference")
	private Action action = new Action();

	@OneToMany(mappedBy = "entity", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference(value = "entityResponse-reference")
	private List<Response> responses = new ArrayList<>();
	
	
	@Transient
	private List<RegEx> regex;

	@Transient
	private List<EntityRegex> entityRegex = new ArrayList<>();
	
	private Boolean flag;

	@Column(name = "created")
	private String date;

	public EntityDetails() {
		//
	}

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

	public List<EntityQuestion> getQuestions() {
		return questions;
	}

	public void setQuestions(List<EntityQuestion> questions) {
		this.questions = questions;
	}

	
	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}


	public List<Response> getResponses() {
		return responses;
	}

	public String getGlobalIdentifier() {
		return globalIdentifier;
	}

	public void setGlobalIdentifier(String globalIdentifier) {
		this.globalIdentifier = globalIdentifier;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public void setResponses(List<Response> responses) {
		this.responses = responses;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRequired() {
		return required;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public List<RegEx> getRegex() {
		return regex;
	}

	public void setRegex(List<RegEx> regex) {
		this.regex = regex;
	}

	public Long getIntentId() {
		return intentId;
	}

	public void setIntentId(Long intentId) {
		this.intentId = intentId;
	}

	public List<EntityRegex> getEntityRegex() {
		return entityRegex;
	}

	public void setEntityRegex(List<EntityRegex> entityRegex) {
		this.entityRegex = entityRegex;
	}

}
