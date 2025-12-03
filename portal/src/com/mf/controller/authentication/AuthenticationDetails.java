package com.mf.controller.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.simple.JSONArray;

import com.mf.discounts.getPriceDisacountInfoJson;
import com.mf.interfaces.IAuthenticationDetails;
import com.mf.modals.ActivePriceList;
import com.mf.modals.ActivePromotion;
import com.mf.modals.BeatPlans;
import com.mf.modals.Distribution;
import com.mf.modals.DistributorLocation;
import com.mf.modals.Outlet;
import com.mf.modals.OutletOB;
import com.mf.modals.PCISubChannels;
import com.mf.modals.PriceHandDiscount;
import com.mf.modals.PriceList;
import com.mf.modals.Cities;
import com.mf.outlet.GetOutletInfoJson;
import com.mf.outlet.GetOutletInfoJsonforOB;
import com.mf.outlet.GetPCISubChannelInfoJson;
import com.mf.price.GetPriceInfoJson;
import com.mf.products.GetProductsInfoJson;
import com.mf.promotions.GetPromoInfoJson;
import com.mf.users.GetUserInfoJson;
import com.mf.modals.Product;
import com.mf.modals.ProductStockPosition;
import com.mf.modals.NoOrderReason;
import com.mf.modals.OBPriceList;
import com.mf.modals.SalesPromotionsProducts;
import com.mf.modals.SpotDiscountProducts;
import com.pbc.util.Datasource;
import com.mf.modals.UserAreas;

@SuppressWarnings("unchecked")

public class AuthenticationDetails implements IAuthenticationDetails {

	public AuthenticationDetails() {

	}

	@Override
	public int ProductGroup(Datasource ds, int user_id) {

		return GetProductsInfoJson.get_product_group_id(ds, user_id);
	}

	@Override
	public String all_outlet_ids(Datasource ds, int user_id) {

		return GetOutletInfoJson.get_outletIds_by_userId(ds, user_id);
	}

	@Override
	public String all_outlet_ids_ob(Datasource ds, int user_id) {

		return GetOutletInfoJsonforOB.get_outletIds_by_userId(ds, user_id);
	}

	@Override
	public JSONArray BeatPlanRows(Datasource ds, int userId, double lat, double lng) {

		System.out.println("HELLO ; " + userId + lat + lng);

		JSONArray Data = new JSONArray();

		try {

			ds.createConnection();

			Statement s = ds.createStatement();

			System.out.println("select dbpv.city_id, (6371 * 1000 *ACOS( COS(RADIANS(" + lat
					+ ")) * COS(RADIANS(lat)) *   COS(RADIANS(lng) - RADIANS(" + lng + ")) + SIN(RADIANS(" + lat
					+ ")) * SIN(RADIANS(lat)))) AS distance_in_meters , dbpv.label pjp_label,dbpv.distributor_id,dbpv.id as beat_plan_id, dbpv.label as beat_plan_label, co.channel_id, dbpv.outlet_id,co.pic_channel_id,(Select label from common_outlets_channels where id=co.channel_id) as channel_name ,(SELECT COUNT(*) FROM distributor_beat_plan_view dbpv WHERE dbpv.assigned_to = "
					+ userId
					+ " and dbpv.outlet_id=co.id) as Visit,(select label from common_outlets_vpo_classifications covc where covc.id=co.vpo_classifications_id) as vpo_classifications,(select order_created_on_date from inventory_sales_adjusted isa where isa.outlet_id=  dbpv.outlet_id and  invoice_amount !=0 and isa.booked_by="
					+ userId
					+ " order by order_created_on_date desc limit 1) as order_created_on_date,(SELECT label from pci_sub_channel psc where psc.id=co.pic_channel_id) as channel_label, co.name outlet_name, dbpv.day_number, coc.contact_name owner, co.accuracy,IFNULL(co.address, '') AS address, coc.contact_number telepohone, co.nfc_tag_id, co.lat, co.lng, co.area_label, co.sub_area_label,co.purchaser_name,co.cache_contact_nic, dbpv.is_alternative from distributor_beat_plan_view dbpv join common_outlets co on dbpv.outlet_id = co.id join common_outlets_contacts coc on dbpv.outlet_id = coc.outlet_id and coc.is_primary = 1 "
					+ "where dbpv.distributor_id in (select distributor_id from common_distributors where rsm_id = "
					+ userId + ")  or dbpv.asm_id = " + userId
					+ " or dbpv.distributor_id in (select distributor_id from common_distributors where snd_id = "
					+ userId + ") ");

			ResultSet rsOutlets = s.executeQuery("select dbpv.city_id, (6371 * 1000 *ACOS( COS(RADIANS(" + lat
					+ ")) * COS(RADIANS(lat)) *   COS(RADIANS(lng) - RADIANS(" + lng + ")) + SIN(RADIANS(" + lat
					+ ")) * SIN(RADIANS(lat)))) AS distance_in_meters , dbpv.label pjp_label,dbpv.distributor_id,dbpv.id as beat_plan_id, dbpv.label as beat_plan_label, co.channel_id, dbpv.outlet_id,co.pic_channel_id,(Select label from common_outlets_channels where id=co.channel_id) as channel_name ,(SELECT COUNT(*) FROM distributor_beat_plan_view dbpv WHERE dbpv.assigned_to = "
					+ userId
					+ " and dbpv.outlet_id=co.id) as Visit,(select label from common_outlets_vpo_classifications covc where covc.id=co.vpo_classifications_id) as vpo_classifications,(select order_created_on_date from inventory_sales_adjusted isa where isa.outlet_id=  dbpv.outlet_id and  invoice_amount !=0 and isa.booked_by="
					+ userId
					+ " order by order_created_on_date desc limit 1) as order_created_on_date,(SELECT label from pci_sub_channel psc where psc.id=co.pic_channel_id) as channel_label, co.name outlet_name, dbpv.day_number, coc.contact_name owner, co.accuracy,IFNULL(co.address, '') AS address, coc.contact_number telepohone, co.nfc_tag_id, co.lat, co.lng, co.area_label, co.sub_area_label,co.purchaser_name,co.cache_contact_nic, dbpv.is_alternative from distributor_beat_plan_view dbpv join common_outlets co on dbpv.outlet_id = co.id join common_outlets_contacts coc on dbpv.outlet_id = coc.outlet_id and coc.is_primary = 1 "
					+ "where dbpv.distributor_id in (select distributor_id from common_distributors where rsm_id = "
					+ userId + ")  or dbpv.asm_id = " + userId
					+ " or dbpv.distributor_id in (select distributor_id from common_distributors where snd_id = "
					+ userId + ") ");

			while (rsOutlets.next()) {

				long distance_in_meters = rsOutlets.getLong("distance_in_meters");

				System.out.println("distance_in_meters : " + distance_in_meters);

				if (distance_in_meters < 501) {
					System.out.println("distance_in_meters : " + distance_in_meters);
					Outlet outlet = new Outlet(0, rsOutlets.getInt("beat_plan_id"), rsOutlets.getLong("distributor_id"),
							rsOutlets.getString("pjp_label"), 0, rsOutlets.getLong("outlet_id"),
							rsOutlets.getString("outlet_name"), rsOutlets.getInt("day_number"),
							rsOutlets.getString("owner"), rsOutlets.getString("address"),
							rsOutlets.getString("telepohone"), rsOutlets.getInt("nfc_tag_id"),
							rsOutlets.getInt("accuracy"), rsOutlets.getInt("pic_channel_id"),
							rsOutlets.getString("channel_label"), rsOutlets.getString("order_created_on_date"),
							rsOutlets.getString("vpo_classifications"), rsOutlets.getInt("Visit"),
							rsOutlets.getDouble("lat"), rsOutlets.getDouble("lng"), rsOutlets.getString("area_label"),
							rsOutlets.getString("sub_area_label"), (rsOutlets.getInt("is_alternative") == 1),
							rsOutlets.getString("purchaser_name"), "", rsOutlets.getString("cache_contact_nic"),
							rsOutlets.getInt("city_id"));

					Data.add(outlet.getIntoJson());

				}

			}
			s.close();
			return Data;

		} catch (SQLException e) {
			System.out.println("User Details Error :- " + e);
			Data = null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Data = null;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Data = null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Data = null;
		}

		return Data;
	}

	@Override
	public JSONArray BeatPlanRows(Datasource ds, int userId) {
		JSONArray Data = new JSONArray();
		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			System.out.println(
					"select  dbpv.label pjp_label,dbpv.distributor_id,dbpv.id as beat_plan_id, dbpv.label as beat_plan_label, co.channel_id, dbpv.outlet_id,co.pic_channel_id,(Select label from common_outlets_channels where id=co.channel_id) as channel_name ,(SELECT COUNT(*) FROM distributor_beat_plan_view dbpv WHERE dbpv.assigned_to = "
							+ userId
							+ " and dbpv.outlet_id=co.id) as Visit,(select label from common_outlets_vpo_classifications covc where covc.id=co.vpo_classifications_id) as vpo_classifications,(select order_created_on_date from inventory_sales_adjusted isa where isa.outlet_id=  dbpv.outlet_id and  invoice_amount !=0 and isa.booked_by="
							+ userId
							+ " order by order_created_on_date desc limit 1) as order_created_on_date,(SELECT label from pci_sub_channel psc where psc.id=co.pic_channel_id) as channel_label, co.name outlet_name, dbpv.day_number, coc.contact_name owner, co.accuracy,IFNULL(co.address, '') AS address, coc.contact_number telepohone, co.nfc_tag_id, co.lat, co.lng, co.area_label, co.sub_area_label,co.purchaser_name,co.cache_contact_nic, dbpv.is_alternative from distributor_beat_plan_view dbpv join common_outlets co on dbpv.outlet_id = co.id join common_outlets_contacts coc on dbpv.outlet_id = coc.outlet_id and coc.is_primary = 1 "
							+ "where dbpv.distributor_id in (select distributor_id from common_distributors where rsm_id = "
							+ userId + ")  or dbpv.asm_id = " + userId
							+ " or dbpv.distributor_id in (select distributor_id from common_distributors where snd_id = "
							+ userId + ") ");
			ResultSet rsOutlets = s.executeQuery(
					"select  dbpv.city_id, dbpv.label pjp_label,dbpv.distributor_id,dbpv.id as beat_plan_id, dbpv.label as beat_plan_label, co.channel_id,dbpv.outlet_id,co.pic_channel_id,(Select label from common_outlets_channels where id=co.channel_id) as channel_name ,(SELECT COUNT(*) FROM distributor_beat_plan_view dbpv WHERE dbpv.assigned_to = "
							+ userId
							+ " and dbpv.outlet_id=co.id) as Visit,(select label from common_outlets_vpo_classifications covc where covc.id=co.vpo_classifications_id) as vpo_classifications,(select order_created_on_date from inventory_sales_adjusted isa where isa.outlet_id=  dbpv.outlet_id and  invoice_amount !=0 and isa.booked_by="
							+ userId
							+ " order by order_created_on_date desc limit 1) as order_created_on_date,(SELECT label from pci_sub_channel psc where psc.id=co.pic_channel_id) as channel_label, co.name outlet_name, dbpv.day_number, coc.contact_name owner, co.accuracy,IFNULL(co.address, '') AS address, coc.contact_number telepohone, co.nfc_tag_id, co.lat, co.lng, co.area_label, co.sub_area_label,co.purchaser_name,co.cache_contact_nic, dbpv.is_alternative from distributor_beat_plan_view dbpv join common_outlets co on dbpv.outlet_id = co.id join common_outlets_contacts coc on dbpv.outlet_id = coc.outlet_id and coc.is_primary = 1 "
							+ "where dbpv.distributor_id in (select distributor_id from common_distributors where rsm_id = "
							+ userId + ")  or dbpv.asm_id = " + userId
							+ " or dbpv.distributor_id in (select distributor_id from common_distributors where snd_id = "
							+ userId + ") ");
			while (rsOutlets.next()) {
				Outlet outlet = new Outlet(0, rsOutlets.getInt("beat_plan_id"), rsOutlets.getLong("distributor_id"),
						rsOutlets.getString("pjp_label"), 0, rsOutlets.getLong("outlet_id"),
						rsOutlets.getString("outlet_name"), rsOutlets.getInt("day_number"),
						rsOutlets.getString("owner"), rsOutlets.getString("address"), rsOutlets.getString("telepohone"),
						rsOutlets.getInt("nfc_tag_id"), rsOutlets.getInt("accuracy"),
						rsOutlets.getInt("pic_channel_id"), rsOutlets.getString("channel_label"),
						rsOutlets.getString("order_created_on_date"), rsOutlets.getString("vpo_classifications"),
						rsOutlets.getInt("Visit"), rsOutlets.getDouble("lat"), rsOutlets.getDouble("lng"),
						rsOutlets.getString("area_label"), rsOutlets.getString("sub_area_label"),
						(rsOutlets.getInt("is_alternative") == 1), rsOutlets.getString("purchaser_name"), "",
						rsOutlets.getString("cache_contact_nic"), rsOutlets.getInt("city_id"));
				Data.add(outlet.getIntoJson());

			}
			s.close();

		} catch (SQLException e) {
			System.out.println("User Details Error :- " + e);
			// Data = null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Data;
	}

	@Override
	public JSONArray Products(Datasource ds, int productGroupId) {

		List<Product> products = GetProductsInfoJson.get_product_by_group_id(ds, productGroupId);

		JSONArray product_array = new JSONArray();

		for (Product product : products) {
			product_array.add(product.getIntoJson());
		}

		return product_array;
	}

	@Override
	public JSONArray Distributions(Datasource ds, int userId) {
		JSONArray Data = new JSONArray();

		try {

			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();

			System.out.println(
					"Distribution Rows ==>> select distributor_id , name from common_distributors where rsm_id="
							+ userId + " or snd_id=" + userId
							+ " or distributor_id in (select distributor_id from distributor_beat_plan where asm_id="
							+ userId + ")");

			ResultSet rsDistributors = s
					.executeQuery("select distributor_id , name from common_distributors where rsm_id=" + userId
							+ " or snd_id=" + userId
							+ " or distributor_id in (select distributor_id from distributor_beat_plan where asm_id="
							+ userId + ")");
			while (rsDistributors.next()) {
				Distribution distribution = new Distribution(rsDistributors.getLong("distributor_id"),
						rsDistributors.getString("name"));

				List<DistributorLocation> distributorLocationList = new ArrayList<DistributorLocation>();

				ResultSet rsDistributorsLocation = s2.executeQuery(
						"select lat , lng , address  , phone_no from common_distributors_locations where distributor_id="
								+ rsDistributors.getLong("distributor_id"));
				while (rsDistributorsLocation.next()) {
					DistributorLocation distributorLocation = new DistributorLocation(
							rsDistributorsLocation.getString("address"), rsDistributorsLocation.getString("phone_no"),
							rsDistributorsLocation.getDouble("lat"), rsDistributorsLocation.getDouble("lng"));
					distributorLocationList.add(distributorLocation);
				}
				distribution.setDistributorLocation(distributorLocationList);

				Data.add(distribution.getIntoJson());
			}

			s.close();
			s2.close();
		} catch (SQLException e) {
			System.out.println("User Details Error :- " + e);
			Data = null;
		}

		return Data;
	}

	@Override
	public JSONArray Distributions(Datasource ds, int userId, double lat, double lng) {
		JSONArray Data = new JSONArray();

		try {

			ds.createConnection();

			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();

			System.out.println(
					"Distribution Rows ==>> select distributor_id , name from common_distributors where rsm_id="
							+ userId + " or snd_id=" + userId
							+ " or distributor_id in (select distributor_id from distributor_beat_plan where asm_id="
							+ userId + ")");

			ResultSet rsDistributors = s
					.executeQuery("select distributor_id , name from common_distributors where rsm_id=" + userId
							+ " or snd_id=" + userId
							+ " or distributor_id in (select distributor_id from distributor_beat_plan where asm_id="
							+ userId + ")");
			while (rsDistributors.next()) {
				Distribution distribution = new Distribution(rsDistributors.getLong("distributor_id"),
						rsDistributors.getString("name"));

				List<DistributorLocation> distributorLocationList = new ArrayList<DistributorLocation>();
				System.out.println("select  lat , lng , address  , phone_no , (6371 * 1000 *ACOS( COS(RADIANS(" + lat
						+ ")) * COS(RADIANS(lat)) *   COS(RADIANS(lng) - RADIANS(" + lng + ")) + SIN(RADIANS(" + lat
						+ ")) * SIN(RADIANS(lat)))) AS distance_in_meters from common_distributors_locations where distributor_id="
						+ rsDistributors.getLong("distributor_id") + " limit 1");
				ResultSet rsDistributorsLocation = s2
						.executeQuery("select  lat , lng , address  , phone_no , (6371 * 1000 *ACOS( COS(RADIANS(" + lat
								+ ")) * COS(RADIANS(lat)) *   COS(RADIANS(lng) - RADIANS(" + lng + ")) + SIN(RADIANS("
								+ lat
								+ ")) * SIN(RADIANS(lat)))) AS distance_in_meters from common_distributors_locations where distributor_id="
								+ rsDistributors.getLong("distributor_id") + " limit 1");
				while (rsDistributorsLocation.next()) {

					long distance_in_meters = rsDistributorsLocation.getLong("distance_in_meters");

					System.out.println("distance_in_meters outside condition : " + distance_in_meters);

					if (distance_in_meters < 501) {

						System.out.println("distance_in_meters : " + distance_in_meters);

						DistributorLocation distributorLocation = new DistributorLocation(
								rsDistributorsLocation.getString("address"),
								rsDistributorsLocation.getString("phone_no"), rsDistributorsLocation.getDouble("lat"),
								rsDistributorsLocation.getDouble("lng"));
						distributorLocationList.add(distributorLocation);
						distribution.setDistributorLocation(distributorLocationList);
						Data.add(distribution.getIntoJson());
					}

				}
			}

			s.close();
			s2.close();
		} catch (SQLException e) {
			System.out.println("User Details Error :- " + e);
			Data = null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Data = null;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Data = null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Data = null;
		}

		return Data;
	}

	@Override
	public JSONArray OrderBookerBeatPlanRows(Datasource ds, int user_id) {

		List<OutletOB> outlets = GetOutletInfoJsonforOB.beat_plan_rows(ds, user_id);

		JSONArray beat_plan_array = new JSONArray();

		for (OutletOB outlet : outlets) {
			beat_plan_array.add(outlet.getIntoJson());
		}

		return beat_plan_array;
	}

	@Override
	public JSONArray get_pjp_list(Datasource ds, int user_id) {

		List<BeatPlans> beat_plans = GetUserInfoJson.get_user_beat_plans(ds, user_id);

		JSONArray beat_plan_array = new JSONArray();

		for (BeatPlans beat_plan : beat_plans) {
			beat_plan_array.add(beat_plan.getIntoJson());
		}

		return beat_plan_array;
	}

	@Override
	public JSONArray get_pci_sub_channels(Datasource ds) {

		List<PCISubChannels> pci_sub_channels = GetPCISubChannelInfoJson.get_pci_sub_channel(ds);

		JSONArray pci_sub_channels_array = new JSONArray();

		for (PCISubChannels pci_sub_channel : pci_sub_channels) {
			pci_sub_channels_array.add(pci_sub_channel.getIntoJson());
		}

		return pci_sub_channels_array;

	}

	@Override
	public JSONArray get_active_promotions(Datasource ds, String AllOutlets) {
		List<ActivePromotion> activePromotions = GetPromoInfoJson.get_active_promotions(ds, AllOutlets);

		JSONArray active_promo_array = new JSONArray();

		for (ActivePromotion activePromotion : activePromotions) {
			active_promo_array.add(activePromotion.getIntoJson());
		}

		return active_promo_array;
	}

	@Override
	public JSONArray get_promotion_products(Datasource ds, String AllOutlets) {
		List<SalesPromotionsProducts> salesPromotionsProducts = GetPromoInfoJson.get_promotion_products(ds, AllOutlets);

		JSONArray promo_product_array = new JSONArray();

		for (SalesPromotionsProducts salesPromotionsProduct : salesPromotionsProducts) {
			promo_product_array.add(salesPromotionsProduct.getIntoJson());
		}

		return promo_product_array;
	}

	@Override
	public JSONArray get_free_promotion_products(Datasource ds, String AllOutlets) {
		List<SalesPromotionsProducts> salesPromotionsProducts = GetPromoInfoJson.get_promotion_products(ds, AllOutlets);

		JSONArray promo_product_array = new JSONArray();

		for (SalesPromotionsProducts salesPromotionsProduct : salesPromotionsProducts) {
			promo_product_array.add(salesPromotionsProduct.getIntoJson());
		}

		return promo_product_array;
	}

	@Override
	public JSONArray get_price_list(Datasource ds) {
		List<PriceList> priceLists = GetPriceInfoJson.get_price_list(ds);

		JSONArray price_list_array = new JSONArray();

		for (PriceList priceList : priceLists) {
			price_list_array.add(priceList.getIntoJson());
		}

		return price_list_array;
	}

	@Override
	public JSONArray get_ob_price_list(Datasource ds) {
		List<OBPriceList> priceLists = GetPriceInfoJson.get_ob_price_list(ds);

		JSONArray price_list_array = new JSONArray();

		for (OBPriceList priceList : priceLists) {
			price_list_array.add(priceList.getIntoJson());
		}

		return price_list_array;
	}

	@Override
	public JSONArray get_active_price_list(Datasource ds, String AllOutlets) {
		List<ActivePriceList> ActivePriceLists = GetPriceInfoJson.get_active_price_list(ds, AllOutlets);

		JSONArray active_price_list_array = new JSONArray();

		for (ActivePriceList activePriceList : ActivePriceLists) {
			active_price_list_array.add(activePriceList.getIntoJson());
		}

		return active_price_list_array;
	}

	@Override
	public JSONArray get_price_hand_discount(Datasource ds, String AllOutlets) {
		List<PriceHandDiscount> PriceHandDiscounts = getPriceDisacountInfoJson.get_price_hand_discount(ds, AllOutlets);

		JSONArray hand_price_discount_array = new JSONArray();

		for (PriceHandDiscount priceHandDiscount : PriceHandDiscounts) {
			hand_price_discount_array.add(priceHandDiscount.getIntoJson());
		}

		return hand_price_discount_array;
	}

	@Override
	public JSONArray AllMobileFeatures(Datasource ds) {
		JSONArray Data = new JSONArray();

		try {

			Statement s = ds.createStatement();

			ResultSet rsfeatures = s.executeQuery("select f.feature_id from features f where f.group_id=7");
			while (rsfeatures.next()) {
				Data.add(rsfeatures.getInt("feature_id"));
			}

			s.close();

		} catch (SQLException e) {
			System.out.println("All Features Error :- " + e);

		}

		return Data;
	}

	@Override
	public JSONArray AllMobileAccessFeatures(Datasource ds, int user_id) {
		JSONArray Data = new JSONArray();

		try {

			Statement s = ds.createStatement();

			System.out.println("select f.feature_id from features f where f.group_id=7 and user_id=" + user_id);
			ResultSet rsfeatures = s
					.executeQuery("select f.feature_id from features f where f.group_id=7 and user_id=" + user_id);
			while (rsfeatures.next()) {
				Data.add(rsfeatures.getInt("feature_id"));
			}

			s.close();

		} catch (SQLException e) {
			System.out.println("All Features Error :- " + e);

		}

		return Data;
	}

	@Override
	public JSONArray NoOrderReason(Datasource ds) {
		JSONArray Data = new JSONArray();

		try {
			Statement s = ds.createStatement();
			ResultSet rsNOR = s.executeQuery("SELECT id, label FROM mobile_order_no_order_reason_type");
			while (rsNOR.next()) {

				NoOrderReason noOrderReason = new NoOrderReason(rsNOR.getInt("id"), rsNOR.getString("label"));

				Data.add(noOrderReason.getIntoJson());
			}

			s.close();

		} catch (SQLException e) {
			System.out.println("User Details Error :- " + e);

		}

		return Data;
	}

	@Override
	public JSONArray SpotDiscount(Datasource ds) {
		JSONArray Data = new JSONArray();

		try {

			Statement s = ds.createStatement();
			ResultSet rsSpotDiscount = s.executeQuery(
					"SELECT isd.id,label, product_id, default_discount, maximum_discount FROM inventory_spot_discount isd join inventory_spot_discount_products isdp on isd.id=isdp.id where isd.id in (1,20)");
			while (rsSpotDiscount.next()) {

				SpotDiscountProducts spotDiscountProducts = new SpotDiscountProducts(rsSpotDiscount.getInt("id"),
						rsSpotDiscount.getString("label"), rsSpotDiscount.getInt("product_id"),
						rsSpotDiscount.getDouble("default_discount"), rsSpotDiscount.getDouble("maximum_discount"));
				Data.add(spotDiscountProducts.getIntoJson());

			}
			s.close();

		} catch (SQLException e) {
			System.out.println("User Details Error :- " + e);

		}

		return Data;
	}

	@Override
	public HashMap<String, Integer> order_lock(Datasource ds, int city_id) {
		// TODO Auto-generated method stub

		HashMap<String, Integer> order_lock = new HashMap<>();

		try {

			Statement s = ds.createStatement();
			ResultSet rsOrderLock = s
					.executeQuery("select is_order_lock, order_lock_time from common_cities where id=" + city_id);
			if (rsOrderLock.first()) {
				order_lock.put("is_order_lock", rsOrderLock.getInt("is_order_lock"));
				order_lock.put("order_lock_time", rsOrderLock.getInt("order_lock_time"));
			} else {
				order_lock.put("is_order_lock", 0);
				order_lock.put("order_lock_time", 15);
			}
			s.close();

		} catch (SQLException e) {
			System.out.println("User Details Error :- " + e);

		}

		return order_lock;
	}

	@Override
	public JSONArray Cities(Datasource ds, int userId) {
		JSONArray Cities = new JSONArray();

		try {

			Statement s = ds.createStatement();
			System.out.println(
					"select * from common_cities where id in (select city_id from distributor_beat_plan_view where asm_id="
							+ userId + " or rsm_id=" + userId + " or snd_id=" + userId + " or assigned_to=" + userId
							+ ")");
			ResultSet rsSpotDiscount = s.executeQuery(
					"select * from common_cities where id in (select city_id from distributor_beat_plan_view where asm_id="
							+ userId + " or rsm_id=" + userId + " or snd_id=" + userId + " or assigned_to=" + userId
							+ ")");
			while (rsSpotDiscount.next()) {

				Cities cities = new Cities(rsSpotDiscount.getInt("id"), rsSpotDiscount.getString("label"));
				Cities.add(cities.getIntoJson());

			}
			s.close();

		} catch (SQLException e) {
			System.out.println("User Details Error :- " + e);

		}

		return Cities;
	}

	@Override
	public JSONArray UserAreas(Datasource ds, int userId) {
		JSONArray UserAreas = new JSONArray();

		try {

			Statement s = ds.createStatement();

			String query;
			if (userId == 1225) {
				query = "SELECT * FROM user_areas WHERE town_id = 1918";
				System.out.println("query : " + query);
			} else {
				query = "SELECT * FROM user_areas WHERE user_id = " + userId;
				System.out.println("query : " + query);

			}
			ResultSet rsUserAreas = s.executeQuery(query);

			while (rsUserAreas.next()) {

				UserAreas userAreas = new UserAreas(rsUserAreas.getInt("id"), rsUserAreas.getString("label"));
				UserAreas.add(userAreas.getIntoJson());

			}
			s.close();

		} catch (SQLException e) {
			System.out.println("User Details Error :- " + e);

		}

		return UserAreas;
	}

	@Override
	public JSONArray StockPosition(Datasource ds, int user_id) {
		// TODO Auto-generated method stub
		
		
		List<ProductStockPosition> get_products_stock = GetProductsInfoJson.get_products_stock(ds, user_id);
		JSONArray products_stock_array = new JSONArray();
		for(ProductStockPosition productStockPosition : get_products_stock) {
			products_stock_array.add(productStockPosition.getIntoJson());
		}
		return products_stock_array;
	}

	@Override
	public JSONArray OrderBookerBeatPlanRowsByLocation(Datasource ds, int user_id, double lat, double lng) {
		// TODO Auto-generated method stub
		return null;
	}

}
