package com.scs.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

public class WorkflowMetadataModel {

	@JsonProperty("class")
	private String classvar;
	
	private List<NodeDataArray> nodeDataArray;

	private String linkFromPortIdProperty;

	private String linkToPortIdProperty;
	
	private LinkDataArray[] linkDataArray;

	public String getClassvar() {
		return classvar;
	}

	public void setClassvar(String classvar) {
		this.classvar = classvar;
	}

	public String getLinkFromPortIdProperty() {
		return linkFromPortIdProperty;
	}

	public void setLinkFromPortIdProperty(String linkFromPortIdProperty) {
		this.linkFromPortIdProperty = linkFromPortIdProperty;
	}

	public String getLinkToPortIdProperty() {
		return linkToPortIdProperty;
	}

	public void setLinkToPortIdProperty(String linkToPortIdProperty) {
		this.linkToPortIdProperty = linkToPortIdProperty;
	}

	public LinkDataArray[] getLinkDataArray() {
		return linkDataArray;
	}

	public void setLinkDataArray(LinkDataArray[] linkDataArray) {
		this.linkDataArray = linkDataArray;
	}

	public List<NodeDataArray> getNodeDataArray() {
		return nodeDataArray;
	}

	public void setNodeDataArray(List<NodeDataArray> nodeDataArray) {
		this.nodeDataArray = nodeDataArray;
	}


}
