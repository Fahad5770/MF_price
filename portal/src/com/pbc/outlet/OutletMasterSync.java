package com.pbc.outlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class OutletMasterSync {
	static Datasource ds = new Datasource();
	public static void main(String[] args) {
		try {
			
			
			ds.createConnection();
			//ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3=ds.createStatement();
			Statement s4=ds.createStatement();
			Statement s5=ds.createStatement();
			
			//Main master query for fields which are not in outletmaster table
			String query1="select * from common_outlets where id not in (select Outlet_ID from outletmaster) ";
			
			ResultSet rs = s.executeQuery(query1);
		     while( rs.next() ){
		      long ID = rs.getLong("id");
		      
		      String Name=rs.getString("name");
		      String Address=rs.getString("address");
		      int RegionID=rs.getInt("region_id");
		      long CustomerID=rs.getLong("distributor_id");
		      
		      double Lat=rs.getDouble("lat");
		      double Lng=rs.getDouble("lng");
		      
		      //variables of table common_outlets_contacts
		      String OwnerName="";
		      String OwnerContactNumber="";
		      
		      //table common_outlets_contacts data fetching query
		      String query2="select * from common_outlets_contacts where outlet_id="+ID;
		      
		      ResultSet rs2 = s2.executeQuery(query2);
			     while( rs2.next() ){
			    	 OwnerName=rs2.getString("contact_name");
			    	 OwnerContactNumber=rs2.getString("contact_number");
		     	}
			     
			   //variables of table common_distributors
			      String DistributorName="";
			      
			     //table common_distributors data fetching query
			     String query3="select * from common_distributors where distributor_id="+CustomerID;
			    
			     ResultSet rs3 = s3.executeQuery(query3);
			     while( rs3.next() ){
			    	 DistributorName=rs3.getString("name");
			    	 
			     }
					 
			     
			    //variables of table common_regions
			    String RegionShortName="";
			    String RegionName="";
		      
			    //table common_regions data fetching query
			    String query4="select * from common_regions where region_id="+RegionID;
			    
			    ResultSet rs4 = s4.executeQuery(query4);
				     while( rs4.next() ){
				    	 RegionShortName=rs4.getString("region_short_name");
				    	 RegionName=rs4.getString("region_name");
			     	}
				
				 
				 //table outletmaster insertion query
				 s5.executeUpdate("INSERT INTO outletmaster(SE_NO,Outlet_ID,Outlet_Name,Customer_ID,Customer_Name,Region,Region_Name,Owner,Address,Telepohone,Created_By,Creation_Date,Latitude,Longitude,Bsi_ID ,Bsi_Name,Market_Code,Market_Name,NID_Number,RSM_ID,ASM_ID,CR_ID,Samp_Allowed,Samp_Type,Fix_Sampling,PCAS_Sampling,Advance_Sampling,Adavance,Adv_type,Deductions,Status,Vehicle,Discounted) VALUES(0,"+ID+",'"+Name+"',"+CustomerID+",'"+DistributorName+"','"+RegionShortName+"','"+RegionName+"','"+OwnerName+"','"+Address+"','"+OwnerContactNumber+"','PBCIT',now(), "+Lat+", "+Lng+",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
		     }
			
		    s5.close(); 
		    s4.close(); 
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
