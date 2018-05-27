package com.scs.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scs.entity.model.Action;

import com.scs.entity.model.EntityDetails;
import com.scs.entity.model.EntityQuestion;

import com.scs.entity.model.Intent;
import com.scs.entity.model.IntentExtn;
import com.scs.entity.model.IntentMapping;
import com.scs.entity.model.Keyword;
import com.scs.entity.model.Ku;
import com.scs.entity.model.Message;
import com.scs.entity.model.ProjectKeyword;
import com.scs.entity.model.Response;
import com.scs.entity.model.WorkFlow;
import com.scs.entity.model.WorkflowSequence;
import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;

import com.scs.model.IntentExtnModel;
import com.scs.model.KeywordModel;
import com.scs.model.ProjectKeywordModel;
import com.scs.model.ResponseModel;
import com.scs.service.IntentDbServices;
import com.scs.util.ApiConstants;
import com.scs.util.Utility;

@Service("IntentDbService")

public class IntentDbServicesImpl implements IntentDbServices {

	private static final Logger logger = Logger.getLogger(IntentDbServicesImpl.class);
	private static final String KEYWORD_EXIST = "KEYWORD_EXIST";
	private static final String KEYWORD_EXIST_MES = "Keyword already Exists.";

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Object getintentDetails(BaseRequestModel baseModel) throws ApiException {

		List<Intent> intentLst = null;

		try (Session session = sessionFactory.openSession()) {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Intent> query = builder.createQuery(Intent.class);
			Root<Intent> root = query.from(Intent.class);
			query.select(root);
			query.orderBy(builder.desc(root.get("id")));
			intentLst = session.createQuery(query).getResultList();

		} catch (HibernateException ex) {
			logger.error("+++++ IntentDbServicesImpl.getintentDetails END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.getintentDetails END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return getIntentsResponse(intentLst);
	}

	@Override
	public Object getProjectKeywords(BaseRequestModel baseModel) throws ApiException {

		List<ProjectKeyword> projectKeywordLst = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ProjectKeyword> query = builder.createQuery(ProjectKeyword.class);
			Root<ProjectKeyword> root = query.from(ProjectKeyword.class);
			query.select(root);
			query.orderBy(builder.desc(root.get("id")));
			projectKeywordLst = session.createQuery(query).getResultList();

		} catch (HibernateException ex) {
			logger.error("+++++ IntentDbServicesImpl.getProjectKeywords END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.getProjectKeywords END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}
		return projectKeywordLst;
	}

	@Override
	public Object getKeywords(BaseRequestModel baseModel) throws ApiException {

		List<Keyword> keywordLst = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Keyword> query = builder.createQuery(Keyword.class);
			Root<Keyword> root = query.from(Keyword.class);
			query.select(root);
			query.orderBy(builder.desc(root.get("id")));
			keywordLst = session.createQuery(query).getResultList();

		} catch (HibernateException ex) {
			logger.error("+++++ IntentDbServicesImpl.getintentDetails END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.getintentDetails END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return keywordLst;
	}

	@Override
	public Object createintent(BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;
		Intent intent = new Intent();

		String intentName = null;
		Boolean addKeyword = true;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			Instant instant = Instant.now();

			List<Intent> intentLst = (List<Intent>) getintentLst(baseModel);

			intent.setId(null);
			intent.setKuId(baseModel.getIntent().getKuId());
			intent.setGlobalIdentifier(baseModel.getIntent().getGlobalIdentifier());
			intent.setActiveInd("Y");
			intent.setDate(instant.toString());

			for (IntentExtn intentExtnModel : baseModel.getIntent().getNames()) {

				if ("en".equals(intentExtnModel.getLocaleCode())) {

					for (Intent intent2 : intentLst) {
						if (intentExtnModel.getName().equalsIgnoreCase(intent2.getName())) {
							throw new ApiException("INTENT_EXIST", "Intent already Exists.");
						}
					}

					intent.setName(intentExtnModel.getName());
					intentName = intentExtnModel.getName();

				}
			}
			session.save(intent);

			for (IntentExtn intentExtnModel : baseModel.getIntent().getNames()) {

				if ("ar".equals(intentExtnModel.getLocaleCode())) {

					IntentExtn intentExt = new IntentExtn();
					intentExt.setName(intentExtnModel.getName());
					intentExt.setIntent(intent);
					intentExt.setLocaleCode(intentExtnModel.getLocaleCode());
					session.save(intentExt);
					intent.getNames().add(intentExt);

					if (intentName != null && addKeyword) {

						Keyword keyword = new Keyword();
						keyword.setIntent(intent);
						keyword.setDate(instant.toString());
						keyword.setPolarity("P");
						keyword.setKeywordField(intentExtnModel.getName().toLowerCase());
						keyword.setLocaleCode("ar");
						session.save(keyword);
						intent.getKeywords().add(keyword);

					}

				} else if ("en".equals(intentExtnModel.getLocaleCode())) {
					IntentExtn intentExt = new IntentExtn();
					intentExt.setName(intentExtnModel.getName());
					intentExt.setIntent(intent);
					intentExt.setLocaleCode(intentExtnModel.getLocaleCode());
					session.save(intentExt);
					intent.getNames().add(intentExt);

					if (intentName != null && addKeyword) {

						Keyword keyword = new Keyword();
						keyword.setIntent(intent);
						keyword.setDate(instant.toString());
						keyword.setPolarity("P");
						keyword.setKeywordField(intentExtnModel.getName().toLowerCase());
						keyword.setLocaleCode("en");
						session.save(keyword);
						intent.getKeywords().add(keyword);

					}
				}

			}

			for (KeywordModel keywordModel : baseModel.getIntent().getKeywords()) {

				if ((!keywordModel.getKeywordField().equalsIgnoreCase(baseModel.getIntent().getName()))
						&& keywordModel.getKeywordField() != null && addKeyword) {

					if (baseModel.getIntent().getKeywords() != null) {
						List<KeywordModel> arabicKeyLst = new ArrayList<>();
						List<KeywordModel> englishKeyLst = new ArrayList<>();
						for (KeywordModel keywordddd : baseModel.getIntent().getKeywords()) {

							if ("ar".equals(keywordddd.getLocaleCode())) {

								arabicKeyLst.add(keywordddd);
							} else {

								englishKeyLst.add(keywordddd);
							}

						}

						for (KeywordModel englishKeywordLst : englishKeyLst) {

							for (KeywordModel arabicKeywordLst : arabicKeyLst) {
								if (arabicKeywordLst.getKeywordField()
										.equalsIgnoreCase(englishKeywordLst.getKeywordField())) {

									throw new ApiException(KEYWORD_EXIST, KEYWORD_EXIST_MES);

								}
							}
						}
					}

					Keyword keyword = new Keyword();
					keyword.setIntent(intent);
					keyword.setDate(instant.toString());
					keyword.setLocaleCode(keywordModel.getLocaleCode());
					keyword.setPolarity(keywordModel.getPolarity());
					keyword.setKeywordField(keywordModel.getKeywordField().toLowerCase());
					session.save(keyword);
					intent.getKeywords().add(keyword);

				}
			}

			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ IntentDbServicesImpl.createintent END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (ApiException ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.createintent END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return intent;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Object updateIntent(BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;
		Intent intent = new Intent();
		List<Keyword> keylst = null;
		List<IntentExtn> names = null;
		Instant instant = Instant.now();
		String reqIntnam = null;
		String arabicName = null;
		Boolean addKeyword = true;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<Intent> update = builder.createCriteriaUpdate(Intent.class);
			Root<Intent> root = update.from(Intent.class);
			update.set(root.get("name"), baseModel.getIntents().getName());
			update.set(root.get("globalIdentifier"), baseModel.getIntents().getGlobalIdentifier());
			String activeInd = "Y";
			update.set(root.get("activeInd"), activeInd);
			update.set(root.get("kuId"), baseModel.getIntents().getKuId());
			update.where(builder.equal(root.get("id"), baseModel.getIntents().getId()));
			session.createQuery(update).executeUpdate();

			updateIntentExtn(session, baseModel);

			Boolean intentNam = true;
			Boolean intentarName = true;

			// Keyword list from db
			keylst = (List<Keyword>) getKeywordByIntentId(baseModel, baseModel.getIntents().getId());

			names = baseModel.getIntents().getNames();
			for (IntentExtn intArNam : names) {

				if ("ar".equals(intArNam.getLocaleCode())) {

					arabicName = intArNam.getName();
				}

			}

			for (Keyword keyworddblist : keylst) {

				for (IntentExtn intNames : names) {

					if ("ar".equals(keyworddblist.getLocaleCode()) && "ar".equals(intNames.getLocaleCode())
							&& keyworddblist.getKeywordField().equals(intNames.getName())) {

						intentarName = false;
						break;
					}
				}

			}

			for (Keyword keyworddblist : keylst) {

				if (keyworddblist.getKeywordField().equalsIgnoreCase(baseModel.getIntents().getName())) {

					intentNam = false;
					break;
				}

			}

			if (intentNam) {
				reqIntnam = baseModel.getIntents().getName().toLowerCase();
				if (reqIntnam != null && addKeyword) {

					for (Keyword keyLst : keylst) {

						if (reqIntnam.equalsIgnoreCase(keyLst.getKeywordField())) {

							throw new ApiException(KEYWORD_EXIST, KEYWORD_EXIST_MES);
						}
					}

					Keyword keyword = new Keyword();

					keyword.setKeywordField(reqIntnam.toLowerCase());
					keyword.setIntent(baseModel.getIntents());
					keyword.setPolarity("P");
					keyword.setLocaleCode("en");
					keyword.setDate(instant.toString());
					session.save(keyword);
				}
			}

			if (intentarName && arabicName != null && addKeyword) {

				for (Keyword keyLst : keylst) {

					if (arabicName.equalsIgnoreCase(keyLst.getKeywordField())) {

						throw new ApiException(KEYWORD_EXIST, KEYWORD_EXIST_MES);
					}
				}

				Keyword keyword = new Keyword();

				keyword.setKeywordField(arabicName);
				keyword.setIntent(baseModel.getIntents());
				keyword.setPolarity("P");
				keyword.setLocaleCode("ar");
				keyword.setDate(instant.toString());
				session.save(keyword);

			}

			if (baseModel.getKeywords() != null) {
				List<KeywordModel> keywordLst = new ArrayList<>();
				for (KeywordModel keywordModel : baseModel.getKeywords()) {
					keywordModel.setKeywordField(keywordModel.getKeywordField());
					keywordLst.add(keywordModel);

				}

				Boolean isKeywordPresent = true;
				// Keyword list from user request
				for (KeywordModel keywordMdl : keywordLst) {
					isKeywordPresent = true;
					for (Keyword keyworddblist : keylst) {

						if (keyworddblist.getKeywordField().equals(keywordMdl.getKeywordField())) {

							isKeywordPresent = false;
							throw new ApiException(KEYWORD_EXIST, KEYWORD_EXIST_MES);
						}

					}
					if ((!keywordMdl.getKeywordField().equalsIgnoreCase(reqIntnam)) && isKeywordPresent
							&& keywordMdl.getKeywordField() != null && addKeyword) {

						for (Keyword keyLst : keylst) {

							if (baseModel.getKeywords() != null) {
								List<KeywordModel> arabicKeyLst = new ArrayList<>();
								List<KeywordModel> englishKeyLst = new ArrayList<>();
								for (KeywordModel keywordddd : baseModel.getKeywords()) {

									if (keyLst.getKeywordField().equalsIgnoreCase(keywordddd.getKeywordField())) {
										throw new ApiException(KEYWORD_EXIST, KEYWORD_EXIST_MES);

									}

									if ("ar".equals(keywordddd.getLocaleCode())) {

										arabicKeyLst.add(keywordddd);
									} else {

										englishKeyLst.add(keywordddd);
									}

								}

								for (KeywordModel englishKeywordLst : englishKeyLst) {

									for (KeywordModel arabicKeywordLst : arabicKeyLst) {
										if (arabicKeywordLst.getKeywordField()
												.equalsIgnoreCase(englishKeywordLst.getKeywordField())) {

											throw new ApiException(KEYWORD_EXIST, KEYWORD_EXIST_MES);

										}
									}
								}
							}

						}

						Keyword keyword = new Keyword();

						keyword.setKeywordField(keywordMdl.getKeywordField().toLowerCase());
						keyword.setIntent(baseModel.getIntents());
						keyword.setPolarity(keywordMdl.getPolarity());
						keyword.setLocaleCode(keywordMdl.getLocaleCode());
						keyword.setDate(instant.toString());
						session.save(keyword);
					}

				}
			}

			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ IntentDbServicesImpl.updateIntent END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (ApiException ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.updateIntent END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return intent;
	}

	private void updateIntentExtn(Session session, BaseRequestModel baseModel) {

		for (IntentExtn intExtn : baseModel.getIntents().getNames()) {

			if (intExtn.getId() != null) {
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaUpdate<IntentExtn> update = builder.createCriteriaUpdate(IntentExtn.class);
				Root<IntentExtn> root = update.from(IntentExtn.class);
				update.set(root.get("name"), intExtn.getName());
				update.where(builder.equal(root.get("id"), intExtn.getId()));
				session.createQuery(update).executeUpdate();

			} else {
				IntentExtn intentExt = new IntentExtn();
				intentExt.setName(intExtn.getName());
				intentExt.setIntent(baseModel.getIntents());
				intentExt.setLocaleCode(intExtn.getLocaleCode());
				session.save(intentExt);
			}

		}

	}

	@Override
	public Object deleteintent(BaseRequestModel baseModel, String id) throws ApiException {

		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			Intent intent = new Intent();
			intent.setId(Long.parseLong(id));
			session.delete(intent);
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ IntentDbServicesImpl.deleteintent END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.deleteintent END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return null;
	}

	@Override
	public Object createKeyword(BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;
		String response = ApiConstants.SUCCESS;
		Boolean addKeyword = true;
		Instant instant = Instant.now();
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();

			for (KeywordModel keywordModel : baseModel.getKeywords()) {

				if (keywordModel.getKeywordField() != null && addKeyword) {

					Keyword keyword = new Keyword();
					keyword.setId(null);
					keyword.setKeywordField(keywordModel.getKeywordField().toLowerCase());
					keyword.setLocaleCode(keywordModel.getLocaleCode());
					keyword.setIntent(keywordModel.getIntent());
					keyword.setPolarity(keywordModel.getPolarity());
					keyword.setDate(instant.toString());
					session.save(keyword);

				}
			}
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ IntentDbServicesImpl.createKeyword END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.createKeyword END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return response;
	}

	@Override
	public Object deleteKeyword(BaseRequestModel baseModel, String id) throws ApiException {

		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			Keyword keyword = new Keyword();
			keyword.setId(Long.parseLong(id));
			session.delete(keyword);
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ IntentDbServicesImpl.deleteKeyword END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.deleteKeyword END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return null;
	}

	@Override
	public Object createProjectKeyword(BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;
		String response = ApiConstants.SUCCESS;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();

			for (ProjectKeywordModel projectKeywordModel : baseModel.getProjectKeywords()) {

				ProjectKeyword projectKeyword = new ProjectKeyword();
				projectKeyword.setId(null);
				projectKeyword.setKeywordType(projectKeywordModel.getKeywordType());
				projectKeyword.setProjectKeyword(projectKeywordModel.getProjectKeyword());
				projectKeyword.setLocaleCode(projectKeywordModel.getLocaleCode());
				projectKeyword.setProjectId(null);
				session.save(projectKeyword);

				if ("CANCEL".equals(projectKeywordModel.getKeywordType())) {

					Intent intent = (Intent) getCancelIntentByName(baseModel);

					Keyword keyword = new Keyword();
					keyword.setIntent(intent);
					keyword.setLocaleCode(projectKeywordModel.getLocaleCode());
					keyword.setKeywordField(projectKeywordModel.getProjectKeyword());
					keyword.setPolarity("P");
					session.save(keyword);
				}

			}
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ IntentDbServicesImpl.createProjectKeyword END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.createProjectKeyword END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return response;
	}

	@Override
	public Object deleteProjectKeyword(BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;
		Keyword keywordDetails = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();

			keywordDetails = (Keyword) getKeywordByName(baseModel, baseModel.getProjectrKeyword().getProjectKeyword());
			Keyword keyword = new Keyword();
			if (keywordDetails != null) {
				keyword.setId(keywordDetails.getId());
				session.delete(keyword);
			}

			ProjectKeyword projectKeyword = new ProjectKeyword();
			projectKeyword.setId(baseModel.getProjectrKeyword().getId());
			session.delete(projectKeyword);

			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ IntentDbServicesImpl.deleteProjectKeyword END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.deleteProjectKeyword END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return null;
	}

	@Override
	public Object deleteIntentResponseByResponseID(BaseRequestModel baseModel, String id) throws ApiException {

		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			Response response = new Response();
			response.setId(Long.parseLong(id));
			session.delete(response);
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error(
					"+++++ IntentDbServicesImpl.deleteIntentResponseByResponseID END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.deleteIntentResponseByResponseID END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return null;
	}

	@Override
	public Object deleteIntentResponse(BaseRequestModel baseModel, String id) throws ApiException {

		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaDelete<Response> delete = builder.createCriteriaDelete(Response.class);
			Root<Response> root = delete.from(Response.class);
			Message message = new Message();
			message.setId(Long.parseLong(id));
			delete.where(builder.equal(root.get("message"), message));
			session.createQuery(delete).executeUpdate();
			tx.commit();
			deleteWorkflowSequence(id);
			deleteIntentMapping(id);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ IntentDbServicesImpl.deleteIntentResponse END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.deleteIntentResponse END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return null;
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
			predicates.add(builder.equal(root.get("entryType"), "MESSAGE"));

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
			predicates.add(builder.equal(root.get("entryType"), "MESSAGE"));

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
	public Object deleteIntentWorkflow(BaseRequestModel baseModel, String id) throws ApiException {

		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaDelete<WorkFlow> delete = builder.createCriteriaDelete(WorkFlow.class);
			Root<WorkFlow> root = delete.from(WorkFlow.class);
			Intent intent = new Intent();
			intent.setId(Long.parseLong(id));
			delete.where(builder.equal(root.get("intent"), intent));
			session.createQuery(delete).executeUpdate();
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ IntentDbServicesImpl.deleteIntentWorkflow END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.deleteIntentWorkflow END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return null;
	}

	@Override
	public Object getIntentById(BaseRequestModel baseModel, String id) throws ApiException {

		Intent intent = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Intent> select = builder.createQuery(Intent.class);
			Root<Intent> root = select.from(Intent.class);
			ParameterExpression<Long> intnetId = builder.parameter(Long.class);
			Predicate intentIdP = builder.equal(root.get("id"), intnetId);
			Predicate and1 = builder.and(intentIdP);
			select.where(and1);

			List<Intent> intentDetails = session.createQuery(select).setParameter(intnetId, Long.parseLong(id))
					.getResultList();
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

	@Override
	public Object getKeywordByIntentId(BaseRequestModel baseModel, Long id) throws ApiException {

		List<Keyword> keywordDetails = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Keyword> select = builder.createQuery(Keyword.class);
			Root<Keyword> root = select.from(Keyword.class);
			ParameterExpression<Intent> intnetId = builder.parameter(Intent.class);
			Intent intent = new Intent();
			intent.setId(id);
			Predicate intentIdP = builder.equal(root.get("intent"), intnetId);
			Predicate and1 = builder.and(intentIdP);
			select.where(and1);

			keywordDetails = session.createQuery(select).setParameter(intnetId, intent).getResultList();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ IntentDbServicesImpl.getKeywordByIntentId END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.getKeywordByIntentId END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}
		return keywordDetails;
	}

	@Override
	public Object getIntentByKu(BaseRequestModel baseModel, String id) throws ApiException {

		List<Intent> intentDetails = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Intent> select = builder.createQuery(Intent.class);
			Root<Intent> root = select.from(Intent.class);
			ParameterExpression<Long> kuId = builder.parameter(Long.class);
			Predicate kuIdP = builder.equal(root.get("kuId"), kuId);
			Predicate and1 = builder.and(kuIdP);
			select.where(and1);

			intentDetails = session.createQuery(select).setParameter(kuId, Long.parseLong(id)).getResultList();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ IntentDbServicesImpl.getIntentByKu END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));

		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.getIntentByKu END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return getIntentsResponse(intentDetails);
	}

	private List<Intent> getIntentsResponse(List<Intent> intentLst) {
		int posKeywordCount;
		int negaKeywordCount;
		for (Intent intent : intentLst) {
			posKeywordCount = 0;
			negaKeywordCount = 0;
			for (Keyword keyword : intent.getKeywords()) {
				if (("P").equalsIgnoreCase(keyword.getPolarity())) {
					posKeywordCount++;
				} else {
					negaKeywordCount++;
				}
			}
			intent.setPositiveKeywords(posKeywordCount);
			intent.setNegativeKeywords(negaKeywordCount);
		}

		return intentLst;

	}

	@Override
	public Object createintentResponses(BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;
		List<ResponseModel> resDetails = null;
		Message message = new Message();

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			int index = 0;
			resDetails = baseModel.getIntent().getResponses();

			if (baseModel.getMessage().getId() == null) {
				message.setId(baseModel.getMessage().getId());
				message.setMessageCode(UUID.randomUUID().toString());
				Intent intentId = new Intent();
				intentId.setId(baseModel.getIntent().getId());
				message.setIntent(intentId);
				session.save(message);

				List<Response> responseLst = new ArrayList<>();
				for (ResponseModel servResponse : baseModel.getIntent().getResponses()) {

					Response serviceResponse = new Response();
					serviceResponse.setKuId(servResponse.getKuId());
					serviceResponse.setResponseText(servResponse.getResponseText());
					serviceResponse.setLocaleCode(servResponse.getLocaleCode());
					serviceResponse.setGlobalIdentifier(servResponse.getGlobalIdentifier());
					serviceResponse.setMessage(message);

					if (baseModel.getIntent().getId() != null) {
						Intent intent = new Intent();
						intent.setId(baseModel.getIntent().getId());

						serviceResponse.setIntent(intent);
					}
					session.save(serviceResponse);
					resDetails.get(index).setId(serviceResponse.getId());
					index++;
					servResponse.setId(serviceResponse.getId());

					responseLst.add(serviceResponse);

				}
				message.setResponses(responseLst);
			} else {

				List<Response> responseLst = new ArrayList<>();
				for (ResponseModel servResponse : baseModel.getIntent().getResponses()) {

					Response serviceResponse = new Response();
					serviceResponse.setKuId(servResponse.getKuId());
					serviceResponse.setResponseText(servResponse.getResponseText());
					serviceResponse.setLocaleCode(servResponse.getLocaleCode());
					serviceResponse.setGlobalIdentifier(servResponse.getGlobalIdentifier());
					serviceResponse.setMessage(message);

					if (baseModel.getIntent().getId() != null) {
						Intent intent = new Intent();
						intent.setId(baseModel.getIntent().getId());

						serviceResponse.setIntent(intent);
					}
					session.save(serviceResponse);
					resDetails.get(index).setId(serviceResponse.getId());
					index++;
					servResponse.setId(serviceResponse.getId());

					responseLst.add(serviceResponse);

				}
				message.setId(baseModel.getMessage().getId());
				message.setMessageCode(UUID.randomUUID().toString());
				Intent intent = new Intent();
				intent.setId(baseModel.getIntent().getId());
				message.setIntent(intent);
				message.setResponses(responseLst);
			}
			tx.commit();

		} catch (HibernateException ex) {
			logger.error("+++++ IntentDbServicesImpl.createintentResponses END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.createintentResponses END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return message;
	}

	@Override
	public Object updateIntentResponse(BaseRequestModel baseModel) throws ApiException {

		Transaction tx = null;
		Message message = new Message();
		List<Response> resDetails = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();

			if (baseModel.getIntent().getResponses() != null) {

				for (ResponseModel servResponse : baseModel.getIntent().getResponses()) {

					CriteriaBuilder builder = session.getCriteriaBuilder();
					CriteriaUpdate<Response> update = builder.createCriteriaUpdate(Response.class);
					Root<Response> root = update.from(Response.class);
					update.set(root.get("intent"), baseModel.getIntent().getId());
					update.set(root.get("responseText"), servResponse.getResponseText());
					update.where(builder.equal(root.get("id"), servResponse.getId()));

					session.createQuery(update).executeUpdate();

				}
			} else {

				Intent intent = new Intent();
				intent.setId(baseModel.getIntent().getId());

				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Response> select = builder.createQuery(Response.class);
				Root<Response> root = select.from(Response.class);
				ParameterExpression<Intent> intnetId = builder.parameter(Intent.class);

				Predicate intentIdP = builder.equal(root.get("intent"), intnetId);
				Predicate and1 = builder.and(intentIdP);
				select.where(and1);

				resDetails = session.createQuery(select).setParameter(intnetId, intent).getResultList();

				for (Response servResponse : resDetails) {

					CriteriaBuilder builder2 = session.getCriteriaBuilder();
					CriteriaUpdate<Response> update = builder2.createCriteriaUpdate(Response.class);
					Root<Response> root2 = update.from(Response.class);
					update.set(root2.get("intent"), baseModel.getIntent().getResponses());
					update.set(root2.get("responseText"), baseModel.getIntent().getResponses());

					update.where(builder2.equal(root2.get("id"), servResponse.getId()));

					session.createQuery(update).executeUpdate();

				}

			}

			message.setId(baseModel.getMessage().getId());
			message.setResponses(resDetails);

			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ IntentDbServicesImpl.updateIntentResponse END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.updateIntentResponse END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);
		}

		return message;
	}

	@Override
	public Object getintentLst(BaseRequestModel baseModel) throws ApiException {
		Session session = null;

		List<Intent> intentLst = null;

		try {
			session = sessionFactory.openSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Intent> query = builder.createQuery(Intent.class);
			Root<Intent> root = query.from(Intent.class);
			query.select(root);
			query.orderBy(builder.desc(root.get("id")));
			intentLst = session.createQuery(query).getResultList();

		} catch (HibernateException ex) {
			logger.error("+++++ IntentDbServicesImpl.getintentLst END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.getintentLst END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		} finally {
			if (session != null) {
				session.close();
			}
		}

		return intentLst;
	}

	@Override
	public Object getWorkflowByIntentId(Long id) throws ApiException {

		WorkFlow workflow = null;
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<WorkFlow> select = builder.createQuery(WorkFlow.class);
			Root<WorkFlow> root = select.from(WorkFlow.class);
			ParameterExpression<Intent> intnetId = builder.parameter(Intent.class);
			Intent intent = new Intent();
			intent.setId(id);
			Predicate intentIdP = builder.equal(root.get("intent"), intnetId);
			Predicate and1 = builder.and(intentIdP);
			select.where(and1);

			List<WorkFlow> workflowDetails = session.createQuery(select).setParameter(intnetId, intent).getResultList();
			if (!workflowDetails.isEmpty())
				workflow = workflowDetails.get(0);

			logger.error("+++++ MappingDbServicesImpl.getWorkflowByIntentId Service End +++++");
		} catch (HibernateException ex) {
			logger.error(
					"+++++ MappingDbServicesImpl.getWorkflowByIntentId END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ MappingDbServicesImpl.getWorkflowByIntentId END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return workflow;
	}

	@Override
	public Object deleteintentbyIntentId(BaseRequestModel baseModel, String id) throws ApiException {

		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaDelete<Action> delete = builder.createCriteriaDelete(Action.class);
			Root<Action> root = delete.from(Action.class);
			Intent intent = new Intent();
			intent.setId(Long.parseLong(id));
			delete.where(builder.equal(root.get("intent"), intent));
			session.createQuery(delete).executeUpdate();
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error(
					"+++++ IntentDbServicesImpl.deleteintentbyIntentId END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.deleteintentbyIntentId END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return null;

	}

	@Override
	public Object getintentExtnDetails(BaseRequestModel baseModel) throws ApiException {
		Session session = null;

		List<IntentExtn> intentExtnLst = null;

		try {
			session = sessionFactory.openSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<IntentExtn> query = builder.createQuery(IntentExtn.class);
			Root<IntentExtn> root = query.from(IntentExtn.class);
			query.select(root);
			query.orderBy(builder.desc(root.get("id")));
			intentExtnLst = session.createQuery(query).getResultList();

		} catch (HibernateException ex) {
			logger.error("+++++ IntentDbServicesImpl.getintentExtnDetails END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.getintentExtnDetails END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		} finally {
			if (session != null) {
				session.close();
			}
		}

		return intentExtnLst;
	}

	public Object getCancelIntentByName(BaseRequestModel baseModel) throws ApiException {

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

	public Object getKeywordByName(BaseRequestModel baseModel, String keyword) throws ApiException {

		List<Keyword> keywordDetailsLst = null;
		Keyword keywordDetails = null;
		try (Session session = sessionFactory.openSession()) {

			Intent intent = (Intent) getCancelIntentByName(baseModel);

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Keyword> select = builder.createQuery(Keyword.class);
			Root<Keyword> root = select.from(Keyword.class);
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("keywordField"), keyword));
			predicates.add(builder.equal(root.get("intent"), intent.getId()));

			select.where(predicates.toArray(new Predicate[] {}));

			keywordDetailsLst = session.createQuery(select).getResultList();
			if (!keywordDetailsLst.isEmpty()) {
				keywordDetails = keywordDetailsLst.get(0);
			}
		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
			logger.error("+++++ IntentDbServicesImpl.getKeywordByIntentId END SERVICE WITH Hibernate EXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
		} catch (Exception ex) {
			logger.error("+++++ IntentDbServicesImpl.getKeywordByIntentId END SERVICE WITHEXCEPTION +++++");
			logger.error(Utility.getExceptionMessage(ex));
			Utility.commonExceptionMethod(ex);

		}

		return keywordDetails;
	}

}
