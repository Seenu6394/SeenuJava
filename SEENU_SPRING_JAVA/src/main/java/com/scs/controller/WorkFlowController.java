package com.scs.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.scs.entity.model.WorkFlow;
import com.scs.entity.model.WorkflowSequence;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scs.entity.model.Intent;
import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;
import com.scs.model.WorkflowMetadataModel;
import com.scs.model.WorkflowResponseModel;
import com.scs.service.WorkFlowDbServices;
import com.scs.service.impl.WorkFlowDbServicesImpl;
import com.scs.service.IntentDbServices;
import com.scs.util.ApiConstants;
import com.scs.util.ErrorConstants;
import com.scs.util.Utility;

@RestController
@RequestMapping(ApiConstants.API)

public class WorkFlowController {

	private static final Logger logger = Logger.getLogger(WorkFlowController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private IntentDbServices intentDbService;

	@Autowired
	private WorkFlowDbServicesImpl workFlowDbService;

	@Autowired
	private WorkFlowDbServices flowChartDbServices;
	
	private static final String  RESPONSE = "response";

	@PostMapping(value = ApiConstants.WORKFLOW, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object createFlowChart(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		Object flowChart = null;

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			if (baseModel.getWorkFlow().getIntentId() != null) {
				Intent intent = (Intent) intentDbService.getIntentById(baseModel,
						baseModel.getWorkFlow().getIntentId().toString());
				if (intent != null && intent.getWorkFlow() != null) {
					throw new ApiException("WORKFLOW_MAPPED", "This intent is already mapped with another workFlow");
				}
			}
			flowChart = flowChartDbServices.createWorkFlow(baseModel);
		} catch (ApiException ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return flowChart;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = ApiConstants.GET_WORK_FLOW_DETAILS, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getIntent(BaseRequestModel baseModel) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			List<WorkFlow> flowCharts;
			flowCharts = (List<WorkFlow>) flowChartDbServices.getWorkFlowDetails(baseModel);

			responseObject.put(ApiConstants.API_RESPONSE, flowCharts);

		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@DeleteMapping(value = ApiConstants.WORKFLOW, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object delete(@RequestParam("id") String id, BaseRequestModel baseModel) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			flowChartDbServices.deleteFlowChart(baseModel, id);
			responseObject.put(ApiConstants.API_RESPONSE, ApiConstants.SUCCESS);
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@GetMapping(value = ApiConstants.WORKFLOW, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getIntentById(@RequestParam("id") String id, BaseRequestModel baseModel) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();
		String metadata = null;
		WorkflowMetadataModel workflowjson = null;
	
		WorkFlow flowChart = null;
		WorkflowResponseModel workflowResponse = new WorkflowResponseModel();

		try {

			flowChart = (WorkFlow) flowChartDbServices.getworkFlowById(baseModel, id);

			if (flowChart != null) {
				metadata = flowChart.getMetaData();

				ObjectMapper mapper = new ObjectMapper();

				WorkflowMetadataModel workflowMetadata = mapper.readValue(metadata, WorkflowMetadataModel.class);
				workflowjson = (WorkflowMetadataModel) workFlowDbService.getMetadataValues(workflowMetadata);

				String finalobj = mapper.writeValueAsString(workflowjson);

				finalobj = finalobj.replace("\\", "");
				if (workflowjson.getNodeDataArray() != null) {

				 workflowjson.getNodeDataArray().get(0).getIntent().getId();
				}

				workflowResponse.setId(flowChart.getId());
				workflowResponse.setMetaData(workflowjson);
				workflowResponse.setName(flowChart.getName());

				flowChart.setId(flowChart.getId());
				flowChart.setMetaData(finalobj);
				flowChart.setName(flowChart.getName());
			}

			responseObject.put(ApiConstants.API_RESPONSE, workflowResponse);

		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}

		return responseObject;
	}


	@PutMapping(value = ApiConstants.WORKFLOW, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object update(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			flowChartDbServices.updateWorkFlow(baseModel);
			responseObject.put(RESPONSE, ApiConstants.SUCCESS);

		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@PostMapping(value = ApiConstants.WORKFLOW_SEQUENCE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object createWorkFlowSequence(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		Object flowChart = null;

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			flowChart = flowChartDbServices.createWorkFlowSequence(baseModel);
		} catch (ApiException ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return flowChart;
	}

	@PutMapping(value = ApiConstants.WORKFLOW_SEQUENCE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object updateWorkflowSequence(@RequestBody @Valid BaseRequestModel baseModel, BindingResult bindingResult,
			HttpSession session) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			if (bindingResult.hasErrors()) {
				logger.debug(ApiConstants.BINDING_ERRORS);
				throw new ApiException(ErrorConstants.INVALIDDATA, Utility.getFirstErrorInformation(bindingResult));
			}

			flowChartDbServices.updateWorkFlowSequence(baseModel);
			responseObject.put(RESPONSE, ApiConstants.SUCCESS);

		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@PutMapping(value = ApiConstants.UPDATE_TERMINAL_TYPE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object updatePrimaryDestinationKey(@RequestParam("id") Long id, @RequestParam("flag") String flag) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();
		

		try {

			flowChartDbServices.updatePrimaryDestinationKey(id, flag);
			responseObject.put(RESPONSE, ApiConstants.SUCCESS);

		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}
	
	@PutMapping(value = ApiConstants.UPDATE_LINK, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object updateLink(@RequestParam("key") String workflowSequenceKey, @RequestParam("intentId") Long intentId, @RequestParam("flag") String flag) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();
		

		try {

			flowChartDbServices.updateLink(workflowSequenceKey, intentId, flag);
			responseObject.put(RESPONSE, ApiConstants.SUCCESS);

		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}
	

	@DeleteMapping(value = ApiConstants.WORKFLOW_SEQUENCE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object deleteWorkFlowSequence(@RequestParam("id") String id, BaseRequestModel baseModel)
			throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			flowChartDbServices.deleteWorkflowSequence(baseModel, id);
			responseObject.put(ApiConstants.API_RESPONSE, ApiConstants.SUCCESS);
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@DeleteMapping(value = ApiConstants.DELETE_INTENT_MAPPING_BY_INT_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object deleteIntentMapping(@RequestParam("intentId") Long intentId,
			@RequestParam("entryExpression") String entryExpression,
			@RequestParam("workflowSequenceKey") String workflowSequenceKey) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			flowChartDbServices.updateWorkflowSequenceAndDeleteIntentMapping(intentId, entryExpression,
					workflowSequenceKey);
			responseObject.put(ApiConstants.API_RESPONSE, ApiConstants.SUCCESS);
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@DeleteMapping(value = ApiConstants.DELETE_WORKFLOW_SEQUENCE_BY_INT_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object deleteWorkFlowSequenceByIntentId(@RequestParam("id") String id, BaseRequestModel baseModel)
			throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			flowChartDbServices.deleteWorkflowSequenceByIntentId(baseModel, id);
			responseObject.put(ApiConstants.API_RESPONSE, ApiConstants.SUCCESS);
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = ApiConstants.WORKFLOW_SEQUENCE_BY_INT_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getWorkFlowSequenceByIntentId(@RequestParam("id") Long id, BaseRequestModel baseModel)
			throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			List<WorkflowSequence> workflowSequencelst;
			workflowSequencelst = (List<WorkflowSequence>) flowChartDbServices.getWorkflowSequenceByIntentId(id);

			responseObject.put(ApiConstants.API_RESPONSE, workflowSequencelst);

		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = ApiConstants.WORKFLOW_SEQUENCE_BY_WF_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getWorkFlowSequenceByWorkflowId(@RequestParam("id") Long id, BaseRequestModel baseModel)
			throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			List<WorkflowSequence> workflowSequencelst;
			workflowSequencelst = (List<WorkflowSequence>) flowChartDbServices.getWorkflowSequenceByWorkflowId(id);

			responseObject.put(ApiConstants.API_RESPONSE, workflowSequencelst);

		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

	@GetMapping(value = ApiConstants.WORKFLOW_SEQUENCE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getWorkFlowSequenceById(@RequestParam("id") Long id, BaseRequestModel baseModel) throws ApiException {

		HashMap<String, Object> responseObject = new HashMap<>();

		try {

			WorkflowSequence workflowSequence;
			workflowSequence = (WorkflowSequence) flowChartDbServices.getWorkflowSequenceById(id);

			responseObject.put(ApiConstants.API_RESPONSE, workflowSequence);

		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return responseObject;
	}

}
