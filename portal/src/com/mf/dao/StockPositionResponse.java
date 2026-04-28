package com.mf.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.json.simple.JSONArray;

public class StockPositionResponse {

	private List<StockPositionDetailResponse> stockPositionDetailResponse;

	public List<StockPositionDetailResponse> getStoscktails() {
		return stockPositionDetailResponse;
	}

	public void setStosckDetails(List<StockPositionDetailResponse> stockPositionDetailResponse) {
		this.stockPositionDetailResponse = stockPositionDetailResponse;
	}

	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> dataArray = new LinkedHashMap<String, Object>();
		JSONArray stoc_position_array = new JSONArray();

		for (StockPositionDetailResponse stockPositionDetailResponse : this.stockPositionDetailResponse) {
			stoc_position_array.add(stockPositionDetailResponse.getIntoJson());
		}

		dataArray.put("stock_position", stoc_position_array);

		return dataArray;
	}
}
