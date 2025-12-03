package com.mf.modals;

import java.util.LinkedHashMap;

public class DistributorLocation {

	private String address;
	private String phone_no;
	private double lat;
	private double lng;
	
	public DistributorLocation() {
	}
	
	public DistributorLocation(String address, String phone_no, double lat, double lng) {
		this.address = address;
		this.phone_no = phone_no;
		this.lat = lat;
		this.lng = lng;
	}
	
	public LinkedHashMap<String, Object> getIntoJson() {
		
		LinkedHashMap<String, Object> dataArray = new LinkedHashMap<String, Object>();
		dataArray.put("lat", this.lat);
		dataArray.put("lng", this.lng);
		dataArray.put("address", this.address);
		dataArray.put("phone_no", this.phone_no);
		
		return dataArray;
		
	}
	

	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPhone_no() {
		return phone_no;
	}
	
	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}
	
	public double getLat() {
		return lat;
	}
	
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public double getLng() {
		return lng;
	}
	
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	
	
	
}
