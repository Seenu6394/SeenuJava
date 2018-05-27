package com.scs.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.scs.entity.model.Action;
import com.scs.entity.model.RegEx;

public class KuModel {

	private Long id;

	private String name;

	private Instant date;

	private List<IntentModel> intents = new ArrayList<>();

	private List<EntityDetailsModel> entities = new ArrayList<>();
	
	private List<Action> action = new ArrayList<>();
	
	private List<RegEx> regex = new ArrayList<>();
	
	private String activeInd;
	
	private String spamEnable;
	
	private String isRankable;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public List<IntentModel> getIntents() {
		return intents;
	}

	public void setIntents(List<IntentModel> intents) {
		this.intents = intents;
	}

	public List<EntityDetailsModel> getEntities() {
		return entities;
	}

	public void setEntities(List<EntityDetailsModel> entities) {
		this.entities = entities;
	}

	public List<Action> getAction() {
		return action;
	}

	public void setAction(List<Action> action) {
		this.action = action;
	}

	public List<RegEx> getRegex() {
		return regex;
	}

	public void setRegex(List<RegEx> regex) {
		this.regex = regex;
	}

	public String getActiveInd() {
		return activeInd;
	}

	public void setActiveInd(String activeInd) {
		this.activeInd = activeInd;
	}

	public String getSpamEnable() {
		return spamEnable;
	}

	public void setSpamEnable(String spamEnable) {
		this.spamEnable = spamEnable;
	}

	public String getIsRankable() {
		return isRankable;
	}

	public void setIsRankable(String isRankable) {
		this.isRankable = isRankable;
	}


}
