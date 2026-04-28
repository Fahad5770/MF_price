package com.pbc.pushmail;

import java.sql.SQLException;

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

public class CaneRatesComparisonTestSendEmail {
	
	public static final String KSML = Utilities.getEmailAttachmentsPath()+ "/KSML_Cane_Rates_Comparison_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String HSML = Utilities.getEmailAttachmentsPath()+ "/HSML_Cane_Rates_Comparison_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	public static void main(String[] args) {
		try {
			new com.pbc.pushmail.CaneRatesComparisonKSMLPDF().createPdf(KSML);
			new com.pbc.pushmail.CaneRatesComparisonHSMLPDF().createPdf(HSML);
			
			Datasource ds = new Datasource();
			ds.createConnectionKSML();
			Statement s = ds.createStatement();
			
			String NewMessage="";
			ResultSet rs12 = s.executeQuery("SELECT message FROM crman_email_messages where created_on_date=date(now())");
			if(rs12.first()){
				NewMessage = rs12.getString("message");
			}
			ds.dropConnection();
			
			// , "HSML_Cane_Rates_Comparison_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"
			
			
			//Utilities.sendPBCEmail(new String[]{"omerfk@pbc.com.pk","nadeem@pbc.com.pk","shahrukh.salman@pbc.com.pk"}, null, new String[]{"anas.wahab@pbc.com.pk"}, "Cane Rates Comparison "+Utilities.getDisplayDateFormat(new java.util.Date()), "This is a system generated message, please do not reply to it.", new String[]{"KSML_Cane_Rates_Comparison_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"});
			
			String msg = NewMessage;//"\n"+
			//"Circle Checawatni is the nearest area in zone 4. Cane flow is towards Tandliawala, Chaudary & Goutmet.\n\n"+
			//"In Circle Sheikh Fazil most private PC are supplying to Chaudary and Gourmet @ 170 and getting payment within 3 days.\n\n"+
			//"KSML position is very much down as compared to other mills even though for competitors cane traffic have to cover 40km extra distance.\n\n"+
			//"KSML procured 1500 ton from Zone 4, mostly from burewala which is almost the end of current zone 4.\n\nRegards,\nKashif Rashid\nManager R&D\n";
			//\n\nRegards,\nKashif Rashid\nManager R&D\n";
			
			
			
			Utilities.sendPBCEmail(new String[]{"shahrukh.salman@pbc.com.pk","kashif.rashid@pbc.com.pk"}, null, new String[]{"anas.wahab@pbc.com.pk"}, "Cane Market Watch "+Utilities.getDisplayDateFormat(new java.util.Date()), msg, new String[]{"KSML_Cane_Rates_Comparison_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf", "HSML_Cane_Rates_Comparison_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"});
			//Utilities.sendPBCEmail(new String[]{"omerfk@pbc.com.pk","nadeem@pbc.com.pk","shahrukh.salman@pbc.com.pk"}, new String[]{"kashif.rashid@pbc.com.pk"}, new String[]{"anas.wahab@pbc.com.pk"}, "Cane Market Watch "+Utilities.getDisplayDateFormat(new java.util.Date()), msg, new String[]{"KSML_Cane_Rates_Comparison_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf", "HSML_Cane_Rates_Comparison_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"});
			
			
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
