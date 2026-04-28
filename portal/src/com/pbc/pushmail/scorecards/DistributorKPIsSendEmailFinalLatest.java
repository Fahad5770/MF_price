package com.pbc.pushmail.scorecards;

import java.sql.SQLException;


import com.pbc.util.Utilities;

import java.io.FileOutputStream;
import java.io.IOException;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pbc.util.Datasource;

import com.pbc.util.Utilities;

public class DistributorKPIsSendEmailFinalLatest {
	
	
	public static final String stock_filename_distributor_all = "Stock_Analysis_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String stock_filename_distributor_sd1 = "Stock_Analysis_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String stock_filename_distributor_sd2 = "Stock_Analysis_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String stock_filename_distributor_sd3 = "Stock_Analysis_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String stock_filename_distributor_sd4 = "Stock_Analysis_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String stock_filename_distributor_sd5 = "Stock_Analysis_SD5_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static final String filename_distributor_all = "KPI_Dist_Today_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd1 = "KPI_Dist_Today_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd2 = "KPI_Dist_Today_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd3 = "KPI_Dist_Today_SD3"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd4 = "KPI_Dist_Today_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd5 = "KPI_Dist_Today_SD5_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	

	
	public static final String filename_pjp_all = "KPI_PJP_Today_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd1 = "KPI_PJP_Today_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd2 = "KPI_PJP_Today_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd3 = "KPI_PJP_Today_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd4 = "KPI_PJP_Today_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd5 = "KPI_PJP_Today_SD5_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static final String filename_pjp_all_mtd = "KPI_PJP_MTD_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd1_mtd = "KPI_PJP_MTD_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd2_mtd = "KPI_PJP_MTD_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd3_mtd = "KPI_PJP_MTD_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd4_mtd = "KPI_PJP_MTD_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd5_mtd = "KPI_PJP_MTD_SD5_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";

	
	public static final String filename_distributor_all_mtd = "KPI_Dist_MTD_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd1_mtd = "KPI_Dist_MTD_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd2_mtd = "KPI_Dist_MTD_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd3_mtd = "KPI_Dist_MTD_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd4_mtd = "KPI_Dist_MTD_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd5_mtd = "KPI_Dist_MTD_SD5_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static final String filename_no_sale_all = "No_Sale_Outlets_Today_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_no_sale_sd1 = "No_Sale_Outlets_Today_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_no_sale_sd2 = "No_Sale_Outlets_Today_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_no_sale_sd3 = "No_Sale_Outlets_Today_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_no_sale_sd4 = "No_Sale_Outlets_Today_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_no_sale_sd5 = "No_Sale_Outlets_Today_SD5_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	
	public static final String filename_sku_availability_all = "SKU_Availability_MTD_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_sku_availability_sd1 = "SKU_Availability_MTD_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_sku_availability_sd2 = "SKU_Availability_MTD_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_sku_availability_sd3 = "SKU_Availability_MTD_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_sku_availability_sd4 = "SKU_Availability_MTD_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_sku_availability_sd5 = "SKU_Availability_MTD_SD5_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static final String filename_volume_analysis_all = "Volume_Analysis_Today_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_volume_analysis_sd1 = "Volume_Analysis_Today_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_volume_analysis_sd2 = "Volume_Analysis_Today_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_volume_analysis_sd3 = "Volume_Analysis_Today_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_volume_analysis_sd4 = "Volume_Analysis_Today_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_volume_analysis_sd5 = "Volume_Analysis_Today_SD5_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static final String filename_KPI_Today = "KPI_Today_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_KPI_MTD = "KPI_MTD_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, Exception {
		
	
			AllDays();//For All Days Except Friday  and Sunday
			/*mailSD1();//For Central Punjab
			mailSD2();//For KPK
			mailSD3();//For Leyyaha,mn 
			mailSD4();//For KPK Special
*/		
		
		  
	}
	 public static void AllDays(){ 
		 System.out.println("Called AllDays");
		 try {
				
				
				//TSO
				
				
				Thread ASMThread = new Thread()
		        {
		            public void run() {
		            	System.out.println("ASMThread");
		                try{
		                
		                	
		            			
				Datasource ds = new Datasource();
				ds.createConnectionToReplica();
				Statement s = ds.createStatement();
				
				//ResultSet rs = s.executeQuery("select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email, (select email from users where id = dbp.asm_id) asm_email from distributor_beat_plan dbp where dbp.distributor_id in ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between from_days(to_days(curdate())-5) and curdate()) and asm_id is not null group by asm_id having ct = 1 and asm_id <= 3217");
				//ResultSet rs = s.executeQuery("select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email, (select email from users where id = dbp.asm_id) asm_email from distributor_beat_plan dbp where dbp.distributor_id in ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between from_days(to_days(curdate())-6) and curdate()) and asm_id is not null group by asm_id");
				System.out.println("Select *from(select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email, (select email from users where id = dbp.asm_id) asm_email,(select (select email from users where id = icd2.rsm_id) from common_distributors icd2 where icd2.distributor_id = dbp.distributor_id) rsm_email from distributor_beat_plan dbp where 1=1 and distributor_id!=100914 and asm_id is not null  group by asm_id)as a where tdm_email is not null and snd_email is not null and asm_email is not null and rsm_email is not null");
				ResultSet rs = s.executeQuery("Select *from(select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email, (select email from users where id = dbp.asm_id) asm_email,(select (select email from users where id = icd2.rsm_id) from common_distributors icd2 where icd2.distributor_id = dbp.distributor_id) rsm_email from distributor_beat_plan dbp where 1=1 and distributor_id!=100914 and asm_id is not null  group by asm_id)as a where tdm_email is not null and snd_email is not null and asm_email is not null and rsm_email is not null");
				while(rs.next()){
					
					
					try{
						long ASMID=rs.getLong(1);
						String ASMName = rs.getString(2);
						long DistributorID = rs.getLong(3);
						String TDMEmail = rs.getString("tdm_email");
						String SNDEmail = rs.getString("snd_email");
						String ASMEmail = rs.getString("asm_email");
						String RSMEmail = rs.getString("rsm_email");
						
						String filename_pjp_today = "KPI_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
						String filename_pjp_mtd = "KPI_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
						
						
									/*
									 * if (TDMEmail == null){ TDMEmail = DistributorID+"@pbc.com.pk"; } if (SNDEmail
									 * == null){ SNDEmail = DistributorID+"@pbc.com.pk"; }
									 */
						
						 new com.pbc.pushmail.scorecards.NewKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath() + "/" + filename_pjp_today, ASMID, false);
						 
						 new com.pbc.pushmail.scorecards.NewKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath() + "/" + filename_pjp_mtd, ASMID, true);
						
						 Utilities.sendPBCHTMLEmailExternalMoiz(new String[]{ASMEmail},new String[]{SNDEmail,RSMEmail,TDMEmail},new String[]{"theia.notifications@pbc.com.pk"}, "KPI | TSO | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp_today,filename_pjp_mtd});	
												
				 //       Utilities.sendPBCHTMLEmailExternalMoiz(new String[]{"shahrukh.salman@pbc.com.pk"},new String[]{"fahad.khalid@pbc.com.pk"},new String[]{"theia.notifications@pbc.com.pk"}, "KPI | SO | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp_today});	

						
						//System.out.println(DistributorID+" "+TDMEmail+" "+ASMID+" "+SNDEmail);
						
					//	String filename_pjp = "KPI_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					//	String filename_pjp_mtd = "KPI_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					//	String filename_no_sale = "No_Sale_Outlets_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					//	String filename_volume_analysis = "Volume_Analysis_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
						
						//String filename_SalesVsPackagesOutlets1 = "Outlet_Sales_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
						//String filename_SalesVsPackagesOutlets2 = "Outlet_Sales_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
						
						
						
						
				//		new com.pbc.pushmail.scorecards.ASMKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp, ASMID, false);
				//		new com.pbc.pushmail.scorecards.ASMKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_mtd, ASMID, true);
						
				//		new com.pbc.pushmail.scorecards.ASMNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale, ASMID, false);
						
						//ASMVolumeAnalysisPSRExcel va2 = new com.pbc.pushmail.scorecards.ASMVolumeAnalysisPSRExcel();
						//va2.setSND(ASMID);
						//va2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis,0);
						
						
						
						//Outlet Sales added
						
						//new com.pbc.pushmail.scorecards.SalesVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_SalesVsPackagesOutlets1, ASMID, false);
						//new com.pbc.pushmail.scorecards.SalesVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_SalesVsPackagesOutlets2, ASMID, true);
						
						
						////////////////////
						
						
						
						
						////////////////////
						

						
						
						////////Utilities.sendPBCHTMLEmail(new String[]{"zulqurnan.aslam@pbc.com.pk"},null,null,  "KPI | MDE | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale,filename_SalesVsPackagesOutlets1,filename_SalesVsPackagesOutlets2});
						
						/*
						if(ASMID==5002 || ASMID==5009 || ASMID==5006 || ASMID==5014 || ASMID==5013) { // For Central Punjab
							Utilities.sendPBCHTMLEmailExternalMoiz(new String[]{ASMEmail},new String[]{"theia.notifications@pbc.com.pk","shahrukh.salman@pbc.com.pk","zulqurnan.aslam@pbc.com.pk"}, null, "KPI | SO | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale});
							//SendHTMLEmailMoiz("zulqurnan.aslam@pbc.com.pk","","shahrukh.salman@pbc.com.pk",  "KPI | SO | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale});
						}
						if(ASMID==5010 || ASMID==5008) { // For KPK
							Utilities.sendPBCHTMLEmailExternalMoiz(new String[]{ASMEmail},new String[]{"imran.shah@moizfoods.com","naik.muhammad@moizfoods.com","theia.notifications@pbc.com.pk","shahrukh.salman@pbc.com.pk","zulqurnan.aslam@pbc.com.pk"},null,  "KPI | SO | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID),new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale});
							//SendHTMLEmailMoiz("zulqurnan.aslam@pbc.com.pk","","shahrukh.salman@pbc.com.pk",  "KPI | SO | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID),new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale});
						}
						if(ASMID==5012 || ASMID==5011) { // For KPK
							Utilities.sendPBCHTMLEmailExternalMoiz(new String[]{ASMEmail},new String[]{"imran.shah@moizfoods.com","naik.muhammad@moizfoods.com","feroz.shah@moizfoods.com","theia.notifications@pbc.com.pk","shahrukh.salman@pbc.com.pk","zulqurnan.aslam@pbc.com.pk"},null,  "KPI | SO | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID),new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale});
							//SendHTMLEmailMoiz("zulqurnan.aslam@pbc.com.pk","","shahrukh.salman@pbc.com.pk",  "KPI | SO | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID),new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale});
						}
						if(ASMID == 9398 || ASMID==9425 || ASMID==9423 || ASMID==9411) { // For Town Mills
							Utilities.sendPBCHTMLEmailExternalMoiz(new String[]{ASMEmail},new String[]{"bilal.nasir@moizfoods.com","theia.notifications@pbc.com.pk","shahrukh.salman@pbc.com.pk","zulqurnan.aslam@pbc.com.pk"},null,  "KPI | SO | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale});
							//SendHTMLEmailMoiz("zulqurnan.aslam@pbc.com.pk","","shahrukh.salman@pbc.com.pk",  "KPI | SO | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale});							
						}*/
						
						
						
						//asm, snd_id, rsm_id, tdm_id
						//bcc => theia notification and sharukh sb

					///	Utilities.sendPBCHTMLEmailExternalMoiz(new String[]{ASMEmail},new String[] {SNDEmail,RSMEmail,TDMEmail},new String[]{"theia.notifications@pbc.com.pk","shahrukh.salman@pbc.com.pk"}, "KPI | SO | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale});
							
				  
						

						
						
					}catch(Exception e){
						e.printStackTrace();
					}
					//break;
				}			
				
				ds.dropConnection();
		                }catch(Exception e){e.printStackTrace();}
		            }
		        };
		        ASMThread.start();
		        
		       
		        
		        
		       
		        
		        Thread ConsolidatedThread = new Thread()
		        {
		            public void run() {
		            	System.out.println("ConsolidatedThread");
		                try{
				// Consolidated
				
		      
		       new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_all, 0, false);
				///////////////System.out.println("cont.");
				new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_all_mtd, 0, true);


		//		new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_all, 0, false);
				new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_all_mtd, 0,true);
				
//				/new String[]{"shahrukh.salman@pbc.com.pk"},new String[]{"fahad.khalid@pbc.com.pk"},new String[]{"theia.notifications@pbc.com.pk"},
				 new com.pbc.pushmail.scorecards.NewKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath() + "/" + filename_KPI_Today, 0, false);
				 new com.pbc.pushmail.scorecards.NewKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath() + "/" + filename_KPI_MTD, 0, true);
		//        DistributorStockAnalysisExcel DistributorStockAnalysis = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
		//		DistributorStockAnalysis.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + stock_filename_distributor_all, 0);
		                	
		//         new com.pbc.pushmail.scorecards.DistributorNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale_all, 0);
				

//		         
				
		        //SendHTMLEmailMoiz("mazhar.iqbal@thalindustries.com,rizwan.jamil@thalindustries.com,asif.iqbal@moizfoods.com,zeeshan.akram@moizfoods.com,mazhar.iqbal@thalindustries.com", "theia.notifications@pbc.com.pk,shahrukh.salman@pbc.com.pk,ali.farhan@pbc.com.pk", "KPI | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_all,filename_distributor_all_mtd,stock_filename_distributor_all,filename_no_sale_all,filename_pjp_all,filename_pjp_all_mtd});
		         Utilities.sendPBCHTMLEmailExternalMoiz(new String[]{"mustajab.ali@moizfoods.com","zeshan.zafar@moizfoods.com","tahir.shahbaz@moizfoods.com","muhammad.usama@moizfoods.com"}, null,new String[]{"theia.notifications@pbc.com.pk"}, "Consolidated KPI | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_all,filename_distributor_all_mtd,stock_filename_distributor_all,filename_no_sale_all,filename_pjp_all,filename_pjp_all_mtd});
		           
		         
		       /*  Utilities.sendPBCHTMLEmailExternalMoiz(new String[]{"shahrukh.salman@pbc.com.pk"},new String[]{"fahad.khalid@pbc.com.pk"},new String[]{"theia.notifications@pbc.com.pk"}, "Consolidated KPI | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_KPI_Today,filename_KPI_MTD});*/
		        Utilities.sendPBCHTMLEmailExternalMoiz(new String[]{"tahir.shahbaz@moizfoods.com","zeshan.zafar@moizfoods.com","muhammad.atique@moizfoods.com","muhammad.usama@moizfoods.com"}, null,new String[]{"theia.notifications@pbc.com.pk"}, "Consolidated KPI | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_KPI_Today,filename_KPI_MTD});
		       //  Utilities.sendPBCHTMLEmailExternalMoiz(new String[]{"muhammad.usama@moizfoods.com","fahad.khalid@pbc.com.pk"}, null,new String[]{"theia.notifications@pbc.com.pk","shahrukh.salman@pbc.com.pk"}, "Consolidated KPI | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_new_KPI_Today,filename_new_KPI_MTD});

				/***********Commneted For Testing Purposes
				 *
				 * 
				 * 
				
				
				// SD1
				new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd1, EmployeeHierarchy.getSDHead(1).USER_ID, false);
				new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd1_mtd, EmployeeHierarchy.getSDHead(1).USER_ID, true);
				
				DistributorStockAnalysisExcel DistributorStockAnalysisSD1 = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
				DistributorStockAnalysisSD1.setSND(EmployeeHierarchy.getSDHead(1).USER_ID);
				DistributorStockAnalysisSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +stock_filename_distributor_sd1,0);

				//VolumeAnalysisPSRExcel va3 = new com.pbc.pushmail.scorecards.VolumeAnalysisPSRExcel();
				//va3.setSND(EmployeeHierarchy.getSDHead(1).USER_ID);
				//va3.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis_sd1,0);
				
				new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd1, EmployeeHierarchy.getSDHead(1).USER_ID, false);
				new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd1_mtd, EmployeeHierarchy.getSDHead(1).USER_ID,true);
				new com.pbc.pushmail.scorecards.DistributorNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale_sd1, EmployeeHierarchy.getSDHead(1).USER_ID);
				new com.pbc.pushmail.scorecards.DistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability_sd1, EmployeeHierarchy.getSDHead(1).USER_ID, true);
				
				//Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(1).USER_EMAIL},new String[]{"abdul.basit@pbc.com.pk","khurram.jaffar@pbc.com.pk","omerfk@pbc.com.pk","salman.baig@pbc.com.pk","anjum.a.ansari@gmail.com","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk","ahmad.raza@pbc.com.pk","saleem.sultan@pbc.com.pk"}, null, "KPI | SD1 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd1,filename_distributor_sd1_mtd,filename_pjp_sd1,filename_pjp_sd1_mtd,stock_filename_distributor_sd1,filename_no_sale_sd1,filename_sku_availability_sd1,filename_volume_analysis_sd1});
				//SD1 String
				//Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(1).USER_EMAIL},new String[]{"abdul.basit@pbc.com.pk","khurram.jaffar@pbc.com.pk","omerfk@pbc.com.pk","salman.baig@pbc.com.pk","anjum.a.ansari@gmail.com","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk","ahmad.raza@pbc.com.pk","saleem.sultan@pbc.com.pk","ayub.qureshi@pepsico.com","naeem.mumtaz@pbc.com.pk","sohaib.zahid@pbc.com.pk","allah.ditta@pbc.com.pk","kbadar@gmail.com","ihsan@pbc.com.pk"}, new String[]{"dev@pbc.com.pk"}, "KPI | SD1 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd1,filename_distributor_sd1_mtd,filename_pjp_sd1,filename_pjp_sd1_mtd,stock_filename_distributor_sd1,filename_no_sale_sd1,filename_sku_availability_sd1});
				Utilities.sendPBCHTMLEmail(new String[]{"ali.farhan@pbc.com.pk"},new String[]{"zulqurnan.aslam@pbc.com.pk"}, new String[]{""}, "KPI | SD1 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd2,filename_distributor_sd2_mtd,filename_pjp_sd2,filename_pjp_sd2_mtd,stock_filename_distributor_sd2,filename_no_sale_sd2,filename_sku_availability_sd2});
				
				*
				*
				*End of SD1
				**********************/
				/*
		         SendHTMLEmailMoiz("mazhar.iqbal@thalindustries.com,ahsan.afzaal@moizfoods.com,rizwan.jamil@thalindustries.com,asif.iqbal@almoiz.com,zeeshan.akram@moizfoods.com,mazhar.iqbal@thalindustries.com", "theia.notifications@pbc.com.pk,shahrukh.salman@pbc.com.pk,ali.farhan@pbc.com.pk", "KPI | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_all,filename_distributor_all_mtd,stock_filename_distributor_all,filename_no_sale_all,filename_pjp_all,filename_pjp_all_mtd});
		      */

				// SD2
				/*new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd2, EmployeeHierarchy.getSDHead(2).USER_ID, false);
				new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd2_mtd, EmployeeHierarchy.getSDHead(2).USER_ID, true);
				
				DistributorStockAnalysisExcel DistributorStockAnalysisSD2 = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
				DistributorStockAnalysisSD2.setSND(EmployeeHierarchy.getSDHead(2).USER_ID);
				DistributorStockAnalysisSD2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +stock_filename_distributor_sd2,0);
				
				new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd2, EmployeeHierarchy.getSDHead(2).USER_ID, false);
				new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd2_mtd, EmployeeHierarchy.getSDHead(2).USER_ID,true);
				new com.pbc.pushmail.scorecards.DistributorNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale_sd2, EmployeeHierarchy.getSDHead(2).USER_ID);
				new com.pbc.pushmail.scorecards.DistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability_sd2, EmployeeHierarchy.getSDHead(2).USER_ID, true);

//				VolumeAnalysisPSRExcel va4 = new com.pbc.pushmail.scorecards.VolumeAnalysisPSRExcel();
//				va4.setSND(EmployeeHierarchy.getSDHead(2).USER_ID);
//				va4.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis_sd2,0);
				
				//SD2 String
				//Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(2).USER_EMAIL},new String[]{"abdul.basit@pbc.com.pk","khurram.jaffar@pbc.com.pk","omerfk@pbc.com.pk","anjum.a.ansari@gmail.com","salman.baig@pbc.com.pk","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk","ahmad.raza@pbc.com.pk","ayub.qureshi@pepsico.com","naeem.mumtaz@pbc.com.pk","sohaib.zahid@pbc.com.pk","junaid.khalil@pbc.com.pk","allah.ditta@pbc.com.pk","kbadar@gmail.com","ihsan@pbc.com.pk"}, new String[]{"dev@pbc.com.pk"}, "KPI | SD2 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd2,filename_distributor_sd2_mtd,filename_pjp_sd2,filename_pjp_sd2_mtd,stock_filename_distributor_sd2,filename_no_sale_sd2,filename_sku_availability_sd2});
				
				Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(2).USER_EMAIL},new String[]{"ali.farhan@pbc.com.pk"}, new String[]{""}, "KPI | SD2 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd2,filename_distributor_sd2_mtd,filename_pjp_sd2,filename_pjp_sd2_mtd,stock_filename_distributor_sd2,filename_no_sale_sd2,filename_sku_availability_sd2});
				*/
				
				 /*Orginal String
		         SendHTMLEmailMoiz("mazhar.iqbal@thalindustries.com,ahsan.afzaal@moizfoods.com,rizwan.jamil@thalindustries.com,asif.iqbal@almoiz.com,zeeshan.akram@moizfoods.com,mazhar.iqbal@thalindustries.com", "theia.notifications@pbc.com.pk,shahrukh.salman@pbc.com.pk,ali.farhan@pbc.com.pk", "KPI | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_all,filename_distributor_all_mtd,stock_filename_distributor_all,filename_no_sale_all,filename_pjp_all,filename_pjp_all_mtd});
		      */
				
				
				
				
		                }catch(Exception e){e.printStackTrace();}
		            }
		        };
		      ConsolidatedThread.start();
		     
		        
		      
				

			}  catch (Exception e){
				e.printStackTrace();
			}
	 }
	 
	 
	
	
	
	public static void mailSD1(){
		// SD1
		System.out.println("SD1");
		  try{
				
				
				
				// SD1
				new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd1, 204201144, false);
				new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd1_mtd, 204201144, true);
				
				DistributorStockAnalysisExcel DistributorStockAnalysisSD1 = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
				DistributorStockAnalysisSD1.setSND(204201144);
				DistributorStockAnalysisSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +stock_filename_distributor_sd1,0);

				//VolumeAnalysisPSRExcel va3 = new com.pbc.pushmail.scorecards.VolumeAnalysisPSRExcel();
				//va3.setSND(EmployeeHierarchy.getSDHead(1).USER_ID);
				//va3.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis_sd1,0);
				
				/*
				 * 
				 * New Patch added on 16th of April*/
				String filename_SalesVsPackagesOutlets21 = "Outlet_Sales_MTD_SND_204201144_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
				new com.pbc.pushmail.scorecards.SalesVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_SalesVsPackagesOutlets21, 0, true, false,204201144);
				
				
				
				
				
				new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd1, 204201144, false);
				new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd1_mtd, 204201144,true);
				new com.pbc.pushmail.scorecards.DistributorNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale_sd1, 204201144);
			//	new com.pbc.pushmail.scorecards.DistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability_sd1, 204201144, true);
				
				
				Utilities.sendPBCHTMLEmailExternalMoiz(new String[]{"junaid.naveed@moizfoods.com","rizwan.jamil@thalindustries.com","mazhar.iqbal@thalindustries.com","theia.notifications@pbc.com.pk","shahrukh.salman@pbc.com.pk","zulqurnan.aslam@pbc.com.pk"},null,null, "KPI | Central Punjab Region | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd1,filename_distributor_sd1_mtd,filename_pjp_sd1,filename_pjp_sd1_mtd,stock_filename_distributor_sd1,filename_no_sale_sd1,filename_SalesVsPackagesOutlets21});
				//SendHTMLEmailMoiz("zulqurnan.aslam@pbc.com.pk","","shahrukh.salman@pbc.com.pk", "KPI | Central Punjab Region | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd1,filename_distributor_sd1_mtd,filename_pjp_sd1,filename_pjp_sd1_mtd,stock_filename_distributor_sd1,filename_no_sale_sd1,filename_SalesVsPackagesOutlets21});


			}  catch (Exception e){
				e.printStackTrace();
			}
	}
	
	
	public static void mailSD2(){
		// SD2
		System.out.println("SD2");
		  try{
				
				// SD2
				new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd2, 204180013, false);
				new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd2_mtd, 204180013, true);
				
				DistributorStockAnalysisExcel DistributorStockAnalysisSD2 = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
				DistributorStockAnalysisSD2.setSND(204180013);
				DistributorStockAnalysisSD2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +stock_filename_distributor_sd2,0);

				//VolumeAnalysisPSRExcel va3 = new com.pbc.pushmail.scorecards.VolumeAnalysisPSRExcel();
				//va3.setSND(EmployeeHierarchy.getSDHead(1).USER_ID);
				//va3.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis_sd1,0);
				
				
				/*
				 * 
				 * New Patch added on 16th of April*/
				
				String filename_SalesVsPackagesOutlets22 = "Outlet_Sales_MTD_SND_204180013_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
				new com.pbc.pushmail.scorecards.SalesVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_SalesVsPackagesOutlets22, 0, true, false,204180013);
				
				
				new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd2, 204180013, false);
				new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd2_mtd, 204180013,true);
				new com.pbc.pushmail.scorecards.DistributorNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale_sd2, 204180013);
			//	new com.pbc.pushmail.scorecards.DistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability_sd1, 204201144, true);
				
				//Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(1).USER_EMAIL},new String[]{"abdul.basit@pbc.com.pk","khurram.jaffar@pbc.com.pk","omerfk@pbc.com.pk","salman.baig@pbc.com.pk","anjum.a.ansari@gmail.com","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk","ahmad.raza@pbc.com.pk","saleem.sultan@pbc.com.pk"}, null, "KPI | SD1 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd1,filename_distributor_sd1_mtd,filename_pjp_sd1,filename_pjp_sd1_mtd,stock_filename_distributor_sd1,filename_no_sale_sd1,filename_sku_availability_sd1,filename_volume_analysis_sd1});
				//SD1 String
				//Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(1).USER_EMAIL},new String[]{"abdul.basit@pbc.com.pk","khurram.jaffar@pbc.com.pk","omerfk@pbc.com.pk","salman.baig@pbc.com.pk","anjum.a.ansari@gmail.com","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk","ahmad.raza@pbc.com.pk","saleem.sultan@pbc.com.pk","ayub.qureshi@pepsico.com","naeem.mumtaz@pbc.com.pk","sohaib.zahid@pbc.com.pk","allah.ditta@pbc.com.pk","kbadar@gmail.com","ihsan@pbc.com.pk"}, new String[]{"dev@pbc.com.pk"}, "KPI | SD1 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd1,filename_distributor_sd1_mtd,filename_pjp_sd1,filename_pjp_sd1_mtd,stock_filename_distributor_sd1,filename_no_sale_sd1,filename_sku_availability_sd1});
				
				Utilities.sendPBCHTMLEmailExternalMoiz(new String[]{"imran.shah@moizfoods.com","junaid.naveed@moizfoods.com","naik.muhammad@moizfoods.com","rizwan.jamil@thalindustries.com","mazhar.iqbal@thalindustries.com","theia.notifications@pbc.com.pk","shahrukh.salman@pbc.com.pk","zulqurnan.aslam@pbc.com.pk"},null,null, "KPI | KPK | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd2,filename_distributor_sd2_mtd,filename_pjp_sd2,filename_pjp_sd2_mtd,stock_filename_distributor_sd2,filename_no_sale_sd2,filename_SalesVsPackagesOutlets22});
			//	SendHTMLEmailMoiz("zulqurnan.aslam@pbc.com.pk","","shahrukh.salman@pbc.com.pk", "KPI | KPK Region | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd2,filename_distributor_sd2_mtd,filename_pjp_sd2,filename_pjp_sd2_mtd,stock_filename_distributor_sd2,filename_no_sale_sd2,filename_SalesVsPackagesOutlets22});
				

			}  catch (Exception e){
				e.printStackTrace();
			}
	}
	
	public static void mailSD3(){
		// SD3
		System.out.println("SD3");
		  try{
				
				
				
				// SD3
				new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd3, 20418008, false);
				new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd3_mtd, 20418008, true);
				
				DistributorStockAnalysisExcel DistributorStockAnalysisSD1 = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
				DistributorStockAnalysisSD1.setSND(201170008);
				DistributorStockAnalysisSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +stock_filename_distributor_sd3,0);

				//VolumeAnalysisPSRExcel va3 = new com.pbc.pushmail.scorecards.VolumeAnalysisPSRExcel();
				//va3.setSND(EmployeeHierarchy.getSDHead(1).USER_ID);
				//va3.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis_sd1,0);
				
				/*
				 * 
				 * New Patch added on 16th of April*/
				String filename_SalesVsPackagesOutlets21 = "Outlet_Sales_MTD_SND_201170008_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
				new com.pbc.pushmail.scorecards.SalesVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_SalesVsPackagesOutlets21, 0, true, false,20418008);
				
				
				
				
				new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd3, 20418008, false);
				new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd3_mtd, 20418008,true);
				new com.pbc.pushmail.scorecards.DistributorNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale_sd3, 20418008);
			//	new com.pbc.pushmail.scorecards.DistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability_sd1, 204201144, true);
				
				//SendHTMLEmailMoiz("zulqurnan.aslam@pbc.com.pk","", "shahrukh.salman@pbc.com.pk", "KPI | Aamir Razzaq | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd1,filename_distributor_sd1_mtd,filename_pjp_sd1,filename_pjp_sd1_mtd,stock_filename_distributor_sd1,filename_no_sale_sd1});
				
				//SendHTMLEmailMoiz("junaid.naveed@moizfoods.com","rizwan.jamil@thalindustries.com,asif.iqbal@moizfoods.com,mazhar.iqbal@thalindustries.com","theia.notifications@pbc.com.pk,shahrukh.salman@pbc.com.pk,zulqurnan.aslam@pbc.com.pk", "KPI | Mill Towns | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd3,filename_distributor_sd3_mtd,filename_pjp_sd3,filename_pjp_sd3_mtd,stock_filename_distributor_sd3,filename_no_sale_sd3,filename_SalesVsPackagesOutlets21});
				Utilities.sendPBCHTMLEmailExternalMoiz(new String[]{"bilal.nasir@moizfoods.com","junaid.naveed@moizfoods.com","rizwan.jamil@thalindustries.com","mazhar.iqbal@thalindustries.com","theia.notifications@pbc.com.pk","shahrukh.salman@pbc.com.pk","zulqurnan.aslam@pbc.com.pk"},null,null, "KPI | Mill Towns | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd3,filename_distributor_sd3_mtd,filename_pjp_sd3,filename_pjp_sd3_mtd,stock_filename_distributor_sd3,filename_no_sale_sd3,filename_SalesVsPackagesOutlets21});
				//SendHTMLEmailMoiz("zulqurnan.aslam@pbc.com.pk","","shahrukh.salman@pbc.com.pk", "KPI | Mill Towns | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd3,filename_distributor_sd3_mtd,filename_pjp_sd3,filename_pjp_sd3_mtd,stock_filename_distributor_sd3,filename_no_sale_sd3,filename_SalesVsPackagesOutlets21});


			}  catch (Exception e){
				e.printStackTrace();
			}
	}
	
	public static void mailSD4() {
		// SD4
		Datasource ds = new Datasource();
		
				System.out.println("SD4");
				  try{
						// SD4
					  
					  ds.createConnectionToReplica();
						Statement s = ds.createStatement();
						long sndId=0;
						//System.out.println("SELECT distinct(snd_id) as snd FROM pep.common_distributors where distributor_id in (3166,3179,3199,3222,3246,3317) and rsm_id=204200003");
					 ResultSet rsSND=s.executeQuery("SELECT distinct(snd_id) as snd FROM pep.common_distributors where distributor_id in (3166,3179,3199,3222,3246,3317) and rsm_id=204200003");
					  if(rsSND.first()) {
						  sndId=rsSND.getLong("snd");
					  }
						new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd4, sndId, false);
						new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd4_mtd, sndId, true);
						
						DistributorStockAnalysisExcel DistributorStockAnalysisSD1 = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
						DistributorStockAnalysisSD1.setSND(sndId);
						DistributorStockAnalysisSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +stock_filename_distributor_sd4,0);

						//VolumeAnalysisPSRExcel va3 = new com.pbc.pushmail.scorecards.VolumeAnalysisPSRExcel();
						//va3.setSND(EmployeeHierarchy.getSDHead(1).USER_ID);
						//va3.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis_sd1,0);
						
						/*
						 * 
						 * New Patch added on 16th of April*/
						String filename_SalesVsPackagesOutlets21 = "Outlet_Sales_MTD_SND_"+sndId+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
						new com.pbc.pushmail.scorecards.SalesVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_SalesVsPackagesOutlets21, 0, true, false,sndId);
						
						
						
						
						new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd4, sndId, false);
						new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd4_mtd, sndId,true);
						new com.pbc.pushmail.scorecards.DistributorNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale_sd4, sndId);
					//	new com.pbc.pushmail.scorecards.DistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability_sd1, 204201144, true);
						
						
						Utilities.sendPBCHTMLEmailExternalMoiz(new String[]{"feroz.shah@moizfoods.com","junaid.naveed@moizfoods.com","naik.muhammad@moizfoods.com","rizwan.jamil@thalindustries.com","mazhar.iqbal@thalindustries.com","theia.notifications@pbc.com.pk","shahrukh.salman@pbc.com.pk","zulqurnan.aslam@pbc.com.pk"},null,null, "KPI | Feroz Shah | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd4,filename_distributor_sd4_mtd,filename_pjp_sd4,filename_pjp_sd4_mtd,stock_filename_distributor_sd4,filename_no_sale_sd4,filename_SalesVsPackagesOutlets21});
						//SendHTMLEmailMoiz("zulqurnan.aslam@pbc.com.pk","","shahrukh.salman@pbc.com.pk", "KPI | Feroz Shah | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd4,filename_distributor_sd4_mtd,filename_pjp_sd4,filename_pjp_sd4_mtd,stock_filename_distributor_sd4,filename_no_sale_sd4,filename_SalesVsPackagesOutlets21});


					}  catch (Exception e){
						e.printStackTrace();
					}
	}
	
	public static String getHTMLMessage(long EmployeeID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		Datasource ds = new Datasource();
		ds.createConnection();
		
		
		Statement s = ds.createStatement();
		Statement s5 = ds.createStatement();
		Statement s2 = ds.createStatement();
		Statement s3 = ds.createStatement();
		
		
		
		
		///String html = "<html>";
		///html += "<body><br>";
				
			///html += "<br><br>";
				
			String html = "Please see attachments.";
			
			
		
		s.close();
		ds.dropConnection();	
		
		return html;
		
	}
	
	
	public static String getHTMLMessageTDM(long EmployeeID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		String html = "<html>";
		html += "<body><br>";
			html += "<table style='width: 450px'>";

			
			html += "<tr>";
			html += "<td colspan='2' style='text-align: left; font-size: 10px;'>&nbsp;</td>";
			html += "</tr>";
			
			html += "</table>";
				
			html += "<br><br>";
				
			html += "Please see attachment for details.<br>";
		
		return html;
		
	}
	
	
	static public void SendHTMLEmailMoiz(String to, String cc, String bcc, String subject, String message15, String[] filename) {
		// Recipient's email ID needs to be mentioned.
	     // String to = "zulqurnan.aslam@gmail.com";

	      // Sender's email ID needs to be mentioned
	      String from = "theia@moizfoods.com";

	      final String username = "theia@moizfoods.com";//change accordingly
	      final String password = "Theia@Pbc.987";//change accordingly

	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = "webmail.moizfoods.com";

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "false");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "587");

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
	            }
	         });

	      try {
	         // Create a default MimeMessage object.
	         Message message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.setRecipients(Message.RecipientType.TO,
	            InternetAddress.parse(to));
	         
	         
	         message.setRecipients(Message.RecipientType.CC,
	 	            InternetAddress.parse(cc));
	         
	         message.setRecipients(Message.RecipientType.BCC,
		 	            InternetAddress.parse(bcc));


	         // Set Subject: header field
	         message.setSubject(subject);

	         // Create the message part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Now set the actual message
	         messageBodyPart.setText(message15);

	         // Create a multipar message
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         
	         
	         for(int i=0;i<filename.length;i++) {
	        	 
	        	// Part two is attachment
		         messageBodyPart = new MimeBodyPart();
	        	 DataSource source = new FileDataSource(Utilities.getEmailAttachmentsPath()+"/"+filename[i]);
		         messageBodyPart.setDataHandler(new DataHandler(source));
		         messageBodyPart.setFileName(filename[i]);
		         multipart.addBodyPart(messageBodyPart);
	         }
	         
	        // String filename = "d://pbc/Outlet_Sales_MTD_SO_5004_FaisalIqbal_20181015.xlsx";
	        

	         // Send the complete message parts
	         message.setContent(multipart);

	         // Send message
	         Transport.send(message);

	         System.out.println("Sent message successfully....");
	  
	      } catch (MessagingException e) {
	         throw new RuntimeException(e);
	      }
	}

}
