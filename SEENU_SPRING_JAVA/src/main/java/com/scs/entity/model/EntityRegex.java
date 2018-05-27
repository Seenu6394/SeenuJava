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

@JsonInclude(Include.NON_NULL)

@Entity
@Table(name = "t_entity_regex")
public class EntityRegex implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4063839834281307883L;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "entity_regex_id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "entity_id")
	private Long entityId;

	@Column(name = "regex_id ")
	private Long regexId;

	@Column(name = "created ")
	private Long created;
	
	

	public EntityRegex() {
		//
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getRegexId() {
		return regexId;
	}

	public void setRegexId(Long regexId) {
		this.regexId = regexId;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

}
