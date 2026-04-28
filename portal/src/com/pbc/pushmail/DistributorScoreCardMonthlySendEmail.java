package com.pbc.pushmail;

import java.sql.SQLException;

import com.pbc.util.Utilities;

import java.io.FileOutputStream;
import java.io.IOException;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
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

public class DistributorScoreCardMonthlySendEmail {
	
	
	public static final String filename_distributor_sd1 = "Distributor_ScoreCard_Monthly_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_distributor_sd2 = "Distributor_ScoreCard_Monthly_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	public static final String filename_orderbooker_sd1 = "OrderBooker_ScoreCard_Monthly_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_orderbooker_sd2 = "OrderBooker_ScoreCard_Monthly_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	
	
	public static void main(String[] args) {
		try {
			
			new com.pbc.pushmail.DistributorScoreCardMonthlyPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd1, 1804);
			new com.pbc.pushmail.DistributorScoreCardMonthlyPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd2, 2262);
			
			
	    	Calendar cc = Calendar.getInstance();
	    	cc.setTime(Utilities.getDateByDays(-7));
	    	
	    	int year = cc.get(Calendar.YEAR);
	    	int month = cc.get(Calendar.MONTH);
			
			String MonthYear = Utilities.getMonthNameByNumber(month+1) + " " + year;
			
			
			
			
			
			
			
			
			Utilities.sendPBCEmail(new String[]{"aamir.aftab@pbc.com.pk"},new String[]{"omerfk@pbc.com.pk","nadeem@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","shahzad.ahmed@pbc.com.pk"}, new String[]{"anas.wahab@pbc.com.pk","shahrukh.salman@pbc.com.pk"}, "Monthly Score Card | SD1 | "+MonthYear, "This is a system generated message, please do not reply to it.", new String[]{filename_distributor_sd1});
			Utilities.sendPBCEmail(new String[]{"saqib.waheed@pbc.com.pk"},new String[]{"omerfk@pbc.com.pk","nadeem@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","shahzad.ahmed@pbc.com.pk","naeem.iqbal@pbc.com.pk"}, new String[]{"anas.wahab@pbc.com.pk","shahrukh.salman@pbc.com.pk"}, "Monthly Score Card | SD2 | "+MonthYear, "This is a system generated message, please do not reply to it.", new String[]{filename_distributor_sd2});
			
			
			
			
			
			
			
			/*
			Utilities.sendPBCEmail(new String[]{"dev@pbc.com.pk"},null, null, "Monthly Score Card | SD1 | "+MonthYear, "This is a system generated message, please do not reply to it.", new String[]{filename_distributor_sd1});
			Utilities.sendPBCEmail(new String[]{"dev@pbc.com.pk"},null, null, "Monthly Score Card | SD2 | "+MonthYear, "This is a system generated message, please do not reply to it.", new String[]{filename_distributor_sd2});
			*/
			
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
