package com.scs.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.scs.entity.model.Confirm;
import com.scs.entity.model.WorkFlow;
import com.scs.model.ErrorResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)

public class ActionModel {
	private Long id;

	private String name;

	private Long intentId;

	private Long entityId;

	private Long callingInterval;

	private String warningMessage;
	
	private String globalIdentifier;

	private Long kuId;

	private String responsePath;

	private String url;

	private String callMethod;

	private String requestBody;

	private String errorCode;

	private String successCode;

	private String node;

	private String dataType;

	private WorkflowSequenceModel workflowSequence = new WorkflowSequenceModel();

	private WorkFlowModel workFlow = new WorkFlowModel();

	private List<ActionExtnModel> actionExtn = new ArrayList<>();

	private List<ConfirmationModel> confirmModel = new ArrayList<>();

	private List<Confirm> confirm = new ArrayList<>();

	private List<ErrorResponseModel> errorResponse = new ArrayList<>();

	private List<ErrorResponseModel> errorResponses = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public List<ErrorResponseModel> getErrorResponse() {
		return errorResponse;
	}

	public void setErrorResponse(List<ErrorResponseModel> errorResponse) {
		this.errorResponse = errorResponse;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCallMethod() {
		return callMethod;
	}

	public void setCallMethod(String callMethod) {
		this.callMethod = callMethod;
	}

	public Long getKuId() {
		return kuId;
	}

	public void setKuId(Long kuId) {
		this.kuId = kuId;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public Long getIntentId() {
		return intentId;
	}

	public void setIntentId(Long intentId) {
		this.intentId = intentId;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}



	public List<ErrorResponseModel> getErrorResponses() {
		return errorResponses;
	}

	public String getSuccessCode() {
		return successCode;
	}

	public void setSuccessCode(String successCode) {
		this.successCode = successCode;
	}

	public void setErrorResponses(List<ErrorResponseModel> errorResponses) {
		this.errorResponses = errorResponses;
	}

	public String getGlobalIdentifier() {
		return globalIdentifier;
	}

	

	public WorkflowSequenceModel getWorkflowSequence() {
		return workflowSequence;
	}

	public void setWorkflowSequence(WorkflowSequenceModel workflowSequence) {
		this.workflowSequence = workflowSequence;
	}

	public void setGlobalIdentifier(String globalIdentifier) {
		this.globalIdentifier = globalIdentifier;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}



	public WorkFlowModel getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(WorkFlowModel workFlow) {
		this.workFlow = workFlow;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public Long getCallingInterval() {
		return callingInterval;
	}

	public void setCallingInterval(Long callingInterval) {
		this.callingInterval = callingInterval;
	}

	public String getWarningMessage() {
		return warningMessage;
	}

	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}

	public List<ConfirmationModel> getConfirmModel() {
		return confirmModel;
	}

	public void setConfirmModel(List<ConfirmationModel> confirmModel) {
		this.confirmModel = confirmModel;
	}

	public List<Confirm> getConfirm() {
		return confirm;
	}

	public void setConfirm(List<Confirm> confirm) {
		this.confirm = confirm;
	}

	public List<ActionExtnModel> getActionExtn() {
		return actionExtn;
	}

	public void setActionExtn(List<ActionExtnModel> actionExtn) {
		this.actionExtn = actionExtn;
	}

	public String getResponsePath() {
		return responsePath;
	}

	public void setResponsePath(String responsePath) {
		this.responsePath = responsePath;
	}



}
