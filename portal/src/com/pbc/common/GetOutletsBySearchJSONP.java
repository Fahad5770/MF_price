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


@WebServlet(description = "Get Outlets by Search in JSONP", urlPatterns = { "/common/GetOutletsBySearchJSONP" })
public class GetOutletsBySearchJSONP extends HttpServlet {
	private static final long serialVersionUID = 1654111L;
       
    public GetOutletsBySearchJSONP() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}
		
		
		String query = Utilities.filterString(request.getParameter("q"), 1, 10);
		String callback = request.getParameter("callback");
				
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj = new JSONObject();
			
			JSONArray jr = new JSONArray();
			
			Statement s2 = ds.createStatement();
			
			ResultSet rs2 = s2.executeQuery("select outlet_id, outlet_name, bsi_name, customer_name, region, region_name, address, owner, telepohone from outletmaster where match(outlet_name, bsi_name, owner, address, telepohone) against('"+query+"') or outlet_id = '"+query+"' limit 20");
			while(rs2.next()){
				
				LinkedHashMap rows = new LinkedHashMap();
				
				rows.put("OutletID", rs2.getString(1));
				rows.put("OutletName", rs2.getString(2));
				rows.put("OutletType", rs2.getString(3));
				rows.put("DistributorName", rs2.getString(4));
				rows.put("region_name_short", rs2.getString("region"));
				rows.put("region_name_long", rs2.getString("region_name"));
				rows.put("address", rs2.getString("address"));
				rows.put("owner", rs2.getString("owner"));
				rows.put("telephone", rs2.getString("telepohone"));
				
				jr.add(rows);
			}
			
			obj.put("rows",jr);
			
			StringBuffer output = new StringBuffer(callback + "("); 
			output.append(obj);
			output.append(");");
			
			rs2.close();
			s2.close();
				
			PrintWriter out = response.getWriter();
			out.print(output);
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
