package com.pbc.reports;

import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.itextpdf.text.Font;
import com.pbc.util.Datasource;
import com.pbc.util.ExcelColorsInHexa;
import com.pbc.util.ExcelFontAlign;
import com.pbc.util.ExcelFontColors;
import com.pbc.util.POIExcelUtils;
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

public class R361Excel {

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

	public R361Excel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

		ds.createConnectionToReplica();
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

	public void createPdf(String filename, Date StartDate, Date EndDate,
			String Regions,String FromDistributorIDs, String ToDistributorIDs,String City)
			throws IOException, SQLException, ParseException {
		String whereCitiesDistributors = (City.length()>0)?" and idst.from_distributor_id IN (select  distributor_id from common_distributors where city_id in ("+City+")) and idst.to_distributor_id IN (select distributor_id from common_distributors where city_id in ("+City+"))":"";  

		String whereRegionDistributors = (Regions.length()>0)?" and idst.from_distributor_id IN (select distributor_id from common_distributors where region_id in ("+Regions+")) and idst.to_distributor_id IN (select distributor_id from common_distributors where region_id in ("+Regions+"))":"";  
//		System.out.println("StartDate : "+StartDate);
//		System.out.println("whereCitiesDistributors : "+whereCitiesDistributors);
//		System.out.println("City : "+City);
//		System.out.println("ToDistributorIDs : "+ToDistributorIDs);
//		System.out.println("ToDistributorIDs : "+ToDistributorIDs);
//		System.out.println("TSOs : "+TSOs);
//		System.out.println("ASMs : "+ASMs);
//		System.out.println("Regions : "+Regions);
//		System.out.println("Channels : "+Channels);
//		System.out.println("Brands : "+Brands);
//		System.out.println("SKUs : "+SKUs);

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("Distributor Stock Transfer");

		ExcelColorsInHexa excelColors = new ExcelColorsInHexa();
		ExcelFontColors excelFontColors = new ExcelFontColors();
		ExcelFontAlign fontAlign = new ExcelFontAlign();

		XSSFCellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();

		XSSFCellStyle styleLeft = POIExcelUtils.getStyleWithOutCell(workbook, excelColors.getWhite(), style, font,
				excelFontColors.getBlack(), true, false, fontAlign.getLeft());
		XSSFCellStyle styleRight = POIExcelUtils.getStyleWithOutCell(workbook, excelColors.getWhite(), style, font,
				excelFontColors.getBlack(), true, false, fontAlign.getRight());

		int RowCount = 0;
		int Col = 0;

		XSSFRow Row = spreadsheet.createRow((short) RowCount);

		// Report Heading

		/*******************************
		 * ROW 1 Start
		 *******************************/
		XSSFCell Cell = (XSSFCell) Row.createCell((short) 0);
		Cell.setCellValue("R360 - Distributor Stock Transfer");
		spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, // first row (0-based)
				RowCount, // last row (0-based)
				0, // first column (0-based)
				2 // last column (0-based)
		));

		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);// set it to bold
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getWhite(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getLeft());

		/*******************************
		 * ROW 1 End
		 *******************************/

		/*******************************
		 * ROW 2 Start
		 *******************************/
		RowCount++;

		// Printing Date

		Row = spreadsheet.createRow((short) RowCount);
		Cell = (XSSFCell) Row.createCell((short) 0);
		Cell.setCellValue("Period : " + Utilities.getDisplayDateFormat(StartDate) + " - "
				+ Utilities.getDisplayDateFormat(EndDate));
		spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, // first row (0-based)
				RowCount, // last row (0-based)
				0, // first column (0-based)
				3 // last column (0-based)
		));

		/*******************************
		 * ROW 2 End
		 *******************************/

		/***************************** Data ***************************************/

		String[] columnNames = { "Journal No.", "Date", 
				"To Distributor ID", "To Distributor Name","From Distributor ID", "From Distributor Name","SKU Name","Quantity","Amount","Reason", "Created By", "Posted Date & Time" };


		/***************************** Data ***************************************/

		/*******************************
		 * ROW 3 Start
		 *******************************/
		RowCount++;

		Row = spreadsheet.createRow((short) RowCount);
		Cell = (XSSFCell) Row.createCell((short) 0);

		Col = 0;
		
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("Journal No.");
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			Col++;
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("Date");
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			
			Col++;
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("To Distributor ID");
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			Col++;
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("To Distributor Name");
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			
			Col++;
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("From Distributor ID");
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			Col++;
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("From Distributor Name");
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			
			Col++;
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("SKU Name");
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, // first row (0-based)
					RowCount, // last row (0-based)
					Col, // first column (0-based)
					Col + 1 // last column (0-based)
			));
			Col += 2;
			//Col++;
			
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("Quantity");
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, // first row (0-based)
					RowCount, // last row (0-based)
					Col, // first column (0-based)
					Col + 2 // last column (0-based)
			));
			Col += 3;
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("Reason");
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			Col++;
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("Created By");
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			Col++;
			
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("Posted Date & Time");
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			Col++;
			
			
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("Units Amount");
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			Col++;
			
			
		String[] columnNames1 = { "", "", "", "",
				"", "", "Brand", "Package", "Bags","Units","Tons","","","","","" };


		/***************************** Data ***************************************/

		/*******************************
		 * ROW 3 Start
		 *******************************/
		RowCount++;

		Row = spreadsheet.createRow((short) RowCount);
		Cell = (XSSFCell) Row.createCell((short) 0);

		Col = 0;
		for (String title : columnNames1) {
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(title);
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			Col++;
		}
		/*******************************
		 * ROW 3 End
		 *******************************/

		/*******************************
		 * Row 5 Starting from here
		 ********************************/

		double 	Grand_Total = 0.0;
		//System.out.println("SELECT (SELECT name FROM common_distributors WHERE distributor_id = idst.to_distributor_id) AS name, (SELECT  name FROM common_distributors WHERE distributor_id = idst.from_distributor_id) AS name2, (SELECT  package_label  FROM inventory_products_view WHERE product_id = idstp.product_id) AS Package, (SELECT brand_label FROM inventory_products_view WHERE product_id = idstp.product_id) AS Brand, idst.from_distributor_id, idst.to_distributor_id,idstp.product_id, idstp.raw_cases, ,idstp.Raw_Cases_Amount,idstp.Units_Amount , idstp.units,  idstp.total_units, idstp.liquid_in_ml,idst.created_on,idst.created_by,idst.Remarks,idst.id FROM inventory_distributor_stock_transfer idst   INNER JOIN inventory_distributor_stock_transfer_products idstp ON idst.id = idstp.id WHERE from_distributor_id in ("+FromDistributorIDs+") and to_distributor_id in ("+ToDistributorIDs+") and  created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
		//ResultSet rs2 = s2.executeQuery("SELECT (SELECT name FROM common_distributors WHERE distributor_id = idst.to_distributor_id) AS name, (SELECT  name FROM common_distributors WHERE distributor_id = idst.from_distributor_id) AS name2, (SELECT  package_label  FROM inventory_products_view WHERE product_id = idstp.product_id) AS Package, (SELECT brand_label FROM inventory_products_view WHERE product_id = idstp.product_id) AS Brand, idst.from_distributor_id, idst.to_distributor_id,idstp.product_id, idstp.raw_cases,idstp.Raw_Cases_Amount,idstp.Units_Amount , idstp.units,  idstp.total_units, idstp.liquid_in_ml,idst.created_on,idst.created_by,idst.Remarks,idst.id FROM inventory_distributor_stock_transfer idst  INNER JOIN inventory_distributor_stock_transfer_products idstp ON idst.id = idstp.id WHERE from_distributor_id in ("+FromDistributorIDs+") and to_distributor_id in ("+ToDistributorIDs+") and  created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
		
		System.out.println("SELECT (SELECT name FROM common_distributors WHERE distributor_id = idst.from_distributor_id) AS name,"
				+ " (SELECT  name FROM common_distributors WHERE distributor_id = idst.to_distributor_id) AS name2, "
				+ " (SELECT  package_label  FROM inventory_products_view WHERE product_id = idstp.product_id) AS Package,"
				+ " (SELECT brand_label FROM inventory_products_view WHERE product_id = idstp.product_id) AS Brand,"
				+ "  (SELECT unit_per_catron FROM inventory_products_view  ip WHERE ip.product_id = idstp.product_id) AS unit_per_catron,"
				+ " (SELECT unit_per_sku FROM inventory_products_view  ip WHERE ip.product_id = idstp.product_id) AS unit_per_sku,"
				+ " idst.from_distributor_id, idst.to_distributor_id,idstp.product_id, idstp.raw_cases ,idstp.Raw_Cases_Amount,idstp.Units_Amount , idstp.units,  idstp.total_units, idstp.liquid_in_ml,idst.created_on,"
				+ " idst.created_by,idst.Remarks,idst.id FROM inventory_distributor_stock_transfer idst INNER JOIN inventory_distributor_stock_transfer_products idstp ON idst.id = idstp.id WHERE "
				+ " idst.from_distributor_id in ("+FromDistributorIDs+") and idst.to_distributor_id in ("+ToDistributorIDs+")"
				+ "and  created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+ whereCitiesDistributors+whereRegionDistributors);
		
		ResultSet rs2 = s2.executeQuery("SELECT (SELECT name FROM common_distributors WHERE distributor_id = idst.from_distributor_id) AS name,"
				+ " (SELECT  name FROM common_distributors WHERE distributor_id = idst.to_distributor_id) AS name2, "
				+ " (SELECT  package_label  FROM inventory_products_view WHERE product_id = idstp.product_id) AS Package,"
				+ " (SELECT brand_label FROM inventory_products_view WHERE product_id = idstp.product_id) AS Brand,"
				+ "  (SELECT unit_per_catron FROM inventory_products_view  ip WHERE ip.product_id = idstp.product_id) AS unit_per_catron,"
				+ " (SELECT unit_per_sku FROM inventory_products_view  ip WHERE ip.product_id = idstp.product_id) AS unit_per_sku,"
				+ "    (SELECT liquid_in_ml FROM inventory_products_view  ip WHERE ip.product_id = idstp.product_id) AS liquid_in_ml,"
				+ " idst.from_distributor_id, idst.to_distributor_id,idstp.product_id, idstp.raw_cases ,idstp.Raw_Cases_Amount,idstp.Units_Amount , idstp.units,  idstp.total_units,idst.created_on,"
				+ " idst.created_by,idst.Remarks,idst.id FROM inventory_distributor_stock_transfer idst INNER JOIN inventory_distributor_stock_transfer_products idstp ON idst.id = idstp.id WHERE "
				+ " idst.from_distributor_id in ("+FromDistributorIDs+") and idst.to_distributor_id in ("+ToDistributorIDs+")"
				+ "and  created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+ whereCitiesDistributors+whereRegionDistributors);
		while(rs2.next()){
			String CreatedOn = rs2.getString("created_on");
			String Createdby = rs2.getString("created_by");
			int ID = rs2.getInt("id");
			String NameFrom = rs2.getString("name2");
			String NameTo = rs2.getString("name");
			String Package = rs2.getString("Package");
			String Brand = rs2.getString("Brand");
			double Units =rs2.getDouble("units");
			//double RawCases = rs2.getDouble("raw_cases");
			double Unit_per_Cartoon = rs2.getDouble("unit_per_catron");
			double liquid_in_ml = rs2.getDouble("liquid_in_ml");
			String Remarks =rs2.getString("Remarks");
			String FromDisID = rs2.getString("from_distributor_id");
			String ToDisID = rs2.getString("to_distributor_id");
			double Raw_Cases_Amount = rs2.getDouble("Raw_Cases_Amount");
			double Unit_Amount = rs2.getDouble("Units_Amount");
			double UnitperSku = rs2.getDouble("unit_per_sku");
		//	String Units =rs2.getString("idstp.units");

			
			
			
			double Bags = Units / Unit_per_Cartoon;
			double Tons = liquid_in_ml * Units * UnitperSku;
			double amountValue = Tons;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date createdOnDate = dateFormat.parse(CreatedOn);

			// Now you can format the date in the desired output format
			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd"); // Change this to your desired output format
			String formattedDate = outputFormat.format(createdOnDate);
			//String formattedAmount = String.format(" %.3f", amountValue);
		//	String FromDisName = "";
/* 				ResultSet rs3 = s3.executeQuery("select name from common_distributors where distributor_id="+FromDisID);
			if(rs3.first()){
				FromDisName = rs3.getString("name");
				//Grand_Total += Total;
			}
			String ToDisName = "";
			ResultSet rs4 = s.executeQuery("select name from common_distributors where distributor_id="+ToDisID);
			if(rs4.first()){
				ToDisName = rs4.getString("name");
				//Grand_Total += Total;
			}
		 	String CreatedbyName = "";
			System.out.println("select DISPLAY_NAME from users where id ="+Createdby);
			ResultSet rs5 = s.executeQuery("select DISPLAY_NAME from users where id ="+Createdby);
			if(rs5.first()){
				CreatedbyName = rs5.getString("DISPLAY_NAME");
				//Grand_Total += Total;
			}	 */ 
		

			// Data

			RowCount++;
			Row = spreadsheet.createRow((short) RowCount);

			Col = 0;

			// Distributor Name
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(ID);
			Cell.setCellStyle(styleLeft);
			Col++;
			// Region
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(formattedDate);
			Cell.setCellStyle(styleRight);
			Col++;

			// Mobile Order No.
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(FromDisID);
			Cell.setCellStyle(styleLeft);
			Col++;

			// PSR Name
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(NameFrom);
			Cell.setCellStyle(styleRight);
			Col++;

			// Mobile Timestamp Date
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(ToDisID);
			Cell.setCellStyle(styleLeft);
			Col++;

			// Mobile Timestamp Time
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(NameTo);
			Cell.setCellStyle(styleRight);
			Col++;

			// Crated on Date
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Brand);
			Cell.setCellStyle(styleRight);
			Col++;

			// Created on Time
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Package);
			Cell.setCellStyle(styleRight);
			Col++;

			// Outlet Name
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Utilities.getDisplayCurrencyFormatTwoDecimalFixed(Bags));
			Cell.setCellStyle(styleLeft);
			Col++;
			// Mobile Timestamp Time
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Units);
			Cell.setCellStyle(styleRight);
			Col++;

			// Crated on Date
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Tons);
			Cell.setCellStyle(styleRight);
			Col++;

			// Created on Time
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Remarks);
			Cell.setCellStyle(styleRight);
			Col++;

			// Outlet Name
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Createdby);
			Cell.setCellStyle(styleLeft);
			Col++;
			
			// Outlet Name
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(CreatedOn);
			Cell.setCellStyle(styleLeft);
			Col++;
			
		
			
			// Outlet Name
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Unit_Amount);
			Cell.setCellStyle(styleLeft);
			Col++;
		}//

		/*******************************
		 * Row 5 will end here
		 *******************************/

		// Auto Sizing Column grandAttainmentTotal

		for (int i = 0; i < 20; i++) {
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
}
