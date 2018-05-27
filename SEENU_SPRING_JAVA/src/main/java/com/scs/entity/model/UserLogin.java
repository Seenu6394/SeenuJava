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
@Table(name = "t_user_login")
public class UserLogin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6760189493043545586L;


	
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_generator")
	@SequenceGenerator(name = "id_generator", sequenceName = "t_user_login_seq", allocationSize = 1, initialValue = 1)
	private Long id;

	/*	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	*/

	@Column(name = "user_id")
	private Long userid;

	@Column(name = "loggedin_time")
	private String loggedInTime;

	@Column(name = "login_status")
	private String loginStatus;

	@Column(name = "login_attempts")
	private Long loginAttempts;

	public UserLogin() {
		//
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getLoggedInTime() {
		return loggedInTime;
	}

	public void setLoggedInTime(String loggedInTime) {
		this.loggedInTime = loggedInTime;
	}

	public String getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	public Long getLoginAttempts() {
		return loginAttempts;
	}

	public void setLoginAttempts(Long loginAttempts) {
		this.loginAttempts = loginAttempts;
	}

}