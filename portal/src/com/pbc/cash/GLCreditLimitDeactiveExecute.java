package com.pbc.cash;

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


@WebServlet(description = "GL Credit Limit Execute", urlPatterns = { "/cash/GLCreditLimitDeactiveExecute" })
public class GLCreditLimitDeactiveExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GLCreditLimitDeactiveExecute() {
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

		
		
		long CustomerID =Utilities.parseLong(request.getParameter("CustomerID"));
		String Deactivation_Remarks = request.getParameter("Deactivation_Remarks");
		
		//System.out.print("Customer is: "+CustomerID +" & end Deactivation_Remarks is : "+Deactivation_Remarks);
		
		PrintWriter out = response.getWriter();
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();	
		
		
try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			//System.out.println("SELECT * FROM pep.ec_empty_credit_limit where request_id="+RequestId);
			
			
			ResultSet rs = s.executeQuery("SELECT * FROM pep.gl_customer_credit_limit where customer_id="+CustomerID+" and is_active=1");
				if(rs.first())
				{
					
					String Query2="update pep.gl_customer_credit_limit set original_valid_to="+Utilities.getSQLDate(rs.getDate("valid_to")) +" where customer_id="+CustomerID+" and is_active=1";
					s.executeUpdate(Query2);
					
					String Query3="update pep.gl_customer_credit_limit set deactivation_reason='"+ Deactivation_Remarks +"' where customer_id="+CustomerID+" and is_active=1";
					//System.out.print(Query3);
					s.executeUpdate(Query3);
					
					Date d= new Date();						
					Date previous = DateUtils.addDays(d, -1);
					
					int updated=0;
					String Query="update pep.gl_customer_credit_limit set valid_to="+Utilities.getSQLDate(previous)+" where customer_id="+CustomerID+" and is_active=1";
					updated=s.executeUpdate(Query);
					
				
					
					if(updated !=0)
					{
						obj.put("success", "true");
						obj.put("Message", "Updated Successfully");
					}
					else
					{
						obj.put("success", "false");
						obj.put("Message", "Update Failed");
					}
					
					
				}
				else
				{
					obj.put("success", "false");
					obj.put("error", "Customer ID Does not Exists");
					
				}
				
			ds.commit();
				
			
			s.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
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
