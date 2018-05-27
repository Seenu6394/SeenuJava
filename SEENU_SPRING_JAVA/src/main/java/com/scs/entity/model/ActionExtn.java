package com.scs.entity.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "t_action_extn")
@JsonInclude(Include.NON_NULL)
public class ActionExtn implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 2180450499153451587L;
	

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "webhook_url")
	private String url;

	@Column(name = "call_method")
	private String callMethod;

	@Column(name = "request_body")
	private String requestBody;

	@Column(name = "success_code")
	private String successCode;

	@Column(name = "error_code")
	private String errorCode;

	@Column(name = "locale_code")
	private String localeCode;

	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "action_id")
	@JsonBackReference
	private Action action;

	@Column(name = "response_path")
	private String responsePath;

	public ActionExtn() {
		//
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}

	public String getCallMethod() {
		return callMethod;
	}

	public void setCallMethod(String callMethod) {
		this.callMethod = callMethod;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getSuccessCode() {
		return successCode;
	}

	public void setSuccessCode(String successCode) {
		this.successCode = successCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}



	public String getResponsePath() {
		return responsePath;
	}

	public void setResponsePath(String responsePath) {
		this.responsePath = responsePath;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

}
