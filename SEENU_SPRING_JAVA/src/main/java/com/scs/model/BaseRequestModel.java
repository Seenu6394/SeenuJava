package com.scs.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.scs.entity.model.Intent;
import com.scs.entity.model.Ku;
import com.scs.entity.model.WorkflowSequence;
import com.scs.validation.ValidationGroups.UserRequest;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseRequestModel {

	private String kuName;
	private String kuType;
	private IntentModel intent;
	private IntentExtnModel names;
	private Intent intents;
	private KeywordModel keyword;
	private List<KeywordModel> keywords = new ArrayList<>();
	private List<ProjectKeywordModel> projectKeywords = new ArrayList<>();
	private ProjectKeywordModel projectrKeyword;
	private List<ConfirmationModel> confirm = new ArrayList<>();
	private KuModel ku;
	private MessageModel message;
	private Ku kus;
	private EntityDetailsModel entity;
	private List<EntityQuestionModel> entityQuestion = new ArrayList<>();
	private RegExModel regularExpression;
	private ActionModel action;
	private ActionModel actions;
	private String errorNode;
	private List<ResponseModel> response;
	private List<ErrorResponseModel> errorResponses;
	private LanguageModel language;
	private MultipartFile file;
	private KuModel data;
	private WorkFlowModel workFlow;
	private WorkflowSequenceModel workflowSequence;
	
	@NotNull(groups = { UserRequest.class })
	private UserInfoModel user;

	public String getKuName() {
		return kuName;
	}

	public void setKuName(String kuName) {
		this.kuName = kuName;
	}

	public String getKuType() {
		return kuType;
	}

	public void setKuType(String kuType) {
		this.kuType = kuType;
	}

	public IntentModel getIntent() {
		return intent;
	}

	public void setIntent(IntentModel intent) {
		this.intent = intent;
	}

	public ProjectKeywordModel getProjectrKeyword() {
		return projectrKeyword;
	}

	public void setProjectrKeyword(ProjectKeywordModel projectrKeyword) {
		this.projectrKeyword = projectrKeyword;
	}

	public MessageModel getMessage() {
		return message;
	}

	public void setMessage(MessageModel message) {
		this.message = message;
	}

	public IntentExtnModel getNames() {
		return names;
	}

	public void setNames(IntentExtnModel names) {
		this.names = names;
	}

	public KeywordModel getKeyword() {
		return keyword;
	}

	public List<ConfirmationModel> getConfirm() {
		return confirm;
	}

	public void setConfirm(List<ConfirmationModel> confirm) {
		this.confirm = confirm;
	}

	public Intent getIntents() {
		return intents;
	}

	public void setIntents(Intent intents) {
		this.intents = intents;
	}

	public void setKeyword(KeywordModel keyword) {
		this.keyword = keyword;
	}

	public KuModel getKu() {
		return ku;
	}

	public void setKu(KuModel ku) {
		this.ku = ku;
	}

	public Ku getKus() {
		return kus;
	}

	public void setKus(Ku kus) {
		this.kus = kus;
	}

	public List<KeywordModel> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<KeywordModel> keywords) {
		this.keywords = keywords;
	}

	public List<EntityQuestionModel> getEntityQuestion() {
		return entityQuestion;
	}

	public void setEntityQuestion(List<EntityQuestionModel> entityQuestion) {
		this.entityQuestion = entityQuestion;
	}

	public LanguageModel getLanguage() {
		return language;
	}

	public WorkflowSequenceModel getWorkflowSequence() {
		return workflowSequence;
	}

	public void setWorkflowSequence(WorkflowSequenceModel workflowSequence) {
		this.workflowSequence = workflowSequence;
	}

	public void setLanguage(LanguageModel language) {
		this.language = language;
	}

	public RegExModel getRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(RegExModel regularExpression) {
		this.regularExpression = regularExpression;
	}

	public ActionModel getAction() {
		return action;
	}

	public void setAction(ActionModel action) {
		this.action = action;
	}

	public ActionModel getActions() {
		return actions;
	}

	public void setActions(ActionModel actions) {
		this.actions = actions;
	}

	public List<ProjectKeywordModel> getProjectKeywords() {
		return projectKeywords;
	}

	public void setProjectKeywords(List<ProjectKeywordModel> projectKeywords) {
		this.projectKeywords = projectKeywords;
	}

	public List<ResponseModel> getResponse() {
		return response;
	}

	public void setResponse(List<ResponseModel> response) {
		this.response = response;
	}

	public List<ErrorResponseModel> getErrorResponses() {
		return errorResponses;
	}

	public void setErrorResponses(List<ErrorResponseModel> errorResponses) {
		this.errorResponses = errorResponses;
	}

	

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public KuModel getData() {
		return data;
	}

	public void setData(KuModel data) {
		this.data = data;
	}

	public EntityDetailsModel getEntity() {
		return entity;
	}

	public void setEntity(EntityDetailsModel entity) {
		this.entity = entity;
	}

	public String getErrorNode() {
		return errorNode;
	}

	public void setErrorNode(String errorNode) {
		this.errorNode = errorNode;
	}

	public WorkFlowModel getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(WorkFlowModel workFlow) {
		this.workFlow = workFlow;
	}

	public UserInfoModel getUser() {
		return user;
	}

	public void setUser(UserInfoModel user) {
		this.user = user;
	}

}