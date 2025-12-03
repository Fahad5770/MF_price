package com.mf.modals;

import java.util.LinkedHashMap;

public class Cities {

	private int id;
	private String label;

	public Cities() {

	}

	public Cities(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> cities = new LinkedHashMap<String, Object>();
		cities.put("id", this.id);
		cities.put("label", this.label);
		return cities;
	}

}
