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

public class DistributorKPIsSendEmail______zul {
	
	
	public static final String stock_filename_distributor_all = "Stock_Analysis_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String stock_filename_distributor_sd1 = "Stock_Analysis_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String stock_filename_distributor_sd2 = "Stock_Analysis_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String stock_filename_distributor_sd3 = "Stock_Analysis_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static final String filename_distributor_all = "KPI_Dist_Today_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd1 = "KPI_Dist_Today_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd2 = "KPI_Dist_Today_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd3 = "KPI_Dist_Today_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";

	
	public static final String filename_pjp_all = "KPI_PJP_Today_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd1 = "KPI_PJP_Today_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd2 = "KPI_PJP_Today_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd3 = "KPI_PJP_Today_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static final String filename_pjp_all_mtd = "KPI_PJP_MTD_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd1_mtd = "KPI_PJP_MTD_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd2_mtd = "KPI_PJP_MTD_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd3_mtd = "KPI_PJP_MTD_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";

	
	public static final String filename_distributor_all_mtd = "KPI_Dist_MTD_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd1_mtd = "KPI_Dist_MTD_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd2_mtd = "KPI_Dist_MTD_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd3_mtd = "KPI_Dist_MTD_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static final String filename_no_sale_all = "No_Sale_Outlets_Today_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_no_sale_sd1 = "No_Sale_Outlets_Today_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_no_sale_sd2 = "No_Sale_Outlets_Today_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_no_sale_sd3 = "No_Sale_Outlets_Today_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	
	public static final String filename_sku_availability_all = "SKU_Availability_MTD_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_sku_availability_sd1 = "SKU_Availability_MTD_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_sku_availability_sd2 = "SKU_Availability_MTD_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_sku_availability_sd3 = "SKU_Availability_MTD_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static final String filename_volume_analysis_all = "Volume_Analysis_Today_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_volume_analysis_sd1 = "Volume_Analysis_Today_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_volume_analysis_sd2 = "Volume_Analysis_Today_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_volume_analysis_sd3 = "Volume_Analysis_Today_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static void main(String[] args) {
		
		try {
			
			//ASM
			Datasource ds = new Datasource();
			ds.createConnectionToReplica();
			Statement s = ds.createStatement();
			
			/*ResultSet rs = s.executeQuery("select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email from distributor_beat_plan dbp where dbp.distributor_id in ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between from_days(to_days(curdate())-5) and curdate()) and asm_id is not null group by asm_id having ct = 1");
			while(rs.next()){
				
				try{
					long ASMID=rs.getLong(1);
					String ASMName = rs.getString(2);
					long DistributorID = rs.getLong(3);
					String TDMEmail = rs.getString("tdm_email");
					String SNDEmail = rs.getString("snd_email");
					
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
					
					new com.pbc.pushmail.scorecards.ASMKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp, ASMID, false);
					new com.pbc.pushmail.scorecards.ASMKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_mtd, ASMID, true);
					
					new com.pbc.pushmail.scorecards.ASMNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale, ASMID, false);
					new com.pbc.pushmail.scorecards.ASMDistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability, ASMID, true);
					
					ASMVolumeAnalysisPSRExcel va2 = new com.pbc.pushmail.scorecards.ASMVolumeAnalysisPSRExcel();
					va2.setSND(ASMID);
					va2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis,0);
					
					

					Utilities.sendPBCHTMLEmail(new String[]{DistributorID+"@pbc.com.pk"},new String[]{"salman.baig@pbc.com.pk","zahoor@pbc.com.pk",TDMEmail,SNDEmail,"shahrukh.salman@pbc.com.pk","omerfk@pbc.com.pk","anas.wahab@pbc.com.pk","asad.ali@pbc.com.pk"}, null, "KPI | MDE | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale,filename_volume_analysis,filename_sku_availability});
					//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "KPI | MDE | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale,filename_volume_analysis,filename_sku_availability});
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}	*/		
			
			ds.dropConnection();
			
			/*
			// Consolidated
			new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_all, 0, false);
			new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_all_mtd, 0, true);
			
			DistributorStockAnalysisExcel DistributorStockAnalysis = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
			DistributorStockAnalysis.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + stock_filename_distributor_all, 0);
			
			new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_all, 0, false);
			new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_all_mtd, 0,true);
			new com.pbc.pushmail.scorecards.DistributorNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale_all, 0);
			new com.pbc.pushmail.scorecards.DistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability_all, 0, true);

			VolumeAnalysisPSRExcel va = new com.pbc.pushmail.scorecards.VolumeAnalysisPSRExcel();
			va.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis_all,0);
			
			Utilities.sendPBCHTMLEmail(new String[]{"salman.baig@pbc.com.pk","zahoor@pbc.com.pk"},new String[]{"omerfk@pbc.com.pk","anjum.a.ansari@gmail.com","ahsan.rashid@optimus-uae.com","muhammadameer@hotmail.com","rafiquemeo@yahoo.com","shehzadaleem@hotmail.com","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk"}, null, "KPI | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_all,filename_distributor_all_mtd,filename_pjp_all,filename_pjp_all_mtd,stock_filename_distributor_all,filename_no_sale_all,filename_sku_availability_all,filename_volume_analysis_all});
			//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "KPI | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_all,filename_distributor_all_mtd,filename_pjp_all,stock_filename_distributor_all,filename_no_sale_all,filename_sku_availability_all,filename_volume_analysis_all});
			
			
					
			
			// SD1
			new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd1, EmployeeHierarchy.getSDHead(1).USER_ID, false);
			new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd1_mtd, EmployeeHierarchy.getSDHead(1).USER_ID, true);
			
			DistributorStockAnalysisExcel DistributorStockAnalysisSD1 = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
			DistributorStockAnalysisSD1.setSND(EmployeeHierarchy.getSDHead(1).USER_ID);
			DistributorStockAnalysisSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +stock_filename_distributor_sd1,0);

			VolumeAnalysisPSRExcel va3 = new com.pbc.pushmail.scorecards.VolumeAnalysisPSRExcel();
			va3.setSND(EmployeeHierarchy.getSDHead(1).USER_ID);
			va3.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis_sd1,0);
			
			new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd1, EmployeeHierarchy.getSDHead(1).USER_ID, false);
			new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd1_mtd, EmployeeHierarchy.getSDHead(1).USER_ID,true);
			new com.pbc.pushmail.scorecards.DistributorNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale_sd1, EmployeeHierarchy.getSDHead(1).USER_ID);
			new com.pbc.pushmail.scorecards.DistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability_sd1, EmployeeHierarchy.getSDHead(1).USER_ID, true);
			
			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(1).USER_EMAIL},new String[]{"zahoor@pbc.com.pk","omerfk@pbc.com.pk","salman.baig@pbc.com.pk","anjum.a.ansari@gmail.com","muhammadameer@hotmail.com","rafiquemeo@yahoo.com","shehzadaleem@hotmail.com","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk"}, null, "KPI | SD1 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd1,filename_distributor_sd1_mtd,filename_pjp_sd1,filename_pjp_sd1_mtd,stock_filename_distributor_sd1,filename_no_sale_sd1,filename_sku_availability_sd1,filename_volume_analysis_sd1});
			//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "KPI | SD1 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd1,filename_distributor_sd1_mtd,filename_pjp_sd1,stock_filename_distributor_sd1,filename_no_sale_sd1,filename_sku_availability_sd1,filename_volume_analysis_sd1});
			

			// SD2
			new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd2, EmployeeHierarchy.getSDHead(2).USER_ID, false);
			new com.pbc.pushmail.scorecards.DistributorKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd2_mtd, EmployeeHierarchy.getSDHead(2).USER_ID, true);
			
			DistributorStockAnalysisExcel DistributorStockAnalysisSD2 = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
			DistributorStockAnalysisSD2.setSND(EmployeeHierarchy.getSDHead(2).USER_ID);
			DistributorStockAnalysisSD2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +stock_filename_distributor_sd2,0);
			
			new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd2, EmployeeHierarchy.getSDHead(2).USER_ID, false);
			new com.pbc.pushmail.scorecards.PJPKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_sd2_mtd, EmployeeHierarchy.getSDHead(2).USER_ID,true);
			new com.pbc.pushmail.scorecards.DistributorNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale_sd2, EmployeeHierarchy.getSDHead(2).USER_ID);
			new com.pbc.pushmail.scorecards.DistributorSKUSalesAnalysisExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_sku_availability_sd2, EmployeeHierarchy.getSDHead(2).USER_ID, true);

			VolumeAnalysisPSRExcel va4 = new com.pbc.pushmail.scorecards.VolumeAnalysisPSRExcel();
			va4.setSND(EmployeeHierarchy.getSDHead(2).USER_ID);
			va4.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis_sd2,0);
			
			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(2).USER_EMAIL},new String[]{"zahoor@pbc.com.pk","omerfk@pbc.com.pk","anjum.a.ansari@gmail.com","salman.baig@pbc.com.pk","muhammadameer@hotmail.com","rafiquemeo@yahoo.com","shehzadaleem@hotmail.com","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk"}, null, "KPI | SD2 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd2,filename_distributor_sd2_mtd,filename_pjp_sd2,filename_pjp_sd2_mtd,stock_filename_distributor_sd2,filename_no_sale_sd2,filename_sku_availability_sd2,filename_volume_analysis_sd2});
			//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "KPI | SD2 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd2,filename_distributor_sd2_mtd,filename_pjp_sd2,stock_filename_distributor_sd2,filename_no_sale_sd2,filename_sku_availability_sd2,filename_volume_analysis_sd2});
			
			
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
			
			VolumeAnalysisPSRExcel va5 = new com.pbc.pushmail.scorecards.VolumeAnalysisPSRExcel();
			va5.setSND(EmployeeHierarchy.getSDHead(4).USER_ID);
			va5.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis_sd3,0);
			
			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(4).USER_EMAIL},new String[]{"zahoor@pbc.com.pk","omerfk@pbc.com.pk","anjum.a.ansari@gmail.com","salman.baig@pbc.com.pk","muhammadameer@hotmail.com","rafiquemeo@yahoo.com","shehzadaleem@hotmail.com","shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk"}, null, "KPI | SD3 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd3,filename_distributor_sd3_mtd,filename_pjp_sd3,filename_pjp_sd3_mtd,stock_filename_distributor_sd3,filename_no_sale_sd3,filename_sku_availability_sd3,filename_volume_analysis_sd3});
			//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "KPI | SD3 | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_sd3,filename_distributor_sd3_mtd,filename_pjp_sd3,stock_filename_distributor_sd3,filename_no_sale_sd3,filename_sku_availability_sd3,filename_volume_analysis_sd3});
			
			*/
			
			
			int ii=0;
			
			User RSMs[] = EmployeeHierarchy.getRSMs();
			for (User user: RSMs){
				System.out.println("Hello");
				if(ii==0){
				try{
					if (user.USER_EMAIL != null && user.USER_ID != EmployeeHierarchy.getSDHead(4).USER_ID){
						
						User SND = EmployeeHierarchy.getSND(user.USER_ID);
						
						String filename_distributor = "KPI_Dist_Today_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
						String filename_distributor_mtd = "KPI_Dist_MTD_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_pjp = "KPI_PJP_Today_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
						String filename_pjp_mtd = "KPI_PJP_MTD_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
						String filename_stock = "Stock_Analysis_RSM_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
						
						new com.pbc.pushmail.scorecards.DistributorKPIsExcel_older_for_sharukh_yearly_sd_wise_13_12_2017().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor, user.USER_ID, false);
						new com.pbc.pushmail.scorecards.DistributorKPIsExcel_older_for_sharukh_yearly_sd_wise_13_12_2017().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_mtd, user.USER_ID, true);
						
						new com.pbc.pushmail.scorecards.PJPKPIsExcel_Old().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp, user.USER_ID, false);
						new com.pbc.pushmail.scorecards.PJPKPIsExcel_Old().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_mtd, user.USER_ID, true);

						DistributorStockAnalysisExcel dsa = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
						dsa.setSND(user.USER_ID);
						dsa.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_stock,0);
						//
						Utilities.sendPBCHTMLEmail(new String[]{"zulqurnan.aslam@pbc.com.pk","anas.wahab@pbc.com.pk"},null, null, "KPI | DDM | "+user.REGION_NAME+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(user.USER_ID), new String[]{filename_stock,filename_distributor,filename_distributor_mtd,filename_pjp,filename_pjp_mtd});
						
						
					}
				}catch(Exception e){e.printStackTrace();}
			}
				ii++;
			}
			
			
			/*User TDMs[] = EmployeeHierarchy.getTDMs();
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
							
							Utilities.sendPBCHTMLEmail(new String[]{user.USER_EMAIL},new String[]{SND.USER_EMAIL, "salman.baig@pbc.com.pk","zahoor@pbc.com.pk","shahrukh.salman@pbc.com.pk","omerfk@pbc.com.pk","anas.wahab@pbc.com.pk","aamir.zafar@pbc.com.pk","asad.ali@pbc.com.pk"}, null, "KPI | Unit Manager | "+user.USER_DISPLAY_NAME+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(user.USER_ID), new String[]{filename_stock,filename_distributor,filename_distributor_mtd,filename_pjp,filename_pjp_mtd,filename_no_sale,filename_sku_availability,filename_volume_analysis});
							//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "KPI | Unit Manager | "+user.USER_DISPLAY_NAME+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(user.USER_ID), new String[]{filename_stock,filename_distributor,filename_distributor_mtd,filename_pjp,filename_pjp_mtd,filename_no_sale,filename_sku_availability,filename_volume_analysis});
							//break;
						}
						
					}
				}catch(Exception e){e.printStackTrace();}
			}*/
			

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
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
