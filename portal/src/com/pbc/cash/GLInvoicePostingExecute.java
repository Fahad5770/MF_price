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


@WebServlet(description = "GL Cash Receipts Execute", urlPatterns = { "/cash/GLInvoicePostingExecute" })
public class GLInvoicePostingExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GLInvoicePostingExecute() {
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
		int WarehouseID = Utilities.parseInt(request.getParameter("WarehouseID"));
		long OrderNo = Utilities.parseLong(request.getParameter("OrderNo"));
		Date OrderDate = Utilities.parseDate(request.getParameter("OrderDate"));
		long InvoiceNo = Utilities.parseLong(request.getParameter("InvoiceNo"));
		Date InvoiceDate = Utilities.parseDate(request.getParameter("InvoiceDate"));
		double InvoiceAmount = Utilities.parseDouble(request.getParameter("InvoiceAmountHidden"));
		long CustomerID = Utilities.parseLong(request.getParameter("CustomerID"));
		double CurrentBalance = Utilities.parseDouble(request.getParameter("CurrentBalance"));
		double CreditLimit = Utilities.parseDouble(request.getParameter("CreditLimit"));
		//double AdvanceAgainstOrder = Utilities.parseDouble(request.getParameter("AdvanceAgainstOrderHidden"));
		
		JSONObject obj = new JSONObject();
		
		// do validation
		boolean isValidationClear = true;
		if( OrderNo == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please select Customer");
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
				
				ResultSet rs2 = s2.executeQuery("select id from gl_invoice_posting where uvid="+UniqueVoucherID);
				if( rs2.first() ){
					obj.put("success", "false");
					obj.put("error", "Already Exists"); 
				}else{
					
					long CustomerAccountID = 0;
					ResultSet rs3 = s3.executeQuery("SELECT id FROM gl_accounts where customer_id="+CustomerID+" and type_id=1");
					if( rs3.first() ){
						CustomerAccountID = rs3.getLong("id");
					}
					
					long SalesAccountID = 0;
					ResultSet rs4 = s3.executeQuery("SELECT id FROM gl_accounts where category_id=4");
					if( rs4.first() ){
						SalesAccountID = rs4.getLong("id");
					}
					
					long OrderPostingSalesAccountID = 0;
					ResultSet rs9 = s3.executeQuery("SELECT id FROM gl_accounts where customer_id="+CustomerID+" and type_id=6");
					if( rs9.first() ){
						OrderPostingSalesAccountID = rs9.getLong("id");
					}
					
					double TotalDebit = 0;
					double TotalCredit = 0;
					double CurrentBalanceTemp = 0;
					ResultSet rs5 = s3.executeQuery("SELECT sum(debit) total_debit, sum(credit) total_credit FROM gl_transactions_accounts where account_id="+CustomerAccountID);
					if( rs5.first() ){
						TotalDebit = rs5.getDouble("total_debit");
						TotalCredit = rs5.getDouble("total_credit");
						CurrentBalanceTemp = TotalDebit - TotalCredit;
						CurrentBalanceTemp = CurrentBalanceTemp * (-1);
					}

					boolean isOrderPosted = false;
					double ReAdvanceAgainstOrder = 0;
					String OrderPostingID = null;
					ResultSet rs7 = s.executeQuery("SELECT id, order_amount FROM gl_order_posting where order_no = "+OrderNo);
					if(rs7.first()){
						OrderPostingID = rs7.getString(1);
						ReAdvanceAgainstOrder = rs7.getDouble(2);
						isOrderPosted = true;
					}
					
					
					double CreditLimitTemp = 0;
					ResultSet rs8 = s3.executeQuery("SELECT credit_limit FROM gl_customer_credit_limit where customer_id="+CustomerID+" and curdate() between valid_from and valid_to and is_active=1");
					if(rs8.first()){
						CreditLimitTemp = rs8.getDouble(1);
					}
					
					if( InvoiceAmount <= ( CurrentBalanceTemp + ReAdvanceAgainstOrder + CreditLimitTemp) ){
						
						
						s.executeUpdate("INSERT INTO `gl_invoice_posting`(`order_no`,`invoice_no`,`invoice_amount`,`customer_id`,`customer_account_id`,`sales_account_id`,`current_balance`,`credit_limit`,`uvid`,`created_on`,`created_on_date`,`created_by`, `warehouse_id`, `order_date`, `invoice_date`)VALUES("+OrderNo+","+InvoiceNo+","+InvoiceAmount+","+CustomerID+","+CustomerAccountID+","+SalesAccountID+","+CurrentBalanceTemp+","+CreditLimit+","+UniqueVoucherID+",now(),curdate(),"+UserID+","+WarehouseID+", "+Utilities.getSQLDate(OrderDate)+", "+Utilities.getSQLDate(InvoiceDate)+")");
						
						long InvoicePostingID = 0;
						ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
						if( rs.first() ){
							InvoicePostingID = rs.getLong(1);
						}
						
						s.executeUpdate("INSERT INTO `gl_transactions`(`document_id`,`document_type_id`,`document_date`,`remarks`,`created_on`,`created_on_date`,`created_by`, `invoice_posting_id`)VALUES("+InvoicePostingID+", 59, now(), '', now(), curdate(), "+UserID+", "+InvoicePostingID+")");
						
						long GLTransactionID = 0;
						ResultSet rs6 = s.executeQuery("select LAST_INSERT_ID()");
						if( rs6.first() ){
							GLTransactionID = rs6.getLong(1);
						}
						
						if(isOrderPosted == true){
							s.executeUpdate("update gl_order_posting set is_invoiced=1, invoiced_on = now() where id="+OrderPostingID);
							
							
							s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`debit`,`remarks`)VALUES("+GLTransactionID+", "+OrderPostingSalesAccountID+", "+ReAdvanceAgainstOrder+", 'Order Posting#"+OrderPostingID+" ')");
							s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`credit`,`remarks`)VALUES("+GLTransactionID+", "+CustomerAccountID+", "+ReAdvanceAgainstOrder+", 'Order Posting#"+OrderPostingID+" ')");
						}
						
						s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`debit`,`remarks`)VALUES("+GLTransactionID+", "+CustomerAccountID+", "+InvoiceAmount+", 'Invoice Posting#"+InvoicePostingID+" ')");
						s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`credit`,`remarks`)VALUES("+GLTransactionID+", "+SalesAccountID+", "+InvoiceAmount+", 'Invoice Posting#"+InvoicePostingID+" ')");
						
						
						obj.put("success", "true");
						
						

						
						/*
						if (CustomerID == 100356 || CustomerID == 100951 || CustomerID == 100431){
							Utilities.sendSMS("923334566993", "Customer ID:"+CustomerID+" Order#"+OrderNo+" Amount:"+InvoiceAmount);
							Utilities.sendPBCEmail(new String[]{"shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk"}, null, null, "Invoice Alert: Customer ID:"+CustomerID+" Order#"+OrderNo+" Amount:"+InvoiceAmount, "Customer ID:"+CustomerID+" Order#"+OrderNo+" Amount:"+InvoiceAmount, null);
						}
						*/
						
					}else{
					obj.put("success", "false");
						obj.put("error", "Insufficient Balance");
					}
					
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
