package com.pbc.cash;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

import javax.jws.soap.SOAPBinding.Use;
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


@WebServlet(description = "GL Customer Opening Balances Execute", urlPatterns = { "/cash/GLCustomerOpeningBalancesExecute" })
public class GLCustomerOpeningBalancesExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GLCustomerOpeningBalancesExecute() {
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
		
		long CustomerID[] = Utilities.parseLong(request.getParameterValues("CustomerID"));
		double Amount[] = Utilities.parseDouble(request.getParameterValues("Amount"));
		
		JSONObject obj = new JSONObject();
		
		// do validation
		boolean isValidationClear = true;
		
		boolean isEmpty = true;
		for( int i = 0; i < Amount.length; i++ ){
			if( Amount[i] > 0 ){
				isEmpty = false;
				break;
			}
		}
		
		if( isEmpty ){
			obj.put("success", "false");
			obj.put("error", "Atleast one entry is mandatory.");
			isValidationClear = false;
		}// end validation
		
		if( isValidationClear ){
		
			Datasource ds = new Datasource();
			
			try {
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();

				for( int i = 0; i < CustomerID.length; i++ ){
					//if( Amount[i] > 0 ){
						
						long GLCustomerOpeningBalancesID = 0;
						
						ResultSet rs = s.executeQuery("select id from gl_customer_opening_balances where customer_id="+CustomerID[i]);
						if(rs.first()){
							GLCustomerOpeningBalancesID = rs.getLong("id");
							s2.executeUpdate("UPDATE `gl_customer_opening_balances` SET `amount` = "+Amount[i]+",`created_on` = now(),`created_by` = "+UserID+" WHERE `id` = "+rs.getLong("id"));
							
							long GLTransactionID = 0;
							ResultSet rs3 = s2.executeQuery("select id from gl_transactions where customer_opening_balance_id="+GLCustomerOpeningBalancesID);
							if(rs3.first()){
								GLTransactionID = rs3.getLong("id");
							}
							
							s2.executeUpdate("delete from gl_transactions_accounts where id="+GLTransactionID);
							
							long OpeningBalanceAccountID = 0;
							ResultSet rs5 = s2.executeQuery("SELECT id FROM gl_accounts where category_id=16 ");
							if(rs5.first()){
								OpeningBalanceAccountID = rs5.getLong("id");
							}
							
							long CustomerAccountID = 0;
							ResultSet rs4 = s2.executeQuery("SELECT id FROM gl_accounts where customer_id = "+CustomerID[i]+" and type_id = 3");
							if(rs4.first()){
								CustomerAccountID = rs4.getLong("id");
								
								long DebitAccount = CustomerAccountID;
								long CreditAccount = OpeningBalanceAccountID;
								
								if(Amount[i] < 0){
									DebitAccount = OpeningBalanceAccountID;
									CreditAccount = CustomerAccountID;
									
									Amount[i] = Amount[i] * (-1);
									
								}
								
								s2.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`debit`,`remarks`)VALUES("+GLTransactionID+", "+DebitAccount+", "+Amount[i]+", 'Customer Opening Balances#"+GLCustomerOpeningBalancesID+" ')");
								s2.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`credit`,`remarks`)VALUES("+GLTransactionID+", "+CreditAccount+", "+Amount[i]+", 'Customer Opening Balances#"+GLCustomerOpeningBalancesID+" ')");
							}
							
						}else{
							s2.executeUpdate("INSERT INTO `gl_customer_opening_balances`(`customer_id`,`amount`,`created_on`,`created_by`)VALUES("+CustomerID[i]+","+Amount[i]+",now(),"+UserID+")");
							
							ResultSet rs2 = s2.executeQuery("select LAST_INSERT_ID()");
							if(rs2.first()){
								GLCustomerOpeningBalancesID = rs2.getLong(1);
							}
							
							s2.executeUpdate("INSERT INTO `gl_transactions`(`document_id`,`document_type_id`,`document_date`,`remarks`,`created_on`,`created_on_date`,`created_by`, `customer_opening_balance_id`)VALUES("+GLCustomerOpeningBalancesID+", 61, now(), 'Opening Balance', now(), curdate(), "+UserID+", "+GLCustomerOpeningBalancesID+")");
							
							long GLTransactionID = 0;
							ResultSet rs3 = s2.executeQuery("select LAST_INSERT_ID()");
							if(rs3.first()){
								GLTransactionID = rs3.getLong(1);
							}
							
							long OpeningBalanceAccountID = 0;
							ResultSet rs5 = s2.executeQuery("SELECT id FROM gl_accounts where category_id=16 ");
							if(rs5.first()){
								OpeningBalanceAccountID = rs5.getLong("id");
							}
							
							long CustomerAccountID = 0;
							ResultSet rs4 = s2.executeQuery("SELECT id FROM gl_accounts where customer_id = "+CustomerID[i]+" and type_id = 3");
							if(rs4.first()){
								CustomerAccountID = rs4.getLong("id");
								
								long DebitAccount = CustomerAccountID;
								long CreditAccount = OpeningBalanceAccountID;
								
								if(Amount[i] < 0){
									DebitAccount = OpeningBalanceAccountID;
									CreditAccount = CustomerAccountID;
									
									Amount[i] = Amount[i] * (-1);
									
								}
								
								s2.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`debit`,`remarks`)VALUES("+GLTransactionID+", "+DebitAccount+", "+Amount[i]+", 'Customer Opening Balances#"+GLCustomerOpeningBalancesID+" ')");
								s2.executeUpdate("INSERT INTO `gl_transactions_accounts`(`id`,`account_id`,`credit`,`remarks`)VALUES("+GLTransactionID+", "+CreditAccount+", "+Amount[i]+", 'Customer Opening Balances#"+GLCustomerOpeningBalancesID+" ')");
							}
							
						}
						
						//s.executeQuery("select id from gl_transactions where customer_opening_balance_id="+GLCustomerOpeningBalancesID);
						
						
						
						
						
						
					//}// end IF
				}
				
				obj.put("success", "true");
				
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
