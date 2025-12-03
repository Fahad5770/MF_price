package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONObject;

import com.pbc.common.EmployeeHierarchy;
import com.pbc.common.User;
import com.pbc.inventory.Product;
import com.pbc.inventory.PromotionItem;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class SyncOrders
 */
@WebServlet(description = "Mobile Sync Orders", urlPatterns = { "/mobile/MobileSyncOrdersV2" })
public class MobileSyncOrdersV2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileSyncOrdersV2() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

    
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		
		PrintWriter out = response.getWriter();
		
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 40000));
		System.out.println(mr.URL);
		
		
		JSONObject json = new JSONObject();
		
		if (!mr.isExpired()){
		
		String order_no = Utilities.filterString(mr.getParameter("order_no"), 1, 100);
		
		
		long outlet_id = Utilities.parseLong(mr.getParameter("outlet_id"));
		String created_on = Utilities.filterString(mr.getParameter("created_on"), 1, 100);
		long created_by = Utilities.parseLong(mr.getParameter("created_by"));
		double sales_tax_rate = Utilities.parseDouble(mr.getParameter("sales_tax_rate"));
		
		if (sales_tax_rate != 0){
			sales_tax_rate = sales_tax_rate - 100;
		}
		double wh_tax_rate = Utilities.parseDouble(mr.getParameter("wh_tax_rate"));
		
		String uuid = Utilities.filterString(mr.getParameter("uuid"), 1, 100);
		String platform = Utilities.filterString(mr.getParameter("platform"), 1, 100);
		double lat = Utilities.parseDouble(mr.getParameter("lat"));
		double lng = Utilities.parseDouble(mr.getParameter("lng"));
		double accuracy_d = Utilities.parseDouble(mr.getParameter("accuracy"));
		
		long accuracy = Math.round(accuracy_d);
		
		int product_id[] = Utilities.parseInt(mr.getParameterValues("product_id"));
		int quantity[] = Utilities.parseInt(mr.getParameterValues("quantity"));
		int unit_quantity[] = Utilities.parseInt(mr.getParameterValues("unit_quantity"));
		
		int is_promotion[] = Utilities.parseInt(mr.getParameterValues("is_promotion"));
		int promotion_id[] = Utilities.parseInt(mr.getParameterValues("promotion_id"));
		
		Datasource ds = new Datasource();
		
		try {
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			
			ResultSet rs4 = s.executeQuery("select * from inventory_sales_tax_rates");
			while (rs4.next()){
				if (rs4.getInt("id") == 1){
					sales_tax_rate = rs4.getDouble("rate");
				}else if (rs4.getInt("id") == 2) {
					wh_tax_rate = rs4.getDouble("rate");
				}
			}
			
			long DistributorID = 0;
			long RegionID = 0;
			// Get Distributor and Region ID
			
			ResultSet rs = s.executeQuery("select co.region_id, (SELECT distributor_id FROM common_outlets_distributors_view codv where outlet_id = co.id limit 1) distribtor_id from common_outlets co where co.id = "+outlet_id);
			if (rs.first()){
				DistributorID = rs.getLong(2);
				RegionID = rs.getLong(1);
			}
			
			boolean OrderAlreadyExists = false;
			boolean shouldIgnore = false;
			ResultSet rs5 = s.executeQuery("SELECT status_type_id from mobile_order where mobile_order_no = "+order_no);
			if (rs5.first()){
				
				if (rs5.getInt("status_type_id") == 1){
					OrderAlreadyExists = true;
				}else{
					shouldIgnore = true;
				}
				
				
			}
			
			if (shouldIgnore == false){
			
				if (OrderAlreadyExists == true){
					s.executeUpdate("delete from mobile_order where mobile_order_no = "+order_no+" and status_type_id = 1");
				}
				
				s.executeUpdate("insert into mobile_order (mobile_order_no, outlet_id, distributor_id, region_id, created_on, created_by, sales_tax_rate, wh_tax_rate, uuid, platform, lat, lng, accuracy, mobile_timestamp) values "+
				"("+order_no+", "+outlet_id+", "+DistributorID+", "+RegionID+",now(), "+created_by+", "+sales_tax_rate+", "+wh_tax_rate+", '"+uuid+"', '"+platform+"', "+lat+", "+lng+", "+accuracy+", '"+created_on+"' ) ");
				
				long OrderID = 0;
				ResultSet rs2 = s.executeQuery("select LAST_INSERT_ID()");
				if(rs2.first()){
					OrderID = rs2.getLong(1);
				}			
				
				double InvoiceAmount = 0;
				double InvoicePromotionAmount = 0;
				
				
				// Product and Quantity Array for Promotion Calculation
				
				List<Integer> ProductIDArray = new ArrayList<Integer>();
				List<Long> TotalUnitsArray = new ArrayList<Long>();
				
					
				if (product_id != null){
				
				for(int i = 0; i < product_id.length; i++){
						
					if (is_promotion[i] == 0){
						
						int UnitsPerSKU = 0;
						long LiquidInMLPerUnit = 0;
						
						ResultSet rs3 = s.executeQuery("SELECT unit_per_sku, liquid_in_ml FROM pep.inventory_products_view where product_id = "+product_id[i]);
						if (rs3.first()){
							UnitsPerSKU = rs3.getInt(1);
							LiquidInMLPerUnit = rs3.getLong(2);
						}
						
						int TotalUnits = (quantity[i] * UnitsPerSKU) + unit_quantity[i];
						long LiquidinML = LiquidInMLPerUnit * TotalUnits;
						
						double UnitRates[] = Product.getSellingPrice(product_id[i], outlet_id);
						double RateRawCase = UnitRates[0];
						double RateUnit = UnitRates[1];
						
						double AmountRawCases = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((quantity[i] * RateRawCase)));
						double AmountUnits = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((unit_quantity[i] * RateUnit)));
						double TotalAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((AmountRawCases + AmountUnits)));
						
						InvoiceAmount += TotalAmount;
						
						String PromotionID = null; 
						
						ProductIDArray.add(product_id[i]);
						TotalUnitsArray.add(TotalUnits * 1l);
						
						s.executeUpdate("replace into mobile_order_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, net_amount, is_promotion, promotion_id) values ("+
								OrderID+", "+product_id[i]+", "+quantity[i]+", "+unit_quantity[i]+", "+TotalUnits+", "+LiquidinML+", "+RateRawCase+", "+RateUnit+", "+AmountRawCases+", "+AmountUnits+", "+TotalAmount+", "+TotalAmount+", "+is_promotion[i]+", "+PromotionID+")  ");
					}
				}
				
				
				if (ProductIDArray.size() > 0){
						
					PromotionItem PromotionProducts[] = Product.getPromotionItems(outlet_id, ArrayUtils.toPrimitive(ProductIDArray.toArray(new Integer[ProductIDArray.size()])), ArrayUtils.toPrimitive(TotalUnitsArray.toArray(new Long[TotalUnitsArray.size()])));
					
					for (int i = 0; i < PromotionProducts.length; i++){
						
						long RawCasesAndUnits[] = Utilities.getRawCasesAndUnits(PromotionProducts[i].TOTAL_UNITS, PromotionProducts[i].UNIT_PER_SKU);
						
						long ProSAPCode = 0;
						int ProProductID = 0;
						double ProSellingPriceRawCase = 0;
						double ProSellingPriceUnit = 0;
						long ProLiquidInML = 0;
						double ProAmount = 0;
						
						int BrandID = getBrandID(PromotionProducts[i].PROMOTION_ID, product_id, promotion_id);
						
						if (BrandID != 0){
							
							Product PromotionProduct = new Product(1, PromotionProducts[i].PACKAGE_ID, PromotionProducts[i].BRANDS.get(0));
							ProProductID = PromotionProduct.PRODUCT_ID;
							ProSAPCode = PromotionProduct.SAP_CODE;
							double rates[] = Product.getSellingPrice(PromotionProduct.SAP_CODE, outlet_id);
							ProSellingPriceRawCase = rates[0];
							ProSellingPriceUnit = rates[1];
							ProLiquidInML = PromotionProduct.LIQUID_IN_ML;
							
							double ProRawCaseAmount = (RawCasesAndUnits[0] * ProSellingPriceRawCase);
							double ProUnitsAmount = (RawCasesAndUnits[1] * ProSellingPriceUnit);
							
							ProAmount = ProRawCaseAmount + ProUnitsAmount;
							
							InvoicePromotionAmount += ProAmount;
							
							s.executeUpdate("replace into mobile_order_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, net_amount, is_promotion, promotion_id) values ("+
									OrderID+", "+ProProductID+", "+RawCasesAndUnits[0]+", "+RawCasesAndUnits[1]+", "+PromotionProducts[i].TOTAL_UNITS+", "+ProLiquidInML+", "+ProSellingPriceRawCase+", "+ProSellingPriceUnit+", "+ProRawCaseAmount+", "+ProUnitsAmount+", "+ProAmount+", "+ProAmount+", 1, "+PromotionProducts[i].PROMOTION_ID+")  ");
							
						}
						
					}
					
				}
				
				
				}
				InvoiceAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount));
				InvoicePromotionAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoicePromotionAmount));
				
				double TotalAmountExSalesTax = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(
								(InvoiceAmount / (sales_tax_rate + 100))*100
				));
				
				double TotalAmountPromotionExSalesTax = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(
						(InvoicePromotionAmount / (sales_tax_rate + 100))*100
				));			
				
				double SalesTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount - TotalAmountExSalesTax));
				double WHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(((TotalAmountExSalesTax + TotalAmountPromotionExSalesTax) * wh_tax_rate)/100));
				
				double InvoiceTotalAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount  + WHTaxAmount));
				
				String InoviceTotalAmountString = InvoiceTotalAmount + "";
				
				if (InoviceTotalAmountString.indexOf(".") != -1){
					double Fraction = Utilities.parseDouble(InoviceTotalAmountString.substring(InoviceTotalAmountString.indexOf("."), InoviceTotalAmountString.length()));
					
					InoviceTotalAmountString = InoviceTotalAmountString.substring(0, InoviceTotalAmountString.indexOf("."));
					
					if (Fraction != 0){
						InoviceTotalAmountString = (Utilities.parseInt(InoviceTotalAmountString)+1)+"";
					}
				}
				
				
				double FractionAmount = Utilities.parseDouble(InoviceTotalAmountString) - InvoiceTotalAmount;
				
				
				s.executeUpdate("update mobile_order set invoice_amount = "+InvoiceAmount+", sales_tax_amount  = "+SalesTaxAmount+", wh_tax_amount = "+WHTaxAmount+", total_amount = "+InvoiceTotalAmount+", fraction_adjustment = "+Utilities.getDisplayCurrencyFormatSimple(FractionAmount)+", net_amount = "+InoviceTotalAmountString+" where id = "+OrderID);
				
				
				
				// Hierarchy Update
				EmployeeHierarchy eh = new EmployeeHierarchy();
				s.executeUpdate("replace into mobile_order_hierarchy (order_id, hierarchy_level_id, user_id) values("+OrderID+", (select current_reporting_level from users where id = "+created_by+"), "+created_by+")");
				User list[] = eh.getReportingToAll(created_by);
				for (int i = 0; i < list.length; i++){
					if (list[i].HIERARCHY_LEVEL_ID != 1){
						s.executeUpdate("replace into mobile_order_hierarchy (order_id, hierarchy_level_id, user_id) values("+OrderID+","+list[i].HIERARCHY_LEVEL_ID+", "+list[i].USER_ID+")");
					}
				}
				
			}
			
			
			ds.commit();
			
			s.close();
			ds.dropConnection();
			
			json.put("success", "true");
			
		} catch (Exception e) {
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			json.put("success", "false");
			json.put("error_code", "106");
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
		
		
		}else{
			json.put("success", "false");
			json.put("error_code", "101");
		}
		
		out.print(json);*/
		
	}
	
    private int getBrandID(long PromotionID, int ProductID[], int PromotionIDs[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		int pret = 0;
		for (int i = 0; i < ProductID.length; i++){
			
			if (PromotionIDs[i] == PromotionID){
				pret = ProductID[i];
			}
			
		}
		
		int ret = 0;
		
		if (pret != 0){
			
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			
			ResultSet rs = s.executeQuery("select brand_id from inventory_products where id  ="+pret);
			if (rs.first()){
				ret = rs.getInt(1);
			}
			
			s.close();
			ds.dropConnection();
			
		}
		
		
		return ret;
	}


}
