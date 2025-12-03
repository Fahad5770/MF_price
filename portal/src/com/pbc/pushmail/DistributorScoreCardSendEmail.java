package com.pbc.pushmail;

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
import com.pbc.util.Utilities;

public class DistributorScoreCardSendEmail {
	
	public static final String filename_distributor_sd1 = "Distributor_ScoreCard_Daily_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_distributor_sd2 = "Distributor_ScoreCard_Daily_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	public static final String filename_orderbooker_sd1 = "OrderBooker_ScoreCard_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_orderbooker_sd2 = "OrderBooker_ScoreCard_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	
	public static void main(String[] args) {
		try {
			
			
			
			
			new com.pbc.pushmail.DistributorScoreCardPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd1, 1804);
			new com.pbc.pushmail.DistributorScoreCardPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_sd2, 2262);
			
			
			
			// 786
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			Utilities.sendPBCEmail(new String[]{"aamir.aftab@pbc.com.pk"},new String[]{"omerfk@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","shahzad.ahmed@pbc.com.pk","shahrukh.salman@pbc.com.pk","nadeem@pbc.com.pk"}, new String[]{"anas.wahab@pbc.com.pk"}, "Daily Score Card | SD1 | "+Utilities.getDisplayDateFormat(new java.util.Date()), "This is a system generated message, please do not reply to it.", new String[]{filename_distributor_sd1});
			Utilities.sendPBCEmail(new String[]{"saqib.waheed@pbc.com.pk"},new String[]{"omerfk@pbc.com.pk","jazeb@pbc.com.pk","obaid@pbc.com.pk","shahzad.ahmed@pbc.com.pk","shahrukh.salman@pbc.com.pk","nadeem@pbc.com.pk"}, new String[]{"anas.wahab@pbc.com.pk"}, "Daily Score Card | SD2 | "+Utilities.getDisplayDateFormat(new java.util.Date()), "This is a system generated message, please do not reply to it.", new String[]{filename_distributor_sd2});
			
			
			
			
			
			
			
			
			
			
			/*
			Utilities.sendPBCEmail(new String[]{"dev@pbc.com.pk"},null, null, "Daily Score Card | SD1 | "+Utilities.getDisplayDateFormat(new java.util.Date()), "This is a system generated message, please do not reply to it.", new String[]{filename_distributor_sd1});
			Utilities.sendPBCEmail(new String[]{"dev@pbc.com.pk"},null, null, "Daily Score Card | SD2 | "+Utilities.getDisplayDateFormat(new java.util.Date()), "This is a system generated message, please do not reply to it.", new String[]{filename_distributor_sd2});
			*/
			
			
			
			User RSMs[] = EmployeeHierarchy.getRSMs();
			for (User user: RSMs){
				
				if (user.USER_EMAIL != null){
					
					User SND = EmployeeHierarchy.getSND(user.USER_ID);
					
					String filename = "Distributor_ScoreCard_Daily_"+user.USER_ID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
					new com.pbc.pushmail.DistributorScoreCardPDF().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename, user.USER_ID);
					
					
					
					
					
					
					Utilities.sendPBCEmail(new String[]{user.USER_EMAIL},new String[]{"omerfk@pbc.com.pk",SND.USER_EMAIL,"jazeb@pbc.com.pk","obaid@pbc.com.pk","shahrukh.salman@pbc.com.pk","nadeem@pbc.com.pk"}, new String[]{"anas.wahab@pbc.com.pk"}, "Daily Score Card | "+user.REGION_NAME+" | "+user.USER_DISPLAY_NAME+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), "RSM: "+user.USER_DISPLAY_NAME+"\nSND: "+SND.USER_DISPLAY_NAME+" \n\nThis is a system generated message, please do not reply to it.", new String[]{filename});
					
					
					
					
					
					
					
					//Utilities.sendPBCEmail(new String[]{"dev@pbc.com.pk"},null, null, "Daily Score Card | "+user.REGION_NAME+" | "+user.USER_DISPLAY_NAME+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), "RSM: "+user.USER_DISPLAY_NAME+" ("+user.USER_EMAIL+")\nSND: "+SND.USER_DISPLAY_NAME+" ("+SND.USER_EMAIL+") \n\nThis is a system generated message, please do not reply to it.", new String[]{filename});
				}
				
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

}
