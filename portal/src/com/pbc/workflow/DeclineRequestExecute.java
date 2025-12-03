package com.pbc.workflow;

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


@WebServlet(description = "Executes decline process of a workflow request", urlPatterns = { "/workflow/DeclineRequestExecute" })

public class DeclineRequestExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DeclineRequestExecute() {
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
		JSONObject obj=new JSONObject();
		
		int StepID = Utilities.parseInt(Utilities.filterString(request.getParameter("StepID"), 0, MaxLength.CURRENCY));
		long RequestID = Utilities.parseLong(Utilities.filterString(request.getParameter("RequestID"), 0, MaxLength.CURRENCY));
		String WorkflowStepRemarks = Utilities.filterString(request.getParameter("WorkflowStepRemarks"), 1, MaxLength.WORKFLOW_REMARKS);

		try {
			Workflow wf = new Workflow();
			
			wf.doDecline(RequestID, StepID, Long.parseLong(UserID), WorkflowStepRemarks);
			
			wf.close();			
			
			response.setContentType("application/json");
			
			obj.put("success", "true");
			obj.put("RequestID", ""+RequestID);
			
		} catch (Exception e) {
			obj.put("success", "false");
			obj.put("error", e.toString());
		}
		
		out.print(obj);
		out.close();
		
	}
	
}