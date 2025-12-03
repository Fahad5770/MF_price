package com.pbc.myscripts;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.pbc.inventory.SalesPosting;
import com.pbc.util.AlmoizFormulas;
import com.pbc.util.Datasource;

public class ResolveDeskSaleInvoices {

	public static void main(String[] args)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		// TODO Auto-generated method stub

		Datasource ds = new Datasource();
		ds.createConnection();

		Statement s = ds.createStatement();
		Statement s2 = ds.createStatement();
		Statement s3 = ds.createStatement();

		
		ResultSet rsIds = s.executeQuery("select * from inventory_sales_invoices where order_id is null and created_on between '2025-08-16' and '2025-09-05' and id!=11779363");
		while (rsIds.next()) {
			double total_amount = 0.0;
			double total_WHTaxAmount = 0.0;
			double total_SalesTaxAmount = 0.0;
			double total_net_amount = 0.0;
			
			long outlet_id = rsIds.getLong("outlet_id");

			//System.out.println(rsIds.getInt("id") + "-" + outlet_id);

			ResultSet rsProducts = s2
					.executeQuery("select * from inventory_sales_invoices_products where id=" + rsIds.getInt("id"));
			while (rsProducts.next()) {
				//System.out.println(rsProducts.getInt("product_id"));

				int product_id = rsProducts.getInt("product_id");
				int quantity = rsProducts.getInt("raw_cases");
				double total_amount_product = rsProducts.getDouble("total_amount");

				HashMap<String, Double> ProductsTax = AlmoizFormulas.ProductsTax(product_id, outlet_id);

				double WHTaxAmount = ProductsTax.get("wh_tax") * quantity;
				//System.out.println(product_id +"-" + WHTaxAmount);
				total_WHTaxAmount += WHTaxAmount;
				double SalesTaxAmount = ProductsTax.get("income_tax") * quantity;
			//	System.out.println(product_id +"-" + SalesTaxAmount);
				total_SalesTaxAmount += SalesTaxAmount;
				double net_amount = total_amount_product +WHTaxAmount+SalesTaxAmount;
				
				
				total_amount += total_amount_product;
				total_net_amount += net_amount;
				//System.out.println(total_amount_product +"-" + net_amount);
				System.out.println("update inventory_sales_invoices_products set wh_tax_amount="+WHTaxAmount+", sales_tax_amount="+SalesTaxAmount+" ,net_amount="+net_amount+" where id="+rsIds.getInt("id")+" and product_id="+product_id);
				s3.executeUpdate("update inventory_sales_invoices_products set wh_tax_amount="+WHTaxAmount+", sales_tax_amount="+SalesTaxAmount+" ,net_amount="+net_amount+" where id="+rsIds.getInt("id")+" and product_id="+product_id);

			}
			//System.out.println(total_amount);
			//System.out.println(total_WHTaxAmount);
			//System.out.println(total_SalesTaxAmount);
			//System.out.println(total_net_amount);
			System.out.println("update inventory_sales_invoices set invoice_amount="+total_amount+", total_amount="+total_amount+", sales_tax_amount="+total_SalesTaxAmount+", wh_tax_amount="+total_WHTaxAmount+", net_amount="+total_net_amount+" ,wh_tax_rate=0 where id="+rsIds.getInt("id"));
			s3.executeUpdate("update inventory_sales_invoices set invoice_amount="+total_amount+", total_amount="+total_amount+", sales_tax_amount="+total_SalesTaxAmount+", wh_tax_amount="+total_WHTaxAmount+", net_amount="+total_net_amount+" ,wh_tax_rate=0 where id="+rsIds.getInt("id"));
		}
		s3.close();
		s2.close();
		s.close();
		ds.dropConnection();
	}

}
