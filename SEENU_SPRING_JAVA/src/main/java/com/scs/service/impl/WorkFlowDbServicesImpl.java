package com.scs.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scs.entity.model.WorkFlow;
import com.scs.entity.model.WorkflowSequence;
import com.scs.entity.model.Action;

import com.scs.entity.model.ActionExtn;
import com.scs.entity.model.Confirm;
import com.scs.entity.model.EntityDetails;
import com.scs.entity.model.EntityQuestion;
import com.scs.entity.model.EntityRegex;
import com.scs.entity.model.ErrorResponse;
import com.scs.entity.model.Intent;
import com.scs.entity.model.IntentMapping;
import com.scs.entity.model.Keyword;
import com.scs.entity.model.Message;
import com.scs.entity.model.RegEx;
import com.scs.entity.model.RegexExtn;
import com.scs.entity.model.Response;
import com.scs.exception.ApiException;

import com.scs.model.ActionExtnModel;
import com.scs.model.ActionModel;
import com.scs.model.BaseRequestModel;
import com.scs.model.ConfirmationModel;
import com.scs.model.DiamondModel;
import com.scs.model.EntityModel;
import com.scs.model.EntityQuestionModel;
import com.scs.model.ErrorResponseModel;
import com.scs.model.IntentModel;
import com.scs.model.KeywordModel;
import com.scs.model.MessageModel;
import com.scs.model.NodeDataArray;
import com.scs.model.RegExModel;
import com.scs.model.RegexExtnModel;
import com.scs.model.ResponseModel;
import com.scs.model.WorkflowMetadataModel;
import com.scs.model.WorkflowSequenceModel;
import com.scs.service.WorkFlowDbServices;
import com.scs.util.ApiConstants;
import com.scs.util.ErrorConstants;
import com.scs.util.Utility;

@Service("FCDbService")

public class WorkFlowDbServicesImpl implements WorkFlowDbServices {

	private static final Logger logger = Logger.getLogger(WorkFlowDbServicesImpl.class);
	private static final String COL_INTENT_NAME = "intent";
	private static final String COL_ENTITY_NAME = "entity";

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Object getWorkFlowDetails(BaseRequestModel baseModel) throws ApiException {

		List<WorkFlow> flowChartLst = null;
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<WorkFlow> query = builder.createQuery(WorkFlow.class);
			Root<WorkFlow> root = query.from(WorkFlow.class);
			query.select(root);
			query.orderBy(builder.desc(root.get("id")));
			flowChartLst = session.createQuery(query).getResultList();

		} catch (HibernateException ex) {

			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}

		return flowChartLst;
	}

	@Override
	public Object createWorkFlow(BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;
		WorkFlow workFlow = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			workFlow = new WorkFlow();

			if (baseModel.getWorkFlow().getIntentId() != null) {
				Intent intent = new Intent();
				intent.setId(baseModel.getWorkFlow().getIntentId());
				workFlow.setIntent(intent);
			}

			if (baseModel.getWorkFlow().getActionId() != null) {
				Action action = new Action();
				action.setId(baseModel.getWorkFlow().getActionId());
				workFlow.setAction(action);
			}

			workFlow.setName(baseModel.getWorkFlow().getName());
			workFlow.setMetaData(baseModel.getWorkFlow().getMetaData());
			workFlow.setKuId(baseModel.getWorkFlow().getKuId());
			session.save(workFlow);
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);

		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}
		return workFlow;
	}

	@Override
	public Object updateWorkFlow(BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;
		String response = ApiConstants.SUCCESS;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<WorkFlow> update = builder.createCriteriaUpdate(WorkFlow.class);
			Root<WorkFlow> root = update.from(WorkFlow.class);
			update.set(root.get("metaData"), baseModel.getWorkFlow().getMetaData());
			update.set(root.get("name"), baseModel.getWorkFlow().getName());
			update.set(root.get("kuId"), baseModel.getWorkFlow().getKuId());

			if (baseModel.getWorkFlow().getIntentId() != null) {
				Intent intent = new Intent();
				intent.setId(baseModel.getWorkFlow().getIntentId());
				update.set(root.get(COL_INTENT_NAME), intent);
			}

			if (baseModel.getWorkFlow().getEntityId() != null) {
				EntityDetails entity = new EntityDetails();
				entity.setId(baseModel.getWorkFlow().getEntityId());
				update.set(root.get(COL_ENTITY_NAME), entity);
			}

			if (baseModel.getWorkFlow().getActionId() != null) {
				Action action = new Action();
				action.setId(baseModel.getWorkFlow().getActionId());
				update.set(root.get("action"), action);
			}

			update.where(builder.equal(root.get("id"), baseModel.getWorkFlow().getId()));
			session.createQuery(update).executeUpdate();
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}

		return response;
	}

	@Override
	public Object deleteFlowChart(BaseRequestModel baseModel, String id) throws ApiException {

		Transaction tx = null;
		String response = ApiConstants.SUCCESS;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			WorkFlow flowChart = new WorkFlow();
			flowChart.setId(Long.parseLong(id));
			session.delete(flowChart);

			tx.commit();

		} catch (HibernateException ex) {

			Utility.commonHibernateExceptionMethod(ex);

		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}

		return response;
	}

	@Override
	public Object deleteWorkflowbyKuId(String id) throws ApiException {

		Transaction tx = null;
		String response = ApiConstants.SUCCESS;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			WorkFlow workFlow = new WorkFlow();
			workFlow.setKuId(Long.parseLong(id));
			session.delete(workFlow);

			tx.commit();

		} catch (HibernateException ex) {

			Utility.commonHibernateExceptionMethod(ex);

		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}
		return response;
	}

	@Override
	public Object getworkFlowById(BaseRequestModel baseModel, String id) throws ApiException {

		WorkFlow workFlow = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<WorkFlow> select = builder.createQuery(WorkFlow.class);
			Root<WorkFlow> root = select.from(WorkFlow.class);
			ParameterExpression<Intent> intentId = builder.parameter(Intent.class);
			Predicate intentIdP = builder.equal(root.get(COL_INTENT_NAME), intentId);
			Predicate and1 = builder.and(intentIdP);
			select.where(and1);
			Intent intent = new Intent();
			intent.setId(Long.parseLong(id));
			List<WorkFlow> flowChartDetails = session.createQuery(select).setParameter(intentId, intent)
					.getResultList();
			if (!flowChartDetails.isEmpty())
				workFlow = flowChartDetails.get(0);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}

		return workFlow;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getMetadataValues(WorkflowMetadataModel workflowMetadataModel) throws ApiException {

		EntityDetails entityobj = null;

		Action actionEntityObj = null;
		Long intentid = null;
		Action actionobj = null;

		List<NodeDataArray> nodeDataArrayList = new ArrayList<>();
		WorkflowMetadataModel workflowmodel = new WorkflowMetadataModel();
		Long intentModelID = 5L;
		Long entityModelID = 2L;
		Long actionModelID = 3L;
		Long responseModelID = 4L;

		try {

			for (NodeDataArray nodeArrayModel : workflowMetadataModel.getNodeDataArray()) {

				NodeDataArray nodeDataArray = new NodeDataArray();

				nodeDataArray.setModelid(nodeArrayModel.getModelid());
				nodeDataArray.setCategory(nodeArrayModel.getCategory());
				nodeDataArray.setText(nodeArrayModel.getText());
				nodeDataArray.setFrom(nodeArrayModel.getFrom());
				nodeDataArray.setTo(nodeArrayModel.getTo());
				nodeDataArray.setIsReadOnly(nodeArrayModel.getIsReadOnly());
				nodeDataArray.setLoc(nodeArrayModel.getLoc());
				nodeDataArray.setId(nodeArrayModel.getId());
				nodeDataArray.setKey(nodeArrayModel.getKey());
				nodeDataArray.setRequired(nodeArrayModel.getRequired());
				nodeDataArray.setOrder(nodeArrayModel.getOrder());
				nodeDataArray.setOrderId(nodeArrayModel.getOrderId());

				// Intent Add for Workflow
				if (nodeArrayModel.getModelid() == intentModelID) {

					IntentModel intent = null;
					intent = (IntentModel) setIntent(nodeArrayModel);
					nodeDataArray.setIntent(intent);

					intentid = intent.getId();

				}

				// Entity Add for Workflow
				if (nodeArrayModel.getModelid() == entityModelID) {

					List<RegEx> regexLst = new ArrayList<>();

					EntityModel entity = null;

					if (nodeArrayModel.getEntity() != null) {

						entityobj = (EntityDetails) getEntityByName(intentid, nodeArrayModel.getEntity().getName());

						if (entityobj != null) {
							logger.info("entityobj.getId(): " + entityobj.getId());
							List<EntityRegex> entityRegex = null;
							entityRegex = (List<EntityRegex>) getMapRegexByEntityId(entityobj.getId());

							for (EntityRegex entRegex : entityRegex) {

								RegEx regEx = null;

								regEx = (RegEx) getRegexById(entRegex.getRegexId());
								regexLst.add(regEx);

							}

							if (intentid != null && entityobj != null) {

								entity = (EntityModel) setEntityModel(entityobj, regexLst);

								actionEntityObj = (Action) getActionByEntityID(entityobj.getId());

								if (actionEntityObj != null) {
									ActionModel actModel = null;
									actModel = (ActionModel) setEntityActionModel(actionEntityObj);
									entity.setAction(actModel);
								}
								// get questions by entity ID --- >

								for (EntityQuestion questionModel : entityobj.getQuestions()) {
									EntityQuestionModel question = null;

									question = (EntityQuestionModel) setEntityQuestion(entityobj, questionModel);

									entity.getQuestions().add(question);

								}

								nodeDataArray.setRegExs(nodeArrayModel.getRegExs());

								nodeDataArray.setRegExs(nodeArrayModel.getRegExs());

								nodeDataArray.setEntity(entity);
							}
						}
					}
				}
				// Action Add for Workflow
				if (nodeArrayModel.getModelid() == actionModelID) {

					actionobj = (Action) getActionByGI(nodeArrayModel.getAction().getName(), intentid);

					ActionModel action = null;

					if (actionobj != null) {
						action = (ActionModel) setAction(actionobj);
						nodeDataArray.setAction(action);
					} else {

						nodeDataArray.setAction(nodeArrayModel.getAction());
					}

				}
				// Response Add in Workflow

				if (nodeArrayModel.getModelid() == responseModelID) {
					List<Response> response = new ArrayList<>();
					Message messageObj = new Message();
					WorkflowSequenceModel workflowSequence = new WorkflowSequenceModel();

					if (nodeArrayModel.getMessage().getId() != null) {

						messageObj = (Message) getMessageByMessageCode(intentid,
								nodeArrayModel.getMessage().getMessageCode());

						workflowSequence = (WorkflowSequenceModel) getResponseWorkflowSequence(
								messageObj.getId().toString());

						response = (List<Response>) getResponseByMessageID(messageObj.getId());
					}
					if ((response != null) && (!response.isEmpty())) {
						List<ResponseModel> responseLstModel = null;

						responseLstModel = (List<ResponseModel>) setResponse(response);

						nodeDataArray.setResponse(responseLstModel);

						nodeDataArray.setWorkflowSequence(workflowSequence);

						MessageModel message = new MessageModel();
						message.setId(messageObj.getId());
						message.setMessageCode(nodeArrayModel.getMessage().getMessageCode());
						message.setResponses(responseLstModel);

						nodeDataArray.setMessage(message);

					} else {

						nodeDataArray.setResponse(nodeArrayModel.getResponse());
						nodeDataArray.setWorkflowSequence(workflowSequence);
					}

				}
				// For Decision Box
				if (nodeArrayModel.getModelid() == 6L) {

					DiamondModel decision = new DiamondModel();
					WorkflowSequenceModel workflowSequenceModel = null;

					workflowSequenceModel = (WorkflowSequenceModel) getDiamondWorkflowSequence(nodeArrayModel.getKey(),
							intentid);
					decision.setWorkflowSequenceModel(workflowSequenceModel);

					nodeDataArray.setDecision(decision);

				}

				nodeDataArrayList.add(nodeDataArray);

			}

			workflowmodel.setClassvar(workflowMetadataModel.getClassvar());
			workflowmodel.setNodeDataArray(nodeDataArrayList);
			workflowmodel.setLinkDataArray(workflowMetadataModel.getLinkDataArray());
			workflowmodel.setLinkFromPortIdProperty(workflowMetadataModel.getLinkFromPortIdProperty());
			workflowmodel.setLinkToPortIdProperty(workflowMetadataModel.getLinkToPortIdProperty());

		} catch (HibernateException ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getMetadataValues END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, "");
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getMetadataValues END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, "");
		}

		return workflowmodel;
	}

	private Object setResponse(List<Response> responseLst) throws ApiException {

		List<ResponseModel> response = new ArrayList<>();

		for (Response intentResponses : responseLst) {
			WorkflowSequenceModel workflowSequence = null;
			ResponseModel responseModel = new ResponseModel();
			responseModel.setId(intentResponses.getId());
			IntentModel intent = new IntentModel();
			intent.setId(intentResponses.getIntent().getId());
			responseModel.setIntent(intent);
			responseModel.setLocaleCode(intentResponses.getLocaleCode());
			responseModel.setResponseText(intentResponses.getResponseText());
			responseModel.setKuId(intentResponses.getKuId());
			responseModel.setGlobalIdentifier(intentResponses.getGlobalIdentifier());

			workflowSequence = (WorkflowSequenceModel) getResponseWorkflowSequence(
					intentResponses.getIntent().getId().toString());

			responseModel.setWorkflowSequence(workflowSequence);

			response.add(responseModel);

		}

		return response;

	}

	private Object getResponseWorkflowSequence(String id) throws ApiException {

		WorkflowSequence workflowSequence = null;
		WorkflowSequenceModel workflowSequenceModel = new WorkflowSequenceModel();

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<WorkflowSequence> select = builder.createQuery(WorkflowSequence.class);
			Root<WorkflowSequence> root = select.from(WorkflowSequence.class);
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("entryExpression"), id));
			predicates.add(builder.equal(root.get("entryType"), "MESSAGE"));

			select.where(predicates.toArray(new Predicate[] {}));

			List<WorkflowSequence> workflowSequenceDetails = session.createQuery(select).getResultList();

			if (!workflowSequenceDetails.isEmpty())
				workflowSequence = workflowSequenceDetails.get(0);
			if (workflowSequence != null) {
				workflowSequenceModel.setId(workflowSequence.getId());
				workflowSequenceModel.setEntryType(workflowSequence.getEntryType());
				workflowSequenceModel.setEntryExpression(workflowSequence.getEntryExpression());
				workflowSequenceModel.setIntentId(workflowSequence.getIntent().getId());
				workflowSequenceModel.setPrimaryDestSequenceKey(workflowSequence.getPrimaryDestSequenceKey());
				workflowSequenceModel.setPrimaryDestWorkflowId(workflowSequence.getPrimaryDestWorkflowId());
				workflowSequenceModel.setSecondaryDestSequenceKey(workflowSequence.getSecondaryDestSequenceKey());
				workflowSequenceModel.setSecondaryDestWorkflowId(workflowSequence.getSecondaryDestWorkflowId());
				workflowSequenceModel.setTerminalType(workflowSequence.getTerminalType());
				workflowSequenceModel.setRequired(workflowSequence.getRequired());
				workflowSequenceModel.setWorkflowId(workflowSequence.getWorkflowId());
				workflowSequenceModel.setWorkflowSequenceKey(workflowSequence.getWorkflowSequenceKey());

			}

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error(
					"+++++ WorkFlowDbServicesImpl.getResponseWorkflowSequence END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getResponseWorkflowSequence END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return workflowSequenceModel;
	}

	private Object getMessageByMessageCode(Long id, String messageCode) throws ApiException {

		Message message = new Message();

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Message> select = builder.createQuery(Message.class);
			Root<Message> root = select.from(Message.class);
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get(COL_INTENT_NAME), id));
			predicates.add(builder.equal(root.get("messageCode"), messageCode));

			select.where(predicates.toArray(new Predicate[] {}));

			List<Message> messageDetails = session.createQuery(select).getResultList();

			if (!messageDetails.isEmpty())
				message = messageDetails.get(0);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error(
					"+++++ WorkFlowDbServicesImpl.getMessageByMessageCode END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getMessageByMessageCode END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return message;
	}

	private Object getDiamondWorkflowSequence(String key, Long id) throws ApiException {

		WorkflowSequence workflowSequence = null;
		WorkflowSequenceModel workflowSequenceModel = new WorkflowSequenceModel();

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<WorkflowSequence> select = builder.createQuery(WorkflowSequence.class);
			Root<WorkflowSequence> root = select.from(WorkflowSequence.class);
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("workflowSequenceKey"), key));
			predicates.add(builder.equal(root.get("entryType"), "DIAMOND"));
			predicates.add(builder.equal(root.get(COL_INTENT_NAME), id));

			select.where(predicates.toArray(new Predicate[] {}));

			List<WorkflowSequence> workflowSequenceDetails = session.createQuery(select).getResultList();

			if (!workflowSequenceDetails.isEmpty())
				workflowSequence = workflowSequenceDetails.get(0);
			if (workflowSequence != null) {
				workflowSequenceModel.setId(workflowSequence.getId());
				workflowSequenceModel.setEntryType(workflowSequence.getEntryType());
				workflowSequenceModel.setEntryExpression(workflowSequence.getEntryExpression());
				workflowSequenceModel.setIntentId(workflowSequence.getIntent().getId());
				workflowSequenceModel.setPrimaryDestSequenceKey(workflowSequence.getPrimaryDestSequenceKey());
				workflowSequenceModel.setPrimaryDestWorkflowId(workflowSequence.getPrimaryDestWorkflowId());
				workflowSequenceModel.setSecondaryDestSequenceKey(workflowSequence.getSecondaryDestSequenceKey());
				workflowSequenceModel.setSecondaryDestWorkflowId(workflowSequence.getSecondaryDestWorkflowId());
				workflowSequenceModel.setTerminalType(workflowSequence.getTerminalType());
				workflowSequenceModel.setRequired(workflowSequence.getRequired());
				workflowSequenceModel.setWorkflowId(workflowSequence.getWorkflowId());
				workflowSequenceModel.setWorkflowSequenceKey(workflowSequence.getWorkflowSequenceKey());

			}

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error(
					"+++++ WorkFlowDbServicesImpl.getDiamondWorkflowSequence END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getDiamondWorkflowSequence END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return workflowSequenceModel;
	}

	private Object setEntityQuestion(EntityDetails entityobj, EntityQuestion questionModel) {

		EntityQuestionModel question = new EntityQuestionModel();
		question.setQuestion(questionModel.getQuestion());
		question.setId(questionModel.getId());
		question.setExample(questionModel.getExample());
		question.setLocaleCode(questionModel.getLocaleCode());
		question.setEntity(entityobj);
		question.setTitle(questionModel.getTitle());
		question.setSubTitle(questionModel.getSubTitle());
		question.setButtonText(questionModel.getButtonText());
		question.setImageUrl(questionModel.getImageUrl());

		return question;

	}

	private Object setActionErrorResponses(Action actionobj) {

		List<ErrorResponseModel> errorResponseModelList = new ArrayList<>();

		for (ErrorResponse errorResponse : actionobj.getErrorResponses()) {
			ErrorResponseModel erResponses = new ErrorResponseModel();
			erResponses.setErrorCode(errorResponse.getErrorCode());
			erResponses.setId(errorResponse.getId());
			erResponses.setErrorResponse(errorResponse.getErrorResponse());
			erResponses.setLocaleCode(errorResponse.getLocaleCode());
			erResponses.setKuId(actionobj.getKuId());
			errorResponseModelList.add(erResponses);

		}

		return errorResponseModelList;

	}

	private Object setAction(Action actionobj) throws ApiException {

		ActionModel action = new ActionModel();

		action.setName(actionobj.getName());
		action.setId(actionobj.getId());
		action.setKuId(actionobj.getKuId());
		action.setCallingInterval(actionobj.getCallingInterval());
		action.setWarningMessage(actionobj.getWarningMessage());
		action.setDataType(actionobj.getDataType());
		action.setGlobalIdentifier(actionobj.getGlobalIdentifier());

		WorkflowSequenceModel workflowSequence = (WorkflowSequenceModel) getActionWorkflowSequence(
				actionobj.getId().toString());

		action.setWorkflowSequence(workflowSequence);

		if (actionobj.getIntent() != null) {
			action.setIntentId(actionobj.getIntent().getId());
		}
		if (actionobj.getConfirm() != null) {

			List<Confirm> confirmLst = new ArrayList<>();

			for (Confirm confirm : actionobj.getConfirm()) {

				confirm.setText(confirm.getText());
				confirm.setConfirmationType(confirm.getConfirmationType());
				confirm.setConfirmationOption(confirm.getConfirmationOption());
				confirm.setUnConfirmationOption(confirm.getUnConfirmationOption());
				confirm.setKuId(confirm.getKuId());
				confirm.setLocaleCode(confirm.getLocaleCode());
				confirm.setTerminationText(confirm.getTerminationText());
				confirm.setAction(actionobj);

				confirmLst.add(confirm);

			}

			action.setConfirm(confirmLst);
		}

		List<ActionExtnModel> actionExtnLst = null;
		actionExtnLst = (List<ActionExtnModel>) setActionExtnModel(actionobj);

		action.setActionExtn(actionExtnLst);

		List<ErrorResponseModel> errorResponseModelList = null;

		errorResponseModelList = (List<ErrorResponseModel>) setActionErrorResponses(actionobj);

		action.setErrorResponses(errorResponseModelList);

		return action;

	}

	private Object getActionWorkflowSequence(String id) throws ApiException {

		WorkflowSequence workflowSequence = null;
		WorkflowSequenceModel workflowSequenceModel = new WorkflowSequenceModel();

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<WorkflowSequence> select = builder.createQuery(WorkflowSequence.class);
			Root<WorkflowSequence> root = select.from(WorkflowSequence.class);
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("entryExpression"), id));
			predicates.add(builder.equal(root.get("entryType"), "ACTION"));

			select.where(predicates.toArray(new Predicate[] {}));

			List<WorkflowSequence> workflowSequenceDetails = session.createQuery(select).getResultList();

			if (!workflowSequenceDetails.isEmpty())
				workflowSequence = workflowSequenceDetails.get(0);
			if (workflowSequence != null) {
				workflowSequenceModel.setId(workflowSequence.getId());
				workflowSequenceModel.setEntryType(workflowSequence.getEntryType());
				workflowSequenceModel.setEntryExpression(workflowSequence.getEntryExpression());
				workflowSequenceModel.setIntentId(workflowSequence.getIntent().getId());
				workflowSequenceModel.setPrimaryDestSequenceKey(workflowSequence.getPrimaryDestSequenceKey());
				workflowSequenceModel.setPrimaryDestWorkflowId(workflowSequence.getPrimaryDestWorkflowId());
				workflowSequenceModel.setSecondaryDestSequenceKey(workflowSequence.getSecondaryDestSequenceKey());
				workflowSequenceModel.setSecondaryDestWorkflowId(workflowSequence.getSecondaryDestWorkflowId());
				workflowSequenceModel.setTerminalType(workflowSequence.getTerminalType());
				workflowSequenceModel.setRequired(workflowSequence.getRequired());
				workflowSequenceModel.setWorkflowId(workflowSequence.getWorkflowId());
				workflowSequenceModel.setWorkflowSequenceKey(workflowSequence.getWorkflowSequenceKey());

			}

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error(
					"+++++ WorkFlowDbServicesImpl.getActionWorkflowSequence END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getActionWorkflowSequence END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return workflowSequenceModel;
	}

	private Object setEntityActionExtnModel(Action actionEntityObj) {

		List<ActionExtnModel> actionExtnLst = new ArrayList<>();

		for (ActionExtn actnExtn : actionEntityObj.getActionExtn()) {

			ActionExtnModel actionExtnModel = new ActionExtnModel();

			actionExtnModel.setAction(actionEntityObj);
			actionExtnModel.setId(actnExtn.getId());
			actionExtnModel.setCallMethod(actnExtn.getCallMethod());
			actionExtnModel.setErrorCode(actnExtn.getErrorCode());
			actionExtnModel.setLocaleCode(actnExtn.getLocaleCode());
			actionExtnModel.setRequestBody(actnExtn.getRequestBody());
			actionExtnModel.setUrl(actnExtn.getUrl());
			actionExtnModel.setSuccessCode(actnExtn.getSuccessCode());
			actionExtnModel.setResponsePath(actnExtn.getResponsePath());

			actionExtnLst.add(actionExtnModel);

		}

		return actionExtnLst;

	}

	private Object setActionExtnModel(Action actionobj) {

		List<ActionExtnModel> actionExtnLst = new ArrayList<>();

		for (ActionExtn actnExtn : actionobj.getActionExtn()) {

			ActionExtnModel actionExtnModel = new ActionExtnModel();
			actionExtnModel.setAction(actionobj);
			actionExtnModel.setId(actnExtn.getId());
			actionExtnModel.setCallMethod(actnExtn.getCallMethod());
			actionExtnModel.setErrorCode(actnExtn.getErrorCode());
			actionExtnModel.setLocaleCode(actnExtn.getLocaleCode());
			actionExtnModel.setRequestBody(actnExtn.getRequestBody());
			actionExtnModel.setUrl(actnExtn.getUrl());
			actionExtnModel.setSuccessCode(actnExtn.getSuccessCode());
			actionExtnModel.setResponsePath(actnExtn.getResponsePath());

			actionExtnLst.add(actionExtnModel);

		}

		return actionExtnLst;

	}

	private Object setEntityActionModel(Action actionEntityObj) throws ApiException {

		ActionModel actModel = new ActionModel();

		actModel.setId(actionEntityObj.getId());
		actModel.setEntityId(actionEntityObj.getEntity().getId());
		actModel.setGlobalIdentifier(actionEntityObj.getGlobalIdentifier());
		actModel.setKuId(actionEntityObj.getKuId());

		List<ErrorResponse> errorResponseDetails = null;
		errorResponseDetails = (List<ErrorResponse>) getErrorResponseByActionID(actionEntityObj.getId());

		List<ErrorResponseModel> errorResponseModel = new ArrayList<>();

		for (ErrorResponse errResponse : errorResponseDetails) {

			ErrorResponseModel errResponseModel = new ErrorResponseModel();

			errResponseModel.setActionId(actionEntityObj.getId());
			errResponseModel.setErrorCode(errResponse.getErrorCode());
			errResponseModel.setErrorResponse(errResponse.getErrorResponse());
			errResponseModel.setId(errResponse.getId());
			errResponseModel.setLocaleCode(errResponse.getLocaleCode());
			errResponseModel.setKuId(errResponse.getKuId());
			errorResponseModel.add(errResponseModel);

		}

		List<ActionExtnModel> actionExtnLst = null;

		actionExtnLst = (List<ActionExtnModel>) setEntityActionExtnModel(actionEntityObj);

		actModel.setActionExtn(actionExtnLst);
		actModel.setErrorResponse(errorResponseModel);

		return actModel;

	}

	private Object setEntityModel(EntityDetails entityobj, List<RegEx> regexLst) throws ApiException {
		EntityModel entity = new EntityModel();
		List<RegExModel> regexModelLst = new ArrayList<>();
		WorkflowSequenceModel workflowSequence = new WorkflowSequenceModel();
		try {

			entity.setId(entityobj.getId());
			entity.setName(entityobj.getName());
			entity.setEntityType(entityobj.getEntityType());
			entity.setKuId(entityobj.getKuId());
			entity.setGlobalIdentifier(entityobj.getGlobalIdentifier());
			entity.setRequired(entityobj.getRequired());
			entity.setDataType(entityobj.getDataType());
			regexModelLst = (List<RegExModel>) setRegEx(regexLst);
			entity.setRegex(regexModelLst);
			entity.setIntentId(entityobj.getIntentId());
			workflowSequence = (WorkflowSequenceModel) getEntityWorkflowSequence(entityobj.getId().toString());

			entity.setWorkflowSequence(workflowSequence);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ WorkFlowDbServicesImpl.setEntityModel END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.setEntityModel END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}
		return entity;

	}

	private Object setRegEx(List<RegEx> regexLst) {
		List<RegExModel> regexModelLst = new ArrayList<>();

		for (RegEx regex : regexLst) {

			RegExModel regexModel = new RegExModel();
			regexModel.setId(regex.getId());
			regexModel.setExpression(regex.getExpression());
			regexModel.setErrorCode(regex.getErrorCode());
			regexModel.setRegexname(regex.getRegexname());
			regexModelLst.add(regexModel);

		}

		return regexModelLst;

	}

	private Object getEntityWorkflowSequence(String id) throws ApiException {

		WorkflowSequence workflowSequence = null;
		WorkflowSequenceModel workflowSequenceModel = new WorkflowSequenceModel();

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<WorkflowSequence> select = builder.createQuery(WorkflowSequence.class);
			Root<WorkflowSequence> root = select.from(WorkflowSequence.class);
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("entryExpression"), id));
			predicates.add(builder.equal(root.get("entryType"), "ENTITY"));

			select.where(predicates.toArray(new Predicate[] {}));

			List<WorkflowSequence> workflowSequenceDetails = session.createQuery(select).getResultList();

			if (!workflowSequenceDetails.isEmpty())
				workflowSequence = workflowSequenceDetails.get(0);
			if (workflowSequence != null) {
				workflowSequenceModel.setId(workflowSequence.getId());
				workflowSequenceModel.setEntryType(workflowSequence.getEntryType());
				workflowSequenceModel.setEntryExpression(workflowSequence.getEntryExpression());
				workflowSequenceModel.setIntentId(workflowSequence.getIntent().getId());
				workflowSequenceModel.setPrimaryDestSequenceKey(workflowSequence.getPrimaryDestSequenceKey());
				workflowSequenceModel.setPrimaryDestWorkflowId(workflowSequence.getPrimaryDestWorkflowId());
				workflowSequenceModel.setSecondaryDestSequenceKey(workflowSequence.getSecondaryDestSequenceKey());
				workflowSequenceModel.setSecondaryDestWorkflowId(workflowSequence.getSecondaryDestWorkflowId());
				workflowSequenceModel.setTerminalType(workflowSequence.getTerminalType());
				workflowSequenceModel.setRequired(workflowSequence.getRequired());
				workflowSequenceModel.setWorkflowId(workflowSequence.getWorkflowId());
				workflowSequenceModel.setWorkflowSequenceKey(workflowSequence.getWorkflowSequenceKey());

			}

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error(
					"+++++ WorkFlowDbServicesImpl.getEntityWorkflowSequence END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getEntityWorkflowSequence END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}
		return workflowSequenceModel;
	}

	private Object setIntent(NodeDataArray nodeArrayModel) throws ApiException {

		Intent intentobj = null;

		Instant instant = Instant.now();
		IntentModel intent = new IntentModel();
		try {

			intentobj = (Intent) getIntentByName(nodeArrayModel.getIntent().getName());

			if (intentobj != null) {
				intent.setId(intentobj.getId());
				intent.setName(intentobj.getName());
				intent.setDate(instant.toString());
				intent.setKuId(intentobj.getKuId());
				intent.setNames(intentobj.getNames());
				intent.setGlobalIdentifier(intentobj.getGlobalIdentifier());

				// get keywords by Intent ID

				for (Keyword keywordModel : intentobj.getKeywords()) {
					KeywordModel keyword = new KeywordModel();
					keyword.setDate(instant.toString());
					keyword.setId(keywordModel.getId());
					keyword.setPolarity(keywordModel.getPolarity());
					keyword.setKeywordField(keywordModel.getKeywordField());
					keyword.setLocaleCode(keywordModel.getLocaleCode());
					intent.getKeywords().add(keyword);

				}
			}
		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ WorkFlowDbServicesImpl.setIntent END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.setIntent END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return intent;
	}

	@Override
	public Object getIntentByName(String name) throws ApiException {

		Intent intent = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Intent> select = builder.createQuery(Intent.class);
			Root<Intent> root = select.from(Intent.class);
			ParameterExpression<String> intentName = builder.parameter(String.class);
			Predicate intentNameP = builder.equal(root.get("name"), intentName);
			Predicate and1 = builder.and(intentNameP);
			select.where(and1);

			List<Intent> intentDetails = session.createQuery(select).setParameter(intentName, name).getResultList();

			if (!intentDetails.isEmpty())
				intent = intentDetails.get(0);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ WorkFlowDbServicesImpl.getIntentByName END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getIntentByName END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return intent;
	}

	@Override
	public Object getActionByGI(String name, Long intentId) throws ApiException {

		Action action = null;

		try (Session session = sessionFactory.openSession()) {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Action> select = builder.createQuery(Action.class);
			Root<Action> root = select.from(Action.class);
			Intent intent = new Intent();
			intent.setId(intentId);

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("name"), name));
			predicates.add(builder.equal(root.get(COL_INTENT_NAME), intentId));

			select.where(predicates.toArray(new Predicate[] {}));

			List<Action> actDetails = session.createQuery(select).getResultList();
			if (!actDetails.isEmpty())
				action = actDetails.get(0);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ WorkFlowDbServicesImpl.getActionByGI END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getActionByGI END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return action;
	}

	@Override
	public Object getIntentByID(Long id) throws ApiException {

		Intent intent = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Intent> select = builder.createQuery(Intent.class);
			Root<Intent> root = select.from(Intent.class);
			ParameterExpression<Long> intentId = builder.parameter(Long.class);
			Predicate intentIdP = builder.equal(root.get("id"), intentId);
			Predicate and1 = builder.and(intentIdP);
			select.where(and1);

			List<Intent> intentDetails = session.createQuery(select).setParameter(intentId, id).getResultList();

			if (!intentDetails.isEmpty())
				intent = intentDetails.get(0);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ WorkFlowDbServicesImpl.getIntentByID END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getIntentByID END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return intent;
	}

	private Object getResponseByMessageID(Long id) throws ApiException {

		List<Response> resDetails = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Response> select = builder.createQuery(Response.class);
			Root<Response> root = select.from(Response.class);
			ParameterExpression<Message> messageId = builder.parameter(Message.class);
			Message message = new Message();
			message.setId(id);
			Predicate messageIdP = builder.equal(root.get("message"), messageId);
			Predicate and1 = builder.and(messageIdP);
			select.where(and1);

			resDetails = session.createQuery(select).setParameter(messageId, message).getResultList();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error(
					"+++++ WorkFlowDbServicesImpl.getResponseByMessageID END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getResponseByMessageID END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return resDetails;
	}

	@Override
	public Object getEntityByName(Long intentId, String name) throws ApiException {

		EntityDetails entityDetails = null;

		try (Session session = sessionFactory.openSession()) {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<EntityDetails> select = builder.createQuery(EntityDetails.class);
			Root<EntityDetails> root = select.from(EntityDetails.class);
			Intent intent = new Intent();
			intent.setId(intentId);

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("name"), name));
			predicates.add(builder.equal(root.get("intentId"), intentId));

			select.where(predicates.toArray(new Predicate[] {}));

			List<EntityDetails> entDetails = session.createQuery(select).getResultList();
			if (!entDetails.isEmpty())
				entityDetails = entDetails.get(0);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}

		return entityDetails;
	}

	private Object getMapRegexByEntityId(Long id) throws ApiException {

		List<EntityRegex> entityDetails = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<EntityRegex> select = builder.createQuery(EntityRegex.class);
			Root<EntityRegex> root = select.from(EntityRegex.class);
			ParameterExpression<Long> entityId = builder.parameter(Long.class);
			Predicate workflowSequenceIdP = builder.equal(root.get("entityId"), entityId);
			Predicate and1 = builder.and(workflowSequenceIdP);
			select.where(and1);

			entityDetails = session.createQuery(select).setParameter(entityId, id).getResultList();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}

		return entityDetails;
	}

	private Object getRegexById(Long id) throws ApiException {

		RegEx regex = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<RegEx> select = builder.createQuery(RegEx.class);
			Root<RegEx> root = select.from(RegEx.class);
			ParameterExpression<Long> regexId = builder.parameter(Long.class);
			Predicate regexIdP = builder.equal(root.get("id"), regexId);
			Predicate and1 = builder.and(regexIdP);
			select.where(and1);

			List<RegEx> regexDetails = session.createQuery(select).setParameter(regexId, id).getResultList();
			if (!regexDetails.isEmpty())
				regex = regexDetails.get(0);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}

		return regex;
	}

	@Override
	public Object getActionByName(Long id) throws ApiException {

		Action action = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Action> select = builder.createQuery(Action.class);
			Root<Action> root = select.from(Action.class);
			ParameterExpression<Intent> intnetId = builder.parameter(Intent.class);
			Intent intent = new Intent();
			intent.setId(id);
			Predicate intentIdP = builder.equal(root.get(COL_INTENT_NAME), intnetId);
			Predicate and1 = builder.and(intentIdP);
			select.where(and1);

			List<Action> actionDetails = session.createQuery(select).setParameter(intnetId, intent).getResultList();
			if (!actionDetails.isEmpty())
				action = actionDetails.get(0);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ WorkFlowDbServicesImpl.getActionByName END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getActionByName END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return action;
	}

	@Override
	public Object getActionByEntityID(Long id) throws ApiException {

		Action action = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Action> select = builder.createQuery(Action.class);
			Root<Action> root = select.from(Action.class);
			ParameterExpression<EntityDetails> entityId = builder.parameter(EntityDetails.class);
			EntityDetails entity = new EntityDetails();
			entity.setId(id);
			Predicate entityIdP = builder.equal(root.get(COL_ENTITY_NAME), entityId);
			Predicate and1 = builder.and(entityIdP);
			select.where(and1);

			List<Action> actionDetails = session.createQuery(select).setParameter(entityId, entity).getResultList();
			if (!actionDetails.isEmpty())
				action = actionDetails.get(0);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ WorkFlowDbServicesImpl.getActionByEntityID END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getActionByEntityID END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return action;
	}

	private Object getErrorResponseByActionID(Long id) throws ApiException {

		List<ErrorResponse> errorResponseDetails = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ErrorResponse> select = builder.createQuery(ErrorResponse.class);
			Root<ErrorResponse> root = select.from(ErrorResponse.class);
			ParameterExpression<Action> actionId = builder.parameter(Action.class);
			Action action = new Action();
			action.setId(id);
			Predicate entityIdP = builder.equal(root.get("action"), actionId);
			Predicate and1 = builder.and(entityIdP);
			select.where(and1);

			errorResponseDetails = session.createQuery(select).setParameter(actionId, action).getResultList();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error(
					"+++++ WorkFlowDbServicesImpl.getErrorResponseByActionID END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getErrorResponseByActionID END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return errorResponseDetails;
	}

	@Override
	public Object updateWorkFlowMetadata(String metadata, Long id) throws ApiException {

		Transaction tx = null;
		String response = ApiConstants.SUCCESS;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<WorkFlow> update = builder.createCriteriaUpdate(WorkFlow.class);
			Root<WorkFlow> root = update.from(WorkFlow.class);
			update.set(root.get("metaData"), metadata);
			update.where(builder.equal(root.get("id"), id));
			session.createQuery(update).executeUpdate();
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}

		return response;
	}

	public Object getRegExByName(String name) throws ApiException {

		RegEx regex = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<RegEx> select = builder.createQuery(RegEx.class);
			Root<RegEx> root = select.from(RegEx.class);
			ParameterExpression<String> regexName = builder.parameter(String.class);
			Predicate regexNameP = builder.equal(root.get("regexname"), regexName);
			Predicate and1 = builder.and(regexNameP);
			select.where(and1);

			List<RegEx> regexDetails = session.createQuery(select).setParameter(regexName, name).getResultList();
			if (!regexDetails.isEmpty())
				regex = regexDetails.get(0);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ WorkFlowDbServicesImpl.getRegExByName END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getRegExByName END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return regex;
	}

	@Override
	public Object createWorkFlowSequence(BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;
		WorkflowSequence workFlowSequence = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			workFlowSequence = new WorkflowSequence();

			Intent intent = new Intent();
			intent.setId(baseModel.getWorkflowSequence().getIntentId());
			workFlowSequence.setIntent(intent);
			workFlowSequence.setWorkflowId(baseModel.getWorkflowSequence().getWorkflowId());
			workFlowSequence.setWorkflowSequenceKey(baseModel.getWorkflowSequence().getWorkflowSequenceKey());
			workFlowSequence.setEntryType(baseModel.getWorkflowSequence().getEntryType());
			workFlowSequence.setEntryExpression(baseModel.getWorkflowSequence().getEntryExpression());
			workFlowSequence.setPrimaryDestWorkflowId(baseModel.getWorkflowSequence().getPrimaryDestWorkflowId());
			workFlowSequence.setPrimaryDestSequenceKey(baseModel.getWorkflowSequence().getPrimaryDestSequenceKey());
			workFlowSequence.setSecondaryDestWorkflowId(baseModel.getWorkflowSequence().getSecondaryDestWorkflowId());
			workFlowSequence.setSecondaryDestSequenceKey(baseModel.getWorkflowSequence().getSecondaryDestSequenceKey());
			workFlowSequence.setTerminalType(baseModel.getWorkflowSequence().getTerminalType());
			workFlowSequence.setRequired(baseModel.getWorkflowSequence().getRequired());
			workFlowSequence.setKuId(baseModel.getWorkflowSequence().getKuId());
			workFlowSequence.setInitialValidation("Y");

			session.save(workFlowSequence);

			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);

		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}
		return workFlowSequence;
	}

	@Override
	public Object updateWorkFlowSequence(BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;
		String response = ApiConstants.SUCCESS;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<WorkflowSequence> update = builder.createCriteriaUpdate(WorkflowSequence.class);
			Root<WorkflowSequence> root = update.from(WorkflowSequence.class);

			update.set(root.get(COL_INTENT_NAME), baseModel.getWorkflowSequence().getIntentId());
			update.set(root.get("workflowId"), baseModel.getWorkflowSequence().getWorkflowId());
			update.set(root.get("workflowSequenceKey"), baseModel.getWorkflowSequence().getWorkflowSequenceKey());
			update.set(root.get("entryType"), baseModel.getWorkflowSequence().getEntryType());
			update.set(root.get("entryExpression"), baseModel.getWorkflowSequence().getEntryExpression());
			update.set(root.get("primaryDestWorkflowId"), baseModel.getWorkflowSequence().getPrimaryDestWorkflowId());
			update.set(root.get("primaryDestSequenceKey"), baseModel.getWorkflowSequence().getPrimaryDestSequenceKey());
			update.set(root.get("secondaryDestWorkflowId"),
					baseModel.getWorkflowSequence().getSecondaryDestWorkflowId());
			update.set(root.get("secondaryDestSequenceKey"),
					baseModel.getWorkflowSequence().getSecondaryDestSequenceKey());
			update.set(root.get("terminalType"), baseModel.getWorkflowSequence().getTerminalType());
			update.set(root.get("required"), baseModel.getWorkflowSequence().getRequired());
			update.set(root.get("initialValidation"), "Y");
			update.where(builder.equal(root.get("id"), baseModel.getWorkflowSequence().getId()));

			session.createQuery(update).executeUpdate();

			if (baseModel.getWorkflowSequence().getTerminalType() != null
					&& "START".equals(baseModel.getWorkflowSequence().getTerminalType())) {

				IntentMapping mapDetails = (IntentMapping) getIntentMapping(
						baseModel.getWorkflowSequence().getIntentId(), baseModel.getWorkflowSequence().getEntryType(),
						baseModel.getWorkflowSequence().getEntryExpression());

				if (mapDetails == null) {

					IntentMapping intentMapping = new IntentMapping();
					intentMapping.setEntryId(baseModel.getWorkflowSequence().getEntryExpression());
					intentMapping.setEntryType(baseModel.getWorkflowSequence().getEntryType());
					Intent intent = new Intent();
					intent.setId(baseModel.getWorkflowSequence().getIntentId());
					intentMapping.setIntent(intent);
					intentMapping.setKuId(baseModel.getWorkflowSequence().getKuId());
					intentMapping.setWorkflowId(baseModel.getWorkflowSequence().getWorkflowId());
					intentMapping.setOrderId(1L);
					intentMapping.setRequired("Y");

					session.save(intentMapping);
				} else {
					updateIntentMapping(baseModel, mapDetails);
				}
			}

			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}

		return response;
	}

	@Override
	public Object updateWorkflowSequenceAndDeleteIntentMapping(Long intentId, String entryExpression,
			String workflowSequenceKey) throws ApiException {

		Transaction tx = null;
		String response = ApiConstants.SUCCESS;
		String terminalType = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<WorkflowSequence> update = builder.createCriteriaUpdate(WorkflowSequence.class);
			Root<WorkflowSequence> root = update.from(WorkflowSequence.class);

			List<Predicate> predicates = new ArrayList<>();
			update.set(root.get("terminalType"), terminalType);
			predicates.add(builder.equal(root.get("workflowSequenceKey"), workflowSequenceKey));
			predicates.add(builder.equal(root.get("intent"), intentId));
			update.where(predicates.toArray(new Predicate[] {}));

			session.createQuery(update).executeUpdate();

			deleteIntentMapping(entryExpression, intentId);

			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}

		return response;
	}

	private void deleteIntentMapping(String entryExpression, Long intentId) throws ApiException {

		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaDelete<IntentMapping> delete = builder.createCriteriaDelete(IntentMapping.class);
			Root<IntentMapping> root = delete.from(IntentMapping.class);

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("entryId"), entryExpression));
			predicates.add(builder.equal(root.get(COL_INTENT_NAME), intentId));

			delete.where(predicates.toArray(new Predicate[] {}));
			session.createQuery(delete).executeUpdate();
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ WorkFlowDbServicesImpl.deleteIntentMapping END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.deleteIntentMapping END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

	}

	private Object getIntentMapping(Long id, String entryType, String entryId) throws ApiException {

		IntentMapping intentMapping = null;

		try (Session session = sessionFactory.openSession()) {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<IntentMapping> select = builder.createQuery(IntentMapping.class);
			Root<IntentMapping> root = select.from(IntentMapping.class);

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get(COL_INTENT_NAME), id));
			predicates.add(builder.equal(root.get("entryType"), entryType));
			predicates.add(builder.equal(root.get("entryId"), entryId));

			select.where(predicates.toArray(new Predicate[] {}));

			List<IntentMapping> mapDetails = session.createQuery(select).getResultList();
			if (!mapDetails.isEmpty())
				intentMapping = mapDetails.get(0);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ WorkFlowDbServicesImpl.getIntentMapping END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getIntentMapping END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return intentMapping;
	}

	private Object updateIntentMapping(BaseRequestModel baseModel, IntentMapping mapDetails) throws ApiException {

		Transaction tx = null;
		String response = ApiConstants.SUCCESS;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<IntentMapping> update = builder.createCriteriaUpdate(IntentMapping.class);
			Root<IntentMapping> root = update.from(IntentMapping.class);
			update.set(root.get("entryType"), baseModel.getWorkflowSequence().getEntryType());
			update.set(root.get("entryId"), baseModel.getWorkflowSequence().getEntryExpression());
			update.set(root.get(COL_INTENT_NAME), baseModel.getWorkflowSequence().getIntentId());
			update.set(root.get("kuId"), baseModel.getWorkflowSequence().getKuId());
			update.set(root.get("workflowId"), baseModel.getWorkflowSequence().getWorkflowId());
			update.set(root.get("required"), baseModel.getWorkflowSequence().getRequired());

			update.where(builder.equal(root.get("id"), mapDetails.getId()));
			session.createQuery(update).executeUpdate();
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}

		return response;
	}

	@Override
	public Object deleteWorkflowSequenceByIntentId(BaseRequestModel baseModel, String id) throws ApiException {

		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaDelete<WorkflowSequence> delete = builder.createCriteriaDelete(WorkflowSequence.class);
			Root<WorkflowSequence> root = delete.from(WorkflowSequence.class);
			delete.where(builder.equal(root.get(COL_INTENT_NAME), id));
			session.createQuery(delete).executeUpdate();
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error(
					"+++++ WorkFlowDbServicesImpl.deleteWorkflowSequenceByIntentId END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error(
					"+++++ WorkFlowDbServicesImpl.deleteWorkflowSequenceByIntentId END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return null;
	}

	@Override
	public Object deleteWorkflowSequence(BaseRequestModel baseModel, String id) throws ApiException {

		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaDelete<WorkflowSequence> delete = builder.createCriteriaDelete(WorkflowSequence.class);
			Root<WorkflowSequence> root = delete.from(WorkflowSequence.class);
			delete.where(builder.equal(root.get("id"), id));
			session.createQuery(delete).executeUpdate();
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error(
					"+++++ WorkFlowDbServicesImpl.deleteWorkflowSequence END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.deleteWorkflowSequence END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return null;
	}

	@Override
	public Object getWorkflowSequenceById(Long id) throws ApiException {

		WorkflowSequence workflowSequence = null;
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<WorkflowSequence> select = builder.createQuery(WorkflowSequence.class);
			Root<WorkflowSequence> root = select.from(WorkflowSequence.class);
			ParameterExpression<Long> workflowSequenceId = builder.parameter(Long.class);
			Predicate workflowSequenceIdP = builder.equal(root.get("id"), workflowSequenceId);
			Predicate and1 = builder.and(workflowSequenceIdP);
			select.where(and1);

			List<WorkflowSequence> workflowSequenceDetails = session.createQuery(select)
					.setParameter(workflowSequenceId, id).getResultList();

			if (!workflowSequenceDetails.isEmpty())
				workflowSequence = workflowSequenceDetails.get(0);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error(
					"+++++ WorkFlowDbServicesImpl.getWorkflowSequenceById END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getWorkflowSequenceById END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return workflowSequence;
	}

	public Object getWorkflowSequenceByKeyandIntentID(String workflowSequenceKey, Long intentId) throws ApiException {

		WorkflowSequence workflowSequence = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<WorkflowSequence> select = builder.createQuery(WorkflowSequence.class);
			Root<WorkflowSequence> root = select.from(WorkflowSequence.class);

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("workflowSequenceKey"), workflowSequenceKey));
			predicates.add(builder.equal(root.get("intent"), intentId));

			select.where(predicates.toArray(new Predicate[] {}));

			List<WorkflowSequence> workflowSequenceDetails = session.createQuery(select).getResultList();

			if (!workflowSequenceDetails.isEmpty())
				workflowSequence = workflowSequenceDetails.get(0);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error(
					"+++++ WorkFlowDbServicesImpl.getWorkflowSequenceById END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getWorkflowSequenceById END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return workflowSequence;
	}

	@Override
	public Object getWorkflowSequenceByIntentId(Long id) throws ApiException {

		List<WorkflowSequence> workflowSequenceDetails = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<WorkflowSequence> select = builder.createQuery(WorkflowSequence.class);
			Root<WorkflowSequence> root = select.from(WorkflowSequence.class);
			ParameterExpression<Long> intentId = builder.parameter(Long.class);
			Predicate intentIdP = builder.equal(root.get(COL_INTENT_NAME), intentId);
			Predicate and1 = builder.and(intentIdP);
			select.where(and1);

			workflowSequenceDetails = session.createQuery(select).setParameter(intentId, id).getResultList();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error(
					"+++++ WorkFlowDbServicesImpl.getWorkflowSequenceByIntentId END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ WorkFlowDbServicesImpl.getWorkflowSequenceByIntentId END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return workflowSequenceDetails;
	}

	@Override
	public Object getWorkflowSequenceByWorkflowId(Long id) throws ApiException {

		List<WorkflowSequence> workflowSequenceDetails = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<WorkflowSequence> select = builder.createQuery(WorkflowSequence.class);
			Root<WorkflowSequence> root = select.from(WorkflowSequence.class);
			ParameterExpression<Long> workflowId = builder.parameter(Long.class);
			Predicate workflowIdP = builder.equal(root.get("workflowId"), workflowId);
			Predicate and1 = builder.and(workflowIdP);
			select.where(and1);

			workflowSequenceDetails = session.createQuery(select).setParameter(workflowId, id).getResultList();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error(
					"+++++ WorkFlowDbServicesImpl.getWorkflowSequenceByWorkflowId END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error(
					"+++++ WorkFlowDbServicesImpl.getWorkflowSequenceByWorkflowId END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return workflowSequenceDetails;
	}

	@Override
	public Object updatePrimaryDestinationKey(Long workflowSequenceId, String flag) throws ApiException {

		Transaction tx = null;
		String response = ApiConstants.SUCCESS;
		String primaryDestinationkey = null;
		String secondaryDestinationkey = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();

			WorkflowSequence workflowSequence = (WorkflowSequence) getWorkflowSequenceById(workflowSequenceId);

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<WorkflowSequence> update = builder.createCriteriaUpdate(WorkflowSequence.class);
			Root<WorkflowSequence> root = update.from(WorkflowSequence.class);

			update.set(root.get(COL_INTENT_NAME), workflowSequence.getIntent().getId());
			update.set(root.get("workflowId"), workflowSequence.getWorkflowId());
			update.set(root.get("workflowSequenceKey"), workflowSequence.getWorkflowSequenceKey());
			update.set(root.get("entryType"), workflowSequence.getEntryType());
			update.set(root.get("entryExpression"), workflowSequence.getEntryExpression());
			update.set(root.get("primaryDestWorkflowId"), workflowSequence.getPrimaryDestWorkflowId());
			if (flag.equals("Y")) {
				update.set(root.get("primaryDestSequenceKey"), primaryDestinationkey);
				update.set(root.get("secondaryDestSequenceKey"), workflowSequence.getSecondaryDestSequenceKey());
			} else {
				update.set(root.get("primaryDestSequenceKey"), workflowSequence.getPrimaryDestSequenceKey());
				update.set(root.get("secondaryDestSequenceKey"), secondaryDestinationkey);
			}
			update.set(root.get("secondaryDestWorkflowId"), workflowSequence.getSecondaryDestWorkflowId());
			update.set(root.get("terminalType"), workflowSequence.getTerminalType());
			update.set(root.get("required"), workflowSequence.getRequired());
			update.set(root.get("initialValidation"), "Y");
			update.where(builder.equal(root.get("id"), workflowSequenceId));

			session.createQuery(update).executeUpdate();

			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}

		return response;
	}

	@Override
	public Object updateLink(String workflowSequenceKey, Long intentId, String flag) throws ApiException {

		Transaction tx = null;
		String response = ApiConstants.SUCCESS;
		String primaryDestinationkey = null;
		String secondaryDestinationkey = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();

			WorkflowSequence workflowSequence = (WorkflowSequence) getWorkflowSequenceByKeyandIntentID(
					workflowSequenceKey, intentId);

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<WorkflowSequence> update = builder.createCriteriaUpdate(WorkflowSequence.class);
			Root<WorkflowSequence> root = update.from(WorkflowSequence.class);

			update.set(root.get(COL_INTENT_NAME), workflowSequence.getIntent().getId());
			update.set(root.get("workflowId"), workflowSequence.getWorkflowId());
			update.set(root.get("workflowSequenceKey"), workflowSequence.getWorkflowSequenceKey());
			update.set(root.get("entryType"), workflowSequence.getEntryType());
			update.set(root.get("entryExpression"), workflowSequence.getEntryExpression());
			update.set(root.get("primaryDestWorkflowId"), workflowSequence.getPrimaryDestWorkflowId());
			if (flag.equals("Y")) {
				update.set(root.get("primaryDestSequenceKey"), primaryDestinationkey);
				update.set(root.get("secondaryDestSequenceKey"), workflowSequence.getSecondaryDestSequenceKey());
			} else {
				update.set(root.get("primaryDestSequenceKey"), workflowSequence.getPrimaryDestSequenceKey());
				update.set(root.get("secondaryDestSequenceKey"), secondaryDestinationkey);
			}
			update.set(root.get("secondaryDestWorkflowId"), workflowSequence.getSecondaryDestWorkflowId());
			update.set(root.get("terminalType"), workflowSequence.getTerminalType());
			update.set(root.get("required"), workflowSequence.getRequired());
			update.set(root.get("initialValidation"), "Y");

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("workflowSequenceKey"), workflowSequenceKey));
			predicates.add(builder.equal(root.get("intent"), intentId));
			update.where(predicates.toArray(new Predicate[] {}));

			session.createQuery(update).executeUpdate();

			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}
		return response;
	}

}
