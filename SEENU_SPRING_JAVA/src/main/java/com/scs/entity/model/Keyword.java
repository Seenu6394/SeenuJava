package com.scs.entity.model;

import java.io.Serializable;
import java.time.Instant;

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
import com.scs.util.Utility;

@JsonInclude(Include.NON_NULL)

@Entity
@Table(name = "t_keyword")
public class Keyword implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8597538559890565961L;

	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "keyword_id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "keyword")
	private String keywordField;

	@Column(name = "polarity")
	private String polarity;

	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "intent_id", nullable = false)
	@JsonBackReference
	private Intent intent;

	@Column(name = "created")
	private String date;

	@Column(name = "locale_code")
	private String localeCode;

	public Keyword() {
		//
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPolarity() {
		return polarity;
	}

	public void setPolarity(String polarity) {
		this.polarity = polarity;
	}

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

	public String getKeywordField() {
		return keywordField;
	}

	public void setKeywordField(String keywordField) {
		this.keywordField = keywordField;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

}
