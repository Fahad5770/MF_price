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


@WebServlet(description = "Get PCI Outlet Info JSON", urlPatterns = { "/mrd/GetPCIOutletInfoJSON" })
public class GetPCIOutletInfoJSON extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
    public GetPCIOutletInfoJSON() {
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
			
			String SQL = "SELECT *, (SELECT concat(region_short_name, ' - ', region_name) FROM outlets_regions where region_id = pov.region_id) region_name FROM pcioutletsverification pov where pov.id="+OutletID;			
			//System.out.println(SQL);			
			ResultSet rs = s.executeQuery(SQL);
			if (rs.first()){
				obj.put("success", "true");
				
				obj.put("OutletName", rs.getString("SHOP_NAME"));
				//obj.put("BusinessTypeLabel", rs.getString("business_type_label"));
				obj.put("Address", rs.getString("SHOP_NUMBER")+" "+rs.getString("STREET_NAME")+" "+rs.getString("AREA_NAME")+" "+rs.getString("MOHALLAH"));
				obj.put("Region", rs.getString("region_name"));
				obj.put("Lat", rs.getString("gps_n"));
				obj.put("Lng", rs.getString("gps_e"));
				obj.put("GpsAccuracy", rs.getString("GPSAccuracy"));
				obj.put("Comments", rs.getString("comments"));
				obj.put("Pepsi", rs.getString("PEPSI"));
				obj.put("Coke", rs.getString("Coke"));
				obj.put("Gourmet", rs.getString("Gourmet"));
				obj.put("ContactPerson", rs.getString("KEY_CONTACT_PERSON"));
				obj.put("Phone", rs.getString("mobile_no"));
				obj.put("UserOutletID", rs.getString("OutletID"));
				
				
				
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
