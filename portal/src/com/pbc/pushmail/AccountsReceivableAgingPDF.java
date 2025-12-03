package com.pbc.pushmail;

import java.io.FileOutputStream;
import java.io.IOException;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class AccountsReceivableAgingPDF {

	public static final String RESULT = Utilities.getEmailAttachmentsPath()+ "/ARSummary.pdf";
	
	
	Font fonttitle = FontFactory.getFont("Arial", 12, Font.BOLD);
	
	Font fonttableheader = FontFactory.getFont("Arial", 10, Font.BOLD);
	Font fontheading = FontFactory.getFont("Arial", 10, Font.NORMAL);
	Font fontpbc = FontFactory.getFont("Arial", 10, Font.BOLD);
	Font reportheading1 = FontFactory.getFont("Arial", 10, Font.BOLD);
	Font reportheading = FontFactory.getFont("Arial", 10, Font.BOLD);
	Font reportheading3 = FontFactory.getFont("Arial", 10, Font.BOLD);
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	Date StartDate = null;
    Date MonthToDateStartDate = null;
    Date MonthToDateEndDate = null;	
    long SND_ID = 0;
    String HTMLMessage;
    String WhereSND;
	
	//Date EndDate = new Date();
	
    
	public static void main(String[] args) throws DocumentException, IOException{
		try {
			new AccountsReceivableAgingPDF().createPdf(RESULT);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getHTMLMessage(){
		return HTMLMessage;
	}
	
	public AccountsReceivableAgingPDF() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		StartDate = new Date();
		//StartDate = Utilities.getDateByDays(-1);
		
    	Calendar cc = Calendar.getInstance();
    	cc.setTime(StartDate);
    	
    	int year = cc.get(Calendar.YEAR);
    	int month = cc.get(Calendar.MONTH);
       
        MonthToDateStartDate = Utilities.getStartDateByMonth(month, year);
        MonthToDateEndDate = new Date();
		
		
        
		ds.createConnectionToReplica();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
	}
	
	public void setSND(long SND_ID){
		this.SND_ID = SND_ID;
	}
	
	public void createPdf(String filename) throws DocumentException, IOException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
	    Document document = new Document(PageSize.A4.rotate());
	    PdfWriter.getInstance(document, new FileOutputStream(filename));
	    
	    document.open();
	    
	    
	    reportheading.setColor(BaseColor.WHITE);
	    reportheading1.setColor(BaseColor.WHITE);
	    
        
        Image img = Image.getInstance(String.format(Utilities.getImageResoucesPath()+"/"+"%s", "pepsi28.png"));
        img.scaleToFit(25, 25);
        img.setAbsolutePosition(35, 537);
        
        Paragraph pbc = new Paragraph("         Punjab Beverages",fontpbc);
        
        PdfPTable rtable = new PdfPTable(7);
	    
	    rtable.setWidthPercentage(100f);
	    rtable.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
    	
	    PdfPCell pcell = new PdfPCell(new Phrase("List of Customers", reportheading1));
        pcell.setBackgroundColor(BaseColor.DARK_GRAY);
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.DARK_GRAY);
        pcell.setColspan(7);
        pcell.setMinimumHeight(22);
        //rtable.addCell(pcell);
        
        
	    pcell = new PdfPCell(new Phrase("Customer", fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        pcell.setColspan(2);
        
        rtable.addCell(pcell);
        
        rtable.addCell(getHeadingCell("Credit Balance"));
       	rtable.addCell(getHeadingCell("< 10 Days"));
       	rtable.addCell(getHeadingCell("10 - 20 Days"));
       	rtable.addCell(getHeadingCell("21 - 30 Days"));
       	rtable.addCell(getHeadingCell("> 30 Days"));
       	
		double TotalTotalCredit=0;
		double TotalCreditLimit=0;
		double TotalOneTimeCreditBalance=0;
		double InvoicesNotPosted=0;
		double RowTotal = 0;
		double GrandRowTotal = 0;
		double TotalInvoicesNotPosted =0;

		double TotalLedgerDr = 0;
		double TotalLedgerCr = 0;
		
		double TotalUnliftedOrders = 0;
		
		WhereSND = " and cd.snd_id in ("+SND_ID+")";
		
		if (SND_ID == 0){
			WhereSND = "";
		}
		
		
		String LastCategoryLabel = "";
		
		ResultSet rs11 = s.executeQuery("SELECT cd.distributor_id, cd.name, (select ifnull(sum(debit),0) - ifnull(sum(credit),0) from gl_transactions_accounts where account_id in (select id from gl_accounts where customer_id = cd.distributor_id and type_id = 1)) ledger_balance, (select ifnull(sum(debit),0) - ifnull(sum(credit),0) from gl_transactions_accounts where account_id in (select id from gl_accounts where customer_id = cd.distributor_id and type_id = 3)) one_time_credit_balance, cd.category_id, (select label from common_distributors_categories where id = cd.category_id) category_label, cd.is_active, cd.is_central_blocked, (select id from gl_accounts where customer_id = cd.distributor_id and type_id = 3) gl_account_id FROM pep.common_distributors cd  where 1=1 "+WhereSND+" having ledger_balance != 0 or one_time_credit_balance != 0 order by category_id");
		while(rs11.next()){
			
			long GLAccountID = rs11.getLong("gl_account_id");
			
			String ActiveInActive="";
			//In Active
			
			if(rs11.getInt("is_active")==0 && rs11.getInt("is_central_blocked")==1){
				ActiveInActive = " [Inactive]"; 
			}
			
			else if(rs11.getInt("is_active")==0){
				ActiveInActive = " [Inactive]"; 
			}
			
			else if(rs11.getInt("is_central_blocked")==1){
				ActiveInActive = " [Blocked]"; 
			}
			
			
			long DistributorID = rs11.getLong(1);
			String DistributorName = rs11.getString(2);
			String CategoryLabel = rs11.getString("category_label");
			
			if (!LastCategoryLabel.equals(CategoryLabel)){
			    
				pcell = new PdfPCell(new Phrase(CategoryLabel, reportheading1));
		        pcell.setBackgroundColor(BaseColor.DARK_GRAY);
		        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        pcell.setBorderColor(BaseColor.DARK_GRAY);
		        pcell.setColspan(7);
		        pcell.setMinimumHeight(22);
		        rtable.addCell(pcell);
				
		        LastCategoryLabel = CategoryLabel;
			}
			
			
			double CreditLimit = 0;//rs11.getDouble("credit_limit");
			double OneTimeCreditBalance = rs11.getDouble("one_time_credit_balance");
			
			double LedgerBalance = rs11.getDouble("ledger_balance");
			double LedgerDr = 0;
			double LedgerCr = 0;
			if (LedgerBalance < 0){
				LedgerCr = LedgerBalance*-1;
			}else{
				LedgerDr = LedgerBalance;
			}
			
			
			TotalLedgerDr += LedgerDr;
			TotalLedgerCr += LedgerCr;
			
			double UnliftedOrders = 0;//rs11.getDouble("unlifted_orders") + rs11.getDouble("unlifted_invoices");
			double TotalCredit = LedgerDr + OneTimeCreditBalance;
			
			TotalTotalCredit += TotalCredit;
			TotalUnliftedOrders += UnliftedOrders;
			
			TotalCreditLimit+=CreditLimit;
			TotalOneTimeCreditBalance+=OneTimeCreditBalance;
			 
			
			
			if (TotalCredit != 0){
			
				double ABSlot1 = 0;
				double ABSlot2 = 0;
				double ABSlot3 = 0;
				double ABSlot4 = 0;
				
				ResultSet rs12 = s2.executeQuery("SELECT sum(glta.debit) FROM pep.gl_transactions glt join gl_transactions_accounts glta on glt.id = glta.id where glta.account_id = "+GLAccountID+" and to_days(curdate())-to_days(glt.created_on) < 10");
				if (rs12.first()){
					ABSlot1 = rs12.getDouble(1);
				}
				ResultSet rs13 = s2.executeQuery("SELECT sum(glta.debit) FROM pep.gl_transactions glt join gl_transactions_accounts glta on glt.id = glta.id where glta.account_id = "+GLAccountID+" and to_days(curdate())-to_days(glt.created_on) >= 10 and to_days(curdate())-to_days(glt.created_on) <= 20");
				if (rs13.first()){
					ABSlot2 = rs13.getDouble(1);
				}
				ResultSet rs14 = s2.executeQuery("SELECT sum(glta.debit) FROM pep.gl_transactions glt join gl_transactions_accounts glta on glt.id = glta.id where glta.account_id = "+GLAccountID+" and to_days(curdate())-to_days(glt.created_on) >= 21 and to_days(curdate())-to_days(glt.created_on) <= 30");
				if (rs14.first()){
					ABSlot3 = rs14.getDouble(1);
				}
				ResultSet rs15 = s2.executeQuery("SELECT sum(glta.debit) FROM pep.gl_transactions glt join gl_transactions_accounts glta on glt.id = glta.id where glta.account_id = "+GLAccountID+" and to_days(curdate())-to_days(glt.created_on) > 30");
				if (rs15.first()){
					ABSlot4 = rs15.getDouble(1);
				}
				
				double RSlot1 = 0;
				//if ()
				
				
    	    pcell = new PdfPCell(new Phrase(Utilities.truncateStringToMax(DistributorID+" - "+DistributorName, 28)+ActiveInActive , fontheading));
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorderColor(BaseColor.LIGHT_GRAY);
            pcell.setColspan(2);
        	rtable.addCell(pcell);
        	
        	rtable.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalCredit)));
        	
        	if (CreditLimit != 0){
        		rtable.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(0)));
        	}else{
        		rtable.addCell("");
        	}
        	
        	//table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(SecurityBalance)));
        	
        	if (LedgerDr != 0){
        		rtable.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(0)));
        	}else{
        		rtable.addCell("");
        	}
        	
        	
        	if (OneTimeCreditBalance != 0){
        		rtable.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(0)));
        	}else{
        		rtable.addCell("");
        	}
        	
        	
        	if (TotalCredit != 0){
        		rtable.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(0)));
        	}else{
        		rtable.addCell("");
        	}
        	
			}
			
			
        	
		}
	    pcell = new PdfPCell(new Phrase("Total", fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setColspan(2);
    	rtable.addCell(pcell);
		
    	rtable.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalTotalCredit)));
    	rtable.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(0)));
    	//table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatTwoDecimalFixedAccounting(SecurityBalance)));
    	rtable.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(0)));
    	rtable.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(0)));
    	rtable.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRoundedAccounting(0)));
       	
        
        
	    // Summary Page
	    
        //document.newPage();
        
        document.add(img);
        document.add(pbc);
        
        Paragraph header = new Paragraph(""+Utilities.getDisplayDateTimeFormat(StartDate),fonttitle);
        header.setAlignment(Element.ALIGN_RIGHT);
        
        document.add(header);
        document.add(Chunk.NEWLINE);
        
        PdfPTable table = new PdfPTable(5);
	    
	    table.setWidthPercentage(100f);
	    table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
    	
	    table = new PdfPTable(5);
	    
	    table.setWidthPercentage(100f);
	    table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
    	
	    pcell = new PdfPCell(new Phrase("Aging Analysis as at "+Utilities.getDisplayDateFormat(StartDate), reportheading1));
        pcell.setBackgroundColor(BaseColor.DARK_GRAY);
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.DARK_GRAY);
        pcell.setColspan(5);
        pcell.setMinimumHeight(25);
        table.addCell(pcell);
	    
        table.addCell(getHeadingCellSummary("Total Credit"));
        table.addCell(getHeadingCellSummary("< 10 Days"));
       	table.addCell(getHeadingCellSummary("10 - 20 Days"));
       	table.addCell(getHeadingCellSummary("21 - 30 Days"));
       	table.addCell(getHeadingCellSummary("> 30 Days"));
        
       	table.addCell(getNormalCellSummary(Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalTotalCredit)));
       	table.addCell(getNormalCellSummary(Utilities.getDisplayCurrencyFormatRoundedAccounting(0)));
    	table.addCell(getNormalCellSummary(Utilities.getDisplayCurrencyFormatRoundedAccounting(0)));
    	table.addCell(getNormalCellSummary(Utilities.getDisplayCurrencyFormatRoundedAccounting(0)));
    	table.addCell(getNormalCellSummary(Utilities.getDisplayCurrencyFormatRoundedAccounting(0)));
    	
    	setHTMLMessage(TotalCreditLimit,TotalLedgerDr,TotalOneTimeCreditBalance, TotalTotalCredit);
    	
    	
    	document.add(table);
    	
    	//document.newPage();
        
        //document.add(cheader);
        //document.add(Chunk.NEWLINE);
        //document.add(ctable);
    	
        //document.newPage();
        //document.add(rheader);
        document.add(Chunk.NEWLINE);
        document.add(rtable);
        
    	// Cash Page
    	
    	
    	
        document.close();
	}
	
	private PdfPCell getHeadingCell(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        pcell.setMinimumHeight(16);
        return pcell;
	}
	private PdfPCell getHeadingCellSummary(String title){
		
		Font fonttableheader = FontFactory.getFont("Arial", 12, Font.BOLD);
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        pcell.setMinimumHeight(22);
        return pcell;
	}
	private PdfPCell getNormalCell(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        return pcell;
	}
	private PdfPCell getNormalCellSummary(String title){
		
		Font fontheading = FontFactory.getFont("Arial", 12, Font.BOLD);
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setMinimumHeight(22);
        return pcell;
	}
	
	public void setHTMLMessage(double CreditLimit, double CreditLimitUtilized, double OneTimeCredit, double TotalCredit) throws SQLException{
		
		String CreditValues = "";
		for (int i = 0; i < 60; i++){
			int days = i - 59;
			double balance = getCreditBalance(days);
			

			if (i != 0){
				CreditValues = CreditValues + ",";
			}
			
			CreditValues = CreditValues + (balance / 1000000);
		}
		
		
		
		
			String html = "<html>";
			html += "<body><br>";
			html += "<table>";
			
				html += "<tr>";
				html += "<th colspan='2' style='text-align: left; background-color: #3D5AB3; color: white; height: 22px;'>Credit Balance in 60 days</th>";
				html += "</tr>";
			
				html += "<tr>";
				html += "<td colspan='2' style='text-align: center;'><img src='https://chart.googleapis.com/chart?cht=lc&chd=t:"+CreditValues+"&chds=a&chs=400x250&chl=60 Days Ago|Today&chxt=y,r&chco=f0516c&chdl=Credit Balance&chdlp=b&chtt=(millions)'></td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<th colspan='2'>&nbsp;";
				html += "</tr>";
				
				html += "<tr>";
					html += "<th style='text-align: left; background-color: #3D5AB3; color: white; height: 22px;'>Classification</th>";
					html += "<th style='background-color: #3D5AB3; color: white; height: 22px;'>Credit Extended</td>";
				html += "</tr>";
				
				
				ResultSet rs = s.executeQuery("select id, label from common_distributors_categories where id in (select distinct category_id from common_distributors)");
				while(rs.next()){
					
					int CategoryID = rs.getInt(1);
					String CategoryName = rs.getString(2);
					
					double iTotalCredit = 0;
					ResultSet rs2 = s2.executeQuery("SELECT cd.distributor_id, cd.name, (select ifnull(sum(debit),0) - ifnull(sum(credit),0) from gl_transactions_accounts where account_id in (select id from gl_accounts where customer_id = cd.distributor_id and type_id = 1)) ledger_balance, (select ifnull(sum(debit),0) - ifnull(sum(credit),0) from gl_transactions_accounts where account_id in (select id from gl_accounts where customer_id = cd.distributor_id and type_id = 3)) one_time_credit_balance, ifnull((SELECT credit_limit FROM gl_customer_credit_limit where customer_id = cd.distributor_id and is_active = 1 and curdate() between valid_from and valid_to limit 1),0) credit_limit, ifnull((SELECT sum(gop.order_amount) FROM gl_order_posting gop where gop.customer_id = cd.distributor_id and gop.is_invoiced = 0 and gop.order_no not in (select sap_order_no from inventory_delivery_note)),0) unlifted_orders, ifnull((SELECT sum(gip.invoice_amount) FROM gl_invoice_posting gip where gip.customer_id = cd.distributor_id and gip.order_no not in (select sap_order_no from inventory_delivery_note)),0) unlifted_invoices FROM pep.common_distributors cd  where 1=1 "+WhereSND+" and category_id = "+CategoryID+" having ledger_balance != 0 or one_time_credit_balance != 0 or credit_limit != 0 or unlifted_orders != 0 or unlifted_invoices != 0");
					while(rs2.next()){
						
						double OneTimeCreditBalance = rs2.getDouble("one_time_credit_balance");
						double LedgerBalance = rs2.getDouble("ledger_balance");
						double LedgerDr = 0;
						if (LedgerBalance < 0){
						}else{
							LedgerDr = LedgerBalance;
						}
						double iCredit = LedgerDr + OneTimeCreditBalance;
						
						iTotalCredit += iCredit;
						
					}
					
					if (iTotalCredit != 0){
						html += "<tr>";
						html += "<td style='text-align: left;background-color: #EDEFF2;'>"+CategoryName+"</td>";
						html += "<td style='text-align: right;background-color: #EDEFF2;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(iTotalCredit)+"</b>";
						html += "</tr>";
					}
					
				}
				
				html += "<tr>";
					html += "<td style='text-align: left;background-color: #EDEFF2;'><b>Total</b>";
					html += "<td style='text-align: right;background-color: #EDEFF2;'><b>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalCredit)+"</b>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<th colspan='2'>&nbsp;";
				html += "</tr>";
				
				html += "<tr>";
				html += "<th style='text-align: left; background-color: #3D5AB3; color: white; height: 22px;' colspan='2'>Summary</th>";
				html += "</tr>";
				
					
					double InactiveCredit = 0;
					
					ResultSet rs2 = s2.executeQuery("SELECT cd.distributor_id, cd.name, (select ifnull(sum(debit),0) - ifnull(sum(credit),0) from gl_transactions_accounts where account_id in (select id from gl_accounts where customer_id = cd.distributor_id and type_id = 1)) ledger_balance, (select ifnull(sum(debit),0) - ifnull(sum(credit),0) from gl_transactions_accounts where account_id in (select id from gl_accounts where customer_id = cd.distributor_id and type_id = 3)) one_time_credit_balance, ifnull((SELECT credit_limit FROM gl_customer_credit_limit where customer_id = cd.distributor_id and is_active = 1 and curdate() between valid_from and valid_to limit 1),0) credit_limit, ifnull((SELECT sum(gop.order_amount) FROM gl_order_posting gop where gop.customer_id = cd.distributor_id and gop.is_invoiced = 0 and gop.order_no not in (select sap_order_no from inventory_delivery_note)),0) unlifted_orders, ifnull((SELECT sum(gip.invoice_amount) FROM gl_invoice_posting gip where gip.customer_id = cd.distributor_id and gip.order_no not in (select sap_order_no from inventory_delivery_note)),0) unlifted_invoices FROM pep.common_distributors cd  where 1=1 "+WhereSND+" and ((is_active = 0 or is_central_blocked = 1)) having ledger_balance != 0 or one_time_credit_balance != 0 or credit_limit != 0 or unlifted_orders != 0 or unlifted_invoices != 0");
					while(rs2.next()){
						
						double OneTimeCreditBalance = rs2.getDouble("one_time_credit_balance");
						double LedgerBalance = rs2.getDouble("ledger_balance");
						double LedgerDr = 0;
						if (LedgerBalance < 0){
						}else{
							LedgerDr = LedgerBalance;
						}
						double iCredit = LedgerDr + OneTimeCreditBalance;
						
						InactiveCredit += iCredit;
						
					}
					
					
					
					
					html += "<tr>";
					html += "<td style='text-align: left;background-color: #EDEFF2;'>Active Accounts</b>";
					html += "<td style='text-align: right;background-color: #EDEFF2;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalCredit-InactiveCredit)+"</b>";
					html += "</tr>";

					html += "<tr>";
					html += "<td style='text-align: left;background-color: #EDEFF2;'>Inactive / Blocked Accounts</b>";
					html += "<td style='text-align: right;background-color: #EDEFF2;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(InactiveCredit)+"</b>";
					html += "</tr>";
					
					html += "<tr>";
					html += "<td style='text-align: left;background-color: #EDEFF2; font-weight: bold;'>Total</b>";
					html += "<td style='text-align: right;background-color: #EDEFF2; font-weight: bold;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalCredit)+"</b>";
					html += "</tr>";
				
					
					html += "<tr>";
					html += "<th colspan='2'>&nbsp;";
					html += "</tr>";
					
				
				html += "<tr>";
				html += "<td colspan='2' style='text-align: left'>Please see attachment for details.<br>This is a system generated email, please do not reply to it.";
				html += "</tr>";

			html += "</table>";
		html += "</body>";
		html += "</html>";
		
		this.HTMLMessage = html;
	}
	
	
	private double getCreditBalance(int days) throws SQLException{
			
			Date idate = Utilities.getDateByDays(days);
			
			double iTotalCredit = 0;
			ResultSet rs2 = s3.executeQuery("SELECT cd.distributor_id, cd.name, (select ifnull(sum(gta.debit),0) - ifnull(sum(gta.credit),0) from gl_transactions_accounts gta join gl_transactions gt on gta.id = gt.id where gt.created_on_date <= "+Utilities.getSQLDate(idate)+" and gta.account_id in (select id from gl_accounts where customer_id = cd.distributor_id and type_id = 1)) ledger_balance, (select ifnull(sum(gta.debit),0) - ifnull(sum(gta.credit),0) from gl_transactions_accounts gta join gl_transactions gt on gta.id = gt.id where gt.created_on_date <= "+Utilities.getSQLDate(idate)+" and gta.account_id in (select id from gl_accounts where customer_id = cd.distributor_id and type_id = 3)) one_time_credit_balance, ifnull((SELECT credit_limit FROM gl_customer_credit_limit where customer_id = cd.distributor_id and is_active = 1 and curdate() between valid_from and valid_to limit 1),0) credit_limit, ifnull((SELECT sum(gop.order_amount) FROM gl_order_posting gop where gop.customer_id = cd.distributor_id and gop.is_invoiced = 0 and gop.order_no not in (select sap_order_no from inventory_delivery_note)),0) unlifted_orders, ifnull((SELECT sum(gip.invoice_amount) FROM gl_invoice_posting gip where gip.customer_id = cd.distributor_id and gip.order_no not in (select sap_order_no from inventory_delivery_note)),0) unlifted_invoices FROM pep.common_distributors cd  where 1=1 "+WhereSND+" having ledger_balance != 0 or one_time_credit_balance != 0 or credit_limit != 0 or unlifted_orders != 0 or unlifted_invoices != 0");
			while(rs2.next()){
				
				double OneTimeCreditBalance = rs2.getDouble("one_time_credit_balance");
				double LedgerBalance = rs2.getDouble("ledger_balance");
				double LedgerDr = 0;
				if (LedgerBalance < 0){
				}else{
					LedgerDr = LedgerBalance;
				}
				double iCredit = LedgerDr + OneTimeCreditBalance;
				
				iTotalCredit += iCredit;
				
			}
		
		return iTotalCredit;
	}
	
}
