package com.pbc.employee;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class OrderBookerDashboard {

	Connection c;
	Datasource ds;
	
	public OrderBookerDashboard() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds = new Datasource();
		ds.createConnection();
		c = ds.getConnection();
	}
	
	public ResultSet getMarkers(long OrderBookerID) throws SQLException{
		Statement s = c.createStatement();
		
		ResultSet rs = s.executeQuery("select id, name, lat, lng, ifnull((select active from sampling where outlet_id = co.id and active = 1),0) is_discounted from common_outlets co where id in(select distinct dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to="+OrderBookerID+") and lat > 0 ");
		
		
		return rs;
	}
	
	public ResultSet getDistributors(long OrderBookerID) throws SQLException{
		Statement s = c.createStatement();
		
		ResultSet rs = s.executeQuery("select distinct dbpv.distributor_id, cd.name from distributor_beat_plan_view dbpv join common_distributors cd on dbpv.distributor_id = cd.distributor_id where dbpv.assigned_to="+OrderBookerID); 
		
		return rs;
	}
	
	public String getProductGroup(long OrderBookerID) throws SQLException{
		Statement s = c.createStatement();
		
		String ProductGroupName = "None";
		ResultSet rs = s.executeQuery("SELECT epg.product_group_name FROM employee_product_specification eps, employee_product_groups epg where eps.employee_product_group_id=epg.product_group_id and eps.employee_sap_code="+OrderBookerID); 
		if(rs.first()){
			ProductGroupName = rs.getString("product_group_name");
		}
		
		return ProductGroupName;
	}
	
	public int getTotalOutletAssigned(long OrderBookerID) throws SQLException{
		Statement s = c.createStatement();
		
		int TotalOutletAssigned = 0;
		ResultSet rs = s.executeQuery("select count(distinct dbpv.outlet_id) total from distributor_beat_plan_view dbpv  where dbpv.assigned_to="+OrderBookerID); 
		if(rs.first()){
			TotalOutletAssigned = rs.getInt("total");
		}
		s.close();
		return TotalOutletAssigned;
	}
	
	public int[] getTotalOutletsDiscountStatus(long OrderBookerID) throws SQLException{
		Statement s = c.createStatement();
		
		int TotalOutlets[] = new int[2];
		int TotalDiscountedOutlets = 0;
		int TotalNonDiscountedOutlets = 0;
		ResultSet rs = s.executeQuery("select id, ifnull((select active from sampling where outlet_id = common_outlets.id and active = 1),0) is_discounted from common_outlets where id in (select distinct dbpv.outlet_id from distributor_beat_plan_view dbpv  where dbpv.assigned_to="+OrderBookerID+")"); 
		while(rs.next()){
			if(rs.getInt("is_discounted") == 1){
				TotalDiscountedOutlets++;
			}else{
				TotalNonDiscountedOutlets++;
			}
		}
		
		TotalOutlets[0] = TotalDiscountedOutlets;
		TotalOutlets[1] = TotalNonDiscountedOutlets;
		
		s.close();
		return TotalOutlets;
	}
	
	public int getAverageDailyVisits(long OrderBookerID) throws SQLException{
		Statement s = c.createStatement();
		
		int AverageDailyVisits = 0;
		/*
		ResultSet rs = s.executeQuery("SELECT count(ebps.outlet_id)/6 as avg_daily_visit FROM employee_beat_plan ebp, employee_beat_plan_schedule ebps where ebp.beat_plan_id=ebps.beat_plan_id and ebp.assigned_to="+OrderBookerID); 
		if(rs.first()){
			AverageDailyVisits = rs.getInt("avg_daily_visit");
		}*/
		s.close();
		return AverageDailyVisits;
	}
	
	public Date getJoiningDate(long OrderBookerID) throws SQLException{
		Statement s = c.createStatement();
		
		Date JoiningDate = new Date();
		
		ResultSet rs = s.executeQuery("SELECT start_date FROM employee_view where sap_code="+OrderBookerID); 
		if(rs.first()){
			JoiningDate = rs.getDate("start_date");
		}
		
		s.close();
		return JoiningDate;
	}
	
	public String getServiceDuration(long OrderBookerID) throws SQLException{
		Statement s = c.createStatement();
		
		double DifferenceInDays = 0;
		
		ResultSet rs = s.executeQuery("SELECT (to_days(now()) - to_days(start_date)) difference_in_days FROM employee_view where sap_code="+OrderBookerID); 
		if(rs.first()){
			DifferenceInDays = rs.getDouble("difference_in_days");
		}
		
		String ServiceDuration = Utilities.getDisplayCurrencyFormatOneDecimal(DifferenceInDays/365);
		
		s.close();
		return ServiceDuration;
	}
	
	public String getDailySales(long OrderBookerID) throws SQLException, ParseException{
		Statement s = c.createStatement();
		
		Date StartDate = new Date();
		Date EndDate = new Date();
		
		StartDate = getSalesMinMaxDates(OrderBookerID)[0];
		EndDate = getSalesMinMaxDates(OrderBookerID)[1];
		
		String DailySales = "";
		
		if (StartDate != null && EndDate != null){
		
		Date CurrentDate = StartDate;
		int i = 0;
		while(true){
			
			if(i > 0){
				DailySales += ", ";
			}
			
			//ResultSet rs2 = s.executeQuery("SELECT sum(net_amount) as daily_sale FROM inventory_sales_adjusted where booked_by="+OrderBookerID+" and created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate));
			ResultSet rs2 = s.executeQuery("SELECT sum(amount_amtnt) as daily_sale FROM sap_sales where personnel_no_crid="+OrderBookerID+" and created_on_erdat between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDate(CurrentDate));
			if(rs2.first()){
				
				if(rs2.getString("daily_sale") != null){
					DailySales += rs2.getString("daily_sale");
				}else{
					DailySales += "0";
				}
			}
			
			if(CurrentDate.equals(EndDate)){
				break;
			}
			
			i++;
			CurrentDate = DateUtils.addDays(CurrentDate, 1);
			
		}
		
		s.close();
		
		}
		
		return DailySales;
	}
	
	public Date[] getSalesMinMaxDates(long OrderBookerID) throws SQLException, ParseException{
		Statement s = c.createStatement();
		
		Date SalesMinMaxDates[] = new Date[2];
		
		//ResultSet rs = s.executeQuery("SELECT min(created_on) as start_date, max(created_on) as end_date FROM inventory_sales_adjusted where booked_by="+OrderBookerID);
		ResultSet rs = s.executeQuery("SELECT min(created_on_erdat) as start_date, max(created_on_erdat) as end_date FROM sap_sales where personnel_no_crid="+OrderBookerID);
		if(rs.first()){
			SalesMinMaxDates[0] = rs.getDate("start_date");
			SalesMinMaxDates[1] = rs.getDate("end_date");
		}
		
		//SimpleDateFormat sdf =  new SimpleDateFormat("yyyy, MM, dd");
		//SalesMinMaxDates[0] = sdf.parse("2013, 12, 1");
		
		s.close();
		
		return SalesMinMaxDates;
	}
	
	
	public int[] getDateAttributesInNumbers(Date CurrentDate) throws SQLException{
		
		int DateAttributes[] = new int[3];
		
		DateAttributes[0] = Utilities.getYearByDate(CurrentDate);
		DateAttributes[1] = Utilities.getMonthNumberByDate(CurrentDate);
		DateAttributes[2] = Utilities.getDayNumberByDate(CurrentDate);
		
		return DateAttributes;
	}
	
	public int getProductivity(long OrderBookerID, Date StartDate, Date EndDate) throws SQLException{
		Statement s = c.createStatement();
		
		int Productivity = 0;
		ResultSet rs = s.executeQuery("SELECT (( sum(total_no_of_orders) / sum(total_visits_due) )*100) productivity FROM bi_orderbooker_daily where orderbooker_id="+OrderBookerID+" and dated between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)); 
		if(rs.first()){
			Productivity = rs.getInt("productivity");
		}
		s.close();
		return Productivity;
	}
	
	public int getDropSize(long OrderBookerID, Date StartDate, Date EndDate) throws SQLException{
		Statement s = c.createStatement();
		
		int DropSize = 0;
		ResultSet rs = s.executeQuery("SELECT sum(raw_cases_sold)/sum(total_no_of_invoices) drop_size FROM bi_orderbooker_daily where orderbooker_id="+OrderBookerID+" and dated between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)); 
		if(rs.first()){
			DropSize = rs.getInt("drop_size");
		}
		s.close();
		return DropSize;
	}
	
	public double getSKUPerBill(long OrderBookerID, Date StartDate, Date EndDate) throws SQLException{
		Statement s = c.createStatement();
		
		double SKUPerBill = 0;
		ResultSet rs = s.executeQuery("SELECT sum(total_no_of_invoice_items)/sum(total_no_of_invoices) sku_per_bill FROM bi_orderbooker_daily where orderbooker_id="+OrderBookerID+" and dated between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)); 
		if(rs.first()){
			SKUPerBill = rs.getDouble("sku_per_bill");
		}
		s.close();
		return SKUPerBill;
	}
	
	public int[] getBIDetail(long OrderBookerID, Date date) throws SQLException{
		Statement s = c.createStatement();
		
		int BIDetail[] = new int[3];
		
		ResultSet rs = s.executeQuery("SELECT total_visits_due, total_no_of_orders, total_no_of_invoices  FROM bi_orderbooker_daily where orderbooker_id="+OrderBookerID+" and dated= "+Utilities.getSQLDate(date)); 
		if(rs.first()){
			BIDetail[0] = rs.getInt("total_visits_due");
			BIDetail[1] = rs.getInt("total_no_of_orders");
			BIDetail[2] = rs.getInt("total_no_of_invoices");
		}
		s.close();
		return BIDetail;
	}
	
	public double getAverageDailySales(long OrderBookerID) throws SQLException{
		Statement s = c.createStatement();
		
		double average = 0;
		
		ResultSet rs = s.executeQuery("SELECT avg(total_amount_invoices) FROM bi_orderbooker_daily where orderbooker_id="+OrderBookerID+" and total_visits_due > 0"); 
		if(rs.first()){
			average = rs.getDouble(1);
		}
		s.close();
		
		return average;
	}
	
	public void close() throws SQLException{
		ds.dropConnection();
	}
	
}
