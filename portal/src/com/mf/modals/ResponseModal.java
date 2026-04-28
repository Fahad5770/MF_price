package com.mf.modals;

import java.util.LinkedHashMap;

import org.json.simple.JSONObject;

public class ResponseModal {

	private boolean status = false;
	private String userMessage = "";
	private LinkedHashMap<String, Object> dataObject = new LinkedHashMap<String, Object>();

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	public LinkedHashMap<String, Object> getData() {
		return dataObject;
	}

	public void setData(LinkedHashMap<String, Object> data) {
		this.dataObject = data;
	}

	public void setErrorResponse(String message) {
		this.status = false;
		this.userMessage = message;
		this.dataObject.clear();
	}
	
	public void setSuccessResponse(String message, LinkedHashMap<String, Object> dataObject ) {
		this.status = true;
		this.userMessage = message;
		this.dataObject = dataObject;
	}

	@SuppressWarnings("unchecked")
	public JSONObject getResponseJson() {
		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put("status", this.status);
		jsonResponse.put("user_message", this.userMessage);
		jsonResponse.put("data", this.dataObject);
		return jsonResponse;
	}

}

