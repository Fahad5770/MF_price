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

import com.pbc.employee.OrderBookerDashboard;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.bi.BiProcesses;


@WebServlet(description = "Mobile Authenticate User", urlPatterns = { "/mobile/MobileAuthenticateUserSurvey" })
public class MobileAuthenticateUserSurvey extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MobileAuthenticateUserSurvey() {
        super();
        //System.out.println("Hello");
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("json");
		
		//System.out.println("Hello");
		
		
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		JSONArray jr = new JSONArray();
		JSONArray jr2 = new JSONArray();
		JSONArray jr3 = new JSONArray();
		
		JSONArray jr4 = new JSONArray();
		JSONArray jr5 = new JSONArray();
		JSONArray jr6 = new JSONArray();
		
		JSONArray jr7 = new JSONArray();
		JSONArray jr8 = new JSONArray();
		
		JSONArray jr9 = new JSONArray();
		JSONArray jr10 = new JSONArray();
		JSONArray jr11 = new JSONArray();
		
		
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 3000));
		
		if (!mr.isExpired()){
			
			long LoginUsername = Utilities.parseLong(mr.getParameter("LoginUsername"));
			String LoginPassword = Utilities.filterString(mr.getParameter("LoginPassword"), 1, 100);
			String DeviceID = Utilities.filterString(mr.getParameter("DeviceID"), 1, 200);
			
			int LogTypeID = 0;
			
			Datasource ds = new Datasource();
			
			try {
				
				ds.createConnection();
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				Statement s3 = ds.createStatement();
				Statement s4 = ds.createStatement();
				Statement s5 = ds.createStatement();
				Statement s6 = ds.createStatement();
				
				//ResultSet rsD = s.executeQuery("select id from mobile_devices where uuid = '"+DeviceID+"'");
				//if(rsD.first()){
				
				if(true){
					
				ResultSet rs = s.executeQuery("select md5('"+LoginPassword+"'), password, DISPLAY_NAME, ID, DESIGNATION, DEPARTMENT from users where ID="+LoginUsername+" and IS_ACTIVE=1 ");
				if(rs.first()){
				
					System.out.println(LoginPassword);
					
					if (rs.getString(1).equals(rs.getString(2)) || LoginPassword.equals(Utilities.getMobileAdminPassword())){
						
						LogTypeID = 4;
						
						
						
					json.put("success", "true");
					json.put("UserID", LoginUsername);
					json.put("DisplayName", rs.getString("DISPLAY_NAME"));
					json.put("Designation", rs.getString("DESIGNATION"));
					json.put("Department", rs.getString("DEPARTMENT"));
					
					
					// Patch for PJP implementation
					
					json.put("BeatPlanID", 1);
					
					ResultSet rs3 = s3.executeQuery("SELECT * FROM mrd_census_channel");
					while(rs3.next()){
						
						LinkedHashMap rows = new LinkedHashMap();
						rows.put("BusinessTypeID", rs3.getString("id"));
						rows.put("BusinessTypeName", rs3.getString("label"));
						
						jr.add(rows);
						
					}
					
					ResultSet rs4 = s3.executeQuery("SELECT * FROM mrd_census_sub_channel");
					while(rs4.next()){
						
						LinkedHashMap rows = new LinkedHashMap();
						rows.put("SubChannelID", rs4.getString("id"));
						rows.put("ParentChannelID", rs4.getString("parent_channel_id"));
						rows.put("SubChannelName", rs4.getString("label"));
						
						jr2.add(rows);
						
					}
					
					json.put("BusinessTypeRows", jr);
					json.put("SubChannelRows", jr2);
					
					
					
					

					ResultSet rs5 = s5.executeQuery("SELECT * FROM mrd_census_district");
					while(rs5.next()){
						
						LinkedHashMap rows = new LinkedHashMap();
						rows.put("DistID", rs5.getString("id"));
						rows.put("DistName", rs5.getString("label"));
						System.out.println(rs5.getString("id")+"-"+rs5.getString("label"));
						
						jr5.add(rows);
						
					}
					
					ResultSet rs6 = s6.executeQuery("SELECT * FROM mrd_census_tehsil");
					while(rs6.next()){
						
						LinkedHashMap rows = new LinkedHashMap();
						rows.put("TehsilID", rs6.getString("id"));
						rows.put("TehsilName", rs6.getString("label"));
						System.out.println(rs6.getString("id")+"-"+rs6.getString("label"));
						jr6.add(rows);
						
					}
					json.put("DistrictArray", jr5);
					json.put("TehsilArray", jr6);
					
					
					
					}else{
						LogTypeID = 7;
						json.put("success", "false");
						json.put("error_code", "104");
					}
					
				}else{
					LogTypeID = 5;
					json.put("success", "false");
					json.put("error_code", "103");
				}
				
				}else{
					LogTypeID = 6;
					json.put("success", "false");
					json.put("error_code", "102");
				}
				
				if (LogTypeID != 0){
					s2.executeUpdate("insert into "+ds.logDatabaseName()+".log_user_login(user_id,password, ip_address, attempted_on,type_id, mobile_uuid) values("+LoginUsername+",'','"+request.getRemoteAddr()+"',now(),"+LogTypeID+",'"+DeviceID+"')");
					
				}
				
				s4.close();
				s3.close();
				s2.close();
				s.close();
				ds.dropConnection();
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			json.put("success", "false");
			json.put("error_code", "101");
		}
		
		out.print(json);
		out.close();
		
	}
	
}
