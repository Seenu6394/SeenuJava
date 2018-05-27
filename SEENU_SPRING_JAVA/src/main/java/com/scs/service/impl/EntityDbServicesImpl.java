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

import com.scs.entity.model.Action;
import com.scs.entity.model.ActionExtn;

import com.scs.entity.model.EntityDetails;
import com.scs.entity.model.EntityQuestion;
import com.scs.entity.model.EntityRegex;
import com.scs.entity.model.EntityTypeDetails;
import com.scs.entity.model.ErrorResponse;

import com.scs.entity.model.IntentMapping;
import com.scs.entity.model.WorkflowSequence;
import com.scs.exception.ApiException;
import com.scs.model.ActionExtnModel;
import com.scs.model.BaseRequestModel;
import com.scs.model.EntityQuestionModel;
import com.scs.model.ErrorResponseModel;
import com.scs.model.RegExModel;
import com.scs.service.EntityDbServices;
import com.scs.util.ApiConstants;
import com.scs.util.Utility;

@Service("EntityDbService")

public class EntityDbServicesImpl implements EntityDbServices {

	private static final Logger logger = Logger.getLogger(EntityDbServicesImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Object getEntityDetails(BaseRequestModel baseModel) throws ApiException {

		List<EntityDetails> entityLst = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<EntityDetails> query = builder.createQuery(EntityDetails.class);
			Root<EntityDetails> root = query.from(EntityDetails.class);
			query.select(root);
			query.orderBy(builder.desc(root.get("id")));
			entityLst = session.createQuery(query).getResultList();
		} catch (HibernateException ex) {
			logger.error("+++++ EntityServicesImpl.getEntityDetails END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++  EntityServicesImpl.getEntityDetails END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return entityLst;
	}

	@Override
	public Object createEntity(BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;
		EntityDetails entity = null;
		Instant instant = Instant.now();

		try (Session session = sessionFactory.openSession()) {

			tx = session.beginTransaction();
			entity = new EntityDetails();
			entity.setDate(instant.toString());
			entity.setName(baseModel.getEntity().getName().toLowerCase());
			entity.setEntityType(baseModel.getEntity().getEntityType());
			entity.setKuId(baseModel.getEntity().getKuId());
			entity.setRequired(baseModel.getEntity().getRequired());
			entity.setGlobalIdentifier(baseModel.getEntity().getGlobalIdentifier());
			entity.setIntentId(baseModel.getEntity().getIntentId());
			entity.setDataType(baseModel.getEntity().getDataType());
			session.save(entity);

			for (EntityQuestionModel questionModel : baseModel.getEntity().getQuestions()) {
				EntityQuestion question = new EntityQuestion();
				question.setEntity(entity);
				question.setQuestion(questionModel.getQuestion());
				question.setExample(questionModel.getExample());
				question.setLocaleCode(questionModel.getLocaleCode());
				question.setTitle(questionModel.getTitle());
				question.setButtonText(questionModel.getButtonText());
				question.setSubTitle(questionModel.getSubTitle());
				question.setImageUrl(questionModel.getImageUrl());
				entity.getQuestions().add(question);
				session.save(question);

			}

			if (baseModel.getEntity().getAction() != null
					&& (!baseModel.getEntity().getAction().getActionExtn().isEmpty())) {
				Action action = new Action();

				action.setName(baseModel.getEntity().getAction().getName());
				action.setCallingInterval(baseModel.getEntity().getAction().getCallingInterval());
				action.setGlobalIdentifier(baseModel.getEntity().getAction().getGlobalIdentifier());
				action.setEntity(entity);
				action.setWarningMessage(baseModel.getEntity().getAction().getWarningMessage());
				session.save(action);
				List<ErrorResponse> errorResponseLst = new ArrayList<>();
				if (!baseModel.getEntity().getAction().getErrorResponses().isEmpty()) {
					for (ErrorResponseModel errorResponseModel : baseModel.getEntity().getAction()
							.getErrorResponses()) {

						ErrorResponse errorResponse = new ErrorResponse();
						errorResponse.setErrorCode(errorResponseModel.getErrorCode());
						errorResponse.setAction(action);
						errorResponse.setLocaleCode(errorResponseModel.getLocaleCode());
						errorResponse.setKuId(baseModel.getEntity().getKuId());
						errorResponse.setErrorResponse(errorResponseModel.getErrorResponse());
						session.save(errorResponse);
						errorResponseLst.add(errorResponse);
					}
				}
				List<ActionExtn> actionExtnLst = new ArrayList<>();
				for (ActionExtnModel actionExtnModel : baseModel.getEntity().getAction().getActionExtn()) {
					ActionExtn actionExtn = new ActionExtn();
					actionExtn.setAction(action);
					actionExtn.setCallMethod(actionExtnModel.getCallMethod());
					actionExtn.setErrorCode(actionExtnModel.getErrorCode());
					actionExtn.setLocaleCode(actionExtnModel.getLocaleCode());
					actionExtn.setRequestBody(actionExtnModel.getRequestBody());
					actionExtn.setResponsePath(actionExtnModel.getResponsePath());
					actionExtn.setSuccessCode(actionExtnModel.getSuccessCode());
					actionExtn.setUrl(actionExtnModel.getUrl());
					session.save(actionExtn);
					actionExtnLst.add(actionExtn);
				}
				action.setErrorResponses(errorResponseLst);
				action.setActionExtn(actionExtnLst);

				entity.setAction(action);
			}

			if (baseModel.getEntity().getRegex() != null) {
				for (RegExModel regex : baseModel.getEntity().getRegex()) {
					EntityRegex entityRegex = new EntityRegex();

					entityRegex.setEntityId(entity.getId());
					entityRegex.setRegexId(regex.getId());
					session.save(entityRegex);
				}
			}
			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ EntityServicesImpl.createEntity END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ EntityServicesImpl.createEntity END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return entity;
	}

	public Object createEntityAction(BaseRequestModel baseModel, EntityDetails entity) throws ApiException {

		Action action = new Action();

		try (Session session = sessionFactory.openSession()) {

			action.setName(baseModel.getEntity().getAction().getName());
			action.setCallingInterval(baseModel.getEntity().getAction().getCallingInterval());
			action.setGlobalIdentifier(baseModel.getEntity().getAction().getGlobalIdentifier());
			action.setEntity(entity);
			action.setWarningMessage(baseModel.getEntity().getAction().getWarningMessage());
			session.save(action);
			List<ErrorResponse> errorResponseLst = new ArrayList<>();
			if (!baseModel.getEntity().getAction().getErrorResponses().isEmpty()) {
				for (ErrorResponseModel errorResponseModel : baseModel.getEntity().getAction().getErrorResponses()) {

					ErrorResponse errorResponse = new ErrorResponse();
					errorResponse.setErrorCode(errorResponseModel.getErrorCode());
					errorResponse.setAction(action);
					errorResponse.setLocaleCode(errorResponseModel.getLocaleCode());
					errorResponse.setKuId(baseModel.getEntity().getKuId());
					errorResponse.setErrorResponse(errorResponseModel.getErrorResponse());
					session.save(errorResponse);
					errorResponseLst.add(errorResponse);
				}
			}
			List<ActionExtn> actionExtnLst = new ArrayList<>();
			for (ActionExtnModel actionExtnModel : baseModel.getEntity().getAction().getActionExtn()) {
				ActionExtn actionExtn = new ActionExtn();
				actionExtn.setAction(action);
				actionExtn.setCallMethod(actionExtnModel.getCallMethod());
				actionExtn.setErrorCode(actionExtnModel.getErrorCode());
				actionExtn.setLocaleCode(actionExtnModel.getLocaleCode());
				actionExtn.setRequestBody(actionExtnModel.getRequestBody());
				actionExtn.setResponsePath(actionExtnModel.getResponsePath());
				actionExtn.setSuccessCode(actionExtnModel.getSuccessCode());
				actionExtn.setUrl(actionExtnModel.getUrl());
				session.save(actionExtn);
				actionExtnLst.add(actionExtn);
			}
			action.setErrorResponses(errorResponseLst);
			action.setActionExtn(actionExtnLst);

		} catch (HibernateException ex) {
			logger.error("+++++ EntityServicesImpl.createEntityQuestion END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ EntityServicesImpl.createEntityQuestion END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return action;
	}

	@Override
	public Object createEntityQuestion(BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;
		EntityQuestion question = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();

			for (EntityQuestionModel entityQuestionModel : baseModel.getEntityQuestion()) {

				EntityQuestion questions = new EntityQuestion();
				questions.setQuestion(entityQuestionModel.getQuestion().toLowerCase());
				questions.setEntity(entityQuestionModel.getEntity());
				questions.setExample(entityQuestionModel.getExample());
				questions.setLocaleCode(entityQuestionModel.getLocaleCode());
				questions.setTitle(entityQuestionModel.getTitle());
				questions.setButtonText(entityQuestionModel.getButtonText());
				questions.setSubTitle(entityQuestionModel.getSubTitle());
				questions.setImageUrl(entityQuestionModel.getImageUrl());
				session.save(questions);
			}

			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ EntityServicesImpl.createEntityQuestion END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ EntityServicesImpl.createEntityQuestion END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return question;
	}

	@Override
	public Object updateEntity(BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<EntityDetails> update = builder.createCriteriaUpdate(EntityDetails.class);
			Root<EntityDetails> root = update.from(EntityDetails.class);

			update.set(root.get("name"), baseModel.getEntity().getName());
			update.set(root.get("kuId"), baseModel.getEntity().getKuId());
			update.set(root.get("entityType"), baseModel.getEntity().getEntityType());
			update.set(root.get("required"), baseModel.getEntity().getRequired());
			update.set(root.get("globalIdentifier"), baseModel.getEntity().getGlobalIdentifier());
			update.set(root.get("dataType"), baseModel.getEntity().getDataType());

			update.where(builder.equal(root.get("id"), baseModel.getEntity().getId()));
			session.createQuery(update).executeUpdate();

			updateEntityExample(session, baseModel);
			updateEntityAction(session, baseModel);

			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ EntityServicesImpl.updateEntity END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ EntityServicesImpl.updateEntity END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return ApiConstants.SUCCESS;
	}

	private void updateEntityExample(Session session, BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;

		List<EntityQuestion> entQuestionLst = null;

		entQuestionLst = (List<EntityQuestion>) getEntityQuestionsById(baseModel.getEntity().getId());
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();

			for (EntityQuestion entQues : entQuestionLst) {

				if (entQues.getLocaleCode().equals("ar")) {

					CriteriaBuilder builder = session.getCriteriaBuilder();
					CriteriaUpdate<EntityQuestion> update = builder.createCriteriaUpdate(EntityQuestion.class);
					Root<EntityQuestion> root = update.from(EntityQuestion.class);

					update.set(root.get("example"), baseModel.getEntity().getArExample());
					update.set(root.get("title"), baseModel.getEntity().getTitle());
					update.set(root.get("buttonText"), baseModel.getEntity().getButtonText());
					update.set(root.get("subTitle"), baseModel.getEntity().getSubTitle());
					update.set(root.get("imageUrl"), baseModel.getEntity().getImageUrl());

					update.where(builder.equal(root.get("id"), entQues.getId()));
					session.createQuery(update).executeUpdate();

				} else if (entQues.getLocaleCode().equals("en")) {

					CriteriaBuilder builder1 = session.getCriteriaBuilder();
					CriteriaUpdate<EntityQuestion> update1 = builder1.createCriteriaUpdate(EntityQuestion.class);
					Root<EntityQuestion> root1 = update1.from(EntityQuestion.class);

					update1.set(root1.get("example"), baseModel.getEntity().getEngExample());
					update1.set(root1.get("title"), baseModel.getEntity().getTitle());
					update1.set(root1.get("buttonText"), baseModel.getEntity().getButtonText());
					update1.set(root1.get("subTitle"), baseModel.getEntity().getSubTitle());
					update1.set(root1.get("imageUrl"), baseModel.getEntity().getImageUrl());

					update1.where(builder1.equal(root1.get("id"), entQues.getId()));
					session.createQuery(update1).executeUpdate();

				}

			}

			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ UPDATEEntityServicesImpl.updateEntity END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ UPDATEEntityServicesImpl.updateEntity END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	private void updateEntityAction(Session session, BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;

		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<Action> update = builder.createCriteriaUpdate(Action.class);
			Root<Action> root = update.from(Action.class);

			if (baseModel.getEntity().getAction() != null) {

				update.set(root.get("name"), baseModel.getEntity().getAction().getName());
				update.set(root.get("kuId"), baseModel.getEntity().getAction().getKuId());
				update.set(root.get("callingInterval"), baseModel.getEntity().getAction().getCallingInterval());
				update.set(root.get("warningMessage"), baseModel.getEntity().getAction().getWarningMessage());
				update.set(root.get("globalIdentifier"), baseModel.getEntity().getAction().getGlobalIdentifier());
				update.set(root.get("entity"), baseModel.getEntity().getId());
				update.set(root.get("dataType"), baseModel.getEntity().getDataType());

				update.where(builder.equal(root.get("id"), baseModel.getEntity().getAction().getId()));
				session.createQuery(update).executeUpdate();

				updateActionExtn(session, baseModel);
				updateErrorResponse(session, baseModel);

				tx.commit();
			}
		} catch (HibernateException ex) {
			logger.error("+++++ UPDATEEntityServicesImpl.updateEntity END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ UPDATEEntityServicesImpl.updateEntity END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	private void updateErrorResponse(Session session, BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;

		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();

			for (ErrorResponseModel errorRespModel : baseModel.getEntity().getAction().getErrorResponses()) {

				if (errorRespModel.getId() != null) {
					CriteriaBuilder builder = session.getCriteriaBuilder();
					CriteriaUpdate<ErrorResponse> update = builder.createCriteriaUpdate(ErrorResponse.class);
					Root<ErrorResponse> root = update.from(ErrorResponse.class);

					update.set(root.get("errorCode"), errorRespModel.getErrorCode());
					update.set(root.get("errorResponse"), errorRespModel.getErrorResponse());
					update.set(root.get("action"), errorRespModel.getActionId());
					update.set(root.get("localeCode"), errorRespModel.getLocaleCode());
					update.set(root.get("kuId"), errorRespModel.getKuId());

					update.where(builder.equal(root.get("id"), errorRespModel.getId()));
					session.createQuery(update).executeUpdate();

				} else {
					ErrorResponse errorResponse = new ErrorResponse();
					errorResponse.setErrorCode(errorRespModel.getErrorCode());
					Action action = new Action();
					action.setId(baseModel.getEntity().getAction().getId());
					errorResponse.setAction(action);
					errorResponse.setLocaleCode(errorRespModel.getLocaleCode());
					errorResponse.setKuId(baseModel.getEntity().getKuId());
					errorResponse.setErrorResponse(errorRespModel.getErrorResponse());

					session.save(errorResponse);

				}
			}

			tx.commit();

		} catch (HibernateException ex) {
			logger.error(
					"+++++ UPDATEEntityServicesImpl.updateErrorResponse END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ UPDATEEntityServicesImpl.updateErrorResponse END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	private void updateActionExtn(Session session, BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();

			if (baseModel.getEntity().getAction() != null) {

				for (ActionExtnModel actionExtnModel : baseModel.getEntity().getAction().getActionExtn()) {

					if (actionExtnModel.getId() != null) {
						CriteriaBuilder builder = session.getCriteriaBuilder();
						CriteriaUpdate<ActionExtn> update = builder.createCriteriaUpdate(ActionExtn.class);
						Root<ActionExtn> root = update.from(ActionExtn.class);

						update.set(root.get("url"), actionExtnModel.getUrl());
						update.set(root.get("callMethod"), actionExtnModel.getCallMethod());
						update.set(root.get("requestBody"), actionExtnModel.getRequestBody());
						update.set(root.get("successCode"), actionExtnModel.getSuccessCode());
						update.set(root.get("errorCode"), actionExtnModel.getErrorCode());
						update.set(root.get("action"), baseModel.getEntity().getAction().getId());
						update.set(root.get("responsePath"), actionExtnModel.getResponsePath());

						update.where(builder.equal(root.get("id"), actionExtnModel.getId()));
						session.createQuery(update).executeUpdate();
					} else {
						ActionExtn actionExtn = new ActionExtn();

						Action action = new Action();
						action.setId(baseModel.getEntity().getAction().getId());

						actionExtn.setAction(action);
						actionExtn.setCallMethod(actionExtnModel.getCallMethod());
						actionExtn.setErrorCode(actionExtnModel.getErrorCode());
						actionExtn.setLocaleCode(actionExtnModel.getLocaleCode());
						actionExtn.setRequestBody(actionExtnModel.getRequestBody());
						actionExtn.setResponsePath(actionExtnModel.getResponsePath());
						actionExtn.setSuccessCode(actionExtnModel.getSuccessCode());
						actionExtn.setUrl(actionExtnModel.getUrl());
						session.save(actionExtn);

					}
				}
			}
			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ UPDATEEntityServicesImpl.updateActionExtn END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ UPDATEEntityServicesImpl.updateActionExtn END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	public Object updateActionExtnValues(BaseRequestModel baseModel, ActionExtnModel actionExtnModel)
			throws ApiException {

		try (Session session = sessionFactory.openSession()) {

			if (actionExtnModel.getId() != null) {
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaUpdate<ActionExtn> update = builder.createCriteriaUpdate(ActionExtn.class);
				Root<ActionExtn> root = update.from(ActionExtn.class);

				update.set(root.get("url"), actionExtnModel.getUrl());
				update.set(root.get("callMethod"), actionExtnModel.getCallMethod());
				update.set(root.get("requestBody"), actionExtnModel.getRequestBody());
				update.set(root.get("successCode"), actionExtnModel.getSuccessCode());
				update.set(root.get("errorCode"), actionExtnModel.getErrorCode());
				update.set(root.get("action"), baseModel.getEntity().getAction().getId());
				update.set(root.get("responsePath"), actionExtnModel.getResponsePath());

				update.where(builder.equal(root.get("id"), actionExtnModel.getId()));
				session.createQuery(update).executeUpdate();
			} else {
				ActionExtn actionExtn = new ActionExtn();

				Action action = new Action();
				action.setId(baseModel.getEntity().getAction().getId());

				actionExtn.setAction(action);
				actionExtn.setCallMethod(actionExtnModel.getCallMethod());
				actionExtn.setErrorCode(actionExtnModel.getErrorCode());
				actionExtn.setLocaleCode(actionExtnModel.getLocaleCode());
				actionExtn.setRequestBody(actionExtnModel.getRequestBody());
				actionExtn.setResponsePath(actionExtnModel.getResponsePath());
				actionExtn.setSuccessCode(actionExtnModel.getSuccessCode());
				actionExtn.setUrl(actionExtnModel.getUrl());
				session.save(actionExtn);

			}
		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}

		return actionExtnModel;

	}

	@Override
	public Object getEntityQuestionsById(Long entityId) throws ApiException {
		
		List<EntityQuestion> questionDetails = null;

		try (Session session = sessionFactory.openSession()) {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<EntityQuestion> select = builder.createQuery(EntityQuestion.class);
			Root<EntityQuestion> root = select.from(EntityQuestion.class);
			EntityDetails entity = new EntityDetails();
			entity.setId(entityId);
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("entity"), entity));

			select.where(predicates.toArray(new Predicate[] {}));

			questionDetails = session.createQuery(select).getResultList();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		} 

		return questionDetails;
	}

	@Override
	public Object deleteEntity(BaseRequestModel baseModel, String id) throws ApiException {
	
		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {

			tx = session.beginTransaction();
			deleteWorkflowSequence(id);
			deleteIntentMapping(id);

			EntityDetails entity = new EntityDetails();
			entity.setId(Long.parseLong(id));
			session.delete(entity);
			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ EntityServicesImpl.deleteEntity END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (ApiException ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error("+++++ EntityServicesImpl.deleteEntity END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} 

		return ApiConstants.SUCCESS;
	}

	@Override
	public Object deleteEntityQuestion(BaseRequestModel baseModel, String id) throws ApiException {
		
		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			EntityQuestion question = new EntityQuestion();
			question.setId(Long.parseLong(id));
			session.delete(question);
			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ EntityServicesImpl.deleteEntityQuestion END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ EntityServicesImpl.deleteEntityQuestion END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return ApiConstants.SUCCESS;
	}

	@Override
	public Object getEntityById(BaseRequestModel baseModel, String id) throws ApiException {


		EntityDetails entity = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<EntityDetails> select = builder.createQuery(EntityDetails.class);
			Root<EntityDetails> root = select.from(EntityDetails.class);
			ParameterExpression<Long> entityId = builder.parameter(Long.class);
			Predicate entityIdP = builder.equal(root.get("id"), entityId);
			Predicate and1 = builder.and(entityIdP);
			select.where(and1);

			List<EntityDetails> entityDetails = session.createQuery(select).setParameter(entityId, Long.parseLong(id))
					.getResultList();

			entity = entityDetails.get(0);

		} catch (HibernateException ex) {
			logger.error("+++++ EntityServicesImpl.getEntityById END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ EntityServicesImpl.getEntityById END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} 

		return entity;
	}

	@Override
	public Object getEntityTypeDetails(BaseRequestModel baseModel) throws ApiException {
	
		List<EntityTypeDetails> entityTypeLst = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<EntityTypeDetails> query = builder.createQuery(EntityTypeDetails.class);
			Root<EntityTypeDetails> root = query.from(EntityTypeDetails.class);
			query.select(root);
			query.orderBy(builder.desc(root.get("id")));
			entityTypeLst = session.createQuery(query).getResultList();

		} catch (HibernateException ex) {
			logger.error("+++++ EntityServicesImpl.getEntityTypeDetails END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ EntityServicesImpl.getEntityTypeDetails END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} 

		return entityTypeLst;
	}

	@Override
	public Object getEntityByKu(BaseRequestModel baseModel, String id) throws ApiException {
		

		List<EntityDetails> entityDetails = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<EntityDetails> select = builder.createQuery(EntityDetails.class);
			Root<EntityDetails> root = select.from(EntityDetails.class);
			ParameterExpression<Long> kuId = builder.parameter(Long.class);
			Predicate entityIdP = builder.equal(root.get("kuId"), kuId);
			Predicate and1 = builder.and(entityIdP);
			select.where(and1);

			entityDetails = session.createQuery(select).setParameter(kuId, Long.parseLong(id)).getResultList();

		} catch (HibernateException ex) {
			logger.error("+++++ EntityServicesImpl.getEntityByKu END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ EntityServicesImpl.getEntityByKu END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} 

		return entityDetails;
	}

	private void deleteWorkflowSequence(String id) throws ApiException {

		
		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaDelete<WorkflowSequence> delete = builder.createCriteriaDelete(WorkflowSequence.class);
			Root<WorkflowSequence> root = delete.from(WorkflowSequence.class);

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("entryExpression"), id));
			predicates.add(builder.equal(root.get("entryType"), "ENTITY"));

			delete.where(predicates.toArray(new Predicate[] {}));
			session.createQuery(delete).executeUpdate();
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error(
					"+++++ IntentDbServicesImpl.deleteWorkflowSequence END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.deleteWorkflowSequence END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}
	}

	private void deleteIntentMapping(String id) throws ApiException {

		
		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaDelete<IntentMapping> delete = builder.createCriteriaDelete(IntentMapping.class);
			Root<IntentMapping> root = delete.from(IntentMapping.class);

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("entryId"), id));
			predicates.add(builder.equal(root.get("entryType"), "ENTITY"));

			delete.where(predicates.toArray(new Predicate[] {}));
			session.createQuery(delete).executeUpdate();
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ IntentDbServicesImpl.deleteIntentMapping END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.deleteIntentMapping END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		} 

	}

}
