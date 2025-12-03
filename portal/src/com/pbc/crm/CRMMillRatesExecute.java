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


@WebServlet(description = "Get Mill Rates in JSON", urlPatterns = { "/crm/CRMMillRatesExecute" })
public class CRMMillRatesExecute extends HttpServlet {
	private static final long serialVersionUID = 1654111L;
       
    public CRMMillRatesExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}
		
		
		int[] ID=Utilities.parseInt(request.getParameterValues("TableID"));
		int[] MillID = Utilities.parseInt(request.getParameterValues("MillID"));
	    String[] Name=Utilities.filterString(request.getParameterValues("Name"), 102, 200);
	    String[] Zone=Utilities.filterString(request.getParameterValues("Zone"), 102, 200);;
	    String[] Circle=Utilities.filterString(request.getParameterValues("Circle"), 102, 200);;
	    String[] IsLP=Utilities.filterString(request.getParameterValues("LP"), 102, 200);;
	    double[] Rate=Utilities.parseDouble(request.getParameterValues("Rate"));
	    double[] LPRate=Utilities.parseDouble(request.getParameterValues("LPRate"));
		
		
		
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
			
			ds.startTransaction();


			for(int i=0;i<ID.length;i++){
				
				
				//s.executeUpdate("delete from crman_own_rates where center_id="+ID[i]);
				//s.executeUpdate("delete from crman_centers where id="+ID[i]);
				
				int IsLPInt=0;
				
				if(IsLP[i].equals("Yes")){
					IsLPInt = 1;
				}else{
					IsLPInt=0;
				}
				
				s.executeUpdate("update crman_centers set mill_id="+MillID[i]+",name='"+Name[i]+"',circle='"+Circle[i]+"',zone='"+Zone[i]+"',is_lp="+IsLPInt+" where id="+ID[i]);
				
				s.executeUpdate("update crman_own_rates set wef_date=now(),rate="+Rate[i]+",tpt_rate="+LPRate[i]+" where center_id="+ID[i]);
				
				
				
			}
			
		   
			
			
			
				
			
			obj.put("success", "true");
			
				ds.commit();
			
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			try {
				
				ds.rollback();
				
				obj.put("success", "false");
				obj.put("error", e.toString());
				e.printStackTrace();
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
	
	
}
