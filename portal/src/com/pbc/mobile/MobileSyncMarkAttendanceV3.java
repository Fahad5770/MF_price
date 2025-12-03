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

import com.mf.utils.MFAPIFunctions;
import com.mysql.jdbc.Blob;
import com.pbc.inventory.Product;
import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class SyncOrders
 */
@WebServlet(description = "Mobile Sync Mark Attendance", urlPatterns = { "/mobile/MobileSyncMarkAttendanceV3" })
public class MobileSyncMarkAttendanceV3 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileSyncMarkAttendanceV3() {
        super();
        // TODO Auto-generated constructor stub
        
        System.out.println("MobileSyncMarkAttendanceV3()");
        
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("MobileSyncMarkAttendanceV3 without distribution location Feature");
		
		try{
			
		
		PrintWriter out = response.getWriter();
		
		
		//System.out.println(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		
		
		JSONObject json = new JSONObject();
		
		response.setContentType("json");
		
		if (!mr.isExpired()){
			
			
			String uuidID = Utilities.filterString(mr.getParameter("AttendanceID"), 1, 100);
			int AttendanceType = Utilities.parseInt(mr.getParameter("TyepID"));
			int MobileUserID = Utilities.parseInt(mr.getParameter("UserID"));
			String Lat = Utilities.filterString(mr.getParameter("LAT"), 1, 100);
			String Lng = Utilities.filterString(mr.getParameter("LNG"), 1, 100);
			String Accurracy = Utilities.filterString(mr.getParameter("Accu"), 1, 100);
			String MobileTimestamp = Utilities.filterString(mr.getParameter("MobileTimestamp"), 1, 100);
			String Deviceid = Utilities.filterString(mr.getParameter("UUID"), 1, 100);
			String platform = Utilities.filterString(mr.getParameter("DevicePlatformVersion"), 1, 100);
			
			
			Datasource ds = new Datasource();
			
			try{
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				Statement s3 = ds.createStatement();
				
				boolean IsValidDeviceID=true;
				
//				ResultSet rsD = s.executeQuery("select id from mobile_devices where uuid = '"+Deviceid+"' and is_active=1");
//				if(rsD.first()){
//					IsValidDeviceID=true;
//				}else {
//					IsValidDeviceID=false;
//				}
				
				if(IsValidDeviceID) {
				
				
						long CalculatedDistance=0;
						
						//Checking Distance within 100 meters - Patch by Zulqurnan on 03/04/2019
						
					
						//Getting Distributor
					
						/********************************************************* Old logic *************************** changed on 10/02/2020
						 * String DistributorID="";
					
					
					
					
						ResultSet rs2 = s3.executeQuery("SELECT distinct distributor_id FROM distributor_beat_plan_view where assigned_to="+MobileUserID);
						if(rs2.first()) {
							DistributorID = rs2.getString("distributor_id");
							
							System.out.println("Orderbooker Case ======>"+"SELECT distinct distributor_id FROM distributor_beat_plan_view where assigned_to="+MobileUserID);
						}
						
						
						//if distributor=0 then check for KPO. If user is secondary then he is KPO
						if(DistributorID=="" || DistributorID==null) { 
							ResultSet rs21 = s3.executeQuery("SELECT distributor_id FROM pep.users where id="+MobileUserID+" and  type_id=2");
							if(rs21.first()) {
								DistributorID = rs21.getString("distributor_id");
							}
							
							System.out.println("KPO Case =====>"+"SELECT distributor_id FROM pep.users where id="+MobileUserID+" and  type_id=2");
						}
					
						if(DistributorID=="" || DistributorID==null) { //for mde
							int i=0;
							ResultSet rs21 = s3.executeQuery("SELECT distinct distributor_id FROM distributor_beat_plan_view where asm_id="+MobileUserID);
							while(rs21.next()) {
													
									if(i == 0){
										DistributorID = rs21.getString("distributor_id");	
									}else{
										DistributorID += ","+rs21.getString("distributor_id");	
									}
							i++;	
							}
							
							System.out.println("MDE Case =====>"+"SELECT distinct distributor_id FROM distributor_beat_plan_view where asm_id="+MobileUserID);
						}
						
						*****************************************************************************************
						*
						*/
						
						/*********** New logic **********/
						
						
						/*String Query = "select group_concat(distributor_id) from ( "+
								"SELECT  group_concat(distinct distributor_id) as distributor_id FROM distributor_beat_plan_view where assigned_to="+MobileUserID+
								" union "+
								" SELECT distributor_id FROM pep.users where id="+MobileUserID+" and  type_id=2 "+
								" union "+
								" SELECT group_concat(distinct distributor_id) as distributor_id FROM distributor_beat_plan_view where asm_id="+MobileUserID+") as t";*/
						
						String DistributorIds = MFAPIFunctions.GetDistributorsOfUser(MobileUserID);
						
						
						
						/**********************************/
					
					
						int Flag=0;
						int counter=0;
						
						System.out.println("Location Query ====>"+"SELECT * FROM common_distributor_location where distributor_id in("+DistributorIds+")");
						ResultSet rs = s3.executeQuery("SELECT * FROM common_distributor_location where distributor_id in("+DistributorIds+")");
						while(rs.first() && counter==0) {
							CalculatedDistance = GetDistance(rs.getString("lat"),rs.getString("lng"),Lat,Lng);
							
							
							if(CalculatedDistance<100) {
								Flag=1;
								counter=10;// just to break the loop
								//break;
								
								
							}
						}
										
						
						
						if(Flag==1) {
							
							
							s.executeUpdate("insert into mobile_order_booker_attendance (uuid,  created_on, created_by, lat, lng,accuracy, mobile_timestamp,attendance_type, device_id, platform) values "+
									"("+uuidID+",  now(), "+MobileUserID+", "+Lat+", "+Lng+","+Accurracy+", '"+MobileTimestamp+"',"+AttendanceType+",'"+Deviceid+"','"+platform+"')");
												
							s3.close();
							s2.close();
							s.close();
							ds.commit();
							//System.out.println("Marked");
							json.put("success", "true");
						}else if(Flag==0) {
							
							System.out.println("ERROR");
							json.put("success", "false");
							json.put("error", "Your attendance could not be marked because your distance is too far from the office - Distance is "+CalculatedDistance);
							
						}
						
						/////////////////////////////////////////////////////////////////////////
						
					/*
					 * 
					 * 
					 * Previous Code for Attendence used by PBC	
						System.out.println("insert into mobile_order_booker_attendance (uuid,  created_on, created_by, lat, lng,accuracy, mobile_timestamp,attendance_type, device_id, platform) values "+
								"("+uuidID+",  now(), "+MobileUserID+", "+Lat+", "+Lng+","+Accurracy+", '"+MobileTimestamp+"',"+AttendanceType+",'"+Deviceid+"','"+platform+"')");
						
						s.executeUpdate("insert into mobile_order_booker_attendance (uuid,  created_on, created_by, lat, lng,accuracy, mobile_timestamp,attendance_type, device_id, platform) values "+
								"("+uuidID+",  now(), "+MobileUserID+", "+Lat+", "+Lng+","+Accurracy+", '"+MobileTimestamp+"',"+AttendanceType+",'"+Deviceid+"','"+platform+"')");
						
						
						
						s2.close();
						s.close();
						json.put("success", "true");
						*
						*/
			
			}else {
				json.put("success", "false");
				json.put("error_code", "102 - Device Inactive");
			}
				
				
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
	
	
	protected long GetDistance(String lat1,String lng1, String lat2, String lng2) {
		
		
		Datasource ds = new Datasource();
		
		long Distance=0;
		
		try{
			
			
			ds.createConnection();
			
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			System.out.println("Attendance Distance Query ============>"+"SELECT (( 3959 * acos( cos( radians('"+lat1+"') ) * cos( radians( '"+lat2+"' ) ) * cos( radians( '"+lng2+"' ) - radians('"+lng1+"') ) + sin ( radians('"+lat1+"') )  * sin( radians( '"+lat2+"' ) ) ) ) * 1609.34 ) AS distance");
			
			ResultSet rs = s.executeQuery("SELECT (( 3959 * acos( cos( radians('"+lat1+"') ) * cos( radians( '"+lat2+"' ) ) * cos( radians( '"+lng2+"' ) - radians('"+lng1+"') ) + sin ( radians('"+lat1+"') )  * sin( radians( '"+lat2+"' ) ) ) ) * 1609.34 ) AS distance");
			if(rs.first()) {
				Distance = rs.getLong("distance");
			}
			
			
			System.out.println("Attendance Distance ==============>"+Distance);
			
		}catch(Exception e){
			
					
		}
		return Distance;
	}
	
	
}
