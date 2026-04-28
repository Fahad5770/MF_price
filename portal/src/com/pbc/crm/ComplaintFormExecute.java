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


@WebServlet(description = "Complaint Form Execute", urlPatterns = { "/crm/ComplaintFormExecute" })
public class ComplaintFormExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ComplaintFormExecute() {
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
		
		int Update = Utilities.parseInt(request.getParameter("Update"));
		long ComplaintID = Utilities.parseLong(request.getParameter("ComplaintID"));
		long OutletID = Utilities.parseLong(request.getParameter("OutletID"));
		String OutletName = Utilities.filterString(request.getParameter("OutletName"), 1, 100);
		String OutletAddress = Utilities.filterString(request.getParameter("OutletAddress"), 1, 100);
		String ContactNo = Utilities.filterString(request.getParameter("ContactNo"), 1, 100);
		String PersonContactNo = Utilities.filterString(request.getParameter("PersonContactNo"), 1, 100);
		int ComplaintType = Utilities.parseInt(request.getParameter("ComplaintType"));
		String Description = Utilities.filterString(request.getParameter("Description"), 1, 1000);
		String UrduWord = Utilities.filterString(request.getParameter("UrduWordHidden"), 1, 1000);
		int ForwardTo = Utilities.parseInt(request.getParameter("ForwardTo"));
		
		int RegionID = Utilities.parseInt(request.getParameter("RegionID"));
		int DistributorID = Utilities.parseInt(request.getParameter("DistributorID"));
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			
			String SQL = "INSERT INTO `crm_complaints_assigned`(`id`,`outlet_id`,`outlet_name`,`outlet_address`,`outlet_contact_no`,`created_on`,`created_by`,`type_id`,`description`,`urdu_description`,`department_id`, `outlet_contact_person`, `region_id`, `distributor_id`)VALUES("+ComplaintID+", "+OutletID+", '"+OutletName+"', '"+OutletAddress+"', '"+ContactNo+"', now(), "+UserID+", "+ComplaintType+", '"+Description+"', '"+UrduWord+"', "+ForwardTo+", '"+PersonContactNo+"', "+RegionID+", "+DistributorID+")";
			
			if( Update == 1 ){
				SQL = "update `crm_complaints_assigned` set `outlet_id`="+OutletID+", `outlet_name`='"+OutletName+"', `outlet_address`='"+OutletAddress+"', `outlet_contact_no`='"+ContactNo+"', `created_on`=now(), `created_by`="+UserID+", `type_id`="+ComplaintType+", `description`='"+Description+"', `urdu_description`='"+UrduWord+"', `department_id`="+ForwardTo+", `outlet_contact_person`='"+PersonContactNo+"', `region_id`="+RegionID+", `distributor_id`="+DistributorID+" where `id`="+ComplaintID;
			}
			//System.out.println(SQL);
			s.executeUpdate(SQL);
			
			s.executeUpdate("update crm_complaints set is_assigned=1 where id="+ComplaintID);
			
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
