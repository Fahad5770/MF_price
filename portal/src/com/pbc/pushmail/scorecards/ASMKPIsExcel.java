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

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
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

public class ASMKPIsExcel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath() + "/CashInflowSummary.pdf";

	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	Statement s10;

	// Date StartDate = Utilities.getDateByDays(0);
	// //Utilities.parseDate("13/02/2016");
	// Date EndDate =
	// Utilities.getDateByDays(0);//Utilities.parseDate("13/02/2016");
	int NoOfDaysToGoBack = -1;

	Date StartDate = Utilities.getDateByDays(NoOfDaysToGoBack); // Utilities.parseDate("13/02/2016");
	Date EndDate = Utilities.getDateByDays(NoOfDaysToGoBack);

	//// Date StartDate = Utilities.parseDate("01/09/2016");
	//// Date EndDate = Utilities.parseDate("28/09/2016");

	Date Yesterday = Utilities.getDateByDays(-2);

	long ASM_ID = 0;

	public static void main(String[] args) throws Exception {

	}

	public ASMKPIsExcel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
		s10 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6) {
			Yesterday = Utilities.getDateByDays(-3);
		}

	}

	public void createPdf(String filename, long ASM_ID, boolean isMTD) throws Exception {

		if (isMTD) {
			// this.StartDate = Utilities.getStartDateByDate(Utilities.getDateByDays(0));
			// this.EndDate = Utilities.getDateByDays(0);

			this.StartDate = Utilities.getStartDateByDate(Utilities.getDateByDays(NoOfDaysToGoBack));
			this.EndDate = Utilities.getDateByDays(NoOfDaysToGoBack);
		}

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("cell types");

		int month = Utilities.getMonthNumberByDate(EndDate);
		int year = Utilities.getYearByDate(EndDate);

		int FirstRowCount = 0;

		int RowCount = 0;

		long DistributorID1 = 0;

		// Report Heading

		XSSFRow rowH = spreadsheet.createRow((short) RowCount);
		XSSFCell cellH = (XSSFCell) rowH.createCell((short) 0);

		cellH.setCellValue("R229 - PJP KPIs");

		spreadsheet.addMergedRegion(new CellRangeAddress(FirstRowCount, // first row (0-based)
				FirstRowCount, // last row (0-based)
				0, // first column (0-based)
				2 // last column (0-based)
		));

		CellStyle style = workbook.createCellStyle();// Create style
		XSSFFont font = workbook.createFont();// Create font
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);// set it to bold
		cellH.setCellStyle(style);

		RowCount = RowCount + 1;
		FirstRowCount = FirstRowCount + 1;
		// Printing Date

		// Report Heading

		XSSFRow rowP = spreadsheet.createRow((short) RowCount);

		XSSFCell cellP = (XSSFCell) rowP.createCell((short) 0);

		cellP.setCellValue("Period: " + Utilities.getDisplayDateFormat(StartDate) + " - "
				+ Utilities.getDisplayDateFormat(EndDate));

		spreadsheet.addMergedRegion(new CellRangeAddress(FirstRowCount, // first row (0-based)
				FirstRowCount, // last row (0-based)
				0, // first column (0-based)
				2 // last column (0-based)
		));

		cellP = (XSSFCell) rowP.createCell((short) 3);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 4);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 5);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 6);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 7);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 8);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 9);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 10);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 11);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 12);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 13);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 14);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 15);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 16);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 17);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 18);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 19);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 20);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 21);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 22);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 23);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 24);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 25);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 26);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 27);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 28);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 29);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 30);
		cellP.setCellValue("");

		cellP = (XSSFCell) rowP.createCell((short) 31);
		cellP.setCellValue("");

		XSSFCellStyle style611 = workbook.createCellStyle();
		style611.setAlignment(CellStyle.ALIGN_RIGHT);
		cellP.setCellStyle(style611);

		String WhereSND = " and distributor_id in (select distributor_id from distributor_beat_plan where asm_id="
				+ ASM_ID + ") limit 1 ";
		if (ASM_ID == 0) {
			WhereSND = "";
		}

		ResultSet rs23 = s10
				.executeQuery("select distinct mo.distributor_id from mobile_order mo where mo.created_on between "
						+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) + " "
						+ WhereSND);

		while (rs23.next()) {

			DistributorID1 = rs23.getLong(1);

			long RegionID = 0;
			String RegionName = "";
			long SNDID = 0;
			String SNDName = "";
			String DistributorName = "";

			ResultSet rs1 = s.executeQuery(
					"SELECT name,region_id,(select region_short_name from common_regions cr where cr.region_id=cd.region_id) region_name,snd_id,(select short_name from common_sd_groups csg where csg.snd_id=cd.snd_id) snd_name FROM common_distributors cd where distributor_id="
							+ DistributorID1);
			while (rs1.next()) {
				RegionID = rs1.getLong("region_id");
				RegionName = rs1.getString("region_name");
				SNDID = rs1.getLong("snd_id");
				SNDName = rs1.getString("snd_name");
				DistributorName = rs1.getString("name");
			}

			double TotalUniqueSKUSold = 0;
			ResultSet rs122 = s3.executeQuery(
					"select count(distinct mop.product_id) total_lines_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mo.created_on between "
							+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
							+ " and beat_plan_id in (select id from distributor_beat_plan where asm_id=" + ASM_ID
							+ ") ");
			if (rs122.first()) {
				TotalUniqueSKUSold = rs122.getDouble("total_lines_sold");
			}

			double TotalAvgPlannedCalls = 0;
			ResultSet rs311 = s3.executeQuery(
					"select avg(ct) avg_daily_sch_call from (select day_number, count(outlet_id) ct from distributor_beat_plan_schedule where id in (select id from distributor_beat_plan where asm_id="
							+ ASM_ID + ") group by day_number) tab1");
			while (rs311.next()) {
				TotalAvgPlannedCalls = rs311.getInt("avg_daily_sch_call");
			}

			// System.out.println("Row Count "+RowCount);
			// System.out.println("First Row Count "+FirstRowCount);
			// System.out.println("Distributor ID "+DistributorID1);

			// Extra Row

			RowCount = RowCount + 1;
			FirstRowCount = FirstRowCount + 1;
			// Printing Date

			// Report Heading

			XSSFRow rowEx1 = spreadsheet.createRow((short) RowCount);

			XSSFCell cellEx1 = (XSSFCell) rowEx1.createCell((short) 0);

			cellEx1 = (XSSFCell) rowEx1.createCell((short) 0);
			cellEx1.setCellValue("");

			cellEx1 = (XSSFCell) rowEx1.createCell((short) 1);
			cellEx1.setCellValue("");

			/////// //////////////////////

			// 2nd row header

			RowCount = RowCount + 1;

			XSSFRow headerrow = spreadsheet.createRow((short) RowCount);
			XSSFCell headercell = (XSSFCell) headerrow.createCell((short) 0);

			headercell.setCellValue(DistributorID1 + " - " + DistributorName + " | " + SNDName + " | " + RegionName);
			Set2ndHeaderBackColorGray(workbook, headercell);
			spreadsheet.addMergedRegion(new CellRangeAddress(FirstRowCount + 1, // first row (0-based)
					FirstRowCount + 1, // last row (0-based)
					0, // first column (0-based)
					7 // last column (0-based)
			));

			headercell = (XSSFCell) headerrow.createCell((short) 8);
			headercell.setCellValue("ORDER KPI");
			Set2ndHeaderBackColorOrderKPI(workbook, headercell);
			spreadsheet.addMergedRegion(new CellRangeAddress(FirstRowCount + 1, // first row (0-based)
					FirstRowCount + 1, // last row (0-based)
					8, // first column (0-based)
					16 // last column (0-based)
			));

			headercell = (XSSFCell) headerrow.createCell((short) 17);
			headercell.setCellValue("DELIVERY KPI");
			Set2ndHeaderBackColorDeliveryKPI(workbook, headercell);
			spreadsheet.addMergedRegion(new CellRangeAddress(FirstRowCount + 1, // first row (0-based)
					FirstRowCount + 1, // last row (0-based)
					17, // first column (0-based)
					28 // last column (0-based)
			));

			headercell = (XSSFCell) headerrow.createCell((short) 29);
			headercell.setCellValue("OTHER SALES");
			Set2ndHeaderBackColorOtherKPI(workbook, headercell);
			spreadsheet.addMergedRegion(new CellRangeAddress(FirstRowCount + 1, // first row (0-based)
					FirstRowCount + 1, // last row (0-based)
					29, // first column (0-based)
					30 // last column (0-based)
			));

			headercell = (XSSFCell) headerrow.createCell((short) 31);
			headercell.setCellValue("");
			Set2ndHeaderBackColorOtherKPI(workbook, headercell);
			spreadsheet.addMergedRegion(new CellRangeAddress(FirstRowCount + 1, // first row (0-based)
					FirstRowCount + 1, // last row (0-based)
					31, // first column (0-based)
					33 // last column (0-based)
			));
			// 3rd Row Header //////////////////////
			///////////////////////////////////////

			RowCount = RowCount + 1;

			XSSFRow headerrow1 = spreadsheet.createRow((short) RowCount);
			XSSFCell headercell1 = (XSSFCell) headerrow1.createCell((short) 0);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 0);
			headercell1.setCellValue("PJP ID");
			Set3rdHeaderBackColor(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 1);
			headercell1.setCellValue("PJP Name");
			Set3rdHeaderBackColor(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 2);
			headercell1.setCellValue("PSR");
			Set3rdHeaderBackColor(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 3);
			headercell1.setCellValue("TOTAL OUTLETS");
			Set3rdHeaderBackColor(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 4);
			headercell1.setCellValue("AVG. DAILY SCHED. CALLS");
			Set3rdHeaderBackColor(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 5);
			headercell1.setCellValue("SCHEDULED CALLS");
			Set3rdHeaderBackColor(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 6);
			headercell1.setCellValue("ACTUAL CALLS");
			Set3rdHeaderBackColor(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 7);
			headercell1.setCellValue("CALL COMPLETION %");
			Set3rdHeaderBackColor(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 8);
			headercell1.setCellValue("UNPLAN. CALLS/ ORDERS");
			Set3rdHeaderBackColorOrder(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 9);
			headercell1.setCellValue("UNPLANNED CALLS %");
			Set3rdHeaderBackColorOrder(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 10);
			headercell1.setCellValue("SCHED. ORDERS");
			Set3rdHeaderBackColorOrder(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 11);
			headercell1.setCellValue("TOTAL ORDERS");
			Set3rdHeaderBackColorOrder(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 12);
			headercell1.setCellValue("STRIKE RATE");
			Set3rdHeaderBackColorOrder(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 13);
			headercell1.setCellValue("ORDERED QTY (Converted)");
			Set3rdHeaderBackColorOrder(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 14);
			headercell1.setCellValue("DROP SIZE");
			Set3rdHeaderBackColorOrder(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 15);
			headercell1.setCellValue("TOTAL SKUs");
			Set3rdHeaderBackColorOrder(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 16);
			headercell1.setCellValue("AVG. SKU / ORDER");
			Set3rdHeaderBackColorOrder(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 17);
			headercell1.setCellValue("ACTUAL SALE (Converted)");
			Set3rdHeaderBackColorDelivery(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 18);
			headercell1.setCellValue("AVG. DAILY TARGET (R/C)");
			Set3rdHeaderBackColorDelivery(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 19);
			headercell1.setCellValue("AVG. DAILY ACTUAL SALE (R/C)");
			Set3rdHeaderBackColorDelivery(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 20);
			headercell1.setCellValue("BOM AVG. DAILY SALE REQ. (R/C)");
			Set3rdHeaderBackColorDelivery(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 21);
			headercell1.setCellValue("PRIOR DAY ORDERED QTY (Converted)");
			Set3rdHeaderBackColorDelivery(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 22);
			headercell1.setCellValue("DELIVERY VS. ORDER %");
			Set3rdHeaderBackColorDelivery(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 23);
			headercell1.setCellValue("LOAD OUT (Converted)");
			Set3rdHeaderBackColorDelivery(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 24);
			headercell1.setCellValue("FULL IN (Converted)");
			Set3rdHeaderBackColorDelivery(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 25);
			headercell1.setCellValue("FULL IN %");
			Set3rdHeaderBackColorDelivery(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 26);
			headercell1.setCellValue("DROP SIZE");
			Set3rdHeaderBackColorDelivery(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 27);
			headercell1.setCellValue("TOTAL SKUs");
			Set3rdHeaderBackColorDelivery(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 28);
			headercell1.setCellValue("AVG. SKU / INVOICE");
			Set3rdHeaderBackColorDelivery(workbook, headercell1);
			/*
			 * headercell1 = (XSSFCell) headerrow1.createCell((short) 29);
			 * headercell1.setCellValue("NO SALE OUTLETS");
			 * Set3rdHeaderBackColorDelivery(workbook,headercell1);
			 */
			// headercell1 = (XSSFCell) headerrow1.createCell((short) 30);
			headercell1 = (XSSFCell) headerrow1.createCell((short) 29);
			headercell1.setCellValue("DESK SALE (Converted)");
			Set3rdHeaderBackColor(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 30);
			headercell1.setCellValue("SPOT SALE (Converted)");
			Set3rdHeaderBackColor(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 31);
			headercell1.setCellValue("DFS (M/Outlet)");
			Set3rdHeaderBackColor(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 32);
			headercell1.setCellValue("TIS (M)");
			Set3rdHeaderBackColor(workbook, headercell1);

			headercell1 = (XSSFCell) headerrow1.createCell((short) 33);
			headercell1.setCellValue("TIM (H)");
			Set3rdHeaderBackColor(workbook, headercell1);
			
			headercell1 = (XSSFCell) headerrow1.createCell((short) 34);
			headercell1.setCellValue("Uptime%");
			Set3rdHeaderBackColor(workbook, headercell1);

			/* Actual Row Data *//////
			//////////////////////////////////////////////////

			long PJPID = 0;
			String PJPName = "";

			int GrandTotalOutlets = 0;
			int GrandAvgDailySchCallTotal = 0;
			double GrandTotalScheduledCalls = 0;
			double GrandTotalOrdersFromPlannedOutlets = 0;
			double GrandOrderProductivity = 0;
			double GrandTotalUnplannedCalls = 0;
			double GrandTotalOrders = 0;
			double GrandTotalTotalQuantitySold = 0;
			double GrandDropSize = 0;
			double GrandTotalTotalLinesSold = 0;
			double GrandSKUPerOrder = 0;
			double GrandD_TotalTotalQuantitySold = 0;
			double GrandSalesPerDay = 0;
			double GrandD_TotalQuantityDispatched = 0;
			double GrandD_Returns = 0;
			double GrandD_DropSize = 0;
			double GrandD_TotalTotalLinesSold = 0;
			double GrandSKUPerInvoice = 0;
			double GrandZeroSalesCount = 0;
			double GrandDD_TotalTotalQuantitySold = 0;
			double GrandTotalOrderCount = 0;
			double GrandD_TotalInvoiceCount = 0;
			double TotalD_UniqueSKUSold = 0;
			double GrandTotalWholeSale = 0;

			double GrandCallCompRate = 0;

			double GrandTotalSchCalls = 0;

			double GrandTotalAvgDailyTarget = 0;
			double GrandTotalAvgDailySale = 0;

			double GrandTotalDeskSalesRawCases = 0;
			double GrandTotalSpotSalesRawCases = 0;

			String AltRowColorGray = "#f7f5f5;";

			String AltRowColorBlue = "#f6fdfd;";

			String AltRowColorPink = "#f8f6f9";

			s.executeUpdate("SET SESSION group_concat_max_len = 1000000");

			ResultSet rs2 = s.executeQuery(
					"SELECT dbp.id, dbp.label, (select count(distinct outlet_id) from distributor_beat_plan_schedule where id = dbp.id) outlet_count, (select assigned_to from distributor_beat_plan_users where id = dbp.id) assigned_to, (select group_concat(distinct outlet_id) from distributor_beat_plan_schedule where id = dbp.id) total_outlet_ids FROM pep.distributor_beat_plan dbp where dbp.asm_id = "
							+ ASM_ID + " having outlet_count != 0 order by assigned_to desc");
			while (rs2.next()) {

				int AltRowColorCount = 1;

				long AssignedTo = rs2.getLong("assigned_to");

				PJPID = rs2.getLong("id");
				PJPName = rs2.getString("label");
				int TotalOutlets = rs2.getInt(3);

				GrandTotalOutlets += TotalOutlets;

				int AvgDailySchCall = 0;
				double ScheduledCalls = 0;// rs2.getInt("scheduled_calls");
				long OrderBookerID = rs2.getInt("assigned_to");
				String TotalOutletIDs = rs2.getString("total_outlet_ids");
				// System.out.println(TotalOutletIDs);
				String OrderBookerName = "";

				ResultSet rs29 = s3.executeQuery("SELECT display_name FROM pep.users where id=" + OrderBookerID);
				if (rs29.first()) {
					OrderBookerName = rs29.getString(1);
				}

				String OrderBookerN = "";

				if (OrderBookerID != 0) {
					OrderBookerN = OrderBookerID + " - " + OrderBookerName;
				}

				double UniqueSKUSold = 0;
				ResultSet rs121 = s3.executeQuery(
						"select count(distinct mop.product_id) total_lines_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mo.created_on between "
								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
								+ " and mo.beat_plan_id=" + PJPID);
				if (rs121.first()) {
					UniqueSKUSold = rs121.getDouble("total_lines_sold");
				}

				// select avg(ct) from (select day_number, count(outlet_id) ct from
				// distributor_beat_plan_schedule where id = 55 group by day_number) tab1
				ResultSet rs3 = s2.executeQuery(
						"select avg(ct) avg_daily_sch_call from (select day_number, count(outlet_id) ct from distributor_beat_plan_schedule where id = "
								+ PJPID + " group by day_number) tab1");
				while (rs3.next()) {
					AvgDailySchCall = rs3.getInt("avg_daily_sch_call");
				}

				GrandAvgDailySchCallTotal += AvgDailySchCall;

				ResultSet rs4 = s2.executeQuery("SELECT display_name FROM users where id=" + OrderBookerID);
				while (rs4.next()) {
					OrderBookerName = rs4.getString("display_name");
				}

				double TotalOrdersFromPlannedOutlets = 0;
				double TotalUnplannedCalls = 0;
				double TotalTotalQuantitySold = 0;
				double TotalOrderCount = 0;
				double TotalTotalLinesSold = 0;
				double D_TotalTotalQuantitySold = 0;
				double D_TotalQuantityDispatched = 0;
				double D_TotalInvoiceCount = 0;
				double D_TotalTotalLinesSold = 0;
				double DD_TotalTotalQuantitySold = 0;
				double D_TotalWholesaleQuantitySold = 0;

				double D_TotalTargetRawCases = 0;

				double TotalActuallSchCalls = 0;

				Date CurrentDate = StartDate;
				int i = 0;
				String DaysOfWeek = "0";
				double days = 0;
				int IsSundayOff = Utilities.IsDistributorOFFStatus(DistributorID1);

				int DayNumberOff = 0;

				if (IsSundayOff == 0) { // Mean Friday Off - Normal Case
					DayNumberOff = 6;
				} else {
					DayNumberOff = 1; // Else Sunday Off
				}
				while (true) {

//							if(Utilities.getDayOfWeekByDate(CurrentDate)!=6 && !IsGazettedHoliday(CurrentDate,DistributorID1)){ //if it is not friday
//
//								DaysOfWeek += ","+Utilities.getDayOfWeekByDate(CurrentDate);

					//////// System.out.println("Current Date : "+CurrentDate);

					if (Utilities.getDayOfWeekByDate(CurrentDate) != DayNumberOff
							&& !IsGazettedHoliday(CurrentDate, DistributorID1)) { // if it is not friday
						DaysOfWeek += "," + Utilities.getDayOfWeekByDate(CurrentDate);
						days++;
						double PlannedOutletsCount = 0;
						String PlannedOutletIDs = "";

						ResultSet rs5 = s3.executeQuery(
								"select count(outlet_id), group_concat(outlet_id) from distributor_beat_plan_log where log_date between "
										+ Utilities.getSQLDate(CurrentDate) + " and "
										+ Utilities.getSQLDate(CurrentDate) + " and distributor_id = " + DistributorID1
										+ " and day_number = dayofweek(log_date) and id = " + PJPID);
						if (rs5.first()) {
							PlannedOutletsCount = rs5.getDouble(1);
							PlannedOutletIDs = rs5.getString(2);
						}

						ScheduledCalls += PlannedOutletsCount;
//								if (DistributorID1 == 100941){
//									System.out.println(PJPID +"\t"+CurrentDate+"\t"+PlannedOutletsCount);
//								}

						//

						String OrderIDs = "";
						String OutletIDsOrdered = "";
						int OrderCount = 0;
						int UniqueOutletsOrdered = 0;
						ResultSet rs45 = s3.executeQuery(
								"select group_concat(mo.id) order_ids, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, group_concat(distinct mo.outlet_id) outlet_ids_ordered from mobile_order mo where mo.created_on between "
										+ Utilities.getSQLDate(CurrentDate) + " and "
										+ Utilities.getSQLDateNext(CurrentDate) + " and mo.beat_plan_id =" + PJPID);
						if (rs45.first()) {
							OrderIDs = rs45.getString("order_ids");
							OutletIDsOrdered = rs45.getString("outlet_ids_ordered");
							OrderCount = rs45.getInt("order_count");
							UniqueOutletsOrdered = rs45.getInt("unique_outlets_ordered");
						}
						TotalOrderCount += OrderCount;

						double OrdersFromPlannedOutlets = 0;
						ResultSet rs10 = s3.executeQuery("select count(id) from common_outlets where id in ("
								+ PlannedOutletIDs + ") and id in (" + OutletIDsOrdered + ")");
						if (rs10.first()) {
							OrdersFromPlannedOutlets = rs10.getDouble(1);
						}
						TotalOrdersFromPlannedOutlets += OrdersFromPlannedOutlets;

						//////////////////////////////////////////////////////
						/////////// *************************************/////////////
						// Actual Scheduled Calls
						/////////////////////////////////////////////////////////////

						double OrdersFromPlannedOutletsZero = 0;
						ResultSet rs101 = s3.executeQuery("select count(id) from common_outlets where id in ("
								+ PlannedOutletIDs
								+ ") and id in (SELECT distinct outlet_id FROM mobile_order_zero where created_on between "
								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
								+ " and created_by=" + OrderBookerID
								+ " union select distinct id from common_outlets where id in (" + PlannedOutletIDs
								+ ") and id in (" + OutletIDsOrdered + "))");
						if (rs101.first()) {
							OrdersFromPlannedOutletsZero = rs101.getDouble(1);
						}

						TotalActuallSchCalls += OrdersFromPlannedOutletsZero;

						/////////////////////////////////////////////////////////////////////////////
						///////////////// ********************************///////////////////////////

						double UnplannedOutletCount = UniqueOutletsOrdered - OrdersFromPlannedOutlets;
						TotalUnplannedCalls += UnplannedOutletCount;

						double TotalQuantitySold = 0;
						ResultSet rs9 = s3.executeQuery(
								"select sum(mop.total_units*ipv.liquid_in_ml)/250 total_qty_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mo.id in ("
										+ OrderIDs + ")");
						if (rs9.first()) {
							TotalQuantitySold = rs9.getDouble("total_qty_sold");
						}

						TotalTotalQuantitySold += TotalQuantitySold;

						double TotalLinesSold = 0;
						ResultSet rs12 = s3.executeQuery(
								"select count(*) total_lines_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mo.id in ("
										+ OrderIDs + ")");
						if (rs12.first()) {
							TotalLinesSold = rs12.getDouble("total_lines_sold");
						}
						TotalTotalLinesSold += TotalLinesSold;
					}

					if (DateUtils.isSameDay(CurrentDate, EndDate)) {
						break;
					}

					CurrentDate = Utilities.getDateByDays(CurrentDate, 1);
					i++;

				}

				double ZeroSalesCount = 0;

				if (PJPID == 28) {
					// System.out.println("select count(distinct outlet_id) from
					// distributor_beat_plan_all_view where distributor_id = "+DistributorID1+" and
					// day_number in ("+DaysOfWeek+") and id = "+PJPID+" and outlet_id not in
					// (select distinct outlet_id from mobile_order where distributor_id =
					// "+DistributorID1+" and created_on between "+Utilities.getSQLDate(StartDate)+"
					// and "+Utilities.getSQLDateNext(EndDate)+" and beat_plan_id = "+PJPID+")");
				}
				ResultSet rs50 = s2.executeQuery(
						"select count(distinct outlet_id) from distributor_beat_plan_log where log_date between "
								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)
								+ " and distributor_id = " + DistributorID1 + " and day_number in (" + DaysOfWeek
								+ ") and id = " + PJPID
								+ " and outlet_id not in (select distinct outlet_id from mobile_order where created_on between "
								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) + ")");
				if (rs50.first()) {
					ZeroSalesCount = rs50.getDouble(1);
				}
				GrandZeroSalesCount += ZeroSalesCount;

				// Delivery KPI Loop
				///////////////////////////////////////////////////////////////

				Date StartDate1 = StartDate;
				Date EndDate1 = EndDate;

				if (DateUtils.isSameDay(StartDate, EndDate)) { // if both daes are same then
					StartDate1 = Utilities.getDateByDays(StartDate, -1);
					EndDate1 = Utilities.getDateByDays(EndDate, -1);

					if (Utilities.getDayOfWeekByDate(StartDate1) == 6) { // if it is friday
						StartDate1 = Utilities.getDateByDays(StartDate1, -1);
						EndDate1 = Utilities.getDateByDays(EndDate1, -1);
					}

					// System.out.println(Utilities.getDayOfWeekByDate(StartDate)+" -
					// "+Utilities.getDisplayDateFormat(StartDate));

				} else {
					EndDate1 = Utilities.getDateByDays(EndDate, -1);

					// if(Utilities.getDayOfWeekByDate(StartDate1)==6){ //if it is friday
					// StartDate1 = Utilities.getDateByDays(StartDate1,-1);
					// }

					if (Utilities.getDayOfWeekByDate(EndDate1) == 6) { // if it is friday
						EndDate1 = Utilities.getDateByDays(EndDate1, -1);
					}
				}

				///////// System.out.println("New Start Date after Friday :"+StartDate1);
				///////// System.out.println("End Start Date after Friday :"+EndDate1);

				double D_UniqueSKUSold = 0;
				ResultSet D_rs51 = s3.executeQuery(
						"select count(distinct isap.product_id) total_lines_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.order_created_on_date between "
								+ Utilities.getSQLDate(StartDate1) + " and " + Utilities.getSQLDate(EndDate1)
								+ " and isa.beat_plan_id=" + PJPID);
				if (D_rs51.first()) {
					D_UniqueSKUSold = D_rs51.getDouble("total_lines_sold");
				}

				ResultSet D_rs511 = s3.executeQuery(
						"select count(distinct isap.product_id) total_lines_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.order_created_on_date between "
								+ Utilities.getSQLDate(StartDate1) + " and " + Utilities.getSQLDate(EndDate1)
								+ " and isa.beat_plan_id in (select id from distributor_beat_plan where asm_id="
								+ ASM_ID + ")");
				if (D_rs511.first()) {
					TotalD_UniqueSKUSold = D_rs511.getDouble("total_lines_sold");
				}

				Date CurrentDate1 = StartDate1;
				int i1 = 1;

				double DailyTargets = 0;

				double days1 = 0;
				while (true) {
					if (Utilities.getDayOfWeekByDate(CurrentDate1) != 6) { // if it is not friday
						///////////// System.out.println("Current Date1 : "+CurrentDate1);

						// System.out.println("brrr "+DailyTargets);

						days1++;
						double PlannedOutletsCount = 0;
						String PlannedOutletIDs = "";

						ResultSet rs5 = s3.executeQuery(
								"select count(distinct outlet_id), group_concat(distinct outlet_id) from distributor_beat_plan_log where log_date between "
										+ Utilities.getSQLDate(CurrentDate1) + " and "
										+ Utilities.getSQLDate(CurrentDate1) + " and distributor_id = " + DistributorID1
										+ " and day_number = dayofweek(log_date) and id = " + PJPID);
						if (rs5.first()) {
							PlannedOutletsCount = rs5.getDouble(1);
							PlannedOutletIDs = rs5.getString(2);
						}

						//

						String OrderIDs = "";
						String OutletIDsOrdered = "";
						int OrderCount = 0;
						int UniqueOutletsOrdered = 0;
						ResultSet rs45 = s3.executeQuery(
								"select group_concat(mo.id) order_ids, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, group_concat(distinct mo.outlet_id) outlet_ids_ordered from mobile_order mo where mo.created_on between "
										+ Utilities.getSQLDate(CurrentDate1) + " and "
										+ Utilities.getSQLDateNext(CurrentDate1) + " and mo.beat_plan_id =" + PJPID);
						if (rs45.first()) {
							OrderIDs = rs45.getString("order_ids");

						}

						double DD_TotalQuantitySold = 0;
						ResultSet rs9 = s3.executeQuery(
								"select sum(mop.total_units*ipv.liquid_in_ml)/250 total_qty_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mo.id in ("
										+ OrderIDs + ")");
						if (rs9.first()) {
							DD_TotalQuantitySold = rs9.getDouble("total_qty_sold");
						}

						DD_TotalTotalQuantitySold += DD_TotalQuantitySold;

						/*
						 * DELIVERY KPIs
						 */

						double D_TotalQuantitySold = 0;
						ResultSet D_rs9 = s3.executeQuery(
								"select sum(isap.total_units*ipv.liquid_in_ml)/250 total_qty_sold from inventory_sales_adjusted_products isap force index (inventory_sales_adjusted_products_cache_order_id) join inventory_products_view ipv on isap.product_id=ipv.product_id  where isap.cache_order_id in ("
										+ OrderIDs + ")");
						if (D_rs9.first()) {
							D_TotalQuantitySold = D_rs9.getDouble("total_qty_sold");
						}
						D_TotalTotalQuantitySold += D_TotalQuantitySold;

						double D_WholesaleQuantitySold = 0;
//								ResultSet WD_rs9 = s3.executeQuery("select sum(isap.total_units/isap.cache_units_per_sku) total_qty_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.order_id in ("+OrderIDs+") and isa.outlet_id in (select id from common_outlets where channel_id in (12,13) and cache_distributor_id = "+DistributorID1+" )");
//								if(WD_rs9.first()){
//									D_WholesaleQuantitySold = WD_rs9.getDouble("total_qty_sold");
//								}
						D_TotalWholesaleQuantitySold += D_WholesaleQuantitySold;
						GrandTotalWholeSale += D_WholesaleQuantitySold;

						double D_QuantityDispatched = 0;
						ResultSet D_rs1 = s3.executeQuery(
								"select sum(isip.total_units*ipv.liquid_in_ml)/250 from inventory_sales_invoices isi join inventory_sales_invoices_products isip on isi.id = isip.id join inventory_products_view ipv on isip.product_id = ipv.product_id where isi.order_id in ("
										+ OrderIDs + ")");
						if (D_rs1.first()) {
							D_QuantityDispatched = D_rs1.getDouble(1);
						}
						D_TotalQuantityDispatched += D_QuantityDispatched;

						double InvoiceCount = 0;

						ResultSet D_rs4 = s3.executeQuery(
								"select count(distinct id) from inventory_sales_adjusted_products force index (inventory_sales_adjusted_products_cache_order_id) where cache_order_id in ("
										+ OrderIDs + ") /* and outlet_id in (" + PlannedOutletIDs
										+ ") */ and net_amount != 0");
						if (D_rs4.first()) {
							InvoiceCount = D_rs4.getDouble(1);
						}
						D_TotalInvoiceCount += InvoiceCount;

						double D_TotalLinesSold = 0;
						ResultSet D_rs5 = s3.executeQuery(
								"select count(*) total_lines_sold from inventory_sales_adjusted_products isap force index (inventory_sales_adjusted_products_cache_order_id) where isap.cache_order_id in ("
										+ OrderIDs + ")");
						if (D_rs5.first()) {
							D_TotalLinesSold = D_rs5.getDouble("total_lines_sold");
						}
						D_TotalTotalLinesSold += D_TotalLinesSold;

						double TargetRawCases = 0;
						ResultSet D_rs19 = s3.executeQuery(
								"SELECT sum(quantity) FROM pep.employee_targets et join employee_targets_packages etp on et.id = etp.id where month = month("
										+ Utilities.getSQLDate(CurrentDate1) + ") and year = year("
										+ Utilities.getSQLDate(CurrentDate1) + ") and employee_id = " + OrderBookerID);
						if (D_rs19.first()) {
							double WorkingDays = Utilities.getDayNumberByDate(Utilities.getEndDateByDate(CurrentDate1))
									- Utilities.getFridayCountByDate(CurrentDate1);

							TargetRawCases = D_rs19.getDouble(1) / WorkingDays;
						}
						D_TotalTargetRawCases += TargetRawCases;

						if (DateUtils.isSameDay(CurrentDate1, EndDate1)) {
							break;
						}

					}

					CurrentDate1 = Utilities.getDateByDays(CurrentDate1, 1);
					i1++;

				}

				// System.out.println(AverageDailyTargets+" - "+DailyTargets+" - "+i1);

				// BOM Target
				double TotalMonthTargetRawCases = 0;
				// System.out.println("SELECT sum(quantity) FROM pep.distributor_targets dt join
				// distributor_targets_packages dtp on dt.id = dtp.id where month =
				// month("+Utilities.getSQLDate(EndDate1)+") and year =
				// year("+Utilities.getSQLDate(EndDate1)+") and distributor_id =
				// "+rDistributorID);
				ResultSet D_rs191 = s3.executeQuery(
						"SELECT sum(quantity) FROM employee_targets et join employee_targets_packages etp on et.id = etp.id where month = month("
								+ Utilities.getSQLDate(EndDate1) + ") and year = year(" + Utilities.getSQLDate(EndDate1)
								+ ") and employee_id = " + OrderBookerID);
				if (D_rs191.first()) {
					TotalMonthTargetRawCases = D_rs191.getDouble(1);
				}

				double TotalMonthSalesRawCases = 0;
				// System.out.println("select sum(total_units/cache_units_per_sku) from
				// inventory_sales_adjusted_products where cache_created_on_date between
				// "+Utilities.getSQLDate(Utilities.getStartDateByDate(EndDate1))+" and
				// "+Utilities.getSQLDate(Utilities.getEndDateByDate(EndDate1))+" and
				// cache_distributor_id = "+rDistributorID);
				ResultSet D_rs192 = s3.executeQuery(
						"select sum(total_units/cache_units_per_sku) from inventory_sales_adjusted_products force index (inventory_sales_adjusted_products_created_on_booked_by) where cache_order_created_on_date between "
								+ Utilities.getSQLDate(Utilities.getStartDateByDate(EndDate1)) + " and "
								+ Utilities.getSQLDate(EndDate1) + " and cache_booked_by = " + OrderBookerID + "");
				if (D_rs192.first()) {
					TotalMonthSalesRawCases = D_rs192.getDouble(1);
				}

				double TotalDeskSalesRawCases = 0;
				ResultSet D_rs193 = s3.executeQuery(
						"SELECT sum(total_units*ipv.liquid_in_ml)/250 FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id=ipv.product_id where isa.type_id = 1 and isa.created_on_date between "
								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)
								+ " and isa.beat_plan_id = " + PJPID + "");
				if (D_rs193.first()) {
					TotalDeskSalesRawCases = D_rs193.getDouble(1);
				}
				GrandTotalDeskSalesRawCases += TotalDeskSalesRawCases;

				double TotalSpotSalesRawCases = 0;
				ResultSet D_rs194 = s3.executeQuery(
						"SELECT sum(total_units*ipv.liquid_in_ml)/250 FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id=ipv.product_id where isa.type_id = 2 and isa.created_on_date between "
								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)
								+ " and isa.beat_plan_id = " + PJPID + "");
				if (D_rs194.first()) {
					TotalSpotSalesRawCases = D_rs194.getDouble(1);
				}
				GrandTotalSpotSalesRawCases += TotalSpotSalesRawCases;

				int DaysInMonth = Utilities.getDayNumberByDate(Utilities.getEndDateByDate(EndDate1));
				int CurrentDay = Utilities.getDayNumberByDate(EndDate1);
				int RemainingDays = (DaysInMonth - CurrentDay) - Utilities.getSubsequentFridayCountByDate(EndDate1);

				double RemainingTarget = TotalMonthTargetRawCases - TotalMonthSalesRawCases;

				// System.out.println("RT:"+RemainingTarget+" MT:"+TotalMonthTargetRawCases+"
				// MS:"+TotalMonthSalesRawCases);

				double BOMDailyTarget = 0;

				if (RemainingDays != 0 && RemainingTarget > 0) {
					BOMDailyTarget = RemainingTarget / RemainingDays;
				}

				/////////////////////////////////////////////////////
				/////////////////////////////////////////////////////

				double OrderProductivity = 0;
				if (ScheduledCalls != 0) {
					OrderProductivity = (TotalOrdersFromPlannedOutlets / ScheduledCalls);
				}

				double CallCompRate = 0;
				if (ScheduledCalls != 0) {
					CallCompRate = (TotalActuallSchCalls / ScheduledCalls);
				}

				double AverageDailyTarget = 0;
				if (days1 != 0) {
					AverageDailyTarget = D_TotalTargetRawCases / days1;
				}

				double UnplannedCallsPercentage = 0;
				if (ScheduledCalls != 0) {
					UnplannedCallsPercentage = (TotalUnplannedCalls / ScheduledCalls);
				}

				double TotalOrders = TotalOrdersFromPlannedOutlets + TotalUnplannedCalls;

				GrandTotalOrders += TotalOrders;

				double DropSize = 0;
				if (TotalOrderCount != 0) {
					DropSize = TotalTotalQuantitySold / TotalOrderCount;
				}

				GrandTotalOrderCount += TotalOrderCount;

				GrandDropSize += DropSize;

				double D_DropSize = 0;
				if (D_TotalInvoiceCount != 0) {
					D_DropSize = D_TotalTotalQuantitySold / D_TotalInvoiceCount;
				}

				GrandD_DropSize += D_DropSize;

				if (PJPID == 55) {
					// System.out.println(TotalTotalQuantitySold+" "+D_TotalTotalQuantitySold);
					// System.out.println(TotalOrderCount+" "+D_TotalInvoiceCount);
				}

				double SKUPerOrder = 0;
				if (TotalOrderCount != 0) {
					SKUPerOrder = TotalTotalLinesSold / TotalOrderCount;
				}

				GrandSKUPerOrder += SKUPerOrder;

				double SKUPerInvoice = 0;
				if (D_TotalInvoiceCount != 0) {
					SKUPerInvoice = D_TotalTotalLinesSold / D_TotalInvoiceCount;
				}

				GrandSKUPerInvoice += SKUPerInvoice;

				GrandD_TotalInvoiceCount += D_TotalInvoiceCount;

				double SalesPerDay = 0;
				if (days1 != 0) {
					SalesPerDay = D_TotalTotalQuantitySold / days1;
				}

				GrandSalesPerDay += SalesPerDay;

				double DeliveryOrderPercentage = 0;
				if (DD_TotalTotalQuantitySold != 0) {
					DeliveryOrderPercentage = (D_TotalTotalQuantitySold / DD_TotalTotalQuantitySold);
				}

				double D_Returns = D_TotalQuantityDispatched - D_TotalTotalQuantitySold;

				GrandD_Returns += D_Returns;

				double D_ReturnsPercentage = 0;
				if (D_TotalQuantityDispatched != 0) {
					D_ReturnsPercentage = (D_Returns / D_TotalQuantityDispatched);
				}

				GrandTotalScheduledCalls += ScheduledCalls;
				GrandTotalOrdersFromPlannedOutlets += TotalOrdersFromPlannedOutlets;
				GrandTotalUnplannedCalls += TotalUnplannedCalls;
				GrandTotalTotalQuantitySold += TotalTotalQuantitySold;
				GrandTotalTotalLinesSold += TotalTotalLinesSold;
				GrandD_TotalTotalQuantitySold += D_TotalTotalQuantitySold;
				GrandD_TotalQuantityDispatched += D_TotalQuantityDispatched;
				GrandDD_TotalTotalQuantitySold += DD_TotalTotalQuantitySold;
				GrandD_TotalTotalLinesSold += D_TotalTotalLinesSold;
				GrandTotalSchCalls += TotalActuallSchCalls;

				// Addition Starts
				// Distance from Shop

				String OrderIDs2 = "";

				ResultSet rs456 = s3.executeQuery(
						"select group_concat(mo.id) order_ids from mobile_order mo where mo.created_on between "
								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
								+ " and mo.beat_plan_id =" + PJPID);
				if (rs456.first()) {
					OrderIDs2 = rs456.getString("order_ids");

				}

				double TotalDistance = 0;

				ResultSet rs232 = s2.executeQuery(
						"SELECT 	avg((( 3959 * acos( cos( radians(mo.lat) ) * cos( radians( co.lat ) ) * cos( radians( co.lng ) - radians(mo.lng) ) + sin ( radians(mo.lat) )  * sin( radians( co.lat ) ) ) ) * 1609.34 )) AS distance "
								+ " FROM mobile_order mo join  common_outlets co on  mo.outlet_id=co.id where mo.created_on between "
								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
								+ "  and mo.beat_plan_id=" + PJPID
								+ " and mo.lat!=0 and mo.lng!=0 and co.lat!=0 and co.lng!=0");
				if (rs232.first()) {
					TotalDistance = rs232.getDouble("distance");
				}

				////////////////////////////////////////////////////////////////////////////////////////////
				/////////////////////////////////////////////////////

				// TotalDistance = TotalDistance/1000; //converting into KM

				// Calculating TIM

				double TIM = 0;
				double TotalTIMInt = 0;

				ResultSet rs421 = s2.executeQuery("select avg(timeshop) avgtis from ("
						+ "Select date(created_on),time_to_sec(max(mobile_timestamp))-time_to_sec(min(mobile_timestamp)) timeshop from mobile_order mo where mobile_timestamp between "
						+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
						+ " and mo.created_on between " + Utilities.getSQLDate(StartDate) + " and "
						+ Utilities.getSQLDateNext(EndDate) + " and mo.beat_plan_id=" + PJPID
						+ " group by date(created_on)" + ") tbl");
				if (rs421.first()) {
					TIM = rs421.getDouble("avgtis") / 3600;

				}
				TotalTIMInt = TIM;

				/////////////////////////////////////////////////////
				///////////////////////////////////////////////////////////

				//////////////////////////////////////////////////////////////////////

				double TotalTIS = 0;
				//////////// Calculating TIS///////////////////

				long OutlettID = 0;

				int OutletCounter = 0;

				ResultSet rs24341 = s3.executeQuery("select avg(timesec) timesec2 from ("
						+ "select mot.mobile_order_no, time_to_sec(max(timestamps))-time_to_sec(min(timestamps)) timesec from mobile_order_timestamps mot join mobile_order mo on mot.mobile_order_no=mo.unedited_order_id  where timestamps between "
						+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
						+ " and mo.created_on between " + Utilities.getSQLDate(StartDate) + " and "
						+ Utilities.getSQLDateNext(EndDate) + "  and mo.beat_plan_id=" + PJPID
						+ " group by mot.mobile_order_no" + ") tbl");
				if (rs24341.first()) {
					TotalTIS = rs24341.getDouble("timesec2");
				}

				////////////////////////////////////////////////

				/////////////////////////////////////////////////////
				///////////////////////////////////////////////////////////

				RowCount = RowCount + 1;

				XSSFRow headerrow2 = spreadsheet.createRow((short) RowCount);
				XSSFCell headercell2 = (XSSFCell) headerrow2.createCell((short) 0);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 0);
				headercell2.setCellValue(PJPID);
				SetNormalCellBackColorLeftAltGray(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
				headercell2.setCellValue(Utilities.truncateStringToMax(PJPName, 15));
				// headercell2.getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
				// headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorLeft2(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 2);
				headercell2.setCellValue(Utilities.truncateStringToMax(OrderBookerN, 25));
				// headercell2.getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
				// headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorLeft2(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 3);
				headercell2.setCellValue(TotalOutlets);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorGray(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 4);
				headercell2.setCellValue(AvgDailySchCall);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 5);
				headercell2.setCellValue(ScheduledCalls);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorGray(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 6);
				headercell2.setCellValue(TotalActuallSchCalls);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				// Order

				headercell2 = (XSSFCell) headerrow2.createCell((short) 7);
				headercell2.setCellValue(CallCompRate);

				SetNormalCellBackColorPercentGray(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 8);
				headercell2.setCellValue(TotalUnplannedCalls);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 9);
				headercell2.setCellValue(UnplannedCallsPercentage);

				SetNormalCellBackColorPercentBlue(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 10);
				headercell2.setCellValue(TotalOrdersFromPlannedOutlets);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 11);
				headercell2.setCellValue(TotalOrders);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorBlue(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 12);
				headercell2.setCellValue(OrderProductivity);

				SetNormalCellBackColorPercent(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 13);
				headercell2.setCellValue(TotalTotalQuantitySold);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorBlue(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 14);
				headercell2.setCellValue(DropSize);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 15);
				headercell2.setCellValue(UniqueSKUSold);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorBlue(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 16);
				headercell2.setCellValue(Double.parseDouble(Utilities.getDisplayCurrencyFormatOneDecimal(SKUPerOrder)));

				SetNormalCellBackColorOneDecimal(workbook, headercell2);
				// Delivery

				headercell2 = (XSSFCell) headerrow2.createCell((short) 17);
				headercell2.setCellValue(D_TotalTotalQuantitySold);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorPink(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 18);
				headercell2.setCellValue(AverageDailyTarget);

				SetNormalCellBackColor(workbook, headercell2);

				GrandTotalAvgDailyTarget += AverageDailyTarget;

				headercell2 = (XSSFCell) headerrow2.createCell((short) 19);
				headercell2.setCellValue(SalesPerDay);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorPink(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 20);
				headercell2.setCellValue(BOMDailyTarget);

				SetNormalCellBackColor(workbook, headercell2);

				GrandTotalAvgDailySale += BOMDailyTarget;

				headercell2 = (XSSFCell) headerrow2.createCell((short) 21);
				headercell2.setCellValue(DD_TotalTotalQuantitySold);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorPink(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 22);
				headercell2.setCellValue(DeliveryOrderPercentage);

				SetNormalCellBackColorPercent(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 23);
				headercell2.setCellValue(D_TotalQuantityDispatched);

				SetNormalCellBackColorPink(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 24);
				headercell2.setCellValue(D_Returns);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 25);

				if (D_ReturnsPercentage > 0.05) {
					headercell2.setCellValue(D_ReturnsPercentage);
					SetNormalCellBackColorPercentPink12(workbook, headercell2);
				} else {
					headercell2.setCellValue(D_ReturnsPercentage);
					SetNormalCellBackColorPercentPink(workbook, headercell2);
				}

				headercell2 = (XSSFCell) headerrow2.createCell((short) 26);
				headercell2.setCellValue(D_DropSize);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 27);
				headercell2.setCellValue(D_UniqueSKUSold);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorPink(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 28);
				headercell2
						.setCellValue(Double.parseDouble(Utilities.getDisplayCurrencyFormatOneDecimal(SKUPerInvoice)));
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorOneDecimal(workbook, headercell2);

				/*
				 * headercell2 = (XSSFCell) headerrow2.createCell((short) 29);
				 * headercell2.setCellValue(ZeroSalesCount);
				 * headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				 * SetNormalCellBackColorPink(workbook,headercell2);
				 */

				double TotalWholeSalePercent = 0;
				if ((D_TotalTotalQuantitySold + TotalDeskSalesRawCases + TotalSpotSalesRawCases) != 0) {
					TotalWholeSalePercent = (TotalDeskSalesRawCases
							/ (D_TotalTotalQuantitySold + TotalDeskSalesRawCases + TotalSpotSalesRawCases));
				}

				headercell2 = (XSSFCell) headerrow2.createCell((short) 29);
				headercell2.setCellValue(TotalDeskSalesRawCases);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorPink(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 30);
				headercell2.setCellValue(TotalSpotSalesRawCases);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);
				
				
				double percentageTime = (TotalTIMInt != 0) ? (TotalTIS/TotalTIMInt) * 100 : 0;

				if (isMTD == false) {// these cols only for Today's report

					headercell2 = (XSSFCell) headerrow2.createCell((short) 31);
					headercell2.setCellValue(TotalDistance);
					headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					SetNormalCellBackColor(workbook, headercell2);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 32);
					headercell2.setCellValue(TotalTIS / 60);
					headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					SetNormalCellBackColor(workbook, headercell2);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 33);
					headercell2.setCellValue(TotalTIMInt);
					headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					SetNormalCellBackColor(workbook, headercell2);
					
					
					headercell2 = (XSSFCell) headerrow2.createCell((short) 34);
					headercell2.setCellValue(Math.round(percentageTime));
					headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					SetNormalCellBackColor(workbook, headercell2);
					
					
				} else {

					headercell2 = (XSSFCell) headerrow2.createCell((short) 31);
					headercell2.setCellValue(TotalDistance);
					headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					SetNormalCellBackColor(workbook, headercell2);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 32);
					headercell2.setCellValue(TotalTIS / 60);
					headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					SetNormalCellBackColor(workbook, headercell2);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 33);
					headercell2.setCellValue(TotalTIMInt);
					headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					SetNormalCellBackColor(workbook, headercell2);
					
					headercell2 = (XSSFCell) headerrow2.createCell((short) 34);
					headercell2.setCellValue(Math.round(percentageTime));
					headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					SetNormalCellBackColor(workbook, headercell2);
				}

				// FirstRowCount++;

			}

			// Grand Total

			if (GrandTotalScheduledCalls != 0) {
				GrandOrderProductivity = (GrandTotalOrdersFromPlannedOutlets / GrandTotalScheduledCalls);
			}

			if (GrandTotalScheduledCalls != 0) {
				GrandCallCompRate = (GrandTotalSchCalls / GrandTotalScheduledCalls);
			}

			double GrandUnplannedCallsPercentage = 0;
			if (GrandTotalScheduledCalls != 0) {
				GrandUnplannedCallsPercentage = (GrandTotalUnplannedCalls / GrandTotalScheduledCalls);
			}

			double GrandDeliveryOrderPercentage = 0;
			if (GrandTotalTotalQuantitySold != 0) {
				GrandDeliveryOrderPercentage = (GrandD_TotalTotalQuantitySold / GrandTotalTotalQuantitySold);
			}

			double GrandD_ReturnsPercentage = 0;
			if (GrandD_TotalQuantityDispatched != 0) {
				GrandD_ReturnsPercentage = (GrandD_Returns / GrandD_TotalQuantityDispatched);
			}

			double GrandD_DeliveryOrderPercentage = 0;
			if (GrandD_TotalTotalQuantitySold != 0) {
				GrandD_DeliveryOrderPercentage = (GrandD_TotalTotalQuantitySold / GrandDD_TotalTotalQuantitySold);
			}

			double TotalDropSize = 0;
			if (GrandTotalOrderCount != 0) {
				TotalDropSize = GrandTotalTotalQuantitySold / GrandTotalOrderCount;
			}

			double TotalSKUPerOrder = 0;
			if (GrandTotalOrderCount != 0) {
				TotalSKUPerOrder = GrandTotalTotalLinesSold / GrandTotalOrderCount;
			}

			double TotalD_DropSize = 0;
			if (GrandD_TotalInvoiceCount != 0) {
				TotalD_DropSize = GrandD_TotalTotalQuantitySold / GrandD_TotalInvoiceCount;
			}

			double TotalSKUPerInvoice = 0;
			if (GrandD_TotalInvoiceCount != 0) {
				TotalSKUPerInvoice = GrandD_TotalTotalLinesSold / GrandD_TotalInvoiceCount;
			}

			RowCount = RowCount + 1;

			XSSFRow headerrow2G = spreadsheet.createRow((short) RowCount);
			XSSFCell headercell2G = (XSSFCell) headerrow2G.createCell((short) 0);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 0);
			headercell2G.setCellValue("");
			SetNormalCellBackColorLeftAltGray(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 1);
			headercell2G.setCellValue("");
			SetNormalCellBackColor(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 2);
			headercell2G.setCellValue("Total");
			// headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColor(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 3);
			headercell2G.setCellValue(GrandTotalOutlets);
			headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColorGray(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 4);
			headercell2G.setCellValue(TotalAvgPlannedCalls);
			headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColor(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 5);
			headercell2G.setCellValue(GrandTotalScheduledCalls);
			headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColorGray(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 6);
			headercell2G.setCellValue(GrandTotalSchCalls);
			headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColor(workbook, headercell2G);

			// Order

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 7);
			headercell2G.setCellValue(GrandCallCompRate);

			SetNormalCellBackColorPercentGray(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 8);
			headercell2G.setCellValue(GrandTotalUnplannedCalls);
			headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColor(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 9);
			headercell2G.setCellValue(GrandUnplannedCallsPercentage);

			SetNormalCellBackColorPercentBlue(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 10);
			headercell2G.setCellValue(GrandTotalOrdersFromPlannedOutlets);
			headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColor(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 11);
			headercell2G.setCellValue(GrandTotalOrders);
			headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColorBlue(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 12);
			headercell2G.setCellValue(GrandOrderProductivity);

			SetNormalCellBackColorPercent(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 13);
			headercell2G.setCellValue(GrandTotalTotalQuantitySold);
			headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColorBlue(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 14);
			headercell2G.setCellValue(TotalDropSize);
			headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColor(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 15);
			headercell2G.setCellValue(TotalUniqueSKUSold);
			headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColorBlue(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 16);
			headercell2G
					.setCellValue(Double.parseDouble(Utilities.getDisplayCurrencyFormatOneDecimal(TotalSKUPerOrder)));

			SetNormalCellBackColorOneDecimal(workbook, headercell2G);
			// Delivery

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 17);
			headercell2G.setCellValue(GrandD_TotalTotalQuantitySold);
			headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColorPink(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 18);
			headercell2G.setCellValue(GrandTotalAvgDailyTarget);

			SetNormalCellBackColor(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 19);
			headercell2G.setCellValue(GrandSalesPerDay);
			headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColorPink(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 20);
			headercell2G.setCellValue(GrandTotalAvgDailySale);

			SetNormalCellBackColor(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 21);
			headercell2G.setCellValue(GrandDD_TotalTotalQuantitySold);
			headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColorPink(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 22);
			headercell2G.setCellValue(GrandD_DeliveryOrderPercentage);

			SetNormalCellBackColorPercent(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 23);
			headercell2G.setCellValue(GrandD_TotalQuantityDispatched);

			SetNormalCellBackColorPink(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 24);
			headercell2G.setCellValue(GrandD_Returns);
			headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColor(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 25);
			headercell2G.setCellValue(GrandD_ReturnsPercentage);

			SetNormalCellBackColorPercentPink(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 26);
			headercell2G.setCellValue(TotalD_DropSize);
			headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColor(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 27);
			headercell2G.setCellValue(TotalD_UniqueSKUSold);
			headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColorPink(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 28);
			headercell2G
					.setCellValue(Double.parseDouble(Utilities.getDisplayCurrencyFormatOneDecimal(TotalSKUPerInvoice)));
			headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColorOneDecimal(workbook, headercell2G);

			/*
			 * headercell2G = (XSSFCell) headerrow2G.createCell((short) 29);
			 * headercell2G.setCellValue(GrandZeroSalesCount);
			 * headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			 * SetNormalCellBackColorPink(workbook,headercell2G);
			 */
			double GrandWholeSalePercent = 0;
			if ((GrandD_TotalTotalQuantitySold + GrandTotalDeskSalesRawCases + GrandTotalSpotSalesRawCases) != 0) {
				GrandWholeSalePercent = (GrandTotalDeskSalesRawCases
						/ (GrandD_TotalTotalQuantitySold + GrandTotalDeskSalesRawCases + GrandTotalSpotSalesRawCases));
			}

			/// //// ///////////////////////
			headercell2G = (XSSFCell) headerrow2G.createCell((short) 29);
			headercell2G.setCellValue(GrandTotalDeskSalesRawCases);
			headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColorPink(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 30);
			headercell2G.setCellValue(GrandTotalSpotSalesRawCases);
			// headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColor(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 31);
			headercell2G.setCellValue("");
			// headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColor(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 32);
			headercell2G.setCellValue("");
			// headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColor(workbook, headercell2G);

			headercell2G = (XSSFCell) headerrow2G.createCell((short) 33);
			headercell2G.setCellValue("");
			// headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColor(workbook, headercell2G);

			FirstRowCount = RowCount; // at the end of loop make bother counter same

			FirstRowCount = FirstRowCount + 1;
			RowCount = RowCount + 1;

		}

		// Generated On

		// RowCount = RowCount+1;

		// Printing Date

		// Report Heading

		XSSFRow rowPG = spreadsheet.createRow((short) RowCount + 1);

		XSSFCell cellPG = (XSSFCell) rowPG.createCell((short) 0);

		cellPG.setCellValue("Generated On: " + Utilities.getDisplayDateTimeFormat(new Date()));

		spreadsheet.addMergedRegion(new CellRangeAddress(RowCount + 1, // first row (0-based)
				RowCount + 1, // last row (0-based)
				0, // first column (0-based)
				3 // last column (0-based)
		));

		for (int i = 0; i < 31; i++) {
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
		spreadsheet.setColumnHidden(4, true);

		FileOutputStream out = new FileOutputStream(new File(filename));
		workbook.write(out);
		out.close();
		// System.out.println(
		// "typesofcells.xlsx written successfully");

		ds.dropConnection();
	}

	public void Set2ndHeaderBackColor(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(162, 162, 163));

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

	public void Set2ndHeaderBackColorGray(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(162, 162, 163));

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
		style61.setAlignment(CellStyle.ALIGN_LEFT);
		headercell.setCellStyle(style61);

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

		style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
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

	public void SetNormalCellBackColor(XSSFWorkbook workbook, XSSFCell headercell) {

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

		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

		headercell.setCellStyle(style61);
	}

	public void SetNormalCellBackColorOneDecimal(XSSFWorkbook workbook, XSSFCell headercell) {

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
		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.#"));

		headercell.setCellStyle(style61);
	}

	public void Set2ndHeaderBackColorOtherKPI(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(162, 162, 163));

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

		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

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

	public void SetNormalCellBackColorPercentGray(XSSFWorkbook workbook, XSSFCell headercell) {

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

	public void SetNormalCellBackColorPercentPink12(XSSFWorkbook workbook, XSSFCell headercell) {

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

	public void SetBorder(XSSFWorkbook workbook, XSSFCell headercell) {
		XSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style);
	}

	public static Boolean IsGazettedHoliday(Date dated, long DistributorId) throws Exception {

		// System.out.println("this is dated "+Utilities.getDisplayDateFormat(dated));

		Boolean IsGazetted = false;

		Datasource ds = new Datasource();
		ds.createConnection();
		Statement s = ds.createStatement();
		Statement s2 = ds.createStatement();

		String WhereGroup = "";
		String WhereGroupIDs = "";

		int i = 0;
		ResultSet rsD1 = s
				.executeQuery("select id from common_distributor_groups_list where distributor_id=" + DistributorId);
		while (rsD1.next()) {
			if (i != 0) {
				WhereGroupIDs += ",";
			}
			WhereGroupIDs += rsD1.getString("id");
			i++;
		}
		if (WhereGroupIDs != null && WhereGroupIDs != "") {
			WhereGroup += "and distributor_group_id in (" + WhereGroupIDs + ")";
		}

		ResultSet rsD = s2
				.executeQuery("select * from common_holidays where date=" + Utilities.getSQLDate(dated) + WhereGroup);
		if (rsD.next()) {
			IsGazetted = true;

			System.out.println("Gazetted Holiday - " + Utilities.getDisplayDateFormat(dated));

		}
		s.close();
		s2.close();

		ds.dropConnection();

		return IsGazetted;

	}

}
