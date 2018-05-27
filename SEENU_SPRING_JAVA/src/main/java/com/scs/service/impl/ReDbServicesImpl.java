package com.scs.service.impl;

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

import com.scs.entity.model.EntityDetails;
import com.scs.entity.model.EntityRegex;

import com.scs.entity.model.Intent;
import com.scs.entity.model.RegEx;
import com.scs.entity.model.RegexExtn;
import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;

import com.scs.model.IntentRegex;
import com.scs.model.RegexExtnModel;
import com.scs.service.ReDbServices;
import com.scs.util.ApiConstants;
import com.scs.util.Utility;

@Service("ReDbService")

public class ReDbServicesImpl implements ReDbServices {

	private static final Logger logger = Logger.getLogger(ReDbServicesImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Object getReDetails(BaseRequestModel baseModel) throws ApiException {
		List<RegEx> reLst = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<RegEx> query = builder.createQuery(RegEx.class);
			Root<RegEx> root = query.from(RegEx.class);
			query.select(root);
			query.orderBy(builder.desc(root.get("id")));
			reLst = session.createQuery(query).getResultList();

		} catch (HibernateException ex) {
			logger.error("+++++ ReDbServicesImpl.getReDetails END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ ReDbServicesImpl.getReDetails END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return reLst;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getRegexwithIntents(BaseRequestModel baseModel) throws ApiException {

		List<RegEx> reLst = new ArrayList<>();
		List<IntentRegex> intentRegexLst = new ArrayList<>();

		try {
			reLst = (List<RegEx>) getReDetails(baseModel);

			for (RegEx reg : reLst) {
				List<Intent> intentLst = new ArrayList<>();
				IntentRegex intentRegex = new IntentRegex();
				intentRegex.setExpression(reg.getExpression());
				intentRegex.setMessage(reg.getErrorCode());
				intentLst = (List<Intent>) getIntentswithRegex(reg.getId());
				if (intentLst != null) {
					intentRegex.setIntents(intentLst);
				}
				intentRegex.setId(reg.getId());
				intentRegex.setRegexname(reg.getRegexname());
				intentRegex.setRegexes(reg.getRegexes());
				intentRegexLst.add(intentRegex);
			}

			logger.error("+++++ ReDbServicesImpl.getRegexwithIntents Service End +++++");
		} catch (HibernateException ex) {
			logger.error("+++++ ReDbServicesImpl.getRegexwithIntents END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ ReDbServicesImpl.getRegexwithIntents END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return intentRegexLst;
	}

	private Object getIntentswithRegex(Long id) throws ApiException {

		List<Intent> intentLst = new ArrayList<>();

		try {
			List<EntityRegex> entityRegexLst = new ArrayList<>();

			entityRegexLst = (List<EntityRegex>) getEntityByRegexId(id);

			if (entityRegexLst != null) {

				List<EntityDetails> entityDetailsLst = new ArrayList<>();

				for (EntityRegex entityRegex : entityRegexLst) {

					EntityDetails entity = new EntityDetails();
					entity = (EntityDetails) getEntityDetails(entityRegex.getEntityId());
					entityDetailsLst.add(entity);

				}

				if (!entityDetailsLst.isEmpty()) {
					for (EntityDetails entityDetails : entityDetailsLst) {
						Intent intent = new Intent();
						intent = (Intent) getIntentDetails(entityDetails.getIntentId());

						if (intent != null) {
							intentLst.add(intent);
						}
					}
				}

			}

			logger.error("+++++ ReDbServicesImpl.getIntentswithRegex Service End +++++");
		} catch (HibernateException ex) {
			logger.error("+++++ ReDbServicesImpl.getIntentswithRegex END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ ReDbServicesImpl.getIntentswithRegex END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}
		return intentLst;
	}

	@Override
	public Object createRe(BaseRequestModel baseModel) throws ApiException {
		
		Transaction tx = null;
		RegEx regularExpression = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			regularExpression = new RegEx();
			regularExpression.setId(null);
			regularExpression.setExpression(baseModel.getRegularExpression().getExpression());
			regularExpression.setRegexname(baseModel.getRegularExpression().getRegexname());
			regularExpression.setErrorCode(baseModel.getRegularExpression().getErrorCode());

			session.save(regularExpression);

			for (RegexExtnModel regexExtnModel : baseModel.getRegularExpression().getRegexes()) {
				RegexExtn regexExtn = new RegexExtn();

				regexExtn.setErrorMessage(regexExtnModel.getErrorMessage());
				regexExtn.setLocaleCode(regexExtnModel.getLocaleCode());
				RegEx regex = new RegEx();
				regex.setId(regularExpression.getId());
				regexExtn.setRegex(regex);
				session.save(regexExtn);

			}

			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ ReDbServicesImpl.createRe END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ ReDbServicesImpl.createRe END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return regularExpression;
	}

	@Override
	public Object createRegexMapping(Long entityId, Long regexId) throws ApiException {
		
		Transaction tx = null;
		EntityRegex entityRegex = new EntityRegex();
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();

			entityRegex.setEntityId(entityId);
			entityRegex.setRegexId(regexId);
			session.save(entityRegex);
			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ ReDbServicesImpl.createRegexMapping END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ ReDbServicesImpl.createRegexMapping END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} 
		return entityRegex;
	}

	@Override
	public Object deleteRegexMapping(Long entityId, Long regexId) throws ApiException {

		
		Transaction tx = null;
		RegEx regularExpression = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaDelete<EntityRegex> delete = builder.createCriteriaDelete(EntityRegex.class);
			Root<EntityRegex> root = delete.from(EntityRegex.class);

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("entityId"), entityId));
			predicates.add(builder.equal(root.get("regexId"), regexId));

			delete.where(predicates.toArray(new Predicate[] {}));
			session.createQuery(delete).executeUpdate();
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ IntentDbServicesImpl.deleteRegexMapping END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.deleteRegexMapping END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		} 
		return regularExpression;

	}

	@Override
	public Object deleteRegEx(String id) throws ApiException {

	
		Transaction tx = null;
		RegEx regularExpression = null;
		try (Session session = sessionFactory.openSession()) {

			tx = session.beginTransaction();

			RegEx regex = new RegEx();
			regex.setId(Long.parseLong(id));
			session.delete(regex);
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ IntentDbServicesImpl.deleteRegEx END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.deleteRegEx END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		} 
		return regularExpression;

	}

	@Override
	public Object updateRe(BaseRequestModel baseModel) throws ApiException {
		
		Transaction tx = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<RegEx> update = builder.createCriteriaUpdate(RegEx.class);
			Root<RegEx> root = update.from(RegEx.class);
			update.set(root.get("regexname"), baseModel.getRegularExpression().getRegexname());
			update.set(root.get("expression"), baseModel.getRegularExpression().getExpression());
			update.set(root.get("errorCode"), baseModel.getRegularExpression().getErrorCode());

			update.where(builder.equal(root.get("id"), baseModel.getRegularExpression().getId()));
			session.createQuery(update).executeUpdate();

			updateRegexExtn(session, baseModel);
			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ ReDbServicesImpl.updateRe END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ ReDbServicesImpl.updateRe END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} 

		return ApiConstants.SUCCESS;
	}

	private void updateRegexExtn(Session session, BaseRequestModel baseModel) {

		for (RegexExtnModel regexExtnModel : baseModel.getRegularExpression().getRegexes()) {

			if (regexExtnModel.getId() != null) {

				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaUpdate<RegexExtn> update = builder.createCriteriaUpdate(RegexExtn.class);
				Root<RegexExtn> root = update.from(RegexExtn.class);
				update.set(root.get("errorMessage"), regexExtnModel.getErrorMessage());

				update.where(builder.equal(root.get("id"), regexExtnModel.getId()));
				session.createQuery(update).executeUpdate();

			} else {
				RegexExtn regexExtn = new RegexExtn();
				regexExtn.setErrorMessage(regexExtnModel.getErrorMessage());
				regexExtn.setLocaleCode(regexExtnModel.getLocaleCode());
				RegEx regex = new RegEx();
				regex.setId(baseModel.getRegularExpression().getId());
				regexExtn.setRegex(regex);
				session.save(regexExtn);

			}
		}

	}

	@Override
	public Object getReById(BaseRequestModel baseModel, String id) throws ApiException {
		

		RegEx regularExpression = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<RegEx> select = builder.createQuery(RegEx.class);
			Root<RegEx> root = select.from(RegEx.class);
			ParameterExpression<Long> regExId = builder.parameter(Long.class);
			Predicate regExIdP = builder.equal(root.get("id"), regExId);
			Predicate and1 = builder.and(regExIdP);
			select.where(and1);

			List<RegEx> regularExpressionsLst = session.createQuery(select).setParameter(regExId, Long.parseLong(id))
					.getResultList();

			regularExpression = regularExpressionsLst.get(0);

		} catch (HibernateException ex) {
			logger.error("+++++ ReDbServicesImpl.getReById END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ ReDbServicesImpl.getReById END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return regularExpression;
	}

	private Object getEntityByRegexId(Long id) throws ApiException {
		;

		List<EntityRegex> regularExpressionsLst = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<EntityRegex> select = builder.createQuery(EntityRegex.class);
			Root<EntityRegex> root = select.from(EntityRegex.class);
			ParameterExpression<Long> regExId = builder.parameter(Long.class);
			Predicate regExIdP = builder.equal(root.get("regexId"), regExId);
			Predicate and1 = builder.and(regExIdP);
			select.where(and1);

			regularExpressionsLst = session.createQuery(select).setParameter(regExId, id).getResultList();

		} catch (HibernateException ex) {
			logger.error("+++++ ReDbServicesImpl.getEntityByRegexId END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ ReDbServicesImpl.getEntityByRegexId END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return regularExpressionsLst;
	}

	private Object getEntityDetails(Long id) throws ApiException {
		

		EntityDetails entityDetails = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<EntityDetails> select = builder.createQuery(EntityDetails.class);
			Root<EntityDetails> root = select.from(EntityDetails.class);
			ParameterExpression<Long> entityId = builder.parameter(Long.class);
			Predicate entityIdP = builder.equal(root.get("id"), entityId);
			Predicate and1 = builder.and(entityIdP);
			select.where(and1);

			List<EntityDetails> entityDetailsLst = session.createQuery(select).setParameter(entityId, id)
					.getResultList();
			if (!entityDetailsLst.isEmpty()) {
				entityDetails = entityDetailsLst.get(0);
			}
		} catch (HibernateException ex) {
			logger.error("+++++ ReDbServicesImpl.getEntityDetails END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ ReDbServicesImpl.getEntityDetails END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} 

		return entityDetails;
	}

	private Object getIntentDetails(Long id) throws ApiException {
		

		Intent intent = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Intent> select = builder.createQuery(Intent.class);
			Root<Intent> root = select.from(Intent.class);
			ParameterExpression<Long> entityId = builder.parameter(Long.class);
			Predicate entityIdP = builder.equal(root.get("id"), entityId);
			Predicate and1 = builder.and(entityIdP);
			select.where(and1);

			List<Intent> intentLst = session.createQuery(select).setParameter(entityId, id).getResultList();

			if (!intentLst.isEmpty()) {
				intent = intentLst.get(0);
			}
		} catch (HibernateException ex) {
			logger.error("+++++ ReDbServicesImpl.getIntentDetails END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ ReDbServicesImpl.getIntentDetails END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} 

		return intent;
	}

}
