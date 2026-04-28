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

@WebServlet(description = "Mobile MDE MobileSyncOutletRegistration3", urlPatterns = {
		"/mobile/MobileSyncOutletRegistration3" })
public class MobileSyncOutletRegistration3 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MobileSyncOutletRegistration3() {
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
		String mobile_transaction_no = Utilities.filterString(mr.getParameter("mobile_transaction_no"), 1, 200);
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
			
			
			int OutletRequestId = 0;
			
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
			
			String args = ""+CreatedBy+",'"+OutletRequestId+"', '"+OutletName+"', 1, '"+OutletAddress+"', '"+OwnerName+"', '"+OwnerContactNumber+"', '"+OwnerCNIC+"', "+ChannelID+", "+DistributorID+", "+CreatedBy+", '"+CreatedOn+"', 1, "+PJPID+", 1, "+city_id+", '"+mobileTimeStamp+"', '"+AreaLabel+"', "+
			"'"+SubAreaLabel+"', '"+PurchaserName+"', '"+PurchaserNumber+"', "+IsOwnerPurchaser+", "+Lat+", "+Lng+", "+Accuracy+", '"+DeviceUUID+"', '"+platform+"', "+day+","+mobile_transaction_no;
			System.out.println("INSERT INTO `common_outlets_request`(`request_by`,`outlet_id`,`outlet_name`,`shop_category`,`outlet_address`,`owner_name`,`owner_contact_number`,`owner_cnic_number`,`sub_channel_id`, `distributor_id`,`created_by`,`created_on`,`vpo_classifications_id`"+
			", `beat_plan_id`,`category_id`,`city_id`,`mobile_time_stamp`,`area_label`, `sub_area_label`, `purchaser_name`, `purchaser_number`, `is_owner_purchaser`, `lat`, `lng`, `accuracy`, `device_id`, `platform`,`day`,`mobile_transaction_no`)VALUES("+args+")");
//			
			
			s.executeUpdate("INSERT INTO `common_outlets_request`(`request_by`,`outlet_id`,`outlet_name`,`shop_category`,`outlet_address`,`owner_name`,`owner_contact_number`,`owner_cnic_number`,`sub_channel_id`, `distributor_id`,`created_by`,`created_on`,`vpo_classifications_id`"+
					", `beat_plan_id`,`category_id`,`city_id`,`mobile_time_stamp`,`area_label`, `sub_area_label`, `purchaser_name`, `purchaser_number`, `is_owner_purchaser`, `lat`, `lng`, `accuracy`, `device_id`, `platform`,`day`,`mobile_transaction_no`)VALUES("+args+")");
			
			// OutletRequestId
			
			
			
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
