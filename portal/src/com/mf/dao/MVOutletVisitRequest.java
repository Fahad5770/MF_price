package com.mf.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MVOutletVisitRequest {
	private int userId;
	private long outletId;
	private String mobileRequestId;
	private String startMobileTimestamp;
	private String endMobileTimestamp;
	private String deviceId;
	private String version;
	private String platform;
	private double lat;
	private double lng;
	private long accuracy;
	private int beatPlanId;
	private long distributorId;
	private int city_id;
	private int region_id;
	private int rsm_id;
	private int asm_id;
	private int tso_id;
	private int reason_id;
	private String comments = "";

	private List<ProductsRequest> products = new ArrayList<ProductsRequest>();

	public MVOutletVisitRequest() {
	}

	public MVOutletVisitRequest(JSONObject Payload) {
		this.userId = ((Number) Payload.get("user_id")).intValue();
		System.out.println(userId);
		this.outletId = (Long) Payload.get("outlet_id");
		System.out.println(outletId);
		this.mobileRequestId = (String) Payload.get("mobile_request_id");
		System.out.println(mobileRequestId);
		this.startMobileTimestamp = (String) Payload.get("start_mobile_timestamp");
		System.out.println(startMobileTimestamp);
		this.endMobileTimestamp = (String) Payload.get("end_mobile_timestamp");
		System.out.println(endMobileTimestamp);
		this.deviceId = (String) Payload.get("device_id");
		this.version = (String) Payload.get("app_version");
		this.platform = (String) Payload.get("platform");
		this.lat = (Double) Payload.get("lat");
		this.lng = (Double) Payload.get("lng");
		this.accuracy = (Long) Payload.get("accuracy");
		this.beatPlanId = ((Number) Payload.get("beat_plan_id")).intValue();
		this.distributorId = (Long) Payload.get("distributor_id");
		this.city_id = ((Number) Payload.get("city_id")).intValue();
		this.region_id = ((Number) Payload.get("region_id")).intValue();
		this.rsm_id = ((Number) Payload.get("rsm_id")).intValue();
		this.asm_id = ((Number) Payload.get("asm_id")).intValue();
		this.tso_id = ((Number) Payload.get("tso_id")).intValue();
		this.reason_id = ((Number) Payload.get("reason_id")).intValue();
		this.comments = (String) Payload.get("comments");

		if (((JSONArray) Payload.get("products")) != null) {
			final JSONArray ProductsArray = (JSONArray) Payload.get("products");

			for (int i = 0; i < ProductsArray.size(); i++) {
				JSONObject productJson = (JSONObject) ProductsArray.get(i);
				ProductsRequest noORderRequestProducts = new ProductsRequest(
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

	public List<ProductsRequest> getProducts() {
		return products;
	}

	public void setProducts(List<ProductsRequest> products) {
		this.products = products;
	}

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	public int getRegion_id() {
		return region_id;
	}

	public void setRegion_id(int region_id) {
		this.region_id = region_id;
	}

	public int getRsm_id() {
		return rsm_id;
	}

	public void setRsm_id(int rsm_id) {
		this.rsm_id = rsm_id;
	}

	public int getAsm_id() {
		return asm_id;
	}

	public void setAsm_id(int asm_id) {
		this.asm_id = asm_id;
	}

	public int getTso_id() {
		return tso_id;
	}

	public void setTso_id(int tso_id) {
		this.tso_id = tso_id;
	}

	public int getReason_id() {
		return reason_id;
	}

	public void setReason_id(int reason_id) {
		this.reason_id = reason_id;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "MVOutletVisitRequest [userId=" + userId + ", outletId=" + outletId + ", mobileRequestId="
				+ mobileRequestId + ", startMobileTimestamp=" + startMobileTimestamp + ", endMobileTimestamp="
				+ endMobileTimestamp + ", deviceId=" + deviceId + ", version=" + version + ", platform=" + platform
				+ ", lat=" + lat + ", lng=" + lng + ", accuracy=" + accuracy + ", beatPlanId=" + beatPlanId
				+ ", distributorId=" + distributorId + ", city_id=" + city_id + ", region_id=" + region_id + ", rsm_id="
				+ rsm_id + ", asm_id=" + asm_id + ", tso_id=" + tso_id + ", reason_id=" + reason_id + ", comments="
				+ comments + ", products=" + products + "]";
	}

}
