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



public class GetSalesExcel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath()+ "/CashInflowSummary.pdf";
	
	
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	
	
	Date StartDate = Utilities.getDateByDays(0); //Utilities.parseDate("13/02/2016");
	Date EndDate = Utilities.getDateByDays(0);//Utilities.parseDate("13/02/2016");
	
	
	///Date StartDate = Utilities.parseDate("29/08/2016");
	///Date EndDate = Utilities.parseDate("29/08/2016");
	
	
	Date Yesterday = Utilities.getDateByDays(-1);
	
	
	long SND_ID = 0;
	
	
	public static void main(String[] args)throws Exception 
   {
	
	
	
	
		   
   }
	
	public GetSalesExcel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		
		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6){
			Yesterday = Utilities.getDateByDays(-2);
		}
		
	}
	
	
	public void createPdf(String filename, long SND_ID, String StartDate, String EndDate,String DistributorID) throws  IOException, SQLException {
		
		
		
		
		XSSFWorkbook workbook = new XSSFWorkbook(); 
	    XSSFSheet spreadsheet = workbook.createSheet("cell types");
	      
	      
	      
	      
	      
			
			int FirstRowCount=0;
			
			int RowCount=0;
			
			
			
			//Report Heading
			
			 XSSFRow rowH = spreadsheet.createRow((short) RowCount);	      
		     XSSFCell cellH = (XSSFCell) rowH.createCell((short) 0);
		     
		     cellH.setCellValue("R318 - Sale Data Dump ");
			
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
		   
		     
		     cellP.setCellValue("Period: "+StartDate+" - "+EndDate);
				
		     spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  FirstRowCount, //first row (0-based)
		    		  FirstRowCount, //last row (0-based)
		    		  0, //first column (0-based)
		    	      1 //last column (0-based)
		    	      ));
			
			
		     
		     

		     RowCount = RowCount+1;
			FirstRowCount = FirstRowCount+1;
			XSSFCellStyle style61 = workbook.createCellStyle();
			
			
			//Report Heading
			
			 XSSFRow headerrow1 = spreadsheet.createRow((short) RowCount);        
		     XSSFCell headercell1 = (XSSFCell) headerrow1.createCell((short) 0);
		     
		    
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 0);	     
		     headercell1.setCellValue("Created Date");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 1);	     
		     headercell1.setCellValue("Created Time");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 2);	     
		     headercell1.setCellValue("Year");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 3);	     
		     headercell1.setCellValue("Month");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 4);	     
		     headercell1.setCellValue("Day");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 5);	     
		     headercell1.setCellValue("Day Name");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 6);	     
		     headercell1.setCellValue("Distributor ID");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 7);	     
		     headercell1.setCellValue("Distributor Name");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 8);	     
		     headercell1.setCellValue("Order ID");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 9);	     
		     headercell1.setCellValue("Sales Type ID");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 10);	     
		     headercell1.setCellValue("Outlet ID");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 11);	     
		     headercell1.setCellValue("Outlet Name");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 12);	     
		     headercell1.setCellValue("Channel ID");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 13);	     
		     headercell1.setCellValue("SND ID");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 14);	     
		     headercell1.setCellValue("Order Booker ID");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 15);	     
		     headercell1.setCellValue("Order Booker Name");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 16);	     
		     headercell1.setCellValue("SND Name");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 17);	     
		     headercell1.setCellValue("RSM ID");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 18);	     
		     headercell1.setCellValue("RSM Name");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 19);	     
		     headercell1.setCellValue("TDM ID");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 20);	     
		     headercell1.setCellValue("TDM Name");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 21);	     
		     headercell1.setCellValue("Channel Label");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 22);	     
		     headercell1.setCellValue("Invoice Amount");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 23);	     
		     headercell1.setCellValue("Region ID");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 24);	     
		     headercell1.setCellValue("Region Name");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 25);	     
		     headercell1.setCellValue("PJP ID");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 26);	     
		     headercell1.setCellValue("PJP Name");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 27);	     
		     headercell1.setCellValue("Dispatch ID");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 28);	     
		     headercell1.setCellValue("Package Label");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 29);	     
		     headercell1.setCellValue("Brand Label");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 30);	     
		     headercell1.setCellValue("Cases");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 31);	     
		     headercell1.setCellValue("Is Promotion");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 32);	     
		     headercell1.setCellValue("Converted Cases");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 33);	     
		     headercell1.setCellValue("Promotion Amount");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 34);	     
		     headercell1.setCellValue("Promotion Cases");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 35);	     
		     headercell1.setCellValue("SKU Amount");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 36);	     
		     headercell1.setCellValue("Discount Rate");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 37);	     
		     headercell1.setCellValue("Hand To Hand Amount");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 38);	     
		     headercell1.setCellValue("Dispatch Created On");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 39);	     
		     headercell1.setCellValue("Is Dispatch Adjusted");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 40);	     
		     headercell1.setCellValue("Dispatch Adjusted On");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 41);	     
		     headercell1.setCellValue("Sales Type");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 42);	     
		     headercell1.setCellValue("Document ID");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     headercell1 = (XSSFCell) headerrow1.createCell((short) 43);	     
		     headercell1.setCellValue("Spot Sale");
		     SetNormalCellBackColorH(workbook,headercell1,style61);
		     
		     
		      
		     
		     XSSFCellStyle style62 = workbook.createCellStyle();   
		     
		     RowCount=RowCount+1;
		     
		     String WhereDistributor="";
		     if(!DistributorID.equals("")) {
		    	 WhereDistributor =" and (isa.distributor_id in("+DistributorID+"))";
		     }
		     
		     System.out.println("Where Dist "+WhereDistributor);
		     
		    // System.out.println("SELECT * FROM pep.BI_SD_ORDERS_COMBINED where SD_CREATED_ON_DATE between '"+StartDate+"' and '"+EndDate+"'");
		     //System.out.println("SELECT * FROM pep.BI_SD_SALES bis join BI_SD_SALES_PRODUCTS bisp on bis.sd_document_id=bisp.sd_document_id where bis.SD_CREATED_ON_DATE between '"+StartDate+"' and '"+EndDate+"' and SD_DISTRIBUTOR_ID in ("+DistributorID+")");
		     /////ResultSet rs = s.executeQuery("SELECT * FROM pep.BI_SD_SALES bis join BI_SD_SALES_PRODUCTS bisp on bis.sd_document_id=bisp.sd_document_id where bis.SD_CREATED_ON_DATE between '"+StartDate+"' and '"+EndDate+"' and SD_DISTRIBUTOR_ID in ("+DistributorID+")");
		     
		     //Changed View to Inside Query for performance improvement by Zulqurna on 11/05/2020
		     System.out.println(
		    		 "SELECT "+
				    	        "`isa`.`id` AS `SD_DOCUMENT_ID`, "+
				    	        "CAST(`isa`.`created_on` AS DATE) AS `SD_CREATED_ON_DATE`, "+
				    	        "CAST(`isa`.`created_on` AS TIME) AS `SD_CREATED_ON_TIME`, "+
				    	        "YEAR(`isa`.`created_on`) AS `SD_CREATED_ON_YEAR`, "+
				    	        "MONTH(`isa`.`created_on`) AS `SD_CREATED_ON_MONTH`, "+
				    	        "DAYOFMONTH(`isa`.`created_on`) AS `SD_CREATED_ON_DAY`, "+
				    	        "DAYNAME(`isa`.`created_on`) AS `SD_CREATED_ON_DAY_NAME`, "+
				    	        "`cd`.`distributor_id` AS `SD_DISTRIBUTOR_ID`, "+
				    	        "CONCAT(`cd`.`distributor_id`, '-', `cd`.`name`) AS `SD_DISTRIBUTOR_LABEL`, "+
				    	        "`isa`.`order_id` AS `SD_ORDER_ID`, "+
				    	        "`isa`.`type_id` AS `SD_SALES_TYPE_ID`, "+
				    	        "`isa`.`outlet_id` AS `SD_OUTLET_ID`, "+
				    	        "`co`.`name` AS `SD_OUTLET_LABEL`, "+
				    	        "`co`.`channel_id` AS `SD_CHANNEL_ID`, "+
				    	        "`cd`.`snd_id` AS `SD_SND_ID`, "+
				    	        "`isa`.`booked_by` AS `SD_ORDER_BOOKER_ID`, "+
				    	        "(SELECT "+
				    	        "        CONCAT(`users`.`ID`, "+
				    	        "                    '-', "+
				    	        "                    `users`.`DISPLAY_NAME`) "+
				    	        "    FROM "+
				    	        "       `users` "+
				    	        "    WHERE "+
				    	        "        (`users`.`ID` = `isa`.`booked_by`)) AS `SD_ORDER_BOOKER_NAME`, "+
				    	        "(SELECT  "+
				    	        "        `users`.`DISPLAY_NAME` "+
				    	        "    FROM "+
				    	        "        `users` "+
				    	        "    WHERE "+
				    	        "        (`users`.`ID` = `cd`.`snd_id`)) AS `SD_SND_NAME`, "+
				    	        "`cd`.`rsm_id` AS `SD_RSM_ID`, "+
				    	        "(SELECT  "+
				    	        "        `users`.`DISPLAY_NAME` "+
				    	        "    FROM "+
				    	        "        `users` "+
				    	        "    WHERE "+
				    	        "        (`users`.`ID` = `cd`.`rsm_id`)) AS `SD_RSM_NAME`, "+
				    	        "`cd`.`tdm_id` AS `SD_TDM_ID`, "+
				    	        "(SELECT  "+
				    	        "        `users`.`DISPLAY_NAME` "+
				    	        "    FROM "+
				    	        "        `users` "+
				    		 	"	WHERE "+
				    	        "        (`users`.`ID` = `cd`.`tdm_id`)) AS `SD_TDM_NAME`, "+
				    	        "(SELECT "+
				    	        "        `common_outlets_channels`.`label` "+
				    	        "    FROM "+
				    	        "        `common_outlets_channels` "+
				    	        "    WHERE "+
				    	        "        (`common_outlets_channels`.`id` = `co`.`channel_id`)) AS `SD_CHANNEL_LABEL`, "+
				    	        "`isa`.`net_amount` AS `SD_INVOICE_AMOUNT`, "+
				    	        "`cd`.`region_id` AS `SD_REGION_ID`, "+
				    	        "(SELECT  "+
				    	        "        `common_regions`.`region_name` "+
				    	        "    FROM "+
				    	        "        `common_regions` "+
				    	        "    WHERE "+
				    	        "        (`common_regions`.`region_id` = `cd`.`region_id`)) AS `SD_REGION_LABEL`, "+
				    	        "`isa`.`beat_plan_id` AS `PJP_ID`, "+
				    	        "(SELECT  "+
				    	        "        `distributor_beat_plan`.`label` "+
				    	        "    FROM  "+
				    	        "        `distributor_beat_plan` "+
				    	        "    WHERE "+
				    	        "        (`distributor_beat_plan`.`id` = `isa`.`beat_plan_id`)) AS `PJP_LABEL`, "+
				    	        "IF((`isa`.`type_id` = 2), "+
				    	        "    (SELECT "+
				    	        "            `isdei`.`id` "+
				    	        "        FROM "+
				    	        "            `inventory_sales_dispatch_extra_invoices` `isdei` "+
				    	        "        WHERE "+
				    	        "            (`isdei`.`sales_id` = `isa`.`id`) "+
				    	        "        LIMIT 1), "+
				    	        "    (SELECT "+
				    	        "            `isdi`.`id` "+
				    	        "        FROM "+
				    	        "           `inventory_sales_dispatch_invoices` `isdi` " +
				    	        "        WHERE "+
				    	        "           (`isdi`.`sales_id` = `isa`.`id`) UNION ALL SELECT "+
				    	        "            `isdei`.`id` "+
				    	        "        FROM "+
				    	        "            `inventory_sales_dispatch_extra_invoices` `isdei` "+
				    	        "        WHERE "+
				    	        "            (`isdei`.`sales_id` = `isa`.`id`) "+
				    	        "        LIMIT 1)) AS `SD_DISPATCH_ID`, "+
				    	        "(SELECT  "+
				    	        "        `i_isd`.`created_on` "+
				    	        "    FROM "+
				    	        "        `inventory_sales_dispatch` `i_isd` "+
				    	        "    WHERE "+
				    	        "        (`i_isd`.`id` = `SD_DISPATCH_ID`)) AS `SD_DISPATCH_CREATED_ON`, "+
				    	        "(SELECT "+
				    	        "        `i_isd`.`is_adjusted` "+
				    	        "    FROM "+
				    	        "        `inventory_sales_dispatch` `i_isd` "+
				    	        "    WHERE "+
				    	        "        (`i_isd`.`id` = `SD_DISPATCH_ID`)) AS `SD_IS_DISPATCH_ADJUSTED`, "+
				    	        "(SELECT "+
				    	        "        `i_isd`.`adjusted_on` "+
				    	        "    FROM "+
				    	        "        `inventory_sales_dispatch` `i_isd` "+
				    	        "    WHERE "+
				    	        "        (`i_isd`.`id` = `SD_DISPATCH_ID`)) AS `SD_DISPATCH_ADJUSTED_ON`, "+
				    	        "(SELECT "+
				    	        "        `mo`.`is_spot_sale` "+
				    	        "    FROM "+
				    	        "        `mobile_order` `mo` "+
				    	        "    WHERE "+
				    	        "        (`MO`.`id` = `SD_ORDER_ID`)) AS `SPOT_SALE`, "+
				    	        "(SELECT  "+
				    	        "        `i_ist`.`label` "+
				    	        "    FROM "+
				    	        "        `inventory_sales_types` `i_ist` "+
				    	        "    WHERE "+
				    	        "        (`i_ist`.`id` = `isa`.`type_id`)) AS `SD_SALES_TYPE`, "+
				    	        "        `isap`.`id` AS `SD_DOCUMENT_ID`, "+
				    	        "`ipv`.`package_label` AS `SD_PACKAGE_LABEL`, "+
				    	        "`ipv`.`brand_label` AS `SD_BRAND_LABEL`, "+
				    	        "(`isap`.`total_units` / `ipv`.`unit_per_sku`) AS `SD_CASES`, "+
				    	        "`isap`.`is_promotion` AS `SD_IS_PROMOTION`, "+
				    	        "((`isap`.`total_units` * `ipv`.`liquid_in_ml`) / `ipv`.`conversion_rate_in_ml`) AS `SD_CONVERTED_CASES`, "+
				    	        "IF((`isap`.`is_promotion` = 1), "+
				    	        "    `isap`.`net_amount`, "+
				    	        "    0) AS `SD_PROMOTION_AMOUNT`, "+
				    	        "IF((`isap`.`is_promotion` = 1), "+
				    	        "    ((`isap`.`total_units` * `ipv`.`liquid_in_ml`) / `ipv`.`conversion_rate_in_ml`), "+
				    	        "    0) AS `SD_PROMOTION_CASES`, "+
				    	        "IF((`isap`.`is_promotion` = 0), "+
				    	        "    `isap`.`net_amount`, "+
				    	        "    0) AS `SD_SKU_AMOUNT`, "+
				    	        "(SELECT  "+
				    	        "        `sp`.`hand_to_hand` "+
				    	        "    FROM "+
				    	        "        (`sampling` `s` "+
				    	        "        JOIN `sampling_percase` `sp` ON ((`s`.`sampling_id` = `sp`.`sampling_id`))) "+
				    	        "    WHERE "+
				    	        "        ((CAST(`isap`.`cache_created_on_date` AS DATE) BETWEEN `s`.`activated_on` AND `s`.`deactivated_on`) "+
				    	        "            AND (CAST(`isap`.`cache_created_on_date` AS DATE) BETWEEN `sp`.`valid_from` AND `sp`.`valid_to`) "+
				    	        "            AND (`sp`.`package` = `isap`.`cache_package_id`) "+
				    	        "            AND (`sp`.`brand_id` = `isap`.`cache_brand_id`) "+
				    	        "            AND (`sp`.`brand_id` <> 0) "+
				    	        "            AND (`s`.`outlet_id` = `isap`.`cache_outlet_id`)) UNION ALL SELECT  "+
				    	        "       `sp`.`company_share` "+
				    	        "    FROM "+
				    	        "        ((`sampling` `s` "+
				    	        "        JOIN `sampling_percase` `sp` ON ((`s`.`sampling_id` = `sp`.`sampling_id`))) "+
				    	        "        JOIN `inventory_products` `ip` ON (((`sp`.`package` = `ip`.`package_id`) "+
				    	        "            AND (`ip`.`category_id` = 1)))) "+
				    	        "    WHERE "+
				    	        "        ((CAST(`isap`.`cache_created_on_date` AS DATE) BETWEEN `s`.`activated_on` AND `s`.`deactivated_on`) "+
				    		 	"			AND (CAST(`isap`.`cache_created_on_date` AS DATE) BETWEEN `sp`.`valid_from` AND `sp`.`valid_to`) "+
				    	        "            AND (`sp`.`package` = `isap`.`cache_package_id`) "+
				    	        "            AND (`sp`.`brand_id` = 0) "+
				    	        "            AND (`s`.`outlet_id` = `isap`.`cache_outlet_id`)) "+
				    	        "    LIMIT 1) AS `SD_DISCOUNT_RATE`, "+
				    	        "`isap`.`hand_discount_amount` AS `SD_HAND_TO_HAND_AMOUNT` "+
				    	    "FROM "+
				    	    "    ((`inventory_sales_adjusted` `isa` "+
				    	    "    JOIN `common_distributors` `cd` ON ((`isa`.`distributor_id` = `cd`.`distributor_id`))) "+
				    	    "    JOIN `common_outlets` `co` ON ((`isa`.`outlet_id` = `co`.`id`)) "+
				    	    "    JOIN `inventory_sales_adjusted_products` `isap` on isa.id=isap.id "+
				    	    "    JOIN `inventory_products_view` `ipv` ON (`isap`.`product_id` = `ipv`.`product_id`)) "+
				    	    "WHERE "+
				    	    "    (`isa`.`distributor_id` <> 123) and (isa.created_on between '"+StartDate+"' and '"+EndDate+"' )  "+WhereDistributor
				    		 );
		     ResultSet rs = s.executeQuery(
		    		 "SELECT "+
		    	        "`isa`.`id` AS `SD_DOCUMENT_ID`, "+
		    	        "CAST(`isa`.`created_on` AS DATE) AS `SD_CREATED_ON_DATE`, "+
		    	        "CAST(`isa`.`created_on` AS TIME) AS `SD_CREATED_ON_TIME`, "+
		    	        "YEAR(`isa`.`created_on`) AS `SD_CREATED_ON_YEAR`, "+
		    	        "MONTH(`isa`.`created_on`) AS `SD_CREATED_ON_MONTH`, "+
		    	        "DAYOFMONTH(`isa`.`created_on`) AS `SD_CREATED_ON_DAY`, "+
		    	        "DAYNAME(`isa`.`created_on`) AS `SD_CREATED_ON_DAY_NAME`, "+
		    	        "`cd`.`distributor_id` AS `SD_DISTRIBUTOR_ID`, "+
		    	        "CONCAT(`cd`.`distributor_id`, '-', `cd`.`name`) AS `SD_DISTRIBUTOR_LABEL`, "+
		    	        "`isa`.`order_id` AS `SD_ORDER_ID`, "+
		    	        "`isa`.`type_id` AS `SD_SALES_TYPE_ID`, "+
		    	        "`isa`.`outlet_id` AS `SD_OUTLET_ID`, "+
		    	        "`co`.`name` AS `SD_OUTLET_LABEL`, "+
		    	        "`co`.`channel_id` AS `SD_CHANNEL_ID`, "+
		    	        "`cd`.`snd_id` AS `SD_SND_ID`, "+
		    	        "`isa`.`booked_by` AS `SD_ORDER_BOOKER_ID`, "+
		    	        "(SELECT "+
		    	        "        CONCAT(`users`.`ID`, "+
		    	        "                    '-', "+
		    	        "                    `users`.`DISPLAY_NAME`) "+
		    	        "    FROM "+
		    	        "       `users` "+
		    	        "    WHERE "+
		    	        "        (`users`.`ID` = `isa`.`booked_by`)) AS `SD_ORDER_BOOKER_NAME`, "+
		    	        "(SELECT  "+
		    	        "        `users`.`DISPLAY_NAME` "+
		    	        "    FROM "+
		    	        "        `users` "+
		    	        "    WHERE "+
		    	        "        (`users`.`ID` = `cd`.`snd_id`)) AS `SD_SND_NAME`, "+
		    	        "`cd`.`rsm_id` AS `SD_RSM_ID`, "+
		    	        "(SELECT  "+
		    	        "        `users`.`DISPLAY_NAME` "+
		    	        "    FROM "+
		    	        "        `users` "+
		    	        "    WHERE "+
		    	        "        (`users`.`ID` = `cd`.`rsm_id`)) AS `SD_RSM_NAME`, "+
		    	        "`cd`.`tdm_id` AS `SD_TDM_ID`, "+
		    	        "(SELECT  "+
		    	        "        `users`.`DISPLAY_NAME` "+
		    	        "    FROM "+
		    	        "        `users` "+
		    		 	"	WHERE "+
		    	        "        (`users`.`ID` = `cd`.`tdm_id`)) AS `SD_TDM_NAME`, "+
		    	        "(SELECT "+
		    	        "        `common_outlets_channels`.`label` "+
		    	        "    FROM "+
		    	        "        `common_outlets_channels` "+
		    	        "    WHERE "+
		    	        "        (`common_outlets_channels`.`id` = `co`.`channel_id`)) AS `SD_CHANNEL_LABEL`, "+
		    	        "`isa`.`net_amount` AS `SD_INVOICE_AMOUNT`, "+
		    	        "`cd`.`region_id` AS `SD_REGION_ID`, "+
		    	        "(SELECT  "+
		    	        "        `common_regions`.`region_name` "+
		    	        "    FROM "+
		    	        "        `common_regions` "+
		    	        "    WHERE "+
		    	        "        (`common_regions`.`region_id` = `cd`.`region_id`)) AS `SD_REGION_LABEL`, "+
		    	        "`isa`.`beat_plan_id` AS `PJP_ID`, "+
		    	        "(SELECT  "+
		    	        "        `distributor_beat_plan`.`label` "+
		    	        "    FROM  "+
		    	        "        `distributor_beat_plan` "+
		    	        "    WHERE "+
		    	        "        (`distributor_beat_plan`.`id` = `isa`.`beat_plan_id`)) AS `PJP_LABEL`, "+
		    	        "IF((`isa`.`type_id` = 2), "+
		    	        "    (SELECT "+
		    	        "            `isdei`.`id` "+
		    	        "        FROM "+
		    	        "            `inventory_sales_dispatch_extra_invoices` `isdei` "+
		    	        "        WHERE "+
		    	        "            (`isdei`.`sales_id` = `isa`.`id`) "+
		    	        "        LIMIT 1), "+
		    	        "    (SELECT "+
		    	        "            `isdi`.`id` "+
		    	        "        FROM "+
		    	        "           `inventory_sales_dispatch_invoices` `isdi` " +
		    	        "        WHERE "+
		    	        "           (`isdi`.`sales_id` = `isa`.`id`) UNION ALL SELECT "+
		    	        "            `isdei`.`id` "+
		    	        "        FROM "+
		    	        "            `inventory_sales_dispatch_extra_invoices` `isdei` "+
		    	        "        WHERE "+
		    	        "            (`isdei`.`sales_id` = `isa`.`id`) "+
		    	        "        LIMIT 1)) AS `SD_DISPATCH_ID`, "+
		    	        "(SELECT  "+
		    	        "        `i_isd`.`created_on` "+
		    	        "    FROM "+
		    	        "        `inventory_sales_dispatch` `i_isd` "+
		    	        "    WHERE "+
		    	        "        (`i_isd`.`id` = `SD_DISPATCH_ID`)) AS `SD_DISPATCH_CREATED_ON`, "+
		    	        "(SELECT "+
		    	        "        `i_isd`.`is_adjusted` "+
		    	        "    FROM "+
		    	        "        `inventory_sales_dispatch` `i_isd` "+
		    	        "    WHERE "+
		    	        "        (`i_isd`.`id` = `SD_DISPATCH_ID`)) AS `SD_IS_DISPATCH_ADJUSTED`, "+
		    	        "(SELECT "+
		    	        "        `i_isd`.`adjusted_on` "+
		    	        "    FROM "+
		    	        "        `inventory_sales_dispatch` `i_isd` "+
		    	        "    WHERE "+
		    	        "        (`i_isd`.`id` = `SD_DISPATCH_ID`)) AS `SD_DISPATCH_ADJUSTED_ON`, "+
		    	        "(SELECT "+
		    	        "        `mo`.`is_spot_sale` "+
		    	        "    FROM "+
		    	        "        `mobile_order` `mo` "+
		    	        "    WHERE "+
		    	        "        (`mo`.`id` = `SD_ORDER_ID`)) AS `SPOT_SALE`, "+
		    	        "(SELECT  "+
		    	        "        `i_ist`.`label` "+
		    	        "    FROM "+
		    	        "        `inventory_sales_types` `i_ist` "+
		    	        "    WHERE "+
		    	        "        (`i_ist`.`id` = `isa`.`type_id`)) AS `SD_SALES_TYPE`, "+
		    	        "        `isap`.`id` AS `SD_DOCUMENT_ID`, "+
		    	        "`ipv`.`package_label` AS `SD_PACKAGE_LABEL`, "+
		    	        "`ipv`.`brand_label` AS `SD_BRAND_LABEL`, "+
		    	        "(`isap`.`total_units` / `ipv`.`unit_per_sku`) AS `SD_CASES`, "+
		    	        "`isap`.`is_promotion` AS `SD_IS_PROMOTION`, "+
		    	        "((`isap`.`total_units` * `ipv`.`liquid_in_ml`) / `ipv`.`conversion_rate_in_ml`) AS `SD_CONVERTED_CASES`, "+
		    	        "IF((`isap`.`is_promotion` = 1), "+
		    	        "    `isap`.`net_amount`, "+
		    	        "    0) AS `SD_PROMOTION_AMOUNT`, "+
		    	        "IF((`isap`.`is_promotion` = 1), "+
		    	        "    ((`isap`.`total_units` * `ipv`.`liquid_in_ml`) / `ipv`.`conversion_rate_in_ml`), "+
		    	        "    0) AS `SD_PROMOTION_CASES`, "+
		    	        "IF((`isap`.`is_promotion` = 0), "+
		    	        "    `isap`.`net_amount`, "+
		    	        "    0) AS `SD_SKU_AMOUNT`, "+
		    	        "(SELECT  "+
		    	        "        `sp`.`hand_to_hand` "+
		    	        "    FROM "+
		    	        "        (`sampling` `s` "+
		    	        "        JOIN `sampling_percase` `sp` ON ((`s`.`sampling_id` = `sp`.`sampling_id`))) "+
		    	        "    WHERE "+
		    	        "        ((CAST(`isap`.`cache_created_on_date` AS DATE) BETWEEN `s`.`activated_on` AND `s`.`deactivated_on`) "+
		    	        "            AND (CAST(`isap`.`cache_created_on_date` AS DATE) BETWEEN `sp`.`valid_from` AND `sp`.`valid_to`) "+
		    	        "            AND (`sp`.`package` = `isap`.`cache_package_id`) "+
		    	        "            AND (`sp`.`brand_id` = `isap`.`cache_brand_id`) "+
		    	        "            AND (`sp`.`brand_id` <> 0) "+
		    	        "            AND (`s`.`outlet_id` = `isap`.`cache_outlet_id`)) UNION ALL SELECT  "+
		    	        "       `sp`.`company_share` "+
		    	        "    FROM "+
		    	        "        ((`sampling` `s` "+
		    	        "        JOIN `sampling_percase` `sp` ON ((`s`.`sampling_id` = `sp`.`sampling_id`))) "+
		    	        "        JOIN `inventory_products` `ip` ON (((`sp`.`package` = `ip`.`package_id`) "+
		    	        "            AND (`ip`.`category_id` = 1)))) "+
		    	        "    WHERE "+
		    	        "        ((CAST(`isap`.`cache_created_on_date` AS DATE) BETWEEN `s`.`activated_on` AND `s`.`deactivated_on`) "+
		    		 	"			AND (CAST(`isap`.`cache_created_on_date` AS DATE) BETWEEN `sp`.`valid_from` AND `sp`.`valid_to`) "+
		    	        "            AND (`sp`.`package` = `isap`.`cache_package_id`) "+
		    	        "            AND (`sp`.`brand_id` = 0) "+
		    	        "            AND (`s`.`outlet_id` = `isap`.`cache_outlet_id`)) "+
		    	        "    LIMIT 1) AS `SD_DISCOUNT_RATE`, "+
		    	        "`isap`.`hand_discount_amount` AS `SD_HAND_TO_HAND_AMOUNT` "+
		    	    "FROM "+
		    	    "    ((`inventory_sales_adjusted` `isa` "+
		    	    "    JOIN `common_distributors` `cd` ON ((`isa`.`distributor_id` = `cd`.`distributor_id`))) "+
		    	    "    JOIN `common_outlets` `co` ON ((`isa`.`outlet_id` = `co`.`id`)) "+
		    	    "    JOIN `inventory_sales_adjusted_products` `isap` on isa.id=isap.id "+
		    	    "    JOIN `inventory_products_view` `ipv` ON (`isap`.`product_id` = `ipv`.`product_id`)) "+
		    	    "WHERE "+
		    	    "    (`isa`.`distributor_id` <> 123) and (isa.created_on between '"+StartDate+"' and '"+EndDate+"' )  "+WhereDistributor
		    		 );
		     
		     
		     while(rs.next()) {
		    	 
		    	 XSSFRow headerrow2 = spreadsheet.createRow((short) RowCount);        
			     XSSFCell headercell2 = (XSSFCell) headerrow2.createCell((short) 0);
		    	 
		    	 headercell2 = (XSSFCell) headerrow2.createCell((short) 0);	     
			     headercell2.setCellValue(rs.getString("SD_CREATED_ON_DATE"));
			     SetNormalCellBackColor(workbook,headercell2,style62);
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 1);	     
			     headercell2.setCellValue( rs.getString("SD_CREATED_ON_TIME"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 2);	     
			     headercell2.setCellValue(rs.getString("SD_CREATED_ON_YEAR"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 3);	     
			     headercell2.setCellValue(rs.getString("SD_CREATED_ON_MONTH"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 4);	     
			     headercell2.setCellValue(rs.getString("SD_CREATED_ON_DAY"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 5);	     
			     headercell2.setCellValue(rs.getString("SD_CREATED_ON_DAY_NAME"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 6);	     
			     headercell2.setCellValue(rs.getString("SD_DISTRIBUTOR_ID"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 7);	     
			     headercell2.setCellValue(rs.getString("SD_DISTRIBUTOR_LABEL"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 8);	     
			     headercell2.setCellValue(rs.getString("SD_ORDER_ID"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 9);	     
			     headercell2.setCellValue(rs.getString("SD_SALES_TYPE_ID"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 10);	     
			     headercell2.setCellValue(rs.getString("SD_OUTLET_ID"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 11);	     
			     headercell2.setCellValue(rs.getString("SD_OUTLET_LABEL"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 12);	     
			     headercell2.setCellValue(rs.getString("SD_CHANNEL_ID") );
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 13);	     
			     headercell2.setCellValue(rs.getString("SD_SND_ID"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 14);	     
			     headercell2.setCellValue(rs.getString("SD_ORDER_BOOKER_ID"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 15);	     
			     headercell2.setCellValue(rs.getString("SD_ORDER_BOOKER_NAME"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 16);	     
			     headercell2.setCellValue(rs.getString("SD_SND_NAME") );
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 17);	     
			     headercell2.setCellValue(rs.getString("SD_RSM_ID"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 18);	     
			     headercell2.setCellValue(rs.getString("SD_RSM_NAME"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 19);	     
			     headercell2.setCellValue(rs.getString("SD_TDM_ID") );
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 20);	     
			     headercell2.setCellValue(rs.getString("SD_TDM_NAME"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 21);	     
			     headercell2.setCellValue(rs.getString("SD_CHANNEL_LABEL"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 22);	     
			     headercell2.setCellValue(rs.getString("SD_INVOICE_AMOUNT"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 23);	     
			     headercell2.setCellValue(rs.getString("SD_REGION_ID"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;

			     headercell2 = (XSSFCell) headerrow2.createCell((short) 24);	     
			     headercell2.setCellValue(rs.getString("SD_REGION_LABEL"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 25);	     
			     headercell2.setCellValue(rs.getString("PJP_ID"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 26);	     
			     headercell2.setCellValue(rs.getString("PJP_LABEL"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 27);	     
			     headercell2.setCellValue(rs.getString("SD_DISPATCH_ID"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 28);	     
			     headercell2.setCellValue(rs.getString("SD_PACKAGE_LABEL"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 29);	     
			     headercell2.setCellValue(rs.getString("SD_BRAND_LABEL"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 30);	     
			     headercell2.setCellValue(rs.getString("SD_CASES"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			    
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 31);	     
			     headercell2.setCellValue(rs.getString("SD_IS_PROMOTION"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 32);	     
			     headercell2.setCellValue(rs.getString("SD_CONVERTED_CASES") );
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 33);	     
			     headercell2.setCellValue(rs.getString("SD_PROMOTION_AMOUNT"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 34);	     
			     headercell2.setCellValue(rs.getString("SD_PROMOTION_CASES") );
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 35);	     
			     headercell2.setCellValue(rs.getString("SD_SKU_AMOUNT") );
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 36);	     
			     headercell2.setCellValue(rs.getString("SD_DISCOUNT_RATE"));
			     SetNormalCellBackColor(workbook,headercell2,style62);;
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 37);	     
			     headercell2.setCellValue(rs.getString("SD_HAND_TO_HAND_AMOUNT"));
			     SetNormalCellBackColor(workbook,headercell2,style62);
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 38);	     
			     headercell2.setCellValue(rs.getString("SD_DISPATCH_CREATED_ON"));
			     SetNormalCellBackColor(workbook,headercell2,style62);
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 39);	     
			     headercell2.setCellValue(rs.getString("SD_IS_DISPATCH_ADJUSTED"));
			     SetNormalCellBackColor(workbook,headercell2,style62);
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 40);	     
			     headercell2.setCellValue(rs.getString("SD_DISPATCH_ADJUSTED_ON"));
			     SetNormalCellBackColor(workbook,headercell2,style62);
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 41);	     
			     headercell2.setCellValue(rs.getString("SD_SALES_TYPE"));
			     SetNormalCellBackColor(workbook,headercell2,style62);
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 42);	     
			     headercell2.setCellValue(rs.getString("SD_DOCUMENT_ID"));
			     SetNormalCellBackColor(workbook,headercell2,style62);
			     
			     headercell2 = (XSSFCell) headerrow2.createCell((short) 43);	     
			     headercell2.setCellValue(rs.getString("SPOT_SALE"));
			     SetNormalCellBackColor(workbook,headercell2,style62);
			     
			     
			     
			     RowCount=RowCount+1;
		     }
		     
		    
			     
		     
		    
		      
				
				
				
				
				
				
					
				/*//Generated On
				    
				    RowCount = RowCount+1;
					
					//Printing Date
					
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
					
					
					*/
			
			
			//Auto Sizing Column
		    
				  
				    
		    for(int i=0;i<43;i++){
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
	
	
	public void SetNormalCellBackColor(XSSFWorkbook workbook,XSSFCell headercell,XSSFCellStyle style61){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));
		
		
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
	
	
	public void SetNormalCellBackColorH(XSSFWorkbook workbook,XSSFCell headercell,XSSFCellStyle style61){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 255));
		
		
	      style61.setFillBackgroundColor(
	      HSSFColor.BLUE.index );
	      style61.setFillForegroundColor(BackColor2ndHeader);
	      style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      
	      
	      style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style61.setAlignment(CellStyle.ALIGN_LEFT);
			
			XSSFFont font = workbook.createFont();//Create font
		     font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		     style61.setFont(font);//set it to bold
			
			headercell.setCellStyle(style61);
			
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
	      headercell.setCellStyle(style61);
	}
	
	public void SetNormalCellBackColor12(XSSFWorkbook workbook,XSSFCell headercell){
		
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
			
			//style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
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
	
	public void SetNormalCellBackColorBlueOneDecimal(XSSFWorkbook workbook,XSSFCell headercell){
		
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
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.#"));
			
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
	public void SetNormalCellBackColorPinkOneDecimal(XSSFWorkbook workbook,XSSFCell headercell){
		
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
			style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.#"));
			
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

	
	public void SetNormalCellBackColorPercentRed(XSSFWorkbook workbook,XSSFCell headercell){
		
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
	
	public void SetNormalCellBackColorRed12(XSSFWorkbook workbook,XSSFCell headercell){
		
		 XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(192, 0, 0));
		
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
	
}
