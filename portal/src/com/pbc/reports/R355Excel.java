package com.pbc.reports;

import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.itextpdf.text.Font;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

import java.awt.Color;
import java.io.File;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.*;

import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import java.math.BigInteger;

public class R355Excel {

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

	public R355Excel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6) {
			Yesterday = Utilities.getDateByDays(-2);
		}

	}

	public void createPdf(String filename, Date StartDate, String whereDistributorIDs)
			throws IOException, SQLException {

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("cell types");

		int FirstRowCount = 0;

		int RowCount = 0;

		// Report Heading

		XSSFRow rowH = spreadsheet.createRow((short) RowCount);
		XSSFCell cellH = (XSSFCell) rowH.createCell((short) 0);

		cellH.setCellValue("R355 - PSR Targets VS Attainment SKU Wise [Tons]");

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

		// System.out.println("Period: " + StartDate + " - " + EndDate);
		int month = Utilities.getMonthNumberByDate(StartDate);
		int year = Utilities.getYearByDate(StartDate);
		
		Date EndDate = Utilities.getEndDateByDate(StartDate);
		
		cellP.setCellValue(
				"Month : " + Utilities.getMonthNameByNumber(month) + " - Year : " + Utilities.getYearByDate(StartDate));
		System.out.println("month: " + month);
		spreadsheet.addMergedRegion(new CellRangeAddress(FirstRowCount, // first row (0-based)
				FirstRowCount, // last row (0-based)
				0, // first column (0-based)
				3 // last column (0-based)
		));

		RowCount = RowCount + 1;
		FirstRowCount = FirstRowCount + 1;
		XSSFCellStyle style61 = workbook.createCellStyle();

		int decrement = 0;
		ArrayList<String> labels = new ArrayList<String>();
		ArrayList<Integer> lrbIds = new ArrayList<Integer>();
		ResultSet rs2 = s.executeQuery("SELECT * from inventory_products_lrb_types where id!=3");
		while (rs2.next()) {
			int countZero = 0;
			ResultSet rsZero = s2.executeQuery(
					"select count(package_id) as c from inventory_products_view where lrb_type_id=" + rs2.getInt("id"));
			if (rsZero.first()) {
				countZero = rsZero.getInt("c");
			}
			if (countZero != 0) {
				lrbIds.add(rs2.getInt("id"));
				labels.add(rs2.getString("label"));
			} else {
				decrement++;
			}

		}

		ArrayList<Integer> lrbSpaces = new ArrayList<Integer>();
		for (int i = 0; i < lrbIds.size(); i++) {
			ResultSet rsLrbMrge = s.executeQuery(
					"select count(package_id) as c from inventory_products_view where lrb_type_id=" + lrbIds.get(i));
			while (rsLrbMrge.next()) {
				lrbSpaces.add(rsLrbMrge.getInt("c"));
			}
		}

		final XSSFRow rowP3 = spreadsheet.createRow((int) (short) RowCount);
		// XSSFCell cellGroupheader = rowP1.createCell(0);
		XSSFCell headercell = rowP3.createCell(0);
		headercell.setCellValue("");
		// System.out.println("Here 1");
		spreadsheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 5));
		this.Set2ndHeaderBackColor(workbook, headercell);

		// final XSSFRow rowP1 = spreadsheet.createRow((int) (short) RowCount);
		// XSSFCell cellGroupheader = rowP1.createCell(0);
		/*
		 * cellGroupheader.setCellValue(""); spreadsheet.addMergedRegion(new
		 * CellRangeAddress(0, 0, 0, 9)); this.Set2ndHeaderBackColor(workbook,
		 * cellGroupheader);
		 */

		// XSSFCell headercell = rowP3.createCell(0);
//		for(int i=0; i<lrbIds.size(); i++) {
//			System.out.println(lrbIds.get(i)+" - "+lrbSpaces.get(i));
//		}

		// System.out.println("Here 2");
		int LRBClo1 = 6, second = 0;
		for (int i = 0; i < lrbIds.size(); i++) {
			second = LRBClo1 + lrbSpaces.get(i);
			headercell = rowP3.createCell((int) (short) LRBClo1);
			headercell.setCellValue(labels.get(i));
			if (lrbSpaces.get(i) != 0) {
				spreadsheet.addMergedRegion(
						new CellRangeAddress(2, 2, LRBClo1, (i == lrbSpaces.size() - 1) ? second + 2 : second));
			}

			this.Set3rdHeaderBackColor(workbook, headercell);
			LRBClo1 = second + 1;
		}
		
		

		RowCount++;
	
		XSSFRow headerrow1 = spreadsheet.createRow((short) RowCount);
		XSSFCell headercell1 = (XSSFCell) headerrow1.createCell((short) 0);

		// System.out.println("Here 4");
		int productCol = 0;

		headercell1 = (XSSFCell) headerrow1.createCell((short) productCol);
		headercell1.setCellValue("PJP ID");
		SetNormalCellBackColorH(workbook, headercell1, style61);
		productCol++;

		headercell1 = (XSSFCell) headerrow1.createCell((short) productCol);
		headercell1.setCellValue("Employee ID");
		SetNormalCellBackColorH(workbook, headercell1, style61);
		productCol++;

		headercell1 = (XSSFCell) headerrow1.createCell((short) productCol);
		headercell1.setCellValue("TSO");
		SetNormalCellBackColorH(workbook, headercell1, style61);
		productCol++;

		headercell1 = (XSSFCell) headerrow1.createCell((short) productCol);
		headercell1.setCellValue("ASM");
		SetNormalCellBackColorH(workbook, headercell1, style61);
		productCol++;

		headercell1 = (XSSFCell) headerrow1.createCell((short) productCol);
		headercell1.setCellValue("Region");
		SetNormalCellBackColorH(workbook, headercell1, style61);
		productCol++;
		
		headercell1 = (XSSFCell) headerrow1.createCell((short) productCol);
		headercell1.setCellValue("");
		SetNormalCellBackColorH(workbook, headercell1, style61);
		productCol++;

		
		 int arrSize = 0;
		for (int i = 0; i < lrbIds.size(); i++) {
			ResultSet rsLrbMrge = s.executeQuery(
					"select concat(brand_label, '-',package_label) as product_name from inventory_products_view where lrb_type_id="
							+ lrbIds.get(i) + " order by package_id");
			while (rsLrbMrge.next()) {
				headercell1 = (XSSFCell) headerrow1.createCell((short) productCol);
				headercell1.setCellValue(rsLrbMrge.getString("product_name"));
				SetNormalCellBackColorH(workbook, headercell1, style61);
				productCol++;
				arrSize++;
			}
			headercell1 = (XSSFCell) headerrow1.createCell((short) productCol);
			headercell1.setCellValue("Total " + labels.get(i));
			this.Set2ndHeaderBackColorssSecondarySales_1(workbook, headercell1, style61);
			productCol++;
			arrSize++;
		}

		headercell1 = (XSSFCell) headerrow1.createCell((short) productCol);
		headercell1.setCellValue("Total Gurr");
		this.Set2ndHeaderBackColorssSecondarySales_brown(workbook, headercell1);
		productCol++;
		arrSize++;

		headercell1 = (XSSFCell) headerrow1.createCell((short) productCol);
		headercell1.setCellValue("Total Brand");
		this.Set2ndHeaderBackColorssSecondarySales_green(workbook, headercell1);
		productCol++;
		arrSize++;
		arrSize++;
		
		System.out.println("arrSize : "+arrSize);
		
		double grandTargetTotal[] = new double[arrSize];
		double grandAttainmentTotal[] = new double[arrSize];
		double grandPercentageTotal[] = new double[arrSize];
		
		for(int i=0; i<arrSize; i++) {
			grandTargetTotal[i] = 0.0;
			grandAttainmentTotal[i] = 0.0;
			grandPercentageTotal[i] = 0.0;
		}

		int RowEnd = RowCount + 2;
		System.out.println("RowCount " + RowCount);

		// Dynamic Work Start
		
	XSSFCellStyle style62 = workbook.createCellStyle();
		
		System.out.println("select id, pjp_id, employee_id, (select DISPLAY_NAME from users where id=employee_id) as name from employee_targets where month="+month+" and year="+year);
		ResultSet rsMainQuery = s.executeQuery("select id, pjp_id, employee_id, (select DISPLAY_NAME from users where id=employee_id) as name from employee_targets where month="+month+" and year="+year);
		while(rsMainQuery.next()) {
			double TargetTotal[] = new double[arrSize];
			double AttainmentTotal[] = new double[arrSize];
			RowCount++;
			RowCount++;
			int colTarget = 0;
			
			int targetId = rsMainQuery.getInt("id");
			int pjpId = rsMainQuery.getInt("pjp_id");
			long EmployeeID = rsMainQuery.getInt("employee_id");
			long distributorId=0;
			
			String tso="";
			ResultSet rsNames = s2.executeQuery("select  (select DISPLAY_NAME from users where ID=asm_id) as TSO,distributor_id from distributor_beat_plan_view where id="+pjpId);
			if(rsNames.first()) {
				 tso=rsNames.getString("TSO");
				 distributorId=rsNames.getLong("distributor_id");
			}
			
			String asm="", region="";
			ResultSet rsASM = s2.executeQuery("select (select region_name from common_regions r where r.region_id=cd.region_id) as Region,(select DISPLAY_NAME from users where ID=rsm_id) as ASM from common_distributors cd where distributor_id="+distributorId);
			if(rsASM.first()) {
				asm=rsASM.getString("ASM");
				region=rsASM.getString("Region");
			}
			
			tso = tso == null ? "" : tso;
			asm = asm == null ? "" : asm;
			
			String employeeName = (EmployeeID == 0 ) ? "" : Utilities.truncateStringToMax((EmployeeID + " - "+ rsMainQuery.getString("name")),30);

			final XSSFRow headerrow222 = spreadsheet.createRow((short) RowCount);
			
			XSSFCell headercel = (XSSFCell) headerrow222.createCell((short) colTarget);
			headercel.setCellValue(pjpId);
			SetNormalCellBackColorH(workbook, headercel, style61);
			colTarget++;
			
			headercel = (XSSFCell) headerrow222.createCell((short) colTarget);
			headercel.setCellValue(employeeName);
			SetNormalCellBackColorH(workbook, headercel, style61);
			colTarget++;

			headercel = (XSSFCell) headerrow222.createCell((short) colTarget);
			headercel.setCellValue(tso);
			SetNormalCellBackColorH(workbook, headercel, style61);
			colTarget++;

			headercel = (XSSFCell) headerrow222.createCell((short) colTarget);
			headercel.setCellValue(asm);
			SetNormalCellBackColorH(workbook, headercel, style61);
			colTarget++;

			headercel = (XSSFCell) headerrow222.createCell((short) colTarget);
			headercel.setCellValue(region);
			SetNormalCellBackColorH(workbook, headercel, style61);
			colTarget++;
			// Target

			XSSFCell headerceld = (XSSFCell) headerrow222.createCell((short) colTarget);
			// final XSSFRow headerrowTarget = spreadsheet.createRow((short) RowCount);
			headerceld = (XSSFCell) headerrow222.createCell((short) colTarget);
			headerceld.setCellValue("Target");
			this.SetNormalCellBackColor(workbook, headerceld, style61);
			colTarget++;
			
	
		// Target

		
		// final XSSFRow headerrowTarget = spreadsheet.createRow((short) RowCount);
			double rowTargetTotal=0.0, gurrTargetTotal =0.0;
			int increment = 0;
			for (int i = 0; i < lrbIds.size(); i++) {
				double total = 0.0;
				ResultSet rsProduct = s2.executeQuery(
						"select package_id, brand_id, liquid_in_ml, unit_per_sku from inventory_products_view where lrb_type_id="+ lrbIds.get(i) + " order by package_id");
				while (rsProduct.next()) {
					double quantity = 0.0;
					//System.out.println("select sum(quantity * "+rsProduct.getDouble("liquid_in_ml")+") as q from employee_targets_packages_brands where id in (select id from employee_targets where month="+month+" and year="+year+" and pjp_id="+pjpId+" and employee_id="+EmployeeID+") and package_id="+rsProduct.getInt("package_id")+" and brand_id="+rsProduct.getInt("brand_id"));
					ResultSet rsQuantity = s3.executeQuery("select sum(quantity * "+rsProduct.getDouble("liquid_in_ml")+" * "+rsProduct.getInt("unit_per_sku")+") as q from employee_targets_packages_brands where id in (select id from employee_targets where month="+month+" and year="+year+" and pjp_id="+pjpId+" and employee_id="+EmployeeID+") and package_id="+rsProduct.getInt("package_id")+" and brand_id="+rsProduct.getInt("brand_id"));
					if(rsQuantity.first()) {
						quantity = rsQuantity.getDouble("q");
					}
					headerceld = (XSSFCell) headerrow222.createCell((short) colTarget);
					headerceld.setCellValue(quantity);
					grandTargetTotal[increment] = grandTargetTotal[increment] + quantity;
					TargetTotal[increment] = TargetTotal[increment] + quantity;
					this.SetNormalCellBackColor_1(workbook, headerceld, style61);
					colTarget++;
					total = total+quantity;
					
					increment++;
				}
				
				rowTargetTotal += total;
				headerceld = (XSSFCell) headerrow222.createCell((short) colTarget);
				headerceld.setCellValue(total);
				grandTargetTotal[increment] = grandTargetTotal[increment] + total;
				TargetTotal[increment] = TargetTotal[increment] + total;
				increment++;
				Set2ndHeaderBackColorssSecondarySales(workbook, headerceld);
				colTarget++;
				
				
				if(lrbIds.get(i) == 8 || lrbIds.get(i) == 9) {
					gurrTargetTotal += total;
				}
				
			}
			

		headercell1 = (XSSFCell) headerrow222.createCell((short) colTarget);
		headercell1.setCellValue(gurrTargetTotal);
		this.Set2ndHeaderBackColorssSecondarySales_brown(workbook, headercell1);
		colTarget++;
		grandTargetTotal[increment] = grandTargetTotal[increment] + gurrTargetTotal;
		TargetTotal[increment] = TargetTotal[increment] + gurrTargetTotal;
		increment++;

		headerceld = (XSSFCell) headerrow222.createCell((short) colTarget);
		headerceld.setCellValue(rowTargetTotal);
		this.Set2ndHeaderBackColorssSecondarySales_green(workbook, headerceld);
		colTarget++;
		grandTargetTotal[increment] = grandTargetTotal[increment] + rowTargetTotal;
		TargetTotal[increment] = TargetTotal[increment] + rowTargetTotal;

		
		
		
	// Achievement

		
		// final XSSFRow headerrowTarget = spreadsheet.createRow((short) RowCount);
		
			double rowAchievementTotal=0.0, gurrAchievementTotal =0.0;
			 increment = 0;
			RowCount++;
			int colAttainment = 6;
			final XSSFRow headerrowAttainment = spreadsheet.createRow((short) RowCount);
			//  headerceld = (XSSFCell) headerrowAttainment.createCell((short) colAttainment);
			for (int i = 0; i < lrbIds.size(); i++) {
			
				double total = 0.0;
				ResultSet rsProduct = s2.executeQuery(
						"select package_id,product_id, brand_id, liquid_in_ml, unit_per_sku from inventory_products_view where lrb_type_id="+ lrbIds.get(i) + " order by package_id");
				while (rsProduct.next()) {
					double quantity = 0.0;
					/*
					 * System.out.println(
					 * "SELECT sum(total_units * "+rsProduct.getDouble("liquid_in_ml")+" * "
					 * +rsProduct.getInt("unit_per_sku")
					 * +") as q FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id  where isa.adjusted_on between "
					 * + Utilities.getSQLDate(StartDate) + " and " +
					 * Utilities.getSQLDateNext(EndDate) + " and isap.product_id=" +
					 * rsProduct.getInt("product_id") + "  order by isa.id desc");
					 */
					ResultSet rsQuantity = s3.executeQuery(
							"SELECT sum(raw_cases*(SELECT liquid_in_ml*unit_per_sku FROM inventory_products_view v where product_id="
									+ rsProduct.getInt("product_id")
									+ ")) as q FROM pep.inventory_sales_adjusted_products where product_id="
									+ rsProduct.getInt("product_id")
									+ " and id in(SELECT id FROM pep.inventory_sales_adjusted where adjusted_on between "
									+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate)
									+ " and booked_by=" + EmployeeID + ")");
					if(rsQuantity.first()) {
						quantity = rsQuantity.getDouble("q");
					}
					headerceld = (XSSFCell) headerrowAttainment.createCell((short) colAttainment);
					headerceld.setCellValue(quantity);
					grandTargetTotal[increment] = grandTargetTotal[increment] + quantity;
					AttainmentTotal[increment] = AttainmentTotal[increment] + quantity;
					this.SetNormalCellBackColor_1(workbook, headerceld, style61);
					colAttainment++;
					total = total+quantity;
					
					increment++;
				}
				
				rowAchievementTotal += total;
				headerceld = (XSSFCell) headerrowAttainment.createCell((short) colAttainment);
				headerceld.setCellValue(total);
				grandAttainmentTotal[increment] = grandAttainmentTotal[increment] + total;
				AttainmentTotal[increment] = AttainmentTotal[increment] + total;
				increment++;
				Set2ndHeaderBackColorssSecondarySales(workbook, headerceld);
				colAttainment++;
				
				
				int colPercentage = 6;
				final XSSFRow headerrowPercentage = spreadsheet.createRow((short) RowCount);
				if(lrbIds.get(i) == 8 || lrbIds.get(i) == 9) {
					gurrAchievementTotal += total;
				}
				
			}
			

		headercell1 = (XSSFCell) headerrowAttainment.createCell((short) colAttainment);
		headercell1.setCellValue(gurrAchievementTotal);
		this.Set2ndHeaderBackColorssSecondarySales_brown(workbook, headercell1);
		colAttainment++;
		grandAttainmentTotal[increment] = grandAttainmentTotal[increment] + gurrAchievementTotal;
		AttainmentTotal[increment] = AttainmentTotal[increment] + gurrAchievementTotal;
		increment++;

		headerceld = (XSSFCell) headerrowAttainment.createCell((short) colAttainment);
		headerceld.setCellValue(rowAchievementTotal);
		this.Set2ndHeaderBackColorssSecondarySales_green(workbook, headerceld);
		colAttainment++;
		grandAttainmentTotal[increment] = grandAttainmentTotal[increment] + rowAchievementTotal;
		AttainmentTotal[increment] = AttainmentTotal[increment] + rowAchievementTotal;
		
		increment = 0;
		int colPercentage=6;
		RowCount++;
		
		final XSSFRow headerrowPercentage = spreadsheet.createRow((short) RowCount);
		double total=0;
		for(int i=0; i<(TargetTotal.length-3); i++) {
			double percentageValue = ((TargetTotal[i] !=0 ) ? (AttainmentTotal[i]*100/TargetTotal[i]) : 0);
			headerceld = (XSSFCell) headerrowPercentage.createCell((short) colPercentage);
			headerceld.setCellValue((percentageValue) + "%");
			grandPercentageTotal[increment] = grandPercentageTotal[increment] + percentageValue;
		
			this.SetNormalCellBackColor_1(workbook, headerceld, style61);
			colAttainment++;
			total = total+percentageValue;
			
			increment++;
		}
		
		int gurrIndex = TargetTotal.length - 3;
		int totalIndex = TargetTotal.length - 2;
		double gurrPercentage = ((TargetTotal[gurrIndex] !=0 ) ? (AttainmentTotal[gurrIndex]*100/TargetTotal[gurrIndex]) : 0);
		double totalPercentage = ((TargetTotal[totalIndex] !=0 ) ? (AttainmentTotal[totalIndex]*100/TargetTotal[totalIndex]) : 0);
		
		
		
		headercell1 = (XSSFCell) headerrowAttainment.createCell((short) colAttainment);
		headercell1.setCellValue(gurrPercentage + "%");
		this.Set2ndHeaderBackColorssSecondarySales_brown(workbook, headercell1);
		colAttainment++;
		grandPercentageTotal[increment] = grandPercentageTotal[increment] + gurrPercentage;
		increment++;

		headerceld = (XSSFCell) headerrowAttainment.createCell((short) colAttainment);
		headerceld.setCellValue(totalPercentage + "%");
		this.Set2ndHeaderBackColorssSecondarySales_green(workbook, headerceld);
		colAttainment++;
		grandPercentageTotal[increment] = grandPercentageTotal[increment] + totalPercentage;

		}
		
		
		
		
		RowCount++;
		RowCount++;
		 XSSFRow headerrowGrand = spreadsheet.createRow((short) RowCount);
		XSSFCell headercellGrand = (XSSFCell) headerrowGrand.createCell((short) 0);
		XSSFCellStyle style63 = workbook.createCellStyle();

		int GcolTarget = 0;
		
		headercellGrand = (XSSFCell) headerrowGrand.createCell((short) GcolTarget);
		headercellGrand.setCellValue("");
		SetNormalCellBackColorH_yellow(workbook, headercellGrand, style63);
		GcolTarget++;
		
		headercellGrand = (XSSFCell) headerrowGrand.createCell((short) GcolTarget);
		headercellGrand.setCellValue("");
		SetNormalCellBackColorH_yellow(workbook, headercellGrand, style63);
		GcolTarget++;
		
		headercellGrand = (XSSFCell) headerrowGrand.createCell((short) GcolTarget);
		headercellGrand.setCellValue("");
		SetNormalCellBackColorH_yellow(workbook, headercellGrand, style63);
		GcolTarget++;
		
		headercellGrand = (XSSFCell) headerrowGrand.createCell((short) GcolTarget);
		headercellGrand.setCellValue("");
		SetNormalCellBackColorH_yellow(workbook, headercellGrand, style63);
		GcolTarget++;
		
		headercellGrand = (XSSFCell) headerrowGrand.createCell((short) GcolTarget);
		headercellGrand.setCellValue("Grand Total");
		SetNormalCellBackColorH_yellow(workbook, headercellGrand, style63);
		GcolTarget++;
		
		// final XSSFRow headerrowTarget = spreadsheet.createRow((short) RowCount);
		headercellGrand = (XSSFCell) headerrowGrand.createCell((short) GcolTarget);
		headercellGrand.setCellValue("Target");
		SetNormalCellBackColorH_yellow(workbook, headercellGrand, style63);
		GcolTarget++;

		//System.out.println("lll : "+grandTargetTotal.length);
		for (int i = 0; i <(grandTargetTotal.length-3); i++) {
			headercellGrand = (XSSFCell) headerrowGrand.createCell((short) GcolTarget);
			headercellGrand.setCellValue(grandTargetTotal[i]);
			SetNormalCellBackColorH_yellow(workbook, headercellGrand, style63);
			GcolTarget++;
		}


		headercell1 = (XSSFCell) headerrowGrand.createCell((short) GcolTarget);
		headercell1.setCellValue(grandTargetTotal[arrSize-3]);
		this.Set2ndHeaderBackColorssSecondarySales_brown(workbook, headercell1);
		GcolTarget++;

		headercellGrand = (XSSFCell) headerrowGrand.createCell((short) GcolTarget);
		headercellGrand.setCellValue(grandTargetTotal[arrSize-2]);
		this.Set2ndHeaderBackColorssSecondarySales_green(workbook, headercellGrand);
		GcolTarget++;
		
		
	//	final XSSFRow headerrowGrand = spreadsheet.createRow((short) RowCount);
		//XSSFCell headercellGrand = (XSSFCell) headerrowGrand.createCell((short) 0);
		//XSSFCellStyle style63 = workbook.createCellStyle();

		RowCount++;
		 headerrowGrand = spreadsheet.createRow((short) RowCount);
		 GcolTarget = 0;
		
		headercellGrand = (XSSFCell) headerrowGrand.createCell((short) GcolTarget);
		headercellGrand.setCellValue("");
		SetNormalCellBackColorH_yellow(workbook, headercellGrand, style63);
		GcolTarget++;
		
		headercellGrand = (XSSFCell) headerrowGrand.createCell((short) GcolTarget);
		headercellGrand.setCellValue("");
		SetNormalCellBackColorH_yellow(workbook, headercellGrand, style63);
		GcolTarget++;
		
		headercellGrand = (XSSFCell) headerrowGrand.createCell((short) GcolTarget);
		headercellGrand.setCellValue("");
		SetNormalCellBackColorH_yellow(workbook, headercellGrand, style63);
		GcolTarget++;
		
		headercellGrand = (XSSFCell) headerrowGrand.createCell((short) GcolTarget);
		headercellGrand.setCellValue("");
		SetNormalCellBackColorH_yellow(workbook, headercellGrand, style63);
		GcolTarget++;
		
		headercellGrand = (XSSFCell) headerrowGrand.createCell((short) GcolTarget);
		headercellGrand.setCellValue("");
		SetNormalCellBackColorH_yellow(workbook, headercellGrand, style63);
		GcolTarget++;
		
		
		
		// final XSSFRow headerrowTarget = spreadsheet.createRow((short) RowCount);
		headercellGrand = (XSSFCell) headerrowGrand.createCell((short) GcolTarget);
		headercellGrand.setCellValue("Attainment");
		SetNormalCellBackColorH_yellow(workbook, headercellGrand, style63);
		GcolTarget++;

		//System.out.println("lll : "+grandTargetTotal.length);
		for (int i = 0; i <(grandAttainmentTotal.length-3); i++) {
			headercellGrand = (XSSFCell) headerrowGrand.createCell((short) GcolTarget);
			headercellGrand.setCellValue(grandAttainmentTotal[i]);
			SetNormalCellBackColorH_yellow(workbook, headercellGrand, style63);
			GcolTarget++;
		}


		headercell1 = (XSSFCell) headerrowGrand.createCell((short) GcolTarget);
		headercell1.setCellValue(grandAttainmentTotal[arrSize-3]);
		this.Set2ndHeaderBackColorssSecondarySales_brown(workbook, headercell1);
		GcolTarget++;

		headercellGrand = (XSSFCell) headerrowGrand.createCell((short) GcolTarget);
		headercellGrand.setCellValue(grandAttainmentTotal[arrSize-2]);
		this.Set2ndHeaderBackColorssSecondarySales_green(workbook, headercellGrand);
		GcolTarget++;

		
		// Auto Sizing Column grandAttainmentTotal

		for (int i = 0; i < 90; i++) {
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

	public void Set2ndHeaderBackColorssSecondarySales(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(0, 0, 255));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setAlignment(HorizontalAlignment.CENTER);
		style61.setVerticalAlignment(VerticalAlignment.CENTER);
		style61.setWrapText(true);
		headercell.setCellStyle((CellStyle) style61);
		final XSSFFont font = workbook.createFont();
		font.setFontName("Calibri");
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont((org.apache.poi.ss.usermodel.Font) font);
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
	}
	
	public void Set2ndHeaderBackColorssSecondarySales_1(final XSSFWorkbook workbook, final XSSFCell headercell, XSSFCellStyle style61) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(0, 0, 255));
		
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setAlignment(HorizontalAlignment.CENTER);
		style61.setVerticalAlignment(VerticalAlignment.CENTER);
		style61.setWrapText(true);
		headercell.setCellStyle((CellStyle) style61);
		final XSSFFont font = workbook.createFont();
		font.setFontName("Calibri");
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont((org.apache.poi.ss.usermodel.Font) font);
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
	}

	public void Set2ndHeaderBackColorssSecondarySales_brown(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(165, 42, 42));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setAlignment(HorizontalAlignment.CENTER);
		style61.setVerticalAlignment(VerticalAlignment.CENTER);
		style61.setWrapText(true);
		headercell.setCellStyle((CellStyle) style61);
		final XSSFFont font = workbook.createFont();
		font.setFontName("Calibri");
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont((org.apache.poi.ss.usermodel.Font) font);
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
	}

	public void Set2ndHeaderBackColorssSecondarySales_green(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(0, 128, 0));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setAlignment(HorizontalAlignment.CENTER);
		style61.setVerticalAlignment(VerticalAlignment.CENTER);
		style61.setWrapText(true);
		headercell.setCellStyle((CellStyle) style61);
		final XSSFFont font = workbook.createFont();
		font.setFontName("Calibri");
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont((org.apache.poi.ss.usermodel.Font) font);
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
	}

	public void Set2ndHeaderBackColor_w(XSSFWorkbook workbook, XSSFCell headercell) {

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

		style61.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style61);

		XSSFFont font = workbook.createFont();
		font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		// font.setFontHeightInPoints((short)10);
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont(font);
	}

	public void Set2ndHeaderBackColor(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(162, 162, 163));

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

//		style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
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

	public void SetNormalCellBlueColor(XSSFWorkbook workbook, XSSFCell headercell, XSSFCellStyle style61) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));
		XSSFColor fontColor = new XSSFColor(new java.awt.Color(246, 253, 253));

		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style61.setAlignment(CellStyle.ALIGN_LEFT);
		headercell.setCellStyle(style61);

		XSSFFont font = workbook.createFont();// Create font
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(fontColor);
		style61.setFont(font);// set it to bold

		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

		headercell.setCellStyle(style61);
	}

	public void SetNormalCellBackColor_1(final XSSFWorkbook workbook, final XSSFCell headercell,
			XSSFCellStyle style61) {

		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(255, 255, 255));

		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 3);
		headercell.setCellStyle((CellStyle) style61);
		style61.setAlignment(HorizontalAlignment.CENTER);
		style61.setVerticalAlignment(VerticalAlignment.CENTER);
		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.##"));
		headercell.setCellStyle((CellStyle) style61);
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

		// style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

		headercell.setCellStyle(style61);
	}

	public void SetNormalCellBackColorH_yellow(XSSFWorkbook workbook, XSSFCell headercell, XSSFCellStyle style61) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 0));

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

		// style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

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

//	public XWPFHyperlinkRun createHyperlinkRunToAnchor(XWPFParagraph paragraph, String anchor) throws Exception {
//		CTHyperlink cthyperLink = paragraph.getCTP().addNewHyperlink();
//		cthyperLink.setAnchor(anchor);
//		cthyperLink.addNewR();
//		return new XWPFHyperlinkRun(cthyperLink, cthyperLink.getRArray(0), paragraph);
//	}
//
//	static XWPFParagraph createBookmarkedParagraph(XWPFDocument document, String anchor, int bookmarkId) {
//		XWPFParagraph paragraph = document.createParagraph();
//		CTBookmark bookmark = paragraph.getCTP().addNewBookmarkStart();
//		bookmark.setName(anchor);
//		bookmark.setId(BigInteger.valueOf(bookmarkId));
//		XWPFRun run = paragraph.createRun();
//		paragraph.getCTP().addNewBookmarkEnd().setId(BigInteger.valueOf(bookmarkId));
//		return paragraph;
//	}

}
