package com.scs.entity.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

@Entity
@Table(name = "t_ku")
public class Ku implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4075245894830126254L;



	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "kuid", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "ku_name", unique = true)
	private String name;

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "kuid", insertable = false, updatable = false)
	private List<Intent> intents = new ArrayList<>();

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "kuid", insertable = false, updatable = false)
	private List<EntityDetails> entities = new ArrayList<>();

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "kuid", insertable = false, updatable = false)
	private List<Action> action = new ArrayList<>();

	@Column(name = "active_ind")
	private String activeInd;

	@Column(name = "spam_enable")
	private String spamEnable;

	@Column(name = "is_rankable")
	private String isRankable;
	
	private Boolean flag;

	@Transient
	private Set<RegEx> regex = new HashSet<>();

	public Ku() {
		//
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Intent> getIntents() {
		return intents;
	}

	public void setIntents(List<Intent> intents) {
		this.intents = intents;
	}

	public List<EntityDetails> getEntities() {
		return entities;
	}

	public void setEntities(List<EntityDetails> entities) {
		this.entities = entities;
	}

	public List<Action> getAction() {
		return action;
	}

	public void setAction(List<Action> action) {
		this.action = action;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public String getActiveInd() {
		return activeInd;
	}

	public void setActiveInd(String activeInd) {
		this.activeInd = activeInd;
	}

	public String getSpamEnable() {
		return spamEnable;
	}

	public void setSpamEnable(String spamEnable) {
		this.spamEnable = spamEnable;
	}

	public Set<RegEx> getRegex() {
		return regex;
	}

	public void setRegex(Set<RegEx> regex) {
		this.regex = regex;
	}

	public String getIsRankable() {
		return isRankable;
	}

	public void setIsRankable(String isRankable) {
		this.isRankable = isRankable;
	}

}
