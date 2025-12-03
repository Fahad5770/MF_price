package com.pbc.pushmail.scorecards;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import com.pbc.util.AlmoizDateUtils;
import com.pbc.util.Almoiz_Configs;
import com.pbc.util.Datasource;
import com.pbc.util.ExcelColorsInHexa;
import com.pbc.util.ExcelFontAlign;
import com.pbc.util.ExcelFontColors;
import com.pbc.util.POIExcelUtils;
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

public class NewKPIsExcel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath() + "/CashInflowSummary.pdf";

	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	Statement s5;

	int NoOfDaysToGoBack = -5;

	Date StartDate = Utilities.getDateByDays(NoOfDaysToGoBack);
	Date EndDate = Utilities.getDateByDays(NoOfDaysToGoBack);

	public void createPdf(String filename, long ASM_Id, boolean isMTD) throws Exception {

		ds.createConnection();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
		s5 = ds.createStatement();

		XSSFWorkbook workbook = new XSSFWorkbook();

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
		XSSFCellStyle styleTotal = POIExcelUtils.getStyleWithOutCell(workbook, excelColors.getBlue(), style, font,
				excelFontColors.getWhite(), true, true, fontAlign.getCenter());
		XSSFCellStyle styleGurr = POIExcelUtils.getStyleWithOutCell(workbook, excelColors.getBrown(), style, font,
				excelFontColors.getWhite(), true, false, fontAlign.getRight());
		XSSFCellStyle styleTotalPerRow = POIExcelUtils.getStyleWithOutCell(workbook, excelColors.getGreen(), style,
				font, excelFontColors.getWhite(), true, true, fontAlign.getCenter());

		int RowCount = 0;
		int Col = 0;

		String whereASM = (ASM_Id != 0) ? "  and asm_id in (" + ASM_Id + ")" : "";
		String whereASMDistributors = (ASM_Id != 0) ? "  and isa.distributor_id in (" + ASM_Id + ")" : "";

		if (isMTD) {
			this.StartDate = Utilities.getStartDateByDate(Utilities.getDateByDays(NoOfDaysToGoBack));
			this.EndDate = Utilities.getDateByDays(NoOfDaysToGoBack);
		} else {
			this.EndDate = this.StartDate;
		}

		System.out.println("StartDate : " + Utilities.getDisplayDateFormat(this.StartDate));
		System.out.println("EndDate : " + Utilities.getDisplayDateFormat(this.EndDate));

		/***************************** Data ***************************************/

		int totalSKUs = 0;
		ResultSet rsSKUCounts = s
				.executeQuery("select count(id) as totalCount from pep.inventory_products where is_visible=1");
		if (rsSKUCounts.first()) {
			totalSKUs = rsSKUCounts.getInt("totalCount");
		}

		int totalLrbs = 0;
		ArrayList<Integer> LRBIds = new ArrayList<Integer>();
		ArrayList<String> LRBTitles = new ArrayList<String>();
		ResultSet rsLRBs = s.executeQuery("select id,label from pep.inventory_products_lrb_types where id not in (3)");
		while (rsLRBs.next()) {
			LRBIds.add(rsLRBs.getInt("id"));
			LRBTitles.add(rsLRBs.getString("label"));
			totalLrbs++;
		}
		// AS Gurr jar and Gurr box both are same
//		LRBIds.add("3,8,9");
//		LRBTitles.add("Gurr");
//		totalLrbs++;

		String[] columnNames = { "THEIA CODE", "PSR NAME", "DISTRIBUTOR NAME", "Region", "TOWN", "TSO", "ASM", "RSM",
				"NATIONAL", "PJP SHOPS This Day", "Visited", "STRIKE RATE %", "BILLED OUTLETS", "BILL PRODUCTIVITY %",
				"DS/SHOP VOL", "DS/SHOP VAL", "UP", "SKU/CALL" };

		int totalBlankColumns = columnNames.length;
		int totalSKUsColumns = totalLrbs + totalSKUs;

		int mergeFromCol = 0;
		int mergeToCol = 0;
		/****************************** Data ***************************************/

		/******************************
		 * Start Loop
		 ******************************/

		Date date = this.StartDate;
		LinkedList<Integer> TotalsBlue = new LinkedList<Integer>();

		int brown = 0, green = 0;

		int arraySize = 0;

		for (int i = 0; i < 2; i++) {
			for (int LRBId : LRBIds) {
				ResultSet rsSKUsTitle = s.executeQuery(
						"select concat(brand_label,'-',package_label) as SKU_label from pep.inventory_products_view where is_visible=1 and lrb_type_id in("
								+ LRBId + ")");
				while (rsSKUsTitle.next()) {
					arraySize++;
				}
				TotalsBlue.add(arraySize);
				arraySize++;

			} // end of LRBs Loop

			if (i == 0) {
				brown = arraySize; // FOR Bill productivity Gurr Total style
			}
			arraySize++;

			if (i == 0) {
				green = arraySize; // FOR Bill productivity Total per row style
			}
			arraySize++;

		}

		// System.out.println(" arraySize : " + arraySize);

		double[] GrandTotal = new double[arraySize];

		while (true) {

//			System.out.println("IN Loop => Date : " + Utilities.getDisplayDateFormat(date));
			XSSFSheet spreadsheet = workbook.createSheet(getDisplayDateFormatWithDashes(date));

			RowCount = 0;
			Col = 0;

			XSSFRow Row = spreadsheet.createRow((short) RowCount);
			XSSFCell Cell = (XSSFCell) Row.createCell((short) 0);

			/*******************************
			 * ROW 1 Start
			 *******************************/
			Cell.setCellValue("KPI Report");
			spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, // first row (0-based)
					RowCount, // last row (0-based)
					0, // first column (0-based)
					2 // last column (0-based)
			));

			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			style.setFont(font);// set it to bold
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getWhite(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getLeft());

			/*******************************
			 * ROW 1 End
			 ******************************/

			/*******************************
			 * ROW 2 Start
			 *******************************/

			RowCount++;
			Row = spreadsheet.createRow((short) RowCount);
			Cell = (XSSFCell) Row.createCell((short) 0);

			Cell.setCellValue("Date : " + Utilities.getDisplayDateFormat(date));

			spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, // first row (0-based)
					RowCount, // last row (0-based)
					0, // first column (0-based)
					2 // last column (0-based)
			));

			/*******************************
			 * ROW 2 End
			 *******************************/

			/*******************************
			 * ROW 3 Start
			 *******************************/
			RowCount++;
			Row = spreadsheet.createRow((short) RowCount);
			Cell = (XSSFCell) Row.createCell((short) 0);

			mergeFromCol = 0;
			mergeToCol = totalBlankColumns - 1;

			// Row 4 Col-1 merging
			POIExcelUtils.SetBackColor(workbook, Cell, style, excelColors.getOrange(), false);
			spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, RowCount, mergeFromCol, mergeToCol));

			// Row 4 Col-2 merging
			mergeFromCol = mergeToCol + 1;
			mergeToCol = mergeToCol + totalSKUsColumns + totalSKUsColumns + 2 + 2;

			Cell = (XSSFCell) Row.createCell((short) mergeFromCol);
			Cell.setCellValue("BRAND WISE PRODUCTIVITY");
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getBrown(), style, font,
					excelFontColors.getWhite(), false, true, fontAlign.getCenter());
			spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, RowCount, mergeFromCol, mergeToCol));

			/*******************************
			 * ROW 3 End
			 *******************************/

			/*******************************
			 * ROW 4 Start
			 *******************************/
			RowCount++;

			// Blank Merging
			Row = spreadsheet.createRow((short) RowCount);
			Cell = (XSSFCell) Row.createCell((short) 0);

			mergeFromCol = 0;
			mergeToCol = totalBlankColumns - 1;

			// Row 4 Col-1 merging
			POIExcelUtils.SetBackColor(workbook, Cell, style, excelColors.getGray(), false);
			spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, RowCount, mergeFromCol, mergeToCol));

			// Row 4 Col-2 merging
			mergeFromCol = mergeToCol + 1;
			mergeToCol = mergeToCol + totalSKUsColumns + 2;

			Cell = (XSSFCell) Row.createCell((short) mergeFromCol);
			Cell.setCellValue("ALL SKUs PRODUCTIVITY By Billed Outlets");// Purple
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getCadetblue(), style, font,
					excelFontColors.getWhite(), false, true, fontAlign.getCenter());
			spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, RowCount, mergeFromCol, mergeToCol));

			// Row 4 Col-3 merging
			mergeFromCol = mergeToCol + 1;
			mergeToCol = mergeToCol + totalSKUsColumns + 2;

			Cell = (XSSFCell) Row.createCell((short) mergeFromCol);
			Cell.setCellValue("SALES ALL SKUs");
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getPurple(), style, font,
					excelFontColors.getWhite(), false, true, fontAlign.getCenter());
			spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, RowCount, mergeFromCol, mergeToCol));

			/*******************************
			 * ROW 4 End
			 *******************************/

			/*******************************
			 * ROW 5 Start
			 *******************************/

			RowCount++;
			Row = spreadsheet.createRow((short) RowCount);
			Cell = (XSSFCell) Row.createCell((short) 0);

			for (String title : columnNames) {
				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(title);
				POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
						excelFontColors.getBlack(), true, true, fontAlign.getCenter());
				Col++;
			}
			int LrbTitleIncrement = 0;

			// SKUs

			for (int i = 0; i < 2; i++) {
				LrbTitleIncrement = 0;
				for (int LRBId : LRBIds) {

					ResultSet rsSKUsTitle = s.executeQuery(
							"select concat(brand_label,'-',package_label) as SKU_label from pep.inventory_products_view where is_visible=1 and lrb_type_id in("
									+ LRBId + ")");
					while (rsSKUsTitle.next()) {
						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(rsSKUsTitle.getString("SKU_label"));
						POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font,
								excelFontColors.getBlack(), true, true, fontAlign.getLeft());
						Col++;
						// arraySize++;
					}

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue("Total " + LRBTitles.get(LrbTitleIncrement));
					POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getBlue(), style, font,
							excelFontColors.getWhite(), true, true, fontAlign.getLeft());
					Col++;
					LrbTitleIncrement++;
					// arraySize++;
				} // end of LRBs Loop

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue("Total Gurr");
				POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getBrown(), style, font,
						excelFontColors.getWhite(), true, true, fontAlign.getLeft());
				Col++;
				// arraySize++;
				if (i == 0) {
					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue("Total SKUs PRODUCTIVITY");
					POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getGreen(), style, font,
							excelFontColors.getWhite(), true, true, fontAlign.getLeft());
					Col++;
					// arraySize++;
				} else {
					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue("Total SKUs SALES");
					POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getGreen(), style, font,
							excelFontColors.getWhite(), true, true, fontAlign.getLeft());
					Col++;
					// arraySize++;
				}

			} // end of 2 times loop
			/*******************************
			 * ROW 5 End
			 *******************************/
			
			

			/*******************************
			 * ROW Dynamic Start
			 *******************************/
double toalSales=0;
			// arraySize = arraySize + 9;
			for (int u = 0; u < arraySize; u++) {
				GrandTotal[u] = 0.00;
			}

			// System.out.println("arraySize : " + arraySize);

//			System.out.println(
//					"select distributor_id, name,(select DISPLAY_NAME from users u where u.id in (select asm_id  from distributor_beat_plan pjp where pjp.distributor_id=cd.distributor_id limit 1)) as tso,(select region_name from common_regions cr where cr.region_id=cd.region_id) as region , city, (select Display_name from users where id=rsm_id) asm, (select Display_name from users where id=snd_id) rsm from common_distributors cd where distributor_id not in (100914,100915) and is_active=1 "
//							+ whereASMDistributors + whereInActiveDistributors);

			int totalVisited = 0, productsVol = 0, totalSkusCount = 0,
					dayNumber = AlmoizDateUtils.getDayOfWeekByDate(date);
			double productsVal = 0;

			ResultSet rsPSR = s.executeQuery(
					"select distinct assigned_to, (select DISPLAY_NAME from users where ID=assigned_to) as name, distributor_id, asm_id from distributor_beat_plan_view where outlet_active=1 and distributor_id not in ("
							+ Almoiz_Configs.getTestDistributorsString() + ") and  day_number in (" + dayNumber + ") "
							+ whereASM + " order by distributor_id");
			while (rsPSR.next()) {

				final long orderBookerId = rsPSR.getLong("assigned_to");
				final long distributor_id = rsPSR.getLong("distributor_id");
				final long ASMID = rsPSR.getLong("asm_id");

				ResultSet rsDistributors = s2.executeQuery(
						"select name, city, (select region_name from common_regions cg where cg.region_id=cd.region_id) as region, (select DISPLAY_NAME from users where ID=cd.rsm_id ) as ASM, (select DISPLAY_NAME from users where ID=cd.snd_id ) as RSM, (select DISPLAY_NAME from users where ID="
								+ ASMID + " ) as TSO from common_distributors cd where distributor_id="
								+ distributor_id);
				if (rsDistributors.first()) {
					int increment = 0;

					RowCount++;
					Row = spreadsheet.createRow((short) RowCount);
					Col = 0;

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(orderBookerId);
					Cell.setCellStyle(styleRight);
					Col++;

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(orderBookerId + "-" + rsPSR.getString("name"));
					Cell.setCellStyle(styleLeft);
					Col++;

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(distributor_id + "-" + rsDistributors.getString("name"));
					Cell.setCellStyle(styleLeft);
					Col++;

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(rsDistributors.getString("region"));
					Cell.setCellStyle(styleLeft);
					Col++;

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(rsDistributors.getString("city"));
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

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(Almoiz_Configs.getConsolidateNationalValue());
					Cell.setCellStyle(styleLeft);
					Col++;

					ResultSet rsPJPShops = s4.executeQuery(
							"select count(*) as pjp_shops from distributor_beat_plan_view pjpv where outlet_active=1 and  day_number in ("
									+ dayNumber + ")  and assigned_to=" + orderBookerId);
					final int PJPShops = (rsPJPShops.first()) ? rsPJPShops.getInt("pjp_shops") : 0;

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(PJPShops);
					Cell.setCellStyle(styleRight);
					Col++;

					int visits = 0;
					System.out.println(
							"select count(distinct outlet_id) as sum_orders from mobile_order where created_by="
									+ orderBookerId + " and mobile_timestamp between "
									+ AlmoizDateUtils.getSQLDate(date) + " and "
									+ AlmoizDateUtils.getSQLDateNext(date));
					ResultSet rsSumOrdera = s4.executeQuery(
							"select count(distinct outlet_id) as sum_orders from mobile_order where created_by="
									+ orderBookerId + " and mobile_timestamp between "
									+ AlmoizDateUtils.getSQLDate(date) + " and "
									+ AlmoizDateUtils.getSQLDateNext(date));
					visits = (rsSumOrdera.first()) ? rsSumOrdera.getInt("sum_orders") : 0;

					System.out.println(
							"select count(distinct outlet_id) as sum_no_orders from mobile_order_zero where created_by="
									+ orderBookerId + " and mobile_timestamp between "
									+ AlmoizDateUtils.getSQLDate(date) + " and "
									+ AlmoizDateUtils.getSQLDateNext(date));
					ResultSet rsSumNoOrdera = s4.executeQuery(
							"select count(distinct outlet_id) as sum_no_orders from mobile_order_zero where created_by="
									+ orderBookerId + " and mobile_timestamp between "
									+ AlmoizDateUtils.getSQLDate(date) + " and "
									+ AlmoizDateUtils.getSQLDateNext(date));
					visits += (rsSumNoOrdera.first()) ? rsSumNoOrdera.getInt("sum_no_orders") : 0;

					System.out.println(
							"select count(distinct outlet_id) as sum_visits from common_outlets_visit_duration where created_by="
									+ orderBookerId + " and mobile_timestamp between "
									+ AlmoizDateUtils.getSQLDate(date) + " and " + AlmoizDateUtils.getSQLDateNext(date)
									+ " and visit_type=1 and outlet_id not in (select outlet_id from mobile_order_zero where created_by="
									+ orderBookerId + " and mobile_timestamp between "
									+ AlmoizDateUtils.getSQLDate(date) + " and " + AlmoizDateUtils.getSQLDateNext(date)
									+ ") and outlet_id not in (select outlet_id from mobile_order where created_by="
									+ orderBookerId + " and mobile_timestamp between "
									+ AlmoizDateUtils.getSQLDate(date) + " and " + AlmoizDateUtils.getSQLDateNext(date)
									+ ")");
					ResultSet rsSumVists = s4.executeQuery(
							"select count(distinct outlet_id) as sum_visits from common_outlets_visit_duration where created_by="
									+ orderBookerId + " and mobile_timestamp between "
									+ AlmoizDateUtils.getSQLDate(date) + " and " + AlmoizDateUtils.getSQLDateNext(date)
									+ " and visit_type=1 and outlet_id not in (select outlet_id from mobile_order_zero where created_by="
									+ orderBookerId + " and mobile_timestamp between "
									+ AlmoizDateUtils.getSQLDate(date) + " and " + AlmoizDateUtils.getSQLDateNext(date)
									+ ") and outlet_id not in (select outlet_id from mobile_order where created_by="
									+ orderBookerId + " and mobile_timestamp between "
									+ AlmoizDateUtils.getSQLDate(date) + " and " + AlmoizDateUtils.getSQLDateNext(date)
									+ ")");
					visits += (rsSumVists.first()) ? rsSumVists.getInt("sum_visits") : 0;

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

					System.out.println(
							"select  count(distinct `isa`.`outlet_id`) as billed FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
									+ "where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(date) + " and "
									+ AlmoizDateUtils.getSQLDateNext(date) + " and order_id is not null and booked_by="
									+ orderBookerId);
					ResultSet rsBilled = s4.executeQuery(
							"select  count(distinct `isa`.`outlet_id`) as billed FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
									+ "where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(date) + " and "
									+ AlmoizDateUtils.getSQLDateNext(date) + " and order_id is not null and booked_by="
									+ orderBookerId);
					int BilledOutlets = (rsBilled.first()) ? rsBilled.getInt("billed") : 0;
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

					System.out.println(
							"select sum(total_units*(ipv.liquid_in_ml*1000)) as raw FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
									+ " join inventory_products_view ipv on ipv.product_id=isap.product_id where i_isd.created_on between "
									+ AlmoizDateUtils.getSQLDate(date) + " and " + AlmoizDateUtils.getSQLDateNext(date)
									+ " and order_id is not null and booked_by=" + orderBookerId);
					ResultSet rsProductsVol = s4.executeQuery(
							"select sum(total_units*(ipv.liquid_in_ml*1000)) as raw FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
									+ " join inventory_products_view ipv on ipv.product_id=isap.product_id where i_isd.created_on between "
									+ AlmoizDateUtils.getSQLDate(date) + " and " + AlmoizDateUtils.getSQLDateNext(date)
									+ " and order_id is not null and booked_by=" + orderBookerId);
					int sumProductsVol = (rsProductsVol.first()) ? rsProductsVol.getInt("raw") : 0;
					productsVol += sumProductsVol;
					double DSVolume = (BilledOutlets != 0) ? ((double) sumProductsVol / (double) BilledOutlets) : 0;
					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(Utilities.getDisplayCurrencyFormatOneDecimal(DSVolume));
					Cell.setCellStyle(styleCenter);
					Col++;

					// DS Value
					System.out.println(
							"select sum(isap.net_amount) as amount FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
									+ " where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(date) + " and "
									+ AlmoizDateUtils.getSQLDateNext(date) + " and order_id is not null and booked_by="
									+ orderBookerId);
					ResultSet rsProductsVal = s4.executeQuery(
							"select sum(isap.net_amount) as amount FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
									+ " where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(date) + " and "
									+ AlmoizDateUtils.getSQLDateNext(date) + " and order_id is not null and booked_by="
									+ orderBookerId);

					double sumProductsVal = (rsProductsVal.first()) ? rsProductsVal.getDouble("amount") : 0;
					productsVal += sumProductsVal;
			
					double DSVal = (BilledOutlets != 0) ? (sumProductsVal / (double) BilledOutlets) : 0;
					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(Utilities.getDisplayCurrencyFormatOneDecimal(DSVal));
					Cell.setCellStyle(styleCenter);
					Col++;

					// UP
					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(BilledOutlets);
					Cell.setCellStyle(styleRight);
					Col++;

					// SKU/Call
					System.out.println(
							"select count(isap.product_id) as skus FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
									+ " where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(date) + " and "
									+ AlmoizDateUtils.getSQLDateNext(date) + " and order_id is not null and booked_by="
									+ orderBookerId);
					ResultSet rsProductsCount = s4.executeQuery(
							"select count(isap.product_id) as skus FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
									+ " where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(date) + " and "
									+ AlmoizDateUtils.getSQLDateNext(date) + " and order_id is not null and booked_by="
									+ orderBookerId);
					int ProductsCount = (rsProductsCount.first()) ? rsProductsCount.getInt("skus") : 0;
					totalSkusCount += ProductsCount;

					double SKUPerOutlets = (BilledOutlets != 0) ? ((double) ProductsCount / (double) BilledOutlets) : 0;
					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(Utilities.getDisplayCurrencyFormatTwoDecimalFixed(SKUPerOutlets));
					Cell.setCellStyle(styleCenter);
					Col++;

					/** SKUS Bill **/
					double totalGurr = 0, grandTotal = 0;
					for (int LRBId : LRBIds) {
						double total = 0;
//								System.out.println(
//										"select product_id from pep.inventory_products_view where is_visible=1 and lrb_type_id="
//												+ LRBId);
						ResultSet rsSKUs = s4.executeQuery(
								"select product_id from pep.inventory_products_view where is_visible=1 and lrb_type_id="
										+ LRBId);
						while (rsSKUs.next()) {
//									System.out.println(
//											"SELECT sum(raw_cases)*(SELECT liquid_in_ml FROM inventory_products_view v where product_id="+rsSKUs.getInt("product_id")+")  as c FROM pep.inventory_sales_invoices_products where product_id="
//													+ rsSKUs.getInt("product_id")
//													+ " and id in(SELECT id FROM pep.inventory_sales_invoices where created_on between "
//													+ Utilities.getSQLDate(date) + " and " + Utilities.getSQLDateNext(date)
//													+ " and booked_by=" + orderBookerId + ")");
//									ResultSet rsBills = s5.executeQuery(
//											"SELECT COUNT(*) as c FROM pep.inventory_sales_invoices_products where product_id="
//													+ rsSKUs.getInt("product_id")
//													+ " and id in(SELECT id FROM pep.inventory_sales_invoices where created_on between "
//													+ Utilities.getSQLDate(date) + " and "
//													+ Utilities.getSQLDateNext(date) + " and booked_by=" + orderBookerId
//													+ ")");
							System.out.println(
									"select  count(distinct `isa`.`order_id`) as mobileOrders FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
											+ "where isap.product_id=" + rsSKUs.getInt("product_id")
											+ " and i_isd.created_on between " + AlmoizDateUtils.getSQLDate(date)
											+ " and " + AlmoizDateUtils.getSQLDateNext(date) + " and booked_by="
											+ orderBookerId);
							ResultSet rsBills = s5.executeQuery(
									"select  count(distinct `isa`.`order_id`) as mobileOrders FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
											+ "where isap.product_id=" + rsSKUs.getInt("product_id")
											+ " and i_isd.created_on between " + AlmoizDateUtils.getSQLDate(date)
											+ " and " + AlmoizDateUtils.getSQLDateNext(date) + " and booked_by="
											+ orderBookerId);
							if (rsBills.first()) {
								Cell = (XSSFCell) Row.createCell((short) Col);
								Cell.setCellValue(rsBills.getDouble("mobileOrders"));
								Cell.setCellStyle(styleRight);
								Col++;
								total += rsBills.getDouble("mobileOrders");
								GrandTotal[increment] = GrandTotal[increment] + rsBills.getDouble("mobileOrders");
								increment++;
							}
						}
						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(total); // Total Per ROw
						Cell.setCellStyle(styleTotal);
						Col++;
						GrandTotal[increment] = GrandTotal[increment] + total;
						increment++;

						grandTotal += total;

						if (LRBId == 8 || LRBId == 9) {
							totalGurr += total;
						}

					}

					// Total gurr
					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(totalGurr); // Total Gurr Per Row
					Cell.setCellStyle(styleGurr);
					Col++;
					GrandTotal[increment] = GrandTotal[increment] + totalGurr;
					increment++;

					// Grand Total
					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(grandTotal); // Grand Total Per Row
					Cell.setCellStyle(styleTotalPerRow);
					Col++;
					GrandTotal[increment] = GrandTotal[increment] + grandTotal;
					increment++;

					/** SKUS Bill **/

					/** SKUS Sales **/
					double totalGurr1 = 0, grandTotal1 = 0;
					for (int LRBId : LRBIds) {
						double total = 0;
//								System.out.println(
//										"select product_id from pep.inventory_products_view where is_visible=1 and lrb_type_id="
//												+ LRBId);
						ResultSet rsSKUs = s4.executeQuery(
								"select product_id,unit_per_sku,liquid_in_ml from pep.inventory_products_view where is_visible=1 and lrb_type_id="
										+ LRBId);
						while (rsSKUs.next()) {
//									System.out.println(
//											"SELECT sum(raw_cases*("+rsSKUs.getInt("unit_per_sku")+"*"+rsSKUs.getDouble("liquid_in_ml")+")) as c FROM pep.inventory_sales_adjusted_products where product_id="
//													+ rsSKUs.getInt("product_id")
//													+ " and id in(SELECT id FROM pep.inventory_sales_adjusted where "
//													+ DateCondition + " between " + Utilities.getSQLDate(date) + " and "
//													+ Utilities.getSQLDateNext(date) + " and booked_by=" + orderBookerId
//													+ ")");
//									ResultSet rsSales = s5.executeQuery("SELECT sum(raw_cases*("
//											+ rsSKUs.getInt("unit_per_sku") + "*" + rsSKUs.getDouble("liquid_in_ml")
//											+ ")) as c FROM pep.inventory_sales_adjusted_products where product_id="
//											+ rsSKUs.getInt("product_id")
//											+ " and id in(SELECT id FROM pep.inventory_sales_adjusted where "
//											+ DateCondition + " between " + Utilities.getSQLDate(date) + " and "
//											+ Utilities.getSQLDateNext(date) + " and booked_by=" + orderBookerId + ")");
							System.out.println("select  sum(raw_cases*(" + rsSKUs.getInt("unit_per_sku") + "*"
									+ rsSKUs.getDouble("liquid_in_ml")
									+ ")) as c FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
									+ "where isap.product_id=" + rsSKUs.getInt("product_id") + " and isap.product_id="
									+ rsSKUs.getInt("product_id") + " and i_isd.created_on between "
									+ AlmoizDateUtils.getSQLDate(date) + " and " + AlmoizDateUtils.getSQLDateNext(date)
									+ " and booked_by=" + orderBookerId);
							ResultSet rsSales = s5.executeQuery("select  sum(raw_cases*("
									+ rsSKUs.getInt("unit_per_sku") + "*" + rsSKUs.getDouble("liquid_in_ml")
									+ ")) as c FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
									+ "where isap.product_id=" + rsSKUs.getInt("product_id") + " and isap.product_id="
									+ rsSKUs.getInt("product_id") + " and i_isd.created_on between "
									+ AlmoizDateUtils.getSQLDate(date) + " and " + AlmoizDateUtils.getSQLDateNext(date)
									+ " and booked_by=" + orderBookerId);
							if (rsSales.first()) {
								Cell = (XSSFCell) Row.createCell((short) Col);
								Cell.setCellValue(rsSales.getDouble("c"));
								Cell.setCellStyle(styleRight);
								Col++;
								total += rsSales.getDouble("c");
								GrandTotal[increment] = GrandTotal[increment] + rsSales.getDouble("c");
								increment++;
							}
						}
						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(total); // Total Per ROw
						Cell.setCellStyle(styleTotal);
						Col++;
						GrandTotal[increment] = GrandTotal[increment] + total;
						increment++;

						grandTotal1 += total;

						if (LRBId == 8 || LRBId == 9) {
							totalGurr1 += total;
						}

					}

					// Total gurr
					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(totalGurr1); // Total Gurr Per Row
					Cell.setCellStyle(styleGurr);
					Col++;
					GrandTotal[increment] = GrandTotal[increment] + totalGurr1;
					increment++;

					// Grand Total
					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(grandTotal1); // Grand Total Per Row
					Cell.setCellStyle(styleTotalPerRow);
					Col++;
					GrandTotal[increment] = GrandTotal[increment] + grandTotal1;
					increment++;

					/** SKUS Sales **/

					/***** Desk Sales Start ******/

				} // end of Distributors Data
			} // End of order Booker Loop

			/**************** Desk Sale ***************************/
//			System.out.println(
//			"SELECT  distinct distributor_id, (select name from common_distributors cd where cd.distributor_id=pjpv.distributor_id) as dis_name, (select city from common_distributors cd where cd.distributor_id=pjpv.distributor_id) as city, asm_id, asm_id, (SELECT region_name from common_regions where region_id in (select region_id from common_distributors cd where cd.distributor_id=pjpv.distributor_id)) as region, (select DISPLAY_NAME from users u where u.id=asm_id) as tso_name, "
//					+ " (select rsm_id from common_distributors cd where cd.distributor_id=pjpv.distributor_id) as rsm_id FROM distributor_beat_plan_view pjpv where day_number in ("
//					+ dayNumbers + ") and outlet_active=1 and pjpv.distributor_id in (" + Distributor + ")"
//					+ whereRegions + whereCities + whereOrderBooker + whereChannels + whereTSOs + whereASMs
//					+ whereSkUs + whereBrands);
			ResultSet rsDeskSaleData = s.executeQuery(
					"select  distinct isa.distributor_id FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
							+ "where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(date) + " and "
							+ AlmoizDateUtils.getSQLDateNext(date) + " and order_id is null " + whereASMDistributors);
			while (rsDeskSaleData.next()) {
				int increment=0;
				long distributorId = rsDeskSaleData.getLong("distributor_id");
				ResultSet rsDistributors = s2.executeQuery(
						"select name, city, (select region_name from common_regions cg where cg.region_id=cd.region_id) as region, (select DISPLAY_NAME from users where ID=cd.rsm_id ) as ASM, (select DISPLAY_NAME from users where ID=cd.snd_id ) as RSM from common_distributors cd where distributor_id=" + distributorId);
				if (rsDistributors.first()) {

					ResultSet rsTso = s3.executeQuery(
							"select (select DISPLAY_NAME from users u where u.id=asm_id ) as asm from distributor_beat_plan where distributor_id="
									+ distributorId + " limit 1");
					String TSO = (rsTso.first()) ? rsTso.getString("asm") : "";
					RowCount++;
					Row = spreadsheet.createRow((short) RowCount);
					Col = 0;

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue("-");
					Cell.setCellStyle(styleCenter);
					Col++;

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue("Desk Sale");
					Cell.setCellStyle(styleLeft);
					Col++;

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(distributorId + "-" + rsDistributors.getString("name"));
					Cell.setCellStyle(styleLeft);
					Col++;

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(rsDistributors.getString("region"));
					Cell.setCellStyle(styleLeft);
					Col++;

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(rsDistributors.getString("city"));
					Cell.setCellStyle(styleLeft);
					Col++;

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(TSO);
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

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(Almoiz_Configs.getConsolidateNationalValue());
					Cell.setCellStyle(styleLeft);
					Col++;

					for (int i = 0; i < 9; i++) {
						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue("");
						Cell.setCellStyle(styleLeft);
						Col++;
					}
					
					/** SKUS Bill **/
					double totalGurr = 0, grandTotal = 0;
					for (int LRBId : LRBIds) {
						double total = 0;
//								System.out.println(
//										"select product_id from pep.inventory_products_view where is_visible=1 and lrb_type_id="
//												+ LRBId);
						ResultSet rsSKUs = s4.executeQuery(
								"select product_id from pep.inventory_products_view where is_visible=1 and lrb_type_id="
										+ LRBId);
						while (rsSKUs.next()) {
							System.out.println(
									"select  count(distinct `isa`.`id`) as mobileOrders FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
											+ "where isap.product_id=" + rsSKUs.getInt("product_id")
											+ " and i_isd.created_on between " + AlmoizDateUtils.getSQLDate(date)
											+ " and " + AlmoizDateUtils.getSQLDateNext(date) + " and isa.distributor_id="+distributorId+" and isa.order_id is null");
							ResultSet rsBills = s5.executeQuery(
									"select  count(distinct `isa`.`id`) as mobileOrders FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
											+ "where isap.product_id=" + rsSKUs.getInt("product_id")
											+ " and i_isd.created_on between " + AlmoizDateUtils.getSQLDate(date)
											+ " and " + AlmoizDateUtils.getSQLDateNext(date) + " and isa.distributor_id="+distributorId+" and isa.order_id is null");
							if (rsBills.first()) {
								Cell = (XSSFCell) Row.createCell((short) Col);
								Cell.setCellValue(rsBills.getDouble("mobileOrders"));
								Cell.setCellStyle(styleRight);
								Col++;
								total += rsBills.getDouble("mobileOrders");
								GrandTotal[increment] = GrandTotal[increment] + rsBills.getDouble("mobileOrders");
								increment++;
							}
						}
						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(total); // Total Per ROw
						Cell.setCellStyle(styleTotal);
						Col++;
						GrandTotal[increment] = GrandTotal[increment] + total;
						increment++;

						grandTotal += total;

						if (LRBId == 8 || LRBId == 9) {
							totalGurr += total;
						}

					}

					// Total gurr
					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(totalGurr); // Total Gurr Per Row
					Cell.setCellStyle(styleGurr);
					Col++;
					GrandTotal[increment] = GrandTotal[increment] + totalGurr;
					increment++;

					// Grand Total
					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(grandTotal); // Grand Total Per Row
					Cell.setCellStyle(styleTotalPerRow);
					Col++;
					GrandTotal[increment] = GrandTotal[increment] + grandTotal;
					increment++;

					/** SKUS Bill **/

					/** SKUS Sales **/
					double totalGurr1 = 0, grandTotal1 = 0;
					for (int LRBId : LRBIds) {
						double total = 0;
//								System.out.println(
//										"select product_id from pep.inventory_products_view where is_visible=1 and lrb_type_id="
//												+ LRBId);
						ResultSet rsSKUs = s4.executeQuery(
								"select product_id,unit_per_sku,liquid_in_ml from pep.inventory_products_view where is_visible=1 and lrb_type_id="
										+ LRBId);
						while (rsSKUs.next()) {
							System.out.println(
									"select  sum(raw_cases*(" + rsSKUs.getInt("unit_per_sku") + "*" + rsSKUs.getDouble("liquid_in_ml") + ")) as c FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
											+ "where isap.product_id=" + rsSKUs.getInt("product_id")
											+ " and i_isd.created_on between " + AlmoizDateUtils.getSQLDate(date)
											+ " and " + AlmoizDateUtils.getSQLDateNext(date) + " and isa.distributor_id="+distributorId+" and isa.order_id is null");
							ResultSet rsSales = s5.executeQuery(
									"select  sum(raw_cases*(" + rsSKUs.getInt("unit_per_sku") + "*" + rsSKUs.getDouble("liquid_in_ml") + ")) as c FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
											+ "where isap.product_id=" + rsSKUs.getInt("product_id")
											+ " and i_isd.created_on between " + AlmoizDateUtils.getSQLDate(date)
											+ " and " + AlmoizDateUtils.getSQLDateNext(date) + " and isa.distributor_id="+distributorId+" and isa.order_id is null");
							if (rsSales.first()) {
								Cell = (XSSFCell) Row.createCell((short) Col);
								Cell.setCellValue(rsSales.getDouble("c"));
								Cell.setCellStyle(styleRight);
								Col++;
								total += rsSales.getDouble("c");
								GrandTotal[increment] = GrandTotal[increment] + rsSales.getDouble("c");
								increment++;
							}
						}
						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(total); // Total Per ROw
						Cell.setCellStyle(styleTotal);
						Col++;
						GrandTotal[increment] = GrandTotal[increment] + total;
						increment++;

						grandTotal1 += total;

						if (LRBId == 8 || LRBId == 9) {
							totalGurr1 += total;
						}

					}

					// Total gurr
					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(totalGurr1); // Total Gurr Per Row
					Cell.setCellStyle(styleGurr);
					Col++;
					GrandTotal[increment] = GrandTotal[increment] + totalGurr1;
					increment++;

					// Grand Total
					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(grandTotal1); // Grand Total Per Row
					Cell.setCellStyle(styleTotalPerRow);
					Col++;
					GrandTotal[increment] = GrandTotal[increment] + grandTotal1;
					increment++;

					/** SKUS Sales **/

				} // end distributor Loop
			}

			/*******************************
			 * ROW Dynamic End
			 *******************************/

			/*******************************
			 * ROW Total Start
			 *******************************/

			RowCount++;
			Row = spreadsheet.createRow((short) RowCount);

//							for(int i=0; i<9; i++) {
//								row7Cell = (XSSFCell) row7.createCell((short) i);
//								row7Cell.setCellValue("");
//								Set2ndHeaderBackColorWhite(workbook, row7Cell);
//							}

			Col = 8;
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("Total");
			POIExcelUtils.getStyleWithCell(workbook, Cell, (excelColors.getYellow()), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getLeft());
			Col++;

			System.out.println(
					"select count(*) as total_pjp_shops from distributor_beat_plan_view pjpv where outlet_active=1 and  distributor_id not in ("
							+ dayNumber + ") and  day_number in (" + AlmoizDateUtils.getDayOfWeekByDate(date) + ")"
							+ whereASM);
			ResultSet rsPJPShops = s3.executeQuery(
					"select count(*) as total_pjp_shops from distributor_beat_plan_view pjpv where outlet_active=1 and  distributor_id not in ("
							+ dayNumber + ") and  day_number in (" + AlmoizDateUtils.getDayOfWeekByDate(date) + ")"
							+ whereASM);
			int totalPJPShops = (rsPJPShops.first()) ? rsPJPShops.getInt("total_pjp_shops") : 0;
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(totalPJPShops);
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(totalVisited);
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			Col++;

			double totalstrikeRate = (totalPJPShops != 0) ? ((double) totalVisited / (double) totalPJPShops) * 100 : 0;
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Utilities.getDisplayCurrencyFormatOneDecimal(totalstrikeRate) + "%");
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			Col++;

			System.out.println(
					"select  count(distinct `isa`.`outlet_id`) as billed FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
							+ "where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(date) + " and "
							+ AlmoizDateUtils.getSQLDateNext(date) + " and order_id is not null "
							+ whereASMDistributors);
			ResultSet rsTotalBilled = s3.executeQuery(
					"select  count(distinct `isa`.`outlet_id`) as billed FROM `inventory_sales_adjusted` `isa` JOIN `inventory_sales_adjusted_products` `isap` ON (`isa`.`id` = `isap`.`id`) join `inventory_sales_dispatch_invoices` `isdi`  on `isa`.`id`=`isdi`.`sales_id` join `inventory_sales_dispatch` `i_isd` on `i_isd`.`id`=`isdi`.`id`"
							+ "where i_isd.created_on between " + AlmoizDateUtils.getSQLDate(date) + " and "
							+ AlmoizDateUtils.getSQLDateNext(date) + " and order_id is not null "
							+ whereASMDistributors);

			int TotalBilledOUtlets = (rsTotalBilled.first()) ? rsTotalBilled.getInt("billed") : 0;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(TotalBilledOUtlets);
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			Col++;

			// Bill Productivity
			double Totalproductivity = (totalVisited != 0) ? ((double) TotalBilledOUtlets / (double) totalVisited) * 100
					: 0;
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Utilities.getDisplayCurrencyFormatOneDecimal(Totalproductivity) + "%");
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			Col++;

			// Total DS Volume productsVol
			double TotalDSVolume = (TotalBilledOUtlets != 0) ? ((double) productsVol / (double) TotalBilledOUtlets) : 0;
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Utilities.getDisplayCurrencyFormatOneDecimal(TotalDSVolume));
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			Col++;

			// Total DS Value
			double TotalDSVal = (TotalBilledOUtlets != 0) ? ((double) productsVal / (double) TotalBilledOUtlets) : 0;
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Utilities.getDisplayCurrencyFormatOneDecimal(TotalDSVal));
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(TotalBilledOUtlets);
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());

			Col++;

			// Total SKU/Call
//			System.out.println(
//					"SELECT count(product_id) as skus"
//							+ " from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id  where isa.adjusted_on between "
//							+ Utilities.getSQLDate() + " and " + Utilities.getSQLDateNext(EndDate)
//							+ whereRSMDistributors + whereASMDistributors + whereRegionDistributors + whereOrderBookerBooked + whereDistributors);

			double TotalSKUPerCall = (TotalBilledOUtlets != 0) ? ((double) totalSkusCount / (double) TotalBilledOUtlets)
					: 0;
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Utilities.getDisplayCurrencyFormatTwoDecimalFixed(TotalSKUPerCall));
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());

			Col++;

			for (int j = 0; j < GrandTotal.length - 2; j++) {

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(GrandTotal[j]);
				if (j != green && j != brown) {
//					System.out.println("J not : " + j);
//					System.out.println("green not : " + green);
//					System.out.println("brown not : " + brown);
					POIExcelUtils.getStyleWithCell(workbook, Cell,
							((TotalsBlue.contains(j)) ? excelColors.getBlue() : excelColors.getLightGray()), style,
							font, ((TotalsBlue.contains(j)) ? excelFontColors.getWhite() : excelFontColors.getBlack()),
							true, true, fontAlign.getCenter());
				} else if (j == green) {
					POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getGreen(), style, font,
							excelFontColors.getWhite(), true, true, fontAlign.getCenter());
				} else if (j == brown) {
					POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getBrown(), style, font,
							excelFontColors.getWhite(), true, true, fontAlign.getCenter());
				}

				Col++;

			}
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(GrandTotal[GrandTotal.length - 2]);
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getBrown(), style, font,
					excelFontColors.getWhite(), true, true, fontAlign.getCenter());
			Col++;
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(GrandTotal[GrandTotal.length - 1]);
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getGreen(), style, font,
					excelFontColors.getWhite(), true, true, fontAlign.getCenter());
			Col++;
			/*******************************
			 * ROW Total End
			 *******************************/

			/*****************************************************************************************************/

			if (date.equals(this.EndDate)) {
				break;
			}
			date = DateUtils.addDays(date, 1);
			for (int i = 0; i < 300; i++) {
				// System.out.println("Auto Sizing - "+i);
				try {
					spreadsheet.autoSizeColumn(i);
				} catch (Exception e) {
					System.out.println(i);
					e.printStackTrace();
				}
			}

		} // ENd of Date while

		if (isMTD) {
			// new com.pbc.pushmail.scorecards.NewKPIsMTDExcel().CreateMTD(workbook,
			// StartDate, EndDate, ASM_Id);
		}

		s.close();
		s2.close();
		s3.close();
		s4.close();
		s5.close();
		ds.dropConnection();

		FileOutputStream out = new FileOutputStream(new File(filename));
		workbook.write(out);
		out.close();

	}

	/**
	 * To get date in "dd/MM/yyyy" format as string to display on UI
	 * 
	 * @param val
	 * @return
	 */
	public String getDisplayDateFormatWithDashes(Date val) {

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			return "" + format.format(val);

		} else {

			return null;

		}

	}
}
