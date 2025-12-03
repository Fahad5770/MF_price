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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class DistributorLiftingComparisonSendEmail {
	
	
	
	public static void main(String[] args) {
		
		
		try {	
			
			
			
			
			Datasource ds = new Datasource();
			ds.createConnectionToReplica();
			
			Statement s = ds.createStatement();
			
			Date ReportDate = Utilities.getDateByDays(-1);
			
			
			//Date StartDate2 = Utilities.parseDate("01/09/2015");
			//Date EndDate2 = Utilities.parseDate("31/09/2015");
			
			s.executeUpdate("DROP TABLE IF EXISTS dist_targets_temp ");

		    s.executeUpdate("DROP TABLE IF EXISTS dist_targets_report_temp");
			
			
			s.executeUpdate("CREATE  TABLE dist_targets_temp (order_number varchar(100) , distributor_id varchar(100) ,distributor_name varchar(100) ,entry_date datetime ,order_date datetime ,fksak varchar(100) ,abgru varchar(100) ,posnr varchar(100) ,sap_code varchar(100) ,arktx varchar(100) , type varchar(100) , raw_case int, units int,total_units int , pstyv varchar(100) )");

			s.executeUpdate("CREATE  TABLE dist_targets_report_temp (distributor_id int,distributor_name varchar(100),sales decimal(18,2),target decimal(18,2), percentage varchar(50),package_id int,type_id int,snd_id int, rsm_id int, tdm_id int, target_mtd decimal(18,2))");
		   	
		   
		   
			if (true){ // Consolidated level
				
				String filename_DistributorTargetAnalysisLifting = "DistributorTargetAnalysis_Lifting_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
				
				
				s.executeUpdate("DELETE FROM dist_targets_temp");
				s.executeUpdate("DELETE FROM dist_targets_report_temp");
				
				DistributorLiftingComparisonPDF pack = new com.pbc.pushmail.scorecards.DistributorLiftingComparisonPDF();
				pack.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_DistributorTargetAnalysisLifting, -1,ReportDate);
				
				//////////////////////////* Graph Code ////////////////////////////
				
				
				double GrandTotalTarget=0;
				double GrandTotalAttainment=0;
				String PackFilteredString[] = new String [pack.PackageArray.length];
				//calculating Total target and attainment
				
				
				for(int ii=0;ii<pack.PackageArray.length;ii++){
					
					GrandTotalTarget += pack.TargetedSale[ii];
					GrandTotalAttainment += pack.AttainedSale[ii];
					
					if(pack.PackageArray[ii].indexOf("ML STD")>0){
						PackFilteredString[ii]= pack.PackageArray[ii].replace("ML STD","ML");
					}else if(pack.PackageArray[ii].indexOf("ML G.")>0){
						PackFilteredString[ii]= pack.PackageArray[ii].replace("ML G.","ML");
					}else if(pack.PackageArray[ii].indexOf("ML PET")>0){
						PackFilteredString[ii]= pack.PackageArray[ii].replace("ML PET","ML");
					}else if(pack.PackageArray[ii].indexOf("ML SSRB")>0){
						PackFilteredString[ii]= pack.PackageArray[ii].replace("ML SSRB","ML");
					}else if(pack.PackageArray[ii].indexOf("ML TETRA")>0){
						PackFilteredString[ii]= pack.PackageArray[ii].replace("ML TETRA","ML");
					}else{
						PackFilteredString[ii]=pack.PackageArray[ii];
					}
					
				}
				
				
				//calculating %age
				
				for(int x=0;x<pack.PackageArray.length;x++){
					pack.TotalTargetedSalePercentage[x] = (pack.TargetedSale[x]/GrandTotalTarget)*100;
					pack.TotalAttainedSalePercentage[x] = (pack.AttainedSale[x]/GrandTotalAttainment)*100;
				}
				
				//Printing
				for(int y=0;y<pack.PackageArray.length;y++){
					System.out.println(PackFilteredString[y]+" ---- Target %age : "+pack.TotalTargetedSalePercentage[y]+" ----- Attainment %age : "+pack.TotalAttainedSalePercentage[y]);
				}
				
				
				/////////////////////////////////////// This is final chart code
				
				 
				String TargetPercentages = "";
				String PackagesLabels ="";
				
				String AttainPercentages="";
				String PackagesLabelsAttainment ="";
				
				
				for(int y=0;y<pack.TotalTargetedSalePercentage.length;y++){
					if(pack.TotalTargetedSalePercentage[y]>2){ //if %age >2
						TargetPercentages += Utilities.getDisplayCurrencyFormatRounded(pack.TotalTargetedSalePercentage[y])+",";						
						PackagesLabels += PackFilteredString[y]+" "+Utilities.getDisplayCurrencyFormatRounded(pack.TotalTargetedSalePercentage[y])+"%"+"%7C";
						
					}
					if(pack.TotalAttainedSalePercentage[y]>2){
						AttainPercentages += Utilities.getDisplayCurrencyFormatRounded(pack.TotalAttainedSalePercentage[y])+",";
						PackagesLabelsAttainment += PackFilteredString[y]+" "+Utilities.getDisplayCurrencyFormatRounded(pack.TotalAttainedSalePercentage[y])+"%"+"%7C";;
					}
					
				}
				
				if (TargetPercentages.length() != 0){
					TargetPercentages = TargetPercentages.substring(0, TargetPercentages.length()-1); //removing last comma
					AttainPercentages = AttainPercentages.substring(0, AttainPercentages.length()-1); //removing last comma
				}
				if (PackagesLabels.length() > 3){
				PackagesLabels = PackagesLabels.substring(0, PackagesLabels.length()-3); //removing last %7C
				}
				if (PackagesLabelsAttainment.length() > 3){
				PackagesLabelsAttainment = PackagesLabelsAttainment.substring(0, PackagesLabelsAttainment.length()-3); //removing last %7C
				}
				
				String GraphString ="https://chart.googleapis.com/chart?cht=p3&chs=450x150&chd=t:"+TargetPercentages+"&chco=28495d|0c92e3|d9eaf5|87949c|5546ae|1acf6b|3b9262|b7f522|f5e922|f7bd1f|f78f1f|6a1d05&chl="+PackagesLabels;
				
				String GraphStringAttainment ="https://chart.googleapis.com/chart?cht=p3&chs=450x150&chd=t:"+AttainPercentages+"&chco=28495d|0c92e3|d9eaf5|87949c|5546ae|1acf6b|3b9262|b7f522|f5e922|f7bd1f|f78f1f|6a1d05&chl="+PackagesLabelsAttainment;
				
				////////////////////////////////////////////////////////////////////
				
				
				DistributorLiftingComparisonDailyPDF rep = new com.pbc.pushmail.scorecards.DistributorLiftingComparisonDailyPDF();    
				String filename = Utilities.getEmailAttachmentsPath()+ "/DailyLifting_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
				rep.createPdf(filename);
				
				
				
				
				
				
				
				Utilities.sendPBCHTMLEmail(new String[]{"obaid@pbc.com.pk","imran.hashim@pbc.com.pk"},null,new String[]{"anas.wahab@pbc.com.pk"}, "Lifting Growth Analysis | Consolidated | "+Utilities.getDisplayDateFormat(ReportDate), rep.getHTMLMessage() + getHTMLMessage(-1, pack,ReportDate, false,GraphString,GraphStringAttainment), new String[]{filename_DistributorTargetAnalysisLifting,"DailyLifting_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"});
				//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null,null, "Lifting Growth Analysis | Consolidated | "+Utilities.getDisplayDateFormat(ReportDate), rep.getHTMLMessage() + getHTMLMessage(-1, pack,ReportDate, false,GraphString,GraphStringAttainment), new String[]{filename_DistributorTargetAnalysisLifting,"DailyLifting_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"});
				
			}
		   
			/*
			String SDTitle = "";
			for(int i=1;i<=4;i++){

				SDTitle = "SD"+i;
				String filename_DistributorTargetAnalysisLifting = "DistributorTargetAnalysis_"+SDTitle+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
				
				if(i==3){
					i++;
				}
				
				
				
				s.executeUpdate("DELETE FROM dist_targets_temp");
				s.executeUpdate("DELETE FROM dist_targets_report_temp");
				
				DistributorLiftingComparisonPDF pack = new com.pbc.pushmail.scorecards.DistributorLiftingComparisonPDF();
				pack.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_DistributorTargetAnalysisLifting, EmployeeHierarchy.getSDHead(i).USER_ID,ReportDate);
				
				//Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(i).USER_EMAIL},new String[]{"obaid@pbc.com.pk","anas.wahab@pbc.com.pk","imran.hashim@pbc.com.pk","ihsan@pbc.com.pk"},null, "DSAR | "+SDTitle+" | "+Utilities.getDisplayDateFormat(ReportDate), getHTMLMessage(EmployeeHierarchy.getSDHead(i).USER_ID, pack,ReportDate, false), new String[]{filename_DistributorTargetAnalysis});
				
				
				//////////////////////////* Graph Code ////////////////////////////
				
				
				double GrandTotalTarget=0;
				double GrandTotalAttainment=0;
				String PackFilteredString[] = new String [pack.PackageArray.length];
				//calculating Total target and attainment
				
				for(int ii=0;ii<pack.PackageArray.length;ii++){
					//System.out.println("Hello -"+pack.PackageArray[ii]);
					//System.out.println("Total Targeted Sales of - "+pack.PackageArray[ii]+" ---- "+pack.TargetedSale[ii]);
					
					GrandTotalTarget += pack.TargetedSale[ii];
					GrandTotalAttainment += pack.AttainedSale[ii];
					
					if(pack.PackageArray[ii].indexOf("ML STD")>0){
						PackFilteredString[ii]= pack.PackageArray[ii].replace("ML STD","ML");
					}else if(pack.PackageArray[ii].indexOf("ML G.")>0){
						PackFilteredString[ii]= pack.PackageArray[ii].replace("ML G.","ML");
					}else if(pack.PackageArray[ii].indexOf("ML PET")>0){
						PackFilteredString[ii]= pack.PackageArray[ii].replace("ML PET","ML");
					}else if(pack.PackageArray[ii].indexOf("ML SSRB")>0){
						PackFilteredString[ii]= pack.PackageArray[ii].replace("ML SSRB","ML");
					}else if(pack.PackageArray[ii].indexOf("ML TETRA")>0){
						PackFilteredString[ii]= pack.PackageArray[ii].replace("ML TETRA","ML");
					}else{
						PackFilteredString[ii]=pack.PackageArray[ii];
					}
					
					
					
					
					
					
					
					
				}
				
				
				//calculating %age
				
				for(int x=0;x<pack.PackageArray.length;x++){
					pack.TotalTargetedSalePercentage[x] = (pack.TargetedSale[x]/GrandTotalTarget)*100;
					pack.TotalAttainedSalePercentage[x] = (pack.AttainedSale[x]/GrandTotalAttainment)*100;
				}
				
				//Printing
				for(int y=0;y<pack.PackageArray.length;y++){
					System.out.println(PackFilteredString[y]+" ---- Target %age : "+pack.TotalTargetedSalePercentage[y]+" ----- Attainment %age : "+pack.TotalAttainedSalePercentage[y]);
				}
				
				
				/////////////////////////////////////// This is final chart code
				
				
				///////
				 
				String TargetPercentages = "";
				String PackagesLabels ="";
				
				String AttainPercentages="";
				String PackagesLabelsAttainment ="";
				
				
				for(int y=0;y<pack.TotalTargetedSalePercentage.length;y++){
					if(pack.TotalTargetedSalePercentage[y]>2){ //if %age >2
						TargetPercentages += Utilities.getDisplayCurrencyFormatRounded(pack.TotalTargetedSalePercentage[y])+",";						
						PackagesLabels += PackFilteredString[y]+" "+Utilities.getDisplayCurrencyFormatRounded(pack.TotalTargetedSalePercentage[y])+"%"+"%7C";
						
					}
					if(pack.TotalAttainedSalePercentage[y]>2){
						AttainPercentages += Utilities.getDisplayCurrencyFormatRounded(pack.TotalAttainedSalePercentage[y])+",";
						PackagesLabelsAttainment += PackFilteredString[y]+" "+Utilities.getDisplayCurrencyFormatRounded(pack.TotalAttainedSalePercentage[y])+"%"+"%7C";;
					}
					
				}
				
				
				TargetPercentages = TargetPercentages.substring(0, TargetPercentages.length()-1); //removing last comma
				AttainPercentages = AttainPercentages.substring(0, AttainPercentages.length()-1); //removing last comma
				PackagesLabels = PackagesLabels.substring(0, PackagesLabels.length()-3); //removing last %7C
				PackagesLabelsAttainment = PackagesLabelsAttainment.substring(0, PackagesLabelsAttainment.length()-3); //removing last %7C
				
				
				String GraphString ="https://chart.googleapis.com/chart?cht=p3&chs=450x150&chd=t:"+TargetPercentages+"&chco=28495d|0c92e3|d9eaf5|87949c|5546ae|1acf6b|3b9262|b7f522|f5e922|f7bd1f|f78f1f|6a1d05&chl="+PackagesLabels;
				
				String GraphStringAttainment ="https://chart.googleapis.com/chart?cht=p3&chs=450x150&chd=t:"+AttainPercentages+"&chco=28495d|0c92e3|d9eaf5|87949c|5546ae|1acf6b|3b9262|b7f522|f5e922|f7bd1f|f78f1f|6a1d05&chl="+PackagesLabelsAttainment;
				
				////////////////////////////////////////////////////////////////////
				
				
				
				
				
				//To: SD Head
				//CC: 
				//Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(i).USER_EMAIL},new String[]{"obaid@pbc.com.pk","anas.wahab@pbc.com.pk","imran.hashim@pbc.com.pk","ihsan@pbc.com.pk","omerfk@pbc.com.pk"},null, "Lifting Attainment Report | "+SDTitle+" | "+Utilities.getDisplayDateFormat(ReportDate), getHTMLMessage(EmployeeHierarchy.getSDHead(i).USER_ID, pack,ReportDate, false,GraphString,GraphStringAttainment), new String[]{filename_DistributorTargetAnalysisLifting});
				Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null,null, "Lifting Comparison Report | "+SDTitle+" | "+Utilities.getDisplayDateFormat(ReportDate), getHTMLMessage(EmployeeHierarchy.getSDHead(i).USER_ID, pack,ReportDate, false,GraphString,GraphStringAttainment), new String[]{filename_DistributorTargetAnalysisLifting});
			
				
				
				
				
				
				//break;
			}
			
			
			
			User RSMs[] = EmployeeHierarchy.getRSMs();
			for (User user: RSMs){
				
				if (user.USER_EMAIL != null && user.USER_ID != EmployeeHierarchy.getSDHead(4).USER_ID){
					
					User SND = EmployeeHierarchy.getSND(user.USER_ID);
					
					String filename_DistributorTargetAnalysisLifting = "DistributorTargetAnalysis_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
					
					s.executeUpdate("DELETE FROM dist_targets_temp");
					s.executeUpdate("DELETE FROM dist_targets_report_temp");
					
					DistributorLiftingComparisonPDF pack = new com.pbc.pushmail.scorecards.DistributorLiftingComparisonPDF();
					pack.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_DistributorTargetAnalysisLifting, user.USER_ID, ReportDate);
					
					
					//Utilities.sendPBCHTMLEmail(new String[]{user.USER_EMAIL},new String[]{SND.USER_EMAIL,"obaid@pbc.com.pk","anas.wahab@pbc.com.pk","imran.hashim@pbc.com.pk","ihsan@pbc.com.pk"}, null, "Lifting Attainment Report | "+user.REGION_NAME+" | "+Utilities.getDisplayDateFormat(ReportDate), getHTMLMessage(user.USER_ID, pack,ReportDate, true,"",""), new String[]{filename_DistributorTargetAnalysisLifting});
					Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "Lifting Comparison Report | "+user.REGION_NAME+" | "+Utilities.getDisplayDateFormat(ReportDate), getHTMLMessage(user.USER_ID, pack,ReportDate, true,"",""), new String[]{filename_DistributorTargetAnalysisLifting});
					
					
					
					
					
				}
				
			}
			
			
			
			User TDMs[] = EmployeeHierarchy.getTDMs();
			for (User user: TDMs){
				
				if (user.USER_EMAIL != null){
					
					User RSM = EmployeeHierarchy.getRSMByTDM(user.USER_ID); 
					User SND = EmployeeHierarchy.getSND(RSM.USER_ID);
					
					if (RSM.USER_EMAIL == null){
						RSM.USER_EMAIL = SND.USER_EMAIL;
					}
					
					if (RSM.USER_EMAIL != null && SND.USER_EMAIL != null){
						String filename_DistributorTargetAnalysis = "DistributorTargetAnalysis_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
						
						s.executeUpdate("DELETE FROM dist_targets_temp");
						s.executeUpdate("DELETE FROM dist_targets_report_temp");
						
						DistributorLiftingComparisonPDF pack = new com.pbc.pushmail.scorecards.DistributorLiftingComparisonPDF();
						pack.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_DistributorTargetAnalysis, user.USER_ID, ReportDate);
						
						//Utilities.sendPBCHTMLEmail(new String[]{user.USER_EMAIL},new String[]{RSM.USER_EMAIL,"ihsan@pbc.com.pk"}, null, "Lifting Attainment Report | "+user.REGION_NAME+" | "+user.USER_DISPLAY_NAME+" | "+Utilities.getDisplayDateFormat(ReportDate), getHTMLMessage(user.USER_ID, pack,ReportDate, true,"",""), new String[]{filename_DistributorTargetAnalysis});
						Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"},null, null, "Lifting Comparison Report | "+user.REGION_NAME+" | "+user.USER_DISPLAY_NAME+" | "+Utilities.getDisplayDateFormat(ReportDate), getHTMLMessage(user.USER_ID, pack,ReportDate, true,"",""), new String[]{filename_DistributorTargetAnalysis});
					
					}
					
				}
				
			}			
			*/
			
			s.executeUpdate("DROP TABLE dist_targets_temp");
		    s.executeUpdate("DROP TABLE dist_targets_report_temp");
			
			
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

	public static String getHTMLMessage(long EmployeeID, DistributorLiftingComparisonPDF pack, Date CurrentDate, boolean isRSM,String SNDGraphTarget,String SNDGraphAttainment) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		double GraphOverallPercentage = 0;
		ArrayList<String> GraphRSMName = new <String>ArrayList();
		ArrayList<Double> GraphRSMPercentage = new <Double>ArrayList();
		
		double GraphOverallSNDPercentage = 0;
		ArrayList<String> GraphSNDName = new <String>ArrayList();
		ArrayList<Double> GraphSNDPercentage = new <Double>ArrayList();
		
		
		int CurrentYear = Utilities.getYearByDate(CurrentDate);
		int LastYear = CurrentYear-1;
		
		Date MonthStartDate = Utilities.getStartDateByDate(CurrentDate);
		Date MonthEndDate = CurrentDate;
		
		
		Datasource ds = new Datasource();
		ds.createConnectionToReplica();
		
		Statement s = ds.createStatement();
		Statement s2 = ds.createStatement();
		Statement s3 = ds.createStatement();
		Statement s4 = ds.createStatement();
		Statement s5 = ds.createStatement();
		Statement s6 = ds.createStatement();
		Statement s7 = ds.createStatement();
		Statement s8 = ds.createStatement();
		Statement s9 = ds.createStatement();
		Statement s10 = ds.createStatement();
		Statement s11= ds.createStatement();
		Statement s12 = ds.createStatement();
		Statement s13 = ds.createStatement();
		Statement s14 = ds.createStatement();
		Statement s15 = ds.createStatement();
		Statement s16 = ds.createStatement();
		Statement s17 = ds.createStatement();
		Statement s18 = ds.createStatement();
		Statement s20 = ds.createStatement();
		
		
		int ArrayCount=0;
		int PackageID=0;
		int DistributorID=0;
		double GrandTotal=0;
		String DistributorName="";
		
		int SNDID=0;
		int RSMID=0;
		int TDMID=0;
		
		//Date StartDate = StartDate3;
		//Date EndDate = EndDate3;
		

	    //System.out.println("select min(dt.start_date), max(dt.end_date) from distributor_targets dt where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y')  and dt.type_id=3");
		Date MinTargetDate = MonthStartDate;//new Date();
		Date MaxTargetDate = MonthEndDate;//new Date();
	   
		//System.out.println();
		
		/*
		ResultSet rs141 = s3.executeQuery("select min(dt.start_date), max(dt.end_date) from distributor_targets dt where date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dt.type_id=2");
	    if(rs141.first()){
	    	MinTargetDate = rs141.getDate(1);
	    	MaxTargetDate = rs141.getDate(2); //Utilities.parseDate("10/01/2014");//rs14.getDate(1);
	    }
		*/
		
		String WherePackage = "";
		
		//String SAPCodes = "0";

	    //System.out.println("SELECT group_concat(distinct sap_code) from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDate(MinTargetDate)+" and "+Utilities.getSQLDate(MaxTargetDate));
	    
		/*
		ResultSet rs601 = s.executeQuery("SELECT group_concat(distinct sap_code) from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDateLifting(MinTargetDate)+" and "+Utilities.getSQLDateNextLifting(MaxTargetDate));
		if (rs601.next()){
			SAPCodes = rs601.getString(1);
		}
		*/
		
		//System.out.println(SAPCodes);
		
		String html = "";
		//html += "<body>";
		
		
		//html +="<p style='text-align:left; font-weight:bold; padding-left:5px;'>MTD Lifting Growth Summary</p>";		
		
		html +="<br>";
		
		
		

		
		
		html +="<br>";
		html +="<span style='display: none' id='HGraphs'></span>";
		
		html +="<br>";

		
		
		
		
		html +="<table border=0 style='font-size:13px; font-weight: 400; width:900px' cellpadding='2' cellspacing='1'   class='GridWithBorder'>";
		html +="<thead>";
						    
		html +="<tr style='font-size:12px;'>";
		html +="<th data-priority='1'  style='text-align:center; min-width:150px; background-color:#3d5ab3; color: white;;border-right:1px solid #fff; min-height: 20px'>&nbsp;</th>";
		html +="<th style='text-align:center; background-color:#3d5ab3;border-right:1px solid #fff; color:white;' colspan='3'>CSD</th>";
		html +="<th style='text-align:center; background-color:#3d5ab3;border-right:1px solid #fff; color:white' colspan='3'>NCB</th>";
		html +="<th style='text-align:center; background-color:#3d5ab3;border-right:1px solid #fff; color:white' colspan='3'>LRB</th>";
		html +="</tr>";		
		
		
		html +="<tr style='font-size:12px;'>";
		html +="<th data-priority='1'  style='text-align:center; background-color:#c8cfe6; c1olor: white;;border-right:1px solid #fff; min-height: 20px'>&nbsp;</th>";
		html +="<th style='text-align:center; background-color:#c8cfe6;border-right:1px solid #fff; c1olor:white; width:75px;'>"+LastYear+"</th>";
		html +="<th style='text-align:center; background-color:#c8cfe6;border-right:1px solid #fff; c1olor:white;width:75px;'>"+CurrentYear+"</th>";
		html +="<th style='text-align:center; background-color:#c8cfe6;border-right:1px solid #fff; c1olor:white;width:75px'>%</th>";
		
		html +="<th style='text-align:center; background-color:#c8cfe6;border-right:1px solid #fff; c1olor:white;width:75px'>"+LastYear+"</th>";
		html +="<th style='text-align:center; background-color:#c8cfe6;border-right:1px solid #fff; c1olor:white;width:75px'>"+CurrentYear+"</th>";
		html +="<th style='text-align:center; background-color:#c8cfe6;border-right:1px solid #fff; c1olor:white;width:75px'>%</th>";
		
		html +="<th style='text-align:center; background-color:#c8cfe6;border-right:1px solid #fff; c1olor:white;width:75px'>"+LastYear+"</th>";
		html +="<th style='text-align:center; background-color:#c8cfe6;border-right:1px solid #fff; c1olor:white;width:75px'>"+CurrentYear+"</th>";
		html +="<th style='text-align:center; background-color:#c8cfe6;border-right:1px solid #fff; c1olor:white;width:75px'>%</th>";
								
		
		
		
		
							
		html +="</tr>";			
						
		//// Top Layer 
		
		
		
		
			html +="<tr>";
			html +="<td style='background-color:#b7bed4; font-weight: bold; font-size: 13px; border-right:1px solid #fff;'>Consolidated</td>";	
						
						
						
						
						ResultSet rs315218 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-2 and type_id=-2"); //-2 for CSD
						if(rs315218.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs315218.getDouble("sum_converted");
							double TargetCoverted1 = rs315218.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = (((SalesCoverted1 * 1d)-(TargetCoverted1 * 1d)) / (TargetCoverted1 * 1d) )*100;
							}
					
							
						
							if(TargetCoverted1!=0){
								html +="<td style='text-align: center; background-color:#b7bed4; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#b7bed4; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center; background-color:#b7bed4; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#b7bed4; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							String Table2Percentage11ConsCSDColor1="";
							
							if(CasesCovertedPercentage11<0){
								Table2Percentage11ConsCSDColor1 = "#e79ca8";
							}else{
								Table2Percentage11ConsCSDColor1 = "#cbe9aa";//green
							}
							
							
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center; background-color:"+Table2Percentage11ConsCSDColor1+"; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center; background-color:#b7bed4; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							
							
						}
						
						
						ResultSet rs315313 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-3 and type_id=-3"); //-3 for NCB
						if(rs315313.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs315313.getDouble("sum_converted");
							double TargetCoverted1 = rs315313.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = (((SalesCoverted1 * 1d)-(TargetCoverted1 * 1d)) / (TargetCoverted1 * 1d) )*100;
							}
					
						
							if(TargetCoverted1!=0){
								html +="<td style='text-align: center; background-color:#b7bed4; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#b7bed4; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center; background-color:#b7bed4; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#b7bed4; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							
							String CasesCovertedPercentage11Color1="";
							
							if(CasesCovertedPercentage11<0){
								CasesCovertedPercentage11Color1 = "#e79ca8";
							}else{
								CasesCovertedPercentage11Color1 = "#cbe9aa";//green
							}
							
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11Color1+"; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center; background-color:#b7bed4; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
						}
				
////LRB
						
						ResultSet rs315312 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1"); //-1 for all
						if(rs315312.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs315312.getDouble("sum_converted");
							double TargetCoverted1 = rs315312.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = (((SalesCoverted1 * 1d)-(TargetCoverted1 * 1d)) / (TargetCoverted1 * 1d) )*100;
							}
					
							if(TargetCoverted1!=0){
								html +="<td style='text-align: center; background-color:#b7bed4; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#b7bed4; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center; background-color:#b7bed4; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#b7bed4; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							GraphOverallSNDPercentage = CasesCovertedPercentage11;
							
							String CasesCovertedPercentage11LRBColor1="";
							
							if(CasesCovertedPercentage11<0){
								CasesCovertedPercentage11LRBColor1 = "#e79ca8";
							}else{
								CasesCovertedPercentage11LRBColor1 = "#cbe9aa";//green
							}
							
							
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11LRBColor1+"; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center; background-color:#b7bed4; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
						}
				//////
				
				html +="</tr>";
			
		
		
		
		///////////////////////////////////////////
		
		
		
		
		
	    
	    long DistributorIDD11=0;
		long DistributorIDD1=0;
		
		long RSMID11=0;
		String RSMName1="";
		
		long SNDID11=0;
		String SNDName1="";
		String SNDDisplay1="";
		
		
		ResultSet rs11321 = s16.executeQuery("SELECT distinct snd_id,(select display_name from users u where u.id=snd_id) snd_name FROM dist_targets_report_temp");
		while(rs11321.next()){
			SNDID11 = rs11321.getLong("snd_id");
			SNDName1= rs11321.getString("snd_name");
			
			SNDDisplay1 = SNDID11+ " - "+ SNDName1;
			if (SNDID11 == 0){
				SNDDisplay1 = "Other";
			}
			if (SNDID11 == 1){
				SNDDisplay1 = "Special Sales";
			}
			
			GraphSNDName.add(SNDDisplay1);
			
			
			if (!isRSM){
			html +="<tr>";
			html +="<td style='background-color:#d3d8ea; font-weight: bold; font-size: 13px; border-right:1px solid #fff;'>"+Utilities.truncateStringToMax(SNDDisplay1, 22)+"</td>";	
						
						
						
						
						ResultSet rs3152 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-2 and type_id=-2 and snd_id="+SNDID11); //-2 for CSD
						if(rs3152.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs3152.getDouble("sum_converted");
							double TargetCoverted1 = rs3152.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = (((SalesCoverted1 * 1d)-(TargetCoverted1 * 1d)) / (TargetCoverted1 * 1d) )*100;
							}
					
							
						
							if(TargetCoverted1!=0){
								html +="<td style='text-align: center; background-color:#d3d8ea; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d3d8ea; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center; background-color:#d3d8ea; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d3d8ea; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							
							String CasesCovertedPercentageSNDColor1="";
							
							if(CasesCovertedPercentage11<0){
								CasesCovertedPercentageSNDColor1 = "#e79ca8";
							}else{
								CasesCovertedPercentageSNDColor1 = "#cbe9aa";//green
							}
							
							
							
							
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center; background-color:"+CasesCovertedPercentageSNDColor1+"; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d3d8ea; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							
							
						}
						
						
						ResultSet rs3153 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-3 and type_id=-3 and snd_id="+SNDID11); //-3 for NCB
						if(rs3153.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs3153.getDouble("sum_converted");
							double TargetCoverted1 = rs3153.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = (((SalesCoverted1 * 1d)-(TargetCoverted1 * 1d)) / (TargetCoverted1 * 1d) )*100;
							}
					
						
							if(TargetCoverted1!=0){
								html +="<td style='text-align: center; background-color:#d3d8ea; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d3d8ea; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center; background-color:#d3d8ea; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d3d8ea; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							
							String NCBCasesCovertedPercentageSNDColor1="";
							
							if(CasesCovertedPercentage11<0){
								NCBCasesCovertedPercentageSNDColor1 = "#e79ca8";
							}else{
								NCBCasesCovertedPercentageSNDColor1 = "#cbe9aa";//green
							}
							
							
							
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center; background-color:"+NCBCasesCovertedPercentageSNDColor1+"; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d3d8ea; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
						}
				
////LRB
						
						ResultSet rs31531 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and snd_id="+SNDID11); //-1 for all
						if(rs31531.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs31531.getDouble("sum_converted");
							double TargetCoverted1 = rs31531.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = (((SalesCoverted1 * 1d)-(TargetCoverted1 * 1d)) / (TargetCoverted1 * 1d) )*100;
							}
					
							if(TargetCoverted1!=0){
								html +="<td style='text-align: center; background-color:#d3d8ea; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d3d8ea; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center; background-color:#d3d8ea; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d3d8ea; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							
							
							String LRBCasesCovertedPercentageSNDColor1="";
							
							if(CasesCovertedPercentage11<0){
								LRBCasesCovertedPercentageSNDColor1 = "#e79ca8";
							}else{
								LRBCasesCovertedPercentageSNDColor1 = "#cbe9aa";//green
							}
							
							
							GraphOverallPercentage = CasesCovertedPercentage11;
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center; background-color:"+LRBCasesCovertedPercentageSNDColor1+"; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d3d8ea; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							GraphSNDPercentage.add(CasesCovertedPercentage11);
						}
				//////
				
				html +="</tr>";
			} // not isRSM
		
		
		String RSMDisplay1="";
		ResultSet rs11311 = s13.executeQuery("SELECT distinct rsm_id,(select display_name from users u where u.id=rsm_id) rsm_name FROM dist_targets_report_temp where snd_id="+SNDID11);
		while(rs11311.next()){
			RSMID11 = rs11311.getLong("rsm_id");
			RSMName1 = rs11311.getString("rsm_name");
			
			RSMDisplay1 = RSMID11+ " - "+ RSMName1;
			if (RSMID11 == 0){
				RSMDisplay1 = "Unassigned";
			}
			
			
			GraphRSMName.add(RSMDisplay1);
			
			
			html +="<tr>";
			html +="<td style='background-color:#d7e0fd;font-weight: bold; border-right:1px solid #fff;font-size: 13px;'>"+Utilities.truncateStringToMax(RSMDisplay1,22)+"</td>";	
						
						
					
				
						double RSMConvetedCasesPercentage=0;
						ResultSet rs31521 = s14.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-2 and type_id=-2 and rsm_id="+RSMID11+" and snd_id="+SNDID11); //-2 for CSD
						if(rs31521.first()){
							
							double SalesCoverted1 = rs31521.getDouble("sum_converted");
							double TargetCoverted1 = rs31521.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								RSMConvetedCasesPercentage = (((SalesCoverted1 * 1d)-(TargetCoverted1 * 1d)) / (TargetCoverted1 * 1d) )*100;
							}
							
						if(TargetCoverted1!=0){
							html +="<td style='text-align: center;background-color:#d7e0fd;border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
						}else{
							html +="<td style='text-align: center;background-color:#d7e0fd;border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
						}
						
						if(SalesCoverted1!=0){
							html +="<td style='text-align: center;background-color:#d7e0fd;border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
						}else{
							html +="<td style='text-align: center;background-color:#d7e0fd;border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
						}
						
						String CSDCasesCovertedPercentageRSMColor1="";
						
						if(RSMConvetedCasesPercentage<0){
							CSDCasesCovertedPercentageRSMColor1 = "#e79ca8";
						}else{
							CSDCasesCovertedPercentageRSMColor1 = "#cbe9aa";//green
						}
						
						
						if(RSMConvetedCasesPercentage!=0){
							html +="<td style='text-align: center;background-color:"+CSDCasesCovertedPercentageRSMColor1+";border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(RSMConvetedCasesPercentage)+"%</td>";
						}else{
							html +="<td style='text-align: center;background-color:#d7e0fd;border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
						}
							
							
				
						}
						
						//GraphRSMPercentage.add(RSMConvetedCasesPercentage);
						
						
						ResultSet rs31521i = s14.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-3 and type_id=-3 and rsm_id="+RSMID11+" and snd_id="+SNDID11); //-3 for NCB
						if(rs31521i.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs31521i.getDouble("sum_converted");
							double TargetCoverted1 = rs31521i.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = (((SalesCoverted1 * 1d)-(TargetCoverted1 * 1d)) / (TargetCoverted1 * 1d) )*100;
							}
							
						
						
							if(TargetCoverted1!=0){
								html +="<td style='text-align: center;background-color:#d7e0fd;border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center;background-color:#d7e0fd;border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center;background-color:#d7e0fd;border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center;background-color:#d7e0fd;border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							
							String NCBCasesCovertedPercentageRSMColor1="";
							
							if(CasesCovertedPercentage11<0){
								NCBCasesCovertedPercentageRSMColor1 = "#e79ca8";
							}else{
								NCBCasesCovertedPercentageRSMColor1 = "#cbe9aa";//green
							}
							
							
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center;background-color:"+NCBCasesCovertedPercentageRSMColor1+";border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center;background-color:#d7e0fd;border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
						}
				
/////LRB
						
						
						ResultSet rs31521i1 = s14.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and rsm_id="+RSMID11+" and snd_id="+SNDID11); //
						if(rs31521i1.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs31521i1.getDouble("sum_converted");
							double TargetCoverted1 = rs31521i1.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = (((SalesCoverted1 * 1d)-(TargetCoverted1 * 1d)) / (TargetCoverted1 * 1d) )*100;
							}
							
						
							if(TargetCoverted1!=0){
								html +="<td style='text-align: center;background-color:#d7e0fd;border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center;background-color:#d7e0fd;border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center;background-color:#d7e0fd;border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center;background-color:#d7e0fd;border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							
							
							String LRBCasesCovertedPercentageRSMColor1="";
							
							if(CasesCovertedPercentage11<0){
								LRBCasesCovertedPercentageRSMColor1 = "#e79ca8";
							}else{
								LRBCasesCovertedPercentageRSMColor1 = "#cbe9aa";//green
							}
							
							//GraphOverallPercentage = CasesCovertedPercentage11;
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center;background-color:"+LRBCasesCovertedPercentageRSMColor1+";border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center;background-color:#d7e0fd;border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
							}
							GraphRSMPercentage.add(CasesCovertedPercentage11);
							
						}
						
						/////
				
				html +="</tr>";
				
				int MonthNumber=0;
				int YearNumber=0;
				Date DisplayStartDate=new Date();
				Date DisplayEndDate=new Date();
				/*
				ResultSet rs99 = s20.executeQuery("select month,year,start_date,end_date from distributor_targets where distributor_id in (SELECT distributor_id FROM dist_targets_report_temp where rsm_id in("+RSMID11+")) and "+Utilities.getSQLDate(CurrentDate)+" between start_date and end_date and type_id = 2 limit 1");
				if(rs99.first()){
					 MonthNumber = rs99.getInt("month");
					 YearNumber = rs99.getInt("year");
					 DisplayStartDate = rs99.getDate("start_date");
					 DisplayEndDate = rs99.getDate("end_date");
				}
				
				html +="<td style='background-color:#ececda;font-weight: bold; border-right:1px solid #fff;font-size: 13px;' colspan='6'>"+Utilities.getMonthNameByNumber(MonthNumber)+" "+YearNumber+"</td>";	
				html +="<td style='background-color:#ececda;font-weight: bold; border-right:1px solid #fff;font-size: 13px;text-align:right;' colspan='4'>"+Utilities.getDisplayDateFormat(DisplayStartDate)+" - "+Utilities.getDisplayDateFormat(DisplayEndDate)+"</td>";
		*/
		
		long TDMID11 = 0;
		String TDMName1 = "";
		
		
		
		ResultSet rs1131 = s10.executeQuery("SELECT distinct tdm_id,(select display_name from users u where u.id=tdm_id) tdm_name FROM dist_targets_report_temp where rsm_id="+RSMID11+" and snd_id="+SNDID11);
		while(rs1131.next()){
			TDMID11 = rs1131.getLong("tdm_id");
			TDMName1 = rs1131.getString("tdm_name");
			
			
			String TDMDisplay1 = TDMID11+ " - "+ TDMName1;
			if (TDMID11 == 0){
				TDMDisplay1 = "Unassigned";
			}
			
			html +="<tr>";
			html +="<td style='font-weight: normal; background-color:#FCFCFC;border-right:1px solid #fff;font-size: 13px;'>"+Utilities.truncateStringToMax(TDMDisplay1,22)+"</td>";	
						
						
					
						
						
						
						
						ResultSet rs3151i = s8.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-2 and type_id=-2 and tdm_id="+TDMID11+" and rsm_id="+RSMID11+" and snd_id="+SNDID11); //-2 for CSD
						if(rs3151i.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs3151i.getDouble("sum_converted");
							double TargetCoverted1 = rs3151i.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = (((SalesCoverted1 * 1d)-(TargetCoverted1 * 1d)) / (TargetCoverted1 * 1d) )*100;
							}
						
						
						
							if(TargetCoverted1!=0){
								html +="<td style='background-color:#FCFCFC; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='background-color:#FCFCFC; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='background-color:#FCFCFC; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='background-color:#FCFCFC; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'></td>";
							}
							
							String CSDCasesCovertedPercentageTDMColor1="";
							
							if(CasesCovertedPercentage11<0){
								CSDCasesCovertedPercentageTDMColor1 = "#e79ca8";
							}else{
								CSDCasesCovertedPercentageTDMColor1 = "#cbe9aa";//green
							}
							
							
							if(CasesCovertedPercentage11!=0){
								html +="<td style='background-color:"+CSDCasesCovertedPercentageTDMColor1+"; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='background-color:#FCFCFC; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'></td>";
							}

							
							
							
				
						}
						
						
						ResultSet rs3151i1 = s8.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-3 and type_id=-3 and tdm_id="+TDMID11+" and rsm_id="+RSMID11+" and snd_id="+SNDID11);  //-3 for NCB
						if(rs3151i1.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs3151i1.getDouble("sum_converted");
							double TargetCoverted1 = rs3151i1.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = (((SalesCoverted1 * 1d)-(TargetCoverted1 * 1d)) / (TargetCoverted1 * 1d) )*100;
							}
						
						
						

							if(TargetCoverted1!=0){
								html +="<td style='background-color:#FCFCFC; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='background-color:#FCFCFC; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='background-color:#FCFCFC; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='background-color:#FCFCFC; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'></td>";
							}
							
							
							String NCBCasesCovertedPercentageTDMColor1="";
							
							if(CasesCovertedPercentage11<0){
								NCBCasesCovertedPercentageTDMColor1 = "#e79ca8";
							}else{
								NCBCasesCovertedPercentageTDMColor1 = "#cbe9aa";//green
							}
							
							if(CasesCovertedPercentage11!=0){
								html +="<td style='background-color:"+NCBCasesCovertedPercentageTDMColor1+"; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='background-color:#FCFCFC; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'></td>";
							}
						}
				
/////LRB
						
						ResultSet rs3151i11 = s8.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and tdm_id="+TDMID11+" and rsm_id="+RSMID11+" and snd_id="+SNDID11);  //-3 for NCB
						if(rs3151i11.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs3151i11.getDouble("sum_converted");
							double TargetCoverted1 = rs3151i11.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = (((SalesCoverted1 * 1d)-(TargetCoverted1 * 1d)) / (TargetCoverted1 * 1d) )*100;
							}
						
							if(TargetCoverted1!=0){
								html +="<td style='background-color:#FCFCFC; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='background-color:#FCFCFC; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='background-color:#FCFCFC; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='background-color:#FCFCFC; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'></td>";
							}
							
							
							String LRBCasesCovertedPercentageTDMColor1="";
							
							if(CasesCovertedPercentage11<0){
								LRBCasesCovertedPercentageTDMColor1 = "#e79ca8";
							}else{
								LRBCasesCovertedPercentageTDMColor1 = "#cbe9aa";//green
							}
							
							
							if(CasesCovertedPercentage11!=0){
								html +="<td style='background-color:"+LRBCasesCovertedPercentageTDMColor1+"; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='background-color:#FCFCFC; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'></td>";
							}
						}
				
				//////////
				
				html +="</tr>";
				
			
			
		
		
		
	
		
	
	
		
		}//end of tdm loop
		}//end of RSM loop
	    }//end of SND loop
	    
		if (!isRSM && EmployeeID != -1){
		
			String RSMNames = "";
			String RSMValues = "";
			String RSMValues2 = "";
			for (int i = 0; i < GraphRSMName.size(); i++){
				
				
				if (i > 0){
					RSMValues += ",";
					RSMValues2 += ",";
				}
				RSMNames = "|"+GraphRSMName.get(i).toString() + RSMNames;
				RSMValues += Math.round(GraphRSMPercentage.get(i).doubleValue());
				RSMValues2 += "100";
			}
			
			String HTMLGraphs = "";
			HTMLGraphs +="<table style='width: 900px;' cellpadding='2' cellspacing='1'>";
			HTMLGraphs +="<tr>";
			HTMLGraphs +="<td width='100%' style='text-align: left'><img src='https://chart.googleapis.com/chart?chs=220x135&cht=gom&chd=t:"+Utilities.getDisplayCurrencyFormatRounded(GraphOverallPercentage)+"&chdl=Growth&chdlp=b&chl="+Utilities.getDisplayCurrencyFormatRounded(GraphOverallPercentage)+"%&chls=5|15'><img src='https://chart.googleapis.com/chart?cht=bhs&chs=410x135&chd=t:"+RSMValues+"|"+RSMValues2+"&chco=4d89f9,c6d9fd&chbh=20&chxt=x,y&chxl=1:"+RSMNames+"&chm=N,000000,0,-1,11&chtt=Growth%20(%)'></td></tr>";
			HTMLGraphs +="</table>";
			html = html.replaceAll("<span style='display: none' id='HGraphs'></span>", HTMLGraphs);

		}
		
		
		
		if (EmployeeID==-1){
			
			String SNDNames = "";
			String SNDValues = "";
			String SNDValues2 = "";
			for (int i = 0; i < GraphSNDName.size(); i++){
				
				
				if (i > 0){
					SNDValues += ",";
					SNDValues2 += ",";
				}
				SNDNames = "|"+GraphSNDName.get(i).toString() + SNDNames;
				SNDValues += Math.round(GraphSNDPercentage.get(i).doubleValue());
				SNDValues2 += "100";
			}
			
			String HTMLGraphs = "";
			HTMLGraphs +="<table style='width: 900px;' cellpadding='2' cellspacing='1'>";
			HTMLGraphs +="<tr>";
			HTMLGraphs +="<td width='100%' style='text-align: left'><img src='https://chart.googleapis.com/chart?chs=220x135&cht=gom&chd=t:"+Utilities.getDisplayCurrencyFormatRounded(GraphOverallSNDPercentage)+"&chdl=Growth&chdlp=b&chl="+Utilities.getDisplayCurrencyFormatRounded(GraphOverallSNDPercentage)+"%&chls=5|15'><img src='https://chart.googleapis.com/chart?cht=bhs&chs=410x125&chd=t:"+SNDValues+"|"+SNDValues2+"&chco=4d89f9,c6d9fd&chbh=20&chxt=x,y&chxl=1:"+SNDNames+"&chm=N,000000,0,-1,11&chtt=Growth%20(%)'></td></tr>";
			HTMLGraphs +="</table>";
			//html = html.replaceAll("<span style='display: none' id='HGraphs'></span>", HTMLGraphs);

		}
    
	    html +="</tr>";
	    html +="</thead>"; 
	    html +="<tbody>";				
						
	html +="</tbody>";
											
					
						
								
	html +="</table>";
	html +="</td>";
	html +="</tr>";
	html +="</table>";
	
	html +="<br/>";
		
	if (!isRSM){
	html +="<p style='text-align:left; font-weight:bold; padding-left:5px;'>Pack Mix</p>";	
	
	html +="<br/>";		
	
	html +="<table style='width: 900px;' cellpadding='2' cellspacing='1'>";
	html +="<tr>";
	html +="<td width='100%' style='text-align: left'><img src='"+SNDGraphTarget+"'></td>";
	html +="<td width='100%' style='text-align: left'><img src='"+SNDGraphAttainment+"'></td>";
	html +="</tr>";
	html +="<tr><td style='text-align:center'><i>"+LastYear+"</i></td><td style='text-align:center'><i>"+CurrentYear+"</i></td></tr>";
	html +="</table>";
	
	html +="<br><br/>";		
	}
		
		
		//////////////////////////////////////////////////////////////////////////// - Actual Email - /////////////////////////////////////////
		
		
		html +="<table style='width: 900px; margin-top:-8px; ' cellpadding='2' cellspacing='1'>";
		html +="<tr>";
			
		html +="<td style='width: 100%' valign='top'>";
		html +="<table border=0 style='font-size:13px; font-weight: 400; width:900px' cellpadding='2' cellspacing='1'   class='GridWithBorder'>";
		html +="<thead>";
						    
		html +="<tr style='font-size:12px;'>";
		html +="<th data-priority='1'  style='text-align:center; min-width:130px;background-color:#3d5ab3; color: white;;border-right:1px solid #fff; min-height: 20px' colspan='2'>&nbsp;</th>";

								
		int TotalPackages=0;
		
		ResultSet rs101 = s.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in (select sap_code from dist_targets_temp) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
		while(rs101.next()){
			TotalPackages+=rs101.getInt("packge_count");
			html +="<th data-priority='1'  style='text-align:center; background-color:#3d5ab3; color: white;;border-right:1px solid #fff' colspan="+rs101.getInt("packge_count")+">"+rs101.getString("type_name") +"</th>";
			
		}
		
		//System.out.println(TotalPackages);
							
		html +="</tr>";			
						
		html +="<tr style='font-size:13px;'>";
		html +="<th data-priority='1'  style='text-align:center; background-color:#c8cfe6; min-width:100px;border-right:1px solid #fff'>&nbsp;</th>";
		html +="<th style='text-align:center; background-color:#c8cfe6;border-right:1px solid #fff'>Converted</th>";
		
		
		
		//System.out.println("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  product_id in (SELECT distinct idnp.product_id FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id where idn.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+") "+WherePackage+ "order by package_sort_order");
		
		ResultSet rs111 = s.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in (select sap_code from dist_targets_temp)  "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
		while(rs111.next()){
		ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in (select sap_code from dist_targets_temp)  "+WherePackage+ " and lrb_type_id="+rs111.getInt("lrb_type_id")+" order by package_sort_order");
		
		
		while(rs2.next()){
			
			ArrayCount++;
			
			String PackageLabel = rs2.getString("package_label");
			PackageLabel = PackageLabel.replace("PET", "");
			PackageLabel = PackageLabel.replace(" ML", "ML");
			PackageLabel = PackageLabel.replace("STD", "");
			PackageLabel = PackageLabel.replace("SSRB", "");
			PackageLabel = PackageLabel.replace("TETRA", "");
			PackageLabel = PackageLabel.replace(" G.", "G.");
			
			//System.out.println("helloooo");
			
			//System.out.println(rs2.getString("package_label"));
		
			html +="<th data-priority='1'  style='text-align:center; background-color:#c8cfe6;border-right:1px solid #fff'>"+PackageLabel+"</th>";
		
			
		
		
		}
		
		
		
		
	}			
		html +="</tr>";
		
		
		/////////////// Upper Layer
		
		
		
	
			html +="<tr>";
			html +="<td style='background-color:#b7bed4; font-weight: bold; font-size: 13px; border-right:1px solid #fff;'>Consolidated</td>";	
						
						
						ResultSet rs31524 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1");
						if(rs31524.first()){
							double CasesCovertedPercentage1=0;
							double SalesCoverted = rs31524.getDouble("sum_converted");
							double TargetCoverted = rs31524.getDouble("sum_targeted");
							
							if(TargetCoverted!=0){
								CasesCovertedPercentage1 = (((SalesCoverted * 1d)-(TargetCoverted * 1d)) / (TargetCoverted * 1d) )*100;
							}
						
						
						if(CasesCovertedPercentage1!=0){
							html +="<td style='text-align: center; background-color:#b7bed4; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage1)+"%</td>";
						}else{
							html +="<td style='text-align: center; background-color:#b7bed4; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
						}
							
							
				
						}
				ResultSet rs12325 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in (select sap_code from dist_targets_temp) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
				while(rs12325.next()){
					
					int TypeID3 = rs12325.getInt("lrb_type_id");
					ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in (select sap_code from dist_targets_temp)  "+WherePackage+ " and lrb_type_id="+rs12325.getInt("lrb_type_id")+" order by package_sort_order");
					
					while(rs.next()){
					int unit_per_sku=0;
					PackageID=rs.getInt("package_id");
					//System.out.println("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1);
					
					ResultSet rs315 = s18.executeQuery("select sum(sales) sum_sales,sum(target_mtd) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+"");
					if(rs315.first()){
						double CasesPercentage1=0;
						double Sales = rs315.getDouble("sum_sales");
						double Target = rs315.getDouble("sum_target");
						
						if(Target!=0){	
							CasesPercentage1 = (((Sales * 1d)-(Target * 1d)) / (Target * 1d) )*100;
						}
						
						
						
						String CosCasesCovertedPercentageTb3Color1="";
						
						if(CasesPercentage1<0){
							CosCasesCovertedPercentageTb3Color1 = "#e79ca8";
						}else{
							CosCasesCovertedPercentageTb3Color1 = "#cbe9aa";//green
						}
						
						
					
					if(CasesPercentage1!=0){
						html +="<td style='text-align: center; background-color:"+CosCasesCovertedPercentageTb3Color1+"; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesPercentage1)+"%</td>";
					}else{
						html +="<td style='text-align: center; background-color:#b7bed4; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
					}
						
						
					
					}
					}
				}
				
				
				
				html +="</tr>";
		
		
		
		
		
		/////////////////////////////////////////////////////
		
		
		
		
	    
	   // long DistributorIDD1=0;
		long DistributorIDD=0;
		
		long RSMID1=0;
		String RSMName="";
		
		long SNDID1=0;
		String SNDName="";
		String SNDDisplay="";
		
		
		ResultSet rs1132 = s16.executeQuery("SELECT distinct snd_id,(select display_name from users u where u.id=snd_id) snd_name FROM dist_targets_report_temp");
		while(rs1132.next()){
			SNDID1 = rs1132.getLong("snd_id");
			SNDName = rs1132.getString("snd_name");
			
			SNDDisplay = SNDID1+ " - "+ SNDName;
			if (SNDID1 == 0){
				SNDDisplay = "Unassigned";
			}
			
			if (!isRSM){
			html +="<tr>";
			html +="<td style='background-color:#d3d8ea; font-weight: bold; font-size: 13px; border-right:1px solid #fff;'>"+Utilities.truncateStringToMax(SNDDisplay,22)+"</td>";	
						
						
						ResultSet rs3152 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and snd_id="+SNDID1);
						if(rs3152.first()){
							double CasesCovertedPercentage1=0;
							double SalesCoverted = rs3152.getDouble("sum_converted");
							double TargetCoverted = rs3152.getDouble("sum_targeted");
							
							if(TargetCoverted!=0){
								CasesCovertedPercentage1 = (((SalesCoverted * 1d)-(TargetCoverted * 1d)) / (TargetCoverted * 1d) )*100;
							}
						
						
							
							String CosCasesCovertedPercentageTb3SNDColor1="";
							
							if(CasesCovertedPercentage1<0){
								CosCasesCovertedPercentageTb3SNDColor1 = "#e79ca8";
							}else{
								CosCasesCovertedPercentageTb3SNDColor1 = "#cbe9aa";//green
							}
							
							
						if(CasesCovertedPercentage1!=0){
							html +="<td style='text-align: center; background-color:"+CosCasesCovertedPercentageTb3SNDColor1+"; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage1)+"%</td>";
						}else{
							html +="<td style='text-align: center; background-color:#d3d8ea; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
						}
							
							
				
						}
				ResultSet rs1232 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in (select sap_code from dist_targets_temp) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
				while(rs1232.next()){
					
					int TypeID3 = rs1232.getInt("lrb_type_id");
					ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in (select sap_code from dist_targets_temp) "+WherePackage+ " and lrb_type_id="+rs1232.getInt("lrb_type_id")+" order by package_sort_order");
					
					while(rs.next()){
					int unit_per_sku=0;
					PackageID=rs.getInt("package_id");
					//System.out.println("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1);
					
					ResultSet rs315 = s18.executeQuery("select sum(sales) sum_sales,sum(target_mtd) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and snd_id="+SNDID1);
					if(rs315.first()){
						double CasesPercentage1=0;
						double Sales = rs315.getDouble("sum_sales");
						double Target = rs315.getDouble("sum_target");
						
						if(Target!=0){	
							CasesPercentage1 = (((Sales * 1d)-(Target * 1d)) / (Target * 1d) )*100;
						}
						
						
						
						String NormalCasesCovertedPercentageTb3SNDColor1="";
						
						if(CasesPercentage1<0){
							NormalCasesCovertedPercentageTb3SNDColor1 = "#e79ca8";
						}else{
							NormalCasesCovertedPercentageTb3SNDColor1 = "#cbe9aa";//green
						}
					
					if(CasesPercentage1!=0){
						html +="<td style='text-align: center; background-color:"+NormalCasesCovertedPercentageTb3SNDColor1+"; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesPercentage1)+"%</td>";
					}else{
						html +="<td style='text-align: center; background-color:#d3d8ea; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
					}
						
						
					
					}
					}
				}
				
				
				
				html +="</tr>";
		
			} // not isRSM
		
		String RSMDisplay="";
		ResultSet rs1131 = s13.executeQuery("SELECT distinct rsm_id,(select display_name from users u where u.id=rsm_id) rsm_name FROM dist_targets_report_temp where snd_id="+SNDID1);
		while(rs1131.next()){
			RSMID1 = rs1131.getLong("rsm_id");
			RSMName = rs1131.getString("rsm_name");
			
			RSMDisplay = RSMID1+ " - "+ RSMName;
			if (RSMID1 == 0){
				RSMDisplay = "Unassigned";
			}
			
			
			html +="<tr>";
			html +="<td style='background-color:#d7e0fd;font-weight: bold; border-right:1px solid #fff;font-size: 13px;'>"+Utilities.truncateStringToMax(RSMDisplay,22)+"</td>";	
						
						
						ResultSet rs3151 = s14.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and rsm_id="+RSMID1+" and snd_id="+SNDID1);
						if(rs3151.first()){
							double CasesCovertedPercentage1=0;
							double SalesCoverted = rs3151.getDouble("sum_converted");
							double TargetCoverted = rs3151.getDouble("sum_targeted");
							
							if(TargetCoverted!=0){
								CasesCovertedPercentage1 = (((SalesCoverted * 1d)-(TargetCoverted * 1d)) / (TargetCoverted * 1d) )*100;
							}
							
						
							String ConsCasesCovertedPercentageTb3RSMColor1="";
							
							if(CasesCovertedPercentage1<0){
								ConsCasesCovertedPercentageTb3RSMColor1 = "#e79ca8";
							}else{
								ConsCasesCovertedPercentageTb3RSMColor1 = "#cbe9aa";//green
							}
							
							
						if(CasesCovertedPercentage1!=0){
							html +="<td style='text-align: center;background-color:"+ConsCasesCovertedPercentageTb3RSMColor1+";border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage1)+"%</td>";
						}else{
							html +="<td style='text-align: center;background-color:#d7e0fd;border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
						}
						
							
				
						}
				ResultSet rs1231 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in (select sap_code from dist_targets_temp) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
				while(rs1231.next()){
					
					int TypeID3 = rs1231.getInt("lrb_type_id");
					ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in (select sap_code from dist_targets_temp) "+WherePackage+ " and lrb_type_id="+rs1231.getInt("lrb_type_id")+" order by package_sort_order");
					
					while(rs.next()){
					int unit_per_sku=0;
					PackageID=rs.getInt("package_id");
					//System.out.println("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1);
					
					ResultSet rs314 = s15.executeQuery("select sum(sales) sum_sales,sum(target_mtd) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and rsm_id="+RSMID1+" and snd_id="+SNDID1);
					if(rs314.first()){
						double CasesPercentage1=0;
						double Sales = rs314.getDouble("sum_sales");
						double Target = rs314.getDouble("sum_target");
						
						if(Target!=0){
							CasesPercentage1 = (((Sales * 1d)-(Target * 1d)) / (Target * 1d) )*100;
						}
						
						
						String NormalCasesCovertedPercentageTb3RSMColor1="";
						
						if(CasesPercentage1<0){
							NormalCasesCovertedPercentageTb3RSMColor1 = "#e79ca8";
						}else{
							NormalCasesCovertedPercentageTb3RSMColor1 = "#cbe9aa";//green
						}
						
						
					if(CasesPercentage1!=0){
						html +="<td style='text-align: center;background-color:"+NormalCasesCovertedPercentageTb3RSMColor1+";border-right:1px solid #fff;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesPercentage1)+"%</td>";
					}else{
						html +="<td style='text-align: center;background-color:#d7e0fd;border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
					}
						
						
					
					}
					}
				}
				
				
				
				html +="</tr>";
				
		
				int MonthNumber=0;
				int YearNumber=0;
				Date DisplayStartDate=new Date();
				Date DisplayEndDate=new Date();
				/*
				ResultSet rs99 = s20.executeQuery("select month,year,start_date,end_date from distributor_targets where distributor_id in (SELECT distributor_id FROM dist_targets_report_temp where rsm_id in("+RSMID1+")) and "+Utilities.getSQLDate(CurrentDate)+" between start_date and end_date and type_id = 2 limit 1");
				if(rs99.first()){
					 MonthNumber = rs99.getInt("month");
					 YearNumber = rs99.getInt("year");
					 DisplayStartDate = rs99.getDate("start_date");
					 DisplayEndDate = rs99.getDate("end_date");
				}
				
				html +="<td style='background-color:#ececda;font-weight: bold; border-right:1px solid #fff;font-size: 13px;' colspan='"+(TotalPackages-3)+"'>"+Utilities.getMonthNameByNumber(MonthNumber)+" "+YearNumber+"</td>";	
					
				html +="<td style='background-color:#ececda;font-weight: bold; border-right:1px solid #fff;font-size: 13px; text-align:right;' colspan='5'>"+Utilities.getDisplayDateFormat(DisplayStartDate)+" - "+Utilities.getDisplayDateFormat(DisplayEndDate)+"</td>";
		*/
	
		
		
		long TDMID1 = 0;
		String TDMName = "";
		
		
		
		ResultSet rs113 = s10.executeQuery("SELECT distinct tdm_id,(select display_name from users u where u.id=tdm_id) tdm_name FROM dist_targets_report_temp where rsm_id="+RSMID1+" and snd_id="+SNDID1);
		while(rs113.next()){
			TDMID1 = rs113.getLong("tdm_id");
			TDMName = rs113.getString("tdm_name");
			
			
			String TDMDisplay = TDMID1+ " - "+ TDMName;
			if (TDMID1 == 0){
				TDMDisplay = "Unassigned";
			}
			
			html +="<tr>";
			html +="<td style='font-weight: normal; background-color:#FCFCFC;border-right:1px solid #fff;font-size: 13px;'>"+Utilities.truncateStringToMax(TDMDisplay,22)+"</td>";	
						
						
						ResultSet rs315 = s8.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and tdm_id="+TDMID1+" and rsm_id="+RSMID1+" and snd_id="+SNDID1);
						if(rs315.first()){
							double CasesCovertedPercentage1=0;
							double SalesCoverted = rs315.getDouble("sum_converted");
							double TargetCoverted = rs315.getDouble("sum_targeted");
							
							if(TargetCoverted!=0){
								CasesCovertedPercentage1 = (((SalesCoverted * 1d)-(TargetCoverted * 1d)) / (TargetCoverted * 1d) )*100;
							}
						
						
							String ConsCasesCovertedPercentageTb3TDMColor1="";
							
							if(CasesCovertedPercentage1<0){
								ConsCasesCovertedPercentageTb3TDMColor1 = "#e79ca8";
							}else{
								ConsCasesCovertedPercentageTb3TDMColor1 = "#cbe9aa";//green
							}
							
						if(CasesCovertedPercentage1!=0){
							html +="<td style='background-color:"+ConsCasesCovertedPercentageTb3TDMColor1+"; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage1)+"%</td>";
						}else{
							html +="<td style='background-color:#FCFCFC; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'></td>";
						}
							
				
						}
				ResultSet rs123 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in (select sap_code from dist_targets_temp) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
				while(rs123.next()){
					
					int TypeID3 = rs123.getInt("lrb_type_id");
					ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in (select sap_code from dist_targets_temp) "+WherePackage+ " and lrb_type_id="+rs123.getInt("lrb_type_id")+" order by package_sort_order");
					
					while(rs.next()){
					int unit_per_sku=0;
					PackageID=rs.getInt("package_id");
					//System.out.println("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1);
					
					ResultSet rs314 = s8.executeQuery("select sum(sales) sum_sales,sum(target_mtd) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1+" and rsm_id="+RSMID1+" and snd_id="+SNDID1);
					if(rs314.first()){
						double CasesPercentage1=0;
						double Sales = rs314.getDouble("sum_sales");
						double Target = rs314.getDouble("sum_target");
						
						if(Target!=0){
							CasesPercentage1 = (((Sales * 1d)-(Target * 1d)) / (Target * 1d) )*100;
						}
						
						
						String NormCasesCovertedPercentageTb3TDMColor1="";
						
						if(CasesPercentage1<0){
							NormCasesCovertedPercentageTb3TDMColor1 = "#e79ca8";
						}else{
							NormCasesCovertedPercentageTb3TDMColor1 = "#cbe9aa";//green
						}
						
						
					if(CasesPercentage1!=0){
						html +="<td style='background-color:"+NormCasesCovertedPercentageTb3TDMColor1+"; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesPercentage1)+"%</td>";
					}else{
						html +="<td style='background-color:#FCFCFC; text-align: center;border-right:1px solid #fff;font-weight: normal; font-size: 13px;'></td>";
					}
					
						
						
					
					}
					}
				}
				
				
				
				html +="</tr>";
				
			
			
		
		
		
	
		
	
	
		
		}//end of tdm loop
		}//end of RSM loop
	    }//end of SND loop
	    
	    
	    
    
	    html +="</tr>";
	    html +="</thead>"; 
	    html +="<tbody>";				
						
	html +="</tbody>";
											
					
						
								
	html +="</table>";
	
	
	html +="</td>";
	html +="</tr>";
	html +="</table>";
		
		
		
		
		
		
		
		
		
			
	html += "<br><br>";
		
	html += "Please see attachment for details.";
			
			
			
		
		s.close();
		ds.dropConnection();	
		
		return html;
		
	}

}
