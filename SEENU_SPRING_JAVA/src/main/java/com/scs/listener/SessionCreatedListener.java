package com.scs.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionCreatedEvent;

import com.scs.util.ApiConstants;

public class SessionCreatedListener implements ApplicationListener<HttpSessionCreatedEvent> {

	@Override
	public void onApplicationEvent(HttpSessionCreatedEvent event) {
		
		event.getSession().setMaxInactiveInterval(ApiConstants.SESSION_MAX_INTERVAL);
	}
}