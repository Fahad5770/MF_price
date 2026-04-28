package com.mf.modals;

import java.util.LinkedHashMap;

public class Product {

	private int category_id;
	private int type_id;
	private Package packages;
	private Brand brand;
	private int sap_code;
	private int product_id;
	private int unit_per_sku;
	private int is_visible;
	private LRB lrb;
	private int is_other_brand;

	public Product() {

	}

	public Product(int product_id, Package packages, Brand brand, LRB lrb, int unit_per_sku, int is_visible,
			int is_other_brand, int category_id, int type_id, int sap_code) {
		this.product_id = product_id;
		this.packages = packages;
		this.brand = brand;
		this.lrb = lrb;
		this.unit_per_sku = unit_per_sku;
		this.is_visible = is_visible;
		this.is_other_brand = is_other_brand;
		this.category_id = category_id;
		this.type_id = type_id;
		this.sap_code = sap_code;
	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> productDetails = new LinkedHashMap<>();
		productDetails.put("product_id", this.product_id);
		productDetails.put("package", this.packages != null ? this.packages.getIntoJson() : null);
		productDetails.put("brand", this.brand != null ? this.brand.getIntoJson() : null);
		productDetails.put("lrb", this.lrb != null ? this.lrb.getIntoJson() : null);
		productDetails.put("unit_per_sku", this.unit_per_sku);
		productDetails.put("is_visible", this.is_visible);
		productDetails.put("is_other_brand", this.is_other_brand);
		productDetails.put("category_id", this.category_id);
		productDetails.put("type_id", this.type_id);
		productDetails.put("sap_code", this.sap_code);
		return productDetails;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

	public Package getPackages() {
		return packages;
	}

	public void setPackages(Package packages) {
		this.packages = packages;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public int getSap_code() {
		return sap_code;
	}

	public void setSap_code(int sap_code) {
		this.sap_code = sap_code;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getUnit_per_sku() {
		return unit_per_sku;
	}

	public void setUnit_per_sku(int unit_per_sku) {
		this.unit_per_sku = unit_per_sku;
	}

	public int getIs_visible() {
		return is_visible;
	}

	public void setIs_visible(int is_visible) {
		this.is_visible = is_visible;
	}

	public LRB getLrb() {
		return lrb;
	}

	public void setLrb(LRB lrb) {
		this.lrb = lrb;
	}

	public int getIs_other_brand() {
		return is_other_brand;
	}

	public void setIs_other_brand(int is_other_brand) {
		this.is_other_brand = is_other_brand;
	}

}
