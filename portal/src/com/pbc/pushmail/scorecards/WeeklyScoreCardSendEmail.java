package com.pbc.pushmail.scorecards;

import java.sql.SQLException;

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
import com.pbc.util.Utilities;

public class WeeklyScoreCardSendEmail {
	
	public static final String filename_distributor_sd1 = "Distributor_ScoreCard_Weekly_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_distributor_sd2 = "Distributor_ScoreCard_Weekly_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	public static final String filename_orderbooker_sd1 = "OrderBooker_ScoreCard_Weekly_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_orderbooker_sd2 = "OrderBooker_ScoreCard_Weekly_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	
	public static final String filename_rsm_sd1 = "RSM_Comparison_Fortnightly_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_rsm_sd2 = "RSM_Comparison_Fortnightly_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	public static void main(String[] args) {
		try {
			
			
			
			
			
			//new com.pbc.pushmail.scorecards.WeeklyDistributorScoreCardPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd1, EmployeeHierarchy.getSDHead(1).USER_ID);
			//new com.pbc.pushmail.scorecards.WeeklyDistributorScoreCardPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd2, EmployeeHierarchy.getSDHead(2).USER_ID);
			
			
			new com.pbc.pushmail.scorecards.WeeklyRSMScoreCardPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_rsm_sd1, EmployeeHierarchy.getSDHead(1).USER_ID);
			//new com.pbc.pushmail.scorecards.WeeklyRSMScoreCardPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_rsm_sd2, EmployeeHierarchy.getSDHead(2).USER_ID);

				
			
			
			/*
			Utilities.sendPBCEmail(new String[]{"aamir.aftab@pbc.com.pk"},new String[]{"omerfk@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","shahzad.ahmed@pbc.com.pk","nadeem@pbc.com.pk","shahrukh.salman@pbc.com.pk"}, new String[]{"anas.wahab@pbc.com.pk"}, "Weekly Score Card | SD1 | "+Utilities.getDisplayDateFormat(new java.util.Date()), "This is a system generated message, please do not reply to it.", new String[]{filename_distributor_sd1});
			Utilities.sendPBCEmail(new String[]{"saqib.waheed@pbc.com.pk"},new String[]{"omerfk@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","shahzad.ahmed@pbc.com.pk","nadeem@pbc.com.pk","naeem.iqbal@pbc.com.pk","shahrukh.salman@pbc.com.pk"}, new String[]{"anas.wahab@pbc.com.pk"}, "Weekly Score Card | SD2 | "+Utilities.getDisplayDateFormat(new java.util.Date()), "This is a system generated message, please do not reply to it.", new String[]{filename_distributor_sd2});
			*/
			
			
			
			
			
			
			
			
			
			
			//Utilities.sendPBCEmail(new String[]{"aamir.aftab@pbc.com.pk","saqib.waheed@pbc.com.pk","ahmad.maqsood@pbc.com.pk","attiquerana@pbc.com.pk","arshad.mehboob@pbc.com.pk","saeed.abbas@pbc.com.pk","arshad.gill@pbc.com.pk","amjad.ali@pbc.com.pk"},new String[]{"omerfk@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","nadeem@pbc.com.pk","shahrukh.salman@pbc.com.pk"},new String[]{"anas.wahab@pbc.com.pk"}, "Fortnightly RSM Comparison | "+Utilities.getDisplayDateFormat(new java.util.Date()), "This is a system generated message, please do not reply to it.", new String[]{filename_rsm_sd1});
			
			
			
			
			Utilities.sendPBCEmail(new String[]{"dev@pbc.com.pk"},null,null, "Fortnightly RSM Comparison | "+Utilities.getDisplayDateFormat(new java.util.Date()), "This is a system generated message, please do not reply to it.", new String[]{filename_rsm_sd1});
			
			
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

}
