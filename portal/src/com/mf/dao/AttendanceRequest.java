package com.mf.dao;

import org.json.simple.JSONObject;

import com.mf.utils.MFParseUtils;

import org.apache.commons.codec.binary.Base64;

public class AttendanceRequest {
	private int user_id;
	private String mobile_request_id;
	private String mobileTimeStamp;
	private String platform;
	private double lat;
	private double lng;
	private long accuracy;
	private long distributor_id;
	private String device_id;
	private String version;
	private byte[] imageBytes;

	public AttendanceRequest() {
		super();
	}

	public AttendanceRequest(JSONObject payload, int is_mv_app) {
		this.user_id = MFParseUtils.parseInt((String) payload.get("user_id"));
		System.out.println(this.user_id);

		this.mobile_request_id = (String) payload.get("mobile_request_id");
		System.out.println(this.mobile_request_id);

		this.mobileTimeStamp = (String) payload.get("mobileTimeStamp");
		System.out.println(this.mobileTimeStamp);

		this.platform = (String) payload.get("platform");
		System.out.println(this.platform);

		this.lat = (Double) payload.get("lat");
		System.out.println(this.lat);

		this.lng = (Double) payload.get("lng");
		System.out.println(this.lng);

		this.accuracy = Math.round((Double) payload.get("accuracy"));
		System.out.println(this.accuracy);

		this.device_id = (String) payload.get("device_id");
		System.out.println(this.device_id);

		this.version = (String) payload.get("version");
		System.out.println(this.version);

		this.distributor_id = MFParseUtils.parseLong((String) payload.get("distributor_id"));
		System.out.println(this.distributor_id);

		this.imageBytes =  Base64.decodeBase64((String) payload.get("imageBytes"));
		System.out.println(this.imageBytes);
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getMobile_request_id() {
		return mobile_request_id;
	}

	public void setMobile_request_id(String mobile_request_id) {
		this.mobile_request_id = mobile_request_id;
	}

	public String getMobileTimeStamp() {
		return mobileTimeStamp;
	}

	public void setMobileTimeStamp(String mobileTimeStamp) {
		this.mobileTimeStamp = mobileTimeStamp;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
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

	public long getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(long accuracy) {
		this.accuracy = accuracy;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public byte[] getImageBytes() {
		return this.imageBytes;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}

	public long getDistributor_id() {
		return this.distributor_id;
	}

	public void setDistributor_id(long distributor_id) {
		this.distributor_id = distributor_id;
	}

}
