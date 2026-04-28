package com.pbc.primarysales;

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
import org.omg.CORBA.portable.ValueFactory;

import com.pbc.inventory.Product;
import com.pbc.inventory.StockPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;



@WebServlet(description = "Executes Desk Sale", urlPatterns = { "/primarysales/PrimaryPurchasesExecute" })
public class PrimaryPurchasesExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PrimaryPurchasesExecute() {
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
		
		int DeskSaleEditID = Utilities.parseInt(request.getParameter("DeskSaleEditID"));
		boolean isEditCase = false;
		
		if (DeskSaleEditID > 0){
			isEditCase = true;
		} 
		
		long OutletID = Utilities.parseLong(request.getParameter("DeskSaleOutledIDHidden"));
		long UniqueVoucherID = Utilities.parseLong(request.getParameter("UniqueVoucherID"));
		long DistributorID = Utilities.parseLong(request.getParameter("DeskSaleDistributorIDHidden"));
		long PlantID = Utilities.parseLong(request.getParameter("DeskSalePlantIDHidden"));
		long RegionID = Utilities.parseLong(request.getParameter("DeskSaleRegionIDHidden"));
		
		double SalesTaxRate = Utilities.parseDouble(request.getParameter("DeskSaleSalesTaxRateHidden"));
		double WHTaxRate = Utilities.parseDouble(request.getParameter("DeskSaleWHTaxHidden"));
		
		//double InvoiceAmount = Utilities.parseDouble(request.getParameter("DeskSaleMainFormTotalInvoiceAmount"));
		//double SalesTaxAmount = Utilities.parseDouble(request.getParameter("DeskSaleMainFormSalesTax"));
		
		
		int ProductID[] = Utilities.parseInt(request.getParameterValues("ProductID"));
		int RawCases[] = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormRawCases"));
		int Units[] = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormUnits"));
		int PSIspromo[] = Utilities.parseInt(request.getParameterValues("PSIspromo"));
		
		
		double Discount[] = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormDiscount"));
		
		
		
		
		
		
		
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		Date VoucherDate = new java.util.Date();
		
		try {
			
			ds.createConnection();
			
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			
			
			
			if(isEditCase){
				ResultSet rs_date_check = s.executeQuery("select created_on from primary_sales_orders where id="+DeskSaleEditID);
				if(rs_date_check.first()){
					VoucherDate = rs_date_check.getDate(1);
				}
			}			
			
			
			//if( DateUtils.isSameDay(new java.util.Date(), VoucherDate) ){	// false only in editcase on date conflict
			if( DateUtils.isSameDay(new java.util.Date(), VoucherDate) ){	// false only in editcase on date conflict
				String SQLMain = "";
				
				ResultSet rs1 = s1.executeQuery("select id from primary_sales_orders where uvid="+UniqueVoucherID);
				if( rs1.first() ){
					
					obj.put("success", "false");
					obj.put("error", "Already Exists");
					
				}else{
					ds.startTransaction();
					//s.getConnection().setAutoCommit(false);
					
					
			
						SQLMain = "insert into primary_purchases_orders (uvid, created_on, created_by,    invoice_amount,plant_id) values("+UniqueVoucherID+", now(), "+UserID+",0,"+PlantID+")";
					
					s.executeUpdate(SQLMain);
					
					int DeskSaleID = 0;
					
					if(!isEditCase){
						ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
						if(rs.first()){
							DeskSaleID = rs.getInt(1);
						}
					}else{
						DeskSaleID = DeskSaleEditID;
					}
					
					s.executeUpdate("delete from primary_purchases_orders_products where id="+DeskSaleID);
					
					double InvoiceAmount = 0;
					double InvoiceWHTaxAmount = 0;
					double InvoiceNetAmount = 0;				
						
						for(int i = 0; i < ProductID.length; i++){
							
							int UnitsPerSKU = 0;
							long LiquidInMLPerUnit = 0;
							
							ResultSet rs3 = s.executeQuery("SELECT unit_per_sku, liquid_in_ml FROM inventory_products_view where product_id = "+ProductID[i]);
							if (rs3.first()){
								UnitsPerSKU = rs3.getInt(1);
								LiquidInMLPerUnit = rs3.getLong(2);
							}
							
							
							long TotalUnits = ( RawCases[i] * UnitsPerSKU ) + Units[i];
							long LiquidInMLValue = TotalUnits * LiquidInMLPerUnit;
							
							double UnitRates[] = Product.getSellingPrice(ProductID[i], OutletID);
							double RateRawCase = UnitRates[0];
							double RateUnit = UnitRates[1];
							
							double RawCaseAmount = RawCases[i] * RateRawCase;
							double UnitAmount = Units[i] * RateUnit;
							
							
							// patch for hand discount
							double HandDiscountRate = UnitRates[2];
							long HandDiscountID = Math.round(UnitRates[3]);
							double HandDiscountAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((HandDiscountRate * RawCases[i]) + ((HandDiscountRate/UnitsPerSKU) * Units[i])));
							String HandDiscountIDInsert = "null";
							if (HandDiscountID != 0){
								HandDiscountIDInsert = "" + HandDiscountID;
							}
							// end patch
							
							double ItemTotalAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((RawCaseAmount + UnitAmount)));
							double ItemWHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(((ItemTotalAmount * WHTaxRate) / 100)));
							double ItemNetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((ItemTotalAmount + ItemWHTaxAmount)));
							
							InvoiceAmount += ItemTotalAmount;
							InvoiceWHTaxAmount += ItemWHTaxAmount;
							InvoiceNetAmount += ItemNetAmount;						
							
							StockPosting sp = new StockPosting();
							
							
							if(PSIspromo[i]==1){//if promo
								RateRawCase=0;
								RateUnit=0;
								RawCaseAmount=0;
								UnitAmount=0;
							}

							
							
							//System.out.println("product query");
							s.executeUpdate("insert into primary_purchases_orders_products (id, product_id, raw_cases, units, total_units, liquid_in_ml,  rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, is_promotion) values ("+DeskSaleID+", "+ProductID[i]+", "+RawCases[i]+", "+Units[i]+", "+TotalUnits+", "+LiquidInMLValue+", "+RateRawCase+", "+RateUnit+", "+RawCaseAmount+", "+UnitAmount+", "+ItemTotalAmount+","+PSIspromo[i]+") ");
						}
						
						
						
						InvoiceAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount));
						
						
						s.executeUpdate("update primary_purchases_orders set invoice_amount = "+InvoiceAmount+" where id = "+DeskSaleID);
					
					ds.commit();
					
					//boolean posted = SalesPosting.post(DeskSaleID, Long.parseLong(UserID));
					
					//System.out.println("success case");
					
					obj.put("success", "true");
					obj.put("DeskSaleID", DeskSaleID);
					
				}
				
			}else{
				obj.put("success", "false");
				obj.put("error", "You can only edit Today's voucher");
			}
			
			s1.close();
			s.close();
			ds.dropConnection();
		} catch (Exception e) {
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			obj.put("success", "false");
			obj.put("exception", e);
			e.printStackTrace();
			//out.print(e);
		}finally{
			
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//System.out.println("before end close");
		out.print(obj);
		out.close();
		//System.out.println("after end close");
		
	}
	
	private String getProductName(Statement s, int ProductID) throws SQLException{
		
		String ProductName = "";
		ResultSet rs = s.executeQuery("SELECT concat(package_label, ' ', brand_label) product_name FROM pep.inventory_products_view where product_id="+ProductID);
		if(rs.first()){
			ProductName = rs.getString("product_name");
		}
		
		return ProductName;
		
	}
	
}