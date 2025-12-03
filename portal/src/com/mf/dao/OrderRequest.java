package com.mf.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class OrderRequest {

	public class OrderRequestProducts {
		private int product_id;
		private int quantity;
		private double discount;
		private int is_promotion;
		private int promotion_id;

		public OrderRequestProducts() {
		}

		public OrderRequestProducts(int product_id, int quantity, double discount, int is_promotion, int promotion_id) {
			super();
			this.product_id = product_id;
			this.quantity = quantity;
			this.discount = discount;
			this.is_promotion = is_promotion;
			this.promotion_id = promotion_id;
		}

		public int getProduct_id() {
			return product_id;
		}

		public void setProduct_id(int product_id) {
			this.product_id = product_id;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		public double getDiscount() {
			return discount;
		}

		public void setDiscount(double discount) {
			this.discount = discount;
		}

		public int getIs_promotion() {
			return is_promotion;
		}

		public void setIs_promotion(int is_promotion) {
			this.is_promotion = is_promotion;
		}

		public int getPromotion_id() {
			return promotion_id;
		}

		public void setPromotion_id(int promotion_id) {
			this.promotion_id = promotion_id;
		}

		@Override
		public String toString() {
			return "OrderRequestProducts [product_id=" + product_id + ", quantity=" + quantity + ", discount="
					+ discount + ", is_promotion=" + is_promotion + ", promotion_id=" + promotion_id + "]";
		}

	}

	private int userId;
	private String mobileRequestId;
	private String deviceId;
	private long outletId;
	private double lat;
	private double lng;
	private long accuracy;
	private int beatPlanId;
	private String startMobileTimestamp;
	private String endMobileTimestamp;
	private String version;
	private long distributorId;
	private String platform;
	private int isTimeOut;
	private byte[] order_image;
	private List<OrderRequestProducts> products = new ArrayList<OrderRequestProducts>();

	public OrderRequest() {
		super();
	}

	public OrderRequest(JSONObject Payload) {
		super();
		this.userId = ((Number) Payload.get("user_id")).intValue();
		this.isTimeOut = ((Number) Payload.get("isTimeOut")).intValue();
		this.outletId = (Long) Payload.get("outlet_id");
		this.mobileRequestId = (String) Payload.get("mobile_request_id");
		this.startMobileTimestamp = (String) Payload.get("start_mobile_timestamp");
		this.endMobileTimestamp = (String) Payload.get("end_mobile_timestamp");
		this.deviceId = (String) Payload.get("device_id");
		this.version = (String) Payload.get("version");
		this.platform = (String) Payload.get("platform");
		this.lat = (Double) Payload.get("lat");
		this.lng = (Double) Payload.get("lng");
		this.accuracy = Math.round((Double) Payload.get("accuracy"));
		this.beatPlanId = ((Number) Payload.get("beat_plan_id")).intValue();
		this.distributorId = (Long) Payload.get("distributor_id");
		this.order_image = Base64.decodeBase64((String) Payload.get("order_image"));

		if (((JSONArray) Payload.get("products")) != null) {
			final JSONArray ProductsArray = (JSONArray) Payload.get("products");

			for (int i = 0; i < ProductsArray.size(); i++) {
				JSONObject productJson = (JSONObject) ProductsArray.get(i);
				OrderRequestProducts orderRequestProducts = new OrderRequestProducts(
						((Number) productJson.get("product_id")).intValue(),
						((Number) productJson.get("quantity")).intValue(), ((double) productJson.get("discount")),
						(((Number) productJson.get("is_promotion")).intValue()),
						((Number) productJson.get("promotion_id")).intValue());
				this.products.add(orderRequestProducts);
			}
		}
	}// end of constructor

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getMobileRequestId() {
		return mobileRequestId;
	}

	public void setMobileRequestId(String mobileRequestId) {
		this.mobileRequestId = mobileRequestId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public long getOutletId() {
		return outletId;
	}

	public void setOutletId(long outletId) {
		this.outletId = outletId;
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

	public long getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(long accuracy) {
		this.accuracy = accuracy;
	}

	public int getBeatPlanId() {
		return beatPlanId;
	}

	public void setBeatPlanId(int beatPlanId) {
		this.beatPlanId = beatPlanId;
	}

	public String getStartMobileTimestamp() {
		return startMobileTimestamp;
	}

	public void setStartMobileTimestamp(String startMobileTimestamp) {
		this.startMobileTimestamp = startMobileTimestamp;
	}

	public String getEndMobileTimestamp() {
		return endMobileTimestamp;
	}

	public void setEndMobileTimestamp(String endMobileTimestamp) {
		this.endMobileTimestamp = endMobileTimestamp;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(long distributorId) {
		this.distributorId = distributorId;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public int getIsTimeOut() {
		return isTimeOut;
	}

	public void setIsTimeOut(int isTimeOut) {
		this.isTimeOut = isTimeOut;
	}

	public byte[] getOrder_image() {
		return order_image;
	}

	public void setOrder_image(byte[] order_image) {
		this.order_image = order_image;
	}

	public List<OrderRequestProducts> getProducts() {
		return products;
	}

	public void setProducts(List<OrderRequestProducts> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "OrderRequest [userId=" + userId + ", mobileRequestId=" + mobileRequestId + ", deviceId=" + deviceId
				+ ", outletId=" + outletId + ", lat=" + lat + ", lng=" + lng + ", accuracy=" + accuracy
				+ ", beatPlanId=" + beatPlanId + ", startMobileTimestamp=" + startMobileTimestamp
				+ ", endMobileTimestamp=" + endMobileTimestamp + ", version=" + version + ", distributorId="
				+ distributorId + ", platform=" + platform + ", isTimeOut=" + isTimeOut + ", order_image="
				+ Arrays.toString(order_image) + ", products=" + products.toString() + "]";
	}

}
