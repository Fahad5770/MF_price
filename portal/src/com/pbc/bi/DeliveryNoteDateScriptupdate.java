package com.pbc.bi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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

public class DeliveryNoteDateScriptupdate {
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
	          
	        String sql ="SELECT * FROM pep.inventory_delivery_note where created_on between '2018-05-01 00:00:00' and '2018-05-06 03:00:00'";
	        
	        ResultSet rs = s.executeQuery(sql);   
	        String creation;
	        while(rs.next()){
	        	long DeliveryID=rs.getLong("delivery_id");
           	    creation = rs.getString("created_on");
           	    System.out.println("creation"+creation);
        	    String[] split = creation.split("\\s+");
           	    String extraction = split[1];
           	    System.out.println("extraction: "+extraction);
           	    String time = extraction.substring(0,extraction.length()-2);
           	    System.out.println("Time: "+time);
        	    String update ="update inventory_delivery_note set created_on= '2018-04-30 "+ time + "' where delivery_id="+DeliveryID;
        	    System.out.println(update);
        	    int updated = s2.executeUpdate(update);   
        	    if(updated>0) {
        	    	  System.out.println("---------------------------updated-----------------------------------");
        	    }
           }                     
			
		    s5.close();
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
