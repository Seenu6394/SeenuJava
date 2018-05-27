package com.scs.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;

public class SessionDestroyedListener implements ApplicationListener<HttpSessionDestroyedEvent> {
	@Override
	public void onApplicationEvent(HttpSessionDestroyedEvent event) {
		// onApplicationEvent
	}
	
}
