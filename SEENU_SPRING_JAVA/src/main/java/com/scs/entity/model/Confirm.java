
package com.scs.entity.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
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
@Table(name = "t_confirm")

public class Confirm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3284970269130437099L;

	
	
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "confirm_id", unique = true, nullable = false)
	private Long id;


	@Column(name = "confirmation_type")
	private String confirmationType;

	@Column(name = "confirmation_text")
	private String text;

	@Column(name = "confirmed_opt")
	private String confirmationOption;

	@Column(name = "unconfirmed_opt")
	private String unConfirmationOption;

	@Column(name = "locale_code")
	private String localeCode;

	@Column(name = "termination_text")
	private String terminationText;

	@Column(name = "kuid")
	private Long kuId;
	
	

	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(name = "action_id", nullable = false)
	@JsonBackReference(value = "confirmAction-reference")
	private Action action;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

	public String getConfirmationType() {
		return confirmationType;
	}

	public void setConfirmationType(String confirmationType) {
		this.confirmationType = confirmationType;
	}

	public String getConfirmationOption() {
		return confirmationOption;
	}

	public void setConfirmationOption(String confirmationOption) {
		this.confirmationOption = confirmationOption;
	}

	public String getUnConfirmationOption() {
		return unConfirmationOption;
	}

	public void setUnConfirmationOption(String unConfirmationOption) {
		this.unConfirmationOption = unConfirmationOption;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public String getTerminationText() {
		return terminationText;
	}

	public void setTerminationText(String terminationText) {
		this.terminationText = terminationText;
	}

}
