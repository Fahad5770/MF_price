package com.pbc.cron;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.pbc.inventory.StockDocument;
import com.pbc.inventory.StockDocumentItems;
import com.pbc.inventory.StockPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class DistributorAutoGRN {
	
	public static void main(String args[]){
		
		Datasource ds = new Datasource();
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			ResultSet rs = s.executeQuery("select barcode, (select product_id from inventory_delivery_note_products where delivery_id = idn.delivery_id and product_id = 82 limit 1) is_bulk from inventory_delivery_note idn where idn.delivered_on > '2016-12-21' and idn.is_received = 0 and idn.distributor_id in (select distinct distributor_id from distributor_beat_plan) and idn.distributor_id != 200769 and ((time_to_sec(now())-time_to_sec(idn.delivered_on) > 14400) or ( to_days(curdate())-to_days(idn.delivered_on) > 0) ) having is_bulk is null");
			while(rs.next()){
				
				long barcode = rs.getLong(1);
				
				doGRNPost(barcode);
				
			}
			
			ds.dropConnection();
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
			e.printStackTrace();
		}

		
		
	}
	
	public static boolean doGRNPost(long Barcode){
		
	
		boolean isPosted = false;
		
		Datasource ds = new Datasource();
		//JSONObject obj = new JSONObject();
		StockDocument GRNStockDocument = new StockDocument();
		
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			
			
			ResultSet rs = s.executeQuery("select distributor_id, created_on, created_by, delivery_id from inventory_delivery_note where barcode="+Barcode);
			if( rs.first() ){
				
				GRNStockDocument.DISTRIBUTOR_ID = rs.getLong("distributor_id");
				GRNStockDocument.DOCUMENT_TYPE_ID = 11;
				GRNStockDocument.DOCUMENT_ID = rs.getLong("delivery_id");
				GRNStockDocument.CREATED_ON = rs.getTimestamp("created_on");
				GRNStockDocument.CREATED_BY = rs.getLong("created_by");
				
				
				ResultSet rs2 = s2.executeQuery("select idnp.product_id, idnp.raw_cases, idnp.units, idnp.total_units, idnp.liquid_in_ml from inventory_delivery_note_products idnp, inventory_products_view ipv where idnp.product_id = ipv.product_id and idnp.delivery_id = "+rs.getLong("delivery_id")+" and ipv.category_id = 1");
				while( rs2.next() ){
					
					StockDocumentItems GRNStockDocumentItems = new StockDocumentItems();
					
					GRNStockDocumentItems.PRODUCT_ID = rs2.getLong("product_id");
					GRNStockDocumentItems.RAW_CASES = rs2.getInt("raw_cases");
					GRNStockDocumentItems.UNITS = rs2.getInt("units");
					GRNStockDocumentItems.TOTAL_UNITS = rs2.getInt("total_units");
					GRNStockDocumentItems.LIQUID_IN_ML = rs2.getLong("liquid_in_ml");
					GRNStockDocumentItems.TRANSACTION_TYPE = 2;
					
					GRNStockDocument.PRODUCTS.add(GRNStockDocumentItems);
				}
				
			}
			
			StockPosting GRNStockPosting = new StockPosting();
			
			isPosted = GRNStockPosting.postStock(GRNStockDocument);
			
			GRNStockPosting.close();
			if(isPosted)
			{
				s.executeUpdate("update inventory_delivery_note set is_received=1, received_on=now(), received_by=1 where barcode="+Barcode);
				isPosted = true;
			}
			else
			{
				isPosted = false;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//obj.put("success", "false");
			//obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
			try {
				if (ds != null){
					ds.dropConnection();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return isPosted;
	}
	
}
