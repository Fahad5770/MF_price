package com.pbc.reports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

import java.util.Date;

public class SalesIndex {
	Connection c;
	Datasource ds;
	Date FromDate;
	Date ToDate;
	
	
	public SalesIndex(Date FromDate, Date ToDate) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds = new Datasource();
		ds.createConnectionToReplica();
		c = ds.getConnection();
		this.FromDate = FromDate;
		this.ToDate = ToDate;
		
	}
	
	private int getUniqueOutletsOrdered(long OrderBookerID) throws SQLException{
		int UniqueOutletsBilled=0;
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("select count(distinct outlet_id) unique_outlets_billed from mobile_order where created_on between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate)+" and created_by = "+OrderBookerID+" and outlet_id in (SELECT distinct outlet_id planned_universe FROM distributor_beat_plan_log where log_date between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDate(this.ToDate)+" and assigned_to = "+OrderBookerID+")");
		if(rs.first()){
			UniqueOutletsBilled = rs.getInt("unique_outlets_billed");
		}
		s.close();
		return UniqueOutletsBilled;
	}
	
	public int getPlannedUniverse (long OrderBookerID) throws SQLException{
		int PlannedUniverse=0;
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("SELECT count(distinct outlet_id) planned_universe FROM distributor_beat_plan_log where log_date between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDate(this.ToDate)+" and assigned_to = "+OrderBookerID+" and day_number in (select distinct dayofweek(created_on) from mobile_order where created_on between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate)+")");
		if(rs.first()){
			PlannedUniverse = rs.getInt("planned_universe");
		}
		s.close();
		return PlannedUniverse;
	}
	
	public double getECO(long OrderBookerID) throws SQLException{
		double UniqueOutletsOrdered=0;
		double PlannedUniverse=0;
		double ECO=0;
		
		UniqueOutletsOrdered = getUniqueOutletsOrdered(OrderBookerID);
		PlannedUniverse = getPlannedUniverse(OrderBookerID);
		if (PlannedUniverse != 0){
			ECO = (UniqueOutletsOrdered/PlannedUniverse)*100;
		}
		if (ECO > 100){
			ECO = 100;
		}
		
		return ECO;
		
	}
	
	public int getECOScore(double ECO){
		int ECOScore=0;
		if(ECO >=90){
			ECOScore = 100;
		}
		return ECOScore;
	}
	
	private int getTotalUniqueInvoices(long OrderBookerID) throws SQLException{
		int TotalCashMemos=0;
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("select count(distinct outlet_id) unique_outlets_billed from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate)+" and created_by = "+OrderBookerID+" and outlet_id in (SELECT distinct outlet_id planned_universe FROM distributor_beat_plan_log where log_date between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDate(this.ToDate)+" and assigned_to = "+OrderBookerID+"))");
		if(rs.first()){
			TotalCashMemos = rs.getInt("unique_outlets_billed");
		}
		s.close();
		return TotalCashMemos;
	}
	
	private int getTotalInvoices(long OrderBookerID) throws SQLException{
		int TotalCashMemos=0;
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("select count(*) invoice_count from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate)+" and created_by = "+OrderBookerID+" and outlet_id in (SELECT distinct outlet_id planned_universe FROM distributor_beat_plan_log where log_date between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDate(this.ToDate)+" and assigned_to = "+OrderBookerID+"))");
		if(rs.first()){
			TotalCashMemos = rs.getInt("invoice_count");
		}
		s.close();
		return TotalCashMemos;
	}
	
	public double getTotalInvoiceAmount(long OrderBookerID) throws SQLException{
		double amount=0;
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("select sum(net_amount) from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate)+" and created_by = "+OrderBookerID+" and outlet_id in (SELECT distinct outlet_id planned_universe FROM distributor_beat_plan_log where log_date between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDate(this.ToDate)+" and assigned_to = "+OrderBookerID+"))");
		if(rs.first()){
			amount = rs.getDouble(1);
		}
		s.close();
		return amount;
	}
	
	public double getBillProductivity(long OrderBookerID) throws SQLException{
		double TotalCashMemos=0;
		double TotalScheduledOutlets=0;
		double BillProductivity = 0;
		
		
		TotalCashMemos = getTotalUniqueInvoices(OrderBookerID);
		TotalScheduledOutlets = getPlannedUniverse(OrderBookerID);
		
		if (TotalScheduledOutlets != 0){
			BillProductivity = (TotalCashMemos/TotalScheduledOutlets)*100; 
		}
		
		if (BillProductivity > 100){
			BillProductivity = 100;
		}
		//System.out.println(TotalCashMemos +" "+TotalScheduledOutlets);
		return BillProductivity;
	}
	
	public int getBillProductivityScore(double BillProductivity){
		int BillProductivityScore=0;
		if(BillProductivity >=65){
			BillProductivityScore = 100;
		}
		return BillProductivityScore;
	}
	
	public int getTotalLinesSold(long OrderBookerID) throws SQLException{
		int TotalLinesSold=0;
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("select count(*) total_lines_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isap.is_promotion = 0 and isa.order_id in (select id from mobile_order where created_on between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate)+" and created_by = "+OrderBookerID+" and outlet_id in (SELECT distinct outlet_id planned_universe FROM distributor_beat_plan_log where log_date between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDate(this.ToDate)+" and assigned_to = "+OrderBookerID+"))");
		if(rs.first()){
			TotalLinesSold = rs.getInt("total_lines_sold");
		}
		s.close();
		return TotalLinesSold;
	}
	
	public double getRangeSelling(long OrderBookerID) throws SQLException{
		double TotalLinesSold=0;
		double TotalCashMemos=getTotalInvoices(OrderBookerID);
		double RangeSelling=0;
		TotalLinesSold = getTotalLinesSold(OrderBookerID);
		
		if(TotalCashMemos>0){
			RangeSelling = TotalLinesSold/TotalCashMemos;
		}
		
		return RangeSelling;
	}
	
	public int getRangeSellingScore(double RangeSelling){
		int RangeSellingScore=0;
		if(RangeSelling >=15){
			RangeSellingScore = 100;
		}
		return RangeSellingScore;
	}
	
	public int getPackagesPerBill(long OrderBookerID) throws SQLException{
		int TotalPackagePerBill=0;
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("select count(package_count) package_total from (" +
				" select isap.id, count(*) package_count  from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isap.is_promotion = 0 and isa.order_id in (select id from mobile_order where created_on between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate)+" and created_by = "+OrderBookerID+" and outlet_id in (SELECT distinct outlet_id planned_universe FROM distributor_beat_plan_log where log_date between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDate(this.ToDate)+" and assigned_to = "+OrderBookerID+")) group by isap.id,ipv.package_id " +
				" ) tb1");
		
		if(rs.first()){
			TotalPackagePerBill = rs.getInt("package_total");
		}
		s.close();
		return TotalPackagePerBill;
	}
	
	public int getConvertedCasesSold(long OrderBookerID) throws SQLException{
		int ConvertedCases=0;
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("select sum(((isap.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isap.is_promotion = 0 and isa.order_id in (select id from mobile_order where created_on between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate)+" and created_by = "+OrderBookerID+" and outlet_id in (SELECT distinct outlet_id planned_universe FROM distributor_beat_plan_log where log_date between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDate(this.ToDate)+" and assigned_to = "+OrderBookerID+")) ");
		
		if(rs.first()){
			ConvertedCases = rs.getInt(1);
		}
		s.close();
		return ConvertedCases;
	}
	
	public double getSKUPerBill(long OrderBookerID) throws SQLException{
		double TotalSKUPerBill=0;
		double TotalCashMemos=getTotalInvoices(OrderBookerID);
		double SKUPerBill=0;
		TotalSKUPerBill = getPackagesPerBill(OrderBookerID);
		
		if(TotalCashMemos>0){
			SKUPerBill = TotalSKUPerBill/TotalCashMemos;
		}
		
		return SKUPerBill;
	}
	public double getGrossRevenue(long OrderBookerID) throws SQLException{
		double TotalLinesSold=0;
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("select sum(net_amount) gross_revenue from inventory_sales_adjusted isa where isa.order_id in (select id from mobile_order where created_on between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate)+" and created_by = "+OrderBookerID+" and outlet_id in (SELECT distinct outlet_id planned_universe FROM distributor_beat_plan_log where log_date between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDate(this.ToDate)+" and assigned_to = "+OrderBookerID+")) ");
		//ResultSet rs = s.executeQuery("SELECT sum(net_amount) gross_revenue FROM inventory_sales_adjusted where created_on between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate)+" and booked_by = "+OrderBookerID);
		
		if(rs.first()){
			TotalLinesSold = rs.getDouble("gross_revenue");
		}
		s.close();
		return TotalLinesSold;
	}
	
	public int getTotalNumberOfOutlets (long OrderBookerID) throws SQLException{
		int TotalNumberOfOutlets=0;
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("SELECT count(distinct outlet_id) total_number_of_outlets FROM distributor_beat_plan_view where assigned_to = "+OrderBookerID+" and created_on between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate));
		if(rs.first()){				
			TotalNumberOfOutlets = rs.getInt("total_number_of_outlets");
		}
		s.close();
		return TotalNumberOfOutlets;
	}
	
	public Date getAvgTimeIn (long OrderBookerID) throws SQLException{
		Date AvgTimeIn= new Date();;
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("SELECT sec_to_time(avg(time_to_sec(time_format(first_order_time,'%H:%i')))) avg_time_in from (" +
				" SELECT min(mobile_timestamp) first_order_time FROM mobile_order where mobile_timestamp between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate)+" and created_on between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate)+" and created_by="+OrderBookerID+" group by date(mobile_timestamp)" +
				" ) tbl1");
		
		if(rs.first()){				
			AvgTimeIn = rs.getTime("avg_time_in");
		}
		s.close();
		return AvgTimeIn;
	}
	
	public Date getAvgTimeOut (long OrderBookerID) throws SQLException{
		Date AvgTimeOut= new Date();;
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("SELECT sec_to_time(avg(time_to_sec(time_format(last_order_time,'%H:%i')))) avg_time_out from (" +
				" SELECT max(mobile_timestamp) last_order_time FROM mobile_order where mobile_timestamp between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate)+" and created_on between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate)+" and created_by="+OrderBookerID+" group by date(mobile_timestamp)" +
				" ) tbl2");
		if(rs.first()){				
			AvgTimeOut = rs.getTime("avg_time_out");
		}
		s.close();
		return AvgTimeOut;
	}
	
	public double getTotalNumberOfOrdersBefore10 (long OrderBookerID) throws SQLException{
		double TotalNumberOfOrdersBefore10=0;
		double TotalNumberOfOrders=0;
		double TotalNumberOfOrdersBefore10Perc=0;
		
		
		Statement s = c.createStatement();
		Statement s1 = c.createStatement();
		ResultSet rs = s.executeQuery("SELECT count(id) total_number_of_orders_before_10 FROM mobile_order where mobile_timestamp between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate)+" and created_by="+OrderBookerID+" and time_to_sec(time(mobile_timestamp))<time_to_sec('10:00:00')");
		ResultSet rs1 = s1.executeQuery("SELECT count(id) total_number_of_orders FROM mobile_order where mobile_timestamp between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate)+" and created_by="+OrderBookerID);
		
		if(rs.first()){				
			TotalNumberOfOrdersBefore10 = rs.getInt("total_number_of_orders_before_10");
		}
		if(rs1.first()){				
			TotalNumberOfOrders = rs1.getInt("total_number_of_orders");
		}
		if(TotalNumberOfOrders !=0){
			TotalNumberOfOrdersBefore10Perc = (TotalNumberOfOrdersBefore10/TotalNumberOfOrders)*100;
		}
		
		s.close();
		return TotalNumberOfOrdersBefore10Perc;
	}
	
	
	public double getTotalNumberOfOrdersAfter6 (long OrderBookerID) throws SQLException{
		
		double TotalNumberOfOrdersAfter6=0;
		double TotalNumberOfOrders=0;
		double TotalNumberOfOrdersAfter6Perc=0;
		
		Statement s = c.createStatement();
		Statement s1 = c.createStatement();
		ResultSet rs = s.executeQuery("SELECT count(id) total_number_of_orders_after_6 FROM mobile_order where mobile_timestamp between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate)+" and created_by="+OrderBookerID+" and time_to_sec(time(mobile_timestamp))>time_to_sec('18:00:00')");
		ResultSet rs1 = s1.executeQuery("SELECT count(id) total_number_of_orders FROM mobile_order where mobile_timestamp between "+Utilities.getSQLDate(this.FromDate)+" and "+Utilities.getSQLDateNext(this.ToDate)+" and created_by="+OrderBookerID);
		
		if(rs.first()){				
			TotalNumberOfOrdersAfter6 = rs.getInt("total_number_of_orders_after_6");
		}
		if(rs1.first()){				
			TotalNumberOfOrders = rs1.getInt("total_number_of_orders");
		}
		if(TotalNumberOfOrders !=0){
			TotalNumberOfOrdersAfter6Perc = (TotalNumberOfOrdersAfter6/TotalNumberOfOrders)*100;
		}
		
		s.close();
		return TotalNumberOfOrdersAfter6Perc;
	}
	
	public String getGrade(int Score){
		String Grade="";
		if(Score == 300){
			Grade = "A";
		}else if(Score == 200){
			Grade = "B";
		}else if(Score == 100){
			Grade = "C";
		}else{
			Grade = "D";
		}
		
	return Grade;			
	}
	
	public void close() throws SQLException{
		ds.dropConnection();
	}
	
}
