package com.pbc.bi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class BiAveragePrice {

	public static void main(String[] args) {
		
		try{
			
			processStep1(); // Fetch from SAP, breakup
			processStep3(); // Promotion isolated
			processStep4(); // Promotion
			
			/*
			Step 5
			
			insert into bi_average_price_monthly SELECT bapi.year, bapi.month, bapi.customer_id, sum(quantity), sum(gross_value), sum(upfront_discount), sum(free_stock), sum(freight), sum(unloading) FROM peplogs.bi_average_price_invoice bapi where vbeln in (select invoice_no from pep.inventory_delivery_note) group by bapi.year, bapi.month, bapi.customer_id;
			
			
			update peplogs.bi_average_price_monthly set upfront_discount = upfront_discount * -1;
			update peplogs.bi_average_price_monthly set freight = freight * -1
			update peplogs.bi_average_price_monthly set unloading = unloading * -1
			update peplogs.bi_average_price_monthly set upfront_discount = 0 where upfront_discount = -0;
			 update peplogs.bi_average_price_monthly set freight = 0 where freight = -0;
			 update peplogs.bi_average_price_monthly set unloading = 0 where unloading = -0;
			*/
			
			processStep5(); // Per Case Sampling
			processStep6(); // Per Case Sampling
			
			processStep7(); // Convert table
			processStep8();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public static void processStep8(){
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			
			
			
			
			ResultSet rs = s.executeQuery("SELECT distinct customer_id FROM "+ds.logDatabaseName()+".bi_average_price_monthly_list_master");
			while(rs.next()){
				ResultSet rs2 = s2.executeQuery("SELECT rsm_id, (select display_name from users where id=rsm_id) rsm_name, category_id, (SELECT label FROM common_distributors_categories where id=category_id) category_name FROM common_distributors where distributor_id="+rs.getString(1));
				if(rs2.first()){
					s3.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_monthly_list_master set rsm_id="+rs2.getString("rsm_id")+", rsm_name='"+rs2.getString("rsm_name")+"', category_id="+rs2.getString("category_id")+", category_name='"+rs2.getString("category_name")+"' where customer_id="+rs.getString(1));
					//System.out.println("update "+ds.logDatabaseName()+".bi_average_price_monthly_list_master set rsm_id="+rs2.getString("rsm_id")+", rsm_name='"+rs2.getString("rsm_name")+"', category_id="+rs2.getString("category_id")+", category_name='"+rs2.getString("category_name")+"' where customer_id="+rs.getString(1));
				}
			}
			
			s3.close();
			s2.close();
			s.close();
			
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException e) {
			// TODO Auto-generated catch block
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
	
	public static void processStep1() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		Datasource dsSAP = new Datasource();
		Datasource ds = new Datasource();
		
		try{
			dsSAP.createConnectionToSAPDB();
			ds.createConnection();
			ds.startTransaction();
			
			Statement sSAP = dsSAP.createStatement();
			Statement s2SAP = dsSAP.createStatement();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			
			
			s.executeUpdate("delete from "+ds.logDatabaseName()+".bi_average_price_invoice");
			s.executeUpdate("delete from "+ds.logDatabaseName()+".bi_average_price_invoice_other_products");
			s.executeUpdate("delete from "+ds.logDatabaseName()+".bi_average_price_invoice_promotions");
			s.executeUpdate("delete from "+ds.logDatabaseName()+".bi_average_price_monthly");
			
			
			
			
			
			
			ResultSet rsSAP = sSAP.executeQuery("select vbrk.vbeln, vbrk.fkart, vbrk.knumv, vbrp.posnr, vbrk.kurrf_dat, vbrp.pstyv, vbrp.matnr, vbrp.fkimg quantity, (vbrp.kzwi1+ /*vbrp.kzwi2 + vbrp.kzwi3 +*/ vbrp.kzwi6) gross_value, vbrp.kzwi4 unloading, vbrp.kzwi5 freight, vbrp.vrkme, vbrk.kunrg from sapsr3.vbrp vbrp join sapsr3.vbrk on vbrk.vbeln = vbrp.vbeln where vbrk.fkart in ('ZDIS', 'ZMRS', 'ZCLA','ZDFR') and vbrk.fksto != 'X' and vbrk.kurrf_dat between '20150501' and '20151231' order by vbrk.vbeln");
			while(rsSAP.next()){
				
				
				String posnr = rsSAP.getString("posnr");
				String vrkme = rsSAP.getString("VRKME");
				long CustomerID = Utilities.parseLong(rsSAP.getString("kunrg"));
				
				boolean isBottle = false;
				if (vrkme.equals("BOT")){
					isBottle = true;
				}
				
				long SAPCode = Utilities.parseLong(rsSAP.getString("matnr"));
				long VBELN = rsSAP.getLong(1);
				long quantity = rsSAP.getInt("quantity");
				
				boolean isPromotion = false;
				String pstyv = rsSAP.getString("pstyv");
				if (pstyv.equals("TANN")){
					isPromotion = true;
				}
				
				int PackageID = 0;
				int ProductID = 0;
				int TypeID = 0;
				int CategoryID = 0;
				ResultSet rs3 = s.executeQuery("select package_id, id, type_id, category_id from inventory_products where sap_code = "+SAPCode);
				if (rs3.first()){
					PackageID = rs3.getInt(1);
					ProductID = rs3.getInt(2);
					TypeID = rs3.getInt(3);
					CategoryID = rs3.getInt(4);
				}
				
				
				if (isPromotion){
					
					if (isBottle){
						s.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_invoice_promotions (vbeln, product_id, cases, bottles, gross_value, freight, unloading, posnr) values ("+VBELN+","+ProductID+",0,"+quantity+","+rsSAP.getDouble("gross_value")+","+(rsSAP.getDouble("freight")*-1)+","+(rsSAP.getDouble("unloading")*-1)+", "+posnr+")");	
					}else{
						s.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_invoice_promotions (vbeln, product_id, cases, bottles, gross_value, freight, unloading, posnr) values ("+VBELN+","+ProductID+","+quantity+",0,"+rsSAP.getDouble("gross_value")+","+(rsSAP.getDouble("freight")*-1)+","+(rsSAP.getDouble("unloading")*-1)+","+posnr+")");
					}
					
					
				}else{
					
					if (!isBottle){
						
						if (CategoryID == 1 && PackageID != 13 && PackageID != 14 && PackageID != 13 && PackageID != 20 && PackageID != 10 && PackageID != 19){
							
							boolean alreadyExists = false;
							ResultSet rs2 = s.executeQuery("select * from "+ds.logDatabaseName()+".bi_average_price_invoice where vbeln = "+VBELN+" and package_id = "+PackageID+" and product_type_id = "+TypeID);
							if (rs2.first()){
								alreadyExists = true;
							}
							
							double UpfrontDiscount = 0;
							ResultSet rs4 = s.executeQuery("select kbetr from sap_konv where knumv = "+rsSAP.getLong("knumv")+" and kposn = "+rsSAP.getInt("posnr"));
							if (rs4.first()){
								UpfrontDiscount = rs4.getDouble(1);
							}
							UpfrontDiscount = UpfrontDiscount * rsSAP.getInt("quantity");
							
							
							
							
							if (alreadyExists){
								s.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_invoice set quantity = quantity + "+rsSAP.getInt("quantity")+", gross_value = gross_value + "+rsSAP.getDouble("gross_value")+", freight = freight + "+rsSAP.getDouble("freight")+", unloading = unloading + "+rsSAP.getDouble("unloading")+", upfront_discount = upfront_discount + "+UpfrontDiscount+" where vbeln = "+rsSAP.getLong(1)+" and package_id = "+PackageID+" and product_type_id = "+TypeID);
							}else{
								s.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_invoice (vbeln, fkart, knumv, posnr, kurrf_dat, pstyv, matnr, quantity, gross_value, freight, unloading, is_isolated, upfront_discount, customer_id, package_id, product_id, product_type_id) values ("+rsSAP.getLong(1)+",'"+rsSAP.getString("fkart")+"',"+rsSAP.getLong("knumv")+","+rsSAP.getInt("posnr")+",'"+rsSAP.getString("kurrf_dat")+"','"+rsSAP.getString("pstyv")+"',"+rsSAP.getInt("matnr")+","+rsSAP.getInt("quantity")+","+rsSAP.getDouble("gross_value")+","+rsSAP.getDouble("freight")+","+rsSAP.getDouble("unloading")+",1,"+UpfrontDiscount+","+CustomerID+","+PackageID+","+ProductID+","+TypeID+")");
							}
							
							
						}else{
							
							s.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_invoice_other_products (vbeln, product_id, cases, bottles) values ("+VBELN+","+ProductID+","+quantity+",0)");
							
						}
					
					}
				}
				
				ds.commit();
			}
			
			
			
			s.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_invoice set with_promotion = 1 where vbeln in (select vbeln from "+ds.logDatabaseName()+".bi_average_price_invoice_promotions)");
			
			
			ResultSet rs = s2.executeQuery("select vbeln, count(distinct package_id) ct from "+ds.logDatabaseName()+".bi_average_price_invoice group by vbeln having ct > 1");
			while(rs.next()){
				
				s.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_invoice set is_isolated = 0 where vbeln = "+rs.getString(1));
				
			}
			
			
			s.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_invoice set month = date_format(kurrf_dat,'%m'), year = date_format(kurrf_dat,'%Y') ");
			
			s.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_invoice set quantity = 0 where fkart = 'ZCLA' "); // Claims
			
			
			ds.commit();
			
			
		}catch(Exception e){
			ds.rollback();
			e.printStackTrace();
		}finally{
			ds.dropConnection();
			dsSAP.dropConnection();
		}
	}

	public static void processStep3() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		Datasource ds = new Datasource();
		
		try{
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			
			ResultSet rs3 = s.executeQuery("select * from "+ds.logDatabaseName()+".bi_average_price_invoice where with_promotion = 1 and is_isolated = 1");
			while (rs3.next()){
				
				ResultSet rs4 = s2.executeQuery("select sum(gross_value) from "+ds.logDatabaseName()+".bi_average_price_invoice_promotions where vbeln = "+rs3.getLong("vbeln"));
				if (rs4.first()){
					
					s3.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_invoice set free_stock = "+rs4.getDouble(1)+" where vbeln = "+rs3.getLong("vbeln"));
					
				}
				
			}

			ds.commit();
			
		}catch(Exception e){
			ds.rollback();
			e.printStackTrace();
		}finally{
			ds.dropConnection();
		}
	}

	public static void processStep4() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		Datasource ds = new Datasource();
		
		try{
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			Statement s4 = ds.createStatement();
			Statement s5 = ds.createStatement();
			
			ResultSet rs3 = s.executeQuery("select * from "+ds.logDatabaseName()+".bi_average_price_invoice bapi join common_distributors cd on bapi.customer_id = cd.distributor_id where with_promotion = 1 and is_isolated = 0");
			while (rs3.next()){
				
				long InoviceNumber = rs3.getLong("vbeln");
				long CustomerID = rs3.getLong("customer_id");
				int RegionID = rs3.getInt("region_id");
				Date InvoiceDate = rs3.getDate("kurrf_dat");
				int PackageID = rs3.getInt("package_id");
				int TypeID = rs3.getInt("product_type_id");
				
				/*
				// Region - Secondary
				ResultSet rs4 = s2.executeQuery("SELECT group_concat(distinct ipv.product_id) FROM inventory_sales_promotions isp join inventory_sales_promotions_products_brands isppb on isp.id = isppb.id join inventory_products_view ipv on isppb.package_id = ipv.package_id and isppb.brand_id = ipv.brand_id and ipv.category_id = 1 where ipv.type_id = 1 and isppb.type_id = 2 and "+Utilities.getSQLDate(InvoiceDate)+" between isp.valid_from and isp.valid_to and (isp.deactivated_on is null or isp.deactivated_on > "+Utilities.getSQLDate(InvoiceDate)+") and isp.id in ( select id from inventory_sales_promotions_products where package_id = "+PackageID+")  and isp.id in ( select product_promotion_id from inventory_sales_promotions_regions where region_id = "+RegionID+")");
				while(rs4.next()){
					
					String ProductIDs = rs4.getString(1);
					
					ResultSet rs5 = s3.executeQuery("select sum(gross_value) from "+ds.logDatabaseName()+".bi_average_price_invoice_promotions where vbeln = "+rs3.getLong("vbeln")+" and product_id in ("+ProductIDs+")");
					if (rs5.first()){
						s4.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_invoice set free_stock = "+rs5.getDouble(1)+" where vbeln = "+rs3.getLong("vbeln")+" and package_id = "+PackageID);
					}
					
				}
				
				// Distributor - Secondary				
				rs4 = s2.executeQuery("SELECT group_concat(distinct ipv.product_id) FROM inventory_sales_promotions isp join inventory_sales_promotions_products_brands isppb on isp.id = isppb.id join inventory_products_view ipv on isppb.package_id = ipv.package_id and isppb.brand_id = ipv.brand_id and ipv.category_id = 1 where ipv.type_id = 1 and isppb.type_id = 2 and "+Utilities.getSQLDate(InvoiceDate)+" between isp.valid_from and isp.valid_to and (isp.deactivated_on is null or isp.deactivated_on > "+Utilities.getSQLDate(InvoiceDate)+") and isp.id in ( select id from inventory_sales_promotions_products where package_id = "+PackageID+")  and isp.id in ( select product_promotion_id from inventory_sales_promotions_distributors where distributor_id = "+CustomerID+")");
				while(rs4.next()){
					
					String ProductIDs = rs4.getString(1);
					
					ResultSet rs5 = s3.executeQuery("select sum(gross_value) from "+ds.logDatabaseName()+".bi_average_price_invoice_promotions where vbeln = "+rs3.getLong("vbeln")+" and product_id in ("+ProductIDs+")");
					if (rs5.first()){
						s4.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_invoice set free_stock = "+rs5.getDouble(1)+" where vbeln = "+rs3.getLong("vbeln")+" and package_id = "+PackageID);
					}
					
				}
				
				// Region - Primary
				rs4 = s2.executeQuery("SELECT group_concat(distinct ipv.product_id) FROM inventory_sales_promotions_request isp join inventory_sales_promotions_request_products_brands isppb on isp.id = isppb.id join inventory_products_view ipv on isppb.package_id = ipv.package_id and isppb.brand_id = ipv.brand_id and ipv.category_id = 1 where ipv.type_id = 1 and isppb.type_id = 2 and "+Utilities.getSQLDate(InvoiceDate)+" between isp.valid_from and curdate() and (isp.deactivated_on is null or isp.deactivated_on > "+Utilities.getSQLDate(InvoiceDate)+") and isp.id in ( select id from inventory_sales_promotions_request_products where package_id = "+PackageID+")  and isp.id in ( select product_promotion_id from inventory_sales_promotions_request_regions where region_id = "+RegionID+")");
				if (InoviceNumber == 9000369646l){
					System.out.println("SELECT group_concat(distinct ipv.product_id) FROM inventory_sales_promotions_request isp join inventory_sales_promotions_request_products_brands isppb on isp.id = isppb.id join inventory_products_view ipv on isppb.package_id = ipv.package_id and isppb.brand_id = ipv.brand_id and ipv.category_id = 1 where ipv.type_id = 1 and isppb.type_id = 2 and "+Utilities.getSQLDate(InvoiceDate)+" between isp.valid_from and curdate() and (isp.deactivated_on is null or isp.deactivated_on > "+Utilities.getSQLDate(InvoiceDate)+") and isp.id in ( select id from inventory_sales_promotions_request_products where package_id = "+PackageID+")  and isp.id in ( select product_promotion_id from inventory_sales_promotions_request_regions where region_id = "+RegionID+")");
				}
				while(rs4.next()){
					
					String ProductIDs = rs4.getString(1);
					
					ResultSet rs5 = s3.executeQuery("select sum(gross_value) from "+ds.logDatabaseName()+".bi_average_price_invoice_promotions where vbeln = "+rs3.getLong("vbeln")+" and product_id in ("+ProductIDs+")");
					if (rs5.first()){
						s4.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_invoice set free_stock = "+rs5.getDouble(1)+" where vbeln = "+rs3.getLong("vbeln")+" and package_id = "+PackageID);
					}
					
				}
				
				// Distributor - Primary
				rs4 = s2.executeQuery("SELECT group_concat(distinct ipv.product_id) FROM inventory_sales_promotions_request isp join inventory_sales_promotions_request_products_brands isppb on isp.id = isppb.id join inventory_products_view ipv on isppb.package_id = ipv.package_id and isppb.brand_id = ipv.brand_id and ipv.category_id = 1 where ipv.type_id = 1 and isppb.type_id = 2 and "+Utilities.getSQLDate(InvoiceDate)+" between isp.valid_from and curdate() and (isp.deactivated_on is null or isp.deactivated_on > "+Utilities.getSQLDate(InvoiceDate)+") and isp.id in ( select id from inventory_sales_promotions_request_products where package_id = "+PackageID+")  and isp.id in ( select product_promotion_id from inventory_sales_promotions_request_distributors where distributor_id = "+CustomerID+")");
				while(rs4.next()){
					
					String ProductIDs = rs4.getString(1);
					
					ResultSet rs5 = s3.executeQuery("select sum(gross_value) from "+ds.logDatabaseName()+".bi_average_price_invoice_promotions where vbeln = "+rs3.getLong("vbeln")+" and product_id in ("+ProductIDs+")");
					if (rs5.first()){
						s4.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_invoice set free_stock = "+rs5.getDouble(1)+" where vbeln = "+rs3.getLong("vbeln")+" and package_id = "+PackageID);
					}
					
				}
				
				*/
				
				
				
				/*
				
				// Secondary
				ResultSet rs4 = s2.executeQuery("select group_concat(ipv.product_id) from inventory_sales_promotions ispr join inventory_sales_promotions_products isprp on ispr.id = isprp.id join inventory_packages ip on isprp.package_id = ip.id join inventory_sales_promotions_products_brands isprpb on isprp.id= isprpb.id and isprp.package_id = isprpb.package_id and isprp.type_id = isprpb.type_id join inventory_brands ib on isprpb.brand_id = ib.id join inventory_products_view ipv on ipv.package_id = ip.id and ipv.brand_id = ib.id and ipv.category_id = 1 where isprp.id in ("+
						"SELECT product_promotion_id FROM pep.inventory_sales_promotions_regions where region_id in (1) union SELECT product_promotion_id FROM pep.inventory_sales_promotions_distributors where distributor_id = "+CustomerID+
						") and isprp.id in (select id from inventory_sales_promotions_products where package_id = "+PackageID+" and type_id = 1) and isprp.type_id = 2 and "+Utilities.getSQLDate(InvoiceDate)+" between ispr.valid_from and ispr.valid_to and (ispr.deactivated_on is null or ispr.deactivated_on > "+Utilities.getSQLDate(InvoiceDate)+")");
				while(rs4.next()){
					
					String ProductIDs = rs4.getString(1);
					
					ResultSet rs5 = s3.executeQuery("select sum(gross_value) from "+ds.logDatabaseName()+".bi_average_price_invoice_promotions where vbeln = "+rs3.getLong("vbeln")+" and product_id in ("+ProductIDs+")");
					if (rs5.first()){
						s4.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_invoice set free_stock = "+rs5.getDouble(1)+" where vbeln = "+rs3.getLong("vbeln")+" and package_id = "+PackageID+" and product_type_id = "+TypeID);
					}
				}
				
				
				
				// Primary
				rs4 = s2.executeQuery("select group_concat(ipv.product_id) from inventory_sales_promotions_request ispr join inventory_sales_promotions_request_products isprp on ispr.id = isprp.id join inventory_packages ip on isprp.package_id = ip.id join inventory_sales_promotions_request_products_brands isprpb on isprp.id= isprpb.id and isprp.package_id = isprpb.package_id and isprp.type_id = isprpb.type_id join inventory_brands ib on isprpb.brand_id = ib.id join inventory_products_view ipv on ipv.package_id = ip.id and ipv.brand_id = ib.id and ipv.category_id = 1 where isprp.id in ("+
						"SELECT product_promotion_id FROM pep.inventory_sales_promotions_request_regions where region_id in (1) union SELECT product_promotion_id FROM pep.inventory_sales_promotions_request_distributors where distributor_id = "+CustomerID+
						") and isprp.id in (select id from inventory_sales_promotions_request_products where package_id = "+PackageID+" and type_id = 1) and isprp.type_id = 2 and "+Utilities.getSQLDate(InvoiceDate)+" between ispr.valid_from and ispr.valid_to and (ispr.deactivated_on is null or ispr.deactivated_on > "+Utilities.getSQLDate(InvoiceDate)+")");
				while(rs4.next()){
					
					String ProductIDs = rs4.getString(1);
					
					ResultSet rs5 = s3.executeQuery("select sum(gross_value) from "+ds.logDatabaseName()+".bi_average_price_invoice_promotions where vbeln = "+rs3.getLong("vbeln")+" and product_id in ("+ProductIDs+")");
					if (rs5.first()){
						s4.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_invoice set free_stock = "+rs5.getDouble(1)+" where vbeln = "+rs3.getLong("vbeln")+" and package_id = "+PackageID+" and product_type_id = "+TypeID);
					}
				}

				*/
				
				
				
				/*
				// Primary
				String RequestIDs = "";
				ResultSet rs4 = s2.executeQuery("SELECT glop.order_no, glopp.request_id, (select invoice_no from gl_invoice_posting where order_no = glop.order_no) invoice_no, glopp.posnr FROM gl_order_posting_promotions glopp join gl_order_posting glop on glopp.id = glop.id having invoice_no = "+InoviceNumber);
				while(rs4.next()){
					
					if (!rs4.isFirst()){
						RequestIDs += ",";
					}
					
					RequestIDs += rs4.getString(2);
					
				}

				if (RequestIDs.length() != 0){
			
					
					ResultSet rs6 = s4.executeQuery("select group_concat(ip.id), ispr.request_id from inventory_sales_promotions_request ispr join inventory_sales_promotions_request_products_brands isprpb on ispr.id = isprpb.id join inventory_products ip on isprpb.package_id = ip.package_id and isprpb.brand_id = ip.brand_id and ip.category_id = 1 where isprpb.id in ("+
						"select ispr.id from inventory_sales_promotions_request ispr join inventory_sales_promotions_request_products isprp on ispr.id = isprp.id where request_id in ("+RequestIDs+") and type_id = 1 and package_id = "+PackageID+
						") and isprpb.type_id = 2");
					if (rs6.first()){
						
						String RequestID = rs6.getString(2);
						String ProductIDs = rs6.getString(1);
						
						if (InoviceNumber == 9000384369l){
							System.out.println(RequestID+" "+ProductIDs);
						}
						
						ResultSet rs7 = s3.executeQuery("select ");
						
						
						ResultSet rs5 = s3.executeQuery("select sum(gross_value) from "+ds.logDatabaseName()+".bi_average_price_invoice_promotions where vbeln = "+rs3.getLong("vbeln")+" and product_id in ("+ProductIDs+")");
						if (rs5.first()){
							s5.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_invoice set free_stock = "+rs5.getDouble(1)+" where vbeln = "+rs3.getLong("vbeln")+" and package_id = "+PackageID+" and product_type_id = "+TypeID);
						}
						
					}
				
				}
				
					
				//ResultSet rs4 = s.executeQuery("select sum(gross_value) from "+ds.logDatabaseName()+".bi_average_price_invoice_promotions where vbeln = "+rs3.getLong("vbeln")+" and product_id in ("+ProductIDs+")");
				*/
				
				/*
				ResultSet rs4 = s2.executeQuery("SELECT glpp.request_id, glpp.posnr, (SELECT isprp.package_id FROM pep.inventory_sales_promotions_request ispr join inventory_sales_promotions_request_products isprp on ispr.id = isprp.id where ispr.request_id = glpp.request_id and isprp.type_id = 1) package_id FROM pep.gl_order_posting glop join gl_invoice_posting glip on glop.order_no = glip.order_no join gl_order_posting_promotions glpp on glpp.id = glop.id where glip.invoice_no = "+InoviceNumber+" having package_id = "+PackageID);
				while(rs4.next()){
					
					String iRequestID = rs4.getString(1);
					String iposnr = rs4.getString(2);
					int iPackageID = rs4.getInt(3);
					
					
					
					ResultSet rs5 = s3.executeQuery("select sum(gross_value) from "+ds.logDatabaseName()+".bi_average_price_invoice_promotions where vbeln = "+rs3.getLong("vbeln")+" and posnr = "+iposnr);
					if (rs5.first()){
						s5.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_invoice set free_stock = ifnull(free_stock,0) + "+rs5.getDouble(1)+" where vbeln = "+rs3.getLong("vbeln")+" and package_id = "+PackageID+" and product_type_id = "+TypeID);
					}
					
				}

				*/
				
				ResultSet rs4 = s2.executeQuery("SELECT (SELECT biapip.gross_value/((biapip.cases*iipv.unit_per_sku)+biapip.bottles) rate FROM peplogs.bi_average_price_invoice_promotions biapip join pep.inventory_products_view iipv on biapip.product_id = iipv.product_id where vbeln = glip.invoice_no and iipv.package_id = ipv.package_id and iipv.type_id = ipv.type_id limit 1)*glpp.total_units promotion_amount, (SELECT isprp.package_id FROM pep.inventory_sales_promotions_request ispr join inventory_sales_promotions_request_products isprp on ispr.id = isprp.id where ispr.request_id = glpp.request_id and isprp.type_id = 1) package_id FROM pep.gl_order_posting glop join gl_invoice_posting glip on glop.order_no = glip.order_no join gl_order_posting_promotions glpp on glpp.id = glop.id join inventory_products_view ipv on glpp.product_id = ipv.product_id where glip.invoice_no = "+InoviceNumber+" having package_id = "+PackageID);
				while(rs4.next()){
					
					double amount = rs4.getDouble(1);
					
					s5.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_invoice set free_stock = ifnull(free_stock,0) + "+amount+" where vbeln = "+InoviceNumber+" and package_id = "+PackageID+" and product_type_id = "+TypeID);
					
				}
				
				
				
				
			}
			
			ds.commit();
			
		}catch(Exception e){
			ds.rollback();
			e.printStackTrace();
		}finally{
			ds.dropConnection();
		}
	}
	

	public static void processStep6() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		Datasource ds = new Datasource();
		
		try{
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			Statement s4 = ds.createStatement();
			
			
			ResultSet rs = s.executeQuery("select year, month, customer_id, package_id, product_type_id from "+ds.logDatabaseName()+".bi_average_price_monthly order by year, month, customer_id");
			while(rs.next()){
				
				int year = rs.getInt("year");
				int month = rs.getInt("month");
				
				
				int PackageID = rs.getInt("package_id");
				int ProductTypeID = rs.getInt("product_type_id");
				
				long CustomerID = rs.getLong("customer_id");
				
				Date StartDate = Utilities.getStartDateByMonth(month-1, year);
				Date EndDate = Utilities.getEndDateByMonth(month-1, year);
				
				
				// Update names
				ResultSet rs9 = s2.executeQuery("select cd.name, cd.snd_id, (select display_name from users where id = cd.snd_id) snd_name, cd.region_id, (select region_name from common_regions where region_id = cd.region_id) region_name from common_distributors cd where distributor_id = "+CustomerID);
				if (rs9.first()){
					s2.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_monthly set customer_name = '" +rs9.getString(1)+"', snd_id = "+rs9.getInt(2)+", snd_name = '"+rs9.getString("snd_name")+"', region_id = "+rs9.getInt("region_id")+", region_name = '"+rs9.getString("region_name")+"' where customer_id = "+CustomerID);
				}
				
				
				// Lifting for the month
				double TotalLifting = 0;
				ResultSet rs10 = s2.executeQuery("select sum(((idnp.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and idn.created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+ " and idn.distributor_id = "+CustomerID);
				if (rs10.first()){
					TotalLifting = rs10.getDouble(1);
				}
				
				double PackLifting = 0;
				ResultSet rs11 = s2.executeQuery("select sum(((idnp.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml)) from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and ipv.type_id = "+ProductTypeID+" and ipv.package_id = "+PackageID+" and idn.created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+ " and idn.distributor_id = "+CustomerID);
				if (rs11.first()){
					PackLifting = rs11.getDouble(1);
				}
				
				double PercentLifting = 0;
				if (TotalLifting != 0){
					PercentLifting = (PackLifting / TotalLifting) * 100;
				}
				
				
				// per case discount
				ResultSet rs2 = s2.executeQuery("SELECT sum(smap.sampling_amount) FROM pep.sampling_monthly_approval sma join pep.sampling_monthly_approval_percase smap on sma.approval_id = smap.approval_id where sma.month = "+Utilities.getSQLDate(EndDate)+" and sma.distributor_id = "+CustomerID+" and sma.status_id = 1 and smap.package_id = "+PackageID+" and (smap.brand_id in (select distinct brand_id from pep.inventory_products_view where category_id = 1 and package_id = "+PackageID+" and type_id = "+ProductTypeID+") or brand_id = 0)");
				if (rs2.first()){
					s2.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_monthly set secondary_per_case_discount = " +rs2.getDouble(1)+" where year = "+year+" and month = "+month+" and package_id = "+PackageID+" and product_type_id = "+ProductTypeID+" and customer_id = "+CustomerID);
				}
				
				
				// fixed discount
				ResultSet rs3 = s2.executeQuery("SELECT sum(smaf.sampling_amount) FROM pep.sampling_monthly_approval sma join pep.sampling_monthly_approval_fixed smaf on sma.approval_id = smaf.approval_id where sma.month = "+Utilities.getSQLDate(EndDate)+" and sma.distributor_id = "+CustomerID+" and sma.status_id = 1");
				if (rs3.first()){
					s2.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_monthly set secondary_fixed_discount = " +getPercentAmount(PercentLifting, rs3.getDouble(1))+" where year = "+year+" and month = "+month+" and package_id = "+PackageID+" and product_type_id = "+ProductTypeID+" and customer_id = "+CustomerID);
				}
				
				
				// target incentive
				ResultSet rs4 = s2.executeQuery("SELECT scsd.id, (SELECT sum(amount) FROM pep.sampling_credit_slip_distributor_outlets where id = scsd.id) amount FROM pep.sampling_credit_slip_distributor scsd where scsd.month = "+month+" and scsd.year = "+year+" and distributor_id = "+CustomerID+" and type_id = 1");
				if (rs4.first()){
					s2.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_monthly set primary_target_incentive = " +getPercentAmount(PercentLifting, rs4.getDouble(2))+" where year = "+year+" and month = "+month+" and package_id = "+PackageID+" and product_type_id = "+ProductTypeID+" and customer_id = "+CustomerID);
				}
				
				// lifting incentive
				ResultSet rs5 = s2.executeQuery("SELECT scsd.id, (SELECT sum(amount) FROM pep.sampling_credit_slip_distributor_outlets where id = scsd.id) amount FROM pep.sampling_credit_slip_distributor scsd where scsd.month = "+month+" and scsd.year = "+year+" and distributor_id = "+CustomerID+" and type_id = 5");
				if (rs5.first()){
					s2.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_monthly set lifting_incentive = " +getPercentAmount(PercentLifting, rs5.getDouble(2))+" where year = "+year+" and month = "+month+" and package_id = "+PackageID+" and product_type_id = "+ProductTypeID+" and customer_id = "+CustomerID);
				}

				// distributor sales incentive 
				ResultSet rs6 = s2.executeQuery("SELECT scsd.id, (SELECT sum(amount) FROM pep.sampling_credit_slip_distributor_outlets where id = scsd.id) amount FROM pep.sampling_credit_slip_distributor scsd where scsd.month = "+month+" and scsd.year = "+year+" and distributor_id = "+CustomerID+" and type_id in (7,6)");
				if (rs6.first()){
					s2.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_monthly set distributor_sales_incentive = " +getPercentAmount(PercentLifting, rs6.getDouble(2))+" where year = "+year+" and month = "+month+" and package_id = "+PackageID+" and product_type_id = "+ProductTypeID+" and customer_id = "+CustomerID);
				}
				
				// party order
				ResultSet rs7 = s2.executeQuery("SELECT scsd.id, (SELECT sum(amount) FROM pep.sampling_credit_slip_distributor_outlets where id = scsd.id) amount FROM pep.sampling_credit_slip_distributor scsd where scsd.month = "+month+" and scsd.year = "+year+" and distributor_id = "+CustomerID+" and type_id in (8,4,2)");
				if (rs7.first()){
					s2.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_monthly set party_order = " +getPercentAmount(PercentLifting, rs7.getDouble(2))+" where year = "+year+" and month = "+month+" and package_id = "+PackageID+" and product_type_id = "+ProductTypeID+" and customer_id = "+CustomerID);
				}
				
				// loader driver incentive
				ResultSet rs8 = s2.executeQuery("SELECT scsd.id, (SELECT sum(amount) FROM pep.sampling_credit_slip_distributor_outlets where id = scsd.id) amount FROM pep.sampling_credit_slip_distributor scsd where scsd.month = "+month+" and scsd.year = "+year+" and distributor_id = "+CustomerID+" and type_id = 3");
				if (rs8.first()){
					s2.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_monthly set loader_driver_incentive = " +getPercentAmount(PercentLifting, rs8.getDouble(2))+" where year = "+year+" and month = "+month+" and package_id = "+PackageID+" and product_type_id = "+ProductTypeID+" and customer_id = "+CustomerID);
				}
				
				
				
			}
			
			
			ds.commit();
			
		}catch(Exception e){
			ds.rollback();
			e.printStackTrace();
		}finally{
			ds.dropConnection();
		}
	}
	
	public static double getPercentAmount(double percent, double amount){
		
		double ret = (amount * percent) / 100; 
		
		return ret;
		
	}
	
	/*
	public static void processStep6() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		Datasource ds = new Datasource();
		
		try{
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			Statement s4 = ds.createStatement();
			
			s3.executeUpdate("delete from "+ds.logDatabaseName()+".bi_average_price_monthly_list");
			
			
			ResultSet rs = s.executeQuery("select * from "+ds.logDatabaseName()+".bi_average_price_monthly where customer_id != 200769 order by year, month, customer_id");
			while(rs.next()){
				
				int year = rs.getInt("year");
				int month = rs.getInt("month");
				long SND_ID = rs.getLong("snd_id");
				long CustomerID = rs.getLong("customer_id");
				
				Date StartDate = Utilities.getStartDateByMonth(month-1, year);
				Date EndDate = Utilities.getEndDateByMonth(month-1, year);
				
				String CostType = "Gross Value";
				double CostAmount = rs.getDouble("gross_value");
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cases, gross_value, cost_type, cost_amount) values ("+year+","+month+","+SND_ID+",'"+rs.getString("snd_name")+"',"+rs.getInt("region_id")+",'"+rs.getString("region_name")+"', "+CustomerID+", '"+rs.getString("customer_name")+"', "+rs.getInt("cases")+", "+rs.getDouble("gross_value")+",'"+CostType+"',"+CostAmount+")");
				
				CostType = "Upfront Discount";
				CostAmount = rs.getDouble("upfront_discount")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cases, gross_value, cost_type, cost_amount) values ("+year+","+month+","+SND_ID+",'"+rs.getString("snd_name")+"',"+rs.getInt("region_id")+",'"+rs.getString("region_name")+"', "+CustomerID+", '"+rs.getString("customer_name")+"', "+rs.getInt("cases")+", "+rs.getDouble("gross_value")+",'"+CostType+"',"+CostAmount+")");

				CostType = "Free Stock";
				CostAmount = rs.getDouble("free_stock")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cases, gross_value, cost_type, cost_amount) values ("+year+","+month+","+SND_ID+",'"+rs.getString("snd_name")+"',"+rs.getInt("region_id")+",'"+rs.getString("region_name")+"', "+CustomerID+", '"+rs.getString("customer_name")+"', "+rs.getInt("cases")+", "+rs.getDouble("gross_value")+",'"+CostType+"',"+CostAmount+")");
				
				CostType = "Freight";
				CostAmount = rs.getDouble("freight")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cases, gross_value, cost_type, cost_amount) values ("+year+","+month+","+SND_ID+",'"+rs.getString("snd_name")+"',"+rs.getInt("region_id")+",'"+rs.getString("region_name")+"', "+CustomerID+", '"+rs.getString("customer_name")+"', "+rs.getInt("cases")+", "+rs.getDouble("gross_value")+",'"+CostType+"',"+CostAmount+")");
				
				CostType = "Unloading";
				CostAmount = rs.getDouble("unloading")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cases, gross_value, cost_type, cost_amount) values ("+year+","+month+","+SND_ID+",'"+rs.getString("snd_name")+"',"+rs.getInt("region_id")+",'"+rs.getString("region_name")+"', "+CustomerID+", '"+rs.getString("customer_name")+"', "+rs.getInt("cases")+", "+rs.getDouble("gross_value")+",'"+CostType+"',"+CostAmount+")");

				CostType = "Secondary Per Case Discount";
				CostAmount = rs.getDouble("secondary_per_case_discount")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cases, gross_value, cost_type, cost_amount) values ("+year+","+month+","+SND_ID+",'"+rs.getString("snd_name")+"',"+rs.getInt("region_id")+",'"+rs.getString("region_name")+"', "+CustomerID+", '"+rs.getString("customer_name")+"', "+rs.getInt("cases")+", "+rs.getDouble("gross_value")+",'"+CostType+"',"+CostAmount+")");
				
				CostType = "Secondary Fixed Discount";
				CostAmount = rs.getDouble("secondary_fixed_discount")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cases, gross_value, cost_type, cost_amount) values ("+year+","+month+","+SND_ID+",'"+rs.getString("snd_name")+"',"+rs.getInt("region_id")+",'"+rs.getString("region_name")+"', "+CustomerID+", '"+rs.getString("customer_name")+"', "+rs.getInt("cases")+", "+rs.getDouble("gross_value")+",'"+CostType+"',"+CostAmount+")");
				
				CostType = "Primary Target Incentive";
				CostAmount = rs.getDouble("primary_target_incentive")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cases, gross_value, cost_type, cost_amount) values ("+year+","+month+","+SND_ID+",'"+rs.getString("snd_name")+"',"+rs.getInt("region_id")+",'"+rs.getString("region_name")+"', "+CustomerID+", '"+rs.getString("customer_name")+"', "+rs.getInt("cases")+", "+rs.getDouble("gross_value")+",'"+CostType+"',"+CostAmount+")");
				
				CostType = "Lifting Incentive Incentive";
				CostAmount = rs.getDouble("lifting_incentive")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cases, gross_value, cost_type, cost_amount) values ("+year+","+month+","+SND_ID+",'"+rs.getString("snd_name")+"',"+rs.getInt("region_id")+",'"+rs.getString("region_name")+"', "+CustomerID+", '"+rs.getString("customer_name")+"', "+rs.getInt("cases")+", "+rs.getDouble("gross_value")+",'"+CostType+"',"+CostAmount+")");
				
				CostType = "Primary Sales Incentive";
				CostAmount = rs.getDouble("distributor_sales_incentive")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cases, gross_value, cost_type, cost_amount) values ("+year+","+month+","+SND_ID+",'"+rs.getString("snd_name")+"',"+rs.getInt("region_id")+",'"+rs.getString("region_name")+"', "+CustomerID+", '"+rs.getString("customer_name")+"', "+rs.getInt("cases")+", "+rs.getDouble("gross_value")+",'"+CostType+"',"+CostAmount+")");

				CostType = "Party Order";
				CostAmount = rs.getDouble("party_order")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cases, gross_value, cost_type, cost_amount) values ("+year+","+month+","+SND_ID+",'"+rs.getString("snd_name")+"',"+rs.getInt("region_id")+",'"+rs.getString("region_name")+"', "+CustomerID+", '"+rs.getString("customer_name")+"', "+rs.getInt("cases")+", "+rs.getDouble("gross_value")+",'"+CostType+"',"+CostAmount+")");

				CostType = "Loader Driver Incentive";
				CostAmount = rs.getDouble("loader_driver_incentive")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cases, gross_value, cost_type, cost_amount) values ("+year+","+month+","+SND_ID+",'"+rs.getString("snd_name")+"',"+rs.getInt("region_id")+",'"+rs.getString("region_name")+"', "+CustomerID+", '"+rs.getString("customer_name")+"', "+rs.getInt("cases")+", "+rs.getDouble("gross_value")+",'"+CostType+"',"+CostAmount+")");

				
			}
			
			
			ds.commit();
			
		}catch(Exception e){
			ds.rollback();
			e.printStackTrace();
		}finally{
			ds.dropConnection();
		}
	}
	*/
	public static void processStep7() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		Datasource ds = new Datasource();
		
		try{
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			Statement s4 = ds.createStatement();
			
			s3.executeUpdate("delete from "+ds.logDatabaseName()+".bi_average_price_monthly_list");
			s3.executeUpdate("delete from "+ds.logDatabaseName()+".bi_average_price_monthly_list_master");
			
			ResultSet rs = s.executeQuery("select bapm.*, (select display_name from users where id = cd.snd_id) cd_snd_name, cd.snd_id cd_snd_id, cd.region_id cd_region_id, (select concat(region_short_name,' ',region_name) from common_regions where region_id = cd.region_id) cd_region_name, concat(cd.distributor_id,'-',cd.name) cd_customer_name from "+ds.logDatabaseName()+".bi_average_price_monthly bapm join common_distributors cd on bapm.customer_id = cd.distributor_id where customer_id != 200769 order by year, month, customer_id");
			while(rs.next()){
				
				
				
				
				
				
				
				int year = rs.getInt("year");
				int month = rs.getInt("month");
				long SND_ID = rs.getLong("cd_snd_id");
				long CustomerID = rs.getLong("customer_id");
				
				Date StartDate = Utilities.getStartDateByMonth(month-1, year);
				Date EndDate = Utilities.getEndDateByMonth(month-1, year);
				
				int PackageID = rs.getInt("package_id");
				int TypeID = rs.getInt("product_type_id");
				
				
				String PackageLabel = "";
				ResultSet rs2 = s2.executeQuery("select label from inventory_packages where id = "+PackageID);
				if (rs2.first()){
					PackageLabel = rs2.getString(1);
				}
				
				String TypeLabel = "";
				rs2 = s2.executeQuery("select label from inventory_products_types where id = "+TypeID);
				if (rs2.first()){
					TypeLabel = rs2.getString(1);
				}
				
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list_master (package_id, product_type_id, year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cases) values ("+PackageID+","+TypeID+","+year+","+month+","+SND_ID+",'"+rs.getString("cd_snd_name")+"',"+rs.getInt("cd_region_id")+",'"+rs.getString("cd_region_name")+"', "+CustomerID+", '"+rs.getString("cd_customer_name")+"', "+rs.getInt("cases")+")");
				
				String CostType = "Gross Value";
				double CostAmount = rs.getDouble("gross_value");
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (package_id, product_type_id, year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cost_type, cost_amount) values ("+PackageID+","+TypeID+","+year+","+month+","+SND_ID+",'"+rs.getString("cd_snd_name")+"',"+rs.getInt("cd_region_id")+",'"+rs.getString("cd_region_name")+"', "+CustomerID+", '"+rs.getString("cd_customer_name")+"', '"+CostType+"',"+CostAmount+")");
				
				CostType = "Upfront Discount";
				CostAmount = rs.getDouble("upfront_discount")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (package_id, product_type_id, year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cost_type, cost_amount) values ("+PackageID+","+TypeID+","+year+","+month+","+SND_ID+",'"+rs.getString("cd_snd_name")+"',"+rs.getInt("cd_region_id")+",'"+rs.getString("cd_region_name")+"', "+CustomerID+", '"+rs.getString("cd_customer_name")+"', '"+CostType+"',"+CostAmount+")");

				CostType = "Free Stock";
				CostAmount = rs.getDouble("free_stock")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (package_id, product_type_id, year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cost_type, cost_amount) values ("+PackageID+","+TypeID+","+year+","+month+","+SND_ID+",'"+rs.getString("cd_snd_name")+"',"+rs.getInt("cd_region_id")+",'"+rs.getString("cd_region_name")+"', "+CustomerID+", '"+rs.getString("cd_customer_name")+"', '"+CostType+"',"+CostAmount+")");
				
				CostType = "Freight";
				CostAmount = rs.getDouble("freight")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (package_id, product_type_id, year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cost_type, cost_amount) values ("+PackageID+","+TypeID+","+year+","+month+","+SND_ID+",'"+rs.getString("cd_snd_name")+"',"+rs.getInt("cd_region_id")+",'"+rs.getString("cd_region_name")+"', "+CustomerID+", '"+rs.getString("cd_customer_name")+"', '"+CostType+"',"+CostAmount+")");
				
				CostType = "Unloading";
				CostAmount = rs.getDouble("unloading")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (package_id, product_type_id, year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cost_type, cost_amount) values ("+PackageID+","+TypeID+","+year+","+month+","+SND_ID+",'"+rs.getString("cd_snd_name")+"',"+rs.getInt("cd_region_id")+",'"+rs.getString("cd_region_name")+"', "+CustomerID+", '"+rs.getString("cd_customer_name")+"', '"+CostType+"',"+CostAmount+")");

				CostType = "Secondary Per Case Discount";
				CostAmount = rs.getDouble("secondary_per_case_discount")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (package_id, product_type_id, year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cost_type, cost_amount) values ("+PackageID+","+TypeID+","+year+","+month+","+SND_ID+",'"+rs.getString("cd_snd_name")+"',"+rs.getInt("cd_region_id")+",'"+rs.getString("cd_region_name")+"', "+CustomerID+", '"+rs.getString("cd_customer_name")+"', '"+CostType+"',"+CostAmount+")");
				
				CostType = "Secondary Fixed Discount";
				CostAmount = rs.getDouble("secondary_fixed_discount")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (package_id, product_type_id, year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cost_type, cost_amount) values ("+PackageID+","+TypeID+","+year+","+month+","+SND_ID+",'"+rs.getString("cd_snd_name")+"',"+rs.getInt("cd_region_id")+",'"+rs.getString("cd_region_name")+"', "+CustomerID+", '"+rs.getString("cd_customer_name")+"', '"+CostType+"',"+CostAmount+")");
				
				CostType = "Primary Target Incentive";
				CostAmount = rs.getDouble("primary_target_incentive")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (package_id, product_type_id, year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cost_type, cost_amount) values ("+PackageID+","+TypeID+","+year+","+month+","+SND_ID+",'"+rs.getString("cd_snd_name")+"',"+rs.getInt("cd_region_id")+",'"+rs.getString("cd_region_name")+"', "+CustomerID+", '"+rs.getString("cd_customer_name")+"', '"+CostType+"',"+CostAmount+")");
				
				CostType = "Lifting Incentive Incentive";
				CostAmount = rs.getDouble("lifting_incentive")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (package_id, product_type_id, year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cost_type, cost_amount) values ("+PackageID+","+TypeID+","+year+","+month+","+SND_ID+",'"+rs.getString("cd_snd_name")+"',"+rs.getInt("cd_region_id")+",'"+rs.getString("cd_region_name")+"', "+CustomerID+", '"+rs.getString("cd_customer_name")+"', '"+CostType+"',"+CostAmount+")");
				
				CostType = "Primary Sales Incentive";
				CostAmount = rs.getDouble("distributor_sales_incentive")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (package_id, product_type_id, year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cost_type, cost_amount) values ("+PackageID+","+TypeID+","+year+","+month+","+SND_ID+",'"+rs.getString("cd_snd_name")+"',"+rs.getInt("cd_region_id")+",'"+rs.getString("cd_region_name")+"', "+CustomerID+", '"+rs.getString("cd_customer_name")+"', '"+CostType+"',"+CostAmount+")");

				CostType = "Party Order";
				CostAmount = rs.getDouble("party_order")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (package_id, product_type_id, year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cost_type, cost_amount) values ("+PackageID+","+TypeID+","+year+","+month+","+SND_ID+",'"+rs.getString("cd_snd_name")+"',"+rs.getInt("cd_region_id")+",'"+rs.getString("cd_region_name")+"', "+CustomerID+", '"+rs.getString("cd_customer_name")+"', '"+CostType+"',"+CostAmount+")");

				CostType = "Loader Driver Incentive";
				CostAmount = rs.getDouble("loader_driver_incentive")*-1;
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly_list (package_id, product_type_id, year, month, snd_id, snd_name, region_id, region_name, customer_id, customer_name, cost_type, cost_amount) values ("+PackageID+","+TypeID+","+year+","+month+","+SND_ID+",'"+rs.getString("cd_snd_name")+"',"+rs.getInt("cd_region_id")+",'"+rs.getString("cd_region_name")+"', "+CustomerID+", '"+rs.getString("cd_customer_name")+"', '"+CostType+"',"+CostAmount+")");
				
				
				
				
				s2.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_monthly_list_master set package_label = '"+PackageLabel+"' where package_id = "+PackageID);
				s2.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_monthly_list set package_label = '"+PackageLabel+"' where package_id = "+PackageID);
				
				s2.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_monthly_list_master set product_type_label = '"+TypeLabel+"' where product_type_id = "+TypeID);
				s2.executeUpdate("update "+ds.logDatabaseName()+".bi_average_price_monthly_list set product_type_label = '"+TypeLabel+"' where product_type_id = "+TypeID);
				
			}
			
			
			
			
			ds.commit();
			
		}catch(Exception e){
			ds.rollback();
			e.printStackTrace();
		}finally{
			ds.dropConnection();
		}
	}

	
	public static void processStep5() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		Datasource ds = new Datasource();
		
		try{
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			
			
			
			s.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_monthly SELECT bapi.package_id, bapi.product_type_id, bapi.year, bapi.month, null, null, null, null, bapi.customer_id, null, sum(quantity), sum(gross_value), sum(upfront_discount), sum(free_stock), sum(freight), sum(unloading), 0, 0, 0, 0, 0, 0, 0 FROM peplogs.bi_average_price_invoice bapi where vbeln in (select invoice_no from pep.inventory_delivery_note) group by bapi.package_id, bapi.product_type_id, bapi.year, bapi.month, bapi.customer_id");
			
			s.executeUpdate("update peplogs.bi_average_price_monthly set upfront_discount = upfront_discount * -1 where upfront_discount != 0");
			s.executeUpdate("update peplogs.bi_average_price_monthly set freight = freight * -1 where freight != 0");
			s.executeUpdate("update peplogs.bi_average_price_monthly set unloading = unloading * -1 where unloading != 0");
			
			ds.commit();
			
		}catch(Exception e){
			ds.rollback();
			e.printStackTrace();
		}finally{
			ds.dropConnection();
		}
		
		
	}
	
}
