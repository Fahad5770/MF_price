package com.mf.interfaces;

import java.util.HashMap;

import org.json.simple.JSONArray;

import com.pbc.util.Datasource;

public interface IAuthenticationDetails {
	
	public HashMap<String, Integer> order_lock(Datasource ds, int city_id);

//	public JSONArray UserDetails(Datasource ds, int userId);
	
	public JSONArray StockPosition(Datasource ds, int userId);

	public JSONArray Products(Datasource ds, int productGroupId);

	public JSONArray BeatPlanRows(Datasource ds, int userId);

	public JSONArray BeatPlanRows(Datasource ds, int userId, double lat, double lng);

	public JSONArray Distributions(Datasource ds, int userId, double lat, double lng);

	public JSONArray Distributions(Datasource ds, int userId);

	public int ProductGroup(Datasource ds, int userId);

	public JSONArray OrderBookerBeatPlanRows(Datasource ds, int userId);
	
	public JSONArray OrderBookerBeatPlanRowsByLocation(Datasource ds, int user_id, double lat, double lng);

	public JSONArray get_pjp_list(Datasource ds, int userId);

	public JSONArray get_pci_sub_channels(Datasource ds);

	public String all_outlet_ids(Datasource ds, int userId);

	public JSONArray get_active_promotions(Datasource ds, String AllOutlets);

	public JSONArray get_promotion_products(Datasource ds, String AllOutlets);

	public JSONArray get_free_promotion_products(Datasource ds, String AllOutlets);

	public JSONArray get_price_list(Datasource ds);

	public JSONArray get_active_price_list(Datasource ds, String AllOutlets);

	public JSONArray get_price_hand_discount(Datasource ds, String AllOutlets);

	public JSONArray AllMobileFeatures(Datasource ds);

	public JSONArray AllMobileAccessFeatures(Datasource ds, int userId);
	
	public JSONArray NoOrderReason(Datasource ds);

	public JSONArray SpotDiscount(Datasource ds);
	
	public JSONArray Cities(Datasource ds, int userId);

	public JSONArray UserAreas(Datasource ds, int userId);

	String all_outlet_ids_ob(Datasource ds, int user_id);

	JSONArray get_ob_price_list(Datasource ds);



}
