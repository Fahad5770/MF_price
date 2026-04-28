package com.mf.interfaces;

import com.pbc.util.Datasource;

public interface IORFunctions {

	public boolean GetOuletExists(Datasource ds, String MobileRequestId);

	public int GetCityByDistributor(Datasource ds, long DistributorID);

	public long GetDistributorByBeatPlanID(Datasource ds, int DistributorID);

	public int GetBeatPlanIDByOrderBooker(Datasource ds, int OrderBookerID);

}
