package com.pbc.employee;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;


@WebServlet(description = "Get Employee By Search JSON", urlPatterns = { "/employee/GetEmployeeBySearchJSON" })
public class GetEmployeeBySearchJSON extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetEmployeeBySearchJSON() {
        super();
        System.out.println("contructor");
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("service");
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		String query = Utilities.filterString(request.getParameter("q"), 1, 10);
		String callback = request.getParameter("callback");
		Datasource ds = new Datasource();
		
		try { 
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			JSONArray jr = new JSONArray();
			
			obj.put("exists", "true");
			
			ResultSet rs = s.executeQuery("SELECT pernr, vorna, nachn FROM sap_pa0002 where match(vorna, nachn) against('"+query+"') or pernr='"+query+"' order by vorna, nachn");
			while (rs.next()){
				
				LinkedHashMap rows = new LinkedHashMap();
				
				rows.put("EmployeeCode", rs.getString("pernr"));
				rows.put("FirstName", rs.getString("vorna"));
				rows.put("LastName", rs.getString("nachn"));
				
				jr.add(rows);
				
				
								
			}
			obj.put("rows", jr);
			
			StringBuffer output = new StringBuffer(callback + "("); 
			output.append(obj);
			output.append(");");
			
			PrintWriter out = response.getWriter();
			out.print(output);
			out.close();
			
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}
	
}
