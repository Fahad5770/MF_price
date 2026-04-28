package com.pbc.inventory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class StockPosting {
	Connection c;
	Datasource ds;
	
	
	
	public StockPosting() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds = new Datasource();
		ds.createConnection();
	}
	
	public StockPosting(boolean CONNECT_TO_REPLICA) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds = new Datasource();
		ds.createConnectionToReplica();
	}
	
	public boolean unPostStock(int DocumentType, long DocumentID) throws SQLException{
		boolean isUnposted = false;
		try{
			ds.startTransaction();
			Statement s = ds.createStatement();
		//	System.out.println("delete from inventory_distributor_stock where document_type_id="+DocumentType+" and document_id="+DocumentID);
			s.executeUpdate("delete from inventory_distributor_stock where document_type_id="+DocumentType+" and document_id="+DocumentID);
			s.close();
			ds.commit();
			isUnposted = true;
		}catch(Exception e){
			e.printStackTrace();
			ds.rollback();
		}
		return isUnposted;
	}
	
	public boolean postStock(StockDocument doc){
		
		
		
		boolean isPosted = false;
		try{
			
			unPostStock(doc.DOCUMENT_TYPE_ID, doc.DOCUMENT_ID);
			
			ds.startTransaction();
			Statement s = ds.createStatement();
			System.out.println("In stock : "+doc.CREATED_BY);
			int InventoryDistributorStockID = 0;
			System.out.println("insert into pep.inventory_distributor_stock (distributor_id,document_type_id,document_id,created_on,created_by) values ("+doc.DISTRIBUTOR_ID+","+doc.DOCUMENT_TYPE_ID+","+doc.DOCUMENT_ID+","+Utilities.getSQLDate(doc.CREATED_ON)+", "+doc.CREATED_BY+")");
			s.executeUpdate("insert into pep.inventory_distributor_stock (distributor_id,document_type_id,document_id,created_on,created_by) values ("+doc.DISTRIBUTOR_ID+","+doc.DOCUMENT_TYPE_ID+","+doc.DOCUMENT_ID+","+Utilities.getSQLDate(doc.CREATED_ON)+", "+doc.CREATED_BY+")");

			ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
			if( rs.first() ){
				InventoryDistributorStockID = rs.getInt(1);
			}
			System.out.println("InventoryDistributorStockID : "+InventoryDistributorStockID);
			String ColumnSuffix = "";
			
			
			
			StockDocumentItems items[] = doc.getProducts();
			
			System.out.println("items "+items.length);
			
			for (StockDocumentItems item: items){
			 if (item.TRANSACTION_TYPE == 1){ // Issuance
					ColumnSuffix = "issued";
				}else if (item.TRANSACTION_TYPE == 2){ // Receipt
					ColumnSuffix = "received";
				}
				System.out.println("insert into pep.inventory_distributor_stock_products (id, product_id, raw_cases_"+ColumnSuffix+", units_"+ColumnSuffix+", total_units_"+ColumnSuffix+", liquid_in_ml_"+ColumnSuffix+", location_id, transaction_type) values ("+InventoryDistributorStockID+", "+item.PRODUCT_ID+", "+item.RAW_CASES+", "+item.UNITS+", "+item.TOTAL_UNITS+", "+item.LIQUID_IN_ML+", 1, "+item.TRANSACTION_TYPE+")");
				s.executeUpdate("insert into pep.inventory_distributor_stock_products (id, product_id, raw_cases_"+ColumnSuffix+", units_"+ColumnSuffix+", total_units_"+ColumnSuffix+", liquid_in_ml_"+ColumnSuffix+", location_id, transaction_type) values ("+InventoryDistributorStockID+", "+item.PRODUCT_ID+", "+item.RAW_CASES+", "+item.UNITS+", "+item.TOTAL_UNITS+", "+item.LIQUID_IN_ML+", 1, "+item.TRANSACTION_TYPE+")");
			}
			ds.commit();
			s.close();
			isPosted = true;
		}catch (Exception e) {
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return isPosted;
	}
	
	
	public boolean postDispatch(long DispatchID){
		
		boolean isPosted = false;
		
		try{
			
			StockDocument document = new StockDocument();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			ds.startTransaction();
			
			ResultSet rs = s.executeQuery("select * from inventory_sales_dispatch where id = "+DispatchID);
			if (rs.first()){
				
				document.DISTRIBUTOR_ID = rs.getLong("distributor_id");
				document.DOCUMENT_TYPE_ID = 13;
				document.DOCUMENT_ID = rs.getLong("id");
				document.CREATED_ON = rs.getTimestamp("created_on");
				document.CREATED_BY = rs.getLong("created_by");		
				
				//ResultSet rs2 = s2.executeQuery("SELECT isp.product_id, ipv.unit_per_sku, ipv.liquid_in_ml, sum(isp.total_units) units FROM inventory_sales_invoices_products isp, inventory_products_view ipv where isp.product_id = ipv.product_id and isp.id IN(select sales_id from inventory_sales_dispatch_invoices where id ="+DispatchID+") group by isp.product_id");
				
				ResultSet rs2 = s2.executeQuery("select ap.product_id, sum(ap.units) units, ipv.unit_per_sku, ipv.liquid_in_ml from ("+
													"SELECT isp.product_id, sum(isp.total_units) units FROM inventory_sales_invoices_products isp, inventory_products_view ipv where isp.product_id = ipv.product_id and isp.id IN (select sales_id from inventory_sales_dispatch_invoices where id = "+DispatchID+") group by isp.product_id union all select isdep.product_id, sum(isdep.total_units) from inventory_sales_dispatch_extra_products isdep where isdep.dispatch_id = "+DispatchID+" group by isdep.product_id"+
												") ap join inventory_products_view ipv on ap.product_id = ipv.product_id group by ap.product_id");
				while( rs2.next() ){
					
					StockDocumentItems DocumentItems = new StockDocumentItems();
					
					DocumentItems.PRODUCT_ID = rs2.getLong("product_id");
					DocumentItems.TOTAL_UNITS = rs2.getInt("units");
					
					long Units[] = Utilities.getRawCasesAndUnits(DocumentItems.TOTAL_UNITS, rs2.getInt("unit_per_sku"));
					
					DocumentItems.RAW_CASES = Utilities.parseInt(""+Units[0]);
					DocumentItems.UNITS = Utilities.parseInt(""+Units[1]);
					
					DocumentItems.LIQUID_IN_ML = (rs2.getLong("liquid_in_ml") * DocumentItems.TOTAL_UNITS);
					DocumentItems.TRANSACTION_TYPE = 1;
					
					document.PRODUCTS.add(DocumentItems);
					
				}
				
				isPosted = postStock(document);
				
			}
			
			ds.commit();
			s.close();
		
		}catch (Exception e) {
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		return isPosted;	
		
	}
	
public boolean postDispatchLiquidReturn(long DispatchID) {

    boolean isPosted = false;
    

    try {
    	
    	 unPostStock(14, DispatchID);
    	
    	Statement s = ds.createStatement();
    	Statement s2 = ds.createStatement();
       

        StockDocument document = new StockDocument();
       
        String query1 = "select * from pep.inventory_sales_dispatch where id = " + DispatchID + " and is_liquid_returned = 1";
        System.out.println(query1);
        ResultSet rs = s.executeQuery(query1);

        if (rs.first()) {
            System.out.println("inventory_sales_dispatch : " + rs.getLong("liquid_returned_by"));
            document.DISTRIBUTOR_ID = rs.getLong("distributor_id");
            document.DOCUMENT_TYPE_ID = 14;
            document.DOCUMENT_ID = rs.getLong("id");
            document.CREATED_ON = rs.getTimestamp("liquid_returned_on");
            document.CREATED_BY = rs.getLong("liquid_returned_by");

            System.out.println("doc : CREATED_BY : " + document.CREATED_BY);

            String query2 = "SELECT isp.product_id, ipv.unit_per_sku, ipv.liquid_in_ml, sum(isp.total_units) units " +
                            "FROM pep.inventory_sales_dispatch_returned_products isp, pep.inventory_products_view ipv " +
                            "WHERE isp.product_id = ipv.product_id AND isp.dispatch_id = " + DispatchID + " AND isp.is_empty = 0 " +
                            "GROUP BY isp.product_id";
            System.out.println(query2);
            ResultSet  rs2 = s2.executeQuery(query2);
            while (rs2.next()) {
                System.out.println("Innnnnn: ");
                StockDocumentItems DocumentItems = new StockDocumentItems();

                DocumentItems.PRODUCT_ID = rs2.getLong("product_id");
                System.out.println("DocumentItems.PRODUCT_ID : " + DocumentItems.PRODUCT_ID);

                DocumentItems.TOTAL_UNITS = rs2.getInt("units");
                System.out.println("DocumentItems.TOTAL_UNITS : " + DocumentItems.TOTAL_UNITS);

                long Units[] = Utilities.getRawCasesAndUnits(DocumentItems.TOTAL_UNITS, rs2.getInt("unit_per_sku"));

                DocumentItems.RAW_CASES = Utilities.parseInt("" + Units[0]);
                System.out.println("DocumentItems.RAW_CASES : " + DocumentItems.RAW_CASES);

                DocumentItems.UNITS = Utilities.parseInt("" + Units[1]);
                System.out.println("DocumentItems.UNITS : " + DocumentItems.UNITS);

                DocumentItems.LIQUID_IN_ML = (rs2.getLong("liquid_in_ml") * DocumentItems.TOTAL_UNITS);
                System.out.println("DocumentItems.LIQUID_IN_ML : " + DocumentItems.LIQUID_IN_ML);

                DocumentItems.TRANSACTION_TYPE = 2;
                System.out.println("DocumentItems.TRANSACTION_TYPE : " + DocumentItems.TRANSACTION_TYPE);

                document.PRODUCTS.add(DocumentItems);
            }
            System.out.println("After while : ");

            isPosted = postStock(document);
        }

      

    } catch (Exception e) {
       
            try {
                ds.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        
        e.printStackTrace();
    } 

    return isPosted;
}

	public boolean postDispatchEmptyReturn(long DispatchID){
		
		
		
		boolean isPosted = false;
		
		try{
			
			unPostStock(15, DispatchID);
			
			StockDocument document = new StockDocument();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			ds.startTransaction();
			
			ResultSet rs = s.executeQuery("select * from inventory_sales_dispatch where id = "+DispatchID+" and is_empty_returned = 1");
			if (rs.first()){
				
				document.DISTRIBUTOR_ID = rs.getLong("distributor_id");
				document.DOCUMENT_TYPE_ID = 15;
				document.DOCUMENT_ID = rs.getLong("id");
				document.CREATED_ON = rs.getTimestamp("liquid_returned_on");
				document.CREATED_BY = rs.getLong("liquid_returned_by");		
				
				
				ResultSet rs2 = s2.executeQuery("SELECT isp.product_id, ipv.unit_per_sku, ipv.liquid_in_ml, sum(isp.total_units) units FROM inventory_sales_dispatch_returned_products isp, inventory_products_view ipv where isp.product_id = ipv.product_id and isp.dispatch_id = "+DispatchID+" and isp.is_empty = 1 group by isp.product_id");
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
				
				isPosted = postStock(document);
				
			}
			
			ds.commit();
			s.close();
		
		}catch (Exception e) {
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		return isPosted;	
		
	}
	
	public long getBalanceafterdispatch(long DistributorID, int ProductID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Statement s = ds.createStatement();

		long TotalUnits = 0;
		System.out.println("select sum(isp.total_units) AS total_units FROM inventory_sales_invoices_products isp join     inventory_sales_invoices isi  ON isp.id = isi.id where isi.distributor_id = "+DistributorID+" and isp.product_id = "+ProductID+" and isi.is_dispatched=0");
		ResultSet rs9 = s.executeQuery("select sum(isp.total_units) AS total_units FROM inventory_sales_invoices_products isp join     inventory_sales_invoices isi  ON isp.id = isi.id where isi.distributor_id = "+DistributorID+" and isp.product_id = "+ProductID+" and isi.is_dispatched=0");
		if (rs9.first()){
			TotalUnits = rs9.getLong(1);
		}
		
		s.close();
		return TotalUnits;
	}
	public long getBalanceafterdispatch(long DistributorID[], int ProductID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Statement s = ds.createStatement();
		
		long TotalUnits = 0;
		if (DistributorID != null && DistributorID.length > 0){
			
			String DistributorIDs = "";
			
			for (int i = 0; i < DistributorID.length; i++){
				if (i != 0){
					DistributorIDs += ",";
				}
				DistributorIDs += DistributorID[i];
			}
System.out.println("select sum(isp.total_units) AS total_units FROM inventory_sales_invoices_products isp join     inventory_sales_invoices isi  ON isp.id = isi.id where isi.distributor_id in ("+DistributorIDs+") and isp.product_id = "+ProductID+" and isi.is_dispatched=0");
			ResultSet rs9 = s.executeQuery("select sum(isp.total_units) AS total_units FROM inventory_sales_invoices_products isp join     inventory_sales_invoices isi  ON isp.id = isi.id where isi.distributor_id in ("+DistributorIDs+") and isp.product_id = "+ProductID+" and isi.is_dispatched=0");
			if (rs9.first()){
				TotalUnits = rs9.getLong(1);
			}
			
			s.close();
			
		}		
		return TotalUnits;
	}
	public long getClosingBalance(long DistributorID, int ProductID, Date ClosingDate) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Statement s = ds.createStatement();

		long TotalUnits = 0;
		System.out.println("SELECT sum(total_units_received-total_units_issued) balance FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id = "+DistributorID+" and idsp.product_id = "+ProductID+" and ids.created_on < "+Utilities.getSQLDateNext(ClosingDate));
		ResultSet rs9 = s.executeQuery("SELECT sum(total_units_received-total_units_issued) balance FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id = "+DistributorID+" and idsp.product_id = "+ProductID+" and ids.created_on < "+Utilities.getSQLDateNext(ClosingDate));
		if (rs9.first()){
			TotalUnits = rs9.getLong(1);
		}
		
		s.close();
		return TotalUnits;
	}
	
	public long getClosingBalanceExInvoiced(long DistributorID, int ProductID, Date ClosingDate) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Statement s = ds.createStatement();

		long TotalUnits = 0;
		ResultSet rs9 = s.executeQuery("SELECT sum(total_units_received-total_units_issued) balance FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id = "+DistributorID+" and idsp.product_id = "+ProductID+" and ids.created_on < "+Utilities.getSQLDateNext(ClosingDate));
		if (rs9.first()){
			TotalUnits = rs9.getLong(1);
		}
		long AlreadyInvoiced = 0;
		ResultSet rs = s.executeQuery("select sum(total_units) from inventory_sales_invoices isi, inventory_sales_invoices_products isip where isi.id = isip.id and isi.is_dispatched = 0 and isi.distributor_id = "+DistributorID+" and isip.product_id = "+ProductID);
		if (rs.first()){
			AlreadyInvoiced = rs.getLong(1);
		}
		
		
		s.close();
		return (TotalUnits - AlreadyInvoiced);
	}	
		
	public long getClosingBalance(long DistributorID[], int ProductID, Date ClosingDate) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Statement s = ds.createStatement();
		
		long TotalUnits = 0;
		if (DistributorID != null && DistributorID.length > 0){
			
			String DistributorIDs = "";
			
			for (int i = 0; i < DistributorID.length; i++){
				if (i != 0){
					DistributorIDs += ",";
				}
				DistributorIDs += DistributorID[i];
			}

			ResultSet rs9 = s.executeQuery("SELECT sum(total_units_received-total_units_issued) balance FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id in ("+DistributorIDs+") and idsp.product_id = "+ProductID+" and ids.created_on < "+Utilities.getSQLDateNext(ClosingDate));
			if (rs9.first()){
				TotalUnits = rs9.getLong(1);
			}
			
			s.close();
			
		}		
		return TotalUnits;
	}
	public long getStockReceipts(long DistributorID, int ProductID, Date FromDate, Date ToDate) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Statement s = ds.createStatement();

		long TotalUnits = 0;
		ResultSet rs9 = s.executeQuery("SELECT sum(total_units_received) balance FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id = "+DistributorID+" and idsp.product_id = "+ProductID+" and ids.created_on between "+Utilities.getSQLDate(FromDate)+" and "+Utilities.getSQLDateNext(ToDate));
		if (rs9.first()){
			TotalUnits = rs9.getLong(1);
		}
		
		s.close();
		return TotalUnits;
	}
	public long getStockReceipts(long DistributorID[], int ProductID, Date FromDate, Date ToDate) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Statement s = ds.createStatement();

		long TotalUnits = 0;
		if (DistributorID != null && DistributorID.length > 0){
			
			String DistributorIDs = "";
			
			for (int i = 0; i < DistributorID.length; i++){
				if (i != 0){
					DistributorIDs += ",";
				}
				DistributorIDs += DistributorID[i];
			}
		
			ResultSet rs9 = s.executeQuery("SELECT sum(total_units_received) balance FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id in ("+DistributorIDs+") and idsp.product_id = "+ProductID+" and ids.created_on between "+Utilities.getSQLDate(FromDate)+" and "+Utilities.getSQLDateNext(ToDate));
			if (rs9.first()){
				TotalUnits = rs9.getLong(1);
			}
			
			s.close();
		}
		return TotalUnits;
	}
	public long getStockReceiptsLiftingOnly(long DistributorID[], int ProductID, Date FromDate, Date ToDate) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Statement s = ds.createStatement();

		long TotalUnits = 0;
		if (DistributorID != null && DistributorID.length > 0){
			
			String DistributorIDs = "";
			
			for (int i = 0; i < DistributorID.length; i++){
				if (i != 0){
					DistributorIDs += ",";
				}
				DistributorIDs += DistributorID[i];
			}
		
			ResultSet rs9 = s.executeQuery("SELECT sum(total_units_received) balance FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id in ("+DistributorIDs+") and idsp.product_id = "+ProductID+" and ids.created_on between "+Utilities.getSQLDate(FromDate)+" and "+Utilities.getSQLDateNext(ToDate)+" and ids.document_type_id = 11");
			if (rs9.first()){
				TotalUnits = rs9.getLong(1);
			}
			
			s.close();
		}
		return TotalUnits;
	}
	public long getStockReceiptsLessLifting(long DistributorID[], int ProductID, Date FromDate, Date ToDate) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Statement s = ds.createStatement();

		long TotalUnits = 0;
		if (DistributorID != null && DistributorID.length > 0){
			
			String DistributorIDs = "";
			
			for (int i = 0; i < DistributorID.length; i++){
				if (i != 0){
					DistributorIDs += ",";
				}
				DistributorIDs += DistributorID[i];
			}
		System.out.println("SELECT sum(total_units_received) balance FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id in ("+DistributorIDs+") and idsp.product_id = "+ProductID+" and ids.created_on between "+Utilities.getSQLDate(FromDate)+" and "+Utilities.getSQLDateNext(ToDate)+" and ids.document_type_id != 11");
			ResultSet rs9 = s.executeQuery("SELECT sum(total_units_received) balance FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id in ("+DistributorIDs+") and idsp.product_id = "+ProductID+" and ids.created_on between "+Utilities.getSQLDate(FromDate)+" and "+Utilities.getSQLDateNext(ToDate)+" and ids.document_type_id != 11");
			if (rs9.first()){
				TotalUnits = rs9.getLong(1);
			}
			
			s.close();
		}
		return TotalUnits;
	}
	
	public long getStockIssuance(long DistributorID, int ProductID, Date FromDate, Date ToDate) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Statement s = ds.createStatement();

		long TotalUnits = 0;
		ResultSet rs9 = s.executeQuery("SELECT sum(total_units_issued) balance FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id = "+DistributorID+" and idsp.product_id = "+ProductID+" and ids.created_on between "+Utilities.getSQLDate(FromDate)+" and "+Utilities.getSQLDateNext(ToDate));
		if (rs9.first()){
			TotalUnits = rs9.getLong(1);
		}
		
		s.close();
		return TotalUnits;
	}
	
	public long getStockIssuance(long DistributorID[], int ProductID, Date FromDate, Date ToDate) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Statement s = ds.createStatement();

		long TotalUnits = 0;
		
		if (DistributorID != null && DistributorID.length > 0){
			
			String DistributorIDs = "";
			
			for (int i = 0; i < DistributorID.length; i++){
				if (i != 0){
					DistributorIDs += ",";
				}
				DistributorIDs += DistributorID[i];
			}
		
			ResultSet rs9 = s.executeQuery("SELECT sum(total_units_issued) balance FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id in ("+DistributorIDs+") and idsp.product_id = "+ProductID+" and ids.created_on between "+Utilities.getSQLDate(FromDate)+" and "+Utilities.getSQLDateNext(ToDate));
			if (rs9.first()){
				TotalUnits = rs9.getLong(1);
			}
			
			s.close();
		}
		return TotalUnits;
	}
	public long getStockIssuanceDispatchOnly(long DistributorID[], int ProductID, Date FromDate, Date ToDate) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Statement s = ds.createStatement();

		long TotalUnits = 0;
		
		if (DistributorID != null && DistributorID.length > 0){
			
			String DistributorIDs = "";
			
			for (int i = 0; i < DistributorID.length; i++){
				if (i != 0){
					DistributorIDs += ",";
				}
				DistributorIDs += DistributorID[i];
			}
		System.out.println("SELECT sum(total_units_issued) balance FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id in ("+DistributorIDs+") and idsp.product_id = "+ProductID+" and ids.created_on between "+Utilities.getSQLDate(FromDate)+" and "+Utilities.getSQLDateNext(ToDate)+" and ids.document_type_id = 13");
			ResultSet rs9 = s.executeQuery("SELECT sum(total_units_issued) balance FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id in ("+DistributorIDs+") and idsp.product_id = "+ProductID+" and ids.created_on between "+Utilities.getSQLDate(FromDate)+" and "+Utilities.getSQLDateNext(ToDate)+" and ids.document_type_id = 13");
			if (rs9.first()){
				TotalUnits = rs9.getLong(1);
			}
			
			s.close();
		}
		return TotalUnits;
	}
	public long getStockIssuanceLessDispatch(long DistributorID[], int ProductID, Date FromDate, Date ToDate) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Statement s = ds.createStatement();

		long TotalUnits = 0;
		
		if (DistributorID != null && DistributorID.length > 0){
			
			String DistributorIDs = "";
			
			for (int i = 0; i < DistributorID.length; i++){
				if (i != 0){
					DistributorIDs += ",";
				}
				DistributorIDs += DistributorID[i];
			}
		
			ResultSet rs9 = s.executeQuery("SELECT sum(total_units_issued) balance FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id in ("+DistributorIDs+") and idsp.product_id = "+ProductID+" and ids.created_on between "+Utilities.getSQLDate(FromDate)+" and "+Utilities.getSQLDateNext(ToDate)+" and ids.document_type_id != 13");
			if (rs9.first()){
				TotalUnits = rs9.getLong(1);
			}
			
			s.close();
		}
		return TotalUnits;
	}
	
public boolean postPostSalesReturn(long SalesReturnID){
		
		boolean isPosted = false;
		
		try{
			unPostStock(66, SalesReturnID);
			
			StockDocument document = new StockDocument();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			ds.startTransaction();
			
			ResultSet rs = s.executeQuery("select * from inventory_sales_returns where id = "+SalesReturnID+" ");
			if (rs.first()){
				
				document.DISTRIBUTOR_ID = rs.getLong("distributor_id");
				document.DOCUMENT_TYPE_ID = 66;
				document.DOCUMENT_ID = rs.getLong("id");
				document.CREATED_ON = rs.getTimestamp("created_on");
				document.CREATED_BY = rs.getLong("created_by");		
				
				
				ResultSet rs2 = s2.executeQuery("SELECT isrp.product_id, ipv.unit_per_sku, ipv.liquid_in_ml, sum(isrp.total_units) units FROM inventory_sales_returns_products isrp, inventory_products_view ipv where isrp.product_id = ipv.product_id and isrp.id = "+SalesReturnID+" group by isrp.product_id");
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
				
				isPosted = postStock(document);
				
			}
			
			ds.commit();
			s.close();
		
		}catch (Exception e) {
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		return isPosted;	
		
	}

	
	public void close() throws SQLException{
		ds.dropConnection();
	}
	
}
