package com.mf.modals;

import java.util.LinkedHashMap;

public class PriceDiscount {
	private int id;
	private String label;
	private int product_id;
	private double discount;
	private int is_percentage;
	private int is_with_tax;

	// Default Constructor
	public PriceDiscount() {
	}

	// Constructor
	public PriceDiscount(int id, String label, int product_id, double discount, int is_percentage, int is_with_tax) {
		super();
		this.id = id;
		this.label = label;
		this.product_id = product_id;
		this.discount = discount;
		this.is_percentage = is_percentage;
		this.is_with_tax = is_with_tax;
	}

	// Getters and Setters
	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public int getIs_percentage() {
		return is_percentage;
	}

	public void setIs_percentage(int is_percentage) {
		this.is_percentage = is_percentage;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getIs_with_tax() {
		return is_with_tax;
	}

	public void setIs_with_tax(int is_with_tax) {
		this.is_with_tax = is_with_tax;
	}

	// Method to return JSON-like structure
	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> priceDetails = new LinkedHashMap<>();
		priceDetails.put("id", this.id);
		priceDetails.put("label", this.label);
		priceDetails.put("product_id", this.product_id);
		priceDetails.put("discount", this.discount);
		priceDetails.put("is_percentage", this.is_percentage);
		priceDetails.put("is_with_tax", this.is_with_tax);
		return priceDetails;
	}

}
