package com.scs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

public class Language {

	private long language_id;

	private Boolean english;

	private Boolean arabic;

	public long getLanguage_id() {
		return language_id;
	}

	public void setLanguage_id(long language_id) {
		this.language_id = language_id;
	}

	public Boolean getEnglish() {
		return english;
	}

	public void setEnglish(Boolean english) {
		this.english = english;
	}

	public Boolean getArabic() {
		return arabic;
	}

	public void setArabic(Boolean arabic) {
		this.arabic = arabic;
	}
}
