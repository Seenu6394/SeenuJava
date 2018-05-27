
package com.scs.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.scs.entity.model.Intent;
import com.scs.entity.model.Languages;
import com.scs.entity.model.ProjectKeyword;

@JsonInclude(Include.NON_NULL)

public class SettingsModel {
	
	private Languages language;

	private List<ProjectKeyword> projectKeywords;


	public Languages getLanguage() {
		return language;
	}

	public void setLanguage(Languages language) {
		this.language = language;
	}

	public List<ProjectKeyword> getProjectKeywords() {
		return projectKeywords;
	}

	public void setProjectKeywords(List<ProjectKeyword> projectKeywords) {
		this.projectKeywords = projectKeywords;
	}



}
