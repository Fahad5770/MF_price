package com.pbc.myscripts;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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

import com.pbc.bi.BiProcesses;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;

public class UpdateOutletWHTax_Moiz {
	static Datasource ds = new Datasource();

	public static void main(String[] args) {
		try {

			CalculateTaxInInvoicesTable();

		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	
	private static void CalculateTaxInInvoicesTable() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		ds.createConnection();
		// ds.startTransaction();

		Statement s = ds.createStatement();
		Statement s2 = ds.createStatement();
		
		long InvoiceID =10303763;
		
		//double WHTTaxRate =0.5;
		
		double WHTTaxRate =0;
		
		double TotalWHTAmount=0;
		double TotalAmount=0;
		
		ResultSet rs = s.executeQuery("select * from inventory_sales_invoices_products where id="+InvoiceID);
		while(rs.next()) {
			int ProductID = rs.getInt("product_id");
			long RawCases = rs.getLong("raw_cases");
			int RateRawCase = rs.getInt("rate_raw_cases");
			double PTotalAmount = rs.getDouble("total_amount"); 
			
			double ProductWHTaxAmount = (RawCases*RateRawCase)*(WHTTaxRate/100);
			
			double ProductNetAMount = PTotalAmount+ProductWHTaxAmount;
			
			System.out.println("update inventory_sales_invoices_products set wh_tax_amount="+ProductWHTaxAmount+", net_amount="+ProductNetAMount+" where id="+InvoiceID+" and product_id="+ProductID);
			
			s2.executeUpdate("update inventory_sales_invoices_products set wh_tax_amount="+ProductWHTaxAmount+", net_amount="+ProductNetAMount+" where id="+InvoiceID+" and product_id="+ProductID);
			
			
			TotalWHTAmount += ProductWHTaxAmount;
			TotalAmount += ProductNetAMount;
		}
		
		double TotalTotalAmount = TotalWHTAmount+TotalAmount;
		double TotalTotalNetAmount = Math.ceil(TotalTotalAmount);
		
		System.out.println("update inventory_sales_invoices set wh_tax_amount="+TotalWHTAmount+", total_amount="+TotalTotalAmount+", net_amount="+TotalTotalNetAmount+" where id="+InvoiceID);
		
		s.executeUpdate("update inventory_sales_invoices set wh_tax_amount="+TotalWHTAmount+", total_amount="+TotalTotalAmount+", net_amount="+TotalTotalNetAmount+" where id="+InvoiceID);
		
		
		s2.close();
		s.close();
		ds.dropConnection();
	}
	
	
	private void CalculateTaxInAdjustedTable() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		ds.createConnection();
		// ds.startTransaction();

		Statement s = ds.createStatement();
		Statement s2 = ds.createStatement();
		
		long InvoiceID =10303763;
		
		//double WHTTaxRate =0.5;
		
		double WHTTaxRate =0;
		
		double TotalWHTAmount=0;
		double TotalAmount=0;
		
		ResultSet rs = s.executeQuery("select * from inventory_sales_invoices_products where id="+InvoiceID);
		while(rs.next()) {
			int ProductID = rs.getInt("product_id");
			long RawCases = rs.getLong("raw_cases");
			int RateRawCase = rs.getInt("rate_raw_cases");
			double PTotalAmount = rs.getDouble("total_amount"); 
			
			double ProductWHTaxAmount = (RawCases*RateRawCase)*(WHTTaxRate/100);
			
			double ProductNetAMount = PTotalAmount+ProductWHTaxAmount;
			
			System.out.println("update inventory_sales_adjusted_products set wh_tax_amount="+ProductWHTaxAmount+", net_amount="+ProductNetAMount+" where id="+InvoiceID+" and product_id="+ProductID);
			
			s2.executeUpdate("update inventory_sales_adjusted_products set wh_tax_amount="+ProductWHTaxAmount+", net_amount="+ProductNetAMount+" where id="+InvoiceID+" and product_id="+ProductID);
			
			
			TotalWHTAmount += ProductWHTaxAmount;
			TotalAmount += ProductNetAMount;
		}
		
		double TotalTotalAmount = TotalWHTAmount+TotalAmount;
		double TotalTotalNetAmount = Math.ceil(TotalTotalAmount);
		
		System.out.println("update inventory_sales_adjusted set wh_tax_amount="+TotalWHTAmount+", total_amount="+TotalTotalAmount+", net_amount="+TotalTotalNetAmount+" where id="+InvoiceID);
		
		s.executeUpdate("update inventory_sales_adjusted set wh_tax_amount="+TotalWHTAmount+", total_amount="+TotalTotalAmount+", net_amount="+TotalTotalNetAmount+" where id="+InvoiceID);
		
		
		s2.close();
		s.close();
		ds.dropConnection();
	}

}
