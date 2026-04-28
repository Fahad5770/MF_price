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


@WebServlet(description = "GL Credit Limit Execute", urlPatterns = { "/cash/GLCreditLimitExecute" })
public class GLCreditLimitExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GLCreditLimitExecute() {
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
		
		long UniqueVoucherID = Utilities.parseLong(request.getParameter("UniqueVoucherID"));
		long CustomerID = Utilities.parseLong(request.getParameter("CustomerID"));
		double CreditLimit = Utilities.parseDouble(request.getParameter("CreditLimit"));
		Date ValidFrom = Utilities.parseDate(request.getParameter("ValidFromDate"));
		Date ValidTo = Utilities.parseDate(request.getParameter("ValidToDate"));
		
		JSONObject obj = new JSONObject();
		
		// do validation
		boolean isValidationClear = true;
		if( CustomerID == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please select Customer");
			isValidationClear = false;
		}else if( CreditLimit == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please enter Credit Limit");
			isValidationClear = false;
		}else if( ValidFrom == null ){
			obj.put("success", "false");
			obj.put("error", "Please enter Valid From");
			isValidationClear = false;
		}else if( ValidTo == null ){
			obj.put("success", "false");
			obj.put("error", "Please enter Valid To");
			isValidationClear = false;
		}// end validation
		
		if( isValidationClear ){
		
			Datasource ds = new Datasource();
			
			
			try {
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				Statement s3 = ds.createStatement();
				
				ResultSet rs2 = s2.executeQuery("select id from gl_customer_credit_limit where uvid="+UniqueVoucherID);
				if( rs2.first() ){
					obj.put("success", "false");
					obj.put("error", "Already Exists");
				}else{
					
					
					s.executeUpdate("update gl_customer_credit_limit set is_active = 0, deactivated_on = now(), deactivated_by = "+UserID+" where customer_id = "+CustomerID+" and is_active = 1");
					
					s.executeUpdate("INSERT INTO `gl_customer_credit_limit`(`customer_id`,`credit_limit`,`valid_from`,`valid_to`,`uvid`,`is_active`,`activated_on`,`activated_by`)VALUES("+CustomerID+","+CreditLimit+","+Utilities.getSQLDate(ValidFrom)+","+Utilities.getSQLDate(ValidTo)+","+UniqueVoucherID+",1,now(),"+UserID+")"); 
					
					obj.put("success", "true");
					
				}
				
				
				ds.commit();
				
				s3.close();
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
		
		} // end if validation
		
		out.print(obj);
		out.close();
		
	}
	
}
