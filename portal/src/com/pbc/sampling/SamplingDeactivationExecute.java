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


@WebServlet(description = "Executes sampling deactivation", urlPatterns = { "/sampling/SamplingDeactivationExecute" })

public class SamplingDeactivationExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SamplingDeactivationExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}else{
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}
		
		long SamplingID = Utilities.parseLong(request.getParameter("DeactivationSamplingID"));
		Date DeactivatedOn = Utilities.parseDate(request.getParameter("DeactivatedOn"));
		
		boolean success = false;
		
		
		try {
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			
			s.executeUpdate("update sampling set active = 0, deactivated_on = " + Utilities.getSQLDate(DeactivatedOn) + ", deactivated_by = "+UserID + ", deactivation_timestamp = now() where sampling_id = "+SamplingID);
			
			ds.dropConnection();
			
			success = true;
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		response.sendRedirect(request.getContextPath()+"/SamplingDeactivation.jsp?success="+success);
		
	}
	
}