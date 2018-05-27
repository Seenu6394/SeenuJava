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

@JsonInclude(Include.NON_NULL)

@Entity
@Table(name = "t_error_response")
public class ErrorResponse implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 2331735800813072750L;
	

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "error_code")
	private String errorCode;

	@Column(name = "error_response")
	private String errorResponse;

	@Column(name = "kuid")
	private Long kuId;

	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "action_id", nullable = false)
	@JsonBackReference(value = "action-reference")
	private Action action;
	
	@Column(name = "locale_code")
	private String localeCode;
	
/*	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "regex_id", nullable = false)
	@JsonBackReference(value = "error-reference")
	private RegEx regex;
*/
	public ErrorResponse() {
		//
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}



	public Long getKuId() {
		return kuId;
	}

	public void setKuId(Long kuId) {
		this.kuId = kuId;
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

	public String getErrorResponse() {
		return errorResponse;
	}

	public void setErrorResponse(String errorResponse) {
		this.errorResponse = errorResponse;
	}

/*	public RegEx getRegex() {
		return regex;
	}

	public void setRegex(RegEx regex) {
		this.regex = regex;
	}*/

}
