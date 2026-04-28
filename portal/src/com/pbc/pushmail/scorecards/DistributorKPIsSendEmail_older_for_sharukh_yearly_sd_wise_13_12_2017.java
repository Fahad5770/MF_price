package com.pbc.pushmail.scorecards;

import java.sql.SQLException;

import com.pbc.common.User;
import com.pbc.employee.EmployeeHierarchy;
import com.pbc.util.Utilities;

import java.io.FileOutputStream;
import java.io.IOException;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

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
import com.pbc.util.UserAccess;
import com.pbc.util.Utilities;

public class DistributorKPIsSendEmail_older_for_sharukh_yearly_sd_wise_13_12_2017 {
	
	
	public static final String stock_filename_distributor_all = "Stock_Analysis_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String stock_filename_distributor_sd1 = "Stock_Analysis_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String stock_filename_distributor_sd2 = "Stock_Analysis_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String stock_filename_distributor_sd3 = "Stock_Analysis_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String stock_filename_distributor_sd4 = "Stock_Analysis_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static final String filename_distributor_all = "KPI_Dist_Today_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd1 = "KPI_Dist_Today_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd2 = "KPI_Dist_Today_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd3 = "KPI_Dist_Today_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd4 = "KPI_Dist_Today_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";

	
	public static final String filename_pjp_all = "KPI_PJP_Today_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd1 = "KPI_PJP_Today_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd2 = "KPI_PJP_Today_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd3 = "KPI_PJP_Today_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd4 = "KPI_PJP_Today_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static final String filename_pjp_all_mtd = "KPI_PJP_MTD_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd1_mtd = "KPI_PJP_MTD_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd2_mtd = "KPI_PJP_MTD_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd3_mtd = "KPI_PJP_MTD_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd4_mtd = "KPI_PJP_MTD_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";

	
	public static final String filename_distributor_all_mtd = "KPI_Dist_MTD_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd1_mtd = "KPI_Dist_MTD_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd2_mtd = "KPI_Dist_MTD_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd3_mtd = "KPI_Dist_MTD_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd4_mtd = "KPI_Dist_MTD_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static final String filename_no_sale_all = "No_Sale_Outlets_Today_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_no_sale_sd1 = "No_Sale_Outlets_Today_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_no_sale_sd2 = "No_Sale_Outlets_Today_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_no_sale_sd3 = "No_Sale_Outlets_Today_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_no_sale_sd4 = "No_Sale_Outlets_Today_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	
	public static final String filename_sku_availability_all = "SKU_Availability_MTD_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_sku_availability_sd1 = "SKU_Availability_MTD_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_sku_availability_sd2 = "SKU_Availability_MTD_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_sku_availability_sd3 = "SKU_Availability_MTD_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_sku_availability_sd4 = "SKU_Availability_MTD_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static final String filename_volume_analysis_all = "Volume_Analysis_Today_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_volume_analysis_sd1 = "Volume_Analysis_Today_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_volume_analysis_sd2 = "Volume_Analysis_Today_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_volume_analysis_sd3 = "Volume_Analysis_Today_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_volume_analysis_sd4 = "Volume_Analysis_Today_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static void main(String[] args) {
		
		try {
			
			//ASM
			
			/*
			Thread ASMThread = new Thread()
	        {
	            public void run() {
	                try{
	                
	                	
	            			
			Datasource ds = new Datasource();
			ds.createConnectionToReplica();
			Statement s = ds.createStatement();
			
			//ResultSet rs = s.executeQuery("select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email, (select email from users where id = dbp.asm_id) asm_email from distributor_beat_plan dbp where dbp.distributor_id in ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between from_days(to_days(curdate())-5) and curdate()) and asm_id is not null group by asm_id having ct = 1 and asm_id <= 3217");
			ResultSet rs = s.executeQuery("select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email, (select email from users where id = dbp.asm_id) asm_email from distributor_beat_plan dbp where dbp.distributor_id in ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between from_days(to_days(curdate())-6) and curdate()) and asm_id is not null group by asm_id having ct = 1 and asm_id<2409");
			
			while(rs.next()){
				
				
				try{
					long ASMID=rs.getLong(1);
					String ASMName = rs.getString(2);
					long DistributorID = rs.getLong(3);
					String TDMEmail = rs.getString("tdm_email");
					String SNDEmail = rs.getString("snd_email");
					String ASMEmail = rs.getString("asm_email");
					
					
					if (TDMEmail == null){
						TDMEmail = DistributorID+"@pbc.com.pk";
					}
					if (SNDEmail == null){
						SNDEmail = DistributorID+"@pbc.com.pk";
					}
					
					//System.out.println(DistributorID+" "+TDMEmail+" "+ASMID+" "+SNDEmail);
					
					String filename_pjp = "KPI_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_pjp_mtd = "KPI_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_no_sale = "No_Sale_Outlets_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_volume_analysis = "Volume_Analysis_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_sku_availability = "SKU_Availability_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_sku_availability1 = "SKU_Availability_PSR_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					String filename_SalesVsPackagesOutlets1 = "Outlet_Sales_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_SalesVsPackagesOutlets2 = "Outlet_Sales_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					String filename_BackOrdersOutlets1 = "Backorder_Outlets_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					
					
					
					new com.pbc.pushmail.scorecards.ASMKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp, ASMID, false);
					new com.pbc.pushmail.scorecards.ASMKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_mtd, ASMID, true);
					
					new com.pbc.pushmail.scorecards.ASMNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale, ASMID, false);
					new com.pbc.pushmail.scorecards.ASMDistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability, ASMID, true);
					
					new com.pbc.pushmail.scorecards.ASMDistributorSKUSalesAnalysisExcel2().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability1, ASMID, true);
					
					ASMVolumeAnalysisPSRExcel va2 = new com.pbc.pushmail.scorecards.ASMVolumeAnalysisPSRExcel();
					va2.setSND(ASMID);
					va2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis,0);
					
					
					
					//Outlet Sales added
					
					new com.pbc.pushmail.scorecards.SalesVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_SalesVsPackagesOutlets1, ASMID, false);
					new com.pbc.pushmail.scorecards.SalesVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_SalesVsPackagesOutlets2, ASMID, true);
					
					
					////////////////////
					
					
					//Backorder added
					
					
					new com.pbc.pushmail.scorecards.BackOrderVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_BackOrdersOutlets1, ASMID, false);
					
					
					////////////////////
					

					Utilities.sendPBCHTMLEmail(new String[]{DistributorID+"@pbc.com.pk"},new String[]{"abdul.basit@pbc.com.pk","salman.baig@pbc.com.pk","zahoor@pbc.com.pk",TDMEmail,SNDEmail,ASMEmail,"shahrukh.salman@pbc.com.pk","omerfk@pbc.com.pk","anas.wahab@pbc.com.pk","asad.ali@pbc.com.pk","kbadar@gmail.com","jazeb@pbc.com.pk","ahmad.raza@pbc.com.pk","naeem.mumtaz@pbc.com.pk","allah.ditta@pbc.com.pk","hunain.ahmad@pbc.com.pk"}, new String[]{"dev@pbc.com.pk"}, "KPI | MDE | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale,filename_volume_analysis,filename_sku_availability,filename_sku_availability1,filename_SalesVsPackagesOutlets1,filename_SalesVsPackagesOutlets2,filename_BackOrdersOutlets1});
					//Utilities.sendPBCHTMLEmail(new String[]{"anas.wahab@pbc.com.pk"},null, null, "KPI | MDE | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale,filename_volume_analysis,filename_sku_availability,filename_sku_availability1});
					//break;
					
					
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
	        
	        
			Thread ASMThread2 = new Thread()
	        {
	            public void run() {
	                try{
	               
	                	
	            			
			Datasource ds = new Datasource();
			ds.createConnectionToReplica();
			Statement s = ds.createStatement();
			
			//ResultSet rs = s.executeQuery("select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email,(select email from users where id = dbp.asm_id) asm_email from distributor_beat_plan dbp where dbp.distributor_id in ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between from_days(to_days(curdate())-5) and curdate()) and asm_id is not null group by asm_id having ct = 1 and asm_id > 3217");
			ResultSet rs = s.executeQuery("select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email, (select email from users where id = dbp.asm_id) asm_email from distributor_beat_plan dbp where dbp.distributor_id in ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between from_days(to_days(curdate())-6) and curdate()) and asm_id is not null group by asm_id having ct = 1 and asm_id>=2409 and asm_id<2774");
			
			while(rs.next()){
				
				
				try{
					long ASMID=rs.getLong(1);
					String ASMName = rs.getString(2);
					long DistributorID = rs.getLong(3);
					String TDMEmail = rs.getString("tdm_email");
					String SNDEmail = rs.getString("snd_email");
					String ASMEmail = rs.getString("asm_email");
					
					
					if (TDMEmail == null){
						TDMEmail = DistributorID+"@pbc.com.pk";
					}
					if (SNDEmail == null){
						SNDEmail = DistributorID+"@pbc.com.pk";
					}
					
					//System.out.println(DistributorID+" "+TDMEmail+" "+ASMID+" "+SNDEmail);
					
					String filename_pjp = "KPI_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_pjp_mtd = "KPI_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_no_sale = "No_Sale_Outlets_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_volume_analysis = "Volume_Analysis_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_sku_availability = "SKU_Availability_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_sku_availability1 = "SKU_Availability_PSR_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					
					String filename_SalesVsPackagesOutlets1 = "Outlet_Sales_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_SalesVsPackagesOutlets2 = "Outlet_Sales_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					String filename_BackOrdersOutlets1 = "Backorder_Outlets_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					
					new com.pbc.pushmail.scorecards.ASMKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp, ASMID, false);
					new com.pbc.pushmail.scorecards.ASMKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_mtd, ASMID, true);
					
					new com.pbc.pushmail.scorecards.ASMNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale, ASMID, false);
					new com.pbc.pushmail.scorecards.ASMDistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability, ASMID, true);
					
					new com.pbc.pushmail.scorecards.ASMDistributorSKUSalesAnalysisExcel2().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability1, ASMID, true);
					
					ASMVolumeAnalysisPSRExcel va2 = new com.pbc.pushmail.scorecards.ASMVolumeAnalysisPSRExcel();
					va2.setSND(ASMID);
					va2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis,0);
					
					
					//Outlet Sales added
					
					new com.pbc.pushmail.scorecards.SalesVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_SalesVsPackagesOutlets1, ASMID, false);
					new com.pbc.pushmail.scorecards.SalesVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_SalesVsPackagesOutlets2, ASMID, true);
					
					
					////////////////////
					
					

					//Backorder added
					
					
					new com.pbc.pushmail.scorecards.BackOrderVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_BackOrdersOutlets1, ASMID, false);
					
					
					////////////////////
					
					

					Utilities.sendPBCHTMLEmail(new String[]{DistributorID+"@pbc.com.pk"},new String[]{"abdul.basit@pbc.com.pk","salman.baig@pbc.com.pk","zahoor@pbc.com.pk",TDMEmail,SNDEmail,ASMEmail,"shahrukh.salman@pbc.com.pk","omerfk@pbc.com.pk","anas.wahab@pbc.com.pk","asad.ali@pbc.com.pk","kbadar@gmail.com","jazeb@pbc.com.pk","ahmad.raza@pbc.com.pk","naeem.mumtaz@pbc.com.pk","allah.ditta@pbc.com.pk","hunain.ahmad@pbc.com.pk"}, new String[]{"dev@pbc.com.pk"}, "KPI | MDE | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale,filename_volume_analysis,filename_sku_availability,filename_sku_availability1,filename_SalesVsPackagesOutlets1,filename_SalesVsPackagesOutlets2,filename_BackOrdersOutlets1});
					//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "KPI | MDE | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale,filename_volume_analysis,filename_sku_availability,filename_sku_availability1});
					//break;
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}			
			
			ds.dropConnection();
	                }catch(Exception e){e.printStackTrace();}
	            }
	        };
	        ASMThread2.start();	   
	        
	        
	        
	        
	        Thread ASMThread3 = new Thread()
	        {
	            public void run() {
	                try{
	                	
	                	
	            			
			Datasource ds = new Datasource();
			ds.createConnectionToReplica();
			Statement s = ds.createStatement();
			
			//ResultSet rs = s.executeQuery("select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email,(select email from users where id = dbp.asm_id) asm_email from distributor_beat_plan dbp where dbp.distributor_id in ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between from_days(to_days(curdate())-5) and curdate()) and asm_id is not null group by asm_id having ct = 1 and asm_id > 3217");
			ResultSet rs = s.executeQuery("select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email, (select email from users where id = dbp.asm_id) asm_email from distributor_beat_plan dbp where dbp.distributor_id in ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between from_days(to_days(curdate())-6) and curdate()) and asm_id is not null group by asm_id having ct = 1 and asm_id>=2774 and asm_id<3142");
			
			while(rs.next()){
				
				
				try{
					long ASMID=rs.getLong(1);
					String ASMName = rs.getString(2);
					long DistributorID = rs.getLong(3);
					String TDMEmail = rs.getString("tdm_email");
					String SNDEmail = rs.getString("snd_email");
					String ASMEmail = rs.getString("asm_email");
					
					
					if (TDMEmail == null){
						TDMEmail = DistributorID+"@pbc.com.pk";
					}
					if (SNDEmail == null){
						SNDEmail = DistributorID+"@pbc.com.pk";
					}
					
					//System.out.println(DistributorID+" "+TDMEmail+" "+ASMID+" "+SNDEmail);
					
					String filename_pjp = "KPI_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_pjp_mtd = "KPI_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_no_sale = "No_Sale_Outlets_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_volume_analysis = "Volume_Analysis_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_sku_availability = "SKU_Availability_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_sku_availability1 = "SKU_Availability_PSR_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					
					String filename_SalesVsPackagesOutlets1 = "Outlet_Sales_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_SalesVsPackagesOutlets2 = "Outlet_Sales_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					String filename_BackOrdersOutlets1 = "Backorder_Outlets_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					
					new com.pbc.pushmail.scorecards.ASMKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp, ASMID, false);
					new com.pbc.pushmail.scorecards.ASMKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_mtd, ASMID, true);
					
					new com.pbc.pushmail.scorecards.ASMNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale, ASMID, false);
					new com.pbc.pushmail.scorecards.ASMDistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability, ASMID, true);
					
					new com.pbc.pushmail.scorecards.ASMDistributorSKUSalesAnalysisExcel2().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability1, ASMID, true);
					
					ASMVolumeAnalysisPSRExcel va2 = new com.pbc.pushmail.scorecards.ASMVolumeAnalysisPSRExcel();
					va2.setSND(ASMID);
					va2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis,0);
					
					
					//Outlet Sales added
					
					new com.pbc.pushmail.scorecards.SalesVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_SalesVsPackagesOutlets1, ASMID, false);
					new com.pbc.pushmail.scorecards.SalesVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_SalesVsPackagesOutlets2, ASMID, true);
					
					
					////////////////////
					
					

					//Backorder added
					
					
					new com.pbc.pushmail.scorecards.BackOrderVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_BackOrdersOutlets1, ASMID, false);
					
					
					////////////////////
					
					

					Utilities.sendPBCHTMLEmail(new String[]{DistributorID+"@pbc.com.pk"},new String[]{"abdul.basit@pbc.com.pk","salman.baig@pbc.com.pk","zahoor@pbc.com.pk",TDMEmail,SNDEmail,ASMEmail,"shahrukh.salman@pbc.com.pk","omerfk@pbc.com.pk","anas.wahab@pbc.com.pk","asad.ali@pbc.com.pk","kbadar@gmail.com","jazeb@pbc.com.pk","ahmad.raza@pbc.com.pk","naeem.mumtaz@pbc.com.pk","allah.ditta@pbc.com.pk","hunain.ahmad@pbc.com.pk"}, new String[]{"dev@pbc.com.pk"}, "KPI | MDE | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale,filename_volume_analysis,filename_sku_availability,filename_sku_availability1,filename_SalesVsPackagesOutlets1,filename_SalesVsPackagesOutlets2,filename_BackOrdersOutlets1});
					//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "KPI | MDE | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale,filename_volume_analysis,filename_sku_availability,filename_sku_availability1});
					//break;
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}			
			
			ds.dropConnection();
	                }catch(Exception e){e.printStackTrace();}
	            }
	        };
	        ASMThread3.start();	   
	        
	        
	        
	        
	        
	        Thread ASMThread4 = new Thread()
	        {
	            public void run() {
	                try{
	                	
	            
	                	
	            			
			Datasource ds = new Datasource();
			ds.createConnectionToReplica();
			Statement s = ds.createStatement();
			
			//ResultSet rs = s.executeQuery("select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email,(select email from users where id = dbp.asm_id) asm_email from distributor_beat_plan dbp where dbp.distributor_id in ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between from_days(to_days(curdate())-5) and curdate()) and asm_id is not null group by asm_id having ct = 1 and asm_id > 3217");
			ResultSet rs = s.executeQuery("select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email, (select email from users where id = dbp.asm_id) asm_email from distributor_beat_plan dbp where dbp.distributor_id in ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between from_days(to_days(curdate())-6) and curdate()) and asm_id is not null group by asm_id having ct = 1 and asm_id>=3142 and asm_id<3589");
			
			while(rs.next()){
				
				try{
					long ASMID=rs.getLong(1);
					String ASMName = rs.getString(2);
					long DistributorID = rs.getLong(3);
					String TDMEmail = rs.getString("tdm_email");
					String SNDEmail = rs.getString("snd_email");
					String ASMEmail = rs.getString("asm_email");
					
					
					if (TDMEmail == null){
						TDMEmail = DistributorID+"@pbc.com.pk";
					}
					if (SNDEmail == null){
						SNDEmail = DistributorID+"@pbc.com.pk";
					}
					
					//System.out.println(DistributorID+" "+TDMEmail+" "+ASMID+" "+SNDEmail);
					
					String filename_pjp = "KPI_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_pjp_mtd = "KPI_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_no_sale = "No_Sale_Outlets_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_volume_analysis = "Volume_Analysis_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_sku_availability = "SKU_Availability_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_sku_availability1 = "SKU_Availability_PSR_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					
					String filename_SalesVsPackagesOutlets1 = "Outlet_Sales_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_SalesVsPackagesOutlets2 = "Outlet_Sales_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					String filename_BackOrdersOutlets1 = "Backorder_Outlets_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					
					new com.pbc.pushmail.scorecards.ASMKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp, ASMID, false);
					new com.pbc.pushmail.scorecards.ASMKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_mtd, ASMID, true);
					
					new com.pbc.pushmail.scorecards.ASMNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale, ASMID, false);
					new com.pbc.pushmail.scorecards.ASMDistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability, ASMID, true);
					
					new com.pbc.pushmail.scorecards.ASMDistributorSKUSalesAnalysisExcel2().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability1, ASMID, true);
					
					ASMVolumeAnalysisPSRExcel va2 = new com.pbc.pushmail.scorecards.ASMVolumeAnalysisPSRExcel();
					va2.setSND(ASMID);
					va2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis,0);
					
					
					//Outlet Sales added
					
					new com.pbc.pushmail.scorecards.SalesVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_SalesVsPackagesOutlets1, ASMID, false);
					new com.pbc.pushmail.scorecards.SalesVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_SalesVsPackagesOutlets2, ASMID, true);
					
					
					////////////////////
					
					

					//Backorder added
					
					
					new com.pbc.pushmail.scorecards.BackOrderVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_BackOrdersOutlets1, ASMID, false);
					
					
					////////////////////
					
					

					Utilities.sendPBCHTMLEmail(new String[]{DistributorID+"@pbc.com.pk"},new String[]{"abdul.basit@pbc.com.pk","salman.baig@pbc.com.pk","zahoor@pbc.com.pk",TDMEmail,SNDEmail,ASMEmail,"shahrukh.salman@pbc.com.pk","omerfk@pbc.com.pk","anas.wahab@pbc.com.pk","asad.ali@pbc.com.pk","kbadar@gmail.com","jazeb@pbc.com.pk","ahmad.raza@pbc.com.pk","naeem.mumtaz@pbc.com.pk","allah.ditta@pbc.com.pk","hunain.ahmad@pbc.com.pk"}, new String[]{"dev@pbc.com.pk"}, "KPI | MDE | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale,filename_volume_analysis,filename_sku_availability,filename_sku_availability1,filename_SalesVsPackagesOutlets1,filename_SalesVsPackagesOutlets2,filename_BackOrdersOutlets1});
					//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "KPI | MDE | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale,filename_volume_analysis,filename_sku_availability,filename_sku_availability1});
					//break;
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}			
			
			ds.dropConnection();
	                }catch(Exception e){e.printStackTrace();}
	            }
	        };
	        ASMThread4.start();	
	        
	        
	        
	        Thread ASMThread5 = new Thread()
	        {
	            public void run() {
	                try{
	          
	                	
	            			
			Datasource ds = new Datasource();
			ds.createConnectionToReplica();
			Statement s = ds.createStatement();
			
			//ResultSet rs = s.executeQuery("select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email,(select email from users where id = dbp.asm_id) asm_email from distributor_beat_plan dbp where dbp.distributor_id in ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between from_days(to_days(curdate())-5) and curdate()) and asm_id is not null group by asm_id having ct = 1 and asm_id > 3217");
			ResultSet rs = s.executeQuery("select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email, (select email from users where id = dbp.asm_id) asm_email from distributor_beat_plan dbp where dbp.distributor_id in ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between from_days(to_days(curdate())-6) and curdate()) and asm_id is not null group by asm_id having ct = 1 and asm_id>=3589 and asm_id<3684");
			
			while(rs.next()){
				
				try{
					long ASMID=rs.getLong(1);
					String ASMName = rs.getString(2);
					long DistributorID = rs.getLong(3);
					String TDMEmail = rs.getString("tdm_email");
					String SNDEmail = rs.getString("snd_email");
					String ASMEmail = rs.getString("asm_email");
					
					
					if (TDMEmail == null){
						TDMEmail = DistributorID+"@pbc.com.pk";
					}
					if (SNDEmail == null){
						SNDEmail = DistributorID+"@pbc.com.pk";
					}
					
					//System.out.println(DistributorID+" "+TDMEmail+" "+ASMID+" "+SNDEmail);
					
					String filename_pjp = "KPI_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_pjp_mtd = "KPI_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_no_sale = "No_Sale_Outlets_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_volume_analysis = "Volume_Analysis_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_sku_availability = "SKU_Availability_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_sku_availability1 = "SKU_Availability_PSR_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					
					String filename_SalesVsPackagesOutlets1 = "Outlet_Sales_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_SalesVsPackagesOutlets2 = "Outlet_Sales_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					String filename_BackOrdersOutlets1 = "Backorder_Outlets_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					
					new com.pbc.pushmail.scorecards.ASMKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp, ASMID, false);
					new com.pbc.pushmail.scorecards.ASMKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_mtd, ASMID, true);
					
					new com.pbc.pushmail.scorecards.ASMNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale, ASMID, false);
					new com.pbc.pushmail.scorecards.ASMDistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability, ASMID, true);
					
					new com.pbc.pushmail.scorecards.ASMDistributorSKUSalesAnalysisExcel2().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability1, ASMID, true);
					
					ASMVolumeAnalysisPSRExcel va2 = new com.pbc.pushmail.scorecards.ASMVolumeAnalysisPSRExcel();
					va2.setSND(ASMID);
					va2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis,0);
					
					
					//Outlet Sales added
					
					new com.pbc.pushmail.scorecards.SalesVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_SalesVsPackagesOutlets1, ASMID, false);
					new com.pbc.pushmail.scorecards.SalesVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_SalesVsPackagesOutlets2, ASMID, true);
					
					
					////////////////////
					
					

					//Backorder added
					
					
					new com.pbc.pushmail.scorecards.BackOrderVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_BackOrdersOutlets1, ASMID, false);
					
					
					////////////////////
					
					

					Utilities.sendPBCHTMLEmail(new String[]{DistributorID+"@pbc.com.pk"},new String[]{"abdul.basit@pbc.com.pk","salman.baig@pbc.com.pk","zahoor@pbc.com.pk",TDMEmail,SNDEmail,ASMEmail,"shahrukh.salman@pbc.com.pk","omerfk@pbc.com.pk","anas.wahab@pbc.com.pk","asad.ali@pbc.com.pk","kbadar@gmail.com","jazeb@pbc.com.pk","ahmad.raza@pbc.com.pk","naeem.mumtaz@pbc.com.pk","allah.ditta@pbc.com.pk","hunain.ahmad@pbc.com.pk"}, new String[]{"dev@pbc.com.pk"}, "KPI | MDE | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale,filename_volume_analysis,filename_sku_availability,filename_sku_availability1,filename_SalesVsPackagesOutlets1,filename_SalesVsPackagesOutlets2,filename_BackOrdersOutlets1});
					//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "KPI | MDE | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale,filename_volume_analysis,filename_sku_availability,filename_sku_availability1});
					//break;
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}			
			
			ds.dropConnection();
	                }catch(Exception e){e.printStackTrace();}
	            }
	        };
	        ASMThread5.start();	
	        
	        
	        Thread ASMThread6 = new Thread()
	        {
	            public void run() {
	                try{
	          
	                	
	            			
			Datasource ds = new Datasource();
			ds.createConnectionToReplica();
			Statement s = ds.createStatement();
			
			//ResultSet rs = s.executeQuery("select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email,(select email from users where id = dbp.asm_id) asm_email from distributor_beat_plan dbp where dbp.distributor_id in ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between from_days(to_days(curdate())-5) and curdate()) and asm_id is not null group by asm_id having ct = 1 and asm_id > 3217");
			ResultSet rs = s.executeQuery("select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email, (select email from users where id = dbp.asm_id) asm_email from distributor_beat_plan dbp where dbp.distributor_id in ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between from_days(to_days(curdate())-6) and curdate()) and asm_id is not null group by asm_id having ct = 1 and asm_id>=3684");
			
			
			while(rs.next()){
				
				try{
					long ASMID=rs.getLong(1);
					String ASMName = rs.getString(2);
					long DistributorID = rs.getLong(3);
					String TDMEmail = rs.getString("tdm_email");
					String SNDEmail = rs.getString("snd_email");
					String ASMEmail = rs.getString("asm_email");
					
					
					if (TDMEmail == null){
						TDMEmail = DistributorID+"@pbc.com.pk";
					}
					if (SNDEmail == null){
						SNDEmail = DistributorID+"@pbc.com.pk";
					}
					
					//System.out.println(DistributorID+" "+TDMEmail+" "+ASMID+" "+SNDEmail);
					
					String filename_pjp = "KPI_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_pjp_mtd = "KPI_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_no_sale = "No_Sale_Outlets_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_volume_analysis = "Volume_Analysis_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_sku_availability = "SKU_Availability_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_sku_availability1 = "SKU_Availability_PSR_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					
					String filename_SalesVsPackagesOutlets1 = "Outlet_Sales_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_SalesVsPackagesOutlets2 = "Outlet_Sales_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					String filename_BackOrdersOutlets1 = "Backorder_Outlets_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					
					new com.pbc.pushmail.scorecards.ASMKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp, ASMID, false);
					new com.pbc.pushmail.scorecards.ASMKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_mtd, ASMID, true);
					
					new com.pbc.pushmail.scorecards.ASMNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale, ASMID, false);
					new com.pbc.pushmail.scorecards.ASMDistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability, ASMID, true);
					
					new com.pbc.pushmail.scorecards.ASMDistributorSKUSalesAnalysisExcel2().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability1, ASMID, true);
					
					ASMVolumeAnalysisPSRExcel va2 = new com.pbc.pushmail.scorecards.ASMVolumeAnalysisPSRExcel();
					va2.setSND(ASMID);
					va2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis,0);
					
					
					//Outlet Sales added
					
					new com.pbc.pushmail.scorecards.SalesVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_SalesVsPackagesOutlets1, ASMID, false);
					new com.pbc.pushmail.scorecards.SalesVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_SalesVsPackagesOutlets2, ASMID, true);
					
					
					////////////////////
					
					

					//Backorder added
					
					
					new com.pbc.pushmail.scorecards.BackOrderVsPackagesOutletAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_BackOrdersOutlets1, ASMID, false);
					
					
					////////////////////
					
					

					Utilities.sendPBCHTMLEmail(new String[]{DistributorID+"@pbc.com.pk"},new String[]{"abdul.basit@pbc.com.pk","salman.baig@pbc.com.pk","zahoor@pbc.com.pk",TDMEmail,SNDEmail,ASMEmail,"shahrukh.salman@pbc.com.pk","omerfk@pbc.com.pk","anas.wahab@pbc.com.pk","asad.ali@pbc.com.pk","kbadar@gmail.com","jazeb@pbc.com.pk","ahmad.raza@pbc.com.pk","naeem.mumtaz@pbc.com.pk","allah.ditta@pbc.com.pk","hunain.ahmad@pbc.com.pk"}, new String[]{"dev@pbc.com.pk"}, "KPI | MDE | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale,filename_volume_analysis,filename_sku_availability,filename_sku_availability1,filename_SalesVsPackagesOutlets1,filename_SalesVsPackagesOutlets2,filename_BackOrdersOutlets1});
					//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "KPI | MDE | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale,filename_volume_analysis,filename_sku_availability,filename_sku_availability1});
					//break;
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}			
			
			ds.dropConnection();
	                }catch(Exception e){e.printStackTrace();}
	            }
	        };
	        ASMThread6.start();	
	        
	       */
	        
	        
	        
	        Thread ConsolidatedThread = new Thread()
	        {
	            public void run() {
	                try{
			// Consolidated
			
	       /* 
	        new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_all, 0, false);
			System.out.println("cont.");
			new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_all_mtd, 0, true);
			
			DistributorStockAnalysisExcel DistributorStockAnalysis = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
			DistributorStockAnalysis.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + stock_filename_distributor_all, 0);
			
			new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_all, 0, false);
			new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_all_mtd, 0,true);
			new com.pbc.pushmail.scorecards.DistributorNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale_all, 0);
			new com.pbc.pushmail.scorecards.DistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability_all, 0, true);

			VolumeAnalysisPSRExcel va = new com.pbc.pushmail.scorecards.VolumeAnalysisPSRExcel();
			va.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis_all,0);
			
			Utilities.sendPBCHTMLEmail(new String[]{"salman.baig@pbc.com.pk","zahoor@pbc.com.pk"},new String[]{"abdul.basit@pbc.com.pk","omerfk@pbc.com.pk","anjum.a.ansari@gmail.com","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk","ahmad.raza@pbc.com.pk","ayub.qureshi@pepsico.com","naeem.mumtaz@pbc.com.pk","sohaib.zahid@pbc.com.pk","allah.ditta@pbc.com.pk","kbadar@gmail.com"}, new String[]{"dev@pbc.com.pk"}, "KPI | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_all,filename_distributor_all_mtd,filename_pjp_all,filename_pjp_all_mtd,stock_filename_distributor_all,filename_no_sale_all,filename_sku_availability_all,filename_volume_analysis_all});
			//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "KPI | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_all,filename_distributor_all_mtd,filename_pjp_all,stock_filename_distributor_all,filename_no_sale_all,filename_sku_availability_all,filename_volume_analysis_all});
	                
	            */    
			
			
			
		/*	// SD1
			//new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd1, EmployeeHierarchy.getSDHead(1).USER_ID, false);
			new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd1_mtd, EmployeeHierarchy.getSDHead(1).USER_ID, true);
			
			//DistributorStockAnalysisExcel DistributorStockAnalysisSD1 = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
			//DistributorStockAnalysisSD1.setSND(EmployeeHierarchy.getSDHead(1).USER_ID);
			//DistributorStockAnalysisSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +stock_filename_distributor_sd1,0);

			//VolumeAnalysisPSRExcel va3 = new com.pbc.pushmail.scorecards.VolumeAnalysisPSRExcel();
			//va3.setSND(EmployeeHierarchy.getSDHead(1).USER_ID);
			//va3.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis_sd1,0);
			
			//new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd1, EmployeeHierarchy.getSDHead(1).USER_ID, false);
			//new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd1_mtd, EmployeeHierarchy.getSDHead(1).USER_ID,true);
			//new com.pbc.pushmail.scorecards.DistributorNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale_sd1, EmployeeHierarchy.getSDHead(1).USER_ID);
			//new com.pbc.pushmail.scorecards.DistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability_sd1, EmployeeHierarchy.getSDHead(1).USER_ID, true);
			
			//Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(1).USER_EMAIL},new String[]{"abdul.basit@pbc.com.pk","zahoor@pbc.com.pk","omerfk@pbc.com.pk","salman.baig@pbc.com.pk","anjum.a.ansari@gmail.com","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk","ahmad.raza@pbc.com.pk","saleem.sultan@pbc.com.pk"}, null, "KPI | SD1 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd1,filename_distributor_sd1_mtd,filename_pjp_sd1,filename_pjp_sd1_mtd,stock_filename_distributor_sd1,filename_no_sale_sd1,filename_sku_availability_sd1,filename_volume_analysis_sd1});
			Utilities.sendPBCHTMLEmail(new String[]{"zulqurnan.aslam@pbc.com.pk","shahrukh.salman@pbc.com.pk"},null, null, "KPI | SD1 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd1_mtd});
	*/		

			// SD2
			//new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd2, EmployeeHierarchy.getSDHead(2).USER_ID, false);
			new com.pbc.pushmail.scorecards.DistributorKPIsExcel_older_for_sharukh_yearly_sd_wise_13_12_2017().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd2_mtd, EmployeeHierarchy.getSDHead(2).USER_ID, true);
			
			//DistributorStockAnalysisExcel DistributorStockAnalysisSD2 = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
			//DistributorStockAnalysisSD2.setSND(EmployeeHierarchy.getSDHead(2).USER_ID);
			//DistributorStockAnalysisSD2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +stock_filename_distributor_sd2,0);
			
			//new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd2, EmployeeHierarchy.getSDHead(2).USER_ID, false);
			//new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd2_mtd, EmployeeHierarchy.getSDHead(2).USER_ID,true);
			//new com.pbc.pushmail.scorecards.DistributorNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale_sd2, EmployeeHierarchy.getSDHead(2).USER_ID);
			//new com.pbc.pushmail.scorecards.DistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability_sd2, EmployeeHierarchy.getSDHead(2).USER_ID, true);

//			VolumeAnalysisPSRExcel va4 = new com.pbc.pushmail.scorecards.VolumeAnalysisPSRExcel();
//			va4.setSND(EmployeeHierarchy.getSDHead(2).USER_ID);
//			va4.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis_sd2,0);
			
			//Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(2).USER_EMAIL},new String[]{"abdul.basit@pbc.com.pk","zahoor@pbc.com.pk","omerfk@pbc.com.pk","anjum.a.ansari@gmail.com","salman.baig@pbc.com.pk","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk","ahmad.raza@pbc.com.pk","muhammad.sabir@pbc.com.pk"}, null, "KPI | SD2 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd2,filename_distributor_sd2_mtd,filename_pjp_sd2,filename_pjp_sd2_mtd,stock_filename_distributor_sd2,filename_no_sale_sd2,filename_sku_availability_sd2,filename_volume_analysis_sd2});
			Utilities.sendPBCHTMLEmail(new String[]{"zulqurnan.aslam@pbc.com.pk","shahrukh.salman@pbc.com.pk"},null, null, "KPI | SD2 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd2_mtd});
	/*		
			
			// SD3
			new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd3, EmployeeHierarchy.getSDHead(4).USER_ID, false);
			new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd3_mtd, EmployeeHierarchy.getSDHead(4).USER_ID, true);
			
			DistributorStockAnalysisExcel DistributorStockAnalysisSD3 = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
			DistributorStockAnalysisSD3.setSND(EmployeeHierarchy.getSDHead(4).USER_ID);
			DistributorStockAnalysisSD3.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +stock_filename_distributor_sd3,0);
			
			new com.pbc.pushmail.scorecards.DistributorNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale_sd3, EmployeeHierarchy.getSDHead(4).USER_ID);
			new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd3, EmployeeHierarchy.getSDHead(4).USER_ID, false);
			new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd3_mtd, EmployeeHierarchy.getSDHead(4).USER_ID,true);
			new com.pbc.pushmail.scorecards.DistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability_sd3, EmployeeHierarchy.getSDHead(4).USER_ID, true);
			
//			VolumeAnalysisPSRExcel va5 = new com.pbc.pushmail.scorecards.VolumeAnalysisPSRExcel();
//			va5.setSND(EmployeeHierarchy.getSDHead(4).USER_ID);
//			va5.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis_sd3,0);
			
			//Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(4).USER_EMAIL},new String[]{"abdul.basit@pbc.com.pk","zahoor@pbc.com.pk","omerfk@pbc.com.pk","anjum.a.ansari@gmail.com","salman.baig@pbc.com.pk","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk","ahmad.raza@pbc.com.pk"}, null, "KPI | SD3 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd3,filename_distributor_sd3_mtd,filename_pjp_sd3,filename_pjp_sd3_mtd,stock_filename_distributor_sd3,filename_no_sale_sd3,filename_sku_availability_sd3,filename_volume_analysis_sd3});
			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(4).USER_EMAIL},new String[]{"abdul.basit@pbc.com.pk","zahoor@pbc.com.pk","omerfk@pbc.com.pk","anjum.a.ansari@gmail.com","salman.baig@pbc.com.pk","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk","ahmad.raza@pbc.com.pk","ayub.qureshi@pepsico.com","naeem.mumtaz@pbc.com.pk","sohaib.zahid@pbc.com.pk","allah.ditta@pbc.com.pk","kbadar@gmail.com"}, new String[]{"dev@pbc.com.pk"}, "KPI | SD3 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd3,filename_distributor_sd3_mtd,filename_pjp_sd3,filename_pjp_sd3_mtd,stock_filename_distributor_sd3,filename_no_sale_sd3,filename_sku_availability_sd3});
			
			
			
			
	                	// SD4
	        			new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd4, EmployeeHierarchy.getSDHead(6).USER_ID, false);
	        			new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd4_mtd, EmployeeHierarchy.getSDHead(6).USER_ID, true);
	        			
	        			DistributorStockAnalysisExcel DistributorStockAnalysisSD4 = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
	        			DistributorStockAnalysisSD4.setSND(EmployeeHierarchy.getSDHead(6).USER_ID);
	        			DistributorStockAnalysisSD4.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +stock_filename_distributor_sd4,0);
	        			
	        			new com.pbc.pushmail.scorecards.DistributorNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale_sd4, EmployeeHierarchy.getSDHead(6).USER_ID);
	        			new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd4, EmployeeHierarchy.getSDHead(6).USER_ID, false);
	        			new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd4_mtd, EmployeeHierarchy.getSDHead(6).USER_ID,true);
	        			new com.pbc.pushmail.scorecards.DistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability_sd4, EmployeeHierarchy.getSDHead(6).USER_ID, true);
	        			
//	        			VolumeAnalysisPSRExcel va6 = new com.pbc.pushmail.scorecards.VolumeAnalysisPSRExcel();
//	        			va6.setSND(EmployeeHierarchy.getSDHead(6).USER_ID);
//	        			va6.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis_sd4,0);
	        			
	        			//Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(6).USER_EMAIL},new String[]{"abdul.basit@pbc.com.pk","zahoor@pbc.com.pk","omerfk@pbc.com.pk","anjum.a.ansari@gmail.com","salman.baig@pbc.com.pk","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk","ahmad.raza@pbc.com.pk","ahmad.maqsood@pbc.com.pk"}, null, "KPI | SD4 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd4,filename_distributor_sd4_mtd,filename_pjp_sd4,filename_pjp_sd4_mtd,stock_filename_distributor_sd4,filename_no_sale_sd4,filename_sku_availability_sd4,filename_volume_analysis_sd4});
	        			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(6).USER_EMAIL},new String[]{"abdul.basit@pbc.com.pk","zahoor@pbc.com.pk","omerfk@pbc.com.pk","anjum.a.ansari@gmail.com","salman.baig@pbc.com.pk","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk","ahmad.raza@pbc.com.pk","ayub.qureshi@pepsico.com","naeem.mumtaz@pbc.com.pk","sohaib.zahid@pbc.com.pk","allah.ditta@pbc.com.pk","kbadar@gmail.com"}, new String[]{"dev@pbc.com.pk"}, "KPI | SD4 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd4,filename_distributor_sd4_mtd,filename_pjp_sd4,filename_pjp_sd4_mtd,stock_filename_distributor_sd4,filename_no_sale_sd4,filename_sku_availability_sd4});
	        	                
			*/
			
	                }catch(Exception e){e.printStackTrace();}
	            }
	        };
	        ConsolidatedThread.start();
	     
	        
	       /*
			User RSMs[] = EmployeeHierarchy.getRSMs();
			for (User user: RSMs){
				try{
					if (user.USER_EMAIL != null && user.USER_ID != EmployeeHierarchy.getSDHead(4).USER_ID){
						
						User SND = EmployeeHierarchy.getSND(user.USER_ID);
						
						String filename_distributor = "KPI_Dist_Today_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
						String filename_distributor_mtd = "KPI_Dist_MTD_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
						String filename_pjp = "KPI_PJP_Today_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
						String filename_pjp_mtd = "KPI_PJP_MTD_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
						String filename_stock = "Stock_Analysis_RSM_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
						
						new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor, user.USER_ID, false);
						new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_mtd, user.USER_ID, true);
						
						new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp, user.USER_ID, false);
						new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_mtd, user.USER_ID, true);

						DistributorStockAnalysisExcel dsa = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
						dsa.setSND(user.USER_ID);
						dsa.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_stock,0);
						//
//						Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "KPI | RSM | "+user.REGION_NAME+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(user.USER_ID), new String[]{filename_stock,filename_distributor,filename_distributor_mtd,filename_pjp,filename_pjp_mtd});
						Utilities.sendPBCHTMLEmail(new String[]{user.USER_EMAIL},new String[]{SND.USER_EMAIL, "abdul.basit@pbc.com.pk","shahrukh.salman@pbc.com.pk","omerfk@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","ahmad.raza@pbc.com.pk"}, null, "KPI | DDM | "+user.REGION_NAME+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(user.USER_ID), new String[]{filename_stock,filename_distributor,filename_distributor_mtd,filename_pjp,filename_pjp_mtd});
						
					}
				}catch(Exception e){e.printStackTrace();}
			}
			*/
		
	        
			/*
	        User TDMs[] = EmployeeHierarchy.getTDMs();
			for (User user: TDMs){
				try{
					if (user.USER_EMAIL != null){
						
						User RSM = EmployeeHierarchy.getRSMByTDM(user.USER_ID); 
						User SND = EmployeeHierarchy.getSND(RSM.USER_ID);
						
						if (RSM.USER_EMAIL == null){
							RSM.USER_EMAIL = SND.USER_EMAIL;
						}
						
						if (RSM.USER_EMAIL != null && SND.USER_EMAIL != null){
							
							
							String filename_distributor = "KPI_Dist_Today_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
							String filename_distributor_mtd = "KPI_Dist_MTD_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
							String filename_pjp = "KPI_PJP_Today_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
							String filename_pjp_mtd = "KPI_PJP_MTD_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
							String filename_stock = "Stock_Analysis_TDM_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
							String filename_no_sale = "No_Sale_Outlets_Today_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
							String filename_sku_availability = "SKU_Availability_MTD_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
							String filename_volume_analysis = "Volume_Analysis_Today_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
							
							new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor, user.USER_ID, false);
							new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_mtd, user.USER_ID, true);
							new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp, user.USER_ID, false);
							new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_mtd, user.USER_ID, true);
							
							DistributorStockAnalysisExcel dsa = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
							dsa.setSND(user.USER_ID);
							dsa.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_stock,0);
							
							new com.pbc.pushmail.scorecards.DistributorNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale, user.USER_ID);
							new com.pbc.pushmail.scorecards.DistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability, user.USER_ID, true);
							
							
							VolumeAnalysisPSRExcel va2 = new com.pbc.pushmail.scorecards.VolumeAnalysisPSRExcel();
							va2.setSND(user.USER_ID);
							va2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis,0);
							
							Utilities.sendPBCHTMLEmail(new String[]{user.USER_EMAIL},new String[]{SND.USER_EMAIL, "abdul.basit@pbc.com.pk","salman.baig@pbc.com.pk","zahoor@pbc.com.pk","shahrukh.salman@pbc.com.pk","omerfk@pbc.com.pk","anas.wahab@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk","ahmad.raza@pbc.com.pk","naeem.mumtaz@pbc.com.pk","allah.ditta@pbc.com.pk","kbadar@gmail.com"}, new String[]{"dev@pbc.com.pk"}, "KPI | Unit Manager | "+user.USER_DISPLAY_NAME+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(user.USER_ID), new String[]{filename_stock,filename_distributor,filename_distributor_mtd,filename_pjp,filename_pjp_mtd,filename_no_sale,filename_sku_availability,filename_volume_analysis});
							//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "KPI | Unit Manager | "+user.USER_DISPLAY_NAME+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(user.USER_ID), new String[]{filename_stock,filename_distributor,filename_distributor_mtd,filename_pjp,filename_pjp_mtd,filename_no_sale,filename_sku_availability,filename_volume_analysis});
							//break;
						}
						
						
					}
				}catch(Exception e){e.printStackTrace();}
			}*/
			

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
		
		
		
		
		String html = "<html>";
		html += "<body><br>";
				
			html += "<br><br>";
				
			html += "Please see attachments.";
			
			
		
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

}
