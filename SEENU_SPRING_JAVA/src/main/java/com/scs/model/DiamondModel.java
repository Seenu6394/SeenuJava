package com.scs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

public class DiamondModel {

	private WorkflowSequenceModel workflowSequenceModel;

	public WorkflowSequenceModel getWorkflowSequenceModel() {
		return workflowSequenceModel;
	}

	public void setWorkflowSequenceModel(WorkflowSequenceModel workflowSequenceModel) {
		this.workflowSequenceModel = workflowSequenceModel;
	}

}
