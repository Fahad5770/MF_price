package com.pbc.bi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import com.pbc.util.Utilities;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import org.apache.commons.lang3.time.DateUtils;

public class InventorySalesAdjustment {

	public static void main(String[] args) {
		
		try{
			SalesAdjustments(); // Fetch from SAP, breakup
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public static void SalesAdjustments() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		Datasource ds = new Datasource();
		
		Datasource dsMaster = new Datasource();
		
		try{
			
			Date d = new Date();
			
			
			
			Date StartDate = Utilities.getDateByDays(d, -112);
			Date EndDate = new Date();
			
			ds.createConnectionToReplica();
			dsMaster.createConnection();
			
			
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			Statement s4 = ds.createStatement();
			
			Statement sMaster = dsMaster.createStatement();
			
			//System.out.println("Started!!!!");
			
			//Date Loop
			
			Date CurrentDate = StartDate;
			
			while(true){
				
				System.out.println(Utilities.getDisplayDateFormat(CurrentDate));
				
				ResultSet rs12 = s4.executeQuery("SELECT distinct outlet_id FROM inventory_sales_adjusted where created_on between date("+Utilities.getSQLDate(CurrentDate)+") and date("+Utilities.getSQLDateNext(CurrentDate)+")");
				while(rs12.next()){
				
				long SamplingID=0;
				long OutletID=0;
				long PackageID=0;
				long BrandID=0;
				double MarketShare=0;
				
				ResultSet rs = s.executeQuery("select s.sampling_id, s.outlet_id, sp.package, sp.brand_id, sp.company_share from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where active = 1 and date("+Utilities.getSQLDate(CurrentDate)+") between s.activated_on and s.deactivated_on and  date("+Utilities.getSQLDate(CurrentDate)+") between sp.valid_from and sp.valid_to and s.outlet_id="+rs12.getLong("outlet_id"));
				while(rs.next()){
					 SamplingID=rs.getLong("sampling_id");
					 OutletID=rs.getLong("outlet_id");
					 PackageID=rs.getLong("package");
					 BrandID=rs.getLong("brand_id");
					 MarketShare=rs.getDouble("company_share");
					 
					 if(BrandID==0){
						 //System.out.println("BrandID is 0");
						 ResultSet rs1 = s2.executeQuery("select * from inventory_sales_adjusted_products isap join inventory_products_view ipv on isap.product_id = ipv.product_id where id in (select id from inventory_sales_adjusted where created_on between date("+Utilities.getSQLDate(CurrentDate)+") and date("+Utilities.getSQLDateNext(CurrentDate)+") and outlet_id = "+OutletID+") and ipv.package_id ="+PackageID);
						 while(rs1.next()){
							 //System.out.println("Updated - "+SamplingID+" - "+MarketShare);
							 //System.out.println("update inventory_sales_adjusted_products set per_case_discount_approval_id="+SamplingID+", per_case_rate="+MarketShare+" where id="+rs1.getLong("id")+" and product_id="+rs1.getLong("product_id")+" and is_promotion=0");
							 sMaster.executeUpdate("update inventory_sales_adjusted_products set per_case_discount_approval_id="+SamplingID+", per_case_rate="+MarketShare+" where id="+rs1.getLong("id")+" and product_id="+rs1.getLong("product_id")+" and is_promotion=0");
							 
						 }
						 
					 }else if(BrandID!=0){
						// System.out.println("BrandID is not 0");
						 ResultSet rs1 = s2.executeQuery("select * from inventory_sales_adjusted_products isap join inventory_products_view ipv on isap.product_id = ipv.product_id where id in (select id from inventory_sales_adjusted where created_on between date("+Utilities.getSQLDate(CurrentDate)+") and date("+Utilities.getSQLDateNext(CurrentDate)+") and outlet_id = "+OutletID+") and ipv.package_id ="+PackageID+" and ipv.brand_id="+BrandID);
						 while(rs1.next()){
							 //System.out.println("Updated - "+SamplingID+" - "+MarketShare+" - "+BrandID);
							 //System.out.println("update inventory_sales_adjusted_products set per_case_discount_approval_id="+SamplingID+", per_case_rate="+MarketShare+" where id="+rs1.getLong("id")+" and product_id="+rs1.getLong("product_id")+" and is_promotion=0");
							 sMaster.executeUpdate("update inventory_sales_adjusted_products set per_case_discount_approval_id="+SamplingID+", per_case_rate="+MarketShare+" where id="+rs1.getLong("id")+" and product_id="+rs1.getLong("product_id")+" and is_promotion=0");
						 }
					 }
					 
				}
				}
				
				
				if(DateUtils.isSameDay(CurrentDate, EndDate)){
					break;
				}
				
				CurrentDate = Utilities.getDateByDays(CurrentDate, 1);
			}
				
			
			//System.out.println("Ended!!!!");
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			ds.dropConnection();
			
		}
	}

	
	
}
