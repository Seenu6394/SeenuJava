package com.scs.entity.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)

@Entity
@Table(name = "t_entity_type")
public class EntityTypeDetails implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = -600476218201053425L;


	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "entity_type_id", unique = true, nullable = false)
	private Long id;


	@Column(name = "entity_type_code")
	private String entityType;
	
	@Column(name = "entity_type_name")
	private String entityTypeName;

	public EntityTypeDetails() {
		//
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityTypeName() {
		return entityTypeName;
	}

	public void setEntityTypeName(String entityTypeName) {
		this.entityTypeName = entityTypeName;
	}

	

}
