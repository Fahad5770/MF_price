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


@WebServlet(description = "Complaint Resolve Execute", urlPatterns = { "/crm/ComplaintResolveExecute" })
public class ComplaintResolveExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ComplaintResolveExecute() {
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
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();		
		
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			//s.executeUpdate("");
			
			long ComplaintID = Utilities.parseLong(request.getParameter("ComplaintID1"));
			String ComplaintRemarks = Utilities.filterString(request.getParameter("Remarks"), 1, 500);
			
			s.executeUpdate("update crm_complaints set is_resolved=1,resolved_on=now(),resolved_by="+UserID+",resolved_description='"+ComplaintRemarks+"' where id="+ComplaintID);
			
			
			//System.out.println("hello - "+ComplaintID+" , Remarks - "+ComplaintRemarks);	
			
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
