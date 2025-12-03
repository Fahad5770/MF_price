package com.pbc.empty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class SyncEmptySAP {

	public static void main(String[] args) {
		
		try{
			NewEmptyIssuanceSync();
			OtherEmptyIssuanceSync();
			OtherEmptyLossesSync();
			EmptyProductionShippingLossesSync();
			EmptyProductionLossSync();
			EmptyStoreReturnOtherBrandsSync();
		}catch(Exception e){
			
			e.printStackTrace();
		}
	}
	
	public static void NewEmptyIssuanceSync() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		Datasource dsSAP = new Datasource();
		Datasource ds = new Datasource();
		
		try{
			dsSAP.createConnectionToSAPDB();
			ds.createConnection();
			
			Statement sSAP = dsSAP.createStatement();
			Statement s2SAP = dsSAP.createStatement();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			
			s.executeUpdate("delete from "+ds.logDatabaseName()+".bi_empty_new_issuance");
			
			ResultSet rs = s2SAP.executeQuery("select mkpf.mblnr DocumentID, mkpf.budat PostingDate, mseg.matnr ProductSAPCode, mseg.erfme UoM, mseg.erfmg Quantity, mseg.lgort from sapsr3.mkpf mkpf join sapsr3.mseg mseg on mkpf.mblnr = mseg.mblnr where mkpf.budat between '20150101' and '20161231' and mseg.lgort in ('363','387') and mkpf.usnam = 'STINCH' and mseg.bwart in ('311','312')");
			while(rs.next()){
				//System.out.println("safasdf");
				
				String QueryPrt="";
				int UnitPerSKU=0;
				int TotalUnits=0;
				int IsOld=0;
				//System.out.println("insert into "+ds.logDatabaseName()+".bi_empty_new_issuance(docuement_id,posting_date,product_id,product_sap_code,raw_cases,total_units) values("+rs.getLong("DocumentID")+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("PostingDate")))+",select id from inventory_products where sap_code="+rs.getInt("ProductSAPCode")+","+rs.getInt("ProductSAPCode")+","+rs.getInt("Quantity")+",0)");
				
				if(rs.getString("UoM").equals("KI")){					
					
					QueryPrt ="raw_cases";
					ResultSet rs1 = s2.executeQuery("select unit_per_sku from inventory_products where id= (select id from inventory_products where sap_code="+rs.getInt("ProductSAPCode")+")");
					if(rs1.first()){
						UnitPerSKU = rs1.getInt("unit_per_sku");
						TotalUnits = UnitPerSKU*rs.getInt("Quantity");
					}
						
				}else{
				//if(rs.getString("UoM").equals("BOT")){
					QueryPrt ="units";
					TotalUnits = rs.getInt("Quantity");
					
				}
				
				if(rs.getInt("lgort")==387){
					IsOld=1;
				}
				
				//System.out.println("insert into "+ds.logDatabaseName()+".bi_empty_new_issuance(document_id,posting_date,product_id,product_sap_code,"+QueryPrt+",total_units,storage_location,is_old) values("+rs.getLong("DocumentID")+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("PostingDate")))+",(select id from inventory_products where sap_code="+rs.getInt("ProductSAPCode")+"),"+rs.getInt("ProductSAPCode")+","+rs.getInt("Quantity")+","+TotalUnits+","+rs.getInt("lgort")+","+IsOld+")");
				s.executeUpdate("insert into "+ds.logDatabaseName()+".bi_empty_new_issuance(document_id,posting_date,product_id,product_sap_code,"+QueryPrt+",total_units,storage_location,is_old) values("+rs.getLong("DocumentID")+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("PostingDate")))+",(select id from inventory_products where sap_code="+rs.getInt("ProductSAPCode")+"),"+rs.getInt("ProductSAPCode")+","+rs.getInt("Quantity")+","+TotalUnits+","+rs.getInt("lgort")+","+IsOld+")");
				
			}
			
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			ds.dropConnection();
			dsSAP.dropConnection();
		}
	}
	
public static void OtherEmptyIssuanceSync() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		Datasource dsSAP = new Datasource();
		Datasource ds = new Datasource();
		
		try{
			
			dsSAP.createConnectionToSAPDB();
			ds.createConnection();
			
			Statement sSAP = dsSAP.createStatement();
			Statement s2SAP = dsSAP.createStatement();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			
			s.executeUpdate("delete from "+ds.logDatabaseName()+".bi_empty_other_issuance");
			
			ResultSet rs = s2SAP.executeQuery("select benid, botcp, reason, vbeln, audat, vehno, movmt, status, quantity, botyp from sapsr3.zsd_credit_bal where regio = 'RM5' and botcp in ('R01','R02','R12') and audat between '20150101' and '20161231'");
			while(rs.next()){
				//System.out.println("safasdf");
				
				String botyp = rs.getString("botyp");
				
				String QueryPrt="";
				int UnitPerSKU=0;
				
				int IsOld=0;
				int PackageID=0;
				long[] RawCasesUnits;
				int RawCases=0;
				int Units=0;
				int TotalUnits=0;
				
				
				if(rs.getString("botcp").equals("R01")){
					PackageID = 11;
				}
				else if(rs.getString("botcp").equals("R02")){
					PackageID = 1;
				}
				else if(rs.getString("botcp").equals("R12")){
					PackageID = 12;
				}
				
					ResultSet rs1 = s2.executeQuery("select unit_per_case from inventory_packages where id="+PackageID);
					if(rs1.first()){
						UnitPerSKU = rs1.getInt("unit_per_case");
					}
					
					TotalUnits = rs.getInt("quantity");
					RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnits, UnitPerSKU);
					
					//System.out.println("insert into "+ds.logDatabaseName()+".bi_empty_other_issuance( customer_id ,package_id ,reason ,document_id ,document_date ,vehicle_no,movmt ,status ,cases ,units ,total_units ) values("+rs.getLong("benid")+","+PackageID+",'"+rs.getString("reason")+"',"+rs.getLong("vbeln")+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("audat")))+",'"+rs.getString("vehno")+"','"+rs.getString("movmt")+"','"+rs.getString("status")+"',"+RawCasesUnits[0]+","+RawCasesUnits[1]+","+TotalUnits+")");
					
					
					if (rs.getString("movmt").equals("Empty Out")){
						TotalUnits = TotalUnits * -1;
						RawCasesUnits[0] = RawCasesUnits[0] * -1;
						RawCasesUnits[1] = RawCasesUnits[1] * -1;
					}
					
					
					s.executeUpdate("insert into "+ds.logDatabaseName()+".bi_empty_other_issuance( customer_id ,package_id ,reason ,document_id ,document_date ,vehicle_no,movmt ,status ,cases ,units ,total_units, botyp ) values("+rs.getLong("benid")+","+PackageID+",'"+Utilities.filterString(rs.getString("reason"), 1, 500)+"',"+rs.getLong("vbeln")+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("audat")))+",'"+Utilities.filterString(rs.getString("vehno"),1,200)+"','"+Utilities.filterString(rs.getString("movmt"),1,200)+"','"+Utilities.filterString(rs.getString("status"),1,200)+"',"+RawCasesUnits[0]+","+RawCasesUnits[1]+","+TotalUnits+",'"+botyp+"')");
						
				}
				
				
				
			
			
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			ds.dropConnection();
			dsSAP.dropConnection();
		}
	}

public static void OtherEmptyLossesSync() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
	
	Datasource dsSAP = new Datasource();
	Datasource ds = new Datasource();
	
	try{
		
		dsSAP.createConnectionToSAPDB();
		ds.createConnection();
		
		Statement sSAP = dsSAP.createStatement();
		Statement s2SAP = dsSAP.createStatement();
		Statement s = ds.createStatement();
		Statement s2 = ds.createStatement();
		
		
		System.out.println("Hello started");
		
		s.executeUpdate("delete from "+ds.logDatabaseName()+".bi_empty_losses");
		
		ResultSet rs = s2SAP.executeQuery("SELECT mkpf.mblnr, mkpf.vgart, mvke.mvgr4, mseg.grund, mkpf.budat, mseg.bwart, mseg.erfme, mseg.erfmg erfmg, mvke.mvgr1 FROM sapsr3.mkpf mkpf JOIN sapsr3.mseg mseg ON (mkpf.mblnr = mseg.mblnr AND mkpf.mjahr = mseg.mjahr)"+
										"JOIN sapsr3.mara ON ( mseg.matnr = mara.matnr )"+
										"JOIN sapsr3.mvke mvke ON ( mara.matnr = mvke.matnr AND mvke.vkorg = '1000' AND mvke.vtweg = '20' )"+
										"WHERE mkpf.budat between '20150101' and '20161231'  AND  ( ( /*mvke.mvgr4 = botcp AND*/ mseg.grund IN ('0003','0007','0005') ) OR ( /*mvke.mvgr1 in pack_rng AND*/ mseg.grund IN ('0013', '0003','0004','0006') ) )"+
										"AND   mara.matkl IN ('001','002','003','004','005','006','007','008','009','010','012','013','054','014','011')"+
										"AND   mseg.bwart IN ('551','552') AND mseg.lgort IN (309,310,313,314,311,312,306,316,307,356,303,357,304,375,366,376,367,363,315,364,370,372,371,373,380,381,377,378,383,384)");
		while(rs.next()){
			//System.out.println("safasdf");
			
			String QueryPrt="";
			int UnitPerSKU=0;
			int TotalUnits=0;
			int IsOld=0;
			int PackageID =0;
			
			int Qty = rs.getInt("erfmg");
			
			//System.out.println("insert into "+ds.logDatabaseName()+".bi_empty_new_issuance(docuement_id,posting_date,product_id,product_sap_code,raw_cases,total_units) values("+rs.getLong("DocumentID")+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("PostingDate")))+",select id from inventory_products where sap_code="+rs.getInt("ProductSAPCode")+","+rs.getInt("ProductSAPCode")+","+rs.getInt("Quantity")+",0)");
			
			if(rs.getString("mvgr4").equals("R01") || rs.getString("mvgr1").equals("P01") || rs.getString("mvgr1").equals("P12")) {
				PackageID = 11;
			}
			else if(rs.getString("mvgr4").equals("R02") || rs.getString("mvgr1").equals("P02")){
				PackageID = 1;
			}
			else if(rs.getString("mvgr4").equals("R12")){
				PackageID = 12;
			}
			
			
			
			if(rs.getString("erfme").equals("KI")){					
				
				QueryPrt ="raw_cases";
				ResultSet rs1 = s2.executeQuery("select unit_per_case from inventory_packages where id="+PackageID);
				if(rs1.first()){
					UnitPerSKU = rs1.getInt("unit_per_case");
					TotalUnits = UnitPerSKU*Qty;
				}
					
			}
			if(rs.getString("erfme").equals("BOT")){
				QueryPrt ="units";
				TotalUnits = Qty;
				
			}
			if(rs.getString("erfme").equals("EA")){
				QueryPrt ="units";
				TotalUnits = Qty;
				
			}
			
			if (rs.getInt("bwart") == 552){
				TotalUnits = TotalUnits * -1;
				Qty = Qty * -1;
			}
			
			
			//System.out.println("insert into "+ds.logDatabaseName()+".bi_empty_losses(document_id,document_type,material_group,reason,posting_date,"+QueryPrt+",total_units,movement_type) values("+rs.getLong("mblnr")+",'"+Utilities.filterString(rs.getString("vgart"),101,300)+"','"+Utilities.filterString(rs.getString("mvgr4"),101,300)+"',"+rs.getInt("grund")+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("budat")))+","+rs.getInt("erfmg")+","+TotalUnits+","+rs.getInt("bwart")+")");
			
			s.executeUpdate("insert into "+ds.logDatabaseName()+".bi_empty_losses(document_id,document_type,material_group,reason,posting_date,"+QueryPrt+",total_units,movement_type, material_group_pack) values("+rs.getLong("mblnr")+",'"+Utilities.filterString(rs.getString("vgart"),101,300)+"','"+Utilities.filterString(rs.getString("mvgr4"),101,300)+"',"+rs.getInt("grund")+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("budat")))+","+Qty+","+TotalUnits+","+rs.getInt("bwart")+",'"+rs.getString("mvgr1")+"')");
			
		}
		
		
		System.out.println("Hello Insert and Ended");
		
	}catch(Exception e){
		
		e.printStackTrace();
	}finally{
		ds.dropConnection();
		dsSAP.dropConnection();
	}
}


public static void EmptyProductionShippingLossesSync() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
	
	Datasource dsSAP = new Datasource();
	Datasource ds = new Datasource();
	
	try{
		dsSAP.createConnectionToSAPDB();
		ds.createConnection();
		
		Statement sSAP = dsSAP.createStatement();
		Statement s2SAP = dsSAP.createStatement();
		Statement s = ds.createStatement();
		Statement s2 = ds.createStatement();
		
		System.out.println("Started");
		s.executeUpdate("delete from "+ds.logDatabaseName()+".bi_empty_production_shipping_loss");
		
		ResultSet rs = s2SAP.executeQuery("select mkpf.mblnr DocumentID, mkpf.budat PostingDate, mseg.matnr ProductSAPCode, mseg.erfme UoM, mseg.erfmg Quantity, mseg.lgort from sapsr3.mkpf mkpf join sapsr3.mseg mseg on mkpf.mblnr = mseg.mblnr where mkpf.budat between '20150101' and '20161231' and mseg.lgort in ('369') and mseg.bwart in ('262') and mseg.matnr in ('000000000000005001','000000000000005002','000000000000005003','000000000000005004','000000000000005005','000000000000005006','000000000000005007','000000000000005031','000000000000005041','000000000000005008','000000000000005009','000000000000005010','000000000000005011')");
		while(rs.next()){
			//System.out.println("safasdf");
			
			String QueryPrt="";
			int UnitPerSKU=0;
			int TotalUnits=0;
			int IsOld=0;
			//System.out.println("insert into "+ds.logDatabaseName()+".bi_empty_new_issuance(docuement_id,posting_date,product_id,product_sap_code,raw_cases,total_units) values("+rs.getLong("DocumentID")+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("PostingDate")))+",select id from inventory_products where sap_code="+rs.getInt("ProductSAPCode")+","+rs.getInt("ProductSAPCode")+","+rs.getInt("Quantity")+",0)");
			
			if(rs.getString("UoM").equals("KI")){					
				
				QueryPrt ="raw_cases";
				ResultSet rs1 = s2.executeQuery("select unit_per_sku from inventory_products where id= (select id from inventory_products where sap_code="+rs.getInt("ProductSAPCode")+")");
				if(rs1.first()){
					UnitPerSKU = rs1.getInt("unit_per_sku");
					TotalUnits = UnitPerSKU*rs.getInt("Quantity");
				}
					
			}
			if(rs.getString("UoM").equals("BOT")){
				QueryPrt ="units";
				TotalUnits = rs.getInt("Quantity");
				
			}
			s.executeUpdate("insert into "+ds.logDatabaseName()+".bi_empty_production_shipping_loss(document_id,posting_date,product_id,product_sap_code,"+QueryPrt+",total_units,storage_location) values("+rs.getLong("DocumentID")+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("PostingDate")))+",(select id from inventory_products where sap_code="+rs.getInt("ProductSAPCode")+"),"+rs.getInt("ProductSAPCode")+","+rs.getInt("Quantity")+","+TotalUnits+","+rs.getInt("lgort")+")");
			
		}
		
		System.out.println("Ended");
		
	}catch(Exception e){
		
		e.printStackTrace();
	}finally{
		ds.dropConnection();
		dsSAP.dropConnection();
	}
}

public static void EmptyProductionLossSync() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
	
	Datasource dsSAP = new Datasource();
	Datasource ds = new Datasource();
	
	try{
		dsSAP.createConnectionToSAPDB();
		ds.createConnection();
		
		Statement sSAP = dsSAP.createStatement();
		Statement s2SAP = dsSAP.createStatement();
		Statement s = ds.createStatement();
		Statement s2 = ds.createStatement();
		
		System.out.println("Started");
		s.executeUpdate("delete from "+ds.logDatabaseName()+".bi_empty_production_loss");
		
		ResultSet rs = s2SAP.executeQuery("SELECT afru.budat DocumentDate, afru.vornr LossType, afru.grund reason, afru.xmnga Qty, mvke.mvgr1 Pack, afru.stokz Flag  FROM sapsr3.afru afru JOIN sapsr3.afpo afpo ON ( afru.aufnr = afpo.aufnr )"+
										  "JOIN sapsr3.mara ON ( afpo.matnr = mara.matnr )"+
										  "JOIN sapsr3.mvke ON ( mara.matnr = mvke.matnr AND mvke.vkorg = '1000' AND mvke.vtweg = '20' AND mvke.mvgr1 in ('P01','P02') )"+
										      "WHERE afru.budat between '20150101' and '20161231'"+
										        "AND afru.werks = '3000'"+
										        "AND afru.xmnga <> '000.0'"+
										        "AND afru.stzhl = '00000000'"+
										        "AND afpo.dauat = 'ZPBF'");
		while(rs.next()){
			
			
			s.executeUpdate("insert into "+ds.logDatabaseName()+".bi_empty_production_loss(document_date,loss_type,reason,quantity,pack,flag) values("+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("DocumentDate")))+",'"+rs.getString("LossType")+"','"+rs.getString("reason")+"',"+rs.getInt("qty")+",'"+rs.getString("Pack")+"','"+rs.getString("Flag")+"')");
			
		}
		
		System.out.println("Ended");
		
	}catch(Exception e){
		
		e.printStackTrace();
	}finally{
		ds.dropConnection();
		dsSAP.dropConnection();
	}
}
	

public static void EmptyStoreReturnOtherBrandsSync() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
	
	Datasource dsSAP = new Datasource();
	Datasource ds = new Datasource();
	
	try{
		dsSAP.createConnectionToSAPDB();
		ds.createConnection();
		
		Statement sSAP = dsSAP.createStatement();
		Statement s2SAP = dsSAP.createStatement();
		Statement s = ds.createStatement();
		Statement s2 = ds.createStatement();
		
		System.out.println("Started");
		s.executeUpdate("delete from "+ds.logDatabaseName()+".empty_store_return_other_brands");
		
		//for 250
		
		ResultSet rs = s2SAP.executeQuery("select mkpf.mblnr DocumentID, mkpf.budat PostingDate, mseg.matnr ProductSAPCode, mseg.erfme UoM, mseg.erfmg Quantity, mseg.lgort, mseg.bwart from sapsr3.mkpf mkpf join sapsr3.mseg mseg on mkpf.mblnr = mseg.mblnr where mkpf.budat between '20150101' and '20161231' and mseg.lgort in ('302','386') and mseg.bwart in ('313','314') and mseg.matnr in ('000000000000005017','000000000000005018','000000000000005019','000000000000005020','000000000000005022','000000000000005023')");
		while(rs.next()){
			//System.out.println("safasdf");
			
			String QueryPrt="";
			int UnitPerSKU=0;
			int TotalUnits=0;
			int IsOld=0;
			int Quantity=0;
			
			
			Quantity = rs.getInt("Quantity");
			
			if(rs.getString("UoM").equals("KI")){					
				
				QueryPrt ="raw_cases";
				
					
					TotalUnits = 24*Quantity;
				
					
			}
			if(rs.getString("UoM").equals("BOT")){
				QueryPrt ="units";
				TotalUnits = Quantity;
				
			}
			
			if(rs.getInt("bwart") == 314 ){
				Quantity = Quantity*-1;
				TotalUnits = TotalUnits*-1;
			}
			
			s.executeUpdate("insert into "+ds.logDatabaseName()+".empty_store_return_other_brands(document_id,posting_date,product_sap_code,"+QueryPrt+",total_units,storage_location) values("+rs.getLong("DocumentID")+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("PostingDate")))+","+rs.getInt("ProductSAPCode")+","+Quantity+","+TotalUnits+","+rs.getInt("lgort")+")");
			
		}
		
		//for 1000
		ResultSet rs2 = s2SAP.executeQuery("select mkpf.mblnr DocumentID, mkpf.budat PostingDate, mseg.matnr ProductSAPCode, mseg.erfme UoM, mseg.erfmg Quantity, mseg.lgort, mseg.bwart from sapsr3.mkpf mkpf join sapsr3.mseg mseg on mkpf.mblnr = mseg.mblnr where mkpf.budat between '20150101' and '20161231' and mseg.lgort in ('302','386') and mseg.bwart in ('313','314') and mseg.matnr in ('000000000000005024','000000000000005026')");
		while(rs2.next()){
			//System.out.println("safasdf");
			
			String QueryPrt="";
			int UnitPerSKU=0;
			int TotalUnits=0;
			int IsOld=0;
			int Quantity=0;
			
			
			Quantity = rs2.getInt("Quantity");
			
			if(rs2.getString("UoM").equals("KI")){					
				
				QueryPrt ="raw_cases";
				
					
					TotalUnits = 6*Quantity;
				
					
			}
			if(rs2.getString("UoM").equals("BOT")){
				QueryPrt ="units";
				TotalUnits = Quantity;
				
			}
			
			if(rs2.getInt("bwart") == 314 ){
				Quantity = Quantity*-1;
				TotalUnits = TotalUnits*-1;
			}
			
			s.executeUpdate("insert into "+ds.logDatabaseName()+".empty_store_return_other_brands(document_id,posting_date,product_sap_code,"+QueryPrt+",total_units,storage_location) values("+rs2.getLong("DocumentID")+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs2.getString("PostingDate")))+","+rs2.getInt("ProductSAPCode")+","+Quantity+","+TotalUnits+","+rs2.getInt("lgort")+")");
			
		}
		
		System.out.println("Ended");
		
	}catch(Exception e){
		
		e.printStackTrace();
	}finally{
		ds.dropConnection();
		dsSAP.dropConnection();
	}
}
	
}
