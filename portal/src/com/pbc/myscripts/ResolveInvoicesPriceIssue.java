package com.pbc.myscripts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import com.pbc.inventory.Product;
import com.pbc.util.AlmoizFormulas;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class ResolveInvoicesPriceIssue {
	static Datasource ds = new Datasource();

	public static void main(String[] args) {

		String InvoiceID = "11791977";

		try {

			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();

			long outlet_id = 0, order_id = 0;

			double total_amount = 0, total_wh_tax = 0, total_sales_tax = 0, total_net_amount = 0, total_discount = 0;

			System.out.println(
					"select outlet_id, order_id from inventory_sales_invoices where id in (" + InvoiceID + ")");
			ResultSet rsInvoice = s.executeQuery(
					"select outlet_id, order_id from inventory_sales_invoices where id in (" + InvoiceID + ")");
			if (rsInvoice.first()) {
				outlet_id = rsInvoice.getLong("outlet_id");
				order_id = rsInvoice.getLong("order_id");
			}

			System.out.println("outlet ID : " + outlet_id);
			System.out.println("Order ID : " + order_id);

			// for (String orderId : OrderIDs) {
			System.out.println("select  product_id, raw_cases from inventory_sales_invoices_products where id in ("
					+ InvoiceID + ") ");
			ResultSet rsInvoiceProducts = s
					.executeQuery("select  product_id, raw_cases, (select unit_per_sku from inventory_products_view ipv where ipv.product_id=isip.product_id) unit_per_sku from inventory_sales_invoices_products isip where id in ("
							+ InvoiceID + ") ");
			while (rsInvoiceProducts.next()) {

				int product_id = rsInvoiceProducts.getInt("product_id");
				int quantity = rsInvoiceProducts.getInt("raw_cases");
				int UnitsPerSKU = rsInvoiceProducts.getInt("unit_per_sku");
				
				int TotalUnits = (quantity * UnitsPerSKU) + 0;

				double UnitRates[] = Product.getSellingPrice_2(product_id, outlet_id);
				double RateRawCase = UnitRates[0];
				double RateUnit = UnitRates[1];
				double price_discount = UnitRates[2];

				HashMap<String, Double> ProductsTax = AlmoizFormulas.ProductsTax(product_id, outlet_id);
				double wh_tax_rate = ProductsTax.get("wh_tax");
				double sales_tax_rate = ProductsTax.get("income_tax");

				double amount = Utilities
						.parseDouble(Utilities.getDisplayCurrencyFormatSimple((double) quantity * RateRawCase));
				double wh_tax_amount = Utilities
						.parseDouble(Utilities.getDisplayCurrencyFormatSimple((double) quantity * wh_tax_rate));
				double sales_tax_amount = Utilities
						.parseDouble(Utilities.getDisplayCurrencyFormatSimple((double) quantity * sales_tax_rate));
				double discount_amount = Utilities
						.parseDouble(Utilities.getDisplayCurrencyFormatSimple((double) quantity * price_discount));
				double net_amount = Utilities.parseDouble(
						Utilities.getDisplayCurrencyFormatSimple(amount + sales_tax_amount + wh_tax_amount));

				total_amount += amount;
				total_wh_tax += wh_tax_amount;
				total_sales_tax += sales_tax_amount;
				total_discount += discount_amount;
				total_net_amount += net_amount;

				System.out.println("------------------------------------------------------------------------");

				String product_params = " total_units="+TotalUnits+", rate_raw_cases=" + RateRawCase + ", rate_units=" + RateUnit + ", amount_raw_cases="
						+ amount + ", amount_units=" + amount + ", total_amount=" + amount + ", hand_discount_rate="
						+ price_discount + ", hand_discount_amount=" + discount_amount + ", wh_tax_amount="
						+ wh_tax_amount + ", sales_tax_amount=" + sales_tax_amount + ", net_amount=" + net_amount;

				System.out.println("update mobile_order_products set " + product_params + " where id=" + order_id
						+ " and product_id=" + product_id);
				s2.executeUpdate("update mobile_order_products set " + product_params + "  where id=" + order_id
						+ " and product_id=" + product_id);

				System.out.println("update inventory_sales_invoices_products set " + product_params + " where id="
						+ InvoiceID + " and product_id=" + product_id);
				s2.executeUpdate("update inventory_sales_invoices_products set " + product_params + " where id="
						+ InvoiceID + " and product_id=" + product_id);

				System.out.println("update inventory_sales_adjusted_products set " + product_params + " where id="
						+ InvoiceID + " and product_id=" + product_id);
				s2.executeUpdate("update inventory_sales_adjusted_products set " + product_params + " where id="
						+ InvoiceID + " and product_id=" + product_id);

				// System.out.println("Product ID : " + product_id);
				// System.out.println("Product Rate : " + RateRawCase);
				// System.out.println("Product Rate Unit : " + RateUnit);
				// System.out.println("Product Price Discount : " + price_discount);
				// System.out.println("Product WH Tax : " + wh_tax_rate);
				// System.out.println("Product Sales Tax : " + sales_tax_rate);
				// System.out.println("Product Quantity : " + quantity);
				//
				// System.out.println("Product Total Amount : " + amount);
				// System.out.println("Product Total Discount : " + discount_amount);
				// System.out.println("Product Total WH Tax Amount : " + wh_tax_amount);
				// System.out.println("Product Total Sales Tax Amount : " + wh_tax_amount);
				// System.out.println("Product Total Net Amount : " + net_amount);

				System.out.println();

				System.out.println("------------------------------------------------------------------------");

			} // emd of while loop

			System.out.println("----------------------------- Total -------------------------------------------");

			// System.out.println("Product Total Amount : " + total_amount);
			// System.out.println("Product Total Discount : " + total_discount);
			// System.out.println("Product Total WH Tax Amount : " + total_wh_tax);
			// System.out.println("Product Total Sales Tax Amount : " + total_sales_tax);
			// System.out.println("Product Total Net Amount : " + total_net_amount);
			String InoviceTotalAmountString = Utilities
					.parseDouble(Utilities.getDisplayCurrencyFormatSimple(total_net_amount)) + "";

			if (InoviceTotalAmountString.indexOf(".") != -1) {
				double Fraction = Utilities.parseDouble(InoviceTotalAmountString
						.substring(InoviceTotalAmountString.indexOf("."), InoviceTotalAmountString.length()));

				InoviceTotalAmountString = InoviceTotalAmountString.substring(0, InoviceTotalAmountString.indexOf("."));

				if (Fraction != 0) {
					InoviceTotalAmountString = (Utilities.parseInt(InoviceTotalAmountString) + 1) + "";
				}
			}
			double FractionAmount = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(
					Utilities.parseDouble(InoviceTotalAmountString) - total_net_amount));
			String final_params = " sales_tax_amount=" + total_sales_tax + ", wh_tax_amount=" + total_wh_tax
					+ ", total_amount=" + total_amount + ", fraction_adjustment=" + FractionAmount + ", net_amount="
					+ total_net_amount;

			System.out.println("update mobile_order set " + final_params + " where id=" + order_id);
			s.executeUpdate("update mobile_order set " + final_params + " where id=" + order_id);

			System.out.println("update inventory_sales_invoices set " + final_params + " where id=" + InvoiceID);
			s.executeUpdate("update inventory_sales_invoices set " + final_params + " where id=" + InvoiceID);

			System.out.println("update inventory_sales_adjusted set " + final_params + " where id=" + InvoiceID);
			s.executeUpdate("update inventory_sales_adjusted set " + final_params + " where id=" + InvoiceID);

			System.out.println("------------------------------------------------------------------------");

			s2.close();
			s.close();
			ds.dropConnection();

		} catch (Exception e) {
			System.out.println(e);
			// TODO Auto-generated catch block
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println(e1);
			}
			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}

	}

}
