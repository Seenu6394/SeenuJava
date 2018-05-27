package com.scs.entity.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

@Entity
@Table(name = "t_regex")
public class RegEx implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4997539443428645567L;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "regex_id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "expression", unique = true)
	private String expression;

	@Column(name = "error_code")
	private String errorCode;

	@Column(name = "regex_name")
	private String regexname;

	@OneToMany(mappedBy = "regex", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference
	private List<RegexExtn> regexes = new ArrayList<>();

	public RegEx() {
		//
	}

	public String getRegexname() {
		return regexname;
	}

	public void setRegexname(String regexname) {
		this.regexname = regexname;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public List<RegexExtn> getRegexes() {
		return regexes;
	}

	public void setRegexes(List<RegexExtn> regexes) {
		this.regexes = regexes;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
