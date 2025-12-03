package com.pbc.bi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.apache.commons.lang3.time.DateUtils;

//import com.itextpdf.text.pdf.codec.Base64.InputStream;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class TestingUrgentMobileData {

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
	          
			
			Date StartDate = new Date();
	        Date EndDate =  new Date();
	        
	        StartDate= Utilities.parseDate("01/01/2018"); 
	        EndDate= Utilities.parseDate("27/02/2018");
			
			long MobileOrderID=0;
			long MobileUnEditedOrderID=0;
			double Distance=0;
			
			
			ResultSet rs = s.executeQuery("select * from mobile_order where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
			while(rs.next()){
			
				MobileOrderID = rs.getLong("id");
				
				
				
				//Calculating DFS
        		ResultSet rs2 = s2.executeQuery("SELECT 	sum((( 3959 * acos( cos( radians(mo.lat) ) * cos( radians( co.lat ) ) * cos( radians( co.lng ) - radians(mo.lng) ) + sin ( radians(mo.lat) )  * sin( radians( co.lat ) ) ) ) * 1609.34 )) AS distance "+ 
                        " FROM mobile_order mo, common_outlets co where mo.outlet_id=co.id and mo.id ="+MobileOrderID+" and mo.lat!=0 and mo.lng!=0 and co.lat!=0 and co.lng!=0");
        		if(rs2.first()){
        			 Distance = rs2.getDouble("distance");
        			
        		}
        		
        		////////////////////////////////

        		double MaxTIM=0;
        		double MinTIM=0;
        		double TotalTIM=0;
        		
        		
        		////////////Calculating TIM///////////////////
        		
        		//getting unedited id
        		
        		ResultSet rs3 = s3.executeQuery("select unedited_order_id from mobile_order where id="+MobileOrderID);
        		if(rs3.first()){
        			MobileUnEditedOrderID = rs3.getLong("unedited_order_id");        			
        		}
        		
        		
        		
        		
        		
        			ResultSet rs24 = s3.executeQuery("select time_to_sec(max(timestamps)), time_to_sec(min(timestamps)) from mobile_order_timestamps where mobile_order_no="+MobileUnEditedOrderID);
        			if(rs24.first()){
        				MaxTIM = rs24.getDouble(1);
        				MinTIM = rs24.getDouble(2);
        				
        				TotalTIM += (MaxTIM-MinTIM);
        			}
        			
        			TotalTIM = TotalTIM/60;
        		
        		////////////////////////////////////////////////
        			
        			
        			
        			String UpdateQuery = "Update mobile_order set distance_from_store="+Distance+" , time_in_shop="+TotalTIM+" where id="+MobileOrderID;
        			
        			//if(TotalTIM!=0){
        				System.out.println(UpdateQuery + "   - "+ rs.getDate("created_on"));
        			//}
        			
        			s4.executeUpdate(UpdateQuery);
        			
        		
        		
			}
			
			System.out.println("Done!!!! till "+StartDate+" - "+EndDate);
			
			
			
	        
			
			
                       
			
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
