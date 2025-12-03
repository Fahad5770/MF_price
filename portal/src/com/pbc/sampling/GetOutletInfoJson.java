package com.pbc.sampling;

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

import com.pbc.outlet.Outlet;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.common.Distributor;
import com.pbc.util.UserAccess;


@WebServlet(description = "Get Outlet Master Information in JSON", urlPatterns = { "/sampling/GetOutletInfoJson" })
public class GetOutletInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetOutletInfoJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		String OutletID = Utilities.filterString(request.getParameter("OutletID"), 0, 20);
		int FeatureID = Utilities.parseInt(request.getParameter("FeatureID"));
		
		
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			
			String UserAccessWhere = "";
			if(FeatureID != 0)
			{
				Distributor [] DistributorObj = UserAccess.getUserFeatureDistributor(Utilities.parseLong(session.getAttribute("UserID").toString()), FeatureID);

				String DistributorsIds = UserAccess.getDistributorQueryString(DistributorObj);
				//System.out.println(DistributorsIds);
				UserAccessWhere = " and (Customer_ID in ("+DistributorsIds+") or outlet_id in (select outlet_id from common_outlets_distributors_view where distributor_id in ("+DistributorsIds+")))";
			}
			
			
			
			ResultSet rs = s.executeQuery("SELECT Outlet_Name, Bsi_Name, Address, Region, ASM_ID, CR_ID, Market_Name, Vehicle, Latitude, Longitude,customer_id,(select name from pep.common_distributors where Customer_ID=distributor_id) as distributor_name,owner,telepohone FROM outletmaster  where Outlet_ID = "+OutletID+UserAccessWhere);
			//System.out.println("SELECT Outlet_Name, Bsi_Name, Address, Region, ASM_ID, CR_ID, Market_Name, Vehicle, Latitude, Longitude,customer_id,(select name from pep.common_distributors where om.Customer_ID=distributor_id) as distributor_name,owner FROM outletmaster om where om.Outlet_ID = "+OutletID+UserAccessWhere);
			//ResultSet rs = s.executeQuery("SELECT om.Outlet_Name,om.Bsi_Name,om.Address,om.Region,om.ASM_ID,(select concat(ev.first_name, ' ', ev.last_name) as asm_name from  pep.employee_view ev where ev.sap_code=om.ASM_ID order by ev.end_date desc limit 1) as asm_name,om.CR_ID,(select concat(ev.first_name, ' ', ev.last_name) as asm_name from  pep.employee_view ev where ev.sap_code=om.CR_ID order by ev.end_date desc limit 1) as cr_name,om.RSM_ID,(select concat(ev.first_name, ' ', ev.last_name) as asm_name from  pep.employee_view ev where ev.sap_code=om.RSM_ID order by ev.end_date desc limit 1) as rsm_name,om.Market_Name,om.Vehicle,om.Latitude,om.Longitude,om.customer_id,(select name from pep.common_distributors where om.Customer_ID=distributor_id) as distributor_name FROM pep.outletmaster om where Outlet_ID = "+OutletID);
			
			
			
			if (rs.first()){
				
			  	double CurrentBalance = 0;
			  	// Get Opening Balance
			  	
			  	ResultSet rs3 = s2.executeQuery("SELECT sum(debit)-sum(credit) from sampling_posting_accounts where outlet_id = "+OutletID);
			  	if (rs3.first()){
			  		CurrentBalance = rs3.getDouble(1);
			  	}
				
				
				obj.put("exists", "true");
				obj.put("OutletName", rs.getString(1));
				obj.put("BusinessType", rs.getString(2));
				obj.put("address", rs.getString(3));
				obj.put("region", rs.getString(4));
				obj.put("asm", rs.getString(5));
				obj.put("cr", rs.getString(6));
				obj.put("market", rs.getString(7));
				obj.put("vehicle", rs.getString(8));
				obj.put("latitude", rs.getString(9));
				obj.put("longitude", rs.getString(10));
				obj.put("CurrentBalance", CurrentBalance);
				
				Outlet CurrentOutlet = new Outlet();
				CurrentOutlet.ID = Utilities.parseLong(OutletID);
				long CSDInfo[] = CurrentOutlet.getCSDDistributor();
				long CSDRegionID = CSDInfo[0];
				long CSDDistributorID = CSDInfo[1];
				
				obj.put("region_id_csd", CSDRegionID);
				obj.put("distributor_id_csd", CSDDistributorID);
				
				obj.put("distributor_id", rs.getString("customer_id"));
				obj.put("distributor_name", rs.getString("distributor_name"));
				obj.put("owner_name", rs.getString("owner"));
				obj.put("owner_tele", rs.getString("telepohone"));
				
			}else{

				obj.put("exists", "false");

			}
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();
			
			s2.close();
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			sendErrorRedirect(request,response,Utilities.getErrorPageURL(request),e);
		}		
		
	}
	
	protected void sendErrorRedirect(HttpServletRequest request, HttpServletResponse response, String errorPageURL, Throwable e) throws ServletException, IOException {
		request.setAttribute ("javax.servlet.jsp.jspException", e);
		getServletConfig().getServletContext().getRequestDispatcher(errorPageURL).forward(request, response);
	}
	
}
