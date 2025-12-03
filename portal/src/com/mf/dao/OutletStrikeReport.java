package com.mf.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.json.simple.JSONArray;

public class OutletStrikeReport {
	private String user;
	//private String totalAmount;
	private List<OutletDetailsStrikeReport> outletDetailsOrdersReport;

	public OutletStrikeReport() {

	};

	public OutletStrikeReport(String user) {
		this.user = user;
		//this.totalAmount = totalAmount;
	};

	// Getters and setters
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

//	public String getTotalAmount() {
//		return totalAmount;
//	}

//	public void setTotalAmount(String totalAmount) {
//		this.totalAmount = totalAmount;
//	}

	public List<OutletDetailsStrikeReport> getOrderDetails() {
		return outletDetailsOrdersReport;
	}

	public void setOrderDetails(List<OutletDetailsStrikeReport> outletDetailsstrikesReport) {
		this.outletDetailsOrdersReport = outletDetailsstrikesReport;
	}

	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> dataArray = new LinkedHashMap<String, Object>();
		dataArray.put("user", this.user);
		//dataArray.put("totalAmount", this.totalAmount);

		JSONArray user_detail_array = new JSONArray();

		for (OutletDetailsStrikeReport outletDetailsOrdersReport : this.outletDetailsOrdersReport) {
			user_detail_array.add(outletDetailsOrdersReport.getIntoJson());
		}

		dataArray.put("user_details", user_detail_array);

		return dataArray;
	}
}
