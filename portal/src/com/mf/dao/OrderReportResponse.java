package com.mf.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.json.simple.JSONArray;

public class OrderReportResponse {

	private String userName;
	private String orderDate;
	private String zone;
	private String area;
	private String grantTotal;
	private List<OutletOrdersReport> outletOrdersReport;

	public OrderReportResponse() {

	}

	public OrderReportResponse(String userName, String orderDate, String zone, String area) {
		this.userName = userName;
		this.orderDate = orderDate;
		this.zone = zone;
		this.area = area;
	}

	// Getters and setters

	public String getGrandTotal() {
		return grantTotal;
	}

	public void setGrandTotal(String grantTotal) {
		this.grantTotal = grantTotal;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public List<OutletOrdersReport> getOrder() {
		return this.outletOrdersReport;
	}

	public void setOrder(List<OutletOrdersReport> outletOrdersReport) {
		this.outletOrdersReport = outletOrdersReport;
	}

	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> dataArray = new LinkedHashMap<String, Object>();
		dataArray.put("user_name", this.userName);
		dataArray.put("order_date", this.orderDate);
		dataArray.put("zone", this.zone);
		dataArray.put("area", this.area);
		dataArray.put("grant_total", this.grantTotal);
		JSONArray order_array = new JSONArray();
		for (OutletOrdersReport outletOrdersReport : this.outletOrdersReport) {
			dataArray.put("order", outletOrdersReport.getIntoJson());
			order_array.add(outletOrdersReport.getIntoJson());
		}

		dataArray.put("order", order_array);

		return dataArray;
	}

}
