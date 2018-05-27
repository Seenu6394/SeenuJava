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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

@Entity
@Table(name = "t_workflow")
public class WorkFlow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6836819279641490664L;

	/**
	 * 
	 */
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "workflow_id", unique = true, nullable = false)
	private Long id;

	@Column(name = "metadata")
	private String metaData;

	@Column(name = "workflow_name")
	private String name;

	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "intent_id", unique = true)
	@JsonBackReference(value = "intent-workflow")
	private Intent intent;

	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "entity_id", unique = true)
	@JsonBackReference(value = "entity-workflow")
	private EntityDetails entity;

	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "action_id", unique = true)
	@JsonBackReference(value = "action-workflow")
	private Action action;

	@Column(name = "kuid")
	private Long kuId;

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMetaData() {
		return metaData;
	}

	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Long getKuId() {
		return kuId;
	}

	public void setKuId(Long kuId) {
		this.kuId = kuId;
	}

}
