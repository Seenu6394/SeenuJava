
package com.scs.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.scs.entity.model.RegEx;

@JsonInclude(Include.NON_NULL)


public class RegexExtnModel  {

	
	private Long id;

	private RegEx regex;

	private String errorMessage;

	private String localeCode;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RegEx getRegex() {
		return regex;
	}

	public void setRegex(RegEx regex) {
		this.regex = regex;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	

}
