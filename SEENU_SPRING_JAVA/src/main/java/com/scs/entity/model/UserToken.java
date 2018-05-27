package com.scs.entity.model;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "user_token")
public class UserToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8372154240596455011L;
	/**
	 * 
	 */
	@Id
	@Column(name = "token_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userToken_generator")
	@SequenceGenerator(name = "userToken_generator", sequenceName = "userTokenr_seq", allocationSize = 1, initialValue = 1)
	private Long id;
	

	/*
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "token_id", unique = true, nullable = false)
	private Long id;*/
	
	
	@Column(name = "token")
	private String token;

	@Column(name = "enabled")
	private short enabled;

	@Column(name = "expiryDate")
	private Instant expiryDate;

	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private UserInfo user;

	public UserToken() {
		//
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public short getEnabled() {
		return enabled;
	}

	public void setEnabled(short enabled) {
		this.enabled = enabled;
	}

	public Instant getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Instant expiryDate) {
		this.expiryDate = expiryDate;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

}