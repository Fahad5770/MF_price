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

/**
 * Servlet implementation class SyncOrders
 */
@WebServlet(description = "Mobile Sync Tasks", urlPatterns = { "/mobile/MobileSyncTasks" })
public class MobileSyncTasks extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileSyncTasks() {
        super();
        // TODO Auto-generated constructor stub
        
        
        
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		try{
			
		
		PrintWriter out = response.getWriter();
		
		
		//System.out.println(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		
		
		
		
		
		JSONObject json = new JSONObject();
		
		response.setContentType("json");
		
		if (!mr.isExpired()){
			
			int RegionID = Utilities.parseInt(mr.getParameter("RegionID")); 
			int OutletID = Utilities.parseInt(mr.getParameter("OutletID"));
			
			String OutletName = Utilities.filterString(mr.getParameter("OutletName"), 1, 100);
			String OutletAddress = Utilities.filterString(mr.getParameter("OutletAddress"), 1, 100);
			String ContactNo = Utilities.filterString(mr.getParameter("ContactNo"), 1, 100);
			String OutletContactPerson = Utilities.filterString(mr.getParameter("OutletContactPerson"), 1, 100);
			String ComplaintType = Utilities.filterString(mr.getParameter("ComplaintType"), 1, 100);
			String Description = Utilities.filterString(mr.getParameter("Description"), 1, 100);
			int MobileUserID = Utilities.parseInt(mr.getParameter("UserID"));
			String Lat = Utilities.filterString(mr.getParameter("Lat"), 1, 100);
			String Lng = Utilities.filterString(mr.getParameter("Lng"), 1, 100);
			
			String DeviceUUID = Utilities.filterString(mr.getParameter("DeviceUUID"), 1, 100);
			//String DevicePlatformVersion = Utilities.filterString(mr.getParameter("DevicePlatformVersion"), 1, 100);
			 
			String MobileTimestamp = Utilities.filterString(mr.getParameter("MobileTimestamp"), 1, 100);
			String ComplaintID = Utilities.filterString(mr.getParameter("ComplaintID"), 1, 100);
			int IsResolved = Utilities.parseInt(mr.getParameter("is_resolved"));
			
			ArrayList TasksTypeList = new ArrayList();
			
			int MerchandisingInvoicesNotGiven = Utilities.parseInt(mr.getParameter("MerchandisingInvoicesNotGiven"));
			int MerchandisingPcsg = Utilities.parseInt(mr.getParameter("MerchandisingPcsg"));
			int MerchandisingPcsc = Utilities.parseInt(mr.getParameter("MerchandisingPcsc"));
			int MerchandisingNonPbclJuices = Utilities.parseInt(mr.getParameter("MerchandisingNonPbclJuices"));
			int MerchandisingNonPbclWater = Utilities.parseInt(mr.getParameter("MerchandisingNonPbclWater"));
			int MerchandisingBbrands = Utilities.parseInt(mr.getParameter("MerchandisingBbrands"));
			int MerchandisingMisuse = Utilities.parseInt(mr.getParameter("MerchandisingMisuse"));
			int MerchandisingOutOfStock = Utilities.parseInt(mr.getParameter("MerchandisingOutOfStock"));
			int MerchandisingOtherDrinks = Utilities.parseInt(mr.getParameter("MerchandisingOtherDrinks"));
			int MerchandisingLowStock = Utilities.parseInt(mr.getParameter("MerchandisingLowStock"));
			
			
			int OthersSupplyIssue = Utilities.parseInt(mr.getParameter("OthersSupplyIssue"));
			int OthersSamplingProblem = Utilities.parseInt(mr.getParameter("OthersSamplingProblem"));
			int OthersAdvanceProblem = Utilities.parseInt(mr.getParameter("OthersAdvanceProblem"));
			int OthersBrandShortage = Utilities.parseInt(mr.getParameter("OthersBrandShortage"));
			int OthersRequestingAdditionalTool = Utilities.parseInt(mr.getParameter("OthersRequestingAdditionalTool"));
			int OthersOtherIssues = Utilities.parseInt(mr.getParameter("OthersOtherIssues"));
			int OthersLeakageNotLifted = Utilities.parseInt(mr.getParameter("OthersLeakageNotLifted"));
			int OthersRateIssues = Utilities.parseInt(mr.getParameter("OthersRateIssues"));
			
			int TotCooling = Utilities.parseInt(mr.getParameter("TotCooling"));
			int TotLeakage = Utilities.parseInt(mr.getParameter("TotLeakage"));
			int TotDoor = Utilities.parseInt(mr.getParameter("TotDoor"));
			int TotDead = Utilities.parseInt(mr.getParameter("TotDead"));
			int TotCompressor = Utilities.parseInt(mr.getParameter("TotCompressor"));
			int TotOtherIssues = Utilities.parseInt(mr.getParameter("TotOtherIssues"));
			
			
			
			
			
			if( MerchandisingPcsg == 1){
				TasksTypeList.add(1);
			}
			if( MerchandisingPcsc == 1){
				TasksTypeList.add(2);
			}
			if( MerchandisingNonPbclJuices == 1){
				TasksTypeList.add(3);
			}
			if( MerchandisingNonPbclWater == 1){
				TasksTypeList.add(4);
			}
			if( MerchandisingBbrands == 1){
				TasksTypeList.add(5);
			}
			if( MerchandisingMisuse == 1){
				TasksTypeList.add(6);
			}
			if( MerchandisingOutOfStock == 1){
				TasksTypeList.add(7);
			}
			if( MerchandisingOtherDrinks == 1){
				TasksTypeList.add(8);
			}
			if( MerchandisingLowStock == 1){
				TasksTypeList.add(24);
			}
			
			
			if( OthersSupplyIssue == 1){
				TasksTypeList.add(9);
			}
			if( OthersSamplingProblem == 1){
				TasksTypeList.add(10);
			}
			if( OthersAdvanceProblem == 1){
				TasksTypeList.add(11);
			}
			if( OthersBrandShortage == 1){
				TasksTypeList.add(12);
			}
			if( OthersRequestingAdditionalTool == 1){
				TasksTypeList.add(13);
			}
			if( OthersOtherIssues == 1){
				TasksTypeList.add(14);
			}
			if( OthersLeakageNotLifted == 1){
				TasksTypeList.add(22);
			}
			if( OthersRateIssues == 1){
				TasksTypeList.add(23);
			}
			
			
			if( TotCooling == 1){
				TasksTypeList.add(15);
			}
			if( TotLeakage == 1){
				TasksTypeList.add(16);
			}
			if( TotDoor == 1){
				TasksTypeList.add(17);
			}
			if( TotDead == 1){
				TasksTypeList.add(18);
			}
			if( TotCompressor == 1){
				TasksTypeList.add(19);
			}
			if( TotOtherIssues == 1){
				TasksTypeList.add(20);
			}
			
			if( MerchandisingInvoicesNotGiven == 1){
				TasksTypeList.add(21);
			}
			
			
			Datasource ds = new Datasource();
			
			try{
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				
				String OutletIDVal = "null";
				
				if(OutletID > 0){
					OutletIDVal = OutletID+"";
				}
				
				if( IsResolved == 0){
					//System.out.println( "INSERT INTO `crm_complaints`(`complaint_id`,`outlet_id`,`outlet_name`,`outlet_address`,`outlet_contact_no`,`created_on`,`created_by`,`type_id`,`description`,`lat`,`lng`,`created_on_mobile`, `outlet_contact_person`)VALUES("+ComplaintID+","+OutletIDVal+",'"+OutletName+"','"+OutletAddress+"','"+ContactNo+"',now(),"+MobileUserID+","+ComplaintType+",'"+Description+"',"+Lat+","+Lng+",'"+MobileTimestamp+"', '"+OutletContactPerson+"')" );
					
					s.executeUpdate("replace INTO `crm_tasks`(`mobile_task_id`,`outlet_id`,`outlet_name`,`outlet_address`,`outlet_contact_no`,`created_on`,`created_by`,`description`,`lat`,`lng`,`created_on_mobile`, `outlet_contact_person`, `mobile_uuid`, `region_id` )VALUES("+ComplaintID+","+OutletIDVal+",'"+OutletName+"','"+OutletAddress+"','"+ContactNo+"',now(),"+MobileUserID+", '"+Description+"',"+Lat+","+Lng+",'"+MobileTimestamp+"', '"+OutletContactPerson+"', '"+DeviceUUID+"', "+RegionID+" ) "); 
					
					int TasksID = 0;
					ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
					if( rs.first() ){
						TasksID = rs.getInt(1);
						
						for(int i = 0; i < TasksTypeList.size(); i++ ){ 
							s2.executeUpdate("replace into crm_tasks_list (id, type_id) values ("+TasksID+", "+TasksTypeList.get(i)+") ");
						}
					}
					
					
				}else{ 
					//System.out.println( "update crm_tasks set resolved_description='"+Description+"', resolved_on=now(), resolved_by="+MobileUserID+" , is_resolved=1, resolved_on_mobile='"+MobileTimestamp+"', `resolved_by_uuid`='"+DeviceUUID+"' where id='"+ComplaintID+"' " );
					s.executeUpdate("update crm_tasks set resolved_description='"+Description+"', resolved_on=now(), resolved_by="+MobileUserID+" , is_resolved=1, is_sent = 0, resolved_on_mobile='"+MobileTimestamp+"', `resolved_by_uuid`='"+DeviceUUID+"' where id='"+ComplaintID+"' ");
					//fowardToWhatsApp(ComplaintID);
				}
				
				s2.close();
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
