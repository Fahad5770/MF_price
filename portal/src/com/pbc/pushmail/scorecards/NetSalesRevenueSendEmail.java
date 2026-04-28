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

public class NetSalesRevenueSendEmail {
	
	public static final String filename_net_trade_price = "NSR_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	
	
	public static void main(String[] args) {
		
		try {
			
			//PJPKPIsExcel PJPKPIs = new com.pbc.pushmail.scorecards.PJPKPIsExcel();
			NetSalesRevenueExcel obj = new NetSalesRevenueExcel();
			obj.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_net_trade_price, 1804);
			
			
			
			
			
			
			//Utilities.sendPBCHTMLEmail(new String[]{"jazeb@pbc.com.pk","omerfk@pbc.com.pk","asim.maan@pbc.com.pk","shayancbg@gmail.com"},new String[]{"anas.wahab@pbc.com.pk"}, null, "NSR | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0, obj.getHtmlBodyData()), new String[]{filename_net_trade_price});
			Utilities.sendPBCHTMLEmail(new String[]{"jazeb@pbc.com.pk"},new String[]{"anas.wahab@pbc.com.pk"}, null, "NSR | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(0, obj.getHtmlBodyData()), new String[]{filename_net_trade_price});
			
			
			
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

	public static String getHTMLMessage(long EmployeeID, String Data) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		Datasource ds = new Datasource();
		ds.createConnection();
		
		
		Statement s = ds.createStatement();
		
		
		String html = "<html>";
		html += "<body style='font-size:12px; font-family: Arial;'>";
		html += "<p><b>Net Sales Revenue</b></p>";
		html += "<table style='font-size:12px; font-family: Arial; width: 600px'>";
			
		html += Data;
			
		html += "</table>";
		html += "<br>Please see attachment for details.";
	
		s.close();
		ds.dropConnection();	
		
		return html;
		
	}

}
