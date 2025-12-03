package com.pbc.employee;

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

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Employee Product Group Execute", urlPatterns = { "/employee/EmployeeProductGroupExecute" })
public class EmployeeProductGroupExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EmployeeProductGroupExecute() {
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
		boolean isEditCase = false;
		int EditID = Utilities.parseInt(request.getParameter("EditID"));
		if(EditID > 0){
			isEditCase = true;
		}
		
		String Name = Utilities.filterString(request.getParameter("EmployeeProductGroupFormName"), 1, 100);
		
		int ProductID[] = Utilities.parseInt(request.getParameterValues("ProductID"));
		
		String IsSelected[] = Utilities.filterString(request.getParameterValues("IsSelected"), 1, 100);
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		Date VoucherDate = new java.util.Date();
		
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			String sql = "";
			
			if(isEditCase){
				sql = "UPDATE employee_product_groups set product_group_name='"+Name+"' where product_group_id="+EditID;
			}else{
				sql = "INSERT INTO `employee_product_groups`(`created_on`,`created_by`,`product_group_name`)VALUES(now(),"+UserID+",'"+Name+"')";
			}
			
			s.executeUpdate(sql);
			
			int EmployeeProductGroupID = 0;
			ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
			if(rs.first()){
				EmployeeProductGroupID = rs.getInt(1);
			}
			
			if(isEditCase){
				EmployeeProductGroupID = EditID;
			}
			
			s2.executeUpdate("delete from employee_product_groups_list where product_group_id="+EmployeeProductGroupID);
			for(int i = 0; i < ProductID.length; i++){
				if(IsSelected[i].equals("on")){
					s2.executeUpdate("INSERT INTO `employee_product_groups_list`(`product_group_id`,`product_id`)VALUES("+EmployeeProductGroupID+","+ProductID[i]+")");
				}
				
			}
			
			obj.put("success", "true");
			
			s2.close();
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
