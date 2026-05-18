package com.pbc.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;


@WebServlet(description = "Get Distributor Information in JSON", urlPatterns = { "/common/GetDistributorVehicleInfoJson" })

public class GetDistributorVehicleInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetDistributorVehicleInfoJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		long DistributorVehicleID = Utilities.parseLong(request.getParameter("DistributorVehicleID"));
		
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			
			ResultSet rs = s.executeQuery("select * from distribtuor_vehicles dv,distributor_employees de where dv.driver_id = de.id and dv.id = " + DistributorVehicleID);
			
			if (rs.first()){
				
				obj.put("exists", "true");
				
				obj.put("DistributorDriverName", rs.getString("name"));				

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
