package com.pbc.crm;
import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

import com.mysql.jdbc.Connection;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;


@WebServlet(description = "Get Category ID in JSON", urlPatterns = { "/crm/CrmHelpDeskComplaintInfoJson" })
public class CrmHelpDeskComplaintInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1654111L;
       
    public CrmHelpDeskComplaintInfoJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		
		Datasource ds = new Datasource();
		
		response.setContentType("application/json");
		JSONObject obj = new JSONObject();
		JSONObject json = new JSONObject();
		JSONArray jr = new JSONArray();
	
		
		PrintWriter out = response.getWriter();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s1=ds.createStatement();
			
		    int CatID=Utilities.parseInt(request.getParameter("CatID"));
		    int id=Utilities.parseInt(request.getParameter("id"));
		   
		    
		    //int ID=0;
		    //String LabelName="";
		  
		    //System.out.println("SELECT *,(Select name from common_outlets where id="+id+") outletname FROM crm_help_desk_complaint_sub_category where category_id="+CatID);
		    		
			ResultSet rs = s.executeQuery("SELECT * FROM crm_help_desk_complaint_sub_category where category_id="+CatID);
				while(rs.next()){
				
				 
			    //System.out.print(rs.getInt(1));
			    //System.out.print(rs.getString(2));
				 LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("id", rs.getInt(1));
					rows.put("label", rs.getString(2));
					
					
					jr.add(rows);
			    
			  
			}
			ResultSet rs1 = s1.executeQuery("Select name from common_outlets where id="+id);
			//System.out.println("Select name from common_outlets where id="+id);
			while(rs1.next()){
				 //System.out.println(rs1.getString("name"));
				 obj.put("name", rs1.getString("name"));
			}
			obj.put("exists","true");
			
			
			obj.put("rows",jr);
		
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			obj.put("success", "false");
			obj.put("error", e.toString());
			
		}		
		out.print(obj);
		//out.print(json);
		out.close();
	}
	
	
	
}
