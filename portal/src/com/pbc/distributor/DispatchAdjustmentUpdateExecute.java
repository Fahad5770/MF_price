package com.pbc.distributor;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Dispatch Adjustment Update Dispatch Table", urlPatterns = { "/distributor/DispatchAdjustmentUpdateExecute" })
public class DispatchAdjustmentUpdateExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DispatchAdjustmentUpdateExecute() {
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
		
		long DispatchID = Utilities.parseLong(request.getParameter("DispatchID"));
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();		
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			
				s.executeUpdate("update inventory_sales_dispatch set is_adjusted=1,adjusted_on=now(),adjusted_by="+UserID+" where id="+DispatchID);
				
				
				
				obj.put("success", "true");
			s.close();
			ds.dropConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
