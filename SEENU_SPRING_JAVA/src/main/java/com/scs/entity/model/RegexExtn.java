
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

import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

@Entity
@Table(name = "t_regex_extn")
public class RegexExtn implements Serializable {

	private static final long serialVersionUID = -2287623048860544956L;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "id", unique = true, nullable = false)
	private Long id;


	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "regex_id")
	@JsonBackReference
	private RegEx regex;

	@Column(name = "error_message")
	private String errorMessage;

	@Column(name = "locale_code")
	private String localeCode;

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

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

}
