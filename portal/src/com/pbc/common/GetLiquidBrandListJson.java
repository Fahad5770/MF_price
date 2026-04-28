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


@WebServlet(description = "Get Liquid Brand List in JSON", urlPatterns = { "/common/GetLiquidBrandListJson" })
public class GetLiquidBrandListJson extends HttpServlet {
	private static final long serialVersionUID = 1654111L;
       
    public GetLiquidBrandListJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}
		
		int PackageID = Utilities.parseInt(request.getParameter("PackageID"));
		
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj = new JSONObject();
			obj.put("is_LrbType","false");
			JSONArray jr = new JSONArray();
			JSONArray jr2 = new JSONArray();
			JSONArray jr3 = new JSONArray();
			
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			
			String Sql = "select id, label from inventory_brands order by label";
			String LrbSql ="";
			String SelectOptions = "<select id='select_id' name='select_id' data-mini='true' ><option value=''>Select Brand</option>";
			String SelectOptions2 = "<select id='select_id' name='select_id' data-mini='true' ><option value=''>Select Brand</option>";
			String SelectOptions3="";
			if( PackageID != 0){
				Sql = "select distinct brand_id, brand_label from inventory_products_view where category_id=1 and package_id="+PackageID+" order by brand_label";
			}
			
			ResultSet rs2 = s2.executeQuery(Sql);
			while(rs2.next()){
				
				LinkedHashMap rows = new LinkedHashMap();
				
				rows.put("id", rs2.getString(1));
				rows.put("label", rs2.getString(2));
				
				jr.add(rows);
				
				SelectOptions += "<option value="+rs2.getString(1)+">"+rs2.getString(2)+"</option>";
			}
			
			SelectOptions += "</select>";
			
			///
			int counter=0;
			//Updated by farhan on 5 dec 2017
			////Starts here 
		if( PackageID != 0){
			
			//getting lrb type
			
			LrbSql = "SELECT distinct ip.lrb_type_id,iplt.label lrb_label FROM inventory_products ip join inventory_products_lrb_types iplt on  ip.lrb_type_id=iplt.id where package_id="+PackageID+" order by label";
			
			ResultSet rs21 = s3.executeQuery(LrbSql);
			while(rs21.next()){
			
				//LinkedHashMap rows112 = new LinkedHashMap();
				
				//rows112.put("lrb_type_id", rs21.getString(1));
				//rows112.put("lrb_type_label", rs21.getString(2));
				
				//jr3.add(rows112);
				
				SelectOptions3 += "<option value=a_"+rs21.getString(1)+">All "+rs21.getString(2)+"</option>";
				
				
			
			}
			
			
			
			//System.out.println("PacakgeID"+PackageID);
				LrbSql = "SELECT distinct ip.brand_id as id, (select label from inventory_brands where id = brand_id) label,ip.lrb_type_id,iplt.label lrb_label FROM inventory_products ip join inventory_products_lrb_types iplt on  ip.lrb_type_id=iplt.id where package_id="+PackageID+" order by label";
			// System.out.println(LrbSql);
			}
			String DistinctLRBVariable="";
			String LRbVariable="";
			ResultSet rs21 = s3.executeQuery(LrbSql);
			while(rs21.next()){
				
				
				LinkedHashMap rows11 = new LinkedHashMap();
				
				rows11.put("id2", rs21.getString("id"));
				rows11.put("label2", rs21.getString("label"));
				
				jr.add(rows11);
				
				//LinkedHashMap rowslrb11 = new LinkedHashMap();
				//rowslrb.put("id2", rs21.getString("id"));
				
				SelectOptions2 += "<option value="+rs21.getString("id")+">"+rs21.getString("label")+"</option>";
				
				obj.put("is_LrbType","true");
			}
			
			SelectOptions2+=SelectOptions3;
			
			
			SelectOptions2 += "</select>";
			
			///Ends here
			obj.put("rowslrb",jr3);
			obj.put("exists","true");
			obj.put("SelectOptions", SelectOptions);
			obj.put("SelectOptions2", SelectOptions2);
			obj.put("rows",jr);
			obj.put("rows11",jr2);			
///	
			
			//obj.put("exists","true");
			//obj.put("SelectOptions", SelectOptions);
			//obj.put("rows",jr);
			
			rs2.close();
			s2.close();
				
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();
			
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			sendErrorRedirect(request,response,Utilities.getErrorPageURL(request),e);
		}		
		
	}
	
	protected void sendErrorRedirect(HttpServletRequest request, HttpServletResponse response, String errorPageURL, Throwable e) throws ServletException, IOException {
		request.setAttribute ("javax.servlet.jsp.jspException", e);
		getServletConfig().getServletContext().getRequestDispatcher(errorPageURL).forward(request, response);
	}
	
}
