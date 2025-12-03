package com.pbc.reports;

import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

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

public class R358Excel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath() + "/CashInflowSummary.pdf";

	Datasource ds = new Datasource();
	Statement s = null;
	Statement s2 = null;
	Statement s3 = null;
	Statement s4 = null;

	Date StartDate = Utilities.getDateByDays(0); // Utilities.parseDate("13/02/2016");
	Date EndDate = Utilities.getDateByDays(0);// Utilities.parseDate("13/02/2016");

	/// Date StartDate = Utilities.parseDate("29/08/2016");
	/// Date EndDate = Utilities.parseDate("29/08/2016");

	Date Yesterday = Utilities.getDateByDays(-1);

	long SND_ID = 0;

	public static void main(String[] args) throws Exception {

	}

	public void createPdf(String filename, Date StartDate, Date EndDate, String Distributors, String OrderBookers,
			String TSOs, String ASMs, String Regions, String Channels, String Brands, String SKUs, String Cities)
			throws IOException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

		ds.createConnection();
		s = ds.createStatement();
		s2 = ds.createStatement();

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
		Row = spreadsheet.createRow((short) RowCount);
		Cell = (XSSFCell) Row.createCell((short) 0);

		Cell.setCellValue("R358 - Brand Wise Sales Report ");

		spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, // first row (0-based)
				RowCount, // last row (0-based)
				0, // first column (0-based)
				3 // last column (0-based)
		));

		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getWhite(), style, font,
				excelFontColors.getBlack(), false, true, fontAlign.getLeft());
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

		String[] columnNames = { "Date", "PSR THEIA ID", "PSR NAME", "DISTRIBUTOR ID", "DISTRIBUTOR NAME", "TOWN",
				"TSO", "ASM", "Region", "Outlet Code", "Outlet Name", "Brand Name", "SKU Name", "Channel Name", "Units",
				"Tons", "Value" };
		
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

		/*******************************
		 * ROW 3 End
		 *******************************/

		long TotalCartons = 0;
		double TotalTonnage = 0.0;
		double TotalAmount = 0.0;

		/*******************************
		 * ROW Dynamic Start
		 *******************************/

		String ids = "";
		String whereDistributors = (Distributors.length() > 0) ? " and isa.distributor_id in (" + Distributors + ")"
				: "";
		String whereOrderBookersMo = (OrderBookers.length() > 0) ? " and isa.booked_by in (" + OrderBookers + ")" : "";
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
			int c = 0;
			ids="";
			// System.out.println("Select distributor_id from common_distributors where
			// rsm_id in ("+ASMs+")");
			ResultSet rsAsms = s
					.executeQuery("Select distinct distributor_id from common_distributors where rsm_id in (" + ASMs + ")");
			while (rsAsms.next()) {
				ids = (c == 0) ? ids + rsAsms.getString("distributor_id")
						: ids + "," + rsAsms.getString("distributor_id");
				c++;
			}
			whereASMsDistributor = " and isa.distributor_id in (" + ids + ")";
		}

		String whereTSOsDistributor = "";
		if (TSOs.length() > 0) {
			int c = 0;
			ids="";
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

		String whereSkUs = (SKUs.length() > 0) ? " and isap.product_id in (" + SKUs + ")" : "";
		String whereLrbs = (Brands.length() > 0) ? " and ipv.lrb_type_id in (" + Brands + ")" : "";

		String OrderBookerQuery = "select isa.id,isa.adjusted_on, isa.booked_by as psr_id, (select DISPLAY_NAME from users where id= isa.booked_by)as psr_name,isa.distributor_id, (select name from common_distributors cd where cd.distributor_id=isa.distributor_id) distributor_name,(select city from common_distributors cd where cd.distributor_id=isa.distributor_id) city,(select DISPLAY_NAME from users where id in (SELECT asm_id FROM distributor_beat_plan pjp where pjp.id=isa.beat_plan_id)) tso,(select DISPLAY_NAME from users where id in (SELECT rsm_id FROM common_distributors cd where cd.distributor_id=isa.distributor_id)) asm,(select region_name from common_regions where region_id=isa.region_id) as region,isa.outlet_id, co.name as outlet_name, ifnull((select psc.label from pci_sub_channel psc where psc.id=co.pic_channel_id),'Untagged') as channel"
				+ " from inventory_sales_adjusted isa join common_outlets co on co.id=isa.outlet_id where isa.adjusted_on between "
				+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) + whereDistributors
				+ whereOrderBookersMo + whereRegions + whereCityDistributors + whereChannels + whereTSOsDistributor + whereASMsDistributor;
		// System.out.println(OrderBookerQuery);
		ResultSet rsMainQuery = s.executeQuery(OrderBookerQuery);
		while (rsMainQuery.next()) {
			int order_id = rsMainQuery.getInt("id");

			System.out.println(
			"select (SELECT label FROM pep.inventory_products_lrb_types where id=ipv.lrb_type_id) brand,concat(package_label,'-',brand_label) as sku,total_units as unit,(total_units*ipv.liquid_in_ml) as tonnage,net_amount as amount from inventory_sales_adjusted_products isap join inventory_products_view ipv on ipv.product_id=isap.product_id where id="
			+ order_id + whereSkUs + whereLrbs);
			ResultSet rsProducts = s2.executeQuery(
					"select (SELECT label FROM pep.inventory_products_lrb_types where id=ipv.lrb_type_id) brand,concat(package_label,'-',brand_label) as sku,total_units as unit,(total_units*ipv.liquid_in_ml) as tonnage,net_amount as amount from inventory_sales_adjusted_products isap join inventory_products_view ipv on ipv.product_id=isap.product_id where id="
							+ order_id + whereSkUs + whereLrbs);
			while (rsProducts.next()) {
				System.out.println("=============> RowCount "+RowCount);
				RowCount++;
				  // Validate row index
				int spreadSSheetCount=2;
		        if (RowCount > 32766) {
		        	RowCount=0;
		        	spreadsheet = workbook.createSheet("Brand-SKU wise sales_"+spreadSSheetCount);
		        	spreadSSheetCount++;
		        }

				
				Row = spreadsheet.createRow((short) RowCount);
				Col = 0;

//				System.out.println("Date "+rsMainQuery.getDate("adjusted_on"));
//				System.out.println("Date "+Utilities.getDisplayDateFormat(rsMainQuery.getDate("adjusted_on")));
//				System.out.println("Date "+Utilities.getDisplayDateFormat(rsMainQuery.getTimestamp("adjusted_on")));

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(Utilities.getDisplayDateFormat(rsMainQuery.getTimestamp("adjusted_on")));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(((rsMainQuery.getLong("psr_id") != 0) ? rsMainQuery.getString("psr_id") : "-"));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(((rsMainQuery.getLong("psr_id") != 0)
						? rsMainQuery.getLong("psr_id") + "-" + rsMainQuery.getString("psr_name")
						: "Desk Sale"));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsMainQuery.getLong("distributor_id"));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(
						rsMainQuery.getLong("distributor_id") + "-" + rsMainQuery.getString("distributor_name"));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsMainQuery.getString("city"));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsMainQuery.getString("tso"));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsMainQuery.getString("asm"));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsMainQuery.getString("region"));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsMainQuery.getLong("outlet_id"));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsMainQuery.getString("outlet_name"));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsProducts.getString("brand"));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsProducts.getString("sku"));
				Cell.setCellStyle(styleLeft);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsMainQuery.getString("channel"));
				Cell.setCellStyle(styleLeft);
				Col++;

				TotalCartons += rsProducts.getLong("unit");
				TotalTonnage += rsProducts.getDouble("tonnage");
				TotalAmount += rsProducts.getDouble("amount");

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsProducts.getLong("unit"));
				Cell.setCellStyle(styleRight);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsProducts.getDouble("tonnage"));
				Cell.setCellStyle(styleRight);
				Col++;

				Cell = (XSSFCell) Row.createCell((short) Col);
				Cell.setCellValue(rsProducts.getDouble("amount"));
				Cell.setCellStyle(styleRight);

			} // End of Product Loo[

		} // END of Main Loop
			

		/*******************************
		 * ROW Dynamic END
		 *******************************/

		/*******************************
		 * ROW 7 Start
		 *******************************/

		RowCount++;

		Row = spreadsheet.createRow((short) RowCount);
		Cell = (XSSFCell) Row.createCell((short) 0);

		for (int i = 0; i < (columnNames.length - 5); i++) {
			Cell = (XSSFCell) Row.createCell((short) i);
			Cell.setCellValue("");
		}

		Col = columnNames.length - 4;

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue("Total");
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		Col++;

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(TotalCartons);
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		Col++;

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(TotalTonnage);
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		Col++;

		Cell = (XSSFCell) Row.createCell((short) Col);
		Cell.setCellValue(TotalAmount);
		POIExcelUtils.getStyleWithCell(workbook, Cell, excelColors.getYellow(), style, font,
				excelFontColors.getBlack(), true, true, fontAlign.getCenter());
		Col++;

		/*******************************
		 * ROW 7 End
		 *******************************/

		// Auto Sizing Column grandAttainmentTotal

		for (int i = 0; i < 100; i++) {
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
