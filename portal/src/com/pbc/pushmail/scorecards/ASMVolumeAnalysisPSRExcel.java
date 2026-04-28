package com.pbc.pushmail.scorecards;

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

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
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



public class ASMVolumeAnalysisPSRExcel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath()+ "/CashInflowSummary.pdf";
	
	XSSFCellStyle style61;
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	Statement s5;
	Statement s6;
	Statement s7;
	
	
	Date StartDate = Utilities.getDateByDays(-1); //Utilities.parseDate("13/02/2016");
	Date EndDate = Utilities.getDateByDays(-1);//Utilities.parseDate("13/02/2016");
	
	
	///////Date StartDate = Utilities.parseDate("06/09/2016");
	//////Date EndDate = Utilities.parseDate("06/09/2016");
	
	
	Date Yesterday = Utilities.getDateByDays(-2);
	
	
	long ASM_ID = 0;
	
	public static void main(String[] args)throws Exception 
	   {
		
		
		
		
			   
	   }
	
	
	public void setSND(long SND_ID){
		this.ASM_ID = SND_ID;
	}
	
	public ASMVolumeAnalysisPSRExcel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		
		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
		s5 = ds.createStatement();
		s6 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6){
			Yesterday = Utilities.getDateByDays(-3);
		}
		
	}
	
	
	public void createPdf(String filename, long ASM_ID) throws  IOException, SQLException {
		
		
		
		
	//	System.out.println(this.SND_ID);
		
		String WhereSND="";
		
		if(this.ASM_ID!=0){
			WhereSND = " and distributor_id in (select distributor_id from distributor_beat_plan where asm_id="+this.ASM_ID+") ";
		}
		
			XSSFWorkbook workbook = new XSSFWorkbook(); 
	      XSSFSheet spreadsheet = workbook.createSheet("cell types");
	      
	      
	      // Style
	      
			XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));
			style61 = workbook.createCellStyle();
			style61.setFillBackgroundColor(HSSFColor.BLUE.index);
			style61.setFillForegroundColor(BackColor2ndHeader);
			style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

	      
	      // End Style
	      
	      int month = Utilities.getMonthNumberByDate(EndDate);
			int year = Utilities.getYearByDate(EndDate);
			Date StartDateLastMonth = Utilities.getStartDateByMonth(month-2, year);
			Date EndDateLastMonth = Utilities.getEndDateByMonth(month-2, year);
			
			int DaysInLastMonth = Utilities.getDayNumberByDate(EndDateLastMonth);
			
			long TotalBrandCount=0;
			
			double GrandTotalBalance = 0;
			double GrandTotalSecondarySales = 0;
			double GrandTotalLiftingTarget = 0;
			
			int FirstRowCount=1;
			
			int RowCount=1;
			
			
			//Printing Date
			
			//Report Heading
			
			 XSSFRow rowP = spreadsheet.createRow((short) RowCount);	      
		     
			 
			 XSSFCell cellP = (XSSFCell) rowP.createCell((short) 1);
			 /*
		     cellP.setCellValue("");
		     
		     cellP = (XSSFCell) rowP.createCell((short) 2);		     
		     cellP.setCellValue("");
		     
		     cellP = (XSSFCell) rowP.createCell((short) 3);		     
		     cellP.setCellValue("");
		     
		     cellP = (XSSFCell) rowP.createCell((short) 4);		     
		     cellP.setCellValue("");
		     
		     cellP = (XSSFCell) rowP.createCell((short) 5);		     
		     cellP.setCellValue("");
			
		     cellP = (XSSFCell) rowP.createCell((short) 6);		     
		     cellP.setCellValue("");
			 
			 
			 cellP = (XSSFCell) rowP.createCell((short) 7);
			 cellP = (XSSFCell) rowP.createCell((short) 8);
		     */
		     cellP.setCellValue("Period: "+Utilities.getDisplayDateFormat(StartDate)+" - "+Utilities.getDisplayDateFormat(EndDate));
			
		     spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  FirstRowCount, //first row (0-based)
		    		  FirstRowCount, //last row (0-based)
		    		  8, //first column (0-based)
		    	      9 //last column (0-based)
		    	      ));
		    
		     XSSFCellStyle style611 = workbook.createCellStyle();		      
		     style611.setAlignment(CellStyle.ALIGN_LEFT);
		     cellP.setCellStyle(style611);
		      
		     /*
		     
			RowCount = RowCount+1;
			FirstRowCount = FirstRowCount+1;
			
			
			//Report Heading
			
			 XSSFRow rowH = spreadsheet.createRow((short) RowCount);	      
		     XSSFCell cellH = (XSSFCell) rowH.createCell((short) 1);
		     
		     cellH.setCellValue("Volume Analysis");
			
		     spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  FirstRowCount, //first row (0-based)
		    		  FirstRowCount, //last row (0-based)
		    		  1, //first column (0-based)
		    	      9 //last column (0-based)
		    	      ));
		     
		    // CellUtil.setAlignment(cellH, workbook, CellStyle.ALIGN_CENTER);
		      
		      XSSFColor BackColorDistributor1 = new XSSFColor(new java.awt.Color(0, 0, 255));
		      
		      XSSFCellStyle style61 = workbook.createCellStyle();
		      style61.setFillBackgroundColor(
		      HSSFColor.BLUE.index );
		      style61.setFillForegroundColor(BackColorDistributor1);
		      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
		      style61.setAlignment(CellStyle.ALIGN_CENTER);
		      cellH.setCellStyle(style61);
		     
		            
		      XSSFFont font1 = workbook.createFont();
		      font1.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		      font1.setFontHeightInPoints((short)10);
		      font1.setColor(IndexedColors.WHITE.getIndex());
		      style61.setFont(font1);
		     
		     */
		      
			RowCount = RowCount+1;
			FirstRowCount = FirstRowCount+1;
			
			String DistributorIDs="";
			
			///////////////////ResultSet rs = s.executeQuery("SELECT *  FROM common_distributors cd where distributor_id in(100914,100922)"+WhereSND);
			
			
			ResultSet rs = s.executeQuery("SELECT *  FROM common_distributors cd where is_shifted_to_other_plant = 0 and distributor_id in(select distributor_id from mobile_order where created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(-30))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(0))+") "+WhereSND);
			while(rs.next()){
				
				double MinToLiftedGrandTotal=0;
	      
	      XSSFRow row = spreadsheet.createRow((short) RowCount);	      
	      XSSFCell cell = (XSSFCell) row.createCell((short) 1);
	     
	      
	      	long DistributorID = rs.getLong("distributor_id");
	      	
	      cell.setCellValue(rs.getLong("distributor_id")+" - "+rs.getString("name"));
	      
	      
	      	      
	      spreadsheet.addMergedRegion(new CellRangeAddress(
	    		  FirstRowCount, //first row (0-based)
	    		  FirstRowCount, //last row (0-based)
	    		  1, //first column (0-based)
	    	      16 //last column (0-based)
	    	      ));
	      
	      XSSFColor BackColorDistributor = new XSSFColor(new java.awt.Color(64, 64, 64));
	      
	      XSSFCellStyle style6 = workbook.createCellStyle();
	      style6.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style6.setFillForegroundColor(BackColorDistributor);
	      style6.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      cell.setCellStyle(style6);
	     
	            
	      XSSFFont font = workbook.createFont();
	      font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
	      font.setFontHeightInPoints((short)10);
	      font.setColor(IndexedColors.WHITE.getIndex());
	      style6.setFont(font);
	      
	      
	      // Grand Total
	    	double Grand_AvgDailyTarget = 0;
	    	double Grand_LastDaySale = 0;
	    	double Grand_AvgDailyBalanceRequired = 0;
	    	double Grand_MTDSale = 0;
	    	double Grand_MonthlyTarget = 0;
	    	double Grand_MTDTargetPercent = 0;
	    	
	    	
	    	double Grand_C2_AvgDailyTarget = 0;
	    	double Grand_C2_LastDaySale = 0;
	    	double Grand_C2_AvgDailyBalanceRequired = 0;
	    	double Grand_C2_MTDSale = 0;
	    	double Grand_C2_MonthlyTarget = 0;
	    	double Grand_C2_MTDTargetPercent = 0;
	      
	      
			double GrandTotalAvgDailyTargets=0;
			double GrandTotalAvgDailyTargetsSting=0;
			double GrandTotalAvgDailyTargetsSlice=0;
			double GrandTotalAvgDailyTargetsSSRB=0;
			double GrandTotalAvgDailyTargetsOtherSS=0;
			double GrandTotalAvgDailyTargets1501750=0;
			double GrandTotalAvgDailyTargets1000=0;
			double GrandTotalAvgDailyTargets2250=0;
			double GrandTotalAvgDailyTargetsTotal=0;
			
			double GrandTotalMTDTargets=0;
			double GrandTotalMTDTargetsSting=0;
			double GrandTotalMTDTargetsSlice=0;
			double GrandTotalMTDTargetsSSRB=0;
			double GrandTotalMTDTargetsOtherSS=0;
			double GrandTotalMTDTargets1501750=0;
			double GrandTotalMTDTargets1000=0;
			double GrandTotalMTDTargets2250=0;
			double GrandTotalMTDTargetsTotal=0;
			
			double GrandTotalYesterdaySalesCSD=0;
			double GrandTotalYesterdaySalesSting=0;
			double GrandTotalYesterdaySalesSlice=0;
			double GrandTotalYesterdaySalesSSRB=0;
			double GrandTotalYesterdaySalesOtherSS=0;
			double GrandTotalYesterdaySales1501750=0;
			double GrandTotalYesterdaySales1000=0;
			double GrandTotalYesterdaySales2250=0;
			double GrandTotalYesterdaySalesTotal=0;
			double GrandTotalYesterdaySalesAF=0;
			
			double GrandTotalMTDSalesCSD=0;
			double GrandTotalMTDSalesSting=0;
			double GrandTotalMTDSalesSlice=0;
			double GrandTotalMTDSalesSSRB=0;
			double GrandTotalMTDSalesOtherSS=0;
			double GrandTotalMTDSales15001750=0;
			double GrandTotalMTDSales1000=0;
			double GrandTotalMTDSales2250=0;
			double GrandTotalMTDSalesTotal=0;
			double GrandTotalMTDSalesAF=0;
			
			double GrandTotalMTDVsTargtetCSD=0;
			double GrandTotalMTDVsTargtetSting=0;
			double GrandTotalMTDVsTargtetSlice=0;
			double GrandTotalMTDVsTargtetSSRB=0;
			double GrandTotalMTDVsTargtetOtherSS=0;
			double GrandTotalMTDVsTargtet15001750=0;
			double GrandTotalMTDVsTargtet1000=0;
			double GrandTotalMTDVsTargtet2250=0;
			double GrandTotalMTDVsTargtetTotal=0;
			
			double GrandTotalBalanceReqCSD=0;
			double GrandTotalBalanceReqSting=0;
			double GrandTotalBalanceReqSlice=0;
			double GrandTotalBalanceReqSSRB=0;
			double GrandTotalBalanceReqOtherSS=0;
			double GrandTotalBalanceReq15001750=0;
			double GrandTotalBalanceReq1000=0;
			double GrandTotalBalanceReq2250=0;
			double GrandTotalBalanceReqTotal=0;

	    	
	      //2nd row header
	      
	     
	      /*
	      RowCount = RowCount+1;
	      
	      XSSFRow headerrow = spreadsheet.createRow((short) RowCount);	      
	      XSSFCell headercell = (XSSFCell) headerrow.createCell((short) 1);
	     
	      headercell.setCellValue("");
	      Set2ndHeaderBackColor(workbook,headercell);
	      spreadsheet.addMergedRegion(new CellRangeAddress(
	    		  FirstRowCount+1, //first row (0-based)
	    		  FirstRowCount+1, //last row (0-based)
	    		  1, //first column (0-based)
	    	      3 //last column (0-based)
	    	      ));
	      
	     
	          
	      headercell = (XSSFCell) headerrow.createCell((short) 4);	     
	      headercell.setCellValue("Current Stock");
	      Set2ndHeaderBackColor(workbook,headercell);
	     
	      
	      headercell = (XSSFCell) headerrow.createCell((short) 5);	     
	      headercell.setCellValue("Average Daily Sale");
	      Set2ndHeaderBackColor(workbook,headercell);
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		  5, //first column (0-based)
	    	      6 //last column (0-based)
	    	      ));
	    
	     
	     headercell = (XSSFCell) headerrow.createCell((short) 7);	     
	      headercell.setCellValue("Stock Days");
	      Set2ndHeaderBackColor(workbook,headercell);
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		 FirstRowCount+1, //first row (0-based)
	    		 FirstRowCount+1, //last row (0-based)
	    		  7, //first column (0-based)
	    	      8 //last column (0-based)
	    	      ));
	      
	     headercell = (XSSFCell) headerrow.createCell((short) 9);	     
	      headercell.setCellValue("");
	      Set2ndHeaderBackColor(workbook,headercell);
	      
	      */
	     //3rd Row Header //////////////////////
	     ///////////////////////////////////////
	    
	     RowCount = RowCount+1;
	     
	     XSSFRow headerrow1 = spreadsheet.createRow((short) RowCount);        
	     XSSFCell headercell1 = (XSSFCell) headerrow1.createCell((short) 1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 1);	     
	     headercell1.setCellValue("PJP");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 2);	     
	     headercell1.setCellValue("PSR");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 3);	     
	     headercell1.setCellValue("");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 4);	     
	     headercell1.setCellValue("Avg. Daily Target");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 5);	     
	     headercell1.setCellValue("Last Day Sale");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 6);	     
	     headercell1.setCellValue("Avg. Daily Bal. Req.");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 7);	     
	     headercell1.setCellValue("MTD Sale");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 8);	     
	     headercell1.setCellValue("Monthly Target");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 9);	     
	     headercell1.setCellValue("MTD vs. Target%");
	     Set3rdHeaderBackColor(workbook,headercell1);

	     headercell1 = (XSSFCell) headerrow1.createCell((short) 10);	     
	     headercell1.setCellValue("");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 11);	     
	     headercell1.setCellValue("Avg. Daily Target");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 12);	     
	     headercell1.setCellValue("Last Day Sale");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 13);	     
	     headercell1.setCellValue("Avg. Daily Bal. Req.");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 14);	     
	     headercell1.setCellValue("MTD Sale");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 15);	     
	     headercell1.setCellValue("Monthly Target");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     
	     headercell1 = (XSSFCell) headerrow1.createCell((short) 16);	     
	     headercell1.setCellValue("MTD vs. Target%");
	     Set3rdHeaderBackColor(workbook,headercell1);
	     
	     //* Actaul Data , Rows and Cols */
	     /////////////////////////////////////////////////////////////////
	     
	     
	     
			String rows[] = new String[]{"CSD","Sting","AF","Slice","Total"};
			String rows2[] = new String[]{"SSRB","Other SS","1.5/1.75","1L","2.25 6U"};
     
	     
	     
	     
	     long BrandCountForDistributor=0;
			int i=0;
			long BrandCount=1;
			int DistCount=0;
			
			double DistributorTotalBalance = 0;
			double DistributorTotalSecondarySales = 0;
			double DistributorTotalLiftingTarget = 0;
			
			int RowCounterInner=1;
			
			
			int Srno=1;
			ResultSet rs2 = s3.executeQuery("SELECT dbpu.assigned_to,u.display_name,dbpu.id, dbp.label pjp_name FROM distributor_beat_plan_users dbpu join users u on dbpu.assigned_to=u.id join distributor_beat_plan dbp on dbp.id=dbpu.id where dbp.distributor_id = "+DistributorID+" and dbp.asm_id="+this.ASM_ID+" order by dbpu.id ");
			while(rs2.next()){
				
				Calendar c1 = Calendar.getInstance();
				c1.setTime(StartDate);
				int NumberOfDays=c1.getActualMaximum(Calendar.DAY_OF_MONTH);
				
				long PSRID = rs2.getLong(1);
				String PSRName = rs2.getString(2);
				
				String PJPLabel = rs2.getString(4);
				
				
				/* /////////Avg. Daily Target/////// */
				
				 double AvgDailyTargetCSD=0;
				double AvgDailyTargets=0;								
				double AvgDailyTargetsFinal=0;
				
				double AvgDailyTargetsSting=0;
				double AvgDailyTargetsStingFinal=0;
				
				double AvgDailyTargetsSlice=0;
				double AvgDailyTargetsSliceFinal=0;
				
				double AvgDailyTargetsSSRB=0;
				double AvgDailyTargetsSSRBFinal=0;
				
				double AvgDailyTargetsOtherSS=0;
				double AvgDailyTargetsOtherSSFinal=0;
				
				double AvgDailyTargets50175=0;
				double AvgDailyTargets50175Final=0;
				
				double AvgDailyTargets1000=0;
				double AvgDailyTargets1000Final=0;
				
				double AvgDailyTargets2250=0;
				double AvgDailyTargets2250Final=0;
				
				double MonthlyTargetTotal=0;
					
				
				
				ResultSet rs1 = s2.executeQuery("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+PSRID +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+")");
				while(rs1.next()){
					AvgDailyTargets = rs1.getDouble("daily_target");
				}
				
				//Sting
				
				ResultSet rs12 = s2.executeQuery("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+PSRID +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+") and etp.package_id=12");
				while(rs12.next()){
					AvgDailyTargetsSting = rs12.getDouble("daily_target");
				}
				
				//Slice
				
				ResultSet rs123 = s2.executeQuery("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+PSRID +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+") and etp.package_id=16");
				while(rs123.next()){
					AvgDailyTargetsSlice = rs123.getDouble("daily_target");
				}
				AvgDailyTargets= AvgDailyTargets - AvgDailyTargetsSting - AvgDailyTargetsSlice;
				
				//SSRB
				
				ResultSet rs1238 = s2.executeQuery("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+PSRID +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+") and etp.package_id in(11,12)");
				while(rs1238.next()){
					AvgDailyTargetsSSRB = rs1238.getDouble("daily_target");
				}
				
				//System.out.println("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+rs.getLong("assigned_to") +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+") and etp.package_id in(11,12)");
				//System.out.println("Hello - "+AvgDailyTargetsSSRB);
				
				
				//OtherSS
				
				ResultSet rs1239 = s2.executeQuery("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+PSRID +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+") and etp.package_id in(28,27,6,9)");
				while(rs1239.next()){
					AvgDailyTargetsOtherSS = rs1239.getDouble("daily_target");
				}
				
				
					//1.5/1.75
				
				ResultSet rs1234 = s2.executeQuery("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+PSRID +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+") and etp.package_id in (2,24)");
				while(rs1234.next()){
					AvgDailyTargets50175 = rs1234.getDouble("daily_target");
				}
				
				//1000
				
				ResultSet rs1235 = s2.executeQuery("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+PSRID +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+") and etp.package_id in (3)");
				while(rs1235.next()){
					AvgDailyTargets1000 = rs1235.getDouble("daily_target");
				}
				
				//2250
				
				ResultSet rs1236 = s2.executeQuery("SELECT sum(etp.quantity) daily_target FROM pep.employee_targets et join employee_targets_packages etp on et.id=etp.id where et.employee_id="+PSRID +" and et.month=month("+Utilities.getSQLDate(StartDate)+") and et.year=year("+Utilities.getSQLDate(StartDate)+") and etp.package_id in (29)");
				while(rs1236.next()){
					AvgDailyTargets2250 = rs1236.getDouble("daily_target");
				}
				
				
				
				//System.out.println("Hello "+c1.getActualMaximum(Calendar.DAY_OF_MONTH) );
												
				
				AvgDailyTargetCSD = (AvgDailyTargets)/(NumberOfDays-Utilities.getFridayCountByDate(StartDate));
				
				AvgDailyTargetsFinal = (AvgDailyTargets+AvgDailyTargetsSting+AvgDailyTargetsSlice) /(NumberOfDays-Utilities.getFridayCountByDate(StartDate));
				AvgDailyTargetsStingFinal = AvgDailyTargetsSting/(NumberOfDays-Utilities.getFridayCountByDate(StartDate));
				AvgDailyTargetsSliceFinal = AvgDailyTargetsSlice/(NumberOfDays-Utilities.getFridayCountByDate(StartDate));
				AvgDailyTargetsSSRBFinal = AvgDailyTargetsSSRB/(NumberOfDays-Utilities.getFridayCountByDate(StartDate));
				AvgDailyTargetsOtherSSFinal = AvgDailyTargetsOtherSS/(NumberOfDays-Utilities.getFridayCountByDate(StartDate));
				AvgDailyTargets50175Final = AvgDailyTargets50175/(NumberOfDays-Utilities.getFridayCountByDate(StartDate));
				AvgDailyTargets1000Final = AvgDailyTargets1000/(NumberOfDays-Utilities.getFridayCountByDate(StartDate));
				AvgDailyTargets2250Final = AvgDailyTargets2250/(NumberOfDays-Utilities.getFridayCountByDate(StartDate));
				
				
				MonthlyTargetTotal =AvgDailyTargets+AvgDailyTargetsSting+AvgDailyTargetsSlice;
				
				//System.out.println(NumberOfDays+" - "+Utilities.numberOfFridays(StartDate)+" - "+AvgDailyTargets+" - "+AvgDailyTargetsFinal);
				
				
				
				
				//////////////////////////////
				
				
				
				////////Last Day Sale///////////
				///////////////////////////////
				
				double YesterdaySalesCSD=0;
				double YesterdaySalesSting=0;
				double YesterdaySalesAF=0;
				double YesterdaySalesSlice=0;
				double YesterdaySalesSSRB=0;
				double YesterdaySalesOtherSS=0;
				double YesterdaySales150175=0;
				double YesterdaySales1000=0;
				double YesterdaySales2250=0;
				
				double TotalYesterdaySales=0;
				
				
				int YesterdayDayNumber = -2;
				if (Utilities.getDayOfWeekByDate(StartDate) == 7){
					YesterdayDayNumber = -3;
				}
				
				
				//For CSD
				ResultSet rs32 = s2.executeQuery("SELECT sum(total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products  where cache_lrb_type_id=1  and cache_order_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,YesterdayDayNumber))+" and "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,YesterdayDayNumber))+" and cache_booked_by="+PSRID);
				//ResultSet rs2 = s2.executeQuery("SELECT sum(isap.total_units/ip.unit_per_sku) sale FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products ip on isap.product_id=ip.id where ip.lrb_type_id=1  and created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(StartDate,-1))+" and isa.booked_by="+rs.getLong("assigned_to"));
				while(rs32.next()){
					YesterdaySalesCSD = rs32.getDouble("sale");
				}
				
				//For Sting - Energy Drink
				ResultSet rs21 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=3  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,YesterdayDayNumber))+" and "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,YesterdayDayNumber))+" and cache_booked_by="+PSRID);
				//ResultSet rs21 = s2.executeQuery("SELECT sum(isap.total_units/ip.unit_per_sku) sale FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products ip on isap.product_id=ip.id where ip.lrb_type_id=3  and created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(StartDate,-1))+" and isa.booked_by="+rs.getLong("assigned_to"));
				while(rs21.next()){
					YesterdaySalesSting = rs21.getDouble("sale");
				}
				
				//For AF - Water
				
				ResultSet rs212 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=2  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,YesterdayDayNumber))+" and "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,YesterdayDayNumber))+" and cache_booked_by="+PSRID);
				//ResultSet rs212 = s2.executeQuery("SELECT sum(isap.total_units/ip.unit_per_sku) sale FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products ip on isap.product_id=ip.id where ip.lrb_type_id=2  and created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(StartDate,-1))+" and isa.booked_by="+rs.getLong("assigned_to"));
				while(rs212.next()){
					YesterdaySalesAF = rs212.getDouble("sale");
				}
				
				//For Slice - Juice
				ResultSet rs2123 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=4  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,YesterdayDayNumber))+" and "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,YesterdayDayNumber))+" and cache_booked_by="+PSRID);
				//ResultSet rs2123 = s2.executeQuery("SELECT sum(isap.total_units/ip.unit_per_sku) sale FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products ip on isap.product_id=ip.id where ip.lrb_type_id=4  and created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(StartDate,-1))+" and isa.booked_by="+rs.getLong("assigned_to"));
				while(rs2123.next()){
					YesterdaySalesSlice = rs2123.getDouble("sale");
				}
				
				//For SSRB
				ResultSet rs2128 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,YesterdayDayNumber))+" and "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,YesterdayDayNumber))+" and cache_package_id in(11,12) and cache_booked_by="+PSRID);
				//ResultSet rs2128 = s2.executeQuery("SELECT sum(isap.total_units/ip.unit_per_sku) sale FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products ip on isap.product_id=ip.id where ip.lrb_type_id=1  and created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(StartDate,-1))+" and isa.booked_by="+rs.getLong("assigned_to")+" and ip.package_id in(11,12)");
				while(rs2128.next()){
					YesterdaySalesSSRB = rs2128.getDouble("sale");
				}
				
				//For OtherSS
				ResultSet rs2129 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,YesterdayDayNumber))+" and "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,YesterdayDayNumber))+" and cache_package_id in(28,27,6,9) and cache_booked_by="+PSRID);
				//ResultSet rs2129 = s2.executeQuery("SELECT sum(isap.total_units/ip.unit_per_sku) sale FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products ip on isap.product_id=ip.id where ip.lrb_type_id=1  and created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(StartDate,-1))+" and isa.booked_by="+rs.getLong("assigned_to")+" and ip.package_id in(28,27,6,9)");
				while(rs2129.next()){
					YesterdaySalesOtherSS = rs2129.getDouble("sale");
				}
				
				//1.5/1.75
				ResultSet rs2124 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,YesterdayDayNumber))+" and "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,YesterdayDayNumber))+" and cache_package_id in(2,24) and cache_booked_by="+PSRID);
				//ResultSet rs2124 = s2.executeQuery("SELECT sum(isap.total_units/ip.unit_per_sku) sale FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products ip on isap.product_id=ip.id where ip.lrb_type_id=1  and created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(StartDate,-1))+" and isa.booked_by="+rs.getLong("assigned_to")+" and ip.package_id in(2,24)");
				while(rs2124.next()){
					YesterdaySales150175 = rs2124.getDouble("sale");
				}
				
				
				//1000
				ResultSet rs2125 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,YesterdayDayNumber))+" and "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,YesterdayDayNumber))+" and cache_package_id in(3) and cache_booked_by="+PSRID);
				//ResultSet rs2125 = s2.executeQuery("SELECT sum(isap.total_units/ip.unit_per_sku) sale FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products ip on isap.product_id=ip.id where ip.lrb_type_id=1  and created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(StartDate,-1))+" and isa.booked_by="+rs.getLong("assigned_to")+" and ip.package_id in(3)");
				while(rs2125.next()){
					YesterdaySales1000 = rs2125.getDouble("sale");
				}
				
				//2250
				ResultSet rs2126 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,YesterdayDayNumber))+" and "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,YesterdayDayNumber))+" and cache_package_id in(5)  and cache_booked_by="+PSRID);
				//ResultSet rs2126 = s2.executeQuery("SELECT sum(isap.total_units/ip.unit_per_sku) sale FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products ip on isap.product_id=ip.id where ip.lrb_type_id=1  and created_on between "+Utilities.getSQLDate(Utilities.getDateByDays(StartDate,-1))+" and "+Utilities.getSQLDateNext(Utilities.getDateByDays(StartDate,-1))+" and isa.booked_by="+rs.getLong("assigned_to")+" and ip.package_id in(5)");
				while(rs2126.next()){
					YesterdaySales2250 = rs2126.getDouble("sale");
				}
				
				TotalYesterdaySales = YesterdaySalesCSD+YesterdaySalesSting+YesterdaySalesAF+YesterdaySalesSlice;
				
				////////////////////////////////
				
				
				
				////////MTD Sale///////////
				///////////////////////////////
				
				double MTDSalesCSD=0;
				double MTDSalesSting=0;
				double MTDSalesAF=0;
				double MTDSalesSlice=0;
				double MTDSalesSSRB=0;
				double MTDSalesOtherSS=0;
				double MTDSales150175=0;
				double MTDSales1000=0;
				double MTDSales2250=0;
				
				double TotalMTDSales=0;
				
				Date MTDStartDate = Utilities.getStartDateByDate(StartDate);
				Date MTDEndDate = StartDate;  //MTD End Date will be equal to entered date of report
				
				//System.out.println(Utilities.getDisplayDateFormat(MTDStartDate)+" - "+Utilities.getDisplayDateFormat(MTDEndDate));
				
				
				//For CSD
				ResultSet MTDrs2 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and cache_booked_by="+PSRID);
				while(MTDrs2.next()){
					MTDSalesCSD = MTDrs2.getDouble("sale");
				}
				
				//For Sting - Energy Drink
				ResultSet MTDrs21 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=3  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and cache_booked_by="+PSRID);
				while(MTDrs21.next()){
					MTDSalesSting = MTDrs21.getDouble("sale");
				}
				
				//For AF - Water
				ResultSet MTDrs212 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=2  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and cache_booked_by="+PSRID);
				while(MTDrs212.next()){
					MTDSalesAF = MTDrs212.getDouble("sale");
				}
				
				//For Slice - Juice
				ResultSet MTDrs2123 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=4  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and cache_booked_by="+PSRID);
				while(MTDrs2123.next()){
					MTDSalesSlice = MTDrs2123.getDouble("sale");
				}
				
				//For SSRB
				ResultSet MTDrs2128 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and cache_package_id in(11,12) and cache_booked_by="+PSRID);
				while(MTDrs2128.next()){
					MTDSalesSSRB = MTDrs2128.getDouble("sale");
				}
				
				//For OtherSS
				ResultSet MTDrs2129 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and cache_package_id in(28,27,6,9) and cache_booked_by="+PSRID);
				while(MTDrs2129.next()){
					MTDSalesOtherSS = MTDrs2129.getDouble("sale");
				}
				
				//1.5/1.75
				ResultSet MTDrs2124 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and cache_package_id in(2,24)  and cache_booked_by="+PSRID);
				while(MTDrs2124.next()){
					MTDSales150175 = MTDrs2124.getDouble("sale");
				}
				
				
				//1000
				ResultSet MTDrs2125 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and cache_package_id in(3) and cache_booked_by="+PSRID);
				while(MTDrs2125.next()){
					MTDSales1000 = MTDrs2125.getDouble("sale");
				}
				
				//2250
				ResultSet MTDrs2126 = s2.executeQuery("SELECT sum(isap.total_units/cache_units_per_sku) sale FROM inventory_sales_adjusted_products isap  where cache_lrb_type_id=1  and isap.cache_order_created_on_date between "+Utilities.getSQLDate(MTDStartDate)+" and "+Utilities.getSQLDate(MTDEndDate)+" and cache_package_id in(5)  and cache_booked_by="+PSRID);
				while(MTDrs2126.next()){
					MTDSales2250 = MTDrs2126.getDouble("sale");
				}
				
				TotalMTDSales = MTDSalesCSD+MTDSalesSting+MTDSalesAF+MTDSalesSlice;
				
				////////////////////////////////
				
				 //AvgDailyTargetCSD = AvgDailyTargetsFinal-AvgDailyTargetsStingFinal-AvgDailyTargetsSliceFinal;
			
			
				//////////////////////////////////////////
				////////////MTD vs. Target %/////////////
				
				double MTDVsTargtetCSD=0;
				double MTDVsTargtetSting=0;
				//double MTDVsTargtetAF=(MTDSalesCSD/AvgDailyTargetsAF)*100;
				double MTDVsTargtetSlice=0;
				double MTDVsTargtetSSRB=0;
				double MTDVsTargtetOtherSS=0;
				double MTDVsTargtet1501725=0;
				double MTDVsTargtet1000=0;
				double MTDVsTargtet2250=0;
				
				double MTDVsTargtetTotal = 0;
				
				
				if(AvgDailyTargets!=0){
					 MTDVsTargtetCSD=(MTDSalesCSD/AvgDailyTargets)*100;	
				}
				
				if(AvgDailyTargetsSting!=0){
					MTDVsTargtetSting=(MTDSalesSting/AvgDailyTargetsSting)*100;	
				}
				
				if(AvgDailyTargetsSlice!=0){
					MTDVsTargtetSlice=(MTDSalesSlice/AvgDailyTargetsSlice)*100;	
				}
				
				if(AvgDailyTargetsSSRB!=0){
					MTDVsTargtetSSRB=(MTDSalesSSRB/AvgDailyTargetsSSRB)*100;	
				}
				 
				if(AvgDailyTargetsOtherSS!=0){
					MTDVsTargtetOtherSS=(MTDSalesOtherSS/AvgDailyTargetsOtherSS)*100;	
				}
				 
				if(AvgDailyTargets50175!=0){
					 MTDVsTargtet1501725=(MTDSales150175/AvgDailyTargets50175)*100;
				}
				
				if(AvgDailyTargets1000!=0){
					MTDVsTargtet1000=(MTDSales1000/AvgDailyTargets1000)*100;	
				}
				
				if(AvgDailyTargets2250!=0){
					MTDVsTargtet2250=(MTDSales2250/AvgDailyTargets2250)*100;	
				}
				 
				if(MonthlyTargetTotal!=0){
					MTDVsTargtetTotal = (TotalMTDSales/MonthlyTargetTotal)*100;	
				}
				
				 
				
				/////////////////////////////////////////
			
				
				/////////////////////////////////////////
				///////////Avg. Daily Balance Req.///////
				
				//MTD Target-MTDSale/Remaining days of Month
				
				int StartDateDays = Utilities.getDayNumberByDate(StartDate);
				int EndDateDays = Utilities.getDayNumberByDate(Utilities.getEndDateByDate(StartDate));
				int RemaingDaysOfMonth=EndDateDays-StartDateDays;
				
				
				double BalanceReqCSD=0;
				double BalanceReqSting=0;
				double BalanceReqSlice=0;
				double BalanceReqSSRB=0;
				double BalanceReqOtherSS=0;
				double BalanceReq1501725=0;
				double BalanceReq1000=0;
				double BalanceReq2250=0;
				double BalanceReqTotal=0;
				
				if(RemaingDaysOfMonth!=0){
					if(AvgDailyTargets!=0){
						BalanceReqCSD =  (AvgDailyTargets-MTDSalesCSD)/RemaingDaysOfMonth;
					}
					
					if(AvgDailyTargetsSting!=0){
						BalanceReqSting = (AvgDailyTargetsSting-MTDSalesSting)/RemaingDaysOfMonth;
					}
					
					if(AvgDailyTargetsSSRB!=0){
						BalanceReqSSRB = (AvgDailyTargetsSSRB-MTDSalesSSRB)/RemaingDaysOfMonth;	
					}
					
					if(AvgDailyTargetsOtherSS!=0){
						BalanceReqOtherSS = (AvgDailyTargetsOtherSS-MTDSalesOtherSS)/RemaingDaysOfMonth;
					}
					
					if(AvgDailyTargets50175!=0){
						BalanceReq1501725 = (AvgDailyTargets50175-MTDSales150175)/RemaingDaysOfMonth;
					}
					
					if(AvgDailyTargets1000!=0){
						BalanceReq1000 = (AvgDailyTargets1000-MTDSales1000)/RemaingDaysOfMonth;
					}
					
					if(AvgDailyTargets2250!=0){
						BalanceReq2250 = (AvgDailyTargets2250-MTDSales2250)/RemaingDaysOfMonth;	
					}
					
					if(MonthlyTargetTotal!=0){
						BalanceReqTotal = (MonthlyTargetTotal-TotalMTDSales)/RemaingDaysOfMonth;
					}
					
					
				}

				
					//System.out.println("Old Row Count "+RowCount);
					
				
					
					//Daily Current Month Target ////////////////////////
					////////////////////////////////////////////////////
					
					
					////////////////////////////////////////////////////
					///////////////////////////////////////////////////
					
				    
				    for (int k = 0; k < rows.length; k++){
				    	
				    	double AvgDailyTarget = 0;
				    	double LastDaySale = 0;
				    	double AvgDailyBalanceRequired = 0;
				    	double MTDSale = 0;
				    	double MonthlyTarget = 0;
				    	double MTDTargetPercent = 0;
				    	
				    	
				    	double C2_AvgDailyTarget = 0;
				    	double C2_LastDaySale = 0;
				    	double C2_AvgDailyBalanceRequired = 0;
				    	double C2_MTDSale = 0;
				    	double C2_MonthlyTarget = 0;
				    	double C2_MTDTargetPercent = 0;
				    	
				    	switch (rows[k]){
				    		case "CSD":
				    			AvgDailyTarget = AvgDailyTargetCSD;
				    			LastDaySale = YesterdaySalesCSD;
				    			AvgDailyBalanceRequired = BalanceReqCSD;
				    			MTDSale = MTDSalesCSD;
				    			MonthlyTarget = AvgDailyTargets;
				    			MTDTargetPercent = MTDVsTargtetCSD;
				    			
				    			break;
				    		case "Sting":
				    			AvgDailyTarget = AvgDailyTargetsStingFinal;
				    			LastDaySale = YesterdaySalesSting;
				    			AvgDailyBalanceRequired = BalanceReqSting;
				    			MTDSale = MTDSalesSting;
				    			MonthlyTarget = AvgDailyTargetsSting;
				    			MTDTargetPercent = MTDVsTargtetSting;
				    			
				    			break;
				    		case "AF":
				    			AvgDailyTarget = 0;
				    			LastDaySale = YesterdaySalesAF;
				    			AvgDailyBalanceRequired = 0;
				    			MTDSale = MTDSalesAF;
				    			MonthlyTarget = 0;
				    			MTDTargetPercent = 0;
				    			
				    			break;
				    		case "Slice":
				    			AvgDailyTarget = AvgDailyTargetsSliceFinal;
				    			LastDaySale = YesterdaySalesSlice;
				    			AvgDailyBalanceRequired = BalanceReqSlice;
				    			MTDSale = MTDSalesSlice;
				    			MonthlyTarget = AvgDailyTargetsSlice;
				    			MTDTargetPercent = MTDVsTargtetSlice;
				    			
				    			break;
				    		case "Total":
				    			AvgDailyTarget = AvgDailyTargetsFinal;
				    			LastDaySale = TotalYesterdaySales;
				    			AvgDailyBalanceRequired = BalanceReqTotal;
				    			MTDSale = TotalMTDSales;
				    			MonthlyTarget = MonthlyTargetTotal;
				    			MTDTargetPercent = MTDVsTargtetTotal;
				    			
				    			break;
				    	}

				    	switch (rows2[k]){
			    		case "SSRB":
			    			
					    	C2_AvgDailyTarget = AvgDailyTargetsSSRBFinal;
					    	C2_LastDaySale = YesterdaySalesSSRB;
					    	C2_AvgDailyBalanceRequired = BalanceReqSSRB;
					    	C2_MTDSale = MTDSalesSSRB;
					    	C2_MonthlyTarget = AvgDailyTargetsSSRB;
					    	C2_MTDTargetPercent = MTDVsTargtetSSRB;
			    			
			    			break;
			    		case "Other SS":
			    			
					    	C2_AvgDailyTarget = AvgDailyTargetsOtherSSFinal;
					    	C2_LastDaySale = YesterdaySalesOtherSS;
					    	C2_AvgDailyBalanceRequired = BalanceReqOtherSS;
					    	C2_MTDSale = MTDSalesOtherSS;
					    	C2_MonthlyTarget = AvgDailyTargetsOtherSS;
					    	C2_MTDTargetPercent = MTDVsTargtetOtherSS;
			    			
			    			break;
			    		case "1.5/1.75":
			    			
					    	C2_AvgDailyTarget = AvgDailyTargets50175Final;
					    	C2_LastDaySale = YesterdaySales150175;
					    	C2_AvgDailyBalanceRequired = BalanceReq1501725;
					    	C2_MTDSale = MTDSales150175;
					    	C2_MonthlyTarget = AvgDailyTargets50175;
					    	C2_MTDTargetPercent = MTDVsTargtet1501725;
			    			
			    			break;
			    		case "1L":
			    			
					    	C2_AvgDailyTarget = AvgDailyTargets1000Final;
					    	C2_LastDaySale = YesterdaySales1000;
					    	C2_AvgDailyBalanceRequired = BalanceReq1000;
					    	C2_MTDSale = MTDSales1000;
					    	C2_MonthlyTarget = AvgDailyTargets1000;
					    	C2_MTDTargetPercent = MTDVsTargtet1000;
			    			
			    			break;
			    		case "2.25":
			    			
					    	C2_AvgDailyTarget = AvgDailyTargets2250Final;
					    	C2_LastDaySale = YesterdaySales2250;
					    	C2_AvgDailyBalanceRequired = BalanceReq2250;
					    	C2_MTDSale = MTDSales2250;
					    	C2_MonthlyTarget = AvgDailyTargets2250;
					    	C2_MTDTargetPercent = MTDVsTargtet2250;
			    			break;
			    	}
				    	
				    	
				    	if (C2_AvgDailyBalanceRequired < 0){
				    		C2_AvgDailyBalanceRequired = 0;
				    	}
				    	if (AvgDailyBalanceRequired < 0){
				    		AvgDailyBalanceRequired = 0;
				    	}
				    	
				    	
				    	
				    	
						XSSFRow headerrow2 = spreadsheet.createRow((short) RowCount+1);        
					    XSSFCell headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
				    	
				    if (k == 0){
						headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
						headercell2.setCellValue(PJPLabel);
					    SetNormalCellBackColorLeft(workbook,headercell2);
							
						headercell2 = (XSSFCell) headerrow2.createCell((short) 2);
						headercell2.setCellValue(PSRID +"-" +PSRName);
					    SetNormalCellBackColorLeft(workbook,headercell2);	
				    }else{
						headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
						headercell2.setCellValue("");
					    SetNormalCellBackColorLeft(workbook,headercell2);
							
						headercell2 = (XSSFCell) headerrow2.createCell((short) 2);
						headercell2.setCellValue("");
					    SetNormalCellBackColorLeft(workbook,headercell2);	
				    	
				    }
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 3);
				    headercell2.setCellValue(rows[k]);
				    if(rows[k].equals("Total")){
				    	SetNormalCellBackColorLeftYellowTotal(workbook,headercell2);
				    }else{
				    	SetNormalCellBackColorLeftYellow(workbook,headercell2);
				    }
				    
				    
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 4);				   
				    headercell2.setCellValue(Math.round(AvgDailyTarget));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    if(rows[k].equals("Total")){
				    	SetNormalCellBackColorLeftYellowTotal(workbook,headercell2);
				    }else{
				    	SetNormalCellBackColorLeftYellow(workbook,headercell2);
				    }
				    
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 5);
				    headercell2.setCellValue(Math.round(LastDaySale));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    if(rows[k].equals("Total")){
				    	SetNormalCellBackColorLeftYellowTotal(workbook,headercell2);
				    }else{
				    	SetNormalCellBackColorLeftYellow(workbook,headercell2);
				    }
				    
				    
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 6);
				    headercell2.setCellValue(Math.round(AvgDailyBalanceRequired));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    if(rows[k].equals("Total")){
				    	SetNormalCellBackColorLeftYellowTotal(workbook,headercell2);
				    }else{
				    	SetNormalCellBackColorLeftYellow(workbook,headercell2);
				    }
				    

			    	headercell2 = (XSSFCell) headerrow2.createCell((short) 7);
			    	headercell2.setCellValue(Math.round(MTDSale));
		    	  headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		    	  if(rows[k].equals("Total")){
				    	SetNormalCellBackColorLeftYellowTotal(workbook,headercell2);
				    }else{
				    	SetNormalCellBackColorLeftYellow(workbook,headercell2);
				    }
				    
				   
				    	 headercell2 = (XSSFCell) headerrow2.createCell((short) 8);
						    headercell2.setCellValue(Math.round(MonthlyTarget));
						    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
						    if(rows[k].equals("Total")){
						    	SetNormalCellBackColorLeftYellowTotal(workbook,headercell2);
						    }else{
						    	SetNormalCellBackColorLeftYellow(workbook,headercell2);
						    }
				    
				   
				    
				   
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 9);
				    headercell2.setCellValue(Math.round(MTDTargetPercent));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    if(rows[k].equals("Total")){
				    	SetNormalCellBackColorLeftYellowTotal(workbook,headercell2);
				    }else{
				    	SetNormalCellBackColorLeftYellow(workbook,headercell2);
				    }

				    headercell2 = (XSSFCell) headerrow2.createCell((short) 10);
				    headercell2.setCellValue(rows2[k]);
				    SetNormalCellBlackColorGray(workbook,headercell2);
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 11);				   
				    headercell2.setCellValue(Math.round(C2_AvgDailyTarget));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    
				    SetNormalCellBlackColorGray(workbook,headercell2);
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 12);
				    headercell2.setCellValue(Math.round(C2_LastDaySale));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBlackColorGray(workbook,headercell2);
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 13);
				    headercell2.setCellValue(Math.round(C2_AvgDailyBalanceRequired));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBlackColorGray(workbook,headercell2);
				    

			    	headercell2 = (XSSFCell) headerrow2.createCell((short) 14);
			    	headercell2.setCellValue(Math.round(C2_MTDSale));
		    	  headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		    	  SetNormalCellBlackColorGray(workbook,headercell2);
				    
				   
				    	 headercell2 = (XSSFCell) headerrow2.createCell((short) 15);
						    headercell2.setCellValue(Math.round(C2_MonthlyTarget));
						    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
						    SetNormalCellBlackColorGray(workbook,headercell2);
				    
				   
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 16);
				    headercell2.setCellValue(Math.round(C2_MTDTargetPercent));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBlackColorGray(workbook,headercell2);
				    
				    //Inserting in Temp Table
				    
				    RowCount++;
				    }
					 GrandTotalAvgDailyTargets+=AvgDailyTargetCSD;
					 GrandTotalAvgDailyTargetsSting+=AvgDailyTargetsStingFinal;
					 GrandTotalAvgDailyTargetsSlice+=AvgDailyTargetsSliceFinal;
					 GrandTotalAvgDailyTargetsSSRB+=AvgDailyTargetsSSRBFinal;
					 GrandTotalAvgDailyTargetsOtherSS+=AvgDailyTargetsOtherSSFinal;
					 GrandTotalAvgDailyTargets1501750+=AvgDailyTargets50175Final;
					 GrandTotalAvgDailyTargets1000+=AvgDailyTargets1000Final;
					 GrandTotalAvgDailyTargets2250+=AvgDailyTargets2250Final;
					 
					 
					 GrandTotalMTDTargets+=AvgDailyTargets;
					 GrandTotalMTDTargetsSting+=AvgDailyTargetsSting;
					 GrandTotalMTDTargetsSlice+=AvgDailyTargetsSlice;
					 GrandTotalMTDTargetsSSRB+=AvgDailyTargetsSSRB;
					 GrandTotalMTDTargetsOtherSS+=AvgDailyTargetsOtherSS;
					 GrandTotalMTDTargets1501750+=AvgDailyTargets50175;
					 GrandTotalMTDTargets1000+=AvgDailyTargets1000;
					 GrandTotalMTDTargets2250+=AvgDailyTargets2250;
					 
					
					
					 GrandTotalYesterdaySalesCSD+=YesterdaySalesCSD;
					 GrandTotalYesterdaySalesSting+=YesterdaySalesSting;
					 GrandTotalYesterdaySalesSlice+=YesterdaySalesSlice;
					 GrandTotalYesterdaySalesSSRB+=YesterdaySalesSSRB;
					 GrandTotalYesterdaySalesOtherSS+=YesterdaySalesOtherSS;
					 GrandTotalYesterdaySales1501750+=YesterdaySales150175;
					 GrandTotalYesterdaySales1000+=YesterdaySales1000;
					 GrandTotalYesterdaySales2250+=YesterdaySales2250;
					 GrandTotalYesterdaySalesAF+=YesterdaySalesAF;
					
					 GrandTotalMTDSalesCSD+=MTDSalesCSD;
					 GrandTotalMTDSalesSting+=MTDSalesSting;
					 GrandTotalMTDSalesSlice+=MTDSalesSlice;
					 GrandTotalMTDSalesSSRB+=MTDSalesSSRB;
					 GrandTotalMTDSalesOtherSS+=MTDSalesOtherSS;
					 GrandTotalMTDSales15001750+=MTDSales150175;
					 GrandTotalMTDSales1000+=MTDSales1000;
					 GrandTotalMTDSales2250+=MTDSales2250;
					 GrandTotalMTDSalesAF+=MTDSalesAF;
					
					
					 if(GrandTotalMTDTargets!=0){
						 GrandTotalMTDVsTargtetCSD =(GrandTotalMTDSalesCSD/GrandTotalMTDTargets)*100;
					 }
					 
					 if(GrandTotalMTDTargetsSting!=0){
						 GrandTotalMTDVsTargtetSting=(GrandTotalMTDSalesSting/GrandTotalMTDTargetsSting)*100;
					 }
					 
					 if(GrandTotalMTDTargetsSlice!=0){
						 GrandTotalMTDVsTargtetSlice=(GrandTotalMTDSalesSlice/GrandTotalMTDTargetsSlice)*100;
					 }
					
					 if(GrandTotalMTDTargetsSSRB!=0){
						 GrandTotalMTDVsTargtetSSRB=(GrandTotalMTDSalesSSRB/GrandTotalMTDTargetsSSRB)*100;	 
					 }
					 
					 if(GrandTotalMTDTargetsOtherSS!=0){
						 GrandTotalMTDVsTargtetOtherSS=(GrandTotalMTDSalesOtherSS/GrandTotalMTDTargetsOtherSS)*100;
					 }
					 
					 if(GrandTotalMTDTargets1501750!=0){
						 GrandTotalMTDVsTargtet15001750=(GrandTotalMTDSales15001750/GrandTotalMTDTargets1501750)*100;	 
					 }
					 
					if(GrandTotalMTDTargets1000!=0){
						 GrandTotalMTDVsTargtet1000=(GrandTotalMTDSales1000/GrandTotalMTDTargets1000)*100;
					}
					
					if(GrandTotalMTDTargets2250!=0){
						GrandTotalMTDVsTargtet2250=(GrandTotalMTDSales2250/GrandTotalMTDTargets2250)*100;
					}
					
					 
					
					if(RemaingDaysOfMonth!=0){
						
						if(GrandTotalMTDTargets!=0){
							GrandTotalBalanceReqCSD=(GrandTotalMTDTargets-GrandTotalMTDSalesCSD)/RemaingDaysOfMonth;
						}
						if(GrandTotalMTDTargetsSting!=0){
							GrandTotalBalanceReqSting=(GrandTotalMTDTargetsSting-GrandTotalMTDSalesSting)/RemaingDaysOfMonth;								
						}
						if(GrandTotalMTDTargetsSlice!=0){
							GrandTotalBalanceReqSlice=(GrandTotalMTDTargetsSlice-GrandTotalMTDSalesSlice)/RemaingDaysOfMonth;
						}
						if(GrandTotalMTDTargetsSSRB!=0){
							GrandTotalBalanceReqSSRB=(GrandTotalMTDTargetsSSRB-GrandTotalMTDSalesSSRB)/RemaingDaysOfMonth;
						}
						if(GrandTotalMTDTargetsOtherSS!=0){
							GrandTotalBalanceReqOtherSS=(GrandTotalMTDTargetsOtherSS-GrandTotalMTDSalesOtherSS)/RemaingDaysOfMonth;
						}
						if(GrandTotalMTDTargets1501750!=0){
							GrandTotalBalanceReq15001750=(GrandTotalMTDTargets1501750-GrandTotalMTDSales15001750)/RemaingDaysOfMonth;
						}
						if(GrandTotalMTDTargets1000!=0){
							GrandTotalBalanceReq1000=(GrandTotalMTDTargets1000-GrandTotalMTDSales1000)/RemaingDaysOfMonth;
						}
						if(GrandTotalMTDTargets2250!=0){
							GrandTotalBalanceReq2250=(GrandTotalMTDTargets2250-GrandTotalMTDSales2250)/RemaingDaysOfMonth;
						}
						
					
						 
					}
					
					//Total of Total
					 GrandTotalAvgDailyTargetsTotal+=AvgDailyTargetsFinal;
					 GrandTotalYesterdaySalesTotal+=TotalYesterdaySales;
					 GrandTotalMTDSalesTotal+=TotalMTDSales;
					 
					 
					 GrandTotalMTDTargetsTotal+=(AvgDailyTargets+AvgDailyTargetsSting+AvgDailyTargetsSlice);
					 
					 if(RemaingDaysOfMonth!=0 && GrandTotalMTDTargetsTotal!=0){
						 GrandTotalBalanceReqTotal=(GrandTotalMTDTargetsTotal-GrandTotalMTDSalesTotal)/RemaingDaysOfMonth;	 
					 }
					 
					 if(GrandTotalMTDTargetsTotal!=0){
						 GrandTotalMTDVsTargtetTotal=(GrandTotalMTDSalesTotal/GrandTotalMTDTargetsTotal)*100;	 
					 }
				    
				    
					}

			
			
			// Distributor Total Row
			
		    for (int k = 0; k < rows.length; k++){
		    	
		    	double AvgDailyTarget = 0;
		    	double LastDaySale = 0;
		    	double AvgDailyBalanceRequired = 0;
		    	double MTDSale = 0;
		    	double MonthlyTarget = 0;
		    	double MTDTargetPercent = 0;
		    	
		    	
		    	double C2_AvgDailyTarget = 0;
		    	double C2_LastDaySale = 0;
		    	double C2_AvgDailyBalanceRequired = 0;
		    	double C2_MTDSale = 0;
		    	double C2_MonthlyTarget = 0;
		    	double C2_MTDTargetPercent = 0;
		    	
		    	switch (rows[k]){
		    		case "CSD":
		    			AvgDailyTarget = GrandTotalAvgDailyTargets;
		    			LastDaySale = GrandTotalYesterdaySalesCSD;
		    			AvgDailyBalanceRequired = GrandTotalBalanceReqCSD;
		    			MTDSale = GrandTotalMTDSalesCSD;
		    			MonthlyTarget = GrandTotalMTDTargets;
		    			MTDTargetPercent = GrandTotalMTDVsTargtetCSD;
		    			
		    			break;
		    		case "Sting":
		    			AvgDailyTarget = GrandTotalAvgDailyTargetsSting;
		    			LastDaySale = GrandTotalYesterdaySalesSting;
		    			AvgDailyBalanceRequired = GrandTotalBalanceReqSting;
		    			MTDSale = GrandTotalMTDSalesSting;
		    			MonthlyTarget = GrandTotalMTDTargetsSting;
		    			MTDTargetPercent = GrandTotalMTDVsTargtetSting;
		    			
		    			break;
		    		case "AF":
		    			AvgDailyTarget = 0;
		    			LastDaySale = GrandTotalYesterdaySalesAF;
		    			AvgDailyBalanceRequired = 0;
		    			MTDSale = GrandTotalMTDSalesAF;
		    			MonthlyTarget = 0;
		    			MTDTargetPercent = 0;
		    			
		    			break;
		    		case "Slice":
		    			AvgDailyTarget = GrandTotalAvgDailyTargetsSlice;
		    			LastDaySale = GrandTotalYesterdaySalesSlice;
		    			AvgDailyBalanceRequired = GrandTotalBalanceReqSlice;
		    			MTDSale = GrandTotalMTDSalesSlice;
		    			MonthlyTarget = GrandTotalMTDTargetsSlice;
		    			MTDTargetPercent = GrandTotalMTDVsTargtetSlice;
		    			
		    			break;
		    		case "Total":
		    			AvgDailyTarget = GrandTotalAvgDailyTargetsTotal;
		    			LastDaySale = GrandTotalYesterdaySalesTotal;
		    			AvgDailyBalanceRequired = GrandTotalBalanceReqTotal;
		    			MTDSale = GrandTotalMTDSalesTotal;
		    			MonthlyTarget = GrandTotalMTDTargetsTotal;
		    			MTDTargetPercent = GrandTotalMTDVsTargtetTotal;
		    			
		    			break;
		    	}

		    	switch (rows2[k]){
	    		case "SSRB":
	    			
			    	C2_AvgDailyTarget = GrandTotalAvgDailyTargetsSSRB;
			    	C2_LastDaySale = GrandTotalYesterdaySalesSSRB;
			    	C2_AvgDailyBalanceRequired = GrandTotalBalanceReqSSRB;
			    	C2_MTDSale = GrandTotalMTDSalesSSRB;
			    	C2_MonthlyTarget = GrandTotalMTDTargetsSSRB;
			    	C2_MTDTargetPercent = GrandTotalMTDVsTargtetSSRB;
	    			
	    			break;
	    		case "Other SS":
	    			
			    	C2_AvgDailyTarget = GrandTotalAvgDailyTargetsOtherSS;
			    	C2_LastDaySale = GrandTotalYesterdaySalesOtherSS;
			    	C2_AvgDailyBalanceRequired = GrandTotalBalanceReqOtherSS;
			    	C2_MTDSale = GrandTotalMTDSalesOtherSS;
			    	C2_MonthlyTarget = GrandTotalMTDTargetsOtherSS;
			    	C2_MTDTargetPercent = GrandTotalMTDVsTargtetSting;
	    			
	    			break;
	    		case "1.5/1.75":
	    			
			    	C2_AvgDailyTarget = GrandTotalAvgDailyTargets1501750;
			    	C2_LastDaySale = GrandTotalYesterdaySales1501750;
			    	C2_AvgDailyBalanceRequired = GrandTotalBalanceReq15001750;
			    	C2_MTDSale = GrandTotalMTDSales15001750;
			    	C2_MonthlyTarget = GrandTotalMTDTargets1501750;
			    	C2_MTDTargetPercent = GrandTotalMTDVsTargtet15001750;
	    			
	    			break;
	    		case "1L":
	    			
			    	C2_AvgDailyTarget = GrandTotalAvgDailyTargets1000;
			    	C2_LastDaySale = GrandTotalYesterdaySales1000;
			    	C2_AvgDailyBalanceRequired = GrandTotalBalanceReq1000;
			    	C2_MTDSale = GrandTotalMTDSales1000;
			    	C2_MonthlyTarget = GrandTotalMTDTargets1000;
			    	C2_MTDTargetPercent = GrandTotalMTDVsTargtet1000;
	    			
	    			break;
	    		case "2.25":
	    			
			    	C2_AvgDailyTarget = GrandTotalAvgDailyTargets2250;
			    	C2_LastDaySale = GrandTotalYesterdaySales2250;
			    	C2_AvgDailyBalanceRequired = GrandTotalBalanceReq2250;
			    	C2_MTDSale = GrandTotalMTDSales2250;
			    	C2_MonthlyTarget = GrandTotalMTDTargets2250;
			    	C2_MTDTargetPercent = GrandTotalMTDVsTargtet2250;
	    			break;
	    	}
		    	
		    	
		    	if (C2_AvgDailyBalanceRequired < 0){
		    		C2_AvgDailyBalanceRequired = 0;
		    	}
		    	if (AvgDailyBalanceRequired < 0){
		    		AvgDailyBalanceRequired = 0;
		    	}

		    	
				XSSFRow headerrow2 = spreadsheet.createRow((short) RowCount+1);        
			    XSSFCell headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
		    	
		    if (k == 0){
				headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
				headercell2.setCellValue("");
			    SetNormalCellBackColorLeft(workbook,headercell2);
					
				headercell2 = (XSSFCell) headerrow2.createCell((short) 2);
				headercell2.setCellValue("Total");
				SetNormalCellBackColorLeft(workbook,headercell2);	
		    }else{
				headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
				headercell2.setCellValue("");
			    SetNormalCellBackColorLeft(workbook,headercell2);
					
				headercell2 = (XSSFCell) headerrow2.createCell((short) 2);
				headercell2.setCellValue("");
			    SetNormalCellBackColorLeft(workbook,headercell2);	
		    	
		    }
		    
		    headercell2 = (XSSFCell) headerrow2.createCell((short) 3);
		    headercell2.setCellValue(rows[k]);
		    SetNormalCellBackColorLeftYellow(workbook,headercell2);
		    
		    headercell2 = (XSSFCell) headerrow2.createCell((short) 4);				   
		    headercell2.setCellValue(Math.round(AvgDailyTarget));
		    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		    
		    SetNormalCellBackColorLeftYellow(workbook,headercell2);
		    
		    headercell2 = (XSSFCell) headerrow2.createCell((short) 5);
		    headercell2.setCellValue(Math.round(LastDaySale));
		    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		    SetNormalCellBackColorLeftYellow(workbook,headercell2);
		    
		    headercell2 = (XSSFCell) headerrow2.createCell((short) 6);
		    headercell2.setCellValue(Math.round(AvgDailyBalanceRequired));
		    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		    SetNormalCellBackColorLeftYellow(workbook,headercell2);
		    

	    	headercell2 = (XSSFCell) headerrow2.createCell((short) 7);
	    	headercell2.setCellValue(Math.round(MTDSale));
    	  headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
    	  SetNormalCellBackColorLeftYellow(workbook,headercell2);
		    
		   
		    	 headercell2 = (XSSFCell) headerrow2.createCell((short) 8);
				    headercell2.setCellValue(Math.round(MonthlyTarget));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColorLeftYellow(workbook,headercell2);
		    
		   
		    
		   
		    headercell2 = (XSSFCell) headerrow2.createCell((short) 9);
		    headercell2.setCellValue(Math.round(MTDTargetPercent));
		    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		    SetNormalCellBackColorLeftYellow(workbook,headercell2);

		    headercell2 = (XSSFCell) headerrow2.createCell((short) 10);
		    headercell2.setCellValue(rows2[k]);
		    SetNormalCellBlackColorGray(workbook,headercell2);
		    
		    headercell2 = (XSSFCell) headerrow2.createCell((short) 11);				   
		    headercell2.setCellValue(Math.round(C2_AvgDailyTarget));
		    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		    
		    SetNormalCellBlackColorGray(workbook,headercell2);
		    
		    headercell2 = (XSSFCell) headerrow2.createCell((short) 12);
		    headercell2.setCellValue(Math.round(C2_LastDaySale));
		    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		    SetNormalCellBlackColorGray(workbook,headercell2);
		    
		    headercell2 = (XSSFCell) headerrow2.createCell((short) 13);
		    headercell2.setCellValue(Math.round(C2_AvgDailyBalanceRequired));
		    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		    SetNormalCellBlackColorGray(workbook,headercell2);
		    

	    	headercell2 = (XSSFCell) headerrow2.createCell((short) 14);
	    	headercell2.setCellValue(Math.round(C2_MTDSale));
    	  headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
    	  SetNormalCellBlackColorGray(workbook,headercell2);
		    
		   
		    	 headercell2 = (XSSFCell) headerrow2.createCell((short) 15);
				    headercell2.setCellValue(Math.round(C2_MonthlyTarget));
				    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBlackColorGray(workbook,headercell2);
		    
		   
		    headercell2 = (XSSFCell) headerrow2.createCell((short) 16);
		    headercell2.setCellValue(Math.round(C2_MTDTargetPercent));
		    headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
		    SetNormalCellBlackColorGray(workbook,headercell2);
		    
		    //Inserting in Temp Table
		    
		    RowCount++;
		    }				

				//RowCount++;
				

			
	     
	     
	     
	     
	     
	     ///////////////////////////////////////////////////////////////////
	     //////////////////////////////////////////////////////////////////
	      
	      
			
			double DistributorAverageSecondaySales = DistributorTotalSecondarySales / DaysInLastMonth;

			
			double DistributorStockDaysLastMonthActual = 0;
			if (DistributorAverageSecondaySales != 0){
				DistributorStockDaysLastMonthActual = DistributorTotalBalance / DistributorAverageSecondaySales;
			}
			
			double DistributorDailyTargetLifting = Math.round(DistributorTotalLiftingTarget / Utilities.getDayNumberByDate(Utilities.getEndDateByDate(EndDate)));
			

			double DistributorStockDaysCurrentMonthTarget = 0;
			if (DistributorDailyTargetLifting != 0){
				DistributorStockDaysCurrentMonthTarget = DistributorTotalBalance / DistributorDailyTargetLifting;
			}

			
			
	      // FirstColCount=FirstColCount+5;
	       RowCount++;
	       FirstRowCount=RowCount;
	       
	       DistributorIDs += DistributorID+",";
	       
	       
	       
			}// end of distributor while
			 
			
			//System.out.println(DistributorIDs);
			
			/////////////////////////////////////////
		    
			
			//Auto Sizing Column
		    
		    for(int i=1;i<17;i++){
		    	spreadsheet.autoSizeColumn(i);
		    }
		    
			
			
			FileOutputStream out = new FileOutputStream(
				      new File(filename));
				      workbook.write(out);
				      out.close();
				      //System.out.println(
				      //"typesofcells.xlsx written successfully");
				      
	}
	
	public void Set2ndHeaderBackColor(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(222, 222, 222));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	     /* style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);*/
	      style61.setAlignment(CellStyle.ALIGN_CENTER);
	      headercell.setCellStyle(style61);
	}
	
	public void Set3rdHeaderBackColor(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(238, 237, 238));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
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
	
	public void Set3rdHeaderBackColorMinToLifted(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(251, 188, 210));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
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
	
	
	public void SetNormalCellBackColor(XSSFWorkbook workbook,XSSFCell headercell){
		
	
		headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBlackColorGray(XSSFWorkbook workbook,XSSFCell headercell){
		
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(250, 250, 248));
			
			XSSFCellStyle style62 = workbook.createCellStyle();
		      style62.setFillBackgroundColor(
		      HSSFColor.BLUE.index );
		      style62.setFillForegroundColor(BackColor2ndHeader);
		      style62.setFillPattern(CellStyle.SOLID_FOREGROUND);
		      
		      
		     
		      style62.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style62.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style62.setBorderRight(HSSFCellStyle.BORDER_THIN);
				style62.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style62.setAlignment(CellStyle.ALIGN_CENTER);
				headercell.setCellStyle(style62);
				
		      headercell.setCellStyle(style62);
	}
	
	public void SetNormalCellBackColorLeft(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
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
	
	
	public void SetNormalCellBackColorLeftYellow(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 230));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
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
	
	public void SetNormalCellBackColorLeftYellowTotal(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(247, 255, 247));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
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
	
	
	public void SetNormalCellBackColorLeftTotal(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 203, 173));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
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
	
	public void SetNormalCellBackColorCenterTotal(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 203, 173));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
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
	
	public void SetNormalCellBackColorRed(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 214, 231));
		
		XSSFCellStyle style61 = workbook.createCellStyle();
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
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
	
	public void SetBorder(XSSFWorkbook workbook,XSSFCell headercell){
		XSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style);
	}
	
}
