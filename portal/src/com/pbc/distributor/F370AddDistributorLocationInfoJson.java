package com.pbc.distributor;

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


@WebServlet(description = "Get distributor Name Info Json", urlPatterns = { "/distributor/F370AddDistributorLocationInfoJson" })
public class F370AddDistributorLocationInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public F370AddDistributorLocationInfoJson() {
        super();
    }

    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		String DistributorID = request.getParameter("DistributorID");
		//System.out.println("DistributorID "+DistributorID);
		Datasource ds = new Datasource();
		JSONObject json = new JSONObject();
		
		JSONArray citiesArray=new JSONArray();
		JSONArray distributordetailArray=new JSONArray();
		
		try { 
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT * from common_distributors where distributor_id="+DistributorID);
			//System.out.println("SELECT * from common_distributors where distributor_id="+DistributorID);
			if(rs.first()){
				json.put("success", "true");
				json.put("DistributorName", rs.getString("name"));
			}
			
			ResultSet rs1 = s.executeQuery("SELECT * from common_distributor_location where distributor_id="+DistributorID);
			
			//System.out.println("SELECT * from common_distributors where distributor_id="+DistributorID);
			while(rs1.next()){
				LinkedHashMap rows = new LinkedHashMap();
				rows.put("lat",  rs1.getString("lat"));
				rows.put("lng", rs1.getString("lng"));
				rows.put("description",  rs1.getString("location_label"));
				
				if( rs1.getString("address")!=null) {
					rows.put("address",  rs1.getString("address"));
				}else {
					rows.put("address", "");
				}
				ResultSet rs2 = s2.executeQuery("select * from common_cities where id="+rs1.getString("city_id"));
				if(rs2.first()) {
				if( rs2.getString("label")!=null) {
					rows.put("city_id",rs2.getInt("id"));
					rows.put("city",  rs2.getString("label"));
				}else {
					rows.put("city",  "");
				}
				}
				
				if( rs1.getString("phone_no")!=null) {
					rows.put("phone",  rs1.getString("phone_no"));
				}else {
					rows.put("phone",  "");
				}
				
				
				
				distributordetailArray.add(rows);
				
			}
			
			
			
			ResultSet rs3 = s1.executeQuery("SELECT * FROM common_cities;");
			while(rs3.next()){
				LinkedHashMap rows1 = new LinkedHashMap();
			if( rs3.getString("label")!=null) {
				rows1.put("city_id",  rs3.getString("id"));
				rows1.put("city_name",  rs3.getString("label"));
			}
				citiesArray.add(rows1);
			}
			
			
			json.put("rows1", citiesArray);
			
			json.put("rows", distributordetailArray);
			
			
			
			
			
			s.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			json.put("success", "false");
			json.put("error", e.toString());
		}finally{
			try { 
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		out.print(json);
		
	}
	
}
