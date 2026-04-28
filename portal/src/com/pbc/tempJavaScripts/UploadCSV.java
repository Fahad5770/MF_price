package com.pbc.tempJavaScripts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.pbc.util.Datasource;

public class UploadCSV {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Datasource ds = null;
		Statement s = null;
		Statement s2 = null;
		
		String csvFilePath = "/home/ftpshared/outletsInactive.csv";
		
		try {
			ds = new Datasource();
			ds.createConnection();
			//ds.startTransaction();
			s = ds.createStatement();
			s2 = ds.createStatement();
			  String line;
	            int i=0;
	            
	            try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {

	            while ((line = reader.readLine()) != null) {
	                String[] data = line.split(",");

	                if (data.length < 1) {
	                    System.out.println("Invalid data: " + line);
	                    continue;
	                }

	                if(i > 1) {
	                // Assuming CSV format: sub_area_label, outlet_id
	                String outletId = data[0];
	               // outletId = outletId+"33";
	               System.out.println(i+" : update distributor_beat_plan_schedule set is_active=0 where outlet_id="+outletId);
	                s.executeUpdate("update distributor_beat_plan_schedule set is_active=0 where outlet_id="+outletId);
	              //  System.out.println("select * from distributor_beat_plan_schedule where outlet_id="+outletId);
//	               ResultSet rsData =  s.executeQuery("select * from distributor_beat_plan_schedule where outlet_id="+outletId);
//	                if(rsData.first()) {
//	                	System.out.println(i+" Found  : "+rsData.getLong("outlet_id"));
//	                }else {
//	                	System.out.println(i+" insert into temp(`outlet_id`,`created_on`) VALUES("+outletId+", now())" );
//	                	s2.executeUpdate("insert into temp(`outlet_id`,`created_on`) VALUES("+outletId+", now())" );
//	                }
	                
	                } // end of row condition
	                i++;
	            }
	            }catch (Exception e){
	    		    
	    		    e.printStackTrace();
	            }

	            System.out.println("Data updated successfully.");

	            s.close();
	            s2.close();
			
		}catch (Exception e){
		    
		    e.printStackTrace();
		   } finally{
		    try {
		     if (s != null){
		      s.close();
		      ds.dropConnection();
		     }
		    } catch (SQLException e) {
		     e.printStackTrace();
		    }    
		   }
		
		

	}

}
