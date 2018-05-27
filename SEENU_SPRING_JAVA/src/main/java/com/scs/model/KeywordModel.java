package com.scs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.scs.entity.model.Intent;

@JsonInclude(Include.NON_NULL)

public class KeywordModel {

	private Long id;

	private String keywordField;

	private String polarity;

	private Intent intent;

	private String date;
	
	private String localeCode;
	
	private String kuId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public String getKeywordField() {
		return keywordField;
	}

	public void setKeywordField(String keywordField) {
		this.keywordField = keywordField;
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

	public String getKuId() {
		return kuId;
	}

	public void setKuId(String kuId) {
		this.kuId = kuId;
	}

}
