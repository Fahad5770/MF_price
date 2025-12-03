package com.mf.modals;

import java.util.LinkedHashMap;

public class SalesPromotionsProducts {

	private int product_promotion_id;
	private int package_id;
	private String package_label;
	private int unit_per_case;
	private int total_units;
	private int brand_id;
	private String brand_label;

	public SalesPromotionsProducts() {

	}

	public SalesPromotionsProducts(int product_promotion_id, int package_id, String package_label, int unit_per_case,
			int total_units, int brand_id, String brand_label) {
		this.product_promotion_id = product_promotion_id;
		this.package_id = package_id;
		this.package_label = package_label;
		this.unit_per_case = unit_per_case;
		this.brand_label = brand_label;
		this.total_units = total_units;
		this.brand_id = brand_id;
	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> promotionDetails = new LinkedHashMap<>();
		promotionDetails.put("product_promotion_id", this.product_promotion_id);
		promotionDetails.put("package_id", this.package_id);
		promotionDetails.put("package_label", this.package_label);
		promotionDetails.put("unit_per_case", this.unit_per_case);
		promotionDetails.put("total_units", this.total_units);
		promotionDetails.put("brand_id", this.brand_id);
		promotionDetails.put("brand_label", this.brand_label);
		return promotionDetails;
	}

	public int getProduct_promotion_id() {
		return product_promotion_id;
	}

	public void setProduct_promotion_id(int product_promotion_id) {
		this.product_promotion_id = product_promotion_id;
	}

	public int getPackage_id() {
		return package_id;
	}

	public void setPackage_id(int package_id) {
		this.package_id = package_id;
	}

	public int getTotal_units() {
		return total_units;
	}

	public void setTotal_units(int total_units) {
		this.total_units = total_units;
	}

	public int getBrand_id() {
		return brand_id;
	}

	public void setBrand_id(int brand_id) {
		this.brand_id = brand_id;
	}

	public String getPackage_label() {
		return package_label;
	}

	public void setPackage_label(String package_label) {
		this.package_label = package_label;
	}

	public int getUnit_per_case() {
		return unit_per_case;
	}

	public void setUnit_per_case(int unit_per_case) {
		this.unit_per_case = unit_per_case;
	}

	public String getBrand_label() {
		return brand_label;
	}

	public void setBrand_label(String brand_label) {
		this.brand_label = brand_label;
	}

}
