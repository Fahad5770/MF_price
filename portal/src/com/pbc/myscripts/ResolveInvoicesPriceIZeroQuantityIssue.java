package com.pbc.myscripts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.inventory.Product;
import com.pbc.util.AlmoizFormulas;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;

public class ResolveInvoicesPriceIZeroQuantityIssue {
	static Datasource ds = new Datasource();

	public static void main(String[] args) {

		String[] OrderIDs = { "10457871" };
	

		try {
			
			ds.createConnection();
			ds.startTransaction();

			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			Statement s4 = ds.createStatement();

			// for (String orderId : OrderIDs) {
			ResultSet rsOrderIds = s.executeQuery("select id from mobile_order where id in (" + 
					"select id from mobile_order_products where rate_raw_cases=0) and mobile_timestamp between '2025-10-01' and '2025-10-30'");
while(rsOrderIds.next()) {
					String orderId = rsOrderIds.getString("id");
					// outletId = outletId+"33";
				//	System.out.println(i + " OrderID : " + orderId);

					System.out.println("select outlet_id, unedited_order_id from mobile_order where id=" + orderId);
					ResultSet rsOrder = s2
							.executeQuery("select outlet_id,unedited_order_id from mobile_order where id=" + orderId);
					if (rsOrder.first()) {

						long outlet_id = rsOrder.getLong("outlet_id");
						long unedited_order_id = rsOrder.getLong("unedited_order_id");

						ResultSet rsInvoiceID = s3
								.executeQuery("select id from inventory_sales_invoices where order_id=" + orderId);
						long invoice_id = (rsInvoiceID.first()) ? rsInvoiceID.getLong("id") : 0;

						double TotalInvoiceAmount = 0, TotalInvoiceWHTaxAmount = 0, TotalInvoiceSalesTaxAmount = 0,
								TotalInvoiceNetAmount = 0, TotalPriceDiscount = 0, TotalSpotDiscountAmount = 0,
								TotalInvoiveNetAmount = 0;

						System.out
								.println(
										"select product_id, raw_cases, is_promotion, promotion_id  from mobile_order_products where id=" + orderId);
						ResultSet rsOrderProducts = s3.executeQuery(
								"select product_id, raw_cases, is_promotion, promotion_id  from mobile_order_products where id=" + orderId);
						while (rsOrderProducts.next()) {
							int product_id = rsOrderProducts.getInt("product_id");
							int quantity = rsOrderProducts.getInt("raw_cases");
							int is_promotion = rsOrderProducts.getInt("is_promotion");
							String promotion_id = (is_promotion == 0) ? null : rsOrderProducts.getString("promotion_id");
							if(is_promotion == 0) {
							int UnitsPerSKU = 0;
							long LiquidInMLPerUnit = 0;
							System.out.println(
									"SELECT unit_per_sku, liquid_in_ml FROM inventory_products_view where product_id = "
											+ product_id);
							ResultSet rs_product = s4.executeQuery(
									"SELECT unit_per_sku, liquid_in_ml FROM inventory_products_view where product_id = "
											+ product_id);
							if (rs_product.first()) {
								UnitsPerSKU = rs_product.getInt(1);
								LiquidInMLPerUnit = rs_product.getLong(2);
							}

							int TotalUnits = (quantity * UnitsPerSKU);

							long LiquidinML = LiquidInMLPerUnit * TotalUnits;

							double UnitRates[] = Product.getSellingPrice_2(product_id, outlet_id);
							double RateRawCase = UnitRates[0];
							double RateUnit = UnitRates[1];
							double price_discount = UnitRates[2];

							double AmountRawCases = RateRawCase * quantity;
							double AmountUnits = RateUnit * quantity;
							double SpotDiscountPercentage = 0;
							double SpotDiscountAmount = ((SpotDiscountPercentage * AmountRawCases) / 100);

							HashMap<String, Double> ProductsTax = AlmoizFormulas.ProductsTax(product_id, outlet_id);

							double WHTaxAmount = Utilities.parseDouble(
									Utilities.getDisplayCurrencyFormatSimple((ProductsTax.get("wh_tax") * quantity)));

							double SalesTaxAmount = Utilities.parseDouble(Utilities
									.getDisplayCurrencyFormatSimple((ProductsTax.get("income_tax") * quantity)));
							double TotalNetAmount = Utilities.parseDouble(
									Utilities.getDisplayCurrencyFormatSimple((AmountRawCases - SpotDiscountAmount)));
							double InvoiveNetAmount = Utilities.parseDouble(Utilities
									.getDisplayCurrencyFormatSimple((TotalNetAmount + WHTaxAmount + SalesTaxAmount)));

							String internal_products = " set raw_cases=" + quantity + ", total_units=" + TotalUnits
									+ ", rate_raw_cases=" + RateRawCase + ", rate_units=" + RateUnit + ", liquid_in_ml="
									+ LiquidinML + ", amount_raw_cases=" + AmountRawCases + ",amount_units="
									+ AmountUnits + ",total_amount=" + TotalNetAmount + ", price_discount="
									+ price_discount + ", discount=" + SpotDiscountPercentage + ", wh_tax_amount="
									+ WHTaxAmount + ",sales_tax_amount=" + SalesTaxAmount + ", net_amount="
									+ InvoiveNetAmount+ ", is_promotion="+is_promotion+", promotion_id="+promotion_id;

							// update Mobile Order Products
							System.out.println("update mobile_order_products " + internal_products
									+ " where product_id=" + product_id + " and id=" + orderId);
							s4.executeUpdate("update mobile_order_products " + internal_products + " where product_id="
									+ product_id + " and id=" + orderId);

							// update Mobile Order Unedited Products
							System.out.println("update mobile_order_unedited_products " + internal_products
									+ " where product_id=" + product_id + " and id=" + unedited_order_id);
							s4.executeUpdate("update mobile_order_unedited_products " + internal_products
									+ " where product_id=" + product_id + " and id=" + unedited_order_id);

							// update Mobile Order invoices Products
							System.out.println("update inventory_sales_invoices_products " + internal_products
									+ " where product_id=" + product_id + " and id=" + invoice_id);
							s4.executeUpdate("update inventory_sales_invoices_products " + internal_products
									+ " where product_id=" + product_id + " and  id=" + invoice_id);

							// update Adjusted Products Products
							System.out.println("update inventory_sales_adjusted_products " + internal_products
									+ " where product_id=" + product_id + " and  id=" + invoice_id);
							s4.executeUpdate("update inventory_sales_adjusted_products " + internal_products
									+ " where product_id=" + product_id + " and  id=" + invoice_id);

							
							TotalInvoiceAmount += AmountRawCases;
							TotalPriceDiscount += price_discount;
							TotalSpotDiscountAmount += SpotDiscountAmount;
							TotalInvoiceNetAmount += TotalNetAmount;
							TotalInvoiveNetAmount += InvoiveNetAmount;
							TotalInvoiceWHTaxAmount = WHTaxAmount;
							TotalInvoiceSalesTaxAmount = SalesTaxAmount;
							}
						} // end of mobile order loop

						String InoviceTotalAmountString = TotalInvoiveNetAmount + "";

						if (InoviceTotalAmountString.indexOf(".") != -1) {
							double Fraction = Utilities.parseDouble(InoviceTotalAmountString.substring(
									InoviceTotalAmountString.indexOf("."), InoviceTotalAmountString.length()));

							InoviceTotalAmountString = InoviceTotalAmountString.substring(0,
									InoviceTotalAmountString.indexOf("."));

							if (Fraction != 0) {
								InoviceTotalAmountString = (Utilities.parseInt(InoviceTotalAmountString) + 1) + "";
							}
						}
						double FractionAmount = Utilities.parseDouble(InoviceTotalAmountString) - TotalInvoiveNetAmount;
						// System.out.println(InoviceTotalAmountString);
						// System.out.println(TotalInvoiveNetAmount);

						String internal = "set invoice_amount = " + TotalInvoiceAmount + ", sales_tax_amount  = "
								+ TotalInvoiceSalesTaxAmount + ", wh_tax_amount = " + TotalInvoiceWHTaxAmount
								+ ", total_amount = " + TotalInvoiceNetAmount + ", fraction_adjustment = "
								+ FractionAmount + ", net_amount = " + InoviceTotalAmountString + ", price_discount="
								+ TotalPriceDiscount + ", spot_discount_amount=" + TotalSpotDiscountAmount;

						// Update Mobile Order Unedited prices
						System.out.println(
								"update mobile_order_unedited " + internal + " where id = " + unedited_order_id);
						s3.executeUpdate(
								"update mobile_order_unedited " + internal + " where id = " + unedited_order_id);

						// Update Mobile Order prices
						System.out.println("update mobile_order " + internal + " where id = " + orderId);
						s3.executeUpdate("update mobile_order " + internal + " where id = " + orderId);

						// Update Invoices prices
						System.out.println("update inventory_sales_invoices " + internal + " where id = " + invoice_id);
						s3.executeUpdate("update inventory_sales_invoices " + internal + " where id = " + invoice_id);

						// Update Adjusted prices
						System.out.println("update inventory_sales_adjusted " + internal + " where id = " + invoice_id);
						s3.executeUpdate("update inventory_sales_adjusted " + internal + " where id = " + invoice_id);

					}

			
			} // emd of while loop

			s2.close();
			s.close();
			ds.commit();
			ds.dropConnection();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
