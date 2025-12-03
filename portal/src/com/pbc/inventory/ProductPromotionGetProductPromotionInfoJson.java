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


@WebServlet(description = "Get Delivery Note Information in JSON", urlPatterns = { "/inventory/ProductPromotionGetProductPromotionInfoJson" })
public class ProductPromotionGetProductPromotionInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ProductPromotionGetProductPromotionInfoJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}		
		
		long ProductPromotionID = Utilities.parseLong(request.getParameter("ProductPromotionID"));		
		
		Datasource ds = new Datasource();
		
		try { 
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			JSONArray jr = new JSONArray();
			JSONArray BrandsJSONArray = new JSONArray();
			
			ResultSet rs = s.executeQuery("select * from inventory_sales_promotions where id = "+ProductPromotionID);
			if (rs.first()){
				String FromDate =  Utilities.getDisplayDateFormat(rs.getDate("valid_from"));
				String ToDate =  Utilities.getDisplayDateFormat(rs.getDate("valid_to"));
				
				obj.put("ID", rs.getString("id"));
				obj.put("Label", rs.getString("label"));
				obj.put("ValidFrom", FromDate);
				obj.put("ValidTo", ToDate);
				obj.put("CreatedOn", rs.getString("created_on"));
				obj.put("CreatedBy", rs.getString("created_by"));
				obj.put("IsActive", rs.getString("is_active"));				
				obj.put("exists", "true");
				
				ResultSet rs1 = s.executeQuery("SELECT ispp.id,ispp.package_id," +
													   "(select label from inventory_packages where id = ispp.package_id) package_label,"+													  
													   "ispp.raw_cases,"+
													   "ispp.units,"+
													   "ispp.type_id "+
												"FROM "+
												    "inventory_sales_promotions_products ispp where ispp.id = "+ProductPromotionID);
				while(rs1.next())
				{
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("ID", rs1.getString("id"));
					rows.put("PackageID", rs1.getString("package_id"));
					rows.put("PackageLabel", rs1.getString("package_label"));
					//rows.put("BrandID", rs1.getString("brand_id"));
					//rows.put("BrandLabel", rs1.getString("brand_label"));
					rows.put("RawCases", rs1.getString("raw_cases"));
					rows.put("Units", rs1.getString("units"));
					rows.put("TypeID", rs1.getString("type_id"));
					jr.add(rows);
					
					ResultSet rs2 = s2.executeQuery("select id,package_id,brand_id,(select label from inventory_brands where id = brand_id) brand_label,type_id from inventory_sales_promotions_products_brands where id="+rs1.getString("id")+" and package_id="+rs1.getString("package_id")+" and type_id="+rs1.getString("type_id"));
					while(rs2.next())
					{
						LinkedHashMap rows1 = new LinkedHashMap();
						rows1.put("BrandID", rs2.getString("brand_id"));
						rows1.put("BrandLabel", rs2.getString("brand_label"));
						rows1.put("PackageID1", rs2.getString("package_id"));
						rows1.put("TypeID1", rs2.getString("type_id"));
						BrandsJSONArray.add(rows1);
					}
				}
				
				obj.put("rows", jr);
				obj.put("BrandsRows", BrandsJSONArray);
				
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
