package com.mf.modals;

import java.util.LinkedHashMap;

public class OutletOB extends BeatPlans {

	private long outletID;
	private String outletName;
	private int dayNumber;
	private String owner;
	private String address;
	private String telephone;
	private int nfcTagID;
	private String orderCreatedOnDate;
	private String subChannelLabel;
	private double lat;
	private double lng;
	private int isFiler;
	private int isRegister;
	private int isGeoFence;
	private int Geo_Radius;
	private String areaLabel;
	private String subAreaLabel;
	private int isAlternative;
	private int outletPciSubChannelID;

	public OutletOB() {
		super();
	}

	public OutletOB(long outletID, String outletName, int dayNumber, String owner, String address, String telephone,
			int nfcTagID, String orderCreatedOnDate, String subChannelLabel, double lat, double lng, int isFiler,
			int isRegister, int isGeoFence, int radius, String areaLabel, String subAreaLabel, int isAlternative,
			int outletPciSubChannelID, int beatPlan, long distributorID, String pjpLabel, int cityID) {
		super(beatPlan, pjpLabel, distributorID, cityID);
		this.outletID = outletID;
		this.outletName = outletName;
		this.dayNumber = dayNumber;
		this.owner = owner;
		this.address = address;
		this.telephone = telephone;
		this.nfcTagID = nfcTagID;
		this.orderCreatedOnDate = orderCreatedOnDate;
		this.subChannelLabel = subChannelLabel;
		this.lat = lat;
		this.lng = lng;
		this.isFiler = isFiler;
		this.isRegister = isRegister;
		this.isGeoFence = isGeoFence;
		this.Geo_Radius = radius;
		this.areaLabel = areaLabel;
		this.subAreaLabel = subAreaLabel;
		this.isAlternative = isAlternative;
		this.outletPciSubChannelID = outletPciSubChannelID;
	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> outlet = new LinkedHashMap<>();

		outlet.put("pjp_id", super.getPjp_id());

		outlet.put("pjp_label", super.getPjp_label());
		outlet.put("distributor_id", super.getDistributor_id());

		// Match DB mappings
		outlet.put("OutletID", this.outletID);
		outlet.put("OutletName", this.outletName);
		outlet.put("DayNumber", this.dayNumber);
		outlet.put("Owner", this.owner);
		outlet.put("Address", this.address);
		outlet.put("Telepohone", this.telephone); // kept same spelling as DB
		outlet.put("NFCTagID", this.nfcTagID);
		outlet.put("order_created_on_date", this.orderCreatedOnDate);
		outlet.put("SUBChannelLabel", this.subChannelLabel);
		outlet.put("lat", this.lat);
		outlet.put("lng", this.lng);
		outlet.put("is_filer", this.isFiler);
		outlet.put("is_Register", this.isRegister);
		outlet.put("IsGeoFence", this.isGeoFence);
		outlet.put("Geo_Radius", this.Geo_Radius);
		outlet.put("AreaLabel", this.areaLabel);
		outlet.put("SubAreaLabel", this.subAreaLabel);
		outlet.put("IsAlternative", this.isAlternative);
		outlet.put("OutletPciSubChannelID", this.outletPciSubChannelID);

		return outlet;
	}

	@Override
	public String toString() {
		return "Outlet{" + "outletID=" + outletID + ", outletName='" + outletName + '\'' + ", dayNumber=" + dayNumber
				+ ", owner='" + owner + '\'' + ", address='" + address + '\'' + ", telephone='" + telephone + '\''
				+ ", nfcTagID=" + nfcTagID + ", orderCreatedOnDate='" + orderCreatedOnDate + '\''
				+ ", subChannelLabel='" + subChannelLabel + '\'' + ", lat=" + lat + ", lng=" + lng + ", isFiler="
				+ isFiler + ", isRegister=" + isRegister + ", isGeoFence=" + isGeoFence + ", Geo_Radius=" + Geo_Radius
				+ ", areaLabel='" + areaLabel + '\'' + ", subAreaLabel='" + subAreaLabel + '\'' + ", isAlternative="
				+ isAlternative + ", outletPciSubChannelID=" + outletPciSubChannelID + '}';
	}

	// ===== Getters & Setters =====

	public long getOutletID() {
		return outletID;
	}

	public void setOutletID(long outletID) {
		this.outletID = outletID;
	}

	public String getOutletName() {
		return outletName;
	}

	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}

	public int getDayNumber() {
		return dayNumber;
	}

	public void setDayNumber(int dayNumber) {
		this.dayNumber = dayNumber;
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

	public int getNfcTagID() {
		return nfcTagID;
	}

	public void setNfcTagID(int nfcTagID) {
		this.nfcTagID = nfcTagID;
	}

	public String getOrderCreatedOnDate() {
		return orderCreatedOnDate;
	}

	public void setOrderCreatedOnDate(String orderCreatedOnDate) {
		this.orderCreatedOnDate = orderCreatedOnDate;
	}

	public String getSubChannelLabel() {
		return subChannelLabel;
	}

	public void setSubChannelLabel(String subChannelLabel) {
		this.subChannelLabel = subChannelLabel;
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

	public int getIsFiler() {
		return isFiler;
	}

	public void setIsFiler(int isFiler) {
		this.isFiler = isFiler;
	}

	public int getIsRegister() {
		return isRegister;
	}

	public void setIsRegister(int isRegister) {
		this.isRegister = isRegister;
	}

	public int getIsGeoFence() {
		return isGeoFence;
	}

	public void setIsGeoFence(int isGeoFence) {
		this.isGeoFence = isGeoFence;
	}

	public int getRadius() {
		return Geo_Radius;
	}

	public void setRadius(int radius) {
		this.Geo_Radius = radius;
	}

	public String getAreaLabel() {
		return areaLabel;
	}

	public void setAreaLabel(String areaLabel) {
		this.areaLabel = areaLabel;
	}

	public String getSubAreaLabel() {
		return subAreaLabel;
	}

	public void setSubAreaLabel(String subAreaLabel) {
		this.subAreaLabel = subAreaLabel;
	}

	public int isAlternative() {
		return isAlternative;
	}

	public void setAlternative(int alternative) {
		isAlternative = alternative;
	}

	public int getOutletPciSubChannelID() {
		return outletPciSubChannelID;
	}

	public void setOutletPciSubChannelID(int outletPciSubChannelID) {
		this.outletPciSubChannelID = outletPciSubChannelID;
	}
}
