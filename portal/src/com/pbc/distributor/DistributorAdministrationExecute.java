package com.pbc.distributor;

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


@WebServlet(description = "Distributor Administration Group ", urlPatterns = { "/distributor/DistributorAdministrationExecute" })
public class DistributorAdministrationExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DistributorAdministrationExecute() {
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
		
		int DistributorAdministrationActionID = Utilities.parseInt(request.getParameter("DistributorAdiministrationActionID"));
		long DistributorID = Utilities.parseLong(request.getParameter("DistributorID"));
		int DistributorProductGroupID = Utilities.parseInt(request.getParameter("SelectProductGroupID"));
		int DistributorTypeID = Utilities.parseInt(request.getParameter("SelectDistributorTypeID"));
		int DistributorRegionID = Utilities.parseInt(request.getParameter("DistributorRegion"));
		
		long DeskOutletID= Utilities.parseLong(request.getParameter("DeskSaleOutletID"));
		
		
		String ProductGroupID = "null";
		if(DistributorProductGroupID > 0){
			ProductGroupID = DistributorProductGroupID+"";
		}
		
		int MonthCycle = Utilities.parseInt(request.getParameter("DistributorMonthCycle"));
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();		
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			
			if(DistributorAdministrationActionID==1){ //assign distributor product groups
				/*if(DistributorProductGroupID !=-1){
					s.executeUpdate("Update common_distributors set product_group_id="+DistributorProductGroupID+", type_id="+DistributorTypeID+" where distributor_id="+DistributorID);
				}else{
					s.executeUpdate("Update common_distributors set product_group_id=null where distributor_id="+DistributorID);
				}*/
				
			//	System.out.println("Update common_distributors set region_id="+DistributorRegionID+", product_group_id="+ProductGroupID+", type_id="+DistributorTypeID+", month_cycle="+MonthCycle+", desk_outlet_id="+DeskOutletID+" where distributor_id="+DistributorID);
			
				s.executeUpdate("Update common_distributors set region_id="+DistributorRegionID+", product_group_id="+ProductGroupID+", type_id="+DistributorTypeID+", month_cycle="+MonthCycle+", desk_outlet_id="+DeskOutletID+" where distributor_id="+DistributorID);
				
				obj.put("success", "true");
			}
			s.close();
			ds.dropConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("success", "false");
			//obj.put("error", e.toString());
			obj.put("error", "Vehicle Number already exists");
			e.printStackTrace();
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
