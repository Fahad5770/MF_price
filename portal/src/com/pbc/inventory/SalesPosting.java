package com.pbc.inventory;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import com.pbc.util.Utilities;
import com.pbc.inventory.Product;
import com.pbc.inventory.PromotionItem;
import com.pbc.util.AlmoizFormulas;
import com.pbc.util.Datasource;
import com.pbc.util.FormulaUtills;

public class SalesPosting
{
 public static boolean post(final long InvoiceID, final long UserID) {
     boolean success = false;
     final Datasource ds = new Datasource();
     try {
         ds.createConnection();
         ds.startTransaction();
         final boolean isReturned = false;
         final Statement s = ds.createStatement();
         final Statement s2 = ds.createStatement();
         double SalesTaxRate = 0.0;
         double WHTaxRate = 0.0;
         boolean isValid = false;
         String CreatedOnDate = "";
         String DistributorID = "";
         String OrderID = "";
         final String OrderDate = "";
         String BookedBy = null;
         boolean ifProductExists = false;
         final ResultSet rs = s.executeQuery("select * from inventory_sales_invoices where id = " + InvoiceID);
         if (rs.first()) {
             isValid = true;
             SalesTaxRate = rs.getDouble("sales_tax_rate");
             WHTaxRate = rs.getDouble("wh_tax_rate");
             CreatedOnDate = rs.getString("created_on");
             DistributorID = rs.getString("distributor_id");
             OrderID = rs.getString("order_id");
             BookedBy = rs.getString("booked_by");
             //System.out.println("replace into inventory_sales_adjusted (id, uvid, order_id, created_on, created_by, outlet_id, distributor_id, region_id, booked_by, type_id, sales_tax_rate, wh_tax_rate, adjusted_on, adjusted_by, beat_plan_id, brand_discount_amount) values (" + InvoiceID + ", " + rs.getString("uvid") + ", " + rs.getString("order_id") + ", '" + rs.getString("created_on") + "', " + rs.getString("created_by") + ", " + rs.getString("outlet_id") + ", " + rs.getString("distributor_id") + ", " + rs.getString("region_id") + ", " + rs.getString("booked_by") + ", " + rs.getString("type_id") + "," + SalesTaxRate + ", " + WHTaxRate + ", now(), " + UserID + ", " + rs.getString("beat_plan_id") + ", "+rs.getDouble("brand_discount_amount")+" ) ");
             s2.executeUpdate("replace into inventory_sales_adjusted (id, uvid, order_id, created_on, created_by, outlet_id, distributor_id, region_id, booked_by, type_id, sales_tax_rate, wh_tax_rate, adjusted_on, adjusted_by, beat_plan_id, brand_discount_amount) values (" + InvoiceID + ", " + rs.getString("uvid") + ", " + rs.getString("order_id") + ", '" + rs.getString("created_on") + "', " + rs.getString("created_by") + ", " + rs.getString("outlet_id") + ", " + rs.getString("distributor_id") + ", " + rs.getString("region_id") + ", " + rs.getString("booked_by") + ", " + rs.getString("type_id") + "," + SalesTaxRate + ", " + WHTaxRate + ", now(), " + UserID + ", " + rs.getString("beat_plan_id") + ", "+rs.getDouble("brand_discount_amount")+" ) ");
             //System.out.println("update inventory_sales_adjusted isa set order_created_on_date = (select date(created_on) from mobile_order where id = " + rs.getString("order_id") + ") where isa.id = " + InvoiceID);
             s2.executeUpdate("update inventory_sales_adjusted isa set order_created_on_date = (select date(created_on) from mobile_order where id = " + rs.getString("order_id") + ") where isa.id = " + InvoiceID);
           //  System.out.println("update inventory_sales_adjusted isa set created_on_date = date('" + rs.getString("created_on") + "') where isa.id = " + InvoiceID);
             s2.executeUpdate("update inventory_sales_adjusted isa set created_on_date = date('" + rs.getString("created_on") + "') where isa.id = " + InvoiceID);
         }
         if (isValid) {
        	// System.out.println("delete from inventory_sales_adjusted_products where id = " + InvoiceID);
             s.executeUpdate("delete from inventory_sales_adjusted_products where id = " + InvoiceID);
             double InvoiceAmount = 0.0;
             double InvoiceWHTaxAmount = 0.0;
             double InvoiceNetAmount = 0.0;
            // System.out.println("SELECT isip.discount, isip.product_id, isip.is_promotion, ipv.unit_per_sku, ipv.unit_per_catron, ipv.liquid_in_ml, isip.product_id,  isip.rate_raw_cases, isip.rate_units, isip.total_units units_sold, ifnull((select tab1.total_units from (select product_id, total_units, ifnull(promotion_id,0) promotion_id from inventory_sales_dispatch_adjusted_products isdap where isdap.invoice_id = " + InvoiceID + ") tab1 where tab1.product_id = isip.product_id and tab1.promotion_id = ifnull(isip.promotion_id,0) " + "),0) units_returned, isip.promotion_id, isip.hand_discount_rate, isip.hand_discount_id, ipv.package_id, ipv.brand_id, ipv.lrb_type_id FROM inventory_sales_invoices_products  isip join inventory_products_view ipv on isip.product_id = ipv.product_id where isip.id = " + InvoiceID);
             final ResultSet rs2 = s.executeQuery("SELECT isip.discount, isip.product_id, isip.is_promotion, ipv.unit_per_sku, ipv.unit_per_catron, ipv.liquid_in_ml, isip.product_id,  isip.rate_raw_cases, isip.rate_units, isip.total_units units_sold, ifnull((select tab1.total_units from (select product_id, total_units, ifnull(promotion_id,0) promotion_id from inventory_sales_dispatch_adjusted_products isdap where isdap.invoice_id = " + InvoiceID + ") tab1 where tab1.product_id = isip.product_id and tab1.promotion_id = ifnull(isip.promotion_id,0) " + "),0) units_returned, isip.promotion_id, isip.hand_discount_rate, isip.hand_discount_id, ipv.package_id, ipv.brand_id, ipv.lrb_type_id FROM inventory_sales_invoices_products  isip join inventory_products_view ipv on isip.product_id = ipv.product_id where isip.id = " + InvoiceID);
             while (rs2.next()) {
                 final int LRBTypeID = rs2.getInt("lrb_type_id");
                 final int PackageID = rs2.getInt("package_id");
                 final int BrandID = rs2.getInt("brand_id");
                 final String PromotionID = rs2.getString("promotion_id");
                 final int ProductID = rs2.getInt("product_id");
                 final double ProductDiscount = rs2.getDouble("discount");
                 final int UnitsPerSKU = rs2.getInt("unit_per_sku");
                 final long LiquidInMLPerUnit = rs2.getLong("liquid_in_ml");
                 final int TotalUnits = rs2.getInt("units_sold") - rs2.getInt("units_returned");
                 if (TotalUnits > 0) {
                     final long LiquidinML = LiquidInMLPerUnit * TotalUnits;
                     final double RateRawCase = rs2.getDouble("rate_raw_cases");
                     final double RateUnit = rs2.getDouble("rate_units");
                     final long[] ConvertedUnits = Utilities.getRawCasesAndUnits((long)TotalUnits, UnitsPerSKU);
                     final long RawCases = ConvertedUnits[0];
                     final long unitPerCarton = rs2.getLong("unit_per_catron");
                     final long Units = ConvertedUnits[1];
                     final double HandDiscountRate = rs2.getDouble("hand_discount_rate");
                     final long HandDiscountID = rs2.getLong("hand_discount_id");
                     final double HandDiscountAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(HandDiscountRate * RawCases + HandDiscountRate / UnitsPerSKU * Units));
                     String HandDiscountIDInsert = "null";
                     if (HandDiscountID != 0L) {
                         HandDiscountIDInsert = new StringBuilder().append(HandDiscountID).toString();
                     }
                     final double AmountRawCases = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(RawCases * RateRawCase));
                     final double AmountUnits = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(Units * RateUnit));
                     final double TotalAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(AmountRawCases + AmountUnits));
                     double WHTaxAmount = 0.0;
                 //    if (LRBTypeID == 5 || LRBTypeID == 6) {
                         WHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(TotalAmount * WHTaxRate / 100.0));
//                     }
//                     else {
//                         WHTaxRate = 0.0;
//                     }
                     final double NetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(TotalAmount + WHTaxAmount));
                     final int isPromotion = rs2.getInt("is_promotion");
                     if (isPromotion == 0) {
                         InvoiceAmount += TotalAmount;
                         InvoiceWHTaxAmount += WHTaxAmount;
                         InvoiceNetAmount += NetAmount;
                     }
                     ifProductExists = true;
                   //  System.out.println("insert into inventory_sales_adjusted_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, discount, wh_tax_amount, net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id, cache_created_on_date, cache_distributor_id, cache_package_id, cache_brand_id, cache_order_id, cache_booked_by, cache_units_per_sku, cache_lrb_type_id) values (" + InvoiceID + ", " + ProductID + ", " + RawCases + ", " + Units + ", " + TotalUnits + ", " + LiquidinML + ", " + RateRawCase + ", " + RateUnit + ", " + AmountRawCases + ", " + AmountUnits + ", " + TotalAmount + ", " + ProductDiscount + ", " + WHTaxAmount + " ," + NetAmount + ", " + isPromotion + ", " + PromotionID + "," + HandDiscountRate + ", " + HandDiscountAmount + ", " + HandDiscountIDInsert + ", date('" + CreatedOnDate + "'), " + DistributorID + ", " + PackageID + ", " + BrandID + ", " + OrderID + ", " + BookedBy + "," + UnitsPerSKU + ", " + LRBTypeID + ")  ");
                     s2.executeUpdate("insert into inventory_sales_adjusted_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, discount, wh_tax_amount, net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id, cache_created_on_date, cache_distributor_id, cache_package_id, cache_brand_id, cache_order_id, cache_booked_by, cache_units_per_sku, cache_lrb_type_id) values (" + InvoiceID + ", " + ProductID + ", " + RawCases + ", " + Units + ", " + TotalUnits + ", " + LiquidinML + ", " + RateRawCase + ", " + RateUnit + ", " + AmountRawCases + ", " + AmountUnits + ", " + TotalAmount + ", " + ProductDiscount + ", " + WHTaxAmount + " ," + NetAmount + ", " + isPromotion + ", " + PromotionID + "," + HandDiscountRate + ", " + HandDiscountAmount + ", " + HandDiscountIDInsert + ", date('" + CreatedOnDate + "'), " + DistributorID + ", " + PackageID + ", " + BrandID + ", " + OrderID + ", " + BookedBy + "," + UnitsPerSKU + ", " + LRBTypeID + ")  ");

               
                 
                 }
                 InvoiceAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount));
                 InvoiceWHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceWHTaxAmount));
                 InvoiceNetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceNetAmount));
                 final double TotalAmountExSalesTax = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount / (SalesTaxRate + 100.0) * 100.0));
                 final double SalesTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount - TotalAmountExSalesTax));
                 String InoviceTotalAmountString = new StringBuilder(String.valueOf(InvoiceNetAmount)).toString();
                 if (InoviceTotalAmountString.indexOf(".") != -1) {
                     final double Fraction = Utilities.parseDouble(InoviceTotalAmountString.substring(InoviceTotalAmountString.indexOf("."), InoviceTotalAmountString.length()));
                     InoviceTotalAmountString = InoviceTotalAmountString.substring(0, InoviceTotalAmountString.indexOf("."));
                     if (Fraction != 0.0) {
                         InoviceTotalAmountString = new StringBuilder(String.valueOf(Utilities.parseInt(InoviceTotalAmountString) + 1)).toString();
                     }
                 }
                 final double FractionAmount = Utilities.parseDouble(InoviceTotalAmountString) - InvoiceNetAmount;
               //  System.out.println("update inventory_sales_adjusted set invoice_amount = " + InvoiceAmount + ", sales_tax_amount  = " + SalesTaxAmount + ", wh_tax_amount = " + InvoiceWHTaxAmount + ", total_amount = " + InvoiceNetAmount + ", fraction_adjustment = " + Utilities.getDisplayCurrencyFormatSimple(FractionAmount) + ", net_amount = " + InoviceTotalAmountString + " where id = " + InvoiceID);
                s2.executeUpdate("update inventory_sales_adjusted set invoice_amount = " + InvoiceAmount + ", sales_tax_amount  = " + SalesTaxAmount + ", wh_tax_amount = " + InvoiceWHTaxAmount + ", total_amount = " + InvoiceNetAmount + ", fraction_adjustment = " + Utilities.getDisplayCurrencyFormatSimple(FractionAmount) + ", net_amount = " + InoviceTotalAmountString + " where id = " + InvoiceID);
               //  System.out.println("update inventory_sales_adjusted_products isa set cache_order_created_on_date = (select date(created_on) from mobile_order where id = " + OrderID + ") where isa.id = " + InvoiceID);
                s2.executeUpdate("update inventory_sales_adjusted_products isa set cache_order_created_on_date = (select date(created_on) from mobile_order where id = " + OrderID + ") where isa.id = " + InvoiceID);
             }
         }
         s.close();
         s2.close();
         ds.commit();
         success = true;
     }
     catch (Exception e) {
         System.out.print(e);
         try {
             ds.rollback();
         }
         catch (SQLException e2) {
             e2.printStackTrace();
         }
         if (ds != null) {
             try {
                 ds.dropConnection();
             }
             catch (SQLException e3) {
                 e3.printStackTrace();
             }
             return success;
         }
         return success;
     }
     finally {
         if (ds != null) {
             try {
                 ds.dropConnection();
             }
             catch (SQLException e3) {
                 e3.printStackTrace();
             }
         }
     }
     if (ds != null) {
         try {
             ds.dropConnection();
         }
         catch (SQLException e3) {
             e3.printStackTrace();
         }
     }
     return success;
 }
 
 public static boolean postOrder2Invoice(final long OrderID, final long UserID, final long UVID) {
     boolean success = false;
     final Datasource ds = new Datasource();
     try {
         ds.createConnection();
         ds.startTransaction();
         final boolean isReturned = false;
         final Statement s = ds.createStatement();
         final Statement s2 = ds.createStatement();
         double SalesTaxRate = 0.0;
         double WHTaxRate = 0.0;
         boolean isValid = false;
         long outletId=0, orderBooker = 0;
         final ResultSet rs = s.executeQuery("select * from mobile_order where id = " + OrderID);
         if (rs.first()) {
        	 outletId=rs.getLong("outlet_id");
        	 orderBooker=rs.getLong("created_by");
             isValid = true;
             SalesTaxRate = rs.getDouble("sales_tax_rate");
             WHTaxRate = rs.getDouble("wh_tax_rate");
             s2.executeUpdate("delete from inventory_sales_invoices where order_id = " + OrderID);
            System.out.println("insert into inventory_sales_invoices (uvid, order_id, created_on, created_by, outlet_id, distributor_id, region_id, booked_by, type_id, sales_tax_rate, wh_tax_rate, beat_plan_id, brand_discount_amount) values (" + UVID + ", " + OrderID + ", now(), " + UserID + ", " + rs.getString("outlet_id") + ", " + rs.getString("distributor_id") + ", " + rs.getString("region_id") + ", " + rs.getString("created_by") + ", 3," + SalesTaxRate + ", " + WHTaxRate + "," + rs.getString("beat_plan_id") + ", "+rs.getDouble("brand_discount_amount") + ")");
            s2.executeUpdate("insert into inventory_sales_invoices (uvid, order_id, created_on, created_by, outlet_id, distributor_id, region_id, booked_by, type_id, sales_tax_rate, wh_tax_rate, beat_plan_id, brand_discount_amount) values (" + UVID + ", " + OrderID + ", now(), " + UserID + ", " + rs.getString("outlet_id") + ", " + rs.getString("distributor_id") + ", " + rs.getString("region_id") + ", " + rs.getString("created_by") + ", 3," + SalesTaxRate + ", " + WHTaxRate + "," + rs.getString("beat_plan_id") + ", "+rs.getDouble("brand_discount_amount") + ")");
         }
         if (isValid) {
             long SaleInvoiceID = 0L;
             final ResultSet rs2 = s2.executeQuery("select LAST_INSERT_ID()");
             if (rs2.first()) {
                 SaleInvoiceID = rs2.getLong(1);
             }
             s.executeUpdate("delete from inventory_sales_invoices_products where id = " + SaleInvoiceID);
             
             int is_filer=0, is_register=0;
             ResultSet rsOutletStatus = s.executeQuery("select is_filer,is_register from common_outlets where id="+outletId);
             if(rsOutletStatus.first()) {
            	 is_filer=rsOutletStatus.getInt("is_filer");
            	 is_register=rsOutletStatus.getInt("is_register");
             }
             
             double InvoiceAmount = 0.0;
             double InvoiceWHTaxAmount = 0.0;
             double InvoiceSalesTaxAmount = 0.0;
             double InvoiceNetAmount = 0.0;
             final double InvoicePromotionAmount = 0.0;
             int LrbTypeID = 0;
             final ResultSet rs3 = s.executeQuery("SELECT mop.discount, mop.wh_tax_amount, mop.sales_tax_amount, mop.price_discount , mop.rate_raw_cases, mop.product_id, mop.is_promotion, ipv.unit_per_sku, ipv.liquid_in_ml, mop.product_id,  mop.rate_raw_cases, mop.rate_units, mop.total_units units_sold, mop.promotion_id, mop.hand_discount_rate, mop.hand_discount_id, (select tab1.total_units from (select product_id, total_units, ifnull(promotion_id,0) promotion_id from mobile_order_products_backorder mopb where mopb.id = " + OrderID + ") tab1 where tab1.product_id = mop.product_id and tab1.promotion_id = ifnull(mop.promotion_id,0)" + ") units_returned,ipv.lrb_type_id FROM mobile_order_products mop join inventory_products_view ipv on mop.product_id = ipv.product_id where mop.id = " + OrderID);
             while (rs3.next()) {
                 final String PromotionID = rs3.getString("promotion_id");
                 final int ProductID = rs3.getInt("product_id");
                 final double Discount = rs3.getDouble("discount");
                 final double ProductDiscount = rs3.getDouble("price_discount");
                 final int UnitsPerSKU = rs3.getInt("unit_per_sku");
                 final long LiquidInMLPerUnit = rs3.getLong("liquid_in_ml");
                 LrbTypeID = rs3.getInt("lrb_type_id");
                 final int TotalUnits = rs3.getInt("units_sold") - rs3.getInt("units_returned");
                 if (TotalUnits > 0) {
                     final long LiquidinML = LiquidInMLPerUnit * TotalUnits;
                     final double RateRawCase = rs3.getDouble("rate_raw_cases");
                     final double RateUnit = rs3.getDouble("rate_units");
                     final long[] ConvertedUnits = Utilities.getRawCasesAndUnits((long)TotalUnits, UnitsPerSKU);
                     final long RawCases = ConvertedUnits[0];
                     final long Units = ConvertedUnits[1];
                     final double HandDiscountRate = rs3.getDouble("hand_discount_rate");
                     final long HandDiscountID = rs3.getLong("hand_discount_id");
                     double saleTax = rs3.getDouble("sales_tax_amount");
                     double wh_tax = rs3.getDouble("wh_tax_amount");
                     final double HandDiscountAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(HandDiscountRate * RawCases + HandDiscountRate / UnitsPerSKU * Units));
                     String HandDiscountIDInsert = "null";
                     if (HandDiscountID != 0L) {
                         HandDiscountIDInsert = new StringBuilder().append(HandDiscountID).toString();
                     }
                     final double AmountRawCases = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(RawCases * RateRawCase));
                     final double AmountUnits = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(Units * RateUnit));
                     final double TotalAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(AmountRawCases + AmountUnits));
                
                     
                     System.out.println("Invoicing");
                     
                 
                   
                     /******************* Tax Calculation *********************/
                     
                    // HashMap<String, Double> ProductTax = AlmoizFormulas.ProductsTax(ProductID, outletId);
                    // System.out.println("Units "+ rs3.getInt("units_sold"));
                 //    saleTax = ProductTax.get("income_tax") * (double)  RawCases;
                  //   wh_tax =  ProductTax.get("wh_tax") * (double)  RawCases;
                  
                     /******************* Tax Calculation *********************/
                   
                     
                    
                     
                    
                     final double NetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(TotalAmount +  wh_tax + saleTax));
                     final int isPromotion = rs3.getInt("is_promotion");
                     if (isPromotion == 0) {
                         InvoiceAmount += TotalAmount;
                         InvoiceWHTaxAmount += wh_tax;
                         InvoiceSalesTaxAmount += saleTax;
                         InvoiceNetAmount += NetAmount;
                     }
                     System.out.println("insert into inventory_sales_invoices_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, price_discount, discount, wh_tax_amount,sales_tax_amount,net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id, salesTax) values (" + SaleInvoiceID + ", " + ProductID + ", " + RawCases + ", " + Units + ", " + TotalUnits + ", " + LiquidinML + ", " + RateRawCase + ", " + RateUnit + ", " + AmountRawCases + ", " + AmountUnits + ", " + TotalAmount + ", " + ProductDiscount + ", "+Discount+", " + wh_tax + " ," + saleTax + "," + NetAmount + ", " + isPromotion + ", " + PromotionID + "," + HandDiscountRate + ", " + HandDiscountAmount + ", " + HandDiscountIDInsert + ", "+saleTax+")  ");
                     s2.executeUpdate("insert into inventory_sales_invoices_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, price_discount, discount, wh_tax_amount,sales_tax_amount,net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id, salesTax) values (" + SaleInvoiceID + ", " + ProductID + ", " + RawCases + ", " + Units + ", " + TotalUnits + ", " + LiquidinML + ", " + RateRawCase + ", " + RateUnit + ", " + AmountRawCases + ", " + AmountUnits + ", " + TotalAmount + ", " + ProductDiscount + ", "+Discount+", " + wh_tax + " ," + saleTax + "," + NetAmount + ", " + isPromotion + ", " + PromotionID + "," + HandDiscountRate + ", " + HandDiscountAmount + ", " + HandDiscountIDInsert + ", "+saleTax+")  ");
                 }
             }
             InvoiceAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount));
             InvoiceWHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceWHTaxAmount));
             InvoiceSalesTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceSalesTaxAmount));
             InvoiceNetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceNetAmount));
             System.out.println("InvoiceNetAmount : "+InvoiceNetAmount);
         //    final double TotalAmountExSalesTax = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount / (SalesTaxRate + 100.0) * 100.0));
          //   final double SalesTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount - 0));
             String InoviceTotalAmountString = new StringBuilder(String.valueOf(InvoiceNetAmount)).toString();
             if (InoviceTotalAmountString.indexOf(".") != -1) {
                 final double Fraction = Utilities.parseDouble(InoviceTotalAmountString.substring(InoviceTotalAmountString.indexOf("."), InoviceTotalAmountString.length()));
                 InoviceTotalAmountString = InoviceTotalAmountString.substring(0, InoviceTotalAmountString.indexOf("."));
                 if (Fraction != 0.0) {
                     InoviceTotalAmountString = new StringBuilder(String.valueOf(Utilities.parseInt(InoviceTotalAmountString) + 1)).toString();
                 }
             }
             final double FractionAmount = Utilities.parseDouble(InoviceTotalAmountString) - InvoiceNetAmount;
             System.out.println("InoviceTotalAmountString : "+InoviceTotalAmountString);
             if (Utilities.parseDouble(InoviceTotalAmountString) == 0.0) {
                 ds.rollback();
                 s2.executeUpdate("update mobile_order set status_type_id=2, status_on=now() where id=" + OrderID);
             }
             else {
                 s2.executeUpdate("update inventory_sales_invoices set invoice_amount = " + InvoiceAmount + ", sales_tax_amount  = " + InvoiceSalesTaxAmount + ", wh_tax_amount = " + InvoiceWHTaxAmount + ", total_amount = " + InvoiceNetAmount + ", fraction_adjustment = " + Utilities.getDisplayCurrencyFormatSimple(FractionAmount) + ", net_amount = " + InoviceTotalAmountString + " where id = " + SaleInvoiceID);
                 s2.executeUpdate("update mobile_order set status_type_id=2, status_on=now() where id=" + OrderID);
             }
         }
         s.close();
         s2.close();
         ds.commit();
         success = true;
     }
     catch (Exception e) {
         System.out.print(e);
         e.printStackTrace();
         try {
             ds.rollback();
         }
         catch (SQLException e2) {
             e2.printStackTrace();
         }
         if (ds != null) {
             try {
                 ds.dropConnection();
             }
             catch (SQLException e3) {
                 e3.printStackTrace();
             }
             return success;
         }
         return success;
     }
     finally {
         if (ds != null) {
             try {
                 ds.dropConnection();
             }
             catch (SQLException e3) {
                 e3.printStackTrace();
             }
         }
     }
     if (ds != null) {
         try {
             ds.dropConnection();
         }
         catch (SQLException e3) {
             e3.printStackTrace();
         }
     }
     return success;
 }
 
 public static boolean splitOrder_2(final long OrderID) {

     boolean success = false;
     final Datasource ds = new Datasource();
     try {
         ds.createConnection();
         final Statement s = ds.createStatement();
         final Statement s2 = ds.createStatement();
         final Statement s3 = ds.createStatement();
         final Statement s4 = ds.createStatement();
         final Statement s5 = ds.createStatement();
       
         long OrderSequence = 0L;
         System.out.println("select codv.distributor_id, (SELECT id FROM distributor_beat_plan_view where distributor_id = codv.distributor_id and outlet_id = codv.outlet_id and assigned_to in (select created_by from mobile_order_unedited where id = " + OrderID + ") limit 1 ) pjp_id, (select snd_id from common_distributors where distributor_id = codv.distributor_id) snd_id, (select rsm_id from common_distributors where distributor_id = codv.distributor_id) rsm_id from common_outlets_distributors_view codv where codv.outlet_id in (select outlet_id from mobile_order_unedited where id = " + OrderID + ") order by codv.distributor_id desc");
         final ResultSet rs1 = s.executeQuery("select codv.distributor_id, (SELECT id FROM distributor_beat_plan_view where distributor_id = codv.distributor_id and outlet_id = codv.outlet_id and assigned_to in (select created_by from mobile_order_unedited where id = " + OrderID + ") limit 1 ) pjp_id, (select snd_id from common_distributors where distributor_id = codv.distributor_id) snd_id, (select rsm_id from common_distributors where distributor_id = codv.distributor_id) rsm_id from common_outlets_distributors_view codv where codv.outlet_id in (select outlet_id from mobile_order_unedited where id = " + OrderID + ") order by codv.distributor_id desc");
         while (rs1.next()) {
             try {
                 final long PJP_ID = rs1.getLong(2);
                 final String SND_ID = rs1.getString("snd_id");
                 final String RSM_ID = rs1.getString("rsm_id");
                 if (PJP_ID == 0L) {
                     continue;
                 }
                 ds.startTransaction();
                 long SplitOrderID = 0L;
                 double InvoiceAmount = 0.0;
                 double InvoiceWHTaxAmount = 0.0;
                 double InvoiceSalesTaxAmount = 0.0;
                 double InvoiceNetAmount = 0.0;
                 final List<Integer> ProductIDArray = new ArrayList<Integer>();
                 final List<Long> TotalUnitsArray = new ArrayList<Long>();
                 ++OrderSequence;
                 ds.startTransaction();
                 final long DistributorID = rs1.getLong(1);
                 long OutletID = 0L;
                System.out.println("select * from mobile_order_unedited where id = " + OrderID);
                 final ResultSet rs2 = s2.executeQuery("select * from mobile_order_unedited where id = " + OrderID);
                 if (rs2.first()) {
                	 InvoiceSalesTaxAmount = rs2.getDouble("sales_tax_amount");
                     InvoiceWHTaxAmount = rs2.getDouble("wh_tax_amount");
                     OutletID = rs2.getLong("outlet_id");
                   System.out.println("insert into mobile_order (app_version,price_discount, mobile_order_no, outlet_id, distributor_id, region_id, created_on, created_by,brand_discount_amount, sales_tax_rate, wh_tax_rate, uuid, platform, lat, lng, accuracy, mobile_timestamp, unedited_order_id, beat_plan_id, snd_id, rsm_id, sm_id, tdm_id, asm_id, is_spot_sale) values ('"+rs2.getString("app_version")+"',"+rs2.getString("price_discount") + ", "+ OrderSequence + rs2.getString("mobile_order_no") + ", " + OutletID + ", " + DistributorID + ", (select region_id from common_distributors where distributor_id = " + DistributorID + "), now(), " + rs2.getString("created_by") + ", "+rs2.getDouble("brand_discount_amount")+" ," + rs2.getString("sales_tax_rate") + ", " + rs2.getString("wh_tax_rate") + ", '" + rs2.getString("uuid") + "', '" + rs2.getString("platform") + "', " + rs2.getString("lat") + ", " + rs2.getString("lng") + ", " + rs2.getString("accuracy") + ", '" + rs2.getString("mobile_timestamp") + "', " + OrderID + ", " + PJP_ID + ", " + SND_ID + ", " + RSM_ID + ",(SELECT if(sm_id = 0, null,sm_id) FROM distributor_beat_plan where id = " + PJP_ID + "), (SELECT if(tdm_id = 0, null,tdm_id) FROM distributor_beat_plan where id = " + PJP_ID + "), (SELECT if(asm_id = 0, null,asm_id) FROM distributor_beat_plan where id = " + PJP_ID + "),'" + rs2.getString("is_spot_sale") + "' )");
                     s3.executeUpdate("insert into mobile_order (app_version,price_discount, mobile_order_no, outlet_id, distributor_id, region_id, created_on, created_by,brand_discount_amount, sales_tax_rate, wh_tax_rate, uuid, platform, lat, lng, accuracy, mobile_timestamp, unedited_order_id, beat_plan_id, snd_id, rsm_id, sm_id, tdm_id, asm_id, is_spot_sale) values ('"+rs2.getString("app_version")+"',"+rs2.getString("price_discount") + ", "+ OrderSequence + rs2.getString("mobile_order_no") + ", " + OutletID + ", " + DistributorID + ", (select region_id from common_distributors where distributor_id = " + DistributorID + "), now(), " + rs2.getString("created_by") + ", "+rs2.getDouble("brand_discount_amount")+" ," + rs2.getString("sales_tax_rate") + ", " + rs2.getString("wh_tax_rate") + ", '" + rs2.getString("uuid") + "', '" + rs2.getString("platform") + "', " + rs2.getString("lat") + ", " + rs2.getString("lng") + ", " + rs2.getString("accuracy") + ", '" + rs2.getString("mobile_timestamp") + "', " + OrderID + ", " + PJP_ID + ", " + SND_ID + ", " + RSM_ID + ",(SELECT if(sm_id = 0, null,sm_id) FROM distributor_beat_plan where id = " + PJP_ID + "), (SELECT if(tdm_id = 0, null,tdm_id) FROM distributor_beat_plan where id = " + PJP_ID + "), (SELECT if(asm_id = 0, null,asm_id) FROM distributor_beat_plan where id = " + PJP_ID + "),'" + rs2.getString("is_spot_sale") + "' )");
                     final ResultSet rs3 = s3.executeQuery("select LAST_INSERT_ID()");
                     if (rs3.first()) {
                         SplitOrderID = rs3.getLong(1);
                     }
                 }
                System.out.println("SELECT * FROM mobile_order_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = " + OrderID + " and moup.is_promotion = 0 and moup.is_processed = 0 and moup.product_id in (SELECT product_id FROM employee_product_groups_list where product_group_id in (select product_group_id from common_distributors where distributor_id = " + DistributorID + "))");
                 final ResultSet rs3 = s2.executeQuery("SELECT * FROM mobile_order_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = " + OrderID + " and moup.is_promotion = 0 and moup.is_processed = 0 and moup.product_id in (SELECT product_id FROM employee_product_groups_list where product_group_id in (select product_group_id from common_distributors where distributor_id = " + DistributorID + "))");
                 while (rs3.next()) {
                     final int ProductID = rs3.getInt("product_id");
                     final int TotalUnits = rs3.getInt("total_units");
                     final int RawCases = rs3.getInt("raw_cases");
                     final int Units = rs3.getInt("units");
                     final int UnitsPerSKU = rs3.getInt("unit_per_sku");
                     final long LiquidInMLPerUnit = rs3.getLong("liquid_in_ml");
                     final long LiquidinML = LiquidInMLPerUnit * TotalUnits;
                     final double RateRawCase = rs3.getDouble("rate_raw_cases");
                     final double RateUnit = rs3.getDouble("rate_units");
                     final double HandDiscountRate = rs3.getDouble("hand_discount_rate");
                     final long HandDiscountID = rs3.getLong("hand_discount_id");
                     final int price_discount = rs3.getInt("price_discount");
                     final double HandDiscountAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(HandDiscountRate * RawCases + HandDiscountRate / UnitsPerSKU * Units));
                     String HandDiscountIDInsert = "null";
                     if (HandDiscountID != 0L) {
                         HandDiscountIDInsert = new StringBuilder().append(HandDiscountID).toString();
                     }
                     final double AmountRawCases = rs3.getDouble("amount_raw_cases");
                     final double AmountUnits = rs3.getDouble("amount_units");
                     final double TotalAmount = rs3.getDouble("total_amount");
                     final double WHTaxAmount = rs3.getDouble("wh_tax_amount");
                     final double SalesTaxAmount = rs3.getDouble("sales_tax_amount");
                     final double NetAmount = rs3.getDouble("net_amount");
                     final double TO_discount = rs3.getDouble("TO_discount");
                     InvoiceAmount += TotalAmount;
                //     InvoiceWHTaxAmount += WHTaxAmount;
                     InvoiceNetAmount += NetAmount;
                     final String PromotionID = null;
                     ProductIDArray.add(ProductID);
                     TotalUnitsArray.add(TotalUnits * 1L);
                     s3.executeUpdate("insert into mobile_order_products (id, product_id, price_discount, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, sales_tax_amount, net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id,TO_discount) values (" + SplitOrderID + ", " + ProductID + ", " + price_discount + ", " + RawCases + ", " + Units + ", " + TotalUnits + ", " + LiquidinML + ", " + RateRawCase + ", " + RateUnit + ", " + AmountRawCases + ", " + AmountUnits + ", " + TotalAmount + ", " + WHTaxAmount + "," + SalesTaxAmount + "," + NetAmount + ", 0, " + PromotionID + "," + HandDiscountRate + ", " + HandDiscountAmount + ", " + HandDiscountIDInsert + ","+TO_discount+")");
                     s3.executeUpdate("update mobile_order_unedited_products set is_processed = 1 where id = " + OrderID + " and product_id = " + ProductID + " and is_promotion = 0");
                 }
                 
                 System.out.println("select * from mobile_order_unedited_brand_discount where id="+OrderID);
                 final ResultSet rsBrandDiscount = s2.executeQuery("select * from mobile_order_unedited_brand_discount where id="+OrderID);
                 while(rsBrandDiscount.next()) {
                	// System.out.println("INSERT INTO `pep`.`mobile_order_brand_discount`(`id`,`discount_brand_id`,`brand_id`,`brand_discount_amount`,`cartons`)VALUES("+SplitOrderID+", "+rsBrandDiscount.getLong("discount_brand_id")+","+rsBrandDiscount.getInt("brand_id")+","+rsBrandDiscount.getDouble("brand_discount_amount")+", "+rsBrandDiscount.getInt("cartons")+")");
						s3.executeUpdate("INSERT INTO `pep`.`mobile_order_brand_discount`(`id`,`discount_brand_id`,`brand_id`,`brand_discount_amount`,`cartons`)VALUES("+SplitOrderID+", "+rsBrandDiscount.getLong("discount_brand_id")+","+rsBrandDiscount.getInt("brand_id")+","+rsBrandDiscount.getDouble("brand_discount_amount")+", "+rsBrandDiscount.getInt("cartons")+")");
                 }
                 
                 System.out.println("select * from mobile_order_unedited_brand_discount_products where id="+OrderID);
                 final ResultSet rsBrandDiscountProducts = s2.executeQuery("select * from mobile_order_unedited_brand_discount_products where id="+OrderID);
                 while(rsBrandDiscountProducts.next()) {
                	 System.out.println("INSERT INTO `pep`.`mobile_order_brand_discount_products`(`id`,`discount_brand_id`,`brand_id`,`product_id`,`quantity`,`cartons`) VALUES("+SplitOrderID+", "+ rsBrandDiscountProducts.getLong("discount_brand_id") +", "+rsBrandDiscountProducts.getInt("brand_id")+", "+rsBrandDiscountProducts.getInt("product_id")+", "+rsBrandDiscountProducts.getInt("quantity")+" ,"+ rsBrandDiscountProducts.getInt("cartons") +")");
                	 s3.executeUpdate("INSERT INTO `pep`.`mobile_order_brand_discount_products`(`id`,`discount_brand_id`,`brand_id`,`product_id`,`quantity`,`cartons`) VALUES("+SplitOrderID+", "+ rsBrandDiscountProducts.getLong("discount_brand_id") +", "+rsBrandDiscountProducts.getInt("brand_id")+", "+rsBrandDiscountProducts.getInt("product_id")+", "+rsBrandDiscountProducts.getInt("quantity")+" ,"+ rsBrandDiscountProducts.getInt("cartons") +")");
                 }
                 
                 
                 
                 if(ProductIDArray != null) {
                 if ( ProductIDArray.size() > 0) {
                     final PromotionItem[] PromotionProducts = Product.getPromotionItems(OutletID, ArrayUtils.toPrimitive((Integer[])ProductIDArray.toArray(new Integer[ProductIDArray.size()])), ArrayUtils.toPrimitive((Long[])TotalUnitsArray.toArray(new Long[TotalUnitsArray.size()])));
                     for (int i = 0; i < PromotionProducts.length; ++i) {
                         final long[] RawCasesAndUnits = Utilities.getRawCasesAndUnits(PromotionProducts[i].TOTAL_UNITS, PromotionProducts[i].UNIT_PER_SKU);
                         long ProSAPCode = 0L;
                         int ProProductID = 0;
                         double ProSellingPriceRawCase = 0.0;
                         double ProSellingPriceUnit = 0.0;
                         long ProLiquidInML = 0L;
                         int BrandID = 0;
                         int SelectedBrandID = 0;
                         System.out.println("SELECT ipv.brand_id FROM mobile_order_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = " + OrderID + " and moup.promotion_id = " + PromotionProducts[i].PROMOTION_ID);
                         final ResultSet rs4 = s4.executeQuery("SELECT ipv.brand_id FROM mobile_order_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = " + OrderID + " and moup.promotion_id = " + PromotionProducts[i].PROMOTION_ID);
                         if (rs4.first()) {
                             SelectedBrandID = rs4.getInt(1);
                         }
                         if (PromotionProducts[i].BRANDS.size() > 0) {
                             BrandID = PromotionProducts[i].BRANDS.get(0);
                         }
                         if (SelectedBrandID != 0) {
                             BrandID = SelectedBrandID;
                         }
                         System.out.println("here 1 ....");
                         if (BrandID != 0) {
                             final Product PromotionProduct = new Product(1, PromotionProducts[i].PACKAGE_ID, BrandID);
                             ProProductID = PromotionProduct.PRODUCT_ID;
                             ProSAPCode = PromotionProduct.SAP_CODE;
                             final double[] rates = Product.getSellingPrice(PromotionProduct.SAP_CODE, OutletID);
                             ProSellingPriceRawCase = rates[0];
                             ProSellingPriceUnit = rates[1];
                             ProLiquidInML = PromotionProduct.LIQUID_IN_ML;
                             System.out.println("here 2 ....");
                             final double AmountRawCases2 = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(RawCasesAndUnits[0] * ProSellingPriceRawCase));
                             final double AmountUnits2 = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(RawCasesAndUnits[1] * ProSellingPriceUnit));
                             final double TotalAmount2 = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(AmountRawCases2 + AmountUnits2));
                             //final double WHTaxAmount2 = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(TotalAmount2 * WHTaxRate / 100.0));
                             System.out.println("here 3 ....");
                             
                             final double WHTaxAmount =0;
                             final double SalesTaxAmount = 0;
                             System.out.println("here 4 ....");
                             final double NetAmount2 = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(TotalAmount2 + WHTaxAmount + SalesTaxAmount));
                             System.out.println("replace into mobile_order_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, sales_tax_amount, net_amount, is_promotion, promotion_id) values (" + SplitOrderID + ", " + ProProductID + ", " + RawCasesAndUnits[0] + ", " + RawCasesAndUnits[1] + ", " + PromotionProducts[i].TOTAL_UNITS + ", " + ProLiquidInML + ", " + ProSellingPriceRawCase + ", " + ProSellingPriceUnit + ", " + AmountRawCases2 + ", " + AmountUnits2 + ", " + TotalAmount2 + ", " + WHTaxAmount + "," + SalesTaxAmount + " ," + NetAmount2 + ", 1, " + PromotionProducts[i].PROMOTION_ID + ")  ");
                             s4.executeUpdate("replace into mobile_order_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, sales_tax_amount, net_amount, is_promotion, promotion_id) values (" + SplitOrderID + ", " + ProProductID + ", " + RawCasesAndUnits[0] + ", " + RawCasesAndUnits[1] + ", " + PromotionProducts[i].TOTAL_UNITS + ", " + ProLiquidInML + ", " + ProSellingPriceRawCase + ", " + ProSellingPriceUnit + ", " + AmountRawCases2 + ", " + AmountUnits2 + ", " + TotalAmount2 + ", " + WHTaxAmount + "," + SalesTaxAmount + " ," + NetAmount2 + ", 1, " + PromotionProducts[i].PROMOTION_ID + ")  ");
                         }
                     }
                 }
             }
                 System.out.println("here....");
                 InvoiceAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount));
                 InvoiceWHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceWHTaxAmount));
                 InvoiceNetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceNetAmount));
            //     final double TotalAmountExSalesTax = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount / (SalesTaxRate + 100.0) * 100.0));
             //    final double SalesTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount - TotalAmountExSalesTax));
                 String InoviceTotalAmountString = new StringBuilder(String.valueOf(InvoiceNetAmount)).toString();
                 if (InoviceTotalAmountString.indexOf(".") != -1) {
                     final double Fraction = Utilities.parseDouble(InoviceTotalAmountString.substring(InoviceTotalAmountString.indexOf("."), InoviceTotalAmountString.length()));
                     InoviceTotalAmountString = InoviceTotalAmountString.substring(0, InoviceTotalAmountString.indexOf("."));
                     if (Fraction != 0.0) {
                         InoviceTotalAmountString = new StringBuilder(String.valueOf(Utilities.parseInt(InoviceTotalAmountString) + 1)).toString();
                     }
                 }
                 final double FractionAmount = Utilities.parseDouble(InoviceTotalAmountString) - InvoiceNetAmount;
                 s5.executeUpdate("update mobile_order set invoice_amount = " + InvoiceAmount + ", sales_tax_amount  = " + InvoiceSalesTaxAmount + ", wh_tax_amount = " + InvoiceWHTaxAmount + ", total_amount = " + InvoiceNetAmount + ", fraction_adjustment = " + Utilities.getDisplayCurrencyFormatSimple(FractionAmount) + ", net_amount = " + InoviceTotalAmountString + " where id = " + SplitOrderID);
                 s5.executeUpdate("update mobile_order_unedited set is_processed = 1 where id = " + OrderID);
                 if (InvoiceAmount != 0.0) {
                     ds.commit();
                     try {
                         final long iOrderID = SplitOrderID;
                        // final Thread smsthread = (Thread)new SalesPosting.SalesPosting$1(iOrderID);
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
                     }
                     catch (Exception e) {
                         System.out.println("Sales Posting (SMS Attempt):");
                         e.printStackTrace();
                     }
                 }
                 else {
                     ds.rollback();
                 }
             }
             catch (SQLException e2) {
                 System.out.println("Split Order: OrderID " + OrderID + "\n" + e2);
             }
         }
         s5.close();
         s4.close();
         s3.close();
         s2.close();
         s.close();
         success = true;
     }
     catch (Exception e3) {
         System.out.println("Split Order: OrderID " + OrderID + "\n" + e3);
         try {
             ds.rollback();
         }
         catch (SQLException e4) {
             e4.printStackTrace();
             System.out.println("Split Order: OrderID " + OrderID + "\n" + e4);
         }
         if (ds != null) {
             try {
                 ds.dropConnection();
             }
             catch (SQLException e5) {
                 e5.printStackTrace();
             }
             return success;
         }
         return success;
     }
     finally {
         if (ds != null) {
             try {
                 ds.dropConnection();
             }
             catch (SQLException e5) {
                 e5.printStackTrace();
             }
         }
     }
     if (ds != null) {
         try {
             ds.dropConnection();
         }
         catch (SQLException e5) {
             e5.printStackTrace();
         }
     }
     return success;
 
 }
 
 
 
 public static boolean splitOrder(final long OrderID) {
     boolean success = false;
     final Datasource ds = new Datasource();
     try {
         ds.createConnection();
         final Statement s = ds.createStatement();
         final Statement s2 = ds.createStatement();
         final Statement s3 = ds.createStatement();
         final Statement s4 = ds.createStatement();
         final Statement s5 = ds.createStatement();
         double SalesTaxRate = 0.0;
         double WHTaxRate = 0.0;
         long OrderSequence = 0L;
      //   System.out.println("select codv.distributor_id, (SELECT id FROM distributor_beat_plan_view where distributor_id = codv.distributor_id and outlet_id = codv.outlet_id and assigned_to in (select created_by from mobile_order_unedited where id = " + OrderID + ") limit 1 ) pjp_id, (select snd_id from common_distributors where distributor_id = codv.distributor_id) snd_id, (select rsm_id from common_distributors where distributor_id = codv.distributor_id) rsm_id from common_outlets_distributors_view codv where codv.outlet_id in (select outlet_id from mobile_order_unedited where id = " + OrderID + ") order by codv.distributor_id desc");
         final ResultSet rs1 = s.executeQuery("select codv.distributor_id, (SELECT id FROM distributor_beat_plan_view where distributor_id = codv.distributor_id and outlet_id = codv.outlet_id and assigned_to in (select created_by from mobile_order_unedited where id = " + OrderID + ") limit 1 ) pjp_id, (select snd_id from common_distributors where distributor_id = codv.distributor_id) snd_id, (select rsm_id from common_distributors where distributor_id = codv.distributor_id) rsm_id from common_outlets_distributors_view codv where codv.outlet_id in (select outlet_id from mobile_order_unedited where id = " + OrderID + ") order by codv.distributor_id desc");
         while (rs1.next()) {
             try {
                 final long PJP_ID = rs1.getLong(2);
                 final String SND_ID = rs1.getString("snd_id");
                 final String RSM_ID = rs1.getString("rsm_id");
                 if (PJP_ID == 0L) {
                     continue;
                 }
                 ds.startTransaction();
                 long SplitOrderID = 0L;
                 double InvoiceAmount = 0.0;
                 double InvoiceWHTaxAmount = 0.0;
                 double InvoiceNetAmount = 0.0;
                 final List<Integer> ProductIDArray = new ArrayList<Integer>();
                 final List<Long> TotalUnitsArray = new ArrayList<Long>();
                 ++OrderSequence;
                 ds.startTransaction();
                 final long DistributorID = rs1.getLong(1);
                 long OutletID = 0L;
             //    System.out.println("select * from mobile_order_unedited where id = " + OrderID);
                 final ResultSet rs2 = s2.executeQuery("select * from mobile_order_unedited where id = " + OrderID);
                 if (rs2.first()) {
                     SalesTaxRate = rs2.getDouble("sales_tax_rate");
                     WHTaxRate = rs2.getDouble("wh_tax_rate");
                     OutletID = rs2.getLong("outlet_id");
                  //   System.out.println("insert into mobile_order (mobile_order_no, outlet_id, distributor_id, region_id, created_on, created_by,brand_discount_amount, sales_tax_rate, wh_tax_rate, uuid, platform, lat, lng, accuracy, mobile_timestamp, unedited_order_id, beat_plan_id, snd_id, rsm_id, sm_id, tdm_id, asm_id, is_spot_sale) values (" + OrderSequence + rs2.getString("mobile_order_no") + ", " + OutletID + ", " + DistributorID + ", (select region_id from common_distributors where distributor_id = " + DistributorID + "), now(), " + rs2.getString("created_by") + ", "+rs2.getDouble("brand_discount_amount")+" ," + rs2.getString("sales_tax_rate") + ", " + rs2.getString("wh_tax_rate") + ", '" + rs2.getString("uuid") + "', '" + rs2.getString("platform") + "', " + rs2.getString("lat") + ", " + rs2.getString("lng") + ", " + rs2.getString("accuracy") + ", '" + rs2.getString("mobile_timestamp") + "', " + OrderID + ", " + PJP_ID + ", " + SND_ID + ", " + RSM_ID + ",(SELECT if(sm_id = 0, null,sm_id) FROM distributor_beat_plan where id = " + PJP_ID + "), (SELECT if(tdm_id = 0, null,tdm_id) FROM distributor_beat_plan where id = " + PJP_ID + "), (SELECT if(asm_id = 0, null,asm_id) FROM distributor_beat_plan where id = " + PJP_ID + "),'" + rs2.getString("is_spot_sale") + "' )");
                     s3.executeUpdate("insert into mobile_order (mobile_order_no, outlet_id, distributor_id, region_id, created_on, created_by,brand_discount_amount, sales_tax_rate, wh_tax_rate, uuid, platform, lat, lng, accuracy, mobile_timestamp, unedited_order_id, beat_plan_id, snd_id, rsm_id, sm_id, tdm_id, asm_id, is_spot_sale) values (" + OrderSequence + rs2.getString("mobile_order_no") + ", " + OutletID + ", " + DistributorID + ", (select region_id from common_distributors where distributor_id = " + DistributorID + "), now(), " + rs2.getString("created_by") + ", "+rs2.getDouble("brand_discount_amount")+" ," + rs2.getString("sales_tax_rate") + ", " + rs2.getString("wh_tax_rate") + ", '" + rs2.getString("uuid") + "', '" + rs2.getString("platform") + "', " + rs2.getString("lat") + ", " + rs2.getString("lng") + ", " + rs2.getString("accuracy") + ", '" + rs2.getString("mobile_timestamp") + "', " + OrderID + ", " + PJP_ID + ", " + SND_ID + ", " + RSM_ID + ",(SELECT if(sm_id = 0, null,sm_id) FROM distributor_beat_plan where id = " + PJP_ID + "), (SELECT if(tdm_id = 0, null,tdm_id) FROM distributor_beat_plan where id = " + PJP_ID + "), (SELECT if(asm_id = 0, null,asm_id) FROM distributor_beat_plan where id = " + PJP_ID + "),'" + rs2.getString("is_spot_sale") + "' )");
                     final ResultSet rs3 = s3.executeQuery("select LAST_INSERT_ID()");
                     if (rs3.first()) {
                         SplitOrderID = rs3.getLong(1);
                     }
                 }
              //   System.out.println("SELECT * FROM mobile_order_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = " + OrderID + " and moup.is_promotion = 0 and moup.is_processed = 0 and moup.product_id in (SELECT product_id FROM employee_product_groups_list where product_group_id in (select product_group_id from common_distributors where distributor_id = " + DistributorID + "))");
                 final ResultSet rs3 = s2.executeQuery("SELECT * FROM mobile_order_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = " + OrderID + " and moup.is_promotion = 0 and moup.is_processed = 0 and moup.product_id in (SELECT product_id FROM employee_product_groups_list where product_group_id in (select product_group_id from common_distributors where distributor_id = " + DistributorID + "))");
                 while (rs3.next()) {
                     final int ProductID = rs3.getInt("product_id");
                     final int TotalUnits = rs3.getInt("total_units");
                     final int RawCases = rs3.getInt("raw_cases");
                     final int Units = rs3.getInt("units");
                     final int UnitsPerSKU = rs3.getInt("unit_per_sku");
                     final long LiquidInMLPerUnit = rs3.getLong("liquid_in_ml");
                     final long LiquidinML = LiquidInMLPerUnit * TotalUnits;
                     final double RateRawCase = rs3.getDouble("rate_raw_cases");
                     final double RateUnit = rs3.getDouble("rate_units");
                     final double HandDiscountRate = rs3.getDouble("hand_discount_rate");
                     final long HandDiscountID = rs3.getLong("hand_discount_id");
                     final double HandDiscountAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(HandDiscountRate * RawCases + HandDiscountRate / UnitsPerSKU * Units));
                     String HandDiscountIDInsert = "null";
                     if (HandDiscountID != 0L) {
                         HandDiscountIDInsert = new StringBuilder().append(HandDiscountID).toString();
                     }
                     final double AmountRawCases = rs3.getDouble("amount_raw_cases");
                     final double AmountUnits = rs3.getDouble("amount_units");
                     final double TotalAmount = rs3.getDouble("total_amount");
                     final double WHTaxAmount = rs3.getDouble("wh_tax_amount");
                     final double NetAmount = rs3.getDouble("net_amount");
                     final double TO_discount = rs3.getDouble("TO_discount");
                     InvoiceAmount += TotalAmount;
                     InvoiceWHTaxAmount += WHTaxAmount;
                     InvoiceNetAmount += NetAmount;
                     final String PromotionID = null;
                     ProductIDArray.add(ProductID);
                     TotalUnitsArray.add(TotalUnits * 1L);
                     s3.executeUpdate("insert into mobile_order_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id,TO_discount) values (" + SplitOrderID + ", " + ProductID + ", " + RawCases + ", " + Units + ", " + TotalUnits + ", " + LiquidinML + ", " + RateRawCase + ", " + RateUnit + ", " + AmountRawCases + ", " + AmountUnits + ", " + TotalAmount + ", " + WHTaxAmount + "," + NetAmount + ", 0, " + PromotionID + "," + HandDiscountRate + ", " + HandDiscountAmount + ", " + HandDiscountIDInsert + ","+TO_discount+")");
                     s3.executeUpdate("update mobile_order_unedited_products set is_processed = 1 where id = " + OrderID + " and product_id = " + ProductID + " and is_promotion = 0");
                 }
                 
                // System.out.println("select * from mobile_order_unedited_brand_discount where id="+OrderID);
                 final ResultSet rsBrandDiscount = s2.executeQuery("select * from mobile_order_unedited_brand_discount where id="+OrderID);
                 while(rsBrandDiscount.next()) {
                	// System.out.println("INSERT INTO `pep`.`mobile_order_brand_discount`(`id`,`discount_brand_id`,`brand_id`,`brand_discount_amount`,`cartons`)VALUES("+SplitOrderID+", "+rsBrandDiscount.getLong("discount_brand_id")+","+rsBrandDiscount.getInt("brand_id")+","+rsBrandDiscount.getDouble("brand_discount_amount")+", "+rsBrandDiscount.getInt("cartons")+")");
						s3.executeUpdate("INSERT INTO `pep`.`mobile_order_brand_discount`(`id`,`discount_brand_id`,`brand_id`,`brand_discount_amount`,`cartons`)VALUES("+SplitOrderID+", "+rsBrandDiscount.getLong("discount_brand_id")+","+rsBrandDiscount.getInt("brand_id")+","+rsBrandDiscount.getDouble("brand_discount_amount")+", "+rsBrandDiscount.getInt("cartons")+")");
                 }
                 
               //  System.out.println("select * from mobile_order_unedited_brand_discount_products where id="+OrderID);
                 final ResultSet rsBrandDiscountProducts = s2.executeQuery("select * from mobile_order_unedited_brand_discount_products where id="+OrderID);
                 while(rsBrandDiscountProducts.next()) {
                //	 System.out.println("INSERT INTO `pep`.`mobile_order_brand_discount_products`(`id`,`discount_brand_id`,`brand_id`,`product_id`,`quantity`,`cartons`) VALUES("+SplitOrderID+", "+ rsBrandDiscountProducts.getLong("discount_brand_id") +", "+rsBrandDiscountProducts.getInt("brand_id")+", "+rsBrandDiscountProducts.getInt("product_id")+", "+rsBrandDiscountProducts.getInt("quantity")+" ,"+ rsBrandDiscountProducts.getInt("cartons") +")");
                	 s3.executeUpdate("INSERT INTO `pep`.`mobile_order_brand_discount_products`(`id`,`discount_brand_id`,`brand_id`,`product_id`,`quantity`,`cartons`) VALUES("+SplitOrderID+", "+ rsBrandDiscountProducts.getLong("discount_brand_id") +", "+rsBrandDiscountProducts.getInt("brand_id")+", "+rsBrandDiscountProducts.getInt("product_id")+", "+rsBrandDiscountProducts.getInt("quantity")+" ,"+ rsBrandDiscountProducts.getInt("cartons") +")");
                 }
                 
                 
                 
                 if(ProductIDArray != null) {
                 if ( ProductIDArray.size() > 0) {
                     final PromotionItem[] PromotionProducts = Product.getPromotionItems(OutletID, ArrayUtils.toPrimitive((Integer[])ProductIDArray.toArray(new Integer[ProductIDArray.size()])), ArrayUtils.toPrimitive((Long[])TotalUnitsArray.toArray(new Long[TotalUnitsArray.size()])));
                     for (int i = 0; i < PromotionProducts.length; ++i) {
                         final long[] RawCasesAndUnits = Utilities.getRawCasesAndUnits(PromotionProducts[i].TOTAL_UNITS, PromotionProducts[i].UNIT_PER_SKU);
                         long ProSAPCode = 0L;
                         int ProProductID = 0;
                         double ProSellingPriceRawCase = 0.0;
                         double ProSellingPriceUnit = 0.0;
                         long ProLiquidInML = 0L;
                         int BrandID = 0;
                         int SelectedBrandID = 0;
                      //   System.out.println("SELECT ipv.brand_id FROM mobile_order_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = " + OrderID + " and moup.promotion_id = " + PromotionProducts[i].PROMOTION_ID);
                         final ResultSet rs4 = s4.executeQuery("SELECT ipv.brand_id FROM mobile_order_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = " + OrderID + " and moup.promotion_id = " + PromotionProducts[i].PROMOTION_ID);
                         if (rs4.first()) {
                             SelectedBrandID = rs4.getInt(1);
                         }
                         if (PromotionProducts[i].BRANDS.size() > 0) {
                             BrandID = PromotionProducts[i].BRANDS.get(0);
                         }
                         if (SelectedBrandID != 0) {
                             BrandID = SelectedBrandID;
                         }
                         if (BrandID != 0) {
                             final Product PromotionProduct = new Product(1, PromotionProducts[i].PACKAGE_ID, BrandID);
                             ProProductID = PromotionProduct.PRODUCT_ID;
                             ProSAPCode = PromotionProduct.SAP_CODE;
                             final double[] rates = Product.getSellingPrice(PromotionProduct.SAP_CODE, OutletID);
                             ProSellingPriceRawCase = rates[0];
                             ProSellingPriceUnit = rates[1];
                             ProLiquidInML = PromotionProduct.LIQUID_IN_ML;
                             final double AmountRawCases2 = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(RawCasesAndUnits[0] * ProSellingPriceRawCase));
                             final double AmountUnits2 = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(RawCasesAndUnits[1] * ProSellingPriceUnit));
                             final double TotalAmount2 = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(AmountRawCases2 + AmountUnits2));
                             final double WHTaxAmount2 = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(TotalAmount2 * WHTaxRate / 100.0));
                             final double NetAmount2 = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(TotalAmount2 + WHTaxAmount2));
                             s3.executeUpdate("replace into mobile_order_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, is_promotion, promotion_id) values (" + SplitOrderID + ", " + ProProductID + ", " + RawCasesAndUnits[0] + ", " + RawCasesAndUnits[1] + ", " + PromotionProducts[i].TOTAL_UNITS + ", " + ProLiquidInML + ", " + ProSellingPriceRawCase + ", " + ProSellingPriceUnit + ", " + AmountRawCases2 + ", " + AmountUnits2 + ", " + TotalAmount2 + ", " + WHTaxAmount2 + " ," + NetAmount2 + ", 1, " + PromotionProducts[i].PROMOTION_ID + ")  ");
                         }
                     }
                 }
             }
                 InvoiceAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount));
                 InvoiceWHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceWHTaxAmount));
                 InvoiceNetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceNetAmount));
                 final double TotalAmountExSalesTax = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount / (SalesTaxRate + 100.0) * 100.0));
                 final double SalesTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount - TotalAmountExSalesTax));
                 String InoviceTotalAmountString = new StringBuilder(String.valueOf(InvoiceNetAmount)).toString();
                 if (InoviceTotalAmountString.indexOf(".") != -1) {
                     final double Fraction = Utilities.parseDouble(InoviceTotalAmountString.substring(InoviceTotalAmountString.indexOf("."), InoviceTotalAmountString.length()));
                     InoviceTotalAmountString = InoviceTotalAmountString.substring(0, InoviceTotalAmountString.indexOf("."));
                     if (Fraction != 0.0) {
                         InoviceTotalAmountString = new StringBuilder(String.valueOf(Utilities.parseInt(InoviceTotalAmountString) + 1)).toString();
                     }
                 }
                 final double FractionAmount = Utilities.parseDouble(InoviceTotalAmountString) - InvoiceNetAmount;
                 s5.executeUpdate("update mobile_order set invoice_amount = " + InvoiceAmount + ", sales_tax_amount  = " + SalesTaxAmount + ", wh_tax_amount = " + InvoiceWHTaxAmount + ", total_amount = " + InvoiceNetAmount + ", fraction_adjustment = " + Utilities.getDisplayCurrencyFormatSimple(FractionAmount) + ", net_amount = " + InoviceTotalAmountString + " where id = " + SplitOrderID);
                 s5.executeUpdate("update mobile_order_unedited set is_processed = 1 where id = " + OrderID);
                 if (InvoiceAmount != 0.0) {
                     ds.commit();
                     try {
                         final long iOrderID = SplitOrderID;
                        // final Thread smsthread = (Thread)new SalesPosting.SalesPosting$1(iOrderID);
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
                     }
                     catch (Exception e) {
                         System.out.println("Sales Posting (SMS Attempt):");
                         e.printStackTrace();
                     }
                 }
                 else {
                     ds.rollback();
                 }
             }
             catch (SQLException e2) {
                 System.out.println("Split Order: OrderID " + OrderID + "\n" + e2);
             }
         }
         s5.close();
         s4.close();
         s3.close();
         s2.close();
         s.close();
         success = true;
     }
     catch (Exception e3) {
         System.out.println("Split Order: OrderID " + OrderID + "\n" + e3);
         try {
             ds.rollback();
         }
         catch (SQLException e4) {
             e4.printStackTrace();
             System.out.println("Split Order: OrderID " + OrderID + "\n" + e4);
         }
         if (ds != null) {
             try {
                 ds.dropConnection();
             }
             catch (SQLException e5) {
                 e5.printStackTrace();
             }
             return success;
         }
         return success;
     }
     finally {
         if (ds != null) {
             try {
                 ds.dropConnection();
             }
             catch (SQLException e5) {
                 e5.printStackTrace();
             }
         }
     }
     if (ds != null) {
         try {
             ds.dropConnection();
         }
         catch (SQLException e5) {
             e5.printStackTrace();
         }
     }
     return success;
 }
 
 public static boolean splitOrderSM(final long OrderID) {
     boolean success = false;
     final Datasource ds = new Datasource();
     try {
         ds.createConnection();
         final Statement s = ds.createStatement();
         final Statement s2 = ds.createStatement();
         final Statement s3 = ds.createStatement();
         final Statement s4 = ds.createStatement();
         final Statement s5 = ds.createStatement();
         double SalesTaxRate = 0.0;
         double WHTaxRate = 0.0;
         long OrderSequence = 0L;
         final ResultSet rs1 = s.executeQuery("select codv.distributor_id, (SELECT id FROM distributor_beat_plan_view where distributor_id = codv.distributor_id and outlet_id = codv.outlet_id and asm_id in (select created_by from mobile_order_sm_unedited where id = " + OrderID + ") limit 1 ) pjp_id, (select snd_id from common_distributors where distributor_id = codv.distributor_id) snd_id, (select rsm_id from common_distributors where distributor_id = codv.distributor_id) rsm_id from common_outlets_distributors_view codv where codv.outlet_id in (select outlet_id from mobile_order_sm_unedited where id = " + OrderID + ") order by codv.distributor_id desc");
         while (rs1.next()) {
             try {
                 final long PJP_ID = rs1.getLong(2);
                 final String SND_ID = rs1.getString("snd_id");
                 final String RSM_ID = rs1.getString("rsm_id");
                 if (PJP_ID == 0L) {
                     continue;
                 }
                 ds.startTransaction();
                 long SplitOrderID = 0L;
                 double InvoiceAmount = 0.0;
                 double InvoiceWHTaxAmount = 0.0;
                 double InvoiceNetAmount = 0.0;
                 final List<Integer> ProductIDArray = new ArrayList<Integer>();
                 final List<Long> TotalUnitsArray = new ArrayList<Long>();
                 ++OrderSequence;
                 ds.startTransaction();
                 final long DistributorID = rs1.getLong(1);
                 long OutletID = 0L;
                 final ResultSet rs2 = s2.executeQuery("select * from mobile_order_sm_unedited where id = " + OrderID);
                 if (rs2.first()) {
                     SalesTaxRate = rs2.getDouble("sales_tax_rate");
                     WHTaxRate = rs2.getDouble("wh_tax_rate");
                     OutletID = rs2.getLong("outlet_id");
                     s3.executeUpdate("insert into mobile_order_sm (mobile_order_no, outlet_id, distributor_id, region_id, created_on, created_by, sales_tax_rate, wh_tax_rate, uuid, platform, lat, lng, accuracy, mobile_timestamp, unedited_order_id, beat_plan_id, snd_id, rsm_id, sm_id, tdm_id, asm_id) values (" + OrderSequence + rs2.getString("mobile_order_no") + ", " + OutletID + ", " + DistributorID + ", (select region_id from common_distributors where distributor_id = " + DistributorID + "), now(), " + rs2.getString("created_by") + ", " + rs2.getString("sales_tax_rate") + ", " + rs2.getString("wh_tax_rate") + ", '" + rs2.getString("uuid") + "', '" + rs2.getString("platform") + "', " + rs2.getString("lat") + ", " + rs2.getString("lng") + ", " + rs2.getString("accuracy") + ", '" + rs2.getString("mobile_timestamp") + "', " + OrderID + ", " + PJP_ID + ", " + SND_ID + ", " + RSM_ID + ",(SELECT if(sm_id = 0, null,sm_id) FROM distributor_beat_plan where id = " + PJP_ID + "), (SELECT if(tdm_id = 0, null,tdm_id) FROM distributor_beat_plan where id = " + PJP_ID + "), (SELECT if(asm_id = 0, null,asm_id) FROM distributor_beat_plan where id = " + PJP_ID + ") )");
                     final ResultSet rs3 = s3.executeQuery("select LAST_INSERT_ID()");
                     if (rs3.first()) {
                         SplitOrderID = rs3.getLong(1);
                     }
                 }
                 final ResultSet rs3 = s2.executeQuery("SELECT * FROM mobile_order_sm_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = " + OrderID + " and moup.is_promotion = 0 and moup.is_processed = 0 and moup.product_id in (SELECT product_id FROM employee_product_groups_list where product_group_id in (select product_group_id from common_distributors where distributor_id = " + DistributorID + "))");
                 while (rs3.next()) {
                     final int ProductID = rs3.getInt("product_id");
                     final int TotalUnits = rs3.getInt("total_units");
                     final int RawCases = rs3.getInt("raw_cases");
                     final int Units = rs3.getInt("units");
                     final int UnitsPerSKU = rs3.getInt("unit_per_sku");
                     final long LiquidInMLPerUnit = rs3.getLong("liquid_in_ml");
                     final long LiquidinML = LiquidInMLPerUnit * TotalUnits;
                     final double RateRawCase = rs3.getDouble("rate_raw_cases");
                     final double RateUnit = rs3.getDouble("rate_units");
                     final double HandDiscountRate = rs3.getDouble("hand_discount_rate");
                     final long HandDiscountID = rs3.getLong("hand_discount_id");
                     final double HandDiscountAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(HandDiscountRate * RawCases + HandDiscountRate / UnitsPerSKU * Units));
                     String HandDiscountIDInsert = "null";
                     if (HandDiscountID != 0L) {
                         HandDiscountIDInsert = new StringBuilder().append(HandDiscountID).toString();
                     }
                     final double AmountRawCases = rs3.getDouble("amount_raw_cases");
                     final double AmountUnits = rs3.getDouble("amount_units");
                     final double TotalAmount = rs3.getDouble("total_amount");
                     final double WHTaxAmount = rs3.getDouble("wh_tax_amount");
                     final double NetAmount = rs3.getDouble("net_amount");
                     InvoiceAmount += TotalAmount;
                     InvoiceWHTaxAmount += WHTaxAmount;
                     InvoiceNetAmount += NetAmount;
                     final String PromotionID = null;
                     ProductIDArray.add(ProductID);
                     TotalUnitsArray.add(TotalUnits * 1L);
                     s3.executeUpdate("insert into mobile_order_sm_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, is_promotion, promotion_id, hand_discount_rate, hand_discount_amount, hand_discount_id) values (" + SplitOrderID + ", " + ProductID + ", " + RawCases + ", " + Units + ", " + TotalUnits + ", " + LiquidinML + ", " + RateRawCase + ", " + RateUnit + ", " + AmountRawCases + ", " + AmountUnits + ", " + TotalAmount + ", " + WHTaxAmount + "," + NetAmount + ", 0, " + PromotionID + "," + HandDiscountRate + ", " + HandDiscountAmount + ", " + HandDiscountIDInsert + ")  ");
                     s3.executeUpdate("update mobile_order_sm_unedited_products set is_processed = 1 where id = " + OrderID + " and product_id = " + ProductID + " and is_promotion = 0");
                 }
                 if (ProductIDArray != null && ProductIDArray.size() > 0) {
                     final PromotionItem[] PromotionProducts = Product.getPromotionItems(OutletID, ArrayUtils.toPrimitive((Integer[])ProductIDArray.toArray(new Integer[ProductIDArray.size()])), ArrayUtils.toPrimitive((Long[])TotalUnitsArray.toArray(new Long[TotalUnitsArray.size()])));
                     for (int i = 0; i < PromotionProducts.length; ++i) {
                         final long[] RawCasesAndUnits = Utilities.getRawCasesAndUnits(PromotionProducts[i].TOTAL_UNITS, PromotionProducts[i].UNIT_PER_SKU);
                         long ProSAPCode = 0L;
                         int ProProductID = 0;
                         double ProSellingPriceRawCase = 0.0;
                         double ProSellingPriceUnit = 0.0;
                         long ProLiquidInML = 0L;
                         int BrandID = 0;
                         int SelectedBrandID = 0;
                         final ResultSet rs4 = s4.executeQuery("SELECT ipv.brand_id FROM mobile_order_sm_unedited_products moup join inventory_products_view ipv on moup.product_id = ipv.product_id where moup.id = " + OrderID + " and moup.promotion_id = " + PromotionProducts[i].PROMOTION_ID);
                         if (rs4.first()) {
                             SelectedBrandID = rs4.getInt(1);
                         }
                         if (PromotionProducts[i].BRANDS.size() > 0) {
                             BrandID = PromotionProducts[i].BRANDS.get(0);
                         }
                         if (SelectedBrandID != 0) {
                             BrandID = SelectedBrandID;
                         }
                         if (BrandID != 0) {
                             final Product PromotionProduct = new Product(1, PromotionProducts[i].PACKAGE_ID, BrandID);
                             ProProductID = PromotionProduct.PRODUCT_ID;
                             ProSAPCode = PromotionProduct.SAP_CODE;
                             final double[] rates = Product.getSellingPrice(PromotionProduct.SAP_CODE, OutletID);
                             ProSellingPriceRawCase = rates[0];
                             ProSellingPriceUnit = rates[1];
                             ProLiquidInML = PromotionProduct.LIQUID_IN_ML;
                             final double AmountRawCases2 = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(RawCasesAndUnits[0] * ProSellingPriceRawCase));
                             final double AmountUnits2 = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(RawCasesAndUnits[1] * ProSellingPriceUnit));
                             final double TotalAmount2 = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(AmountRawCases2 + AmountUnits2));
                             final double WHTaxAmount2 = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(TotalAmount2 * WHTaxRate / 100.0));
                             final double NetAmount2 = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(TotalAmount2 + WHTaxAmount2));
                             s3.executeUpdate("replace into mobile_order_sm_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, is_promotion, promotion_id) values (" + SplitOrderID + ", " + ProProductID + ", " + RawCasesAndUnits[0] + ", " + RawCasesAndUnits[1] + ", " + PromotionProducts[i].TOTAL_UNITS + ", " + ProLiquidInML + ", " + ProSellingPriceRawCase + ", " + ProSellingPriceUnit + ", " + AmountRawCases2 + ", " + AmountUnits2 + ", " + TotalAmount2 + ", " + WHTaxAmount2 + " ," + NetAmount2 + ", 1, " + PromotionProducts[i].PROMOTION_ID + ")  ");
                         }
                     }
                 }
                 InvoiceAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount));
                 InvoiceWHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceWHTaxAmount));
                 InvoiceNetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceNetAmount));
                 final double TotalAmountExSalesTax = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount / (SalesTaxRate + 100.0) * 100.0));
                 final double SalesTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount - TotalAmountExSalesTax));
                 String InoviceTotalAmountString = new StringBuilder(String.valueOf(InvoiceNetAmount)).toString();
                 if (InoviceTotalAmountString.indexOf(".") != -1) {
                     final double Fraction = Utilities.parseDouble(InoviceTotalAmountString.substring(InoviceTotalAmountString.indexOf("."), InoviceTotalAmountString.length()));
                     InoviceTotalAmountString = InoviceTotalAmountString.substring(0, InoviceTotalAmountString.indexOf("."));
                     if (Fraction != 0.0) {
                         InoviceTotalAmountString = new StringBuilder(String.valueOf(Utilities.parseInt(InoviceTotalAmountString) + 1)).toString();
                     }
                 }
                 final double FractionAmount = Utilities.parseDouble(InoviceTotalAmountString) - InvoiceNetAmount;
                 s5.executeUpdate("update mobile_order_sm set invoice_amount = " + InvoiceAmount + ", sales_tax_amount  = " + SalesTaxAmount + ", wh_tax_amount = " + InvoiceWHTaxAmount + ", total_amount = " + InvoiceNetAmount + ", fraction_adjustment = " + Utilities.getDisplayCurrencyFormatSimple(FractionAmount) + ", net_amount = " + InoviceTotalAmountString + " where id = " + SplitOrderID);
                 s5.executeUpdate("update mobile_order_sm_unedited set is_processed = 1 where id = " + OrderID);
                 if (InvoiceAmount != 0.0) {
                     ds.commit();
                     try {
                         final long iOrderID = SplitOrderID;
                        // final Thread smsthread = (Thread)new SalesPosting.SalesPosting$2(iOrderID);
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
                     }
                     catch (Exception e) {
                         System.out.println("Sales Posting SM(SMS Attempt):");
                         e.printStackTrace();
                     }
                 }
                 else {
                     ds.rollback();
                 }
             }
             catch (SQLException e2) {
                 System.out.println("SM Split Order: OrderID " + OrderID + "\n" + e2);
             }
         }
         s5.close();
         s4.close();
         s3.close();
         s2.close();
         s.close();
         success = true;
     }
     catch (Exception e3) {
         System.out.println("SM Split Order: OrderID " + OrderID + "\n" + e3);
         try {
             ds.rollback();
         }
         catch (SQLException e4) {
             e4.printStackTrace();
         }
         if (ds != null) {
             try {
                 ds.dropConnection();
             }
             catch (SQLException e5) {
                 e5.printStackTrace();
             }
             return success;
         }
         return success;
     }
     finally {
         if (ds != null) {
             try {
                 ds.dropConnection();
             }
             catch (SQLException e5) {
                 e5.printStackTrace();
             }
         }
     }
     if (ds != null) {
         try {
             ds.dropConnection();
         }
         catch (SQLException e5) {
             e5.printStackTrace();
         }
     }
     return success;
 }
}