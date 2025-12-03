package com.mf.dao;

import java.util.LinkedHashMap;

import org.json.simple.JSONArray;

public class LoginResponseMV {

	private JSONArray UserData = new JSONArray();
	private JSONArray BeatPlanRows = new JSONArray();
	private JSONArray Products = new JSONArray();
	private JSONArray Distributions = new JSONArray();
	private JSONArray Cities = new JSONArray();
	private JSONArray pjpList = new JSONArray();

	public JSONArray getDistributions() {
		return Distributions;
	}

	public void setDistributions(JSONArray distributions) {
		Distributions = distributions;
	}

	private String token = "";

	public JSONArray getUserData() {
		return UserData;
	}

	public void setUserData(JSONArray userData) {
		UserData = userData;
	}

	public JSONArray getBeatPlanRows() {
		return BeatPlanRows;
	}

	public void setBeatPlanRows(JSONArray beatPlanRows) {
		BeatPlanRows = beatPlanRows;
	}

	public JSONArray getProducts() {
		return Products;
	}

	public void setProducts(JSONArray products) {
		Products = products;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public JSONArray getCities() {
		return Cities;
	}

	public void setCities(JSONArray cities) {
		Cities = cities;
	}

	public JSONArray getBeatPlans() {
		return pjpList;
	}

	public void setBeatPlans(JSONArray beatPlans) {
		this.pjpList = beatPlans;
	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> dataArray = new LinkedHashMap<String, Object>();

		dataArray.put("jwt_token", this.token);
		dataArray.put("user", this.UserData);
		dataArray.put("beat_plan_rows", this.BeatPlanRows);
		dataArray.put("products", this.Products);
		dataArray.put("distributions", this.Distributions);
		dataArray.put("cities", this.Cities);
		dataArray.put("pjp_list", this.pjpList);

		return dataArray;
	}

}
