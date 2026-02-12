package com.mf.dao;

import java.util.LinkedHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class OBLoginResponse {
	private JSONArray priceDiscount = new JSONArray();
	private JSONArray priceDiscountRegion = new JSONArray();
	private JSONArray priceDiscountDistributor = new JSONArray();
	private JSONArray priceDiscountChannel = new JSONArray();

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
	private JSONObject SalesTax = new JSONObject();
	private JSONObject IncomeTax = new JSONObject();

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

	public JSONObject getSalesTax() {
		return SalesTax;
	}

	public void setSalesTax(JSONObject salesTax) {
		SalesTax = salesTax;
	}

	public JSONObject getIncomeTax() {
		return IncomeTax;
	}

	public void setIncomeTax(JSONObject incomeTax) {
		IncomeTax = incomeTax;
	}

	public JSONArray getPriceDiscount() {
		return priceDiscount;
	}

	public void setPriceDiscount(JSONArray priceDiscount) {
		this.priceDiscount = priceDiscount;
	}

	public JSONArray getPriceDiscountRegion() {
		return priceDiscountRegion;
	}

	public void setPriceDiscountRegion(JSONArray priceDiscountRegion) {
		this.priceDiscountRegion = priceDiscountRegion;
	}

	public JSONArray getPriceDiscountDistributor() {
		return priceDiscountDistributor;
	}

	public void setPriceDiscountDistributor(JSONArray priceDiscountDistributor) {
		this.priceDiscountDistributor = priceDiscountDistributor;
	}

	public JSONArray getPriceDiscountChannel() {
		return priceDiscountChannel;
	}

	public void setPriceDiscountChannel(JSONArray priceDiscountChannel) {
		this.priceDiscountChannel = priceDiscountChannel;
	}

	public LinkedHashMap<String, Object> getIntoJson() {
		LinkedHashMap<String, Object> dataObject = new LinkedHashMap<String, Object>();

		dataObject.put("is_order_lock", this.is_order_lock);
		dataObject.put("order_lock_time", this.order_lock_time);
		dataObject.put("jwt_token", this.token);
		dataObject.put("sales_tax", this.SalesTax);
		dataObject.put("incomeTax", this.IncomeTax);
		dataObject.put("access_features", this.AccessFeatures);
		dataObject.put("all_features", this.AllFeatures);
		dataObject.put("user", this.UserData);
		dataObject.put("beat_plan_rows", this.BeatPlanRows);
		dataObject.put("product_group_id", this.productgroupId);
		dataObject.put("Products", this.Products); // old ProductGroupRows
		dataObject.put("pjp_list", pjpList);
		dataObject.put("pci_sub_channel", this.pcisubChannnel);
		dataObject.put("promotions_active", this.activePromotion);
		dataObject.put("promotion_free_products", this.promotionsProductsFree);
		dataObject.put("promotion_products", this.promoProducts);
		dataObject.put("active_price_list", this.ActivePriceList);
		dataObject.put("price_list", this.PriceList);
		dataObject.put("hand_discount", this.PriceHandDiscount);
		dataObject.put("spot_discount", this.SpotDiscount);
		dataObject.put("no_order_reasons", this.NoOrderReason);
		dataObject.put("cities", this.Cities);
		dataObject.put("user_areas", this.UserAreas);
		dataObject.put("stock_position", this.StockPosition);
		
		dataObject.put("price_discount", this.priceDiscount);
		dataObject.put("price_discount_region", this.priceDiscountRegion);
		dataObject.put("price_discount_distributor", this.priceDiscountDistributor);
		dataObject.put("price_discount_channel", this.priceDiscountChannel);


		// / System.out.println(dataArray);
		return dataObject;
	}

}
