package com.mf.controller.outletregistration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mf.interfaces.IORFunctions;
import com.pbc.util.Datasource;

public class ORFunctions implements IORFunctions {

	@Override
	public boolean GetOuletExists(final Datasource ds, final String MobileRequestId) {
		boolean isExists = false;
		try {
			Statement s = ds.createStatement();
			ResultSet rsOrderExists = s
					.executeQuery("SELECT id from common_outlets_request where mobile_request_id = " + MobileRequestId);
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

	@Override
	public int GetCityByDistributor(final Datasource ds, final long DistributorID) {
		int city_id = 0;
		try {
			Statement s = ds.createStatement();

			System.out.println("SELECT city_id from common_distributors where distributor_id = " + DistributorID);
			ResultSet rsOrderExists = s
					.executeQuery("SELECT city_id from common_distributors where distributor_id = " + DistributorID);
			if (rsOrderExists.first()) {
				city_id = rsOrderExists.getInt("city_id");
			}
			s.close();
		} catch (SQLException e) {
			System.out.println("Check Order Exists Error :- " + e);
			city_id = 0;
		}

		return city_id;
	}

	@Override
	public long GetDistributorByBeatPlanID(Datasource ds, int BeatPlanID) {
		int distributor_id = 0;
		try {
			Statement s = ds.createStatement();

			System.out.println("select distributor_id from distributor_beat_plan where id=" + BeatPlanID);
			ResultSet rsOrderExists = s
					.executeQuery("select distributor_id from distributor_beat_plan where id=" + BeatPlanID);
			if (rsOrderExists.first()) {
				distributor_id = rsOrderExists.getInt("distributor_id");
			}
			s.close();
		} catch (SQLException e) {
			System.out.println("Check Order Exists Error :- " + e);
			distributor_id = 0;
		}

		return distributor_id;
	}

	@Override
	public int GetBeatPlanIDByOrderBooker(Datasource ds, int OrderBookerID) {
		int beat_plan_id = 0;
		try {
			Statement s = ds.createStatement();

			System.out.println("SELECT id FROM distributor_beat_plan_users where assigned_to=" + OrderBookerID);
			ResultSet rsOrderExists = s
					.executeQuery("SELECT id FROM distributor_beat_plan_users where assigned_to=" + OrderBookerID);
			if (rsOrderExists.first()) {
				beat_plan_id = rsOrderExists.getInt("id");
			}
			s.close();
		} catch (SQLException e) {
			System.out.println("Check Order Exists Error :- " + e);
			beat_plan_id = 0;
		}

		return beat_plan_id;
	}
	
	

}
