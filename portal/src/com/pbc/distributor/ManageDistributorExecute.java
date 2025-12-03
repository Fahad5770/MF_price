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

import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Distributor Target Execute", urlPatterns = { "/distributor/ManageDistributorExecute" })
public class ManageDistributorExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ManageDistributorExecute() {
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
		
		long DistributorID[] = Utilities.parseLong(request.getParameterValues("DistributorID"));
		int IsSundayOff[] = Utilities.parseInt(request.getParameterValues("IsSundayOff"));
		int IsActive[] = Utilities.parseInt(request.getParameterValues("IsActive"));
		int Type[] = Utilities.parseInt(request.getParameterValues("Type"));
		int MonthCycle[] = Utilities.parseInt(request.getParameterValues("MonthCycle"));
		long SND_ID[] = Utilities.parseLong(request.getParameterValues("SND_ID"));
		long RSM_ID[] = Utilities.parseLong(request.getParameterValues("RSM_ID"));
		long TDM_ID[] = Utilities.parseLong(request.getParameterValues("TDM_ID"));
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			
			for(int i = 0; i < DistributorID.length; i++){
				
				String SNDIDVal = null;
				if( SND_ID[i] > 0 ){
					SNDIDVal = SND_ID[i]+"";
				}
				
				String RSMIDVal = null;
				if( RSM_ID[i] > 0 ){
					RSMIDVal = RSM_ID[i]+"";
				}
				
				String TDMIDVal = null;
				if( TDM_ID[i] > 0 ){
					TDMIDVal = TDM_ID[i]+"";
				}
			//	System.out.println("IsSundayOff : "+IsSundayOff[i]);
			//	System.out.println("UPDATE common_distributors SET is_active="+IsActive[i]+", type_id="+Type[i]+", month_cycle="+MonthCycle[i]+", snd_id="+SNDIDVal+", rsm_id="+RSMIDVal+", tdm_id="+TDMIDVal+", is_sunday_off="+IsSundayOff[i]+" WHERE `distributor_id` = "+DistributorID[i]);
				s.executeUpdate("UPDATE common_distributors SET is_active="+IsActive[i]+", type_id="+Type[i]+", month_cycle="+MonthCycle[i]+", snd_id="+SNDIDVal+", rsm_id="+RSMIDVal+", tdm_id="+TDMIDVal+", is_sunday_off="+IsSundayOff[i]+" WHERE `distributor_id` = "+DistributorID[i]);


			}
			
			obj.put("success", "true");
			
			ds.commit();
			s.close();
			
		} catch (Exception e) {
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
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
