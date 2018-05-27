package com.scs.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.scs.entity.model.IntentExtn;
import com.scs.entity.model.WorkFlow;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class IntentModel {

	private Long id;

	private String name;

	private Long kuId;
	
	private List<IntentExtn> names =  new ArrayList<>();
	
	//private List<IntentExtn> namesExtn =  new ArrayList<>();

	private List<KeywordModel> keywords = new ArrayList<>();

	private List<ActionModel> action = new ArrayList<>();

	private List<ResponseModel> responses = new ArrayList<>();
	
	private WorkFlowModel workFlow = new WorkFlowModel();

	private Integer positiveKeywords;

	private Integer negativeKeywords;
	
	private String globalIdentifier;
	
	private String activeInd;
	
	private String date;
	
	private String localeCode;

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

	public Long getKuId() {
		return kuId;
	}

	public void setKuId(Long kuId) {
		this.kuId = kuId;
	}

	public List<KeywordModel> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<KeywordModel> keywords) {
		this.keywords = keywords;
	}


	public List<ResponseModel> getResponses() {
		return responses;
	}

	public void setResponses(List<ResponseModel> responses) {
		this.responses = responses;
	}

	public List<IntentExtn> getNames() {
		return names;
	}

	public void setNames(List<IntentExtn> names) {
		this.names = names;
	}
	public Integer getPositiveKeywords() {
		return positiveKeywords;
	}

	public void setPositiveKeywords(Integer positiveKeywords) {
		this.positiveKeywords = positiveKeywords;
	}

	public Integer getNegativeKeywords() {
		return negativeKeywords;
	}

	public void setNegativeKeywords(Integer negativeKeywords) {
		this.negativeKeywords = negativeKeywords;
	}

	public WorkFlowModel getWorkFlow() {
		return workFlow;
	}

	/*public List<IntentExtn> getNamesExtn() {
		return namesExtn;
	}

	public void setNamesExtn(List<IntentExtn> namesExtn) {
		this.namesExtn = namesExtn;
	}*/

	public void setWorkFlow(WorkFlowModel workFlow) {
		this.workFlow = workFlow;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getGlobalIdentifier() {
		return globalIdentifier;
	}

	public void setGlobalIdentifier(String globalIdentifier) {
		this.globalIdentifier = globalIdentifier;
	}

	public String getActiveInd() {
		return activeInd;
	}

	public void setActiveInd(String activeInd) {
		this.activeInd = activeInd;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public List<ActionModel> getAction() {
		return action;
	}

	public void setAction(List<ActionModel> action) {
		this.action = action;
	}
	
	

}
