package com.pbc.mrd;

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


@WebServlet(description = "MRD Generate Report Update Observations", urlPatterns = { "/mrd/MRDGenerateReportUpdateObservations" })
public class MRDGenerateReportUpdateObservations extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MRDGenerateReportUpdateObservations() {
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
		
		long SurveySummaryID =  Utilities.parseLong(request.getParameter("SurveySummaryID"));
		int OutcomeID =  Utilities.parseInt(request.getParameter("OutcomeID"));
		String Observations =  Utilities.filterString(request.getParameter("Observations"), 1, 100);

		PrintWriter out = response.getWriter();
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			
			s.executeUpdate("update `mrd_survey_summary` set outcome_id="+OutcomeID+", observations='"+Observations+"' where id="+SurveySummaryID);
			
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