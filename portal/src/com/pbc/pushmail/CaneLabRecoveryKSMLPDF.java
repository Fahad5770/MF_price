package com.pbc.pushmail;

import java.io.FileOutputStream;
import java.io.IOException;
 









import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
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
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class CaneLabRecoveryKSMLPDF {

	public static final String RESULT = Utilities.getEmailAttachmentsPath()+ "/CaneLabRecoveryKSML.pdf";
	
	Font fonttableheader = FontFactory.getFont("Arial", 8, Font.BOLD);
	Font fontheading = FontFactory.getFont("Arial", 8, Font.NORMAL);
	Font fontpbc = FontFactory.getFont("Arial", 10, Font.BOLD);
	Font fonttitle = FontFactory.getFont("Arial", 10, Font.BOLD);
	Font fonttitleblack = FontFactory.getFont("Arial", 10, Font.BOLD);
	Font reportheading = FontFactory.getFont("Arial", 8, Font.BOLD);
	
	
	Datasource ds = new Datasource();
	Statement s;
	Statement s2;
	Statement s3;
	Statement s4;
	
	
	Date StartDate = new Date();
	Date EndDate = new Date();
	
	
	Date Yesterday = Utilities.getDateByDays(0);
	
	
	long SND_ID = 0;
	
	
	public static void main(String[] args) throws DocumentException, IOException{
		
		try {
			//new DistributorScoreCardPDF().createPdf(RESULT, this.SND_ID);
			CaneLabRecoveryKSMLPDF DistributorSD1 = new CaneLabRecoveryKSMLPDF();
			//DistributorSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/test"+Math.random()+".pdf", 2262,Yesterday);
			
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

	public static String getSQLDateLifting(java.util.Date val) {
		
		// Converts date into sql format and ignores time

		if (val != null) {

			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			return "date_format('" + format.format(val) + " 7:00','%Y-%m-%d %H:%i')";

		} else {

			return null;

		}

	}
	public static String getSQLDateNextLifting(java.util.Date val) {

		
		// Adds another day to Date and converts date into SQL format, ignores time
		if (val != null) {

			java.util.Date next = DateUtils.addDays(val, 1);
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			return "date_format('" + format.format(next) + " 7:00','%Y-%m-%d %H:%i')";

		} else {

			return null;

		}

	}
	
	public CaneLabRecoveryKSMLPDF() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		
		ds.createConnectionToKSMLLab();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();

		
		
	}
	
	public void createPdf(String filename, long SND_ID,Date d) throws DocumentException, IOException, SQLException {
		
		
		this.SND_ID = SND_ID;
		
		Document document = new Document(PageSize.A3.rotate());
	    PdfWriter.getInstance(document, new FileOutputStream(filename));
	    
	    document.open();
	    
	    PdfPTable table = new PdfPTable(16);
	    
	    table.setWidthPercentage(100f);
	    table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
	    
	    reportheading.setColor(BaseColor.WHITE);
	    fonttitle.setColor(BaseColor.WHITE);
	    
	    
		
	    int colcount=0;
		
       
        
		
		
	    
		
		
		
	    PdfPCell pcell = new PdfPCell(new Phrase("Recovery Report", fonttitle));
        pcell.setBackgroundColor(BaseColor.BLUE);
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.BLUE);
        pcell.setColspan(16);
        pcell.setMinimumHeight(13);
        table.addCell(pcell);
        
        
        
        
       
        
       
      
        	/*
            table.addCell(getHeadingCellLevel2("bbbbbbb",2));
            table.addCell(getHeadingCellLevel2Center("Coke",2));
            table.addCell(getHeadingCellLevel2Center("Pepsi",2));
            table.addCell(getHeadingCellLevel2Center("Gourmet",2));
*/
            
            //Query 3 : Outlet
            //
            
            
            
            table.addCell(getHeadingCellCenter("Entry"));
            table.addCell(getHeadingCellCenter("Token#"));
            table.addCell(getHeadingCellCenter("Date"));
            table.addCell(getHeadingCellCenter("Passbook"));
            table.addCell(getHeadingCellCenter("Grower",4));
            table.addCell(getHeadingCellCenter("Truck"));
            table.addCell(getHeadingCellCenter("Zone"));
            table.addCell(getHeadingCellCenter("Circle"));
            table.addCell(getHeadingCellCenter("Center"));
            table.addCell(getHeadingCellCenter("Pol"));
            table.addCell(getHeadingCellCenter("Brix"));
            table.addCell(getHeadingCellCenter("Purity"));
            table.addCell(getHeadingCellCenter("Recovery"));
            
            
            ResultSet rs = s.executeQuery("select *, date(timestamp) token_date from cane_sample where created_on between "+Utilities.getSQLDateLifting(d)+" and "+Utilities.getSQLDateNextLifting(d)+" order by zone, circle, center");
    		while(rs.next()){
    			
    			table.addCell(getNormalCellCenter(rs.getString("created_on_date")));
    			table.addCell(getNormalCellCenter(rs.getString("token")));
    			table.addCell(getNormalCellCenter(rs.getString("token_date")));
    			table.addCell(getNormalCellCenter(rs.getString("passbook")));
    			table.addCell(getNormalCell2(rs.getString("grower_name")+" s/o "+rs.getString("grower_father_name"),4));
    			table.addCell(getNormalCellCenter(rs.getString("truck_no")));
    			table.addCell(getNormalCellCenter(rs.getString("zone")));
    			table.addCell(getNormalCellCenter(rs.getString("circle")));
    			table.addCell(getNormalCellCenter(rs.getString("center")));
    			table.addCell(getNormalCellCenter(rs.getString("pol")));
    			table.addCell(getNormalCellCenter(rs.getString("brix")));
    			table.addCell(getNormalCellCenter(rs.getString("purity")));
    			table.addCell(getNormalCellCenter(rs.getString("recovery")));
    			
    			
    		}
            
            
            
        
     
		
	 
		
		
		
		
        
        Paragraph header = new Paragraph("Period: "+Utilities.getDisplayDateFormat(new Date())+" - "+Utilities.getDisplayDateFormat(new Date()),fontheading);
        header.setAlignment(Element.ALIGN_RIGHT);
        
        Image img = Image.getInstance(String.format(Utilities.getImageResoucesPath()+"/"+"%s", "ksml_small_logo.jpg"));
       // img.scaleToFit(20, 20);
        //img.setTransparency(new int[]{ 0xF0, 0xFF });
        img.setAbsolutePosition(35, 788);
        
        document.add(img);
        //Paragraph pbc = new Paragraph("          Punjab Beverages",fontpbc);
        //document.add(pbc);
        
        
        Paragraph Heading = new Paragraph("Recovery Report",fontpbc);
        Heading.setAlignment(Element.ALIGN_CENTER);
	     document.add(Heading);
	    
        
	    document.add(header);
	    document.add(Chunk.NEWLINE);
	    
	    
	    
	    
	    document.add(table);
	    document.add(Chunk.NEWLINE);
	    
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
	
private PdfPCell getNormalCell2(String title,int colspan){
		
	    PdfPCell pcell = new PdfPCell(new Phrase(title, fontheading));
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setBorderColor(BaseColor.LIGHT_GRAY);
        pcell.setColspan(colspan);
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
