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

public class UpdateOutletSugarWHTax_Moiz {
	static Datasource ds = new Datasource();

	public static void main(String[] args) {
		try {

			long InvoiceID[] = {10358899,10358897,10358895,10358896,10359180,10358883,10358886,10359179,10359367,10359360,10359177,10359359,10358885,10358900,10358898};
			for(int i=0;i<InvoiceID.length;i++) {
				CalculateTaxInInvoicesTable(InvoiceID[i]);
			}
			
			

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
	
	private static void CalculateTaxInInvoicesTable(long InvID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		ds.createConnection();
		// ds.startTransaction();

		Statement s = ds.createStatement();
		Statement s2 = ds.createStatement();
		
		long InvoiceID =InvID;
		
		long MobileOrderID=0;
		ResultSet rs = s.executeQuery("select * from inventory_sales_invoices where id="+InvoiceID);
		if(rs.first()) {
			MobileOrderID=rs.getLong("order_id");
		}
		
		ResultSet rs2 = s.executeQuery("select * from mobile_order_products where id="+MobileOrderID);
		while(rs2.next()) {
			double WHTAmount=rs2.getDouble("wh_tax_amount");
			double NetAmount=rs2.getDouble("net_amount");
			
			
		//	System.out.println("update inventory_sales_invoices_products set wh_tax_amount="+WHTAmount+",net_amount="+NetAmount+" where id="+InvoiceID);
			//System.out.println("update inventory_sales_adjusted_products set wh_tax_amount="+WHTAmount+",net_amount="+NetAmount+" where id="+InvoiceID);
			
			s2.executeUpdate("update inventory_sales_invoices_products set wh_tax_amount="+WHTAmount+",net_amount="+NetAmount+" where id="+InvoiceID);
			s2.executeUpdate("update inventory_sales_adjusted_products set wh_tax_amount="+WHTAmount+",net_amount="+NetAmount+" where id="+InvoiceID);
			
		}
		
		
		//Master Table Update
		ResultSet rs3 = s.executeQuery("select * from mobile_order where id="+MobileOrderID);
		if(rs3.first()) {
			double MasterWHTAmount=rs3.getDouble("wh_tax_amount");
			double MasterNetAmount=rs3.getDouble("net_amount");
			
			
			System.out.println("update inventory_sales_invoices set wh_tax_amount="+MasterWHTAmount+",net_amount="+MasterNetAmount+" where id="+InvoiceID);
			///System.out.println("update inventory_sales_adjusted_products set wh_tax_amount="+MasterNetAmount+",net_amount="+MasterNetAmount+" where id="+InvoiceID);
			
			s2.executeUpdate("update inventory_sales_invoices set wh_tax_amount="+MasterWHTAmount+",net_amount="+MasterNetAmount+"  where id="+InvoiceID);
			s2.executeUpdate("update inventory_sales_adjusted set wh_tax_amount="+MasterNetAmount+",net_amount="+MasterNetAmount+" where id="+InvoiceID);
			
		}
		
		
		
		s2.close();
		s.close();
		ds.dropConnection();
	}
	
	
	

}
