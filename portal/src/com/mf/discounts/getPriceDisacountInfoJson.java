package com.mf.discounts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mf.modals.PriceHandDiscount;
import com.pbc.util.Datasource;

public class getPriceDisacountInfoJson {
	public static List<PriceHandDiscount> get_price_hand_discount(Datasource ds, String AllOutlets) {
		List<PriceHandDiscount> PriceHandDiscounts = new ArrayList<PriceHandDiscount>();

		try {

			Statement s = ds.createStatement();
			if (AllOutlets.length() > 0) {
				// System.out.println( " SELECT * FROM inventory_price_list_hand_discount_mview
				// where outlet_id in("+AllOutletsString+") " );
				ResultSet rsPHD = s
						.executeQuery(" SELECT * FROM inventory_price_list_hand_discount_mview where outlet_id in("
								+ AllOutlets + ") ");
				while (rsPHD.next()) {

					PriceHandDiscount priceHandDiscount = new PriceHandDiscount(rsPHD.getInt("sampling_id"),
							rsPHD.getLong("outlet_id"), rsPHD.getInt("product_id"), rsPHD.getDouble("discount"),
							rsPHD.getString("created_on"));
					PriceHandDiscounts.add(priceHandDiscount);
				}
			}
			s.close();

		} catch (SQLException e) {
			System.out.println("User Details Error :- " + e);

		}

		return PriceHandDiscounts;
	}
}
