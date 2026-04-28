package com.mf.modals;

import java.util.LinkedHashMap;

public class NoOrderReason {
	private int id;
	private String label;

	// Constructor
	public NoOrderReason(int id, String label) {
		this.id = id;
		this.label = label;
	}

	// Default Constructor
	public NoOrderReason() {
	}

	// Getters and Setters
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

	// Method to return JSON-like structure
	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> noOrderReason = new LinkedHashMap<>();
		noOrderReason.put("id", this.id);
		noOrderReason.put("label", this.label);
		return noOrderReason;
	}
}
