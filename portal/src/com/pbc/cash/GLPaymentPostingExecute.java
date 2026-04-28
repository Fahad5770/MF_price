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


@WebServlet(description = "GL Payment Posting Execute", urlPatterns = { "/cash/GLPaymentPostingExecute" })
public class GLPaymentPostingExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GLPaymentPostingExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();
		try {
		ds.createConnection();
		ds.startTransaction();
		
		Statement s = ds.createStatement();
		
		
		
		long SerialNumber = Utilities.parseLong(request.getParameter("SerialNo"));
		int PostingTypeDate = Utilities.parseInt(request.getParameter("PostingType"));
		long InstrumentID = Utilities.parseLong(request.getParameter("InstrumentID1"));
		long UVID = Utilities.parseLong(request.getParameter("UVID"));
		String AccountName = Utilities.filterString(request.getParameter("AccountName1"), 1, 500);
		long WarehouseID = Utilities.parseLong(request.getParameter("WarehouseID1"));
		long AccountNumber = Utilities.parseLong(request.getParameter("AccountNumber1"));
		double Amount = Utilities.parseDouble(request.getParameter("Amount1"));
		
		
		ResultSet rs2 = s.executeQuery("select uvid from gl_cheque_bounce where uvid="+UVID);
		if(rs2.first()){
			obj.put("success", "false");
		}else{
			int PostingType=0;
			
			//if posting type = 1 then clear , if posting id = 2 then bounced
			if(PostingTypeDate==1){ //clear
				PostingType = 3;
			}else if(PostingTypeDate==2){//bounced
				PostingType = 4;
			}
			
			String Query = "update gl_cash_payments_instruments set status_id="+PostingType+", status_on=now(),status_by="+UserID+" where serial_no="+SerialNumber;
			
			
			//System.out.println(Query);
			
			s.executeUpdate(Query);
			
			long ReceiptSerialNo = 0;
			int InstrumentAccountID = 0;
			ResultSet rs = s.executeQuery("select receipt_serial_no,instrument_account_id from gl_cash_payments_instruments where serial_no="+SerialNumber);
			if(rs.first()){
				ReceiptSerialNo = rs.getLong("receipt_serial_no");
				InstrumentAccountID = rs.getInt("instrument_account_id");
			}
			
			//updating gl_cash_receipts_instruments
			
			if(ReceiptSerialNo != 0){
				String Query1 = "update gl_cash_receipts_instruments set status_id="+PostingType+", status_on=now(),status_by="+UserID+" where serial_no="+ReceiptSerialNo;
				s.executeUpdate(Query1);
			}
			
			
			
			//System.out.println(InstrumentID);
			String CustomerName="";
			long CustomerID=0;
			
			//other process
			if((InstrumentID==2 || InstrumentID==3 || InstrumentID==4 || InstrumentID==5 || InstrumentID==6 || InstrumentID==7) && PostingTypeDate==2){ //for cheque or BankDraft and bounced
				//gettting customer ID
				
				
				
				
				ResultSet rs1 = s.executeQuery("select glcri.serial_no,glcr.customer_id,(select name from common_distributors cd where cd.distributor_id=glcr.customer_id) customer_name from gl_cash_receipts_instruments glcri join gl_cash_receipts glcr on glcri.id=glcr.id where glcri.serial_no="+ReceiptSerialNo);
				if(rs1.first()){
					CustomerName = rs1.getString("customer_name");
					CustomerID = rs1.getLong("customer_id");
				}
				
				//System.out.println("insert into gl_cheque_bounce (customer_id,narration,created_on,created_on_date,created_by,uvid,warehouse_id,bank_account_id,instrument_id,instrument_account_id) values("+CustomerID+",'"+AccountName+" of "+CustomerID+" - "+CustomerName+" Bounced',now(),curdate(),"+UserID+","+UVID+","+WarehouseID+","+AccountNumber+","+InstrumentID+","+InstrumentAccountID+")");
				s.executeUpdate("insert into gl_cheque_bounce (customer_id,narration,created_on,created_on_date,created_by,uvid,warehouse_id,bank_account_id,instrument_id,instrument_account_id,receipt_serial_no) values("+CustomerID+",'"+AccountName+" of "+CustomerID+" - "+CustomerName+" Bounced',now(),curdate(),"+UserID+","+UVID+","+WarehouseID+","+AccountNumber+","+InstrumentID+","+InstrumentAccountID+","+ReceiptSerialNo+")");
			
				long lastid = 0;
				long lastid1=0;
				rs = s.executeQuery("select last_insert_id() from gl_cheque_bounce");
				if(rs.first()){
					 lastid = rs.getLong("last_insert_id()");	
				}
				
				//Debit and Credit Entries
				
				
				//System.out.println("INSERT INTO `gl_transactions`(`document_id`,`document_type_id`,`document_date`,`remarks`,`created_on`,`created_on_date`,`created_by`, `cheque_bounce_id`)VALUES("+lastid+", 58, now(), '"+AccountName+" of "+CustomerID+" - "+CustomerName+" bounced', now(), curdate(), "+UserID+", "+lastid+")");
				s.executeUpdate("INSERT INTO `gl_transactions`(`document_id`,`document_type_id`,`document_date`,`remarks`,`created_on`,`created_on_date`,`created_by`, `cheque_bounce_id`)VALUES("+lastid+", 58, now(), '"+AccountName+" of "+CustomerID+" - "+CustomerName+" bounced', now(), curdate(), "+UserID+", "+lastid+")");
				rs = s.executeQuery("select last_insert_id() from gl_transactions");
				if(rs.first()){
					 lastid1 = rs.getLong("last_insert_id()"); 
				}
				
				
				//System.out.println("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`debit`,`remarks`)VALUES("+lastid1+", "+InstrumentAccountID+", "+Amount+", '"+AccountName+" of "+CustomerID+" - "+CustomerName+" bounced')");
				s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`debit`,`remarks`)VALUES("+lastid1+", "+InstrumentAccountID+", "+Amount+", '"+AccountName+" of "+CustomerID+" - "+CustomerName+" bounced')");
				 
				//System.out.println("INSERT INTO gl_transactions_accounts(id,account_id,credit,remarks)VALUES("+lastid1+", "+InstrumentAccountID+", "+Amount+", '"+AccountName+" of "+CustomerID+" - "+CustomerName+" bounced')");
				s.executeUpdate("INSERT INTO gl_transactions_accounts(id,account_id,credit,remarks)VALUES("+lastid1+", "+AccountNumber+", "+Amount+", '"+AccountName+" of "+CustomerID+" - "+CustomerName+" bounced')");
				
			}
			
			
			ds.commit();		
			
			s.close();
			
			obj.put("success", "true");
		}
		
		
		
		
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
		
		
			
		
		
		
		
		out.print(obj);
		out.close();
		
	}
	
}
