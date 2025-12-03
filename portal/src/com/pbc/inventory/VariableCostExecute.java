package com.pbc.inventory;

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


@WebServlet(description = "Variable Cost Execute", urlPatterns = { "/inventory/VariableCostExecute" })
public class VariableCostExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public VariableCostExecute() {
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
		JSONObject obj = new JSONObject();
		
		long SapCode[] = Utilities.parseLong(request.getParameterValues("SapCode"));		
		double Cost[] = Utilities.parseDouble(request.getParameterValues("Cost"));		
		
		boolean isValid = true;
		for(int i = 0; i < SapCode.length ; i++){
			if(Cost[i] <= 0){
				isValid = false;
				break;
			}
		}
		
		if(isValid){
			Datasource ds = new Datasource();
			
			try {
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				
				for(int i = 0; i < SapCode.length ; i++){
					s.executeUpdate("UPDATE `inventory_products_variable_costs` SET `cost` = "+Cost[i]+" WHERE `sap_code` = "+SapCode[i]);
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
		}else{
			obj.put("success", "false");
			obj.put("error", "Field Missing");
		}
		
		out.print(obj);
		out.close();
		
	}
	
}
