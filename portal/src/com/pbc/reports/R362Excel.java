package com.pbc.reports;

import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class R362Excel {

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

	public R362Excel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

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

	public void createPdf(String filename, Date StartDate, Date EndDate, String Distributors, String OrderBookers, String TSOs, String ASMs, String Regions, String Channels, String Brands, String SKUs)
			throws IOException, SQLException, ParseException {
		
//		System.out.println("StartDate : "+StartDate);
//		System.out.println("EndDate : "+EndDate);
//		System.out.println("Distributors : "+Distributors);
//		System.out.println("OrderBookers : "+OrderBookers);
//		System.out.println("TSOs : "+TSOs);
//		System.out.println("ASMs : "+ASMs);
//		System.out.println("Regions : "+Regions);
//		System.out.println("Channels : "+Channels);
//		System.out.println("Brands : "+Brands);
//		System.out.println("SKUs : "+SKUs);



		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("cell types");

		int FirstRowCount = 0;

		int RowCount = 0;

		// Report Heading

		XSSFRow rowH = spreadsheet.createRow((short) RowCount);
		XSSFCell cellH = (XSSFCell) rowH.createCell((short) 0);

		


		cellH.setCellValue("R362 - Order MobileTimeStamp");

		spreadsheet.addMergedRegion(new CellRangeAddress(FirstRowCount, // first row (0-based)
				FirstRowCount, // last row (0-based)
				0, // first column (0-based)
				3 // last column (0-based)
		));

		CellStyle style = workbook.createCellStyle();// Create style
		XSSFFont font = workbook.createFont();// Create font
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);// set it to bold
		cellH.setCellStyle(style);

		RowCount++;

		// Printing Date

		XSSFRow rowP = spreadsheet.createRow((short) RowCount);

		XSSFCell cellP = (XSSFCell) rowP.createCell((short) 0);

		// Date EndDate = Utilities.getEndDateByDate(StartDate);

		cellP.setCellValue("Period : " + Utilities.getDisplayDateFormat(StartDate) + " - "
				+ Utilities.getDisplayDateFormat(EndDate));
		spreadsheet.addMergedRegion(new CellRangeAddress(RowCount, // first row (0-based)
				RowCount, // last row (0-based)
				0, // first column (0-based)
				3 // last column (0-based)
		));

		/***************************** Data ***************************************/


		String[] columnNames = {"Distributor Name","Region", "Mobile Order No.", "PSR Name", "	Mobile Timestamp Date","Mobile Timestamp Time","Crated on Date","Created on Time","Outlet Name","Address",
				"Channel","Invoice Amount","Net Invoice Amount","Lat","Lng","	Accuracy","Reason"		
		};
		
								
		RowCount++;
		
		/** Filters For MO **/
		String whereDistributors = (Distributors.length() > 0) ? " and mo.distributor_id in ("+Distributors+")" : "" ;
		String whereOrderBookers = (OrderBookers.length() > 0) ? " and mo.created_by in ("+OrderBookers+")" : "" ;
		String whereRegions = (Regions.length() > 0) ? " and mo.region_id in ("+Regions+")" : "" ;
		String whereChannels = (Channels.length() > 0) ? " and co.pic_channel_id in ("+Channels+")" : "" ;
		String whereSKUs = (SKUs.length() > 0) ? " and product_id in ("+SKUs+")" : "" ;
		String whereBrands = (Brands.length() > 0) ? " and ip.lrb_type_id in ("+Brands+")" : "" ;
		String whereASMsDistributor = "";
		String ids = "";
		if(ASMs.length() > 0) {
			int c=0;
			
			//System.out.println("Select distributor_id from common_distributors where rsm_id in ("+ASMs+")");
			ResultSet rsAsms = s.executeQuery("Select distributor_id from common_distributors where rsm_id in ("+ASMs+")");
			while(rsAsms.next()) {
				ids = (c == 0) ? ids + rsAsms.getString("distributor_id") :  ids + ","+rsAsms.getString("distributor_id");
				c++;
			}
			
			whereASMsDistributor = " and mo.distributor_id in ("+ids+")"; 
		}
		String whereTSOs = (TSOs.length() > 0) ? " and mo.asm_id in ("+TSOs+")" : "" ;
		
		String mobileOrderIds = "";
		int counter=0;
		
		System.out.println("select distinct mop.id from mobile_order_products mop join inventory_products ip on ip.id=product_id where mop.id in (select mo.id FROM mobile_order mo join common_outlets co on mo.outlet_id=co.id WHERE mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+whereDistributors+whereASMsDistributor+whereTSOs+whereOrderBookers+whereRegions+whereChannels+") "+whereSKUs+whereBrands);
		ResultSet rsMobileOrderIds = s.executeQuery("select distinct mop.id from mobile_order_products mop join inventory_products ip on ip.id=product_id where mop.id in (select mo.id FROM mobile_order mo join common_outlets co on mo.outlet_id=co.id WHERE mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+whereDistributors+whereASMsDistributor+whereTSOs+whereOrderBookers+whereRegions+whereChannels+") "+whereSKUs+whereBrands);
		while(rsMobileOrderIds.next()) {
			mobileOrderIds += (counter == 0) ? rsMobileOrderIds.getString("id") : " ,"+rsMobileOrderIds.getString("id");
			counter++;
		}
		
		mobileOrderIds = (mobileOrderIds.length() > 0 ? mobileOrderIds : "0");
	//	System.out.println("mobileOrderIds "+mobileOrderIds);
		
		/** Filters For NoOrder **/
	
		String whereDistributorsNR = (Distributors.length() > 0) ? " and distributor_id in ("+Distributors+")" : "" ;
		// String faha = (SKUs.length() > 0) ? " and product_id in ("+SKUs+")" : "" ;
		String whereASMsDistributorNR = "";
		if(ASMs.length() > 0) {
			whereASMsDistributorNR = " and distributor_id in ("+ids+")"; 
		}
		String whereTSOsNR = "";
		if(TSOs.length() > 0) {
		//	System.out.println(" and co.id in (select distinct outlet_id from distributor_beat_plan_schedule where id in (select id from distributor_beat_plan where asm_id in ("+TSOs+")))");
			whereTSOsNR = (" and co.id in (select distinct outlet_id from distributor_beat_plan_schedule where id in (select id from distributor_beat_plan where asm_id in ("+TSOs+")))");
//			ResultSet rsTSODistributor = s.executeQuery("select group_concat(distinct outlet_id) as outlets from distributor_beat_plan_schedule where id in (select id from distributor_beat_plan where asm_id in ("+TSOs+"))");
//			if(rsTSODistributor.first()) {
//				whereTSOsNR = " and co.id in ("+rsTSODistributor.getString("outlets")+")"; 
//			}
		}
		String whereRegionsNO = (Regions.length() > 0) ? " and co.region_id in ("+Regions+")" : "" ;
		
		String distributorOutlets = "";
	//	System.out.println(" and co.id in (select distinct outlet_id from distributor_beat_plan_schedule where id in (select id from distributor_beat_plan where 1=1 "+whereDistributorsNR+whereASMsDistributorNR+"))");
		distributorOutlets = (" and co.id in (select distinct outlet_id from distributor_beat_plan_schedule where id in (select id from distributor_beat_plan where 1=1 "+whereDistributorsNR+whereASMsDistributorNR+"))");
		
//		ResultSet rsOutletsNR = s.executeQuery("select group_concat(distinct outlet_id) outlets from distributor_beat_plan_schedule where id in (select id from distributor_beat_plan where 1=1 "+whereDistributorsNR+whereASMsDistributorNR+")");
//		if(rsOutletsNR.first()) {
//			distributorOutlets = " and co.id in ("+rsOutletsNR.getString("outlets")+")";
//		}
		
		counter=0;
		String NoORdersIds = "";
		
	//	System.out.println("select distinct mop.id from mobile_retailer_stock mop join inventory_products ip on ip.id=product_id where mop.id in (select mo.id FROM mobile_order_zero mo join common_outlets co on mo.outlet_id=co.id WHERE mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+whereOrderBookers+distributorOutlets+whereTSOsNR+whereRegionsNO+whereChannels+" )"+whereSKUs+whereBrands);
		ResultSet rsMarketVisitIds = s.executeQuery("select distinct mop.id from mobile_retailer_stock mop join inventory_products ip on ip.id=product_id where mop.id in (select mo.id FROM mobile_order_zero mo join common_outlets co on mo.outlet_id=co.id WHERE mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+whereOrderBookers+distributorOutlets+whereTSOsNR+whereRegionsNO+whereChannels+" )"+whereSKUs+whereBrands);
		while(rsMarketVisitIds.next()) {
			NoORdersIds += (counter == 0) ? rsMarketVisitIds.getString("id") : " ,"+rsMarketVisitIds.getString("id");
			counter++;
		}
		
		NoORdersIds = (NoORdersIds.length() > 0 ? NoORdersIds : "0");
	
		
		//System.out.println("NoORdersIds "+NoORdersIds);
		
		/***************************** Data ***************************************/
		
		/*******************************
		 * ROW 3 Start
		 *******************************/
		

		CellStyle style2 = workbook.createCellStyle();// Create style !DistributorIDs.equals("")
		style2.setFont(font);// set it to bold
		style2.setAlignment(CellStyle.ALIGN_CENTER);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		XSSFRow row3 = spreadsheet.createRow((short) RowCount);
		XSSFCell row3Cell = (XSSFCell) row3.createCell((short) 0);

		
		
		int col = 0;
		for (String title : columnNames) {
			row3Cell = (XSSFCell) row3.createCell((short) col);
			row3Cell.setCellValue(title);
		//	Set2ndHeaderBackColorOrderKPI(workbook, row5Cell);
			row3Cell.setCellStyle(style2);
			col++;
		}

		/*******************************
		 * ROW 3 End
		 *******************************/
		
		


		/*******************************
		 * Row 5 Starting from here
		 ********************************/

		CellStyle simpleTextStyle = workbook.createCellStyle();
		simpleTextStyle.setAlignment(CellStyle.ALIGN_LEFT);
		simpleTextStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		simpleTextStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		simpleTextStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		simpleTextStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);


	 
		int colRow5 = 0;	
			   // System.out.println("SELECT * FROM (SELECT  (select  pic_channel_id  from common_outlets where id=outlet_id) as channel_id,(select address from common_outlets where id=outlet_id) as address,(select name from common_outlets where id=outlet_id) as outlet,no_order_reason_type_v2, mobile_order_no, mobile_timestamp, created_on, outlet_id, created_by, lat, lng, accuracy, 0 AS invoice_amount, 0 AS net_amount, distributor_id, region_id, (select region_name from common_regions cr where cr.region_id=moz.region_id) as region, (select name from common_distributors cd where cd.distributor_id=moz.distributor_id ) as distributor_name, (select Display_Name from users u where u.id=moz.created_by ) as psr FROM mobile_order_zero moz WHERE  UNION SELECT  (select  pic_channel_id  from common_outlets where id=outlet_id) as channel_id,(select address from common_outlets where id=outlet_id) as address, (select name from common_outlets where id=outlet_id) as outlet,'' AS no_order_reason_type_v2, mobile_order_no, mobile_timestamp, created_on, outlet_id, created_by, lat, lng, accuracy, invoice_amount, net_amount, distributor_id, region_id, (select region_name from common_regions cr where cr.region_id=mo.region_id) as region, (select name from common_distributors cd where cd.distributor_id=mo.distributor_id ) as distributor_name, (select Display_Name from users u where u.id=mo.created_by ) as psr FROM mobile_order mo  WHERE distributor_id in (" + Distributors + ")  AND  created_on between  "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+") AS combined_result ORDER BY mobile_timestamp");
	//	System.out.println("SELECT * FROM (SELECT  (select  pic_channel_id  from common_outlets where id=outlet_id) as channel_id,(select address from common_outlets where id=outlet_id) as address,(select name from common_outlets where id=outlet_id) as outlet,no_order_reason_type_v2, mobile_order_no, mobile_timestamp, created_on, outlet_id, created_by, lat, lng, accuracy, 0 AS invoice_amount, 0 AS net_amount, distributor_id, 0 as region_id, '' as region, '' as distributor_name, (select Display_Name from users u where u.id=moz.created_by ) as psr FROM mobile_order_zero moz WHERE moz.id in ("+NoORdersIds+") UNION SELECT  (select  pic_channel_id  from common_outlets where id=outlet_id) as channel_id,(select address from common_outlets where id=outlet_id) as address, (select name from common_outlets where id=outlet_id) as outlet,'' AS no_order_reason_type_v2, mobile_order_no, mobile_timestamp, created_on, outlet_id, created_by, lat, lng, accuracy, invoice_amount, net_amount, distributor_id, region_id, (select region_name from common_regions cr where cr.region_id=mo.region_id) as region, (select name from common_distributors cd where cd.distributor_id=mo.distributor_id ) as distributor_name, (select Display_Name from users u where u.id=mo.created_by ) as psr FROM mobile_order mo  WHERE mo.id in ("+mobileOrderIds+") ) AS combined_result ORDER BY mobile_timestamp");
		ResultSet rs2 = s2.executeQuery("SELECT * FROM (SELECT  (SELECT label FROM pep.mobile_order_no_order_reason_type rt where rt.id=no_order_reason_type_v2) reason,(select  pic_channel_id  from common_outlets where id=outlet_id) as channel_id,(select address from common_outlets where id=outlet_id) as address,(select name from common_outlets where id=outlet_id) as outlet,no_order_reason_type_v2, mobile_order_no, mobile_timestamp, created_on, outlet_id, created_by, lat, lng, accuracy, 0 AS invoice_amount, 0 AS net_amount, distributor_id, 0 as region_id, '' as region, '' as distributor_name, (select Display_Name from users u where u.id=moz.created_by ) as psr FROM mobile_order_zero moz WHERE moz.id in ("+NoORdersIds+") UNION SELECT  '' AS reason,(select  pic_channel_id  from common_outlets where id=outlet_id) as channel_id,(select address from common_outlets where id=outlet_id) as address, (select name from common_outlets where id=outlet_id) as outlet,'' AS no_order_reason_type_v2, mobile_order_no, mobile_timestamp, created_on, outlet_id, created_by, lat, lng, accuracy, invoice_amount, net_amount, distributor_id, region_id, (select region_name from common_regions cr where cr.region_id=mo.region_id) as region, (select name from common_distributors cd where cd.distributor_id=mo.distributor_id ) as distributor_name, (select Display_Name from users u where u.id=mo.created_by ) as psr FROM mobile_order mo  WHERE mo.id in ("+mobileOrderIds+") ) AS combined_result ORDER BY mobile_timestamp");

				while(rs2.next()) {
					
					String total = "";
					String Outlet_name = "";
					String Region = "";
					String Distributor_Name = "";
					int ASM = 0;
					int RSM = 0;
				    String Address = "", Channel="";
				    String Reason = rs2.getString("reason");
				 	String psr="";
					//System.out.println("====================");
					
				    double lat = rs2.getDouble("lat");
				    double lng = rs2.getDouble("lng");
				    int accuracy = rs2.getInt("accuracy");
				    
				    String outletid = rs2.getString("outlet_id");
				    Outlet_name = outletid+"-"+ rs2.getString("outlet");
				    
				    String Mobile_Order_No = rs2.getString("mobile_order_no"); 
				    
				    String Mobile_Time_Stamp = rs2.getString("mobile_timestamp"); 
				    
				    String Created_by = rs2.getString("created_by");
				    psr = rs2.getString("created_by") +"-"+rs2.getString("psr");;
				    
				    
				    String invoice_amount = rs2.getString("invoice_amount");
				    String net_amount = rs2.getString("net_amount");
				    
				    long distributor_id = rs2.getLong("distributor_id");
				    Distributor_Name = (distributor_id == 0) ? "" : distributor_id+"-"+rs2.getString("distributor_name");
				    
				    Region = rs2.getString("region");
//				    int region_id = rs2.getInt("region_id");
//				    if(region_id == 0) {
//				    	//System.out.println("select (select region_name from common_regions cr where cr.region_id=co.region_id) as region from common_outlets co where id="+outletid);	
//				    	ResultSet rsRegion = s3.executeQuery("select (select region_name from common_regions cr where cr.region_id=co.region_id) as region from common_outlets co where id="+outletid);	
//				    	Region = (rsRegion.first() ? rsRegion.getString("region") : "");
//				    }
				    
				    Address = rs2.getString("address");
				    
				    int channel_id = rs2.getInt("channel_id");
				    ResultSet rsChannel = s3.executeQuery("select label from pci_sub_channel psc where id="+channel_id);
				    if(rsChannel.first()) {
				    	Channel = rsChannel.getString("label");
				    }
				    
				    
				    
				    String Created_on = rs2.getString("created_on");


				    
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			 	  Date date = dateFormat.parse(Created_on);

		            // Format the date and time into separate variables
		            SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
		            SimpleDateFormat timeOnlyFormat = new SimpleDateFormat("HH:mm:ss");

		            String formattedDate = dateOnlyFormat.format(date);
		            String formattedTime = timeOnlyFormat.format(date);
		            
		            
		        	SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				 	  Date date2 = dateFormat2.parse(Mobile_Time_Stamp);

			            // Format the date and time into separate variables
			            SimpleDateFormat dateOnlyFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			            SimpleDateFormat timeOnlyFormat2 = new SimpleDateFormat("HH:mm:ss");

			            String formattedDate2 = dateOnlyFormat.format(date);
			            String formattedTime2 = timeOnlyFormat.format(date);
			            
				

				 // Data
				
        RowCount++;	
			            
			            
        XSSFRow row5 = spreadsheet.createRow((short) RowCount);
		XSSFCell row5Cell = (XSSFCell) row5.createCell((short) 0);	
		colRow5 =0;
						
	//Distributor Name
		row5Cell = (XSSFCell) row5.createCell((short) colRow5);
 		row5Cell.setCellValue(Distributor_Name);
		row5Cell.setCellStyle(simpleTextStyle);
		colRow5++;
	//Region
		row5Cell = (XSSFCell) row5.createCell((short) colRow5);
		row5Cell.setCellValue(Region);
		row5Cell.setCellStyle(simpleTextStyle);
		colRow5++;

	//Mobile Order No.
		row5Cell = (XSSFCell) row5.createCell((short) colRow5);
		row5Cell.setCellValue(Mobile_Order_No);
		row5Cell.setCellStyle(simpleTextStyle);
		colRow5++;

	//PSR Name
		row5Cell = (XSSFCell) row5.createCell((short) colRow5);
		row5Cell.setCellValue(psr);
		row5Cell.setCellStyle(simpleTextStyle);
		colRow5++;

	//Mobile Timestamp Date
		row5Cell = (XSSFCell) row5.createCell((short) colRow5);
		row5Cell.setCellValue(formattedDate2 );
		row5Cell.setCellStyle(simpleTextStyle);
		colRow5++;

	//Mobile Timestamp Time
		row5Cell = (XSSFCell) row5.createCell((short) colRow5);
		row5Cell.setCellValue(formattedTime2);
		row5Cell.setCellStyle(simpleTextStyle);
		colRow5++;

 
	//Crated on Date
		row5Cell = (XSSFCell) row5.createCell((short) colRow5);
		row5Cell.setCellValue(formattedDate);
		row5Cell.setCellStyle(simpleTextStyle);
		colRow5++;

	//Created on Time
		row5Cell = (XSSFCell) row5.createCell((short) colRow5);
		row5Cell.setCellValue(formattedTime);
		row5Cell.setCellStyle(simpleTextStyle);
		colRow5++;

		
	//Outlet Name
			row5Cell = (XSSFCell) row5.createCell((short) colRow5);
			row5Cell.setCellValue(Outlet_name);
			row5Cell.setCellStyle(simpleTextStyle);
			colRow5++;
				
				
	//Address
			row5Cell = (XSSFCell) row5.createCell((short) colRow5);
			row5Cell.setCellValue(Address);
			row5Cell.setCellStyle(simpleTextStyle);
			colRow5++;
			
	//Channel
			row5Cell = (XSSFCell) row5.createCell((short) colRow5);
			row5Cell.setCellValue(Channel);
			row5Cell.setCellStyle(simpleTextStyle);
			colRow5++;

	//Invoice Amount
			row5Cell = (XSSFCell) row5.createCell((short) colRow5);
			row5Cell.setCellValue(invoice_amount);
			row5Cell.setCellStyle(simpleTextStyle);
			colRow5++;
			
	//Net Invoice Amount
			row5Cell = (XSSFCell) row5.createCell((short) colRow5);
			row5Cell.setCellValue(net_amount);
			row5Cell.setCellStyle(simpleTextStyle);
			colRow5++;
		
	//Latitude
			row5Cell = (XSSFCell) row5.createCell((short) colRow5);
			row5Cell.setCellValue(lat);
			row5Cell.setCellStyle(simpleTextStyle);
			colRow5++;
		
	//Longitude
			row5Cell = (XSSFCell) row5.createCell((short) colRow5);
 		    row5Cell.setCellValue(lng);
			row5Cell.setCellStyle(simpleTextStyle);
			colRow5++;
		
	//Accuracy
			row5Cell = (XSSFCell) row5.createCell((short) colRow5);
 			row5Cell.setCellValue(accuracy);
			row5Cell.setCellStyle(simpleTextStyle);
			colRow5++;
		
	//Reason 
			row5Cell = (XSSFCell) row5.createCell((short) colRow5);
			row5Cell.setCellValue(Reason);
			row5Cell.setCellStyle(simpleTextStyle);
			colRow5++;
				}
			
		/*******************************
		 * Row 5 will end here
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

	public void Set2ndHeaderBackColorssSecondarySales_1(final XSSFWorkbook workbook, final XSSFCell headercell,
			XSSFCellStyle style61) {
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

	public void Set2ndHeaderBackColorOrange(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 140, 0));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style61.setAlignment(CellStyle.ALIGN_LEFT);
		headercell.setCellStyle(style61);

	}

	public void Set2ndHeaderBackColorBrownKPI(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(78, 53, 36));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style61.setAlignment(CellStyle.ALIGN_CENTER);
		headercell.setCellStyle(style61);

		XSSFFont font = workbook.createFont();
		font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		// font.setFontHeightInPoints((short)10);
		font.setColor(IndexedColors.WHITE.getIndex());
		style61.setFont(font);
	}

	public void Set2ndHeaderBackColorGray(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(162, 162, 163));

		XSSFCellStyle style61 = workbook.createCellStyle();
		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style61.setAlignment(CellStyle.ALIGN_LEFT);
		headercell.setCellStyle(style61);

	}

	public void SetNormalCellBackColorH_yellow(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(255, 255, 0));
		XSSFCellStyle style61 = workbook.createCellStyle();

		style61.setFillBackgroundColor(HSSFColor.BLUE.index);
		style61.setFillForegroundColor(BackColor2ndHeader);
		style61.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style61.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style61.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style61.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style61.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style61.setAlignment(CellStyle.ALIGN_RIGHT);

		XSSFFont font = workbook.createFont();// Create font
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style61.setFont(font);// set it to bold

		headercell.setCellStyle(style61);

		//style61.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

		headercell.setCellStyle(style61);
	}

	public void SetNormalCellBackColorH_blue(XSSFWorkbook workbook, XSSFCell headercell, XSSFCellStyle style61) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(0, 0, 255));

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

	public void SetNormalCellBackColorH_green(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(0, 128, 0));

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

	public void SetNormalCellBackColorH_blue(XSSFWorkbook workbook, XSSFCell headercell) {

		XSSFColor BackColor2ndHeader = new XSSFColor(new java.awt.Color(0, 0, 255));

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

	public void Set2ndHeaderBackColorWhite(XSSFWorkbook workbook, XSSFCell headercell) {

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
