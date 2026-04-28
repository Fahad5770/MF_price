package com.pbc.crm;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Complaint Form Execute", urlPatterns = { "/crm/MarketWatchFormExecute" })
public class MarketWatchFormExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MarketWatchFormExecute() {
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
		
		
		long MarketWatchID = Utilities.parseLong(request.getParameter("WatchID"));
		long OutletID = Utilities.parseLong(request.getParameter("OutletID"));
		String OutletName = Utilities.filterString(request.getParameter("OutletName"), 1, 100);
		String OutletAddress = Utilities.filterString(request.getParameter("OutletAddress"), 1, 100);		
		int RegionID = Utilities.parseInt(request.getParameter("RegionID"));
		int DistributorID = Utilities.parseInt(request.getParameter("DistID"));
		String DistributorName = Utilities.filterString(request.getParameter("DistName"),1,100);
		
		
		long  PackageID[] = Utilities.parseLong(request.getParameterValues("mwpackageid"));
		int  CompanyID[] = Utilities.parseInt(request.getParameterValues("companyid"));
		double  Rate[] = Utilities.parseDouble(request.getParameterValues("rate"));
		String  Remarks[] = Utilities.filterString(request.getParameterValues("remarks"), 1, 100);
		
		//System.out.println("hey - "+PackageID.length);		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			
			//Master Table Updation
			s.executeUpdate("update crm_market_watch set outlet_id="+OutletID+",outlet_name='"+OutletName+"',outlet_address='"+OutletAddress+"',distributor_id="+DistributorID+",region_id="+RegionID+ " where id="+MarketWatchID);
			
			//now updating child record
			
			if(PackageID!=null){				
			
				for(int i=0;i<PackageID.length;i++){
					s.executeUpdate("update crm_market_watch_rates set rate="+Rate[i]+",promotion_remarks='"+Remarks[i]+"' where id="+MarketWatchID +" and package_id="+PackageID[i]+" and company_id="+CompanyID[i]);
				}
			}
			
			
			
			
			obj.put("success", "true");
			
			ds.commit();
			
			s.close();
			
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
