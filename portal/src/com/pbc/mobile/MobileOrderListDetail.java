package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.inventory.Product;
import com.pbc.util.Datasource;
import com.pbc.util.UserAccess;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class SyncOrders
 */

@WebServlet(description = "Mobile Order List Detail", urlPatterns = { "/mobile/MobileOrderListDetail" })
public class MobileOrderListDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileOrderListDetail() {
        super();
        // TODO Auto-generated constructor stub
        //System.out.println("constructor ...");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//System.out.println("service() ...");
		
		PrintWriter out = response.getWriter();
		
		int FeatureID = 64;
		
		
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 3000));

		JSONObject json = new JSONObject();
		JSONArray jr = new JSONArray();
		
		if (!mr.isExpired()){
			
		
		long UserID = Utilities.parseLong(mr.getParameter("UserID"));
		String Password = Utilities.filterString(mr.getParameter("Identity"), 1, 100);
		String DeviceID = Utilities.filterString(mr.getParameter("UVID"), 1, 100);
		
		long OrderID = Utilities.parseLong(mr.getParameter("OrderID"));
	
		Datasource ds = new Datasource();

		try {
			ds.createConnection();
			Statement s1 = ds.createStatement();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			boolean isValidAccess = false;
			
			ResultSet rs1 = s1.executeQuery("select id, password from users where id="+UserID);
			if(rs1.first()){
				
				if( Password.equals(rs1.getString("password")) || Password.equals(Utilities.getMobileAdminPasswordMD5())){
				//	if( UserAccess.isMobileDeviceValid(DeviceID) ){
						if( Utilities.isAuthorized(FeatureID, UserID) ){
							isValidAccess = true;
						}else{
							json.put("error_code", "105");
							isValidAccess = false;
						}
//					}else{
//						json.put("error_code", "102");
//						isValidAccess = false;
//					}
				}else{
					json.put("error_code", "104");
					isValidAccess = false;
				}
				
				if(isValidAccess){
					
					ResultSet rs = s.executeQuery("SELECT invoice_amount, wh_tax_amount, net_amount FROM pep.mobile_order where id="+OrderID);
					if(rs.first()){
						json.put("success", "true");
						
						json.put("InvoiceAmount", rs.getString("invoice_amount"));
						json.put("WHTaxAmount", rs.getString("wh_tax_amount"));
						json.put("NetAmount", rs.getString("net_amount"));
						
						ResultSet rs2 = s2.executeQuery("SELECT mop.product_id, concat(ipv.package_label, ' ', ipv.brand_label ) product, mop.total_units, mop.rate_raw_cases, mop.net_amount, ipv.unit_per_sku FROM pep.mobile_order_products mop, inventory_products_view ipv where mop.product_id=ipv.product_id and mop.id="+OrderID);
						while(rs2.next()){
							LinkedHashMap rows = new LinkedHashMap();
							
							rows.put("Product", rs2.getString("product"));
							rows.put("Quantity", Utilities.convertToRawCases(rs2.getLong("total_units"), rs2.getInt("unit_per_sku")));
							rows.put("RateRawCases", rs2.getString("rate_raw_cases"));
							rows.put("NetAmount", rs2.getString("net_amount"));
							
							jr.add(rows);
						}
						
						json.put("rows", jr);
						
					}else{
						json.put("success", "false");
					}
					
					
				}else{
					json.put("success", "false");
				}
				
			}else{
				json.put("success", "false");
			}
			
			s2.close();
			s1.close();
			s.close();
			ds.dropConnection();
			
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException e) {
			// TODO Auto-generated catch block
			json.put("success", "false");
			json.put("error_code", "106");
			
			e.printStackTrace();
		}
		
		}else{
			json.put("success", "false");
			json.put("error_code", "101");
		}
		out.print(json);
		
		
	}


}
