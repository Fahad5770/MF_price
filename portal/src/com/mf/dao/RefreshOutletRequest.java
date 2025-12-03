package com.mf.dao;

import org.json.simple.JSONObject;

import com.mf.utils.MFParseUtils;


public class RefreshOutletRequest {

	private int user_id;
	private double lat;
	private double lng;

	public RefreshOutletRequest(JSONObject payload) {
		this.user_id = MFParseUtils.parseInt((String) payload.get("user_id"));
		this.lat = (Double) payload.get("lat");
		this.lng = (Double) payload.get("lng");
		
        
		System.out.println( "LoginRequest [user_id=" + user_id + ", lat=" + lat + ", lng=" + lng + " ");
	}
	
	public RefreshOutletRequest() {
		// TODO Auto-generated constructor stub
	}

	public void RefreshOutletRequestForOB(JSONObject payload) {
		this.user_id = MFParseUtils.parseInt((String) payload.get("user_id"));
        
		//System.out.println( "LoginRequest [user_id=" + user_id + ", lat=" + lat + ", lng=" + lng + " ");
	}

	@Override
	public String toString() {
		return "LoginRequest [user_id=" + user_id + ", lat=" + lat + ", lng=" + lng + "]";
	}

	public int getUser_id() {
		return user_id;
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
}
