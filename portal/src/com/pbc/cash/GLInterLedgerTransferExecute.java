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


@WebServlet(description = "GL Inter Ledger Transfer Execute", urlPatterns = { "/cash/GLInterLedgerTransferExecute" })
public class GLInterLedgerTransferExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public GLInterLedgerTransferExecute() {
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
		int DebitAccountTypeID = Utilities.parseInt(request.getParameter("DebitAccountTypeID"));
		int CreditAccountTypeID = Utilities.parseInt(request.getParameter("CreditAccountTypeID"));
		double Amount = Utilities.parseDouble(request.getParameter("Amount"));
		String Reason = Utilities.filterString(request.getParameter("Reason"), 1, 100);
		
		JSONObject obj = new JSONObject();
		
		// do validation
		boolean isValidationClear = true;
		if( CustomerID == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please select Customer");
			isValidationClear = false;
		}else if( DebitAccountTypeID == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please select Debit Account");
			isValidationClear = false;
		}else if( CreditAccountTypeID == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please select Credit Account");
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
				
				ResultSet rs2 = s2.executeQuery("select id from gl_customer_interledger_transfer where uvid="+UniqueVoucherID);
				if( rs2.first() ){
					obj.put("success", "false");
					obj.put("error", "Already Exists");
				}else{
					
					long DebitAccountID = 0;
					long CreditAccountID = 0;
					ResultSet rs = s.executeQuery("select (SELECT id FROM gl_accounts where customer_id="+CustomerID+" and type_id="+DebitAccountTypeID+") debit_account, (SELECT id FROM gl_accounts where customer_id="+CustomerID+" and type_id="+CreditAccountTypeID+") credit_account");
					if(rs.first()){
						DebitAccountID = rs.getLong("debit_account");
						CreditAccountID = rs.getLong("credit_account");
					}
					
					s.executeUpdate("INSERT INTO `gl_customer_interledger_transfer`(`customer_id`,`debit_account_type_id`,`credit_account_type_id`,`debit_account_id`,`credit_account_id`,`amount`,`created_on`,`created_by`,`uvid`,`reason`)VALUES("+CustomerID+","+DebitAccountTypeID+","+CreditAccountTypeID+","+DebitAccountID+","+CreditAccountID+","+Amount+",now(),"+UserID+", "+UniqueVoucherID+", '"+Reason+"')"); 
					
					long GLInterLedgerTransferID = 0;
					ResultSet rs3 = s.executeQuery("select last_insert_id()");
					if(rs3.first()){
						GLInterLedgerTransferID = rs3.getLong(1);
					}
					
					s.executeUpdate("INSERT INTO `gl_transactions`(`document_id`,`document_type_id`,`document_date`,`remarks`,`created_on`,`created_on_date`,`created_by`, `inter_ledger_transfer_id`)VALUES("+GLInterLedgerTransferID+", 62, now(), '', now(), curdate(), "+UserID+", "+GLInterLedgerTransferID+")");
					
					long GLTransactionID = 0;
					ResultSet rs4 = s.executeQuery("select last_insert_id()");
					if(rs4.first()){
						GLTransactionID = rs4.getLong(1);
					}
					
					s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`debit`,`remarks`)VALUES("+GLTransactionID+", "+DebitAccountID+", "+Amount+", 'Inter Ledger Transfer#"+GLInterLedgerTransferID+" ')");
					s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`credit`,`remarks`)VALUES("+GLTransactionID+", "+CreditAccountID+", "+Amount+", 'Inter Ledger Transfer#"+GLInterLedgerTransferID+" ')");
					
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
