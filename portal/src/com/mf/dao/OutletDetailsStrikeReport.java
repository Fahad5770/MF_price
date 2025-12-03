package com.mf.dao;

import java.util.LinkedHashMap;

public class OutletDetailsStrikeReport {
	private int visitCount;
	private int noVisitCount;
	private int totalVisits;

	public OutletDetailsStrikeReport() {

	}

	public OutletDetailsStrikeReport(int visitCount, int noVisitCount, int totalVisits) {
		this.visitCount = visitCount;
		this.noVisitCount = noVisitCount;
		this.totalVisits = totalVisits;
	}




	public int getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}

	public int getNoVisitCount() {
		return noVisitCount;
	}

	public void setNoVisitCount(int noVisitCount) {
		this.noVisitCount = noVisitCount;
	}

	public int getTotalVisits() {
		return totalVisits;
	}

	public void setTotalVisits(int totalVisits) {
		this.totalVisits = totalVisits;
	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> dataArray = new LinkedHashMap<String, Object>();
		dataArray.put("visitCount", this.visitCount);
		dataArray.put("noVisitCount", this.noVisitCount);
		dataArray.put("totalVisits", this.totalVisits);
		return dataArray;
	}
}
