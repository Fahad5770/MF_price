package com.pbc.bi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
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

public class LiftingDateChange {
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
	          
	        
			
			
			int i=0;
			String sql ="SELECT * FROM pep.inventory_delivery_note where created_on between '2017-12-01 00:00:00' and '2017-12-08 06:00:00';";
	        
	        ResultSet rs = s.executeQuery(sql);   
	        String creation;
	        while(rs.next()){
	        	i++;
           	    creation = rs.getString("created_on");
           	    Date CreatedOn =  rs.getDate("created_on");
           	    
           	    System.out.println("creation"+creation);
        	    String[] split = creation.split("\\s+");
           	    String extraction = split[1];
           	    System.out.println("extraction: "+extraction);
           	    String time = extraction.substring(0,extraction.length()-2);
           	    System.out.println("Time: "+time);
           	    
           	 SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
           	format.format(CreatedOn); 
           	    
           	    
        	    String update ="update inventory_delivery_note set created_on= '2017-11-30 "+ time + "' where created_on between '"+CreatedOn+" "+time +"' and '"+CreatedOn+" "+time +"'";
        	    System.out.println(update);
        	    int updated = s2.executeUpdate(update);   
           }    
           
              System.out.println("Total Looped run "+i);             
			
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
