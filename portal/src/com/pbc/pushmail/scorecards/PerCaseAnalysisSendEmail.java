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

public class PerCaseAnalysisSendEmail {
	
	public static final String filename_distributor_sd1 = "DistributorScoreCard_Daily_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_distributor_sd2 = "DistributorScoreCard_Daily_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	public static final String filename_orderbooker_sd1 = "OrderBookerScoreCard_Daily_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_orderbooker_sd2 = "OrderBookerScoreCard_Daily_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	
	public static void main(String[] args) {
		
		try {
			
			//Date StartDate2 = Utilities.parseDate("01/12/2015");
			//Date EndDate2 = Utilities.parseDate("20/12/2015");
			Date StartDate2 = Utilities.getStartDateByDate(new Date());
			Date EndDate2 = Utilities.getDateByDays(-1);
			
			
			String filename_PerCaseCostConsolidated = "NetRevenuePackage_Consolidated_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
			String filename_RevenuePerCaseConsolidated = "NetRevenueDistributor_Consolidated_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
			

			PerCaseAnalysisPackagePDF packConsolidated = new com.pbc.pushmail.scorecards.PerCaseAnalysisPackagePDF();
			packConsolidated.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_PerCaseCostConsolidated, -1,StartDate2,EndDate2);
			
			PerCaseAnalysisPDF summaryConsolidated = new com.pbc.pushmail.scorecards.PerCaseAnalysisPDF();
			summaryConsolidated.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_RevenuePerCaseConsolidated, -1,StartDate2,EndDate2);
			
			
			
			//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk","jazeb@pbc.com.pk"},null, null, "Per Case Analysis | Consolidated | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(-1, packConsolidated,summaryConsolidated,StartDate2,EndDate2), new String[]{filename_PerCaseCostConsolidated,filename_RevenuePerCaseConsolidated});
			
			Utilities.sendPBCHTMLEmail(new String[]{"imran.hashim@pbc.com.pk"},new String[]{"jazeb@pbc.com.pk","omerfk@pbc.com.pk","asim.maan@pbc.com.pk","anas.wahab@pbc.com.pk"}, new String[]{"shahrukh.salman@pbc.com.pk"}, "Per Case Analysis | Consolidated | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(-1, packConsolidated,summaryConsolidated,StartDate2,EndDate2), new String[]{filename_PerCaseCostConsolidated,filename_RevenuePerCaseConsolidated});
			
			
			String SDTitle = "";
			for(int i=1;i<=4;i++){

				SDTitle = "SD"+i;

				String filename_PerCaseCost = "NetRevenuePackage_"+SDTitle+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
				String filename_RevenuePerCase = "NetRevenueDistributor_"+SDTitle+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
				
				if(i==3){
					i++;
				}
				PerCaseAnalysisPackagePDF pack = new com.pbc.pushmail.scorecards.PerCaseAnalysisPackagePDF();
				pack.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_PerCaseCost, EmployeeHierarchy.getSDHead(i).USER_ID,StartDate2,EndDate2);
				
				PerCaseAnalysisPDF summary = new com.pbc.pushmail.scorecards.PerCaseAnalysisPDF();
				summary.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_RevenuePerCase, EmployeeHierarchy.getSDHead(i).USER_ID,StartDate2,EndDate2);
				
				Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(i).USER_EMAIL},new String[]{"jazeb@pbc.com.pk","omerfk@pbc.com.pk","asim.maan@pbc.com.pk","anas.wahab@pbc.com.pk","imran.hashim@pbc.com.pk"}, new String[]{"shahrukh.salman@pbc.com.pk"}, "Per Case Analysis | "+SDTitle+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(EmployeeHierarchy.getSDHead(i).USER_ID, pack,summary,StartDate2,EndDate2), new String[]{filename_PerCaseCost,filename_RevenuePerCase});

				//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk","jazeb@pbc.com.pk"},null, null, "Per Case Analysis | "+SDTitle+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(EmployeeHierarchy.getSDHead(i).USER_ID, pack,summary,StartDate2,EndDate2), new String[]{filename_PerCaseCost,filename_RevenuePerCase});
			}
			
			
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

	public static String getHTMLMessage(long EmployeeID, PerCaseAnalysisPackagePDF pack,PerCaseAnalysisPDF summary,Date StartDate3, Date EndDate3) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		Datasource ds = new Datasource();
		ds.createConnectionToReplica();
		
		
		Statement s = ds.createStatement();
		Statement s5 = ds.createStatement();
		Statement s2 = ds.createStatement();
		Statement s3 = ds.createStatement();
		
		
		
		String html = "<html>";
		html += "<body><br>";
		html+=" Period: "+Utilities.getDisplayDateFormat(StartDate3)+" - "+Utilities.getDisplayDateFormat(EndDate3)+"<br/><br/>";
		
		html += "<table style='width: 450px;'>";

		html += "<tr>";
		html += "<th style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;'>Summary</th>";
		html += "<th style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold; width: 120px'>Per CC</th>";
		
		html += "</tr>";
		html += "<tr>";
		html += "<td style='b1ackground-color: #3D5AB3; c1olor: white; height: 18px; text-align: left; f1ont-weight: bold;'>Sales Promotion</td>";
		html += "<td style='b1ackground-color: #3D5AB3; c1olor: white; height: 18px; text-align: center; f1ont-weight: bold;'>"+Utilities.getDisplayCurrencyFormatOneDecimal(pack.SALES_PROMOTION)+"</td>";
		
		html += "</tr>";
		html += "<tr>";
		html += "<td style='background-color: #E9EBF2; c1olor: white; height: 18px; text-align: left; f1ont-weight: bold;'>Upfront Discount</td>";
		html += "<td style='background-color: #E9EBF2; co1lor: white; height: 18px; text-align: center; f1ont-weight: bold;'>"+Utilities.getDisplayCurrencyFormatOneDecimal(pack.UPFRONT_DISCOUNT)+"</td>";
		
		html += "</tr>";
		html += "<tr>";
		html += "<td style='b1ackground-color: #3D5AB3; c1olor: white; height: 18px; text-align: left; f1ont-weight: bold;'>Retailer - Variable</td>";
		html += "<td style='b1ackground-color: #3D5AB3; c1olor: white; height: 18px; text-align: center; f1ont-weight: bold;'>"+Utilities.getDisplayCurrencyFormatOneDecimal(pack.RETAILER_VARIABLE)+"</th>";
		
		html += "</tr>";
		html += "<tr>";
		html += "<td style='background-color: #E9EBF2; co1lor: white; height: 18px; text-align: left; f1ont-weight: bold;'>Retailer - Fixed</td>";
		html += "<td style='background-color: #E9EBF2; c1olor: white; height: 18px; text-align: center; f1ont-weight: bold;'>"+Utilities.getDisplayCurrencyFormatOneDecimal(pack.RETAILER_FIXED)+"</td>";
				
		double total = pack.SALES_PROMOTION + pack.UPFRONT_DISCOUNT + pack.RETAILER_VARIABLE + pack.RETAILER_FIXED;
		
		double target = 0;
		if (EmployeeHierarchy.getSDHead(1).USER_ID == EmployeeID){
			target = 27.4;
		}else
		if (EmployeeHierarchy.getSDHead(2).USER_ID == EmployeeID){
			target = 25.2;
		}else
		if (EmployeeHierarchy.getSDHead(4).USER_ID == EmployeeID){
			target = 23.8;
		}else{
			target = 0;
		}
		
		
		html += "</tr>";
		html += "<tr>";
		html += "<th style='b1ackground-color: #3D5AB3; c1olor: white; height: 18px; text-align: left; fo1nt-weight: bold;'>Total Discount</th>";
		html += "<th style='b1ackground-color: #3D5AB3; c1olor: white; height: 18px; text-align: center; f1ont-weight: bold; '>"+Utilities.getDisplayCurrencyFormatOneDecimal(total)+"</th>";
		
		html += "</tr>";
		
		if (target != 0){
		html += "<tr>";
		html += "<th style='background-color: #E9EBF2; c1olor: white; height: 18px; text-align: left; fo1nt-weight: bold;'>Target</th>";
		html += "<th style='background-color: #E9EBF2; c1olor: white; height: 18px; text-align: center; fo1nt-weight: bold;'>"+target+"</th>";
		html += "</tr>";

		html += "<tr>";
		html += "<th style='background-color: #C8CFE6; c1olor: white; height: 18px; text-align: left; fo1nt-weight: bold;'>Difference</th>";
		html += "<th style='background-color: #C8CFE6; c1olor: white; height: 18px; text-align: center; fo1nt-weight: bold;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(target-total)+"</th>";
		html += "</tr>";
		}
		html += "</table>";
		
		html += "<br><br>";
		//html += "Bottom line Distributors.";
		
		html += "<table style='width: 450px;'>";

		html += "<tr>";
		html += "<th style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;'>Bottom-liner</th>";
		html += "<th style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold; width: 120px'>Net Revenue</th>";
		html += "</tr>";
		
		String RowColor="";
		
		for(int x=0;x<5;x++){
			if(x%2==0){
				RowColor="#E9EBF2";
			}else{
				RowColor="";
			}
				
			
			html += "<tr>";
			html += "<td style='background-color: "+RowColor+"; c1olor: white; height: 18px; text-align: left; f1ont-weight: bold;'>"+summary.BOTTOM_LINE_DISTRIBUTORS[x]+"</td>";
			html += "<td style='background-color: "+RowColor+"; c1olor: white; height: 18px; text-align: center; f1ont-weight: bold;'>"+Utilities.getDisplayCurrencyFormatRounded(summary.BOTTOM_LINE_DISTRIBUTORS_REVENUE[x])+"</td>";
			html += "</tr>";
		}
			
			
		
		
		
		
		
		
		for(int x=0;x<summary.BOTTOM_LINE_DISTRIBUTORS_REVENUE.length;x++){
			
		}
		
		
		html += "</table>";
			
	html += "<br><br>";
		
	html += "Please see attachment for details.";
			
			
			
		
		s.close();
		ds.dropConnection();	
		
		return html;
		
	}

}
