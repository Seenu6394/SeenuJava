package com.scs.model;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.scs.entity.model.Intent;
import com.scs.entity.model.RegexExtn;
@JsonInclude(Include.NON_NULL)

public class IntentRegex {
	
	private Long id;

	private String expression;

	private String message;

	private Long kuId;

	private String regexname;

	private List<RegexExtn> regexes;
	
	private List<Intent> intents;
	
	public List<Intent> getIntents() {
		return intents;
	}

	public void setIntents(List<Intent> intents) {
		this.intents = intents;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getKuId() {
		return kuId;
	}

	public void setKuId(Long kuId) {
		this.kuId = kuId;
	}

	public String getRegexname() {
		return regexname;
	}

	public void setRegexname(String regexname) {
		this.regexname = regexname;
	}

	public List<RegexExtn> getRegexes() {
		return regexes;
	}

	public void setRegexes(List<RegexExtn> regexes) {
		this.regexes = regexes;
	}





}
