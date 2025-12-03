package com.mf.modals;

import java.util.LinkedHashMap;

public class SpotDiscountProducts extends SpotDiscount {
	private int product_id;
	private double default_discount;
	private double maximum_discount;

	// Constructor
	public SpotDiscountProducts(int id, String label, int product_id, double default_discount,
			double maximum_discount) {
		super(id, label); // Initialize fields from the parent class
		this.product_id = product_id;
		this.default_discount = default_discount;
		this.maximum_discount = maximum_discount;
	}

	// Default Constructor
	public SpotDiscountProducts() {
		super(); // Call the parent class default constructor
	}

	// Getters and Setters
	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public double getDefault_discount() {
		return default_discount;
	}

	public void setDefault_discount(double default_discount) {
		this.default_discount = default_discount;
	}

	public double getMaximum_discount() {
		return maximum_discount;
	}

	public void setMaximum_discount(double maximum_discount) {
		this.maximum_discount = maximum_discount;
	}

	// Method to return JSON-like structure, including parent fields
	@Override
	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> discountProductDetails = super.getIntoJson(); // Include parent fields
		discountProductDetails.put("product_id", this.product_id);
		discountProductDetails.put("default_discount", this.default_discount);
		discountProductDetails.put("maximum_discount", this.maximum_discount);
		return discountProductDetails;
	}
}
