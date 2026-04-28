package com.pbc.distributor;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Distributor Target Edit Info Json", urlPatterns = { "/distributor/DistributorTargetEditInfoJson" })
public class DistributorTargetEditInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DistributorTargetEditInfoJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();
		
		int EditID = Utilities.parseInt(request.getParameter("EditID"));
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		JSONArray jr = new JSONArray();
		JSONArray jr2 = new JSONArray();
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT *, date_format(start_date, '%d/%m/%Y') start_date_formatted, date_format(end_date, '%d/%m/%Y') end_date_formatted  FROM distributor_targets where id="+EditID);
			if(rs.first()){
				
				obj.put("success", "true");
				
				obj.put("DistributorID", rs.getString("distributor_id"));
				obj.put("Month", rs.getString("month"));
				obj.put("Year", rs.getString("year"));
				obj.put("StartDate", rs.getString("start_date_formatted"));
				obj.put("EndDate", rs.getString("end_date_formatted"));
				obj.put("TargetTypeID", rs.getString("type_id"));
				
				ResultSet rs2 = s2.executeQuery("SELECT * FROM distributor_targets_packages where id="+EditID);
				while(rs2.next()){
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("PackageID", rs2.getString("package_id"));
					rows.put("Quantity", rs2.getString("quantity"));
					
					jr2 = new JSONArray();
					ResultSet rs3 = s3.executeQuery("SELECT * FROM distributor_targets_packages_brands where id="+EditID+" and package_id="+rs2.getString("package_id"));
					while(rs3.next()){
						LinkedHashMap rows2 = new LinkedHashMap();
						
						rows2.put("BrandID", rs3.getString("brand_id"));
						rows2.put("Quantity", rs3.getString("quantity"));
						
						jr2.add(rows2);
					}
					
					rows.put("BrandRows", jr2);
					
					
					jr.add(rows);
				}
				
				obj.put("PackageRows", jr);
				
				
				
			}
			
			s3.close();
			s2.close();
			s.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
			try {
				if (ds != null){
					ds.dropConnection();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
