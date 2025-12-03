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


@WebServlet(description = "GL One Time Credit Execute", urlPatterns = { "/cash/GLOneTimeCreditExecute" })
public class GLOneTimeCreditExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GLOneTimeCreditExecute() {
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
		long DistributorID = Utilities.parseLong(request.getParameter("DistributorID"));
		long ApproverID = Utilities.parseLong(request.getParameter("ApproverID"));
		String Narration = Utilities.filterString(request.getParameter("Narration"), 1, 1000);
		double Amount = Utilities.parseDouble(request.getParameter("Amount"));
		
		
		
		JSONObject obj = new JSONObject();
		
		// do validation
		boolean isValidationClear = true;
		if( DistributorID == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please select Customer");
			isValidationClear = false;
		}if( ApproverID == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please select Approver");
			isValidationClear = false;
		}if( Amount == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please enter Amount");
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
				
				ResultSet rs2 = s2.executeQuery("select id from gl_one_time_credit where uvid="+UniqueVoucherID);
				if( rs2.first() ){
					obj.put("success", "false");
					obj.put("error", "Already Exists"); 
				}else{
	
					s.executeUpdate("INSERT INTO `gl_one_time_credit`(`customer_id`,`narration`,`created_on`,`created_on_date`,`created_by`,`uvid`,`amount`, `approver_id`)VALUES("+DistributorID+",'"+Narration+"',now(),curdate(),"+UserID+","+UniqueVoucherID+","+Amount+", "+ApproverID+")"); 
					
					long GLOneTimeCreditID = 0;
					ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
					if( rs.first() ){
						GLOneTimeCreditID = rs.getLong(1);
					}
					
					s.executeUpdate("INSERT INTO `gl_transactions`(`document_id`,`document_type_id`,`document_date`,`remarks`,`created_on`,`created_on_date`,`created_by`, `one_time_credit_id`)VALUES("+GLOneTimeCreditID+", 60, now(), '"+Narration+"', now(), curdate(), "+UserID+", "+GLOneTimeCreditID+")"); 
					
					long GLTransactionID = 0;
					ResultSet rs3 = s.executeQuery("select LAST_INSERT_ID()");
					if( rs3.first() ){
						GLTransactionID = rs3.getLong(1);
					}
					
					
							
					long CustomerAccountIDCredit = 0;
					ResultSet rs5 = s3.executeQuery("SELECT id FROM gl_accounts where customer_id="+DistributorID+" and type_id=1");
					if( rs5.first() ){
						CustomerAccountIDCredit = rs5.getLong("id");
					}
					
					ResultSet rs4 = s3.executeQuery("SELECT id FROM gl_accounts where customer_id="+DistributorID+" and type_id=3");
					if( rs4.first() ){
						long CustomerAccountIDDebit = rs4.getLong("id");
						s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`debit`,`remarks`)VALUES("+GLTransactionID+", "+CustomerAccountIDDebit+", "+Amount+", 'One Time Credit#"+GLOneTimeCreditID+" ')");
						s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`credit`,`remarks`)VALUES("+GLTransactionID+", "+CustomerAccountIDCredit+", "+Amount+", 'One Time Credit#"+GLOneTimeCreditID+" ')");
						
					}
						
					
					
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
