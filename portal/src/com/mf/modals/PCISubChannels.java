package com.mf.modals;

import java.util.LinkedHashMap;

public class PCISubChannels {
	private int id;
	private String label;

	public PCISubChannels() {

	}

	public PCISubChannels(int channel_id, String channel_label) {
		this.id = channel_id;
		this.label = channel_label;
	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> beatPlans = new LinkedHashMap<String, Object>();
		beatPlans.put("channel_id", this.id);
		beatPlans.put("channel_label", this.label);
		return beatPlans;
	}

	public int getFeature_id() {
		return id;
	}

	public void setFeature_id(int feature_id) {
		this.id = feature_id;
	}

	public String getFeature_label() {
		return label;
	}

	public void setFeature_label(String feature_label) {
		this.label = feature_label;
	}
}
