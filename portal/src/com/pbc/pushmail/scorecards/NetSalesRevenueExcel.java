package com.pbc.pushmail.scorecards;

import java.io.FileOutputStream;
import java.io.IOException;
 









import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
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



public class NetSalesRevenueExcel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath()+ "/CashInflowSummary.pdf";
	
	
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	
	
	Date StartDate = Utilities.getDateByDays(0); //Utilities.parseDate("13/02/2016");
	Date EndDate = Utilities.getDateByDays(0);//Utilities.parseDate("13/02/2016");
	
	String HtmlBodyData = "";
	
	///////////Date StartDate = Utilities.parseDate("13/02/2016");
	/////////////Date EndDate = Utilities.parseDate("13/02/2016");
	
	
	Date Yesterday = Utilities.getDateByDays(-1);
	
	
	
	long SND_ID = 0;
	
	public static void main(String[] args)throws Exception 
	   {
		/* 
		 * 
		NetTradePriceExcel obj = new NetTradePriceExcel();
		
		obj.createPdf("abc.xls", 2252);
		
		
		*/
			   
	   }
	
	public NetSalesRevenueExcel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		
		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6){
			Yesterday = Utilities.getDateByDays(-2);
		}
		
	}
	
	public String getHtmlBodyData(){
		return this.HtmlBodyData;
	}
	
	public void createPdf(String filename, long SND_ID) throws  IOException, SQLException {
		
			XSSFWorkbook workbook = new XSSFWorkbook(); 
			XSSFSheet spreadsheet = workbook.createSheet("cell types");
			
			int FirstRowCount=1;				
			int RowCount=1;			
			
			createTable("NSR", workbook, spreadsheet, FirstRowCount, RowCount);					
			createTable("Discount", workbook, spreadsheet, 37, 37);
			createTable("GSR", workbook, spreadsheet, 73, 73);
			
			spreadsheet.setColumnWidth(1, 9999);
			
			FileOutputStream out = new FileOutputStream(new File(filename));
			workbook.write(out);
			out.close();
			
	}
	
	public void createTable(String Title, XSSFWorkbook workbook, XSSFSheet spreadsheet, int FirstRowCount, int RowCount) throws SQLException {
		//Report Heading
		
		XSSFRow rowH = spreadsheet.createRow((short) RowCount);
		 
		XSSFCell cellH1 = (XSSFCell) rowH.createCell((short) 1);		     
		cellH1.setCellValue(Title);
		 
		spreadsheet.addMergedRegion(new CellRangeAddress(
			FirstRowCount, //first row (0-based)
			FirstRowCount+2, //last row (0-based)
			1, //first column (0-based)
			1 //last column (0-based)
			));
		
		XSSFColor FormatCellH1 = new XSSFColor(new java.awt.Color(112, 173, 71));			
		XSSFCellStyle styleH1 = workbook.createCellStyle();
		styleH1.setFillBackgroundColor(HSSFColor.BLUE.index );			
		styleH1.setFillForegroundColor(FormatCellH1);
		styleH1.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleH1.setAlignment(CellStyle.ALIGN_CENTER);
		styleH1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
		XSSFFont fontCellH1 = workbook.createFont();				
		fontCellH1.setColor(IndexedColors.WHITE.getIndex());
		fontCellH1.setBold(true);
		styleH1.setFont(fontCellH1);
		
		cellH1.setCellStyle(styleH1);
		

		XSSFCell cellH2 = (XSSFCell) rowH.createCell((short) 2);		     
		cellH2.setCellValue("Faisalabad");

		XSSFColor FormatCellH2 = new XSSFColor(new java.awt.Color(255, 192, 0));			
		XSSFCellStyle styleH2 = workbook.createCellStyle();
		styleH2.setFillBackgroundColor(HSSFColor.BLUE.index );			
		styleH2.setFillForegroundColor(FormatCellH2);
		styleH2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleH2.setAlignment(CellStyle.ALIGN_CENTER);			
		cellH2.setCellStyle(styleH2);
	     
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		  FirstRowCount, //first row (0-based)
	    		  FirstRowCount, //last row (0-based)
	    		  2, //first column (0-based)
	    	      37 //last column (0-based)
	    	      ));
	     
	     
		RowCount = RowCount+1;
		FirstRowCount = FirstRowCount+1;
		
		
	     XSSFRow row1 = spreadsheet.createRow((short) RowCount);
		 
	     XSSFCell cell1 = (XSSFCell) row1.createCell((short) 1);		     
	     cell1.setCellValue("space");
	     
	     XSSFCell cell11 = (XSSFCell) row1.createCell((short) 2);
	     cell11.setCellValue("Overall");
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		  FirstRowCount, //first row (0-based)
	    		  FirstRowCount, //last row (0-based)
	    		  2, //first column (0-based)
	    	      2+11 //last column (0-based)
	    	      ));
	     
	     XSSFCell cell12 = (XSSFCell) row1.createCell((short) 14);
	     cell12.setCellValue("Key City");
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		  FirstRowCount, //first row (0-based)
	    		  FirstRowCount, //last row (0-based)
	    		  14, //first column (0-based)
	    	      14+11 //last column (0-based)
	    	      ));
	     
	     XSSFCell cell13 = (XSSFCell) row1.createCell((short) 26);
	     cell13.setCellValue("ROF");
	     
	     spreadsheet.addMergedRegion(new CellRangeAddress(
	    		  FirstRowCount, //first row (0-based)
	    		  FirstRowCount, //last row (0-based)
	    		  26, //first column (0-based)
	    	      26+11 //last column (0-based)
	    	      ));

	    XSSFColor FormatCell11 = new XSSFColor(new java.awt.Color(255, 242, 204));
			XSSFCellStyle style11 = workbook.createCellStyle();
			style11.setFillBackgroundColor(HSSFColor.BLUE.index );			
			style11.setFillForegroundColor(FormatCell11);
			style11.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style11.setAlignment(CellStyle.ALIGN_CENTER);
			
			cell11.setCellStyle(style11);
	
		XSSFColor FormatCell12 = new XSSFColor(new java.awt.Color(221, 235, 247));
			XSSFCellStyle style12 = workbook.createCellStyle();
			style12.setFillBackgroundColor(HSSFColor.BLUE.index );			
			style12.setFillForegroundColor(FormatCell12);
			style12.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style12.setAlignment(CellStyle.ALIGN_CENTER);			
			cell12.setCellStyle(style12);
		
		XSSFColor FormatCell13 = new XSSFColor(new java.awt.Color(214, 220, 228));			
			XSSFCellStyle style13 = workbook.createCellStyle();
			style13.setFillBackgroundColor(HSSFColor.BLUE.index );			
			style13.setFillForegroundColor(FormatCell13);
			style13.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style13.setAlignment(CellStyle.ALIGN_CENTER);
			cell13.setCellStyle(style13);
		
		RowCount = RowCount+1;
		FirstRowCount = FirstRowCount+1;
		
		XSSFRow row2 = spreadsheet.createRow((short) RowCount);
		
		XSSFCell cell2 = (XSSFCell) row2.createCell((short) 1);
		cell2.setCellValue("space");
		
		if(Title.equals("NSR")){
			this.HtmlBodyData += "<tr><th style='text-align: left'>Package</th>";
		}
		
		Date DateArray[] = Utilities.getPastMonthsInDate(new Date(), 12);
		int CellCounter = 2;
		for(int i = 0; i < DateArray.length; i++){
			cell2 = (XSSFCell) row2.createCell((short) CellCounter++);
			cell2.setCellValue( Utilities.getDisplayDateMonthYearFormat(DateArray[i]) );
			
			XSSFColor FormatCell2 = new XSSFColor(new java.awt.Color(255, 242, 204));
			XSSFCellStyle stylecell12 = workbook.createCellStyle();
			stylecell12.setFillForegroundColor(FormatCell2);
			stylecell12.setFillPattern(CellStyle.SOLID_FOREGROUND);
			stylecell12.setAlignment(CellStyle.ALIGN_CENTER);
			stylecell12.setBorderTop(HSSFCellStyle.BORDER_THIN);
			stylecell12.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			stylecell12.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cell2.setCellStyle(stylecell12);
			
			if(Title.equals("NSR") && i > 5){
				this.HtmlBodyData += "<th style='text-align: center'>"+Utilities.getDisplayDateMonthYearFormat(DateArray[i])+"</th>";
			}
			
		}
		
		if(Title.equals("NSR")){
			this.HtmlBodyData += "</tr>";
		}
		
		for(int i = 0; i < DateArray.length; i++){
			cell2 = (XSSFCell) row2.createCell((short) CellCounter++);
			cell2.setCellValue( Utilities.getDisplayDateMonthYearFormat(DateArray[i]) );
			
			XSSFColor FormatCell2 = new XSSFColor(new java.awt.Color(221, 235, 247));
			XSSFCellStyle stylecell12 = workbook.createCellStyle();
			stylecell12.setFillForegroundColor(FormatCell2);
			stylecell12.setFillPattern(CellStyle.SOLID_FOREGROUND);
			stylecell12.setAlignment(CellStyle.ALIGN_CENTER);
			stylecell12.setBorderTop(HSSFCellStyle.BORDER_THIN);
			stylecell12.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			stylecell12.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cell2.setCellStyle(stylecell12);
			
		}
		
		
		for(int i = 0; i < DateArray.length; i++){
			cell2 = (XSSFCell) row2.createCell((short) CellCounter++);
			cell2.setCellValue( Utilities.getDisplayDateMonthYearFormat(DateArray[i]) );
			
			XSSFColor FormatCell2 = new XSSFColor(new java.awt.Color(214, 220, 227));
			XSSFCellStyle stylecell12 = workbook.createCellStyle();
			stylecell12.setFillForegroundColor(FormatCell2);
			stylecell12.setFillPattern(CellStyle.SOLID_FOREGROUND);
			stylecell12.setAlignment(CellStyle.ALIGN_CENTER);
			stylecell12.setBorderTop(HSSFCellStyle.BORDER_THIN);
			stylecell12.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			stylecell12.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cell2.setCellStyle(stylecell12);
		}
		
		
		ResultSet rs = s.executeQuery("SELECT id, label FROM inventory_products_lrb_types");
		while(rs.next()){
			int TypeID = rs.getInt(1);
			String TypeLabel = rs.getString(2);
			RowCount = RowCount+1;
			FirstRowCount = FirstRowCount+1;
			
			if(Title.equals("NSR") && TypeID < 4){
				this.HtmlBodyData += "<tr><td colspan='7' style='background: #cecece'>"+TypeLabel+"</td></tr>";
			}
			
			XSSFRow row_temp = spreadsheet.createRow((short) RowCount);
			
			XSSFCell cell_temp1 = (XSSFCell) row_temp.createCell((short) 1);
			cell_temp1.setCellValue(rs.getString(2));
			
			XSSFCell cell_temp12;
			for(int i = 1; i <= 36; i++){
				cell_temp12 = (XSSFCell) row_temp.createCell((short) i+1);
				cell_temp12.setCellValue("");
				
				XSSFColor FormatCellTemp12 = new XSSFColor(new java.awt.Color(255, 242, 204));
				
				if(i > 12 && i <= 24){
					FormatCellTemp12 = new XSSFColor(new java.awt.Color(221, 235, 247));
				}else if(i > 24 && i <= 36){
					FormatCellTemp12 = new XSSFColor(new java.awt.Color(214, 220, 227));
				}
				
				
				XSSFCellStyle styletemp12 = workbook.createCellStyle();
				styletemp12.setFillForegroundColor(FormatCellTemp12);
				styletemp12.setFillPattern(CellStyle.SOLID_FOREGROUND);
				styletemp12.setAlignment(CellStyle.ALIGN_CENTER);
				styletemp12.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				styletemp12.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cell_temp12.setCellStyle(styletemp12);
				
			}
			
			XSSFColor FormatCellTemp = new XSSFColor(new java.awt.Color(124, 124, 124));			
			XSSFCellStyle styletemp = workbook.createCellStyle();
			styletemp.setFillBackgroundColor(HSSFColor.WHITE.index);			
			styletemp.setFillForegroundColor(FormatCellTemp);
			styletemp.setFillPattern(CellStyle.SOLID_FOREGROUND);
			styletemp.setAlignment(CellStyle.ALIGN_LEFT);
			
			XSSFFont fonttemp = workbook.createFont();				
			fonttemp.setColor(IndexedColors.WHITE.getIndex());
			fonttemp.setBold(true);
		    styletemp.setFont(fonttemp);
		    
			cell_temp1.setCellStyle(styletemp);
			
			ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where is_visible = 1 and lrb_type_id="+rs.getString(1));
			while(rs2.next()){
				int PackageID = rs2.getInt(1);
				String PackageLabel = rs2.getString(2);
				RowCount = RowCount+1;
				FirstRowCount = FirstRowCount+1;
				
				
				XSSFRow row_temp2 = spreadsheet.createRow((short) RowCount);
				
				XSSFCell cell_temp2 = (XSSFCell) row_temp2.createCell((short) 1);
				cell_temp2.setCellValue(rs2.getString(2));
				
				XSSFCell cell_temp21;
				for(int i = 1; i <= 36; i++){
					cell_temp21 = (XSSFCell) row_temp2.createCell((short) i+1);		
					cell_temp21.setCellType(Cell.CELL_TYPE_NUMERIC);
					
					DecimalFormat CellValueFormat = new DecimalFormat("#.##");
					
					XSSFColor FormatCellTemp21 = new XSSFColor(new java.awt.Color(255, 242, 204));
					
					if(i <= 12){
						int Month = Utilities.getMonthNumberByDate(DateArray[i-1]);
						int Year = Utilities.getYearByDate(DateArray[i-1]);
						
						if(Title.equals("NSR")){
							ResultSet rs21 = s3.executeQuery("SELECT (sum(revenue)-sum(amount))/sum(cases) FROM peplogs.bi_nsr where is_franchise =1 and package_id ="+PackageID+" and lrb_type_id = "+TypeID+" and month = "+Month+" and year = "+Year);
							if(rs21.first()){
								
								double CellValue =  Double.valueOf(CellValueFormat.format(rs21.getDouble(1)));
								
								if(rs21.getDouble(1) != 0){
									cell_temp21.setCellValue(CellValue);									
								}else{
									cell_temp21.setCellValue("");
								}
								
								if(PackageID == 2 || PackageID == 24 || PackageID == 3 || PackageID == 5 || PackageID == 6 || PackageID == 11 || PackageID == 12){									
									if(i == 6){
										this.HtmlBodyData += "<tr><td>"+PackageLabel+"</td>";
									}
									
									if(i > 6 && i <= 12){
										if(CellValue != 0){
											this.HtmlBodyData += "<td style='text-align: right'>"+ Utilities.getDisplayCurrencyFormatRounded(CellValue)+"</td>";
										}else{
											this.HtmlBodyData += "<td style='text-align: right'>&nbsp;</td>";
										}
										
									}
									
									if(i == 12){
										this.HtmlBodyData += "</tr>";
									}									
								}
								
							}						
						}else if(Title.equals("Discount")){
							ResultSet rs21 = s3.executeQuery("SELECT sum(amount)/sum(cases) FROM peplogs.bi_nsr where is_franchise =1 and package_id ="+PackageID+" and lrb_type_id = "+TypeID+" and month = "+Month+" and year = "+Year);
							if(rs21.first()){							
								if(rs21.getDouble(1) != 0){
									//cell_temp21.setCellValue(Utilities.getDisplayCurrencyFormatRounded(rs21.getDouble(1)));
									double CellValue =  Double.valueOf(CellValueFormat.format(rs21.getDouble(1)));
									cell_temp21.setCellValue(CellValue);
								}else{
									cell_temp21.setCellValue("");
								}							
							}
						}else if(Title.equals("GSR")){
							ResultSet rs21 = s3.executeQuery("SELECT sum(revenue)/sum(cases) FROM peplogs.bi_nsr where is_franchise =1 and package_id ="+PackageID+" and lrb_type_id = "+TypeID+" and month = "+Month+" and year = "+Year);
							if(rs21.first()){							
								if(rs21.getDouble(1) != 0){
									//cell_temp21.setCellValue(Utilities.getDisplayCurrencyFormatRounded(rs21.getDouble(1)));
									double CellValue =  Double.valueOf(CellValueFormat.format(rs21.getDouble(1)));
									cell_temp21.setCellValue(CellValue);
								}else{
									cell_temp21.setCellValue("");
								}							
							}
						}
						
					}else if(i > 12 && i <= 24){
						FormatCellTemp21 = new XSSFColor(new java.awt.Color(221, 235, 247));
						
						int Month = Utilities.getMonthNumberByDate(DateArray[i-13]);
						int Year = Utilities.getYearByDate(DateArray[i-13]);
						
						if(Title.equals("NSR")){						
							ResultSet rs21 = s3.executeQuery("SELECT (sum(revenue)-sum(amount))/sum(cases) FROM peplogs.bi_nsr where is_faisalabad =1 and package_id ="+PackageID+" and lrb_type_id = "+TypeID+" and month = "+Month+" and year = "+Year);
							if(rs21.first()){							
								if(rs21.getDouble(1) != 0){
									//cell_temp21.setCellValue(Utilities.getDisplayCurrencyFormatRounded(rs21.getDouble(1)));
									double CellValue =  Double.valueOf(CellValueFormat.format(rs21.getDouble(1)));
									cell_temp21.setCellValue(CellValue);
								}else{
									cell_temp21.setCellValue("");
								}							
							}						
						}else if(Title.equals("Discount")){
							ResultSet rs21 = s3.executeQuery("SELECT sum(amount)/sum(cases) FROM peplogs.bi_nsr where is_faisalabad =1 and package_id ="+PackageID+" and lrb_type_id = "+TypeID+" and month = "+Month+" and year = "+Year);
							if(rs21.first()){							
								if(rs21.getDouble(1) != 0){
									//cell_temp21.setCellValue(Utilities.getDisplayCurrencyFormatRounded(rs21.getDouble(1)));
									double CellValue =  Double.valueOf(CellValueFormat.format(rs21.getDouble(1)));
									cell_temp21.setCellValue(CellValue);
								}else{
									cell_temp21.setCellValue("");
								}							
							}
						}else if(Title.equals("GSR")){
							ResultSet rs21 = s3.executeQuery("SELECT sum(revenue)/sum(cases) FROM peplogs.bi_nsr where is_faisalabad =1 and package_id ="+PackageID+" and lrb_type_id = "+TypeID+" and month = "+Month+" and year = "+Year);
							if(rs21.first()){							
								if(rs21.getDouble(1) != 0){
									//cell_temp21.setCellValue(Utilities.getDisplayCurrencyFormatRounded(rs21.getDouble(1)));
									double CellValue =  Double.valueOf(CellValueFormat.format(rs21.getDouble(1)));
									cell_temp21.setCellValue(CellValue);
								}else{
									cell_temp21.setCellValue("");
								}							
							}
						}
						
					}else if(i > 24 && i <= 36){
						FormatCellTemp21 = new XSSFColor(new java.awt.Color(214, 220, 227));
						
						int Month = Utilities.getMonthNumberByDate(DateArray[i-25]);
						int Year = Utilities.getYearByDate(DateArray[i-25]);
						
						if(Title.equals("NSR")){						
							ResultSet rs21 = s3.executeQuery("SELECT (sum(revenue)-sum(amount))/sum(cases) FROM peplogs.bi_nsr where is_rest_of_franchise =1 and package_id ="+PackageID+" and lrb_type_id = "+TypeID+" and month = "+Month+" and year = "+Year);
							if(rs21.first()){							
								if(rs21.getDouble(1) != 0){
									//cell_temp21.setCellValue(Utilities.getDisplayCurrencyFormatRounded(rs21.getDouble(1)));
									double CellValue =  Double.valueOf(CellValueFormat.format(rs21.getDouble(1)));
									cell_temp21.setCellValue(CellValue);
								}else{
									cell_temp21.setCellValue("");
								}							
							}						
						}else if(Title.equals("Discount")){
							ResultSet rs21 = s3.executeQuery("SELECT sum(amount)/sum(cases) FROM peplogs.bi_nsr where is_rest_of_franchise =1 and package_id ="+PackageID+" and lrb_type_id = "+TypeID+" and month = "+Month+" and year = "+Year);
							if(rs21.first()){							
								if(rs21.getDouble(1) != 0){
									//cell_temp21.setCellValue(Utilities.getDisplayCurrencyFormatRounded(rs21.getDouble(1)));
									double CellValue =  Double.valueOf(CellValueFormat.format(rs21.getDouble(1)));
									cell_temp21.setCellValue(CellValue);
								}else{
									cell_temp21.setCellValue("");
								}							
							}
						}else if(Title.equals("GSR")){
							ResultSet rs21 = s3.executeQuery("SELECT sum(revenue)/sum(cases) FROM peplogs.bi_nsr where is_rest_of_franchise =1 and package_id ="+PackageID+" and lrb_type_id = "+TypeID+" and month = "+Month+" and year = "+Year);
							if(rs21.first()){							
								if(rs21.getDouble(1) != 0){
									//cell_temp21.setCellValue(Utilities.getDisplayCurrencyFormatRounded(rs21.getDouble(1)));
									double CellValue =  Double.valueOf(CellValueFormat.format(rs21.getDouble(1)));
									cell_temp21.setCellValue(CellValue);
								}else{
									cell_temp21.setCellValue("");
								}							
							}
						}
						
					}
					
					XSSFCellStyle styletemp21 = workbook.createCellStyle();
					styletemp21.setFillForegroundColor(FormatCellTemp21);
					styletemp21.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styletemp21.setAlignment(CellStyle.ALIGN_CENTER);
					styletemp21.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					styletemp21.setBorderRight(HSSFCellStyle.BORDER_THIN);
					
					styletemp21.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("#,#"));
					
					cell_temp21.setCellStyle(styletemp21);
					
				}
				
				XSSFCellStyle styletemp2 = workbook.createCellStyle();
				styletemp2.setAlignment(CellStyle.ALIGN_LEFT);
				styletemp2.setIndention((short) 2);
				cell_temp2.setCellStyle(styletemp2);
				
			}
			
		}
		
		
		
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
	
	
	public void SetNormalCellBackColor(XSSFWorkbook workbook,XSSFCell headercell){
		
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
			style61.setAlignment(CellStyle.ALIGN_CENTER);
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
	      headercell.setCellStyle(style61);
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
