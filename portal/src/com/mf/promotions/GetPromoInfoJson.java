package com.mf.promotions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mf.modals.SalesPromotionsProducts;
import com.mf.modals.ActivePromotion;
import com.pbc.util.Datasource;

public class GetPromoInfoJson {

	public static List<ActivePromotion> get_active_promotions(Datasource ds, String AllOutlets) {
		List<ActivePromotion> activePromotions = new ArrayList<ActivePromotion>();

		try {

			Statement s = ds.createStatement();

			System.out.println(
					"SELECT * FROM inventory_sales_promotions_active_mview where outlet_id in("
							+ AllOutlets + ") order by outlet_id");
			ResultSet rsActivePromo = s
					.executeQuery(
							"SELECT * FROM inventory_sales_promotions_active_mview where outlet_id in("
									+ AllOutlets + ") order by outlet_id");
			while (rsActivePromo.next()) {
				ActivePromotion activePromotion = new ActivePromotion(rsActivePromo.getInt("product_promotion_id"),
						rsActivePromo.getInt("outlet_id"));
				activePromotions.add(activePromotion);
			}

			s.close();

		} catch (SQLException e) {
			System.out.println("User Details Error :- " + e);

		}

		return activePromotions;
	}

	public static List<SalesPromotionsProducts> get_promotion_products(Datasource ds, String AllOutlets) {
		List<SalesPromotionsProducts> salesPromotionsProducts = new ArrayList<SalesPromotionsProducts>();

		try {

			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();

			if (AllOutlets.length() > 0) {
				ResultSet rsPromoId = s.executeQuery(
						"SELECT distinct product_promotion_id FROM inventory_sales_promotions_active_mview where outlet_id in("
								+ AllOutlets + ") order by outlet_id");
				while (rsPromoId.next()) {

					ResultSet rsSPP = s2.executeQuery(
							"SELECT package_id, total_units FROM inventory_sales_promotions_products where id="
									+ rsPromoId.getString("product_promotion_id") + " and type_id = 2");
					while (rsSPP.next()) {

						int promoId = rsPromoId.getInt("product_promotion_id");
						int packageId = rsSPP.getInt("package_id");

						ResultSet rsPromoBrand = s3.executeQuery(
								"SELECT brand_id FROM inventory_sales_promotions_products_brands where id=" + promoId
										+ " and type_id = 1 and package_id = " + packageId);
						int brand_id = (rsPromoBrand.first()) ? rsPromoBrand.getInt("brand_id") : 0;

						SalesPromotionsProducts salesPromotionsProduct = new SalesPromotionsProducts(promoId, packageId,
								"", 0, rsSPP.getInt("total_units"), brand_id, "");

						salesPromotionsProducts.add(salesPromotionsProduct);
					}

				} // end while
			}

			s.close();
			s2.close();
			s3.close();

		} catch (SQLException e) {
			System.out.println("User Details Error :- " + e);

		}

		return salesPromotionsProducts;
	}

	public static List<SalesPromotionsProducts> get_promotion_Free_products(Datasource ds, String AllOutlets) {
		List<SalesPromotionsProducts> salesPromotionsProducts = new ArrayList<SalesPromotionsProducts>();

		try {

			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();

			if (AllOutlets.length() > 0) {
				ResultSet rsPromoId = s.executeQuery(
						"SELECT distinct product_promotion_id FROM inventory_sales_promotions_active_mview where outlet_id in("
								+ AllOutlets + ") order by outlet_id");
				while (rsPromoId.next()) {

					ResultSet rsSPP = s2.executeQuery(
							"SELECT package_id, total_units FROM inventory_sales_promotions_products where id="
									+ rsPromoId.getString("product_promotion_id") + " and type_id = 2");
					while (rsSPP.next()) {

						int promoId = rsSPP.getInt("product_promotion_id");
						int packageId = rsSPP.getInt("package_id");

						ResultSet rsPromoBrand = s3.executeQuery(
								"SELECT brand_id FROM inventory_sales_promotions_products_brands where id=" + promoId
										+ " and type_id = 2 and package_id = " + packageId);
						int brand_id = (rsPromoBrand.first()) ? rsPromoBrand.getInt("brand_id") : 0;

						SalesPromotionsProducts salesPromotionsProduct = new SalesPromotionsProducts(promoId, packageId,
								"", 0, rsSPP.getInt("total_units"), brand_id, "");

						salesPromotionsProducts.add(salesPromotionsProduct);
					}

				} // end while
			}

			s.close();
			s2.close();
			s3.close();

		} catch (SQLException e) {
			System.out.println("User Details Error :- " + e);

		}

		return salesPromotionsProducts;
	}
}
