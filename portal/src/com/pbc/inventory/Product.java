package com.pbc.inventory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.workflow.WorkflowDocument;

public class Product {

	public int PRODUCT_ID;
	public int CATEGORY_ID;
	public int PACKAGE_ID;
	public int BRAND_ID;
	public long SAP_CODE;
	public int UNIT_PER_SKU;
	public long LIQUID_IN_ML;
	public boolean EXISTS = false;
	
	 public static double[] getSellingPrice_2(long SAPCode, long OutletID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
	      Datasource ds = new Datasource();
	      ds.createConnection();
	      Statement s = ds.createStatement();
	      int ProductID = 0;
	      ResultSet rs9 = s.executeQuery("select id from inventory_products where sap_code = " + SAPCode);
	      if (rs9.first()) {
	         ProductID = rs9.getInt(1);
	      }

	      s.close();
	      ds.dropConnection();
	      return getSellingPrice_2(ProductID, OutletID);
	   }
	 
	 public static double[] getSellingPrice_2(int ProductID, long OutletID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
	      Datasource ds = new Datasource();
	      double[] ret = new double[5];
	      long PriceListID = 0L;
	      ds.createConnection();
	      Statement s = ds.createStatement();
	      Statement s1 = ds.createStatement();
	      Statement s2 = ds.createStatement();
	      int isFiler = 0;
	      int isRegister = 0;
	    //  System.out.println("select is_filer, is_register from common_outlets where id=" + OutletID);
	      ResultSet rsOutlet = s.executeQuery("select is_filer, is_register from common_outlets where id=" + OutletID);
	      if (rsOutlet.first()) {
	         isFiler = rsOutlet.getInt("is_filer");
	         isRegister = rsOutlet.getInt("is_register");
	      }

	     // System.out.println("SELECT id FROM pep.inventory_price_list where is_filer=" + isFiler + " and is_register=" + isRegister + " and id > 38");
	      ResultSet rs2 = s1.executeQuery("SELECT id FROM pep.inventory_price_list where is_filer=" + isFiler + " and is_register=" + isRegister + " and id > 38");
	      if (rs2.first()) {
	         PriceListID = (long)rs2.getInt("id");
	      }

	      //System.out.println("select * from inventory_price_list_products where id=" + PriceListID + " and product_id=" + ProductID);
	      ResultSet rs3 = s.executeQuery("select * from inventory_price_list_products where id=" + PriceListID + " and product_id=" + ProductID);
	      
	      if (rs3.first()) {
	         ret[0] = rs3.getDouble("raw_case");
	         ret[1] = rs3.getDouble("unit");
	         ret[2] = rs3.getDouble("discount");
	      } else {
	    	//  System.out.println("select * from inventory_price_list_products where id=1 and product_id=" + ProductID);
	    	  ResultSet rs4 = s2.executeQuery("select * from inventory_price_list_products where id=1 and product_id=" + ProductID);
	         if (rs4.first()) {
	            ret[0] = rs4.getDouble("raw_case");
	            ret[1] = rs4.getDouble("unit");
	            ret[2] = rs4.getDouble("discount");
	         }
	      }

	     /* rs4 = s.executeQuery("SELECT iplhdm.discount, ip.unit_per_sku, iplhdm.sampling_id FROM inventory_price_list_hand_discount_mview iplhdm join inventory_products ip on iplhdm.product_id=ip.id where iplhdm.product_id=" + ProductID + " and iplhdm.outlet_id=" + OutletID);
	      if (rs4.first()) {
	         ret[0] -= rs4.getDouble("discount");
	         if (rs4.getInt("unit_per_sku") != 0) {
	            ret[1] = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(ret[0] / rs4.getDouble("unit_per_sku")));
	         }

	       
	         ret[3] = rs4.getDouble("sampling_id");
	         ret[4] = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(ret[2] / (double)rs4.getInt("unit_per_sku")));
	      }*/

	      s.close();
	      ds.dropConnection();
	      return ret;
	   }


	public static double[] getSellingPrice(long SAPCode, long OutletID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Datasource ds = new Datasource();
		ds.createConnection();
		Statement s = ds.createStatement();

		int ProductID = 0;
		ResultSet rs9 = s.executeQuery("select id from inventory_products where sap_code = "+SAPCode);
		if (rs9.first()){
			ProductID = rs9.getInt(1);
		}
		
		s.close();
		
		ds.dropConnection();
		
		return getSellingPrice(ProductID, OutletID);
	}
	
	public static BackOrderProduct[] getBackOrderProducts(long DistributorID, long OrderIDs[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		ArrayList<BackOrderProduct> ret = new ArrayList<BackOrderProduct>();
		
		Datasource ds = new Datasource();
		ds.createConnection();
		Statement s = ds.createStatement();
		
		String OrderIDsQuery = "";
		for (int i = 0; i < OrderIDs.length; i++){
			if (i != 0){
				OrderIDsQuery += ",";
			}
			OrderIDsQuery += OrderIDs[i];
		}
		
		System.out.println("SELECT mop.product_id, sum(mop.total_units) ordered, ("+
				"select sum(total_units) from inventory_sales_invoices isi, inventory_sales_invoices_products isip where isi.id = isip.id and isi.is_dispatched = 0 and isi.distributor_id = "+DistributorID+" and isip.product_id = mop.product_id"+
			") invoiced FROM mobile_order_products mop where mop.id in ("+OrderIDsQuery+") group by mop.product_id");		ResultSet rs = s.executeQuery("SELECT mop.product_id, sum(mop.total_units) ordered, ("+
				"select sum(total_units) from inventory_sales_invoices isi, inventory_sales_invoices_products isip where isi.id = isip.id and isi.is_dispatched = 0 and isi.distributor_id = "+DistributorID+" and isip.product_id = mop.product_id"+
			") invoiced FROM mobile_order_products mop where mop.id in ("+OrderIDsQuery+") group by mop.product_id");

		
		while(rs.next()){
			
			int ProductID = rs.getInt("product_id");
			
			long UnitsAvailable = getCurrentBalance(DistributorID, ProductID);
			//System.out.println("D:" + DistributorID +" P:"+ ProductID );
			
			long UnitsOrdered = rs.getLong("ordered");
			long UnitsInvoiced = rs.getLong("invoiced");
			long UnitsRequired = UnitsOrdered + UnitsInvoiced;
			
			//System.out.println("Balance: Product ID: "+ProductID + " Available:" + UnitsAvailable + " Required:" + UnitsRequired);
			
			long UnitsShort = 0;
			if (UnitsAvailable < UnitsRequired){
				
				UnitsShort = UnitsRequired - UnitsAvailable;
				
				BackOrderProduct item = new BackOrderProduct();
				
				
				
				item.PRODUCT_ID = ProductID;
				item.UNITS_AVAILABLE = UnitsAvailable;
				item.UNITS_ORDERED = UnitsOrdered;
				item.UNITS_INVOICED = UnitsInvoiced;
				item.UNITS_REQUIRED = UnitsRequired;
				item.UNITS_SHORT = UnitsShort;
				

				if (UnitsShort < UnitsOrdered ){
					item.UNITS_SHORT_THIS_ORDER = UnitsShort;
				}else{
					item.UNITS_SHORT_THIS_ORDER = UnitsOrdered;
				}
				
				ret.add(item);
			}
			
		}
		
		
		s.close();
		ds.dropConnection();

		return ret.toArray(new BackOrderProduct[ret.size()]);
	}
	
	public static void changePromotionsBeforeBackorder(long DistributorID, long OrderID, long UserID) {
		Datasource ds = new Datasource();
		try{
			
			ds.createConnection();
			ds.startTransaction();
			ds.startTransaction();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			StockPosting sp = new StockPosting();
			
			ResultSet rs = s.executeQuery("SELECT mop.product_id, mop.total_units, mop.promotion_id, (select package_id from inventory_products where id = mop.product_id) package_id FROM mobile_order_products mop where mop.id = "+OrderID+" and mop.is_promotion = 1"); 
			while(rs.next()){
				
				int ProductID = rs.getInt(1);
				int PackageID = rs.getInt(4);
				int OrderUnits = rs.getInt(2);
				int PromotionID = rs.getInt(3);
				
				boolean shouldProductIDChange = false;
				int UpdatedProductID = 0;
				
				
				long AvailableStock = sp.getClosingBalanceExInvoiced(DistributorID, ProductID, new Date());
				
				
				//System.out.println("Product ID:"+ProductID+",Order Units:"+OrderUnits+",Available Stock: "+AvailableStock+",PromotionID:"+PromotionID);
				
				
				if (OrderUnits > AvailableStock){
					
					// Get Other Product IDs against this promotion
					ResultSet rs2 = s2.executeQuery("SELECT isppb.brand_id, (select id from inventory_products where package_id = "+PackageID+" and brand_id = isppb.brand_id and category_id = 1) product_id FROM inventory_sales_promotions_products_brands isppb where isppb.id="+PromotionID+" and isppb.type_id = 2 and isppb.package_id = "+PackageID);
					//System.out.println("SELECT isppb.brand_id, (select id from inventory_products where package_id = "+PackageID+" and brand_id = isppb.brand_id and category_id = 1) product_id FROM inventory_sales_promotions_products_brands isppb where isppb.id="+PromotionID+" and isppb.type_id = 2 and isppb.package_id = "+PackageID);
					while(rs2.next()){
						
						int ProspectProductID = rs2.getInt(2);
						
						//System.out.println("Prospect Product ID:"+ProspectProductID+",Order Units:"+OrderUnits+",Available Stock: "+sp.getClosingBalanceExInvoiced(DistributorID, ProspectProductID, new Date())+",PromotionID:"+PromotionID);
						
						if (OrderUnits < sp.getClosingBalanceExInvoiced(DistributorID, ProspectProductID, new Date())){
							
							UpdatedProductID = ProspectProductID;
							shouldProductIDChange = true;
							break;
							
						}
						
					}
					
					if (shouldProductIDChange){
						
						s2.executeUpdate("update mobile_order_products set product_id = "+UpdatedProductID+", is_promotion_changed = 1, promotion_changed_on = now() where id = "+OrderID+" and product_id = "+ProductID+" and promotion_id = "+PromotionID);
						s2.executeUpdate("insert into mobile_order_products_promotion_change (id, product_id, promotion_id, product_id_changed_to) values ("+OrderID+","+ProductID+","+PromotionID+","+UpdatedProductID+")");
						ds.commit();
						
					}
					
				}
				
			}
			
			sp.close();
			
			s2.close();
			s.close();
			
		}catch(Exception e){
			System.out.println(e);
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static boolean insertBackOrder(long DistributorID, long OrderID, long UserID) {
		
		changePromotionsBeforeBackorder(DistributorID, OrderID, UserID);
		
		boolean isBackOrderCase = false;
		
		Datasource ds = new Datasource();
		try{
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			
			s.executeUpdate("create temporary table temp_backorder_products (product_id smallint(4), total_units int(10), is_promotion tinyint(1), package_id smallint(4), brand_id smallint(4), promotion_id int(8), is_backorder tinyint(1))");
			
			
			
			
			// Checking if non-promotion products are in stock
			ResultSet rs = s2.executeQuery("SELECT ipv.package_id, ipv.brand_id, mop.product_id, mop.total_units ordered, ("+
					"select sum(total_units) from inventory_sales_invoices isi, inventory_sales_invoices_products isip where isi.id = isip.id and isi.is_dispatched = 0 and isi.distributor_id = "+DistributorID+" and isip.product_id = mop.product_id"+
				") invoiced, mop.promotion_id, ipv.unit_per_sku FROM mobile_order_products mop, inventory_products_view ipv where mop.product_id = ipv.product_id and mop.id = "+OrderID+" and mop.is_promotion = 0");
			while(rs.next()){
				int PackageID = rs.getInt("package_id");
				int BrandID = rs.getInt("brand_id");			
				int ProductID = rs.getInt("product_id");
				
				
				int UnitPerSKU = rs.getInt("unit_per_sku");
				
				long UnitsAvailable = getCurrentBalance(DistributorID, ProductID);
				
				long UnitsAvailableRawCases[] = Utilities.getRawCasesAndUnits(UnitsAvailable, UnitPerSKU);
				
				UnitsAvailable = UnitsAvailableRawCases[0] * UnitPerSKU;  // Exclude bottles in available Qty
				
				
				//System.out.println("i: D:" + DistributorID +" P:"+ ProductID );
				long UnitsOrdered = rs.getLong("ordered");
				long UnitsInvoiced = rs.getLong("invoiced");
				long UnitsRequired = UnitsOrdered + UnitsInvoiced;
				
				long UnitsShort = 0;
				
				s.executeUpdate("insert into temp_backorder_products (package_id, brand_id, product_id, total_units, is_promotion) values ("+PackageID+","+BrandID+","+ProductID+", "+UnitsOrdered+", 0)");
				
				//System.out.println("Product ID: "+ProductID + " Available:" + UnitsAvailable + " Required:" + UnitsRequired);
				if (UnitsAvailable < UnitsRequired){
					isBackOrderCase = true;
					
					UnitsShort = UnitsRequired - UnitsAvailable;
					if (UnitsShort >= UnitsOrdered ){
						UnitsShort = UnitsOrdered;
					}
					
					s.executeUpdate("insert into temp_backorder_products (package_id, brand_id, product_id, total_units, is_promotion, is_backorder) values ("+PackageID+","+BrandID+","+ProductID+", "+(UnitsShort*-1)+", 0, 1)");
				}
				
			}
			ArrayList<Integer> MatchPackageIDs = new ArrayList<Integer>();
			ArrayList<Integer> MatchBrandIDs = new ArrayList<Integer>();	        				
			
			// Checking if promotion products are in stock
			ResultSet rs11 = s2.executeQuery("SELECT ipv.package_id, ipv.brand_id, mop.product_id, mop.total_units ordered, ("+
					"select sum(total_units) from inventory_sales_invoices isi, inventory_sales_invoices_products isip where isi.id = isip.id and isi.is_dispatched = 0 and isi.distributor_id = "+DistributorID+" and isip.product_id = mop.product_id"+
				") invoiced, mop.promotion_id FROM mobile_order_products mop, inventory_products_view ipv where mop.product_id = ipv.product_id and mop.id = "+OrderID+" and mop.is_promotion = 1");
			while(rs11.next()){
				
				MatchPackageIDs.add(rs11.getInt("package_id"));
				MatchBrandIDs.add(rs11.getInt("brand_id"));
				
				int PackageID = rs11.getInt("package_id");
				int BrandID = rs11.getInt("brand_id");
				int ProductID = rs11.getInt("product_id");
				
				
				long UnitsOrdered = rs11.getLong("ordered");
				int PromotionID = rs11.getInt("promotion_id");
				s.executeUpdate("insert into temp_backorder_products (product_id, total_units, is_promotion, package_id, brand_id, promotion_id, is_backorder) values ("+ProductID+", "+(UnitsOrdered*-1)+", 1, "+PackageID+", "+BrandID+", "+PromotionID+", 1)");
				
				long UnitsAvailable = getCurrentBalance(DistributorID, ProductID);
				
				long UnitsInvoiced = rs11.getLong("invoiced");
				long UnitsRequired = UnitsOrdered + UnitsInvoiced;
				
				long UnitsShort = 0;
				
				if (UnitsAvailable < UnitsRequired){
					isBackOrderCase = true;
					
					UnitsShort = UnitsRequired - UnitsAvailable;
					if (UnitsShort >= UnitsOrdered ){
						UnitsShort = UnitsOrdered;
					}
					
					long ReversePromotionUnits = 0;
					int ReversePackageID = 0;
					
					ResultSet rs2 = s.executeQuery("SELECT package_id, total_units FROM inventory_sales_promotions_products where id="+PromotionID+" and type_id = 1 limit 1");
					if (rs2.first()){
						ReversePackageID = rs2.getInt(1);
						ReversePromotionUnits = rs2.getLong(2);
					}
					
					int ReversePromotionProductID = 0;
					long ReversePromotionTotalUnits = 0;
					ResultSet rs3 = s.executeQuery("SELECT product_id, total_units from temp_backorder_products where is_promotion = 0 and package_id = "+ReversePackageID+" and total_units >= "+ReversePromotionUnits);
					if (rs3.first()){
						ReversePromotionProductID = rs3.getInt(1);
						ReversePromotionTotalUnits = rs3.getLong(2);
					}
					
					s.executeUpdate("delete from temp_backorder_products where product_id = "+ReversePromotionProductID+" and is_promotion = 0 and is_backorder = 1");
					s.executeUpdate("insert into temp_backorder_products (product_id, total_units, is_promotion, is_backorder) values ("+ReversePromotionProductID+", "+(ReversePromotionTotalUnits*-1)+", 0, 1)");
					
					
				}
				
			}
			
			if (isBackOrderCase == true){
				
				ArrayList<Integer> ArrayProductIDs = new ArrayList<Integer>();
				ArrayList<Long> ArrayTotalUnits = new ArrayList<Long>();
				
				ResultSet rs2 = s.executeQuery("select product_id, sum(total_units) from temp_backorder_products where is_promotion = 0 group by product_id");
				while(rs2.next()){
					
					int ProductID = rs2.getInt(1);
					long TotalUnits = rs2.getLong(2);
					
					if (TotalUnits > 0){
						ArrayProductIDs.add(ProductID);
						ArrayTotalUnits.add(TotalUnits);
					}
					
				}
				ArrayList<Integer> PromotionIDs = new ArrayList<Integer>();	
				
				ResultSet rs4 = s.executeQuery("SELECT distinct promotion_id FROM mobile_order_products mop where mop.id = "+OrderID+" and mop.is_promotion = 1");
				while(rs4.next()){
					PromotionIDs.add(rs4.getInt(1));
				}
				
				int nMatchPackageIDs[] = ArrayUtils.toPrimitive(MatchPackageIDs.toArray(new Integer[MatchPackageIDs.size()]));
				int nMatchBrandIDs[] = ArrayUtils.toPrimitive(MatchBrandIDs.toArray(new Integer[MatchBrandIDs.size()]));
				
				int nProductIDs[] = ArrayUtils.toPrimitive(ArrayProductIDs.toArray(new Integer[ArrayProductIDs.size()]));
				long nTotalUnits[] = ArrayUtils.toPrimitive(ArrayTotalUnits.toArray(new Long[ArrayTotalUnits.size()]));
				
				for (int k = 0; k < PromotionIDs.size(); k++){
					
					PromotionItem promotions[] = Product.getPromotionItemsMatched(PromotionIDs.get(k), nProductIDs, nTotalUnits, nMatchPackageIDs, nMatchBrandIDs);
						
					for (int i = 0; i < promotions.length; i++){
						
						int iBrandID = 0;
						if (promotions[i].BRANDS.size() > 0){
							iBrandID = promotions[i].BRANDS.get(0);
						}
						
						s.executeUpdate("update temp_backorder_products set total_units = total_units + "+promotions[i].TOTAL_UNITS+" where package_id = "+promotions[i].PACKAGE_ID+" and brand_id = "+iBrandID+" and is_promotion = 1 and promotion_id = "+PromotionIDs.get(k));
						
					}
					
				}
				
				
				s3.executeUpdate("delete from mobile_order_products_backorder where id = "+OrderID);
				
				ResultSet rs5 = s.executeQuery("select product_id, total_units, is_promotion, promotion_id from temp_backorder_products where is_backorder = 1");
				while(rs5.next()){
					
					int ProductIDFinal = rs5.getInt("product_id");
					long TotalUnitsFinal = rs5.getLong("total_units") * -1;
					
					int isPromotionFinal = rs5.getInt("is_promotion");
					int PromotionIDFinal = rs5.getInt("promotion_id");
					
					String PromotionIDString = null;
					
					if (PromotionIDFinal != 0){
						PromotionIDString = PromotionIDFinal + "";
					}
					
					if (TotalUnitsFinal != 0){
						s3.executeUpdate("insert into mobile_order_products_backorder (id, product_id, total_units, is_promotion, promotion_id) values ("+OrderID+", "+ProductIDFinal+", "+TotalUnitsFinal+", "+isPromotionFinal+", "+PromotionIDString+")");
					}
					
				}
				s.executeUpdate("update mobile_order set is_backordered = 1, backordered_on = now(), backordered_by = "+UserID+" where id = "+ OrderID);
				
			}
			
			ds.commit();
			
			s.close();
			s2.close();
			s3.close();
		
		}catch(Exception e){
			System.out.println(e);
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return isBackOrderCase;
	}

	
	
	public static long getCurrentBalance(long DistributorID, int ProductID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Datasource ds = new Datasource();
		ds.createConnection();
		Statement s = ds.createStatement();

		long TotalUnits = 0;
		System.out.println("SELECT sum(total_units_received-total_units_issued) balance FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id = "+DistributorID+" and idsp.product_id = "+ProductID);
		ResultSet rs9 = s.executeQuery("SELECT sum(total_units_received-total_units_issued) balance FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id = "+DistributorID+" and idsp.product_id = "+ProductID);
		if (rs9.first()){
			TotalUnits = rs9.getLong(1);
		}
		
		s.close();
		ds.dropConnection();
		
		return TotalUnits;
	}
	public static double[] getSellingPriceByOutlet(int ProductID, int is_filer, int is_register) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Datasource ds = new Datasource();
		
		ds.createConnection();
		Statement s = ds.createStatement();
		
		double ret[] = new double[5];
		System.out.println("select * from inventory_price_list_products where id in(select id from inventory_price_list where is_filer="+is_filer+" and is_register="+is_register+") and product_id="+ProductID);
		ResultSet rs3 = s.executeQuery("select * from inventory_price_list_products where id in(select id from inventory_price_list where is_filer="+is_filer+" and is_register="+is_register+") and product_id="+ProductID);
		if (rs3.first()) {
			ret[0] = rs3.getDouble("raw_case");
			ret[1] = rs3.getDouble("unit");
		}
		
		s.close();
		ds.dropConnection();
		return ret;
	}
	
	
	public static double[] getSellingPrice(int ProductID, long OutletID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Datasource ds = new Datasource();
		double ret[] = new double[5];
		/*
		 * 0 = Raw Case Rate
		 * 1 = Unit Rate
		 * 2 = Discount Rate - Raw Case
		 * 3 = Discount ID
		 * 4 = Discount Rate - Unit
		*/
		
		long DistributorID = 0;
		long RegionID = 0;

		long PriceListID = 0;

		ds.createConnection();
		Statement s = ds.createStatement();
		Statement s1 = ds.createStatement();
		Statement s2 = ds.createStatement();
		
		ResultSet rs = s.executeQuery("select co.cache_distributor_id, cd.region_id from common_outlets co join common_distributors cd on co.cache_distributor_id = cd.distributor_id where co.id=" + OutletID);
		if (rs.first()){
			DistributorID = rs.getLong(1);
			RegionID = rs.getLong(2);
		}

		ResultSet rs1 = s.executeQuery("select * from inventory_price_list ipl where ipl.is_active = 1 and ipl.id != 1 and curdate() < valid_to order by valid_from");
		while (rs1.next()) {
			long InnerPriceList = rs1.getLong("id");
			ResultSet rs2 = s1.executeQuery("select distributor_id from ("
							+ "select ipld.distributor_id from inventory_price_list_distributors ipld where price_list_id = "+ InnerPriceList+ " union "
							+ "select cdgl.distributor_id from inventory_price_list_distributor_groups ipldg, common_distributor_groups_list cdgl where ipldg.group_id=cdgl.id and ipldg.price_list_id = "	+ InnerPriceList+ " union "
							+ "select cd.distributor_id from inventory_price_list_regions iplr,common_distributors cd where iplr.region_id=cd.region_id and iplr.price_list_id="+ InnerPriceList + ") tab where distributor_id=" + DistributorID);
			if (rs2.first()) {
				PriceListID = InnerPriceList;
			}
		}

		ResultSet rs3 = s.executeQuery("select * from inventory_price_list_products where id=" + PriceListID + " and product_id=" + ProductID);
		if (rs3.first()) {
			ret[0] = rs3.getDouble("raw_case");
			ret[1] = rs3.getDouble("unit");
		} else {
			
			ResultSet rs4 = s2.executeQuery("select * from inventory_price_list_products where id=1 and product_id="+ ProductID);
			if (rs4.first()) {
				ret[0] = rs4.getDouble("raw_case");
				ret[1] = rs4.getDouble("unit");
			}
		}
		
		//reducing hand to hand discount
		ResultSet rs4 = s.executeQuery("SELECT iplhdm.discount, ip.unit_per_sku, iplhdm.sampling_id FROM inventory_price_list_hand_discount_mview iplhdm join inventory_products ip on iplhdm.product_id=ip.id where iplhdm.product_id="+ProductID+" and iplhdm.outlet_id="+OutletID);
		if(rs4.first()){
			ret[0] = ret[0] - rs4.getDouble("discount");
			if(rs4.getInt("unit_per_sku")!=0){
				ret[1] = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(ret[0]/rs4.getDouble("unit_per_sku")));
			}
			ret[2] = rs4.getDouble("discount");
			ret[3] = rs4.getDouble("sampling_id");
			ret[4] = Utilities.parseDouble(Utilities.getDisplayCurrencyFormatSimple(ret[2] / rs4.getInt("unit_per_sku")));
		}
		
		s.close();
		ds.dropConnection();
		return ret;
	}
	
public static PromotionItem[] getPromotionItems(long OutletID, int ProductID, long TotalUnits) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		
		if (ProductID != 0 && TotalUnits != 0){
			
		Datasource ds = new Datasource();
		ds.createConnection();
		Statement s = ds.createStatement();
		Statement s2 = ds.createStatement();
		Statement s3 = ds.createStatement();
		
		List <PromotionItem>list = new ArrayList<PromotionItem>();
		
		// Creating and populating temporary table for summaries
		s.executeUpdate("create temporary table temp_product_summary (product_id int(11), units bigint(11))");
			s.executeUpdate("insert into temp_product_summary values ("+ProductID+","+TotalUnits+")");
	
		s.executeUpdate("delete from temp_product_summary where product_id in (SELECT product_id FROM inventory_products_view where category_id != 1)");
		
		// Looping through active promotions against the outlet
		ResultSet rs = s.executeQuery("SELECT * FROM inventory_sales_promotions_active_mview where outlet_id = "+OutletID);
		while(rs.next()){
			long AppliedPromotionID = rs.getLong("product_promotion_id");
			int PackageIDSales = 0;
			long UnitsSales = 0;
			long UnitsOrdered = 0;
			boolean isBrandSpecific = false;
			
			// Getting Package
			ResultSet rs2 = s2.executeQuery("SELECT package_id, total_units FROM inventory_sales_promotions_products where id="+AppliedPromotionID+" and type_id = 1");
			if (rs2.first()){
				PackageIDSales = rs2.getInt("package_id");
				UnitsSales = rs2.getLong("total_units");
			}
			
			String SalesBrandIDs = "";
			// Looping through brands
			ResultSet rs3 = s2.executeQuery("SELECT brand_id FROM inventory_sales_promotions_products_brands where id="+AppliedPromotionID+" and type_id = 1 and package_id = "+PackageIDSales);
			while(rs3.next()){
				isBrandSpecific = true;
				if (!rs3.isFirst()){
					SalesBrandIDs = SalesBrandIDs + ",";
				}
				SalesBrandIDs = SalesBrandIDs + rs3.getString(1);
			}
			
			String WhereSales = "";
			if (isBrandSpecific == true){
				WhereSales =  "and brand_id in ("+SalesBrandIDs+")";
			}
			
			// Getting sum of sales order against Package ID and Brand IDs
			ResultSet rs4 = s2.executeQuery("select sum(tps.units) from temp_product_summary tps, inventory_products_view ipv where tps.product_id = ipv.product_id and ipv.package_id = "+PackageIDSales+" "+WhereSales);
			while(rs4.next()){
				UnitsOrdered = rs4.getLong(1);
			}
			
			long PromotionUnits = 0;
			if (UnitsSales != 0){
				PromotionUnits = UnitsOrdered / UnitsSales;
			}
						
			// If Promotion is applicable
			if (PromotionUnits > 0){
				// Getting Package
				ResultSet rs5 = s2.executeQuery("SELECT ispp.package_id, ispp.total_units, ip.label, ip.unit_per_case FROM inventory_sales_promotions_products ispp, inventory_packages ip where ispp.package_id = ip.id and ispp.id="+AppliedPromotionID+" and ispp.type_id = 2");
				while (rs5.next()){
					PromotionItem item = new PromotionItem();
					
					item.PROMOTION_ID = AppliedPromotionID;
					item.PACKAGE_ID = rs5.getInt("package_id");
					item.TOTAL_UNITS = (rs5.getLong("total_units") * PromotionUnits);
					item.PACKAGE_LABEL = rs5.getString("label");
					item.UNIT_PER_SKU = rs5.getInt("unit_per_case");
					
					// Getting allowed brands
					ResultSet rs6 = s3.executeQuery("SELECT isppb.brand_id, ib.label FROM inventory_sales_promotions_products_brands isppb, inventory_brands ib where isppb.brand_id = ib.id and isppb.id="+AppliedPromotionID+" and isppb.type_id = 2 and isppb.package_id = "+item.PACKAGE_ID);
					while(rs6.next()){
						item.BRANDS.add(rs6.getInt(1));
						item.BRAND_LABELS.add(rs6.getString(2));
					}
					
					list.add(item);
				}
			}
		}
		
		s2.close();
		s.close();
		ds.dropConnection();
		PromotionItem ret[] = list.toArray(new PromotionItem[list.size()]);
		
		return ret;
		
		}else{
			return null;
		}
		
	}

	
	public static PromotionItem[] getPromotionItems(long OutletID, int ProductID[], long TotalUnits[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		
		if (ProductID != null && TotalUnits != null){
			
		Datasource ds = new Datasource();
		ds.createConnection();
		Statement s = ds.createStatement();
		Statement s2 = ds.createStatement();
		Statement s3 = ds.createStatement();
		
		List <PromotionItem>list = new ArrayList<PromotionItem>();
		
		// Creating and populating temporary table for summaries
		s.executeUpdate("create temporary table temp_product_summary (product_id int(11), units bigint(11))");
		for(int i = 0; i < ProductID.length; i++){
			s.executeUpdate("insert into temp_product_summary values ("+ProductID[i]+","+TotalUnits[i]+")");
		}
		s.executeUpdate("delete from temp_product_summary where product_id in (SELECT product_id FROM inventory_products_view where category_id != 1)");
		
		// Looping through active promotions against the outlet
		ResultSet rs = s.executeQuery("SELECT * FROM inventory_sales_promotions_active_mview where outlet_id = "+OutletID);
		while(rs.next()){
			long AppliedPromotionID = rs.getLong("product_promotion_id");
			int PackageIDSales = 0;
			long UnitsSales = 0;
			long UnitsOrdered = 0;
			boolean isBrandSpecific = false;
			
			// Getting Package
			ResultSet rs2 = s2.executeQuery("SELECT package_id, total_units FROM inventory_sales_promotions_products where id="+AppliedPromotionID+" and type_id = 1");
			if (rs2.first()){
				PackageIDSales = rs2.getInt("package_id");
				UnitsSales = rs2.getLong("total_units");
			}
			
			String SalesBrandIDs = "";
			// Looping through brands
			ResultSet rs3 = s2.executeQuery("SELECT brand_id FROM inventory_sales_promotions_products_brands where id="+AppliedPromotionID+" and type_id = 1 and package_id = "+PackageIDSales);
			while(rs3.next()){
				isBrandSpecific = true;
				if (!rs3.isFirst()){
					SalesBrandIDs = SalesBrandIDs + ",";
				}
				SalesBrandIDs = SalesBrandIDs + rs3.getString(1);
			}
			
			String WhereSales = "";
			if (isBrandSpecific == true){
				WhereSales =  "and brand_id in ("+SalesBrandIDs+")";
			}
			
			// Getting sum of sales order against Package ID and Brand IDs
			ResultSet rs4 = s2.executeQuery("select sum(tps.units) from temp_product_summary tps, inventory_products_view ipv where tps.product_id = ipv.product_id and ipv.package_id = "+PackageIDSales+" "+WhereSales);
			while(rs4.next()){
				UnitsOrdered = rs4.getLong(1);
			}
			
			long PromotionUnits = 0;
			if (UnitsSales != 0){
				PromotionUnits = UnitsOrdered / UnitsSales;
			}
						
			// If Promotion is applicable
			if (PromotionUnits > 0){
				// Getting Package
				ResultSet rs5 = s2.executeQuery("SELECT ispp.package_id, ispp.total_units, ip.label, ip.unit_per_case FROM inventory_sales_promotions_products ispp, inventory_packages ip where ispp.package_id = ip.id and ispp.id="+AppliedPromotionID+" and ispp.type_id = 2");
				while (rs5.next()){
					PromotionItem item = new PromotionItem();
					
					item.PROMOTION_ID = AppliedPromotionID;
					item.PACKAGE_ID = rs5.getInt("package_id");
					item.TOTAL_UNITS = (rs5.getLong("total_units") * PromotionUnits);
					item.PACKAGE_LABEL = rs5.getString("label");
					item.UNIT_PER_SKU = rs5.getInt("unit_per_case");
					
					// Getting allowed brands
					ResultSet rs6 = s3.executeQuery("SELECT isppb.brand_id, ib.label FROM inventory_sales_promotions_products_brands isppb, inventory_brands ib where isppb.brand_id = ib.id and isppb.id="+AppliedPromotionID+" and isppb.type_id = 2 and isppb.package_id = "+item.PACKAGE_ID);
					while(rs6.next()){
						item.BRANDS.add(rs6.getInt(1));
						item.BRAND_LABELS.add(rs6.getString(2));
					}
					
					list.add(item);
				}
			}
		}
		
		s2.close();
		s.close();
		ds.dropConnection();
		PromotionItem ret[] = list.toArray(new PromotionItem[list.size()]);
		
		return ret;
		
		}else{
			return null;
		}
		
	}

	public static PromotionItem[] getPromotionItemsMatched(int PromotionID, int ProductID[], long TotalUnits[], int MatchPackages[], int MatchBrands[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		
		if (ProductID != null && TotalUnits != null && MatchPackages != null && MatchBrands != null){
		
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			
			List <PromotionItem>list = new ArrayList<PromotionItem>();
			
			// Creating and populating temporary table for summaries
			s.executeUpdate("create temporary table temp_product_summary (product_id int(11), units bigint(11))");
			
			for(int i = 0; i < ProductID.length; i++){
				s.executeUpdate("insert into temp_product_summary values ("+ProductID[i]+","+TotalUnits[i]+")");
			}
			s.executeUpdate("delete from temp_product_summary where product_id in (SELECT product_id FROM inventory_products_view where category_id != 1)");
			
			
			
			// Looping through active promotions against the outlet
			long AppliedPromotionID = PromotionID;
			int PackageIDSales = 0;
			long UnitsSales = 0;
			long UnitsOrdered = 0;
			boolean isBrandSpecific = false;
			
			// Getting Package
			ResultSet rs2 = s2.executeQuery("SELECT package_id, total_units FROM inventory_sales_promotions_products where id="+AppliedPromotionID+" and type_id = 1");
			if (rs2.first()){
				PackageIDSales = rs2.getInt("package_id");
				UnitsSales = rs2.getLong("total_units");
			}
				
			String SalesBrandIDs = "";
			// Looping through brands
			ResultSet rs3 = s2.executeQuery("SELECT brand_id FROM inventory_sales_promotions_products_brands where id="+AppliedPromotionID+" and type_id = 1 and package_id = "+PackageIDSales);
			while(rs3.next()){
				isBrandSpecific = true;
				if (!rs3.isFirst()){
					SalesBrandIDs = SalesBrandIDs + ",";
				}
				SalesBrandIDs = SalesBrandIDs + rs3.getString(1);
			}
			
			String WhereSales = "";
			if (isBrandSpecific == true){
				WhereSales =  "and brand_id in ("+SalesBrandIDs+")";
			}
				
				// Getting sum of sales order against Package ID and Brand IDs
			ResultSet rs4 = s2.executeQuery("select sum(tps.units) from temp_product_summary tps, inventory_products_view ipv where tps.product_id = ipv.product_id and ipv.package_id = "+PackageIDSales+" "+WhereSales);
			while(rs4.next()){
				UnitsOrdered = rs4.getLong(1);
			}
			
			long PromotionUnits = 0;
			if (UnitsSales != 0){
				PromotionUnits = UnitsOrdered / UnitsSales;
			}
						
			// If Promotion is applicable
			if (PromotionUnits > 0){
				// Getting Package
				//System.out.println("SELECT ispp.package_id, ispp.total_units, ip.label, ip.unit_per_case FROM inventory_sales_promotions_products ispp, inventory_packages ip where ispp.package_id = ip.id and ispp.id="+AppliedPromotionID+" and ispp.type_id = 2");
				ResultSet rs5 = s2.executeQuery("SELECT ispp.package_id, ispp.total_units, ip.label, ip.unit_per_case FROM inventory_sales_promotions_products ispp, inventory_packages ip where ispp.package_id = ip.id and ispp.id="+AppliedPromotionID+" and ispp.type_id = 2");
				while (rs5.next()){
					PromotionItem item = new PromotionItem();
					
					item.PROMOTION_ID = AppliedPromotionID;
					item.PACKAGE_ID = rs5.getInt("package_id");
					item.TOTAL_UNITS = (rs5.getLong("total_units") * PromotionUnits);
					item.PACKAGE_LABEL = rs5.getString("label");
					item.UNIT_PER_SKU = rs5.getInt("unit_per_case");
					
					int SelectedBrandID = 0;
					String SelectedBrandIDs = "0";
					for (int i = 0; i < MatchPackages.length; i++){
						if (item.PACKAGE_ID == MatchPackages[i]){
							SelectedBrandID = MatchBrands[i];
							SelectedBrandIDs += ","+MatchBrands[i];
							//System.out.println(AppliedPromotionID +"Matched");
						}
					}
					
					// Getting allowed brands
					//System.out.println("SELECT isppb.brand_id, ib.label FROM inventory_sales_promotions_products_brands isppb, inventory_brands ib where isppb.brand_id = ib.id and isppb.id="+AppliedPromotionID+" and isppb.type_id = 2 and isppb.package_id = "+item.PACKAGE_ID+" and isppb.brand_id = "+SelectedBrandID);
					ResultSet rs6 = s3.executeQuery("SELECT isppb.brand_id, ib.label FROM inventory_sales_promotions_products_brands isppb, inventory_brands ib where isppb.brand_id = ib.id and isppb.id="+AppliedPromotionID+" and isppb.type_id = 2 and isppb.package_id = "+item.PACKAGE_ID+" and isppb.brand_id in ("+SelectedBrandIDs+")");
					while(rs6.next()){
						item.BRANDS.add(rs6.getInt(1));
						item.BRAND_LABELS.add(rs6.getString(2));
					}
					
					if (item.BRANDS.size() > 0){
						list.add(item);
					}
					
				}
			}
			
			s2.close();
			s.close();
			ds.dropConnection();
			PromotionItem ret[] = list.toArray(new PromotionItem[list.size()]);
			
			return ret;
		
		}else{
			return null;
		}
		
	}

	public Product(int ProductID) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

		Datasource ds = new Datasource();
		ds.createConnection();

		Statement s = ds.createStatement();

		ResultSet rs = s
				.executeQuery("select *, (select liquid_in_ml from inventory_packages where id=package_id) liquid_in_ml from inventory_products where id = "
						+ ProductID);
		if (rs.first()) {
			PRODUCT_ID = ProductID;
			CATEGORY_ID = rs.getInt("category_id");
			PACKAGE_ID = rs.getInt("package_id");
			BRAND_ID = rs.getInt("brand_id");
			SAP_CODE = rs.getInt("sap_code");
			UNIT_PER_SKU = rs.getInt("unit_per_sku");
			LIQUID_IN_ML = rs.getInt("liquid_in_ml");
			EXISTS = true;
		}

		rs.close();
		s.close();
		ds.dropConnection();

	}

	public Product(long SAPCode) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

		Datasource ds = new Datasource();
		ds.createConnection();

		Statement s = ds.createStatement();

		ResultSet rs = s
				.executeQuery("select *, (select liquid_in_ml from inventory_packages where id=package_id) liquid_in_ml from inventory_products where sap_code = "
						+ SAPCode);
		if (rs.first()) {
			PRODUCT_ID = rs.getInt("id");
			CATEGORY_ID = rs.getInt("category_id");
			PACKAGE_ID = rs.getInt("package_id");
			BRAND_ID = rs.getInt("brand_id");
			SAP_CODE = SAPCode;
			UNIT_PER_SKU = rs.getInt("unit_per_sku");
			LIQUID_IN_ML = rs.getInt("liquid_in_ml");
			EXISTS = true;
		}

		rs.close();
		s.close();
		ds.dropConnection();

	}

	public Product(int CategoryID, int PackageID, int BrandID) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

		Datasource ds = new Datasource();
		ds.createConnection();

		Statement s = ds.createStatement();

		ResultSet rs = s
				.executeQuery("select *, (select liquid_in_ml from inventory_packages where id=package_id) liquid_in_ml from inventory_products where category_id = "
						+ CategoryID
						+ " and package_id = "
						+ PackageID
						+ " and brand_id = " + BrandID);
		if (rs.first()) {
			PRODUCT_ID = rs.getInt("id");
			CATEGORY_ID = rs.getInt("category_id");
			PACKAGE_ID = rs.getInt("package_id");
			BRAND_ID = rs.getInt("brand_id");
			SAP_CODE = rs.getInt("sap_code");
			UNIT_PER_SKU = rs.getInt("unit_per_sku");
			LIQUID_IN_ML = rs.getLong("liquid_in_ml");
			EXISTS = true;
		}

		rs.close();
		s.close();
		ds.dropConnection();

	}

}
