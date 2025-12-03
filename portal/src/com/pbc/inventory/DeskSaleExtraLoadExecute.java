package com.pbc.inventory;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
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
import org.json.simple.JSONObject;

@WebServlet(
   description = "Executes Desk Sale Extra Load",
   urlPatterns = {"/inventory/DeskSaleExtraLoadExecute"}
)
public class DeskSaleExtraLoadExecute extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      HttpSession session = request.getSession();
      String UserID = null;
      if (session.getAttribute("UserID") != null) {
         UserID = (String)session.getAttribute("UserID");
      }

      if (UserID == null) {
         response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
      }

      PrintWriter out = response.getWriter();
      int DeskSaleEditID = Utilities.parseInt(request.getParameter("DeskSaleEditID"));
      boolean isEditCase = false;
      if (DeskSaleEditID > 0) {
         isEditCase = true;
      }

      long DispatchID = Utilities.parseLong(request.getParameter("DispatchIDHidden"));
      long OutletID = Utilities.parseLong(request.getParameter("DeskSaleOutledIDHidden"));
      long UniqueVoucherID = Utilities.parseLong(request.getParameter("UniqueVoucherID"));
      long DistributorID = Utilities.parseLong(request.getParameter("DeskSaleDistributorIDHidden"));
      long RegionID = Utilities.parseLong(request.getParameter("DeskSaleRegionIDHidden"));
      double SalesTaxRate = Utilities.parseDouble(request.getParameter("DeskSaleSalesTaxRateHidden"));
      double WHTaxRate = Utilities.parseDouble(request.getParameter("DeskSaleWHTaxHidden"));
      double WHTaxAmount = Utilities.parseDouble(request.getParameter("DeskSaleMainFormWithHoldingTax"));
      double TotalAmount = Utilities.parseDouble(request.getParameter("DeskSaleMainFormTotalAmount"));
      double TotalDiscout = Utilities.parseDouble(request.getParameter("DeskSaleMainFormTotalDiscount"));
      double Adjustment = Utilities.parseDouble(request.getParameter("DeskSaleMainFormAdjustment"));
      double NetAmount = Utilities.parseDouble(request.getParameter("DeskSaleMainFormFinalNetAmountRounded"));
      int[] ProductID = Utilities.parseInt(request.getParameterValues("ProductID"));
      int[] RawCases = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormRawCases"));
      int[] Units = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormUnits"));
      double[] aRate = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormRateHidden"));
      double[] aUnitRate = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormUnitRateHidden"));
      double[] Discount = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormDiscount"));
      int[] UnitPerSKU = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormUnitPerSKU"));
      int[] LiquidInML = Utilities.parseInt(request.getParameterValues("DeskSaleMainFormLiquidInML"));
      double[] RowAmount = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormAmount"));
      double[] RowNetAmount = Utilities.parseDouble(request.getParameterValues("DeskSaleMainFormNetAmount"));
      long[] PromotionID = Utilities.parseLong(request.getParameterValues("PromotionID"));
      int[] PromotionsProductID = Utilities.parseInt(request.getParameterValues("PromotionsProductID"));
      int[] PromotionsRawCases = Utilities.parseInt(request.getParameterValues("PromotionsRawCases"));
      int[] PromotionsUnits = Utilities.parseInt(request.getParameterValues("PromotionsUnits"));
      double[] PromotionsRate = Utilities.parseDouble(request.getParameterValues("PromotionsRateRawCase"));
      double[] PromotionsUnitRate = Utilities.parseDouble(request.getParameterValues("PromotionsRateUnit"));
      int[] PromotionsUnitPerSKU = Utilities.parseInt(request.getParameterValues("PromotionsUnitPerSKU"));
      int[] PromotionsLiquidInML = Utilities.parseInt(request.getParameterValues("PromotionsLiquidInML"));
      double[] PromotionsRowAmount = Utilities.parseDouble(request.getParameterValues("PromotionsAmount"));
      Datasource ds = new Datasource();
      JSONObject obj = new JSONObject();
      new Date();

      try {
         ds.createConnection();
         Statement s = ds.createStatement();
         Statement s1 = ds.createStatement();
         if (isEditCase) {
            ResultSet rs_date_check = s.executeQuery("select created_on from inventory_sales_invoices where id=" + DeskSaleEditID);
            if (rs_date_check.first()) {
               java.sql.Date var53 = rs_date_check.getDate(1);
            }
         }

         String SQLMain = "";
         ResultSet rs1 = s1.executeQuery("select id from inventory_sales_invoices where uvid=" + UniqueVoucherID);
         if (rs1.first()) {
            obj.put("success", "false");
            obj.put("error", "Already Exists");
         } else {
            ds.startTransaction();
            long PJPID = 0L;

            for(ResultSet rsp = s.executeQuery("select distinct dbp.id from distributor_beat_plan dbp join distributor_beat_plan_schedule dbps on dbp.id = dbps.id where dbps.outlet_id = " + OutletID + " and dbp.distributor_id = " + DistributorID); rsp.next(); PJPID = rsp.getLong(1)) {
            }

            if (isEditCase) {
               SQLMain = "update inventory_sales_invoices set uvid=" + UniqueVoucherID + ", created_on=now(), created_by=" + UserID + ", outlet_id=" + OutletID + ", type_id=1, distributor_id=" + DistributorID + ", invoice_amount=0, sales_tax_rate=" + SalesTaxRate + ", sales_tax_amount=0, wh_tax_rate=" + WHTaxRate + ", wh_tax_amount=0, total_amount=0, discount=0, net_amount=0, region_id=" + RegionID + ", fraction_adjustment=0 where id=" + DeskSaleEditID;
            } else {
               SQLMain = "insert into inventory_sales_invoices (uvid, created_on, created_by, outlet_id, type_id, distributor_id, invoice_amount, sales_tax_rate, sales_tax_amount, wh_tax_rate, wh_tax_amount, total_amount, discount, net_amount, region_id, fraction_adjustment, is_dispatched, beat_plan_id) values(" + UniqueVoucherID + ", now(), " + UserID + ", " + OutletID + ", 2, " + DistributorID + ", 0, " + SalesTaxRate + ", 0, " + WHTaxRate + ", 0, 0, 0, 0, " + RegionID + ", 0, 1, " + PJPID + ")";
            }

            s.executeUpdate(SQLMain);
            int DeskSaleID = 0;
            if (!isEditCase) {
               ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
               if (rs.first()) {
                  DeskSaleID = rs.getInt(1);
               }
            } else {
               DeskSaleID = DeskSaleEditID;
            }

            s.executeUpdate("update inventory_sales_dispatch set is_liquid_returned=1,liquid_returned_on=now(),liquid_returned_by=" + UserID + " where id=" + DispatchID);
            s.executeUpdate("delete from inventory_sales_invoices_products where id=" + DeskSaleID);
            double InvoiceAmount = 0.0D;
            double InvoiceWHTaxAmount = 0.0D;
            double InvoiceNetAmount = 0.0D;

            double ItemTotalAmount;
            double ItemWHTaxAmount;
            double ItemNetAmount;
            for(int i = 0; i < ProductID.length; ++i) {
               int UnitsPerSKU = 0;
               long LiquidInMLPerUnit = 0L;
               ResultSet rs3 = s.executeQuery("SELECT unit_per_sku, liquid_in_ml FROM inventory_products_view where product_id = " + ProductID[i]);
               if (rs3.first()) {
                  UnitsPerSKU = rs3.getInt(1);
                  LiquidInMLPerUnit = rs3.getLong(2);
               }

               long TotalUnits = (long)(RawCases[i] * UnitsPerSKU + Units[i]);
               long LiquidInMLValue = TotalUnits * LiquidInMLPerUnit;
               double[] UnitRates = Product.getSellingPrice(ProductID[i], OutletID);
               double RateRawCase = UnitRates[0];
               double RateUnit = UnitRates[1];
               double RawCaseAmount = (double)RawCases[i] * RateRawCase;
               double UnitAmount = (double)Units[i] * RateUnit;
               double HandDiscountRate = UnitRates[2];
               long HandDiscountID = Math.round(UnitRates[3]);
               double HandDiscountAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(HandDiscountRate * (double)RawCases[i] + HandDiscountRate / (double)UnitsPerSKU * (double)Units[i]));
               String HandDiscountIDInsert = "null";
               if (HandDiscountID != 0L) {
                  HandDiscountIDInsert = "" + HandDiscountID;
               }

               ItemTotalAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(RawCaseAmount + UnitAmount));
               ItemWHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(ItemTotalAmount * WHTaxRate / 100.0D));
               ItemNetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(ItemTotalAmount + ItemWHTaxAmount));
               InvoiceAmount += ItemTotalAmount;
               InvoiceWHTaxAmount += ItemWHTaxAmount;
               InvoiceNetAmount += ItemNetAmount;
               if (!this.isExtraValid(DispatchID, ProductID[i], TotalUnits, s)) {
                  obj.put("success", "false");
                  obj.put("error", "Quantity exceeded available quota.");
                  ds.rollback();
                  out.print(obj);
                  out.close();
                  return;
               }

               s.executeUpdate("insert into inventory_sales_invoices_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, discount, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, hand_discount_rate, hand_discount_amount, hand_discount_id) values (" + DeskSaleID + ", " + ProductID[i] + ", " + RawCases[i] + ", " + Units[i] + ", " + TotalUnits + ", " + LiquidInMLValue + ", " + Discount[i] + ", " + RateRawCase + ", " + RateUnit + ", " + RawCaseAmount + ", " + UnitAmount + ", " + ItemTotalAmount + ", " + ItemWHTaxAmount + "," + ItemNetAmount + "," + HandDiscountRate + ", " + HandDiscountAmount + ", " + HandDiscountIDInsert + ") ");
               int returnProducts = 0;
               int totalProducts = 0;
               System.out.println("select raw_cases from inventory_sales_dispatch_extra_products where dispatch_id=" + DispatchID + " and product_id=" + ProductID[i]);
               ResultSet rsReturnProducts = s.executeQuery("select raw_cases from inventory_sales_dispatch_extra_products where dispatch_id=" + DispatchID + " and product_id=" + ProductID[i]);
               if (rsReturnProducts.first()) {
                  totalProducts = rsReturnProducts.getInt("raw_cases");
               }

                returnProducts = totalProducts - RawCases[i];
               if (returnProducts > 0) {
                  long TotalUnitsRetuned = (long)(returnProducts * UnitsPerSKU + Units[i]);
                  long TotalLiquidInMLValue = TotalUnitsRetuned * LiquidInMLPerUnit;
                  s.executeUpdate("insert into inventory_sales_dispatch_adjusted_products (dispatch_id,outlet_id,product_id,raw_cases,units,total_units,liquid_in_ml,invoice_id) values(" + DispatchID + "," + OutletID + "," + ProductID[i] + "," + returnProducts + "," + TotalUnitsRetuned + "," + TotalUnits + "," + TotalLiquidInMLValue + "," + DeskSaleID + ")");
                  System.out.println("insert into inventory_sales_dispatch_returned_products (dispatch_id,product_id,raw_cases,units,total_units,liquid_in_ml,outlet_id,invoice_id) values(" + DispatchID + "," + ProductID[i] + "," + returnProducts + "," + Units[i] + "," + TotalUnitsRetuned + "," + TotalLiquidInMLValue + ", " + OutletID + ", " + DeskSaleID + ")");
                  s.executeUpdate("insert into inventory_sales_dispatch_returned_products (dispatch_id,product_id,raw_cases,units,total_units,liquid_in_ml,outlet_id,invoice_id) values(" + DispatchID + "," + ProductID[i] + "," + returnProducts + "," + Units[i] + "," + TotalUnitsRetuned + "," + TotalLiquidInMLValue + ", " + OutletID + ", " + DeskSaleID + ")");
               }
            }

            InvoiceAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount));
            InvoiceWHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceWHTaxAmount));
            InvoiceNetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceNetAmount));
            double TotalAmountExSalesTax = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount / (SalesTaxRate + 100.0D) * 100.0D));
            double SalesTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(InvoiceAmount - TotalAmountExSalesTax));
            String InoviceTotalAmountString = String.valueOf(InvoiceNetAmount);
            double FractionAmount;
            if (InoviceTotalAmountString.indexOf(".") != -1) {
               FractionAmount = Utilities.parseDouble(InoviceTotalAmountString.substring(InoviceTotalAmountString.indexOf("."), InoviceTotalAmountString.length()));
               InoviceTotalAmountString = InoviceTotalAmountString.substring(0, InoviceTotalAmountString.indexOf("."));
               if (FractionAmount != 0.0D) {
                  InoviceTotalAmountString = String.valueOf(Utilities.parseInt(InoviceTotalAmountString) + 1);
               }
            }

            FractionAmount = Utilities.parseDouble(InoviceTotalAmountString) - InvoiceNetAmount;
            if (PromotionsProductID != null) {
               for(int i = 0; i < PromotionsProductID.length; ++i) {
                  int UnitsPerSKU = 0;
                  long LiquidInMLPerUnit = 0L;
                  ResultSet rs3 = s.executeQuery("SELECT unit_per_sku, liquid_in_ml FROM inventory_products_view where product_id = " + PromotionsProductID[i]);
                  if (rs3.first()) {
                     UnitsPerSKU = rs3.getInt(1);
                     LiquidInMLPerUnit = rs3.getLong(2);
                  }

                  long TotalUnits = (long)(PromotionsRawCases[i] * UnitsPerSKU + PromotionsUnits[i]);
                  long LiquidInMLValue = TotalUnits * LiquidInMLPerUnit;
                  double[] UnitRates = Product.getSellingPrice(ProductID[i], OutletID);
                  double RateRawCase = UnitRates[0];
                  double RateUnit = UnitRates[1];
                  double RawCaseAmount = (double)PromotionsRawCases[i] * RateRawCase;
                  double UnitAmount = (double)PromotionsUnits[i] * RateUnit;
                  ItemTotalAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(RawCaseAmount + UnitAmount));
                  ItemWHTaxAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(ItemTotalAmount * WHTaxRate / 100.0D));
                  ItemNetAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(ItemTotalAmount + ItemWHTaxAmount));
                  if (!this.isExtraValid(DispatchID, PromotionsProductID[i], TotalUnits, s)) {
                     obj.put("success", "false");
                     obj.put("error", "Quantity exceeded available quota.");
                     ds.rollback();
                     out.print(obj);
                     out.close();
                     return;
                  }

                  s.executeUpdate("insert into inventory_sales_invoices_products (id, product_id, raw_cases, units, total_units, liquid_in_ml, discount, rate_raw_cases, rate_units, amount_raw_cases, amount_units, total_amount, wh_tax_amount, net_amount, is_promotion, promotion_id) values (" + DeskSaleID + ", " + PromotionsProductID[i] + ", " + PromotionsRawCases[i] + ", " + PromotionsUnits[i] + ", " + TotalUnits + ", " + LiquidInMLValue + ", 0, " + PromotionsRate[i] + ", " + PromotionsUnitRate[i] + ", " + Utilities.getDisplayCurrencyFormatSimple(RawCaseAmount) + ", " + Utilities.getDisplayCurrencyFormatSimple(UnitAmount) + ", " + ItemTotalAmount + ", " + ItemWHTaxAmount + "," + ItemNetAmount + ", 1, " + PromotionID[i] + ") ");
               }
            }

            s.executeUpdate("update inventory_sales_invoices set invoice_amount = " + InvoiceAmount + ", sales_tax_amount  = " + SalesTaxAmount + ", wh_tax_amount = " + InvoiceWHTaxAmount + ", total_amount = " + InvoiceNetAmount + ", fraction_adjustment = " + Utilities.getDisplayCurrencyFormatSimple(FractionAmount) + ", net_amount = " + InoviceTotalAmountString + " where id = " + DeskSaleID);
            if (!isEditCase) {
               s.executeUpdate("insert into inventory_sales_dispatch_extra_invoices (id, sales_id) values(" + DispatchID + ", " + DeskSaleID + ")");
            }

            ds.commit();
            boolean posted = SalesPosting.post((long)DeskSaleID, Long.parseLong(UserID));
            if (posted) {
               obj.put("success", "true");
               obj.put("DeskSaleID", DeskSaleID);
               obj.put("DispatchID", DispatchID);
            } else {
            	for(int i = 0; i < ProductID.length; ++i) {
				s.executeUpdate("delete from inventory_sales_dispatch_adjusted_products where invoice_id = " + DeskSaleID + " and product_id = "+ProductID[i]+ " and dispatch_id = " + DispatchID );
            	}
				obj.put("success", "false");
               obj.put("error", "Saved but could not be posted.");
            }

            StockPosting sp = new StockPosting();
            boolean isPosted = sp.postDispatchLiquidReturn(DispatchID);
            sp.close();
            if (isPosted) {
               obj.put("success", "true");
            } else {
               obj.put("success", "false");
               obj.put("error", "Could not post stock in store.");
            }
         }

         s1.close();
         s.close();
         ds.dropConnection();
      } catch (Exception var118) {
         try {
            ds.rollback();
         } catch (SQLException var117) {
            var117.printStackTrace();
         }

         obj.put("success", "false");
         obj.put("exception", var118);
         var118.printStackTrace();
      } finally {
         try {
            ds.dropConnection();
         } catch (SQLException var116) {
            var116.printStackTrace();
         }

      }

      out.print(obj);
      out.close();
   }

   private String getProductName(Statement s, int ProductID) throws SQLException {
      String ProductName = "";
      ResultSet rs = s.executeQuery("SELECT concat(package_label, ' ', brand_label) product_name FROM pep.inventory_products_view where product_id=" + ProductID);
      if (rs.first()) {
         ProductName = rs.getString("product_name");
      }

      return ProductName;
   }

   private boolean isExtraValid(long DispatchID, int ProductID, long TotalUnits, Statement s) throws SQLException {
      boolean isValid = false;
      long AvailableUnits = 0L;
      ResultSet rs2 = s.executeQuery("select ipv.sap_code, ipv.package_label, ipv.brand_label, ipv.unit_per_sku, ep.product_id, sum(case when type='e' then ep.units else 0 end) extra_units, sum(case when type='a' then ep.units else 0 end) adjusted_units from (SELECT isdep.product_id, sum(isdep.total_units) units, 'e' type FROM inventory_sales_dispatch_extra_products isdep where isdep.dispatch_id = " + DispatchID + " group by isdep.product_id union all select isip.product_id, sum(isip.total_units) units, 'a' type from inventory_sales_dispatch_extra_invoices isdei join inventory_sales_invoices_products isip on isdei.sales_id = isip.id where isdei.id = " + DispatchID + " group by isip.product_id" + ") ep join inventory_products_view ipv on ep.product_id = ipv.product_id and ep.product_id = " + ProductID + " group by ep.product_id");
      if (rs2.first()) {
         AvailableUnits = rs2.getLong("extra_units") - rs2.getLong("adjusted_units");
      }

      if (TotalUnits <= AvailableUnits) {
         isValid = true;
      }

      ResultSet rs1 = s.executeQuery("select empty_product_id, ipv.sap_code, ipv.package_label, ipv.brand_label, ipv.unit_per_sku, sum(total_units-units_returned) total_units, ifnull((select sum(isip.total_units) units from inventory_sales_dispatch_extra_invoices isdei join inventory_sales_invoices_products isip on isdei.sales_id = isip.id where isdei.id = " + DispatchID + " and isip.product_id = tab1.empty_product_id),0) units_invoiced, ifnull((SELECT sum(total_units) FROM inventory_sales_dispatch_returned_products where dispatch_id=" + DispatchID + " and product_id=tab1.empty_product_id and is_empty=1),0) units_returned from ( select empty_product_id, sum(total_units) total_units, ifnull((select sum(total_units) from inventory_sales_dispatch_returned_products isdrp join inventory_products_map ipm on isdrp.product_id = ipm.product_id where isdrp.dispatch_id = " + DispatchID + " and ipm.empty_product_id = tab1.empty_product_id),0) units_returned from ( SELECT isip.product_id, ipm.empty_product_id, isip.total_units FROM inventory_sales_invoices_products isip join inventory_products_map ipm on isip.product_id=ipm.product_id where isip.id in (select sales_id from inventory_sales_dispatch_invoices where id= " + DispatchID + ") union all select isdep.product_id, ipm.empty_product_id, isdep.total_units from inventory_sales_dispatch_extra_products isdep join inventory_products_map ipm on isdep.product_id=ipm.product_id where isdep.dispatch_id = " + DispatchID + " ) tab1 group by empty_product_id ) tab1 join inventory_products_view ipv on tab1.empty_product_id = ipv.product_id and tab1.empty_product_id=" + ProductID + " group by empty_product_id");
      if (rs1.first()) {
         AvailableUnits = rs1.getLong("total_units") - rs1.getLong("units_returned");
         TotalUnits += rs1.getLong("units_invoiced");
      }

      if (TotalUnits <= AvailableUnits) {
         isValid = true;
      }

      return isValid;
   }
}
