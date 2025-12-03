package com.pbc.cash;

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


@WebServlet(description = "GL Unpost Cash Receipt Get Receipt Info Json", urlPatterns = { "/cash/GLUnpostCashReceiptGetReceiptInfoJson" })
public class GLUnpostCashReceiptGetReceiptInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GLUnpostCashReceiptGetReceiptInfoJson() {
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
			
			
			
			ResultSet rs = s.executeQuery("SELECT *, (SELECT name FROM common_distributors where distributor_id=customer_id) customer_name, (SELECT label FROM gl_cash_receipts_types where id=receipt_type) receipt_type_label FROM gl_cash_receipts where id="+EditID);
			if (rs.first()){
				
				obj.put("success", "true");
				
				obj.put("CustomerID", rs.getString("customer_id"));
				obj.put("CustomerName", rs.getString("customer_name"));
				obj.put("ReceiptType", rs.getString("receipt_type"));
				obj.put("ReceiptTypeLabel", rs.getString("receipt_type_label"));
				
				obj.put("Narration", rs.getString("narration"));
				
				//ResultSet rs2 = s2.executeQuery("SELECT *, (select package_id from inventory_products where id=product_id) package_id, (select brand_id from inventory_products where id=product_id) brand_id, (select sap_code from inventory_products where id=product_id) sap_code, (SELECT label FROM inventory_packages where id=package_id) package_name, (SELECT label FROM inventory_brands where id=brand_id) brand_name, (select unit_per_sku from inventory_products where id=product_id) unit_per_sku, (SELECT liquid_in_ml FROM inventory_packages where id=package_id) liquid_in_ml_val, (select shell_product_id from inventory_products_map where product_id=idnp.product_id limit 1) shell_product_id FROM inventory_delivery_note_products idnp where delivery_id="+EditID);
				ResultSet rs2 = s2.executeQuery("SELECT *, (SELECT label FROM gl_cash_instruments where id=instrument_id) instrument_label FROM gl_cash_receipts_instruments where id="+rs.getString("id"));
				while( rs2.next() ){
					
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("InstrumentID", rs2.getString("instrument_id"));
					rows.put("InstrumentLabel", rs2.getString("instrument_label"));
					rows.put("Amount", rs2.getString("amount"));
					rows.put("InstrumentNo", rs2.getString("instrument_no"));
					rows.put("InstrumentDate", Utilities.getDisplayDateFormat(rs2.getDate("instrument_date")));
					
					jr.add(rows);
				}
				
				obj.put("rows", jr);
				
			}else{

				obj.put("success", "false");

			}
			
			
			
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
