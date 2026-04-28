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
import com.pbc.employee.EmployeeHierarchy;
import com.pbc.util.Datasource;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.net.*;
import java.io.*;
public class S110BackorderStatsSD3 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public S110BackorderStatsSD3() {
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
        
        long SNDID = EmployeeHierarchy.getSDHead(4).USER_ID;
        
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
   		  Statement s2 = ds.createStatement();
   		  
   		  
  		  	String messageWA = "-----------------------------\nBackorder Statistics\n"+Utilities.getDisplayFullDateFormat(TodayDate)+"\n-----------------------------\n";
          
          
          	double TotalAmount = 0;
			ResultSet rs3 = s.executeQuery("select mopb.product_id, ipv.package_label, ipv.brand_label, sum(mopb.total_units) units, sum(if(mop.is_promotion = 1,0,mopb.total_units * mop.rate_units)) amount, sum(mopb.total_units/ipv.unit_per_sku) order_total, ipv.unit_per_sku, count(distinct mopb.id) orders_count from mobile_order_products mop join mobile_order_products_backorder mopb on mop.id = mopb.id and mop.product_id = mopb.product_id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mopb.id in ("+
					"select id from mobile_order where backordered_on between "+Utilities.getSQLDate(TodayDate)+" and "+Utilities.getSQLDateNext(TodayDate)+" and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+SNDID+")) "+
					")");
			if(rs3.first()){
				TotalAmount = rs3.getDouble("amount");
			}
			
			messageWA += "Total Amount: "+Utilities.getDisplayCurrencyFormatRounded(TotalAmount)+"\n";
			

			
			messageWA += "\nTop 5 Products\n-----------------------------";
			
          //ResultSet rs = s.executeQuery("SELECT mopb.product_id, ipv.package_label, ipv.brand_label, sum(mopb.total_units) total_units,ipv.unit_per_sku, sum(mopb.total_units/ipv.unit_per_sku) order_total, count(distinct mo.id) orders FROM mobile_order mo join mobile_order_products_backorder mopb on mo.id = mopb.id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mo.backordered_on between "+Utilities.getSQLDate(TodayDate)+" and "+Utilities.getSQLDateNext(TodayDate)+" group by mopb.product_id order by order_total desc limit 10");
			ResultSet rs = s.executeQuery("select mopb.product_id, ipv.package_label, ipv.brand_label, sum(mopb.total_units) units, sum(if(mop.is_promotion = 1,0,mopb.total_units * mop.rate_units)) amount, sum(mopb.total_units/ipv.unit_per_sku) order_total, ipv.unit_per_sku, count(distinct mopb.id) orders_count from mobile_order_products mop join mobile_order_products_backorder mopb on mop.id = mopb.id and mop.product_id = mopb.product_id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mopb.id in ("+
					"select id from mobile_order where backordered_on between "+Utilities.getSQLDate(TodayDate)+" and "+Utilities.getSQLDateNext(TodayDate)+" and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+SNDID+")) "+
					") group by mopb.product_id order by order_total desc limit 5");
          
          
          while(rs.next()){
        	  if (Math.round(rs.getDouble("order_total")) > 0){
              messageWA += "\n"+rs.getString(2)+" - "+rs.getString(3)+"\n";
              messageWA += "\tCases: "+Math.round(rs.getDouble("order_total"))+"\n";
              messageWA += "\tBackorders: "+Math.round(rs.getDouble("orders_count"))+"\n";
              messageWA += "\tDistributors: (Cases)\n";
              
              //ResultSet rs2 = s2.executeQuery("SELECT mo.distributor_id, (select name from common_distributors where distributor_id = mo.distributor_id) distributor_name, mopb.product_id, ipv.package_label, ipv.brand_label, sum(mopb.total_units) total_units,ipv.unit_per_sku, sum(mopb.total_units/ipv.unit_per_sku) order_total, count(distinct mo.id) FROM mobile_order mo join mobile_order_products_backorder mopb on mo.id = mopb.id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mo.backordered_on between "+Utilities.getSQLDate(TodayDate)+" and "+Utilities.getSQLDateNext(TodayDate)+" and mopb.product_id = "+rs.getInt(1)+" group by mo.distributor_id, mopb.product_id order by order_total desc");
  			ResultSet rs2 = s2.executeQuery("select mo.distributor_id, (select name from common_distributors where distributor_id = mo.distributor_id) distributor_name, mopb.product_id, ipv.package_label, ipv.brand_label, sum(mopb.total_units) units, sum(if(mop.is_promotion = 1,0,mopb.total_units * mop.rate_units)) amount, sum(mopb.total_units/ipv.unit_per_sku) order_total, ipv.unit_per_sku, count(distinct mopb.id) orders_count from mobile_order_products mop join mobile_order mo on mo.id = mop.id join mobile_order_products_backorder mopb on mop.id = mopb.id and mop.product_id = mopb.product_id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mopb.id in ("+
					"select id from mobile_order where backordered_on between "+Utilities.getSQLDate(TodayDate)+" and "+Utilities.getSQLDateNext(TodayDate)+" and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+SNDID+")) "+
					") and mopb.product_id = "+rs.getInt(1)+" group by mo.distributor_id order by order_total desc");
              while(rs2.next()){
            	  double cases = rs2.getDouble("order_total");
            	  if (cases > 0){
	            	  if (cases >= 1){
	            		  messageWA += "\t"+Utilities.truncateStringToMax(rs2.getString(2), 15) +"\t "+Math.round(cases)+"\n";
	            	  }else if (cases < 1){
	            		  messageWA += "\t"+Utilities.truncateStringToMax(rs2.getString(2), 15) +"\t <1\n";
	            	  }
            	  }
              }
        	  }
          }
          
          
          
          
          
          
          //Utilities.sendWhatsApp("923444471200", messageWA, null); // Anas Wahab
          //786
          
          if (TotalAmount > 50000){
          //786
          Utilities.sendWhatsApp("923008406444-1408475526", messageWA, null); // Anas Wahab
          
          
          Utilities.sendWhatsApp("923334566993-1436339854", messageWA, null); // SD3
          
          }
          
          //Utilities.sendWhatsApp("923444471426", messageWA, null); // Bilal
          //Utilities.sendWhatsApp("923334566993", messageWA, null); // Shahrukh
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
