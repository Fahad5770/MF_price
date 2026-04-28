package com.pbc.employee;

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


@WebServlet(description = "Get Employee Product Specification Info Json", urlPatterns = { "/employee/GetEmployeeProductSpecificationInfoJson" })
public class GetEmployeeProductSpecificationInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetEmployeeProductSpecificationInfoJson() {
        super();
        System.out.println("contructor");
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("service");
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		int EditID = Utilities.parseInt(request.getParameter("EditID"));
		
		Datasource ds = new Datasource();
		
		try { 
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			JSONArray jr = new JSONArray();
			
			
			ResultSet rs = s.executeQuery("SELECT `employee_product_group_id`, `employee_sap_code`, (SELECT concat(vorna,' ',nachn) as name FROM sap_pa0002 where pernr=employee_sap_code limit 1) employee_name FROM `employee_product_specification` where employee_product_specification_id="+EditID);
			if(rs.first()){
				obj.put("exists", "true");
				
				obj.put("GroupID", rs.getString("employee_product_group_id"));
				obj.put("EmployeeID", rs.getString("employee_sap_code"));
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
