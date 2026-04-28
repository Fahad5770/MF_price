package com.pbc.reports;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.Font;
import com.opencsv.CSVWriter;
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

public class R366Excel {

	public static final String RESULT = Utilities.getEmailAttachmentsPath() + "/CashInflowSummary.pdf";

	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;

	/// Date StartDate = Utilities.getDateByDays(0);
	/// //Utilities.parseDate("13/02/2016");
	/// Date EndDate =
	/// Utilities.getDateByDays(0);//Utilities.parseDate("13/02/2016");

	Date StartDate = Utilities.parseDate("01/03/2020");
	Date EndDate = Utilities.parseDate("20/03/2020");

	Date Yesterday = Utilities.getDateByDays(-1);

	long SND_ID = 0;

	public static void main(String[] args) throws Exception {

	}

	public R366Excel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
	
		if (Utilities.getDayOfWeekByDate(Yesterday) == 6) {
			Yesterday = Utilities.getDateByDays(-2);
		}

	}

	public void createPdf(String filename, long SND_ID, String StartDate, String EndDate, String DistributorID, String BrandID, String SKUID, String RegionID)
			throws IOException, SQLException {

		// System.out.println("Open CSV File for Secondary User -> Distributor : "+
		// DistributorID);

		// System.out.println(filename);

		// create FileWriter object with file as parameter
		FileWriter outputfile = new FileWriter(filename);

		// create CSVWriter object filewriter object as parameter
		CSVWriter writer = new CSVWriter(outputfile);
		
//		System.out.println("Start Date : "+StartDate);
//		System.out.println("End Date : "+EndDate);
//		System.out.println("Distributor ID : "+DistributorID);
//		System.out.println("Brand ID : "+BrandID);
//		System.out.println("SKU ID : "+SKUID);
//		System.out.println("Region ID : "+RegionID);

		String WhereDistributor = "";
		if (!DistributorID.equals("")) {
			WhereDistributor = " and (SD_DISTRIBUTOR_ID in ("+DistributorID+"))";
		}
		
		String WhereBrand = "";
		if (!BrandID.equals("")) {
			WhereBrand = " and (SD_LRB_ID in ("+BrandID+"))";
		}
		
		String WhereSKU = "";
		if (!SKUID.equals("")) {
			WhereSKU = " and (SD_PRODUCT_ID in ("+SKUID+"))";
		}
		
		String WhereRegion = "";
		if (!RegionID.equals("")) {
			WhereRegion = " and (SD_REGION_ID in ("+RegionID+"))";
		}

		// adding header to csv
		String[] header = { "Created Date", "Created Time", "Year", "Month", "Day", "Day Name", "Adjusted Date" ,"Distributor ID",
				"Distributor Name", "Order ID", "Sales Type ID", "Outlet ID", "Outlet Name", "Channel ID", "SND ID",
				"Order Booker ID", "Order Booker Name", "SND Name", "RSM ID", "RSM Name", "TDM ID", "TDM Name",
				"Channel Label", "Invoice Amount", "Region ID", "Region Name", "PJP ID", "PJP Name", "Dispatch ID",
				"Package Label", "Brand Label", "Cases", "Is Promotion", "Converted Cases", "Promotion Amount",
				"Promotion Cases", "Pre Tax Amount","SKU Amount", "Discount Rate", "Hand To Hand Amount", "Dispatch Created On",
				"Is Dispatch Adjusted", "Dispatch Adjusted On", "Sales Type", "DOCUMENT ID", "Spot Sales" };
		writer.writeNext(header);
		Datasource ds = new Datasource();
		try {
			 //ds.createConnectionToReplicaNBCFinalNewServer();
			
			ds.createConnection();

			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			List<Integer> exclusiveOutlets = new ArrayList<Integer>();
			
			
			System.out.println("Select * from get_sale_R366_view WHERE"
					+ " (`SD_DISTRIBUTOR_ID` <> 123) and (SD_DISPATCH_CREATED_ON between '"+StartDate+"' and '"+EndDate+"' )   "
					+ WhereDistributor+WhereBrand+WhereSKU+WhereRegion);
			ResultSet rs = s.executeQuery("Select * from get_sale_R366_view WHERE"
					+ " (`SD_DISTRIBUTOR_ID` <> 123) and (SD_DISPATCH_CREATED_ON between '"+StartDate+"' and '"+EndDate+"' )   "
					+ WhereDistributor+WhereBrand+WhereSKU+WhereRegion);
			while (rs.next()) {
				exclusiveOutlets.add(Utilities.parseInt(rs.getString("SD_OUTLET_ID")));

				String[] data1 = { rs.getString("SD_CREATED_ON_DATE"), rs.getString("SD_CREATED_ON_TIME"),
						rs.getString("SD_CREATED_ON_YEAR"), rs.getString("SD_CREATED_ON_MONTH"),
						rs.getString("SD_CREATED_ON_DAY"),

						rs.getString("SD_CREATED_ON_DAY_NAME"),
						
						rs.getString("SD_ADJUSTED_ON_DATE"),
						rs.getString("SD_DISTRIBUTOR_ID"),
						rs.getString("SD_DISTRIBUTOR_LABEL"), rs.getString("SD_ORDER_ID"),
						rs.getString("SD_SALES_TYPE_ID"),

						rs.getString("SD_OUTLET_ID"), rs.getString("SD_OUTLET_LABEL"), rs.getString("SD_CHANNEL_ID"),
						rs.getString("SD_SND_ID"), rs.getString("SD_ORDER_BOOKER_ID"),

						rs.getString("SD_ORDER_BOOKER_NAME"), rs.getString("SD_SND_NAME"), rs.getString("SD_RSM_ID"),
						rs.getString("SD_RSM_NAME"), rs.getString("SD_TDM_ID"),

						rs.getString("SD_TDM_NAME"), rs.getString("SD_CHANNEL_LABEL"),
						rs.getString("SD_INVOICE_AMOUNT"), rs.getString("SD_REGION_ID"), rs.getString("SD_REGION_LABEL")

						, rs.getString("PJP_ID"), rs.getString("PJP_LABEL"), rs.getString("SD_DISPATCH_ID"),
						rs.getString("SD_PACKAGE_LABEL"), rs.getString("SD_BRAND_LABEL"), rs.getString("SD_CASES"),
						rs.getString("SD_IS_PROMOTION"), rs.getString("SD_CONVERTED_CASES"),
						rs.getString("SD_PROMOTION_AMOUNT"), rs.getString("SD_PROMOTION_CASES"),
						rs.getString("SD_SKU_PRE_TAX_Amount"),
						rs.getString("SD_SKU_AMOUNT"), rs.getString("SD_DISCOUNT_RATE"),
						rs.getString("SD_HAND_TO_HAND_AMOUNT"), rs.getString("SD_DISPATCH_CREATED_ON"),
						rs.getString("SD_IS_DISPATCH_ADJUSTED"), rs.getString("SD_DISPATCH_ADJUSTED_ON"),
						// rs.getString("SD_SALES_TYPE"), rs.getString("SD_DOCUMENT_ID"),
						rs.getString("SD_SALES_TYPE"), rs.getString("ISA_SD_DOCUMENT_ID"),
						rs.getString("SPOT_SALES") };
				
				writer.writeNext(data1);

			}// end of while
			
			

			int ids[] = {
					103101572,103101634,103100646,103100582,103101853,103102164,103103002,103102060,103101022,103103026,103100197,103101014,103101193,103101984,103100723,
		            103100309,103102319,103103117,103100918,103103033,103101692,103102679, 103102400,111100095,111101077,111101086,111100392,111100105,111100853,111101395,111101359,
		            111101002,111101003,111100952,111100715,107100204,107101452,107100942,107101111,107100842, 107101241,107101256,107100939,107100886,107100063,107100061, 140102959,140102960,140100279,
		            140100137,140102660,140102545,140105476,140102991,140102702,140102970,140104893,140100499,140104846,140100553,140100555,140104573,140100592,140100501,140104361,140102646,140101582,
		            127111358,127112803,127113667,127111345,127112660,127111280,127113732,127111268,127113779, 127111378, 127111377,127113275,127112418,127111368,127111374,127111369,127112637,		            127112636,
		            127112700,127111504,127112670,127112699,127112648,127111545,127112404,127111720,140100431,140102037,127111538,127112413,127109858,127112402,127111543,137105482,137102642,137105758,137100015,137104852,137101369,137100331,137102016,137105243,
		            137105593,137106305,137105239,137104941,137103320,137104759,138101359,138100992,138101145,138101206,138101276,138101286,138101071,138101163,122104906,122104489,122104528,122104746,122104884,122103485,
		            122104433,122104806,122105074,122104491,122104547,122103656,137104869,137105705,137102284,137102044,142100614,142100780,137104278,137106996,137104417,137104508,137104297,137104381,137103160,137102888,
		            137104688,128109004,128108693,128108864,128108114,128107463,128108055,128108054,128107505,128108052,128112111,128112304,128112071,128112099,128112119,128107212,
		            128107145,128109451,128100734,128107137,128111226,128110224,128110608,128107119,128107118,128100365,128102363,128101943,
		            128111881,128111968,128111689,128111828,128106867,128107658,128110161,128110079,128110878,128106967,128107983,128107014,128105543,128105545,128109037,128107999,
		            128110284,128110903,128111011,128111516,128108124,128101740,128105471,105100194,105100058,105100591,105100729,105100560,105100500,105100806,105100802,105100006,
		            105100809,105101112,121100357,121100508,121100465,121100180,121100481,101101246,101101303,100106710,101101214,100100176,101101274,100100343,101101222,100104238,100105753,
		            100106686,101101397,100105208,100106684,102101203,102101209,102101498,102101143,102101140,102101141,102101805,102101890,102101799, 102101799,118118777,118117940,118122783,118117922,118101930,
		            118123447,118123686,118123443,118119141,118120142,118121909,118121570,118124515,118105850,118124795,118105055,118124793,118105141,118116209,118116210,118124067,118120589,118120049,105100494,121100599
			};

			int count = 0;
			for(int i=0; i<ids.length; i++) {
				if(!exclusiveOutlets.contains(ids[i])) {
					String name="", channel="";
					int channel_id=0;
					ResultSet rsOutletName = s.executeQuery("Select name, pic_channel_id,(select label from pci_sub_channel where id=pic_channel_id) channel from common_outlets where id="+ids[i]);
					if(rsOutletName.first()) {
						name=rsOutletName.getString("name");
						channel=rsOutletName.getString("channel");
						channel_id=rsOutletName.getInt("pic_channel_id");
					}
	System.out.println("ID Not Exists : "+ids[i]);
	String[] data1 = { "","","","","","","","","","","",Integer.toString(ids[i]),name,Integer.toString(channel_id),"","",
			"","", "","","","",channel,"0","","","","","","","","0","","0","0","0","0","0","0","0","","","",
			"","","" };
	
	writer.writeNext(data1);
				}
				
				
				count++;
			
			}
			System.out.println("ID counts : "+count);
			
		} catch (Exception e) {

			e.printStackTrace();

		}
		writer.close();

		ds.dropConnection();
	}

}
