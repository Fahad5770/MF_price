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

public class CashInflowSummaryPDF {

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
	Date StartDate = null;
    Date MonthToDateStartDate = null;
    Date MonthToDateEndDate = null;	
    
    Date YearStartDate = null;
    Date YearEndDate = null;
    
    String HTMLMessage;
	//Date EndDate = new Date();
	
    
    double MTDSD1 = 0;
    double MTDSD2 = 0;
    double MTDB2B = 0;
    double MTDOther = 0;
    double MTDTotal = 0;

    double YTDSD1 = 0;
    double YTDSD2 = 0;
    double YTDB2B = 0;
    double YTDOther = 0;
    double YTDTotal = 0;

    double YTDPSD1 = 0;
    double YTDPSD2 = 0;
    double YTDPB2B = 0;
    double YTDPOther = 0;

	public static void main(String[] args) throws DocumentException, IOException{
		try {
			new CashInflowSummaryPDF().createPdf(RESULT);
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
	
	public CashInflowSummaryPDF() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		StartDate = new Date();
		StartDate = Utilities.getDateByDays(-1);
		
		
    	Calendar cc = Calendar.getInstance();
    	cc.setTime(StartDate);
    	
    	int year = cc.get(Calendar.YEAR);
    	int month = cc.get(Calendar.MONTH);
       
        MonthToDateStartDate = Utilities.getStartDateByMonth(month, year);
        MonthToDateEndDate = StartDate;
		
        YearStartDate = Utilities.getStartDateByMonth(0, year);
        YearEndDate = StartDate;
		
        
		ds.createConnection();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();
	}
	
	public void createPdf(String filename) throws DocumentException, IOException, SQLException {
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
        
	    
        PdfPCell pcellb = new PdfPCell(new Phrase("SD1 - Base Group", reportheading1));
        pcellb.setBackgroundColor(BaseColor.DARK_GRAY);
        pcellb.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcellb.setBorderColor(BaseColor.DARK_GRAY);
        pcellb.setMinimumHeight(20);
        pcellb.setColspan(ColumnCount);
        table.addCell(pcellb);
        
        
        ResultSet rs3 = s.executeQuery("select distributor_id,name from common_distributors where distributor_id in (select distinct(customer_id) from gl_cash_receipts where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(StartDate)+" ) and snd_id = 1804");
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
        
		
        pcellb = new PdfPCell(new Phrase("SD2 - Sargodha Group", reportheading1));
        pcellb.setBackgroundColor(BaseColor.DARK_GRAY);
        pcellb.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcellb.setBorderColor(BaseColor.DARK_GRAY);
        pcellb.setMinimumHeight(20);
        pcellb.setColspan(ColumnCount);
        table.addCell(pcellb);
		
        
        rs3 = s.executeQuery("select distributor_id,name from common_distributors where distributor_id in (select distinct(customer_id) from gl_cash_receipts where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(StartDate)+" ) and snd_id = 2262");
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
        pcellb = new PdfPCell(new Phrase("B2B - Key Accounts", reportheading1));
        pcellb.setBackgroundColor(BaseColor.DARK_GRAY);
        pcellb.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcellb.setBorderColor(BaseColor.DARK_GRAY);
        pcellb.setMinimumHeight(20);
        pcellb.setColspan(ColumnCount);
        table.addCell(pcellb);
		
        
        rs3 = s.executeQuery("select distributor_id,name from common_distributors where distributor_id in (select distinct(customer_id) from gl_cash_receipts where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(StartDate)+" ) and snd_id = 2648");
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
		
        
        
        
        
        rs3 = s.executeQuery("select distributor_id,name from common_distributors where distributor_id in (select distinct(customer_id) from gl_cash_receipts where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(StartDate)+" ) and (snd_id is null or snd_id not in (1804, 2262, 2648))");
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
    	    pcell = new PdfPCell(new Phrase("SD1 - Base Group", fontheading));
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
						" on glcr.id=glcri.id where glcri.instrument_id="+InstrumentID+" and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate)+" and customer_id in (select distributor_id from common_distributors where snd_id = 1804) group by glcri.instrument_id ");
				
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
    	    pcell = new PdfPCell(new Phrase("SD2 - Sargodha Group", fontheading));
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
						" on glcr.id=glcri.id where glcri.instrument_id="+InstrumentID+" and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate)+" and customer_id in (select distributor_id from common_distributors where snd_id = 2262) group by glcri.instrument_id ");
				
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
    	    pcell = new PdfPCell(new Phrase("B2B - Key Accounts", fontheading));
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
						" on glcr.id=glcri.id where glcri.instrument_id="+InstrumentID+" and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate)+" and customer_id in (select distributor_id from common_distributors where snd_id = 2648) group by glcri.instrument_id ");
				
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
						" on glcr.id=glcri.id where glcri.instrument_id="+InstrumentID+" and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate)+" and customer_id in (select distributor_id from common_distributors where (snd_id is null or snd_id not in (1804, 2262, 2648))) group by glcri.instrument_id ");
				
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
        document.add(new Paragraph("* This report tracks inflow of instruments explicitly. Please see R167 for dishonored or post-dated instruments.",reportheading4));


        // This Month
		ResultSet rs6 = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = 1804) and glci.is_internal = 0 ");
		if(rs6.first()){
			MTDSD1 = rs6.getDouble(1);
		}
		ResultSet rs7 = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = 2262) and glci.is_internal = 0 ");
		if(rs7.first()){
			MTDSD2 = rs7.getDouble(1);
		}
		ResultSet rs8 = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = 2648) and glci.is_internal = 0 ");
		if(rs8.first()){
			MTDB2B = rs8.getDouble(1);
		}
		ResultSet rs9 = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate)+" and glcr.customer_id in (select distributor_id from common_distributors where (snd_id is null or snd_id not in (1804, 2262, 2648))) and glci.is_internal = 0 ");
		if(rs9.first()){
			MTDOther = rs9.getDouble(1);
		}
		
		MTDTotal = MTDSD1 + MTDSD2 + MTDB2B + MTDOther;
		
		
		// This Year
		ResultSet rs10 = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YearStartDate)+" and "+Utilities.getSQLDateNextLifting(YearEndDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = 1804) and glci.is_internal = 0 ");
		if(rs10.first()){
			YTDSD1 = rs10.getDouble(1);
		}
		ResultSet rs11 = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YearStartDate)+" and "+Utilities.getSQLDateNextLifting(YearEndDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = 2262) and glci.is_internal = 0 ");
		if(rs11.first()){
			YTDSD2 = rs11.getDouble(1);
		}
		ResultSet rs12 = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YearStartDate)+" and "+Utilities.getSQLDateNextLifting(YearEndDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = 2648) and glci.is_internal = 0 ");
		if(rs12.first()){
			YTDB2B = rs12.getDouble(1);
		}
		ResultSet rs13 = s3.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YearStartDate)+" and "+Utilities.getSQLDateNextLifting(YearEndDate)+" and glcr.customer_id in (select distributor_id from common_distributors where (snd_id is null or snd_id not in (1804, 2262, 2648))) and glci.is_internal = 0 ");
		if(rs13.first()){
			YTDOther = rs13.getDouble(1);
		}
		
		YTDTotal = YTDSD1 + YTDSD2 + YTDB2B + YTDOther;
		
		if (YTDTotal != 0){
			YTDPSD1 = (YTDSD1 / YTDTotal) * 100;
			YTDPSD2 = (YTDSD2 / YTDTotal) * 100;
			YTDPB2B = (YTDB2B / YTDTotal) * 100;
			YTDPOther = (YTDOther / YTDTotal) * 100;
		}
		
		
		
        setHTMLMessage(GrandTotalSD1,GrandTotalSD2,GrandTotalB2B,GrandTotalOther,GrandTotalAll);
        
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
		
		String html = "<html>";
		html += "<body><br>";
			html += "<table style='width: 600px;'>";
				html += "<tr>";
					html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold;'></td>";
					html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold;'>"+Utilities.getDisplayFullDateFormatShort(StartDate)+"</td>";
					html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold;'>This Month</td>";
					html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold;'>This Year</td>";
					html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: bold;'>This Year (%)</td>";
				html += "</tr>";
			
				
				html += "<tr>";
					html += "<td style='background-color: #C8CFE6; width: 120px; text-align: left;font-weight: normal;'>SD1";
					html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(SD1);
					html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDSD1);
					html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDSD1);
					html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatOneDecimal(YTDPSD1)+"%";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td style='background-color: #C8CFE6; width: 120px; text-align: left;font-weight: normal;'>SD2";
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(SD2);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDSD2);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDSD2);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatOneDecimal(YTDPSD2)+"%";
				html += "</tr>";
			
				html += "<tr>";
				html += "<td style='background-color: #C8CFE6; width: 120px; text-align: left;font-weight: normal;'>B2B";
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(B2B);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDB2B);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDB2B);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatOneDecimal(YTDPB2B)+"%";
				html += "</tr>";
			
				html += "<tr>";
				html += "<td style='background-color: #C8CFE6; width: 120px; text-align: left;font-weight: normal;'>Other";
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(other);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDOther);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDOther);
				html += "<td style='background-color: #EDEFF2;text-align: right'>"+Utilities.getDisplayCurrencyFormatOneDecimal(YTDPOther)+"%";
				html += "</tr>";

				html += "<tr>";
				html += "<td style='background-color: #C8CFE6; width: 120px; text-align: left;font-weight: bold;'>Total";
				html += "<td style='background-color: #EDEFF2;text-align: right;font-weight: bold;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(total);
				html += "<td style='background-color: #EDEFF2;text-align: right;font-weight: bold;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDTotal);
				html += "<td style='background-color: #EDEFF2;text-align: right;font-weight: bold;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDTotal);
				html += "<td style='background-color: #EDEFF2;text-align: right;font-weight: bold;'>";
				
				
				html += "</tr>";
				
				
				html += "<tr>";
				html += "<td colspan='4'>&nbsp;";
				html += "</tr>";
				
				html += "</table>";
				
				html += "Please see attachment for details.<br>This is a system generated email, please do not reply to it.";
			
		html += "</body>";
		html += "</html>";
		
		this.HTMLMessage = html;
	}

	
}
