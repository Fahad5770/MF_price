package com.mf.modals;

import java.util.LinkedHashMap;

public class UserAreas {

	private int id;
	private String label;

	public UserAreas() {
		
	}

	public UserAreas(int id, String label) {
		super();
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> userAreas = new LinkedHashMap<String, Object>();
		userAreas.put("id", this.id);
		userAreas.put("label", this.label);
		return userAreas;
	}

}
