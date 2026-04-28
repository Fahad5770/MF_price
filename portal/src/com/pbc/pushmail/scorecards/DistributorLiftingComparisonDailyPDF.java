package com.pbc.pushmail.scorecards;

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

public class DistributorLiftingComparisonDailyPDF {

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
	Datasource dsSAP = new Datasource();
	
	Statement sSAP;
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
    double MTDSpecial = 0;
    double MTDTotal = 0;
    
    double MTDPSD1 = 0;
    double MTDPSD2 = 0;
    double MTDPB2B = 0;
    double MTDPOther = 0;
    double MTDPSpecial = 0;
    double MTDPTotal = 0;    

    double MTDSD1LY = 0;
    double MTDSD2LY = 0;
    double MTDB2BLY = 0;
    double MTDOtherLY = 0;
    double MTDSpecialLY = 0;
    double MTDTotalLY = 0;
    
    double YTDSD1 = 0;
    double YTDSD2 = 0;
    double YTDB2B = 0;
    double YTDOther = 0;
    double YTDSpecial = 0;
    double YTDTotal = 0;

    double YTDSD1LY = 0;
    double YTDSD2LY = 0;
    double YTDB2BLY = 0;
    double YTDOtherLY = 0;
    double YTDSpecialLY = 0;
    double YTDTotalLY = 0;
    
    double YTDPSD1 = 0;
    double YTDPSD2 = 0;
    double YTDPB2B = 0;
    double YTDPOther = 0;
    double YTDPSpecial = 0;
    double YTDPTotal = 0;
    
    
	public static void main(String[] args) throws DocumentException, IOException{
		/*try {
			//new CashInflowSummaryV2PDF().createPdf(RESULT);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
	}
	
	public String getHTMLMessage(){
		return HTMLMessage;
	}
	
	public DistributorLiftingComparisonDailyPDF() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		System.out.println("running 10 ...");
		
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
        
        
        
        
		//ds.createConnection();
		ds.createConnectionToReplica();
		dsSAP.createConnectionToSAPDB();
		
		sSAP = dsSAP.createStatement();
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
        
        
	    
	    
	    /*
        int InstrumentCount = 0;
        ResultSet rs = s.executeQuery("select count(id) from gl_cash_instruments where is_visible_cash_receipt = 1 and is_internal = 0 order by id");
        while(rs.next()){
        	InstrumentCount = rs.getInt(1);
        }*/

	    int ColumnCount = 3;

		double AmountTotalSD1 = 0;
		double AmountTotalSD2 = 0;
		double AmountTotalSD3 = 0;
		double AmountTotalSD4 = 0;
		double AmountTotalSD5 = 0;
		/*
		for (int i = 0; i < AmountTotalSD1.length; i++){
			AmountTotalSD1[i] = 0;								
		}	*/    
		
	    PdfPTable table = new PdfPTable(ColumnCount);
	    
	    table.setWidthPercentage(100f);
	    table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
    	
	    PdfPCell pcell = new PdfPCell(new Phrase("Lifting Summary, Period: "+Utilities.getDisplayFullDateFormatShort(StartDate), reportheading5));
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
        table.addCell(getHeadingCell("Converted Cases"));
        
        AmountTotalSD1 = processSD(pcell, table, EmployeeHierarchy.getSDHead(1).USER_ID, ColumnCount, "SD1");
        AmountTotalSD2 = processSD(pcell, table, EmployeeHierarchy.getSDHead(2).USER_ID, ColumnCount, "SD2");
        AmountTotalSD3 = processSD(pcell, table, EmployeeHierarchy.getSDHead(4).USER_ID, ColumnCount, "SD3");
        //AmountTotalSD4 = processSD(pcell, table, 2646, ColumnCount, "Bulk");
        AmountTotalSD1 += processSD(pcell, table, 2646, ColumnCount, "Bulk");
        AmountTotalSD5 = processSD(pcell, table, 0, ColumnCount, "Special");	// common_dist > category_id = 8 
        
        PdfPCell pcellb = new PdfPCell(new Phrase("Total", reportheading4));
        pcellb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        pcellb.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcellb.setBorderColor(BaseColor.LIGHT_GRAY);
        pcellb.setMinimumHeight(20);
        pcellb.setColspan(2);
        table.addCell(pcellb);
		
    	
		double GrandTotal = AmountTotalSD1 + AmountTotalSD2 + AmountTotalSD3 + AmountTotalSD4 + AmountTotalSD5; 
		
		table.addCell(getNormalCellTotal(Utilities.getDisplayCurrencyFormatRounded(GrandTotal)));
		
		
        Paragraph header = new Paragraph("All Warehouses | "+Utilities.getDisplayDateFormat(StartDate),fonttitle);
        header.setAlignment(Element.ALIGN_RIGHT);

        double GrandTotalAll = GrandTotal;
        
        
        Paragraph footertext1 = new Paragraph("Total: "+Utilities.getDisplayCurrencyFormatRounded(GrandTotalAll),fonttitle);
        footertext1.setAlignment(Element.ALIGN_LEFT);
        
        
        document.add(header);
        document.add(Chunk.NEWLINE);
        
        document.add(table);
        
        document.add(Chunk.NEWLINE);
        document.add(footertext1);
        
        
        
        // This Month        	
		ResultSet rs6 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(1).USER_ID+")) and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate));
		if(rs6.first()){
			MTDSD1 = rs6.getDouble(1);
		}
		ResultSet rs61 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(2).USER_ID+")) and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate));
		if(rs61.first()){
			MTDSD2 = rs61.getDouble(1);
		}
		ResultSet rs62 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(4).USER_ID+")) and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate));
		if(rs62.first()){
			MTDB2B = rs62.getDouble(1);
		}
		ResultSet rs63 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (2646)) and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate));
		if(rs63.first()){
			//MTDOther = rs63.getDouble(1);
			MTDSD1 += rs63.getDouble(1);
		}
		ResultSet rs64 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where category_id=8) and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDate));
		if(rs64.first()){
			MTDSpecial = rs64.getDouble(1);
		}
		
		
		MTDTotal = MTDSD1 + MTDSD2 + MTDB2B + MTDOther + MTDSpecial;

		// MTD Last Year		
		ResultSet rs6LY = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(1).USER_ID+")) and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDateLastYear));
		if(rs6LY.first()){
			MTDSD1LY = rs6LY.getDouble(1); 
		}
		ResultSet rs6LY1 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(2).USER_ID+")) and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDateLastYear));
		if(rs6LY1.first()){
			MTDSD2LY = rs6LY1.getDouble(1); 
		}
		ResultSet rs6LY2 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(4).USER_ID+")) and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDateLastYear));
		if(rs6LY2.first()){
			MTDB2BLY = rs6LY2.getDouble(1); 
		}
		ResultSet rs6LY3 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (2646)) and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDateLastYear));
		if(rs6LY3.first()){
			//MTDOtherLY = rs6LY3.getDouble(1);
			MTDSD1LY += rs6LY3.getDouble(1);
		}
		ResultSet rs6LY4 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where category_id=8) and created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(MonthToDateEndDateLastYear));
		if(rs6LY4.first()){
			MTDSpecialLY = rs6LY4.getDouble(1); 
		}
		
		MTDTotalLY = MTDSD1LY + MTDSD2LY + MTDB2BLY + MTDOtherLY + MTDSpecialLY;
		
		// This Year		
		ResultSet rs10 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(1).USER_ID+")) and created_on between "+Utilities.getSQLDateLifting(YearStartDate)+" and "+Utilities.getSQLDateNextLifting(YearEndDate));
		if(rs10.first()){
			YTDSD1 = rs10.getDouble(1);
		}
		ResultSet rs101 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(2).USER_ID+")) and created_on between "+Utilities.getSQLDateLifting(YearStartDate)+" and "+Utilities.getSQLDateNextLifting(YearEndDate));
		if(rs101.first()){
			YTDSD2 = rs101.getDouble(1);
		}
		ResultSet rs102 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(4).USER_ID+")) and created_on between "+Utilities.getSQLDateLifting(YearStartDate)+" and "+Utilities.getSQLDateNextLifting(YearEndDate));
		if(rs102.first()){
			YTDB2B = rs102.getDouble(1);
		}
		ResultSet rs103 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (2646)) and created_on between "+Utilities.getSQLDateLifting(YearStartDate)+" and "+Utilities.getSQLDateNextLifting(YearEndDate));
		if(rs103.first()){
			//YTDOther = rs103.getDouble(1);
			YTDSD1 += rs103.getDouble(1);
		}
		ResultSet rs104 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where category_id=8) and created_on between "+Utilities.getSQLDateLifting(YearStartDate)+" and "+Utilities.getSQLDateNextLifting(YearEndDate));
		if(rs104.first()){
			YTDSpecial = rs104.getDouble(1);
		}
		
		YTDTotal = YTDSD1 + YTDSD2 + YTDB2B + YTDOther + YTDSpecial;

		// YTD Last Year		
		ResultSet rs10LY = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(1).USER_ID+")) and created_on between "+Utilities.getSQLDateLifting(YearStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(YearEndDateLastYear));
		if(rs10LY.first()){
			YTDSD1LY = rs10LY.getDouble(1);
		}
		ResultSet rs10LY1 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(2).USER_ID+")) and created_on between "+Utilities.getSQLDateLifting(YearStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(YearEndDateLastYear));
		if(rs10LY1.first()){
			YTDSD2LY = rs10LY1.getDouble(1);
		}
		ResultSet rs10LY2 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(4).USER_ID+")) and created_on between "+Utilities.getSQLDateLifting(YearStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(YearEndDateLastYear));
		if(rs10LY2.first()){
			YTDB2BLY = rs10LY2.getDouble(1);
		}
		ResultSet rs10LY3 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in (2646)) and created_on between "+Utilities.getSQLDateLifting(YearStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(YearEndDateLastYear));
		if(rs10LY3.first()){
			//YTDOtherLY = rs10LY3.getDouble(1);
			YTDSD1LY += rs10LY3.getDouble(1);
		}
		ResultSet rs10LY4 = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in (SELECT distributor_id FROM common_distributors where category_id=8) and created_on between "+Utilities.getSQLDateLifting(YearStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(YearEndDateLastYear));
		if(rs10LY4.first()){
			YTDSpecialLY = rs10LY4.getDouble(1);
		}
		
		YTDTotalLY = YTDSD1LY + YTDSD2LY + YTDB2BLY + YTDOtherLY + YTDSpecialLY; 
		
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
		if (YTDSpecialLY != 0){
			YTDPSpecial = ((YTDSpecial - YTDSpecialLY) / YTDSpecialLY) * 100;
		}
		if (YTDTotalLY != 0){
			YTDPTotal = ((YTDTotal - YTDTotalLY) / YTDTotalLY) * 100;
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
		if (MTDSpecialLY != 0){
			MTDPSpecial = ((MTDSpecial - MTDSpecialLY) / MTDSpecialLY) * 100;
		}
		if (MTDTotalLY != 0){
			MTDPTotal = ((MTDTotal - MTDTotalLY) / MTDTotalLY) * 100;
		}
		

		Date iDate = YearStartDate;
		while((iDate.before(DateUtils.addDays(YearEndDate, 1)))){
			Date iDateLY = DateUtils.addYears(iDate, -1);
			
			double AmountTY = 0;		
			ResultSet rsc = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and (distributor_id in( SELECT distributor_id FROM common_distributors where category_id = 8) or distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(1).USER_ID+", "+EmployeeHierarchy.getSDHead(2).USER_ID+", "+EmployeeHierarchy.getSDHead(4).USER_ID+", 2646))) and created_on between "+Utilities.getSQLDateLifting(YearStartDate)+" and "+Utilities.getSQLDateNextLifting(iDate));
			if(rsc.first()){
				AmountTY = rsc.getDouble(1);
			}

			double AmountLY = 0;
			ResultSet rscLY = s3.executeQuery("select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and (distributor_id in( SELECT distributor_id FROM common_distributors where category_id = 8) or distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(1).USER_ID+", "+EmployeeHierarchy.getSDHead(2).USER_ID+", "+EmployeeHierarchy.getSDHead(4).USER_ID+", 2646))) and created_on between "+Utilities.getSQLDateLifting(YearStartDateLastYear)+" and "+Utilities.getSQLDateNextLifting(iDateLY));
			if(rscLY.first()){
				AmountLY = rscLY.getDouble(1);
			}
			
			if (YTDSeries.length() != 0){
				YTDSeries = YTDSeries + ",";
			}
			if (YTDSeriesLY.length() != 0){
				YTDSeriesLY = YTDSeriesLY + ",";
			}
			
			YTDSeries = YTDSeries + Utilities.getDisplayCurrencyFormatSimple(AmountTY / 1000000);
			YTDSeriesLY = YTDSeriesLY + Utilities.getDisplayCurrencyFormatSimple(AmountLY / 1000000);
			
			//System.out.println(Utilities.getDisplayCurrencyFormat(AmountLY));
			//System.out.println(iDate + " " +iDateLY);
			
			
			
			iDate = DateUtils.addDays(iDate, 1);
		}
		
		dsSAP.dropConnection();				
		
        setHTMLMessage(AmountTotalSD1, AmountTotalSD2, AmountTotalSD3, AmountTotalSD4, AmountTotalSD5 ,GrandTotalAll); 
        
	    document.close();
	    
	    
	}
	
	public double processSD(PdfPCell pcell, PdfPTable table, long SDID, int ColumnCount, String SDTitle) throws SQLException{
		

        PdfPCell pcellb = new PdfPCell(new Phrase(SDTitle, reportheading1));
        pcellb.setBackgroundColor(BaseColor.DARK_GRAY);
        pcellb.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcellb.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcellb.setBorderColor(BaseColor.DARK_GRAY);
        pcellb.setMinimumHeight(20);
        pcellb.setColspan(ColumnCount);
        table.addCell(pcellb);
		
        String WhereSDClause = "and snd_id = "+SDID;
        if(SDID == 0){
        	WhereSDClause = "and category_id = "+SDID;
        }
		double AmountTotalSD = 0;
		ResultSet rs3 = s.executeQuery("select distributor_id,name from common_distributors where distributor_id in (select distinct(distributor_id) from inventory_delivery_note where created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(StartDate)+" ) "+WhereSDClause);
        while(rs3.next()){
        	
        	long DistributorID = rs3.getLong(1);
        	String DistributorName = rs3.getString(2);
        	
    	    pcell = new PdfPCell(new Phrase(Utilities.truncateStringToMax(DistributorID+" - "+DistributorName, 25) , fontheading));
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorderColor(BaseColor.LIGHT_GRAY);
            pcell.setColspan(2);
        	table.addCell(pcell);
        	        	        	
        	double amount = 0;
        	
        	String SD1SQL = "select sum(idnp.total_units * ipv.liquid_in_ml)/6000 total_lifting from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id in ("+rs3.getString("distributor_id")+") and created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(StartDate);
			//System.out.println(SD1SQL);
			ResultSet rs5 = s3.executeQuery(SD1SQL);  
			
			if(rs5.first()){
				amount = rs5.getDouble("total_lifting");  
			}
			
			AmountTotalSD += amount;
			
			if (amount != 0){
				table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(amount)));	
			}else{
				table.addCell(getNormalCell("=="));
			}
        	
        }
        

	    pcell = new PdfPCell(new Phrase("Subtotal" , fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setColspan(2);
    	table.addCell(pcell);    	
		table.addCell(getNormalCell(Utilities.getDisplayCurrencyFormatRounded(AmountTotalSD)));
		
		return AmountTotalSD;
	}
	
	public double getPercentage(double value, double total){
		double result = 0;
		
		if (total != 0){
			result = (value/total)*100;
		}
		
		return result;
		
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
	public void setHTMLMessage(double SD1, double SD2, double SD3, double SD4, double Special, double total){ 
		
		Date ReportDate = Utilities.getDateByDays(-1);
		
    	Calendar cc = Calendar.getInstance();
    	cc.setTime(ReportDate);
    	
    	int year = cc.get(Calendar.YEAR);
    	int LastYear = year-1;

		
		
		String html = "<html>";
		html += "<body><br>";
			html += "<table border='0' style='border-collapse: collapse; border:1px solid #dee0e1;width:800px;font-size: 14px;'>";
			
				html += "<tr>";
					html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: normal;' colspan='8'>Lifting Summary - LRB</td>";
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
				html += "<td style='border:1px solid #c2c3c3;text-align: right;background-color: #EDEFF2;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(SD3);
				html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDB2BLY);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDB2B);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(MTDPB2B)+";'>"+Utilities.getDisplayCurrencyFormatRounded(MTDPB2B)+"%";
				html += "<td style='border:1px solid #c2c3c3;text-align: right;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDB2BLY);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDB2B);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(YTDPB2B)+";'>"+Utilities.getDisplayCurrencyFormatRounded(YTDPB2B)+"%";
				html += "</tr>";
				/*
				html += "<tr>";
				html += "<td style='border:1px solid #c2c3c3; text-align: left;font-weight: normal; background-color: #D5DBED'>Bulk";
				html += "<td style='border:1px solid #c2c3c3;text-align: right;background-color: #EDEFF2;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(SD4);
				html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDOtherLY);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDOther);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(MTDPOther)+";'>"+Utilities.getDisplayCurrencyFormatRounded(MTDPOther)+"%";
				html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDOtherLY);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDOther);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(YTDPOther)+";'>"+Utilities.getDisplayCurrencyFormatRounded(YTDPOther)+"%";
				html += "</tr>";
				*/
				html += "<tr>";
				html += "<td style='border:1px solid #c2c3c3; text-align: left;font-weight: normal; background-color: #D5DBED'>B2B";
				html += "<td style='border:1px solid #c2c3c3;text-align: right;background-color: #EDEFF2;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(Special);
				html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDSpecialLY);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDSpecial);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(MTDPSpecial)+";'>"+Utilities.getDisplayCurrencyFormatRounded(MTDPSpecial)+"%";
				html += "<td style='border:1px solid #c2c3c3;text-align: right'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDSpecialLY);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDSpecial);
				html += "<td style='border:1px solid #c2c3c3;text-align: right; background-color: "+getColor(YTDPSpecial)+";'>"+Utilities.getDisplayCurrencyFormatRounded(YTDPSpecial)+"%";
				html += "</tr>";
				
				html += "<tr>";
				html += "<td style='border:1px solid #c2c3c3; text-align: left;font-weight: normal; background-color: #D5DBED; font-weight: bold'>Total";
				html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal;background-color: #EDEFF2;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(total);
				html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDTotalLY);
				html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(MTDTotal);
				html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal; background-color: "+getColor(MTDPTotal)+"'>"+Utilities.getDisplayCurrencyFormatRounded(MTDPTotal)+"%";
				html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal;'>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDTotalLY);
				html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal; '>"+Utilities.getDisplayCurrencyFormatRoundedAccounting(YTDTotal);
				html += "<td style='border:1px solid #c2c3c3;text-align: right;font-weight: normal; background-color: "+getColor(YTDPTotal)+";'>"+Utilities.getDisplayCurrencyFormatRounded(YTDPTotal)+"%";
				
				
				
				html += "</tr>";

				html += "</table>";
				
				html +="<br><table style='font-size: 14px;'><tr><td style='width:20px; hight: 20px; background-color:#cbe9aa'></td><td>Increased w.r.t. last year</td></tr><tr><td style='width:20px; hight: 20px; background-color:#e79ca8'></td><td>Decreased w.r.t. last year</td></tr></table>";
				html += "<br>";
				
				html += "<table border='0' style='border-collapse: collapse; border:0px solid #dee0e1;width:600px;font-size: 14px;'>";
				
				html += "<tr>";
					html += "<td style='background-color: #3D5AB3; color: white; height: 18px; text-align: center; font-weight: normal;'>YTD Lifting Comparison</td>";
				html += "</tr>";
				html += "<tr>";
				html += "<td><img src='https://chart.googleapis.com/chart?cht=lc&chd=t:"+YTDSeriesLY+"|"+YTDSeries+"&chds=a&chs=800x350&chl=Jan%201|Today&chxt=y,r&chco=bda609,0963bd,f0516c&chdl="+LastYear+"|"+year+"&chdlp=b&chtt=(millions)'></td>";
				html += "</tr>";
				html += "</table>";
				
				html += "<br>";
				
				
				
				html += "<br><br>";
				
				//html += "Please see attachment for details.<br>This is a system generated email, please do not reply to it.";
				
		html += "</body>";
		html += "</html>";
		
		this.HTMLMessage = html;
	}
	private String getColor(double val){
		String color = "#cbe9aa";
		if (Math.round(val) < 0){
			color = "#e79ca8";
		}
		/*
		if (Math.round(val) == 0){
			color = "#FFF";
		}
		*/
		return color;
	}
	
}
