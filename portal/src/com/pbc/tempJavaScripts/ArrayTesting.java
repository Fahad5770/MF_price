package com.pbc.tempJavaScripts;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.pbc.inventory.StockPosting;
import com.pbc.reports.HaversineDistanceCalculator;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.util.DateFormatConverter;

public class ArrayTesting {

	public static void main(String[] args) throws SQLException, ParseException, ClassNotFoundException,
	InstantiationException, IllegalAccessException {
		
		int DayOfMonth =33;
		//Date StartDate = Utilities.getDateByDays(new Date(), -1);
		Date StartDateToShow = Utilities.getDateByDays(new Date(), -DayOfMonth);
		Date StartDate = Utilities.getDateByDays(new Date(), -DayOfMonth);
		
		Date EndDate = Utilities.getDateByDays(new Date(), 0);
		
		System.out.println("StartDateToShow "+StartDateToShow);
		System.out.println("StartDate "+StartDate);
		System.out.println("EndDate "+EndDate);
		
		System.out.println("SELECT"+ 
		        "`iplpp`.`price_list_id` AS `price_list_id`,"+
		        "`co`.`id` AS `outlet_id`,"+
		        "`iplp`.`product_id` AS `product_id`,"+
		        "`iplp`.`raw_case` AS `raw_case`,"+
		       " `iplp`.`unit` AS `unit`"+
		    "FROM"+
		        "(((`inventory_price_list_pjp` `iplpp`"+
		        "JOIN `inventory_price_list` `ipl`)"+
		       " JOIN `inventory_price_list_products` `iplp`)"+
		        "JOIN `common_outlets` `co`)"+
		    "WHERE"+
		        "((`ipl`.`id` = `iplp`.`id`)"+
		            "AND (`ipl`.`id` = `iplpp`.`price_list_id`)"+
		            "AND (`iplpp`.`pjp_id` = `co`.`cache_beat_plan_id`)"+
		            "AND (`ipl`.`is_active` = 1)"+
		            "AND (`ipl`.`id` <> 1)"+
		            "AND (`ipl`.`valid_from` <= CURDATE())"+
		            "AND (`ipl`.`valid_to` >= CURDATE()))");
		
//		double distance = HaversineDistanceCalculator.calculateHaversineDistance(33.686118333333330,
//				
//				73.016131666666670,	33.6861183333333300000,	73.0161316666666700000
//
//);
//		
//		System.out.println("Distance : "+distance);

		// TODO Auto-generated method stub
//		ArrayList<Integer> VisitsWithStock = new ArrayList<Integer>();
//		int CountPackage=9;
//		int[] Visits = new int[CountPackage];
//		for(int i=0; i<Visits.length; i++) {
//			Visits[i] = i;
//		}
//		for(int i=0; i<Visits.length; i++) {
//			System.out.println("Visit : "+Visits[i]);
//		}
//		
//	for(int i=0; i<5; i++) {
//			
//			try {
//				VisitsWithStock.get(i);
//			}catch(Exception e){
//				//System.out.println("Total Date: "+e);
//				VisitsWithStock.add(i,0);
//			}
//			
//			
//			if(i%2 == 1) {
//				 VisitsWithStock.set(i, VisitsWithStock.get(i)+1) ;
//			}
//			
//		}
//		
//		
//	
//		System.out.println("Total Date: "+VisitsWithStock);
		}

}
