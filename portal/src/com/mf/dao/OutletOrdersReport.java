package com.mf.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.json.simple.JSONArray;

public class OutletOrdersReport {
	private String outlet;
	private String totalAmount;
	private String date = "";
	private List<OutletDetailsOrdersReport> outletDetailsOrdersReport;

	public OutletOrdersReport() {

	};

	public OutletOrdersReport(String outlet, String totalAmount, String date) {
		this.outlet = outlet;
		this.totalAmount = totalAmount;
		this.date = date;
	};

	public OutletOrdersReport(String outlet, String totalAmount, String date,
			List<OutletDetailsOrdersReport> outletDetailsOrdersReport) {
		this.outlet = outlet;
		this.totalAmount = totalAmount;
		this.date = date;
		this.outletDetailsOrdersReport = outletDetailsOrdersReport;
	};

	public OutletOrdersReport(String outlet, String totalAmount) {
		this.outlet = outlet;
		this.totalAmount = totalAmount;
	};

	// Getters and setters
	public String getOutlet() {
		return outlet;
	}

	public void setOutlet(String outlet) {
		this.outlet = outlet;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<OutletDetailsOrdersReport> getOrderDetails() {
		return outletDetailsOrdersReport;
	}

	public void setOrderDetails(List<OutletDetailsOrdersReport> outletDetailsOrdersReport) {
		this.outletDetailsOrdersReport = outletDetailsOrdersReport;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<OutletDetailsOrdersReport> getOutletDetailsOrdersReport() {
		return outletDetailsOrdersReport;
	}

	public void setOutletDetailsOrdersReport(List<OutletDetailsOrdersReport> outletDetailsOrdersReport) {
		this.outletDetailsOrdersReport = outletDetailsOrdersReport;
	}

	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> dataArray = new LinkedHashMap<String, Object>();
		dataArray.put("outlet", this.outlet);
		dataArray.put("totalAmount", this.totalAmount);
		dataArray.put("date", this.date);
		JSONArray order_deetail_array = new JSONArray();

		for (OutletDetailsOrdersReport outletDetailsOrdersReport : this.outletDetailsOrdersReport) {
			order_deetail_array.add(outletDetailsOrdersReport.getIntoJson());
		}

		dataArray.put("order_details", order_deetail_array);

		return dataArray;
	}
}
