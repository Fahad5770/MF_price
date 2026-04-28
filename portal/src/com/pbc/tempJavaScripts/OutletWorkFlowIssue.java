package com.pbc.tempJavaScripts;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.pbc.inventory.StockPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.util.DateFormatConverter;

public class OutletWorkFlowIssue {

	public static void main(String[] args) throws SQLException, ParseException, ClassNotFoundException,
	InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		Datasource ds = new Datasource();
		
		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			
				
			 class SKus {
				public int packageId = 0;
				public int brandId = 0;
				public int lrbId = 0;
			}
			 
			 ArrayList<SKus> SKUSObj = new ArrayList<SKus>();
			 
			 ResultSet rs=s.executeQuery("select * from inventory_products_view where is_visible=1 order by lrb_type_id");
			 while(rs.next()) {
				 int package_id = rs.getInt("package_id");
				 int brand_id = rs.getInt("brand_id");
				 int lrb_id = rs.getInt("lrb_type_id");
				 //System.out.println(package_id+"-"+brandid+"-"+lrb_id);
				 SKus sku = new SKus();

				// Set the properties of the SKU object
				sku.packageId = package_id;
				sku.brandId = brand_id;
				sku.lrbId = lrb_id;

				// Add the SKU object to the SKUSObj ArrayList
				SKUSObj.add(sku);
				 //SKUSObj.add(package_id,brandid,lrb_id);
			 }
			 
			 for(int i=0; i<SKUSObj.size(); i++) {
				 System.out.println(SKUSObj.get(i).packageId + "-" + SKUSObj.get(i).brandId + "-" + SKUSObj.get(i).lrbId);
			 }
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	//	System.out.println("Total Dispatches: "+i);
		}

}
