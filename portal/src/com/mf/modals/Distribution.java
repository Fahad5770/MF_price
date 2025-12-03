package com.mf.modals;

import java.util.LinkedHashMap;
import java.util.List;

import org.json.simple.JSONArray;

public class Distribution {
	
	private long distributor_id;
	private String distributor_name;
	private List<DistributorLocation> distributorLocation;
		
	public Distribution() {
	}

	public Distribution(long outlet_id, String outlet_name) {
		this.distributor_id = outlet_id;
		this.distributor_name = outlet_name;
	}
	
	public long getOutlet_id() {
		return distributor_id;
	}
	
	public void setOutlet_id(long outlet_id) {
		this.distributor_id = outlet_id;
	}
	
	public String getOutlet_name() {
		return distributor_name;
	}
	
	public void setOutlet_name(String outlet_name) {
		this.distributor_name = outlet_name;
	}
	
	public List<DistributorLocation> getDistributorLocation() {
		return distributorLocation;
	}
	
	public void setDistributorLocation(List<DistributorLocation> distributorLocation) {
		this.distributorLocation = distributorLocation;
	}
	
	
	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> dataArray = new LinkedHashMap<String, Object>();
		dataArray.put("distributor_id", this.distributor_id);
		dataArray.put("distributor_name", this.distributor_name);
		
		JSONArray distribution_array = new JSONArray();
		for (DistributorLocation distributorLocation : this.distributorLocation) {
			distribution_array.add(distributorLocation.getIntoJson());
		}
		
		
		
		dataArray.put("distributor_location",distribution_array);

		return dataArray;
	}
	
}
