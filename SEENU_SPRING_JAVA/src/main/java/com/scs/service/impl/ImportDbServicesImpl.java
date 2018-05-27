package com.scs.service.impl;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scs.entity.model.Action;

import com.scs.entity.model.ActionExtn;

import com.scs.entity.model.Confirm;
import com.scs.entity.model.EntityDetails;
import com.scs.entity.model.EntityQuestion;
import com.scs.entity.model.EntityRegex;
import com.scs.entity.model.ErrorResponse;

import com.scs.entity.model.Intent;

import com.scs.entity.model.IntentExtn;
import com.scs.entity.model.Keyword;
import com.scs.entity.model.Ku;
import com.scs.entity.model.Languages;

import com.scs.entity.model.Message;
import com.scs.entity.model.ProjectKeyword;
import com.scs.entity.model.RegEx;
import com.scs.entity.model.RegexExtn;
import com.scs.entity.model.Response;
import com.scs.entity.model.UserInfo;
import com.scs.entity.model.WorkFlow;
import com.scs.entity.model.WorkflowSequence;
import com.scs.exception.ApiException;
import com.scs.model.ActionExtnModel;
import com.scs.model.ActionModel;
import com.scs.model.BCSettingsModel;
import com.scs.model.BaseRequestModel;

import com.scs.model.EntityModel;

import com.scs.model.IntentModel;
import com.scs.model.MessageModel;
import com.scs.model.NodeDataArray;
import com.scs.model.RegExModel;
import com.scs.model.RegexExtnModel;
import com.scs.model.SettingsModel;
import com.scs.model.WorkflowMetadataModel;
import com.scs.service.EntityDbServices;
import com.scs.service.ImportDbServices;
import com.scs.service.IntentDbServices;
import com.scs.service.KuDbServices;
import com.scs.service.ReDbServices;
import com.scs.util.ApiConstants;
import com.scs.util.ErrorConstants;
import com.scs.util.Utility;

@Service("importDbService")

public class ImportDbServicesImpl implements ImportDbServices {

	private static final Logger logger = Logger.getLogger(ImportDbServicesImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MessageSource messageSource;

	
	@Autowired
	private ReDbServices reDbService;

	@Override
	@SuppressWarnings("unchecked")
	public Object importNewKu(Ku ku) throws ApiException {


		Transaction tx = null;
		String response = ApiConstants.SUCCESS;
		List<Intent> intentLst = new ArrayList<>();
		List<RegEx> regularExpressionLst = null;
		try (Session session = sessionFactory.openSession()) {
			
			tx = session.beginTransaction();

			List<RegExModel> listRegex = (List<RegExModel>) importSetRegexModel(ku);

			List<EntityModel> listEntity = (List<EntityModel>) importSetEntityModel(ku);

			List<ActionModel> listAction = (List<ActionModel>) importSetActionModel(ku);

			List<MessageModel> listMessage = (List<MessageModel>) importSetMessageModel(ku);

			importKu(session, ku);

			intentLst = (List<Intent>) processFlowchart(ku);

			regularExpressionLst = (List<RegEx>) importRegex(session, ku);

			importIntents(session, ku, intentLst, regularExpressionLst, listRegex, listEntity, listAction, listMessage);

			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ KuDbServicesImpl.createKU END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			response = Utility.checkUniqueConstraint(ex);
			logger.error("+++++ KuDbServicesImpl.createKU END SERVICE WITHEXCEPTION +++++");
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		} 

		return response;
	}

	private void importKu(Session session, Ku ku) {

		ku.setName(ku.getName());
		ku.setActiveInd(ku.getActiveInd());
		ku.setSpamEnable(ku.getSpamEnable());
		ku.setIsRankable(ku.getIsRankable());
		session.save(ku);

	}

	private Object importSetRegexModel(Ku ku) {

		List<RegEx> listRegex = new ArrayList<>(ku.getRegex());

		List<RegExModel> regexLst = new ArrayList<>();
		for (RegEx regex : listRegex) {
			RegExModel regexModel = new RegExModel();
			regexModel.setId(regex.getId());
			regexModel.setExpression(regex.getExpression());
			regexModel.setRegexname(regex.getRegexname());
			regexModel.setErrorCode(regex.getErrorCode());
			regexLst.add(regexModel);

		}
		return regexLst;
	}

	private Object importSetEntityModel(Ku ku) {

		List<EntityDetails> listEntity = ku.getEntities();

		List<EntityModel> entityLst = new ArrayList<>();
		for (EntityDetails entity : listEntity) {
			EntityModel entityModel = new EntityModel();
			entityModel.setId(entity.getId());
			entityModel.setName(entity.getName());
			entityModel.setIntentId(entity.getIntentId());
			entityLst.add(entityModel);

		}
		return entityLst;
	}

	private Object importSetActionModel(Ku ku) {

		List<ActionModel> actionModelLst = new ArrayList<>();
		for (Intent intentlst : ku.getIntents()) {
			List<Action> listAction = intentlst.getAction();
			for (Action actionlst : listAction) {
				ActionModel actModel = new ActionModel();
				actModel.setId(actionlst.getId());
				actModel.setName(actionlst.getName());
				actModel.setIntentId(actionlst.getIntent().getId());
				actionModelLst.add(actModel);
			}

		}

		return actionModelLst;
	}

	private Object importSetMessageModel(Ku ku) {

		List<MessageModel> messageModelLst = new ArrayList<>();
		for (Intent intentlst : ku.getIntents()) {
			List<Message> listMessage = intentlst.getMessage();
			for (Message messagelst : listMessage) {
				MessageModel messageModel = new MessageModel();
				messageModel.setId(messagelst.getId());
				messageModel.setMessageCode(messagelst.getMessageCode());
				IntentModel intent = new IntentModel();
				intent.setId(intentlst.getId());
				messageModel.setIntent(intent);
				messageModelLst.add(messageModel);
			}

		}

		return messageModelLst;
	}

	public static List<Intent> copyIntentList(List<Intent> list) {
		List<Intent> copylst = new ArrayList<>(list.size());
		for (Intent item : list) {
			copylst.add(new Intent(item));
		}
		return copylst;
	}

	private void importIntents(Session session, Ku ku, List<Intent> intentLst, List<RegEx> regularExpressionLst,
			List<RegExModel> listRegex, List<EntityModel> listEntity, List<ActionModel> listAction,
			List<MessageModel> listMessage) throws ApiException{

		try {
			Instant instant = Instant.now();

			String intentName = "";
			List<Intent> oldintentLst = copyIntentList(intentLst);
			logger.info("intentLst length: " + intentLst.size());
			for (Intent intent : intentLst) {

				for (IntentExtn intentExtn : intent.getNames()) {
					if ("en".equals(intentExtn.getLocaleCode())) {
						intentName = intentExtn.getName();
					}
				}

				intent.setName(intentName);
				intent.setKuId(ku.getId());
				intent.setAction(intent.getAction());
				intent.setDate(instant.toString());
				intent.setFlag(null);
				intent.setGlobalIdentifier(UUID.randomUUID().toString());
				session.save(intent);

				Intent oldIntentObj = getOldIntentObj(oldintentLst, intentName);
				logger.info("oldIntentObj.getId(): " + oldIntentObj.getId());
				List<MessageModel> intentMessages = getIntentMessages(oldIntentObj, listMessage);
				List<EntityModel> intentEntities = getIntentEntities(oldIntentObj, listEntity);
				List<ActionModel> intentActions = getIntentActions(oldIntentObj, listAction);

				WorkFlow workflow = null;
				workflow = (WorkFlow) importFlowchart(session, intent, intentName);
				importIntentExtn(session, intent);
				importKeywords(session, intent, intentName);
				importEntities(session, ku, regularExpressionLst, intent, oldIntentObj, workflow, listRegex,
						intentEntities);

				List<Action> latestActionDetails = importAction(session, intent, ku, intentEntities, workflow,
						intentActions);
				importMessage(session, intent, workflow, ku, intentMessages, latestActionDetails, intentEntities,
						intentActions);
				if (workflow != null) {
					importWorkFlowSequence(session, intent, workflow, ku, latestActionDetails, intentEntities,
							intentActions);
				}
			}
		} catch (HibernateException ex) {
			logger.error("+++++ importDbServicesImpl.createImport END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error("+++++ importDbServicesImpl.createImport END SERVICE WITHEXCEPTION +++++");
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
	}

	private List<MessageModel> getIntentMessages(Intent oldintentObj, List<MessageModel> messageObjs) {

		List<MessageModel> intentMessages = new ArrayList<>();
		for (MessageModel messageOBJ : messageObjs) {
			if (oldintentObj.getId().equals(messageOBJ.getIntent().getId())) {
				intentMessages.add(messageOBJ);
			}
		}
		return intentMessages;
	}

	private List<EntityModel> getIntentEntities(Intent oldintentObj, List<EntityModel> entityObjs) {

		List<EntityModel> intentEntities = new ArrayList<>();
		for (EntityModel entityOBJ : entityObjs) {
			if (oldintentObj.getId().equals(entityOBJ.getIntentId())) {
				intentEntities.add(entityOBJ);
			}
		}
		return intentEntities;
	}

	private List<ActionModel> getIntentActions(Intent oldintentObj, List<ActionModel> actionObjs) {

		List<ActionModel> intentActions = new ArrayList<>();
		for (ActionModel actionOBJ : actionObjs) {
			if (oldintentObj.getId().equals(actionOBJ.getIntentId())) {
				intentActions.add(actionOBJ);
			}
		}
		return intentActions;
	}

	private Intent getOldIntentObj(List<Intent> oldintentLst, String intentName) {

		Intent intentObj = new Intent();
		String oldIntentName = "";
		for (Intent intent : oldintentLst) {

			for (IntentExtn intentExtn : intent.getNames()) {
				if ("en".equals(intentExtn.getLocaleCode())) {
					oldIntentName = intentExtn.getName();
				}
			}

			if (oldIntentName.equals(intentName)) {
				intentObj = intent;
			}
		}
		return intentObj;
	}

	private void importIntentExtn(Session session, Intent intent) {

		if (intent.getNames() != null) {
			for (IntentExtn intentExtn : intent.getNames()) {
				intentExtn.setIntent(intent);
				intentExtn.setLocaleCode(intentExtn.getLocaleCode());
				intentExtn.setName(intentExtn.getName());
				session.save(intentExtn);
			}
		}

	}

	private void importKeywords(Session session, Intent intent, String intentName) {
		Instant instant = Instant.now();
		Boolean intName = false;

		if (intent.getKeywords() != null) {
			for (Keyword keyword : intent.getKeywords()) {

				if ((keyword.getKeywordField()).equalsIgnoreCase(intentName)) {
					intName = true;

				}

				if (keyword.getKeywordField() != null) {
					keyword.setIntent(intent);
					keyword.setKeywordField(keyword.getKeywordField().toLowerCase());
					keyword.setPolarity(keyword.getPolarity());
					keyword.setLocaleCode(keyword.getLocaleCode());
					keyword.setDate(instant.toString());
					session.save(keyword);
				}
			}

			if (!intName) {

				for (IntentExtn intentExtn : intent.getNames()) {

					if (intentExtn.getName() != null) {
						Keyword keyword = new Keyword();
						keyword.setIntent(intent);
						keyword.setKeywordField(intentExtn.getName().toLowerCase());
						keyword.setPolarity("P");
						keyword.setLocaleCode(intentExtn.getLocaleCode());
						keyword.setDate(instant.toString());
						session.save(keyword);
					}
				}
			}
		}
	}

	private Object importRegex(Session session, Ku ku) throws ApiException {

		BaseRequestModel baseModel = null;
		List<RegEx> regularExpressionLst = new ArrayList<>();
		List<RegEx> reLst = (List<RegEx>) reDbService.getReDetails(baseModel);

		for (RegEx regex : ku.getRegex()) {
			boolean ifRegexNotCreated = true;

			for (RegEx regexLs : reLst) {

				if (regexLs.getExpression().equals(regex.getExpression())) {
					ifRegexNotCreated = false;
					break;
				}
			}
			if (ifRegexNotCreated) {
				regex.setRegexname(regex.getRegexname());
				regex.setExpression(regex.getExpression());
				regex.setErrorCode(regex.getErrorCode());
				session.save(regex);

				for (RegexExtn regexextn : regex.getRegexes()) {

					RegexExtn regexExtn = new RegexExtn();
					regexExtn.setRegex(regex);
					regexExtn.setErrorMessage(regexextn.getErrorMessage());
					regexExtn.setLocaleCode(regexextn.getLocaleCode());
					session.save(regexExtn);

				}
			}

			regularExpressionLst.add(regex);
		}

		return regularExpressionLst;
	}

	private Object importEntities(Session session, Ku ku, List<RegEx> regularExpressionLst, Intent intent,
			Intent oldIntentObj, WorkFlow workflow, List<RegExModel> listRegex, List<EntityModel> listEntity)
			 throws ApiException {
		Instant instant = Instant.now();
		List<EntityDetails> entityDetails = new ArrayList<>();
		List<EntityQuestion> entityQuestionLst = null;
		Action action = null;

		for (EntityDetails entity : ku.getEntities()) {
			if (entity.getIntentId().equals(oldIntentObj.getId())) {
				entity.setEntityType(entity.getEntityType());
				entity.setKuId(ku.getId());
				entity.setGlobalIdentifier(UUID.randomUUID().toString());
				entity.setIntentId(intent.getId());
				entity.setEntityRegex(entity.getEntityRegex());
				entity.setDate(instant.toString());
				entity.setFlag(null);
				entity.setRequired(entity.getRequired());
				entity.setExample(entity.getExample());
				entity.setDataType(entity.getDataType());

				session.save(entity);

				entityQuestionLst = (List<EntityQuestion>) importQuestions(session, entity);
				action = (Action) importEntityAction(session, entity);

				entity.setAction(action);
				entity.setQuestions(entityQuestionLst);

				entityDetails.add(entity);
				importEntityRegex(session, regularExpressionLst, entity, listRegex);
				importEntityWorkFlowSequence(session, intent, entity, workflow, listEntity, ku);
			}

		}
		return entityDetails;
	}

	private Object importQuestions(Session session, EntityDetails entity) {
		List<EntityQuestion> entityQuestionLst = new ArrayList<>();
		if (entity.getQuestions() != null) {
			for (EntityQuestion entityQuestion : entity.getQuestions()) {
				entityQuestion.setEntity(entity);
				entityQuestion.setQuestion(entityQuestion.getQuestion());
				entityQuestion.setExample(entityQuestion.getExample());
				entityQuestion.setLocaleCode(entityQuestion.getLocaleCode());
				entityQuestion.setTitle(entityQuestion.getTitle());
				entityQuestion.setSubTitle(entityQuestion.getSubTitle());
				entityQuestion.setButtonText(entityQuestion.getButtonText());
				entityQuestion.setImageUrl(entityQuestion.getImageUrl());

				session.save(entityQuestion);
				entityQuestionLst.add(entityQuestion);
			}
		}
		return entityQuestionLst;
	}

	private void importEntityRegex(Session session, List<RegEx> regularExpressionLst, EntityDetails entity,
			List<RegExModel> regexlst) throws ApiException {

		try {
			for (EntityRegex entRegex : entity.getEntityRegex()) {

				for (RegExModel regex : regexlst) {

					if (entRegex.getRegexId().equals(regex.getId())) {

						List<RegEx> regexDetails = (List<RegEx>) getRegexByExp(session, regex.getExpression());

						if (!regexDetails.isEmpty()) {

							EntityRegex entityRegex = new EntityRegex();
							entityRegex.setEntityId(entity.getId());
							entityRegex.setRegexId(regexDetails.get(0).getId());
							session.save(entityRegex);

						} else {

							for (RegEx regularExp : regularExpressionLst) {

								if (regularExp.getExpression().equals(regex.getExpression())) {

									EntityRegex entityRegex = new EntityRegex();
									entityRegex.setEntityId(entity.getId());
									entityRegex.setRegexId(regularExp.getId());
									session.save(entityRegex);
								}

							}

						}
					}

				}

			}
		} catch (HibernateException ex) {
			logger.error("+++++ getRegexByExp END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error("+++++ getRegexByExp  END SERVICE WITHEXCEPTION +++++");
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}

	}

	private Object getRegexByExp(Session session, String expression) throws ApiException {
		List<RegEx> regexDetails = new ArrayList<>();
		try {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<RegEx> select = builder.createQuery(RegEx.class);
			Root<RegEx> root = select.from(RegEx.class);
			ParameterExpression<String> rgxbuilder = builder.parameter(String.class);
			Predicate regexPredicate = builder.equal(root.get("expression"), rgxbuilder);
			Predicate and1 = builder.and(regexPredicate);
			select.where(and1);

			regexDetails = session.createQuery(select).setParameter(rgxbuilder, expression).getResultList();

		} catch (HibernateException ex) {
			logger.error("+++++ getRegexByExp END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		} catch (Exception ex) {
			logger.error(Utility.getExceptionMessage(ex));
			logger.error("+++++ getRegexByExp  END SERVICE WITHEXCEPTION +++++");
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		}
		return regexDetails;
	}

	private Object importEntityAction(Session session, EntityDetails entityDetails) {
		Action action = entityDetails.getAction();
		List<ActionExtn> actnExtnLst = new ArrayList<>();
		List<ErrorResponse> errorResponseLst = null;

		if (action.getId() != null) {

			action.setEntity(entityDetails);

			action.setGlobalIdentifier(UUID.randomUUID().toString());
			action.setConfirm(action.getConfirm());
			action.setName(action.getName());
			action.setKuId(entityDetails.getKuId());
			session.save(action);

			for (ActionExtn actionExtnLst : action.getActionExtn()) {

				ActionExtn actionExtn = new ActionExtn();

				actionExtn.setRequestBody(actionExtnLst.getRequestBody());
				actionExtn.setCallMethod(actionExtnLst.getCallMethod());
				actionExtn.setErrorCode(actionExtnLst.getErrorCode());
				actionExtn.setLocaleCode(actionExtnLst.getLocaleCode());
				actionExtn.setResponsePath(actionExtnLst.getResponsePath());
				actionExtn.setSuccessCode(actionExtnLst.getSuccessCode());
				actionExtn.setAction(action);
				actionExtn.setUrl(actionExtnLst.getUrl());
				actnExtnLst.add(actionExtn);

				session.save(actionExtn);

			}

			action.setActionExtn(actnExtnLst);

			errorResponseLst = (List<ErrorResponse>) importErrorResponses(session, action);

			action.setErrorResponses(errorResponseLst);
		}
		return action;
	}

	private Object importErrorResponses(Session session, Action action) {

		List<ErrorResponse> errorResponseLst = new ArrayList<>();
		if (action.getErrorResponses() != null) {
			for (ErrorResponse errorResponse : action.getErrorResponses()) {
				errorResponse.setAction(action);
				errorResponse.setErrorCode(errorResponse.getErrorCode());
				errorResponse.setErrorResponse(errorResponse.getErrorResponse());
				errorResponse.setLocaleCode(errorResponse.getLocaleCode());
				errorResponse.setKuId(action.getKuId());
				session.save(errorResponse);
				errorResponseLst.add(errorResponse);
			}
		}
		return errorResponseLst;

	}

	private Object processFlowchart(Ku ku) throws IOException {

		WorkFlow workflow = null;
		List<NodeDataArray> nodeDataArrayList = null;
		List<Intent> intentLst = new ArrayList<>();

		for (Intent intent : ku.getIntents()) {
			nodeDataArrayList = new ArrayList<>();
			workflow = intent.getWorkFlow();
			String metadata = workflow.getMetaData();

			ObjectMapper mapper = new ObjectMapper();

			WorkflowMetadataModel workflowMetadata = mapper.readValue(metadata, WorkflowMetadataModel.class);

			for (NodeDataArray nodeData : workflowMetadata.getNodeDataArray()) {

				if (nodeData.getModelid() == 5) {
					nodeData.getIntent().setName(intent.getName());
					nodeData.setText(intent.getName());

				}

				if (nodeData.getModelid() == 2) {

					for (EntityDetails entity : ku.getEntities()) {

						if (entity.getId().equals(nodeData.getEntity().getId())) {
							nodeData.getEntity().setName(entity.getName());

							nodeData.setText(entity.getName());

						}
					}

				}

				nodeDataArrayList.add(nodeData);

				workflowMetadata.setNodeDataArray(nodeDataArrayList);
				workflowMetadata.setClassvar(workflowMetadata.getClassvar());
				workflowMetadata.setLinkDataArray(workflowMetadata.getLinkDataArray());
				workflowMetadata.setLinkFromPortIdProperty(workflowMetadata.getLinkFromPortIdProperty());
				workflowMetadata.setLinkToPortIdProperty(workflowMetadata.getLinkToPortIdProperty());

			}

			String jsonInString = mapper.writeValueAsString(workflowMetadata);

			intent.getWorkFlow().setMetaData(jsonInString);

			intentLst.add(intent);

		}

		return intentLst;

	}

	private Map<String, String> rearrangingEntityIdColl(List<EntityModel> oldEntityDetails,
			List<EntityDetails> newEntityDetails) {

		Map<String, String> matchingEntityIds = new HashMap<>();
		for (EntityModel oldEntity : oldEntityDetails) {
			for (EntityDetails newEntity : newEntityDetails) {
				if (newEntity.getName().equals(oldEntity.getName())) {
					logger.info("Exchanging entity id " + newEntity.getId().toString() + " for "
							+ oldEntity.getId().toString());
					matchingEntityIds.put(oldEntity.getId().toString(), newEntity.getId().toString());
				}
			}
		}
		return matchingEntityIds;
	}

	private Map<String, String> rearrangingActionIdColl(List<ActionModel> oldActionDetails,
			List<Action> newActionDetails) {

		Map<String, String> matchingActionIds = new HashMap<>();
		for (ActionModel oldAction : oldActionDetails) {
			for (Action newAction : newActionDetails) {
				if (newAction.getName().equals(oldAction.getName())) {
					logger.info("Exchanging action id " + newAction.getId().toString() + " for "
							+ oldAction.getId().toString());
					matchingActionIds.put(oldAction.getId().toString(), newAction.getId().toString());
				}
			}
		}
		return matchingActionIds;
	}

	@SuppressWarnings("unchecked")
	private List<Action> importAction(Session session, Intent intent, Ku ku, List<EntityModel> entityDetails,
			WorkFlow workflow, List<ActionModel> listAction) throws ApiException {

		List<Action> actionLst = intent.getAction();
		try {

			List<ActionExtn> actnExtnLst = null;

			for (Action action : actionLst) {
				if (action.getName() != null) {

					action.setIntent(intent);
					action.setGlobalIdentifier(UUID.randomUUID().toString());
					action.setConfirm(action.getConfirm());
					action.setName(action.getName());
					action.setKuId(intent.getKuId());
					action.setDataType(action.getDataType());
					action.setCallingInterval(action.getCallingInterval());
					action.setWarningMessage(action.getWarningMessage());
					session.save(action);

					actnExtnLst = (List<ActionExtn>) importActionExtn(session, action);

					action.setActionExtn(actnExtnLst);
					importErrorResponses(session, action);
					importActionWorkFlowSequence(session, intent, action, workflow, ku, listAction);
				}
			}

			List<EntityDetails> newEntitiesForIntent = new ArrayList<>();
			for (EntityDetails newEntity : ku.getEntities()) {
				if (newEntity.getIntentId().equals(intent.getId())) {
					newEntitiesForIntent.add(newEntity);
				}
			}

			Map<String, String> entityIdsToRearrange = rearrangingEntityIdColl(entityDetails, newEntitiesForIntent);
			Map<String, String> actionIdsToRearrange = rearrangingActionIdColl(listAction, actionLst);

			// now actionLst contains new actionIds using this we can replace old ids in
			// request body and confirmation text
			for (Action action : actionLst) {

				if (action.getName() != null) {

					for (ActionExtn actionExtnRec : action.getActionExtn()) {

						String requestBody = actionExtnRec.getRequestBody();
						List<String> entityIdsInReqBody = Utility.fetchEntites(requestBody);
						logger.info("entityIdsInReqBody: " + entityIdsInReqBody);
						List<String> actionIdsInReqBody = Utility.fetchActions(requestBody);
						logger.info("actionIdsInReqBody: " + actionIdsInReqBody);

						for (String entityID : entityIdsInReqBody) {
							String newID = entityIdsToRearrange.get(entityID);
							requestBody = requestBody.replace("#%" + entityID + "%#", "#%" + newID + "%#");
						}

						for (String actionID : actionIdsInReqBody) {
							String newID = actionIdsToRearrange.get(actionID);
							requestBody = requestBody.replace("#@" + actionID + "@#", "#@" + newID + "@#");
						}

						String actionUrl = actionExtnRec.getUrl();
						List<String> entityIdsInActionUrl = Utility.fetchEntites(actionUrl);
						List<String> actionIdsInActionUrl = Utility.fetchActions(actionUrl);

						for (String entityID : entityIdsInActionUrl) {
							String newID = entityIdsToRearrange.get(entityID);
							actionUrl = actionUrl.replace("#%" + entityID + "%#", "#%" + newID + "%#");
						}

						for (String actionID : actionIdsInActionUrl) {
							String newID = actionIdsToRearrange.get(actionID);
							actionUrl = actionUrl.replace("#@" + actionID + "@#", "#@" + newID + "@#");
						}

						updateActionReqBody(session, requestBody, actionUrl, actionExtnRec);
					}
					importConfirmAction(session, action, entityIdsToRearrange, actionIdsToRearrange);
				}
			}

		} catch (HibernateException ex) {

			logger.error("+++++ importAction END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);

		} catch (Exception ex) {
			logger.error("+++++ importAction END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));

		}
		return actionLst;
	}

	private Object importActionExtn(Session session, Action action) {

		List<ActionExtn> actnExtnLst = new ArrayList<>();

		for (ActionExtn actionExtnLst : action.getActionExtn()) {

			ActionExtn actionExtn = new ActionExtn();
			actionExtn.setRequestBody(actionExtnLst.getRequestBody());
			actionExtn.setCallMethod(actionExtnLst.getCallMethod());
			actionExtn.setErrorCode(actionExtnLst.getErrorCode());
			actionExtn.setLocaleCode(actionExtnLst.getLocaleCode());
			actionExtn.setResponsePath(actionExtnLst.getResponsePath());
			actionExtn.setSuccessCode(actionExtnLst.getSuccessCode());
			actionExtn.setAction(action);
			actionExtn.setUrl(actionExtnLst.getUrl());
			session.save(actionExtn);
			actnExtnLst.add(actionExtn);

		}
		return actnExtnLst;
	}

	private void updateActionReqBody(Session session, String requestBody, String actionUrl, ActionExtn acionExtnRec) {
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<ActionExtn> update = builder.createCriteriaUpdate(ActionExtn.class);
			Root<ActionExtn> root = update.from(ActionExtn.class);
			update.set(root.get("requestBody"), requestBody);
			update.set(root.get("url"), actionUrl);
			update.where(builder.equal(root.get("id"), acionExtnRec.getId()));
			session.createQuery(update).executeUpdate();
		} catch (HibernateException ex) {

			logger.error("+++++ updateActionReqBody END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));

		} catch (Exception ex) {
			logger.error("+++++ updateActionReqBody END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));

		}

	}

	private void importConfirmAction(Session session, Action action, Map<String, String> entityIdsToRearrange,
			Map<String, String> actionIdsToRearrange) {

		if (action.getConfirm() != null) {
			List<Confirm> confirmLst = action.getConfirm();

			for (Confirm confirm : confirmLst) {

				confirm.setAction(action);
				confirm.setKuId(action.getKuId());
				confirm.setConfirmationOption(confirm.getConfirmationOption());
				confirm.setConfirmationType(confirm.getConfirmationType());
				confirm.setLocaleCode(confirm.getLocaleCode());
				confirm.setTerminationText(confirm.getTerminationText());
				confirm.setUnConfirmationOption(confirm.getUnConfirmationOption());

				String confirmationText = confirm.getText();
				List<String> entityIdsInConfrmTxt = Utility.fetchEntites(confirmationText);
				List<String> actionIdsInConfrmTxt = Utility.fetchActions(confirmationText);
				for (String entityID : entityIdsInConfrmTxt) {
					String newID = entityIdsToRearrange.get(entityID);
					confirmationText = confirmationText.replace("#%" + entityID + "%#", "#%" + newID + "%#");
				}
				for (String actionID : actionIdsInConfrmTxt) {
					String newID = actionIdsToRearrange.get(actionID);
					confirmationText = confirmationText.replace("#@" + actionID + "@#", "#@" + newID + "@#");
				}
				confirm.setText(confirmationText);
				session.save(confirm);
			}
		}

	}

	private Object importMetadata(Intent intent, String intentName) throws IOException {

		WorkFlow workflow = null;
		String jsonInString = null;
		List<NodeDataArray> nodeDataArrayList = null;

		nodeDataArrayList = new ArrayList<>();
		workflow = intent.getWorkFlow();
		String metadata = workflow.getMetaData();

		ObjectMapper mapper = new ObjectMapper();

		WorkflowMetadataModel workflowMetadata = mapper.readValue(metadata, WorkflowMetadataModel.class);

		for (NodeDataArray nodeData : workflowMetadata.getNodeDataArray()) {

			if (nodeData.getModelid() == 5) {
				nodeData.getIntent().setName(intentName);
				nodeData.setText(intentName);

			}

			nodeDataArrayList.add(nodeData);

			workflowMetadata.setNodeDataArray(nodeDataArrayList);
			workflowMetadata.setClassvar(workflowMetadata.getClassvar());
			workflowMetadata.setLinkDataArray(workflowMetadata.getLinkDataArray());
			workflowMetadata.setLinkFromPortIdProperty(workflowMetadata.getLinkFromPortIdProperty());
			workflowMetadata.setLinkToPortIdProperty(workflowMetadata.getLinkToPortIdProperty());

		}

		jsonInString = mapper.writeValueAsString(workflowMetadata);

		return jsonInString;

	}

	private Object importFlowchart(Session session, Intent intent, String intentName) {
		WorkFlow workflow = null;

		workflow = intent.getWorkFlow();
		String metadata = "";
		if (workflow != null) {

			try {
				workflow.setIntent(intent);
				workflow.setName(workflow.getName());
				metadata = (String) importMetadata(intent, intentName);
				workflow.setMetaData(metadata);
				workflow.setKuId(intent.getKuId());
				session.save(workflow);

			} catch (HibernateException ex) {

				logger.error("+++++ importFlowchart END SERVICE WITH Hibernate EXCEPTION +++++");
				logger.error(Utility.getExceptionMessage(ex));

			} catch (Exception ex) {
				logger.error("+++++ importFlowchart END SERVICE WITHEXCEPTION +++++");
				logger.error(Utility.getExceptionMessage(ex));

			}

		}
		return workflow;

	}

	private void importMessage(Session session, Intent intent, WorkFlow workflow, Ku ku, List<MessageModel> listMessage,
			List<Action> latestActionDetails, List<EntityModel> oldEntityDetails, List<ActionModel> listAction) {

		List<EntityDetails> newEntitiesForIntent = new ArrayList<>();
		for (EntityDetails newEntity : ku.getEntities()) {
			if (newEntity.getIntentId().equals(intent.getId())) {
				newEntitiesForIntent.add(newEntity);
			}
		}

		Map<String, String> entityIdsToRearrange = rearrangingEntityIdColl(oldEntityDetails, newEntitiesForIntent);
		Map<String, String> actionIdsToRearrange = rearrangingActionIdColl(listAction, latestActionDetails);

		if (intent.getMessage() != null) {

			for (Message messagelst : intent.getMessage()) {
				Message message = new Message();
				message.setIntent(intent);
				message.setMessageCode(messagelst.getMessageCode());
				Long oldMessageID = messagelst.getId();

				session.save(message);

				importMessageWorkFlowSequence(session, intent, message, oldMessageID, workflow, ku, listMessage);

				if (messagelst.getResponses() != null) {
					for (Response response : messagelst.getResponses()) {
						response.setIntent(intent);
						String msg = response.getResponseText();

						List<String> entityIdsInConfrmTxt = Utility.fetchEntites(msg);
						List<String> actionIdsInConfrmTxt = Utility.fetchActions(msg);
						for (String entityID : entityIdsInConfrmTxt) {
							String newID = entityIdsToRearrange.get(entityID);
							msg = msg.replace("#%" + entityID + "%#", "#%" + newID + "%#");
						}
						for (String actionID : actionIdsInConfrmTxt) {
							String newID = actionIdsToRearrange.get(actionID);
							msg = msg.replace("#@" + actionID + "@#", "#@" + newID + "@#");
						}

						response.setResponseText(msg);
						response.setGlobalIdentifier(UUID.randomUUID().toString());
						response.setKuId(intent.getKuId());
						response.setMessage(message);
						session.save(response);
					}
				}
			}
		}
	}

	private Object importEntityWorkFlowSequence(Session session, Intent intent, EntityDetails entity, WorkFlow workflow,
			List<EntityModel> listEntity, Ku ku) {
		WorkflowSequence workflowSequence = new WorkflowSequence();
		String workflowSequencekey = null;

		try {
			workflowSequence.setWorkflowId(workflow.getId());
			workflowSequence.setEntryType("ENTITY");
			workflowSequence.setEntryExpression(entity.getId().toString());
			workflowSequence.setPrimaryDestWorkflowId(workflow.getId());
			workflowSequence.setRequired("Y");
			workflowSequence.setIntent(intent);
			workflowSequence.setKuId(ku.getId());

			List<WorkflowSequence> workflowSequenceLst = intent.getWorkflowSequence();
			for (EntityModel entityLst : listEntity) {
				if (entityLst.getName().equalsIgnoreCase(entity.getName())) {
					for (WorkflowSequence workflowSequenceItem : workflowSequenceLst) {
						if (workflowSequenceItem.getEntryExpression().equals(entityLst.getId().toString())
								&& ("ENTITY".equals(workflowSequenceItem.getEntryType()))) {

							workflowSequence
									.setPrimaryDestSequenceKey(workflowSequenceItem.getPrimaryDestSequenceKey());
							workflowSequencekey = workflowSequenceItem.getWorkflowSequenceKey();
							workflowSequence.setTerminalType(workflowSequenceItem.getTerminalType());

						}
					}
				}
			}

			workflowSequence.setWorkflowSequenceKey(workflowSequencekey);
			session.save(workflowSequence);

		} catch (HibernateException ex) {

			logger.error("+++++ importEntityWorkFlowSequence END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));

		} catch (Exception ex) {
			logger.error("+++++ importEntityWorkFlowSequence END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));

		}

		return workflowSequence;

	}

	private Object importActionWorkFlowSequence(Session session, Intent intent, Action action, WorkFlow workflow, Ku ku,
			List<ActionModel> listAction) {
		WorkflowSequence workflowSequence = new WorkflowSequence();

		try {
			workflowSequence.setWorkflowId(workflow.getId());
			workflowSequence.setEntryType("ACTION");
			workflowSequence.setEntryExpression(action.getId().toString());
			workflowSequence.setPrimaryDestWorkflowId(workflow.getId());
			workflowSequence.setRequired("Y");
			workflowSequence.setIntent(intent);
			workflowSequence.setKuId(ku.getId());

			List<WorkflowSequence> workflowSequenceLst = intent.getWorkflowSequence();
			for (ActionModel oldactionitem : listAction) {
				if (oldactionitem.getName().equalsIgnoreCase(action.getName())) {
					for (WorkflowSequence workflowSequenceItem : workflowSequenceLst) {
						if (workflowSequenceItem.getEntryExpression().equals(oldactionitem.getId().toString())
								&& ("ACTION".equals(workflowSequenceItem.getEntryType()))) {

							workflowSequence
									.setPrimaryDestSequenceKey(workflowSequenceItem.getPrimaryDestSequenceKey());
							workflowSequence.setWorkflowSequenceKey(workflowSequenceItem.getWorkflowSequenceKey());
							workflowSequence.setTerminalType(workflowSequenceItem.getTerminalType());

						}
					}
				}
			}
			session.save(workflowSequence);

		} catch (HibernateException ex) {

			logger.error("+++++ importActionWorkFlowSequence END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));

		} catch (Exception ex) {
			logger.error("+++++ importActionWorkFlowSequence END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));

		}

		return workflowSequence;

	}

	private Object importMessageWorkFlowSequence(Session session, Intent intent, Message message, Long oldMessageID,
			WorkFlow workflow, Ku ku, List<MessageModel> listMessage) {
		WorkflowSequence workflowSequence = new WorkflowSequence();

		try {
			workflowSequence.setWorkflowId(workflow.getId());
			workflowSequence.setEntryType("MESSAGE");
			workflowSequence.setEntryExpression(message.getId().toString());
			workflowSequence.setPrimaryDestWorkflowId(workflow.getId());
			workflowSequence.setRequired("Y");
			workflowSequence.setIntent(intent);
			workflowSequence.setKuId(ku.getId());

			List<WorkflowSequence> workflowSequenceLst = intent.getWorkflowSequence();// list of all sequences
			for (MessageModel oldMessageitem : listMessage) {
				logger.info("oldMessageitem.getId(): " + oldMessageitem.getId());
				if (oldMessageitem.getId().equals(oldMessageID)) {
					for (WorkflowSequence workflowSequenceItem : workflowSequenceLst) {
						if (("MESSAGE".equals(workflowSequenceItem.getEntryType())) && (workflowSequenceItem
								.getEntryExpression().equals(oldMessageitem.getId().toString()))) {

							workflowSequence
									.setPrimaryDestSequenceKey(workflowSequenceItem.getPrimaryDestSequenceKey());
							workflowSequence.setWorkflowSequenceKey(workflowSequenceItem.getWorkflowSequenceKey());
							workflowSequence.setTerminalType(workflowSequenceItem.getTerminalType());

						}
					}
				}
			}

			session.save(workflowSequence);

		} catch (HibernateException ex) {

			logger.error("+++++ importMessageWorkFlowSequence END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));

		} catch (Exception ex) {
			logger.error("+++++ importMessageWorkFlowSequence END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));

		}

		return workflowSequence;

	}

	private void importWorkFlowSequence(Session session, Intent intent, WorkFlow workflow, Ku ku,
			List<Action> latestActionDetails, List<EntityModel> oldEntityDetails, List<ActionModel> listAction) {

		try {

			List<EntityDetails> newEntitiesForIntent = new ArrayList<>();
			for (EntityDetails newEntity : ku.getEntities()) {
				if (newEntity.getIntentId().equals(intent.getId())) {
					newEntitiesForIntent.add(newEntity);
				}
			}

			Map<String, String> entityIdsToRearrange = rearrangingEntityIdColl(oldEntityDetails, newEntitiesForIntent);
			Map<String, String> actionIdsToRearrange = rearrangingActionIdColl(listAction, latestActionDetails);

			logger.info("ku.getIntents() length: " + ku.getIntents().size());
			for (Intent intentlst : ku.getIntents()) {
				logger.info("intent id in diamond inside for loop: " + intentlst.getId());
				logger.info("intent id in diamond: " + intent.getId());

				if (intentlst.getId().equals(intent.getId())) {

					List<WorkflowSequence> workflowSequenceLst = intentlst.getWorkflowSequence();
					for (WorkflowSequence workflowSequences : workflowSequenceLst) {

						if ("DIAMOND".equals(workflowSequences.getEntryType())) {
							WorkflowSequence workflowSequence = new WorkflowSequence();
							workflowSequence.setWorkflowId(workflow.getId());
							workflowSequence.setEntryType("DIAMOND");

							String diamondExpression = workflowSequences.getEntryExpression();
							List<String> entityIdsInConfrmTxt = Utility.fetchEntites(diamondExpression);
							List<String> actionIdsInConfrmTxt = Utility.fetchActions(diamondExpression);
							for (String entityID : entityIdsInConfrmTxt) {
								String newID = entityIdsToRearrange.get(entityID);
								diamondExpression = diamondExpression.replace("#%" + entityID + "%#", "#%" + newID + "%#");
							}
							for (String actionID : actionIdsInConfrmTxt) {
								String newID = actionIdsToRearrange.get(actionID);
								diamondExpression = diamondExpression.replace("#@" + actionID + "@#", "#@" + newID + "@#");
							}

							workflowSequence.setEntryExpression(diamondExpression);
							workflowSequence.setPrimaryDestWorkflowId(workflow.getId());
							workflowSequence.setSecondaryDestWorkflowId(workflow.getId());
							workflowSequence.setRequired("Y");
							workflowSequence.setIntent(intent);
							workflowSequence.setKuId(ku.getId());
							workflowSequence.setWorkflowSequenceKey(workflowSequences.getWorkflowSequenceKey());
							workflowSequence.setPrimaryDestSequenceKey(workflowSequences.getPrimaryDestSequenceKey());
							workflowSequence
									.setSecondaryDestSequenceKey(workflowSequences.getSecondaryDestSequenceKey());

							session.save(workflowSequence);
						}
					}
				}
			}

		} catch (HibernateException ex) {

			logger.error("+++++ importWorkFlowSequence END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));

		} catch (Exception ex) {
			logger.error("+++++ importWorkFlowSequence END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));

		}
	}

}
