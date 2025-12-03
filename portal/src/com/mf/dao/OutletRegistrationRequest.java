package com.mf.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.commons.codec.binary.Base64;

import com.mf.dao.OrderRequest;
import com.mf.dao.OrderRequest.OrderRequestProducts;

public class OutletRegistrationRequest {

	private int UserId;
	private long DistributorId;
	private String OutletName;
	private String Address;
	private String OwnerName;
	private String OwnerCNIC;
	private String OwnerMobileNo;
	private String PurchaserName;
	private String PurchaserMobileNo;
	private int IsOwnerPurchaser;
	private int BeatPlanId;
	private double Lat;
	private double Lng;
	private long Accuracy;
	private String StartMobileTimestamp;
	private String EndMobileTimestamp;
	private String AreaLabel;
	private String SubAreaLabel;
	private int SubChannelId;
	private String DeviceId;
	private String Platform;
	private String Version;
	private byte[] NewOutletImage1;
	private byte[] NewOutletImage2;
	private String MobileRequestId;
	private int IsOrder;
	private List<OrderRequestProducts> products = new ArrayList<OrderRequestProducts>();

	public OutletRegistrationRequest() {

	}

	public OutletRegistrationRequest(JSONObject payload) {

		UserId = ((Number) payload.get("user_id")).intValue();
		System.out.println(UserId);
		DistributorId = (Long) payload.get("distributor_id");
		System.out.println(DistributorId);
		OutletName = (String) payload.get("outlet_name");
		System.out.println(OutletName);
		Address = (String) payload.get("address");
		System.out.println(Address);
		OwnerName = (String) payload.get("owner_name");
		System.out.println(OwnerName);
		OwnerCNIC = (String) payload.get("owner_cnic");
		System.out.println(OwnerCNIC);
		OwnerMobileNo = (String) payload.get("owner_mobile_no");
		System.out.println(OwnerMobileNo);
		PurchaserName = (String) payload.get("purchaser_name");
		System.out.println(PurchaserName);
		PurchaserMobileNo = (String) payload.get("purchaser_mobile_no");
		System.out.println(PurchaserMobileNo);
		IsOwnerPurchaser = ((Number) payload.get("is_owner_purchaser")).intValue();
		System.out.println(IsOwnerPurchaser);
		Lat = (Double) payload.get("lat");
		System.out.println(Lat);
		Lng = (Double) payload.get("lng");
		System.out.println(Lng);
		Accuracy = Math.round((Double) payload.get("accuracy"));
		System.out.println(Accuracy);
		StartMobileTimestamp = (String) payload.get("mobile_timestamp");
		System.out.println(StartMobileTimestamp);
		EndMobileTimestamp = (String) payload.get("end_mobile_timestamp");
		System.out.println(EndMobileTimestamp);
		AreaLabel = (String) payload.get("area_label");
		System.out.println(AreaLabel);
		SubAreaLabel = (String) payload.get("sub_area_label");
		System.out.println(SubAreaLabel);
		SubChannelId = ((Number) payload.get("sub_channel_id")).intValue();
		System.out.println(SubChannelId);
		DeviceId = (String) payload.get("device_id");
		System.out.println(DeviceId);
		Platform = (String) payload.get("platform");
		System.out.println(Platform);
		Version = (String) payload.get("version");
		System.out.println(Version);
		BeatPlanId = ((Number) payload.get("beat_plan_id")).intValue();
		System.out.println(BeatPlanId);
		NewOutletImage1 = Base64.decodeBase64((String) payload.get("new_outlet_image1"));
		NewOutletImage2 = Base64.decodeBase64((String) payload.get("new_outlet_image2"));

		MobileRequestId = (String) payload.get("mobile_request_id");
		System.out.println(MobileRequestId);

		IsOrder = ((Number) payload.get("is_order")).intValue();
		System.out.println(IsOrder);

		if (((JSONArray) payload.get("products")) != null) {
			final JSONArray ProductsArray = (JSONArray) payload.get("products");

			for (int i = 0; i < ProductsArray.size(); i++) {
				OrderRequest OR = new OrderRequest();
				JSONObject productJson = (JSONObject) ProductsArray.get(i);
				OrderRequestProducts orderRequestProducts = OR.new OrderRequestProducts(
						((Number) productJson.get("product_id")).intValue(),
						((Number) productJson.get("quantity")).intValue(), ((double) productJson.get("discount")),
						(((Number) productJson.get("is_promotion")).intValue()),
						((Number) productJson.get("promotion_id")).intValue());
				this.products.add(orderRequestProducts);
			}
		}

	}

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public long getDistributorId() {
		return DistributorId;
	}

	public void setDistributorId(long distributorId) {
		DistributorId = distributorId;
	}

	public String getOutletName() {
		return OutletName;
	}

	public void setOutletName(String outletName) {
		OutletName = outletName;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getOwnerName() {
		return OwnerName;
	}

	public void setOwnerName(String ownerName) {
		OwnerName = ownerName;
	}

	public String getOwnerCNIC() {
		return OwnerCNIC;
	}

	public void setOwnerCNIC(String ownerCNIC) {
		OwnerCNIC = ownerCNIC;
	}

	public String getOwnerMobileNo() {
		return OwnerMobileNo;
	}

	public void setOwnerMobileNo(String ownerMobileNo) {
		OwnerMobileNo = ownerMobileNo;
	}

	public String getPurchaserName() {
		return PurchaserName;
	}

	public void setPurchaserName(String purchaserName) {
		PurchaserName = purchaserName;
	}

	public String getPurchaserMobileNo() {
		return PurchaserMobileNo;
	}

	public void setPurchaserMobileNo(String purchaserMobileNo) {
		PurchaserMobileNo = purchaserMobileNo;
	}

	public int getIsOwnerPurchaser() {
		return IsOwnerPurchaser;
	}

	public void setIsOwnerPurchaser(int isOwnerPurchaser) {
		IsOwnerPurchaser = isOwnerPurchaser;
	}

	public int getBeatPlanId() {
		return BeatPlanId;
	}

	public void setBeatPlanId(int beatPlanId) {
		BeatPlanId = beatPlanId;
	}

	public double getLat() {
		return Lat;
	}

	public void setLat(double lat) {
		Lat = lat;
	}

	public double getLng() {
		return Lng;
	}

	public void setLng(double lng) {
		Lng = lng;
	}

	public long getAccuracy() {
		return Accuracy;
	}

	public void setAccuracy(long accuracy) {
		Accuracy = accuracy;
	}

	public String getStartMobileTimestamp() {
		return StartMobileTimestamp;
	}

	public void setStartMobileTimestamp(String startMobileTimestamp) {
		StartMobileTimestamp = startMobileTimestamp;
	}

	public String getEndMobileTimestamp() {
		return EndMobileTimestamp;
	}

	public void setEndMobileTimestamp(String endMobileTimestamp) {
		EndMobileTimestamp = endMobileTimestamp;
	}

	public String getAreaLabel() {
		return AreaLabel;
	}

	public void setAreaLabel(String areaLabel) {
		AreaLabel = areaLabel;
	}

	public String getSubAreaLabel() {
		return SubAreaLabel;
	}

	public void setSubAreaLabel(String subAreaLabel) {
		SubAreaLabel = subAreaLabel;
	}

	public int getSubChannelId() {
		return SubChannelId;
	}

	public void setSubChannelId(int subChannelId) {
		SubChannelId = subChannelId;
	}

	public String getDeviceId() {
		return DeviceId;
	}

	public void setDeviceId(String deviceId) {
		DeviceId = deviceId;
	}

	public String getPlatform() {
		return Platform;
	}

	public void setPlatform(String platform) {
		Platform = platform;
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	public byte[] getNew_outlet_image1() {
		return NewOutletImage1;
	}

	public void setNew_outlet_image1(byte[] NewOutletImage1) {
		this.NewOutletImage1 = NewOutletImage1;
	}

	public byte[] getNew_outlet_image2() {
		return NewOutletImage2;
	}

	public void setNew_outlet_image2(byte[] NewOutletImage2) {
		this.NewOutletImage2 = NewOutletImage2;
	}

	public String getMobileRequestId() {
		return MobileRequestId;
	}

	public void setMobileRequestId(String mobileRequestId) {
		MobileRequestId = mobileRequestId;
	}

	public int getIsOrder() {
		return IsOrder;
	}

	public void setIsOrder(int isOrder) {
		IsOrder = isOrder;
	}

	public List<OrderRequestProducts> getProducts() {
		return products;
	}

	public void setProducts(List<OrderRequestProducts> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "OutletRegistrationRequest [UserId=" + UserId + ", DistributorId=" + DistributorId + ", OutletName="
				+ OutletName + ", Address=" + Address + ", OwnerName=" + OwnerName + ", OwnerCNIC=" + OwnerCNIC
				+ ", OwnerMobileNo=" + OwnerMobileNo + ", PurchaserName=" + PurchaserName + ", PurchaserMobileNo="
				+ PurchaserMobileNo + ", IsOwnerPurchaser=" + IsOwnerPurchaser + ", BeatPlanId=" + BeatPlanId + ", Lat="
				+ Lat + ", Lng=" + Lng + ", Accuracy=" + Accuracy + ", StartMobileTimestamp=" + StartMobileTimestamp
				+ ", EndMobileTimestamp=" + EndMobileTimestamp + ", AreaLabel=" + AreaLabel + ", SubAreaLabel="
				+ SubAreaLabel + ", SubChannelId=" + SubChannelId + ", DeviceId=" + DeviceId + ", Platform=" + Platform
				+ ", Version=" + Version + ", NewOutletImage1=" + NewOutletImage1 + ", NewOutletImage2="
				+ NewOutletImage2 + ", MobileRequestId=" + MobileRequestId + ", IsOrder=" + IsOrder + ", products="
				+ products + "]";
	}

}
