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


@WebServlet(description = "Get Desk Sale Information in JSON", urlPatterns = { "/inventory/GetDeskSaleInfoJson" })
public class GetDeskSaleInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetDeskSaleInfoJson() {
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
			System.out.println("SELECT *, distributor_id as distributor_id_val, (SELECT name FROM common_distributors where distributor_id=distributor_id_val) distributor_name FROM inventory_sales_invoices where id="+EditID);
			ResultSet rs = s.executeQuery("SELECT *, distributor_id as distributor_id_val, (SELECT name FROM common_distributors where distributor_id=distributor_id_val) distributor_name FROM inventory_sales_invoices where id="+EditID);
			if (rs.first()){
				
				obj.put("exists", "true");
				
				if( !DateUtils.isSameDay(new java.util.Date(), rs.getDate("created_on")) ){
					obj.put("isTodaysVoucher", "false");
				}else{
					obj.put("isTodaysVoucher", "true");
				}
				
				obj.put("OutletID", rs.getLong("outlet_id"));
				
				obj.put("InvoiceAmount", rs.getDouble("invoice_amount"));
				obj.put("SalesTaxRate", rs.getDouble("sales_tax_rate"));
				obj.put("SalesTaxAmount", rs.getDouble("sales_tax_amount"));
				obj.put("WHTaxRate", rs.getDouble("wh_tax_rate"));
				obj.put("WHTaxAmount", rs.getDouble("wh_tax_amount"));
				obj.put("TotalAmount", rs.getDouble("total_amount"));
				obj.put("Discount", rs.getDouble("discount"));
				obj.put("NetAmount", rs.getDouble("net_amount"));
				obj.put("FractionAdjustment", rs.getDouble("fraction_adjustment"));
				
				ResultSet rs2 = s2.executeQuery("SELECT *, (select package_id from inventory_products where id=product_id) package_id, (select brand_id from inventory_products where id=product_id) brand_id, (select sap_code from inventory_products where id=product_id) sap_code, (SELECT label FROM inventory_packages where id=package_id) package_name, (SELECT label FROM inventory_brands where id=brand_id) brand_name, (select unit_per_sku from inventory_products where id=product_id) unit_per_sku, (SELECT liquid_in_ml FROM inventory_packages where id=package_id) liquid_in_ml_val FROM inventory_sales_invoices_products where id="+EditID);
				while( rs2.next() ){
					
					LinkedHashMap rows = new LinkedHashMap();
					
					System.out.println("rs2.getDouble(\"discount\") : " + rs2.getDouble("discount"));
					System.out.println("rs2.getInt(\"raw_cases\") : " + rs2.getInt("raw_cases"));

					
					rows.put("ProductID", rs2.getLong("product_id"));
					rows.put("RawCases", rs2.getInt("raw_cases"));
					rows.put("Units", rs2.getInt("units"));
					rows.put("RateRawCases", rs2.getDouble("rate_raw_cases"));
					rows.put("RateUnits", rs2.getDouble("rate_units"));
					
					rows.put("TotalAmount", rs2.getDouble("total_amount"));
					rows.put("Discount", rs2.getDouble("discount"));
					rows.put("NetAmount", rs2.getDouble("net_amount"));
					
					rows.put("UnitPerSKU", rs2.getString("unit_per_sku"));
					
					rows.put("PackageID", rs2.getString("package_id"));
					rows.put("PackageName", rs2.getString("package_name"));
					rows.put("BrandName", rs2.getString("brand_name"));
					rows.put("SAPCode", rs2.getString("sap_code"));
					
					rows.put("LiquidInML", rs2.getString("liquid_in_ml_val"));
					
					rows.put("IsPromotion", rs2.getString("is_promotion"));
					rows.put("PromotionID", rs2.getString("promotion_id"));
					
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
