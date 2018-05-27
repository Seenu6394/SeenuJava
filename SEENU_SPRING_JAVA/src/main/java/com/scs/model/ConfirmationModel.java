package com.scs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

public class ConfirmationModel {

	private Long id;

	private String confirmationType;

	private String text;

	private String confirmationOption;

	private String unConfirmationOption;

	private String localeCode;

	private String terminationText;

	private Long kuId;

	private ActionModel action;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getConfirmationType() {
		return confirmationType;
	}

	public void setConfirmationType(String confirmationType) {
		this.confirmationType = confirmationType;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getConfirmationOption() {
		return confirmationOption;
	}

	public void setConfirmationOption(String confirmationOption) {
		this.confirmationOption = confirmationOption;
	}

	public String getUnConfirmationOption() {
		return unConfirmationOption;
	}

	public void setUnConfirmationOption(String unConfirmationOption) {
		this.unConfirmationOption = unConfirmationOption;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public String getTerminationText() {
		return terminationText;
	}

	public void setTerminationText(String terminationText) {
		this.terminationText = terminationText;
	}

	public Long getKuId() {
		return kuId;
	}

	public void setKuId(Long kuId) {
		this.kuId = kuId;
	}

	public ActionModel getAction() {
		return action;
	}

	public void setAction(ActionModel action) {
		this.action = action;
	}
}
