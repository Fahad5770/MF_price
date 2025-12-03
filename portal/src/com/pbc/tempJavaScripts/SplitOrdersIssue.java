package com.pbc.tempJavaScripts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;

public class SplitOrdersIssue {

	public static void main(String[] args) {
		Datasource ds = null;
		Statement s = null;
		Statement s2 = null;
		try {
			ds = new Datasource();
			ds.createConnection();
			//ds.startTransaction();
			s = ds.createStatement();
			s2 = ds.createStatement();
			int id=9691;
			ResultSet rsUneditedOrders = s.executeQuery("select id from mobile_order_unedited where created_by="+id+" and id not in(select unedited_order_id from mobile_order where created_by="+id+" and created_on between '2024-01-18' and '2024-01-19') and created_on between '2024-01-18' and '2024-01-19' order by created_on desc");
			while(rsUneditedOrders.next()) {
			long OrderID = rsUneditedOrders.getLong("id");
		System.out.println("SalesPosting.splitOrder(OrderID)" + OrderID);
		SalesPosting.splitOrder(OrderID);
			}
	}catch (Exception e){
		    
		    e.printStackTrace();
		   } finally{
		    try {
		     if (s != null){
		      s.close();
		      ds.dropConnection();
		     }
		    } catch (SQLException e) {
		     e.printStackTrace();
		    }    
		   }
	}

}
