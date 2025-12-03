package com.pbc.inventory;

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


@WebServlet(description = "Get Outlet By SAP Code JSON", urlPatterns = { "/inventory/GetInventryInfoJSON" })
public class GetInventryInfoJSON extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
    public GetInventryInfoJSON() {
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
		
		long InventryID = Utilities.parseLong(request.getParameter("InventryID"));
		
		Datasource ds = new Datasource();
		 
		try { 
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			
			//System.out.println("SELECT * FROM pep.inventory_sales_dispatch where id="+InventryID);
			
			ResultSet rs = s.executeQuery("SELECT * FROM pep.inventory_sales_dispatch where id="+InventryID);
			if (rs.first()){
				obj.put("exists", "true");
				obj.put("DistributorId", rs.getLong("distributor_id"));
				String distributorName="";
				ResultSet rs1 = s2.executeQuery("SELECT name FROM pep.common_distributors where distributor_id="+rs.getLong("distributor_id"));
				if (rs1.first()){
					distributorName=rs1.getString("name");
				}
				System.out.println(rs.getInt("is_blocked"));
				obj.put("DistributorName", distributorName);
				obj.put("Adjusted", rs.getInt("is_adjusted"));
				obj.put("Blocked", rs.getInt("is_blocked"));
				obj.put("Created_on",Utilities.getDisplayDateFormat(rs.getDate("created_on")));
			}else{
				obj.put("exists", "false");
			}
						
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();
			
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}
	
}
