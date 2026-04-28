package com.mf.dao;

import java.util.LinkedHashMap;

public class noOrderResponseProducts {
	private String product;
	private int quantity;

	public noOrderResponseProducts() {
	}

	public noOrderResponseProducts(String product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}



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
	
	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> dataArray = new LinkedHashMap<String, Object>();
		dataArray.put("product", this.product);
		dataArray.put("quantity", this.quantity);
		return dataArray;
	}



}