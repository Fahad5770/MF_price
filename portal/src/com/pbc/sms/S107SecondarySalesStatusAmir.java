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
public class S107SecondarySalesStatusAmir extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public S107SecondarySalesStatusAmir() {
        super();
    }

    
    public static void main(String [] args) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
    	Date TodayDate = new Date();
    	
    	Calendar cc = Calendar.getInstance();
    	int year = cc.get(Calendar.YEAR);
    	int month = cc.get(Calendar.MONTH);
       
        Date MonthToDateStartDate = Utilities.getStartDateByMonth(month, year);
        Date MonthToDateEndDate = new Date();
        Date YesterdayDate = Utilities.getDateByDays(-1);
        /*
        System.out.println( "TodayDate = "+Utilities.getSQLDate(TodayDate) );
        System.out.println( "NextDate = "+Utilities.getSQLDateNext(TodayDate) );
        System.out.println( "MonthToDateStartDate = "+Utilities.getSQLDate(MonthToDateStartDate) );
        System.out.println( "MonthToDateEndDate = "+Utilities.getSQLDate(MonthToDateEndDate) );
        */
        Datasource ds = new Datasource();
        
    	try {
    	
    	  
   		  ds.createConnection();
   			
   		  Statement s = ds.createStatement();
   		  
   		  int PlannedVisits = 0;
   		  ResultSet rs = s.executeQuery("SELECT count(distinct outlet_id) FROM distributor_beat_plan_view where distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (1804)) and day_number = dayofweek(curdate())");
   		  if (rs.first()){
   			  PlannedVisits = rs.getInt(1);
   		  }
   		  
   		  double OrdersAmount = 0;
   		  ResultSet rs2 = s.executeQuery("select sum(net_amount) from mobile_order where distributor_id in ((SELECT distributor_id FROM common_distributors where snd_id in (1804))) and created_on between "+Utilities.getSQLDate(TodayDate)+" and "+Utilities.getSQLDateNext(TodayDate));
   		  if (rs2.first()){
   			  OrdersAmount = rs2.getDouble(1);
   		  }
   		  
   		  int CRsWorked = 0;
   		  ResultSet rs3 = s.executeQuery("select count(distinct created_by) from mobile_order where distributor_id in ((SELECT distributor_id FROM common_distributors where snd_id in (1804))) and created_on between "+Utilities.getSQLDate(TodayDate)+" and "+Utilities.getSQLDateNext(TodayDate));
   		  if (rs3.first()){
   			  CRsWorked = rs3.getInt(1);
   		  }
   		  
   		  int CRsAssigned = 0;
   		  ResultSet rs4 = s.executeQuery("SELECT count(distinct dbpv.assigned_to) FROM distributor_beat_plan_view dbpv join users u on dbpv.assigned_to = u.id where u.is_active = 1 and dbpv.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (1804)) and dbpv.day_number = dayofweek(curdate())");
   		  if (rs4.first()){
   			  CRsAssigned = rs4.getInt(1);
   		  }
   		  
   		  int OutletsOrdered = 0;
   		  ResultSet rs5 = s.executeQuery("select count(distinct outlet_id) from mobile_order where distributor_id in ((SELECT distributor_id FROM common_distributors where snd_id in (1804))) and created_on between "+Utilities.getSQLDate(TodayDate)+" and "+Utilities.getSQLDateNext(TodayDate));
   		  if (rs5.first()){
   			OutletsOrdered = rs5.getInt(1);
   		  }
   		  
   		  
   		  double DiscountedOrderAmount = 0;
   		  double ProjectedSampling = 0;
   		  double DiscountedPercentage = 0;
   		  
   		  ResultSet rs6 = s.executeQuery("select sum(product_amount), sum(qty*discounted) discount from ("+
				"select mo.distributor_id, mo.outlet_id, ipv.brand_id, sum(mop.raw_cases) qty, sum(mop.net_amount) product_amount, ("+
				"select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where date("+Utilities.getSQLDate(TodayDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(TodayDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id  and sp.brand_id = ipv.brand_id and sp.brand_id != 0 and s.outlet_id = mo.outlet_id"+
				" union all "+
				"select sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id join inventory_products ip on sp.package = ip.package_id and ip.category_id = 1 where date("+Utilities.getSQLDate(TodayDate)+") between s.activated_on and s.deactivated_on and date("+Utilities.getSQLDate(TodayDate)+") between sp.valid_from and sp.valid_to and sp.package = ipv.package_id and sp.brand_id = 0 and sp.brand_id = ipv.brand_id and s.outlet_id = mo.outlet_id"+
				 ") discounted from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mo.distributor_id in ((SELECT distributor_id FROM common_distributors where snd_id in (1804))) and mo.created_on between "+Utilities.getSQLDate(TodayDate)+" and "+Utilities.getSQLDateNext(TodayDate)+" and mop.is_promotion = 0 group by mo.outlet_id, ipv.brand_id having discounted is not null"+
   			") tab1");
   		  if (rs6.first()){
   			  DiscountedOrderAmount = rs6.getDouble(1);
   			  ProjectedSampling = rs6.getDouble(2);
   		  }
   		  if (OrdersAmount > 0){
   			  DiscountedPercentage = (DiscountedOrderAmount / OrdersAmount) * 100;
   		  }
   		  
   		  double PromotionAmount = 0;
   		  ResultSet rs7 = s.executeQuery("select sum(mop.total_amount) from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mo.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (1804)) and mo.created_on between "+Utilities.getSQLDate(TodayDate)+" and "+Utilities.getSQLDateNext(TodayDate)+" and mop.is_promotion = 1");
   		  if (rs7.first()){
   			  PromotionAmount = rs7.getDouble(1);
   		  }

   		  double PromotionAmountDeskSales = 0;
   		  ResultSet rs9 = s.executeQuery("select sum(isap.total_amount) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (1804)) and isa.created_on between "+Utilities.getSQLDate(TodayDate)+" and "+Utilities.getSQLDateNext(TodayDate)+" and isap.is_promotion = 1 and isa.type_id = 1");
   		  if (rs9.first()){
   			PromotionAmountDeskSales = rs9.getDouble(1);
   		  }
   		  
   		  PromotionAmount = PromotionAmount + PromotionAmountDeskSales;
   		  
   		  
   		  double DeskSales = 0;
   		  ResultSet rs8 = s.executeQuery("select sum(net_amount) from inventory_sales_invoices where distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (1804)) and created_on between "+Utilities.getSQLDate(TodayDate)+" and "+Utilities.getSQLDateNext(TodayDate)+" and type_id = 1");
   		  if (rs8.first()){
   			  DeskSales = rs8.getDouble(1);
   		  }
   		  
   		  double SpotSelling = 0;
   		  ResultSet rs10 = s.executeQuery("select sum(net_amount) from inventory_sales_invoices where distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (1804)) and created_on between "+Utilities.getSQLDate(TodayDate)+" and "+Utilities.getSQLDateNext(TodayDate)+" and type_id = 2");
   		  if (rs10.first()){
   			SpotSelling = rs10.getDouble(1);
   		  }
   		  
		  double BackorderAmount = 0;
		  ResultSet rs11 = s.executeQuery("select mopb.product_id, ipv.package_label, ipv.brand_label, sum(mopb.total_units) units, sum(if(mop.is_promotion = 1,0,mopb.total_units * mop.rate_units)) amount, sum(mopb.total_units/ipv.unit_per_sku) order_total, ipv.unit_per_sku, count(distinct mopb.id) orders_count from mobile_order_products mop join mobile_order_products_backorder mopb on mop.id = mopb.id and mop.product_id = mopb.product_id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mopb.id in ("+
				  "select id from mobile_order where backordered_on between "+Utilities.getSQLDate(TodayDate)+" and "+Utilities.getSQLDateNext(TodayDate)+" and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (1804)) "+
				  ")");
		  if(rs11.first()){
			  BackorderAmount = rs11.getDouble("amount");
		  }
   		  
   		  
          String messageWA = "-----------------------------\nSales Force Activity\n"+Utilities.getDisplayFullDateFormat(TodayDate)+"\n-----------------------------\n\n";
          messageWA += "CRs Worked:\t "+CRsWorked+" of "+CRsAssigned+" assigned\n";
          messageWA += "Planned Visits:\t "+PlannedVisits+" outlets\n";
          messageWA += "Orders Taken:\t "+OutletsOrdered+" outlets\n\n";
          
          messageWA += "Orders Amount:\t "+Utilities.getDisplayCurrencyFormatRounded(OrdersAmount)+"\n";
          
          messageWA += "Backorders Amount:\t"+Utilities.getDisplayCurrencyFormatRounded(BackorderAmount)+"\n";
          
          messageWA += "\nDiscounted Items:\t "+Math.round(DiscountedPercentage)+"% ("+Utilities.getDisplayCurrencyFormatRounded(DiscountedOrderAmount)+")\n";
          messageWA += "Projected Sampling:\t "+Utilities.getDisplayCurrencyFormatRounded(ProjectedSampling)+"\n";
          messageWA += "Value of Promotion Items:\t"+Utilities.getDisplayCurrencyFormatRounded(PromotionAmount)+"\n";
          
          messageWA += "\nDesk Sales:\t"+Utilities.getDisplayCurrencyFormatRounded(DeskSales)+"\n";
          if (SpotSelling > 0){
        	  messageWA += "Spot Selling:\t"+Utilities.getDisplayCurrencyFormatRounded(SpotSelling)+"\n";
          }
          
          
          
          
          
          
          
          
          
          
          
          // Amir Aftab
          Utilities.sendWhatsApp("923334566993-1406051187", messageWA, null);
			
          Utilities.sendWhatsApp("923008406444-1408475526", "Amir Aftab\n\n"+messageWA, null); // Anas Wahab
          
          
          
          
          
          
          //Utilities.sendWhatsApp("923444471200", messageWA, null); // Anas Wahab
          
			
          s.close();
          
       }catch(Exception e)
       {
          e.printStackTrace();
       }finally{
    	   ds.dropConnection();
       }
    	
    	
    }
    

}
