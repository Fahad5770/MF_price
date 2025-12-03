<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>


<%@page import=" javax.activation.DataHandler"%>
<%@page import=" javax.activation.DataSource"%>
<%@page import=" javax.activation.FileDataSource"%>
<%@page import=" javax.mail.BodyPart"%>
<%@page import=" javax.mail.Message"%>
<%@page import=" javax.mail.MessagingException"%>
<%@page import=" javax.mail.Multipart"%>
<%@page import=" javax.mail.PasswordAuthentication"%>
<%@page import=" javax.mail.Session"%>
<%@page import=" javax.mail.Transport"%>
<%@page import=" javax.mail.internet.InternetAddress"%>
<%@page import=" javax.mail.internet.MimeBodyPart"%>
<%@page import=" javax.mail.internet.MimeMessage"%>
<%@page import=" javax.mail.internet.MimeMultipart"%>
<%@page import=" java.util.Properties"%>
<%@page import=" org.apache.commons.mail.*"%>





<style>
td{
font-size: 8pt;
}

</style>


<%
out.println(SendHTMLEmailToFarhan("zulqurnan.aslam@pbc.com.pk", "", "", "Yo Got a mail", "Finally I got a mail"));



%>


<%!

public String SendHTMLEmailToFarhan(String to, String cc, String bcc, String subject, String message15) {
	// Recipient's email ID needs to be mentioned.
     // String to = "zulqurnan.aslam@gmail.com";
  String Mess="";
      // Sender's email ID needs to be mentioned
      String from = "theia@moizfoods.com";

      final String username = "theia@moizfoods.com";//change accordingly
      final String password = "Theia@Pbc.987";//change accordingly

      // Assuming you are sending email through relay.jangosmtp.net
      String host = "mail.moizfoods.com";

      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
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
	        
        /* String[] filename= null;
         
         filename[0] = "d://pbc/KPI_MTD_5005_20181218.xlsx";
         
         
           for(int i=0;i<filename.length;i++) {
        	 
        	// Part two is attachment
	         messageBodyPart = new MimeBodyPart();
        	 DataSource source = new FileDataSource(Utilities.getEmailAttachmentsPath()+"/"+filename[i]);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(filename[i]);
	         multipart.addBodyPart(messageBodyPart);
         }
         
        */
        

         // Send the complete message parts
         message.setContent(multipart);

         // Send message
         Transport.send(message);

         System.out.println("Sent message successfully....");
         
          Mess = "Sent message successfully....";
         
  
      } catch (MessagingException e) {
        Mess = e.toString();
      }
      
      return Mess;
}






%>