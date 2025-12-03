package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
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
@WebServlet(description = "Mobile Sync Complaints", urlPatterns = { "/mobile/MobileSyncComplaints" })
public class MobileSyncComplaints extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileSyncComplaints() {
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
		
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		System.out.println(mr.URL);
		
		
		JSONObject json = new JSONObject();
		
		response.setContentType("json");
		
		if (!mr.isExpired()){
			
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
			
			Datasource ds = new Datasource();
			
			try{
				
				ds.createConnection();
				Statement s = ds.createStatement();
				
				String OutletIDVal = "null";
				
				if(OutletID > 0){
					OutletIDVal = OutletID+"";
				}
				
				
				if( IsResolved == 0){
					//System.out.println( "INSERT INTO `crm_complaints`(`complaint_id`,`outlet_id`,`outlet_name`,`outlet_address`,`outlet_contact_no`,`created_on`,`created_by`,`type_id`,`description`,`lat`,`lng`,`created_on_mobile`, `outlet_contact_person`)VALUES("+ComplaintID+","+OutletIDVal+",'"+OutletName+"','"+OutletAddress+"','"+ContactNo+"',now(),"+MobileUserID+","+ComplaintType+",'"+Description+"',"+Lat+","+Lng+",'"+MobileTimestamp+"', '"+OutletContactPerson+"')" );
					
					s.executeUpdate("replace INTO `crm_complaints`(`complaint_id`,`outlet_id`,`outlet_name`,`outlet_address`,`outlet_contact_no`,`created_on`,`created_by`,`type_id`,`description`,`lat`,`lng`,`created_on_mobile`, `outlet_contact_person`, `uuid`)VALUES("+ComplaintID+","+OutletIDVal+",'"+OutletName+"','"+OutletAddress+"','"+ContactNo+"',now(),"+MobileUserID+","+ComplaintType+",'"+Description+"',"+Lat+","+Lng+",'"+MobileTimestamp+"', '"+OutletContactPerson+"', '"+DeviceUUID+"')");
				}else{
					//System.out.println( "update crm_complaints set resolved_description='"+Description+"', resolved_on=now(), resolved_by="+MobileUserID+" , is_resolved=1 where complaint_id='"+ComplaintID+"' " );
					s.executeUpdate("update crm_complaints set resolved_description='"+Description+"', resolved_on=now(), resolved_by="+MobileUserID+" , is_resolved=1, resolved_on_mobile='"+MobileTimestamp+"', `resolved_by_uuid`='"+DeviceUUID+"' where id='"+ComplaintID+"' ");
				}
				
				s.close();
				
				json.put("success", "true");
				
			}catch(Exception e){
				
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
