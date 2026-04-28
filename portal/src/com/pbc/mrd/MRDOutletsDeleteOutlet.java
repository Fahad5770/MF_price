package com.pbc.mrd;

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
import com.pbc.util.UserAccess;
import com.pbc.util.Utilities;


@WebServlet(description = "MRD Outlets Delete Outlet", urlPatterns = { "/mrd/MRDOutletsDeleteOutlet" })
public class MRDOutletsDeleteOutlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
    public MRDOutletsDeleteOutlet() {
        super();
        //System.out.println("contructor");
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//System.out.println("service");
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));		
		long OutletID = Utilities.parseLong(request.getParameter("OutletID"));
		
		Datasource ds = new Datasource();
		JSONObject obj=new JSONObject();
		try { 
			
			ds.createConnectionToMRD();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			response.setContentType("application/json");
			
			
			ResultSet rs = s.executeQuery("select * from outlets where outlet_id="+OutletID);
			if(rs.first()){				
				s2.executeUpdate("INSERT INTO `outlets_deleted`(`outlet_id`,`outlet_name`,`business_type_id`,`address`,`region_id`,`latitude`,`longitude`,`gps_accuracy`,`comments`,`pepsi`,`coke`,`gourmet`,`contact_person`,`phone`,`user_outlet_id`,`created_by`,`created_on`,`changed_on`,`changed_by`,`deleted_on`,`deleted_by`)VALUES("+rs.getString("outlet_id")+",'"+rs.getString("outlet_name")+"',"+rs.getString("business_type_id")+",'"+rs.getString("address")+"', "+rs.getString("region_id")+","+rs.getString("latitude")+","+rs.getString("longitude")+","+rs.getString("gps_accuracy")+",'"+rs.getString("comments")+"',"+rs.getString("pepsi")+","+rs.getString("coke")+","+rs.getString("gourmet")+",'"+rs.getString("contact_person")+"','"+rs.getString("phone")+"',"+rs.getString("user_outlet_id")+","+rs.getString("created_by")+",'"+rs.getString("created_on")+"','"+rs.getString("changed_on")+"',"+rs.getString("changed_by")+", now(), "+SessionUserID+")");
			}
			
			s.executeUpdate("delete from outlets where outlet_id="+OutletID);
			
			obj.put("success", "true");
			
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();
			
			s2.close();
			s.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("success", "false");
			obj.put("error", e.toString());
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
