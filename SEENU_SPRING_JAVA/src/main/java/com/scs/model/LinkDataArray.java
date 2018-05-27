package com.scs.model;

import java.util.List;

public class LinkDataArray {

	private Long id;

	private String to;

	private String fromPort;

	private String[] points;

	private String from;

	private String toPort;

	private Long modelid;

	private Boolean visible;

	private ActionModel action;

	private FromNodeModel fromNode;

	private ToNodeModel toNode;

	public Boolean getOrder() {
		return order;
	}

	public void setOrder(Boolean order) {
		this.order = order;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	private String text;

	private String intentName;

	private List<ResponseModel> response;

	private Boolean order;

	private Long orderId;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFromPort() {
		return fromPort;
	}

	public void setFromPort(String fromPort) {
		this.fromPort = fromPort;
	}

	public String[] getPoints() {
		return points;
	}

	public void setPoints(String[] points) {
		this.points = points;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getToPort() {
		return toPort;
	}

	public void setToPort(String toPort) {
		this.toPort = toPort;
	}

	public Long getModelid() {
		return modelid;
	}

	public void setModelid(Long modelid) {
		this.modelid = modelid;
	}

	public FromNodeModel getFromNode() {
		return fromNode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFromNode(FromNodeModel fromNode) {
		this.fromNode = fromNode;
	}

	public ToNodeModel getToNode() {
		return toNode;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public void setToNode(ToNodeModel toNode) {
		this.toNode = toNode;
	}

	public ActionModel getAction() {
		return action;
	}

	public void setAction(ActionModel action) {
		this.action = action;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIntentName() {
		return intentName;
	}

	public void setIntentName(String intentName) {
		this.intentName = intentName;
	}

	public List<ResponseModel> getResponse() {
		return response;
	}

	public void setResponse(List<ResponseModel> response) {
		this.response = response;
	}

}
