package com.pbc.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.pbc.util.Datasource;
import com.pbc.util.ExcelColorsInHexa;
import com.pbc.util.ExcelFontAlign;
import com.pbc.util.ExcelFontColors;
import com.pbc.util.POIExcelUtils;
import com.pbc.util.Utilities;

public class R373Excel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath() + "/CashInflowSummary.pdf";

	Datasource ds = new Datasource();
	Statement s;
	Statement s2;

	Date StartDate = Utilities.getDateByDays(0); // Utilities.parseDate("13/02/2016");
	Date EndDate = Utilities.getDateByDays(0);// Utilities.parseDate("13/02/2016");

	/// Date StartDate = Utilities.parseDate("29/08/2016");
	/// Date EndDate = Utilities.parseDate("29/08/2016");

	Date Yesterday = Utilities.getDateByDays(-1);

	long SND_ID = 0;

	public static void main(String[] args) throws Exception {

	}

	public R373Excel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6) {
			Yesterday = Utilities.getDateByDays(-2);
		}

	}

	public void createPdf(String filename, Date StartDate, Date EndDate, String Distributors, String Regions,
			String Channels, String CityIDs, String OrderBookersIDs, String ASMIDs, String TSOIDs ) throws IOException, SQLException, ParseException {

		String whereregion = (Regions.length() > 0) ? (" and distributor_id in (select distributor_id from common_distributors where region_id in ("+Regions+") )") : "";

		String wherecities = (CityIDs.length() > 0) ? (" and city_id in ("+ CityIDs + ")") : "";
		
		String whereChannels = (Channels.length() > 0) ? (" and pic_channel_id in ("+ Channels + ")") : "";

		String whereOrderBookersIDs = (OrderBookersIDs.length() > 0) ? (" and created_by in ("+ OrderBookersIDs + ")") : "";

		String whereasmids = (ASMIDs.length() > 0) ? (" and distributor_id in (select distributor_id from common_distributors where rsm_id in ("+ASMIDs+") )") : "";
		
		String wheretsoids = (TSOIDs.length() > 0) ? (" and distributor_id in (select distributor_id from distributor_beat_plan_view where asm_id in ("+TSOIDs+") )") : "";

		
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
		XSSFCell Cell = (XSSFCell) Row.createCell((short) 0);

		// Report Heading

		/*******************************
		 * ROW 1 Start
		 *******************************/
		Cell = (XSSFCell) Row.createCell((short) 0);
		Cell.setCellValue("R371 - Post Sales Return");
		spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, // first row (0-based)
				RowCount, // last row (0-based)
				0, // first column (0-based)
				2 // last column (0-based)
		));

		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);// set it to bold
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getWhite(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getLeft());

		Cell = (XSSFCell) Row.createCell((short) 1);
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getWhite(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getLeft());

		Cell = (XSSFCell) Row.createCell((short) 2);
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
				2 // last column (0-based)
		));

		Cell = (XSSFCell) Row.createCell((short) 1);
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getWhite(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getLeft());

		Cell = (XSSFCell) Row.createCell((short) 2);
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getWhite(), style, font, excelFontColors.getBlack(),
				true, true, fontAlign.getLeft());

		/*******************************
		 * ROW 2 End
		 *******************************/

		/***************************** Data ***************************************/

		String[] columnNames = { "Sr No", "PJP ID", "PSR ID", "PSR Name", "Distribution ID", "Distribution Name",
				"Outlet ID", "Outlet Name", "Address", "Created On", "Lat", "Lng", "Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday", "Saturday", "Sunday", "Channel" };

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
		 * Row Dynamic Start
		 ********************************/

//		System.out.println(
//				"SELECT  r.created_on, r.distributor_id, (select name from common_distributors cd where cd.distributor_id=r.distributor_id) as distributor_name, r.outlet_id, (select region_name from common_regions cr where cr.region_id=r.region_id) as region, type_id ,p.raw_cases,  p.net_amount, p.product_id FROM inventory_sales_returns_products p JOIN inventory_sales_returns r ON p.id = r.id WHERE r.created_on BETWEEN "
//						+ Utilities.getSQLDate(StartDate) + " AND " + Utilities.getSQLDateNext(EndDate)
//						+ " AND r.distributor_id in (" + Distributors + ")   " + whereregion + whereBrand + "  "
//						+ whereproduct + " " + wherecities + "  " + wherechannel + "");

		ResultSet rsData = s.executeQuery(
				"select mobile_transaction_no,cache_beat_plan_id, distributor_id,(select name from common_distributors cd where cd.distributor_id=co.distributor_id) as distributor_name,id,name,address,created_on,lat,lng,(select label from pci_sub_channel where id=co.pic_channel_id) as channel from common_outlets co where  created_on between "
						+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
						+ " and mobile_transaction_no is not null" + whereregion + wherecities + whereChannels + whereOrderBookersIDs + whereasmids + wheretsoids);

		int count = 1;
		int RowCount1 = 0; // Ensure you have this initialized somewhere

		while (rsData.next()) {

			int Cache_Beat_Plan_Id = rsData.getInt("cache_beat_plan_id");
			int Created_By = 0;
			String PSR_Name = "";
			int Distributor_Id = rsData.getInt("distributor_id");
			String Distributor_Name = rsData.getString("distributor_name");
			int ID = rsData.getInt("id");
			String Name = rsData.getString("name");
			String Address = rsData.getString("address");
			String Created_on = Utilities.getDisplayDateFormat(rsData.getTimestamp("created_on"));
			double Lat = rsData.getDouble("lat");
			double Long = rsData.getDouble("lng");
			String Channel = rsData.getString("channel");
			String mobile_transaction_no = rsData.getString("mobile_transaction_no");
			
			ResultSet rsPSR = s2.executeQuery("select request_by,(select DISPLAY_NAME from users where id=request_by) as psr_name from common_outlets_request where mobile_transaction_no='"+mobile_transaction_no+"'");
			if(rsPSR.first()) {
				Created_By = rsPSR.getInt("request_by");
				 PSR_Name = rsPSR.getString("psr_name");
			}
			
			

			int[] result = new int[7];
			Arrays.fill(result, 0);
			ResultSet rsData1 = s2.executeQuery("SELECT day_number FROM distributor_beat_plan_schedule WHERE outlet_id="
					+ ID + " AND id=" + Cache_Beat_Plan_Id);

			while (rsData1.next()) {
				switch (rsData1.getInt("day_number")) {
				case 1:
					result[6] = 1;
					break;
				case 2:
					result[0] = 1;
					break;
				case 3:
					result[1] = 1;
					break;
				case 4:
					result[2] = 1;
					break;
				case 5:
					result[3] = 1;
					break;
				case 6:
					result[4] = 1;
					break;
				case 7:
					result[5] = 1;
					break;
				}
			}

			RowCount++;
			Row = spreadsheet.createRow((short) RowCount);

			Col = 0;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(count);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Cache_Beat_Plan_Id);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Created_By);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(PSR_Name);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Distributor_Id);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Distributor_Name);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(ID);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Name);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Address);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Created_on);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Lat);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Long);
			Cell.setCellStyle(styleRight);
			Col++;

			// Add day numbers or zeroes to the respective columns
			for (int i = 0; i < result.length; i++) {
				Cell = Row.createCell(Col);
				Cell.setCellValue(result[i]);
				Cell.setCellStyle(styleRight);
				Col++;
			}

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Channel);
			Cell.setCellStyle(styleRight);
			Col++;

			count++;

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