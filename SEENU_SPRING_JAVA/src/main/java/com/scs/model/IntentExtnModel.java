package com.scs.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.scs.entity.model.Intent;
import com.scs.entity.model.WorkFlow;

@JsonInclude(Include.NON_NULL)

public class IntentExtnModel {

	private Long id;

	private Intent intent;

	private String name;

	private String localeCode;

	public Intent getIntent() {
		return intent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

}
