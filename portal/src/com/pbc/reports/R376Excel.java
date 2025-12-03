package com.pbc.reports;

import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Date;

import com.pbc.util.AlmoizDateUtils;
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

public class R376Excel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath() + "/CashInflowSummary.pdf";

	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	Statement s5;
	Statement s6;

	Date StartDate = Utilities.getDateByDays(0); // Utilities.parseDate("13/02/2016");
	Date EndDate = Utilities.getDateByDays(0);// Utilities.parseDate("13/02/2016");

	/// Date StartDate = Utilities.parseDate("29/08/2016");
	/// Date EndDate = Utilities.parseDate("29/08/2016");

	Date Yesterday = Utilities.getDateByDays(-1);

	long SND_ID = 0;

	public static void main(String[] args) throws Exception {

	}

	public R376Excel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
		s5 = ds.createStatement();
		s6 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6) {
			Yesterday = Utilities.getDateByDays(-2);
		}

	}

	public void createPdf(String filename, Date StartDate, Date EndDate, String Distributors, String Regions,
			String Channels, String Brands, String SKUs, String CityIDs)
			throws IOException, SQLException, ParseException {

		String whereregion = (Regions.length() > 0) ? (" and r.region_id in (" + Regions + ")") : "";
		String whereproduct = (SKUs.length() > 0) ? (" and p.product_id in (" + SKUs + ")") : "";

		String whereBrand = (Brands.length() > 0)
				? (" and product_id in (select id from inventory_products where lrb_type_id in (" + Brands + "))")
				: "";
		String wherecities = (CityIDs.length() > 0)
				? (" and distributor_id in ( select distinct isr.distributor_id from inventory_sales_returns isr, common_distributors cd where cd.distributor_id = isr.distributor_id and cd.city_id in ("
						+ CityIDs + "))")
				: "";
		String wherechannel = (Channels.length() > 0)
				? (" and outlet_id in (select distinct co.id from inventory_sales_returns isr,   common_outlets co where co.id = isr.outlet_id and cd.pic_channel_id in ("
						+ Channels + "))")
				: "";
		 
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
		Cell.setCellValue("R376 - Return Type");
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

		String[] columnNames = { "Created Date", "Distributor ID", "Distributor Name", "Outlet ID", "Outlet Name",
				"Outlet Address", "Channel Label", "Region Name", "Sales Type", "Package Label", "Brand Label", "Cases",
				"Converted Cases", "SKU Amount" , "Return Type" };

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
				"SELECT  (select label from inventory_return_stock_types where id =return_type) return_type,r.created_on, r.distributor_id, (select name from common_distributors cd where cd.distributor_id=r.distributor_id) as distributor_name, r.outlet_id, (select region_name from common_regions cr where cr.region_id=r.region_id) as region, type_id ,p.raw_cases,  p.net_amount, p.product_id FROM inventory_sales_returns_products p JOIN inventory_sales_returns r ON p.id = r.id WHERE r.created_on BETWEEN "
						+ Utilities.getSQLDate(StartDate) + " AND " + Utilities.getSQLDateNext(EndDate)
						+ " AND return_type in (2,3) AND r.distributor_id in (" + Distributors + ")   " + whereregion + whereBrand + "  "
						+ whereproduct + " " + wherecities + "  " + wherechannel + "");
		while (rsData.next()) {
			
			String Outlet_ID = rsData.getString("outlet_id");
			String Product_Id = rsData.getString("product_id");
			double Cases = rsData.getDouble("raw_cases");
			int Type_id = rsData.getInt("type_id");
			
			String Outlet = "", address = "", channel=""; 
			ResultSet rsOutletData = s2.executeQuery("select name, address, (select label from pci_sub_channel where id=pic_channel_id) as channel from common_outlets where id="+Outlet_ID);	
			if(rsOutletData.first()) {
				Outlet = rsOutletData.getString("name");
				address = rsOutletData.getString("address");
				channel= rsOutletData.getString("channel");
			}
			
			String Type = (Type_id == 1) ? "Sales Type" : "Damage / Expire Return";
			
			String Package_label = "", Brand_label="";
			double convertedCses=0.0;
			ResultSet rsProductData = s2.executeQuery("select package_label, brand_label, unit_per_sku, liquid_in_ml from inventory_products_view where product_id="+Product_Id);	
			if(rsProductData.first()) {
				Package_label = rsProductData.getString("package_label");
				Brand_label = rsProductData.getString("brand_label");
				convertedCses = Cases * rsProductData.getDouble("liquid_in_ml") * rsProductData.getDouble("unit_per_sku");
			}
		
			RowCount++;
			Row = spreadsheet.createRow((short) RowCount);

			Col = 0;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(AlmoizDateUtils.getDisplayDateFormat(rsData.getTimestamp("created_on")));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getLong("distributor_id"));
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("distributor_name"));
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Outlet_ID);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Outlet_ID + "-" + Outlet);
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(address);
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(channel);
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("region"));
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Type);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Package_label);
			Cell.setCellStyle(styleLeft);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Brand_label);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(Cases);
			Cell.setCellStyle(styleRight);
			Col++;

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(convertedCses);
			Cell.setCellStyle(styleRight);
			Col++;
			

			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getDouble("net_amount"));
			Cell.setCellStyle(styleLeft);
			Col++;
			
			Cell = (XSSFCell) Row.createCell((short) Col);
			Cell.setCellValue(rsData.getString("return_type")); // Add Return Type column
			Cell.setCellStyle(styleLeft);
			Col++;

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

		ds.dropConnection();
	}
}