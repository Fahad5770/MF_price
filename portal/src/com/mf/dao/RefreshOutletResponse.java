package com.mf.dao;

import java.util.LinkedHashMap;

import org.json.simple.JSONArray;

public class RefreshOutletResponse {

	private JSONArray BeatPlanRows = new JSONArray();

	public JSONArray getBeatPlanRows() {
		return BeatPlanRows;
	}

	public void setBeatPlanRows(JSONArray beatPlanRows) {
		BeatPlanRows = beatPlanRows;
	}

	@Override
	public String toString() {
		return "RefreshOutletResponse [BeatPlanRows=" + BeatPlanRows + "]";
	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> dataArray = new LinkedHashMap<String, Object>();

		dataArray.put("beat_plan_rows", this.BeatPlanRows);

		return dataArray;
	}

}
