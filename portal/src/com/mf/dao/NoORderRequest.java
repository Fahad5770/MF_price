package com.mf.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class NoORderRequest {
	private int userId;
	private long outletId;
	private String mobileRequestId;
	private String startMobileTimestamp;
	private String endMobileTimestamp;
	private String deviceId;
	private int reasonId;
	private String reason;
	private String version;
	private String platform;
	private double lat;
	private double lng;
	private long accuracy;
	private int beatPlanId;
	private long distributorId;

	private List<NoORderRequestProducts> products = new ArrayList<NoORderRequestProducts>();

	public NoORderRequest() {
	}

	public NoORderRequest(JSONObject Payload) {
		this.userId = ((Number) Payload.get("user_id")).intValue();
		this.outletId = (Long) Payload.get("outlet_id");
		this.mobileRequestId = (String) Payload.get("mobile_request_id");
		this.startMobileTimestamp = (String) Payload.get("start_mobile_timestamp");
		this.endMobileTimestamp = (String) Payload.get("end_mobile_timestamp");
		this.deviceId = (String) Payload.get("device_id");
		this.reasonId = ((Number) Payload.get("reason_id")).intValue();
		this.reason = (String) Payload.get("reason");
		this.version = (String) Payload.get("version");
		this.platform = (String) Payload.get("platform");
		this.lat = (Double) Payload.get("lat");
		this.lng = (Double) Payload.get("lng");
		double accuracyD = (Double) Payload.get("accuracy");
		this.accuracy = Math.round(accuracyD);
		this.beatPlanId = ((Number) Payload.get("beat_plan_id")).intValue();
		this.distributorId = (Long) Payload.get("distributor_id");
		System.out.println((String) Payload.get("imageBytes"));
	
		if (((JSONArray) Payload.get("products")) != null) {
			final JSONArray ProductsArray = (JSONArray) Payload.get("products");

			for (int i = 0; i < ProductsArray.size(); i++) {
				JSONObject productJson = (JSONObject) ProductsArray.get(i);
				NoORderRequestProducts noORderRequestProducts = new NoORderRequestProducts(
						((Number) productJson.get("product_id")).intValue(),
						((Number) productJson.get("quantity")).intValue());
				this.products.add(noORderRequestProducts);
			}
		}

	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public long getOutletId() {
		return this.outletId;
	}

	public void setOutletId(long outletId) {
		this.outletId = outletId;
	}

	public String getMobileRequestId() {
		return this.mobileRequestId;
	}

	public void setMobileRequestId(String mobileRequestId) {
		this.mobileRequestId = mobileRequestId;
	}

	public String getStartMobileTimestamp() {
		return this.startMobileTimestamp;
	}

	public void setStartMobileTimestamp(String startMobileTimestamp) {
		this.startMobileTimestamp = startMobileTimestamp;
	}

	public String getEndMobileTimestamp() {
		return this.endMobileTimestamp;
	}

	public void setEndMobileTimestamp(String endMobileTimestamp) {
		this.endMobileTimestamp = endMobileTimestamp;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getReasonId() {
		return this.reasonId;
	}

	public void setReasonId(int reasonId) {
		this.reasonId = reasonId;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPlatform() {
		return this.platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public double getLat() {
		return this.lat;
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

	public int getBeatPlanId() {
		return beatPlanId;
	}

	public void setBeatPlanId(int beatPlanId) {
		this.beatPlanId = beatPlanId;
	}

	public long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(long distributorId) {
		this.distributorId = distributorId;
	}


	public List<NoORderRequestProducts> getProducts() {
		return products;
	}

	public void setProducts(List<NoORderRequestProducts> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "NoORderRequest [userId=" + userId + ", outletId=" + outletId + ", mobileRequestId=" + mobileRequestId
				+ ", startMobileTimestamp=" + startMobileTimestamp + ", endMobileTimestamp=" + endMobileTimestamp
				+ ", deviceId=" + deviceId + ", reasonId=" + reasonId + ", reason=" + reason + ", version=" + version
				+ ", platform=" + platform + ", lat=" + lat + ", lng=" + lng + ", accuracy=" + accuracy
				+ ", beatPlanId=" + beatPlanId + ", distributorId=" + distributorId + ", products=" + products + "]";
	}

}
