package com.pbc.bi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class BiPerCasePrice {
	
	
	public static String StartDateMySQL = "2017-03-01";
	public static String StartDateOracle = "20170301";
	
	public static void main(String[] args) {
		
		
		try{
			
			
			
			processStep1(); // Fetch from SAP, breakup
			processStep3(); // Promotion isolated
			processStep4(); // Promotion
			//processStep5(); // Promotion UTC
			
			/*
			processStep3(); // Promotion isolated
			processStep4(); // Promotion
			processStep5(); // Per Case Sampling
			processStep6(); // Per Case Sampling
			processStep7(); // Convert table
			processStep8();
			*/
		}catch(Exception e){
			e.printStackTrace();
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
			
			Date CurrentDate = Utilities.getDateByDays(1);
			
			
			s.executeUpdate("delete from "+ds.logDatabaseName()+".bi_percase_price_invoice where kurrf_dat between '"+StartDateMySQL+"' and curdate()");
			s.executeUpdate("delete from "+ds.logDatabaseName()+".bi_percase_price_invoice_promotions where kurrf_dat between '"+StartDateMySQL+"' and curdate()");
			
			ResultSet rsSAP = sSAP.executeQuery("select vbrk.vbeln, vbrk.fkart, vbrk.knumv, vbrp.posnr, vbrk.kurrf_dat, vbrp.pstyv, vbrp.matnr, vbrp.fkimg quantity, (vbrp.kzwi1+ /*vbrp.kzwi2 + vbrp.kzwi3 +*/ vbrp.kzwi6) gross_value, vbrp.kzwi4 unloading, vbrp.kzwi5 freight, vbrp.vrkme, vbrk.kunrg from sapsr3.vbrp vbrp join sapsr3.vbrk on vbrk.vbeln = vbrp.vbeln where vbrk.fkart in ('ZDIS', 'ZMRS', 'ZDFR') and vbrk.fksto != 'X' and vbrk.kurrf_dat between '"+StartDateOracle+"' and "+Utilities.getSQLDateOracle(CurrentDate)+" order by vbrk.vbeln");
			while(rsSAP.next()){
				
				
				String posnr = rsSAP.getString("posnr");
				String vrkme = rsSAP.getString("VRKME");
				long CustomerID = Utilities.parseLong(rsSAP.getString("kunrg"));
				
				
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
				ResultSet rs3 = s.executeQuery("select package_id, id, lrb_type_id, category_id from inventory_products where sap_code = "+SAPCode);
				if (rs3.first()){
					PackageID = rs3.getInt(1);
					ProductID = rs3.getInt(2);
					TypeID = rs3.getInt(3);
					CategoryID = rs3.getInt(4);
				}
				
				boolean isBottle = false;
				if (vrkme.equals("EA") || vrkme.equals("BOT")){
					isBottle = true;
				}
				if (PackageID == 13){
					isBottle = false;
				}
				
				
				if (isPromotion){
					
					
					/*
					 * s.executeUpdate("insert into "+ds.logDatabaseName()+".bi_percase_price_invoice (vbeln, fkart, knumv, posnr, kurrf_dat, pstyv, matnr, quantity, gross_value, freight, unloading, is_isolated, upfront_discount, customer_id, package_id, product_id, product_type_id) values ("+rsSAP.getLong(1)+",'"+rsSAP.getString("fkart")+"',"+rsSAP.getLong("knumv")+","+rsSAP.getInt("posnr")+",'"+rsSAP.getString("kurrf_dat")+"','"+rsSAP.getString("pstyv")+"',"+rsSAP.getInt("matnr")+","+rsSAP.getInt("quantity")+","+rsSAP.getDouble("gross_value")+","+rsSAP.getDouble("freight")+","+rsSAP.getDouble("unloading")+",1,"+UpfrontDiscount+","+CustomerID+","+PackageID+","+ProductID+","+TypeID+")");
					 * */
					
					if (isBottle){
						s.executeUpdate("insert into "+ds.logDatabaseName()+".bi_percase_price_invoice_promotions (vbeln, product_id, cases, bottles, gross_value, freight, unloading, posnr, kurrf_dat, customer_id, package_id, product_type_id) values ("+VBELN+","+ProductID+",0,"+quantity+","+rsSAP.getDouble("gross_value")+","+(rsSAP.getDouble("freight")*-1)+","+(rsSAP.getDouble("unloading")*-1)+", "+posnr+", '"+rsSAP.getString("kurrf_dat")+"', "+CustomerID+", "+PackageID+", "+TypeID+")");	
					}else{
						s.executeUpdate("insert into "+ds.logDatabaseName()+".bi_percase_price_invoice_promotions (vbeln, product_id, cases, bottles, gross_value, freight, unloading, posnr, kurrf_dat, customer_id, package_id, product_type_id) values ("+VBELN+","+ProductID+","+quantity+",0,"+rsSAP.getDouble("gross_value")+","+(rsSAP.getDouble("freight")*-1)+","+(rsSAP.getDouble("unloading")*-1)+","+posnr+", '"+rsSAP.getString("kurrf_dat")+"', "+CustomerID+", "+PackageID+", "+TypeID+")");
					}
					
				}else{
					
					if (!isBottle){
							
					
						if (CategoryID == 1 && PackageID != 20){
							
							boolean alreadyExists = false;
							ResultSet rs2 = s.executeQuery("select * from "+ds.logDatabaseName()+".bi_percase_price_invoice where vbeln = "+VBELN+" and package_id = "+PackageID+" and product_type_id = "+TypeID);
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
								s.executeUpdate("update "+ds.logDatabaseName()+".bi_percase_price_invoice set quantity = quantity + "+rsSAP.getInt("quantity")+", gross_value = gross_value + "+rsSAP.getDouble("gross_value")+", freight = freight + "+rsSAP.getDouble("freight")+", unloading = unloading + "+rsSAP.getDouble("unloading")+", upfront_discount = upfront_discount + "+UpfrontDiscount+" where vbeln = "+rsSAP.getLong(1)+" and package_id = "+PackageID+" and product_type_id = "+TypeID);
							}else{
								s.executeUpdate("insert into "+ds.logDatabaseName()+".bi_percase_price_invoice (vbeln, fkart, knumv, posnr, kurrf_dat, pstyv, matnr, quantity, gross_value, freight, unloading, is_isolated, upfront_discount, customer_id, package_id, product_id, product_type_id) values ("+rsSAP.getLong(1)+",'"+rsSAP.getString("fkart")+"',"+rsSAP.getLong("knumv")+","+rsSAP.getInt("posnr")+",'"+rsSAP.getString("kurrf_dat")+"','"+rsSAP.getString("pstyv")+"',"+rsSAP.getInt("matnr")+","+rsSAP.getInt("quantity")+","+rsSAP.getDouble("gross_value")+","+rsSAP.getDouble("freight")+","+rsSAP.getDouble("unloading")+",1,"+UpfrontDiscount+","+CustomerID+","+PackageID+","+ProductID+","+TypeID+")");
							}
							
						}else{
							//s.executeUpdate("insert into "+ds.logDatabaseName()+".bi_average_price_invoice_other_products (vbeln, product_id, cases, bottles) values ("+VBELN+","+ProductID+","+quantity+",0)");
						}
						
					}
				}
				
				ds.commit();
			}
			
			
			
			s.executeUpdate("update "+ds.logDatabaseName()+".bi_percase_price_invoice set with_promotion = 1 where vbeln in (select vbeln from "+ds.logDatabaseName()+".bi_percase_price_invoice_promotions)");
			
			
			ResultSet rs = s2.executeQuery("select vbeln, count(package_id) ct from "+ds.logDatabaseName()+".bi_percase_price_invoice group by vbeln having ct > 1");
			while(rs.next()){
				
				s.executeUpdate("update "+ds.logDatabaseName()+".bi_percase_price_invoice set is_isolated = 0 where vbeln = "+rs.getString(1));
				
			}
			
			
			s.executeUpdate("update "+ds.logDatabaseName()+".bi_percase_price_invoice set month = date_format(kurrf_dat,'%m'), year = date_format(kurrf_dat,'%Y') ");
			
			//s.executeUpdate("update "+ds.logDatabaseName()+".bi_percase_price_invoice set quantity = 0 where fkart = 'ZCLA' "); // Claims

			
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
			
			ResultSet rs3 = s.executeQuery("select * from "+ds.logDatabaseName()+".bi_percase_price_invoice where with_promotion = 1 and is_isolated = 1 and kurrf_dat between '"+StartDateMySQL+"' and curdate()");
			while (rs3.next()){
				
				ResultSet rs4 = s2.executeQuery("select sum(gross_value) from "+ds.logDatabaseName()+".bi_percase_price_invoice_promotions where vbeln = "+rs3.getLong("vbeln"));
				if (rs4.first()){
					
					s3.executeUpdate("update "+ds.logDatabaseName()+".bi_percase_price_invoice set free_stock = "+rs4.getDouble(1)+" where vbeln = "+rs3.getLong("vbeln"));
					
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
			
			
			ResultSet rs3 = s.executeQuery("select * from "+ds.logDatabaseName()+".bi_percase_price_invoice bapi join common_distributors cd on bapi.customer_id = cd.distributor_id where with_promotion = 1 and is_isolated = 0 and kurrf_dat between '"+StartDateMySQL+"' and curdate()");
			while (rs3.next()){
				
				long InoviceNumber = rs3.getLong("vbeln");
				long CustomerID = rs3.getLong("customer_id");
				int RegionID = rs3.getInt("region_id");
				Date InvoiceDate = rs3.getDate("kurrf_dat");
				int PackageID = rs3.getInt("package_id");
				int TypeID = rs3.getInt("product_type_id");
				
				//if (InoviceNumber == 9000394844l){
					
					//System.out.println("SELECT (SELECT biapip.gross_value/((biapip.cases*iipv.unit_per_sku)+biapip.bottles) rate FROM peplogs.bi_percase_price_invoice_promotions biapip join pep.inventory_products_view iipv on biapip.product_id = iipv.product_id where vbeln = glip.invoice_no and iipv.package_id = ipv.package_id and iipv.lrb_type_id = ipv.lrb_type_id limit 1)*glpp.total_units promotion_amount, (SELECT isprp.package_id FROM pep.inventory_sales_promotions_request ispr join inventory_sales_promotions_request_products isprp on ispr.id = isprp.id where ispr.request_id = glpp.request_id and isprp.type_id = 1) package_id, (SELECT (select ip.lrb_type_id from inventory_products ip where ip.brand_id = isprpb.brand_id limit 1) FROM pep.inventory_sales_promotions_request ispr join inventory_sales_promotions_request_products isprp on ispr.id = isprp.id  join inventory_sales_promotions_request_products_brands isprpb on isprp.id = isprpb.id and isprp.package_id = isprpb.package_id and isprp.type_id = isprpb.type_id where ispr.request_id = glpp.request_id and isprp.type_id = 1 limit 1) product_type_id FROM pep.gl_order_posting glop join gl_invoice_posting glip on glop.order_no = glip.order_no join gl_order_posting_promotions glpp on glpp.id = glop.id join inventory_products_view ipv on glpp.product_id = ipv.product_id where glip.invoice_no = "+InoviceNumber+" having package_id = "+PackageID+" and product_type_id = "+TypeID);
					ResultSet rs4 = s2.executeQuery("SELECT (SELECT biapip.gross_value/((biapip.cases*iipv.unit_per_sku)+biapip.bottles) rate FROM peplogs.bi_percase_price_invoice_promotions biapip join pep.inventory_products_view iipv on biapip.product_id = iipv.product_id where vbeln = glip.invoice_no and iipv.package_id = ipv.package_id and iipv.lrb_type_id = ipv.lrb_type_id limit 1)*glpp.total_units promotion_amount, (SELECT isprp.package_id FROM pep.inventory_sales_promotions_request ispr join inventory_sales_promotions_request_products isprp on ispr.id = isprp.id where ispr.request_id = glpp.request_id and isprp.type_id = 1) package_id, (SELECT (select ip.lrb_type_id from inventory_products ip where ip.brand_id = isprpb.brand_id limit 1) FROM pep.inventory_sales_promotions_request ispr join inventory_sales_promotions_request_products isprp on ispr.id = isprp.id  join inventory_sales_promotions_request_products_brands isprpb on isprp.id = isprpb.id and isprp.package_id = isprpb.package_id and isprp.type_id = isprpb.type_id where ispr.request_id = glpp.request_id and isprp.type_id = 1 limit 1) product_type_id FROM pep.gl_order_posting glop join gl_invoice_posting glip on glop.order_no = glip.order_no join gl_order_posting_promotions glpp on glpp.id = glop.id join inventory_products_view ipv on glpp.product_id = ipv.product_id where glip.invoice_no = "+InoviceNumber+" having package_id = "+PackageID+" and product_type_id = "+TypeID);
					while(rs4.next()){
						double amount = rs4.getDouble(1);
						s5.executeUpdate("update "+ds.logDatabaseName()+".bi_percase_price_invoice set free_stock = ifnull(free_stock,0) + "+amount+" where vbeln = "+InoviceNumber+" and package_id = "+PackageID+" and product_type_id = "+TypeID);
						//System.out.println("update "+ds.logDatabaseName()+".bi_percase_price_invoice set free_stock = ifnull(free_stock,0) + "+amount+" where vbeln = "+InoviceNumber+" and package_id = "+PackageID+" and product_type_id = "+TypeID);
					}
				//}
				
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
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			Statement s4 = ds.createStatement();
			Statement s5 = ds.createStatement();
			
			
			ResultSet rs3 = s.executeQuery("SELECT vbeln, kurrf_dat, customer_id, package_id, product_type_id, sum(gross_value) FROM peplogs.bi_percase_price_invoice_promotions bppip where vbeln not in (select vbeln from peplogs.bi_percase_price_invoice) group by vbeln, kurrf_dat, customer_id, package_id, product_type_id");
			while (rs3.next()){
				
				
				s2.executeUpdate("insert into "+ds.logDatabaseName()+".bi_percase_price_invoice (vbeln, fkart, knumv, posnr, kurrf_dat, pstyv, matnr, quantity, gross_value, freight, unloading, is_isolated, upfront_discount, customer_id, package_id, product_id, product_type_id, free_stock) values ("+rs3.getString(1)+",'',0,0,"+Utilities.getSQLDate(rs3.getDate(2))+",'',0,0,0,0,0,0,0,"+rs3.getString(3)+","+rs3.getString(4)+",0,"+rs3.getString(5)+","+rs3.getDouble(6)+")");
				
			}
			
			ds.commit();
			
		}catch(Exception e){
			ds.rollback();
			e.printStackTrace();
		}finally{
			ds.dropConnection();
		}
	}
	
	
}
