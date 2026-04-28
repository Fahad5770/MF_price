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
import com.pbc.util.Utilities;


@WebServlet(description = "Get Physical Stock Adjustment Info Json", urlPatterns = { "/inventory/GetPhysicalStockAdjustmentInfoJson" })
public class GetPhysicalStockAdjustmentInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetPhysicalStockAdjustmentInfoJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		String EditID = Utilities.filterString(request.getParameter("EditID"), 0, 20);
		
		Datasource ds = new Datasource();
		
		try { 
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			JSONArray jr = new JSONArray();
			
			ResultSet rs = s.executeQuery("SELECT * from inventory_distributor_stock_adjustment where id="+EditID);
			if (rs.first()){
				
				obj.put("success", "true");
				
				ResultSet rs2 = s2.executeQuery("SELECT *, (select package_id from inventory_products where id=product_id) package_id, (select brand_id from inventory_products where id=product_id) brand_id, (select sap_code from inventory_products where id=product_id) sap_code, (SELECT label FROM inventory_packages where id=package_id) package_name, (SELECT label FROM inventory_brands where id=brand_id) brand_name, (select unit_per_sku from inventory_products where id=product_id) unit_per_sku, (SELECT liquid_in_ml FROM inventory_packages where id=package_id) liquid_in_ml_val FROM inventory_distributor_stock_adjustment_products where id="+EditID);
				while( rs2.next() ){
					
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("ProductID", rs2.getLong("product_id"));
					rows.put("RawCases", rs2.getInt("raw_cases"));
					rows.put("Units", rs2.getInt("units"));
					
					
					
					rows.put("UnitPerSKU", rs2.getString("unit_per_sku"));
					
					rows.put("PackageID", rs2.getString("package_id"));
					rows.put("PackageLabel", rs2.getString("package_name"));
					rows.put("BrandID", rs2.getString("brand_id"));
					rows.put("BrandLabel", rs2.getString("brand_name"));
					rows.put("SAPCode", rs2.getString("sap_code"));
					
					rows.put("LiquidInML", rs2.getString("liquid_in_ml_val"));
					
					jr.add(rows);
				}
				
				
				obj.put("rows", jr);
				
			}else{

				obj.put("success", "false");

			}
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
