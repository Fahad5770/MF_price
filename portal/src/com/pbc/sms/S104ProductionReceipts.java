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
public class S104ProductionReceipts extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public S104ProductionReceipts() {
        super();
    }

    
    public static void main(String [] args) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
    	Date StartDate = new Date();
    	
    	Calendar cc = Calendar.getInstance();
    	int year = cc.get(Calendar.YEAR);
    	int month = cc.get(Calendar.MONTH);
       
        Date MonthToDateStartDate = Utilities.getStartDateByMonth(month, year);
        Date MonthToDateEndDate = new Date();
        Date YesterdayDate = Utilities.getDateByDays(-1); 
        
        double ConvertedCases = 0;
        
    	try {
    	
    		
    	  Datasource ds = new Datasource();
   		  ds.createConnection();
   			
   		  Statement s = ds.createStatement();
   		  Statement s2 = ds.createStatement();
          
   		  String WAMessage = "-------------------------\nProduction Summary\n-------------------------\n\n"+Utilities.getDisplayFullDateFormat(YesterdayDate)+"";
   		  
          String Sql1 = "select ip.category_id,ip.package_id,ipa.label,ipa.unit_per_case,sum(iprp.total_units) bottles,sum(iprp.liquid_in_ml) / ipa.conversion_rate_in_ml converted_cases from inventory_production_receipts ipr, inventory_production_receipts_products iprp, inventory_products ip, inventory_packages ipa, inventory_brands ib where ipr.id = iprp.id and iprp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and ipr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and ip.category_id = 1 group by ip.category_id, ip.package_id order by ip.category_id, ipa.sort_order";
          ResultSet rs1 = s.executeQuery(Sql1);
          while(rs1.next()){
          	
        	ConvertedCases += rs1.getDouble("converted_cases");
        	  
			String PackageLabel = rs1.getString("ipa.label");
			String PackageRawCases = Utilities.convertToRawCases(rs1.getLong("bottles"), rs1.getInt("ipa.unit_per_case"));
			
			WAMessage += "\n\n" +PackageLabel +"\t "+PackageRawCases+"";
			
			String Sql = "select ip.category_id, ip.package_id, ipa.label, ip.brand_id, ib.label, ip.unit_per_sku, sum(iprp.total_units) bottles, sum(iprp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_production_receipts ipr, inventory_production_receipts_products iprp, inventory_products ip, inventory_packages ipa, inventory_brands ib where ipr.id = iprp.id and iprp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id  and ipr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and ip.category_id = 1  and ip.package_id="+rs1.getInt("ip.package_id")+"  group by ip.category_id, ip.package_id, ip.brand_id order by ip.category_id, ip.package_id, ip.brand_id";
			ResultSet rs2 = s2.executeQuery(Sql);
			while( rs2.next() ){
              	
				String BrandLabel = rs2.getString("ib.label");
  				String BrandRawCases = Utilities.convertToRawCases( rs2.getLong("bottles"), rs2.getInt("ip.unit_per_sku"));
  				
  				WAMessage += "\n\t" +BrandLabel +"\t "+BrandRawCases;
  				
              }
             
          }
         
          WAMessage += "\n\nTotal Converted\t "+ Utilities.getDisplayCurrencyFormatRounded(ConvertedCases)+"";
          
          
          // WhatsApp Messages
          /*
          Utilities.sendWhatsApp("923008406444-1408475526", WAMessage, null); // Anas Wahab
          Utilities.sendWhatsApp("923458468658", WAMessage, null); // Omer Farooq Khan
          Utilities.sendWhatsApp("923018430696", WAMessage, null); // Atiq Baloch
          Utilities.sendWhatsApp("923444002777", WAMessage, null); // Imran Hashim
          Utilities.sendWhatsApp("923452004454", WAMessage, null); // AAA
          Utilities.sendWhatsApp("923444471000", WAMessage, null); // Shafqat Riaz (Production Head)
          Utilities.sendWhatsApp("923444471064", WAMessage, null); // Jazeb
          */
		  Utilities.sendTelegram("-134060758", WAMessage, null); // Executive Summary Group
		  Utilities.sendTelegram("-121808227", WAMessage, null); // Daily Summary Group
		  Utilities.sendTelegram("-128535487", WAMessage, null); // Production Group
          /*
          Utilities.sendSMS("923458468658", WAMessage); // Omer Farooq Khan
          Thread.currentThread().sleep(10000);
          Utilities.sendSMS("923018430696", WAMessage); // Atiq Baloch
          Thread.currentThread().sleep(10000);
          Utilities.sendSMS("923444002777", WAMessage); // Imran Hashim
          Thread.currentThread().sleep(10000);
          Utilities.sendSMS("923452004454", WAMessage); // AAA
          Thread.currentThread().sleep(10000);
          Utilities.sendSMS("923444471000", WAMessage); // Shafqat Riaz (Production Head)
          Thread.currentThread().sleep(10000);
          Utilities.sendSMS("923444471064", WAMessage); // Jazeb
          Thread.currentThread().sleep(10000);
          Utilities.sendSMS("923444471200", WAMessage); // Anas
          Thread.currentThread().sleep(10000);
          */
       }catch(Exception e)
       {
          e.printStackTrace();
       }
    }
    

}
