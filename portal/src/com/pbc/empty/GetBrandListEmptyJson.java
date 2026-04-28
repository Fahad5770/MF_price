package com.pbc.empty;

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


@WebServlet(description = "Get Empty Brand List in JSON", urlPatterns = { "/empty/GetBrandListEmptyJson" })
public class GetBrandListEmptyJson extends HttpServlet {
	private static final long serialVersionUID = 1654111L;
       
    public GetBrandListEmptyJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}
		
		int PackageID = Utilities.parseInt(request.getParameter("PackageID"));
		//System.out.println(PackageID);
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj = new JSONObject();
			
			JSONArray jr = new JSONArray();
			
			Statement s2 = ds.createStatement();
			

			String Sql = "SELECT brand_id as id, (select label from inventory_brands where id = brand_id) label, sap_code FROM inventory_products where package_id="+PackageID+" and category_id in (2,3,4) order by brand_id";
			ResultSet rs2 = s2.executeQuery(Sql);
			while(rs2.next()){
				
				LinkedHashMap rows = new LinkedHashMap();
				
				rows.put("id", rs2.getString(1));
				rows.put("label", rs2.getString(2));
				rows.put("SAPCode", rs2.getString(3));
				
				jr.add(rows);
				
			}
			
			
			obj.put("exists","true");
			obj.put("rows",jr);
			
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
