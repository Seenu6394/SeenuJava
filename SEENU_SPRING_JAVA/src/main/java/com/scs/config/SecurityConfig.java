package com.scs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

import com.scs.service.impl.UserDetailsServiceImpl;
import com.scs.util.CustomAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
// @EnableGlobalMethodSecurity(securedEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationEntryPoint authEntryPoint;

	@Autowired
	private UserDetailsServiceImpl myAppUserDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
		.antMatchers("/api/reset*","/api/forgot*","/api/importFiles*","/api/socket", "/api/s4mBotLogin*").permitAll()
		.antMatchers("/api/**").hasAnyRole("ADMIN")
		.and().formLogin()
		.usernameParameter("username")
		.passwordParameter("password")
		.defaultSuccessUrl("/api/loginSuccess")
		//.failureUrl("/api/loginFailure")
		.failureHandler(new CustomAuthenticationFailureHandler())
		 .and().logout()
	    .logoutUrl("/api/logout")
	    .and().csrf().disable()
        .addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class)
	    .exceptionHandling()
	    .authenticationEntryPoint(authEntryPoint);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myAppUserDetailsService).passwordEncoder(passwordEncoder());

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}