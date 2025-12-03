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

public class CashInflowSummarySendEmailPre {
	
	public static final String filename = Utilities.getEmailAttachmentsPath()+ "/Cash_Inflow_Summary_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	public static void main(String[] args) {
		try {
			CashInflowSummaryV2PDF rep = new com.pbc.pushmail.CashInflowSummaryV2PDF();
			rep.createPdf(filename);
			
			
			
			
			//786
			//786
			//786
			Utilities.sendPBCHTMLEmail(new String[]{"asim.maan@pbc.com.pk"}, new String[]{"nawaz@pbc.com.pk","abid.hussain@pbc.com.pk","qamar.hussain@pbc.com.pk","qadeer@pbc.com.pk"}, new String[]{"anas.wahab@pbc.com.pk","shahrukh.salman@pbc.com.pk"}, "Cash Inflow Summary "+Utilities.getDisplayDateFormat(Utilities.getDateByDays(-1)), rep.getHTMLMessage(), new String[]{"Cash_Inflow_Summary_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"});
			
			
			
			
			
			
			
			//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"}, null, null, "Cash Inflow Summary "+Utilities.getDisplayDateFormat(Utilities.getDateByDays(-1)), rep.getHTMLMessage(), new String[]{"Cash_Inflow_Summary_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"});
			
			
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
