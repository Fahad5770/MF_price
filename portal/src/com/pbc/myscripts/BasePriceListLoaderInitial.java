package com.pbc.myscripts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.pbc.inventory.Product;
import com.pbc.util.Datasource;

public class BasePriceListLoaderInitial {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	///	InventoryPriceListBaseProducts();
		try {
			double UnitRates[] = Product.getSellingPrice_2(10, 137104570);
			System.out.println(UnitRates[0]);
			System.out.println(UnitRates[1]);
			System.out.println(UnitRates[2]);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	
	
private static void InventoryPriceListBaseProducts() {
		
		Datasource ds=new Datasource();
		try {
			ds.createConnection();
			Statement s=ds.createStatement();
			Statement s1=ds.createStatement();
			
			
			ResultSet rs=s.executeQuery("select * from inventory_price_list_products where id=29");
			
			while(rs.next()) {
				
				
				
				long ProductID=rs.getLong("product_id");
				long RawCases=rs.getLong("raw_case");
				double Units=rs.getDouble("unit");
				
				//String StartDate="2020-10-29";
				//String EndDate="2021-10-30";
				
				s1.executeUpdate("insert into inventory_price_list_products_base(product_id,raw_case,unit,created_on,created_by,start_date,end_date)values("+ProductID+","+RawCases+","+Units+",now(),2593,'2021-01-01','2999-12-31')");
				//System.out.println("insert into inventory_price_list_products_base(product_id,raw_case,unit,created_on,created_by,start_date,end_date)values("+ProductID+","+RawCases+","+Units+",now(),2593,'"+StartDate+"','"+EndDate+"')");

				
				
				
			}
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
