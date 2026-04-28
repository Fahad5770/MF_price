package com.pbc.reports;

import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.itextpdf.text.Font;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

import java.awt.Color;
import java.io.File;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.*;

import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import java.math.BigInteger;

public class R368Excel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath() + "/CashInflowSummary.pdf";

	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	Statement s5;
	Statement s6;

	Date StartDate = Utilities.getDateByDays(0); // Utilities.parseDate("13/02/2016");
	Date EndDate = Utilities.getDateByDays(0);// Utilities.parseDate("13/02/2016");

	/// Date StartDate = Utilities.parseDate("29/08/2016");
	/// Date EndDate = Utilities.parseDate("29/08/2016");

	Date Yesterday = Utilities.getDateByDays(-1);

	long SND_ID = 0;

	public static void main(String[] args) throws Exception {

	}

	public R368Excel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

		ds.createConnection();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
		s5 = ds.createStatement();
		s6 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6) {
			Yesterday = Utilities.getDateByDays(-2);
		}

	}

	public void createPdf(String filename, Date StartDate, Date EndDate, String whereDistributor)
			throws IOException, SQLException {


		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("cell types");

		int FirstRowCount = 0;

		int RowCount = 0;

		// Report Heading

		XSSFRow rowH = spreadsheet.createRow((short) RowCount);
		XSSFCell cellH = (XSSFCell) rowH.createCell((short) 0);

		


		cellH.setCellValue("R368 - Stock Register Report");

		spreadsheet.addMergedRegion(new CellRangeAddress(FirstRowCount, // first row (0-based)
				FirstRowCount, // last row (0-based)
				0, // first column (0-based)
				3 // last column (0-based)
		));

		CellStyle style = workbook.createCellStyle();// Create style
		XSSFFont font = workbook.createFont();// Create font
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);// set it to bold
		cellH.setCellStyle(style);

		RowCount++;

		// Printing Date

		XSSFRow rowP = spreadsheet.createRow((short) RowCount);

		XSSFCell cellP = (XSSFCell) rowP.createCell((short) 0);

		// Date EndDate = Utilities.getEndDateByDate(StartDate);

		cellP.setCellValue("Period : " + Utilities.getDisplayDateFormat(StartDate) + " - "
				+ Utilities.getDisplayDateFormat(EndDate));
		spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, // first row (0-based)
				RowCount, // last row (0-based)
				0, // first column (0-based)
				3 // last column (0-based)
		));

		/***************************** Data ***************************************/


	
		RowCount++;
		
		/*******************************
		 * ROW 3 Start
		 *******************************/
		

		CellStyle style2 = workbook.createCellStyle();// Create style
		style2.setFont(font);// set it to bold
		style2.setAlignment(CellStyle.ALIGN_CENTER);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		
		XSSFRow row3 = spreadsheet.createRow((short) RowCount);
		XSSFCell row3Cell = (XSSFCell) row3.createCell((short) 0);
		
		
		
		
		int colRow3 = 0;
		for(int i=0; i<7; i++) {
			row3Cell = (XSSFCell) row3.createCell((short) colRow3);
			row3Cell.setCellValue("");
			row3Cell.setCellStyle(style2);
			colRow3++;
		}
		
 		ResultSet rsLrbs = s.executeQuery("select label from inventory_products_lrb_types where id != 3");
 		int countForColor=0;
 		while( rsLrbs.next()) {
 		
 			String label = rsLrbs.getString("label");
 			System.out.println("Label : "+label);
 			
 			
 		 
 			
 			
 			spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, // first row (0-based)
 	  				RowCount, // last row (0-based)
 	 				colRow3, // first column (0-based)
 	  				colRow3 + 3 // last column (0-based)		
 	  	));
 	 		
 	 		
 	 		
 	 		
 	 		row3Cell = (XSSFCell) row3.createCell((short) colRow3);
 	 		row3Cell.setCellValue(label);
        //	row3Cell.setCellStyle(style2);
 	 	 
 	 			if (countForColor %2 == 0) {
 	 				
 	 		 SetNormalCellBackColorH_yellow(workbook, row3Cell);
 	 				
 	 		 
 	 		 
 	 				}
 	 			else{
 	 				
 	 				SetNormalCellBackColorH_green(workbook, row3Cell);
 	 				System.out.println("Green Is rinning");
 	 			}
 	 			
 	 			
 	 			
 	 		 
 
 	 		
 	 		
  		// SetNormalCellBackColorH_yellow(workbook, row3Cell);
  		 //SetNormalCellBackColorH_green(workbook, row3Cell);
  		 
 	 		colRow3++;
 	 		
 	 		
 	 		row3Cell = (XSSFCell) row3.createCell((short) colRow3);
 	 		row3Cell.setCellValue("");
 	 		
 	 		row3Cell.setCellStyle(style2);
 	 		colRow3++;
 	 		
 	 		row3Cell = (XSSFCell) row3.createCell((short) colRow3);
 	 		row3Cell.setCellValue("");
 	 		row3Cell.setCellStyle(style2);
 	 		colRow3++;
 	 		
 	 		row3Cell = (XSSFCell) row3.createCell((short) colRow3);
 	 		row3Cell.setCellValue("");
 	 		row3Cell.setCellStyle(style2);
 	 		colRow3++;
 			
 	 		countForColor++;
 	 		
 	 		
 	 		
 		}
 		
 	
		/*******************************
		 * ROW 3 End
		 *******************************/
		/*******************************
		 * ROW 4 Start
		 *******************************/
		
 		String[] columnNames = {"Sr No.","DISTRIBUTOR ID", "DISTRIBUTOR NAME", "TOWN", "TSO","ASM","RSM"};
 		
 		
 		
		RowCount ++;
		
	

		XSSFRow row4 = spreadsheet.createRow((short) RowCount);
		XSSFCell row4Cell = (XSSFCell) row4.createCell((short) 0);
		
		int col = 0;
		for (String title : columnNames) {
			row4Cell = (XSSFCell) row4.createCell((short) col);
			row4Cell.setCellValue(title);
		//	Set2ndHeaderBackColorOrderKPI(workbook, row5Cell);
			row4Cell.setCellStyle(style2);
			col++;
		}
		
		
	 
 		 
		ResultSet opsc = s.executeQuery("select count(*) as total from inventory_products_lrb_types where id != 3");
	 
	
		int totalLrbs = 0;
		if(opsc.first()) {
			totalLrbs = opsc.getInt("total");
		}
		
		for(int i= 0; i<totalLrbs; i++ ) {
			
			row4Cell = (XSSFCell) row4.createCell((short) col);
			row4Cell.setCellValue("Opening Stock");
			row4Cell.setCellStyle(style2);
			col++;
 	 		
 	 		row4Cell = (XSSFCell) row4.createCell((short) col);
			row4Cell.setCellValue("Primary Sales");
			row4Cell.setCellStyle(style2);
			col++;
 	 		
 	 		row4Cell = (XSSFCell) row4.createCell((short) col);
			row4Cell.setCellValue("Secondary Sales");
			row4Cell.setCellStyle(style2);
			col++;
 	 		
 	 		row4Cell = (XSSFCell) row4.createCell((short) col);
			row4Cell.setCellValue("Closing Stock");
			row4Cell.setCellStyle(style2);
			col++;
 	 		
			
		}
		
		
		
		
		
		
		
		
		
		
		
	
		
	
		/*******************************
		 * ROW 4 End
		 *******************************/


	
//		int TotalMobileOrders = 0;
//		int TotalNonMobileOrders = 0;
//		int TotalOrders = 0;

		
		

		
		
		
		
		
		/*******************************
		 * Row 5 Starting from here
		 *******************************/
		
		int serialNumber=1;
		
		String[] labelSubctegories = {"1","1.52", "2.59", "0.58"};
		

		CellStyle simpleTextStyle = workbook.createCellStyle();
		simpleTextStyle.setAlignment(CellStyle.ALIGN_LEFT);
		simpleTextStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		simpleTextStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		simpleTextStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		simpleTextStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		for(int j=0; j<5;  j++) {
			
			
			
			RowCount++;
			XSSFRow row5 = spreadsheet.createRow((short) RowCount);
			XSSFCell row5Cell = (XSSFCell) row5.createCell((short) 0);

			int colRow5 = 0;

			row5Cell = (XSSFCell) row5.createCell((short) colRow5);
			row5Cell.setCellValue(serialNumber);
			row5Cell.setCellStyle(simpleTextStyle);
			serialNumber++;
			colRow5++;

			row5Cell = (XSSFCell) row5.createCell((short) colRow5);
			row5Cell.setCellValue("7553");
			row5Cell.setCellStyle(simpleTextStyle);
			colRow5++;

			row5Cell = (XSSFCell) row5.createCell((short) colRow5);
			row5Cell.setCellValue("7553 - United Enterprises");
			row5Cell.setCellStyle(simpleTextStyle);
			colRow5++;

			row5Cell = (XSSFCell) row5.createCell((short) colRow5);
			row5Cell.setCellValue("Lala Musa");
			row5Cell.setCellStyle(simpleTextStyle);
			colRow5++;

			row5Cell = (XSSFCell) row5.createCell((short) colRow5);
			row5Cell.setCellValue("SMP 35 GM");
			row5Cell.setCellStyle(simpleTextStyle);
			colRow5++;

			row5Cell = (XSSFCell) row5.createCell((short) colRow5);
			row5Cell.setCellValue("SMP 35 GM");
			row5Cell.setCellStyle(simpleTextStyle);
			colRow5++;

			row5Cell = (XSSFCell) row5.createCell((short) colRow5);
			row5Cell.setCellValue("12");
			row5Cell.setCellStyle(simpleTextStyle);
		    colRow5++;
	       
	        for(int i= 0; i<totalLrbs; i++ ) {
				
	        	row5Cell = (XSSFCell) row5.createCell((short) colRow5);
	        	row5Cell.setCellValue("0.1");
	        	row5Cell.setCellStyle(simpleTextStyle);
	        	colRow5++;

	        	row5Cell = (XSSFCell) row5.createCell((short) colRow5);
	        	row5Cell.setCellValue("27");
	        	row5Cell.setCellStyle(simpleTextStyle);
	        	colRow5++;
	        	
	        	row5Cell = (XSSFCell) row5.createCell((short) colRow5);
	        	row5Cell.setCellValue("3.5");
	        	row5Cell.setCellStyle(simpleTextStyle);
	        	colRow5++;
	        	
	        	row5Cell = (XSSFCell) row5.createCell((short) colRow5);
	        	row5Cell.setCellValue("1.86");
	        	row5Cell.setCellStyle(simpleTextStyle);
	        	colRow5++;
				
			}
			
			
			
			
			
		}
	    
//		row5Cell = (XSSFCell) row5.createCell((short) colRow5);
//		row5Cell.setCellValue("1");
//		row5Cell.setCellStyle(simpleTextStyle);
//		colRow5++;
//
//		row5Cell = (XSSFCell) row5.createCell((short) colRow5);
//		row5Cell.setCellValue("0");
//		row5Cell.setCellStyle(simpleTextStyle);
//		colRow5++;
//
//		row5Cell = (XSSFCell) row5.createCell((short) colRow5);
//		row5Cell.setCellValue("6748");
//		row5Cell.setCellStyle(simpleTextStyle);
//		colRow5++;

		/*******************************
		 * Row 5 will end here
		 *******************************/


    	/*******************************
		 Start: Here I will manage all the list 
		 *******************************/
        
        int RowNumber= 6;
        int maximumLength = 4;
        XSSFRow[] dataRows = new XSSFRow[maximumLength];
        
				 
//        XSSFCell   row[RowNumber] = spreadsheet.createRow((short) RowCount);
//		XSSFCell row$RowNumberCell = (XSSFCell) row5.createCell((short) 0);
//				 
				 
 
    	/*******************************
		 End: Here I will manage all the list 
		 *******************************/



		/*******************************
		 * ROW 6 Start
		 *******************************/
 

		
		{
		
		// var row+ser col
		RowCount ++;
		XSSFRow row6 = spreadsheet.createRow((short) RowCount);
		XSSFCell row6Cell = (XSSFCell) row6.createCell((short) 0);
		
		int colRow6 = 0;
		for(int i=0; i<6; i++) {
			row6Cell = (XSSFCell) row6.createCell((short) colRow6);
			row6Cell.setCellValue("");
			row6Cell.setCellStyle(style2);
			colRow6++;
		}
		
		row6Cell = (XSSFCell) row6.createCell((short) colRow6);
		row6Cell.setCellValue("Total");
		SetNormalCellBackColorH_green(workbook, row6Cell);
		colRow6++;
		
		int colorForTotal = 0;
		
	
	
		
		for(int i=0 ; i <totalLrbs;i++) {
			
			
			row6Cell = (XSSFCell) row6.createCell((short) colRow6);
			row6Cell.setCellValue("168");
			if (colorForTotal %2 == 0) {
				
		 		 SetNormalCellBackColorH_yellow(workbook, row6Cell);
		 				}
		 			else{
		 				SetNormalCellBackColorH_green(workbook, row6Cell);
		 			}
			
			colRow6++;
			
			
			
			row6Cell = (XSSFCell) row6.createCell((short) colRow6); 
			row6Cell.setCellValue("11.75");
			if (colorForTotal %2 == 0) {
				
		 		 SetNormalCellBackColorH_yellow(workbook, row6Cell);
		 				}
		 			else{
		 				SetNormalCellBackColorH_green(workbook, row6Cell);
		 			}
			
			colRow6++;
			
			
			
			row6Cell = (XSSFCell) row6.createCell((short) colRow6);
			row6Cell.setCellValue("0.0114656");
			if (colorForTotal %2 == 0) {
				
		 		 SetNormalCellBackColorH_yellow(workbook, row6Cell);
		 				}
		 			else{
		 				SetNormalCellBackColorH_green(workbook, row6Cell);
		 			}
			colRow6++;
			
			
			row6Cell = (XSSFCell) row6.createCell((short) colRow6);
			row6Cell.setCellValue("2025456");	 
			colRow6++;
			if (colorForTotal %2 == 0) {
				
		 		 SetNormalCellBackColorH_yellow(workbook, row6Cell);
		 				}
		 			else{
		 				SetNormalCellBackColorH_green(workbook, row6Cell);
		 			}
			
			colorForTotal++;
			}
			
			
			
			
			}
		
		/*******************************
		 * ROW 6 End
		 *******************************/
		
		
		
		
		
		/*******************************
		 * ROW 6 Start
		 *******************************/
		
//		RowCount++;
//		XSSFRow row6 = spreadsheet.createRow((short) RowCount);
//		XSSFCell row6Cell = (XSSFCell) row6.createCell((short) 0);
//		
//		int colRow6 = 0;
//		for(int i=0; i<columnNames.length; i++) {
//			row6Cell = (XSSFCell) row6.createCell((short) colRow6);
//			row6Cell.setCellValue("");
//			colRow6++;
//		}
//		
//		row6Cell = (XSSFCell) row6.createCell((short) colRow6);
//		row6Cell.setCellValue(TotalMobileOrders);
//		SetNormalCellBackColorH_yellow(workbook, row6Cell);
//		colRow6++;
//		
//		row6Cell = (XSSFCell) row6.createCell((short) colRow6);
//		row6Cell.setCellValue(TotalNonMobileOrders);
//		SetNormalCellBackColorH_yellow(workbook, row6Cell);
//		colRow6++;
//		
//		row6Cell = (XSSFCell) row6.createCell((short) colRow6);
//		row6Cell.setCellValue(TotalOrders);
//		SetNormalCellBackColorH_yellow(workbook, row6Cell);
//		colRow6++;
//		
//		 
//		
//		row6Cell = (XSSFCell) row6.createCell((short) colRow6);
//		row6Cell.setCellValue("");
//		SetNormalCellBackColorH_yellow(workbook, row6Cell);
//		colRow6++;

		/*******************************
		 * ROW 6 End
		 *******************************/



		
		// Auto Sizing Column grandAttainmentTotal

		for (int i = 0; i < 50 ;i++) {
			// System.out.println("Auto Sizing - "+i);
			try {
				spreadsheet.autoSizeColumn(i);
			} catch (Exception e) {
				System.out.println(i);
				e.printStackTrace();
			}
		}

		/*
		 * for (int x = 0; x < spreadsheet.getRow(0).getPhysicalNumberOfCells(); x++) {
		 * spreadsheet.autoSizeColumn(x); }
		 */

		FileOutputStream out = new FileOutputStream(new File(filename));
		workbook.write(out);
		out.close();
		// System.out.println(
		// "typesofcells.xlsx written successfully");

		ds.dropConnection();
	}

	public void Set2ndHeaderBackColorssSecondarySales(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(0, 0, 255));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setAlignment(HorizontalAlignment.CENTER);
		style61.setVerticalAlignment(VerticalAlignment.CENTER);
		style61.setWrapText(true);
		headercell.setCellStyle((CellStyle) style61);
		final XSSFFont font = workbook.createFont();
		font.setFontName("Calibri");
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont((org.apache.poi.ss.usermodel.Font) font);
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
	}

	public void Set2ndHeaderBackColorssSecondarySales_1(final XSSFWorkbook workbook, final XSSFCell headercell,
			XSSFCellStyle style61) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(0, 0, 255));

		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setAlignment(HorizontalAlignment.CENTER);
		style61.setVerticalAlignment(VerticalAlignment.CENTER);
		style61.setWrapText(true);
		headercell.setCellStyle((CellStyle) style61);
		final XSSFFont font = workbook.createFont();
		font.setFontName("Calibri");
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont((org.apache.poi.ss.usermodel.Font) font);
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
	}

	public void Set2ndHeaderBackColorssSecondarySales_brown(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(165, 42, 42));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setAlignment(HorizontalAlignment.CENTER);
		style61.setVerticalAlignment(VerticalAlignment.CENTER);
		style61.setWrapText(true);
		headercell.setCellStyle((CellStyle) style61);
		final XSSFFont font = workbook.createFont();
		font.setFontName("Calibri");
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont((org.apache.poi.ss.usermodel.Font) font);
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
	}

	public void Set2ndHeaderBackColorssSecondarySales_green(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(0, 128, 0));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setAlignment(HorizontalAlignment.CENTER);
		style61.setVerticalAlignment(VerticalAlignment.CENTER);
		style61.setWrapText(true);
		headercell.setCellStyle((CellStyle) style61);
		final XSSFFont font = workbook.createFont();
		font.setFontName("Calibri");
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont((org.apache.poi.ss.usermodel.Font) font);
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
	}

	public void Set2ndHeaderBackColor_w(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style61.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style61);

		style61.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style61);

		XSSFFont font = workbook.createFont();
		font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		// font.setFontHeightInPoints((short)10);
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont(font);
	}

	public void Set2ndHeaderBackColor(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(162, 162, 163));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style61.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style61);

		style61.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style61);

		XSSFFont font = workbook.createFont();
		font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		// font.setFontHeightInPoints((short)10);
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont(font);
	}

	public void Set2ndHeaderBackColorOrderKPI(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(95, 149, 153));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		/*
		 * style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		 * style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
		 * style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
		 * style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		 * style61.setAlignment(CellStyle.ALIGN_CENTER);
		 * headercell.setCellStyle(style61);
		 */
		style61.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style61);

		XSSFFont font = workbook.createFont();
		font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		// font.setFontHeightInPoints((short)10);
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont(font);
	}

	public void Set2ndHeaderBackColorDeliveryKPI(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(128, 95, 153));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		/*
		 * style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		 * style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
		 * style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
		 * style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		 * style61.setAlignment(CellStyle.ALIGN_CENTER);
		 * headercell.setCellStyle(style61);
		 */
		style61.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style61);

		XSSFFont font = workbook.createFont();
		font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		// font.setFontHeightInPoints((short)10);
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont(font);
	}

	public void Set3rdHeaderBackColor(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(227, 227, 227));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

//		style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style61.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style61);

		headercell.setCellStyle(style61);
	}

	public void Set3rdHeaderBackColorOrder(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(202, 232, 234));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
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

	public void Set3rdHeaderBackColorDelivery(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(240, 230, 247));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
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

	public void SetNormalCellBlueColor(XSSFWorkbook workbook, XSSFCell headercell, XSSFCellStyle style61) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));
		XSSFColor fontColor = new XSSFColor(new java.awt.Color(246, 253, 253));

		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style61.setAlignment(CellStyle.ALIGN_LEFT);
		headercell.setCellStyle(style61);

		XSSFFont font = workbook.createFont();// Create font
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(fontColor);
		style61.setFont(font);// set it to bold

		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

		headercell.setCellStyle(style61);
	}

	public void SetNormalCellBackColor_1(final XSSFWorkbook workbook, final XSSFCell headercell,
			XSSFCellStyle style61) {

		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(255, 255, 255));

		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 3);
		headercell.setCellStyle((CellStyle) style61);
		style61.setAlignment(HorizontalAlignment.CENTER);
		style61.setVerticalAlignment(VerticalAlignment.CENTER);
		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.##"));
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetNormalCellBackColor(XSSFWorkbook workbook, XSSFCell headercell, XSSFCellStyle style61) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));

		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style61.setAlignment(CellStyle.ALIGN_LEFT);
		headercell.setCellStyle(style61);

		// style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

		headercell.setCellStyle(style61);
	}

	public void SetNormalCellBackColorH_yellow(XSSFWorkbook workbook, XSSFCell headercell, XSSFCellStyle style61) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 0));

		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style61.setAlignment(CellStyle.ALIGN_LEFT);

		XSSFFont font = workbook.createFont();// Create font
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style61.setFont(font);// set it to bold

		headercell.setCellStyle(style61);

		// style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

		headercell.setCellStyle(style61);
	}

	public void SetNormalCellBackColorH(XSSFWorkbook workbook, XSSFCell headercell, XSSFCellStyle style61) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));

		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style61.setAlignment(CellStyle.ALIGN_LEFT);

		XSSFFont font = workbook.createFont();// Create font
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style61.setFont(font);// set it to bold

		headercell.setCellStyle(style61);

		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

		headercell.setCellStyle(style61);
	}

	public void SetNormalCellBackColor12(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 192, 197));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
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

	public void SetNormalCellBackColorGray(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(245, 245, 245));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style61.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style61);

		// style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

		headercell.setCellStyle(style61);
	}

	public void SetNormalCellBackColorBlue(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(246, 253, 253));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
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

	public void SetNormalCellBackColorBlueOneDecimal(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(246, 253, 253));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
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

	public void SetNormalCellBackColorPink(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 246, 249));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
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

	public void SetNormalCellBackColorPinkOneDecimal(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 246, 249));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
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

	public void SetNormalCellBackColorPercent(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
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

	public void SetNormalCellBackColorPercentRed(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 192, 197));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
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

	public void SetNormalCellBackColorPercentBlue(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(246, 253, 253));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
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

	public void SetNormalCellBackColorPercentPink(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 246, 249));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
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

	public void SetNormalCellBackColorLeft(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
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

	public void SetNormalCellBackColorLeftAltGray(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(245, 245, 245));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
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

	public void SetNormalCellBackColorLeftTotal(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 203, 173));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
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

	public void SetNormalCellBackColorCenterTotal(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 203, 173));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
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

	public void SetNormalCellBackColorRed(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 214, 231));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
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

	public void SetNormalCellBackColorRed12(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(192, 0, 0));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
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

	public void SetBorder(XSSFWorkbook workbook, XSSFCell headercell) {
		XSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style);
	}

	public void SetNormalCellBackColorLeft2(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
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

	public void Set2ndHeaderBackColorOrange(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 140, 0));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style61.setAlignment(CellStyle.ALIGN_LEFT);
		headercell.setCellStyle(style61);

	}

	public void Set2ndHeaderBackColorBrownKPI(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(78, 53, 36));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style61.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style61);

		XSSFFont font = workbook.createFont();
		font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		// font.setFontHeightInPoints((short)10);
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont(font);
	}

	public void Set2ndHeaderBackColorGray(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(162, 162, 163));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style61.setAlignment(CellStyle.ALIGN_LEFT);
		headercell.setCellStyle(style61);

	}

	public void SetNormalCellBackColorH_yellow(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 0));
		XSSFCellStyle style61 = workbook.createCellStyle();

		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style61.setAlignment(CellStyle.ALIGN_CENTER);

		XSSFFont font = workbook.createFont();// Create font
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style61.setFont(font);// set it to bold

		headercell.setCellStyle(style61);

		//style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

		headercell.setCellStyle(style61);
	}

	
 
	
	
	
	public void SetNormalCellBackColorH_blue(XSSFWorkbook workbook, XSSFCell headercell, XSSFCellStyle style61) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(0, 0, 255));

		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style61.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style61);

		style61.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style61);

		XSSFFont font = workbook.createFont();
		font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		// font.setFontHeightInPoints((short)10);
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont(font);
	}

	public void SetNormalCellBackColorH_green(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(0, 128, 0));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style61.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style61);

		style61.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style61);

		XSSFFont font = workbook.createFont();
		font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		// font.setFontHeightInPoints((short)10);
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont(font);
	}

	public void SetNormalCellBackColorH_blue(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(0, 0, 255));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style61.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style61);

		style61.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style61);

		XSSFFont font = workbook.createFont();
		font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		// font.setFontHeightInPoints((short)10);
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont(font);
	}

	public void Set2ndHeaderBackColorWhite(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style61.setAlignment(CellStyle.ALIGN_LEFT);
		headercell.setCellStyle(style61);

	}

//	public XWPFHyperlinkRun createHyperlinkRunToAnchor(XWPFParagraph paragraph, String anchor) throws Exception {
//		CTHyperlink cthyperLink = paragraph.getCTP().addNewHyperlink();
//		cthyperLink.setAnchor(anchor);
//		cthyperLink.addNewR();
//		return new XWPFHyperlinkRun(cthyperLink, cthyperLink.getRArray(0), paragraph);
//	}
//
//	static XWPFParagraph createBookmarkedParagraph(XWPFDocument document, String anchor, int bookmarkId) {
//		XWPFParagraph paragraph = document.createParagraph();
//		CTBookmark bookmark = paragraph.getCTP().addNewBookmarkStart();
//		bookmark.setName(anchor);
//		bookmark.setId(BigInteger.valueOf(bookmarkId));
//		XWPFRun run = paragraph.createRun();
//		paragraph.getCTP().addNewBookmarkEnd().setId(BigInteger.valueOf(bookmarkId));
//		return paragraph;
//	}

}
