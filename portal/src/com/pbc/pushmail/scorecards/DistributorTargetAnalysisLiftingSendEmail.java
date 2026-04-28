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

public class DistributorTargetAnalysisLiftingSendEmail {
	
	
	
	public static void main(String[] args) {
		
		try{
			

			Datasource ds = new Datasource();
			ds.createConnectionToReplica();
			
			Statement s = ds.createStatement();
			
			Date ReportDate = Utilities.getDateByDays(-1);
			
			System.out.println("Started");
			//Date StartDate2 = Utilities.parseDate("01/09/2015");
			//Date EndDate2 = Utilities.parseDate("31/09/2015");
			
			s.executeUpdate("DROP TABLE IF EXISTS dist_targets_temp ");

		    s.executeUpdate("DROP TABLE IF EXISTS dist_targets_report_temp");
			
			
			s.executeUpdate("CREATE  TABLE dist_targets_temp (order_number varchar(100) , distributor_id varchar(100) ,distributor_name varchar(100) ,entry_date datetime ,order_date datetime ,fksak varchar(100) ,abgru varchar(100) ,posnr varchar(100) ,sap_code varchar(100) ,arktx varchar(100) , type varchar(100) , raw_case int, units int,total_units int , pstyv varchar(100) )");

			 s.executeUpdate("CREATE  TABLE dist_targets_report_temp (distributor_id int,distributor_name varchar(100),sales decimal(18,2),target decimal(18,2), percentage varchar(50),package_id int,type_id int,snd_id int, rsm_id int, tdm_id int, target_mtd decimal(18,2),month_target decimal(18,2))");
		   	
		   
		   
			if (true){ // Consolidated level
				
				String filename_DistributorTargetAnalysisLifting = "DistributorTargetAnalysis_Lifting_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
				
				
				s.executeUpdate("DELETE FROM dist_targets_temp");
				s.executeUpdate("DELETE FROM dist_targets_report_temp");
				
				DistributorTargetAnalysisLiftingPDF pack = new com.pbc.pushmail.scorecards.DistributorTargetAnalysisLiftingPDF();
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
				
				
				TargetPercentages = TargetPercentages.substring(0, TargetPercentages.length()-1); //removing last comma
				AttainPercentages = AttainPercentages.substring(0, AttainPercentages.length()-1); //removing last comma
				PackagesLabels = PackagesLabels.substring(0, PackagesLabels.length()-3); //removing last %7C
				PackagesLabelsAttainment = PackagesLabelsAttainment.substring(0, PackagesLabelsAttainment.length()-3); //removing last %7C
				
				
				String GraphString ="https://chart.googleapis.com/chart?cht=p3&chs=450x150&chd=t:"+TargetPercentages+"&chco=28495d|0c92e3|d9eaf5|87949c|5546ae|1acf6b|3b9262|b7f522|f5e922|f7bd1f|f78f1f|6a1d05&chl="+PackagesLabels;
				
				String GraphStringAttainment ="https://chart.googleapis.com/chart?cht=p3&chs=450x150&chd=t:"+AttainPercentages+"&chco=28495d|0c92e3|d9eaf5|87949c|5546ae|1acf6b|3b9262|b7f522|f5e922|f7bd1f|f78f1f|6a1d05&chl="+PackagesLabelsAttainment;
				
				////////////////////////////////////////////////////////////////////
				
				Utilities.sendPBCHTMLEmail(new String[]{"salman.baig@pbc.com.pk","khurram.jaffar@pbc.com.pk"},new String[]{"omerfk@pbc.com.pk","obaid@pbc.com.pk","anas.wahab@pbc.com.pk","shahrukh.salman@pbc.com.pk","ihsan@pbc.com.pk","anjum.a.ansari@gmail.com","jazeb@pbc.com.pk","sohaib.zahid@pbc.com.pk","abdul.basit@pbc.com.pk"}, null, "Lifting Attainment Report | Consolidated | "+Utilities.getDisplayDateFormat(ReportDate), getHTMLMessage(-1, pack,ReportDate, false,GraphString,GraphStringAttainment), new String[]{filename_DistributorTargetAnalysisLifting});
				//Utilities.sendPBCHTMLEmail(new String[]{"zulqurnan.aslam@pbc.com.pk","anas.wahab@pbc.com.pk","obaid@pbc.com.pk","jazeb@pbc.com.pk"},null, null, "Lifting Attainment Report | Consolidated | "+Utilities.getDisplayDateFormat(ReportDate), getHTMLMessage(-1, pack,ReportDate, false,GraphString,GraphStringAttainment), new String[]{filename_DistributorTargetAnalysisLifting});
				
			}
		   
			
			
			String SDTitle = "";
			for(int i=1;i<=4;i++){

				SDTitle = "SD"+i;
				String filename_DistributorTargetAnalysisLifting = "DistributorTargetAnalysis_"+SDTitle+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
				
				if(i==3){
					i++;
				}
				
				
				
				s.executeUpdate("DELETE FROM dist_targets_temp");
				s.executeUpdate("DELETE FROM dist_targets_report_temp");
				
				DistributorTargetAnalysisLiftingPDF pack = new com.pbc.pushmail.scorecards.DistributorTargetAnalysisLiftingPDF();
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
				
				Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(i).USER_EMAIL},new String[]{"obaid@pbc.com.pk","anas.wahab@pbc.com.pk","salman.baig@pbc.com.pk","khurram.jaffar@pbc.com.pk","ihsan@pbc.com.pk","shahrukh.salman@pbc.com.pk","abdul.basit@pbc.com.pk","sohaib.zahid@pbc.com.pk"},null, "Lifting Attainment Report | "+SDTitle+" | "+Utilities.getDisplayDateFormat(ReportDate), getHTMLMessage(EmployeeHierarchy.getSDHead(i).USER_ID, pack,ReportDate, false,GraphString,GraphStringAttainment), new String[]{filename_DistributorTargetAnalysisLifting});
				
			///////////	Utilities.sendPBCHTMLEmail(new String[]{"anas.wahab@pbc.com.pk","zulqurnan.aslam@pbc.com.pk"},null,null, "Lifting Attainment Report | "+SDTitle+" | "+Utilities.getDisplayDateFormat(ReportDate), getHTMLMessage(EmployeeHierarchy.getSDHead(i).USER_ID, pack,ReportDate, false,GraphString,GraphStringAttainment), new String[]{filename_DistributorTargetAnalysisLifting});
			
				
				
				
				
				
				//break;
			}
			
			
			/*
			User RSMs[] = EmployeeHierarchy.getRSMs();
			for (User user: RSMs){
				
				if (user.USER_EMAIL != null && user.USER_ID != EmployeeHierarchy.getSDHead(4).USER_ID){
					
					User SND = EmployeeHierarchy.getSND(user.USER_ID);
					
					String filename_DistributorTargetAnalysisLifting = "DistributorTargetAnalysis_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
					
					s.executeUpdate("DELETE FROM dist_targets_temp");
					s.executeUpdate("DELETE FROM dist_targets_report_temp");
					
					DistributorTargetAnalysisLiftingPDF pack = new com.pbc.pushmail.scorecards.DistributorTargetAnalysisLiftingPDF();
					pack.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_DistributorTargetAnalysisLifting, user.USER_ID, ReportDate);
					
					
					
					//////////Utilities.sendPBCHTMLEmail(new String[]{user.USER_EMAIL},new String[]{SND.USER_EMAIL,"obaid@pbc.com.pk","anas.wahab@pbc.com.pk","salman.baig@pbc.com.pk","zahoor@pbc.com.pk","ihsan@pbc.com.pk"}, null, "Lifting Attainment Report | "+user.REGION_NAME+" | "+Utilities.getDisplayDateFormat(ReportDate), getHTMLMessage(user.USER_ID, pack,ReportDate, true,"",""), new String[]{filename_DistributorTargetAnalysisLifting});
					
					/////Utilities.sendPBCHTMLEmail(new String[]{"zulqurnan.aslam@pbc.com.pk","anas.wahab@pbc.com.pk"},null, null, "Lifting Attainment Report | "+user.REGION_NAME+" | "+Utilities.getDisplayDateFormat(ReportDate), getHTMLMessage(user.USER_ID, pack,ReportDate, true,"",""), new String[]{filename_DistributorTargetAnalysisLifting});
					
					
					
					
					
				}
				
			}*/
			
		/*	
			
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
						
						DistributorTargetAnalysisLiftingPDF pack = new com.pbc.pushmail.scorecards.DistributorTargetAnalysisLiftingPDF();
						pack.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_DistributorTargetAnalysis, user.USER_ID, ReportDate);
						
						/////////////////////Utilities.sendPBCHTMLEmail(new String[]{user.USER_EMAIL},new String[]{RSM.USER_EMAIL,"ihsan@pbc.com.pk","zahoor@pbc.com.pk"}, null, "Lifting Attainment Report | "+user.REGION_NAME+" | "+user.USER_DISPLAY_NAME+" | "+Utilities.getDisplayDateFormat(ReportDate), getHTMLMessage(user.USER_ID, pack,ReportDate, true,"",""), new String[]{filename_DistributorTargetAnalysis});
						
						/////////Utilities.sendPBCHTMLEmail(new String[]{"zulqurnan.aslam@pbc.com.pk","anas.wahab@pbc.com.pk"},null, null, "Lifting Attainment Report | "+user.REGION_NAME+" | "+user.USER_DISPLAY_NAME+" | "+Utilities.getDisplayDateFormat(ReportDate), getHTMLMessage(user.USER_ID, pack,ReportDate, true,"",""), new String[]{filename_DistributorTargetAnalysis});
					
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

	
	public static String getHTMLMessage(long EmployeeID, DistributorTargetAnalysisLiftingPDF pack, Date CurrentDate, boolean isRSM,String SNDGraphTarget,String SNDGraphAttainment) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		double GraphOverallPercentage = 0;
		ArrayList<String> GraphRSMName = new <String>ArrayList();
		ArrayList<Double> GraphRSMPercentage = new <Double>ArrayList();
		
		double GraphOverallSNDPercentage = 0;
		ArrayList<String> GraphSNDName = new <String>ArrayList();
		ArrayList<Double> GraphSNDPercentage = new <Double>ArrayList();
		
		Date MonthStartDate = Utilities.getStartDateByDate(CurrentDate);
		Date MonthEndDate = Utilities.getEndDateByDate(CurrentDate);
		
		
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
		
		//old Hirar
		/*
		long SNDID111 = 3427;
		String SNDName11= "";
		String SNDDisplay11="";
		String SND1Short="SD2";
		
		
		
		long SNDID112 = 3848;
		String SNDName12= "";
		String SNDDisplay12="";
		String SND2Short="SD3";
		
		
		long SNDID113 = 3845;
		String SNDName13= "";
		String SNDDisplay13="";
		String SND3Short="SD1";
		
		long SNDID114 = 2646;
		String SNDName14= "";
		String SNDDisplay14="";
		String SND4Short="AQUAFINA";
		
		
		
		
		long SNDID115 = 3644;
		String SNDName15= "";
		String SNDDisplay15="";
		String SND5Short="KA";

		
		long SNDID116 = 3645;
		String SNDName16= "";
		String SNDDisplay16="";
		String SND6Short="SD4";
		
		*/
		
		
		
		long SNDID111 = 3845;
		String SNDName11= "";
		String SNDDisplay11="";
		String SND1Short="SD2";
		
		
		
		long SNDID112 = 3427;
		String SNDName12= "";
		String SNDDisplay12="";
		String SND2Short="SD3";
		
		
		long SNDID113 = 3987;
		String SNDName13= "";
		String SNDDisplay13="";
		String SND3Short="SD1";
		
		long SNDID114 = 2646;
		String SNDName14= "";
		String SNDDisplay14="";
		String SND4Short="AQUAFINA";
		
		
		long SNDID115 = 3848;
		String SNDName15= "";
		String SNDDisplay15="";
		String SND5Short="KA";

		
		long SNDID116 = 3645;
		String SNDName16= "";
		String SNDDisplay16="";
		String SND6Short="SD4";
		
		
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
		Date MinTargetDate = new Date();
		Date MaxTargetDate = new Date();
	   
		//System.out.println();
		
		
		ResultSet rs141 = s3.executeQuery("select min(dt.start_date), max(dt.end_date) from distributor_targets dt where date("+Utilities.getSQLDate(CurrentDate)+") between dt.start_date and dt.end_date and dt.type_id=2");
	    if(rs141.first()){
	    	MinTargetDate = rs141.getDate(1);
	    	MaxTargetDate = rs141.getDate(2); //Utilities.parseDate("10/01/2014");//rs14.getDate(1);
	    }
		
		
		String WherePackage = "";
		
		String SAPCodes = "0";

	    //System.out.println("SELECT group_concat(distinct sap_code) from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDate(MinTargetDate)+" and "+Utilities.getSQLDate(MaxTargetDate));
	    
		
		ResultSet rs601 = s.executeQuery("SELECT group_concat(distinct sap_code) from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDateLifting(MinTargetDate)+" and "+Utilities.getSQLDateNextLifting(MaxTargetDate));
		if (rs601.next()){
			SAPCodes = rs601.getString(1);
		}
		
		
		//System.out.println(SAPCodes);
		
		String html = "<html>";
		html += "<body>";
		
		
		html +="<p style='text-align:left; font-weight:bold; padding-left:5px;'>MTD Target Attainment Summary</p>";		
		
		html +="<br>";
		
		html +="<span style='display: none' id='HGraphs'></span>";
		
		html +="<br>";

		
		
		
		html +="<br/><br/>";
		
		html +="<table style='width: 900px; margin-top:-8px; ' cellpadding='2' cellspacing='1'>";
		
		
		
		
		
		html +="<tr>";
			
		html +="<td style='width: 100%' valign='top'>";
		
		// Top Summary: Start
		if (!isRSM && EmployeeID == -1){
		
			
//////////////////New table ///////////////
			
			
	html +="<table border='0' style='border-collapse: collapse; border:1px solid #dee0e1;width:900px;font-size: 14px;'>";
	
	html +="<tr>";
		html +="<td  valign='top' style='background-color: #3d5ab3; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold; color:white; width:120px;'></td>";
		html +="<td  valign='top' style='background-color: #3d5ab3; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold; color:white; width:116px;'>Consolidated</td>";
		
		html +="<td  valign='top' style='background-color: #3d5ab3; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold; color:white; width:116px;'>"+SND3Short+"</td>";
		html +="<td  valign='top' style='background-color: #3d5ab3; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold; color:white; width:116px;'>"+SND1Short+"</td>";		
		html +="<td  valign='top' style='background-color: #3d5ab3; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold; color:white; width:116px;'>"+SND2Short+"</td>";
		
		
		
		//html +="<td  valign='top' style='background-color: #3d5ab3; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold; color:white; width:116px;'>"+SND4Short+"</td>";
		
		
		html +="<td  valign='top' style='background-color: #3d5ab3; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold; color:white; width:116px;'>"+SND6Short+"</td>";
		
		html +="<td  valign='top' style='background-color: #3d5ab3; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold; color:white; width:116px;'>"+SND5Short+"</td>";
		
		
		
	html +="</tr>";
		
	html +="<tr>";
	
	
	
	
	double CasesCovertedPercentage11LRBCons=0;
	double CasesCovertedPercentage11OverallLRBCons=0;
	double SalesCoverted1LRBCons = 0;
	double TargetCoverted1LRBCons = 0;
	double TargetCoverted1OverallLRBCons = 0;
	
	double MonthTargetCoverted1LRBCons = 0;
	
	if(1==1){
	
	ResultSet rs315312 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted, sum(target) target,sum(month_target) sum_targeted_month from dist_targets_report_temp where package_id=-1 and type_id=-1 and (snd_id in (SELECT snd_id FROM pep.common_sd_groups where snd_id is not null) or snd_id = 2646)"); //-1 for all
	if(rs315312.first()){
		 CasesCovertedPercentage11LRBCons=0;
		 CasesCovertedPercentage11OverallLRBCons=0;
		 SalesCoverted1LRBCons = rs315312.getDouble("sum_converted");
		 TargetCoverted1LRBCons = rs315312.getDouble("sum_targeted");
		 MonthTargetCoverted1LRBCons = rs315312.getDouble("sum_targeted_month");
		 TargetCoverted1OverallLRBCons = rs315312.getDouble("target");
		
		if(TargetCoverted1LRBCons!=0){
			CasesCovertedPercentage11LRBCons = ((SalesCoverted1LRBCons * 1d) / (TargetCoverted1LRBCons * 1d) )*100;
		}
		if(TargetCoverted1OverallLRBCons!=0){
			CasesCovertedPercentage11OverallLRBCons = ((SalesCoverted1LRBCons * 1d) / (TargetCoverted1OverallLRBCons * 1d) )*100;
		}
		

	}
	}
//////
	
	double CasesCovertedPercentage11LRBS1=0;
	double CasesCovertedPercentage11OverallLRBS1=0;
	double SalesCoverted1LRBS1 =0;
	double TargetCoverted1LRBS1 = 0;
	double MonthTargetCoverted1LRBS1 = 0;
	double TargetCoverted1OverallLRBS1=0;
	double LRBS1 = 0;
	
	if(!isRSM){
	ResultSet rs31531 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted, sum(target) target,sum(month_target) sum_targeted_month from dist_targets_report_temp where package_id=-1 and type_id=-1 and snd_id="+SNDID111); //-1 for all
	if(rs31531.first()){
		 CasesCovertedPercentage11LRBS1=0;
		 CasesCovertedPercentage11OverallLRBS1=0;
		 SalesCoverted1LRBS1 = rs31531.getDouble("sum_converted");
		 TargetCoverted1LRBS1 = rs31531.getDouble("sum_targeted");
		 MonthTargetCoverted1LRBS1 = rs31531.getDouble("sum_targeted_month");
		 TargetCoverted1OverallLRBS1 = rs31531.getDouble("target");
		
		if(TargetCoverted1LRBS1!=0){
			CasesCovertedPercentage11LRBS1 = ((SalesCoverted1LRBS1 * 1d) / (TargetCoverted1LRBS1 * 1d) )*100;
		}
		if(TargetCoverted1OverallLRBS1!=0){
			CasesCovertedPercentage11OverallLRBS1 = ((SalesCoverted1LRBS1 * 1d) / (TargetCoverted1OverallLRBS1 * 1d) )*100;
		}


	}
	}
	
	
	
	double CasesCovertedPercentage11LRBS2=0;
	double CasesCovertedPercentage11OverallLRBS2=0;
	double SalesCoverted1LRBS2 =0;
	double TargetCoverted1LRBS2 = 0;
	double MonthTargetCoverted1LRBS2 = 0;
	double TargetCoverted1OverallLRBS2=0;
	double LRBS2 = 0;
	
	if(!isRSM){
	ResultSet rs31531 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted, sum(target) target,sum(month_target) sum_targeted_month from dist_targets_report_temp where package_id=-1 and type_id=-1 and snd_id="+SNDID112); //-1 for all
	if(rs31531.first()){
		 CasesCovertedPercentage11LRBS2=0;
		 CasesCovertedPercentage11OverallLRBS2=0;
		 SalesCoverted1LRBS2 = rs31531.getDouble("sum_converted");
		 TargetCoverted1LRBS2 = rs31531.getDouble("sum_targeted");
		 MonthTargetCoverted1LRBS2 = rs31531.getDouble("sum_targeted_month");
		 TargetCoverted1OverallLRBS2 = rs31531.getDouble("target");
		
		if(TargetCoverted1LRBS2!=0){
			CasesCovertedPercentage11LRBS2 = ((SalesCoverted1LRBS2 * 1d) / (TargetCoverted1LRBS2 * 1d) )*100;
		}
		if(TargetCoverted1OverallLRBS2!=0){
			CasesCovertedPercentage11OverallLRBS2 = ((SalesCoverted1LRBS2 * 1d) / (TargetCoverted1OverallLRBS2 * 1d) )*100;
		}


	}
	}
	
	
	double CasesCovertedPercentage11LRBS3=0;
	double CasesCovertedPercentage11OverallLRBS3=0;
	double SalesCoverted1LRBS3 =0;
	double TargetCoverted1LRBS3 = 0;
	double MonthTargetCoverted1LRBS3 = 0;
	double TargetCoverted1OverallLRBS3=0;
	double LRBS3 = 0;
	
	if(!isRSM){
	ResultSet rs31531 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted, sum(target) target,sum(month_target) sum_targeted_month from dist_targets_report_temp where package_id=-1 and type_id=-1 and snd_id="+SNDID113); //-1 for all
	if(rs31531.first()){
		 CasesCovertedPercentage11LRBS3=0;
		 CasesCovertedPercentage11OverallLRBS3=0;
		 SalesCoverted1LRBS3 = rs31531.getDouble("sum_converted");
		 TargetCoverted1LRBS3 = rs31531.getDouble("sum_targeted");
		 MonthTargetCoverted1LRBS3 = rs31531.getDouble("sum_targeted_month");
		 TargetCoverted1OverallLRBS3 = rs31531.getDouble("target");
		
		if(TargetCoverted1LRBS3!=0){
			CasesCovertedPercentage11LRBS3 = ((SalesCoverted1LRBS3 * 1d) / (TargetCoverted1LRBS3 * 1d) )*100;
		}
		if(TargetCoverted1OverallLRBS3!=0){
			CasesCovertedPercentage11OverallLRBS3 = ((SalesCoverted1LRBS3 * 1d) / (TargetCoverted1OverallLRBS3 * 1d) )*100;
		}


	}
	}
	
	
	double CasesCovertedPercentage11LRBS4=0;
	double CasesCovertedPercentage11OverallLRBS4=0;
	double SalesCoverted1LRBS4 =0;
	double TargetCoverted1LRBS4 = 0;
	double MonthTargetCoverted1LRBS4 = 0;
	double TargetCoverted1OverallLRBS4=0;
	double LRBS4 = 0;
	
	if(!isRSM){
	ResultSet rs31531 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted, sum(target) target,sum(month_target) sum_targeted_month from dist_targets_report_temp where package_id=-1 and type_id=-1 and snd_id="+SNDID114); //-1 for all
	if(rs31531.first()){
		 CasesCovertedPercentage11LRBS4=0;
		 CasesCovertedPercentage11OverallLRBS4=0;
		 SalesCoverted1LRBS4 = rs31531.getDouble("sum_converted");
		 TargetCoverted1LRBS4 = rs31531.getDouble("sum_targeted");
		 MonthTargetCoverted1LRBS4 = rs31531.getDouble("sum_targeted_month");
		 TargetCoverted1OverallLRBS4 = rs31531.getDouble("target");
		
		if(TargetCoverted1LRBS4!=0){
			CasesCovertedPercentage11LRBS4 = ((SalesCoverted1LRBS4 * 1d) / (TargetCoverted1LRBS4 * 1d) )*100;
		}
		if(TargetCoverted1OverallLRBS4!=0){
			CasesCovertedPercentage11OverallLRBS4 = ((SalesCoverted1LRBS4 * 1d) / (TargetCoverted1OverallLRBS4 * 1d) )*100;
		}


	}
	}
	
	
	
	
	double CasesCovertedPercentage11LRBS5=0;
	double CasesCovertedPercentage11OverallLRBS5=0;
	double SalesCoverted1LRBS5 =0;
	double TargetCoverted1LRBS5 = 0;
	double MonthTargetCoverted1LRBS5 = 0;
	double TargetCoverted1OverallLRBS5=0;
	double LRBS5 = 0;
	
	if(!isRSM){
	ResultSet rs31531 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted, sum(target) target,sum(month_target) sum_targeted_month from dist_targets_report_temp where package_id=-1 and type_id=-1 and snd_id="+SNDID115); //-1 for all
	if(rs31531.first()){
		 CasesCovertedPercentage11LRBS5=0;
		 CasesCovertedPercentage11OverallLRBS5=0;
		 SalesCoverted1LRBS5 = rs31531.getDouble("sum_converted");
		 TargetCoverted1LRBS5 = rs31531.getDouble("sum_targeted");
		 MonthTargetCoverted1LRBS5 = rs31531.getDouble("sum_targeted_month");
		 TargetCoverted1OverallLRBS5 = rs31531.getDouble("target");
		
		if(TargetCoverted1LRBS5!=0){
			CasesCovertedPercentage11LRBS5 = ((SalesCoverted1LRBS5 * 1d) / (TargetCoverted1LRBS5 * 1d) )*100;
		}
		if(TargetCoverted1OverallLRBS5!=0){
			CasesCovertedPercentage11OverallLRBS5 = ((SalesCoverted1LRBS5 * 1d) / (TargetCoverted1OverallLRBS5 * 1d) )*100;
		}


	}
	}
	
	
	
	
	double CasesCovertedPercentage11LRBS6=0;
	double CasesCovertedPercentage11OverallLRBS6=0;
	double SalesCoverted1LRBS6 =0;
	double TargetCoverted1LRBS6 = 0;
	double MonthTargetCoverted1LRBS6 = 0;
	double TargetCoverted1OverallLRBS6=0;
	double LRBS6 = 0;
	
	if(!isRSM){
	ResultSet rs31531 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted, sum(target) target,sum(month_target) sum_targeted_month from dist_targets_report_temp where package_id=-1 and type_id=-1 and snd_id="+SNDID116); //-1 for all
	if(rs31531.first()){
		 CasesCovertedPercentage11LRBS6=0;
		 CasesCovertedPercentage11OverallLRBS6=0;
		 SalesCoverted1LRBS6 = rs31531.getDouble("sum_converted");
		 TargetCoverted1LRBS6 = rs31531.getDouble("sum_targeted");
		 MonthTargetCoverted1LRBS6 = rs31531.getDouble("sum_targeted_month");
		 TargetCoverted1OverallLRBS6 = rs31531.getDouble("target");
		
		if(TargetCoverted1LRBS6!=0){
			CasesCovertedPercentage11LRBS6 = ((SalesCoverted1LRBS6 * 1d) / (TargetCoverted1LRBS6 * 1d) )*100;
		}
		if(TargetCoverted1OverallLRBS6!=0){
			CasesCovertedPercentage11OverallLRBS6 = ((SalesCoverted1LRBS6 * 1d) / (TargetCoverted1OverallLRBS6 * 1d) )*100;
		}


	}
	}
	
	
	
	//////////////////////////////////////////// Yesterday Lifting ///////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	Date YesterdayDate = Utilities.getDateByDays(CurrentDate, -1);  //Getting Yesterday
	
	
	//System.out.println("This is yesterday date - "+YesterdayDate);
	
	
	double SD1YesterdayLifting =0;
	
	//System.out.println("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3845)) and created_on between date_format("+Utilities.getSQLDateLifting(YesterdayDate)+",'%Y-%m-%d %H:%i') and date_format("+Utilities.getSQLDateNextLifting(YesterdayDate)+",'%Y-%m-%d %H:%i')");
	
	//System.out.println("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3845)) and created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
	
	ResultSet rs34 = s.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3848)) and created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
	if(rs34.first()){
		SD1YesterdayLifting = rs34.getDouble("total_lifting");
	}
	
	

	double SD2YesterdayLifting =0;
	
	//System.out.println("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (2262)) and created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
	
	ResultSet rs35 = s.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3845)) and created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
	if(rs35.first()){
		SD2YesterdayLifting = rs35.getDouble("total_lifting");
	}
	
	
	

	double SD3YesterdayLifting =0;
	
	//System.out.println("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3848)) and created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
	
	ResultSet rs36 = s.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3427)) and created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
	if(rs36.first()){
		SD3YesterdayLifting = rs36.getDouble("total_lifting");
	}
	
	
	

	double SD4YesterdayLifting =0;
	
	//System.out.println("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (2646)) and created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
	
	ResultSet rs37 = s.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (2646)) and created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
	if(rs37.first()){
		SD4YesterdayLifting = rs37.getDouble("total_lifting");
	}
	
	
	
double SD5YesterdayLifting =0;
	
	//System.out.println("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3848)) and created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
	
	ResultSet rs365 = s.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3987)) and created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
	if(rs365.first()){
		SD5YesterdayLifting = rs365.getDouble("total_lifting");
	}
	
	
	
double SD6YesterdayLifting =0;
	
	//System.out.println("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3848)) and created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
	
	ResultSet rs366 = s.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (3645)) and created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
	if(rs366.first()){
		SD6YesterdayLifting = rs366.getDouble("total_lifting");
	}
	
	
	Double ConsYesterdayLifting = SD1YesterdayLifting+SD2YesterdayLifting+SD3YesterdayLifting+SD4YesterdayLifting+SD5YesterdayLifting+SD6YesterdayLifting;
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	html +="<tr>";
	html +="<td  valign='top'  style='background-color: #c8cfe6; height: 18px; border:1px solid #dee0e1;t1ext-align:center; font-weight: normal;'>Yesterday Lifting</td>";
	if(TargetCoverted1LRBCons!=0){
		html +="<td style='text-align: center; b1ackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(ConsYesterdayLifting)+"</td>";
	}else{
		html +="<td style='text-align: center; b1ackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
	}
	
	if(TargetCoverted1LRBS3!=0){
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SD1YesterdayLifting)+"</td>";
	}else{
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
	}
	
	if(TargetCoverted1LRBS1!=0){
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SD2YesterdayLifting)+"</td>";
	}else{
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
	}
	
	if(TargetCoverted1LRBS2!=0){
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SD3YesterdayLifting)+"</td>";
	}else{
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
	}
	
	
	
	/*if(TargetCoverted1LRBS4!=0){
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SD4YesterdayLifting)+"</td>";
	}else{
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
	}*/
	
	if(TargetCoverted1LRBS6!=0){
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SD6YesterdayLifting)+"</td>";
	}else{
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
	}
	
	if(TargetCoverted1LRBS5!=0){
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SD5YesterdayLifting)+"</td>";
	}else{
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
	}
	
	
	
	
html +="</tr>";
	
	
	
	
	html +="<tr>";
	html +="<td  valign='top'  style='background-color: #c8cfe6; height: 18px; border:1px solid #dee0e1;t1ext-align:center; font-weight: normal;'>Target MTD</td>";
	if(TargetCoverted1LRBCons!=0){
		html +="<td style='text-align: center; b1ackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1LRBCons)+"</td>";
	}else{
		html +="<td style='text-align: center; b1ackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
	}
	
	if(TargetCoverted1LRBS3!=0){
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1LRBS3)+"</td>";
	}else{
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
	}
	
	if(TargetCoverted1LRBS1!=0){
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1LRBS1)+"</td>";
	}else{
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
	}
	
	if(TargetCoverted1LRBS2!=0){
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1LRBS2)+"</td>";
	}else{
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
	}
	
	
/*	
	if(TargetCoverted1LRBS4!=0){
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1LRBS4)+"</td>";
	}else{
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
	}*/
	
	if(TargetCoverted1LRBS6!=0){
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1LRBS6)+"</td>";
	}else{
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
	}
	
	if(TargetCoverted1LRBS5!=0){
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1LRBS5)+"</td>";
	}else{
		html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
	}
	
	
	
	
html +="</tr>";


html +="<tr>";
html +="<td  valign='top'  style='background-color: #c8cfe6; height: 18px; border:1px solid #dee0e1;t1ext-align:center; font-weight: normal;'>Attainment</td>";
if(SalesCoverted1LRBCons!=0){
	html +="<td style='text-align: center; 1background-color:#C2C2C2;border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1LRBCons)+"</td>";
}else{
	html +="<td style='text-align: center; b1ackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
}


if(SalesCoverted1LRBS3!=0){
	html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1LRBS3)+"</td>";
}else{
	html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
}

if(SalesCoverted1LRBS1!=0){
	html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1LRBS1)+"</td>";
}else{
	html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
}

if(SalesCoverted1LRBS2!=0){
	html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1LRBS2)+"</td>";
}else{
	html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
}

/*
if(SalesCoverted1LRBS4!=0){
	html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1LRBS4)+"</td>";
}else{
	html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
}
*/


if(SalesCoverted1LRBS6!=0){
	html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1LRBS6)+"</td>";
}else{
	html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
}

if(SalesCoverted1LRBS5!=0){
	html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1LRBS5)+"</td>";
}else{
	html +="<td style='text-align: center; abackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
}





html +="</tr>";

html +="<tr>";
html +="<td  valign='top'  style='background-color: #c8cfe6; height: 18px; border:1px solid #dee0e1;t1ext-align:center; font-weight: normal;'>MTD (%)</td>";
GraphOverallSNDPercentage = CasesCovertedPercentage11LRBCons;


String CasesCovertedPercentage11LRBConsColor="";
String TextColorGen11="black";



if(CasesCovertedPercentage11LRBCons>100){
	CasesCovertedPercentage11LRBConsColor="#D6FFEE";
	TextColorGen11 = "black";
}else if(CasesCovertedPercentage11LRBCons>=80 && CasesCovertedPercentage11LRBCons<=100){
	CasesCovertedPercentage11LRBConsColor="#FBFFD6";
	TextColorGen11 = "black";
}else{
	CasesCovertedPercentage11LRBConsColor="#FFD6E7";
	TextColorGen11 = "black";
}


if(CasesCovertedPercentage11LRBCons!=0){
	html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11LRBConsColor+"; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px; color:"+TextColorGen11+"'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11LRBCons)+"%</td>";
}else{
	html +="<td style='text-align: center; background-color:#d9dce7; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
}


String CasesCovertedPercentage11LRBS3Color="";
String TextColorGen="black";



if(CasesCovertedPercentage11LRBS3>100){
	CasesCovertedPercentage11LRBS3Color="#D6FFEE";
	TextColorGen = "black";
}else if(CasesCovertedPercentage11LRBS3>=80 && CasesCovertedPercentage11LRBS3<=100){
	CasesCovertedPercentage11LRBS3Color="#FBFFD6";
	TextColorGen = "black";
}else{
	CasesCovertedPercentage11LRBS3Color="#FFD6E7";
	TextColorGen = "black";
}



String CasesCovertedPercentage11LRBS1Color="";

if(CasesCovertedPercentage11LRBS1>100){
	CasesCovertedPercentage11LRBS1Color="#D6FFEE";
	TextColorGen = "black";
}else if(CasesCovertedPercentage11LRBS1>=80 && CasesCovertedPercentage11LRBS1<=100){
	CasesCovertedPercentage11LRBS1Color="#FBFFD6";
	TextColorGen = "black";
}else{
	CasesCovertedPercentage11LRBS1Color="#FFD6E7";
	TextColorGen = "black";
}


String CasesCovertedPercentage11LRBS2Color="";

if(CasesCovertedPercentage11LRBS2>100){
	CasesCovertedPercentage11LRBS2Color="#D6FFEE";
	TextColorGen = "black";
}else if(CasesCovertedPercentage11LRBS2>=80 && CasesCovertedPercentage11LRBS2<=100){
	CasesCovertedPercentage11LRBS2Color="#FBFFD6";
	TextColorGen = "black";
}else{
	CasesCovertedPercentage11LRBS2Color="#FFD6E7";
	TextColorGen = "black";
}


String CasesCovertedPercentage11LRBS4Color="";

if(CasesCovertedPercentage11LRBS4>100){
	CasesCovertedPercentage11LRBS4Color="#D6FFEE";
	TextColorGen = "black";
}else if(CasesCovertedPercentage11LRBS4>=80 && CasesCovertedPercentage11LRBS4<=100){
	CasesCovertedPercentage11LRBS4Color="#FBFFD6";
	TextColorGen = "black";
}else{
	CasesCovertedPercentage11LRBS4Color="#FFD6E7";
	TextColorGen = "black";
}


String CasesCovertedPercentage11LRBS5Color="";
String TextColorGenS5="black";



if(CasesCovertedPercentage11LRBS5>100){
	CasesCovertedPercentage11LRBS5Color="#D6FFEE";
	TextColorGenS5 = "black";
}else if(CasesCovertedPercentage11LRBS5>=80 && CasesCovertedPercentage11LRBS5<=100){
	CasesCovertedPercentage11LRBS5Color="#FBFFD6";
	TextColorGenS5 = "black";
}else{
	CasesCovertedPercentage11LRBS5Color="#FFD6E7";
	TextColorGenS5 = "black";
}



String CasesCovertedPercentage11LRBS6Color="";
String TextColorGenS6="black";

if(CasesCovertedPercentage11LRBS6>100){
	CasesCovertedPercentage11LRBS6Color="#D6FFEE";
	TextColorGenS6 = "black";
}else if(CasesCovertedPercentage11LRBS6>=80 && CasesCovertedPercentage11LRBS6<=100){
	CasesCovertedPercentage11LRBS6Color="#FBFFD6";
	TextColorGenS6 = "black";
}else{
	CasesCovertedPercentage11LRBS6Color="#FFD6E7";
	TextColorGenS6 = "black";
}



if(CasesCovertedPercentage11LRBS3!=0){
	html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11LRBS3Color+"; color:"+TextColorGen+"; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11LRBS3)+"%</td>";
}else{
	html +="<td style='text-align: center; background-color:#d9dce7; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
}
if(CasesCovertedPercentage11LRBS1!=0){
	html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11LRBS1Color+"; color:"+TextColorGen+"; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11LRBS1)+"%</td>";
}else{
	html +="<td style='text-align: center; background-color:#d9dce7; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
}

if(CasesCovertedPercentage11LRBS2!=0){
	html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11LRBS2Color+"; color:"+TextColorGen+"; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11LRBS2)+"%</td>";
}else{
	html +="<td style='text-align: center; background-color:#d9dce7; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
}


/*
if(CasesCovertedPercentage11LRBS4!=0){
	html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11LRBS4Color+"; color:"+TextColorGen+"; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11LRBS4)+"%</td>";
}else{
	html +="<td style='text-align: center; background-color:#d9dce7; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
}

*/

if(CasesCovertedPercentage11LRBS6!=0){
	html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11LRBS6Color+"; color:"+TextColorGenS6+"; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11LRBS6)+"%</td>";
}else{
	html +="<td style='text-align: center; background-color:#d9dce7; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
}

if(CasesCovertedPercentage11LRBS5!=0){
	html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11LRBS5Color+"; color:"+TextColorGenS5+"; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11LRBS5)+"%</td>";
}else{
	html +="<td style='text-align: center; background-color:#d9dce7; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
}




html +="</tr>";





html +="<tr>";
html +="<td  valign='top'  style='background-color: #c8cfe6; height: 18px; border:1px solid #dee0e1;t1ext-align:center; font-weight: normal;'>Month Target</td>";
GraphOverallSNDPercentage = CasesCovertedPercentage11LRBCons;
if(MonthTargetCoverted1LRBCons!=0){
	html +="<td style='text-align: center; b1ackground-color:#d9dce7; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(MonthTargetCoverted1LRBCons)+"</td>";
}else{
	html +="<td style='text-align: center; b1ackground-color:#d9dce7; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
}


if(MonthTargetCoverted1LRBS3!=0){
	html +="<td style='text-align: center; b1ackground-color:#d9dce7; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(MonthTargetCoverted1LRBS3)+"</td>";
}else{
	html +="<td style='text-align: center; b1ackground-color:#d9dce7; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
}
if(MonthTargetCoverted1LRBS1!=0){
	html +="<td style='text-align: center; b1ackground-color:#d9dce7; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(MonthTargetCoverted1LRBS1)+"</td>";
}else{
	html +="<td style='text-align: center; b1ackground-color:#d9dce7; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
}

if(MonthTargetCoverted1LRBS2!=0){
	html +="<td style='text-align: center; b1ackground-color:#d9dce7; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(MonthTargetCoverted1LRBS2)+"</td>";
}else{
	html +="<td style='text-align: center; b1ackground-color:#d9dce7; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
}

/*

if(MonthTargetCoverted1LRBS4!=0){
	html +="<td style='text-align: center; b1ackground-color:#d9dce7; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(MonthTargetCoverted1LRBS4)+"</td>";
}else{
	html +="<td style='text-align: center; b1ackground-color:#d9dce7; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
}

*/

if(MonthTargetCoverted1LRBS6!=0){
	html +="<td style='text-align: center; b1ackground-color:#d9dce7; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(MonthTargetCoverted1LRBS6)+"</td>";
}else{
	html +="<td style='text-align: center; b1ackground-color:#d9dce7; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
}

if(MonthTargetCoverted1LRBS5!=0){
	html +="<td style='text-align: center; b1ackground-color:#d9dce7; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(MonthTargetCoverted1LRBS5)+"</td>";
}else{
	html +="<td style='text-align: center; b1ackground-color:#d9dce7; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
}




html +="</tr>";










String TextColorGen1="black";

String CasesCovertedPercentage11OverallLRBConsColor="";

if(CasesCovertedPercentage11OverallLRBCons>100){
	CasesCovertedPercentage11OverallLRBConsColor="#D6FFEE";
	TextColorGen1 = "black";
}else if(CasesCovertedPercentage11OverallLRBCons>=80 && CasesCovertedPercentage11OverallLRBCons<=100){
	CasesCovertedPercentage11OverallLRBConsColor="#FBFFD6";
	TextColorGen1 = "black";
}else{
	CasesCovertedPercentage11OverallLRBConsColor="#FFD6E7";
	TextColorGen1 = "black";
}


String CasesCovertedPercentage11OverallLRBS3Color="";

if(CasesCovertedPercentage11OverallLRBS3>100){
	CasesCovertedPercentage11OverallLRBS3Color="#D6FFEE";
	TextColorGen1 = "black";
}else if(CasesCovertedPercentage11OverallLRBS3>=80 && CasesCovertedPercentage11OverallLRBS3<=100){
	CasesCovertedPercentage11OverallLRBS3Color="#FBFFD6";
	TextColorGen1 = "black";
}else{
	CasesCovertedPercentage11OverallLRBS3Color="#FFD6E7";
	TextColorGen1 = "black";
}

String CasesCovertedPercentage11OverallLRBS1Color="";

if(CasesCovertedPercentage11OverallLRBS1>100){
	CasesCovertedPercentage11OverallLRBS1Color="#D6FFEE";
	TextColorGen1 = "black";
}else if(CasesCovertedPercentage11OverallLRBS1>=80 && CasesCovertedPercentage11OverallLRBS1<=100){
	CasesCovertedPercentage11OverallLRBS1Color="#FBFFD6";
	TextColorGen1 = "black";
}else{
	CasesCovertedPercentage11OverallLRBS1Color="#FFD6E7";
	TextColorGen1 = "black";
}

String CasesCovertedPercentage11OverallLRBS2Color="";

if(CasesCovertedPercentage11OverallLRBS2>100){
	CasesCovertedPercentage11OverallLRBS2Color="#D6FFEE";
	TextColorGen1 = "black";
}else if(CasesCovertedPercentage11OverallLRBS2>=80 && CasesCovertedPercentage11OverallLRBS2<=100){
	CasesCovertedPercentage11OverallLRBS2Color="#FBFFD6";
	TextColorGen1 = "black";
}else{
	CasesCovertedPercentage11OverallLRBS2Color="#FFD6E7";
	TextColorGen1 = "black";
}


String CasesCovertedPercentage11OverallLRBS4Color="";

if(CasesCovertedPercentage11OverallLRBS4>100){
	CasesCovertedPercentage11OverallLRBS4Color="#D6FFEE";
	TextColorGen1 = "black";
}else if(CasesCovertedPercentage11OverallLRBS4>=80 && CasesCovertedPercentage11OverallLRBS4<=100){
	CasesCovertedPercentage11OverallLRBS4Color="#FBFFD6";
	TextColorGen1 = "black";
}else{
	CasesCovertedPercentage11OverallLRBS4Color="#FFD6E7";
	TextColorGen1 = "black";
}



String TextColorGen5="black";
String CasesCovertedPercentage11OverallLRBS5Color="";

if(CasesCovertedPercentage11OverallLRBS5>100){
	CasesCovertedPercentage11OverallLRBS5Color="#D6FFEE";
	TextColorGen5 = "black";
}else if(CasesCovertedPercentage11OverallLRBS5>=80 && CasesCovertedPercentage11OverallLRBS5<=100){
	CasesCovertedPercentage11OverallLRBS5Color="#FBFFD6";
	TextColorGen5 = "black";
}else{
	CasesCovertedPercentage11OverallLRBS5Color="#FFD6E7";
	TextColorGen5 = "black";
}


String TextColorGen6="black";
String CasesCovertedPercentage11OverallLRBS6Color="";

if(CasesCovertedPercentage11OverallLRBS6>100){
	CasesCovertedPercentage11OverallLRBS6Color="#D6FFEE";
	TextColorGen6 = "black";
}else if(CasesCovertedPercentage11OverallLRBS6>=80 && CasesCovertedPercentage11OverallLRBS6<=100){
	CasesCovertedPercentage11OverallLRBS6Color="#FBFFD6";
	TextColorGen6 = "black";
}else{
	CasesCovertedPercentage11OverallLRBS6Color="#FFD6E7";
	TextColorGen6 = "black";
}



html +="<tr>";
html +="<td  valign='top'  style='background-color: #c8cfe6; height: 18px; border:1px solid #dee0e1;t1ext-align:center; font-weight: normal;'>Overall (%)</td>";
if(CasesCovertedPercentage11OverallLRBCons!=0){
	html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11OverallLRBConsColor+"; color:"+TextColorGen1+"; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11OverallLRBCons)+"%</td>";
}else{
	html +="<td style='text-align: center; background-color:#d9dce7; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
}


if(CasesCovertedPercentage11OverallLRBS3!=0){
	html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11OverallLRBS3Color+"; color:"+TextColorGen1+"; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11OverallLRBS3)+"%</td>";
}else{
	html +="<td style='text-align: center; background-color:#d9dce7; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
}

if(CasesCovertedPercentage11OverallLRBS1!=0){
	html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11OverallLRBS1Color+"; color:"+TextColorGen1+"; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11OverallLRBS1)+"%</td>";
}else{
	html +="<td style='text-align: center; background-color:#d9dce7; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
}

if(CasesCovertedPercentage11OverallLRBS2!=0){
	html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11OverallLRBS2Color+"; color:"+TextColorGen1+"; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11OverallLRBS2)+"%</td>";
}else{
	html +="<td style='text-align: center; background-color:#d9dce7; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
}

/*

if(CasesCovertedPercentage11OverallLRBS4!=0){
	html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11OverallLRBS4Color+"; color:"+TextColorGen1+"; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11OverallLRBS4)+"%</td>";
}else{
	html +="<td style='text-align: center; background-color:#d9dce7; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
}
*/

if(CasesCovertedPercentage11OverallLRBS6!=0){
	html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11OverallLRBS6Color+"; color:"+TextColorGen6+"; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11OverallLRBS6)+"%</td>";
}else{
	html +="<td style='text-align: center; background-color:#d9dce7; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
}

if(CasesCovertedPercentage11OverallLRBS5!=0){
	html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11OverallLRBS5Color+"; color:"+TextColorGen5+"; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11OverallLRBS5)+"%</td>";
}else{
	html +="<td style='text-align: center; background-color:#d9dce7; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
}






html +="</tr></table><br/>";


	
	//////////////////////////////////////////
			
			
		

		
		// End Top Summary
		}
		
		html +="<table border='0' style='border-collapse: collapse; border:1px solid #dee0e1;width:900px;font-size: 14px;'>";
		html +="<thead>";
						    
		html +="<tr style='font-size:12px;'>";
		html +="<td style='background-color: #3d5ab3; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;color:white;'>&nbsp;</th>";
		html +="<td style='background-color: #3d5ab3; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;color:white;' colspan='3'>CSD</th>";
		html +="<td style='background-color: #3d5ab3; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;color:white;' colspan='3'>NCB</th>";
		html +="<td style='background-color: #3d5ab3; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;color:white;' colspan='3'>LRB</th>";
		html +="</tr>";		
		
		
		html +="<tr style='font-size:12px;'>";
		html +="<td style='background-color: #c8cfe6; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;'>&nbsp;</th>";
		html +="<td style='background-color: #c8cfe6; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;'>Target</th>";
		html +="<td style='background-color: #c8cfe6; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;'>Attainment</th>";
		html +="<td style='background-color: #c8cfe6; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;'>%</th>";
		
		html +="<td style='background-color: #c8cfe6; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;'>Target</th>";
		html +="<td style='background-color: #c8cfe6; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;'>Attainment</th>";
		html +="<td style='background-color: #c8cfe6; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;'>%</th>";
		
		html +="<td style='background-color: #c8cfe6; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;'>Target</th>";
		html +="<td style='background-color: #c8cfe6; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;'>Attainment</th>";
		html +="<td style='background-color: #c8cfe6; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;'>%</th>";
								
		
		
		
		
							
		html +="</tr>";			
						
		//// Top Layer 
		
		
		
		
			html +="<tr>";
			html +="<td style='background-color: #b7bed4; c1olor: white; height: 18px; text-align: left; font-weight: normal;border:1px solid #c2c3c3;'>Consolidated</td>";	
						
						
			String Color3="black";
						
						ResultSet rs315218 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-2 and type_id=-2 and snd_id in (SELECT snd_id FROM pep.common_sd_groups where snd_id is not null)"); //-2 for CSD
						if(rs315218.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs315218.getDouble("sum_converted");
							double TargetCoverted1 = rs315218.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = ((SalesCoverted1 * 1d) / (TargetCoverted1 * 1d) )*100;
							}
					
							
						
							if(TargetCoverted1!=0){
								html +="<td style='text-align: center; background-color: #b7bed4; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color: #b7bed4; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center; background-color: #b7bed4; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color: #b7bed4; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							
							
							
							
							String CasesCovertedPercentage11Color="";

							if(CasesCovertedPercentage11>100){
								CasesCovertedPercentage11Color="#D6FFEE";
								Color3="black";
							}else if(CasesCovertedPercentage11>=80 && CasesCovertedPercentage11<=100){
								CasesCovertedPercentage11Color="#FBFFD6";
								Color3="black";
							}else{
								CasesCovertedPercentage11Color="#FFD6E7";
								Color3="black";
							}
							
							
							
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center; background-color: "+CasesCovertedPercentage11Color+"; color:"+Color3+"; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center; background-color: #b7bed4; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							
							
							
						}
						
						
						ResultSet rs315313 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-3 and type_id=-3 and snd_id in (SELECT snd_id FROM pep.common_sd_groups where snd_id is not null)"); //-3 for NCB
						if(rs315313.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs315313.getDouble("sum_converted");
							double TargetCoverted1 = rs315313.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = ((SalesCoverted1 * 1d) / (TargetCoverted1 * 1d) )*100;
							}
					
						
							if(TargetCoverted1!=0){
								html +="<td style='text-align: center; background-color:#b7bed4; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#b7bed4; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center; background-color:#b7bed4; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#b7bed4; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							
							
							String CasesCovertedPercentage11NCBColor="";

							if(CasesCovertedPercentage11>100){
								CasesCovertedPercentage11NCBColor="#D6FFEE";
								Color3="black";
							}else if(CasesCovertedPercentage11>=80 && CasesCovertedPercentage11<=100){
								CasesCovertedPercentage11NCBColor="#FBFFD6";
								Color3="black";
							}else{
								CasesCovertedPercentage11NCBColor="#FFD6E7";
								Color3="black";
							}
							
							
							
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11NCBColor+"; color:"+Color3+"; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center; background-color:#b7bed4; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
						}
				
////LRB
						
						ResultSet rs315312 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and snd_id in (SELECT snd_id FROM pep.common_sd_groups where snd_id is not null)"); //-1 for all
						if(rs315312.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs315312.getDouble("sum_converted");
							double TargetCoverted1 = rs315312.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = ((SalesCoverted1 * 1d) / (TargetCoverted1 * 1d) )*100;
							}
					
							if(TargetCoverted1!=0){
								html +="<td style='text-align: center; background-color:#b7bed4; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#b7bed4; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center; background-color:#b7bed4; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#b7bed4; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							
							
							String CasesCovertedPercentage11LRBColor="";

							if(CasesCovertedPercentage11>100){
								CasesCovertedPercentage11LRBColor="#D6FFEE";
								Color3="black";
							}else if(CasesCovertedPercentage11>=80 && CasesCovertedPercentage11<=100){
								CasesCovertedPercentage11LRBColor="#FBFFD6";
								Color3="black";
							}else{
								CasesCovertedPercentage11LRBColor="#FFD6E7";
								Color3="black";
							}
							
							GraphOverallSNDPercentage = CasesCovertedPercentage11;
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11LRBColor+"; color:"+Color3+"; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center; background-color:#b7bed4; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
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
		
		
		String Color4="black";
		
		ResultSet rs11321 = s16.executeQuery("SELECT distinct snd_id,(select display_name from users u where u.id=snd_id) snd_name FROM dist_targets_report_temp where snd_id is not null");
		while(rs11321.next()){
			SNDID11 = rs11321.getLong("snd_id");
			SNDName1= rs11321.getString("snd_name");
			
			SNDDisplay1 = SNDName1;
			if (SNDID11 == 0){
				SNDDisplay1 = "Unassigned";
			}
			
			GraphSNDName.add(SNDDisplay1);
			
			
			if (!isRSM){
			html +="<tr>";
			html +="<td style='background-color: #d3d8ea; c1olor: white; height: 18px; t1ext-align: center; font-weight: normal;border:1px solid #c2c3c3;'>"+Utilities.truncateStringToMax(SNDDisplay1,22)+"</td>";	
						
						
						
						
						ResultSet rs3152 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-2 and type_id=-2 and snd_id="+SNDID11); //-2 for CSD
						if(rs3152.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs3152.getDouble("sum_converted");
							double TargetCoverted1 = rs3152.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = ((SalesCoverted1 * 1d) / (TargetCoverted1 * 1d) )*100;
							}
					
							
						
							if(TargetCoverted1!=0){
								html +="<td style='text-align: center; background-color: #d3d8ea; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color: #d3d8ea; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center; background-color: #d3d8ea; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color: #d3d8ea; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							
							
							String CasesCovertedPercentage11NormalColor="";

							if(CasesCovertedPercentage11>100){
								CasesCovertedPercentage11NormalColor="#D6FFEE";
								Color4="black";
							}else if(CasesCovertedPercentage11>=80 && CasesCovertedPercentage11<=100){
								CasesCovertedPercentage11NormalColor="#FBFFD6";
								Color4="black";
							}else{
								CasesCovertedPercentage11NormalColor="#FFD6E7";
								Color4="black";
							}
							
							
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center; background-color: "+CasesCovertedPercentage11NormalColor+"; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center; background-color: #d3d8ea; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							
							
							
						}
						
						
						ResultSet rs3153 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-3 and type_id=-3 and snd_id="+SNDID11); //-3 for NCB
						if(rs3153.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs3153.getDouble("sum_converted");
							double TargetCoverted1 = rs3153.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = ((SalesCoverted1 * 1d) / (TargetCoverted1 * 1d) )*100;
							}
					
						
							if(TargetCoverted1!=0){
								html +="<td style='text-align: center; background-color:#d3d8ea; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d3d8ea; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center; background-color:#d3d8ea; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d3d8ea; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							
							
							String CasesCovertedPercentage11NormalNCBColor="";

							if(CasesCovertedPercentage11>100){
								CasesCovertedPercentage11NormalNCBColor="#D6FFEE";
								Color4="black";
							}else if(CasesCovertedPercentage11>=80 && CasesCovertedPercentage11<=100){
								CasesCovertedPercentage11NormalNCBColor="#FBFFD6";
								Color4="black";
							}else{
								CasesCovertedPercentage11NormalNCBColor="#FFD6E7";
								Color4="black";
							}
							
							
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11NormalNCBColor+"; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d3d8ea; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
						}
				
////LRB
						
						ResultSet rs31531 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and snd_id="+SNDID11); //-1 for all
						if(rs31531.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs31531.getDouble("sum_converted");
							double TargetCoverted1 = rs31531.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = ((SalesCoverted1 * 1d) / (TargetCoverted1 * 1d) )*100;
							}
					
							if(TargetCoverted1!=0){
								html +="<td style='text-align: center; background-color:#d3d8ea; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d3d8ea; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center; background-color:#d3d8ea; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d3d8ea; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							
							
							String CasesCovertedPercentage11NormalLRBColor="";

							if(CasesCovertedPercentage11>100){
								CasesCovertedPercentage11NormalLRBColor="#D6FFEE";
								Color4="black";
							}else if(CasesCovertedPercentage11>=80 && CasesCovertedPercentage11<=100){
								CasesCovertedPercentage11NormalLRBColor="#FBFFD6";
								Color4="black";
							}else{
								CasesCovertedPercentage11NormalLRBColor="#FFD6E7";
								Color4="black";
							}
							
							
							GraphOverallPercentage = CasesCovertedPercentage11;
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center; background-color:"+CasesCovertedPercentage11NormalLRBColor+"; color:"+Color4+"; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d3d8ea; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
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
			
			RSMDisplay1 = RSMName1;
			if (RSMID11 == 0){
				RSMDisplay1 = "Unassigned";
			}
			
			
			GraphRSMName.add(RSMDisplay1);
			
			
			html +="<tr>";
			html +="<td style='background-color: #d7e0fd; c1olor: white; height: 18px; t1ext-align: center; font-weight: normal;border:1px solid #c2c3c3;'>"+Utilities.truncateStringToMax(RSMDisplay1,22)+"</td>";	
						
						
					
				
						double RSMConvetedCasesPercentage=0;
						ResultSet rs31521 = s14.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-2 and type_id=-2 and rsm_id="+RSMID11+" and snd_id="+SNDID11); //-2 for CSD
						if(rs31521.first()){
							
							double SalesCoverted1 = rs31521.getDouble("sum_converted");
							double TargetCoverted1 = rs31521.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								RSMConvetedCasesPercentage = ((SalesCoverted1 * 1d) / (TargetCoverted1 * 1d) )*100;
							}
							
						if(TargetCoverted1!=0){
							html +="<td style='text-align: center; background-color: #d7e0fd; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
						}else{
							html +="<td style='text-align: center; background-color: #d7e0fd; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
						}
						
						if(SalesCoverted1!=0){
							html +="<td style='text-align: center; background-color: #d7e0fd; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
						}else{
							html +="<td style='text-align: center; background-color: #d7e0fd; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
						}
						
						
						String RSMConvetedCasesPercentageColor="";

						if(RSMConvetedCasesPercentage>100){
							RSMConvetedCasesPercentageColor="#D6FFEE";
						}else if(RSMConvetedCasesPercentage>=80 && RSMConvetedCasesPercentage<=100){
							RSMConvetedCasesPercentageColor="#FBFFD6";
						}else{
							RSMConvetedCasesPercentageColor="#FFD6E7";
						}
						
						
						if(RSMConvetedCasesPercentage!=0){
							html +="<td style='text-align: center; background-color: "+RSMConvetedCasesPercentageColor+"; color:"+Color4+"; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(RSMConvetedCasesPercentage)+"%</td>";
						}else{
							html +="<td style='text-align: center; background-color: #d7e0fd; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
						}
							
							
				
						}
						
						//GraphRSMPercentage.add(RSMConvetedCasesPercentage);
						
						
						ResultSet rs31521i = s14.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-3 and type_id=-3 and rsm_id="+RSMID11+" and snd_id="+SNDID11); //-3 for NCB
						if(rs31521i.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs31521i.getDouble("sum_converted");
							double TargetCoverted1 = rs31521i.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = ((SalesCoverted1 * 1d) / (TargetCoverted1 * 1d) )*100;
							}
							
						
						
							if(TargetCoverted1!=0){
								html +="<td style='text-align: center; background-color:#d7e0fd; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d7e0fd; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center; background-color:#d7e0fd; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d7e0fd; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							
							String RSMConvetedCasesPercentageNCBColor="";

							if(CasesCovertedPercentage11>100){
								RSMConvetedCasesPercentageNCBColor="#D6FFEE";
							}else if(CasesCovertedPercentage11>=80 && CasesCovertedPercentage11<=100){
								RSMConvetedCasesPercentageNCBColor="#FBFFD6";
							}else{
								RSMConvetedCasesPercentageNCBColor="#FFD6E7";
							}
							
							
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center; background-color:"+RSMConvetedCasesPercentageNCBColor+"; color:"+Color4+"; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d7e0fd; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
						}
				
/////LRB
						
						
						ResultSet rs31521i1 = s14.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and rsm_id="+RSMID11+" and snd_id="+SNDID11); //
						if(rs31521i1.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs31521i1.getDouble("sum_converted");
							double TargetCoverted1 = rs31521i1.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = ((SalesCoverted1 * 1d) / (TargetCoverted1 * 1d) )*100;
							}
							
						
							if(TargetCoverted1!=0){
								html +="<td style='text-align: center; background-color:#d7e0fd; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d7e0fd; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center; background-color:#d7e0fd; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d7e0fd; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							
							
							String RSMConvetedCasesPercentageLRBColor="";

							if(CasesCovertedPercentage11>100){
								RSMConvetedCasesPercentageLRBColor="#D6FFEE";
							}else if(CasesCovertedPercentage11>=80 && CasesCovertedPercentage11<=100){
								RSMConvetedCasesPercentageLRBColor="#FBFFD6";
							}else{
								RSMConvetedCasesPercentageLRBColor="#FFD6E7";
							}
							
							//GraphOverallPercentage = CasesCovertedPercentage11;
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center; background-color:"+RSMConvetedCasesPercentageLRBColor+"; color:"+Color4+"; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center; background-color:#d7e0fd; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
							}
							GraphRSMPercentage.add(CasesCovertedPercentage11);
							
						}
						
						/////
				
				html +="</tr>";
				
				int MonthNumber=0;
				int YearNumber=0;
				Date DisplayStartDate=new Date();
				Date DisplayEndDate=new Date();
				
				ResultSet rs99 = s20.executeQuery("select month,year,start_date,end_date from distributor_targets where distributor_id in (SELECT distributor_id FROM dist_targets_report_temp where rsm_id in("+RSMID11+")) and "+Utilities.getSQLDate(CurrentDate)+" between start_date and end_date and type_id = 2 limit 1");
				if(rs99.first()){
					 MonthNumber = rs99.getInt("month");
					 YearNumber = rs99.getInt("year");
					 DisplayStartDate = rs99.getDate("start_date");
					 DisplayEndDate = rs99.getDate("end_date");
				}
				
		
		long TDMID11 = 0;
		String TDMName1 = "";
		
		String Color6="black";
		
		ResultSet rs1131 = s10.executeQuery("SELECT distinct tdm_id,(select display_name from users u where u.id=tdm_id) tdm_name FROM dist_targets_report_temp where rsm_id="+RSMID11+" and snd_id="+SNDID11);
		while(rs1131.next()){
			TDMID11 = rs1131.getLong("tdm_id");
			TDMName1 = rs1131.getString("tdm_name");
			
			
			String TDMDisplay1 = TDMName1;
			if (TDMID11 == 0){
				TDMDisplay1 = "Unassigned";
			}
			
			html +="<tr>";
			html +="<td style='b1ackground-color: #D5DBED; c1olor: white; height: 18px; t1ext-align: center; font-weight: normal;border:1px solid #c2c3c3;'>"+Utilities.truncateStringToMax(TDMDisplay1,22)+"</td>";	
						
						
					
						
						
						
						
						ResultSet rs3151i = s8.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-2 and type_id=-2 and tdm_id="+TDMID11+" and rsm_id="+RSMID11+" and snd_id="+SNDID11); //-2 for CSD
						if(rs3151i.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs3151i.getDouble("sum_converted");
							double TargetCoverted1 = rs3151i.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = ((SalesCoverted1 * 1d) / (TargetCoverted1 * 1d) )*100;
							}
						
						
						
							if(TargetCoverted1!=0){
								html +="<td style='text-align: center; b1ackground-color: #D5DBED; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; b1ackground-color: #D5DBED; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center;b1ackground-color: #D5DBED; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; b1ackground-color: #D5DBED; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
							}
							
							
							String TDMCasesCovertedPercentage11Color="";

							if(CasesCovertedPercentage11>100){
								TDMCasesCovertedPercentage11Color="#D6FFEE";
								Color6="black";
							}else if(CasesCovertedPercentage11>=80 && CasesCovertedPercentage11<=100){
								TDMCasesCovertedPercentage11Color="#FBFFD6";
								Color6="black";
							}else{
								TDMCasesCovertedPercentage11Color="#FFD6E7";
								Color6="black";
							}
							
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center; background-color: "+TDMCasesCovertedPercentage11Color+"; color:"+Color6+"; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center; b1ackground-color: #D5DBED; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
							}

							
							
							
				
						}
						
						
						ResultSet rs3151i1 = s8.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-3 and type_id=-3 and tdm_id="+TDMID11+" and rsm_id="+RSMID11+" and snd_id="+SNDID11);  //-3 for NCB
						if(rs3151i1.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs3151i1.getDouble("sum_converted");
							double TargetCoverted1 = rs3151i1.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = ((SalesCoverted1 * 1d) / (TargetCoverted1 * 1d) )*100;
							}
						
						
						

							if(TargetCoverted1!=0){
								html +="<td style='text-align: center; b1ackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; b1ackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center; b1ackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; b1ackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
							}
							
							
							String TDMCasesCovertedPercentage11NCBColor="";

							if(CasesCovertedPercentage11>100){
								TDMCasesCovertedPercentage11NCBColor="#D6FFEE";
								Color6="black";
							}else if(CasesCovertedPercentage11>=80 && CasesCovertedPercentage11<=100){
								TDMCasesCovertedPercentage11NCBColor="#FBFFD6";
								Color6="black";
							}else{
								TDMCasesCovertedPercentage11NCBColor="#FFD6E7";
								Color6="black";
							}
							
							
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center; background-color:"+TDMCasesCovertedPercentage11NCBColor+"; color:"+Color6+"; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center; b1ackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
							}
						}
				
/////LRB
						
						ResultSet rs3151i11 = s8.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and tdm_id="+TDMID11+" and rsm_id="+RSMID11+" and snd_id="+SNDID11);  //-3 for NCB
						if(rs3151i11.first()){
							double CasesCovertedPercentage11=0;
							double SalesCoverted1 = rs3151i11.getDouble("sum_converted");
							double TargetCoverted1 = rs3151i11.getDouble("sum_targeted");
							
							if(TargetCoverted1!=0){
								CasesCovertedPercentage11 = ((SalesCoverted1 * 1d) / (TargetCoverted1 * 1d) )*100;
							}
						
							if(TargetCoverted1!=0){
								html +="<td style='text-align: center; b1ackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(TargetCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; b1ackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
							}
							
							if(SalesCoverted1!=0){
								html +="<td style='text-align: center; b1ackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(SalesCoverted1)+"</td>";
							}else{
								html +="<td style='text-align: center; b1ackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'></td>";
							}
							
							String TDMCasesCovertedPercentage11LRBColor="";

							if(CasesCovertedPercentage11>100){
								TDMCasesCovertedPercentage11LRBColor="#D6FFEE";
								Color6="black";
							}else if(CasesCovertedPercentage11>=80 && CasesCovertedPercentage11<=100){
								TDMCasesCovertedPercentage11LRBColor="#FBFFD6";
								Color6="black";
							}else{
								TDMCasesCovertedPercentage11LRBColor="#FFD6E7";
								Color6="black";
							}
							
							
							if(CasesCovertedPercentage11!=0){
								html +="<td style='text-align: center; background-color:"+TDMCasesCovertedPercentage11LRBColor+"; color:"+Color6+"; border:1px solid #c2c3c3;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage11)+"%</td>";
							}else{
								html +="<td style='text-align: center; b1ackground-color:#C2C2C2; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
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
			HTMLGraphs +="<td width='100%' style='text-align: left'><img src='https://chart.googleapis.com/chart?chs=220x135&cht=gom&chd=t:"+Utilities.getDisplayCurrencyFormatRounded(GraphOverallPercentage)+"&chdl=Target%20Attainment&chdlp=b&chl="+Utilities.getDisplayCurrencyFormatRounded(GraphOverallPercentage)+"%&chls=5|15'><img src='https://chart.googleapis.com/chart?cht=bhs&chs=410x135&chd=t:"+RSMValues+"|"+RSMValues2+"&chco=4d89f9,c6d9fd&chbh=20&chxt=x,y&chxl=1:"+RSMNames+"&chm=N,000000,0,-1,11&chtt=Target Attainment%20(%)'></td></tr>";
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
			HTMLGraphs +="<td width='100%' style='text-align: left'><img src='https://chart.googleapis.com/chart?chs=220x135&cht=gom&chd=t:"+Utilities.getDisplayCurrencyFormatRounded(GraphOverallSNDPercentage)+"&chdl=Target%20Attainment&chdlp=b&chl="+Utilities.getDisplayCurrencyFormatRounded(GraphOverallSNDPercentage)+"%&chls=5|15'><img src='https://chart.googleapis.com/chart?cht=bhs&chs=410x140&chd=t:"+SNDValues+"|"+SNDValues2+"&chco=4d89f9,c6d9fd&chbh=20&chxt=x,y&chxl=1:"+SNDNames+"&chm=N,000000,0,-1,11&chtt=Target Attainment%20(%)'></td></tr>";
			HTMLGraphs +="</table>";
			html = html.replaceAll("<span style='display: none' id='HGraphs'></span>", HTMLGraphs);

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
	html +="<tr><td style='text-align:center'><i>Planned</i></td><td style='text-align:center'><i>Actual</i></td></tr>";
	html +="</table>";
	
	html +="<br><br/>";		
	}
		
		
		//////////////////////////////////////////////////////////////////////////// - Actual Email - /////////////////////////////////////////
		
		
		html +="<table style='width: 900px; margin-top:-8px; ' cellpadding='2' cellspacing='1'>";
		html +="<tr>";
			
		html +="<td style='width: 100%' valign='top'>";
		html +="<table border='0' style='border-collapse: collapse; border:1px solid #dee0e1;width:800px;font-size: 14px;'>";
		html +="<thead>";
						    
		html +="<tr style='font-size:12px;'>";
		html +="<th data-priority='1'  style='background-color: #3d5ab3; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;color:white;' colspan='2'>&nbsp;</th>";

								
		int TotalPackages=0;
		
		ResultSet rs101 = s.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
		while(rs101.next()){
			TotalPackages+=rs101.getInt("packge_count");
			html +="<td style='background-color: #3d5ab3; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;color:white;' colspan="+rs101.getInt("packge_count")+">"+rs101.getString("type_name") +"</th>";
			
		}
		
		//System.out.println(TotalPackages);
							
		html +="</tr>";			
						
		html +="<tr style='font-size:13px;'>";
		html +="<th data-priority='1'  style='text-align:center; background-color:#E0E0E0; min-width:100px;border-right:1px solid #fff'>&nbsp;</th>";
		html +="<th style='text-align:center; background-color:#E0E0E0;border-right:1px solid #fff'>Converted</th>";
		
		
		
		//System.out.println("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  product_id in (SELECT distinct idnp.product_id FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id where idn.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+") "+WherePackage+ "order by package_sort_order");
		
		ResultSet rs111 = s.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
		while(rs111.next()){
		ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) "+WherePackage+ " and lrb_type_id="+rs111.getInt("lrb_type_id")+" order by package_sort_order");
		
		
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
		
			html +="<th data-priority='1'  style='text-align:center; background-color:#E0E0E0;border-right:1px solid #fff'>"+PackageLabel+"</th>";
		
			
		
		
		}
		
		
		 
		
	}			
		html +="</tr>";
		
		
		/////////////// Upper Layer
		
		
		
	
			html +="<tr>";
			html +="<td style='background-color:#b7bed4; font-weight: bold; font-size: 13px; border-right:1px solid #fff;'>Consolidated</td>";	
						
						
						ResultSet rs31524 = s17.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and snd_id in (SELECT snd_id FROM pep.common_sd_groups where snd_id is not null)");
						if(rs31524.first()){
							double CasesCovertedPercentage1=0;
							double SalesCoverted = rs31524.getDouble("sum_converted");
							double TargetCoverted = rs31524.getDouble("sum_targeted");
							
							if(TargetCoverted!=0){
								CasesCovertedPercentage1 = ((SalesCoverted * 1d) / (TargetCoverted * 1d) )*100;
							}
						
						
						if(CasesCovertedPercentage1!=0){
							html +="<td style='background-color:#b7bed4; text-align:center; font-weight: bold; font-size: 13px; border-right:1px solid #fff;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage1)+"%</td>";
						}else{
							html +="<td style='background-color:#b7bed4; text-align:center; font-weight: bold; font-size: 13px; border-right:1px solid #fff;'></td>";
						}
							
							
				
						}
				ResultSet rs12325 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
				while(rs12325.next()){
					
					int TypeID3 = rs12325.getInt("lrb_type_id");
					ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) "+WherePackage+ " and lrb_type_id="+rs12325.getInt("lrb_type_id")+" order by package_sort_order");
					
					while(rs.next()){
					int unit_per_sku=0;
					PackageID=rs.getInt("package_id");
					//System.out.println("select sum(sales) sum_sales,sum(target) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and tdm_id="+TDMID1);
					
					ResultSet rs315 = s18.executeQuery("select sum(sales) sum_sales,sum(target_mtd) sum_target from dist_targets_report_temp where package_id="+PackageID+" and type_id="+TypeID3+" and snd_id in (SELECT snd_id FROM pep.common_sd_groups where snd_id is not null)");
					if(rs315.first()){
						double CasesPercentage1=0;
						double Sales = rs315.getDouble("sum_sales");
						double Target = rs315.getDouble("sum_target");
						
						if(Target!=0){	
							CasesPercentage1 = ((Sales * 1d) / (Target * 1d) )*100;
						}
						
						
					
					if(CasesPercentage1!=0){
						html +="<td style='text-align: center; background-color:#b7bed4; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesPercentage1)+"%</td>";
					}else{
						html +="<td style='text-align: center; background-color:#b7bed4; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
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
		
		
		ResultSet rs1132 = s16.executeQuery("SELECT distinct snd_id,(select display_name from users u where u.id=snd_id) snd_name FROM dist_targets_report_temp where snd_id is not null");
		while(rs1132.next()){
			SNDID1 = rs1132.getLong("snd_id");
			SNDName = rs1132.getString("snd_name");
			
			SNDDisplay = SNDName;
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
								CasesCovertedPercentage1 = ((SalesCoverted * 1d) / (TargetCoverted * 1d) )*100;
							}
						
						
						if(CasesCovertedPercentage1!=0){
							html +="<td style='text-align:center; background-color:#d3d8ea; font-weight: bold; font-size: 13px; border-right:1px solid #fff;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage1)+"%</td>";
						}else{
							html +="<td style='text-align:center; background-color:#d3d8ea; font-weight: bold; font-size: 13px; border-right:1px solid #fff;'></td>";
						}
							
							
				
						}
				ResultSet rs1232 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
				while(rs1232.next()){
					
					int TypeID3 = rs1232.getInt("lrb_type_id");
					ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) "+WherePackage+ " and lrb_type_id="+rs1232.getInt("lrb_type_id")+" order by package_sort_order");
					
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
							CasesPercentage1 = ((Sales * 1d) / (Target * 1d) )*100;
						}
						
						
					
					if(CasesPercentage1!=0){
						html +="<td style='text-align: center; background-color:#d3d8ea; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesPercentage1)+"%</td>";
					}else{
						html +="<td style='text-align: center; background-color:#d3d8ea; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
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
			
			RSMDisplay = RSMName;
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
								CasesCovertedPercentage1 = ((SalesCoverted * 1d) / (TargetCoverted * 1d) )*100;
							}
							
						
						if(CasesCovertedPercentage1!=0){
							html +="<td style='text-align:center; background-color:#d7e0fd;font-weight: bold; border-right:1px solid #fff;font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage1)+"%</td>";
						}else{
							html +="<td style='text-align:center; background-color:#d7e0fd;font-weight: bold; border-right:1px solid #fff;font-size: 13px;'></td>";
						}
						
							
				
						}
				ResultSet rs1231 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
				while(rs1231.next()){
					
					int TypeID3 = rs1231.getInt("lrb_type_id");
					ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in ("+SAPCodes+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) "+WherePackage+ " and lrb_type_id="+rs1231.getInt("lrb_type_id")+" order by package_sort_order");
					
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
							CasesPercentage1 = ((Sales * 1d) / (Target * 1d) )*100;
						}
						
						
					
					if(CasesPercentage1!=0){
						html +="<td style='text-align: center; background-color:#d7e0fd; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesPercentage1)+"%</td>";
					}else{
						html +="<td style='text-align: center; background-color:#d7e0fd; border:1px solid #c2c3c3;font-weight: bold; font-size: 13px;'></td>";
					}
						
						
					
					}
					}
				}
				
				
				
				html +="</tr>";
				
		
				int MonthNumber=0;
				int YearNumber=0;
				Date DisplayStartDate=new Date();
				Date DisplayEndDate=new Date();
				ResultSet rs99 = s20.executeQuery("select month,year,start_date,end_date from distributor_targets where distributor_id in (SELECT distributor_id FROM dist_targets_report_temp where rsm_id in("+RSMID1+")) and "+Utilities.getSQLDate(CurrentDate)+" between start_date and end_date and type_id = 2 limit 1");
				if(rs99.first()){
					 MonthNumber = rs99.getInt("month");
					 YearNumber = rs99.getInt("year");
					 DisplayStartDate = rs99.getDate("start_date");
					 DisplayEndDate = rs99.getDate("end_date");
				}
				
				
		
		
		long TDMID1 = 0;
		String TDMName = "";
		
		
		
		ResultSet rs113 = s10.executeQuery("SELECT distinct tdm_id,(select display_name from users u where u.id=tdm_id) tdm_name FROM dist_targets_report_temp where rsm_id="+RSMID1+" and snd_id="+SNDID1);
		while(rs113.next()){
			TDMID1 = rs113.getLong("tdm_id");
			TDMName = rs113.getString("tdm_name");
			
			
			String TDMDisplay = TDMName;
			if (TDMID1 == 0){
				TDMDisplay = "Unassigned";
			}
			
			html +="<tr>";
			html +="<td style='font-weight: normal; background-color:#fff;border-right:1px solid #fff;font-size: 13px;'>"+Utilities.truncateStringToMax(TDMDisplay,22)+"</td>";	
						
						
						ResultSet rs315 = s8.executeQuery("select sum(sales) sum_converted,sum(target_mtd) sum_targeted from dist_targets_report_temp where package_id=-1 and type_id=-1 and tdm_id="+TDMID1+" and rsm_id="+RSMID1+" and snd_id="+SNDID1);
						if(rs315.first()){
							double CasesCovertedPercentage1=0;
							double SalesCoverted = rs315.getDouble("sum_converted");
							double TargetCoverted = rs315.getDouble("sum_targeted");
							
							if(TargetCoverted!=0){
								CasesCovertedPercentage1 = ((SalesCoverted * 1d) / (TargetCoverted * 1d) )*100;
							}
						
						
						if(CasesCovertedPercentage1!=0){
							html +="<td style='text-align:center; font-weight: normal; background-color:#fff;border-right:1px solid #fff;font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesCovertedPercentage1)+"%</td>";
						}else{
							html +="<td style='text-align:center; font-weight: normal; background-color:#fff;border-right:1px solid #fff;font-size: 13px;'></td>";
						}
							
				
						}
				ResultSet rs123 = s5.executeQuery("SELECT lrb_type_id,(select label FROM inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name,count(distinct package_id) packge_count FROM inventory_products_view where sap_code in (SELECT distinct sap_code from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDateLifting(MinTargetDate)+" and "+Utilities.getSQLDateNextLifting(MaxTargetDate)+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) "+WherePackage+ " and lrb_type_id is not null group by lrb_type_id order by lrb_type_id");
				while(rs123.next()){
					
					int TypeID3 = rs123.getInt("lrb_type_id");
					ResultSet rs = s6.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  sap_code in (SELECT distinct sap_code from dist_targets_temp dtt where dtt.order_date between "+Utilities.getSQLDateLifting(MinTargetDate)+" and "+Utilities.getSQLDateNextLifting(MaxTargetDate)+") and package_id in (SELECT distinct package_id from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.type_id = 2) "+WherePackage+ " and lrb_type_id="+rs123.getInt("lrb_type_id")+" order by package_sort_order");
					
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
							CasesPercentage1 = ((Sales * 1d) / (Target * 1d) )*100;
						}
						
						
					if(CasesPercentage1!=0){
						html +="<td style='text-align: center; background-color:#FCFCFC; border-right:1px solid #fff;font-weight: normal; font-size: 13px;'>"+Utilities.getDisplayCurrencyFormatRounded(CasesPercentage1)+"%</td>";
					}else{
						html +="<td style='text-align: center; background-color:#FCFCFC; border-right:1px solid #fff;font-weight: bold; font-size: 13px;'></td>";
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
	
	
	String WhereLiftingWithoutTargets = " and idn.distributor_id in (select distributor_id from common_distributors where snd_id in (SELECT snd_id FROM pep.common_sd_groups where snd_id is not null)) ";
	if (EmployeeID != -1){
		WhereLiftingWithoutTargets = " and idn.distributor_id in (select distributor_id from common_distributors where (snd_id = "+EmployeeID+" or rsm_id = "+EmployeeID+" or tdm_id = "+EmployeeID+")) ";
	}
	html += "<br><br>";	
	
	
	
	html +="<p style='text-align:left; font-weight:normal; padding-left:5px; color: red;'>Lifting without Target</p>";
	
	html +="<table border=0 style='font-size:13px; font-weight: 400; width:900px' cellpadding='2' cellspacing='1'   class='GridWithBorder'>";
	html +="<thead>";
					    
	html +="<tr style='font-size:12px;'>";
	html +="<th data-priority='1'  style='text-align:center; background-color:#C2C2C2; color:  white;width:50%;border-right:1px solid #fff; min-height: 20px'>Distributor</th>";
	html +="<th style='text-align:center; background-color:#C2C2C2;border-right:1px solid #fff; color:white;width:50%;'>Converted Cases</th>";
	
	html +="</tr>";
	
	
	 ResultSet rs = s3.executeQuery("select idn.sap_order_no, idn.distributor_id, (select cd.name from common_distributors cd where cd.distributor_id=idn.distributor_id) distributor_name,idn.created_on, ipv.sap_code, sum((idnp.total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml) total_units, ipv.unit_per_sku from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where idn.created_on between "+Utilities.getSQLDateLifting(MonthStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthEndDate)+" and ipv.category_id = 1 and idn.distributor_id not in (SELECT distributor_id  FROM distributor_targets where month("+Utilities.getSQLDate(CurrentDate)+")=month and year("+Utilities.getSQLDate(CurrentDate)+")=year and type_id=2) "+WhereLiftingWithoutTargets+" group by idn.distributor_id");
	    while(rs.next()){
	    	
	    						    	
	    	double units = rs.getDouble("total_units");
	    	
	    							    	
	    	html+="<tr>";
	    	html+="<td>"+rs.getLong("distributor_id")+" - "+rs.getString("distributor_name")+"</td>";   	
	    	html+="<td style='text-align: right;'>"+Utilities.getDisplayCurrencyFormatRounded(units)+"</td>";	    		
	    	html+="</tr>";
	    
	    }
	
	
	
	
	
	html +="</thead>";
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
	
