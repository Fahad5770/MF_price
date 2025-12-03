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
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.util.DateFormatConverter;

public class OutletWorkFlowImageIssue {

	public static void main(String[] args) throws SQLException, ParseException, ClassNotFoundException,
	InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		Datasource ds = new Datasource();
		
		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			
			int reqId=39;
			
			String[] images = {
					"Outlet_5181686205613867_Outlet_scaled_20940772-da43-49e0-9716-16fe5b5ae2654725511924565825543",
					"Outlet_5181686205613867_Outlet_scaled_bc50643a-06e6-4b6b-9090-8fbb1dd1458a2690406163955384880",
					"Outlet_5181686205613867_Outlet_scaled_82e31f7f-23c5-4ebc-8bce-36513ab7d9cb8695670733877239979",
					"Outlet_5181686205613867_Outlet_scaled_77e7fa58-2bdf-4aa2-99dc-5abe183a065d2850036194916189672"
					};
			
			String path="/home/ftpshared/OutletImages/";
			
			ResultSet rsMain = s.executeQuery("SELECT outlet_id,outlet_name,created_on,created_by,lat,lng,accuracy FROM pep.common_outlets_request where request_id in ("+reqId+")");
			if(rsMain.first()) {
				
				for(int i=0; i<images.length; i++) {
					String name= images[i]+".jpg";
					String IPath = path+images[i]+".jpg";
					
					String values="'"+rsMain.getString("outlet_id")+"', '"+name+"','"+IPath+"', '"+rsMain.getString("created_on")+"', '"+rsMain.getString("created_by")+"',1,"+rsMain.getString("lat")+", "+rsMain.getString("lng")+", "+rsMain.getString("accuracy")+", '"+rsMain.getString("created_on")+"'";
					System.out.println("INSERT INTO `pep`.`mobile_outlets_request_files`(`outlet_request_id`,`filename`,`uri`,`created_on`,`created_by`,`file_type`,`lat`,`lng`,`accuracy`,`mobile_timestamp`)VALUES("+values+")");
					s2.executeUpdate("INSERT INTO `pep`.`mobile_outlets_request_files`(`outlet_request_id`,`filename`,`uri`,`created_on`,`created_by`,`file_type`,`lat`,`lng`,`accuracy`,`mobile_timestamp`)VALUES("+values+")");
				}
				
			
				//				System.out.println("update pep.workflow_requests_steps set user_id="+rsRSM.getString("rsm_id")+", step_id=3 where sequence_id="+sequence_id);
					//			s3.executeUpdate("update pep.workflow_requests_steps set user_id="+rsRSM.getString("rsm_id")+", step_id=3 where sequence_id="+sequence_id);
						//		System.out.println("update pep.workflow_requests set current_step_id=3,current_userid="+rsRSM.getString("rsm_id")+" where request_id="+rsMain.getString("request_id"));
							//	s3.executeUpdate("update pep.workflow_requests set current_step_id=3,current_userid="+rsRSM.getString("rsm_id")+" where request_id="+rsMain.getString("request_id"));
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	//	System.out.println("Total Dispatches: "+i);
		}

}
