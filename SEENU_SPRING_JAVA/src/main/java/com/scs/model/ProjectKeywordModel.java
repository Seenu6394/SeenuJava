
package com.scs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.scs.entity.model.Intent;

@JsonInclude(Include.NON_NULL)

public class ProjectKeywordModel {

	private Long id;

	private String projectKeyword;

	private String keywordType;

	private Long projectId;
	
	private String localeCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProjectKeyword() {
		return projectKeyword;
	}

	public void setProjectKeyword(String projectKeyword) {
		this.projectKeyword = projectKeyword;
	}

	public String getKeywordType() {
		return keywordType;
	}

	public void setKeywordType(String keywordType) {
		this.keywordType = keywordType;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

}
