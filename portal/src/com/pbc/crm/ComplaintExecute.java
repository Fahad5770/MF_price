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


@WebServlet(description = "Complaint Execute", urlPatterns = { "/crm/ComplaintExecute" })
public class ComplaintExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ComplaintExecute() {
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
		
		
		long ComplaintTableID = Utilities.parseLong(request.getParameter("ComplaintID"));
		long ComplaintID = Utilities.parseLong(request.getParameter("CurrentComplaintID"));
		long OutletID = Utilities.parseLong(request.getParameter("OutletID"));
		String OutletName = Utilities.filterString(request.getParameter("OutletName"), 1, 100);
		String OutletAddress = Utilities.filterString(request.getParameter("OutletAddress"), 1, 100);
		String ContactNo = Utilities.filterString(request.getParameter("ContactNo"), 1, 100);
		String PersonContactNo = Utilities.filterString(request.getParameter("PersonContactNo"), 1, 100);
		int ComplaintType = Utilities.parseInt(request.getParameter("ComplaintType"));
		String Description = Utilities.filterString(request.getParameter("Description"), 1, 1000);
		String UrduDescription = Utilities.filterString(request.getParameter("UrduWordHidden"), 1, 1000);
		
		int IsResolved = Utilities.parseInt(request.getParameter("IsResolved"));
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			String SQL = "INSERT INTO `crm_complaints`(`complaint_id`,`outlet_id`,`outlet_name`,`outlet_address`,`outlet_contact_no`,`created_on`,`created_by`,`type_id`,`description`, `outlet_contact_person`, `urdu_description`)VALUES("+ComplaintID+","+OutletID+",'"+OutletName+"','"+OutletAddress+"', '"+ContactNo+"', now(), "+UserID+", "+ComplaintType+", '"+Description+"', '"+PersonContactNo+"', '"+UrduDescription+"')";
			
			if( IsResolved == 1 ){
				SQL = "update crm_complaints set `is_resolved`=1,`resolved_on`=now(), `resolved_by`="+UserID+", `resolved_description`='"+Description+"' where id="+ComplaintTableID;
				
				s2.executeUpdate("update crm_complaints_assigned set `is_resolved`=1,`resolved_on`=now(), `resolved_by`="+UserID+", `resolved_description`='"+Description+"' where id="+ComplaintTableID);
			}
			
			//System.out.println(SQL);
			
			s.executeUpdate(SQL);
			
			if( IsResolved == 1 ){
				ResultSet rs = s.executeQuery("select complaint_id from crm_complaints where id="+ComplaintTableID);
				if(rs.first()){
					ComplaintID = rs.getLong(1);
				}
			}
			
			obj.put("success", "true");
			obj.put("InsertedComplaintID", ComplaintID);
			
			ds.commit();
			
			s2.close();
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
