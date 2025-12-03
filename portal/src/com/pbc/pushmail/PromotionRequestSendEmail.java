package com.pbc.pushmail;

import java.io.FileOutputStream;
import java.io.IOException;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.mail.EmailException;

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

public class PromotionRequestSendEmail {
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	String HTMLMessage;
    
	public static void main(String[] args) throws DocumentException, IOException{
		try {
			PromotionRequestSendEmail pe = new PromotionRequestSendEmail();
			pe.setHTMLMessage();
			
			
			Utilities.sendPBCHTMLEmail(new String[]{"omerfk@pbc.com.pk"}, new String[]{"jazeb@pbc.com.pk","anas.wahab@pbc.com.pk"}, null, "Promotion Request | 1500ML | ID# 2528", pe.getHTMLMessage(), null);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getHTMLMessage(){
		return HTMLMessage;
	}
	
	public PromotionRequestSendEmail() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds.createConnection();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
	}
	

	public void setHTMLMessage(){
		
		String html = "<html>";
			html += "<body><br>";
		
			html += "<table style='width: 400px;'>";
			
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='4'>Promotional Product</td>";
				html += "</tr>";
			
				html += "<tr>";
					html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Package</td>";
					html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Brand</td>";
					html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Case</td>";
					html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Bottle</td>";
				html += "</tr>";
			
				html += "<tr>";
					html += "<td valign='top' style='background-color: #EDEFF2;'>1500ML";
					html += "<td style='background-color: #EDEFF2;'>PEPSI<br>MIRINDA<br>M.DEW";
					html += "<td valign='top' style='background-color: #EDEFF2; text-align: left; '>1";
					html += "<td valign='top' style='background-color: #EDEFF2; text-align: left;'>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td colspan='4'>&nbsp;";
				html += "</tr>";
		
		html += "<tr>";
		html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='4'>Free Product</td>";
		html += "</tr>";
	
		html += "<tr>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Package</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Brand</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Case</td>";
			html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Bottle</td>";
		html += "</tr>";
	
		html += "<tr>";
			html += "<td valign='top' style='background-color: #EDEFF2;'>500ML";
			html += "<td style='background-color: #EDEFF2;'>7-UP";
			html += "<td valign='top' style='background-color: #EDEFF2;text-align: left;'>";
			html += "<td valign='top' style='background-color: #EDEFF2;text-align: left;'>1";
		html += "</tr>";
		
		html += "<tr>";
		html += "<td colspan='4'>&nbsp;";
		html += "</tr>";
		
		html += "</table>";
		
		html += "<table style='width: 400px;'>";
		
		html += "<tr>";
		html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='2'>Profitability</td>";
		html += "</tr>";
		
		html += "<tr>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;'>Estimated Sales Volume (Cases)</td>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right;'>10,000</td>";
		html += "</tr>";
		html += "<tr>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;'>Sales SKU Price</td>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right;'>442</td>";
		html += "</tr>";
		html += "<tr>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;'>Free SKU Price (Bottle)</td>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right;'>50</td>";
		html += "</tr>";
		html += "<tr>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: normal;'>Variable Cost + Taxes</td>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right;'>250</td>";
		html += "</tr>";
		html += "<tr>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: left; font-weight: bold;'>Marginal Contribution</td>";
		html += "<td style='background-color: #EDEFF2; height: 18px; text-align: right; font-weight: bold;'>142</td>";
		html += "</tr>";
		
		html += "</table>";
		html += "<br>";
		html += "<table style='width: 400px;'>";
		html += "<tr>";
		html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: left; font-weight: bold;' colspan='1'>Comments</td>";
		html += "</tr>";
		html += "</table>";
		html += "<b>Jazeb Mehmood:</b> Looks feasible [Feb 25 | 04:25PM]<br>";
		html += "<b>Aamir Aftab:</b> Please proceed ASAP [Feb 24 | 11:46AM]<br>";
		html += "<br>";
		html += "<br>";
		html += "<b>Valid until</b> March 15, 2015<br>";
		html += "<br>";
		html += "<a href='#'>Approve</a> | <a href='#'>Decline</a> | <a href='#'>Send Message</a>";
		
		html += "</body>";
		
		
		html += "</html>";
		
		this.HTMLMessage = html;
	}

	
}
