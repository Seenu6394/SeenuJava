package com.scs.model;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PuristUserLogin {

	@NotNull
   	@NotEmpty
	public String p_username;

	public String getP_username() {
		return p_username;
	}

	public void setP_username(String p_username) {
		this.p_username = p_username;
	}

	
}
