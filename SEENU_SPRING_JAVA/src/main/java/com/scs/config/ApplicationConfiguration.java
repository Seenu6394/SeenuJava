package com.scs.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scs.interceptor.ApiInterceptor;
import com.scs.util.ApiConstants;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.scs")
//@PropertySource("classpath:config.properties"),
//@PropertySource("file:/C:/Weblogic/wls12210/user_projects/domains/base_domain/properties/config.properties"),
//@PropertySource("file:/C:/Weblogic/wls12210/user_projects/domains/staging/properties/config.properties"),
//@PropertySource("file:/C:/Weblogic/wls12210/user_projects/domains/dev_test/properties/config.properties"),
//@PropertySource("file:/C:/Oracle/Middleware/Oracle_Home/user_projects/domains/prod1/properties/config.properties"),



@PropertySources({
	@PropertySource("file:/C:/Weblogic/wls12210/user_projects/domains/base_domain/properties/config.properties"),
	@PropertySource("classpath:domain.properties")
})

@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ApplicationConfiguration extends WebMvcConfigurerAdapter {

	@Autowired
	Environment env;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(MediaType.APPLICATION_JSON);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(apiInterceptor()).addPathPatterns("/**");
	}

	@Bean
	ApiInterceptor apiInterceptor() {
		return new ApiInterceptor();
	}

	@Bean
	public ObjectMapper mapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper;
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {

		ResourceBundleMessageSource msgSource = new ResourceBundleMessageSource();
		msgSource.setBasename(ApiConstants.APP_MESSAGES_RESOURCE_BASENAME);
		return msgSource;
	}

	@Bean
	public ViewResolver getViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setPackagesToScan(new String[] { "com.scs.entity", "com.scs.service" });
		sessionFactory.setHibernateProperties(getHibernateProperties());
		return sessionFactory;
	}

	private Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.put(AvailableSettings.DIALECT, env.getProperty("scs.db.hibernate.dialect"));
		properties.put(AvailableSettings.SHOW_SQL, false);
		properties.put(AvailableSettings.HBM2DDL_AUTO, env.getProperty("scs.db.hibernate.hbm2ddl_auto"));
		properties.put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS,
				"org.springframework.orm.hibernate5.SpringSessionContext");
		return properties;
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("scs.db.driverclass"));
		dataSource.setUrl(env.getProperty("scs.db.url"));
		dataSource.setUsername(env.getProperty("scs.db.username"));
		dataSource.setPassword(env.getProperty("scs.db.password"));

		return dataSource;
	}

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		return new CommonsMultipartResolver();

	}

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		Properties mailProperties = new Properties();
		mailProperties.put("mail.smtp.auth", true);
		mailProperties.put("mail.smtp.starttls.enable", true);
		mailProperties.put("mail.smtp.socketFactory.port", env.getProperty("scs.mail.port"));
		mailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		mailSender.setJavaMailProperties(mailProperties);
		mailSender.setHost(env.getProperty("scs.mail.host"));
		mailSender.setPort(Integer.parseInt(env.getProperty("scs.mail.port")));
		mailSender.setProtocol(env.getProperty("scs.mail.protocol"));
		mailSender.setUsername(env.getProperty("scs.mail.username"));
		mailSender.setPassword(env.getProperty("scs.mail.password"));
		return mailSender;
	}

	@Bean
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);
		return txManager;
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

}