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

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
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



public class DistributorStockAnalysisExcel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath()+ "/CashInflowSummary.pdf";
	
	XSSFCellStyle style61;
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	Statement s5;
	Statement s6;
	Statement s7;
	
	
	Date StartDate = Utilities.getDateByDays(0); //Utilities.parseDate("13/02/2016");
	Date EndDate = Utilities.getDateByDays(0);//Utilities.parseDate("13/02/2016");
	
	
	///////Date StartDate = Utilities.parseDate("06/09/2016");
	//////Date EndDate = Utilities.parseDate("06/09/2016");
	
	
	Date Yesterday = Utilities.getDateByDays(-2);
	
	
	long SND_ID = 0;
	
	public static void main(String[] args)throws Exception 
	   {
		
		
		
		
			   
	   }
	
	
	public void setSND(long SND_ID){
		this.SND_ID = SND_ID;
	}
	
	public DistributorStockAnalysisExcel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		
		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
		s5 = ds.createStatement();
		s6 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6){
			Yesterday = Utilities.getDateByDays(-3);
		}
		
	}
	
	
	public void createPdf(String filename, long SND_ID) throws  IOException, SQLException {
		
		s.executeUpdate("DROP TABLE IF EXISTS dist_stock_temp ");
		s.executeUpdate("CREATE temporary TABLE dist_stock_temp (pack_id int , brand_id int , to_be_lifted decimal(11,2))");
		
		
		
	//	System.out.println(this.SND_ID);
		
		String WhereSND="";
		
		if(this.SND_ID!=0){
			WhereSND = " and (snd_id ="+this.SND_ID+" or rsm_id ="+this.SND_ID+" or tdm_id ="+this.SND_ID+") ";
		}
		
			XSSFWorkbook workbook = new XSSFWorkbook(); 
	      XSSFSheet spreadsheet = workbook.createSheet("cell types");
	      
	      
	      // Style
	      
			XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));
			style61 = workbook.createCellStyle();
			style61.setFillBackgroundColor(HSSFColor.BLUE.index);
			style61.setFillForegroundColor(BackColor2ndHeader);
			style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

	      
	      // End Style
	      
	      int month = Utilities.getMonthNumberByDate(EndDate);
			int year = Utilities.getYearByDate(EndDate);
			Date StartDateLastMonth = Utilities.getStartDateByMonth(month-2, year);
			Date EndDateLastMonth = Utilities.getEndDateByMonth(month-2, year);
			
			int DaysInLastMonth = Utilities.getDayNumberByDate(EndDateLastMonth);
			
			long TotalBrandCount=0;
			
			double GrandTotalBalance = 0;
			double GrandTotalSecondarySales = 0;
			double GrandTotalLiftingTarget = 0;
			
			int FirstRowCount=1;
			
			int RowCount=1;
			
			
			//Printing Date
			
			//Report Heading
			
			 XSSFRow rowP = spreadsheet.createRow((short) RowCount);	      
		     
			 XSSFCell cellP = (XSSFCell) rowP.createCell((short) 1);		     
		     cellP.setCellValue("");
		     
		     cellP = (XSSFCell) rowP.createCell((short) 2);		     
		     cellP.setCellValue("");
		     
		     cellP = (XSSFCell) rowP.createCell((short) 3);		     
		     cellP.setCellValue("");
		     
		     cellP = (XSSFCell) rowP.createCell((short) 4);		     
		     cellP.setCellValue("");
		     
		     cellP = (XSSFCell) rowP.createCell((short) 5);		     
		     cellP.setCellValue("");
			
		     cellP = (XSSFCell) rowP.createCell((short) 6);		     
		     cellP.setCellValue("");
			 
			 
			 cellP = (XSSFCell) rowP.createCell((short) 7);
			 cellP = (XSSFCell) rowP.createCell((short) 8);
		     
		     cellP.setCellValue("Period: "+Utilities.getDisplayDateFormat(StartDate)+" - "+Utilities.getDisplayDateFormat(EndDate));
			
		     spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  FirstRowCount, //first row (0-based)
		    		  FirstRowCount, //last row (0-based)
		    		  8, //first column (0-based)
		    	      9 //last column (0-based)
		    	      ));
		    
		     XSSFCellStyle style611 = workbook.createCellStyle();		      
		     style611.setAlignment(CellStyle.ALIGN_RIGHT);
		     cellP.setCellStyle(style611);
		      
		     
		     
			RowCount = RowCount+1;
			FirstRowCount = FirstRowCount+1;
			
			
			//Report Heading
			
			 XSSFRow rowH = spreadsheet.createRow((short) RowCount);	      
		     XSSFCell cellH = (XSSFCell) rowH.createCell((short) 1);
		     
		     cellH.setCellValue("Distributor Stock Analysis");
			
		     spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  FirstRowCount, //first row (0-based)
		    		  FirstRowCount, //last row (0-based)
		    		  1, //first column (0-based)
		    	      9 //last column (0-based)
		    	      ));
		     
		    // CellUtil.setAlignment(cellH, workbook, CellStyle.ALIGN_CENTER);
		      
		      XSSFColor BackColorDistributor1 = new XSSFColor(new java.awt.Color(0, 0, 255));
		      
		      XSSFCellStyle style61 = workbook.createCellStyle();
		      style61.setFillBackgroundColor(
		      HSSFColor.BLUE.index );
		      style61.setFillForegroundColor(BackColorDistributor1);
		      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
		      style61.setAlignment(CellStyle.ALIGN_CENTER);
		      cellH.setCellStyle(style61);
		     
		            
		      XSSFFont font1 = workbook.createFont();
		      font1.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		      font1.setFontHeightInPoints((short)10);
		      font1.setColor(IndexedColors.WHITE.getIndex());
		      style61.setFont(font1);
		     
		     
		      
			RowCount = RowCount+1;
			FirstRowCount = FirstRowCount+1;
			
			String DistributorIDs="";
			
			///////////////////ResultSet rs = s.executeQuery("SELECT *  FROM common_distributors cd where distributor_id in(100914,100922)"+WhereSND);
			
			String SelectedProductIDs = "";
			ResultSet rsx1 = s.executeQuery("select group_concat(distinct product_id) from inventory_sales_adjusted_products where cache_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(-60))+" and curdate()");
			if(rsx1.first()){
				SelectedProductIDs = rsx1.getString(1);
			}			
			
			ResultSet rs = s.executeQuery("SELECT *  FROM common_distributors cd where is_shifted_to_other_plant = 0 and distributor_id not in (200769,201001) and distributor_id in(select distributor_id from mobile_order where created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(-30))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(0))+") "+WhereSND);
			while(rs.next()){
				
				if(rs.getLong("distributor_id")!=100914) {
				
				
				
				double MinToLiftedGrandTotal=0;
	      
	      XSSFRow row = spreadsheet.createRow((short) RowCount);	      
	      XSSFCell cell = (XSSFCell) row.createCell((short) 1);
	     
	      cell.setCellValue(rs.getLong("distributor_id")+" - "+rs.getString("name"));
	      
	      
	      	      
	      spreadsheet.addMergedRegion(new CellRangeAddress(
	    		  FirstRowCount, //first row (0-based)
	    		  FirstRowCount, //last row (0-based)
	    		  1, //first column (0-based)
	    	      9 //last column (0-based)
	    	      ));
	      
	      XSSFColor BackColorDistributor = new XSSFColor(new java.awt.Color(64, 64, 64));
	      
	      XSSFCellStyle style6 = workbook.createCellStyle();
	      style6.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style6.setFillForegroundColor(BackColorDistributor);
	      style6.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      cell.setCellStyle(style6);
	     
	            
	      XSSFFont font = workbook.createFont();
	      font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
	      font.setFontHeightInPoints((short)10);
	      font.setColor(IndexedColors.WHITE.getIndex());
	      style6.setFont(font);
	      
	      
	      //2nd row header
	      
	     
	      
	      RowCount = RowCount+1;
	      
	      XSSFRow headerrow = spreadsheet.createRow((short) RowCount);	      
	      XSSFCell headercell = (XSSFCell) headerrow.createCell((short) 1);
	     
	      headercell.setCellValue("");
	      Set2ndHeaderBackColor(workbook,headercell);
	      spreadsheet.addMergedRegion(new CellRangeAddress(
	    		  FirstRowCount+1, //first row (0-based)
	    		  FirstRowCount+1, //last row (0-based)
	    		  1, //first column (0-based)
	    	      3 //last column (0-based)
	    	      ));
	      
	     
	          
	      headercell = (XSSFCell) headerrow.createCell((short) 4);	     
	      headercell.setCellValue("Current Stock");
	      Set2ndHeaderBackColor(workbook,headercell);
	     
	      
	      headercell = (XSSFCell) headerrow.createCell((short) 5);	     
	      headercell.setCellValue("Average Daily Sale");
	      Set2ndHeaderBackColor(workbook,headercell);
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		  5, //first column (0-based)
	    	      6 //last column (0-based)
	    	      ));
	    
	     
	     headercell = (XSSFCell) headerrow.createCell((short) 7);	     
	      headercell.setCellValue("Stock Days");
	      Set2ndHeaderBackColor(workbook,headercell);
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		  7, //first column (0-based)
	    	      8 //last column (0-based)
	    	      ));
	      
	     headercell = (XSSFCell) headerrow.createCell((short) 9);	     
	      headercell.setCellValue("");
	      Set2ndHeaderBackColor(workbook,headercell);
	      
	      
	     //3rd Row Header //////////////////////
	     ///////////////////////////////////////
	    
	     RowCount = RowCount+1;
	     
	     XSSFRow headerrow1 = spreadsheet.createRow((short) RowCount);        
	     XSSFCell headercell1 = (XSSFCell) headerrow1.createCell((short) 1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 1);	     
	     headercell1.setCellValue("Distributor");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 2);	     
	     headercell1.setCellValue("Packages");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 3);	     
	     headercell1.setCellValue("Brands");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 4);	     
	     headercell1.setCellValue("R/C");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 5);	     
	     headercell1.setCellValue("Last Month Actual");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 6);	     
	     headercell1.setCellValue("Current Month Target");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 7);	     
	     headercell1.setCellValue("Last Month Actual");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 8);	     
	     headercell1.setCellValue("Current Month Target");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 9);	     
	     headercell1.setCellValue("Minimum to be lifted");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     //* Actaul Data , Rows and Cols */
	     /////////////////////////////////////////////////////////////////
	     
	     
	     
	     	     
	     
	     
	     
	     long BrandCountForDistributor=0;
			int i=0;
			long BrandCount=1;
			int DistCount=0;
			long DistributorID = rs.getLong("distributor_id");
			
			double DistributorTotalBalance = 0;
			double DistributorTotalSecondarySales = 0;
			double DistributorTotalLiftingTarget = 0;
			
			int RowCounterInner=1;
			
			
			
			ResultSet rs3 = s3.executeQuery("SELECT distinct ipv.package_id, ipv.package_label FROM pep.inventory_products_view ipv where ipv.category_id = 1 and ipv.is_visible = 1 and product_id in ("+SelectedProductIDs+") and ipv.package_id not in (13) order by ipv.package_id limit 5");
			while(rs3.next()){
				int PackageID = rs3.getInt("package_id");
				double MinToLiftedTotal=0;
				double PackageTotalBalance = 0;
				double PackageTotalSecondarySales = 0;
				
				ResultSet rs1 = s2.executeQuery("SELECT ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.product_id, ipv.unit_per_sku, (SELECT sum(total_units_received-total_units_issued) FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id = "+DistributorID+" and idsp.product_id = ipv.product_id) balance, (select sum(isap.total_units) from inventory_sales_adjusted_products isap where isap.cache_distributor_id = "+DistributorID+" and isap.product_id = ipv.product_id and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDate(EndDateLastMonth)+") total_secondary_sales FROM pep.inventory_products_view ipv where ipv.category_id = 1 and ipv.is_visible = 1 and ipv.package_id = "+PackageID+" order by ipv.package_id");
				while(rs1.next()){
					
					//System.out.println("Old Row Count "+RowCount);
					
					XSSFRow headerrow2 = spreadsheet.createRow((short) RowCount+1);        
				    XSSFCell headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
				
					int BrandID = rs1.getInt("brand_id");
					int ProductID = rs1.getInt("product_id");
					double UnitPerSKU = rs1.getDouble("unit_per_sku");
					double balance = rs1.getDouble("balance");
					if (balance < 0){
						balance = 0;
					}
					double TotalSecondarySales = rs1.getDouble("total_secondary_sales") / UnitPerSKU;
					
					double BalanceRawCases = balance / UnitPerSKU;
					
					double AverageSecondaySales = TotalSecondarySales / DaysInLastMonth;
					
					double StockDaysLastMonthActual = 0;
					if (AverageSecondaySales != 0){
						StockDaysLastMonthActual = BalanceRawCases / AverageSecondaySales;
					}
					
					PackageTotalBalance += BalanceRawCases;
					DistributorTotalBalance += BalanceRawCases; 
					GrandTotalBalance += BalanceRawCases;
					
					PackageTotalSecondarySales += TotalSecondarySales;
					DistributorTotalSecondarySales += TotalSecondarySales;
					GrandTotalSecondarySales += TotalSecondarySales;
					
					
					//Daily Current Month Target ////////////////////////
					////////////////////////////////////////////////////
					
					double TargetLifting = 0;
					ResultSet rs11 = s4.executeQuery("SELECT dtpb.quantity FROM pep.distributor_targets dt join distributor_targets_packages_brands dtpb on dt.id = dtpb.id where month = "+month+" and year = "+year+" and distributor_id = "+DistributorID+" and package_id ="+PackageID+" and brand_id="+BrandID);
					if (rs11.first()){
						TargetLifting = rs11.getDouble(1);
					}
					DistributorTotalLiftingTarget += TargetLifting;
					GrandTotalLiftingTarget +=TargetLifting;
							
					double DailyTargetLifting = TargetLifting / Utilities.getDayNumberByDate(Utilities.getEndDateByDate(EndDate));
					
					double StockDaysCurrentMonthTarget = 0;
					if (DailyTargetLifting != 0){
						StockDaysCurrentMonthTarget = BalanceRawCases / DailyTargetLifting;
					}
					
					////////////////////////////////////////////////////
					///////////////////////////////////////////////////
					
					headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
					headercell2.setCellValue(rs.getLong("distributor_id")+" - "+rs.getString("name"));
				    SetNormalCellBackColorLeft(workbook,headercell2);
						
					headercell2 = (XSSFCell) headerrow2.createCell((short) 2);
					headercell2.setCellValue(rs1.getString("package_label"));
				    SetNormalCellBackColorLeft(workbook,headercell2);	
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 3);
				    headercell2.setCellValue(rs1.getString("brand_label"));
				    SetNormalCellBackColorLeft(workbook,headercell2);
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 4);				   
				    headercell2.setCellValue(Math.round(BalanceRawCases));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    
				    SetNormalCellBackColor(workbook,headercell2);
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 5);
				    headercell2.setCellValue(Math.round(AverageSecondaySales));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColor(workbook,headercell2);
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 6);
				    headercell2.setCellValue(Math.round(DailyTargetLifting));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColor(workbook,headercell2);
				    
				    if(StockDaysLastMonthActual<=3){
				    	headercell2 = (XSSFCell) headerrow2.createCell((short) 7);
				    	headercell2.setCellValue(Math.round(StockDaysLastMonthActual));
				    	  headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    	SetNormalCellBackColorRed(workbook,headercell2);
				    }else{
				    	headercell2 = (XSSFCell) headerrow2.createCell((short) 7);
				    	headercell2.setCellValue(Math.round(StockDaysLastMonthActual));
				    	  headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    	SetNormalCellBackColor(workbook,headercell2);
				    }
				    
				   
				    if(StockDaysCurrentMonthTarget<7){
				    	headercell2 = (XSSFCell) headerrow2.createCell((short) 8);
					    headercell2.setCellValue(Math.round(StockDaysCurrentMonthTarget));
					    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					    SetNormalCellBackColorRed(workbook,headercell2);
				    }else{
				    	 headercell2 = (XSSFCell) headerrow2.createCell((short) 8);
						    headercell2.setCellValue(Math.round(StockDaysCurrentMonthTarget));
						    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
						    SetNormalCellBackColor(workbook,headercell2);
				    }
				    
				   
					
				    double MinToLifted=0;
				    if(StockDaysCurrentMonthTarget<7){
				    	MinToLifted = (7-StockDaysCurrentMonthTarget)*DailyTargetLifting;
				    }
				    
				    
				    
				   
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 9);
				    headercell2.setCellValue(Math.round(MinToLifted));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColor(workbook,headercell2);
				    
				    //Inserting in Temp Table
				    
				    s6.executeUpdate("insert into dist_stock_temp(pack_id,brand_id,to_be_lifted) values("+PackageID+","+BrandID+","+MinToLifted+")");
				    
				    
				    MinToLiftedTotal+=MinToLifted;
				    
				    RowCount++;
					}
				
				if (0 == 0){
					
					double TargetLifting = 0;
					ResultSet rs11 = s2.executeQuery("SELECT dtp.quantity FROM pep.distributor_targets dt join distributor_targets_packages dtp on dt.id = dtp.id where month = "+month+" and year = "+year+" and distributor_id = "+DistributorID+" and package_id ="+PackageID);
					if (rs11.first()){
						TargetLifting = rs11.getDouble(1);
					}
					//DistributorTotalLiftingTarget += TargetLifting;
					//GrandTotalLiftingTarget +=TargetLifting;
							
					double DailyTargetLifting = Math.round(TargetLifting / Utilities.getDayNumberByDate(Utilities.getEndDateByDate(EndDate)));
					
					double PackageAverageSecondaySales = PackageTotalSecondarySales / DaysInLastMonth;
					
					double PackageStockDaysLastMonthActual = 0;
					if (PackageAverageSecondaySales != 0){
						PackageStockDaysLastMonthActual = PackageTotalBalance / PackageAverageSecondaySales;
					}
					
					
					double StockDaysCurrentMonthTarget = 0;
					if (DailyTargetLifting != 0){
						StockDaysCurrentMonthTarget = PackageTotalBalance / DailyTargetLifting;
					}
					 RowCount = RowCount+1;
					XSSFRow headerrow3 = spreadsheet.createRow((short) RowCount);        
				    XSSFCell headercell3 = (XSSFCell) headerrow3.createCell((short) 1);
				     
				    
				   
				    
				    headercell3 = (XSSFCell) headerrow3.createCell((short) 1);
				    headercell3.setCellValue( rs.getLong("distributor_id")+" - "+rs.getString("name"));
				    SetNormalCellBackColorLeftTotal(workbook,headercell3);
				    
				    headercell3 = (XSSFCell) headerrow3.createCell((short) 2);
				    headercell3.setCellValue(rs3.getString("package_label"));
				    SetNormalCellBackColorLeftTotal(workbook,headercell3);
				    
				    headercell3 = (XSSFCell) headerrow3.createCell((short) 3);
				    headercell3.setCellValue("Total");
				    SetNormalCellBackColorLeftTotal(workbook,headercell3);
				    
				    headercell3 = (XSSFCell) headerrow3.createCell((short) 4);
				    headercell3.setCellValue(Math.round(PackageTotalBalance));
				    headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColorCenterTotal(workbook,headercell3);
				    
				    headercell3 = (XSSFCell) headerrow3.createCell((short) 5);
				    headercell3.setCellValue(Math.round(PackageAverageSecondaySales));
				    headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColorCenterTotal(workbook,headercell3);
				    
				    headercell3 = (XSSFCell) headerrow3.createCell((short) 6);
				    headercell3.setCellValue(Math.round(DailyTargetLifting));
				    headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColorCenterTotal(workbook,headercell3);
				    
				    if(PackageStockDaysLastMonthActual<=3){
					    headercell3 = (XSSFCell) headerrow3.createCell((short) 7);
					    headercell3.setCellValue(Math.round(PackageStockDaysLastMonthActual));
					    headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
					    SetNormalCellBackColorCenterTotal(workbook,headercell3);
				    }else{
				    	 headercell3 = (XSSFCell) headerrow3.createCell((short) 7);
						 headercell3.setCellValue(Math.round(PackageStockDaysLastMonthActual));
						 headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
						 SetNormalCellBackColorCenterTotal(workbook,headercell3);
				    }
				    
				    if(StockDaysCurrentMonthTarget<=3){
					    headercell3 = (XSSFCell) headerrow3.createCell((short) 8);
					    headercell3.setCellValue(Math.round(StockDaysCurrentMonthTarget));
					    headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
					    SetNormalCellBackColorCenterTotal(workbook,headercell3);
				    }else{
				    	 headercell3 = (XSSFCell) headerrow3.createCell((short) 8);
						    headercell3.setCellValue(Math.round(StockDaysCurrentMonthTarget));
						    headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
						    SetNormalCellBackColorCenterTotal(workbook,headercell3);
				    }
					
				    headercell3 = (XSSFCell) headerrow3.createCell((short) 9);
				    headercell3.setCellValue(Math.round(MinToLiftedTotal));
				    headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColorCenterTotal(workbook,headercell3);
				    
					 	
				  //  RowCount++;	
				}
				//RowCount++;
				
				MinToLiftedGrandTotal+=MinToLiftedTotal;
			}
	     
	     
	     
	     
	     
	     ///////////////////////////////////////////////////////////////////
	     //////////////////////////////////////////////////////////////////
	      
	      
			
			double DistributorAverageSecondaySales = DistributorTotalSecondarySales / DaysInLastMonth;

			
			double DistributorStockDaysLastMonthActual = 0;
			if (DistributorAverageSecondaySales != 0){
				DistributorStockDaysLastMonthActual = DistributorTotalBalance / DistributorAverageSecondaySales;
			}
			
			double DistributorDailyTargetLifting = Math.round(DistributorTotalLiftingTarget / Utilities.getDayNumberByDate(Utilities.getEndDateByDate(EndDate)));
			

			double DistributorStockDaysCurrentMonthTarget = 0;
			if (DistributorDailyTargetLifting != 0){
				DistributorStockDaysCurrentMonthTarget = DistributorTotalBalance / DistributorDailyTargetLifting;
			}

			RowCount = RowCount+1;	
			
			XSSFRow headerrow4 = spreadsheet.createRow((short) RowCount);        
		    XSSFCell headercell4 = (XSSFCell) headerrow4.createCell((short) 1);
		     
		    
			
		    headercell4 = (XSSFCell) headerrow4.createCell((short) 1);
		    headercell4.setCellValue(rs.getLong("distributor_id")+" - "+rs.getString("name"));
		    SetNormalCellBackColorLeft(workbook,headercell4);
		    
		    headercell4 = (XSSFCell) headerrow4.createCell((short) 2);
		    headercell4.setCellValue("Total");
		    SetNormalCellBackColorLeft(workbook,headercell4);
		    
		    headercell4 = (XSSFCell) headerrow4.createCell((short) 3);
		    headercell4.setCellValue("");
		    SetNormalCellBackColor(workbook,headercell4);
		    
		    headercell4 = (XSSFCell) headerrow4.createCell((short) 4);
		    headercell4.setCellValue(Math.round(DistributorTotalBalance));
		    headercell4.setCellType(Cell.CELL_TYPE_NUMERIC);
		    SetNormalCellBackColor(workbook,headercell4);
		    
		    headercell4 = (XSSFCell) headerrow4.createCell((short) 5);
		    headercell4.setCellValue(Math.round(DistributorAverageSecondaySales));
		    headercell4.setCellType(Cell.CELL_TYPE_NUMERIC);
		    SetNormalCellBackColor(workbook,headercell4);
		    
		    headercell4 = (XSSFCell) headerrow4.createCell((short) 6);
		    headercell4.setCellValue(Math.round(DistributorDailyTargetLifting));
		    headercell4.setCellType(Cell.CELL_TYPE_NUMERIC);
		    SetNormalCellBackColor(workbook,headercell4);
		    
		    if(DistributorStockDaysLastMonthActual<=3){
			    headercell4 = (XSSFCell) headerrow4.createCell((short) 7);
			    headercell4.setCellValue(Math.round(DistributorStockDaysLastMonthActual));
			    headercell4.setCellType(Cell.CELL_TYPE_NUMERIC);
			    SetNormalCellBackColorRed(workbook,headercell4);
		    }else{
		    	 headercell4 = (XSSFCell) headerrow4.createCell((short) 7);
				    headercell4.setCellValue(Math.round(DistributorStockDaysLastMonthActual));
				    headercell4.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColor(workbook,headercell4);
		    }
		    
			if(DistributorStockDaysCurrentMonthTarget<=3){
			    headercell4 = (XSSFCell) headerrow4.createCell((short) 8);
			    headercell4.setCellValue(Math.round(DistributorStockDaysCurrentMonthTarget));
			    headercell4.setCellType(Cell.CELL_TYPE_NUMERIC);
			    SetNormalCellBackColorRed(workbook,headercell4);
			}else{
				headercell4 = (XSSFCell) headerrow4.createCell((short) 8);
			    headercell4.setCellValue(Math.round(DistributorStockDaysCurrentMonthTarget));
			    headercell4.setCellType(Cell.CELL_TYPE_NUMERIC);
			    SetNormalCellBackColor(workbook,headercell4);
			}
		    
			headercell4 = (XSSFCell) headerrow4.createCell((short) 9);
		    headercell4.setCellValue(Math.round(MinToLiftedGrandTotal));
		    headercell4.setCellType(Cell.CELL_TYPE_NUMERIC);
		    SetNormalCellBackColor(workbook,headercell4);
			
			/*
			
			//Extra Row
			
			RowCount = RowCount+1;	
			XSSFRow headerrow41 = spreadsheet.createRow((short) RowCount);        
		    XSSFCell headercell41 = (XSSFCell) headerrow41.createCell((short) 1);
		     
		    
			
		    headercell41 = (XSSFCell) headerrow41.createCell((short) 1);
		    headercell41.setCellValue("");
		    SetNormalCellBackColor(workbook,headercell41);
		    headercell41 = (XSSFCell) headerrow41.createCell((short) 2);
		    headercell41.setCellValue("");
		    SetNormalCellBackColor(workbook,headercell41);
		    headercell41 = (XSSFCell) headerrow41.createCell((short) 3);
		    headercell41.setCellValue("");
		    SetNormalCellBackColor(workbook,headercell41);
		    headercell41 = (XSSFCell) headerrow41.createCell((short) 4);
		    headercell41.setCellValue("");
		    SetNormalCellBackColor(workbook,headercell41);
		    headercell41 = (XSSFCell) headerrow41.createCell((short) 5);
		    headercell41.setCellValue("");
		    SetNormalCellBackColor(workbook,headercell41);
		    headercell41 = (XSSFCell) headerrow41.createCell((short) 6);
		    headercell41.setCellValue("");
		    SetNormalCellBackColor(workbook,headercell41);
		    headercell41 = (XSSFCell) headerrow41.createCell((short) 7);
		    headercell41.setCellValue("");
		    SetNormalCellBackColor(workbook,headercell41);
		    headercell41 = (XSSFCell) headerrow41.createCell((short) 8);
		    headercell41.setCellValue("");
		    SetNormalCellBackColor(workbook,headercell41);
			
			*/
			
			
	      // FirstColCount=FirstColCount+5;
	       RowCount++;
	       FirstRowCount=RowCount;
	       
	       DistributorIDs += DistributorID+",";
	       
			}//end of distributor check if
	       
			}// end of distributor while
			 
			if (DistributorIDs.length() > 0){
				DistributorIDs = DistributorIDs.substring(0, DistributorIDs.length()-1); //removing extra last comma
			}else{
				DistributorIDs = "0";
			}
			
			//System.out.println(DistributorIDs);
			
			
			
			
			
			
			
			
		    
		    
///////////////Consolidated
			
		    RowCount = RowCount+1;

		      XSSFRow row = spreadsheet.createRow((short) RowCount);	      
		      XSSFCell cell = (XSSFCell) row.createCell((short) 1);
		     
		      cell.setCellValue("Consolidated");
		      
		      
		     
		      
		      	      
		      spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  RowCount, //first row (0-based)
		    		  RowCount, //last row (0-based)
		    		  1, //first column (0-based)
		    	      9 //last column (0-based)
		    	      ));
		      
		      XSSFColor BackColorDistributor = new XSSFColor(new java.awt.Color(64, 64, 64));
		      
		      XSSFCellStyle style6 = workbook.createCellStyle();
		      style6.setFillBackgroundColor(
		      HSSFColor.BLUE.index );
		      style6.setFillForegroundColor(BackColorDistributor);
		      style6.setFillPattern(CellStyle.SOLID_FOREGROUND);
		      cell.setCellStyle(style6);
		     
		            
		      XSSFFont font = workbook.createFont();
		      font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		      font.setFontHeightInPoints((short)10);
		      font.setColor(IndexedColors.WHITE.getIndex());
		      style6.setFont(font);
		    
		    
		    double DistributorTotalBalance = 0;
			double DistributorTotalSecondarySales = 0;
			double DistributorTotalLiftingTarget = 0;
			
			
			
			
			ResultSet rs3 = s3.executeQuery("SELECT distinct ipv.package_id, ipv.package_label FROM pep.inventory_products_view ipv where ipv.category_id = 1 and ipv.is_visible = 1 and product_id in ("+SelectedProductIDs+") order by ipv.package_id");
			while(rs3.next()){
				int PackageID = rs3.getInt("package_id");
				
				double PackageTotalBalance = 0;
				double PackageTotalSecondarySales = 0;
				
				
				
				
				//////ResultSet rs1 = s2.executeQuery("SELECT ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.product_id, ipv.unit_per_sku, (SELECT sum(total_units_received-total_units_issued) FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id in(SELECT distributor_id  FROM common_distributors cd where distributor_id in(100914,100922)"+WhereSND+") and idsp.product_id = ipv.product_id) balance, (select sum(isap.total_units) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.distributor_id in(SELECT distributor_id  FROM common_distributors cd where distributor_id in(100914,100922)"+WhereSND+") and isap.product_id = ipv.product_id and isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDateNext(EndDateLastMonth)+") total_secondary_sales FROM pep.inventory_products_view ipv where ipv.category_id = 1 and ipv.is_visible = 1 and ipv.package_id = "+PackageID+" having balance > 0 order by ipv.package_id");
				ResultSet rs1 = s2.executeQuery("SELECT ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.product_id, ipv.unit_per_sku, (SELECT sum(total_units_received-total_units_issued) FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id in("+DistributorIDs+") and idsp.product_id = ipv.product_id) balance, (select sum(isap.total_units) from inventory_sales_adjusted_products isap where isap.cache_distributor_id in("+DistributorIDs+") and isap.product_id = ipv.product_id and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDate(EndDateLastMonth)+") total_secondary_sales FROM pep.inventory_products_view ipv where ipv.category_id = 1 and ipv.is_visible = 1 and ipv.package_id = "+PackageID+" order by ipv.package_id");
				
				while(rs1.next()){
					
					//System.out.println("Old Row Count "+RowCount);
					
					XSSFRow headerrow2 = spreadsheet.createRow((short) RowCount+1);        
				    XSSFCell headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
				
					int BrandID = rs1.getInt("brand_id");
					int ProductID = rs1.getInt("product_id");
					double UnitPerSKU = rs1.getDouble("unit_per_sku");
					double balance = rs1.getDouble("balance");
					if (balance < 0){
						balance = 0;
					}
					double TotalSecondarySales = rs1.getDouble("total_secondary_sales") / UnitPerSKU;
					
					double BalanceRawCases = balance / UnitPerSKU;
					
					double AverageSecondaySales = TotalSecondarySales / DaysInLastMonth;
					
					double StockDaysLastMonthActual = 0;
					if (AverageSecondaySales != 0){
						StockDaysLastMonthActual = BalanceRawCases / AverageSecondaySales;
					}
					
					PackageTotalBalance += BalanceRawCases;
					DistributorTotalBalance += BalanceRawCases; 
					GrandTotalBalance += BalanceRawCases;
					
					PackageTotalSecondarySales += TotalSecondarySales;
					DistributorTotalSecondarySales += TotalSecondarySales;
					GrandTotalSecondarySales += TotalSecondarySales;
					
					
					//Daily Current Month Target ////////////////////////
					////////////////////////////////////////////////////
					
					double TargetLifting = 0;
					ResultSet rs11 = s4.executeQuery("SELECT sum(dtpb.quantity) FROM pep.distributor_targets dt join distributor_targets_packages_brands dtpb on dt.id = dtpb.id where month = "+month+" and year = "+year+" and distributor_id in( "+DistributorIDs+") and package_id ="+PackageID+" and brand_id="+BrandID);
					if (rs11.first()){
						TargetLifting = rs11.getDouble(1);
					}
					DistributorTotalLiftingTarget += TargetLifting;
					GrandTotalLiftingTarget +=TargetLifting;
							
					double DailyTargetLifting = Math.round(TargetLifting / Utilities.getDayNumberByDate(Utilities.getEndDateByDate(EndDate)));
					
					
					double StockDaysCurrentMonthTarget = 0;
					if (DailyTargetLifting != 0){
						StockDaysCurrentMonthTarget = BalanceRawCases / DailyTargetLifting;
					}
					
					////////////////////////////////////////////////////
					///////////////////////////////////////////////////
					
					
					headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
					headercell2.setCellValue("Consolidated");
				    SetNormalCellBackColorLeft(workbook,headercell2);
						
					headercell2 = (XSSFCell) headerrow2.createCell((short) 2);
					headercell2.setCellValue(rs1.getString("package_label"));
				    SetNormalCellBackColorLeft(workbook,headercell2);	
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 3);
				    headercell2.setCellValue(rs1.getString("brand_label"));
				    SetNormalCellBackColorLeft(workbook,headercell2);
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 4);				   
				    headercell2.setCellValue(Math.round(BalanceRawCases));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    
				    SetNormalCellBackColor(workbook,headercell2);
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 5);
				    headercell2.setCellValue(Math.round(AverageSecondaySales));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColor(workbook,headercell2);
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 6);
				    headercell2.setCellValue(Math.round(DailyTargetLifting));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColor(workbook,headercell2);
				    
				    if(StockDaysLastMonthActual<=3){
				    	headercell2 = (XSSFCell) headerrow2.createCell((short) 7);
				    	headercell2.setCellValue(Math.round(StockDaysLastMonthActual));
				    	  headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    	SetNormalCellBackColorRed(workbook,headercell2);
				    }else{
				    	headercell2 = (XSSFCell) headerrow2.createCell((short) 7);
				    	headercell2.setCellValue(Math.round(StockDaysLastMonthActual));
				    	  headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    	SetNormalCellBackColor(workbook,headercell2);
				    }
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 8);
				    headercell2.setCellValue(Math.round(StockDaysCurrentMonthTarget));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColor(workbook,headercell2);
				    
				    double total_to_be_lifted=0;
				   
				    ResultSet rs111 = s5.executeQuery("select sum(to_be_lifted) total_to_be_lifted from dist_stock_temp where pack_id="+PackageID+" and brand_id="+BrandID);
				    if(rs111.first()){
				    	total_to_be_lifted = rs111.getDouble("total_to_be_lifted");
				    }
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 9);
				    headercell2.setCellValue(total_to_be_lifted);
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColor(workbook,headercell2);
					
				    RowCount++;
					}
				
				
					if (0 == 0){
					
					double TargetLifting = 0;
					//////ResultSet rs11 = s2.executeQuery("SELECT dtp.quantity FROM pep.distributor_targets dt join distributor_targets_packages dtp on dt.id = dtp.id where month = "+month+" and year = "+year+" and distributor_id in( SELECT distributor_id  FROM common_distributors cd where distributor_id in(100914,100922)) and package_id ="+PackageID);
					ResultSet rs11 = s2.executeQuery("SELECT sum(dtp.quantity) FROM pep.distributor_targets dt join distributor_targets_packages dtp on dt.id = dtp.id where month = "+month+" and year = "+year+" and distributor_id in( "+DistributorIDs+") and package_id ="+PackageID);
					if (rs11.first()){
						TargetLifting = rs11.getDouble(1);
					}
					//DistributorTotalLiftingTarget += TargetLifting;
					//GrandTotalLiftingTarget +=TargetLifting;
					
					
					double DailyTargetLifting = Math.round(TargetLifting / Utilities.getDayNumberByDate(Utilities.getEndDateByDate(EndDate)));
					
					double PackageAverageSecondaySales = PackageTotalSecondarySales / DaysInLastMonth;
					
					double PackageStockDaysLastMonthActual = 0;
					if (PackageAverageSecondaySales != 0){
						PackageStockDaysLastMonthActual = PackageTotalBalance / PackageAverageSecondaySales;
					}
					
					
					double StockDaysCurrentMonthTarget = 0;
					if (DailyTargetLifting != 0){
						StockDaysCurrentMonthTarget = PackageTotalBalance / DailyTargetLifting;
					}
					 RowCount = RowCount+1;
					XSSFRow headerrow3 = spreadsheet.createRow((short) RowCount);        
				    XSSFCell headercell3 = (XSSFCell) headerrow3.createCell((short) 1);
				     
				    
				   
				    
				    headercell3 = (XSSFCell) headerrow3.createCell((short) 1);
				    headercell3.setCellValue( "Consolidated");
				    SetNormalCellBackColorLeftTotal(workbook,headercell3);
				    
				    headercell3 = (XSSFCell) headerrow3.createCell((short) 2);
				    headercell3.setCellValue(rs3.getString("package_label"));
				    SetNormalCellBackColorLeftTotal(workbook,headercell3);
				    
				    headercell3 = (XSSFCell) headerrow3.createCell((short) 3);
				    headercell3.setCellValue("Total");
				    SetNormalCellBackColorLeftTotal(workbook,headercell3);
				    
				    headercell3 = (XSSFCell) headerrow3.createCell((short) 4);
				    headercell3.setCellValue(Math.round(PackageTotalBalance));
				    headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColorCenterTotal(workbook,headercell3);
				    
				    headercell3 = (XSSFCell) headerrow3.createCell((short) 5);
				    headercell3.setCellValue(Math.round(PackageAverageSecondaySales));
				    headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColorCenterTotal(workbook,headercell3);
				    
				    headercell3 = (XSSFCell) headerrow3.createCell((short) 6);
				    headercell3.setCellValue(Math.round(DailyTargetLifting));
				    headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColorCenterTotal(workbook,headercell3);
				    
				    if(PackageStockDaysLastMonthActual<=3){
					    headercell3 = (XSSFCell) headerrow3.createCell((short) 7);
					    headercell3.setCellValue(Math.round(PackageStockDaysLastMonthActual));
					    headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
					    SetNormalCellBackColorCenterTotal(workbook,headercell3);
				    }else{
				    	 headercell3 = (XSSFCell) headerrow3.createCell((short) 7);
						 headercell3.setCellValue(Math.round(PackageStockDaysLastMonthActual));
						 headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
						 SetNormalCellBackColorCenterTotal(workbook,headercell3);
				    }
				    
				    if(StockDaysCurrentMonthTarget<=3){
					    headercell3 = (XSSFCell) headerrow3.createCell((short) 8);
					    headercell3.setCellValue(Math.round(StockDaysCurrentMonthTarget));
					    headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
					    SetNormalCellBackColorCenterTotal(workbook,headercell3);
				    }else{
				    	 headercell3 = (XSSFCell) headerrow3.createCell((short) 8);
						    headercell3.setCellValue(Math.round(StockDaysCurrentMonthTarget));
						    headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
						    SetNormalCellBackColorCenterTotal(workbook,headercell3);
				    }
					
				   
				    double gtotal_to_be_lifted=0;
					   
				    ResultSet rs111 = s5.executeQuery("select sum(to_be_lifted) total_to_be_lifted from dist_stock_temp where pack_id="+PackageID);
				    if(rs111.first()){
				    	gtotal_to_be_lifted = rs111.getDouble("total_to_be_lifted");
				    }
				    
				    headercell3 = (XSSFCell) headerrow3.createCell((short) 9);
				    headercell3.setCellValue(gtotal_to_be_lifted);
				    headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColorCenterTotal(workbook,headercell3);
					 
				  //  RowCount++;	
				}
				
				
				
			}
	     
	     
///////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////



double DistributorAverageSecondaySales = DistributorTotalSecondarySales / DaysInLastMonth;


double DistributorStockDaysLastMonthActual = 0;
if (DistributorAverageSecondaySales != 0){
DistributorStockDaysLastMonthActual = DistributorTotalBalance / DistributorAverageSecondaySales;
}

double DistributorDailyTargetLifting = DistributorTotalLiftingTarget / Utilities.getDayNumberByDate(Utilities.getEndDateByDate(EndDate));


double DistributorStockDaysCurrentMonthTarget = 0;
if (DistributorDailyTargetLifting != 0){
DistributorStockDaysCurrentMonthTarget = DistributorTotalBalance / DistributorDailyTargetLifting;
}

RowCount = RowCount+1;	

XSSFRow headerrow4 = spreadsheet.createRow((short) RowCount);        
XSSFCell headercell4 = (XSSFCell) headerrow4.createCell((short) 1);



headercell4 = (XSSFCell) headerrow4.createCell((short) 1);
headercell4.setCellValue("Consolidated");
SetNormalCellBackColorLeft(workbook,headercell4);

headercell4 = (XSSFCell) headerrow4.createCell((short) 2);
headercell4.setCellValue("Total");
SetNormalCellBackColorLeft(workbook,headercell4);

headercell4 = (XSSFCell) headerrow4.createCell((short) 3);
headercell4.setCellValue("");
SetNormalCellBackColor(workbook,headercell4);

headercell4 = (XSSFCell) headerrow4.createCell((short) 4);
headercell4.setCellValue(Math.round(DistributorTotalBalance));
headercell4.setCellType(Cell.CELL_TYPE_NUMERIC);
SetNormalCellBackColor(workbook,headercell4);

headercell4 = (XSSFCell) headerrow4.createCell((short) 5);
headercell4.setCellValue(Math.round(DistributorAverageSecondaySales));
headercell4.setCellType(Cell.CELL_TYPE_NUMERIC);
SetNormalCellBackColor(workbook,headercell4);

headercell4 = (XSSFCell) headerrow4.createCell((short) 6);
headercell4.setCellValue(Math.round(DistributorDailyTargetLifting));
headercell4.setCellType(Cell.CELL_TYPE_NUMERIC);
SetNormalCellBackColor(workbook,headercell4);

if(DistributorStockDaysLastMonthActual<=3){
headercell4 = (XSSFCell) headerrow4.createCell((short) 7);
headercell4.setCellValue(Math.round(DistributorStockDaysLastMonthActual));
headercell4.setCellType(Cell.CELL_TYPE_NUMERIC);
SetNormalCellBackColorRed(workbook,headercell4);
}else{
headercell4 = (XSSFCell) headerrow4.createCell((short) 7);
headercell4.setCellValue(Math.round(DistributorStockDaysLastMonthActual));
headercell4.setCellType(Cell.CELL_TYPE_NUMERIC);
SetNormalCellBackColor(workbook,headercell4);
}

if(DistributorStockDaysCurrentMonthTarget<=3){
headercell4 = (XSSFCell) headerrow4.createCell((short) 8);
headercell4.setCellValue(Math.round(DistributorStockDaysCurrentMonthTarget));
headercell4.setCellType(Cell.CELL_TYPE_NUMERIC);
SetNormalCellBackColorRed(workbook,headercell4);
}else{
headercell4 = (XSSFCell) headerrow4.createCell((short) 8);
headercell4.setCellValue(Math.round(DistributorStockDaysCurrentMonthTarget));
headercell4.setCellType(Cell.CELL_TYPE_NUMERIC);
SetNormalCellBackColor(workbook,headercell4);
}


double Ggtotal_to_be_lifted=0;

ResultSet rs111 = s5.executeQuery("select sum(to_be_lifted) total_to_be_lifted from dist_stock_temp ");
if(rs111.first()){
	Ggtotal_to_be_lifted = rs111.getDouble("total_to_be_lifted");
}

headercell4 = (XSSFCell) headerrow4.createCell((short) 9);
headercell4.setCellValue(Ggtotal_to_be_lifted);
headercell4.setCellType(Cell.CELL_TYPE_NUMERIC);
SetNormalCellBackColor(workbook,headercell4);
			
			
			
			/////////////////////////////////////////
		    
			
			//Auto Sizing Column
		    
		    for(int i=1;i<10;i++){
		    	spreadsheet.autoSizeColumn(i);
		    }
		    
			
			
			
			
			
			
			FileOutputStream out = new FileOutputStream(
				      new File(filename));
				      workbook.write(out);
				      out.close();
				      //System.out.println(
				      //"typesofcells.xlsx written successfully");
				      
				      
				      s.executeUpdate("DROP TABLE IF EXISTS dist_stock_temp ");
	}
	
	public void Set2ndHeaderBackColor(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(222, 222, 222));
		
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
	}
	
	public void Set3rdHeaderBackColor(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(238, 237, 238));
		
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
	
	public void Set3rdHeaderBackColorMinToLifted(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(251, 188, 210));
		
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
