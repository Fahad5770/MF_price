package com.pbc.empty;

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


@WebServlet(description = "Empty Credit Get Receipt Info Json", urlPatterns = { "/empty/EmptyCreditReceiptGetReceiptInfoJson" })
public class EmptyCreditReceiptGetReceiptInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EmptyCreditReceiptGetReceiptInfoJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		response.setContentType("application/json");
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		String EditID = Utilities.filterString(request.getParameter("EditID"), 0, 20);
		
		Datasource ds = new Datasource();
		
		PrintWriter out = response.getWriter();
		JSONObject obj=new JSONObject();
		JSONArray jr = new JSONArray();
		
		try { 
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			
			
			ResultSet rs = s.executeQuery("SELECT *, (SELECT name FROM common_distributors where distributor_id=ect.distributor_id) distributor_name,(select label from ec_empty_receipt_types ecert where ecert.id=ect.type_id) receipt_type_label,(select label from inventory_packages ipp where ipp.id=ip.package_id) package_label,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where ect.empty_receipt_id="+EditID);
			while (rs.next()){
				
				
				obj.put("success", "true");
				
				LinkedHashMap rows = new LinkedHashMap();
				
				
				rows.put("CustomerID", rs.getString("distributor_id"));
				rows.put("CustomerName", rs.getString("distributor_name"));
				rows.put("ReceiptType", rs.getString("type_id"));
				rows.put("ReceiptTypeLabel", rs.getString("receipt_type_label"));
				rows.put("PackageID", rs.getString("package_id"));
				rows.put("PackageLabel", rs.getString("package_label"));
				rows.put("BrandID", rs.getString("brand_id"));
				rows.put("BrandLabel", rs.getString("brand_label"));
				rows.put("Rawcases", rs.getString("raw_cases_received"));
				rows.put("Units", rs.getString("units_received"));
				rows.put("TotalUnits", rs.getString("total_units_received"));
				rows.put("ProductCode", rs.getString("sap_code"));
				
				jr.add(rows);
				
			}
			
			obj.put("rows", jr);
			
			s2.close();
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
		
		out.print(obj);
		out.close();
		
	}
	
}
