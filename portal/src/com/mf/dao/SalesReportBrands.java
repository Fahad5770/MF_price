package com.mf.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.json.simple.JSONArray;

public class SalesReportBrands {

	private String brand;
	private List<SaleDetailsReport> saleDetailsReport;

	// Getters and setters
	public String getBrand() {
		return brand;
	}

	public void setBrand(String outlet) {
		this.brand = outlet;
	}

	public List<SaleDetailsReport> getSaleDetails() {
		return saleDetailsReport;
	}

	public void setSaleDetails(List<SaleDetailsReport> saleDetailsReport) {
		this.saleDetailsReport = saleDetailsReport;
	}

	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> dataArray = new LinkedHashMap<String, Object>();
		dataArray.put("brand", this.brand);

		JSONArray sale_detail_array = new JSONArray();

		for (SaleDetailsReport saleDetailsReport : this.saleDetailsReport) {
			sale_detail_array.add(saleDetailsReport.getIntoJson());
		}

		dataArray.put("sale_details", sale_detail_array);

		return dataArray;
	}

}
