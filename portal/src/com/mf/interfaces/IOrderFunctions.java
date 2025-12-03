package com.mf.interfaces;

import java.sql.SQLException;

import com.pbc.util.Datasource;

public interface IOrderFunctions {

	public boolean GetOrderExists(Datasource ds, String MobileRequestId);

	public int getBrandID(long PromotionID, int ProductID, int PromotionIDs)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException;

}
