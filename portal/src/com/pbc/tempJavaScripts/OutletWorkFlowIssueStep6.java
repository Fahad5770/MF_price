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

public class OutletWorkFlowIssueStep6 {

	public static void main(String[] args) throws SQLException, ParseException, ClassNotFoundException,
	InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		Datasource ds = new Datasource();
		int i=0;
		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			Statement s4= ds.createStatement();
			Statement s5= ds.createStatement();
			
			ResultSet rsMain = s.executeQuery("SELECT request_id FROM pep.workflow_requests where current_step_id=6  and status_id=1");
			while(rsMain.next()) {
				
				int requestId=rsMain.getInt("request_id");
				
				long DistributorID=0;
				ResultSet rsDistributor = s2.executeQuery("select distributor_id from pep.common_outlets_request where request_id="+requestId);
				if(rsDistributor.first()) {
					DistributorID = rsDistributor.getLong("distributor_id");
				}
				
				int regionId=0;
				System.out.println("i : "+i);
				System.out.println("select region_id from common_distributors where distributor_id="+DistributorID);
				ResultSet rsRegion = s2.executeQuery("select region_id from common_distributors where distributor_id="+DistributorID);
				if(rsRegion.first()) {
					regionId = rsRegion.getInt("region_id");
				}
				
				int finalUser = 0;
				
				switch(regionId){
				case 1: // North
					finalUser = 204211264; // Zeshan
					break;
				case 2: // Center
					finalUser = 204230058; // Atique
					break;
				case 3: // Multan
					finalUser = 204230058; // Atique
					break;
				case 4: // South
					finalUser = 204220064; // Usama
					break;
				case 5: // Karachi
					finalUser = 204220064; // Usama
					break;
				default:
					System.out.println("No Region");
				}
				
				int seqId=0;
				ResultSet rsSequenceID = s2.executeQuery("SELECT sequence_id FROM pep.workflow_requests_steps  where request_id="+requestId+" and completed_on is null and step_id=6");
				if(rsSequenceID.first()) {	
				seqId=rsSequenceID.getInt("sequence_id");
				}
				
				
				System.out.println("request ID : "+requestId);
				System.out.println("sequence ID : "+seqId);
				System.out.println("Distributor ID : "+DistributorID);
				System.out.println("Region ID : "+regionId);
				System.out.println("Final User : "+finalUser);
				
				
				System.out.println("update pep.workflow_requests_steps set user_id="+finalUser+", step_id=5,action_id=5 where sequence_id="+seqId);
				s3.executeUpdate("update pep.workflow_requests_steps set user_id="+finalUser+", step_id=5,action_id=5 where sequence_id="+seqId);
				
				System.out.println("update pep.workflow_requests set current_userid="+finalUser+", current_step_id=5,current_action_id=5 where request_id="+requestId);
				s3.executeUpdate("update pep.workflow_requests set current_userid="+finalUser+", current_step_id=5,current_action_id=5 where request_id="+requestId);
				
				System.out.println("======================================");
				i++;
			}
			System.out.println("Total Mainiterations : "+i);
		}catch (Exception e) {
			e.printStackTrace();
		}
	//	System.out.println("Total Dispatches: "+i);
		}

}
