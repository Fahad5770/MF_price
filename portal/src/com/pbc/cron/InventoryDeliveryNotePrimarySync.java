package com.pbc.cron;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class InventoryDeliveryNotePrimarySync {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		CheckForMultipleDistributor();
	}

	public static void CheckForMultipleDistributor() {
		
		Date CurrentDate = new Date();
		Date YesteryDate=Utilities.getDateByDays(-7);
		
		
		Datasource ds=new Datasource();
		
		try {
			ds.createConnection();
			Statement s=ds.createStatement();
			Statement s1=ds.createStatement();
			

			System.out.println("select * from OGP_Header opgh  where opgh.OGP_Date between "+Utilities.getSQLDate(YesteryDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and opgh.OGP_Id not in (select opg_id from inventory_delivery_note where opg_id is not null)");
			ResultSet rs=s.executeQuery("select * from OGP_Header opgh  where opgh.OGP_Date between "+Utilities.getSQLDate(YesteryDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and opgh.OGP_Id not in (select opg_id from inventory_delivery_note where opg_id is not null)");
			////ResultSet rs=s.executeQuery("select * from OGP_Header opgh where opgh.ogp_id='AIL2-MF-OGP-21-00607'");
			while(rs.next()) {
				String OPGID=rs.getString("ogp_id");
				
				ResultSet rs2 = s1.executeQuery("SELECT distinct customer_id FROM pep.OGP_Detail where OGP_Id='"+OPGID+"'"); //checking if one distributor or two
				while(rs2.next()) {
					
					String CustomerID=rs2.getString("Customer_Id");
					InsertInventoryDeliveryNotes(OPGID,CustomerID);
					
				}
			}
			
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	public static void InsertInventoryDeliveryNotes(String OPGID, String CustomerID) {
		
		
	
		
		
		
		Datasource ds=new Datasource();
		
		try {
			ds.createConnection();
			//ds.startTransaction();
			Statement s=ds.createStatement();
			Statement s1=ds.createStatement();
			Statement s2=ds.createStatement();
			Statement s3=ds.createStatement();
			Statement s4=ds.createStatement();
			
			long UserID=1;  // System User
			
			System.out.println("select * from OGP_Header opgh join OGP_Detail opgd on opgh.OGP_Id=opgd.OGP_Id where opgh.OGP_Id='"+OPGID+"' and opgd.Customer_Id='"+CustomerID+"'");
			ResultSet rs=s.executeQuery("select * from OGP_Header opgh join OGP_Detail opgd on opgh.OGP_Id=opgd.OGP_Id where opgh.OGP_Id='"+OPGID+"' and opgd.Customer_Id='"+CustomerID+"'");
			
			while (rs.next()){
				
				///String OPGID=rs.getString("OGP_Id");
				
				String OGPDate=rs.getString("OGP_Date");
				
				String VehicleNo=rs.getString("Vehicle_Num");
				
				/////String CustomerID=rs.getString("Customer_Id");
				
				
				String TransporterID=rs.getString("Transporter_Id");
				String TransporterName=rs.getString("Transporter_Name");
				
				String OGPExistDate=rs.getString("OGP_ExistDate");
				
				long ItemID=rs.getLong("Item_id");
				
				String ItemVariantIDStr=rs.getString("Variant_id");
				long ItemVariantID=0;
				if(!ItemVariantIDStr.equals("")) {
					ItemVariantID=Utilities.parseLong(ItemVariantIDStr.split("-")[1]);					
				}
				double Qty=rs.getDouble("Qty"); 
				
				
				
				
				
				
				long LiquidInMLPerCase=0;				
				int ProductID=0;				
				double UnitPerSku=0;
				
				if(ItemVariantID==0) { //mean no variant id so just fetch against item id code
					ResultSet rs2 = s2.executeQuery("SELECT * FROM pep.inventory_products ip join inventory_products_view ipv on ip.id=ipv.product_id where ip.item_ax_code in ("+ItemID+")");
					if(rs2.first()) {
						LiquidInMLPerCase=rs2.getLong("liquid_in_ml");					
						ProductID=rs2.getInt("product_id");					
						UnitPerSku=rs2.getDouble("unit_per_sku");
					}
				}else if(ItemVariantID>0) {
					ResultSet rs2 = s2.executeQuery("SELECT * FROM pep.inventory_products ip join inventory_products_view ipv on ip.id=ipv.product_id where ip.item_ax_code in ("+ItemID+") and ip.item_variant_code in ("+ItemVariantID+")");
					if(rs2.first()) {
						LiquidInMLPerCase=rs2.getLong("liquid_in_ml");					
						ProductID=rs2.getInt("product_id");					
						UnitPerSku=rs2.getDouble("unit_per_sku");
					}
				}
				
				
				/* Special Case discussed with Saad and Sharukh sb for this on 06/07/2022  = Saad said for this time only we need to tag these 2 SKUs on older skus of theia hardcoded*/
				/*
				 * 
				 * Gr Jar => 

					AIL-617844 => 9000001 

					AIL-552283 => 10011301
				 * 
				 */
				
				if(ItemID==10011303) {
					if(ItemVariantID==617844) {
						LiquidInMLPerCase=300;					
						ProductID=18;					
						UnitPerSku=1;
					}else if(ItemVariantID==552283) {
						LiquidInMLPerCase=300;					
						ProductID=13;					
						UnitPerSku=1;
					}
				}
				
				/////////////////////////////////////////////////////////////////////////////
				
				
			
				
				
			
				double Units=0;
				
				
				if(ProductID==3) {  //Beeta original 25 G is not correct this item need below conversion - In AX 24 pices in Theia 1 hanger 
					
					Qty=Qty/24;
				}
				
				
			////Productid = 23 for 500G Shakkar
				
				if(ProductID==23) {					
					Qty=Qty*2;
				}
				
				if(ProductID==33) { //100G Sachet is coming from AX as original qty so no need to divide on unit per sku. Added by Zulqurnan by Sharukh sb on 14/07/2022 
					Qty=Qty*10;
				}
				
				if(ProductID==60) {  
					
					Qty=Qty/12;
				}
				
			////New Patch Added by Zulqurnan for 70G,140G and Shakkar 100 G -=----- 11/06/2022
				
							int PackageID=0;
							ResultSet rs21 = s2.executeQuery("select distinct package_id from inventory_products_view where product_id="+ProductID);
							if(rs21.first()) {
								PackageID=rs21.getInt("package_id");
							}
							
							
							if(PackageID==9) { //70 G
								Qty=Qty/12;
							}else if(PackageID==10) { //140 G
								Qty=Qty/10;
							}else if(PackageID==22) { //Sachet 100G
								Qty=Qty/10;
							}else if(PackageID==25) { //Sugar SMP 60 G
								Qty=Qty/12;
							}else if(PackageID==26) { //Sugar SMP 120 G
								Qty=Qty/10;
							}else if(PackageID==41) { //Sugar SMP 35 G
								Qty=Qty/12;
							}else if(PackageID==42) { //Sugar Sachet 70 G
								Qty=Qty/10;
							}else if(PackageID==43) { //Sugar Sachet 50 G
								Qty=Qty/10;
							}else if(PackageID==45) { //Sugar SMP 30 G
								Qty=Qty/12;
							}else if(PackageID==50) { //Sugar SMP Sachet 140 G
								Qty=Qty/10;
							}else if(PackageID==59) { //Sugar SMP Sachet 140 G
								Qty=Qty/10;
							}
							
							
							
							
							
							
							////////////////////////////
				
				
				
				
				double TotalUnits=Qty*UnitPerSku;
				
				double LiquidInML=TotalUnits*LiquidInMLPerCase;
				
				
				
				
				
								
				
				
				
				String CustomeOPGID="";
				
				try{
					
					if(OPGID!=null) {
					System.out.println(" OPGID "+OPGID);
						String[] ParseOPG=OPGID.split("-");
						CustomeOPGID=ParseOPG[ParseOPG.length-1];
					}
					
					
					long DistributorID=0;
					
					if(CustomerID!=null) {
						
						String[] ParseCustomerID=CustomerID.split("-");
						DistributorID=Utilities.parseLong(ParseCustomerID[1]);
						
					}
					
					// check for parent entery
					
					ResultSet rs1=s1.executeQuery("select delivery_id from inventory_delivery_note where opg_id='"+OPGID+"' and distributor_id="+DistributorID);
					
					if(rs1.first()) {
						
						long DeliveryID=rs1.getLong("delivery_id");
						
						s2.executeUpdate("insert into inventory_delivery_note_products(delivery_id, product_id, raw_cases, units, total_units, liquid_in_ml)values("+DeliveryID+","+ProductID+","+Qty+","+Units+","+TotalUnits+","+LiquidInML+")");
						
					}else {
						
						
						long UVID=Utilities.getUniqueVoucherID(UserID);
						
						String VehicleType="null";
						
						
						//check Transporter Exist If not then create new
						long NewTranpoterID=0;
						ResultSet rs3 = s4.executeQuery("select  * from inventory_delivery_note_haulage_contractors where external_transporter_id like '"+TransporterID+"'");
						if (rs3.first()){
							
							NewTranpoterID=rs3.getInt("id");
						}else {
							
							ResultSet rs4 = s4.executeQuery("select max(id) from inventory_delivery_note_haulage_contractors");
							if (rs4.first()){
								int MaxID = rs4.getInt(1);
								
								NewTranpoterID=MaxID+1;
							}
							
							
							s3.executeUpdate("insert into inventory_delivery_note_haulage_contractors(id,label,external_transporter_id)values("+NewTranpoterID+",'"+TransporterName+"','"+TransporterID+"')");
	
						}
						
						//// end Transporter patch//////////////////////
						
	
						System.out.println("insert into inventory_delivery_note(created_on, created_by, distributor_id, vehicle_no, vehicle_type,uvid,freight_contractor_id,original_created_on, opg_id, opg_id_custome,delivered_on,barcode)values(now(),"+UserID+","+DistributorID+",'"+VehicleNo+"',"+VehicleType+","+UVID+","+NewTranpoterID+",'"+OGPDate+"','"+OPGID+"','"+CustomeOPGID+"','"+OGPExistDate+"',"+UVID+")");
						
						s3.executeUpdate("insert into inventory_delivery_note(created_on, created_by, distributor_id, vehicle_no, vehicle_type,uvid,freight_contractor_id,original_created_on, opg_id, opg_id_custome,delivered_on,barcode)values(now(),"+UserID+","+DistributorID+",'"+VehicleNo+"',"+VehicleType+","+UVID+","+NewTranpoterID+",'"+OGPDate+"','"+OPGID+"','"+CustomeOPGID+"','"+OGPExistDate+"',"+UVID+")");
	
						
						
						long AutoIncrementedDeliveryID=0;
						
						ResultSet rs2 = s4.executeQuery("select LAST_INSERT_ID()");
						if (rs2.first()){
							AutoIncrementedDeliveryID = rs2.getLong(1);
							
						}
						
						//Updating the SAP Code - Replace SAP Code with Delivery ID 
						
						s2.executeUpdate("update inventory_delivery_note set sap_order_no="+AutoIncrementedDeliveryID+" where delivery_id="+AutoIncrementedDeliveryID);
	
						// insert into child table
						
						s3.executeUpdate("insert into inventory_delivery_note_products(delivery_id, product_id, raw_cases, units, total_units, liquid_in_ml)values("+AutoIncrementedDeliveryID+","+ProductID+","+Qty+","+Units+","+TotalUnits+","+LiquidInML+")");
				
						
					}
				
			}catch(SQLException e) {
					
				e.printStackTrace();
			}
				
		}			
			
			
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
}
