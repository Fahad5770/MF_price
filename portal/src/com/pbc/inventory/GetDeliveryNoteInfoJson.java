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


@WebServlet(description = "Get Delivery Note Information in JSON", urlPatterns = { "/inventory/GetDeliveryNoteInfoJson" })
public class GetDeliveryNoteInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetDeliveryNoteInfoJson() {
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
			
			ResultSet rs = s.executeQuery("SELECT *, distributor_id as distributor_id_val, (SELECT name FROM common_distributors where distributor_id=distributor_id_val) distributor_name FROM inventory_delivery_note where delivery_id="+EditID);
			if (rs.first()){
				
				obj.put("exists", "true");
				
				if( !DateUtils.isSameDay(new java.util.Date(), rs.getDate("created_on")) ){
					obj.put("isTodaysVoucher", "false");
				}else{
					obj.put("isTodaysVoucher", "true");
				}
				
				obj.put("VehicleType", rs.getLong("vehicle_type"));
				obj.put("InvoiceNo", rs.getLong("invoice_no"));
				obj.put("IsPartial", rs.getLong("is_partial"));
				obj.put("DistributorID", rs.getLong("distributor_id"));
				obj.put("DistributorName", rs.getString("distributor_name"));
				obj.put("PaymentMethod", rs.getLong("payment_method"));
				obj.put("Remarks", rs.getString("remarks"));
				obj.put("VehicleNo", rs.getString("vehicle_no"));
				obj.put("SAPOrderNo", rs.getString("sap_order_no"));
				obj.put("IsDelivered", rs.getString("is_delivered"));
				obj.put("IsReceived", rs.getString("is_received"));
				
				
				if(rs.getString("palletize_type_id")==null){
					obj.put("PalletizeID", "3"); //if null then un-specified
				}else{
					obj.put("PalletizeID", rs.getString("palletize_type_id"));
				}
				
				
				
				
				
				//ResultSet rs2 = s2.executeQuery("SELECT *, (select package_id from inventory_products where id=product_id) package_id, (select brand_id from inventory_products where id=product_id) brand_id, (select sap_code from inventory_products where id=product_id) sap_code, (SELECT label FROM inventory_packages where id=package_id) package_name, (SELECT label FROM inventory_brands where id=brand_id) brand_name, (select unit_per_sku from inventory_products where id=product_id) unit_per_sku, (SELECT liquid_in_ml FROM inventory_packages where id=package_id) liquid_in_ml_val, (select shell_product_id from inventory_products_map where product_id=idnp.product_id limit 1) shell_product_id FROM inventory_delivery_note_products idnp where delivery_id="+EditID);
				ResultSet rs2 = s2.executeQuery("SELECT *, (select package_id from inventory_products where id=product_id) package_id, (select brand_id from inventory_products where id=product_id) brand_id, (select sap_code from inventory_products where id=product_id) sap_code, (SELECT label FROM inventory_packages where id=package_id) package_name, (SELECT label FROM inventory_brands where id=brand_id) brand_name, (select unit_per_sku from inventory_products where id=product_id) unit_per_sku, (SELECT liquid_in_ml FROM inventory_packages where id=package_id) liquid_in_ml_val, (select shell_product_id from inventory_products_map where product_id=idnp.product_id limit 1) shell_product_id FROM inventory_delivery_note_products idnp, inventory_products ip where idnp.product_id=ip.id and delivery_id="+EditID+" and ip.category_id!=2");
				while( rs2.next() ){
					
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("ProductID", rs2.getLong("product_id"));
					rows.put("RawCases", rs2.getInt("raw_cases"));
					rows.put("Units", rs2.getInt("units"));
					rows.put("UnitPerSKU", rs2.getString("unit_per_sku"));
					
					rows.put("PackageID", rs2.getString("package_id"));
					rows.put("PackageName", rs2.getString("package_name"));
					rows.put("BrandName", rs2.getString("brand_name"));
					rows.put("SAPCode", rs2.getString("sap_code"));
					
					rows.put("LiquidInML", rs2.getString("liquid_in_ml_val"));
					rows.put("BatchCode", rs2.getString("batch_code"));
					
					rows.put("ShellProductID", rs2.getString("shell_product_id"));
					
					
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
