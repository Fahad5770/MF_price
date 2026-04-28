package com.pbc.myscripts;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;

public class ResolveUneditedOrders {

	public static void main(String[] args)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		// TODO Auto-generated method stub

		Datasource ds = new Datasource();
		ds.createConnection();
		Connection c = ds.getConnection();

		Statement s = ds.createStatement();

		ResultSet rsIds = s.executeQuery(
				"select id from mobile_order_unedited where mobile_timestamp between '2025-09-01' and '2025-09-10' and id not in (select unedited_order_id from mobile_order where  mobile_timestamp between '2025-09-01' and '2025-09-10')");
		while (rsIds.next()) {
		//9942715,
		// 9942730,
		// 9942731
			System.out.println("ID : " + rsIds.getLong("id"));
			SalesPosting.splitOrder_2(rsIds.getLong("id"));

}

		s.close();

	}

	public static boolean IsAllowed(long Beatplanid, long outlet_id, int DayNumber, long IsAlternative) {

		boolean Allowed = false;
		try {
			Datasource ds = new Datasource();
			ds.createConnection();
			ds.startTransaction();
			Statement s1 = ds.createStatement();

			Date CurrentDate = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(CurrentDate);
			System.out.println("CurrentDate" + CurrentDate);
			int week = cal.get(Calendar.WEEK_OF_YEAR);
			week--;
			System.out.println("week" + week);
			if (IsAlternative == 2 && week % 2 == 0) {
				// week is even
				Allowed = true;
				System.out.println("even" + week);
			} else if (IsAlternative == 1 && week % 2 != 0) {
				// week is odd
				Allowed = true;
				System.out.println("odd" + week);
			}

			// int month = Utilities.getMonthNumberByDate(CurrentDate);
			// System.out.println("month"+month);
			// int year = Utilities.getYearByDate(CurrentDate);
			// System.out.println("year"+year);
			// Date StartDateThisMonth = Utilities.getStartDateByDate(CurrentDate);
			// System.out.println("StartDateThisMonth"+StartDateThisMonth);
			// Date iDate = StartDateThisMonth;
			// int Counter=0;
			// int MondayCounter = 0;

			// while(true) {
			//
			//
			// System.out.println("week ="+week);
			//
			// if(Utilities.getDayOfWeekByDate(iDate)==2) {
			// MondayCounter++;
			// }
			// if(DateUtils.isSameDay(CurrentDate,iDate)){
			// System.out.println("its same date");
			// break;
			// }
			// if(Counter>31) {
			// break;
			// }
			// iDate = Utilities.getDateByDays(iDate, 1);
			// Counter++;
			// }
			// System.out.println("outlet_id"+outlet_id);
			// System.out.println("MondayCounter"+MondayCounter);
			// if(IsAlternative ==1 && (MondayCounter==1||MondayCounter==3)) {
			// Allowed=true;
			// }else if(IsAlternative ==2 && (MondayCounter==2||MondayCounter==4)) {
			// Allowed=true;
			// }else {
			// Allowed=false;
			// }
			/*
			 * String sql = "SELECT * FROM pep.distributor_beat_plan_log where id='" +
			 * Beatplanid + "' and outlet_id='" + outlet_id + "' and log_date=" +
			 * Utilities.getSQLDate(Utilities.getDateByDays(-7))+" and day_number="
			 * +DayNumber;
			 * 
			 * ResultSet rs = s1.executeQuery(sql); if (rs.next()) { Allowed = false;
			 * //System.out.println( //"Exists in previos week " + "id=" + Beatplanid +
			 * " and outlet_id=" + outlet_id); }
			 */

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Allowed;
	}

}
