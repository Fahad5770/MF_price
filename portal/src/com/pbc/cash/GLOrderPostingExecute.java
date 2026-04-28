package com.pbc.cash;

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

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "GL Cash Receipts Execute", urlPatterns = { "/cash/GLOrderPostingExecute" })
public class GLOrderPostingExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GLOrderPostingExecute() {
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
		
		long POSNR[] = Utilities.parseLong(request.getParameterValues("POSNR"));
		String MATNR[] = Utilities.filterString(request.getParameterValues("MATNR"), 1, 100);
		String ARKTX[] = Utilities.filterString(request.getParameterValues("ARKTX"), 1, 100);
		String VRKME[] = Utilities.filterString(request.getParameterValues("VRKME"), 1, 100);
		long KWMENG[] = Utilities.parseLong(request.getParameterValues("KWMENG"));
		long ApprovalID[] = Utilities.parseLong(request.getParameterValues("ApprovalID"));
		
		int ProductID[] = Utilities.parseInt(request.getParameterValues("ProductID"));
		int RawCases[] = Utilities.parseInt(request.getParameterValues("RawCases"));
		int Units[] = Utilities.parseInt(request.getParameterValues("Units"));
		long TotalUnits[] = Utilities.parseLong(request.getParameterValues("TotalUnits"));
		
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
			Datasource ds2 = new Datasource();
			
			
			try {
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				Statement s3 = ds.createStatement();
				Statement s5 = ds.createStatement();
				
				ds2.createConnectionToSAPDB();
				Statement s4 = ds2.createStatement();
				
				ResultSet rs2 = s2.executeQuery("select id from gl_order_posting where uvid="+UniqueVoucherID);
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
					ResultSet rs4 = s3.executeQuery("SELECT id FROM gl_accounts where customer_id="+CustomerID+" and type_id=6");
					if( rs4.first() ){
						SalesAccountID = rs4.getLong("id");
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
					
					double CreditLimitTemp = 0;
					ResultSet rs7 = s3.executeQuery("SELECT credit_limit FROM gl_customer_credit_limit where customer_id="+CustomerID+" and curdate() between valid_from and valid_to and is_active=1");
					if(rs7.first()){
						CreditLimitTemp = rs7.getDouble(1);
					}
					
					if( InvoiceAmount <= (CurrentBalanceTemp+CreditLimitTemp) ){
						
						
						
						s.executeUpdate("INSERT INTO `gl_order_posting`(`order_no`,`order_date`,`order_amount`,`customer_id`,`customer_ledger_account_id`,`customer_advance_account_id`,`current_balance`,`credit_limit`,`uvid`,`created_on`,`created_on_date`,`created_by`,`warehouse_id`)VALUES("+OrderNo+","+Utilities.getSQLDate(OrderDate)+","+InvoiceAmount+","+CustomerID+","+CustomerAccountID+","+SalesAccountID+","+CurrentBalanceTemp+","+CreditLimit+","+UniqueVoucherID+",now(),curdate(),"+UserID+","+WarehouseID+")");
						
						
						long OrderPostingID = 0;
						ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
						if( rs.first() ){
							OrderPostingID = rs.getLong(1);
						}
						
						if (POSNR != null){
							for(int i = 0; i < POSNR.length; i++){
								//ResultSet rs8 = s.executeQuery("SELECT id FROM inventory_sales_promotions_request where request_id="+ApprovalID[i]+" and curdate() between valid_from and valid_to and is_active=1");
								//if(rs8.first()){
								
								if(ApprovalID[i] > 0){
									s5.executeUpdate("INSERT INTO `gl_order_posting_promotions`(`id`,`posnr`,`matnr`,`arktx`,`vrkme`,`kwmeng`,`request_id`, `product_id`, `raw_cases`, `units`, `total_units`)VALUES("+OrderPostingID+","+POSNR[i]+",'"+MATNR[i]+"','"+ARKTX[i]+"','"+VRKME[i]+"',"+KWMENG[i]+","+ApprovalID[i]+", "+ProductID[i]+", "+RawCases[i]+", "+Units[i]+", "+TotalUnits[i]+")");
								}
								//}
								
							}
						}
						
						
						s.executeUpdate("INSERT INTO `gl_transactions`(`document_id`,`document_type_id`,`document_date`,`remarks`,`created_on`,`created_on_date`,`created_by`, `order_posting_id`)VALUES("+OrderPostingID+", 64, now(), '', now(), curdate(), "+UserID+", "+OrderPostingID+")");
						
						long GLTransactionID = 0;
						ResultSet rs6 = s.executeQuery("select LAST_INSERT_ID()");
						if( rs6.first() ){
							GLTransactionID = rs6.getLong(1);
						}
						
						s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`debit`,`remarks`)VALUES("+GLTransactionID+", "+CustomerAccountID+", "+InvoiceAmount+", 'Order Posting#"+OrderPostingID+" ')");
						s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`credit`,`remarks`)VALUES("+GLTransactionID+", "+SalesAccountID+", "+InvoiceAmount+", 'Order Posting#"+OrderPostingID+" ')");
						
						obj.put("success", "true");
						
					}else{
					obj.put("success", "false");
						obj.put("error", "Insufficient Balance");
					}
					
				}
				
				
				
				ds.commit();
				
				s5.close();
				s4.close();
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
					ds2.dropConnection();
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
