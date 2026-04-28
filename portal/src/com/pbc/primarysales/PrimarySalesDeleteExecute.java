package com.pbc.primarysales;

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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;


@WebServlet(description = "Get Distributor Information in JSON", urlPatterns = { "/primarysales/PrimarySalesDeleteExecute" })

public class PrimarySalesDeleteExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PrimarySalesDeleteExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		long SerialID = Utilities.parseLong(request.getParameter("SerialID"));
		
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			
			ds.startTransaction();
			
		
			
			//deleting from inventory table  - From detail table it will delete automatically.
			
			s.executeUpdate("delete from inventory_delivery_note where outsourced_primary_sales_id="+SerialID);
			
			s.executeUpdate("delete from primary_sales_orders where id="+SerialID);
			
			
			ds.commit();
	
				
				obj.put("success", "true");
				

			
			
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
