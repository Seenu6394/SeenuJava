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
@Table(name = "t_response")
public class Response implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 7025992408161260090L;
	

	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "response_id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "response")
	private String responseText;

	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "intent_id")
	@JsonBackReference(value = "intentResponse-reference")
	private Intent intent;

	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "message_id")
	@JsonBackReference(value = "messageResponse-reference")
	private Message message;
	
	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "entity_id")
	@JsonBackReference(value = "entityResponse-reference")
	private EntityDetails entity;

	@Column(name = "kuid")
	private Long kuId;
	
	@Column(name = "locale_code")
	private String localeCode;
	
	@Column(name = "global_identifier")
	private String globalIdentifier;

	public Response() {
		//
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResponseText() {
		return responseText;
	}

	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

	public Long getKuId() {
		return kuId;
	}

	public void setKuId(Long kuId) {
		this.kuId = kuId;
	}

	public EntityDetails getEntity() {
		return entity;
	}

	public void setEntity(EntityDetails entity) {
		this.entity = entity;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public String getGlobalIdentifier() {
		return globalIdentifier;
	}

	public void setGlobalIdentifier(String globalIdentifier) {
		this.globalIdentifier = globalIdentifier;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
	

}
