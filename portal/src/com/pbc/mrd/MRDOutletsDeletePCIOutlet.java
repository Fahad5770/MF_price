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


@WebServlet(description = "MRD Outlets Delete PCI Outlet", urlPatterns = { "/mrd/MRDOutletsDeletePCIOutlet" })
public class MRDOutletsDeletePCIOutlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
    public MRDOutletsDeletePCIOutlet() {
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
			
			
			ResultSet rs = s.executeQuery("select * from pcioutletsverification where id="+OutletID);
			if(rs.first()){
				
				s2.executeUpdate("INSERT INTO `pcioutletsverification_deleted`(`ID`,`SHOP_NAME`,`SHOP_NUMBER`, `STREET_NAME`, `AREA_NAME`, `MOHALLAH`,`region_id`,`gps_n`,`gps_e`,`GPSAccuracy`,`comments`,`PEPSI`,`Coke`,`Gourmet`,`KEY_CONTACT_PERSON`,`mobile_no`,`userid`,`deleted_on`,`deleted_by`, `changed_on`, `changed_by`)VALUES('"+rs.getString("ID")+"', '"+rs.getString("SHOP_NAME")+"', '"+rs.getString("SHOP_NUMBER")+"', '"+rs.getString("STREET_NAME")+"', '"+rs.getString("AREA_NAME")+"', '"+rs.getString("MOHALLAH")+"', "+rs.getString("region_id")+","+rs.getString("gps_n")+","+rs.getString("gps_e")+","+rs.getString("GPSAccuracy")+",'"+rs.getString("comments")+"',"+rs.getString("PEPSI")+","+rs.getString("Coke")+","+rs.getString("Gourmet")+",'"+rs.getString("KEY_CONTACT_PERSON")+"','"+rs.getString("mobile_no")+"',"+rs.getString("userid")+", now(), "+SessionUserID+", '"+rs.getString("changed_on")+"', '"+rs.getString("changed_by")+"')");
			}
			
			//s.executeUpdate("delete from pcioutletsverification where id="+OutletID);
			
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
