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
public class S210SecondaryUpdateUMHourly extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public S210SecondaryUpdateUMHourly() {
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
   		   Statement s2 = ds.createStatement();
   		   
   		   
   		   
   		   /*
          //Converted Cases Today
          double CasesToday = 0;
          ResultSet rs2 = s.executeQuery("select sum(mop.total_units * ipv.liquid_in_ml)/6000 converted_cases from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mo.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3845) or snd_id in (2646)) and mo.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs2.first()){        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
        	  CasesToday = rs2.getDouble(1);
          }
          */
          
   		  for (int i = 1; i<= 4; i++){
   			  
   			  if (i != 3){
   		   
   				  String SDLabel = "";
   				  if (i == 1){
   					SDLabel = "SD1";
   				  }
   				  if (i == 2){
   					SDLabel = "SD2";
   				  }
   				  if (i == 4){
   					SDLabel = "SD3";
   				  }
   				  
   				  
   				  
   		  double CasesTodayTotalSD1 = 0;
   		   
          String messageWA = "------------------------------\nSecondary Orders - "+SDLabel+"\n------------------------------\n\nToday\n";
          
   		  ResultSet rs3 = s.executeQuery("select distinct tdm_id, (select display_name from users where id = tdm_id) from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(i).USER_ID+" and tdm_id is not null");
   		  while(rs3.next()){
   			  long TDMID = rs3.getLong(1);
   			  String TDMName = rs3.getString(2);
   			  
   	          //Converted Cases Today
   	          double CasesToday = 0;
   	          ResultSet rs2 = s2.executeQuery("select sum(mop.total_units * ipv.liquid_in_ml)/6000 converted_cases from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mo.distributor_id in (SELECT distributor_id FROM common_distributors where tdm_id = "+TDMID+") and mo.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
   	          if(rs2.first()){        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
   	        	  CasesToday = rs2.getDouble(1);
   	          }
   	          CasesTodayTotalSD1 += CasesToday;
   			  
   	          messageWA += "\t"+TDMName+": "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesToday)+"\n";
   		  }
          messageWA += "\tTotal: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesTodayTotalSD1)+"\n"+"\n\n"+Utilities.getDisplayDateTimeFormat(new Date());
          
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
          
          /*
          Utilities.sendTelegram("-134060758", messageWA+"\n", null);
          Utilities.sendTelegram("-114584068", messageWA+"\n", null);
          Utilities.sendTelegram("-125339765", messageWA, null);
          */
          
          String group = "";
          if (i == 1){
        	  group = "-156810637";
          }
          if (i == 2){
        	  group = "-192478110";
          }
          if (i == 4){
        	  group = "-197596789";
          }
          Utilities.sendTelegram(group, messageWA+"\n", null);
          //Utilities.sendTelegram("-105413386", , null);
   			  }
   		  }
          
       }catch(Exception e)
       {
          e.printStackTrace();
       }
    }
    

}
