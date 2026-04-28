package com.pbc.reports;

import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.pbc.inventory.StockPosting;
import com.pbc.util.AlmoizDateUtils;
import com.pbc.util.Datasource;
import com.pbc.util.ExcelColorsInHexa;
import com.pbc.util.ExcelFontAlign;
import com.pbc.util.ExcelFontColors;
import com.pbc.util.Utilities;

import java.io.File;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.pbc.util.POIExcelUtils;

public class R380Excel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath() + "/CashInflowSummary.pdf";

	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	Statement s5;

	public void createPdf(String filename, Date StartDate, Date EndDate, String Distributors)
			throws IOException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

		ds.createConnection();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
		s5 = ds.createStatement();

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
		double primarySales = 0.0;
		double pendingVal = 0.0;
		double secondarySales = 0.0;

		XSSFRow Row = spreadsheet.createRow((short) RowCount);
		XSSFCell Cell = (XSSFCell) Row.createCell((short) 0);

		/*******************************
		 * ROW 1 Start
		 *******************************/
		Row = spreadsheet.createRow((short) RowCount);
		Cell = (XSSFCell) Row.createCell((short) 0);

		Cell.setCellValue("R380 - Stock Report");

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

		String[] columnNames = { "Distributor Id", "Distributor Name", "Town", "Region", "Primary Sales", "Pending DNs",
				"Total Proimary Sales", "Secondary Sales", "Stock Transfer", "Opening Stock", "Closing Stock",
				"Stock Check", "Difference Stock" };

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

		System.out.println(
				"SELECT *, (select region_name from common_regions cr where cr.region_id = cd.region_id) as region_name FROM common_distributors cd where distributor_id in ("
						+ Distributors + ")");
		ResultSet rsData = s.executeQuery(
				"SELECT *, (select region_name from common_regions cr where cr.region_id = cd.region_id) as region_name FROM common_distributors cd where distributor_id in ("
						+ Distributors + ")");
		while (rsData.next()) {

			RowCount++;
			Row = spreadsheet.createRow((int) RowCount);
			Col = 0;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getInt("distributor_id"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("name"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("city"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("region_name"));
			Cell.setCellStyle(styleLeft);
			Col++;

			// Primary Sale Query

			System.out.println(
					"SELECT sum(raw_cases * ipv.liquid_in_ml) primary_sales_tons " + "FROM inventory_delivery_note idn "
							+ "JOIN inventory_delivery_note_products idnp ON idn.delivery_id = idnp.delivery_id "
							+ "JOIN inventory_products_view ipv ON idnp.product_id = ipv.product_id "
							+ "WHERE idn.created_on BETWEEN " + Utilities.getSQLDate(StartDate) + " AND "
							+ Utilities.getSQLDateNext(EndDate) + " AND is_received = 1 AND distributor_id = "
							+ rsData.getInt("distributor_id") + " ");

			ResultSet rsPrimarySalesInTons = s2.executeQuery(
					"SELECT sum(raw_cases * ipv.liquid_in_ml) primary_sales_tons " + "FROM inventory_delivery_note idn "
							+ "JOIN inventory_delivery_note_products idnp ON idn.delivery_id = idnp.delivery_id "
							+ "JOIN inventory_products_view ipv ON idnp.product_id = ipv.product_id "
							+ "WHERE idn.created_on BETWEEN " + Utilities.getSQLDate(StartDate) + " AND "
							+ Utilities.getSQLDateNext(EndDate) + " AND is_received = 1 AND distributor_id = "
							+ rsData.getInt("distributor_id") + " ");

			Cell = Row.createCell(Col);

			if (rsPrimarySalesInTons.next()) {
				String value = rsPrimarySalesInTons.getString("primary_sales_tons");
				primarySales = (value == null ? 0.0 : Double.parseDouble(value));
				Cell.setCellValue(primarySales);
			} else {
				Cell.setCellValue("0");
			}
			Cell.setCellStyle(styleLeft);
			Col++;

			// Pending DNs

			System.out.println("SELECT sum(raw_cases * ipv.liquid_in_ml) pending_Dns__sales_tons "
					+ "FROM inventory_delivery_note idn "
					+ "JOIN inventory_delivery_note_products idnp ON idn.delivery_id = idnp.delivery_id "
					+ "JOIN inventory_products_view ipv ON idnp.product_id = ipv.product_id "
					+ "WHERE idn.created_on BETWEEN " + Utilities.getSQLDate(StartDate) + " AND "
					+ Utilities.getSQLDateNext(EndDate) + " AND is_received = 0 AND distributor_id = "
					+ rsData.getInt("distributor_id") + " ");

			ResultSet rsPendingDNsInTons = s3
					.executeQuery("SELECT sum(raw_cases * ipv.liquid_in_ml) pending_Dns__sales_tons "
							+ "FROM inventory_delivery_note idn "
							+ "JOIN inventory_delivery_note_products idnp ON idn.delivery_id = idnp.delivery_id "
							+ "JOIN inventory_products_view ipv ON idnp.product_id = ipv.product_id "
							+ "WHERE idn.created_on BETWEEN " + Utilities.getSQLDate(StartDate) + " AND "
							+ Utilities.getSQLDateNext(EndDate) + " AND is_received = 0 AND distributor_id = "
							+ rsData.getInt("distributor_id") + " ");

			Cell = Row.createCell(Col);

			if (rsPendingDNsInTons.next()) {
				String value = rsPendingDNsInTons.getString("pending_Dns__sales_tons");
				pendingVal = (value == null ? 0.0 : Double.parseDouble(value));
				Cell.setCellValue(pendingVal);
			} else {
				Cell.setCellValue("0");
			}

			Cell.setCellStyle(styleLeft);
			Col++;

			double total = primarySales + pendingVal;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(total);
			Cell.setCellStyle(styleLeft);
			Col++;

			// Secondary Sales

			ResultSet rsSechondarySales = s3
					.executeQuery("SELECT SUM(raw_cases * ipv.liquid_in_ml) AS secondarySalesInTons "
							+ "FROM inventory_sales_invoices_products mop "
							+ "JOIN inventory_products_view ipv ON mop.product_id = ipv.product_id "
							+ "WHERE mop.id IN ( " + "   SELECT id FROM inventory_sales_invoices "
							+ "   WHERE created_on BETWEEN " + Utilities.getSQLDate(StartDate) + "   AND "
							+ Utilities.getSQLDateNext(EndDate) + "   AND distributor_id = "
							+ rsData.getInt("distributor_id") + ")");

			Cell = Row.createCell(Col);

			if (rsSechondarySales.next()) {
				String value = rsSechondarySales.getString("secondarySalesInTons");
				secondarySales = (value == null ? 0.0 : Double.parseDouble(value));
				Cell.setCellValue(secondarySales);
			} else {
				Cell.setCellValue(0);
			}

			Cell.setCellStyle(styleLeft);
			Col++;

			// Stock Transfer

			double issueStock = 0;
			double recieveStock = 0;

			System.out.println("select sum(raw_cases * liquid_in_ml)  fromStockTransfer from inventory_distributor_stock_transfer idst join inventory_distributor_stock_transfer_products idstp where idst.id = idstp.id"
					+ " and from_distributor_id = " + rsData.getInt("distributor_id")
					+ " and created_on between " + Utilities.getSQLDate(StartDate) + "   AND "
					+ Utilities.getSQLDateNext(EndDate) + " ");
			ResultSet rsStockIssue = s3.executeQuery(
					"select sum(raw_cases * liquid_in_ml)  fromStockTransfer from inventory_distributor_stock_transfer idst join inventory_distributor_stock_transfer_products idstp where idst.id = idstp.id"
							+ " and from_distributor_id = " + rsData.getInt("distributor_id")
							+ " and created_on between " + Utilities.getSQLDate(StartDate) + "   AND "
							+ Utilities.getSQLDateNext(EndDate) + " ");
			if (rsStockIssue.first()) {
				issueStock = rsStockIssue.getDouble("fromStockTransfer");
			}

			System.out.println("select sum(raw_cases * liquid_in_ml)  toStockTransfer from inventory_distributor_stock_transfer idst join inventory_distributor_stock_transfer_products idstp where idst.id = idstp.id"
					+ " and to_distributor_id = " + rsData.getInt("distributor_id") + " and created_on between "
					+ Utilities.getSQLDate(StartDate) + "   AND " + Utilities.getSQLDateNext(EndDate) + " ");
			ResultSet rsStockRecieve = s3.executeQuery(
					"select sum(raw_cases * liquid_in_ml)  toStockTransfer from inventory_distributor_stock_transfer idst join inventory_distributor_stock_transfer_products idstp where idst.id = idstp.id"
							+ " and to_distributor_id = " + rsData.getInt("distributor_id") + " and created_on between "
							+ Utilities.getSQLDate(StartDate) + "   AND " + Utilities.getSQLDateNext(EndDate) + " ");

			if (rsStockRecieve.first()) {
				recieveStock = rsStockRecieve.getDouble("toStockTransfer");
			}

			double stockTransfer = issueStock - recieveStock;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(stockTransfer);
			Cell.setCellStyle(styleLeft);
			Col++;

			// Stock Posting

			
			double OpeningUnits = 0;
			double ClosingUnits = 0;
			double PendingDispatch = 0;
			double totalclosingingUnits = 0;
			double totalOpeningUnits = 0;
			double totalpendingDispatch = 0;

			StockPosting sp = new StockPosting(true);

			ResultSet rsProductId = s4
					.executeQuery("SELECT product_id , liquid_in_ml from inventory_products_view  where category_id = 1");
			while (rsProductId.next()) {
				int	ProductID = rsProductId.getInt(1);
				double	LiquidMl = rsProductId.getDouble(2);
				
				Date OpeningDate = DateUtils.addDays(StartDate, -1);
				
				OpeningUnits =  LiquidMl * sp.getClosingBalance(rsData.getInt("distributor_id"), ProductID, OpeningDate);
				
				System.out.println("Liquid in ml : " + ProductID  + " - " +  OpeningUnits );
				
				totalOpeningUnits += OpeningUnits;

				ClosingUnits = LiquidMl * sp.getClosingBalance(rsData.getInt("distributor_id"), ProductID, EndDate);
				totalclosingingUnits += ClosingUnits;

				PendingDispatch = sp.getBalanceafterdispatch(rsData.getInt("distributor_id"), ProductID);
				totalpendingDispatch += PendingDispatch;

			}
			
			double totalUnits = totalclosingingUnits - totalpendingDispatch;

			// Opening Stock

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(totalOpeningUnits);
			Cell.setCellStyle(styleLeft);
			Col++;

			// closing stock

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(totalUnits);
			Cell.setCellStyle(styleLeft);
			Col++;
			
			// Stock Check
			
			double stockCheck = 0;
			
			stockCheck = ( primarySales +  totalOpeningUnits + stockTransfer ) - secondarySales;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(stockCheck);
			Cell.setCellStyle(styleLeft);
			Col++;

			// Difference Stock 
			double differanceStock = totalUnits - stockCheck;
			
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(differanceStock);
			Cell.setCellStyle(styleLeft);
			Col++;

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
