package com.pbc.crm;

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


@WebServlet(description = "Task Resolve Form Execute", urlPatterns = { "/crm/TaskResolveFormExecute" })
public class TaskResolveFormExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public TaskResolveFormExecute() {
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
		
		long ComplaintID = Utilities.parseLong(request.getParameter("ComplaintID"));
		String Description = Utilities.filterString(request.getParameter("Description"), 1, 1000);
		int IsResolved = Utilities.parseInt(request.getParameter("IsResolved")); 
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			
			String SQL = "";
			
			obj.put("TaskID", ComplaintID);
			SQL = "update crm_tasks set `is_resolved`=1,`resolved_on`=now(), `resolved_by`="+UserID+", `resolved_description`='"+Description+"', is_sent = 0 where id="+ComplaintID;
			
			
			//System.out.println(SQL);
			s.executeUpdate(SQL);
			
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
