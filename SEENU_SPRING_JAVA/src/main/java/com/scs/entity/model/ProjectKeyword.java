package com.scs.entity.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

@Entity
@Table(name = "t_project_keyword")
public class ProjectKeyword implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6953322733395949564L;


	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "project_keyword_id", unique = true, nullable = false)
	private Long id;

	
	@Column(name = "project_keyword")
	private String projectKeyword;

	@Column(name = "project_keyword_type")
	private String keywordType;

	@Column(name = "project_id")
	private Long projectId;

	@Column(name = "locale_code")
	private String localeCode;
	
	public ProjectKeyword() {
		//
	}

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
