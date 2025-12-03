package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.workflow.Workflow;

@WebServlet(description = "Mobile MDE MobileSyncOutletRegistration", urlPatterns = {
		"/mobile/MobileSyncOutletRegistration" })
public class MobileSyncOutletRegistration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MobileSyncOutletRegistration() {
		super();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("json");

		System.out.println("MobileSyncOutletRegistration opo");

		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();

		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 3000));

		String mobileTimeStamp = Utilities.filterString(mr.getParameter("mobile_timestamp"), 1, 200);
		String OutletRequestId = Utilities.filterString(mr.getParameter("outlet_request_id"), 1, 200);
		String OutletName = Utilities.filterString(mr.getParameter("outlet_name"), 1, 200);
		int ChannelID = Utilities.parseInt(mr.getParameter("channel_id"));
		String AreaLabel = Utilities.filterString(mr.getParameter("area_label"), 1, 200);
		String SubAreaLabel = Utilities.filterString(mr.getParameter("sub_area_label"), 1, 200);
		String OutletAddress = Utilities.filterString(mr.getParameter("address"), 1, 200);
		String OwnerName = Utilities.filterString(mr.getParameter("owner_name"), 1, 200);
		String OwnerCNIC = Utilities.filterString(mr.getParameter("owner_cnic"), 1, 200);
		String OwnerContactNumber = Utilities.filterString(mr.getParameter("owner_mobile_no"), 1, 200);
		String PurchaserName = Utilities.filterString(mr.getParameter("owner_name"), 1, 200);
		String PurchaserNumber = Utilities.filterString(mr.getParameter("owner_mobile_no"), 1, 200);
		int IsOwnerPurchaser = Utilities.parseInt(mr.getParameter("is_owner_purchaser"));
		double Lat = Utilities.parseDouble(mr.getParameter("lat"));
		double Lng = Utilities.parseDouble(mr.getParameter("lng"));
		double Accuracy = Utilities.parseDouble(mr.getParameter("accuracy"));
		String CreatedOn = Utilities.filterString(mr.getParameter("created_on"), 1, 200);
		long CreatedBy = Utilities.parseLong(mr.getParameter("created_by"));
		String DeviceUUID = Utilities.filterString(mr.getParameter("uuid"), 1, 200);
		String platform = Utilities.filterString(mr.getParameter("platform"), 1, 200);
		int PJPID = 0;
		
		
		
		
		int day = Utilities.getDayOfWeekByDate(Utilities.parseDateYYYYMMDD(mobileTimeStamp));
		
	
		
		Datasource ds = new Datasource();

		try {
			if(ChannelID != 0) {

			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			
			
			/* need parameters 
			 * 
			 1.  cr_id
			 2. agency_id
			 3. agency_name
			 4. shop_category ==> 1
			 5. vehicle_number
			 6. comments
			 7. rm_id,
			
			
			
			
			 ==>
			  area_id,
			 distributor_id
			 beat_plan_id
			 vpo_classifications_id =1
			 created_by,
			 created_on,
			 category_id =1 
			 city_id = 1
			 
			 
			 New Columns
			 1. mobile_time_stamp
			 2. area_label
			 3. sub_area_label
			 4. purchaser_name
			 5. purchaser_number
			 6. is_owner_purchaser
			 7. lat
			 8. lng
			 9. accuracy
			 10. device_id
			 11. platform
			*/
			
			int city_id = 1;
			long DistributorID=0, ASMID=0;

			//System.out.println("SELECT id FROM pep.distributor_beat_plan_users where assigned_to="+CreatedBy);
			ResultSet rsPJP = s.executeQuery("SELECT id FROM distributor_beat_plan_users where assigned_to="+CreatedBy);
			if (rsPJP.first()) {

				PJPID = rsPJP.getInt("id");
			}
			//System.out.println("select distributor_id, asm_id from distributor_beat_plan where id=" + PJPID + " limit 1");
			ResultSet rs = s.executeQuery("select distributor_id, asm_id from distributor_beat_plan where id=" + PJPID + " limit 1");
			if (rs.first()) {

				DistributorID = rs.getLong("distributor_id");
				ASMID = rs.getLong("asm_id");
			}
			
			String args = "'"+OutletRequestId+"', '"+OutletName+"', 1, '"+OutletAddress+"', '"+OwnerName+"', '"+OwnerContactNumber+"', '"+OwnerCNIC+"', "+ChannelID+", "+DistributorID+", "+CreatedBy+", '"+CreatedOn+"', 1, "+PJPID+", 1, "+city_id+", '"+mobileTimeStamp+"', '"+AreaLabel+"', "+
			"'"+SubAreaLabel+"', '"+PurchaserName+"', '"+PurchaserNumber+"', "+IsOwnerPurchaser+", "+Lat+", "+Lng+", "+Accuracy+", '"+DeviceUUID+"', '"+platform+"', "+day;
			System.out.println("INSERT INTO `common_outlets_request`(`outlet_id`,`outlet_name`,`shop_category`,`outlet_address`,`owner_name`,`owner_contact_number`,`owner_cnic_number`,`sub_channel_id`, `distributor_id`,`created_by`,`created_on`,`vpo_classifications_id`"+
			", `beat_plan_id`,`category_id`,`city_id`,`mobile_time_stamp`,`area_label`, `sub_area_label`, `purchaser_name`, `purchaser_number`, `is_owner_purchaser`, `lat`, `lng`, `accuracy`, `device_id`, `platform`,`day`)VALUES("+args+")");
//			
			
			s.executeUpdate("INSERT INTO `common_outlets_request`(`outlet_id`,`outlet_name`,`shop_category`,`outlet_address`,`owner_name`,`owner_contact_number`,`owner_cnic_number`,`sub_channel_id`, `distributor_id`,`created_by`,`created_on`,`vpo_classifications_id`"+
					", `beat_plan_id`,`category_id`,`city_id`,`mobile_time_stamp`,`area_label`, `sub_area_label`, `purchaser_name`, `purchaser_number`, `is_owner_purchaser`, `lat`, `lng`, `accuracy`, `device_id`, `platform`,`day`)VALUES("+args+")");
			
			// OutletRequestId
			
			long AutoIncrementID = 0;

			ResultSet rsAutoIncrementID = s.executeQuery("select LAST_INSERT_ID()");
			if (rsAutoIncrementID.first()) {
				AutoIncrementID = rsAutoIncrementID.getInt(1);
			}
			
			int regionId=0;
			ResultSet rsRegion = s.executeQuery("select region_id from common_distributors where distributor_id="+DistributorID);
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
			
			long requestID = 0;
			Workflow wf = new Workflow();
			if(ASMID!=0) {
			 requestID = wf.createRequestForOutlerRegistration(1, Integer.parseInt(CreatedBy+""), ASMID, 4,2, "New Outlet Request Raised");
			}else {
				ResultSet rsRSM = s.executeQuery("select rsm_id from common_distributors where distributor_id=" + DistributorID);
				if (rsRSM.first()) {
					int RSM=rsRSM.getInt("rsm_id");
					if(RSM!=0) {
						requestID = wf.createRequestForOutlerRegistration(1, Integer.parseInt(CreatedBy+""), rsRSM.getInt("rsm_id"), 4,3, "New Outlet Request Raised");	
					}else {
						
						ResultSet rsSND = s.executeQuery("select snd_id from common_distributors where distributor_id=" + DistributorID);
						if (rsSND.first()) {
							int SND = rsSND.getInt("snd_id");
							if(SND!=0) {
								requestID = wf.createRequestForOutlerRegistration(1, Integer.parseInt(CreatedBy+""), rsSND.getInt("snd_id"), 4,4, "New Outlet Request Raised");
							}else{
								requestID = wf.createRequestForOutlerRegistration(1, Integer.parseInt(CreatedBy+""), finalUser, 5,5, "New Outlet Request Raised");
							}
						}
					}	
				}
			}
			//System.out.println("update common_outlets_request set request_id='"+requestID+"', request_by = "+CreatedBy+" where id="+AutoIncrementID+" and outlet_id="+OutletRequestId);
			s.executeUpdate("update common_outlets_request set request_id='"+requestID+"', request_by = "+CreatedBy+" where id="+AutoIncrementID+" and outlet_id="+OutletRequestId); 
			
			
			json.put("success", "true");
			}else {
				json.put("success", "false");
				json.put("msg", "Channel Tagging Incorrect ");
			}

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("success", "false");
			json.put("msg", "Server Error ");
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("success", "false");
			json.put("msg", "Server Error ");
		}

//		  if (!mr.isExpired()){
//		  
//		  }else{
//		  
//		  json.put("success", "false"); json.put("error_code", "101"); }

		out.print(json);
		out.close();

	}

}
