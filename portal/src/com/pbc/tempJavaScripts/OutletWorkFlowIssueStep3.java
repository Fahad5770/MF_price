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

public class OutletWorkFlowIssueStep3 {

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
			
			String tt = "2877,2880,2884,2886,2887,2902,2905,3075,3076,3077,3078,3079,3081,3083,3086,3087,3088,3089,3374,3388,3393,3397,3402,3411,3416,3419,3445,3458,3472,3473,3475,3476,3479,3481,3492,3493";
			ResultSet rsMain = s.executeQuery("SELECT created_by, request_id FROM pep.workflow_requests where request_id in ("+tt+")");
			while(rsMain.next()) {
				//System.out.println("Created By : "+rsMain.getString("created_by"));
				System.out.println("request ID : "+rsMain.getString("request_id"));
				int sequence_id=0;
				System.out.println("SELECT sequence_id FROM pep.workflow_requests_steps where request_id="+rsMain.getString("request_id")+" and user_id=0");
				ResultSet rsSeq = s2.executeQuery("SELECT sequence_id FROM pep.workflow_requests_steps where request_id="+rsMain.getString("request_id")+" and user_id=0");
				if(rsSeq.first()) {
					System.out.println("sequence_id :"+rsSeq.getInt("sequence_id"));
					sequence_id=rsSeq.getInt("sequence_id");
					int PJPID=0;
					long DistributorID=0, RSMID=0;
					ResultSet rsPJP = s2.executeQuery("SELECT id FROM pep.distributor_beat_plan_users where assigned_to="+rsMain.getString("created_by"));
					if (rsPJP.first()) {

						PJPID = rsPJP.getInt("id");
					}
				//	System.out.println("PJPID : "+PJPID);
					//System.out.println("select distributor_id, asm_id from distributor_beat_plan where id=" + PJPID + " limit 1");
					ResultSet rs = s3.executeQuery("select distributor_id, asm_id from distributor_beat_plan where id=" + PJPID + " limit 1");
					if (rs.first()) {

						DistributorID = rs.getLong("distributor_id");
						//ASMID = rs.getLong("asm_id");
					}
					//System.out.println("DistributorID : "+DistributorID);
					//System.out.println("ASMID : "+ASMID);
					ResultSet rsRSM = s3.executeQuery("select rsm_id from common_distributors where distributor_id=" + DistributorID);
					if (rsRSM.first()) {
								int rsm = rsRSM.getInt("rsm_id");
								if(rsm !=0) {
									System.out.println("RSMID : "+rsRSM.getString("rsm_id"));
									System.out.println("update pep.workflow_requests_steps set user_id="+rsRSM.getString("rsm_id")+", step_id=3 where sequence_id="+sequence_id);
									s4.executeUpdate("update pep.workflow_requests_steps set user_id="+rsRSM.getString("rsm_id")+", step_id=3 where sequence_id="+sequence_id);
									System.out.println("update pep.workflow_requests set current_step_id=3,current_userid="+rsRSM.getString("rsm_id")+" where request_id="+rsMain.getString("request_id"));
									s4.executeUpdate("update pep.workflow_requests set current_step_id=3,current_userid="+rsRSM.getString("rsm_id")+" where request_id="+rsMain.getString("request_id"));
								}else {
									ResultSet rsSND = s4.executeQuery("select snd_id from common_distributors where distributor_id=" + DistributorID);
									if (rsSND.first()) {
										int snd=rsSND.getInt("snd_id");
										if(snd !=0) {
											System.out.println("SND : "+rsSND.getString("snd_id"));
											System.out.println("update pep.workflow_requests_steps set user_id="+rsSND.getString("snd_id")+", step_id=4 where sequence_id="+sequence_id);
											s5.executeUpdate("update pep.workflow_requests_steps set user_id="+rsSND.getString("snd_id")+", step_id=4 where sequence_id="+sequence_id);
											System.out.println("update pep.workflow_requests set current_step_id=4,current_userid="+rsSND.getString("snd_id")+" where request_id="+rsMain.getString("request_id"));
											s5.executeUpdate("update pep.workflow_requests set current_step_id=4,current_userid="+rsSND.getString("snd_id")+" where request_id="+rsMain.getString("request_id"));
										}else {
											System.out.println("Atique : 204230058");
											System.out.println("update pep.workflow_requests_steps set user_id=204230058, step_id=5 where sequence_id="+sequence_id);
											s5.executeUpdate("update pep.workflow_requests_steps set user_id=204230058, step_id=5 where sequence_id="+sequence_id);
											System.out.println("update pep.workflow_requests set current_step_id=5,current_userid=204230058 where request_id="+rsMain.getString("request_id"));
											s5.executeUpdate("update pep.workflow_requests set current_step_id=5,current_userid=204230058 where request_id="+rsMain.getString("request_id"));
										}
									}
								}
						
					}
					
						
				}
				
				System.out.println("======================================");
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	//	System.out.println("Total Dispatches: "+i);
		}

}
