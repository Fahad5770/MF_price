package com.pbc.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;


@WebServlet(description = "Get Product By Search JSON", urlPatterns = { "/common/GetVariableCostJSON" })
public class GetVariableCostJSON extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetVariableCostJSON() {
        super();
        //System.out.println("contructor");
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//System.out.println("service");
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		int PackageID = Utilities.parseInt(request.getParameter("PackageID"));
		int BrandID = Utilities.parseInt(request.getParameter("BrandID"));
		
		Datasource ds = new Datasource();
		
		try { 
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			
			long SapCode=0;
			
			//System.out.println("select sap_code from inventory_products where package_id="+PackageID+" and brand_id="+BrandID);
			
			ResultSet rs = s.executeQuery("select sap_code from inventory_products where package_id="+PackageID+" and brand_id="+BrandID+" and category_id = 1");
			if (rs.first()){	
				SapCode = rs.getLong("sap_code");
			}
			
			//System.out.println(SapCode);
			
			ResultSet rs1 = s.executeQuery("select cost from inventory_products_variable_costs where sap_code="+SapCode);
			if(rs1.next()){
				obj.put("exists", "true");
				obj.put("VariableCost", rs1.getDouble("cost"));
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
