package com.mf.modals;

import java.util.LinkedHashMap;

public class PriceDiscountChannel {
	private int sub_channel_id;
	private int product_id;
	private double discount;
	private int is_percentage;

	// Default Constructor
	public PriceDiscountChannel() {
	}

	// Constructor
	public PriceDiscountChannel(int sub_channel_id, int product_id, double discount, int is_percentage) {
		super();
		this.sub_channel_id = sub_channel_id;
		this.product_id = product_id;
		this.discount = discount;
		this.is_percentage = is_percentage;
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

	public int getSub_channel_id() {
		return sub_channel_id;
	}

	public void setSub_channel_id(int sub_channel_id) {
		this.sub_channel_id = sub_channel_id;
	}

	// Method to return JSON-like structure
	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> priceDetails = new LinkedHashMap<>();
		priceDetails.put("sub_channel_id", this.sub_channel_id);
		priceDetails.put("product_id", this.product_id);
		priceDetails.put("discount", this.discount);
		priceDetails.put("is_percentage", this.is_percentage);
		return priceDetails;
	}

}
