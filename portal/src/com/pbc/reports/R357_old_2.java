package com.pbc.reports;

import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

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

public class R357_old_2 {

	public static final String RESULT = Utilities.getEmailAttachmentsPath() + "/CashInflowSummary.pdf";

	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;

	public void createPdf(String filename, Date StartDate, Date EndDate, String Distributors, String Brands,
			String OrderBookers, String SKUIDs, String ASMs, String TSOs, String Regions, String City)
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
				3 // last column (0-based)
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

		String[] columnNames = { "Serial #", "Distributor ID", "Distributor Name", "Order Date", "PSR ID", "PSR Name",
				"City", "Region", "TSO Name", "ASM Name", "Outlet Name", "Orders within 25m", "Orders within 50m",
				"Orders within 100m", "Orders within 200m", "Orders greater than 200m",
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

		String whereOrderBooker = "";
		if (OrderBookers.length() > 0) {
			whereOrderBooker = "and created_by in(" + OrderBookers + ")";
		}

		String whereRegions = (Regions.length() > 0) ? " and region_id in (" + Regions + ")" : "";

		String whereASMs = (ASMs.length() > 0) ? " and rsm_id in (" + ASMs + ")" : "";
		String whereTSOs = (TSOs.length() > 0) ? " and asm_id in (" + TSOs + ")" : "";

		String whereCities = "";
		if (City.length() > 0) {
			int c = 0;
			String ids = "";
			ResultSet rsCity = s
					.executeQuery("select distributor_id from common_distributors where city_id in (" + City + ");");
			while (rsCity.next()) {
				ids = (c == 0) ? ids + rsCity.getString("distributor_id")
						: ids + "," + rsCity.getString("distributor_id");
				c++;
			}
			whereCities = " and distributor_id in (" + ids + ")";
		}

		String whereBrands = "";
		if (Brands.length() > 0) {
			int c = 0;
			String ids = "";
//			System.out.println(
//			"SELECT DISTINCT mop.id as id FROM mobile_order mo JOIN mobile_order_products mop ON mo.id = mop.id WHERE product_id IN (SELECT product_id FROM inventory_products_view WHERE lrb_type_id IN ("
//			+ Brands + ")) and distributor_id in ("+ Distributors +") AND created_on BETWEEN " + Utilities.getSQLDate(StartDate) + " AND "
//			+ Utilities.getSQLDateNext(EndDate) + whereOrderBooker + whereCities + whereRegions + whereTSOs + whereASMs);
			ResultSet rsBrands = s.executeQuery(
					"SELECT DISTINCT mop.id as id FROM mobile_order mo JOIN mobile_order_products mop ON mo.id = mop.id WHERE product_id IN (SELECT product_id FROM inventory_products_view WHERE lrb_type_id IN ("
							+ Brands + ")) and distributor_id in (" + Distributors + ") AND created_on BETWEEN "
							+ Utilities.getSQLDate(StartDate) + " AND " + Utilities.getSQLDateNext(EndDate)
							+ whereOrderBooker + whereCities + whereRegions + whereTSOs + whereASMs);
			while (rsBrands.next()) {
				ids = (c == 0) ? ids + rsBrands.getString("id") : ids + "," + rsBrands.getString("id");
				c++;
			}
			whereBrands = (ids.length() > 0) ? " and mo.id in (" + ids + ")" : "";
		}

		String whereSKUs = "";
		if (SKUIDs.length() > 0) {
			int c = 0;
			String ids = "";
//			System.out.println(
//					"SELECT DISTINCT mop.id as id FROM mobile_order mo JOIN mobile_order_products mop ON mo.id = mop.id WHERE product_id IN ("
//							+ SKUIDs + ") and distributor_id in ("+ Distributors +") AND created_on BETWEEN " + Utilities.getSQLDate(StartDate) + " AND "
//							+ Utilities.getSQLDateNext(EndDate) + whereOrderBooker + whereCities + whereRegions + whereTSOs + whereASMs);
			ResultSet rsSKUs = s.executeQuery(
					"SELECT DISTINCT mop.id as id FROM mobile_order mo JOIN mobile_order_products mop ON mo.id = mop.id WHERE product_id IN ("
							+ SKUIDs + ") and distributor_id in (" + Distributors + ") AND created_on BETWEEN "
							+ Utilities.getSQLDate(StartDate) + " AND " + Utilities.getSQLDateNext(EndDate)
							+ whereOrderBooker + whereCities + whereRegions + whereTSOs + whereASMs);
			while (rsSKUs.next()) {
				ids = (c == 0) ? ids + rsSKUs.getString("id") : ids + "," + rsSKUs.getString("id");
				c++;
			}
			whereSKUs = (ids.length() > 0) ? " and mo.id in (" + ids + ")" : "";
		}

		int[] array = new int[8];
		int serialNumber = 1;
		String queryTime = " union SELECT 2 as order_nature,lat, lng, outlet_id,mo.created_on,mo.created_by as created_by ,  (select DISPLAY_NAME from users u where u.id=mo.created_by) as psr_name,  (select name from common_distributors cd where cd.distributor_id=mo.distributor_id) as Dis_name,(select city from common_distributors cd where cd.distributor_id=mo.distributor_id) as city,mo.distributor_id as distributor_id,(select region_name from common_regions cr where cr.region_id=mo.region_id) as region ,(select DISPLAY_NAME from users u where u.id=mo.asm_id) as tso_name,(select DISPLAY_NAME from users u where u.id=mo.rsm_id) as asm_name ,(select lat from common_outlets co where co.id=outlet_id) as outlet_lat,(select lng from common_outlets co where co.id=outlet_id) as outlet_lng,(select name from common_outlets co where co.id=outlet_id) as outlet_name, '' as comments from common_outlets_visit_duration mo where  distributor_id in ("
				+ Distributors + ") and mo.visit_type=1 and mobile_timestamp between " + Utilities.getSQLDate(StartDate)
				+ " and " + Utilities.getSQLDateNext(EndDate) + whereOrderBooker + whereCities + whereRegions
				+ whereTSOs + whereASMs
				+ " and outlet_id not in (SELECT outlet_id FROM mobile_order where mobile_timestamp between "
				+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
				+ ") and outlet_id not in ( SELECT outlet_id FROM mobile_order_zero where mobile_timestamp between "
				+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) + " )";

		System.out.println(
				"SELECT 1 as order_nature,lat, lng, outlet_id,mo.created_on,mo.created_by as created_by ,  (select DISPLAY_NAME from users u where u.id=mo.created_by) as psr_name,  (select name from common_distributors cd where cd.distributor_id=mo.distributor_id) as Dis_name,(select city from common_distributors cd where cd.distributor_id=mo.distributor_id) as city,mo.distributor_id as distributor_id,(select region_name from common_regions cr where cr.region_id=mo.region_id) as region ,(select DISPLAY_NAME from users u where u.id=mo.asm_id) as tso_name,(select DISPLAY_NAME from users u where u.id=mo.rsm_id) as asm_name ,(select lat from common_outlets co where co.id=outlet_id) as outlet_lat,(select lng from common_outlets co where co.id=outlet_id) as outlet_lng,(select name from common_outlets co where co.id=outlet_id) as outlet_name, '' as comments FROM mobile_order mo  where distributor_id in ("
						+ Distributors + ") and mobile_timestamp between " + Utilities.getSQLDate(StartDate) + " and "
						+ Utilities.getSQLDateNext(EndDate) + whereOrderBooker + whereCities + whereRegions + whereTSOs
						+ whereASMs + whereBrands + whereSKUs
						+ " UNION SELECT 0 as order_nature,lat, lng, outlet_id,mo.created_on, mo.created_by as created_by, (select DISPLAY_NAME from users u where u.id=mo.created_by) as psr_name,  (select name from common_distributors cd where cd.distributor_id=mo.distributor_id) as Dis_name,(select city from common_distributors cd where cd.distributor_id=mo.distributor_id) as city,mo.distributor_id as distributor_id,(select region_name from common_regions cr where cr.region_id=mo.region_id) as region ,(select DISPLAY_NAME from users u where u.id=mo.asm_id) as tso_name,(select DISPLAY_NAME from users u where u.id=mo.rsm_id) as asm_name ,(select lat from common_outlets co where co.id=outlet_id) as outlet_lat,(select lng from common_outlets co where co.id=outlet_id) as outlet_lng,(select name from common_outlets co where co.id=outlet_id) as outlet_name,(select label from mobile_order_no_order_reason_type where id=mo.no_order_reason_type_v2) as comments  FROM mobile_order_zero mo  where distributor_id in ("
						+ Distributors + ") and is_time_calculate=0 and mobile_timestamp between "
						+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
						+ whereOrderBooker + whereCities + whereRegions + whereTSOs + whereASMs + whereBrands
						+ whereSKUs + queryTime);

		ResultSet rs2 = s.executeQuery(
				"SELECT 1 as order_nature,lat, lng, outlet_id,mo.created_on,mo.created_by as created_by ,  (select DISPLAY_NAME from users u where u.id=mo.created_by) as psr_name,  (select name from common_distributors cd where cd.distributor_id=mo.distributor_id) as Dis_name,(select city from common_distributors cd where cd.distributor_id=mo.distributor_id) as city,mo.distributor_id as distributor_id,(select region_name from common_regions cr where cr.region_id=mo.region_id) as region ,(select DISPLAY_NAME from users u where u.id=mo.asm_id) as tso_name,(select DISPLAY_NAME from users u where u.id=mo.rsm_id) as asm_name ,(select lat from common_outlets co where co.id=outlet_id) as outlet_lat,(select lng from common_outlets co where co.id=outlet_id) as outlet_lng,(select name from common_outlets co where co.id=outlet_id) as outlet_name, '' as comments FROM mobile_order mo  where distributor_id in ("
						+ Distributors + ") and mobile_timestamp between " + Utilities.getSQLDate(StartDate) + " and "
						+ Utilities.getSQLDateNext(EndDate) + whereOrderBooker + whereCities + whereRegions + whereTSOs
						+ whereASMs + whereBrands + whereSKUs
						+ " UNION SELECT 0 as order_nature,lat, lng, outlet_id,mo.created_on, mo.created_by as created_by, (select DISPLAY_NAME from users u where u.id=mo.created_by) as psr_name,  (select name from common_distributors cd where cd.distributor_id=mo.distributor_id) as Dis_name,(select city from common_distributors cd where cd.distributor_id=mo.distributor_id) as city,mo.distributor_id as distributor_id,(select region_name from common_regions cr where cr.region_id=mo.region_id) as region ,(select DISPLAY_NAME from users u where u.id=mo.asm_id) as tso_name,(select DISPLAY_NAME from users u where u.id=mo.rsm_id) as asm_name ,(select lat from common_outlets co where co.id=outlet_id) as outlet_lat,(select lng from common_outlets co where co.id=outlet_id) as outlet_lng,(select name from common_outlets co where co.id=outlet_id) as outlet_name,(select label from mobile_order_no_order_reason_type where id=mo.no_order_reason_type_v2) as comments  FROM mobile_order_zero mo  where distributor_id in ("
						+ Distributors + ") and is_time_calculate=0 and mobile_timestamp between "
						+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
						+ whereOrderBooker + whereCities + whereRegions + whereTSOs + whereASMs + whereBrands
						+ whereSKUs + queryTime);
		while (rs2.next()) {
			
			int total = 0;
			String Outlet_name = "";
			boolean dataexist = false;
			
			String psrName = "";
			String city = "";
			String region = "";
			String tsoName = "";
			String asmName = "";
			Date Order_Date = null;
			Date Start_time = null;
			Date End_time = null;

			dataexist = true;
			double lat1 = rs2.getDouble("lat");
			double lng1 = rs2.getDouble("lng");
			String outletid = rs2.getString("outlet_id");
			psrName = rs2.getString("psr_name");
			city = rs2.getString("city");
			region = rs2.getString("region");
			tsoName = rs2.getString("tso_name");
			asmName = rs2.getString("asm_name");
			double lat2 = rs2.getDouble("outlet_lat");
			double lng2 = rs2.getDouble("outlet_lng");
			Outlet_name = rs2.getString("outlet_name");
			Order_Date = rs2.getTimestamp("created_on");
			long PSRID = rs2.getLong("created_by");
			long DistID = rs2.getLong("distributor_id");
			String Dis_Name = rs2.getString("Dis_name");
			String comment = rs2.getString("comments");
			int order_nature = rs2.getInt("order_nature");

//			System.out.println(
//					"select count(id) from "+((order_nature == 1) ? "mobile_order" : "mobile_order_sm_zero")+" where outlet_id=" + outletid + " and created_on between "
//							+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) +" and distributor_id in ("+Distributors+")");
			ResultSet rs4 = s2.executeQuery("select count(id) from "
					+ ((order_nature == 1) ? "mobile_order" : "mobile_order_zero") + " where outlet_id=" + outletid
					+ " and created_on between " + Utilities.getSQLDate(StartDate) + " and "
					+ Utilities.getSQLDateNext(EndDate) + " and distributor_id in (" + DistID + ")");
			if (rs4.first()) {
				total = rs4.getInt("count(id)");
			}

			// System.out.println("select mobile_timestamp from
			// common_outlets_visit_duration where visit_type=1 and outlet_id="+outletid+"
			// and mobile_timestamp between " + Utilities.getSQLDate(StartDate) + " and " +
			// Utilities.getSQLDateNext(EndDate)+ " and created_by="+PSRID);
			ResultSet rs5 = s2.executeQuery(
					"select mobile_timestamp from common_outlets_visit_duration where visit_type=1 and outlet_id="
							+ outletid + " and mobile_timestamp between  " + Utilities.getSQLDate(StartDate) + " and "
							+ Utilities.getSQLDateNext(EndDate) + " and created_by=" + PSRID);
			if (rs5.first()) {
				Start_time = rs5.getTimestamp("mobile_timestamp");
			}

			// System.out.println("select mobile_timestamp from
			// common_outlets_visit_duration where visit_type=2 and outlet_id="+outletid+"
			// and mobile_timestamp between " + Utilities.getSQLDate(StartDate) + " and " +
			// Utilities.getSQLDateNext(EndDate)+ " and created_by="+PSRID);
			ResultSet rs6 = s2.executeQuery(
					"select mobile_timestamp from common_outlets_visit_duration where visit_type=2 and outlet_id="
							+ outletid + " and mobile_timestamp between  " + Utilities.getSQLDate(StartDate) + " and "
							+ Utilities.getSQLDateNext(EndDate) + " and created_by=" + PSRID);
			if (rs6.first()) {
				End_time = rs6.getTimestamp("mobile_timestamp");
			}

			array = new int[8];
			long Shop_Time_milliseconds = (Start_time != null && End_time != null)
					? DateUtils.getTimeDifference(Start_time, End_time)
					: 0;
			// System.out.println(Shop_Time_milliseconds);
//			/long Shop_Time = (Shop_Time_milliseconds / 60000);
			long Shop_Time = (Shop_Time_milliseconds / 1000);
			// Shop_Time= (Shop_Time!=0) ? Shop_Time+1 : Shop_Time+0;

			/* Journey time */
			Date StartJourney = null;
			Date EndJourney = null;

			ResultSet rsStartJourney = s2.executeQuery(
					"SELECT mobile_timestamp FROM pep.common_outlets_visit_duration where  mobile_timestamp between "
							+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
							+ " and created_by=" + PSRID + " and visit_type=1 order by id limit 1");
			if (rsStartJourney.first()) {
				StartJourney = rsStartJourney.getTimestamp("mobile_timestamp");
				// System.out.println(StartJourney);
			}

			ResultSet rsEndJourney = s2.executeQuery(
					"SELECT mobile_timestamp FROM pep.common_outlets_visit_duration where  mobile_timestamp between "
							+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
							+ " and created_by=" + PSRID + " and visit_type=2 order by id desc limit 1");
			if (rsEndJourney.first()) {
				EndJourney = rsEndJourney.getTimestamp("mobile_timestamp");
				// System.out.println(EndJourney);
			}

			long Journey_Time_milliseconds = (StartJourney != null && EndJourney != null)
					? DateUtils.getTimeDifference(StartJourney, EndJourney)
					: 0;
			long Journey_Time = (Journey_Time_milliseconds / 60000);
			// Journey_Time= (Journey_Time!=0) ? Journey_Time+1 : Journey_Time+0;

			double distance = HaversineDistanceCalculator.calculateHaversineDistance(lat1, lng1, lat2, lng2);
//				System.out.println("distance===== " +"  Outlet_name    " +distance);
			if (distance <= 25) {
				array[0]++;
			} else if (distance > 25 && distance <= 50) {
				array[1]++;
			} else if (distance > 50 && distance <= 100) {
				array[2]++;
			} else if (distance > 100 && distance <= 200) {
				array[3]++;
			} else if (distance > 200) {
				array[4]++;
			} else {
				array[5]++;
			}
			// A new outlet ID is encountered, so we create a new row

			RowCount++;
			Row = spreadsheet.createRow((int) RowCount);
			Col = 0;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(serialNumber);
			Cell.setCellStyle(styleLeft);
			Col++;
			serialNumber++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(DistID);
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Dis_Name);
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Utilities.getDisplayDateFormat(Order_Date));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(PSRID);
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(psrName);
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(city);
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(region);
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(tsoName);
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(asmName);
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(outletid + "-" + Outlet_name);
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(array[0]);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(array[1]);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(array[2]);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(array[3]);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(array[4]);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(array[5]);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Utilities.getDisplayCurrencyFormatOneDecimal(distance));
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(total);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Utilities.getDisplayDateTimeFormatUniversal(Start_time));
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Utilities.getDisplayDateTimeFormatUniversal(End_time));
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Shop_Time);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Journey_Time);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(comment);
			Cell.setCellStyle(styleRight);
			Col++;

			// Initialize data for the new outlet ID
		}

		/*******************************
		 * Row Dynamic End
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
