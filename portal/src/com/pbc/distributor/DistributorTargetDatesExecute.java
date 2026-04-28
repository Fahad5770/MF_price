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


@WebServlet(description = "Distributor Target Execute", urlPatterns = { "/distributor/DistributorTargetDatesExecute" })
public class DistributorTargetDatesExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DistributorTargetDatesExecute() {
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
		
	
		 int Month = Utilities.parseInt(request.getParameter("Month"));
		 int Year = Utilities.parseInt(request.getParameter("Year"));
		 
		 
		// System.out.println(Month+" - "+Year);
		 
		 
		
		int DistributorID[] = Utilities.parseInt(request.getParameterValues("DistributorID"));
		Date StartDate[] = Utilities.parseDate(request.getParameterValues("StartDateDis"));
		Date EndDate[] = Utilities.parseDate(request.getParameterValues("EndDateDis"));
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		//checking date
		
		for(int i=0;i<DistributorID.length;i++){
			if(StartDate[i].after(EndDate[i])){
				obj.put("success", "false");
				obj.put("error", "Please enter valid date");
				
				out.print(obj);
				out.close();
				return;
			}
		}
		
		
		
				
		try {
			
			ds.createConnection();
		
			
			Statement s = ds.createStatement();
			
			//System.out.println(DistributorID.length);
			
			
			for(int i=0;i<DistributorID.length;i++){
				
				//System.out.println("update distributor_targets set start_date='"+StartDate[i]+"', end_date='"+EndDate[i]+"' where distributor_id="+DistributorID[i]+" and month="+Month+" and year="+Year);
				
				s.executeUpdate("update distributor_targets set start_date="+Utilities.getSQLDate(StartDate[i])+", end_date="+Utilities.getSQLDate(EndDate[i])+" where distributor_id="+DistributorID[i]+" and month="+Month+" and year="+Year);
			}
			
			obj.put("success", "true");
			
			
			s.close();
			
		} catch (Exception e) {
			
			
			
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
