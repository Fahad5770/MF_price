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

@WebServlet(description = "Mobile MDE MobileSyncOutletRegistrationTest", urlPatterns = { "/mobile/MobileSyncOutletRegistrationTest" })
public class MobileSyncOutletRegistrationTest extends HttpServlet {  
	private static final long serialVersionUID = 1L;

	public MobileSyncOutletRegistrationTest() {
		super();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException { 
		response.setContentType("json");

		System.out.println("MobileOutletRegisterationV8");

		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();

		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 3000));

		String UVID = Utilities.filterString(mr.getParameter("outlet_uvid"), 1, 200);
		String OutletName = Utilities.filterString(mr.getParameter("outlet_name"), 1, 200);
		String OutletAddress = Utilities.filterString(mr.getParameter("outlet_address"), 1, 200);
		String OutletNearContact = Utilities.filterString(mr.getParameter("outlet_near_contact"), 1, 200);
		String OutletContactPerson = Utilities.filterString(mr.getParameter("outlet_contact_person"), 1, 200);
		String OutletNIC = Utilities.filterString(mr.getParameter("outlet_contact_nic"), 1, 200);
		String OutletContactNumber = Utilities.filterString(mr.getParameter("outlet_contact_number"), 1, 200);
		int RegionID = Utilities.parseInt(mr.getParameter("outlet_region_id"));
		int CallCenterRequestID = Utilities.parseInt(mr.getParameter("CallCenterRequestID"));
		String CallCenterReqCreatedOn = Utilities.filterString(mr.getParameter("CallCenterReqCreatedOn"), 1, 200);
		int CallCenterAgentId = Utilities.parseInt(mr.getParameter("CallCenterAgentId"));
		
		int isCallCenterRequest = Utilities.parseInt(mr.getParameter("isCallCenterRequest"));
		System.out.println(">>>>>>>>>>>"+isCallCenterRequest);
		System.out.println("CallCenterRequestID>>>>>>>>>>>"+CallCenterRequestID);
		
		String CityName = Utilities.filterString(mr.getParameter("city"), 1, 200);
		
		int CityID = 0;
		int TypeID = Utilities.parseInt(mr.getParameter("outlet_type"));
		int CategoryID = Utilities.parseInt(mr.getParameter("outlet_category"));
		int ChannelID = Utilities.parseInt(mr.getParameter("outlet_channel"));
		int HandlerID = Utilities.parseInt(mr.getParameter("outlet_handler"));
		int PJPID = Utilities.parseInt(mr.getParameter("outlet_pjp"));
		String CreatedOn = Utilities.filterString(mr.getParameter("outlet_created_on"), 1, 200);
		long CreatedBy = Utilities.parseLong(mr.getParameter("created_by"));
		int MainChannelID = Utilities.parseInt(mr.getParameter("outlet_main_channel_id"));
		boolean ByPassGeoLocation=Utilities.parseBoolean(mr.getParameter("is_bypass_geo_loc"));
		
		String ContactPersonNumber = Utilities.filterString(mr.getParameter("contact_person_number"), 1, 200);
		String OwnerContactNumber = Utilities.filterString(mr.getParameter("owner_contact_number"), 1, 200);
		String ContactType = Utilities.filterString(mr.getParameter("contact_type"), 1, 200);
		String IsFiler = Utilities.filterString(mr.getParameter("is_filer"), 1, 200);
		String NTN = Utilities.filterString(mr.getParameter("ntn"), 1, 200);
		String STN = Utilities.filterString(mr.getParameter("stn"), 1, 200);
		
		System.out.println("Geo Loction"+ByPassGeoLocation);

		int OutletStatus = Utilities.parseInt(mr.getParameter("OStatus"));

		long DistributorID = Utilities.parseLong(mr.getParameter("outlet_distributor"));
		double Lat = Utilities.parseDouble(mr.getParameter("outlet_lat"));
		double Lng = Utilities.parseDouble(mr.getParameter("outlet_lng"));
		double Accuracy = Utilities.parseDouble(mr.getParameter("outlet_accuracy"));

	
	
		int VPO = Utilities.parseInt(mr.getParameter("vpo"));

		boolean isFullEditCase = Utilities.parseBoolean(mr.getParameter("isFullEditCase"));
		int MrdHandlerID = Utilities.parseInt(mr.getParameter("mrd_ref"));
		

		System.out.println(" =  isFullEditCase  "+isFullEditCase);

		String Owner = Utilities.filterString(mr.getParameter("owner"), 1, 200);
		String Email = Utilities.filterString(mr.getParameter("email"), 1, 200);

		int ShopActive = Utilities.parseInt(mr.getParameter("is_shop_active"));
		int InActiveReason = Utilities.parseInt(mr.getParameter("in_active_reason"));
		System.out.println("InActiveReason>>>>>>>>>>>>>>>>>>>"+InActiveReason);
		int BulkWaterCategoryID = Utilities.parseInt(mr.getParameter("bulk_water_category"));
		int SubAreaID = Utilities.parseInt(mr.getParameter("sub_area"));
		int FrequencyWeek = Utilities.parseInt(mr.getParameter("frequency_week"));
		int EstimatedSale = Utilities.parseInt(mr.getParameter("estimate_sale"));
		int OutletAccountType=Utilities.parseInt(mr.getParameter("account_type"));
		int CustomerOriginID=1;// Sales
		
		String [] IsOwnerContact=mr.getParameterValues("is_owner_contact");
		String[] days = mr.getParameterValues("day_number");
		String[] outletTypes = mr.getParameterValues("outlet_multiple_type");
		String[] mobileContacts = mr.getParameterValues("outlet_multiple_contact");
		if(mobileContacts != null) {
			System.out.println("---- mobileContacts len = "+mobileContacts.length);
		}
		String[] mobileContactsPrimary = mr.getParameterValues("is_primary_contact");
		
		long EditOutletID=Utilities.parseLong(mr.getParameter("EditOutletID"));
		int PJPChangeFlag=Utilities.parseInt(mr.getParameter("IsPJPChange"));

		
		int IsSameOwner=Utilities.parseInt(mr.getParameter("same_owner"));
		
		int RequestType = Utilities.parseInt(mr.getParameter("request_type"));
		
		long MarkedClosedOutletID=Utilities.parseLong(mr.getParameter("MarkCloseOutletID"));
		long RequestID=Utilities.parseLong(mr.getParameter("RequestID"));
		int IsMarkClose=Utilities.parseInt(mr.getParameter("IsMarkClosed"));
		String Remarks=Utilities.filterString(mr.getParameter("RemarksCloseOutletID"), 1, 200);
		String DeviceUUID = Utilities.filterString(mr.getParameter("device_UUID"), 1, 200);

		System.out.println(Remarks);
		if(Remarks==null || Remarks.isEmpty()) {
			Remarks="N/A";
		}
		System.out.println("----"+Remarks);
		//System.out.println(OutletName+"  -- "+ mr.getParameter("outlet_name"));
		//System.out.println("is filer "+IsFiler+" vpo "+VPO+ " ntn "+NTN  +" Email "+Email+" FrequencyWeek"+FrequencyWeek+" EstimateSale "+EstimateSale);

		if(mobileContacts!=null) {
			
			//System.out.println("days " + days.length+ "outletTypes "+outletTypes.length+"  mobileContacts "+mobileContacts.length);
		}
		//System.out.println(OutletNIC);
		Datasource ds = new Datasource();

		try {

			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();

			// overriding distributorID coming from mobile app on the base of pjp;
			ResultSet rs134 = s.executeQuery("select * from common_outlets_cities where label like '%"+CityName+"%'");
			if(rs134.first()) {
				CityID = rs134.getInt("id");
			}
			ResultSet rs12 = s3.executeQuery("select distributor_id from distributor_beat_plan where id=" + PJPID + " limit 1");
			if (rs12.first()) {

				DistributorID = rs12.getLong("distributor_id");
			}

			// overriding Region coming from mobile app on the base of distributor;

			

			
			if(RequestType==2 || RequestType==6) { //in case of sdm edit we don't need to update outelt name
				
				ResultSet rs34 = s.executeQuery("select name from common_outlets where id="+EditOutletID);
				if(rs34.first()) {
					OutletName = rs34.getString("name");
				}
			}
			
			
			
			ResultSet rsCheck = s.executeQuery("select * from common_outlets_request where owner_cnic_number='"+ OutletNIC + "' AND owner_contact_number='" + OutletContactNumber + "' AND outlet_name='"+OutletName+"' AND outlet_address='"+OutletAddress+"'");
			//if (!rsCheck.first()) {
			if (true) {
	
				
				String SQL="";
				String ShopActiveVal = null;
					if(ShopActive == 1) {
						ShopActiveVal = "1";
					}
					System.out.println("City_id: "+CityID+" Region_id: "+RegionID);
					System.out.println("accuracy>>>>>>>>>>>>>> "+ Accuracy);
					if(isCallCenterRequest==0) {
					SQL="insert into common_outlets_request(uvid,outlet_name,outlet_address,contact_person_num,owner_name,owner_contact_number,owner_cnic_number,distributor_id,created_by,created_on,sub_channel_id,beat_plan_id,category_id,outlet_near_place,handler_id,type_id,main_channel_id,lat,lng,accuracy,status,vpo_classifications_id,ntn,stn,contact_person,contact_type,is_filer,email,shop_active,sub_area_id,customer_origin_id,frequency_week,estimated_sale,account_type,app_version,is_same_owner,edit_outlet_id,is_pjp_change,outlet_request_id,is_request_approved,request_type,in_active_reason,is_bypass_geo_loc,contact_person_is_owner,device_uuid,is_full_edit_case, mrd_handler_id,city_id,region_id)values('"
									+ UVID + "','" + OutletName + "','" + OutletAddress + "','"+ContactPersonNumber+"','" + Owner + "','"+OwnerContactNumber+"','" + OutletNIC + "'," + DistributorID + "," + CreatedBy + ",now()," + ChannelID + "," + PJPID + "," + CategoryID + ",'"
									+ OutletNearContact + "'," + HandlerID + "," + TypeID + ","+ MainChannelID + "," + Lat + "," + Lng + "," + Accuracy + "," + OutletStatus + ","+VPO+",'"+NTN+"','"+STN+"','"+OutletContactPerson+"',"+ContactType+","+IsFiler+",'"+Email+"',"+ShopActiveVal+","+SubAreaID+","+CustomerOriginID+","+FrequencyWeek+","+EstimatedSale+","+OutletAccountType+",1,"+IsSameOwner+","+EditOutletID+","+PJPChangeFlag+","+RequestID+",2,"+RequestType+","+InActiveReason+","+ByPassGeoLocation+","+IsSameOwner+",'"+DeviceUUID+"',"+isFullEditCase+", "+MrdHandlerID+", "+CityID+", "+RegionID+")";
					}else {
						SQL="insert into common_outlets_request(uvid,outlet_name,outlet_address,contact_person_num,owner_name,owner_contact_number,owner_cnic_number,distributor_id,created_by,created_on,sub_channel_id,beat_plan_id,category_id,outlet_near_place,handler_id,type_id,main_channel_id,lat,lng,accuracy,status,vpo_classifications_id,ntn,stn,contact_person,contact_type,is_filer,email,shop_active,sub_area_id,customer_origin_id,frequency_week,estimated_sale,account_type,app_version,is_same_owner,edit_outlet_id,is_pjp_change,outlet_request_id,is_request_approved,request_type,in_active_reason,is_bypass_geo_loc,contact_person_is_owner,device_uuid,is_full_edit_case, mrd_handler_id,city_id,region_id,call_center_request_id)values('"
								+ UVID + "','" + OutletName + "','" + OutletAddress + "','"+ContactPersonNumber+"','" + Owner + "','"+OwnerContactNumber+"','" + OutletNIC + "'," + DistributorID + "," + CreatedBy + ",now()," + ChannelID + "," + PJPID + "," + CategoryID + ",'"
								+ OutletNearContact + "'," + HandlerID + "," + TypeID + ","+ MainChannelID + "," + Lat + "," + Lng + "," + Accuracy + "," + OutletStatus + ","+VPO+",'"+NTN+"','"+STN+"','"+OutletContactPerson+"',"+ContactType+","+IsFiler+",'"+Email+"',"+ShopActiveVal+","+SubAreaID+","+CustomerOriginID+","+FrequencyWeek+","+EstimatedSale+","+OutletAccountType+",1,"+IsSameOwner+","+EditOutletID+","+PJPChangeFlag+","+RequestID+",2,"+RequestType+","+InActiveReason+","+ByPassGeoLocation+","+IsSameOwner+",'"+DeviceUUID+"',"+isFullEditCase+", "+MrdHandlerID+", "+CityID+", "+RegionID+", "+CallCenterRequestID+")";
						
						
						s2.executeUpdate("update common_outlets_request  set is_sdm_approved=1 where outlet_id="+CallCenterRequestID);
						System.out.println("update common_outlets_request  set is_sdm_approved=1 where outlet_id="+CallCenterRequestID);
						
					}
				
				System.out.println(SQL);
				if(IsMarkClose==0 && MarkedClosedOutletID==0) {
					
					s.executeUpdate(SQL);
					
					
					

					long AutoIncrementID = 0;

					ResultSet rs2 = s1.executeQuery("select LAST_INSERT_ID()");
					if (rs2.first()) {
						AutoIncrementID = rs2.getInt(1);
					}
					
					if(ShopActive == 0) {
						
						long RSM_ID = 0; 
						ResultSet rs3 = s1.executeQuery("select rsm_id from common_distributors where distributor_id="+DistributorID);
						if (rs3.first()) {
							RSM_ID = rs3.getLong(1);
						}
						
						Workflow wf = new Workflow();
						long requestID = wf.createRequest(15, Integer.parseInt(CreatedBy+""), RSM_ID, 4, "Full Edit Inactive Request Raised");
						s1.executeUpdate("update common_outlets_request set request_id='"+requestID+"', request_by = "+CreatedBy+" where outlet_id="+AutoIncrementID);
					}

					if (days != null) {

						for (int i = 0; i < days.length; i++) {
							
							s2.executeUpdate("insert into common_outlets_request_pjp(pjp_id,outlet_id,day_number)values("
									+ PJPID + "," + AutoIncrementID + "," + days[i] + ")");
						}
					}
					if (outletTypes != null) {

						for (int i = 0; i < outletTypes.length; i++) {
							
							s2.executeUpdate("insert into common_outlets_request_types_details(outlet_id,type_id)values(" + AutoIncrementID + "," + outletTypes[i] + ")");
						}
					}
					
					if (mobileContacts != null) {

						for (int i = 0; i < mobileContacts.length; i++) {
							
							//if(IsSameOwner==1) {
								//s2.executeUpdate("insert into common_outlets_request_contacts_details(outlet_id,contact_number,is_owner_contact,is_primary_contact)values(" + AutoIncrementID + "," + mobileContacts[i] + ","+IsOwnerContact[i]+","+mobileContactsPrimary[i]+")");
							s2.executeUpdate("insert into common_outlets_request_contacts_details(outlet_id,contact_name,contact_number,is_owner_contact,is_primary_contact,contact_nic,type_id,contact_ntn,email,contact_type,contact_stn,owner_name)values(" + AutoIncrementID + ",'"+OutletContactPerson+"'," + mobileContacts[i] + ","+IsOwnerContact[i]+","+mobileContactsPrimary[i]+",'"+ OutletNIC +"',"+TypeID+",'"+NTN+"','"+Email+"','"+ContactType+"','"+STN+"','"+Owner+"')");
								// insert only one record in contact table if owner and contact person is same;
								//break;
							//}
						}
					}
					
				}else if(IsMarkClose==1 && MarkedClosedOutletID!=0){
					
					s2.executeUpdate("update common_outlets_request  set mark_close_outlet_id="+MarkedClosedOutletID+",remarks='"+Remarks+"',is_mark_closed=1,mark_closed_by="+CreatedBy+",mark_closed_on=now()  where outlet_id="+RequestID);
					
				}
				
				System.out.println("update common_outlets_request  set is_request_close=1 where outlet_id="+RequestID);

				s2.executeUpdate("update common_outlets_request  set is_request_close=1 where outlet_id="+RequestID);
				
				json.put("success", "true");
				json.put("uvid", UVID);
			}else {
				json.put("success", "true");
				json.put("uvid", UVID);
			}

			

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
