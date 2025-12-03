package com.pbc.bi;

import java.io.FileOutputStream;
import java.io.IOException;
 









import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.pbc.common.Distributor;
import com.pbc.inventory.Product;
import com.pbc.inventory.SalesPosting;
import com.pbc.util.Datasource;
import com.pbc.util.UserAccess;
import com.pbc.util.Utilities;

public class TestAnalytics {

		
	
	public static void main(String[] args) throws DocumentException, IOException, ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		 
		 
		Datasource ds = new Datasource();
		ds.createConnection();
		Connection c = ds.getConnection();
		Statement s = c.createStatement();
		Statement s2 = c.createStatement();

		long UVID = Utilities.getUniqueVoucherID(1);
		Product.insertBackOrder(200769, 4256542, 1);
		SalesPosting.postOrder2Invoice(4256542, 1, UVID);
		//createBalance();

		// Account Transfer - TO
		// A/C To A/C Trf. TO 300100293940001 # 280985
		/*
		ResultSet rs = s.executeQuery("SELECT * FROM reconciliation.record where particulars like 'A/C To A/C Trf. TO%'");
		while(rs.next()){
			long gsr = rs.getLong("gsr");
			String description = rs.getString("particulars");
			
			description = description.toLowerCase();
			System.out.println(description);
			
			String account_no = description.substring(description.indexOf("a/c to a/c trf. t")+19,description.indexOf("# ")-1);
			
			System.out.println(account_no);
			
			s2.executeUpdate("update record set transaction_type = 'Account Transfer' where gsr = "+gsr);
			s2.executeUpdate("update record set transferred_to = '"+account_no+"' where gsr = "+gsr);
		}
		*/

		// Account Transfer - From
		// A/C To A/C Trf. From 201300173960001 # 373080
		/*
		ResultSet rs = s.executeQuery("SELECT * FROM reconciliation.record where particulars like 'A/C To A/C Trf. Fr%'");
		while(rs.next()){
			long gsr = rs.getLong("gsr");
			String description = rs.getString("particulars");
			
			description = description.toLowerCase();
			System.out.println(description);
			
			String account_no = description.substring(description.indexOf("a/c to a/c trf. f")+21,description.indexOf("# ")-1);
			
			System.out.println(account_no);
			
			s2.executeUpdate("update record set transaction_type = 'Account Transfer' where gsr = "+gsr);
			s2.executeUpdate("update record set transferred_from = '"+account_no+"' where gsr = "+gsr);
		}
		*/

		// Fee And Charges
		
		//s2.executeUpdate("update record set transaction_type = 'Bank Charges' where particulars like '%Charges%'");
		
		
		
		// Inward Clearing
		// Inward Clearing.. TO 900184301140586 # 371159,Cheque No:30850661 Cheque No:30850661
		/*
		ResultSet rs = s.executeQuery("SELECT * FROM reconciliation.record where particulars like 'Inward Clearing%'");
		while(rs.next()){
			long gsr = rs.getLong("gsr");
			String description = rs.getString("particulars");
			
			description = description.toLowerCase();
			System.out.println(description);
			
			String cheque_no = "";
			if (description.indexOf("cheque no") != -1){
				cheque_no = description.substring(description.indexOf("cheque no")+10,description.lastIndexOf("cheque no")-1);
			}else{
				cheque_no  = description.substring(description.indexOf("chq")+4,description.lastIndexOf("chq")-1);
			}
			System.out.println(cheque_no);
			
			s2.executeUpdate("update record set transaction_type = 'Inward Clearing' where gsr = "+gsr);
			s2.executeUpdate("update record set cheque_no = '"+cheque_no+"' where gsr = "+gsr);
			
		}
		*/
		
		// Inward Clearing
		// Same Day i/W Clering TO 900183186010586 # 380040,Cheque No:30852159 Cheque No:30852159
		/*
		ResultSet rs = s.executeQuery("SELECT * FROM reconciliation.record where particulars like 'Same Day i/w%'");
		while(rs.next()){
			long gsr = rs.getLong("gsr");
			String description = rs.getString("particulars");
			
			description = description.toLowerCase();
			System.out.println(description);
			
			String cheque_no = "";
			if (description.indexOf("cheque no") != -1){
				cheque_no = description.substring(description.indexOf("cheque no")+10,description.lastIndexOf("cheque no")-1);
			}else{
				cheque_no  = description.substring(description.indexOf("chq")+4,description.lastIndexOf("chq")-1);
			}
			System.out.println(cheque_no);
			
			s2.executeUpdate("update record set transaction_type = 'Inward Clearing' where gsr = "+gsr);
			s2.executeUpdate("update record set cheque_no = '"+cheque_no+"' where gsr = "+gsr);
			
		}
		 */
		
		
		
		
		//Cash withdrawl
		//	Cash Withdrawal   # 370244,Cheque No:30850637 Cheque No:30850637
		/*
		ResultSet rs = s.executeQuery("SELECT * FROM reconciliation.record where particulars like 'Cash Withdrawal%'");
		while(rs.next()){
			long gsr = rs.getLong("gsr");
			String description = rs.getString("particulars");
			
			description = description.toLowerCase();
			System.out.println(description);
			
			String cheque_no = "";
			if (description.indexOf("cheque no") != -1){
				cheque_no = description.substring(description.indexOf("cheque no")+10,description.lastIndexOf("cheque no")-1);
			}else{
				cheque_no  = description.substring(description.indexOf("chq")+4,description.lastIndexOf("chq")-1);
			}
			System.out.println(cheque_no);
			
			s2.executeUpdate("update record set transaction_type = 'Cash Withdrawal' where gsr = "+gsr);
			s2.executeUpdate("update record set cheque_no = '"+cheque_no+"' where gsr = "+gsr);
			
		}
		*/
		
		// Cheque Numbers
		/*
		ResultSet rs = s.executeQuery("SELECT * FROM reconciliation.record where particulars like '%cheque no%cheque no%' or  particulars like '%chq%chq%'");
		while(rs.next()){
			long gsr = rs.getLong("gsr");
			String description = rs.getString("particulars");
			
			description = description.toLowerCase();
			System.out.println(description);
			
			String cheque_no = "";
			if (description.indexOf("cheque no") != -1){
				cheque_no = description.substring(description.indexOf("cheque no")+10,description.lastIndexOf("cheque no")-1);
			}else{
				cheque_no  = description.substring(description.indexOf("chq")+4,description.lastIndexOf("chq")-1);
			}
			
			//System.out.println(cheque_no);
			if (cheque_no.indexOf(" ") != -1){
				cheque_no = cheque_no.substring(0,cheque_no.indexOf(" "));
			}
			
			System.out.println(cheque_no);
			
			s2.executeUpdate("update record set cheque_no = '"+cheque_no+"' where gsr = "+gsr);
			
		}
		
		*/
		/*
		ResultSet rs = s.executeQuery("SELECT * FROM reconciliation.pbc_bip where additional_reference in (300100293940025,100216990590025,102610414520025,201300174420025,20130173960025,201300157330025,202900079190025,300100293940221,3001002939400940001,3001002939400940003,3001002939400940004,3001002939400940005,3001002939400940006,3001002939404410001,3001002939404410002,3001002939404610001,3001002939404610002,100216990593121,100216968900221,300100293943121,2007003193300940001,2007003193300940002,2007000914800940001,2007000914800940002,2007000914804610001,107900026155511,102610414528106,102610414528411,10269820505058662688,2013001739604610001,2013001739604610002,201300156440001,2013001573304610001,2013001573304610002,2013001573304610003,2013001573304710001,202900078890001,202900078890221,202900079190001,2029000791900940001,2029000791904410001,2029000791904420001,2029000791904610001,2029000791904730001,202900079198106,202900079198107,200200396080201) and narr like '%General Credit From%'");
		while(rs.next()){
			long id = rs.getLong("id");
			String narr = rs.getString("narr");
			
			narr = narr.toLowerCase();
			System.out.println(narr);
			
			String account_no = "";
			if (narr.indexOf("general credit") != -1){
				account_no = narr.substring(narr.indexOf("general credit from")+20,narr.indexOf("#")-1);
			}
			System.out.println(account_no);
			
			s2.executeUpdate("update reconciliation.pbc_bip set pool_to_account = '"+account_no+"' where id = "+id);
		}
		*/
		/*
		ResultSet rs = s.executeQuery("SELECT * FROM reconciliation.pbc_bip where additional_reference in (300100293940025,100216990590025,102610414520025,201300174420025,20130173960025,201300157330025,202900079190025,300100293940221,3001002939400940001,3001002939400940003,3001002939400940004,3001002939400940005,3001002939400940006,3001002939404410001,3001002939404410002,3001002939404610001,3001002939404610002,100216990593121,100216968900221,300100293943121,2007003193300940001,2007003193300940002,2007000914800940001,2007000914800940002,2007000914804610001,107900026155511,102610414528106,102610414528411,10269820505058662688,2013001739604610001,2013001739604610002,201300156440001,2013001573304610001,2013001573304610002,2013001573304610003,2013001573304710001,202900078890001,202900078890221,202900079190001,2029000791900940001,2029000791904410001,2029000791904420001,2029000791904610001,2029000791904730001,202900079198106,202900079198107,200200396080201) and narr like '%Break Term Deposit From%'");
		while(rs.next()){
			long id = rs.getLong("id");
			String narr = rs.getString("narr");
			
			narr = narr.toLowerCase();
			System.out.println(narr);
			
			String account_no = "";
			if (narr.indexOf("break term deposit from") != -1){
				account_no = narr.substring(narr.indexOf("break term deposit from")+24,narr.indexOf("#")-1);
			}
			System.out.println(account_no);
			
			s2.executeUpdate("update reconciliation.pbc_bip set pool_to_account = '"+account_no+"' where id = "+id);
		}
		
		s.close();
		c.close();
		ds.dropConnection();
	
	}
	*/
	/*
	public static void createBalance() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		Connection c = ds.getConnection();
		Statement s = c.createStatement();
		Statement s2 = c.createStatement();
		
		ResultSet rs = s.executeQuery("select distinct TRANS_DATE from pbc_bip where TRANS_DATE is not null order by TRANS_DATE");
		while(rs.next()){
			
			Date TDate = rs.getDate(1);
			
			double balance = 0;
			ResultSet rs2 = s2.executeQuery("SELECT sum(ifnull(debit_amount,0))-sum(ifnull(credit_amount,0)) balance FROM reconciliation.pbc_bip where TRANS_DATE between '2012-12-31' and "+Utilities.getSQLDate(TDate)+" and additional_reference in (300100293940025,100216990590025,102610414520025,201300174420025,20130173960025,201300157330025,202900079190025,300100293940221,3001002939400940001,3001002939400940003,3001002939400940004,3001002939400940005,3001002939400940006,3001002939404410001,3001002939404410002,3001002939404610001,3001002939404610002,100216990593121,100216968900221,300100293943121,2007003193300940001,2007003193300940002,2007000914800940001,2007000914800940002,2007000914804610001,107900026155511,102610414528106,102610414528411,10269820505058662688,2013001739604610001,2013001739604610002,201300156440001,2013001573304610001,2013001573304610002,2013001573304610003,2013001573304710001,202900078890001,202900078890221,202900079190001,2029000791900940001,2029000791904410001,2029000791904420001,2029000791904610001,2029000791904730001,202900079198106,202900079198107,200200396080201)");
			while(rs2.next()){
				balance = rs2.getDouble(1);
			}
			
			double debit = 0;
			double credit = 0;
			ResultSet rs3 = s2.executeQuery("SELECT sum(ifnull(debit_amount,0)), sum(ifnull(credit_amount,0)) FROM reconciliation.pbc_bip where TRANS_DATE = "+Utilities.getSQLDate(TDate));
			while(rs3.next()){
				debit = rs3.getDouble(1);
				credit = rs3.getDouble(2);
			}
			
			s2.executeUpdate("insert into balance values("+Utilities.getSQLDate(TDate)+","+debit+","+credit+","+balance+")");
			
		}
		
		
		ds.dropConnection();
		
	
		*/
	}
	
}
