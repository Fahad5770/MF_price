package com.pbc.pushmail.scorecards;

import java.io.FileOutputStream;
import java.io.IOException;
 









import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pbc.common.Distributor;
import com.pbc.util.Datasource;
import com.pbc.util.UserAccess;
import com.pbc.util.Utilities;

public class DistributorTargetAnalysisPDF {

	public static final String RESULT = Utilities.getEmailAttachmentsPath()+ "/CashInflowSummary.pdf";
	
	public String PackageArray[];
	public double TargetedSale[];
	public double AttainedSale[];
	public double TotalTargetedSalePercentage[];
	public double TotalAttainedSalePercentage[];
	
	
	
	
	
	
	
	
	Font fonttableheader = FontFactory.getFont("Arial", 7, Font.BOLD);
	Font fontheading = FontFactory.getFont("Arial", 8, Font.NORMAL);
	Font fontpbc = FontFactory.getFont("Arial", 9, Font.BOLD);
	Font fonttitle = FontFactory.getFont("Arial", 9, Font.BOLD);
	Font fonttitleblack = FontFactory.getFont("Arial", 9, Font.BOLD);
	Font reportheading = FontFactory.getFont("Arial", 7, Font.BOLD);
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	
	
	//Date StartDate = new Date();
	//Date EndDate = new Date();
	
	
	Date Yesterday = Utilities.getDateByDays(-1);
	
	
	long SND_ID = 0;
	
	
	
	public static void main(String[] args) throws DocumentException, IOException, ParseException{
		
		try {
			
			//Date StartDate2 = Utilities.parseDate("01/01/2014");
			//Date EndDate2 = Utilities.parseDate("31/01/2014");
			
			DistributorTargetAnalysisPDF DistributorSD1 = new DistributorTargetAnalysisPDF();
			DistributorSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/DistTarget"+Math.random()+".pdf", 2262, new Date());
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public DistributorTargetAnalysisPDF() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6){
			Yesterday = Utilities.getDateByDays(-2);
		}
		
	}
	
	public void createPdf(String filename, long SND_ID, Date CurrentDate) throws DocumentException, IOException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		
		this.SND_ID = SND_ID;
		
		//this.StartDate = StartDate1;
		//this.EndDate = EndDate1;
		
		
		Document document = new Document(PageSize.A2.rotate());
	    PdfWriter.getInstance(document, new FileOutputStream(filename));
	    
	    document.open();
	    
	   
	    
	   
	    // --- Top Filters -- //
	    
	    Datasource ds = new Datasource();
	    ds.createConnectionToReplica();
	    Connection c = ds.getConnection();
	    Statement s = c.createStatement();
	    Statement s2 = c.createStatement();
	    Statement s3 = c.createStatement();
	    Statement s4 = c.createStatement();
	    Statement s5 = c.createStatement();
	    Statement s6 = c.createStatement();
	    Statement s7 = c.createStatement();
	    Statement s8 = c.createStatement();
	    Statement s9 = c.createStatement();
	    Statement s10 = c.createStatement();
	    Statement s11= c.createStatement();
	    Statement s12 = c.createStatement();
	    Statement s13 = c.createStatement();
	    Statement s14 = c.createStatement();
	    Statement s15 = c.createStatement();
	    Statement s16 = c.createStatement();
	    Statement s17 = c.createStatement();
	    Statement s18 = c.createStatement();

	 
	    String WherePackage = "";

	   
	    //HOD
	    String WhereHOD = "";
	    
	    //creating temporary table

	    Datasource dsSAP = new Datasource();
	    dsSAP.createConnectionToSAPDB();
	    Statement sSAP = dsSAP.createStatement();


	    Date MinTargetDate = new Date();
	    Date MaxTargetDate = new Date();

	    ResultSet rs14 = s3.executeQuery("select min(dt.start_date), max(dt.end_date) from distributor_targets dt where date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dt.type_id=3");
	    if(rs14.first()){
	    	MinTargetDate = rs14.getDate(1);
	    	MaxTargetDate = rs14.getDate(2); //Utilities.parseDate("10/01/2014");//rs14.getDate(1);
	    }


	    //s.executeUpdate("CREATE  TABLE dist_targets_temp (order_number varchar(100) , distributor_id varchar(100) ,distributor_name varchar(100) ,entry_date date ,order_date date ,fksak varchar(100) ,abgru varchar(100) ,posnr varchar(100) ,sap_code varchar(100) ,arktx varchar(100) , type varchar(100) , raw_case int, units int,total_units int , pstyv varchar(100) )");

	    //s.executeUpdate("CREATE  TABLE dist_targets_report_temp (distributor_id int,distributor_name varchar(100),sales decimal(18,2),target decimal(18,2), percentage varchar(50),package_id int,type_id int,snd_id int, rsm_id int, tdm_id int)");


	    String QueryPrt="";
	    int UnitPerSKU=0;
	    int TotalUnits=0;

	    //System.out.println("Hello "+StartDate);

	    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");


	    //System.out.println("SELECT vbak.vbeln order_number, vbak.kunnr distributor_id, vbak.erdat entry_date, vbak.audat order_date, vbuk.fksak, vbap.ABGRU, vbap.posnr, vbap.matnr, vbap.arktx, vbap.vrkme, vbap.KWMENG, vbap.pstyv FROM sapsr3.vbak vbak join sapsr3.vbuk vbuk on vbak.vbeln = vbuk.vbeln join sapsr3.vbap vbap on vbak.vbeln = vbap.vbeln where vbak.auart in ('ZDIS','ZMRS','ZDFR') and vbak.audat between  '"+df.format(StartDate)+"' and '"+df.format(EndDate)+"' and vbuk.vbtyp = 'C'");

	    //System.out.println("SELECT vbak.vbeln order_number, vbak.kunnr distributor_id, vbak.erdat entry_date, vbak.audat order_date, vbuk.fksak, vbap.ABGRU, vbap.posnr, vbap.matnr, vbap.arktx, vbap.vrkme, vbap.KWMENG, vbap.pstyv FROM sapsr3.vbak vbak join "+dsSAP.getSAPDatabaseAlias()+".vbuk vbuk on vbak.vbeln = vbuk.vbeln join sapsr3.vbap vbap on vbak.vbeln = vbap.vbeln where vbak.auart in ('ZDIS','ZMRS','ZDFR') and vbak.audat between  '"+df.format(StartDate)+"' and '"+df.format(EndDate)+"' and vbuk.vbtyp = 'C'");

	    
	    
	    ResultSet rs5 = sSAP.executeQuery("SELECT vbak.vbeln order_number, vbak.kunnr distributor_id, vbak.erdat entry_date, vbak.audat order_date, vbuk.fksak, vbap.ABGRU, vbap.posnr, vbap.matnr, vbap.arktx, vbap.vrkme, vbap.KWMENG, vbap.pstyv FROM sapsr3.vbak vbak join "+dsSAP.getSAPDatabaseAlias()+".vbuk vbuk on vbak.vbeln = vbuk.vbeln join sapsr3.vbap vbap on vbak.vbeln = vbap.vbeln where vbak.auart in ('ZDIS','ZMRS','ZDFR') and vbak.audat between  '"+df.format(MinTargetDate)+"' and '"+df.format(MaxTargetDate)+"' and vbuk.vbtyp = 'C' and vbap.pstyv != 'TANN' and vbap.ABGRU = ' '");
	    while(rs5.next()){
	    	//System.out.println("insert into dist_targets_temp values("+rs5.getInt("order_number")+","+rs5.getInt("distributor_id")+",(select name from common_distributors cd where cd.distributor_id="+rs5.getInt("distributor_id")+") ,"+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs5.getString("entry_date")))+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs5.getString("order_date")))+",'"+rs5.getString("fksak")+"','"+rs5.getString("ABGRU")+"','"+rs5.getString("posnr")+"','"+rs5.getString("matnr")+"','"+Utilities.filterString(rs5.getString("arktx"), 2, 100)+"','"+rs5.getString("vrkme")+"',"+rs5.getInt("KWMENG")+",'"+rs5.getString("pstyv")+"')");
	    	
	    	long SapCode=Utilities.parseLong(rs5.getString("matnr"));
	    	
	    	if(rs5.getString("vrkme").equals("KI") || rs5.getString("vrkme").equals("KAR")){					
	    		
	    		QueryPrt ="raw_case";
	    		ResultSet rs1 = s2.executeQuery("select unit_per_sku from inventory_products where id= (select id from inventory_products where sap_code="+SapCode+")");
	    		if(rs1.first()){
	    			UnitPerSKU = rs1.getInt("unit_per_sku");
	    			TotalUnits = UnitPerSKU*(Utilities.parseInt(rs5.getString("KWMENG")));
	    		}else{
	    			//System.out.println("Unit Per SKU does not exist for "+SapCode);
	    		}
	    			
	    	}else{
	    		QueryPrt ="units";
	    		TotalUnits = Utilities.parseInt(rs5.getString("KWMENG"));
	    	}
	    	
	    	//System.out.println("Units "+TotalUnits);
	    	
	    	
	    	//System.out.println("insert into dist_targets_temp(order_number  , distributor_id  ,distributor_name  ,entry_date  ,order_date  ,fksak  ,abgru  ,posnr  ,sap_code  ,arktx  , type  , "+QueryPrt+" ,total_units  , pstyv ) values('"+rs5.getString("order_number")+"','"+rs5.getString("distributor_id")+"',(select name from common_distributors cd where cd.distributor_id='"+rs5.getString("distributor_id")+"') ,"+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs5.getString("entry_date")))+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs5.getString("order_date")))+",'"+rs5.getString("fksak")+"','"+rs5.getString("ABGRU")+"','"+rs5.getString("posnr")+"','"+rs5.getString("matnr")+"','"+Utilities.filterString(rs5.getString("arktx"), 2, 100)+"','"+rs5.getString("vrkme")+"','"+rs5.getString("KWMENG")+"','"+TotalUnits+"','"+rs5.getString("pstyv")+"')");
	    	
	    	s.executeUpdate("insert into dist_targets_temp(order_number  , distributor_id  ,distributor_name  ,entry_date  ,order_date  ,fksak  ,abgru  ,posnr  ,sap_code  ,arktx  , type  , "+QueryPrt+" ,total_units  , pstyv ) values('"+rs5.getString("order_number")+"','"+rs5.getString("distributor_id")+"',(select name from common_distributors cd where cd.distributor_id='"+rs5.getString("distributor_id")+"') ,"+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs5.getString("entry_date")))+","+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs5.getString("order_date")))+",'"+rs5.getString("fksak")+"','"+rs5.getString("ABGRU")+"','"+rs5.getString("posnr")+"',"+SapCode+",'"+Utilities.filterString(rs5.getString("arktx"), 2, 100)+"','"+rs5.getString("vrkme")+"',"+rs5.getInt("KWMENG")+","+TotalUnits+",'"+rs5.getString("pstyv")+"')");
	    }
	    
	    
	    // ---------------------------- //
		

       // -- Putting data in Temp Tabel -- //
	    
	    
	    String SAPCodes = "0";

	    //System.out.println("SELECT group_concat(distinct sap_code) from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDate(MinTargetDate)+" and "+Utilities.getSQLDate(MaxTargetDate));
	    
		ResultSet rs60 = s.executeQuery("SELECT group_concat(distinct sap_code) from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDate(MinTargetDate)+" and "+Utilities.getSQLDate(MaxTargetDate));
		if (rs60.next()){
			SAPCodes = rs60.getString(1);
		}

		int PackageCount = 0;
		int ArrayCount=0;
		int PackageID=0;
		int DistributorID=0;
		double GrandTotal=0;
		String DistributorName="";
		
		int SNDID=0;
		int RSMID=0;
		int TDMID=0;
		
		//System.out.println("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  product_id in (SELECT distinct idnp.product_id FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id where idn.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+") "+WherePackage+ "order by package_sort_order");
		
		ResultSet rs11 = s.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
		while(rs11.next()){
		ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id="+rs11.getInt("lrb_type_id")+" order by package_sort_order");
		
		
		while(rs2.next()){
			
			PackageCount++;
		
			ArrayCount++;
		}
		
		//PackageCount = PackageCount+1; //1 plus for extra column of converted cases
		
		
	}
		
		PackageArray = new String [ArrayCount];	
		TargetedSale = new double [ArrayCount];
		AttainedSale = new double [ArrayCount];
		TotalTargetedSalePercentage = new double [ArrayCount];
		TotalAttainedSalePercentage = new double [ArrayCount];
		
		
		
		
	long SalesTotal[] = new long[ArrayCount+1];
	int TargetTotal[] = new int[ArrayCount+1];
	long PercentageTotal[] = new long[ArrayCount+1]; //+1 for converted cases
	
	for (int i = 0; i < SalesTotal.length; i++){
		SalesTotal[i] = 0;								
		TargetTotal[i]=0;
		PercentageTotal[i]=0;
	}
	
						ResultSet rs1 = s.executeQuery("select * from common_distributors where 1=1 "+WhereHOD+" and (snd_id in ("+SND_ID+") or rsm_id in ("+SND_ID+")  or tdm_id in ("+SND_ID+") ) and distributor_id in (select dt.distributor_id from distributor_targets dt where date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dt.type_id=3)"); //distributor query
						
						while(rs1.next()){
							
							//double TDMWiseTotalSales=0;
							//double TDMWiseTotalTarget=0;
							
							
							double TotalSalesConverted = 0;
							double TotalTargetConverted = 0;
							double TotalTargetConvertedMTD = 0;
							
							DistributorID = rs1.getInt("distributor_id");
							DistributorName = rs1.getString("name");
							SNDID = rs1.getInt("snd_id");
							RSMID = rs1.getInt("rsm_id");
							TDMID = rs1.getInt("tdm_id");
							
							
							Date TargetStartDate = new Date();
							Date TargetEndDate = new Date();

							ResultSet rs19 = s3.executeQuery("select dt.start_date, dt.end_date from distributor_targets dt where date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dt.distributor_id = "+DistributorID+" and dt.type_id=3");
							if(rs19.first()){
								TargetStartDate = rs19.getDate(1);
								TargetEndDate = rs19.getDate(2);
							}
							
					
							//ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM pep.mobile_order_products) "+WherePackage+" order by package_sort_order");//package query
							//rs.beforeFirst();
							
							int PackageIndex = 0;
							long SalesConvertedCases=0;
							long TargetConvertedCases=0;
							double ConvertedCasesPercentage=0;
							
							long SalesConvertedCasesCSD=0;
							long TargetConvertedCasesCSD=0;
							double ConvertedCasesPercentageCSD=0;
							
							
							long SalesConvertedCasesNCB=0;
							long TargetConvertedCasesNCB=0;
							double ConvertedCasesPercentageNCB=0;
							
							
							int TypeID=0;
							
							//Converted Cases 
							/*
							ResultSet rs6 = s3.executeQuery("select "+    
										"sum(((total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml ) ) sale_amount "+																		        
								",ipv.unit_per_sku,ipv.conversion_rate_in_ml,ipv.liquid_in_ml "+
								"from "+
								    "dist_targets_temp dtt "+
								        "join "+								   
								    "inventory_products_view ipv ON dtt.sap_code=ipv.sap_code "+
								"where ipv.category_id = 1 and dtt.order_date between "+Utilities.getSQLDate(TargetStartDate)+" and "+Utilities.getSQLDate(CurrentDate)+" and dtt.distributor_id = "+DistributorID);
							
							if(rs6.first()){								
									SalesConvertedCases = rs6.getLong("sale_amount");	
									TDMWiseTotalSales+=SalesConvertedCases;
							}
							 
							
							ResultSet rs7 = s3.executeQuery("select sum((dtp.quantity*ip.unit_per_case*ip.liquid_in_ml)/ip.conversion_rate_in_ml) quantity,ip.liquid_in_ml,ip.conversion_rate_in_ml from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id join inventory_packages ip on dtp.package_id = ip.id where dt.distributor_id="+DistributorID+" and date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dt.type_id=3");
							if(rs7.first()){								
									TargetConvertedCases = rs7.getLong("quantity");	
									TDMWiseTotalTarget+=TargetConvertedCases;
							}
							
							if(TargetConvertedCases!=0){
								ConvertedCasesPercentage = ((SalesConvertedCases * 1d) / (TargetConvertedCases * 1d) )*100;
							}
							
							s7.executeUpdate("insert into dist_targets_report_temp(distributor_id,distributor_name,sales,target, percentage,package_id,type_id,snd_id, rsm_id, tdm_id) values("+DistributorID+",'"+DistributorName+"',"+SalesConvertedCases+","+TargetConvertedCases+",'"+ConvertedCasesPercentage+"',-1,-1,"+SNDID+","+RSMID+","+TDMID+")");
							*/
							SalesTotal[PackageIndex] += SalesConvertedCases;
							TargetTotal[PackageIndex] += TargetConvertedCases;							
							
							
							
							
							//////////////////////////// ----------- Converted FOR CSD --------------------- ////////////////////////////////////
							
							/*
							ResultSet rs61 = s3.executeQuery("select "+    
									"sum(((total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml ) ) sale_amount "+																		        
							",ipv.unit_per_sku,ipv.conversion_rate_in_ml,ipv.liquid_in_ml "+
							"from "+
							    "dist_targets_temp dtt "+
							        "join "+								   
							    "inventory_products_view ipv ON dtt.sap_code=ipv.sap_code "+
							"where ipv.category_id = 1 and dtt.order_date between "+Utilities.getSQLDate(TargetStartDate)+" and "+Utilities.getSQLDate(CurrentDate)+" and dtt.distributor_id = "+DistributorID+" and ipv.lrb_type_id=1");
						
						if(rs61.first()){								
								SalesConvertedCasesCSD = rs61.getLong("sale_amount");	
								
						}
							*/
							
						
						// TARGET CSD
						/*
						
						TypeID=1; //1 for CSD
					
							ResultSet rs12i = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id=1 order by package_sort_order");
							
							while(rs12i.next()){
								
								double iTargetConverted = 0;
								int unit_per_sku=0;
								PackageID=rs12i.getInt("package_id");
							
								ResultSet rs71i = s3.executeQuery("select sum((dtp.quantity*ip.unit_per_case*ip.liquid_in_ml)/ip.conversion_rate_in_ml) quantity,ip.liquid_in_ml,ip.conversion_rate_in_ml from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id join inventory_packages ip on dtp.package_id = ip.id where dt.distributor_id="+DistributorID+" and date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dt.type_id=3 and dtp.package_id="+PackageID);
								if(rs71i.first()){								
									iTargetConverted = rs71i.getLong("quantity");	
										
								}
								
								if (PackageID == 2 || PackageID == 6){ // 500ML, 1500ML
									iTargetConverted = 0;
									if (TypeID == 1 || TypeID == 2 || TypeID == 3 || TypeID == 4){ // Water, Energy Drink, Juices
										ResultSet rs20 = s3.executeQuery("select sum((dtp.quantity*ip.unit_per_case*ip.liquid_in_ml)/ip.conversion_rate_in_ml) from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id join distributor_targets_packages_brands dtpb on dtp.id = dtpb.id and dtp.package_id = dtpb.package_id  join inventory_packages ip on dtp.package_id = ip.id where dt.distributor_id="+DistributorID+" and date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dtp.package_id="+PackageID+" and dt.type_id=3 and dtpb.brand_id in (select brand_id from inventory_products where lrb_type_id = "+TypeID+")");
										if(rs20.first()){
											iTargetConverted =rs20.getLong(1);	
										}
									}
								}
								
								TargetConvertedCasesCSD += iTargetConverted; 
							
							}
						
						
							if(TargetConvertedCasesCSD!=0){
								ConvertedCasesPercentageCSD = ((SalesConvertedCasesCSD * 1d) / (TargetConvertedCasesCSD * 1d) )*100;
							}
							
							s7.executeUpdate("insert into dist_targets_report_temp(distributor_id,distributor_name,sales,target, percentage,package_id,type_id,snd_id, rsm_id, tdm_id) values("+DistributorID+",'"+DistributorName+"',"+SalesConvertedCasesCSD+","+TargetConvertedCasesCSD+",'"+ConvertedCasesPercentageCSD+"',-2,-2,"+SNDID+","+RSMID+","+TDMID+")");
							
							//////////////////////////////////////////////////////////////////////////////////////////////////////////
							*/
							
							
							////////////////////////////----------- Converted FOR NCB --------------------- ////////////////////////////////////
							
							/*
							ResultSet rs612 = s3.executeQuery("select "+    
									"sum(((total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml ) ) sale_amount "+																		        
							",ipv.unit_per_sku,ipv.conversion_rate_in_ml,ipv.liquid_in_ml "+
							"from "+
							    "dist_targets_temp dtt "+
							        "join "+								   
							    "inventory_products_view ipv ON dtt.sap_code=ipv.sap_code "+
							"where ipv.category_id = 1 and dtt.order_date between "+Utilities.getSQLDate(TargetStartDate)+" and "+Utilities.getSQLDate(CurrentDate)+" and dtt.distributor_id = "+DistributorID+" and ipv.lrb_type_id!=1");
						
						if(rs612.first()){								
								SalesConvertedCasesNCB = rs612.getLong("sale_amount");	
								
						}
							*/
							
						
						// TARGET NCB
						
						/*
						TypeID=1; //not CSD 
					
							ResultSet rs12ii = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id!=1 order by package_sort_order");
							
							while(rs12ii.next()){
								
								double iTargetConverted = 0;
								
								int unit_per_sku=0;
								PackageID=rs12ii.getInt("package_id");
							
								ResultSet rs71ii = s3.executeQuery("select sum((dtp.quantity*ip.unit_per_case*ip.liquid_in_ml)/ip.conversion_rate_in_ml) quantity,ip.liquid_in_ml,ip.conversion_rate_in_ml from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id join inventory_packages ip on dtp.package_id = ip.id where dt.distributor_id="+DistributorID+" and date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dt.type_id=3 and dtp.package_id="+PackageID);
								if(rs71ii.first()){								
									iTargetConverted = rs71ii.getLong("quantity");	
										
								}
								
								
								if (PackageID == 2 || PackageID == 6){ // 500ML, 1500ML
									iTargetConverted = 0;
									
									if (TypeID == 1 || TypeID == 2 || TypeID == 3 || TypeID == 4){ // Water, Energy Drink, Juices
										ResultSet rs20 = s3.executeQuery("select sum((dtp.quantity*ip.unit_per_case*ip.liquid_in_ml)/ip.conversion_rate_in_ml) from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id join distributor_targets_packages_brands dtpb on dtp.id = dtpb.id and dtp.package_id = dtpb.package_id  join inventory_packages ip on dtp.package_id = ip.id where dt.distributor_id="+DistributorID+" and date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dtp.package_id="+PackageID+" and dt.type_id=3 and dtpb.brand_id in (select brand_id from inventory_products where lrb_type_id in (2,3,4))");
										if(rs20.first()){
											iTargetConverted=rs20.getLong(1);	
										}
									}
								}
								
								
								TargetConvertedCasesNCB += iTargetConverted;
							}
							
							
						
							if(TargetConvertedCasesNCB!=0){
								ConvertedCasesPercentageNCB = ((SalesConvertedCasesNCB * 1d) / (TargetConvertedCasesNCB * 1d) )*100;
							}
							
							s7.executeUpdate("insert into dist_targets_report_temp(distributor_id,distributor_name,sales,target, percentage,package_id,type_id,snd_id, rsm_id, tdm_id) values("+DistributorID+",'"+DistributorName+"',"+SalesConvertedCasesNCB+","+TargetConvertedCasesNCB+",'"+ConvertedCasesPercentageNCB+"',-3,-3,"+SNDID+","+RSMID+","+TDMID+")");
							
							//////////////////////////////////////////////////////////////////////////////////////////////////////////
							*/
							
							
							PackageIndex++;
							
						
							
						
							ResultSet rs12 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
							while(rs12.next()){
								
								TypeID = rs12.getInt("lrb_type_id");
								
								ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku, liquid_in_ml FROM inventory_products_view where  sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id="+rs12.getInt("lrb_type_id")+" order by package_sort_order");
								while(rs.next()){
									
									int unit_per_sku=0;
									PackageID=rs.getInt("package_id");
								
									double target_sales_amount=0;
									double TargetUnitPerSKU = rs.getDouble(3);
									double TargetLiquidInML = rs.getDouble(4);
									
									double percentage=0;
									
									int TargetTotalDays = 0;
									int TargetDaysLapsed = 0;
									ResultSet rs99 = s3.executeQuery("select (to_days(dt.end_date)-to_days(dt.start_date)) TotalDays, to_days(date("+Utilities.getSQLDate(CurrentDate)+"))-to_days(dt.start_date) DaysLapsed from distributor_targets dt where dt.distributor_id="+DistributorID+" and date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dt.type_id=3");
									if(rs99.first()){
										TargetTotalDays = rs99.getInt(1);
										TargetDaysLapsed = rs99.getInt(2);
									}
									
									
									ResultSet rs4 = s3.executeQuery("select dtp.quantity from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.distributor_id="+DistributorID+" and date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dtp.package_id="+PackageID+WherePackage+" and dt.type_id=3");
									if(rs4.first()){
										target_sales_amount=rs4.getDouble("quantity");	
									}
									
									if (PackageID == 2 || PackageID == 6){ // 500ML, 1500ML
										if (TypeID == 1 || TypeID == 2 || TypeID == 3 || TypeID == 4){ // Water, Energy Drink, Juices
											//target_sales_amount = 0;
											ResultSet rs20 = s3.executeQuery("select sum(dtpb.quantity) from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id join distributor_targets_packages_brands dtpb on dtp.id = dtpb.id and dtp.package_id = dtpb.package_id where dt.distributor_id="+DistributorID+" and date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dtp.package_id="+PackageID+" and dt.type_id=3 and dtpb.brand_id in (select brand_id from inventory_products where lrb_type_id = "+TypeID+")");
											if(rs20.first()){
												target_sales_amount=rs20.getDouble(1);	
											}
										}
									}
									
									double TargetMTD = (target_sales_amount / TargetTotalDays) * TargetDaysLapsed;
									
									long sales_amount=0;
									ResultSet rs3 = s3.executeQuery("select sum(total_units) sale_amount, ipv.unit_per_sku from dist_targets_temp dtt join inventory_products_view ipv ON dtt.sap_code = ipv.sap_code where ipv.category_id = 1 and dtt.order_date between "+Utilities.getSQLDate(TargetStartDate)+" and "+Utilities.getSQLDate(CurrentDate)+" and dtt.distributor_id = "+DistributorID+" and ipv.package_id="+PackageID+WherePackage+" and ipv.lrb_type_id="+TypeID);
									if(rs3.first()){
										sales_amount = rs3.getLong("sale_amount");									
										unit_per_sku = rs3.getInt("unit_per_sku");								
									}
									
									long SalesRawCases = Utilities.getRawCasesAndUnits(sales_amount, unit_per_sku)[0];
									
									
									/* Insert converted cases */
									
									int ConvertedTypeID = -2; // CSD
									if (TypeID != 1){
										ConvertedTypeID = -3; // NCB
									}
									
									double TargetConverted = (target_sales_amount *  TargetUnitPerSKU * TargetLiquidInML) / 6000;
									double SalesConverted = (SalesRawCases * TargetUnitPerSKU * TargetLiquidInML) / 6000;
									
									
									double TargetConvertedMTD = (TargetConverted / TargetTotalDays) * TargetDaysLapsed;
									TotalSalesConverted += SalesConverted;
									TotalTargetConverted += TargetConverted;
									TotalTargetConvertedMTD += TargetConvertedMTD;
									
									
									
									s7.executeUpdate("insert into dist_targets_report_temp(distributor_id,distributor_name,sales,target, percentage,package_id,type_id,snd_id, rsm_id, tdm_id, target_mtd) values("+DistributorID+",'"+DistributorName+"',"+SalesConverted+","+TargetConverted+",'0',"+ConvertedTypeID+","+ConvertedTypeID+","+SNDID+","+RSMID+","+TDMID+","+TargetConvertedMTD+")");
									
									
									/* end insert converted cases*/
									
									
									if(target_sales_amount!=0){
										percentage = (Utilities.parseDouble(SalesRawCases+"")/target_sales_amount)*100;	
									}	
									
									SalesTotal[PackageIndex] += SalesRawCases;
									TargetTotal[PackageIndex] += target_sales_amount;
									
							
									
									//insert it into temp table
									
									s7.executeUpdate("insert into dist_targets_report_temp(distributor_id,distributor_name,sales,target, percentage,package_id,type_id,snd_id, rsm_id, tdm_id, target_mtd) values("+DistributorID+",'"+DistributorName+"',"+SalesRawCases+","+target_sales_amount+",'"+percentage+"',"+PackageID+","+TypeID+","+SNDID+","+RSMID+","+TDMID+","+TargetMTD+")");
									
									PackageIndex++;
								}
							}
							
							double TotalConvertedPercentage = 0;
							if (TotalTargetConverted != 0){
								TotalConvertedPercentage = (TotalSalesConverted / TotalTargetConverted) * 100;
							}
							s7.executeUpdate("insert into dist_targets_report_temp(distributor_id,distributor_name,sales,target, percentage,package_id,type_id,snd_id, rsm_id, tdm_id, target_mtd) values("+DistributorID+",'"+DistributorName+"',"+TotalSalesConverted+","+TotalTargetConverted+",'"+TotalConvertedPercentage+"',-1,-1,"+SNDID+","+RSMID+","+TDMID+","+TotalTargetConvertedMTD+")");
					}
	
					
	    
	    
	    // -- ---------------------------- -- //
        
		
		
		// -- Display Report -- //
						int rowcount=0;
						
						//System.out.println("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
						
						ResultSet rs1011 = s.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
						while(rs1011.next()){
							
							rowcount += rs1011.getInt("packge_count");
							
						}
						
						
						PdfPTable table = new PdfPTable((rowcount*3)+7) ;  //*3 for [S T P] and + 6 for [4 for distributor Name] [3 for Converted S T P]
					    
					    table.setWidthPercentage(100f);
					    table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
					    
					    reportheading.setColor(BaseColor.WHITE);
					    fonttitle.setColor(BaseColor.WHITE);
					    
					    
					    PdfPCell pcell = new PdfPCell(new Phrase("Distributor Target Analysis", fonttitle));
				        pcell.setBackgroundColor(BaseColor.BLACK);
				        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				        pcell.setBorderColor(BaseColor.BLACK);
				        pcell.setColspan((rowcount*3)+7);
				        pcell.setMinimumHeight(22);
				        table.addCell(pcell);	
				        
				      //  System.out.println(rowcount);
				        
				        
				        table.addCell(getHeadingCellLevel2("",4)); //For heading 
				        table.addCell(getHeadingCellLevel2("",3)); //For heading 
				        
				       // System.out.println("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
				        
				        ResultSet rs101 = s.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
						while(rs101.next()){
							
							table.addCell(getHeadingCellLevel2Center(rs101.getString("type_name"),rs101.getInt("packge_count")*3));
							
						}
						
						
						 table.addCell(getHeadingCellCenter("",4)); //For heading 
					     table.addCell(getHeadingCellCenter("Converted Cases",3)); //For heading
						
						ResultSet rs111 = s.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
						while(rs111.next()){
						ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id="+rs111.getInt("lrb_type_id")+" order by package_sort_order");
						
						
						while(rs2.next()){
							
							PackageCount++;
						
							table.addCell(getHeadingCellCenter(rs2.getString("package_label"),3));
								
						}
						
						//PackageCount = PackageCount+1; //1 plus for extra column of converted cases
						
						
					}
						
						table.addCell(getHeadingCellCenter("",4));
						table.addCell(getHeadingCellCenter("S"));
						table.addCell(getHeadingCellCenter("T"));
						table.addCell(getHeadingCellCenter("P"));
						  
						for (int i=0; i<ArrayCount; i++){
							  table.addCell(getHeadingCellCenter("S"));
							  table.addCell(getHeadingCellCenter("T"));
							  table.addCell(getHeadingCellCenter("P"));
						}
						
						
						
						
						
						long DistributorIDD1=0;
						long DistributorIDD=0;
						
						long RSMID1=0;
						String RSMName="";
						String RSMDisplay="";
						
						long SNDID1=0;
						String SNDName="";
						String SNDDisplay ="";
						int PackArrayCount=0;
						
						ResultSet rs1132 = s16.executeQuery("SELECT distinct snd_id,(select display_name from users u where u.id=snd_id) snd_name FROM dist_targets_report_temp where snd_id is not null");
						while(rs1132.next()){
							SNDID1 = rs1132.getLong("snd_id");
							SNDName = rs1132.getString("snd_name");
							
							
							
							SNDDisplay = SNDID1+ " - "+ SNDName;
							if (SNDID1 == 0){
								SNDDisplay = "Unassigned";
							}
							
							
							table.addCell(getHeadingCellCenterSND(SNDDisplay,4));
										
										
										ResultSet rs3152 = s17.executeQuery("select sum(sales) sum_converted,sum(target) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and snd_id="+SNDID1);
										if(rs3152.first()){
											double CasesCovertedPercentage1=0;
											double SalesCoverted = rs3152.getDouble("sum_converted");
											double TargetCoverted = rs3152.getDouble("sum_targeted");
											
											if(TargetCoverted!=0){
												CasesCovertedPercentage1 = ((SalesCoverted * 1d) / (TargetCoverted * 1d) )*100;
											}
										
										if(SalesCoverted!=0){
											table.addCell(getHeadingCellCenterSND(Utilities.getDisplayCurrencyFormatRounded(SalesCoverted)));
										}else{
											table.addCell(getHeadingCellCenterSND(""));
										}
										
										if(TargetCoverted!=0){
											table.addCell(getHeadingCellCenterSND(Utilities.getDisplayCurrencyFormatRounded(TargetCoverted)));
										}else{
											table.addCell(getHeadingCellCenterSND(""));
										}
											
										if(CasesCovertedPercentage1!=0){
											table.addCell(getHeadingCellCenterSND(Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage1)+"%"));
										}else{
											table.addCell(getHeadingCellCenterSND(""));
										}
											
											
											
										
										}
								ResultSet rs1232 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
								while(rs1232.next()){
									
									int TypeID3 = rs1232.getInt("lrb_type_id");
									String TypeName = rs1232.getString("type_name");
									ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku,liquid_in_ml FROM inventory_products_view where  sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id="+rs1232.getInt("lrb_type_id")+" order by package_sort_order");
									
									while(rs.next()){
									int unit_per_sku=0;
									PackageID=rs.getInt("package_id");
									String PackageLabel="";									
									PackageLabel = rs.getString("package_label");
									
									double UnitPerSKUPieChart=rs.getDouble("unit_per_sku");
									double LiquidInMLPieChart=rs.getDouble("liquid_in_ml");
									
									//System.out.println("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1);
									
									ResultSet rs315 = s18.executeQuery("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and snd_id="+SNDID1);
									if(rs315.first()){
										double CasesPercentage1=0;
										double Sales = rs315.getDouble("sum_sales");
										double Target = rs315.getDouble("sum_target");
										
										if(Target!=0){	
											CasesPercentage1 = ((Sales * 1d) / (Target * 1d) )*100;
										}
										
										
										if(Sales!=0){
											table.addCell(getHeadingCellCenterSND(Utilities.getDisplayCurrencyFormatRounded(Sales)));
										}else{
											table.addCell(getHeadingCellCenterSND(""));
										}
										
										if(Target!=0){
											table.addCell(getHeadingCellCenterSND(Utilities.getDisplayCurrencyFormatRounded(Target)));
										}else{
											table.addCell(getHeadingCellCenterSND(""));
										}
										
										if(CasesPercentage1!=0){
											table.addCell(getHeadingCellCenterSND(Utilities.getDisplayCurrencyFormatRounded(CasesPercentage1)+"%"));
										}else{
											table.addCell(getHeadingCellCenterSND(""));
										}
										
										
										TargetedSale[PackArrayCount] = Math.round(((Target*UnitPerSKUPieChart)*LiquidInMLPieChart)/6000);
										AttainedSale[PackArrayCount] = Math.round(((Sales*UnitPerSKUPieChart)*LiquidInMLPieChart)/6000);
										
										
									}
									
									if(TypeName.equals("Energy Drinks")){
										TypeName="Sting";
									}
									if(PackageID==6 || PackageID==2){ //500ML and 1500ML
										PackageArray[PackArrayCount] = PackageLabel+" "+TypeName;
									}else{
										PackageArray[PackArrayCount] = PackageLabel;
									}
									
									
									
									
									PackArrayCount++;
									}
								}
						
						
						
						
						
						ResultSet rs1131 = s13.executeQuery("SELECT distinct rsm_id,(select display_name from users u where u.id=rsm_id) rsm_name FROM dist_targets_report_temp where snd_id="+SNDID1);
						while(rs1131.next()){
							RSMID1 = rs1131.getLong("rsm_id");
							RSMName = rs1131.getString("rsm_name");
							
							
							RSMDisplay = RSMID1+ " - "+ RSMName;
							if (RSMID1 == 0){
								RSMDisplay = "Unassigned";
							}
							
							
							table.addCell(getHeadingCellCenterRSM(RSMDisplay,4)); 
										
										
										ResultSet rs3151 = s14.executeQuery("select sum(sales) sum_converted,sum(target) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and rsm_id="+RSMID1+" and snd_id="+SNDID1);
										if(rs3151.first()){
											double CasesCovertedPercentage1=0;
											double SalesCoverted = rs3151.getDouble("sum_converted");
											double TargetCoverted = rs3151.getDouble("sum_targeted");
											
											if(TargetCoverted!=0){
												CasesCovertedPercentage1 = ((SalesCoverted * 1d) / (TargetCoverted * 1d) )*100;
											}
											
										
										
										
										if(SalesCoverted!=0){											
											table.addCell(getHeadingCellCenterRSM(Utilities.getDisplayCurrencyFormatRounded(SalesCoverted)));											
										}else{
											table.addCell(getHeadingCellCenterRSM(""));
										}
										if(TargetCoverted!=0){											
											table.addCell(getHeadingCellCenterRSM(Utilities.getDisplayCurrencyFormatRounded(TargetCoverted)));											
										}else{
											table.addCell(getHeadingCellCenterRSM(""));
										}
										if(CasesCovertedPercentage1!=0){											
											table.addCell(getHeadingCellCenterRSM(Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage1)+"%"));
										}else{
											table.addCell(getHeadingCellCenterRSM(""));
										}
								
										}
								ResultSet rs1231 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
								while(rs1231.next()){
									
									int TypeID3 = rs1231.getInt("lrb_type_id");
									ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id="+rs1231.getInt("lrb_type_id")+" order by package_sort_order");
									
									while(rs.next()){
									int unit_per_sku=0;
									PackageID=rs.getInt("package_id");
									//System.out.println("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1);
									
									ResultSet rs314 = s15.executeQuery("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and rsm_id="+RSMID1+" and snd_id="+SNDID1);
									if(rs314.first()){
										double CasesPercentage1=0;
										double Sales = rs314.getDouble("sum_sales");
										double Target = rs314.getDouble("sum_target");
										
										if(Target!=0){
											CasesPercentage1 = ((Sales * 1d) / (Target * 1d) )*100;
										}
										
										
										if(Sales!=0){
											table.addCell(getHeadingCellCenterRSM(Utilities.getDisplayCurrencyFormatRounded(Sales)));											
										}else{
											table.addCell(getHeadingCellCenterRSM(""));
										}
										if(Target!=0){											
											table.addCell(getHeadingCellCenterRSM(Utilities.getDisplayCurrencyFormatRounded(Target)));																			
										}else{
											table.addCell(getHeadingCellCenterRSM(""));
										}
										if(CasesPercentage1!=0){											
											table.addCell(getHeadingCellCenterRSM(Utilities.getDisplayCurrencyFormatRounded(CasesPercentage1)+"%"));
										}else{
											table.addCell(getHeadingCellCenterRSM(""));
										}
									
									}
									}
								}
								
								long TDMID1 = 0;
								String TDMName = "";
								
								
								
								ResultSet rs113 = s10.executeQuery("SELECT distinct tdm_id,(select display_name from users u where u.id=tdm_id) tdm_name FROM dist_targets_report_temp where rsm_id="+RSMID1+" and snd_id="+SNDID1);
								while(rs113.next()){
									TDMID1 = rs113.getLong("tdm_id");
									TDMName = rs113.getString("tdm_name");
									
									
									 String TDMDisplay = TDMID1+ " - "+ TDMName;
									if (TDMID1 == 0){
										TDMDisplay = "Unassigned";
									}
									
									//;	
									table.addCell(getHeadingCellCenterTDM(TDMDisplay,4)); 		
												
												ResultSet rs315 = s8.executeQuery("select sum(sales) sum_converted,sum(target) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and tdm_id="+TDMID1+" and rsm_id="+RSMID1+" and snd_id="+SNDID1);
												if(rs315.first()){
													double CasesCovertedPercentage1=0;
													double SalesCoverted = rs315.getDouble("sum_converted");
													double TargetCoverted = rs315.getDouble("sum_targeted");
													
													if(TargetCoverted!=0){
														CasesCovertedPercentage1 = ((SalesCoverted * 1d) / (TargetCoverted * 1d) )*100;
													}
												
												
												
													if(SalesCoverted!=0){											
														table.addCell(getHeadingCellCenterTDM(Utilities.getDisplayCurrencyFormatRounded(SalesCoverted)));											
													}else{
														table.addCell(getHeadingCellCenterTDM(""));
													}
													if(TargetCoverted!=0){											
														table.addCell(getHeadingCellCenterTDM(Utilities.getDisplayCurrencyFormatRounded(TargetCoverted)));											
													}else{
														table.addCell(getHeadingCellCenterTDM(""));
													}
													if(CasesCovertedPercentage1!=0){											
														table.addCell(getHeadingCellCenterTDM(Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage1)+"%"));
													}else{
														table.addCell(getHeadingCellCenterTDM(""));
													}
												}
												
										ResultSet rs123 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in (SELECT distinct sap_code from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDate(MinTargetDate)+" and "+Utilities.getSQLDate(MaxTargetDate)+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
										while(rs123.next()){
											
											int TypeID3 = rs123.getInt("lrb_type_id");
											ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in (SELECT distinct sap_code from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDate(MinTargetDate)+" and "+Utilities.getSQLDate(MaxTargetDate)+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id="+rs123.getInt("lrb_type_id")+" order by package_sort_order");
											
											while(rs.next()){
											int unit_per_sku=0;
											PackageID=rs.getInt("package_id");
											//System.out.println("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1);
											
											ResultSet rs314 = s8.executeQuery("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1+" and rsm_id="+RSMID1+" and snd_id="+SNDID1);
											if(rs314.first()){
												double CasesPercentage1=0;
												double Sales = rs314.getDouble("sum_sales");
												double Target = rs314.getDouble("sum_target");
												
												if(Target!=0){
													CasesPercentage1 = ((Sales * 1d) / (Target * 1d) )*100;
												}
												
												
												if(Sales!=0){
													table.addCell(getHeadingCellCenterTDM(Utilities.getDisplayCurrencyFormatRounded(Sales)));											
												}else{
													table.addCell(getHeadingCellCenterTDM(""));
												}
												if(Target!=0){											
													table.addCell(getHeadingCellCenterTDM(Utilities.getDisplayCurrencyFormatRounded(Target)));																			
												}else{
													table.addCell(getHeadingCellCenterTDM(""));
												}
												if(CasesPercentage1!=0){											
													table.addCell(getHeadingCellCenterTDM(Utilities.getDisplayCurrencyFormatRounded(CasesPercentage1)+"%"));
												}else{
													table.addCell(getHeadingCellCenterTDM(""));
												}
											
											}
											}
										}
										
										
										String WhereTDMi = " and tdm_id="+TDMID1;
										if (TDMID1 == 0){
											WhereTDMi = " and tdm_id is null ";
										}
										
										
										String WhereRSMi = " and rsm_id="+RSMID1;
										if (RSMID1 == 0){
											WhereRSMi = " and rsm_id is null ";
										}
										
										String WhereSNDi = " and snd_id="+SNDID1;
										if (SNDID1 == 0){
											WhereSNDi = " and snd_id is null ";
										}
										
										//System.out.println("select * from common_distributors where 1=1 "+WhereHOD+" and distributor_id in ("+DistributorIDs+") and distributor_id in (select dt.distributor_id from distributor_targets dt where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and dt.type_id=3) "+WhereTDMi+" and rsm_id="+RSMID1+" and snd_id="+SNDID1);
										
										ResultSet rs112 = s.executeQuery("select * from common_distributors where 1=1 "+WhereHOD+" and (snd_id in ("+SND_ID+") or rsm_id in ("+SND_ID+") or tdm_id in ("+SND_ID+")) and distributor_id in (select dt.distributor_id from distributor_targets dt where date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dt.type_id=3) "+WhereTDMi+WhereRSMi+WhereSNDi); //distributor query
										while(rs112.next()){
											
											//System.out.println("hello");
											DistributorIDD = rs112.getLong("distributor_id");
											
										 table.addCell(getNormalCellSpan(  Utilities.truncateStringToMax(DistributorIDD+ " - "+ rs112.getString("name"), 29) ,4));	
											
											
											ResultSet rs314 = s8.executeQuery("select * from dist_targets_report_temp where package_id=-1 and type_id=-1 and distributor_id="+rs112.getLong("distributor_id"));
											while(rs314.next()){
												
												double SalesConvertedCases1 = rs314.getDouble("sales");
												double TargetConvertedCases1 = rs314.getDouble("target");
												int ConvertedCasesPercentage1 = rs314.getInt("percentage");
												
												
												if(SalesConvertedCases1!=0){
													table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(SalesConvertedCases1)));
													
												}else{
													table.addCell(getNormalCell(""));
												}
												if(TargetConvertedCases1!=0){
													table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(TargetConvertedCases1)));
													
												}else{
													table.addCell(getNormalCell(""));
												}
												if(ConvertedCasesPercentage1 != 0){
													table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesPercentage1)+"%"));
													
												}else{
													table.addCell(getNormalCell(""));
												}
											
											}
											
											
											
											ResultSet rs12 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in (SELECT distinct sap_code from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDate(MinTargetDate)+" and "+Utilities.getSQLDate(MaxTargetDate)+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
										while(rs12.next()){
											
											int TypeID = rs12.getInt("lrb_type_id");
											
											ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in (SELECT distinct sap_code from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDate(MinTargetDate)+" and "+Utilities.getSQLDate(MaxTargetDate)+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id) "+WherePackage+ " and lrb_type_id="+rs12.getInt("lrb_type_id")+" order by package_sort_order");
											
											while(rs.next()){
											int unit_per_sku=0;
											PackageID=rs.getInt("package_id");
											
											ResultSet rs313 = s8.executeQuery("select * from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID+" and distributor_id="+rs112.getLong("distributor_id"));
											while(rs313.next()){
												
												
												
												
												double SalesRawCases1 = rs313.getDouble("sales");
												double target_sales_amount1 = rs313.getDouble("target");
												int percentage1 = rs313.getInt("percentage");
												
												
												DistributorIDD1=DistributorIDD; 
												
												
												if(SalesRawCases1!=0){
													table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(SalesRawCases1)));
													
												}else{
													table.addCell(getNormalCell(""));
												}
												if(target_sales_amount1!=0){
													table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(target_sales_amount1)));
													
												}else{
													table.addCell(getNormalCell(""));
												}
												if(percentage1 != 0){
													table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(percentage1)+"%"));
													
												}else{
													table.addCell(getNormalCell(""));
												}
																			
												
												
											}
											
											
											}
											
										}		
								
								
								
								}		
								
						}//end of TDM loop	
						}//end of RSM loop
					    }//end of SND loop	
						
	  // -- -------------------------------- -- //					
						
	    
		
		
		
		
		
        
						
        Paragraph header = new Paragraph(Utilities.getDisplayDateFormat(CurrentDate),fontheading);
        header.setAlignment(Element.ALIGN_RIGHT);
        
        Image img = Image.getInstance(String.format(Utilities.getImageResoucesPath()+"/"+"%s", "pepsi28.png"));
        img.scaleToFit(20, 20);
        //img.setTransparency(new int[]{ 0xF0, 0xFF });
        img.setAbsolutePosition(35, 1135);
        
        document.add(img);
        Paragraph pbc = new Paragraph("          Punjab Beverages",fontpbc);
        document.add(pbc);
        
        
        Paragraph Heading = new Paragraph("",fontpbc);
        Heading.setAlignment(Element.ALIGN_CENTER);
	     document.add(Heading);
	    
        
	    document.add(header);
	    document.add(Chunk.NEWLINE);
	    
	    
	    
	    
	    document.add(table);
	    document.add(Chunk.NEWLINE);
	    
	    //document.add(new Paragraph("Revenue/Chiller/Day = Average revenue per chiller per day in last 30 days",fontheading));
	    
		
	    
	    
	    document.close();
	    
		
	    
	}
	
	private PdfPCell getHeadingCell(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        pcell.setMinimumHeight(20);
        return pcell;
	}
	
	private PdfPCell getHeadingCellCenter(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        pcell.setMinimumHeight(20);
        return pcell;
	}
	private PdfPCell getHeadingCellCenter(String title, int colspan){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setVerticalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        pcell.setMinimumHeight(20);
        pcell.setColspan(colspan);
        return pcell;
	}
	
	
	
	private PdfPCell getNormalCell(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        return pcell;
	}
	
private PdfPCell getNormalCellSpan(String title,int colspan){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setColspan(colspan);
        return pcell;
	}
	private PdfPCell getNormalCellCenter(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        return pcell;
	}
	
	private PdfPCell getNormalCellOB(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(255,255,217));
        return pcell;
	}

	private PdfPCell getNormalCellStrikeRate(String title){
		
		int StrikeRate = Utilities.parseInt(title);
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title+"%", fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        
        if (StrikeRate < 50){
        	pcell.setBackgroundColor(BaseColor.RED);
        	pcell.setBorderColor(BaseColor.RED);
        }
        
        return pcell;
	}
	private PdfPCell getNormalCellDropSize(String title){
		
		double DropSize = Utilities.parseDouble(title);
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        
        
        if (DropSize < 4){
        	pcell.setBackgroundColor(BaseColor.RED);
        	pcell.setBorderColor(BaseColor.RED);
        }
        
        return pcell;
	}
	private PdfPCell getNormalCellSKU(String title){
		
		double SKU = Utilities.parseDouble(title);
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        
        if (SKU < 2){
        	pcell.setBackgroundColor(BaseColor.RED);
        	pcell.setBorderColor(BaseColor.RED);
        }
        
        return pcell;
	}
	private PdfPCell getNormalCellPackPerOrder(String title){
		
		double SKU = Utilities.parseDouble(title);
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        
        if (SKU < 1.5){
        	pcell.setBackgroundColor(BaseColor.RED);
        	pcell.setBorderColor(BaseColor.RED);
        }
        
        return pcell;
	}
	
	private PdfPCell getNormalCellRightAligned(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        return pcell;
	}
	private PdfPCell getNormalCellRightAlignedTarget(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(255, 245, 254));
        return pcell;
	}
	
	private PdfPCell getNormalCellTargetAchieved(String title, boolean isHighlighted){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(255, 245, 254));
        
        if (isHighlighted){
        	pcell.setBackgroundColor(BaseColor.RED);
        	pcell.setBorderColor(BaseColor.RED);
        }
        
        return pcell;
	}

	private PdfPCell getNormalCellRightAlignedSSRB(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(233, 255, 217));
        return pcell;
	}
	
	private PdfPCell getNormalCellRightAlignedCC(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(227, 230, 255));
        return pcell;
	}
	
	private PdfPCell getHeadingCellLevel2(String title, int colspan){	
	    PdfPCell pcell2 = new PdfPCell(new Phrase(title, reportheading));
	    pcell2.setBackgroundColor(BaseColor.DARK_GRAY);
	    pcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
	    pcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    pcell2.setBorderColor(BaseColor.LIGHT_GRAY);
	    pcell2.setColspan(colspan);
	    pcell2.setMinimumHeight(20);
	    return pcell2;
	}
	
	private PdfPCell getHeadingCellLevel2Center(String title, int colspan){	
	    PdfPCell pcell2 = new PdfPCell(new Phrase(title, reportheading));
	    pcell2.setBackgroundColor(BaseColor.DARK_GRAY);
	    pcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	    pcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    pcell2.setBorderColor(BaseColor.LIGHT_GRAY);
	    pcell2.setColspan(colspan);
	    pcell2.setMinimumHeight(20);
	    return pcell2;
	}
	
	private PdfPCell getHeadingCellLevel3(String title, int colspan){	
	    PdfPCell pcell2 = new PdfPCell(new Phrase(title, fonttableheader));
	    pcell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
	    pcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
	    pcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    pcell2.setBorderColor(BaseColor.LIGHT_GRAY);
	    pcell2.setColspan(colspan);
	    //pcell2.setMinimumHeight(20);
	    return pcell2;
	}
	private PdfPCell getHeadingCellLevel3Center(String title, int colspan){	
	    PdfPCell pcell2 = new PdfPCell(new Phrase(title, fonttableheader));
	    pcell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
	    pcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	    pcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    pcell2.setBorderColor(BaseColor.LIGHT_GRAY);
	    pcell2.setColspan(colspan);
	    pcell2.setMinimumHeight(20);
	    return pcell2;
	}
	
	private PdfPCell getHeadingCellCenterSND(String title){
		
		
		PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(194, 194, 194));        
        pcell.setMinimumHeight(20);
        return pcell;
	}
	
private PdfPCell getHeadingCellCenterSND(String title, int colspan){
		
		
		PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(194, 194, 194));
        pcell.setColspan(colspan);
        pcell.setMinimumHeight(20);
        return pcell;
	}

private PdfPCell getHeadingCellCenterRSM(String title){
	
	
	PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
    pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
    pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    pcell.setBorderColor(BaseColor.LIGHT_GRAY);
    pcell.setBackgroundColor(new BaseColor(212, 212, 187));        
    pcell.setMinimumHeight(20);
    return pcell;
}
private PdfPCell getHeadingCellCenterRSM(String title, int colspan){
	
	
	PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
    pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
    pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    pcell.setBorderColor(BaseColor.LIGHT_GRAY);
    pcell.setBackgroundColor(new BaseColor(212, 212, 187));  
    pcell.setColspan(colspan);
    pcell.setMinimumHeight(20);
    return pcell;
}

private PdfPCell getHeadingCellCenterTDM(String title){
	
	
	PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
    pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
    pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    pcell.setBorderColor(BaseColor.LIGHT_GRAY);
    pcell.setBackgroundColor(new BaseColor(237, 237, 224));        
    pcell.setMinimumHeight(20);
    return pcell;
}
private PdfPCell getHeadingCellCenterTDM(String title, int colspan){
	
	
	PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
    pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
    pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    pcell.setBorderColor(BaseColor.LIGHT_GRAY);
    pcell.setBackgroundColor(new BaseColor(237, 237, 224));  
    pcell.setColspan(colspan);
    pcell.setMinimumHeight(20);
    return pcell;
}
	
}
