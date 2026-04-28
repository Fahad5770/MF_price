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

public class MarketWatchSourcePDF {

	public static final String RESULT = Utilities.getEmailAttachmentsPath()+ "/CashInflowSummary.pdf";
	
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
	
	
	Date Yesterday = Utilities.getDateByDays(-1);
	
	
	long SND_ID = 0;
	
	
	public static void main(String[] args) throws DocumentException, IOException{
		
		try {
			//new DistributorScoreCardPDF().createPdf(RESULT, this.SND_ID);
			MarketWatchSourcePDF DistributorSD1 = new MarketWatchSourcePDF();
			DistributorSD1.createPdf(Utilities.getEmailAttachmentsPath()+ "/test"+Math.random()+".pdf", 2262);
			
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
	
	public MarketWatchSourcePDF() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		
		
		
		ds.createConnection();
		s = ds.createStatement();
		s2 = ds.createStatement();
		s3 = ds.createStatement();
		s4 = ds.createStatement();

		if (Utilities.getDayOfWeekByDate(Yesterday) == 6){
			Yesterday = Utilities.getDateByDays(-2);
		}
		
	}
	
	public void createPdf(String filename, long SND_ID) throws DocumentException, IOException, SQLException {
		
		
		this.SND_ID = SND_ID;
		
		Document document = new Document(PageSize.A3.rotate());
	    PdfWriter.getInstance(document, new FileOutputStream(filename));
	    
	    document.open();
	    
	    PdfPTable table = new PdfPTable(8);
	    
	    table.setWidthPercentage(100f);
	    table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
	    
	    reportheading.setColor(BaseColor.WHITE);
	    fonttitle.setColor(BaseColor.WHITE);
	    
	    
		long TotalZeroSaleOutlets = 0;
		long ZeroSaleOutletsToDisplay = 0;
	    
		

       
        
		
		
	    
		ResultSet rs = s.executeQuery("SELECT * FROM inventory_packages where id in(SELECT distinct package_id  FROM crm_market_watch_rates)");
		while(rs.next()){
		
		
	    PdfPCell pcell = new PdfPCell(new Phrase(rs.getString("label"), fonttitle));
        pcell.setBackgroundColor(BaseColor.BLUE);
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pcell.setBorderColor(BaseColor.BLUE);
        pcell.setColspan(8);
        pcell.setMinimumHeight(22);
        table.addCell(pcell);
        
        
        
        
        long TotalTOTInjected = 0;
        long TotalTOTInjected2015 = 0;
        long TotalTOTInjected2014 = 0;
        long TotalTOTInjected2013 = 0;
        
        ResultSet rs1 = s2.executeQuery("SELECT distinct distributor_id,(select name from common_distributors cd where cd.distributor_id=cmw.distributor_id) distributor_name FROM crm_market_watch cmw where cmw.created_on between curdate() and from_days(to_days(curdate())+1)");
        while(rs1.next()){
        
        String DistributorLabel = rs1.getLong("distributor_id")+" - "+rs1.getString("distributor_name");
        	long DistributorID = 1;
        	
            table.addCell(getHeadingCellLevel2(DistributorLabel,2));
            table.addCell(getHeadingCellLevel2Center("Coke",2));
            table.addCell(getHeadingCellLevel2Center("Pepsi",2));
            table.addCell(getHeadingCellLevel2Center("Cross Franchise",2));
            
            //Query 3 : Outlet
            //
            
            table.addCell(getHeadingCell("Outlet Name"));
            table.addCell(getHeadingCell("Address"));
            table.addCell(getHeadingCellCenter("Rate"));
            table.addCell(getHeadingCellCenter("Comments"));
            table.addCell(getHeadingCellCenter("Rate"));
            table.addCell(getHeadingCellCenter("Comments"));
            table.addCell(getHeadingCellCenter("Rate"));
            table.addCell(getHeadingCellCenter("Comments"));
            
            
            ResultSet rs2 = s3.executeQuery("SELECT cmw.outlet_id,cmw.outlet_name,cmw.outlet_address , (select rate from crm_market_watch_rates where id=cmw.id and company_id=1 and package_id="+rs.getLong("id")+") pepsi,(select promotion_remarks from crm_market_watch_rates where id=cmw.id and company_id=1 and package_id="+rs.getLong("id")+") pepsi_remarks,(select rate from crm_market_watch_rates where id=cmw.id and company_id=2 and package_id="+rs.getLong("id")+") coke,(select promotion_remarks from crm_market_watch_rates where id=cmw.id and company_id=2 and package_id="+rs.getLong("id")+") coke_remarks,(select rate from crm_market_watch_rates where id=cmw.id and company_id=3 and package_id="+rs.getLong("id")+") gourment,(select promotion_remarks from crm_market_watch_rates where id=cmw.id and company_id=3 and package_id="+rs.getLong("id")+") gourment_remarks FROM crm_market_watch cmw where cmw.created_on between curdate() and from_days(to_days(curdate())+1) and cmw.distributor_id="+rs1.getLong("distributor_id"));
            while(rs2.next()){
            
           		String coke="-";
           		String pepsi="-";
           		String gourment="-";
            	
            	
            	if(rs2.getInt("coke")!=0){
            		 coke=rs2.getInt("coke")+"";
            	}
            	if(rs2.getInt("pepsi")!=0){
            		 pepsi=rs2.getInt("pepsi")+"";
            	}
            	if(rs2.getInt("gourment")!=0){
            		 gourment=rs2.getInt("gourment")+"";
            	}
            	
            	String OutlettID="";
            	if(rs2.getLong("outlet_id")==0){
            		OutlettID="";
            	}else{
            		OutlettID = rs2.getLong("outlet_id")+" - ";
            	}
            	
            	if (rs2.getInt("coke")!=0 || rs2.getInt("pepsi")!=0 || rs2.getInt("gourment")!=0){
            		
            		
			        table.addCell(getNormalCell(OutlettID+rs2.getString("outlet_name")));
			        table.addCell(getNormalCell(rs2.getString("outlet_address")));
			        table.addCell(getNormalCellCenter(coke));
			        table.addCell(getNormalCell(rs2.getString("coke_remarks")));
			        table.addCell(getNormalCellCenter(pepsi));
			        table.addCell(getNormalCell(rs2.getString("pepsi_remarks")));
			        table.addCell(getNormalCellCenter(gourment));
			        table.addCell(getNormalCell(rs2.getString("gourment_remarks"))); 
		        
            	}
            }//end of outlet loop
        
        }//end of distributor loop
		
		}//end of package loop 
		
		
		
		
        
        Paragraph header = new Paragraph("Period: "+Utilities.getDisplayDateFormat(new Date())+" - "+Utilities.getDisplayDateFormat(new Date()),fontheading);
        header.setAlignment(Element.ALIGN_RIGHT);
        
        Image img = Image.getInstance(String.format(Utilities.getImageResoucesPath()+"/"+"%s", "pepsi28.png"));
        img.scaleToFit(20, 20);
        //img.setTransparency(new int[]{ 0xF0, 0xFF });
        img.setAbsolutePosition(35, 788);
        
        document.add(img);
        Paragraph pbc = new Paragraph("          Punjab Beverages",fontpbc);
        document.add(pbc);
        
        
        Paragraph Heading = new Paragraph("Market Watch",fontpbc);
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
