package com.mf.modals;

import java.util.LinkedHashMap;

public class SpotDiscount {
	private int id;
	private String label;

	// Constructor
	public SpotDiscount(int id, String label) {
		this.id = id;
		this.label = label;
	}

	// Default Constructor
	public SpotDiscount() {
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
		LinkedHashMap<String, Object> discountDetails = new LinkedHashMap<>();
		discountDetails.put("id", this.id);
		discountDetails.put("label", this.label);
		return discountDetails;
	}
}
