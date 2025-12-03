// 
// Decompiled by Procyon v0.5.36
// 

package com.pbc.reports;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFColor;
import java.awt.Color;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import java.sql.ResultSet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.sql.SQLException;
import com.pbc.util.Utilities;

import java.util.ArrayList;
import java.util.Date;
import java.sql.Statement;
import com.pbc.util.Datasource;
import org.apache.commons.lang3.time.DateUtils;
import com.pbc.inventory.StockPosting;

public class Report343UniqueProductivityShopsExcel {
	int FeatureID;
	public static final String RESULT;
	Datasource ds;
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	Statement s5;

	Date StartDate;
	Date EndDate;
	Date Yesterday;
	long SND_ID;

	static {
		RESULT = String.valueOf(Utilities.getCommonFilePath()) + "/DistributorSale.xls";
	}

	public static void main(final String[] args) throws Exception {
	}

	public Report343UniqueProductivityShopsExcel()
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		this.FeatureID = 419;
		this.ds = new Datasource();
		this.StartDate = Utilities.getDateByDays(-1);
		this.EndDate = Utilities.getDateByDays(0);
		this.Yesterday = Utilities.getDateByDays(-2);
		this.SND_ID = 0L;
		this.ds.createConnection();
		this.s = this.ds.createStatement();
		this.s2 = this.ds.createStatement();
		this.s3 = this.ds.createStatement();
		this.s4 = this.ds.createStatement();
		this.s5 = this.ds.createStatement();

		if (Utilities.getDayOfWeekByDate(this.Yesterday) == 6) {
			this.Yesterday = Utilities.getDateByDays(-3);
		}
	}

	public void createExcel(final String filename, final Date StartDate, final Date EndDate, final long SessionUserID,
			final String RSMIDs, final String SNDIDs) throws Exception {
		final XSSFWorkbook workbook = new XSSFWorkbook();
		final XSSFSheet spreadsheet = workbook.createSheet();
		int FirstRowCount = 0;
		int RowCount = 0;
		final CellStyle style = (CellStyle) workbook.createCellStyle();
		final XSSFFont font = workbook.createFont();

		final XSSFCellStyle style2 = workbook.createCellStyle();
		style2.setAlignment((short) 3);

		int count = 0;
		ArrayList<String> labels = new ArrayList<String>();
		ArrayList<Integer> lrbIds = new ArrayList<Integer>();
		ResultSet rs2 = s.executeQuery("SELECT * from inventory_products_lrb_types");
		while (rs2.next()) {
			lrbIds.add(rs2.getInt("id"));
			labels.add(rs2.getString("label"));
			count++;
		}

		Date CurrentDate = StartDate;
		int Serial = 0;
		final XSSFRow headerrow = spreadsheet.createRow((int) (short) Serial);

		XSSFCell headercell = headerrow.createCell(Serial);
		headercell.setCellValue("Start Date");
		this.Set2ndHeaderBackColor(workbook, headercell);
		++Serial;
		headercell = headerrow.createCell((int) (short) Serial);
		headercell.setCellValue("End Date");
		this.Set2ndHeaderBackColor(workbook, headercell);
		++Serial;
		headercell = headerrow.createCell((int) (short) Serial);
		headercell.setCellValue("Code");
		this.Set2ndHeaderBackColor(workbook, headercell);
		++Serial;
		headercell = headerrow.createCell((int) (short) Serial);
		headercell.setCellValue("Dist. Name");
		this.Set2ndHeaderBackColor(workbook, headercell);
		++Serial;
		headercell = headerrow.createCell((int) (short) Serial);
		headercell.setCellValue("Town");
		this.Set2ndHeaderBackColor(workbook, headercell);
		++Serial;
		headercell = headerrow.createCell((int) (short) Serial);
		headercell.setCellValue("TSO");
		this.Set2ndHeaderBackColor(workbook, headercell);
		++Serial;
		headercell = headerrow.createCell((int) (short) Serial);
		headercell.setCellValue("ASM");
		this.Set2ndHeaderBackColor(workbook, headercell);
		++Serial;
		headercell = headerrow.createCell((int) (short) Serial);
		headercell.setCellValue("RSM");
		this.Set2ndHeaderBackColor(workbook, headercell);
		++Serial;
		headercell = headerrow.createCell((int) (short) Serial);
		headercell.setCellValue("PJP ID");
		this.Set2ndHeaderBackColor(workbook, headercell);
		++Serial;
		headercell = headerrow.createCell((int) (short) Serial);
		headercell.setCellValue("PJP Name");
		this.Set2ndHeaderBackColor(workbook, headercell);
		++Serial;
		headercell = headerrow.createCell((int) (short) Serial);
		headercell.setCellValue("Order Booker");
		this.Set2ndHeaderBackColor(workbook, headercell);
		++Serial;
		headercell = headerrow.createCell((int) (short) Serial);
		headercell.setCellValue("Oultet Id");
		this.Set2ndHeaderBackColor(workbook, headercell);
		++Serial;
		headercell = headerrow.createCell((int) (short) Serial);
		headercell.setCellValue("Oultet Name");
		this.Set2ndHeaderBackColor(workbook, headercell);
		++Serial;
		headercell = headerrow.createCell((int) (short) Serial);
		headercell.setCellValue("Region");
		this.Set2ndHeaderBackColor(workbook, headercell);
		++Serial;

		for (int i = 0; i < labels.size(); i++) {
			headercell = headerrow.createCell((int) (short) Serial);
			headercell.setCellValue(labels.get(i));
			this.Set2ndHeaderBackColorsSecondary(workbook, headercell);
			++Serial;

		}

		headercell = headerrow.createCell((int) (short) Serial);
		headercell.setCellValue("TOTAL");
		this.Set2ndHeaderBackColorsFloorStock(workbook, headercell);

		++Serial;

		int SerialColumns1 = 0;

		final XSSFRow headerrow222 = spreadsheet.createRow((short) RowCount + 1);
		XSSFCell headercell22 = headerrow222.createCell(3);
		headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
		headercell22.setCellValue("");
		this.Set2ndHeaderBackColor(workbook, headercell22);
		++SerialColumns1;
		headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
		headercell22.setCellValue("");
		this.Set2ndHeaderBackColor(workbook, headercell22);
		++SerialColumns1;
		headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
		headercell22.setCellValue("");
		this.Set2ndHeaderBackColor(workbook, headercell22);
		++SerialColumns1;
		headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
		headercell22.setCellValue("");
		this.Set2ndHeaderBackColor(workbook, headercell22);
		++SerialColumns1;
		headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
		headercell22.setCellValue("");
		this.Set2ndHeaderBackColor(workbook, headercell22);
		++SerialColumns1;
		headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
		headercell22.setCellValue("");
		this.Set2ndHeaderBackColor(workbook, headercell22);
		++SerialColumns1;
		headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
		headercell22.setCellValue("");
		this.Set2ndHeaderBackColor(workbook, headercell22);
		++SerialColumns1;
		headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
		headercell22.setCellValue("");
		this.Set2ndHeaderBackColor(workbook, headercell22);
		++SerialColumns1;
		headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
		headercell22.setCellValue("");
		this.Set2ndHeaderBackColor(workbook, headercell22);
		++SerialColumns1;
		headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
		headercell22.setCellValue("");
		this.Set2ndHeaderBackColor(workbook, headercell22);
		++SerialColumns1;
		headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
		headercell22.setCellValue("");
		this.Set2ndHeaderBackColor(workbook, headercell22);
		++SerialColumns1;
		headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
		headercell22.setCellValue("");
		this.Set2ndHeaderBackColor(workbook, headercell22);
		++SerialColumns1;
		headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
		headercell22.setCellValue("");
		this.Set2ndHeaderBackColor(workbook, headercell22);
		++SerialColumns1;
		headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
		headercell22.setCellValue("");
		this.Set2ndHeaderBackColor(workbook, headercell22);
		++SerialColumns1;

		for (int i = 0; i < labels.size() + 1; i++) {

			headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
			headercell22.setCellValue("U.P");

			this.Set2ndHeaderBackColorsSecondary(workbook, headercell22);
			++SerialColumns1;

		}
		XSSFCell headercell2;
		int SerialColumns = 0;
		long Distributor_id = 0;
		String RSM = "", TSO = "", ASM = "", Region = "", whereRSM = "", whereSND = "";
		if (!RSMIDs.equals("")) {
			whereRSM = " and asm_id in (" + RSMIDs + ")";
		}

		if (!SNDIDs.equals("")) {
			whereSND = " and snd_id in (" + SNDIDs + ")";
		}

//		System.out.println(
//				"SELECT distributor_id,name,snd_id,city,tdm_id,rsm_id,region_id FROM pep.common_distributors where rsm_id in ("
//						+ RSMIDs + ")");
		/*
		 * System.out.println(
		 * "SELECT distributor_id,name,snd_id,city,tdm_id,rsm_id,region_id FROM pep.common_distributors where 1=1 "
		 * + whereSND);
		 */
		ResultSet rs13 = s.executeQuery(
				"SELECT distributor_id,name,snd_id,city,tdm_id,rsm_id,region_id FROM pep.common_distributors where 1=1 "
						+ whereSND);
		while (rs13.next()) {

			Distributor_id = rs13.getLong("distributor_id");

			// System.out.println("Select region_name from common_regions where region_id="
			// + rs13.getInt("region_id"));
			ResultSet rs151 = s2
					.executeQuery("Select region_name from common_regions where region_id=" + rs13.getInt("region_id"));
			if (rs151.next()) {
				Region = rs151.getString("region_name");
			}

			// System.out.println("SELECT DISPLAY_NAME FROM users where
			// users.ID="+rs13.getInt("rsm_id"));
			ResultSet rsRSM = s2.executeQuery("SELECT DISPLAY_NAME FROM users where users.ID=" + rs13.getInt("rsm_id"));
			if (rsRSM.next()) {
				ASM = rsRSM.getString("DISPLAY_NAME");
			}

//			System.out.println("SELECT DISPLAY_NAME FROM users where users.ID="+rs13.getInt("snd_id"));
			ResultSet rsSND = s2.executeQuery("SELECT DISPLAY_NAME FROM users where users.ID=" + rs13.getInt("snd_id"));
			if (rsSND.first()) {
				RSM = rsSND.getString("DISPLAY_NAME");
			}

			/*
			 * System.out.println(
			 * "SELECT id, label, created_on, outlet_id, asm_id,(SELECT DISPLAY_NAME FROM pep.users where id=assigned_to) as order_booker,(SELECT name FROM pep.common_outlets where id=outlet_id) as outlet_name, (SELECT DISPLAY_NAME FROM users where users.ID=asm_id) as ASMName FROM pep.distributor_beat_plan_view where distributor_id="
			 * + Distributor_id + " and assigned_on between " +
			 * Utilities.getSQLDate(StartDate) + " and " +
			 * Utilities.getSQLDateNext(EndDate)); ResultSet rs14 = s2.executeQuery(
			 * "SELECT id, label, created_on, outlet_id, asm_id,(SELECT DISPLAY_NAME FROM pep.users where id=assigned_to) as order_booker,(SELECT name FROM pep.common_outlets where id=outlet_id) as outlet_name, (SELECT DISPLAY_NAME FROM users where users.ID=asm_id) as ASMName FROM pep.distributor_beat_plan_view where distributor_id="
			 * + Distributor_id + " and assigned_on between " +
			 * Utilities.getSQLDate(StartDate) + " and " +
			 * Utilities.getSQLDateNext(EndDate)); while (rs14.next()) { TSO =
			 * rs14.getString("ASMName");
			 */

			/*
			 * System.out.
			 * println("SELECT distinct outlet_id FROM pep.distributor_beat_plan_view  where distributor_id="
			 * + Distributor_id + " and  assigned_on between " +
			 * Utilities.getSQLDate(StartDate) + " and " +
			 * Utilities.getSQLDateNext(EndDate));
			 */
			ResultSet rs15 = s2
					.executeQuery("SELECT distinct outlet_id FROM pep.distributor_beat_plan_view  where distributor_id="
							+ Distributor_id + " and  assigned_on between " + Utilities.getSQLDate(StartDate) + " and "
							+ Utilities.getSQLDateNext(EndDate));
			while (rs15.next()) {
			//	System.out.println("SELECT id, label, created_on, outlet_id, asm_id,(SELECT DISPLAY_NAME FROM pep.users where id=assigned_to) as order_booker,(SELECT name FROM pep.common_outlets where id=outlet_id) as outlet_name, (SELECT DISPLAY_NAME FROM users where users.ID=asm_id) as ASMName FROM pep.distributor_beat_plan_view where outlet_id="+rs15.getInt("outlet_id"));
				ResultSet rs14 = s3.executeQuery("SELECT id, label, created_on, outlet_id, asm_id,(SELECT DISPLAY_NAME FROM pep.users where id=assigned_to) as order_booker,(SELECT name FROM pep.common_outlets where id=outlet_id) as outlet_name, (SELECT DISPLAY_NAME FROM users where users.ID=asm_id) as ASMName FROM pep.distributor_beat_plan_view where outlet_id="+rs15.getInt("outlet_id"));
				if (rs14.first()) {

						TSO = rs14.getString("ASMName");

						SerialColumns = 0;

						XSSFRow headerrow22 = spreadsheet.createRow((short) RowCount + 1);
						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(Utilities.getDisplayDateFormat(StartDate));
						this.SetNormalCellBackColor(workbook, headercell2);
						++SerialColumns;

						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(Utilities.getDisplayDateFormat(EndDate));
						this.SetNormalCellBackColor(workbook, headercell2);
						++SerialColumns;

						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(Distributor_id);
						this.SetNormalCellBackColor(workbook, headercell2);
						++SerialColumns;

						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(rs13.getString("name"));
						this.SetNormalCellBackColor(workbook, headercell2);
						++SerialColumns;

						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(rs13.getString("city"));
						this.SetNormalCellBackColor(workbook, headercell2);
						++SerialColumns;

						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(TSO);
						this.SetNormalCellBackColor(workbook, headercell2);
						++SerialColumns;

						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(ASM);
						this.SetNormalCellBackColor(workbook, headercell2);
						++SerialColumns;

						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(RSM);
						this.SetNormalCellBackColor(workbook, headercell2);
						++SerialColumns;

						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(rs14.getString("id"));
						this.SetNormalCellBackColor(workbook, headercell2);
						++SerialColumns;

						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(rs14.getString("label"));
						this.SetNormalCellBackColor(workbook, headercell2);
						++SerialColumns;

						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(rs14.getString("order_booker"));
						this.SetNormalCellBackColor(workbook, headercell2);
						++SerialColumns;

						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(rs14.getString("outlet_id"));
						this.SetNormalCellBackColor(workbook, headercell2);
						++SerialColumns;

						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(rs14.getString("outlet_name"));
						this.SetNormalCellBackColor(workbook, headercell2);
						++SerialColumns;

						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(Region);
						this.SetNormalCellBackColor(workbook, headercell2);
						++SerialColumns;

						/// UP calculation

						double grandTotal = 0;
						for (int i = 0; i < lrbIds.size(); i++) {
							/*
							 * System.out.println(
							 * "SELECT COUNT(DISTINCT order_id) FROM inventory_sales_adjusted isa JOIN inventory_sales_adjusted_products isap ON isa.id = isap.id  JOIN inventory_products_view ipv ON ipv.product_id = isap.product_id WHERE isa.created_on BETWEEN "
							 * + Utilities.getSQLDate(StartDate) + " AND " +
							 * Utilities.getSQLDateNext(EndDate) + " AND outlet_id = " +
							 * rs14.getString("outlet_id") + " AND distributor_id = " + Distributor_id +
							 * " AND ipv.product_id IN (SELECT id FROM inventory_products WHERE lrb_type_id = "
							 * + lrbIds.get(i) + " AND is_visible = 1)");
							 */
							ResultSet rs40 = s4.executeQuery(
									"SELECT COUNT(DISTINCT order_id) FROM inventory_sales_adjusted isa JOIN inventory_sales_adjusted_products isap ON isa.id = isap.id  JOIN inventory_products_view ipv ON ipv.product_id = isap.product_id WHERE isa.created_on BETWEEN "
											+ Utilities.getSQLDate(StartDate) + " AND "
											+ Utilities.getSQLDateNext(EndDate) + " AND outlet_id = "
											+ rs14.getString("outlet_id") + " AND distributor_id = " + Distributor_id
											+ " AND ipv.product_id IN (SELECT id FROM inventory_products WHERE lrb_type_id = "
											+ lrbIds.get(i) + " AND is_visible = 1)");
							if (rs40.first()) {
								long TotalSecondaryLMPUP = rs40.getLong(1);
								System.out.println("TotalSecondaryLMPUP " + TotalSecondaryLMPUP);
								grandTotal += TotalSecondaryLMPUP;

								headercell2 = headerrow22.createCell((int) (short) SerialColumns);
								headercell2.setCellValue(TotalSecondaryLMPUP);
								if (TotalSecondaryLMPUP == 0) {
									this.Set2ndHeaderBackColorRed(workbook, headercell2);
								} else if (TotalSecondaryLMPUP == 1) {
									this.Set2ndHeaderBackColorsFloorStock(workbook, headercell2);
								} else if (TotalSecondaryLMPUP == 2) {
									this.Set2ndHeaderBackColorYellow(workbook, headercell2);
								} else if (TotalSecondaryLMPUP == 3) {
									this.Set2ndHeaderBackColorGreen(workbook, headercell2);
								} else if (TotalSecondaryLMPUP == 4) {
									this.Set2ndHeaderBackColorBlue(workbook, headercell2);
								} else if (TotalSecondaryLMPUP == 5) {
									this.Set2ndHeaderBackColorRed(workbook, headercell2);
								} else {
									this.SetNormalCellBackColor(workbook, headercell2);
								}
								++SerialColumns;

							}

						}
						

						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(grandTotal);
						if (grandTotal == 0) {
							this.Set2ndHeaderBackColorRed(workbook, headercell2);
						} else if (grandTotal == 1) {
							this.Set2ndHeaderBackColorsFloorStock(workbook, headercell2);
						} else if (grandTotal == 2) {
							this.Set2ndHeaderBackColorYellow(workbook, headercell2);
						} else if (grandTotal == 3) {
							this.Set2ndHeaderBackColorGreen(workbook, headercell2);
						} else if (grandTotal == 4) {
							this.Set2ndHeaderBackColorBlue(workbook, headercell2);
						} else if (grandTotal == 5) {
							this.Set2ndHeaderBackColorRed(workbook, headercell2);
						} else {
							this.SetNormalCellBackColor(workbook, headercell2);
						}

						++SerialColumns;
						++RowCount;
					
				}
			}
		}
		for (int i = 0; i < 17; i++) {
			spreadsheet.autoSizeColumn(i);
		}
		final FileOutputStream out = new FileOutputStream(new File(filename));
		workbook.write((OutputStream) out);
		out.close();
		this.ds.dropConnection();
	}

	public void Set2ndHeaderBackColor(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(162, 162, 163));
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
		style61.setFont((Font) font);
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
	}

	public void Set2ndHeaderBackColorsSecondary(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(0, 0, 153));
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
		style61.setFont((Font) font);
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
	}

	public void Set2ndHeaderBackColorsFloorStock(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(255, 102, 0));
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
		style61.setFont((Font) font);
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
	}

	public void Set2ndHeaderBackColorGreen(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(60, 179, 113));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setAlignment((short) 2);
		headercell.setCellStyle((CellStyle) style61);
		final XSSFFont font = workbook.createFont();
		font.setFontName("Calibri");
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont((Font) font);
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
	}

	public void Set2ndHeaderBackColorYellow(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(255, 195, 0));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setAlignment((short) 2);
		headercell.setCellStyle((CellStyle) style61);
		final XSSFFont font = workbook.createFont();
		font.setFontName("Calibri");
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont((Font) font);
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
	}

	public void Set2ndHeaderBackColorRed(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(255, 0, 0));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setAlignment((short) 2);
		headercell.setCellStyle((CellStyle) style61);
		final XSSFFont font = workbook.createFont();
		font.setFontName("Calibri");
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont((Font) font);
		style61.setFont((Font) font);
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
	}

	public void Set2ndHeaderBackColorBlue(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(0, 0, 225));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setAlignment((short) 2);
		headercell.setCellStyle((CellStyle) style61);
		final XSSFFont font = workbook.createFont();
		font.setFontName("Calibri");
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont((Font) font);
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
	}

	public void SetNormalCellBackColor(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(255, 255, 255));
		final XSSFCellStyle style61 = workbook.createCellStyle();
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
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
	}

}
