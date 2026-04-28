package com.pbc.distributor;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.common.Distributor;
import com.pbc.util.Datasource;
import com.pbc.util.UserAccess;
import com.pbc.util.Utilities;


@WebServlet(description = "Get Distributor Information in JSON", urlPatterns = { "/distributor/GetDistributorByIDJson" })

public class GetDistributorByIDJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetDistributorByIDJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		long SessionUserID=0;
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}else{
			SessionUserID=Utilities.parseLong(session.getAttribute("UserID").toString());
		}
		
		long DistributorID = Utilities.parseLong(request.getParameter("DistributorIDD"));
		
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			int FeatureIDD = Utilities.parseInt(request.getParameter("FeatureIDD")); 
			
			Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureIDD);
			String ScopeDistributors="";
			if( UserDistributor != null ){
				if(UserDistributor.length==0){ //if it has no distributor in its scope then 
					response.sendRedirect("AccessDenied.jsp");
				}else{
					 ScopeDistributors = UserAccess.getDistributorQueryString(UserDistributor);
					// System.out.println("SELECT * from common_distributors cd,common_regions cr  where cd.region_id=cr.region_id and  distributor_id in("+ScopeDistributors+") and distributor_id = " + DistributorID);
					 ResultSet rs = s.executeQuery("SELECT *,(select name from common_outlets co where co.id=cd.desk_outlet_id) desk_outlet_name from common_distributors cd,common_regions cr  where cd.region_id=cr.region_id and  distributor_id in("+ScopeDistributors+") and distributor_id = " + DistributorID);
						
						if (rs.first()){
							
							obj.put("DistributorName", rs.getString("name"));
							obj.put("DistributorCity", rs.getString("city"));
							obj.put("DistributorAddress", rs.getString("address"));
							// obj.put("DistributorRegion", rs.getString("region_short_name")+" - "+rs.getString("region_name"));
							obj.put("DistributorRegion", rs.getString("region_id"));
							obj.put("DistributorRoute", rs.getString("route"));
							obj.put("DistributorContact", rs.getString("contact_no"));
							obj.put("DistributorProductGroupID", rs.getString("product_group_id"));
							obj.put("DistributorTypeID", rs.getString("type_id"));
							obj.put("DistributorMonthCycle", rs.getString("month_cycle"));
							obj.put("DistributorDeskSaleOutletID", rs.getString("desk_outlet_id"));
							obj.put("DistributorDeskSaleOutletName", rs.getString("desk_outlet_name"));
							
							
							obj.put("success", "true");
							
					}else{
							obj.put("error", "This user has no distributor in his scope");
	
							}
				}
			}else{
				obj.put("error", "This user has no distributor in his scope");
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
