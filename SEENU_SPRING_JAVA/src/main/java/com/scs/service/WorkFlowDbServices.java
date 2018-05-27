package com.scs.service;

import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;
import com.scs.model.NodeDataArray;
import com.scs.model.WorkflowMetadataModel;

public interface WorkFlowDbServices {

	public Object getWorkFlowDetails(BaseRequestModel baseModel) throws ApiException;

	public Object createWorkFlow(BaseRequestModel baseModel) throws ApiException;

	public Object updateWorkFlow(BaseRequestModel baseModel) throws ApiException;

	public Object deleteFlowChart(BaseRequestModel baseModel, String id) throws ApiException;

	public Object getworkFlowById(BaseRequestModel baseModel, String id) throws ApiException;

	public Object getMetadataValues(WorkflowMetadataModel workflowMetadataModel) throws ApiException;

	public Object getIntentByName(String name) throws ApiException;

	public Object getActionByName(Long id) throws ApiException;

	public Object updateWorkFlowMetadata(String metadata, Long id ) throws ApiException;

	public Object getIntentByID(Long id) throws ApiException;

	public Object deleteWorkflowbyKuId(String id) throws ApiException;

	public Object getEntityByName(Long intentId, String name) throws ApiException;

	public Object getActionByEntityID(Long id) throws ApiException;

	public Object getActionByGI(String name, Long intentId) throws ApiException;

	public Object createWorkFlowSequence(BaseRequestModel baseModel) throws ApiException;

	public Object updateWorkFlowSequence(BaseRequestModel baseModel) throws ApiException;

	public Object deleteWorkflowSequence(BaseRequestModel baseModel, String id) throws ApiException;

	public Object deleteWorkflowSequenceByIntentId(BaseRequestModel baseModel, String id) throws ApiException;

	public Object getWorkflowSequenceById(Long id) throws ApiException;

	public Object getWorkflowSequenceByIntentId(Long id) throws ApiException;

	public Object getWorkflowSequenceByWorkflowId(Long id) throws ApiException;

	public Object updateWorkflowSequenceAndDeleteIntentMapping(Long intentId, String entryExpression, String workflowSequenceKey)
			throws ApiException;

	public Object updatePrimaryDestinationKey(Long workflowSequenceId, String flag) throws ApiException;

	public Object updateLink(String workflowSequenceKey, Long intentId, String flag) throws ApiException;

	
	

}
