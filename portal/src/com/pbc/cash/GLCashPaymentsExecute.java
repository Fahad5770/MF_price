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


@WebServlet(description = "GL Cash Payments Execute", urlPatterns = { "/cash/GLCashPaymentsExecute" })
public class GLCashPaymentsExecute extends HttpServlet { 
	private static final long serialVersionUID = 1L;
       
    public GLCashPaymentsExecute() {
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
		long AccountID = Utilities.parseLong(request.getParameter("AccountIDSubmitForm"));
		
		String Narration = Utilities.filterString(request.getParameter("NarrationSubmitForm"), 1, 1000);
		
		double InstrumentAmount[] = Utilities.parseDouble(request.getParameterValues("InstrumentAmount"));
		long InstrumentID[] = Utilities.parseLong(request.getParameterValues("InstrumentID"));
		long InstrumentSerialNo[] = Utilities.parseLong(request.getParameterValues("InstrumentSerialNo"));
		int InstrumentCategoryID[] = Utilities.parseInt(request.getParameterValues("InstrumentCategoryID"));
		Date InstrumentDate[] = Utilities.parseDate(request.getParameterValues("InstrumentDate"));
		String InstrumentNo[] = Utilities.filterString(request.getParameterValues("InstrumentNo"), 1, 100);
		
		JSONObject obj = new JSONObject();
		
		// do validation
		boolean isValidationClear = true;
		if( AccountID == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please select Account");
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
				
				ResultSet rs2 = s2.executeQuery("select id from gl_cash_payments where uvid="+UniqueVoucherID);
				if( rs2.first() ){
					obj.put("success", "false");
					obj.put("error", "Already Exists"); 
				}else{
	
					s.executeUpdate("INSERT INTO `gl_cash_payments`(`uvid`,`account_id`,`narration`,`created_on`,`created_on_date`,`created_by`, `warehouse_id`)VALUES("+UniqueVoucherID+", "+AccountID+", '"+Narration+"', now(), curdate(), "+UserID+", "+WarehouseID+")");
					
					long GLCashPaymentID = 0;
					ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
					if( rs.first() ){
						GLCashPaymentID = rs.getLong(1);
					}
					
					double TotalInstrumentAmount = 0;
					for( int i = 0; i < InstrumentAmount.length; i++ ){ 
						if( InstrumentAmount[i] > 0 ){
							TotalInstrumentAmount += InstrumentAmount[i];
							String InstrumentSerialNoTemp = null;
							if( InstrumentSerialNo[i] > 0 ){
								InstrumentSerialNoTemp = InstrumentSerialNo[i]+"";
							}
							
							long InstrumentAccountID = 0;
							ResultSet rs5 = s3.executeQuery("SELECT id FROM gl_accounts where category_id="+InstrumentCategoryID[i]+" and warehouse_id="+WarehouseID);
							if( rs5.first() ){
								InstrumentAccountID = rs5.getLong("id");
							}
							
							
							String InstrumentNoTemp = InstrumentNo[i];
							Date InstrumentDateTemp = InstrumentDate[i];
							double InstrumentAmountTemp = InstrumentAmount[i];
							
							if( InstrumentSerialNo[i] > 0 ){
								InstrumentNoTemp = null;
								InstrumentDateTemp = null;
								InstrumentAmountTemp = 0;
								
								ResultSet rs6 = s3.executeQuery("SELECT instrument_no, instrument_date, amount FROM gl_cash_receipts_instruments where serial_no="+InstrumentSerialNoTemp);
								if( rs6.first() ){
									InstrumentNoTemp = rs6.getString("instrument_no");
									InstrumentDateTemp = rs6.getDate("instrument_date");
									InstrumentAmountTemp = rs6.getDouble("amount");
								}
							}
							
							s.executeUpdate("INSERT INTO `gl_cash_payments_instruments`(`id`,`instrument_id`,`instrument_no`,`instrument_date`,`amount`, `receipt_serial_no`, `instrument_account_id`, status_id, status_on, status_by)VALUES("+GLCashPaymentID+", "+InstrumentID[i]+", '"+InstrumentNoTemp+"', "+ Utilities.getSQLDate(InstrumentDateTemp)+", "+InstrumentAmountTemp+", "+InstrumentSerialNoTemp+", "+InstrumentAccountID+", 1, now(), "+UserID+")");
							
							s.executeUpdate("update gl_cash_receipts_instruments set status_id=2, status_on=now(), status_by="+UserID+" where serial_no="+InstrumentSerialNo[i]);
							
						}
					}
					
					s.executeUpdate("INSERT INTO `gl_transactions`(`document_id`,`document_type_id`,`document_date`,`remarks`,`created_on`,`created_on_date`,`created_by`, `cash_payment_id`)VALUES("+GLCashPaymentID+", 55, now(), '"+Narration+"', now(), curdate(), "+UserID+", "+GLCashPaymentID+")");
					
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
							
							s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`debit`,`remarks`)VALUES("+GLTransactionID+", "+AccountID+", "+InstrumentAmount[i]+", 'Cash Payment#"+GLCashPaymentID+" ')");
							
							s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`credit`,`remarks`)VALUES("+GLTransactionID+", "+WarehouseAccountID+", "+InstrumentAmount[i]+", 'Cash Payment#"+GLCashPaymentID+" ')");
							
						}
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
