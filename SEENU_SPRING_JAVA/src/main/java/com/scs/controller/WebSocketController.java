package com.scs.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.scs.model.RequestModel;
import com.scs.model.SampleJson;
import com.scs.util.Utility;


@Controller
public class WebSocketController {
	private static final Logger logger = Logger.getLogger(WebSocketController.class);
	private final SimpMessagingTemplate template;

	@Autowired
	WebSocketController(SimpMessagingTemplate template) {
		this.template = template;
	}

	@MessageMapping("/send/message")
	public void onReceivedMesage(String message, SimpMessageHeaderAccessor headerAccessor) {
		String sessionId = headerAccessor.getSessionId();
		headerAccessor.setSessionId(sessionId);
		sendToScala(message, sessionId);

	}

	public void sendToScala(String message, String sessionID) {
		final String uri = "http://b29a75ce.ngrok.io/scs/webhook";
		try {
			
			SampleJson sJ = new SampleJson();
			sJ.setEvent("message");
			sJ.setConversation_id(754L);
			sJ.setSupport_catagory_id(1L);
			sJ.setSupport_catagory_name("General");
			sJ.setOperator_id(null);
			sJ.setOperator_name(null);
			sJ.setEnd_customer_id(6L);
			sJ.setEnd_customer_name("user1");
			sJ.setBody(message);
			sJ.setFile_url(null);
			sJ.setSessionId(sessionID);

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.postForObject(uri, sJ, SampleJson.class);

		} catch (HttpClientErrorException ex) {
			logger.error(Utility.getExceptionMessage(ex));
		} 
	}

	@PostMapping(value = "/postMessage", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object postMessagetoSocket(@RequestBody RequestModel requestModel) {
		this.template.convertAndSend("/chat/" + requestModel.getSessionId(), requestModel.getMessage());
		return requestModel;
	}

}

