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


@WebServlet(description = "Get Employee Product Group Info Json", urlPatterns = { "/employee/GetEmployeeProductGroupInfoJson" })
public class GetEmployeeProductGroupInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetEmployeeProductGroupInfoJson() {
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
			Statement s2 = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			JSONArray jr = new JSONArray();
			
			System.out.println("SELECT * FROM employee_product_groups where product_group_id="+EditID);
			ResultSet rs = s.executeQuery("SELECT * FROM employee_product_groups where product_group_id="+EditID);
			if(rs.first()){
				obj.put("exists", "true");
				
				obj.put("GroupName", rs.getString("product_group_name"));
				
				ResultSet rs2 = s2.executeQuery("SELECT * FROM employee_product_groups_list where product_group_id="+EditID);
				while(rs2.next()){
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("ProductID", rs2.getString("product_id"));
					
					jr.add(rows);
					
				}
				obj.put("rows", jr);
				
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
