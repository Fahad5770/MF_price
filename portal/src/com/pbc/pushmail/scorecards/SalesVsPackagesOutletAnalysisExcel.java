package com.pbc.pushmail.scorecards;

import java.io.FileOutputStream;
import java.io.IOException;
 









import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import com.itextpdf.text.Font;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class SalesVsPackagesOutletAnalysisExcel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath()+ "/CashInflowSummary.pdf";
	
	
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	
	Statement s21;
	
	
	Date StartDate = new Date(); //Utilities.getDateByDays(-1); //Utilities.parseDate("13/02/2016");
	Date EndDate = new Date(); //Utilities.getDateByDays(-1);//Utilities.parseDate("13/02/2016");
	
	
	//Date StartDate = Utilities.parseDate("27/07/2018");
	//Date EndDate = Utilities.parseDate("28/07/2018");
	
	
	Date Yesterday = Utilities.getDateByDays(-2);
	
	
	long SND_ID = 0;
	
	public static void main(String[] args)throws Exception 
	   {
		
		
		
		
			   
	   }
	
	public SalesVsPackagesOutletAnalysisExcel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		
		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
		
		s21 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6){
			Yesterday = Utilities.getDateByDays(-3);
		}
		
	}
	
	
	public void createPdf(String filename, long SND_ID, boolean isMTD, boolean isComplete, long SNDID2) throws  IOException, SQLException {
		
		if (isMTD){
			//this.StartDate = Utilities.getStartDateByDate(Utilities.getDateByDays(-1));
			//this.EndDate = Utilities.getDateByDays(-1);
			
			this.StartDate = Utilities.getStartDateByDate(new Date());
			this.EndDate = Utilities.getDateByDays(0);
		}
		
		
		System.out.println("Hello "+Utilities.getDisplayDateFormat(this.StartDate)+" - "+Utilities.getDisplayDateFormat(this.EndDate));
		
		long iDistributorID = 0;
		String DistributorName="";
		String iPJPIDs = "0";
		ResultSet irs = s.executeQuery("select group_concat(id), dbpv.distributor_id, (select name from common_distributors cd where cd.distributor_id=dbpv.distributor_id) distributor_name from distributor_beat_plan dbpv where asm_id="+SND_ID+" group by distributor_id limit 1");
		if (irs.first()){
			iDistributorID = irs.getLong(2);
			iPJPIDs = irs.getString(1);
			
			DistributorName = irs.getString(3);
		}
		
		
		String WhereHOD = " and isap.cache_distributor_id = "+iDistributorID+" ";
		if (SND_ID == 0){
			WhereHOD = "";
		}
		
		
		Date CurrentDate = new Date();
		
		XSSFWorkbook workbook = new XSSFWorkbook(); 
	    XSSFSheet spreadsheet = workbook.createSheet("cell types");
	      
	      
	      int month = Utilities.getMonthNumberByDate(EndDate);
			int year = Utilities.getYearByDate(EndDate);
			Date StartDateLastMonth = Utilities.getStartDateByMonth(month-2, year);
			Date EndDateLastMonth = Utilities.getEndDateByMonth(month-2, year);
			
			int DaysInLastMonth = Utilities.getDayNumberByDate(EndDateLastMonth);
			
			long TotalBrandCount=0;
			
			double GrandTotalBalance = 0;
			double GrandTotalSecondarySales = 0;
			double GrandTotalLiftingTarget = 0;
			
			int FirstRowCount=0;
			
			int RowCount=0;
			
			
			
			
			//Report Heading
			
			 XSSFRow rowH = spreadsheet.createRow((short) RowCount);	      
		     XSSFCell cellH = (XSSFCell) rowH.createCell((short) 0);
		     
		     cellH.setCellValue("R106 - Sales/Package and Outlets Analysis");
			
		     spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  FirstRowCount, //first row (0-based)
		    		  FirstRowCount, //last row (0-based)
		    		  0, //first column (0-based)
		    	      2 //last column (0-based)
		    	      ));
			
		     
		     CellStyle style = workbook.createCellStyle();//Create style
		     XSSFFont font = workbook.createFont();//Create font
		     font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		     style.setFont(font);//set it to bold
		     cellH.setCellStyle(style);
		     
			
		     RowCount = RowCount+1;
			FirstRowCount = FirstRowCount+1;
			//Printing Date
			
			//Report Heading
			
			 XSSFRow rowP = spreadsheet.createRow((short) RowCount);	      
		     
			 XSSFCell cellP = (XSSFCell) rowP.createCell((short) 0);		     
		   
		     
		     cellP.setCellValue("Period: "+Utilities.getDisplayDateFormat(StartDate)+" - "+Utilities.getDisplayDateFormat(EndDate));
				
		     spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  FirstRowCount, //first row (0-based)
		    		  FirstRowCount, //last row (0-based)
		    		  0, //first column (0-based)
		    	      4 //last column (0-based)
		    	      ));
		     
		    
		     
		    
		    
		     XSSFCellStyle style611 = workbook.createCellStyle();		      
		     style611.setAlignment(CellStyle.ALIGN_LEFT);
		     cellP.setCellStyle(style611);
		      
		     
		     
		     // Extra Row
		     
		     
		     RowCount = RowCount+1;
		     FirstRowCount = FirstRowCount+1;	
				//Printing Date
				
				//Report Heading
				
				 XSSFRow rowEx1 = spreadsheet.createRow((short) RowCount);	      
			     
				 XSSFCell cellEx1 = (XSSFCell) rowEx1.createCell((short) 0);		     
			   
			     
				 cellEx1 = (XSSFCell) rowEx1.createCell((short) 0);	
				 cellEx1.setCellValue("");
				 
									
			    
			     
				 
		     
			
		   /////// //////////////////////
		     
				 
		    
		     
		      //3rd Row Header //////////////////////
			     ///////////////////////////////////////
			    
			     RowCount = RowCount+1;
			     FirstRowCount = FirstRowCount+1;	
			     
			     
			     
			   
			     
			     
			     XSSFRow headerrow1 = spreadsheet.createRow((int) RowCount);        
			     XSSFCell headercell1 = (XSSFCell) headerrow1.createCell((short) 0);	     
			    
			    	     
			     headercell1.setCellValue("");
			     
			     
			     
			     
			           
			    // XSSFCell headercell2 = (XSSFCell) headerrow1.createCell((short) 1);	     
			    
			    	     
			    // headercell2.setCellValue("");
			    
			     int NumberOfColums1=1;
			     int NumberOfColums2=4;
			     
			     String ConditionalQuery = "";
			     
			     if(isComplete) {
			    	 ConditionalQuery = "SELECT distinct asm_id, (select display_name from users u where u.id=asm_id) display_name FROM pep.distributor_beat_plan_view"; // full data
			     }else {
			    	 ConditionalQuery = "SELECT distinct asm_id, (select display_name from users u where u.id=asm_id) display_name FROM pep.distributor_beat_plan_view where snd_id="+SNDID2; // full data
			     }
			     
			     
			     ResultSet rs231 = s21.executeQuery(ConditionalQuery);
			     while(rs231.next()){
			     if(rs231.getLong("asm_id")!=0) {
			    	 
			    	 SND_ID = rs231.getLong("asm_id");
			    	 String SND_Name=rs231.getString("display_name");
			    
			     
			    /// String SND_Name="";
			    	 RowCount = RowCount+3;
					 
					  XSSFRow headerrow121 = spreadsheet.createRow((short) RowCount);        
					  XSSFCell headercell121 = (XSSFCell) headerrow1.createCell((short) 0);
					     
					    
					     headercell121 = (XSSFCell) headerrow121.createCell((short) 0);	     
					     headercell121.setCellValue(SND_ID+" - "+SND_Name);
					     Set3rdOutletHeaderBackColor(workbook,headercell121);
					     
					     
					    
					     ResultSet irs1 = s.executeQuery("select group_concat(id), dbpv.distributor_id, (select name from common_distributors cd where cd.distributor_id=dbpv.distributor_id) distributor_name from distributor_beat_plan dbpv where asm_id="+SND_ID+" group by distributor_id limit 1");
							if (irs1.first()){
								iDistributorID = irs1.getLong(2);
								iPJPIDs = irs1.getString(1);
								
								DistributorName = irs1.getString(3);
							}
					     
					     
					     
					     
					     headercell121 = (XSSFCell) headerrow121.createCell((short) 1);	     
					     headercell121.setCellValue(iDistributorID+" - "+DistributorName);
					     Set3rdOutletHeaderBackColor(workbook,headercell121);
					     
					     spreadsheet.addMergedRegion(new CellRangeAddress(
					    		 RowCount, //first row (0-based)
					    		 RowCount, //last row (0-based)
					    		  1, //first column (0-based)
					    	      8 //last column (0-based)
					    	      ));
			    	 
			    	 
			    	 
			    	 
			    	 //RowCount = RowCount+2;
			    	 
			     
			    
					
					
					
					
//Packages header
					
					 RowCount = RowCount+1;
					 
					  XSSFRow headerrow12 = spreadsheet.createRow((short) RowCount);        
					  XSSFCell headercell12 = (XSSFCell) headerrow1.createCell((short) 0);
					     
					    
					     headercell12 = (XSSFCell) headerrow12.createCell((short) 0);	     
					     headercell12.setCellValue("Outlets");
					     Set3rdOutletHeaderBackColor(workbook,headercell12);
					     
					     
					     
					     
					    /* headercell12 = (XSSFCell) headerrow12.createCell((short) 1);	     
					     headercell12.setCellValue("No. of Coolers");
					     Set3rdOutletHeaderBackColor(workbook,headercell12);*/
					 
					
					
						
					     int PackageCount = 0;
							
							
							int ArrayCount=0;
							
							  NumberOfColums1=1;
							  NumberOfColums2=4;
							
							
								
								
								
									
								     headercell12 = (XSSFCell) headerrow12.createCell((short) 1);	     
								     headercell12.setCellValue("250G");
								     Set4rdHeaderBackColor(workbook,headercell12);								     
								     
								     spreadsheet.addMergedRegion(new CellRangeAddress(
								    		 RowCount, //first row (0-based)
								    		 RowCount, //last row (0-based)
								    		 1, //first column (0-based)
								    		 3 //last column (0-based)
								    	      ));
								     
								     
								     headercell12 = (XSSFCell) headerrow12.createCell((short) 4);	     
								     headercell12.setCellValue("500G");
								     Set4rdHeaderBackColor(workbook,headercell12);								     
								     
								     spreadsheet.addMergedRegion(new CellRangeAddress(
								    		 RowCount, //first row (0-based)
								    		 RowCount, //last row (0-based)
								    		 4, //first column (0-based)
								    		 6 //last column (0-based)
								    	      ));
								     
								     
								     
								     headercell12 = (XSSFCell) headerrow12.createCell((short) 7);	     
								     headercell12.setCellValue("25G");
								     Set4rdHeaderBackColor(workbook,headercell12);
								     
								     
								     headercell12 = (XSSFCell) headerrow12.createCell((short) 8);	     
								     headercell12.setCellValue("Order Booker");
								     Set4rdHeaderBackColor(workbook,headercell12);	
								     
								    
								     
								
							
							
								
							
				
							 
							long PackageTotal[] = new long[8];
							int PackageTotalUnitPerSKU[] = new int[8];
							for (int i = 0; i < PackageTotal.length; i++){
								PackageTotal[i] = 0;
								PackageTotalUnitPerSKU[i]=0;
							}
							
							
							
			     
							//Actual Data
							
							//Adding Brands
							RowCount = RowCount+1;
							 
							  XSSFRow headerrow1245 = spreadsheet.createRow((short) RowCount);        
							  XSSFCell headercell1245 = (XSSFCell) headerrow1245.createCell((short) 0);
							     
							    
							     headercell1245 = (XSSFCell) headerrow1245.createCell((short) 0);	     
							     headercell1245.setCellValue("");
							     Set3rdOutletHeaderBackColor(workbook,headercell1245);
							     
							     
							     headercell1245 = (XSSFCell) headerrow1245.createCell((short) 1);	     
							     headercell1245.setCellValue("BEETA Original");
							     Set3rdOutletHeaderBackColor(workbook,headercell1245);
							     
							     
							     headercell1245 = (XSSFCell) headerrow1245.createCell((short) 2);	     
							     headercell1245.setCellValue("BEETA Strawberry");
							     Set3rdOutletHeaderBackColor(workbook,headercell1245);
							     
							     headercell1245 = (XSSFCell) headerrow1245.createCell((short) 3);	     
							     headercell1245.setCellValue("BEETA Choco Hazelnut");
							     Set3rdOutletHeaderBackColor(workbook,headercell1245);
							     
							     
							     
							     
							     
							     headercell1245 = (XSSFCell) headerrow1245.createCell((short) 4);	     
							     headercell1245.setCellValue("BEETA Original");
							     Set3rdOutletHeaderBackColor(workbook,headercell1245);
							     
							     headercell1245 = (XSSFCell) headerrow1245.createCell((short) 5);	     
							     headercell1245.setCellValue("BEETA Strawberry");
							     Set3rdOutletHeaderBackColor(workbook,headercell1245);
							     
							     headercell1245 = (XSSFCell) headerrow1245.createCell((short) 6);	     
							     headercell1245.setCellValue("BEETA Choco Hazelnut");
							     Set3rdOutletHeaderBackColor(workbook,headercell1245);
							     
							     
							     
							     
							     
							     
							     
							     headercell1245 = (XSSFCell) headerrow1245.createCell((short) 7);	     
							     headercell1245.setCellValue("BEETA Original");
							     Set3rdOutletHeaderBackColor(workbook,headercell1245);
							     
							     headercell1245 = (XSSFCell) headerrow1245.createCell((short) 8);	     
							     headercell1245.setCellValue("");
							     Set3rdOutletHeaderBackColor(workbook,headercell1245);
							    
							
							
							
							
							
							
							//////////////////////////////////////////////////////////////////
							
							
							
							
							
							 RowCount = RowCount+1;
							 
							 
							 
							 
							
							 
								 
							
							String OutletName = "";
							long OutletID=0;
							/////ResultSet rs1 = s.executeQuery("select distinct cache_outlet_id as outlet_id, (select name from common_outlets co where co.id=cache_outlet_id) name from inventory_sales_adjusted_products where cache_order_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)); //distributor query
							
							//Above outlet query was wrong, identified by AlMoiz. Now query updated by Zulqurnan - 18-09-2018
							
							
							
							//System.out.println("select distinct dbpv.outlet_id,co.name from distributor_beat_plan_view dbpv,common_outlets co where co.id = dbpv.outlet_id and dbpv.asm_id="+SND_ID+" and outlet_id in (select distinct cache_outlet_id  from inventory_sales_adjusted_products where cache_order_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+")");
							
							
							
							
							
							
							ResultSet rs1 = s.executeQuery("select distinct dbpv.outlet_id,co.name,dbpv.assigned_to, (select display_name from users u where u.id=dbpv.assigned_to) psr_name from distributor_beat_plan_view dbpv,common_outlets co where co.id = dbpv.outlet_id and dbpv.asm_id="+SND_ID+" and outlet_id in (select distinct outlet_id  from inventory_sales_adjusted where order_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+")"); //distributor query
							
							while(rs1.next()){
								
								//int NoSaleCounter = 0;//rs1.getInt("count");
								
								
								int PackageIndex = 0;
								
								int ActualDataCount=1;
								OutletName = rs1.getInt("outlet_id")+" - "+rs1.getString("name");
								
								OutletID = rs1.getLong("outlet_id");
								
								
								int OrderbookerID = rs1.getInt("assigned_to");
								String OrderBookerName = rs1.getString("psr_name");
								
								
								//System.out.println("Row Count "+RowCount);
								
								  XSSFRow headerrow123 = spreadsheet.createRow((int) RowCount);        
								  XSSFCell headercell123 = (XSSFCell) headerrow123.createCell((short) 0);
								 headercell123 = (XSSFCell) headerrow123.createCell((short) 0);	     
							     headercell123.setCellValue(OutletName);
							     
							     
							    
							     
							     ///////////////////////////////////////////////
							     
							     
												 //Actual Sales Query
											     
												int unit_per_sku=0;
											
												
												//250 - Beeta
												
												ResultSet rs3 = s4.executeQuery("select sum(isap.total_units) qty,isap.cache_units_per_sku from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap force index (inventory_sales_adjusted_products_order_created_outlet_pack_type) on isa.id=isap.id   where isa.order_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isa.outlet_id = "+OutletID+" and isap.product_id =1");	
												while(rs3.next()){
													long qty = rs3.getLong("qty");
													unit_per_sku = rs3.getInt("cache_units_per_sku");
													PackageTotal[1] += qty;
													if(unit_per_sku > 0){
														PackageTotalUnitPerSKU[1] = unit_per_sku;
														
														
													}
													 XSSFCell headercell1234 = (XSSFCell) headerrow123.createCell((short) 1);
													 headercell1234 = (XSSFCell) headerrow123.createCell((short) 1);
												     headercell1234.setCellValue(Utilities.convertToRawCases(qty,unit_per_sku));
												     SetNormalCellBackColorR(workbook,headercell1234);
												  
												}
												
												
												//250 - BEETA Strawberry
												
												ResultSet rs31 = s4.executeQuery("select sum(isap.total_units) qty,isap.cache_units_per_sku from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap force index (inventory_sales_adjusted_products_order_created_outlet_pack_type) on isa.id=isap.id   where isa.order_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isa.outlet_id = "+OutletID+" and isap.product_id =4");	
												while(rs31.next()){
													long qty = rs31.getLong("qty");
													unit_per_sku = rs31.getInt("cache_units_per_sku");
													PackageTotal[2] += qty;
													if(unit_per_sku > 0){
														PackageTotalUnitPerSKU[2] = unit_per_sku;
														
														
													}
													 XSSFCell headercell1234 = (XSSFCell) headerrow123.createCell((short) 2);
													 headercell1234 = (XSSFCell) headerrow123.createCell((short) 2);
												     headercell1234.setCellValue(Utilities.convertToRawCases(qty,unit_per_sku));
												     SetNormalCellBackColorR(workbook,headercell1234);
												  
												}
												
												
												
												
												//250 - BEETA Choco Hazelnut
												
												ResultSet rs312 = s4.executeQuery("select sum(isap.total_units) qty,isap.cache_units_per_sku from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap force index (inventory_sales_adjusted_products_order_created_outlet_pack_type) on isa.id=isap.id   where isa.order_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isa.outlet_id = "+OutletID+" and isap.product_id =5");	
												while(rs312.next()){
													long qty = rs312.getLong("qty");
													unit_per_sku = rs312.getInt("cache_units_per_sku");
													PackageTotal[3] += qty;
													if(unit_per_sku > 0){
														PackageTotalUnitPerSKU[3] = unit_per_sku;
														
														
													}
													 XSSFCell headercell1234 = (XSSFCell) headerrow123.createCell((short) 3);
													 headercell1234 = (XSSFCell) headerrow123.createCell((short) 3);
												     headercell1234.setCellValue(Utilities.convertToRawCases(qty,unit_per_sku));
												     SetNormalCellBackColorR(workbook,headercell1234);
												  
												}
												
												
												
												//500 - BEETA Original
												
												ResultSet rs313 = s4.executeQuery("select sum(isap.total_units) qty,isap.cache_units_per_sku from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap force index (inventory_sales_adjusted_products_order_created_outlet_pack_type) on isa.id=isap.id   where isa.order_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isa.outlet_id = "+OutletID+" and isap.product_id =2");	
												while(rs313.next()){
													long qty = rs313.getLong("qty");
													unit_per_sku = rs313.getInt("cache_units_per_sku");
													PackageTotal[4] += qty;
													if(unit_per_sku > 0){
														PackageTotalUnitPerSKU[4] = unit_per_sku;
														
														
													}
													 XSSFCell headercell1234 = (XSSFCell) headerrow123.createCell((short) 4);
													 headercell1234 = (XSSFCell) headerrow123.createCell((short) 4);
												     headercell1234.setCellValue(Utilities.convertToRawCases(qty,unit_per_sku));
												     SetNormalCellBackColorR(workbook,headercell1234);
												  
												}
												
												
												
												//500 - BEETA Strawberry
												
												ResultSet rs314 = s4.executeQuery("select sum(isap.total_units) qty,isap.cache_units_per_sku from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap force index (inventory_sales_adjusted_products_order_created_outlet_pack_type) on isa.id=isap.id   where isa.order_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isa.outlet_id = "+OutletID+" and isap.product_id =6");	
												while(rs314.next()){
													long qty = rs314.getLong("qty");
													unit_per_sku = rs314.getInt("cache_units_per_sku");
													PackageTotal[5] += qty;
													if(unit_per_sku > 0){
														PackageTotalUnitPerSKU[5] = unit_per_sku;
														
														
													}
													 XSSFCell headercell1234 = (XSSFCell) headerrow123.createCell((short) 5);
													 headercell1234 = (XSSFCell) headerrow123.createCell((short) 5);
												     headercell1234.setCellValue(Utilities.convertToRawCases(qty,unit_per_sku));
												     SetNormalCellBackColorR(workbook,headercell1234);
												  
												}
												
												
												
												//500 - BEETA Choco Hazelnut
												
												ResultSet rs315 = s4.executeQuery("select sum(isap.total_units) qty,isap.cache_units_per_sku from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap force index (inventory_sales_adjusted_products_order_created_outlet_pack_type) on isa.id=isap.id   where isa.order_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isa.outlet_id = "+OutletID+" and isap.product_id =7");	
												while(rs315.next()){
													long qty = rs315.getLong("qty");
													unit_per_sku = rs315.getInt("cache_units_per_sku");
													PackageTotal[6] += qty;
													if(unit_per_sku > 0){
														PackageTotalUnitPerSKU[6] = unit_per_sku;
														
														
													}
													 XSSFCell headercell1234 = (XSSFCell) headerrow123.createCell((short) 6);
													 headercell1234 = (XSSFCell) headerrow123.createCell((short) 6);
												     headercell1234.setCellValue(Utilities.convertToRawCases(qty,unit_per_sku));
												     SetNormalCellBackColorR(workbook,headercell1234);
												  
												}
												
												
												
												//25G - BEETA 
												
												ResultSet rs316 = s4.executeQuery("select sum(isap.total_units) qty,isap.cache_units_per_sku from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap force index (inventory_sales_adjusted_products_order_created_outlet_pack_type) on isa.id=isap.id   where isa.order_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isa.outlet_id = "+OutletID+" and isap.product_id =3");	
												while(rs316.next()){
													long qty = rs316.getLong("qty");
													unit_per_sku = rs316.getInt("cache_units_per_sku");
													PackageTotal[7] += qty;
													if(unit_per_sku > 0){
														PackageTotalUnitPerSKU[7] = unit_per_sku;
														
														
													}
													 XSSFCell headercell1234 = (XSSFCell) headerrow123.createCell((short) 7);
													 headercell1234 = (XSSFCell) headerrow123.createCell((short) 7);
												     headercell1234.setCellValue(Utilities.convertToRawCases(qty,unit_per_sku));
												     SetNormalCellBackColorR(workbook,headercell1234);
												  
												}
												
												
											
											
											XSSFCell headercell1234 = (XSSFCell) headerrow123.createCell((short) 8);
											headercell1234 = (XSSFCell) headerrow123.createCell((short) 8);
											headercell1234.setCellValue(OrderbookerID+" - "+OrderBookerName);
											
											
											
											RowCount++;
											
										}
							     
							
							////////////////////////////////////////////////////////////////////
							///////For no sale outlets which are part of PJP
							///////////////////////////////////////////////////////////////////////
							
							ResultSet rs11 = s.executeQuery("select distinct dbpv.outlet_id,co.name,dbpv.assigned_to, (select display_name from users u where u.id=dbpv.assigned_to) psr_name from distributor_beat_plan_view dbpv,common_outlets co where co.id = dbpv.outlet_id and dbpv.asm_id="+SND_ID+" and outlet_id not in (select distinct outlet_id  from inventory_sales_adjusted where order_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+")"); //distributor query
							
							while(rs11.next()){
								
								//int NoSaleCounter = 0;//rs1.getInt("count");
								
								
								int PackageIndex = 0;
								
								int ActualDataCount=1;
								OutletName = rs11.getInt("outlet_id")+" - "+rs11.getString("name");
								
								OutletID = rs11.getLong("outlet_id");
								
								
								int OrderbookerID = rs11.getInt("assigned_to");
								String OrderBookerName = rs11.getString("psr_name");
								
								
								//System.out.println("Row Count "+RowCount);
								
								  XSSFRow headerrow123 = spreadsheet.createRow((int) RowCount);        
								  XSSFCell headercell123 = (XSSFCell) headerrow123.createCell((short) 0);
								 headercell123 = (XSSFCell) headerrow123.createCell((short) 0);	     
							     headercell123.setCellValue(OutletName);
							     
							     
							    
							     
							     ///////////////////////////////////////////////
							     
							     
												 //Actual Sales Query
											     
												int unit_per_sku=0;
											
												
												//250 - Beeta
												
												
													 XSSFCell headercell12341 = (XSSFCell) headerrow123.createCell((short) 1);
													 headercell12341 = (XSSFCell) headerrow123.createCell((short) 1);
												     headercell12341.setCellValue("");
												     SetNormalCellBackColorR(workbook,headercell12341);
												  
												
												
												
												//250 - BEETA Strawberry
												
												
													 XSSFCell headercell12342 = (XSSFCell) headerrow123.createCell((short) 2);
													 headercell12342 = (XSSFCell) headerrow123.createCell((short) 2);
												     headercell12342.setCellValue("");
												     SetNormalCellBackColorR(workbook,headercell12342);
												  
												
												
												
												
												
												//250 - BEETA Choco Hazelnut
												
												
													 XSSFCell headercell12343 = (XSSFCell) headerrow123.createCell((short) 3);
													 headercell12343 = (XSSFCell) headerrow123.createCell((short) 3);
												     headercell12343.setCellValue("");
												     SetNormalCellBackColorR(workbook,headercell12343);
												  
												
												
												
												
												//500 - BEETA Original
												
												
													 XSSFCell headercell12344 = (XSSFCell) headerrow123.createCell((short) 4);
													 headercell12344 = (XSSFCell) headerrow123.createCell((short) 4);
												     headercell12344.setCellValue("");
												     SetNormalCellBackColorR(workbook,headercell12344);
												  
												
												
												
												
												//500 - BEETA Strawberry
												
												
													 XSSFCell headercell12345 = (XSSFCell) headerrow123.createCell((short) 5);
													 headercell12345 = (XSSFCell) headerrow123.createCell((short) 5);
												     headercell12345.setCellValue("");
												     SetNormalCellBackColorR(workbook,headercell12345);
												  
												
												
												
												
												//500 - BEETA Choco Hazelnut
												
												
													 XSSFCell headercell12346 = (XSSFCell) headerrow123.createCell((short) 6);
													 headercell12346 = (XSSFCell) headerrow123.createCell((short) 6);
												     headercell12346.setCellValue("");
												     SetNormalCellBackColorR(workbook,headercell12346);
												  
												
												
												
												
												//25G - BEETA 
												
												
													 XSSFCell headercell12347 = (XSSFCell) headerrow123.createCell((short) 7);
													 headercell12347 = (XSSFCell) headerrow123.createCell((short) 7);
												     headercell12347.setCellValue("");
												     SetNormalCellBackColorR(workbook,headercell12347);
												  
												
												
												
											
											
											XSSFCell headercell12348 = (XSSFCell) headerrow123.createCell((short) 8);
											headercell12348 = (XSSFCell) headerrow123.createCell((short) 8);
											headercell12348.setCellValue(OrderbookerID+" - "+OrderBookerName);
											
											
											
											RowCount++;
											
										}
							
							
							
							
							
							//////////////////////////////////////////////
							    
							     
							     
							     
							    
							
			     
			     
			    // RowCount=RowCount+1;
			     int TotalRowCount=1;
			     
			     XSSFRow headerrow123 = spreadsheet.createRow((int) RowCount);        
				  XSSFCell headercell123 = (XSSFCell) headerrow123.createCell((short) 0);
				  headercell123 = (XSSFCell) headerrow123.createCell((short) 0);	     
				     headercell123.setCellValue("Total");
				     SetNormalCellBackColor(workbook,headercell123);
				     
				   
				     
				     
				    /* headercell123 = (XSSFCell) headerrow123.createCell((short) 1);	     
				     headercell123.setCellValue(TotalNumberOfChillers);
				     SetNormalCellBackColorR(workbook,headercell123);*/
				     
			     
							for (int i = 1; i < PackageTotal.length; i++){
								
								
								
								 headercell123 = (XSSFCell) headerrow123.createCell((short) TotalRowCount);	     
							     headercell123.setCellValue(Utilities.convertToRawCases(PackageTotal[i],PackageTotalUnitPerSKU[i]));
							     headercell123.setCellType(Cell.CELL_TYPE_STRING);
							     SetNormalCellBackColorR(workbook,headercell123);
							     
								
								
							     TotalRowCount++;	
							
							}
							
							
							headercell123 = (XSSFCell) headerrow123.createCell((short) 8);	     
						     headercell123.setCellValue("");
						     headercell123.setCellType(Cell.CELL_TYPE_STRING);
						     SetNormalCellBackColorR(workbook,headercell123);
							
			     }//end of if for snd loop		     
			   
	}//end of Big Loop
					
					
					
					
				
					
					
				 
				  // Extra Row
				     
				     
				     RowCount = RowCount+1;
				     FirstRowCount = FirstRowCount+1;	
						//Printing Date
						
						//Report Heading
						
						 XSSFRow rowEx11 = spreadsheet.createRow((short) RowCount);	      
					     
						 XSSFCell cellEx11 = (XSSFCell) rowEx11.createCell((short) 0);		     
					   
					     
						 cellEx11 = (XSSFCell) rowEx11.createCell((short) 0);	
						 cellEx11.setCellValue("");
						 
										
					    
					     
						 
				     
					
				   /////// //////////////////////
						 
						 
						 
						
						 
						 
					
					
			
			
			//Auto Sizing Column
		    
				  
				    
		    for(int i=0;i<9;i++){
		    	//System.out.println("Auto Sizing - "+i);
		    	try{
		    		spreadsheet.autoSizeColumn(i);
		    	}catch(Exception e){System.out.println(i);e.printStackTrace();}
		    }
				     
				     
				     /*
				     for (int x = 0; x < spreadsheet.getRow(0).getPhysicalNumberOfCells(); x++) {
				    	 spreadsheet.autoSizeColumn(x);
				     }*/
		    
			
			
			
			
			
			
			FileOutputStream out = new FileOutputStream(
				      new File(filename));
				      workbook.write(out);
				      out.close();
				      //System.out.println(
				      //"typesofcells.xlsx written successfully");
				      
				      ds.dropConnection();
	}
	
	public void Set2ndHeaderBackColor(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(162, 162, 163));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	     /* style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);*/
	      style61.setAlignment(CellStyle.ALIGN_CENTER);
	      headercell.setCellStyle(style61);
	      
	      
	      XSSFFont font = workbook.createFont();
	      font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
	      //font.setFontHeightInPoints((short)10);
	      font.setColor(IndexedColors.WHITE.getIndex());
	      style61.setFont(font);
	}
	
	public void Set2ndHeaderBackColorOrderKPI(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(95, 149, 153));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	     /* style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);*/
	      style61.setAlignment(CellStyle.ALIGN_CENTER);
	      headercell.setCellStyle(style61);
	      
	      XSSFFont font = workbook.createFont();
	      font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
	      //font.setFontHeightInPoints((short)10);
	      font.setColor(IndexedColors.WHITE.getIndex());
	      style61.setFont(font);
	}
	
	public void Set2ndHeaderBackColorDeliveryKPI(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(128, 95, 153));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	     /* style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);*/
	      style61.setAlignment(CellStyle.ALIGN_CENTER);
	      headercell.setCellStyle(style61);
	      
	      XSSFFont font = workbook.createFont();
	      font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
	      //font.setFontHeightInPoints((short)10);
	      font.setColor(IndexedColors.WHITE.getIndex());
	      style61.setFont(font);
	}
	
	public void Set3rdHeaderBackColor(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(227, 227, 227));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	        //style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			//style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			//style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
	      headercell.setCellStyle(style61);
	}
	
	public void Set3rdOutletHeaderBackColor(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(227, 227, 227));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	        style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
	      headercell.setCellStyle(style61);
	}
	
	
	public void Set4rdHeaderBackColor(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(227, 227, 227));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	        //style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			//style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			//style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
	      headercell.setCellStyle(style61);
	}
	
	public void Set3rdHeaderBackColorOrder(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(202, 232, 234));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
	      headercell.setCellStyle(style61);
	}
	
	public void Set3rdHeaderBackColorDelivery(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(240, 230, 247));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
	      headercell.setCellStyle(style61);
	}
	
	
	public void SetNormalCellBackColor(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_LEFT);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorR(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_RIGHT);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorTotal(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(250, 250, 248));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorCSD(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 230));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorLRB(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(247, 255, 247));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColor12(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 192, 197));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorGray(XSSFWorkbook workbook,XSSFCell headercell){
		
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(245, 245, 245));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorBlue(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(246, 253, 253));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorBlueOneDecimal(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(246, 253, 253));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.#"));
			
			headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorPink(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 246, 249));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
	      headercell.setCellStyle(style61);
	}
	public void SetNormalCellBackColorPinkOneDecimal(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 246, 249));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.#"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorPercent(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
			
	      headercell.setCellStyle(style61);
	}

	
	public void SetNormalCellBackColorPercentRed(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 192, 197));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorPercentBlue(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(246, 253, 253));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
			
	      headercell.setCellStyle(style61);
	}

	public void SetNormalCellBackColorPercentPink(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 246, 249));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorLeft(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_LEFT);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorLeftAltGray(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(245, 245, 245));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_LEFT);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
	      headercell.setCellStyle(style61);
	}
	
	
	public void SetNormalCellBackColorLeftTotal(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 203, 173));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_LEFT);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorCenterTotal(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 203, 173));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorRed(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 214, 231));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_LEFT);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
	      headercell.setCellStyle(style61);
	}
	
	
	public void SetNormalCellBackColorRedR(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 214, 231));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_RIGHT);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorRed12(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(192, 0, 0));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetBorder(XSSFWorkbook workbook,XSSFCell headercell){
		XSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style);
	}
	
}
