package com.mf.dao;

import org.json.simple.JSONObject;

import com.mf.utils.MFParseUtils;

public class SalesReportRequest {

	private int user_id;
	private String start_date = "";
	private String end_date = "";

	public SalesReportRequest() {

	}

	public SalesReportRequest(JSONObject jsonData) {
		this.user_id = MFParseUtils.parseInt((String) jsonData.get("user_id"));
		this.start_date = (String) jsonData.get("start_date");
		this.end_date = (String) jsonData.get("end_date");
		
		 System.out.println("SalesReportRequest Initialized:");
		    System.out.println("User ID    : " + this.user_id);
		    System.out.println("Start Date : " + this.start_date);
		    System.out.println("End Date   : " + this.end_date);

	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	@Override
	public String toString() {
		return "SalesReportRequest [user_id=" + user_id + ", start_date=" + start_date + ", end_date=" + end_date + "]";
	}

}
