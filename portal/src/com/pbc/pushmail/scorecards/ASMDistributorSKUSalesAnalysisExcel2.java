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



public class ASMDistributorSKUSalesAnalysisExcel2 {

	public static final String RESULT = Utilities.getEmailAttachmentsPath()+ "/CashInflowSummary.pdf";
	
	
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	
	
	Date StartDate = Utilities.getDateByDays(0); //Utilities.parseDate("13/02/2016");
	Date EndDate = Utilities.getDateByDays(0);//Utilities.parseDate("13/02/2016");
	
	
	
	
////Date StartDate = Utilities.parseDate("01/03/2017");
////Date EndDate = Utilities.parseDate("31/03/2017");
	
	///Date StartDate = Utilities.parseDate("29/08/2016");
	///Date EndDate = Utilities.parseDate("29/08/2016");
	
	
	Date Yesterday = Utilities.getDateByDays(-2);
	
	
	long SND_ID = 0;
	
	public static void main(String[] args)throws Exception 
	   {
		
		
		
		
			   
	   }
	
	public ASMDistributorSKUSalesAnalysisExcel2() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		
		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6){
			Yesterday = Utilities.getDateByDays(-3);
		}
		
	}
	
	
	public void createPdf(String filename, long SND_ID, boolean isMTD) throws  IOException, SQLException {
		
		if (isMTD){
			this.StartDate = Utilities.getStartDateByDate(Utilities.getDateByDays(0));
			this.EndDate = Utilities.getDateByDays(0);
		}
		
		
		//this.StartDate = Utilities.parseDate("01/03/2017");
		//this.EndDate = Utilities.parseDate("31/03/2017");
		
		
		System.out.println("Hello "+Utilities.getDisplayDateFormat(this.StartDate)+" - "+Utilities.getDisplayDateFormat(this.EndDate));
		
		long iDistributorID = 0;
		String iPJPIDs = "0";
		ResultSet irs = s.executeQuery("select group_concat(id), distributor_id from distributor_beat_plan where asm_id="+SND_ID+" group by distributor_id limit 1");
		if (irs.first()){
			iDistributorID = irs.getLong(2);
			iPJPIDs = irs.getString(1);
		}
		
		
		String WhereHOD = " and isap.cache_distributor_id = "+iDistributorID+" ";
		if (SND_ID == 0){
			WhereHOD = "";
		}
		
		
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
			
			String BrandIDs = "1,7,4,10,42,2,14,12,16,23,5"; 
			String PackageIDs = "11,28,6,3,2,29,30,16";
			
			
			//Report Heading
			
			 XSSFRow rowH = spreadsheet.createRow((short) RowCount);	      
		     XSSFCell cellH = (XSSFCell) rowH.createCell((short) 0);
		     
		     cellH.setCellValue("R236 - SKU & Sales Analysis");
			
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
				 
				 cellEx1 = (XSSFCell) rowEx1.createCell((short) 1);	
				 cellEx1.setCellValue("");						
			    
			     
				 
		     
			
		   /////// //////////////////////
		     
				 
		   ///////New modification by Zulqurnan - 07/04/2017 - Orderbooker added
				 int NumberOfColums1=2;
				 
				 ResultSet rs45 = s4.executeQuery("SELECT *,(select display_name from users u where u.id=assigned_to) order_booker_name FROM pep.distributor_beat_plan_users where id in (SELECT id FROM pep.distributor_beat_plan  where  asm_id="+SND_ID+")");
				 while(rs45.next()){
					 NumberOfColums1=2;
				 
					 iPJPIDs = rs45.getString("id");
					 
					 
					 //Orderbooker name
					 

				     RowCount = RowCount+1;
				     RowCount = RowCount+1;
				     RowCount = RowCount+1;
				     RowCount = RowCount+1;
				     
				     //FirstRowCount = FirstRowCount+1;	
						//Printing Date
						
						//Report Heading
						
					 XSSFRow rowEx113 = spreadsheet.createRow((short) RowCount);	      
				     
					 XSSFCell cellEx113 = (XSSFCell) rowEx113.createCell((short) 0);		     
				   
				     
					 cellEx113 = (XSSFCell) rowEx113.createCell((short) 0);	
					 cellEx113.setCellValue(rs45.getLong("assigned_to")+" - "+rs45.getString("order_booker_name"));
					 
					/* spreadsheet.addMergedRegion(new CellRangeAddress(
				    		  FirstRowCount, //first row (0-based)
				    		  FirstRowCount, //last row (0-based)
				    		  0, //first column (0-based)
				    	      4 //last column (0-based)
				    	      ));							
					 */
					 /////////////////////
					 
					 
					 
					 
					 
		     
			      //3rd Row Header //////////////////////
				     ///////////////////////////////////////
				    
				     RowCount = RowCount+1;
				     
				     XSSFRow headerrow1 = spreadsheet.createRow((short) RowCount);        
				     XSSFCell headercell1 = (XSSFCell) headerrow1.createCell((short) 0);
				     
				    
				     headercell1 = (XSSFCell) headerrow1.createCell((short) 0);	     
				     headercell1.setCellValue(" # of Outlets");
				     Set3rdHeaderBackColor(workbook,headercell1);
				     
				     headercell1 = (XSSFCell) headerrow1.createCell((short) 1);	     
				     headercell1.setCellValue("Total");
				     Set3rdHeaderBackColor(workbook,headercell1);
				     
				     
				    
					   ResultSet rs = s.executeQuery("SELECT distinct brand_id , (select label from inventory_brands ib where ib.id=brand_id) brand_label FROM pep.inventory_products where brand_id in("+BrandIDs+")");
					   while(rs.next()){				  
						   headercell1 = (XSSFCell) headerrow1.createCell((short) NumberOfColums1);	     
						   headercell1.setCellValue(rs.getString("brand_label"));
						   Set3rdHeaderBackColor(workbook,headercell1);
						   NumberOfColums1++;
					   }
					   
				     
				     
				     headercell1 = (XSSFCell) headerrow1.createCell((short) NumberOfColums1);	     
				     headercell1.setCellValue("All CSD*");
				     Set3rdHeaderBackColorDelivery(workbook,headercell1);
				     
				     NumberOfColums1=NumberOfColums1+1;
				     
				     headercell1 = (XSSFCell) headerrow1.createCell((short) NumberOfColums1);	     
				     headercell1.setCellValue("All LRB*");
				     Set3rdHeaderBackColorDelivery(workbook,headercell1);
				     
				     
				    ////*** Actual Data ///////
				     
				     
				     
						ResultSet rs1 = s.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM pep.inventory_products_view where package_id in("+PackageIDs+") order by package_sort_order ");
						while(rs1.next()){
							
							
							long PackageID=rs1.getLong("package_id");
							String PackageIDQuery = PackageID +"";
							
							if(PackageID == 11){
								PackageIDQuery = PackageID +",12";
							}
							if(PackageID == 2){
								PackageIDQuery = PackageID +",24";
							}
							
							
						XSSFRow headerrow2 = spreadsheet.createRow((short) RowCount+1);        
					    XSSFCell headercell2 = (XSSFCell) headerrow2.createCell((short) 0);
					
					
					    headercell2 = (XSSFCell) headerrow2.createCell((short) 0);
						headercell2.setCellValue(rs1.getString("package_label").replace("250 ML","250/240").replace("1500 ML", "1500/1750"));
					    SetNormalCellBackColorLeftAltGray(workbook,headercell2);
						
						
					   
					
							//System.out.println("SELECT count(distinct isa.outlet_id) outletcount FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id where isap.cache_package_id="+PackageID+" and isap.cache_brand_id in("+BrandIDs+") and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" "+WhereHOD);
								long OutletTotal = 0;
								ResultSet rs31 = s3.executeQuery("SELECT count(distinct isap.cache_outlet_id) outletcount FROM inventory_sales_adjusted_products isap where isap.cache_package_id in ("+PackageIDQuery+") and isap.cache_brand_id in("+BrandIDs+") and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_pjp_id in ("+iPJPIDs+") "+WhereHOD);
								if(rs31.first()){
									OutletTotal=rs31.getLong("outletcount");
								}
								
								
								//System.out.println("Helo "+OutletTotal);
								
								
								if(OutletTotal!=0){
									 headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
									    headercell2.setCellValue(OutletTotal);
									    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
									    SetNormalCellBackColorTotal(workbook,headercell2);
								}else{
									 headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
									    headercell2.setCellValue("");
									    headercell2.setCellType(Cell.CELL_TYPE_STRING);
									    SetNormalCellBackColorTotal(workbook,headercell2);
								}
								  
								int MainBodyCounter=2;
							
								
								ResultSet rs2 = s2.executeQuery("SELECT distinct brand_id , (select label from inventory_brands ib where ib.id=brand_id) brand_label FROM pep.inventory_products where brand_id in("+BrandIDs+")");
								while(rs2.next()){
									
									long BrandID=rs2.getLong("brand_id");
									
									ResultSet rs3 = s3.executeQuery("SELECT count(distinct cache_outlet_id) outletcount FROM inventory_sales_adjusted_products isap where isap.cache_package_id in ("+PackageIDQuery+") and isap.cache_brand_id="+BrandID+" and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_pjp_id in ("+iPJPIDs+") "+WhereHOD);
									while(rs3.next()){
									
										if(rs3.getLong("outletcount")!=0){
											headercell2 = (XSSFCell) headerrow2.createCell((short) MainBodyCounter);
										    headercell2.setCellValue(rs3.getLong("outletcount"));
										   headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
										    SetNormalCellBackColorTotal(workbook,headercell2);
										}else{
											headercell2 = (XSSFCell) headerrow2.createCell((short) MainBodyCounter);
										    headercell2.setCellValue("");
										    headercell2.setCellType(Cell.CELL_TYPE_STRING);
										    SetNormalCellBackColorTotal(workbook,headercell2);
										}
										
									} 
									MainBodyCounter++;
									
								}
								
								//All CSD
								long AllCSDCount = 0;
								//ResultSet rs4 = s3.executeQuery("SELECT count(distinct outlet_id) outletcount FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isap.cache_package_id="+PackageID+" and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and cache_lrb_type_id=1 and isap.cache_brand_id in ("+BrandIDs+") and isap.cache_brand_id not in(7,10,2,42,12,16,23) "+WhereHOD);
								ResultSet rs4 = s3.executeQuery("select count(distinct cache_outlet_id) outletcount from (SELECT cache_outlet_id, count(distinct cache_brand_id) brands FROM inventory_sales_adjusted_products isap where isap.cache_package_id in ("+PackageIDQuery+") and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_brand_id in (1,4,5) and isap.cache_pjp_id in ("+iPJPIDs+") "+WhereHOD+" group by cache_outlet_id having brands = 3) tab1");
								
									if(rs4.first()){
										AllCSDCount = rs4.getLong("outletcount");
									}
									
									if(AllCSDCount!=0){
										headercell2 = (XSSFCell) headerrow2.createCell((short) MainBodyCounter);
									    headercell2.setCellValue(AllCSDCount);
									   headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
									    SetNormalCellBackColorCSD(workbook,headercell2);
									}else{
										headercell2 = (XSSFCell) headerrow2.createCell((short) MainBodyCounter);
									    headercell2.setCellValue("");
									    headercell2.setCellType(Cell.CELL_TYPE_STRING);
									    SetNormalCellBackColorCSD(workbook,headercell2);
									}
									
									MainBodyCounter=MainBodyCounter+1;	
							
							//All LRB
								long AllLRBCount = 0;
								ResultSet rs5 = s3.executeQuery("select count(distinct cache_outlet_id) outletcount from (SELECT cache_outlet_id, count(distinct cache_brand_id) brands FROM inventory_sales_adjusted_products isap where isap.cache_package_id in ("+PackageIDQuery+") and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_brand_id in (1,4,5,14) and isap.cache_pjp_id in ("+iPJPIDs+") "+WhereHOD+" group by cache_outlet_id having brands = 4) tab1");
									if(rs5.first()){
										AllLRBCount = rs5.getLong("outletcount");
									}
										
	
									if(AllLRBCount!=0){
										headercell2 = (XSSFCell) headerrow2.createCell((short) MainBodyCounter);
									    headercell2.setCellValue(AllLRBCount);
									    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
									    SetNormalCellBackColorLRB(workbook,headercell2);
									}else{
										headercell2 = (XSSFCell) headerrow2.createCell((short) MainBodyCounter);
									    headercell2.setCellValue("");
									    headercell2.setCellType(Cell.CELL_TYPE_STRING);
									    SetNormalCellBackColorLRB(workbook,headercell2);
									}
									
									RowCount = RowCount+1;
						}
						
						
						
						
						
					
						
						
					 
					  // Extra Row
					     
					     
					     RowCount = RowCount+1;
					     FirstRowCount = FirstRowCount+1;	
							//Printing Date
							
							//Report Heading
							
							 XSSFRow rowEx11 = spreadsheet.createRow((short) RowCount);	      
						     
							 XSSFCell cellEx11 = (XSSFCell) rowEx11.createCell((short) 0);		     
						   
						     
							 cellEx11 = (XSSFCell) rowEx11.createCell((short) 0);	
							 cellEx11.setCellValue("");
							 
							 cellEx11 = (XSSFCell) rowEx11.createCell((short) 1);	
							 cellEx11.setCellValue("");						
						    
						     
							 
					     
						
					   /////// //////////////////////
							 
							 
							 
							////////////<!----  Number of Invoices -->
					     
					     
					     if(1==1){
					    	 
					    	 
					    	 RowCount = RowCount+1;
						     
						     XSSFRow headerrow12 = spreadsheet.createRow((short) RowCount);        
						     XSSFCell headercell12 = (XSSFCell) headerrow12.createCell((short) 0);
						     
						    
						     headercell12 = (XSSFCell) headerrow12.createCell((short) 0);	     
						     headercell12.setCellValue(" # of Invoices");
						     Set3rdHeaderBackColor(workbook,headercell12);
						     
						     headercell12 = (XSSFCell) headerrow12.createCell((short) 1);	     
						     headercell12.setCellValue("Total");
						     Set3rdHeaderBackColor(workbook,headercell12);
						     
						     
						     int NumberOfColums12=2;
						     ResultSet Irs = s.executeQuery("SELECT distinct brand_id , (select label from inventory_brands ib where ib.id=brand_id) brand_label FROM pep.inventory_products where brand_id in("+BrandIDs+")");
							   while(Irs.next()){			  
								   headercell12 = (XSSFCell) headerrow12.createCell((short) NumberOfColums12);	     
								   headercell12.setCellValue(Irs.getString("brand_label"));
								   Set3rdHeaderBackColor(workbook,headercell12);
								   NumberOfColums12++;
							   }
							   
						     
						     
						     headercell12 = (XSSFCell) headerrow12.createCell((short) NumberOfColums12);	     
						     headercell12.setCellValue("All CSD*");
						     Set3rdHeaderBackColorDelivery(workbook,headercell12);
						     
						     NumberOfColums12=NumberOfColums12+1;
						     
						     headercell12 = (XSSFCell) headerrow12.createCell((short) NumberOfColums12);	     
						     headercell12.setCellValue("All LRB*");
						     Set3rdHeaderBackColorDelivery(workbook,headercell12);
					    	 
					    	 
					    	 
						    
								ResultSet rs12 = s.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM pep.inventory_products_view where package_id in("+PackageIDs+") order by package_sort_order");
								while(rs12.next()){
									
									
									long PackageID=rs12.getLong("package_id");
									String PackageIDQuery = PackageID +"";
									
									if(PackageID == 11){
										PackageIDQuery = PackageID +",12";
									}
									if(PackageID == 2){
										PackageIDQuery = PackageID +",24";
									}
	
									
									XSSFRow headerrow2 = spreadsheet.createRow((short) RowCount+1);        
								    XSSFCell headercell2 = (XSSFCell) headerrow2.createCell((short) 0);
								
								
								    headercell2 = (XSSFCell) headerrow2.createCell((short) 0);
									headercell2.setCellValue(rs12.getString("package_label").replace("250 ML","250/240").replace("1500 ML", "1500/1750"));
								    SetNormalCellBackColorLeftAltGray(workbook,headercell2);
									
								
								   /////// System.out.println("SELECT count(distinct isap.id) totalinvoices FROM  inventory_sales_adjusted_products isap  where isap.cache_package_id="+rs12.getLong("package_id")+" and isap.cache_brand_id in("+BrandIDs+") and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" "+WhereHOD);
								    
										
										double TotalInvoiceCount= 0;
										ResultSet rs31 = s3.executeQuery("SELECT count(distinct isap.id) totalinvoices FROM  inventory_sales_adjusted_products isap force index (inventory_sales_adjusted_products_pack_brand_date_pjp_dist) where isap.cache_package_id in ("+PackageIDQuery+") and isap.cache_brand_id in("+BrandIDs+") and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_pjp_id in ("+iPJPIDs+") "+WhereHOD);
										if(rs31.first()){
											TotalInvoiceCount = rs31.getLong("totalinvoices");
										}
										
										/////System.out.println("he "+TotalInvoiceCount);
										
										if(TotalInvoiceCount!=0){
											 headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
											    headercell2.setCellValue(TotalInvoiceCount);
											    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
											    SetNormalCellBackColorTotal(workbook,headercell2);
										}else{
											 headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
											    headercell2.setCellValue("");
											    headercell2.setCellType(Cell.CELL_TYPE_STRING);
											    SetNormalCellBackColorTotal(workbook,headercell2);
										}
										
										
										int MainBodyCounter2=2;
										
										ResultSet rs2 = s2.executeQuery("SELECT distinct brand_id , (select label from inventory_brands ib where ib.id=brand_id) brand_label FROM pep.inventory_products where brand_id in("+BrandIDs+")");
										while(rs2.next()){
											
											long BrandID=rs2.getLong("brand_id");
											
											ResultSet rs3 = s3.executeQuery("SELECT count(distinct isap.id) outletcount FROM inventory_sales_adjusted_products isap where isap.cache_package_id in ("+PackageIDQuery+") and isap.cache_brand_id="+BrandID+" and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_pjp_id in ("+iPJPIDs+") "+WhereHOD);
											while(rs3.next()){
												
												
												if(rs3.getLong("outletcount")!=0){
													headercell2 = (XSSFCell) headerrow2.createCell((short) MainBodyCounter2);
												    headercell2.setCellValue(rs3.getLong("outletcount"));
												   headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
												    SetNormalCellBackColorTotal(workbook,headercell2);
												}else{
													headercell2 = (XSSFCell) headerrow2.createCell((short) MainBodyCounter2);
												    headercell2.setCellValue("");
												    headercell2.setCellType(Cell.CELL_TYPE_STRING);
												    SetNormalCellBackColorTotal(workbook,headercell2);
												}
											
													
											
											}
											MainBodyCounter2++;
										}
										
										//All CSD
										long CSDInvoiceCount = 0;
										ResultSet rs4 = s3.executeQuery("select count(distinct id) invoicecsd from (SELECT isap.id, count(distinct cache_brand_id) brands FROM inventory_sales_adjusted_products isap where isap.cache_package_id in ("+PackageIDQuery+") and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_brand_id in (1,4,5) and isap.cache_pjp_id in ("+iPJPIDs+") "+WhereHOD+" group by isap.id having brands = 3) tab1");
											if(rs4.first()){
												CSDInvoiceCount = rs4.getLong("invoicecsd");
											}
												
											
											
											if(CSDInvoiceCount!=0){
												headercell2 = (XSSFCell) headerrow2.createCell((short) MainBodyCounter2);
											    headercell2.setCellValue(CSDInvoiceCount);
											   headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
											    SetNormalCellBackColorCSD(workbook,headercell2);
											}else{
												headercell2 = (XSSFCell) headerrow2.createCell((short) MainBodyCounter2);
											    headercell2.setCellValue("");
											    headercell2.setCellType(Cell.CELL_TYPE_STRING);
											    SetNormalCellBackColorCSD(workbook,headercell2);
											}
											
											MainBodyCounter2=MainBodyCounter2+1;	
											
											
											
												
										
										
										
									//All LRB
										long LRBInvoiceCount = 0;
										ResultSet rs5 = s3.executeQuery("select count(distinct id) invoicelrb from (SELECT isap.id, count(distinct cache_brand_id) brands FROM inventory_sales_adjusted_products isap where isap.cache_package_id in ("+PackageIDQuery+") and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_brand_id in (1,4,5,14) and isap.cache_pjp_id in ("+iPJPIDs+") "+WhereHOD+" group by isap.id having brands = 4) tab1");
											if(rs5.first()){
												LRBInvoiceCount = rs5.getLong("invoicelrb");
											}
												
											
											
	
											if(LRBInvoiceCount!=0){
												headercell2 = (XSSFCell) headerrow2.createCell((short) MainBodyCounter2);
											    headercell2.setCellValue(LRBInvoiceCount);
											    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
											    SetNormalCellBackColorLRB(workbook,headercell2);
											}else{
												headercell2 = (XSSFCell) headerrow2.createCell((short) MainBodyCounter2);
											    headercell2.setCellValue("");
											    headercell2.setCellType(Cell.CELL_TYPE_STRING);
											    SetNormalCellBackColorLRB(workbook,headercell2);
											}
											
											RowCount = RowCount+1;
											
	
								
								}
									  
							 
					     }
					     
					     
					     
					     // Extra Row
					     
					     
					     RowCount = RowCount+1;
					     FirstRowCount = FirstRowCount+1;	
							//Printing Date
							
							//Report Heading
							
							 XSSFRow rowEx112 = spreadsheet.createRow((short) RowCount);	      
						     
							 XSSFCell cellEx112 = (XSSFCell) rowEx112.createCell((short) 0);		     
						   
						     
							 cellEx112 = (XSSFCell) rowEx112.createCell((short) 0);	
							 cellEx112.setCellValue("");
							 
							 cellEx112 = (XSSFCell) rowEx112.createCell((short) 1);	
							 cellEx112.setCellValue("");						
						    
						     
							 
					     
						
					   /////// //////////////////////
							 
							 
						///////////////////////////////////	 <!-- Volume -->///////////////
						///////////////////////////////////////////////////////////////////	 
					     
					     
					     
							 RowCount = RowCount+1;
						     
						     XSSFRow headerrow12 = spreadsheet.createRow((short) RowCount);        
						     XSSFCell headercell12 = (XSSFCell) headerrow12.createCell((short) 0);
						     
						    
						     headercell12 = (XSSFCell) headerrow12.createCell((short) 0);	     
						     headercell12.setCellValue(" Volume(r/c)");
						     Set3rdHeaderBackColor(workbook,headercell12);
						     
						     headercell12 = (XSSFCell) headerrow12.createCell((short) 1);	     
						     headercell12.setCellValue("Total");
						     Set3rdHeaderBackColor(workbook,headercell12);
						     
						     
						     int NumberOfColums12=2;
						     ResultSet Vrs = s.executeQuery("SELECT distinct brand_id , (select label from inventory_brands ib where ib.id=brand_id) brand_label FROM pep.inventory_products where brand_id in("+BrandIDs+")");
							   while(Vrs.next()){		  
								   headercell12 = (XSSFCell) headerrow12.createCell((short) NumberOfColums12);	     
								   headercell12.setCellValue(Vrs.getString("brand_label"));
								   Set3rdHeaderBackColor(workbook,headercell12);
								   NumberOfColums12++;
							   }
							   
						     
						     
						     headercell12 = (XSSFCell) headerrow12.createCell((short) NumberOfColums12);	     
						     headercell12.setCellValue("All CSD*");
						     Set3rdHeaderBackColorDelivery(workbook,headercell12);
						     
						     NumberOfColums12=NumberOfColums12+1;
						     
						     headercell12 = (XSSFCell) headerrow12.createCell((short) NumberOfColums12);	     
						     headercell12.setCellValue("All LRB*");
						     Set3rdHeaderBackColorDelivery(workbook,headercell12);
							 
							 
						     
						     //////Actual Data ////
						     
						     
						     
						    
							
						   
								ResultSet Vrs1 = s.executeQuery("SELECT distinct package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM pep.inventory_products_view where package_id in("+PackageIDs+") order by package_sort_order");
								while(Vrs1.next()){
								
									
									long PackageID=Vrs1.getLong("package_id");
									String PackageIDQuery = PackageID +"";
									
									if(PackageID == 11){
										PackageIDQuery = PackageID +",12";
									}
									if(PackageID == 2){
										PackageIDQuery = PackageID +",24";
									}
									
									
									XSSFRow headerrow22 = spreadsheet.createRow((short) RowCount+1);        
								    XSSFCell headercell22 = (XSSFCell) headerrow22.createCell((short) 0);
									
									
									 
									headercell22.setCellValue(Vrs1.getString("package_label").replace("250 ML","250/240").replace("1500 ML", "1500/1750"));
									SetNormalCellBackColorLeftAltGray(workbook,headercell22);	
									
									
										
										
										//System.out.println("SELECT sum(total_units/cache_units_per_sku) totalvol FROM  inventory_sales_adjusted_products isap  where isap.cache_package_id="+Vrs1.getLong("package_id")+" and isap.cache_brand_id in("+BrandIDs+") and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" "+WhereHOD);
										
										
										
										
										double TotalRawCases = 0;
										ResultSet rs31 = s3.executeQuery("SELECT sum(total_units/cache_units_per_sku) totalvol FROM inventory_sales_adjusted_products isap where isap.cache_package_id in ("+PackageIDQuery+") and isap.cache_brand_id in("+BrandIDs+") and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_pjp_id in ("+iPJPIDs+") "+WhereHOD);
										if(rs31.first()){
											TotalRawCases = rs31.getDouble("totalvol");
										}
										
										
										System.out.println("VVV "+TotalRawCases);
										
										if(TotalRawCases!=0){
											 headercell22 = (XSSFCell) headerrow22.createCell((short) 1);
											    headercell22.setCellValue(TotalRawCases);
											    headercell22.setCellType(Cell.CELL_TYPE_NUMERIC);
											    SetNormalCellBackColorTotal(workbook,headercell22);
										}else{
											 headercell22 = (XSSFCell) headerrow22.createCell((short) 1);
											    headercell22.setCellValue("");
											    headercell22.setCellType(Cell.CELL_TYPE_STRING);
											    SetNormalCellBackColorTotal(workbook,headercell22);
										}
										
										int MainBodyCounter3=2;
										
										ResultSet Vrs2 = s2.executeQuery("SELECT distinct brand_id , (select label from inventory_brands ib where ib.id=brand_id) brand_label FROM pep.inventory_products where brand_id in("+BrandIDs+")");
										while(Vrs2.next()){
											
											ResultSet rs3 = s3.executeQuery("SELECT sum(total_units/cache_units_per_sku) vol FROM inventory_sales_adjusted_products isap where isap.cache_package_id in ("+PackageIDQuery+") and isap.cache_brand_id="+Vrs2.getLong("brand_id")+" and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_pjp_id in ("+iPJPIDs+") "+WhereHOD);
											while(rs3.next()){
											
												if(rs3.getDouble("vol")!=0){
													headercell22 = (XSSFCell) headerrow22.createCell((short) MainBodyCounter3);
												    headercell22.setCellValue(rs3.getDouble("vol"));
												   headercell22.setCellType(Cell.CELL_TYPE_NUMERIC);
												    SetNormalCellBackColorTotal(workbook,headercell22);
												}else{
													headercell22 = (XSSFCell) headerrow22.createCell((short) MainBodyCounter3);
												    headercell22.setCellValue("");
												    headercell22.setCellType(Cell.CELL_TYPE_STRING);
												    SetNormalCellBackColorTotal(workbook,headercell22);
												}
												
											}
											MainBodyCounter3++;
									
										}
										
										//All CSD
										double CSDRawCases = 0;
										ResultSet rs4 = s3.executeQuery("SELECT sum(total_units/cache_units_per_sku) volcsd FROM inventory_sales_adjusted_products isap where isap.cache_package_id in ("+PackageIDQuery+") and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_brand_id in (1,4,5) and isap.cache_pjp_id in ("+iPJPIDs+") "+WhereHOD);
											if(rs4.first()){
												CSDRawCases = rs4.getDouble("volcsd");
											}
												
											
											if(CSDRawCases!=0){
												headercell22 = (XSSFCell) headerrow22.createCell((short) MainBodyCounter3);
											    headercell22.setCellValue(CSDRawCases);
											   headercell22.setCellType(Cell.CELL_TYPE_NUMERIC);
											    SetNormalCellBackColorCSD(workbook,headercell22);
											}else{
												headercell22 = (XSSFCell) headerrow22.createCell((short) MainBodyCounter3);
											    headercell22.setCellValue("");
											    headercell22.setCellType(Cell.CELL_TYPE_STRING);
											    SetNormalCellBackColorCSD(workbook,headercell22);
											}
											
											
											MainBodyCounter3=MainBodyCounter3+1;
											
									//All LRB
										double LRBRawCases = 0;
										ResultSet rs5 = s3.executeQuery("SELECT sum(total_units/cache_units_per_sku) vollrb FROM inventory_sales_adjusted_products isap where isap.cache_package_id in ("+PackageIDQuery+") and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isap.cache_brand_id in (1,4,5,14) and isap.cache_pjp_id in ("+iPJPIDs+") "+WhereHOD);
											if(rs5.first()){
												LRBRawCases = rs5.getDouble("vollrb");
											}
												
											
											if(LRBRawCases!=0){
												headercell22 = (XSSFCell) headerrow22.createCell((short) MainBodyCounter3);
											    headercell22.setCellValue(LRBRawCases);
											    headercell22.setCellType(Cell.CELL_TYPE_NUMERIC);
											    SetNormalCellBackColorLRB(workbook,headercell22);
											}else{
												headercell22 = (XSSFCell) headerrow22.createCell((short) MainBodyCounter3);
											    headercell22.setCellValue("");
											    headercell22.setCellType(Cell.CELL_TYPE_STRING);
											    SetNormalCellBackColorLRB(workbook,headercell22);
											}
	
											RowCount = RowCount+1;
								
								}
							
				 }//end of orderbooker while
							
							//Generated On
						    
						    RowCount = RowCount+1;
							
							//Printing Date
							
							//Report Heading
							
							 XSSFRow rowPG = spreadsheet.createRow((short) RowCount+1);	      
						     
							 XSSFCell cellPG = (XSSFCell) rowPG.createCell((short) 0);		     
						   
						     
						     cellPG.setCellValue("Generated On: "+Utilities.getDisplayDateTimeFormat(new Date()));
								
						     spreadsheet.addMergedRegion(new CellRangeAddress(
						    		 RowCount+1, //first row (0-based)
						    		 RowCount+1, //last row (0-based)
						    		  0, //first column (0-based)
						    	      5 //last column (0-based)
						    	      ));
					
			
			
			//Auto Sizing Column
		    
				  
				    
		    for(int i=0;i<NumberOfColums1;i++){
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
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
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
			style61.setAlignment(CellStyle.ALIGN_CENTER);
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
			style61.setAlignment(CellStyle.ALIGN_CENTER);
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
