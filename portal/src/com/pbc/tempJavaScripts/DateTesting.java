package com.pbc.tempJavaScripts;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.pbc.inventory.StockDocument;
import com.pbc.inventory.StockPosting;
import com.pbc.util.Datasource;

import com.pbc.util.Utilities;
import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.util.DateFormatConverter;

public class DateTesting {

	public static void main(String[] args) throws SQLException, ParseException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		

		boolean Allowed = false;
		try {
		//	Datasource ds = new Datasource();
		//	ds.createConnection();
		//	ds.startTransaction();
		//	Statement s1 = ds.createStatement();
			int IsAlternative=1;
			Date CurrentDate = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(CurrentDate);
			// System.out.println("CurrentDate"+CurrentDate);
			int week = cal.get(Calendar.WEEK_OF_MONTH);
			//week--;
			System.out.println("week " + week);
			if (IsAlternative == 1 && week % 2 == 0) {
				// week is even
				Allowed = true;
				// System.out.println("even"+week);
			} else if (IsAlternative == 0 && week % 2 != 0) {
				// week is odd
				Allowed = true;
				// System.out.println("odd"+week);
			}
//			//get first monday of month
//			Date CurrentDate = new Date();
//			int month = Utilities.getMonthNumberByDate(CurrentDate);
//			System.out.println("month"+month);
//			int year = Utilities.getYearByDate(CurrentDate);
//			System.out.println("year"+year);
//			Date StartDateThisMonth = Utilities.getStartDateByDate(CurrentDate);   
//			System.out.println("StartDateThisMonth"+StartDateThisMonth);
//			Date iDate = StartDateThisMonth;
//			int Counter=0;
//			int MondayCounter = 0;
//			while(true) {
//				System.out.println("Utilities.getDayOfWeekByDate(iDate)"+Utilities.getDayOfWeekByDate(iDate));
//				if(Utilities.getDayOfWeekByDate(iDate)==2) {
//					MondayCounter++;
//				}
//				if(DateUtils.isSameDay(CurrentDate,iDate)){
//					System.out.println("its same date");
//					break;
//				}
//				if(Counter>31) {
//					break;
//				}
//				iDate = Utilities.getDateByDays(iDate, 1);
//				Counter++;
//			}
//			//System.out.println("outlet_id"+outlet_id);
//			System.out.println("MondayCounter"+MondayCounter);
//			if(IsAlternative ==1 && (MondayCounter==1 || MondayCounter==3 || MondayCounter==5)) {
//				Allowed=true;
//			}else if(IsAlternative ==2 && (MondayCounter==2 || MondayCounter==4)) {
//				Allowed=true;
//			}else  {
//				Allowed=false;
//			}
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
		System.out.println(Allowed);
		
//		/Date d= new Date("2024-06-01");
//		Date today= new Date();
//		Date StartDate= Utilities.parseDate("01/07/2024");
//		System.out.println(StartDate);
//		System.out.println(today.after(StartDate));
//		
//		Connection c;
//		Datasource ds;
//		ds = new Datasource();

	//	try {
			// StockPosting sp = new StockPosting(true);
		
//		int DayOfMonth =Utilities.getDayNumberByDate(new Date());
//		//Date StartDate = Utilities.getDateByDays(new Date(), -1);
//		Date StartDateToShow = Utilities.getDateByDays(new Date(), -DayOfMonth);
//		Date StartDate = Utilities.getDateByDays(new Date(), -DayOfMonth);
//		
//		Date EndDate = Utilities.getDateByDays(new Date(), 0);
//		
//		StartDate = Utilities.parseDate("01/11/2022");
//		
//				//EndDate = Utilities.parseDate("07/11/2022");
//				int ManualFlag=0;  
//				Date CurrentDate = StartDate;
//				
//				//String EmployeeIDs="0204211291,0204211283,0204211300,0204211299,0204211298";
//				
//				String EmployeeIDs=""; //for regular flow
//				System.out.println(StartDateToShow +"-"+ ManualFlag);
//				while(true){
//					
//					System.out.println(Utilities.getSQLDate(StartDate)+" - "+Utilities.getSQLDate(EndDate)+" - "+Utilities.getSQLDate(CurrentDate));
//					
//					//PopulateData(CurrentDate,EmployeeIDs,ManualFlag, StartDateToShow);
//					if(DateUtils.isSameDay(CurrentDate, EndDate)){
//						break;
//					}
//
//					CurrentDate = Utilities.getDateByDays(CurrentDate, 1);
//				}
//				
//		Date Start_Date= new Date();
//		Date End_Date = Utilities.getDateByDays(3);
//		//System.out.println(Start_Date);
//		//System.out.println(End_Date);
//		String dayNumbers = "";
//		int c=0;
//		Date date = Start_Date;
//		while (true) {
		// System.out.println(Utilities.getDayOfWeekByDate(date));
//			dayNumbers += (c == 0) ? Utilities.getDayOfWeekByDate(date) : ","+Utilities.getDayOfWeekByDate(date);
//			if (DateUtils.isSameDay(date,End_Date)) {
//				break;
//			}
//			date = DateUtils.addDays(date, 1);
//			c++;
//		}
//		System.out.println(dayNumbers);
//		long Shop_Time = DateUtils.getTimeDifference(Start_time, End_time);
//		 System.out.println(Shop_Time);
//		int NoOfDaysToGoBack = -18;
//
//		Date StartDate = Utilities.getDateByDays(NoOfDaysToGoBack);
//		Date	EndDate = StartDate;
//		 Date date = StartDate;
//			while (true) {
//				System.out.println(date);
//				if (date.equals(EndDate)) {
//					break;
//				}
//				date = DateUtils.addDays(date, 1);
//				
//			}
//		 final long[] ConvertedUnits = Utilities.getRawCasesAndUnits(10, 10);
//		 System.out.println(ConvertedUnits[0]);
//		 System.out.println(ConvertedUnits[1]);
//		 
//         final double AmountRawCases = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(ConvertedUnits[0] * 172.96));
//         System.out.println(AmountRawCases);
////         
////         System.out.println(Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(0 * 18.40)));
////         
//         System.out.println(Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(172.96 *1 /100)));
	

//			}
//		}
	/// String input = "Hello, World,";

	// Remove trailing comma if it exists
	// if (input.endsWith(",")) {
	// input = input.substring(0, input.length() - 1);
	// }

	// System.out.println("Modified string: " + input);
//		
	// Date parsedDate=null;
	// Date parsedDate2=null;

//    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//      //  Date d = null;
//        try {
//             parsedDate = dateFormat.parse(val);
//             parsedDate2 = dateFormat.parse(val2);
//            d=parsedDate;
//            System.out.println("Parsed Date: " + parsedDate);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//		
//		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
//		
//		  long diffInMillis = parsedDate2.getTime() - parsedDate.getTime();
//          long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
//	
//		System.out.println("Total Date: "+diffInMinutes);
//	//	System.out.println("'" + format.format(d) + "'");
//		}

}
}
	
