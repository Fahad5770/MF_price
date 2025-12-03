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
import com.pbc.employee.EmployeeHierarchy;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class AccountsReceivableSummarySendEmail {
	
	public static final String filename_base = Utilities.getEmailAttachmentsPath()+ "/CreditSummary_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_out = Utilities.getEmailAttachmentsPath()+ "/CreditSummary_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_sd3 = Utilities.getEmailAttachmentsPath()+ "/CreditSummary_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_sd4 = Utilities.getEmailAttachmentsPath()+ "/CreditSummary_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_KA = Utilities.getEmailAttachmentsPath()+ "/CreditSummary_KA_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	public static final String filename_all = Utilities.getEmailAttachmentsPath()+ "/Receivables_AllRegions_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	public static void main(String[] args) {
		try {
			
			
			
			
			AccountsReceivableSummaryPDF basegroup = new com.pbc.pushmail.AccountsReceivableSummaryPDF();
			basegroup.setSND(EmployeeHierarchy.getSDHead(1).USER_ID);
			basegroup.createPdf(filename_base);
			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(1).USER_EMAIL}, new String[]{"omerfk@pbc.com.pk","nadeem@pbc.com.pk","asim.maan@pbc.com.pk","jazeb@pbc.com.pk","mashraf@pbc.com.pk","obaid@pbc.com.pk","nawaz@pbc.com.pk","anas.wahab@pbc.com.pk","shahrukh.salman@pbc.com.pk","salman.baig@pbc.com.pk","anjum.a.ansari@gmail.com","ihsan@pbc.com.pk","allah.ditta@pbc.com.pk","zahoor@pbc.com.pk","sohaib.zahid@pbc.com.pk","abdul.basit@pbc.com.pk"}, new String[]{"dev@pbc.com.pk"}, "Credit Summary | SD1 | "+Utilities.getDisplayDateTimeFormat(new Date()), basegroup.getHTMLMessage(), new String[]{"CreditSummary_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"});
			//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"}, null, null, "Credit Summary | SD1 | "+Utilities.getDisplayDateTimeFormat(new Date()), basegroup.getHTMLMessage(), new String[]{"CreditSummary_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"});
			
			AccountsReceivableSummaryPDF outregions = new com.pbc.pushmail.AccountsReceivableSummaryPDF();
			outregions.setSND(EmployeeHierarchy.getSDHead(2).USER_ID);
			outregions.createPdf(filename_out);
			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(2).USER_EMAIL}, new String[]{"omerfk@pbc.com.pk","nadeem@pbc.com.pk","asim.maan@pbc.com.pk","jazeb@pbc.com.pk","mashraf@pbc.com.pk","obaid@pbc.com.pk","nawaz@pbc.com.pk","anas.wahab@pbc.com.pk","shahrukh.salman@pbc.com.pk","salman.baig@pbc.com.pk","anjum.a.ansari@gmail.com","ihsan@pbc.com.pk","allah.ditta@pbc.com.pk","zahoor@pbc.com.pk","sohaib.zahid@pbc.com.pk","abdul.basit@pbc.com.pk"}, new String[]{"dev@pbc.com.pk"}, "Credit Summary | SD2 | "+Utilities.getDisplayDateTimeFormat(new Date()), outregions.getHTMLMessage(), new String[]{"CreditSummary_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"});
			//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"}, null, null, "Credit Summary | SD2 | "+Utilities.getDisplayDateTimeFormat(new Date()), outregions.getHTMLMessage(), new String[]{"CreditSummary_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"});

			AccountsReceivableSummaryPDF sd3 = new com.pbc.pushmail.AccountsReceivableSummaryPDF();
			sd3.setSND(EmployeeHierarchy.getSDHead(4).USER_ID);
			sd3.createPdf(filename_sd3);
			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(4).USER_EMAIL}, new String[]{"omerfk@pbc.com.pk","nadeem@pbc.com.pk","asim.maan@pbc.com.pk","jazeb@pbc.com.pk","mashraf@pbc.com.pk","obaid@pbc.com.pk","nawaz@pbc.com.pk","anas.wahab@pbc.com.pk","shahrukh.salman@pbc.com.pk","salman.baig@pbc.com.pk","anjum.a.ansari@gmail.com","ihsan@pbc.com.pk","allah.ditta@pbc.com.pk","zahoor@pbc.com.pk","sohaib.zahid@pbc.com.pk","abdul.basit@pbc.com.pk"}, new String[]{"dev@pbc.com.pk"}, "Credit Summary | SD3 | "+Utilities.getDisplayDateTimeFormat(new Date()), sd3.getHTMLMessage(), new String[]{"CreditSummary_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"});
			//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"}, null, null, "Credit Summary | SD3 | "+Utilities.getDisplayDateTimeFormat(new Date()), sd3.getHTMLMessage(), new String[]{"CreditSummary_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"});
			
			
			
			
			AccountsReceivableSummaryPDF sd4 = new com.pbc.pushmail.AccountsReceivableSummaryPDF();
			sd4.setSND(EmployeeHierarchy.getSDHead(6).USER_ID);
			sd4.createPdf(filename_sd4);
			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(6).USER_EMAIL}, new String[]{"omerfk@pbc.com.pk","nadeem@pbc.com.pk","asim.maan@pbc.com.pk","jazeb@pbc.com.pk","mashraf@pbc.com.pk","obaid@pbc.com.pk","nawaz@pbc.com.pk","anas.wahab@pbc.com.pk","shahrukh.salman@pbc.com.pk","salman.baig@pbc.com.pk","anjum.a.ansari@gmail.com","ihsan@pbc.com.pk","allah.ditta@pbc.com.pk","zahoor@pbc.com.pk","sohaib.zahid@pbc.com.pk","abdul.basit@pbc.com.pk"}, new String[]{"dev@pbc.com.pk"}, "Credit Summary | SD4 | "+Utilities.getDisplayDateTimeFormat(new Date()), sd4.getHTMLMessage(), new String[]{"CreditSummary_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"});
			//Utilities.sendPBCHTMLEmail(new String[]{"anas.wahab@pbc.com.pk","zulqurnan.aslam@pbc.com.pk"}, null, new String[]{"dev@pbc.com.pk"}, "Credit Summary | SD4 | "+Utilities.getDisplayDateTimeFormat(new Date()), sd4.getHTMLMessage(), new String[]{"CreditSummary_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"});
			
			
			AccountsReceivableSummaryPDF KA = new com.pbc.pushmail.AccountsReceivableSummaryPDF();
			KA.setSND(EmployeeHierarchy.getSDHead(5).USER_ID);
			KA.createPdf(filename_KA);
			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(5).USER_EMAIL}, new String[]{"omerfk@pbc.com.pk","nadeem@pbc.com.pk","asim.maan@pbc.com.pk","jazeb@pbc.com.pk","mashraf@pbc.com.pk","obaid@pbc.com.pk","nawaz@pbc.com.pk","anas.wahab@pbc.com.pk","shahrukh.salman@pbc.com.pk","salman.baig@pbc.com.pk","anjum.a.ansari@gmail.com","ihsan@pbc.com.pk","allah.ditta@pbc.com.pk","zahoor@pbc.com.pk","sohaib.zahid@pbc.com.pk","abdul.basit@pbc.com.pk"}, new String[]{"dev@pbc.com.pk"}, "Credit Summary | KA | "+Utilities.getDisplayDateTimeFormat(new Date()), KA.getHTMLMessage(), new String[]{"CreditSummary_KA_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"});
			//Utilities.sendPBCHTMLEmail(new String[]{"anas.wahab@pbc.com.pk","zulqurnan.aslam@pbc.com.pk"}, null, new String[]{"dev@pbc.com.pk"}, "Credit Summary | KA | "+Utilities.getDisplayDateTimeFormat(new Date()), sdKA.getHTMLMessage(), new String[]{"CreditSummary_KA_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf"});
			
			
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
