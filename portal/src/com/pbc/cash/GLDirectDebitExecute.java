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


@WebServlet(description = "GL Direct Debit Execute", urlPatterns = { "/cash/GLDirectDebitExecute" })
public class GLDirectDebitExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GLDirectDebitExecute() {
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
		long BankAccountID = Utilities.parseLong(request.getParameter("BankAccountIDSubmitForm"));
		
		String Narration = Utilities.filterString(request.getParameter("NarrationSubmitForm"), 1, 1000);
		
		double InstrumentAmount[] = Utilities.parseDouble(request.getParameterValues("InstrumentAmount"));
		
		Date InstrumentDate[] = Utilities.parseDate(request.getParameterValues("InstrumentDate"));
		String InstrumentNo[] = Utilities.filterString(request.getParameterValues("InstrumentNo"), 1, 100);
		
		JSONObject obj = new JSONObject();
		
		// do validation
		boolean isValidationClear = true;
		if( DistributorID == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please select Customer");
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
				
				ResultSet rs2 = s2.executeQuery("select id from gl_direct_debit where uvid="+UniqueVoucherID);
				if( rs2.first() ){
					obj.put("success", "false");
					obj.put("error", "Already Exists"); 
				}else{
	
					s.executeUpdate("INSERT INTO `gl_direct_debit`(`uvid`,`customer_id`,`narration`,`created_on`,`created_on_date`,`created_by`, `warehouse_id`, `bank_account_id`)VALUES("+UniqueVoucherID+", "+DistributorID+", '"+Narration+"', now(), curdate(), "+UserID+", "+WarehouseID+", "+BankAccountID+")");
					
					long GLDirectDebitID = 0;
					ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
					if( rs.first() ){
						GLDirectDebitID = rs.getLong(1);
					}
					
					double TotalInstrumentAmount = 0;
					for( int i = 0; i < InstrumentAmount.length; i++ ){ 
						if( InstrumentAmount[i] > 0 ){
							TotalInstrumentAmount += InstrumentAmount[i];
							s.executeUpdate("INSERT INTO `gl_direct_debit_instruments`(`id`,`reference_no`,`transaction_date`,`amount`)VALUES("+GLDirectDebitID+", '"+InstrumentNo[i]+"', "+ Utilities.getSQLDate(InstrumentDate[i])+", "+InstrumentAmount[i]+")");
						}
					}
					
					s.executeUpdate("INSERT INTO `gl_transactions`(`document_id`,`document_type_id`,`document_date`,`remarks`,`created_on`,`created_on_date`,`created_by`, `direct_debit_id`)VALUES("+GLDirectDebitID+", 57, now(), '"+Narration+"', now(), curdate(), "+UserID+", "+GLDirectDebitID+")");
					
					long GLTransactionID = 0;
					ResultSet rs3 = s.executeQuery("select LAST_INSERT_ID()");
					if( rs3.first() ){
						GLTransactionID = rs3.getLong(1);
					}
					
					for( int i = 0; i < InstrumentAmount.length; i++ ){ 
						if( InstrumentAmount[i] > 0 ){
							
							ResultSet rs4 = s3.executeQuery("SELECT id FROM gl_accounts where customer_id="+DistributorID);
							while( rs4.next() ){
								s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`credit`,`remarks`)VALUES("+GLTransactionID+", "+rs4.getLong("id")+", "+InstrumentAmount[i]+", 'Direct Debit#"+GLDirectDebitID+" ')");
								s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`debit`,`remarks`)VALUES("+GLTransactionID+", "+BankAccountID+", "+InstrumentAmount[i]+", 'Direct Debit#"+GLDirectDebitID+" ')");
							}
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
