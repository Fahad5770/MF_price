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

public class R360Excel {

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

	public R360Excel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

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

	public void createPdf(String filename, Date StartDate, Date EndDate, String Distributors, String OrderBookers,
			String TSOs, String ASMs, String Regions, String Channels, String Brands, String SKUs)
			throws IOException, SQLException, ParseException {

//		System.out.println("StartDate : "+StartDate);
//		System.out.println("EndDate : "+EndDate);
//		System.out.println("Distributors : "+Distributors);
//		System.out.println("OrderBookers : "+OrderBookers);
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

		String[] columnNames = { "Journal No.", "Date", "To Distributor ID", 
				"To Distributor ID", "From Distributor Name" ,"From Distributor Name", "Reason", "Created By", "Posted Date & Time" };


		/***************************** Data ***************************************/

		/*******************************
		 * ROW 3 Start
		 *******************************/
		RowCount++;

		Row = spreadsheet.createRow((short) RowCount);
		Cell = (XSSFCell) Row.createCell((short) 0);

		Col = 0;
		for (String title : columnNames) {
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

		System.out.println(
				"select id,created_on , created_by , from_distributor_id , to_distributor_id,Remarks from inventory_distributor_stock_transfer where from_distributor_id in ("
						+ Distributors + ") and  created_on between " + Utilities.getSQLDate(StartDate) + " and "
						+ Utilities.getSQLDateNext(EndDate));
		ResultSet rs2 = s2.executeQuery(
				"select id,created_on , created_by , from_distributor_id , to_distributor_id,Remarks from inventory_distributor_stock_transfer where from_distributor_id in ("
						+ Distributors + ") and  created_on between " + Utilities.getSQLDate(StartDate) + " and "
						+ Utilities.getSQLDateNext(EndDate));

		while (rs2.next()) {
			System.out.println("inside Loop");
			int ID = rs2.getInt("id");
			String CreatedOn = rs2.getString("created_on");
			String Createdby = rs2.getString("created_by");
			String FromDisID = rs2.getString("from_distributor_id");
			String ToDisID = rs2.getString("to_distributor_id");
			String Remarks = rs2.getString("Remarks");
			System.out.println("Createdby" + Createdby);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date createdOnDate = dateFormat.parse(CreatedOn);

			// Now you can format the date in the desired output format
			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd"); // Change this to your desired output
																				// format
			String formattedDate = outputFormat.format(createdOnDate);
			String FromDisName = "";
			ResultSet rs3 = s3.executeQuery("select name from common_distributors where distributor_id=" + FromDisID);
			if (rs3.first()) {
				FromDisName = rs3.getString("name");
				// Grand_Total += Total;
			}
			String ToDisName = "";
			ResultSet rs4 = s.executeQuery("select name from common_distributors where distributor_id=" + ToDisID);
			if (rs4.first()) {
				ToDisName = rs4.getString("name");
				// Grand_Total += Total;
			}
			String CreatedbyName = "";
			System.out.println("select DISPLAY_NAME from users where id =" + Createdby);
			ResultSet rs5 = s.executeQuery("select DISPLAY_NAME from users where id =" + Createdby);
			if (rs5.first()) {
				CreatedbyName = rs5.getString("DISPLAY_NAME");
				// Grand_Total += Total;
			}

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
			Cell.setCellValue(ToDisName);
			Cell.setCellStyle(styleRight);
			Col++;

			// Mobile Timestamp Date
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(ToDisID);
			Cell.setCellStyle(styleLeft);
			Col++;

			// Mobile Timestamp Time
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(FromDisName);
			Cell.setCellStyle(styleRight);
			Col++;

			// Crated on Date
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Remarks);
			Cell.setCellStyle(styleRight);
			Col++;

			// Created on Time
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(CreatedbyName);
			Cell.setCellStyle(styleRight);
			Col++;

			// Outlet Name
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(CreatedOn);
			Cell.setCellStyle(styleLeft);
			Col++;
		}

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
