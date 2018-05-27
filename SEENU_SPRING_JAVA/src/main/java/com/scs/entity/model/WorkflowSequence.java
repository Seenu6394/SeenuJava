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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "t_workflow_sequence")
@JsonInclude(Include.NON_NULL)
public class WorkflowSequence implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9192636434914420724L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "workflow_sequence_id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "intent_id")
	@JsonBackReference(value = "intentWorkflowSequence-reference")
	private Intent intent;

	@Column(name = "workflow_id")
	private Long workflowId;

	@Column(name = "workflow_sequence_key")
	private String workflowSequenceKey;

	@Column(name = "entry_type")
	private String entryType;

	@Column(name = "entry_expression")
	private String entryExpression;

	@Column(name = "primary_dest_wrkflw_id")
	private Long primaryDestWorkflowId;

	@Column(name = "primary_dest_sequence_key")
	private String primaryDestSequenceKey;

	@Column(name = "secondary_dest_wrkflw_id")
	private Long secondaryDestWorkflowId;

	@Column(name = "secondary_dest_sequence_key")
	private String secondaryDestSequenceKey;

	@Column(name = "terminal_type")
	private String terminalType;

	@Column(name = "required")
	private String required;

	@Column(name = "kuid")
	private Long kuId;

	@Column(name = "initial_validation")
	private String initialValidation;

	public WorkflowSequence() {
		//
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	public String getEntryType() {
		return entryType;
	}

	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}

	public String getEntryExpression() {
		return entryExpression;
	}

	public void setEntryExpression(String entryExpression) {
		this.entryExpression = entryExpression;
	}

	public Long getPrimaryDestWorkflowId() {
		return primaryDestWorkflowId;
	}

	public void setPrimaryDestWorkflowId(Long primaryDestWorkflowId) {
		this.primaryDestWorkflowId = primaryDestWorkflowId;
	}

	public Long getSecondaryDestWorkflowId() {
		return secondaryDestWorkflowId;
	}

	public void setSecondaryDestWorkflowId(Long secondaryDestWorkflowId) {
		this.secondaryDestWorkflowId = secondaryDestWorkflowId;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getWorkflowSequenceKey() {
		return workflowSequenceKey;
	}

	public void setWorkflowSequenceKey(String workflowSequenceKey) {
		this.workflowSequenceKey = workflowSequenceKey;
	}

	public String getPrimaryDestSequenceKey() {
		return primaryDestSequenceKey;
	}

	public void setPrimaryDestSequenceKey(String primaryDestSequenceKey) {
		this.primaryDestSequenceKey = primaryDestSequenceKey;
	}

	public String getSecondaryDestSequenceKey() {
		return secondaryDestSequenceKey;
	}

	public void setSecondaryDestSequenceKey(String secondaryDestSequenceKey) {
		this.secondaryDestSequenceKey = secondaryDestSequenceKey;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public Long getKuId() {
		return kuId;
	}

	public void setKuId(Long kuId) {
		this.kuId = kuId;
	}

	public String getInitialValidation() {
		return initialValidation;
	}

	public void setInitialValidation(String initialValidation) {
		this.initialValidation = initialValidation;
	}

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

}
