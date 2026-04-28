package com.pbc.cron;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class CacheExecute {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("CacheExecute ...");
		
		System.out.println("common_outlets ...");
		cacheCommonOutlets();
		///////System.out.println("inventory_products_mview ...");
		
		
		///////////cacheInventoryProductsView();
		try {
			////////System.out.println("inventory_delivery_note_source_invoice ...");
			/////////cacheDeliveryNoteFromSAP();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void cacheDeliveryNoteFromSAP() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		Datasource ds = new Datasource();
		ds.createConnection();

		Datasource ds2 = new Datasource();
		ds2.createConnectionToSAPDB();

		try{
			
			Date StartDate = Utilities.getDateByDays(-30);//Utilities.parseDate("01/01/2016"); //Utilities.getDateByDays(-1);
			Date EndDate = Utilities.getDateByDays(0);
			
			Statement s = ds.createStatement();		
			Statement s2 = ds.createStatement();
			ds.startTransaction();
			
			Statement sap = ds2.createStatement();
			Statement sap2 = ds2.createStatement();
			ds2.startTransaction();
			
			long rand = Math.round(Math.random())+Math.round(Math.random());
			
			//sap.executeUpdate("drop table temp_invoice_list"+rand+""+rand+"");
			
			sap.executeUpdate("create global temporary table temp_invoice_list1"+rand+""+rand+"(invoice_no VARCHAR2(30 BYTE))");
			
			s.executeUpdate("create temporary table temp_revenue_lifting (vbeln bigint(10), distributor_id bigint(10), matnr int(10), total_units double)");
			
			
			
		
			s2.executeUpdate("delete from inventory_delivery_note_source_invoice where delivery_created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate));
			
			ResultSet rs9 = s.executeQuery("SELECT invoice_no FROM inventory_delivery_note where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate));
			while(rs9.next()){
				
				sap.executeUpdate("insert into temp_invoice_list1"+rand+""+rand+" values ( '"+ Utilities.addLeadingZeros(rs9.getString("invoice_no"), 10)+"') ");
				
				
			}
			
			
			
			ResultSet rsSAP = sap2.executeQuery("select vbrk.vbeln, vbrk.fkart, vbrk.knumv, vbrp.posnr, vbrk.kurrf_dat, vbrp.pstyv, vbrp.matnr, vbrp.fkimg quantity, (vbrp.kzwi1+ /*vbrp.kzwi2 + vbrp.kzwi3 +*/ vbrp.kzwi6) gross_value, vbrp.kzwi4 unloading, vbrp.kzwi5 freight, vbrp.vrkme, vbrk.kunrg from sapsr3.vbrp vbrp join sapsr3.vbrk on vbrk.vbeln = vbrp.vbeln where vbrk.vbeln in (select invoice_no from temp_invoice_list1"+rand+""+rand+")");
			while(rsSAP.next()){
				
				long PriceConditionNo = rsSAP.getLong("knumv");
				String ItemType = rsSAP.getString("pstyv");
				long DistributorID = rsSAP.getLong("kunrg");
				long InvoiceNo = rsSAP.getLong("vbeln");
				long SAPCode = Utilities.parseLong(rsSAP.getString("matnr"));
				int quantity = rsSAP.getInt("quantity");
				String DocumentType = rsSAP.getString("fkart");
				Date InvoiceDate = Utilities.parseDateYYYYMMDDWithoutSeparator(rsSAP.getString("kurrf_dat"));
				int LineNo = rsSAP.getInt("posnr");
				double unloading = rsSAP.getDouble("unloading");
				if (unloading < 0){
					unloading = unloading * -1;
				}
				double freight = rsSAP.getDouble("freight");
				if (freight < 0){
					freight = freight * -1;
				}
				double InvoiceAmount = rsSAP.getDouble("gross_value");
				
				String TransactionType = rsSAP.getString("vrkme");
				long UnitPerSKU = 1;
				int ProductID = 0;
				int PackageID = 0;
				int BrandID = 0;
				int LRBTypeID = 0;
				if(TransactionType.equals("KI") || TransactionType.equals("KAR")){		
					ResultSet rs1 = s.executeQuery("select id, unit_per_sku, package_id, brand_id, lrb_type_id from inventory_products where id= (select id from inventory_products where sap_code="+SAPCode+")");
					if(rs1.first()){
						UnitPerSKU = rs1.getInt("unit_per_sku");
						ProductID = rs1.getInt("id");
						PackageID = rs1.getInt("package_id");
						BrandID = rs1.getInt("brand_id");
						LRBTypeID = rs1.getInt("lrb_type_id");
					}		
				}
				
				long TotalUnits = quantity * UnitPerSKU;
				
				int isRevenue = 0;
				if (DocumentType.equals("ZDIS") || DocumentType.equals("ZMRS") || DocumentType.equals("ZDFR")){
					if (!ItemType.equals("TANN")){
						isRevenue = 1;
					}
				}
				
				s2.executeUpdate("insert into inventory_delivery_note_source_invoice (id, delivery_created_on, invoice_no, distributor_id, product_sap_code, product_id, total_units, transaction_type, document_type, invoice_date, line_no, cache_units_per_sku, cache_package_id, cache_brand_id, unloading, freight, gross_value, cache_lrb_type_id, item_type, is_revenue, price_condition_no) values ((select delivery_id from inventory_delivery_note where invoice_no = "+InvoiceNo+"),(select created_on from inventory_delivery_note where invoice_no = "+InvoiceNo+"),"+InvoiceNo+","+DistributorID+","+SAPCode+","+ProductID+","+TotalUnits+",'"+TransactionType+"','"+DocumentType+"', "+Utilities.getSQLDate(InvoiceDate)+", "+LineNo+","+UnitPerSKU+","+PackageID+","+BrandID+","+unloading+","+freight+","+InvoiceAmount+","+LRBTypeID+",'"+ItemType+"', "+isRevenue+", "+PriceConditionNo+")");
				
				
			}
			
			
			ds2.commit();
			sap.executeUpdate("drop table temp_invoice_list1"+rand+""+rand+"");
			ds2.commit();
			sap.close();			
			ds.commit();
			
		}catch(Exception e){
			
			ds2.rollback();
			ds.rollback();
			e.printStackTrace();
			
		}finally{
			
			ds2.dropConnection();
			ds.dropConnection();
		}
	}
	
	public static void cacheInventoryProductsView(){
		Datasource ds = new Datasource();
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			s.executeUpdate("delete from inventory_products_mview");
			s2.executeUpdate("insert into inventory_products_mview SELECT * FROM inventory_products_view");

			ds.commit();
			
			s2.close();
			s.close();
			
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException e) {
				
				try {
					ds.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}	
	
	public static void cacheCommonOutlets(){
		
		
		Datasource ds = new Datasource();
		try {
			
			ds.createConnection();
			//ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			s.executeUpdate("update common_outlets set cache_distributor_id=null, cache_distributor_name=null, cache_contact_name=null, cache_contact_number = null, cache_contact_nic = null, cache_snd_id = null, cache_rsm_id = null, cache_tdm_id = null, cache_orderbooker_id = null");
			
			//System.out.println("SELECT codv.outlet_id, codv.distributor_id, (select name from common_distributors where distributor_id=codv.distributor_id) distributor_name FROM common_outlets_distributors_view codv");
			ResultSet rs = s.executeQuery("SELECT codv.outlet_id, codv.distributor_id, (select name from common_distributors where distributor_id=codv.distributor_id) distributor_name FROM common_outlets_distributors_view codv");
			while(rs.next()){
				//System.out.println("update common_outlets set cache_distributor_id="+rs.getString("distributor_id")+", cache_distributor_name='"+rs.getString("distributor_name")+"' where id="+rs.getString("outlet_id"));
				s2.executeUpdate("update common_outlets set distributor_id="+rs.getString("distributor_id")+", cache_distributor_id="+rs.getString("distributor_id")+", cache_distributor_name='"+rs.getString("distributor_name")+"' where id="+rs.getString("outlet_id"));
				
				s2.executeUpdate("update outletmaster set customer_id="+rs.getString("distributor_id")+", customer_name='"+rs.getString("distributor_name")+"' where outlet_id="+rs.getString("outlet_id"));
			}
			
			s2.executeUpdate("update pep.common_outlets set cache_contact_name = (select contact_name from common_outlets_contacts where is_primary = 1 and outlet_id = common_outlets.id limit 1)");
			s2.executeUpdate("update pep.common_outlets set cache_contact_number = (select contact_number from common_outlets_contacts where is_primary = 1 and outlet_id = common_outlets.id limit 1)");
			s2.executeUpdate("update pep.common_outlets set cache_contact_nic = (select contact_nic from common_outlets_contacts where is_primary = 1 and outlet_id = common_outlets.id limit 1)");
			s2.executeUpdate("update pep.common_outlets set cache_snd_id = (select snd_id from common_distributors where distributor_id = common_outlets.cache_distributor_id)");
			s2.executeUpdate("update pep.common_outlets set cache_rsm_id = (select rsm_id from common_distributors where distributor_id = common_outlets.cache_distributor_id)");
			s2.executeUpdate("update pep.common_outlets set cache_tdm_id = (select tdm_id from common_distributors where distributor_id = common_outlets.cache_distributor_id)");
			s2.executeUpdate("update pep.common_outlets set cache_orderbooker_id = (select assigned_to from distributor_beat_plan_view where outlet_id = common_outlets.id limit 1)");
			s2.executeUpdate("update pep.common_outlets set cache_beat_plan_id = (select id from distributor_beat_plan_view where outlet_id = common_outlets.id limit 1)");
			
			
			s2.close();
			s.close();
			//ds.dropConnection();
			//ds.commit();
			
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException e) {
			// TODO Auto-generated catch block
			
			//try {
				e.printStackTrace();
				//ds.rollback();
			//} catch (SQLException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			//}
			
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
