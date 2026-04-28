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
public class S210SecondaryUpdateHourly extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public S210SecondaryUpdateHourly() {
        super();
    }

    
    public static void main(String [] args) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
    	Date YesterdayDate = Utilities.getDateByDays(0); 
    	
    	Calendar cc = Calendar.getInstance();
    	cc.setTime(YesterdayDate);
    	int year = cc.get(Calendar.YEAR);
    	int month = cc.get(Calendar.MONTH);
       
        Date MonthToDateStartDate = Utilities.getStartDateByMonth(month, year);
        
    	try {
    	
    	   Datasource ds = new Datasource();
   		   ds.createConnectionToReplica();
   			
   		   Statement s = ds.createStatement();
          
          //Converted Cases Today
          double CasesToday = 0;
          ResultSet rs2 = s.executeQuery("select sum(mop.total_units * ipv.liquid_in_ml)/6000 converted_cases from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mo.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3845) or snd_id in (2646)) and mo.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs2.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
        	  CasesToday = rs2.getDouble(1);
          }
          
          //Converted Cases Today
          double CasesTodaySD3 = 0;
          //ResultSet rs2SD3 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (2252)) and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          ResultSet rs2SD3 = s.executeQuery("select sum(mop.total_units * ipv.liquid_in_ml)/6000 converted_cases from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mo.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3644)) and mo.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs2SD3.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
        	  CasesTodaySD3 = rs2SD3.getDouble(1);
          }
          
          //Converted Cases Today
          double CasesTodaySD2 = 0;
          //ResultSet rs2SD2 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3498)) and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          ResultSet rs2SD2 = s.executeQuery("select sum(mop.total_units * ipv.liquid_in_ml)/6000 converted_cases from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mo.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3498)) and mo.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs2SD2.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
        	  CasesTodaySD2 = rs2SD2.getDouble(1);
          }

          //Converted Cases Today
          double CasesTodaySD4 = 0;
          //ResultSet rs2SD3 = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (2252)) and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          ResultSet rs2SD4 = s.executeQuery("select sum(mop.total_units * ipv.liquid_in_ml)/6000 converted_cases from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mo.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3645)) and mo.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs2SD4.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
        	  CasesTodaySD4 = rs2SD4.getDouble(1);
          }

          
          String messageWA = "------------------------------\nSecondary Orders\n------------------------------\n\nToday\n";
          
          messageWA += "\tOmer Qayyum: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesToday)+"\n";
          messageWA += "\tJawad Akram: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesTodaySD2)+"\n";
          messageWA += "\tKhurram Jaffar: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesTodaySD3)+"\n";
          messageWA += "\tMuhammad Ajmal: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesTodaySD4)+"\n";
          messageWA += "\tTotal: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesToday+CasesTodaySD3+CasesTodaySD2+CasesTodaySD4)+"\n"+"\n\n"+Utilities.getDisplayDateTimeFormat(new Date());
          
          /*
          messageWA += "\nMTD, "+Utilities.getDisplayDateFullMonthYearFormat(YesterdayDate)+":\n";
          messageWA += "\tSaeed Abbas: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTD)+"\n";
          messageWA += "\tJawad Akram: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTDSD2)+"\n";
          messageWA += "\tArshad Mehboob: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTDSD3)+"\n";
          messageWA += "\tTotal: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(TotalMTD)+"\n\n"+Utilities.getDisplayDateTimeFormat(new Date());
		  */
          
          /*
          String messageWAC = "---------------------------\nCash\n---------------------------\n\nToday\n";
          messageWAC += "\tSaeed Abbas: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashToday)+"\n";
          messageWAC += "\tJawad Akram: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashTodaySD2)+"\n";
          messageWAC += "\tArshad Mehboob: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashTodaySD3)+"\n";
          messageWAC += "\tTotal: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashToday+CashTodaySD3+CashTodaySD2)+"\n";
          
          messageWAC += "\nMTD, "+Utilities.getDisplayDateFullMonthYearFormat(YesterdayDate)+":\n";
          messageWAC += "\tSaeed Abbas: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashMTD)+"\n";
          messageWAC += "\tJawad Akram: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashMTDSD2)+"\n";
          messageWAC += "\tArshad Mehboob: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashMTDSD3)+"\n";
          messageWAC += "\tTotal: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(TotalMTDCash)+"\n\n"+Utilities.getDisplayDateTimeFormat(new Date());
          */          
          
          
          Utilities.sendTelegram("-134060758", messageWA+"\n", null);
          Utilities.sendTelegram("-114584068", messageWA+"\n", null);
          Utilities.sendTelegram("-125339765", messageWA, null);
          
          //Utilities.sendTelegram("-105413386", messageWA+"\n", null);
          //Utilities.sendTelegram("-105413386", , null);
          
          
       }catch(Exception e)
       {
          e.printStackTrace();
       }
    }
    

}
