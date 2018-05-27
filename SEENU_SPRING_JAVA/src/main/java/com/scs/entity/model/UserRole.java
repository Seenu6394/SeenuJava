package com.scs.entity.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "user_roles")
public class UserRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7672815375249917151L;

	@Id
	@Column(name = "role_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userrole_generator")
	@SequenceGenerator(name = "userrole_generator", sequenceName = "userrole_seq", allocationSize = 1, initialValue = 1)
	private Long id;

/*	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id", unique = true, nullable = false)
	private Long id;*/
	
	@Column(name = "user_role", unique = true)
	private String userRole;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
}
