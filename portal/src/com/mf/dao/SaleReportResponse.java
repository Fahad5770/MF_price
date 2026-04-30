package com.mf.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.json.simple.JSONArray;

import com.pbc.util.Utilities;

public class SaleReportResponse {

	private int grandOrderQuantity;
	private double grandOrderAmount;
	private int grandSaleQuantity;
	private double grandSaleAmount;

	private List<SalesReportBrands> salesReportBrands;

	public SaleReportResponse() {

	}

	// Getters and setters

	public List<SalesReportBrands> getSale() {
		return this.salesReportBrands;
	}

	public int getGrandOrderQuantity() {
		return grandOrderQuantity;
	}

	public void setGrandOrderQuantity(int grandOrderQuantity) {
		this.grandOrderQuantity = grandOrderQuantity;
	}

	public double getGrandOrderAmount() {
		return grandOrderAmount;
	}

	public void setGrandOrderAmount(double grandOrderAmount) {
		this.grandOrderAmount = grandOrderAmount;
	}

	public int getGrandSaleQuantity() {
		return grandSaleQuantity;
	}

	public void setGrandSaleQuantity(int grandSaleQuantity) {
		this.grandSaleQuantity = grandSaleQuantity;
	}

	public double getGrandSaleAmount() {
		return grandSaleAmount;
	}

	public void setGrandSaleAmount(double grandSaleAmount) {
		this.grandSaleAmount = grandSaleAmount;
	}

	public List<SalesReportBrands> getSalesReportBrands() {
		return salesReportBrands;
	}

	public void setSalesReportBrands(List<SalesReportBrands> salesReportBrands) {
		this.salesReportBrands = salesReportBrands;
	}

	public void setSale(List<SalesReportBrands> salesReportBrands) {
		this.salesReportBrands = salesReportBrands;
	}

	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> dataArray = new LinkedHashMap<String, Object>();

		JSONArray order_array = new JSONArray();
		for (SalesReportBrands salesReportBrands : this.salesReportBrands) {

			order_array.add(salesReportBrands.getIntoJson());
		}

		dataArray.put("total_order_quantiry", this.grandOrderQuantity);
		dataArray.put("total_order_amount", Utilities.getDisplayCurrencyFormat(this.grandOrderAmount));
		dataArray.put("total_sale_quantiry", this.grandSaleQuantity);
		dataArray.put("total_sale_amount", Utilities.getDisplayCurrencyFormat(this.grandSaleAmount));
		dataArray.put("sale", order_array);

		return dataArray;
	}

}
