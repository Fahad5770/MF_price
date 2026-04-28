package com.pbc.inventory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

/**
 * Servlet implementation class PostSalesReturnExecute
 */
@WebServlet(description = "Executes Desk Sale", urlPatterns = { "/inventory/PostSalesReturnExecute"})
public class PostSalesReturnExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostSalesReturnExecute() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		
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
		long RegionID = Utilities.parseLong(request.getParameter("DeskSaleRegionIDHidden"));
		
		int returnType = Utilities.parseInt(request.getParameter("returnId"));
		
		double SalesTaxRate = Utilities.parseDouble(request.getParameter("DeskSaleSalesTaxRateHidden"));
		double WHTaxRate = Utilities.parseDouble(request.getParameter("DeskSaleWHTaxHidden"));
		
		//double InvoiceAmount = Utilities.parseDouble(request.getParameter("DeskSaleMainFormTotalInvoiceAmount"));
		//double SalesTaxAmount = Utilities.parseDouble(request.getParameter("DeskSaleMainFormSalesTax"));
		double WHTaxAmount = Utilities.parseDouble(request.getParameter("DeskSaleMainFormWithHoldingTax"));
		double TotalAmount = Utilities.parseDouble(request.getParameter("DeskSaleMainFormTotalAmount"));
		double TotalDiscout = Utilities.parseDouble(request.getParameter("DeskSaleMainFormTotalDiscount"));
		double Adjustment = Utilities.parseDouble(request.getParameter("DeskSaleMainFormAdjustment"));
		double NetAmount = Utilities.parseDouble(request.getParameter("DeskSaleMainFormFinalNetAmountRounded"));
		
		int ProductID[] = Utilities.parseInt(request.getParameterValues("ProductID"));
		int RawCases[] = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormRawCases"));
		int Units[] = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormUnits"));
		
		double aRate[] = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormRateHidden"));
		double aUnitRate[] = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormUnitRateHidden"));
		
		double Discount[] = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormDiscount"));
		int UnitPerSKU[] = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormUnitPerSKU"));
		int LiquidInML[] = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormLiquidInML"));
		
		double RowAmount[] = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormAmount"));
		double RowNetAmount[] = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormNetAmount"));
		
		
		
		long PromotionID[] = Utilities.parseLong(request.getParameterValues("PromotionID"));
		
		int PromotionsProductID[] = Utilities.parseInt(request.getParameterValues("PromotionsProductID"));
		int PromotionsRawCases[] = Utilities.parseInt(request.getParameterValues("PromotionsRawCases"));
		int PromotionsUnits[] = Utilities.parseInt(request.getParameterValues("PromotionsUnits"));
		
		double PromotionsRate[] = Utilities.parseDouble(request.getParameterValues("PromotionsRateRawCase"));
		double PromotionsUnitRate[] = Utilities.parseDouble(request.getParameterValues("PromotionsRateUnit"));
		
		int PromotionsUnitPerSKU[] = Utilities.parseInt(request.getParameterValues("PromotionsUnitPerSKU"));
		int PromotionsLiquidInML[] = Utilities.parseInt(request.getParameterValues("PromotionsLiquidInML"));
		
		double PromotionsRowAmount[] = Utilities.parseDouble(request.getParameterValues("PromotionsAmount"));
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		Date VoucherDate = new java.util.Date();
		
		try {
			
			ds.createConnection();
			
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			
			
			
			if(isEditCase){
				ResultSet rs_date_check = s.executeQuery("select created_on from inventory_sales_returns where id="+DeskSaleEditID);
				if(rs_date_check.first()){
					VoucherDate = rs_date_check.getDate(1);
				}
			}			
		
			
			//if( DateUtils.isSameDay(new java.util.Date(), VoucherDate) ){	// false only in editcase on date conflict
			if( DateUtils.isSameDay(new java.util.Date(), VoucherDate) ){	// false only in editcase on date conflict
				String SQLMain = "";
				
				ResultSet rs1 = s1.executeQuery("select id from inventory_sales_returns where uvid="+UniqueVoucherID);
				if( rs1.first() ){
					
					obj.put("success", "false");
					obj.put("error", "Already Exists");
					
				}else{
					
					ds.startTransaction();				
					
					
					// Patch for updating PJP ID
					
					long PJPID = 0;
					ResultSet rsp = s.executeQuery("select distinct dbp.id from distributor_beat_plan dbp join distributor_beat_plan_schedule dbps on dbp.id = dbps.id where dbps.outlet_id = "+OutletID+" and dbp.distributor_id = "+DistributorID);
					while(rsp.next()){
						PJPID = rsp.getLong(1);
					}
					// 
					
					
					if(isEditCase){
						//SQLMain = "replace into inventory_sales_returns (id, uvid, created_on, created_by, outlet_id, type_id, distributor_id, invoice_amount, sales_tax_rate, sales_tax_amount, wh_tax_rate, wh_tax_amount, total_amount, discount, net_amount, region_id, fraction_adjustment, beat_plan_id) values("+DeskSaleEditID+", "+UniqueVoucherID+", now(), "+UserID+", "+OutletID+", 1, "+DistributorID+", 0, "+SalesTaxRate+", 0, "+WHTaxRate+", 0, 0, 0, 0, "+RegionID+", 0, "+PJPID+" )";
					}else{
						SQLMain = "insert into inventory_sales_returns (uvid, created_on, created_by, outlet_id, type_id, distributor_id, invoice_amount, sales_tax_rate, sales_tax_amount, wh_tax_rate, wh_tax_amount, total_amount, discount, net_amount, region_id, fraction_adjustment, beat_plan_id, return_type) values("+UniqueVoucherID+", now(), "+UserID+", "+OutletID+", 1, "+DistributorID+", 0, "+SalesTaxRate+", 0, "+WHTaxRate+", 0, 0, 0, 0, "+RegionID+", 0, "+PJPID+", "+returnType+")";
					}
					
					//System.out.println(SQLMain);
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
					
					s.executeUpdate("delete from inventory_sales_returns_products where id="+DeskSaleID);
					
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
							
							
							
							
							s.executeUpdate("insert into inventory_sales_returns_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, discount, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, hand_discount_rate, hand_discount_amount, hand_discount_id) values ("+DeskSaleID+", "+ProductID[i]+", "+RawCases[i]+", "+Units[i]+", "+TotalUnits+", "+LiquidInMLValue+", "+Discount[i]+", "+RateRawCase+", "+RateUnit+", "+RawCaseAmount+", "+UnitAmount+", "+ItemTotalAmount+", "+ItemWHTaxAmount+","+ItemNetAmount+","+HandDiscountRate+", "+HandDiscountAmount+", "+HandDiscountIDInsert+") ");
						}
						
						InvoiceAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount));
						InvoiceWHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceWHTaxAmount));
						InvoiceNetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceNetAmount));
						
						
						double TotalAmountExSalesTax = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(
										(InvoiceAmount / (SalesTaxRate + 100))*100
						));
						
						double SalesTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount - TotalAmountExSalesTax));
						
						String InoviceTotalAmountString = InvoiceNetAmount + "";
						
						if (InoviceTotalAmountString.indexOf(".") != -1){
							double Fraction = Utilities.parseDouble(InoviceTotalAmountString.substring(InoviceTotalAmountString.indexOf("."), InoviceTotalAmountString.length()));
							
							InoviceTotalAmountString = InoviceTotalAmountString.substring(0, InoviceTotalAmountString.indexOf("."));
							
							if (Fraction != 0){
								InoviceTotalAmountString = (Utilities.parseInt(InoviceTotalAmountString)+1)+"";
							}
						}
						
						
						double FractionAmount = Utilities.parseDouble(InoviceTotalAmountString) - InvoiceNetAmount;
						
						
						if( PromotionsProductID != null ){
							for(int i = 0; i < PromotionsProductID.length; i++){
								
								
								int UnitsPerSKU = 0;
								long LiquidInMLPerUnit = 0;
								
								ResultSet rs3 = s.executeQuery("SELECT unit_per_sku, liquid_in_ml FROM inventory_products_view where product_id = "+PromotionsProductID[i]);
								if (rs3.first()){
									UnitsPerSKU = rs3.getInt(1);
									LiquidInMLPerUnit = rs3.getLong(2);
								}
								
								long TotalUnits = ( PromotionsRawCases[i] * UnitsPerSKU ) + PromotionsUnits[i];
								long LiquidInMLValue = TotalUnits * LiquidInMLPerUnit;
								
								double UnitRates[] = Product.getSellingPrice(ProductID[i], OutletID);
								double RateRawCase = UnitRates[0];
								double RateUnit = UnitRates[1];
								
								
								double RawCaseAmount = PromotionsRawCases[i] * RateRawCase;
								double UnitAmount = PromotionsUnits[i] * RateUnit;
								
								double ItemTotalAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((RawCaseAmount + UnitAmount)));
								double ItemWHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(((ItemTotalAmount * WHTaxRate) / 100)));
								
								double ItemNetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((ItemTotalAmount + ItemWHTaxAmount)));
																
								
								
								
								
								
								s.executeUpdate("insert into inventory_sales_returns_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, discount, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, is_promotion, promotion_id) values ("+DeskSaleID+", "+PromotionsProductID[i]+", "+PromotionsRawCases[i]+", "+PromotionsUnits[i]+", "+TotalUnits+", "+LiquidInMLValue+", 0, "+PromotionsRate[i]+", "+PromotionsUnitRate[i]+", "+Utilities.getDisplayCurrencyFormatSimple(RawCaseAmount)+", "+Utilities.getDisplayCurrencyFormatSimple(UnitAmount)+", "+ItemTotalAmount+", "+ItemWHTaxAmount+","+ItemNetAmount+", 1, "+PromotionID[i]+") ");
							}
						}

						s.executeUpdate("update inventory_sales_returns set invoice_amount = "+InvoiceAmount+", sales_tax_amount  = "+SalesTaxAmount+", wh_tax_amount = "+InvoiceWHTaxAmount+", total_amount = "+InvoiceNetAmount+", fraction_adjustment = "+Utilities.getDisplayCurrencyFormatSimple(FractionAmount)+", net_amount = "+InoviceTotalAmountString+" where id = "+DeskSaleID);
								
					ds.commit();
					
					if(returnType == 1) { // if return type is sales
					StockPosting sp = new StockPosting();
					boolean isPosted = sp.postPostSalesReturn(DeskSaleID);
					sp.close();
					}
					
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
