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
public class S105ComplaintStatusAmir extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public S105ComplaintStatusAmir() {
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
   		  
          String TodayMessage =  "Complaint Status\n\n"+Utilities.getDisplayFullDateFormat(TodayDate)+"\n";
          
          String Sql1 = "select ( SELECT count(id) FROM crm_tasks ct where region_id in (5, 2, 4, 7) and created_on between "+Utilities.getSQLDate(TodayDate)+" and "+Utilities.getSQLDateNext(TodayDate)+" and ifnull((select '1' from crm_tasks_list ctl where ctl.id = ct.id and ((ctl.type_id >= 1 and ctl.type_id <= 14) or (ctl.type_id >= 21 and ctl.type_id <= 24)) limit 1),0) = 1 ) complaint_registered, ( SELECT count(id)  FROM crm_tasks ct where region_id in (5, 2, 4, 7) and resolved_on between "+Utilities.getSQLDate(TodayDate)+" and "+Utilities.getSQLDateNext(TodayDate)+" and ifnull((select '1' from crm_tasks_list ctl where ctl.id = ct.id and ((ctl.type_id >= 1 and ctl.type_id <= 14) or (ctl.type_id >= 21 and ctl.type_id <= 24)) limit 1),0) = 1 ) complaint_resolved";
          ResultSet rs1 = s.executeQuery(Sql1);
          while(rs1.next()){
        	  
        	  int ComplaintRegistered = rs1.getInt("complaint_registered"); 
        	  int ComplaintResolved = rs1.getInt("complaint_resolved"); 
        	  
        	  TodayMessage += "\tRegistered : "+ComplaintRegistered+" \n";
        	  TodayMessage += "\tResolved : "+ComplaintResolved+" \n";
          }
          
          

          String MonthToDateMessage =  "\nTotal\n";
          
          
          String Sql2 = "select ( SELECT count(id) FROM crm_tasks ct where region_id in (5, 2, 4, 7) and created_on between '2015-03-14' and "+Utilities.getSQLDateNext(TodayDate)+" and ifnull((select '1' from crm_tasks_list ctl where ctl.id = ct.id and ((ctl.type_id >= 1 and ctl.type_id <= 14) or (ctl.type_id >= 21 and ctl.type_id <= 24)) limit 1),0) = 1 ) complaint_registered, ( SELECT count(id)  FROM crm_tasks ct where region_id in (5, 2, 4, 7) and resolved_on between '2015-03-14' and "+Utilities.getSQLDateNext(TodayDate)+" and ifnull((select '1' from crm_tasks_list ctl where ctl.id = ct.id and ((ctl.type_id >= 1 and ctl.type_id <= 14) or (ctl.type_id >= 21 and ctl.type_id <= 24)) limit 1),0) = 1 ) complaint_resolved, ( SELECT count(id) FROM crm_tasks ct where region_id in (5, 2, 4, 7) and created_on between '2015-03-14' and "+Utilities.getSQLDateNext(TodayDate)+" and is_resolved=0  and ifnull((select '1' from crm_tasks_list ctl where ctl.id = ct.id and ((ctl.type_id >= 1 and ctl.type_id <= 14) or (ctl.type_id >= 21 and ctl.type_id <= 24)) limit 1),0) = 1) complaint_pending ";
          //System.out.println(Sql2);
          
          ResultSet rs2 = s.executeQuery(Sql2);
          while(rs2.next()){
        	  
        	  int ComplaintRegistered = rs2.getInt("complaint_registered"); 
        	  int ComplaintResolved = rs2.getInt("complaint_resolved");
        	  int ComplaintPending = rs2.getInt("complaint_pending");
        	  
        	  MonthToDateMessage += "\tRegistered : "+ComplaintRegistered+" \n";
        	  MonthToDateMessage += "\tResolved : "+ComplaintResolved+" \n";
        	  MonthToDateMessage += "\tPending : "+ComplaintPending+" \n";
          }
         
          // WhatsApp Messages
          
          MonthToDateMessage += "\nTop Complaints";
          
          ResultSet rs3 = s.executeQuery("SELECT ctl.type_id, ctt.english_label, (count(*)/("+
        		  "SELECT count(*) FROM crm_tasks ct join crm_tasks_list ctl on ct.id = ctl.id join crm_tasks_types ctt on ctl.type_id = ctt.id where region_id in (5, 2, 4, 7) and created_on between '2014-08-23' and "+Utilities.getSQLDateNext(TodayDate)+""+
        		  "))*100 percentage FROM crm_tasks ct join crm_tasks_list ctl on ct.id = ctl.id join crm_tasks_types ctt on ctl.type_id = ctt.id where region_id in (5, 2, 4, 7) and created_on between '2014-08-23' and "+Utilities.getSQLDateNext(TodayDate)+" group by ctl.type_id order by percentage desc"+
        		  "");
          while(rs3.next()){
        	  long percentage = Math.round(rs3.getDouble(3));
        	  if (percentage > 3){
        		  MonthToDateMessage += "\n\t" + rs3.getString(2) +"\t"+percentage+"%";
        	  }
          }
          
          
          String Message = TodayMessage + MonthToDateMessage;
          
          System.out.println(Message);
          
          
          
          
          
          
          // Amir Aftab
			
          
          
          
          
          
          
          	Utilities.sendWhatsApp("923334566993-1406051187", Message, null);
			Thread.currentThread().sleep(5000);
			
			
			// RSM Group A
			//Utilities.sendWhatsApp("923449279270-1409211750", Message, null);
			//Thread.currentThread().sleep(5000);
			
			//Thread.currentThread().sleep(5000);
          
			
          
          
          
			Utilities.sendWhatsApp("923008406444-1408475526", Message, null); // Anas Wahab
			//Utilities.sendWhatsApp("923444471200", Message, null); // Anas Wahab
	          
			
			
			
			
          s.close();
          
       }catch(Exception e)
       {
          e.printStackTrace();
       }finally{
    	   ds.dropConnection();
       }
    	
    	
    }
    

}
