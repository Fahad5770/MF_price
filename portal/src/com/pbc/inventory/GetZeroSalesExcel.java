package com.pbc.inventory;

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

public class GetZeroSalesExcel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath() + "/CashInflowSummary.pdf";

	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;

	Date StartDate = Utilities.getDateByDays(0); // Utilities.parseDate("13/02/2016");
	Date EndDate = Utilities.getDateByDays(0);// Utilities.parseDate("13/02/2016");

	/// Date StartDate = Utilities.parseDate("29/08/2016");
	/// Date EndDate = Utilities.parseDate("29/08/2016");

	Date Yesterday = Utilities.getDateByDays(-1);

	long SND_ID = 0;

	public static void main(String[] args) throws Exception {

	}

	public GetZeroSalesExcel()
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6) {
			Yesterday = Utilities.getDateByDays(-2);
		}

	}

	public void createPdf(String filename, long SND_ID, String StartDate, String EndDate, String DistributorID)
			throws IOException, SQLException {

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("cell types");

		int FirstRowCount = 0;

		int RowCount = 0;

		// Report Heading

		XSSFRow rowH = spreadsheet.createRow((short) RowCount);
		XSSFCell cellH = (XSSFCell) rowH.createCell((short) 0);

		cellH.setCellValue("R318 - Sale Data Dump ");

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

		System.out.println("Period: " + StartDate + " - " + EndDate);

		cellP.setCellValue("Period: " + StartDate + " - " + EndDate);

		spreadsheet.addMergedRegion(new CellRangeAddress(FirstRowCount, // first row (0-based)
				FirstRowCount, // last row (0-based)
				0, // first column (0-based)
				1 // last column (0-based)
		));

		RowCount = RowCount + 1;
		FirstRowCount = FirstRowCount + 1;
		XSSFCellStyle style61 = workbook.createCellStyle();

		// Report Heading

		XSSFRow headerrow1 = spreadsheet.createRow((short) RowCount);
		XSSFCell headercell1 = (XSSFCell) headerrow1.createCell((short) 0);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 0);
		headercell1.setCellValue("Created Date");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 1);
		headercell1.setCellValue("Created Time");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 2);
		headercell1.setCellValue("Year");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 3);
		headercell1.setCellValue("Month");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 4);
		headercell1.setCellValue("Day");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 5);
		headercell1.setCellValue("Day Name");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 6);
		headercell1.setCellValue("Distributor ID");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 7);
		headercell1.setCellValue("Distributor Name");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 8);
		headercell1.setCellValue("Outlet ID");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 9);
		headercell1.setCellValue("Outlet Name");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 10);
		headercell1.setCellValue("SND ID");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 11);
		headercell1.setCellValue("SND Name");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 12);
		headercell1.setCellValue("RSM ID");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 13);
		headercell1.setCellValue("RSM Name");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 14);
		headercell1.setCellValue("TDM ID");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 15);
		headercell1.setCellValue("TDM Name");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 16);
		headercell1.setCellValue("Region ID");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 17);
		headercell1.setCellValue("Region Name");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 18);
		headercell1.setCellValue("PJP ID");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 19);
		headercell1.setCellValue("PJP Name");
		SetNormalCellBackColorH(workbook, headercell1, style61);
		
		headercell1 = (XSSFCell) headerrow1.createCell((short) 20);
		headercell1.setCellValue("Visit Day Number");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 21);
		headercell1.setCellValue("VISIT DAY NAME");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 22);
		headercell1.setCellValue("Order Booker ID");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 23);
		headercell1.setCellValue("Order Booker Name");
		SetNormalCellBackColorH(workbook, headercell1, style61);

		XSSFCellStyle style62 = workbook.createCellStyle();

		RowCount = RowCount + 1;


		
	//	System.out.println("fff"+DistributorID);

		// System.out.println("Where Dist "+WhereDistributor);

		// System.out.println(StartDate+" - "+EndDate);
	//	Date startDate = Utilities.parseDateYYYYMMDDWithBackSlash(StartDate);
		// Date endDate = Utilities.parseDateYYYYMMDDWithBackSlash(EndDate);

		// System.out.println(CurrentDate+" - "+Endate+" -
		// "+Utilities.getSQLDate(Utilities.getDateByDays(CurrentDate,1)));

	//	 System.out.println("SELECT  pjpv.id as PJP_ID, pjpv.label PJP_LABEL, CAST(created_on AS DATE) AS `SD_CREATED_ON_DATE`, CAST(created_on AS TIME) AS `SD_CREATED_ON_TIME`, YEAR(created_on) AS `SD_CREATED_ON_YEAR`, MONTH(created_on) AS `SD_CREATED_ON_MONTH`, DAYOFMONTH(created_on) AS `SD_CREATED_ON_DAY`, DAYNAME(created_on) AS `SD_CREATED_ON_DAY_NAME`, day_number, (SELECT long_name FROM pep.common_days_of_week cdw where cdw.id=pjpv.day_number) as SD_VISIT_DAY_NAME, pjpv.distributor_id as SD_DISTRIBUTOR_ID, (SELECT name FROM pep.common_distributors where distributor_id=pjpv.distributor_id) as SD_DISTRIBUTOR_LABEL, pjpv.outlet_id as SD_OUTLET_ID, (SELECT name FROM pep.common_outlets where id=pjpv.outlet_id) as SD_OUTLET_LABEL, (SELECT snd_id FROM pep.common_distributors where distributor_id=pjpv.distributor_id) as SD_SND_ID, (SELECT  users.DISPLAY_NAME   FROM users  WHERE users.ID=SD_SND_ID) as SD_SND_NAME, (SELECT rsm_id FROM pep.common_distributors where distributor_id=pjpv.distributor_id) as SD_RSM_ID, (SELECT  users.DISPLAY_NAME   FROM users  WHERE users.ID=SD_RSM_ID) as SD_RSM_NAME, (SELECT tdm_id FROM pep.common_distributors where distributor_id=pjpv.distributor_id) as SD_TDM_ID, (SELECT  users.DISPLAY_NAME   FROM users  WHERE users.ID=SD_TDM_ID) as SD_TDM_NAME, (SELECT region_id FROM pep.common_distributors where distributor_id=pjpv.distributor_id) as SD_REGION_ID, (SELECT  common_regions.region_name   FROM common_regions  WHERE common_regions.region_id=SD_REGION_ID) as SD_REGION_LABEL, pjpv.created_by AS SD_ORDER_BOOKER_ID, (SELECT CONCAT(`users`.`ID`, '-',`users`.`DISPLAY_NAME`) FROM  users  WHERE  users.ID = pjpv.created_by) AS SD_ORDER_BOOKER_NAME FROM pep.distributor_beat_plan_view pjpv WHERE pjpv.created_on between '"+StartDate+"' and '"+EndDate+"' AND  pjpv.distributor_id in ("+DistributorID+")  AND pjpv.distributor_id NOT IN(SELECT  distinct(isa.distributor_id) FROM  inventory_sales_adjusted isa WHERE  isa.distributor_id <> 123  AND isa.created_on BETWEEN '"+StartDate+"' AND '"+EndDate+"'AND  (isa.distributor_id IN ("+DistributorID+")))");
		ResultSet rs = s.executeQuery("SELECT  pjpv.id as PJP_ID, pjpv.label PJP_LABEL, CAST(created_on AS DATE) AS `SD_CREATED_ON_DATE`, CAST(created_on AS TIME) AS `SD_CREATED_ON_TIME`, YEAR(created_on) AS `SD_CREATED_ON_YEAR`, MONTH(created_on) AS `SD_CREATED_ON_MONTH`, DAYOFMONTH(created_on) AS `SD_CREATED_ON_DAY`, DAYNAME(created_on) AS `SD_CREATED_ON_DAY_NAME`, day_number, (SELECT long_name FROM pep.common_days_of_week cdw where cdw.id=pjpv.day_number) as SD_VISIT_DAY_NAME, pjpv.distributor_id as SD_DISTRIBUTOR_ID, (SELECT name FROM pep.common_distributors where distributor_id=pjpv.distributor_id) as SD_DISTRIBUTOR_LABEL, pjpv.outlet_id as SD_OUTLET_ID, (SELECT name FROM pep.common_outlets where id=pjpv.outlet_id) as SD_OUTLET_LABEL, (SELECT snd_id FROM pep.common_distributors where distributor_id=pjpv.distributor_id) as SD_SND_ID, (SELECT  users.DISPLAY_NAME   FROM users  WHERE users.ID=SD_SND_ID) as SD_SND_NAME, (SELECT rsm_id FROM pep.common_distributors where distributor_id=pjpv.distributor_id) as SD_RSM_ID, (SELECT  users.DISPLAY_NAME   FROM users  WHERE users.ID=SD_RSM_ID) as SD_RSM_NAME, (SELECT tdm_id FROM pep.common_distributors where distributor_id=pjpv.distributor_id) as SD_TDM_ID, (SELECT  users.DISPLAY_NAME   FROM users  WHERE users.ID=SD_TDM_ID) as SD_TDM_NAME, (SELECT region_id FROM pep.common_distributors where distributor_id=pjpv.distributor_id) as SD_REGION_ID, (SELECT  common_regions.region_name   FROM common_regions  WHERE common_regions.region_id=SD_REGION_ID) as SD_REGION_LABEL, pjpv.created_by AS SD_ORDER_BOOKER_ID, (SELECT CONCAT(`users`.`ID`, '-',`users`.`DISPLAY_NAME`) FROM  users  WHERE  users.ID = pjpv.created_by) AS SD_ORDER_BOOKER_NAME FROM pep.distributor_beat_plan_view pjpv WHERE pjpv.created_on between '"+StartDate+"' and '"+EndDate+"' AND  pjpv.distributor_id in ("+DistributorID+")  AND pjpv.distributor_id NOT IN(SELECT  distinct(isa.distributor_id) FROM  inventory_sales_adjusted isa WHERE  isa.distributor_id <> 123  AND isa.created_on BETWEEN '"+StartDate+"' AND '"+EndDate+"'AND  (isa.distributor_id IN ("+DistributorID+")))");
		while (rs.next()) {
			 
					XSSFRow headerrow2 = spreadsheet.createRow((short) RowCount);
					XSSFCell headercell2 = (XSSFCell) headerrow2.createCell((short) 0);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 0);
					headercell2.setCellValue(rs.getString("SD_CREATED_ON_DATE"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
					headercell2.setCellValue(rs.getString("SD_CREATED_ON_TIME"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 2);
					headercell2.setCellValue(rs.getString("SD_CREATED_ON_YEAR"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 3);
					headercell2.setCellValue(rs.getString("SD_CREATED_ON_MONTH"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 4);
					headercell2.setCellValue(rs.getString("SD_CREATED_ON_DAY"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 5);
					headercell2.setCellValue(rs.getString("SD_CREATED_ON_DAY_NAME"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 6);
					headercell2.setCellValue(rs.getString("SD_DISTRIBUTOR_ID"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 7);
					headercell2.setCellValue(rs.getString("SD_DISTRIBUTOR_LABEL"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 8);
					headercell2.setCellValue(rs.getString("SD_OUTLET_ID"));
					SetNormalCellBackColor(workbook, headercell2, style62);
					

					headercell2 = (XSSFCell) headerrow2.createCell((short) 9);
					headercell2.setCellValue(rs.getString("SD_OUTLET_LABEL"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 10);
					headercell2.setCellValue(rs.getString("SD_SND_ID"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 11);
					headercell2.setCellValue(rs.getString("SD_SND_NAME"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 12);
					headercell2.setCellValue(rs.getString("SD_RSM_ID"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 13);
					headercell2.setCellValue(rs.getString("SD_RSM_NAME"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 14);
					headercell2.setCellValue(rs.getString("SD_TDM_ID"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 15);
					headercell2.setCellValue(rs.getString("SD_TDM_NAME"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 16);
					headercell2.setCellValue(rs.getString("SD_REGION_ID"));
					SetNormalCellBackColor(workbook, headercell2, style62);
					

					headercell2 = (XSSFCell) headerrow2.createCell((short) 17);
					headercell2.setCellValue(rs.getString("SD_REGION_LABEL"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 18);
					headercell2.setCellValue(rs.getString("PJP_ID"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 19);
					headercell2.setCellValue(rs.getString("PJP_LABEL"));
					SetNormalCellBackColor(workbook, headercell2, style62);
					
					headercell2 = (XSSFCell) headerrow2.createCell((short) 20);
					headercell2.setCellValue(rs.getString("day_number"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell2 = (XSSFCell) headerrow2.createCell((short) 21);
					headercell2.setCellValue(rs.getString("SD_VISIT_DAY_NAME"));
					SetNormalCellBackColor(workbook, headercell2, style62);

					headercell1 = (XSSFCell) headerrow1.createCell((short) 22);
					headercell1.setCellValue("Order Booker ID");
					SetNormalCellBackColorH(workbook, headercell1, style61);

					headercell1 = (XSSFCell) headerrow1.createCell((short) 23);
					headercell1.setCellValue("Order Booker Name");
					SetNormalCellBackColorH(workbook, headercell1, style61);

					RowCount = RowCount + 1;
				
		}

		/*
		 * //Generated On
		 * 
		 * RowCount = RowCount+1;
		 * 
		 * //Printing Date
		 * 
		 * //Report Heading
		 * 
		 * XSSFRow rowPG = spreadsheet.createRow((short) RowCount+1);
		 * 
		 * XSSFCell cellPG = (XSSFCell) rowPG.createCell((short) 0);
		 * 
		 * 
		 * cellPG.setCellValue("Generated On: "+Utilities.getDisplayDateTimeFormat(new
		 * Date()));
		 * 
		 * spreadsheet.addMergedRegion(new CellRangeAddress( RowCount+1, //first row
		 * (0-based) RowCount+1, //last row (0-based) 0, //first column (0-based) 3
		 * //last column (0-based) ));
		 * 
		 * 
		 */

		// Auto Sizing Column

		for (int i = 0; i < 43; i++) {
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

	public void SetNormalCellBackColor(XSSFWorkbook workbook, XSSFCell headercell, XSSFCellStyle style61) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));

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

	public void SetNormalCellBackColorH(XSSFWorkbook workbook, XSSFCell headercell, XSSFCellStyle style61) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));

		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style61.setAlignment(CellStyle.ALIGN_LEFT);

		XSSFFont font = workbook.createFont();// Create font
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style61.setFont(font);// set it to bold

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

		// style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

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

}
