package com.pbc.util;


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

public class DispatchIssue {

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
			String tt = "289,250,241,236,235";
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
					long DistributorID=0, ASMID=0;
					ResultSet rsPJP = s2.executeQuery("SELECT id FROM pep.distributor_beat_plan_users where assigned_to="+rsMain.getString("created_by"));
					if (rsPJP.first()) {

						PJPID = rsPJP.getInt("id");
					}
				//	System.out.println("PJPID : "+PJPID);
					//System.out.println("select distributor_id, asm_id from distributor_beat_plan where id=" + PJPID + " limit 1");
					ResultSet rs = s2.executeQuery("select distributor_id, asm_id from distributor_beat_plan where id=" + PJPID + " limit 1");
					if (rs.first()) {

						DistributorID = rs.getLong("distributor_id");
						ASMID = rs.getLong("asm_id");
					}
					//System.out.println("DistributorID : "+DistributorID);
					//System.out.println("ASMID : "+ASMID);
					
					if(ASMID==0) {
						
							ResultSet rsRSM = s2.executeQuery("select rsm_id from common_distributors where distributor_id=" + DistributorID);
							if (rsRSM.first()) {
								System.out.println("RSMID : "+rsRSM.getString("rsm_id"));
								System.out.println("update pep.workflow_requests_steps set user_id="+rsRSM.getString("rsm_id")+", step_id=3 where sequence_id="+sequence_id);
								s3.executeUpdate("update pep.workflow_requests_steps set user_id="+rsRSM.getString("rsm_id")+", step_id=3 where sequence_id="+sequence_id);
								System.out.println("update pep.workflow_requests set current_step_id=3,current_userid="+rsRSM.getString("rsm_id")+" where request_id="+rsMain.getString("request_id"));
								s3.executeUpdate("update pep.workflow_requests set current_step_id=3,current_userid="+rsRSM.getString("rsm_id")+" where request_id="+rsMain.getString("request_id"));
							}else {
								ResultSet rsSND = s2.executeQuery("select snd_id from common_distributors where distributor_id=" + DistributorID);
								if (rsSND.first()) {
									System.out.println("SNDID : "+rsSND.getString("rsSND"));
								}else{
									System.out.println("Final : "+204211264);
								}
							}	
						}
				}
				
				System.out.println("======================================");
				
			}
			
//			ResultSet rsDay = s.executeQuery("SELECT request_id,created_on,day FROM pep.common_outlets_request where request_id != 0 order by created_on desc;");
//			
//			System.out.println("SELECT distinct(dispatch_id) as dispatch_id FROM pep.inventory_sales_dispatch_adjusted_products isdap join pep.inventory_sales_dispatch isd on isd.id = isdap.dispatch_id where isd.created_on between '2023-05-01' and '2023-05-31' and raw_cases!=0 and is_blocked=0 and dispatch_id not in(SELECT dispatch_id FROM pep.inventory_sales_dispatch_returned_products)");
//			ResultSet rsDistinctDispatch = s.executeQuery("SELECT distinct(dispatch_id) as dispatch_id FROM pep.inventory_sales_dispatch_adjusted_products isdap join pep.inventory_sales_dispatch isd on isd.id = isdap.dispatch_id where isd.created_on between '2023-05-01' and '2023-05-31' and raw_cases!=0 and is_blocked=0 and dispatch_id not in(SELECT dispatch_id FROM pep.inventory_sales_dispatch_returned_products)");
//	
//			while(rsDistinctDispatch.next()) {
//				int dispatchId = rsDistinctDispatch.getInt("dispatch_id");
//				ResultSet rsDispatchData = s2.executeQuery("SELECT * FROM pep.inventory_sales_dispatch_adjusted_products isdap join pep.inventory_sales_dispatch isd on isd.id = isdap.dispatch_id where dispatch_id="+dispatchId);
//				if(rsDispatchData.first()) {
//					
//					int productId = rsDispatchData.getInt("product_id");
//					int innerDispatchId = rsDispatchData.getInt("dispatch_id");
//					int rawCase = rsDispatchData.getInt("raw_cases");
//					int units = rsDispatchData.getInt("units");
//					int totalUnits = rsDispatchData.getInt("total_units");
//					long liquid_in_ml = rsDispatchData.getLong("liquid_in_ml");
//					int created_by = rsDispatchData.getInt("created_by");
//					
//					System.out.println("Delete from pep.inventory_sales_dispatch_returned_products where is_empty=0 and dispatch_id="+innerDispatchId+" and product_id="+productId);
//					s3.executeUpdate("Delete from pep.inventory_sales_dispatch_returned_products where is_empty=0 and dispatch_id="+innerDispatchId+" and product_id="+productId);
//					
//					System.out.println("insert into pep.inventory_sales_dispatch_returned_products (dispatch_id,product_id,raw_cases,units,total_units,liquid_in_ml) values("+innerDispatchId+","+productId+","+rawCase+","+units+","+totalUnits+","+liquid_in_ml+")");
//					s3.executeUpdate("insert into pep.inventory_sales_dispatch_returned_products (dispatch_id,product_id,raw_cases,units,total_units,liquid_in_ml) values("+innerDispatchId+","+productId+","+rawCase+","+units+","+totalUnits+","+liquid_in_ml+")");
//					
//					System.out.println("update pep.inventory_sales_dispatch set is_liquid_returned=1,liquid_returned_on=now(),liquid_returned_by="+created_by+" where id="+innerDispatchId);
//					s3.executeUpdate("update pep.inventory_sales_dispatch set is_liquid_returned=1,liquid_returned_on=now(),liquid_returned_by="+created_by+" where id="+innerDispatchId);
//					
//					StockPosting sp = new StockPosting();
//					boolean isPosted = sp.postDispatchLiquidReturn(innerDispatchId);
//					sp.close();
//					
//					if (isPosted == true){
//						System.out.println("DispatchId : "+innerDispatchId+" has resoled");
//					}else{
//						System.out.println("Error in DispatchId : "+innerDispatchId);
//					}
//					
//					}
//				i++;
//			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	//	System.out.println("Total Dispatches: "+i);
		}

}
