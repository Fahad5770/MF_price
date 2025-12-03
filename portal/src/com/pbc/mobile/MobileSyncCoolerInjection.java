package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.Blob;
import com.pbc.inventory.Product;
import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.workflow.Workflow;

/**
 * Servlet implementation class SyncOrders
 */
@WebServlet(description = "Mobile Sync Cooler Injection", urlPatterns = { "/mobile/MobileSyncCoolerInjection" })
public class MobileSyncCoolerInjection extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileSyncCoolerInjection() {
        super();
        // TODO Auto-generated constructor stub
        
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("service() ...");
		
		try{
			
		
		PrintWriter out = response.getWriter();
		
		
		//System.out.println(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		
		
		JSONObject json = new JSONObject();
		
		response.setContentType("json");
		
		if (!mr.isExpired()){
			
			int IsNewOutlet = Utilities.parseInt(mr.getParameter("IsNewOutlet"));
			int OutletID = Utilities.parseInt(mr.getParameter("OutletID"));			
			String OutletName = Utilities.filterString(mr.getParameter("OutletName"), 1, 100);
			String OutletAddress = Utilities.filterString(mr.getParameter("OutletAddress"), 1, 100);
			String ContactNo = Utilities.filterString(mr.getParameter("ContactNo"), 1, 100);
			String OutletContactName = Utilities.filterString(mr.getParameter("OutletContactName"), 1, 100);
			String CNIC = Utilities.filterString(mr.getParameter("CNIC"), 1, 100);
			String Channel = Utilities.filterString(mr.getParameter("Channel"), 1, 100);
			String MeterNumber = Utilities.filterString(mr.getParameter("MeterNumber"), 1, 100);
			String Region = Utilities.filterString(mr.getParameter("Region"), 1, 100);
			String DistributorID = Utilities.filterString(mr.getParameter("DistributorID"), 1, 100);

			String BeatPlan[] = Utilities.filterString(mr.getParameterValues("BeatPlan"), 1, 100);
			
			String EmptyStatus = Utilities.filterString(mr.getParameter("EmptyStatus"), 1, 100);
			String ExistingTotSize = Utilities.filterString(mr.getParameter("ExistingTotSize"), 1, 100);
			String TotCode = Utilities.filterString(mr.getParameter("TotCode"), 1, 100);
			
			String TOTSizeID[] = Utilities.filterString(mr.getParameterValues("TOTSizeID"), 1, 100);
			String TOTSize[] = Utilities.filterString(mr.getParameterValues("TOTSize"), 1, 100);
			String TOTSizeStatus[] = Utilities.filterString(mr.getParameterValues("TOTSizeStatus"), 1, 100);
			
			int MobileUserID = Utilities.parseInt(mr.getParameter("MobileUserID"));
			String Lat = Utilities.filterString(mr.getParameter("Lat"), 1, 100);
			String Lng = Utilities.filterString(mr.getParameter("Lng"), 1, 100);
			
			String DeviceUUID = Utilities.filterString(mr.getParameter("DeviceUUID"), 1, 100);
			//String DevicePlatformVersion = Utilities.filterString(mr.getParameter("DevicePlatformVersion"), 1, 100);
			 
			String MobileTimestamp = Utilities.filterString(mr.getParameter("MobileTimestamp"), 1, 100);
			String CoolerInjectionID = Utilities.filterString(mr.getParameter("CoolerInjectionID"), 1, 100);
			
			String LRBTypeID[] = Utilities.filterString(mr.getParameterValues("LRBTypeID"), 1, 100);
			String PackageID[] = Utilities.filterString(mr.getParameterValues("PackageID"), 1, 100);
			String Qty[] = Utilities.filterString(mr.getParameterValues("Qty"), 1, 100);
			
			
			System.out.println("IsNewOutlet = "+IsNewOutlet);
			
			json.put("success", "true");
			
			Datasource ds = new Datasource();
			
			try{
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				
				String OutletIDVal = "null";
				
				if(OutletID > 0){
					OutletIDVal = OutletID+"";
				}
				
				String ChannelStr = Channel;
				String RegionStr = Region;
				String DistributorIDStr = DistributorID;
				if(IsNewOutlet == 0){
					ChannelStr = "0";
					RegionStr = "0";
					DistributorIDStr = "0";
				}else{
					if(Channel.equals("")){
						ChannelStr = "0";
					}
					
					if(Region.equals("")){
						RegionStr = "0";
					}
					
					if(DistributorID.equals("")){
						DistributorIDStr = "0";
					}
				}
				
				
				String Sql = "INSERT INTO `tot_issue_request`(`outlet_id`,`outlet_name`,`outlet_address`,`outlet_contact_number`,`outlet_contact_name`,`outlet_contact_cnic`,`outlet_channel`,`outlet_region`,`outlet_distributor_id`,`tot_empty_status`,`existing_tot`,`tot_code`,`created_on`,`created_by`,`request_id`,`uvid`,`is_active`,`meter_number`,`is_new_outlet`,`asset_number`)VALUES("+OutletIDVal+",'"+OutletName+"','"+OutletAddress+"','"+ContactNo+"','"+OutletContactName+"','"+CNIC+"',"+ChannelStr+",'"+RegionStr+"','"+DistributorIDStr+"','"+EmptyStatus+"','"+ExistingTotSize+"','"+TotCode+"',now(),"+MobileUserID+","+0/*request_id*/+","+CoolerInjectionID+","+0/*is_active*/+","+0/*meter_number*/+",'"+IsNewOutlet+"',"+0/*asset_number*/+")";
				System.out.println(Sql);
				s.executeUpdate(Sql);
				
				long LastDBID = 0;
				ResultSet rs = s.executeQuery("select last_insert_id()");
				if(rs.first()){
					LastDBID = rs.getLong(1);
				}
				
				if(BeatPlan != null){
					for(int i = 0; i < BeatPlan.length; i++){
						if(!BeatPlan[i].equals("false")){
							s.executeUpdate("INSERT INTO `tot_issue_request_beat_plans`(`id`,`day_number`)VALUES("+LastDBID+", "+BeatPlan[i]+")");
						}						
					}
				}
				
				if(TOTSizeID != null){
					for(int i = 0; i < TOTSizeID.length; i++){
						if(TOTSize[i] != null && !TOTSize[i].equals("null")){
							System.out.println("INSERT INTO `tot_issue_request_sizes`(`id`,`tot_size_id`,`quantity`,`type_id`)VALUES("+LastDBID+","+TOTSizeID[i]+","+TOTSize[i]+", "+TOTSizeStatus[i]+")");
							s.executeUpdate("INSERT INTO `tot_issue_request_sizes`(`id`,`tot_size_id`,`quantity`,`type_id`)VALUES("+LastDBID+","+TOTSizeID[i]+","+TOTSize[i]+", "+TOTSizeStatus[i]+")");
						}
						
					}
				}
				
				if(LRBTypeID != null){
					for(int i = 0; i < LRBTypeID.length; i++){
						s.executeUpdate("INSERT INTO `tot_issue_request_sales`(`id`,`type`,`package_id`,`quantity`)VALUES("+LastDBID+","+LRBTypeID[i]+","+PackageID[i]+","+Qty[i]+")");
					}
				}
				
				
				long ProcessUserID = 0;
				ResultSet rs1 = s.executeQuery("select * from workflow_processes_steps where step_id=2 and process_id=6");
				
				if(rs1.first()){
					ProcessUserID = rs1.getLong("user_id");
				}
				
				
				Workflow wf = new Workflow();
				long WorkFlowRequestID = wf.createRequest(6, MobileUserID, ProcessUserID, 5, "Cooler Injection Request Raised");
				s.executeUpdate("update tot_issue_request set request_id="+WorkFlowRequestID+" where id="+LastDBID);
	
				
				
				
				
				s.close();
				
				ds.commit();
				
				json.put("success", "true");
				
				
			}catch(Exception e){
				
				
				ds.rollback();
				
				e.printStackTrace();
				//System.out.print(e);
				json.put("success", "false");
				json.put("error_code", "106");
				
			}finally{
				
				try {
					ds.dropConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			
		}else{
			json.put("success", "false");
			json.put("error_code", "101");
		}
		
		
		out.print(json);
		}catch(Exception e){e.printStackTrace();}
	}
	
	
}
