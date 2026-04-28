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



public class DistributorStockAnalysisExcel2 {

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
	
	
	Date Yesterday = Utilities.getDateByDays(-1);
	
	
	long SND_ID = 0;
	
	public static void main(String[] args)throws Exception 
	   {
		
		
		
		
			   
	   }
	
	
	public void setSND(long SND_ID){
		this.SND_ID = SND_ID;
	}
	
	public DistributorStockAnalysisExcel2() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		
		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
		s5 = ds.createStatement();
		s6 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6){
			Yesterday = Utilities.getDateByDays(-2);
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
		    	      65 //last column (0-based)
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
		    	      65 //last column (0-based)
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
			
			//System.out.println("SELECT *  FROM common_distributors cd where is_shifted_to_other_plant = 0 and distributor_id not in (200769,201001) and distributor_id in(select distributor_id from mobile_order where created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(-30))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(0))+") "+WhereSND+" and distributor_id in (100001,100914)");
			
			
			ResultSet rs = s.executeQuery("SELECT *  FROM common_distributors cd where is_shifted_to_other_plant = 0 and distributor_id not in (200769,201001) and distributor_id in(select distributor_id from mobile_order where created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(-30))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(0))+") "+WhereSND+" and distributor_id in (100996,100914,100986,101003,101006,100951,100981,100657,100638,100706)");
			while(rs.next()){
				
				double MinToLiftedGrandTotal=0;
	      
	      XSSFRow row = spreadsheet.createRow((short) RowCount);	      
	      XSSFCell cell = (XSSFCell) row.createCell((short) 1);
	     
	      cell.setCellValue(rs.getLong("distributor_id")+" - "+rs.getString("name"));
	      
	      
	      	      
	      spreadsheet.addMergedRegion(new CellRangeAddress(
	    		  FirstRowCount, //first row (0-based)
	    		  FirstRowCount, //last row (0-based)
	    		  1, //first column (0-based)
	    	      65 //last column (0-based)
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
	      headercell.setCellValue("");
	      Set2ndHeaderBackColor(workbook,headercell);
	     
	      
	      headercell = (XSSFCell) headerrow.createCell((short) 5);	     
	      headercell.setCellValue("");
	      Set2ndHeaderBackColor(workbook,headercell);
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		  5, //first column (0-based)
	    	      6 //last column (0-based)
	    	      ));
	    
	     
	     headercell = (XSSFCell) headerrow.createCell((short) 7);	     
	      headercell.setCellValue("");
	      Set2ndHeaderBackColor(workbook,headercell);
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		  7, //first column (0-based)
	    	      64 //last column (0-based)
	    	      ));
	      
	     headercell = (XSSFCell) headerrow.createCell((short) 65);	     
	      headercell.setCellValue("");
	      Set2ndHeaderBackColor(workbook,headercell);
	      
	      //// Extra Header ////
	      
	      
	      RowCount = RowCount+1;
	      FirstRowCount++;
	      
	      XSSFRow headerrow1n = spreadsheet.createRow((short) RowCount);        
		  XSSFCell headercell1n = (XSSFCell) headerrow1n.createCell((short) 1);
		     
		     headercell1n = (XSSFCell) headerrow1n.createCell((short) 1);	     
		     headercell1n.setCellValue("");
		     Set3rdHeaderBackColor(workbook,headercell1n);
		     
		     headercell1n = (XSSFCell) headerrow1n.createCell((short) 2);	     
		     headercell1n.setCellValue("");
		     Set3rdHeaderBackColor(workbook,headercell1n);
		     
		     headercell1n = (XSSFCell) headerrow1n.createCell((short) 3);	     
		     headercell1n.setCellValue("");
		     Set3rdHeaderBackColor(workbook,headercell1n);
	      
	      
		     int h=4;
		     while(h<65){
		    	 headercell1n = (XSSFCell) headerrow1n.createCell((short) h);	     
			     headercell1n.setCellValue("Stock");
			     Set3rdHeaderBackColor(workbook,headercell1n);
			     
			     
			     h++;
			     headercell1n = (XSSFCell) headerrow1n.createCell((short) h);	     
			     headercell1n.setCellValue("Back Order");
			     Set3rdHeaderBackColor(workbook,headercell1n);
			     
			     h++;
		     }
		     
		     
	      ///////////////////////
	      
	      
	      
	     //3rd Row Header //////////////////////
	     ///////////////////////////////////////
	    
	     RowCount = RowCount+1;
	     
	     FirstRowCount++;
	     
	     
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
	     
	     
	    /* headercell1 = (XSSFCell) headerrow1.createCell((short) 4);	     
	     headercell1.setCellValue("R/C");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 5);	     
	     headercell1.setCellValue("Last Month Actual");
	     Set3rdHeaderBackColor(workbook,headercell1);*/
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 4);	     
	     headercell1.setCellValue("01/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		  4, //first column (0-based)
	    	      5 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 6);	     
	     headercell1.setCellValue("02/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 6, //first column (0-based)
	    	      7 //last column (0-based)
	    	      ));
	     
	     
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 8);	     
	     headercell1.setCellValue("03/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		  8, //first column (0-based)
	    	      9 //last column (0-based)
	    	      ));
	     
	     
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 10);	     
	     headercell1.setCellValue("04/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 10, //first column (0-based)
	    	      11 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 12);	     
	     headercell1.setCellValue("05/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		  12, //first column (0-based)
		    	     13 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 14);	     
	     headercell1.setCellValue("06/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 14, //first column (0-based)
	    	      15 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 16);	     
	     headercell1.setCellValue("07/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 16, //first column (0-based)
	    	      17 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 18);	     
	     headercell1.setCellValue("08/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 18, //first column (0-based)
	    	      19 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 20);	     
	     headercell1.setCellValue("09/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 20, //first column (0-based)
	    	      21 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 22);	     
	     headercell1.setCellValue("10/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 22, //first column (0-based)
	    	      23 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 24);	     
	     headercell1.setCellValue("11/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 24, //first column (0-based)
	    	      25 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 26);	     
	     headercell1.setCellValue("12/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 26, //first column (0-based)
	    	      27 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 28);	     
	     headercell1.setCellValue("13/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 28, //first column (0-based)
	    	      29 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 30);	     
	     headercell1.setCellValue("14/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 30, //first column (0-based)
	    	      31 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 32);	     
	     headercell1.setCellValue("15/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 32, //first column (0-based)
	    	      33 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 34);	     
	     headercell1.setCellValue("16/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 34, //first column (0-based)
	    	      35 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 36);	     
	     headercell1.setCellValue("17/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 36, //first column (0-based)
	    	      37 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 38);	     
	     headercell1.setCellValue("18/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 38, //first column (0-based)
	    	      39 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 40);	     
	     headercell1.setCellValue("19/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		  40, //first column (0-based)
	    	      41 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 42);	     
	     headercell1.setCellValue("20/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 42, //first column (0-based)
	    	      43 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 44);	     
	     headercell1.setCellValue("21/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 44, //first column (0-based)
	    	      45 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 46);	     
	     headercell1.setCellValue("22/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 46, //first column (0-based)
	    	      47 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 48);	     
	     headercell1.setCellValue("23/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 48, //first column (0-based)
	    	      49 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 50);	     
	     headercell1.setCellValue("24/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 50, //first column (0-based)
	    	      51 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 52);	     
	     headercell1.setCellValue("25/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 52, //first column (0-based)
	    	      53 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 54);	     
	     headercell1.setCellValue("26/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 54, //first column (0-based)
	    	      55 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 56);	     
	     headercell1.setCellValue("27/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 56, //first column (0-based)
	    	      57 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 58);	     
	     headercell1.setCellValue("28/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 58, //first column (0-based)
	    	      59 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 60);	     
	     headercell1.setCellValue("29/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 60, //first column (0-based)
	    	      61 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 62);	     
	     headercell1.setCellValue("30/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 62, //first column (0-based)
	    	      63 //last column (0-based)
	    	      ));
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 64);	     
	     headercell1.setCellValue("31/07/2017");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		 64, //first column (0-based)
	    	      65 //last column (0-based)
	    	      ));
	     
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
			
			
			
			ResultSet rs3 = s3.executeQuery("SELECT distinct ipv.package_id, ipv.package_label FROM pep.inventory_products_view ipv where ipv.category_id = 1 and ipv.is_visible = 1 and product_id in ("+SelectedProductIDs+") and product_id in (select distinct ipv.product_id from mobile_order_products_backorder mopb join mobile_order mo on mo.id = mopb.id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mo.backordered_on between '2017-07-01' and '2017-08-01') and ipv.package_id not in (13) order by ipv.package_id");
			while(rs3.next()){
				int PackageID = rs3.getInt("package_id");
				double MinToLiftedTotal=0;
				double PackageTotalBalance = 0;
				double PackageTotalSecondarySales = 0;
				
				 double[] NewTotalPackageBalance=  new double[31];
				 double[] NewTotalPackageBalance2Nd=  new double[31];
				 
				 
				 
				
				
				ResultSet rs1 = s2.executeQuery("SELECT ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.product_id, ipv.unit_per_sku, (SELECT sum(total_units_received-total_units_issued) FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id = "+DistributorID+" and idsp.product_id = ipv.product_id) balance, (select sum(isap.total_units) from inventory_sales_adjusted_products isap where isap.cache_distributor_id = "+DistributorID+" and isap.product_id = ipv.product_id and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDate(EndDateLastMonth)+") total_secondary_sales FROM pep.inventory_products_view ipv where ipv.category_id = 1 and ipv.is_visible = 1 and ipv.package_id = "+PackageID+" and ipv.product_id in (select distinct ipv.product_id from mobile_order_products_backorder mopb join mobile_order mo on mo.id = mopb.id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mo.backordered_on between '2017-07-01' and '2017-08-01') order by ipv.package_id");
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
				    
				    /*headercell2 = (XSSFCell) headerrow2.createCell((short) 4);				   
				    headercell2.setCellValue(Math.round(BalanceRawCases));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    
				    SetNormalCellBackColor(workbook,headercell2);*/
				    
				    
				    
				  
				    Date CurrentDate = Utilities.parseDate("01/07/2017");
				    Date EmdDateOfMonth= Utilities.parseDate("31/07/2017");
				    
				   // System.out.println("End Date "+EmdDateOfMonth+" Start Date "+CurrentDate);
				    
				    
				   
				    
				    
				    int totalcounter=0;
				    int j=4;
				    while(j<65){
				    	
				    	
				    	//System.out.println("SELECT sum(total_units_received-total_units_issued) balance FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id in ("+DistributorID+") and idsp.product_id = "+ProductID+" and ids.created_on < "+Utilities.getSQLDateNext(CurrentDate));
				    	
				    	double BalanceRawCases3=0;
				    	
				    	ResultSet rs113 = s4.executeQuery("SELECT sum(total_units_received-total_units_issued) balance, ipv.unit_per_sku FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp, inventory_products_view ipv where ids.id = idsp.id and idsp.product_id=ipv.product_id and ids.distributor_id in ("+DistributorID+") and idsp.product_id = "+ProductID+" and ids.created_on < '"+Utilities.getSQLDateWithoutQuotes(CurrentDate)+" 20:00:00'");
						while(rs113.next()){
						
							double UnitPerSKU1 = rs113.getDouble("unit_per_sku");
							double balance3 = rs113.getDouble("balance");
							if (balance3 < 0){
								balance3 = 0;
							}
							
							
							 BalanceRawCases3 = balance3 / UnitPerSKU1;
							 
							 NewTotalPackageBalance[totalcounter]+=BalanceRawCases3;
							
						}
				    
						headercell2 = (XSSFCell) headerrow2.createCell((short) j);				   
					    headercell2.setCellValue(Math.round(BalanceRawCases3));
					    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					    
					    SetNormalCellBackColor(workbook,headercell2);
					    
					    
					    j++;
					    
					    //Other Query
					    double BackOrders=0;
					    
					   // System.out.println("select sum(mopb.total_units/ipv.unit_per_sku) from mobile_order_products_backorder mopb join mobile_order mo on mo.id = mopb.id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mo.backordered_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and ipv.package_id = "+PackageID+" and ipv.brand_id = "+BrandID+" and mo.distributor_id = "+DistributorID);
					    
					    ResultSet rsO = s4.executeQuery("select sum(mopb.total_units)/ipv.unit_per_sku from mobile_order_products_backorder mopb join mobile_order mo on mo.id = mopb.id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mo.backordered_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and ipv.package_id = "+PackageID+" and ipv.brand_id = "+BrandID+" and mo.distributor_id = "+DistributorID);
					    while(rsO.next()){
					    	BackOrders = rsO.getDouble(1);
					    	
					    	NewTotalPackageBalance2Nd[totalcounter]+=BackOrders;
					    	
					    }
					    
					    
					    headercell2 = (XSSFCell) headerrow2.createCell((short) j);				   
					    headercell2.setCellValue(Math.round(BackOrders));
					    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					    
					    SetNormalCellBackColor(workbook,headercell2);
				    	
						
						
				    	
				    	
				    	
						CurrentDate=Utilities.getDateByDays(CurrentDate,1);	
				    	
				    	j++; totalcounter++;
				    }
				    
				    
				   
					
				   
				    
				    
				    
				   
				   
				    
				    
				    
				    RowCount++;
					}
				
				if (0 == 0){
					
					
							
					
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
				    
				   		int l=4;		    
				    
				    for(int k=0;k<NewTotalPackageBalance.length;k++,l++){
				    	 headercell3 = (XSSFCell) headerrow3.createCell((short) l);
						 headercell3.setCellValue(Math.round(NewTotalPackageBalance[k]));
						 headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
						 SetNormalCellBackColorCenterTotal(workbook,headercell3);
						 
						 
						 l++;
						 
						 
						 headercell3 = (XSSFCell) headerrow3.createCell((short) l);
						 headercell3.setCellValue(Math.round(NewTotalPackageBalance2Nd[k]));
						 headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
						 SetNormalCellBackColorCenterTotal(workbook,headercell3);
						 
				    }
				    
				    
				   /* for(int k=0;k<NewTotalPackageBalance2Nd.length;k++,l++){
				    	 headercell3 = (XSSFCell) headerrow3.createCell((short) l);
						 headercell3.setCellValue(Math.round(NewTotalPackageBalance2Nd[k]));
						 headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
						 SetNormalCellBackColorCenterTotal(workbook,headercell3);
				    }*/
					 	
				    
				    
				    
				    
				  //  RowCount++;	
				}
				//RowCount++;
				
				MinToLiftedGrandTotal+=MinToLiftedTotal;
			}
	     
	     
	     
	     
	     
	     ///////////////////////////////////////////////////////////////////
	     //////////////////////////////////////////////////////////////////
	      
	      
			
			
			
			
			RowCount = RowCount+1;	
			
			/*
			
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
		    
		   */
			
			
	      // FirstColCount=FirstColCount+5;
	       RowCount++;
	       FirstRowCount=RowCount;
	       
	       DistributorIDs += DistributorID+",";
	       
	       
	       
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
		    	     65 //last column (0-based)
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
			
			
			
			
			ResultSet rs3 = s3.executeQuery("SELECT distinct ipv.package_id, ipv.package_label FROM pep.inventory_products_view ipv where ipv.category_id = 1 and ipv.is_visible = 1 and product_id in ("+SelectedProductIDs+") and product_id in (select distinct ipv.product_id from mobile_order_products_backorder mopb join mobile_order mo on mo.id = mopb.id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mo.backordered_on between '2017-07-01' and '2017-08-01') order by ipv.package_id");
			while(rs3.next()){
				int PackageID = rs3.getInt("package_id");
				
				double PackageTotalBalance = 0;
				double PackageTotalSecondarySales = 0;
				double[] NewTotalPackageBalanceConsolidated=  new double[31];
				double[] NewTotalPackageBalanceConsolidated2Nd=  new double[31];
				
				
				
				//////ResultSet rs1 = s2.executeQuery("SELECT ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.product_id, ipv.unit_per_sku, (SELECT sum(total_units_received-total_units_issued) FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id in(SELECT distributor_id  FROM common_distributors cd where distributor_id in(100914,100922)"+WhereSND+") and idsp.product_id = ipv.product_id) balance, (select sum(isap.total_units) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.distributor_id in(SELECT distributor_id  FROM common_distributors cd where distributor_id in(100914,100922)"+WhereSND+") and isap.product_id = ipv.product_id and isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDateNext(EndDateLastMonth)+") total_secondary_sales FROM pep.inventory_products_view ipv where ipv.category_id = 1 and ipv.is_visible = 1 and ipv.package_id = "+PackageID+" having balance > 0 order by ipv.package_id");
				ResultSet rs1 = s2.executeQuery("SELECT ipv.package_id, ipv.package_label, ipv.brand_id, ipv.brand_label, ipv.product_id, ipv.unit_per_sku, (SELECT sum(total_units_received-total_units_issued) FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id in("+DistributorIDs+") and idsp.product_id = ipv.product_id) balance, (select sum(isap.total_units) from inventory_sales_adjusted_products isap where isap.cache_distributor_id in("+DistributorIDs+") and isap.product_id = ipv.product_id and isap.cache_created_on_date between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDate(EndDateLastMonth)+") total_secondary_sales FROM pep.inventory_products_view ipv where ipv.category_id = 1 and ipv.is_visible = 1 and ipv.package_id = "+PackageID+" and ipv.product_id in (select distinct ipv.product_id from mobile_order_products_backorder mopb join mobile_order mo on mo.id = mopb.id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mo.backordered_on between '2017-07-01' and '2017-08-01') order by ipv.package_id");
				
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
					
					
					
					
					
					headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
					headercell2.setCellValue("Consolidated");
				    SetNormalCellBackColorLeft(workbook,headercell2);
						
					headercell2 = (XSSFCell) headerrow2.createCell((short) 2);
					headercell2.setCellValue(rs1.getString("package_label"));
				    SetNormalCellBackColorLeft(workbook,headercell2);	
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 3);
				    headercell2.setCellValue(rs1.getString("brand_label"));
				    SetNormalCellBackColorLeft(workbook,headercell2);
				    
				   /* headercell2 = (XSSFCell) headerrow2.createCell((short) 4);				   
				    headercell2.setCellValue(Math.round(BalanceRawCases));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    
				    SetNormalCellBackColor(workbook,headercell2);
				    
				    */
				    Date CurrentDate = Utilities.parseDate("01/07/2017");
				    Date EmdDateOfMonth= Utilities.parseDate("31/07/2017");
				    
				    int totalcounter=0;
				    int j=4;
				    while(j<65){
				    	
				    	
				    	//System.out.println("SELECT sum(total_units_received-total_units_issued) balance FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp where ids.id = idsp.id and ids.distributor_id in ("+DistributorID+") and idsp.product_id = "+ProductID+" and ids.created_on < "+Utilities.getSQLDateNext(CurrentDate));
				    	
				    	double BalanceRawCases3=0;
				    	
				    	ResultSet rs113 = s4.executeQuery("SELECT sum(total_units_received-total_units_issued) balance, ipv.unit_per_sku FROM inventory_distributor_stock ids, inventory_distributor_stock_products idsp, inventory_products_view ipv where ids.id = idsp.id and idsp.product_id=ipv.product_id and ids.distributor_id in ("+DistributorIDs+") and idsp.product_id = "+ProductID+" and ids.created_on < "+Utilities.getSQLDateNext(CurrentDate));
						while(rs113.next()){
						
							double UnitPerSKU1 = rs113.getDouble("unit_per_sku");
							double balance3 = rs113.getDouble("balance");
							if (balance3 < 0){
								balance3 = 0;
							}
							
							
							 BalanceRawCases3 = balance3 / UnitPerSKU1;
							 
							 NewTotalPackageBalanceConsolidated[totalcounter]+=BalanceRawCases3;
							
						}
				    
						headercell2 = (XSSFCell) headerrow2.createCell((short) j);				   
					    headercell2.setCellValue(Math.round(BalanceRawCases3));
					    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					    
					    SetNormalCellBackColor(workbook,headercell2);
				    	
						
					    j++;
					    
					    //Other Query
					    double BackOrdersCons=0;
					    
					   // System.out.println("select sum(mopb.total_units/ipv.unit_per_sku) from mobile_order_products_backorder mopb join mobile_order mo on mo.id = mopb.id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mo.backordered_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and ipv.package_id = "+PackageID+" and ipv.brand_id = "+BrandID+" and mo.distributor_id = "+DistributorID);
					    
					    ResultSet rsO = s4.executeQuery("select sum(mopb.total_units/ipv.unit_per_sku) from mobile_order_products_backorder mopb join mobile_order mo on mo.id = mopb.id join inventory_products_view ipv on mopb.product_id = ipv.product_id where mo.backordered_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and ipv.package_id = "+PackageID+" and ipv.brand_id = "+BrandID+" and mo.distributor_id in("+DistributorIDs+")");
					    while(rsO.next()){
					    	BackOrdersCons = rsO.getDouble(1);
					    	
					    	NewTotalPackageBalanceConsolidated2Nd[totalcounter]+=BackOrdersCons;
					    	
					    }
					    
					    
					    headercell2 = (XSSFCell) headerrow2.createCell((short) j);				   
					    headercell2.setCellValue(Math.round(BackOrdersCons));
					    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					    
					    SetNormalCellBackColor(workbook,headercell2);
				    	
					    
					    
						
				    	
				    	
				    	
						CurrentDate=Utilities.getDateByDays(CurrentDate,1);	
				    	
				    	j++; totalcounter++;
				    }
				    
				    
					
				    RowCount++;
					}
				
				
					if (0 == 0){
					
					
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
				    
				    int l=4;
				    
				    for(int k=0;k<NewTotalPackageBalanceConsolidated.length;k++,l++){
				    	 headercell3 = (XSSFCell) headerrow3.createCell((short) l);
						 headercell3.setCellValue(Math.round(NewTotalPackageBalanceConsolidated[k]));
						 headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
						 SetNormalCellBackColorCenterTotal(workbook,headercell3);
						 
						 l++;
						 
						 headercell3 = (XSSFCell) headerrow3.createCell((short) l);
						 headercell3.setCellValue(Math.round(NewTotalPackageBalanceConsolidated2Nd[k]));
						 headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
						 SetNormalCellBackColorCenterTotal(workbook,headercell3);
						 
				    }
				    
				    /*
				    for(int k=0;k<NewTotalPackageBalanceConsolidated.length;k++,l++){
				    	 headercell3 = (XSSFCell) headerrow3.createCell((short) l);
						 headercell3.setCellValue(Math.round(NewTotalPackageBalanceConsolidated2Nd[k]));
						 headercell3.setCellType(Cell.CELL_TYPE_NUMERIC);
						 SetNormalCellBackColorCenterTotal(workbook,headercell3);
				    }*/
				    
				   
					 
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


/*

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
*/

			
			
			
			/////////////////////////////////////////
		    
			
			//Auto Sizing Column
		    
		    for(int i=1;i<65;i++){
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
