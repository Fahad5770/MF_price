package com.mf.modals;

import java.util.LinkedHashMap;

public class PriceList {
	private int price_id;
	private int product_id;
	private double raw_cases;
	private double unit;

	// Constructor
	public PriceList(int price_id, int product_id, double raw_cases, double unit) {
		this.price_id = price_id;
		this.product_id = product_id;
		this.raw_cases = raw_cases;
		this.unit = unit;
	}

	// Default Constructor
	public PriceList() {
	}

	// Getters and Setters
	public int getPrice_id() {
		return price_id;
	}

	public void setPrice_id(int price_id) {
		this.price_id = price_id;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public double getRaw_cases() {
		return raw_cases;
	}

	public void setRaw_cases(double raw_cases) {
		this.raw_cases = raw_cases;
	}

	public double getUnit() {
		return unit;
	}

	public void setUnit(double unit) {
		this.unit = unit;
	}

	// Method to return JSON-like structure
	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> priceDetails = new LinkedHashMap<>();
		priceDetails.put("price_id", this.price_id);
		priceDetails.put("product_id", this.product_id);
		priceDetails.put("raw_cases", this.raw_cases);
		priceDetails.put("unit", this.unit);
		return priceDetails;
	}
}
