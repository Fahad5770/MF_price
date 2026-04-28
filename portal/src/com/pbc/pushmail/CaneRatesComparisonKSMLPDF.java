package com.pbc.pushmail;

import java.io.FileOutputStream;
import java.io.IOException;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class CaneRatesComparisonKSMLPDF {

	public static final String RESULT = Utilities.getEmailAttachmentsPath()+ "/KSML_Cane_Rates_Comparison.pdf";
	Font fonttableheader = FontFactory.getFont("Arial", 8, Font.BOLD);
	Font fontheading = FontFactory.getFont("Arial", 8, Font.NORMAL);
	Font fontpbc = FontFactory.getFont("Arial", 10, Font.BOLD);
	Font reportheading1 = FontFactory.getFont("Arial", 12, Font.BOLD);
	Font reportheading = FontFactory.getFont("Arial", 10, Font.BOLD);
	Font reportheading3 = FontFactory.getFont("Arial", 10, Font.BOLD);
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	Date StartDate = new Date();
	Date EndDate = new Date();
	
	public static void main(String[] args) throws DocumentException, IOException{
		try {
			new CaneRatesComparisonKSMLPDF().createPdf(RESULT);
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
	
	public CaneRatesComparisonKSMLPDF() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds.createConnectionKSML();
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
	    
        Paragraph header = new Paragraph(""+Utilities.getDisplayDateTimeFormat(new Date()),fontheading);
        header.setAlignment(Element.ALIGN_RIGHT);
        
        Image img = Image.getInstance(String.format(Utilities.getImageResoucesPath()+"/"+"%s", "ksml_small_logo.jpg"));
        //img.scaleToFit(25, 25);
        img.setAbsolutePosition(0, 525);
        
        document.add(img);
        //Paragraph pbc = new Paragraph("         Kamalia Sugar Mills Limited",fontpbc);
        //document.add(pbc);
        
        
	    document.add(header);
	    document.add(Chunk.NEWLINE);
	    
	    ResultSet rs = s.executeQuery("select distinct zone from crman_centers where mill_id = 2 order by zone");
	    while(rs.next()){
	    	String zone = rs.getString(1);
	    	

	    	
		    PdfPTable table = new PdfPTable(6);
		    
		    table.setWidthPercentage(100f);
		    table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
	    	
		    PdfPCell pcell = new PdfPCell(new Phrase(zone, reportheading1));
	        pcell.setBackgroundColor(BaseColor.BLUE);
	        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        pcell.setBorderColor(BaseColor.BLUE);
	        pcell.setColspan(6);
	        pcell.setMinimumHeight(22);
	        table.addCell(pcell);
	        
	    	ResultSet rs2 = s2.executeQuery("SELECT distinct circle FROM crman_centers where zone = '"+zone+"' and circle in (SELECT distinct circle FROM crman_valid_messages cvm join crman_centers cc on cvm.center_id = cc.id)");
	    	while(rs2.next()){
	    		
	    		String circle = rs2.getString(1);
	    		
			    pcell = new PdfPCell(new Phrase(circle, reportheading));
		        pcell.setBackgroundColor(BaseColor.DARK_GRAY);
		        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        pcell.setBorderColor(BaseColor.DARK_GRAY);
		        pcell.setColspan(6);
		        pcell.setMinimumHeight(20);
		        table.addCell(pcell);
		

		        table.addCell(getHeadingCell(""));
		        
		        pcell = new PdfPCell(new Phrase("Our Mill", reportheading3));
		        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        pcell.setBorderColor(new BaseColor(169, 222, 155));
		        pcell.setBackgroundColor(new BaseColor(169, 222, 155));
		        pcell.setMinimumHeight(18);
		        pcell.setColspan(3);
			    
		        table.addCell(pcell);
		        
		        pcell = new PdfPCell(new Phrase("R & D", reportheading3));
		        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        pcell.setBorderColor(BaseColor.PINK);
		        pcell.setBackgroundColor(BaseColor.PINK);
		        pcell.setColspan(2);
		        pcell.setMinimumHeight(18);
		        
		        table.addCell(pcell);
		        
		        table.addCell(getHeadingCell("Center"));
		        table.addCell(getHeadingCell("Ex-Mill"));
		        table.addCell(getHeadingCell("TPT"));
		        table.addCell(getHeadingCellHigh("Mill Cane Rate"));
		        table.addCell(getHeadingCellHigh("Competitor Cane Rate (RnD)"));
		        table.addCell(getHeadingCell("Visit Date"));
		        
		        
		        ResultSet rs3 = s3.executeQuery("SELECT id, name FROM crman_centers where zone = '"+zone+"' and circle = '"+circle+"' and id in (select center_id from crman_valid_messages)");
		        while(rs3.next()){
		        	
		        	int CenterID = rs3.getInt(1);
		        	String CenterName = rs3.getString(2);
		        	
		        	double MillRate = 0;
		        	double TPTRate = 0;
		        	double ExMill = 0;
		        	ResultSet rs4 = s4.executeQuery("SELECT rate, tpt_rate FROM ksml.crman_own_rates where center_id = "+CenterID+" order by wef_date desc limit 1");
		        	if (rs4.first()){
		        		MillRate = rs4.getDouble(1);
		        		TPTRate = rs4.getDouble(2);
		        		ExMill = MillRate + TPTRate; 
		        	}
		        	
			        table.addCell(getNormalCell(CenterName));
			        table.addCell(getNormalCell(""+Utilities.getDisplayCurrencyFormat(ExMill)));
			        table.addCell(getNormalCell(""+Utilities.getDisplayCurrencyFormat(TPTRate)));
			        table.addCell(getNormalCell(""+Utilities.getDisplayCurrencyFormatRounded(MillRate)));
			        
			        Date LastDate = null;
		        	ResultSet rs5 = s4.executeQuery("SELECT date(max(created_on)) FROM crman_valid_messages where center_id = "+CenterID);
		        	if (rs5.first()){
		        		LastDate = rs5.getDate(1);
		        	}
			        
		        	
		        	
		        	PdfPTable table2 = new PdfPTable(2);
		        	table2.setWidthPercentage(100f);
		        	table2.getDefaultCell().setBorderColor(BaseColor.WHITE);
		        	
		        	ResultSet rs6 = s4.executeQuery("SELECT rate, center_type FROM crman_valid_messages where center_id = "+CenterID+" and created_on between "+Utilities.getSQLDate(LastDate)+" and "+Utilities.getSQLDateNext(LastDate)+" group by rate, center_type");
		        	while (rs6.next()){
			        	table2.addCell(getNormalCellRate(Math.round(rs6.getDouble(1))+""));
			        	table2.addCell(getNormalCellComp(rs6.getString(2)));
		        	}
			        
			        table.addCell(table2);
			        
			        table.addCell(getNormalCell(Utilities.getDisplayDateFormat(LastDate)));
		        }
		        
		        
	    	}
		    
		    document.add(table);
		    document.add(Chunk.NEWLINE);
		    
		    document.newPage();
	    }
	    
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
	private PdfPCell getHeadingCellHigh(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fonttableheader));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.YELLOW);
        pcell.setBackgroundColor(BaseColor.YELLOW);
        pcell.setMinimumHeight(16);
        return pcell;
	}
	private PdfPCell getNormalCell(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        return pcell;
	}
	private PdfPCell getNormalCellRate(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.WHITE);
        return pcell;
	}
	private PdfPCell getNormalCellComp(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setBorderColor(BaseColor.WHITE);
        return pcell;
	}
	private PdfPCell getNormalCellStrikeRate(String title){
		
		int StrikeRate = Utilities.parseInt(title);
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title+"%", fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        
        if (StrikeRate < 30){
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
        
        if (DropSize < 3){
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
	private PdfPCell getNormalCellRightAligned(String title){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        return pcell;
	}
	

	
}
