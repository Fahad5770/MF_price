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


@WebServlet(description = "GL Unpost Cash Receipts Execute", urlPatterns = { "/cash/GLUnpostCashReceiptsExecute" })
public class GLUnpostCashReceiptsExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GLUnpostCashReceiptsExecute() {
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
		
		long ReceiptID = Utilities.parseLong(request.getParameter("ReceiptID"));
		String Reason = Utilities.filterString(request.getParameter("Reason"), 1, 100);
		
		JSONObject obj = new JSONObject();
		
		// do validation
		boolean isValidationClear = true;
		if( ReceiptID == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please enter Receipt ID");
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

				ResultSet rs = s.executeQuery("SELECT * FROM gl_cash_receipts where id="+ReceiptID);
				if(rs.first()){
					
					s2.executeUpdate("INSERT INTO `gl_cash_receipts_unposted`(`id`,`customer_id`,`receipt_type`,`narration`,`created_on`,`created_on_date`,`created_by`,`uvid`,`warehouse_id`,`unposted_on`,`unposted_by`, `reason`)VALUES("+rs.getString("id")+","+rs.getString("customer_id")+","+rs.getString("receipt_type")+",'"+rs.getString("narration")+"','"+rs.getString("created_on")+"','"+rs.getString("created_on_date")+"',"+rs.getString("created_by")+","+rs.getString("uvid")+","+rs.getString("warehouse_id")+",now(),"+UserID+", '"+Reason+"')");
					
					ResultSet rs2 = s2.executeQuery("SELECT * FROM gl_cash_receipts_instruments where id="+ReceiptID);
					while(rs2.next()){
						String InstrumentDate = "'"+rs2.getString("instrument_date")+"'";
						if(rs2.getString("instrument_date") == null){
							InstrumentDate = null;
						}
						
						String StatusOnDate = "'"+rs2.getString("status_on")+"'";
						if(rs2.getString("status_on") == null){
							StatusOnDate = null;
						}
						
						s3.executeUpdate("INSERT INTO `gl_cash_receipts_unposted_instruments`(`id`,`serial_no`,`instrument_id`,`instrument_account_id`,`instrument_no`,`instrument_date`,`amount`,`status_id`,`status_on`,`status_by`)VALUES("+rs2.getString("id")+","+rs2.getString("serial_no")+","+rs2.getString("instrument_id")+","+rs2.getString("instrument_account_id")+",'"+rs2.getString("instrument_no")+"',"+InstrumentDate+","+rs2.getString("amount")+","+rs2.getString("status_id")+","+StatusOnDate+","+rs2.getString("status_by")+")");
						
						long GLTransactionID = 0;
						ResultSet rs3 = s3.executeQuery("SELECT id FROM gl_transactions where cash_receipt_id="+ReceiptID);
						if(rs3.first()){
							GLTransactionID = rs3.getLong("id");
						}
						
						s3.executeUpdate("delete from gl_transactions_accounts where id="+GLTransactionID);
						s3.executeUpdate("delete from gl_transactions where id="+GLTransactionID);
						
						s3.executeUpdate("delete from gl_cash_receipts_instruments where id="+ReceiptID);
						s3.executeUpdate("delete from gl_cash_receipts where id="+ReceiptID);
						
					}
				}
				
				
				
				ds.commit();
				
				obj.put("success", "true");
				
				
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
