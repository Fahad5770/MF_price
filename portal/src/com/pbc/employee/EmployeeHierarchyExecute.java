package com.pbc.employee;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Employee Tree New Approach", urlPatterns = { "/employee/EmployeeHierarchyExecute" })
public class EmployeeHierarchyExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EmployeeHierarchyExecute() {
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

		int ParentSapCode = Utilities.parseInt(request.getParameter("ParentSapCode1"));
		int ChildSapCode = Utilities.parseInt(request.getParameter("ChildSapCode1"));
		int ActionValue = Utilities.parseInt(request.getParameter("ActionValue1"));
		int ReportingLevelID = Utilities.parseInt(request.getParameter("ReportingLevelID1"));
		
		//System.out.println("Parent Sap Code "+ParentSapCode+" Child Sap Code "+ChildSapCode+" Action Value "+ActionValue);
		
		PrintWriter out = response.getWriter();
		Datasource ds = new Datasource();
		JSONObject obj=new JSONObject();
		JSONArray jr = new JSONArray();	
		try {
			
			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();
			if(ActionValue==1){//move under
				//updating log table
				s.executeUpdate("update user_reporting_to set deactivated_on=now(),deactivated_by="+UserID+",is_active=0 where id="+ChildSapCode+" and is_active=1");
				s.executeUpdate("insert into user_reporting_to(id,reporting_to,activated_on,activated_by,is_active) values("+ChildSapCode+","+ParentSapCode+",now(),"+UserID+",1)");
				
				s.executeUpdate("update users set current_reporting_to="+ParentSapCode+" where id="+ChildSapCode);				
				
				obj.put("success", "true");				
			}
			else if(ActionValue==4){ //add under
				int AddUnerValue = Utilities.parseInt(request.getParameter("AddUnerValue1"));
				int ReportingLevelIDAddUnder = Utilities.parseInt(request.getParameter("ReportingLevelIDAddUnder1"));
				
				//first check for valid sap code
				ResultSet rs = s.executeQuery("select * from users where id="+AddUnerValue);
				if(rs.first()){
					//check if it has assigned to someone already then error
					if(rs.getString("current_reporting_to") == null || rs.getString("current_reporting_to").equals("")){
						s.executeUpdate("update users set current_reporting_to="+ChildSapCode+",current_reporting_level="+ReportingLevelIDAddUnder+" where id="+AddUnerValue);
						s.executeUpdate("insert into user_reporting_to(id,reporting_to,activated_on,activated_by,is_active) values("+AddUnerValue+","+ChildSapCode+",now(),"+UserID+",1)");
						obj.put("success", "true");		
					}
					else{
						obj.put("error", "Already exists");
					}
				}
				else{
					obj.put("error", "Invalid Employee ID");
				}
			}
			else if(ActionValue==2){//remove
				//System.out.println("Me in Remove case ");
				ResultSet rs = s.executeQuery("select * from users where current_reporting_to="+ChildSapCode);
				if(rs.first()){ //if it has 1 or more child then remove not allowed
					obj.put("error", "Couldn`t remove due to child nodes.");
				}else{
					s.executeUpdate("update users set current_reporting_to=null,current_reporting_level=null where id="+ChildSapCode);
					//update log table
					s.executeUpdate("update user_reporting_to set deactivated_on=now(),deactivated_by="+UserID+",is_active=0 where id="+ChildSapCode+" and is_active=1");
					obj.put("success", "true");
				}
			}
			else if(ActionValue==3){//move child
				//System.out.println("Me in Remove case ");
				ResultSet rs = s.executeQuery("select * from users where current_reporting_to="+ChildSapCode);
				while(rs.next())
				{
					//updating log table
					s2.executeUpdate("update user_reporting_to set deactivated_on=now(),deactivated_by="+UserID+",is_active=0 where id="+rs.getInt("id")+" and is_active=1");
					s2.executeUpdate("insert into user_reporting_to(id,reporting_to,activated_on,activated_by,is_active) values("+rs.getInt("id")+","+ParentSapCode+",now(),"+UserID+",1)");
					s1.executeUpdate("update users set current_reporting_to="+ParentSapCode+" where id="+rs.getInt("id"));
				}
				obj.put("success", "true");
			}
			else if(ActionValue==5){//reporting level
				//System.out.println("Me in Remove case ");
				//update log table
				//System.out.println("insert into user_hierarchy_levels(id,reporting_level,activated_on,activated_by,is_active) values("+ChildSapCode+","+ReportingLevelID+",now(),"+UserID+",1)");
				s.executeUpdate("update user_hierarchy_levels set deactivated_on=now(),deactivated_by="+UserID+",is_active=0 where id="+ChildSapCode +" and is_active=1");
				s.executeUpdate("insert into user_hierarchy_levels(id,reporting_level,activated_on,activated_by,is_active) values("+ChildSapCode+","+ReportingLevelID+",now(),"+UserID+",1)");	
				s.executeUpdate("update users set current_reporting_level="+ReportingLevelID+" where id="+ChildSapCode);
					
					obj.put("success", "true");
				
			}
				
			ds.commit();
			
			
			out.print(obj);
			out.close();
			
			s.close();
			s1.close();
			s2.close();
			ds.dropConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			obj.put("error", e.toString());
			e.printStackTrace();
		}finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
