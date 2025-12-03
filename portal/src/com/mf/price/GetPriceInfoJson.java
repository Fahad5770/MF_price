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
	            "SELECT ipl.id, iplp.product_id, raw_case, discount, unit, " +
	            "ipl.is_filer, ipl.is_register, " +
	            "(SELECT package_id FROM inventory_products WHERE id = product_id) package_id, " +
	            "(SELECT brand_id FROM inventory_products WHERE id = product_id) brand_id, " +
	            "(SELECT label FROM inventory_packages WHERE id = package_id) package_label, " +
	            "(SELECT label FROM inventory_brands WHERE id = brand_id) brand_label, " +
	            "(SELECT unit_per_case FROM inventory_packages WHERE id = package_id) unit_per_case, " +
	            "(SELECT liquid_in_ml FROM inventory_packages WHERE id = package_id) liquid_in_ml " +
	            "FROM inventory_price_list_products iplp " +
	            "JOIN inventory_price_list ipl ON iplp.id = ipl.id " +
	            "WHERE ipl.id IN (39,40,41,42)"
	        );

	        while (rsDefaultPrice.next()) {
	            int id = rsDefaultPrice.getInt("id");
	            int productId = rsDefaultPrice.getInt("product_id");
	            int packageId = rsDefaultPrice.getInt("package_id");
	            int brandId = rsDefaultPrice.getInt("brand_id");

	            String packageLabel = rsDefaultPrice.getString("package_label");
	            String brandLabel = rsDefaultPrice.getString("brand_label");
	            String unitPerCase = rsDefaultPrice.getString("unit_per_case");
	            String liquidInML = rsDefaultPrice.getString("liquid_in_ml");

	            int isFiler = rsDefaultPrice.getInt("is_filer");
	            int isRegister = rsDefaultPrice.getInt("is_register");

	            double rawCasePrice = rsDefaultPrice.getDouble("raw_case");
	            double unitPrice = rsDefaultPrice.getDouble("unit");
	            double discount = rsDefaultPrice.getDouble("discount");

	            // ✅ Calculate tax using your existing formula
            HashMap<String, Double> productsTax = AlmoizFormulas.ProductsTax(
	                    productId, (isRegister == 1), (isFiler == 1));
//          /  double tax = productsTax.values().iterator().next();
            double tax = productsTax.get("wh_tax") + productsTax.get("income_tax");
	            // ✅ Use new constructor
	            OBPriceList priceList = new OBPriceList(
	                    id, productId, packageId, brandId,
	                    packageLabel, brandLabel,
	                    unitPerCase, liquidInML,
	                    isFiler, isRegister,
	                    rawCasePrice, unitPrice,
	                    discount, tax
	            );

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
