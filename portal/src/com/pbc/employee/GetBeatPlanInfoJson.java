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


@WebServlet(description = "Get Beat Plan Info Json", urlPatterns = { "/employee/GetBeatPlanInfoJson" })
public class GetBeatPlanInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetBeatPlanInfoJson() {
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
			JSONArray jr2 = new JSONArray();
			
			ResultSet rs = s.executeQuery("SELECT *, (SELECT concat(vorna,' ',nachn) as name FROM sap_pa0002 where pernr=assigned_to limit 1) cr_name FROM employee_beat_plan where beat_plan_id="+EditID);
			if(rs.first()){
				obj.put("exists", "true");
				obj.put("AssignedTo", rs.getString("assigned_to"));
				obj.put("CRName", rs.getString("cr_name"));
			}
			
			
			ResultSet rs3 = s.executeQuery("SELECT distinct outlet_id, (SELECT Outlet_Name FROM outletmaster where outlet_id=ebps.outlet_id) outlet_name FROM employee_beat_plan_schedule ebps where beat_plan_id="+EditID);
			
			while (rs3.next()){
				
				LinkedHashMap rows = new LinkedHashMap();
				
				rows.put("OutletID", rs3.getString("outlet_id"));
				rows.put("OutletName", rs3.getString("outlet_name"));
				
				jr.add(rows);
								
			}
			
			obj.put("rows_distinct", jr);
			
			
			ResultSet rs2 = s.executeQuery("SELECT *, (select long_name from common_days_of_week where id=day_number) day_name  FROM employee_beat_plan_schedule ebps where beat_plan_id="+EditID);
			
			while (rs2.next()){
				
				LinkedHashMap rows = new LinkedHashMap();
				
				rows.put("OutletID", rs2.getString("outlet_id"));
				rows.put("DayNumber", rs2.getString("day_number"));
				rows.put("DayName", rs2.getString("day_name"));
				
				jr2.add(rows);
								
			}
			
			obj.put("rows", jr2);
			
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
