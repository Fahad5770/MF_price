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


@WebServlet(description = "Price List ", urlPatterns = { "/distributor/DistributorEmployeeExecute" })
public class DistributorEmployeeExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DistributorEmployeeExecute() {
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
		
		String DistributorEmpName = Utilities.filterString(request.getParameter("DistributorEmpName"), 1, 100);
		String DistributorDeptName = Utilities.filterString(request.getParameter("DistributorDeptName"),1,100);
		String DistributorEmpDesignation = Utilities.filterString(request.getParameter("DistributorEmpDesignation"),1,100);
		String DistributorEmpNIC = Utilities.filterString(request.getParameter("DistributorEmpNIC"),1,100);
		long DistributorEmpDistID = Utilities.parseLong(request.getParameter("DistributorEmpDistID"));
		
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();		
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			
				if(Utilities.parseLong(request.getParameter("isEditCase"))==0)//insertion case master table
				{
					s.executeUpdate("insert into distributor_employees (name,department,designation,nic,distributor_id) values('"+DistributorEmpName+"','"+DistributorDeptName+"','"+DistributorEmpDesignation+"',"+DistributorEmpNIC+","+DistributorEmpDistID+")");					
				} 
				else if(Utilities.parseLong(request.getParameter("isEditCase"))==1) //updation case for master table
				{
					s.executeUpdate("update distributor_employees set name='"+DistributorEmpName+"',department='"+DistributorDeptName+"',designation='"+DistributorEmpDesignation+"',nic="+DistributorEmpNIC+" where id="+Utilities.parseLong(request.getParameter("DistEmpIDForWhole")));					
				}
				
				obj.put("success", "true");				
			
			s.close();
			ds.dropConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("success", "false");
			//obj.put("error", e.toString());
			obj.put("error", "NIC# already exists");
			e.printStackTrace();
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
