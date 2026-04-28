package com.pbc.sms;

import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.util.Datasource;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.net.*;
import java.io.*;
public class S111MonthlySecondarySalesStats extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public S111MonthlySecondarySalesStats() {
        super();
    }

    
    public static void main(String [] args) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
    	Date TodayDate = new Date();
        Date YesterdayDate = Utilities.getDateByDays(-2);
    	
    	Calendar cc = Calendar.getInstance();
    	cc.setTime(YesterdayDate);
    	
    	int year = cc.get(Calendar.YEAR);
    	int month = cc.get(Calendar.MONTH);
       
        Date MonthToDateStartDate = Utilities.getStartDateByMonth(month, year);
        Date MonthToDateEndDate = Utilities.getEndDateByMonth(month, year);

        
    	Calendar LMcc = Calendar.getInstance();
    	LMcc.setTime(YesterdayDate);
    	LMcc.add(Calendar.MONTH, -1);
    	
    	int LMyear = LMcc.get(Calendar.YEAR);
    	int LMmonth = LMcc.get(Calendar.MONTH);
       
        Date LMMonthToDateStartDate = Utilities.getStartDateByMonth(LMmonth, LMyear);
        Date LMMonthToDateEndDate = Utilities.getEndDateByMonth(LMmonth, LMyear);
        
        
        Datasource ds = new Datasource();
        
    	try {
    	
    	  
   		  ds.createConnection();
   			
   		  Statement s = ds.createStatement();
   		  Statement s2 = ds.createStatement();

   		  
   		  double UniqueOutletsBilledDiscounted = 0;
   		  
   		  ResultSet rs0 = s.executeQuery("select count(distinct outlet_id) unique_outlets_billed from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between "+Utilities.getSQLDate(MonthToDateStartDate)+" and "+Utilities.getSQLDateNext(MonthToDateEndDate)+" and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (1804)) and outlet_id in (select outlet_id from sampling where active = 1)) and invoice_amount != 0");
   		  if(rs0.first()){
   			UniqueOutletsBilledDiscounted = rs0.getDouble("unique_outlets_billed");
   		  }
   		  
   		  
   		  
   		  double UniqueOutletsBilled = 0;
   		  
   		  ResultSet rs = s.executeQuery("select count(distinct outlet_id) unique_outlets_billed from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between "+Utilities.getSQLDate(MonthToDateStartDate)+" and "+Utilities.getSQLDateNext(MonthToDateEndDate)+" and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (1804))) and invoice_amount != 0");
   		  if(rs.first()){
   			UniqueOutletsBilled = rs.getDouble("unique_outlets_billed");
   		  }
   		  
   		  
   		  
   		  
   		  double UniqueOutletsPlanned = 0;
   		  ResultSet rs2 = s.executeQuery("SELECT count(distinct outlet_id) planned_universe FROM distributor_beat_plan_log where log_date between "+Utilities.getSQLDate(MonthToDateStartDate)+" and "+Utilities.getSQLDate(MonthToDateEndDate)+" and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (1804))");
   		  if(rs2.first()){
   			  UniqueOutletsPlanned = rs2.getDouble("planned_universe");
   		  }
   		  
   		  double BillProductivity = 0;
   		  if (UniqueOutletsPlanned != 0){
   			  BillProductivity =  (UniqueOutletsBilled / UniqueOutletsPlanned) * 100;
   		  }
   		  
   		  double TotalLinesSold = 0;
   		  ResultSet rs3 = s.executeQuery("select count(*) total_lines_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isap.is_promotion = 0 and isa.order_id in (select id from mobile_order where created_on between "+Utilities.getSQLDate(MonthToDateStartDate)+" and "+Utilities.getSQLDateNext(MonthToDateEndDate)+" and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (1804)))");
   		  if(rs3.first()){
   			  TotalLinesSold = rs3.getDouble("total_lines_sold");
   		  }
   		  
   		  double TotalInvoices = 0;
   		  ResultSet rs4 = s.executeQuery("select count(*) invoice_count from inventory_sales_adjusted where order_id in (select id from mobile_order where created_on between "+Utilities.getSQLDate(MonthToDateStartDate)+" and "+Utilities.getSQLDateNext(MonthToDateEndDate)+" and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (1804))) and invoice_amount != 0");
   		  if(rs4.first()){
   			TotalInvoices = rs4.getInt("invoice_count");
   		  }
   		  
   		  
   		  double SKUPerBill = 0;
   		  if (TotalInvoices != 0){
   			  SKUPerBill = TotalLinesSold / TotalInvoices;
   		  }
   		  
   		  
   		  double PackagesSold = 0;
   		  ResultSet rs5 = s.executeQuery("select count(package_count) package_total from (" +
				" select isap.id, count(*) package_count  from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isap.is_promotion = 0 and isa.order_id in (select id from mobile_order where created_on between "+Utilities.getSQLDate(MonthToDateStartDate)+" and "+Utilities.getSQLDateNext(MonthToDateEndDate)+" and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (1804))) group by isap.id,ipv.package_id " +
				" ) tb1");
   		  if(rs5.first()){
   			  PackagesSold = rs5.getDouble("package_total");
   		  }
   		  
   		  double QuantitySold = 0;
   		  ResultSet rs12 = s.executeQuery("select sum(isap.raw_cases) total_qty_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isap.is_promotion = 0 and isa.order_id in (select id from mobile_order where created_on between "+Utilities.getSQLDate(MonthToDateStartDate)+" and "+Utilities.getSQLDateNext(MonthToDateEndDate)+" and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (1804)))");
   		  if(rs12.first()){
   			  	QuantitySold = rs12.getDouble("total_qty_sold");
   		  }
   		  
		  double DropSize = 0;
		  if (TotalInvoices != 0){
		 	 DropSize = QuantitySold / TotalInvoices;
		  }
			
   		  double PackPerBill = 0;
   		  if (TotalInvoices != 0){
   			PackPerBill = PackagesSold / TotalInvoices;
   		  }
   		  
   		  double InactiveOutlets = 0;
   		  ResultSet rs6 = s.executeQuery("SELECT count(distinct outlet_id) FROM mobile_order where created_on between "+Utilities.getSQLDate(LMMonthToDateStartDate)+" and "+Utilities.getSQLDateNext(LMMonthToDateEndDate)+" and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (1804)) "+
   			"and outlet_id not in ("+
   			  "SELECT distinct outlet_id FROM mobile_order where created_on between "+Utilities.getSQLDate(MonthToDateStartDate)+" and "+Utilities.getSQLDateNext(MonthToDateEndDate)+
   			")");
   		  if (rs6.first()){
   			  InactiveOutlets = rs6.getDouble(1);
   		  }
   		  
   		  double OutletsWithChillersNotBeatPlans = 0;
   		  ResultSet rs7 = s.executeQuery("SELECT count(distinct outlet_id) FROM common_assets ca join common_outlets co on ca.outlet_id = co.id where ca.tot_status = 'INJECTED' and co.region_id in (2,4,5,7) and co.is_active = 1 and ca.outlet_id not in (select distinct dbpav.outlet_id from distributor_beat_plan_all_view dbpav join common_outlets coi on dbpav.outlet_id = coi.id where coi.region_id in (2,4,5,7))");
   		  if (rs7.first()){
   			OutletsWithChillersNotBeatPlans = rs7.getDouble(1);
   		  }
   		  
   		  double AdvancesApprovedAmount = 0;
   		  double AdvancesApprovedCount = 0;
   		  ResultSet rs8 = s.executeQuery("SELECT sum(s.advance_company_share), count(distinct s.outlet_id) FROM sampling s join common_outlets co on s.outlet_id = co.id where s.activated_on between "+Utilities.getSQLDate(MonthToDateStartDate)+" and "+Utilities.getSQLDateNext(MonthToDateEndDate)+" and co.region_id in (2,4,5,7) and s.advance_company_share != 0");
   		  if (rs8.first()){
   			  AdvancesApprovedAmount = rs8.getDouble(1);
   			  AdvancesApprovedCount = rs8.getDouble(2); 
   		  }
   		  
   		  double SamplingApprovedCount = 0;
   		  ResultSet rs9 = s.executeQuery("SELECT count(distinct s.outlet_id) FROM sampling s join common_outlets co on s.outlet_id = co.id where s.activated_on between "+Utilities.getSQLDate(MonthToDateStartDate)+" and "+Utilities.getSQLDateNext(MonthToDateEndDate)+" and co.region_id in (2,4,5,7)");
   		  if (rs9.first()){
   			SamplingApprovedCount = rs9.getDouble(1); 
   		  }
   		  
   		  
   		  
   		  double TOTInjected = 0;
   		  double TOTScrapped = 0;
   		  double TOTUplifted = 0;
   		  ResultSet rs10 = s.executeQuery("SELECT ca.tot_status,count(*) FROM common_assets ca join common_outlets co on ca.outlet_id = co.id where ca.movement_date_parsed between "+Utilities.getSQLDate(MonthToDateStartDate)+" and "+Utilities.getSQLDate(MonthToDateEndDate)+" and co.region_id in (2,4,5,7) group by ca.tot_status");
   		  while(rs10.next()){
   			  String status = rs10.getString(1);
   			  
   			  if (status.equals("INJECTED")){
   				  TOTInjected = rs10.getDouble(2);
   			  }
   			  if (status.equals("SCRAPPED")){
   				  TOTScrapped = rs10.getDouble(2);
   			  }
   			  if (status.equals("UPLIFTED")){
   				  TOTUplifted = rs10.getDouble(2);
   			  }
   			  
   		  }
   		  
   		  double TOTTotal = 0;
   		  ResultSet rs11 = s.executeQuery("SELECT count(*) FROM common_assets ca join common_outlets co on ca.outlet_id = co.id where co.region_id in (2,4,5,7) and ca.tot_status = 'INJECTED'");
   		  if (rs11.first()){
   			  TOTTotal = rs11.getDouble(1);
   		  }
   		  
   		  double DiscountedOutletsPercentage = 0;
   		  if (UniqueOutletsBilled != 0){
   			DiscountedOutletsPercentage = (UniqueOutletsBilledDiscounted / UniqueOutletsBilled) * 100;
   		  }
   		  
   		  double OutletsBilledPercentage = 0;
   		  if (UniqueOutletsPlanned != 0){
   			OutletsBilledPercentage = (UniqueOutletsBilled / UniqueOutletsPlanned) * 100;
   		  }
   		  
   		  double TOTMarkedScrapped = 0;
   		  ResultSet rs13 = s.executeQuery("select count(*) from common_assets_tot_log catl join common_outlets co on catl.outlet_id = co.id where catl.created_on between '2014-11-11' and "+Utilities.getSQLDate(MonthToDateEndDate)+" and catl.created_on between "+Utilities.getSQLDate(MonthToDateStartDate)+" and "+Utilities.getSQLDate(MonthToDateEndDate)+" and catl.tot_status = 'SCRAPPED' and co.region_id in (2,4,5,7)");
   		  if (rs13.first()){
   			TOTMarkedScrapped = rs13.getDouble(1);
   		  }
   		  
   		  
   		  
   		  
          String messageWA = "-----------------------------\nMonthly KPIs (Aamir Aftab)\n"+Utilities.getDisplayDateFullMonthYearFormat(YesterdayDate)+"\n-----------------------------\n\n";
          
          messageWA += "Billing:\n";
          messageWA += "\tOutlets in Beat Plans:\t"+Utilities.getDisplayCurrencyFormatRounded(UniqueOutletsPlanned)+"\n";
          messageWA += "\tOutlets Billed:\t"+Utilities.getDisplayCurrencyFormatRounded(UniqueOutletsBilled)+" ("+Math.round(OutletsBilledPercentage)+"%)\n";
          messageWA += "\tDiscounted Outlets:\t"+Utilities.getDisplayCurrencyFormatRounded(UniqueOutletsBilledDiscounted)+" ("+Math.round(DiscountedOutletsPercentage)+"%) \n";
          messageWA += "\n";
          messageWA += "Productivity:\n";
          messageWA += "\tBill Productivity:\t"+Math.round(BillProductivity)+"%\n";
          messageWA += "\tSKU/Bill:\t"+Utilities.getDisplayCurrencyFormatTwoDecimalFixed(SKUPerBill)+"\n";
          messageWA += "\tDrop Size:\t"+Utilities.getDisplayCurrencyFormatTwoDecimalFixed(DropSize)+"\n";
          messageWA += "\tPack/Bill:\t"+Utilities.getDisplayCurrencyFormatTwoDecimalFixed(PackPerBill)+"\n";
          messageWA += "\n";
          messageWA += "TOT Activity:\n";
          messageWA += "\tInjected: "+Math.round(TOTInjected)+"\n";
          messageWA += "\tUplifted: "+Math.round(TOTUplifted)+"\n";
          messageWA += "\tScrapped: "+Math.round(TOTScrapped)+"\n";
          messageWA += "\tInjected as on today: "+Utilities.getDisplayCurrencyFormatRounded(TOTTotal)+"\n";
          messageWA += "\n";
          messageWA += Utilities.getDisplayCurrencyFormatRounded(TOTMarkedScrapped)+" TOT were marked as Scrapped backdated during the month of "+Utilities.getDisplayDateFullMonthYearFormat(YesterdayDate)+".\n\n";
          messageWA += Utilities.getDisplayCurrencyFormatRounded(OutletsWithChillersNotBeatPlans)+" outlets with active chillers are not in Beat Plan of any Order Booker\n";
          messageWA += "\n";
          messageWA += "Rs. "+Utilities.getDisplayCurrencyFormatRounded(AdvancesApprovedAmount)+" of advances were approved for "+Math.round(AdvancesApprovedCount)+" outlets\n";
          messageWA += "\n";
          messageWA += "Sampling requests of "+Math.round(SamplingApprovedCount)+" outlets were approved\n";
          messageWA += "\n";
          messageWA += ""+Math.round(InactiveOutlets)+" active outlets of "+Utilities.getDisplayDateFullMonthYearFormat(LMMonthToDateStartDate)+" did not place order in "+Utilities.getDisplayDateFullMonthYearFormat(YesterdayDate)+".\n\n";
          messageWA += "Formulas:\n";
          messageWA += "\tBill Productivity:\n\t\tUnique Outlets Billed / Planned\n";
          messageWA += "\tSKU/Bill:\n\t\tTotal Lines Sold/Total Invoices\n";
          messageWA += "\tDrop Size:\n\t\tTotal Quantity Sold/Total Invoices\n";
          messageWA += "\tPack/Bill:\n\t\tTotal Packs Sold/Total Invoices\n";
          
          
          //Utilities.sendWhatsApp("923444471200", messageWA, null); // Anas Wahab
          
          Utilities.sendWhatsApp("923008406444-1408475526",messageWA, null); // Portal Admin
          
          Utilities.sendWhatsApp("923334566993-1406051187", messageWA, null); // Amir Aftab
          
          Utilities.sendWhatsApp("923018430696", messageWA, null); // Atiq Baloch
          
          Utilities.sendWhatsApp("923444471064", messageWA, null); // Jazeb
          
          //Utilities.sendWhatsApp("923458468658", messageWA, null); // Omer Farooq Khan
          
          
          s.close();
          
       }catch(Exception e)
       {
          e.printStackTrace();
       }finally{
    	   ds.dropConnection();
       }
    	
    	
    }
    

}
