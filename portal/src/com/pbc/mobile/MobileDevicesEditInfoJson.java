package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;


@WebServlet(description = "Mobile Devices Edit Info Json", urlPatterns = { "/mobile/MobileDevicesEditInfoJson" })
public class MobileDevicesEditInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MobileDevicesEditInfoJson() {
        super();
    }

    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		int EditID = Utilities.parseInt(request.getParameter("EditID"));
		
		Datasource ds = new Datasource();
		JSONObject json = new JSONObject();
		
		try { 
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT * FROM mobile_devices where id="+EditID);
			if(rs.first()){
				json.put("success", "true");
				json.put("DeviceID", rs.getString("uuid"));
				json.put("IssuedTo", rs.getString("issued_to"));
				json.put("IsActive", rs.getString("is_active"));
				json.put("MobileNo", rs.getString("mobile_no"));
			}
			
			s.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			json.put("success", "false");
			json.put("error", e.toString());
		}finally{
			try { 
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		out.print(json);
		
	}
	
}
