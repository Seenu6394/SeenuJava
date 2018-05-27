package com.scs.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.scs.entity.model.Confirm;
import com.scs.entity.model.EntityDetails;
import com.scs.entity.model.ErrorResponse;
import com.scs.entity.model.Intent;
import com.scs.entity.model.IntentMapping;

import com.scs.entity.model.WorkflowSequence;
import com.scs.exception.ApiException;

import com.scs.model.ActionExtnModel;
import com.scs.model.ActionModel;
import com.scs.model.BaseRequestModel;
import com.scs.model.ConfirmationModel;
import com.scs.model.ErrorResponseModel;

import com.scs.service.SADbServices;

import com.scs.util.Utility;

@Service("SADbService")

public class SADbServicesImpl implements SADbServices {

	private static final Logger logger = Logger.getLogger(SADbServicesImpl.class);
	private static final String INTERVAL_MES = "ACTION_CALL_INTERVAL_ERRMSG";
	

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Object getServiceActionDetails(BaseRequestModel baseModel) throws ApiException {
		
		List<Action> actionLst = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Action> query = builder.createQuery(Action.class);
			Root<Action> root = query.from(Action.class);
			query.select(root);
			query.orderBy(builder.desc(root.get("id")));
			actionLst = session.createQuery(query).getResultList();

		} catch (HibernateException ex) {
			logger.error("+++++ SADbServicesImpl.getServiceActionDetails END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ SADbServicesImpl.getServiceActionDetails END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} 
		return actionLst;
	}

	@Override
	public Object createServiceAction(BaseRequestModel baseModel) throws ApiException {
		
		Transaction tx = null;
		Action action = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			List<Confirm> confirmArr = new ArrayList<>();

			action = new Action();

			action.setName(baseModel.getAction().getName().toLowerCase());
			action.setCallingInterval(baseModel.getAction().getCallingInterval());
			action.setWarningMessage(INTERVAL_MES);
			action.setGlobalIdentifier(baseModel.getAction().getGlobalIdentifier());
			Intent intent = new Intent();
			intent.setId(baseModel.getAction().getIntentId());
			action.setIntent(intent);
			action.setDataType(baseModel.getAction().getDataType());
			if (baseModel.getAction().getEntityId() != null) {
				EntityDetails entity = new EntityDetails();
				entity.setId(baseModel.getAction().getEntityId());
				action.setEntity(entity);
			}
			session.save(action);

			List<ActionExtn> actExtn = new ArrayList<>();
			for (ActionExtnModel actionExtnModel : baseModel.getAction().getActionExtn()) {
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
				actExtn.add(actionExtn);
			}

			action.setActionExtn(actExtn);
			if (!baseModel.getConfirm().isEmpty()) {
				for (ConfirmationModel confirmModel : baseModel.getConfirm()) {
					Confirm confirm = new Confirm();
					confirm.setText(confirmModel.getText());
					confirm.setConfirmationType(confirmModel.getConfirmationType());
					confirm.setConfirmationOption(confirmModel.getConfirmationOption());
					confirm.setUnConfirmationOption(confirmModel.getUnConfirmationOption());
					confirm.setKuId(confirmModel.getKuId());
					confirm.setLocaleCode(confirmModel.getLocaleCode());
					confirm.setTerminationText(confirmModel.getTerminationText());
					confirm.setAction(action);
					session.save(confirm);
					confirmArr.add(confirm);

				}
			}
			action.setConfirm(confirmArr);

			List<ErrorResponse> errResponse = new ArrayList<>();

			for (ErrorResponseModel errorResponse : baseModel.getAction().getErrorResponses()) {
				ErrorResponse erResponses = new ErrorResponse();

				erResponses.setErrorCode(errorResponse.getErrorCode());
				erResponses.setErrorResponse(errorResponse.getErrorResponse());
				erResponses.setLocaleCode(errorResponse.getLocaleCode());
				erResponses.setKuId(baseModel.getAction().getKuId());
				erResponses.setAction(action);
				session.save(erResponses);
				errResponse.add(erResponses);

			}
			action.setErrorResponses(errResponse);

			
			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ SADbServicesImpl.createServiceAction END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ SADbServicesImpl.createServiceAction END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return action;
	}

	@Override
	public Object createErrorResponse(BaseRequestModel baseModel) throws ApiException {
		
		Transaction tx = null;
		List<ErrorResponse> errResponse = new ArrayList<>();

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();

			for (ErrorResponseModel errorResponseModel : baseModel.getAction().getErrorResponses()) {
				ErrorResponse errorResponse = new ErrorResponse();
				if (errorResponseModel.getId() == null) {

					errorResponse.setErrorCode(errorResponseModel.getErrorCode());
					errorResponse.setErrorResponse(errorResponseModel.getErrorResponse());
					errorResponse.setLocaleCode(errorResponseModel.getLocaleCode());
					errorResponse.setKuId(errorResponseModel.getKuId());
					Action action = new Action();
					action.setId(baseModel.getAction().getId());
					errorResponse.setAction(action);

					session.save(errorResponse);
				} else {
					errorResponse.setId(errorResponseModel.getId());
					errorResponse.setErrorCode(errorResponseModel.getErrorCode());
					errorResponse.setErrorResponse(errorResponseModel.getErrorResponse());
					errorResponse.setLocaleCode(errorResponseModel.getLocaleCode());
					errorResponse.setKuId(errorResponseModel.getKuId());
					Action action = new Action();
					action.setId(baseModel.getAction().getId());
					errorResponse.setAction(action);
				}
				errResponse.add(errorResponse);
			}

			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ SADbServicesImpl.createErrorResponse END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ SADbServicesImpl.createErrorResponse END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return errResponse;
	}

	@Override
	public Object updateServiceAction(BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;
		ActionModel action = new ActionModel();

		
		List<ErrorResponseModel> errResponse = new ArrayList<>();
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<Action> update = builder.createCriteriaUpdate(Action.class);
			Root<Action> root = update.from(Action.class);

			if (baseModel.getAction().getName() != null) {
				update.set(root.get("name"), baseModel.getAction().getName());
			}

			update.set(root.get("kuId"), baseModel.getAction().getKuId());
			update.set(root.get("callingInterval"), baseModel.getAction().getCallingInterval());
			update.set(root.get("warningMessage"), INTERVAL_MES);
			update.set(root.get("globalIdentifier"), baseModel.getAction().getGlobalIdentifier());
			update.set(root.get("intent"), baseModel.getAction().getIntentId());
			update.set(root.get("entity"), baseModel.getAction().getEntityId());
			update.set(root.get("dataType"), baseModel.getAction().getDataType());
			update.where(builder.equal(root.get("id"), baseModel.getAction().getId()));
			session.createQuery(update).executeUpdate();
			
			updateActionExtn(session, baseModel);

			if (!baseModel.getConfirm().isEmpty()) {

				updateConfirmAction(session, baseModel);

			}

			errResponse = (List<ErrorResponseModel>) createErrorResponse(baseModel);
			if (baseModel.getAction().getName() != null) {

				action.setId(baseModel.getAction().getId());
				action.setName(baseModel.getAction().getName());
				action.setCallingInterval(baseModel.getAction().getCallingInterval());
				action.setWarningMessage(INTERVAL_MES);
				action.setGlobalIdentifier(baseModel.getAction().getGlobalIdentifier());
				action.setIntentId(baseModel.getAction().getIntentId());
				action.setEntityId(baseModel.getAction().getEntityId());
				action.setErrorResponses(errResponse);
				action.setDataType(baseModel.getAction().getDataType());
				action.setActionExtn(baseModel.getAction().getActionExtn());
				action.setConfirm(baseModel.getAction().getConfirm());
			}
			
			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ SADbServicesImpl.updateServiceAction END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ SADbServicesImpl.updateServiceAction END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} 
		return action;
	}

	@Override
	public Object updateSAIntent(BaseRequestModel baseModel) throws ApiException {
		
		Transaction tx = null;
		Action action = new Action();
		Intent intent = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();

			action = (Action) getServiceActionById(baseModel, baseModel.getAction().getId().toString());

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<Action> update = builder.createCriteriaUpdate(Action.class);
			Root<Action> root = update.from(Action.class);
			update.set(root.get("name"), action.getName());

			update.set(root.get("callingInterval"), action.getCallingInterval());
			update.set(root.get("warningMessage"), INTERVAL_MES);
			update.set(root.get("globalIdentifier"), action.getGlobalIdentifier());
		
			update.set(root.get("intent"), intent);

			update.where(builder.equal(root.get("id"), action.getId()));
			session.createQuery(update).executeUpdate();

			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ SADbServicesImpl.updateServiceAction END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ SADbServicesImpl.updateServiceAction END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} 
		return action;
	}

	@Override
	public Object deleteServiceAction(BaseRequestModel baseModel, String id) throws ApiException {
	
		Transaction tx = null;

	
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaDelete<Action> delete = builder.createCriteriaDelete(Action.class);
			Root<Action> root = delete.from(Action.class);
			delete.where(builder.equal(root.get("id"), id));
			session.createQuery(delete).executeUpdate();
			tx.commit();
			
			deleteWorkflowSequence(id);
			deleteIntentMapping(id);
			

		} catch (HibernateException ex) {
			logger.error("+++++ SADbServicesImpl.deleteServiceAction END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ SADbServicesImpl.deleteServiceAction END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} 

		return null;
	}
	
private void deleteWorkflowSequence(String id) throws ApiException{
		
		
		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaDelete<WorkflowSequence> delete = builder.createCriteriaDelete(WorkflowSequence.class);
			Root<WorkflowSequence> root = delete.from(WorkflowSequence.class);
			
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("entryExpression"), id));
			predicates.add(builder.equal(root.get("entryType"), "ACTION"));

			delete.where(predicates.toArray(new Predicate[] {}));
			session.createQuery(delete).executeUpdate();
			tx.commit();
			
	
		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ IntentDbServicesImpl.deleteWorkflowSequence END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.deleteWorkflowSequence END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		} 
		
	}
	
	
private void deleteIntentMapping(String id) throws ApiException{
		
		
		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaDelete<IntentMapping> delete = builder.createCriteriaDelete(IntentMapping.class);
			Root<IntentMapping> root = delete.from(IntentMapping.class);
			
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("entryId"), id));
			predicates.add(builder.equal(root.get("entryType"), "ACTION"));

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

	

	@Override
	public Object deleteErrorResponse(BaseRequestModel baseModel, String id) throws ApiException {
		
		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();

			ErrorResponse errResponse = new ErrorResponse();
			errResponse.setId(Long.parseLong(id));
			session.delete(errResponse);
			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ SADbServicesImpl.deleteServiceAction END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ SADbServicesImpl.deleteServiceAction END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} 

		return null;
	}

	@Override
	public Object getServiceActionById(BaseRequestModel baseModel, String id) throws ApiException {
		
		Action action = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Action> select = builder.createQuery(Action.class);
			Root<Action> root = select.from(Action.class);
			ParameterExpression<Long> actionId = builder.parameter(Long.class);
			Predicate actionIdP = builder.equal(root.get("id"), actionId);
			Predicate and1 = builder.and(actionIdP);
			select.where(and1);

			List<Action> actionLst = session.createQuery(select).setParameter(actionId, Long.parseLong(id))
					.getResultList();

			action = actionLst.get(0);
			logger.error("+++++ SADbServicesImpl.getServiceActionById Service End +++++");
		} catch (HibernateException ex) {
			logger.error("+++++ SADbServicesImpl. getServiceActionById END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ SADbServicesImpl. getServiceActionById END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return action;
	}

	@Override
	public Object getServiceActionByKu(BaseRequestModel baseModel, String id) throws ApiException {
		

		List<Action> action = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Action> select = builder.createQuery(Action.class);
			Root<Action> root = select.from(Action.class);
			ParameterExpression<Long> kuId = builder.parameter(Long.class);
			Predicate kuIdP = builder.equal(root.get("kuId"), kuId);
			Predicate and1 = builder.and(kuIdP);
			select.where(and1);

			action = session.createQuery(select).setParameter(kuId, Long.parseLong(id)).getResultList();
			logger.error("+++++ SADbServicesImpl.getServiceActionByKu Service End +++++");
		} catch (HibernateException ex) {
			logger.error("+++++ SADbServicesImpl.getServiceActionByKu END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);
			logger.error("+++++ SADbServicesImpl.getServiceActionByKu END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} 

		return action;
	}

	
	private void updateConfirmAction(Session session, BaseRequestModel baseModel) {

		for (ConfirmationModel confirmModel : baseModel.getConfirm()) {

			if (confirmModel.getId() != null) {
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaUpdate<Confirm> update = builder.createCriteriaUpdate(Confirm.class);
				Root<Confirm> root = update.from(Confirm.class);

				update.set(root.get("text"), confirmModel.getText());
				update.set(root.get("confirmationType"), confirmModel.getConfirmationType());
				update.set(root.get("confirmationOption"), confirmModel.getConfirmationOption());
				update.set(root.get("unConfirmationOption"), confirmModel.getUnConfirmationOption());
				update.set(root.get("localeCode"), confirmModel.getLocaleCode());
				update.set(root.get("terminationText"), confirmModel.getTerminationText());
				update.set(root.get("kuId"), confirmModel.getKuId());

				update.where(builder.equal(root.get("id"), confirmModel.getId()));
				session.createQuery(update).executeUpdate();

			} else {

				Action actionobj = new Action();
				actionobj.setId(baseModel.getAction().getId());

				Confirm confirm = new Confirm();
				confirm.setText(confirmModel.getText());
				confirm.setConfirmationType(confirmModel.getConfirmationType());
				confirm.setConfirmationOption(confirmModel.getConfirmationOption());
				confirm.setUnConfirmationOption(confirmModel.getUnConfirmationOption());
				confirm.setKuId(confirmModel.getKuId());
				confirm.setLocaleCode(confirmModel.getLocaleCode());
				confirm.setTerminationText(confirmModel.getTerminationText());
				confirm.setAction(actionobj);

				session.save(confirm);

			}
		}
	}

	private void updateActionExtn(Session session, BaseRequestModel baseModel) {

		for (ActionExtnModel actionExtnModel : baseModel.getAction().getActionExtn()) {

			if (actionExtnModel.getId() != null) {
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaUpdate<ActionExtn> update = builder.createCriteriaUpdate(ActionExtn.class);
				Root<ActionExtn> root = update.from(ActionExtn.class);

				update.set(root.get("requestBody"), actionExtnModel.getRequestBody());
				update.set(root.get("responsePath"), actionExtnModel.getResponsePath());
				update.set(root.get("url"), actionExtnModel.getUrl());
				update.set(root.get("callMethod"), actionExtnModel.getCallMethod());
				update.set(root.get("successCode"), actionExtnModel.getSuccessCode());
				update.set(root.get("errorCode"), actionExtnModel.getErrorCode());
				update.set(root.get("localeCode"), actionExtnModel.getLocaleCode());
				update.set(root.get("action"), baseModel.getAction().getId());

				update.where(builder.equal(root.get("id"), actionExtnModel.getId()));
				session.createQuery(update).executeUpdate();

			} else {
				ActionExtn actionExtn = new ActionExtn();

				Action action = new Action();
				action.setId(baseModel.getAction().getId());
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

}
