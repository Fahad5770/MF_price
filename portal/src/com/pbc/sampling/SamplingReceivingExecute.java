package com.pbc.sampling;

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


@WebServlet(description = "Executes sampling receiving", urlPatterns = { "/sampling/SamplingReceivingExecute" })

public class SamplingReceivingExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SamplingReceivingExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();
		JSONObject obj=new JSONObject();
		
		long DistributorID = Utilities.parseLong(request.getParameter("DistributorID"));
		long ApprovalID[] = Utilities.parseLong(request.getParameterValues("ApprovalID"));
		
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			s.executeUpdate("insert into sampling_receiving (distributor_id, received_on, received_by) values("+DistributorID+", now(), "+UserID+")");
			
			long ReceivingID = 0;
			ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
			if (rs.first()){
				ReceivingID = rs.getLong(1);
			}			
			
			for (int i = 0; i < ApprovalID.length; i++){
				s.executeUpdate("insert into sampling_receiving_items (receiving_id, approval_id) values ("+ReceivingID+", "+ApprovalID[i]+")");
				s.executeUpdate("update sampling_monthly_approval set is_received = 1 where approval_id = "+ApprovalID[i]);
			}
			
			response.setContentType("application/json");
			
			obj.put("success", "true");
			obj.put("ReceivingID", ""+ReceivingID);
			
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		}
		
		out.print(obj);
		out.close();
		
	}
	
}