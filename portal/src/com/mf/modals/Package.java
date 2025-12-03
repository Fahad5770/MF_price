package com.mf.modals;

import java.util.LinkedHashMap;

public class Package {

	private int package_id;
	private String package_label;
	private String short_label;
	private double liquidInMl;
	private int sort_order;

	public Package() {

	}

	public Package(int package_id, String package_label, String short_label, double liquidInMl, int sort_order) {

		this.package_id = package_id;
		this.package_label = package_label;
		this.short_label = short_label;
		this.liquidInMl = liquidInMl;
		this.sort_order = sort_order;

	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> packages = new LinkedHashMap<String, Object>();
		packages.put("package_id", this.package_id);
		packages.put("package_label", this.package_label);
		packages.put("short_label", this.short_label);
		packages.put("liquid_in_ml", this.liquidInMl);
		packages.put("sort_order", this.sort_order);
		return packages;
	}

	public int getPackage_id() {
		return package_id;
	}

	public void setPackage_id(int package_id) {
		this.package_id = package_id;
	}

	public String getPackage_label() {
		return package_label;
	}

	public void setPackage_label(String package_label) {
		this.package_label = package_label;
	}

	public String getShort_label() {
		return short_label;
	}

	public void setShort_label(String short_label) {
		this.short_label = short_label;
	}

	public double getLiquidInMl() {
		return liquidInMl;
	}

	public void setLiquidInMl(double liquidInMl) {
		this.liquidInMl = liquidInMl;
	}

	public int getSort_order() {
		return sort_order;
	}

	public void setSort_order(int sort_order) {
		this.sort_order = sort_order;
	}

}
