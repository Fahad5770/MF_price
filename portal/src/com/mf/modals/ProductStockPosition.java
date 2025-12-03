package com.mf.modals;

import java.util.LinkedHashMap;

public class ProductStockPosition {

	private long ClosingUnits = 0;
	private String ClosingRawCases = "";
	private int product_id = 0;

	public ProductStockPosition() {

	}

	public ProductStockPosition(int product_id, long closingUnits, String closingRawCases) {
		super();
		ClosingUnits = closingUnits;
		ClosingRawCases = closingRawCases;
		this.product_id = product_id;
	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> stock_position = new LinkedHashMap<String, Object>();
		stock_position.put("product_id", this.product_id);
		stock_position.put("closing_units", this.ClosingUnits);
		stock_position.put("closing_raw_cases", this.ClosingRawCases);
		return stock_position;
	}

	public long getClosingUnits() {
		return ClosingUnits;
	}

	public void setClosingUnits(long closingUnits) {
		ClosingUnits = closingUnits;
	}

	public String getClosingRawCases() {
		return ClosingRawCases;
	}

	public void setClosingRawCases(String closingRawCases) {
		ClosingRawCases = closingRawCases;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

}
