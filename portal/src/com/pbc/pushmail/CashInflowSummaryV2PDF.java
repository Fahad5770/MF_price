package com.pbc.pushmail;

import java.io.FileOutputStream;
import java.io.IOException;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

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

public class CashInflowSummaryV2PDF {

	public static final String RESULT = Utilities.getEmailAttachmentsPath()+ "/CashInflowSummary.pdf";
	
	
	Font fonttitle = FontFactory.getFont("Arial", 12, Font.BOLD);
	
	Font fonttableheader = FontFactory.getFont("Arial", 8, Font.BOLD);
	Font fontheading = FontFactory.getFont("Arial", 8, Font.NORMAL);
	Font fontpbc = FontFactory.getFont("Arial", 10, Font.BOLD);
	Font reportheading1 = FontFactory.getFont("Arial", 8, Font.BOLD);
	Font reportheading = FontFactory.getFont("Arial", 10, Font.BOLD);
	Font reportheading3 = FontFactory.getFont("Arial", 10, Font.BOLD);
	Font reportheading4 = FontFactory.getFont("Arial", 8, Font.BOLD);
	Font reportheading5 = FontFactory.getFont("Arial", 10, Font.BOLD);
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	
	String YTDSeries = "";
	String YTDSeriesLY = "";
	
	Date StartDate = null;
    Date MonthToDateStartDate = null;
    Date MonthToDateEndDate = null;	
    Date MonthToDateStartDateLastYear = null;
    Date MonthToDateEndDateLastYear = null;	
    
    Date YearStartDate = null;
    Date YearEndDate = null;
    Date YearStartDateLastYear = null;
    Date YearEndDateLastYear = null;
    
    String HTMLMessage;
	//Date EndDate = new Date();
	
    
    double MTDSD1 = 0;
    double MTDSD2 = 0;
    double MTDB2B = 0;
    double MTDOther = 0;
    double MTDTotal = 0;
    
    double MTDPSD1 = 0;
    double MTDPSD2 = 0;
    double MTDPB2B = 0;
    double MTDPOther = 0;
    double MTDPTotal = 0;    

    double MTDPSD1_FULL = 0;
    double MTDPSD2_FULL = 0;
    double MTDPB2B_FULL = 0;
    double MTDPOther_FULL = 0;
    double MTDPTotal_FULL = 0;    
    
    double MTDSD1LY = 0;
    double MTDSD2LY = 0;
    double MTDB2BLY = 0;
    double MTDOtherLY = 0;
    double MTDTotalLY = 0;
    
    double MTDSD1LY_FULL = 0;
    double MTDSD2LY_FULL = 0;
    double MTDB2BLY_FULL = 0;
    double MTDOtherLY_FULL = 0;
    double MTDTotalLY_FULL = 0;
    
    double YTDSD1 = 0;
    double YTDSD2 = 0;
    double YTDB2B = 0;
    double YTDOther = 0;
    double YTDTotal = 0;

    double YTDSD1LY = 0;
    double YTDSD2LY = 0;
    double YTDB2BLY = 0;
    double YTDOtherLY = 0;
    double YTDTotalLY = 0;

    double YTDSD1LY_FULL = 0;
    double YTDSD2LY_FULL = 0;
    double YTDB2BLY_FULL = 0;
    double YTDOtherLY_FULL = 0;
    double YTDTotalLY_FULL = 0;

    
    double YTDPSD1 = 0;
    double YTDPSD2 = 0;
    double YTDPB2B = 0;
    double YTDPOther = 0;
    double YTDPTotal = 0;
    
    double YTDPSD1_FULL = 0;
    double YTDPSD2_FULL = 0;
    double YTDPB2B_FULL = 0;
    double YTDPOther_FULL = 0;
    double YTDPTotal_FULL = 0;
    
    double AdvanceSD1 = 0;
    double AdvanceSD2 = 0;
    double AdvanceB2B = 0;
    double AdvanceOther = 0;
    
    double NotLiftedSD1 = 0;
    double NotLiftedSD2 = 0;
    double NotLiftedB2B = 0;
    double NotLiftedOther = 0;
    
    
	public static void main(String[] args) throws DocumentException, IOException{
		try {
			new CashInflowSummaryV2PDF().createPdf(RESULT);
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
	
	public CashInflowSummaryV2PDF() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		StartDate = new Date();
		StartDate = Utilities.getDateByDays(-1);
		
		
    	Calendar cc = Calendar.getInstance();
    	cc.setTime(StartDate);
    	
    	int year = cc.get(Calendar.YEAR);
    	int LastYear = year-1;
    	int month = cc.get(Calendar.MONTH);
       
        MonthToDateStartDate = Utilities.getStartDateByMonth(month, year);
        MonthToDateEndDate = StartDate;
		
        MonthToDateStartDateLastYear = DateUtils.addYears(MonthToDateStartDate, -1);
        MonthToDateEndDateLastYear = DateUtils.addYears(MonthToDateEndDate, -1);
        
        
        YearStartDate = Utilities.getStartDateByMonth(0, year);
        YearEndDate = StartDate;
		
        YearStartDateLastYear = DateUtils.addYears(YearStartDate, -1);
        YearEndDateLastYear = DateUtils.addYears(YearEndDate, -1);
        
        
        
        
		ds.createConnection();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
	}
	
	public void createPdf(String filename) throws DocumentException, IOException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
	    Document document = new Document(PageSize.A4.rotate());
	    PdfWriter.getInstance(document, new FileOutputStream(filename));
	    
	    document.open();
	    
	    
	    reportheading.setColor(BaseColor.WHITE);
	    reportheading1.setColor(BaseColor.WHITE);
	    reportheading5.setColor(BaseColor.WHITE);
        
        Image img = Image.getInstance(String.format(Utilities.getImageResoucesPath()+"/"+"%s", "pepsi28.png"));
        img.scaleToFit(25, 25);
        //img.setTransparency(new int[]{ 0xF0, 0xFF });
        img.setAbsolutePosition(35, 537);
        
        document.add(img);
        Paragraph pbc = new Paragraph("         Punjab Beverages",fontpbc);
        document.add(pbc);
        
        
	    
	    
	    
        int InstrumentCount = 0;
        ResultSet rs = s.executeQuery("select count(id) from gl_cash_instruments where is_visible_cash_receipt = 1 and is_internal = 0 order by id");
        while(rs.next()){
        	InstrumentCount = rs.getInt(1);
        }

	    int ColumnCount = InstrumentCount + 3;

		double AmountTotalSD1[] = new double[InstrumentCount];
		
		for (int i = 0; i < AmountTotalSD1.length; i++){
			AmountTotalSD1[i] = 0;								
		}	    
		double AmountTotalSD2[] = new double[InstrumentCount];
		
		for (int i = 0; i < AmountTotalSD2.length; i++){
			AmountTotalSD2[i] = 0;								
		}	    

		double AmountTotalB2B[] = new double[InstrumentCount];
		
		for (int i = 0; i < AmountTotalB2B.length; i++){
			AmountTotalB2B[i] = 0;								
		}	    

		double AmountTotalOther[] = new double[InstrumentCount];
		
		for (int i = 0; i < AmountTotalOther.length; i++){
			AmountTotalOther[i] = 0;								
		}	    
		
	    PdfPTable table = new PdfPTable(ColumnCount);
	    
	    table.setWidthPercentage(100f);
	    table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
    	
	    PdfPCell pcell = new PdfPCell(new Phrase("Cash Inflow Summary, Period: "+Utilities.getDisplayFullDateFormatShort(StartDate), reportheading5));
        pcell.setBackgroundColor(BaseColor.BLUE);
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.BLUE);
        pcell.setColspan(ColumnCount);
        pcell.setMinimumHeight(22);
        table.addCell(pcell);
	    
	    pcell = new PdfPCell(new Phrase("Customer", fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        pcell.setColspan(2);
        
        table.addCell(pcell);


        
        ResultSet rs2 = s.executeQuery("select id, short_label from gl_cash_instruments where is_visible_cash_receipt = 1 and is_internal = 0 order by id");
        while(rs2.next()){
        	table.addCell(getHeadingCell(rs2.getString(2)));
        }
        
        table.addCell(getHeadingCell("Total"));
        
	    
        PdfPCell pcellb = new PdfPCell(new Phrase("SD1", reportheading1));
        pcellb.setBackgroundColor(BaseColor.DARK_GRAY);
        pcellb.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcellb.setBorderColor(BaseColor.DARK_GRAY);
        pcellb.setMinimumHeight(20);
        pcellb.setColspan(ColumnCount);
        table.addCell(pcellb);
        
        
        ResultSet rs3 = s.executeQuery("select distributor_id,name from common_distributors where distributor_id in (select distinct(customer_id) from gl_cash_receipts where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(StartDate)+" ) and snd_id = "+EmployeeHierarchy.getSDHead(1).USER_ID);
        while(rs3.next()){
        	
        	long DistributorID = rs3.getLong(1);
        	String DistributorName = rs3.getString(2);
        	
    	    pcell = new PdfPCell(new Phrase(Utilities.truncateStringToMax(DistributorID+" - "+DistributorName, 25) , fontheading));
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorderColor(BaseColor.LIGHT_GRAY);
            pcell.setColspan(2);
        	table.addCell(pcell);
        	
        	double RowTotal = 0;
        	int ColumnIndex = -1;
        	ResultSet rs4 = s2.executeQuery("select id, short_label from gl_cash_instruments where is_visible_cash_receipt = 1 and is_internal = 0 order by id");
        	while(rs4.next()){
        		ColumnIndex++;
        		
        		int InstrumentID = rs4.getInt(1);
				double amount = 0;
				
				ResultSet rs5 = s3.executeQuery("SELECT glcr.id,glcr.customer_id,(select name from common_distributors cd where cd.distributor_id=glcr.customer_id) customer_name,glcr.receipt_type,glcr.created_on,"+
						" glcr.created_on_date,glcr.created_by,glcr.warehouse_id,glcri.instrument_id,"+
						" (select glci.label from gl_cash_instruments glci where glci.id=glcri.instrument_id) instrument_name,"+
						" amount, sum(amount) total_amount FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri"+
						" on glcr.id=glcri.id where glcri.instrument_id="+InstrumentID+" and glcr.customer_id="+DistributorID+" and created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(StartDate)+" group by glcri.instrument_id ");
				
				if(rs5.first()){
					amount = rs5.getDouble("total_amount");
					RowTotal += amount;
				}
				
				AmountTotalSD1[ColumnIndex] += amount;
				
				if (amount != 0){
					table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(amount)));	
				}else{
					table.addCell(getNormalCell(""));
				}
        		
        	}
        	
        	
        	table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(RowTotal)));
        }
	    pcell = new PdfPCell(new Phrase("Subtotal" , fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setColspan(2);
    	table.addCell(pcell);
    	
		double GrandTotalSD1 = 0;
		for(int x=0;x<AmountTotalSD1.length;x++){
			
			table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(AmountTotalSD1[x])));
			GrandTotalSD1 += AmountTotalSD1[x];
		}
        
		table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(GrandTotalSD1)));
        
		
        pcellb = new PdfPCell(new Phrase("SD2", reportheading1));
        pcellb.setBackgroundColor(BaseColor.DARK_GRAY);
        pcellb.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcellb.setBorderColor(BaseColor.DARK_GRAY);
        pcellb.setMinimumHeight(20);
        pcellb.setColspan(ColumnCount);
        table.addCell(pcellb);
		
        
        rs3 = s.executeQuery("select distributor_id,name from common_distributors where distributor_id in (select distinct(customer_id) from gl_cash_receipts where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(StartDate)+" ) and snd_id = "+EmployeeHierarchy.getSDHead(2).USER_ID);
        while(rs3.next()){
        	
        	long DistributorID = rs3.getLong(1);
        	String DistributorName = rs3.getString(2);
        	
    	    pcell = new PdfPCell(new Phrase(Utilities.truncateStringToMax(DistributorID+" - "+DistributorName, 25) , fontheading));
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorderColor(BaseColor.LIGHT_GRAY);
            pcell.setColspan(2);
        	table.addCell(pcell);
        	
        	double RowTotal = 0;
        	int ColumnIndex = -1;
        	ResultSet rs4 = s2.executeQuery("select id, short_label from gl_cash_instruments where is_visible_cash_receipt = 1 and is_internal = 0 order by id");
        	while(rs4.next()){
        		ColumnIndex++;
        		
        		int InstrumentID = rs4.getInt(1);
				double amount = 0;
				
				ResultSet rs5 = s3.executeQuery("SELECT glcr.id,glcr.customer_id,(select name from common_distributors cd where cd.distributor_id=glcr.customer_id) customer_name,glcr.receipt_type,glcr.created_on,"+
						" glcr.created_on_date,glcr.created_by,glcr.warehouse_id,glcri.instrument_id,"+
						" (select glci.label from gl_cash_instruments glci where glci.id=glcri.instrument_id) instrument_name,"+
						" amount, sum(amount) total_amount FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri"+
						" on glcr.id=glcri.id where glcri.instrument_id="+InstrumentID+" and glcr.customer_id="+DistributorID+" and created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(StartDate)+" group by glcri.instrument_id ");
				
				if(rs5.first()){
					amount = rs5.getDouble("total_amount");
					RowTotal += amount;
				}
				
				AmountTotalSD2[ColumnIndex] += amount;
				
				if (amount != 0){
					table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(amount)));	
				}else{
					table.addCell(getNormalCell(""));
				}
        		
        	}
        	
        	
        	table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(RowTotal)));
        }
	    pcell = new PdfPCell(new Phrase("Subtotal" , fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setColspan(2);
    	table.addCell(pcell);
    	
		double GrandTotalSD2 = 0;
		for(int x=0;x<AmountTotalSD2.length;x++){
			
			table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(AmountTotalSD2[x])));
			GrandTotalSD2 += AmountTotalSD2[x];
		}
        
		table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(GrandTotalSD2)));
        
		// B2B
        pcellb = new PdfPCell(new Phrase("SD3", reportheading1));
        pcellb.setBackgroundColor(BaseColor.DARK_GRAY);
        pcellb.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcellb.setBorderColor(BaseColor.DARK_GRAY);
        pcellb.setMinimumHeight(20);
        pcellb.setColspan(ColumnCount);
        table.addCell(pcellb);
		
        
        rs3 = s.executeQuery("select distributor_id,name from common_distributors where distributor_id in (select distinct(customer_id) from gl_cash_receipts where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(StartDate)+" ) and snd_id = "+EmployeeHierarchy.getSDHead(4).USER_ID);
        while(rs3.next()){
        	
        	long DistributorID = rs3.getLong(1);
        	String DistributorName = rs3.getString(2);
        	
    	    pcell = new PdfPCell(new Phrase(Utilities.truncateStringToMax(DistributorID+" - "+DistributorName, 25) , fontheading));
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorderColor(BaseColor.LIGHT_GRAY);
            pcell.setColspan(2);
        	table.addCell(pcell);
        	
        	double RowTotal = 0;
        	int ColumnIndex = -1;
        	ResultSet rs4 = s2.executeQuery("select id, short_label from gl_cash_instruments where is_visible_cash_receipt = 1 and is_internal = 0 order by id");
        	while(rs4.next()){
        		ColumnIndex++;
        		
        		int InstrumentID = rs4.getInt(1);
				double amount = 0;
				
				ResultSet rs5 = s3.executeQuery("SELECT glcr.id,glcr.customer_id,(select name from common_distributors cd where cd.distributor_id=glcr.customer_id) customer_name,glcr.receipt_type,glcr.created_on,"+
						" glcr.created_on_date,glcr.created_by,glcr.warehouse_id,glcri.instrument_id,"+
						" (select glci.label from gl_cash_instruments glci where glci.id=glcri.instrument_id) instrument_name,"+
						" amount, sum(amount) total_amount FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri"+
						" on glcr.id=glcri.id where glcri.instrument_id="+InstrumentID+" and glcr.customer_id="+DistributorID+" and created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(StartDate)+" group by glcri.instrument_id ");
				
				if(rs5.first()){
					amount = rs5.getDouble("total_amount");
					RowTotal += amount;
				}
				
				AmountTotalB2B[ColumnIndex] += amount;
				
				if (amount != 0){
					table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(amount)));	
				}else{
					table.addCell(getNormalCell(""));
				}
        		
        	}
        	
        	
        	table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(RowTotal)));
        }
	    pcell = new PdfPCell(new Phrase("Subtotal" , fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setColspan(2);
    	table.addCell(pcell);
    	
		double GrandTotalB2B = 0;
		for(int x=0;x<AmountTotalB2B.length;x++){
			
			table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(AmountTotalB2B[x])));
			GrandTotalB2B += AmountTotalB2B[x];
		}
        
		table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(GrandTotalB2B)));
		
		// other
        pcellb = new PdfPCell(new Phrase("Other Accounts", reportheading1));
        pcellb.setBackgroundColor(BaseColor.DARK_GRAY);
        pcellb.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcellb.setBorderColor(BaseColor.DARK_GRAY);
        pcellb.setMinimumHeight(20);
        pcellb.setColspan(ColumnCount);
        table.addCell(pcellb);
		
        
        
        
        
        rs3 = s.executeQuery("select distributor_id,name from common_distributors where distributor_id in (select distinct(customer_id) from gl_cash_receipts where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(StartDate)+" ) and (snd_id is null or snd_id not in ("+EmployeeHierarchy.getSDHead(1).USER_ID+", "+EmployeeHierarchy.getSDHead(2).USER_ID+", "+EmployeeHierarchy.getSDHead(4).USER_ID+"))");
        while(rs3.next()){
        	
        	long DistributorID = rs3.getLong(1);
        	String DistributorName = rs3.getString(2);
        	
    	    pcell = new PdfPCell(new Phrase(Utilities.truncateStringToMax(DistributorID+" - "+DistributorName, 25) , fontheading));
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorderColor(BaseColor.LIGHT_GRAY);
            pcell.setColspan(2);
        	table.addCell(pcell);
        	
        	double RowTotal = 0;
        	int ColumnIndex = -1;
        	ResultSet rs4 = s2.executeQuery("select id, short_label from gl_cash_instruments where is_visible_cash_receipt = 1 and is_internal = 0 order by id");
        	while(rs4.next()){
        		ColumnIndex++;
        		
        		int InstrumentID = rs4.getInt(1);
				double amount = 0;
				
				ResultSet rs5 = s3.executeQuery("SELECT glcr.id,glcr.customer_id,(select name from common_distributors cd where cd.distributor_id=glcr.customer_id) customer_name,glcr.receipt_type,glcr.created_on,"+
						" glcr.created_on_date,glcr.created_by,glcr.warehouse_id,glcri.instrument_id,"+
						" (select glci.label from gl_cash_instruments glci where glci.id=glcri.instrument_id) instrument_name,"+
						" amount, sum(amount) total_amount FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri"+
						" on glcr.id=glcri.id where glcri.instrument_id="+InstrumentID+" and glcr.customer_id="+DistributorID+" and created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(StartDate)+" group by glcri.instrument_id ");
				
				if(rs5.first()){
					amount = rs5.getDouble("total_amount");
					RowTotal += amount;
				}
				
				AmountTotalOther[ColumnIndex] += amount;
				
				if (amount != 0){
					table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(amount)));	
				}else{
					table.addCell(getNormalCell(""));
				}
        		
        	}
        	
        	
        	table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(RowTotal)));
        }
        
	    pcell = new PdfPCell(new Phrase("Subtotal" , fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setColspan(2);
    	table.addCell(pcell);
    	
		double GrandTotalOther = 0;
		for(int x=0;x<AmountTotalOther.length;x++){
			
			table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(AmountTotalOther[x])));
			GrandTotalOther += AmountTotalOther[x];
		}
		
		table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(GrandTotalOther)));
		
		
        pcellb = new PdfPCell(new Phrase("Total", reportheading4));
        pcellb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        pcellb.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcellb.setBorderColor(BaseColor.LIGHT_GRAY);
        pcellb.setMinimumHeight(20);
        pcellb.setColspan(2);
        table.addCell(pcellb);
		
    	
		double GrandTotal = 0;
		for(int x=0;x<AmountTotalOther.length;x++){
			
			double xtotal = AmountTotalSD1[x]+AmountTotalSD2[x]+AmountTotalB2B[x]+AmountTotalOther[x];
			table.addCell(getNormalCellTotal(Utilities.getDisplayCurrencyFormatRounded(xtotal)));
			GrandTotal += xtotal;
		}
        
		table.addCell(getNormalCellTotal(Utilities.getDisplayCurrencyFormatRounded(GrandTotal)));
		
		
        Paragraph header = new Paragraph("All Warehouses | "+Utilities.getDisplayDateFormat(StartDate),fonttitle);
        header.setAlignment(Element.ALIGN_RIGHT);

        double GrandTotalAll = GrandTotalSD1 + GrandTotalSD2 + GrandTotalB2B + GrandTotalOther;
        
        
        Paragraph footertext1 = new Paragraph("Total Inflow: "+Utilities.getDisplayCurrencyFormatRounded(GrandTotalAll),fonttitle);
        footertext1.setAlignment(Element.ALIGN_LEFT);
        
        
        document.add(header);
        document.add(Chunk.NEWLINE);
        
        document.add(table);
        
        document.add(Chunk.NEWLINE);
        document.add(footertext1);
        
        
        // Page 2
        
		double AmountTotal[] = new double[InstrumentCount];
		
		for (int i = 0; i < AmountTotal.length; i++){
			AmountTotal[i] = 0;								
		}	    
        
        document.newPage();
        
        
        document.add(img);
        document.add(pbc);
        
        
		double ColumnTotal[] = new double[InstrumentCount];
		
		for (int i = 0; i < ColumnTotal.length; i++){
			ColumnTotal[i] = 0;								
		}	    
		
		
        header = new Paragraph("All Warehouses | "+Utilities.getDisplayDateFullMonthYearFormat(StartDate),fonttitle);
        header.setAlignment(Element.ALIGN_RIGHT);

        document.add(header);
        document.add(Chunk.NEWLINE);
        
	    table = new PdfPTable(ColumnCount);
	    
	    table.setWidthPercentage(100f);
	    table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
    	
	    pcell = new PdfPCell(new Phrase("Cash Inflow Summary, Period: "+Utilities.getDisplayFullDateFormatShort(MonthToDateStartDate)+" - "+Utilities.getDisplayFullDateFormatShort(MonthToDateEndDate) , reportheading5));
        pcell.setBackgroundColor(BaseColor.BLUE);
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.BLUE);
        pcell.setColspan(ColumnCount);
        pcell.setMinimumHeight(22);
        table.addCell(pcell);
	    
	    pcell = new PdfPCell(new Phrase("Area", fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        pcell.setColspan(2);
        
        table.addCell(pcell);
        
        rs2 = s.executeQuery("select id, short_label from gl_cash_instruments where is_visible_cash_receipt = 1 and is_internal = 0 order by id");
        while(rs2.next()){
        	table.addCell(getHeadingCell(rs2.getString(2)));
        }
        
        table.addCell(getHeadingCell("Total"));
	    
        //rs3 = s.executeQuery("select distributor_id,name from common_distributors where distributor_id in (select distinct(customer_id) from gl_cash_receipts where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(StartDate)+" )");
        if (1 == 1){
    	    pcell = new PdfPCell(new Phrase("SD1", fontheading));
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorderColor(BaseColor.LIGHT_GRAY);
            pcell.setColspan(2);
        	table.addCell(pcell);
        	
        	double RowTotal = 0;
        	int ColumnIndex = -1;
        	ResultSet rs4 = s2.executeQuery("select id, short_label from gl_cash_instruments where is_visible_cash_receipt = 1 and is_internal = 0 order by id");
        	while(rs4.next()){
        		ColumnIndex++;
        		
        		int InstrumentID = rs4.getInt(1);
				double amount = 0;
				
				ResultSet rs5 = s3.executeQuery("SELECT glcr.id,glcr.receipt_type,glcr.created_on,"+
						" glcr.created_on_date,glcr.created_by,glcr.warehouse_id,glcri.instrument_id,"+
						" (select glci.label from gl_cash_instruments glci where glci.id=glcri.instrument_id) instrument_name,"+
						" amount, sum(amount) total_amount FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri"+
						" on glcr.id=glcri.id where glcri.instrument_id="+InstrumentID+" and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate)+" and customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(1).USER_ID+") group by glcri.instrument_id ");
				
				if(rs5.first()){
					amount = rs5.getDouble("total_amount");
					RowTotal += amount;
				}
				
				ColumnTotal[ColumnIndex] += amount;
				
				if (amount != 0){
					table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(amount)));	
				}else{
					table.addCell(getNormalCell(""));
				}
				
        	}
        	
        	table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(RowTotal)));
        }

        if (1 == 1){
    	    pcell = new PdfPCell(new Phrase("SD2", fontheading));
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorderColor(BaseColor.LIGHT_GRAY);
            pcell.setColspan(2);
        	table.addCell(pcell);
        	
        	double RowTotal = 0;
        	int ColumnIndex = -1;
        	ResultSet rs4 = s2.executeQuery("select id, short_label from gl_cash_instruments where is_visible_cash_receipt = 1 and is_internal = 0 order by id");
        	while(rs4.next()){
        		ColumnIndex++;
        		
        		int InstrumentID = rs4.getInt(1);
				double amount = 0;
				
				ResultSet rs5 = s3.executeQuery("SELECT glcr.id,glcr.receipt_type,glcr.created_on,"+
						" glcr.created_on_date,glcr.created_by,glcr.warehouse_id,glcri.instrument_id,"+
						" (select glci.label from gl_cash_instruments glci where glci.id=glcri.instrument_id) instrument_name,"+
						" amount, sum(amount) total_amount FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri"+
						" on glcr.id=glcri.id where glcri.instrument_id="+InstrumentID+" and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate)+" and customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(2).USER_ID+") group by glcri.instrument_id ");
				
				if(rs5.first()){
					amount = rs5.getDouble("total_amount");
					RowTotal += amount;
				}
				
				ColumnTotal[ColumnIndex] += amount;
				
				if (amount != 0){
					table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(amount)));	
				}else{
					table.addCell(getNormalCell(""));
				}
				
        	}
        	
        	table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(RowTotal)));
        }

        
        if (1 == 1){
    	    pcell = new PdfPCell(new Phrase("SD3", fontheading));
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorderColor(BaseColor.LIGHT_GRAY);
            pcell.setColspan(2);
        	table.addCell(pcell);
        	
        	double RowTotal = 0;
        	int ColumnIndex = -1;
        	ResultSet rs4 = s2.executeQuery("select id, short_label from gl_cash_instruments where is_visible_cash_receipt = 1 and is_internal = 0 order by id");
        	while(rs4.next()){
        		ColumnIndex++;
        		
        		int InstrumentID = rs4.getInt(1);
				double amount = 0;
				
				ResultSet rs5 = s3.executeQuery("SELECT glcr.id,glcr.receipt_type,glcr.created_on,"+
						" glcr.created_on_date,glcr.created_by,glcr.warehouse_id,glcri.instrument_id,"+
						" (select glci.label from gl_cash_instruments glci where glci.id=glcri.instrument_id) instrument_name,"+
						" amount, sum(amount) total_amount FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri"+
						" on glcr.id=glcri.id where glcri.instrument_id="+InstrumentID+" and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate)+" and customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(4).USER_ID+") group by glcri.instrument_id ");
				
				if(rs5.first()){
					amount = rs5.getDouble("total_amount");
					RowTotal += amount;
				}
				
				ColumnTotal[ColumnIndex] += amount;
				
				if (amount != 0){
					table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(amount)));	
				}else{
					table.addCell(getNormalCell(""));
				}
				
        	}
        	
        	table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(RowTotal)));
        }

        if (1 == 1){
    	    pcell = new PdfPCell(new Phrase("Other Accounts", fontheading));
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorderColor(BaseColor.LIGHT_GRAY);
            pcell.setColspan(2);
        	table.addCell(pcell);
        	
        	double RowTotal = 0;
        	int ColumnIndex = -1;
        	ResultSet rs4 = s2.executeQuery("select id, short_label from gl_cash_instruments where is_visible_cash_receipt = 1 and is_internal = 0 order by id");
        	while(rs4.next()){
        		ColumnIndex++;
        		
        		int InstrumentID = rs4.getInt(1);
				double amount = 0;
				
				ResultSet rs5 = s3.executeQuery("SELECT glcr.id,glcr.receipt_type,glcr.created_on,"+
						" glcr.created_on_date,glcr.created_by,glcr.warehouse_id,glcri.instrument_id,"+
						" (select glci.label from gl_cash_instruments glci where glci.id=glcri.instrument_id) instrument_name,"+
						" amount, sum(amount) total_amount FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri"+
						" on glcr.id=glcri.id where glcri.instrument_id="+InstrumentID+" and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate)+" and customer_id in (select distributor_id from common_distributors where (snd_id is null or snd_id not in ("+EmployeeHierarchy.getSDHead(1).USER_ID+", "+EmployeeHierarchy.getSDHead(2).USER_ID+", "+EmployeeHierarchy.getSDHead(4).USER_ID+"))) group by glcri.instrument_id ");
				
				if(rs5.first()){
					amount = rs5.getDouble("total_amount");
					RowTotal += amount;
				}
				
				ColumnTotal[ColumnIndex] += amount;
				
				if (amount != 0){
					table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(amount)));	
				}else{
					table.addCell(getNormalCell(""));
				}
				
        	}
        	
        	table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(RowTotal)));
        }
        
        pcellb = new PdfPCell(new Phrase("Total", reportheading4));
        pcellb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        pcellb.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcellb.setBorderColor(BaseColor.LIGHT_GRAY);
        pcellb.setMinimumHeight(20);
        pcellb.setColspan(2);
        table.addCell(pcellb);
    	
		GrandTotal = 0;
		for(int x=0;x<ColumnTotal.length;x++){
			
			table.addCell(getNormalCellTotal(Utilities.getDisplayCurrencyFormatRounded(ColumnTotal[x])));
			GrandTotal += ColumnTotal[x];
		}
        
		table.addCell(getNormalCellTotal(Utilities.getDisplayCurrencyFormatRounded(GrandTotal)));
		
	    document.add(table);
	    
	    Paragraph footertext = new Paragraph("Total Inflow: "+Utilities.getDisplayCurrencyFormatRounded(GrandTotal),fonttitle);
        footertext.setAlignment(Element.ALIGN_LEFT);

        document.add(Chunk.NEWLINE);
        document.add(footertext);
        
	     
        
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("* This report does not take effect of dishonored or post-dated instruments.",reportheading4));


        // This Month
		ResultSet rs6 = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(1).USER_ID+") and glci.is_internal = 0 ");
		if(rs6.first()){
			MTDSD1 = rs6.getDouble(1);
		}
		ResultSet rs7 = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(2).USER_ID+") and glci.is_internal = 0 ");
		if(rs7.first()){
			MTDSD2 = rs7.getDouble(1);
		}
		ResultSet rs8 = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(4).USER_ID+") and glci.is_internal = 0 ");
		if(rs8.first()){
			MTDB2B = rs8.getDouble(1);
		}
		ResultSet rs9 = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate)+" and glcr.customer_id in (select distributor_id from common_distributors where (snd_id is null or snd_id not in ("+EmployeeHierarchy.getSDHead(1).USER_ID+", "+EmployeeHierarchy.getSDHead(2).USER_ID+", "+EmployeeHierarchy.getSDHead(4).USER_ID+"))) and glci.is_internal = 0 ");
		if(rs9.first()){
			MTDOther = rs9.getDouble(1);
		}
		
		MTDTotal = MTDSD1 + MTDSD2 + MTDB2B + MTDOther;

		// MTD Last Year
		ResultSet rs6LY = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDateLastYear)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(1).USER_ID+") and glci.is_internal = 0 ");
		if(rs6LY.first()){
			MTDSD1LY = rs6LY.getDouble(1);
		}
		ResultSet rs7LY = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDateLastYear)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(2).USER_ID+") and glci.is_internal = 0 ");
		if(rs7LY.first()){
			MTDSD2LY = rs7LY.getDouble(1);
		}
		ResultSet rs8LY = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDateLastYear)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(4).USER_ID+") and glci.is_internal = 0 ");
		if(rs8LY.first()){
			MTDB2BLY = rs8LY.getDouble(1);
		}
		ResultSet rs9LY = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDateLastYear)+" and glcr.customer_id in (select distributor_id from common_distributors where ((snd_id is null or snd_id not in ("+EmployeeHierarchy.getSDHead(1).USER_ID+", "+EmployeeHierarchy.getSDHead(2).USER_ID+", "+EmployeeHierarchy.getSDHead(4).USER_ID+")))) and glci.is_internal = 0 ");
		if(rs9LY.first()){
			MTDOtherLY = rs9LY.getDouble(1);
		}
		
		MTDTotalLY = MTDSD1LY + MTDSD2LY + MTDB2BLY + MTDOtherLY;

		// MTD Last Year FULL
		ResultSet rs6LY_FULL = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDateLastYear)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(1).USER_ID+") and glci.is_internal = 0 ");
		if(rs6LY_FULL.first()){
			MTDSD1LY_FULL = rs6LY_FULL.getDouble(1);
		}
		ResultSet rs7LY_FULL = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDateLastYear)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(2).USER_ID+") and glci.is_internal = 0 ");
		if(rs7LY_FULL.first()){
			MTDSD2LY_FULL = rs7LY_FULL.getDouble(1);
		}
		ResultSet rs8LY_FULL = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDateLastYear)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(4).USER_ID+") and glci.is_internal = 0 ");
		if(rs8LY_FULL.first()){
			MTDB2BLY_FULL = rs8LY_FULL.getDouble(1);
		}
		ResultSet rs9LY_FULL = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDateLastYear)+" and glcr.customer_id in (select distributor_id from common_distributors where ((snd_id is null or snd_id not in ("+EmployeeHierarchy.getSDHead(1).USER_ID+", "+EmployeeHierarchy.getSDHead(2).USER_ID+", "+EmployeeHierarchy.getSDHead(4).USER_ID+")))) and glci.is_internal = 0 ");
		if(rs9LY_FULL.first()){
			MTDOtherLY_FULL = rs9LY_FULL.getDouble(1);
		}
		
		MTDTotalLY_FULL = MTDSD1LY_FULL + MTDSD2LY_FULL + MTDB2BLY_FULL + MTDOtherLY_FULL;
		
		// This Year
		ResultSet rs10 = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YearStartDate)+" and "+Utilities.getSQLDateNextLifting(YearEndDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(1).USER_ID+") and glci.is_internal = 0 ");
		if(rs10.first()){
			YTDSD1 = rs10.getDouble(1);
		}
		ResultSet rs11 = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YearStartDate)+" and "+Utilities.getSQLDateNextLifting(YearEndDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(2).USER_ID+") and glci.is_internal = 0 ");
		if(rs11.first()){
			YTDSD2 = rs11.getDouble(1);
		}
		ResultSet rs12 = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YearStartDate)+" and "+Utilities.getSQLDateNextLifting(YearEndDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(4).USER_ID+") and glci.is_internal = 0 ");
		if(rs12.first()){
			YTDB2B = rs12.getDouble(1);
		}
		ResultSet rs13 = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YearStartDate)+" and "+Utilities.getSQLDateNextLifting(YearEndDate)+" and glcr.customer_id in (select distributor_id from common_distributors where (snd_id is null or snd_id not in ("+EmployeeHierarchy.getSDHead(1).USER_ID+", "+EmployeeHierarchy.getSDHead(2).USER_ID+", "+EmployeeHierarchy.getSDHead(4).USER_ID+"))) and glci.is_internal = 0 ");
		if(rs13.first()){
			YTDOther = rs13.getDouble(1);
		}
		
		YTDTotal = YTDSD1 + YTDSD2 + YTDB2B + YTDOther;

		// YTD Last Year
		ResultSet rs10LY = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YearStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(YearEndDateLastYear)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(1).USER_ID+") and glci.is_internal = 0 ");
		if(rs10LY.first()){
			YTDSD1LY = rs10LY.getDouble(1);
		}
		ResultSet rs11LY = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YearStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(YearEndDateLastYear)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(2).USER_ID+") and glci.is_internal = 0 ");
		if(rs11LY.first()){
			YTDSD2LY = rs11LY.getDouble(1);
		}
		ResultSet rs12LY = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YearStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(YearEndDateLastYear)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(4).USER_ID+") and glci.is_internal = 0 ");
		if(rs12LY.first()){
			YTDB2BLY = rs12LY.getDouble(1);
		}
		ResultSet rs13LY = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YearStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(YearEndDateLastYear)+" and glcr.customer_id in (select distributor_id from common_distributors where ((snd_id is null or snd_id not in ("+EmployeeHierarchy.getSDHead(1).USER_ID+", "+EmployeeHierarchy.getSDHead(2).USER_ID+", "+EmployeeHierarchy.getSDHead(4).USER_ID+")))) and glci.is_internal = 0 ");
		if(rs13LY.first()){
			YTDOtherLY = rs13LY.getDouble(1);
		}
		
		YTDTotalLY = YTDSD1LY + YTDSD2LY + YTDB2BLY + YTDOtherLY;
		
		// YTD Last Year FULL
		ResultSet rs10LY_FULL = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YearStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(YearEndDateLastYear)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(1).USER_ID+") and glci.is_internal = 0 ");
		if(rs10LY_FULL.first()){
			YTDSD1LY_FULL = rs10LY_FULL.getDouble(1);
		}
		ResultSet rs11LY_FULL = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YearStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(YearEndDateLastYear)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(2).USER_ID+") and glci.is_internal = 0 ");
		if(rs11LY_FULL.first()){
			YTDSD2LY_FULL = rs11LY_FULL.getDouble(1);
		}
		ResultSet rs12LY_FULL = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YearStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(YearEndDateLastYear)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(4).USER_ID+") and glci.is_internal = 0 ");
		if(rs12LY_FULL.first()){
			YTDB2BLY_FULL = rs12LY_FULL.getDouble(1);
		}
		ResultSet rs13LY_FULL = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YearStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(YearEndDateLastYear)+" and glcr.customer_id in (select distributor_id from common_distributors where ((snd_id is null or snd_id not in ("+EmployeeHierarchy.getSDHead(1).USER_ID+", "+EmployeeHierarchy.getSDHead(2).USER_ID+", "+EmployeeHierarchy.getSDHead(4).USER_ID+"))) ) and glci.is_internal = 0 ");
		if(rs13LY_FULL.first()){
			YTDOtherLY_FULL = rs13LY_FULL.getDouble(1);
		}
		
		YTDTotalLY_FULL = YTDSD1LY_FULL + YTDSD2LY_FULL + YTDB2BLY_FULL + YTDOtherLY_FULL;		
		
		if (YTDSD1LY != 0){
			YTDPSD1 = ((YTDSD1 - YTDSD1LY) / YTDSD1LY) * 100;
		}
		if (YTDSD2LY != 0){
			YTDPSD2 = ((YTDSD2 - YTDSD2LY) / YTDSD2LY) * 100;
		}
		if (YTDB2BLY != 0){
			YTDPB2B = ((YTDB2B - YTDB2BLY) / YTDB2BLY) * 100;
		}
		if (YTDOtherLY != 0){
			YTDPOther = ((YTDOther - YTDOtherLY) / YTDOtherLY) * 100;
		}
		if (YTDTotalLY != 0){
			YTDPTotal = ((YTDTotal - YTDTotalLY) / YTDTotalLY) * 100;
		}

		// YTD P FULL
		if (YTDSD1LY_FULL != 0){
			YTDPSD1_FULL = ((YTDSD1 - YTDSD1LY_FULL) / YTDSD1LY_FULL) * 100;
		}
		if (YTDSD2LY_FULL != 0){
			YTDPSD2_FULL = ((YTDSD2 - YTDSD2LY_FULL) / YTDSD2LY_FULL) * 100;
		}
		if (YTDB2BLY_FULL != 0){
			YTDPB2B_FULL = ((YTDB2B - YTDB2BLY_FULL) / YTDB2BLY_FULL) * 100;
		}
		if (YTDOtherLY_FULL != 0){
			YTDPOther_FULL = ((YTDOther - YTDOtherLY_FULL) / YTDOtherLY_FULL) * 100;
		}
		if (YTDTotalLY_FULL != 0){
			YTDPTotal_FULL = ((YTDTotal - YTDTotalLY_FULL) / YTDTotalLY_FULL) * 100;
		}
		
		
		if (MTDSD1LY != 0){
			MTDPSD1 = ((MTDSD1 - MTDSD1LY) / MTDSD1LY) * 100;
		}
		if (MTDSD2LY != 0){
			MTDPSD2 = ((MTDSD2 - MTDSD2LY) / MTDSD2LY) * 100;
		}
		if (MTDB2BLY != 0){
			MTDPB2B = ((MTDB2B - MTDB2BLY) / MTDB2BLY) * 100;
		}
		if (MTDOtherLY != 0){
			MTDPOther = ((MTDOther - MTDOtherLY) / MTDOtherLY) * 100;
		}
		if (MTDTotalLY != 0){
			MTDPTotal = ((MTDTotal - MTDTotalLY) / MTDTotalLY) * 100;
		}
		
		// MTD P FULL
		if (MTDSD1LY_FULL != 0){
			MTDPSD1_FULL = ((MTDSD1 - MTDSD1LY_FULL) / MTDSD1LY_FULL) * 100;
		}
		if (MTDSD2LY_FULL != 0){
			MTDPSD2_FULL = ((MTDSD2 - MTDSD2LY_FULL) / MTDSD2LY_FULL) * 100;
		}
		if (MTDB2BLY_FULL != 0){
			MTDPB2B_FULL = ((MTDB2B - MTDB2BLY_FULL) / MTDB2BLY_FULL) * 100;
		}
		if (MTDOtherLY_FULL != 0){
			MTDPOther_FULL = ((MTDOther - MTDOtherLY_FULL) / MTDOtherLY_FULL) * 100;
		}
		if (MTDTotalLY_FULL != 0){
			MTDPTotal_FULL = ((MTDTotal - MTDTotalLY_FULL) / MTDTotalLY_FULL) * 100;
		}
		
		
		
		Date iDate = YearStartDate;
		while((iDate.before(DateUtils.addDays(YearEndDate, 1)))){
			Date iDateLY = DateUtils.addYears(iDate, -1);
			
			double AmountTY = 0;
			ResultSet rsc = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YearStartDate)+" and "+Utilities.getSQLDateNextLifting(iDate)+" and glci.is_internal = 0 ");
			if(rsc.first()){
				AmountTY = rsc.getDouble(1);
			}

			double AmountLY = 0;
			ResultSet rscLY = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YearStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(iDateLY)+" and glci.is_internal = 0 ");
			if(rscLY.first()){
				AmountLY = rscLY.getDouble(1);
			}
			
			if (YTDSeries.length() != 0){
				YTDSeries = YTDSeries + ",";
			}
			if (YTDSeriesLY.length() != 0){
				YTDSeriesLY = YTDSeriesLY + ",";
			}
			
			YTDSeries = YTDSeries + Math.round(AmountTY / 1000000);
			YTDSeriesLY = YTDSeriesLY + Math.round(AmountLY / 1000000);
			
			//System.out.println(Utilities.getDisplayCurrencyFormat(AmountLY));
			//System.out.println(iDate + " " +iDateLY);
			
			
			
			iDate = DateUtils.addDays(iDate, 1);
		}
		
		
		setAdvances();
		
		
		
        setHTMLMessage(GrandTotalSD1,GrandTotalSD2,GrandTotalB2B,GrandTotalOther,GrandTotalAll);
        
	    document.close();
	    
	    
	}
	
	public void setAdvances() throws SQLException{
		
		
		ResultSet rs2 = s2.executeQuery("select id, short_name, snd_id from common_sd_groups where id in (1,2,4,100) order by id");
		while(rs2.next()){
			
			long SND_ID = rs2.getLong(3);
			int SDID = rs2.getInt(1);
			
			String where = "";
			if (SND_ID != 0){
				where = " snd_id =  "+SND_ID;
			}else{
				where = " (snd_id is null or snd_id not in (select snd_id from common_sd_groups where snd_id is not null)) ";
			}
			
			ResultSet rs = s.executeQuery("select sum(ledger_balance) from ("+
					"SELECT cd.distributor_id, cd.name, (select ifnull(sum(debit),0) - ifnull(sum(credit),0) from gl_transactions_accounts where account_id in (select id from gl_accounts where customer_id = cd.distributor_id and type_id = 1)) ledger_balance FROM pep.common_distributors cd where "+where+" having ledger_balance < 0 "+
			") tab1");
			if (rs.first()){
				
				if (SDID == 1){
					AdvanceSD1 = rs.getDouble(1)*-1;
				}
				if (SDID == 2){
					AdvanceSD2 = rs.getDouble(1)*-1;
				}
				if (SDID == 4){
					AdvanceB2B = rs.getDouble(1)*-1;
				}
				if (SDID == 100){
					AdvanceOther = rs.getDouble(1)*-1;
				}
				
			}
			/*
			 * SELECT sum(gop.order_amount) FROM gl_order_posting gop where gop.customer_id = cd.distributor_id and gop.is_invoiced = 0 and gop.order_no not in (select sap_order_no from inventory_delivery_note)
			 * 
			 * */
			
			
			ResultSet rs3 = s.executeQuery("SELECT sum(invoice_amount) FROM gl_invoice_posting gip join common_distributors cd on gip.customer_id = cd.distributor_id where "+where+" and gip.order_no not in (select sap_order_no from inventory_delivery_note)");
			if (rs3.first()){
				if (SDID == 1){
					NotLiftedSD1 = rs3.getDouble(1);
				}
				if (SDID == 2){
					NotLiftedSD2 = rs3.getDouble(1);
				}
				if (SDID == 4){
					NotLiftedB2B = rs3.getDouble(1);
				}
				if (SDID == 100){
					NotLiftedOther = rs3.getDouble(1);
				}
			}

			
			ResultSet rs4 = s.executeQuery("SELECT sum(gop.order_amount) FROM gl_order_posting gop join common_distributors cd on gop.customer_id = cd.distributor_id where "+where+" and gop.is_invoiced = 0 and gop.order_no not in (select sap_order_no from inventory_delivery_note)");
			if (rs4.first()){
				if (SDID == 1){
					NotLiftedSD1 += rs4.getDouble(1);
				}
				if (SDID == 2){
					NotLiftedSD2 += rs4.getDouble(1);
				}
				if (SDID == 4){
					NotLiftedB2B += rs4.getDouble(1);
				}
				if (SDID == 100){
					NotLiftedOther += rs4.getDouble(1);
				}
			}
			
			
			
		}
		
	}
	
	private PdfPCell getHeadingCell(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setBackgroundColor(new BaseColor(222, 222, 222));
        pcell.setMinimumHeight(16);
        return pcell;
	}
	private PdfPCell getNormalCell(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        return pcell;
	}
	
	private PdfPCell getNormalCellTotal(String title){
		
	    PdfPCell pcellb = new PdfPCell(new Phrase(title, reportheading4));
	    
        pcellb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        pcellb.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcellb.setBorderColor(BaseColor.LIGHT_GRAY);
        pcellb.setMinimumHeight(20);
	    
        return pcellb;
	}
	public void setHTMLMessage(double SD1, double SD2, double B2B, double other, double total){
		
		Date ReportDate = Utilities.getDateByDays(-1);
		
    	Calendar cc = Calendar.getInstance();
    	cc.setTime(ReportDate);
    	
    	int year = cc.get(Calendar.YEAR);
    	int LastYear = year-1;

		
		
		String html = "<html>";
		html += "<body><br>";
			html += "<table border='0' style='border-collapse: collapse; border:1px solid #dee0e1;width:600px;font-size: 14px;'>";
			
				html += "<tr>";
					html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: normal;' colspan='8'>Cash / Bank Inflow</td>";
				html += "</tr>";
		
				html += "<tr>";
					html += "<td style='background-color: #C8CFE6; width: 70px; border:1px solid #dee0e1; height: 18px; text-align: left; font-weight: normal;' rowspan='2' valign='middle'>Area</td>";
					html += "<td style='background-color: #DEE0E3; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;' rowspan='2' valign='middle'>Today</td>";
					html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;' colspan='2'>MTD</td>";
					html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center;'>Chg</td>";
					html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;' colspan='2'>YTD</td>";
					html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center;'>Chg</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center;'>"+LastYear+"</td>";
				html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center;'>"+year+"</td>";
				html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center;'>(%)</td>";
				html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center;'>"+LastYear+"</td>";
				html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center;'>"+year+"</td>";
				html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center;'>(%)</td>";
				html += "</tr>";
				
				html += "<tr>";
					html += "<td style='border:1px solid #c2c3c3;  text-align: left;font-weight: normal; background-color: #D5DBED'>SD1";
					html += "<td style='border:1px solid #c2c3c3;text-align: right;background-color: #EDEFF2;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(SD1);
					html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDSD1LY);
					html += "<td style='border:1px solid #c2c3c3;text-align: right;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDSD1);
					html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(MTDPSD1)+";'>"+Utilities.getDisplayCurrencyFormatRounded(MTDPSD1)+"%";
					html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDSD1LY);
					html += "<td style='border:1px solid #c2c3c3;text-align: right;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDSD1);
					html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(YTDPSD1)+";'>"+Utilities.getDisplayCurrencyFormatRounded(YTDPSD1)+"%";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td style='border:1px solid #c2c3c3;  text-align: left;font-weight: normal; background-color: #D5DBED'>SD2";
				html += "<td style='border:1px solid #c2c3c3;text-align: right;background-color: #EDEFF2;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(SD2);
				html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDSD2LY);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDSD2);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(MTDPSD2)+";'>"+Utilities.getDisplayCurrencyFormatRounded(MTDPSD2)+"%";
				html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDSD2LY);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDSD2);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(YTDPSD2)+";'>"+Utilities.getDisplayCurrencyFormatRounded(YTDPSD2)+"%";
				
				html += "</tr>";
			
				html += "<tr>";
				html += "<td style='border:1px solid #c2c3c3;  text-align: left;font-weight: normal; background-color: #D5DBED'>SD3";
				html += "<td style='border:1px solid #c2c3c3;text-align: right;background-color: #EDEFF2;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(B2B);
				html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDB2BLY);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDB2B);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(MTDPB2B)+";'>"+Utilities.getDisplayCurrencyFormatRounded(MTDPB2B)+"%";
				html += "<td style='border:1px solid #c2c3c3;text-align: right;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDB2BLY);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDB2B);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(YTDPB2B)+";'>"+Utilities.getDisplayCurrencyFormatRounded(YTDPB2B)+"%";
				html += "</tr>";
			
				html += "<tr>";
				html += "<td style='border:1px solid #c2c3c3; text-align: left;font-weight: normal; background-color: #D5DBED'>Other";
				html += "<td style='border:1px solid #c2c3c3;text-align: right;background-color: #EDEFF2;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(other);
				html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDOtherLY);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDOther);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(MTDPOther)+";'>"+Utilities.getDisplayCurrencyFormatRounded(MTDPOther)+"%";
				html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDOtherLY);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDOther);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(YTDPOther)+";'>"+Utilities.getDisplayCurrencyFormatRounded(YTDPOther)+"%";
				html += "</tr>";

				
				html += "<tr>";
				html += "<td style='border:1px solid #c2c3c3; text-align: left;font-weight: normal; background-color: #D5DBED'>Total";
				html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal;background-color: #EDEFF2;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(total);
				html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDTotalLY);
				html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDTotal);
				html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal; background-color: "+getColor(MTDPTotal)+"'>"+Utilities.getDisplayCurrencyFormatRounded(MTDPTotal)+"%";
				html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDTotalLY);
				html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDTotal);
				html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal; background-color: "+getColor(YTDPTotal)+";'>"+Utilities.getDisplayCurrencyFormatRounded(YTDPTotal)+"%";
				
				
				
				html += "</tr>";
				/*
				html += "</table>";
				html += "<br>";
				html += "<table border='0' style='border-collapse: collapse; border:1px solid #dee0e1;width:600px;font-size: 14px;'>";
				
				html += "<tr>";
				html += "<td style='color: white; height: 18px; text-align: center; font-weight: normal;' colspan='8'>&nbsp;</td>";
			html += "</tr>";
			*/
			/*
			html += "<tr>";
			html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: normal;' colspan='8'>Cash / Bank Inflow (100% Territory) </td>";
		html += "</tr>";
	
			html += "<tr>";
				html += "<td style='background-color: #C8CFE6; border:1px solid #dee0e1; height: 18px; text-align: left; font-weight: normal;' rowspan='2' valign='middle' colspan='2'>Area</td>";
				//html += "<td style='background-color: #DEE0E3; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;' rowspan='2' valign='middle'>Today</td>";
				html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;' colspan='2'>MTD</td>";
				html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center;'>Chg</td>";
				html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center; font-weight: bold;' colspan='2'>YTD</td>";
				html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center;'>Chg</td>";
			html += "</tr>";
			html += "<tr>";
			html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center;'>"+LastYear+"</td>";
			html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center;'>"+year+"</td>";
			html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center;'>(%)</td>";
			html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center;'>"+LastYear+"</td>";
			html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center;'>"+year+"</td>";
			html += "<td style='background-color: #C8CFE6; height: 18px; border:1px solid #dee0e1;text-align:center;'>(%)</td>";
			html += "</tr>";
			
			html += "<tr>";
				html += "<td style='border:1px solid #c2c3c3;  text-align: left;font-weight: normal; background-color: #D5DBED' colspan='2'>SD1";
				//html += "<td style='border:1px solid #c2c3c3;text-align: right;background-color: #EDEFF2;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(SD1);
				html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDSD1LY_FULL);
				html += "<td style='border:1px solid #c2c3c3;text-align: right;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDSD1);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(MTDPSD1_FULL)+";'>"+Utilities.getDisplayCurrencyFormatRounded(MTDPSD1_FULL)+"%";
				html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDSD1LY_FULL);
				html += "<td style='border:1px solid #c2c3c3;text-align: right;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDSD1);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(YTDPSD1_FULL)+";'>"+Utilities.getDisplayCurrencyFormatRounded(YTDPSD1_FULL)+"%";
			html += "</tr>";
			
			html += "<tr>";
			html += "<td style='border:1px solid #c2c3c3;  text-align: left;font-weight: normal; background-color: #D5DBED' colspan='2'>SD2";
			//html += "<td style='border:1px solid #c2c3c3;text-align: right;background-color: #EDEFF2;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(SD2);
			html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDSD2LY_FULL);
			html += "<td style='border:1px solid #c2c3c3;text-align: right; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDSD2);
			html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(MTDPSD2_FULL)+";'>"+Utilities.getDisplayCurrencyFormatRounded(MTDPSD2_FULL)+"%";
			html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDSD2LY_FULL);
			html += "<td style='border:1px solid #c2c3c3;text-align: right; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDSD2);
			html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(YTDPSD2_FULL)+";'>"+Utilities.getDisplayCurrencyFormatRounded(YTDPSD2_FULL)+"%";
			
			html += "</tr>";
		
			html += "<tr>";
			html += "<td style='border:1px solid #c2c3c3;  text-align: left;font-weight: normal; background-color: #D5DBED' colspan='2'>SD3";
			//html += "<td style='border:1px solid #c2c3c3;text-align: right;background-color: #EDEFF2;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(B2B);
			html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDB2BLY_FULL);
			html += "<td style='border:1px solid #c2c3c3;text-align: right; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDB2B);
			html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(MTDPB2B_FULL)+";'>"+Utilities.getDisplayCurrencyFormatRounded(MTDPB2B_FULL)+"%";
			html += "<td style='border:1px solid #c2c3c3;text-align: right;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDB2BLY_FULL);
			html += "<td style='border:1px solid #c2c3c3;text-align: right; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDB2B);
			html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(YTDPB2B_FULL)+";'>"+Utilities.getDisplayCurrencyFormatRounded(YTDPB2B_FULL)+"%";
			html += "</tr>";
		
			html += "<tr>";
			html += "<td style='border:1px solid #c2c3c3; text-align: left;font-weight: normal; background-color: #D5DBED' colspan='2'>Other";
			//html += "<td style='border:1px solid #c2c3c3;text-align: right;background-color: #EDEFF2;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(other);
			html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDOtherLY_FULL);
			html += "<td style='border:1px solid #c2c3c3;text-align: right; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDOther);
			html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(MTDPOther_FULL)+";'>"+Utilities.getDisplayCurrencyFormatRounded(MTDPOther_FULL)+"%";
			html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDOtherLY_FULL);
			html += "<td style='border:1px solid #c2c3c3;text-align: right; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDOther);
			html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(YTDPOther_FULL)+";'>"+Utilities.getDisplayCurrencyFormatRounded(YTDPOther_FULL)+"%";
			html += "</tr>";

			
			html += "<tr>";
			html += "<td style='border:1px solid #c2c3c3; text-align: left;font-weight: normal; background-color: #D5DBED' colspan='2'>Total";
			//html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal;background-color: #EDEFF2;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(total);
			html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDTotalLY_FULL);
			html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDTotal);
			html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal; background-color: "+getColor(MTDPTotal_FULL)+"'>"+Utilities.getDisplayCurrencyFormatRounded(MTDPTotal_FULL)+"%";
			html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDTotalLY_FULL);
			html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDTotal);
			html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal; background-color: "+getColor(YTDPTotal_FULL)+";'>"+Utilities.getDisplayCurrencyFormatRounded(YTDPTotal_FULL)+"%";
			
			
			
			html += "</tr>";
*/
			html += "</table>";
							
				html +="<br><table style='font-size: 14px;'><tr><td style='width:20px; hight: 20px; background-color:#cbe9aa'></td><td>Increased w.r.t. last year</td></tr><tr><td style='width:20px; hight: 20px; background-color:#e79ca8'></td><td>Decreased w.r.t. last year</td></tr></table>";
				html += "<br>";
				
				html += "<table border='0' style='border-collapse: collapse; border:0px solid #dee0e1;width:600px;font-size: 14px;'>";
				
				html += "<tr>";
					html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: normal;'>YTD Cash / Bank Inflow</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td><img src='https://chart.googleapis.com/chart?cht=lc&chd=t:"+YTDSeriesLY+"|"+YTDSeries+"&chds=a&chs=595x250&chl=Jan%201|Today&chxt=y,r&chco=bda609,0963bd,f0516c&chdl="+LastYear+"|"+year+"&chdlp=b&chtt=(millions)'></td>";
				html += "</tr>";
				html += "</table>";
				
				html += "<br>";
				html += "<br>";
				//
				
				html += "<table style='width: 600px;font-size: 14px;'>";
				
				html += "<tr>";
				html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold;' colspan='5'>Customer Advances</td>";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td style='background-color: #C8CFE6; height: 18px; text-align: center; font-weight: bold;'></td>";
				html += "<td style='background-color: #C8CFE6; height: 18px; text-align: center; font-weight: bold;'>Advance against Liquid</td>";
				html += "<td style='background-color: #C8CFE6; height: 18px; text-align: center; font-weight: bold;'>Unlifted Stock</td>";
				html += "<td style='background-color: #C8CFE6; height: 18px; text-align: center; font-weight: bold;'>Total Advance</td>";
				html += "<td style='background-color: #C8CFE6; height: 18px; text-align: center; font-weight: bold;'>(%)</td>";
				html += "</tr>";
				
				double TotalAdvanceSD1 = AdvanceSD1 + NotLiftedSD1;
				double TotalAdvanceSD2 = AdvanceSD2 + NotLiftedSD2;
				double TotalAdvanceB2B = AdvanceB2B + NotLiftedB2B;
				double TotalAdvanceOther = AdvanceOther + NotLiftedOther;
				
				
				double TotalAdvance = AdvanceSD1 + AdvanceSD2 + AdvanceB2B + AdvanceOther;
				double TotalNotLifted = NotLiftedSD1 + NotLiftedSD2 + NotLiftedB2B + NotLiftedOther;
				
				double TotalAdvances =  TotalAdvance + TotalNotLifted;
				
				
				double AdvancePercentSD1 = 0;
				double AdvancePercentSD2 = 0;
				double AdvancePercentB2B = 0;
				double AdvancePercentOther = 0;
				if (TotalAdvances != 0){
					
					AdvancePercentSD1 = (TotalAdvanceSD1 / TotalAdvances) * 100;
					AdvancePercentSD2 = (TotalAdvanceSD2 / TotalAdvances) * 100;
					AdvancePercentB2B = (TotalAdvanceB2B / TotalAdvances) * 100;
					AdvancePercentOther = (TotalAdvanceOther / TotalAdvances) * 100;
					
				}
				
				html += "<tr>";
				html += "<td style='background-color: #C8CFE6; width: 120px; text-align: left;font-weight: normal;'>SD1";
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(AdvanceSD1);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(NotLiftedSD1);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalAdvanceSD1);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatOneDecimal(AdvancePercentSD1)+"%";
				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #C8CFE6; width: 120px; text-align: left;font-weight: normal;'>SD2";
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(AdvanceSD2);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(NotLiftedSD2);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalAdvanceSD2);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatOneDecimal(AdvancePercentSD2)+"%";
				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #C8CFE6; width: 120px; text-align: left;font-weight: normal;'>SD3";
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(AdvanceB2B);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(NotLiftedB2B);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalAdvanceB2B);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatOneDecimal(AdvancePercentB2B)+"%";
				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #C8CFE6; width: 120px; text-align: left;font-weight: normal;'>Other";
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(AdvanceOther);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(NotLiftedOther);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalAdvanceOther);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatOneDecimal(AdvancePercentOther)+"%";
				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #C8CFE6; width: 120px; text-align: left;font-weight: bold;'>Total";
				html += "<td style='background-color: #EDEFF2;text-align: right;font-weight: bold;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalAdvance);
				html += "<td style='background-color: #EDEFF2;text-align: right;font-weight: bold;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalNotLifted);
				html += "<td style='background-color: #EDEFF2;text-align: right;font-weight: bold;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalAdvances);
				html += "<td style='background-color: #EDEFF2;text-align: right;font-weight: bold;'>";
				
				
				html += "</tr>";
				
				html += "</table>";
				
				
				html += "<br><br>";
				
				html += "Please see attachment for details.<br>This is a system generated email, please do not reply to it.";
				
		html += "</body>";
		html += "</html>";
		
		this.HTMLMessage = html;
	}
	private String getColor(double val){
		String color = "#cbe9aa";
		if (val < 0){
			color = "#e79ca8";
		}
		return color;
	}
	
}
