package com.pbc.myscripts;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.pbc.inventory.Product;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class TestFunctions {
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

		Datasource ds = null;
		Statement s = null;
		Statement s2 = null;

					String csvFilePath = "D:\\order_ids.csv";
					
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
				                String OrderID = data[0];
				               // outletId = outletId+"33";
				               System.out.println(i+" OrderID : "+OrderID);
				              //  s.executeUpdate("update distributor_beat_plan_schedule set is_active=0 where outlet_id="+outletId);
				              //  System.out.println("select * from distributor_beat_plan_schedule where outlet_id="+outletId);
//				               ResultSet rsData =  s.executeQuery("select * from distributor_beat_plan_schedule where outlet_id="+outletId);
//				                if(rsData.first()) {
//				                	System.out.println(i+" Found  : "+rsData.getLong("outlet_id"));
//				                }else {
//				                	System.out.println(i+" insert into temp(`outlet_id`,`created_on`) VALUES("+outletId+", now())" );
//				                	s2.executeUpdate("insert into temp(`outlet_id`,`created_on`) VALUES("+outletId+", now())" );
//				                }
				                
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
