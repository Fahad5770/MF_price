package com.mf.modals;

import java.util.LinkedHashMap;

public class Outlet extends BeatPlans {

	private long outlet_id;
	private String outlet_name;
	private int day_number;
	private String owner;
	private String address;
	private String telephone;
	private int nfc_tag_id;
	private int accuracy;
	private int is_geo_fence;
	private int geo_radius;
	private int sub_channel_id;
	private String sub_channel_label;
	private String order_created_on_date;
	private String vpo_classifications;
	private int visit;
	private double lat;
	private double lng;
	private String area_label;
	private String sub_area_label;
	private boolean is_alternative;
	private String purchaser_name;
	private String purchaser_mobile_no;
	private String cache_contact_nic;

	public Outlet() {
		super();
	}

	public Outlet(int isGeoFence, int beatPlan, long distributor_id, String pjp_label, int geo_radius, long outlet_id,
			String outletName, int dayNumber, String owner, String address, String telephone, int nfcTagId,
			int accuracy, int subChannelId, String subChannelLabel, String orderCreatedOnDate,
			String vpo_classifications, int visit, double lat, double lng, String areaLabel, String subAreaLabel,
			boolean isAlternative, String purchaserName, String purchaserMobileNo, String cacheContactNic,
			int city_id) {
		super(beatPlan, pjp_label, distributor_id, city_id);
		this.is_geo_fence = isGeoFence;
		this.geo_radius = geo_radius;
		this.outlet_id = outlet_id;
		this.outlet_name = outletName;
		this.day_number = dayNumber;
		this.owner = owner;
		this.address = address;
		this.telephone = telephone;
		this.nfc_tag_id = nfcTagId;
		this.accuracy = accuracy;
		this.sub_channel_id = subChannelId;
		this.sub_channel_label = subChannelLabel;
		this.order_created_on_date = orderCreatedOnDate;
		this.vpo_classifications = vpo_classifications;
		this.visit = visit;
		this.lat = lat;
		this.lng = lng;
		this.area_label = areaLabel;
		this.sub_area_label = subAreaLabel;
		this.is_alternative = isAlternative;
		this.purchaser_name = purchaserName;
		this.purchaser_mobile_no = purchaserMobileNo;
		this.cache_contact_nic = cacheContactNic;
	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> outlet = new LinkedHashMap<String, Object>();
		outlet.put("pjp_id", super.getPjp_id());
		outlet.put("pjp_label", super.getPjp_label());
		outlet.put("distributor_id", super.getDistributor_id());
		// Current class properties
		outlet.put("is_geo_fence", this.is_geo_fence);
		outlet.put("geo_radius", this.geo_radius);
		outlet.put("outlet_id", this.outlet_id);
		outlet.put("outlet_name", this.outlet_name);
		outlet.put("day_number", this.day_number);
		outlet.put("owner", this.owner);
		outlet.put("address", this.address);
		outlet.put("telephone", this.telephone);
		outlet.put("nfc_tag_id", this.nfc_tag_id);
		outlet.put("accuracy", this.accuracy);
		outlet.put("sub_channel_id", this.sub_channel_id);
		outlet.put("sub_channel_label", this.sub_channel_label);
		outlet.put("order_created_on_date", this.order_created_on_date);
		outlet.put("vpo_classifications", this.vpo_classifications);
		outlet.put("visit", this.visit);
		outlet.put("lat", this.lat);
		outlet.put("lng", this.lng);
		outlet.put("area_label", this.area_label);
		outlet.put("sub_area_label", this.sub_area_label);
		outlet.put("is_alternative", this.is_alternative);
		outlet.put("purchaser_name", this.purchaser_name);
		outlet.put("purchaser_mobile_no", this.purchaser_mobile_no);
		outlet.put("cache_contact_nic", this.cache_contact_nic);
		return outlet;
	}

	@Override
	public String toString() {
		return "Outlet [outlet_id=" + outlet_id + ", outlet_name=" + outlet_name + ", day_number=" + day_number
				+ ", owner=" + owner + ", address=" + address + ", telephone=" + telephone + ", nfc_tag_id="
				+ nfc_tag_id + ", accuracy=" + accuracy + ", is_geo_fence=" + is_geo_fence + ", geo_radius="
				+ geo_radius + ", sub_channel_id=" + sub_channel_id + ", sub_channel_label=" + sub_channel_label
				+ ", order_created_on_date=" + order_created_on_date + ", vpo_classifications=" + vpo_classifications
				+ ", visit=" + visit + ", lat=" + lat + ", lng=" + lng + ", area_label=" + area_label
				+ ", sub_area_label=" + sub_area_label + ", is_alternative=" + is_alternative + ", purchaser_name="
				+ purchaser_name + ", purchaser_mobile_no=" + purchaser_mobile_no + ", cache_contact_nic="
				+ cache_contact_nic + "]";
	}

	public long getOutlet_id() {
		return outlet_id;
	}

	public void setOutlet_id(long outlet_id) {
		this.outlet_id = outlet_id;
	}

	public String getOutlet_name() {
		return outlet_name;
	}

	public void setOutlet_name(String outlet_name) {
		this.outlet_name = outlet_name;
	}

	public int getDay_number() {
		return day_number;
	}

	public void setDay_number(int day_number) {
		this.day_number = day_number;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public int getNfc_tag_id() {
		return nfc_tag_id;
	}

	public void setNfc_tag_id(int nfc_tag_id) {
		this.nfc_tag_id = nfc_tag_id;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	public int getIs_geo_fence() {
		return is_geo_fence;
	}

	public void setIs_geo_fence(int is_geo_fence) {
		this.is_geo_fence = is_geo_fence;
	}

	public int getGeo_radius() {
		return geo_radius;
	}

	public void setGeo_radius(int geo_radius) {
		this.geo_radius = geo_radius;
	}

	public int getSub_channel_id() {
		return sub_channel_id;
	}

	public void setSub_channel_id(int sub_channel_id) {
		this.sub_channel_id = sub_channel_id;
	}

	public String getSub_channel_label() {
		return sub_channel_label;
	}

	public void setSub_channel_label(String sub_channel_label) {
		this.sub_channel_label = sub_channel_label;
	}

	public String getOrder_created_on_date() {
		return order_created_on_date;
	}

	public void setOrder_created_on_date(String order_created_on_date) {
		this.order_created_on_date = order_created_on_date;
	}

	public String getCommon_outlets_vpo_classifications() {
		return vpo_classifications;
	}

	public void setCommon_outlets_vpo_classifications(String common_outlets_vpo_classifications) {
		this.vpo_classifications = common_outlets_vpo_classifications;
	}

	public int getVisit() {
		return visit;
	}

	public void setVisit(int visit) {
		this.visit = visit;
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

	public String getArea_label() {
		return area_label;
	}

	public void setArea_label(String area_label) {
		this.area_label = area_label;
	}

	public String getSub_area_label() {
		return sub_area_label;
	}

	public void setSub_area_label(String sub_area_label) {
		this.sub_area_label = sub_area_label;
	}

	public boolean isIs_alternative() {
		return is_alternative;
	}

	public void setIs_alternative(boolean is_alternative) {
		this.is_alternative = is_alternative;
	}

	public String getPurchaser_name() {
		return purchaser_name;
	}

	public void setPurchaser_name(String purchaser_name) {
		this.purchaser_name = purchaser_name;
	}

	public String getPurchaser_mobile_no() {
		return purchaser_mobile_no;
	}

	public void setPurchaser_mobile_no(String purchaser_mobile_no) {
		this.purchaser_mobile_no = purchaser_mobile_no;
	}

	public String getCache_contact_nic() {
		return cache_contact_nic;
	}

	public void setCache_contact_nic(String cache_contact_nic) {
		this.cache_contact_nic = cache_contact_nic;
	}

}
