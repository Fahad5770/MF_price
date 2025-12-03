package com.pbc.tempJavaScripts;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.pbc.inventory.Product;
import com.pbc.inventory.PromotionItem;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class SalesPosting_s {

	public static boolean post(long InvoiceID, long UserID) {
		boolean success = false;
		Datasource ds = new Datasource();
		
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			boolean isReturned = false;
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			double SalesTaxRate = 0;
			double WHTaxRate = 0;
			boolean isValid = false;
			String CreatedOnDate = "";
			String DistributorID = "";
			String OrderID = "";
			String OrderDate = "";
			String BookedBy = null;
			
			boolean ifProductExists = false;
			
			ResultSet rs = s.executeQuery("select * from inventory_sales_invoices where id = "+InvoiceID);
			if(rs.first()){
				isValid = true;
				SalesTaxRate = rs.getDouble("sales_tax_rate");
				WHTaxRate = rs.getDouble("wh_tax_rate");
				CreatedOnDate = rs.getString("created_on");
				DistributorID = rs.getString("distributor_id");
				OrderID = rs.getString("order_id");
				BookedBy = rs.getString("booked_by");
				
				s2.executeUpdate("replace into inventory_sales_adjusted (id, uvid, order_id, created_on, created_by, outlet_id, distributor_id, region_id, booked_by, type_id, sales_tax_rate, wh_tax_rate, adjusted_on, adjusted_by, beat_plan_id) values "+
						"("+InvoiceID+", "+rs.getString("uvid")+", "+rs.getString("order_id")+", '"+rs.getString("created_on")+"', "+rs.getString("created_by")+", "+rs.getString("outlet_id")+", "+rs.getString("distributor_id")+", "+rs.getString("region_id")+", "+rs.getString("booked_by")+", "+rs.getString("type_id")+","+SalesTaxRate+", "+WHTaxRate+", now(), "+UserID+", "+rs.getString("beat_plan_id")+" ) ");
				
				
				
				
				s2.executeUpdate("update inventory_sales_adjusted isa set order_created_on_date = (select date(created_on) from mobile_order where id = "+rs.getString("order_id")+") where isa.id = "+InvoiceID);
				s2.executeUpdate("update inventory_sales_adjusted isa set created_on_date = date('"+rs.getString("created_on")+"') where isa.id = "+InvoiceID);
				
			}
			
			
			if (isValid == true){
				
				
				
				
				s.executeUpdate("delete from inventory_sales_adjusted_products where id = "+InvoiceID);
				
				
				double InvoiceAmount = 0;
				double InvoiceWHTaxAmount = 0;
				double InvoiceNetAmount = 0;					

				
				//ResultSet rs2 = s.executeQuery("SELECT isip.discount, isip.product_id, isip.is_promotion, ipv.unit_per_sku, ipv.liquid_in_ml, isip.product_id,  isip.rate_raw_cases, isip.rate_units, isip.total_units units_sold, ifnull(isdap.total_units,0) units_returned, isip.promotion_id FROM inventory_sales_invoices_products  isip join inventory_products_view ipv on isip.product_id = ipv.product_id left outer join inventory_sales_dispatch_adjusted_products isdap on isip.id = isdap.invoice_id and isip.product_id = isdap.product_id and isip.promotion_id = isdap.promotion_id where isip.id = "+InvoiceID);
				
				/*
				ResultSet rs2 = s.executeQuery("SELECT isip.discount, isip.product_id, isip.is_promotion, ipv.unit_per_sku, ipv.liquid_in_ml, isip.product_id,  isip.rate_raw_cases, isip.rate_units, isip.total_units units_sold, ifnull(("+
						"select total_units from inventory_sales_dispatch_adjusted_products isdap where isdap.invoice_id = isip.id and isdap.product_id = isip.product_id and ifnull(isdap.promotion_id,0) = ifnull(isip.promotion_id,0)"+
					"),0) units_returned, isip.promotion_id FROM inventory_sales_invoices_products  isip join inventory_products_view ipv on isip.product_id = ipv.product_id where isip.id = "+InvoiceID);
				*/
				
				
				
				ResultSet rs2 = s.executeQuery("SELECT isip.discount, isip.product_id, isip.is_promotion, ipv.unit_per_sku, ipv.liquid_in_ml, isip.product_id,  isip.rate_raw_cases, isip.rate_units, isip.total_units units_sold, ifnull(("+
						"select tab1.total_units from (select product_id, total_units, ifnull(promotion_id,0) promotion_id from inventory_sales_dispatch_adjusted_products isdap where isdap.invoice_id = "+InvoiceID+") tab1 where tab1.product_id = isip.product_id and tab1.promotion_id = ifnull(isip.promotion_id,0) "+
					"),0) units_returned, isip.promotion_id, isip.hand_discount_rate, isip.hand_discount_id, ipv.package_id, ipv.brand_id, ipv.lrb_type_id FROM inventory_sales_invoices_products  isip join inventory_products_view ipv on isip.product_id = ipv.product_id where isip.id = "+InvoiceID);
				
				while(rs2.next()){
					
					int LRBTypeID = rs2.getInt("lrb_type_id");
					
					int PackageID = rs2.getInt("package_id");
					int BrandID = rs2.getInt("brand_id");
					
					String PromotionID = rs2.getString("promotion_id");
					
					int ProductID = rs2.getInt("product_id");
					double ProductDiscount = rs2.getDouble("discount");
					
					int UnitsPerSKU = rs2.getInt("unit_per_sku");
					long LiquidInMLPerUnit = rs2.getLong("liquid_in_ml");
					
					int TotalUnits = rs2.getInt("units_sold") - rs2.getInt("units_returned");
					
					if (TotalUnits > 0){
						long LiquidinML = LiquidInMLPerUnit * TotalUnits;
						
						double RateRawCase = rs2.getDouble("rate_raw_cases");
						double RateUnit = rs2.getDouble("rate_units");
						
						long ConvertedUnits[] = Utilities.getRawCasesAndUnits(TotalUnits, UnitsPerSKU);
						
						long RawCases = ConvertedUnits[0];
						long Units = ConvertedUnits[1];
						
						// patch for hand discount
						double HandDiscountRate = rs2.getDouble("hand_discount_rate");
						long HandDiscountID = rs2.getLong("hand_discount_id");
						double HandDiscountAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((HandDiscountRate * RawCases) + ((HandDiscountRate/UnitsPerSKU) * Units)));
						String HandDiscountIDInsert = "null";
						if (HandDiscountID != 0){
							HandDiscountIDInsert = "" + HandDiscountID;
						}						
						// end patch						
						
						
						double AmountRawCases = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((RawCases * RateRawCase)));
						double AmountUnits = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((Units * RateUnit)));
						double TotalAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((AmountRawCases + AmountUnits)));

						double WHTaxAmount=0;
						if(LRBTypeID==2) {
							 WHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((TotalAmount * WHTaxRate / 100)));
						}else {
							WHTaxRate=0;
						}
						
						double NetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((TotalAmount + WHTaxAmount)));
						
						int isPromotion = rs2.getInt("is_promotion");
						
						if (isPromotion == 0){
							InvoiceAmount += TotalAmount;
							InvoiceWHTaxAmount += WHTaxAmount;
							InvoiceNetAmount += NetAmount;						
						}
						
						
						ifProductExists = true;
						s2.executeUpdate("insert into inventory_sales_adjusted_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, discount, wh_tax_amount, net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id, cache_created_on_date, cache_distributor_id, cache_package_id, cache_brand_id, cache_order_id, cache_booked_by, cache_units_per_sku, cache_lrb_type_id) values ("+
								InvoiceID+", "+ProductID+", "+RawCases+", "+Units+", "+TotalUnits+", "+LiquidinML+", "+RateRawCase+", "+RateUnit+", "+AmountRawCases+", "+AmountUnits+", "+TotalAmount+", "+ProductDiscount+", "+WHTaxAmount+" ,"+NetAmount+", "+isPromotion+", "+PromotionID+","+HandDiscountRate+", "+HandDiscountAmount+", "+HandDiscountIDInsert+", date('"+CreatedOnDate+"'), "+DistributorID+", "+PackageID+", "+BrandID+", "+OrderID+", "+BookedBy+","+UnitsPerSKU+", "+LRBTypeID+")  ");
						
						
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
					
					s2.executeUpdate("update inventory_sales_adjusted set invoice_amount = "+InvoiceAmount+", sales_tax_amount  = "+SalesTaxAmount+", wh_tax_amount = "+InvoiceWHTaxAmount+", total_amount = "+InvoiceNetAmount+", fraction_adjustment = "+Utilities.getDisplayCurrencyFormatSimple(FractionAmount)+", net_amount = "+InoviceTotalAmountString+" where id = "+InvoiceID);
					s2.executeUpdate("update inventory_sales_adjusted_products isa set cache_order_created_on_date = (select date(created_on) from mobile_order where id = "+OrderID+") where isa.id = "+InvoiceID);
					
				}
				
				if (ifProductExists == false){
					//	s.executeUpdate("delete from inventory_sales_adjusted where id = "+InvoiceID);
				}
				
			}
			
			
			s.close();
			s2.close();
			ds.commit();
			success = true;
		}catch(Exception e){
			System.out.print(e);
			try {
				ds.rollback();
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
		} finally{
			if (ds != null){
				try {
					ds.dropConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return success;
	}
	
	public static boolean postOrder2Invoice(long OrderID, long UserID, long UVID) {
		boolean success = false;
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			boolean isReturned = false;
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			double SalesTaxRate = 0;
			double WHTaxRate = 0;
			boolean isValid = false;
			
			ResultSet rs = s.executeQuery("select * from mobile_order where id = "+OrderID);
			if(rs.first()){
				isValid = true;
				SalesTaxRate = rs.getDouble("sales_tax_rate");
				WHTaxRate = rs.getDouble("wh_tax_rate");
				
				s2.executeUpdate("delete from inventory_sales_invoices where order_id = "+OrderID);
				s2.executeUpdate("insert into inventory_sales_invoices (uvid, order_id, created_on, created_by, outlet_id, distributor_id, region_id, booked_by, type_id, sales_tax_rate, wh_tax_rate, beat_plan_id) values "+
						"("+UVID+", "+OrderID+", now(), "+UserID+", "+rs.getString("outlet_id")+", "+rs.getString("distributor_id")+", "+rs.getString("region_id")+", "+rs.getString("created_by")+", 3,"+SalesTaxRate+", "+WHTaxRate+","+rs.getString("beat_plan_id")+")");
			}

			if (isValid == true){
				
				long SaleInvoiceID = 0;
				
				ResultSet rs9 = s2.executeQuery("select LAST_INSERT_ID()");
				if(rs9.first()){
					SaleInvoiceID = rs9.getLong(1);
				}			
				
				
				
				s.executeUpdate("delete from inventory_sales_invoices_products where id = "+SaleInvoiceID);
				
				double InvoiceAmount = 0;
				double InvoiceWHTaxAmount = 0;
				double InvoiceNetAmount = 0;				
				
				double InvoicePromotionAmount = 0;
				
				//ResultSet rs2 = s.executeQuery("SELECT mop.discount, mop.product_id, mop.is_promotion, ipv.unit_per_sku, ipv.liquid_in_ml, mop.product_id,  mop.rate_raw_cases, mop.rate_units, mop.total_units units_sold, ifnull(mopb.total_units,0) units_returned, mop.promotion_id FROM mobile_order_products mop join inventory_products_view ipv on mop.product_id = ipv.product_id left outer join mobile_order_products_backorder mopb on mop.id = mopb.id and mop.product_id = mopb.product_id where mop.id = "+OrderID);
				
				int LrbTypeID=0;
				ResultSet rs2 = s.executeQuery("SELECT mop.discount, mop.product_id, mop.is_promotion, ipv.unit_per_sku, ipv.liquid_in_ml, mop.product_id,  mop.rate_raw_cases, mop.rate_units, mop.total_units units_sold, mop.promotion_id, mop.hand_discount_rate, mop.hand_discount_id, ("+
							"select tab1.total_units from (select product_id, total_units, ifnull(promotion_id,0) promotion_id from mobile_order_products_backorder mopb where mopb.id = "+OrderID+") tab1 where tab1.product_id = mop.product_id and tab1.promotion_id = ifnull(mop.promotion_id,0)"+
						") units_returned,ipv.lrb_type_id FROM mobile_order_products mop join inventory_products_view ipv on mop.product_id = ipv.product_id where mop.id = "+OrderID);

				while(rs2.next()){
					
					String PromotionID = rs2.getString("promotion_id");
					
					int ProductID = rs2.getInt("product_id");
					double ProductDiscount = rs2.getDouble("discount");
					
					int UnitsPerSKU = rs2.getInt("unit_per_sku");
					long LiquidInMLPerUnit = rs2.getLong("liquid_in_ml");
					
					 LrbTypeID = rs2.getInt("lrb_type_id");
					
					int TotalUnits = rs2.getInt("units_sold") - rs2.getInt("units_returned");
					
					if (TotalUnits > 0){
						long LiquidinML = LiquidInMLPerUnit * TotalUnits;
						
						double RateRawCase = rs2.getDouble("rate_raw_cases");
						double RateUnit = rs2.getDouble("rate_units");
						
						long ConvertedUnits[] = Utilities.getRawCasesAndUnits(TotalUnits, UnitsPerSKU);
						
						long RawCases = ConvertedUnits[0];
						long Units = ConvertedUnits[1];

						// patch for hand discount
						double HandDiscountRate = rs2.getDouble("hand_discount_rate");
						long HandDiscountID = rs2.getLong("hand_discount_id");
						double HandDiscountAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((HandDiscountRate * RawCases) + ((HandDiscountRate/UnitsPerSKU) * Units)));
						String HandDiscountIDInsert = "null";
						if (HandDiscountID != 0){
							HandDiscountIDInsert = "" + HandDiscountID;
						}						
						// end patch						
						
						
						double AmountRawCases = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((RawCases * RateRawCase)));
						double AmountUnits = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((Units * RateUnit)));
						
						double TotalAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((AmountRawCases + AmountUnits)));
						
						double WHTaxAmount=0;
						if(LrbTypeID==2) {//for gur
							 WHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((TotalAmount * WHTaxRate / 100)));
						}else {
							WHTaxRate=0;
						}
						
						double NetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((TotalAmount + WHTaxAmount)));
						
						int isPromotion = rs2.getInt("is_promotion");
						
						if (isPromotion == 0){
							InvoiceAmount += TotalAmount;
							InvoiceWHTaxAmount += WHTaxAmount;
							InvoiceNetAmount += NetAmount;						
						}					
						
						s2.executeUpdate("insert into inventory_sales_invoices_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, discount, wh_tax_amount, net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id) values ("+
								SaleInvoiceID+", "+ProductID+", "+RawCases+", "+Units+", "+TotalUnits+", "+LiquidinML+", "+RateRawCase+", "+RateUnit+", "+AmountRawCases+", "+AmountUnits+", "+TotalAmount+", "+ProductDiscount+", "+WHTaxAmount+" ,"+NetAmount+", "+isPromotion+", "+PromotionID+","+HandDiscountRate+", "+HandDiscountAmount+", "+HandDiscountIDInsert+")  ");
					}
					
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
					
					
					if (Utilities.parseDouble(InoviceTotalAmountString) == 0){
						ds.rollback();
						s2.executeUpdate("update mobile_order set status_type_id=2, status_on=now() where id="+OrderID);
					}else{
						s2.executeUpdate("update inventory_sales_invoices set invoice_amount = "+InvoiceAmount+", sales_tax_amount  = "+SalesTaxAmount+", wh_tax_amount = "+InvoiceWHTaxAmount+", total_amount = "+InvoiceNetAmount+", fraction_adjustment = "+Utilities.getDisplayCurrencyFormatSimple(FractionAmount)+", net_amount = "+InoviceTotalAmountString+" where id = "+SaleInvoiceID);
						s2.executeUpdate("update mobile_order set status_type_id=2, status_on=now() where id="+OrderID);
					}
			}
			
			
			s.close();
			s2.close();
			ds.commit();
			success = true;
		}catch(Exception e){
			System.out.print(e);
			e.printStackTrace();
			try {
				ds.rollback();
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
		} finally{
			if (ds != null){
				try {
					ds.dropConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return success;
	}
	
	public static boolean splitOrder(long OrderID) {
		boolean success = false;
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			Statement s4 = ds.createStatement();
			Statement s5 = ds.createStatement();
			
			double SalesTaxRate = 0;
			double WHTaxRate = 0;
			long OrderSequence = 0;

			
			
			//ResultSet rs1 = s.executeQuery("select distributor_id from common_outlets_distributors_view where outlet_id in (select outlet_id from mobile_order_unedited where id = "+OrderID+")");
			ResultSet rs1 = s.executeQuery("select codv.distributor_id, (SELECT id FROM distributor_beat_plan_view where distributor_id = codv.distributor_id and outlet_id = codv.outlet_id and assigned_to in (select created_by from mobile_order_unedited where id = "+OrderID+") limit 1 ) pjp_id, (select snd_id from common_distributors where distributor_id = codv.distributor_id) snd_id, (select rsm_id from common_distributors where distributor_id = codv.distributor_id) rsm_id from common_outlets_distributors_view codv where codv.outlet_id in (select outlet_id from mobile_order_unedited where id = "+OrderID+") order by codv.distributor_id desc");
			
			
			while(rs1.next()){
				
				try{
					
				long PJP_ID = rs1.getLong(2);
				String SND_ID = rs1.getString("snd_id");
				String RSM_ID = rs1.getString("rsm_id");
				
				if (PJP_ID != 0){
				
					ds.startTransaction();
					
					long SplitOrderID = 0;
					double InvoiceAmount = 0;
					double InvoiceWHTaxAmount = 0;
					double InvoiceNetAmount = 0;
					List<Integer> ProductIDArray = new ArrayList<Integer>();
					List<Long> TotalUnitsArray = new ArrayList<Long>();
					
					OrderSequence++;
					ds.startTransaction();
					long DistributorID = rs1.getLong(1);
					long OutletID = 0;
					
					ResultSet rs = s2.executeQuery("select * from mobile_order_unedited where id = "+OrderID);
					if(rs.first()){
						
						SalesTaxRate = rs.getDouble("sales_tax_rate");
						WHTaxRate = rs.getDouble("wh_tax_rate");
						OutletID = rs.getLong("outlet_id");
						
						
						s3.executeUpdate("insert into mobile_order (mobile_order_no, outlet_id, distributor_id, region_id, created_on, created_by, sales_tax_rate, wh_tax_rate, uuid, platform, lat, lng, accuracy, mobile_timestamp, unedited_order_id, beat_plan_id, snd_id, rsm_id, sm_id, tdm_id, asm_id) values "+
						"("+OrderSequence+rs.getString("mobile_order_no")+", "+OutletID+", "+DistributorID+", (select region_id from common_distributors where distributor_id = "+DistributorID+"), now(), "+rs.getString("created_by")+", "+rs.getString("sales_tax_rate")+", "+rs.getString("wh_tax_rate")+", '"+rs.getString("uuid")+"', '"+rs.getString("platform")+"', "+rs.getString("lat")+", "+rs.getString("lng")+", "+rs.getString("accuracy")+", '"+rs.getString("mobile_timestamp")+"', "+OrderID+", "+PJP_ID+", "+SND_ID+", "+RSM_ID+",(SELECT if(sm_id = 0, null,sm_id) FROM distributor_beat_plan where id = "+PJP_ID+"), (SELECT if(tdm_id = 0, null,tdm_id) FROM distributor_beat_plan where id = "+PJP_ID+"), (SELECT if(asm_id = 0, null,asm_id) FROM distributor_beat_plan where id = "+PJP_ID+") )");
						
						
						ResultSet rs2 = s3.executeQuery("select LAST_INSERT_ID()");
						if(rs2.first()){
							SplitOrderID = rs2.getLong(1);
						}			
						
						//System.out.println(SplitOrderID);
					}
					
					
					ResultSet rs2 = s2.executeQuery("SELECT * FROM mobile_order_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = "+OrderID+" and moup.is_promotion = 0 and moup.is_processed = 0 and moup.product_id in (SELECT product_id FROM employee_product_groups_list where product_group_id in (select product_group_id from common_distributors where distributor_id = "+DistributorID+"))");
					while(rs2.next()){
						
						int ProductID = rs2.getInt("product_id");
						int TotalUnits = rs2.getInt("total_units");
						
						int RawCases = rs2.getInt("raw_cases");
						int Units = rs2.getInt("units");
						
						int UnitsPerSKU = rs2.getInt("unit_per_sku");
						long LiquidInMLPerUnit = rs2.getLong("liquid_in_ml");
						
						long LiquidinML = LiquidInMLPerUnit * TotalUnits;
						
						double RateRawCase = rs2.getDouble("rate_raw_cases");
						double RateUnit = rs2.getDouble("rate_units");
						
						// patch for hand discount
						double HandDiscountRate = rs2.getDouble("hand_discount_rate");
						long HandDiscountID = rs2.getLong("hand_discount_id");
						double HandDiscountAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((HandDiscountRate * RawCases) + ((HandDiscountRate/UnitsPerSKU) * Units)));
						String HandDiscountIDInsert = "null";
						if (HandDiscountID != 0){
							HandDiscountIDInsert = "" + HandDiscountID;
						}						
						// end patch
						
						double AmountRawCases = rs2.getDouble("amount_raw_cases");
						double AmountUnits = rs2.getDouble("amount_units");
	
						double TotalAmount = rs2.getDouble("total_amount");
						double WHTaxAmount = rs2.getDouble("wh_tax_amount");
						double NetAmount = rs2.getDouble("net_amount");
						
						InvoiceAmount += TotalAmount;
						InvoiceWHTaxAmount += WHTaxAmount;
						InvoiceNetAmount += NetAmount;
	
						String PromotionID = null;
						
						ProductIDArray.add(ProductID);
						TotalUnitsArray.add(TotalUnits * 1l);
						
						s3.executeUpdate("insert into mobile_order_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id) values ("+
								SplitOrderID+", "+ProductID+", "+RawCases+", "+Units+", "+TotalUnits+", "+LiquidinML+", "+RateRawCase+", "+RateUnit+", "+AmountRawCases+", "+AmountUnits+", "+TotalAmount+", "+WHTaxAmount+","+NetAmount+", 0, "+PromotionID+","+HandDiscountRate+", "+HandDiscountAmount+", "+HandDiscountIDInsert+")  ");
						
						s3.executeUpdate("update mobile_order_unedited_products set is_processed = 1 where id = "+OrderID+" and product_id = "+ProductID+" and is_promotion = 0");
						
					}
					
					if (ProductIDArray != null && ProductIDArray.size() > 0){
						
						PromotionItem PromotionProducts[] = Product.getPromotionItems(OutletID, ArrayUtils.toPrimitive(ProductIDArray.toArray(new Integer[ProductIDArray.size()])), ArrayUtils.toPrimitive(TotalUnitsArray.toArray(new Long[TotalUnitsArray.size()])));
						
						for (int i = 0; i < PromotionProducts.length; i++){
							
							long RawCasesAndUnits[] = Utilities.getRawCasesAndUnits(PromotionProducts[i].TOTAL_UNITS, PromotionProducts[i].UNIT_PER_SKU);
							
							long ProSAPCode = 0;
							int ProProductID = 0;
							double ProSellingPriceRawCase = 0;
							double ProSellingPriceUnit = 0;
							long ProLiquidInML = 0;
							
							int BrandID = 0;
							int SelectedBrandID = 0;
							ResultSet rs4 = s4.executeQuery("SELECT ipv.brand_id FROM mobile_order_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = "+OrderID+" and moup.promotion_id = "+PromotionProducts[i].PROMOTION_ID);
							if (rs4.first()){
								SelectedBrandID = rs4.getInt(1);
							}
							
							if (PromotionProducts[i].BRANDS.size() > 0){
								BrandID = PromotionProducts[i].BRANDS.get(0);
							}
							
							if (SelectedBrandID != 0){
								BrandID = SelectedBrandID;
							}
	
							
							if (BrandID != 0){
								
								Product PromotionProduct = new Product(1, PromotionProducts[i].PACKAGE_ID, BrandID);
								ProProductID = PromotionProduct.PRODUCT_ID;
								ProSAPCode = PromotionProduct.SAP_CODE;
								double rates[] = Product.getSellingPrice(PromotionProduct.SAP_CODE, OutletID);
								ProSellingPriceRawCase = rates[0];
								ProSellingPriceUnit = rates[1];
								ProLiquidInML = PromotionProduct.LIQUID_IN_ML;
								
								double AmountRawCases = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((RawCasesAndUnits[0] * ProSellingPriceRawCase)));
								double AmountUnits = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((RawCasesAndUnits[1] * ProSellingPriceUnit)));
								
								double TotalAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((AmountRawCases + AmountUnits)));
								double WHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((TotalAmount * WHTaxRate / 100)));
								double NetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((TotalAmount + WHTaxAmount)));
								
								
								s3.executeUpdate("replace into mobile_order_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, is_promotion, promotion_id) values ("+
										SplitOrderID+", "+ProProductID+", "+RawCasesAndUnits[0]+", "+RawCasesAndUnits[1]+", "+PromotionProducts[i].TOTAL_UNITS+", "+ProLiquidInML+", "+ProSellingPriceRawCase+", "+ProSellingPriceUnit+", "+AmountRawCases+", "+AmountUnits+", "+TotalAmount+", "+WHTaxAmount+" ,"+NetAmount+", 1, "+PromotionProducts[i].PROMOTION_ID+")  ");
								
								
								
								
							}
							
						}
						
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
					
					s5.executeUpdate("update mobile_order set invoice_amount = "+InvoiceAmount+", sales_tax_amount  = "+SalesTaxAmount+", wh_tax_amount = "+InvoiceWHTaxAmount+", total_amount = "+InvoiceNetAmount+", fraction_adjustment = "+Utilities.getDisplayCurrencyFormatSimple(FractionAmount)+", net_amount = "+InoviceTotalAmountString+" where id = "+SplitOrderID);
					s5.executeUpdate("update mobile_order_unedited set is_processed = 1 where id = "+OrderID);
					
					if (InvoiceAmount != 0){
						ds.commit();
						
						try{
							
							final long iOrderID = SplitOrderID;
							
							Thread smsthread = new Thread(){
							    public void run(){
							    	try {
										Utilities.sendSMSOrderBookering(iOrderID);
									} catch (IOException e) {
										System.out.println("Sales Posting (SMS Attempt Thread):");
										e.printStackTrace();
									}
							    }
							};

							smsthread.start();
							  
							
						}catch(Exception e){
							System.out.println("Sales Posting (SMS Attempt):");
							e.printStackTrace();
						}
						
					}else{
						ds.rollback();
					}
				}
				
				}catch(SQLException e){System.out.println("Split Order: OrderID " + OrderID+"\n"+e);}
				
			}
			
			
			
			s5.close();
			s4.close();
			s3.close();
			s2.close();
			s.close();
			//ds.commit();
			success = true;
		}catch(Exception e){
			System.out.println("Split Order: OrderID " + OrderID+"\n"+e);
			try {
				ds.rollback();
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
		} finally{
			if (ds != null){
				try {
					ds.dropConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return success;
	}
	
	public static boolean splitOrderSM(long OrderID) {
		
		
		boolean success = false;
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			Statement s4 = ds.createStatement();
			Statement s5 = ds.createStatement();
			
			double SalesTaxRate = 0;
			double WHTaxRate = 0;
			long OrderSequence = 0;

			
			
			//ResultSet rs1 = s.executeQuery("select distributor_id from common_outlets_distributors_view where outlet_id in (select outlet_id from mobile_order_unedited where id = "+OrderID+")");
			ResultSet rs1 = s.executeQuery("select codv.distributor_id, (SELECT id FROM distributor_beat_plan_view where distributor_id = codv.distributor_id and outlet_id = codv.outlet_id and asm_id in (select created_by from mobile_order_sm_unedited where id = "+OrderID+") limit 1 ) pjp_id, (select snd_id from common_distributors where distributor_id = codv.distributor_id) snd_id, (select rsm_id from common_distributors where distributor_id = codv.distributor_id) rsm_id from common_outlets_distributors_view codv where codv.outlet_id in (select outlet_id from mobile_order_sm_unedited where id = "+OrderID+") order by codv.distributor_id desc");
			
			
			while(rs1.next()){
				
				try{
					
				long PJP_ID = rs1.getLong(2);
				String SND_ID = rs1.getString("snd_id");
				String RSM_ID = rs1.getString("rsm_id");
				
				if (PJP_ID != 0){
				
					ds.startTransaction();
					
					long SplitOrderID = 0;
					double InvoiceAmount = 0;
					double InvoiceWHTaxAmount = 0;
					double InvoiceNetAmount = 0;
					List<Integer> ProductIDArray = new ArrayList<Integer>();
					List<Long> TotalUnitsArray = new ArrayList<Long>();
					
					OrderSequence++;
					ds.startTransaction();
					long DistributorID = rs1.getLong(1);
					long OutletID = 0;
					
					ResultSet rs = s2.executeQuery("select * from mobile_order_sm_unedited where id = "+OrderID);
					if(rs.first()){
						
						SalesTaxRate = rs.getDouble("sales_tax_rate");
						WHTaxRate = rs.getDouble("wh_tax_rate");
						OutletID = rs.getLong("outlet_id");
						
						
						s3.executeUpdate("insert into mobile_order_sm (mobile_order_no, outlet_id, distributor_id, region_id, created_on, created_by, sales_tax_rate, wh_tax_rate, uuid, platform, lat, lng, accuracy, mobile_timestamp, unedited_order_id, beat_plan_id, snd_id, rsm_id, sm_id, tdm_id, asm_id) values "+
						"("+OrderSequence+rs.getString("mobile_order_no")+", "+OutletID+", "+DistributorID+", (select region_id from common_distributors where distributor_id = "+DistributorID+"), now(), "+rs.getString("created_by")+", "+rs.getString("sales_tax_rate")+", "+rs.getString("wh_tax_rate")+", '"+rs.getString("uuid")+"', '"+rs.getString("platform")+"', "+rs.getString("lat")+", "+rs.getString("lng")+", "+rs.getString("accuracy")+", '"+rs.getString("mobile_timestamp")+"', "+OrderID+", "+PJP_ID+", "+SND_ID+", "+RSM_ID+",(SELECT if(sm_id = 0, null,sm_id) FROM distributor_beat_plan where id = "+PJP_ID+"), (SELECT if(tdm_id = 0, null,tdm_id) FROM distributor_beat_plan where id = "+PJP_ID+"), (SELECT if(asm_id = 0, null,asm_id) FROM distributor_beat_plan where id = "+PJP_ID+") )");
						
						
						ResultSet rs2 = s3.executeQuery("select LAST_INSERT_ID()");
						if(rs2.first()){
							SplitOrderID = rs2.getLong(1);
						}			
						
						
					}
					
					//System.out.println("SELECT * FROM mobile_order_sm_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = "+OrderID+" and moup.is_promotion = 0 and moup.is_processed = 0 and moup.product_id in (SELECT product_id FROM employee_product_groups_list where product_group_id in (select product_group_id from common_distributors where distributor_id = "+DistributorID+"))");
					ResultSet rs2 = s2.executeQuery("SELECT * FROM mobile_order_sm_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = "+OrderID+" and moup.is_promotion = 0 and moup.is_processed = 0 and moup.product_id in (SELECT product_id FROM employee_product_groups_list where product_group_id in (select product_group_id from common_distributors where distributor_id = "+DistributorID+"))");
					while(rs2.next()){
						
						int ProductID = rs2.getInt("product_id");
						int TotalUnits = rs2.getInt("total_units");
						
						int RawCases = rs2.getInt("raw_cases");
						int Units = rs2.getInt("units");
						
						int UnitsPerSKU = rs2.getInt("unit_per_sku");
						long LiquidInMLPerUnit = rs2.getLong("liquid_in_ml");
						
						long LiquidinML = LiquidInMLPerUnit * TotalUnits;
						
						double RateRawCase = rs2.getDouble("rate_raw_cases");
						double RateUnit = rs2.getDouble("rate_units");
						
						// patch for hand discount
						double HandDiscountRate = rs2.getDouble("hand_discount_rate");
						long HandDiscountID = rs2.getLong("hand_discount_id");
						double HandDiscountAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((HandDiscountRate * RawCases) + ((HandDiscountRate/UnitsPerSKU) * Units)));
						String HandDiscountIDInsert = "null";
						if (HandDiscountID != 0){
							HandDiscountIDInsert = "" + HandDiscountID;
						}						
						// end patch
						
						double AmountRawCases = rs2.getDouble("amount_raw_cases");
						double AmountUnits = rs2.getDouble("amount_units");
	
						double TotalAmount = rs2.getDouble("total_amount");
						double WHTaxAmount = rs2.getDouble("wh_tax_amount");
						double NetAmount = rs2.getDouble("net_amount");
						
						InvoiceAmount += TotalAmount;
						InvoiceWHTaxAmount += WHTaxAmount;
						InvoiceNetAmount += NetAmount;
	
						String PromotionID = null;
						
						ProductIDArray.add(ProductID);
						TotalUnitsArray.add(TotalUnits * 1l);
						
						s3.executeUpdate("insert into mobile_order_sm_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id) values ("+
								SplitOrderID+", "+ProductID+", "+RawCases+", "+Units+", "+TotalUnits+", "+LiquidinML+", "+RateRawCase+", "+RateUnit+", "+AmountRawCases+", "+AmountUnits+", "+TotalAmount+", "+WHTaxAmount+","+NetAmount+", 0, "+PromotionID+","+HandDiscountRate+", "+HandDiscountAmount+", "+HandDiscountIDInsert+")  ");
						
						s3.executeUpdate("update mobile_order_sm_unedited_products set is_processed = 1 where id = "+OrderID+" and product_id = "+ProductID+" and is_promotion = 0");
						//System.out.println("ABCCCC 6"+SplitOrderID);
					}
					
					if (ProductIDArray != null && ProductIDArray.size() > 0){
						
						PromotionItem PromotionProducts[] = Product.getPromotionItems(OutletID, ArrayUtils.toPrimitive(ProductIDArray.toArray(new Integer[ProductIDArray.size()])), ArrayUtils.toPrimitive(TotalUnitsArray.toArray(new Long[TotalUnitsArray.size()])));
						
						for (int i = 0; i < PromotionProducts.length; i++){
							
							long RawCasesAndUnits[] = Utilities.getRawCasesAndUnits(PromotionProducts[i].TOTAL_UNITS, PromotionProducts[i].UNIT_PER_SKU);
							
							long ProSAPCode = 0;
							int ProProductID = 0;
							double ProSellingPriceRawCase = 0;
							double ProSellingPriceUnit = 0;
							long ProLiquidInML = 0;
							
							int BrandID = 0;
							int SelectedBrandID = 0;
							ResultSet rs4 = s4.executeQuery("SELECT ipv.brand_id FROM mobile_order_sm_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = "+OrderID+" and moup.promotion_id = "+PromotionProducts[i].PROMOTION_ID);
							if (rs4.first()){
								SelectedBrandID = rs4.getInt(1);
							}
							
							if (PromotionProducts[i].BRANDS.size() > 0){
								BrandID = PromotionProducts[i].BRANDS.get(0);
							}
							
							if (SelectedBrandID != 0){
								BrandID = SelectedBrandID;
							}
	
							
							if (BrandID != 0){
								
								Product PromotionProduct = new Product(1, PromotionProducts[i].PACKAGE_ID, BrandID);
								ProProductID = PromotionProduct.PRODUCT_ID;
								ProSAPCode = PromotionProduct.SAP_CODE;
								double rates[] = Product.getSellingPrice(PromotionProduct.SAP_CODE, OutletID);
								ProSellingPriceRawCase = rates[0];
								ProSellingPriceUnit = rates[1];
								ProLiquidInML = PromotionProduct.LIQUID_IN_ML;
								
								double AmountRawCases = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((RawCasesAndUnits[0] * ProSellingPriceRawCase)));
								double AmountUnits = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((RawCasesAndUnits[1] * ProSellingPriceUnit)));
								
								double TotalAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((AmountRawCases + AmountUnits)));
								double WHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((TotalAmount * WHTaxRate / 100)));
								double NetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple((TotalAmount + WHTaxAmount)));
								
								
								s3.executeUpdate("replace into mobile_order_sm_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, is_promotion, promotion_id) values ("+
										SplitOrderID+", "+ProProductID+", "+RawCasesAndUnits[0]+", "+RawCasesAndUnits[1]+", "+PromotionProducts[i].TOTAL_UNITS+", "+ProLiquidInML+", "+ProSellingPriceRawCase+", "+ProSellingPriceUnit+", "+AmountRawCases+", "+AmountUnits+", "+TotalAmount+", "+WHTaxAmount+" ,"+NetAmount+", 1, "+PromotionProducts[i].PROMOTION_ID+")  ");
								
								
								//System.out.println("ABCCCC dasdas2222222s"+SplitOrderID);
								
							}
							
						}
						
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
					//System.out.println("ABCCCC dasdass Commit"+SplitOrderID);
					s5.executeUpdate("update mobile_order_sm set invoice_amount = "+InvoiceAmount+", sales_tax_amount  = "+SalesTaxAmount+", wh_tax_amount = "+InvoiceWHTaxAmount+", total_amount = "+InvoiceNetAmount+", fraction_adjustment = "+Utilities.getDisplayCurrencyFormatSimple(FractionAmount)+", net_amount = "+InoviceTotalAmountString+" where id = "+SplitOrderID);
					s5.executeUpdate("update mobile_order_sm_unedited set is_processed = 1 where id = "+OrderID);
					
					if (InvoiceAmount != 0){
						//System.out.println("ABCCCC dasdass Commit"+SplitOrderID);
						ds.commit();
						
						try{
							
							final long iOrderID = SplitOrderID;
							
							Thread smsthread = new Thread(){
							    public void run(){
							    	try {
										Utilities.sendSMSOrderBookering(iOrderID);
									} catch (IOException e) {
										System.out.println("Sales Posting SM (SMS Attempt Thread):");
										e.printStackTrace();
									}
							    }
							};

							smsthread.start();
							  
							
						}catch(Exception e){
							System.out.println("Sales Posting SM(SMS Attempt):");
							e.printStackTrace();
						}
						
					}else{
						ds.rollback();
					}
				}
				
				}catch(SQLException e){System.out.println("SM Split Order: OrderID " + OrderID+"\n"+e);}
				
			}
			
			
			
			s5.close();
			s4.close();
			s3.close();
			s2.close();
			s.close();
			//ds.commit();
			success = true;
		}catch(Exception e){
			System.out.println("SM Split Order: OrderID " + OrderID+"\n"+e);
			try {
				ds.rollback();
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
		} finally{
			if (ds != null){
				try {
					ds.dropConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return success;
	}
	
}
