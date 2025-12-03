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


@WebServlet(description = "Get Brand ExchangeInfo in JSON", urlPatterns = { "/inventory/GetBrandExchangeInfoJson" })
public class GetBrandExchangeInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetBrandExchangeInfoJson() {
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
			
			ResultSet rs = s.executeQuery("SELECT *, distributor_id as distributor_id_val, (SELECT name FROM common_distributors where distributor_id=distributor_id_val) distributor_name FROM inventory_brand_exchange where document_id="+EditID);
			if (rs.first()){
				
				obj.put("exists", "true");
				
				if( !DateUtils.isSameDay(new java.util.Date(), rs.getDate("created_on")) ){
					obj.put("isTodaysVoucher", "false");
				}else{
					obj.put("isTodaysVoucher", "true");
				}
				
				
				obj.put("Remarks", rs.getString("remarks"));
				
				String Sql = "SELECT *, "+
				
					"(select package_id from inventory_products where id=product_id_issued) package_id, "+
					"(SELECT label FROM inventory_packages where id=package_id) package_name, "+
					
					"(select brand_id from inventory_products where id=product_id_issued) brand_id_issued, "+
					"(select brand_id from inventory_products where id=product_id_received) brand_id_received, "+
					"(SELECT label FROM inventory_brands where id=brand_id_issued) brand_name_issued, "+
					"(SELECT label FROM inventory_brands where id=brand_id_received) brand_name_received, "+
					
					"(select unit_per_sku from inventory_products where id=product_id_issued) unit_per_sku_issued, "+
					"(select unit_per_sku from inventory_products where id=product_id_received) unit_per_sku_received, "+
					
					"(SELECT liquid_in_ml FROM inventory_packages where id=package_id) liquid_in_ml_val"+
					
					" FROM inventory_brand_exchange_products where document_id="+EditID;
				
				System.out.println(Sql);
				
				ResultSet rs2 = s2.executeQuery(Sql);
				while( rs2.next() ){
					
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("ProductIDIssued", rs2.getLong("product_id_issued"));
					rows.put("ProductIDReceived", rs2.getLong("product_id_received"));
					
					rows.put("PackageID", rs2.getString("package_id"));
					
					rows.put("PackageName", rs2.getString("package_name"));
					rows.put("BrandNameIssued", rs2.getString("brand_name_issued"));
					rows.put("BrandNameReceived", rs2.getString("brand_name_received"));
					
					rows.put("UnitPerSKUIssued", rs2.getString("unit_per_sku_issued"));
					rows.put("UnitPerSKUReceived", rs2.getString("unit_per_sku_received"));
					rows.put("LiquidInML", rs2.getString("liquid_in_ml_val"));
					
					rows.put("RawCases", rs2.getInt("raw_cases"));
					rows.put("Units", rs2.getInt("units"));
					rows.put("TotalUnits", rs2.getString("total_units"));
					
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
