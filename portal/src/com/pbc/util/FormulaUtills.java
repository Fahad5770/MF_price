package com.pbc.util;

import java.sql.ResultSet;
import java.sql.Statement;

public class FormulaUtills {

	public static long GetDistance(String lat1, String lng1, String lat2, String lng2) {
		// System.out.println("lat1"+lat1);
		// System.out.println("lng1"+lng1);
		// System.out.println("lat2"+lat2);
		// System.out.println("lng2"+lng2);

		Datasource ds = new Datasource();

		long Distance = 0;

		try {

			ds.createConnection();

			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();

			ResultSet rs = s.executeQuery("SELECT (( 3959 * acos( cos( radians('" + lat1 + "') ) * cos( radians( '"
					+ lat2 + "' ) ) * cos( radians( '" + lng2 + "' ) - radians('" + lng1 + "') ) + sin ( radians('"
					+ lat1 + "') )  * sin( radians( '" + lat2 + "' ) ) ) ) * 1609.34 ) AS distance");
			if (rs.first()) {
				Distance = rs.getLong("distance");
			}

		} catch (Exception e) {

		}
		return Distance;
	}

	public static double CalculateProductsInCartons(long totalUnits, int productId) {
		Datasource ds = new Datasource();

		try {

			ds.createConnection();

			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			ResultSet productCarton = s
					.executeQuery("select unit_per_catron from inventory_products_view where product_id=" + productId);
			if (productCarton.first()) {
				return (productCarton.getInt("unit_per_catron") != 0)
						? ((double) totalUnits / productCarton.getDouble("unit_per_catron"))
						: 0;
			}

			return 0;
		} catch (Exception e) {
			return 0;
		}
	}
}
