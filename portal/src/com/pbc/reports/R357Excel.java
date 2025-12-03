package com.pbc.reports;

import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.pbc.util.AlmoizDateUtils;
import com.pbc.util.Datasource;
import com.pbc.util.DateUtils;
import com.pbc.util.ExcelColorsInHexa;
import com.pbc.util.ExcelFontAlign;
import com.pbc.util.ExcelFontColors;
import com.pbc.util.Utilities;

import java.io.File;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.pbc.util.POIExcelUtils;

public class R357Excel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath() + "/CashInflowSummary.pdf";

	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;

	public void createPdf(String filename, Date StartDate, Date EndDate, String Distributors, String Brands,
			String OrderBookers, String SKUIDs, String ASMs, String TSOs, String RSMs, String Regions, String City,
			String Channels)
			throws IOException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

		ds.createConnection();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("Order Distance Report");

		ExcelColorsInHexa excelColors = new ExcelColorsInHexa();
		ExcelFontColors excelFontColors = new ExcelFontColors();
		ExcelFontAlign fontAlign = new ExcelFontAlign();

		XSSFCellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();

		XSSFCellStyle styleLeft = POIExcelUtils.getStyleWithOutCell(workbook, excelColors.getWhite(), style, font,
				excelFontColors.getBlack(), true, false, fontAlign.getLeft());
		XSSFCellStyle styleRight = POIExcelUtils.getStyleWithOutCell(workbook, excelColors.getWhite(), style, font,
				excelFontColors.getBlack(), true, false, fontAlign.getRight());
		XSSFCellStyle styleCenter = POIExcelUtils.getStyleWithOutCell(workbook, excelColors.getWhite(), style, font,
				excelFontColors.getBlack(), true, false, fontAlign.getCenter());

		int RowCount = 0;
		int Col = 0;

		XSSFRow Row = spreadsheet.createRow((short) RowCount);
		XSSFCell Cell = (XSSFCell) Row.createCell((short) 0);

		/*******************************
		 * ROW 1 Start
		 *******************************/
		Row = spreadsheet.createRow((short) RowCount);
		Cell = (XSSFCell) Row.createCell((short) 0);

		Cell.setCellValue("R357 - Order Distance Report");

		spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, // first row (0-based)
				RowCount, // last row (0-based)
				0, // first column (0-based)
				2 // last column (0-based)
		));

		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getWhite(), style, font, excelFontColors.getBlack(),
				false, true, fontAlign.getLeft());
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
				2 // last column (0-based)
		));

		/*******************************
		 * ROW 2 End
		 *******************************/

		/***************************** Data ***************************************/

		String[] columnNames = { "Serial #", "Ref ID" , "visit Type", "Distributor ID", "Distributor Name", "Order Date", "PSR ID",
				"PSR Name", "City", "Region", "TSO Name", "ASM Name", "Outlet Name", "Channel", "Orders within 25m",
				"Orders within 50m", "Orders within 100m", "Orders within 200m", "Orders greater than 200m",
				"Orders where distance not available", "Actual Distance", "Total Orders by App", "Start Time",
				"End Time", "Shop Time", "Journey Time", "Comments" };

		/***************************** Data ***************************************/

		/*******************************
		 * ROW 3 Start
		 *******************************/

		RowCount++;
		Col = 0;
		Row = spreadsheet.createRow((short) RowCount);

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
		 * Row Dynamic Start
		 *******************************/

		// Filters

		String whereOrderBooker = (OrderBookers.length() > 0) ? " and psr_id in(" + OrderBookers + ")" : "";
		String whereRegions = (Regions.length() > 0) ? " and region_id in (" + Regions + ")" : "";
		String whereASMs = (ASMs.length() > 0) ? " and asm_id in (" + ASMs + ")" : "";
		String whereTSOs = (TSOs.length() > 0) ? " and tso_id in (" + TSOs + ")" : "";
		String whereRSMs = (RSMs.length() > 0) ? " and rsm_id in (" + RSMs + ")" : "";
		String whereCities = (City.length() > 0) ? " and city_id in (" + City + ")" : "";
		String whereChannels = (Channels.length() > 0) ? " and pci_channel_id in (" + Channels + ")" : "";

		String whereBrands = "";
		if (Brands.length() > 0) {
			int c = 0;
			String ids = "";
//			System.out.println(
//					"SELECT DISTINCT mop.id as id FROM mobile_order mo JOIN mobile_order_products mop ON mo.id = mop.id WHERE product_id IN (SELECT product_id FROM inventory_products_view WHERE lrb_type_id IN ("
//							+ Brands + ")) and distributor_id in (" + Distributors + ") AND mobile_timestamp BETWEEN "
//							+ Utilities.getSQLDate(StartDate) + " AND " + Utilities.getSQLDateNext(EndDate));
			ResultSet rsBrands = s.executeQuery(
					"SELECT DISTINCT mop.id as id FROM mobile_order mo JOIN mobile_order_products mop ON mo.id = mop.id WHERE product_id IN (SELECT product_id FROM inventory_products_view WHERE lrb_type_id IN ("
							+ Brands + ")) and distributor_id in (" + Distributors + ") AND mobile_timestamp BETWEEN "
							+ Utilities.getSQLDate(StartDate) + " AND " + Utilities.getSQLDateNext(EndDate));
			while (rsBrands.next()) {
				ids = (c == 0) ? ids + rsBrands.getString("id") : ids + "," + rsBrands.getString("id");
				c++;
			}
			whereBrands = (ids.length() > 0) ? " and ref_id in (" + ids + ")" : "";
		}

		String whereSKUs = "";
		if (SKUIDs.length() > 0) {
			int c = 0;
			String ids = "";
//			System.out.println(
//					"SELECT DISTINCT mop.id as id FROM mobile_order mo JOIN mobile_order_products mop ON mo.id = mop.id WHERE product_id IN ("
//							+ SKUIDs + ") and distributor_id in (" + Distributors + ") AND mobile_timestamp BETWEEN "
//							+ Utilities.getSQLDate(StartDate) + " AND " + Utilities.getSQLDateNext(EndDate));
			ResultSet rsSKUs = s.executeQuery(
					"SELECT DISTINCT mop.id as id FROM mobile_order mo JOIN mobile_order_products mop ON mo.id = mop.id WHERE product_id IN ("
							+ SKUIDs + ") and distributor_id in (" + Distributors + ") AND mobile_timestamp BETWEEN "
							+ Utilities.getSQLDate(StartDate) + " AND " + Utilities.getSQLDateNext(EndDate));
			while (rsSKUs.next()) {
				ids = (c == 0) ? ids + rsSKUs.getString("id") : ids + "," + rsSKUs.getString("id");
				c++;
			}
			whereSKUs = (ids.length() > 0) ? " and ref_id in (" + ids + ")" : "";
		}

		int serialNumber = 1;

//		System.out.println("SELECT * FROM pep.psr_2024_visits where mobile_timestamp between "
//				+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
//				+ " and distributor_id in (" + Distributors + ")" + whereOrderBooker + whereRegions + whereASMs
//				+ whereTSOs + whereRSMs + whereCities + whereChannels + whereBrands + whereSKUs);

		ResultSet rsData = s.executeQuery("SELECT * FROM pep.psr_2024_visits where mobile_timestamp between "
				+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
				+ " and distributor_id in (" + Distributors + ")" + whereOrderBooker + whereRegions + whereASMs
				+ whereTSOs + whereRSMs + whereCities + whereChannels + whereBrands + whereSKUs );
		while (rsData.next()) {

			double distance = rsData.getDouble("visit_distance");

			RowCount++;
			Row = spreadsheet.createRow((int) RowCount);
			Col = 0;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(serialNumber);
			Cell.setCellStyle(styleLeft);
			Col++;
			
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("ref_id"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("visit_status"));
			Cell.setCellStyle(styleCenter);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("distributor_id"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("distributor_name"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(AlmoizDateUtils.getDisplayDateFormatMMDDYYYY(rsData.getTimestamp("mobile_timestamp")));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("psr_id"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("psr_name"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("city_name"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("region_name"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("tso_name"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("asm_name"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("outlet_id") + "-" + rsData.getString("outlet_name"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("channel_name"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue((distance > 0 && distance <= 25) ? 1 : 0);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue((distance > 25 && distance <= 50) ? 1 : 0);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue((distance > 50 && distance <= 100) ? 1 : 0);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue((distance > 100 && distance <= 200) ? 1 : 0);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue((distance > 200) ? 1 : 0);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue((distance <= 0) ? distance : 0);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(distance);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getInt("total_orders_day"));
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Utilities.getDisplayTimeFormat(rsData.getTime("start_time")));
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Utilities.getDisplayTimeFormat(rsData.getTime("end_time")));
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getInt("shop_time"));
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getDouble("journey_time"));
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("comments"));
			Cell.setCellStyle(styleRight);
			Col++;

			serialNumber++;
		}

		/*******************************
		 * Row Dynamic End
		 *******************************/

		// Auto Sizing Column grandAttainmentTotal

		for (int i = 0; i < 30; i++) {
			// System.out.println("Auto Sizing - "+i);
			try {
				spreadsheet.autoSizeColumn(i);
			} catch (Exception e) {
				System.out.println(i);
				e.printStackTrace();
			}
		}

		FileOutputStream out = new FileOutputStream(new File(filename));
		workbook.write(out);
		workbook.close();
		out.close();

		s.close();
		s2.close();
		s3.close();
		ds.dropConnection();
	}

}
