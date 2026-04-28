package com.mf.modals;

import java.util.LinkedHashMap;

public class BeatPlans {

	private int pjp_id;
	private String pjp_label;
	private long distributor_id;
	private int city_id;
	private int region_id;
	private int rsm_id;
	private int asm_id;
	private int tso_id;

	public BeatPlans() {

	}
	
	public BeatPlans(int pjp_id, String pjp_label, long distributor_id, int city_id) {
		super();
		System.out.println("pjp_id "+pjp_id);
		this.pjp_id = pjp_id;
		this.pjp_label = pjp_label;
		this.distributor_id = distributor_id;
		this.city_id = city_id;
	}


	public BeatPlans(int pjp_id, String pjp_label, long distributor_id, int city_id, int region_id, int rsm_id,
			int asm_id, int tso_id) {
		super();
		this.pjp_id = pjp_id;
		this.pjp_label = pjp_label;
		this.distributor_id = distributor_id;
		this.city_id = city_id;
		this.region_id = region_id;
		this.rsm_id = rsm_id;
		this.asm_id = asm_id;
		this.tso_id = tso_id;
	}

	public int getPjp_id() {
		return pjp_id;
	}

	public void setPjp_id(int pjp_id) {
		this.pjp_id = pjp_id;
	}

	public String getPjp_label() {
		return pjp_label;
	}

	public void setPjp_label(String pjp_label) {
		this.pjp_label = pjp_label;
	}

	public long getDistributor_id() {
		return distributor_id;
	}

	public void setDistributor_id(long distributor_id) {
		this.distributor_id = distributor_id;
	}

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	public int getRegion_id() {
		return region_id;
	}

	public void setRegion_id(int region_id) {
		this.region_id = region_id;
	}

	public int getRsm_id() {
		return rsm_id;
	}

	public void setRsm_id(int rsm_id) {
		this.rsm_id = rsm_id;
	}

	public int getAsm__id() {
		return asm_id;
	}

	public void setAsm__id(int asm__id) {
		this.asm_id = asm__id;
	}

	public int getTso_id() {
		return tso_id;
	}

	public void setTso_id(int tso_id) {
		this.tso_id = tso_id;
	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> beatPlans = new LinkedHashMap<String, Object>();
		// / System.out.println(this.pjp_label);city_id
		beatPlans.put("pjp_id", this.pjp_id);
		beatPlans.put("pjp_label", this.pjp_label);
		beatPlans.put("distributor_id", this.distributor_id);
		beatPlans.put("city_id", this.city_id);
		beatPlans.put("region_id", this.region_id);
		beatPlans.put("rsm_id", this.rsm_id);
		beatPlans.put("asm_id", this.asm_id);
		beatPlans.put("tso_id", this.tso_id);

		return beatPlans;
	}

}
