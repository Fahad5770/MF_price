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


@WebServlet(description = "Executes sampling adjustment", urlPatterns = { "/sampling/SamplingAdjustmentExecute" })

public class SamplingAdjustmentExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SamplingAdjustmentExecute() {
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
		
		long OutletID = Utilities.parseLong(request.getParameter("OutletID"));
		double amount = Utilities.parseDouble(request.getParameter("AdjustmentAmount"));
		int AdjustmentTypeID = Utilities.parseInt(request.getParameter("AdjustmentType"));
		
		String remarks = Utilities.filterString(request.getParameter("AdjustmentRemarks"), 1, 255);
		
		boolean success = false;
		
		try {
			SamplingPosting sp = new SamplingPosting();
			sp.postBalanceAdjustment(OutletID, Long.parseLong(UserID), AdjustmentTypeID, amount, remarks);
			sp.close();
			success = true;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.sendRedirect(request.getContextPath()+"/SamplingAdjustment.jsp?success="+success);
	}
	
}