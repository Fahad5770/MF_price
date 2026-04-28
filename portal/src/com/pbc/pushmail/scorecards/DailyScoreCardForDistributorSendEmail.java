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

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;

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
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.mail.*;
import org.apache.commons.mail.HtmlEmail;

public class DailyScoreCardForDistributorSendEmail {
	
	public static final String filename_orderbooker_sd1 = "OrderBookerScoreCard_Daily_FD_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".pdf";
	
	public static void main(String[] args) {
		
		try {
			
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			
			ResultSet rs91 = s.executeQuery("select distributor_id, name, snd_id, (select email from users where id = snd_id) snd_email, rsm_id, (select email from users where id = rsm_id) rsm_email, email_address, tdm_id, (select email from users where id = tdm_id) tdm_email from common_distributors where is_scorecard_enabled = 1");
			while(rs91.next()){
				String DistributorLabel = rs91.getString(2);
				long DistributorID = rs91.getLong(1);
				String DistributorEmail = rs91.getString("email_address");
				String SNDEmail = rs91.getString("snd_email");
				String RSMEmail = rs91.getString("rsm_email");
				String TDMEmail = rs91.getString("tdm_email");
				
				DailyOrderBookerScoreCardForDistributorPDF OrderBookerSD1 = new com.pbc.pushmail.scorecards.DailyOrderBookerScoreCardForDistributorPDF();
				OrderBookerSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_orderbooker_sd1, DistributorID);
				
				
				
				
				try{
					if (DistributorID == 100914){
						Utilities.sendPBCHTMLEmail(new String[]{DistributorEmail,"khalilahmad7053@gmail.com"},new String[]{SNDEmail,RSMEmail,TDMEmail,"omerfk@pbc.com.pk","anas.wahab@pbc.com.pk","shahrukh.salman@pbc.com.pk","imran.hashim@pbc.com.pk"}, null, "Distributor Score Card | "+DistributorID+"-"+DistributorLabel+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(DistributorID), new String[]{filename_orderbooker_sd1});
					}else{
						Utilities.sendPBCHTMLEmail(new String[]{DistributorEmail},new String[]{SNDEmail,RSMEmail,TDMEmail,"omerfk@pbc.com.pk","anas.wahab@pbc.com.pk","shahrukh.salman@pbc.com.pk","imran.hashim@pbc.com.pk"}, null, "Distributor Score Card | "+DistributorID+"-"+DistributorLabel+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(DistributorID), new String[]{filename_orderbooker_sd1});					
					}
				}catch(Exception e){System.out.println(e);}
				
				//Utilities.sendPBCHTMLEmail(new String[]{"dev@pbc.com.pk"}, null, null, "Daily Score Card | "+DistributorID+"-"+DistributorLabel+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(DistributorID), new String[]{filename_orderbooker_sd1});
			}
			
			
			s.close();
			ds.dropConnection();
			
			
//			try{
//				
//				HtmlEmail email = new HtmlEmail();
//				
//				//email.setSmtpPort(465);
//				//email.setSslSmtpPort("465");
//				//email.setPopBeforeSmtp(true, "mail.pbc.com.pk", "theia@pbc.com.pk", "smooking");
//
//				//email.setAuthenticator(new DefaultAuthenticator("theia@pbc.com.pk", "smooking"));
//				//email.setDebug(true);
//				//email.setHostName("mail.pbc.com.pk");
//				//email.setSSLCheckServerIdentity(false);
//				//email.setSSLOnConnect(true);
//				
//				//email.setSmtpPort(465);
//				//email.setAuthentication("theia@pbc.com.pk","smooking");
//				//email.setAuthenticator(new DefaultAuthenticator("theia@pbc.com.pk", "smooking"));
//				//email.setTLS(true);
//				//email.setSSL(true);
//				//email.setSSLOnConnect(true);
//				
//				//email.setSSLCheckServerIdentity(true);
//				//email.setStartTLSRequired(true);
//				
//				email.addTo("anaswhb@gmail.com");
//				email.setFrom("theia@pbc.com.pk", "Theia");
//				
//				email.setSubject("test");
//				
//				email.setMsg("test");
//				email.send();
//				
//			}catch(Exception e){
//				e.printStackTrace();
//			}
			
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
		
		
		String html = "<html>";
		html += "<body><br>";
			html += "<table style='width: 450px'></table>";

			html += "<br><br>";
				
			html += "Please see attachment for details.";
			
		return html;
		
	}

}
