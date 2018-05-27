package com.scs.entity.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "t_action")
@JsonInclude(Include.NON_NULL)
public class Action implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1985224587644742976L;


	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "action_id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "action_name")
	private String name;

	@Column(name = "calling_interval")
	private Long callingInterval;

	@Column(name = "warning_message")
	private String warningMessage;

	@Column(name = "global_identifier")
	private String globalIdentifier;
	
	@Column(name = "data_type")
	private String dataType;

	@Column(name = "kuid")
	private Long kuId;
	
	@OneToMany(mappedBy = "action", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference
	private List<ActionExtn> actionExtn = new ArrayList<>();

	
	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "intent_id")
	@JsonBackReference(value = "intentAction-reference")
	private Intent intent;

	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "entity_id", unique = true)
	@JsonBackReference(value = "entityAction-reference")
	private EntityDetails entity;

	@OneToMany(mappedBy = "action", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference(value = "action-reference")
	private List<ErrorResponse> errorResponses = new ArrayList<>();

	@OneToMany(mappedBy = "action", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference(value = "confirmAction-reference")
	private List<Confirm> confirm = new ArrayList<>();

	@OneToOne(mappedBy = "action", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JsonManagedReference(value = "action-workflow")
	private WorkFlow workFlow = new WorkFlow();

	public Action() {
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

	public Long getCallingInterval() {
		return callingInterval;
	}

	public void setCallingInterval(Long callingInterval) {
		this.callingInterval = callingInterval;
	}

	public String getWarningMessage() {
		return warningMessage;
	}

	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}

	public String getGlobalIdentifier() {
		return globalIdentifier;
	}

	public void setGlobalIdentifier(String globalIdentifier) {
		this.globalIdentifier = globalIdentifier;
	}

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

	public EntityDetails getEntity() {
		return entity;
	}

	public void setEntity(EntityDetails entity) {
		this.entity = entity;
	}

	public List<ErrorResponse> getErrorResponses() {
		return errorResponses;
	}

	public void setErrorResponses(List<ErrorResponse> errorResponses) {
		this.errorResponses = errorResponses;
	}

	public List<Confirm> getConfirm() {
		return confirm;
	}

	public void setConfirm(List<Confirm> confirm) {
		this.confirm = confirm;
	}

	public WorkFlow getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(WorkFlow workFlow) {
		this.workFlow = workFlow;
	}

	public Long getKuId() {
		return kuId;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public List<ActionExtn> getActionExtn() {
		return actionExtn;
	}

	public void setActionExtn(List<ActionExtn> actionExtn) {
		this.actionExtn = actionExtn;
	}

	public void setKuId(Long kuId) {
		this.kuId = kuId;
	}

}
