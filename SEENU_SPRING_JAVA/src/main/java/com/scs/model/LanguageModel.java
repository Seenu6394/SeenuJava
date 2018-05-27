package com.scs.model;

import java.time.Instant;

public class LanguageModel {

	
	private Long id;

	private Boolean english;

	private Boolean arabic;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
