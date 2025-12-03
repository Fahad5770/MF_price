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

public class DistributorStockAnalysisSendEmail {
	
	public static final String filename_distributor_base = "Distributor_Stock_Analysis_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd1 = "Distributor_Stock_Analysis_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd2 = "Distributor_Stock_Analysis_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd3 = "Distributor_Stock_Analysis_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	
	
	public static void main(String[] args) {
		
		try {
			
			DistributorStockAnalysisExcel DistributorStockAnalysis = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
			DistributorStockAnalysis.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_distributor_base, 0);
			 
			Utilities.sendPBCHTMLEmail(new String[]{"imran.hashim@pbc.com.pk"},new String[]{"omerfk@pbc.com.pk","obaid@pbc.com.pk","anas.wahab@pbc.com.pk","shahrukh.salman@pbc.com.pk","anjum.a.ansari@gmail.com","jazeb@pbc.com.pk","ahsan.rashid@optimus-uae.com","muhammadameer@hotmail.com","rafiquemeo@yahoo.com","shehzadaleem@hotmail.com"}, null, "Distributor Stock Analysis | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_base});
			//Utilities.sendPBCHTMLEmail(new String[]{"anas.wahab@pbc.com.pk"},null, null, "Distributor Stock Analysis | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0), new String[]{filename_distributor_base});
			
			DistributorStockAnalysisExcel DistributorStockAnalysisSD1 = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
			DistributorStockAnalysisSD1.setSND(EmployeeHierarchy.getSDHead(1).USER_ID);
			DistributorStockAnalysisSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_distributor_sd1,0);
			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(1).USER_EMAIL}, new String[]{"omerfk@pbc.com.pk","obaid@pbc.com.pk","anas.wahab@pbc.com.pk","shahrukh.salman@pbc.com.pk","anjum.a.ansari@gmail.com","jazeb@pbc.com.pk","ahsan.rashid@optimus-uae.com","muhammadameer@hotmail.com","rafiquemeo@yahoo.com","shehzadaleem@hotmail.com"}, null, "Distributor Stock Analysis | SD1 | "+Utilities.getDisplayDateTimeFormat(new Date()), getHTMLMessage(0), new String[]{filename_distributor_sd1});
			
			DistributorStockAnalysisExcel DistributorStockAnalysisSD2 = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
			DistributorStockAnalysisSD2.setSND(EmployeeHierarchy.getSDHead(2).USER_ID);
			DistributorStockAnalysisSD2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_distributor_sd2,0);
			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(2).USER_EMAIL}, new String[]{"omerfk@pbc.com.pk","obaid@pbc.com.pk","anas.wahab@pbc.com.pk","shahrukh.salman@pbc.com.pk","anjum.a.ansari@gmail.com","jazeb@pbc.com.pk","ahsan.rashid@optimus-uae.com","muhammadameer@hotmail.com","rafiquemeo@yahoo.com","shehzadaleem@hotmail.com"}, null, "Distributor Stock Analysis | SD2 | "+Utilities.getDisplayDateTimeFormat(new Date()), getHTMLMessage(0), new String[]{filename_distributor_sd2});

			
			DistributorStockAnalysisExcel DistributorStockAnalysisSD3 = new com.pbc.pushmail.scorecards.DistributorStockAnalysisExcel();
			DistributorStockAnalysisSD3.setSND(EmployeeHierarchy.getSDHead(4).USER_ID);
			DistributorStockAnalysisSD3.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_distributor_sd3,0);
			Utilities.sendPBCHTMLEmail(new String[]{EmployeeHierarchy.getSDHead(4).USER_EMAIL}, new String[]{"omerfk@pbc.com.pk","obaid@pbc.com.pk","anas.wahab@pbc.com.pk","shahrukh.salman@pbc.com.pk","anjum.a.ansari@gmail.com","jazeb@pbc.com.pk","ahsan.rashid@optimus-uae.com","muhammadameer@hotmail.com","rafiquemeo@yahoo.com","shehzadaleem@hotmail.com"}, null, "Distributor Stock Analysis | SD3 | "+Utilities.getDisplayDateTimeFormat(new Date()), getHTMLMessage(0), new String[]{filename_distributor_sd3});
			//Utilities.sendPBCHTMLEmail(new String[]{"anas.wahab@pbc.com.pk"}, null, null, "Distributor Stock Analysis | SD3 | "+Utilities.getDisplayDateTimeFormat(new Date()), getHTMLMessage(0), new String[]{filename_distributor_sd3});
			
			
			
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
		ds.createConnectionToReplica();
		
		
		Statement s = ds.createStatement();
		Statement s5 = ds.createStatement();
		Statement s2 = ds.createStatement();
		Statement s3 = ds.createStatement();
		
		
		

		
		
		
		
		
		String html = "<html>";
		html += "<body><br>";
			html += "<p></p>";
			html += "<br><br>";
			html += "Please see attachment for details.<br>";
			
			
		
		s.close();
		ds.dropConnection();	
		
		return html;
		
	}

}
