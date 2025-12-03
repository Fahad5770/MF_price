package com.mf.controller.order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mf.interfaces.IOrderFunctions;
import com.pbc.util.Datasource;

public class OrderFunctions implements IOrderFunctions {
	
	@Override
	public int getBrandID(long PromotionID, int ProductID, int PromotionIDs)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

		int pret = 0;
		

			if (ProductID == PromotionID) {
				pret = ProductID;
			}

		

		int ret = 0;

		if (pret != 0) {

			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();

			ResultSet rs = s.executeQuery("select brand_id from inventory_products where id  =" + pret);
			if (rs.first()) {
				ret = rs.getInt(1);
			}

			s.close();
			ds.dropConnection();

		}

		return ret;
	}

	@Override
	public boolean GetOrderExists(final Datasource ds, final String MobileRequestId) {
		boolean isExists = false;
		try {
			Statement s = ds.createStatement();
			System.out.println("SELECT id from mobile_order_unedited where mobile_order_no = " + MobileRequestId);
			ResultSet rsOrderExists = s
					.executeQuery("SELECT id from mobile_order_unedited where mobile_order_no = " + MobileRequestId);
			if (rsOrderExists.first()) {
				isExists = true;
			}
			s.close();
		} catch (SQLException e) {
			System.out.println("Check Order Exists Error :- " + e);
			isExists = false;
		}

		return isExists;
	}

}
