package com.scs.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.scs.entity.model.RegexExtn;
@JsonInclude(Include.NON_NULL)


public class RegExModel  {

	
	private Long id;

	private String expression;

	private String errorCode;

	private Long kuId;

	private String regexname;
	
	private Long regmapid;

	private String date;
	
	private List<RegexExtnModel> regexes = new ArrayList<>();
	
//	private List<ErrorResponseModel> errorResponses;
	


/*	public List<ErrorResponseModel> getErrorResponses() {
		return errorResponses;
	}

	public void setErrorResponses(List<ErrorResponseModel> errorResponses) {
		this.errorResponses = errorResponses;
	}
*/
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

	public Long getRegmapid() {
		return regmapid;
	}

	public void setRegmapid(Long regmapid) {
		this.regmapid = regmapid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public List<RegexExtnModel> getRegexes() {
		return regexes;
	}

	public void setRegexes(List<RegexExtnModel> regexes) {
		this.regexes = regexes;
	}

}
