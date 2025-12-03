package com.pbc.pushmail.scorecards;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

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

public class NewKPIsMTDExcel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath() + "/CashInflowSummary.pdf";

	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	Statement s5;

	public void CreateMTD(XSSFWorkbook workbook, Date StartDate, Date EndDate, long ASM_Id) throws Exception {

		ds.createConnection();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
		s5 = ds.createStatement();

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

		String whereASMDistributors = (ASM_Id != 0)
				? "  and distributor_id in (select distinct distributor_id from distributor_beat_plan pjp where pjp.asm_id="
						+ ASM_Id + ")"
				: "";

		System.out.println("MTD => StartDate : " + Utilities.getDisplayDateFormat(StartDate));
		System.out.println("MTD => EndDate : " + Utilities.getDisplayDateFormat(EndDate));

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
				"DS/SHOP VAL", "DS/SHOP VOL", "DS KGs", "UP", "SKU/CALL" };

		int totalBlankColumns = columnNames.length;
		int totalSKUsColumns = totalLrbs + totalSKUs;

		int mergeFromCol = 0;
		int mergeToCol = 0;
		/****************************** Data ***************************************/

		/******************************
		 * Start Loop
		 ******************************/

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

		XSSFSheet spreadsheet = workbook
				.createSheet(getDisplayDateFormatWithDashes(StartDate) + "-" + getDisplayDateFormatWithDashes(EndDate));

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
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getWhite(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getLeft());

		/*******************************
		 * ROW 1 End
		 *******************************/

		/*******************************
		 * ROW 2 Start
		 *******************************/

		RowCount++;
		Row = spreadsheet.createRow((short) RowCount);
		Cell = (XSSFCell) Row.createCell((short) 0);

		Cell.setCellValue("Date : " + Utilities.getDisplayDateFormat(StartDate) + " - "
				+ Utilities.getDisplayDateFormat(EndDate));

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
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getBrown(), style, font, excelFontColors.getWhite(),
				false, true, fontAlign.getCenter());
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
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getPurple(), style, font, excelFontColors.getWhite(),
				false, true, fontAlign.getCenter());
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

		String DateCondition = "adjusted_on";

		// arraySize = arraySize + 9;
		for (int u = 0; u < arraySize; u++) {
			GrandTotal[u] = 0.00;
		}

		// System.out.println("arraySize : " + arraySize);

		String inactiveDistributor = "";
		int c = 0;
		System.out.println("select distinct distributor_id from inventory_sales_adjusted isa where " + DateCondition
				+ " between " + Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
				+ whereASMDistributors);
		ResultSet rsInActiveDistributors = s
				.executeQuery("select distinct distributor_id from inventory_sales_adjusted isa where " + DateCondition
						+ " between " + Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
						+ whereASMDistributors);
		while (rsInActiveDistributors.next()) {
			inactiveDistributor += (c == 0) ? rsInActiveDistributors.getLong("distributor_id")
					: "," + rsInActiveDistributors.getLong("distributor_id");
			c++;
		}
		String whereInActiveDistributors = (inactiveDistributor.length() > 0)
				? " or distributor_id in (" + inactiveDistributor + ")"
				: "";
//			System.out.println(
//					"select distributor_id, name,(select DISPLAY_NAME from users u where u.id in (select asm_id  from distributor_beat_plan pjp where pjp.distributor_id=cd.distributor_id limit 1)) as tso,(select region_name from common_regions cr where cr.region_id=cd.region_id) as region , city, (select Display_name from users where id=rsm_id) asm, (select Display_name from users where id=snd_id) rsm from common_distributors cd where distributor_id not in (100914,100915) and is_active=1 "
//							+ whereASMDistributors + whereInActiveDistributors);
		ResultSet rsDistributors = s.executeQuery(
				"select distributor_id, name, (select region_name from common_regions cr where cr.region_id=cd.region_id) as region , city, (select Display_name from users where id=rsm_id) asm, (select Display_name from users where id=snd_id) rsm from common_distributors cd where distributor_id not in (100914,100915) and is_active=1 "
						+ whereASMDistributors + whereInActiveDistributors);
		while (rsDistributors.next()) {

			long distributor_id = rsDistributors.getLong("distributor_id");

			int tso_id = 0;
			ResultSet rsTso_id = s2
					.executeQuery("select asm_id as tso_id  from distributor_beat_plan where distributor_id="
							+ distributor_id + " limit 1");
			if (rsTso_id.first()) {
				tso_id = rsTso_id.getInt("tso_id");
			}

			String TSO = "";
			ResultSet rsTso = s2.executeQuery("select DISPLAY_NAME from users u where u.id=" + tso_id);
			if (rsTso.first()) {
				TSO = rsTso.getString("DISPLAY_NAME");
			}

			ResultSet rsCountSales = s2
					.executeQuery("select count(*) as countSales from inventory_sales_adjusted isa where  isa."
							+ DateCondition + "  between  " + Utilities.getSQLDate(StartDate) + " and "
							+ Utilities.getSQLDateNext(EndDate) + " and distributor_id=" + distributor_id);
			if (rsCountSales.first()) {

				if (rsCountSales.getInt("countSales") > 0) { // if distributor sale exists

//						System.out.println(
//								"select distinct booked_by, (select DISPLAY_NAME from users u where u.id=booked_by) as psr_name from inventory_sales_adjusted where "
//										+ DateCondition + " between " + Utilities.getSQLDate(StartDate) + " and "
//										+ Utilities.getSQLDateNext(EndDate) + " and distributor_id=" + distributor_id
//										+ " and order_id is not null");
					ResultSet rsOrderBookers = s3.executeQuery(
							"select distinct booked_by, (select DISPLAY_NAME from users u where u.id=booked_by) as psr_name from inventory_sales_adjusted where "
									+ DateCondition + " between " + Utilities.getSQLDate(StartDate) + " and "
									+ Utilities.getSQLDateNext(EndDate) + " and distributor_id=" + distributor_id
									+ " and order_id is not null");
					while (rsOrderBookers.next()) {
						int increment = 0;

						int orderBookerId = rsOrderBookers.getInt("booked_by");

						RowCount++;
						Row = spreadsheet.createRow((short) RowCount);
						Col = 0;

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(orderBookerId);
						Cell.setCellStyle(styleRight);
						Col++;

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(orderBookerId + "-" + rsOrderBookers.getString("psr_name"));
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
						Cell.setCellValue(TSO);
						Cell.setCellStyle(styleLeft);
						Col++;

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(rsDistributors.getString("asm"));
						Cell.setCellStyle(styleLeft);
						Col++;

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(rsDistributors.getString("rsm"));
						Cell.setCellStyle(styleLeft);
						Col++;

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue("Tahir Shahbaz");
						Cell.setCellStyle(styleLeft);
						Col++;

						int PJPID = 0;
						ResultSet rsPjp = s4
								.executeQuery("select beat_plan_id from inventory_sales_adjusted where distributor_id="
										+ distributor_id + " and booked_by=" + orderBookerId + " and " + DateCondition
										+ " between " + Utilities.getSQLDate(StartDate) + " and "
										+ Utilities.getSQLDateNext(EndDate) + " limit 1");
						if (rsPjp.first()) {
							PJPID = rsPjp.getInt("beat_plan_id");
						}

						int ScheduledCalls = 0;
						String PlannedOutletIDs = "";
						ResultSet rs5 = s4.executeQuery(
								"select count(outlet_id) as ScheduledCalls, group_concat(outlet_id) as PlannedOutletIDs from distributor_beat_plan_log where log_date between "
										+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(StartDate)
										+ " and distributor_id = " + distributor_id
										+ " and day_number = dayofweek(log_date) and id = " + PJPID
										+ " and assigned_to=" + orderBookerId);
						if (rs5.first()) {
							ScheduledCalls = rs5.getInt("ScheduledCalls");
							PlannedOutletIDs = rs5.getString("PlannedOutletIDs");
						}

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(ScheduledCalls); // PJP Shops TOday
						Cell.setCellStyle(styleRight);
						Col++;

						int OrderCount = 0;
						String OrderIDs = "";
						String OutletIDsOrdered = "";
//							System.out.println(
//									"select group_concat(mo.id) order_ids, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, group_concat(distinct mo.outlet_id) outlet_ids_ordered from mobile_order mo where mo.created_on between "
//											+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
//											+ " and mo.beat_plan_id =" + PJPID + " and mo.created_by="+orderBookerId + " and  mo.distributor_id="+distributor_id);				
						ResultSet rs45 = s4.executeQuery(
								"select group_concat(mo.id) order_ids, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, group_concat(distinct mo.outlet_id) outlet_ids_ordered from inventory_sales_adjusted mo where mo.adjusted_on between "
										+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
										+ " and mo.beat_plan_id =" + PJPID + " and mo.booked_by=" + orderBookerId
										+ " and  mo.distributor_id=" + distributor_id);
						if (rs45.first()) {
							OrderIDs = rs45.getString("order_ids");
							OutletIDsOrdered = rs45.getString("outlet_ids_ordered");
							OrderCount = rs45.getInt("order_count");
							// UniqueOutletsOrdered = rs45.getInt("unique_outlets_ordered");
						}

						if (OrderIDs != null && OrderIDs.endsWith(",")) {
							OrderIDs = OrderIDs.substring(0, OrderIDs.length() - 1);
						}

						if (OutletIDsOrdered != null && OutletIDsOrdered.endsWith(",")) {
							OutletIDsOrdered = OutletIDsOrdered.substring(0, OutletIDsOrdered.length() - 1);
						}

						int ActuallSchCalls = 0;
//						System.out.println("select count(id) from common_outlets where id in ("
//								+ PlannedOutletIDs
//								+ ") and id in (SELECT distinct outlet_id FROM mobile_order_zero where created_on between "
//								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
//								+ " and created_by=" + orderBookerId
//								+ ") or id in (select distinct id from common_outlets where id in (" + PlannedOutletIDs
//								+ ") and id in (" + OutletIDsOrdered + "))");
						ResultSet rs101 = s4.executeQuery("select count(id) from common_outlets where id in ("
								+ PlannedOutletIDs + ")  or id in (select distinct id from common_outlets where id in ("
								+ PlannedOutletIDs + ") and id in (" + OutletIDsOrdered + "))");
						if (rs101.first()) {
							ActuallSchCalls = rs101.getInt(1);
						}

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(ActuallSchCalls); // Visits
						Cell.setCellStyle(styleRight);
						Col++;

						double CallCompRate = 0;
						if (ScheduledCalls != 0) {
							CallCompRate = ((double) ActuallSchCalls / (double) ScheduledCalls) * 100; // Visited /
																										// PJP SHOPS
																										// This
							// Day
							// *100 = STRIKE RATE %
						}

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(Math.round(CallCompRate) + "%"); // STRIKE RATE %
						Cell.setCellStyle(styleRight);
						Col++;

						int OrdersFromPlannedOutlets = 0;
						ResultSet rs10 = s4.executeQuery("select count(id) from common_outlets where id in ("
								+ PlannedOutletIDs + ") and id in (" + OutletIDsOrdered + ")");
						if (rs10.first()) {
							OrdersFromPlannedOutlets = rs10.getInt(1);
						}

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(OrdersFromPlannedOutlets); // BILLED OUTLETS
						Cell.setCellStyle(styleRight);
						Col++;

						double billProductivity = 0;
						if (ActuallSchCalls != 0) {
							billProductivity = ((double) OrdersFromPlannedOutlets / (double) ActuallSchCalls) * 100; // Visited
																														// /
																														// PJP
							// This Day
						}

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(Math.round(billProductivity) + "%"); // Bill Productivity%
						Cell.setCellStyle(styleRight);
						Col++;

						double LinesAmount = 0;
//							System.out.println(
//									"select sum(mo.net_amount) total_lines_amount from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mo.id in ("
//											+ OrderIDs + ") and mo.created_by=" + orderBookerId + " and mo.beat_plan_id ="
//											+ PJPID + " and mo.created_on BETWEEN " + Utilities.getSQLDate(StartDate) + " and "
//											+ Utilities.getSQLDateNext(EndDate) + " and mo.distributor_id="+distributor_id);
						ResultSet rsAmount = s4.executeQuery(
								"select sum(mo.net_amount) total_lines_amount from inventory_sales_adjusted mo join inventory_sales_adjusted_products mop on mo.id = mop.id where mo.id in ("
										+ OrderIDs + ") and mo.booked_by=" + orderBookerId + " and mo.beat_plan_id ="
										+ PJPID + " and mo.adjusted_on BETWEEN " + Utilities.getSQLDate(StartDate)
										+ " and " + Utilities.getSQLDateNext(EndDate) + " and mo.distributor_id="
										+ distributor_id);
						if (rsAmount.first()) {
							LinesAmount = rsAmount.getDouble("total_lines_amount");
						}

						double DropAmount = 0;
						if (OrderCount != 0) {
							DropAmount = (double) LinesAmount / (double) OrderCount;
						}

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(
								Utilities.parseDouble(Utilities.getDisplayCurrencyFormatTwoDecimalFixed(DropAmount))); // Drop
																														// Amount
						Cell.setCellStyle(styleRight);
						Col++;

						double TotalQuantitySold = 0;
						if (ActuallSchCalls != 0) {
							TotalQuantitySold = Math
									.round(((double) OrdersFromPlannedOutlets * 100) / (double) ActuallSchCalls);
						}

						double DropSize = 0;
						if (OrderCount != 0) {
							DropSize = (double) TotalQuantitySold / (double) OrderCount;
						}

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(
								Utilities.parseDouble(Utilities.getDisplayCurrencyFormatTwoDecimalFixed(DropSize))); // Drop
																														// Size
						Cell.setCellStyle(styleRight);
						Col++;

						int Kgs = 0;

						ResultSet rsKgs = s5.executeQuery(
								"SELECT sum(raw_cases*(select unit_per_sku from inventory_products_view ipv where ipv.product_id=isap.product_id )) as c FROM pep.inventory_sales_adjusted_products isap where "
										+ " id in(SELECT id FROM pep.inventory_sales_adjusted where " + DateCondition
										+ " between " + Utilities.getSQLDate(StartDate) + " and "
										+ Utilities.getSQLDateNext(EndDate) + " and booked_by=" + orderBookerId + ")");
						if (rsKgs.first()) {
							Kgs = rsKgs.getInt("c");
						}

						double DSKgs = 0;

						if (OrderCount != 0) {
							DSKgs = (double) Kgs / (double) OrderCount;
						}

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(Utilities.parseDouble(Utilities.getDisplayCurrencyFormatOneDecimal(DSKgs))); // Drop
																														// KGs
						Cell.setCellStyle(styleRight);
						Col++;

						int UP = 0;

						ResultSet rs40 = s4.executeQuery(
								"select  COUNT(DISTINCT outlet_id) c from inventory_sales_adjusted mo where mo.adjusted_on between "
										+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
										+ "  and mo.booked_by=" + orderBookerId + " and  mo.distributor_id="
										+ distributor_id);
						if (rs40.first()) {
							UP = rs40.getInt("c");
						}

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(UP); // Drop Size
						Cell.setCellStyle(styleRight);
						Col++;

						int countSKU = 0;
//							System.out.println(
//									"select count(*) total_lines_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mo.id in ("
//											+ OrderIDs + ") and mo.created_by=" + orderBookerId + " and mo.beat_plan_id ="
//											+ PJPID+ " and mo.created_on BETWEEN "+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate));
						ResultSet rs12 = s4.executeQuery(
								"select count(*) total_lines_sold from inventory_sales_adjusted mo join inventory_sales_adjusted_products mop on mo.id = mop.id where mo.id in ("
										+ OrderIDs + ") and mo.booked_by=" + orderBookerId + " and mo.beat_plan_id ="
										+ PJPID + " and mo.adjusted_on BETWEEN " + Utilities.getSQLDate(StartDate)
										+ " and " + Utilities.getSQLDateNext(EndDate));
						if (rs12.first()) {
							countSKU = rs12.getInt("total_lines_sold");
						}

						double SKUPerOrder = 0;
						if (OrderCount != 0) {
							SKUPerOrder = (double) countSKU / (double) OrderCount;
						}

						// System.out.println(" SKUPerOrder : "+SKUPerOrder);

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(
								Utilities.parseDouble(Utilities.getDisplayCurrencyFormatOneDecimal(SKUPerOrder))); // SKU
																													// per
																													// call
						Cell.setCellStyle(styleRight);
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
//													+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
//													+ " and booked_by=" + orderBookerId + ")");
								ResultSet rsBills = s5.executeQuery(
										"SELECT COUNT(*) as c FROM pep.inventory_sales_invoices_products where product_id="
												+ rsSKUs.getInt("product_id")
												+ " and id in(SELECT id FROM pep.inventory_sales_invoices where created_on between "
												+ Utilities.getSQLDate(StartDate) + " and "
												+ Utilities.getSQLDateNext(EndDate) + " and booked_by=" + orderBookerId
												+ ")");
								if (rsBills.first()) {
									Cell = (XSSFCell) Row.createCell((short) Col);
									Cell.setCellValue(rsBills.getDouble("c"));
									Cell.setCellStyle(styleRight);
									Col++;
									total += rsBills.getDouble("c");
									GrandTotal[increment] = GrandTotal[increment] + rsBills.getDouble("c");
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
//													+ DateCondition + " between " + Utilities.getSQLDate(StartDate) + " and "
//													+ Utilities.getSQLDateNext(EndDate) + " and booked_by=" + orderBookerId
//													+ ")");
								ResultSet rsSales = s5.executeQuery("SELECT sum(raw_cases*("
										+ rsSKUs.getInt("unit_per_sku") + "*" + rsSKUs.getDouble("liquid_in_ml")
										+ ")) as c FROM pep.inventory_sales_adjusted_products where product_id="
										+ rsSKUs.getInt("product_id")
										+ " and id in(SELECT id FROM pep.inventory_sales_adjusted where "
										+ DateCondition + " between " + Utilities.getSQLDate(StartDate) + " and "
										+ Utilities.getSQLDateNext(EndDate) + " and booked_by=" + orderBookerId + ")");
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

					} // End of order booker Query

					/***** Desk Sales Start ******/
//						System.out.println("select id from inventory_sales_adjusted where " + DateCondition
//								+ " between " + Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
//								+ " and distributor_id=" + distributor_id + "  and order_id is null");
					ResultSet rsDeskSlaes = s3
							.executeQuery("select id from inventory_sales_adjusted where " + DateCondition + " between "
									+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
									+ " and distributor_id=" + distributor_id + "  and order_id is null");
					while (rsDeskSlaes.next()) {

						int orderID = rsDeskSlaes.getInt("id");

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
						Cell.setCellValue(TSO);
						Cell.setCellStyle(styleLeft);
						Col++;

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(rsDistributors.getString("asm"));
						Cell.setCellStyle(styleLeft);
						Col++;

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(rsDistributors.getString("rsm"));
						Cell.setCellStyle(styleLeft);
						Col++;

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue("Tahir Shahbaz");
						Cell.setCellStyle(styleLeft);
						Col++;

						for (int i = 0; i < 10; i++) {
							Cell = (XSSFCell) Row.createCell((short) Col);
							Cell.setCellValue(0);
							Cell.setCellStyle(styleRight);
							Col++;
						}

						int increment = 0;

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
//											"SELECT COUNT(*) as c FROM pep.inventory_sales_invoices_products where product_id="
//													+ rsSKUs.getInt("product_id") + " and id=" + orderID);
								ResultSet rsBills = s5.executeQuery(
										"SELECT COUNT(*) as c FROM pep.inventory_sales_invoices_products where product_id="
												+ rsSKUs.getInt("product_id") + " and id=" + orderID);
								if (rsBills.first()) {
									Cell = (XSSFCell) Row.createCell((short) Col);
									Cell.setCellValue(rsBills.getDouble("c"));
									Cell.setCellStyle(styleRight);
									Col++;
									total += rsBills.getDouble("c");
									GrandTotal[increment] = GrandTotal[increment] + rsBills.getDouble("c");
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
//									System.out.println("SELECT sum(raw_cases*(" + rsSKUs.getInt("unit_per_sku") + "*"
//											+ rsSKUs.getDouble("liquid_in_ml")
//											+ ")) as c FROM pep.inventory_sales_adjusted_products where product_id="
//											+ rsSKUs.getInt("product_id") + " and id=" + orderID);
								ResultSet rsSales = s5.executeQuery("SELECT sum(raw_cases*("
										+ rsSKUs.getInt("unit_per_sku") + "*" + rsSKUs.getDouble("liquid_in_ml")
										+ ")) as c FROM pep.inventory_sales_adjusted_products where product_id="
										+ rsSKUs.getInt("product_id") + " and id=" + orderID);
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

					}
					/***** Desk Sales Start ******/
				} else {

					RowCount++;
					Row = spreadsheet.createRow((short) RowCount);
					Col = 0;

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue("");
					Cell.setCellStyle(styleLeft);
					Col++;

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue("");
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
					Cell.setCellValue(TSO);
					Cell.setCellStyle(styleLeft);
					Col++;

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(rsDistributors.getString("asm"));
					Cell.setCellStyle(styleLeft);
					Col++;

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(rsDistributors.getString("rsm"));
					Cell.setCellStyle(styleLeft);
					Col++;

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue("Tahir Shahbaz");
					Cell.setCellStyle(styleLeft);
					Col++;

					for (int i = 0; i < 10; i++) {
						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(0);
						Cell.setCellStyle(styleRight);
						Col++;
					}

					for (int j = 0; j < GrandTotal.length - 2; j++) {
						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(0);
						if (j != green && j != brown) {
							Cell.setCellStyle((!TotalsBlue.contains(j) ? styleRight : styleTotal));
						} else if (j == green) {
							Cell.setCellStyle(styleTotalPerRow);
						} else if (j == brown) {
							Cell.setCellStyle(styleGurr);
						}
						Col++;

					}

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(0);
					Cell.setCellStyle(styleGurr);
					Col++;

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(0);
					Cell.setCellStyle(styleTotalPerRow);
					POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getGreen(), style, font,
							excelFontColors.getWhite(), true, true, fontAlign.getCenter());
					Col++;

				}

			} // End of rsCountSales

		} // end of Distributors Loop

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

		int TotalScheduledCalls = 0;
		String TotalPlannedOutletIDs = "";
		ResultSet rs5 = s3.executeQuery(
				"select count(outlet_id) as TotalScheduledCalls, group_concat(outlet_id) as PlannedOutletIDs  from distributor_beat_plan_log where log_date between "
						+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(StartDate)
						+ " and day_number = dayofweek(log_date)");
		if (rs5.first()) {
			TotalScheduledCalls = rs5.getInt("TotalScheduledCalls");
			TotalPlannedOutletIDs = rs5.getString("PlannedOutletIDs");
		}

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(TotalScheduledCalls); // Total PJP Shops
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		Col++;

		int TotalOrderCount = 0;
		String TotalOrderIDs = "";
		String TotalOutletIDsOrdered = "";
//			System.out.println(
//					"select group_concat(mo.id) order_ids, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, group_concat(distinct mo.outlet_id) outlet_ids_ordered from mobile_order mo where mo.created_on between "
//							+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
//							+ " and mo.beat_plan_id =" + PJPID + " and mo.created_by="+orderBookerId + " and  mo.distributor_id="+distributor_id);
		ResultSet rs45 = s4.executeQuery(
				"select group_concat(mo.id) order_ids, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, group_concat(distinct mo.outlet_id) outlet_ids_ordered from inventory_sales_adjusted mo where mo.adjusted_on between "
						+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate));
		if (rs45.first()) {
			TotalOrderIDs = rs45.getString("order_ids");
			TotalOutletIDsOrdered = rs45.getString("outlet_ids_ordered");
			TotalOrderCount = rs45.getInt("order_count");
			// UniqueOutletsOrdered = rs45.getInt("unique_outlets_ordered");
		}

		if (TotalOrderIDs != null && TotalOrderIDs.endsWith(",")) {
			TotalOrderIDs = TotalOrderIDs.substring(0, TotalOrderIDs.length() - 1);
		}

		if (TotalOutletIDsOrdered != null && TotalOutletIDsOrdered.endsWith(",")) {
			TotalOutletIDsOrdered = TotalOutletIDsOrdered.substring(0, TotalOutletIDsOrdered.length() - 1);
		}

		if (TotalPlannedOutletIDs != null && TotalPlannedOutletIDs.endsWith(",")) {
			TotalPlannedOutletIDs = TotalOutletIDsOrdered.substring(0, TotalPlannedOutletIDs.length() - 1);
		}

		int TotalActuallSchCalls = 0;
//			System.out.println("select count(id) from common_outlets where id in (" + TotalPlannedOutletIDs
//					+ ") and id in (SELECT distinct outlet_id FROM mobile_order_zero where created_on between "
//					+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
//					+ ") or id in (select distinct id from common_outlets where id in (" + TotalPlannedOutletIDs
//					+ ") and id in (" + TotalOutletIDsOrdered + "))");
		ResultSet rs101 = s4.executeQuery("select count(id) from common_outlets where id in (" + TotalPlannedOutletIDs
				+ ")  or id in (select distinct id from common_outlets where id in (" + TotalPlannedOutletIDs
				+ ") and id in (" + TotalOutletIDsOrdered + "))");
		if (rs101.first()) {
			TotalActuallSchCalls = rs101.getInt(1);
		}

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(TotalActuallSchCalls); // Total Visits
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		Col++;

		double TotalCallCompRate = 0;
		if (TotalScheduledCalls != 0) {
			TotalCallCompRate = ((double) TotalActuallSchCalls / (double) TotalScheduledCalls) * 100; // Total =>
																										// Visited /
																										// PJP SHOPS
			// This
			// Day
			// *100 = STRIKE RATE %
		}

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(Math.round(TotalCallCompRate) + "%"); // Total STRIKE RATE %
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		Col++;

		int TotalOrdersFromPlannedOutlets = 0;
		ResultSet rs10 = s3.executeQuery("select count(distinct id) from common_outlets where id in ("
				+ TotalPlannedOutletIDs + ") and id in (" + TotalOutletIDsOrdered + ")");
		if (rs10.first()) {
			TotalOrdersFromPlannedOutlets = rs10.getInt(1);
		}

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(TotalOrdersFromPlannedOutlets); // Total BILLED OUTLETS
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		Col++;

		double TotalBillProductivity = 0;
		if (TotalActuallSchCalls != 0) {
			TotalBillProductivity = ((double) TotalOrdersFromPlannedOutlets / (double) TotalActuallSchCalls) * 100; // Visited
																													// /
																													// PJP
			// This Day
		}

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(Math.round(TotalBillProductivity) + "%"); // Total Bill Productivity %
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		Col++;

		double TotalLinesAmount = 0;
//			System.out.println(
//					"select sum(mo.net_amount) total_lines_amount from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mo.id in ("
//							+ OrderIDs + ") and mo.created_by=" + orderBookerId + " and mo.beat_plan_id ="
//							+ PJPID + " and mo.created_on BETWEEN " + Utilities.getSQLDate(StartDate) + " and "
//							+ Utilities.getSQLDateNext(EndDate) + " and mo.distributor_id="+distributor_id);
		ResultSet rsAmount = s3.executeQuery(
				"select sum(mo.net_amount) total_lines_amount from inventory_sales_adjusted mo join inventory_sales_adjusted_products mop on mo.id = mop.id where mo.id in ("
						+ TotalOrderIDs + ")  and mo.created_on BETWEEN " + Utilities.getSQLDate(StartDate) + " and "
						+ Utilities.getSQLDateNext(EndDate));
		if (rsAmount.first()) {
			TotalLinesAmount = rsAmount.getDouble("total_lines_amount");
		}

		double TotalDropAmount = 0;
		if (TotalOrderCount != 0) {
			TotalDropAmount = (double) TotalLinesAmount / (double) TotalOrderCount;
		}

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(Utilities.parseDouble(Utilities.getDisplayCurrencyFormatTwoDecimalFixed(TotalDropAmount))); // Total
																														// Drop
																														// Amount
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		Col++;

		double TotalQuantitySold = 0;
		if (TotalActuallSchCalls != 0) {
			TotalQuantitySold = Math
					.round(((double) TotalOrdersFromPlannedOutlets * 100) / (double) TotalActuallSchCalls);
		}

		double TotalDropSize = 0;
		if (TotalOrderCount != 0) {
			TotalDropSize = (double) TotalQuantitySold / (double) TotalOrderCount;
		}

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(Utilities.parseDouble(Utilities.getDisplayCurrencyFormatTwoDecimalFixed(TotalDropSize))); // Total
																													// Drop
																													// Size
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		Col++;

		int TotalKgs = 0;

		ResultSet rsTotalKgs = s5.executeQuery(
				"SELECT sum(raw_cases*(select unit_per_sku from inventory_products_view ipv where ipv.product_id=isap.product_id )) as c FROM pep.inventory_sales_adjusted_products isap where "
						+ " id in(SELECT id FROM pep.inventory_sales_adjusted where " + DateCondition + " between "
						+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) + ")");
		if (rsTotalKgs.first()) {
			TotalKgs = rsTotalKgs.getInt("c");
		}

		double TotalDSKgs = 0;

		if (TotalOrderCount != 0) {
			TotalDSKgs = (double) TotalKgs / (double) TotalOrderCount;
		}

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(Utilities.parseDouble(Utilities.getDisplayCurrencyFormatTwoDecimalFixed(TotalDSKgs)));
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		Col++;

		int TotalUP = 0;

		ResultSet rs40 = s3.executeQuery(
				"select  COUNT(DISTINCT outlet_id) c from inventory_sales_adjusted mo where mo.adjusted_on between "
						+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate));
		if (rs40.first()) {
			TotalUP = rs40.getInt("c");
		}

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(TotalUP); // Total UP
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		Col++;

		int TotalLinesSold = 0;
//			System.out.println(
//					"select count(*) total_lines_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mo.id in ("
//							+ TotalOrderIDs + ")  and mo.created_on BETWEEN " + Utilities.getSQLDate(StartDate) + " and "
//							+ Utilities.getSQLDateNext(EndDate));
		ResultSet rs12 = s3.executeQuery(
				"select count(*) total_lines_sold from inventory_sales_adjusted mo join inventory_sales_adjusted_products mop on mo.id = mop.id where mo.id in ("
						+ TotalOrderIDs + ")  and mo.adjusted_on BETWEEN " + Utilities.getSQLDate(StartDate) + " and "
						+ Utilities.getSQLDateNext(EndDate));
		if (rs12.first()) {
			TotalLinesSold = rs12.getInt("total_lines_sold");
		}

		double TotalSKUPerOrder = 0;
		//// System.out.println("TotalOrderCount : " + TotalOrderCount + "
		//// TotalLinesSold : " + TotalLinesSold);
		if (TotalOrderCount != 0) {
			TotalSKUPerOrder = (double) TotalLinesSold / (double) TotalOrderCount;
		}
		// System.out.println("TotalSKUPerOrder : " + TotalSKUPerOrder);

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(Utilities.parseDouble(Utilities.getDisplayCurrencyFormatTwoDecimalFixed(TotalSKUPerOrder))); // Total
																														// SKU
																														// Per
																														// Order
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		Col++;

//						
		for (int j = 0; j < GrandTotal.length - 2; j++) {

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(GrandTotal[j]);
			if (j != green && j != brown) {
//					System.out.println("J not : " + j);
//					System.out.println("green not : " + green);
//					System.out.println("brown not : " + brown);
				POIExcelUtils.getStyleWithCell(workbook, Cell,
						((TotalsBlue.contains(j)) ? excelColors.getBlue() : excelColors.getLightGray()), style, font,
						((TotalsBlue.contains(j)) ? excelFontColors.getWhite() : excelFontColors.getBlack()), true,
						true, fontAlign.getCenter());
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
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getBrown(), style, font, excelFontColors.getWhite(),
				true, true, fontAlign.getCenter());
		Col++;
		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(GrandTotal[GrandTotal.length - 1]);
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getGreen(), style, font, excelFontColors.getWhite(),
				true, true, fontAlign.getCenter());
		Col++;
		/*******************************
		 * ROW Total End
		 *******************************/

		/*****************************************************************************************************/

		for (int i = 0; i < 400; i++) {
			// System.out.println("Auto Sizing - "+i);
			try {
				spreadsheet.autoSizeColumn(i);
			} catch (Exception e) {
				System.out.println(i);
				e.printStackTrace();
			}
		}

		s.close();
		s2.close();
		s3.close();
		s4.close();
		s5.close();
		ds.dropConnection();

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
