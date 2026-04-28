package com.pbc.cash;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedHashMap;

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


@WebServlet(description = "GL Order Posting Validate", urlPatterns = { "/cash/GLOrderPostingValidate" })
public class GLOrderPostingValidate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GLOrderPostingValidate() {
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
		long OrderNo = Utilities.parseLong(request.getParameter("OrderNo"));
		Date OrderDate = Utilities.parseDate(request.getParameter("OrderDate"));
		Date EntryDate = Utilities.parseDate(request.getParameter("EntryDate"));
		long InvoiceNo = Utilities.parseLong(request.getParameter("InvoiceNo"));
		Date InvoiceDate = Utilities.parseDate(request.getParameter("InvoiceDate"));
		double InvoiceAmount = Utilities.parseDouble(request.getParameter("InvoiceAmountHidden"));
		long CustomerID = Utilities.parseLong(request.getParameter("CustomerID"));
		double CurrentBalance = Utilities.parseDouble(request.getParameter("CurrentBalance"));
		double CreditLimit = Utilities.parseDouble(request.getParameter("CreditLimit"));
		
		long POSNR[] = Utilities.parseLong(request.getParameterValues("POSNR"));
		String MATNR[] = Utilities.filterString(request.getParameterValues("MATNR"), 1, 100);
		String ARKTX[] = Utilities.filterString(request.getParameterValues("ARKTX"), 1, 100);
		String VRKME[] = Utilities.filterString(request.getParameterValues("VRKME"), 1, 100);
		long KWMENG[] = Utilities.parseLong(request.getParameterValues("KWMENG"));
		long ApprovalID[] = Utilities.parseLong(request.getParameterValues("ApprovalID"));
		
		long TotalUnits[] = Utilities.parseLong(request.getParameterValues("TotalUnits"));
		
		
		JSONObject obj = new JSONObject();
		
		// do validation
		boolean isValidationClear = true;
		if( OrderNo == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please select Customer");
			isValidationClear = false;
		}else{
			
			
			/*
			for(int i = 0; i < ApprovalID.length; i++ ){
				for(int j = 0; j < ApprovalID.length; j++){
					if(i!= j && ApprovalID[i] == ApprovalID[j]){
						obj.put("success", "false");
						obj.put("error", "Approval ID should not repeat.");
						isValidationClear = false;
						break;
					}
				}
			}
			*/
		}
		// end validation
		
		if( isValidationClear ){
		
			if (POSNR != null){
			Datasource ds = new Datasource();
			Datasource ds2 = new Datasource();
			
			try {
				
				ds.createConnection();
				
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				Statement s3 = ds.createStatement();
				Statement s4 = ds.createStatement();
				Statement s5 = ds.createStatement();
				Statement s7 = ds.createStatement();
				Statement s8 = ds.createStatement();
				Statement s9 = ds.createStatement();
				
				ds2.createConnectionToSAPDB();
				Statement s6 = ds2.createStatement();
				
				boolean isApprovalIDValid = true;
				boolean isCustomerIDValid = true;
				boolean isPromotionPackageIDValid = true;
				boolean isPromotionBrandIDValid = true;
				boolean isPackageIDValid = true;
				boolean isBrandIDValid = true;
				boolean isPromotionQuantityValid = true;
				long CurrentApprovalID = 0;
				long DocumentID = 0;
				
				for(int i = 0; i < POSNR.length; i++){
					
					
					CurrentApprovalID = ApprovalID[i];
					if(CurrentApprovalID > 0){
						
						//ResultSet rs = s.executeQuery("SELECT id FROM inventory_sales_promotions_request where request_id="+ApprovalID[i]+" and cast("+Utilities.getSQLDate(EntryDate)+" as datetime) between cast(date_format(valid_from,'%Y-%m-%d 06:00:00') as datetime) and cast(date_format(from_days(to_days(valid_to)+1),'%Y-%m-%d 06:00:00') as datetime) and is_active=1");
						
						ResultSet rs = s.executeQuery("SELECT ispr.id, ispr.is_isolated FROM inventory_sales_promotions_request ispr join workflow_requests wr on ispr.request_id = wr.request_id where ispr.request_id="+ApprovalID[i]+" and wr.status_id=2 and ( ( cast("+Utilities.getSQLDate(EntryDate)+" as date) between ispr.valid_from and ispr.valid_to ) OR ( now() between cast(date_format(ispr.valid_from,'%Y-%m-%d 06:00:00') as datetime) and cast(date_format(from_days(to_days(ispr.valid_to)+1),'%Y-%m-%d 06:00:00') as datetime) ) ) and ispr.is_active=1");
						 
						//ResultSet rs = s.executeQuery("SELECT ispr.id FROM inventory_sales_promotions_request ispr where ispr.request_id="+ApprovalID[i]+" and ( ( cast("+Utilities.getSQLDate(EntryDate)+" as datetime) between cast(date_format(ispr.valid_from,'%Y-%m-%d 06:00:00') as datetime) and cast(date_format(from_days(to_days(ispr.valid_to)+1),'%Y-%m-%d 06:00:00') as datetime) ) OR ( now() between cast(date_format(ispr.valid_from,'%Y-%m-%d 06:00:00') as datetime) and cast(date_format(from_days(to_days(ispr.valid_to)+1),'%Y-%m-%d 06:00:00') as datetime) ) ) and ispr.is_active=1");
						if(rs.first()){
							
							int isIsolated = rs.getInt(2);
							
							isApprovalIDValid = true;
							DocumentID = rs.getLong(1);
							
							ResultSet rs4 = s4.executeQuery("SELECT * FROM inventory_sales_promotions_request_distributors where product_promotion_id="+DocumentID+" and distributor_id="+CustomerID);
							if(rs4.first()){
								isCustomerIDValid = true;
								
							}else{
								isCustomerIDValid = false;
								//break;
								ResultSet rs5 = s5.executeQuery("SELECT isprr.product_promotion_id FROM inventory_sales_promotions_request_regions isprr, common_distributors cd where isprr.region_id=cd.region_id and isprr.product_promotion_id="+DocumentID+" and cd.distributor_id="+CustomerID);
								if(rs5.first()){
									isCustomerIDValid = true;
								}else{
									isCustomerIDValid = false;
									break;
								}
							}
							
							if(isCustomerIDValid){
								
								ResultSet rs2 = s2.executeQuery("SELECT isprp.id FROM inventory_sales_promotions_request_products isprp, inventory_products_view ipv where isprp.package_id=ipv.package_id and isprp.id = "+DocumentID+" and isprp.type_id = 2 and ipv.sap_code="+MATNR[i]);
								if(rs2.first()){
									isPromotionPackageIDValid = true;
									
									ResultSet rs3 = s3.executeQuery("SELECT isprpb.id FROM inventory_sales_promotions_request_products_brands isprpb, inventory_products_view ipv where isprpb.package_id=ipv.package_id and isprpb.brand_id=ipv.brand_id and isprpb.id = "+DocumentID+" and isprpb.type_id = 2 and ipv.sap_code="+MATNR[i]);
									if(rs3.first()){
										isPromotionBrandIDValid = true;
										
									}else{
										isPromotionBrandIDValid = false;
										break;
									}
									
								}else{
									isPromotionPackageIDValid = false;
									break;
								}
								
								
								
								if (isIsolated == 0){ // If not UTC type promotion
									
									if(isPromotionPackageIDValid && isPromotionBrandIDValid){
										ResultSet rs6 = s6.executeQuery("select posnr, matnr, arktx, vrkme, KWMENG from "+ds2.getSAPDatabaseAlias()+".vbap where vbeln = '"+OrderNo+"' and pstyv != 'TANN'");
										boolean isCounterPackageIDValid = false;
										boolean isCounterBrandIDValid = false;
										while(rs6.next()){
											ResultSet rs7 = s7.executeQuery("SELECT isprp.id FROM inventory_sales_promotions_request_products isprp, inventory_products_view ipv where isprp.package_id=ipv.package_id and isprp.id = "+DocumentID+" and isprp.type_id = 1 and ipv.sap_code="+rs6.getString("matnr"));
											if(rs7.first()){
												isCounterPackageIDValid = true;
												
												ResultSet rs8 = s8.executeQuery("SELECT isprpb.id FROM inventory_sales_promotions_request_products_brands isprpb, inventory_products_view ipv where isprpb.package_id=ipv.package_id and isprpb.brand_id=ipv.brand_id and isprpb.id = "+DocumentID+" and isprpb.type_id = 1 and ipv.sap_code="+rs6.getString("matnr"));
												if(rs8.first()){
													isCounterBrandIDValid = true;
													//break;
												}
												if(isCounterPackageIDValid && isCounterBrandIDValid){
													break;
												}
												
											}
										}// end while
										
										if(isCounterPackageIDValid){
											isPackageIDValid = true;
										}else{
											isPackageIDValid = false;
											break;
										}
										
										if(isCounterBrandIDValid){
											isBrandIDValid = true;
										}else{
											isBrandIDValid = false;
											break;
										}
										
									}
								}else if (isIsolated == 1){ // UTC Type
									isPackageIDValid = true;
									isBrandIDValid = true;
								}
								
								if (isPackageIDValid && isBrandIDValid){
									ResultSet rs9 = s9.executeQuery("SELECT ispr.estimated_sales_volume, (SELECT raw_cases FROM inventory_sales_promotions_request_products where id = ispr.id and type_id = 1 limit 1) sales_cases, (SELECT units FROM inventory_sales_promotions_request_products where id = ispr.id and type_id = 2 limit 1) free_bottles, (SELECT sum(glopp.total_units) consumed_qty FROM gl_order_posting_promotions glopp where glopp.request_id = ispr.request_id ) consumed_qty FROM inventory_sales_promotions_request ispr where ispr.request_id="+ApprovalID[i]);
									if(rs9.first()){
										
										double Quota = (rs9.getDouble("free_bottles") / rs9.getDouble("sales_cases")) * rs9.getDouble("estimated_sales_volume");
										
										double Consumed = rs9.getDouble("consumed_qty");
										double RemainingQty = Quota - Consumed;
										
										double SumTotalUnits = 0;
										for(int c = 0; c < POSNR.length; c++){
											if(ApprovalID[c] == CurrentApprovalID){
												//System.out.println("ApprovalID="+ApprovalID[c]+", TotalUnits="+TotalUnits[c]);
												SumTotalUnits += TotalUnits[c];
											}
										}
										
										if(SumTotalUnits > RemainingQty){
											isPromotionQuantityValid = false;
											break;
										}else{
											isPromotionQuantityValid = true;
										}
										
									}
								}
									
							}
							
							
							
						}else{
							isApprovalIDValid = false;
							break;
						}
					
					}
					
				}
				
				if(isApprovalIDValid){
					if(isCustomerIDValid){
						if(isPromotionPackageIDValid){
							if(isPromotionBrandIDValid){
								if(isPackageIDValid){
									if(isBrandIDValid){
										if(isPromotionQuantityValid){
											obj.put("success", "true");
										}else{
											obj.put("success", "false");
											obj.put("error", "Quantity exceeds the quota against free product. Approval ID:"+CurrentApprovalID+"");
										}
									}else{
										obj.put("success", "false");
										obj.put("error", "Brand could not be found against free product. Approval ID:"+CurrentApprovalID+"");
									}
								}else{
									obj.put("success", "false");
									obj.put("error", "Package could not be found against free product. Approval ID:"+CurrentApprovalID+"");
								}
							}else{
								obj.put("success", "false");
								obj.put("error", "Brand of free product is not valid. Approval ID:"+CurrentApprovalID+"");
							}
						}else{
							obj.put("success", "false");
							obj.put("error", "Package of free product is not valid. Approval ID:"+CurrentApprovalID+"");
						}
					}else{
						obj.put("success", "false");
						obj.put("error", "Approval ID:"+CurrentApprovalID+" is not valid for this customer");
					}
				}else{
					obj.put("success", "false");
					obj.put("error", "Approval ID "+CurrentApprovalID+" is invalid or expired or declined");
				}
				
				s9.close();
				s8.close();
				s7.close();
				s6.close();
				s5.close();
				s4.close();
				s3.close();
				s2.close();
				s.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				obj.put("success", "false");
				obj.put("error", e.toString());
				e.printStackTrace();
				
			}finally{
				try {
					ds.dropConnection();
					ds2.dropConnection();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}else{
				obj.put("success", "true");
			}
		} // end if validation
		
		out.print(obj);
		out.close();
		
	}
	
}
