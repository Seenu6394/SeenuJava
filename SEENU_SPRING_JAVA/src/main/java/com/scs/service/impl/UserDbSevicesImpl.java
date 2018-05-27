package com.scs.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.scs.entity.model.Languages;
import com.scs.entity.model.Locale;
import com.scs.entity.model.UserInfo;
import com.scs.entity.model.UserLogin;
import com.scs.entity.model.UserToken;
import com.scs.exception.ApiException;
import com.scs.model.BaseRequestModel;
import com.scs.service.UserDbServices;
import com.scs.util.ApiConstants;
import com.scs.util.SessionUtil;
import com.scs.util.StorageConstants;
import com.scs.util.Utility;

@Service("UserDbService")
@Component
public class UserDbSevicesImpl implements UserDbServices {

	private static final Logger logger = Logger.getLogger(UserDbSevicesImpl.class);
	private static final String PROPERTY_LOC = "scs.mail.subject";
	private static final String COLUMN_NAME = "userName";

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	Environment env;

	@Override
	public UserInfo getUserByName(String name) throws ApiException {

		UserInfo user = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<UserInfo> select = builder.createQuery(UserInfo.class);
			Root<UserInfo> root = select.from(UserInfo.class);
			ParameterExpression<String> userName = builder.parameter(String.class);
			Predicate userNameP = builder.equal(root.get(COLUMN_NAME), userName);
			Predicate and1 = builder.and(userNameP);
			select.where(and1);

			List<UserInfo> userDetails = session.createQuery(select).setParameter(userName, name).getResultList();
			if (!userDetails.isEmpty())
				user = userDetails.get(0);

		} catch (HibernateException ex) {

			Utility.commonHibernateExceptionMethod(ex);

		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}

		return user;
	}

	@Override
	public boolean updateSuccess(String name) throws ApiException {

		Transaction tx = null;
		UserInfo user = null;
		boolean status = false;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<UserInfo> select = builder.createQuery(UserInfo.class);
			Root<UserInfo> root = select.from(UserInfo.class);
			ParameterExpression<String> userName = builder.parameter(String.class);
			Predicate userNameP = builder.equal(root.get(COLUMN_NAME), userName);
			Predicate and1 = builder.and(userNameP);
			select.where(and1);

			List<UserInfo> userDetails = session.createQuery(select).setParameter(userName, name).getResultList();
			if (!userDetails.isEmpty()) {

				user = userDetails.get(0);
				CriteriaBuilder builder1 = session.getCriteriaBuilder();
				CriteriaQuery<UserLogin> select1 = builder1.createQuery(UserLogin.class);
				Root<UserLogin> root1 = select1.from(UserLogin.class);
				ParameterExpression<Long> userId = builder1.parameter(Long.class);
				Predicate userIdP = builder1.equal(root1.get("userid"), userId);
				Predicate and2 = builder1.and(userIdP);
				select1.where(and2);

				List<UserLogin> userLoginDetails = session.createQuery(select1).setParameter(userId, user.getId())
						.getResultList();

				if (!userLoginDetails.isEmpty()) {

					userLoginDetails.get(0).setLoggedInTime(Instant.now().toString());
					userLoginDetails.get(0).setUserid(user.getId());
					userLoginDetails.get(0).setLoginStatus("Success");
					userLoginDetails.get(0).setLoginAttempts(0L);
					session.update(userLoginDetails.get(0));
					tx.commit();

				} else {

					UserLogin userLogin = new UserLogin();
					userLogin.setLoggedInTime(Instant.now().toString());
					userLogin.setUserid(user.getId());
					userLogin.setLoginStatus("Success");
					userLogin.setLoginAttempts(0L);
					session.save(userLogin);
					tx.commit();
					status = true;
				}
			}
		} catch (HibernateException ex) {

			Utility.commonHibernateExceptionMethod(ex);

		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}
		return status;
	}

	@Override
	public Object getUsersDetails(BaseRequestModel baseModel) throws ApiException {
		
		List<UserInfo> userLst = null;
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<UserInfo> query = builder.createQuery(UserInfo.class);
			Root<UserInfo> root = query.from(UserInfo.class);
			query.select(root);
			query.orderBy(builder.desc(root.get("id")));
			userLst = session.createQuery(query).getResultList();

		} catch (HibernateException ex) {

			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		} 

		return userLst;
	}

	@Override
	public Object getLanguages(BaseRequestModel baseModel) throws ApiException {
		
		List<Languages> languageLst = new ArrayList<>();
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Languages> query = builder.createQuery(Languages.class);
			Root<Languages> root = query.from(Languages.class);
			query.select(root);
			query.orderBy(builder.desc(root.get("id")));
			languageLst = session.createQuery(query).getResultList();

		} catch (HibernateException ex) {

			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		} 

		return languageLst.get(0);
	}

	@Override
	public Object getLocale(BaseRequestModel baseModel) throws ApiException {
	
		List<Locale> languageLst = null;
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Locale> query = builder.createQuery(Locale.class);
			Root<Locale> root = query.from(Locale.class);
			query.select(root);
			query.orderBy(builder.desc(root.get("id")));
			languageLst = session.createQuery(query).getResultList();

		} catch (HibernateException ex) {

			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		} 

		return languageLst;
	}

	@Override
	public Object updateLanguage(BaseRequestModel baseModel) throws ApiException {
		
		Transaction tx = null;
		String response = ApiConstants.SUCCESS;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<Languages> update = builder.createCriteriaUpdate(Languages.class);
			Root<Languages> root = update.from(Languages.class);
			update.set(root.get("english"), baseModel.getLanguage().getEnglish());
			update.set(root.get("arabic"), baseModel.getLanguage().getArabic());

			update.where(builder.equal(root.get("id"), baseModel.getLanguage().getId()));
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
	public Object createUser(BaseRequestModel baseModel) throws ApiException {
	
		Transaction tx = null;
		UserInfo user = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			int noOfCAPSAlpha = 1;
			int noOfDigits = 1;
			int noOfSplChars = 1;
			int minLen = 8;
			int maxLen = 16;

			user = new UserInfo();
			user.setId(null);
			user.setEmail(baseModel.getUser().getEmail());
			user.setFirstName(baseModel.getUser().getFirstName());
			user.setLastName(baseModel.getUser().getLastName());
			user.setEnabled((short) 1);
			user.setFullName(baseModel.getUser().getFullName());

			String pwd = RandomPasswordGenerator.generatePswd(minLen, maxLen, noOfCAPSAlpha, noOfDigits, noOfSplChars);

			user.setPassword(passwordEncoder().encode(pwd));

			user.setRole("ROLE_" + baseModel.getUser().getRole().toUpperCase());
			user.setUserName(baseModel.getUser().getUserName());

			session.save(user);
			tx.commit();

			sendPasswordMail(baseModel.getUser().getEmail(), pwd);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);

		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		} 

		return user;
	}

	@Async
	private void sendPasswordMail(String email, String password) {

		logger.debug(env.getProperty(PROPERTY_LOC));
		SimpleMailMessage passwordEmail = new SimpleMailMessage();
		passwordEmail.setTo(email);
		passwordEmail.setSubject("Password for BotChestra");
		passwordEmail.setText("Your password is " + password);
		mailSender.send(passwordEmail);

	}

	@Override
	public Object updateUser(BaseRequestModel baseModel) throws ApiException {
		
		Transaction tx = null;
		String response = ApiConstants.SUCCESS;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<UserInfo> update = builder.createCriteriaUpdate(UserInfo.class);
			Root<UserInfo> root = update.from(UserInfo.class);
			update.set(root.get(COLUMN_NAME), baseModel.getUser().getUserName());
			update.set(root.get("fullName"), baseModel.getUser().getFullName());
			update.set(root.get("firstName"), baseModel.getUser().getFirstName());
			update.set(root.get("lastName"), baseModel.getUser().getLastName());
			update.set(root.get("enabled"), baseModel.getUser().getEnabled());
			update.set(root.get("role"), "ROLE_" + baseModel.getUser().getRole().toUpperCase());
			update.set(root.get("email"), baseModel.getUser().getEmail());
			update.where(builder.equal(root.get("id"), baseModel.getUser().getId()));
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
	public Object deleteUser(BaseRequestModel baseModel, String id) throws ApiException {
	
		Transaction tx = null;
		String response = ApiConstants.SUCCESS;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			UserInfo user = new UserInfo();
			user.setId(Long.parseLong(id));
			session.delete(user);

			tx.commit();

		} catch (HibernateException ex) {

			Utility.commonHibernateExceptionMethod(ex);

		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		} 

		return response;
	}

	@Override
	public Object getUserById(BaseRequestModel baseModel, String id) throws ApiException {
		
		UserInfo user = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<UserInfo> select = builder.createQuery(UserInfo.class);
			Root<UserInfo> root = select.from(UserInfo.class);
			ParameterExpression<Long> userId = builder.parameter(Long.class);
			Predicate flowChartIdP = builder.equal(root.get("id"), userId);
			Predicate and1 = builder.and(flowChartIdP);
			select.where(and1);

			List<UserInfo> userDetails = session.createQuery(select).setParameter(userId, Long.parseLong(id))
					.getResultList();
			if (!userDetails.isEmpty())
				user = userDetails.get(0);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		} 

		return user;
	}

	@Override
	public Object findUserByEmail(String email, HttpSession httpSession) throws ApiException {
	
		UserInfo user = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<UserInfo> select = builder.createQuery(UserInfo.class);
			Root<UserInfo> root = select.from(UserInfo.class);
			ParameterExpression<String> emailId = builder.parameter(String.class);
			Predicate emailIdP = builder.equal(root.get("email"), emailId);
			Predicate and1 = builder.and(emailIdP);
			select.where(and1);

			List<UserInfo> userDetails = session.createQuery(select).setParameter(emailId, email).getResultList();
			if (!userDetails.isEmpty())
				user = userDetails.get(0);
			else {
				throw new ApiException("INVALID_EMAIL", "This email doesn't exist");
			}

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (ApiException ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}

		return user;
	}

	@Override
	public Object findUserByResetToken(String resetToken, HttpSession httpSession) throws ApiException {
		
		UserToken userToken = null;

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<UserToken> select = builder.createQuery(UserToken.class);
			Root<UserToken> root = select.from(UserToken.class);
			ParameterExpression<String> token = builder.parameter(String.class);
			Predicate tokenP = builder.equal(root.get("token"), token);
			Predicate and1 = builder.and(tokenP);
			select.where(and1);

			List<UserToken> tokenDetails = session.createQuery(select).setParameter(token, resetToken).getResultList();
			if (!tokenDetails.isEmpty()) {
				userToken = tokenDetails.get(0);

				Duration duration = Duration.between(Instant.now(), userToken.getExpiryDate());
				if (duration.getSeconds() <= 0) {
					throw new ApiException("INVALID_TOKEN", "This token is invalid or expired");
				}
				SessionUtil.setValue(httpSession, StorageConstants.USER, userToken.getUser());

			} else {
				throw new ApiException("INVALID_TOKEN", "This token is invalid or expired");
			}

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		} 
		return userToken;
	}

	@Override
	public Object createUserToken(UserInfo user, HttpSession httpSession) throws ApiException {
		
		Transaction tx = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			UserToken userToken = new UserToken();
			userToken.setEnabled((short) 1);
			userToken.setUser(user);
			userToken.setToken(UUID.randomUUID().toString());
			userToken.setExpiryDate(Instant.now().plus(24L * 7, ChronoUnit.HOURS));
			session.save(userToken);

			tx.commit();

			sendMail(user, userToken);

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}

		return ApiConstants.SUCCESS;
	}

	@Override
	public Object resetPassword(String password, HttpSession httpSession) throws ApiException {
		
		Transaction tx = null;
		UserInfo user = SessionUtil.getValue(httpSession, StorageConstants.USER);
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<UserInfo> update = builder.createCriteriaUpdate(UserInfo.class);
			Root<UserInfo> root = update.from(UserInfo.class);
			update.set(root.get("password"), passwordEncoder().encode(password));
			update.where(builder.equal(root.get("id"), user.getId()));
			session.createQuery(update).executeUpdate();
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}

		return ApiConstants.SUCCESS;
	}

	@Override
	public Object changePassword(BaseRequestModel baseModel, HttpSession httpSession) throws ApiException {
		
		Transaction tx = null;
		UserInfo user = SessionUtil.getValue(httpSession, StorageConstants.USER);
		
		try (Session session = sessionFactory.openSession()) {
			if (passwordEncoder().matches(baseModel.getUser().getPassword(), user.getPassword())) {
				throw new ApiException("RECENT_PASSWORD", "Please use a password which is not recently used");
			}

			if (!passwordEncoder().matches(baseModel.getUser().getCurrentPassword(), user.getPassword())) {
				throw new ApiException("INVALID_CURRENT_PASSWORD", "Current password doesn't match");
			}


			tx = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaUpdate<UserInfo> update = builder.createCriteriaUpdate(UserInfo.class);
			Root<UserInfo> root = update.from(UserInfo.class);
			update.set(root.get("password"), passwordEncoder().encode(baseModel.getUser().getNewPassword()));
			update.where(builder.equal(root.get("id"), user.getId()));
			session.createQuery(update).executeUpdate();
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (ApiException ex) {
			logger.error(Utility.getExceptionMessage(ex));
			throw new ApiException(ex.getErrorCode(), ex.getErrorMessage());
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		}

		return ApiConstants.SUCCESS;
	}

	@Async
	private void sendMail(UserInfo user, UserToken userToken) {

		logger.debug(env.getProperty(PROPERTY_LOC));
		SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
		passwordResetEmail.setTo(user.getEmail());
		passwordResetEmail.setSubject(env.getProperty(PROPERTY_LOC));
		passwordResetEmail.setText(env.getProperty("scs.mail.text"));
		mailSender.send(passwordResetEmail);

	}

	@Override
	public Object deleteUserToken(UserInfo user) throws ApiException {
		
		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			UserToken userToken = new UserToken();
			userToken.setId(user.getUserToken().getId());
			session.delete(userToken);
			tx.commit();

		} catch (HibernateException ex) {
			Utility.commonHibernateExceptionMethod(ex);
		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		} 

		return ApiConstants.SUCCESS;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public Object loginFailure(String name) throws ApiException {
		

		Transaction tx = null;
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<UserInfo> select = builder.createQuery(UserInfo.class);
			Root<UserInfo> root = select.from(UserInfo.class);
			ParameterExpression<String> userName = builder.parameter(String.class);
			Predicate userNameP = builder.equal(root.get(COLUMN_NAME), userName);
			Predicate and1 = builder.and(userNameP);
			select.where(and1);

			List<UserInfo> userDetails = session.createQuery(select).setParameter(userName, name).getResultList();

			if (!userDetails.isEmpty()) {

				userDetails.get(0);

				tx = session.beginTransaction();

				CriteriaBuilder builder1 = session.getCriteriaBuilder();
				CriteriaQuery<UserLogin> select1 = builder1.createQuery(UserLogin.class);
				Root<UserLogin> root1 = select1.from(UserLogin.class);
				ParameterExpression<Long> userId = builder1.parameter(Long.class);
				Predicate userIdP = builder1.equal(root1.get("userid"), userId);
				Predicate and2 = builder1.and(userIdP);
				select1.where(and2);

				List<UserLogin> userLoginDetails = session.createQuery(select1)
						.setParameter(userId, userDetails.get(0).getId()).getResultList();

				if (!userLoginDetails.isEmpty()) {

					if (userLoginDetails.get(0).getLoginAttempts() < ApiConstants.MAX_ATTEMPTS) {

						userLoginDetails.get(0).setLoggedInTime(Instant.now().toString());
						userLoginDetails.get(0).setUserid(userDetails.get(0).getId());
						userLoginDetails.get(0).setLoginStatus("Failure");
						userLoginDetails.get(0).setLoginAttempts(userLoginDetails.get(0).getLoginAttempts() + 1L);
						session.update(userLoginDetails.get(0));
						tx.commit();
						return ApiConstants.LOGIN_FAILURE_STATUS;

					} else {

						userDetails.get(0).setAccountLocked(1L);
						session.update(userDetails.get(0));
						tx.commit();
						return ApiConstants.ATTEMPT_FAILURE;
					}
				} else {
					return ApiConstants.LOGIN_FAILURE_STATUS;
				}
			}
		} catch (HibernateException ex) {

			Utility.commonHibernateExceptionMethod(ex);

		} catch (Exception ex) {
			Utility.commonExceptionMethod(ex);

		} 

		return ApiConstants.LOGIN_FAILURE_STATUS;
	}

	@Override
	public Object deleteUserLoginDetails(Long userid) throws ApiException {
		
		Transaction tx = null;

		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			UserLogin userLogin = new UserLogin();
			userLogin.setUserid(userid);
			session.delete(userLogin);
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

}
