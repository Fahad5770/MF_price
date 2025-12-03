package com.pbc.cash;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.google.gson.JsonArray;
import com.mysql.jdbc.Util;
import com.pbc.sap.SAPUtilities;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

@WebServlet(description = "Get Order Info Json (Order Posting)", urlPatterns = { "/sap/GLOrderPostingGetOrderInfoJson" })

public class GLOrderPostingGetOrderInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GLOrderPostingGetOrderInfoJson() {
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
		
		Datasource ds = new Datasource();
		Datasource ds2 = new Datasource();
		
		try{
		
			
			//ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			Statement s6 = ds.createStatement();
			
			
			ds2.createConnectionToSAPDB();
			Statement s4 = ds2.createStatement();
			Statement s5 = ds2.createStatement();
			
			long SaleOrderNo = Utilities.parseLong(request.getParameter("SaleOrderNo"));
			boolean isEditCase = Utilities.parseBoolean(request.getParameter("isEditCase"));
			boolean isDuplicate = false;
			boolean isPartial = false;
			boolean isLifted = false;
			
			/*
			ResultSet rs_check = s.executeQuery("select idn.delivery_id, idn.sap_order_no, ( select sap_order_no from inventory_delivery_note_partial_orders where sap_order_no=idn.sap_order_no) partial from inventory_delivery_note idn where idn.sap_order_no="+SaleOrderNo);
			if(rs_check.first()){
				isDuplicate = true;
				isLifted = true;
				if(rs_check.getLong("partial") != 0){
					isDuplicate = false;
					isPartial = true;
				}
			}
			*/
			
			ResultSet rs2 = s.executeQuery("SELECT * FROM gl_order_posting where order_no = "+SaleOrderNo);
			if( rs2.first() ){
				isDuplicate = true;
				isLifted = false;
			}
			
			if (isEditCase){
				isDuplicate = false;
			}
			
			if(isDuplicate == true){
				// 1: already exists
				JsonSuccess = false;
				JsonErrorMessage = "Already Posted";
			}else{
 
				try {
					
					
					
					String SaleOrderStr = SaleOrderNo+"";
					if(SaleOrderStr.length() < 10){
						
						int MissingDigits = 10 - SaleOrderStr.length();
						for(int i = 0; i < MissingDigits; i++){
							SaleOrderStr = "0"+SaleOrderStr;
						}
						
					}
					
					ResultSet rs5 = s4.executeQuery("SELECT vbak.vbeln order_number, vbak.kunnr distributor_id, vbak.erdat entry_date, vbak.audat order_date, (vbak.netwr + (select sum(mwsbp) from "+ds2.getSAPDatabaseAlias()+".vbap where vbeln = vbak.vbeln)) /* Tax Amount from VBAP and Order Amount from VBAP-NETWR*/ order_amount, kna1.name1 distributor_name, vbuk.fksak FROM "+ds2.getSAPDatabaseAlias()+".vbak vbak join "+ds2.getSAPDatabaseAlias()+".vbuk vbuk on vbak.vbeln = vbuk.vbeln join "+ds2.getSAPDatabaseAlias()+".kna1 kna1 on vbak.kunnr = kna1.kunnr where vbak.vbeln = "+SaleOrderStr);
					
					if (rs5.next()){
						
						String DistributorName = "";
						ResultSet rsDistributor = s.executeQuery("SELECT name FROM common_distributors where distributor_id="+Utilities.parseLong(rs5.getString("distributor_id"))+" and is_delivery_blocked = 0 and is_central_blocked  = 0");
						if(rsDistributor.first()){
							DistributorName = Utilities.parseLong(rs5.getString("distributor_id"))+"-"+rsDistributor.getString("name");
						}
						
						double OrderAmount = Utilities.parseDouble(rs5.getString("order_amount"));
						
						Date OrderDate = Utilities.parseDateYYYYMMDDWithoutSeparator(rs5.getString("order_date"));
						Date EntryDate = Utilities.parseDateYYYYMMDDWithoutSeparator(rs5.getString("entry_date"));
						
						if (DistributorName.length() > 0){
							JsonSuccess = true;
						}else{
							// 3: Invalid Customer ID
							JsonSuccess = false;
							JsonErrorMessage = "Invalid Customer ID";
						}
						
						
						
						
						JsonObj.put("DistrubutorID", rs5.getString("distributor_id"));
						JsonObj.put("DistrubutorName", DistributorName);
						
						double CreditLimit = 0;
						ResultSet rs = s3.executeQuery("SELECT credit_limit FROM gl_customer_credit_limit where customer_id="+rs5.getString("distributor_id")+" and curdate() between valid_from and valid_to and is_active=1");
						if(rs.first()){
							CreditLimit = rs.getDouble(1);
						}
						
						JsonObj.put("CreditLimit", CreditLimit);
						
						JSONArray jr = new JSONArray();
						ResultSet rs6 = s5.executeQuery("select posnr, matnr, arktx, vrkme, KWMENG from "+ds.getSAPDatabaseAlias()+".vbap where vbeln = '"+SaleOrderNo+"' and pstyv = 'TANN'");
						while(rs6.next()){
							LinkedHashMap rows = new LinkedHashMap();
							rows.put("POSNR", rs6.getString("posnr"));
							rows.put("MATNR", rs6.getString("matnr"));
							rows.put("ARKTX", rs6.getString("arktx"));
							rows.put("VRKME", rs6.getString("vrkme"));
							rows.put("KWMENG", rs6.getString("KWMENG"));
							
							int ProductID = 0;
							int UnitPerSKU = 0;
							int RawCases = 0;
							int Units = 0;
							long TotalUnits = 0;
							
							ResultSet rs7 = s6.executeQuery("SELECT product_id, unit_per_sku FROM inventory_products_view where sap_code="+rs6.getString("matnr"));
							if(rs7.first()){
								ProductID = rs7.getInt("product_id");
								UnitPerSKU = rs7.getInt("unit_per_sku");
							}
							
							
							if(rs6.getString("vrkme").equals("KI") || rs6.getString("vrkme").equals("KAR")){
								RawCases = Utilities.parseInt(rs6.getString("KWMENG"));
								Units = 0;
							}else{
								RawCases = 0;
								Units = Utilities.parseInt(rs6.getString("KWMENG"));
							}
							
							TotalUnits = (RawCases * UnitPerSKU) + Units;
							
							rows.put("ProductID", ProductID);
							rows.put("RawCases", RawCases);
							rows.put("Units", Units);
							rows.put("TotalUnits", TotalUnits);
							
							jr.add(rows);
						}
						
						JsonObj.put("PromotionRows", jr);
						
						long CustomerAccountID = 0;
						ResultSet rs3 = s3.executeQuery("SELECT id FROM gl_accounts where customer_id="+rs5.getString("distributor_id")+" and type_id=1");
						if( rs3.first() ){
							CustomerAccountID = rs3.getLong("id");
						}
						
						double TotalDebit = 0;
						double TotalCredit = 0;
						double CurrentBalance = 0;
						ResultSet rs4 = s3.executeQuery("SELECT sum(debit) total_debit, sum(credit) total_credit FROM gl_transactions_accounts where account_id="+CustomerAccountID);
						if( rs4.first() ){
							TotalDebit = rs4.getDouble("total_debit");
							TotalCredit = rs4.getDouble("total_credit");
							CurrentBalance = TotalDebit - TotalCredit;
							CurrentBalance = CurrentBalance * (-1);
						}
						
						
						JsonObj.put("CurrentBalance", Utilities.getDisplayCurrencyFormatSimple(CurrentBalance));
						JsonObj.put("OrderAmount", OrderAmount);
						JsonObj.put("OrderDate", Utilities.getDisplayDateFormat(OrderDate));
						JsonObj.put("EntryDate", Utilities.getDisplayDateFormat(EntryDate));
						
					
					}else{
						//obj.dropConnection();
						// 2: invalid order number
						JsonSuccess = false;
						JsonErrorMessage = "Invalid order number";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					JsonSuccess = false;
					JsonErrorMessage = e.toString();
					
					System.out.println("exception = "+e);
				}
			}
			
			s6.close();
			s5.close();
			s4.close();
			s3.close();
			s2.close();
			s.close();
			
		
		}catch(Exception e){
			JsonSuccess = false;
			JsonErrorMessage = e.toString();
			System.out.println(e);
		}finally{
			try {
				ds.dropConnection();
				ds2.dropConnection();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		JsonObj.put("success", JsonSuccess);
		JsonObj.put("error", JsonErrorMessage);
		
		out.print(JsonObj);
		out.close();
		
	}

}
