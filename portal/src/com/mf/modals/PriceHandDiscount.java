package com.mf.modals;

import java.util.LinkedHashMap;

public class PriceHandDiscount {
	private int sampling_id;
	private long outlet_id;
	private int product_id;
	private double discount;
	private String created_on;

	// Constructor
	public PriceHandDiscount(int sampling_id, long outlet_id, int product_id, double discount, String created_on) {

	}

	// Default Constructor
	public PriceHandDiscount() {
	}

	// Getters and Setters
	public int getSampling_id() {
		return sampling_id;
	}

	public void setSampling_id(int sampling_id) {
		this.sampling_id = sampling_id;
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

	public double getdiscount() {
		return discount;
	}

	public void setdiscount(double discount) {
		this.discount = discount;
	}

	public String getCreated_on() {
		return created_on;
	}

	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}

	// Method to return JSON-like structure
	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> discountDetails = new LinkedHashMap<>();
		discountDetails.put("sampling_id", this.sampling_id);
		discountDetails.put("outlet_id", this.outlet_id);
		discountDetails.put("product_id", this.product_id);
		discountDetails.put("discount", this.discount);
		discountDetails.put("created_on", this.created_on != null ? this.created_on.toString() : null);
		return discountDetails;
	}
}
