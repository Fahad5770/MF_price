package com.pbc.reports;

import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.pbc.util.AlmoizDateUtils;
import com.pbc.util.Almoiz_Configs;
import com.pbc.util.Datasource;
import com.pbc.util.ExcelColorsInHexa;
import com.pbc.util.ExcelFontAlign;
import com.pbc.util.ExcelFontColors;
import com.pbc.util.POIExcelUtils;
import com.pbc.util.Utilities;

import java.io.File;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class R363Excel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath() + "/CashInflowSummary.pdf";

	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;

	public void createPdf(String filename, Date StartDate, Date EndDate, String Distributor, String Brands,
			String OrderBookers, String SKUIDs, String ASMs, String TSOs, String Channel, String Regions, String City)
			throws IOException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

		ds.createConnection();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("Brand-SKU wise sales");

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
		XSSFCell Cell = (XSSFCell) Row.createCell((short) 0);

		/*******************************
		 * ROW 1 Start
		 *******************************/
		Cell.setCellValue("R363 - Orders Punching Status Report");
		spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, // first row (0-based)
				RowCount, // last row (0-based)
				0, // first column (0-based)
				3 // last column (0-based)
		));

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

		/***************************** Data ***************************************/

		String[] columnNames = { "S.No", "Theia ID", "PSR Name", "Distributor Code", "Distributor Name", "Region",
				"TOWN", "TSO", "ASM" };

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
					excelFontColors.getBlack(), true, true, fontAlign.getLeft());
			Col++;
		}

		int mergeCol = Col;

		for (int i = 0; i < 4; i++) {
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("");
			Cell.setCellStyle(styleLeft);
			Col++;
		}

		Cell = (XSSFCell) Row.createCell((short) mergeCol);
		Cell.setCellValue("Mobile Orders %age");
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, // first row (0-based)
				RowCount, // last row (0-based)
				mergeCol, // first column (0-based)
				mergeCol + 3 // last column (0-based)
		));

		/*******************************
		 * ROW 3 End
		 *******************************/

		/*******************************
		 * ROW 4 Start
		 *******************************/

		RowCount++;
		Row = spreadsheet.createRow((short) RowCount);
		Cell = (XSSFCell) Row.createCell((short) 0);

		Col = 0;
		for (int i = 0; i < 9; i++) {
			Cell = (XSSFCell) Row.createCell((short) RowCount);
			Cell.setCellValue("");
			Cell.setCellStyle(styleLeft);
			Col++;
		}

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue("Mobile Orders");
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGreen(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		Col++;

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue("Non Mobile Orders");
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGreen(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		Col++;

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue("Total Orders");
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGreen(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		Col++;

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue("Mobile Orders %age");
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGreen(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		Col++;

		/*******************************
		 * ROW 4 End
		 *******************************/

		int TotalMobileOrders = 0;
		int TotalDeskSales = 0;
		int TotalOrders = 0;

		/*******************************
		 * ROW Dynamic Start
		 *******************************/

		String whereOrderBooker = (OrderBookers.length() > 0) ? " and pjpv.assigned_to in (" + OrderBookers + ")" : "";

		String whereCities = "";
		if (City.length() > 0) {
			int c = 0;
			String ids = "";
			ResultSet rsCity = s3.executeQuery("select distributor_id from common_distributors where city_id in ("
					+ City + ") and distributor_id in (" + Distributor + ")");
			while (rsCity.next()) {

				ids = (c == 0) ? ids + rsCity.getString("distributor_id")
						: ids + "," + rsCity.getString("distributor_id");
				c++;
			}

			whereCities = (ids.length() > 0) ? " and pjpv.distributor_id in (" + ids + ")" : "";
		}
		// System.out.println("whereCities : "+whereCities);

		String whereRegions = "";
		if (Regions.length() > 0) {
			int c = 0;
			String ids = "";
			ResultSet rsRegion = s3.executeQuery("select distributor_id from common_distributors where region_id in ("
					+ Regions + ") and distributor_id in (" + Distributor + ")");
			while (rsRegion.next()) {

				ids = (c == 0) ? ids + rsRegion.getString("distributor_id")
						: ids + "," + rsRegion.getString("distributor_id");
				c++;
			}
			whereRegions = (ids.length() > 0) ? " and pjpv.distributor_id in (" + ids + ")" : "";
		}
		// System.out.println("whereRegions : "+whereRegions);

		String whereASMs = "";
		if (ASMs.length() > 0) {
			int c = 0;
			String ids = "";
			// System.out.println("select distributor_id from common_distributors where
			// rsm_id in (" + ASMs + ") and distributor_id in (" + Distributor + ")");
			ResultSet rsASMs = s3.executeQuery("select distributor_id from common_distributors where rsm_id in (" + ASMs
					+ ") and distributor_id in (" + Distributor + ")");
			while (rsASMs.next()) {
				ids = (c == 0) ? ids + rsASMs.getString("distributor_id")
						: ids + "," + rsASMs.getString("distributor_id");
				c++;
			}

			whereASMs = (ids.length() > 0) ? " and pjpv.distributor_id in (" + ids + ")" : "";
		}

		// System.out.println("whereASMs : "+whereASMs);

		String whereTSOs = (TSOs.length() > 0) ? " and pjpv.asm_id in (" + TSOs + ")" : "";

		String whereChannels = (Channel.length() > 0) ? " and pjpv.channel_id in (" + Channel + ")" : "";

		String whereSkUs = (SKUIDs.length() > 0) ? " and isap.product_id in (" + SKUIDs + ")" : "";
		String whereBrands = (Brands.length() > 0)
				? " and isap.product_id in (select product_id from inventory_products where lrb_type_id in(" + Brands
						+ "))"
				: "";

		String dayNumbers = AlmoizDateUtils.getDayNumbersString(StartDate, EndDate);

		int serial = 1;
		System.out.println(
				"SELECT distinct assigned_to, (select DISPLAY_NAME from users u where u.id=assigned_to) as psr_name, (select city from common_distributors cd where cd.distributor_id=pjpv.distributor_id) as city, asm_id, asm_id, (SELECT region_name from common_regions where region_id in (select region_id from common_distributors cd where cd.distributor_id=pjpv.distributor_id)) as region, (select DISPLAY_NAME from users u where u.id=asm_id) as tso_name, distributor_id, (select name from common_distributors cd where cd.distributor_id=pjpv.distributor_id) as dis_name,"
						+ " (select rsm_id from common_distributors cd where cd.distributor_id=pjpv.distributor_id) as rsm_id FROM distributor_beat_plan_view pjpv where day_number in ("
						+ dayNumbers + ") and outlet_active=1 and pjpv.distributor_id in (" + Distributor + ")"
						+ whereRegions + whereCities + whereOrderBooker + whereChannels + whereTSOs + whereASMs);
		ResultSet rsData = s.executeQuery(
				"SELECT distinct assigned_to, (select DISPLAY_NAME from users u where u.id=assigned_to) as psr_name, (select city from common_distributors cd where cd.distributor_id=pjpv.distributor_id) as city, asm_id, asm_id, (SELECT region_name from common_regions where region_id in (select region_id from common_distributors cd where cd.distributor_id=pjpv.distributor_id)) as region, (select DISPLAY_NAME from users u where u.id=asm_id) as tso_name, distributor_id, (select name from common_distributors cd where cd.distributor_id=pjpv.distributor_id) as dis_name,"
						+ " (select rsm_id from common_distributors cd where cd.distributor_id=pjpv.distributor_id) as rsm_id FROM distributor_beat_plan_view pjpv where day_number in ("
						+ dayNumbers + ") and outlet_active=1 and pjpv.distributor_id in (" + Distributor + ") and pjpv.distributor_id not in ("+Almoiz_Configs.getTestDistributorsString()+")"
						+ whereRegions + whereCities + whereOrderBooker + whereChannels + whereTSOs + whereASMs);
		while (rsData.next()) {

			long orderBookerId = rsData.getLong("assigned_to"), distributorId = rsData.getLong("distributor_id");

			ResultSet rsASMName = s2
					.executeQuery("select DISPLAY_NAME from users u where u.id=" + rsData.getLong("rsm_id"));
			String asmName = (rsASMName.first()) ? rsASMName.getString("DISPLAY_NAME") : "";

			RowCount++;
			Row = spreadsheet.createRow((short) RowCount);
			Cell = (XSSFCell) Row.createCell((short) 0);
			Col = 0;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(serial);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(orderBookerId);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("psr_name"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(distributorId);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("dis_name"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("region"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("city"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("tso_name"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(asmName);
			Cell.setCellStyle(styleLeft);
			Col++;

			// int rowTotalOrders = 0;
//			System.out.println(
//					"select  count(distinct `isa`.`order_id`) as mobileOrders FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
//							+ "where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(StartDate) + " and "
//							+ AlmoizDateUtils.getSQLDateNext(EndDate) + " and order_id is not null and booked_by="
//							+ orderBookerId + whereSkUs + whereBrands);
			ResultSet rsOrders = s3.executeQuery(
					"select  count(distinct `isa`.`order_id`) as mobileOrders FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
							+ "where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(StartDate) + " and "
							+ AlmoizDateUtils.getSQLDateNext(EndDate) + " and order_id is not null and booked_by="
							+ orderBookerId + whereSkUs + whereBrands);
			int mobileOrders = (rsOrders.first()) ? rsOrders.getInt("mobileOrders") : 0;
			TotalMobileOrders += mobileOrders;
			// rowTotalOrders = mobileOrders;
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(mobileOrders);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(0);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(mobileOrders);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("100%");
			Cell.setCellStyle(styleRight);
			Col++;

			// TotalOrders += mobileOrders;
			serial++;
		}

//		System.out.println(
//				"SELECT  distinct distributor_id, (select name from common_distributors cd where cd.distributor_id=pjpv.distributor_id) as dis_name, (select city from common_distributors cd where cd.distributor_id=pjpv.distributor_id) as city, asm_id, asm_id, (SELECT region_name from common_regions where region_id in (select region_id from common_distributors cd where cd.distributor_id=pjpv.distributor_id)) as region, (select DISPLAY_NAME from users u where u.id=asm_id) as tso_name, "
//						+ " (select rsm_id from common_distributors cd where cd.distributor_id=pjpv.distributor_id) as rsm_id FROM distributor_beat_plan_view pjpv where day_number in ("
//						+ dayNumbers + ") and outlet_active=1 and pjpv.distributor_id in (" + Distributor + ")"
//						+ whereRegions + whereCities + whereOrderBooker + whereChannels + whereTSOs + whereASMs
//						+ whereSkUs + whereBrands);
		ResultSet rsDeskSaleData = s.executeQuery(
				"SELECT  distinct distributor_id, (select name from common_distributors cd where cd.distributor_id=pjpv.distributor_id) as dis_name, (select city from common_distributors cd where cd.distributor_id=pjpv.distributor_id) as city, asm_id, asm_id, (SELECT region_name from common_regions where region_id in (select region_id from common_distributors cd where cd.distributor_id=pjpv.distributor_id)) as region, (select DISPLAY_NAME from users u where u.id=asm_id) as tso_name, "
						+ " (select rsm_id from common_distributors cd where cd.distributor_id=pjpv.distributor_id) as rsm_id FROM distributor_beat_plan_view pjpv where day_number in ("
						+ dayNumbers + ") and outlet_active=1 and pjpv.distributor_id in (" + Distributor + ") and pjpv.distributor_id not in ("+Almoiz_Configs.getTestDistributorsString()+")"
						+ whereRegions + whereCities + whereOrderBooker + whereChannels + whereTSOs + whereASMs
						);
		while (rsDeskSaleData.next()) {
			long distributorId = rsDeskSaleData.getLong("distributor_id");

			ResultSet rsASMName = s2
					.executeQuery("select DISPLAY_NAME from users u where u.id=" + rsDeskSaleData.getLong("rsm_id"));
			String asmName = (rsASMName.first()) ? rsASMName.getString("DISPLAY_NAME") : "";

			

			// int rowTotalOrders = 0;
//			System.out.println(
//					"select  count(distinct `isa`.`id`) as deskSales FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
//							+ "where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(StartDate) + " and "
//							+ AlmoizDateUtils.getSQLDateNext(EndDate) + " and isa.distributor_id="+distributorId+" and order_id is null "
//							 + whereSkUs + whereBrands);
			ResultSet rsDeskSale= s3.executeQuery(
					"select  count(distinct `isa`.`id`) as deskSales FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
							+ "where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(StartDate) + " and "
							+ AlmoizDateUtils.getSQLDateNext(EndDate) + " and isa.distributor_id="+distributorId+" and order_id is null "
							 + whereSkUs + whereBrands);
			if(rsDeskSale.first()) {
			int deskSales = rsDeskSale.getInt("deskSales");
			TotalDeskSales += deskSales;
			if(deskSales!=0) {
			RowCount++;
			Row = spreadsheet.createRow((short) RowCount);
			Cell = (XSSFCell) Row.createCell((short) 0);
			Col = 0;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(serial);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("-");
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("Desk Sale");
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(distributorId);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsDeskSaleData.getString("dis_name"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsDeskSaleData.getString("region"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsDeskSaleData.getString("city"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsDeskSaleData.getString("tso_name"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(asmName);
			Cell.setCellStyle(styleLeft);
			Col++;
			// rowTotalOrders = mobileOrders;
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(0);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(deskSales);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(deskSales);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("100%");
			Cell.setCellStyle(styleRight);
			Col++;

			// TotalOrders += mobileOrders;
			serial++;
			}
			}
		}

		/*******************************
		 * ROW Dynamic End
		 *******************************/

		/*******************************
		 * ROW 6 Start
		 *******************************/

		RowCount++;
		Row = spreadsheet.createRow((short) RowCount);
		Cell = (XSSFCell) Row.createCell((short) 0);

		Col = 0;
		for (int i = 0; i < 8; i++) {
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("");
			Col++;
		}

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue("Total");
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getCenter());
		Col++;

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(TotalMobileOrders);
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getCenter());
		Col++;

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(TotalDeskSales);
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getCenter());
		Col++;

		TotalOrders = TotalDeskSales + TotalMobileOrders;
		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(TotalOrders);
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getCenter());
		Col++;

		double TotalPercentageMobileOrders = (TotalOrders != 0
				? (((double) TotalMobileOrders / (double) TotalOrders) * 100)
				: 0);

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(( (TotalPercentageMobileOrders == 100 )? Math.round(TotalPercentageMobileOrders) : AlmoizDateUtils.getDisplayCurrencyFormatTwoDecimalFixed(TotalPercentageMobileOrders)) + "%");
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getCenter());
		Col++;

		/*******************************
		 * ROW 6 End
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
