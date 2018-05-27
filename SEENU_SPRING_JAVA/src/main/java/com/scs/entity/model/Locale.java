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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

@Entity
@Table(name = "t_locale")
public class Locale implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1872478104626227232L;


	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "locale_id", unique = true, nullable = false)
	private Long id;
		
	@Column(name = "locale_name")
	private String localeName;

	@Column(name = "locale_code")
	private String localeCode;

	@Column(name = "locale_cnfrm_msg")
	private String localeConfirmMessage;

	@Column(name = "locale_error_msg")
	private String localeErrorMessage;

	@Column(name = "intent_choice_msg")
	private String intentChoiceMsg;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocaleName() {
		return localeName;
	}

	public void setLocaleName(String localeName) {
		this.localeName = localeName;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public String getLocaleConfirmMessage() {
		return localeConfirmMessage;
	}

	public void setLocaleConfirmMessage(String localeConfirmMessage) {
		this.localeConfirmMessage = localeConfirmMessage;
	}

	public String getLocaleErrorMessage() {
		return localeErrorMessage;
	}

	public void setLocaleErrorMessage(String localeErrorMessage) {
		this.localeErrorMessage = localeErrorMessage;
	}

	public String getIntentChoiceMsg() {
		return intentChoiceMsg;
	}

	public void setIntentChoiceMsg(String intentChoiceMsg) {
		this.intentChoiceMsg = intentChoiceMsg;
	}




}
