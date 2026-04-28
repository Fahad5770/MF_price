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
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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

public class DistributorKPIsSendEmail {
	
	
	public static final String stock_filename_distributor_all = "Stock_Analysis_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String stock_filename_distributor_sd1 = "Stock_Analysis_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String stock_filename_distributor_sd2 = "Stock_Analysis_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String stock_filename_distributor_sd3 = "Stock_Analysis_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String stock_filename_distributor_sd4 = "Stock_Analysis_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String stock_filename_distributor_sd5 = "Stock_Analysis_SD5_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static final String filename_distributor_all = "KPI_Dist_Today_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd1 = "KPI_Dist_Today_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd2 = "KPI_Dist_Today_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd3 = "KPI_Dist_Today_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd4 = "KPI_Dist_Today_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd5 = "KPI_Dist_Today_SD5_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	

	
	public static final String filename_pjp_all = "KPI_PJP_Today_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd1 = "KPI_PJP_Today_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd2 = "KPI_PJP_Today_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd3 = "KPI_PJP_Today_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd4 = "KPI_PJP_Today_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd5 = "KPI_PJP_Today_SD5_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static final String filename_pjp_all_mtd = "KPI_PJP_MTD_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd1_mtd = "KPI_PJP_MTD_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd2_mtd = "KPI_PJP_MTD_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd3_mtd = "KPI_PJP_MTD_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd4_mtd = "KPI_PJP_MTD_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_pjp_sd5_mtd = "KPI_PJP_MTD_SD5_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";

	
	public static final String filename_distributor_all_mtd = "KPI_Dist_MTD_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd1_mtd = "KPI_Dist_MTD_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd2_mtd = "KPI_Dist_MTD_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd3_mtd = "KPI_Dist_MTD_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd4_mtd = "KPI_Dist_MTD_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_distributor_sd5_mtd = "KPI_Dist_MTD_SD5_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static final String filename_no_sale_all = "No_Sale_Outlets_Today_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_no_sale_sd1 = "No_Sale_Outlets_Today_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_no_sale_sd2 = "No_Sale_Outlets_Today_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_no_sale_sd3 = "No_Sale_Outlets_Today_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_no_sale_sd4 = "No_Sale_Outlets_Today_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_no_sale_sd5 = "No_Sale_Outlets_Today_SD5_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	
	public static final String filename_sku_availability_all = "SKU_Availability_MTD_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_sku_availability_sd1 = "SKU_Availability_MTD_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_sku_availability_sd2 = "SKU_Availability_MTD_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_sku_availability_sd3 = "SKU_Availability_MTD_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_sku_availability_sd4 = "SKU_Availability_MTD_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_sku_availability_sd5 = "SKU_Availability_MTD_SD5_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static final String filename_volume_analysis_all = "Volume_Analysis_Today_All_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_volume_analysis_sd1 = "Volume_Analysis_Today_SD1_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_volume_analysis_sd2 = "Volume_Analysis_Today_SD2_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_volume_analysis_sd3 = "Volume_Analysis_Today_SD3_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_volume_analysis_sd4 = "Volume_Analysis_Today_SD4_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	public static final String filename_volume_analysis_sd5 = "Volume_Analysis_Today_SD5_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
	
	public static void main(String[] args) {
		
		try {
			
			
			
			
			
			//ASM
			
			
			Thread ASMThread = new Thread()
	        {
	            public void run() {
	                try{
	                
	                	
	            			
			Datasource ds = new Datasource();
			ds.createConnectionToReplica();
			Statement s = ds.createStatement();
			
			//ResultSet rs = s.executeQuery("select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email, (select email from users where id = dbp.asm_id) asm_email from distributor_beat_plan dbp where dbp.distributor_id in ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between from_days(to_days(curdate())-5) and curdate()) and asm_id is not null group by asm_id having ct = 1 and asm_id <= 3217");
			ResultSet rs = s.executeQuery("select dbp.asm_id, (select display_name from users where id = dbp.asm_id) asm_name, dbp.distributor_id, count(distinct dbp.distributor_id) ct, (select (select email from users where id = icd.tdm_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) tdm_email, (select (select email from users where id = icd.snd_id) from common_distributors icd where icd.distributor_id = dbp.distributor_id) snd_email, (select email from users where id = dbp.asm_id) asm_email from distributor_beat_plan dbp where dbp.distributor_id in ( select distinct mo.distributor_id from mobile_order mo where mo.created_on between from_days(to_days(curdate())-6) and curdate()) and asm_id is not null group by asm_id having ct = 1");
			
			while(rs.next()){
				
				
				try{
					long ASMID=rs.getLong(1);
					String ASMName = rs.getString(2);
					long DistributorID = rs.getLong(3);
					String TDMEmail = rs.getString("tdm_email");
					String SNDEmail = rs.getString("snd_email");
					String ASMEmail = rs.getString("asm_email");
					
					
					if (TDMEmail == null){
						TDMEmail = DistributorID+"@pbc.com.pk";
					}
					if (SNDEmail == null){
						SNDEmail = DistributorID+"@pbc.com.pk";
					}
					
					//System.out.println(DistributorID+" "+TDMEmail+" "+ASMID+" "+SNDEmail);
					
					String filename_pjp = "KPI_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_pjp_mtd = "KPI_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_no_sale = "No_Sale_Outlets_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_volume_analysis = "Volume_Analysis_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					String filename_SalesVsPackagesOutlets1 = "Outlet_Sales_Today_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					String filename_SalesVsPackagesOutlets2 = "Outlet_Sales_MTD_"+ASMID+"_"+Utilities.getSQLDateWithoutSeprator(new java.util.Date())+".xlsx";
					
					
					
					
					
					new com.pbc.pushmail.scorecards.ASMKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp, ASMID, false);
					new com.pbc.pushmail.scorecards.ASMKPIsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_pjp_mtd, ASMID, true);
					
					new com.pbc.pushmail.scorecards.ASMNoOrderReasonsExcel().createPdf(Utilities.getEmailAttachmentsPath()+ "/" + filename_no_sale, ASMID, false);
					
					//ASMVolumeAnalysisPSRExcel va2 = new com.pbc.pushmail.scorecards.ASMVolumeAnalysisPSRExcel();
					//va2.setSND(ASMID);
					//va2.createPdf(Utilities.getEmailAttachmentsPath()+ "/" +filename_volume_analysis,0);
					
					
					
					
					
					
					
					////////////////////
					

					
					
					////////Utilities.sendPBCHTMLEmail(new String[]{"zulqurnan.aslam@pbc.com.pk"},null,null,  "KPI | MDE | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale,filename_SalesVsPackagesOutlets1,filename_SalesVsPackagesOutlets2});
					
					
					//////////SendHTMLEmailMoiz(ASMEmail,"rizwan.jamil@thalindustries.com,asif.iqbal@almoiz.com,imran.asif@moizfoods.com,zeeshan.akram@moizfoods.com,mazhar.iqbal@thalindustries.com,aamir.razzaq@moizfoods.com","zulqurnan.aslam@pbc.com.pk,theia.notifications@pbc.com.pk,shahrukh.salman@pbc.com.pk",  "KPI | SO | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale,filename_SalesVsPackagesOutlets1,filename_SalesVsPackagesOutlets2});
					//break;
					
					
					SendHTMLEmailMoiz("zulqurnan.aslam@pbc.com.pk","","",  "KPI | SO | "+ASMID+"-"+ASMName+" | "+Utilities.getDisplayDateFormat(new java.util.Date()), getHTMLMessage(ASMID), new String[]{filename_pjp,filename_pjp_mtd,filename_no_sale,filename_SalesVsPackagesOutlets1,filename_SalesVsPackagesOutlets2});
					
					
				}catch(Exception e){
					e.printStackTrace();
				}
				//break;
			}			
			
			ds.dropConnection();
	                }catch(Exception e){e.printStackTrace();}
	            }
	        };
	        ASMThread.start();
	        
	        
	        
	        
			

		}  catch (Exception e){
			e.printStackTrace();
		}
	}

	public static String getHTMLMessage(long EmployeeID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		Datasource ds = new Datasource();
		ds.createConnection();
		
		
		Statement s = ds.createStatement();
		Statement s5 = ds.createStatement();
		Statement s2 = ds.createStatement();
		Statement s3 = ds.createStatement();
		
		
		
		
		///String html = "<html>";
		///html += "<body><br>";
				
			///html += "<br><br>";
				
			String html = "Please see attachments.";
			
			
		
		s.close();
		ds.dropConnection();	
		
		return html;
		
	}
	
	
	public static String getHTMLMessageTDM(long EmployeeID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		String html = "<html>";
		html += "<body><br>";
			html += "<table style='width: 450px'>";

			
			html += "<tr>";
			html += "<td colspan='2' style='text-align: left; font-size: 10px;'>&nbsp;</td>";
			html += "</tr>";
			
			html += "</table>";
				
			html += "<br><br>";
				
			html += "Please see attachment for details.<br>";
		
		return html;
		
	}
	
	
	static public void SendHTMLEmailMoiz(String to, String cc, String bcc, String subject, String message15, String[] filename) {
		// Recipient's email ID needs to be mentioned.
	     // String to = "zulqurnan.aslam@gmail.com";

	      // Sender's email ID needs to be mentioned
	      String from = "theia@moizfoods.com";

	      final String username = "theia@moizfoods.com";//change accordingly
	      final String password = "Theia@Pbc.987";//change accordingly

	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = "webmail.moizfoods.com";

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "false");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "25");

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
	            }
	         });

	      try {
	         // Create a default MimeMessage object.
	         Message message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.setRecipients(Message.RecipientType.TO,
	            InternetAddress.parse(to));
	         
	         
	         message.setRecipients(Message.RecipientType.CC,
	 	            InternetAddress.parse(cc));
	         
	         message.setRecipients(Message.RecipientType.BCC,
		 	            InternetAddress.parse(bcc));


	         // Set Subject: header field
	         message.setSubject(subject);

	         // Create the message part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Now set the actual message
	         messageBodyPart.setText(message15);

	         // Create a multipar message
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         
	         
	         for(int i=0;i<filename.length;i++) {
	        	 
	        	// Part two is attachment
		         messageBodyPart = new MimeBodyPart();
	        	 DataSource source = new FileDataSource(Utilities.getEmailAttachmentsPath()+"/"+filename[i]);
		         messageBodyPart.setDataHandler(new DataHandler(source));
		         messageBodyPart.setFileName(filename[i]);
		         multipart.addBodyPart(messageBodyPart);
	         }
	         
	        // String filename = "d://pbc/Outlet_Sales_MTD_SO_5004_FaisalIqbal_20181015.xlsx";
	        

	         // Send the complete message parts
	         message.setContent(multipart);

	         // Send message
	         Transport.send(message);

	         System.out.println("Sent message successfully....");
	  
	      } catch (MessagingException e) {
	         throw new RuntimeException(e);
	      }
	}

}
