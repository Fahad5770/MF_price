package com.mf.dao;

import java.util.LinkedHashMap;

public class OutletDetailsOrdersReport {
	private String product;
	private int quantity;
	private String amount;

	public OutletDetailsOrdersReport() {

	}

	public OutletDetailsOrdersReport(String product, int quantity, String amount) {
		this.quantity = quantity;
		this.product = product;
		this.amount = amount;
	}

	// Getters and setters
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> dataArray = new LinkedHashMap<String, Object>();
		dataArray.put("product", this.product);
		dataArray.put("quantity", this.quantity);
		dataArray.put("amount", this.amount);
		return dataArray;
	}
}
