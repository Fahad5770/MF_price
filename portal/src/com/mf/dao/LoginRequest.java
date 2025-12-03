package com.mf.dao;

import org.json.simple.JSONObject;

public class LoginRequest {

	private int user_id;
	private double lat;
	private double lng;
	private long accuracy;
	private String password;
	private String app_version;
	private String mobileTimeStamp;
	private String device_id;

	public LoginRequest() {

	}

	public LoginRequest(JSONObject Payload) {
		this.user_id = ((Number) Payload.get("user_id")).intValue();
		this.lat = (Double) Payload.get("lat");
		this.lng = (Double) Payload.get("lng");
		this.accuracy = Math.round((Double) Payload.get("accuracy"));
		this.password = (String) Payload.get("password");
		this.app_version = (String) Payload.get("app_version");
		this.mobileTimeStamp = (String) Payload.get("mobileTimeStamp");
		this.device_id = (String) Payload.get("device_id");

		System.out.println("  LoginRequest [user_id=" + user_id + ", lat=" + lat + ", lng=" + lng + ", accuracy="
				+ accuracy + ", password=" + password + ", app_version=" + app_version + ", mobileTimeStamp="
				+ mobileTimeStamp + ", device_id=" + device_id + "]");
	}

	public void LoginRequestForOB(JSONObject Payload) {
		this.user_id = ((Number) Payload.get("user_id")).intValue();
		this.password = (String) Payload.get("password");
		this.app_version = (String) Payload.get("app_version");
		this.device_id = (String) Payload.get("device_id");
	}

	@Override
	public String toString() {
		return "LoginRequest [user_id=" + user_id + ", lat=" + lat + ", lng=" + lng + ", accuracy=" + accuracy
				+ ", password=" + password + ", app_version=" + app_version + ", mobileTimeStamp=" + mobileTimeStamp
				+ ", device_id=" + device_id + "]";
	}

	public int getUser_id() {
		return user_id;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(long accuracy) {
		this.accuracy = accuracy;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getApp_version() {
		return app_version;
	}

	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}

	public String getMobileTimeStamp() {
		return mobileTimeStamp;
	}

	public void setMobileTimeStamp(String mobileTimeStamp) {
		this.mobileTimeStamp = mobileTimeStamp;
	}

}
