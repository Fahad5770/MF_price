package com.pbc.mobile;

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



public class MobileCensusExcel {

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
	
	public MobileCensusExcel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		
		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6){
			Yesterday = Utilities.getDateByDays(-2);
		}
		
	}
	
	
	public void createPdf(String filename, long SND_ID, long CensusID) throws  IOException, SQLException {
		
		
		
		
		XSSFWorkbook workbook = new XSSFWorkbook(); 
	    XSSFSheet spreadsheet = workbook.createSheet("cell types");
	      
	      
	      
	      
	      
			
			int FirstRowCount=0;
			
			int RowCount=0;
			
			
			
			
			
			//Report Heading
			
			 XSSFRow rowH = spreadsheet.createRow((short) RowCount);	      
		     XSSFCell cellH = (XSSFCell) rowH.createCell((short) 0);
		     
		     cellH.setCellValue("R238 - Census Summary");
			
		     spreadsheet.addMergedRegion(new CellRangeAddress(
		    		  FirstRowCount, //first row (0-based)
		    		  FirstRowCount, //last row (0-based)
		    		  0, //first column (0-based)
		    	      3 //last column (0-based)
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
		    	      1 //last column (0-based)
		    	      ));
		     
		     cellP = (XSSFCell) rowP.createCell((short) 2);		     
		     cellP.setCellValue("");
		     
		     cellP = (XSSFCell) rowP.createCell((short) 3);		     
		     cellP.setCellValue("");
		     
		     
		     
		     
			
		     
		     
		    
		    
		     XSSFCellStyle style611 = workbook.createCellStyle();		      
		     style611.setAlignment(CellStyle.ALIGN_RIGHT);
		     cellP.setCellStyle(style611);
		      
		     
		     
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
				 
				 cellEx1 = (XSSFCell) rowEx1.createCell((short) 2);	
				 cellEx1.setCellValue("");
				 
				 cellEx1 = (XSSFCell) rowEx1.createCell((short) 3);	
				 cellEx1.setCellValue("");
			    
			     
				 
		     
			
		   /////// //////////////////////
		     
		    
		      
		     
		          
		     
		     
		   
		    
		     
		      //3rd Row Header //////////////////////
			     ///////////////////////////////////////
			  /*  
			     RowCount = RowCount+1;
			     
			     XSSFRow headerrow1 = spreadsheet.createRow((short) RowCount);        
			     XSSFCell headercell1 = (XSSFCell) headerrow1.createCell((short) 0);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 0);	     
			     headercell1.setCellValue("Distributor");
			     Set3rdHeaderBackColor(workbook,headercell1);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 1);	     
			     headercell1.setCellValue("TOTAL OUTLETS");
			     Set3rdHeaderBackColor(workbook,headercell1);
			     
			     headercell1 = (XSSFCell) headerrow1.createCell((short) 2);	     
			     headercell1.setCellValue("AVG. DAILY SCHED. CALLS");
			     Set3rdHeaderBackColor(workbook,headercell1);
			     
			    
			    */ 
			    
			     
			     
			     /* Actual Row Data *//////
			     //////////////////////////////////////////////////
			     
		      long CensusIDMain=0;
				long TableMainID=0;
				
				String OutletID="";
				String OutletName="";
				String OutletNameOB="";
				String Region="";
				String Distributor="";
				String Area="";
				String ShopType="";
				String District="";
				String Village="";
				String LandMark="";
				String Location="";
				String ShopLocation="";
				String OwnerName="";
				String OwnerContactNo1="";
				String OwnerContactNo2="";
				String OwnerCNIC="";
				String ContactPersonName="";
				String ContactPerson="";
				String ContactPersonNo="";
				String ContactPersonCNIC="";
				String FBID="";
				String EmailID="";
				
				
				 //Shopkeeper
				 
				 String IsShopClosed="";
				 String ShopClosedStatus="";
				 String DayOff="";
				 String PartialTiming="";
				 String SDistributor="";
				 String WholeSaller="";
				 String SubDistributor="";
				 String SuppliedBy="";
				 
				 String FinancialService="";
				 
				 String EasyPaisa="";
				 String Upaisa="";
				 String MobiCash="";
				 String Warid="";
				 String TimePay="";
				 String Other="";
				 
				 
				 String ShopOpeningTime="";
				 String ShopCloseTime="";
				 
				 
				 //Status
				 
				 String ShopStatus="";
				 String ShopHistory="";
				 String BusinessStructure="";
				 String SocioEco="";
				 String SShopType="";
				 String Wholesale="";
				 String Retailer="";
				 String ServiceType="";
				 String CSDTurnOver="";
				 String TradeChannel="";
				 String TradeSubChannel="";
				 String SupplyFrequency="";
				 String SupplyFrequencyKO="";
				 String SupplyFrequencyGormet="";
				 String SupplyFrequencyColaNext="";
				 
				 
				 
				 //Beverages
				 
				 int PICSD=0;
				 int PISting=0;
				 int KO=0;
				
				 String Exclusivity="";
				 String ExPepsi="";
				 String ExCoke="";
				 String ExGourmet="";
				 String ExMezan="";
				 String ExOther="";
				 String DiscountStatus="";
				 String DPerCase="";
				 String DTradePaymentFix="";
				 String DTradePaymentTarget="";

				 
				 String IsCoveredByRCompnay="";
				 String IsCoveredByRCompnayDuration="";
				 
				 String StockStorageLocation1="";
				 String StockStorageLocation2="";
				 String StockStorageLocation3="";
				 String StockStorageLocation4="";
				 
				 String StockStorageLocation="";
				 
				 int CashMachineQuantity=0;
				 
				 int BeveragesSellingFullCasesSSRB=0;
				 int BeveragesSellingFullCasesPET=0;
				 int BeveragesSellingFullCasesTETRA=0;
				 int BeveragesSellingFullCasesCAN=0;
				 
				 String ExclusivityAgreementPI="";
				 String ExclusivityAgreementKO="";
				 String ExclusivityAgreementGou="";
				 String ExclusivityAgreementMezan="";
				 String ExclusivityAgreementOther="";
				 
				 
				 String VisibleColors="";
				 String AccessibleColors="";
				 
				 
				 //Shopkeeper
				 
				 String PerRawCaseDiscSSRB="";
				 String PerRawCaseDiscPET="";
				 
				 String AgreementExpDate="";
				 String AgreementType="";
				 String AgreementPeriod="";
				 
				 String AgreementType2="";
				 
				 
				 String PerRawCaseDiscSSRBPI="";
				 String PerRawCaseDiscPETPI="";
				 
				 String AgreementExpDatePI="";
				 String AgreementTypePI="";
				 String AgreementPeriodPI="";
				 
				 String AgreementType2PI="";
				 
				 String PerRawCaseDiscSSRBKO="";
				 String PerRawCaseDiscPETKO="";
				 
				 String AgreementExpDateKO="";
				 String AgreementTypeKO="";
				 String AgreementPeriodKO="";
				 
				 String AgreementType2KO="";
				 
				 
				 
				 String SuppliedByDistributorKO="";
				 String SuppliedByWholeSellerKO="";
				 String SuppliedByMobilerKO="";
				 String SuppliedByDealerKO="";
				 
				 String SuppliedByDistributorPI="";
				 String SuppliedByWholeSellerPI="";
				 String SuppliedByMobilerPI="";
				 String SuppliedByDealerPI="";
				 
				 
				 //General Tab Modified
				 
				 String OutletNameActual="";
				 String PJP="";
				 String OutletType="";
				 String OutletAddress="";
				 String MDistrict="";
				 String MTehsil="";
				 String MVillage="";
				 String MLandMark="";
				 String ShopClosedStatusN="";
				 String OutletStructure="";
				 String AreaSqFt="";
				 
				 String CreatedBy="";
				 
				 double Lati=0;
				 double Longi=0;
				 double Accuracy=0;
				 
				//System.out.println("SELECT *,coc.label as trade_channel_label,(select label from common_outlets_channels_sub_channels cocsc where cocsc.parent_channel_id=coc.id and cocsc.id=mc.census_trader_channel_sub_channel) trade_sub_channel_labe FROM mrd_census mc join common_outlets_channels coc on mc.census_trader_channel=coc.id where mc.id="+CensusID);
				
				ResultSet rs = s.executeQuery("SELECT *,(select display_name from users u where u.id=mc.created_by) created_display_name FROM mrd_census mc where mc.id="+CensusID);
				while(rs.next()){
					 
					CensusIDMain = rs.getLong("census_id");
					TableMainID= rs.getLong("id");
					
					
					  VisibleColors= rs.getString("cooler_visible");
					  AccessibleColors= rs.getString("cooler_access");
					
					CreatedBy = rs.getLong("created_by")+" - "+rs.getString("created_display_name");
					  
					
					OutletID= rs.getString("outlet_id");
					 OutletName=rs.getString("outlet_name");;
					 OutletNameOB=rs.getString("outlet_board_name");
					 OutletNameActual = rs.getString("outlet_name_actual");
					 OutletAddress=rs.getString("census_outlet_address");
					 
					 
					 
					  Lati=rs.getDouble("lat");
					  Longi=rs.getDouble("lng");
					  Accuracy=rs.getDouble("accuracy");
					 
					 //Outlet District
					 if(rs.getInt("census_distributor_district")==1){
						 MDistrict="FAISALABAD"; 
					 }else if(rs.getInt("census_distributor_district")==2){
						 MDistrict="TOBA TEKSINGH"; 
					 }else if(rs.getInt("census_distributor_district")==3){
						 MDistrict="JHANG"; 
					 }else if(rs.getInt("census_distributor_district")==4){
						 MDistrict="CHINIOT"; 
					 }
					 
					 //Outlet Tehsil
					 
					 if(rs.getInt("census_distributor_tehsil")==1){
						 MTehsil="CHINIOT";
					 }else if(rs.getInt("census_distributor_tehsil")==2){
						 MTehsil="BHOWANA";
					 }else if(rs.getInt("census_distributor_tehsil")==3){
						 MTehsil="JHANG";
					 }else if(rs.getInt("census_distributor_tehsil")==4){
						 MTehsil="GOJRA";
					 }else if(rs.getInt("census_distributor_tehsil")==5){
						 MTehsil="FAISALABAD";
					 }else if(rs.getInt("census_distributor_tehsil")==6){
						 MTehsil="TANDLIANWALA";
					 }else if(rs.getInt("census_distributor_tehsil")==7){
						 MTehsil="SAMUNDRI";
					 }else if(rs.getInt("census_distributor_tehsil")==8){
						 MTehsil="JARANWALA";
					 }else if(rs.getInt("census_distributor_tehsil")==9){
						 MTehsil="JHUMRA";
					 }
					 
					 MVillage = rs.getString("census_distributor_town");
					 MLandMark = rs.getString("land_mark");
					 
					 //PJP ID
					 if(!rs.getString("pjp_id").equals("null")){
						 ResultSet rs23 = s2.executeQuery("SELECT * FROM pep.distributor_beat_plan where id="+rs.getLong("pjp_id"));
						 while(rs23.next()){
							PJP =  rs.getLong("pjp_id")+" - "+rs23.getString("label");
						 }
					 }
					 //Outlet Type
					 
					 if(rs.getLong("census_outlet_type")==1){
						 OutletType="Individual Outlet";
					 }else if(rs.getLong("census_outlet_type")==2){
						 OutletType="Clustered Outlet";
					 }
					 
					 //Shop Closed Status
					 
					 if(rs.getInt("is_census_shop_closed")==1){
						 if(rs.getInt("census_shop_closed_status")==1){
							 ShopClosedStatusN="Permanently";
						 }else{
							 ShopClosedStatusN="Temporarily";
						 }
					 }
					 
					 //Outlet Structure
					 if(rs.getInt("census_shop_structure")==1){
						 OutletStructure = "Fixed Structure";
					 }else if(rs.getInt("census_shop_structure")==2){
						 OutletStructure = "Non-Fix Structure";
					 }
					 
					 // Trade Sub channel
					 
					 if(rs.getInt("census_trader_channel_sub_channel")==1){
						 TradeSubChannel="Kiryana Store";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==2){
						 TradeSubChannel="General Store";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==3){
						 TradeSubChannel="Pan/Cigarette Shop";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==4){
						 TradeSubChannel="Beverage Street Vendor";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==5){
						 TradeSubChannel="Modern General Store";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==6){
						 TradeSubChannel="Supermarket";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==7){
						 TradeSubChannel="Hypermarket";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==8){
						 TradeSubChannel="Fine Restaurant";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==9){
						 TradeSubChannel="Fast Food Restaurant";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==10){
						 TradeSubChannel="Food Courts";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==11){
						 TradeSubChannel="Conventional Restaurant";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==12){
						 TradeSubChannel="Food Street Outlets";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==13){
						 TradeSubChannel="Local Food Stand";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==14){
						 TradeSubChannel="Airport";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==15){
						 TradeSubChannel="Airline";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==16){
						 TradeSubChannel="Railway Station";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==17){
						 TradeSubChannel="Railways";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==18){
						 TradeSubChannel="Bus Stand";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==19){
						 TradeSubChannel="Bus Service";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==20){
						 TradeSubChannel="Bakeries/Sweet Shops";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==21){
						 TradeSubChannel="Medical Stores";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==22){
						 TradeSubChannel="Petroleum Food Marts";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==23){
						 TradeSubChannel="School/Colleges/Universitie";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==24){
						 TradeSubChannel="Hospitals";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==25){
						 TradeSubChannel="Hotels";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==26){
						 TradeSubChannel="Clubs";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==27){
						 TradeSubChannel="Marriage Hall/Caterers";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==28){
						 TradeSubChannel="Cinemas";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==29){
						 TradeSubChannel="Park/Zoo/Play Land";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==30){
						 TradeSubChannel="Utility Stores/CSD";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==31){
						 TradeSubChannel="Institutional Canteens";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==32){
						 TradeSubChannel="Wholesaler";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==33){
						 TradeSubChannel="Modern Meat Shop";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==34){
						 TradeSubChannel="PCO";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==35){
						 TradeSubChannel="Snooker Club";
					 }else if(rs.getInt("census_trader_channel_sub_channel")==36){
						 TradeSubChannel="Other Beverage Selling Outlets";
					 }
					 
					 //Area Sq.ft
					 
					 if(rs.getInt("census_area_seq_feet")==1){
						AreaSqFt="Upto 100";
					 }else if(rs.getInt("census_area_seq_feet")==2){
						 AreaSqFt="100 - 500"; 
					 }else if(rs.getInt("census_area_seq_feet")==3){
						 AreaSqFt="500 - 1000";
					 }else if(rs.getInt("census_area_seq_feet")==4){
						 AreaSqFt="1000 & Above";
					 }
					 
					 
					 if(rs.getInt("census_region_id")==1){
						 Region= "RM1 (Sargodha)";
					 }else if(rs.getInt("census_region_id")==2){
						 Region= "RM2 (Okara)";
					 }else if(rs.getInt("census_region_id")==3){
						 Region= "RM3 (Jhang)";
					 }else if(rs.getInt("census_region_id")==4)	{
						 Region= "RM4 (Faisalabad Out-1)";
					 }else if(rs.getInt("census_region_id")==5){
						 Region= "RM5 (FD Base Market)";
					 }else if(rs.getInt("census_region_id")==6){
						 Region= "RM6 (Toba Tek Singh)";
					 }else if(rs.getInt("census_region_id")==7){
						 Region= "RM9 (Faisalabad out-2)";
					 }else if(rs.getInt("census_region_id")==8){
						 Region= "Misc";
					 }else if(rs.getInt("census_region_id")==9){
						 Region= "None";
					 }else if(rs.getInt("census_region_id")==10){
						 Region= "RM7 Sargodha-II";
					 }else if(rs.getInt("census_region_id")==11){
						 Region= "RM8 FSD Base-II";
					 }
					 
						
					 
					Distributor = rs.getString("census_distributor_id")+" - "+rs.getString("census_distributor_name");
					 
					if(rs.getInt("census_area")==1){
						Area="Urban";
					}else{
						Area="Rural";	
					}
					if(rs.getInt("census_shop_type")==1){
						ShopType="Beverage Selling";
					}else{
						ShopType="Non Beverage Selling";
					}
					
					 
					LandMark=rs.getString("land_mark");
					
					if(rs.getInt("census_segment")==1){
						Location="Main Street";
					}else if(rs.getInt("census_segment")==2){
						Location="Secondary Street";
					}else if(rs.getInt("census_segment")==3){
						Location="Highway";
					}/*else if(rs.getInt("census_segment")==4){
						Location="Captive";
					}*/
					 
					
					
					 if(rs.getInt("census_shop_location")==1){
						 ShopLocation="Residential"; 
					 }else if(rs.getInt("census_shop_location")==2){
						 ShopLocation="Commercial"; 
					 }else if(rs.getInt("census_shop_location")==3){
						 ShopLocation= "Industrial";
					 }
					 
					
					 OwnerName=rs.getString("census_outlet_contact_person");
					
					
					 OwnerContactNo1=rs.getString("census_outlet_contact_1");
					 OwnerContactNo2=rs.getString("census_outlet_contact_2");
					 OwnerCNIC=rs.getString("census_owner_cnic_1");
					 
					 if(rs.getInt("census_owner_contact_person_relation")==1){
						 ContactPerson="Owner";
					 }else if(rs.getInt("census_owner_contact_person_relation")==2){
						 ContactPerson="Employee";
					 }else if(rs.getInt("census_owner_contact_person_relation")==3){
						 ContactPerson="First Relation";
					 }else if(rs.getInt("census_owner_contact_person_relation")==4){
						 ContactPerson="Second Relation";
					 }else if(rs.getInt("census_owner_contact_person_relation")==5){
						 ContactPerson="Friend";
					 }else if(rs.getInt("census_owner_contact_person_relation")==6){
						 ContactPerson="Partner";
					 }
					 
					 ContactPersonName=rs.getString("census_outlet_contact_name");
					 
					 ContactPersonNo=rs.getString("census_outlet_contact_3");
					 ContactPersonCNIC=rs.getString("census_owner_cnic_2");
					 FBID=rs.getString("census_facebook_id");
					 EmailID=rs.getString("census_email_id");
					 
					//shopkeeper
					 
					 if(rs.getInt("is_census_shop_closed")!=0){
						 IsShopClosed = "Closed";
						 
						 if(rs.getInt("census_shop_closed_status")==1){
							 ShopClosedStatus="Permanently";
						 }else{
							 ShopClosedStatus="Temporarily";
						 }
					 }
					 
					 
					 if(rs.getInt("census_day_off")==1){
						 DayOff="Sunday";
					 }else  if(rs.getInt("census_day_off")==2){
						 DayOff="Monday";
					 }else  if(rs.getInt("census_day_off")==3){
						 DayOff="Tuesday";
					 }else  if(rs.getInt("census_day_off")==4){
						 DayOff="Wednesday";
					 }else  if(rs.getInt("census_day_off")==5){
						 DayOff="Thursday";
					 }else  if(rs.getInt("census_day_off")==6){
						 DayOff="Friday";
					 }else  if(rs.getInt("census_day_off")==7){
						 DayOff="Saturday";
					 }else{
						 DayOff="None";
					 }
					 
					 
					 if(rs.getInt("census_partially_timing")==1){
						 PartialTiming="Morning";
					 }else if(rs.getInt("census_partially_timing")==2){
						 PartialTiming="Noon";
					 }else if(rs.getInt("census_partially_timing")==3){
						 PartialTiming="Night";
					 }else if(rs.getInt("census_partially_timing")==4){
						 PartialTiming="None";
					 }
					 
					 
					 if(rs.getInt("census_feeded_stock_1")==1){
						 SDistributor="Distributor";
					 }else{
						 SDistributor=""; 
					 }
					 
					 if(rs.getInt("census_feeded_stock_2")==1){
						 WholeSaller=", Wholeseller";
					 }else{
						 WholeSaller="";
					 }
					 
					 if(rs.getInt("census_feeded_stock_3")==1){
						 SubDistributor=", Sub-Distributor";
					 }else{
						 SubDistributor="";
					 }
					 
					 SuppliedBy=SDistributor+""+WholeSaller+""+SubDistributor;
					
					 if(rs.getInt("census_financial_service_1")==1){
						 EasyPaisa="Easy Paisa";
					 }
					 
					 if(rs.getInt("census_financial_service_2")==1){
						 Upaisa="U Paisa";
					 }
					 
					 if(rs.getInt("census_financial_service_3")==1){
						 MobiCash="Mobi Cash";
					 }
					 
					 if(rs.getInt("census_financial_service_4")==1){
						 Warid="Warid Mobile Paisa";
					 }
					 
					 if(rs.getInt("census_financial_service_5")==1){
						 TimePay="Time Pay";
					 }
					 
					 if(rs.getInt("census_financial_service_6")==1){
						 Other="Other";
					 }
					 
					
					 
					 if(EasyPaisa!=""){
						 EasyPaisa+=","; 
					 }
					 if(Upaisa!=""){
						 Upaisa+=","; 
					 }
					 if(MobiCash!=""){
						 MobiCash+=","; 
					 }
					 if(Warid!=""){
						 Warid+=","; 
					 }
					 if(TimePay!=""){
						 TimePay+=","; 
					 }
					 if(Other!=""){
						 Other+=","; 
					 }
					 
					 FinancialService += EasyPaisa+""+Upaisa+""+MobiCash+""+Warid+""+TimePay+""+Other;
					 
					 
					 ShopOpeningTime = rs.getString("census_owner_contact_person_shop_opening_time");
					 ShopCloseTime = rs.getString("census_owner_contact_person_shop_close_time");
					 
					 
					 //Status Tab
					 
					 if(rs.getInt("census_shop_status")==1){
						 ShopStatus="Seasonal";
					 }else if(rs.getInt("census_shop_status")==2){
						 ShopStatus="Permanent";
					 }
					 
					  
					  ShopHistory=rs.getString("census_shop_establishment_history");
					  
					  if(rs.getInt("census_business_structure")==1){
						  BusinessStructure="Self";
					   }else if(rs.getInt("census_business_structure")==2){
						   BusinessStructure="Chain";
					   }else if(rs.getInt("census_business_structure")==3){
						   BusinessStructure="Partnership";
					   }
					  
					  if(rs.getInt("census_shop_potential")==1){
						  SocioEco="High";
					   }else if(rs.getInt("census_shop_potential")==2){
						   SocioEco="Medium";
					   }else if(rs.getInt("census_shop_potential")==3){
						   SocioEco="Low";
					   }
					  
					  if(rs.getInt("census_customer_type")==1){
						  SShopType="Wholesale";
					   }else if(rs.getInt("census_customer_type")==2){
						   SShopType="Retailer";
					   }else if(rs.getInt("census_customer_type")==3){
						   SShopType="Both";
					   }
					  
					  
					 
					  
					 
					  Wholesale=rs.getString("census_customer_type_wholesale_percent");
					  Retailer=rs.getString("census_customer_type_retailer_percent");
					  
					  if(rs.getInt("census_service_type")==1){
						  ServiceType="Self";
					   }else if(rs.getInt("census_service_type")==2){
						   ServiceType="Counter";
					   }else if(rs.getInt("census_service_type")==3){
						   ServiceType="Both";
					   }
					  
					  if(rs.getInt("census_csd_turnover_per_day")==1){
						  CSDTurnOver="Less than 2 cases";
					   }else if(rs.getInt("census_csd_turnover_per_day")==2){
						   CSDTurnOver="2-5 cases";
					   }else if(rs.getInt("census_csd_turnover_per_day")==3){
						   CSDTurnOver="5-10 cases";
					   }else if(rs.getInt("census_csd_turnover_per_day")==4){
						   CSDTurnOver="10-15 cases";
					   }else if(rs.getInt("census_csd_turnover_per_day")==5){
						   CSDTurnOver="More than 15";
					   }
					  
					  
					  //TradeChannel=rs.getString("trade_channel_label");
					 // TradeSubChannel=rs.getString("trade_sub_channel_labe");
					 
				
					  //Beverages
					  
					   PICSD= rs.getInt("pi_csd");
					   PISting=rs.getInt("pi_sting");
					   KO=rs.getInt("ko");
					   
					   SupplyFrequency=rs.getString("supply_frequency");
					   SupplyFrequencyKO=rs.getString("supply_frequency_ko");
					   SupplyFrequencyGormet=rs.getString("supply_frequency_g");
					   SupplyFrequencyColaNext=rs.getString("supply_frequency_cn");
					   
					   
					   if(rs.getInt("census_exclusivity_agreement_1")==1){
						   ExPepsi="Pepsi";
						 }
						 
						 if(rs.getInt("census_exclusivity_agreement_2")==1){
							 ExCoke="Coke";
						 }
						 
						 if(rs.getInt("census_exclusivity_agreement_3")==1){
							 ExGourmet="Gourmet";
						 }
						 
						 if(rs.getInt("census_exclusivity_agreement_4")==1){
							 ExMezan="Mezan";
						 }
						 
						 if(rs.getInt("census_exclusivity_agreement_5")==1){
							 ExOther="Other";
						 }
						 
						 
						 
						 if(ExPepsi!=""){
							 ExPepsi+=",";
						 }
						 
						 if(ExCoke!=""){
							 ExCoke+=",";
						 }
						 
						 if(ExGourmet!=""){
							 ExGourmet+=",";
						 }
						 
						 if(ExMezan!=""){
							 ExMezan+=",";
						 }
						 
						 if(ExOther!=""){
							 ExOther+=",";
						 }
						 
						 
					   
					   Exclusivity=ExPepsi+""+ExCoke+""+ExGourmet+""+ExMezan+""+ExOther ;
					  
					   
					   DiscountStatus="";
					   
					   
					   if(rs.getInt("census_discount_status_1")==1){
						   DPerCase="Per-Case";
						 }
						 
						 if(rs.getInt("census_discount_status_2")==1){
							 DTradePaymentFix="Trade Payment(Period Fixed)";
						 }
						 
						 if(rs.getInt("census_discount_status_3")==1){
							 DTradePaymentTarget="Trade Payment(Target Fixed)";
						 }
						 
						 
						 if(DPerCase!=""){
							 DPerCase+=",";
						 }
						 
						 if(DTradePaymentFix!=""){
							 DTradePaymentFix+=",";
						 }
						 
						 if(DTradePaymentTarget!=""){
							 DTradePaymentTarget+=",";
						 }
						 
					   
						 DiscountStatus=DPerCase+""+DTradePaymentFix+""+DTradePaymentTarget;
						 
						 
						 if(rs.getInt("census_shop_closed_status")==1){
							 IsCoveredByRCompnay="Yes";
							 
							 if(rs.getInt("census_shop_closed_status")==1){
								 IsCoveredByRCompnayDuration="Monthly";
							 }else if(rs.getInt("census_shop_closed_status")==2){
								 IsCoveredByRCompnayDuration="Quarterly";
							 }else if(rs.getInt("census_shop_closed_status")==3){
								 IsCoveredByRCompnayDuration="Annually";
							 }
							 
							 
							 
						 }
						 
						 
						 if(rs.getInt("stock_storage_loc1")==1){
							 StockStorageLocation1="Within Outlet";
						 }
						 
						 if(rs.getInt("stock_storage_loc2")==1){
							 StockStorageLocation2="Attached with Shop";
						 }
						 
						 if(rs.getInt("stock_storage_loc3")==1){
							 StockStorageLocation3="Elsewhere";
						 }
						 
						 if(rs.getInt("stock_storage_loc4")==1){
							 StockStorageLocation4="No Storage";
						 }
						 
						 if(rs.getInt("cash_machine_status")==1){
							 CashMachineQuantity=rs.getInt("cash_machine_quantity");
						 }
						 
						 
						 
						 
						  
						  if(rs.getInt("census_shop_selling_status_ssrb")==1){
							  BeveragesSellingFullCasesSSRB=rs.getInt("census_shop_selling_bev"); 
						  }
						  
						  if(rs.getInt("census_shop_selling_status_pet")==1){
							  BeveragesSellingFullCasesPET=rs.getInt("census_shop_selling_pet"); 
						  }
						  
						  if(rs.getInt("census_shop_selling_status_nrb")==1){
							  BeveragesSellingFullCasesTETRA=rs.getInt("census_shop_selling_nrb"); 
						  }
						  
						  if(rs.getInt("census_shop_selling_status_can")==1){
							  BeveragesSellingFullCasesCAN=rs.getInt("census_shop_selling_can"); 
						  }
						  
						  
						  //Exclusivity agreement
						  
						   
						   
						  
						 if(rs.getInt("census_exclusivity_agreement_1")==1){
							 ExclusivityAgreementPI = "Pepsi"; 
						 }
						 
						 if(rs.getInt("census_exclusivity_agreement_2")==1){
							 ExclusivityAgreementKO = "Coke"; 
						 }
						 
						 if(rs.getInt("census_exclusivity_agreement_3")==1){
							 ExclusivityAgreementGou = "Gormet"; 
						 }
						 
						 if(rs.getInt("census_exclusivity_agreement_4")==1){
							 ExclusivityAgreementMezan = "Mezan"; 
						 }
						 
						 if(rs.getInt("census_exclusivity_agreement_5")==1){
							 ExclusivityAgreementOther = "Other"; 
						 }
						 
						 
						 //Per Raw Case Discount  - SSRB
						 
						 if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_1")==1){
							  PerRawCaseDiscSSRB="1-25";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_1")==2){
							  PerRawCaseDiscSSRB="26-35";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_1")==3){
							  PerRawCaseDiscSSRB="36-45";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_1")==4){
							  PerRawCaseDiscSSRB="46-75";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_1")==5){
							  PerRawCaseDiscSSRB="76 Above";
							  //PerRawCaseDiscPET="";
						 }
						 
						 
						 //Per Raw Case Discount  - PET
						 
						 if(rs.getInt("census_exclusivity_agreement_pkr_pet_1")==1){
							 PerRawCaseDiscPET="1-25";
							
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_1")==2){
							 PerRawCaseDiscPET="26-35";
							 
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_1")==3){
							 PerRawCaseDiscPET="36-45";
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_1")==4){
							 PerRawCaseDiscPET="46-75";
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_1")==5){
							 PerRawCaseDiscPET="76 Above";
							  
						 }
						 
						 //Agreement Expiry Date
						 
						 
						 AgreementExpDate = rs.getInt("aggr_exp_date_mm")+"/"+rs.getInt("aggr_exp_date_yy");
						 
						 
						 //Agreement Type
						 
						 if(rs.getInt("census_shop_agreement_type")==1){
							 AgreementType="Verbal";
						 }else if(rs.getInt("census_shop_agreement_type")==2){
							 AgreementType="Written-Witnessed";
						 }else if(rs.getInt("census_shop_agreement_type")==3){
							 AgreementType="Written-Not Witnessed";
						 }
						 
						 //Agreement Period
						 
						 if(rs.getInt("census_shop_agreement_period")==1){
							 AgreementPeriod="1";
						 }else if(rs.getInt("census_shop_agreement_period")==2){
							 AgreementPeriod="2";
						 }else if(rs.getInt("census_shop_agreement_period")==3){
							 AgreementPeriod="3 & Above";
						 }
						 
						 //Agreement Type - 2
						 
						 if(rs.getInt("census_shop_agreement_type1_1")==1){
							 AgreementType2 += "Discount,";
						 } if(rs.getInt("census_shop_agreement_type1_2")==1){
							 AgreementType2 += "Signage,";
						 } if(rs.getInt("census_shop_agreement_type1_3")==1){
							 AgreementType2 += "Annual Support,";
						 } if(rs.getInt("census_shop_agreement_type1_4")==1){
							 AgreementType2 += "Exclusivity Fee,";
						 } if(rs.getInt("census_shop_agreement_type1_5")==1){
							 AgreementType2 += "Vol Incentive,";
						 } if(rs.getString("census_shop_agreement_type_other")!=""){
							 AgreementType2 += "Any Other,";
						 }
						 
						 
						 
						 /////////////////////////////////
						 ////////////////Discount Agreement - PI//////////
						 
							//Per Raw Case Discount  - SSRB
						 
						 if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_pi_1")==1){
							  PerRawCaseDiscSSRBPI="1-25";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_pi_1")==2){
							  PerRawCaseDiscSSRBPI="26-35";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_pi_1")==3){
							  PerRawCaseDiscSSRBPI="36-45";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_pi_1")==4){
							  PerRawCaseDiscSSRBPI="46-75";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_pi_1")==5){
							  PerRawCaseDiscSSRBPI="76 Above";
							  //PerRawCaseDiscPET="";
						 }
						 
						 
						 //Per Raw Case Discount  - PET
						 
						 if(rs.getInt("census_exclusivity_agreement_pkr_pet_pi_1")==1){
							 PerRawCaseDiscPETPI="1-25";
							
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_pi_1")==2){
							 PerRawCaseDiscPETPI="26-35";
							 
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_pi_1")==3){
							 PerRawCaseDiscPETPI="36-45";
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_pi_1")==4){
							 PerRawCaseDiscPETPI="46-75";
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_pi_1")==5){
							 PerRawCaseDiscPETPI="76 Above";
							  
						 }
						 
						 //Agreement Expiry Date
						 
						 
						 AgreementExpDatePI = rs.getInt("aggr_exp_date_mm_pi")+"/"+rs.getInt("aggr_exp_date_yy_pi");
						 
						 
						 //Agreement Type
						 
						 if(rs.getInt("census_shop_agreement_type_pi")==1){
							 AgreementTypePI="Verbal";
						 }else if(rs.getInt("census_shop_agreement_type_pi")==2){
							 AgreementTypePI="Written-Witnessed";
						 }else if(rs.getInt("census_shop_agreement_type_pi")==3){
							 AgreementTypePI="Written-Not Witnessed";
						 }
						 
						 //Agreement Period
						 
						 if(rs.getInt("census_shop_agreement_period_pi")==1){
							 AgreementPeriodPI="1";
						 }else if(rs.getInt("census_shop_agreement_period_pi")==2){
							 AgreementPeriodPI="2";
						 }else if(rs.getInt("census_shop_agreement_period_pi")==3){
							 AgreementPeriodPI="3 & Above";
						 }
						 
						 //Agreement Type - 2
						 
						 if(rs.getInt("census_shop_agreement_type1_pi_1")==1){
							 AgreementType2PI += "Discount,";
						 } if(rs.getInt("census_shop_agreement_type1_pi_2")==1){
							 AgreementType2PI += "Signage,";
						 } if(rs.getInt("census_shop_agreement_type1_pi_3")==1){
							 AgreementType2PI += "Annual Support,";
						 } if(rs.getInt("census_shop_agreement_type1_pi_4")==1){
							 AgreementType2PI += "Exclusivity Fee,";
						 } if(rs.getInt("census_shop_agreement_type1_pi_5")==1){
							 AgreementType2PI += "Vol Incentive,";
						 } if(rs.getString("census_shop_agreement_type_pi_other")!=""){
							 AgreementType2PI += "Any Other,";
						 }
						 
						 
						 
						 /////////////////////////////////////////////////////////
						 
						 /////////////////////////////////
						 ////////////////Discount Agreement - KO//////////
						 
							//Per Raw Case Discount  - SSRB
						 
						 if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_ko_1")==1){
							  PerRawCaseDiscSSRBKO="1-25";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_ko_1")==2){
							  PerRawCaseDiscSSRBKO="26-35";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_ko_1")==3){
							  PerRawCaseDiscSSRBKO="36-45";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_ko_1")==4){
							  PerRawCaseDiscSSRBKO="46-75";
							  //PerRawCaseDiscPET="";
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_ssrb_ko_1")==5){
							  PerRawCaseDiscSSRBKO="76 Above";
							  //PerRawCaseDiscPET="";
						 }
						 
						 
						 //Per Raw Case Discount  - PET
						 
						 if(rs.getInt("census_exclusivity_agreement_pkr_pet_ko_1")==1){
							 PerRawCaseDiscPETKO="1-25";
							
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_ko_1")==2){
							 PerRawCaseDiscPETKO="26-35";
							 
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_ko_1")==3){
							 PerRawCaseDiscPETKO="36-45";
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_ko_1")==4){
							 PerRawCaseDiscPETKO="46-75";
							  
						 }else if(rs.getInt("census_exclusivity_agreement_pkr_pet_ko_1")==5){
							 PerRawCaseDiscPETKO="76 Above";
							  
						 }
						 
						 //Agreement Expiry Date
						 
						 
						 AgreementExpDateKO = rs.getInt("aggr_exp_date_mm_ko")+"/"+rs.getInt("aggr_exp_date_yy_ko");
						 
						 
						 //Agreement Type
						 
						 if(rs.getInt("census_shop_agreement_type_ko")==1){
							 AgreementTypeKO="Verbal";
						 }else if(rs.getInt("census_shop_agreement_type_ko")==2){
							 AgreementTypeKO="Written-Witnessed";
						 }else if(rs.getInt("census_shop_agreement_type_ko")==3){
							 AgreementTypeKO="Written-Not Witnessed";
						 }
						 
						 //Agreement Period
						 
						 if(rs.getInt("census_shop_agreement_period_ko")==1){
							 AgreementPeriodKO="1";
						 }else if(rs.getInt("census_shop_agreement_period_ko")==2){
							 AgreementPeriodKO="2";
						 }else if(rs.getInt("census_shop_agreement_period_ko")==3){
							 AgreementPeriodKO="3 & Above";
						 }
						 
						 //Agreement Type - 2
						 
						 if(rs.getInt("census_shop_agreement_type1_ko_1")==1){
							 AgreementType2KO += "Discount,";
						 } if(rs.getInt("census_shop_agreement_type1_ko_2")==1){
							 AgreementType2KO += "Signage,";
						 } if(rs.getInt("census_shop_agreement_type1_ko_3")==1){
							 AgreementType2KO += "Annual Support,";
						 } if(rs.getInt("census_shop_agreement_type1_ko_4")==1){
							 AgreementType2KO += "Exclusivity Fee,";
						 } if(rs.getInt("census_shop_agreement_type1_ko_5")==1){
							 AgreementType2KO += "Vol Incentive,";
						 } if(rs.getString("census_shop_agreement_type_ko_other")!=""){
							 AgreementType2KO += "Any Other,";
						 }
						 
						 
						 
						 /////////////////////////////////////////////////////////
						 
						 
						 //// Supplied by Ko 
						 
						 //census_feeded_stock_percentage_1,census_feeded_stock_percentage_2,census_feeded_stock_percentage_3,census_feeded_stock_percentage_4
				
						if(!rs.getString("census_feeded_stock_percentage_1").equals("null")){
							SuppliedByDistributorKO=rs.getString("census_feeded_stock_percentage_1")+"%";
						}
						 
						if(!rs.getString("census_feeded_stock_percentage_2").equals("null")){
							SuppliedByWholeSellerKO=rs.getString("census_feeded_stock_percentage_2")+"%";
						}
						
						if(!rs.getString("census_feeded_stock_percentage_3").equals("null")){
							SuppliedByMobilerKO=rs.getString("census_feeded_stock_percentage_3")+"%";
						}
						
						if(!rs.getString("census_feeded_stock_percentage_4").equals("null")){
							SuppliedByDealerKO=rs.getString("census_feeded_stock_percentage_4")+"%";
						}
						 
						 
						 //Supplied by PI 
						 
						 //census_feeded_stock_1_p_pi_21,census_feeded_stock_2_p_pi_21,census_feeded_stock_3_p_pi_21,census_feeded_stock_4_p_pi_21
				
						if(!rs.getString("census_feeded_stock_1_p_pi_21").equals("null")){
							SuppliedByDistributorPI=rs.getString("census_feeded_stock_1_p_pi_21")+"%";
						}
						 
						if(!rs.getString("census_feeded_stock_2_p_pi_21").equals("null")){
							SuppliedByWholeSellerPI=rs.getString("census_feeded_stock_2_p_pi_21")+"%";
						}
						
						if(!rs.getString("census_feeded_stock_3_p_pi_21").equals("null")){
							SuppliedByMobilerPI=rs.getString("census_feeded_stock_3_p_pi_21")+"%";
						}
						
						if(!rs.getString("census_feeded_stock_4_p_pi_21").equals("null")){
							SuppliedByDealerPI=rs.getString("census_feeded_stock_4_p_pi_21")+"%";
						}
				}
		      
				
				
				if(1==1){
		      
		      /////// General 
					
					//2nd row header
				      
				     
				      
				      RowCount = RowCount+1;
				      
				      XSSFRow headerrow = spreadsheet.createRow((short) RowCount);	      
				      XSSFCell headercell = (XSSFCell) headerrow.createCell((short) 0);
				     
				      headercell.setCellValue("General");
				      Set2ndHeaderBackColor(workbook,headercell);
				      spreadsheet.addMergedRegion(new CellRangeAddress(
				    		  FirstRowCount+1, //first row (0-based)
				    		  FirstRowCount+1, //last row (0-based)
				    		  0, //first column (0-based)
				    	      3 //last column (0-based)
				    	      ));
		      
		      	XSSFRow headerrow2 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell2 = (XSSFCell) headerrow2.createCell((short) 0);  
		  		headercell2 = (XSSFCell) headerrow2.createCell((short) 0);
				headercell2.setCellValue("Outlet ID");
				SetNormalCellBackColor(workbook,headercell2);
			    
			     
		  		headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
				headercell2.setCellValue(OutletID);
				SetNormalCellBackColor(workbook,headercell2);
			    
			    headercell2 = (XSSFCell) headerrow2.createCell((short) 2);
				headercell2.setCellValue("");
				SetNormalCellBackColor(workbook,headercell2);
				
				headercell2 = (XSSFCell) headerrow2.createCell((short) 3);
				headercell2.setCellValue("");
				SetNormalCellBackColor(workbook,headercell2);
			    
			    
			    
			    RowCount=RowCount+1;
			    XSSFRow headerrow21 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell21 = (XSSFCell) headerrow21.createCell((short) 0);  
		  		headercell21 = (XSSFCell) headerrow21.createCell((short) 0);
				headercell21.setCellValue("System Outlet Name");
				SetNormalCellBackColor(workbook,headercell21);
			    
			     
		  		headercell21 = (XSSFCell) headerrow21.createCell((short) 1);
				headercell21.setCellValue(OutletName);
				SetNormalCellBackColor(workbook,headercell21);
			    
			    headercell21 = (XSSFCell) headerrow21.createCell((short) 2);
				headercell21.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21);
				
				headercell21 = (XSSFCell) headerrow21.createCell((short) 3);
				headercell21.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21);
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow22 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell22 = (XSSFCell) headerrow22.createCell((short) 0);  
		  		headercell22 = (XSSFCell) headerrow22.createCell((short) 0);
				headercell22.setCellValue("Outlet Name On Board");
				SetNormalCellBackColor(workbook,headercell22);
			    
			     
		  		headercell22 = (XSSFCell) headerrow22.createCell((short) 1);
				headercell22.setCellValue(OutletNameOB);
				SetNormalCellBackColor(workbook,headercell22);
			    
			    headercell22 = (XSSFCell) headerrow22.createCell((short) 2);
				headercell22.setCellValue("");
				SetNormalCellBackColor(workbook,headercell22);
				
				 headercell22 = (XSSFCell) headerrow22.createCell((short) 3);
					headercell22.setCellValue("");
					SetNormalCellBackColor(workbook,headercell22);
				
				 
				
				RowCount=RowCount+1;
			    XSSFRow headerrow23 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell23 = (XSSFCell) headerrow23.createCell((short) 0);  
		  		headercell23 = (XSSFCell) headerrow23.createCell((short) 0);
				headercell23.setCellValue("Actual Outlet Name");
				SetNormalCellBackColor(workbook,headercell23);
			    
			     
		  		headercell23 = (XSSFCell) headerrow23.createCell((short) 1);
				headercell23.setCellValue(OutletNameActual);
				SetNormalCellBackColor(workbook,headercell23);
			    
			    headercell23 = (XSSFCell) headerrow23.createCell((short) 2);
				headercell23.setCellValue("");
				SetNormalCellBackColor(workbook,headercell23);
				
				 headercell23 = (XSSFCell) headerrow23.createCell((short) 3);
					headercell23.setCellValue("");
					SetNormalCellBackColor(workbook,headercell23);
				
				
				
				
				 RowCount=RowCount+1;
			    XSSFRow headerrow24 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell24 = (XSSFCell) headerrow24.createCell((short) 0);  
		  		headercell24 = (XSSFCell) headerrow24.createCell((short) 0);
				headercell24.setCellValue("Distributor");
				SetNormalCellBackColor(workbook,headercell24);
			    
			     
		  		headercell24 = (XSSFCell) headerrow24.createCell((short) 1);
				headercell24.setCellValue(Distributor);
				SetNormalCellBackColor(workbook,headercell24);
			    
			    headercell24 = (XSSFCell) headerrow24.createCell((short) 2);
				headercell24.setCellValue("");
				SetNormalCellBackColor(workbook,headercell24);
				
				headercell24 = (XSSFCell) headerrow24.createCell((short) 3);
				headercell24.setCellValue("");
				SetNormalCellBackColor(workbook,headercell24);
				
				
				
				
				 RowCount=RowCount+1;
			    XSSFRow headerrow25 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell25 = (XSSFCell) headerrow25.createCell((short) 0);  
		  		headercell25 = (XSSFCell) headerrow25.createCell((short) 0);
				headercell25.setCellValue("PJP");
				SetNormalCellBackColor(workbook,headercell25);
			    
			     
		  		headercell25 = (XSSFCell) headerrow25.createCell((short) 1);
				headercell25.setCellValue(PJP);
				SetNormalCellBackColor(workbook,headercell25);
			    
			    headercell25 = (XSSFCell) headerrow25.createCell((short) 2);
				headercell25.setCellValue("");
				SetNormalCellBackColor(workbook,headercell25);
				
				 headercell25 = (XSSFCell) headerrow25.createCell((short) 3);
					headercell25.setCellValue("");
					SetNormalCellBackColor(workbook,headercell25);
				
				
				
				
				
				
				 RowCount=RowCount+1;
			    XSSFRow headerrow26 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell26 = (XSSFCell) headerrow26.createCell((short) 0);  
		  		headercell26 = (XSSFCell) headerrow26.createCell((short) 0);
				headercell26.setCellValue("Area");
				SetNormalCellBackColor(workbook,headercell26);
			    
			     
		  		headercell26 = (XSSFCell) headerrow26.createCell((short) 1);
				headercell26.setCellValue(Area);
				SetNormalCellBackColor(workbook,headercell26);
			    
			    headercell26 = (XSSFCell) headerrow26.createCell((short) 2);
				headercell26.setCellValue("");
				SetNormalCellBackColor(workbook,headercell26);
				
				 headercell26 = (XSSFCell) headerrow26.createCell((short) 3);
					headercell26.setCellValue("");
					SetNormalCellBackColor(workbook,headercell26);
				
				
				
				
				
				 RowCount=RowCount+1;
			    XSSFRow headerrow27 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell27 = (XSSFCell) headerrow27.createCell((short) 0);  
		  		headercell27 = (XSSFCell) headerrow27.createCell((short) 0);
				headercell27.setCellValue("Location");
				SetNormalCellBackColor(workbook,headercell27);
			    
			     
		  		headercell27 = (XSSFCell) headerrow27.createCell((short) 1);
				headercell27.setCellValue(Location);
				SetNormalCellBackColor(workbook,headercell27);
			    
			    headercell27 = (XSSFCell) headerrow27.createCell((short) 2);
				headercell27.setCellValue("");
				SetNormalCellBackColor(workbook,headercell27);
				
				headercell27 = (XSSFCell) headerrow27.createCell((short) 3);
				headercell27.setCellValue("");
				SetNormalCellBackColor(workbook,headercell27);
				
				
				
				
				
				 RowCount=RowCount+1;
			    XSSFRow headerrow28 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell28 = (XSSFCell) headerrow28.createCell((short) 0);  
		  		headercell28 = (XSSFCell) headerrow28.createCell((short) 0);
				headercell28.setCellValue("Shop Location");
				SetNormalCellBackColor(workbook,headercell28);
			    
			     
		  		headercell28 = (XSSFCell) headerrow28.createCell((short) 1);
				headercell28.setCellValue(ShopLocation);
				SetNormalCellBackColor(workbook,headercell28);
			    
			    headercell28 = (XSSFCell) headerrow28.createCell((short) 2);
				headercell28.setCellValue("");
				SetNormalCellBackColor(workbook,headercell28);
				
				headercell28 = (XSSFCell) headerrow28.createCell((short) 3);
				headercell28.setCellValue("");
				SetNormalCellBackColor(workbook,headercell28);
				
				
				
				
				
				 RowCount=RowCount+1;
			    XSSFRow headerrow29 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell29 = (XSSFCell) headerrow29.createCell((short) 0);  
		  		headercell29 = (XSSFCell) headerrow29.createCell((short) 0);
				headercell29.setCellValue("Socio Economic Classification");
				SetNormalCellBackColor(workbook,headercell29);
			    
			     
		  		headercell29 = (XSSFCell) headerrow29.createCell((short) 1);
				headercell29.setCellValue(SocioEco);
				SetNormalCellBackColor(workbook,headercell29);
			    
			    headercell29 = (XSSFCell) headerrow29.createCell((short) 2);
				headercell29.setCellValue("");
				SetNormalCellBackColor(workbook,headercell29);
				
				 headercell29 = (XSSFCell) headerrow29.createCell((short) 3);
					headercell29.setCellValue("");
					SetNormalCellBackColor(workbook,headercell29);
				
				
				
				
				
				 RowCount=RowCount+1;
			    XSSFRow headerrow2110 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell2110 = (XSSFCell) headerrow2110.createCell((short) 0);  
		  		headercell2110 = (XSSFCell) headerrow2110.createCell((short) 0);
				headercell2110.setCellValue("Outlet Type");
				SetNormalCellBackColor(workbook,headercell2110);
			    
			     
		  		headercell2110 = (XSSFCell) headerrow2110.createCell((short) 1);
				headercell2110.setCellValue(OutletType);
				SetNormalCellBackColor(workbook,headercell2110);
			    
			    headercell2110 = (XSSFCell) headerrow2110.createCell((short) 2);
				headercell2110.setCellValue("");
				SetNormalCellBackColor(workbook,headercell2110);
				
				 headercell2110 = (XSSFCell) headerrow2110.createCell((short) 3);
					headercell2110.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2110);
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow2111 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell2111 = (XSSFCell) headerrow2111.createCell((short) 0);  
		  		headercell2111 = (XSSFCell) headerrow2111.createCell((short) 0);
				headercell2111.setCellValue("Address (with key location)");
				SetNormalCellBackColor(workbook,headercell2110);
			    
			     
		  		headercell2111 = (XSSFCell) headerrow2111.createCell((short) 1);
				headercell2111.setCellValue(OutletAddress);
				SetNormalCellBackColor(workbook,headercell2111);
			    
			    headercell2111 = (XSSFCell) headerrow2111.createCell((short) 2);
				headercell2111.setCellValue("");
				SetNormalCellBackColor(workbook,headercell2111);
				
				 headercell2111 = (XSSFCell) headerrow2111.createCell((short) 3);
					headercell2111.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2111);
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow2112 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell2112 = (XSSFCell) headerrow2112.createCell((short) 0);  
		  		headercell2112 = (XSSFCell) headerrow2112.createCell((short) 0);
				headercell2112.setCellValue("District");
				SetNormalCellBackColor(workbook,headercell2112);
			    
			     
		  		headercell2112 = (XSSFCell) headerrow2112.createCell((short) 1);
				headercell2112.setCellValue(MDistrict);
				SetNormalCellBackColor(workbook,headercell2112);
			    
			    headercell2112 = (XSSFCell) headerrow2112.createCell((short) 2);
				headercell2112.setCellValue("");
				SetNormalCellBackColor(workbook,headercell2112);
				
				headercell2112 = (XSSFCell) headerrow2112.createCell((short) 3);
				headercell2112.setCellValue("");
				SetNormalCellBackColor(workbook,headercell2112);
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow2113 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell2113 = (XSSFCell) headerrow2113.createCell((short) 0);  
		  		headercell2113 = (XSSFCell) headerrow2113.createCell((short) 0);
				headercell2113.setCellValue("Tehsil");
				SetNormalCellBackColor(workbook,headercell2113);
			    
			     
		  		headercell2113 = (XSSFCell) headerrow2113.createCell((short) 1);
				headercell2113.setCellValue(MTehsil);
				SetNormalCellBackColor(workbook,headercell2113);
			    
			    headercell2113 = (XSSFCell) headerrow2113.createCell((short) 2);
				headercell2113.setCellValue("");
				SetNormalCellBackColor(workbook,headercell2113);
				
				headercell2113 = (XSSFCell) headerrow2113.createCell((short) 3);
				headercell2113.setCellValue("");
				SetNormalCellBackColor(workbook,headercell2113);
			     
			     
			    
		      
		      
				RowCount=RowCount+1;
			    XSSFRow headerrow2114 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell2114 = (XSSFCell) headerrow2114.createCell((short) 0);  
		  		headercell2114 = (XSSFCell) headerrow2114.createCell((short) 0);
				headercell2114.setCellValue("Village");
				SetNormalCellBackColor(workbook,headercell2114);
			    
			     
		  		headercell2114 = (XSSFCell) headerrow2114.createCell((short) 1);
				headercell2114.setCellValue(MVillage);
				SetNormalCellBackColor(workbook,headercell2114);
			    
			    headercell2114 = (XSSFCell) headerrow2114.createCell((short) 2);
				headercell2114.setCellValue("");
				SetNormalCellBackColor(workbook,headercell2114);
				
				headercell2114 = (XSSFCell) headerrow2114.createCell((short) 3);
				headercell2114.setCellValue("");
				SetNormalCellBackColor(workbook,headercell2114);
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow2115 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);  
		  		headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);
				headercell2115.setCellValue("Land Mark");
				SetNormalCellBackColor(workbook,headercell2115);
			    
			     
		  		headercell2115 = (XSSFCell) headerrow2115.createCell((short) 1);
				headercell2115.setCellValue(MLandMark);
				SetNormalCellBackColor(workbook,headercell2115);
			    
			    headercell2115 = (XSSFCell) headerrow2115.createCell((short) 2);
				headercell2115.setCellValue("");
				SetNormalCellBackColor(workbook,headercell2115);
				
				 headercell2115 = (XSSFCell) headerrow2115.createCell((short) 3);
					headercell2115.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2115);
			
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow2116 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell2116 = (XSSFCell) headerrow2116.createCell((short) 0);  
		  		headercell2116 = (XSSFCell) headerrow2116.createCell((short) 0);
				headercell2116.setCellValue("Shop Closed Status");
				SetNormalCellBackColor(workbook,headercell2116);
			    
			     
		  		headercell2116 = (XSSFCell) headerrow2116.createCell((short) 1);
				headercell2116.setCellValue(ShopClosedStatusN);
				SetNormalCellBackColor(workbook,headercell2116);
			    
			    headercell2116 = (XSSFCell) headerrow2116.createCell((short) 2);
				headercell2116.setCellValue("");
				SetNormalCellBackColor(workbook,headercell2116);
				
				headercell2116 = (XSSFCell) headerrow2116.createCell((short) 3);
				headercell2116.setCellValue("");
				SetNormalCellBackColor(workbook,headercell2116);
			 
					
					
				   
				    
				    
				RowCount=RowCount+1;
			    XSSFRow headerrow2117 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell2117 = (XSSFCell) headerrow2117.createCell((short) 0);  
		  		headercell2117 = (XSSFCell) headerrow2117.createCell((short) 0);
				headercell2117.setCellValue("Outlet Structure");
				SetNormalCellBackColor(workbook,headercell2117);
			    
			     
		  		headercell2117 = (XSSFCell) headerrow2117.createCell((short) 1);
				headercell2117.setCellValue(OutletStructure);
				SetNormalCellBackColor(workbook,headercell2117);
			    
			    headercell2117 = (XSSFCell) headerrow2117.createCell((short) 2);
				headercell2117.setCellValue("");
				SetNormalCellBackColor(workbook,headercell2117);
				
				 headercell2117 = (XSSFCell) headerrow2117.createCell((short) 3);
					headercell2117.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2117);
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow2118 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell2118 = (XSSFCell) headerrow2118.createCell((short) 0);  
		  		headercell2118 = (XSSFCell) headerrow2118.createCell((short) 0);
				headercell2118.setCellValue("Trade Sub-Channel");
				SetNormalCellBackColor(workbook,headercell2118);
			    
			     
		  		headercell2118 = (XSSFCell) headerrow2118.createCell((short) 1);
				headercell2118.setCellValue(TradeSubChannel);
				SetNormalCellBackColor(workbook,headercell2118);
			    
			    headercell2118 = (XSSFCell) headerrow2118.createCell((short) 2);
				headercell2118.setCellValue("");
				SetNormalCellBackColor(workbook,headercell2118);
				
				headercell2118 = (XSSFCell) headerrow2118.createCell((short) 3);
				headercell2118.setCellValue("");
				SetNormalCellBackColor(workbook,headercell2118);
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow2119 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell2119 = (XSSFCell) headerrow2119.createCell((short) 0);  
		  		headercell2119 = (XSSFCell) headerrow2119.createCell((short) 0);
				headercell2119.setCellValue("Service Type");
				SetNormalCellBackColor(workbook,headercell2119);
			    
			     
		  		headercell2119 = (XSSFCell) headerrow2119.createCell((short) 1);
				headercell2119.setCellValue(ServiceType);
				SetNormalCellBackColor(workbook,headercell2119);
			    
			    headercell2119 = (XSSFCell) headerrow2119.createCell((short) 2);
				headercell2119.setCellValue("");
				SetNormalCellBackColor(workbook,headercell2119);
				
				headercell2119 = (XSSFCell) headerrow2119.createCell((short) 3);
				headercell2119.setCellValue("");
				SetNormalCellBackColor(workbook,headercell2119);
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow21110 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell21110 = (XSSFCell) headerrow21110.createCell((short) 0);  
		  		headercell21110 = (XSSFCell) headerrow21110.createCell((short) 0);
				headercell21110.setCellValue("Shop Type");
				SetNormalCellBackColor(workbook,headercell21110);
			    
			     
		  		headercell21110 = (XSSFCell) headerrow21110.createCell((short) 1);
				headercell21110.setCellValue(ShopType);
				SetNormalCellBackColor(workbook,headercell21110);
			    
			    headercell21110 = (XSSFCell) headerrow21110.createCell((short) 2);
				headercell21110.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21110);
				
				headercell21110 = (XSSFCell) headerrow21110.createCell((short) 3);
				headercell21110.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21110);
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow21111 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell21111 = (XSSFCell) headerrow21111.createCell((short) 0);  
		  		headercell21111 = (XSSFCell) headerrow21111.createCell((short) 0);
				headercell21111.setCellValue("Owner Name");
				SetNormalCellBackColor(workbook,headercell21111);
			    
			     
		  		headercell21111 = (XSSFCell) headerrow21111.createCell((short) 1);
				headercell21111.setCellValue(OwnerName);
				SetNormalCellBackColor(workbook,headercell21111);
			    
			    headercell21111 = (XSSFCell) headerrow21111.createCell((short) 2);
				headercell21111.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21111);
				
				headercell21111 = (XSSFCell) headerrow21111.createCell((short) 3);
				headercell21111.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21111);
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow21112 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell21112 = (XSSFCell) headerrow21112.createCell((short) 0);  
		  		headercell21112 = (XSSFCell) headerrow21112.createCell((short) 0);
				headercell21112.setCellValue("Landline");
				SetNormalCellBackColor(workbook,headercell21112);
			    
			     
		  		headercell21112 = (XSSFCell) headerrow21112.createCell((short) 1);
				headercell21112.setCellValue(OwnerContactNo1);
				SetNormalCellBackColor(workbook,headercell21112);
			    
			    headercell21112 = (XSSFCell) headerrow21112.createCell((short) 2);
				headercell21112.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21112);
				
				headercell21112 = (XSSFCell) headerrow21112.createCell((short) 3);
				headercell21112.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21112);
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow21113 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell21113 = (XSSFCell) headerrow21113.createCell((short) 0);  
		  		headercell21113 = (XSSFCell) headerrow21113.createCell((short) 0);
				headercell21113.setCellValue("Mobile");
				SetNormalCellBackColor(workbook,headercell21113);
			    
			     
		  		headercell21113 = (XSSFCell) headerrow21113.createCell((short) 1);
				headercell21113.setCellValue(OwnerContactNo2);
				SetNormalCellBackColor(workbook,headercell21113);
			    
			    headercell21113 = (XSSFCell) headerrow21113.createCell((short) 2);
				headercell21113.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21113);
				
				headercell21113 = (XSSFCell) headerrow21113.createCell((short) 3);
				headercell21113.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21113);
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow21114 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell21114 = (XSSFCell) headerrow21114.createCell((short) 0);  
		  		headercell21114 = (XSSFCell) headerrow21114.createCell((short) 0);
				headercell21114.setCellValue("Owner CNIC");
				SetNormalCellBackColor(workbook,headercell21114);
			    
			     
		  		headercell21114 = (XSSFCell) headerrow21114.createCell((short) 1);
				headercell21114.setCellValue(OwnerCNIC);
				SetNormalCellBackColor(workbook,headercell21114);
			    
			    headercell21114 = (XSSFCell) headerrow21114.createCell((short) 2);
				headercell21114.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21114);
				
				headercell21114 = (XSSFCell) headerrow21114.createCell((short) 3);
				headercell21114.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21114);
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow21115 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell21115 = (XSSFCell) headerrow21115.createCell((short) 0);  
		  		headercell21115 = (XSSFCell) headerrow21115.createCell((short) 0);
				headercell21115.setCellValue("Contact Person");
				SetNormalCellBackColor(workbook,headercell21115);
			    
			     
		  		headercell21115 = (XSSFCell) headerrow21115.createCell((short) 1);
				headercell21115.setCellValue(ContactPerson);
				SetNormalCellBackColor(workbook,headercell21115);
			    
			    headercell21115 = (XSSFCell) headerrow21115.createCell((short) 2);
				headercell21115.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21115);
				
				headercell21115 = (XSSFCell) headerrow21115.createCell((short) 3);
				headercell21115.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21115);
				
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow21117 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell21117 = (XSSFCell) headerrow21117.createCell((short) 0);  
		  		headercell21117 = (XSSFCell) headerrow21117.createCell((short) 0);
				headercell21117.setCellValue("Contact Person Name");
				SetNormalCellBackColor(workbook,headercell21117);
			    
			     
		  		headercell21117 = (XSSFCell) headerrow21117.createCell((short) 1);
				headercell21117.setCellValue(ContactPersonName);
				SetNormalCellBackColor(workbook,headercell21117);
			    
			    headercell21117 = (XSSFCell) headerrow21117.createCell((short) 2);
				headercell21117.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21117);
				
				headercell21117 = (XSSFCell) headerrow21117.createCell((short) 3);
				headercell21117.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21117);
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow21118 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell21118 = (XSSFCell) headerrow21118.createCell((short) 0);  
		  		headercell21118 = (XSSFCell) headerrow21118.createCell((short) 0);
				headercell21118.setCellValue("Contact Person No.");
				SetNormalCellBackColor(workbook,headercell21118);
			    
			     
		  		headercell21118 = (XSSFCell) headerrow21118.createCell((short) 1);
				headercell21118.setCellValue(ContactPersonNo);
				SetNormalCellBackColor(workbook,headercell21118);
			    
			    headercell21118 = (XSSFCell) headerrow21118.createCell((short) 2);
				headercell21118.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21118);
				
				headercell21118 = (XSSFCell) headerrow21118.createCell((short) 3);
				headercell21118.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21118);
				
				
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow21119 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell21119 = (XSSFCell) headerrow21119.createCell((short) 0);  
		  		headercell21119 = (XSSFCell) headerrow21119.createCell((short) 0);
				headercell21119.setCellValue("Contact Person CNIC");
				SetNormalCellBackColor(workbook,headercell21119);
			    
			     
		  		headercell21119 = (XSSFCell) headerrow21119.createCell((short) 1);
				headercell21119.setCellValue(ContactPersonCNIC);
				SetNormalCellBackColor(workbook,headercell21119);
			    
			    headercell21119 = (XSSFCell) headerrow21119.createCell((short) 2);
				headercell21119.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21119);
				
				headercell21119 = (XSSFCell) headerrow21119.createCell((short) 3);
				headercell21119.setCellValue("");
				SetNormalCellBackColor(workbook,headercell21119);
				
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow211120 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell211120 = (XSSFCell) headerrow211120.createCell((short) 0);  
		  		headercell211120 = (XSSFCell) headerrow211120.createCell((short) 0);
				headercell211120.setCellValue("Email ID");
				SetNormalCellBackColor(workbook,headercell211120);
			    
			     
		  		headercell211120 = (XSSFCell) headerrow211120.createCell((short) 1);
				headercell211120.setCellValue(EmailID);
				SetNormalCellBackColor(workbook,headercell211120);
			    
			    headercell211120 = (XSSFCell) headerrow211120.createCell((short) 2);
				headercell211120.setCellValue("");
				SetNormalCellBackColor(workbook,headercell211120);
				
				 headercell211120 = (XSSFCell) headerrow211120.createCell((short) 3);
					headercell211120.setCellValue("");
					SetNormalCellBackColor(workbook,headercell211120);
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow211121 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell211121 = (XSSFCell) headerrow211121.createCell((short) 0);  
		  		headercell211121 = (XSSFCell) headerrow211121.createCell((short) 0);
				headercell211121.setCellValue("Area Sq. ft");
				SetNormalCellBackColor(workbook,headercell211121);
			    
			     
		  		headercell211121 = (XSSFCell) headerrow211121.createCell((short) 1);
				headercell211121.setCellValue(AreaSqFt);
				SetNormalCellBackColor(workbook,headercell211121);
			    
			    headercell211121 = (XSSFCell) headerrow211121.createCell((short) 2);
				headercell211121.setCellValue("");
				SetNormalCellBackColor(workbook,headercell211121);
				
				 headercell211121 = (XSSFCell) headerrow211121.createCell((short) 3);
					headercell211121.setCellValue("");
					SetNormalCellBackColor(workbook,headercell211121);
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow211122 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell211122 = (XSSFCell) headerrow211122.createCell((short) 0);  
		  		headercell211122 = (XSSFCell) headerrow211122.createCell((short) 0);
				headercell211122.setCellValue("CreatedBy");
				SetNormalCellBackColor(workbook,headercell211122);
			    
			     
		  		headercell211122 = (XSSFCell) headerrow211122.createCell((short) 1);
				headercell211122.setCellValue(CreatedBy);
				SetNormalCellBackColor(workbook,headercell211122);
			    
			    headercell211122 = (XSSFCell) headerrow211122.createCell((short) 2);
				headercell211122.setCellValue("");
				SetNormalCellBackColor(workbook,headercell211122);
				
				headercell211122 = (XSSFCell) headerrow211122.createCell((short) 3);
				headercell211122.setCellValue("");
				SetNormalCellBackColor(workbook,headercell211122);
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow211123 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell211123 = (XSSFCell) headerrow211123.createCell((short) 0);  
		  		headercell211123 = (XSSFCell) headerrow211123.createCell((short) 0);
				headercell211123.setCellValue("Latitude");
				SetNormalCellBackColor(workbook,headercell211123);
			    
			     
		  		headercell211123 = (XSSFCell) headerrow211123.createCell((short) 1);
				headercell211123.setCellValue(Lati);
				SetNormalCellBackColor(workbook,headercell211123);
			    
			    headercell211123 = (XSSFCell) headerrow211123.createCell((short) 2);
				headercell211123.setCellValue("");
				SetNormalCellBackColor(workbook,headercell211123);
				
				headercell211123 = (XSSFCell) headerrow211123.createCell((short) 3);
				headercell211123.setCellValue("");
				SetNormalCellBackColor(workbook,headercell211123);
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow211124 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell211124 = (XSSFCell) headerrow211124.createCell((short) 0);  
		  		headercell211124 = (XSSFCell) headerrow211124.createCell((short) 0);
				headercell211124.setCellValue("Longitude");
				SetNormalCellBackColor(workbook,headercell211124);
			    
			     
		  		headercell211124 = (XSSFCell) headerrow211124.createCell((short) 1);
				headercell211124.setCellValue(Longi);
				SetNormalCellBackColor(workbook,headercell211124);
			    
			    headercell211124 = (XSSFCell) headerrow211124.createCell((short) 2);
				headercell211124.setCellValue("");
				SetNormalCellBackColor(workbook,headercell211124);
				
				headercell211124 = (XSSFCell) headerrow211124.createCell((short) 3);
				headercell211124.setCellValue("");
				SetNormalCellBackColor(workbook,headercell211124);
				
				
				
				
				
				RowCount=RowCount+1;
			    XSSFRow headerrow211125 = spreadsheet.createRow((short) RowCount+1);        
		  		
		  		XSSFCell headercell211125 = (XSSFCell) headerrow211125.createCell((short) 0);  
		  		headercell211125 = (XSSFCell) headerrow211125.createCell((short) 0);
				headercell211125.setCellValue("Accuracy");
				SetNormalCellBackColor(workbook,headercell211125);
			    
			     
		  		headercell211125 = (XSSFCell) headerrow211125.createCell((short) 1);
				headercell211125.setCellValue(Accuracy);
				SetNormalCellBackColor(workbook,headercell211125);
			    
			    headercell211125 = (XSSFCell) headerrow211125.createCell((short) 2);
				headercell211125.setCellValue("");
				SetNormalCellBackColor(workbook,headercell211125);
				
				 headercell211125 = (XSSFCell) headerrow211125.createCell((short) 3);
					headercell211125.setCellValue("");
					SetNormalCellBackColor(workbook,headercell211125);
				}
				
				
				
				
				
				
				int ShopkeeperFlag=1;
				if(ShopkeeperFlag==1){
				/////// Shopkeeper 
					
					RowCount = RowCount+1;
				      
				      XSSFRow headerrow = spreadsheet.createRow((short) RowCount+1);	      
				      XSSFCell headercell = (XSSFCell) headerrow.createCell((short) 0);
				     
				      headercell.setCellValue("Shopkeeper");
				      Set2ndHeaderBackColor(workbook,headercell);
				      spreadsheet.addMergedRegion(new CellRangeAddress(
				    		  RowCount+1, //first row (0-based)
				    		  RowCount+1, //last row (0-based)
				    		  0, //first column (0-based)
				    	      3 //last column (0-based)
				    	      ));
					
				      
				      
				      RowCount = RowCount+1;
				      
			      	XSSFRow headerrow2Shop = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell2Shop = (XSSFCell) headerrow2Shop.createCell((short) 0);  
			  		headercell2Shop = (XSSFCell) headerrow2Shop.createCell((short) 0);
					headercell2Shop.setCellValue("Shop Status");
					SetNormalCellBackColor(workbook,headercell2Shop);
				    
				     
			  		headercell2Shop = (XSSFCell) headerrow2Shop.createCell((short) 1);
					headercell2Shop.setCellValue(ShopStatus);
					SetNormalCellBackColor(workbook,headercell2Shop);
				    
				    headercell2Shop = (XSSFCell) headerrow2Shop.createCell((short) 2);
					headercell2Shop.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2Shop);
					
					 headercell2Shop = (XSSFCell) headerrow2Shop.createCell((short) 3);
						headercell2Shop.setCellValue("");
						SetNormalCellBackColor(workbook,headercell2Shop);
				    
				    
				    
				    RowCount=RowCount+1;
				    XSSFRow headerrow21Shop = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell21Shop = (XSSFCell) headerrow21Shop.createCell((short) 0);  
			  		headercell21Shop = (XSSFCell) headerrow21Shop.createCell((short) 0);
					headercell21Shop.setCellValue("Off Day");
					SetNormalCellBackColor(workbook,headercell21Shop);
				    
				     
			  		headercell21Shop = (XSSFCell) headerrow21Shop.createCell((short) 1);
					headercell21Shop.setCellValue(DayOff);
					SetNormalCellBackColor(workbook,headercell21Shop);
				    
				    headercell21Shop = (XSSFCell) headerrow21Shop.createCell((short) 2);
					headercell21Shop.setCellValue("");
					SetNormalCellBackColor(workbook,headercell21Shop);
					
					 headercell21Shop = (XSSFCell) headerrow21Shop.createCell((short) 3);
						headercell21Shop.setCellValue("");
						SetNormalCellBackColor(workbook,headercell21Shop);
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow22Shop = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell22Shop = (XSSFCell) headerrow22Shop.createCell((short) 0);  
			  		headercell22Shop = (XSSFCell) headerrow22Shop.createCell((short) 0);
					headercell22Shop.setCellValue("Shop Opening-Closing Time");
					SetNormalCellBackColor(workbook,headercell22Shop);
				    
				     
			  		headercell22Shop = (XSSFCell) headerrow22Shop.createCell((short) 1);
					headercell22Shop.setCellValue(ShopOpeningTime+" - "+ShopCloseTime);
					SetNormalCellBackColor(workbook,headercell22Shop);
				    
				    headercell22Shop = (XSSFCell) headerrow22Shop.createCell((short) 2);
					headercell22Shop.setCellValue("");
					SetNormalCellBackColor(workbook,headercell22Shop);
					
					headercell22Shop = (XSSFCell) headerrow22Shop.createCell((short) 3);
					headercell22Shop.setCellValue("");
					SetNormalCellBackColor(workbook,headercell22Shop);
					
					 
					
					RowCount=RowCount+1;
				    XSSFRow headerrow23Shop = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell23Shop = (XSSFCell) headerrow23Shop.createCell((short) 0);  
			  		headercell23Shop = (XSSFCell) headerrow23Shop.createCell((short) 0);
					headercell23Shop.setCellValue("Financial Service");
					SetNormalCellBackColor(workbook,headercell23Shop);
				    
				     
			  		headercell23Shop = (XSSFCell) headerrow23Shop.createCell((short) 1);
					headercell23Shop.setCellValue(FinancialService);
					SetNormalCellBackColor(workbook,headercell23Shop);
				    
				    headercell23Shop = (XSSFCell) headerrow23Shop.createCell((short) 2);
					headercell23Shop.setCellValue("");
					SetNormalCellBackColor(workbook,headercell23Shop);
					
					headercell23Shop = (XSSFCell) headerrow23Shop.createCell((short) 3);
					headercell23Shop.setCellValue("");
					SetNormalCellBackColor(workbook,headercell23Shop);
					
					
					
					
					 RowCount=RowCount+1;
				    XSSFRow headerrow24Shop = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell24Shop = (XSSFCell) headerrow24Shop.createCell((short) 0);  
			  		headercell24Shop = (XSSFCell) headerrow24Shop.createCell((short) 0);
					headercell24Shop.setCellValue("Outlet Selling Beverages in full cases?");
					SetNormalCellBackColor(workbook,headercell24Shop);
				    
				     
			  		headercell24Shop = (XSSFCell) headerrow24Shop.createCell((short) 1);
					headercell24Shop.setCellValue("SSRB: "+BeveragesSellingFullCasesSSRB+"% - PET: "+BeveragesSellingFullCasesPET +"% - Tetra: "+BeveragesSellingFullCasesTETRA +"% - CAN:"+BeveragesSellingFullCasesCAN+"%");
					SetNormalCellBackColor(workbook,headercell24Shop);
				    
				    headercell24Shop = (XSSFCell) headerrow24Shop.createCell((short) 2);
					headercell24Shop.setCellValue("");
					SetNormalCellBackColor(workbook,headercell24Shop);
					
					 headercell24Shop = (XSSFCell) headerrow24Shop.createCell((short) 3);
						headercell24Shop.setCellValue("");
						SetNormalCellBackColor(workbook,headercell24Shop);
						
					
					
					
					 RowCount=RowCount+1;
				    XSSFRow headerrow25 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell25 = (XSSFCell) headerrow25.createCell((short) 0);  
			  		headercell25 = (XSSFCell) headerrow25.createCell((short) 0);
					headercell25.setCellValue("Exclusivity Agreement");
					SetNormalCellBackColor(workbook,headercell25);
				    
				     
			  		headercell25 = (XSSFCell) headerrow25.createCell((short) 1);
					headercell25.setCellValue(ExclusivityAgreementPI+","+ExclusivityAgreementKO+","+ExclusivityAgreementGou+","+ExclusivityAgreementMezan+","+ExclusivityAgreementOther);
					SetNormalCellBackColor(workbook,headercell25);
				    
				    headercell25 = (XSSFCell) headerrow25.createCell((short) 2);
					headercell25.setCellValue("");
					SetNormalCellBackColor(workbook,headercell25);
					
					headercell25 = (XSSFCell) headerrow25.createCell((short) 3);
					headercell25.setCellValue("");
					SetNormalCellBackColor(workbook,headercell25);
					
					
					
					
					
					
					 RowCount=RowCount+1;
				    XSSFRow headerrow26 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell26 = (XSSFCell) headerrow26.createCell((short) 0);  
			  		headercell26 = (XSSFCell) headerrow26.createCell((short) 0);
					headercell26.setCellValue("Per Raw Case Discount - SSRB");
					SetNormalCellBackColor(workbook,headercell26);
				    
				     
			  		headercell26 = (XSSFCell) headerrow26.createCell((short) 1);
					headercell26.setCellValue(PerRawCaseDiscSSRB);
					SetNormalCellBackColor(workbook,headercell26);
				    
				    headercell26 = (XSSFCell) headerrow26.createCell((short) 2);
					headercell26.setCellValue("");
					SetNormalCellBackColor(workbook,headercell26);
					
					headercell26 = (XSSFCell) headerrow26.createCell((short) 3);
					headercell26.setCellValue("");
					SetNormalCellBackColor(workbook,headercell26);
					
					
					
					
					
					 RowCount=RowCount+1;
				    XSSFRow headerrow27 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell27 = (XSSFCell) headerrow27.createCell((short) 0);  
			  		headercell27 = (XSSFCell) headerrow27.createCell((short) 0);
					headercell27.setCellValue("Per Raw Case Discount - PET");
					SetNormalCellBackColor(workbook,headercell27);
				    
				     
			  		headercell27 = (XSSFCell) headerrow27.createCell((short) 1);
					headercell27.setCellValue(PerRawCaseDiscPET);
					SetNormalCellBackColor(workbook,headercell27);
				    
				    headercell27 = (XSSFCell) headerrow27.createCell((short) 2);
					headercell27.setCellValue("");
					SetNormalCellBackColor(workbook,headercell27);
					
					headercell27 = (XSSFCell) headerrow27.createCell((short) 3);
					headercell27.setCellValue("");
					SetNormalCellBackColor(workbook,headercell27);
					
					
					
					
					
					 RowCount=RowCount+1;
				    XSSFRow headerrow28 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell28 = (XSSFCell) headerrow28.createCell((short) 0);  
			  		headercell28 = (XSSFCell) headerrow28.createCell((short) 0);
					headercell28.setCellValue("Agreement Expiry Date");
					SetNormalCellBackColor(workbook,headercell28);
				    
				     
			  		headercell28 = (XSSFCell) headerrow28.createCell((short) 1);
					headercell28.setCellValue(AgreementExpDate);
					SetNormalCellBackColor(workbook,headercell28);
				    
				    headercell28 = (XSSFCell) headerrow28.createCell((short) 2);
					headercell28.setCellValue("");
					SetNormalCellBackColor(workbook,headercell28);
					
					headercell28 = (XSSFCell) headerrow28.createCell((short) 3);
					headercell28.setCellValue("");
					SetNormalCellBackColor(workbook,headercell28);
					
					
					
					
					
					 RowCount=RowCount+1;
				    XSSFRow headerrow29 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell29 = (XSSFCell) headerrow29.createCell((short) 0);  
			  		headercell29 = (XSSFCell) headerrow29.createCell((short) 0);
					headercell29.setCellValue("Agreement Type");
					SetNormalCellBackColor(workbook,headercell29);
				    
				     
			  		headercell29 = (XSSFCell) headerrow29.createCell((short) 1);
					headercell29.setCellValue(AgreementType);
					SetNormalCellBackColor(workbook,headercell29);
				    
				    headercell29 = (XSSFCell) headerrow29.createCell((short) 2);
					headercell29.setCellValue("");
					SetNormalCellBackColor(workbook,headercell29);
					
					 headercell29 = (XSSFCell) headerrow29.createCell((short) 3);
						headercell29.setCellValue("");
						SetNormalCellBackColor(workbook,headercell29);
					
					
					
					
					
					 RowCount=RowCount+1;
				    XSSFRow headerrow2110 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell2110 = (XSSFCell) headerrow2110.createCell((short) 0);  
			  		headercell2110 = (XSSFCell) headerrow2110.createCell((short) 0);
					headercell2110.setCellValue("Agreement Period");
					SetNormalCellBackColor(workbook,headercell2110);
				    
				     
			  		headercell2110 = (XSSFCell) headerrow2110.createCell((short) 1);
					headercell2110.setCellValue(AgreementPeriod);
					SetNormalCellBackColor(workbook,headercell2110);
				    
				    headercell2110 = (XSSFCell) headerrow2110.createCell((short) 2);
					headercell2110.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2110);
					
					headercell2110 = (XSSFCell) headerrow2110.createCell((short) 3);
					headercell2110.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2110);
					
					
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow2111 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell2111 = (XSSFCell) headerrow2111.createCell((short) 0);  
			  		headercell2111 = (XSSFCell) headerrow2111.createCell((short) 0);
					headercell2111.setCellValue("Agreement Type");
					SetNormalCellBackColor(workbook,headercell2110);
				    
				     
			  		headercell2111 = (XSSFCell) headerrow2111.createCell((short) 1);
					headercell2111.setCellValue(AgreementType2);
					SetNormalCellBackColor(workbook,headercell2111);
				    
				    headercell2111 = (XSSFCell) headerrow2111.createCell((short) 2);
					headercell2111.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2111);
					
					headercell2111 = (XSSFCell) headerrow2111.createCell((short) 3);
					headercell2111.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2111);
					
					
					
					
				      
				     
				      
				      RowCount = RowCount+1;
				      
				      XSSFRow headerrow2 = spreadsheet.createRow((short) RowCount+1);	      
				      XSSFCell headercell2= (XSSFCell) headerrow2.createCell((short) 0);
				     
				      headercell2.setCellValue("Discount Agreement PI");
				      SetNormalCellBackColor(workbook,headercell2);
				      spreadsheet.addMergedRegion(new CellRangeAddress(
				    		  RowCount+1, //first row (0-based)
				    		  RowCount+1, //last row (0-based)
				    		  0, //first column (0-based)
				    	      3 //last column (0-based)
				    	      ));
					
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow2112 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell2112 = (XSSFCell) headerrow2112.createCell((short) 0);  
			  		headercell2112 = (XSSFCell) headerrow2112.createCell((short) 0);
					headercell2112.setCellValue("Per Raw Case Discount - SSRB");
					SetNormalCellBackColor(workbook,headercell2112);
				    
				     
			  		headercell2112 = (XSSFCell) headerrow2112.createCell((short) 1);
					headercell2112.setCellValue(PerRawCaseDiscSSRBPI);
					SetNormalCellBackColor(workbook,headercell2112);
				    
				    headercell2112 = (XSSFCell) headerrow2112.createCell((short) 2);
					headercell2112.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2112);
					
					headercell2112 = (XSSFCell) headerrow2112.createCell((short) 3);
					headercell2112.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2112);
					
					
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow2113 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell2113 = (XSSFCell) headerrow2113.createCell((short) 0);  
			  		headercell2113 = (XSSFCell) headerrow2113.createCell((short) 0);
					headercell2113.setCellValue("Per Raw Case Discount - PET");
					SetNormalCellBackColor(workbook,headercell2113);
				    
				     
			  		headercell2113 = (XSSFCell) headerrow2113.createCell((short) 1);
					headercell2113.setCellValue(PerRawCaseDiscPETPI);
					SetNormalCellBackColor(workbook,headercell2113);
				    
				    headercell2113 = (XSSFCell) headerrow2113.createCell((short) 2);
					headercell2113.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2113);
					
					headercell2113 = (XSSFCell) headerrow2113.createCell((short) 3);
					headercell2113.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2113);
				     
				     
				    
			      
			      
					RowCount=RowCount+1;
				    XSSFRow headerrow2114 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell2114 = (XSSFCell) headerrow2114.createCell((short) 0);  
			  		headercell2114 = (XSSFCell) headerrow2114.createCell((short) 0);
					headercell2114.setCellValue("Agreement Expiry Date");
					SetNormalCellBackColor(workbook,headercell2114);
				    
				     
			  		headercell2114 = (XSSFCell) headerrow2114.createCell((short) 1);
					headercell2114.setCellValue(AgreementExpDatePI);
					SetNormalCellBackColor(workbook,headercell2114);
				    
				    headercell2114 = (XSSFCell) headerrow2114.createCell((short) 2);
					headercell2114.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2114);
					
					headercell2114 = (XSSFCell) headerrow2114.createCell((short) 3);
					headercell2114.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2114);
					
					
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow2115 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);  
			  		headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);
					headercell2115.setCellValue("Agreement Type PI");
					SetNormalCellBackColor(workbook,headercell2115);
				    
				     
			  		headercell2115 = (XSSFCell) headerrow2115.createCell((short) 1);
					headercell2115.setCellValue(AgreementTypePI);
					SetNormalCellBackColor(workbook,headercell2115);
				    
				    headercell2115 = (XSSFCell) headerrow2115.createCell((short) 2);
					headercell2115.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2115);
					
					headercell2115 = (XSSFCell) headerrow2115.createCell((short) 3);
					headercell2115.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2115);
				
					
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow2116 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell2116 = (XSSFCell) headerrow2116.createCell((short) 0);  
			  		headercell2116 = (XSSFCell) headerrow2116.createCell((short) 0);
					headercell2116.setCellValue("Agreement Period");
					SetNormalCellBackColor(workbook,headercell2116);
				    
				     
			  		headercell2116 = (XSSFCell) headerrow2116.createCell((short) 1);
					headercell2116.setCellValue(AgreementPeriodPI);
					SetNormalCellBackColor(workbook,headercell2116);
				    
				    headercell2116 = (XSSFCell) headerrow2116.createCell((short) 2);
					headercell2116.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2116);
					
					headercell2116 = (XSSFCell) headerrow2116.createCell((short) 3);
					headercell2116.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2116);
				 
						
						
					   
					    
					    
					RowCount=RowCount+1;
				    XSSFRow headerrow2117 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell2117 = (XSSFCell) headerrow2117.createCell((short) 0);  
			  		headercell2117 = (XSSFCell) headerrow2117.createCell((short) 0);
					headercell2117.setCellValue("Agreement Type");
					SetNormalCellBackColor(workbook,headercell2117);
				    
				     
			  		headercell2117 = (XSSFCell) headerrow2117.createCell((short) 1);
					headercell2117.setCellValue(AgreementType2PI);
					SetNormalCellBackColor(workbook,headercell2117);
				    
				    headercell2117 = (XSSFCell) headerrow2117.createCell((short) 2);
					headercell2117.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2117);
					
					 headercell2117 = (XSSFCell) headerrow2117.createCell((short) 3);
						headercell2117.setCellValue("");
						SetNormalCellBackColor(workbook,headercell2117);
					
					
					RowCount=RowCount+1;
					XSSFRow headerrow22 = spreadsheet.createRow((short) RowCount+1);	      
				      XSSFCell headercell22= (XSSFCell) headerrow22.createCell((short) 0);
				     
				      headercell22.setCellValue("Discount Agreement KO");
				      SetNormalCellBackColor(workbook,headercell22);
				      spreadsheet.addMergedRegion(new CellRangeAddress(
				    		  RowCount+1, //first row (0-based)
				    		  RowCount+1, //last row (0-based)
				    		  0, //first column (0-based)
				    	      3 //last column (0-based)
				    	      ));
					
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow2118 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell2118 = (XSSFCell) headerrow2118.createCell((short) 0);  
			  		headercell2118 = (XSSFCell) headerrow2118.createCell((short) 0);
					headercell2118.setCellValue("Per Raw Case Discount - SSRB");
					SetNormalCellBackColor(workbook,headercell2118);
				    
				     
			  		headercell2118 = (XSSFCell) headerrow2118.createCell((short) 1);
					headercell2118.setCellValue(PerRawCaseDiscSSRBKO);
					SetNormalCellBackColor(workbook,headercell2118);
				    
				    headercell2118 = (XSSFCell) headerrow2118.createCell((short) 2);
					headercell2118.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2118);
					
					 headercell2118 = (XSSFCell) headerrow2118.createCell((short) 3);
						headercell2118.setCellValue("");
						SetNormalCellBackColor(workbook,headercell2118);
					
					
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow2119 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell2119 = (XSSFCell) headerrow2119.createCell((short) 0);  
			  		headercell2119 = (XSSFCell) headerrow2119.createCell((short) 0);
					headercell2119.setCellValue("Per Raw Case Discount - PET");
					SetNormalCellBackColor(workbook,headercell2119);
				    
				     
			  		headercell2119 = (XSSFCell) headerrow2119.createCell((short) 1);
					headercell2119.setCellValue(PerRawCaseDiscPETKO);
					SetNormalCellBackColor(workbook,headercell2119);
				    
				    headercell2119 = (XSSFCell) headerrow2119.createCell((short) 2);
					headercell2119.setCellValue("");
					SetNormalCellBackColor(workbook,headercell2119);
					
					  headercell2119 = (XSSFCell) headerrow2119.createCell((short) 3);
						headercell2119.setCellValue("");
						SetNormalCellBackColor(workbook,headercell2119);
					
					
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow21110 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell21110 = (XSSFCell) headerrow21110.createCell((short) 0);  
			  		headercell21110 = (XSSFCell) headerrow21110.createCell((short) 0);
					headercell21110.setCellValue("Agreement Expiry Date");
					SetNormalCellBackColor(workbook,headercell21110);
				    
				     
			  		headercell21110 = (XSSFCell) headerrow21110.createCell((short) 1);
					headercell21110.setCellValue(AgreementExpDateKO);
					SetNormalCellBackColor(workbook,headercell21110);
				    
				    headercell21110 = (XSSFCell) headerrow21110.createCell((short) 2);
					headercell21110.setCellValue("");
					SetNormalCellBackColor(workbook,headercell21110);
					
					 headercell21110 = (XSSFCell) headerrow21110.createCell((short) 3);
						headercell21110.setCellValue("");
						SetNormalCellBackColor(workbook,headercell21110);
					
					
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow21111 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell21111 = (XSSFCell) headerrow21111.createCell((short) 0);  
			  		headercell21111 = (XSSFCell) headerrow21111.createCell((short) 0);
					headercell21111.setCellValue("Agreement Type KO");
					SetNormalCellBackColor(workbook,headercell21111);
				    
				     
			  		headercell21111 = (XSSFCell) headerrow21111.createCell((short) 1);
					headercell21111.setCellValue(AgreementTypeKO);
					SetNormalCellBackColor(workbook,headercell21111);
				    
				    headercell21111 = (XSSFCell) headerrow21111.createCell((short) 2);
					headercell21111.setCellValue("");
					SetNormalCellBackColor(workbook,headercell21111);
					
					 headercell21111 = (XSSFCell) headerrow21111.createCell((short) 3);
						headercell21111.setCellValue("");
						SetNormalCellBackColor(workbook,headercell21111);
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow21112 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell21112 = (XSSFCell) headerrow21112.createCell((short) 0);  
			  		headercell21112 = (XSSFCell) headerrow21112.createCell((short) 0);
					headercell21112.setCellValue("Agreement Period");
					SetNormalCellBackColor(workbook,headercell21112);
				    
				     
			  		headercell21112 = (XSSFCell) headerrow21112.createCell((short) 1);
					headercell21112.setCellValue(AgreementPeriodKO);
					SetNormalCellBackColor(workbook,headercell21112);
				    
				    headercell21112 = (XSSFCell) headerrow21112.createCell((short) 2);
					headercell21112.setCellValue("");
					SetNormalCellBackColor(workbook,headercell21112);
					
					headercell21112 = (XSSFCell) headerrow21112.createCell((short) 3);
					headercell21112.setCellValue("");
					SetNormalCellBackColor(workbook,headercell21112);
					
					
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow21113 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell21113 = (XSSFCell) headerrow21113.createCell((short) 0);  
			  		headercell21113 = (XSSFCell) headerrow21113.createCell((short) 0);
					headercell21113.setCellValue("Agreement Type");
					SetNormalCellBackColor(workbook,headercell21113);
				    
				     
			  		headercell21113 = (XSSFCell) headerrow21113.createCell((short) 1);
					headercell21113.setCellValue(AgreementType2KO);
					SetNormalCellBackColor(workbook,headercell21113);
				    
				    headercell21113 = (XSSFCell) headerrow21113.createCell((short) 2);
					headercell21113.setCellValue("");
					SetNormalCellBackColor(workbook,headercell21113);
					
					headercell21113 = (XSSFCell) headerrow21113.createCell((short) 3);
					headercell21113.setCellValue("");
					SetNormalCellBackColor(workbook,headercell21113);
					
					
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow21114 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell21114 = (XSSFCell) headerrow21114.createCell((short) 0);  
			  		headercell21114 = (XSSFCell) headerrow21114.createCell((short) 0);
					headercell21114.setCellValue("If Partially close, then timing");
					SetNormalCellBackColor(workbook,headercell21114);
				    
				     
			  		headercell21114 = (XSSFCell) headerrow21114.createCell((short) 1);
					headercell21114.setCellValue(PartialTiming);
					SetNormalCellBackColor(workbook,headercell21114);
				    
				    headercell21114 = (XSSFCell) headerrow21114.createCell((short) 2);
					headercell21114.setCellValue("");
					SetNormalCellBackColor(workbook,headercell21114);
					
					
					headercell21114 = (XSSFCell) headerrow21114.createCell((short) 3);
					headercell21114.setCellValue("");
					SetNormalCellBackColor(workbook,headercell21114);
					
					
					RowCount=RowCount+1;
					XSSFRow headerrow23 = spreadsheet.createRow((short) RowCount+1);	      
				      XSSFCell headercell23= (XSSFCell) headerrow23.createCell((short) 0);
				     
				      headercell23.setCellValue("Supplied By KO");
				      SetNormalCellBackColor(workbook,headercell23);
				      spreadsheet.addMergedRegion(new CellRangeAddress(
				    		  RowCount+1, //first row (0-based)
				    		  RowCount+1, //last row (0-based)
				    		  0, //first column (0-based)
				    	      3 //last column (0-based)
				    	      ));
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow21115 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell21115 = (XSSFCell) headerrow21115.createCell((short) 0);  
			  		headercell21115 = (XSSFCell) headerrow21115.createCell((short) 0);
					headercell21115.setCellValue("Distributor");
					SetNormalCellBackColor(workbook,headercell21115);
				    
				     
			  		headercell21115 = (XSSFCell) headerrow21115.createCell((short) 1);
					headercell21115.setCellValue(SuppliedByDistributorKO);
					SetNormalCellBackColor(workbook,headercell21115);
				    
				    headercell21115 = (XSSFCell) headerrow21115.createCell((short) 2);
					headercell21115.setCellValue("");
					SetNormalCellBackColor(workbook,headercell21115);
					
					headercell21115 = (XSSFCell) headerrow21115.createCell((short) 3);
					headercell21115.setCellValue("");
					SetNormalCellBackColor(workbook,headercell21115);
					
					
					
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow21117 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell21117 = (XSSFCell) headerrow21117.createCell((short) 0);  
			  		headercell21117 = (XSSFCell) headerrow21117.createCell((short) 0);
					headercell21117.setCellValue("Wholeseller");
					SetNormalCellBackColor(workbook,headercell21117);
				    
				     
			  		headercell21117 = (XSSFCell) headerrow21117.createCell((short) 1);
					headercell21117.setCellValue(SuppliedByWholeSellerKO);
					SetNormalCellBackColor(workbook,headercell21117);
				    
				    headercell21117 = (XSSFCell) headerrow21117.createCell((short) 2);
					headercell21117.setCellValue("");
					SetNormalCellBackColor(workbook,headercell21117);
					
					 headercell21117 = (XSSFCell) headerrow21117.createCell((short) 3);
						headercell21117.setCellValue("");
						SetNormalCellBackColor(workbook,headercell21117);
					
					
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow21118 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell21118 = (XSSFCell) headerrow21118.createCell((short) 0);  
			  		headercell21118 = (XSSFCell) headerrow21118.createCell((short) 0);
					headercell21118.setCellValue("Mobiler");
					SetNormalCellBackColor(workbook,headercell21118);
				    
				     
			  		headercell21118 = (XSSFCell) headerrow21118.createCell((short) 1);
					headercell21118.setCellValue(SuppliedByMobilerKO);
					SetNormalCellBackColor(workbook,headercell21118);
				    
				    headercell21118 = (XSSFCell) headerrow21118.createCell((short) 2);
					headercell21118.setCellValue("");
					SetNormalCellBackColor(workbook,headercell21118);
					
					headercell21118 = (XSSFCell) headerrow21118.createCell((short) 3);
					headercell21118.setCellValue("");
					SetNormalCellBackColor(workbook,headercell21118);
					
					
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow21119 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell21119 = (XSSFCell) headerrow21119.createCell((short) 0);  
			  		headercell21119 = (XSSFCell) headerrow21119.createCell((short) 0);
					headercell21119.setCellValue("Dealer");
					SetNormalCellBackColor(workbook,headercell21119);
				    
				     
			  		headercell21119 = (XSSFCell) headerrow21119.createCell((short) 1);
					headercell21119.setCellValue(SuppliedByDealerKO);
					SetNormalCellBackColor(workbook,headercell21119);
				    
				    headercell21119 = (XSSFCell) headerrow21119.createCell((short) 2);
					headercell21119.setCellValue("");
					SetNormalCellBackColor(workbook,headercell21119);
					
					headercell21119 = (XSSFCell) headerrow21119.createCell((short) 3);
					headercell21119.setCellValue("");
					SetNormalCellBackColor(workbook,headercell21119);
					
					
					
					RowCount=RowCount+1;
					XSSFRow headerrow24 = spreadsheet.createRow((short) RowCount+1);	      
				      XSSFCell headercell24= (XSSFCell) headerrow24.createCell((short) 0);
				     
				      headercell24.setCellValue("Supplied By PI");
				      SetNormalCellBackColor(workbook,headercell24);
				      spreadsheet.addMergedRegion(new CellRangeAddress(
				    		  RowCount+1, //first row (0-based)
				    		  RowCount+1, //last row (0-based)
				    		  0, //first column (0-based)
				    	      3 //last column (0-based)
				    	      ));
					
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow211120 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell211120 = (XSSFCell) headerrow211120.createCell((short) 0);  
			  		headercell211120 = (XSSFCell) headerrow211120.createCell((short) 0);
					headercell211120.setCellValue("Distributor");
					SetNormalCellBackColor(workbook,headercell211120);
				    
				     
			  		headercell211120 = (XSSFCell) headerrow211120.createCell((short) 1);
					headercell211120.setCellValue(SuppliedByDistributorPI);
					SetNormalCellBackColor(workbook,headercell211120);
				    
				    headercell211120 = (XSSFCell) headerrow211120.createCell((short) 2);
					headercell211120.setCellValue("");
					SetNormalCellBackColor(workbook,headercell211120);
					
					 headercell211120 = (XSSFCell) headerrow211120.createCell((short) 3);
						headercell211120.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211120);
					
					
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow211121 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell211121 = (XSSFCell) headerrow211121.createCell((short) 0);  
			  		headercell211121 = (XSSFCell) headerrow211121.createCell((short) 0);
					headercell211121.setCellValue("Wholeseller");
					SetNormalCellBackColor(workbook,headercell211121);
				    
				     
			  		headercell211121 = (XSSFCell) headerrow211121.createCell((short) 1);
					headercell211121.setCellValue(SuppliedByWholeSellerPI);
					SetNormalCellBackColor(workbook,headercell211121);
				    
				    headercell211121 = (XSSFCell) headerrow211121.createCell((short) 2);
					headercell211121.setCellValue("");
					SetNormalCellBackColor(workbook,headercell211121);
					
					 headercell211121 = (XSSFCell) headerrow211121.createCell((short) 3);
						headercell211121.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211121);
					
					
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow211122 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell211122 = (XSSFCell) headerrow211122.createCell((short) 0);  
			  		headercell211122 = (XSSFCell) headerrow211122.createCell((short) 0);
					headercell211122.setCellValue("Mobiler");
					SetNormalCellBackColor(workbook,headercell211122);
				    
				     
			  		headercell211122 = (XSSFCell) headerrow211122.createCell((short) 1);
					headercell211122.setCellValue(SuppliedByMobilerPI);
					SetNormalCellBackColor(workbook,headercell211122);
				    
				    headercell211122 = (XSSFCell) headerrow211122.createCell((short) 2);
					headercell211122.setCellValue("");
					SetNormalCellBackColor(workbook,headercell211122);
					
					headercell211122 = (XSSFCell) headerrow211122.createCell((short) 3);
					headercell211122.setCellValue("");
					SetNormalCellBackColor(workbook,headercell211122);
					
					
					
					
					
					RowCount=RowCount+1;
				    XSSFRow headerrow211123 = spreadsheet.createRow((short) RowCount+1);        
			  		
			  		XSSFCell headercell211123 = (XSSFCell) headerrow211123.createCell((short) 0);  
			  		headercell211123 = (XSSFCell) headerrow211123.createCell((short) 0);
					headercell211123.setCellValue("Dealer");
					SetNormalCellBackColor(workbook,headercell211123);
				    
				     
			  		headercell211123 = (XSSFCell) headerrow211123.createCell((short) 1);
					headercell211123.setCellValue(SuppliedByDealerPI);
					SetNormalCellBackColor(workbook,headercell211123);
				    
				    headercell211123 = (XSSFCell) headerrow211123.createCell((short) 2);
					headercell211123.setCellValue("");
					SetNormalCellBackColor(workbook,headercell211123);
					
					headercell211123 = (XSSFCell) headerrow211123.createCell((short) 3);
					headercell211123.setCellValue("");
					SetNormalCellBackColor(workbook,headercell211123);
					
					
					
					
					
					
				}
				
				if(1==1){
				      
				      /////// Status 
							
							//2nd row header
						      
						     
						      
						      RowCount = RowCount+1;
						      
						      XSSFRow headerrow = spreadsheet.createRow((short) RowCount+1);	      
						      XSSFCell headercell = (XSSFCell) headerrow.createCell((short) 0);
						     
						      headercell.setCellValue("Status");
						      Set2ndHeaderBackColor(workbook,headercell);
						      spreadsheet.addMergedRegion(new CellRangeAddress(
						    		  RowCount+1, //first row (0-based)
						    		  RowCount+1, //last row (0-based)
						    		  0, //first column (0-based)
						    	      3 //last column (0-based)
						    	      ));
				      
						      
						      RowCount = RowCount+1;     
						      
				      	XSSFRow headerrow2 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell2 = (XSSFCell) headerrow2.createCell((short) 0);  
				  		headercell2 = (XSSFCell) headerrow2.createCell((short) 0);
						headercell2.setCellValue("Supply Frequency");
						SetNormalCellBackColor(workbook,headercell2);
					    
					     
				  		headercell2 = (XSSFCell) headerrow2.createCell((short) 1);
						headercell2.setCellValue(SupplyFrequency);
						SetNormalCellBackColor(workbook,headercell2);
					    
					    headercell2 = (XSSFCell) headerrow2.createCell((short) 2);
						headercell2.setCellValue("");
						SetNormalCellBackColor(workbook,headercell2);
						
						 headercell2 = (XSSFCell) headerrow2.createCell((short) 3);
							headercell2.setCellValue("");
							SetNormalCellBackColor(workbook,headercell2);
					    
					    
					    
					    RowCount=RowCount+1;
					    XSSFRow headerrow21 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell21 = (XSSFCell) headerrow21.createCell((short) 0);  
				  		headercell21 = (XSSFCell) headerrow21.createCell((short) 0);
						headercell21.setCellValue("Supply Frequency KO");
						SetNormalCellBackColor(workbook,headercell21);
					    
					     
				  		headercell21 = (XSSFCell) headerrow21.createCell((short) 1);
						headercell21.setCellValue(SupplyFrequencyKO);
						SetNormalCellBackColor(workbook,headercell21);
					    
					    headercell21 = (XSSFCell) headerrow21.createCell((short) 2);
						headercell21.setCellValue("");
						SetNormalCellBackColor(workbook,headercell21);
						
						headercell21 = (XSSFCell) headerrow21.createCell((short) 3);
						headercell21.setCellValue("");
						SetNormalCellBackColor(workbook,headercell21);
						
						
						RowCount=RowCount+1;
					    XSSFRow headerrow22 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell22 = (XSSFCell) headerrow22.createCell((short) 0);  
				  		headercell22 = (XSSFCell) headerrow22.createCell((short) 0);
						headercell22.setCellValue("Supply Frequency Gormet");
						SetNormalCellBackColor(workbook,headercell22);
					    
					     
				  		headercell22 = (XSSFCell) headerrow22.createCell((short) 1);
						headercell22.setCellValue(SupplyFrequencyGormet);
						SetNormalCellBackColor(workbook,headercell22);
					    
					    headercell22 = (XSSFCell) headerrow22.createCell((short) 2);
						headercell22.setCellValue("");
						SetNormalCellBackColor(workbook,headercell22);
						
						headercell22 = (XSSFCell) headerrow22.createCell((short) 3);
						headercell22.setCellValue("");
						SetNormalCellBackColor(workbook,headercell22);
						
						 
						
						RowCount=RowCount+1;
					    XSSFRow headerrow23 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell23 = (XSSFCell) headerrow23.createCell((short) 0);  
				  		headercell23 = (XSSFCell) headerrow23.createCell((short) 0);
						headercell23.setCellValue("Supply Frequency Cola Next");
						SetNormalCellBackColor(workbook,headercell23);
					    
					     
				  		headercell23 = (XSSFCell) headerrow23.createCell((short) 1);
						headercell23.setCellValue(SupplyFrequencyColaNext);
						SetNormalCellBackColor(workbook,headercell23);
					    
					    headercell23 = (XSSFCell) headerrow23.createCell((short) 2);
						headercell23.setCellValue("");
						SetNormalCellBackColor(workbook,headercell23);
						
						headercell23 = (XSSFCell) headerrow23.createCell((short) 3);
						headercell23.setCellValue("");
						SetNormalCellBackColor(workbook,headercell23);
						
						
						
						
						 RowCount=RowCount+1;
					    XSSFRow headerrow24 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell24 = (XSSFCell) headerrow24.createCell((short) 0);  
				  		headercell24 = (XSSFCell) headerrow24.createCell((short) 0);
						headercell24.setCellValue("Covered by Research Company?");
						SetNormalCellBackColor(workbook,headercell24);
					    
					     
				  		headercell24 = (XSSFCell) headerrow24.createCell((short) 1);
						headercell24.setCellValue(IsCoveredByRCompnayDuration);
						SetNormalCellBackColor(workbook,headercell24);
					    
					    headercell24 = (XSSFCell) headerrow24.createCell((short) 2);
						headercell24.setCellValue("");
						SetNormalCellBackColor(workbook,headercell24);
						
						headercell24 = (XSSFCell) headerrow24.createCell((short) 3);
						headercell24.setCellValue("");
						SetNormalCellBackColor(workbook,headercell24);
						
						
						
						
						 RowCount=RowCount+1;
					    XSSFRow headerrow25 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell25 = (XSSFCell) headerrow25.createCell((short) 0);  
				  		headercell25 = (XSSFCell) headerrow25.createCell((short) 0);
						headercell25.setCellValue("Stock Storage Location");
						SetNormalCellBackColor(workbook,headercell25);
					    
					     
				  		headercell25 = (XSSFCell) headerrow25.createCell((short) 1);
						headercell25.setCellValue(StockStorageLocation1+","+StockStorageLocation2+","+StockStorageLocation3+","+StockStorageLocation4);
						SetNormalCellBackColor(workbook,headercell25);
					    
					    headercell25 = (XSSFCell) headerrow25.createCell((short) 2);
						headercell25.setCellValue("");
						SetNormalCellBackColor(workbook,headercell25);
						
						headercell25 = (XSSFCell) headerrow25.createCell((short) 3);
						headercell25.setCellValue("");
						SetNormalCellBackColor(workbook,headercell25);
						
						
						
						
						
						
						 RowCount=RowCount+1;
					    XSSFRow headerrow26 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell26 = (XSSFCell) headerrow26.createCell((short) 0);  
				  		headercell26 = (XSSFCell) headerrow26.createCell((short) 0);
						headercell26.setCellValue("Cash Machine");
						SetNormalCellBackColor(workbook,headercell26);
					    
					     
				  		headercell26 = (XSSFCell) headerrow26.createCell((short) 1);
						headercell26.setCellValue(CashMachineQuantity);
						SetNormalCellBackColor(workbook,headercell26);
					    
					    headercell26 = (XSSFCell) headerrow26.createCell((short) 2);
						headercell26.setCellValue("");
						SetNormalCellBackColor(workbook,headercell26);
						
						 headercell26 = (XSSFCell) headerrow26.createCell((short) 3);
							headercell26.setCellValue("");
							SetNormalCellBackColor(workbook,headercell26);
						
						
						
						
						
						 RowCount=RowCount+1;
					    XSSFRow headerrow27 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell27 = (XSSFCell) headerrow27.createCell((short) 0);  
				  		headercell27 = (XSSFCell) headerrow27.createCell((short) 0);
						headercell27.setCellValue("Shop Establishment History");
						SetNormalCellBackColor(workbook,headercell27);
					    
					     
				  		headercell27 = (XSSFCell) headerrow27.createCell((short) 1);
						headercell27.setCellValue(ShopHistory);
						SetNormalCellBackColor(workbook,headercell27);
					    
					    headercell27 = (XSSFCell) headerrow27.createCell((short) 2);
						headercell27.setCellValue("");
						SetNormalCellBackColor(workbook,headercell27);
						
						 headercell27 = (XSSFCell) headerrow27.createCell((short) 3);
							headercell27.setCellValue("");
							SetNormalCellBackColor(workbook,headercell27);
						
						
						
						
						
						 RowCount=RowCount+1;
					    XSSFRow headerrow28 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell28 = (XSSFCell) headerrow28.createCell((short) 0);  
				  		headercell28 = (XSSFCell) headerrow28.createCell((short) 0);
						headercell28.setCellValue("Business Structure");
						SetNormalCellBackColor(workbook,headercell28);
					    
					     
				  		headercell28 = (XSSFCell) headerrow28.createCell((short) 1);
						headercell28.setCellValue(BusinessStructure);
						SetNormalCellBackColor(workbook,headercell28);
					    
					    headercell28 = (XSSFCell) headerrow28.createCell((short) 2);
						headercell28.setCellValue("");
						SetNormalCellBackColor(workbook,headercell28);
						
						 headercell28 = (XSSFCell) headerrow28.createCell((short) 3);
							headercell28.setCellValue("");
							SetNormalCellBackColor(workbook,headercell28);
						
						
						
						
						
						 RowCount=RowCount+1;
					    XSSFRow headerrow29 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell29 = (XSSFCell) headerrow29.createCell((short) 0);  
				  		headercell29 = (XSSFCell) headerrow29.createCell((short) 0);
						headercell29.setCellValue("Shop Type");
						SetNormalCellBackColor(workbook,headercell29);
					    
					     
				  		headercell29 = (XSSFCell) headerrow29.createCell((short) 1);
						headercell29.setCellValue(SShopType);
						SetNormalCellBackColor(workbook,headercell29);
					    
					    headercell29 = (XSSFCell) headerrow29.createCell((short) 2);
						headercell29.setCellValue("");
						SetNormalCellBackColor(workbook,headercell29);
						
						headercell29 = (XSSFCell) headerrow29.createCell((short) 3);
						headercell29.setCellValue("");
						SetNormalCellBackColor(workbook,headercell29);
						
						
						
						
						
						 RowCount=RowCount+1;
					    XSSFRow headerrow2110 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell2110 = (XSSFCell) headerrow2110.createCell((short) 0);  
				  		headercell2110 = (XSSFCell) headerrow2110.createCell((short) 0);
						headercell2110.setCellValue("Wholesale %");
						SetNormalCellBackColor(workbook,headercell2110);
					    
					     
				  		headercell2110 = (XSSFCell) headerrow2110.createCell((short) 1);
						headercell2110.setCellValue(Wholesale);
						SetNormalCellBackColor(workbook,headercell2110);
					    
					    headercell2110 = (XSSFCell) headerrow2110.createCell((short) 2);
						headercell2110.setCellValue("");
						SetNormalCellBackColor(workbook,headercell2110);
						
						 headercell2110 = (XSSFCell) headerrow2110.createCell((short) 3);
							headercell2110.setCellValue("");
							SetNormalCellBackColor(workbook,headercell2110);
						
						
						
						
						
						RowCount=RowCount+1;
					    XSSFRow headerrow2111 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell2111 = (XSSFCell) headerrow2111.createCell((short) 0);  
				  		headercell2111 = (XSSFCell) headerrow2111.createCell((short) 0);
						headercell2111.setCellValue("Retailer %");
						SetNormalCellBackColor(workbook,headercell2110);
					    
					     
				  		headercell2111 = (XSSFCell) headerrow2111.createCell((short) 1);
						headercell2111.setCellValue(Retailer);
						SetNormalCellBackColor(workbook,headercell2111);
					    
					    headercell2111 = (XSSFCell) headerrow2111.createCell((short) 2);
						headercell2111.setCellValue("");
						SetNormalCellBackColor(workbook,headercell2111);
						
						 headercell2111 = (XSSFCell) headerrow2111.createCell((short) 3);
							headercell2111.setCellValue("");
							SetNormalCellBackColor(workbook,headercell2111);
						
						
						
						
						
						RowCount=RowCount+1;
					    XSSFRow headerrow2112 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell2112 = (XSSFCell) headerrow2112.createCell((short) 0);  
				  		headercell2112 = (XSSFCell) headerrow2112.createCell((short) 0);
						headercell2112.setCellValue("PI CSD");
						SetNormalCellBackColor(workbook,headercell2112);
					    
					     
				  		headercell2112 = (XSSFCell) headerrow2112.createCell((short) 1);
						headercell2112.setCellValue(PICSD);
						SetNormalCellBackColor(workbook,headercell2112);
					    
					    headercell2112 = (XSSFCell) headerrow2112.createCell((short) 2);
						headercell2112.setCellValue("");
						SetNormalCellBackColor(workbook,headercell2112);
						
						 headercell2112 = (XSSFCell) headerrow2112.createCell((short) 3);
							headercell2112.setCellValue("");
							SetNormalCellBackColor(workbook,headercell2112);
						
						
						
						
						
						RowCount=RowCount+1;
					    XSSFRow headerrow2113 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell2113 = (XSSFCell) headerrow2113.createCell((short) 0);  
				  		headercell2113 = (XSSFCell) headerrow2113.createCell((short) 0);
						headercell2113.setCellValue("PI Sting");
						SetNormalCellBackColor(workbook,headercell2113);
					    
					     
				  		headercell2113 = (XSSFCell) headerrow2113.createCell((short) 1);
						headercell2113.setCellValue(PISting);
						SetNormalCellBackColor(workbook,headercell2113);
					    
					    headercell2113 = (XSSFCell) headerrow2113.createCell((short) 2);
						headercell2113.setCellValue("");
						SetNormalCellBackColor(workbook,headercell2113);
						
						 headercell2113 = (XSSFCell) headerrow2113.createCell((short) 3);
							headercell2113.setCellValue("");
							SetNormalCellBackColor(workbook,headercell2113);
					     
					     
					    //v
						
						
						
						RowCount=RowCount+1;
					    XSSFRow headerrow2115 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);  
				  		headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);
						headercell2115.setCellValue("KO");
						SetNormalCellBackColor(workbook,headercell2115);
					    
					     
				  		headercell2115 = (XSSFCell) headerrow2115.createCell((short) 1);
						headercell2115.setCellValue(KO);
						SetNormalCellBackColor(workbook,headercell2115);
					    
					    headercell2115 = (XSSFCell) headerrow2115.createCell((short) 2);
						headercell2115.setCellValue("");
						SetNormalCellBackColor(workbook,headercell2115);
						
						
						headercell2115 = (XSSFCell) headerrow2115.createCell((short) 3);
						headercell2115.setCellValue("");
						SetNormalCellBackColor(workbook,headercell2115);
					
						
						}
				
				
				if(1==1){
					//Cooler Type
					
					 RowCount = RowCount+1;
				      
				      XSSFRow headerrow = spreadsheet.createRow((short) RowCount+1);	      
				      XSSFCell headercell = (XSSFCell) headerrow.createCell((short) 0);
				     
				      headercell.setCellValue("Cooler Type");
				      Set2ndHeaderBackColor(workbook,headercell);
				      spreadsheet.addMergedRegion(new CellRangeAddress(
				    		  RowCount+1, //first row (0-based)
				    		  RowCount+1, //last row (0-based)
				    		  0, //first column (0-based)
				    	      3 //last column (0-based)
				    	      ));
				      
				      
				      
				      String CoolerTypeCompnayName="";
						String CoolerTypeTypeName="";
						
						ResultSet rs2 = s.executeQuery("SELECT * FROM mrd_census_cooler_types where id="+TableMainID);
						while(rs2.next()){
						
							if(rs2.getInt("cooler_type_company_id")==1){
								CoolerTypeCompnayName="PI";
							}else if(rs2.getInt("cooler_type_company_id")==2){
								CoolerTypeCompnayName="KO";
							}else if(rs2.getInt("cooler_type_company_id")==3){
								CoolerTypeCompnayName="NESTLE";
							}else if(rs2.getInt("cooler_type_company_id")==4){
								CoolerTypeCompnayName="REDBULL";
							}else if(rs2.getInt("cooler_type_company_id")==5){
								CoolerTypeCompnayName="ICE CREAM";
							}else if(rs2.getInt("cooler_type_company_id")==6){
								CoolerTypeCompnayName="PERSONAL";
							}else if(rs2.getInt("cooler_type_company_id")==7){
								CoolerTypeCompnayName="OTHERS";
							}
							
							
							if(rs2.getInt("cooler_type_type_id")==1){
								CoolerTypeTypeName="V/C 250SAX";
							}else if(rs2.getInt("cooler_type_type_id")==2){
								CoolerTypeTypeName="V/C 400SAX";
							}else if(rs2.getInt("cooler_type_type_id")==3){
								CoolerTypeTypeName="V/C 550SAX";
							}else if(rs2.getInt("cooler_type_type_id")==4){
								CoolerTypeTypeName="V/C 1000SAX";
							}else if(rs2.getInt("cooler_type_type_id")==5){
								CoolerTypeTypeName="C/C 8CFT";
							}else if(rs2.getInt("cooler_type_type_id")==6){
								CoolerTypeTypeName="C/C 10CFT";
							}else if(rs2.getInt("cooler_type_type_id")==7){
								CoolerTypeTypeName="C/C 12CFT";
							}else if(rs2.getInt("cooler_type_type_id")==8){
								CoolerTypeTypeName="C/C 15CFT";
							}else if(rs2.getInt("cooler_type_type_id")==9){
								CoolerTypeTypeName="Other V/C";
							}else if(rs2.getInt("cooler_type_type_id")==10){
								CoolerTypeTypeName="Other C/C";
							}else if(rs2.getInt("cooler_type_type_id")==11){
								CoolerTypeTypeName="V/C 260SAX";
							}else if(rs2.getInt("cooler_type_type_id")==12){
								CoolerTypeTypeName="V/C 1000SAX DD";
							}
				      
				      
				      
					        RowCount=RowCount+1;
						    XSSFRow headerrow2115 = spreadsheet.createRow((short) RowCount+1);        
					  		
					  		XSSFCell headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);  
					  		headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);
							headercell2115.setCellValue(CoolerTypeCompnayName);
							SetNormalCellBackColor(workbook,headercell2115);
						    
						     
					  		headercell2115 = (XSSFCell) headerrow2115.createCell((short) 1);
							headercell2115.setCellValue(CoolerTypeTypeName);
							SetNormalCellBackColor(workbook,headercell2115);
						    
						    headercell2115 = (XSSFCell) headerrow2115.createCell((short) 2);
							headercell2115.setCellValue(rs2.getInt("quantity"));
							SetNormalCellBackColor(workbook,headercell2115);
							
							 headercell2115 = (XSSFCell) headerrow2115.createCell((short) 3);
								headercell2115.setCellValue("");
								SetNormalCellBackColor(workbook,headercell2115);
				      
						}//end of for 
				}
				
				if(1==1){
					//Cooler codes
					
					 RowCount = RowCount+1;
				      
				      XSSFRow headerrow = spreadsheet.createRow((short) RowCount+1);	      
				      XSSFCell headercell = (XSSFCell) headerrow.createCell((short) 0);
				     
				      headercell.setCellValue("Cooler Codes");
				      Set2ndHeaderBackColor(workbook,headercell);
				      spreadsheet.addMergedRegion(new CellRangeAddress(
				    		  RowCount+1, //first row (0-based)
				    		  RowCount+1, //last row (0-based)
				    		  0, //first column (0-based)
				    	      3 //last column (0-based)
				    	      ));
				      
				      
				     
						
							
				      
				      
					        RowCount=RowCount+1;
						    XSSFRow headerrow2115 = spreadsheet.createRow((short) RowCount+1);        
					  		
					  		XSSFCell headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);  
					  		headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);
							headercell2115.setCellValue("Visible Coolors");
							SetNormalCellBackColor(workbook,headercell2115);
						    
						     
					  		headercell2115 = (XSSFCell) headerrow2115.createCell((short) 1);
							headercell2115.setCellValue(VisibleColors);
							SetNormalCellBackColor(workbook,headercell2115);
						    
						    headercell2115 = (XSSFCell) headerrow2115.createCell((short) 2);
							headercell2115.setCellValue("");
							SetNormalCellBackColor(workbook,headercell2115);
							
							 headercell2115 = (XSSFCell) headerrow2115.createCell((short) 3);
								headercell2115.setCellValue("");
								SetNormalCellBackColor(workbook,headercell2115);
							
							
							RowCount=RowCount+1;
						    XSSFRow headerrow21151 = spreadsheet.createRow((short) RowCount+1);        
					  		
					  		XSSFCell headercell21151 = (XSSFCell) headerrow21151.createCell((short) 0);  
					  		headercell21151 = (XSSFCell) headerrow21151.createCell((short) 0);
							headercell21151.setCellValue("Accessible Coolors");
							SetNormalCellBackColor(workbook,headercell21151);
						    
						     
					  		headercell21151 = (XSSFCell) headerrow21151.createCell((short) 1);
							headercell21151.setCellValue(AccessibleColors);
							SetNormalCellBackColor(workbook,headercell21151);
						    
						    headercell21151 = (XSSFCell) headerrow21151.createCell((short) 2);
							headercell21151.setCellValue("");
							SetNormalCellBackColor(workbook,headercell21151);
							
							 headercell21151 = (XSSFCell) headerrow21151.createCell((short) 3);
								headercell21151.setCellValue("");
								SetNormalCellBackColor(workbook,headercell21151);
							
							
							
							String CoolerSerialNo="";
							
							ResultSet rs2c = s.executeQuery("SELECT * FROM mrd_census_cooler_code where id="+TableMainID);
							while(rs2c.next()){
								CoolerSerialNo +=rs2c.getString("tot_code")+",";
							
							}
							
							
							RowCount=RowCount+1;
						    XSSFRow headerrow211511 = spreadsheet.createRow((short) RowCount+1);        
					  		
					  		XSSFCell headercell211511 = (XSSFCell) headerrow211511.createCell((short) 0);  
					  		headercell211511 = (XSSFCell) headerrow211511.createCell((short) 0);
							headercell211511.setCellValue("Codes");
							SetNormalCellBackColor(workbook,headercell211511);
						    
						     
					  		headercell211511 = (XSSFCell) headerrow211511.createCell((short) 1);
							headercell211511.setCellValue(CoolerSerialNo);
							SetNormalCellBackColor(workbook,headercell211511);
						    
						    headercell211511 = (XSSFCell) headerrow211511.createCell((short) 2);
							headercell211511.setCellValue("");
							SetNormalCellBackColor(workbook,headercell211511);
							
							headercell211511 = (XSSFCell) headerrow211511.createCell((short) 3);
							headercell211511.setCellValue("");
							SetNormalCellBackColor(workbook,headercell211511);
				      
						
				}
				
				
				if(1==1){
					//Publicity
					
					 RowCount = RowCount+1;
				      
				      XSSFRow headerrow = spreadsheet.createRow((short) RowCount+1);	      
				      XSSFCell headercell = (XSSFCell) headerrow.createCell((short) 0);
				     
				      headercell.setCellValue("Publicity Type");
				      Set2ndHeaderBackColor(workbook,headercell);
				      spreadsheet.addMergedRegion(new CellRangeAddress(
				    		  RowCount+1, //first row (0-based)
				    		  RowCount+1, //last row (0-based)
				    		  0, //first column (0-based)
				    	      3 //last column (0-based)
				    	      ));
				      
				      
				      
				      
				      String PublicityTypeCompnayName="";
						String PublicityTypeTypeName="";
						
						ResultSet rs21 = s.executeQuery("SELECT * FROM mrd_census_publicity_types where id="+TableMainID);
						while(rs21.next()){
						
							if(rs21.getInt("publicity_type_company_id")==1){
								PublicityTypeCompnayName="PI";
							}else if(rs21.getInt("publicity_type_company_id")==2){
								PublicityTypeCompnayName="KO";
							}else if(rs21.getInt("publicity_type_company_id")==3){
								PublicityTypeCompnayName="NESTLE";
							}else if(rs21.getInt("publicity_type_company_id")==4){
								PublicityTypeCompnayName="OTHERS";
							}else if(rs21.getInt("publicity_type_company_id")==5){
								PublicityTypeCompnayName="TELECOM";
							}else if(rs21.getInt("publicity_type_company_id")==6){
								PublicityTypeCompnayName="ICECREAM";
							}
							
							
							
							if(rs21.getInt("publicity_type_type_id")==1){
								PublicityTypeTypeName="FLAG";
							}else if(rs21.getInt("publicity_type_type_id")==2){
								PublicityTypeTypeName="RACK";
							}else if(rs21.getInt("publicity_type_type_id")==3){
								PublicityTypeTypeName="CANOPY";
							}else if(rs21.getInt("publicity_type_type_id")==4){
								PublicityTypeTypeName="SIGNAGE F/L";
							}else if(rs21.getInt("publicity_type_type_id")==5){
								PublicityTypeTypeName="SIGNAGE B/L";
							}else if(rs21.getInt("publicity_type_type_id")==6){
								PublicityTypeTypeName="SIGNAGE TIN";
							}else if(rs21.getInt("publicity_type_type_id")==7){
								PublicityTypeTypeName="COUNTER";
							}else if(rs21.getInt("publicity_type_type_id")==8){
								PublicityTypeTypeName="PAINT";
							}else if(rs21.getInt("publicity_type_type_id")==9){
								PublicityTypeTypeName="POSTER";
							}else if(rs21.getInt("publicity_type_type_id")==10){
								PublicityTypeTypeName="Gondola";
							}else if(rs21.getInt("publicity_type_type_id")==11){
								PublicityTypeTypeName="Cabin";
							}
				      
				      
				      
					        RowCount=RowCount+1;
						    XSSFRow headerrow2115 = spreadsheet.createRow((short) RowCount+1);        
					  		
					  		XSSFCell headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);  
					  		headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);
							headercell2115.setCellValue(PublicityTypeCompnayName);
							SetNormalCellBackColor(workbook,headercell2115);
						    
						     
					  		headercell2115 = (XSSFCell) headerrow2115.createCell((short) 1);
							headercell2115.setCellValue(PublicityTypeTypeName);
							SetNormalCellBackColor(workbook,headercell2115);
						    
						    headercell2115 = (XSSFCell) headerrow2115.createCell((short) 2);
							headercell2115.setCellValue(rs21.getInt("quantity"));
							SetNormalCellBackColor(workbook,headercell2115);
							
							headercell2115 = (XSSFCell) headerrow2115.createCell((short) 3);
							headercell2115.setCellValue(rs21.getInt("quantity"));
							SetNormalCellBackColor(workbook,headercell2115);
				      
						}//end of for 
						
						
						//Extra row
					      
				        RowCount=RowCount+1;
					    XSSFRow headerrow211534 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell211534 = (XSSFCell) headerrow211534.createCell((short) 0);  
				  		headercell211534 = (XSSFCell) headerrow211534.createCell((short) 0);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
					    
					     
				  		headercell211534 = (XSSFCell) headerrow211534.createCell((short) 1);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
					    
					    headercell211534 = (XSSFCell) headerrow211534.createCell((short) 2);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
						
						 headercell211534 = (XSSFCell) headerrow211534.createCell((short) 3);
							headercell211534.setCellValue("");
							SetNormalCellBackColor(workbook,headercell211534);
						
				}
				
				
				
				if(1==1){
					//CSD
					
					 RowCount = RowCount+1;
				      
				      XSSFRow headerrow = spreadsheet.createRow((short) RowCount+1);	      
				      XSSFCell headercell = (XSSFCell) headerrow.createCell((short) 0);
				     
				      headercell.setCellValue("CSD");
				      Set2ndHeaderBackColor(workbook,headercell);
				      spreadsheet.addMergedRegion(new CellRangeAddress(
				    		  RowCount+1, //first row (0-based)
				    		  RowCount+1, //last row (0-based)
				    		  0, //first column (0-based)
				    	      3 //last column (0-based)
				    	      ));
				      
				      
				      
				      String CSDCompanyName="";
						String CSDPackageName="";
						String CSDBrandName="";
						
						ResultSet rs211 = s.executeQuery("SELECT * FROM mrd_census_csd_types where id="+TableMainID);
						while(rs211.next()){
						
							if(rs211.getInt("csd_company_id")==1){
								CSDCompanyName="Pepsi";
							}else if(rs211.getInt("csd_company_id")==2){
								CSDCompanyName="Coke";
							}else if(rs211.getInt("csd_company_id")==3){
								CSDCompanyName="Cola Next";
							}else if(rs211.getInt("csd_company_id")==4){
								CSDCompanyName="Gourmet";
							}else if(rs211.getInt("csd_company_id")==5){
								CSDCompanyName="None";
							}
							
							
							
							if(rs211.getInt("csd_package_id")==1){
								CSDPackageName="CAN - NORMAL";
							}else if(rs211.getInt("csd_package_id")==2){
								CSDPackageName="CAN - SLIM";
							}else if(rs211.getInt("csd_package_id")==3){
								CSDPackageName="250ML SSRB";
							}else if(rs211.getInt("csd_package_id")==4){
								CSDPackageName="250 NRB";
							}else if(rs211.getInt("csd_package_id")==5){
								CSDPackageName="345ML PET";
							}else if(rs211.getInt("csd_package_id")==6){
								CSDPackageName="500ML PET";
							}else if(rs211.getInt("csd_package_id")==7){
								CSDPackageName="1000ML PET";
							}else if(rs211.getInt("csd_package_id")==8){
								CSDPackageName="1500-1750ML PET";
							}else if(rs211.getInt("csd_package_id")==9){
								CSDPackageName="2000ML PET";
							}else if(rs211.getInt("csd_package_id")==10){
								CSDPackageName="2250ML PET";
							}else if(rs211.getInt("csd_package_id")==11){
								CSDPackageName="2500ML PET";
							}
							
							
							//Pepsi Brands
							if(rs211.getInt("csd_brand")==1 && rs211.getInt("csd_company_id")==1){
								CSDBrandName="Pepsi";
							}else if(rs211.getInt("csd_brand")==2 && rs211.getInt("csd_company_id")==1){
								CSDBrandName="7-UP";
							}else if(rs211.getInt("csd_brand")==3 && rs211.getInt("csd_company_id")==1){
								CSDBrandName="M. Dew";
							}else if(rs211.getInt("csd_brand")==4 && rs211.getInt("csd_company_id")==1){
								CSDBrandName="Mirinda";
							}else if(rs211.getInt("csd_brand")==5 && rs211.getInt("csd_company_id")==1){
								CSDBrandName="M.Dew BP";
							}else if(rs211.getInt("csd_brand")==6 && rs211.getInt("csd_company_id")==1){
								CSDBrandName="7UP-F";
							}else if(rs211.getInt("csd_brand")==7 && rs211.getInt("csd_company_id")==1){
								CSDBrandName="Diet Pepsi";
							}
							
							//Coke
							if(rs211.getInt("csd_brand")==1 && rs211.getInt("csd_company_id")==2){
								CSDBrandName="Coke";
							}else if(rs211.getInt("csd_brand")==2 && rs211.getInt("csd_company_id")==2){
								CSDBrandName="Coke Zero";
							}else if(rs211.getInt("csd_brand")==3 && rs211.getInt("csd_company_id")==2){
								CSDBrandName="Diet Coke";
							}else if(rs211.getInt("csd_brand")==4 && rs211.getInt("csd_company_id")==2){
								CSDBrandName="Sprite";
							}else if(rs211.getInt("csd_brand")==5 && rs211.getInt("csd_company_id")==2){
								CSDBrandName="Spr. Zero";
							}else if(rs211.getInt("csd_brand")==6 && rs211.getInt("csd_company_id")==2){
								CSDBrandName="Fanta";
							}else if(rs211.getInt("csd_brand")==7 && rs211.getInt("csd_company_id")==2){
								CSDBrandName="Fanta Citrus";
							}else if(rs211.getInt("csd_brand")==8 && rs211.getInt("csd_company_id")==2){
								CSDBrandName="Fanta Apple";
							}else if(rs211.getInt("csd_brand")==9 && rs211.getInt("csd_company_id")==2){
								CSDBrandName="Fanta Grape";
							}
							
							//Cola Next
							if(rs211.getInt("csd_brand")==1 && rs211.getInt("csd_company_id")==3){
								CSDBrandName="Storm";
							}else if(rs211.getInt("csd_brand")==2 && rs211.getInt("csd_company_id")==3){
								CSDBrandName="Fiz Up";
							}else if(rs211.getInt("csd_brand")==3 && rs211.getInt("csd_company_id")==3){
								CSDBrandName="Rango";
							}else if(rs211.getInt("csd_brand")==4 && rs211.getInt("csd_company_id")==3){
								CSDBrandName="Mount Dare";
							}
							
							//Gourmet
							
							if(rs211.getInt("csd_brand")==1 && rs211.getInt("csd_company_id")==4){
								CSDBrandName="Gourmet Lemon";
							}else if(rs211.getInt("csd_brand")==2 && rs211.getInt("csd_company_id")==4){
								CSDBrandName="Ice Soda";
							}else if(rs211.getInt("csd_brand")==3 && rs211.getInt("csd_company_id")==4){
								CSDBrandName="Twister";
							}else if(rs211.getInt("csd_brand")==4 && rs211.getInt("csd_company_id")==4){
								CSDBrandName="Red Annar";
							}if(rs211.getInt("csd_brand")==5 && rs211.getInt("csd_company_id")==4){
								CSDBrandName="Malta";
							}else if(rs211.getInt("csd_brand")==6 && rs211.getInt("csd_company_id")==4){
								CSDBrandName="Spark";
							}else if(rs211.getInt("csd_brand")==7 && rs211.getInt("csd_company_id")==4){
								CSDBrandName="Diet Cola";
							}else if(rs211.getInt("csd_brand")==8 && rs211.getInt("csd_company_id")==4){
								CSDBrandName="Dite Lemon";
							}if(rs211.getInt("csd_brand")==9 && rs211.getInt("csd_company_id")==4){
								CSDBrandName="Gourmet Apple";
							}else if(rs211.getInt("csd_brand")==10 && rs211.getInt("csd_company_id")==4){
								CSDBrandName="Moje Mango";
							}else if(rs211.getInt("csd_brand")==11 && rs211.getInt("csd_company_id")==4){
								CSDBrandName="Pulpy Orange";
							}else if(rs211.getInt("csd_brand")==12 && rs211.getInt("csd_company_id")==4){
								CSDBrandName="Gava";
							}else if(rs211.getInt("csd_brand")==13 && rs211.getInt("csd_company_id")==4){
								CSDBrandName="Gourmet Cola";
							}
							
							
							if(rs211.getInt("csd_company_id")==5){  //for none
								CSDPackageName="";
								CSDBrandName="";
							}
				      
				      
				      
					        RowCount=RowCount+1;
						    XSSFRow headerrow2115 = spreadsheet.createRow((short) RowCount+1);        
					  		
					  		XSSFCell headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);  
					  		//headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);
							headercell2115.setCellValue(CSDCompanyName);
							SetNormalCellBackColor(workbook,headercell2115);
						    
						     
					  		headercell2115 = (XSSFCell) headerrow2115.createCell((short) 1);
							headercell2115.setCellValue(CSDPackageName);
							SetNormalCellBackColor(workbook,headercell2115);
						    
						    headercell2115 = (XSSFCell) headerrow2115.createCell((short) 2);
							headercell2115.setCellValue(CSDBrandName);
							SetNormalCellBackColor(workbook,headercell2115);
							
							 headercell2115 = (XSSFCell) headerrow2115.createCell((short) 3);
								headercell2115.setCellValue("");
								SetNormalCellBackColor(workbook,headercell2115);
				      
						}//end of for 
						
						//Extra row
					      
				        RowCount=RowCount+1;
					    XSSFRow headerrow211534 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell211534 = (XSSFCell) headerrow211534.createCell((short) 0);  
				  		headercell211534 = (XSSFCell) headerrow211534.createCell((short) 0);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
					    
					     
				  		headercell211534 = (XSSFCell) headerrow211534.createCell((short) 1);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
					    
					    headercell211534 = (XSSFCell) headerrow211534.createCell((short) 2);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
						
						 headercell211534 = (XSSFCell) headerrow211534.createCell((short) 3);
							headercell211534.setCellValue("");
							SetNormalCellBackColor(workbook,headercell211534);
				}
				
				if(1==1){
					//NCB
					
					 RowCount = RowCount+1;
				      
				      XSSFRow headerrow = spreadsheet.createRow((short) RowCount+1);	      
				      XSSFCell headercell = (XSSFCell) headerrow.createCell((short) 0);
				     
				      headercell.setCellValue("NCB");
				      Set2ndHeaderBackColor(workbook,headercell);
				      spreadsheet.addMergedRegion(new CellRangeAddress(
				    		  RowCount+1, //first row (0-based)
				    		  RowCount+1, //last row (0-based)
				    		  0, //first column (0-based)
				    	      3 //last column (0-based)
				    	      ));
				      
				      
				      
				      String NCBCompanyName="";
						String NCBPackageName="";
						String NCBBrandName="";
						
						ResultSet rs2112 = s.executeQuery("SELECT * FROM mrd_census_ncb_types where id="+TableMainID);
						while(rs2112.next()){
						
							if(rs2112.getInt("ncb_company_id")==1){
								NCBCompanyName="WATER";
							}else if(rs2112.getInt("ncb_company_id")==2){
								NCBCompanyName="JUICE";
							}else if(rs2112.getInt("ncb_company_id")==3){
								NCBCompanyName="ENERGY DRINK";
							}else if(rs2112.getInt("ncb_company_id")==4){
								NCBCompanyName="FLAVOURED MILK";
							}else if(rs2112.getInt("ncb_company_id")==5){
								NCBCompanyName="None";
							}
							
							
							
							if(rs2112.getInt("ncb_package_id")==1){
								NCBPackageName="TP";
							}else if(rs2112.getInt("ncb_package_id")==2){
								NCBPackageName="SSRB";
							}else if(rs2112.getInt("ncb_package_id")==3){
								NCBPackageName="NRB";
							}else if(rs2112.getInt("ncb_package_id")==4){
								NCBPackageName="355ml";
							}else if(rs2112.getInt("ncb_package_id")==5){
								NCBPackageName="500ml";
							}else if(rs2112.getInt("ncb_package_id")==6){
								NCBPackageName="1000ml";
							}else if(rs2112.getInt("ncb_package_id")==7){
								NCBPackageName="1500ml";
							}
							
							
							//Water Brands
							if(rs2112.getInt("ncb_brand")==1 && rs2112.getInt("ncb_company_id")==1){
								NCBBrandName="A.F";
							}else if(rs2112.getInt("ncb_brand")==2 && rs2112.getInt("ncb_company_id")==1){
								NCBBrandName="NESTLE";
							}else if(rs2112.getInt("ncb_brand")==3 && rs2112.getInt("ncb_company_id")==1){
								NCBBrandName="KINLEY";
							}else if(rs2112.getInt("ncb_brand")==4 && rs2112.getInt("ncb_company_id")==1){
								NCBBrandName="GOURMET";
							}else if(rs2112.getInt("ncb_brand")==5 && rs2112.getInt("ncb_company_id")==1){
								NCBBrandName="OTHERS";
							}
							
							//Juice Brands
							if(rs2112.getInt("ncb_brand")==1 && rs2112.getInt("ncb_company_id")==2){
								NCBBrandName="SLICE";
							}else if(rs2112.getInt("ncb_brand")==2 && rs2112.getInt("ncb_company_id")==2){
								NCBBrandName="NESTLE";
							}else if(rs2112.getInt("ncb_brand")==3 && rs2112.getInt("ncb_company_id")==2){
								NCBBrandName="SHEZAN";
							}else if(rs2112.getInt("ncb_brand")==4 && rs2112.getInt("ncb_company_id")==2){
								NCBBrandName="FRUITIEN";
							}else if(rs2112.getInt("ncb_brand")==5 && rs2112.getInt("ncb_company_id")==2){
								NCBBrandName="RANI";
							}else if(rs2112.getInt("ncb_brand")==6 && rs2112.getInt("ncb_company_id")==2){
								NCBBrandName="OTHER";
							}
							
							//Energy Drinks Brands
							if(rs2112.getInt("ncb_brand")==1 && rs2112.getInt("ncb_company_id")==3){
								NCBBrandName="STING";
							}else if(rs2112.getInt("ncb_brand")==2 && rs2112.getInt("ncb_company_id")==3){
								NCBBrandName="REDBULL";
							}else if(rs2112.getInt("ncb_brand")==3 && rs2112.getInt("ncb_company_id")==3){
								NCBBrandName="OTHER";
							}
							
							//Falavored Milk Brands
							if(rs2112.getInt("ncb_brand")==1 && rs2112.getInt("ncb_company_id")==4){
								NCBBrandName="NESTLE";
							}else if(rs2112.getInt("ncb_brand")==2 && rs2112.getInt("ncb_company_id")==4){
								NCBBrandName="OLPER";
							}
							
							 if(rs2112.getInt("ncb_company_id")==5){ //for None
								NCBPackageName="";
								NCBBrandName="";
							}
				      
				      
				      
					        RowCount=RowCount+1;
						    XSSFRow headerrow2115 = spreadsheet.createRow((short) RowCount+1);        
					  		
					  		XSSFCell headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);  
					  		//headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);
							headercell2115.setCellValue(NCBCompanyName);
							SetNormalCellBackColor(workbook,headercell2115);
						    
						     
					  		headercell2115 = (XSSFCell) headerrow2115.createCell((short) 1);
							headercell2115.setCellValue(NCBPackageName);
							SetNormalCellBackColor(workbook,headercell2115);
						    
						    headercell2115 = (XSSFCell) headerrow2115.createCell((short) 2);
							headercell2115.setCellValue(NCBBrandName);
							SetNormalCellBackColor(workbook,headercell2115);
							
							 headercell2115 = (XSSFCell) headerrow2115.createCell((short) 3);
								headercell2115.setCellValue("");
								SetNormalCellBackColor(workbook,headercell2115);
				      
						}//end of for 
						
						//Extra row
					      
				        RowCount=RowCount+1;
					    XSSFRow headerrow211534 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell211534 = (XSSFCell) headerrow211534.createCell((short) 0);  
				  		headercell211534 = (XSSFCell) headerrow211534.createCell((short) 0);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
					    
					     
				  		headercell211534 = (XSSFCell) headerrow211534.createCell((short) 1);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
					    
					    headercell211534 = (XSSFCell) headerrow211534.createCell((short) 2);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
						 headercell211534 = (XSSFCell) headerrow211534.createCell((short) 3);
							headercell211534.setCellValue("");
							SetNormalCellBackColor(workbook,headercell211534);
				}
				
				
				if(1==1){
					//Vol CSD
					
					 RowCount = RowCount+1;
				      
				      XSSFRow headerrow = spreadsheet.createRow((short) RowCount+1);	      
				      XSSFCell headercell = (XSSFCell) headerrow.createCell((short) 0);
				     
				      headercell.setCellValue("Vol CSD");
				      Set2ndHeaderBackColor(workbook,headercell);
				      spreadsheet.addMergedRegion(new CellRangeAddress(
				    		  RowCount+1, //first row (0-based)
				    		  RowCount+1, //last row (0-based)
				    		  0, //first column (0-based)
				    	      3 //last column (0-based)
				    	      ));
				      
				      
				      
				      String VolCSDCompanyName="";
						String VolCSDPackageName="";
						int VolCSDBrandName=0;
						int VolCSDBrandNameMDE=0;
						
						ResultSet rs2112vc = s.executeQuery("SELECT * FROM mrd_census_vol_csd where id="+TableMainID);
						while(rs2112vc.next()){
						
							if(rs2112vc.getInt("vol_csd_company_id")==1){
								VolCSDCompanyName="PI";
							}else if(rs2112vc.getInt("vol_csd_company_id")==2){
								VolCSDCompanyName="KO";
							}else if(rs2112vc.getInt("vol_csd_company_id")==3){
								VolCSDCompanyName="Others";
							}else if(rs2112vc.getInt("vol_csd_company_id")==4){
								VolCSDCompanyName="None";
							}
							
							
							
							if(rs2112vc.getInt("vol_csd_package_id")==1){
								VolCSDPackageName="SSRB";
							}else if(rs2112vc.getInt("vol_csd_package_id")==2){
								VolCSDPackageName="PET - SS";
							}else if(rs2112vc.getInt("vol_csd_package_id")==3){
								VolCSDPackageName="PET - MS";
							}
							
							
							VolCSDBrandName = rs2112vc.getInt("vol_csd_brand");
							VolCSDBrandNameMDE = rs2112vc.getInt("vol_csd_mde");
							
							
							 if(rs2112vc.getInt("vol_csd_company_id")==4){ //for None
								 VolCSDBrandName=0;
								 VolCSDBrandNameMDE=0;
								}
				      
				      
				      
					        RowCount=RowCount+1;
						    XSSFRow headerrow2115 = spreadsheet.createRow((short) RowCount+1);        
					  		
					  		XSSFCell headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);  
					  		//headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);
							headercell2115.setCellValue(VolCSDCompanyName);
							SetNormalCellBackColor(workbook,headercell2115);
						    
						     
					  		headercell2115 = (XSSFCell) headerrow2115.createCell((short) 1);
							headercell2115.setCellValue(VolCSDPackageName);
							SetNormalCellBackColor(workbook,headercell2115);
						    
						    headercell2115 = (XSSFCell) headerrow2115.createCell((short) 2);
							headercell2115.setCellValue(VolCSDBrandName);
							SetNormalCellBackColor(workbook,headercell2115);
							
							 headercell2115 = (XSSFCell) headerrow2115.createCell((short) 3);
								headercell2115.setCellValue(VolCSDBrandNameMDE);
								SetNormalCellBackColor(workbook,headercell2115);
				      
						}//end of for 
						
						//Extra row
					      
				        RowCount=RowCount+1;
					    XSSFRow headerrow211534 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell211534 = (XSSFCell) headerrow211534.createCell((short) 0);  
				  		headercell211534 = (XSSFCell) headerrow211534.createCell((short) 0);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
					    
					     
				  		headercell211534 = (XSSFCell) headerrow211534.createCell((short) 1);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
					    
					    headercell211534 = (XSSFCell) headerrow211534.createCell((short) 2);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
						 headercell211534 = (XSSFCell) headerrow211534.createCell((short) 3);
							headercell211534.setCellValue("");
							SetNormalCellBackColor(workbook,headercell211534);
				}
				
				if(1==1){
					//Vol Juice
					
					 RowCount = RowCount+1;
				      
				      XSSFRow headerrow = spreadsheet.createRow((short) RowCount+1);	      
				      XSSFCell headercell = (XSSFCell) headerrow.createCell((short) 0);
				     
				      headercell.setCellValue("Vol Juice");
				      Set2ndHeaderBackColor(workbook,headercell);
				      spreadsheet.addMergedRegion(new CellRangeAddress(
				    		  RowCount+1, //first row (0-based)
				    		  RowCount+1, //last row (0-based)
				    		  0, //first column (0-based)
				    	      3 //last column (0-based)
				    	      ));
				      
				      
				      
				      String VolJuiceCompanyName="";
						String VolJuicePackageName="";
						int VolJuiceBrandName=0;
						int VolJuiceBrandNameMDE=0;
						
						ResultSet rs2112vj = s.executeQuery("SELECT * FROM mrd_census_vol_juice where id="+TableMainID);
						while(rs2112vj.next()){
						
							if(rs2112vj.getInt("vol_juice_company_id")==1){
								VolJuiceCompanyName="PI";
							}else if(rs2112vj.getInt("vol_juice_company_id")==2){
								VolJuiceCompanyName="KO";
							}else if(rs2112vj.getInt("vol_juice_company_id")==3){
								VolJuiceCompanyName="Others";
							}else if(rs2112vj.getInt("vol_juice_company_id")==4){
								VolJuiceCompanyName="None";
							}
							
							
							
							if(rs2112vj.getInt("vol_juice_company_id")==1 && rs2112vj.getInt("vol_juice_package_id")==1){
								VolJuicePackageName="TETRA";
							}
							
							
							if(rs2112vj.getInt("vol_juice_company_id")==2 && rs2112vj.getInt("vol_juice_package_id")==1){
								VolJuicePackageName="SSRB";
							}else if(rs2112vj.getInt("vol_juice_company_id")==2 && rs2112vj.getInt("vol_juice_package_id")==2){
								VolJuicePackageName="PET";
							}
							
							
							if(rs2112vj.getInt("vol_juice_company_id")==3 && rs2112vj.getInt("vol_juice_package_id")==1){
								VolJuicePackageName="SSRB";
							}else if(rs2112vj.getInt("vol_juice_company_id")==3 && rs2112vj.getInt("vol_juice_package_id")==2){
								VolJuicePackageName="NRB";
							}else if(rs2112vj.getInt("vol_juice_company_id")==3 && rs2112vj.getInt("vol_juice_package_id")==3){
								VolJuicePackageName="PET";
							}else if(rs2112vj.getInt("vol_juice_company_id")==3 && rs2112vj.getInt("vol_juice_package_id")==4){
								VolJuicePackageName="TETRA";
							}else if(rs2112vj.getInt("vol_juice_company_id")==3 && rs2112vj.getInt("vol_juice_package_id")==5){
								VolJuicePackageName="CAN";
							}
							
							
							VolJuiceBrandName = rs2112vj.getInt("vol_juice_brand");
							VolJuiceBrandNameMDE = rs2112vj.getInt("vol_juice_mde");
							if(rs2112vj.getInt("vol_juice_company_id")==4){
								VolJuiceBrandName=0;
								VolJuiceBrandNameMDE=0;
							}
				      
				      
				      
					        RowCount=RowCount+1;
						    XSSFRow headerrow2115 = spreadsheet.createRow((short) RowCount+1);        
					  		
					  		XSSFCell headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);  
					  		//headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);
							headercell2115.setCellValue(VolJuiceCompanyName);
							SetNormalCellBackColor(workbook,headercell2115);
						    
						     
					  		headercell2115 = (XSSFCell) headerrow2115.createCell((short) 1);
							headercell2115.setCellValue(VolJuicePackageName);
							SetNormalCellBackColor(workbook,headercell2115);
						    
						    headercell2115 = (XSSFCell) headerrow2115.createCell((short) 2);
							headercell2115.setCellValue(VolJuiceBrandName);
							SetNormalCellBackColor(workbook,headercell2115);
							
							 headercell2115 = (XSSFCell) headerrow2115.createCell((short) 3);
								headercell2115.setCellValue(VolJuiceBrandNameMDE);
								SetNormalCellBackColor(workbook,headercell2115);
				      
						}//end of for 
						
						//Extra row
					      
				        RowCount=RowCount+1;
					    XSSFRow headerrow211534 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell211534 = (XSSFCell) headerrow211534.createCell((short) 0);  
				  		headercell211534 = (XSSFCell) headerrow211534.createCell((short) 0);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
					    
					     
				  		headercell211534 = (XSSFCell) headerrow211534.createCell((short) 1);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
					    
					    headercell211534 = (XSSFCell) headerrow211534.createCell((short) 2);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
						 headercell211534 = (XSSFCell) headerrow211534.createCell((short) 3);
							headercell211534.setCellValue("");
							SetNormalCellBackColor(workbook,headercell211534);
				}
				
				if(1==1){
					//Vol Drinks
					
					 RowCount = RowCount+1;
				      
				      XSSFRow headerrow = spreadsheet.createRow((short) RowCount+1);	      
				      XSSFCell headercell = (XSSFCell) headerrow.createCell((short) 0);
				     
				      headercell.setCellValue("Vol Drinks");
				      Set2ndHeaderBackColor(workbook,headercell);
				      spreadsheet.addMergedRegion(new CellRangeAddress(
				    		  RowCount+1, //first row (0-based)
				    		  RowCount+1, //last row (0-based)
				    		  0, //first column (0-based)
				    	      3 //last column (0-based)
				    	      ));
				      
				      
				      
				      String VolDrinkCompanyName="";
						String VolDrinkPackageName="";
						int VolDrinkBrandName=0;
						int VolDrinkBrandNameMDE=0;
						
						ResultSet rs2112vd = s.executeQuery("SELECT * FROM mrd_census_vol_drink where id="+TableMainID);
						while(rs2112vd.next()){
						
							if(rs2112vd.getInt("vol_drink_company_id")==1){
								VolDrinkCompanyName="Water";
							}else if(rs2112vd.getInt("vol_drink_company_id")==2){
								VolDrinkCompanyName="Energy Drink";
							}else if(rs2112vd.getInt("vol_drink_company_id")==3){
								VolDrinkCompanyName="None";
							}
							
							
							if(rs2112vd.getInt("vol_drink_company_id")==1 && rs2112vd.getInt("vol_drink_package_id")==1){
								VolDrinkPackageName="PI";
							}else if(rs2112vd.getInt("vol_drink_company_id")==1 && rs2112vd.getInt("vol_drink_package_id")==2){
								VolDrinkPackageName="KO";
							}else if(rs2112vd.getInt("vol_drink_company_id")==1 && rs2112vd.getInt("vol_drink_package_id")==3){
								VolDrinkPackageName="Others";
							}
							
							if(rs2112vd.getInt("vol_drink_company_id")==2 && rs2112vd.getInt("vol_drink_package_id")==1){
								VolDrinkPackageName="Sting";
							}else if(rs2112vd.getInt("vol_drink_company_id")==2 && rs2112vd.getInt("vol_drink_package_id")==2){
								VolDrinkPackageName="RedBull";
							}else if(rs2112vd.getInt("vol_drink_company_id")==2 && rs2112vd.getInt("vol_drink_package_id")==3){
								VolDrinkPackageName="Others";
							}
					
							
							
							
							
							VolDrinkBrandName = rs2112vd.getInt("vol_drink_brand");
							VolDrinkBrandNameMDE = rs2112vd.getInt("vol_drink_mde");
							
							 if(rs2112vd.getInt("vol_drink_company_id")==3){ //for None
								 VolDrinkBrandName=0;
								 VolDrinkBrandNameMDE=0;
								}
				      
				      
				      
					        RowCount=RowCount+1;
						    XSSFRow headerrow2115 = spreadsheet.createRow((short) RowCount+1);        
					  		
					  		XSSFCell headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);  
					  		//headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);
							headercell2115.setCellValue(VolDrinkCompanyName);
							SetNormalCellBackColor(workbook,headercell2115);
						    
						     
					  		headercell2115 = (XSSFCell) headerrow2115.createCell((short) 1);
							headercell2115.setCellValue(VolDrinkPackageName);
							SetNormalCellBackColor(workbook,headercell2115);
						    
						    headercell2115 = (XSSFCell) headerrow2115.createCell((short) 2);
							headercell2115.setCellValue(VolDrinkBrandName);
							SetNormalCellBackColor(workbook,headercell2115);
							
							 headercell2115 = (XSSFCell) headerrow2115.createCell((short) 3);
								headercell2115.setCellValue(VolDrinkBrandNameMDE);
								SetNormalCellBackColor(workbook,headercell2115);
				      
						}//end of for 
						
						//Extra row
					      
				        RowCount=RowCount+1;
					    XSSFRow headerrow211534 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell211534 = (XSSFCell) headerrow211534.createCell((short) 0);  
				  		headercell211534 = (XSSFCell) headerrow211534.createCell((short) 0);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
					    
					     
				  		headercell211534 = (XSSFCell) headerrow211534.createCell((short) 1);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
					    
					    headercell211534 = (XSSFCell) headerrow211534.createCell((short) 2);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
						 headercell211534 = (XSSFCell) headerrow211534.createCell((short) 3);
							headercell211534.setCellValue("");
							SetNormalCellBackColor(workbook,headercell211534);
				}
				
				if(1==1){
					//Cooler Placement
					
					 RowCount = RowCount+1;
				      
				      XSSFRow headerrow = spreadsheet.createRow((short) RowCount+1);	      
				      XSSFCell headercell = (XSSFCell) headerrow.createCell((short) 0);
				     
				      headercell.setCellValue("Cooler Placement");
				      Set2ndHeaderBackColor(workbook,headercell);
				      spreadsheet.addMergedRegion(new CellRangeAddress(
				    		  RowCount+1, //first row (0-based)
				    		  RowCount+1, //last row (0-based)
				    		  0, //first column (0-based)
				    	      3 //last column (0-based)
				    	      ));
				      
				      
				      
				      String PlaceCompanyName="";
						String PlacePackageName="";
						String PlaceBrandName="";
						
						//System.out.println("SELECT * FROM mrd_census_pace where id="+TableMainID);
						ResultSet rs2112p = s.executeQuery("SELECT * FROM mrd_census_pace where id="+TableMainID);
						while(rs2112p.next()){
						
							if(rs2112p.getInt("pace_company_id")==1){
								PlaceCompanyName="PI";
							}else if(rs2112p.getInt("pace_company_id")==2){
								PlaceCompanyName="KO";
							}else if(rs2112p.getInt("pace_company_id")==3){
								PlaceCompanyName="Nestle";
							}else if(rs2112p.getInt("pace_company_id")==4){
								PlaceCompanyName="Redbull";
							}else if(rs2112p.getInt("pace_company_id")==5){
								PlaceCompanyName="Ice Cream";
							}else if(rs2112p.getInt("pace_company_id")==6){
								PlaceCompanyName="Personal";
							}else if(rs2112p.getInt("pace_company_id")==7){
								PlaceCompanyName="Others";
							}
							
							
							//
							
							if(rs2112p.getInt("pace_package_id")==1){
								PlacePackageName="Placement";
							}else if(rs2112p.getInt("pace_package_id")==2){
								PlacePackageName="Visible";
							}else if(rs2112p.getInt("pace_package_id")==3){
								PlacePackageName="Access";
							}
							
							
							if(rs2112p.getInt("pace_package_id")==1 && rs2112p.getInt("pace_brand")==1){
								PlaceBrandName="I/S";
							}else if(rs2112p.getInt("pace_package_id")==1 && rs2112p.getInt("pace_brand")==2){
								PlaceBrandName="O/S";
							}else if(rs2112p.getInt("pace_package_id")==1 && rs2112p.getInt("pace_brand")==3){
								PlaceBrandName="B/S";
							}
						
				      
				      
					        RowCount=RowCount+1;
						    XSSFRow headerrow2115 = spreadsheet.createRow((short) RowCount+1);        
					  		
					  		XSSFCell headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);  
					  		//headercell2115 = (XSSFCell) headerrow2115.createCell((short) 0);
							headercell2115.setCellValue(PlaceCompanyName);
							SetNormalCellBackColor(workbook,headercell2115);
						    
						     
					  		headercell2115 = (XSSFCell) headerrow2115.createCell((short) 1);
							headercell2115.setCellValue(PlacePackageName);
							SetNormalCellBackColor(workbook,headercell2115);
						    
						    headercell2115 = (XSSFCell) headerrow2115.createCell((short) 2);
							headercell2115.setCellValue(PlaceBrandName);
							SetNormalCellBackColor(workbook,headercell2115);
							
							 headercell2115 = (XSSFCell) headerrow2115.createCell((short) 3);
								headercell2115.setCellValue("");
								SetNormalCellBackColor(workbook,headercell2115);
				      
						}//end of for 
						
						//Extra row
					      
				        RowCount=RowCount+1;
					    XSSFRow headerrow211534 = spreadsheet.createRow((short) RowCount+1);        
				  		
				  		XSSFCell headercell211534 = (XSSFCell) headerrow211534.createCell((short) 0);  
				  		headercell211534 = (XSSFCell) headerrow211534.createCell((short) 0);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
					    
					     
				  		headercell211534 = (XSSFCell) headerrow211534.createCell((short) 1);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
					    
					    headercell211534 = (XSSFCell) headerrow211534.createCell((short) 2);
						headercell211534.setCellValue("");
						SetNormalCellBackColor(workbook,headercell211534);
						 headercell211534 = (XSSFCell) headerrow211534.createCell((short) 3);
							headercell211534.setCellValue("");
							SetNormalCellBackColor(workbook,headercell211534);
				}
				
				
				
				
					
				//Generated On
				    
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
					
					
					
			
			
			//Auto Sizing Column
		    
				  
				    
		    for(int i=0;i<4;i++){
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
			style61.setAlignment(CellStyle.ALIGN_LEFT);
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
	
}
