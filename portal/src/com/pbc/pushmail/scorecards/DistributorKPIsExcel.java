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

public class DistributorKPIsExcel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath() + "/CashInflowSummary.pdf";
	int NoOfDaysToGoBack = -1;

	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;

	// Date StartDate = Utilities.getDateByDays(0);
	// Date EndDate = Utilities.getDateByDays(0);

	Date StartDate = Utilities.getDateByDays(NoOfDaysToGoBack); // Utilities.parseDate("13/02/2016");
	Date EndDate = Utilities.getDateByDays(NoOfDaysToGoBack);
	// Utilities.parseDate("13/02/2016");

	Date Yesterday = Utilities.getDateByDays(-2);

	long SND_ID = 0;

	public static void main(String[] args) throws Exception {

	}

	public DistributorKPIsExcel()
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6) {
			Yesterday = Utilities.getDateByDays(-3);
		}

	}

	public void createPdf(String filename, long SND_ID, boolean isMTD) throws Exception {

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
		Date StartDateLastMonth = Utilities.getStartDateByMonth(month - 2, year);
		Date EndDateLastMonth = Utilities.getEndDateByMonth(month - 2, year);

		int DaysInLastMonth = Utilities.getDayNumberByDate(EndDateLastMonth);

		long TotalBrandCount = 0;

		double GrandTotalBalance = 0;
		double GrandTotalSecondarySales = 0;
		double GrandTotalLiftingTarget = 0;

		int FirstRowCount = 0;

		int RowCount = 0;

		// Report Heading

		XSSFRow rowH = spreadsheet.createRow((short) RowCount);
		XSSFCell cellH = (XSSFCell) rowH.createCell((short) 0);

		cellH.setCellValue("R230 - Distributor KPIs");

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
				1 // last column (0-based)
		));

		cellP = (XSSFCell) rowP.createCell((short) 2);
		cellP.setCellValue("");

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

		XSSFCellStyle style611 = workbook.createCellStyle();
		style611.setAlignment(CellStyle.ALIGN_RIGHT);
		cellP.setCellStyle(style611);

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

		headercell.setCellValue("");
		Set2ndHeaderBackColor(workbook, headercell);
		spreadsheet.addMergedRegion(new CellRangeAddress(FirstRowCount + 1, // first row (0-based)
				FirstRowCount + 1, // last row (0-based)
				0, // first column (0-based)
				5 // last column (0-based)
		));

		headercell = (XSSFCell) headerrow.createCell((short) 6);
		headercell.setCellValue("ORDER KPI");
		Set2ndHeaderBackColorOrderKPI(workbook, headercell);
		spreadsheet.addMergedRegion(new CellRangeAddress(FirstRowCount + 1, // first row (0-based)
				FirstRowCount + 1, // last row (0-based)
				6, // first column (0-based)
				14 // last column (0-based)
		));

		headercell = (XSSFCell) headerrow.createCell((short) 15);
		headercell.setCellValue("DELIVERY KPI");
		Set2ndHeaderBackColorDeliveryKPI(workbook, headercell);
		spreadsheet.addMergedRegion(new CellRangeAddress(FirstRowCount + 1, // first row (0-based)
				FirstRowCount + 1, // last row (0-based)
				15, // first column (0-based)
				27 // last column (0-based)
		));

		headercell = (XSSFCell) headerrow.createCell((short) 28);
		headercell.setCellValue("OTHER SALES");
		Set2ndHeaderBackColor(workbook, headercell);
		spreadsheet.addMergedRegion(new CellRangeAddress(FirstRowCount + 1, // first row (0-based)
				FirstRowCount + 1, // last row (0-based)
				28, // first column (0-based)
				29 // last column (0-based)
		));

		// 3rd Row Header //////////////////////
		///////////////////////////////////////

		RowCount = RowCount + 1;

		XSSFRow headerrow1 = spreadsheet.createRow((short) RowCount);
		XSSFCell headercell1 = (XSSFCell) headerrow1.createCell((short) 0);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 0);
		headercell1.setCellValue("Distributor");
		Set3rdHeaderBackColor(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 1);
		headercell1.setCellValue("TOTAL OUTLETS");
		Set3rdHeaderBackColor(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 2);
		headercell1.setCellValue("AVG. DAILY SCHED. CALLS");
		Set3rdHeaderBackColor(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 3);
		headercell1.setCellValue("SCHEDULED CALLS");
		Set3rdHeaderBackColor(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 4);
		headercell1.setCellValue("ACTUAL CALLS");
		Set3rdHeaderBackColor(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 5);
		headercell1.setCellValue("CALL COMPLETION %");
		Set3rdHeaderBackColor(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 6);
		headercell1.setCellValue("UNPLAN. CALLS/ ORDERS");
		Set3rdHeaderBackColorOrder(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 7);
		headercell1.setCellValue("UNPLANNED CALLS %");
		Set3rdHeaderBackColorOrder(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 8);
		headercell1.setCellValue("SCHED. ORDERS");
		Set3rdHeaderBackColorOrder(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 9);
		headercell1.setCellValue("TOTAL ORDERS");
		Set3rdHeaderBackColorOrder(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 10);
		headercell1.setCellValue("STRIKE RATE");
		Set3rdHeaderBackColorOrder(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 11);
		headercell1.setCellValue("ORDERED QTY (Converted)");
		Set3rdHeaderBackColorOrder(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 12);
		headercell1.setCellValue("DROP SIZE");
		Set3rdHeaderBackColorOrder(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 13);
		headercell1.setCellValue("TOTAL SKUs");
		Set3rdHeaderBackColorOrder(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 14);
		headercell1.setCellValue("AVG. SKU / ORDER");
		Set3rdHeaderBackColorOrder(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 15);
		headercell1.setCellValue("ACTUAL SALE (Converted)");
		Set3rdHeaderBackColorDelivery(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 16);
		headercell1.setCellValue("AVG. DAILY TARGET (R/C)");
		Set3rdHeaderBackColorDelivery(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 17);
		headercell1.setCellValue("AVG. DAILY ACTUAL SALE (R/C)");
		Set3rdHeaderBackColorDelivery(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 18);
		headercell1.setCellValue("BOM AVG. DAILY SALE REQ. (R/C)");
		Set3rdHeaderBackColorDelivery(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 19);
		headercell1.setCellValue("PRIOR DAY ORDERED QTY (Converted)");
		Set3rdHeaderBackColorDelivery(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 20);
		headercell1.setCellValue("DELIVERY VS. ORDER %");
		Set3rdHeaderBackColorDelivery(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 21);
		headercell1.setCellValue("LOAD OUT (Converted)");
		Set3rdHeaderBackColorDelivery(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 22);
		headercell1.setCellValue("FULL IN (Converted)");
		Set3rdHeaderBackColorDelivery(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 23);
		headercell1.setCellValue("FULL IN %");
		Set3rdHeaderBackColorDelivery(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 24);
		headercell1.setCellValue("DROP SIZE");
		Set3rdHeaderBackColorDelivery(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 25);
		headercell1.setCellValue("TOTAL SKUs");
		Set3rdHeaderBackColorDelivery(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 26);
		headercell1.setCellValue("AVG. SKU / INVOICE");
		Set3rdHeaderBackColorDelivery(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 27);
		headercell1.setCellValue("NO SALE OUTLETS");
		Set3rdHeaderBackColorDelivery(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 28);
		headercell1.setCellValue("DESK SALE (Converted)");
		Set3rdHeaderBackColor(workbook, headercell1);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 29);
		headercell1.setCellValue("SPOT SALE (Converted)");
		Set3rdHeaderBackColor(workbook, headercell1);

		/* Actual Row Data *//////
		//////////////////////////////////////////////////

		long PJPID = 0;
		String PJPName = "";

		long rDistributorID = 0;
		String rDistributorName = "";

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
		double Grand_UniqueSKUs = 0;
		double GrandTotalWholeSale = 0;
		double GrandCallCompRate = 0;

		double GrandTotalActuallSchCalls = 0;

		double GrandTotalDeskSalesRawCases = 0;
		double GrandTotalSpotSalesRawCases = 0;

		String AltRowColorGray = "#f7f5f5;";

		String AltRowColorBlue = "#f6fdfd;";

		String AltRowColorPink = "#f8f6f9";

		double GrandTotalAvgDailyTarget = 0;
		double GrandTotalAvgDailySale = 0;

		s.executeUpdate("SET SESSION group_concat_max_len = 1000000");

		double GrandTotalUniqueSKUs = 0;
		/*
		 * System.out.println(
		 * "select count(distinct product_id) from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mop.is_promotion = 0 and mo.created_on between "
		 * + Utilities.getSQLDate(StartDate) + " and " +
		 * Utilities.getSQLDateNext(EndDate) +
		 * " and mo.distributor_id in (select distributor_id from common_distributors where snd_id="
		 * + SND_ID + " OR rsm_id=" + SND_ID + " OR tdm_id=" + SND_ID + ")");
		 */
		ResultSet rs1211 = s3.executeQuery(
				"select count(distinct product_id) from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mop.is_promotion = 0 and mo.created_on between "
						+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
						+ " and mo.distributor_id in (select distributor_id from common_distributors where snd_id="
						+ SND_ID + " OR rsm_id=" + SND_ID + " OR tdm_id=" + SND_ID + ")");
		if (rs1211.first()) {
			GrandTotalUniqueSKUs = rs1211.getDouble(1);
		}

		String WhereSND = " and distributor_id in (select distributor_id from common_distributors where is_shifted_to_other_plant = 0 and (snd_id="
				+ SND_ID + " OR rsm_id=" + SND_ID + " OR tdm_id=" + SND_ID + ") ) ";
		if (SND_ID == 0) {
			WhereSND = " and distributor_id in (select distributor_id from common_distributors where is_shifted_to_other_plant = 0) ";
		}
		/*
		 * System.out.println(
		 * "select tab1.distributor_id, (select name from common_distributors cd where cd.distributor_id=tab1.distributor_id) distributor_name, (select count(distinct outlet_id) from distributor_beat_plan dbp join distributor_beat_plan_schedule dbps on dbp.id = dbps.id where dbp.distributor_id = tab1.distributor_id) outlet_count from ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between "
		 * + Utilities.getSQLDate(StartDate) + " and " +
		 * Utilities.getSQLDateNext(EndDate) + " " + WhereSND +
		 * " ) tab1 order by distributor_id");
		 */
		ResultSet rs2 = s.executeQuery(
				"select tab1.distributor_id, (select name from common_distributors cd where cd.distributor_id=tab1.distributor_id) distributor_name, (select count(distinct outlet_id) from distributor_beat_plan dbp join distributor_beat_plan_schedule dbps on dbp.id = dbps.id where dbp.distributor_id = tab1.distributor_id) outlet_count from ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between "
						+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) + " " + WhereSND
						+ " ) tab1 order by distributor_id");
		while (rs2.next()) {

			rDistributorID = rs2.getLong("distributor_id");
			rDistributorName = rs2.getString("distributor_name");
			int TotalOutlets = rs2.getInt("outlet_count");

			if (rDistributorID != 100914) {// 100914 was our test distributor for moiz

				GrandTotalOutlets += TotalOutlets;

				int AvgDailySchCall = 0;
				double ScheduledCalls = 0;// rs2.getInt("scheduled_calls");
				long OrderBookerID = 0;//// rs2.getInt("assigned_to");
				// String TotalOutletIDs = rs2.getString("total_outlet_ids");
				// System.out.println(TotalOutletIDs);
				String OrderBookerName = "";
				/*
				 * double ZeroSalesCount = 0; ResultSet rs50 =
				 * s2.executeQuery("select count(*) from (select outlet_id, to_days(date("
				 * +Utilities.getSQLDate(EndDate)
				 * +"))-to_days(max(created_on)) days from inventory_sales_adjusted where outlet_id in ("
				 * +TotalOutletIDs+") group by outlet_id having days > 15) tab1"); if
				 * (rs50.first()){ ZeroSalesCount = rs50.getDouble(1); }
				 * 
				 * 
				 * GrandZeroSalesCount +=ZeroSalesCount;
				 */
				// select avg(ct) from (select day_number, count(outlet_id) ct from
				// distributor_beat_plan_schedule where id = 55 group by day_number) tab1
				ResultSet rs3 = s2.executeQuery(
						"select avg(ct) avg_daily_sch_call from (select day_number, count(outlet_id) ct from distributor_beat_plan_schedule where id in (select id from distributor_beat_plan where distributor_id = "
								+ rDistributorID + ") group by day_number) tab1");
				while (rs3.next()) {
					AvgDailySchCall = rs3.getInt("avg_daily_sch_call");
				}

				GrandAvgDailySchCallTotal += AvgDailySchCall;

				double TotalUniqueSKUs = 0;
				ResultSet rs121 = s3.executeQuery(
						"select count(distinct product_id) from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mo.created_on between "
								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
								+ " and distributor_id = " + rDistributorID);
				if (rs121.first()) {
					TotalUniqueSKUs = rs121.getDouble(1);
				}

				/*
				 * ResultSet rs4 =
				 * s2.executeQuery("SELECT display_name FROM users where id="+OrderBookerID);
				 * while(rs4.next()){ OrderBookerName = rs4.getString("display_name"); }
				 */

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

				double TotalActuallSchCalls = 0;

				double D_TotalTargetRawCases = 0;

				Date CurrentDate = StartDate;
				int i = 0;
				String DaysOfWeek = "-1";
				double days = 0;
				int IsSundayOff = Utilities.IsDistributorOFFStatus(rDistributorID);

				int DayNumberOff = 0;

				if (IsSundayOff == 0) { // Mean Friday Off - Normal Case
					DayNumberOff = 6;
				} else {
					DayNumberOff = 1; // Else Sunday Off
				}
				while (true) {
					// if(Utilities.getDayOfWeekByDate(CurrentDate)!=6 &&
					// !IsGazettedHoliday(CurrentDate,rDistributorID)){ //if it is not friday
					if (Utilities.getDayOfWeekByDate(CurrentDate) != DayNumberOff
							&& !IsGazettedHoliday(CurrentDate, rDistributorID)) { // if it is not friday

						DaysOfWeek += "," + Utilities.getDayOfWeekByDate(CurrentDate);
						//////// System.out.println("Current Date : "+CurrentDate);
						days++;
						double PlannedOutletsCount = 0;
						String PlannedOutletIDs = "";

						ResultSet rs5 = s3.executeQuery(
								"select count(outlet_id), group_concat(outlet_id) from distributor_beat_plan_log where log_date between "
										+ Utilities.getSQLDate(CurrentDate) + " and "
										+ Utilities.getSQLDate(CurrentDate) + " and distributor_id = " + rDistributorID
										+ " and day_number = dayofweek(log_date)");
						if (rs5.first()) {
							PlannedOutletsCount = rs5.getDouble(1);
							PlannedOutletIDs = rs5.getString(2);
						}

						ScheduledCalls += PlannedOutletsCount;
//								
//								if (rDistributorID == 100941){
//									System.out.println(CurrentDate+"\t"+PlannedOutletsCount);
//								}
						//

						String OrderIDs = "";
						String OutletIDsOrdered = "";
						int OrderCount = 0;
						int UniqueOutletsOrdered = 0;

						ResultSet rs45 = s3.executeQuery(
								"select group_concat(mo.id) order_ids, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, group_concat(distinct mo.outlet_id) outlet_ids_ordered from mobile_order mo where mo.created_on between "
										+ Utilities.getSQLDate(CurrentDate) + " and "
										+ Utilities.getSQLDateNext(CurrentDate) + " and mo.distributor_id ="
										+ rDistributorID);
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
						// ResultSet rs101 = s3.executeQuery("select count(id) from common_outlets where
						// id in ("+PlannedOutletIDs+") and id in (SELECT distinct outlet_id FROM
						// mobile_order_zero where created_on between
						// "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+"
						// and outlet_id in (select id from common_outlets where cache_distributor_id =
						// "+rDistributorID+") union select distinct id from common_outlets where id in
						// ("+PlannedOutletIDs+") and id in ("+OutletIDsOrdered+"))");
						ResultSet rs101 = s3.executeQuery("select count(id) from common_outlets where id in ("
								+ PlannedOutletIDs
								+ ") and (id in (SELECT distinct outlet_id FROM mobile_order_zero where created_on between "
								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
								+ " and outlet_id in (select id from common_outlets where cache_distributor_id = "
								+ rDistributorID + ")) or id in (select distinct id from common_outlets where id in ("
								+ PlannedOutletIDs + ") and id in (" + OutletIDsOrdered + ")))");
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
								"select sum(mop.total_units*ipv.liquid_in_ml)/250 total_qty_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id  where mo.id in ("
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

				if (rDistributorID == 100953) {
					// System.out.println("select count(distinct outlet_id) from
					// distributor_beat_plan_log where log_date between
					// "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and
					// distributor_id = "+rDistributorID+" and day_number in ("+DaysOfWeek+") and
					// outlet_id not in (select distinct outlet_id from mobile_order where
					// distributor_id = "+rDistributorID+" and created_on between
					// "+Utilities.getSQLDate(StartDate)+" and
					// "+Utilities.getSQLDateNext(EndDate)+")");
				}

				double ZeroSalesCount = 0;
				ResultSet rs50 = s2.executeQuery(
						"select count(distinct outlet_id) from distributor_beat_plan_log where log_date between "
								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)
								+ " and distributor_id = " + rDistributorID + " and day_number in (" + DaysOfWeek
								+ ") and outlet_id not in (select distinct outlet_id from mobile_order where created_on between "
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

				double D_UniqueSKUs = 0;
				ResultSet D_rs51 = s3.executeQuery(
						"select count(distinct isap.product_id) from inventory_sales_adjusted_products isap where isap.cache_order_created_on_date between "
								+ Utilities.getSQLDate(StartDate1) + " and " + Utilities.getSQLDate(EndDate1)
								+ " and isap.cache_distributor_id = " + rDistributorID);
				if (D_rs51.first()) {
					D_UniqueSKUs = D_rs51.getDouble(1);
				}

				ResultSet D_rs511 = s3.executeQuery(
						"select count(distinct isap.product_id) from inventory_sales_adjusted_products isap where isap.cache_order_created_on_date between "
								+ Utilities.getSQLDate(StartDate1) + " and " + Utilities.getSQLDate(EndDate1));
				if (D_rs511.first()) {
					Grand_UniqueSKUs = D_rs511.getDouble(1);
				}

				Date CurrentDate1 = StartDate1;
				int i1 = 0;

				double days1 = 0;
				while (true) {
					if (Utilities.getDayOfWeekByDate(CurrentDate1) != 6) { // if it is not friday
						///////////// System.out.println("Current Date1 : "+CurrentDate1);

						days1++;
						double PlannedOutletsCount = 0;
						String PlannedOutletIDs = "";

						ResultSet rs5 = s3.executeQuery(
								"select count(distinct outlet_id), group_concat(distinct outlet_id) from distributor_beat_plan_log where log_date between "
										+ Utilities.getSQLDate(CurrentDate1) + " and "
										+ Utilities.getSQLDate(CurrentDate1) + " and distributor_id = " + rDistributorID
										+ " and day_number = dayofweek(log_date)");
						if (rs5.first()) {
							PlannedOutletsCount = rs5.getDouble(1);
							PlannedOutletIDs = rs5.getString(2);
						}

						// ScheduledCalls += PlannedOutletsCount;

						//

						String OrderIDs = "";
						String OutletIDsOrdered = "";
						int OrderCount = 0;
						int UniqueOutletsOrdered = 0;
						ResultSet rs45 = s3.executeQuery(
								"select group_concat(mo.id) order_ids, count(mo.id) order_count, count(distinct mo.outlet_id) unique_outlets_ordered, group_concat(distinct mo.outlet_id) outlet_ids_ordered from mobile_order mo where mo.created_on between "
										+ Utilities.getSQLDate(CurrentDate1) + " and "
										+ Utilities.getSQLDateNext(CurrentDate1) + " and mo.distributor_id ="
										+ rDistributorID);
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
								"select sum(isap.total_units*ipv.liquid_in_ml)/250 total_qty_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id=ipv.product_id where isa.order_id in ("
										+ OrderIDs + ")");
						if (D_rs9.first()) {
							D_TotalQuantitySold = D_rs9.getDouble("total_qty_sold");
						}
						D_TotalTotalQuantitySold += D_TotalQuantitySold;

						double D_WholesaleQuantitySold = 0;
//								ResultSet WD_rs9 = s3.executeQuery("select sum(isap.total_units/cache_units_per_sku) total_qty_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.order_id in ("+OrderIDs+") and isa.outlet_id in (select id from common_outlets where channel_id in (12,13) and cache_distributor_id = "+rDistributorID+" )");
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

						ResultSet D_rs4 = s3
								.executeQuery("select count(id) from inventory_sales_adjusted where order_id in ("
										+ OrderIDs + ")  and net_amount != 0");
						if (D_rs4.first()) {
							InvoiceCount = D_rs4.getDouble(1);
						}
						D_TotalInvoiceCount += InvoiceCount;

						double D_TotalLinesSold = 0;
						ResultSet D_rs5 = s3.executeQuery(
								"select count(*) total_lines_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.order_id in ("
										+ OrderIDs + ")");
						if (D_rs5.first()) {
							D_TotalLinesSold = D_rs5.getDouble("total_lines_sold");
						}
						D_TotalTotalLinesSold += D_TotalLinesSold;

						double TargetRawCases = 0;
						// ResultSet D_rs19 = s3.executeQuery("SELECT
						// sum(quantity)/dayofmonth(last_day(date("+Utilities.getSQLDate(CurrentDate1)+")))
						// FROM pep.distributor_targets dt join distributor_targets_packages dtp on
						// dt.id = dtp.id where month = month("+Utilities.getSQLDate(CurrentDate1)+")
						// and year = year("+Utilities.getSQLDate(CurrentDate1)+") and distributor_id =
						// "+rDistributorID);
						ResultSet D_rs19 = s3.executeQuery(
								"SELECT sum(quantity) FROM pep.distributor_targets dt join distributor_targets_packages dtp on dt.id = dtp.id where month = month("
										+ Utilities.getSQLDate(CurrentDate1) + ") and year = year("
										+ Utilities.getSQLDate(CurrentDate1) + ") and distributor_id = "
										+ rDistributorID);
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

				// BOM Target
				double TotalMonthTargetRawCases = 0;
				// System.out.println("SELECT sum(quantity) FROM pep.distributor_targets dt join
				// distributor_targets_packages dtp on dt.id = dtp.id where month =
				// month("+Utilities.getSQLDate(EndDate1)+") and year =
				// year("+Utilities.getSQLDate(EndDate1)+") and distributor_id =
				// "+rDistributorID);
				ResultSet D_rs191 = s3.executeQuery(
						"SELECT sum(quantity) FROM pep.distributor_targets dt join distributor_targets_packages dtp on dt.id = dtp.id where month = month("
								+ Utilities.getSQLDate(EndDate1) + ") and year = year(" + Utilities.getSQLDate(EndDate1)
								+ ") and distributor_id = " + rDistributorID);
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
						"select sum(total_units/cache_units_per_sku) from inventory_sales_adjusted_products where cache_created_on_date between "
								+ Utilities.getSQLDate(Utilities.getStartDateByDate(EndDate1)) + " and "
								+ Utilities.getSQLDateNext(EndDate1) + " and cache_distributor_id = " + rDistributorID
								+ "");
				if (D_rs192.first()) {
					TotalMonthSalesRawCases = D_rs192.getDouble(1);
				}

				double TotalDeskSalesRawCases = 0;
				ResultSet D_rs193 = s3.executeQuery(
						"SELECT sum(isap.total_units*ipv.liquid_in_ml)/250 FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id=ipv.product_id where isa.type_id = 1 and isa.created_on_date between "
								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)
								+ " and isa.distributor_id = " + rDistributorID + "");
				if (D_rs193.first()) {
					TotalDeskSalesRawCases = D_rs193.getDouble(1);
				}
				GrandTotalDeskSalesRawCases += TotalDeskSalesRawCases;

				double TotalSpotSalesRawCases = 0;
				ResultSet D_rs194 = s3.executeQuery(
						"SELECT sum(isap.total_units*ipv.liquid_in_ml)/250 FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id=ipv.product_id where isa.type_id = 2 and isa.created_on_date between "
								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDate(EndDate)
								+ " and isa.distributor_id = " + rDistributorID + "");
				if (D_rs194.first()) {
					TotalSpotSalesRawCases = D_rs194.getDouble(1);
				}
				GrandTotalSpotSalesRawCases += TotalSpotSalesRawCases;

				int DaysInMonth = Utilities.getDayNumberByDate(Utilities.getEndDateByDate(EndDate1));
				int CurrentDay = Utilities.getDayNumberByDate(EndDate1);
				int RemainingDays = (DaysInMonth - CurrentDay) - Utilities.getSubsequentFridayCountByDate(EndDate1);

				double RemainingTarget = TotalMonthTargetRawCases - TotalMonthSalesRawCases;

				// System.out.println(TotalMonthTargetRawCases +" "+TotalMonthSalesRawCases+"
				// "+RemainingDays);

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

				// GrandDropSize +=DropSize;

				double D_DropSize = 0;
				if (D_TotalInvoiceCount != 0) {
					D_DropSize = D_TotalTotalQuantitySold / D_TotalInvoiceCount;
				}

				GrandD_TotalInvoiceCount += D_TotalInvoiceCount;

				GrandD_DropSize += D_DropSize;

				if (PJPID == 55) {
					System.out.println(TotalTotalQuantitySold + " " + D_TotalTotalQuantitySold);
					System.out.println(TotalOrderCount + " " + D_TotalInvoiceCount);
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

				GrandTotalActuallSchCalls += TotalActuallSchCalls;

				XSSFRow headerrow2 = spreadsheet.createRow((short) RowCount + 1);
				XSSFCell headercell2 = (XSSFCell) headerrow2.createCell((short) 0);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 0);
				headercell2.setCellValue(rDistributorID + " - " + Utilities.truncateStringToMax(rDistributorName, 22));
				SetNormalCellBackColorLeftAltGray(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
				headercell2.setCellValue(TotalOutlets);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 2);
				headercell2.setCellValue(AvgDailySchCall);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorGray(workbook, headercell2);

				// Patch - Modified (27/09/2016)
				double SchCallsNew = 0;

				if (AvgDailySchCall != 0) {
					SchCallsNew = ((ScheduledCalls - AvgDailySchCall) / AvgDailySchCall) * 100;
				}

				////////////////////////////////////

				headercell2 = (XSSFCell) headerrow2.createCell((short) 3);

//					    if(SchCallsNew>=20){
//					    	headercell2.setCellValue(ScheduledCalls);
//						    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
//						    SetNormalCellBackColor12(workbook,headercell2);
//					    }else{
				headercell2.setCellValue(ScheduledCalls);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);
//					    }

				headercell2 = (XSSFCell) headerrow2.createCell((short) 4);
				headercell2.setCellValue(TotalActuallSchCalls);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorGray(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 5);
				headercell2.setCellValue(CallCompRate);

				SetNormalCellBackColorPercent(workbook, headercell2);

				// Order

				headercell2 = (XSSFCell) headerrow2.createCell((short) 6);
				headercell2.setCellValue(TotalUnplannedCalls);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorBlue(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 7);
				headercell2.setCellValue(UnplannedCallsPercentage);

				SetNormalCellBackColorPercent(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 8);
				headercell2.setCellValue(TotalOrdersFromPlannedOutlets);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorBlue(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 9);
				headercell2.setCellValue(TotalOrders);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 10);
				headercell2.setCellValue(OrderProductivity);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorPercentBlue(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 11);
				headercell2.setCellValue(TotalTotalQuantitySold);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 12);
				headercell2.setCellValue(DropSize);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorBlue(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 13);
				headercell2.setCellValue(TotalUniqueSKUs);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 14);
				headercell2.setCellValue(Double.parseDouble(Utilities.getDisplayCurrencyFormatOneDecimal(SKUPerOrder)));
				// headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorBlueOneDecimal(workbook, headercell2);

				// Delivery

				headercell2 = (XSSFCell) headerrow2.createCell((short) 15);
				headercell2.setCellValue(D_TotalTotalQuantitySold);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				// ResultSet

				headercell2 = (XSSFCell) headerrow2.createCell((short) 16);
				headercell2.setCellValue(AverageDailyTarget);
				SetNormalCellBackColorPink(workbook, headercell2);

				GrandTotalAvgDailyTarget += AverageDailyTarget;

				headercell2 = (XSSFCell) headerrow2.createCell((short) 17);
				headercell2.setCellValue(SalesPerDay);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 18);
				headercell2.setCellValue(BOMDailyTarget);

				SetNormalCellBackColorPink(workbook, headercell2);

				GrandTotalAvgDailySale += BOMDailyTarget;

				headercell2 = (XSSFCell) headerrow2.createCell((short) 19);
				headercell2.setCellValue(DD_TotalTotalQuantitySold);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 20);
				headercell2.setCellValue(DeliveryOrderPercentage);

				SetNormalCellBackColorPercentPink(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 21);
				headercell2.setCellValue(D_TotalQuantityDispatched);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 22);
				headercell2.setCellValue(D_Returns);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorPink(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 23);
				// System.out.println(D_ReturnsPercentage);

				if (D_ReturnsPercentage > 0.05) {

					headercell2.setCellValue(D_ReturnsPercentage);

					SetNormalCellBackColorPercentRed(workbook, headercell2);
				} else {
					headercell2.setCellValue(D_ReturnsPercentage);
					SetNormalCellBackColorPercent(workbook, headercell2);
				}

				headercell2 = (XSSFCell) headerrow2.createCell((short) 24);
				headercell2.setCellValue(D_DropSize);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorPink(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 25);
				headercell2.setCellValue(D_UniqueSKUs);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 26);
				headercell2
						.setCellValue(Double.parseDouble(Utilities.getDisplayCurrencyFormatOneDecimal(SKUPerInvoice)));
				// headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColorPinkOneDecimal(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 27);
				headercell2.setCellValue(ZeroSalesCount);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				double TotalWholeSalePercent = 0;
				if ((D_TotalTotalQuantitySold + TotalDeskSalesRawCases + TotalSpotSalesRawCases) != 0) {
					TotalWholeSalePercent = (TotalDeskSalesRawCases
							/ (D_TotalTotalQuantitySold + TotalDeskSalesRawCases + TotalSpotSalesRawCases));
				}

				headercell2 = (XSSFCell) headerrow2.createCell((short) 28);
				headercell2.setCellValue(TotalDeskSalesRawCases);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				headercell2 = (XSSFCell) headerrow2.createCell((short) 29);
				headercell2.setCellValue(TotalSpotSalesRawCases);
				headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				SetNormalCellBackColor(workbook, headercell2);

				RowCount++;
			}

		}

		//////////////////////////////////////////////////////////////////////

		// Total ///
		///////////
		if (GrandTotalScheduledCalls != 0) {
			GrandOrderProductivity = (GrandTotalOrdersFromPlannedOutlets / GrandTotalScheduledCalls);
		}

		if (GrandTotalScheduledCalls != 0) {
			GrandCallCompRate = (GrandTotalActuallSchCalls / GrandTotalScheduledCalls);
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

		double D_TotalDropSize = 0;
		if (GrandD_TotalInvoiceCount != 0) {
			D_TotalDropSize = GrandD_TotalTotalQuantitySold / GrandD_TotalInvoiceCount;
		}

		double TotalSKUPerOrder = 0;
		if (GrandTotalOrderCount != 0) {
			TotalSKUPerOrder = GrandTotalTotalLinesSold / GrandTotalOrderCount;
		}

		double TotalSKUPerInvoice = 0;
		if (GrandD_TotalInvoiceCount != 0) {
			TotalSKUPerInvoice = GrandD_TotalTotalLinesSold / GrandD_TotalInvoiceCount;
		}

		RowCount = RowCount + 1;

		XSSFRow headerrow2 = spreadsheet.createRow((short) RowCount);
		XSSFCell headercell2 = (XSSFCell) headerrow2.createCell((short) 0);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 0);
		headercell2.setCellValue("Total");
		SetNormalCellBackColorLeftAltGray(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
		headercell2.setCellValue(GrandTotalOutlets);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColor(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 2);
		headercell2.setCellValue(GrandAvgDailySchCallTotal);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColorGray(workbook, headercell2);

		// Patch - Modified (27/09/2016)
		double SchCallsNewTot = 0;

		if (GrandAvgDailySchCallTotal != 0) {
			SchCallsNewTot = ((GrandTotalScheduledCalls - GrandAvgDailySchCallTotal) / GrandAvgDailySchCallTotal) * 100;
		}

		headercell2 = (XSSFCell) headerrow2.createCell((short) 3);

//				    if(SchCallsNewTot>=20){
//				    	 headercell2.setCellValue(GrandTotalScheduledCalls);
//						    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
//						    SetNormalCellBackColorRed12(workbook,headercell2);
//				    }else{
		headercell2.setCellValue(GrandTotalScheduledCalls);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColor(workbook, headercell2);
//				    }

		////////////////////////////////////

		headercell2 = (XSSFCell) headerrow2.createCell((short) 4);
		headercell2.setCellValue(GrandTotalActuallSchCalls);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColorGray(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 5);
		headercell2.setCellValue(GrandCallCompRate);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColorPercent(workbook, headercell2);

		// Order

		headercell2 = (XSSFCell) headerrow2.createCell((short) 6);
		headercell2.setCellValue(GrandTotalUnplannedCalls);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColorBlue(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 7);
		headercell2.setCellValue(GrandUnplannedCallsPercentage);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColorPercent(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 8);
		headercell2.setCellValue(GrandTotalOrdersFromPlannedOutlets);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColorBlue(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 9);
		headercell2.setCellValue(GrandTotalOrders);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColor(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 10);
		headercell2.setCellValue(GrandOrderProductivity);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColorPercentBlue(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 11);
		headercell2.setCellValue(GrandTotalTotalQuantitySold);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColor(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 12);
		headercell2.setCellValue(TotalDropSize);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColorBlue(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 13);
		// headercell2.setCellValue(GrandTotalUniqueSKUs);
		headercell2.setCellValue("");
		// headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColor(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 14);
		headercell2.setCellValue(Double.parseDouble(Utilities.getDisplayCurrencyFormatOneDecimal(TotalSKUPerOrder)));
		// headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColorBlueOneDecimal(workbook, headercell2);

		headercell2.setCellValue(Double.parseDouble(Utilities.getDisplayCurrencyFormatOneDecimal(TotalSKUPerOrder)));
		// headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColorBlueOneDecimal(workbook, headercell2);

		// Delivery

		headercell2 = (XSSFCell) headerrow2.createCell((short) 15);
		headercell2.setCellValue(GrandD_TotalTotalQuantitySold);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColor(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 16);
		headercell2.setCellValue(GrandTotalAvgDailyTarget);

		SetNormalCellBackColorPink(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 17);
		headercell2.setCellValue(GrandSalesPerDay);
		// headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColor(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 18);
		headercell2.setCellValue(GrandTotalAvgDailySale);

		SetNormalCellBackColorPink(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 19);
		headercell2.setCellValue(GrandDD_TotalTotalQuantitySold);
		// headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColor(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 20);
		headercell2.setCellValue(GrandD_DeliveryOrderPercentage);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColorPercentPink(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 21);
		headercell2.setCellValue(GrandD_TotalQuantityDispatched);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColor(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 22);
		headercell2.setCellValue(GrandD_Returns);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColorPink(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 23);

		if (GrandD_ReturnsPercentage > 0.05) {
			headercell2.setCellValue(GrandD_ReturnsPercentage);
			headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColorPercentRed(workbook, headercell2);
		} else {
			headercell2.setCellValue(GrandD_ReturnsPercentage);
			headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
			SetNormalCellBackColorPercent(workbook, headercell2);
		}

		headercell2 = (XSSFCell) headerrow2.createCell((short) 24);
		headercell2.setCellValue(D_TotalDropSize);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColorPink(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 25);
		headercell2.setCellValue(Grand_UniqueSKUs);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColor(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 26);
		headercell2.setCellValue(Double.parseDouble(Utilities.getDisplayCurrencyFormatOneDecimal(TotalSKUPerInvoice)));
		// headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColorPinkOneDecimal(workbook, headercell2);

		headercell2.setCellValue(Double.parseDouble(Utilities.getDisplayCurrencyFormatOneDecimal(TotalSKUPerInvoice)));
		// headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColorPinkOneDecimal(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 27);
		headercell2.setCellValue(GrandZeroSalesCount);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColor(workbook, headercell2);

		double GrandWholeSalePercent = 0;
		if ((GrandD_TotalTotalQuantitySold + GrandTotalDeskSalesRawCases + GrandTotalSpotSalesRawCases) != 0) {
			GrandWholeSalePercent = (GrandTotalDeskSalesRawCases
					/ (GrandD_TotalTotalQuantitySold + GrandTotalDeskSalesRawCases + GrandTotalSpotSalesRawCases));
		}

		headercell2 = (XSSFCell) headerrow2.createCell((short) 28);
		headercell2.setCellValue(GrandTotalDeskSalesRawCases);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColor(workbook, headercell2);

		headercell2 = (XSSFCell) headerrow2.createCell((short) 29);
		headercell2.setCellValue(GrandTotalSpotSalesRawCases);
		headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		SetNormalCellBackColor(workbook, headercell2);
		
		
		// KPIs for Spot Sales/Counter Sales/Non-Automation Towns

		
		createExcelSheetWithActualSale(filename,SND_ID,isMTD,workbook);

		// Generated On

		RowCount = RowCount + 1;

		// Printing Date

		// Report Heading

		XSSFRow rowPG = spreadsheet.createRow((short) RowCount + 1);

		XSSFCell cellPG = (XSSFCell) rowPG.createCell((short) 0);

		cellPG.setCellValue("Generated On: " + Utilities.getDisplayDateTimeFormat(new Date()));

		spreadsheet.addMergedRegion(new CellRangeAddress(RowCount + 1, // first row (0-based)
				RowCount + 1, // last row (0-based)
				0, // first column (0-based)
				1 // last column (0-based)
		));

		// Auto Sizing Column

		for (int i = 0; i < 30; i++) {
			// System.out.println("Auto Sizing - "+i);
			try {
				spreadsheet.autoSizeColumn(i);
			} catch (Exception e) {
				System.out.println(i);
				e.printStackTrace();
			}
		}

		spreadsheet.setColumnHidden(2, true);
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
	
	
	//-----------------------------------------------------------------------------------------------------//
	
	public void createExcelSheetWithActualSale(String filename, long SND_ID,boolean isMTD,XSSFWorkbook workbook) throws Exception {
		if (isMTD){
			this.StartDate = Utilities.getStartDateByDate(Utilities.getDateByDays(-1));
			this.EndDate = Utilities.getDateByDays(-1);
			
			////this.StartDate=Utilities.parseDate("01/04/2020");
			////this.EndDate=Utilities.parseDate("05/04/2020");
		}
		
		
		
	    XSSFSheet spreadsheet = workbook.createSheet("All KPI");
	      
	      
	      
	      
	      int month = Utilities.getMonthNumberByDate(EndDate);
			int year = Utilities.getYearByDate(EndDate);
			Date StartDateLastMonth = Utilities.getStartDateByMonth(month-2, year);
			Date EndDateLastMonth = Utilities.getEndDateByMonth(month-2, year);
			
			int DaysInLastMonth = Utilities.getDayNumberByDate(EndDateLastMonth);
			
			long TotalBrandCount=0;
			
			double GrandTotalBalance = 0;
			double GrandTotalSecondarySales = 0;
			double GrandTotalLiftingTarget = 0;
			
			int FirstRowCount=0;
			
			int RowCount=0;
			
			
			
			
			
			//Report Heading
			
			 XSSFRow rowH = spreadsheet.createRow((short) RowCount);	      
		     XSSFCell cellH = (XSSFCell) rowH.createCell((short) 0);
		     
		     cellH.setCellValue("R230 - Distributor KPIs");
			
		     spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  FirstRowCount, //first row (0-based)
		    		  FirstRowCount, //last row (0-based)
		    		  0, //first column (0-based)
		    	      1 //last column (0-based)
		    	      ));
			
		     
		     CellStyle style = workbook.createCellStyle();//Create style
		     XSSFFont font = workbook.createFont();//Create font
		     font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		     style.setFont(font);//set it to bold
		     cellH.setCellStyle(style);
		     
			
		     RowCount = RowCount+1;
			FirstRowCount = FirstRowCount+1;
			//Printing Date
			
			//Report Heading
			
			 XSSFRow rowP = spreadsheet.createRow((short) RowCount);	      
		     
			 XSSFCell cellP = (XSSFCell) rowP.createCell((short) 0);		     
		   
		     
		     cellP.setCellValue("Period: "+Utilities.getDisplayDateFormat(StartDate)+" - "+Utilities.getDisplayDateFormat(EndDate));
				
		     spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  FirstRowCount, //first row (0-based)
		    		  FirstRowCount, //last row (0-based)
		    		  0, //first column (0-based)
		    	      1 //last column (0-based)
		    	      ));
		     
		     cellP = (XSSFCell) rowP.createCell((short) 2);		     
		     cellP.setCellValue("");
		     
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
			 
			
			

		    
		     XSSFCellStyle style611 = workbook.createCellStyle();		      
		     style611.setAlignment(CellStyle.ALIGN_RIGHT);
		     cellP.setCellStyle(style611);
		      
		     
		     
		     // Extra Row
		     
		     
		     RowCount = RowCount+1;
		     FirstRowCount = FirstRowCount+1;	
				//Printing Date
				
				//Report Heading
				
				 XSSFRow rowEx1 = spreadsheet.createRow((short) RowCount);	      
			     
				 XSSFCell cellEx1 = (XSSFCell) rowEx1.createCell((short) 0);		     
			   
			     
				 cellEx1 = (XSSFCell) rowEx1.createCell((short) 0);	
				 cellEx1.setCellValue("");
				 
				 cellEx1 = (XSSFCell) rowEx1.createCell((short) 1);	
				 cellEx1.setCellValue("");						
			    
			     
				 
		     
			
		   /////// //////////////////////
		     
		    //2nd row header
		      
			     
		      
		      RowCount = RowCount+1;
		      
		      XSSFRow headerrow = spreadsheet.createRow((short) RowCount);	      
		      XSSFCell headercell = (XSSFCell) headerrow.createCell((short) 0);
		     
		      headercell.setCellValue("");
		      Set2ndHeaderBackColor(workbook,headercell);
		      spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  FirstRowCount+1, //first row (0-based)
		    		  FirstRowCount+1, //last row (0-based)
		    		  0, //first column (0-based)
		    	      2 //last column (0-based)
		    	      ));
		      
		     
		          
		      
		     
		      
		      headercell = (XSSFCell) headerrow.createCell((short) 3);	     
		      headercell.setCellValue("DELIVERY KPI");
		      Set2ndHeaderBackColorDeliveryKPI(workbook,headercell);
		      spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  FirstRowCount+1, //first row (0-based)
		    		  FirstRowCount+1, //last row (0-based)
		    		  3, //first column (0-based)
		    	      11 //last column (0-based)
		    	      ));
		    
		      headercell = (XSSFCell) headerrow.createCell((short) 12);	     
		      headercell.setCellValue("OTHER SALES");
		      Set2ndHeaderBackColor(workbook,headercell);
		      spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  FirstRowCount+1, //first row (0-based)
		    		  FirstRowCount+1, //last row (0-based)
		    		  12, //first column (0-based)
		    	      14 //last column (0-based)
		    	      ));

		      
		      //3rd Row Header //////////////////////
			     ///////////////////////////////////////
			    
			     RowCount = RowCount+1;
			     
			     XSSFRow headerrow1 = spreadsheet.createRow((short) RowCount);        
			     XSSFCell headercell1 = (XSSFCell) headerrow1.createCell((short) 0);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 0);	     
			     headercell1.setCellValue("Distributor");
			     Set3rdHeaderBackColor(workbook,headercell1);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 1);	     
			     headercell1.setCellValue("TOTAL OUTLETS");
			     Set3rdHeaderBackColor(workbook,headercell1);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 2);	 
			     headercell1.setCellValue("AVG. DAILY SCHED. CALLS");
			     Set3rdHeaderBackColor(workbook,headercell1);
			     
			     
			    		     
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 3);	     
			     headercell1.setCellValue("ACTUAL SALE (Converted)");
			     Set3rdHeaderBackColorDelivery(workbook,headercell1);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 4);	     
			     headercell1.setCellValue("AVG. DAILY TARGET (Converted)");
			     Set3rdHeaderBackColorDelivery(workbook,headercell1);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 5);	     
			     headercell1.setCellValue("AVG. DAILY ACTUAL SALE (Converted)");
			     Set3rdHeaderBackColorDelivery(workbook,headercell1);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 6);	     
			     headercell1.setCellValue("BOM AVG. DAILY SALE REQ. (Converted)");
			     Set3rdHeaderBackColorDelivery(workbook,headercell1);
			     
			    
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 7);	     
			     headercell1.setCellValue("LOAD OUT (Converted");
			     Set3rdHeaderBackColorDelivery(workbook,headercell1);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 8);	     
			     headercell1.setCellValue("FULL IN");
			     Set3rdHeaderBackColorDelivery(workbook,headercell1);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 9);	     
			     headercell1.setCellValue("DROP SIZE");
			     Set3rdHeaderBackColorDelivery(workbook,headercell1);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 10);	     
			     headercell1.setCellValue("TOTAL SKUs");
			     Set3rdHeaderBackColorDelivery(workbook,headercell1);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 11);	     
			     headercell1.setCellValue("AVG. SKU / INVOICE");
			     Set3rdHeaderBackColorDelivery(workbook,headercell1);
			     
			   
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 12);	     
			     headercell1.setCellValue("WHOLESALE % OF TOTAL");
			     Set3rdHeaderBackColorDelivery(workbook,headercell1);
			     
			     

			     headercell1 = (XSSFCell) headerrow1.createCell((short) 13);	     
			     headercell1.setCellValue("WHOLESALE (Converted)");
			     Set3rdHeaderBackColor(workbook,headercell1);

			     headercell1 = (XSSFCell) headerrow1.createCell((short) 14);	     
			     headercell1.setCellValue("SPOT SALE (Converted)");
			     Set3rdHeaderBackColor(workbook,headercell1);

			     
			     /* Actual Row Data *//////
			     //////////////////////////////////////////////////
			     
			     long PJPID =0;
					String PJPName="";
					
					long rDistributorID =0;
					String rDistributorName="";
					
					int GrandTotalOutlets=0;
					int GrandAvgDailySchCallTotal=0;
					double GrandTotalScheduledCalls=0;
					double GrandTotalOrdersFromPlannedOutlets=0;
					double GrandOrderProductivity=0;
					double GrandTotalUnplannedCalls=0;
					double GrandTotalOrders=0;
					double GrandTotalTotalQuantitySold=0;
					double GrandDropSize=0;
					double GrandTotalTotalLinesSold=0;
					double GrandSKUPerOrder=0;
					double GrandD_TotalTotalQuantitySold=0;
					double GrandSalesPerDay=0;
					double GrandD_TotalQuantityDispatched=0;
					double GrandD_Returns=0;
					double GrandD_DropSize=0;
					double GrandD_TotalTotalLinesSold=0;
					double GrandSKUPerInvoice=0;
					double GrandZeroSalesCount=0;
					double GrandDD_TotalTotalQuantitySold=0;
					double GrandTotalOrderCount = 0;
					double GrandD_TotalInvoiceCount = 0;
					double Grand_UniqueSKUs = 0;
					double GrandTotalWholeSale = 0;
					double GrandCallCompRate=0;
					
					double GrandTotalActuallSchCalls=0;
					
					double GrandTotalDeskSalesRawCases = 0;
					double GrandTotalSpotSalesRawCases = 0;
					
					
					String AltRowColorGray="#f7f5f5;";			
					
					String AltRowColorBlue="#f6fdfd;";				
					
					String AltRowColorPink="#f8f6f9";
					
					double GrandTotalAvgDailyTarget = 0;
					double GrandTotalAvgDailySale = 0;
					
					
					
					s.executeUpdate("SET SESSION group_concat_max_len = 1000000");
					
					
					
					/*
					 * String WhereSND = " and is_shifted_to_other_plant = 0 and (snd_id=" + SND_ID
					 * + " OR rsm_id=" + SND_ID + " OR tdm_id=" + SND_ID + ") "; if (SND_ID == 0) {
					 * WhereSND = " and  is_shifted_to_other_plant = 0 "; }
					 */
					
					String WhereSND ="";
					if (SND_ID != 0) {
						WhereSND = " and (snd_id=" + SND_ID + " OR rsm_id=" + SND_ID + " OR tdm_id=" + SND_ID + ") ) ";					}
					
				//	System.out.println("SELECT tab1.distributor_id, (SELECT name FROM common_distributors cd WHERE cd.distributor_id = tab1.distributor_id) distributor_name, (SELECT COUNT(DISTINCT outlet_id) FROM distributor_beat_plan dbp JOIN distributor_beat_plan_schedule dbps ON dbp.id = dbps.id WHERE dbp.distributor_id = tab1.distributor_id) outlet_count FROM (SELECT  distributor_id  FROM common_distributors WHERE is_active = 1 AND is_shifted_to_other_plant = 0 "+WhereSND+" AND distributor_id NOT IN (SELECT DISTINCT mo.distributor_id FROM mobile_order mo WHERE mo.created_on BETWEEN "+Utilities.getSQLDate(StartDate)+" AND "+Utilities.getSQLDateNext(EndDate)+")) tab1 ORDER BY distributor_id");
					// ResultSet rs2 = s.executeQuery("select tab1.distributor_id, (select name from common_distributors cd where cd.distributor_id=tab1.distributor_id) distributor_name, (select count(distinct outlet_id) from distributor_beat_plan dbp join distributor_beat_plan_schedule dbps on dbp.id = dbps.id where dbp.distributor_id = tab1.distributor_id) outlet_count from ( select distinct isa.distributor_id from inventory_sales_adjusted isa where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereSND+" ) tab1 order by distributor_id");
					ResultSet rs2 = s.executeQuery("SELECT tab1.distributor_id, (SELECT name FROM common_distributors cd WHERE cd.distributor_id = tab1.distributor_id) distributor_name, (SELECT COUNT(DISTINCT outlet_id) FROM distributor_beat_plan dbp JOIN distributor_beat_plan_schedule dbps ON dbp.id = dbps.id WHERE dbp.distributor_id = tab1.distributor_id) outlet_count FROM (SELECT  distributor_id  FROM common_distributors WHERE is_active = 1 AND is_shifted_to_other_plant = 0 "+WhereSND+" AND distributor_id NOT IN (SELECT DISTINCT mo.distributor_id FROM mobile_order mo WHERE mo.created_on BETWEEN "+Utilities.getSQLDate(StartDate)+" AND "+Utilities.getSQLDateNext(EndDate)+")) tab1 ORDER BY distributor_id");
					while(rs2.next()){
						
						rDistributorID = rs2.getLong("distributor_id");
						rDistributorName = rs2.getString("distributor_name");
						int TotalOutlets = rs2.getInt("outlet_count");
						
						int count_orders = 0;
						
					//	System.out.println("SELECT count(assigned_to) as c FROM distributor_beat_plan_view where distributor_id="+rDistributorID+" and created_on BETWEEN "+Utilities.getSQLDate(StartDate)+" AND "+Utilities.getSQLDateNext(EndDate));
						ResultSet rsCountCheck = s2.executeQuery("SELECT count(assigned_to) as c FROM distributor_beat_plan_view where distributor_id="+rDistributorID+" and created_on BETWEEN "+Utilities.getSQLDate(StartDate)+" AND "+Utilities.getSQLDateNext(EndDate));
						if(rsCountCheck.next()){
							count_orders = rsCountCheck.getInt("c");
						}
				//		System.out.println("TotalOutlets : "+TotalOutlets+"count_orders : "+count_orders);
						
						if(TotalOutlets !=0 && count_orders == 0) {
						GrandTotalOutlets += TotalOutlets;
						
						int AvgDailySchCall=0;
						double ScheduledCalls=0;//rs2.getInt("scheduled_calls");
						long OrderBookerID = 0;////rs2.getInt("assigned_to");
						//String TotalOutletIDs = rs2.getString("total_outlet_ids");
						//System.out.println(TotalOutletIDs);
						String OrderBookerName="";
						/*
						double ZeroSalesCount = 0;
						ResultSet rs50 = s2.executeQuery("select count(*) from (select outlet_id, to_days(date("+Utilities.getSQLDate(EndDate)+"))-to_days(max(created_on)) days from inventory_sales_adjusted where outlet_id in ("+TotalOutletIDs+") group by outlet_id having days > 15) tab1");
						if (rs50.first()){
							ZeroSalesCount = rs50.getDouble(1);
						}
						
						
						GrandZeroSalesCount +=ZeroSalesCount;
						*/
							//select avg(ct) from (select day_number, count(outlet_id) ct from distributor_beat_plan_schedule where id = 55 group by day_number) tab1
						ResultSet rs3 = s2.executeQuery("select avg(ct) avg_daily_sch_call from (select day_number, count(outlet_id) ct from distributor_beat_plan_schedule where id in (select id from distributor_beat_plan where distributor_id = "+rDistributorID+") group by day_number) tab1");
						while(rs3.next()){
							 AvgDailySchCall = rs3.getInt("avg_daily_sch_call");
						}
						
						GrandAvgDailySchCallTotal += AvgDailySchCall;
						
						double TotalUniqueSKUs = 0;
						ResultSet rs121 = s3.executeQuery("select count(distinct product_id) from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and distributor_id = "+rDistributorID);
						if(rs121.first()){
							TotalUniqueSKUs = rs121.getDouble(1);
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
						
						double TotalActuallSchCalls=0;
						
						double D_TotalTargetRawCases = 0;
						
						
						Date CurrentDate = StartDate;
						
						//System.out.println("StartDate2222222: "+Utilities.getDisplayDateFormat(StartDate)+" - "+Utilities.getDisplayDateFormat(EndDate));
						
						
						
						
						
						// Delivery KPI Loop
						///////////////////////////////////////////////////////////////
						
						Date StartDate1=StartDate;
						Date EndDate1=EndDate;
						
						
						
						
						/////////System.out.println("New Start Date after Friday :"+StartDate1);
						/////////System.out.println("End Start Date after Friday :"+EndDate1);
						
						double D_UniqueSKUs = 0;
						ResultSet D_rs51 = s3.executeQuery("select count(distinct isap.product_id) from inventory_sales_adjusted_products isap where isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate1)+" and "+Utilities.getSQLDate(EndDate1)+" and isap.cache_distributor_id = "+rDistributorID);
						if(D_rs51.first()){
							D_UniqueSKUs = D_rs51.getDouble(1);
						}
						
						ResultSet D_rs511 = s3.executeQuery("select count(distinct isap.product_id) from inventory_sales_adjusted_products isap where isap.cache_created_on_date between "+Utilities.getSQLDate(StartDate1)+" and "+Utilities.getSQLDate(EndDate1));
						if(D_rs511.first()){
							Grand_UniqueSKUs = D_rs511.getDouble(1);
						}
						
						
						Date CurrentDate1 = StartDate1;
						int i1 = 0;
						
						double days1 = 0;
						int DayNumberOff = 0;
						int IsSundayOff = Utilities.IsDistributorOFFStatus(rDistributorID);
						if (IsSundayOff == 0) { // Mean Friday Off - Normal Case
							DayNumberOff = 6;
						} else {
							DayNumberOff = 1; // Else Sunday Off
						}
						while(true){
						//	if(Utilities.getDayOfWeekByDate(CurrentDate1)!=6 && !IsGazettedHoliday(CurrentDate1,rDistributorID)){ //if it is not friday
								//System.out.println("Current Date1 : "+Utilities.getDisplayDateFormat(CurrentDate1)+" - End Date: "+Utilities.getDisplayDateFormat(EndDate));
							if (Utilities.getDayOfWeekByDate(CurrentDate) != DayNumberOff
									&& !IsGazettedHoliday(CurrentDate, rDistributorID)) { // if it is not friday
								
								days1++;
								/*double PlannedOutletsCount = 0;
								String PlannedOutletIDs = "";

								ResultSet rs5 = s3.executeQuery("select count(distinct outlet_id), group_concat(distinct outlet_id) from distributor_beat_plan_log where log_date between "+Utilities.getSQLDate(CurrentDate1)+" and "+Utilities.getSQLDate(CurrentDate1)+" and distributor_id = "+rDistributorID+" and day_number = dayofweek(log_date)");
								if (rs5.first()){
									PlannedOutletsCount = rs5.getDouble(1);
									PlannedOutletIDs = rs5.getString(2);
								}*/
								
								//ScheduledCalls += PlannedOutletsCount;
								
								
								
								
								
								
							
								
							
								
								
								/*
								DELIVERY KPIs
								*/
								
								double D_TotalQuantitySold = 0;
								ResultSet D_rs9 = s3.executeQuery("select sum(isap.liquid_in_ml)/6000 total_qty_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between "+Utilities.getSQLDate(CurrentDate1)+" and "+Utilities.getSQLDateNext(CurrentDate1)+" and isa.distributor_id in ("+rDistributorID+")");
								if(D_rs9.first()){
									D_TotalQuantitySold = D_rs9.getDouble("total_qty_sold");
								}
								D_TotalTotalQuantitySold += D_TotalQuantitySold;
								

								double D_WholesaleQuantitySold = 0;
//								ResultSet WD_rs9 = s3.executeQuery("select sum(isap.total_units/cache_units_per_sku) total_qty_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.order_id in ("+OrderIDs+") and isa.outlet_id in (select id from common_outlets where channel_id in (12,13) and cache_distributor_id = "+rDistributorID+" )");
//								if(WD_rs9.first()){
//									D_WholesaleQuantitySold = WD_rs9.getDouble("total_qty_sold");
//								}
								D_TotalWholesaleQuantitySold += D_WholesaleQuantitySold;
								GrandTotalWholeSale += D_WholesaleQuantitySold;
								
								double D_QuantityDispatched = 0;
								ResultSet D_rs1 = s3.executeQuery("select sum(isip.total_units*ipv.liquid_in_ml)/6000 from inventory_sales_invoices isi join inventory_sales_invoices_products isip on isi.id = isip.id join inventory_products_view ipv on isip.product_id = ipv.product_id where  isi.created_on between "+Utilities.getSQLDate(CurrentDate1)+" and "+Utilities.getSQLDateNext(CurrentDate1)+" and isi.distributor_id in ("+rDistributorID+")");
								if (D_rs1.first()){
									D_QuantityDispatched = D_rs1.getDouble(1);
								}
								D_TotalQuantityDispatched += D_QuantityDispatched;
								
								
								
								
								double InvoiceCount = 0;
								
								ResultSet D_rs4 = s3.executeQuery("select count(id) from inventory_sales_adjusted where distributor_id  in ("+rDistributorID+") and created_on between "+Utilities.getSQLDate(CurrentDate1)+" and "+Utilities.getSQLDateNext(CurrentDate1)+"  and net_amount != 0");
								if(D_rs4.first()){
									InvoiceCount = D_rs4.getDouble(1);
								}
								D_TotalInvoiceCount += InvoiceCount;
							
								
								
								
								
								double TargetRawCases = 0;
								//ResultSet D_rs19 = s3.executeQuery("SELECT sum(quantity)/dayofmonth(last_day(date("+Utilities.getSQLDate(CurrentDate1)+"))) FROM pep.distributor_targets dt join distributor_targets_packages dtp on dt.id = dtp.id where month = month("+Utilities.getSQLDate(CurrentDate1)+") and year = year("+Utilities.getSQLDate(CurrentDate1)+") and distributor_id = "+rDistributorID);
//								System.out.println("SELECT sum(etp.quantity*ip.unit_per_case*ip.liquid_in_ml)/6000 FROM pep.employee_targets et join employee_targets_packages etp on et.id = etp.id join inventory_packages ip on etp.package_id=ip.id where month = month("+Utilities.getSQLDate(CurrentDate1)+") and year = year("+Utilities.getSQLDate(CurrentDate1)+") and et.pjp_id in (SELECT distinct id FROM distributor_beat_plan where distributor_id="+rDistributorID+") ");
//								ResultSet D_rs19 = s3.executeQuery("SELECT sum(etp.quantity*ip.unit_per_case*ip.liquid_in_ml)/6000 FROM pep.employee_targets et join employee_targets_packages etp on et.id = etp.id join inventory_packages ip on etp.package_id=ip.id where month = month("+Utilities.getSQLDate(CurrentDate1)+") and year = year("+Utilities.getSQLDate(CurrentDate1)+") and et.pjp_id in (SELECT distinct id FROM distributor_beat_plan where distributor_id="+rDistributorID+") ");
//								if (D_rs19.first()){
//									
//									double WorkingDays = Utilities.getDayNumberByDate(Utilities.getEndDateByDate(CurrentDate1)) -Utilities.getFridayCountByDate(CurrentDate1);
//									
//									TargetRawCases = D_rs19.getDouble(1) / WorkingDays;
//								}
								D_TotalTargetRawCases += TargetRawCases;
								
								
								double D_TotalLinesSold = 0;
								ResultSet D_rs5 = s3.executeQuery("select count(*) total_lines_sold from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.created_on between "+Utilities.getSQLDate(CurrentDate1)+" and "+Utilities.getSQLDateNext(CurrentDate1));
								if(D_rs5.first()){
									D_TotalLinesSold = D_rs5.getDouble("total_lines_sold");
								}
								D_TotalTotalLinesSold += D_TotalLinesSold;
								

								
							}
							
							
							
							if(DateUtils.isSameDay(CurrentDate1, EndDate1)){
								break;
							}
							
							CurrentDate1 = Utilities.getDateByDays(CurrentDate1, 1);
							i1++;
							
						
						}
						
						
						// BOM Target
						double TotalMonthTargetRawCases = 0;
						//System.out.println("SELECT sum(quantity) FROM pep.distributor_targets dt join distributor_targets_packages dtp on dt.id = dtp.id where month = month("+Utilities.getSQLDate(EndDate1)+") and year = year("+Utilities.getSQLDate(EndDate1)+") and distributor_id = "+rDistributorID);
//						ResultSet D_rs191 = s3.executeQuery("SELECT sum(etp.quantity*ip.unit_per_case*ip.liquid_in_ml)/6000 FROM pep.employee_targets et join employee_targets_packages etp on et.id = etp.id join inventory_packages ip on etp.package_id=ip.id where month = month("+Utilities.getSQLDate(EndDate1)+") and year = year("+Utilities.getSQLDate(EndDate1)+") and et.pjp_id in (SELECT distinct id FROM distributor_beat_plan where distributor_id= "+rDistributorID+")");
//						if (D_rs191.first()){
//							TotalMonthTargetRawCases = D_rs191.getDouble(1);
//						}
						
						double TotalMonthSalesRawCases = 0;
						//System.out.println("select sum(total_units/cache_units_per_sku) from inventory_sales_adjusted_products where cache_created_on_date between "+Utilities.getSQLDate(Utilities.getStartDateByDate(EndDate1))+" and "+Utilities.getSQLDate(Utilities.getEndDateByDate(EndDate1))+" and cache_distributor_id = "+rDistributorID);
						//ResultSet D_rs192 = s3.executeQuery("select sum(total_units/cache_units_per_sku) from inventory_sales_adjusted_products where cache_created_on_date between "+Utilities.getSQLDate(Utilities.getStartDateByDate(EndDate1))+" and "+Utilities.getSQLDateNext(EndDate1)+" and cache_distributor_id = "+rDistributorID+"");
						
						ResultSet D_rs192 = s3.executeQuery("select sum(liquid_in_ml)/6000 from inventory_sales_adjusted_products where cache_created_on_date between "+Utilities.getSQLDate(Utilities.getStartDateByDate(EndDate1))+" and "+Utilities.getSQLDateNext(EndDate1)+" and cache_distributor_id = "+rDistributorID+"");
						if (D_rs192.first()){
							TotalMonthSalesRawCases = D_rs192.getDouble(1);
						}
						
						double TotalDeskSalesRawCases = 0;
						ResultSet D_rs193 = s3.executeQuery("SELECT sum(isap.liquid_in_ml)/6000 FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.type_id in(1,4) and isa.created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isa.distributor_id = "+rDistributorID+"");
						if (D_rs193.first()){
							TotalDeskSalesRawCases = D_rs193.getDouble(1);
						}
						GrandTotalDeskSalesRawCases += TotalDeskSalesRawCases;

						double TotalSpotSalesRawCases = 0;
						ResultSet D_rs194 = s3.executeQuery("SELECT sum(isap.liquid_in_ml)/6000 FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id where isa.type_id = 2 and isa.created_on_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and isa.distributor_id = "+rDistributorID+"");
						if (D_rs194.first()){
							TotalSpotSalesRawCases = D_rs194.getDouble(1);
						}
						GrandTotalSpotSalesRawCases += TotalSpotSalesRawCases;
						
						
						
						int DaysInMonth = Utilities.getDayNumberByDate(Utilities.getEndDateByDate(EndDate1));
						int CurrentDay = Utilities.getDayNumberByDate(EndDate1);
						int RemainingDays = (DaysInMonth-CurrentDay)-Utilities.getSubsequentFridayCountByDate(EndDate1);
						
						
						double RemainingTarget = TotalMonthTargetRawCases - TotalMonthSalesRawCases;
						
						//System.out.println(TotalMonthTargetRawCases +" "+TotalMonthSalesRawCases+" "+RemainingDays);
						
						//System.out.println("RT:"+RemainingTarget+" MT:"+TotalMonthTargetRawCases+" MS:"+TotalMonthSalesRawCases);
						
						
						double BOMDailyTarget = 0;
						
						if (RemainingDays != 0 && RemainingTarget > 0){
							BOMDailyTarget = RemainingTarget / RemainingDays;
						}
						
						
						/////////////////////////////////////////////////////
						/////////////////////////////////////////////////////
						
						
						
						
						double OrderProductivity = 0;
						if (ScheduledCalls != 0){
							OrderProductivity = (TotalOrdersFromPlannedOutlets / ScheduledCalls);
						}
						
						double CallCompRate = 0;
						if (ScheduledCalls != 0){
							CallCompRate = (TotalActuallSchCalls / ScheduledCalls);
						}
						
						
						double AverageDailyTarget = 0;
						if (days1 != 0){
							AverageDailyTarget = D_TotalTargetRawCases / days1;
						}
						
						double UnplannedCallsPercentage = 0;
						if (ScheduledCalls != 0){
							UnplannedCallsPercentage = (TotalUnplannedCalls / ScheduledCalls);
						}
						
						double TotalOrders = TotalOrdersFromPlannedOutlets + TotalUnplannedCalls;
						
						GrandTotalOrders +=TotalOrders;
						
						double DropSize = 0;
						if (TotalOrderCount != 0){
							 DropSize = TotalTotalQuantitySold / TotalOrderCount;
						}
						GrandTotalOrderCount += TotalOrderCount;
						
						
						//GrandDropSize +=DropSize;
						
						double D_DropSize = 0;
						if (D_TotalInvoiceCount != 0){
							D_DropSize = D_TotalTotalQuantitySold / D_TotalInvoiceCount;
						}
						
						GrandD_TotalInvoiceCount += D_TotalInvoiceCount;
						
						GrandD_DropSize +=D_DropSize;

						if (PJPID == 55){
						//	System.out.println(TotalTotalQuantitySold+" "+D_TotalTotalQuantitySold);
						//	System.out.println(TotalOrderCount+" "+D_TotalInvoiceCount);
						}
						
						double SKUPerOrder = 0;
						if (TotalOrderCount != 0){
							SKUPerOrder = TotalTotalLinesSold / TotalOrderCount;
						}
						
						GrandSKUPerOrder +=SKUPerOrder; 
						
						double SKUPerInvoice = 0;
						if (D_TotalInvoiceCount != 0){
							SKUPerInvoice = D_TotalTotalLinesSold / D_TotalInvoiceCount;
						}
						
						GrandSKUPerInvoice +=SKUPerInvoice;
						
						
					//	System.out.println("Dayssss 1 "+days1);
						
						
						double SalesPerDay = 0;
						if (days1 != 0){
							SalesPerDay = D_TotalTotalQuantitySold / days1;
						}
						
						GrandSalesPerDay += SalesPerDay;
						
						double DeliveryOrderPercentage = 0;
						if (DD_TotalTotalQuantitySold != 0){
							DeliveryOrderPercentage = (D_TotalTotalQuantitySold / DD_TotalTotalQuantitySold);
						}
						
						
						double D_Returns = D_TotalQuantityDispatched - D_TotalTotalQuantitySold;
						
						GrandD_Returns +=D_Returns;
						
						double D_ReturnsPercentage = 0;
						if (D_TotalQuantityDispatched != 0){
							D_ReturnsPercentage = (D_Returns / D_TotalQuantityDispatched);
						}
						
						GrandTotalScheduledCalls +=ScheduledCalls;
						GrandTotalOrdersFromPlannedOutlets += TotalOrdersFromPlannedOutlets;
						GrandTotalUnplannedCalls += TotalUnplannedCalls;
						GrandTotalTotalQuantitySold +=TotalTotalQuantitySold;
						GrandTotalTotalLinesSold +=TotalTotalLinesSold;
						GrandD_TotalTotalQuantitySold+=D_TotalTotalQuantitySold;
						GrandD_TotalQuantityDispatched +=D_TotalQuantityDispatched;
						GrandDD_TotalTotalQuantitySold +=DD_TotalTotalQuantitySold;
						GrandD_TotalTotalLinesSold+=D_TotalTotalLinesSold;
					
						
						GrandTotalActuallSchCalls +=TotalActuallSchCalls;
						
						XSSFRow headerrow2 = spreadsheet.createRow((short) RowCount+1);        
					    XSSFCell headercell2 = (XSSFCell) headerrow2.createCell((short) 0);
					
					
					    headercell2 = (XSSFCell) headerrow2.createCell((short) 0);
						headercell2.setCellValue(rDistributorID+" - "+Utilities.truncateStringToMax(rDistributorName, 22));
					    SetNormalCellBackColorLeftAltGray(workbook,headercell2);
					    
					    
					    headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
					    headercell2.setCellValue(TotalOutlets);
					    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					    SetNormalCellBackColor(workbook,headercell2);

					    headercell2 = (XSSFCell) headerrow2.createCell((short) 2);
					    headercell2.setCellValue(AvgDailySchCall);
					    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					    SetNormalCellBackColorGray(workbook,headercell2);
					    
					    
					    
					    
					    
					    headercell2 = (XSSFCell) headerrow2.createCell((short) 3);
					    
//					   
					    	headercell2.setCellValue(D_TotalTotalQuantitySold);
						    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
						    SetNormalCellBackColor(workbook,headercell2);
//					   
					    
					    
					    headercell2 = (XSSFCell) headerrow2.createCell((short) 4);
					    headercell2.setCellValue(AverageDailyTarget);
					    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					    SetNormalCellBackColorGray(workbook,headercell2);
					    
					    
					    headercell2 = (XSSFCell) headerrow2.createCell((short) 5);
					    headercell2.setCellValue(SalesPerDay);
					    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					    SetNormalCellBackColor(workbook,headercell2);					    
					    
					 
					    
					    //Order
					    
					    headercell2 = (XSSFCell) headerrow2.createCell((short) 6);
					    headercell2.setCellValue(BOMDailyTarget);
					    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					    SetNormalCellBackColorBlue(workbook,headercell2);
					    
					    
					    headercell2 = (XSSFCell) headerrow2.createCell((short) 7);
					    headercell2.setCellValue(D_TotalQuantityDispatched);
					    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					    SetNormalCellBackColor(workbook,headercell2);
					    
					   
					    
					    headercell2 = (XSSFCell) headerrow2.createCell((short) 8);
					    headercell2.setCellValue(D_Returns );
					    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					    SetNormalCellBackColorBlue(workbook,headercell2);
					    
					    headercell2 = (XSSFCell) headerrow2.createCell((short) 9);
					    headercell2.setCellValue(D_DropSize );
					    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					    SetNormalCellBackColor(workbook,headercell2);
					    
					    
					    
					    headercell2 = (XSSFCell) headerrow2.createCell((short) 10);
					    headercell2.setCellValue(TotalUniqueSKUs);
					    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					    SetNormalCellBackColor(workbook,headercell2);
					    
					   
					    
					    
					    
					    headercell2 = (XSSFCell) headerrow2.createCell((short) 11);
					    headercell2.setCellValue(Utilities.getDisplayCurrencyFormatOneDecimal(SKUPerInvoice));
					    //headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					    SetNormalCellBackColorPinkOneDecimal(workbook,headercell2);
					    
					   
					    
					    double TotalWholeSalePercent = 0;
						if ((D_TotalTotalQuantitySold+TotalDeskSalesRawCases+TotalSpotSalesRawCases) != 0){
							TotalWholeSalePercent = (TotalDeskSalesRawCases / (D_TotalTotalQuantitySold+TotalDeskSalesRawCases+TotalSpotSalesRawCases));
						}
					    
					    headercell2 = (XSSFCell) headerrow2.createCell((short) 12);
					    headercell2.setCellValue(TotalWholeSalePercent);
					 
					    SetNormalCellBackColorPercentPink(workbook,headercell2);
					    
					    headercell2 = (XSSFCell) headerrow2.createCell((short) 13);
					    headercell2.setCellValue(TotalDeskSalesRawCases);
					    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					    SetNormalCellBackColor(workbook,headercell2);
					    
					    headercell2 = (XSSFCell) headerrow2.createCell((short) 14);
					    headercell2.setCellValue(TotalSpotSalesRawCases);
					    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					    SetNormalCellBackColor(workbook,headercell2);
					    
					    
					    
					   

					    					    
						
					
					RowCount++;
					}
					
			     
					  for(int i=0;i<15;i++){
					    	//System.out.println("Auto Sizing - "+i);
					    	try{
					    		spreadsheet.autoSizeColumn(i);
					    	}catch(Exception e){System.out.println(i);e.printStackTrace();}
					    }
							     
					   
			     
			    
		      
					} // outlet !=0 conditin
		      
			
			 //////////////////////////////////////////////////////////////////////
	}
	
	
	//-----------------------------------------------------------------------------------------------------//

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
