package com.mf.modals;

import java.util.LinkedHashMap;

public class ActivePriceList {
	private int price_id;
	private long outlet_id;
	private int product_id;
	private double raw_cases;
	private double unit;

	// Constructor
	public ActivePriceList(int price_id, long outlet_id, int product_id, double raw_cases, double unit) {
		this.price_id = price_id;
		this.outlet_id = outlet_id;
		this.product_id = product_id;
		this.raw_cases = raw_cases;
		this.unit = unit;
	}

	// Default Constructor
	public ActivePriceList() {
	}

	// Getters and Setters
	public int getPrice_id() {
		return price_id;
	}

	public void setPrice_id(int price_id) {
		this.price_id = price_id;
	}

	public long getOutlet_id() {
		return outlet_id;
	}

	public void setOutlet_id(long outlet_id) {
		this.outlet_id = outlet_id;
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
		priceDetails.put("outlet_id", this.outlet_id);
		priceDetails.put("product_id", this.product_id);
		priceDetails.put("raw_cases", this.raw_cases);
		priceDetails.put("unit", this.unit);
		return priceDetails;
	}
}
