package com.pbc.util;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class AlmoizFormulas {
	public static double calculateHaversineDistance(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 6371; // Earth's radius in kilometers

		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = earthRadius * c;

		return distance;
	}

	public static double BeetaTaxValue() {
		return 3.6585;
	}
	
	public static HashMap<String, Double> ProductsTax(int productId, boolean isRegister, boolean isFiler) {
		HashMap<String, Double> ProductTax = new HashMap<String, Double>();
		Datasource ds = new Datasource();
		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();

			System.out.println(isFiler);
			System.out.println(isRegister);

			String SelectText = (isRegister && isFiler) ? "register_filer"
					: (isRegister && !isFiler) ? "register_non_filer"
							: (!isRegister && isFiler) ? "unregister_filer"
									: (!isRegister && !isFiler) ? "unregister_non_filer" : "";

			System.out.println(
					"SELECT " + SelectText + " FROM inventory_product_tax_rates where product_id=" + productId);
			ResultSet rsSaleTax = s2.executeQuery(
					"SELECT " + SelectText + " FROM inventory_product_tax_rates where product_id=" + productId);
			ProductTax.put("wh_tax", (rsSaleTax.first()) ? rsSaleTax.getDouble(1) : 0);
			System.out.println(
					"SELECT " + SelectText + " FROM inventory_product_income_tax where product_id=" + productId);
			ResultSet rsIncomeTax = s2.executeQuery(
					"SELECT " + SelectText + " FROM inventory_product_income_tax where product_id=" + productId);
			ProductTax.put("income_tax", (rsIncomeTax.first()) ? rsIncomeTax.getDouble(1) : 0);
			System.out.println("-----------------------------------------------------------------------------");
			s.close();
			s2.close();
			ds.dropConnection();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return ProductTax;
	}

	public static HashMap<String, Double> ProductsTax(int productId, long outlet_id) {
		HashMap<String, Double> ProductTax = new HashMap<String, Double>();
		Datasource ds = new Datasource();
		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			boolean isRegister = false, isFiler = false;
			//System.out.println("-----------------------------------------------------------------------------");
			//System.out.println("select is_filer, is_register from common_outlets where id=" + outlet_id);
			ResultSet rsStatus = s
					.executeQuery("select is_filer, is_register from common_outlets where id=" + outlet_id);
			if (rsStatus.first()) {
				isRegister = (rsStatus.getInt("is_register") == 1);
				isFiler = (rsStatus.getInt("is_filer") == 1);
			}

			// double incomeTax=0.0, salesTax=0.0;

		//	System.out.println(isFiler);
		//	System.out.println(isRegister);

			String SelectText = (isRegister && isFiler) ? "register_filer"
					: (isRegister && !isFiler) ? "register_non_filer"
							: (!isRegister && isFiler) ? "unregister_filer"
									: (!isRegister && !isFiler) ? "unregister_non_filer" : "";

//			System.out.println(
//					"SELECT " + SelectText + " FROM inventory_product_tax_rates where product_id=" + productId);
			ResultSet rsSaleTax = s2.executeQuery(
					"SELECT " + SelectText + " FROM inventory_product_tax_rates where product_id=" + productId);
			ProductTax.put("wh_tax", (rsSaleTax.first()) ? rsSaleTax.getDouble(1) : 0);
//			System.out.println(
//					"SELECT " + SelectText + " FROM inventory_product_income_tax where product_id=" + productId);
			ResultSet rsIncomeTax = s2.executeQuery(
					"SELECT " + SelectText + " FROM inventory_product_income_tax where product_id=" + productId);
			ProductTax.put("income_tax", (rsIncomeTax.first()) ? rsIncomeTax.getDouble(1) : 0);
			//System.out.println("-----------------------------------------------------------------------------");
			s.close();
			s2.close();
			ds.dropConnection();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return ProductTax;
	}
}
