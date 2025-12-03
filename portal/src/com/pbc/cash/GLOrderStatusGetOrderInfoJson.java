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

@WebServlet(description = "GL Order Status Get Order Info Json", urlPatterns = { "/sap/GLOrderStatusGetOrderInfoJson" })

public class GLOrderStatusGetOrderInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GLOrderStatusGetOrderInfoJson() {
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
		
		boolean isInvoiceGenerated = false;
		boolean isSAPPosted = false;
		boolean isSAPLifted = false;
		
		boolean isPortalPosted = false;
		boolean isPortalLifted = false;
		
		Date PortalPostedOn = null;
		Date PortalLiftedOn = null;
		
		try{
		
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			
			long SaleOrderNo = Utilities.parseLong(request.getParameter("SaleOrderNo"));
			
			
			ResultSet rs_check = s.executeQuery("select idn.delivery_id, idn.sap_order_no, created_on from inventory_delivery_note idn where idn.sap_order_no="+SaleOrderNo);
			if(rs_check.first()){
				isPortalLifted = true;
				PortalLiftedOn = rs_check.getDate("created_on");
			}
			
			ResultSet rs2 = s.executeQuery("SELECT * FROM gl_invoice_posting where order_no = "+SaleOrderNo);
			if( rs2.first() ){
				isPortalPosted = true;
				PortalPostedOn = rs2.getDate("created_on");
			}
			
			
			if(true){
				
				try {
					SAPUtilities obj = new SAPUtilities();
					//obj.connectPRD();
					
					String SaleOrderStr = SaleOrderNo+"";
					if(SaleOrderStr.length() < 10){
						
						int MissingDigits = 10 - SaleOrderStr.length();
						for(int i = 0; i < MissingDigits; i++){
							SaleOrderStr = "0"+SaleOrderStr;
						}
						
					}
					
					

					JCoTable tab[] = obj.getSalesOrder(SaleOrderStr);
					
					
					if (tab[0].getNumRows() > 0){
					
						tab[0].firstRow();
						
						String DistributorName = "";
						ResultSet rsDistributor = s.executeQuery("SELECT name FROM common_distributors where distributor_id="+Utilities.parseLong(tab[0].getString("KNKLI"))+" and is_delivery_blocked = 0 and is_central_blocked  = 0");
						if(rsDistributor.first()){
							DistributorName = Utilities.parseLong(tab[0].getString("KNKLI"))+"-"+rsDistributor.getString("name");
						}
						
						long InvoiceVal = 0;
						double InvoiceAmount = 0;
						boolean InvoicePaid = false;
						
						Date OrderDate = tab[0].getDate("AUDAT");
						Date InvoiceDate = null;
						
						
						
						if (DistributorName.length() > 0){
							JsonSuccess = true;
						}else{
							// 3: Invalid Customer ID
							JsonSuccess = false;
							JsonErrorMessage = "Invalid Customer ID";
						}
						
						JsonObj.put("DistrubutorID", tab[0].getString("KNKLI"));
						JsonObj.put("DistrubutorName", DistributorName);
						
						long CustomerAccountID = 0;
						ResultSet rs3 = s3.executeQuery("SELECT id FROM gl_accounts where customer_id="+tab[0].getString("KNKLI")+" and type_id=1");
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
						
						JsonObj.put("CurrentBalance", CurrentBalance);
						
						
						int rows = tab[2].getNumRows();
						if(rows > 0 ){
							
							tab[2].firstRow();
							for (int i = 0; i < rows; i++) {
								String VBTYP_N = tab[2].getString("VBTYP_N");
								
								if(VBTYP_N.equals("M")){
									JCoTable InvoiceHeader = obj.getSalesInvoice(tab[2].getString("VBELN"));
									
									if (InvoiceHeader.getNumRows() > 0){
										isInvoiceGenerated = true;
										//System.out.println(InvoiceHeader.getString("FKSTO"));
										if (!InvoiceHeader.getString("FKSTO").equals("X")){ // FKSTO is X when invoice is cancelled
											InvoiceVal = tab[2].getLong("VBELN");
											
											InvoiceAmount = InvoiceHeader.getDouble("NETWR") + InvoiceHeader.getDouble("MWSBK");
											
											InvoiceDate = InvoiceHeader.getDate("FKDAT");
											
											//System.out.println(InvoiceAmount);
											
											if (InvoiceHeader.getString("ZZCDS_STATUS").equals("X")){
												InvoicePaid = true;
												
												isSAPPosted = true;
												
												//JsonSuccess = false;
												//JsonErrorMessage = "Invoice already posted (SAP)";
											}
											
											break;
										}
										
									}
								}
								
								if(VBTYP_N.equals("J")){
									
									isSAPLifted = true;
									
									//JsonSuccess = false;
									//JsonErrorMessage = "Order already delivered (SAP)";
								}
								
								tab[2].setRow(i+1);
								
							}
							
						}else{
							// 4: Invoice does not exist against this order
							JsonSuccess = false;
							JsonErrorMessage = "Invoice does not exist against this order";
						}
						
						boolean isInvoiceCancelledManually = false;
						ResultSet rsCheckInvoice = s.executeQuery("SELECT vbeln FROM sap_vbrk_cancelled_invoices where vbeln = "+InvoiceVal);
						if(rsCheckInvoice.first()){
							isInvoiceCancelledManually = true;
						}
						
						
						if (isInvoiceCancelledManually == true){
							InvoiceVal = 0;
						}
						
						if (InvoiceVal == 0){
							// 5: Invoice does not exist against this order
							JsonSuccess = false;
							JsonErrorMessage = "Invoice does not exist against this order";
							
						}
						
						JsonObj.put("InvoiceNo", InvoiceVal);
						
						
						
						//obj.dropConnection();
						
						JsonObj.put("InvoiceAmount", InvoiceAmount);
						
						JsonObj.put("InvoiceDate", Utilities.getDisplayDateFormat(InvoiceDate));
						JsonObj.put("OrderDate", Utilities.getDisplayDateFormat(OrderDate));
						//System.out.print(InvoiceAmount);
					
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
			
			s3.close();
			s2.close();
			s.close();
			ds.dropConnection();
		
		}catch(Exception e){
			JsonSuccess = false;
			JsonErrorMessage = e.toString();
			System.out.println(e);
		}
		
		
		JsonObj.put("InvoiceGenerated", isInvoiceGenerated);
		JsonObj.put("SAPPosted", isSAPPosted);
		JsonObj.put("SAPLifted", isSAPLifted);
		
		JsonObj.put("PortalPosted", isPortalPosted);
		JsonObj.put("PortalLifted", isPortalLifted);
		
		JsonObj.put("PortalPostedOn", Utilities.getDisplayDateFormat(PortalPostedOn)); 
		JsonObj.put("PortalLiftedOn", Utilities.getDisplayDateFormat(PortalLiftedOn));
		
		JsonObj.put("success", JsonSuccess);
		JsonObj.put("error", JsonErrorMessage);
		
		out.print(JsonObj);
		out.close();
		
	}

}
