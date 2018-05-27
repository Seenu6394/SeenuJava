
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

@Entity
@Table(name = "t_language")
public class Languages implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3275750951545128684L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "language_id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "english")
	private Boolean english;

	@Column(name = "arabic")
	private Boolean arabic;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getEnglish() {
		return english;
	}

	public void setEnglish(Boolean english) {
		this.english = english;
	}

	public Boolean getArabic() {
		return arabic;
	}

	public void setArabic(Boolean arabic) {
		this.arabic = arabic;
	}




}
