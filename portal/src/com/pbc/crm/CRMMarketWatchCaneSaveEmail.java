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


@WebServlet(description = "Complaint Verification Execute", urlPatterns = { "/crm/CRMMarketWatchCaneSaveEmail" })
public class CRMMarketWatchCaneSaveEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CRMMarketWatchCaneSaveEmail() {
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
		
		
		String Message = Utilities.filterString(request.getParameter("CRMMWCaneMsg"), 102, 500);
		
		long CaneIDD = Utilities.parseLong(request.getParameter("CRMMWCaneID"));
		
		String Comments = Utilities.filterString(request.getParameter("CRMMWCaneComment"), 102, 500);
		
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		try {
			
			ds.createConnectionKSML();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			
			//Updating comments
			
			s.executeUpdate("update crman_valid_messages set comments='"+Comments+"' where id="+CaneIDD);
			
			//System.out.println("update crman_valid_messages set comments='"+Comments+"' where id="+CaneIDD);
			
			
			
			Date NewDate = new Date();
			
			//System.out.println("select created_on  from crman_valid_messages where id="+CaneIDD);
			
			ResultSet rs = s.executeQuery("select created_on  from crman_valid_messages where id="+CaneIDD);
			if(rs.first()){
				NewDate = rs.getTimestamp("created_on");
			}
			
			//System.out.println(NewDate);
			
			s.executeUpdate("delete from crman_email_messages where created_on_date="+Utilities.getSQLDate(NewDate));
			//System.out.println("select date(created_on) date15 from crman_valid_messages where id="+CaneIDD);
			
			
			
			
			s.executeUpdate("insert into crman_email_messages(created_on,message,created_on_date) values("+Utilities.getSQLDateTime(NewDate)+",'"+Message+"',"+Utilities.getSQLDate(NewDate)+")");
			
			
			
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
