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
import com.pbc.util.AlmoizDateUtils;
import com.pbc.util.Almoiz_Configs;
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

public class R359Excel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath() + "/CashInflowSummary.pdf";

	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;

	public void createPdf(String filename, Date StartDate, Date EndDate, String Distributors, String orderBookers,
			String TSOs, String ASMs, String Regions, String City)
			throws IOException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

		ds.createConnection();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("KPI Report");

		ExcelColorsInHexa excelColors = new ExcelColorsInHexa();
		ExcelFontColors excelFontColors = new ExcelFontColors();
		ExcelFontAlign fontAlign = new ExcelFontAlign();

		XSSFCellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();

		XSSFCellStyle styleLeft = POIExcelUtils.getStyleWithOutCell(workbook, excelColors.getWhite(), style, font,
				excelFontColors.getBlack(), true, false, fontAlign.getLeft());
		XSSFCellStyle styleRight = POIExcelUtils.getStyleWithOutCell(workbook, excelColors.getWhite(), style, font,
				excelFontColors.getBlack(), true, false, fontAlign.getLeft());
		XSSFCellStyle styleCenter = POIExcelUtils.getStyleWithOutCell(workbook, excelColors.getWhite(), style, font,
				excelFontColors.getBlack(), true, false, fontAlign.getCenter());

		int RowCount = 0;
		int Col = 0;

		XSSFRow Row = spreadsheet.createRow((short) RowCount);
		XSSFCell Cell = (XSSFCell) Row.createCell((short) 0);

		/*******************************
		 * ROW 1 Start
		 *******************************/
		Cell.setCellValue("R359 - KPIs Report");
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
				3 // last column (0-based)
		));

		/*******************************
		 * ROW 2 End
		 *******************************/

		/***************************** Data ***************************************/

		String[] columnNames = { "Sr No.", "PSR THEIA ID", "PSR NAME", "DISTRIBUTOR ID", "DISTRIBUTOR NAME", "Town",
				"Region", "TSO", "ASM", "RSM", "PJP SHOPS This Day", "Visited", "STRIKE RATE %", "BILLED OUTLETS",
				"BILL PRODUCTIVITY %", "DS Volume", "DS Value", "UP", "SKU/Call" };

		String whereDistributors = " and distributor_id in (" + Distributors + ")";
		String whereDistributorsISA = " and isa.distributor_id in (" + Distributors + ")";

		// City Filter
		String whereCityDistributors = "", whereCityDistributorsISA = "";
		if (City.length() > 0) {
			int c = 0;
			String ids = "";
			System.out.println("select distributor_id from common_distributors where city_id in (" + City + ")");
			ResultSet rsCity = s
					.executeQuery("select distributor_id from common_distributors where city_id in (" + City + ")");
			while (rsCity.next()) {
				ids = (c == 0) ? ids + rsCity.getString("distributor_id")
						: ids + "," + rsCity.getString("distributor_id");
				c++;
			}
			whereCityDistributors = " and distributor_id in (" + ids + ")";
			whereCityDistributorsISA = " and isa.distributor_id in (" + ids + ")";
		}

		// Region Filter
		String whereRegionDistributors = "", whereRegionDistributorsISA = "";
		if (Regions.length() > 0) {
			int c = 0;
			String ids = "";
			System.out.println("select distributor_id from common_distributors where region_id in (" + Regions + ")");
			ResultSet rsRegion = s.executeQuery(
					"select distributor_id from common_distributors where region_id in (" + Regions + ")");
			while (rsRegion.next()) {
				ids = (c == 0) ? ids + rsRegion.getString("distributor_id")
						: ids + "," + rsRegion.getString("distributor_id");
				c++;
			}
			whereRegionDistributors = " and distributor_id in (" + ids + ")";
			whereRegionDistributorsISA = " and ias.distributor_id in (" + ids + ")";
		}

		// RSM Filter
		String whereRSMDistributors = "", whereRSMDistributorsISA = "";
		if (ASMs.length() > 0) {
			int c = 0;
			String ids = "";
			System.out.println("select distributor_id from common_distributors where rsm_id in (" + ASMs + ")");
			ResultSet rsRSM = s
					.executeQuery("select distributor_id from common_distributors where rsm_id in (" + ASMs + ")");
			while (rsRSM.next()) {
				ids = (c == 0) ? ids + rsRSM.getString("distributor_id")
						: ids + "," + rsRSM.getString("distributor_id");
				c++;
			}
			whereRSMDistributors = " and distributor_id in (" + ids + ")";
			whereRSMDistributorsISA = " and ias.distributor_id in (" + ids + ")";
		}

		// ASM Filter
		String whereASMDistributors = "", whereASMDistributorsISA = "";
		if (TSOs.length() > 0) {
			int c = 0;
			String ids = "";
			System.out.println("select distributor_id from distributor_beat_plan where asm_id in (" + TSOs + ")");
			ResultSet rsASM = s
					.executeQuery("select distributor_id from distributor_beat_plan where asm_id in (" + TSOs + ")");
			while (rsASM.next()) {
				ids = (c == 0) ? ids + rsASM.getString("distributor_id")
						: ids + "," + rsASM.getString("distributor_id");
				c++;
			}
			whereASMDistributors = " and distributor_id in (" + ids + ")";
			whereASMDistributorsISA = " and isa.distributor_id in (" + ids + ")";
		}

		// System.out.println(" whereASMDistributors : " +whereASMDistributors);

		// orderBookers Filter
		String whereOrderBooker = "", whereOrderBookerPJP = "", whereOrderBookerISA = "";
		if (orderBookers.length() > 0) {
			whereOrderBooker = " and created_by in (" + orderBookers + ")";
			whereOrderBookerPJP = " and assigned_to  in (" + orderBookers + ")";
			whereOrderBookerISA = " and isa.booked_by in (" + orderBookers + ")";
		}
		// System.out.println(" whereOrderBooker : " +whereOrderBooker);
		// System.out.println(" whereOrderBookerPJP : " +whereOrderBookerPJP);
		/****************************** Data ***************************************/

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

		/*******************************
		 * ROW 3 End
		 *******************************/

		/*******************************
		 * ROW Dynamic Start
		 *******************************/

		String dayNumbers = "";
		Date date = StartDate;
		int count = 0;
		while (true) {
			// System.out.println("Date Loop : "+Utilities.getDayOfWeekByDate(date));
			dayNumbers += (count == 0) ? Utilities.getDayOfWeekByDate(date) : "," + Utilities.getDayOfWeekByDate(date);
			if (date.equals(EndDate)) {
				break;
			}
			date = DateUtils.addDays(date, 1);
			count++;
		}
		// System.out.println("Date Loop Days : "+dayNumbers);
		int totalVisited = 0, productsVol = 0, totalSkusCount = 0;
		double productsVal = 0;

		int srNo = 1;
//		System.out.println(
//				"select distinct assigned_to, (select DISPLAY_NAME from users where ID=assigned_to) as name, distributor_id, asm_id from distributor_beat_plan_view where outlet_active=1 and distributor_id not in (100914, 100915) /* Test Distributors */ and  day_number in ("
//						+ dayNumbers + ") " + whereRSMDistributors + whereASMDistributors + whereCityDistributors + whereRegionDistributors + whereDistributors + whereOrderBookerPJP + " order by assigned_to");
		ResultSet rsPSR = s.executeQuery(
				"select distinct assigned_to, (select DISPLAY_NAME from users where ID=assigned_to) as name, distributor_id, asm_id from distributor_beat_plan_view where outlet_active=1 and distributor_id not in ("
						+ Almoiz_Configs.getTestDistributorsString() + ") /* Test Distributors */ and  day_number in ("
						+ dayNumbers + ") " + whereRSMDistributors + whereASMDistributors + whereCityDistributors
						+ whereRegionDistributors + whereDistributors + whereOrderBookerPJP + " order by assigned_to");
		while (rsPSR.next()) {

			final long orderBooker = rsPSR.getLong("assigned_to");
			final long distributorID = rsPSR.getLong("distributor_id");
			final long ASMID = rsPSR.getLong("asm_id");

//			System.out.println(
//					"select name, city, (select region_name from common_regions cg where cg.region_id=cd.region_id) as region, (select DISPLAY_NAME from users where ID=cd.rsm_id ) as ASM, (select DISPLAY_NAME from users where ID=cd.snd_id ) as RSM, (select DISPLAY_NAME from users where ID="
//							+ ASMID + " ) as TSO from common_distributors cd where distributor_id=" + distributorID);
			ResultSet rsDistributors = s2.executeQuery(
					"select name, city, (select region_name from common_regions cg where cg.region_id=cd.region_id) as region, (select DISPLAY_NAME from users where ID=cd.rsm_id ) as ASM, (select DISPLAY_NAME from users where ID=cd.snd_id ) as RSM, (select DISPLAY_NAME from users where ID="
							+ ASMID + " ) as TSO from common_distributors cd where distributor_id=" + distributorID);
			if (rsDistributors.first()) {

				RowCount++;
				Col = 0;
				Row = spreadsheet.createRow((short) RowCount);

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(srNo);
				Cell.setCellStyle(styleCenter);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(orderBooker);
				Cell.setCellStyle(styleRight);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsPSR.getString("name"));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(distributorID);
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsDistributors.getString("name"));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsDistributors.getString("city"));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsDistributors.getString("region"));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsDistributors.getString("TSO"));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsDistributors.getString("ASM"));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsDistributors.getString("RSM"));
				Cell.setCellStyle(styleLeft);
				Col++;

//				System.out.println(
//						"select count(*) as pjp_shops from distributor_beat_plan_view pjpv where outlet_active=1 and  day_number in ("
//								+ dayNumbers + ")  and assigned_to=" + orderBooker);
				ResultSet rsPJPShops = s3.executeQuery(
						"select count(*) as pjp_shops from distributor_beat_plan_view pjpv where outlet_active=1 and  day_number in ("
								+ dayNumbers + ")  and assigned_to=" + orderBooker);
				final int PJPShops = (rsPJPShops.first()) ? rsPJPShops.getInt("pjp_shops") : 0;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(PJPShops);
				Cell.setCellStyle(styleRight);
				Col++;

			
//				ResultSet rsBilled = s3.executeQuery(
//				"select count(distinct `isa`.`order_id`) as billed from inventory_sales_adjusted where order_id in (select id from mobile_order where created_by="
//						+ orderBooker + " and mobile_timestamp between " + Utilities.getSQLDate(StartDate)
//						+ " and " + Utilities.getSQLDateNext(EndDate) + ")");
		ResultSet rsSumOrders = s3.executeQuery(
				"select  count(distinct `isa`.`order_id`) as visits FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
						+ "where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(StartDate) + " and "
						+ AlmoizDateUtils.getSQLDateNext(EndDate) + " and order_id is not null and booked_by="
						+ orderBooker);
		int visits = (rsSumOrders.first()) ? rsSumOrders.getInt("visits") : 0;
		
				totalVisited += visits;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(visits);
				Cell.setCellStyle(styleRight);
				Col++;

				// Strike Rate
				double strikeRate = (PJPShops != 0) ? ((double) visits / (double) PJPShops) * 100 : 0;
				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(Utilities.getDisplayCurrencyFormatOneDecimal(strikeRate) + "%");
				Cell.setCellStyle(styleCenter);
				Col++;

//				ResultSet rsBilled = s3.executeQuery(
//						"select count(distinct `isa`.`order_id`) as billed from inventory_sales_adjusted where order_id in (select id from mobile_order where created_by="
//								+ orderBooker + " and mobile_timestamp between " + Utilities.getSQLDate(StartDate)
//								+ " and " + Utilities.getSQLDateNext(EndDate) + ")");
//				ResultSet rsBilled = s3.executeQuery(
//						"select  count(distinct `isa`.`order_id`) as billed FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
//								+ "where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(StartDate) + " and "
//								+ AlmoizDateUtils.getSQLDateNext(EndDate) + " and order_id is not null and booked_by="
//								+ orderBooker);
//				int BilledOutlets = (rsBilled.first()) ? rsBilled.getInt("billed") : 0;
				int BilledOutlets =visits;
				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(BilledOutlets);
				Cell.setCellStyle(styleRight);
				Col++;

				// Bill Productivity
				double productivity = (visits != 0) ? ((double) BilledOutlets / (double) visits) * 100 : 0;
				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(Utilities.getDisplayCurrencyFormatOneDecimal(productivity) + "%");
				Cell.setCellStyle(styleCenter);
				Col++;

				// DS Volume

//				ResultSet rsProductsVol = s3.executeQuery(
//						"SELECT sum(total_units*(ipv.liquid_in_ml*1000)) as raw  from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.order_id in (select id from mobile_order where created_by="
//							 + orderBooker + " and mobile_timestamp between " + Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) + ")");
				ResultSet rsProductsVol = s3.executeQuery(
						"select sum(total_units*(ipv.liquid_in_ml*1000)) as raw FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
								+ " join inventory_products_view ipv on ipv.product_id=isap.product_id where i_isd.created_on between "
								+ AlmoizDateUtils.getSQLDate(StartDate) + " and "
								+ AlmoizDateUtils.getSQLDateNext(EndDate) + " and order_id is not null and booked_by="
								+ orderBooker);
				int sumProductsVol = (rsProductsVol.first()) ? rsProductsVol.getInt("raw") : 0;
				productsVol += sumProductsVol;
				double DSVolume = (BilledOutlets != 0) ? ((double) sumProductsVol / (double) BilledOutlets) : 0;
				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(Utilities.getDisplayCurrencyFormatOneDecimal(DSVolume));
				Cell.setCellStyle(styleCenter);
				Col++;

				// DS Value
//				System.out.println(
//						"SELECT sum(net_amount) as amount"
//								+ " from inventory_sales_adjusted isa  where isa.adjusted_on between "
//								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
//								+ " and isa.booked_by=" + orderBooker);
				ResultSet rsProductsVal = s3.executeQuery(
						"select sum(isap.net_amount) as amount FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
								+ " where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(StartDate) + " and "
								+ AlmoizDateUtils.getSQLDateNext(EndDate) + " and order_id is not null and booked_by="
								+ orderBooker+ " and `isap`.`is_promotion` = 0");

				double sumProductsVal = (rsProductsVal.first()) ? rsProductsVal.getDouble("amount") : 0;
				productsVal += sumProductsVal;
				double DSVal = (BilledOutlets != 0) ? (sumProductsVal / (double) BilledOutlets) : 0;
				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(Utilities.getDisplayCurrencyFormatOneDecimal(DSVal));
				Cell.setCellStyle(styleCenter);
				Col++;
				
				ResultSet rsUP = s3.executeQuery(
						"select  count(distinct `isa`.`outlet_id`) as UP FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
								+ "where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(StartDate) + " and "
								+ AlmoizDateUtils.getSQLDateNext(EndDate) + " and order_id is not null and booked_by="
								+ orderBooker);

				// UP
				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue((rsUP.first()) ? rsUP.getInt("UP") : 0);
				Cell.setCellStyle(styleRight);
				Col++;

				// SKU/Call
//				System.out.println(
//						"SELECT count(product_id) as skus"
//								+ " from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isa.adjusted_on between "
//								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
//								+ " and isa.booked_by=" + orderBooker);
				ResultSet rsProductsCount = s3.executeQuery(
						"select count(isap.product_id) as skus FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
								+ " where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(StartDate) + " and "
								+ AlmoizDateUtils.getSQLDateNext(EndDate) + " and order_id is not null and booked_by="
								+ orderBooker);
				int ProductsCount = (rsProductsCount.first()) ? rsProductsCount.getInt("skus") : 0;
				totalSkusCount += ProductsCount;

				double SKUPerOutlets = (BilledOutlets != 0) ? ((double) ProductsCount / (double) BilledOutlets) : 0;
				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(Utilities.getDisplayCurrencyFormatTwoDecimalFixed(SKUPerOutlets));
				Cell.setCellStyle(styleCenter);
				Col++;

				srNo++;
			} // End of Distributor Query
		}

		/*******************************
		 * ROW Dynamic End
		 *******************************/

		/*******************************
		 * ROW 7 Start
		 *******************************/

		RowCount++;
		Row = spreadsheet.createRow((short) RowCount);

		Col = 9;
		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue("Total");
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getLeft());
		Col++;

//		System.out.println(
//				"select count(*) as total_pjp_shops from distributor_beat_plan_view pjpv where outlet_active=1 and  distributor_id not in (100914, 100915) /* Test Distributors */ and  day_number in ("
//						+ dayNumbers + ")" + whereRSMDistributors + whereASMDistributors + whereRegionDistributors + whereCityDistributors + whereOrderBookerPJP + whereDistributors);
		ResultSet rsPJPShops = s3.executeQuery(
				"select count(*) as total_pjp_shops from distributor_beat_plan_view pjpv where outlet_active=1 and  distributor_id not in (100914, 100915) /* Test Distributors */ and  day_number in ("
						+ dayNumbers + ")" + whereRSMDistributors + whereASMDistributors + whereRegionDistributors
						+ whereCityDistributors + whereOrderBookerPJP + whereDistributors);
		int totalPJPShops = (rsPJPShops.first()) ? rsPJPShops.getInt("total_pjp_shops") : 0;
		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(totalPJPShops);
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getCenter());
		Col++;

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(totalVisited);
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getCenter());
		Col++;

		double totalstrikeRate = (totalPJPShops != 0) ? ((double) totalVisited / (double) totalPJPShops) * 100 : 0;
		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(Utilities.getDisplayCurrencyFormatOneDecimal(totalstrikeRate) + "%");
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getCenter());
		Col++;

//		ResultSet rsTotalBilled = s3.executeQuery(
//				"select count(distinct outlet_id) as billed from inventory_sales_invoices where order_id in (select id from mobile_order where mobile_timestamp between  "
//						+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
//						+ whereRSMDistributors + whereASMDistributors + whereRegionDistributors + whereCityDistributors
//						+ whereOrderBooker + whereDistributors + ")");
		ResultSet rsTotalBilled = s3.executeQuery(
				"select  count(distinct `isa`.`order_id`) as billed FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
						+ "where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(StartDate) + " and "
						+ AlmoizDateUtils.getSQLDateNext(EndDate) + " and order_id is not null " + whereRSMDistributors
						+ whereASMDistributorsISA + whereRegionDistributorsISA + whereCityDistributorsISA + whereOrderBookerISA
						+ whereDistributorsISA);

		int TotalBilledOUtlets = (rsTotalBilled.first()) ? rsTotalBilled.getInt("billed") : 0;

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(TotalBilledOUtlets);
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getCenter());
		Col++;

		// Bill Productivity
		double Totalproductivity = (totalVisited != 0) ? ((double) TotalBilledOUtlets / (double) totalVisited) * 100
				: 0;
		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(Utilities.getDisplayCurrencyFormatOneDecimal(Totalproductivity) + "%");
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getCenter());
		Col++;

		// Total DS Volume productsVol
		double TotalDSVolume = (TotalBilledOUtlets != 0) ? ((double) productsVol / (double) TotalBilledOUtlets) : 0;
		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(Utilities.getDisplayCurrencyFormatOneDecimal(TotalDSVolume));
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getCenter());
		Col++;

		// Total DS Value
		double TotalDSVal = (TotalBilledOUtlets != 0) ? ((double) productsVal / (double) TotalBilledOUtlets) : 0;
		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(Utilities.getDisplayCurrencyFormatOneDecimal(TotalDSVal));
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getCenter());
		Col++;
		
		ResultSet rsTotalUP = s3.executeQuery(
				"select  count(distinct `isa`.`outlet_id`) as up FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
						+ "where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(StartDate) + " and "
						+ AlmoizDateUtils.getSQLDateNext(EndDate) + " and order_id is not null " + whereRSMDistributors
						+ whereASMDistributorsISA + whereRegionDistributorsISA + whereCityDistributorsISA + whereOrderBookerISA
						+ whereDistributorsISA);

		
		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue((rsTotalUP.first()) ? rsTotalUP.getInt("UP") : 0);
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getCenter());

		Col++;

		// Total SKU/Call
//		System.out.println(
//				"SELECT count(product_id) as skus"
//						+ " from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id  where isa.adjusted_on between "
//						+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
//						+ whereRSMDistributors + whereASMDistributors + whereRegionDistributors + whereOrderBookerBooked + whereDistributors);

		double TotalSKUPerCall = (TotalBilledOUtlets != 0) ? ((double) totalSkusCount / (double) TotalBilledOUtlets)
				: 0;
		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(Utilities.getDisplayCurrencyFormatTwoDecimalFixed(TotalSKUPerCall));
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getCenter());

		Col++;

		/*******************************
		 * ROW 7 End
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

		s.close();
		s2.close();
		ds.dropConnection();
	}
}
