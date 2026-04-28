package com.pbc.common;

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


@WebServlet(description = "Get User Information in JSON", urlPatterns = { "/common/GetUserInfoJson" })

public class GetUserInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetUserInfoJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		String Userid = request.getParameter("UserID");
		
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			JSONArray jr = new JSONArray();
			JSONArray jr2 = new JSONArray();
			
			
			ResultSet rs = s.executeQuery("SELECT display_name, (SELECT employee_product_group_id FROM employee_product_specification where employee_sap_code="+Userid+") product_group_id from users where id = " + Userid);
			if (rs.first()){
				
				obj.put("exists", "true");
				
				obj.put("DistributorName", rs.getString(1));
				obj.put("UserName", rs.getString(1));
				obj.put("ProductGroupID", rs.getString("product_group_id"));
				
				int ProductGroupID = rs.getInt("product_group_id");
				
				ResultSet rs2 = s2.executeQuery("select distinct package_id, package_label from (SELECT epg.product_group_id, epgl.product_id, ipv.package_id, ipv.package_label FROM employee_product_groups epg, employee_product_groups_list epgl, inventory_products_view ipv where epg.product_group_id=epgl.product_group_id and epgl.product_id=ipv.product_id and epg.product_group_id="+ProductGroupID+" order by ipv.package_sort_order) PackageTable");
				while(rs2.next()){
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("PackageID", rs2.getString("package_id"));
					rows.put("PackageLabel", rs2.getString("package_label"));
					
					int PackageID = rs2.getInt("package_id");
					
					jr2 = new JSONArray();
					
					ResultSet rs3 = s3.executeQuery("SELECT brand_id, brand_label FROM inventory_products_view where product_id in ( SELECT epgl.product_id FROM employee_product_groups epg, employee_product_groups_list epgl, inventory_products_view ipv where epg.product_group_id=epgl.product_group_id and epgl.product_id=ipv.product_id and epg.product_group_id="+ProductGroupID+" and ipv.package_id="+PackageID+" order by ipv.package_sort_order )");
					while(rs3.next()){
						LinkedHashMap rows2 = new LinkedHashMap();
						
						rows2.put("BrandID", rs3.getString("brand_id"));
						rows2.put("BrandLabel", rs3.getString("brand_label"));
						
						jr2.add(rows2);
					}
					
					rows.put("BrandRows", jr2);
					
					jr.add(rows);
				}
				
				obj.put("ProductGroupRows", jr);
				
			}else{
				
				
				obj.put("exists", "false");

			}
			
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();
			
			s3.close();
			s2.close();
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}
	
}
