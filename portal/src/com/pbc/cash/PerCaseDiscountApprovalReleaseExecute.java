package com.pbc.cash;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;



@WebServlet(description = "Price List ", urlPatterns = { "/cash/PerCaseDiscountApprovalReleaseExecute" })
public class PerCaseDiscountApprovalReleaseExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PerCaseDiscountApprovalReleaseExecute() {
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
		
		long ApprovalID=Utilities.parseLong(request.getParameter("ApprID"));
		
		//System.out.println(ApprovalID+" Approval ID ");
		
		PrintWriter out = response.getWriter();
		
		
		
		
		JSONObject obj = new JSONObject();
		Datasource ds = new Datasource();
			
		//long MasterTableCoolerInjectionID = 0;
		try {
			
			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			Statement s4 = ds.createStatement();
			Statement s5 = ds.createStatement();
			Statement s6 = ds.createStatement();
			

			
				//System.o
			
			long DistributorID=0;
			long RequestID=0;
			//long PacakgeID=0;
			//long MainTableID=0;
			//long LrbTypeID=0;
			//Date LiftingStartDate=new Date();
			//Date LiftingEndDate=new Date();
			//int AmountTotal=0;
			
			 
				         s.executeUpdate("INSERT INTO gl_transactions(document_id,document_type_id,document_date,remarks,created_on,created_on_date,created_by, percase_approval_id)VALUES("+ApprovalID+", 65, now(), 'Percase document release', now(), curdate(), "+UserID+", "+ApprovalID+")");
					
				         //System.out.println("INSERT INTO gl_transactions(document_id,document_type_id,document_date,remarks,created_on,created_on_date,created_by, cash_receipt_id)VALUES("+ApprovalID+", 65, now(), 'Percase document release', now(), curdate(), "+UserID+", "+ApprovalID+")");
					long GLTransactionID = 2;
					ResultSet rs1 = s.executeQuery("select LAST_INSERT_ID()");
					if( rs1.first() ){
						GLTransactionID = rs1.getLong(1);
						
					}
					
					ResultSet rs2 = s2.executeQuery("SELECT distributor_id,request_id FROM inventory_primary_percase_approval where id="+ApprovalID); 
					//System.out.println();
					while( rs2.next() ){
						DistributorID = rs2.getLong(1);
						RequestID = rs2.getLong(2);
					}
					
					//System.out.println("SELECT id FROM gl_accounts where customer_id="+DistributorID+" and type_id=7");
					long CustomerAccountID = 0;
					ResultSet rs4 = s3.executeQuery("SELECT id FROM gl_accounts where customer_id="+DistributorID+" and type_id=7"); 
					while( rs4.next() ){
						CustomerAccountID = rs4.getLong(1);
					}
					//System.out.println("SELECT sum(amount) total_amount FROM inventory_primary_percase_approval_products where id="+ApprovalID);
					
					long TotalAmount = 0;
					ResultSet rs5 = s4.executeQuery("SELECT sum(amount) total_amount FROM inventory_primary_percase_approval_products where id="+ApprovalID); 
					while( rs5.next() ){
						TotalAmount = rs5.getLong(1);
					}
					
					long IncentiveAccountID=0;
					
					ResultSet rs51 = s3.executeQuery("SELECT id FROM gl_accounts where category_id=19");
					if( rs51.first() ){
						IncentiveAccountID = rs51.getLong("id");
					}
					
					
					
					s.executeUpdate("INSERT INTO gl_transactions_accounts(id,account_id,credit,remarks) VALUES("+GLTransactionID+", "+CustomerAccountID+", "+TotalAmount+", 'Percase Discount Approval ID#"+RequestID+" ')");
					//System.out.println("INSERT INTO gl_transactions_accounts(id,account_id,credit,remarks)VALUES("+GLTransactionID+", "+CustomerAccountID+", "+TotalAmount+", 'Percase Discount Approval ID#"+ApprovalID+" ')");
					
					s.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`debit`,`remarks`) VALUES("+GLTransactionID+", "+IncentiveAccountID+", "+TotalAmount+", 'Percase Discount Approval ID#"+RequestID+" ')");
					
					
					
					
					//Updating inventory_primary_percase_approval
					
					s.executeUpdate("update inventory_primary_percase_approval set is_released=1 where id="+ApprovalID);
					
				
			//obj.put("product_promotion_id",MasterTableCoolerInjectionID);
				obj.put("success", "true");
				ds.commit();
				
		
				
				
				
			s.close();
			ds.dropConnection();
		} catch (Exception e) {
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			// TODO Auto-generated catch block
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		
		
		
		out.print(obj);
		out.close();
	
	}
	
}
