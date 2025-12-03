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


@WebServlet(description = "GL Unpost Order Execute", urlPatterns = { "/cash/GLUnpostOrderExecute" })
public class GLUnpostOrderExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GLUnpostOrderExecute() {
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
		
		long OrderNo = Utilities.parseLong(request.getParameter("OrderNo"));
		
		
		JSONObject obj = new JSONObject();
		
		// do validation
		boolean isValidationClear = true;
		if( OrderNo == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please enter Order no");
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
				
				boolean isInvoiced = false;
				
				ResultSet rs = s.executeQuery("SELECT * FROM gl_order_posting where order_no = "+OrderNo);
				if( rs.first() ){
					
					 if(rs.getInt("is_invoiced") == 1){
						 isInvoiced = true;
					 }else{
						 
						//s2.executeUpdate("INSERT INTO `gl_order_posting_unposted`(`id`,`order_no`,`order_date`,`invoice_no`,`invoice_date`,`invoice_amount`,`customer_id`,`customer_account_id`,`sales_account_id`,`current_balance`,`credit_limit`,`uvid`,`created_on`,`created_on_date`,`created_by`,`warehouse_id`,`unposted_on`,`unposted_by`)VALUES("+rs.getString("id")+","+rs.getString("order_no")+",'"+rs.getString("order_date")+"',"+rs.getString("invoice_no")+",'"+rs.getString("invoice_date")+"',"+rs.getString("invoice_amount")+","+rs.getString("customer_id")+","+rs.getString("customer_account_id")+","+rs.getString("sales_account_id")+","+rs.getString("current_balance")+","+rs.getString("credit_limit")+","+rs.getString("uvid")+",'"+rs.getString("created_on")+"','"+rs.getString("created_on_date")+"',"+rs.getString("created_by")+","+rs.getString("warehouse_id")+",now(),"+UserID+")");
						 
						 
						s2.executeUpdate("INSERT INTO `gl_order_posting_unposted`(`id`,`order_no`,`order_date`,`order_amount`,`customer_id`,`customer_ledger_account_id`,`customer_advance_account_id`,`current_balance`,`credit_limit`,`uvid`,`created_on`,`created_on_date`,`created_by`,`warehouse_id`,`is_invoiced`,`unposted_on`,`unposted_by`)VALUES("+rs.getString("id")+","+rs.getString("order_no")+",'"+rs.getString("order_date")+"',"+rs.getString("order_amount")+","+rs.getString("customer_id")+","+rs.getString("customer_ledger_account_id")+","+rs.getString("customer_advance_account_id")+","+rs.getString("current_balance")+","+rs.getString("credit_limit")+","+rs.getString("uvid")+",'"+rs.getString("created_on")+"','"+rs.getString("created_on_date")+"',"+rs.getString("created_by")+","+rs.getString("warehouse_id")+","+rs.getString("is_invoiced")+",now(),"+UserID+")");
						
						
						long GLOrderPostingID = rs.getLong("id");
						
						long GLTransactionID = 0;
						ResultSet rs6 = s2.executeQuery("SELECT id FROM gl_transactions where order_posting_id="+GLOrderPostingID);
						if( rs6.first() ){
							GLTransactionID = rs6.getLong(1);
						}
						
						s.executeUpdate("delete from gl_transactions_accounts where id="+GLTransactionID);
						s.executeUpdate("delete from gl_transactions where id="+GLTransactionID);
						s.executeUpdate("delete from gl_order_posting where id="+GLOrderPostingID);
						
					}
					
				}
				
				if(isInvoiced){
					obj.put("success", "false");
					obj.put("error", "Order is Invoiced.");
				}else{
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
