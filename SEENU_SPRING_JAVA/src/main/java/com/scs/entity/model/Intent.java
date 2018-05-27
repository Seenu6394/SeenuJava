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

import org.apache.log4j.Logger;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scs.controller.WebSocketController;
import com.scs.util.Utility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

@Entity
@Table(name = "t_intent")
public class Intent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1272803936319186671L;
	private static final Logger logger = Logger.getLogger(Intent.class);


	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "intent_id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "intent_definition")
	private String name;

	@OneToMany(mappedBy = "intent", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference
	private List<IntentExtn> names = new ArrayList<>();

	@Column(name = "kuid", nullable = false)
	private Long kuId;

	@OneToMany(mappedBy = "intent", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference
	private List<Keyword> keywords = new ArrayList<>();

	@OneToMany(mappedBy = "intent", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference(value = "intentAction-reference")
	private List<Action> action = new ArrayList<>();

	@OneToMany(mappedBy = "intent", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference(value = "intentResponse-reference")
	private List<Response> responses = new ArrayList<>();

	@OneToMany(mappedBy = "intent", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference(value = "intentMessage-reference")
	private List<Message> message = new ArrayList<>();

	@OneToOne(mappedBy = "intent", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JsonManagedReference(value = "intent-workflow")
	private WorkFlow workFlow = new WorkFlow();
	
	@OneToMany(mappedBy = "intent", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference(value = "intentWorkflowSequence-reference")
	private List<WorkflowSequence> workflowSequence = new ArrayList<>();
	
	

	@Column(name = "active_ind")
	private String activeInd;

	@Transient
	private Integer positiveKeywords;

	@Transient
	private Integer negativeKeywords;

	@Column(name = "created")
	private String date;

	@Column(name = "global_identifier")
	private String globalIdentifier;

	private Boolean flag;

	public Intent() {
		//
	}

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

	public List<Keyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;
	}

	public Integer getPositiveKeywords() {
		return positiveKeywords;
	}

	public void setPositiveKeywords(Integer positiveKeywords) {
		this.positiveKeywords = positiveKeywords;
	}

	public List<WorkflowSequence> getWorkflowSequence() {
		return workflowSequence;
	}

	public void setWorkflowSequence(List<WorkflowSequence> workflowSequence) {
		this.workflowSequence = workflowSequence;
	}

	public Integer getNegativeKeywords() {
		return negativeKeywords;
	}

	public List<Response> getResponses() {
		return responses;
	}

	public void setResponses(List<Response> responses) {
		this.responses = responses;
	}

	public void setNegativeKeywords(Integer negativeKeywords) {
		this.negativeKeywords = negativeKeywords;
	}

	public List<IntentExtn> getNames() {
		return names;
	}

	public void setNames(List<IntentExtn> names) {
		this.names = names;
	}

	public List<Message> getMessage() {
		return message;
	}

	public void setMessage(List<Message> message) {
		this.message = message;
	}

	public WorkFlow getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(WorkFlow workFlow) {
		this.workFlow = workFlow;
	}

	public String getDate() {
		return date;
	}

	public String getActiveInd() {
		return activeInd;
	}

	public void setActiveInd(String activeInd) {
		this.activeInd = activeInd;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public String getGlobalIdentifier() {
		return globalIdentifier;
	}

	public void setGlobalIdentifier(String globalIdentifier) {
		this.globalIdentifier = globalIdentifier;
	}

	public List<Action> getAction() {
		return action;
	}

	public void setAction(List<Action> action) {
		this.action = action;
	}
	
	///remove clone and add this copy constructor
	
	//copy constructor
	public Intent(Intent intent) {
		
		this.id = intent.id;
		this.name = intent.name;
		this.names = intent.names;
		this.kuId = intent.kuId;
		this.keywords = intent.keywords;
		this.action = intent.action;
		this.responses = intent.responses;
		this.message = intent.message;
		this.workFlow = intent.workFlow;
		this.workflowSequence = intent.workflowSequence;
		this.activeInd = intent.activeInd;
		this.positiveKeywords = intent.positiveKeywords;
		this.negativeKeywords = intent.negativeKeywords;
		this.date = intent.date;
		this.globalIdentifier = intent.globalIdentifier;
		this.flag = intent.flag;
	}
	
}
