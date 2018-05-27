package com.scs.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.scs.entity.model.Intent;

@JsonInclude(Include.NON_NULL)

public class BCSettingsModel {

	private SettingsModel settings;

	public SettingsModel getSettings() {
		return settings;
	}

	public void setSettings(SettingsModel settings) {
		this.settings = settings;
	}

}
