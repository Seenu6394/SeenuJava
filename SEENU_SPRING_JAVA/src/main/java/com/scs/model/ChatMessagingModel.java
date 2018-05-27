package com.scs.model;

public class ChatMessagingModel {

	private SenderModel sender = new SenderModel();
	
	private RecipientModel recipient = new RecipientModel();
	
	private MessageModel message = new MessageModel();

	public SenderModel getSender() {
		return sender;
	}

	public void setSender(SenderModel sender) {
		this.sender = sender;
	}

	public RecipientModel getRecipient() {
		return recipient;
	}

	public void setRecipient(RecipientModel recipient) {
		this.recipient = recipient;
	}

	public MessageModel getMessage() {
		return message;
	}

	public void setMessage(MessageModel message) {
		this.message = message;
	}
		
}
