package com.mf.dao;

import java.util.LinkedHashMap;

import org.json.simple.JSONArray;

public class OBLoginResponse {

	private JSONArray UserData = new JSONArray();
	private JSONArray BeatPlanRows = new JSONArray();
	private JSONArray Products = new JSONArray();
	private JSONArray pjpList = new JSONArray();
	private JSONArray pcisubChannnel = new JSONArray();
	private JSONArray activePromotion = new JSONArray();
	private JSONArray promoProducts = new JSONArray();
	private JSONArray promotionsProductsFree = new JSONArray();
	private JSONArray ActivePriceList = new JSONArray();
	private JSONArray PriceList = new JSONArray();
	private JSONArray PriceHandDiscount = new JSONArray();
	private JSONArray AllFeatures = new JSONArray();
	private JSONArray AccessFeatures = new JSONArray();
	private JSONArray NoOrderReason = new JSONArray();
	private JSONArray SpotDiscount = new JSONArray();
	private JSONArray Cities = new JSONArray();
	private JSONArray UserAreas = new JSONArray();
	private JSONArray StockPosition = new JSONArray();


	private String token = "";
	private int is_order_lock;
	private int order_lock_time;
	private int productgroupId = 0;



	public JSONArray getStockPosition() {
		return StockPosition;
	}

	public void setStockPosition(JSONArray stockPosition) {
		StockPosition = stockPosition;
	}

	public JSONArray getUserAreas() {
		return UserAreas;
	}

	public void setUserAreas(JSONArray userAreas) {
		UserAreas = userAreas;
	}

	public JSONArray getUserData() {
		return UserData;
	}

	public void setUserData(JSONArray userData) {
		UserData = userData;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public JSONArray getBeatPlanRows() {
		return BeatPlanRows;
	}

	public void setBeatPlanRows(JSONArray beatPlanRows) {
		BeatPlanRows = beatPlanRows;
	}

	public int getProductgroupId() {
		return productgroupId;
	}

	public void setProductgroupId(int productgroupId) {
		this.productgroupId = productgroupId;
	}

	public JSONArray getProducts() {
		return Products;
	}

	public void setProducts(JSONArray products) {
		Products = products;
	}

	public JSONArray getPjpList() {
		return pjpList;
	}

	public void setPjpList(JSONArray pjpList) {
		this.pjpList = pjpList;
	}

	public JSONArray getPcisubChannnel() {
		return pcisubChannnel;
	}

	public void setPcisubChannnel(JSONArray pcisubChannnel) {
		this.pcisubChannnel = pcisubChannnel;
	}

	public JSONArray getActivePromotion() {
		return activePromotion;
	}

	public void setActivePromotion(JSONArray activePromotion) {
		this.activePromotion = activePromotion;
	}

	public JSONArray getPromoProducts() {
		return promoProducts;
	}

	public void setPromoProducts(JSONArray promoProducts) {
		this.promoProducts = promoProducts;
	}

	public JSONArray getPromotionsProductsFree() {
		return promotionsProductsFree;
	}

	public void setPromotionsProductsFree(JSONArray promotionsProductsFree) {
		this.promotionsProductsFree = promotionsProductsFree;
	}

	public JSONArray getActivePriceList() {
		return ActivePriceList;
	}

	public void setActivePriceList(JSONArray activePriceList) {
		ActivePriceList = activePriceList;
	}

	public JSONArray getPriceList() {
		return PriceList;
	}

	public void setPriceList(JSONArray priceList) {
		PriceList = priceList;
	}

	public JSONArray getPriceHandDiscount() {
		return PriceHandDiscount;
	}

	public void setPriceHandDiscount(JSONArray priceHandDiscount) {
		PriceHandDiscount = priceHandDiscount;
	}

	public JSONArray getAllFeatures() {
		return AllFeatures;
	}

	public void setAllFeatures(JSONArray allFeatures) {
		AllFeatures = allFeatures;
	}

	public JSONArray getAccessFeatures() {
		return AccessFeatures;
	}

	public void setAccessFeatures(JSONArray accessFeatures) {
		AccessFeatures = accessFeatures;
	}

	public JSONArray getNoOrderReason() {
		return NoOrderReason;
	}

	public void setNoOrderReason(JSONArray noOrderReason) {
		this.NoOrderReason = noOrderReason;
	}

	public JSONArray getSpotDiscount() {
		return this.SpotDiscount;
	}

	public void setSpotDiscount(JSONArray spotDiscount) {
		this.SpotDiscount = spotDiscount;
	}

	public int getIs_order_lock() {
		return this.is_order_lock;
	}

	public void setIs_order_lock(int is_order_lock) {
		this.is_order_lock = is_order_lock;
	}

	public int getOrder_lock_time() {
		return this.order_lock_time;
	}

	public void setOrder_lock_time(int order_lock_time) {
		this.order_lock_time = order_lock_time;
	}

	public JSONArray getCities() {
		return this.Cities;
	}

	public void setCities(JSONArray cities) {
		this.Cities = cities;
	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> dataArray = new LinkedHashMap<String, Object>();

		dataArray.put("is_order_lock", this.is_order_lock);
		dataArray.put("order_lock_time", this.order_lock_time);
		dataArray.put("jwt_token", this.token);
		dataArray.put("access_features", this.AccessFeatures);
		dataArray.put("all_features", this.AllFeatures);
		dataArray.put("user", this.UserData);
		dataArray.put("beat_plan_rows", this.BeatPlanRows);
		dataArray.put("product_group_id", this.productgroupId);
		dataArray.put("Products", this.Products); // old ProductGroupRows
		dataArray.put("pjp_list", pjpList);
		dataArray.put("pci_sub_channel", this.pcisubChannnel);
		dataArray.put("promotions_active", this.activePromotion);
		dataArray.put("promotion_free_products", this.promotionsProductsFree);
		dataArray.put("promotion_products", this.promoProducts);
		dataArray.put("active_price_list", this.ActivePriceList);
		dataArray.put("price_list", this.PriceList);
		dataArray.put("hand_discount", this.PriceHandDiscount);
		dataArray.put("spot_discount", this.SpotDiscount);
		dataArray.put("no_order_reasons", this.NoOrderReason);
		dataArray.put("cities", this.Cities);
		dataArray.put("user_areas", this.UserAreas);
		dataArray.put("stock_position", this.StockPosition);

		// / System.out.println(dataArray);
		return dataArray;
	}

}
