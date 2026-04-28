package com.pbc.crm;

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

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;


@WebServlet(description = "Get Mill Rates in JSON", urlPatterns = { "/crm/GetMillRatesInfoJson" })
public class GetMillRatesInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1654111L;
       
    public GetMillRatesInfoJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}
		
		
		//System.out.println(PackageID);
		Datasource ds = new Datasource();
		response.setContentType("application/json");
		JSONObject obj = new JSONObject();
		JSONArray jr = new JSONArray();
	
		
		PrintWriter out = response.getWriter();
		
		try {
			
			ds.createConnectionKSML();
			
			Statement s = ds.createStatement();
			
			
			
			
			
			Statement s2 = ds.createStatement();
			
			Datasource dsSAP = new Datasource();
		    dsSAP.createConnectionToSAPDB();
		    Statement sSAP = dsSAP.createStatement();


		    int TableID=Utilities.parseInt(request.getParameter("TableID"));
		   
		    
		    int ID=0;
		    String Name="";
		    String Zone="";
		    String Circle="";
		    String IsLP="";
		    double Rate=0;
		    double LPRate=0;
		    int MillID=0;
		    
			ResultSet rs = s.executeQuery("SELECT cc.mill_id, cc.id,name,circle,zone,if(is_lp,'Yes','No') is_lp,rate,tpt_rate FROM crman_centers cc join crman_own_rates cor on cc.id=cor.center_id where cc.id="+TableID);
			while(rs.next()){
				
				
				
				 ID=rs.getInt("id");
			     Name=rs.getString("name");
			     Zone=rs.getString("zone");
			     Circle=rs.getString("circle");
			     IsLP=rs.getString("is_lp");
			     Rate=rs.getDouble("rate");
			     LPRate=rs.getDouble("tpt_rate");
				 MillID = rs.getInt("mill_id");
				
			     obj.put("ID", ID);
			     obj.put("Name", Name);
			     obj.put("Zone", Zone);
			     obj.put("Circle", Circle);
			     obj.put("IsLP", IsLP);
			     obj.put("Rate", Rate);
			     obj.put("LPRate", LPRate);
			     obj.put("MillID", MillID);
			     
				
				 	 
			}
			
			
			
			
			
			
			obj.put("success","true");
			obj.put("rows",jr);
			
			
			s2.close();
				
			
			
			
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			obj.put("success", "false");
			obj.put("error", e.toString());
		}		
		out.print(obj);
		out.close();
	}
	
	
	
}
