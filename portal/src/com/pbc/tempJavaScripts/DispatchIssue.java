package com.pbc.tempJavaScripts;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.pbc.inventory.StockDocument;
import com.pbc.inventory.StockDocumentItems;
import com.pbc.inventory.StockPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class DispatchIssue {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection c;
		Datasource ds;
		ds = new Datasource();

		try {
		//	StockPosting sp = new StockPosting(true);
			ds.createConnection();
			StockDocument document = new StockDocument();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			System.out.println("SELECT isp.product_id, ipv.unit_per_sku, ipv.liquid_in_ml, sum(isp.total_units) units FROM pep.inventory_sales_dispatch_returned_products isp, pep.inventory_products_view ipv where isp.product_id = ipv.product_id and isp.dispatch_id = 343013 and isp.is_empty = 0 group by isp.product_id");
			ResultSet rs2 = s2.executeQuery("SELECT isp.product_id, ipv.unit_per_sku, ipv.liquid_in_ml, sum(isp.total_units) units FROM pep.inventory_sales_dispatch_returned_products isp, pep.inventory_products_view ipv where isp.product_id = ipv.product_id and isp.dispatch_id = 343013 and isp.is_empty = 0 group by isp.product_id");
			while( rs2.next() ){
				
				StockDocumentItems DocumentItems = new StockDocumentItems();
				
				DocumentItems.PRODUCT_ID = rs2.getLong("product_id");
				DocumentItems.TOTAL_UNITS = rs2.getInt("units");
				
				long Units[] = Utilities.getRawCasesAndUnits(DocumentItems.TOTAL_UNITS, rs2.getInt("unit_per_sku"));
				
				DocumentItems.RAW_CASES = Utilities.parseInt(""+Units[0]);
				DocumentItems.UNITS = Utilities.parseInt(""+Units[1]);
				
				DocumentItems.LIQUID_IN_ML = (rs2.getLong("liquid_in_ml") * DocumentItems.TOTAL_UNITS);
				DocumentItems.TRANSACTION_TYPE = 2;
				
				document.PRODUCTS.add(DocumentItems);
			}
			StockDocumentItems items[] = document.getProducts();
	for (StockDocumentItems item: items){
				String ColumnSuffix="";
				if (item.TRANSACTION_TYPE == 1){ // Issuance
					ColumnSuffix = "issued";
				}else if (item.TRANSACTION_TYPE == 2){ // Receipt
					ColumnSuffix = "received";
				}
				System.out.println("insert into pep.inventory_distributor_stock_products (id, product_id, raw_cases_"+ColumnSuffix+", units_"+ColumnSuffix+", total_units_"+ColumnSuffix+", liquid_in_ml_"+ColumnSuffix+", location_id, transaction_type) values (22, "+item.PRODUCT_ID+", "+item.RAW_CASES+", "+item.UNITS+", "+item.TOTAL_UNITS+", "+item.LIQUID_IN_ML+", 1, "+item.TRANSACTION_TYPE+")");
			//	s.executeUpdate("insert into pep.inventory_distributor_stock_products (id, product_id, raw_cases_"+ColumnSuffix+", units_"+ColumnSuffix+", total_units_"+ColumnSuffix+", liquid_in_ml_"+ColumnSuffix+", location_id, transaction_type) values ("+InventoryDistributorStockID+", "+item.PRODUCT_ID+", "+item.RAW_CASES+", "+item.UNITS+", "+item.TOTAL_UNITS+", "+item.LIQUID_IN_ML+", 1, "+item.TRANSACTION_TYPE+")");
			}
			
			//ds.startTransaction();
			//StockPosting sp = new StockPosting();
			//boolean isPosted = sp.postDispatchLiquidReturn(342274);
		//	sp.close();
//			ResultSet rs = s.executeQuery(
//					"select sum(SD_CONVERTED_CASES) as c from get_sale where  SD_DISPATCH_CREATED_ON  between  '2023/12/01' and '2023/12/06'");
//			while (rs.next()) {
//				System.out.println(rs.getDouble("c"));
//			}
//
//		//	String ids = "333293,333292,333291,333290,333289,333288,333287,333286,333285,333284,333283,333282,333281,333280,333279,333278,333277,333276,333275,333274,333273,333272,333271,333270,333269,333268,333224";
//		//	String issueIds = "333293,333291,333286,333283,333281,333278,333277,333270";
//			String issueIds = "333270";
//			String ids = "333270";
//			System.out.println(
//					"select * from inventory_sales_dispatch where id in ("+ids+")");
//			ResultSet rs = s.executeQuery(
//					"select * from inventory_sales_dispatch where id in ("+ids+")  ");
//			while (rs.next()) {
//				System.out.println("-------------------------------------------------------------------------------------------------------");
//				Date ClosingDate = rs.getDate("created_on");
//				System.out.println("ClosingDate : " + ClosingDate);
//				document.DISTRIBUTOR_ID = rs.getLong("distributor_id");
//				document.DOCUMENT_TYPE_ID = 13;
//				document.DOCUMENT_ID = rs.getLong("id");
//				document.CREATED_ON = rs.getTimestamp("created_on");
//				document.CREATED_BY = rs.getLong("created_by");
//				boolean postingFlag=true;
//				// ResultSet rs2 = s2.executeQuery("SELECT isp.product_id, ipv.unit_per_sku,
//				// ipv.liquid_in_ml, sum(isp.total_units) units FROM
//				// inventory_sales_invoices_products isp, inventory_products_view ipv where
//				// isp.product_id = ipv.product_id and isp.id IN(select sales_id from
//				// inventory_sales_dispatch_invoices where id ="+DispatchID+") group by
//				// isp.product_id");
//				System.out
//						.println("select ap.product_id, sum(ap.units) units, ipv.unit_per_sku, ipv.liquid_in_ml from ("
//								+ "SELECT isp.product_id, sum(isp.total_units) units FROM inventory_sales_invoices_products isp, inventory_products_view ipv where isp.product_id = ipv.product_id and isp.id IN (select sales_id from inventory_sales_dispatch_invoices where id = "
//								+ rs.getLong("id")
//								+ ") group by isp.product_id union all select isdep.product_id, sum(isdep.total_units) from inventory_sales_dispatch_extra_products isdep where isdep.dispatch_id = "
//								+ rs.getLong("id") + " group by isdep.product_id"
//								+ ") ap join inventory_products_view ipv on ap.product_id = ipv.product_id group by ap.product_id");
//				ResultSet rs2 = s2.executeQuery(
//						"select ap.product_id, sum(ap.units) units, ipv.unit_per_sku, ipv.liquid_in_ml from ("
//								+ "SELECT isp.product_id, sum(isp.total_units) units FROM inventory_sales_invoices_products isp, inventory_products_view ipv where isp.product_id = ipv.product_id and isp.id IN (select sales_id from inventory_sales_dispatch_invoices where id = "
//								+ rs.getLong("id")
//								+ ") group by isp.product_id union all select isdep.product_id, sum(isdep.total_units) from inventory_sales_dispatch_extra_products isdep where isdep.dispatch_id = "
//								+ rs.getLong("id") + " group by isdep.product_id"
//								+ ") ap join inventory_products_view ipv on ap.product_id = ipv.product_id group by ap.product_id");
//				while (rs2.next()) {
//
//					long UnitAfterDispatch = sp.getBalanceafterdispatch(rs.getLong("distributor_id"),
//							(int) rs2.getLong("product_id"));
//
//					long ClosingUnits = sp.getClosingBalance(rs.getLong("distributor_id"),
//							(int) rs2.getLong("product_id"), ClosingDate);
//					
//					long ClosingUnitsToday = sp.getClosingBalance(rs.getLong("distributor_id"),
//							(int) rs2.getLong("product_id"), new Date());
//					
//					
//				
//
//					long TotalUnits = ClosingUnits - UnitAfterDispatch;
//					long TotalUnitsToday = ClosingUnitsToday - UnitAfterDispatch;
//				//	System.out.println("-----------------------------------------------");
//					System.out.println("product_id : "+rs2.getLong("product_id"));
//					
//					System.out.println("UnitAfterDispatch : "+UnitAfterDispatch);
//					
//					System.out.println("ClosingUnits : "+ClosingUnits);
//					
//					System.out.println("TotalUnits : "+TotalUnits);
//					
//					System.out.println("ClosingUnitsToday : "+ClosingUnitsToday);
//					
//					System.out.println("TotalUnitsToday : "+TotalUnitsToday);
//					
//					System.out.println("raw units : "+rs2.getInt("units"));
//					//System.out.println("-----------------------`------------------------");
//					StockDocumentItems DocumentItems = new StockDocumentItems();
//				//	if (rs2.getInt("units") <= TotalUnits && rs2.getInt("units") != 0 && rs2.getInt("units") <= TotalUnitsToday) {
//
//						
//
//						DocumentItems.PRODUCT_ID = rs2.getLong("product_id");
//						DocumentItems.TOTAL_UNITS = rs2.getInt("units");
//
//						long Units[] = Utilities.getRawCasesAndUnits(DocumentItems.TOTAL_UNITS,
//								rs2.getInt("unit_per_sku"));
//
//						DocumentItems.RAW_CASES = Utilities.parseInt("" + Units[0]);
//						DocumentItems.UNITS = Utilities.parseInt("" + Units[1]);
//
//						DocumentItems.LIQUID_IN_ML = (rs2.getLong("liquid_in_ml") * DocumentItems.TOTAL_UNITS);
//						DocumentItems.TRANSACTION_TYPE = 1;
//
//						document.PRODUCTS.add(DocumentItems);
//
////					}else {
////						postingFlag=false;
////						System.out.println("Dispatch : "+rs.getLong("id") + " - " + " against product : "+rs2.getLong("product_id") + " Date : "+ClosingDate);
////					}
//				}
//
//				if(postingFlag) {
//				boolean ff = false;
//				StockPosting so = new StockPosting();
//				ff = so.postStock(document);
//				System.out.println(ff);
//				}
//				System.out.println("-------------------------------------------------------------------------------------------------------");
			//}

			//ds.commit();
			s.close();

		} catch (Exception e) {

			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

	}

}
