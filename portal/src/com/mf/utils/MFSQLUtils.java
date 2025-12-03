package com.mf.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import org.apache.commons.lang3.time.DateUtils;

import com.pbc.util.Datasource;
import com.pbc.util.AlmoizDateUtils;

public class MFSQLUtils {

	public static String db_md5(String password) {
		Datasource ds = new Datasource();

		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			System.out.println("select md5('" + password + "') encypt_password");
			ResultSet rsOffDayQuery = s.executeQuery("select md5('" + password + "') encypt_password");

			return (rsOffDayQuery.first() ? rsOffDayQuery.getString("encypt_password") : "");

		} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("Getting User Payroll Error :- " + e);
			return "";
		}

	}

	public static int total_expense(int user_id, int month, int year) {
		Datasource ds = new Datasource();
		Date startDate = AlmoizDateUtils.getStartDateByMonth(month - 1, year);
		Date endDate = (AlmoizDateUtils.isCurrentMonth(startDate)) ? new Date()
				: AlmoizDateUtils.getEndDateByMonth(month - 1, year);
		System.out.println("startDate " + startDate);
		System.out.println("endDate " + endDate);

		int TA_Total = 0, DA_Total = 0, salary = 0, mobile_expenses = 0, bike_maintainance = 0, total_stationary = 0,
				total_Salary = 0;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			System.out.println("select * from user_payroll where user_id=" + user_id + " and is_active=1");
			ResultSet rsPay = s
					.executeQuery("select * from user_payroll where user_id=" + user_id + " and is_active=1");
			if (rsPay.first()) {

				salary = rsPay.getInt("salary");
				mobile_expenses = rsPay.getInt("mobile_expenses");
				bike_maintainance = rsPay.getInt("bike_maintainance");

				Date currentDate = startDate;
				while (true) {

					int today_TA = 0, today_DA = 0, today_stationary = 0, allowanceCategoryId = 0, kilometers = 0,
							allowance_id = 0;

					System.out.println(
							"select id, allowance_category_id, kilometers, allowance_id  from mobile_travel_allowance mta where mobile_timestamp between "
									+ AlmoizDateUtils.getSQLDate(currentDate) + " and "
									+ AlmoizDateUtils.getSQLDateNext(currentDate) + " and created_by=" + user_id);
					ResultSet rsTravel = s2.executeQuery(
							"select id, allowance_category_id, kilometers, allowance_id  from mobile_travel_allowance mta where mobile_timestamp between "
									+ AlmoizDateUtils.getSQLDate(currentDate) + " and "
									+ AlmoizDateUtils.getSQLDateNext(currentDate) + " and created_by=" + user_id);

					if (rsTravel.first()) {
						kilometers = rsTravel.getInt("kilometers");
						allowanceCategoryId = rsTravel.getInt("allowance_category_id");
						allowance_id = rsTravel.getInt("allowance_id");
					}

					ResultSet rsGetTAAmount = s2.executeQuery("select "
							+ ((allowanceCategoryId == 1) ? "ta_local" : "(ta_out_station * " + kilometers + ")")
							+ " ta from user_payroll where user_id=" + user_id + " and id=" + allowance_id);
					today_TA = (rsGetTAAmount.first()) ? rsGetTAAmount.getInt("ta") : 0;

					ResultSet rsDaily = s2.executeQuery(
							"select id, allowance_category_id from mobile_daily_allowance where mobile_timestamp between "
									+ AlmoizDateUtils.getSQLDate(currentDate) + " and "
									+ AlmoizDateUtils.getSQLDateNext(currentDate) + " and created_by=" + user_id);
					today_DA = (rsDaily.first())
							? (rsDaily.getInt("allowance_category_id") == 1) ? rsPay.getInt("da_local")
									: rsPay.getInt("da_out_station")
							: 0;

					// int daCategoryId = 0, da_allowance_id = 0;
					// ResultSet rsDaily = s2.executeQuery(
					// "select id, allowance_category_id, allowance_id from mobile_daily_allowance
					// where mobile_timestamp between "
					// + MMCDateUtils.getSQLDate(currentDate) + " and "
					// + MMCDateUtils.getSQLDateNext(currentDate) + " and created_by=" + user_id);
					// if (rsDaily.first()) {
					// daCategoryId = rsDaily.getInt("allowance_category_id");
					// da_allowance_id = rsDaily.getInt("allowance_id");
					// }
					// System.out.println("select " + ((daCategoryId == 1) ? "da_local" :
					// "da_out_station")
					// + " da from user_payroll where user_id=" + user_id + " and id=" +
					// da_allowance_id);
					// ResultSet rsGetDAAmount = s2
					// .executeQuery("select " + ((daCategoryId == 1) ? "da_local" :
					// "da_out_station")
					// + " da from user_payroll where user_id=" + user_id + " and id=" +
					// da_allowance_id);
					// today_DA = (rsGetDAAmount.first()) ? rsGetDAAmount.getInt("da") : 0;

					ResultSet rsStstionary = s2.executeQuery(
							"select sum(amount) as amount from mobile_stationary where mobile_timestamp between "
									+ AlmoizDateUtils.getSQLDate(currentDate) + " and "
									+ AlmoizDateUtils.getSQLDateNext(currentDate) + " and created_by=" + user_id);
					today_stationary = (rsStstionary.first()) ? rsStstionary.getInt("amount") : 0;

					total_stationary += today_stationary;
					TA_Total += today_TA;
					DA_Total += today_DA;
					if (DateUtils.isSameDay(currentDate, endDate)) {
						break;
					}

					currentDate = AlmoizDateUtils.getDateByDays(currentDate, 1);

				} // end of while date

			}

			total_Salary = salary + mobile_expenses + bike_maintainance + TA_Total + DA_Total + total_stationary;
			System.out.println("salary " + salary);
			System.out.println("mobile_expenses " + mobile_expenses);
			System.out.println("bike_maintainance " + bike_maintainance);
			System.out.println("TA_Total " + TA_Total);
			System.out.println("DA_Total " + DA_Total);
			System.out.println("total_stationary " + total_stationary);

			return total_Salary;
		} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("Getting User Payroll Error :- " + e);
			return 0;
		}
	}

	public static boolean check_off_day(int user_id, Date date) {
		Datasource ds = new Datasource();

		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			System.out.println("select day_number from user_payroll where user_id=" + user_id + " and is_active=1");
			ResultSet rsOffDayQuery = s.executeQuery("select * from user_payroll where user_id=" + user_id
					+ " and day_number=" + AlmoizDateUtils.getDayOfWeekByDate(date));

			return (rsOffDayQuery.first());

		} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("Getting User Payroll Error :- " + e);
			return false;
		}

	}

	public static boolean R365_is_decline(int level, int user_id, int month, int year) {
		Datasource ds = new Datasource();

		try {
			ds.createConnection();
			Statement s = ds.createStatement();

			System.out.println("select id from payroll_approval where is_L1_approved=1 and is_L2_approved="
					+ ((level == 2) ? "0" : "1") + " and is_delete=0 and is_L3_approved=0 and month=" + month
					+ " and year=" + year + " and user_id=" + user_id);
			ResultSet rsStatusQuery = s
					.executeQuery("select id from payroll_approval where is_L1_approved=1 and is_L2_approved="
							+ ((level == 2) ? "0" : "1") + " and is_delete=0 and is_L3_approved=0 and month=" + month
							+ " and year=" + year + " and user_id=" + user_id);
			System.out.println(rsStatusQuery.first());
			return (rsStatusQuery.first());

		} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("Getting User Payroll Error :- " + e);
			return false;
		}

	}

	public static boolean R365_is_approve(int level, int user_id, int month, int year) {
		Datasource ds = new Datasource();

		try {
			ds.createConnection();
			Statement s = ds.createStatement();

			String levelQuery = (level == 1) ? " and is_L1_approved=1 "
					: (level == 2) ? " and is_L1_approved=1 and is_L2_approved=0"
							: " and is_L1_approved=1 and is_L2_approved=1 and is_L3_approved=0";

			System.out.println("SELECT   id FROM payroll_approval where month=" + month + " and year=" + year
					+ " and is_delete=0 and user_id=" + user_id + levelQuery);
			ResultSet rsStatusQuery = s.executeQuery("SELECT   id FROM payroll_approval where month=" + month
					+ " and year=" + year + " and is_delete=0 and user_id=" + user_id + levelQuery);
			System.out.println(!rsStatusQuery.first());
			if (level == 1) {
				return (rsStatusQuery.first());
			} else {
				return (!rsStatusQuery.first());
			}

		} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("Getting User Payroll Error :- " + e);
			return false;
		}

	}

	public static boolean check_payroll_exists(int user_id) {
		Datasource ds = new Datasource();

		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			ResultSet rsPay = s.executeQuery("select * from user_payroll where user_id=" + user_id);
			return (rsPay.first());
		} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("Getting User Payroll Error :- " + e);
			return false;
		}
	}

	public static LinkedHashMap<String, String> getUserRegion(final Datasource ds, final int userId) {
		LinkedHashMap<String, String> UserRegion = new LinkedHashMap<String, String>();
		UserRegion.put("region_id", "0");
		UserRegion.put("region_name", "");

		try {
			Statement s = ds.createStatement();
			/*
			 * System.out.println("SELECT DISTINCT region_id, " +
			 * "(SELECT region_name FROM common_regions cr WHERE cr.region_id = v.region_id) AS region "
			 * + "FROM distributor_beat_plan_view v WHERE assigned_to = " + userId);
			 */
			ResultSet rsRegion = s.executeQuery("SELECT DISTINCT region_id, "
					+ "(SELECT region_name FROM common_regions cr WHERE cr.region_id = v.region_id) AS region "
					+ "FROM distributor_beat_plan_view v WHERE assigned_to = " + userId);
			if (rsRegion.first()) {
				UserRegion.put("region_id", rsRegion.getString("region_id"));
				UserRegion.put("region_name", rsRegion.getString("region"));
			}
			s.close();
		} catch (SQLException e) {
			System.out.println("Getting Sales Tax Error :- " + e);
			UserRegion.put("region_id", "0");
			UserRegion.put("region", "");
		}

		return UserRegion;
	}

	public static LinkedHashMap<String, Double> getTaxeinfo(final Datasource ds) {
		LinkedHashMap<String, Double> taxInfo = new LinkedHashMap<String, Double>();
		taxInfo.put("sales_tax_rate", 0.0);
		taxInfo.put("wh_tax_rate", 0.0);
		try {
			Statement s = ds.createStatement();
			System.out.println("select * from inventory_sales_tax_rates");
			ResultSet rsTaxes = s.executeQuery("select * from inventory_sales_tax_rates");
			while (rsTaxes.next()) {
				switch (rsTaxes.getInt("id")) {
				case 1:
					taxInfo.put("sales_tax_rate", rsTaxes.getDouble("rate"));
					break;
				case 2:
					taxInfo.put("wh_tax_rate", rsTaxes.getDouble("rate"));
					break;
				default:
					taxInfo.put("sales_tax_rate", 0.0);
					taxInfo.put("wh_tax_rate", 0.0);
				}
			}
			s.close();
		} catch (SQLException e) {
			System.out.println("Getting Sales Tax Error :- " + e);
			taxInfo.put("sales_tax_rate", 0.0);
			taxInfo.put("wh_tax_rate", 0.0);
		}

		return taxInfo;
	}

}
