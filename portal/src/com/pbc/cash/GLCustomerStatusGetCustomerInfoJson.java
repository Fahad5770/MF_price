package com.pbc.cash;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.Util;
import com.pbc.sap.SAPUtilities;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

@WebServlet(description = "GL Customer Status Get Customer Info Json", urlPatterns = { "/sap/GLCustomerStatusGetCustomerInfoJson" })

public class GLCustomerStatusGetCustomerInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GLCustomerStatusGetCustomerInfoJson() {
        super();
        // TODO Auto-generated constructor stub
        //System.out.println("contructor() ...");
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//System.out.println("service() ...");
		
		
		HttpSession session = request.getSession();
		
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		response.setContentType("application/json");
		JSONObject JsonObj = new JSONObject();
		PrintWriter out = response.getWriter();
		
		boolean JsonSuccess = false;
		String JsonErrorMessage = "";
		
		try{
		
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			
			long CustomerID = Utilities.parseLong(request.getParameter("CustomerID"));
			long CustomerAccountID = 0;
			String CustomerName = "";
			double CurrentBalance = 0;
			double Ledger=0;
			double Avaliablebalance=0;
			System.out.println(Avaliablebalance);
			
			
			ResultSet rs2 = s.executeQuery("SELECT name FROM common_distributors where distributor_id="+CustomerID);
			if( rs2.first() ){
				CustomerName = rs2.getString("name");
				
				ResultSet rs3 = s3.executeQuery("SELECT id FROM gl_accounts where customer_id="+CustomerID+" and type_id=1");
				if( rs3.first() ){
					CustomerAccountID = rs3.getLong("id");
					
					double TotalDebit = 0;
					double TotalCredit = 0;
					
					ResultSet rs4 = s2.executeQuery("SELECT sum(debit) total_debit, sum(credit) total_credit FROM gl_transactions_accounts where account_id="+CustomerAccountID);
					if( rs4.first() ){
						TotalDebit = rs4.getDouble("total_debit");
						TotalCredit = rs4.getDouble("total_credit");
						CurrentBalance = TotalDebit - TotalCredit;
						System.out.println("CurrentBalance"+CurrentBalance);
						//CurrentBalance = CurrentBalance * (-1);
					}
					
					
					
				}
				
				JsonObj.put("CustomerName", CustomerName);
				Ledger=CurrentBalance;
				JsonObj.put("LedgerBalanceAmount", CurrentBalance);
				JsonObj.put("LedgerBalance", Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(CurrentBalance));
				
				
				ResultSet rs4 = s3.executeQuery("SELECT id FROM gl_accounts where customer_id="+CustomerID+" and type_id=2");
				if( rs4.first() ){
					CustomerAccountID = rs4.getLong("id");
					
					double TotalDebit = 0;
					double TotalCredit = 0;
					
					ResultSet rs5 = s2.executeQuery("SELECT sum(debit) total_debit, sum(credit) total_credit FROM gl_transactions_accounts where account_id="+CustomerAccountID);
					if( rs5.first() ){
						TotalDebit = rs5.getDouble("total_debit");
						TotalCredit = rs5.getDouble("total_credit");
						CurrentBalance = TotalDebit - TotalCredit;
						//CurrentBalance = CurrentBalance * (-1);
					}
					
				}
				
				JsonObj.put("SecurityBalanceAmount", CurrentBalance);
				JsonObj.put("SecurityBalance", Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(CurrentBalance));
				
				
				ResultSet rs6 = s3.executeQuery("SELECT id FROM gl_accounts where customer_id="+CustomerID+" and type_id=3");
				if( rs6.first() ){
					CustomerAccountID = rs6.getLong("id");
					
					double TotalDebit = 0;
					double TotalCredit = 0;
					
					ResultSet rs7 = s2.executeQuery("SELECT sum(debit) total_debit, sum(credit) total_credit FROM gl_transactions_accounts where account_id="+CustomerAccountID);
					if( rs7.first() ){
						TotalDebit = rs7.getDouble("total_debit");
						TotalCredit = rs7.getDouble("total_credit");
						CurrentBalance = TotalDebit - TotalCredit;
						//CurrentBalance = CurrentBalance * (-1);
					}
					
				}
				
				double CreditLimit = 0;
				ResultSet rs = s3.executeQuery("SELECT credit_limit FROM gl_customer_credit_limit where customer_id="+CustomerID+" and curdate() between valid_from and valid_to and is_active=1");
				if(rs.first()){
					CreditLimit = rs.getDouble(1);
				}
				//System.out.println("CreditLimit"+CreditLimit);
				JsonObj.put("CreditLimit", CreditLimit);
				
				JsonObj.put("CreditBalanceAmount", CurrentBalance);
				JsonObj.put("CreditBalance", Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(CurrentBalance));
				
				//if(Ledger<0 && CreditLimit>0){
					Avaliablebalance=Ledger-CreditLimit;
				//}else{
				//	Avaliablebalance=0;
				//}
				
				
				JsonObj.put("AvaliableBalance",Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(Avaliablebalance));
				
				JsonSuccess = true;
			
				
			}else{
				JsonSuccess = false;
				JsonErrorMessage = "Customer not found.";
			}
			
			s3.close();
			s2.close();
			s.close();
			ds.dropConnection();
		
		}catch(Exception e){
			System.out.println(e);
			JsonSuccess = false;
		}
		
		JsonObj.put("success", JsonSuccess);
		JsonObj.put("error", JsonErrorMessage);
		
		out.print(JsonObj);
		out.close();
		
	}

}
