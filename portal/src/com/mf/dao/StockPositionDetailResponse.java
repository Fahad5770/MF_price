package com.mf.dao;

import java.util.LinkedHashMap;

public class StockPositionDetailResponse {

	private int product_id;
	private String product_label;
	private long closing_units;
	private String closing_raw_cases;

	public StockPositionDetailResponse() {

	}

	public StockPositionDetailResponse(int product_id, long closing_units, String closing_raw_cases,
			String product_label) {
		this.product_id = product_id;
		this.closing_units = closing_units;
		this.closing_raw_cases = closing_raw_cases;
		this.product_label = product_label;
	}

	public String getProduct_label() {
		return product_label;
	}

	public void setProduct_label(String product_label) {
		this.product_label = product_label;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public long getClosing_units() {
		return closing_units;
	}

	public void setClosing_units(long closing_units) {
		this.closing_units = closing_units;
	}

	public String getClosing_raw_cases() {
		return closing_raw_cases;
	}

	public void setClosing_raw_cases(String closing_raw_cases) {
		this.closing_raw_cases = closing_raw_cases;
	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> dataArray = new LinkedHashMap<String, Object>();
		dataArray.put("product_id", this.product_id);
		dataArray.put("closing_units", this.closing_units);
		dataArray.put("closing_raw_cases", this.closing_raw_cases);
		dataArray.put("product_label", this.product_label);
		return dataArray;
	}

}
