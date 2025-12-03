package com.pbc.common;

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
import com.pbc.util.UserAccess;
import com.pbc.util.Utilities;


@WebServlet(description = "Get Outlet By SAP Code JSON", urlPatterns = { "/common/GetOutletBySAPCodeJSON" })
public class GetOutletBySAPCodeJSON extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
    public GetOutletBySAPCodeJSON() {
        super();
        //System.out.println("contructor");
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//System.out.println("service");
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
		
		long SAPCode = Utilities.parseLong(request.getParameter("SAPCode"));
		int FeatureID = Utilities.parseInt(request.getParameter("FeatureID"));
		Distributor UserDistributor[];
		String DistributorIDs = "";
		System.out.println("FeatureID "+FeatureID);
		if(FeatureID > 0){
			
			try {
				
				UserDistributor = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
				
				for(int i = 0; i < UserDistributor.length; i++){
					if(i > 0){
						DistributorIDs += ", ";
					}
					
					DistributorIDs += UserDistributor[i].DISTRIBUTOR_ID;
				}
				
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | SQLException e1) {
				
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
		}
		Datasource ds = new Datasource();
		 
		try { 
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			
			String SQL = "SELECT concat(Outlet_Name, ' ', Bsi_Name) Outlet_Name FROM outletmaster where outlet_id = "+SAPCode;
			if(FeatureID > 0){
				SQL = "SELECT concat(Outlet_Name, ' ', Bsi_Name) Outlet_Name FROM outletmaster where outlet_id = "+SAPCode+" and (Customer_ID in ("+DistributorIDs+") or outlet_id in (select outlet_id from common_outlets_distributors_view where distributor_id in ("+DistributorIDs+")))";
			}
			System.out.println(SQL);
			
			ResultSet rs = s.executeQuery(SQL);
			if (rs.first()){
				obj.put("exists", "true");
				obj.put("OutletName", rs.getString("Outlet_Name"));
			}else{
				obj.put("exists", "false");
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
