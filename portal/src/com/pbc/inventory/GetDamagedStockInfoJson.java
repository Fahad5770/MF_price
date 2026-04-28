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


@WebServlet(description = "Get Damaged Stock Information in JSON", urlPatterns = { "/inventory/GetDamagedStockInfoJson" })
public class GetDamagedStockInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetDamagedStockInfoJson() {
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
			
			ResultSet rs = s.executeQuery("SELECT *, distributor_id as distributor_id_val, (SELECT name FROM common_distributors where distributor_id=distributor_id_val) distributor_name FROM inventory_damaged_stock where document_id="+EditID);
			if (rs.first()){
				
				obj.put("exists", "true");
				
				if( !DateUtils.isSameDay(new java.util.Date(), rs.getDate("created_on")) ){
					obj.put("isTodaysVoucher", "false");
				}else{
					obj.put("isTodaysVoucher", "true");
				}
				
				
				obj.put("Remarks", rs.getString("remarks"));
				
				ResultSet rs2 = s2.executeQuery("SELECT *, (select package_id from inventory_products where id=product_id) package_id, (select brand_id from inventory_products where id=product_id) brand_id, (select sap_code from inventory_products where id=product_id) sap_code, (SELECT label FROM inventory_packages where id=package_id) package_name, (SELECT label FROM inventory_brands where id=brand_id) brand_name, (select unit_per_sku from inventory_products where id=product_id) unit_per_sku, (SELECT liquid_in_ml FROM inventory_packages where id=package_id) liquid_in_ml_val, (select label from inventory_damaged_stock_types where id=type_id) damage_type_name FROM inventory_damaged_stock_products where document_id="+EditID);
				while( rs2.next() ){
					
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("ProductID", rs2.getLong("product_id"));
					rows.put("RawCases", rs2.getInt("raw_cases"));
					rows.put("Units", rs2.getInt("units"));
					rows.put("UnitPerSKU", rs2.getString("unit_per_sku"));
					
					rows.put("PackageName", rs2.getString("package_name"));
					rows.put("BrandName", rs2.getString("brand_name"));
					rows.put("SAPCode", rs2.getString("sap_code"));
					
					rows.put("LiquidInML", rs2.getString("liquid_in_ml_val"));
					rows.put("BatchCode", rs2.getString("batch_code"));
					
					rows.put("DamageTypeName", rs2.getString("damage_type_name"));
					rows.put("TypeID", rs2.getString("type_id"));
					
					
					
					jr.add(rows);
				}
				
				obj.put("rows", jr);
				
			}else{

				obj.put("exists", "false");

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
