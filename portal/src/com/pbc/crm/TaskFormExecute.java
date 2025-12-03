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


@WebServlet(description = "Task Form Execute", urlPatterns = { "/crm/TaskFormExecute" })
public class TaskFormExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public TaskFormExecute() {
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
		long OutletID = Utilities.parseLong(request.getParameter("OutletID"));
		String OutletName = Utilities.filterString(request.getParameter("OutletName"), 1, 100);
		String OutletAddress = Utilities.filterString(request.getParameter("OutletAddress"), 1, 100);
		String ContactNo = Utilities.filterString(request.getParameter("ContactNo"), 1, 100);
		String PersonContactNo = Utilities.filterString(request.getParameter("PersonContactNo"), 1, 100);
		String Description = Utilities.filterString(request.getParameter("Description"), 1, 1000);
		int RegionID = Utilities.parseInt(request.getParameter("RegionID"));
		
		int TypeID[] = Utilities.parseInt(request.getParameterValues("TypeID"));
		if(TypeID == null){
			response.sendRedirect(com.pbc.util.Utilities.getAccessDeniedPageURL(request));
		}


		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			
			String SQL = "update `crm_tasks` set `outlet_id`="+OutletID+", `outlet_name`='"+OutletName+"', `outlet_address`='"+OutletAddress+"', `outlet_contact_no`='"+ContactNo+"', `edited_on`=now(), `edited_by`="+UserID+", `description`='"+Description+"', `outlet_contact_person`='"+PersonContactNo+"', `region_id`="+RegionID+" where `id`="+ComplaintID;
			
			//System.out.println(SQL);
			s.executeUpdate(SQL);
			
			s.executeUpdate("delete from crm_tasks_list where id="+ComplaintID);
			
			if(TypeID != null){
				for(int i = 0; i < TypeID.length; i++){
					s.executeUpdate("insert into crm_tasks_list(id, type_id) values("+ComplaintID+", "+TypeID[i]+" ) ");
				}
			}
			
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
