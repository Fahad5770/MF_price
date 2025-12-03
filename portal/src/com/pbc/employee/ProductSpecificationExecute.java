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


@WebServlet(description = "Product Specification Execute", urlPatterns = { "/employee/ProductSpecificationExecute" })
public class ProductSpecificationExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ProductSpecificationExecute() {
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
		
		int EmployeeSAPCode = Utilities.parseInt(request.getParameter("EmployeeSAPCode"));
		int EmployeeProductGroupID = Utilities.parseInt(request.getParameter("EmployeeProductGroup"));
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			
			String sql = "";
			
			if(isEditCase){
				sql = "UPDATE `employee_product_specification` SET `employee_product_group_id` = "+EmployeeProductGroupID+", `employee_sap_code` = "+EmployeeSAPCode+" WHERE `employee_product_specification_id` = "+EditID;
			}else{
				sql = "INSERT INTO `employee_product_specification`(`created_on`,`created_by`,`employee_product_group_id`,`employee_sap_code`)VALUES(now(),"+UserID+","+EmployeeProductGroupID+","+EmployeeSAPCode+")";
			}
			
			s.executeUpdate(sql);
			
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
