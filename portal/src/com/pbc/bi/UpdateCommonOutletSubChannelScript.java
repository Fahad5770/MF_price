package com.pbc.bi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;

public class UpdateCommonOutletSubChannelScript {
	static Datasource ds = new Datasource();
	public static void main(String[] args) {
		try {
			
			
			ds.createConnection();
			//ds.startTransaction();
			
			Statement s =ds.createStatement();
			Statement s2 =ds.createStatement();
			Statement s3=ds.createStatement();
		
	          
	        String MainSql ="";
	        MainSql="select * from common_outlets where census_sub_channel_id is null"; 
	        ResultSet rs = s.executeQuery(MainSql);   
	        while(rs.next()){
	             
           	     long OutletID=rs.getLong("id");
           	     System.out.println("OutletID :"+OutletID);
           	    
		         String sql1="select * from mrd_census where outlet_id="+OutletID+" and created_on between '2017-11-06' and '2018-01-30' order by id desc";
		         ResultSet rs1 = s2.executeQuery(sql1);
		         if(rs1.first()){
		        	 int SubTradeID=rs1.getInt("census_trader_channel_sub_channel");
		        	
		        	 String updateSql ="update common_outlets set census_sub_channel_id="+SubTradeID+" where id="+OutletID;
		        	 System.out.println(updateSql);
		        	 int updated = s3.executeUpdate(updateSql);  
	        	 }  
    	 
				
           }  //End of MainSql LOOP
	       

           	 //ds.commit();
		 
		    s3.close(); 
			s2.close();
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 
				e.printStackTrace();
			}			
			finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		

		
	}

}
