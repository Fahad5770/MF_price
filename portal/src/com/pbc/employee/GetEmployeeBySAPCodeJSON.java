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


@WebServlet(description = "Get Employee By SAP Code JSON", urlPatterns = { "/employee/GetEmployeeBySAPCodeJSON" })
public class GetEmployeeBySAPCodeJSON extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetEmployeeBySAPCodeJSON() {
        super();
        System.out.println("contructor");
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("service");
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		long SAPCode = Utilities.parseLong(request.getParameter("SAPCode"));
		Datasource ds = new Datasource();
		
		try { 
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			
			ResultSet rs = s.executeQuery("SELECT concat(vorna, ' ', nachn) employee_name FROM sap_pa0002 where pernr="+SAPCode);
			if (rs.first()){
				obj.put("exists", "true");
				obj.put("EmployeeName", rs.getString("employee_name"));
			}
						
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();
			
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}
	
}
