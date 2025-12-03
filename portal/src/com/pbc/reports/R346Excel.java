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

public class R346Excel {

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

	public R346Excel()
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
		
		String whereDistributor = (DistributorID.equals("") ? "" : " and (distributor_id in ("+DistributorID+") or distributor_id in (select distributor_id from distributor_beat_plan where asm_id in ("+DistributorID+"))");

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("cell types");

		int FirstRowCount = 0;

		int RowCount = 0;

		// Report Heading

		XSSFRow rowH = spreadsheet.createRow((short) RowCount);
		XSSFCell cellH = (XSSFCell) rowH.createCell((short) 0);

		cellH.setCellValue("R346 - Market Visit Report SKU Wise");

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

	//	System.out.println("Period: " + StartDate + " - " + EndDate);

		cellP.setCellValue("Period: " + StartDate + " - " + EndDate);

		spreadsheet.addMergedRegion(new CellRangeAddress(FirstRowCount, // first row (0-based)
				FirstRowCount, // last row (0-based)
				0, // first column (0-based)
				1 // last column (0-based)
		));

		RowCount = RowCount + 1;
		FirstRowCount = FirstRowCount + 1;
		XSSFCellStyle style61 = workbook.createCellStyle();
		
		
		int decrement=0;
		
		ArrayList<String> labels = new ArrayList<String>();
		ArrayList<Integer> lrbIds = new ArrayList<Integer>();
		ResultSet rs2 = s.executeQuery("SELECT * from inventory_products_lrb_types");
		while (rs2.next()) {
			int countZero=0;
			ResultSet rsZero = s2.executeQuery("select count(package_id) as c from inventory_products_view where lrb_type_id="+rs2.getInt("id"));
			if(rsZero.first()) {
				countZero=rsZero.getInt("c");
			}
			if(countZero!=0) {
				lrbIds.add(rs2.getInt("id"));
				labels.add(rs2.getString("label"));
			}else {
				decrement++;
			}
			
			
		}
		
		ArrayList<Integer> lrbSpaces = new ArrayList<Integer>();
		for(int i=0; i<lrbIds.size(); i++) {
		ResultSet rsLrbMrge = s.executeQuery("select count(package_id) as c from inventory_products_view where lrb_type_id="+lrbIds.get(i));
		while (rsLrbMrge.next()) {
			lrbSpaces.add(rsLrbMrge.getInt("c"));
		}
		}
//		/select count(package_id) from inventory_products_view where lrb_type_id=5;
		final XSSFRow rowP3 = spreadsheet.createRow((int) (short) RowCount);
		//XSSFCell cellGroupheader = rowP1.createCell(0);
		XSSFCell headercell = rowP3.createCell(0);
		headercell.setCellValue("");
		//System.out.println("Here 1");
		spreadsheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 9));
		this.Set2ndHeaderBackColor(workbook, headercell);
		
	//	final XSSFRow rowP1 = spreadsheet.createRow((int) (short) RowCount);
	//	XSSFCell cellGroupheader = rowP1.createCell(0);
	/*
	 * cellGroupheader.setCellValue(""); spreadsheet.addMergedRegion(new
	 * CellRangeAddress(0, 0, 0, 9)); this.Set2ndHeaderBackColor(workbook,
	 * cellGroupheader);
	 */
		
	//	XSSFCell headercell = rowP3.createCell(0);
//		for(int i=0; i<lrbIds.size(); i++) {
//			System.out.println(lrbIds.get(i)+" - "+lrbSpaces.get(i));
//		}
		
		
	//	System.out.println("Here 2");
		int LRBClo1=10, second=0;
		for(int i=0; i<lrbIds.size(); i++) {
			second = LRBClo1+lrbSpaces.get(i);
			headercell = rowP3.createCell((int) (short) LRBClo1);
			headercell.setCellValue(labels.get(i));
			if(lrbSpaces.get(i)!=0) {
				spreadsheet.addMergedRegion(new CellRangeAddress(2, 2, LRBClo1 , second));
			}
			
			this.Set3rdHeaderBackColor(workbook, headercell);
			LRBClo1 = second+1;
	}
		
		
		
		RowCount++;
		// Report Heading
	//	System.out.println("Here 3");
		XSSFRow headerrow1 = spreadsheet.createRow((short) RowCount);
		XSSFCell headercell1 = (XSSFCell) headerrow1.createCell((short) 0);

		headercell1 = (XSSFCell) headerrow1.createCell((short) 0);
		headercell1.setCellValue("Shop Name");
		SetNormalCellBackColorH(workbook, headercell1, style61);
		
		headercell1 = (XSSFCell) headerrow1.createCell((short) 1);
		headercell1.setCellValue("Visit On");
		SetNormalCellBackColorH(workbook, headercell1, style61);
		
		headercell1 = (XSSFCell) headerrow1.createCell((short) 2);
		headercell1.setCellValue("Visit Time");
		SetNormalCellBackColorH(workbook, headercell1, style61);
		
		headercell1 = (XSSFCell) headerrow1.createCell((short) 3);
		headercell1.setCellValue("Visit By");
		SetNormalCellBackColorH(workbook, headercell1, style61);
		
		headercell1 = (XSSFCell) headerrow1.createCell((short) 4);
		headercell1.setCellValue("Distributor Name");
		SetNormalCellBackColorH(workbook, headercell1, style61);
		
		headercell1 = (XSSFCell) headerrow1.createCell((short) 5);
		headercell1.setCellValue("Town (City)");
		SetNormalCellBackColorH(workbook, headercell1, style61);
		
		headercell1 = (XSSFCell) headerrow1.createCell((short) 6);
		headercell1.setCellValue("RSM Name");
		SetNormalCellBackColorH(workbook, headercell1, style61);
		
		headercell1 = (XSSFCell) headerrow1.createCell((short) 7);
		headercell1.setCellValue("ASM Name");
		SetNormalCellBackColorH(workbook, headercell1, style61);
		
		headercell1 = (XSSFCell) headerrow1.createCell((short) 8);
		headercell1.setCellValue("TSO Name");
		SetNormalCellBackColorH(workbook, headercell1, style61);
		
		headercell1 = (XSSFCell) headerrow1.createCell((short) 9);
		headercell1.setCellValue("PSR Name");
		SetNormalCellBackColorH(workbook, headercell1, style61);
		
	//	System.out.println("Here 4");
		int productCol=10;
		for(int i=0; i<lrbIds.size(); i++) {
			ResultSet rsLrbMrge = s.executeQuery("select concat(brand_label, '-',package_label) as product_name from inventory_products_view where lrb_type_id="+lrbIds.get(i)+" order by package_id");
			while (rsLrbMrge.next()) {
				headercell1 = (XSSFCell) headerrow1.createCell((short) productCol);
				headercell1.setCellValue(rsLrbMrge.getString("product_name"));
				SetNormalCellBackColorH(workbook, headercell1, style61);
				productCol++;
			}
			headercell1 = (XSSFCell) headerrow1.createCell((short) productCol);
			headercell1.setCellValue("Total "+labels.get(i));
			this.Set2ndHeaderBackColorssSecondarySales(workbook, headercell1);
			productCol++;
			}
		
		
		
		String startDate = Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithBackSlash(StartDate));
		String endDate = Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithBackSlash(EndDate));

		ArrayList<Long> Users = new ArrayList<Long>();
		System.out.println("SELECT distinct created_by FROM mobile_order_sm_zero where  created_on between "+ startDate + " and " + endDate+whereDistributor);
		ResultSet rs233 = s.executeQuery("SELECT distinct created_by FROM mobile_order_sm_zero where  created_on between "+ startDate + " and " + endDate+whereDistributor);
		while (rs233.next()) {
			Users.add(rs233.getLong("created_by"));
		}
		
		int CountPackage=0;
		for(int u=0; u<lrbIds.size(); u++){
		//	System.out.println("select count(*) as c from inventory_products_view where lrb_type_id="+lrbIds.get(u));
		ResultSet rsCountPackage = s2.executeQuery("select count(*) as c from inventory_products_view where lrb_type_id="+lrbIds.get(u));
		if(rsCountPackage.first()) {
			CountPackage += rsCountPackage.getInt("c");
		}
		}
		CountPackage += (8-decrement);
		//System.out.println("Here 5 count : " + CountPackage);
		int[] TotalVisits = new int[CountPackage];
		int[] TotalVisitsWithStock = new int[CountPackage];
		int TotalDates = 0;
		
		for (int y = 0; y < Users.size(); y++) {
			String name="";
		//	ArrayList<Integer> Visits = new ArrayList<Integer>();
		//	ArrayList<Integer> VisitsWithStock = new ArrayList<Integer>();
			int[] Visits = new int[CountPackage];
			int[] VisitsWithStock = new int[CountPackage];
			
			/*
			 * for(int i=0; i<CountPackage; i++) { Visits[i] = 0; }
			 */
			
			System.out.println(
					"SELECT (select city from common_distributors d  where d.distributor_id=o.distributor_id) as city,(select name from common_distributors d  where d.distributor_id=o.distributor_id) as distributor_name,(select DISPLAY_NAME from users where id=created_by) as name,created_on,id, outlet_id, (SELECT name FROM common_outlets where id=o.outlet_id) outlet_name,mobile_order_no FROM mobile_order_sm_zero o where  created_on between "
							+ startDate + " and " + endDate + " and created_by="
							+ Users.get(y) + " order by date(created_on) desc, mobile_timestamp desc");
			ResultSet rs22 = s.executeQuery(
					"SELECT (select city from common_distributors d  where d.distributor_id=o.distributor_id) as city,(select name from common_distributors d  where d.distributor_id=o.distributor_id) as distributor_name,(select DISPLAY_NAME from users where id=created_by) as name,created_on,id, outlet_id, (SELECT name FROM common_outlets where id=o.outlet_id) outlet_name,mobile_order_no FROM mobile_order_sm_zero o where  created_on between "
							+ startDate + " and " + endDate + " and created_by="
							+ Users.get(y) + " order by date(created_on) desc, mobile_timestamp desc");
			while (rs22.next()) {
				name = rs22.getString("name");
				
				String rsm="",asm="",tso="",psr="";
				long distributorId=0;
				ResultSet rsNames = s2.executeQuery("select (select DISPLAY_NAME from users where ID=assigned_to) as PSR, distributor_id ,(select DISPLAY_NAME from users where ID=snd_id) as RSM ,  (select DISPLAY_NAME from users where ID=rsm_id) as SND , (select DISPLAY_NAME from users where ID=asm_id) as TSO from distributor_beat_plan_view where outlet_id="+rs22.getString("outlet_id"));
				if(rsNames.first()) {
					 rsm=rsNames.getString("RSM");
					 distributorId=rsNames.getLong("distributor_id");
					 tso=rsNames.getString("TSO");
					 psr=rsNames.getString("PSR");
				}
				
				
				ResultSet rsASM = s2.executeQuery("select (select DISPLAY_NAME from users where ID=rsm_id) as ASM from common_distributors cd where distributor_id="+distributorId);
				if(rsASM.first()) {
					asm=rsASM.getString("ASM");
				}
				
				tso = tso == null ? "" : tso;
				asm = asm == null ? "" : asm;
				rsm = rsm == null ? "" : rsm;
			//	System.out.println("Here 7");
				RowCount++;
				final XSSFRow headerrow222 = spreadsheet.createRow((short) RowCount);
				XSSFCell headercell1e = (XSSFCell) headerrow222.createCell((short) 0);
				int col=0;
				
				headercell1e = (XSSFCell) headerrow222.createCell((short) col);
				headercell1e.setCellValue(rs22.getString("outlet_id")+"-"+rs22.getString("outlet_name"));
				SetNormalCellBackColor(workbook, headercell1e, style61);
				col++;
				
				headercell1e = (XSSFCell) headerrow222.createCell((short) col);
				headercell1e.setCellValue(Utilities.getDisplayDateFormat(rs22.getTimestamp("created_on")));
				SetNormalCellBackColor(workbook, headercell1e, style61);
				col++;
				
				headercell1e = (XSSFCell) headerrow222.createCell((short) col);
				headercell1e.setCellValue(Utilities.getDisplayTimeFormat(rs22.getTimestamp("created_on")));
				SetNormalCellBackColor(workbook, headercell1e, style61);
				col++;
				
				headercell1e = (XSSFCell) headerrow222.createCell((short) col);
				headercell1e.setCellValue(name);
				SetNormalCellBackColor(workbook, headercell1e, style61);
				col++;
				
				headercell1e = (XSSFCell) headerrow222.createCell((short) col);
				headercell1e.setCellValue(rs22.getString("distributor_name"));
				SetNormalCellBackColor(workbook, headercell1e, style61);
				col++;
				
				headercell1e = (XSSFCell) headerrow222.createCell((short) col);
				headercell1e.setCellValue(rs22.getString("city"));
				SetNormalCellBackColor(workbook, headercell1e, style61);
				col++;
				
				headercell1e = (XSSFCell) headerrow222.createCell((short) col);
				headercell1e.setCellValue(rsm);
				SetNormalCellBackColor(workbook, headercell1e, style61);
				col++;
				
				headercell1e = (XSSFCell) headerrow222.createCell((short) col);
				headercell1e.setCellValue(asm);
				SetNormalCellBackColor(workbook, headercell1e, style61);
				col++;
				
				headercell1e = (XSSFCell) headerrow222.createCell((short) col);
				headercell1e.setCellValue(tso);
				SetNormalCellBackColor(workbook, headercell1e, style61);
				col++;
				
				headercell1e = (XSSFCell) headerrow222.createCell((short) col);
				headercell1e.setCellValue(psr);
				SetNormalCellBackColor(workbook, headercell1e, style61);
				col++;
				
				int increment=0;
				for(int u=0; u<lrbIds.size(); u++){
					int rowVisit=0;
					
					
					
				//	System.out.println("select package_id, brand_id from inventory_products_view where lrb_type_id="+lrbIds.get(u)+" order by package_id");
					ResultSet rsPackage = s2.executeQuery("select package_id, brand_id from inventory_products_view where lrb_type_id="+lrbIds.get(u)+" order by package_id");
							while(rsPackage.next()) {
								Visits[increment] = Visits[increment] +1;
								TotalVisits[increment] = TotalVisits[increment] +1;
								int visit=0;
								//System.out.println(increment+" - "+lrbIds.get(u));
							//	System.out.println(lrbIds.get(u) + " - "+rsPackage.getInt("package_id"));
//								System.out.println(
//										"SELECT mrs.product_id FROM mobile_retailer_sm_stock mrs join inventory_products_view ipv on mrs.product_id=ipv.product_id where ipv.is_visible=1 and lrb_type_id="
//												+ lrbIds.get(u) + " and package_id="+rsPackage.getInt("package_id")+" and brand_id="+rsPackage.getInt("brand_id")+" and   mrs.order_no=" + rs22.getLong("mobile_order_no") + " and outlet_id=" + rs22.getString("outlet_id"));
					ResultSet rs111 = s3.executeQuery(
							"SELECT mrs.product_id FROM mobile_retailer_sm_stock mrs join inventory_products_view ipv on mrs.product_id=ipv.product_id where ipv.is_visible=1 and lrb_type_id="
									+ lrbIds.get(u) + " and package_id="+rsPackage.getInt("package_id")+" and brand_id="+rsPackage.getInt("brand_id")+" and   mrs.order_no=" + rs22.getLong("mobile_order_no") + " and outlet_id=" + rs22.getString("outlet_id"));
							if (rs111.first()) {
								visit = (rs111.getInt("product_id") > 0) ? 1 : 0;
								rowVisit=1;
							}// end of Visit
							
							if(visit == 1) {
								 VisitsWithStock[increment] = VisitsWithStock[increment] +1;
								 TotalVisitsWithStock[increment] = TotalVisitsWithStock[increment] +1;
							}
							//System.out.println("Here 8");
							
							headercell1e = (XSSFCell) headerrow222.createCell((short) col);
							headercell1e.setCellValue(visit);
							SetNormalCellBackColor(workbook, headercell1e, style61);
							col++;
							
							increment++;
							}// end of package and brand Loop
							
							
							headercell1e = (XSSFCell) headerrow222.createCell((short) col);
							headercell1e.setCellValue(rowVisit);
							SetNormalCellBackColorH(workbook, headercell1e, style61);
							col++;
							
							increment++;	
				}// end of LRB Loop
				//System.out.println("Here 10");
			}// End of User Data Row
		//	System.out.println("Here 11");
				/* Yellow Box starts for users */
//			System.out.println("====================");
//			
//			for(int i=0; i<VisitsWithStock.length; i++) {
//			
//				System.out.println(i+" ==> "+VisitsWithStock[i]);
//			}
//			System.out.println("====================");
//			
				/* 1 : no. of visits */
				RowCount++;
				final XSSFRow headerrow223 = spreadsheet.createRow((short) RowCount);
				XSSFCell headercell12 = (XSSFCell) headerrow223.createCell((short) 0);
				XSSFCellStyle style63 = workbook.createCellStyle();
				int col2=0;
				
				
				
				int dateVisits=0;
				//	System.out.println("select count(distinct MYDATE ) as c from (SELECT date(created_on) AS MYDATE, created_on FROM mobile_order_sm_zero where  created_on between  "+Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) +" and created_by="+Users.get(y)+" ) t");
					ResultSet rsDateCount = s.executeQuery("select count(distinct MYDATE ) as c from (SELECT date(created_on) AS MYDATE, created_on FROM mobile_order_sm_zero where  created_on between  "+startDate + " and " + endDate +" and created_by="+Users.get(y)+" ) t");
				if(rsDateCount.first()){
					 dateVisits= rsDateCount.getInt("c");
					 TotalDates = TotalDates + dateVisits;
					 
				}
				
				for(int i=0; i<8; i++) {
					headercell1 = (XSSFCell) headerrow223.createCell((short) col2);
					headercell1.setCellValue("");
					SetNormalCellBackColorH(workbook, headercell12, style63);
					col2++;
				}
				
				
				
				headercell12 = (XSSFCell) headerrow223.createCell((short) col2);
				headercell12.setCellValue(name);
				SetNormalCellBackColorH(workbook, headercell12, style63);
				col2++;
				
				headercell12 = (XSSFCell) headerrow223.createCell((short) col2);
				headercell12.setCellValue("No Of Visits");
				SetNormalCellBackColorH_yellow(workbook, headercell12, style63);
				col2++;
				
				for(int i=0; i<CountPackage; i++) {
					headercell12 = (XSSFCell) headerrow223.createCell((short) col2);
					headercell12.setCellValue(dateVisits);
					SetNormalCellBackColorH_yellow(workbook, headercell12, style63);
					col2++;
				}
				
				/* 2 : No of Shops Visited*/
				RowCount++;
				final XSSFRow headerrow224 = spreadsheet.createRow((short) RowCount);
				XSSFCell headercell14 = (XSSFCell) headerrow224.createCell((short) 0);
				XSSFCellStyle style64 = workbook.createCellStyle();
				int col3=0;
				
				for(int i=0; i<9; i++) {
					headercell1 = (XSSFCell) headerrow224.createCell((short) col3);
					headercell1.setCellValue("");
					SetNormalCellBackColorH(workbook, headercell12, style64);
					col3++;
				}
				
				headercell12 = (XSSFCell) headerrow224.createCell((short) col3);
				headercell12.setCellValue("No of Shops Visited");
				SetNormalCellBackColorH_yellow(workbook, headercell12, style64);
				col3++;
				
				for(int i=0; i<Visits.length; i++) {
					headercell14 = (XSSFCell) headerrow224.createCell((short) col3);
					headercell14.setCellValue(Visits[i]);
					SetNormalCellBackColorH_yellow(workbook, headercell14, style64);
					col3++;
				}
				
				/* 3 : No of Shops with stock availability */
				RowCount++;
				final XSSFRow headerrow225 = spreadsheet.createRow((short) RowCount);
				XSSFCell headercell15 = (XSSFCell) headerrow224.createCell((short) 0);
				XSSFCellStyle style65 = workbook.createCellStyle();
				int col4=0;
				
				for(int i=0; i<9; i++) {
					headercell1 = (XSSFCell) headerrow225.createCell((short) col4);
					headercell1.setCellValue("");
					SetNormalCellBackColorH(workbook, headercell12, style65);
					col4++;
				}
				
				headercell15 = (XSSFCell) headerrow225.createCell((short) col4);
				headercell15.setCellValue("No of Shops with stock availability");
				SetNormalCellBackColorH_yellow(workbook, headercell15, style65);
				col4++;
				
				
				for(int i=0; i<VisitsWithStock.length; i++) {
					headercell15 = (XSSFCell) headerrow225.createCell((short) col4);
					headercell15.setCellValue(VisitsWithStock[i]);
					SetNormalCellBackColorH_yellow(workbook, headercell15, style65);
					col4++;
				}
				
				/* 4 : Availability %age */
				RowCount++;
				final XSSFRow headerrow226 = spreadsheet.createRow((short) RowCount);
				XSSFCell headercell16 = (XSSFCell) headerrow224.createCell((short) 0);
				XSSFCellStyle style66 = workbook.createCellStyle();
				int col5=0;
				
				for(int i=0; i<9; i++) {
					headercell1 = (XSSFCell) headerrow226.createCell((short) col5);
					headercell1.setCellValue("");
					SetNormalCellBackColorH(workbook, headercell12, style66);
					col5++;
				}
				
				headercell16 = (XSSFCell) headerrow226.createCell((short) col5);
				headercell16.setCellValue("Availability %age");
				SetNormalCellBackColorH_yellow(workbook, headercell16, style66);
				col5++;
				
				for(int i=0; i<VisitsWithStock.length; i++) {
					headercell16 = (XSSFCell) headerrow226.createCell((short) col5);
					headercell16.setCellValue(((Visits[i] != 0) ? Math.round((VisitsWithStock[i] * 100) / Visits[i]) : 0)+"%");
					SetNormalCellBackColorH_yellow(workbook, headercell16, style66);
					col5++;
				}
			
			
				RowCount++;
		}
	
		/* Total work start */
		
		RowCount++;
		/* 1 : no. of visits */
		
	
		
		
		RowCount++;
		final XSSFRow headerrow223 = spreadsheet.createRow((short) RowCount);
		XSSFCell headercell12 = (XSSFCell) headerrow223.createCell((short) 0);
		XSSFCellStyle style63 = workbook.createCellStyle();
		int TotalCol=0;

		for(int i=0; i<9; i++) {
			headercell1 = (XSSFCell) headerrow223.createCell((short) TotalCol);
			headercell1.setCellValue("");
			SetNormalCellBackColorH(workbook, headercell12, style63);
			TotalCol++;
		}
		
		headercell12 = (XSSFCell) headerrow223.createCell((short) TotalCol);
		headercell12.setCellValue("No Of Visits");
		SetNormalCellBackColorH_yellow(workbook, headercell12, style63);
		TotalCol++;
		
		for(int i=0; i<TotalVisits.length; i++) {
			headercell12 = (XSSFCell) headerrow223.createCell((short) TotalCol);
			headercell12.setCellValue(TotalDates);
			SetNormalCellBackColorH_yellow(workbook, headercell12, style63);
			TotalCol++;
		}
		
		/* 2 : No of Shops Visited*/
		RowCount++;
		final XSSFRow headerrow224 = spreadsheet.createRow((short) RowCount);
		XSSFCell headercell14 = (XSSFCell) headerrow224.createCell((short) 0);
		XSSFCellStyle style64 = workbook.createCellStyle();
		TotalCol=0;
		
		for(int i=0; i<9; i++) {
			headercell1 = (XSSFCell) headerrow224.createCell((short) TotalCol);
			headercell1.setCellValue("");
			SetNormalCellBackColorH(workbook, headercell12, style64);
			TotalCol++;
		}
		
		headercell12 = (XSSFCell) headerrow224.createCell((short) TotalCol);
		headercell12.setCellValue("No of Shops Visited");
		SetNormalCellBackColorH_yellow(workbook, headercell12, style64);
		TotalCol++;
		
		for(int i=0; i<TotalVisits.length; i++) {
			headercell14 = (XSSFCell) headerrow224.createCell((short) TotalCol);
			headercell14.setCellValue(TotalVisits[i]);
			SetNormalCellBackColorH_yellow(workbook, headercell14, style64);
			TotalCol++;
		}
		
		/* 3 : No of Shops with stock availability */
		RowCount++;
		final XSSFRow headerrow225 = spreadsheet.createRow((short) RowCount);
		XSSFCell headercell15 = (XSSFCell) headerrow224.createCell((short) 0);
		XSSFCellStyle style65 = workbook.createCellStyle();
		 TotalCol=0;
		
		for(int i=0; i<9; i++) {
			headercell1 = (XSSFCell) headerrow225.createCell((short) TotalCol);
			headercell1.setCellValue("");
			SetNormalCellBackColorH(workbook, headercell12, style65);
			TotalCol++;
		}
		
		headercell15 = (XSSFCell) headerrow225.createCell((short) TotalCol);
		headercell15.setCellValue("No of Shops with stock availability");
		SetNormalCellBackColorH_yellow(workbook, headercell15, style65);
		TotalCol++;
		
		
		for(int i=0; i<TotalVisitsWithStock.length; i++) {
			headercell15 = (XSSFCell) headerrow225.createCell((short) TotalCol);
			headercell15.setCellValue(TotalVisitsWithStock[i]);
			SetNormalCellBackColorH_yellow(workbook, headercell15, style65);
			TotalCol++;
		}
		
		/* 4 : Availability %age */
		RowCount++;
		final XSSFRow headerrow226 = spreadsheet.createRow((short) RowCount);
		XSSFCell headercell16 = (XSSFCell) headerrow224.createCell((short) 0);
		XSSFCellStyle style66 = workbook.createCellStyle();
		 TotalCol=0;
		
		for(int i=0; i<9; i++) {
			headercell1 = (XSSFCell) headerrow226.createCell((short) TotalCol);
			headercell1.setCellValue("");
			SetNormalCellBackColorH(workbook, headercell12, style66);
			TotalCol++;
		}
		
		headercell16 = (XSSFCell) headerrow226.createCell((short) TotalCol);
		headercell16.setCellValue("Total Availability %age");
		SetNormalCellBackColorH_yellow(workbook, headercell16, style66);
		TotalCol++;
		
		for(int i=0; i<TotalVisitsWithStock.length; i++) {
			headercell16 = (XSSFCell) headerrow226.createCell((short) TotalCol);
			headercell16.setCellValue(((TotalVisits[i] != 0) ? Math.round((TotalVisitsWithStock[i] * 100) / TotalVisits[i]) : 0)+"%");
			SetNormalCellBackColorH_yellow(workbook, headercell16, style66);
			TotalCol++;
		}
	

//		RowCount = RowCount + 1;


		// Auto Sizing Column

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
	
	public void SetNormalCellBackColorH_yellow(XSSFWorkbook workbook, XSSFCell headercell, XSSFCellStyle style61) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255,255,0));

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
