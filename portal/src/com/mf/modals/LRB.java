package com.mf.modals;

import java.util.LinkedHashMap;

public class LRB {
	private int lrb_id;
	private String lrb_label;

	public LRB() {

	}

	public LRB(int lrb_id, String lrb_label) {

		this.lrb_id = lrb_id;
		this.lrb_label = lrb_label;

	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> lrb = new LinkedHashMap<String, Object>();
		lrb.put("lrb_id", this.lrb_id);
		lrb.put("lrb_label", this.lrb_label);
		return lrb;
	}

	public int getLrb_id() {
		return lrb_id;
	}

	public void setLrb_id(int lrb_id) {
		this.lrb_id = lrb_id;
	}

	public String getLrb_label() {
		return lrb_label;
	}

	public void setLrb_label(String lrb_label) {
		this.lrb_label = lrb_label;
	}

}
