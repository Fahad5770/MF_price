package com.pbc.bi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HeaderFooter;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Header;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.sun.prism.paint.Color;

public class MarketWatchSourcePDF_Dist_statment {

	
	
	public static final String RESULT = Utilities.getEmailAttachmentsPath()+ "/CashInflowSummary.pdf";
		
	Font fonttableheader = FontFactory.getFont("Arial", 8, Font.BOLD);
	Font fontheading = FontFactory.getFont("Arial", 8, Font.NORMAL);
	Font fontpbc = FontFactory.getFont("Arial", 10, Font.BOLD);
	Font fonttitle = FontFactory.getFont("Arial", 10, Font.BOLD);
	Font fonttitleblack = FontFactory.getFont("Arial", 10, Font.BOLD);
	Font reportheading = FontFactory.getFont("Arial", 8, Font.BOLD);
	Font FontDisclaimnr=FontFactory.getFont("Arial",8);
    Font FontDisclaimerHeading=FontFactory.getFont("Arial",10,Font.BOLD);
	
	Datasource ds = new Datasource();
	Statement s1;	Statement s2;	Statement s3;	Statement s4;	Statement s5;
	Statement s6;	Statement s7;	Statement s8;	Statement s9;	Statement s10;
	Statement s11;	Statement s12;	Statement s13;	Statement s14;	Statement s15;
	Statement s16;	Statement s51;	Statement s52;	Statement s53;	Statement s54;
	Statement s55;	Statement s61;	Statement s60;	Statement s59;	Statement s58;	
	Statement s57;	Statement s56;	Statement s62;	Statement s63;	Statement s64;	
	Statement s65;	Statement s66;	Statement sSAP;


	
//	Date StartDate = new Date();
//	Date EndDate = new Date();
	
	
	Date Yesterday = Utilities.getDateByDays(-1);
	
	
	long SND_ID = 0;
//	static long CID=101026;
//	Date Sdate=Utilities.parseDate("01/01/2017");
//	Date Edate=Utilities.parseDate("03/10/2017");
//	String PACKAGE_ID="";
	
	public static void main(String[] args) throws DocumentException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		
		
		
		//int a[]={100920,100951,};
		
			
		try {
			//new DistributorScoreCardPDF().createPdf(RESULT, this.SND_ID);
			/////doneeeeeeeeeeeeee --- long DistributorArray [] = {100001,100055,100096,100287,100454,100636,100657,100714,100867,100877,100920,100951,100962,100965,100982,100988,100989,100990,100991,100994,101003,101004,101006,101011};
			
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s1=ds.createStatement();
			
			
			
			//Batch 2
			/////// Doneeeeeeeeeeeeeeeeeeee --- long DistributorArray []={100002,100006,100008,100017,100028,100049,100051,100053,100054,100056,100059,100065,100095,100099,100101,100227,100301,100311,100312,100327,100396,100401,100416,100431,100439,100441,100552,100553,100574,100586};
			
			
			//Batch no 3
			//////Done - long DistributorArray []={100590,100618,100619,100621,100631,100638,100684,100692,100695,100706,100741,100746,100776,100785,100811,100846,100914,100919,100922,100929};
			
			
			//batch no 4
			/////long DistributorArray []={100964,100967,100971,100985,100986,100987,100992,100993,100995,100997,100998,100999,101000,101002,101005,101012,101016,101021,101022};
			
			
			long DistributorArray []={	
										100741,
										100746,
										100776,
										100785,
										100811,
										100846,
										100914,
										100919,
										100922,
										100929,
										100964,
										100967,
										100971,
										100985,
										100986,
										100987,
										100992,
										100993,
										100995,
										100997,
										100998,
										100999,
										101000,
										101002,
										101005,
										101012,
										101016,
										101021,
										101022,
										101026,
										101027,
										101028,
										101029
									};
			
		for(int i=0;i<DistributorArray.length;i++){
			MarketWatchSourcePDF_Dist_statment DistributorSD1 = new MarketWatchSourcePDF_Dist_statment();
			DistributorSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/DistributorStatement_"+DistributorArray[i]+".pdf", DistributorArray[i],Utilities.parseDate("01/01/2017"),Utilities.parseDate("31/12/2017"),"");
		
			System.out.println(DistributorArray[i]+" - Generated!!");
		
		}
			
			
			//Inserting into dist table 
			///////////////////////////////////////
			
			//long DistributorArray []={100002,100006,100008,100017,100028,100049,100051,100053,100054,100056,100059,100065,100095,100099,100101,100227,100301,100311,100312,100327,100396,100401,100416,100431,100439,100441,100552,100553,100574,100586};
			
			
			///long DistributorArray []={100590,100618,100619,100621,100631,100638,100684,100692,100695,100706,100741,100746,100776,100785,100811,100846,100914,100919,100922,100929};
			
			
			/////long DistributorArray []={100964,100967,100971,100985,100986,100987,100992,100993,100995,100997,100998,100999,101000,101002,101005,101012,101016,101021,101022};
			
			
			/////for(int i=0;i<DistributorArray.length;i++){
				///s1.executeUpdate("insert into pep.dp_account_statement (distributor_id,url,year,month) values("+DistributorArray[i]+",'/usr/share/apache-tomcat-7.0.62/webapps/distributor/distributorstatement_pdf/',2017,10)");
				
				////System.out.println("insert into pep.dp_account_statement (distributor_id,url,year,month) values("+DistributorArray[i]+",'/usr/share/apache-tomcat-7.0.62/webapps/distributor/distributorstatement_pdf/',2017,10)");
			///////}
			
			 
			
			
			
			
			////////////////////////
			////////////////////////
			
			
			
	//	System.out.println("Finished!!");	
		
		
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public MarketWatchSourcePDF_Dist_statment() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
				
		
		
		ds.createConnection();
		s1 = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
		s5 = ds.createStatement();
		s6 = ds.createStatement();
		s7 = ds.createStatement();
		s8 = ds.createStatement();
		s9 = ds.createStatement();
		s10 = ds.createStatement();
		s11= ds.createStatement();
		s12= ds.createStatement();
		s13= ds.createStatement();
		s14= ds.createStatement();
		s15= ds.createStatement();
		s16= ds.createStatement();		s51= ds.createStatement();		s52= ds.createStatement();		s53= ds.createStatement();
		s57= ds.createStatement();		s56= ds.createStatement();		s55= ds.createStatement();		s54= ds.createStatement();
		s58= ds.createStatement();		s61= ds.createStatement();		s60= ds.createStatement();		s59= ds.createStatement();
		s62= ds.createStatement();		s63= ds.createStatement();		s64= ds.createStatement();		s65= ds.createStatement();	
		s66= ds.createStatement();	
		if (Utilities.getDayOfWeekByDate(Yesterday) == 6){
			Yesterday = Utilities.getDateByDays(-2);
		}
		 Datasource dsSAP = new Datasource();
		 dsSAP.createConnectionToSAPDB();
		 Connection cSAP = dsSAP.getConnection();
		  sSAP = cSAP.createStatement();

	}
	
/*	public void getArgs(long CID2,Date sdate2,Date edate2 , String PCKG2)
	{
		
		CID=CID2;
		Sdate=sdate2;
		Edate=edate2;
		PACKAGE_ID=PCKG2;
//		System.out.println("CID is :"+CID+", Sdate is :"+Sdate+" , Edate is :"+Edate);
			
	}*/
	
	/**
	 * @param filename
	 * @param SND_ID
	 * @throws DocumentException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void createPdf(String filename, long CID,Date Sdate,Date Edate,String PACKAGE_ID) throws DocumentException, IOException, SQLException {
		
	//	this.SND_ID = SND_ID;
		
	//	  Header event = new Header();
	   
		
		Document document = new Document(PageSize.A4.rotate());
//		 PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
	      
		PdfWriter.getInstance(document, new FileOutputStream(filename));
//		 writer.setPageEvent(event);	    
	    document.open();
	    
	    
	    
		long TotalZeroSaleOutlets = 0;
		long ZeroSaleOutletsToDisplay = 0;
	    
		

       
		
	    
		String Distributor="";
		String DistName="";
		String DistContact="";
		
		ResultSet rs1 = s1.executeQuery("SELECT * FROM pep.common_distributors where distributor_id="+CID);
		if(rs1.first()){
			Distributor = rs1.getLong("distributor_id")+" - "+rs1.getString("name");
			DistName = rs1.getString("name2");
			DistContact = rs1.getString("contact_no");
		}
		
		long TotalOutlets=0;
		ResultSet rs2 = s2.executeQuery("SELECT count(*) total_outlet FROM pep.common_outlets where cache_distributor_id="+CID);
		if(rs2.first()){
			TotalOutlets = rs2.getLong("total_outlet");
		}
		Date FirstLifting = new Date();
		Date LastLifting = new Date();
		
		ResultSet rs3 = s3.executeQuery("SELECT min(created_on) first_lifting FROM pep.inventory_delivery_note where distributor_id="+CID);
		if(rs3.first()){
			FirstLifting = rs3.getDate("first_lifting");
		}
		
		ResultSet rs4 = s4.executeQuery("SELECT max(created_on) last_lifting FROM pep.inventory_delivery_note where distributor_id="+CID);
		if(rs4.first()){
			LastLifting = rs4.getDate("last_lifting");
		}
		
		double CreditLimit=0;
		ResultSet rs66 = s66.executeQuery("SELECT cd.distributor_id, cd.name, (select ifnull(sum(debit),0) - ifnull(sum(credit),0) from gl_transactions_accounts where account_id in (select id from gl_accounts where customer_id = cd.distributor_id and type_id = 1)) ledger_balance, (select ifnull(sum(debit),0) - ifnull(sum(credit),0) from gl_transactions_accounts where account_id in (select id from gl_accounts where customer_id = cd.distributor_id and type_id = 3)) one_time_credit_balance, ifnull((SELECT credit_limit FROM gl_customer_credit_limit where customer_id = cd.distributor_id and is_active = 1 and curdate() between valid_from and valid_to limit 1),0) credit_limit, ifnull((SELECT sum(gop.order_amount) FROM gl_order_posting gop where gop.customer_id = cd.distributor_id and gop.is_invoiced = 0 and gop.order_no not in (select sap_order_no from inventory_delivery_note)),0) unlifted_orders, ifnull((SELECT sum(gip.invoice_amount) FROM gl_invoice_posting gip where gip.customer_id = cd.distributor_id and gip.order_no not in (select sap_order_no from inventory_delivery_note)),0) unlifted_invoices FROM pep.common_distributors cd  where 1=1 and cd.distributor_id="+CID+" having ledger_balance != 0 or one_time_credit_balance != 0 or credit_limit != 0 or unlifted_orders != 0 or unlifted_invoices != 0");
		while(rs66.next()){
		
			CreditLimit = rs66.getDouble("credit_limit");
		}
		
		long AccountID =0;
		int counter=0;			
		double Balance=0;
		ResultSet rs5 = s5.executeQuery("select * from gl_accounts where 1=1 and type_id in (1) and customer_id="+CID);
		if(rs5.first()){
			AccountID = rs5.getLong("id");
		}
		
		double OpeningBalanceLedger = 0;
		//opening balance
		ResultSet rs6 = s6.executeQuery("Select sum(debit), sum(credit) FROM gl_transactions_accounts glta join gl_transactions glt  on glta.id=glt.id where account_id="+AccountID+" and created_on_date <= "+Utilities.getSQLDate(Edate));
		if(rs6.first()){
			OpeningBalanceLedger = rs6.getDouble(1) - rs6.getDouble(2);
		}
		
		//Credit
		ResultSet rs7 = s7.executeQuery("select * from gl_accounts where 1=1 and type_id in (3) and customer_id ="+CID);
		if(rs7.first()){
			AccountID = rs7.getLong("id");
		}
		
		double OpeningBalanceCredit = 0;
		ResultSet rs8 = s8.executeQuery("Select sum(debit), sum(credit) FROM gl_transactions_accounts glta join gl_transactions glt  on glta.id=glt.id where account_id="+AccountID+" and created_on_date <= "+Utilities.getSQLDate(Edate));//Error here i put Sdate instead of Edate
		if(rs8.first()){
			OpeningBalanceCredit = rs8.getDouble(1) - rs8.getDouble(2);
		}
		
		//Advance
		
		ResultSet rs9 = s9.executeQuery("select * from gl_accounts where 1=1 and type_id in (6) and customer_id ="+CID);
		if(rs9.first()){
			AccountID = rs9.getLong("id");
		}
		
		double OpeningBalanceAdvance = 0;
		ResultSet rs10 = s10.executeQuery("Select sum(debit), sum(credit) FROM gl_transactions_accounts glta join gl_transactions glt  on glta.id=glt.id where account_id="+AccountID+" and created_on_date <= "+Utilities.getSQLDate(Edate));
		if(rs10.first()){
			OpeningBalanceAdvance = rs10.getDouble(1) - rs10.getDouble(2);
		}
		Balance = OpeningBalanceLedger;						
		
		
		

		
		//SAP Start
		// SAP COMMENTED
		com.pbc.sap.SAPUtilities obj = new com.pbc.sap.SAPUtilities();
		obj.connectPRD();

		double INC_BAL = 0;
		double ATAX_BAL = 0;
		double CRSAL_BAL = 0;
		double VEH_BAL = 0;
		
		
		// SAP COMMENTED
		try {
			
			Date FromDate = Sdate;
			Date ToDate = Edate;
			
			com.sap.conn.jco.JCoTable tab = obj.getDistributorStatement(CID, FromDate, ToDate);
			
				
			tab.firstRow();
			
			int countInserts = 0;
			int countUpdates = 0;
			
			for(int i = 0; i <= tab.getNumRows(); i++){
								
				INC_BAL = tab.getDouble("INC_BAL");
				ATAX_BAL = tab.getDouble("ATAX_BAL");
				CRSAL_BAL = tab.getDouble("CRSAL_BAL");
				VEH_BAL = tab.getDouble("VEH_BAL");
				
				
				tab.setRow(i+1);
				
			}
		obj.dropConnection();
		
	} catch (Exception e) {

		e.printStackTrace();
	}		
		
		
		
		double NetBalanceRec=OpeningBalanceLedger+OpeningBalanceCredit+OpeningBalanceAdvance+VEH_BAL+ATAX_BAL+CRSAL_BAL+(INC_BAL);
		// SAP END
		
		
		long Balance17=0;
		long Balance12=0;
		long Balance11=0;
		
		ResultSet rs11 = s11.executeQuery("SELECT *,(Select sum(total_units_received)-sum(total_units_issued) FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 11 and ect.created_on_date <= "+Utilities.getSQLDate(Edate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id=cd.distributor_id) balance11,(Select sum(total_units_received)-sum(total_units_issued) FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 12 and ect.created_on_date <= "+Utilities.getSQLDate(Edate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id=cd.distributor_id) balance12 ,(Select sum(total_units_received)-sum(total_units_issued)  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 17 and ect.created_on_date <= "+Utilities.getSQLDate(Edate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id=cd.distributor_id) balance17 FROM common_distributors cd where distributor_id="+CID+"");
		while(rs11.next()){
			
			Balance17=rs11.getLong("balance17");
			Balance12=rs11.getLong("balance12")/24;
			Balance11=rs11.getLong("balance11")/24;
			
			
		}
		
		
		double EmptyCrdit17 = 0;
		double EmptyCrdit12 = 0;
		double EmptyCrdit11 = 0;
		
		if(Balance17<0){ //
			EmptyCrdit17 = Balance17*(-1)*440;
		}
		
		if(Balance12<0){ //
			EmptyCrdit12 = Balance12*(-1)*340;
		}
		
		if(Balance11<0){ //
			EmptyCrdit11 = Balance11*(-1)*340;
		}
		
		
		double EmptyBalance17 = 0;
		double EmptyBalance12 = 0;
		double EmptyBalance11 = 0;
		
		if(Balance17>0){ //
			EmptyBalance17 = Balance17*(-1)*180;
		}
		
		if(Balance12>0){ //
			EmptyBalance12 = Balance12*(-1)*170;
		}
		
		if(Balance11>0){ //
			EmptyBalance11 = Balance11*(-1)*170;
		}

		
		// SAP Start
		double SAPVehicle=0;
		double SAPEmpty=0;
		double SAPVehNdEmptyTotal=0;
		
	// SAP COMMENTED
		ResultSet rsSAP = sSAP.executeQuery("select sum(vbrp.KZWI2) vehicle_deduction, sum(vbrp.KZWI3) empty_deduction "+
				" from sapsr3.vbrk vbrk join sapsr3.vbuk vbuk on vbrk.vbeln = vbuk.vbeln join sapsr3.vbrp vbrp on vbrk.vbeln = vbrp.vbeln where vbrk.kurrf_dat between "+Utilities.getSQLDateOracle(Sdate)+" and "+Utilities.getSQLDateOracle(Edate)+" and vbrk.fksto != 'X' and vbrk.fkart in ('ZDIS','ZMRS','ZDFR','ZCLA') /*and vbuk.buchk != 'C'*/ and vbrk.kunag = '"+Utilities.addLeadingZeros(CID+"",10)+"'");
		
		while(rsSAP.next()){
			SAPVehicle = rsSAP.getDouble("vehicle_deduction");
			SAPEmpty = rsSAP.getDouble("empty_deduction");
		}
		
		
		SAPVehicle = SAPVehicle*-1;
		SAPEmpty = SAPEmpty*-1;
		
		SAPVehNdEmptyTotal = SAPVehicle+SAPEmpty;
		// SAP END
		
		
		
		
		
		
		
		
		long Balance1=0;
		long Balance1N=0;
		int UnitPerSKU1=0;
		
		ResultSet rs51 = s51.executeQuery("SELECT distinct ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label ,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3,4) and package_id not in (10) and package_id =1 and ip.is_other_brand !=1  order by package_id desc");
		while(rs51.next()){
		
			 Balance1=0;
			 
			
			long BrandID = rs51.getLong("brand_id");
			
			String BrandIDModify = "";
			
			if(BrandID==1 || BrandID==37 || BrandID==38){ //pepsi
				BrandIDModify += "1,24,28,37,38"; //pepsi+coke+gourm cola
			}else if(BrandID==2){ //merinda 
				BrandIDModify += "2,25"; //mirinda+fanta
			}else if(BrandID==4){ //7up
				BrandIDModify += "4,26,29"; //7up+sprite+gourm lemon
			}else if(BrandID==5){ //Dew
				BrandIDModify += "5,27"; //7up+3g
			}else{
				BrandIDModify = rs51.getLong("brand_id")+"";
			}
			
			
			
			ResultSet rs52 = s52.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 1  and ip.brand_id in ("+BrandIDModify+") and ect.created_on_date <= "+Utilities.getSQLDate(Edate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+CID+" order by ip.package_id desc");
			if(rs52.first()){
				 Balance1 = rs52.getLong(1) - rs52.getLong(2);
				 UnitPerSKU1 = rs52.getInt(3);
				 
				 
			}
			if(UnitPerSKU1!=0){
				Balance1N += Balance1/UnitPerSKU1;
			}
		}
		
		
		
		
		long Balance18=0;
		long Balance18N=0;
		int UnitPerSKU18=0;
		
		ResultSet rs53 = s53.executeQuery("SELECT distinct ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label ,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3,4) and package_id not in (10) and package_id =18 and ip.is_other_brand !=1 "+PACKAGE_ID+" order by package_id desc");
		while(rs53.next()){
		
				 Balance18=0;
				 
				
				long BrandID = rs53.getLong("brand_id");
				
				String BrandIDModify = "";
				
				if(BrandID==1 || BrandID==37 || BrandID==38){ //pepsi
					BrandIDModify += "1,24,28,37,38"; //pepsi+coke+gourm cola
				}else if(BrandID==2){ //merinda 
					BrandIDModify += "2,25"; //mirinda+fanta
				}else if(BrandID==4){ //7up
					BrandIDModify += "4,26,29"; //7up+sprite+gourm lemon
				}else if(BrandID==5){ //Dew
					BrandIDModify += "5,27"; //7up+3g
				}else{
					BrandIDModify = rs53.getLong("brand_id")+"";
				}
			
			
			
			ResultSet rs54 = s54.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 18  and ip.brand_id in ("+BrandIDModify+") and ect.created_on_date <= "+Utilities.getSQLDate(Edate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+CID+" order by ip.package_id desc");
			if(rs54.first()){
				 Balance18 = rs54.getLong(1) - rs54.getLong(2);
				 UnitPerSKU18 = rs54.getInt(3);
				 
				 
			}
			
			if(UnitPerSKU18!=0){
				Balance18N += Balance18/UnitPerSKU18;
			}
		}
		
		
		
		long Balance26=0;
		long Balance26N=0;
		int UnitPerSKU26=0;
		ResultSet rs55 = s55.executeQuery("SELECT distinct ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label ,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3,4) and package_id not in (10) and package_id =26 and ip.is_other_brand !=1 "+PACKAGE_ID+" order by package_id desc");
		while(rs55.next()){
		
				 Balance26=0;
				 
				
				long BrandID = rs55.getLong("brand_id");
				
				String BrandIDModify = "";
				
				if(BrandID==1 || BrandID==37 || BrandID==38){ //pepsi
					BrandIDModify += "1,24,28,37,38"; //pepsi+coke+gourm cola
				}else if(BrandID==2){ //merinda 
					BrandIDModify += "2,25"; //mirinda+fanta
				}else if(BrandID==4){ //7up
					BrandIDModify += "4,26,29"; //7up+sprite+gourm lemon
				}else if(BrandID==5){ //Dew
					BrandIDModify += "5,27"; //7up+3g
				}else{
					BrandIDModify = rs55.getLong("brand_id")+"";
				}
		
		
				
				
				ResultSet rs56= s56.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 26  and ip.brand_id in ("+BrandIDModify+") and  ect.created_on_date <= "+Utilities.getSQLDate(Edate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+CID+" order by ip.package_id desc");
				if(rs56.first()){
					 Balance26 = rs56.getLong(1) - rs56.getLong(2);
					 UnitPerSKU26 = rs56.getInt(3);
					 
					
				}
				
					Balance26N += Balance26;
				
			if(UnitPerSKU26!=0){
				Balance26N=Balance26N/UnitPerSKU26;
			}
		}
		
		
		
		long Balance25=0;
		long Balance25N=0;
		int UnitPerSKU25=0;
		
		ResultSet rs57 = s57.executeQuery("SELECT distinct ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label ,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3,4) and package_id not in (10) and package_id =25 and ip.is_other_brand !=1 "+PACKAGE_ID+" order by package_id desc");
		while(rs57.next()){
		
				 Balance25=0;
				 
				
				long BrandID = rs57.getLong("brand_id");
				
				String BrandIDModify = "";
				
				if(BrandID==1 || BrandID==37 || BrandID==38){ //pepsi
					BrandIDModify += "1,24,28,37,38"; //pepsi+coke+gourm cola
				}else if(BrandID==2){ //merinda 
					BrandIDModify += "2,25"; //mirinda+fanta
				}else if(BrandID==4){ //7up
					BrandIDModify += "4,26,29"; //7up+sprite+gourm lemon
				}else if(BrandID==5){ //Dew
					BrandIDModify += "5,27"; //7up+3g
				}else{
					BrandIDModify = rs57.getLong("brand_id")+"";
				}
					
					//out.println("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 25  and ip.brand_id in ("+BrandIDModify+") and ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+DistributorID1+" order by ip.package_id desc");
					ResultSet rs58 = s58.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 25  and ip.brand_id in ("+BrandIDModify+") and ect.created_on_date <= "+Utilities.getSQLDate(Edate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+CID+" order by ip.package_id desc");
					if(rs58.first()){
						 Balance25 = rs58.getLong(1) - rs58.getLong(2);
						 UnitPerSKU25 = rs58.getInt(3);
						 
						 
					}
					
					if(UnitPerSKU25!=0){
						Balance25N += Balance25/UnitPerSKU25;
					}
		}
		
		
		long Balance13=0;
		long Balance13N=0;
		int UnitPerSKU13=0;
		
		ResultSet rs59 = s59.executeQuery("SELECT distinct ip.brand_id,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label ,ip.package_id,(select label from inventory_packages ip where ip.id=package_id) package_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id where category_id in(2,3,4) and package_id not in (10) and package_id =13 and ip.is_other_brand !=1 "+PACKAGE_ID+" order by package_id desc");
		while(rs59.next()){
		
				 Balance13=0;
				 
				
				long BrandID = rs59.getLong("brand_id");
				
				String BrandIDModify = "";
				
				if(BrandID==1 || BrandID==37 || BrandID==38){ //pepsi
					BrandIDModify += "1,24,28,37,38"; //pepsi+coke+gourm cola
				}else if(BrandID==2){ //merinda 
					BrandIDModify += "2,25"; //mirinda+fanta
				}else if(BrandID==4){ //7up
					BrandIDModify += "4,26,29"; //7up+sprite+gourm lemon
				}else if(BrandID==5){ //Dew
					BrandIDModify += "5,27"; //7up+3g
				}else{
					BrandIDModify = rs59.getLong("brand_id")+"";
				}
		
						
						//out.println("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 13  and ip.brand_id in ("+BrandIDModify+") and ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+DistributorID1+" order by ip.package_id desc");
						ResultSet rs60 = s60.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 13  and ip.brand_id in ("+BrandIDModify+") and ect.created_on_date <= "+Utilities.getSQLDate(Edate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+CID+" order by ip.package_id desc");
						if(rs60.first()){
							 Balance13 = rs60.getLong(1) - rs60.getLong(2);
							 UnitPerSKU13 = rs60.getInt(3);
							 
							 
						}
						if(UnitPerSKU13!=0){
							Balance13N += Balance13/UnitPerSKU13;
						}
		}
						
		
		
		
		double Balance1RT=0;
		double Balance18RT=0;
		double Balance26RT=0;
		double Balance25RT=0;
		double Balance13RT=0;
		
		
		int ShowedBalance1N=0;
		int ShowedBalance18N=0;
		
		
		if(Balance1N<0){
			ShowedBalance1N=340;
			Balance1RT = Balance1N*340*(-1);
		}else{
			ShowedBalance1N=170;
			Balance1RT = Balance1N*170*(-1);
		}
		
		if(Balance18N<0){ //
			ShowedBalance18N=340;
			Balance18RT = Balance18N*340*(-1);
		}else{
			ShowedBalance18N=180;
			Balance18RT = Balance18N*180*(-1);
		}
		
		
		//if(Balance26>0){ //
			Balance26RT = Balance26N*895*(-1);
		//}
		
		
		//if(Balance25>0){ //
			Balance25RT = Balance25N*6700*(-1);
		//}
		
		
		//if(Balance13>0){ //
			Balance13RT = Balance13N*365*(-1);
		//}
		//}
		
		
		
		
		///////Wooden Pallet
		////////////////////////////////////////////////////////
		
		
		long Balance31=0;
		long Balance31N=0;
		int UnitPerSKU31=0;
		
		
		long Balance3145=0;
		long Balance3145N=0;
		int UnitPerSKU3145=0;
		
		
		
				 Balance31=0;
				 
				
				
	//Wooden Pallet (O/W)  package=31 & brand=44
						
						//out.println("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 13  and ip.brand_id in ("+BrandIDModify+") and ect.created_on_date <= "+Utilities.getSQLDate(EndDate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+DistributorID1+" order by ip.package_id desc");
						ResultSet rs61 = s61.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 31  and ip.brand_id in (44) and ect.created_on_date <= "+Utilities.getSQLDate(Edate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+CID+" order by ip.package_id desc");
						if(rs61.first()){
							 Balance31 = rs61.getLong(1) - rs61.getLong(2);
							 UnitPerSKU31 = rs61.getInt(3);
							 
							 
						}
						if(UnitPerSKU31!=0){
							Balance31N += Balance31/UnitPerSKU31;
						}
						
						//for TW
						
						 Balance3145=0;
						
						ResultSet rs62 = s62.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  ip.package_id= 31  and ip.brand_id in (45) and ect.created_on_date <= "+Utilities.getSQLDate(Edate)+" and ect.type_id in(1,5,2,8,9,10,11,12,13,14,15) and ect.distributor_id="+CID+" order by ip.package_id desc");
						if(rs62.first()){
							 Balance3145 = rs62.getLong(1) - rs62.getLong(2);
							 UnitPerSKU3145 = rs62.getInt(3);
							 
							 
						}
						if(UnitPerSKU3145!=0){
							Balance3145N += Balance3145/UnitPerSKU3145;
						}
		
						
		
		
		
						double Balance31RT=0;
						double Balance3145RT=0;
						int Rate31RT=2100;
						int Rate3145RT=2200;
						
						
						//if(Balance1>0){ //
							Balance31RT = Balance31N*Rate31RT*(-1);
						//}
						
						//if(Balance18>0){ //
							Balance3145RT = Balance3145N*Rate3145RT*(-1);
						//}

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		long cases=0;
		String pack_name="";
		ResultSet rs12=s12.executeQuery(" SELECT (select label from inventory_packages ip where ip.id=tela.package_id) package_name,tela.cases FROM pep.temp_empty_leakage_allowance tela where 1=1 and distributor_id ="+CID +" and date between "+Utilities.getSQLDate(Sdate)+" and "+Utilities.getSQLDateNext(Edate));

		
//		double EmptyBalance=EmptyCrdit17+EmptyCrdit12+EmptyCrdit11+EmptyBalance17+EmptyBalance12+EmptyBalance11;
		double EmptyBalance=EmptyCrdit17+EmptyCrdit12+EmptyCrdit11+EmptyBalance17+EmptyBalance12+EmptyBalance11+Balance1RT+Balance18RT+Balance26RT+Balance25RT+Balance13RT+Balance31RT+Balance3145RT;
		
		
		
		long LimitShell = 0;
		ResultSet rs13 = s13.executeQuery("SELECT ecl.id,ecl.distributor_id,(select name from common_distributors cd where cd.distributor_id=ecl.distributor_id) dist_name,ecl.credit_type,(select label from  ec_empty_credit_types ect where ect.id=ecl.credit_type) credit_type,ecl.reason,ecl.start_date,ecl.end_date,sum(raw_cases) limitt FROM ec_empty_credit_limit ecl join ec_empty_credit_limit_products eclp on ecl.id=eclp.id  where ecl.is_active=1 and curdate() between ecl.start_date and ecl.end_date and distributor_id="+CID+" and package_id=17");
		if(rs13.first()){
			LimitShell = rs13.getLong("limitt");
		}
		
	
		long Limit240 = 0;
		ResultSet rs14 = s14.executeQuery("SELECT ecl.id,ecl.distributor_id,(select name from common_distributors cd where cd.distributor_id=ecl.distributor_id) dist_name,ecl.credit_type,(select label from  ec_empty_credit_types ect where ect.id=ecl.credit_type) credit_type,ecl.reason,ecl.start_date,ecl.end_date,sum(raw_cases) limitt FROM ec_empty_credit_limit ecl join ec_empty_credit_limit_products eclp on ecl.id=eclp.id  where ecl.is_active=1 and curdate() between ecl.start_date and ecl.end_date and distributor_id="+CID+" and package_id=12");
		if(rs14.first()){
			Limit240 = rs14.getLong("limitt");
		}
		
		
		long Limit250 = 0;
		ResultSet rs15 = s15.executeQuery("SELECT ecl.id,ecl.distributor_id,(select name from common_distributors cd where cd.distributor_id=ecl.distributor_id) dist_name,ecl.credit_type,(select label from  ec_empty_credit_types ect where ect.id=ecl.credit_type) credit_type,ecl.reason,ecl.start_date,ecl.end_date,sum(raw_cases) limitt FROM ec_empty_credit_limit ecl join ec_empty_credit_limit_products eclp on ecl.id=eclp.id  where ecl.is_active=1 and curdate() between ecl.start_date and ecl.end_date and distributor_id="+CID+" and package_id=11");
		if(rs15.first()){
			Limit250 = rs15.getLong("limitt");
		}
		
		//Available limit
		
		long Overlimit17 = Balance17 + LimitShell;
		
		long Overlimit12 = Balance12 + Limit240;
		
		long Overlimit11 = Balance11 + Limit250;
		
		

	    Paragraph DisclaimerHeading= new Paragraph("Disclaimer", FontDisclaimerHeading);						        
		List Disclaimer = new List(List.ORDERED);
		Disclaimer.add(new ListItem("Ledger balance has been adjusted for the last month incentive.",FontDisclaimnr));
		Disclaimer.add(new ListItem("Vehicle account does not cover temporary vehicles issued. If applicable, depreciation/demiurges against temporary vehicles will be charged according to policy.",FontDisclaimnr));
        
        

//		Distributor
		Paragraph header = new Paragraph(Distributor,fontheading);
//		Paragraph header = new Paragraph("Period: "+Utilities.getDisplayDateFormat(Sdate)+" - "+Utilities.getDisplayDateFormat(Edate),fontheading);
		header.setAlignment(Element.ALIGN_LEFT);

Image img = Image.getInstance(String.format(Utilities.getImageResoucesPath()+"/"+"%s", "pepsi28.png"));
img.scaleToFit(20, 20);
//img.setTransparency(new int[]{ 0xF0, 0xFF });
img.setAbsolutePosition(35, 487);

Image img2 = Image.getInstance(String.format(Utilities.getImageResoucesPath()+"/"+"%s", "mf.jpg"));
img2.scaleToFit(90, 39);
//img.setTransparency(new int[]{ 0xF0, 0xFF });
img2.setAbsolutePosition(718, 469);














Font pg= FontFactory.getFont("Arial",6);
Paragraph pbc = new Paragraph("          Punjab Beverages",fontpbc);
Paragraph Heading = new Paragraph("Distributor Statement",fontpbc);
Heading.setAlignment(Element.ALIGN_CENTER);
Paragraph PageNum= new Paragraph("Page 1 of 2");
PageNum.setAlignment(Element.ALIGN_RIGHT);

Paragraph PageNum2= new Paragraph("Page 2 of 2");
PageNum2.setAlignment(Element.ALIGN_RIGHT);

		





document.add(PageNum);        
document.add(Chunk.NEWLINE);	    
document.add(Chunk.NEWLINE);	    
document.add(img);
document.add(img2);
document.add(pbc);        
document.add(Heading);
document.add(header);
document.add(Chunk.NEWLINE);	    







		
		
		
		
		PdfPTable table = new PdfPTable(4);
	    
	    table.setWidthPercentage(100f);
	    table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
	    
	    reportheading.setColor(BaseColor.WHITE);
	    fonttitle.setColor(BaseColor.WHITE);

		
		
		PdfPCell pcell = new PdfPCell(new Phrase("Summary", fonttitle));
        pcell.setBackgroundColor(new BaseColor(64,64,64));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.BLACK);
        pcell.setColspan(4);
        pcell.setMinimumHeight(22);
        table.addCell(pcell);

        
//        PdfPCell DistributorMain=new PdfPCell(new Phrase("Distributor",fonttitle)) ;
//        PdfPCell DistributorMain=new PdfPCell(getHeadingCell("Distributor"));
 //       DistributorMain.setBackgroundColor(new BaseColor(122,122,122));
 //       table.addCell(DistributorMain);
        
        PdfPCell ContactPerson=new PdfPCell(new Phrase("Duration",fonttitle));
        ContactPerson.setBackgroundColor(new BaseColor(122,122,122));
        table.addCell(ContactPerson);

        PdfPCell FirstLiftDate=new PdfPCell(new Phrase("First Lifting Date",fonttitle));
        FirstLiftDate.setBackgroundColor(new BaseColor(122,122,122));
        table.addCell(FirstLiftDate);
        
        PdfPCell LastLiftDate=new PdfPCell(new Phrase("Last Lifting Date",fonttitle));
        LastLiftDate.setBackgroundColor(new BaseColor(122,122,122));
        table.addCell(LastLiftDate);
        

        PdfPCell NoOfOutlets=new PdfPCell(new Phrase("Number of Outlets",fonttitle));
        NoOfOutlets.setBackgroundColor(new BaseColor(122,122,122));
        table.addCell(NoOfOutlets);
                                    
            	            		
	//		        table.addCell(getNormalCell(Distributor));
			        table.addCell(getNormalCell(Utilities.getDisplayDateFormat(Sdate)+"-"+Utilities.getDisplayDateFormat(Edate)));
			        table.addCell(getNormalCell(Utilities.getDisplayDateFormat(FirstLifting)));
			        table.addCell(getNormalCell(Utilities.getDisplayDateFormat(LastLifting)));
			  //      table.addCell(getNormalCell(DistName+"-"+DistContact));
			        table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormat(TotalOutlets)));		        
        

			        document.add(table);
			        document.add(Chunk.NEWLINE);

			        
			        
			        PdfPTable table2 = new PdfPTable(5);
				    
				    table2.setWidthPercentage(100f);
				    table2.getDefaultCell().setBorderColor(new BaseColor(122,122,122));
				    
				    reportheading.setColor(BaseColor.WHITE);
				    fonttitle.setColor(BaseColor.WHITE);
		
				    
			 				    
			        PdfPCell Fin=new PdfPCell(new Phrase("Financials",fonttitle));
				    Fin.setColspan(4);
				    Fin.setBackgroundColor(new BaseColor(122,122,122));
			        table2.addCell(Fin);

			        PdfPCell Amount=new PdfPCell(new Phrase("Amount",fonttitle));
				    Amount.setColspan(1);
				    Amount.setHorizontalAlignment(Element.ALIGN_CENTER);
				    Amount.setBackgroundColor(new BaseColor(122,122,122));
			        table2.addCell(Amount);
				    
			        PdfPCell AC_Rec=new PdfPCell(getHeadingCell("Accounts Receivable"));
			        AC_Rec.setColspan(5);
			        AC_Rec.setBackgroundColor(new BaseColor(193,193,193));
			        table2.addCell(AC_Rec);

			        
			        PdfPCell Ledger_Bal=new PdfPCell(getNormalCell("Ledger Balance"));
			        Ledger_Bal.setColspan(4);
			        table2.addCell(Ledger_Bal);

			        
			        PdfPCell Ledger_Bal_Val=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(OpeningBalanceLedger)));
			        Ledger_Bal_Val.setColspan(1);
			        Ledger_Bal_Val.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table2.addCell(Ledger_Bal_Val);
			        

			        PdfPCell Credit_Bal=new PdfPCell(getNormalCell("Credit Balance"));
			        Credit_Bal.setColspan(4);
			        table2.addCell(Credit_Bal);
				    
			        PdfPCell Credit_Bal_Val=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(OpeningBalanceCredit)));
			        Credit_Bal_Val.setColspan(1);
			        Credit_Bal_Val.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table2.addCell(Credit_Bal_Val);

			        PdfPCell Advance_Bal=new PdfPCell(getNormalCell("Advance Balance"));
			        Advance_Bal.setColspan(4);
			        table2.addCell(Advance_Bal);
				    
			        PdfPCell Advance_Val=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(OpeningBalanceAdvance)));
			        Advance_Val.setColspan(1);
			        Advance_Val.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table2.addCell(Advance_Val);
			        
			        
			        /// SAP Start
			        
			        PdfPCell Vehicle_Bal=new PdfPCell(getNormalCell("Vehicle Account"));
			        Vehicle_Bal.setColspan(4);
			        table2.addCell(Vehicle_Bal);
				    
			        PdfPCell Vehicle_Val=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(VEH_BAL)));
			        Vehicle_Val.setColspan(1);
			        Vehicle_Val.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table2.addCell(Vehicle_Val);
			        
			        PdfPCell Ac_Tax_Bal=new PdfPCell(getNormalCell("Account Tax Balance"));
			        Ac_Tax_Bal.setColspan(4);
			        table2.addCell(Ac_Tax_Bal);
				    
			        PdfPCell Ac_Tax_Val=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(ATAX_BAL)));
			        Ac_Tax_Val.setColspan(1);
			        Ac_Tax_Val.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table2.addCell(Ac_Tax_Val);
			        
			        PdfPCell PSC_Sal_Ac=new PdfPCell(getNormalCell("PSR Salary Account"));
			        PSC_Sal_Ac.setColspan(4);
			        
			        table2.addCell(PSC_Sal_Ac);
				    
			        PdfPCell PSC_Sal_Ac_Val=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(CRSAL_BAL)));
			        PSC_Sal_Ac_Val.setColspan(1);
			        PSC_Sal_Ac_Val.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table2.addCell(PSC_Sal_Ac_Val);
			        
			        PdfPCell Inc_Ac=new PdfPCell(getNormalCell("Incentive Account"));
			        Inc_Ac.setColspan(4);
			        table2.addCell(Inc_Ac);
				    
			        PdfPCell Inc_Ac_Val=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(INC_BAL)));
			        Inc_Ac_Val.setColspan(1);
			        Inc_Ac_Val.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table2.addCell(Inc_Ac_Val);
					
			      
			        PdfPCell Net_Bal=new PdfPCell(getNormalCell("Net Balance"));
			        Net_Bal.setColspan(4);
			        Net_Bal.setBackgroundColor(new BaseColor(238,239,223));
			        table2.addCell(Net_Bal);
				    
			        PdfPCell Net_Bal_Val=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(NetBalanceRec)));
			        Net_Bal_Val.setColspan(1);
			        Net_Bal_Val.setBackgroundColor(new BaseColor(238,239,223));
			        
			        Net_Bal_Val.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table2.addCell(Net_Bal_Val);
			        
			        
			        
			        PdfPCell Deduc=new PdfPCell(getHeadingCell("Deductions and Margins"));
			        Deduc.setColspan(5);
			        Deduc.setBackgroundColor(new BaseColor(193,193,193));
			        table2.addCell(Deduc);
			        
			        
			        
			        PdfPCell VehicleSap=new PdfPCell(getNormalCell("Vehicle"));
			        VehicleSap.setColspan(4);
			        table2.addCell(VehicleSap);
				    
			        PdfPCell VehicleSap_Val=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(SAPVehicle)));
			        VehicleSap_Val.setColspan(1);
			        
			        VehicleSap_Val.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table2.addCell(VehicleSap_Val);
			     
			        PdfPCell EmptySap=new PdfPCell(getNormalCell("Empty"));
			        EmptySap.setColspan(4);
			        table2.addCell(EmptySap);
				    
			        PdfPCell EmptySap_Val=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(SAPEmpty)));
			        EmptySap_Val.setColspan(1);
			        
			        EmptySap_Val.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table2.addCell(EmptySap_Val);
			     
			        PdfPCell TotalSAP=new PdfPCell(getNormalCell("Total"));
			        TotalSAP.setColspan(4);
			        TotalSAP.setBackgroundColor(new BaseColor(238,239,223));
			        table2.addCell(TotalSAP);
				    
			        PdfPCell TotalSAP_Val=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(SAPVehNdEmptyTotal)));
			        TotalSAP_Val.setColspan(1);
			        
			        TotalSAP_Val.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        TotalSAP_Val.setBackgroundColor(new BaseColor(238,239,223));
			        table2.addCell(TotalSAP_Val);
			        // SAP END
//			        PdfPCell TotalSAPSpace=new PdfPCell(getNormalCell(" "));
//			        TotalSAPSpace.setColspan(5);
//			        table2.addCell(TotalSAPSpace);
			        
			        
			        document.add(table2);
			        
			        
			        PdfPTable table5 = new PdfPTable(5);
				    
				    table5.setWidthPercentage(100f);
				    table5.getDefaultCell().setBorderColor(new BaseColor(122,122,122));
				    
				    reportheading.setColor(BaseColor.WHITE);
				    fonttitle.setColor(BaseColor.WHITE);

			        
			        
			        
			        PdfPCell Empty=new PdfPCell(getHeadingCell("Empty"));
			        Empty.setColspan(2);
			        Empty.setBackgroundColor(new BaseColor(193,193,193));
			        table5.addCell(Empty);

			        PdfPCell Quantity=new PdfPCell(getHeadingCell("Quantity"));
			        Quantity.setColspan(1);
			        Quantity.setHorizontalAlignment(Element.ALIGN_CENTER);
			        Quantity.setBackgroundColor(new BaseColor(193,193,193));
			        table5.addCell(Quantity);
			        
			        PdfPCell Rate=new PdfPCell(getHeadingCell("Rate"));
			        Rate.setColspan(1);
			        Rate.setHorizontalAlignment(Element.ALIGN_CENTER);
			        Rate.setBackgroundColor(new BaseColor(193,193,193));
			        table5.addCell(Rate);
			        
			        PdfPCell Total=new PdfPCell(getHeadingCell("Total"));
			        Total.setColspan(1);
			        Total.setHorizontalAlignment(Element.ALIGN_CENTER);
			        Total.setBackgroundColor(new BaseColor(193,193,193));
			        table5.addCell(Total);
			        
			        PdfPCell Empty_Credit=new PdfPCell(getHeadingCell("Empty Credit"));
			        Empty_Credit.setColspan(5);
			        table5.addCell(Empty_Credit);			        
			        
			        
			        
			        PdfPCell _250_ML_Shell=new PdfPCell(getNormalCell("250 ML SHELL"));
			        _250_ML_Shell.setColspan(2);
			        table5.addCell(_250_ML_Shell);
				    

			      
			        PdfPCell _250_ML_Shell_Quantity=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance17*-1)));
			        _250_ML_Shell_Quantity.setColspan(1);
			        _250_ML_Shell_Quantity.setHorizontalAlignment(Element.ALIGN_RIGHT);
					
			        table5.addCell(_250_ML_Shell_Quantity);
			        
			        PdfPCell _250_ML_Shell_Rate=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(440)));
			        _250_ML_Shell_Rate.setColspan(1);
			        _250_ML_Shell_Rate.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table5.addCell(_250_ML_Shell_Rate);

			       
			       

			        
			        PdfPCell _250_ML_Shell_Total=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(EmptyCrdit17)));
			        _250_ML_Shell_Total.setColspan(1);
			        _250_ML_Shell_Total.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table5.addCell(_250_ML_Shell_Total);
			        

			        
			        PdfPCell _240_ML_SSRB=new PdfPCell(getNormalCell("240 ML SSRB"));
			        _240_ML_SSRB.setColspan(2);
			        table5.addCell(_240_ML_SSRB);
				    

			        if(EmptyCrdit12!=0) 
			        {
			        PdfPCell _240_ML_SSRB_Quantity=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance12*-1)));
			        _240_ML_SSRB_Quantity.setColspan(1);
			        _240_ML_SSRB_Quantity.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table5.addCell(_240_ML_SSRB_Quantity);
			        
			        PdfPCell _240_ML_SSRB_Rate=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(340)));
			        _240_ML_SSRB_Rate.setColspan(1);
			        _240_ML_SSRB_Rate.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table5.addCell(_240_ML_SSRB_Rate);

			        } 
			        else 
			        {

			        	PdfPCell _240_ML_SSRB_Quantity=new PdfPCell(getNormalCell(""));
			        	_240_ML_SSRB_Quantity.setColspan(1);
			        table5.addCell(_240_ML_SSRB_Quantity);

			        PdfPCell _240_ML_SSRB_Rate=new PdfPCell(getNormalCell(""));
			        _240_ML_SSRB_Rate.setColspan(1);
			        table5.addCell(_240_ML_SSRB_Rate);

			        }
			      			        
			        PdfPCell _240_ML_SSRB_Total=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(EmptyCrdit12)));
			        _240_ML_SSRB_Total.setColspan(1);
			        _240_ML_SSRB_Total.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table5.addCell(_240_ML_SSRB_Total);
			      
			        
			        

			        PdfPCell _250_ML_STD=new PdfPCell(getNormalCell("250 ML STD"));
			        _250_ML_STD.setColspan(2);
			        table5.addCell(_250_ML_STD);
				    

			        if(EmptyCrdit11!=0) 
			        {
			        PdfPCell _250_ML_STD_Quantity=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance11*-1)));
			        _250_ML_STD_Quantity.setColspan(1);
			        _250_ML_STD_Quantity.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table5.addCell(_250_ML_STD_Quantity);
			        
			        PdfPCell _250_ML_STD_Rate=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(340)));
			        _250_ML_STD_Rate.setColspan(1);
			        _250_ML_STD_Rate.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        
			        table5.addCell(_250_ML_STD_Rate);

			        } 
			        else 
			        {

			        	PdfPCell _250_ML_STD_Quantity=new PdfPCell(getNormalCell(""));
			        	_250_ML_STD_Quantity.setColspan(1);
			        table5.addCell(_250_ML_STD_Quantity);

			        PdfPCell _250_ML_STD_Rate=new PdfPCell(getNormalCell(""));
			        _250_ML_STD_Rate.setColspan(1);
			        table5.addCell(_250_ML_STD_Rate);

			        }
			      			        
			        PdfPCell _250_ML_STD_Total=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(EmptyCrdit11)));
			        _250_ML_STD_Total.setColspan(1);
			        _250_ML_STD_Total.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table5.addCell(_250_ML_STD_Total);
			      
			        			        
			        
			        
			        
			        
			        PdfPCell _1000_ML_Glass=new PdfPCell(getNormalCell("1000ML Glass"));
			        _1000_ML_Glass.setColspan(2);
			        table5.addCell(_1000_ML_Glass);
				    

			        if(Balance1RT!=0) 
			        {
			        PdfPCell _1000_ML_Glass_Quantity=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance1N*-1)));
			        _1000_ML_Glass_Quantity.setColspan(1);
			        _1000_ML_Glass_Quantity.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table5.addCell(_1000_ML_Glass_Quantity);
			        
			        PdfPCell _1000_ML_Glass_Rate=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(ShowedBalance1N)));
			        _1000_ML_Glass_Rate.setColspan(1);
			        _1000_ML_Glass_Rate.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table5.addCell(_1000_ML_Glass_Rate);

			        } 
			        else 
			        {

			        	PdfPCell _1000_ML_Glass_Quantity=new PdfPCell(getNormalCell(""));
			        	_1000_ML_Glass_Quantity.setColspan(1);
			        table5.addCell(_1000_ML_Glass_Quantity);

			        PdfPCell _1000_ML_Glass_Rate=new PdfPCell(getNormalCell(""));
			        _1000_ML_Glass_Rate.setColspan(1);
			        table5.addCell(_1000_ML_Glass_Rate);

			        }
			      			        
			        PdfPCell _1000_ML_Glass_Rate_Total=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance1RT)));
			        _1000_ML_Glass_Rate_Total.setColspan(1);
			        _1000_ML_Glass_Rate_Total.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table5.addCell(_1000_ML_Glass_Rate_Total);
			        
			        
			        
			        
			        PdfPCell _1000_ML_Shell=new PdfPCell(getNormalCell("1000ML Shell"));
			        _1000_ML_Shell.setColspan(2);
			        table5.addCell(_1000_ML_Shell);
				    

			        if(Balance18RT!=0) 
			        {
			        PdfPCell _1000_ML_Shell_Quantity=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance18N*-1)));
			        _1000_ML_Shell_Quantity.setColspan(1);
			        _1000_ML_Shell_Quantity.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table5.addCell(_1000_ML_Shell_Quantity);
			        
			        PdfPCell _1000_ML_Shell_Rate=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(ShowedBalance18N)));
			        _1000_ML_Shell_Rate.setColspan(1);
			        _1000_ML_Shell_Rate.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table5.addCell(_1000_ML_Shell_Rate);

			        } 
			        else 
			        {

			        	PdfPCell _1000_ML_Shell_Quantity=new PdfPCell(getNormalCell(""));
			        	_1000_ML_Shell_Quantity.setColspan(1);
			        table5.addCell(_1000_ML_Shell_Quantity);

			        PdfPCell _1000_ML_Shell_Rate=new PdfPCell(getNormalCell(""));
			        _1000_ML_Shell_Rate.setColspan(1);
			        table5.addCell(_1000_ML_Shell_Rate);

			        }
			      			        
			        PdfPCell _1000_ML_Shell_Rate_Total=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance18RT)));
			        _1000_ML_Shell_Rate_Total.setColspan(1);
			        _1000_ML_Shell_Rate_Total.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table5.addCell(_1000_ML_Shell_Rate_Total);
			        
			        
			        
			        
			        PdfPCell Plastic_Sheet=new PdfPCell(getNormalCell("Plastic Sheet"));
			        Plastic_Sheet.setColspan(2);
			        table5.addCell(Plastic_Sheet);
				    

			        if(Balance26RT!=0) 
			        {
			        PdfPCell Plastic_Sheet_Quantity=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance26N*-1)));
			        Plastic_Sheet_Quantity.setColspan(1);
			        Plastic_Sheet_Quantity.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table5.addCell(Plastic_Sheet_Quantity);
			        
			        PdfPCell Plastic_Sheet_Rate=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(895)));
			        Plastic_Sheet_Rate.setColspan(1);
			        Plastic_Sheet_Rate.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table5.addCell(Plastic_Sheet_Rate);

			        } 
			        else 
			        {

			        	PdfPCell Plastic_Sheet_Quantity=new PdfPCell(getNormalCell(""));
			        	Plastic_Sheet_Quantity.setColspan(1);
			        table5.addCell(Plastic_Sheet_Quantity);

			        PdfPCell Plastic_Sheet_Rate=new PdfPCell(getNormalCell(""));
			        Plastic_Sheet_Rate.setColspan(1);

			        table5.addCell(Plastic_Sheet_Rate);

			        }
			      			        
			        PdfPCell Plastic_Sheet_Total=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance26RT)));
			        Plastic_Sheet_Total.setColspan(1);
			        Plastic_Sheet_Total.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table5.addCell(Plastic_Sheet_Total);			        
			        

			        
			        PdfPCell Plastic_Pallets=new PdfPCell(getNormalCell("Plastic Pallets"));
			        Plastic_Pallets.setColspan(2);
			        table5.addCell(Plastic_Pallets);
				    

			        if(Balance25RT!=0) 
			        {
			        PdfPCell Plastic_Pallets_Quantity=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance25N*-1)));
			        Plastic_Pallets_Quantity.setColspan(1);
			        Plastic_Pallets_Quantity.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table5.addCell(Plastic_Pallets_Quantity);
			        
			        PdfPCell Plastic_Pallets_Rate=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(6700)));
			        Plastic_Pallets_Rate.setColspan(1);
			        Plastic_Pallets_Rate.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table5.addCell(Plastic_Pallets_Rate);

			        } 
			        else 
			        {

			        	PdfPCell Plastic_Pallets_Quantity=new PdfPCell(getNormalCell(""));
			        	Plastic_Pallets_Quantity.setColspan(1);
			        table5.addCell(Plastic_Pallets_Quantity);

			        PdfPCell Plastic_Pallets_Rate=new PdfPCell(getNormalCell(""));
			        Plastic_Pallets_Rate.setColspan(1);

			        table5.addCell(Plastic_Pallets_Rate);

			        }
			      			        
			        PdfPCell Plastic_Pallets_Total=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance25RT)));
			        Plastic_Pallets_Total.setColspan(1);
			        Plastic_Pallets_Total.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table5.addCell(Plastic_Pallets_Total);			        

			        document.add(table5);

			        document.newPage();
			        document.add(PageNum2);        
				    document.add(Chunk.NEWLINE);	    
				    document.add(Chunk.NEWLINE);	    
			        document.add(img);
			        document.add(img2);
			        document.add(pbc);        
			        document.add(Heading);
				    document.add(header);
				    document.add(Chunk.NEWLINE);	    

			        
			        PdfPTable table6 = new PdfPTable(5);
				    
				    table6.setWidthPercentage(100f);
				    table6.getDefaultCell().setBorderColor(new BaseColor(122,122,122));
				    
				    reportheading.setColor(BaseColor.WHITE);
				    fonttitle.setColor(BaseColor.WHITE);

			        
			        

			        PdfPCell Bulk_Empty=new PdfPCell(getNormalCell("Bulk Empty"));
			        Bulk_Empty.setColspan(2);
			        table6.addCell(Bulk_Empty);
				    

			        if(Balance13RT!=0) 
			        {
			        PdfPCell Bulk_Empty_Quantity=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance13N*-1)));
			        Bulk_Empty_Quantity.setColspan(1);
			        Bulk_Empty_Quantity.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table6.addCell(Bulk_Empty_Quantity);
			        
			        PdfPCell Bulk_Empty_Rate=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(365)));
			        Bulk_Empty_Rate.setColspan(1);
			        Bulk_Empty_Rate.setHorizontalAlignment(Element.ALIGN_RIGHT);
			       

			        table6.addCell(Bulk_Empty_Rate);

			        } 
			        else 
			        {

			        	PdfPCell Bulk_Empty_Quantity=new PdfPCell(getNormalCell(""));
			        	Bulk_Empty_Quantity.setColspan(1);
			        table6.addCell(Bulk_Empty_Quantity);

			        PdfPCell Bulk_Empty_Rate=new PdfPCell(getNormalCell(""));
			        Bulk_Empty_Rate.setColspan(1);
			        table6.addCell(Bulk_Empty_Rate);

			        }
			      			        
			        PdfPCell Bulk_Empty_Total=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance13RT)));
			        Bulk_Empty_Total.setColspan(1);
			        Bulk_Empty_Total.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table6.addCell(Bulk_Empty_Total);			        


			        
			        		        

			        

			        PdfPCell WPOW=new PdfPCell(getNormalCell("Wooden Pallet (O/W)"));
			        WPOW.setColspan(2);
			        table6.addCell(WPOW);
				    

			        if(Balance31RT!=0) 
			        {
			        PdfPCell WPOW_Quantity=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance31N*-1)));
			        WPOW_Quantity.setColspan(1);
			        WPOW_Quantity.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table6.addCell(WPOW_Quantity);
			        
			        PdfPCell WPOW_Rate=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Rate31RT)));
			        WPOW_Rate.setColspan(1);
			        WPOW_Rate.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table6.addCell(WPOW_Rate);

			        } 
			        else 
			        {

			        	PdfPCell WPOW_Quantity=new PdfPCell(getNormalCell(""));
			        	WPOW_Quantity.setColspan(1);
			        table6.addCell(WPOW_Quantity);

			        PdfPCell WPOW_Rate=new PdfPCell(getNormalCell(""));
			        WPOW_Rate.setColspan(1);
			        table6.addCell(WPOW_Rate);

			        }
			      			        
			        PdfPCell WPOW_Total=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance31RT)));
			        WPOW_Total.setColspan(1);
			        WPOW_Total.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table6.addCell(WPOW_Total);			        

			        
			        PdfPCell WPTW=new PdfPCell(getNormalCell("Wooden Pallet (T/W)"));
			        WPTW.setColspan(2);
			        table6.addCell(WPTW);
				    

			        if(Balance3145RT!=0) 
			        {
			        PdfPCell WPTW_Quantity=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance3145N*-1)));
			        WPTW_Quantity.setColspan(1);
			        WPTW_Quantity.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table6.addCell(WPTW_Quantity);
			        
			        PdfPCell WPTW_Rate=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Rate3145RT)));
			        WPTW_Rate.setColspan(1);
			        WPTW_Rate.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table6.addCell(WPTW_Rate);

			        } 
			        else 
			        {

			        	PdfPCell WPTW_Quantity=new PdfPCell(getNormalCell(""));
			        	WPTW_Quantity.setColspan(1);
			        table6.addCell(WPTW_Quantity);

			        PdfPCell WPTW_Rate=new PdfPCell(getNormalCell(""));
			        WPTW_Rate.setColspan(1);
			        table6.addCell(WPTW_Rate);

			        }
			      			        
			        PdfPCell WPTW_Total=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance3145RT)));
			        WPOW_Total.setColspan(1);
			        WPTW_Total.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table6.addCell(WPTW_Total);			        
			        
			        
			        
			        
			        
			        PdfPCell Empty_Balance=new PdfPCell(getHeadingCell("Empty Balance"));
			        Empty_Balance.setColspan(5);
			        table6.addCell(Empty_Balance);			        
			        
			        
			        
			        PdfPCell _250_ML_Shell_Bal=new PdfPCell(getNormalCell("250 ML SHELL"));
			        _250_ML_Shell_Bal.setColspan(2);
			        table6.addCell(_250_ML_Shell_Bal);
				    

			        if(EmptyBalance17!=0) 
			        {
			        PdfPCell _250_ML_Shell_Quantity_Bal=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance17*-1)));
			        _250_ML_Shell_Quantity_Bal.setColspan(1);
			        _250_ML_Shell_Quantity_Bal.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table6.addCell(_250_ML_Shell_Quantity_Bal);
			        
			        PdfPCell _250_ML_Shell_Rate_Bal=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(180)));
			        _250_ML_Shell_Rate_Bal.setColspan(1);
			        _250_ML_Shell_Rate_Bal.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table6.addCell(_250_ML_Shell_Rate_Bal);

			        } 
			        else 
			        {

			        	PdfPCell _250_ML_Shell_Quantity_Bal=new PdfPCell(getNormalCell(""));
			        _250_ML_Shell_Quantity_Bal.setColspan(1);
			        table6.addCell(_250_ML_Shell_Quantity_Bal);

			        PdfPCell _250_ML_Shell_Rate_Bal=new PdfPCell(getNormalCell(""));
			        _250_ML_Shell_Rate_Bal.setColspan(1);
			        table6.addCell(_250_ML_Shell_Rate_Bal);

			        }
			      			        
			        PdfPCell _250_ML_Shell_Total_Bal=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(EmptyBalance17)));
			        _250_ML_Shell_Total_Bal.setColspan(1);
			        _250_ML_Shell_Total_Bal.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table6.addCell(_250_ML_Shell_Total_Bal);
			        

			        
			        PdfPCell _240_ML_SSRB_Bal=new PdfPCell(getNormalCell("240 ML SSRB"));
			        _240_ML_SSRB_Bal.setColspan(2);
			        table6.addCell(_240_ML_SSRB_Bal);
				    

			        if(EmptyBalance12!=0) 
			        {
			        PdfPCell _240_ML_SSRB_Quantity_Bal=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance12*-1)));
			        _240_ML_SSRB_Quantity_Bal.setColspan(1);
			        _240_ML_SSRB_Quantity_Bal.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table6.addCell(_240_ML_SSRB_Quantity_Bal);
			        
			        PdfPCell _240_ML_SSRB_Rate_Bal=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(170)));
			        _240_ML_SSRB_Rate_Bal.setColspan(1);
			        _240_ML_SSRB_Rate_Bal.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table6.addCell(_240_ML_SSRB_Rate_Bal);

			        } 
			        else 
			        {

			        	PdfPCell _240_ML_SSRB_Quantity_Bal=new PdfPCell(getNormalCell(""));
			        	_240_ML_SSRB_Quantity_Bal.setColspan(1);
			        table6.addCell(_240_ML_SSRB_Quantity_Bal);

			        PdfPCell _240_ML_SSRB_Rate_Bal=new PdfPCell(getNormalCell(""));
			        _240_ML_SSRB_Rate_Bal.setColspan(1);
			        table6.addCell(_240_ML_SSRB_Rate_Bal);

			        }
			      			        
			        PdfPCell _240_ML_SSRB_Total_Bal=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(EmptyBalance12)));
			        _240_ML_SSRB_Total_Bal.setColspan(1);
			        _240_ML_SSRB_Total_Bal.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table6.addCell(_240_ML_SSRB_Total_Bal);
			      
			        
			        

			        PdfPCell _250_ML_STD_Bal=new PdfPCell(getNormalCell("250 ML STD"));
			        _250_ML_STD_Bal.setColspan(2);
			        table6.addCell(_250_ML_STD_Bal);
				    

			        if(EmptyBalance11!=0) 
			        {
			        PdfPCell _250_ML_STD_Quantity_Bal=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance11*-1)));
			        _250_ML_STD_Quantity_Bal.setColspan(1);
			        _250_ML_STD_Quantity_Bal.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        table6.addCell(_250_ML_STD_Quantity_Bal);
			        
			        PdfPCell _250_ML_STD_Rate_Bal=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(170)));
			        _250_ML_STD_Rate_Bal.setColspan(1);
			        _250_ML_STD_Rate_Bal.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table6.addCell(_250_ML_STD_Rate_Bal);

			        } 
			        else 
			        {

			        	PdfPCell _250_ML_STD_Quantity_Bal=new PdfPCell(getNormalCell(""));
			        	_250_ML_STD_Quantity_Bal.setColspan(1);
			        table6.addCell(_250_ML_STD_Quantity_Bal);

			        PdfPCell _250_ML_STD_Rate_Bal=new PdfPCell(getNormalCell(""));
			        _250_ML_STD_Rate_Bal.setColspan(1);
			        table6.addCell(_250_ML_STD_Rate_Bal);

			        }
			      			        
			        PdfPCell _250_ML_STD_Total_Bal=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(EmptyBalance11)));
			        _250_ML_STD_Total_Bal.setColspan(1);
			        _250_ML_STD_Total_Bal.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table6.addCell(_250_ML_STD_Total_Bal);
			      
			        
			        PdfPCell Leakage_allowance=new PdfPCell(getHeadingCell("Leakage Allowance"));
			        Leakage_allowance.setColspan(5);
			        table6.addCell(Leakage_allowance);
			        
			        while(rs12.next())
					{
						cases=rs12.getLong("tela.cases");
						pack_name=rs12.getString("package_name");
						
						 PdfPCell Leakage_Pack_name=new PdfPCell(getNormalCell(pack_name));
						 Leakage_Pack_name.setColspan(2);
					        
						 table6.addCell(Leakage_Pack_name);
						    
					        PdfPCell Leakage_Cases=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(cases)));
					        Leakage_Cases.setColspan(1);
					        Leakage_Cases.setHorizontalAlignment(Element.ALIGN_RIGHT);

					        table6.addCell(Leakage_Cases);
					        
					        PdfPCell Leakage_Rate=new PdfPCell(getNormalCell("0"));
					        Leakage_Rate.setColspan(1);
					        Leakage_Rate.setHorizontalAlignment(Element.ALIGN_RIGHT);

					        table6.addCell(Leakage_Rate);
					        
					        PdfPCell Leakage_Total=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(cases*0)));
					        Leakage_Total.setColspan(1);
					        Leakage_Total.setHorizontalAlignment(Element.ALIGN_RIGHT);

					        table6.addCell(Leakage_Total);
					}
			        if(pack_name=="")
			        {
			        	 PdfPCell Leakage_Pack_name=new PdfPCell(getNormalCell(""));
						 Leakage_Pack_name.setColspan(2);
					        table6.addCell(Leakage_Pack_name);
						    
					        PdfPCell Leakage_Cases=new PdfPCell(getNormalCell(""));
					        Leakage_Cases.setColspan(1);
					        table6.addCell(Leakage_Cases);
					        
					        PdfPCell Leakage_Rate=new PdfPCell(getNormalCell(""));
					        Leakage_Rate.setColspan(1);
					        table6.addCell(Leakage_Rate);
					        
					        PdfPCell Leakage_Total=new PdfPCell(getNormalCell(""));
					        Leakage_Total.setColspan(1);
					        table6.addCell(Leakage_Total);
			        }			        
			        

			        
			        PdfPCell NetBal_Empty=new PdfPCell(getHeadingCell("Net Balance"));
			        NetBal_Empty.setColspan(4);
			        NetBal_Empty.setBackgroundColor(new BaseColor(238,239,223));
			        table6.addCell(NetBal_Empty);
			        
			        PdfPCell NetBal_Empty_Val=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(EmptyBalance)));
			        NetBal_Empty_Val.setColspan(1);
			        NetBal_Empty_Val.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        NetBal_Empty_Val.setBackgroundColor(new BaseColor(238,239,223));
			        table6.addCell(NetBal_Empty_Val);
			        
			        PdfPCell NetBal_Total=new PdfPCell(getHeadingCell("Net Balance"));
			        NetBal_Total.setColspan(4);
			        NetBal_Total.setBackgroundColor(new BaseColor(213,212,192));
			        
			        table6.addCell(NetBal_Total);
			        
			        PdfPCell NetBal_Total_Val=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(EmptyBalance+NetBalanceRec+SAPVehNdEmptyTotal)));
			        NetBal_Total_Val.setColspan(1);
			        NetBal_Total_Val.setBackgroundColor(new BaseColor(213,212,192));
			        NetBal_Total_Val.setHorizontalAlignment(Element.ALIGN_RIGHT);

			        table6.addCell(NetBal_Total_Val);

			        

			        document.add(table6);
			        document.add(Chunk.NEWLINE);	    


			        PdfPTable table3 = new PdfPTable(9);
				    
				    table3.setWidthPercentage(100f);
				    table3.getDefaultCell().setBorderColor(new BaseColor(122,122,122));
				    
				    reportheading.setColor(BaseColor.WHITE);
				    fonttitle.setColor(BaseColor.WHITE);
			        
				    
				    PdfPCell Assets=new PdfPCell(new Phrase("Assets",fonttitle));
				    Assets.setColspan(9);
				    Assets.setBackgroundColor(new BaseColor(122,122,122));
			        table3.addCell(Assets);
				    
				    PdfPCell Assets_Empty=new PdfPCell(getHeadingCell("Empty"));
				    Assets_Empty.setColspan(9);
				    Assets_Empty.setBackgroundColor(new BaseColor(193,193,193));
			        table3.addCell(Assets_Empty);
				    

				    PdfPCell Assets_250_ML_Shell=new PdfPCell(getHeadingCell("250 ML SHELL"));
				    Assets_250_ML_Shell.setColspan(3);
				    Assets_250_ML_Shell.setHorizontalAlignment(Element.ALIGN_CENTER);

				    table3.addCell(Assets_250_ML_Shell);

				    PdfPCell Assets_240_ML_SSRB=new PdfPCell(getHeadingCell("240 ML SSRB"));
				    Assets_240_ML_SSRB.setColspan(3);
				    Assets_240_ML_SSRB.setHorizontalAlignment(Element.ALIGN_CENTER);

				    table3.addCell(Assets_240_ML_SSRB);

			        PdfPCell Assets_250_ML_STD=new PdfPCell(getHeadingCell("250 ML STD"));
				    Assets_250_ML_STD.setColspan(3);
				    Assets_250_ML_STD.setHorizontalAlignment(Element.ALIGN_CENTER);

				    table3.addCell(Assets_250_ML_STD);
			        
		        	 
			        PdfPCell Balance_250_ML_SHELL=new PdfPCell(getNormalCell("Balance"));
			        Balance_250_ML_SHELL.setHorizontalAlignment(Element.ALIGN_CENTER);

			        table3.addCell(Balance_250_ML_SHELL);			        

		        	 PdfPCell Approved_Limit_250_ML_SHELL=new PdfPCell(getNormalCell("Approved Limit"));
		        	 Approved_Limit_250_ML_SHELL.setHorizontalAlignment(Element.ALIGN_CENTER);

		        	 table3.addCell(Approved_Limit_250_ML_SHELL);			        

		        	 PdfPCell Availble_Limit_250_ML_SHELL=new PdfPCell(getNormalCell("Available Limit"));
		        	 Availble_Limit_250_ML_SHELL.setHorizontalAlignment(Element.ALIGN_CENTER);

		        	 table3.addCell(Availble_Limit_250_ML_SHELL);			        

		        	 PdfPCell Balance_240_ML_SSRB=new PdfPCell(getNormalCell("Balance"));
		        	 Balance_240_ML_SSRB.setHorizontalAlignment(Element.ALIGN_CENTER);

		        	 table3.addCell(Balance_240_ML_SSRB);			        

			        	 PdfPCell Approved_Limit_240_ML_SSRB=new PdfPCell(getNormalCell("Approved Limit"));
			        	 Approved_Limit_240_ML_SSRB.setHorizontalAlignment(Element.ALIGN_CENTER);

			        	 table3.addCell(Approved_Limit_240_ML_SSRB);			        

			        	 PdfPCell Availble_Limit_240_ML_SSRB=new PdfPCell(getNormalCell("Available Limit"));
			        	 Availble_Limit_240_ML_SSRB.setHorizontalAlignment(Element.ALIGN_CENTER);

			        	 table3.addCell(Availble_Limit_240_ML_SSRB);			        
		        	 
					        PdfPCell Balance_250_ML_STD=new PdfPCell(getNormalCell("Balance"));
					        Balance_250_ML_STD.setHorizontalAlignment(Element.ALIGN_CENTER);

					        table3.addCell(Balance_250_ML_STD);			        

				        	 PdfPCell Approved_Limit_250_ML_STD=new PdfPCell(getNormalCell("Approved Limit"));
				        	 Approved_Limit_250_ML_STD.setHorizontalAlignment(Element.ALIGN_CENTER);

				        	 table3.addCell(Approved_Limit_250_ML_STD);			        

				        	 PdfPCell Availble_Limit_250_ML_STD=new PdfPCell(getNormalCell("Available Limit"));
				        	 Availble_Limit_250_ML_STD.setHorizontalAlignment(Element.ALIGN_CENTER);

				        	 table3.addCell(Availble_Limit_250_ML_STD);			        

				        	 if(Balance17!=0){
				         	 PdfPCell Bal_Shell=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance17)));
				         	Bal_Shell.setHorizontalAlignment(Element.ALIGN_CENTER);

				         	 table3.addCell(Bal_Shell);
				        	 } else
				        	 {
					         	 PdfPCell Bal_Shell=new PdfPCell(getNormalCell(""));
					        	 table3.addCell(Bal_Shell);
					         }
				        	 
				        	 if(LimitShell!=0){
					         	 PdfPCell Limit_Shell=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(LimitShell)));
					         	Limit_Shell.setHorizontalAlignment(Element.ALIGN_CENTER);

					         	 table3.addCell(Limit_Shell);
					        	 } else
					        	 {
						         	 PdfPCell Limit_Shell=new PdfPCell(getNormalCell(""));
						        	 table3.addCell(Limit_Shell);
						         }
				        	 
				        	 if(Overlimit17!=0){
					         	 PdfPCell OvrLimit_Shell=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Overlimit17)));
					         	OvrLimit_Shell.setHorizontalAlignment(Element.ALIGN_CENTER);

					         	 table3.addCell(OvrLimit_Shell);
					        	 } else
					        	 {
						         	 PdfPCell OvrLimit=new PdfPCell(getNormalCell(""));
						        	 table3.addCell(OvrLimit);
						         }
				        	 
				        	 if(Balance12!=0){
					         	 PdfPCell Bal_SSRB=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance12)));
					         	Bal_SSRB.setHorizontalAlignment(Element.ALIGN_CENTER);

					         	 table3.addCell(Bal_SSRB);
					        	 } else
					        	 {
						         	 PdfPCell Bal_SSRB=new PdfPCell(getNormalCell(""));
						        	 table3.addCell(Bal_SSRB);
						         }
				        	 
				        	 if(Limit240!=0){
					         	 PdfPCell Limit_240=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Limit240)));
					         	Limit_240.setHorizontalAlignment(Element.ALIGN_CENTER);

					         	 table3.addCell(Limit_240);
					        	 } else
					        	 {
						         	 PdfPCell Limit_240=new PdfPCell(getNormalCell(""));
						        	 table3.addCell(Limit_240);
						         }
				        	 
				        	 if(Overlimit12!=0){
					         	 PdfPCell OvrLimit_240=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Overlimit12)));
					         	OvrLimit_240.setHorizontalAlignment(Element.ALIGN_CENTER);

					         	 table3.addCell(OvrLimit_240);
					        	 } else
					        	 {
						         	 PdfPCell OvrLimit_240=new PdfPCell(getNormalCell(""));
						        	 table3.addCell(OvrLimit_240);
						         }
				        	 
				        	 if(Balance11!=0){
					         	 PdfPCell Bal_STD=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Balance11)));
					         	Bal_STD.setHorizontalAlignment(Element.ALIGN_CENTER);

					         	 table3.addCell(Bal_STD);
					        	 } else
					        	 {
						         	 PdfPCell Bal_STD=new PdfPCell(getNormalCell(""));
						        	 table3.addCell(Bal_STD);
						         }
				        	 
				        	 if(Limit250!=0){
					         	 PdfPCell Limit_STD=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Limit250)));
					         	Limit_STD.setHorizontalAlignment(Element.ALIGN_CENTER);

					         	 table3.addCell(Limit_STD);
					        	 } else
					        	 {
						         	 PdfPCell Limit_STD=new PdfPCell(getNormalCell(""));
						        	 table3.addCell(Limit_STD);
						         }
				        	 
				        	 if(Overlimit11!=0){
					         	 PdfPCell OvrLimit_STD=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(Overlimit11)));
					         	OvrLimit_STD.setHorizontalAlignment(Element.ALIGN_CENTER);

					         	 table3.addCell(OvrLimit_STD);
					        	 } else
					        	 {
						         	 PdfPCell OvrLimit_STD=new PdfPCell(getNormalCell(""));
						        	 table3.addCell(OvrLimit_STD);
						         }
						        
						        document.add(table3);
						        document.add(Chunk.NEWLINE);
						        
						        PdfPTable table4 = new PdfPTable(9);
							    
							    table4.setWidthPercentage(100f);
							    table4.getDefaultCell().setBorderColor(new BaseColor(122,122,122));
							    
							    reportheading.setColor(BaseColor.WHITE);
							    fonttitle.setColor(BaseColor.WHITE);

						        
						        
						        PdfPCell Color_injection=new PdfPCell(new Phrase("Coolor Injection",fonttitle));
						        Color_injection.setColspan(9);
						        Color_injection.setBackgroundColor(new BaseColor(122,122,122));
						        table4.addCell(Color_injection);
				        	 
						        PdfPCell Color_injection_Total=new PdfPCell(getHeadingCell("Total"));
						        Color_injection_Total.setColspan(3);
						        Color_injection_Total.setHorizontalAlignment(Element.ALIGN_CENTER);

						        table4.addCell(Color_injection_Total);
						        
						        ResultSet rs16 = s15.executeQuery("SELECT year(movement_date_parsed) year FROM pep.common_assets where tot_status = 'INJECTED' and outlet_id_parsed in (select id from common_outlets where cache_distributor_id = "+CID+") group by year(movement_date_parsed) order by year desc limit 5");
								while(rs16.next()){	
									PdfPCell Color_injection_Total_Val=new PdfPCell(getHeadingCell(rs16.getString("year")));
									Color_injection_Total_Val.setHorizontalAlignment(Element.ALIGN_CENTER);

									table4.addCell(Color_injection_Total_Val);
								}
						        
						        PdfPCell Color_injection_Other=new PdfPCell(getHeadingCell("Others"));
						        Color_injection_Other.setColspan(1);
						        Color_injection_Other.setHorizontalAlignment(Element.ALIGN_CENTER);

						        table4.addCell(Color_injection_Other);
						        
				        	 
						    	
							
								
								
								long TotalCoolerCount=0;
								ResultSet rs65 = s65.executeQuery("select sum(no_cooler) total_cooler from ( "+
										  " SELECT year(movement_date_parsed) year, count(*) no_cooler FROM pep.common_assets where tot_status = 'INJECTED' and outlet_id_parsed in (select id from common_outlets where cache_distributor_id = "+CID+") group by year(movement_date_parsed) order by year "+
							              " ) as tab ");
								if(rs65.first()){

									TotalCoolerCount = rs65.getLong("total_cooler");
								}
								PdfPCell TotalColCount=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalCoolerCount)));
								TotalColCount.setColspan(3);
								TotalColCount.setHorizontalAlignment(Element.ALIGN_CENTER);

								table4.addCell(TotalColCount);
																
								
								ResultSet rs63 = s63.executeQuery("SELECT year(movement_date_parsed) year, count(*) no_cooler FROM pep.common_assets where tot_status = 'INJECTED' and outlet_id_parsed in (select id from common_outlets where cache_distributor_id = "+CID+") group by year(movement_date_parsed) order by year desc limit 5");
								
								while(rs63.next()){
									PdfPCell TotalColCount1=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormat(rs63.getLong("no_cooler"))));
									TotalColCount1.setHorizontalAlignment(Element.ALIGN_CENTER);

									table4.addCell(TotalColCount1);
								}

								int RowCount=0;	
								long RemainingOtherTOTCounter=0;
								
								ResultSet rs64 = s64.executeQuery("SELECT year(movement_date_parsed) year, count(*) no_cooler FROM pep.common_assets where tot_status = 'INJECTED' and outlet_id_parsed in (select id from common_outlets where cache_distributor_id = "+CID+") group by year(movement_date_parsed) order by year desc");
								while(rs64.next()){
									
									if(RowCount>4){ //skipping 1st 5 years
										RemainingOtherTOTCounter += rs64.getLong("no_cooler");
									}
									RowCount++;
								}
								
								PdfPCell TotalColCount2=new PdfPCell(getNormalCell(Utilities.getDisplayCurrencyFormat(RemainingOtherTOTCounter)));
								TotalColCount2.setHorizontalAlignment(Element.ALIGN_CENTER);

								table4.addCell(TotalColCount2);
						        
//								  PdfPCell Disclaimer=new PdfPCell(getHeadingCell("Disclaimer"));
//								  Disclaimer.setColspan(5);
//							        table3.addCell(Disclaimer);
//									PdfPCell Dis1=new PdfPCell(getNormalCell("1.Ledger balance has been adjusted for the last month incentive."));
//									Dis1.setColspan(5);
//							        table3.addCell(Dis1);
//									PdfPCell Dis2=new PdfPCell(getNormalCell("2.Vehicle account does not cover temporary vehicles issued. If applicable, depreciation/demiurges against temporary vehicles will be charged according to policy."));
//									Dis2.setColspan(5);
//							        table3.addCell(Dis2);
//
						        document.add(table4);
						        document.add(Chunk.NEWLINE);
						        document.add(DisclaimerHeading);
						        //document.add(new Paragraph("Disclaimer"));
						        document.add(Disclaimer);
//						        document.add(Chunk.NEWLINE);

						        //document.add(new Paragraph("Revenue/Chiller/Day = Average revenue per chiller per day in last 30 days",fontheading));

						        document.close();					        


                
	    
	    
	}
	
	private PdfPCell getHeadingCell(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        
        pcell.setMinimumHeight(20);
        return pcell;
	}
	
	private PdfPCell getHeadingCellCenter(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        pcell.setMinimumHeight(20);
        return pcell;
	}
	private PdfPCell getHeadingCellCenter(String title, int colspan){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setVerticalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        pcell.setMinimumHeight(20);
        pcell.setColspan(colspan);
        return pcell;
	}
	
	
	
	private PdfPCell getNormalCell(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        return pcell;
	}
	private PdfPCell getNormalCellCenter(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        return pcell;
	}
	
	private PdfPCell getNormalCellOB(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(255,255,217));
        return pcell;
	}

	private PdfPCell getNormalCellStrikeRate(String title){
		
		int StrikeRate = Utilities.parseInt(title);
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title+"%", fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        
        if (StrikeRate < 50){
        	pcell.setBackgroundColor(BaseColor.RED);
        	pcell.setBorderColor(BaseColor.RED);
        }
        
        return pcell;
	}
	private PdfPCell getNormalCellDropSize(String title){
		
		double DropSize = Utilities.parseDouble(title);
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        
        
        if (DropSize < 4){
        	pcell.setBackgroundColor(BaseColor.RED);
        	pcell.setBorderColor(BaseColor.RED);
        }
        
        return pcell;
	}
	private PdfPCell getNormalCellSKU(String title){
		
		double SKU = Utilities.parseDouble(title);
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        
        if (SKU < 2){
        	pcell.setBackgroundColor(BaseColor.RED);
        	pcell.setBorderColor(BaseColor.RED);
        }
        
        return pcell;
	}
	private PdfPCell getNormalCellPackPerOrder(String title){
		
		double SKU = Utilities.parseDouble(title);
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        
        if (SKU < 1.5){
        	pcell.setBackgroundColor(BaseColor.RED);
        	pcell.setBorderColor(BaseColor.RED);
        }
        
        return pcell;
	}
	
	private PdfPCell getNormalCellRightAligned(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        return pcell;
	}
	private PdfPCell getNormalCellRightAlignedTarget(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(255, 245, 254));
        return pcell;
	}
	
	private PdfPCell getNormalCellTargetAchieved(String title, boolean isHighlighted){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(255, 245, 254));
        
        if (isHighlighted){
        	pcell.setBackgroundColor(BaseColor.RED);
        	pcell.setBorderColor(BaseColor.RED);
        }
        
        return pcell;
	}

	private PdfPCell getNormalCellRightAlignedSSRB(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(233, 255, 217));
        return pcell;
	}
	
	private PdfPCell getNormalCellRightAlignedCC(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(227, 230, 255));
        return pcell;
	}
	
	private PdfPCell getHeadingCellLevel2(String title, int colspan){	
	    PdfPCell pcell2 = new PdfPCell(new Phrase(title, reportheading));
	    pcell2.setBackgroundColor(BaseColor.DARK_GRAY);
	    pcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
	    pcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    pcell2.setBorderColor(BaseColor.LIGHT_GRAY);
	    pcell2.setColspan(colspan);
	    pcell2.setMinimumHeight(20);
	    return pcell2;
	}
	
	private PdfPCell getHeadingCellLevel2Center(String title, int colspan){	
	    PdfPCell pcell2 = new PdfPCell(new Phrase(title, reportheading));
	    pcell2.setBackgroundColor(BaseColor.DARK_GRAY);
	    pcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	    pcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    pcell2.setBorderColor(BaseColor.LIGHT_GRAY);
	    pcell2.setColspan(colspan);
	    pcell2.setMinimumHeight(20);
	    return pcell2;
	}
	
	private PdfPCell getHeadingCellLevel3(String title, int colspan){	
	    PdfPCell pcell2 = new PdfPCell(new Phrase(title, fonttableheader));
	    //pcell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
	    pcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
	    pcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    pcell2.setBorderColor(BaseColor.LIGHT_GRAY);
	    pcell2.setColspan(colspan);
	    //pcell2.setMinimumHeight(20);
	    return pcell2;
	}
	private PdfPCell getHeadingCellLevel3Center(String title, int colspan){	
	    PdfPCell pcell2 = new PdfPCell(new Phrase(title, fonttableheader));
	    pcell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
	    pcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	    pcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    pcell2.setBorderColor(BaseColor.LIGHT_GRAY);
	    pcell2.setColspan(colspan);
	    pcell2.setMinimumHeight(20);
	    return pcell2;
	}

}
