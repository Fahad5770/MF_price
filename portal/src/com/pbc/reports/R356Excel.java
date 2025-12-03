package com.pbc.reports;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

import java.io.File;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.pbc.util.ExcelColorsInHexa;
import com.pbc.util.ExcelFontAlign;
import com.pbc.util.ExcelFontColors;
import com.pbc.util.POIExcelUtils;

public class R356Excel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath() + "/CashInflowSummary.pdf";

	Datasource ds = new Datasource();
	Statement s = null;
	Statement s2 = null;
	Statement s3 = null;
	Statement s4 = null;

	public static void main(String[] args) throws Exception {

	}

	public void createPdf(String filename, Date StartDate, Date EndDate, String Distributors, String OrderBookers,
			String TSOs, String ASMs, String Regions, String Channels, String Brands, String SKUs, String Cities,
			int reportType)
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

		ds.createConnection();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("Channel Wise Sales");

		XSSFCellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();

		ExcelColorsInHexa excelColors = new ExcelColorsInHexa();
		ExcelFontColors excelFontColors = new ExcelFontColors();
		ExcelFontAlign fontAlign = new ExcelFontAlign();

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

		boolean showGurr = true;

		if (Brands.length() > 0) {
			String[] arrOfStr = Brands.split(",");

			for (String a : arrOfStr) {
				showGurr = (Utilities.parseInt(a) == 8 || Utilities.parseInt(a) == 9) ? true : false;
			}
		}

		/*******************************
		 * ROW 1 Start
		 *******************************/

		// Report Heading
		String reportName = "Channel Wise Sales in ";
		reportName += (reportType == 1) ? "Units" : (reportType == 2) ? "Tonnage" : (reportType == 3) ? "Amount" : "";
		Row = spreadsheet.createRow((short) RowCount);
		Cell = (XSSFCell) Row.createCell((short) 0);
		Cell.setCellValue("R356 - " + reportName);

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

		String whereIPVSkUs = (SKUs.length() > 0) ? " and ipv.product_id in (" + SKUs + ")" : "";
		String whereIPVLrbs = (Brands.length() > 0) ? " and ipv.lrb_type_id in (" + Brands + ")" : "";

		int totalSKUs = 0;
//		System.out.println(
//				"select count(product_id) as totalCount from pep.inventory_products_view ipv where is_visible=1"
//						+ whereIPVSkUs + whereIPVLrbs);
		ResultSet rsSKUCounts = s.executeQuery(
				"select count(product_id) as totalCount from pep.inventory_products_view ipv where is_visible=1"
						+ whereIPVSkUs + whereIPVLrbs);
		if (rsSKUCounts.first()) {
			totalSKUs = rsSKUCounts.getInt("totalCount");
		}

		// System.out.println("Total SKUs : "+totalSKUs);

		String whereLrbs = (Brands.length() > 0) ? " and iplt.id in (" + Brands + ")" : "";
		ArrayList<String> LRBTitles = new ArrayList<String>();
		ArrayList<Integer> LRBIds = new ArrayList<Integer>();
		// System.out.println("SELECT * from inventory_products_lrb_types iplt where
		// id!=3"+whereLrbs);
		ResultSet rs2 = s.executeQuery("SELECT * from inventory_products_lrb_types iplt where id!=3" + whereLrbs);
		while (rs2.next()) {
			LRBIds.add(rs2.getInt("id"));
			LRBTitles.add(rs2.getString("label"));
		}

		ArrayList<Integer> lrbSpaces = new ArrayList<Integer>();
		for (int i = 0; i < LRBIds.size(); i++) {
			ResultSet rsLrbMrge = s
					.executeQuery("select count(product_id) as c from inventory_products_view ipv where is_visible=1 "
							+ whereIPVSkUs + " and lrb_type_id=" + LRBIds.get(i)); // Brand
			// Filter
			while (rsLrbMrge.next()) {
				lrbSpaces.add(rsLrbMrge.getInt("c"));
			}
		}

		String[] columnNames = { "PSR THEIA ", "PSR NAME", "DISTRIBUTOR ID", "DISTRIBUTOR NAME", "TOWN", "TSO", "ASM",
				"Region", "Channel" };
		int totalBlankHeadings = columnNames.length;
		int totalBlankSKUs = totalSKUs + LRBIds.size();

		// System.out.println("Total : " + (totalBlankSKUs + totalBlankHeadings));

		int mergeFromCol = 0;
		int mergeToCol = 0;

		/***************************** Data ***************************************/

		/*******************************
		 * ROW 3 Start
		 *******************************/

		RowCount++;
		Row = spreadsheet.createRow((short) RowCount);
		Cell = (XSSFCell) Row.createCell((short) 0);

		// Row 3 Column merging
		mergeFromCol = 0;
		mergeToCol = totalBlankHeadings - 1;
		spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, RowCount, mergeFromCol, mergeToCol));

		mergeFromCol = mergeToCol + 1;
		mergeToCol = mergeToCol + totalBlankSKUs + ((showGurr) ? 2 : 1);

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
		Row = spreadsheet.createRow((short) RowCount);
		Cell = (XSSFCell) Row.createCell((short) 0);

		// Row 4 Column merging
		mergeFromCol = 0;
		mergeToCol = totalBlankHeadings - 1;
		spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, RowCount, mergeFromCol, mergeToCol));

		mergeFromCol = totalBlankHeadings;
		mergeToCol = 0;

		// System.out.println("LRB Size : " + LRBIds.size());
		// System.out.println("Colors Size : " +
		// excelColors.getExcelColorsList().size());
		for (int i = 0; i < LRBIds.size(); i++) {
			// System.out.println(i + " Size : " + lrbSpaces.get(i));
			mergeToCol = mergeFromCol + lrbSpaces.get(i);
			Cell = Row.createCell((int) (short) mergeFromCol);
			Cell.setCellValue(LRBTitles.get(i));
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getExcelColorsList().get(i), style, font,
					excelFontColors.getBlack(), false, true, fontAlign.getCenter());
			if (lrbSpaces.get(i) != 0) {
				spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, RowCount, mergeFromCol, mergeToCol));
			}

			mergeFromCol = mergeToCol + 1;
		}

		mergeToCol = mergeFromCol + 1;
		Cell = Row.createCell((int) (short) mergeFromCol);
		Cell.setCellValue("");
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getGray(), style, font, excelFontColors.getBlack(),
				false, true, fontAlign.getCenter());
		if (showGurr)
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

		Col = 0;
		for (String title : columnNames) {
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(title);
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getLightGray(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			Col++;
		}

		int arraySize = 1;
		int LrbTitleIncrement = 0;
		// List<Integer> list=new ArrayList<Integer>();

		for (int LRBId : LRBIds) {

			ResultSet rsSKUsTitle = s.executeQuery(
					"select concat(brand_label,'-',package_label) as SKU_label from pep.inventory_products_view ipv where is_visible=1 "
							+ whereIPVSkUs + " and lrb_type_id in(" + LRBId + ")");
			while (rsSKUsTitle.next()) {
				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsSKUsTitle.getString("SKU_label"));
				POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font,
						excelFontColors.getBlack(), true, true, fontAlign.getCenter());
				Col++;
				arraySize++;
			}

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("Total " + LRBTitles.get(LrbTitleIncrement));
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getBlue(), style, font,
					excelFontColors.getWhite(), true, true, fontAlign.getCenter());
			Col++;
			LrbTitleIncrement++;
			arraySize++;
			// list.add(arraySize -1);
		} // end of LRBs Loop

		if (showGurr) {
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue("Total Gurr");
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getBrown(), style, font,
					excelFontColors.getWhite(), true, true, fontAlign.getCenter());
			Col++;
			LrbTitleIncrement++;
			arraySize++;
		}

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue("Total Brand");
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getGreen(), style, font, excelFontColors.getWhite(),
				true, true, fontAlign.getCenter());
		Col++;
		LrbTitleIncrement++;
		arraySize++;

		// System.out.println("arraySize : " + arraySize);

		/*******************************
		 * ROW 5 End
		 *******************************/

		/*******************************
		 * ROW Dynamic Start
		 *******************************/
		double GrandTotal[] = new double[arraySize];
		for (int u = 0; u < arraySize; u++) {
			GrandTotal[u] = 0.00;
		}

		String ids = "";
		String whereDistributors = (Distributors.length() > 0) ? " and isa.distributor_id in (" + Distributors + ")"
				: "";
		// String whereOrderBookersMo = (OrderBookers.length() > 0) ? " and
		// isa.booked_by in (" + OrderBookers + ")" : "";
		String whereOrderBookersMo = (OrderBookers.length() > 0) ? " and isap.cache_booked_by in (" + OrderBookers + ")"
				: "";
		String whereRegions = (Regions.length() > 0) ? " and isa.region_id in (" + Regions + ")" : "";

		String whereCityDistributors = "";
		ids = "";
		if (Cities.length() > 0) {
			int c = 0;
			// System.out.println("Select distributor_id from common_distributors where
			// rsm_id in ("+ASMs+")");
			ResultSet rsCity = s
					.executeQuery("Select distributor_id from common_distributors where city_id in (" + Cities + ")");
			while (rsCity.next()) {
				ids = (c == 0) ? ids + rsCity.getString("distributor_id")
						: ids + "," + rsCity.getString("distributor_id");
				c++;
			}

			whereCityDistributors = " and isa.distributor_id in (" + ids + ")";
		}

		String whereChannels = (Channels.length() > 0) ? " and co.pic_channel_id in (" + Channels + ")" : "";

		String whereASMsDistributor = "";
		ids = "";
		if (ASMs.length() > 0) {
			ids = "";
			int c = 0;
			// System.out.println("Select distributor_id from common_distributors where
			// rsm_id in ("+ASMs+")");
			ResultSet rsAsms = s
					.executeQuery("Select distributor_id from common_distributors where rsm_id in (" + ASMs + ")");
			while (rsAsms.next()) {
				ids = (c == 0) ? ids + rsAsms.getString("distributor_id")
						: ids + "," + rsAsms.getString("distributor_id");
				c++;
			}
			whereASMsDistributor = " and isa.distributor_id in (" + ids + ")";
		}

		String whereTSOsDistributor = "";
		if (TSOs.length() > 0) {
			ids = "";
			int c = 0;
			// System.out.println("Select distributor_id from common_distributors where
			// rsm_id in ("+ASMs+")");
			ResultSet rsTsos = s.executeQuery(
					"SELECT distinct distributor_id from distributor_beat_plan where asm_id in (" + TSOs + ")");
			while (rsTsos.next()) {
				ids = (c == 0) ? ids + rsTsos.getString("distributor_id")
						: ids + "," + rsTsos.getString("distributor_id");
				c++;
			}
			whereTSOsDistributor = " and isa.distributor_id in (" + ids + ")";
		}

		/*********************** Query Work Start **********************/

		// For Mobile Orders
		String OrderBookerQuery = "select distinct isa.booked_by,"
				+ "(select Display_Name from users u where u.id=isa.booked_by)"
				+ " as psr_name from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id join common_outlets co on isa.outlet_id=co.id where isa.adjusted_on between "
				+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) + whereDistributors
				+ whereASMsDistributor + whereTSOsDistributor + whereCityDistributors + whereOrderBookersMo
				+ whereChannels + whereRegions + " and isa.order_id is not null";

		// System.out.println(OrderBookerQuery);
		ResultSet rsOrderBooker = s.executeQuery(OrderBookerQuery);
		while (rsOrderBooker.next()) {

			int increment = 0;
			long orderBookerId = rsOrderBooker.getLong("booked_by");
			String name = rsOrderBooker.getString("psr_name");

//			System.out.println(
//			"select distinct pic_channel_id, ifnull((select psc.label from pci_sub_channel psc where psc.id=co.pic_channel_id),'Untagged') as channel from common_outlets co where id in( select distinct outlet_id from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isa.adjusted_on between "
//			+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
//			+ ((i==0) ? " and isa.booked_by =" : "and isa.created_by =") + orderBookerId + ")" + whereChannels);
			ResultSet rsChannel = s2.executeQuery(
					"select distinct pic_channel_id, ifnull((select psc.label from pci_sub_channel psc where psc.id=co.pic_channel_id),'Untagged') as channel from common_outlets co where id in( select distinct outlet_id from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isa.adjusted_on between "
							+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
							+ " and isa.booked_by=" + orderBookerId + ")" + whereChannels);
			while (rsChannel.next()) {
				// System.out.println("OB : " + orderBookerId + " Channel : " +
				// rsChannel.getString("channel"));
				int channel_id = rsChannel.getInt("pic_channel_id");
				long distributor_id = 0;
				String region = "", asm = "", tso = "", city = "", distributor_name = "";

//				System.out.println(
//				"select beat_plan_id, distributor_id, (select name from common_distributors cd where cd.distributor_id=isa.distributor_id) distributor_name, (select region_name from common_regions cr where cr.region_id=isa.region_id) region, (select city from common_distributors cd where cd.distributor_id=isa.distributor_id) city, (select DISPLAY_NAME from users u where u.id in (select rsm_id from common_distributors cd where cd.distributor_id=isa.distributor_id)) asm, (select DISPLAY_NAME from users u where u.id in (select distinct asm_id from distributor_beat_plan pjp where pjp.id=isa.beat_plan_id)) tso from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isa.adjusted_on between "
//				+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
//				+ ((i==0) ? " and isa.booked_by =" : " and isa.created_by =") + orderBookerId);
				ResultSet rsPJP = s3.executeQuery(
						"select beat_plan_id, distributor_id, (select name from common_distributors cd where cd.distributor_id=isa.distributor_id) distributor_name, (select region_name from common_regions cr where cr.region_id=isa.region_id) region, (select city from common_distributors cd where cd.distributor_id=isa.distributor_id) city, (select DISPLAY_NAME from users u where u.id in (select rsm_id from common_distributors cd where cd.distributor_id=isa.distributor_id)) asm, (select DISPLAY_NAME from users u where u.id in (select distinct asm_id from distributor_beat_plan pjp where pjp.id=isa.beat_plan_id)) tso from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isa.adjusted_on between "
								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
								+ " and isa.booked_by=" + orderBookerId);
				if (rsPJP.first()) {

					distributor_id = rsPJP.getLong("distributor_id");
					region = rsPJP.getString("region");
					asm = rsPJP.getString("asm");
					tso = rsPJP.getString("tso");
					city = rsPJP.getString("city");
					distributor_name = rsPJP.getString("distributor_name");
				}

				increment = 0;

				RowCount++;
				Row = spreadsheet.createRow((short) RowCount);
				Cell = (XSSFCell) Row.createCell((short) 0);
				Col = 0;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(orderBookerId);
				Cell.setCellStyle(styleRight);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(orderBookerId + "-" + name);
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(distributor_id);
				Cell.setCellStyle(styleRight);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(distributor_id + "-" + distributor_name);
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(city);
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(tso);
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(asm);
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(region);
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsChannel.getString("channel"));
				Cell.setCellStyle(styleLeft);
				Col++;

				/***************************** Sales Data ***********************************/
				ArrayList<Integer> ProductIDs = new ArrayList<Integer>();
				ResultSet Product = s3.executeQuery(
						"select product_id FROM inventory_sales_adjusted isa JOIN inventory_sales_adjusted_products isap ON (isa.id = isap.id) join inventory_sales_dispatch_invoices isdi  on isa.id=isdi.sales_id join inventory_sales_dispatch i_isd on i_isd.id=isdi.id"
								+ " where  " + " i_isd.created_on between " + Utilities.getSQLDate(StartDate) + " and "
								+ Utilities.getSQLDate(EndDate) + " and booked_by=" + orderBookerId);
				while (Product.next()) {
					ProductIDs.add(Product.getInt("product_id"));
				}
				double totalGurr1 = 0, grandTotal1 = 0;
				for (int LRBId : LRBIds) {
					double total = 0;
//					System.out.println("select product_id from pep.inventory_products_view ipv where is_visible=1 "
//							+ whereIPVSkUs + " and lrb_type_id=" + LRBId);

					ResultSet rsSKUs = s3.executeQuery(
							"select product_id,liquid_in_ml,unit_per_sku from pep.inventory_products_view ipv where is_visible=1 "
									+ whereIPVSkUs + " and lrb_type_id=" + LRBId);
					while (rsSKUs.next()) {
						int product_id = rsSKUs.getInt("product_id");
						double Product_Sale = 0;
						if (ProductIDs.contains(product_id)) {

							String whereChannel_id = (channel_id != 0) ? " and co.pic_channel_id=" + channel_id
									: " and co.pic_channel_id is null";

							String mainQuery = "SELECT sum(total_units*(" + rsSKUs.getDouble("liquid_in_ml") + ")) as c"
									+ " from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id join common_outlets co  on isa.outlet_id=co.id  where isa.adjusted_on between "
									+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
									+ " and isa.booked_by=" + orderBookerId + " and isap.product_id=" + product_id
									+ whereChannel_id;
							if (reportType == 1) {
								mainQuery = "SELECT sum(total_units) as c"
										+ " from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id join common_outlets co  on isa.outlet_id=co.id where isa.adjusted_on between "
										+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
										+ " and isa.booked_by=" + orderBookerId + " and isap.product_id=" + product_id
										+ whereChannel_id;
							} else if (reportType == 3) {
								mainQuery = "SELECT sum(isap.net_amount) as c"
										+ " from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id join common_outlets co on isa.outlet_id=co.id where isa.adjusted_on between "
										+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
										+ " and isa.booked_by=" + orderBookerId + " and isap.product_id=" + product_id
										+ whereChannel_id;
							}

			
							//System.out.println(mainQuery);
							// System.out.println(
//								"SELECT "+dataQuery+" FROM pep.inventory_sales_adjusted_products where product_id="
//										+ rsSKUs.getInt("product_id")
//										+ " and id in(SELECT isa.id FROM pep.inventory_sales_adjusted isa  join common_outlets co on isa.outlet_id=co.id where adjusted_on between "
//										+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
//										+ " and booked_by=" + orderBookerId +" and co.pic_channel_id="+channel_id+")");
							// System.out.println(mainQuery);
							ResultSet rsSales = s4.executeQuery(mainQuery);
							if (rsSales.first()) {

								Product_Sale = rsSales.getDouble("c");
							}
						} else {
							Product_Sale = 0;
						}

						Cell = (XSSFCell) Row.createCell((short) Col);
						Cell.setCellValue(Product_Sale);
						Cell.setCellStyle(styleRight);
						Col++;

						total += Product_Sale;
						GrandTotal[increment] = GrandTotal[increment] + Product_Sale;
						increment++;

					} // End of Product Loop

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(total);
					Cell.setCellStyle(styleRight);
					Col++;
					GrandTotal[increment] = GrandTotal[increment] + total;
					increment++;

					grandTotal1 += total;

					if (LRBId == 8 || LRBId == 9) {
						totalGurr1 += total;
					}

				} // End of LRB Loop

				if (showGurr) {
					// Total gurr
					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(totalGurr1);
					Cell.setCellStyle(styleRight);
					Col++;
					GrandTotal[increment] = GrandTotal[increment] + totalGurr1;
					increment++;
				}

				// Grand Total
				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(grandTotal1);
				Cell.setCellStyle(styleRight);
				Col++;
				GrandTotal[increment] = GrandTotal[increment] + grandTotal1;
				increment++;

				/***************************** Sales Data ***********************************/

			} // End of channel loop
		} // End of main loop

		/*********************** Query Work End **********************/

		// For Desk Sale
		String DeskSalesQuery = "select isa.id ," + "'Desk Sale'"
				+ " as psr_name from inventory_sales_adjusted isa join common_outlets co on isa.outlet_id=co.id where isa.adjusted_on between "
				+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) + whereDistributors
				+ whereASMsDistributor + whereTSOsDistributor + whereCityDistributors + whereOrderBookersMo
				+ whereChannels + whereRegions + " and isa.order_id is  null";

		// System.out.println(DeskSalesQuery);
		ResultSet rsDeskSalesQuery = s.executeQuery(DeskSalesQuery);
		while (rsDeskSalesQuery.next()) {

			int increment = 0;
			long orderId = rsDeskSalesQuery.getLong("id");
			String name = rsDeskSalesQuery.getString("psr_name");

//						System.out.println(
//						"select distinct pic_channel_id, ifnull((select psc.label from pci_sub_channel psc where psc.id=co.pic_channel_id),'Untagged') as channel from common_outlets co where id in( select distinct outlet_id from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isa.adjusted_on between "
//						+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
//						+ ((i==0) ? " and isa.booked_by =" : "and isa.created_by =") + orderBookerId + ")" + whereChannels);
			ResultSet rsChannel = s2.executeQuery(
					"select distinct pic_channel_id, ifnull((select psc.label from pci_sub_channel psc where psc.id=co.pic_channel_id),'Untagged') as channel from common_outlets co where id in( select distinct outlet_id from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isa.adjusted_on between "
							+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
							+ " and isa.id=" + orderId + ")" + whereChannels);
			while (rsChannel.next()) {
				// System.out.println("OB : " + orderBookerId + " Channel : " +
				// rsChannel.getString("channel"));
				int channel_id = rsChannel.getInt("pic_channel_id");
				long distributor_id = 0;
				String region = "", asm = "", tso = "", city = "", distributor_name = "";

//							System.out.println(
//							"select beat_plan_id, distributor_id, (select name from common_distributors cd where cd.distributor_id=isa.distributor_id) distributor_name, (select region_name from common_regions cr where cr.region_id=isa.region_id) region, (select city from common_distributors cd where cd.distributor_id=isa.distributor_id) city, (select DISPLAY_NAME from users u where u.id in (select rsm_id from common_distributors cd where cd.distributor_id=isa.distributor_id)) asm, (select DISPLAY_NAME from users u where u.id in (select distinct asm_id from distributor_beat_plan pjp where pjp.id=isa.beat_plan_id)) tso from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isa.adjusted_on between "
//							+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
//							+ ((i==0) ? " and isa.booked_by =" : " and isa.created_by =") + orderBookerId);
				ResultSet rsPJP = s3.executeQuery(
						"select beat_plan_id, distributor_id, (select name from common_distributors cd where cd.distributor_id=isa.distributor_id) distributor_name, (select region_name from common_regions cr where cr.region_id=isa.region_id) region, (select city from common_distributors cd where cd.distributor_id=isa.distributor_id) city, (select DISPLAY_NAME from users u where u.id in (select rsm_id from common_distributors cd where cd.distributor_id=isa.distributor_id)) asm, (select DISPLAY_NAME from users u where u.id in (select distinct asm_id from distributor_beat_plan pjp where pjp.id=isa.beat_plan_id)) tso from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id where isa.adjusted_on between "
								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
								+ " and isa.id=" + orderId);
				if (rsPJP.first()) {

					distributor_id = rsPJP.getLong("distributor_id");
					region = rsPJP.getString("region");
					asm = rsPJP.getString("asm");
					tso = rsPJP.getString("tso");
					city = rsPJP.getString("city");
					distributor_name = rsPJP.getString("distributor_name");
				}

				increment = 0;

				RowCount++;
				Row = spreadsheet.createRow((short) RowCount);
				Cell = (XSSFCell) Row.createCell((short) 0);
				Col = 0;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue("-");
				Cell.setCellStyle(styleCenter);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(name);
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(distributor_id);
				Cell.setCellStyle(styleRight);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(distributor_id + "-" + distributor_name);
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(city);
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(tso);
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(asm);
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(region);
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsChannel.getString("channel"));
				Cell.setCellStyle(styleLeft);
				Col++;

				/***************************** Sales Data ***********************************/

				double totalGurr1 = 0, grandTotal1 = 0;
				for (int LRBId : LRBIds) {
					double total = 0;
//								System.out.println("select product_id from pep.inventory_products_view ipv where is_visible=1 "
//										+ whereIPVSkUs + " and lrb_type_id=" + LRBId);
					ResultSet rsSKUs = s3.executeQuery(
							"select product_id,liquid_in_ml from pep.inventory_products_view ipv where is_visible=1 "
									+ whereIPVSkUs + " and lrb_type_id=" + LRBId);
					while (rsSKUs.next()) {
						int product_id = rsSKUs.getInt("product_id");

						String whereChannel_id = (channel_id != 0) ? " and co.pic_channel_id=" + channel_id
								: " and co.pic_channel_id is null";

						String mainQuery = "SELECT sum(total_units*(" + rsSKUs.getDouble("liquid_in_ml") + ")) as c"
								+ " from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id join common_outlets co  on isa.outlet_id=co.id  where isa.id in ("
								+ orderId + ") and isap.product_id=" + product_id + whereChannel_id;
						if (reportType == 1) {
							mainQuery = "SELECT sum(total_units) as c"
									+ " from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id join common_outlets co  on isa.outlet_id=co.id where isa.id in ("
									+ orderId + ") and isap.product_id=" + product_id + whereChannel_id;
						} else if (reportType == 3) {
							mainQuery = "SELECT sum(isap.net_amount) as c"
									+ " from inventory_sales_adjusted_products as isap join inventory_sales_adjusted isa on isap.id=isa.id join common_outlets co on isa.outlet_id=co.id where isa.id in ("
									+ orderId + ") and isap.product_id=" + product_id + whereChannel_id;
						}

						// System.out.println(
//											"SELECT "+dataQuery+" FROM pep.inventory_sales_adjusted_products where product_id="
//													+ rsSKUs.getInt("product_id")
//													+ " and id in(SELECT isa.id FROM pep.inventory_sales_adjusted isa  join common_outlets co on isa.outlet_id=co.id where adjusted_on between "
//													+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
//													+ " and booked_by=" + orderBookerId +" and co.pic_channel_id="+channel_id+")");
						// System.out.println(mainQuery);
						ResultSet rsSales = s4.executeQuery(mainQuery);
						if (rsSales.first()) {

							Cell = (XSSFCell) Row.createCell((short) Col);
							Cell.setCellValue(rsSales.getDouble("c"));
							Cell.setCellStyle(styleRight);
							Col++;

							total += rsSales.getDouble("c");
							GrandTotal[increment] = GrandTotal[increment] + rsSales.getDouble("c");
							increment++;
						}
					} // End of Product Loop

					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(total);
					Cell.setCellStyle(styleRight);
					Col++;
					GrandTotal[increment] = GrandTotal[increment] + total;
					increment++;

					grandTotal1 += total;

					if (LRBId == 8 || LRBId == 9) {
						totalGurr1 += total;
					}

				} // End of LRB Loop

				if (showGurr) {
					// Total gurr
					Cell = (XSSFCell) Row.createCell((short) Col);
					Cell.setCellValue(totalGurr1);
					Cell.setCellStyle(styleRight);
					Col++;
					GrandTotal[increment] = GrandTotal[increment] + totalGurr1;
					increment++;
				}

				// Grand Total
				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(grandTotal1);
				Cell.setCellStyle(styleRight);
				Col++;
				GrandTotal[increment] = GrandTotal[increment] + grandTotal1;
				increment++;

				/***************************** Sales Data ***********************************/

			} // End of channel loop
		} // End of main loop

		/*******************************
		 * ROW Dynamic End
		 *******************************/

		/*******************************
		 * ROW Grand Start
		 *******************************/

		RowCount++;
		Row = spreadsheet.createRow((short) RowCount);
		Cell = (XSSFCell) Row.createCell((short) 0);

		mergeFromCol = 0;
		mergeToCol = totalBlankHeadings - 2;
		spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, RowCount, mergeFromCol, mergeToCol));

		Col = mergeToCol + 1;

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue("Grand Total");
		Cell.setCellStyle(styleRight);
		Col++;

		for (int j = 0; j < GrandTotal.length - ((showGurr ? 3 : 2)); j++) {
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(GrandTotal[j]);
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font,
					excelFontColors.getBlack(), true, true, fontAlign.getCenter());
			Col++;
		}

		// Total gurr
		if (showGurr) {
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(GrandTotal[GrandTotal.length - 3]);
			POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getBrown(), style, font,
					excelFontColors.getWhite(), true, true, fontAlign.getCenter());
			Col++;
		}

		// Grand Total
		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(GrandTotal[GrandTotal.length - 2]);
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getGreen(), style, font, excelFontColors.getWhite(),
				true, true, fontAlign.getCenter());
		Col++;

		/*******************************
		 * ROW Grand End
		 *******************************/

		// Auto Sizing Column grandAttainmentTotal
		for (int i = 0; i < 150; i++) {
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
		out.close();

		s.close();
		s2.close();
		s3.close();
		s4.close();
		ds.dropConnection();
	}
}
