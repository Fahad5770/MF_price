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

public class Report340DistributorSalesWithOutStockExcel {
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

	public Report340DistributorSalesWithOutStockExcel()
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
			final String WherePackage, final String DistributorID, final String DistributorIDsWhere,
			final long[] SelectedDistributorsArray, final String WhereBrand) throws Exception {
		final XSSFWorkbook workbook = new XSSFWorkbook();
		final XSSFSheet spreadsheet = workbook.createSheet();
		int FirstRowCount = 0;
		int RowCount = 0;
		final CellStyle style = (CellStyle) workbook.createCellStyle();
		final XSSFFont font = workbook.createFont();
		final XSSFRow rowP = spreadsheet.createRow((int) (short) RowCount);
		XSSFCell cellP = rowP.createCell(0);
		cellP.setCellStyle(style);
		final XSSFRow rowP1 = spreadsheet.createRow((int) (short) RowCount);
		XSSFCell cellGroupheader = rowP1.createCell(0);
		cellGroupheader.setCellValue("Brand Wise Sales & Stock Report (Tons)");
		spreadsheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
		this.Set2ndHeaderBackColor(workbook, cellGroupheader);

		cellP = rowP.createCell(1);
		cellP.setCellValue("");
		cellP = rowP.createCell(2);
		cellP.setCellValue("");
		cellP = rowP.createCell(3);
		cellP.setCellValue("");
		cellP = rowP.createCell(4);
		cellP.setCellValue("");
		cellP = rowP.createCell(5);
		cellP.setCellValue("");
		cellP = rowP.createCell(6);
		cellP.setCellValue("");
		cellP = rowP.createCell(7);
		cellP.setCellValue("");

		final XSSFCellStyle style2 = workbook.createCellStyle();
		style2.setAlignment((short) 3);
		cellP.setCellStyle((CellStyle) style2);
		++RowCount;
		++FirstRowCount;
		this.Set2ndHeaderBackColorDeliveryKPI(workbook, cellGroupheader);

		int count = 0;
		ArrayList<String> labels = new ArrayList<String>();
		ArrayList<Integer> lrbIds = new ArrayList<Integer>();
		ResultSet rs2 = s.executeQuery("SELECT * from inventory_products_lrb_types");
		while (rs2.next()) {
			lrbIds.add(rs2.getInt("id"));
			labels.add(rs2.getString("label"));
			count++;
		}
	
		int col3 = count + count + 1;
		int RowCount1 = 0;
		int FirstRowCount1 = 0;
		XSSFCell cellGroupheader1 = rowP1.createCell(8);
		cellGroupheader1.setCellValue("Secondary & U.P");
		spreadsheet.addMergedRegion(new CellRangeAddress(0, 0, 8, col3 + 7));
		this.Set2ndHeaderBackColor(workbook, cellGroupheader1);

		cellP = rowP.createCell(9);
		cellP.setCellValue("");
		cellP = rowP.createCell(10);
		cellP.setCellValue("");
		cellP = rowP.createCell(11);
		cellP.setCellValue("");
		cellP = rowP.createCell(12);
		cellP.setCellValue("");
		cellP = rowP.createCell(13);
		cellP.setCellValue("");
		cellP = rowP.createCell(14);
		cellP.setCellValue("");
		cellP = rowP.createCell(15);
		cellP.setCellValue("");

		style2.setAlignment((short) 3);
		cellP.setCellStyle((CellStyle) style2);
		++RowCount1;
		++FirstRowCount1;
		this.Set2ndHeaderBackColorDeliveryKPI(workbook, cellGroupheader1);

		int col6 = count + 1;

		int RowCount12 = 0;
		int FirstRowCount12 = 0;
		XSSFCell cellGroupheader2 = rowP1.createCell(col3 + 8);
		cellGroupheader2.setCellValue("PRIMARY SALES REPORT - (TONS)");
		this.Set2ndHeaderBackColor(workbook, cellGroupheader2);
		spreadsheet.addMergedRegion(new CellRangeAddress(0, 0, col3 + 8, col3 + 8 + col6));

		cellP = rowP.createCell(9);
		cellP.setCellValue("");
		cellP = rowP.createCell(10);
		cellP.setCellValue("");
		cellP = rowP.createCell(11);
		cellP.setCellValue("");
		cellP = rowP.createCell(12);
		cellP.setCellValue("");
		cellP = rowP.createCell(13);
		cellP.setCellValue("");
		cellP = rowP.createCell(14);
		cellP.setCellValue("");
		cellP = rowP.createCell(15);
		cellP.setCellValue("");

		style2.setAlignment((short) 3);
		cellP.setCellStyle((CellStyle) style2);
		++RowCount12;
		++FirstRowCount12;
		this.Set2ndHeaderBackColorDeliveryKPI(workbook, cellGroupheader2);

		int col0 = count;
	


		cellP = rowP.createCell(9);
		cellP.setCellValue("");
		cellP = rowP.createCell(10);
		cellP.setCellValue("");
		cellP = rowP.createCell(11);
		cellP.setCellValue("");
		cellP = rowP.createCell(12);
		cellP.setCellValue("");
		cellP = rowP.createCell(13);
		cellP.setCellValue("");
		cellP = rowP.createCell(14);
		cellP.setCellValue("");
		cellP = rowP.createCell(15);
		cellP.setCellValue("");

		style2.setAlignment((short) 3);
		cellP.setCellStyle((CellStyle) style2);
		
//		this.Set2ndHeaderBackColorDeliveryKPI(workbook, cellGroupheader3);

		Date CurrentDate = StartDate;
		int Serial = 1;
		final XSSFRow headerrow = spreadsheet.createRow((int) (short) Serial);
		
		XSSFCell headercell = headerrow.createCell(0);
		headercell.setCellValue("Date");
		this.Set2ndHeaderBackColor(workbook, headercell);
		
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
		headercell.setCellValue("Region");
		this.Set2ndHeaderBackColor(workbook, headercell);
		++Serial;

		int SerialColumns1 = 0;

		final XSSFRow headerrow222 = spreadsheet.createRow((short) RowCount + 1);
		// XSSFCell headercell22 = headerrow222.createCell(0);
		XSSFCell headercell22 = headerrow222.createCell(1);
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

		// Secondary Sales

		
		int col = 8, col2 = 9, mergeCell = 0;
		 for (int i = 0; i < labels.size(); i++) {
			mergeCell = 2;

			headercell = headerrow.createCell((int) (short) Serial);
			headercell.setCellValue(labels.get(i));
			col2 = (col - 1) + mergeCell;
			Serial += mergeCell;
			spreadsheet.addMergedRegion(new CellRangeAddress(FirstRowCount, FirstRowCount, col, col2));
			this.Set2ndHeaderBackColorsSecondary(workbook, headercell);
			col = col2 + 1;

			headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
			headercell22.setCellValue("Sales");
			this.Set2ndHeaderBackColorssSecondarySales(workbook, headercell22);
			++SerialColumns1;
			headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
			headercell22.setCellValue("U.P");
			this.Set2ndHeaderBackColorssSecondarySales(workbook, headercell22);
			++SerialColumns1;
		}
		// Total of Secondary
		headercell = headerrow.createCell((int) (short) Serial);
		headercell.setCellValue("TOTAL");
		col2 = (col - 1) + mergeCell;
		Serial += mergeCell;
		spreadsheet.addMergedRegion(new CellRangeAddress(FirstRowCount, FirstRowCount, col, col2));
		this.Set2ndHeaderBackColorsFloorStock(workbook, headercell);
		col = col2 + 1;
		headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
		headercell22.setCellValue("Sales");
		this.Set2ndHeaderBackColorssSecondarySales(workbook, headercell22);
		++SerialColumns1;
		headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
		headercell22.setCellValue("U.P");
		this.Set2ndHeaderBackColorssSecondarySales(workbook, headercell22);
		++SerialColumns1;
		
		
//		// Primary Sales
		 for (int i = 0; i < labels.size(); i++) {
			headercell = headerrow.createCell((int) (short) Serial);
			headercell.setCellValue(labels.get(i));
			this.Set2ndHeaderBackColorsSecondary(workbook, headercell);
			++Serial;

			headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
			headercell22.setCellValue("Sales");
			this.Set2ndHeaderBackColorssPrimarySales(workbook, headercell22);
			++SerialColumns1;
		}
		headercell = headerrow.createCell((int) (short) Serial);
		headercell.setCellValue("TOTAL");
		this.Set2ndHeaderBackColorsFloorStock(workbook, headercell);
		++Serial;
		headercell22 = headerrow222.createCell((int) (short) SerialColumns1);
		headercell22.setCellValue("Sales");
		this.Set2ndHeaderBackColorssPrimarySales(workbook, headercell22);
		++SerialColumns1;
		

		RowCount++;
		int total_Clm = 0;

		if (total_Clm == 0) {
			final XSSFRow headerrow2 = spreadsheet.createRow((short) RowCount + 1);
			XSSFCell headercell2 = headerrow2.createCell(2);
			headercell2.setCellValue(Utilities.getDisplayDateFormat(CurrentDate));
			this.SetNormalLeftCellBackColor(workbook, headercell2);
			//System.out.println("CurrentDate" + CurrentDate);
			
			long Distributor_id = 0;
			String RSM = "", TSO = "", ASM = "", Region = "";
		//	System.out.println( "Select distributor_id,name,snd_id,city,tdm_id,rsm_id,region_id from common_distributors where distributor_id in ("+ DistributorID + ") ");
			ResultSet rs13 = s.executeQuery( "Select distributor_id,name,snd_id,city,tdm_id,rsm_id,region_id from common_distributors where distributor_id in ("+ DistributorID + ") ");

			while (rs13.next()) {
				
				
				Distributor_id = rs13.getLong("distributor_id");
		
			//	System.out.println("Select region_name from common_regions where region_id=" + rs13.getInt("region_id"));
				ResultSet rs151 = s2.executeQuery("Select region_name from common_regions where region_id=" + rs13.getInt("region_id"));
				if (rs151.next()) {
					Region = rs151.getString("region_name");
				}
				
			//	System.out.println("SELECT asm_id, (SELECT DISPLAY_NAME FROM users where users.ID=asm_id) as ASMName FROM pep.distributor_beat_plan where id in (SELECT distinct beat_plan_id FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+"  and  distributor_id="+Distributor_id+"  and isap.product_id in (select distinct package_id from inventory_products where lrb_type_id in (select id from inventory_products_lrb_types)))");
				ResultSet rsAsm = s2.executeQuery("SELECT asm_id, (SELECT DISPLAY_NAME FROM users where users.ID=asm_id) as ASMName FROM pep.distributor_beat_plan where id in (SELECT distinct beat_plan_id FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+"  and  distributor_id="+Distributor_id+"  and isap.product_id in (select distinct package_id from inventory_products where lrb_type_id in (select id from inventory_products_lrb_types)))");
				if (rsAsm.next()) {
					TSO = rsAsm.getString("ASMName");
				}
				
			//	System.out.println("SELECT DISPLAY_NAME FROM users where users.ID="+rs13.getInt("rsm_id"));
				ResultSet rsRSM = s2.executeQuery("SELECT DISPLAY_NAME FROM users where users.ID="+rs13.getInt("rsm_id"));
				if (rsRSM.next()) {
					ASM = rsRSM.getString("DISPLAY_NAME");
				}
				
				
//				System.out.println("SELECT DISPLAY_NAME FROM users where users.ID="+rs13.getInt("snd_id"));
				ResultSet rsSND = s2.executeQuery("SELECT DISPLAY_NAME FROM users where users.ID="+rs13.getInt("snd_id"));
				if (rsSND.first()) {
					RSM = rsSND.getString("DISPLAY_NAME");
				}
				
				int SerialColumns = 1;

				// int i=1;
				CurrentDate = StartDate;
				
				while (true) {
					SerialColumns = 0;

XSSFRow headerrow22 = spreadsheet.createRow((short) RowCount + 1);

					

					headercell2 = headerrow22.createCell((int) (short) SerialColumns);
					headercell2.setCellValue(Utilities.getDisplayDateFormat(CurrentDate));
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
					headercell2.setCellValue(Region);
					this.SetNormalCellBackColor(workbook, headercell2);
					++SerialColumns;

					// Secondary Sales
					// MTD

					// LMP
			long upTotal=0;
			
			double salesSum = 0, grandTotal=0;
					 for (int i = 0; i < lrbIds.size(); i++) {
						 salesSum = 0;
						ResultSet rs1 = s2.executeQuery(
								"SELECT total_units, ipv.liquid_in_ml FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "
										+ Utilities.getSQLDate(CurrentDate) + " and "
										+ Utilities.getSQLDateNext(CurrentDate) + "  and  distributor_id="
										+ Distributor_id + " and ipv.product_id in (select id from inventory_products where lrb_type_id = "+lrbIds.get(i)+" and is_visible=1)");

						while (rs1.next()) {
							salesSum += rs1.getLong(1) * rs1.getDouble(2);
						};
						grandTotal += salesSum;
						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(salesSum);
						this.SetNormalCellBackColor(workbook, headercell2);
						++SerialColumns;
						ResultSet rs40 = s2.executeQuery(
								"SELECT (OV / OT) * 100 FROM(SELECT COUNT(DISTINCT outlet_id) AS OV, (SELECT COUNT(DISTINCT outlet_id)FROM  distributor_beat_plan_log WHERE distributor_id ="
										+ Distributor_id + " AND log_date BETWEEN " + Utilities.getSQLDate(CurrentDate)
										+ " and " + Utilities.getSQLDateNext(CurrentDate)
										+ ") AS OT FROM  inventory_sales_adjusted isa JOIN inventory_sales_adjusted_products isap ON isa.id = isap.id JOIN inventory_products_view ipv ON ipv.product_id = isap.product_id WHERE isa.created_on BETWEEN "
										+ Utilities.getSQLDate(CurrentDate) + " and "
										+ Utilities.getSQLDateNext(CurrentDate)
										+ " AND outlet_id IN (SELECT DISTINCT outlet_id FROM  distributor_beat_plan_log WHERE distributor_id ="
										+ Distributor_id + " AND log_date BETWEEN " + Utilities.getSQLDate(CurrentDate)
										+ " and " + Utilities.getSQLDateNext(CurrentDate) + ")  and ipv.product_id in (select id from inventory_products where lrb_type_id = "+lrbIds.get(i)+" and is_visible=1))     UP");
						if (rs40.first()) {
							long TotalSecondaryLMPUP = rs40.getLong(1);
							upTotal += TotalSecondaryLMPUP;

							headercell2 = headerrow22.createCell((int) (short) SerialColumns);
							headercell2.setCellValue(TotalSecondaryLMPUP);
							this.SetNormalCellBackColor(workbook, headercell2);
							++SerialColumns;

						}
					};
					// total	
//					ResultSet rs27 = s2.executeQuery("select sum(p1*p2) as total from (SELECT sum(total_units) as p1, ipv.liquid_in_ml as p2 FROM  inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where ipv.is_visible=1 and isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and   distributor_id="+Distributor_id+") t");
//					while (rs27.next()) {
						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(grandTotal);
						this.SetNormalCellBackColor(workbook, headercell2);
						++SerialColumns;
			//		};
					

					
					headercell2 = headerrow22.createCell((int) (short) SerialColumns);
					headercell2.setCellValue(upTotal);
					this.SetNormalCellBackColor(workbook, headercell2);
					++SerialColumns;

					
					// PRIMARY SALES
					grandTotal = 0;
					 for (int i = 0; i < lrbIds.size(); i++) {
						 salesSum = 0;
						ResultSet rs28 = s2.executeQuery(
								"SELECT total_units, ipv.liquid_in_ml FROM  inventory_delivery_note isa join inventory_delivery_note_products isap on isa.delivery_id=isap.delivery_id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "
										+ Utilities.getSQLDate(CurrentDate) + " and "
										+ Utilities.getSQLDateNext(CurrentDate) + "  and   distributor_id="
										+ Distributor_id + "  and ipv.product_id in (select id from inventory_products where lrb_type_id = "+lrbIds.get(i)+" and is_visible=1)");
						while(rs28.next()) {
							salesSum = rs28.getDouble(1) * rs28.getDouble(2);
						}
						grandTotal += salesSum;
						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(salesSum);
						this.SetNormalCellBackColor(workbook, headercell2);
						++SerialColumns;
					};
//					ResultSet rs217 = s2.executeQuery(
//							"select sum(p1*p2) as total from (SELECT sum(total_units) as p1, ipv.liquid_in_ml as p2 FROM  inventory_delivery_note isa join inventory_delivery_note_products isap on isa.delivery_id=isap.delivery_id join inventory_products_view ipv on ipv.product_id=isap.product_id where ipv.is_visible=1 and isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and   distributor_id="+Distributor_id+" ) t"
//							);
//					while (rs217.next()) {

						headercell2 = headerrow22.createCell((int) (short) SerialColumns);
						headercell2.setCellValue(grandTotal);
						this.SetNormalCellBackColor(workbook, headercell2);
						++SerialColumns;
			//		};

					total_Clm = SerialColumns;

					++RowCount;
					if (DateUtils.isSameDay(CurrentDate, EndDate)) {
						break;
					}
					CurrentDate = Utilities.getDateByDays(CurrentDate, 1);

				}

			}

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
		// style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
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
		style61.setFont((Font) font);
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
	}

	public void Set2ndHeaderBackColorssPrimarySales(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(0, 153, 0));
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

	public void Set2ndHeaderBackColorssFloor(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(255, 153, 0));
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

	public void Set2ndHeaderBackColorsPrimary(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(0, 102, 0));
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

	public void Set2ndHeaderBackColor2(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(162, 162, 130));
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
	}

	public void Set2ndHeaderBackColorOrderKPI(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(95, 149, 153));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 1);
		style61.setBorderColor(XSSFCellBorder.BorderSide.TOP, new XSSFColor(new Color(95, 149, 153)));
		style61.setBorderColor(XSSFCellBorder.BorderSide.LEFT, new XSSFColor(new Color(95, 149, 153)));
		style61.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, new XSSFColor(new Color(95, 149, 153)));
		style61.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, new XSSFColor(new Color(95, 149, 153)));
		headercell.setCellStyle((CellStyle) style61);
		final XSSFFont font = workbook.createFont();
		font.setFontName("Calibri");
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont((Font) font);
	}

	public void Set2ndHeaderBackColorOrderKPI2(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(50, 100, 150));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 2);
		style61.setBorderColor(XSSFCellBorder.BorderSide.TOP, new XSSFColor(new Color(95, 149, 153)));
		style61.setBorderColor(XSSFCellBorder.BorderSide.LEFT, new XSSFColor(new Color(95, 149, 153)));
		style61.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, new XSSFColor(new Color(95, 149, 153)));
		style61.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, new XSSFColor(new Color(95, 149, 153)));
		style61.setAlignment((short) 1);
		headercell.setCellStyle((CellStyle) style61);
		final XSSFFont font = workbook.createFont();
		font.setFontName("Calibri");
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont((Font) font);
	}

	public void Set2ndHeaderBackColors1(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(0, 153, 0));
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
	}

	public void Set2ndHeaderBackColorDeliveryKPI(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(128, 95, 153));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setAlignment(HorizontalAlignment.CENTER);
		style61.setVerticalAlignment(VerticalAlignment.CENTER);
		headercell.setCellStyle((CellStyle) style61);
		final XSSFFont font = workbook.createFont();
		font.setFontName("Calibri");
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont((Font) font);
	}

	public void Set3rdHeaderBackColor(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(227, 227, 227));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 2);
		headercell.setCellStyle((CellStyle) style61);
		headercell.setCellStyle((CellStyle) style61);
	}

	public void Set3rdHeaderBackColorOrder(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(202, 232, 234));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 2);
		headercell.setCellStyle((CellStyle) style61);
		headercell.setCellStyle((CellStyle) style61);
	}

	public void Set3rdHeaderBackColorDelivery(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(240, 230, 247));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 2);
		headercell.setCellStyle((CellStyle) style61);
		headercell.setCellStyle((CellStyle) style61);
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
	}

	public void SetNormalLeftCellBackColor(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(255, 255, 255));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 1);
		headercell.setCellStyle((CellStyle) style61);
		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.##"));
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetNormalCellBackColor12(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(248, 192, 197));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 2);
		headercell.setCellStyle((CellStyle) style61);
		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.##"));
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetNormalCellBackColorGray(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(162, 162, 150));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		headercell.setCellStyle((CellStyle) style61);
		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.##"));
		style61.setVerticalAlignment(VerticalAlignment.CENTER);
		style61.setAlignment(HorizontalAlignment.CENTER);
		final XSSFFont font = workbook.createFont();
		font.setFontName("Calibri");
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setAlignment(HorizontalAlignment.CENTER);
		style61.setVerticalAlignment(VerticalAlignment.CENTER);
		style61.setWrapText(true);
		style61.setFont((Font) font);
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetNormalCellBackColorBlue(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(246, 253, 253));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 2);
		headercell.setCellStyle((CellStyle) style61);
		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.##"));
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetNormalCellBackColorBlueOneDecimal(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(246, 253, 253));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 2);
		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.#"));
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetNormalCellBackColorPink(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(248, 246, 249));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 2);
		headercell.setCellStyle((CellStyle) style61);
		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.##"));
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetNormalCellBackColorPinkOneDecimal(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(248, 246, 249));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 2);
		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.#"));
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetNormalCellBackColorPercent(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(255, 255, 255));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 2);
		headercell.setCellStyle((CellStyle) style61);
		style61.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetNormalCellBackColorPercentRed(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(248, 192, 197));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 2);
		headercell.setCellStyle((CellStyle) style61);
		style61.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetNormalCellBackColorPercentBlue(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(246, 253, 253));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 2);
		headercell.setCellStyle((CellStyle) style61);
		style61.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetNormalCellBackColorPercentPink(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(248, 246, 249));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 2);
		headercell.setCellStyle((CellStyle) style61);
		style61.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetNormalCellBackColorLeft(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(255, 255, 255));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 1);
		headercell.setCellStyle((CellStyle) style61);
		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.##"));
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetNormalCellBackColorLeftAltGray(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(245, 245, 245));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 1);
		headercell.setCellStyle((CellStyle) style61);
		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.##"));
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetNormalCellBackColorLeftTotal(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(248, 203, 173));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 1);
		headercell.setCellStyle((CellStyle) style61);
		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.##"));
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetNormalCellBackColorRightTotal(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(248, 203, 173));
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
		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.##"));
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetNormalCellBackColorCenterTotal(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(248, 203, 173));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 2);
		headercell.setCellStyle((CellStyle) style61);
		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.##"));
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetNormalCellBackColorRed(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(255, 214, 231));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 2);
		headercell.setCellStyle((CellStyle) style61);
		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.##"));
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetNormalCellBackColorRed12(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFColor BackColor2ndHeader = new XSSFColor(new Color(192, 0, 0));
		final XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor((short) 12);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern((short) 1);
		style61.setBorderBottom((short) 1);
		style61.setBorderTop((short) 1);
		style61.setBorderRight((short) 1);
		style61.setBorderLeft((short) 1);
		style61.setAlignment((short) 2);
		headercell.setCellStyle((CellStyle) style61);
		style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.##"));
		headercell.setCellStyle((CellStyle) style61);
	}

	public void SetBorder(final XSSFWorkbook workbook, final XSSFCell headercell) {
		final XSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom((short) 1);
		style.setBorderTop((short) 1);
		style.setBorderRight((short) 1);
		style.setBorderLeft((short) 1);
		style.setAlignment((short) 2);
		headercell.setCellStyle((CellStyle) style);
	}
}
