package com.mf.price;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mf.modals.ActivePriceList;
import com.mf.modals.PriceList;
import com.mf.modals.OBPriceList;
import com.mf.modals.PriceDiscount;
import com.mf.modals.PriceDiscountChannel;
import com.mf.modals.PriceDiscountDistributor;
import com.mf.modals.PriceDiscountRegion;
import com.pbc.util.AlmoizFormulas;
import com.pbc.util.Datasource;

public class GetPriceInfoJson {

	public static List<PriceList> get_price_list(Datasource ds) {
		List<PriceList> PriceLists = new ArrayList<PriceList>();

		try {

			Statement s = ds.createStatement();

			ResultSet rsDefaultPrice = s.executeQuery("SELECT * FROM inventory_price_list_products where id=1");
			while (rsDefaultPrice.next()) {

				PriceList priceList = new PriceList(rsDefaultPrice.getInt("id"), rsDefaultPrice.getInt("product_id"),
						rsDefaultPrice.getDouble("raw_case"), rsDefaultPrice.getDouble("unit"));
				PriceLists.add(priceList);
			}

			s.close();

		} catch (SQLException e) {
			System.out.println("User Details Error :- " + e);

		}

		return PriceLists;
	}

	public static List<PriceDiscount> get_price_disc(Datasource ds) {
		List<PriceDiscount> priceDiscountArray = new ArrayList<PriceDiscount>();

		try {

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();

			ResultSet rstPriceDisc = s.executeQuery("SELECT * FROM inventory_price_discount where id=1");
			while (rstPriceDisc.next()) {

				int price_discount_id = rstPriceDisc.getInt("id");

				ResultSet rstPriceDiscProducts = s1
						.executeQuery("SELECT * FROM inventory_price_discount_products where price_discount_id= "
								+ price_discount_id + " ");
				while (rstPriceDiscProducts.next()) {

					PriceDiscount priceDiscount = new PriceDiscount(rstPriceDiscProducts.getInt("product_id"),
							rstPriceDiscProducts.getDouble("discount_value"),
							rstPriceDiscProducts.getInt("is_percentage"));
					priceDiscountArray.add(priceDiscount);

				}

			}

			s.close();

		} catch (SQLException e) {
			System.out.println("Price Disc Details Error :- " + e);

		}

		return priceDiscountArray;
	}

	public static List<PriceDiscountRegion> get_price_disc_region(Datasource ds, int userId) {

		List<PriceDiscountRegion> priceDiscountRegionArray = new ArrayList<PriceDiscountRegion>();

		try {

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();

			System.out.println(
					"SELECT id FROM inventory_price_discount ipd join inventory_price_discount_region ipdc on ipd.id=ipdc.price_discount_id where curdate() between valid_from and valid_to "
							+ "and is_active=1 and region_id in (select region_id from distributor_beat_plan_view where assigned_to = "
							+ userId + " ) ");
			ResultSet rstPriceDisc = s.executeQuery(
					"SELECT id FROM inventory_price_discount ipd join inventory_price_discount_region ipdc on ipd.id=ipdc.price_discount_id where curdate() between valid_from and valid_to "
							+ "and is_active=1 and region_id in (select region_id from distributor_beat_plan_view where assigned_to = "
							+ userId + " ) ");

			while (rstPriceDisc.next()) {

				int price_discount_id = rstPriceDisc.getInt("id");

				System.out.println("SELECT * FROM inventory_price_discount_products WHERE price_discount_id = "
						+ price_discount_id);

				ResultSet rstPriceDiscProducts = s1
						.executeQuery("SELECT * FROM inventory_price_discount_products WHERE price_discount_id = "
								+ price_discount_id);

				while (rstPriceDiscProducts.next()) {

					PriceDiscountRegion priceDiscountRegion = new PriceDiscountRegion(
							rstPriceDiscProducts.getInt("product_id"), rstPriceDiscProducts.getDouble("discount_value"),
							rstPriceDiscProducts.getInt("is_percentage"));

					priceDiscountRegionArray.add(priceDiscountRegion);
				}
			}

			s1.close();
			s.close();

		} catch (SQLException e) {
			System.out.println("Price Discount Region Error :- " + e);
		}

		return priceDiscountRegionArray;
	}

	public static List<PriceDiscountChannel> get_price_disc_channel(Datasource ds) {

		List<PriceDiscountChannel> priceDiscountChannelArray = new ArrayList<PriceDiscountChannel>();

		try {

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();

			ResultSet rstPriceDisc = s.executeQuery(
					"SELECT id, pci_sub_channel_id FROM inventory_price_discount ipd join inventory_price_discount_channel ipdc on ipd.id=ipdc.price_discount_id where curdate() between valid_from and valid_to and is_active=1");

			while (rstPriceDisc.next()) {

				int price_discount_id = rstPriceDisc.getInt("id");

				ResultSet rstPriceDiscProducts = s1
						.executeQuery("SELECT * FROM inventory_price_discount_products WHERE price_discount_id = "
								+ price_discount_id);

				while (rstPriceDiscProducts.next()) {

					PriceDiscountChannel priceDiscountChannel = new PriceDiscountChannel(
							rstPriceDisc.getInt("pci_sub_channel_id"), rstPriceDiscProducts.getInt("product_id"),
							rstPriceDiscProducts.getDouble("discount_value"),
							rstPriceDiscProducts.getInt("is_percentage"));

					priceDiscountChannelArray.add(priceDiscountChannel);
				}
			}

			s1.close();
			s.close();

		} catch (SQLException e) {
			System.out.println("Price Discount Channel Error :- " + e);
		}

		return priceDiscountChannelArray;
	}

	public static List<PriceDiscountDistributor> get_price_disc_distributor(Datasource ds, int userId) {

		List<PriceDiscountDistributor> priceDiscountDistributorArray = new ArrayList<PriceDiscountDistributor>();

		try {

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();

			ResultSet rstPriceDisc = s.executeQuery(
					"SELECT id FROM inventory_price_discount ipd join inventory_price_discount_distributor ipdc on ipd.id=ipdc.price_discount_id where curdate() between valid_from and valid_to "
							+ "and is_active=1 and distributor_id in (select distributor_id from distributor_beat_plan_view where assigned_to = "
							+ userId + " ) ");

			while (rstPriceDisc.next()) {

				int price_discount_id = rstPriceDisc.getInt("id");

				ResultSet rstPriceDiscProducts = s1
						.executeQuery("SELECT * FROM inventory_price_discount_products WHERE price_discount_id = "
								+ price_discount_id);

				while (rstPriceDiscProducts.next()) {

					PriceDiscountDistributor priceDiscountDistributor = new PriceDiscountDistributor(
							rstPriceDiscProducts.getInt("product_id"), rstPriceDiscProducts.getDouble("discount_value"),
							rstPriceDiscProducts.getInt("is_percentage"));

					priceDiscountDistributorArray.add(priceDiscountDistributor);
				}
			}

			s1.close();
			s.close();

		} catch (SQLException e) {
			System.out.println("Price Discount Distributor Error :- " + e);
		}

		return priceDiscountDistributorArray;
	}

	public static List<OBPriceList> get_ob_price_list(Datasource ds) {
		List<OBPriceList> PriceLists = new ArrayList<>();

		try {
			Statement s = ds.createStatement();
//	        System.out.println(
//		            "SELECT ipl.id, iplp.product_id, raw_case, discount, unit, " +
//		    	            "ipl.is_filer, ipl.is_register, " +
//		    	            "(SELECT package_id FROM inventory_products WHERE id = product_id) package_id, " +
//		    	            "(SELECT brand_id FROM inventory_products WHERE id = product_id) brand_id, " +
//		    	            "(SELECT label FROM inventory_packages WHERE id = package_id) package_label, " +
//		    	            "(SELECT label FROM inventory_brands WHERE id = brand_id) brand_label, " +
//		    	            "(SELECT unit_per_case FROM inventory_packages WHERE id = package_id) unit_per_case, " +
//		    	            "(SELECT liquid_in_ml FROM inventory_packages WHERE id = package_id) liquid_in_ml " +
//		    	            "FROM inventory_price_list_products iplp " +
//		    	            "JOIN inventory_price_list ipl ON iplp.id = ipl.id " +
//		    	            "WHERE ipl.id IN (39,40,41,42)"
//		    	        );
			ResultSet rsDefaultPrice = s.executeQuery(
					"SELECT ipl.id, iplp.product_id, raw_case, discount, unit, " + "ipl.is_filer, ipl.is_register, "
							+ "(SELECT package_id FROM inventory_products WHERE id = product_id) package_id, "
							+ "(SELECT brand_id FROM inventory_products WHERE id = product_id) brand_id, "
							+ "(SELECT label FROM inventory_packages WHERE id = package_id) package_label, "
							+ "(SELECT label FROM inventory_brands WHERE id = brand_id) brand_label, "
							+ "(SELECT unit_per_case FROM inventory_packages WHERE id = package_id) unit_per_case, "
							+ "(SELECT liquid_in_ml FROM inventory_packages WHERE id = package_id) liquid_in_ml "
							+ "FROM inventory_price_list_products iplp "
							+ "JOIN inventory_price_list ipl ON iplp.id = ipl.id " + "WHERE ipl.id = 1 ");

			while (rsDefaultPrice.next()) {
				int id = rsDefaultPrice.getInt("id");
				int productId = rsDefaultPrice.getInt("product_id");
				int packageId = rsDefaultPrice.getInt("package_id");
				int brandId = rsDefaultPrice.getInt("brand_id");

				String packageLabel = rsDefaultPrice.getString("package_label");
				String brandLabel = rsDefaultPrice.getString("brand_label");
				String unitPerCase = rsDefaultPrice.getString("unit_per_case");
				String liquidInML = rsDefaultPrice.getString("liquid_in_ml");

				double rawCasePrice = rsDefaultPrice.getDouble("raw_case");
				double unitPrice = rsDefaultPrice.getDouble("unit");

				OBPriceList priceList = new OBPriceList(id, productId, packageId, brandId, packageLabel, brandLabel,
						unitPerCase, liquidInML, rawCasePrice, unitPrice);

				PriceLists.add(priceList);
			}

			s.close();

		} catch (SQLException e) {
			System.out.println("User Details Error :- " + e);
		}

		return PriceLists;
	}

	public static List<ActivePriceList> get_active_price_list(Datasource ds, String AllOutlets) {
		List<ActivePriceList> ActivePriceLists = new ArrayList<ActivePriceList>();

		try {

			Statement s = ds.createStatement();

			if (AllOutlets.length() > 0) {
				System.out.println(
						"SELECT * FROM inventory_price_list_active_mview where outlet_id in(" + AllOutlets + ") ");
				ResultSet rsPL = s.executeQuery(
						"SELECT * FROM inventory_price_list_active_mview where outlet_id in(" + AllOutlets + ") ");
				while (rsPL.next()) {
					ActivePriceList activePriceList = new ActivePriceList(rsPL.getInt("price_list_id"),
							rsPL.getLong("outlet_id"), rsPL.getInt("product_id"), rsPL.getDouble("raw_case"),
							rsPL.getDouble("unit"));

					ActivePriceLists.add(activePriceList);

				}
			}

			s.close();

		} catch (SQLException e) {
			System.out.println("User Details Error :- " + e);

		}

		return ActivePriceLists;
	}
}
