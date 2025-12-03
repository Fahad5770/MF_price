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

@WebServlet(description = "GL Account Adjustment Execute", urlPatterns = { "/cash/GLAccountAdjustmentExecute" })
public class GLAccountAdjustmentExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public GLAccountAdjustmentExecute() {
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
		int AccountTypeID = Utilities.parseInt(request.getParameter("AccountTypeID"));
		int Type = Utilities.parseInt(request.getParameter("Type"));
		double Amount = Utilities.parseDouble(request.getParameter("Amount"));
		String Reason = Utilities.filterString(request.getParameter("Reason"), 1, 100);
		
		JSONObject obj = new JSONObject();

		// do validation
		boolean isValidationClear = true;
		if( CustomerID == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please select Customer");
			isValidationClear = false;
		}else if( AccountTypeID == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please select Account");
			isValidationClear = false;
		}else if( Type == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please select Type");
			isValidationClear = false;
		}else if( Amount == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please enter Amount");
			isValidationClear = false;
		}else if( Reason.equals("") ){
			obj.put("success", "false");
			obj.put("error", "Please enter Reason");
			isValidationClear = false;
		}// end validation
		 
		if( isValidationClear ){
		
			Datasource ds = new Datasource();
			
			try {
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				
				ResultSet rs2 = s2.executeQuery("select id from gl_customer_account_adjustment where uvid="+UniqueVoucherID);
				if( rs2.first() ){
					obj.put("success", "false");
					obj.put("error", "Already Exists");
				}else{
					
					long CustomerAccountID = 0;
					ResultSet rs = s.executeQuery("select (SELECT id FROM gl_accounts where customer_id="+CustomerID+" and type_id="+AccountTypeID+") customer_account_id ");
					if(rs.first()){
						CustomerAccountID = rs.getLong("customer_account_id");
					}
					
					s.executeUpdate("INSERT INTO `gl_customer_account_adjustment`(`customer_id`,`account_type_id`,`customer_account_id`,`type_id`,`amount`,`created_on`,`created_by`,`uvid`,`reason`)VALUES("+CustomerID+","+AccountTypeID+","+CustomerAccountID+","+Type+","+Amount+",now(),"+UserID+","+UniqueVoucherID+",'"+Reason+"')"); 
					
					long GLAccountAdjustmentID = 0;
					ResultSet rs3 = s.executeQuery("select last_insert_id()");
					if(rs3.first()){
						GLAccountAdjustmentID = rs3.getLong(1);
					}
					
					s.executeUpdate("INSERT INTO `gl_transactions`(`document_id`,`document_type_id`,`document_date`,`remarks`,`created_on`,`created_on_date`,`created_by`, `customer_account_adjustment_id`)VALUES("+GLAccountAdjustmentID+", 63, now(), '', now(), curdate(), "+UserID+", "+GLAccountAdjustmentID+")");
					
					long GLTransactionID = 0;
					ResultSet rs4 = s.executeQuery("select last_insert_id()");
					if(rs4.first()){
						GLTransactionID = rs4.getLong(1);
					}
					
					long AdjustmentAccountID = 0;
					ResultSet rs5 = s.executeQuery("SELECT id FROM gl_accounts where category_id=17");
					if(rs5.first()){
						AdjustmentAccountID = rs5.getLong(1);
					}
					
					long DebitAccountID = 0;
					long CreditAccountID = 0;
					
					// Add Case
					if(Type == 1){
						DebitAccountID = AdjustmentAccountID;
						CreditAccountID = CustomerAccountID;
					}else{
						// Subtract Case
						DebitAccountID = CustomerAccountID;
						CreditAccountID = AdjustmentAccountID;
					}
					
					s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`debit`,`remarks`)VALUES("+GLTransactionID+", "+DebitAccountID+", "+Amount+", 'Account Adjustment#"+GLAccountAdjustmentID+" - "+Reason+"')");
					s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`credit`,`remarks`)VALUES("+GLTransactionID+", "+CreditAccountID+", "+Amount+", 'Account Adjustment#"+GLAccountAdjustmentID+" - "+Reason+"')");
					
					obj.put("success", "true");
					
					
					
				}
				
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
		
		} // end if validation
		
		out.print(obj);
		out.close();
		
	}
	
}
