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


@WebServlet(description = "Get Distributor Information in JSON", urlPatterns = { "/primarysales/GetPriceListJson" })

public class GetPriceListJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetPriceListJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		long DistributorID = Utilities.parseLong(request.getParameter("DistributorID"));
		long SapCode = Utilities.parseLong(request.getParameter("ProductCode"));
		long ProductID =0;
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			
			ResultSet rs1 = s.executeQuery("SELECT id FROM inventory_products where sap_code="+SapCode);
			if(rs1.first()){
				ProductID = rs1.getInt("id");
			}
			
			//System.out.println("SELECT * FROM primary_sales_price_list where distributor_id="+DistributorID+" and product_id="+ProductID);
			ResultSet rs = s.executeQuery("SELECT * FROM primary_sales_price_list where distributor_id="+DistributorID+" and product_id="+ProductID);
			if (rs.first()){
				
				obj.put("exists", "true");
				
				obj.put("Price", rs.getDouble("selling_price"));
				

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
