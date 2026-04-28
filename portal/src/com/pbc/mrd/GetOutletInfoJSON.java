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


@WebServlet(description = "Get Outlet Info JSON", urlPatterns = { "/mrd/GetOutletInfoJSON" })
public class GetOutletInfoJSON extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
    public GetOutletInfoJSON() {
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
		 
		try { 
			
			ds.createConnectionToMRD();
			
			Statement s = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			
			String SQL = "SELECT *, (SELECT label FROM outlets_business_types where id=business_type_id) business_type_label, (SELECT concat(region_short_name, ' - ', region_name) FROM outlets_regions where region_id = outlets.region_id) region_name FROM outlets where outlet_id="+OutletID;			
			//System.out.println(SQL);			
			ResultSet rs = s.executeQuery(SQL);
			if (rs.first()){
				obj.put("success", "true");
				
				obj.put("OutletName", rs.getString("outlet_Name"));
				obj.put("BusinessTypeLabel", rs.getString("business_type_label"));
				obj.put("Address", rs.getString("address"));
				obj.put("Region", rs.getString("region_name"));
				obj.put("Lat", rs.getString("latitude"));
				obj.put("Lng", rs.getString("longitude"));
				obj.put("GpsAccuracy", rs.getString("gps_accuracy"));
				obj.put("Comments", rs.getString("comments"));
				obj.put("Pepsi", rs.getString("pepsi"));
				obj.put("Coke", rs.getString("coke"));
				obj.put("Gourmet", rs.getString("gourmet"));
				obj.put("ContactPerson", rs.getString("contact_person"));
				obj.put("Phone", rs.getString("phone"));
				obj.put("UserOutletID", rs.getString("user_outlet_id"));
				
			}else{
				obj.put("success", "false");
			}
			
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();
			
			s.close();
			
		} catch (Exception e) {
			e.printStackTrace();
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
