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


@WebServlet(description = "GL Cash Receipts Execute", urlPatterns = { "/cash/GLCashReceiptsExecute" })
public class GLCashReceiptsExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GLCashReceiptsExecute() {
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
		long DistributorID = Utilities.parseLong(request.getParameter("DistributorIDSubmitForm"));
		int Type = Utilities.parseInt(request.getParameter("ReceiptTypeSubmitForm"));
		String Narration = Utilities.filterString(request.getParameter("NarrationSubmitForm"), 1, 1000);
		
		double InstrumentAmount[] = Utilities.parseDouble(request.getParameterValues("InstrumentAmount"));
		long InstrumentID[] = Utilities.parseLong(request.getParameterValues("InstrumentID"));
		int InstrumentCategoryID[] = Utilities.parseInt(request.getParameterValues("InstrumentCategoryID"));
		Date InstrumentDate[] = Utilities.parseDate(request.getParameterValues("InstrumentDate"));
		String InstrumentNo[] = Utilities.filterString(request.getParameterValues("InstrumentNo"), 1, 100);
		
		JSONObject obj = new JSONObject();
		
		// do validation
		boolean isValidationClear = true;
		if( DistributorID == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please select Customer");
			isValidationClear = false;
		}else if( Type == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please select Type");
			isValidationClear = false;
		}else {
			boolean isEmpty = true;
			for( int i = 0; i < InstrumentAmount.length; i++ ){
				if( InstrumentAmount[i] > 0 ){
					isEmpty = false;
					break;
				}
			}
			
			if( isEmpty ){
				obj.put("success", "false");
				obj.put("error", "Atleast one Instrument is mandatory.");
				isValidationClear = false;
			}
		}// end validation
		
		if( isValidationClear ){
		
			Datasource ds = new Datasource();
			
			
			try {
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				Statement s3 = ds.createStatement();
				
				ResultSet rs2 = s2.executeQuery("select id from gl_cash_receipts where uvid="+UniqueVoucherID);
				if( rs2.first() ){
					obj.put("success", "false");
					obj.put("error", "Already Exists"); 
				}else{
	
					
					boolean isTransactionComplete = false;
					
					s.executeUpdate("INSERT INTO `gl_cash_receipts`(`uvid`, `document_id`,`customer_id`,`receipt_type`,`narration`,`created_on`,`created_on_date`,`created_by`, `warehouse_id`)VALUES("+UniqueVoucherID+", null, "+DistributorID+", "+Type+", '"+Narration+"', now(), curdate(), "+UserID+", "+WarehouseID+")");
					
					long GLCashReceiptID = 0;
					ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
					if( rs.first() ){
						GLCashReceiptID = rs.getLong(1);
					}
					
					double TotalInstrumentAmount = 0;
					for( int i = 0; i < InstrumentAmount.length; i++ ){ 
						if( InstrumentAmount[i] > 0 ){
							TotalInstrumentAmount += InstrumentAmount[i];
							
							long InstrumentAccountID = 0;
							ResultSet rs5 = s3.executeQuery("SELECT id FROM gl_accounts where category_id="+InstrumentCategoryID[i]+" and warehouse_id="+WarehouseID);
							if( rs5.first() ){
								InstrumentAccountID = rs5.getLong("id");
							}
							
							long InstrumentCanBounce = 0;
							ResultSet rs6 = s3.executeQuery("SELECT can_bounce FROM gl_cash_instruments where id="+InstrumentID[i]);
							if( rs6.first() ){
								InstrumentCanBounce = rs6.getInt("can_bounce"); 
							}
							
							if( InstrumentCanBounce == 1 ){
								s.executeUpdate("INSERT INTO `gl_cash_receipts_instruments`(`id`,`instrument_id`,`instrument_no`,`instrument_date`,`amount`, `status_id`, `status_on`, `status_by`, `instrument_account_id`)VALUES("+GLCashReceiptID+", "+InstrumentID[i]+", '"+InstrumentNo[i]+"', "+ Utilities.getSQLDate(InstrumentDate[i])+", "+InstrumentAmount[i]+", 1, now(), "+UserID+", "+InstrumentAccountID+")");
							}else{
								s.executeUpdate("INSERT INTO `gl_cash_receipts_instruments`(`id`,`instrument_id`,`instrument_no`,`instrument_date`,`amount`, `instrument_account_id`)VALUES("+GLCashReceiptID+", "+InstrumentID[i]+", '"+InstrumentNo[i]+"', "+ Utilities.getSQLDate(InstrumentDate[i])+", "+InstrumentAmount[i]+", "+InstrumentAccountID+")");
							}
						}
					}
					
					s.executeUpdate("INSERT INTO `gl_transactions`(`document_id`,`document_type_id`,`document_date`,`remarks`,`created_on`,`created_on_date`,`created_by`, `cash_receipt_id`)VALUES("+GLCashReceiptID+", 54, now(), '"+Narration+"', now(), curdate(), "+UserID+", "+GLCashReceiptID+")");
					
					long GLTransactionID = 0;
					ResultSet rs3 = s.executeQuery("select LAST_INSERT_ID()");
					if( rs3.first() ){
						GLTransactionID = rs3.getLong(1);
					}
					
					for( int i = 0; i < InstrumentAmount.length; i++ ){ 
						if( InstrumentAmount[i] > 0 ){
							
							long WarehouseAccountID = 0;
							ResultSet rs5 = s3.executeQuery("SELECT id FROM gl_accounts where category_id="+InstrumentCategoryID[i]+" and warehouse_id="+WarehouseID);
							if( rs5.first() ){
								WarehouseAccountID = rs5.getLong("id");
							}
							 
							long CustomerAccountID = 0;
							ResultSet rs4 = s3.executeQuery("SELECT id FROM gl_accounts where customer_id="+DistributorID+" and type_id="+Type); 
							while( rs4.next() ){
								CustomerAccountID = rs4.getLong(1);
							}
							
							s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`credit`,`remarks`)VALUES("+GLTransactionID+", "+CustomerAccountID+", "+InstrumentAmount[i]+", 'Cash Receipt#"+GLCashReceiptID+" ')");
							s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`debit`,`remarks`)VALUES("+GLTransactionID+", "+WarehouseAccountID+", "+InstrumentAmount[i]+", 'Cash Receipt#"+GLCashReceiptID+" ')");
							
							isTransactionComplete = true;
							
							long NullifyAccountID = 0;
							ResultSet rs7 = s3.executeQuery("SELECT nullify_account_id FROM gl_cash_instruments where id="+InstrumentID[i]);
							if( rs7.first() ){
								
								if( rs7.getLong("nullify_account_id") > 0 ){
									
									NullifyAccountID = rs7.getLong("nullify_account_id");
									
									s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`credit`,`remarks`)VALUES("+GLTransactionID+", "+WarehouseAccountID+", "+InstrumentAmount[i]+", 'Cash Receipt#"+GLCashReceiptID+" ')");
									s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`debit`,`remarks`)VALUES("+GLTransactionID+", "+NullifyAccountID+", "+InstrumentAmount[i]+", 'Cash Receipt#"+GLCashReceiptID+" ')");
									
								}
							}
							
						}
					}
					
					
					if (isTransactionComplete == true){
						
						obj.put("success", "true");
						obj.put("PrintID", GLCashReceiptID);
						
					}else{
						
						ds.rollback();
						obj.put("success", "false");
						obj.put("error", "Transaction Incomplete");
						
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
