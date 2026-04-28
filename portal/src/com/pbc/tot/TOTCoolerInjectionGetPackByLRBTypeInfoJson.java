package com.pbc.tot;

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


@WebServlet(description = "Get the information again outlet for cooler injection", urlPatterns = { "/tot/TOTCoolerInjectionGetPackByLRBTypeInfoJson" })
public class TOTCoolerInjectionGetPackByLRBTypeInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public TOTCoolerInjectionGetPackByLRBTypeInfoJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}		
		
		
		Long LRBType = Utilities.parseLong(request.getParameter("LRBTypeIDD"));
				
		
		//System.out.println("brrrr - "+LRBType);
		
		Datasource ds = new Datasource();
		
		try { 
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			JSONArray jr = new JSONArray();
			JSONArray jr1 = new JSONArray();
			
			
			
			
				
			ResultSet rs1 = s.executeQuery("SELECT distinct package_id,package_label FROM inventory_products_view where lrb_type_id="+LRBType);
				
				while(rs1.next())
				{
					
					obj.put("exists", "true");
					LinkedHashMap rows = new LinkedHashMap();
				
					rows.put("package_id", rs1.getString("package_id"));
					rows.put("package_label", rs1.getString("package_label"));
					
					jr.add(rows);
				}
				
				obj.put("rows", jr);
				
				
				
				
			
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();
			
			s2.close();
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}
	
}
