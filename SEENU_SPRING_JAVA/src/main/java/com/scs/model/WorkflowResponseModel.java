package com.scs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

public class WorkflowResponseModel {

	private Long id;

	private WorkflowMetadataModel metaData;

	private String name;

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public WorkflowMetadataModel getMetaData() {
		return metaData;
	}

	public void setMetaData(WorkflowMetadataModel metaData) {
		this.metaData = metaData;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
}
