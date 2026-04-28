package com.mf.modals;

import java.util.LinkedHashMap;

public class ActivePromotion {
	private int product_id;
	private long outlet_id;

	public ActivePromotion() {

	}
	
	public ActivePromotion(int product_id, long outlet_id) {
		this.product_id = product_id;
		this.outlet_id = outlet_id;
	}

	public int getId() {
		return product_id;
	}

	public void setId(int product_id) {
		this.product_id = product_id;
	}

	public long getOutlet_id() {
		return outlet_id;
	}

	public void setOutlet_id(long outlet_id) {
		this.outlet_id = outlet_id;
	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> active_promotion = new LinkedHashMap<String, Object>();
		active_promotion.put("product_id", this.product_id);
		active_promotion.put("outlet_id", this.outlet_id);
		return active_promotion;
	}
}
