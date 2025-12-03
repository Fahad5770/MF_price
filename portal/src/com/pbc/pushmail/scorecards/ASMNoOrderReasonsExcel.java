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



public class ASMNoOrderReasonsExcel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath()+ "/CashInflowSummary.pdf";
	
	
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	Statement s10;
	
	
	Date StartDate = Utilities.getDateByDays(0); //Utilities.parseDate("13/02/2016");
	Date EndDate = Utilities.getDateByDays(0);//Utilities.parseDate("13/02/2016");
	
	
	////Date StartDate = Utilities.parseDate("01/09/2016");
	////Date EndDate = Utilities.parseDate("28/09/2016");
	
	
	Date Yesterday = Utilities.getDateByDays(-2);
	
	
	long ASM_ID = 0;
	
	public static void main(String[] args)throws Exception 
	   {
		
		
		
		
			   
	   }
	
	public ASMNoOrderReasonsExcel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
		s10 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6){
			Yesterday = Utilities.getDateByDays(-3);
		}
		
	}
	
	
	public void createPdf(String filename, long ASM_ID,boolean isMTD) throws  IOException, SQLException {
		
		
		
		if (isMTD){
			this.StartDate = Utilities.getStartDateByDate(Utilities.getDateByDays(0));
			this.EndDate = Utilities.getDateByDays(0);
		}
		
		
			XSSFWorkbook workbook = new XSSFWorkbook(); 
			XSSFSheet spreadsheet = workbook.createSheet("cell types");
	      
	      
	      
	      
	      int month = Utilities.getMonthNumberByDate(EndDate);
			int year = Utilities.getYearByDate(EndDate);
			
			
			
			int FirstRowCount=0;
			
			int RowCount=0;
			
			long DistributorID1=0;
			
			
			
			//Report Heading
			
			 XSSFRow rowH = spreadsheet.createRow((short) RowCount);	      
		     XSSFCell cellH = (XSSFCell) rowH.createCell((short) 0);
		     
		     cellH.setCellValue("No Sale Outlets");
			
		     spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  FirstRowCount, //first row (0-based)
		    		  FirstRowCount, //last row (0-based)
		    		  0, //first column (0-based)
		    	      2 //last column (0-based)
		    	      ));
			
		     
		     CellStyle style = workbook.createCellStyle();//Create style
		     XSSFFont font = workbook.createFont();//Create font
		     font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		     style.setFont(font);//set it to bold
		     cellH.setCellStyle(style);
		     
			
		     RowCount = RowCount+1;
			FirstRowCount = FirstRowCount+1;
			//Printing Date
			
			//Report Heading
			
			 XSSFRow rowP = spreadsheet.createRow((short) RowCount);	      
		     
			 XSSFCell cellP = (XSSFCell) rowP.createCell((short) 0);		     
		   
		     
		     cellP.setCellValue("Period: "+Utilities.getDisplayDateFormat(StartDate)+" - "+Utilities.getDisplayDateFormat(EndDate));
				
		     spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  FirstRowCount, //first row (0-based)
		    		  FirstRowCount, //last row (0-based)
		    		  0, //first column (0-based)
		    	      2 //last column (0-based)
		    	      ));
		     
		    
		     
		     cellP = (XSSFCell) rowP.createCell((short) 3);		     
		     cellP.setCellValue("");
		     
		     cellP = (XSSFCell) rowP.createCell((short) 4);		     
		     cellP.setCellValue("");
			
		     cellP = (XSSFCell) rowP.createCell((short) 5);		     
		     cellP.setCellValue("");
			 
			 
		    
		    
		     XSSFCellStyle style611 = workbook.createCellStyle();		      
		     style611.setAlignment(CellStyle.ALIGN_RIGHT);
		     cellP.setCellStyle(style611);
			
			String WhereSND = " and distributor_id in (select distributor_id from distributor_beat_plan where asm_id="+ASM_ID+") limit 1 ";
			if (ASM_ID == 0){
				WhereSND = "";
			}
			
			ResultSet rs23 = s10.executeQuery("select distinct mo.distributor_id from mobile_order mo where mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereSND);
			
			while(rs23.next()){
				
				DistributorID1 = rs23.getLong(1);
			
				
				long RegionID=0;
				String RegionName="";
				long SNDID=0;
				String SNDName="";
				String DistributorName="";
				
				
				ResultSet rs1 = s.executeQuery("SELECT name,region_id,(select region_short_name from common_regions cr where cr.region_id=cd.region_id) region_name,snd_id,(select short_name from common_sd_groups csg where csg.snd_id=cd.snd_id) snd_name FROM common_distributors cd where distributor_id="+DistributorID1);
				while(rs1.next()){
					 RegionID= rs1.getLong("region_id");
					 RegionName=rs1.getString("region_name");
					 SNDID=rs1.getLong("snd_id");
					 SNDName=rs1.getString("snd_name");
					 DistributorName=rs1.getString("name");
				}
				/*
				double TotalUniqueSKUSold = 0;
				ResultSet rs122 = s3.executeQuery("select count(distinct mop.product_id) total_lines_sold from mobile_order mo join mobile_order_products mop on mo.id = mop.id where mop.is_promotion = 0 and mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and beat_plan_id in (select id from distributor_beat_plan where asm_id="+ASM_ID+") ");
				if(rs122.first()){
					TotalUniqueSKUSold = rs122.getDouble("total_lines_sold");
				}
				
				
				double TotalAvgPlannedCalls = 0;
				ResultSet rs311 = s3.executeQuery("select avg(ct) avg_daily_sch_call from (select day_number, count(outlet_id) ct from distributor_beat_plan_schedule where id in (select id from distributor_beat_plan where asm_id="+ASM_ID+") group by day_number) tab1");
				while(rs311.next()){
					TotalAvgPlannedCalls = rs311.getInt("avg_daily_sch_call");
				}
				*
				
			//System.out.println("Row Count "+RowCount);
			//System.out.println("First Row Count "+FirstRowCount);
			//System.out.println("Distributor ID "+DistributorID1);
			
			
			
			
			
			
			
		      
		     
		     
		     // Extra Row
		     
		     
		     RowCount = RowCount+1;
		     FirstRowCount = FirstRowCount+1;	
				//Printing Date
				
				//Report Heading
				
				 XSSFRow rowEx1 = spreadsheet.createRow((short) RowCount);	      
			     
				 XSSFCell cellEx1 = (XSSFCell) rowEx1.createCell((short) 0);		     
			   
			     
				 cellEx1 = (XSSFCell) rowEx1.createCell((short) 0);	
				 cellEx1.setCellValue("");
				 
				 cellEx1 = (XSSFCell) rowEx1.createCell((short) 1);	
				 cellEx1.setCellValue("");						
			    
			     
				 
		     
			
		   /////// //////////////////////
		     
		    //2nd row header
		      
			     
		      
		      RowCount = RowCount+1;
		      
		      XSSFRow headerrow = spreadsheet.createRow((short) RowCount);	      
		      XSSFCell headercell = (XSSFCell) headerrow.createCell((short) 0);
		     
		      headercell.setCellValue(DistributorID1 +" - "+DistributorName+" | "+SNDName+" | "+RegionName);
		      Set2ndHeaderBackColorGray(workbook,headercell);
		      spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  FirstRowCount+1, //first row (0-based)
		    		  FirstRowCount+1, //last row (0-based)
		    		  0, //first column (0-based)
		    	      5 //last column (0-based)
		    	      ));
		      
		     
		          /*
		      headercell = (XSSFCell) headerrow.createCell((short) 8);	     
		      headercell.setCellValue("ORDER KPI");
		      Set2ndHeaderBackColorOrderKPI(workbook,headercell);
		      spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  FirstRowCount+1, //first row (0-based)
		    		  FirstRowCount+1, //last row (0-based)
		    		  8, //first column (0-based)
		    	      16 //last column (0-based)
		    	      ));
		     
		      
		      headercell = (XSSFCell) headerrow.createCell((short) 17);	     
		      headercell.setCellValue("DELIVERY KPI");
		      Set2ndHeaderBackColorDeliveryKPI(workbook,headercell);
		      spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  FirstRowCount+1, //first row (0-based)
		    		  FirstRowCount+1, //last row (0-based)
		    		  17, //first column (0-based)
		    	      29 //last column (0-based)
		    	      ));
		    */
		     
		      //3rd Row Header //////////////////////
			     ///////////////////////////////////////
			    
		      RowCount = RowCount+1;
			     
			     XSSFRow headerrow1 = spreadsheet.createRow((short) RowCount);        
			     XSSFCell headercell1 = (XSSFCell) headerrow1.createCell((short) 0);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 0);	     
			     headercell1.setCellValue("PJP ID");
			     Set3rdHeaderBackColor(workbook,headercell1);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 1);	     
			     headercell1.setCellValue("PJP Name");
			     Set3rdHeaderBackColor(workbook,headercell1);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 2);	     
			     headercell1.setCellValue("PSR");
			     Set3rdHeaderBackColor(workbook,headercell1);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 3);	     
			     headercell1.setCellValue("Outlet ID");
			     Set3rdHeaderBackColor(workbook,headercell1);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 4);	     
			     headercell1.setCellValue("Outlet Name");
			     Set3rdHeaderBackColor(workbook,headercell1);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 5);	     
			     headercell1.setCellValue("Reason");
			     Set3rdHeaderBackColor(workbook,headercell1);
			     
			     
			     /* Actual Row Data *//////
			     //////////////////////////////////////////////////
			     
			     
			   
					long PJPID =0;
					String PJPName="";
					
					int GrandTotalOutlets=0;
					int GrandAvgDailySchCallTotal=0;
					double GrandTotalScheduledCalls=0;
					double GrandTotalOrdersFromPlannedOutlets=0;
					double GrandOrderProductivity=0;
					double GrandTotalUnplannedCalls=0;
					double GrandTotalOrders=0;
					double GrandTotalTotalQuantitySold=0;
					double GrandDropSize=0;
					double GrandTotalTotalLinesSold=0;
					double GrandSKUPerOrder=0;
					double GrandD_TotalTotalQuantitySold=0;
					double GrandSalesPerDay=0;
					double GrandD_TotalQuantityDispatched=0;
					double GrandD_Returns=0;
					double GrandD_DropSize=0;
					double GrandD_TotalTotalLinesSold=0;
					double GrandSKUPerInvoice=0;
					double GrandZeroSalesCount=0;
					double GrandDD_TotalTotalQuantitySold=0;
					double GrandTotalOrderCount=0;
					double GrandD_TotalInvoiceCount=0;
					double TotalD_UniqueSKUSold = 0;
					double GrandTotalWholeSale = 0;
					
					double GrandCallCompRate=0;
					
					double GrandTotalSchCalls=0;
					
					
					String AltRowColorGray="#f7f5f5;";			
					
					String AltRowColorBlue="#f6fdfd;";				
					
					String AltRowColorPink="#f8f6f9";
					
					s.executeUpdate("SET SESSION group_concat_max_len = 1000000");
					
					ResultSet rs2 = s.executeQuery("SELECT dbp.id, dbp.label, (select assigned_to from distributor_beat_plan_users where id = dbp.id) assigned_to, (select group_concat(distinct outlet_id) from distributor_beat_plan_schedule where id = dbp.id and day_number = dayofweek(date("+Utilities.getSQLDate(StartDate)+"))) total_outlet_ids FROM pep.distributor_beat_plan dbp where dbp.asm_id = "+ASM_ID+" having assigned_to is not null order by assigned_to desc limit 5");
					while(rs2.next()){
						
						int AltRowColorCount=1;
						long AssignedTo=rs2.getLong("assigned_to");
					
						PJPID = rs2.getLong("id");
						PJPName = rs2.getString("label");
						int TotalOutlets = rs2.getInt(3);
						
						String OutletIDs = rs2.getString("total_outlet_ids");
						
						String PSRName = "";
						ResultSet rs3 = s2.executeQuery("select display_name from users where id = "+AssignedTo);
						if (rs3.first()){
							PSRName = rs3.getString(1);
						}
						
						
						//////System.out.println("select id, name, reason from (select id, name, 'Not Visited' as reason from common_outlets where id in ("+OutletIDs+") and id not in (select distinct outlet_id from mobile_order where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+"  ) and id not in (select distinct outlet_id from mobile_order_zero where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and created_by ="+AssignedTo+") union select distinct outlet_id as id, (select name from common_outlets where id = outlet_id) as name, (select label from mobile_order_zero_reason_type where id = no_order_reason_type_v2) as reason from mobile_order_zero where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and created_by ="+AssignedTo+" and outlet_id in ("+OutletIDs+")) tab1 order by reason");
						
						
						ResultSet rs4 = s2.executeQuery("select id, name, reason from (select id, name, 'Not Visited' as reason from common_outlets where id in ("+OutletIDs+") and id not in (select distinct outlet_id from mobile_order where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+"  ) and id not in (select distinct outlet_id from mobile_order_zero where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and created_by ="+AssignedTo+") union select distinct outlet_id as id, (select name from common_outlets where id = outlet_id) as name, (select label from mobile_order_no_order_reason_type where id = no_order_reason_type_v2) as reason from mobile_order_zero where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and created_by ="+AssignedTo+" and outlet_id in ("+OutletIDs+")) tab1 order by reason");
						while(rs4.next()){
						
							long OutletID = rs4.getLong(1);
							String OutletName = rs4.getString(2);
							String reason = rs4.getString(3);
							
						
						RowCount = RowCount+1;
					
					XSSFRow headerrow2 = spreadsheet.createRow((short) RowCount);        
				    XSSFCell headercell2 = (XSSFCell) headerrow2.createCell((short) 0);
				
				
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 0);
					headercell2.setCellValue(PJPID);
				    SetNormalCellBackColorLeftAltGray(workbook,headercell2);
				    
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
				    headercell2.setCellValue(Utilities.truncateStringToMax(PJPName, 25));
				    //headercell2.getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
				    //headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColorLeft2(workbook,headercell2);
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 2);
				    headercell2.setCellValue(Utilities.truncateStringToMax(AssignedTo+"-"+PSRName, 25));
				    //headercell2.getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
				    //headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColorLeft2(workbook,headercell2);

				    headercell2 = (XSSFCell) headerrow2.createCell((short) 3);
				    headercell2.setCellValue(""+OutletID);
				    //headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColor(workbook,headercell2);
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 4);
				    headercell2.setCellValue(OutletName);
				    //headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColorLeft2(workbook,headercell2);
				    
				    headercell2 = (XSSFCell) headerrow2.createCell((short) 5);
				    headercell2.setCellValue(reason);
				    //headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColorGray(workbook,headercell2);
				    
				    
						}
					
					}
					
				   
				    
				    
								
								/*
					RowCount = RowCount+1;
					
					XSSFRow headerrow2G = spreadsheet.createRow((short) RowCount);        
				    XSSFCell headercell2G = (XSSFCell) headerrow2G.createCell((short) 0);
				
				
				    headercell2G = (XSSFCell) headerrow2G.createCell((short) 0);
					headercell2G.setCellValue("");
				    SetNormalCellBackColorLeftAltGray(workbook,headercell2G);
				    
				    headercell2G = (XSSFCell) headerrow2G.createCell((short) 1);
					headercell2G.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2G);
				    
				    
				    headercell2G = (XSSFCell) headerrow2G.createCell((short) 2);
				    headercell2G.setCellValue("Total");
				    //headercell2.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColor(workbook,headercell2G);

				    headercell2G = (XSSFCell) headerrow2G.createCell((short) 3);
				    headercell2G.setCellValue(0);
				    headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColorGray(workbook,headercell2G);
				    
				    headercell2G = (XSSFCell) headerrow2G.createCell((short) 4);
				    headercell2G.setCellValue(0);
				    headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColor(workbook,headercell2G);
				    
				    headercell2G = (XSSFCell) headerrow2G.createCell((short) 5);
				    headercell2G.setCellValue(0);
				    headercell2G.setCellType(Cell.CELL_TYPE_NUMERIC);
				    SetNormalCellBackColorGray(workbook,headercell2G);
							*/	    
								    
		
				    FirstRowCount = RowCount; //at the end of loop make bother counter same
				     
				     
				    FirstRowCount=FirstRowCount+1;
				    RowCount=RowCount+1;  
				     
				     
			}	  
				
			
			//Report Heading
			
			 XSSFRow rowPG = spreadsheet.createRow((short) RowCount+1);	      
		     
			 XSSFCell cellPG = (XSSFCell) rowPG.createCell((short) 0);		     
		   
		     
		     cellPG.setCellValue("Generated On: "+Utilities.getDisplayDateTimeFormat(new Date()));
				
		     spreadsheet.addMergedRegion(new CellRangeAddress(
		    		 RowCount+1, //first row (0-based)
		    		 RowCount+1, //last row (0-based)
		    		  0, //first column (0-based)
		    	      3 //last column (0-based)
		    	      ));
			
			
		    for(int i=0;i<30;i++){
		    	//System.out.println("Auto Sizing - "+i);
	    		try{
	    			spreadsheet.autoSizeColumn(i);
	    		}catch(Exception e){System.out.println(i);e.printStackTrace();}
		    }
		    
		    
		     /*
		     for (int x = 0; x < spreadsheet.getRow(0).getPhysicalNumberOfCells(); x++) {
		    	 spreadsheet.autoSizeColumn(x);
		     }*/
			
			
			
			
			
			
			FileOutputStream out = new FileOutputStream(
				      new File(filename));
				      workbook.write(out);
				      out.close();
				      //System.out.println(
				      //"typesofcells.xlsx written successfully");
	
				      
				      ds.dropConnection();
	}
	
	public void Set2ndHeaderBackColor(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(162, 162, 163));
		
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
	      
	      
	      XSSFFont font = workbook.createFont();
	      font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
	      //font.setFontHeightInPoints((short)10);
	      font.setColor(IndexedColors.WHITE.getIndex());
	      style61.setFont(font);
	}
	
	public void Set2ndHeaderBackColorGray(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(162, 162, 163));
		
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
	      style61.setAlignment(CellStyle.ALIGN_LEFT);
	      headercell.setCellStyle(style61);
	      
	    
	}
	
	public void Set2ndHeaderBackColorOrderKPI(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(95, 149, 153));
		
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
	      
	      XSSFFont font = workbook.createFont();
	      font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
	      //font.setFontHeightInPoints((short)10);
	      font.setColor(IndexedColors.WHITE.getIndex());
	      style61.setFont(font);
	}
	
	public void Set2ndHeaderBackColorDeliveryKPI(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(128, 95, 153));
		
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
	      
	      XSSFFont font = workbook.createFont();
	      font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
	      //font.setFontHeightInPoints((short)10);
	      font.setColor(IndexedColors.WHITE.getIndex());
	      style61.setFont(font);
	}
	
	public void Set3rdHeaderBackColor(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(227, 227, 227));
		
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
	
	public void Set3rdHeaderBackColorOrder(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(202, 232, 234));
		
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
	
	public void Set3rdHeaderBackColorDelivery(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(240, 230, 247));
		
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
	
	public void SetNormalCellBackColorOneDecimal(XSSFWorkbook workbook,XSSFCell headercell){
		
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
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.#"));
			
			headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorLeft2(XSSFWorkbook workbook,XSSFCell headercell){
		
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
	
	public void SetNormalCellBackColorGray(XSSFWorkbook workbook,XSSFCell headercell){
		
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(245, 245, 245));
		
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
	
	public void SetNormalCellBackColorBlue(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(246, 253, 253));
		
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
	
	public void SetNormalCellBackColorPink(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 246, 249));
		
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
	
	public void SetNormalCellBackColorPercent(XSSFWorkbook workbook,XSSFCell headercell){
		
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
			
			style61.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorPercentGray(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(245, 245, 245));
		
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
			
			style61.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorPercentBlue(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(246, 253, 253));
		
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
			
			style61.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
			
	      headercell.setCellStyle(style61);
	}

	public void SetNormalCellBackColorPercentPink(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 246, 249));
		
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
			
			style61.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColorPercentPink12(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(248, 192, 197));
		
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
			
			style61.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
			
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
	
	public void SetNormalCellBackColorLeftAltGray(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(245, 245, 245));
		
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
