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


@WebServlet(description = "Task Resolve Form Execute", urlPatterns = { "/distributor/DistributorAreaAllocationFormExecute" })
public class DistributorAreaAllocationFormExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DistributorAreaAllocationFormExecute() {
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
		
		
		int LastYear = Utilities.parseInt(request.getParameter("lastYear"));
		int CurrentYear = Utilities.parseInt(request.getParameter("CurrentYear"));
		int Month = Utilities.parseInt(request.getParameter("Month"));
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			
			
			
			
			
			
			
			s.executeUpdate("insert into common_distributors_area_allocation (year_previous,year_current,month,distributor_id_previous,distributor_id_current,volume_percentage,created_on,created_by) values("+LastYear+","+CurrentYear+","+Month+",100662,100662,10,now(),"+UserID+")");
			
			obj.put("success", "true");
			
			obj.put("CurrentYear", CurrentYear);
			obj.put("CurrentMonth", Month);
			
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
