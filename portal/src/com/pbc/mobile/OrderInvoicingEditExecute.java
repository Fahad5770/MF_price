package com.pbc.mobile;

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

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Order Invoicing Edit Execute", urlPatterns = { "/mobile/OrderInvoicingEditExecute" })
public class OrderInvoicingEditExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public OrderInvoicingEditExecute() {
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
		long RegionID = Utilities.parseLong(request.getParameter("DeskSaleRegionIDHidden"));
		long BookedBy = Utilities.parseLong(request.getParameter("DeskSaleCreatedByHidden"));
		
		
		
		double SalesTaxRate = Utilities.parseDouble(request.getParameter("DeskSaleSalesTaxRateHidden"));
		double WHTaxRate = Utilities.parseDouble(request.getParameter("DeskSaleWHTaxHidden"));
		
		double InvoiceAmount = Utilities.parseDouble(request.getParameter("DeskSaleMainFormTotalInvoiceAmount"));
		double SalesTaxAmount = Utilities.parseDouble(request.getParameter("DeskSaleMainFormSalesTax"));
		double WHTaxAmount = Utilities.parseDouble(request.getParameter("DeskSaleMainFormWithHoldingTax"));
		double TotalAmount = Utilities.parseDouble(request.getParameter("DeskSaleMainFormTotalAmount"));
		double TotalDiscout = Utilities.parseDouble(request.getParameter("DeskSaleMainFormTotalDiscount"));
		double Adjustment = Utilities.parseDouble(request.getParameter("DeskSaleMainFormAdjustment"));
		double NetAmount = Utilities.parseDouble(request.getParameter("DeskSaleMainFormFinalNetAmountRounded"));
		
		long ProductID[] = Utilities.parseLong(request.getParameterValues("ProductID"));
		int RawCases[] = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormRawCases"));
		int Units[] = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormUnits"));
		
		double Rate[] = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormRateHidden"));
		double UnitRate[] = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormUnitRateHidden"));
		
		double Discount[] = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormDiscount"));
		int UnitPerSKU[] = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormUnitPerSKU"));
		int LiquidInML[] = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormLiquidInML"));
		
		double RowAmount[] = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormAmount"));
		double RowNetAmount[] = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormNetAmount"));
		
		
		long PromotionID[] = Utilities.parseLong(request.getParameterValues("PromotionID"));
		
		long PromotionsProductID[] = Utilities.parseLong(request.getParameterValues("PromotionsProductID"));
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
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			
			
			String SQLMain = "";
			
			SQLMain = "update mobile_order set is_edited=1, edited_on=now(), edited_by="+UserID+", invoice_amount="+InvoiceAmount +", sales_tax_rate="+SalesTaxRate+", sales_tax_amount="+SalesTaxAmount+", wh_tax_rate="+WHTaxRate+", wh_tax_amount="+WHTaxAmount+", total_amount="+TotalAmount+", net_amount="+NetAmount+", fraction_adjustment="+Adjustment+" where id="+DeskSaleEditID;
			
			s.executeUpdate(SQLMain);
			
			s.executeUpdate("delete from mobile_order_products where id="+DeskSaleEditID);
			//try{
				for(int i = 0; i < ProductID.length; i++){
					
					long TotalUnits = ( RawCases[i] * UnitPerSKU[i] ) + Units[i];
					long LiquidInMLValue = TotalUnits * LiquidInML[i];
					double RawCaseAmount = RawCases[i] * Rate[i];
					double UnitAmount = Units[i] * UnitRate[i];
					
					double ItemTotalAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((RawCaseAmount + UnitAmount)));
					double ItemWHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(((ItemTotalAmount * WHTaxRate) / 100)));
					double ItemNetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((ItemTotalAmount + ItemWHTaxAmount)));
					
					//System.out.println("insert into mobile_order_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, discount, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, net_amount) values ("+DeskSaleEditID+", "+ProductID[i]+", "+RawCases[i]+", "+Units[i]+", "+TotalUnits+", "+LiquidInMLValue+", "+Discount[i]+", "+Rate[i]+", "+UnitRate[i]+", "+RawCaseAmount+", "+UnitAmount+", "+RowAmount[i]+", "+RowNetAmount[i]+") ");
					s.executeUpdate("insert into mobile_order_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, discount, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount) values ("+DeskSaleEditID+", "+ProductID[i]+", "+RawCases[i]+", "+Units[i]+", "+TotalUnits+", "+LiquidInMLValue+", "+Discount[i]+", "+Rate[i]+", "+UnitRate[i]+", "+RawCaseAmount+", "+UnitAmount+", "+ItemTotalAmount+", "+ItemWHTaxAmount+","+ItemNetAmount+") ");
				}
				
				
				if( PromotionsProductID != null ){
					for(int i = 0; i < PromotionsProductID.length; i++){
						
						long TotalUnits = ( PromotionsRawCases[i] * PromotionsUnitPerSKU[i] ) + PromotionsUnits[i];
						long LiquidInMLValue = TotalUnits * PromotionsLiquidInML[i];
						double RawCaseAmount = PromotionsRawCases[i] * PromotionsRate[i];
						double UnitAmount = PromotionsUnits[i] * PromotionsUnitRate[i];
						
						
						double ItemTotalAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((RawCaseAmount + UnitAmount)));
						double ItemWHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(((ItemTotalAmount * WHTaxRate) / 100)));
						
						double ItemNetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((ItemTotalAmount + ItemWHTaxAmount)));

						
						//System.out.println("insert into mobile_order_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, discount, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, net_amount, is_promotion, promotion_id) values ("+DeskSaleEditID+", "+PromotionsProductID[i]+", "+PromotionsRawCases[i]+", "+PromotionsUnits[i]+", "+TotalUnits+", "+LiquidInMLValue+", 0, "+PromotionsRate[i]+", "+PromotionsUnitRate[i]+", "+Utilities.getDisplayCurrencyFormatSimple(RawCaseAmount)+", "+Utilities.getDisplayCurrencyFormatSimple(UnitAmount)+", "+PromotionsRowAmount[i]+", "+PromotionsRowAmount[i]+", 1, "+PromotionID[i]+") ");
						s.executeUpdate("insert into mobile_order_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, discount, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, is_promotion, promotion_id) values ("+DeskSaleEditID+", "+PromotionsProductID[i]+", "+PromotionsRawCases[i]+", "+PromotionsUnits[i]+", "+TotalUnits+", "+LiquidInMLValue+", 0, "+PromotionsRate[i]+", "+PromotionsUnitRate[i]+", "+Utilities.getDisplayCurrencyFormatSimple(RawCaseAmount)+", "+Utilities.getDisplayCurrencyFormatSimple(UnitAmount)+", "+ItemTotalAmount+", "+ItemWHTaxAmount+","+ItemNetAmount+", 1, "+PromotionID[i]+") ");
					}
				}
				
				//s.executeUpdate("update mobile_order set status_type_id=1, status_on=now() where id="+DeskSaleEditID);
				
			//}catch(Exception e){
			///	System.out.println("Exception during inserting products in desk sale");
			//	System.out.println(e);
			//}
			
			
			
			obj.put("success", "true");
			obj.put("DeskSaleID", DeskSaleEditID);
					
				
				
			
			
			ds.commit();
			
			s1.close();
			s.close();
			ds.dropConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally{
				try {
					ds.dropConnection();
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		}
		
		out.print(obj);
		out.close();
		
	}
	
}