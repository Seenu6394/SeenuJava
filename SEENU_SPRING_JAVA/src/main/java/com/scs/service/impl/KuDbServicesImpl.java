package com.scs.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import java.util.Set;

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

import com.scs.entity.model.EntityDetails;
import com.scs.entity.model.EntityRegex;

import com.scs.entity.model.Intent;

import com.scs.entity.model.IntentExtn;
import com.scs.entity.model.Keyword;
import com.scs.entity.model.Ku;
import com.scs.entity.model.Languages;

import com.scs.entity.model.ProjectKeyword;
import com.scs.entity.model.RegEx;
import com.scs.entity.model.UserInfo;
import com.scs.exception.ApiException;
import com.scs.model.BCSettingsModel;
import com.scs.model.BaseRequestModel;

import com.scs.model.SettingsModel;
import com.scs.service.EntityDbServices;
import com.scs.service.IntentDbServices;
import com.scs.service.KuDbServices;
import com.scs.util.ApiConstants;
import com.scs.util.ErrorConstants;
import com.scs.util.Utility;

@Service("kuDbService")

public class KuDbServicesImpl implements KuDbServices {

	private static final Logger logger = Logger.getLogger(KuDbServicesImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private IntentDbServices intentDbService;

	@Autowired
	private EntityDbServices entityDbService;

	@Override
	public Object getKuDetails(BaseRequestModel baseModel) throws ApiException {

		
		List<Ku> kuLst = null;
		String name = "CANCEL";
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Ku> select = builder.createQuery(Ku.class);
			Root<Ku> root = select.from(Ku.class);
			ParameterExpression<String> kuId = builder.parameter(String.class);
			Predicate kuIdP = builder.notEqual(root.get("name"), kuId);
			Predicate and1 = builder.and(kuIdP);
			select.where(and1);

		  kuLst = session.createQuery(select).setParameter(kuId, name).getResultList();
		} catch (HibernateException ex) {

			logger.error("+++++ KuDbServicesImpl.getKUDetails END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ KuDbServicesImpl.getKUDetails END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} 

		return kuLst;
	}

	@Override
	public Object getKuById(BaseRequestModel baseModel, String id) throws ApiException {

		
		Ku ku = null;
		Set<RegEx> regexSet = new HashSet<>();
		List<EntityDetails> entity = new ArrayList<>();

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Ku> select = builder.createQuery(Ku.class);
			Root<Ku> root = select.from(Ku.class);
			ParameterExpression<Long> kuId = builder.parameter(Long.class);
			Predicate kuIdP = builder.equal(root.get("id"), kuId);
			Predicate and1 = builder.and(kuIdP);
			select.where(and1);

			List<Ku> kuDetails = session.createQuery(select).setParameter(kuId, Long.parseLong(id)).getResultList();

			ku = kuDetails.get(0);

			
			regexSet = (Set<RegEx>) setRegexInKu(session, ku);
			entity = (List<EntityDetails>) setEntityRegexInKu(session, ku);

			ku.setEntities(entity);
			ku.setRegex(regexSet);

		} catch (HibernateException ex) {

			logger.error("+++++ KuDbServicesImpl.getKuById END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);

		} catch (Exception ex) {
			logger.error("+++++ KuDbServicesImpl.getKuById END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return ku;
	}

	@SuppressWarnings("unchecked")
	private Object setRegexInKu(Session session, Ku ku) {

		List<RegEx> regexLst = new ArrayList<>();

		List<EntityDetails> entityLst = ku.getEntities();

		for (EntityDetails entity : entityLst) {

			List<EntityRegex> entityRegexLst = (List<EntityRegex>) getEntityRegex(session, entity.getId());

			for (EntityRegex entityRegex : entityRegexLst) {

				RegEx regex = (RegEx) getRegex(session, entityRegex.getRegexId());
				regexLst.add(regex);

			}

		}
		Set<RegEx> regexSet = new HashSet<RegEx>(regexLst);

		return regexSet;

	}

	@SuppressWarnings("unchecked")
	private Object setEntityRegexInKu(Session session, Ku ku) {

		List<EntityDetails> entityLstRespone = new ArrayList<>();

		List<EntityRegex> entityRegexLst = null;

		List<EntityDetails> entityLst = ku.getEntities();

		for (EntityDetails entity : entityLst) {

			entityRegexLst = (List<EntityRegex>) getEntityRegex(session, entity.getId());

			EntityDetails entityDetails = new EntityDetails();

			entityDetails.setName(entity.getName());
			entityDetails.setEntityRegex(entityRegexLst);
			entityDetails.setAction(entity.getAction());
			entityDetails.setEntityType(entity.getEntityType());
			entityDetails.setExample(entity.getExample());
			entityDetails.setQuestions(entity.getQuestions());
			entityDetails.setGlobalIdentifier(entity.getGlobalIdentifier());
			entityDetails.setIntentId(entity.getIntentId());
			entityDetails.setKuId(entity.getKuId());
			entityDetails.setId(entity.getId());
			entityDetails.setDate(entity.getDate());
			entityDetails.setDataType(entity.getDataType());
			entityLstRespone.add(entityDetails);

		}

		return entityLstRespone;

	}

	private Object getEntityRegex(Session session, Long id) {
		List<EntityRegex> entityRegexDetails = null;
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<EntityRegex> select = builder.createQuery(EntityRegex.class);
		Root<EntityRegex> root = select.from(EntityRegex.class);
		ParameterExpression<Long> kuId = builder.parameter(Long.class);
		Predicate kuIdP = builder.equal(root.get("entityId"), kuId);
		Predicate and1 = builder.and(kuIdP);
		select.where(and1);

		entityRegexDetails = session.createQuery(select).setParameter(kuId, id).getResultList();

		return entityRegexDetails;
	}

	private Object getRegex(Session session, Long id) {

		RegEx regex = null;

		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<RegEx> select = builder.createQuery(RegEx.class);
		Root<RegEx> root = select.from(RegEx.class);
		ParameterExpression<Long> kuId = builder.parameter(Long.class);
		Predicate kuIdP = builder.equal(root.get("id"), kuId);
		Predicate and1 = builder.and(kuIdP);
		select.where(and1);

		List<RegEx> regexDetails = session.createQuery(select).setParameter(kuId, id).getResultList();

		regex = regexDetails.get(0);
		return regex;
	}

	@Override
	public Object getKuByName(BaseRequestModel baseModel, String name) throws ApiException {

		
		Ku ku = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Ku> select = builder.createQuery(Ku.class);
			Root<Ku> root = select.from(Ku.class);
			ParameterExpression<String> kuId = builder.parameter(String.class);
			Predicate kuIdP = builder.equal(root.get("name"), kuId);
			Predicate and1 = builder.and(kuIdP);
			select.where(and1);

			List<Ku> kuDetails = session.createQuery(select).setParameter(kuId, name).getResultList();
			if (!kuDetails.isEmpty()) {
				ku = kuDetails.get(0);
			}

		} catch (HibernateException ex) {

			logger.error("+++++ KuDbServicesImpl.getKuByName END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);

		} catch (Exception ex) {
			logger.error("+++++ KuDbServicesImpl.getKuByName END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return ku;
	}

	@Override
	public Object createKU(BaseRequestModel baseModel) throws ApiException {
		
		Transaction tx = null;
		Ku ku = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			
			List<Ku> kuLst = (List<Ku>) getKuDetails(baseModel);

			for (Ku kunames : kuLst) {
				if (kunames.getName().equalsIgnoreCase(baseModel.getKu().getName())) {
					throw new ApiException("KU_EXIST", "KU already Exists.");
				}
			}
			
			ku = new Ku();
			ku.setId(null);
			ku.setName(Utility.CapsFirst(baseModel.getKu().getName()));
			ku.setActiveInd(baseModel.getKu().getActiveInd());
			ku.setSpamEnable("Y");
			ku.setIsRankable(baseModel.getKu().getIsRankable());

			session.save(ku);
			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ KuDbServicesImpl.createKU END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);

		} catch(ApiException ae) {
			logger.info("+++++ KuDbServicesImpl.createKU END SERVICE WITH APIEXCEPTION +++++"+ae.getMessage());
			throw ae;
		} catch (Exception ex) {
			logger.error("+++++ KuDbServicesImpl.createKU END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} 

		return ku;
	}

	@Override
	public Object updateKU(BaseRequestModel baseModel) throws ApiException {
		
		Transaction tx = null;
		String response = ApiConstants.SUCCESS;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			
			List<Ku> kuLst = (List<Ku>) getKuDetails(baseModel);
			
			for (Ku kunames : kuLst) {
				if (kunames.getName().equalsIgnoreCase(baseModel.getKu().getName())) {
					throw new ApiException("KU_EXIST", "KU already Exists.");
				}
			}
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<Ku> update = builder.createCriteriaUpdate(Ku.class);
			Root<Ku> root = update.from(Ku.class);
			update.set(root.get("name"), Utility.CapsFirst(baseModel.getKu().getName()));
			update.set(root.get("activeInd"), baseModel.getKu().getActiveInd());
			update.set(root.get("spamEnable"), "Y");
			update.set(root.get("isRankable"), baseModel.getKu().getIsRankable());
			update.where(builder.equal(root.get("id"), baseModel.getKu().getId()));
			session.createQuery(update).executeUpdate();
			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ KuDbServicesImpl.updateKU END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch(ApiException ae) {
			logger.info("+++++ KuDbServicesImpl.createKU END SERVICE WITH APIEXCEPTION +++++"+ae.getMessage());
			throw ae;
		} catch (Exception ex) {
			logger.error("+++++ KuDbServicesImpl.updateKU END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} 

		return response;

	}

	@Override
	public Object deleteKU(BaseRequestModel baseModel, String id) throws ApiException {
	
		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			Ku ku = new Ku();
			ku.setId(Long.parseLong(id));
			session.delete(ku);
			tx.commit();

		} catch (HibernateException ex) {

			logger.error("+++++ KuDbServicesImpl.deleteKU END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);

		} catch (Exception ex) {
			logger.error("+++++ KuDbServicesImpl.deleteKU END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		} 

		return null;
	}

	@Override
	public Object getUserByName(BaseRequestModel baseModel, String name) throws ApiException {

		
		UserInfo user = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<UserInfo> select = builder.createQuery(UserInfo.class);
			Root<UserInfo> root = select.from(UserInfo.class);
			ParameterExpression<String> userName = builder.parameter(String.class);
			Predicate kuIdP = builder.equal(root.get("username"), userName);
			Predicate and1 = builder.and(kuIdP);
			select.where(and1);

			List<UserInfo> userInfo = session.createQuery(select).setParameter(userName, name).getResultList();

			user = userInfo.get(0);

		} catch (HibernateException ex) {

			logger.error("+++++ KuDbServicesImpl.getUserByName END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);

		} catch (Exception ex) {
			logger.error("+++++ KuDbServicesImpl.getUserByName END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		} 

		return user;
	}


	@Override
	public Object checkNames(BaseRequestModel baseModel, Ku ku) throws ApiException {
		Session session = null;

		try {
			Ku kus = null;

			kus = (Ku) getKuByName(baseModel, ku.getName());
			if (kus == null) {
				ku.setFlag(false);
			} else {
				ku.setFlag(true);
			}

			List<Intent> intentLsts = new ArrayList<>();

			List<IntentExtn> intentNameLst = (List<IntentExtn>) intentDbService.getintentExtnDetails(baseModel);

			List<EntityDetails> entityLsts = new ArrayList<>();

			for (Intent intent : ku.getIntents()) {

				List<IntentExtn> names = new ArrayList<>();
				intent.setFlag(false);

				for (IntentExtn intentName : intent.getNames()) {

					intentName.setFlag(false);
					for (IntentExtn intentDtl : intentNameLst) {
						
						if (intentDtl.getName().equalsIgnoreCase(intentName.getName())) {
							intentName.setFlag(true);
							intent.setFlag(true);
							break;
						}
						
						}
					names.add(intentName);
				}
				Collections.sort(names);
				intent.setNames(names);
				intentLsts.add(intent);

			}

			for (EntityDetails entity : ku.getEntities()) {

				entityLsts.add(entity);

			}

			ku.setIntents(intentLsts);
			ku.setEntities(entityLsts);

		} catch (HibernateException ex) {
			logger.error("+++++ KuDbServicesImpl.checkNames END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		} catch (Exception ex) {
			logger.error("+++++ KuDbServicesImpl.checkNames END SERVICE WITHEXCEPTION +++++");
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return ku;

	}

	@Override
	public Object importSettings(BCSettingsModel bcSettingsModel) throws ApiException {
		
		Transaction tx = null;
		String response = ApiConstants.SUCCESS;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();

			Languages language = new Languages();

			Languages languageLst = (Languages) getLanguage();

			if (languageLst == null) {

				language.setId(bcSettingsModel.getSettings().getLanguage().getId());
				language.setEnglish(bcSettingsModel.getSettings().getLanguage().getEnglish());
				language.setArabic(bcSettingsModel.getSettings().getLanguage().getArabic());
				session.save(language);
			}

			List<ProjectKeyword> projectKeywordLst = (List<ProjectKeyword>) getProjectKeywordDetails();

			for (ProjectKeyword projectKey : bcSettingsModel.getSettings().getProjectKeywords()) {

				for (ProjectKeyword projectKeywordList : projectKeywordLst) {

					if (projectKeywordList.getProjectKeyword().contains(projectKey.getProjectKeyword())) {

						updateProjectKeywords(projectKey);

					} else {
						insertProjectKeywords(projectKey);

					}
				}

			}

			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ KuDbServicesImpl.importSettings END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);

		} catch (Exception ex) {
			logger.error("+++++ KuDbServicesImpl.importSettings END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return response;
	}

	private void insertProjectKeywords(ProjectKeyword projectKey) throws ApiException {
		
		Transaction tx = null;

		List<ProjectKeyword> projectKeywords = new ArrayList<>();

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();

			ProjectKeyword projectKeyword = new ProjectKeyword();
			projectKeyword.setKeywordType(projectKey.getKeywordType());
			projectKeyword.setProjectKeyword(projectKey.getProjectKeyword());
			projectKeyword.setProjectId(null);
			projectKeyword.setLocaleCode(projectKey.getLocaleCode());
			session.save(projectKeyword);
			projectKeywords.add(projectKey);
			
			if ("CANCEL".equals(projectKey.getKeywordType())) {

				Intent intent = (Intent) getCancelIntentByName();

				Keyword keyword = new Keyword();
				keyword.setIntent(intent);
				keyword.setLocaleCode(projectKey.getLocaleCode());
				keyword.setKeywordField(projectKey.getProjectKeyword());
				keyword.setPolarity("P");
				session.save(keyword);
			}

			tx.commit();

		} catch (Exception ex) {

			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ErrorConstants.SERVICEEXCEPTION, messageSource);
		} 

	}
	
	public Object getCancelIntentByName() throws ApiException {

		Intent intent = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Intent> select = builder.createQuery(Intent.class);
			Root<Intent> root = select.from(Intent.class);
			ParameterExpression<String> intnetId = builder.parameter(String.class);
			Predicate intentIdP = builder.equal(root.get("name"), intnetId);
			Predicate and1 = builder.and(intentIdP);
			select.where(and1);

			List<Intent> intentDetails = session.createQuery(select).setParameter(intnetId, "CANCEL").getResultList();
			if (!intentDetails.isEmpty())
				intent = intentDetails.get(0);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ IntentDbServicesImpl.getIntentById END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.getIntentById END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return intent;
	}

	private void updateProjectKeywords(ProjectKeyword projectKey) throws ApiException {
	
		Transaction tx = null;
	

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<ProjectKeyword> update = builder.createCriteriaUpdate(ProjectKeyword.class);
			Root<ProjectKeyword> root = update.from(ProjectKeyword.class);
			update.set(root.get("projectKeyword"), projectKey.getProjectKeyword());
			update.set(root.get("keywordType"), projectKey.getKeywordType());
			update.set(root.get("localeCode"), projectKey.getLocaleCode());
			update.where(builder.equal(root.get("id"), projectKey.getId()));

			session.createQuery(update).executeUpdate();
			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ KuDbServicesImpl.updateProjectKeywords END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ KuDbServicesImpl.updateProjectKeywords END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

	}

	@Override
	public Object getSettings(BaseRequestModel baseModel) throws ApiException {
		

		BCSettingsModel settings = new BCSettingsModel();
		SettingsModel setModel = new SettingsModel();
		try (Session session = sessionFactory.openSession()) {

			Languages language = null;
			language = (Languages) getLanguage();

			List<ProjectKeyword> projectKeywordLst = null;
			projectKeywordLst = (List<ProjectKeyword>) getProjectKeywordDetails();

			setModel.setLanguage(language);
			setModel.setProjectKeywords(projectKeywordLst);

			settings.setSettings(setModel);

		} catch (HibernateException ex) {
			logger.error("+++++ KuDbServicesImpl.getSettings END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);

		} catch (Exception ex) {
			logger.error("+++++ KuDbServicesImpl.getSettings END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return settings;
	}

	@Override
	public Object getLanguage() throws ApiException {

		
		Languages language = null;
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Languages> query = builder.createQuery(Languages.class);
			Root<Languages> root = query.from(Languages.class);
			query.select(root);
			query.orderBy(builder.desc(root.get("id")));
			language = session.createQuery(query).getResultList().get(0);

		} catch (HibernateException ex) {

			logger.error("+++++ KuDbServicesImpl.getLanguage END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ KuDbServicesImpl.getLanguage END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} 

		return language;
	}

	@Override
	public Object getProjectKeywordDetails() throws ApiException {

		
		List<ProjectKeyword> projectKeywordLst = null;
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ProjectKeyword> query = builder.createQuery(ProjectKeyword.class);
			Root<ProjectKeyword> root = query.from(ProjectKeyword.class);
			query.select(root);
			query.orderBy(builder.desc(root.get("id")));
			projectKeywordLst = session.createQuery(query).getResultList();

		} catch (HibernateException ex) {

			logger.error("+++++ KuDbServicesImpl.getProjectKeywordDetails END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ KuDbServicesImpl.getProjectKeywordDetails END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		} 

		return projectKeywordLst;
	}

}
