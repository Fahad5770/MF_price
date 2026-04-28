package com.pbc.outlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.employee.EmployeeHierarchy;
import com.pbc.outlet.OutletDashboard;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;
import com.pbc.workflow.WorkflowEmail;

@WebServlet(description = "New Outlet Request Execute", urlPatterns = {
		"/outlet/WorkflowManagerNewOutletRequestExecute" })

public class WorkflowManagerNewOutletRequestExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public WorkflowManagerNewOutletRequestExecute() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		String UserID = null;

		if (session.getAttribute("UserID") != null) {
			UserID = (String) session.getAttribute("UserID");
		}

		if (UserID == null) {
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		JSONObject obj = new JSONObject();

		response.setContentType("application/json");

		long RequestIDVal = Utilities.parseLong(request.getParameter("RequestID"));
		String WorkflowStepRemarks = Utilities.filterString(request.getParameter("WorkflowStepRemarks"), 1, 100);
		int StepID = Utilities.parseInt(request.getParameter("StepID"));
		int NextStepID = Utilities.parseInt(request.getParameter("NextStepID"));
		int NextActionID = Utilities.parseInt(request.getParameter("NextActionID"));
		// long NextUserID = Utilities.parseLong(request.getParameter("NextUserID"));
		boolean isLastStep = Utilities.parseBoolean(request.getParameter("isLastStep"));
		long DistributorId = Utilities.parseLong(request.getParameter("DistributorId"));

		// Form Data

		Datasource ds = new Datasource();

		try {

			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();

			Workflow wf = new Workflow();
			long NextUserID = 0;
			String updateQuery = "";
			
			int regionId=0;
			ResultSet rsRegion = s.executeQuery("select region_id from common_distributors where distributor_id="+DistributorId);
			if(rsRegion.first()) {
				regionId = rsRegion.getInt("region_id");
			}
			
			int finalUser = 0;
			
			switch(regionId){
			case 1: // North
			//	finalUser = 204211264; // Zeshan
				finalUser = 204220064; // Usama
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
			
			//System.out.println("StepID "+StepID);

			if (StepID == 2 || StepID == 3 || StepID == 4) {
				long OutletrequestId = Utilities.parseLong(request.getParameter("OutletrequestId"));

				String OutletAddress = Utilities.filterString(request.getParameter("OutletAddress"), 1, 100);
				int ChannelId = Utilities.parseInt(request.getParameter("Channel"));
				String Area = Utilities.filterString(request.getParameter("Area"), 1, 100);
				String SubArea = Utilities.filterString(request.getParameter("SubArea"), 1, 100);
				String OwnerName = Utilities.filterString(request.getParameter("OwnerName"), 1, 100);
				String OwnerNumber = Utilities.filterString(request.getParameter("OwnerNumber"), 1, 100);
				String OwnerCNICNo = Utilities.filterString(request.getParameter("OwnerCNICNo"), 1, 100);
				int IsPurchaser = Utilities.parseInt(request.getParameter("IsPurchaser"));
				String PurchaserName = Utilities.filterString(request.getParameter("PurchaserName"), 1, 100);
				String PurchaserNumber = Utilities.filterString(request.getParameter("PurchaserNumber"), 1, 100);
				String OutletName = Utilities.filterString(request.getParameter("OutletName"), 1, 100);
				
				updateQuery = "UPDATE `common_outlets_request` SET `outlet_name` = '" + OutletName
						+ "', `outlet_address` = '" + OutletAddress + "', `area_label` = '" + Area
						+ "', `sub_area_label` = '" + SubArea + "', `owner_name` = '" + OwnerName
						+ "', `owner_contact_number` = '" + OwnerNumber + "', `owner_cnic_number` = '" + OwnerCNICNo
						+ "', `purchaser_name` = '" + PurchaserName + "', `purchaser_number` = '" + PurchaserNumber + "', `sub_channel_id` = " + ChannelId
						+ " where request_id=" + RequestIDVal;

			}

			if (StepID == 2) { // Approval distributor RSM with editable
				ResultSet rsSND = s.executeQuery(
						"select rsm_id from common_distributors where distributor_id=" + DistributorId);
				if (rsSND.first()) {
							int rsm = rsSND.getInt("rsm_id");
							if(rsm !=0) {
								NextUserID = rsSND.getInt("rsm_id");
								wf.doStepActionForOutlet(RequestIDVal, StepID, isLastStep, NextUserID, NextActionID, WorkflowStepRemarks, 3);
							}else {
								ResultSet rsSNDI = s.executeQuery(
										"select snd_id from common_distributors where distributor_id=" + DistributorId);
								if (rsSNDI.first()) {
									int snd=rsSNDI.getInt("snd_id");
									if(snd !=0) {
										NextUserID = rsSNDI.getInt("snd_id");
										wf.doStepActionForOutlet(RequestIDVal, StepID, isLastStep, NextUserID, NextActionID, WorkflowStepRemarks,4);
									}else {
										wf.doStepActionForOutlet(RequestIDVal, StepID, isLastStep, finalUser, NextActionID, WorkflowStepRemarks,5);
									}
								}
							}
					
				}

				
			//	System.out.println("StepID :2 " + updateQuery);
				s2.executeUpdate(updateQuery);
			}

			if (StepID == 3) { // Approval SND
				ResultSet rsSND = s.executeQuery(
						"select snd_id from common_distributors where distributor_id=" + DistributorId);
				if (rsSND.first()) {
					int snd = rsSND.getInt("snd_id");
					if(snd !=0) {
						NextUserID = rsSND.getInt("snd_id");
//						if(NextUserID == 204230036) {
//							NextUserID = 204211264;
//						}
						wf.doStepActionForOutlet(RequestIDVal, StepID, isLastStep, NextUserID, NextActionID, WorkflowStepRemarks,4);
					}else {
						wf.doStepActionForOutlet(RequestIDVal, StepID, isLastStep,finalUser, NextActionID, WorkflowStepRemarks,5);	
					}
					
				}

			
			//	System.out.println("StepID :3 " + updateQuery);
				s2.executeUpdate(updateQuery);
			}

			if (StepID == 4) { // Approval RSM

			//	NextUserID = 204230058; // Muhammad Attique Coordinator
				
			

				wf.doStepActionForOutlet(RequestIDVal, StepID, isLastStep,finalUser , NextActionID, WorkflowStepRemarks,5);
				s2.executeUpdate(updateQuery);
			}
			
			/*
			 * if (StepID == 5) { // Approval SND
			 * 
			 * NextUserID = 204211264; // Zeshan sb. wf.doStepActionForOutlet(RequestIDVal,
			 * StepID, isLastStep, NextUserID, NextActionID, WorkflowStepRemarks,6);
			 * 
			 * }
			 */

			if (StepID == 5) { // Final Approval 
			//	System.out.println("This is final step 5 " + NextActionID);

			
				
				// ADD Outlet
				System.out.println("SELECT * FROM common_outlets_request where request_id=" + RequestIDVal);
				ResultSet reOut = s.executeQuery("SELECT * FROM common_outlets_request where request_id=" + RequestIDVal);
				if (reOut.first()) {
					
					long maxID = 0;
					int CityID = 0, id = 0, RegionID = 0;
					String DistributorName = "";

					System.out.println("select city_id,region_id,name FROM common_distributors where distributor_id="+ DistributorId);
					ResultSet rs23 = s2.executeQuery("select city_id,region_id,name FROM common_distributors where distributor_id="+ DistributorId);
					if (rs23.first()) {
						CityID = rs23.getInt("city_id");
						RegionID = rs23.getInt("region_id");
						DistributorName = rs23.getString("name");
					}

					System.out.println("select max(id) from common_outlets where city_id=" + CityID);
					ResultSet rs1 = s2.executeQuery("select max(id) from common_outlets where city_id=" + CityID);
					if (rs1.first()) {
						maxID = rs1.getLong(1);
					};

					if (maxID == 0) {
						System.out.println("SELECT * FROM common_outlet_code_range where city_id=" + CityID);
						ResultSet RsRange = s2.executeQuery("SELECT * FROM common_outlet_code_range where city_id=" + CityID);
						if (RsRange.first()) {
							maxID = RsRange.getLong("start_code");
						}
					} else {
						maxID += 1;
					}

					String RegionShortName = "";
					String RegionName = "";

					// table common_regions data fetching query
					System.out.println("select * from common_regions where region_id=" + RegionID);
					ResultSet rs6 = s2.executeQuery("select * from common_regions where region_id=" + RegionID);
					if (rs6.first()) {
						RegionShortName = rs6.getString("region_short_name");
						RegionName = rs6.getString("region_name");
					}

					id = reOut.getInt("id");

					System.out.println("INSERT INTO common_outlets(id,name,address,region_id,distributor_id,created_on,created_by,city_id,cache_contact_name,cache_contact_number,cache_contact_nic,cache_beat_plan_id,kpo_request_id,is_mobile,lat,lng,accuracy,pic_channel_id,is_active)VALUES"
					+ "('" + maxID + "','" + reOut.getString("outlet_name") + "','"
					+ reOut.getString("outlet_address") + "','" + RegionID + "','" + DistributorId
					+ "','"+ reOut.getString("created_on") +"', '" + reOut.getString("created_by") + "','" + CityID + "','" + reOut.getString("owner_name")
					+ "', '" + reOut.getString("owner_contact_number") + "','"
					+ reOut.getString("owner_cnic_number") + "','" + reOut.getString("beat_plan_id")
					+ "','" + id + "',1, " + reOut.getDouble("lat") + ", " + reOut.getDouble("lng")
					+ ", " + reOut.getDouble("accuracy") + ", " + reOut.getInt("sub_channel_id")
					+ ",1)");
					s2.executeUpdate(
							"INSERT INTO common_outlets(id,name,address,region_id,distributor_id,created_on,created_by,city_id,cache_contact_name,cache_contact_number,cache_contact_nic,cache_beat_plan_id,kpo_request_id,is_mobile,lat,lng,accuracy,pic_channel_id,is_active)VALUES"
									+ "('" + maxID + "','" + reOut.getString("outlet_name") + "','"
									+ reOut.getString("outlet_address") + "','" + RegionID + "','" + DistributorId
									+ "','"+ reOut.getString("created_on") +"', '" + reOut.getString("created_by") + "','" + CityID + "','" + reOut.getString("owner_name")
									+ "', '" + reOut.getString("owner_contact_number") + "','"
									+ reOut.getString("owner_cnic_number") + "','" + reOut.getString("beat_plan_id")
									+ "','" + id + "',1, " + reOut.getDouble("lat") + ", " + reOut.getDouble("lng")
									+ ", " + reOut.getDouble("accuracy") + ", " + reOut.getInt("sub_channel_id")
									+ ",1)");

					System.out.println(
					"INSERT INTO common_outlets_contacts(outlet_id,contact_name,contact_number,contact_nic,type_id,is_primary,puchaser_name,purchase_number,is_owner_purchaser)values ("
					+ maxID + ",'" + reOut.getString("owner_name") + "','"
					+ reOut.getString("owner_contact_number") + "','"
					+ reOut.getString("owner_cnic_number") + "',5,1,'"
					+ reOut.getString("purchaser_name") + "', '" + reOut.getString("purchaser_number")
					+ "', " + reOut.getString("is_owner_purchaser") + ")");
					s2.executeUpdate(
							"INSERT INTO common_outlets_contacts(outlet_id,contact_name,contact_number,contact_nic,type_id,is_primary,puchaser_name,purchase_number,is_owner_purchaser)values ("
									+ maxID + ",'" + reOut.getString("owner_name") + "','"
									+ reOut.getString("owner_contact_number") + "','"
									+ reOut.getString("owner_cnic_number") + "',5,1,'"
									+ reOut.getString("purchaser_name") + "', '" + reOut.getString("purchaser_number")
									+ "', " + reOut.getString("is_owner_purchaser") + ")");
					System.out.println(
					"INSERT INTO outletmaster(SE_NO,Outlet_ID,Outlet_Name,Customer_ID,Customer_Name,Region,Region_Name,Owner,Address,Telepohone,Created_By,Creation_Date,Latitude,Longitude,Bsi_ID ,Bsi_Name,Market_Code,Market_Name,NID_Number,RSM_ID,ASM_ID,CR_ID,Samp_Allowed,Samp_Type,Fix_Sampling,PCAS_Sampling,Advance_Sampling,Adavance,Adv_type,Deductions,Status,Vehicle,Discounted,city_id) VALUES"
					+ "(0," + maxID + ",'" + reOut.getString("outlet_name") + "'," + DistributorId
					+ ",'" + DistributorName + "','" + RegionShortName + "','" + RegionName + "','"
					+ reOut.getString("owner_name") + "','" + reOut.getString("outlet_address") + "','"
					+ reOut.getString("owner_contact_number")
					+ "','PBCIT',now(), 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0," + CityID + ")");
					s2.executeUpdate(
							"INSERT INTO outletmaster(SE_NO,Outlet_ID,Outlet_Name,Customer_ID,Customer_Name,Region,Region_Name,Owner,Address,Telepohone,Created_By,Creation_Date,Latitude,Longitude,Bsi_ID ,Bsi_Name,Market_Code,Market_Name,NID_Number,RSM_ID,ASM_ID,CR_ID,Samp_Allowed,Samp_Type,Fix_Sampling,PCAS_Sampling,Advance_Sampling,Adavance,Adv_type,Deductions,Status,Vehicle,Discounted,city_id) VALUES"
									+ "(0," + maxID + ",'" + reOut.getString("outlet_name") + "'," + DistributorId
									+ ",'" + DistributorName + "','" + RegionShortName + "','" + RegionName + "','"
									+ reOut.getString("owner_name") + "','" + reOut.getString("outlet_address") + "','"
									+ reOut.getString("owner_contact_number")
									+ "','PBCIT',now(), 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0," + CityID + ")");
					
					String DayInsertSql="INSERT INTO distributor_beat_plan_schedule(id,outlet_id,day_number)values ("+reOut.getString("beat_plan_id")+","+maxID+","+reOut.getString("day")+")";
					System.out.println(DayInsertSql);
					s2.executeUpdate(DayInsertSql);
					
					System.out.println("SELECT * FROM mobile_outlets_request_files where outlet_request_id="+reOut.getString("outlet_id"));
					ResultSet rsImages = s2.executeQuery("SELECT * FROM mobile_outlets_request_files where outlet_request_id="+reOut.getString("outlet_id"));
					while(rsImages.next()) {
						s3.executeUpdate("insert into mobile_common_outlets_files(filename, uri, created_on, created_by, file_type, outlet_id, lat, lng, accuracy, mobile_timestamp) values('"+rsImages.getString("filename")+"', '"+rsImages.getString("uri")+"', '"+rsImages.getString("created_on")+"', '"+rsImages.getString("created_by")+"', 1, "+maxID+", "+rsImages.getDouble("lat")+", "+rsImages.getDouble("lng")+", "+rsImages.getDouble("accuracy")+", '"+rsImages.getString("mobile_timestamp")+"' )");
					}
					
				
					wf.doStepAction(RequestIDVal, StepID, true, 0, 0, WorkflowStepRemarks); // close this request
			       //  Utilities.sendPBCHTMLEmailExternalMoiz(new String[]{""}, null,null, "New Outlet Opening | "+maxID , "New Outlet Opened Successsfully", null);
				};
			}

			ds.commit();

		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				obj.put("success", "false");
				obj.put("error", e.toString());
				e.printStackTrace();
			}

		}
		obj.put("RequestID", RequestIDVal);
		obj.put("success", "true");

		PrintWriter out = response.getWriter();

		out.print(obj);
		out.close();

	}

}