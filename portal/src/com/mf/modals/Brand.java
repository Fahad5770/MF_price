package com.mf.modals;

import java.util.LinkedHashMap;

public class Brand {

	private int brand_id;
	private String brand_label;
	private int sort_order;

	public Brand() {

	}

	public Brand(int brand_id, String brand_label, int sort_order) {

		this.brand_id = brand_id;
		this.brand_label = brand_label;
		this.sort_order = sort_order;

	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> brand = new LinkedHashMap<String, Object>();
		brand.put("brand_id", this.brand_id);
		brand.put("brand_label", this.brand_label);
		brand.put("sort_order", this.sort_order);
		return brand;
	}

	public int getBrand_id() {
		return brand_id;
	}

	public void setBrand_id(int brand_id) {
		this.brand_id = brand_id;
	}

	public String getBrand_label() {
		return brand_label;
	}

	public void setBrand_label(String brand_label) {
		this.brand_label = brand_label;
	}

	public int getSort_order() {
		return sort_order;
	}

	public void setSort_order(int sort_order) {
		this.sort_order = sort_order;
	}

}
